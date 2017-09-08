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
package org.talend.commons.ui.runtime.swt.tableviewer;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.talend.commons.ui.runtime.i18n.Messages;
import org.talend.commons.ui.runtime.swt.celleditor.ComboxCellEditorImproved;
import org.talend.commons.ui.runtime.swt.tableviewer.behavior.CellEditorValueAdapter;
import org.talend.commons.ui.runtime.swt.tableviewer.behavior.ComboEditorValueAdapter;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class CellEditorValueAdapterFactory {

    public static CellEditorValueAdapter getPositiveIntAdapter() {
        return getPositiveIntAdapter(false);
    }

    public static CellEditorValueAdapter getPositiveIntAdapter(final boolean ignoreZero) {

        CellEditorValueAdapter positiveIntValueAdapter = new CellEditorValueAdapter() {

            public Object getOriginalTypedValue(final CellEditor cellEditor, Object value) {
                try {
                    Integer integer = new Integer(value.toString().trim());
                    if (integer < 0) {
                        return integer * -1; // why change to positive???
                    }
                    return integer;
                } catch (Exception ex) {
                    return null; // when remove the value(empty, "")
                }
            }

            public Object getCellEditorTypedValue(final CellEditor cellEditor, Object value) {
                if (value != null && value instanceof Integer) {
                    int i = ((Integer) value).intValue();
                    if (i > -1 && !(ignoreZero && i == 0)) {
                        return String.valueOf(value);
                    }
                }
                return ""; //$NON-NLS-1$
            }

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.talend.commons.ui.swt.tableviewer.behavior.CellEditorValueAdapter#getColumnText(org.eclipse.jface
             * .viewers.CellEditor, java.lang.Object)
             */
            @Override
            public String getColumnText(CellEditor cellEditor, Object bean, Object cellEditorTypedValue) {
                return (String) getCellEditorTypedValue(cellEditor, cellEditorTypedValue);
            }

        };

        return positiveIntValueAdapter;
    }

    /**
     * DOC amaumont Comment method "getComboAdapter".
     * 
     * @return
     */
    public static CellEditorValueAdapter getComboAdapterForComboCellEditor(final String defaultItem) {
        return new ComboEditorValueAdapter(defaultItem) {

            @Override
            public String[] getItems(CellEditor cellEditor) {
                return ((ComboBoxCellEditor) cellEditor).getItems();
            }

        };

    }

    /**
     * DOC amaumont Comment method "getComboAdapter".
     * 
     * @return
     */
    public static CellEditorValueAdapter getComboAdapterForComboCellEditor() {
        return getComboAdapterForComboCellEditor(null);
    }

    /**
     * DOC amaumont Comment method "getComboAdapter".
     * 
     * @return
     */
    public static CellEditorValueAdapter getComboAdapterForComboCellEditorImproved(final String defaultItem) {
        return new ComboEditorValueAdapter(defaultItem) {

            @Override
            public String[] getItems(CellEditor cellEditor) {
                return ((ComboxCellEditorImproved) cellEditor).getItems();
            }

        };

    }

    /**
     * DOC amaumont Comment method "getComboAdapter".
     * 
     * @return
     */
    public static CellEditorValueAdapter getComboAdapterForComboCellEditorImproved() {
        return getComboAdapterForComboCellEditorImproved(null);
    }

    /**
     * DOC amaumont Comment method "getNullToEmptyStringTextAdapater".
     */
    public static CellEditorValueAdapter getNullToEmptyStringTextAdapater() {
        CellEditorValueAdapter nullToEmptyStringAdapter = new CellEditorValueAdapter() {

            public Object getCellEditorTypedValue(final CellEditor cellEditor, Object value) {
                if (value != null) {
                    if (!(value instanceof String)) {
                        throw new IllegalArgumentException(Messages.getString("CellEditorValueAdapterFactory.valueNotStringType")); //$NON-NLS-1$
                    }
                    return value;
                }

                return ""; //$NON-NLS-1$
            }

        };

        return nullToEmptyStringAdapter;
    }

}
