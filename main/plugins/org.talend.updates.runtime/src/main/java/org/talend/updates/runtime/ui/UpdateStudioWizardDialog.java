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
package org.talend.updates.runtime.ui;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.wizard.ProgressMonitorPart;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.talend.updates.runtime.i18n.Messages;

/**
 * created by sgandon on 26 sept. 2013 Detailled comment
 * 
 */
class UpdateStudioWizardDialog extends WizardDialog {

    /**
     * DOC sgandon WizardDialogExtension constructor comment.
     * 
     * @param parentShell
     * @param newWizard
     * @param updateStudioWizard TODO
     * @param shell
     */
    UpdateStudioWizardDialog(UpdateStudioWizard updateStudioWizard, Shell parentShell) {
        super(parentShell, updateStudioWizard);
    }

    @Override
    protected org.eclipse.jface.wizard.ProgressMonitorPart createProgressMonitorPart(Composite parent,
            org.eclipse.swt.layout.GridLayout pmlayout) {
        final UpdateStudioWizard updateStudioWizard = (UpdateStudioWizard) getWizard();
        Composite checkAndProgressComposite = new Composite(parent, SWT.NONE);
        checkAndProgressComposite.setLayout(new GridLayout(3, false));
        GridDataFactory.fillDefaults().grab(true, false).applyTo(checkAndProgressComposite);
        // do not show this again check box
        updateStudioWizard.doNotShowAgainCB = new Button(checkAndProgressComposite, SWT.CHECK);
        updateStudioWizard.doNotShowAgainCB.setText(Messages.getString("UpdateStudioWizard.do.no.show.check.box.text")); //$NON-NLS-1$
        GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.CENTER).applyTo(updateStudioWizard.doNotShowAgainCB);
        updateStudioWizard.updateCbStateFromPref();
        // progress bar
        ProgressMonitorPart createProgressMonitorPart = super.createProgressMonitorPart(checkAndProgressComposite, pmlayout);
        // proxy configuration button
        Button proxyPrefBtn = new Button(checkAndProgressComposite, SWT.PUSH);
        proxyPrefBtn.setText(Messages.getString("UpdateStudioWizard.proxy.config.button.text")); //$NON-NLS-1$
        GridDataFactory.fillDefaults().align(SWT.END, SWT.END).applyTo(proxyPrefBtn);
        proxyPrefBtn.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(getShell(),
                        "org.eclipse.ui.net.NetPreferences", new String[] { "org.eclipse.ui.net.NetPreferences" }, null); //$NON-NLS-1$ //$NON-NLS-2$
                dialog.open();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
        // store the fact that the user has modified the value of the check box to enable the finish button
        updateStudioWizard.doNotShowAgainCB.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                updateStudioWizard.updateWizardModel.hasDoNotShowThisAgainChanged = true;
                updateButtons();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);

            }
        });
        return createProgressMonitorPart;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#create()
     */
    @Override
    public void create() {
        super.create();
        ((UpdateStudioWizard) getWizard()).launchInitialRunnable(this);
    }

}
