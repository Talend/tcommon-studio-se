package org.talend.core.utils;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.CommonsPlugin;

public class DialogUtils {

    private static ELoginInfoCase finalCase;

    public static void setWarningInfo(ELoginInfoCase warnningInfo) {
        finalCase = warnningInfo;
    }

    public static void syncOpenWarningDialog(String title) {
        if (CommonsPlugin.isHeadless() || DialogUtils.finalCase == null) {
            return;
        }
        int dialogType = DialogUtils.finalCase.getDialogType();
        String[] contents = DialogUtils.finalCase.getContents();
        List<String> asList = Arrays.asList(contents);
        StringBuffer sb = new StringBuffer();
        asList.forEach(w -> {
            sb.append(w);
            sb.append("\n");// $NON-NLS-1$
        });
        int[] selectIndex = new int[1];
        Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {

                String[] dialogButtonLabels = new String[] { IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL, };

                if (dialogType == MessageDialog.ERROR) {
                    dialogButtonLabels = new String[] { IDialogConstants.CANCEL_LABEL };
                }
                int open = MessageDialog.open(dialogType, Display.getDefault().getActiveShell(), title, sb.toString(), SWT.NONE,
                        dialogButtonLabels);
                selectIndex[0] = open;

            }


        });
        DialogUtils.finalCase = null;
        if (dialogType == MessageDialog.ERROR) {
            throw new OperationCanceledException(""); //$NON-NLS-1$
        }
        if (1 == selectIndex[0]) {
            throw new OperationCanceledException(""); //$NON-NLS-1$
        }
    }
}
