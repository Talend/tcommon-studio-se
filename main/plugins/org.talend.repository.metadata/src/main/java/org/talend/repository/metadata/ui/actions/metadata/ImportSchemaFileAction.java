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
package org.talend.repository.metadata.ui.actions.metadata;

import java.io.File;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.runtime.image.OverlayImageProvider;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.repository.ProjectManager;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.wizards.metadata.connection.genericshema.ImportSchemaFileWizard;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 */
public class ImportSchemaFileAction extends CreateGenericSchemaAction {

    private static final String LABEL = Messages.getString("ImportSchemaFileAction.Label"); //$NON-NLS-1$

    private File file = null;

    /**
     * DOC ggu ImportSchemaFileAction constructor comment.
     */
    public ImportSchemaFileAction() {
        setText(LABEL);
        setToolTipText(LABEL);
        setImageDescriptor(OverlayImageProvider.getImageWithNew(ImageProvider.getImage(ECoreImage.METADATA_GENERIC_ICON)));

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.metadata.CreateGenericSchemaAction#getClassForDoubleClick()
     */
    @Override
    public Class getClassForDoubleClick() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.ui.actions.metadata.CreateGenericSchemaAction#init(org.talend.repository.model.RepositoryNode
     * )
     */
    @Override
    protected void init(RepositoryNode node) {
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        if (factory.isUserReadOnlyOnCurrentProject() || !ProjectManager.getInstance().isInCurrentMainProject(node)) {
            setEnabled(false);
            return;
        }

        ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
        if (nodeType == null) {
            return;
        }
        if (nodeType != ERepositoryObjectType.METADATA_GENERIC_SCHEMA) {
            return;
        }

        switch (node.getType()) {
        case SIMPLE_FOLDER:
        case SYSTEM_FOLDER:
            collectChildNames(node);
            setEnabled(true);
            break;
        default:
            return;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    protected void doRun() {
        file = openImportFileDialog();
        if (file == null) {
            return;
        }

        openImportWizard();
    }

    private File openImportFileDialog() {
        FileDialog dial = new FileDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), SWT.OPEN);
        dial.setFilterExtensions(new String[] { "*.xml" }); //$NON-NLS-1$
        String fileName = dial.open();
        if ((fileName != null) && (!fileName.equals(""))) { //$NON-NLS-1$
            return new File(fileName);
        } else {
            return null;
        }
    }

    private void openImportWizard() {
        ISelection selection = getSelection();
        ImportSchemaFileWizard wizard = new ImportSchemaFileWizard(PlatformUI.getWorkbench(), selection, getExistingNames(), file);
        if (!wizard.isInitOK()) {
            wizard.dispose();
            return;
        }
        WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
        wizardDialog.setPageSize(WIZARD_WIDTH, WIZARD_HEIGHT);
        wizardDialog.create();
        wizardDialog.open();
    }

}
