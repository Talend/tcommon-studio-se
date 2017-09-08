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
package org.talend.repository.viewer.ui.provider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreePathViewerSorter;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.utils.RepositoryManagerHelper;
import org.talend.core.repository.model.ProjectRepositoryNode;
import org.talend.core.repository.ui.utils.RecombineRepositoryNodeUtil;
import org.talend.core.repository.ui.view.RepositoryLabelProvider;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.nodes.IProjectRepositoryNode;
import org.talend.repository.navigator.RepoViewCommonNavigator;
import org.talend.repository.ui.views.IRepositoryView;
import org.talend.repository.viewer.ui.viewer.CheckboxRepositoryTreeViewer;

/**
 * ggu class global comment. Detailled comment
 */
public class RepositoryViewerProvider extends AbstractViewerProvider {

    private IRepositoryView realRepView;

    public RepositoryViewerProvider() {
    }

    protected IRepositoryView getRepView() {
        if (realRepView == null) {
            realRepView = RepositoryManagerHelper.findRepositoryView();
        }
        if (realRepView == null) {
            realRepView = RepositoryManagerHelper.getRepositoryView();
            if (realRepView != null && realRepView instanceof RepoViewCommonNavigator) {
                ((RepoViewCommonNavigator) realRepView).getCommonViewer().expandToLevel(2);
            }
        }
        return realRepView;
    }

    protected IStructuredContentProvider getContextProvider() {
        // can't reuse the content provider
        // final StructuredViewer viewer = getRepView().getViewer();
        // return (IStructuredContentProvider) viewer.getContentProvider();
        return new RepositoryContentProvider(getRepView());
    }

    protected ILabelProvider getLabelProvider() {
        // can't reuse the label provider
        // final StructuredViewer viewer = getRepView().getViewer();
        // return (ILabelProvider) viewer.getLabelProvider();
        return new RepositoryLabelProvider(getRepView());
    }

    protected ERepositoryObjectType getCheckingType() {
        return null;
    }

    protected List<ERepositoryObjectType> getCheckingTypes() {
        final ERepositoryObjectType checkingType = getCheckingType();
        List<ERepositoryObjectType> checkingTypes = new ArrayList<ERepositoryObjectType>();
        if (checkingType != null) {
            checkingTypes.add(checkingType);
        }
        return checkingTypes;
    }

    @Override
    protected void addProviders(TreeViewer treeViewer) {
        final IStructuredContentProvider contentProvider = getContextProvider();
        if (contentProvider != null) {
            treeViewer.setContentProvider(contentProvider);
        }
        final ILabelProvider labelProvider = getLabelProvider();
        if (labelProvider != null) {
            treeViewer.setLabelProvider(labelProvider);
        }
    }

    @Override
    protected void checkSorter(TreeViewer treeViewer) {
        ViewerSorter sorter = null;
        if (getRepView() != null) {
            final StructuredViewer viewer = getRepView().getViewer();
            sorter = viewer.getSorter();
        }
        final ViewerSorter viewerSorter = sorter;
        // TDI-20528
        // treeViewer.setSorter(sorter);
        treeViewer.setSorter(new TreePathViewerSorter() {

            @Override
            public int compare(Viewer viewer, TreePath parentPath, Object e1, Object e2) {
                if (e1 instanceof RepositoryNode && e2 instanceof RepositoryNode) {
                    final RepositoryNode node1 = (RepositoryNode) e1;
                    final RepositoryNode node2 = (RepositoryNode) e2;
                    if (node1.isBin() || node1.getType().equals(ENodeType.REFERENCED_PROJECT)) { // recycle bin is
                                                                                                 // always in bottom
                        return 1;
                    }
                    if (node2.isBin() || node2.getType().equals(ENodeType.REFERENCED_PROJECT)) { // recycle bin is
                                                                                                 // always in bottom
                        return -1;
                    }
                    // do special for simple folder,TDI-20528
                    if (node1.getType() == IRepositoryNode.ENodeType.SIMPLE_FOLDER
                            || node2.getType() == IRepositoryNode.ENodeType.SIMPLE_FOLDER) {
                        return e1.toString().compareTo(e2.toString());
                    } else if (viewerSorter != null) {
                        if (viewerSorter instanceof TreePathViewerSorter) {
                            if (!node1.isInitialized() && !node2.isInitialized()) {
                                return viewerSorter.compare(viewer, e1, e2);
                            }
                            return ((TreePathViewerSorter) viewerSorter).compare(viewer, parentPath, e1, e2);
                        } else {
                            return viewerSorter.compare(viewer, e1, e2);
                        }
                    }
                }
                return super.compare(viewer, parentPath, e1, e2);

            }

        });
    }

    @Override
    public IProjectRepositoryNode getProjectRepositoryNode() {
        if (getRepView() != null) { // in fact, they are same
            return getRepView().getRoot();
        } else {
            return ProjectRepositoryNode.getInstance();
        }
    }

    @Override
    protected TreeViewer createTreeViewer(final Composite parent, final int style) {
        return new CheckboxRepositoryTreeViewer(parent, style);
    }

    @Override
    protected IRepositoryNode getInputRoot(final IProjectRepositoryNode projectRepoNode) {
        return RecombineRepositoryNodeUtil.getFixingTypesInputRoot(projectRepoNode, getCheckingTypes());
    }

}
