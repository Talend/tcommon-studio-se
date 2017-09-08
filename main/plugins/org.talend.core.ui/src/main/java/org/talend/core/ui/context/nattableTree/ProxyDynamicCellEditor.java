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
package org.talend.core.ui.context.nattableTree;

import java.util.Arrays;
import java.util.List;

import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.edit.ICellEditHandler;
import org.eclipse.nebula.widgets.nattable.edit.InlineEditHandler;
import org.eclipse.nebula.widgets.nattable.edit.editor.AbstractCellEditor;
import org.eclipse.nebula.widgets.nattable.edit.editor.ComboBoxCellEditor;
import org.eclipse.nebula.widgets.nattable.edit.editor.ICellEditor;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.GlazedListsDataProvider;
import org.eclipse.nebula.widgets.nattable.group.ColumnGroupModel;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer.MoveDirectionEnum;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.ui.context.ContextTreeTable.ContextTreeNode;

/**
 * 
 * This one is a proxy for dynamic cellEditor for different type.
 */
public class ProxyDynamicCellEditor extends AbstractCellEditor {

    private ICellEditor dynamicEditor;

    private IDataProvider dataProvider;

    private ColumnGroupModel columnGroupModel;

    private IContextManager manager;

    /**
     * DOC ldong ProxyDynamicCellEditor constructor comment.
     * 
     * @param dataProvider
     * @param columnGroupModel
     * @param manager
     */
    public ProxyDynamicCellEditor(IDataProvider dataProvider, ColumnGroupModel columnGroupModel, IContextManager manager) {
        super();
        this.dataProvider = dataProvider;
        this.columnGroupModel = columnGroupModel;
        this.manager = manager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.nebula.widgets.nattable.edit.editor.ICellEditor#getEditorValue()
     */
    @Override
    public Object getEditorValue() {
        if (!getEditorControl().isDisposed()) {
            return dynamicEditor.getEditorValue();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.nebula.widgets.nattable.edit.editor.ICellEditor#setEditorValue(java.lang.Object)
     */
    @Override
    public void setEditorValue(Object value) {
        dynamicEditor.setEditorValue(value);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.nebula.widgets.nattable.edit.editor.ICellEditor#getEditorControl()
     */
    @Override
    public Control getEditorControl() {
        return dynamicEditor.getEditorControl();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.nebula.widgets.nattable.edit.editor.ICellEditor#createEditorControl(org.eclipse.swt.widgets.Composite
     * )
     */
    @Override
    public Control createEditorControl(Composite parent) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.nebula.widgets.nattable.edit.editor.AbstractCellEditor#activateCell(org.eclipse.swt.widgets.Composite
     * , java.lang.Object)
     */
    @Override
    public Control activateCell(Composite parent, Object originalCanonicalValue) {
        ICellEditHandler editHandler = new InlineEditHandler(this.layerCell.getLayer(), this.layerCell.getColumnPosition(),
                this.layerCell.getRowPosition());
        int cellColumnIndex = this.layerCell.getColumnIndex();
        int cellRowIndex = this.layerCell.getRowIndex();
        if (columnGroupModel != null && columnGroupModel.isPartOfAGroup(cellColumnIndex)) {
            String columnGroupName = columnGroupModel.getColumnGroupByIndex(cellColumnIndex).getName();

            ContextTreeNode rowNode = ((GlazedListsDataProvider<ContextTreeNode>) dataProvider).getRowObject(cellRowIndex);

            IContextParameter realPara = ContextNatTableUtils.getRealParameter(manager, columnGroupName, rowNode.getTreeData());

            if (NatTableCellEditorFactory.isBoolean(realPara.getType())) {
                final List<String> list = Arrays.asList(NatTableCellEditorFactory.BOOLEANS);
                ComboBoxCellEditor comboBoxCellEditor = new ComboBoxCellEditor(list);
                comboBoxCellEditor.setFreeEdit(false);
                dynamicEditor = comboBoxCellEditor;
                Object displayDefaultValue = false;
                // the combox need a default value at least
                if (!originalCanonicalValue.equals("")) {
                    displayDefaultValue = originalCanonicalValue;
                }
                return dynamicEditor.activateCell(parent, displayDefaultValue, editMode, editHandler, layerCell, configRegistry);
            } else {
                dynamicEditor = new CustomTextCellEditor(realPara, this.cellStyle, true, true);
                ((CustomTextCellEditor) dynamicEditor).setFreeEdit(true);
                return dynamicEditor.activateCell(parent, originalCanonicalValue, editMode, editHandler, layerCell,
                        configRegistry);
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.nebula.widgets.nattable.edit.editor.AbstractCellEditor#commit(org.eclipse.nebula.widgets.nattable
     * .selection.SelectionLayer.MoveDirectionEnum, boolean, boolean)
     */
    @Override
    public boolean commit(MoveDirectionEnum direction, boolean closeAfterCommit, boolean skipValidation) {
        if (dynamicEditor != null) {
            return dynamicEditor.commit(direction, closeAfterCommit, skipValidation);
        }
        return super.commit(direction, closeAfterCommit, skipValidation);
    }

}
