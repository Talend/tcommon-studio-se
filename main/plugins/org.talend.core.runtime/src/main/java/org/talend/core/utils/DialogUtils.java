package org.talend.core.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.CommonsPlugin;

public class DialogUtils {

    private static List<String> warningInforList = new ArrayList<>();

    public static void addWarningInfo(String warnningInfo) {
        if (!warningInforList.contains(warnningInfo)) {
            warningInforList.add(warnningInfo);
        }
    }

    public static void syncOpenWarningDialog(String title) {
        if (CommonsPlugin.isHeadless()) {
            return;
        }
        StringBuffer sb = new StringBuffer();
        warningInforList.forEach(w -> {
            sb.append(w);
            sb.append("\n");// $NON-NLS-1$
            sb.append("\n");// $NON-NLS-1$
        });

        Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {

                String[] dialogButtonLabels = new String[] { IDialogConstants.OK_LABEL };
                int open = MessageDialog.open(MessageDialog.WARNING, Display.getDefault().getActiveShell(), title, sb.toString(),
                        SWT.NONE, 
                        dialogButtonLabels);
                warningInforList.clear();
            }

        });
    }
}
