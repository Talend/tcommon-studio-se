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
package org.talend.repository.viewer.ui.viewer;

import java.util.Iterator;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.internal.navigator.NavigatorContentService;
import org.eclipse.ui.internal.navigator.NavigatorDecoratingLabelProvider;
import org.eclipse.ui.navigator.CommonViewerSorter;
import org.eclipse.ui.navigator.INavigatorContentService;
import org.eclipse.ui.navigator.INavigatorPipelineService;
import org.eclipse.ui.navigator.PipelinedViewerUpdate;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ui.ITestContainerProviderService;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.viewer.ui.provider.INavigatorContentServiceProvider;

/**
 * DOC ggu class global comment. Detailled comment
 */
@SuppressWarnings("restriction")
public class CheckboxRepoCommonViewer extends CheckboxRepositoryTreeViewer implements INavigatorContentServiceProvider {

    private final NavigatorContentService contentService;

    public CheckboxRepoCommonViewer(String viewerId, Composite parent, int style) {
        super(parent, style);
        contentService = new NavigatorContentService(viewerId, this);
        init();
    }

    @Override
    public INavigatorContentService getNavigatorContentService() {
        return this.contentService;
    }

    protected void init() {
        setUseHashlookup(true);
        setContentProvider(contentService.createCommonContentProvider());
        setLabelProvider(new NavigatorDecoratingLabelProvider(contentService.createCommonLabelProvider()));
    }

    public void dispose() {
        if (contentService != null) {
            contentService.dispose();
        }
    }

    @Override
    public void setSorter(ViewerSorter sorter) {
        if (sorter != null && sorter instanceof CommonViewerSorter) {
            ((CommonViewerSorter) sorter).setContentService(contentService);
        }

        super.setSorter(sorter);
    }

    @Override
    public void refresh(Object element, boolean updateLabels) {

        if (element != getInput()) {
            INavigatorPipelineService pipeDream = contentService.getPipelineService();

            PipelinedViewerUpdate update = new PipelinedViewerUpdate();
            update.getRefreshTargets().add(element);
            update.setUpdateLabels(updateLabels);
            /* if the update is modified */
            if (pipeDream.interceptRefresh(update)) {
                /* intercept and apply the update */
                boolean toUpdateLabels = update.isUpdateLabels();
                for (Iterator iter = update.getRefreshTargets().iterator(); iter.hasNext();) {
                    super.refresh(iter.next(), toUpdateLabels);
                }
            } else {
                super.refresh(element, updateLabels);
            }
        } else {
            super.refresh(element, updateLabels);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.Viewer#setSelection(org.eclipse.jface.viewers.ISelection, boolean)
     */
    @Override
    public void setSelection(ISelection selection, boolean reveal) {

        if (selection instanceof IStructuredSelection) {
            IStructuredSelection sSelection = (IStructuredSelection) selection;

            INavigatorPipelineService pipeDream = contentService.getPipelineService();

            PipelinedViewerUpdate update = new PipelinedViewerUpdate();
            update.getRefreshTargets().addAll(sSelection.toList());
            update.setUpdateLabels(false);
            /* if the update is modified */
            if (pipeDream.interceptRefresh(update)) {
                /* intercept and apply the update */
                super.setSelection(new StructuredSelection(update.getRefreshTargets().toArray()), reveal);
            } else {
                super.setSelection(selection, reveal);
            }
        }
    }

    public void doUpdateItem(Widget item) {
        doUpdateItem(item, item.getData(), true);
    }

    @Override
    public void refresh(Object element) {
        refresh(element, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.StructuredViewer#update(java.lang.Object, java.lang.String[])
     */
    @Override
    public void update(Object element, String[] properties) {
        if (element != getInput()) {
            INavigatorPipelineService pipeDream = contentService.getPipelineService();

            PipelinedViewerUpdate update = new PipelinedViewerUpdate();
            update.getRefreshTargets().add(element);
            update.setUpdateLabels(true);
            /* if the update is modified */
            if (pipeDream.interceptUpdate(update)) {
                /* intercept and apply the update */
                for (Iterator iter = update.getRefreshTargets().iterator(); iter.hasNext();) {
                    super.update(iter.next(), properties);
                }
            } else {
                super.update(element, properties);
            }
        } else {
            super.update(element, properties);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.dialogs.ContainerCheckedTreeViewer#doCheckStateChanged(java.lang.Object)
     */
    @Override
    protected void doCheckStateChanged(Object element) {
        boolean isTestCase = false;
        if (element instanceof IRepositoryNode) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class)) {
                ITestContainerProviderService testContainerService = (ITestContainerProviderService) GlobalServiceRegister
                        .getDefault().getService(ITestContainerProviderService.class);
                if (testContainerService != null) {
                    if (((IRepositoryNode) element).getObjectType() != null
                            && testContainerService.isTestContainerType(((IRepositoryNode) element).getObjectType())) {
                        isTestCase = true;
                    }
                }
            }
        }
        Widget item = findItem(element);
        if (item instanceof TreeItem) {
            if (!((TreeItem) item).getChecked() && isTestCase) {
                TreeItem treeItem = (TreeItem) item;
                treeItem.setGrayed(false);
                return;
            }
        }
        super.doCheckStateChanged(element);
    }
}
