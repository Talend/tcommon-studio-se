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
package org.talend.commons.ui.swt.tableviewer.celleditor;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.ui.runtime.i18n.Messages;
import org.talend.commons.ui.runtime.swt.tableviewer.data.ModifiedObjectInfo;
import org.talend.commons.ui.runtime.thread.AsynchronousThreading;
import org.talend.commons.ui.runtime.utils.ControlUtils;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator.CELL_EDITOR_STATE;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;

/**
 * Validate column value and show a dialog message if value is not valid.
 * 
 * $Id: DialogErrorForCellEditorListener.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public abstract class DialogErrorForCellEditorListener implements ICellEditorListener {

    protected TextCellEditor cellEditor;

    protected TableViewerCreatorColumn column;

    protected TableViewerCreator tableViewerCreator;

    /**
     * DOC amaumont CellEditorListener constructor comment.
     * 
     * @param column
     * @param cellEditor
     */
    public DialogErrorForCellEditorListener(TextCellEditor cellEditor, TableViewerCreatorColumn column) {
        super();
        this.cellEditor = cellEditor;
        this.column = column;
        this.tableViewerCreator = column.getTableViewerCreator();
    }

    Object lastValidValue = null;

    public void applyEditorValue() {
        ModifiedObjectInfo modifiedObjectInfo = tableViewerCreator.getModifiedObjectInfo();
        Object bean = modifiedObjectInfo.getCurrentModifiedBean() != null ? modifiedObjectInfo.getCurrentModifiedBean()
                : modifiedObjectInfo.getPreviousModifiedBean();
        String text = ControlUtils.getText(cellEditor.getControl());
        onValueChanged(text, true, bean, CELL_EDITOR_STATE.APPLYING);
        lastValidValue = null;
    }

    public void cancelEditor() {
        ModifiedObjectInfo modifiedObjectInfo = tableViewerCreator.getModifiedObjectInfo();
        String originalName = (String) modifiedObjectInfo.getOriginalPropertyBeanValue();
        ControlUtils.setText(cellEditor.getControl(), originalName);
        onValueChanged(originalName, false, modifiedObjectInfo.getCurrentModifiedBean(), CELL_EDITOR_STATE.CANCELING);
        lastValidValue = null;
    }

    public void editorValueChanged(boolean oldValidState, boolean newValidState) {
        ModifiedObjectInfo modifiedObjectInfo = tableViewerCreator.getModifiedObjectInfo();
        onValueChanged(((Text) cellEditor.getControl()).getText(), false, modifiedObjectInfo.getCurrentModifiedBean(),
                CELL_EDITOR_STATE.EDITING);
    }

    protected void onValueChanged(final String newValue, boolean showAlertIfError, final Object currentModifiedBean,
            CELL_EDITOR_STATE state) {
        final Text text = (Text) cellEditor.getControl();
        final ModifiedObjectInfo modifiedObjectInfo = tableViewerCreator.getModifiedObjectInfo();
        final Object originalValue = modifiedObjectInfo.getOriginalPropertyBeanValue();
        lastValidValue = lastValidValue != null && state == CELL_EDITOR_STATE.EDITING ? lastValidValue : originalValue;

        int beanPosition = tableViewerCreator.getInputList().indexOf(currentModifiedBean);
        final String errorMessage = validateValue(newValue, beanPosition);
        if (errorMessage == null) {
            newValidValueTyped(beanPosition, lastValidValue, newValue, state);
            text.setBackground(text.getDisplay().getSystemColor(SWT.COLOR_WHITE));
            lastValidValue = newValue;
        } else {
            text.setBackground(text.getDisplay().getSystemColor(SWT.COLOR_RED));
            if (showAlertIfError) {
                final Point selection = text.getSelection();
                text.setText(lastValidValue.toString());

                new AsynchronousThreading(20, false, text.getDisplay(), new Runnable() {

                    public void run() {
                        if (errorMessage.equals("")) {
                            MessageDialog.openError(text.getShell(),
                                    Messages.getString("DialogErrorForCellEditorListener.Error.MsgDialogTitle"), //$NON-NLS-1$
                                    Messages.getString("ErrorDialogWidthDetailArea.ErrorMessage.ColumnText.NotValid", //$NON-NLS-1$
                                            newValue, originalValue == null ? "" : originalValue.toString())); //$NON-NLS-1$
                            final int columnPosition = tableViewerCreator.getColumns().indexOf(column);
                            tableViewerCreator.getTableViewer().editElement(currentModifiedBean, columnPosition);
                            // Added by Marvin Wang on Dec. 12, 2012 for bug TDI-24174 and it also replaces the bug
                            // TDI-23253.
                            text.setText(originalValue == null ? "" : originalValue.toString()); //$NON-NLS-1$
                            text.setSelection(selection.x, selection.y);
                        } else {
                            MessageDialog.openError(text.getShell(),
                                    Messages.getString("DialogErrorForCellEditorListener.Error.MsgDialogTitle"), errorMessage); //$NON-NLS-1$
                            final int columnPosition = tableViewerCreator.getColumns().indexOf(column);
                            tableViewerCreator.getTableViewer().editElement(currentModifiedBean, columnPosition);
                            text.setText(originalValue == null ? "" : originalValue.toString());
                            text.setSelection(selection.x, selection.y);
                        }
                    }
                }).start();
            }
        }
    }

    /**
     * Implement this to execute treatments when new value is valid.
     * 
     * @param itemIndex TODO
     * @param previousValue
     * @param newValue
     * @param state
     */
    public abstract void newValidValueTyped(int itemIndex, final Object previousValue, final Object newValue,
            CELL_EDITOR_STATE state);

    /**
     * DOC amaumont Comment method "validateValue".
     * 
     * @param value to validate
     * @param beanPosition
     * @return error message if value is invalid, null else
     */
    public abstract String validateValue(String value, int beanPosition);

}
