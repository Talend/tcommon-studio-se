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
package org.talend.rcp.intro;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.rcp.i18n.Messages;
import org.talend.repository.ui.wizards.newproject.copyfromeclipse.TalendImportExportWizard;

/**
 * ggu class global comment. Detailled comment
 */
public class ExportCommandAction extends Action {

    private static final int SIZING_WIZARD_WIDTH = 470;

    private static final int SIZING_WIZARD_HEIGHT = 550;

    private IWorkbenchAction exportAction;

    /**
     * Constants from org.eclipse.ui.internal.IWorkbenchHelpContextIds
     */
    public class IWorkbenchHelpContextIds {

        public static final String EXPORT_ACTION = PlatformUI.PLUGIN_ID + "." + "export_action_context"; //$NON-NLS-1$ //$NON-NLS-2$

        public static final String EXPORT_WIZARD = PlatformUI.PLUGIN_ID + "." + "export_wizard_context"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     */
    public ExportCommandAction(IWorkbenchWindow window) {
        exportAction = ActionFactory.EXPORT.create(window);
        setId("talend_" + exportAction.getId()); //$NON-NLS-1$
        setText(exportAction.getText());
        setToolTipText(exportAction.getToolTipText());
        setImageDescriptor(exportAction.getImageDescriptor());
        window.getWorkbench().getHelpSystem().setHelp(this, IWorkbenchHelpContextIds.EXPORT_ACTION);
    }

    @Override
    public void run() {
        // qli comment
        // see the bug "0005942",the IRunnableWithProgress can't run on the linux.so the Job is used on here.
        if (exportAction != null) {
            Job job = new Job(Messages.getString("ExportCommandAction.refreshWorkspace")) { //$NON-NLS-1$

                @Override
                protected IStatus run(IProgressMonitor monitor) {
                    monitor.beginTask(ExportCommandAction.this.getToolTipText(), IProgressMonitor.UNKNOWN);
                    try {
                        ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, null);
                    } catch (CoreException e) {
                        e.printStackTrace();
                    }
                    monitor.done();
                    return Status.OK_STATUS;
                }
            };
            job.setUser(true);
            job.setPriority(Job.BUILD);
            job.schedule();

            // TalendImportExportWizard wizard = new TalendImportExportWizard(ImportExportWizard.EXPORT);
            TalendImportExportWizard wizard = new TalendImportExportWizard(TalendImportExportWizard.EXPORT);
            IStructuredSelection selectionToPass = StructuredSelection.EMPTY;

            IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
            wizard.init(activeWorkbenchWindow.getWorkbench(), selectionToPass);
            // IDialogSettings workbenchSettings = WorkbenchPlugin.getDefault().getDialogSettings();
            IDialogSettings workbenchSettings = CoreUIPlugin.getDefault().getDialogSettings();
            IDialogSettings wizardSettings = workbenchSettings.getSection("ImportExportAction"); //$NON-NLS-1$
            if (wizardSettings == null) {
                wizardSettings = workbenchSettings.addNewSection("ImportExportAction"); //$NON-NLS-1$
            }
            wizard.setDialogSettings(wizardSettings);
            wizard.setForcePreviousAndNextButtons(true);

            Shell parent = activeWorkbenchWindow.getShell();
            WizardDialog dialog = new WizardDialog(parent, wizard);
            dialog.create();
            dialog.getShell().setSize(Math.max(SIZING_WIZARD_WIDTH, dialog.getShell().getSize().x), SIZING_WIZARD_HEIGHT);
            activeWorkbenchWindow.getWorkbench().getHelpSystem()
                    .setHelp(dialog.getShell(), IWorkbenchHelpContextIds.EXPORT_WIZARD);
            dialog.open();
            // call system export
            // exportAction.run();
        }
    }

}
