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
package org.talend.updates.runtime.ui.feature.form.item;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.model.InstallationStatus;
import org.talend.updates.runtime.ui.feature.model.IFeatureDetail;
import org.talend.updates.runtime.ui.feature.model.runtime.FeaturesManagerRuntimeData;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class FeatureListInstallItem extends AbstractFeatureListInfoItem<IFeatureDetail> {

    private Button installButton;

    public FeatureListInstallItem(Composite parent, int style, FeaturesManagerRuntimeData runtimeData, IFeatureDetail element) {
        super(parent, style, runtimeData, element);
    }

    @Override
    protected Composite createInstallationPanel(Composite panel) {
        Composite installationPanel = new Composite(panel, SWT.NONE);

        FormLayout formLayout = new FormLayout();
        installationPanel.setLayout(formLayout);

        installButton = new Button(installationPanel, SWT.NONE);
        installButton.setText(Messages.getString("ComponentsManager.form.install.label.install")); //$NON-NLS-1$
        installButton.setFont(getInstallButtonFont());

        return installationPanel;
    }

    @Override
    protected void layoutControl() {
        super.layoutControl();
        FormData formData = null;

        formData = new FormData();
        formData.bottom = new FormAttachment(100, 0);
        formData.right = new FormAttachment(100, 0);
        installButton.setLayoutData(formData);
    }

    @Override
    protected void addListeners() {
        super.addListeners();
        installButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                onInstallButtonClicked(e);
            }
        });
    }

    private void onInstallButtonClicked(SelectionEvent e) {
        executeInstall(null, true);
        getRuntimeData().recheckUpdate();
        getCheckListener().updateButtons();
    }

    @Override
    public void executeInstall(IProgressMonitor monitor, boolean runInModelContext) {
        super.executeInstall(monitor, runInModelContext);
    }

    @Override
    protected void preInstall(IProgressMonitor monitor) throws Exception {
        super.preInstall(monitor);
        Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {
                if (!installButton.isDisposed()) {
                    installButton.setText(Messages.getString("ComponentsManager.form.install.label.installing")); //$NON-NLS-1$
                    installButton.setEnabled(false);
                    getInstallationPanel().layout();
                }
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        loadData();
    }

    private void loadData() {
        // nothing to do
    }

    @Override
    public void reload() {
        super.reload();
        installButton.setText(Messages.getString("ComponentsManager.form.install.label.checking")); //$NON-NLS-1$
        installButton.setEnabled(false);
        getInstallationPanel().layout();
        execute(new Runnable() {

            @Override
            public void run() {
                checkInstallation();
            }
        });
    }

    @Override
    protected void execute(Runnable runnable) {
        getRuntimeData().getFeaturesManager().getSearchThreadPoolExecutor().execute(runnable);
    }

    @Override
    protected void updateInstallationButtons(InstallationStatus installationStatus) {
        installButton.setText(getInstallationButtonLabel(installationStatus));
        installButton.setEnabled(installationStatus.canBeInstalled());
    }
}
