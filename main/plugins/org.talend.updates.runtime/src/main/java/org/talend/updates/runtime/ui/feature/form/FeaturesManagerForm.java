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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.exception.ExceptionMessageDialog;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.ui.feature.form.FeaturesUpdatesNotificationForm.AbstractNotificationButtonListener;
import org.talend.updates.runtime.ui.feature.model.runtime.FeaturesManagerRuntimeData;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class FeaturesManagerForm extends AbstractFeatureForm {

    private CTabFolder tabFolder;

    private CTabItem searchTabItem;

    private CTabItem updateTabItem;

    public FeaturesManagerForm(Composite parent, int style, FeaturesManagerRuntimeData runtimeData) {
        super(parent, style, runtimeData);
    }

    @Override
    protected void init() {
        FormLayout layout = new FormLayout();
        this.setLayout(layout);
        addTabFolder();
        String firstShowTab = getRuntimeData().getFirstShowTab();
        if (FeaturesManagerRuntimeData.TAB_INSTALLATION.equalsIgnoreCase(firstShowTab)) {
            tabFolder.setSelection(0);
        } else if (FeaturesManagerRuntimeData.TAB_UPDATE.equalsIgnoreCase(firstShowTab)) {
            tabFolder.setSelection(1);
        } else {
            tabFolder.setSelection(0);
        }
        onSwitchTab();
        initData();
        addListeners();
    }

    private void addTabFolder() {
        tabFolder = new CTabFolder(this, SWT.BORDER);

        tabFolder.setTabPosition(SWT.TOP);
        tabFolder.setSelectionBackground(Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        FormData tabFolderFormData = new FormData();
        tabFolderFormData.left = new FormAttachment(0);
        tabFolderFormData.right = new FormAttachment(100);
        tabFolderFormData.top = new FormAttachment(0);
        tabFolderFormData.bottom = new FormAttachment(100);
        tabFolder.setLayoutData(tabFolderFormData);

        FillLayout tFolderLayout = new FillLayout();
        tabFolder.setLayout(tFolderLayout);

        addSearchTab();
        addInstalledTab();
    }

    private void addSearchTab() {
        searchTabItem = new CTabItem(tabFolder, SWT.NONE);
        searchTabItem.setText(Messages.getString("ComponentsManager.tab.label.search")); //$NON-NLS-1$
        FeaturesSearchForm searchForm = new FeaturesSearchForm(tabFolder, SWT.NONE, getRuntimeData());
        searchTabItem.setControl(searchForm);
    }

    private void addInstalledTab() {
        updateTabItem = new CTabItem(tabFolder, SWT.NONE);
        updateTabItem.setText(Messages.getString("ComponentsManager.tab.label.update")); //$NON-NLS-1$
        FeaturesUpdatesForm installedForm = new FeaturesUpdatesForm(tabFolder, SWT.NONE, getRuntimeData());
        updateTabItem.setControl(installedForm);
    }

    @Override
    protected void addListeners() {
        tabFolder.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                onSwitchTab();
            }
        });
    }

    private void switch2UpdateTab() {
        tabFolder.setSelection(1);
        tabFolder.layout();
        onSwitchTab();
    }

    @Override
    protected void initData() {
        getRuntimeData().setUpdateNotificationButtonListener(new AbstractNotificationButtonListener() {

            @Override
            public void onInstallUpdatesButtonClicked(SelectionEvent e, FeaturesUpdatesNotificationForm form) {
                try {
                    Exception ex[] = new Exception[1];
                    getRuntimeData().getCheckListener().run(true, true, new IRunnableWithProgress() {

                        @Override
                        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                            try {
                                installUpdates(monitor, getRuntimeData(), form);
                            } catch (Exception e) {
                                ex[0] = e;
                            }
                        }
                    });
                    if (ex[0] != null) {
                        throw ex[0];
                    }
                } catch (Exception ex) {
                    ExceptionHandler.process(ex);
                    ExceptionMessageDialog.openError(getShell(),
                            Messages.getString("ComponentsManager.form.updates.notification.execute.exception.title"), //$NON-NLS-1$
                            Messages.getString("ComponentsManager.form.updates.notification.execute.exception.description"), ex); //$NON-NLS-1$
                }
            }

            @Override
            public void onShowUpdatesButtonClicked(SelectionEvent e, FeaturesUpdatesNotificationForm form) {
                switch2UpdateTab();
            }

            @Override
            public void close() {
                getRuntimeData().getDialog().close();
            }
        });
    }

    @Override
    public boolean canFinish() {
        boolean canFinish = true;

        /**
         * MUST check all tabs, since the canFinish method may trigger some essential actions
         */
        Control searchCtrl = searchTabItem.getControl();
        if (searchCtrl instanceof AbstractFeatureForm) {
            if (!((AbstractFeatureForm) searchCtrl).canFinish()) {
                canFinish = false;
            }
        }
        Control updateCtrl = updateTabItem.getControl();
        if (updateCtrl instanceof AbstractFeatureForm) {
            if (!((AbstractFeatureForm) updateCtrl).canFinish()) {
                canFinish = false;
            }
        }
        if (!canFinish) {
            return canFinish;
        }
        return super.canFinish();
    }

    private void onSwitchTab() {
        AbstractFeatureForm form = (AbstractFeatureForm) tabFolder.getSelection().getControl();
        form.onTabSelected();
    }
}
