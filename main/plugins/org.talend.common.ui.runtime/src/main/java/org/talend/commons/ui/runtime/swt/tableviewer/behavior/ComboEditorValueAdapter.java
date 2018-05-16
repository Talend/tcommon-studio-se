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
package org.talend.commons.ui.runtime.swt.tableviewer.behavior;

import org.eclipse.jface.viewers.CellEditor;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class ComboEditorValueAdapter extends CellEditorValueAdapter {

    private String defaultItem;

    /**
     * DOC amaumont ComboEditorValueAdapter constructor comment.
     * 
     * @param defaultItem
     */
    public ComboEditorValueAdapter(String defaultItem) {
        this.defaultItem = defaultItem;
    }

    public Object getOriginalTypedValue(final CellEditor cellEditor, Object value) {
        String[] items = getItems(cellEditor);
        int i = new Integer(value.toString());
        if (i >= 0) {
            return items[i];
        } else {
            return ""; //$NON-NLS-1$
        }
    }

    public Object getCellEditorTypedValue(final CellEditor cellEditor, Object value) {
        String[] items = getItems(cellEditor);

        if (value == null && defaultItem != null) {
            value = defaultItem;
        }

        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.tableviewer.behavior.CellEditorValueAdapter#getColumnText(org.eclipse.jface.viewers.CellEditor,
     * java.lang.Object)
     */
    @Override
    public String getColumnText(CellEditor cellEditor, Object bean, Object originalTypedValue) {
        String displayedValue = super.getColumnText(cellEditor, bean, originalTypedValue);
        if (displayedValue == null && defaultItem != null) {
            displayedValue = defaultItem;
        }
        return displayedValue;
    }

    public abstract String[] getItems(CellEditor cellEditor);

}
