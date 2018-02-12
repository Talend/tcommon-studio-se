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
 * 
 * This class is used to adapt value from external data to cell editor and inverse.
 * 
 * <BR/>If your property's object has a different type of the cellEditor value, you must adapt them.
 * 
 * <BR/>For example you have a price property with Integer type and you want use a TextCellEditor. The TextCellEditor
 * only accept a String value. Then you must convert the value :
 * <p>
 * <code>
 *  column.setCellEditor(new TextCellEditor(table), new CellEditorValueAdapter() {
 *
 *    public String getColumnText(CellEditor cellEditor, Object originalTypedValue) {
 *       String[] items = ((ComboBoxCellEditor) cellEditor).getItems();
 *       int index = (Integer) originalTypedValue;
 *       if (index >= 0 && index < items.length) {
 *          return items[index];
 *       } else {
 *          return "";
 *       }
 *    };
 *
 *     public Object getOriginalTypedValue(CellEditor cellEditor, Object cellEditorTypedValue) {
 *        return new Integer((String)cellEditorTypedValue);
 *     }
 *
 *     public Object getCellEditorTypedValue(CellEditor cellEditor, Object originalTypedValue) {
 *        return String.valueOf(originalTypedValue);
 *     }
 *   });
 * </code>
 * </p>
 * 
 * <br/>
 * 
 * $Id: CellEditorValueAdapter.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class CellEditorValueAdapter {

    /**
     * Convert cellEditorValue to String value which will be set in the current cell of the <code>Table</code>.
     * 
     * @param cellEditor current cellEditor, use if necessary.
     * @param bean TODO
     * @param originalTypedValue.
     * @return
     */
    public String getColumnText(CellEditor cellEditor, Object bean, Object originalTypedValue) {
        return String.valueOf(originalTypedValue);
    };

    /**
     * Convert cellEditor value to original type of data (type of object's property which is introspected).
     * 
     * @param cellEditor
     * @param cellEditorTypedValue
     * @return
     */
    public Object getOriginalTypedValue(CellEditor cellEditor, Object cellEditorTypedValue) {
        return cellEditorTypedValue;
    };

    /**
     * Convert original typed value to cellEditor typed value.
     * 
     * DOC amaumont Comment method "getInternalTypedValue".
     * 
     * @param cellEditor
     * @param originalTypedValue
     * @return
     */
    public Object getCellEditorTypedValue(CellEditor cellEditor, Object originalTypedValue) {
        return originalTypedValue;
    };
}
