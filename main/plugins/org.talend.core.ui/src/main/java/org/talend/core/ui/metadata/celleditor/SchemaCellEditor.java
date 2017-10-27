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
package org.talend.core.ui.metadata.celleditor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.PluginChecker;
import org.talend.core.model.metadata.IEbcdicConstant;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.ISAPConstant;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.metadata.MetadataToolHelper;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.SAPFunctionUnit;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.EbcdicConnectionItem;
import org.talend.core.model.properties.SAPConnectionItem;
import org.talend.core.service.IEBCDICProviderService;
import org.talend.core.service.IHL7ProviderService;
import org.talend.core.service.ISAPProviderService;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.core.ui.i18n.Messages;
import org.talend.core.ui.metadata.celleditor.SchemaOperationChoiceDialog.EProcessType;
import org.talend.core.ui.metadata.celleditor.SchemaOperationChoiceDialog.ESelectionCategory;
import org.talend.core.ui.metadata.command.ChangeRuleMetadataCommand;
import org.talend.core.ui.metadata.command.RepositoryChangeMetadataForEBCDICCommand;
import org.talend.core.ui.metadata.command.RepositoryChangeMetadataForSAPBapi;
import org.talend.core.ui.metadata.command.RepositoryChangeMetadataForSAPCommand;
import org.talend.core.ui.metadata.dialog.MetadataDialog;
import org.talend.core.ui.process.IGEFProcess;
import org.talend.core.ui.process.IGraphicalNode;
import org.talend.core.ui.services.IDesignerCoreUIService;
import org.talend.core.ui.services.IRulesProviderService;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public class SchemaCellEditor extends DialogCellEditor {

    private INode node;

    private AbstractDataTableEditorView tableEditorView;

    private final String SINGLE = "SINGLE";

    private final String STRUCTURE = "STRUCTURE";

    private final String TABLE = "TABLE";

    private final String PARENT_ROW = "PARENT_ROW";

    private final String ISINPUT = "isinput";

    private final String TRUE = "true";

    public SchemaCellEditor(Composite parent, INode node) {
        super(parent, SWT.NONE);
        this.node = node;
    }

    public AbstractDataTableEditorView getTableEditorView() {
        return this.tableEditorView;
    }

    private TableViewer getTableViewer() {
        if (this.tableEditorView != null) {
            AbstractExtendedTableViewer extendedTableViewer = this.tableEditorView.getExtendedTableViewer();
            if (extendedTableViewer != null) {
                TableViewerCreator tableViewerCreator = extendedTableViewer.getTableViewerCreator();
                if (tableViewerCreator != null) {
                    return tableViewerCreator.getTableViewer();
                }
            }
        }
        return null;
    }

    public void setTableEditorView(AbstractDataTableEditorView tableEditorView) {
        this.tableEditorView = tableEditorView;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(org.eclipse.swt.widgets.Control)
     */
    @Override
    protected Object openDialogBox(Control cellEditorWindow) {

        String schemaToEdit = (String) this.getValue();
        String oldValue = schemaToEdit;

        boolean metaReadonly = false;

        final List<IMetadataTable> metadatas = node.getMetadataList();
        IMetadataTable tableToEdit = null;
        if (schemaToEdit == null || "".equals(schemaToEdit)) { //$NON-NLS-1$
            // for SAP
            SAPConnectionItem sapItem = getSAPRepositoryItem();
            if (sapItem != null) {
                boolean readonly = node.getProcess().isReadOnly();
                if (node.getJobletNode() != null) {
                    readonly = node.isReadOnly();
                }
                SchemaOperationChoiceDialog choiceDialog = new SchemaOperationChoiceDialog(cellEditorWindow.getShell(), node,
                        sapItem, EProcessType.CREATE, schemaToEdit, readonly);
                if (choiceDialog.open() == Window.OK) {
                    if (choiceDialog.getSelctionType() == ESelectionCategory.REPOSITORY) {
                        org.talend.core.model.metadata.builder.connection.MetadataTable selectedMetadataTable = choiceDialog
                                .getSelectedMetadataTable();
                        if (selectedMetadataTable != null) {
                            final IMetadataTable metaTable = ConvertionHelper.convert(selectedMetadataTable);
                            int index = 0;
                            if (getTableViewer() != null) {
                                index = getTableViewer().getTable().getSelectionIndex();
                            }
                            SAPFunctionUnit functionUnit = null;
                            if (selectedMetadataTable.eContainer() instanceof SAPFunctionUnit) {
                                functionUnit = (SAPFunctionUnit) selectedMetadataTable.eContainer();
                            }
                            if ("tSAPBapi".equals(node.getComponent().getName())) {
                                node.getMetadataFromConnector(schemaToEdit);
                                executeCommand(new RepositoryChangeMetadataForSAPBapi(node, functionUnit, metaTable, null, index));
                            } else {
                                executeCommand(new RepositoryChangeMetadataForSAPCommand(node, ISAPConstant.TABLE_SCHEMAS,
                                        metaTable.getLabel(), metaTable, index));
                            }
                            //
                            if (getTableViewer() != null) {
                                getTableViewer().refresh(true);
                            }
                            return selectedMetadataTable.getLabel();
                        }
                        return null;
                    } else {
                        // built-in
                    }
                } else {
                    this.setValue(oldValue);
                    return oldValue;
                }
            }

            // for EBCDIC (bug 5860)
            EbcdicConnectionItem repositoryItem = getRepositoryItem();
            if (repositoryItem != null) {
                SchemaOperationChoiceDialog choiceDialog = new SchemaOperationChoiceDialog(cellEditorWindow.getShell(), node,
                        repositoryItem, EProcessType.CREATE, schemaToEdit, node.getProcess().isReadOnly());
                if (choiceDialog.open() == Window.OK) {
                    if (choiceDialog.getSelctionType() == ESelectionCategory.REPOSITORY) {
                        org.talend.core.model.metadata.builder.connection.MetadataTable selectedMetadataTable = choiceDialog
                                .getSelectedMetadataTable();
                        if (selectedMetadataTable != null) {
                            final IMetadataTable metaTable = ConvertionHelper.convert(selectedMetadataTable);
                            int index = 0;
                            if (getTableViewer() != null) {
                                index = getTableViewer().getTable().getSelectionIndex();
                            }
                            SAPFunctionUnit functionUnit = null;
                            if (selectedMetadataTable.eContainer() instanceof SAPFunctionUnit) {
                                functionUnit = (SAPFunctionUnit) selectedMetadataTable.eContainer();
                            }
                            if ("tSAPBapi".equals(node.getComponent().getName())) {
                                executeCommand(new RepositoryChangeMetadataForSAPBapi(node, functionUnit, tableToEdit, metaTable));
                            } else {
                                executeCommand(new RepositoryChangeMetadataForEBCDICCommand(node, IEbcdicConstant.TABLE_SCHEMAS,
                                        metaTable.getLabel(), metaTable, index));
                            }
                            //
                            if (getTableViewer() != null) {
                                getTableViewer().refresh(true);
                            }
                            return selectedMetadataTable.getLabel();
                        }
                        return null;
                    } else {
                        // built-in
                    }
                } else {
                    return null;
                }
            }

            // built-in
            InputDialog dialogInput = new InputDialog(cellEditorWindow.getShell(),
                    Messages.getString("SchemaCellEditor.giveSchemaName"), Messages.getString("SchemaCellEditor.schemaName"), //$NON-NLS-1$ //$NON-NLS-2$
                    "", new IInputValidator() { //$NON-NLS-1$

                        @Override
                        public String isValid(String newText) {
                            if ("".equals(newText.trim())) { //$NON-NLS-1$
                                return Messages.getString("SchemaCellEditor.inputName"); //$NON-NLS-1$
                            }
                            if (MetadataToolHelper.getMetadataTableFromNodeTableName(node, newText) != null
                                    || !node.getProcess().checkValidConnectionName(newText)) {
                                return Messages.getString("SchemaCellEditor.nameExistOrInvalid"); //$NON-NLS-1$
                            }
                            return null;
                        }

                    });

            if (dialogInput.open() == InputDialog.OK) {
                Object newPropValue = dialogInput.getValue();
                if (!isRulesComponent(node)) {
                    node.getProcess().addUniqueConnectionName(dialogInput.getValue());
                    final MetadataTable table = new MetadataTable();
                    table.setLabel(dialogInput.getValue());
                    table.setTableName(dialogInput.getValue());
                    tableToEdit = table;
                    metadatas.add(table);
                    executeCommand(new Command() {

                        @Override
                        public void execute() {
                            if (getTableViewer() != null) {
                                Table tTable = getTableViewer().getTable();
                                Object data = tTable.getItem(tTable.getSelectionIndex()).getData();
                                if (data instanceof Map) {
                                    final Map<String, Object> valueMap = (Map<String, Object>) data;
                                    Object code = valueMap.get(IEbcdicConstant.FIELD_CODE);
                                    if (code == null || "".equals(code)) { //$NON-NLS-1$
                                        valueMap.put(IEbcdicConstant.FIELD_CODE, table.getTableName());
                                        valueMap.put(IEbcdicConstant.FIELD_SCHEMA, table.getTableName());
                                    }
                                }
                                getTableViewer().refresh();
                            }
                        }
                    });
                } else { // when create a new schema for rule ,open metadataDialog,for rules
                    // 1.getinput connection and current output connection
                    INode inputNode = null, outputNode = null;
                    IMetadataTable originalInputTable = null, originalCurrentOutTable = null, finalOutTable = null, finalInputTable = null;
                    MetadataDialog metaDialog = null;
                    int index = getTableViewer().getTable().getSelectionIndex();
                    // IElementParameter param = node.getElementParameter("SCHEMAS");

                    if (node.getIncomingConnections().size() > 0) {
                        // for rules there is only one input,so just get the first element
                        if (node.getOutgoingConnections().size() > 0) {
                            outputNode = node.getOutgoingConnections().get(0).getTarget();
                        }
                        originalInputTable = node.getIncomingConnections().get(0).getMetadataTable();
                        inputNode = node.getIncomingConnections().get(0).getSource();
                        if (outputNode == null) {
                            outputNode = node;
                        }
                        List<IMetadataColumn> listColumns = new ArrayList<IMetadataColumn>();
                        originalCurrentOutTable = new MetadataTable();
                        originalCurrentOutTable.setListColumns(listColumns);
                        // 2.open metadataDialog,set finalOutTable
                        metaDialog = new MetadataDialog(new Shell(), originalInputTable, inputNode, originalCurrentOutTable,
                                outputNode, tableEditorView.getTableViewerCreator().getCommandStack());
                        if (metaDialog.open() == Window.OK) {
                            finalInputTable = metaDialog.getInputMetaData().clone();
                            finalOutTable = metaDialog.getOutputMetaData().clone();
                            // 3.ChangeRuleMetadataCommand
                            ChangeRuleMetadataCommand changeRuleMetadataCommand = new ChangeRuleMetadataCommand(node, "SCHEMAS", //$NON-NLS-1$
                                    newPropValue, finalOutTable, index);
                            executeCommand(changeRuleMetadataCommand);
                            if (getTableViewer() != null) {
                                getTableViewer().refresh();
                            }
                        }
                    } else {
                        node.getProcess().addUniqueConnectionName(dialogInput.getValue());
                        final MetadataTable table = new MetadataTable();
                        table.setLabel(dialogInput.getValue());
                        table.setTableName(dialogInput.getValue());
                        tableToEdit = table;
                        metadatas.add(table);
                        executeCommand(new Command() {

                            @Override
                            public void execute() {
                                if (getTableViewer() != null) {
                                    Table tTable = getTableViewer().getTable();
                                    Object data = tTable.getItem(tTable.getSelectionIndex()).getData();
                                    if (data instanceof Map) {
                                        final Map<String, Object> valueMap = (Map<String, Object>) data;
                                        Object code = valueMap.get(IEbcdicConstant.FIELD_CODE);
                                        if (code == null || "".equals(code)) { //$NON-NLS-1$
                                            valueMap.put(IEbcdicConstant.FIELD_CODE, table.getTableName());
                                            valueMap.put(IEbcdicConstant.FIELD_SCHEMA, table.getTableName());
                                        }
                                    }
                                    getTableViewer().refresh();
                                }
                            }
                        });
                    }

                }
            }

        } else {
            if (isSAPNode(node) && getTableViewer() != null) {
                Table tTable = getTableViewer().getTable();
                Object data = tTable.getItem(tTable.getSelectionIndex()).getData();
                if (data instanceof Map) {
                    final Map<String, Object> valueMap = (Map<String, Object>) data;
                    Object code = valueMap.get(IEbcdicConstant.FIELD_SCHEMA);
                    tableToEdit = MetadataToolHelper.getMetadataTableFromNodeTableName(node, code.toString());
                }
            } else {
                tableToEdit = MetadataToolHelper.getMetadataTableFromNodeTableName(node, schemaToEdit);
            }
            if (getTableViewer() != null && tableToEdit != null && isEBCDICNode(node)) {
                Table tTable = getTableViewer().getTable();
                Object data = tTable.getItem(tTable.getSelectionIndex()).getData();
                if (data instanceof Map) {
                    final Map<String, Object> valueMap = (Map<String, Object>) data;
                    final String key = IEbcdicConstant.FIELD_SCHEMA + IEbcdicConstant.REF_TYPE;
                    Object repositoyType = valueMap.get(key);
                    EbcdicConnectionItem repositoryItem = getRepositoryItem();

                    EProcessType processType = EProcessType.BUILTIN;
                    if (repositoryItem != null) {
                        if (repositoyType != null && IEbcdicConstant.REF_ATTR_REPOSITORY.equals(repositoyType)) {
                            processType = EProcessType.REPOSITORY;
                        }
                    }
                    SchemaOperationChoiceDialog choiceDialog = new SchemaOperationChoiceDialog(cellEditorWindow.getShell(), node,
                            repositoryItem, processType, schemaToEdit, node.getProcess().isReadOnly());
                    if (choiceDialog.open() == Window.OK) {
                        ESelectionCategory selctionType = choiceDialog.getSelctionType();
                        switch (selctionType) {
                        case SHOW_SCHEMA:
                            if (processType == EProcessType.REPOSITORY) {
                                metaReadonly = true; // readonly
                            }
                            break;
                        case REPOSITORY:
                            final org.talend.core.model.metadata.builder.connection.MetadataTable selectedMetadataTable = choiceDialog
                                    .getSelectedMetadataTable();
                            if (selectedMetadataTable != null) {
                                String newSchema = selectedMetadataTable.getLabel();
                                if (!schemaToEdit.equals(newSchema) || processType == EProcessType.BUILTIN) {
                                    // changed
                                    RepositoryChangeMetadataForEBCDICCommand cmd = new RepositoryChangeMetadataForEBCDICCommand(
                                            node, IEbcdicConstant.TABLE_SCHEMAS, newSchema, schemaToEdit,
                                            ConvertionHelper.convert(selectedMetadataTable), tableToEdit,
                                            tTable.getSelectionIndex());
                                    executeCommand(cmd);
                                    if (getTableViewer() != null) {
                                        getTableViewer().refresh();
                                    }
                                }
                                return newSchema;
                            } else {
                                // built-in
                            }
                        case BUILDIN:
                            executeCommand(new Command() {

                                @Override
                                public void execute() {
                                    valueMap.remove(key);
                                    if (getTableViewer() != null) {
                                        getTableViewer().refresh();
                                    }
                                }

                            });
                            return schemaToEdit; // keep
                        default:

                        }
                    } else {
                        return null;
                    }
                }
            }

            if (getTableViewer() != null && tableToEdit != null && isSAPNode(node)) {
                Table tTable = getTableViewer().getTable();
                Object data = tTable.getItem(tTable.getSelectionIndex()).getData();
                if (data instanceof Map) {
                    final Map<String, Object> valueMap = (Map<String, Object>) data;
                    final String key = ISAPConstant.FIELD_SCHEMA + ISAPConstant.REF_TYPE;
                    Object repositoyType = valueMap.get(key);
                    SAPConnectionItem repositoryItem = getSAPRepositoryItem();

                    EProcessType processType = EProcessType.BUILTIN;
                    if (repositoryItem != null) {
                        if (repositoyType != null && ISAPConstant.REF_ATTR_REPOSITORY.equals(repositoyType)) {
                            processType = EProcessType.REPOSITORY;
                        }
                    }
                    Object type = valueMap.get(ISAPConstant.TYPE);
                    if (type != null && type.toString().equals(TABLE)) {
                        SchemaOperationChoiceDialog choiceDialog = new SchemaOperationChoiceDialog(cellEditorWindow.getShell(),
                                node, repositoryItem, processType, schemaToEdit, node.getProcess().isReadOnly());
                        if (choiceDialog.open() == Window.OK) {
                            ESelectionCategory selctionType = choiceDialog.getSelctionType();
                            switch (selctionType) {
                            case SHOW_SCHEMA:
                                if (processType == EProcessType.REPOSITORY) {
                                    metaReadonly = true; // readonly
                                }
                                break;
                            case REPOSITORY:
                                final org.talend.core.model.metadata.builder.connection.MetadataTable selectedMetadataTable = choiceDialog
                                        .getSelectedMetadataTable();
                                if (selectedMetadataTable != null) {
                                    String newSchema = selectedMetadataTable.getLabel();
                                    if (!schemaToEdit.equals(newSchema) || processType == EProcessType.BUILTIN) {
                                        // changed
                                        RepositoryChangeMetadataForSAPCommand cmd = new RepositoryChangeMetadataForSAPCommand(
                                                node, ISAPConstant.TABLE_SCHEMAS, newSchema, schemaToEdit,
                                                ConvertionHelper.convert(selectedMetadataTable), tableToEdit,
                                                tTable.getSelectionIndex());

                                        // (node, ISAPConstant.TABLE_SCHEMAS, metaTable.getLabel(), metaTable, index)
                                        executeCommand(cmd);
                                        if (getTableViewer() != null) {
                                            getTableViewer().refresh();
                                        }
                                    }
                                    return newSchema;
                                } else {
                                    // built-in
                                }
                            case BUILDIN:
                                executeCommand(new Command() {

                                    @Override
                                    public void execute() {
                                        valueMap.remove(key);
                                        if (getTableViewer() != null) {
                                            getTableViewer().refresh();
                                        }
                                    }

                                });
                                return schemaToEdit; // keep
                            default:

                            }
                        } else {
                            return null;
                        }
                    } else {
                        if (processType == EProcessType.REPOSITORY) {
                            metaReadonly = true; // readonly
                        }
                    }
                }
            }

        }
        Table tTable = getTableViewer().getTable();
        boolean hasParentRow = false;
        Object data = tTable.getItem(tTable.getSelectionIndex()).getData();
        Object type = null;
        Object parent_row = null;
        if (data instanceof Map) {
            final Map<String, Object> valueMap = (Map<String, Object>) data;
            if (valueMap.containsKey(PARENT_ROW)) {
                hasParentRow = true;
            }
            final String key = ISAPConstant.TYPE;
            type = valueMap.get(key);
            parent_row = valueMap.get(PARENT_ROW);
            if (isSAPNode(node) && type instanceof Integer) {
                type = SINGLE;
                valueMap.remove(key);
                valueMap.put(key, SINGLE);
            }
        }
        final boolean hasParentRowForExe = hasParentRow;
        if (tableToEdit != null) {
            if (isHL7OutputNode(node) && node.getIncomingConnections().size() > 0) {
                copyHL7OutputMetadata(node, tableToEdit);
            } else if (isSAPNode(node) && type != null && type.toString().equals(TABLE)
                    && node.getIncomingConnections().size() > 0 && hasParentRow
                    && (parent_row != null && !parent_row.equals("") && !(parent_row instanceof Integer))) {
                copySAPOutputMetadata(node, tableToEdit);
            } else {
                MetadataDialog dialog = new MetadataDialog(cellEditorWindow.getShell(), tableToEdit.clone(), node, null);
                dialog.setInputReadOnly(metaReadonly);
                dialog.setOutputReadOnly(metaReadonly);
                if (isSAPNode(node) && type != null && (type.equals(SINGLE) || type.equals(STRUCTURE)) && hasParentRow) {
                    MetadataDialog.setSingleAndStruct(true);
                }
                final IMetadataTable oldTable = tableToEdit;
                if (dialog.open() == MetadataDialog.OK) {
                    final IMetadataTable newTable = dialog.getOutputMetaData();
                    if (!oldTable.sameMetadataAs(newTable, IMetadataColumn.OPTIONS_NONE)) {
                        executeCommand(new Command() {

                            @Override
                            public void execute() {
                                oldTable.getListUsedColumns().clear();
                                oldTable.getListUnusedColumns().clear();
                                oldTable.setListColumns(newTable.getListColumns(true));
                                List<IMetadataColumn> columns = newTable.getListColumns(true);
                                if (node instanceof IGraphicalNode) {
                                    IGraphicalNode gNode = (IGraphicalNode) node;
                                    gNode.checkAndRefreshNode();
                                }
                                if (isSAPNode(node) && hasParentRowForExe) {
                                    oldTable.getAdditionalProperties().put(ISINPUT, TRUE);
                                }
                                if (getTableViewer() != null) {
                                    getTableViewer().refresh();
                                }
                            }

                        });
                    } else {
                        if (isSAPNode(node) && hasParentRowForExe) {
                            oldTable.getAdditionalProperties().put(ISINPUT, TRUE);
                        }
                    }
                } else {
                    if (isSAPNode(node) && hasParentRowForExe) {
                        oldTable.getAdditionalProperties().put(ISINPUT, TRUE);
                    }
                }
            }
            return schemaToEdit;
        } else {
            return ""; //$NON-NLS-1$
        }
    }

    private boolean isEBCDICNode(INode node) {
        if (PluginChecker.isEBCDICPluginLoaded()) {
            IEBCDICProviderService service = (IEBCDICProviderService) GlobalServiceRegister.getDefault().getService(
                    IEBCDICProviderService.class);
            if (service != null) {
                return service.isEbcdicNode(node);
            }
        }
        return false;
    }

    private boolean isHL7OutputNode(INode node) {
        if (PluginChecker.isHL7PluginLoaded()) {
            IHL7ProviderService service = (IHL7ProviderService) GlobalServiceRegister.getDefault().getService(
                    IHL7ProviderService.class);
            if (service != null) {
                return service.isHL7OutputNode(node);
            }
        }
        return false;
    }

    private boolean isSAPNode(INode node) {
        if (PluginChecker.isSAPWizardPluginLoaded()) {
            ISAPProviderService service = (ISAPProviderService) GlobalServiceRegister.getDefault().getService(
                    ISAPProviderService.class);
            if (service != null) {
                return service.isSAPNode(node);
            }
        }
        return false;
    }

    private boolean isRulesComponent(INode node) {
        if (PluginChecker.isRulesPluginLoaded()) {
            IRulesProviderService service = (IRulesProviderService) GlobalServiceRegister.getDefault().getService(
                    IRulesProviderService.class);
            if (service != null) {
                return service.isRuleComponent(node);
            }
        }
        return false;
    }

    private EbcdicConnectionItem getRepositoryItem() {
        if (PluginChecker.isEBCDICPluginLoaded()) {
            IEBCDICProviderService service = (IEBCDICProviderService) GlobalServiceRegister.getDefault().getService(
                    IEBCDICProviderService.class);
            if (service != null) {
                return service.getRepositoryItem(node);
            }

        }
        return null;
    }

    private SAPConnectionItem getSAPRepositoryItem() {
        if (PluginChecker.isSAPWizardPluginLoaded()) {
            ISAPProviderService service = (ISAPProviderService) GlobalServiceRegister.getDefault().getService(
                    ISAPProviderService.class);
            if (service != null) {
                return service.getRepositoryItem(node);
            }
        }
        return null;
    }

    private void executeCommand(Command cmd) {
        final IProcess process = node.getProcess();

        boolean executed = false;
        if (process != null && process instanceof IGEFProcess) {
            IDesignerCoreUIService designerCoreUIService = CoreUIPlugin.getDefault().getDesignerCoreUIService();
            if (designerCoreUIService != null) {
                executed = designerCoreUIService.executeCommand((IGEFProcess) process, cmd);
            }
        }
        if (!executed) {
            cmd.execute();
        }
    }

    private void copySAPOutputMetadata(INode node, IMetadataTable tableToEdit) {
        List<Map<String, String>> maps = (List<Map<String, String>>) ElementParameterParser.getObjectValue(node,
                "__MAPPING_INPUT__"); //$NON-NLS-1$
        String tableName = tableToEdit.getTableName();
        List<IConnection> connList = (List<IConnection>) node.getIncomingConnections(EConnectionType.FLOW_MERGE);
        for (Map<String, String> map : maps) {
            if (map.containsValue(tableName)) {
                if (map.get("SCHEMA") != null && map.get("SCHEMA").equals(tableName)) {
                    String rowName = map.get("PARENT_ROW");
                    for (IConnection conn : connList) {
                        if (conn.getName().equals(rowName)) {
                            IMetadataTable originalInputTable = null, originalCurrentOutTable = null, finalOutTable = null, finalInputTable = null;
                            int index = getTableViewer().getTable().getSelectionIndex();
                            originalInputTable = conn.getMetadataTable();
                            INode inputNode = conn.getSource();
                            List<IMetadataColumn> listColumns = new ArrayList<IMetadataColumn>();
                            // originalCurrentOutTable = new MetadataTable();
                            // originalCurrentOutTable.setListColumns(listColumns);
                            // 2.open metadataDialog,set finalOutTable
                            MetadataDialog metaDialog = new MetadataDialog(new Shell(), originalInputTable, inputNode,
                                    tableToEdit.clone(), node, tableEditorView.getTableViewerCreator().getCommandStack());
                            if (metaDialog.open() == Window.OK) {
                                finalInputTable = metaDialog.getInputMetaData().clone();
                                finalOutTable = metaDialog.getOutputMetaData().clone();
                                tableToEdit.setListColumns(finalOutTable.getListColumns());
                                // 3.ChangeRuleMetadataCommand
                                // ChangeRuleMetadataCommand changeRuleMetadataCommand = new
                                // ChangeRuleMetadataCommand(node,
                                //                                        "SCHEMAS", //$NON-NLS-1$
                                // tableToEdit.getLabel(), finalOutTable, index);
                                // changeMetadataCommand changeMetadataCommand = new ChangeMetadataCommand(node, param,
                                // inputNode,
                                // inputMetadata, finalInputTable, originaleOutputTable, finalOutTable);
                                // executeCommand(changeRuleMetadataCommand);
                                IProcess process = node.getProcess();
                                if (process instanceof IProcess2) {
                                    ((IProcess2) process).checkTableParameters();
                                    ((IProcess2) process).checkProcess();
                                }
                                if (getTableViewer() != null) {
                                    getTableViewer().refresh();
                                }
                            }
                            return;
                        }
                    }

                }
            }
        }

    }

    private void copyHL7OutputMetadata(INode node, IMetadataTable tableToEdit) {
        List<Map<String, String>> maps = (List<Map<String, String>>) ElementParameterParser.getObjectValue(node, "__SCHEMAS__"); //$NON-NLS-1$
        String tableName = tableToEdit.getTableName();
        List<IConnection> connList = (List<IConnection>) node.getIncomingConnections(EConnectionType.FLOW_MERGE);
        for (Map<String, String> map : maps) {
            if (map.containsValue(tableName)) {
                if (map.get("SCHEMA") != null && map.get("SCHEMA").equals(tableName)) {
                    String rowName = map.get("PARENT_ROW");
                    for (IConnection conn : connList) {
                        if (conn.getName().equals(rowName)) {
                            IMetadataTable originalInputTable = null, originalCurrentOutTable = null, finalOutTable = null, finalInputTable = null;
                            int index = getTableViewer().getTable().getSelectionIndex();
                            originalInputTable = conn.getMetadataTable();
                            INode inputNode = conn.getSource();
                            List<IMetadataColumn> listColumns = new ArrayList<IMetadataColumn>();
                            // originalCurrentOutTable = new MetadataTable();
                            // originalCurrentOutTable.setListColumns(listColumns);
                            // 2.open metadataDialog,set finalOutTable
                            MetadataDialog metaDialog = new MetadataDialog(new Shell(), originalInputTable, inputNode,
                                    tableToEdit.clone(), node, tableEditorView.getTableViewerCreator().getCommandStack());
                            if (metaDialog.open() == Window.OK) {
                                finalInputTable = metaDialog.getInputMetaData().clone();
                                finalOutTable = metaDialog.getOutputMetaData().clone();
                                tableToEdit.setListColumns(finalOutTable.getListColumns());
                                // 3.ChangeRuleMetadataCommand
                                // ChangeRuleMetadataCommand changeRuleMetadataCommand = new
                                // ChangeRuleMetadataCommand(node,
                                //                                        "SCHEMAS", //$NON-NLS-1$
                                // tableToEdit.getLabel(), finalOutTable, index);
                                // changeMetadataCommand changeMetadataCommand = new ChangeMetadataCommand(node, param,
                                // inputNode,
                                // inputMetadata, finalInputTable, originaleOutputTable, finalOutTable);
                                // executeCommand(changeRuleMetadataCommand);
                                IProcess process = node.getProcess();
                                if (process instanceof IProcess2) {
                                    ((IProcess2) process).checkTableParameters();
                                    ((IProcess2) process).checkProcess();
                                }
                                if (getTableViewer() != null) {
                                    getTableViewer().refresh();
                                }
                            }
                            return;
                        }
                    }

                }
            }
        }

    }

    // private void updateColumnsOnElement(IElement element, IMetadataTable metadataTable) {
    // List<Map<String, Object>> paramValues = (List<Map<String, Object>>) param.getValue();
    // List<Map<String, Object>> newParamValues = new ArrayList<Map<String, Object>>();
    // for (int j = 0; j < columnNameList.length; j++) {
    // String columnName = columnNameList[j];
    // String[] codes = param.getListItemsDisplayCodeName();
    // Map<String, Object> newLine = null;
    // boolean found = false;
    // ColumnNameChanged colChanged = null;
    // if (columnsChanged != null) {
    // for (int k = 0; k < columnsChanged.size() && !found; k++) {
    // colChanged = columnsChanged.get(k);
    // if (colChanged.getNewName().equals(columnName)) {
    // found = true;
    // }
    // }
    // }
    // if (found) {
    // found = false;
    // for (int k = 0; k < paramValues.size() && !found; k++) {
    // Map<String, Object> currentLine = paramValues.get(k);
    // if (currentLine.get(codes[0]).equals(colChanged.getOldName())) {
    // currentLine.put(codes[0], colChanged.getNewName());
    // found = true;
    // }
    // }
    // }
    // found = false;
    // for (int k = 0; k < paramValues.size() && !found; k++) {
    // Map<String, Object> currentLine = paramValues.get(k);
    // if (currentLine.get(codes[0]) == null) {
    // currentLine.put(codes[0], columnName);
    // }
    // if (currentLine.get(codes[0]).equals(columnName)) {
    // found = true;
    // newLine = currentLine;
    // }
    // }
    // if (!found) {
    // newLine = TableController.createNewLine(param);
    // newLine.put(codes[0], columnName);
    // }
    // if (synWidthWithMetadataColumn) {
    // setColumnSize(newLine, element, codes, param);
    // }
    // newParamValues.add(j, newLine);
    // }
    // paramValues.clear();
    // paramValues.addAll(newParamValues);
    // }
}
