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
import org.eclipse.nebula.widgets.nattable.edit.config.DialogErrorHandling;
import org.eclipse.nebula.widgets.nattable.edit.editor.CheckBoxCellEditor;
import org.eclipse.nebula.widgets.nattable.edit.editor.ComboBoxCellEditor;
import org.eclipse.nebula.widgets.nattable.edit.editor.TextCellEditor;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.GlazedListsDataProvider;
import org.eclipse.nebula.widgets.nattable.group.ColumnGroupModel;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.painter.cell.CheckBoxPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.ComboBoxPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.ImagePainter;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.CellStyleUtil;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.nebula.widgets.nattable.ui.util.CellEdgeEnum;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.talend.core.model.metadata.types.ContextParameterJavaTypeManager;
import org.talend.core.model.process.IContextManager;
import org.talend.core.ui.context.ContextTreeTable.ContextTreeNode;
import org.talend.core.ui.context.IContextModelManager;
import org.talend.core.ui.context.model.ContextTabChildModel;
import org.talend.core.ui.context.model.table.ContextTableConstants;

/**
 * this one for the main configuration of context NatTable such as the color,editTable,custom control,etc.
 */
public class ContextNatTableConfiguration extends AbstractRegistryConfiguration {

    private IDataProvider dataProvider;

    private ColumnGroupModel columnGroupModel;

    private IContextManager manager;

    private IContextModelManager modelManager;

    /**
     * DOC ldong ContextNatTableConfiguration constructor comment.
     * 
     * @param dataProvider
     */
    public ContextNatTableConfiguration(IDataProvider dataProvider, ColumnGroupModel columnGroupModel, IContextManager manager,
            IContextModelManager modelManager) {
        super();
        this.dataProvider = dataProvider;
        this.columnGroupModel = columnGroupModel;
        this.manager = manager;
        this.modelManager = modelManager;
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
        registerErrorHandlingStrategies(configRegistry);
    }

