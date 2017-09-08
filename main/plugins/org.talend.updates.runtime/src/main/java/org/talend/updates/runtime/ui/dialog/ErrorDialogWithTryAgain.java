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
package org.talend.updates.runtime.ui.dialog;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * created by wchen on 2015年8月10日 Detailled comment
 *
 */
public class ErrorDialogWithTryAgain extends ErrorDialog {

    static int displayMask = IStatus.OK | IStatus.INFO | IStatus.WARNING | IStatus.ERROR;

    public ErrorDialogWithTryAgain(Shell parentShell, String dialogTitle, String message, IStatus status, int displayMask) {
        super(parentShell, dialogTitle, message, status, displayMask);
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        // create OK and Details buttons
        createButton(parent, IDialogConstants.OK_ID, "Try again", true);// IDialogConstants.OK_LABEL //$NON-NLS-1$
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, true);
        createDetailsButton(parent);
    }

    public static int openErrorWithTryAgainButton(Shell parentShell, String title, String message, IStatus status) {
        ErrorDialogWithTryAgain dialog = new ErrorDialogWithTryAgain(parentShell, title, message, status, displayMask);
        return dialog.open();
    }
}
