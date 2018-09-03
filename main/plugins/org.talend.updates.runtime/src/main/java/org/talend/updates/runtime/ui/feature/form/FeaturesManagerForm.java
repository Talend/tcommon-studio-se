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
import org.talend.updates.runtime.i18n.Messages;
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
        tabFolder.setSelection(0);
        onSwitchTab();
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
