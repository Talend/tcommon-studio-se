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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Item;
import org.talend.core.repository.model.ProjectRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.nodes.IProjectRepositoryNode;
import org.talend.repository.navigator.RepoViewCommonNavigator;
import org.talend.repository.navigator.RepoViewCommonViewer;
import org.talend.repository.viewer.content.listener.ResourceCollectorVisitor;

/**
 * this handle content that root node is of type ProjectRepositoryNode
 * */
public abstract class FolderListenerSingleTopContentProvider extends SingleTopLevelContentProvider {

    /*
     * map used to store the folder path related to a top level node in order to cache it for performance purposes the
     * map value may be null
     */
    Map<RepositoryNode, IPath> topLevelNodeToPathMap = new HashMap<RepositoryNode, IPath>();

    public FolderListenerSingleTopContentProvider() {
        super();
    }

    private final class DirectChildrenNodeVisitor extends ResourceCollectorVisitor {

        /* (non-Javadoc)
         * @see org.talend.repository.viewer.content.listener.ResourceCollectorVisitor#getTopLevelNodes()
         */
        @Override
        protected Set<RepositoryNode> getTopNodes() {
            return getTopLevelNodes();
        }

        /* (non-Javadoc)
         * @see org.talend.repository.viewer.content.listener.ResourceCollectorVisitor#getTopLevelNodePath(org.talend.repository.model.RepositoryNode)
         */
        @Override
        protected IPath getTopLevelNodePath(RepositoryNode repoNode) {
            return topLevelNodeToPathMap.get(repoNode);
        }

    }

    /*
     * stores the folder workspace relative path related to the topLevel node into a map.
     * 
     * @see
     * org.talend.repository.viewer.content.SingleTopLevelContentProvider#initilizeContentProvider(org.talend.repository
     * .model.RepositoryNode)
     */
    @Override
    protected void initilizeContentProviderWithTopLevelNode(RepositoryNode aTopLevelNode) {
        super.initilizeContentProviderWithTopLevelNode(aTopLevelNode);
        if (!topLevelNodeToPathMap.containsKey(aTopLevelNode)) {
            topLevelNodeToPathMap.put(aTopLevelNode, getWorkspaceTopNodePath(aTopLevelNode));
        }

    }

    /**
     * This calls the refresh of the toplevel node, this must be invoke from the UI thread.
     * 
     * @param repoNode
     * 
     * @param topLevelNode
     */
    protected void refreshTopLevelNode(RepositoryNode repoNode) {
        Project project = repoNode.getRoot().getProject();

        ProjectRepositoryNode projectNode = ProjectRepositoryNode.getInstance();
        Project mainProject = projectNode.getProject();

        RepositoryNode testNode = repoNode;
        // if ref-node and merged, force to refresh the related node for main project.
        if (projectNode.getMergeRefProject() && !mainProject.getTechnicalLabel().equals(project.getTechnicalLabel())) {
            // find the related node in main project
            if (testNode.isBin()) {
                testNode = projectNode.getRecBinNode();
            } else {
                testNode = projectNode.getRootRepositoryNode(testNode.getContentType());
            }
        }
        if (testNode == null) {
            return;
        }
        getTopLevelNodes();
        resetTopLevelNode(testNode);

        beforeRefreshTopLevelNode(testNode);

        // for bug 11786
        if (testNode.getParent() instanceof ProjectRepositoryNode) {
            ((ProjectRepositoryNode) testNode.getParent()).clearNodeAndProjectCash();
        }
        if (viewer != null && !viewer.getTree().isDisposed()) {
            viewer.refresh(testNode);
        }
    }

    protected void beforeRefreshTopLevelNode(RepositoryNode repoNode) {
        // do nothing here
    }

    protected IPath getWorkspaceTopNodePath(RepositoryNode topLevelNode) {
        IPath workspaceRelativePath = null;
        IProjectRepositoryNode root = topLevelNode.getRoot();
        if (root != null) {
            String projectName = root.getProject().getTechnicalLabel();
            String topLevelNodeProjectRelativePath = getTopLevelNodeProjectRelativePath(topLevelNode);
            if (projectName != null && (topLevelNodeProjectRelativePath != null && !"".equals(topLevelNodeProjectRelativePath))) { //$NON-NLS-1$
                workspaceRelativePath = Path.fromPortableString('/' + projectName).append(topLevelNodeProjectRelativePath);
            }
        }
        return workspaceRelativePath;
    }

    protected boolean isLinkedTopNode(RepositoryNode topLevelNode, Item item) {
        return false;
    }

    /**
     * DOC sgandon Comment method "getTopLevelNodeProjectRelativePath".
     * 
     * @param topLevelNode
     * 
     * @return the relative path to the project or null if none
     */
    protected String getTopLevelNodeProjectRelativePath(RepositoryNode topLevelNode) {
        if (topLevelNode != null && topLevelNode.getContentType() != null) {
            return topLevelNode.getContentType().getFolder();
        }// else return null
        return null;
    }

    protected CommonViewer viewer;

    private DirectChildrenNodeVisitor visitor;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
        if (arg0 instanceof CommonViewer) {
            this.viewer = (CommonViewer) arg0;
        }
        // FIXME by TDI-22701, If the input model has been changed, so need clean
        super.inputChanged(arg0, arg1, arg2);
        topLevelNodeToPathMap.clear();

        addResourceVisitor(this.viewer);

    }

    /**
     * 
     * DOC ggu Comment method "addResourceVisitor".
     * 
     * @param v
     */
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

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IContentProvider#dispose()
     */
    @Override
    public void dispose() {
        // visitor
        if (this.viewer != null && this.visitor != null && this.viewer instanceof RepoViewCommonViewer) {
            final Control control = this.viewer.getControl();
            if (control != null && !control.isDisposed()) {
                CommonNavigator commonNavigator = ((RepoViewCommonViewer) this.viewer).getCommonNavigator();
                if (commonNavigator instanceof RepoViewCommonNavigator) {
                    ((RepoViewCommonNavigator) commonNavigator).removeVisitor(this.visitor);
                    this.visitor = null;
                }
            }
        }

        clearCache();
        super.dispose();
    }

    public void clearCache() {
        // to help garbage collection
        topLevelNodeToPathMap.clear();
    }
}
