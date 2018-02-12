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
package org.talend.repository.ui.wizards.metadata.connection.files.salesforce;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorNotModifiable.LAYOUT_MODE;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledText;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.utils.data.list.IListenableListListener;
import org.talend.commons.utils.data.list.ListenableListEvent;
import org.talend.commons.utils.data.text.IndiceHelper;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataContextModeManager;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.MetadataToolHelper;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.SalesforceModuleUnit;
import org.talend.core.model.metadata.builder.connection.SalesforceSchemaConnection;
import org.talend.core.model.metadata.types.JavaDataTypeHelper;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.PerlDataTypeHelper;
import org.talend.core.model.metadata.types.PerlTypesManager;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.model.preview.SalesforceSchemaBean;
import org.talend.core.ui.metadata.editor.MetadataEmfTableEditor;
import org.talend.core.ui.metadata.editor.MetadataEmfTableEditorView;
import org.talend.core.utils.CsvArray;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.metadata.managment.ui.preview.ProcessDescription;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;
import org.talend.metadata.managment.ui.utils.OtherConnectionContextUtils;
import org.talend.metadata.managment.ui.utils.ShadowProcessHelper;
import org.talend.metadata.managment.ui.wizard.metadata.MetadataContextModeManager;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.metadata.ui.wizards.form.AbstractSalesforceStepForm;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * 
 * DOC yexiaowei class global comment. Detailled comment
 */
public class SalesforceStep4Form extends AbstractSalesforceStepForm {

    private static Logger log = Logger.getLogger(SalesforceStep4Form.class);

    private static final int WIDTH_GRIDDATA_PIXEL = 550;

    private TableViewerCreator tableViewerCreator;

    private Table tableNavigator;

    private UtilsButton cancelButton;

    private UtilsButton guessButton;

    private UtilsButton addTableButton;

    private UtilsButton removeTableButton;

    private MetadataEmfTableEditor metadataEditor;

    private MetadataEmfTableEditorView tableEditorView;

    private Label informationLabel;

    private MetadataTable metadataTable;

    private LabelledText metadataNameText;

    private LabelledText metadataCommentText;

    private boolean readOnly;

    private SalesforceSchemaConnection temConnection;

    private String moduleName;

    private ArrayList<String> existedNames = new ArrayList<String>();

    /**
     * Constructor to use by RCP Wizard.
     * 
     * @param Composite
     */
    // public SalesforceStep3FormToBe(Composite parent, ConnectionItem connectionItem, MetadataTable metadataTable,
    // String[] existingNames, SalesforceModuleParseAPI salesforceAPI) {
    // this(parent, connectionItem, metadataTable, existingNames, salesforceAPI, null);
    // }

    public SalesforceStep4Form(Composite parent, ConnectionItem connectionItem, SalesforceSchemaConnection temConnection,
            MetadataTable metadataTable, String[] existingNames, SalesforceModuleParseAPI salesforceAPI,
            IMetadataContextModeManager contextModeManager, String moduleName) {
        super(parent, connectionItem, metadataTable, existingNames, salesforceAPI);
        this.temConnection = temConnection;
        this.connectionItem = connectionItem;
        this.metadataTable = metadataTable;
        this.moduleName = moduleName;
        setConnectionItem(connectionItem);
        setContextModeManager(contextModeManager);
        setupForm();
    }

    /**
     * 
     * Initialize value, forceFocus first field.
     */
    @Override
    protected void initialize() {
        // init the metadata Table
        String label = MetadataToolHelper.validateValue(metadataTable.getLabel());
        metadataNameText.setText(label);
        metadataCommentText.setText(metadataTable.getComment());
        metadataEditor.setMetadataTable(metadataTable);
        tableEditorView.setMetadataEditor(metadataEditor);
        tableEditorView.getTableViewerCreator().layout();

        if (getConnection().isReadOnly()) {
            adaptFormToReadOnly();
        } else {
            updateStatus(IStatus.OK, null);
        }

    }

    @Override
    protected void adaptFormToReadOnly() {
        readOnly = isReadOnly();
        guessButton.setEnabled(!isReadOnly());
        metadataNameText.setReadOnly(isReadOnly());
        metadataCommentText.setReadOnly(isReadOnly());
        tableEditorView.setReadOnly(isReadOnly());

        // if (getParent().getChildren().length == 1) { // open the table
        // guessButton.setEnabled(false);
        // informationLabel.setVisible(false);
        // }
    }

