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
package org.talend.commons.ui.swt.tableviewer;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.talend.commons.ui.runtime.i18n.Messages;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorColumnNotModifiable;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorNotModifiable;
import org.talend.commons.ui.runtime.swt.tableviewer.behavior.ITableCellValueModifiedListener;
import org.talend.commons.ui.runtime.swt.tableviewer.data.AccessorUtils;
import org.talend.commons.ui.swt.extended.table.ModifyBeanValueCommand;
import org.talend.commons.ui.swt.proposal.ExtendedTextCellEditorWithProposal;
import org.talend.commons.ui.swt.tableviewer.behavior.DefaultCellModifier;
import org.talend.commons.utils.data.list.ListenableList;

/**
 * A concrete Table viewer based on the JFace <code>TableViewer</code> and the SWT <code>Table</code> control.
 * <p>
 * This class is intended to make easier creation and use of a table.
 * 
 * For a basic usage you need create columns with <code>TableViewerCreatorColumn</code> and init a addedObjects of
 * objects. The addedObjects will be introspected to retrieve values of each its objects. Each object of the
 * addedObjects will represent one line in the Table.
 * 
 * The following functions are already implemented : sort, automatic resize of columns (see layoutMode), and basic
 * functions.
 * 
 * You can access directly to instanciated <code>TableViewer</code> and <code>Table</code> to manage, add custom
 * listeners or other usages.
 * 
 * Filtering is not currently implemented, but you can add manually yours.
 * 
 * <p>
 * You can add CellEditor using <code>TableViewerCreatorColumn</code> and <code>setCellEditor()</code> method. According
 * case you will have to override certain methods of <code>CellEditorValueAdapter</code>.
 * </p>
 * 
 * <p>
 * You can add TableEditor using <code>TableViewerCreatorColumn</code> and <code>setTableEditor()</code> method.
 * According case you will have to override certain methods of <code>TableEditorInitializer</code>.
 * </p>
 * 
 * Read the following steps to create a reflect table :
 * <p>
 * 1) instanciate <code>TableViewerCreator</code>
 * </p>
 * <p>
 * 2) configure, the methods below are used to configure internally style of <code>Table</code> object, so if you want
 * use these methods call them before <code>createTable</code> : <br/>
 * - <code>setLineSelection</code> <br/>
 * - <code>setShowSelection</code> <br/>
 * - <code>setCheckboxInFirstColumn</code> <br/>
 * - <code>setBorderVisible</code> <br/>
 * - <code>setHorizontalScroll</code> <br/>
 * - <code>setVerticalScroll</code> <br/>
 * - <code>setHeaderVisible</code> <br/>
 * - <code>setLinesVisible</code>
 * </p>
 * 
 * <p>
 * 3) optionally call <code>createTable</code> if you need initialize <code>Table</code>'s children components before
 * <code>init</code> call.
 * </p>
 * <p>
 * 4) create <code>TableViewerCreatorColumn</code> columns and configure them. <BR/>
 * Description of the main parameters : <BR/>
 * - <code>beanPropertyName</code> represents the property of each object of your addedObjects which will be read (and
 * write). <BR/>
 * - <code>idProperty</code> (optional) represents the <b>unique id</b> of the column, it is by default the value of
 * <code>beanPropertyName</code>. If the unicity is not respected, a assertion is thrown. <BR/>
 * - set a <code>width</code> or a <code>weight</code> value. <BR/>
 * - see others parameters in <code>TableViewerCreatorColumn</code>
 * </p>
 * <p>
 * 5) set commons value for columns as you want, call them before <code>init</code> : <br/>
 * - <code>setAllColumnsMoveable</code> <br/>
 * - <code>setAllColumnsResizable</code> <br/>
 * - <code>setAllColumnsSortable</code> <br/>
 * </p>
 * 
 * <p>
 * 6) call <code>init</code> method with your addedObjects of objects in parameter.
 * </p>
 * 
 * @see org.eclipse.jface.viewers#TableViewer
 * @see org.eclipse.swt.widgets#Table <br/>
 * 
 * <br/>
 * 
 * $Id: TableViewerCreator.java 7183 2007-11-23 11:03:36Z amaumont $
 * 
 * @param <B> type of objects in the input list of <code>TableViewer</code>
 */
