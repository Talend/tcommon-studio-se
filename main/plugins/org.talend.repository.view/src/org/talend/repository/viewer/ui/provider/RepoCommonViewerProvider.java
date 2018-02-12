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
package org.talend.repository.viewer.ui.provider;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonViewerSorter;
import org.eclipse.ui.navigator.INavigatorContentService;
import org.eclipse.ui.navigator.INavigatorFilterService;
import org.talend.repository.ui.views.IRepositoryView;
import org.talend.repository.view.sorter.IRepositoryNodeSorter;
import org.talend.repository.view.sorter.RepositoryNodeSorterRegister;
import org.talend.repository.viewer.filter.PerspectiveFilterActionProvider;
import org.talend.repository.viewer.filter.PerspectiveFilterHelper;
import org.talend.repository.viewer.ui.viewer.CheckboxRepoCommonViewer;
import org.talend.repository.viewer.ui.viewer.RepoCommonViewer;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class RepoCommonViewerProvider extends AbstractViewerProvider {

    public static final RepoCommonViewerProvider NORMAL = new RepoCommonViewerProvider();

    public static final RepoCommonViewerProvider CHECKBOX = new RepoCommonViewerProvider() {

        @Override
        protected TreeViewer createTreeViewer(Composite parent, int style) {
            return new CheckboxRepoCommonViewer(getViewId(), parent, style) {

                @Override
                protected Object[] getSortedChildren(Object parentElementOrTreePath) {
                    Object[] children = super.getSortedChildren(parentElementOrTreePath);
                    sortChildren(this, parentElementOrTreePath, children);
                    return children;
                }
            };
        }

    };

    private String baseViewId = null;

    public TreeViewer createViewer(Composite parent, String specifiedPerspectiveId) {
        TreeViewer treeViewer = super.createViewer(parent);
        doFilterForCommonViewer(treeViewer, specifiedPerspectiveId);
        return treeViewer;
    }

    @Override
    public TreeViewer createViewer(Composite parent) {
        return createViewer(parent, null);
    }

    @Override
    protected TreeViewer createTreeViewer(final Composite parent, final int style) {
        return new RepoCommonViewer(getViewId(), parent, style) {

            @Override
            protected Object[] getSortedChildren(Object parentElementOrTreePath) {
                Object[] children = super.getSortedChildren(parentElementOrTreePath);
                sortChildren(this, parentElementOrTreePath, children);
                return children;
            }
        };
    }

    @Override
    protected void checkSorter(TreeViewer treeViewer) {
        treeViewer.setSorter(new CommonViewerSorter());
    }

    protected void doFilterForCommonViewer(TreeViewer treeViewer) {
        doFilterForCommonViewer(treeViewer, null);
    }

    protected void doFilterForCommonViewer(TreeViewer treeViewer, String specifiedPerspectiveId) {
        if (treeViewer instanceof INavigatorContentServiceProvider) {
            INavigatorContentService navigatorContentService = ((INavigatorContentServiceProvider) treeViewer)
                    .getNavigatorContentService();

            // perspecitive filter
            doPerspecitiveFilterForCommonViewer(treeViewer, specifiedPerspectiveId);

            // filers
            INavigatorFilterService filterService = navigatorContentService.getFilterService();
            ViewerFilter[] visibleFilters = filterService.getVisibleFilters(true);
            for (ViewerFilter visibleFilter : visibleFilters) {
                treeViewer.addFilter(visibleFilter);
            }
        }
    }

    protected void doPerspecitiveFilterForCommonViewer(TreeViewer treeViewer, String specifiedPerspectiveId) {
        if (treeViewer instanceof INavigatorContentServiceProvider) {
            INavigatorContentService navigatorContentService = ((INavigatorContentServiceProvider) treeViewer)
                    .getNavigatorContentService();
            // perspecitive filter
            IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
            if (activeWorkbenchWindow != null) {
                IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
                if (activePage != null) {
                    PerspectiveFilterHelper helper = new PerspectiveFilterHelper();
                    helper.setTreeViewer(treeViewer);
                    helper.setNavigatorContentService(navigatorContentService);
                    helper.setActionProviderId(PerspectiveFilterActionProvider.ID);

                    String perspectiveId = specifiedPerspectiveId == null ? activePage.getPerspective().getId()
                            : specifiedPerspectiveId;
                    helper.doFiltering(perspectiveId);

                }
            }
        }
    }

    protected String getViewId() {
        if (baseViewId != null && !"".equals(baseViewId)) {
            return baseViewId;
        }
        return IRepositoryView.VIEW_ID;
    }

    public void setViewId(String id) {
        this.baseViewId = id;
    }

    protected void sortChildren(Viewer viewer, Object parentElementOrTreePath, Object[] children) {
        if (parentElementOrTreePath == null || children == null || children.length < 2) {
            return;
        }
        // do special sorter for repository
        IRepositoryNodeSorter[] sorters = RepositoryNodeSorterRegister.getInstance().getSorters();
        if (sorters != null) {
            for (IRepositoryNodeSorter sorter : sorters) {
                sorter.sort(viewer, parentElementOrTreePath, children);
            }
        }
    }

}
