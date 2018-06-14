// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.viewer.content;

import java.util.Collection;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.general.TalendNature;
import org.talend.core.repository.constants.FileConstants;
import org.talend.core.repository.model.ProjectRepositoryNode;
import org.talend.core.repository.recyclebin.RecycleBinManager;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.BinRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.navigator.RepoViewCommonNavigator;
import org.talend.repository.navigator.RepoViewCommonViewer;
import org.talend.repository.viewer.content.listener.RunnableResourceVisitor;

public class RecycleBinContentProvider extends ProjectRepoDirectChildrenNodeContentProvider {

    BinRepositoryNode binRepositoryNode;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.viewer.content.ProjectRepoChildrenNodeContentProvider#getTopLevelNodeFromProjectRepositoryNode
     * (org.talend.repository.model.ProjectRepositoryNode)
     */
    @Override
    protected RepositoryNode getTopLevelNodeFromProjectRepositoryNode(ProjectRepositoryNode projectRepositoryNode) {
        if (binRepositoryNode == null) {
            binRepositoryNode = new BinRepositoryNode(projectRepositoryNode);
        }
        return binRepositoryNode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.viewer.content.SingleTopLevelContentProvider#getChildren(java.lang.Object)
     */
    @Override
    public Object[] getChildren(Object element) {
        if (element instanceof BinRepositoryNode) {
            ((BinRepositoryNode) element).getChildren().clear();
            ProjectRepositoryNode.getInstance().buildDeletedItemsTree((RepositoryNode) element);
            return ((BinRepositoryNode) element).getChildren().toArray(
                    new RepositoryNode[((BinRepositoryNode) element).getChildren().size()]);
        }
        return super.getChildren(element);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.viewer.content.FolderListenerSingleTopContentProvider#addResourceVisitor(org.eclipse.ui
     * .navigator.CommonViewer)
     */
    @Override
    protected void addResourceVisitor(CommonViewer v) {
        if (v == null) {
            return;
        }
        RepoViewCommonNavigator navigator = null;
        if (v instanceof RepoViewCommonViewer) {
            CommonNavigator commonNavigator = ((RepoViewCommonViewer) v).getCommonNavigator();
            if (commonNavigator instanceof RepoViewCommonNavigator) {
                navigator = ((RepoViewCommonNavigator) commonNavigator);
            }
        }
        if (navigator == null) {
            return;
        }
        //
        if (this.visitor != null) {
            navigator.removeVisitor(this.visitor);
        }
        this.visitor = new DirectChildrenNodeVisitor();
        navigator.addVisitor(this.visitor);
    }

    private DirectChildrenNodeVisitor visitor;

    /**
     * DOC sgandon class global comment. Detailled comment <br/>
     * 
     * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
     * 
     */
    private final class DirectChildrenNodeVisitor extends RunnableResourceVisitor {

        @Override
        protected boolean visit(IResourceDelta delta, Collection<Runnable> runnables) {
            IResource resource = delta.getResource();
            if (resource.getType() == IResource.ROOT) {
                return true;
            } else if (resource.getType() == IResource.PROJECT) {
                if (!resource.getProject().isOpen()) {
                    return false;
                }
                try {
                    if (resource.getProject().hasNature(TalendNature.ID)) {
                        if (!ProjectManager.getInstance().getCurrentProject().getTechnicalLabel()
                                .equals(resource.getProject().getName())) {
                            return false;
                        }
                        return true; // if not talend project, ignore.
                    }
                } catch (CoreException e) {
                    ExceptionHandler.process(e);
                }
            } else if (resource.getType() == IResource.FOLDER) {
                return false;
            } else if (resource.getType() == IResource.FILE) {
                if (resource.getName().equals(FileConstants.TALEND_RECYCLE_BIN_INDEX)
                        || resource.getName().equals(FileConstants.LOCAL_PROJECT_FILENAME)) {
                    if (viewer != null && viewer.getControl() != null && !viewer.getTree().isDisposed()) {
                        viewer.getControl().getDisplay().asyncExec(new Runnable() {

                            @Override
                            public void run() {
                                viewer.refresh(binRepositoryNode);
                            }
                        });
                    }// else the viewer as not control so not displayed
                }
            }
            return false;
        }
    }
}
