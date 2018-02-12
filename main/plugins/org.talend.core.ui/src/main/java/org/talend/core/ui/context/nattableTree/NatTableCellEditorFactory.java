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
package org.talend.core.ui.context.nattableTree;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.ui.runtime.swt.tableviewer.celleditor.DateDialog;
import org.talend.commons.ui.utils.PathUtils;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.MetadataToolHelper;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.ui.context.MultiStringSelectionDialog;
import org.talend.core.utils.TalendQuoteUtils;

/**
 * created by ldong on Jul 30, 2014 Detailled comment
 * 
 */
public class NatTableCellEditorFactory {

    public static final String[] BOOLEANS = new String[] { Boolean.FALSE.toString(), Boolean.TRUE.toString() };

    public NatTableCellEditorFactory() {
    }

    public boolean isSpecialType(IContextParameter para) {
        String currentType = para.getType();
        if (currentType != null) {
            if (isFile(currentType) || isDate(currentType) || isDirectory(currentType) || isList(currentType)) {
                return true;
            }
        }
        return false;
    }

    public Object openCustomCellEditor(IContextParameter para, Shell parentShell) {
        String currentType = para.getType();
        String defalutDataValue = para.getValue();
        Object transformResult = "";
        if (currentType != null) {
            if (isFile(currentType)) {
                transformResult = openFileDialogForCellEditor(parentShell);
            } else if (isDate(currentType)) {
                transformResult = openDateDialogForCellEditor(parentShell, para);
            } else if (isDirectory(currentType)) {
                transformResult = openDirectoryDialogForCellEditor(parentShell, defalutDataValue);
            } else if (isList(currentType)) {
                transformResult = openListValueDialogForCellEditor(parentShell, para);
                defalutDataValue = para.getDisplayValue();
            }
        }
        return transformResult;
    }

    private String openDirectoryDialogForCellEditor(Shell parentShell, String defaultPath) {
        DirectoryDialog dialog = new DirectoryDialog(parentShell);
        String path = defaultPath;
        if (path != null) {
            dialog.setFilterPath(PathUtils.getOSPath(getRemoveQuoteString(path)));
        }

        path = dialog.open();
        if (path != null) {
            path = PathUtils.getPortablePath(path);
            path += "/"; //$NON-NLS-1$
        }
        return getAddQuoteString(path);
    }

    private Object openListValueDialogForCellEditor(Shell parentShell, IContextParameter para) {
        String[] input = para.getValueList();
        MultiStringSelectionDialog d = new MultiStringSelectionDialog(parentShell, input);
        int res = d.open();
        if (res == Dialog.OK) {
            String[] result = d.getResultString();
            para.setValueList(result);
            return result;
        }
        return para.getDisplayValue();
    }

    private String openDateDialogForCellEditor(Shell parentShell, IContextParameter param) {
        DateDialog d = new DateDialog(parentShell);
        int res = d.open();
        if (res == Dialog.OK) {
            return getAddQuoteString(d.getTalendDateString());
        }
        return "";
    }

    private String openFileDialogForCellEditor(Shell parentShell) {
        FileDialog dialog = new FileDialog(parentShell);
        String path = "";
        if (path != null) {
            dialog.setFileName(PathUtils.getOSPath(getRemoveQuoteString(path)));
        }

        path = dialog.open();
        if (path != null) {
            path = PathUtils.getPortablePath(path);
        }
        return getAddQuoteString(path);
    }

    public static String getAddQuoteString(String path) {
        ECodeLanguage codeLanguage = LanguageManager.getCurrentLanguage();
        if (codeLanguage == ECodeLanguage.PERL) {
            return TalendQuoteUtils.addQuotes(path);
        }
        return path;
    }

    public static String getRemoveQuoteString(String path) {
        ECodeLanguage codeLanguage = LanguageManager.getCurrentLanguage();
        if (codeLanguage == ECodeLanguage.PERL) {
            return TalendQuoteUtils.removeQuotes(path);
        }
        return path;
    }

    public static boolean isBoolean(final String value) {
        return MetadataToolHelper.isBoolean(value);
    }

    public static boolean isDirectory(final String value) {
        return MetadataToolHelper.isDirectory(value);
    }

    public static boolean isList(final String value) {
        return MetadataToolHelper.isList(value);
    }

    public static boolean isDate(final String value) {
        return MetadataToolHelper.isDate(value);
    }

    public static boolean isFile(final String value) {
        return MetadataToolHelper.isFile(value);
    }

    public static boolean isPassword(final String value) {
        return MetadataToolHelper.isPassword(value);
    }

}
