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
import org.talend.updates.runtime.ui.feature.model.IFeatureUpdate;
import org.talend.updates.runtime.ui.feature.model.runtime.FeaturesManagerRuntimeData;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class FeatureListUpdateItem extends AbstractFeatureListInfoItem<IFeatureUpdate> {

    private Button updateButton;

    public FeatureListUpdateItem(Composite parent, int style, FeaturesManagerRuntimeData runtimeData, IFeatureUpdate element) {
        super(parent, style, runtimeData, element);
    }

    @Override
    protected Composite createInstallationPanel(Composite panel) {
        Composite installationPanel = new Composite(panel, SWT.NONE);

        FormLayout formLayout = new FormLayout();
        installationPanel.setLayout(formLayout);

        updateButton = new Button(installationPanel, SWT.NONE);
        updateButton.setText(Messages.getString("ComponentsManager.form.install.label.update")); //$NON-NLS-1$
        updateButton.setFont(getInstallButtonFont());

        return installationPanel;
    }

    @Override
    protected void layoutControl() {
        super.layoutControl();
        FormData formData = null;

        formData = new FormData();
        formData.bottom = new FormAttachment(100, 0);
        formData.right = new FormAttachment(100, 0);
        updateButton.setLayoutData(formData);
    }

    @Override
    protected void addListeners() {
        super.addListeners();
        updateButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                onUpdateButtonPressed(e);
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
        updateButton.setText(Messages.getString("ComponentsManager.form.updates.label.checking")); //$NON-NLS-1$
        updateButton.setEnabled(false);
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
        getRuntimeData().getFeaturesManager().getUpdateThreadPoolExecutor().execute(runnable);
    }

    private void onUpdateButtonPressed(SelectionEvent e) {
        executeUpdate(null, true);
        getRuntimeData().recheckUpdate();
        getCheckListener().updateButtons();
    }

    public void executeUpdate(IProgressMonitor monitor, boolean runInModelContext) {
        super.executeInstall(monitor, runInModelContext);
    }

    @Override
    protected void preInstall(IProgressMonitor monitor) throws Exception {
        super.preInstall(monitor);
        if (!updateButton.isDisposed()) {
            Display.getDefault().syncExec(new Runnable() {

                @Override
                public void run() {
                    updateButton.setText(Messages.getString("ComponentsManager.form.updates.label.updating")); //$NON-NLS-1$
                    updateButton.setEnabled(false);
                    getInstallationPanel().layout();
                }
            });
        }
    }

    @Override
    protected void updateInstallationButtons(InstallationStatus installationStatus) {
        updateButton.setText(getInstallationButtonLabel(installationStatus));
        updateButton.setEnabled(installationStatus.canBeInstalled());
    }
}