    @Override
    protected void addFields() {

        int leftCompositeWidth = 125;
        int rightCompositeWidth = WIDTH_GRIDDATA_PIXEL - leftCompositeWidth;
        int headerCompositeHeight = 80;
        int tableSettingsCompositeHeight = 15;
        int tableCompositeHeight = 200;

        int height = headerCompositeHeight + tableSettingsCompositeHeight + tableCompositeHeight;

        // Header Fields
        Composite mainComposite = Form.startNewDimensionnedGridLayout(this, 2, WIDTH_GRIDDATA_PIXEL, 60);

        SashForm sash = new SashForm(mainComposite, SWT.HORIZONTAL);
        GridData sashData = new GridData(GridData.FILL_BOTH);
        sash.setLayoutData(sashData);
        Composite leftComposite = Form.startNewDimensionnedGridLayout(sash, 1, leftCompositeWidth, height);
        Composite rightComposite = Form.startNewDimensionnedGridLayout(sash, 1, rightCompositeWidth, height);
        sash.setWeights(new int[] { 1, 5 });
        addTreeNavigator(leftComposite, leftCompositeWidth, height);
        metadataNameText = new LabelledText(rightComposite, Messages.getString("FileStep3.metadataName")); //$NON-NLS-1$
        metadataCommentText = new LabelledText(rightComposite, Messages.getString("FileStep3.metadataComment")); //$NON-NLS-1$

        // Group MetaData
        Group groupMetaData = Form.createGroup(rightComposite, 1, Messages.getString("FileStep3.groupMetadata"), 280); //$NON-NLS-1$
        Composite compositeMetaData = Form.startNewGridLayout(groupMetaData, 1);

        // Composite Guess
        Composite compositeGuessButton = Form.startNewDimensionnedGridLayout(compositeMetaData, 2, WIDTH_GRIDDATA_PIXEL, 40);
        informationLabel = new Label(compositeGuessButton, SWT.NONE);
        informationLabel
                .setText(Messages.getString("FileStep3.informationLabel") + "                                                  "); //$NON-NLS-1$ //$NON-NLS-2$
        informationLabel.setSize(500, HEIGHT_BUTTON_PIXEL);

        guessButton = new UtilsButton(compositeGuessButton, Messages.getString("FileStep3.guess"), WIDTH_BUTTON_PIXEL, //$NON-NLS-1$
                HEIGHT_BUTTON_PIXEL);
        guessButton.setToolTipText(Messages.getString("FileStep3.guessTip")); //$NON-NLS-1$

        // Composite MetadataTableEditorView
        Composite compositeTable = Form.startNewDimensionnedGridLayout(compositeMetaData, 1, WIDTH_GRIDDATA_PIXEL, 200);
        compositeTable.setLayout(new FillLayout());
        metadataEditor = new MetadataEmfTableEditor(Messages.getString("FileStep3.metadataDescription")); //$NON-NLS-1$
        tableEditorView = new MetadataEmfTableEditorView(compositeTable, SWT.NONE);

        if (!isInWizard()) {
            // Bottom Button
            Composite compositeBottomButton = Form.startNewGridLayout(this, 2, false, SWT.CENTER, SWT.CENTER);
            // Button Cancel
            cancelButton = new UtilsButton(compositeBottomButton, Messages.getString("CommonWizard.cancel"), WIDTH_BUTTON_PIXEL, //$NON-NLS-1$
                    HEIGHT_BUTTON_PIXEL);
        }
        addUtilsButtonListeners();
    }

