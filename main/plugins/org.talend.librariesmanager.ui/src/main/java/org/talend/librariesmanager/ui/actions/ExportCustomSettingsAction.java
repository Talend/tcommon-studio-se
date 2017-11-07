// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.librariesmanager.model.service.CustomUriManager;
import org.talend.librariesmanager.ui.i18n.Messages;

/**
 * Imports the external jar files into talend.
 * 
 * $Id: ImportExternalJarAction.java Mar 15, 20075:58:30 PM bqian $
 * 
 */
public class ExportCustomSettingsAction extends Action {

    /**
     * DOC acer ImportExternalJarAction constructor comment.
     */
    public ExportCustomSettingsAction() {
        super();
        this.setText(Messages.getString("ExportCustomSettingsAction.title")); //$NON-NLS-1$
        this.setDescription(Messages.getString("ExportCustomSettingsAction.title")); //$NON-NLS-1$
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.EXPORT_ICON));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        FileDialog dialog = new FileDialog(shell, SWT.SAVE);
        dialog.setText(Messages.getString("ImportCustomSettingsAction.title"));
        String selectedFile = dialog.open();
        if (selectedFile != null) {
            CustomUriManager.getInstance().exportSettings(dialog.getFilterPath(), dialog.getFileName());
        }
    }

}
