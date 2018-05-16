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
package org.talend.designer.maven.ui.setting.repository;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.navigator.CommonViewer;
import org.talend.core.model.utils.RepositoryManagerHelper;
import org.talend.designer.maven.ui.setting.repository.registry.MavenSettingPagesRegistryReader;
import org.talend.designer.maven.ui.setting.repository.tester.IRepositorySettingTester;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.navigator.RepoViewCommonNavigator;
import org.talend.repository.ui.views.IRepositoryView;
import org.talend.repository.viewer.filter.PerspectiveFilterHelper;
import org.talend.repository.viewer.filter.RepositoryNodeFilterHelper;
import org.talend.repository.viewer.ui.provider.RepoCommonViewerProvider;

/**
 * DOC ggu class global comment. Detailled comment
 * 
 * the path is splitted via "." by default
 */
public class RepositoryMavenSettingManager extends PreferenceManager {

    public final static MavenSettingPagesRegistryReader REGISTRY = new MavenSettingPagesRegistryReader();

    public RepositoryMavenSettingManager() {
        super();
    }

    public void init(TreeViewer treeViewer) {
        // from tree item
        TreeItem[] items = treeViewer.getTree().getItems();
        createNodes(this.getRoot(), (ILabelProvider) treeViewer.getLabelProvider(), items);

    }

    public void init(String viewId) {
        if (viewId == null) {
            return;
        }

        Composite parent = null;
        try {
            // create on fake shell for parent of viewer
            parent = new Shell();

            RepositoryContextTypesFilter filter = new RepositoryContextTypesFilter(
                    Arrays.asList(RepositoryMavenSettingManager.REGISTRY.getSupportTypes()));

            RepoCommonViewerProvider provider = new RepoCommonViewerProvider() {

                protected void doPerspecitiveFilterForCommonViewer(TreeViewer treeViewer, String specifiedPerspectiveId) {
                    // don't filter for perspective, so the active false first.
                    boolean oldValue = PerspectiveFilterHelper.isActivedPerspectiveFilter();
                    PerspectiveFilterHelper.setActivedPerspectiveFilter(false);
                    try {
                        // call do filter to make sure it disable the perspective filter.
                        super.doPerspecitiveFilterForCommonViewer(treeViewer, specifiedPerspectiveId);
                    } finally {
                        PerspectiveFilterHelper.setActivedPerspectiveFilter(oldValue);
                    }
                }
            };
            provider.setViewId(viewId);
            TreeViewer fakeTreeViewer = provider.createViewer(parent);
            fakeTreeViewer.addFilter(filter);
            // FIXME, maybe should enhance the performance more here to init the nodes, when expand the preference
            // node.
            fakeTreeViewer.expandAll(); // in order to load all nodes
            init(fakeTreeViewer);
        } finally {
            if (parent != null) {
                parent.dispose();
            }
            // need enable the perspective filter.
            IRepositoryView repoView = RepositoryManagerHelper.findRepositoryView();
            if (repoView != null && repoView instanceof RepoViewCommonNavigator) { // if the repository is displayed.
                CommonViewer commonViewer = ((RepoViewCommonNavigator) repoView).getCommonViewer();
                RepositoryNodeFilterHelper.filter(commonViewer, RepositoryNodeFilterHelper.isActivedFilter(),
                        PerspectiveFilterHelper.isActivedPerspectiveFilter());
            }
        }
    }

    private void createNodes(IPreferenceNode parentNode, ILabelProvider labelProvider, TreeItem[] items) {
        for (TreeItem item : items) {
            Object data = item.getData();
            if (data instanceof RepositoryNode) {
                RepositoryNode node = ((RepositoryNode) data);

                // use set to make sure there is no duplicated nodes.
                Set<IPreferenceNode> folderNodesSet = new LinkedHashSet<IPreferenceNode>();
                RepositoryMavenSetting[] settings = REGISTRY.getSettings();
                for (RepositoryMavenSetting setting : settings) {
                    IRepositorySettingTester tester = setting.getTester();
                    if (tester != null && tester.valid(node)) {
                        IPreferenceNode mavenFolderNode = setting.createAndFindMavenFolderNode(parentNode, labelProvider, node);
                        if (mavenFolderNode != null) {
                            if (setting.needMavenScripts(node)) { // create the maven scripts children nodes
                                setting.createMavenScriptsChildren(mavenFolderNode, node);
                            }
                            folderNodesSet.add(mavenFolderNode);
                        }
                    }
                }

                // sub folders
                for (IPreferenceNode folderNode : folderNodesSet) {
                    createNodes(folderNode, labelProvider, item.getItems());
                }
            }
        }
    }

}
