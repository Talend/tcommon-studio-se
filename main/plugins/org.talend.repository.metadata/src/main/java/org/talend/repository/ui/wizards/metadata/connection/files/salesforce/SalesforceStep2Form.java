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
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
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
import org.talend.commons.ui.swt.thread.SWTUIThreadProcessor;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataContextModeManager;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.SalesforceModuleUnit;
import org.talend.core.model.metadata.builder.connection.SalesforceSchemaConnection;
import org.talend.core.model.metadata.types.JavaType;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.repository.model.preview.SalesforceSchemaBean;
import org.talend.core.utils.CsvArray;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.metadata.managment.ui.preview.ProcessDescription;
import org.talend.metadata.managment.ui.preview.ShadowProcessPreview;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;
import org.talend.metadata.managment.ui.utils.OtherConnectionContextUtils;
import org.talend.metadata.managment.ui.utils.ShadowProcessHelper;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.metadata.ui.wizards.form.AbstractSalesforceStepForm;

/**
 * DOC YeXiaowei class global comment. Detailled comment <br/>
 * 
 */
public class SalesforceStep2Form extends AbstractSalesforceStepForm {

    private static Logger log = Logger.getLogger(SalesforceStep2Form.class);

    private TableViewerCreator tableViewerCreator;

    private Table tableNavigator;

    // private UtilsButton addTableButton;
    //
    // private UtilsButton removeTableButton;

    private static final int WIDTH_GRIDDATA_PIXEL = 750;

    private LabelledText queryConditionText = null;

    private String defaultQueryString = ""; // 'name == talend' //$NON-NLS-1$

    private Button previewButton = null;

    private Label previewInformationLabel = null;

    private ShadowProcessPreview salesforcePreviewProcess = null;

    private boolean readOnly;

    private Button alphabet;

    private UtilsButton cancelButton;

    private TableViewer moduleViewer = null;

    private static final int COLUMN_WIDTH = 60;

    private SWTUIThreadProcessor processor = new PreviewProcessor();

    /**
     * Output tab.
     */
    private CTabFolder tabFolder;

    private CTabItem previewTabItem;

    private CTabItem outputTabItem;

    private Composite outputComposite;

    private SalesforceModuleParseAPI salesforceAPI = null;

    private IMetadataTable metadataTable;

    private SalesforceSchemaConnection temConnection;

    private String moduleName;

