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
package org.talend.commons.ui.runtime.swt.tableviewer.celleditor;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * DOC qiang.zhang class global comment. Detailled comment <br/>
 * 
 */
public class DateCellEditor extends DialogCellEditor {

    /**
     * qiang.zhang DateCellEditor constructor comment.
     * 
     * @param parent
     */
    public DateCellEditor(Composite parent) {
        super(parent);
    }

    /**
     * qiang.zhang DateCellEditor constructor comment.
     * 
     * @param parent
     * @param style
     */
    public DateCellEditor(Composite parent, int style) {
        super(parent, style);
    }

    /*
     * (non-Java)
     * 
     * @see org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(org.eclipse.swt.widgets.Control)
     */
    @Override
    protected Object openDialogBox(Control cellEditorWindow) {
        DateDialog d = new DateDialog(cellEditorWindow.getShell());
        int res = d.open();
        if (res == Dialog.OK) {
            return d.getTalendDateString();
        } else {
            return getValue();
        }
    }
}
