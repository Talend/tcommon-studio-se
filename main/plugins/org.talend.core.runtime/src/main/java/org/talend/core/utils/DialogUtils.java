package org.talend.core.utils;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

public class DialogUtils {

    public static void syncOpenWarningDialog(String title, String info) {

        Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {

                String[] dialogButtonLabels = new String[] { IDialogConstants.OK_LABEL };
                int open = MessageDialog.open(MessageDialog.WARNING, Display.getDefault().getActiveShell(), title, // $NON-NLS-1$
                        info, SWT.NONE, // $NON-NLS-1$
                        dialogButtonLabels);
            }

        });
    }
}
