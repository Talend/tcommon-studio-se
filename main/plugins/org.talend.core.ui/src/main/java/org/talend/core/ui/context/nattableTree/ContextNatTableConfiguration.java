// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.EditableRule;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IEditableRule;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultBooleanDisplayConverter;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.edit.editor.AbstractCellEditor;
import org.eclipse.nebula.widgets.nattable.edit.editor.CheckBoxCellEditor;
import org.eclipse.nebula.widgets.nattable.edit.editor.ComboBoxCellEditor;
import org.eclipse.nebula.widgets.nattable.edit.editor.TextCellEditor;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.GlazedListsDataProvider;
import org.eclipse.nebula.widgets.nattable.group.ColumnGroupModel;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.painter.cell.CheckBoxPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.ComboBoxPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.ImagePainter;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer.MoveDirectionEnum;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.CellStyleUtil;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.nebula.widgets.nattable.ui.util.CellEdgeEnum;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.nebula.widgets.nattable.widget.EditModeEnum;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.talend.core.model.metadata.types.ContextParameterJavaTypeManager;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.ui.context.ContextTreeTable.ContextTreeNode;
import org.talend.core.ui.context.model.ContextTabChildModel;
import org.talend.core.ui.context.model.table.ContextTableConstants;

/**
 * this one for the main configuration of context NatTable such as the color,editTable,custom control,etc.
 */
public class ContextNatTableConfiguration extends AbstractRegistryConfiguration {

    private IDataProvider dataProvider;

    private ColumnGroupModel columnGroupModel;

    private IContextManager manager;

    /**
     * DOC ldong ContextNatTableConfiguration constructor comment.
     * 
     * @param dataProvider
     */
    public ContextNatTableConfiguration(IDataProvider dataProvider, ColumnGroupModel columnGroupModel, IContextManager manager) {
        super();
        this.dataProvider = dataProvider;
        this.columnGroupModel = columnGroupModel;
        this.manager = manager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.nebula.widgets.nattable.config.IConfiguration#configureRegistry(org.eclipse.nebula.widgets.nattable
     * .config.IConfigRegistry)
     */
    @Override
    public void configureRegistry(IConfigRegistry configRegistry) {
        registerEditableRules(configRegistry);
        registerEditors(configRegistry);
        registerStyleRules(configRegistry);
        registerValidateRules(configRegistry);
    }

