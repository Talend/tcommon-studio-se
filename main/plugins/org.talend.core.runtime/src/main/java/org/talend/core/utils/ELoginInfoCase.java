package org.talend.core.utils;

import org.eclipse.jface.dialogs.MessageDialog;

public enum ELoginInfoCase {

    STUDIO_LOWER_THAN_PROJECT(MessageDialog.ERROR),

    STUDIO_HIGHER_THAN_PROJECT(MessageDialog.WARNING);

    private int dialogType;

    private String[] contents;

    ELoginInfoCase(int dialogType) {
        this.dialogType = dialogType;
    }

    ELoginInfoCase(int dialogType, String[] contents) {
        this.dialogType = dialogType;
        this.contents = contents;
    }

    public int getDialogType() {
        return dialogType;
    }

    public void setDialogType(int dialogType) {
        this.dialogType = dialogType;
    }

    public String[] getContents() {
        return contents;
    }

    public void setContents(String[] contents) {
        this.contents = contents;
    }

}
