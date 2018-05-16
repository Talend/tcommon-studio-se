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
package org.talend.repository.items.importexport.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.GlobalServiceRegister;
import org.talend.repository.items.importexport.ui.handlers.IImportExportItemsActionHelper;
import org.talend.repository.items.importexport.ui.handlers.manager.ImportExportUiHandlersManager;
import org.talend.repository.items.importexport.ui.i18n.Messages;
import org.talend.repository.items.importexport.ui.wizard.imports.ImportItemsWizard;
import org.talend.repository.model.IProxyRepositoryService;
import org.talend.repository.ui.actions.AContextualAction;

/**
 */
public class ImportItemsAction extends AContextualAction implements IWorkbenchWindowActionDelegate {

    private IStructuredSelection structureSelection;

    public ImportItemsAction() {
        super();
        this.setText(Messages.getString("ImportItemsAction_title")); //$NON-NLS-1$
        this.setToolTipText(this.getText());
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.IMPORT_ICON));
    }

    @Override
    public void run(IAction action) {
        run();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.AContextualAction#getSelection()
     */
    @Override
    public IStructuredSelection getSelection() {
        return this.structureSelection;
    }

    @Override
    protected void doRun() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IProxyRepositoryService.class)) {
            IProxyRepositoryService service = (IProxyRepositoryService) GlobalServiceRegister.getDefault().getService(
                    IProxyRepositoryService.class);
            if (service.getProxyRepositoryFactory().isUserReadOnlyOnCurrentProject()) {
                return;
            }
        }
        if (getSelection() == null) {
            return;
        }

        ImportItemsWizard wizard = getNewImportItemsWizard();
        wizard.setWindowTitle(this.getText());
        wizard.init(PlatformUI.getWorkbench(), getSelection());

        WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), wizard);
        if (dialog.open() == Window.OK) {
            // if import for joblet, need update the palette
            // if (wizard.isNeedToRefreshPalette()) {
            // ComponentPaletteUtilities.updatePalette();
            // }

            // update routines
//            if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
//                IRunProcessService service = (IRunProcessService) GlobalServiceRegister.getDefault().getService(
//                        IRunProcessService.class);
//                ITalendProcessJavaProject talendProcessJavaProject = service.getTalendProcessJavaProject();
//                if (talendProcessJavaProject != null) {
//                    talendProcessJavaProject.updateRoutinesPom(true, true);
//                }
//            }
        }
    }

    protected ImportItemsWizard getNewImportItemsWizard() {
        return new ImportItemsWizard();
    }

    @Override
    public void init(IWorkbenchWindow window) {

    }

    @Override
    public void init(TreeViewer viewer, IStructuredSelection selection) {
        setSelection(selection);

        boolean enabled = false;
        IImportExportItemsActionHelper[] actionsHelpers = ImportExportUiHandlersManager.getInstance().getActionsHelpers();
        for (IImportExportItemsActionHelper helper : actionsHelpers) {
            // if one of helper return true, will enable the action.
            if (helper.isImportEnabled(viewer, selection)) {
                enabled = true;
                break;
            }
        }
        setEnabled(enabled);
    }

    @Override
    public void selectionChanged(IAction action, ISelection selection) {
        setSelection(selection);

        boolean enabled = false;
        IImportExportItemsActionHelper[] actionsHelpers = ImportExportUiHandlersManager.getInstance().getActionsHelpers();
        for (IImportExportItemsActionHelper helper : actionsHelpers) {
            // if one of helper return true, will enable the action.
            if (helper.isImportEnabled(action, selection)) {
                enabled = true;
                break;
            }
        }
        setEnabled(enabled);

    }

    private void setSelection(ISelection selection) {
        if (selection instanceof IStructuredSelection) {
            this.structureSelection = ((IStructuredSelection) selection);
        } else {
            this.structureSelection = null;
        }

    }

    @Override
    public void dispose() {
        //
    }
}