public class TableViewerCreator<B> extends TableViewerCreatorNotModifiable<B> implements IModifiedBeanListenable<B> {

    /**
     * 
     * DOC amaumont MetadataEditorEvent class global comment. Detailled comment <br/>
     * 
     * $Id: TableViewerCreator.java 7183 2007-11-23 11:03:36Z amaumont $
     * 
     */
    public enum CELL_EDITOR_STATE {
        EDITING,
        APPLYING,
        CANCELING;
    }

    private CommandStack commandStack;

    /*
     * The list of listeners who wish to be notified when something significant happens with the proposals.
     */
    private final ListenerList modifiedBeanListeners = new ListenerList();

    /**
     * Constructor.
     * 
     * @param compositeParent used to initialize <code>Table</code>.
     */
    public TableViewerCreator(Composite compositeParent) {
        super(compositeParent);
    }

    /**
     * 
     * Instantiate the <code>Table</code> with the <code>compositeParent</code> as parent, with pre-configured styles
     * and options.
     * 
     * @return
     */
    @Override
    public Table createTable() {
        Table table = super.createTable();
        initCellModifier();

        return table;
    }

    /**
     * DOC amaumont Comment method "initCellModifier".
     */
    protected void initCellModifier() {
        ICellModifier cellModifier = getCellModifier();
        if (cellModifier == null) {
            cellModifier = new DefaultCellModifier(this);
        }
        getTableViewer().setCellModifier(cellModifier);
    }

    /**
     * You must use DefaultCellModifier or a class which extends it to use this method. You can call this method only if
     * you have already called createTable().
     * 
     * 
     * @param tableCellValueModifiedListener
     * @throws UnsupportedOperationException if current CellModifier is not DefaultCellModifier or a class which extends
     * it @
     */
    public void addCellValueModifiedListener(ITableCellValueModifiedListener tableCellValueModifiedListener) {
        ICellModifier cellModifier = getCellModifier();
        if (cellModifier == null) {
            throw new IllegalStateException(Messages.getString("TableViewerCreator.CallMethod.ErrorMsg")); //$NON-NLS-1$
        }
        if (cellModifier instanceof DefaultCellModifier) {
            ((DefaultCellModifier) cellModifier).addCellEditorAppliedListener(tableCellValueModifiedListener);
        } else {
            throw new UnsupportedOperationException(Messages.getString(
                    "TableViewerCreator.CellModifier.ExError", DefaultCellModifier.class)); //$NON-NLS-1$
        }
    }

    /**
     * You must use DefaultCellModifier or a class which extends it to use this method. You can call this method only if
     * you have already called createTable().
     * 
     * @param tableCellValueModifiedListener
     * @throws UnsupportedOperationException if current CellModifier is not DefaultCellModifier or a class which extends
     * it
     */
    public void removeCellValueModifiedListener(ITableCellValueModifiedListener tableCellValueModifiedListener) {
        ICellModifier cellModifier = getCellModifier();
        if (cellModifier == null) {
            throw new IllegalStateException(Messages.getString("TableViewerCreator.CallMethod.ErrorMsg")); //$NON-NLS-1$
        }
        if (cellModifier instanceof DefaultCellModifier) {
            ((DefaultCellModifier) cellModifier).removeCellEditorAppliedListener(tableCellValueModifiedListener);
        } else {
            throw new UnsupportedOperationException(Messages.getString(
                    "TableViewerCreator.CellModifier.ExError", DefaultCellModifier.class)); //$NON-NLS-1$
        }
    }

