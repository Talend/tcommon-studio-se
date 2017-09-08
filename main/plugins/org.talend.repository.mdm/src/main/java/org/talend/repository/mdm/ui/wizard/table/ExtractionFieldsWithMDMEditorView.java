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
package org.talend.repository.mdm.ui.wizard.table;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.talend.commons.ui.runtime.swt.tableviewer.behavior.CellEditorValueAdapter;
import org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView;
import org.talend.commons.ui.swt.advanced.dataeditor.ExtendedToolbarView;
import org.talend.commons.ui.swt.advanced.dataeditor.button.AddPushButtonForExtendedTable;
import org.talend.commons.ui.swt.advanced.dataeditor.button.PastePushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.PastePushButtonForExtendedTable;
import org.talend.commons.ui.swt.advanced.dataeditor.commands.ExtendedTablePasteCommand;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.commons.ui.swt.proposal.TextCellEditorWithProposal;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator.CELL_EDITOR_STATE;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.ui.swt.tableviewer.celleditor.DialogErrorForCellEditorListener;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.commons.utils.data.list.ListenableListEvent;
import org.talend.commons.utils.data.list.UniqueStringGenerator;
import org.talend.core.model.metadata.builder.connection.ConceptTarget;
import org.talend.repository.mdm.i18n.Messages;
import org.talend.repository.mdm.model.MDMXSDExtractorFieldModel;
import org.talend.repository.mdm.ui.wizard.dnd.MDMLinker;

/**
 * DOC hwang class global comment. Detailled comment
 */
public class ExtractionFieldsWithMDMEditorView extends AbstractDataTableEditorView<ConceptTarget> {

    public static final String ID_COLUMN_NAME = "ID_COLUMN_NAME"; //$NON-NLS-1$

    private TextCellEditorWithProposal xPathCellEditor;

    private TableViewerCreatorColumn xPathColumn;

    private MDMLinker linker;

    public ExtractionFieldsWithMDMEditorView(MDMXSDExtractorFieldModel model, Composite parent, int styleChild) {
        this(model, parent, styleChild, false);
    }

    public ExtractionFieldsWithMDMEditorView(MDMXSDExtractorFieldModel model, Composite parent) {
        this(model, parent, SWT.NONE, false);
    }

    /**
     * TargetSchemaTableEditorView2 constructor comment.
     * 
     * @param parent
     * @param styleChild
     * @param showDbTypeColumn
     */
    public ExtractionFieldsWithMDMEditorView(MDMXSDExtractorFieldModel model, Composite parent, int styleChild,
            boolean showDbTypeColumn) {
        super(parent, styleChild, model);
    }

