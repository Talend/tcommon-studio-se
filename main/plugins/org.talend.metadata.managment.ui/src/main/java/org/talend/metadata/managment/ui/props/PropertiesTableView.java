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
package org.talend.metadata.managment.ui.props;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView;
import org.talend.commons.ui.swt.advanced.dataeditor.ExtendedToolbarView;
import org.talend.commons.ui.swt.advanced.dataeditor.button.AddPushButtonForExtendedTable;
import org.talend.commons.ui.swt.advanced.dataeditor.button.PastePushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.PastePushButtonForExtendedTable;
import org.talend.commons.ui.swt.advanced.dataeditor.commands.ExtendedTablePasteCommand;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.commons.utils.data.list.ListenableListEvent;

/**
 * 
 * created by ycbai on 2015年1月4日 Detailled comment
 *
 */
public class PropertiesTableView extends AbstractDataTableEditorView<Map<String, Object>> {

    public static final String DEFAULT_KEY_NAME = "PROPERTY"; //$NON-NLS-1$

    public static final String DEFAULT_VALUE_NAME = "VALUE"; //$NON-NLS-1$

    public static final String DEFAULT_KEY_COLUMN_NAME = "Property"; //$NON-NLS-1$

    public static final String DEFAULT_VALUE_COLUMN_NAME = "Value"; //$NON-NLS-1$

    public PropertiesTableView(Composite parent, PropertiesFieldModel model) {
        super(parent, SWT.NONE, model, false, true, false);
    }

    @Override
    protected void handleBeforeListenableListOperationEvent(ListenableListEvent<Map<String, Object>> event) {
        super.handleBeforeListenableListOperationEvent(event);
    }

    @Override
    protected void handleAfterListenableListOperationEvent(ListenableListEvent<Map<String, Object>> event) {
        super.handleAfterListenableListOperationEvent(event);
    }

    @Override
    protected void setTableViewerCreatorOptions(TableViewerCreator<Map<String, Object>> newTableViewerCreator) {
        super.setTableViewerCreatorOptions(newTableViewerCreator);
    }

    @Override
    protected void createColumns(TableViewerCreator<Map<String, Object>> tableViewerCreator, Table table) {
        createKeyColumn(tableViewerCreator);
        createValueColumn(tableViewerCreator);
    }

    private TableViewerCreatorColumn createValueColumn(TableViewerCreator<Map<String, Object>> tableViewerCreator) {
        TableViewerCreatorColumn column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(getValueColumnName());
        if (getHideColumns().contains(column.getTitle())) {
            return null;
        }
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<Map<String, Object>, String>() {

            @Override
            public String get(Map<String, Object> bean) {
                return (String) bean.get(getValueName());
            }

            @Override
            public void set(Map<String, Object> bean, String value) {
                bean.put(getValueName(), value);
            }

        });
        column.setCellEditor(new TextCellEditor(tableViewerCreator.getTable()));

        column.setModifiable(true);
        column.setWeight(30);
        column.setMinimumWidth(50);
        column.setDefaultInternalValue(""); //$NON-NLS-1$
        return column;
    }

    private TableViewerCreatorColumn createKeyColumn(TableViewerCreator<Map<String, Object>> tableViewerCreator) {
        TableViewerCreatorColumn column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(getKeyColumnName());
        if (getHideColumns().contains(column.getTitle())) {
            return null;
        }
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<Map<String, Object>, String>() {

            @Override
            public String get(Map<String, Object> bean) {
                return (String) bean.get(getKeyName());
            }

            @Override
            public void set(Map<String, Object> bean, String value) {
                bean.put(getKeyName(), value);
            }

        });
        column.setCellEditor(new TextCellEditor(tableViewerCreator.getTable()));

        column.setModifiable(true);
        column.setWeight(30);
        column.setMinimumWidth(50);
        column.setDefaultInternalValue(""); //$NON-NLS-1$
        return column;
    }

    public List<String> getHideColumns() {
        return new ArrayList<>();
    }

    public PropertiesFieldModel getModel() {
        return (PropertiesFieldModel) getExtendedTableModel();
    }

    @Override
    protected ExtendedToolbarView initToolBar() {
        return new ExtendedToolbarView(getMainComposite(), SWT.NONE, getExtendedTableViewer()) {

            @Override
            protected AddPushButtonForExtendedTable createAddPushButton() {
                return new AddPushButtonForExtendedTable(this.toolbar, getExtendedTableViewer()) {

                    @Override
                    protected Object getObjectToAdd() {
                        Map<String, Object> hpt = getModel().createHadoopPropertiesType();
                        hpt.put(getKeyName(), "new line");
                        hpt.put(getValueName(), "");
                        return hpt;
                    }

                };
            }

            @Override
            protected PastePushButton createPastePushButton() {
                return new PastePushButtonForExtendedTable(toolbar, extendedTableViewer) {

                    @Override
                    protected Command getCommandToExecute(ExtendedTableModel extendedTableModel, Integer indexWhereInsert) {
                        return new ExtendedTablePasteCommand(extendedTableModel, indexWhereInsert) {

                            @Override
                            public List<Map<String, Object>> createPastableBeansList(ExtendedTableModel extendedTableModel,
                                    List copiedObjectsList) {
                                List list = new ArrayList();
                                PropertiesFieldModel fieldsModel = (PropertiesFieldModel) extendedTableModel;
                                for (Object current : copiedObjectsList) {
                                    if (current instanceof Map) {
                                        Map<String, Object> original = (Map<String, Object>) current;
                                        Map<String, Object> copy = fieldsModel.createHadoopPropertiesType();
                                        copy.putAll(original);
                                        list.add(copy);
                                    }
                                }
                                return list;
                            }
                        };
                    }

                };
            }
        };
    }

    public String getKeyName() {
        return DEFAULT_KEY_NAME;
    }

    public String getValueName() {
        return DEFAULT_VALUE_NAME;
    }

    public String getKeyColumnName() {
        return DEFAULT_KEY_COLUMN_NAME;
    }

    public String getValueColumnName() {
        return DEFAULT_VALUE_COLUMN_NAME;
    }

}