    private void registerEditableRules(IConfigRegistry configRegistry) {
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE, IEditableRule.ALWAYS_EDITABLE);

        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE, getEditRule(), DisplayMode.EDIT,
                ContextTableConstants.COLUMN_NAME_PROPERTY);
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE, getEditRule(), DisplayMode.EDIT,
                ContextTableConstants.COLUMN_TYPE_PROPERTY);
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE, getEditRule(), DisplayMode.EDIT,
                ContextTableConstants.COLUMN_COMMENT_PROPERTY);
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE, getEditRule(), DisplayMode.EDIT,
                ContextTableConstants.COLUMN_CHECK_PROPERTY);
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE, getEditRule(), DisplayMode.EDIT,
                ContextTableConstants.COLUMN_PROMPT_PROPERTY);
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE, getEditRule(), DisplayMode.EDIT,
                ContextTableConstants.COLUMN_CONTEXT_VALUE);
    }

    private void registerStyleRules(IConfigRegistry configRegistry) {
        // register the default cell fg/bg colour for the natTable
        Style cellStyleDefault = new Style();
        cellStyleDefault.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, GUIHelper.COLOR_WHITE);
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyleDefault, DisplayMode.NORMAL,
                ContextTableConstants.COLUMN_TYPE_PROPERTY);

        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyleDefault, DisplayMode.NORMAL,
                ContextTableConstants.COLUMN_NAME_PROPERTY);

        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyleDefault, DisplayMode.NORMAL,
                ContextTableConstants.COLUMN_COMMENT_PROPERTY);

        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyleDefault, DisplayMode.NORMAL,
                ContextTableConstants.COLUMN_CHECK_PROPERTY);

        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyleDefault, DisplayMode.NORMAL,
                ContextTableConstants.COLUMN_PROMPT_PROPERTY);

        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyleDefault, DisplayMode.NORMAL,
                ContextTableConstants.COLUMN_CONTEXT_VALUE);

        Style cellStyleSelect = new Style();
        cellStyleSelect.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, GUIHelper.COLOR_TITLE_INACTIVE_BACKGROUND);
        cellStyleSelect.setAttributeValue(CellStyleAttributes.FOREGROUND_COLOR, GUIHelper.COLOR_RED);
        cellStyleSelect.setAttributeValue(CellStyleAttributes.FONT, GUIHelper.DEFAULT_FONT);
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyleSelect, DisplayMode.SELECT,
                ContextTableConstants.COLUMN_TYPE_PROPERTY);

        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyleSelect, DisplayMode.SELECT,
                ContextTableConstants.COLUMN_NAME_PROPERTY);

        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyleSelect, DisplayMode.SELECT,
                ContextTableConstants.COLUMN_COMMENT_PROPERTY);

        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyleSelect, DisplayMode.SELECT,
                ContextTableConstants.COLUMN_CHECK_PROPERTY);

        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyleSelect, DisplayMode.SELECT,
                ContextTableConstants.COLUMN_PROMPT_PROPERTY);

        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyleSelect, DisplayMode.SELECT,
                ContextTableConstants.COLUMN_CONTEXT_VALUE);
    }

    private void registerValidateRules(IConfigRegistry configRegistry) {
        configRegistry.registerConfigAttribute(EditConfigAttributes.DATA_VALIDATOR,
                new EventDataValidator(dataProvider, manager), DisplayMode.EDIT, ContextTableConstants.COLUMN_NAME_PROPERTY);
    }

    private void registerErrorHandlingStrategies(IConfigRegistry configRegistry) {
        configRegistry.registerConfigAttribute(EditConfigAttributes.VALIDATION_ERROR_HANDLER, new DialogErrorHandling(),
                DisplayMode.EDIT, ContextTableConstants.COLUMN_NAME_PROPERTY);
    }

    private IEditableRule getEditRule() {
        EditableRule rule = new EditableRule() {

            @Override
            public boolean isEditable(int columnIndex, int rowIndex) {
                if (modelManager != null && modelManager.isReadOnly()) {
                    return false;
                }
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
        registerColumnSixTextEditor(configRegistry);
    }

    private void registerColumnFirstTextEditor(IConfigRegistry configRegistry) {
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new TextCellEditor(true, true),
                DisplayMode.EDIT, ContextTableConstants.COLUMN_NAME_PROPERTY);
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
        customPainter.setWrappedPainter(new ContextCellPainterDecorator(new ContextAutoResizeTextPainter(false, false, true),
                CellEdgeEnum.RIGHT, new ImagePainter(GUIHelper.getImage("down_2"))));

        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER, customPainter, DisplayMode.NORMAL,
                ContextTableConstants.COLUMN_TYPE_PROPERTY);
    }

    private void registerColumnThreeCheckBoxEditor(IConfigRegistry configRegistry) {
        CheckBoxCellEditor checkCellEditor = new CheckBoxCellEditor();
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, checkCellEditor, DisplayMode.EDIT,
                ContextTableConstants.COLUMN_CHECK_PROPERTY);
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
        configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER, new ContextCheckDisplayConverter(),
                DisplayMode.NORMAL, ContextTableConstants.COLUMN_CHECK_PROPERTY);
    }

    private void registerColumnFourTextEditor(IConfigRegistry configRegistry) {
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new TextCellEditor(true, true),
                DisplayMode.NORMAL, ContextTableConstants.COLUMN_PROMPT_PROPERTY);
    }

    private void registerColumnFiveTextEditor(IConfigRegistry configRegistry) {
        ProxyDynamicCellEditor cutomCellEditor = new ProxyDynamicCellEditor(dataProvider, columnGroupModel, manager);
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, cutomCellEditor, DisplayMode.EDIT,
                ContextTableConstants.COLUMN_CONTEXT_VALUE);
    }
    
    private void registerColumnSixTextEditor(IConfigRegistry configRegistry) {
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new TextCellEditor(true, true),
                DisplayMode.NORMAL, ContextTableConstants.COLUMN_COMMENT_PROPERTY);
    }

    public class ContextCheckDisplayConverter extends DefaultBooleanDisplayConverter {

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.eclipse.nebula.widgets.nattable.data.convert.DefaultBooleanDisplayConverter#displayToCanonicalValue(java
         * .lang.Object)
         */
        @Override
        public Object displayToCanonicalValue(Object displayValue) {
            return super.displayToCanonicalValue(displayValue);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.eclipse.nebula.widgets.nattable.data.convert.DefaultBooleanDisplayConverter#canonicalToDisplayValue(java
         * .lang.Object)
         */
        @Override
        public Object canonicalToDisplayValue(Object canonicalValue) {
            if (canonicalValue.equals("")) {
                return null;
            }
            return super.canonicalToDisplayValue(canonicalValue);
        }

    }
}