    /**
     * Getter for xPathCellEditor.
     * 
     * @return the xPathCellEditor
     */
    public TextCellEditorWithProposal getXPathCellEditor() {
        return this.xPathCellEditor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView#handleBeforeListenableListOperationEvent
     * (org.talend.commons.utils.data.list.ListenableListEvent)
     */
    @Override
    protected void handleBeforeListenableListOperationEvent(ListenableListEvent<ConceptTarget> event) {
        super.handleBeforeListenableListOperationEvent(event);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.commons.ui.swt.extended.macrotable.AbstractExtendedTableViewer#handleListenableListEvent(org.talend
     * .commons.utils.data.list.ListenableListEvent)
     */
    @Override
    protected void handleAfterListenableListOperationEvent(ListenableListEvent<ConceptTarget> event) {
        super.handleAfterListenableListOperationEvent(event);
        // if (event.type == TYPE.REMOVED) {
        if (linker != null) {
            linker.getBackgroundRefresher().refreshBackground();
        }
        // }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.commons.ui.swt.extended.macrotable.AbstractExtendedTableViewer#setTableViewerCreatorOptions(org.talend
     * .commons.ui.swt.tableviewer.TableViewerCreator)
     */
    @Override
    protected void setTableViewerCreatorOptions(TableViewerCreator<ConceptTarget> newTableViewerCreator) {
        super.setTableViewerCreatorOptions(newTableViewerCreator);
        newTableViewerCreator.setFirstVisibleColumnIsSelection(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.commons.ui.swt.advanced.macrotable.AbstractExtendedTableViewer#createColumns(org.talend.commons.ui
     * .swt.tableviewer.TableViewerCreator, org.eclipse.swt.widgets.Table)
     */
    @Override
    protected void createColumns(TableViewerCreator<ConceptTarget> tableViewerCreator, final Table table) {
        CellEditorValueAdapter intValueAdapter = new CellEditorValueAdapter() {

            @Override
            public Object getOriginalTypedValue(final CellEditor cellEditor, Object value) {
                try {
                    return new Integer(value.toString());
                } catch (Exception ex) {
                    return null;
                }
            }

            @Override
            public Object getCellEditorTypedValue(final CellEditor cellEditor, Object value) {
                if (value != null) {
                    return String.valueOf(value);
                }
                return ""; //$NON-NLS-1$
            }
        };

        // //////////////////////////////////////////////////////////////////////////////////////

        // column for mouse selection
        TableViewerCreatorColumn column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(""); //$NON-NLS-1$
        column.setDefaultInternalValue(""); //$NON-NLS-1$
        column.setWidth(15);

        // //////////////////////////////////////////////////////////////////////////////////////
        // X Path Query

        column = new TableViewerCreatorColumn(tableViewerCreator);
        xPathColumn = column;
        column.setTitle(Messages.getString("ExtractionFieldsWithXPathEditorView.columnTitle.xPath")); //$NON-NLS-1$
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<ConceptTarget, String>() {

            public String get(ConceptTarget bean) {
                return bean.getRelativeLoopExpression();
            }

            public void set(ConceptTarget bean, String value) {
                bean.setRelativeLoopExpression(value);
            }
        });
        xPathCellEditor = new TextCellEditorWithProposal(tableViewerCreator.getTable(), SWT.NONE, column);
        column.setCellEditor(xPathCellEditor);
        xPathCellEditor.addListener(new DialogErrorForCellEditorListener(xPathCellEditor, column) {

            @Override
            public void newValidValueTyped(int itemIndex, Object previousValue, Object newValue, CELL_EDITOR_STATE state) {
                if (state == CELL_EDITOR_STATE.EDITING) {
                    linker.onXPathValueChanged(table, newValue.toString(), itemIndex);
                }

            }

            @Override
            public String validateValue(String newValue, int beanPosition) {
                String currentLoopXPath = linker.getCurrentLoopXPath();
                String value = null;
                if (newValue.trim().length() == 0) {
                    return null;
                } else if (newValue.trim().startsWith("/")) { //$NON-NLS-1$
                    value = newValue;
                } else {
                    value = currentLoopXPath + "/" + newValue; //$NON-NLS-1$
                }
                return linker.validateXPathExpression(value);
            }

        });
        column.setModifiable(true);
        column.setWeight(30);
        column.setMinimumWidth(50);
        column.setDefaultInternalValue(""); //$NON-NLS-1$
        // //////////////////////////////////////////////////////////////////////////////////////

        // //////////////////////////////////////////////////////////////////////////////////////
        // Tag Name
        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(Messages.getString("ExtractionFieldsWithXPathEditorView.columnTitle.columnName")); //$NON-NLS-1$
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<ConceptTarget, String>() {

            public String get(ConceptTarget bean) {
                return bean.getTargetName();
            }

            public void set(ConceptTarget bean, String value) {
                bean.setTargetName(value);
            }

        });
        column.setModifiable(true);
        column.setWeight(10);
        column.setMinimumWidth(50);
        // column.setCellEditor(new TextCellEditor(table));
        column.setDefaultInternalValue(""); //$NON-NLS-1$

        final TextCellEditorWithProposal tagNameCellEditor = createTagNameEditor(tableViewerCreator, column);
        column.setCellEditor(tagNameCellEditor);
    }

    /**
     * DOC chuang Comment method "createTagName".
     * 
     * @param tableViewerCreator
     * @param column
     * @return
     */
    private TextCellEditorWithProposal createTagNameEditor(TableViewerCreator<ConceptTarget> tableViewerCreator,
            TableViewerCreatorColumn column) {
        final TextCellEditorWithProposal tagNameCellEditor = new TextCellEditorWithProposal(tableViewerCreator.getTable(),
                SWT.NONE, column);
        // (bug 6038) zywang disabled method "newValidValueTyped" to removed duplicate dialog.

        // tagNameCellEditor.addListener(new DialogErrorForCellEditorListener(tagNameCellEditor, column) {
        //
        // @Override
        // public void newValidValueTyped(int itemIndex, Object previousValue, Object newValue, CELL_EDITOR_STATE state)
        // {
        // }
        //
        // @Override
        // public String validateValue(String newValue, int beanPosition) {
        // List<SchemaTarget> list = getModel().getBeansList();
        // String errorMessage = null;
        // int lstSize = list.size();
        // for (int i = 0; i < lstSize; i++) {
        // if (newValue.equals(list.get(i).getTagName()) && i != beanPosition) {
        //                        errorMessage = "The column name '" + newValue + "' already exists."; //$NON-NLS-1$
        // break;
        // }
        //
        // }
        // return errorMessage;
        // }
        //
        // });
        return tagNameCellEditor;
    }

