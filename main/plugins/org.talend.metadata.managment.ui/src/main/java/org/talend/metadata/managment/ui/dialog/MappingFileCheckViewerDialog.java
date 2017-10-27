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
package org.talend.metadata.managment.ui.dialog;

import java.io.StringReader;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.ui.swt.dialogs.SourceViewerDialog;
import org.talend.core.model.utils.XSDValidater;
import org.talend.metadata.managment.ui.MetadataManagmentUiPlugin;
import org.talend.metadata.managment.ui.i18n.Messages;

/**
 * DOC bqian class global comment. Detailled comment <br/>
 * 
 * $Id: MappingFileCheckViewerDialog.java 1 May 22, 2007 10:44:43 AM +0000 $
 * 
 */
public class MappingFileCheckViewerDialog extends SourceViewerDialog {

    private XSDValidater validater;

    /**
     * Sets the validater.
     * 
     * @param validater the validater to set
     */
    public void setValidater(XSDValidater validater) {
        this.validater = validater;
    }

    /**
     * bqian MappingFileCheckViewerDialog constructor comment.
     * 
     * @param parentShell
     * @param title
     */
    public MappingFileCheckViewerDialog(Shell parentShell, String title) {
        super(parentShell, title);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed() {
        if (validater != null) {
            try {
                validater.validateWithDom(new StringReader(this.getDocument().get()));
            } catch (Exception e) {
                IStatus status = new Status(IStatus.ERROR, MetadataManagmentUiPlugin.PLUGIN_ID, IStatus.ERROR,
                        Messages.getString("MetadataTalendTypeEditor.fileIsInvalid"), e); //$NON-NLS-1$
                ErrorDialog
                        .openError(this.getShell(), Messages.getString("MetadataTalendTypeEditor.error.message"), null, status); //$NON-NLS-1$
                return;
            }
        }
        super.okPressed();
    }
}
