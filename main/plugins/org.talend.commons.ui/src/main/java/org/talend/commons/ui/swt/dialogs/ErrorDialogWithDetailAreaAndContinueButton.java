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
package org.talend.commons.ui.swt.dialogs;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.ui.runtime.i18n.Messages;

/**
 * DOC yexiaowei class global comment. Detailled comment
 */
public class ErrorDialogWithDetailAreaAndContinueButton {

    private int codeOfButton;

    /**
     * Open an Error dialog with a details area (title = "Error Message").
     * 
     * @param shell
     * @param String (Product ID -> Activator.PLUGIN_ID)
     * @param mainMessage
     * @param detailMessage (\n an \t are interpreted ; \r are deleted)
     */
    public ErrorDialogWithDetailAreaAndContinueButton(Shell shell, String pid, String mainMessage, String detailMessage) {
        MultiStatus info = new MultiStatus(pid, 1, mainMessage, null);
        if (detailMessage != null) {
            String[] lines = detailMessage.split("\n"); //$NON-NLS-1$
            for (int i = 0; i < lines.length; i++) {
                info
                        .add(new Status(IStatus.INFO, pid, 1, lines[i].replaceAll("\t", "    ").replaceAll("\r", ""), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                                null));
            }
        }
        codeOfButton = ErrorDialogWithContinue.openErrorWithContinueButton(shell, Messages
                .getString("ErrorDialogWidthDetailArea.ErrorMessage.Text"), null, info); //$NON-NLS-1$
    }

    public int getCodeOfButton() {
        return this.codeOfButton;
    }
}