    /**
     * DOC chuang Comment method "checkColumnNames".
     */
    public String checkColumnNames() {
        List<ConceptTarget> list = getModel().getBeansList();
        Set<String> conflictNames = new HashSet<String>();
        Set<String> names = new HashSet<String>();

        int lstSize = list.size();
        for (int i = 0; i < lstSize; i++) {
            String name = list.get(i).getTargetName();

            if (name == null || name.trim().equals("")) {
                return "Column name can't be null";
            }

            if (names.contains(name)) {
                conflictNames.add(name);
            } else {
                names.add(name);
            }
        }
        setRowBackground(list, conflictNames);

        if (conflictNames.isEmpty()) {
            return null;
        } else {

            // create error message
            StringBuffer buf = new StringBuffer();
            buf.append(Messages.getString("ExtractionFieldsWithXPathEditorView.columnName")); //$NON-NLS-1$
            for (String name : conflictNames) {
                buf.append(name);
                buf.append(","); //$NON-NLS-1$
            }
            buf.deleteCharAt(buf.length() - 1);
            buf.append(Messages.getString("ExtractionFieldsWithXPathEditorView.exist")); //$NON-NLS-1$
            return buf.toString();
        }
    }

    /**
     * DOC hcw Comment method "setRowBackground".
     * 
     * @param list
     * @param conflictNames
     */
    private void setRowBackground(List<ConceptTarget> list, Set<String> conflictNames) {
        // set background
        getTable().setRedraw(false);
        for (int i = 0; i < list.size(); i++) {
            String name = list.get(i).getTargetName();
            if (conflictNames.contains(name)) {
                getTable().getItem(i).setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
            } else {
                getTable().getItem(i).setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
            }
        }

        getTable().setRedraw(true);
    }

    public MDMXSDExtractorFieldModel getModel() {
        return (MDMXSDExtractorFieldModel) getExtendedTableModel();
    }

    /**
     * Getter for xPathColumn.
     * 
     * @return the xPathColumn
     */
    public TableViewerCreatorColumn getXPathColumn() {
        return this.xPathColumn;
    }

    /**
     * DOC amaumont Comment method "setLinker".
     * 
     * @param linker
     */
    public void setLinker(MDMLinker linker) {
        this.linker = linker;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView#initToolBar()
     */
    @Override
    protected ExtendedToolbarView initToolBar() {
        return new ExtendedToolbarView(getMainComposite(), SWT.NONE, getExtendedTableViewer()) {

            @Override
            protected AddPushButtonForExtendedTable createAddPushButton() {
                return new AddPushButtonForExtendedTable(this.toolbar, getExtendedTableViewer()) {

                    @Override
                    protected Object getObjectToAdd() {
                        UniqueStringGenerator<ConceptTarget> generator = new UniqueStringGenerator<ConceptTarget>("column",
                                getModel().getBeansList()) {

                            @Override
                            protected String getBeanString(ConceptTarget bean) {
                                // TODO Auto-generated method stub
                                return bean.getTargetName();
                            }

                        };
                        ConceptTarget tarhe = getModel().createNewSchemaTarget();
                        tarhe.setRelativeLoopExpression(generator.getUniqueString());
                        tarhe.setTargetName(generator.getUniqueString());
                        return tarhe;
                    }

                };
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.talend.core.ui.extended.ExtendedToolbarView#createPastePushButton()
             */
            @Override
            protected PastePushButton createPastePushButton() {
                return new PastePushButtonForExtendedTable(toolbar, extendedTableViewer) {

                    @Override
                    protected Command getCommandToExecute(ExtendedTableModel extendedTableModel, Integer indexWhereInsert) {
                        return new ExtendedTablePasteCommand(extendedTableModel, indexWhereInsert) {

                            @Override
                            public List createPastableBeansList(ExtendedTableModel extendedTableModel, List copiedObjectsList) {
                                ArrayList list = new ArrayList();
                                MDMXSDExtractorFieldModel fieldsModel = (MDMXSDExtractorFieldModel) extendedTableModel;
                                for (Object current : copiedObjectsList) {
                                    if (current instanceof ConceptTarget) {
                                        ConceptTarget original = (ConceptTarget) current;
                                        ConceptTarget copy = fieldsModel.createNewSchemaTarget();
                                        copy.setRelativeLoopExpression(original.getRelativeLoopExpression());
                                        copy.setTargetName(original.getTargetName());
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

}