    /**
     * DOC YeXiaowei SalesforceStep2Form constructor comment.
     * 
     * @param parent
     * @param connectionItem
     */
    public SalesforceStep2Form(Composite parent, ConnectionItem connectionItem, SalesforceSchemaConnection temConnection,
            SalesforceModuleParseAPI salesforceAPI, IMetadataContextModeManager contextModeManager, String moduleName) {
        super(parent, connectionItem, salesforceAPI);
        setConnectionItem(connectionItem);
        setContextModeManager(contextModeManager);
        setupForm(true);
        this.salesforceAPI = salesforceAPI;
        this.temConnection = temConnection;
        this.moduleName = moduleName;
        initTreeNavigatorNodes();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#adaptFormToReadOnly()
     */
    @Override
    protected void adaptFormToReadOnly() {
        readOnly = isReadOnly();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFields()
     */
    @Override
    protected void addFields() {
        int leftCompositeWidth = 125;
        int rightCompositeWidth = WIDTH_GRIDDATA_PIXEL - leftCompositeWidth;
        int headerCompositeHeight = 80;
        int tableSettingsCompositeHeight = 15;
        int tableCompositeHeight = 200;

        int height = headerCompositeHeight + tableSettingsCompositeHeight + tableCompositeHeight;
        Composite mainComposite = Form.startNewDimensionnedGridLayout(this, 2, WIDTH_GRIDDATA_PIXEL, height);
        mainComposite.setLayout(new GridLayout(2, false));
        GridData gridData = new GridData(GridData.FILL_BOTH);
        mainComposite.setLayoutData(gridData);

        SashForm sash = new SashForm(mainComposite, SWT.HORIZONTAL);
        GridData sashData = new GridData(GridData.FILL_BOTH);
        sash.setLayoutData(sashData);
        Composite leftComposite = Form.startNewDimensionnedGridLayout(sash, 1, leftCompositeWidth, height);
        Composite rightComposite = Form.startNewDimensionnedGridLayout(sash, 1, rightCompositeWidth, height);
        sash.setWeights(new int[] { 1, 5 });

        Composite composite1 = Form.startNewDimensionnedGridLayout(rightComposite, 1, rightCompositeWidth, headerCompositeHeight);
        addTreeNavigator(leftComposite, leftCompositeWidth, height);
        addQueryConditionGroup(composite1, tableCompositeHeight);
        addSalesforcePreviewGroup(composite1);

        if (!isInWizard()) {
            Composite compositeBottomButton = Form.startNewGridLayout(this, 2, false, SWT.CENTER, SWT.CENTER);
            cancelButton = new UtilsButton(compositeBottomButton, Messages.getString("CommonWizard.cancel"), WIDTH_BUTTON_PIXEL, //$NON-NLS-1$
                    HEIGHT_BUTTON_PIXEL);
        }
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
        Group group = Form.createGroup(parent, 1, Messages.getString("SalesforceStep2Form.module"), height); //$NON-NLS-1$

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
    }

    /**
     * DOC YeXiaowei Comment method "addSalesforcePreviewGroup".
     */
    private void addSalesforcePreviewGroup(Composite mainComposite) {
        // Group previewGroup = Form.createGroup(this, 2, "Salesforce Preview");

        tabFolder = new CTabFolder(mainComposite, SWT.BORDER);
        tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));

        previewTabItem = new CTabItem(tabFolder, SWT.BORDER);
        previewTabItem.setText(Messages.getString("SalesforceStep2Form.preview")); //$NON-NLS-1$
        outputTabItem = new CTabItem(tabFolder, SWT.BORDER);
        outputTabItem.setText(Messages.getString("SalesforceStep2Form.output")); //$NON-NLS-1$

        Composite previewComposite = Form.startNewGridLayout(tabFolder, 1);
        outputComposite = Form.startNewGridLayout(tabFolder, 1);

        previewButton = new Button(previewComposite, SWT.NONE);
        previewButton.setText(Messages.getString("FileStep2.refreshPreview")); //$NON-NLS-1$
        previewButton.setSize(WIDTH_BUTTON_PIXEL, HEIGHT_BUTTON_PIXEL);

        previewInformationLabel = new Label(previewComposite, SWT.NONE);
        previewInformationLabel
                .setText("                                                                                                                        "); //$NON-NLS-1$
        previewInformationLabel.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLUE));

        salesforcePreviewProcess = new ShadowProcessPreview(previewComposite, null, 600, 200);
        salesforcePreviewProcess.newTablePreview();

        previewTabItem.setControl(previewComposite);
        outputTabItem.setControl(outputComposite);
        tabFolder.setSelection(previewTabItem);
        tabFolder.pack();

    }

    /**
     * DOC YeXiaowei Comment method "addQueryConditionGroup".
     */
    private void addQueryConditionGroup(Composite parent, int tableCompositeHeight) {
        Group queryConditionGroup = Form.createGroup(parent, 2,
                Messages.getString("SalesforceStep2Form.queryCondition"), tableCompositeHeight); //$NON-NLS-1$

        queryConditionText = new LabelledText(queryConditionGroup, "Query Condition", true); //$NON-NLS-1$
        queryConditionText.setText(defaultQueryString);

        Composite moduleViewerComposite = new Composite(queryConditionGroup, SWT.NONE);

        GridData data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 2;
        moduleViewerComposite.setLayoutData(data);

        moduleViewerComposite.setLayout(new GridLayout(2, true));

        Label label = new Label(moduleViewerComposite, SWT.NONE);
        label.setText(Messages.getString("SalesforceStep2Form.saleforceDetail")); //$NON-NLS-1$
        label.setLayoutData(new GridData(GridData.FILL | GridData.BEGINNING));

        alphabet = new Button(moduleViewerComposite, SWT.CHECK);
        alphabet.setText(Messages.getString("SalesforceStep2Form.orderTheFields")); //$NON-NLS-1$
        alphabet.setLayoutData(new GridData(GridData.CENTER));

        createModuleDetailViewer(moduleViewerComposite);

    }

    /**
     * DOC YeXiaowei Comment method "readAndSetModuleDetailContent".
     */
    private void readAndSetModuleDetailContent() {
        org.talend.core.model.metadata.builder.connection.MetadataTable table = getTableByLabel(moduleName);
        if (table != null) {
            IMetadataTable metadataTable = ConvertionHelper.convert(table);

            if (metadataTable != null) {
                if (useAlphbet) {
                    List<IMetadataColumn> listColumns = metadataTable.getListColumns();
                    if (listColumns != null) {
                        moduleViewer.setInput(listColumns.toArray());
                    }
                } else {
                    List<IMetadataColumn> listColumns = metadataTable.getListColumns();
                    if (listColumns != null) {

                        moduleViewer.setInput(listColumns.toArray());
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFieldsListeners()
     */
    @Override
    protected org.talend.core.model.metadata.builder.connection.MetadataTable getTableByLabel(String label) {
        org.talend.core.model.metadata.builder.connection.MetadataTable result = null;
        EList<SalesforceModuleUnit> modules = null;
        if (temConnection == null) {
            modules = getConnection().getModules();
        } else {
            modules = temConnection.getModules();
        }
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

    @Override
    protected void addFieldsListeners() {
        // Navigation : when the user select a table
        tableNavigator.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                String schemaLabel = tableNavigator.getSelection()[0].getText();
                temConnection.setModuleName(schemaLabel);
                getConnection().setModuleName(schemaLabel);
                org.talend.core.model.metadata.builder.connection.MetadataTable table = getTableByLabel(schemaLabel);
                metadataTable = ConvertionHelper.convert(table);
                List<IMetadataColumn> listColumns = metadataTable.getListColumns();
                if (listColumns != null) {
                    moduleViewer.setInput(listColumns.toArray());
                }
            }
        });
        queryConditionText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    if (checkFieldsValue()) {
                        getConnection().setQueryCondition(queryConditionText.getText());
                    }
                }
            }

        });

        alphabet.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                useAlphbet = alphabet.getSelection();
                getConnection().setUseAlphbet(useAlphbet);
                metadataTableOrder = readMetadataDetail();
                metadataTableClone = metadataTableOrder.clone();
                Object input = moduleViewer.getInput();
                if (input instanceof Object[]) {
                    if (useAlphbet) {
                        modifyMetadataTable();
                        List<IMetadataColumn> listColumns = metadataTableOrder.getListColumns();
                        if (listColumns != null) {
                            moduleViewer.setInput(listColumns.toArray());
                        }
                    } else {
                        List<IMetadataColumn> listColumns = metadataTableClone.getListColumns();
                        if (listColumns != null) {
                            moduleViewer.setInput(listColumns.toArray());
                        }
                    }

                }
                moduleViewer.refresh();
            }

        });

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addUtilsButtonListeners()
     */
    @Override
    protected void addUtilsButtonListeners() {
        // Event PreviewButton
        previewButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                processor.execute();
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#checkFieldsValue()
     */
    @Override
    protected boolean checkFieldsValue() {
        previewInformationLabel.setText("   " + Messages.getString("FileStep2.settingsIncomplete")); //$NON-NLS-1$ //$NON-NLS-2$
        updateStatus(IStatus.OK, null);
        previewButton.setEnabled(false);
        previewInformationLabel.setText(""); //$NON-NLS-1$
        previewButton.setEnabled(true);

        updateStatus(IStatus.OK, null);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#initialize()
     */
    @Override
    protected void initialize() {

        String queryCondition = getConnection().getQueryCondition();
        if (queryCondition != null && !queryCondition.equals("")) { //$NON-NLS-1$
            queryConditionText.setText(queryCondition);
        } else {
            queryConditionText.setText(""); //$NON-NLS-1$
        }
        useAlphbet = getConnection().isUseAlphbet();
        alphabet.setSelection(useAlphbet);

        checkFieldsValue();

    }

    @Override
    public void setVisible(boolean visible) {

        super.setVisible(visible);

        if (super.isVisible()) {
            if (!isContextMode()) {
                if ((!"".equals(temConnection.getWebServiceUrl())) && (temConnection.getModuleName() != null)) { //$NON-NLS-1$
                    refreshPreview();
                }
            }

            if (isReadOnly() != readOnly) {
                adaptFormToReadOnly();
            }
            if (isContextMode()) {
                adaptFormToEditable();
            }
        }
    }

    /**
     * DOC YeXiaowei Comment method "refreshPreview".
     */
    private void refreshPreview() {
        initTreeNavigatorNodes();
        // processor.execute();
    }

    /**
     * 
     * DOC YeXiaowei SalesforceStep2Form class global comment. Detailled comment <br/>
     * 
     */
    class PreviewProcessor extends SWTUIThreadProcessor {

        String previewInformationLabelMsg = null;

        CsvArray csvArray = null;

        ProcessDescription processDescription = null;

        boolean firstRowIsCatption = false;

        @Override
        public boolean preProcessStart() {
            previewButton.setText(Messages.getString("FileStep2.stop")); //$NON-NLS-1$

            clearPreview();
            String webServiceUrl = getConnection().getWebServiceUrl();
            SalesforceSchemaConnection originalValueConnection = null;
            if (isContextMode()) {
                boolean found = false;
                ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(getShell(), getConnection());
                if (contextType != null) {
                    if (getContextModeManager() != null) {
                        getContextModeManager().setSelectedContextType(contextType);
                        webServiceUrl = getContextModeManager().getOriginalValue(getConnection().getWebServiceUrl());
                        found = true;
                    }
                    originalValueConnection = OtherConnectionContextUtils.cloneOriginalValueSalesforceConnection(getConnection(),
                            contextType);
                }
                if (!found) {
                    webServiceUrl = null;
                }
            }

            if (webServiceUrl == null || webServiceUrl.equals("")) { //$NON-NLS-1$
                previewInformationLabel.setText(" Please reset Salesforce URL"); //$NON-NLS-1$ 
                return false;
            }

            if (!checkFieldsValue()) {
                previewInformationLabel.setText("   " + Messages.getString("FileStep2.settingsIncomplete")); //$NON-NLS-1$ //$NON-NLS-2$
                return false;
            }

            previewInformationLabel.setText("   " + Messages.getString("FileStep2.previewProgress")); //$NON-NLS-1$ //$NON-NLS-2$
            firstRowIsCatption = false;
            if (originalValueConnection == null) {
                originalValueConnection = getConnection();
            }
            processDescription = getProcessDescription(originalValueConnection);
            updateStatus(IStatus.ERROR, null);
            return true;
        }

        @Override
        public void nonUIProcessInThread() {
            // get the XmlArray width an adapt ProcessDescription
            try {
                List<IMetadataTable> schema = processDescription.getSchema();
                if (schema != null && schema.size() > 0) {
                    if (useAlphbet) {
                        if (metadataTableOrder == null) {
                            metadataTableOrder = schema.get(0);
                            metadataTableOrder = modifyMetadataTable();
                        }
                        if (metadataTableOrder != null) {
                            schema.get(0).setListColumns(metadataTableOrder.getListColumns());
                        }
                    } else {
                        if (metadataTableClone == null) {
                            metadataTableClone = schema.get(0);
                        }
                        if (metadataTableClone != null) {
                            schema.get(0).setListColumns(metadataTableClone.getListColumns());
                        }
                    }
                }

                csvArray = ShadowProcessHelper.getCsvArray(processDescription, "SALESFORCE_SCHEMA", true); //$NON-NLS-1$
                if (csvArray == null) {
                    previewInformationLabelMsg = "   " + Messages.getString("FileStep2.previewFailure"); //$NON-NLS-1$ //$NON-NLS-2$
                } else {
                    previewInformationLabelMsg = "   " + Messages.getString("FileStep2.previewIsDone"); //$NON-NLS-1$ //$NON-NLS-2$

                    // refresh TablePreview on this step
                    previewInformationLabelMsg = ""; //$NON-NLS-1$
                }
            } catch (Exception ex) {
                setException(ex);
                previewInformationLabelMsg = "   " + Messages.getString("FileStep2.previewFailure"); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }

        @Override
        public void updateUIInThreadIfThreadIsCanceled() {
            if (!previewInformationLabel.isDisposed()) {
                previewInformationLabel.setText(""); //$NON-NLS-1$
            }
        }

        @Override
        public void updateUIInThreadIfThreadIsNotCanceled() {
            if (previewInformationLabel.isDisposed()) {
                return;
            }
            previewInformationLabel.setText(previewInformationLabelMsg);
            if (getException() != null) {

                // new ErrorDialogWidthDetailArea(getShell(), PID,
                // Messages.getString("FileStep2.previewFailure"), getException().getMessage()); //$NON-NLS-1$
                previewInformationLabel.setText("   " + Messages.getString("FileStep2.previewFailure")); //$NON-NLS-1$ //$NON-NLS-2$
                Display.getDefault().asyncExec(new Runnable() {

                    @Override
                    public void run() {
                        handleErrorOutput(outputComposite, tabFolder, outputTabItem);
                    }
                });
                return;

            }
            if (csvArray != null) {
                salesforcePreviewProcess.refreshTablePreview(csvArray, firstRowIsCatption, processDescription);
            }
        }

        @Override
        public void updateUIInThreadIfThreadFinally() {
            checkFieldsValue();
            if (!previewButton.isDisposed()) {
                previewButton.setText(Messages.getString("FileStep2.refreshPreview")); //$NON-NLS-1$
                previewButton.setEnabled(true);

            }
        }

        @Override
        public void postProcessCancle() {
            checkFieldsValue();
            previewButton.setEnabled(false);
        }
    }

    void clearPreview() {
        salesforcePreviewProcess.clearTablePreview();
    }

    /**
     * DOC YeXiaowei Comment method "getProcessDescription".
     * 
     * @return
     */
    private ProcessDescription getProcessDescription(SalesforceSchemaConnection originalValueConnection) {

        ProcessDescription processDescription = ShadowProcessHelper.getProcessDescription(originalValueConnection);

        SalesforceSchemaBean bean = new SalesforceSchemaBean();
        bean.setWebServerUrl(originalValueConnection.getWebServiceUrl());
        bean.setUserName(originalValueConnection.getUserName());
        bean.setPassword(originalValueConnection.getValue(originalValueConnection.getPassword(), false));
        bean.setModuleName(originalValueConnection.getModuleName());
        bean.setQueryCondition(originalValueConnection.getQueryCondition());
        bean.setUseCustomModule(originalValueConnection.isUseCustomModuleName());
        bean.setBatchSize(originalValueConnection.getBatchSize());
        bean.setUseProxy(originalValueConnection.isUseProxy());
        bean.setUesHttp(originalValueConnection.isUseHttpProxy());
        bean.setProxyHost(originalValueConnection.getProxyHost());
        bean.setProxyPort(originalValueConnection.getProxyPort());
        bean.setProxyUsername(originalValueConnection.getProxyUsername());
        bean.setProxyPassword(originalValueConnection.getValue(originalValueConnection.getProxyPassword(), false));
        try {
            bean.setTimeOut(Integer.parseInt(originalValueConnection.getTimeOut()));
        } catch (NumberFormatException e) {
            // use default
        }
        processDescription.setSalesforceSchemaBean(bean);

        IMetadataTable tableGet = getMetadatasForSalesforce(bean.getWebServerUrl(), bean.getUserName(), bean.getPassword(),
                String.valueOf(bean.getTimeOut()), bean.getModuleName(), bean.getBatchSize(), bean.isUseProxy(),
                bean.isUesHttp(), bean.getProxyHost(), bean.getProxyPort(), bean.getProxyUsername(), bean.getProxyPassword(),
                false);

        List<IMetadataTable> tableSchema = new ArrayList<IMetadataTable>();
        IMetadataTable table = new MetadataTable();
        List<IMetadataColumn> schema = new ArrayList<IMetadataColumn>();

        for (IMetadataColumn column : tableGet.getListColumns()) {
            schema.add(column.clone());
        }

        table.setTableName("tSalesforceInput"); //$NON-NLS-1$
        table.setListColumns(schema);
        tableSchema.add(table);

        processDescription.setSchema(tableSchema);

        processDescription.setEncoding(TalendQuoteUtils.addQuotes("ISO-8859-15")); //$NON-NLS-1$  
        if (tableGet != null) {
            moduleViewer.getTable().clearAll();
            if (useAlphbet) {
                if (metadataTableOrder == null) {
                    List<IMetadataTable> schema2 = processDescription.getSchema();
                    if (schema2 != null && schema2.size() > 0) {
                        metadataTableOrder = schema2.get(0);
                        metadataTableOrder = modifyMetadataTable();
                    }
                }
                if (metadataTableOrder != null) {
                    tableGet.setListColumns(metadataTableOrder.getListColumns());
                }
            } else {
                if (metadataTableClone == null) {
                    List<IMetadataTable> schema2 = processDescription.getSchema();
                    if (schema2 != null && schema2.size() > 0) {
                        metadataTableClone = schema2.get(0);
                    }
                }
                if (metadataTableClone != null) {
                    tableGet.setListColumns(metadataTableClone.getListColumns());
                }
            }
            moduleViewer.setInput(tableGet.getListColumns().toArray());
            moduleViewer.refresh();
        }

        return processDescription;
    }

    /**
     * DOC YeXiaowei Comment method "createModuleDetailViewer".
     * 
     * @param moduleGroup
     */
    private void createModuleDetailViewer(Composite moduleGroup) {
        moduleViewer = new TableViewer(moduleGroup, SWT.FILL | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION);

        moduleViewer.getTable().setHeaderVisible(true);
        moduleViewer.getTable().setLinesVisible(true);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.horizontalSpan = 2;
        moduleViewer.getTable().setLayoutData(gridData);

        moduleViewer.setContentProvider(new IStructuredContentProvider() {

            @Override
            public void dispose() {

            }

            @Override
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

            }

            @Override
            public Object[] getElements(Object inputElement) {
                if (inputElement instanceof Object[]) {
                    return (Object[]) inputElement;
                }
                return null;
            }

        });

        moduleViewer.setLabelProvider(new ITableLabelProvider() {

            @Override
            public Image getColumnImage(Object element, int columnIndex) {
                return null;
            }

            @Override
            public String getColumnText(Object element, int columnIndex) {
                if (element instanceof IMetadataColumn) {
                    IMetadataColumn metadataColumn = (IMetadataColumn) element;
                    String title = null;
                    switch (columnIndex) {
                    case 0:
                        title = metadataColumn.getLabel();
                        break;
                    case 1:
                        title = metadataColumn.isKey() ? "true" : "false"; //$NON-NLS-1$ //$NON-NLS-2$
                        break;
                    case 2:
                        String talendType = metadataColumn.getTalendType();
                        JavaType javaTypeFromId = JavaTypesManager.getJavaTypeFromId(talendType);
                        if (javaTypeFromId != null) {
                            title = javaTypeFromId.getLabel();
                        }
                        break;
                    case 3:
                        title = metadataColumn.isNullable() ? "true" : "false"; //$NON-NLS-1$ //$NON-NLS-2$
                        break;
                    case 4:
                        title = metadataColumn.getPattern();
                        break;
                    case 5:
                        title = getStringFromInt(metadataColumn.getLength());
                        break;
                    case 6:
                        title = getStringFromInt(metadataColumn.getPrecision());
                        break;
                    case 7:
                        title = metadataColumn.getDefault();
                        break;
                    case 8:
                        title = metadataColumn.getComment();
                        break;
                    default:
                        title = Messages.getString("SalesforceStep2Form.otherTitle"); //$NON-NLS-1$
                    }

                    return title;
                }
                return null;
            }

            private String getStringFromInt(int x) {
                try {
                    return Integer.toString(x);
                } catch (Error e) {
                    return ""; //$NON-NLS-1$
                }
            }

            @Override
            public void addListener(ILabelProviderListener listener) {

            }

            @Override
            public void dispose() {

            }

            @Override
            public boolean isLabelProperty(Object element, String property) {
                return false;
            }

            @Override
            public void removeListener(ILabelProviderListener listener) {

            }

        });

        String[] titles = new String[] { "Column", "Key", "Type", "Nullable", "Data Pattern", "Length", "Precision", "Default", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
                "Comment" }; //$NON-NLS-1$

        for (String title : titles) {
            int width = COLUMN_WIDTH;
            if (title.equals("Column") || title.equals("Data Pattern") || title.equals("Comment")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                width = COLUMN_WIDTH * 2;
            }
            createTableColumn(title, width);
        }

    }

    private void createTableColumn(String title, int width) {
        TableColumn column = new TableColumn(moduleViewer.getTable(), SWT.NONE);
        column.setText(title);
        if (width < COLUMN_WIDTH || width > 400) {
            column.setWidth(COLUMN_WIDTH);
        } else {
            column.setWidth(width);
        }
    }

    @Override
    protected void adaptFormToEditable() {
        super.adaptFormToEditable();
        queryConditionText.setEditable(!isContextMode());
    }

    @Override
    protected void exportAsContext() {
        super.exportAsContext();
        if (getContextModeManager() != null) {
            getContextModeManager().setDefaultContextType(getConnection());
        }
    }

    @Override
    protected void processWhenDispose() {
        if (processor != null) {
            processor.forceStop();
        }
    }

    private void initTreeNavigatorNodes() {
        List<String> selectedNames = new ArrayList<String>();
        EList<SalesforceModuleUnit> modules = temConnection.getModules();
        // EList<SalesforceModuleUnit> modules = getConnection().getModules();
        selectedNames.add(moduleName);
        // for (int i = 0; i < modules.size(); i++) {
        // if (modules.get(i).getModuleName().equals(moduleName)) {
        //
        // for (int j = 0; j < modules.get(i).getTables().size(); j++) {
        // selectedNames.add(modules.get(i).getTables().get(j).getLabel());
        // }
        // break;
        // }
        // }
        tableNavigator.removeAll();
        if (selectedNames != null && selectedNames.size() >= 1) {
            for (int i = 0; i < selectedNames.size(); i++) {
                TableItem subItem = new TableItem(tableNavigator, SWT.NULL);
                subItem.setText(selectedNames.get(i));
                tableNavigator.setSelection(subItem);
            }
        } else {
            TableItem subItem = new TableItem(tableNavigator, SWT.NULL);
            subItem.setText(getConnection().getModuleName());
        }
        readAndSetModuleDetailContent();
    }
}
