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
package org.talend.designer.maven.ui.utils;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.model.StableRepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment
 */
public final class DesignerMavenUiHelper {

    /**
     * for the tree of maven setting ,need display the extension of pom, assembly or not.
     */
    public final static boolean displayWithExtension = true;

    /**
     * this option is more technical for tree nodes, and the link message, just make sure both use unify id.
     */
    public final static boolean idWithExtension = true;

    /**
     * only display the tree nodes with same type as selected node or not.
     * 
     * false, by default, display all support nodes.
     */
    public final static boolean onlyWithselectedType = false;

    /**
     * When have BD nodes, the "Job Designer" node is a fake node for process type now.
     * 
     */
    public static boolean isFakeProcessRootNode(RepositoryNode node) {
        ENodeType nodeType = node.getType();
        if (node.getContentType().equals(ERepositoryObjectType.PROCESS) && nodeType.equals(ENodeType.SYSTEM_FOLDER)) {
            List<IRepositoryNode> children = node.getChildren();
            if (!children.isEmpty()) {
                // have "Standard Job" node sometime
                for (IRepositoryNode child : children) {
                    // contained "Standard Job" node
                    if (isStandardJobNode(child)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 
     * FIXME, maybe this checking is not good.
     */
    private static boolean isStandardJobNode(IRepositoryNode node) {
        if (node.getContentType().equals(ERepositoryObjectType.PROCESS)
                && (node instanceof StableRepositoryNode || node.getType().equals(ENodeType.STABLE_SYSTEM_FOLDER))) {
            return true;
        }
        return false;
    }

    /**
     * Check the pom and assembly exsited under the node of disk or not.
     * 
     * FIXME, need check the node is in reference project, also with merge reference project option.
     */
    public static boolean existMavenSetting(RepositoryNode node, String... fileNames) {
        IFolder nodeFolder = getNodeFolder(node);
        return existMavenSetting(nodeFolder, fileNames);
    }

    public static boolean existMavenSetting(IFolder parentFolder, String... fileNames) {
        if (parentFolder == null) {
            return false;
        }
        boolean existed = false;

        try {
            parentFolder.refreshLocal(IResource.DEPTH_ONE, null);
        } catch (CoreException e) {
            ExceptionHandler.process(e);
        }

        if (fileNames == null || fileNames.length == 0) { // by default, check the pom.xml only
            IFile pomFile = parentFolder.getFile(TalendMavenConstants.POM_FILE_NAME);
            if (pomFile.exists()) {
                existed = true;
            }
        } else {
            existed = true;
            for (String name : fileNames) {
                if (!parentFolder.getFile(name).exists()) {
                    existed = false; // if one file is not existed, will return false
                    break;
                }
            }
        }

        return existed;
    }

    /**
     * 
     * get the absolute path for node.
     * 
     * for job, like <project absolute path>/process/...
     */
    public static IPath getNodeAbsolutePath(RepositoryNode node) throws PersistenceException {
        IFolder nodeFolder = getNodeFolder(node);
        return nodeFolder != null ? nodeFolder.getLocation() : null;
    }

    public static IFolder getNodeFolder(RepositoryNode node) {
        try {
            IPath projectRelativePath = getNodeProjectRelativePath(node);
            if (projectRelativePath != null) {
                IProject project = ResourceUtils.getProject(node.getRoot().getProject());
                IFolder parentFolder = project.getFolder(projectRelativePath);
                return parentFolder;
            }
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
        return null;
    }

    /**
     * 
     * get the node path in disk, it's relative path in project.
     * 
     * for job, like process/...
     */
    public static IPath getNodeProjectRelativePath(RepositoryNode node) {
        if (node == null) {
            return null;
        }
        ERepositoryObjectType contentType = node.getContentType();

        String path = RepositoryNodeUtilities.getPath(node).toString();
        IPath projectRelativePath = new Path(contentType.getFolder());
        if (path != null && path.length() > 0) {
            projectRelativePath = projectRelativePath.append(path);
        }
        return projectRelativePath;
    }

    /**
     * make sure all node id with parent id for unique. and unify the hyphen "-"
     */
    public static String buildRepositoryPreferenceNodeId(String parentId, String name) {
        if (parentId != null && parentId.length() > 0) {
            return parentId + '-' + name;
        }
        return name;
    }

    /**
     * only use the name of file, no need exstension.
     */
    public static String buildRepositoryPreferenceNodeId(String parentId, IFile file) {
        String name = null;
        if (idWithExtension) {
            name = file.getLocation().lastSegment();
        } else {
            name = file.getLocation().removeFileExtension().lastSegment();
        }
        return buildRepositoryPreferenceNodeId(parentId, name);
    }

}
