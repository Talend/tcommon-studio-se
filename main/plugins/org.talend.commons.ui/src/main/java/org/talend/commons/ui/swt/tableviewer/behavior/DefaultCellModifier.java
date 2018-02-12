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
package org.talend.commons.ui.swt.tableviewer.behavior;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;
import org.talend.commons.ui.runtime.i18n.Messages;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorColumnNotModifiable;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorNotModifiable;
import org.talend.commons.ui.runtime.swt.tableviewer.behavior.ITableCellValueModifiedListener;
import org.talend.commons.ui.runtime.swt.tableviewer.behavior.TableCellValueModifiedEvent;
import org.talend.commons.ui.runtime.swt.tableviewer.data.AccessorUtils;
import org.talend.commons.ui.runtime.swt.tableviewer.data.ModifiedObjectInfo;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;

/**
 * 
 * Default implementation of <code>ICellModifier</code> used by <code>TableViewerCreator</code>. This implementation
 * retrieve or modify values (using accesors defined in <code>TableViewerCreatorColumn</code>) on data object. <br/>
 * 
 * $Id: DefaultCellModifier.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 * @see ICellModifier
 * 
 */
public class DefaultCellModifier implements ICellModifier {

    private static Logger log = Logger.getLogger(DefaultCellModifier.class);

    private TableViewerCreator tableViewerCreator;

    private ListenerList cellEditorAppliedListeners = new ListenerList();

    public DefaultCellModifier(TableViewerCreator tableViewerCreator) {
        super();
        this.tableViewerCreator = tableViewerCreator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
     */
    public boolean canModify(Object bean, String idColumn) {
    	TableViewerCreatorColumn column = tableViewerCreator.getModifiableColumn(idColumn);
        if (column != null && column.getColumnCellModifier() != null) {
            return column.getColumnCellModifier().canModify(bean);
        }
        return tableViewerCreator.getColumn(idColumn).isModifiable();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public Object getValue(Object bean, String idColumn) {
    	TableViewerCreatorColumnNotModifiable column = tableViewerCreator.getColumn(idColumn);
        ModifiedObjectInfo modifiedObjectInfo = this.tableViewerCreator.getModifiedObjectInfo();
        modifiedObjectInfo.setCurrentModifiedBean(bean);
        modifiedObjectInfo.setCurrentModifiedColumn(column);
        modifiedObjectInfo.setCurrentModifiedIndex(this.tableViewerCreator.getInputList().indexOf(bean));

        Object returnValue = null;
        if (column.getColumnCellModifier() != null) {
            returnValue = column.getColumnCellModifier().getValue(bean);
        }
        if (returnValue == null) {
            Object value = AccessorUtils.get(bean, column);

            if (column.getCellEditorValueAdapter() != null) {
                returnValue = column.getCellEditorValueAdapter().getCellEditorTypedValue(column.getCellEditor(), value);
            } else {
                returnValue = value;
            }
            if (returnValue == null && column.getDefaultInternalValue() != null) {
                returnValue = column.getDefaultInternalValue();
            }
            modifiedObjectInfo.setOriginalPropertyBeanValue(returnValue);
            modifiedObjectInfo.setPreviousPropertyBeanValue(returnValue);
            // System.out.println("getValue : value=" + returnValue);
        }
        return returnValue == null ? "" : returnValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public void modify(Object tableItem, String idColumn, Object value) {
        if (((TableItem) tableItem).isDisposed()) {
            log.warn(Messages.getString("DefaultCellModifier.tableItemDispose"), new Exception()); //$NON-NLS-1$
            return;
        }
        Object bean = ((TableItem) tableItem).getData();
        TableViewerCreatorColumn column = tableViewerCreator.getModifiableColumn(idColumn);
		if (column == null) {
			return;
		}
        boolean modifiedByColumnCellModifier = false;
        if (column.getColumnCellModifier() != null) {
            modifiedByColumnCellModifier = column.getColumnCellModifier().modify(bean, value);
        }

        if (!modifiedByColumnCellModifier && column.getBeanPropertyAccessors() != null) {
            Object typedValue = null;
            if (column.getCellEditorValueAdapter() != null) {
                typedValue = column.getCellEditorValueAdapter().getOriginalTypedValue(column.getCellEditor(), value);
            } else {
                typedValue = value;
            }
            if (typedValue == null && column.getDefaultInternalValue() != null) {
                typedValue = column.getDefaultInternalValue();
            }
            Object previousValue = AccessorUtils.get(bean, column);

            boolean needCommand = true;
            if (column.getCellEditor() != null && column.getCellEditor().getControl() != null) {
                needCommand = column.getCellEditor().getControl().isEnabled();
            }

            tableViewerCreator.setBeanValue(column, bean, typedValue, needCommand);
            fireCellEditorApplied((TableItem) tableItem, bean, column, value, previousValue, typedValue);
        }
        ModifiedObjectInfo modifiedObjectInfo = this.tableViewerCreator.getModifiedObjectInfo();
        modifiedObjectInfo.setPreviousModifiedBean(bean);
        modifiedObjectInfo.setPreviousModifiedIndex(modifiedObjectInfo.getCurrentModifiedIndex());
        modifiedObjectInfo.setCurrentModifiedBean(null);
        modifiedObjectInfo.setPreviousModifiedColumn(modifiedObjectInfo.getCurrentModifiedColumn());
        modifiedObjectInfo.setCurrentModifiedColumn(null);
        modifiedObjectInfo.setOriginalPropertyBeanValue(null);
    }

    /**
     * 
     * DOC amaumont Comment method "fireCellEditorApplied".
     * 
     * @param tableItem
     * @param bean
     * @param idColumn
     * @param cellEditorAppliedValue
     * @param newValue
     */
    private void fireCellEditorApplied(TableItem tableItem, Object bean, TableViewerCreatorColumnNotModifiable column,
            Object cellEditorAppliedValue, Object previousValue, Object newValue) {
        TableCellValueModifiedEvent event = new TableCellValueModifiedEvent(tableItem, bean, column, cellEditorAppliedValue,
                newValue);
        final Object[] listenerArray = cellEditorAppliedListeners.getListeners();
        for (Object element : listenerArray) {
            ((ITableCellValueModifiedListener) element).cellValueModified(event);
        }
        if (column.getCellEditorAppliedListener() != null) {
            column.getCellEditorAppliedListener().cellValueModified(event);
        }
    }

    public void addCellEditorAppliedListener(ITableCellValueModifiedListener lineSelectionListener) {
        this.cellEditorAppliedListeners.add(lineSelectionListener);
    }

    public void removeCellEditorAppliedListener(ITableCellValueModifiedListener cellEditorAppliedListener) {
        this.cellEditorAppliedListeners.remove(cellEditorAppliedListener);
    }

    /**
     * Getter for tableViewerCreator.
     * 
     * @return the tableViewerCreator
     */
    public TableViewerCreatorNotModifiable getTableViewerCreator() {
        return this.tableViewerCreator;
    }

}
