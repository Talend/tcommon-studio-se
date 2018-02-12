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
package org.talend.librariesmanager.ui.actions;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.librariesmanager.model.service.CustomUriManager;
import org.talend.librariesmanager.ui.LibManagerUiPlugin;
import org.talend.librariesmanager.ui.i18n.Messages;

/**
 * Imports the external jar files into talend.
 * 
 * $Id: ImportExternalJarAction.java Mar 15, 20075:58:30 PM bqian $
 * 
 */
public class ImportCustomSettingsAction extends Action {

    /**
     * DOC acer ImportExternalJarAction constructor comment.
     */
    public ImportCustomSettingsAction() {
        super();
        this.setText(Messages.getString("ImportCustomSettingsAction.title")); //$NON-NLS-1$
        this.setDescription(Messages.getString("ImportCustomSettingsAction.title")); //$NON-NLS-1$
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.IMPORT_ICON));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        FileDialog fileDialog = new FileDialog(shell, SWT.OPEN | SWT.MULTI);
        fileDialog.setText(Messages.getString("ImportCustomSettingsAction.title"));
        String selectedFile = fileDialog.open();
        if (selectedFile != null) {
            try {
                boolean openQuestion = MessageDialog.openQuestion(shell, "Warning",
                        Messages.getString("ImportCustomSettingsAction.warning"));
                if (openQuestion) {
                    CustomUriManager.getInstance().importSettings(fileDialog.getFilterPath(), fileDialog.getFileName());
                    LibManagerUiPlugin.getDefault().getLibrariesService().checkLibraries();
                }
            } catch (Exception e) {
                new ErrorDialogWidthDetailArea(shell, LibManagerUiPlugin.PLUGIN_ID,
                        "Import settings fail, please check the setting file format!", ExceptionUtils.getFullStackTrace(e),
                        IStatus.ERROR);
            }
        }
    }
}
