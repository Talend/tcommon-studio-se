package org.talend.core.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.CommonsPlugin;

public class DialogUtils {

    private static List<ELoginInfoCase> warningInforList = new ArrayList<>();

    public static void addWarningInfo(ELoginInfoCase warnningInfo) {
        if (!warningInforList.contains(warnningInfo)) {
            warningInforList.add(warnningInfo);
        }

    }

    private static ELoginInfoCase getFinalCase() {

        if (warningInforList.contains(ELoginInfoCase.ARTIFACT_UNCONNECTED)) {

            String[] warningContents = ELoginInfoCase.ARTIFACT_UNCONNECTED.getContents();

            if (warningInforList.contains(ELoginInfoCase.STUDIO_LOWER_THAN_PROJECT)) {

                String[] errerContents = ELoginInfoCase.STUDIO_LOWER_THAN_PROJECT.getContents();

                String[] contents = new String[] { warningContents[0], errerContents[0] };

                ELoginInfoCase.ARTIFACT_UNCONNECTED_AND_STUDIO_LOWER.setContents(contents);

                return ELoginInfoCase.ARTIFACT_UNCONNECTED_AND_STUDIO_LOWER;
            }

            if (warningInforList.contains(ELoginInfoCase.STUDIO_HIGHER_THAN_PROJECT)) {

                String[] warnContents = ELoginInfoCase.STUDIO_HIGHER_THAN_PROJECT.getContents();

                String[] contents = new String[] { warningContents[0], warnContents[0] };

                ELoginInfoCase.ARTIFACT_UNCONNECTED_AND_STUDIO_HIGHER.setContents(contents);

                return ELoginInfoCase.ARTIFACT_UNCONNECTED_AND_STUDIO_HIGHER;
            }
            return ELoginInfoCase.ARTIFACT_UNCONNECTED;
        }
        if (!warningInforList.contains(ELoginInfoCase.ARTIFACT_UNCONNECTED)) {
            if (warningInforList.contains(ELoginInfoCase.STUDIO_LOWER_THAN_PROJECT)) {
                String[] warnContents = ELoginInfoCase.STUDIO_LOWER_THAN_PROJECT.getContents();
                ELoginInfoCase.STUDIO_LOWER_THAN_PROJECT.setContents(warnContents);
                return ELoginInfoCase.STUDIO_LOWER_THAN_PROJECT;
            }
            if (warningInforList.contains(ELoginInfoCase.STUDIO_HIGHER_THAN_PROJECT)) {
                String[] warnContents = ELoginInfoCase.STUDIO_HIGHER_THAN_PROJECT.getContents();
                ELoginInfoCase.STUDIO_HIGHER_THAN_PROJECT.setContents(warnContents);
                return ELoginInfoCase.STUDIO_HIGHER_THAN_PROJECT;
            }
            return null;
        }
        return null;

    }

    public static void syncOpenWarningDialog(String title) {
        ELoginInfoCase finalInfoCase = getFinalCase();
        if (CommonsPlugin.isHeadless() || finalInfoCase == null) {
            return;
        }
        int dialogType = finalInfoCase.getDialogType();
        String[] contents = finalInfoCase.getContents();
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
                warningInforList.clear();
            }


        });
        if (dialogType == MessageDialog.ERROR) {
            throw new OperationCanceledException(""); //$NON-NLS-1$
        }
        if (1 == selectIndex[0]) {
            throw new OperationCanceledException(""); //$NON-NLS-1$
        }
    }
}