    private void registerEditableRules(IConfigRegistry configRegistry) {
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE, IEditableRule.ALWAYS_EDITABLE);

        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE, getEditRule(), DisplayMode.EDIT,
                ContextTableConstants.COLUMN_NAME_PROPERTY);
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE, getEditRule(), DisplayMode.EDIT,
                ContextTableConstants.COLUMN_TYPE_PROPERTY);
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE, getEditRule(), DisplayMode.EDIT,
                ContextTableConstants.COLUMN_CHECK_PROPERTY);
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE, getEditRule(), DisplayMode.EDIT,
                ContextTableConstants.COLUMN_PROMPT_PROPERTY);
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE, getEditRule(), DisplayMode.EDIT,
                ContextTableConstants.COLUMN_CONTEXT_VALUE);
    }

    private void registerStyleRules(IConfigRegistry configRegistry) {
        Style cellStyle = new Style();
        cellStyle.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, GUIHelper.COLOR_WHITE);
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL,
                ContextTableConstants.COLUMN_TYPE_PROPERTY);

        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL,
                ContextTableConstants.COLUMN_NAME_PROPERTY);

        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL,
                ContextTableConstants.COLUMN_CHECK_PROPERTY);

        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL,
                ContextTableConstants.COLUMN_PROMPT_PROPERTY);

        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL,
                ContextTableConstants.COLUMN_CONTEXT_VALUE);
    }

    private void registerValidateRules(IConfigRegistry configRegistry) {
        configRegistry.registerConfigAttribute(EditConfigAttributes.DATA_VALIDATOR, new EventDataValidator(dataProvider),
                DisplayMode.EDIT, ContextTableConstants.COLUMN_CHECK_PROPERTY);
    }

    private IEditableRule getEditRule() {
        EditableRule rule = new EditableRule() {

            @Override
            public boolean isEditable(int columnIndex, int rowIndex) {
                ContextTreeNode rowNode = ((GlazedListsDataProvider<ContextTreeNode>) dataProvider).getRowObject(rowIndex);
                if (ContextNatTableUtils.isEmptyTreeNode(rowNode.getTreeData())) {
                    return false;
                } else {
                    if (rowNode.getTreeData() instanceof ContextTabChildModel || rowNode.getChildren().size() > 0) {
                        return false;
                    } else {
                        return true;
                    }
                }
            }
        };
        return rule;
    }

    private void registerEditors(IConfigRegistry configRegistry) {
        registerColumnFirstTextEditor(configRegistry);
        registerColumnTwoComboxEditor(configRegistry);
        registerColumnThreeCheckBoxEditor(configRegistry);
        registerColumnFourTextEditor(configRegistry);
        registerColumnFiveTextEditor(configRegistry);
    }

    private void registerColumnFirstTextEditor(IConfigRegistry configRegistry) {
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new TextCellEditor(true, true),
                DisplayMode.NORMAL, ContextTableConstants.COLUMN_NAME_PROPERTY);
    }

    private void registerColumnTwoComboxEditor(IConfigRegistry configRegistry) {
        List<String> originalTypes = Arrays.asList(ContextParameterJavaTypeManager.getJavaTypesLabels());
        List<String> finalTypes = new ArrayList<String>();
        for (String type : originalTypes) {
            String newType = type;
            if (type.contains("|")) {
                int index = type.indexOf("|");
                newType = type.substring(index + 1).trim();
                finalTypes.add(newType);
            } else {
                finalTypes.add(type);
            }
        }
        ComboBoxCellEditor comboBoxCellEditor = new ComboBoxCellEditor(finalTypes);
        comboBoxCellEditor.setFreeEdit(true);
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, comboBoxCellEditor, DisplayMode.EDIT,
                ContextTableConstants.COLUMN_TYPE_PROPERTY);

        ComboBoxPainter customPainter = new ComboBoxPainter() {

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.eclipse.nebula.widgets.nattable.painter.cell.CellPainterWrapper#paintCell(org.eclipse.nebula.widgets
             * .nattable.layer.cell.ILayerCell, org.eclipse.swt.graphics.GC, org.eclipse.swt.graphics.Rectangle,
             * org.eclipse.nebula.widgets.nattable.config.IConfigRegistry)
             */
            @Override
            public void paintCell(ILayerCell cell, GC gc, Rectangle adjustedCellBounds, IConfigRegistry configRegistry) {
                ContextTreeNode rowNode = ((GlazedListsDataProvider<ContextTreeNode>) dataProvider).getRowObject(cell
                        .getRowIndex());
                if (rowNode.getChildren().size() > 0) {
                    // do nothing just set back the color,no need paint control here
                    Color backgroundColor = CellStyleUtil.getCellStyle(cell, configRegistry).getAttributeValue(
                            CellStyleAttributes.BACKGROUND_COLOR);
                    if (backgroundColor != null) {
                        Color originalBackground = gc.getBackground();

                        gc.setBackground(backgroundColor);
                        gc.fillRectangle(adjustedCellBounds);

                        gc.setBackground(originalBackground);
                    }

                } else if (rowNode.getTreeData() instanceof ContextTabChildModel) {
                    ((ContextCellPainterDecorator) getWrappedPainter()).setChangeBackgroundColor(true);
                    super.paintCell(cell, gc, adjustedCellBounds, configRegistry);
                } else {
                    ((ContextCellPainterDecorator) getWrappedPainter()).setChangeBackgroundColor(false);
                    super.paintCell(cell, gc, adjustedCellBounds, configRegistry);
                }
            }
        };
        customPainter.setWrappedPainter(new ContextCellPainterDecorator(new ContextTextPainter(false, false, false),
                CellEdgeEnum.RIGHT, new ImagePainter(GUIHelper.getImage("down_2"))));

        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER, customPainter, DisplayMode.NORMAL,
                ContextTableConstants.COLUMN_TYPE_PROPERTY);
    }

    private void registerColumnThreeCheckBoxEditor(IConfigRegistry configRegistry) {
        CheckBoxCellEditor checkCellEditor = new CheckBoxCellEditor();
        CheckBoxPainter customPainter = new CheckBoxPainter() {

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.eclipse.nebula.widgets.nattable.painter.cell.ImagePainter#paintCell(org.eclipse.nebula.widgets.nattable
             * .layer.cell.ILayerCell, org.eclipse.swt.graphics.GC, org.eclipse.swt.graphics.Rectangle,
             * org.eclipse.nebula.widgets.nattable.config.IConfigRegistry)
             */
            @Override
            public void paintCell(ILayerCell cell, GC gc, Rectangle bounds, IConfigRegistry configRegistry) {
                ContextTreeNode rowNode = ((GlazedListsDataProvider<ContextTreeNode>) dataProvider).getRowObject(cell
                        .getRowIndex());
                if (rowNode.getChildren().size() > 0) {
                    Color backgroundColor = CellStyleUtil.getCellStyle(cell, configRegistry).getAttributeValue(
                            CellStyleAttributes.BACKGROUND_COLOR);
                    if (backgroundColor != null) {
                        Color originalBackground = gc.getBackground();

                        gc.setBackground(backgroundColor);
                        gc.fillRectangle(bounds);

                        gc.setBackground(originalBackground);
                    }
                } else {
                    super.paintCell(cell, gc, bounds, configRegistry);
                }
            }
        };
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER, customPainter, DisplayMode.NORMAL,
                ContextTableConstants.COLUMN_CHECK_PROPERTY);
        configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER, new DefaultBooleanDisplayConverter(),
                DisplayMode.NORMAL, ContextTableConstants.COLUMN_CHECK_PROPERTY);
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, checkCellEditor, DisplayMode.EDIT,
                ContextTableConstants.COLUMN_CHECK_PROPERTY);
    }

    private void registerColumnFourTextEditor(IConfigRegistry configRegistry) {
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new TextCellEditor(true, true),
                DisplayMode.NORMAL, ContextTableConstants.COLUMN_PROMPT_PROPERTY);
    }

    private void registerColumnFiveTextEditor(IConfigRegistry configRegistry) {
        CustomTextCellEditor cutomCellEditor = new CustomTextCellEditor(true, true);
        cutomCellEditor.setFreeEdit(true);
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, cutomCellEditor, DisplayMode.EDIT,
                ContextTableConstants.COLUMN_CONTEXT_VALUE);
    }

    class CustomTextCellEditor extends AbstractCellEditor {

        private final boolean commitOnUpDown;

        private final boolean moveSelectionOnEnter;

        protected boolean freeEdit = false;

        protected boolean commitOnEnter = true;

        /*
         * The wrapped editor control.
         */
        private ContextValuesNatText buttonText;

        public CustomTextCellEditor(boolean commitOnUpDown, boolean moveSelectionOnEnter) {
            this.commitOnUpDown = commitOnUpDown;
            this.moveSelectionOnEnter = moveSelectionOnEnter;
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
            int style = this.editMode == EditModeEnum.INLINE ? SWT.NONE : SWT.BORDER;
            if (!this.freeEdit) {
                style |= SWT.READ_ONLY;
            }
            int cellColumnIndex = this.layerCell.getColumnIndex();
            int cellRowIndex = this.layerCell.getRowIndex();
            IContextParameter realPara = null;
            if (columnGroupModel != null && columnGroupModel.isPartOfAGroup(cellColumnIndex)) {
                String columnGroupName = columnGroupModel.getColumnGroupByIndex(cellColumnIndex).getName();

                ContextTreeNode rowNode = ((GlazedListsDataProvider<ContextTreeNode>) dataProvider).getRowObject(cellRowIndex);

                realPara = ContextNatTableUtils.getRealParameter(manager, columnGroupName, rowNode.getTreeData());
            }
            final ContextValuesNatText text = new ContextValuesNatText(parent, this.cellStyle, realPara, style);

            text.setCursor(new Cursor(Display.getDefault(), SWT.CURSOR_IBEAM));

            addTextListener(text);
            return text;

        }

        protected void addTextListener(final ContextValuesNatText text) {
            text.addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(KeyEvent event) {
                    if (commitOnEnter && (event.keyCode == SWT.CR || event.keyCode == SWT.KEYPAD_CR)) {
                        MoveDirectionEnum move = MoveDirectionEnum.NONE;
                        if (moveSelectionOnEnter && editMode == EditModeEnum.INLINE) {
                            if (event.stateMask == 0) {
                                move = MoveDirectionEnum.DOWN;
                            } else if (event.stateMask == SWT.SHIFT) {
                                move = MoveDirectionEnum.UP;
                            }
                        }

                        commit(move);
                    } else if (event.keyCode == SWT.ESC && event.stateMask == 0) {
                        close();
                    } else if (commitOnUpDown && editMode == EditModeEnum.INLINE) {
                        if (event.keyCode == SWT.ARROW_UP) {
                            commit(MoveDirectionEnum.UP);
                        } else if (event.keyCode == SWT.ARROW_DOWN) {
                            commit(MoveDirectionEnum.DOWN);
                        }
                    }
                }

            });

            text.addFocusListener(new FocusAdapter() {

                @Override
                public void focusLost(FocusEvent e) {
                    commit(MoveDirectionEnum.NONE, editMode == EditModeEnum.INLINE);
                }
            });
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.nebula.widgets.nattable.edit.editor.ICellEditor#getEditorControl()
         */
        @Override
        public Control getEditorControl() {
            return this.buttonText;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.nebula.widgets.nattable.edit.editor.ICellEditor#getEditorValue()
         */
        @Override
        public Object getEditorValue() {
            return this.buttonText.getValue();
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.nebula.widgets.nattable.edit.editor.ICellEditor#setEditorValue(java.lang.Object)
         */
        @Override
        public void setEditorValue(Object value) {
            this.buttonText.setValue(value != null && value.toString().length() > 0 ? value.toString() : "");

        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.eclipse.nebula.widgets.nattable.edit.editor.AbstractCellEditor#activateCell(org.eclipse.swt.widgets.Composite
         * , java.lang.Object)
         */
        @Override
        protected Control activateCell(Composite parent, Object originalCanonicalValue) {
            this.buttonText = (ContextValuesNatText) createEditorControl(parent);

            setCanonicalValue(originalCanonicalValue);

            return this.buttonText;
        }

        /**
         * Getter for freeEdit.
         * 
         * @return the freeEdit
         */
        public boolean isFreeEdit() {
            return this.freeEdit;
        }

        /**
         * Sets the freeEdit.
         * 
         * @param freeEdit the freeEdit to set
         */
        public void setFreeEdit(boolean freeEdit) {
            this.freeEdit = freeEdit;
        }

    }
}
