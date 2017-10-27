// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.viewer.content.listener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.TalendNature;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.model.ProjectRepositoryNode;
import org.talend.core.repository.utils.XmiResourceManager;
import org.talend.core.runtime.services.IGenericDBService;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.viewer.content.VisitResourceHelper;
import org.talend.utils.string.StringUtilities;

/**
 * created by nrousseau on Jun 26, 2016 Detailled comment
 *
 */
public abstract class ResourceCollectorVisitor implements IResourceDeltaVisitor {

    private Collection<ResourceNode> resourcesCollections;

    public void setRunnables(Collection<ResourceNode> resourcesCollections) {
        this.resourcesCollections = resourcesCollections;
    }

    @Override
    public boolean visit(IResourceDelta delta) throws CoreException {
        return visit(delta, resourcesCollections);
    }

    protected abstract Set<RepositoryNode> getTopNodes();

    protected abstract IPath getTopLevelNodePath(RepositoryNode repoNode);
    

    protected IPath getTopLevelNodePathForRefresh(RepositoryNode repoNode) {
        return getTopLevelNodePath(repoNode);
    }

    protected boolean isValidResourceDelta(IResourceDelta delta) {
        return getTopNodeFromResourceDelta(delta) != null;
    }

    protected IRepositoryNode getTopNodeFromResourceDelta(IResourceDelta delta) {
        VisitResourceHelper visitHelper = new VisitResourceHelper(delta);
        boolean merged = ProjectRepositoryNode.getInstance().getMergeRefProject();
        Set<RepositoryNode> topLevelNodes = getTopNodes();
        for (final RepositoryNode repoNode : topLevelNodes) {
            IPath topLevelNodeWorkspaceRelativePath = getTopLevelNodePath(repoNode);
            if (topLevelNodeWorkspaceRelativePath != null && visitHelper.valid(topLevelNodeWorkspaceRelativePath, merged)) {
                return findTopNode(repoNode);
            }
        }
        // this visitor doesn't handle the current folder
        return null;
    }
    
    private IRepositoryNode findTopNode(IRepositoryNode repoNode){
        List<ERepositoryObjectType> extraTypes = new ArrayList<ERepositoryObjectType>();
        IGenericDBService dbService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IGenericDBService.class)) {
            dbService = (IGenericDBService) GlobalServiceRegister.getDefault().getService(
                    IGenericDBService.class);
        }
        if(dbService != null){
            extraTypes.addAll(dbService.getExtraTypes());
        }
        
        if(repoNode.getContentType() != null && extraTypes.contains(repoNode.getContentType())){
            RepositoryNode dbRootNode = (RepositoryNode) repoNode.getRoot().getRootRepositoryNode(
                    ERepositoryObjectType.METADATA_CONNECTIONS);
            if(dbRootNode != null){
                return dbRootNode;
            }
        }
        return repoNode;
    }

    protected boolean visit(IResourceDelta delta, Collection<ResourceNode> pathToRefresh) {
        IResource resource = delta.getResource();
        if (resource.getType() == IResource.ROOT) {
            return true;
        }
        // if not under talend project. ignore
        try {
            if (resource.getType() == IResource.PROJECT) {
                if (resource.getProject().isOpen() && resource.getProject().hasNature(TalendNature.ID)) {
                    return true;
                }
                return false; // if not talend project, ignore.
            }
        } catch (CoreException e) {
            ExceptionHandler.process(e);
            return false;
        }
        if (resource.getType() == IResource.FOLDER) {
            // if under svn folder, ignore
            if (FilesUtils.isSVNFolder(resource)) {
                return false;
            }
            if (!isValidResourceDelta(delta)) {
                return false;
            }
            if (delta.getKind() == IResourceDelta.ADDED) {
                // new folder added, need to refresh the parent.
                ResourceNode resourceNode = getParentResourceNode(delta, resource);
                pathToRefresh.add(resourceNode);
                return false;
            } else if (delta.getKind() == IResourceDelta.REMOVED) {
                // folder deleted, need to refresh the parent.
                ResourceNode resourceNode = getParentResourceNode(delta, resource);
                pathToRefresh.add(resourceNode);
                return false;
            }
            // not a folder added or removed, so we must check in the childrens
            return true;
        }

        if (resource.getType() == IResource.FILE) {
            if (!isValidResourceDelta(delta)) {
                return false;
            }
            IRepositoryNode topNode = getTopNodeFromResourceDelta(delta);
            ResourceNode resourceNode = new ResourceNode();
            resourceNode.setPath(resource.getParent().getFullPath().toPortableString());
            resourceNode.setTopNode(topNode);
            resourceNode.setTopNodePath(getTopLevelNodePathForRefresh((RepositoryNode)topNode).toPortableString());
            if (delta.getKind() == IResourceDelta.ADDED) {
                // new file added, need to refresh the parent.
                pathToRefresh.add(resourceNode);
                ((RepositoryNode)topNode).setInitialized(false);
                topNode.getChildren().clear();
                return false;
            } else if (delta.getKind() == IResourceDelta.REMOVED) {
                // file deleted, need to refresh the parent.
                pathToRefresh.add(resourceNode);
                ((RepositoryNode)topNode).setInitialized(false);
                topNode.getChildren().clear();
                try {
                    String key = resource.getFullPath().toString();
                    key = StringUtilities.removeStartingString(key, "/"); //$NON-NLS-1$
                    ProjectRepositoryNode.getInstance().removeCache(key);
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
                return false;
            } else if (delta.getKind() == IResourceDelta.CHANGED) {
                // file modified, need to refresh the current file
                XmiResourceManager xrm = new XmiResourceManager();
                if (xrm.isPropertyFile((IFile) resource)) {
                    // no refresh needed if something else than a .properties is modified                    
                    pathToRefresh.add(resourceNode);
                    
//                    ResourceNode fileResourceNode = new ResourceNode();
//                    fileResourceNode.setPath(resource.getFullPath().toPortableString());
//                    fileResourceNode.setTopNode(topNode);
//                    pathToRefresh.add(fileResourceNode);
                    ((RepositoryNode)topNode).setInitialized(false);
                    topNode.getChildren().clear();

                }
                return false;
            }
        }

        return false;
    }

    /**
     * DOC nrousseau Comment method "getParentResourceNode".
     * @param delta
     * @param resource
     * @return
     */
    private ResourceNode getParentResourceNode(IResourceDelta delta, IResource resource) {
        IRepositoryNode topNode = getTopNodeFromResourceDelta(delta);
        ResourceNode resourceNode = new ResourceNode();
        resourceNode.setPath(resource.getParent().getFullPath().toPortableString());
        resourceNode.setTopNode(topNode);
        resourceNode.setTopNodePath(getTopLevelNodePathForRefresh((RepositoryNode)topNode).toPortableString());
        ((RepositoryNode)topNode).setInitialized(false);
        topNode.getChildren().clear();
        return resourceNode;
    }

}
