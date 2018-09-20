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
package org.talend.updates.runtime.ui.feature.form;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.operation.ModalContext;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.swt.listviewer.ControlListItem;
import org.talend.updates.runtime.feature.FeaturesManager.SearchOption;
import org.talend.updates.runtime.feature.FeaturesManager.SearchResult;
import org.talend.updates.runtime.feature.model.Category;
import org.talend.updates.runtime.feature.model.Type;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.ui.feature.form.item.AbstractControlListItem;
import org.talend.updates.runtime.ui.feature.form.item.FeatureListUpdateItem;
import org.talend.updates.runtime.ui.feature.form.item.FeatureListViewer;
import org.talend.updates.runtime.ui.feature.job.FeaturesCheckUpdateJob;
import org.talend.updates.runtime.ui.feature.model.IFeatureItem;
import org.talend.updates.runtime.ui.feature.model.Message;
import org.talend.updates.runtime.ui.feature.model.impl.FeatureProgress;
import org.talend.updates.runtime.ui.feature.model.impl.FeatureTitle;
import org.talend.updates.runtime.ui.feature.model.impl.ModelAdapter;
import org.talend.updates.runtime.ui.feature.model.runtime.FeaturesManagerRuntimeData;
import org.talend.updates.runtime.ui.util.UIUtils;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class FeaturesUpdatesForm extends AbstractFeatureForm {

    private FeatureListViewer featureListViewer;

    private Button updateAllButton;

    private Collection<IFeatureItem> selectedItems;

    private FeaturesCheckUpdateJob cachedUpdateJob;

    private boolean firstShow;

    public FeaturesUpdatesForm(Composite parent, int style, FeaturesManagerRuntimeData runtimeData) {
        super(parent, style, runtimeData);
    }

    @Override
    protected void initControl(Composite parent) {
        super.initControl(parent);
        featureListViewer = new FeatureListViewer(parent, getRuntimeData(), SWT.BORDER);
        featureListViewer.setContentProvider(ArrayContentProvider.getInstance());
        updateAllButton = new Button(parent, SWT.NONE);
        updateAllButton.setText(Messages.getString("ComponentsManager.form.updates.label.updateAll")); //$NON-NLS-1$
        updateAllButton.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        updateAllButton.setFont(getInstallButtonFont());
    }

    @Override
    protected void initLayout() {
        super.initLayout();
        FormData formData = null;
        final int verticalAlignHeight = getVerticalAlignHeight();

        formData = new FormData();
        formData.top = new FormAttachment(0, 0);
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(100, 0);
        formData.bottom = new FormAttachment(updateAllButton, -1 * verticalAlignHeight, SWT.TOP);
        featureListViewer.getControl().setLayoutData(formData);

        updateAllButton.pack();
        formData = new FormData();
        formData.bottom = new FormAttachment(100, 0);
        formData.right = new FormAttachment(featureListViewer.getControl(), 0, SWT.RIGHT);
        formData.width = updateAllButton.getSize().x + getHorizonAlignWidth() * 2;
        updateAllButton.setLayoutData(formData);
    }

    @Override
    protected void initData() {
        super.initData();
        firstShow = true;
        selectedItems = new LinkedList<>();
    }

    @Override
    public void onTabSelected() {
        super.onTabSelected();
        boolean doSearch = false;
        if (firstShow) {
            firstShow = false;
            doSearch = true;
        }
        FeaturesCheckUpdateJob checkUpdateJob = getRuntimeData().getCheckUpdateJob();
        if (cachedUpdateJob != checkUpdateJob) {
            cachedUpdateJob = checkUpdateJob;
            doSearch = true;
        }
        if (doSearch) {
            doSearch();
        }
    }

    @Override
    protected void addListeners() {
        super.addListeners();
        updateAllButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                onUpdateAllButtonPressed(e);
            }
        });
    }

    @Override
    public boolean canFinish() {
        checkStatus();
        return super.canFinish();
    }

    private void checkStatus() {
        updateItemsStatus();
        checkUpdateAllButton();
    }

    private void checkUpdateAllButton() {
        Object input = featureListViewer.getInput();
        boolean allInstalled = true;
        if (input instanceof Collection) {
            try {
                IProgressMonitor monitor = new NullProgressMonitor();
                for (Object obj : (Collection) input) {
                    if (obj instanceof IFeatureItem) {
                        ExtraFeature feature = ((IFeatureItem) obj).getFeature();
                        if (feature != null) {
                            if (feature.canBeInstalled(monitor)) {
                                allInstalled = false;
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
        if (allInstalled) {
            updateAllButton.setEnabled(false);
        } else {
            updateAllButton.setEnabled(true);
        }
    }

    private void updateItemsStatus() {
        if (featureListViewer.getControl().isDisposed()) {
            return;
        }
        Collection<ControlListItem<?>> controlListItems = featureListViewer.getControlListItems();
        if (controlListItems != null) {
            for (ControlListItem<?> controlListItem : controlListItems) {
                if (controlListItem instanceof AbstractControlListItem) {
                    ((AbstractControlListItem) controlListItem).reload();
                }
            }
        }
    }

    private void onUpdateAllButtonPressed(SelectionEvent e) {
        try {
            installSelectedItems();
        } catch (Exception ex) {
            ExceptionHandler.process(ex);
        }
    }

    private void installSelectedItems() throws Exception {
        getRuntimeData().getCheckListener().run(true, true, new IRunnableWithProgress() {

            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                installSelectedItems(monitor);
            }
        });
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                canFinish();
            }
        });
    }

    private void installSelectedItems(IProgressMonitor monitor) {
        boolean firstShow = true;
        for (IFeatureItem featureItem : selectedItems) {
            if (firstShow) {
                firstShow = false;
                getRuntimeData().setCheckWarnDialog(true);
            } else {
                getRuntimeData().setCheckWarnDialog(false);
            }
            installFeature(monitor, featureItem);
        }
    }

    private void installFeature(IProgressMonitor monitor, IFeatureItem featureItem) {
        try {
            final ControlListItem<?>[] uiItem = new ControlListItem<?>[1];
            Display.getDefault().syncExec(new Runnable() {

                @Override
                public void run() {
                    uiItem[0] = featureListViewer.doFindItem(featureItem);
                }
            });
            ExtraFeature feature = featureItem.getFeature();
            if (feature != null) {
                if (uiItem[0] instanceof FeatureListUpdateItem) {
                    ((FeatureListUpdateItem) uiItem[0]).executeUpdate(monitor, false);
                } else {
                    IStatus status = feature.install(monitor, new ArrayList<>());
                    if (status != null) {
                        switch (status.getSeverity()) {
                        case IStatus.OK:
                        case IStatus.INFO:
                        case IStatus.WARNING:
                            getRuntimeData().getInstalledFeatures().add(feature);
                            break;
                        default:
                            throw new Exception(status.getMessage(), status.getException());
                        }
                    }
                }
                getRuntimeData().getInstalledFeatures().add(feature);
            }
        } catch (Exception ex) {
            ExceptionHandler.process(ex);
        }
    }

    private void doSearch() {
        SearchOption searchOption = new SearchOption(Type.ALL, Category.ALL, ""); //$NON-NLS-1$
        searchOption.setPage(0);
        searchOption.setPageSize(6);
        doSearch(searchOption);
    }

    private void doSearch(SearchOption searchOption) {
        final FeatureProgress progress = showProgress();
        execute(new Runnable() {

            @Override
            public void run() {
                final Thread thread = Thread.currentThread();
                try {
                    if (thread.isInterrupted()) {
                        return;
                    }
                    ModalContext.run(new IRunnableWithProgress() {

                        @Override
                        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                            doSearch(monitor, searchOption);
                        }
                    }, true, progress.getProgressMonitor(), getDisplay());
                } catch (Exception e1) {
                    ExceptionHandler.process(e1);
                }
            }
        });
    }

    private void doSearch(IProgressMonitor monitor, SearchOption searchOption) {
        monitor.beginTask(Messages.getString("ComponentsManager.form.updates.label.progress.begin"), IProgressMonitor.UNKNOWN); //$NON-NLS-1$
        selectedItems.clear();
        List<IFeatureItem> features = new ArrayList<>();
        Collection<IFeatureItem> featureItems = Collections.EMPTY_SET;
        SearchResult searchResult = null;
        FeaturesCheckUpdateJob checkUpdateJob = getRuntimeData().getCheckUpdateJob();
        try {
            while (true) {
                if (checkUpdateJob.isFinished()) {
                    break;
                }
                UIUtils.checkMonitor(monitor);
                Thread.sleep(50);
            }
            Exception exception = checkUpdateJob.getException();
            if (exception != null) {
                throw exception;
            }
            searchResult = checkUpdateJob.getSearchResult();
            Collection<ExtraFeature> allComponentFeatures = searchResult.getCurrentPageResult();
            allComponentFeatures = checkFeatures(allComponentFeatures);
            featureItems = ModelAdapter.convert(allComponentFeatures, true);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }

        if (featureItems == null) {
            featureItems = Collections.EMPTY_SET;
        }

        boolean showTitleBar = true;

        String titleMsg = ""; //$NON-NLS-1$
        Collection<Message> detailMessages = new ArrayList<>();
        if (featureItems.isEmpty()) {
            titleMsg = Messages.getString("ComponentsManager.form.updates.label.head.searchResult.empty"); //$NON-NLS-1$
        } else {
            titleMsg = Messages.getString("ComponentsManager.form.updates.label.head.featured"); //$NON-NLS-1$
            detailMessages = getRuntimeData().getFeaturesManager().createDefaultMessage();
        }

        if (showTitleBar) {
            FeatureTitle title = new FeatureTitle();
            title.setTitle(titleMsg);
            title.setMessages(detailMessages);
            features.add(title);
        }

        features.addAll(featureItems);
        selectedItems.addAll(featureItems);

        if (monitor.isCanceled()) {
            return;
        }
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                if (monitor.isCanceled()) {
                    return;
                }
                if (!featureListViewer.getControl().isDisposed()) {
                    featureListViewer.setInput(features);
                }
                checkStatus();
            }
        });
    }

    private FeatureProgress showProgress() {
        List<IFeatureItem> progressList = new ArrayList<>();
        final FeatureProgress progress = new FeatureProgress();
        progressList.add(progress);
        featureListViewer.setInput(progressList);
        return progress;
    }

    private void execute(Runnable run) {
        getRuntimeData().getFeaturesManager().getUpdateThreadPoolExecutor().execute(run);
    }
}
