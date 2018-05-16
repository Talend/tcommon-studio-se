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
package org.talend.commons.ui.swt.dialogs;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.ui.runtime.i18n.Messages;

/**
 * Open an Error dialog with a details area (title = "Error Message"). This dialog box show a Details button that shows
 * or hides an extra area with more information. This functionality is provided by the JFace ErrorDialog. The naming of
 * this class is a bit unfortunate as it doesn�t fully express all the things it can be used for. A better name might
 * have been StatusDialog as it is used to display any IStatus object, which can represent information, warnings, or
 * errors. The dialog looks at the severity of the supplied status and uses an appropriate icon: an exclamation mark for
 * errors, a yield sign for warnings, and an i character for information.
 * 
 * If you want to provide more information in the details area, you need to supply a MultiStatus object. The dialog will
 * obtain the message string from the MultiStatus parent, and one line in the details area will be for the message from
 * each child status.
 * 
 * $Id: ErrorDialogWidthDetailArea.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class ErrorDialogWidthDetailArea {

    private int codeOfButton;

    /**
     * Open an Error dialog with a details area (title = "Error Message").
     * 
     * @param shell
     * @param String (Product ID -> Activator.PLUGIN_ID)
     * @param mainMessage
     * @param detailMessage (\n an \t are interpreted ; \r are deleted)
     */
    public ErrorDialogWidthDetailArea(Shell shell, String pid, String mainMessage, String detailMessage) {
        this(shell, pid, mainMessage, detailMessage, IStatus.ERROR);
    }

    public ErrorDialogWidthDetailArea(Shell shell, String pid, String mainMessage, String detailMessage, int status) {
        MultiStatus info = new MultiStatus(pid, 1, mainMessage, null);
        if (detailMessage != null) {
            String[] lines = detailMessage.split("\n"); //$NON-NLS-1$
            for (String line : lines) {
                info.add(new Status(status, pid, 1, line.replaceAll("\t", "    ").replaceAll("\r", ""), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                        null));
            }
        }
        codeOfButton = ErrorDialog.openError(shell, Messages.getString("ErrorDialogWidthDetailArea.ErrorMessage.Text"), //$NON-NLS-1$
                null, info);
    }

    public int getCodeOfButton() {
        return this.codeOfButton;
    }
}
