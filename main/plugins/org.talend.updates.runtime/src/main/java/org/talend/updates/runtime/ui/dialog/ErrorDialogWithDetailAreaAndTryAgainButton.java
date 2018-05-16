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
package org.talend.updates.runtime.ui.dialog;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * created by wchen on 2015年8月10日 Detailled comment
 *
 */
public class ErrorDialogWithDetailAreaAndTryAgainButton {

    private int codeOfButton;

    /**
     * Open an Error dialog with a details area (title = "Error Message").
     * 
     * @param shell
     * @param String (Product ID -> Activator.PLUGIN_ID)
     * @param mainMessage
     * @param detailMessage (\n an \t are interpreted ; \r are deleted)
     */
    public ErrorDialogWithDetailAreaAndTryAgainButton(Shell shell, String pid, String mainMessage, String detailMessage) {
        MultiStatus info = new MultiStatus(pid, 1, mainMessage, null);
        if (detailMessage != null) {
            String[] lines = detailMessage.split("\n"); //$NON-NLS-1$
            for (String line : lines) {
                info.add(new Status(IStatus.INFO, pid, 1, line.replaceAll("\t", "    ").replaceAll("\r", ""), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                        null));
            }
        }
        codeOfButton = ErrorDialogWithTryAgain.openErrorWithTryAgainButton(shell, "Error Message", null, info); //$NON-NLS-1$
    }

    public int getCodeOfButton() {
        return this.codeOfButton;
    }
}
