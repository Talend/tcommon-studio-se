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
package org.talend.repository.ui.wizards.metadata.connection.files.salesforce;

import java.lang.reflect.InvocationTargetException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.SearchPattern;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.exception.ExceptionMessageDialog;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorNotModifiable.LAYOUT_MODE;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.utils.data.text.IndiceHelper;
import org.talend.commons.utils.threading.TalendCustomThreadPoolExecutor;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataToolHelper;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.SalesforceModuleUnit;
import org.talend.core.model.metadata.builder.connection.SalesforceSchemaConnection;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase.ETableTypes;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.metadata.builder.database.TableInfoParameters;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.ui.metadata.editor.MetadataEmfTableEditor;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.TableHelper;
import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.TdColumn;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.metadata.managment.repository.ManagerConnection;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;
import org.talend.metadata.managment.ui.utils.OtherConnectionContextUtils;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.metadata.ui.wizards.form.AbstractSalesforceStepForm;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.salesforce.SforceConnection;
import org.talend.salesforce.SforceManagementImpl;
import org.talend.salesforce.SforceSessionConnection;
import org.talend.salesforce.oauth.OAuthClient;
import org.talend.salesforce.oauth.Token;

import orgomg.cwm.objectmodel.core.CoreFactory;

import com.salesforce.soap.partner.DescribeGlobalSObjectResult;
import com.sforce.soap.enterprise.DescribeGlobalResult;

/**
 * @author cantoine
 * 
 */
public class SelectorModulesForm extends AbstractSalesforceStepForm {

    private String endPoint;

    private String username;

    private String pwd;

    private String batchSize;

    private String timeOut;

    private String proxyHost = null;

    private String proxyPort = null;

    private String proxyUsername = null;

    private String proxyPassword = null;

    private boolean useHttpProxy = false;

    private boolean useHttpsProxy = false;

    private boolean useSocketProxy = false;

    private boolean httpsProxy = false;

    private String endPointForAuth;

    private String consumerKey;

    private String consumeSecret;

    private String callbackHost;

    private String callbackPort;

    private String salesforceVersion;

    private String tokenProperties;

    private String loginType;

    private final static String BASIC = "basic";

    // private String

    /**
     * FormTable Settings.
     */
    private static final int WIDTH_GRIDDATA_PIXEL = 700;

    /**
     * FormTable Var.
     */
    private final ManagerConnection managerConnection;

    private List<String> itemTableName;

    private IMetadataConnection iMetadataConnection = null;

    private MetadataTable dbtable;

    private MetadataEmfTableEditor metadataEditor;

    private UtilsButton selectAllTablesButton;

    private UtilsButton selectNoneTablesButton;

    private UtilsButton checkConnectionButton;

    /**
     * Anothers Fields.
     */
    private final ConnectionItem connectionItem;

    private ConnectionItem templateConntion;

    // private DatabaseConnection connection;
    protected Table table;

    private int count = 0;

    private Integer countSuccess = 0;

    private Integer countPending = 0;

    private final WizardPage parentWizardPage;

    CustomThreadPoolExecutor threadExecutor;

    ScrolledComposite scrolledCompositeFileViewer;

    private Text nameFilter;

    private final TableInfoParameters tableInfoParameters;

    private boolean forTemplate = false;

    // store column number for each table name
    private Map<String, Integer> tableColumnNums = new HashMap<String, Integer>();

    private SalesforceSchemaConnection temConnection;

    private SalesforceSchemaConnection oldTemConnection;

    SalesforceModuleParseAPI salesforceAPI;

    /**
     * TableForm Constructor to use by RCP Wizard.
     * 
     * @param parent
     * @param page
     * @param connection
     * @param page
     * @param metadataTable
     */
    public SelectorModulesForm(Composite parent, ConnectionItem connectionItem, SelectorModulesWizardPage page,
            SalesforceModuleParseAPI salesforceAPI) {

        super(parent, connectionItem, salesforceAPI);
        managerConnection = new ManagerConnection();
        this.connectionItem = connectionItem;
        this.parentWizardPage = page;
        this.tableInfoParameters = page.getTableInfoParameters();
        setupForm();
    }

    public SelectorModulesForm(Composite parent, ConnectionItem connectionItem, SelectorModulesWizardPage page,
            boolean forTemplate, SalesforceSchemaConnection temConnection, SalesforceModuleParseAPI salesforceAPI) {
        super(parent, connectionItem, salesforceAPI);
        managerConnection = new ManagerConnection();
        this.connectionItem = connectionItem;
        this.templateConntion = connectionItem;
        this.parentWizardPage = page;
        this.tableInfoParameters = page.getTableInfoParameters();
        this.forTemplate = forTemplate;
        this.temConnection = temConnection;
        if (forTemplate && ConnectionHelper.getTables(getConnection()).size() <= 0) {
            page.setPageComplete(false);
        }
        setupForm();
        this.salesforceAPI = salesforceAPI;
    }

    /**
     * 
     * Initialize value, forceFocus first field for right Click (new Table).
     * 
     */
    @Override
    public void initialize() {
    }

    public void initializeForm() {
        initExistingNames();
        selectAllTablesButton.setEnabled(true);
        count = 0;
    }

