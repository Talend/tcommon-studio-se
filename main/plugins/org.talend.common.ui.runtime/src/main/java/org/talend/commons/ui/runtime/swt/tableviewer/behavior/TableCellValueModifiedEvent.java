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
package org.talend.commons.ui.runtime.swt.tableviewer.behavior;

import org.eclipse.swt.widgets.TableItem;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorColumnNotModifiable;

/**
 * This event is used by TableViewerCreator to notify that a cell value has changed.
 * 
 * $Id: TableCellValueModifiedEvent.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class TableCellValueModifiedEvent {

    /** current tableItem where value has been applied. */
    public TableItem tableItem;

    /** current bean where value has been stored. */
    public Object bean;

    /** current column where value has been applied. */
    public TableViewerCreatorColumnNotModifiable column;

    /** value returned by CellEditor. */
    public Object cellEditorAppliedValue;

    /** value stored in the bean. */
    public Object storedValue;

    /**
     * DOC amaumont CellEditorAppliedEvent constructor comment.
     * 
     * @param tableItem
     * @param bean
     * @param idColumn
     * @param cellEditorAppliedValue
     * @param storedValue
     */
    public TableCellValueModifiedEvent(TableItem tableItem, Object bean, TableViewerCreatorColumnNotModifiable column,
            Object cellEditorAppliedValue, Object storedValue) {
        this.tableItem = tableItem;
        this.bean = bean;
        this.column = column;
        this.cellEditorAppliedValue = cellEditorAppliedValue;
        this.storedValue = storedValue;
    }

}
