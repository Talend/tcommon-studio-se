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
package org.talend.core.ui.metadata.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.talend.commons.ui.command.CommandStackForComposite;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.swt.advanced.composite.ThreeCompositesSashForm;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.commons.ui.swt.tableviewer.IModifiedBeanListener;
import org.talend.commons.ui.swt.tableviewer.ModifiedBeanEvent;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.components.EComponentType;
import org.talend.core.model.metadata.Dbms;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.MetadataToolHelper;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.GenericSchemaConnection;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeConnector;
import org.talend.core.model.utils.NodeUtil;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.core.ui.i18n.Messages;
import org.talend.core.ui.metadata.editor.AbstractMetadataTableEditorView;
import org.talend.core.ui.metadata.editor.MetadataTableEditor;
import org.talend.core.ui.metadata.editor.MetadataTableEditorView;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.designer.core.IDesignerCoreService;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class MetadataDialog extends Dialog {

    private static final String DATABASE_LABEL = "Database"; //$NON-NLS-1$

    @Override
    protected void setShellStyle(int newShellStyle) {
        newShellStyle = newShellStyle | SWT.RESIZE;
        super.setShellStyle(newShellStyle);
    }

    private MetadataTableEditorView outputMetaView;

    private MetadataTableEditorView inputMetaView;

    private Point size;

    private IMetadataTable outputMetaTable;

    private IMetadataTable inputMetaTable;

    private String text = ""; //$NON-NLS-1$

    private String titleOutput = ""; //$NON-NLS-1$

    private String titleInput = ""; //$NON-NLS-1$

    private boolean inputReadOnly = false;

    private boolean outputReadOnly = false;

    private Map<IMetadataColumn, String> changedNameColumns = new HashMap<IMetadataColumn, String>();

    private Map<String, String> changeNameInColumns = new HashMap<String, String>();

    private Map<String, String> changeNameOutColumns = new HashMap<String, String>();

    private CommandStack commandStack;

    private INode inputNode;

    private INode outputNode;

    private ThreeCompositesSashForm compositesSachForm;

    private TableItem[] tableItem;

    private List<IMetadataColumn> list;

    private Set<String> preOutputColumnSet = new HashSet<String>();

    private Set<String> preInputColumnSet = new HashSet<String>();

    private IMetadataColumn column;

    private boolean isUsefulChange = false;

    private static boolean isSingleAndStruct = false;

    public MetadataDialog(Shell parent, IMetadataTable inputMetaTable, INode inputNode, IMetadataTable outputMetaTable,
            INode outputNode, CommandStack commandStack) {
        super(parent);
        this.inputMetaTable = inputMetaTable;
        this.inputNode = inputNode;
        isSingleAndStruct = false;
        if (inputNode != null) {
            this.titleInput = inputMetaTable.getTableName() + " (Input)"; //$NON-NLS-1$
            INodeConnector connector = inputNode.getConnectorFromName(inputMetaTable.getAttachedConnector());
            if (!connector.isMultiSchema()) {
                this.titleInput = inputNode.getLabel() + " (Input - " + connector.getLinkName() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        this.outputNode = outputNode;
        if (outputNode != null) {
            this.titleOutput = outputNode.getLabel();
        }
        this.outputMetaTable = outputMetaTable;
        this.commandStack = commandStack;
        list = outputMetaTable.getListColumns(true);
        for (IMetadataColumn preColumn : list) {
            preOutputColumnSet.add(preColumn.getLabel());
        }
        if (inputMetaTable == null) {
            size = new Point(550, 400);
        } else {
            for (IMetadataColumn preColumn : inputMetaTable.getListColumns(true)) {
                preInputColumnSet.add(preColumn.getLabel());
            }
            size = new Point(1000, 400);
        }
    }

    public MetadataDialog(Shell parent, IMetadataTable outputMetaTable, INode outputNode, CommandStack commandStack) {
        this(parent, null, null, outputMetaTable, outputNode, commandStack);
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    protected void configureShell(final Shell newShell) {
        super.configureShell(newShell);
        this.commandStack = new CommandStackForComposite(newShell);
        newShell.setSize(size);
        newShell.setText(text);
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, false);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    public static void initializeMetadataTableView(MetadataTableEditorView metaView, INode node, IMetadataTable metadataTable) {
        boolean dbComponent = false;
        boolean hasMappingType = false;
        boolean eltComponent = false;
        boolean isSAPELT = false;
        boolean hasRepositoryDbSchema = false;
        boolean isEBCDIC = false;
        boolean isNewFram = false;
        if (node != null && node.getComponent() != null) {
            isNewFram = node.getComponent().getComponentType()==EComponentType.GENERIC;
            eltComponent = node.isELTComponent();
            isEBCDIC = node.getComponent().getName().contains("EBCDIC");
            isSAPELT = node.getComponent().getName().startsWith("tELTSAP");
            if (node.getComponent().isSupportDbType() || node.getComponent().getOriginalFamilyName().startsWith(DATABASE_LABEL)
                    || eltComponent || isEBCDIC) {
                dbComponent = !isEBCDIC;
                for (IElementParameter currentParam : node.getElementParameters()) {
                    if (currentParam.getFieldType().equals(EParameterFieldType.MAPPING_TYPE)) {
                        metaView.setCurrentDbms((String) currentParam.getValue());
                        hasMappingType = true;
                    }
                }

                IElementParameter schemaParam = node.getElementParameter("SCHEMA_TYPE"); //$NON-NLS-1$
                if (!hasMappingType && schemaParam != null) { // if there is no
                    // mapping type,
                    // then check if a
                    // db
                    // repository schema is used
                    String schemaType = (String) schemaParam.getValue();
                    if (schemaType.equals("REPOSITORY")) { //$NON-NLS-1$
                        // repository mode
                        String metaRepositoryName = (String) node.getElementParameter("REPOSITORY_SCHEMA_TYPE").getValue(); //$NON-NLS-1$
                        Connection connection = MetadataToolHelper.getConnectionFromRepository(metaRepositoryName);

                        boolean isDatabaseConnection = connection instanceof DatabaseConnection;
                        boolean isGenericSchemaConnection = connection instanceof GenericSchemaConnection;
                        if (isDatabaseConnection || isGenericSchemaConnection) {
                            hasRepositoryDbSchema = true;

                            for (IMetadataColumn column : metadataTable.getListColumns()) {
                                if ((column.getType() == "") || (column.getType() == null)) { //$NON-NLS-1$
                                    hasRepositoryDbSchema = false;
                                }
                            }
                            String componentDbType = ""; //$NON-NLS-1$
                            for (IElementParameter param : (List<IElementParameter>) node.getElementParameters()) {
                                if (param.getRepositoryValue() != null) {
                                    if (param.getRepositoryValue().equals("TYPE")) { //$NON-NLS-1$
                                        componentDbType = (String) param.getValue();
                                    }
                                }
                            }

                            // if we don't support yet the db type for the mapping
                            // type, then don't display.
                            if (!EDatabaseTypeName.supportDbType(componentDbType)) {
                                hasRepositoryDbSchema = false;
                            }
                            String componentProduct = EDatabaseTypeName.getTypeFromDbType(componentDbType).getProduct();
                            String connectionProduct = null;
                            if (isDatabaseConnection) {
                                connectionProduct = ((DatabaseConnection) connection).getProductId();
                                if (!componentProduct.equals(connectionProduct)) {
                                    hasRepositoryDbSchema = false;
                                    // the component don't support this product so don't
                                    // display.
                                } else {
                                    metaView.setCurrentDbms(((DatabaseConnection) connection).getDbmsId());
                                }
                            }

                            else if (isGenericSchemaConnection) {
                                String mappingTypeId = ((GenericSchemaConnection) connection).getMappingTypeId();

                                if (mappingTypeId != null) {
                                    connectionProduct = mappingTypeId;
                                    metaView.setCurrentDbms(connectionProduct);
                                }
                            }
                        }
                    }
                }
            }
        } else {
            eltComponent = false;
        }
        if(isNewFram){
            dbComponent = true;
        }
        metaView.setShowDbTypeColumn(hasMappingType || eltComponent, false, hasMappingType
                || (dbComponent && !hasRepositoryDbSchema));
        metaView.setShowDbColumnName(dbComponent, hasMappingType || (dbComponent && !hasRepositoryDbSchema));

        // hide the talend type for ELT components
        metaView.setShowTalendTypeColumn(!eltComponent || isSAPELT);
        // hide the pattern for ELT components
        metaView.setShowPatternColumn(!eltComponent);

        metaView.setShowOriginalLength(isEBCDIC);

        if (isEBCDIC) {
            metaView.setShowOriginalLength(true);
            List<String> fieldList = new ArrayList<String>();
            fieldList.add("ImpliedDecimal"); //$NON-NLS-1$
            fieldList.add("Signed"); //$NON-NLS-1$
            metaView.setAdditionalFields(fieldList);
        }

        showColumnsOfCustomComponents(node, metaView);
    }

    private static void showColumnsOfCustomComponents(INode node, MetadataTableEditorView metaView) {
        if (isSingleAndStruct) {
            metaView.setShowDbColumnName(false, false);
            metaView.setShowKeyColumn(false);
            metaView.setShowNullableColumn(false);
            metaView.setShowLengthColumn(false);
            metaView.setShowAdditionalFieldColumn(false);
            metaView.setShowPrecisionColumn(false);
            metaView.setShowCommentColumn(false);
            metaView.setSapSpecialSchema(true);
        }
    }

    private boolean isRepository(INode node, IMetadataTable currentTable) {
        // TDI-29264:improve here for adapt two or more schemas with different mode,such as tHiveCreateTable etc
        boolean nodeModeFlag = false;
        for (IElementParameter param : node.getElementParameters()) {
            if ((param.getFieldType() == EParameterFieldType.SCHEMA_TYPE || param.getFieldType() == EParameterFieldType.SCHEMA_REFERENCE)
                    && (param.getContext() == null || param.getContext().equals(currentTable.getAttachedConnector()))) {
                IElementParameter schemaParam = param.getChildParameters().get("SCHEMA_TYPE");
                if (schemaParam.getValue() != null) {
                    if (schemaParam.getValue().equals("REPOSITORY")) {
                        nodeModeFlag = true;
                        break;
                    }
                }
            }
        }

        // for sap
        if (!nodeModeFlag) {
            IElementParameter schemaParam = node.getElementParameter("SCHEMAS");
            if (schemaParam != null) {
                List schemaType = (List) schemaParam.getValue();
                for (int i = 0; i < schemaType.size(); i++) {
                    HashMap map = (HashMap) schemaType.get(i);
                    Set set = map.keySet();
                    Iterator it = set.iterator();
                    while (it.hasNext()) {
                        String key = (String) it.next();
                        if (key.equals("SAP_TABLE_NAME")) {
                            String value = (String) map.get(key);
                            String tableLabel = this.outputMetaTable.getLabel();
                            if (tableLabel != null && tableLabel.equals(TalendQuoteUtils.removeQuotes(value))) {
                                if (map.containsKey("SCHEMA-TYPE") && map.containsValue("REPOSITORY")) {
                                    nodeModeFlag = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return nodeModeFlag;
    }

    @Override
    protected Control createDialogArea(final Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);

        MetadataTableEditor metadataTableEditor;
        if (inputMetaTable == null) {
            composite.setLayout(new FillLayout());
            if (isRepository(outputNode, outputMetaTable)) {
                metadataTableEditor = new MetadataTableEditor(outputMetaTable, titleOutput, true);
            } else {
                metadataTableEditor = new MetadataTableEditor(outputMetaTable, titleOutput);
            }
            outputMetaView = new DialogMetadataTableEditorView(composite, SWT.NONE, metadataTableEditor, outputReadOnly, true,
                    true, false);
            outputMetaView.setMetadataTalendTypeFilter(NodeUtil.createMetadataTalendTypeFilter(outputNode));
            outputMetaView.setIsRepository(isRepository(outputNode, outputMetaTable));
            initializeMetadataTableView(outputMetaView, outputNode, outputMetaTable);
            outputMetaView.initGraphicComponents();
            outputMetaView.getExtendedTableViewer().setCommandStack(commandStack);
        } else {
            compositesSachForm = new ThreeCompositesSashForm(composite, SWT.NONE);

            GridLayout gridLayout = new GridLayout(1, false);
            gridLayout.marginBottom = 0;
            gridLayout.marginHeight = 0;
            gridLayout.marginLeft = 0;
            gridLayout.marginRight = 0;
            gridLayout.marginTop = 0;
            gridLayout.marginWidth = 0;
            gridLayout.horizontalSpacing = 0;
            composite.setLayout(gridLayout);
            GridData gridData = new GridData(GridData.FILL_BOTH);
            composite.setLayoutData(gridData);

            metadataTableEditor = new MetadataTableEditor(inputMetaTable, titleInput);
            inputMetaView = new DialogMetadataTableEditorView(compositesSachForm.getLeftComposite(), SWT.NONE,
                    metadataTableEditor, inputReadOnly, true, true, false);
            inputMetaView.setMetadataTalendTypeFilter(NodeUtil.createMetadataTalendTypeFilter(inputNode));
            initializeMetadataTableView(inputMetaView, inputNode, inputMetaTable);
            inputMetaView.initGraphicComponents();
            inputMetaView.getExtendedTableViewer().setCommandStack(commandStack);

            Label label1 = new Label(compositesSachForm.getMidComposite(), SWT.NONE);
            GridDataFactory.swtDefaults().hint(42, 18).applyTo(label1);
            Composite buttonComposite = new Composite(compositesSachForm.getMidComposite(), SWT.BORDER);
            Label label2 = new Label(compositesSachForm.getMidComposite(), SWT.NONE);
            GridDataFactory.swtDefaults().hint(42, 36).applyTo(label2);

            gridLayout = new GridLayout(1, true);
            buttonComposite.setLayout(gridLayout);
            gridData = new GridData(GridData.FILL_BOTH);
            // gridData.verticalAlignment = GridData.CENTER;
            buttonComposite.setLayoutData(gridData);

            Composite buttonComposite2 = new Composite(buttonComposite, SWT.NONE);

            gridLayout = new GridLayout(1, true);
            gridLayout.marginBottom = 0;
            gridLayout.marginHeight = 0;
            gridLayout.marginLeft = 0;
            gridLayout.marginRight = 0;
            gridLayout.marginTop = 0;
            gridLayout.marginWidth = 0;
            buttonComposite2.setLayout(gridLayout);
            gridData = new GridData(GridData.FILL_BOTH);
            gridData.verticalAlignment = GridData.CENTER;
            buttonComposite2.setLayoutData(gridData);

            // qli comment
            // Input => Output(the selection)
            Button copySelectionToOutput = new Button(buttonComposite2, SWT.NONE);
            copySelectionToOutput.setImage(ImageProvider.getImage(EImage.RIGHT_ICON));
            copySelectionToOutput.setToolTipText(Messages.getString("MetadataDialog.CopySelectionToOutput")); //$NON-NLS-1$
            GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).applyTo(copySelectionToOutput);

            copySelectionToOutput.addListener(SWT.Selection, new Listener() {

                @Override
                public void handleEvent(Event event) {
                    // qli comment
                    // Input => Output(the selection)
                    // if the selectionline above zero.just run the method "copyTable(list, getOutputMetaData())".
                    tableItem = inputMetaView.getTable().getSelection();
                    List list = new ArrayList<IMetadataColumn>();
                    for (TableItem element : tableItem) {
                        column = (IMetadataColumn) element.getData();
                        list.add(column);
                    }
                    if (tableItem.length > 0) {
                        MetadataToolHelper.copyTable(list, getOutputMetaData());
                        outputMetaView.getTableViewerCreator().refresh();
                    }
                }
            });

            // Input => Output
            Button copyToOutput = new Button(buttonComposite2, SWT.NONE);
            copyToOutput.setImage(ImageProvider.getImage(EImage.RIGHTX_ICON));
            copyToOutput.setToolTipText(Messages.getString("MetadataDialog.CopyToOutput")); //$NON-NLS-1$
            GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).applyTo(copyToOutput);

            copyToOutput.addListener(SWT.Selection, new Listener() {

                @Override
                public void handleEvent(Event event) {
                    MessageBox messageBox = new MessageBox(parent.getShell(), SWT.APPLICATION_MODAL | SWT.OK | SWT.CANCEL);
                    messageBox.setText(Messages.getString("MetadataDialog.SchemaModification")); //$NON-NLS-1$
                    messageBox.setMessage(Messages.getString("MetadataDialog.Message")); //$NON-NLS-1$
                    if (messageBox.open() == SWT.OK) {
                        MetadataToolHelper.copyTable(getInputMetaData(), getOutputMetaData(), true);
                        outputMetaView.getTableViewerCreator().refresh();
                    }
                }
            });

            Label lable1 = new Label(buttonComposite2, SWT.NONE);
            GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).applyTo(lable1);
            Label lable2 = new Label(buttonComposite2, SWT.NONE);
            GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).applyTo(lable2);
            Label lable3 = new Label(buttonComposite2, SWT.NONE);
            GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).applyTo(lable3);

            // qli comment
            // Output => Input(the selection)
            Button copySelectionToInput = new Button(buttonComposite2, SWT.NONE);
            copySelectionToInput.setImage(ImageProvider.getImage(EImage.LEFT_ICON));
            copySelectionToInput.setToolTipText(Messages.getString("MetadataDialog.CopySelectionToInput.toolTipText")); //$NON-NLS-1$
            gridData.verticalAlignment = GridData.CENTER;
            copySelectionToInput.setLayoutData(gridData);
            copySelectionToInput.addListener(SWT.Selection, new Listener() {

                @Override
                public void handleEvent(Event event) {
                    // qli comment
                    // Output => Input(selection)
                    // if the selectionline above zero.just run the method "copyTable(list, getInputMetaData())".
                    tableItem = outputMetaView.getTable().getSelection();
                    List list = new ArrayList<IMetadataColumn>();
                    for (TableItem element : tableItem) {
                        column = (IMetadataColumn) element.getData();
                        list.add(column);
                    }
                    if (tableItem.length > 0) {
                        MetadataToolHelper.copyTable(list, getInputMetaData());
                        inputMetaView.getTableViewerCreator().refresh();
                    }
                }
            });

            // Output => Input
            Button copyToInput = new Button(buttonComposite2, SWT.NONE);
            copyToInput.setImage(ImageProvider.getImage(EImage.LEFTX_ICON));
            copyToInput.setToolTipText(Messages.getString("MetadataDialog.CopyToInput.toolTipText")); //$NON-NLS-1$
            gridData.verticalAlignment = GridData.CENTER;
            copyToInput.setLayoutData(gridData);
            copyToInput.addListener(SWT.Selection, new Listener() {

                @Override
                public void handleEvent(Event event) {
                    MessageBox messageBox = new MessageBox(parent.getShell(), SWT.APPLICATION_MODAL | SWT.OK | SWT.CANCEL);
                    messageBox.setText(Messages.getString("MetadataDialog.SchemaModification")); //$NON-NLS-1$
                    messageBox.setMessage(Messages.getString("MetadataDialog.TransferMessage")); //$NON-NLS-1$
                    if (messageBox.open() == SWT.OK) {
                        MetadataToolHelper.copyTable(getOutputMetaData(), getInputMetaData());
                        inputMetaView.getTableViewerCreator().refresh();
                    }
                }
            });

            if (inputReadOnly || inputMetaTable.isReadOnly()) {
                copyToInput.setEnabled(false);
                copySelectionToInput.setEnabled(false);
            }

            MetadataTableEditor metadataTableEditorForOutput;
            if (isRepository(outputNode, outputMetaTable)) {
                metadataTableEditorForOutput = new MetadataTableEditor(outputMetaTable, titleOutput + " (Output)", true);
            } else {
                metadataTableEditorForOutput = new MetadataTableEditor(outputMetaTable, titleOutput + " (Output)");
            }
            outputMetaView = new DialogMetadataTableEditorView(compositesSachForm.getRightComposite(), SWT.NONE,
                    metadataTableEditorForOutput, outputReadOnly, true, true, false);
            outputMetaView.setMetadataTalendTypeFilter(NodeUtil.createMetadataTalendTypeFilter(outputNode));
            outputMetaView.setIsRepository(isRepository(outputNode, outputMetaTable));
            initializeMetadataTableView(outputMetaView, outputNode, outputMetaTable);
            outputMetaView.initGraphicComponents();
            outputMetaView.getExtendedTableViewer().setCommandStack(commandStack);
            // see bug 7471,add a listener for outputView
            outputMetaView.getMetadataTableEditor().addModifiedBeanListener(new IModifiedBeanListener<IMetadataColumn>() {

                @Override
                public void handleEvent(ModifiedBeanEvent<IMetadataColumn> event) {
                    if (AbstractMetadataTableEditorView.ID_COLUMN_NAME.equals(event.column.getId())) {
                        IMetadataColumn modifiedObject = event.bean;
                        if (modifiedObject != null) {
                            String originalLabel = changeNameOutColumns.get(modifiedObject);
                            if (originalLabel == null) {
                                changeNameOutColumns.put(modifiedObject.getLabel(), (String) event.previousValue);
                            }
                        }
                    }
                }

            });

            if (outputReadOnly || outputMetaTable.isReadOnly()) {
                boolean enabledForAll = false;
                copySelectionToOutput.setEnabled(false);
                if (outputNode.getComponent().isSchemaAutoPropagated()
                        && !outputMetaTable.sameMetadataAs(inputMetaTable, IMetadataColumn.OPTIONS_IGNORE_KEY
                                | IMetadataColumn.OPTIONS_IGNORE_NULLABLE | IMetadataColumn.OPTIONS_IGNORE_COMMENT
                                | IMetadataColumn.OPTIONS_IGNORE_PATTERN | IMetadataColumn.OPTIONS_IGNORE_DBCOLUMNNAME
                                | IMetadataColumn.OPTIONS_IGNORE_DBTYPE | IMetadataColumn.OPTIONS_IGNORE_DEFAULT
                                | IMetadataColumn.OPTIONS_IGNORE_BIGGER_SIZE)) {
                    enabledForAll = true;
                }

                copyToOutput.setEnabled(enabledForAll);
            }
            compositesSachForm.setGridDatas();
            CustomTableManager.addCustomManagementToTable(inputMetaView, inputReadOnly);
            CustomTableManager.addCustomManagementToToolBar(inputMetaView, inputMetaTable, inputReadOnly, outputMetaView,
                    outputMetaTable, outputNode.getComponent().isSchemaAutoPropagated());
        }
        CustomTableManager.addCustomManagementToTable(outputMetaView, outputReadOnly);
        CustomTableManager.addCustomManagementToToolBar(outputMetaView, outputMetaTable, outputReadOnly, inputMetaView,
                inputMetaTable, false);
        metadataTableEditor.addModifiedBeanListener(new IModifiedBeanListener<IMetadataColumn>() {

            @Override
            public void handleEvent(ModifiedBeanEvent<IMetadataColumn> event) {
                if ((inputMetaTable != null) && outputMetaTable.isReadOnly()
                        && outputNode.getComponent().isSchemaAutoPropagated()) {
                    MetadataToolHelper.copyTable(inputMetaTable, outputMetaTable);
                    outputMetaView.getTableViewerCreator().refresh();
                }
                if (AbstractMetadataTableEditorView.ID_COLUMN_NAME.equals(event.column.getId())) {
                    IMetadataColumn modifiedObject = event.bean;
                    if (modifiedObject != null) {
                        String originalLabel = changedNameColumns.get(modifiedObject);
                        if (originalLabel == null) {
                            changedNameColumns.put(modifiedObject, (String) event.previousValue);
                            changeNameInColumns.put(modifiedObject.getLabel(), (String) event.previousValue);
                        }
                    }
                }
                if (AbstractMetadataTableEditorView.ID_COLUMN_USEFUL.equals(event.column.getId())) {
                    isUsefulChange = true;
                }

            }

        });
        return composite;
    }

    /**
     * Returns input metadata.
     * 
     * @return
     */
    public IMetadataTable getInputMetaData() {
        if (inputMetaView == null) {
            return null;
        }
        return inputMetaView.getMetadataTableEditor().getMetadataTable();
    }

    /**
     * Returns output metadata.
     * 
     * @return
     */
    public IMetadataTable getOutputMetaData() {
        return outputMetaView.getMetadataTableEditor().getMetadataTable();
    }

    public void setInputReadOnly(boolean inputReadOnly) {
        this.inputReadOnly = inputReadOnly;
    }

    public void setOutputReadOnly(boolean outputReadOnly) {
        this.outputReadOnly = outputReadOnly;
    }

    /**
     * Getter for inputReadOnly.
     * 
     * @return the inputReadOnly
     */
    public boolean isInputReadOnly() {
        return this.inputReadOnly;
    }

    /**
     * Getter for outputReadOnly.
     * 
     * @return the outputReadOnly
     */
    public boolean isOutputReadOnly() {
        return this.outputReadOnly;
    }

    public ThreeCompositesSashForm getCompositesSachForm() {
        return this.compositesSachForm;
    }

    public MetadataTableEditorView getInputMetaView() {
        return this.inputMetaView;
    }

    /**
     * DOC Administrator Comment method "getMappingTypeLabelById".
     * 
     * @param mappingTypeId
     * @return
     */
    private static String getMappingTypeLabelById(String mappingTypeId) {
        Dbms[] dbmsArray = MetadataTalendType.getAllDbmsArray();

        for (Dbms element : dbmsArray) {
            String indexId = element.getId();
            if (mappingTypeId.equals(indexId)) {
                return element.getLabel();
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed() {
        // ComboEditor focus lost
        if (inputMetaView != null) {
            inputMetaView.notifyFocusLost();
        }
        if (outputMetaView != null) {
            outputMetaView.notifyFocusLost();
        }
        super.okPressed();
        IMetadataTable outputTable = getOutputMetaData();
        IMetadataTable inputTable = getInputMetaData();

        // see bug 7471
        Map<String, String> changedNameColumnsForInput = null;
        Map<String, String> changedNameColumnsForOutput = null;
        if (inputMetaTable == null) {
            changedNameColumnsForOutput = this.changeNameInColumns;
            getPreColumnsSet(preOutputColumnSet, changedNameColumnsForOutput);
        } else {
            changedNameColumnsForInput = this.changeNameInColumns;
            getPreColumnsSet(preInputColumnSet, changedNameColumnsForInput);
            changedNameColumnsForOutput = this.changeNameOutColumns;
            getPreColumnsSet(preOutputColumnSet, changedNameColumnsForOutput);
        }

        IDesignerCoreService designerCoreService = CoreUIPlugin.getDefault().getDesignerCoreService();
        designerCoreService.setTraceFilterParameters(outputNode, outputTable, preOutputColumnSet, changedNameColumnsForOutput);
        designerCoreService.setTraceFilterParameters(inputNode, inputTable, preInputColumnSet, changedNameColumnsForInput);
        if (outputTable != null && inputTable != null) {
            for (IMetadataColumn column : outputTable.getListColumns()) {
                IMetadataColumn inputColumn = inputTable.getColumn(column.getLabel());
                if (inputColumn != null) {
                    inputColumn.setOriginalDbColumnName(column.getOriginalDbColumnName());
                    inputColumn.setOriginalLength(column.getOriginalLength());
                }
            }
        }
        if (isUsefulChange) {
            if (outputNode != null) {
                if (outputNode.getComponent().getRepositoryType() != null
                        && outputNode.getComponent().getRepositoryType().contains("DATABASE")) {
                    MessageDialog.openInformation(getParentShell(), "", Messages.getString("MetadataDialog.NeedDoGuessQuery"));
                }
            }
        }
    }

    /**
     * DOC wzhang Comment method "getPreColumnsSet".
     */
    public Set<String> getPreColumnsSet(Set<String> preColumnSet, Map<String, String> changedNameColumnMap) {
        if (changedNameColumnMap != null) {
            for (String changeNameCol : changedNameColumnMap.keySet()) {
                String colName = changedNameColumnMap.get(changeNameCol);
                if (preColumnSet.contains(colName)) {
                    preColumnSet.remove(colName);
                    preColumnSet.add(changeNameCol);
                }
            }
            return preColumnSet;
        }
        return preColumnSet;
    }

    class DialogMetadataTableEditorView extends MetadataTableEditorView {

        public DialogMetadataTableEditorView(Composite parentComposite, int mainCompositeStyle,
                ExtendedTableModel<IMetadataColumn> extendedTableModel, boolean readOnly, boolean toolbarVisible,
                boolean labelVisible, boolean initGraphicsComponents) {
            super(parentComposite, mainCompositeStyle, extendedTableModel, readOnly, toolbarVisible, labelVisible,
                    initGraphicsComponents);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.talend.core.ui.metadata.editor.MetadataTableEditorView#setTableViewerCreatorOptions(org.talend.commons
         * .ui.swt.tableviewer.TableViewerCreator)
         */
        @Override
        protected void setTableViewerCreatorOptions(TableViewerCreator<IMetadataColumn> newTableViewerCreator) {
            super.setTableViewerCreatorOptions(newTableViewerCreator);
            newTableViewerCreator.setLazyLoad(true);
        }
    }

    public static void setSingleAndStruct(boolean isSingle) {
        isSingleAndStruct = isSingle;
    }

}