    @Override
    protected void addFields() {
        int leftCompositeWidth = 80;
        int rightCompositeWidth = WIDTH_GRIDDATA_PIXEL - leftCompositeWidth;
        int headerCompositeHeight = 60;
        int tableSettingsCompositeHeight = 90;
        int tableCompositeHeight = 200;

        int height = headerCompositeHeight + tableSettingsCompositeHeight + tableCompositeHeight;

        // Main Composite : 2 columns
        Composite mainComposite = Form.startNewDimensionnedGridLayout(this, 1, leftCompositeWidth + rightCompositeWidth, height);
        mainComposite.setLayout(new GridLayout(1, false));
        GridData gridData = new GridData(GridData.FILL_BOTH);
        mainComposite.setLayoutData(gridData);

        Composite rightComposite = Form.startNewDimensionnedGridLayout(mainComposite, 1, rightCompositeWidth, height);

        // Group Table Settings
        Group groupTableSettings = Form.createGroup(rightComposite, 1,
                Messages.getString("SelectorTableForm.groupTableSettings"), tableSettingsCompositeHeight); //$NON-NLS-1$

        // Composite TableSettings
        Composite compositeTableSettings = Form.startNewDimensionnedGridLayout(groupTableSettings, 1, rightCompositeWidth,
                tableSettingsCompositeHeight);
        gridData = new GridData(GridData.FILL_BOTH);
        gridData.widthHint = rightCompositeWidth;
        gridData.horizontalSpan = 3;

        Composite filterComposite = new Composite(compositeTableSettings, SWT.NONE);
        GridLayout gridLayout = new GridLayout(2, false);
        filterComposite.setLayout(gridLayout);
        GridData gridData2 = new GridData(GridData.FILL_HORIZONTAL);
        filterComposite.setLayoutData(gridData2);
        Label label = new Label(filterComposite, SWT.NONE);
        label.setText(Messages.getString("SelectorTableForm.nameFilter")); //$NON-NLS-1$
        nameFilter = new Text(filterComposite, SWT.BORDER);
        nameFilter.setToolTipText(Messages.getString("SelectorTableForm.enterType")); //$NON-NLS-1$
        nameFilter.setEditable(true);
        gridData2 = new GridData(GridData.FILL_HORIZONTAL);
        nameFilter.setLayoutData(gridData2);
        scrolledCompositeFileViewer = new ScrolledComposite(compositeTableSettings, SWT.H_SCROLL | SWT.V_SCROLL | SWT.NONE);
        scrolledCompositeFileViewer.setExpandHorizontal(true);
        scrolledCompositeFileViewer.setExpandVertical(true);
        GridData gridData1 = new GridData(GridData.FILL_BOTH);
        int width = 700;
        int hight = 325;
        if (forTemplate) {
            width = 375;
            hight = 300;
        }
        gridData1.widthHint = width;
        gridData1.heightHint = hight;
        gridData1.horizontalSpan = 2;
        scrolledCompositeFileViewer.setLayoutData(gridData1);

        createTable();

        // Composite retreiveSchema
        Composite compositeRetreiveSchemaButton = Form.startNewGridLayout(compositeTableSettings, 3, false, SWT.CENTER,
                SWT.BOTTOM);

        GC gc = new GC(compositeRetreiveSchemaButton);
        // Button Create Table
        String displayStr = Messages.getString("SelectorTableForm.selectAllTables"); //$NON-NLS-1$
        Point buttonSize = gc.stringExtent(displayStr);
        selectAllTablesButton = new UtilsButton(compositeRetreiveSchemaButton, displayStr, buttonSize.x + 12, HEIGHT_BUTTON_PIXEL);

        displayStr = Messages.getString("SelectorTableForm.selectNoneTables"); //$NON-NLS-1$
        buttonSize = gc.stringExtent(displayStr);
        selectNoneTablesButton = new UtilsButton(compositeRetreiveSchemaButton, displayStr, buttonSize.x + 12,
                HEIGHT_BUTTON_PIXEL);

        // Button Check Connection
        displayStr = Messages.getString("DatabaseTableForm.checkConnection"); //$NON-NLS-1$
        buttonSize = gc.stringExtent(displayStr);
        checkConnectionButton = new UtilsButton(compositeRetreiveSchemaButton, displayStr, buttonSize.x + 12, HEIGHT_BUTTON_PIXEL);
        gc.dispose();

        metadataEditor = new MetadataEmfTableEditor(""); //$NON-NLS-1$
        // addUtilsButtonListeners();
    }

