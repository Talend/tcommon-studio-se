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
package org.talend.metadata.managment.ui;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.service.IMetadataManagmentUiService;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.metadata.managment.ui.dialog.OpenXSDFileDialog;
import org.talend.metadata.managment.ui.i18n.Messages;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;
import org.talend.metadata.managment.ui.wizard.context.ContextWizard;
import org.talend.repository.model.RepositoryNode;

/**
 *
 * ggu class global comment. Detailled comment
 */
public class MetadataManagmentUiService implements IMetadataManagmentUiService {

    @Override
    public String getAndOpenXSDFileDialog(Path initPath) {
        OpenXSDFileDialog openXSDFileDialog = new OpenXSDFileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                .getShell());
        openXSDFileDialog.setTitle(Messages.getString("RepositoryToComponentProperty.xmlFileSelection")); //$NON-NLS-1$
        openXSDFileDialog.setPath(initPath);
        int dialogValue = openXSDFileDialog.open();
        if (dialogValue == Window.OK) {
            return openXSDFileDialog.getNewValue();
        }
        return null; // don't set
    }

    @Override
    public ContextType getContextTypeForContextMode(Connection connection) {
        return ConnectionContextHelper.getContextTypeForContextMode(connection);
    }

    public ContextType getContextTypeForContextMode(Connection connection, String selectedContext, boolean defaultContext) {
        return ConnectionContextHelper.getContextTypeForContextMode(null, connection, selectedContext, defaultContext);
    }

    @Override
    public String getOriginalValue(ContextType contextType, String value) {
        return ConnectionContextHelper.getOriginalValue(contextType, value);
    }

    @Override
    public void openRepositoryContextWizard(RepositoryNode repositoryNode) {
        ContextWizard contextWizard = new ContextWizard(PlatformUI.getWorkbench(), false, repositoryNode, false);
        WizardDialog dlg = new WizardDialog(Display.getCurrent().getActiveShell(), contextWizard);
        dlg.open();
    }

    @Override
    public boolean isContextMode(Connection connection, String value) {
        return ConnectionContextHelper.isContextMode(connection, value);
    }

}