    @Override
    protected void addFieldsListeners() {
        // Navigation : when the user select a table
        tableNavigator.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                String schemaLabel = tableNavigator.getSelection()[0].getText();

                // org.talend.core.model.metadata.MetadataTable table = null;
                EList<SalesforceModuleUnit> modules = temConnection.getModules();
                for (int i = 0; i < modules.size(); i++) {
                    if (modules.get(i).getModuleName().equals(moduleName)) {
                        for (int j = 0; j < modules.get(i).getTables().size(); j++) {
                            if (modules.get(i).getTables().get(j).getLabel().equals(schemaLabel)) {
                                metadataTable = modules.get(i).getTables().get(j);
                                metadataNameText.setText(schemaLabel);
                                break;
                            }
                        }
                        break;
                    }
                }
                metadataEditor.setMetadataTable(metadataTable);
                if (isReadOnly()) {
                    addTableButton.setEnabled(false);
                }
                // }
            }
        });
        // metadataNameText : Event modifyText
        metadataNameText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                String labelText = metadataNameText.getText();
                MetadataToolHelper.validateSchema(labelText);
                changeTableNavigatorStatus(labelText);
                metadataTable.setLabel(labelText);
                if (tableNavigator.getSelection().length > 0) {
                    tableNavigator.getSelection()[0].setText(labelText);
                }
                changeTableNavigatorStatus(checkFieldsValue());

                // kFieldsValue();
            }
        });
        // metadataNameText : Event KeyListener
        metadataNameText.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                MetadataToolHelper.checkSchema(getShell(), e);
                // initTreeNavigatorNodes();

            }
        });

        // metadataCommentText : Event modifyText
        metadataCommentText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                metadataTable.setComment(metadataCommentText.getText());
            }
        });

        // add listener to tableMetadata (listen the event of the toolbars)
        tableEditorView.getMetadataEditor().addAfterOperationListListener(new IListenableListListener() {

            @Override
            public void handleEvent(ListenableListEvent event) {
                checkFieldsValue();
            }
        });
        // add listener to tableMetadata (listen the event of the toolbars)
        metadataEditor.addAfterOperationListListener(new IListenableListListener() {

            @Override
            public void handleEvent(ListenableListEvent event) {
                changeTableNavigatorStatus(checkFieldsValue());
            }
        });
    }

    private void changeTableNavigatorStatus(String schemaLabel) {
        Composite leftGroup = tableNavigator.getParent().getParent().getParent();
        Control[] children = leftGroup.getChildren();
        if (schemaLabel == null || schemaLabel.length() == 0) {
            leftGroup.setEnabled(false);
            changeControlStatus(children, false);
        } else {
            leftGroup.setEnabled(true);
            changeControlStatus(children, true);
        }
    }

    private void changeControlStatus(Control[] children, boolean status) {
        for (Control control : children) {
            control.setEnabled(status);
            if (control instanceof Composite) {
                Control[] subChildren = ((Composite) control).getChildren();
                changeControlStatus(subChildren, status);
            }
        }
    }

    private void changeTableNavigatorStatus(boolean isEnabled) {
        Composite leftGroup = tableNavigator.getParent().getParent().getParent();
        Control[] children = leftGroup.getChildren();
        leftGroup.setEnabled(isEnabled);
        changeControlStatus(children, isEnabled);
    }

    /**
     * addButtonControls.
     * 
     * @param cancelButton
     */
    @Override
    protected void addUtilsButtonListeners() {

        // Event guessButton
        guessButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (tableEditorView.getMetadataEditor().getBeanCount() > 0) {

                    if (!guessButton.getEnabled()) {
                        guessButton.setEnabled(true);
                        if (MessageDialog.openConfirm(getShell(), Messages.getString("FileStep3.guessConfirmation"), Messages //$NON-NLS-1$
                                .getString("FileStep3.guessConfirmationMessage"))) { //$NON-NLS-1$
                            runShadowProcess();
                        }
                    } else {
                        guessButton.setEnabled(false);
                    }

                } else {

                    if (!guessButton.getEnabled()) {
                        guessButton.setEnabled(true);
                        runShadowProcess();
                    } else {
                        guessButton.setEnabled(false);
                    }
                }
            }

        });
        // Event addTable Button
        addTableButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (!addTableButton.getEnabled()) {
                    addTableButton.setEnabled(true);
                    addMetadataTable();
                } else {
                    addTableButton.setEnabled(false);
                }
            }
        });

        removeTableButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (!removeTableButton.getEnabled()) {
                    removeTableButton.setEnabled(true);
                    TableItem[] selection = tableNavigator.getSelection();
                    if (selection != null && selection.length > 0) {
                        boolean openConfirm = MessageDialog.openConfirm(getShell(), "Confirm",
                                "Are you sure to delete this schema ?");
                        if (openConfirm) {
                            for (TableItem item : selection) {

                                if (tableNavigator.indexOf(item) != -1) {
                                    EList<SalesforceModuleUnit> modules = temConnection.getModules();
                                    for (int i = 0; i < modules.size(); i++) {
                                        if (modules.get(i).getModuleName().equals(moduleName)) {
                                            for (int j = 0; j < modules.get(i).getTables().size(); j++) {
                                                if (item.getText().equals(modules.get(i).getTables().get(j).getLabel())) {
                                                    modules.get(i).getTables().remove(j);
                                                }
                                            }
                                        }
                                    }
                                    tableNavigator.remove(tableNavigator.indexOf(item));
                                    if (tableNavigator.getItemCount() > 1) {
                                        tableNavigator.setSelection(tableNavigator.getItem(tableNavigator.getItemCount() - 1));
                                    }
                                }
                            }
                            initTreeNavigatorNodes();
                        }
                    }
                } else {
                    removeTableButton.setEnabled(false);
                }

            }

        });
        if (cancelButton != null) {
            // Event CancelButton
            cancelButton.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    getShell().close();
                }
            });
        }

    }

    protected void addMetadataTable() {
        // Create a new metadata and Add it on the connection
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
        metadataTable.setId(factory.getNextId());
        // initExistingNames();

        metadataTable.setLabel(IndiceHelper.getIndexedLabel(metadataTable.getLabel(), existingNames));
        EList<SalesforceModuleUnit> modules = temConnection.getModules();
        // EList<SalesforceModuleUnit> modules = getConnection().getModules();
        for (int i = 0; i < modules.size(); i++) {
            if (modules.get(i).getModuleName().equals(moduleName)) {

                modules.get(i).getTables().add(modules.get(i).getTables().size(), metadataTable);
                break;
            }

        }
        // init TreeNavigator
        initTreeNavigatorNodes();
        metadataNameText.setText(metadataTable.getLabel());
    }

    /**
     * DOC ocarbone Comment method "addTreeNavigator".
     * 
     * @param parent
     * @param width
     * @param height
     */
    private void addTreeNavigator(Composite parent, int width, int height) {
        // Group
        Group group = Form.createGroup(parent, 1, Messages.getString("DatabaseTableForm.navigatorTree"), height); //$NON-NLS-1$

        // ScrolledComposite
        ScrolledComposite scrolledCompositeFileViewer = new ScrolledComposite(group, SWT.H_SCROLL | SWT.V_SCROLL | SWT.NONE);
        scrolledCompositeFileViewer.setExpandHorizontal(true);
        scrolledCompositeFileViewer.setExpandVertical(true);
        GridData gridData1 = new GridData(GridData.FILL_BOTH);
        gridData1.widthHint = width + 12;
        gridData1.heightHint = height;
        gridData1.horizontalSpan = 2;
        scrolledCompositeFileViewer.setLayoutData(gridData1);

        tableViewerCreator = new TableViewerCreator(scrolledCompositeFileViewer);
        tableViewerCreator.setHeaderVisible(false);
        tableViewerCreator.setColumnsResizableByDefault(false);
        tableViewerCreator.setBorderVisible(false);
        tableViewerCreator.setLinesVisible(false);
        tableViewerCreator.setLayoutMode(LAYOUT_MODE.NONE);
        tableViewerCreator.setCheckboxInFirstColumn(false);
        tableViewerCreator.setFirstColumnMasked(false);

        tableNavigator = tableViewerCreator.createTable();
        tableNavigator.setLayoutData(new GridData(GridData.FILL_BOTH));

        TableColumn tableColumn = new TableColumn(tableNavigator, SWT.NONE);
        tableColumn.setText(Messages.getString("DatabaseTableForm.tableColumnText.talbe")); //$NON-NLS-1$
        tableColumn.setWidth(width + 120);

        scrolledCompositeFileViewer.setContent(tableNavigator);
        scrolledCompositeFileViewer.setSize(width + 12, height);

        // // Button Add metadata Table
        Composite button = Form.startNewGridLayout(group, HEIGHT_BUTTON_PIXEL, false, SWT.CENTER, SWT.CENTER);
        addTableButton = new UtilsButton(button,
                Messages.getString("DatabaseTableForm.AddTable"), width - 30, HEIGHT_BUTTON_PIXEL); //$NON-NLS-1$

        Composite rmButton = Form.startNewGridLayout(group, HEIGHT_BUTTON_PIXEL, false, SWT.CENTER, SWT.CENTER);
        removeTableButton = new UtilsButton(rmButton, "Remove Schema", width - 30, HEIGHT_BUTTON_PIXEL); //$NON-NLS-1$
    }

    /**
     * create ProcessDescription and set it.
     * 
     * WARNING ::field FieldSeparator, RowSeparator, EscapeChar and TextEnclosure are surround by double quote.
     * 
     * 
     * @return processDescription
     */
    private ProcessDescription getProcessDescription(SalesforceSchemaConnection originalValueConnection) {

        ProcessDescription processDescription = ShadowProcessHelper.getProcessDescription(originalValueConnection);

        SalesforceSchemaBean bean = new SalesforceSchemaBean();

        bean.setWebServerUrl(originalValueConnection.getWebServiceUrl());
        bean.setUserName(originalValueConnection.getUserName());
        bean.setPassword(originalValueConnection.getValue(originalValueConnection.getPassword(), false));
        bean.setModuleName(originalValueConnection.getModuleName());
        bean.setQueryCondition(originalValueConnection.getQueryCondition());
        // add for feature 7507
        bean.setBatchSize(originalValueConnection.getBatchSize());
        bean.setUseProxy(originalValueConnection.isUseProxy());
        bean.setUesHttp(originalValueConnection.isUseHttpProxy());
        bean.setProxyHost(originalValueConnection.getProxyHost());
        bean.setProxyPort(originalValueConnection.getProxyPort());
        bean.setProxyUsername(originalValueConnection.getProxyUsername());
        bean.setProxyPassword(originalValueConnection.getValue(originalValueConnection.getProxyPassword(), false));

        processDescription.setSalesforceSchemaBean(bean);

        List<IMetadataTable> tableSchema = new ArrayList<IMetadataTable>();
        IMetadataTable tableGet = getMetadatasForSalesforce(bean.getWebServerUrl(), bean.getUserName(), bean.getPassword(),
                String.valueOf(bean.getTimeOut()), bean.getModuleName(), bean.getBatchSize(), bean.isUseProxy(),
                bean.isUesHttp(), bean.getProxyHost(), bean.getProxyPort(), bean.getProxyUsername(), bean.getProxyPassword(),
                false);

        IMetadataTable table = new org.talend.core.model.metadata.MetadataTable();
        List<IMetadataColumn> schema = new ArrayList<IMetadataColumn>();
        for (IMetadataColumn column : tableGet.getListColumns()) {
            schema.add(column.clone());
        }

        table.setTableName("tSalesforceInput"); //$NON-NLS-1$
        table.setListColumns(schema);
        tableSchema.add(table);

        processDescription.setSchema(tableSchema);

        processDescription.setEncoding(TalendQuoteUtils.addQuotes("ISO-8859-15")); //$NON-NLS-1$

        return processDescription;
    }

    /**
     * run a ShadowProcess to determined the Metadata.
     */
    protected void runShadowProcess() {
        initGuessSchema();
        SalesforceSchemaConnection originalValueConnection = getOriginalValueConnection();
        // if no file, the process don't be executed
        if (originalValueConnection.getWebServiceUrl() == null || originalValueConnection.getWebServiceUrl().equals("")) { //$NON-NLS-1$
            informationLabel.setText("Salesforce endpoint lost" //$NON-NLS-1$ 
                    + "                                                                              "); //$NON-NLS-1$
            return;
        }

        // try {
        informationLabel.setText("   " + Messages.getString("FileStep3.guessProgress")); //$NON-NLS-1$ //$NON-NLS-2$

        // get the XmlArray width an adapt ProcessDescription
        ProcessDescription processDescription = getProcessDescription(originalValueConnection);

        IMetadataTable metadataTableOrder = readMetadataDetail();
        if (metadataTableOrder != null) {
            metadataTableClone = metadataTableOrder.clone();
            metadataTableOrder = modifyMetadataTable();
        }
        List<IMetadataTable> schema = processDescription.getSchema();
        if (schema != null && schema.size() > 0) {
            if (useAlphbet) {
                if (metadataTableOrder != null) {
                    schema.get(0).setListColumns(metadataTableOrder.getListColumns());
                }
            } else {
                if (metadataTableClone != null) {
                    schema.get(0).setListColumns(metadataTableClone.getListColumns());
                }
            }
        }
        // the web service url is used by tSalesforceInput, see 0004027: Studio crashes when clicking Next on
        // Step 3 of SF wizard
        // processDescription.getSalesforceSchemaBean().setWebServerUrl(TSALESFORCE_INPUT_URL);
        //            CsvArray csvArray = ShadowProcessHelper.getCsvArray(processDescription, "SALESFORCE_SCHEMA", true); //$NON-NLS-1$
        //
        // if (csvArray == null) {
        //                informationLabel.setText("   " + Messages.getString("FileStep3.guessFailure")); //$NON-NLS-1$ //$NON-NLS-2$
        // } else {
        // refreshMetaDataTable(csvArray, processDescription);
        // }

        // } catch (CoreException e) {
        // if (getParent().getChildren().length == 1) {
        //                new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("FileStep3.guessFailureTip") + "\n" //$NON-NLS-1$ //$NON-NLS-2$
        //                        + Messages.getString("FileStep3.guessFailureTip2"), e.getMessage()); //$NON-NLS-1$
        // } else {
        //                new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("FileStep3.guessFailureTip"), e.getMessage()); //$NON-NLS-1$
        // }
        //            log.error(Messages.getString("FileStep3.guessFailure") + " " + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
        // }
        guessSchema(processDescription);
        checkFieldsValue();
    }

    /**
     * DOC zli Comment method "guessSchema".
     * 
     * @param processDescription
     */
    public void guessSchema(ProcessDescription processDescription) {
        informationLabel.setText("   " + Messages.getString("FileStep3.guessIsDone")); //$NON-NLS-1$ //$NON-NLS-2$
        // clear all items
        tableEditorView.getMetadataEditor().removeAll();
        List<MetadataColumn> columns = new ArrayList<MetadataColumn>();

        List<IMetadataColumn> listColumns = processDescription.getSchema().get(0).getListColumns();
        int size = listColumns.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                MetadataColumn metadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
                metadataColumn.setNullable(listColumns.get(i).isNullable());
                metadataColumn.setLength(listColumns.get(i).getLength());
                metadataColumn.setPattern(listColumns.get(i).getPattern());
                metadataColumn.setTalendType(listColumns.get(i).getTalendType());
                metadataColumn.setPrecision(listColumns.get(i).getPrecision());
                metadataColumn.setLabel(listColumns.get(i).getLabel());
                columns.add(i, metadataColumn);
            }
        }
        tableEditorView.getMetadataEditor().addAll(columns);
        tableEditorView.getTableViewerCreator().layout();
        informationLabel.setText(Messages.getString("FileStep3.guessTip")); //$NON-NLS-1$

    }

    public void refreshMetaDataTable(final CsvArray csvArray, ProcessDescription processDescription) {
        informationLabel.setText("   " + Messages.getString("FileStep3.guessIsDone")); //$NON-NLS-1$ //$NON-NLS-2$
        // clear all items
        tableEditorView.getMetadataEditor().removeAll();

        List<MetadataColumn> columns = new ArrayList<MetadataColumn>();
        if (csvArray == null || csvArray.getRows().isEmpty()) {
            return;
        } else {

            List<String[]> csvRows = csvArray.getRows();
            Integer numberOfCol = getRightFirstRow(csvRows);

            // define the label to the metadata width the content of the first row
            int firstRowToExtractMetadata = 0;
            List<IMetadataColumn> listColumns = processDescription.getSchema().get(0).getListColumns();
            // the first rows is used to define the label of any metadata
            String[] label = new String[numberOfCol.intValue()];
            for (int i = 0; i < numberOfCol; i++) {
                label[i] = DEFAULT_LABEL + i;
                if (firstRowToExtractMetadata == 0) {

                    label[i] = "" + listColumns.get(i); //$NON-NLS-1$
                }
            }

            for (int i = 0; i < numberOfCol.intValue(); i++) {
                // define the first currentType and assimile it to globalType
                String globalType = null;
                int lengthValue = 0;
                int precisionValue = 0;
                boolean nullAble = true;

                int current = firstRowToExtractMetadata;
                while (globalType == null) {
                    String value = csvRows.get(current)[i];
                    if (LanguageManager.getCurrentLanguage() == ECodeLanguage.JAVA) {
                        if (i >= csvRows.get(current).length) {
                            globalType = "id_String"; //$NON-NLS-1$
                        } else {
                            if (value != null && !"".equals(value)) { //$NON-NLS-1$
                                globalType = JavaDataTypeHelper.getTalendTypeOfValue(value);
                            } else {
                                globalType = listColumns.get(i).getTalendType();
                            }
                            current++;
                        }
                    } else {
                        if (i >= csvRows.get(current).length) {
                            globalType = "String"; //$NON-NLS-1$
                        } else {
                            globalType = PerlDataTypeHelper.getTalendTypeOfValue(value);
                            current++;
                        }
                    }
                }
                nullAble = listColumns.get(i).isNullable();
                lengthValue = listColumns.get(i).getLength();
                precisionValue = listColumns.get(i).getPrecision();

                // define the metadataColumn to field i
                MetadataColumn metadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
                // Convert javaType to TalendType
                String talendType = null;
                if (LanguageManager.getCurrentLanguage() == ECodeLanguage.JAVA) {
                    talendType = globalType;
                    if (globalType.equals(JavaTypesManager.FLOAT.getId()) || globalType.equals(JavaTypesManager.DOUBLE.getId())) {
                        metadataColumn.setPrecision(precisionValue);
                    } else {
                        metadataColumn.setPrecision(0);
                    }
                } else {
                    talendType = PerlTypesManager.getNewTypeName(MetadataTalendType.loadTalendType(globalType,
                            "TALENDDEFAULT", false)); //$NON-NLS-1$
                    if (globalType.equals("FLOAT") || globalType.equals("DOUBLE")) { //$NON-NLS-1$ //$NON-NLS-2$
                        metadataColumn.setPrecision(precisionValue);
                    } else {
                        metadataColumn.setPrecision(0);
                    }
                }

                metadataColumn.setNullable(nullAble);
                metadataColumn.setTalendType(talendType);
                metadataColumn.setLength(lengthValue);
                // bug 6758
                if (talendType.equals("id_Date")) { //$NON-NLS-1$
                    String pattern = listColumns.get(i).getPattern();
                    metadataColumn.setPattern(pattern);
                }
                // Check the label and add it to the table
                metadataColumn.setLabel(tableEditorView.getMetadataEditor().getNextGeneratedColumnName(label[i]));
                columns.add(i, metadataColumn);
            }
        }
        // tableEditorView.getMetadataEditor().registerDataList(columns);
        tableEditorView.getMetadataEditor().addAll(columns);

        checkFieldsValue();
        tableEditorView.getTableViewerCreator().layout();
        informationLabel.setText(Messages.getString("FileStep3.guessTip")); //$NON-NLS-1$
    }

    // CALCULATE THE NULBER OF COLUMNS IN THE PREVIEW
    public Integer getRightFirstRow(List<String[]> csvRows) {

        Integer numbersOfColumns = null;
        int parserLine = csvRows.size();
        if (parserLine > 50) {
            parserLine = 50;
        }
        for (int i = 0; i < parserLine; i++) {
            if (csvRows.get(i) != null) {
                String[] nbRow = csvRows.get(i);
                // List<XmlField> nbRowFields = nbRow.getFields();
                if (numbersOfColumns == null || nbRow.length >= numbersOfColumns) {
                    numbersOfColumns = nbRow.length;
                }
            }
        }
        return numbersOfColumns;
    }

    /**
     * Ensures that fields are set. Update checkEnable / use to checkConnection().
     * 
     * @return
     */
    @Override
    protected boolean checkFieldsValue() {

        if (metadataNameText.getCharCount() == 0) {
            metadataNameText.forceFocus();
            updateStatus(IStatus.ERROR, Messages.getString("FileStep1.nameAlert")); //$NON-NLS-1$
            return false;
        } else if (!MetadataToolHelper.isValidSchemaName(metadataNameText.getText())) {
            metadataNameText.forceFocus();
            updateStatus(IStatus.ERROR, Messages.getString("FileStep1.nameAlertIllegalChar")); //$NON-NLS-1$
            return false;
        } else if (nameExist(metadataNameText.getText())) {
            updateStatus(IStatus.ERROR, Messages.getString("CommonWizard.nameAlreadyExist")); //$NON-NLS-1$
            return false;

        }

        if (tableEditorView.getMetadataEditor().getBeanCount() > 0) {
            updateStatus(IStatus.OK, null);
            return true;
        }
        updateStatus(IStatus.ERROR, Messages.getString("FileStep3.itemAlert")); //$NON-NLS-1$

        return false;
    }

    private boolean nameExist(String name) {
        existedNames.clear();
        for (int i = 0; i < this.temConnection.getModules().size(); i++) {
            if (this.temConnection.getModules().get(i).getModuleName().equals(moduleName)) {
                for (int j = 0; j < temConnection.getModules().get(i).getTables().size(); j++) {
                    existedNames.add(temConnection.getModules().get(i).getTables().get(j).getLabel());
                }
                break;
            }
        }
        boolean result = false;
        int number = 0;
        for (int i = 0; i < this.existedNames.size(); i++) {
            if (existedNames.get(i).equals(name)) {
                number++;
            }
        }
        if (number > 1) {
            result = true;
        } else {
            result = false;
        }

        return result;

    }

    public void saveMetaData() {
    }

    private void initTreeNavigatorNodes() {
        List<String> selectedNames = new ArrayList<String>();
        EList<SalesforceModuleUnit> modules = temConnection.getModules();
        // EList<SalesforceModuleUnit> modules = getConnection().getModules();
        for (int i = 0; i < modules.size(); i++) {
            if (modules.get(i).getModuleName().equals(moduleName)) {
                for (int j = 0; j < modules.get(i).getTables().size(); j++) {
                    selectedNames.add(modules.get(i).getTables().get(j).getLabel());

                }
                break;
            }
        }
        tableNavigator.removeAll();
        TableItem subItem = null;
        String lastSelectName = null;
        if (selectedNames != null && selectedNames.size() >= 1) {
            for (int i = 0; i < selectedNames.size(); i++) {
                subItem = new TableItem(tableNavigator, SWT.NULL);
                subItem.setText(selectedNames.get(i));
                lastSelectName = selectedNames.get(i);
            }
            metadataNameText.setText(subItem.getText());
            tableNavigator.setSelection(subItem);
            metadataTable = getTableByLabel(lastSelectName);
        } else {
            subItem = new TableItem(tableNavigator, SWT.NULL);
            subItem.setText(moduleName);
        }
        metadataEditor.setMetadataTable(metadataTable);
    }

    @Override
    protected org.talend.core.model.metadata.builder.connection.MetadataTable getTableByLabel(String label) {
        org.talend.core.model.metadata.builder.connection.MetadataTable result = null;
        EList<SalesforceModuleUnit> modules = temConnection.getModules();
        for (int i = 0; i < modules.size(); i++) {
            if (modules.get(i).getModuleName().equals(moduleName)) {
                for (int j = 0; j < modules.get(i).getTables().size(); j++) {
                    if (modules.get(i).getTables().get(j).getLabel().equals(label)) {
                        result = modules.get(i).getTables().get(j);
                    }
                }

            }
        }
        return result;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Control#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (super.isVisible()) {
            initTreeNavigatorNodes();
            changeTableNavigatorStatus(checkFieldsValue());
            SalesforceSchemaConnection originalValueConnection = getOriginalValueConnection();
            if (originalValueConnection.getWebServiceUrl() != null && (!originalValueConnection.getWebServiceUrl().equals("")) //$NON-NLS-1$
                    && (tableEditorView.getMetadataEditor().getBeanCount() <= 0)) {
                runShadowProcess();
            }
            if (isReadOnly() != readOnly) {
                adaptFormToReadOnly();
            }
        }
        checkFieldsValue();
    }

    private SalesforceSchemaConnection getOriginalValueConnection() {
        if (isContextMode() && getContextModeManager() != null) {
            return OtherConnectionContextUtils.cloneOriginalValueSalesforceConnection(getConnection(), getContextModeManager()
                    .getSelectedContextType());
        }
        return getConnection();

    }

    protected void initGuessSchema() {
        if (getParent().getChildren().length == 1) { // only open table
            if (getContextModeManager() == null) { // first
                setContextModeManager(new MetadataContextModeManager());
                ConnectionContextHelper.checkContextMode(connectionItem);
            }
            if (connectionItem.getConnection().isContextMode()) {
                ContextType contextTypeForContextMode = ConnectionContextHelper.getContextTypeForContextMode(getShell(),
                        connectionItem.getConnection());
                getContextModeManager().setSelectedContextType(contextTypeForContextMode);
            }

        }
        useAlphbet = getConnection().isUseAlphbet();
    }
}
