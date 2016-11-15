package org.talend.librariesmanager.ui.wizards;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.talend.core.model.general.ModuleToInstall;
import org.talend.librariesmanager.ui.LibManagerUiPlugin;
import org.talend.librariesmanager.ui.i18n.Messages;

public class AcceptModuleLicensesWizardDialog extends WizardDialog {

    private static final int ACCEPT_ALL_ID = 9999;

    private Button acceptAllButton;

    private List<ModuleToInstall> modulesToInstall;

    private IProgressMonitor monitor;

    public AcceptModuleLicensesWizardDialog(Shell parentShell, IWizard newWizard, List<ModuleToInstall> modulesToInstall,
            final IProgressMonitor monitor) {
        super(parentShell, newWizard);
        this.monitor = monitor;
        this.modulesToInstall = modulesToInstall;
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        acceptAllButton = createButton(parent, ACCEPT_ALL_ID,
                Messages.getString("AcceptModuleLicensesWizardDialog.button.acceptAll"), false); //$NON-NLS-1$
        acceptAllButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                acceptAllLicenses();
            }
        });
        super.createButtonsForButtonBar(parent);
    }

    private void acceptAllLicenses() {
        if (modulesToInstall != null && modulesToInstall.size() > 0) {
            for (ModuleToInstall module : modulesToInstall) {
                String licenseType = module.getLicenseType();
                if (licenseType != null) {
                    LibManagerUiPlugin.getDefault().getPreferenceStore().setValue(licenseType, true);
                }
            }
        }
        setReturnCode(Window.OK);
        close();
    }

    @Override
    protected void cancelPressed() {
        boolean canceled = MessageDialog.openQuestion(getShell(),
                Messages.getString("AcceptModuleLicensesWizardDialog.cancelConfirmation.title"), //$NON-NLS-1$
                Messages.getString("AcceptModuleLicensesWizardDialog.cancelConfirmation.msg")); //$NON-NLS-1$
        if (canceled) {
            setReturnCode(Window.CANCEL);
            monitor.setCanceled(true);
            close();
        } else {
            setReturnCode(Window.OK);
        }
    }

}