    /**
     * Change value in model and refresh auomatically the <code>TableViewer</code> if <code>Table</code> is not
     * disposed.
     * 
     * @param currentRowObject
     * @param createNewCommand TODO
     * @param beanPropertyAccessors
     * @param b
     */
    @SuppressWarnings("unchecked")
    public void setBeanValue(TableViewerCreatorColumn column, B currentRowObject, Object value, boolean createNewCommand) {
        boolean listened = modifiedBeanListeners.size() != 0;
        boolean needChange = true;
        Object previousValue = AccessorUtils.get(currentRowObject, column);
        if ("ID_COLUMN_LENGHT".equals(column.getId()) || "ID_COLUMN_PRECISION".equals(column.getId())) {
            if (previousValue == null) {
                if (value == null) {
                    needChange = false;
                }
            } else {
                if (previousValue instanceof Integer) {
                    Integer integer = (Integer) previousValue;
                    if (integer == 0) {
                        if (value != null && value instanceof Integer) {
                            Integer intValue = (Integer) value;
                            if (intValue == 0) {
                                needChange = false;
                            }
                        }
                    } else {
                        if (value instanceof Integer) {
                            Integer intValue = (Integer) value;
                            if (intValue == integer) {
                                needChange = false;
                            }
                        }
                    }
                }
            }
        }
        if (!(value == null && previousValue != null || value != null && !value.equals(previousValue))) {
            needChange = false;
        }

        if (needChange) {

            AccessorUtils.set(column, currentRowObject, value);

            // System.out.println("Set : " + "currentRowObject=" + currentRowObject + " value="+ value );
            if (getTable() != null && !getTable().isDisposed()) {
                // System.out.println(currentRowObject + " refreshed");
                getTableViewer().refresh(currentRowObject);
            }

            ListenableList<B> extendedList = new ListenableList<B>(this.list);
            extendedList.setUseEquals(false);

            ModifiedBeanEvent<B> event = new ModifiedBeanEvent<B>();
            event.bean = currentRowObject;
            event.column = column;
            event.index = extendedList.indexOf(currentRowObject);
            event.newValue = value;
            event.previousValue = previousValue;
            if (listened) {
                fireModifiedBeanEvent(event);
            }
            if (createNewCommand && this.commandStack != null) {
                ModifyBeanValueCommand<B> modifyBeanValueCommand = new ModifyBeanValueCommand<B>(event);
                this.commandStack.execute(modifyBeanValueCommand);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public B getBeanValue(TableViewerCreatorColumn column, Object currentRowObject) {
        return (B) AccessorUtils.get(currentRowObject, column);
    }

    public void addModifiedBeanListener(IModifiedBeanListener<B> modifiedBeanListener) {
        this.modifiedBeanListeners.add(modifiedBeanListener);
    }

    public void removeModifiedBeanListener(IModifiedBeanListener<B> modifiedBeanListener) {
        this.modifiedBeanListeners.remove(modifiedBeanListener);
    }

    @SuppressWarnings("unchecked")
    protected void fireModifiedBeanEvent(ModifiedBeanEvent<B> event) {
        // In all cases, notify listeners of an accepted proposal.
        final Object[] listenerArray = modifiedBeanListeners.getListeners();
        for (Object element : listenerArray) {
            ((IModifiedBeanListener<B>) element).handleEvent(event);
        }
    }

    /**
     * Getter for commandStackAdapter.
     * 
     * @return the commandStackAdapter
     */
    public CommandStack getCommandStack() {
        return this.commandStack;
    }

    /**
     * Sets the commandStackAdapter.
     * 
     * @param commandStack the commandStackAdapter to set
     */
    public void setCommandStack(CommandStack commandStack) {
        this.commandStack = commandStack;
    }

    /**
     * DOC amaumont Comment method "applyActivatedCellEditor".
     */
    public void applyActivatedCellEditor() {
        TableViewer tableViewer = getTableViewer();
        if (tableViewer != null && !tableViewer.getTable().isDisposed()) {
            CellEditor activatedCellEditor = null;
            if (tableViewer.isCellEditorActive()) {
                CellEditor[] cellEditors = tableViewer.getCellEditors();
                for (CellEditor cellEditor : cellEditors) {
                    if (cellEditor != null && cellEditor.isActivated()
                            && cellEditor instanceof ExtendedTextCellEditorWithProposal) {
                        ((ExtendedTextCellEditorWithProposal) cellEditor).fireApplyEditorValue();
                        activatedCellEditor = cellEditor;
                    }
                }
            }
            if (activatedCellEditor != null) {
                Object currentModifiedBean = getModifiedObjectInfo().getCurrentModifiedBean();
                tableViewer.refresh(currentModifiedBean, true);
            }
        }

    }

	public TableViewerCreatorColumn getModifiableColumn(String idProperty) {
		TableViewerCreatorColumnNotModifiable column = super.getColumn(idProperty);
		if (column instanceof TableViewerCreatorColumn) {
			return (TableViewerCreatorColumn) column;
		}
		return null;
	}
}