    /**
     * DOC qzhang Comment method "createTable".
     */
    private void createTable() {
        // List Table
        TableViewerCreator tableViewerCreator = new TableViewerCreator(scrolledCompositeFileViewer);
        tableViewerCreator.setColumnsResizableByDefault(true);
        tableViewerCreator.setBorderVisible(true);
        tableViewerCreator.setLayoutMode(LAYOUT_MODE.FILL_HORIZONTAL);
        tableViewerCreator.setCheckboxInFirstColumn(true);
        // tableViewerCreator.setAdjustWidthValue(-15);
        tableViewerCreator.setFirstColumnMasked(true);

        int columnWidth1 = 300;
        int columnWidth2 = 140;
        int columnWidth3 = 125;
        int columnWidth4 = 140;

        table = tableViewerCreator.createTable();
        if (forTemplate) {
            columnWidth1 = 150;
            columnWidth2 = 100;
            columnWidth3 = 100;
            columnWidth4 = 110;
        }
        table.setLayoutData(new GridData(GridData.FILL_BOTH));

        // table = new Table(scrolledCompositeFileViewer, SWT.CHECK | SWT.BORDER);
        // table.setBackground(table.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
        // table.setHeaderVisible(true);

        // table.setHeaderVisible(true);
        TableColumn tableName = new TableColumn(table, SWT.NONE);
        tableName.setText(Messages.getString("SelectorTableForm.TableName")); //$NON-NLS-1$
        tableName.setWidth(columnWidth1);

        // tableName.addSelectionListener(getColumnSelectionListener());
        TableColumn tableType = new TableColumn(table, SWT.NONE);
        tableType.setText(Messages.getString("SelectorTableForm.TableType")); //$NON-NLS-1$
        tableType.setWidth(columnWidth2);

        // tableType.addSelectionListener(getColumnSelectionListener());
        TableColumn nbColumns = new TableColumn(table, SWT.RIGHT);
        nbColumns.setText(Messages.getString("SelectorTableForm.ColumnNumber")); //$NON-NLS-1$
        nbColumns.setWidth(columnWidth3);

        TableColumn creationStatus = new TableColumn(table, SWT.RIGHT);
        creationStatus.setText(Messages.getString("SelectorTableForm.CreationStatus")); //$NON-NLS-1$
        creationStatus.setWidth(columnWidth4);

        scrolledCompositeFileViewer.setContent(table);
        scrolledCompositeFileViewer.setMinSize(table.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }

    private final Collator col = Collator.getInstance(Locale.getDefault());

    /**
     * DOC qzhang Comment method "getColumnSelectionListener".
     * 
     * @return
     */
    private SelectionAdapter getColumnSelectionListener() {
        return new SelectionAdapter() {

            int colIndex = 1;

            int updown = 1;

            private final Comparator strComparator = new Comparator() {

                @Override
                public int compare(Object arg0, Object arg1) {

                    TableItem t1 = (TableItem) arg0;
                    TableItem t2 = (TableItem) arg1;

                    String v1 = (t1.getText(colIndex));
                    String v2 = (t2.getText(colIndex));

                    return (col.compare(v1, v2)) * updown;
                }
            };

            @Override
            public void widgetSelected(SelectionEvent e) {
                updown = updown * -1;

                TableColumn currentColumn = (TableColumn) e.widget;
                Table selectiontable = currentColumn.getParent();

                colIndex = searchColumnIndex(currentColumn);

                selectiontable.setRedraw(false);

                TableItem[] items = table.getItems();

                Arrays.sort(items, strComparator);

                selectiontable.setItemCount(items.length);

                for (int i = 0; i < items.length; i++) {
                    TableItem item = new TableItem(table, SWT.NONE, i);
                    item.setText(getData(items[i]));
                    if (items[i].getChecked()) {
                        clearTableItem(items[i]);
                        items[i].setChecked(false);
                        item.setChecked(true);
                        createTable(item);
                    }
                    items[i].dispose();
                }

                selectiontable.setRedraw(true);
                selectiontable.getParent().layout(true, true);
            }

            private String[] getData(TableItem t) {
                Table selectiontable = t.getParent();

                int colCount = selectiontable.getColumnCount();
                String[] s = new String[colCount];

                for (int i = 0; i < colCount; i++) {
                    s[i] = t.getText(i);
                }
                return s;

            }

            private int searchColumnIndex(TableColumn currentColumn) {
                Table selectiontable = currentColumn.getParent();

                int in = 0;

                for (int i = 0; i < selectiontable.getColumnCount(); i++) {
                    if (selectiontable.getColumn(i) == currentColumn) {
                        in = i;
                        break;
                    }
                }
                return in;
            }
        };
    }

    /**
     * addButtonControls.
     * 
     */
    @Override
    protected void addUtilsButtonListeners() {
        // Event CheckConnection Button
        checkConnectionButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                count = 0;
                checkConnection(true);
            }
        });

        selectAllTablesButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                updateStatus(IStatus.ERROR, null);
                TableItem[] tableItems = table.getItems();
                int size = tableItems.length;
                for (TableItem tableItem2 : tableItems) {
                    TableItem tableItem = tableItem2;
                    if (!tableItem.getChecked()) {
                        tableItem.setText(3, Messages.getString("SelectorTableForm.Pending")); //$NON-NLS-1$
                        synchronized (countPending) {
                            countPending++;
                        }
                        parentWizardPage.setPageComplete(false);
                        refreshTable(tableItem, size);
                    } else {
                        updateStatus(IStatus.OK, null);
                    }
                    tableItem.setChecked(true);
                }
                if (forTemplate) {
                    parentWizardPage.setPageComplete(true);
                }
            }
        });

        selectNoneTablesButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                count = 0;
                countSuccess = 0;
                countPending = 0;
                TableItem[] tableItems = table.getItems();
                for (TableItem tableItem : tableItems) {
                    if (tableItem.getChecked()) {
                        clearTableItem(tableItem);
                        tableItem.setChecked(false);
                    }
                }
                if (forTemplate) {
                    parentWizardPage.setPageComplete(false);
                }
            }

        });

        addTableListener();
    }

    /**
     * DOC qzhang Comment method "addTableListener".
     */
    private void addTableListener() {
        // Event checkBox action
        table.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (e.detail == SWT.CHECK) {
                    TableItem tableItem = (TableItem) e.item;
                    boolean promptNeeded = tableItem.getChecked();
                    if (promptNeeded) {
                        clearTableItem(tableItem);
                        tableItem.setText(2, ""); //$NON-NLS-1$
                        tableItem.setText(3, Messages.getString("SelectorTableForm.Pending")); //$NON-NLS-1$
                        synchronized (countPending) {
                            countPending++;
                        }
                        parentWizardPage.setPageComplete(false);
                        refreshTable(tableItem, -1);
                    } else {
                        String pending = tableItem.getText(3);
                        clearTableItem(tableItem);
                        if (pending != null && pending.equals(Messages.getString("SelectorTableForm.Pending"))) { //$NON-NLS-1$
                            synchronized (countPending) {
                                countPending--;
                            }
                        }
                    }
                    if (forTemplate && (ConnectionHelper.getTables(getConnection()).size() <= 0)) {
                        parentWizardPage.setPageComplete(false);
                    }
                }
            }
        });
    }

    private SalesforceSchemaConnection getOriginalValueConnection() {
        if (isContextMode()) {
            ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(null, getConnection(), null, false);
            return OtherConnectionContextUtils.cloneOriginalValueSalesforceConnection(getConnection(), contextType);
        }
        return getConnection();

    }

    /**
     * checkConnection.
     * 
     * @param displayMessageBox
     */
    protected void checkConnection(final boolean displayMessageBox) {
        try {
            if (table.getItemCount() > 0) {
                table.removeAll();
            }
            parentWizardPage.getWizard().getContainer().run(true, true, new IRunnableWithProgress() {

                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    monitor.beginTask(Messages.getString("CreateTableAction.action.createTitle"), IProgressMonitor.UNKNOWN); //$NON-NLS-1$

                    managerConnection.check(getIMetadataConnection());

                    String proxy = null;
                    oldTemConnection = getOriginalValueConnection();
                    if (oldTemConnection.isUseProxy()) {
                        proxy = SalesforceModuleParseAPI.USE_SOCKS_PROXY;
                    } else if (oldTemConnection.isUseHttpProxy()) {
                        proxy = SalesforceModuleParseAPI.USE_HTTP_PROXY;
                    }
                    try {
                        itemTableName = connectFromCustomModuleName(proxy);

                        if (itemTableName == null || itemTableName.size() <= 0) {
                            // connection is done but any table exist
                            if (displayMessageBox) {
                                openInfoDialogInUIThread(null, Messages.getString("DatabaseTableForm.checkConnection"), Messages //$NON-NLS-1$
                                        .getString("DatabaseTableForm.tableNoExist"), true);//$NON-NLS-1$
                            }
                        } else {
                            createAllItems(displayMessageBox, null);
                        }
                    } catch (final Exception ex) {
                        Display.getDefault().asyncExec(new Runnable() {

                            @Override
                            public void run() {
                                ExceptionMessageDialog.openError(
                                        getShell(),
                                        Messages.getString("SeletorModuleForm.connectFromCustomModuleName.errorTitle"), ex.getMessage(), ex); //$NON-NLS-1$
                            }
                        });
                    } finally {
                        monitor.done();
                    }
                }
            });
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }

    }

    /**
     * DOC qzhang Comment method "createAllItems".
     * 
     * @param displayMessageBox
     * @param newList
     */
    private void createAllItems(final boolean displayMessageBox, final List<String> newList) {
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                List<String> list = new ArrayList<String>();
                if (newList != null) {
                    list.addAll(newList);
                } else {
                    list = itemTableName;
                }
                // connection is done and tables exist
                if (list != null && !list.isEmpty()) {
                    // fill the combo
                    Iterator<String> iterate = list.iterator();
                    while (iterate.hasNext()) {
                        String nameTable = iterate.next();
                        String name = "Modules";
                        if (nameTable != null && name != null) {
                            TableItem item = new TableItem(table, SWT.NONE);
                            item.setText(0, nameTable);
                            item.setText(1, name);
                        }
                    }
                }
                restoreCheckItems();
                // if (forTemplate) {
                // if (displayMessageBox) {
                // MessageBox box = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK | SWT.CANCEL);
                //                        box.setText(Messages.getString("DatabaseTableForm.checkConnection")); //$NON-NLS-1$
                //                        box.setMessage("Connection unsuccessfully!"); //$NON-NLS-1$
                // box.open();
                // }
                // } else {
                if (displayMessageBox) {
                    String msg = Messages.getString("DatabaseTableForm.connectionIsDone"); //$NON-NLS-1$
                    openInfoDialogInUIThread(getShell(), Messages.getString("DatabaseTableForm.checkConnection"), msg, false); //$NON-NLS-1$
                }
                // }
            }
        });
    }

    public static void openInfoDialogInUIThread(Shell shell, final String title, final String msg, boolean ifUseRunnable) {
        if (ifUseRunnable) {
            Display.getDefault().asyncExec(new Runnable() {

                @Override
                public void run() {
                    MessageDialog.openInformation(new Shell(), title, msg);
                }
            });
        } else {
            Shell iShell = shell;
            if (iShell == null) {
                iShell = new Shell();
            }
            MessageDialog.openInformation(iShell, title, msg);
        }
    }

    /**
     * createTable.
     * 
     * @param tableItem
     */
    protected void createTable(TableItem tableItem) {
        String tableString = tableItem.getText(0);

        boolean checkConnectionIsDone = managerConnection.check(getIMetadataConnection(), true);
        if (!checkConnectionIsDone) {
            updateStatus(IStatus.WARNING, Messages.getString("DatabaseTableForm.connectionFailure")); //$NON-NLS-1$
            new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("DatabaseTableForm.connectionFailure"), //$NON-NLS-1$
                    managerConnection.getMessageException());
        } else {

            if (ExtractMetaDataFromDataBase.getTableTypeByTableName(tableString).equals(ETableTypes.TABLETYPE_TABLE.getName())) {
                dbtable = RelationalFactory.eINSTANCE.createTdTable();
            } else if (ExtractMetaDataFromDataBase.getTableTypeByTableName(tableString).equals(
                    ETableTypes.TABLETYPE_VIEW.getName())) {
                dbtable = RelationalFactory.eINSTANCE.createTdView();
            } else {
                dbtable = RelationalFactory.eINSTANCE.createTdTable();
            }
            dbtable.getTaggedValue().add(CoreFactory.eINSTANCE.createTaggedValue());
            List<TdColumn> metadataColumns = new ArrayList<TdColumn>();
            metadataColumns = ExtractMetaDataFromDataBase.returnMetadataColumnsFormTable(iMetadataConnection,
                    tableItem.getText(0));

            tableItem.setText(2, "" + metadataColumns.size()); //$NON-NLS-1$
            tableItem.setText(3, Messages.getString("SelectorTableForm.Success")); //$NON-NLS-1$
            synchronized (countSuccess) {
                countSuccess++;
            }

            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

            // dbtable = ConnectionFactory.eINSTANCE.createMetadataTable();

            initExistingNames();
            String labelName = IndiceHelper.getIndexedLabel(tableString, existingNames);

            if (forTemplate) {
                labelName = MetadataToolHelper.validateValue(labelName);
            }
            dbtable.setLabel(labelName);
            dbtable.setSourceName(tableItem.getText(0));
            dbtable.setId(factory.getNextId());
            dbtable.setTableType(ExtractMetaDataFromDataBase.getTableTypeByTableName(tableString));

            List<MetadataColumn> metadataColumnsValid = new ArrayList<MetadataColumn>();
            Iterator iterate = metadataColumns.iterator();

            while (iterate.hasNext()) {
                MetadataColumn metadataColumn = (MetadataColumn) iterate.next();

                // Check the label and add it to the table
                metadataColumnsValid.add(metadataColumn);
                dbtable.getColumns().add(metadataColumn);
            }
            if (!ConnectionHelper.getTables(getConnection()).contains(dbtable) && !limitTemplateTable(dbtable)) {
                // Catalog c = (Catalog) ConnectionHelper.getPackage((getConnection().getSID()), getConnection(),
                // Catalog.class);
                // if (c != null) {
                // PackageHelper.addMetadataTable(dbtable, c);
                // } else {
                // Schema s = (Schema) ConnectionHelper.getPackage((getConnection().getSID()), getConnection(),
                // Schema.class);
                // if (s != null) {
                // PackageHelper.addMetadataTable(dbtable, s);
                // }
                // }
                // getConnection().getTables().add(metadataTable); hywang
            }
            // if (!getConnection().getTables().contains(metadataTable) && !limitTemplateTable(metadataTable)) {
            // getConnection().getTables().add(metadataTable);
            // }

        }
    }

    /**
     * deleteTable.
     * 
     * @param tableItem
     */
    protected void deleteTable(TableItem tableItem) {
        SalesforceSchemaConnection connection = getConnection();
        Iterator<MetadataTable> iterate = ConnectionHelper.getTables(getConnection()).iterator();
        while (iterate.hasNext()) {
            MetadataTable metadata = iterate.next();
            if (metadata != null && metadata.getLabel().equals(tableItem.getText(0))
                    && metadata.eContainer() instanceof SalesforceModuleUnit) {
                SalesforceModuleUnit moduleUnit = (SalesforceModuleUnit) metadata.eContainer();
                connection.getModules().remove(moduleUnit);
            }
        }
        iterate = ConnectionHelper.getTables(temConnection).iterator();
        while (iterate.hasNext()) {
            MetadataTable metadata = iterate.next();
            if (metadata != null && metadata.getLabel().equals(tableItem.getText(0))
                    && metadata.eContainer() instanceof SalesforceModuleUnit) {
                SalesforceModuleUnit moduleUnit = (SalesforceModuleUnit) metadata.eContainer();
                temConnection.getModules().remove(moduleUnit);
            }
        }
    }

    /**
     * A subclass of ThreadPoolExecutor that executes each submitted RetrieveColumnRunnable using one of possibly
     * several pooled threads.
     * 
     * 
     */
    class CustomThreadPoolExecutor extends TalendCustomThreadPoolExecutor {

        // This map is used to store the tableItems that are selected or unselected by the user.
        // see afterExecute() and beforeExecute(). If an item is in the map, it means that the item's
        // related thread is running.
        Map<TableItem, RetrieveColumnRunnable> map = Collections
                .synchronizedMap(new HashMap<TableItem, RetrieveColumnRunnable>());

        public CustomThreadPoolExecutor(int queueCapacity) {
            super(queueCapacity);
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.concurrent.ThreadPoolExecutor#afterExecute(java.lang.Runnable, java.lang.Throwable)
         */
        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            RetrieveColumnRunnable runnable = (RetrieveColumnRunnable) r;
            map.remove(runnable.getTableItem());
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.concurrent.ThreadPoolExecutor#beforeExecute(java.lang.Thread, java.lang.Runnable)
         */
        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            RetrieveColumnRunnable runnable = (RetrieveColumnRunnable) r;
            map.put(runnable.getTableItem(), runnable);
        }

        /**
         * If an item is in the List runningThreads, it means that the item's related thread is running.
         * 
         * @param item
         * @return
         */
        public boolean isThreadRunning(TableItem item) {
            return map.containsKey(item);
        }

        /**
         * Find the RetrieveColumnRunnable from map and waiting queue. Map stores running runnables
         * 
         * @param key
         * @return
         */
        public synchronized RetrieveColumnRunnable getRunnable(TableItem key) {
            // Get the runnable from map first, else then find it in the waiting queue.
            RetrieveColumnRunnable runnable = map.get(key);
            if (runnable != null) {
                return runnable;
            }
            for (Object element2 : getQueue()) {
                RetrieveColumnRunnable element = (RetrieveColumnRunnable) element2;
                if (element.getTableItem() == key) {
                    return element;
                }
            }
            return null;
        }
    }

    /**
     * Subclass of SWTUIThreadProcessor to process the Retrieving Columns job. <br/>
     * 
     */
    class RetrieveColumnRunnable implements Runnable {

        IMetadataTable metadaTable;

        List<IMetadataColumn> listColumns;

        TableItem tableItem;

        String tableString = null;

        boolean checkConnectionIsDone = false;

        List<TdColumn> metadataColumns = null;

        volatile boolean isCanceled = false;

        /**
         * Getter for tableItem.
         * 
         * @return the tableItem
         */
        public TableItem getTableItem() {
            return this.tableItem;
        }

        RetrieveColumnRunnable(TableItem tableItem) {
            this.tableItem = tableItem;
            // setup();
        }

        public void setCanceled(boolean cancel) {
            this.isCanceled = cancel;
        }

        /**
         * Getter for isCanceled.
         * 
         * @return the isCanceled
         */
        public boolean isCanceled() {
            return this.isCanceled;
        }

        /**
         * Get all the parameters from UI for the non-UI job to use.
         */
        private void setup() {
            tableString = tableItem.getText(0);
            getConnection().setModuleName(tableString);
        }

        @Override
        public void run() {
            if (isCanceled()) {
                return;
            }

            // metadataColumns = ExtractMetaDataFromDataBase.returnMetadataColumnsFormTable(iMetadataConnection,
            // tableString, true);
            // if (isCanceled()) {
            // return;
            // }
            // IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            //
            // if
            // (ExtractMetaDataFromDataBase.getTableTypeByTableName(tableString).equals(ETableTypes.TABLETYPE_TABLE.getName()))
            // {
            // dbtable = RelationalFactory.eINSTANCE.createTdTable();
            // } else if (ExtractMetaDataFromDataBase.getTableTypeByTableName(tableString).equals(
            // ETableTypes.TABLETYPE_VIEW.getName())) {
            // dbtable = RelationalFactory.eINSTANCE.createTdView();
            // } else {
            // dbtable = RelationalFactory.eINSTANCE.createTdTable();
            // }
            // // metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
            // initExistingNames();
            // String lableName = IndiceHelper.getIndexedLabel(tableString, existingNames);
            //
            // // if (forTemplate) { //hywang modified for 0010012
            // lableName = MetadataToolHelper.validateValue(lableName);
            // // }
            // dbtable.setLabel(lableName);
            // dbtable.setSourceName(tableString);
            //
            // Iterator<String> it = ExtractMetaDataFromDataBase.tableCommentsMap.keySet().iterator();
            // while (it.hasNext()) {
            // String key = it.next().toString();
            // if (key.equals(tableString)) {
            // String comment = ExtractMetaDataFromDataBase.tableCommentsMap.get(key);
            // dbtable.setComment(comment);
            // TableHelper.setComment(comment, dbtable);
            // break;
            // }
            // }
            // dbtable.setId(factory.getNextId());
            // dbtable.setTableType(ExtractMetaDataFromDataBase.getTableTypeByTableName(tableString));
            // List<MetadataColumn> metadataColumnsValid = new ArrayList<MetadataColumn>();
            // Iterator iterate = metadataColumns.iterator();
            // while (iterate.hasNext()) {
            // MetadataColumn metadataColumn = (MetadataColumn) iterate.next();
            // if (metadataColumn.getTalendType().equals(JavaTypesManager.DATE.getId())
            // || metadataColumn.getTalendType().equals(PerlTypesManager.DATE)) {
            //                    if ("".equals(metadataColumn.getPattern())) { //$NON-NLS-1$
            //                        metadataColumn.setPattern(TalendQuoteUtils.addQuotes("dd-MM-yyyy")); //$NON-NLS-1$
            // }
            // }
            // // Check the label and add it to the table
            // metadataColumnsValid.add(metadataColumn);
            // dbtable.getColumns().add(metadataColumn);
            // }
            // if (!ConnectionHelper.getTables(getConnection()).contains(dbtable) && !limitTemplateTable(dbtable)) {
            //
            // String schema = iMetadataConnection.getSchema();
            // String catalog = iMetadataConnection.getDatabase();
            // String databaseType = iMetadataConnection.getDbType();
            // EDatabaseTypeName currentType = EDatabaseTypeName.getTypeFromDbType(databaseType);
            // EDatabaseSchemaOrCatalogMapping curCatalog = currentType.getCatalogMappingField();
            // EDatabaseSchemaOrCatalogMapping curSchema = currentType.getSchemaMappingField();
            // if (curCatalog != null && curSchema != null) {
            // switch (curCatalog) {
            // case Login:
            // catalog = iMetadataConnection.getUsername();
            // break;
            // case None:
            // catalog = "";
            // break;
            // }
            // switch (curSchema) {
            // case Login:
            // schema = iMetadataConnection.getUsername();
            // break;
            // case Schema:
            // schema = iMetadataConnection.getSchema();
            // break;
            // case None:
            // schema = "";
            // break;
            // case Default_Name:
            // schema = iMetadataConnection.getLabel(); // label for default name for
            // // access or such kind of
            // // non-catalogs databases
            // break;
            // }
            // }
            //
            // schema = ExtractMetaDataUtils.getMeataConnectionSchema(iMetadataConnection);
            // // ProjectNodeHelper.addTableForTemCatalogOrSchema(catalog, schema, getConnection(), dbtable,
            // // iMetadataConnection);
            // // getConnection().getTables().add(metadataTable);hywang
            // }
            // // }
            //
            // checkConnectionIsDone = true;
            //
            Display.getDefault().syncExec(new Runnable() {

                @Override
                public void run() {
                    if (isCanceled()) {
                        return;
                    }
                    setup();
                    metadaTable = readMetadataDetail();
                    listColumns = metadataTableOrder.getListColumns();
                    checkConnectionIsDone = true;
                    updateUIInThreadIfThread();
                }
            });

        }

        public void updateUIInThreadIfThread() {
            if (tableItem.isDisposed()) {
                return;
            }

            if (checkConnectionIsDone) {
                tableItem.setText(2, "" + listColumns.size()); //$NON-NLS-1$
                tableItem.setText(3, Messages.getString("SelectorTableForm.Success")); //$NON-NLS-1$
                synchronized (countSuccess) {
                    countSuccess++;
                }
                ((SalesforceSchemaConnection) connectionItem.getConnection()).setModuleName(tableItem.getText(0));
                temConnection.setModuleName(tableItem.getText(0));
                SalesforceModuleUnit module = ConnectionFactory.eINSTANCE.createSalesforceModuleUnit();
                module.setId(ProxyRepositoryFactory.getInstance().getNextId());
                module.setModuleName(tableItem.getText(0));
                module.setLabel(tableItem.getText(0));

                MetadataTable table = ConnectionFactory.eINSTANCE.createMetadataTable();
                String nextId = ProxyRepositoryFactory.getInstance().getNextId();
                table.setId(nextId);
                table.setLabel(tableItem.getText(0));
                if (listColumns.size() > 0) {
                    for (int i = 0; i < listColumns.size(); i++) {
                        MetadataColumn metadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
                        metadataColumn.setNullable(listColumns.get(i).isNullable());
                        metadataColumn.setLength(listColumns.get(i).getLength());
                        metadataColumn.setPattern(listColumns.get(i).getPattern());
                        metadataColumn.setTalendType(listColumns.get(i).getTalendType());
                        metadataColumn.setPrecision(listColumns.get(i).getPrecision());
                        metadataColumn.setLabel(listColumns.get(i).getLabel());
                        table.getColumns().add(i, metadataColumn);

                    }
                }
                module.getTables().add(table);
                temConnection.getModules().add(module);
                tableColumnNums.put(tableItem.getText(0), listColumns.size());
            } else {
                updateStatus(IStatus.WARNING, Messages.getString("DatabaseTableForm.connectionFailure")); //$NON-NLS-1$
                new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("DatabaseTableForm.connectionFailure"), //$NON-NLS-1$
                        managerConnection.getMessageException());

            }
            count++;

            updateStatus(IStatus.OK, null);
            // selectNoneTablesButton.setEnabled(true);
            // checkConnectionButton.setEnabled(true);

            parentWizardPage.setPageComplete(threadExecutor.getQueue().isEmpty()
                    && (threadExecutor.getActiveCount() == 0 || countSuccess == countPending));
        }
    }

    /**
     * refreshTable. This Methos execute the CreateTable in a Thread task.
     * 
     * @param tableItem
     * @param size
     */
    private void refreshTable(final TableItem tableItem, final int size) {
        if (threadExecutor == null) {
            return;
        }
        RetrieveColumnRunnable runnable = new RetrieveColumnRunnable(tableItem);
        threadExecutor.execute(runnable);
    }

    private void clearTableItem(TableItem item) {
        clearTableItem(item, true);
    }

    private void clearTableItem(TableItem item, boolean deleteFromConnection) {
        if (deleteFromConnection) {
            deleteTable(item);
        }
        item.setText(2, ""); //$NON-NLS-1$
        item.setText(3, ""); //$NON-NLS-1$
        RetrieveColumnRunnable runnable = threadExecutor.getRunnable(item);
        if (runnable != null) {
            runnable.setCanceled(true);
        }
    }

    /**
     * DOC ocarbone Comment method "initExistingNames".
     * 
     * @param connection
     * @param metadataTable
     */
    private void initExistingNames() {
        String[] exisNames;
        if (dbtable != null) {
            exisNames = TableHelper.getTableNames(getConnection(), dbtable.getLabel());
        } else {
            exisNames = TableHelper.getTableNames(getConnection());
        }
        this.existingNames = existingNames == null ? Collections.EMPTY_LIST : Arrays.asList(exisNames);
    }

    /**
     * Main Fields addControls.
     */
    @Override
    protected void addFieldsListeners() {
        nameFilter.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (itemTableName == null) {
                    return;
                }
                List<String> newList = new ArrayList<String>();

                String pattern = nameFilter.getText();
                SearchPattern matcher = new SearchPattern();
                matcher.setPattern(pattern);
                for (int i = 0; i < itemTableName.size(); i++) {
                    String item = itemTableName.get(i);
                    if (matcher.matches(item)) {
                        newList.add(item);
                    }
                }
                for (int j = 0; j < table.getItemCount(); j++) {
                    TableItem item = table.getItem(j);
                    if (item.getChecked()) {
                        clearTableItem(item, false);
                        item.setChecked(false);
                    }
                }
                table.clearAll();
                if (!table.isDisposed()) {
                    table.dispose();
                    table = null;
                    createTable();
                    addTableListener();
                }
                createAllItems(false, newList);
            }

        });
    }

    /**
     * DOC hcw Comment method "restoreCheckItems".
     */
    protected void restoreCheckItems() {
        Set<String> checkedItems = new HashSet<String>();
        if (isContextMode()) {
            for (Object obj : ConnectionHelper.getTables(temConnection)) {
                if (obj == null) {
                    continue;
                }
                MetadataTable table = (MetadataTable) obj;
                checkedItems.add(table.getLabel());
            }
            for (TableItem tableItem : table.getItems()) {
                tableItem.setChecked(false);
                if (checkedItems.contains(tableItem.getText(0))) {
                    tableItem.setChecked(true);
                    Integer num = tableColumnNums.get(tableItem.getText(0));
                    if (num != null) {
                        // get column num from previous result
                        tableItem.setText(2, num.toString());
                        tableItem.setText(3, Messages.getString("SelectorTableForm.Success")); //$NON-NLS-1$   
                    } else {
                        // retrieve column num again
                        refreshTable(tableItem, -1);
                    }
                }
            }
        } else {
            for (Object obj : ConnectionHelper.getTables(getConnection())) {
                if (obj == null) {
                    continue;
                }
                MetadataTable table = (MetadataTable) obj;
                checkedItems.add(table.getLabel());
            }
            for (TableItem tableItem : table.getItems()) {
                tableItem.setChecked(false);
                if (checkedItems.contains(tableItem.getText(0))) {
                    tableItem.setChecked(true);
                    Integer num = tableColumnNums.get(tableItem.getText(0));
                    if (num != null) {
                        // get column num from previous result
                        tableItem.setText(2, num.toString());
                        tableItem.setText(3, Messages.getString("SelectorTableForm.Success")); //$NON-NLS-1$   
                    } else {
                        // retrieve column num again
                        refreshTable(tableItem, -1);
                    }
                }
            }
        }
    }

    /**
     * wzhang Comment method "isExistingNames".
     */
    private boolean isExistingNames(String name) {
        if (name == null) {
            return false;
        }
        String[] existedNames;
        if (dbtable != null) {
            existedNames = TableHelper.getTableNames(getConnection(), dbtable.getLabel());
        } else {
            existedNames = TableHelper.getTableNames(getConnection());
        }
        if (existedNames.length > 0) {
            if (Arrays.asList(existedNames).contains(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Ensures that fields are set. Update checkEnable / use to checkTableSetting().
     */
    @Override
    protected boolean checkFieldsValue() {
        updateStatus(IStatus.OK, null);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.AbstractForm#adaptFormToReadOnly()
     */
    @Override
    protected void adaptFormToReadOnly() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Control#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            initializeForm();
        }
        // initControlData();
    }

    /**
     * DOC qzhang Comment method "initControlData".
     */
    public void initControlData() {
        checkConnection(false);
        if (itemTableName != null && itemTableName.size() > 0) {
            threadExecutor = new CustomThreadPoolExecutor(itemTableName.size());
        }
    }

    public void initControlData(boolean flag) {
        checkConnection(flag);
        if (itemTableName != null && itemTableName.size() > 0) {
            threadExecutor = new CustomThreadPoolExecutor(itemTableName.size());
        }
    }

    @Override
    protected SalesforceSchemaConnection getConnection() {
        if (oldTemConnection != null) {
            return oldTemConnection;
        } else {
            if (forTemplate) {
                return (SalesforceSchemaConnection) templateConntion.getConnection();
            } else {
                return (SalesforceSchemaConnection) connectionItem.getConnection();
            }
        }
    }

    public Table getTable() {
        return this.table;
    }

    /**
     * DOC nrousseau Comment method "performCancel".
     */
    public void performCancel() {
        processWhenDispose();
    }

    /**
     * Getter for tableInfoParameters.
     * 
     * @return the tableInfoParameters
     */
    public TableInfoParameters getTableInfoParameters() {
        return this.tableInfoParameters;
    }

    public IMetadataConnection getIMetadataConnection() {
        return this.iMetadataConnection;
    }

    public void setIMetadataConnection(IMetadataConnection metadataConnection) {
        this.iMetadataConnection = metadataConnection;
    }

    @Override
    protected void processWhenDispose() {
        if (threadExecutor != null) {
            threadExecutor.clearThreads();
            ExtractMetaDataUtils.getInstance().closeConnection();
        }
    }

    public ConnectionItem getTemplateConntion() {
        return this.templateConntion;
    }

    public void setTemplateConntion(ConnectionItem templateConntion) {
        this.templateConntion = templateConntion;
    }

    private boolean limitTemplateTable(MetadataTable tabel) {
        boolean exist = false;
        if (!forTemplate) {
            return exist;
        }
        for (int i = 0; i < ConnectionHelper.getTables(getConnection()).size(); i++) {
            String sourceName = tabel.getSourceName();
            if ((ConnectionHelper.getTables(getConnection()).toArray(new MetadataTable[0])[i].getSourceName().equals(sourceName))) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    public List<String> getItemTableNameList() {
        return this.itemTableName;
    }

    public List<String> connectFromCustomModuleName(String proxy) throws Exception {
        preparModuleInit();
        SalesforceModuleParseAPI salesforceAPI = new SalesforceModuleParseAPI();
        String[] types = null;
        DescribeGlobalSObjectResult[] dgsrs = null;
        DescribeGlobalResult describeGlobalResult = null;
        boolean socksProxy = false;
        boolean httpProxy = false;
        boolean httpsProxy = false;
        if (SalesforceModuleParseAPI.USE_SOCKS_PROXY.equals(proxy)) {
            socksProxy = true;
        }
        if (SalesforceModuleParseAPI.USE_HTTP_PROXY.equals(proxy)) {
            if (endPoint.startsWith("https")) {
                httpsProxy = true;
            } else {
                httpProxy = true;
            }
        }
        if (loginType.equalsIgnoreCase(BASIC)) {
            salesforceAPI.resetAllProxy();
            salesforceAPI.setProxy(proxyHost, proxyPort, proxyUsername, proxyPassword, httpProxy, socksProxy, httpsProxy);
            salesforceAPI.login(endPoint, username, pwd, timeOut);
            ISalesforceModuleParser currentAPI = salesforceAPI.getCurrentAPI();
            if (currentAPI instanceof SalesforceModuleParseEnterprise) {
                describeGlobalResult = describeGlobal();
                if (describeGlobalResult != null) {
                    types = describeGlobalResult.getTypes();
                }
            } else {
                // for bug 17280 use new jar axis2 for salesforce component and wizard.
                if (currentAPI instanceof SalesforceModuleParserPartner) {
                    SalesforceModuleParserPartner partner = (SalesforceModuleParserPartner) currentAPI;
                    SforceManagementImpl sforceManagement = partner.getSforceManagement();
                    com.salesforce.soap.partner.DescribeGlobalResult dgr = sforceManagement.describeGlobal();
                    dgsrs = dgr.getSobjects();

                }
            }
        } else {
            salesforceAPI.resetAllProxy();
            salesforceAPI.setProxy(proxyHost, proxyPort, proxyUsername, proxyPassword, httpProxy, socksProxy, httpsProxy);
            Token token = salesforceAPI.login(endPointForAuth, consumerKey, consumeSecret, callbackHost, callbackPort,
                    salesforceVersion, tokenProperties, timeOut);
            if (token != null) {
                OAuthClient client = new OAuthClient();
                client.setBaseOAuthURL(endPointForAuth);
                client.setCallbackHost(callbackHost);
                client.setCallbackPort(Integer.parseInt(callbackPort));
                client.setClientID(consumerKey);
                client.setClientSecret(consumeSecret);
                String endpoint = OAuthClient.getSOAPEndpoint(token, salesforceVersion);
                org.talend.salesforce.SforceManagement sfMgr = null;
                SforceConnection sforceConn = new SforceSessionConnection.Builder(endpoint, token.getAccess_token())
                        .setTimeout(timeOut).needCompression(false).build();
                sfMgr = new org.talend.salesforce.SforceManagementImpl(sforceConn);
                dgsrs = sfMgr.describeGlobal().getSobjects();
            }
        }

        salesforceAPI.resetAllProxy();
        INode node = getSalesforceNode();

        List list = new ArrayList();

        IElementParameter modulesNameParam = node.getElementParameter("MODULENAME"); //$NON-NLS-1$
        Object[] modulename = modulesNameParam.getListItemsValue();
        if (modulename != null && modulename.length > 1) {
            for (int i = 0; i < modulename.length - 1; i++) {
                list.add(i, modulename[i]);
            }
        }
        if (types != null && types.length > 0) {
            for (int j = 0; j < types.length; j++) {
                if (!list.contains(types[j])) {
                    list.add(types[j]);
                }
            }
        }
        if (dgsrs != null && dgsrs.length > 0) {
            for (DescribeGlobalSObjectResult dsResult : dgsrs) {
                String name = dsResult.getName();
                if (!list.contains(name)) {
                    list.add(name);
                }
            }

        }
        // createAllItems(false, list);
        return list;

    }

    private void preparModuleInit() {
        /*
         * prepare to ininCustomModule
         */
        SalesforceSchemaConnection connection = oldTemConnection;
        endPoint = connection.getWebServiceUrl();
        username = connection.getUserName();
        pwd = connection.getValue(connection.getPassword(), false);
        batchSize = connection.getBatchSize();
        timeOut = connection.getTimeOut();
        useHttpProxy = connection.isUseHttpProxy();
        if (useHttpProxy && endPoint.startsWith("https")) {
            useHttpsProxy = true;
        }
        useSocketProxy = connection.isUseProxy();
        proxyHost = connection.getProxyHost();
        proxyPort = connection.getProxyPort();
        proxyUsername = connection.getProxyUsername();
        proxyPassword = connection.getValue(connection.getProxyPassword(), false);

        endPointForAuth = connection.getWebServiceUrlTextForOAuth();
        consumerKey = connection.getConsumeKey();
        consumeSecret = connection.getValue(connection.getConsumeSecret(), false);
        callbackHost = connection.getCallbackHost();
        callbackPort = connection.getCallbackPort();
        salesforceVersion = connection.getSalesforceVersion();
        tokenProperties = connection.getToken();
        loginType = connection.getLoginType();
    }
}
