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
package org.talend.core.repository.seeker;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.talend.core.model.utils.RepositoryManagerHelper;
import org.talend.core.repository.model.ProjectRepositoryNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public final class RepositorySeekerManager {

    private final RepositorySeekerRegistryReader seekerReader;

    private static RepositorySeekerManager instance;

    public static RepositorySeekerManager getInstance() {
        if (instance == null) {
            instance = new RepositorySeekerManager();
        }
        return instance;
    }

    private RepositorySeekerManager() {
        this.seekerReader = new RepositorySeekerRegistryReader();
    }

    public RepositorySeekerRegistryReader getSeekerReader() {
        return this.seekerReader;
    }

    public final TreeViewer getRepoTreeViewer() {
        TreeViewer treeViewer = null;
        // find the repository view
        final IRepositoryView repositoryView = RepositoryManagerHelper.findRepositoryView();
        if (repositoryView != null) {
            StructuredViewer viewer = repositoryView.getViewer();
            if (viewer instanceof TreeViewer) {
                treeViewer = (TreeViewer) viewer;
            }
        }
        return treeViewer;
    }

    public IRepositoryNode searchRepoViewNode(String itemId) {
        return searchRepoViewNode(itemId, true);
    }

    public IRepositoryNode searchRepoViewNode(String itemId, boolean expand) {
        return searchRepoViewNode(itemId, expand, 1);
    }

    /**
     * This work for the repository view of Integration and Mediation
     */
    @SuppressWarnings("deprecation")
    public IRepositoryNode searchRepoViewNode(String itemId, boolean expand, int expandLevel) {
        final TreeViewer repoTreeViewer = getRepoTreeViewer();

        IRepositorySeeker<IRepositoryNode>[] allSeeker = getSeekerReader().getAllSeeker();
        for (IRepositorySeeker<IRepositoryNode> seeker : allSeeker) {
            IRepositoryNode searchedNode = seeker.searchNode(repoTreeViewer, itemId);
            if (searchedNode != null) {
                if (seeker.neededExpand()) {
                    seeker.expandNode(repoTreeViewer, searchedNode, expandLevel);
                }
                return searchedNode;
            }
        }
        /*
         * if not found, use the old one.
         * 
         * later, will use the seeker always.
         */
        return RepositoryNodeUtilities.getRepositoryNode(itemId, expand);
    }

    public void expandNode(TreeViewer viewer, IRepositoryNode repoNode, int expandLevel) {
        if (repoNode != null && viewer != null) {
            RepositoryNode parent = repoNode.getParent();
            if (parent != null) { // when repoNode is ProjectRepositoryNode in main project, the parent will be null.
                if (parent instanceof ProjectRepositoryNode && parent.getParent() == null) {
                    // the parent is main project, nothing to do
                } else {
                    expandNode(viewer, parent, expandLevel);
                }
                viewer.expandToLevel(repoNode, expandLevel);
            }
        }

    }
}
