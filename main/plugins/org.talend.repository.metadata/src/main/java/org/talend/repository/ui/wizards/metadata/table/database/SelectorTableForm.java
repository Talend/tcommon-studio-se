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
package org.talend.repository.ui.wizards.metadata.table.database;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.SQLException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ContainerCheckedTreeViewer;
import org.eclipse.ui.dialogs.SearchPattern;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.commons.utils.data.text.IndiceHelper;
import org.talend.commons.utils.threading.TalendCustomThreadPoolExecutor;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.database.conn.ConnParameterKeys;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.MetadataToolHelper;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase.ETableTypes;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.metadata.builder.database.TableInfoParameters;
import org.talend.core.model.metadata.builder.database.TableNode;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.PerlTypesManager;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.model.provider.AbstractMetadataExtractorViewProvider;
import org.talend.core.sqlbuilder.util.TextUtil;
import org.talend.core.ui.metadata.editor.MetadataEmfTableEditor;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.PackageHelper;
import org.talend.cwm.helper.SchemaHelper;
import org.talend.cwm.helper.TableHelper;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdTable;
import org.talend.cwm.relational.TdView;
import org.talend.metadata.managment.connection.manager.HiveConnectionManager;
import org.talend.metadata.managment.connection.manager.ImpalaConnectionManager;
import org.talend.metadata.managment.model.MetadataFillFactory;
import org.talend.metadata.managment.repository.ManagerConnection;
import org.talend.metadata.managment.ui.model.ProjectNodeHelper;
import org.talend.metadata.managment.ui.wizard.AbstractForm;
import org.talend.metadata.managment.utils.MetadataConnectionUtils;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.utils.sql.ConnectionUtils;

import orgomg.cwm.objectmodel.core.CoreFactory;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.TaggedValue;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.NamedColumnSet;
import orgomg.cwm.resource.relational.Schema;
import orgomg.cwm.resource.relational.impl.CatalogImpl;
import orgomg.cwm.resource.relational.impl.SchemaImpl;

/**
 * @author cantoine
 * 
 */
public class SelectorTableForm extends AbstractForm {

    /**
     * FormTable Settings.
     */
    private static final int WIDTH_GRIDDATA_PIXEL = 700;

    /**
     * FormTable Var.
     */
    private final ManagerConnection managerConnection;

    private MetadataTable dbtable;

    private MetadataEmfTableEditor metadataEditor;

    private UtilsButton selectAllTablesButton;

    private UtilsButton selectNoneTablesButton;

    private UtilsButton checkConnectionButton;

    /**
     * Anothers Fields.
     */

    private ConnectionItem templateConntion;

    private ContainerCheckedTreeViewer viewer;

    private Tree tree;

    private List<TableNode> tableNodeList = new ArrayList<TableNode>();

    private volatile int  countSuccess = 0;

    private volatile int countPending = 0;

    private final WizardPage parentWizardPage;

    CustomThreadPoolExecutor threadExecutor;

    ScrolledComposite scrolledCompositeFileViewer;

    private Text nameFilter;

    private final TableInfoParameters tableInfoParameters;

    private boolean forTemplate = false;

    // store column number for each table name
    private final Map<String, Integer> tableColumnNums = new HashMap<String, Integer>();

    private DatabaseConnection temConnection;

    private String locker = "";

    private Map<String, Boolean> mapCheckState = new HashMap<String, Boolean>();

    private ScrolledComposite scrolledComposite;

    private Composite newParent;

    /**
     * TableForm Constructor to use by RCP Wizard.
     * 
     * @param parent
     * @param page
     * @param connection
     * @param page
     * @param metadataTable
     */
    public SelectorTableForm(Composite parent, ConnectionItem connectionItem, SelectorTableWizardPage page) {
        super(parent, SWT.NONE);
        managerConnection = new ManagerConnection();
        this.connectionItem = connectionItem;
        this.parentWizardPage = page;
        this.tableInfoParameters = page.getTableInfoParameters();
        setupForm();
    }

    public SelectorTableForm(Composite parent, ConnectionItem connectionItem, SelectorTableWizardPage page, boolean forTemplate,
            DatabaseConnection temConnection, IMetadataConnection iMetadataConnection) {
        super(parent, SWT.NONE);
        managerConnection = new ManagerConnection();
        this.connectionItem = connectionItem;
        this.templateConntion = connectionItem;
        this.parentWizardPage = page;
        this.tableInfoParameters = page.getTableInfoParameters();
        this.forTemplate = forTemplate;
        this.temConnection = temConnection;
        this.metadataconnection = iMetadataConnection;
        this.typeName = EDatabaseTypeName.getTypeFromDbType(metadataconnection.getDbType());
        /* use provider for the databse didn't use JDBC,for example: HBase */
        if (typeName != null && typeName.isUseProvider()) {
            this.provider = ExtractMetaDataFromDataBase.getProviderByDbType(iMetadataConnection.getDbType());
        }

        if (forTemplate) {// && ConnectionHelper.getTables(getConnection()).size() <= 0
            page.setPageComplete(false);
        }
        setupForm();
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
    }

    @Override
    protected void addFields() {
        int width = getSize().x;
        // Main Composite : 2 columns
        Composite mainComposite = Form.startNewGridLayout(this, 1);
        mainComposite.setLayout(new GridLayout(1, false));
        GridData gridData = new GridData(GridData.FILL_BOTH);
        mainComposite.setLayoutData(gridData);

        Composite rightComposite = Form.startNewGridLayout(mainComposite, 1);

        // Group Table Settings
        Group groupTableSettings = Form
                .createGroup(rightComposite, 1, Messages.getString("SelectorTableForm.groupTableSettings")); //$NON-NLS-1$

        scrolledComposite = new ScrolledComposite(groupTableSettings, SWT.V_SCROLL | SWT.H_SCROLL);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        scrolledComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
        newParent = new Composite(scrolledComposite, SWT.NONE);
        newParent.setLayout(new GridLayout());
        scrolledComposite.setContent(newParent);

        // Composite TableSettings
        Composite compositeTableSettings = Form.startNewGridLayout(newParent, 1);
        gridData = new GridData(GridData.FILL_BOTH);
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
        int hight = 290;
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
        parentWizardPage.setPageComplete(false);
        // addUtilsButtonListeners();
        if (this.typeName != null
                && (this.typeName.getProduct().equals(EDatabaseTypeName.HBASE.getProduct()) || this.typeName.getProduct().equals(
                        EDatabaseTypeName.MAPRDB.getProduct()))) {
            selectAllTablesButton.setEnabled(false);
            selectAllTablesButton.setToolTipText(Messages.getString("SelectorTableForm.toolTip"));
            selectNoneTablesButton.setEnabled(false);
            selectNoneTablesButton.setToolTipText(Messages.getString("SelectorTableForm.toolTip"));
        }

        scrolledComposite.addControlListener(new ControlAdapter() {

            @Override
            public void controlResized(ControlEvent e) {
                scrolledComposite.setMinSize(newParent.computeSize(SWT.DEFAULT, SWT.DEFAULT));
            }
        });
    }

    /**
     * DOC qzhang Comment method "createTable".
     */
    private void createTable() {
        int columnWidth1 = 300;
        int columnWidth2 = 140;
        int columnWidth3 = 125;
        int columnWidth4 = 140;

        if (forTemplate) {
            columnWidth1 = 150;
            columnWidth2 = 100;
            columnWidth3 = 100;
            columnWidth4 = 110;
        }
        viewer = new ContainerCheckedTreeViewer(scrolledCompositeFileViewer, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER
                | SWT.FULL_SELECTION | SWT.MULTI);
        viewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
        viewer.setUseHashlookup(true);
        viewer.addFilter(new selectorViewerFilter());
        viewer.setCheckStateProvider(new checkStateProvider());
        tree = viewer.getTree();
        tree.setHeaderVisible(true);
        tree.setLinesVisible(true);
        // if table already exist in connection, should set checked.
        tree.addListener(SWT.Expand, new Listener() {

            @Override
            public void handleEvent(Event event) {
                TreeItem treeItem = (TreeItem) event.item;
                String itemText = treeItem.getText();
                boolean needUpdate = treeItem.getChecked();
                TableNode parentNode = (TableNode) treeItem.getData();
                // TDI-28768 after add checkStateProvider, the catalog and schema checked status become true , then
                // force them to false
                if (parentNode.getType() == TableNode.CATALOG || parentNode.getType() == TableNode.SCHEMA) {
                    needUpdate = false;
                }
                boolean firstExpand = false;
                if (mapCheckState.containsKey(itemText)) {
                    firstExpand = mapCheckState.get(itemText);
                } else {
                    firstExpand = true;
                }
                for (TreeItem item : treeItem.getItems()) {
                    if (item.getData() != null) {
                        TableNode node = (TableNode) item.getData();
                        if (useProvider()) {
                            if (node.getType() == TableNode.COLUMN_FAMILY) {
                                if (firstExpand && needUpdate && item.getData() != null) {
                                    updateLeafItem(item, true, TableNode.COLUMN);
                                } else if (isExistTable(node)) {
                                    item.setChecked(true);
                                } else {
                                    item.setChecked(false);
                                }
                            } else if (node.getType() == provider.getRunnableAccessNodeType()) {
                                if (isExistTable(node)) {
                                    item.setChecked(true);
                                } else {
                                    item.setChecked(false);
                                }
                            }
                        } else if (node.getType() == TableNode.TABLE) {
                            if (firstExpand && needUpdate && item.getData() != null) {
                                updateItem(item, true, true);
                            } else if (isExistTable(node)) {
                                item.setChecked(true);
                            } else {
                                item.setChecked(false);
                            }
                        } else if (firstExpand && needUpdate && node.getType() == TableNode.SCHEMA) {
                            item.setChecked(true);
                            mapCheckState.put(item.getText(), firstExpand);
                        } else {
                            item.setGrayed(item.getChecked());
                        }
                    }
                }
                if (firstExpand) {
                    mapCheckState.put(itemText, false);
                }
            }
        });
        // tree.setBackground(tree.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
        TreeColumn tableName = new TreeColumn(tree, SWT.NONE);
        tableName.setText(Messages.getString("SelectorTableForm.TableName")); //$NON-NLS-1$
        tableName.setWidth(columnWidth1);

        TreeColumn tableType = new TreeColumn(tree, SWT.NONE);
        tableType.setText(Messages.getString("SelectorTableForm.TableType")); //$NON-NLS-1$
        tableType.setWidth(columnWidth2);

        TreeColumn nbColumns = new TreeColumn(tree, SWT.RIGHT);
        nbColumns.setText(Messages.getString("SelectorTableForm.ColumnNumber")); //$NON-NLS-1$
        if (this.typeName != null
                && (this.typeName.getProduct().equals(EDatabaseTypeName.HBASE.getProduct()) || this.typeName.getProduct().equals(
                        EDatabaseTypeName.MAPRDB.getProduct()))) {
            nbColumns.setWidth(0);
            nbColumns.setResizable(false);
        } else {
            nbColumns.setWidth(columnWidth3);
        }

        TreeColumn creationStatus = new TreeColumn(tree, SWT.RIGHT);
        creationStatus.setText(Messages.getString("SelectorTableForm.CreationStatus")); //$NON-NLS-1$
        creationStatus.setWidth(columnWidth4);

        AbstractMetadataExtractorViewProvider viewProvider = null;
        /* use extractor for the databse didn't use JDBC,for example: HBase */
        if (useProvider()) {
            viewProvider = provider.getMetadataViewProvider();

        } else {
            viewProvider = new SelectorTreeViewerProvider();
        }

        viewer.setLabelProvider(viewProvider);
        viewer.setContentProvider(viewProvider);
        viewer.setInput(tableNodeList);

        scrolledCompositeFileViewer.setContent(tree);
        scrolledCompositeFileViewer.setMinSize(tree.computeSize(SWT.DEFAULT, SWT.DEFAULT));
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

                TreeItem[] items = tree.getItems();

                Arrays.sort(items, strComparator);

                selectiontable.setItemCount(items.length);

                for (int i = 0; i < items.length; i++) {
                    TreeItem item = new TreeItem(tree, SWT.NONE, i);
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

            private String[] getData(TreeItem t) {
                Tree selectiontree = t.getParent();

                int colCount = selectiontree.getColumnCount();
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
                checkConnection(true);
            }
        });

        selectAllTablesButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                try {
                    parentWizardPage.getWizard().getContainer().run(true, true, new IRunnableWithProgress() {

                        @Override
                        public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                            PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {

                                @Override
                                public void run() {
                                    monitor.beginTask("extract Columns", tree.getItems().length);
                                    int length = tree.getItems().length;
                                    updateStatus(IStatus.ERROR, null);
                                    for (int i = 0; i < length; i++) {
                                        monitor.worked(i + 1);
                                        TreeItem catalogItem = tree.getItem(i);
                                        int type = ((TableNode) catalogItem.getData()).getType();
                                        if (type == TableNode.CATALOG) {
                                            for (TreeItem schemaItem : catalogItem.getItems()) {
                                                if (schemaItem.getData() != null) {
                                                    int t = ((TableNode) schemaItem.getData()).getType();
                                                    if (t == TableNode.SCHEMA) {
                                                        for (TreeItem tableItem : schemaItem.getItems()) {
                                                            updateItem(tableItem, true, false);
                                                        }
                                                        schemaItem.setChecked(true);
                                                    } else if (t == TableNode.TABLE) {
                                                        updateItem(schemaItem, true, false);
                                                    }
                                                }
                                            }
                                            catalogItem.setChecked(true);
                                            catalogItem.setGrayed(true);
                                        } else if (type == TableNode.SCHEMA) {
                                            for (TreeItem tableItem : catalogItem.getItems()) {
                                                updateItem(tableItem, true, false);
                                            }
                                            catalogItem.setChecked(true);
                                            catalogItem.setGrayed(true);
                                        } else if (type == TableNode.TABLE) {
                                            updateItem(catalogItem, true, false);
                                        }
                                    }

                                    if (forTemplate) {
                                        parentWizardPage.setPageComplete(true);
                                    }
                                    monitor.done();
                                }
                            });
                        }

                    });
                } catch (InvocationTargetException e1) {
                    ExceptionHandler.process(e1);
                } catch (InterruptedException e1) {
                    ExceptionHandler.process(e1);
                }
            }
        });

        selectNoneTablesButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                countSuccess = 0;
                countPending = 0;
                for (TreeItem catalogItem : tree.getItems()) {
                    int type = ((TableNode) catalogItem.getData()).getType();
                    if (type == TableNode.CATALOG) {
                        for (TreeItem schemaItem : catalogItem.getItems()) {
                            if (schemaItem.getData() != null) {
                                int t = ((TableNode) schemaItem.getData()).getType();
                                if (t == TableNode.SCHEMA) {
                                    for (TreeItem tableItem : schemaItem.getItems()) {
                                        updateItem(tableItem, false, false);
                                    }
                                    schemaItem.setChecked(false);
                                } else if (t == TableNode.TABLE) {
                                    updateItem(schemaItem, false, false);
                                }
                            }
                        }
                        catalogItem.setChecked(false);
                    } else if (type == TableNode.SCHEMA) {
                        for (TreeItem tableItem : catalogItem.getItems()) {
                            updateItem(tableItem, false, false);
                        }
                        catalogItem.setChecked(false);
                    } else if (type == TableNode.TABLE) {
                        updateItem(catalogItem, false, false);
                    }
                }
                // if (forTemplate) {
                parentWizardPage.setPageComplete(false);
                // }
            }

        });

        addTableListener();
    }

    /**
     * DOC qzhang Comment method "addTableListener".
     */
    private void addTableListener() {
        // Event checkBox action
        tree.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (e.detail == SWT.CHECK) {
                    TreeItem treeItem = (TreeItem) e.item;
                    Object data = treeItem.getData();
                    TableNode tableNode = (TableNode) data;
                    int type = tableNode.getType();
                    boolean promptNeeded = treeItem.getChecked();
                    /*
                     * if use extractor to get metadata,should access the runnable by using extractor
                     */
                    if (useProvider()) {
                        if (type != TableNode.COLUMN) {
                            treeItem.setGrayed(true);
                        }
                        if (type == TableNode.TABLE) {
                            viewer.expandToLevel(tableNode, AbstractTreeViewer.ALL_LEVELS);
                            for (TreeItem childItem : treeItem.getItems()) {
                                if (childItem.getData() != null) {
                                    TableNode childNode = (TableNode) childItem.getData();
                                    int childNodeType = childNode.getType();
                                    if (childNodeType == TableNode.COLUMN_FAMILY) {
                                        childItem.setChecked(promptNeeded);
                                        for (TreeItem colItem : childItem.getItems()) {
                                            updateLeafItem(colItem, promptNeeded, TableNode.COLUMN);
                                        }
                                    } else if (childNodeType == TableNode.COLUMN) {
                                        updateLeafItem(childItem, promptNeeded, TableNode.COLUMN);
                                    }
                                }
                            }
                        } else if (type == TableNode.COLUMN_FAMILY) {
                            viewer.expandToLevel(tableNode, AbstractTreeViewer.ALL_LEVELS);
                            for (TreeItem colItem : treeItem.getItems()) {
                                updateLeafItem(colItem, promptNeeded, TableNode.COLUMN);
                            }
                        } else if (type == TableNode.COLUMN) {
                            updateLeafItem(treeItem, promptNeeded, TableNode.COLUMN);
                        }
                        /* use old logical if no extractor used */
                    } else {
                        if (type != TableNode.TABLE) {
                            treeItem.setGrayed(true);
                        }
                        if (type == TableNode.CATALOG) {
                            for (TreeItem schemaItem : treeItem.getItems()) {
                                if (schemaItem.getData() != null) {
                                    int t = ((TableNode) schemaItem.getData()).getType();
                                    if (t == TableNode.SCHEMA) {
                                        for (TreeItem tableItem : schemaItem.getItems()) {
                                            updateItem(tableItem, promptNeeded, true);
                                        }
                                    } else if (t == TableNode.TABLE) {
                                        updateItem(schemaItem, promptNeeded, true);
                                    }
                                }
                            }
                        } else if (type == TableNode.SCHEMA) {
                            for (TreeItem tableItem : treeItem.getItems()) {
                                updateItem(tableItem, promptNeeded, true);
                            }
                        } else if (type == TableNode.TABLE) {
                            if (promptNeeded) {
                                MetadataTable existTable = getExistTable(treeItem.getText(0));
                                if (existTable != null) {
                                    refreshExistItem(existTable, treeItem);
                                } else {
                                    treeItem.setText(2, ""); //$NON-NLS-1$
                                    treeItem.setText(3, Messages.getString("SelectorTableForm.Pending")); //$NON-NLS-1$
                                    refreshColumnData(tableNode, treeItem);
                                    parentWizardPage.setPageComplete(false);
                                    refreshTable(treeItem, -1);
                                }
                            } else {
                                clearTableItem(treeItem);    
                            }
                        }
                    }
                    boolean pageC = pageComplete();
                    if (pageC) {
                        parentWizardPage.setPageComplete(false);
                    }

                }
            }
        });
    }

    private void updateLeafItem(final TreeItem treeItem, boolean promptNeeded, int nodeType) {
        if (treeItem == null) {
            return;
        }
        Object data = treeItem.getData();
        if (data != null && data instanceof TableNode) {
            treeItem.setChecked(promptNeeded);
            TableNode tableNode = (TableNode) data;
            if (tableNode.getType() == nodeType) {
                if (promptNeeded) {
                    MetadataTable existTable = getExistTable(treeItem.getText(0));
                    if (existTable != null) {
                        refreshExistItem(existTable, treeItem);
                    } else {
                        treeItem.setText(2, ""); //$NON-NLS-1$
                        treeItem.setText(3, Messages.getString("SelectorTableForm.Pending")); //$NON-NLS-1$
                        refreshColumnData(tableNode, treeItem);
                        parentWizardPage.setPageComplete(false);
                        refreshTable(treeItem, -1);
                    }
                } else {                   
                    clearTableItem(treeItem);
                }
            } else {
                if (!treeItem.getExpanded()) {
                    viewer.expandToLevel(tableNode, 1);
                }
                for (TreeItem colItem : treeItem.getItems()) {
                    updateLeafItem(colItem, promptNeeded, nodeType);
                }
            }
        }
    }

    private boolean pageComplete() {
        if (parentWizardPage instanceof SelectorTableWizardPage) {
            List<MetadataTable> list = new ArrayList<MetadataTable>();
            Set<MetadataTable> oldTables = ((SelectorTableWizardPage) parentWizardPage).getOldTables();
            Set<MetadataTable> tables = ConnectionHelper.getTables(getConnection());
            if (oldTables != null && oldTables.size() > 0) {
                for (MetadataTable table : tables) {
                    boolean found = false;
                    for (MetadataTable oldtable : oldTables) {
                        if (table != null && oldtable != null && table.getLabel().equals(oldtable.getLabel())) {
                            found = true;
                            break;
                        }
                    }
                    if (table != null && !found) {
                        list.add(table);
                    }
                }
            } else {
                list.addAll(tables);
            }
            if (forTemplate || list.size() <= 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * wzhang Comment method "updateItem".
     * 
     * @param item
     * @param checked
     */
    private void updateItem(final TreeItem item, boolean checked, boolean isEvent) {
        if (item == null) {
            return;
        }
        Object data = item.getData();
        if (data != null && data instanceof TableNode) {
            TableNode tableNode = (TableNode) data;
            if (tableNode.getType() == TableNode.TABLE) {
                String pattern = getNameFilter();
                SearchPattern matcher = new SearchPattern();
                matcher.setPattern(pattern);
                if (!matcher.matches(tableNode.getValue())) {
                    return;
                }
                // if from click event, should set the table item original status.
                if (isEvent) {
                    Set<MetadataTable> tables = ConnectionHelper.getTables(getConnection());
                    for (MetadataTable table : tables) {
                        if (table.getLabel().equals(tableNode.getValue())) {
                            item.setChecked(true);
                        }
                    }
                    item.setChecked(!checked);
                }
                if (checked) {
                    if (!item.getChecked()) {
                        MetadataTable existTable = getExistTable(item.getText(0));
                        if (existTable != null) {
                            refreshExistItem(existTable, item);
                        } else {
                            item.setText(3, Messages.getString("SelectorTableForm.Pending")); //$NON-NLS-1$
                            refreshColumnData(tableNode, item);
                            parentWizardPage.setPageComplete(false);
                            refreshTable(item, -1);
                        }
                    } else {
                        updateStatus(IStatus.OK, null);
                    }
                    item.setChecked(true);
                } else {
                    if (item.getChecked()) {
                        clearTableItem(item);
                        item.setChecked(false);
                    }
                }
            }
        }
    }

    private List<TableNode> getTableNodeInfo() {
        List<TableNode> tableNodes = new ArrayList<TableNode>();
        DatabaseConnection connection = (DatabaseConnection) metadataconnection.getCurrentConnection();
        List<Catalog> catalogs = ConnectionHelper.getCatalogs(connection);
        if (catalogs.isEmpty()) {
            List<Schema> schemas = ConnectionHelper.getSchema(connection);
            String specifiedSchema = null;
            if (ManagerConnection.isSchemaFromSidOrDatabase(EDatabaseTypeName.getTypeFromDbType(connection.getDatabaseType()))) {
                String sid = connection.getSID();
                if (sid != null && sid.length() > 0) {
                    specifiedSchema = sid;
                }
            }
            if (schemas.isEmpty()) {
                return tableNodes;
            } else {
                EDatabaseTypeName dbType = EDatabaseTypeName.getTypeFromDbType(connection.getDatabaseType());
                for (Schema s : schemas) {
                    TableNode schemaNode = new TableNode();
                    schemaNode.setSchema(s);
                    schemaNode.setValue(s.getName());
                    schemaNode.setType(TableNode.SCHEMA);
                    schemaNode.setMetadataConn(metadataconnection);
                    schemaNode.setParas(tableInfoParameters);
                    // should hide the fake schema which used to store the synonyms for oracle
                    if (s.getName() != null && !s.getName().equals(MetadataConnectionUtils.FAKE_SCHEMA_SYNONYMS)) {
                        boolean canAdd = true;
                        if (specifiedSchema != null) {
                            specifiedSchema = TextUtil.removeQuots(specifiedSchema);
                            if (ManagerConnection.isSchemaCaseSensitive(dbType)) {
                                if (!s.getName().equals(specifiedSchema)) {
                                    canAdd = false;
                                }
                            } else {
                                if (!s.getName().toLowerCase().equals(specifiedSchema.toLowerCase())) {
                                    canAdd = false;
                                }
                            }
                        }
                        if (canAdd) {
                            tableNodes.add(schemaNode);
                        }
                    }
                }
            }
        } else {
            for (Catalog c : catalogs) {
                TableNode catalogNode = new TableNode();
                catalogNode.setCatalog(c);
                catalogNode.setValue(c.getName());
                catalogNode.setType(TableNode.CATALOG);
                catalogNode.setMetadataConn(metadataconnection);
                catalogNode.setParas(tableInfoParameters);
                List<Schema> schemas = CatalogHelper.getSchemas(c);
                if (schemas.isEmpty()) {
                    tableNodes.add(catalogNode);
                } else {
                    for (Schema s : schemas) {
                        TableNode schemaNode = new TableNode();
                        schemaNode.setSchema(s);
                        schemaNode.setValue(s.getName());
                        schemaNode.setType(TableNode.SCHEMA);
                        schemaNode.setMetadataConn(metadataconnection);
                        schemaNode.setParas(tableInfoParameters);
                        schemaNode.setParent(catalogNode);
                        catalogNode.addChild(schemaNode);
                    }
                    tableNodes.add(catalogNode);
                }
            }
        }
        return tableNodes;
    }

    private List<TableNode> getTableNodeForAllSynonyms(List<String> list, boolean useSynonyms) {
        List<TableNode> tableNodes = new ArrayList<TableNode>();
        if (list != null && !list.isEmpty()) {
            for (String string : list) {
                TableNode tableNode = new TableNode();
                tableNode.setType(TableNode.TABLE);
                tableNode.setValue(string);
                if (useSynonyms) {
                    tableNode.setItemType(ETableTypes.TABLETYPE_SYNONYM.getName());
                } else {
                    tableNode.setItemType(ExtractMetaDataFromDataBase.getTableTypeByTableName(string));
                }
                tableNodes.add(tableNode);
            }
        }

        return tableNodes;
    }

    private void updatePackage(IMetadataConnection metadataConnection) {
        if (metadataConnection == null) {
            return;
        }
        Driver derbyDriver = null;
        Connection sqlConn = null;
        ExtractMetaDataUtils extractMeta = ExtractMetaDataUtils.getInstance();
        // get dbType before get connection so that the dbtype won't be null.TDI-18366
        String dbType = metadataconnection.getDbType();
        DatabaseConnection dbConn = (DatabaseConnection) metadataconnection.getCurrentConnection();

        MetadataFillFactory dbInstance = MetadataFillFactory.getDBInstance(metadataconnection);
        if (EDatabaseTypeName.HIVE.getXmlName().equalsIgnoreCase(dbType)) {
            DatabaseMetaData dm = null;
            try {
                dm = HiveConnectionManager.getInstance().extractDatabaseMetaData(metadataConnection);
                dbInstance.fillCatalogs(dbConn, dm, metadataConnection,
                        MetadataConnectionUtils.getPackageFilter(dbConn, dm, true));
                dbInstance.fillSchemas(dbConn, dm, metadataConnection,
                        MetadataConnectionUtils.getPackageFilter(dbConn, dm, false));
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        } else if (EDatabaseTypeName.IMPALA.getDisplayName().equals(dbType)) {
            Connection conn = null;
            try {
                conn = ImpalaConnectionManager.getInstance().createConnection(metadataConnection);
                DatabaseMetaData dm = conn.getMetaData();
                dbInstance.fillCatalogs(dbConn, dm, metadataConnection,
                        MetadataConnectionUtils.getPackageFilter(dbConn, dm, true));
                dbInstance.fillSchemas(dbConn, dm, metadataConnection,
                        MetadataConnectionUtils.getPackageFilter(dbConn, dm, false));
            } catch (Exception e) {
                ExceptionHandler.process(e);
            } finally {
                if (sqlConn != null) {
                    ConnectionUtils.closeConnection(sqlConn);
                }
            }

        } else {
            List list = MetadataConnectionUtils.getConnection(metadataconnection);
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) instanceof Driver) {
                    String driverClass = metadataconnection.getDriverClass();
                    if (MetadataConnectionUtils.isDerbyRelatedDb(driverClass, dbType)) {
                        derbyDriver = (Driver) list.get(i);
                    }
                }
                if (list.get(i) instanceof java.sql.Connection) {
                    sqlConn = (java.sql.Connection) list.get(i);
                }
            }
            try {
                if (sqlConn != null) {
                    DatabaseMetaData dm = extractMeta.getDatabaseMetaData(sqlConn, dbType, false,
                            metadataconnection.getDatabase());
                    dbInstance.fillCatalogs(dbConn, dm, MetadataConnectionUtils.getPackageFilter(dbConn, dm, true));
                    dbInstance.fillSchemas(dbConn, dm, MetadataConnectionUtils.getPackageFilter(dbConn, dm, false));
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
            } finally {
                // bug 22619
                if (sqlConn != null) {
                    ConnectionUtils.closeConnection(sqlConn);
                }
                if (derbyDriver != null) {
                    try {
                        derbyDriver.connect("jdbc:derby:;shutdown=true", null); //$NON-NLS-1$
                    } catch (SQLException e) {
                        // exception of shutdown success. no need to catch.
                    }
                }
            }
        }

    }

    /**
     * checkConnection.
     * 
     * @param displayMessageBox
     */
    protected void checkConnection(final boolean displayMessageBox) {
        try {
            if (tree.getItemCount() > 0) {
                tree.removeAll();
            }
            parentWizardPage.getWizard().getContainer().run(true, true, new IRunnableWithProgress() {

                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    monitor.beginTask(Messages.getString("CreateTableAction.action.createTitle"), IProgressMonitor.UNKNOWN); //$NON-NLS-1$
                    // add
                    if (EDatabaseTypeName.HIVE.getDisplayName().equals(metadataconnection.getDbType())) {
                        String key = (String) metadataconnection.getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_MODE);
                        // if (HiveConnVersionInfo.MODE_EMBEDDED.getKey().equals(key)) {
                        try {
                            HiveConnectionManager.getInstance().checkConnection(metadataconnection);
                            managerConnection.setValide(true);
                        } catch (Exception e) {
                            managerConnection.setMessageException(e.getMessage());
                            ExceptionHandler.process(e);
                        }
                        // }
                        // else if (HiveConnVersionInfo.MODE_STANDALONE.getKey().equals(key)) {
                        // managerConnection.check(metadataconnection, true);
                        // }
                    } else {
                        managerConnection.check(metadataconnection, false); // Always check the connection.
                    }
                    if (managerConnection.getIsValide()) {
                        // need to check catalog/schema if import a old db connection
                        /* use extractor for the databse didn't use JDBC,for example: HBase */
                        boolean useAllSyn = ExtractMetaDataUtils.getInstance().isUseAllSynonyms();
                        if (useProvider()) {
                            provider.updatePackage(metadataconnection);
                        } else {
                            updatePackage(metadataconnection);
                        }
                        // update the connection
                        // EList<Package> dp = ((DatabaseConnection)
                        // iMetadataConnection.getCurrentConnection()).getDataPackage();
                        // Collection<Package> newDataPackage = EcoreUtil.copyAll(dp);
                        // ConnectionHelper.addPackages(newDataPackage, getConnection());

                        // need to enhance later
                        if (useAllSyn // before had a check if oracle
                                      // connection, but
                                // remove since useAllSynonyms is only on Oracle
                                || EDatabaseTypeName.ACCESS.getDisplayName().equals(metadataconnection.getDbType())) {
                            List<String> itemTableName = ExtractMetaDataFromDataBase.returnTablesFormConnection(
                                    metadataconnection, getTableInfoParameters());
                            if (useAllSyn) {
                                tableNodeList = getTableNodeForAllSynonyms(itemTableName, true);
                            } else {
                                tableNodeList = getTableNodeForAllSynonyms(itemTableName, false);
                            }
                        } else if (useProvider()) {
                            tableNodeList = provider.getTableNodeInfo(metadataconnection);
                        } else {
                            tableNodeList = getTableNodeInfo();
                        }
                        if (tableNodeList.isEmpty()) {
                            if (displayMessageBox) {
                                parentWizardPage.getShell().getDisplay().asyncExec(new Runnable() {

                                    @Override
                                    public void run() {
                                        MessageDialog.openInformation(getShell(), "Error", "No catalog or schema exist");
                                    }
                                });
                            }
                        } else {
                            // filter to display the extra context schema if exist after migration from 402 to new
                            // version
                            tableNodeList = filterItemContextSchema(tableNodeList);
                            createAllItems(displayMessageBox, null);
                        }
                    } else if (displayMessageBox) {
                        // connection failure
                        parentWizardPage.getShell().getDisplay().asyncExec(new Runnable() {

                            @Override
                            public void run() {
                                new ErrorDialogWidthDetailArea(getShell(), PID, Messages
                                        .getString("DatabaseTableForm.connectionFailureTip"), //$NON-NLS-1$
                                        managerConnection.getMessageException());
                            }
                        });
                    }
                    monitor.done();
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
                viewer.setInput(tableNodeList);
                // restoreCheckItems();
                // if (displayMessageBox) {
                //                    String msg = Messages.getString("DatabaseTableForm.connectionIsDone"); //$NON-NLS-1$
                //                    openInfoDialogInUIThread(getShell(), Messages.getString("DatabaseTableForm.checkConnection"), msg, false); //$NON-NLS-1$
                // }
            }
        });
    }

    public static void openInfoDialogInUIThread(final Shell shell, final String title, final String msg, boolean ifUseRunnable) {
        if (ifUseRunnable) {
            shell.getDisplay().asyncExec(new Runnable() {

                @Override
                public void run() {
                    MessageDialog.openInformation(shell, title, msg);
                }
            });
        } else {
            MessageDialog.openInformation(shell, title, msg);
        }
    }

    /**
     * createTable.
     * 
     * @param tableItem
     */
    protected void createTable(TreeItem tableItem) {
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
            metadataColumns = ExtractMetaDataFromDataBase
                    .returnMetadataColumnsFormTable(metadataconnection, tableItem.getText(0));

            tableItem.setText(2, "" + metadataColumns.size()); //$NON-NLS-1$
            tableItem.setText(3, Messages.getString("SelectorTableForm.Success")); //$NON-NLS-1$
            refreshColumnData((TableNode) tableItem.getData(), tableItem);

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
                Catalog c = (Catalog) ConnectionHelper.getPackage((getConnection().getSID()), getConnection(), Catalog.class);
                if (c != null) {
                    PackageHelper.addMetadataTable(dbtable, c);
                } else {
                    Schema s = (Schema) ConnectionHelper.getPackage((getConnection().getSID()), getConnection(), Schema.class);
                    if (s != null) {
                        PackageHelper.addMetadataTable(dbtable, s);
                    }
                }
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
    protected void deleteTable(TreeItem tableItem) {
        Collection<MetadataTable> tables = new ArrayList<MetadataTable>();
        Iterator<MetadataTable> iterate = ConnectionHelper.getTables(getConnection()).iterator();
        while (iterate.hasNext()) {
            MetadataTable metadata = iterate.next();
            if (metadata != null
                    && (metadata.getLabel().equals(tableItem.getText(0)) || (metadata.getName() != null && metadata.getName()
                            .equals(tableItem.getText(0))))) {
                tables.add(metadata);
            }
        }
        TableNode tableNode = (TableNode) tableItem.getData();
        TableNode parent = tableNode.getParent();

        removeTable(parent, tables);
    }

    protected void deleteTable(TableNode tableItem) {
        Collection<MetadataTable> tables = new ArrayList<MetadataTable>();
        Iterator<MetadataTable> iterate = ConnectionHelper.getTables(getConnection()).iterator();
        while (iterate.hasNext()) {
            MetadataTable metadata = iterate.next();
            if (metadata != null && metadata.getLabel().equals(tableItem.getValue())) {
                tables.add(metadata);
            }
        }
        TableNode parent = tableItem.getParent();
        removeTable(parent, tables);
    }

    private void removeTable(TableNode parent, Collection<MetadataTable> tables) {
        String catalog = "";
        String schema = "";
        if (parent != null) {
            if (parent.getType() == TableNode.CATALOG) {
                catalog = parent.getValue();
            } else if (parent.getType() == TableNode.SCHEMA) {
                schema = parent.getValue();
                TableNode catalogNode = parent.getParent();
                if (catalogNode != null) {
                    catalog = catalogNode.getValue();
                }
            }
        }

        boolean isAccess = EDatabaseTypeName.ACCESS.getDisplayName().equals(metadataconnection.getDbType());
        if (isAccess) {
            // For access's table's remove,must be accordance with its addTable
            ProjectNodeHelper.removeTablesFromCurrentCatalogOrSchema(catalog, getConnection().getName(), getConnection(), tables);
        } else {
            if ("".equals(schema) && ExtractMetaDataUtils.getInstance().isUseAllSynonyms()) {
                schema = MetadataConnectionUtils.FAKE_SCHEMA_SYNONYMS;
            }
            ProjectNodeHelper.removeTablesFromCurrentCatalogOrSchema(catalog, schema, getConnection(), tables);
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
        Map<TreeItem, RetrieveColumnRunnable> map = Collections.synchronizedMap(new HashMap<TreeItem, RetrieveColumnRunnable>());

        public CustomThreadPoolExecutor(int queueCapacity) {
            super(queueCapacity);
        }

        public CustomThreadPoolExecutor(int queueCapacity, RejectedExecutionHandler handler) {
            super(queueCapacity, handler);
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.concurrent.ThreadPoolExecutor#afterExecute(java.lang.Runnable, java.lang.Throwable)
         */
        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            RetrieveColumnRunnable runnable = (RetrieveColumnRunnable) r;
            map.remove(runnable.getTreeItem());
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.concurrent.ThreadPoolExecutor#beforeExecute(java.lang.Thread, java.lang.Runnable)
         */
        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            RetrieveColumnRunnable runnable = (RetrieveColumnRunnable) r;
            map.put(runnable.getTreeItem(), runnable);
        }

        /**
         * If an item is in the List runningThreads, it means that the item's related thread is running.
         * 
         * @param item
         * @return
         */
        public boolean isThreadRunning(TreeItem item) {
            return map.containsKey(item);
        }

        /**
         * Find the RetrieveColumnRunnable from map and waiting queue. Map stores running runnables
         * 
         * @param key
         * @return
         */
        public synchronized RetrieveColumnRunnable getRunnable(TreeItem key) {
            // Get the runnable from map first, else then find it in the waiting queue.
            RetrieveColumnRunnable runnable = map.get(key);
            if (runnable != null) {
                return runnable;
            }
            for (Object element2 : getQueue()) {
                RetrieveColumnRunnable element = (RetrieveColumnRunnable) element2;
                if (element.getTreeItem() == key) {
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

        TreeItem treeItem;

        TableNode tableNode;

        String tableString = null;

        boolean checkConnectionIsDone = false;

        List<TdColumn> metadataColumns = null;

        volatile boolean isCanceled = false;
        
        volatile boolean isFinished = false;
       

        /**
         * Getter for tableItem.
         * 
         * @return the tableItem
         */
        public TreeItem getTreeItem() {
            return this.treeItem;
        }

        RetrieveColumnRunnable(TreeItem treeItem) {
            this.treeItem = treeItem;
            this.tableNode = (TableNode) treeItem.getData();
            setup();
        }

        public void setCanceled(boolean cancel) {
            this.isCanceled = cancel;
            if (isCanceled) {
                if (!isFinished) {
                    countPending--;
                }
            } else {
                if (!isFinished) {
                    countPending++;
                }
            }
            final boolean isAllTaskFinished = isAllTaskFinished();
            final boolean pageC = pageComplete();
            Display.getDefault().syncExec(new Runnable() {
                @Override
                public void run() {
                    parentWizardPage.setPageComplete(isAllTaskFinished && !pageC);
                }
            });
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
            tableString = treeItem.getText(0);
        }

        @Override
        public void run() {
            boolean isAccess = EDatabaseTypeName.ACCESS.getDisplayName().equals(metadataconnection.getDbType());
            if (isCanceled()) {
                return;
            }

            if (tableNode.getType() == TableNode.TABLE) {
                // Connection conn = ExtractMetaDataUtils.getSqlConnection(iMetadataConnection);
                // DatabaseMetaData dbMetaData = ExtractMetaDataUtils.getDatabaseMetaData(conn,
                // iMetadataConnection.getDbType());
                NamedColumnSet table = tableNode.getTable();
                if (table == null) {
                    table = tableNode.getView();
                }

                if (isCanceled()) {
                    return;
                }
                IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
                // only for oracle use all synonyms
                boolean useAllSynonyms = ExtractMetaDataUtils.getInstance().isUseAllSynonyms();
                // Added a locker by Marvin Wang on May 15, 2012 for bug TDI-21058.
                synchronized (locker) {
                    if (useAllSynonyms || isAccess) {
                        metadataColumns = ExtractMetaDataFromDataBase.returnMetadataColumnsFormTable(metadataconnection,
                                tableString, true);
                        if (ExtractMetaDataFromDataBase.getTableTypeByTableName(tableString).equals(
                                ETableTypes.TABLETYPE_TABLE.getName())) {
                            dbtable = RelationalFactory.eINSTANCE.createTdTable();
                        } else if (ExtractMetaDataFromDataBase.getTableTypeByTableName(tableString).equals(
                                ETableTypes.TABLETYPE_VIEW.getName())) {
                            dbtable = RelationalFactory.eINSTANCE.createTdView();
                        } else {
                            dbtable = RelationalFactory.eINSTANCE.createTdTable();
                        }

                        Iterator<String> it = ExtractMetaDataFromDataBase.tableCommentsMap.keySet().iterator();
                        while (it.hasNext()) {
                            String key = it.next().toString();
                            if (key.equals(tableString)) {
                                String comment = ExtractMetaDataFromDataBase.tableCommentsMap.get(key);
                                dbtable.setComment(comment);
                                TableHelper.setComment(comment, dbtable);
                                break;
                            }
                        }
                        String lableName = IndiceHelper.getIndexedLabel(tableString, existingNames);
                        lableName = MetadataToolHelper.validateValue(lableName);
                        dbtable.setLabel(lableName);
                    } else {
                        String comment = null;
                        String type = null;
                        if (table instanceof TdTable) {
                            comment = ((TdTable) table).getComment();
                            type = ((TdTable) table).getTableType();
                            dbtable = RelationalFactory.eINSTANCE.createTdTable();
                        } else if (table instanceof TdView) {
                            comment = ((TdView) table).getComment();
                            type = ((TdView) table).getTableType();
                            dbtable = RelationalFactory.eINSTANCE.createTdView();
                        } else {
                            comment = ((TdTable) table).getComment();
                            type = ((TdTable) table).getTableType();
                            dbtable = RelationalFactory.eINSTANCE.createTdTable();
                        }
                        dbtable.setComment(comment);
                        EList<TaggedValue> tvs = table.getTaggedValue();
                        for (TaggedValue tv : tvs) {
                            TaggedValueHelper.setTaggedValue(dbtable, tv.getTag(), tv.getValue());
                        }
                        dbtable.setTableType(type);
                        String lableName = MetadataToolHelper.validateTableName(table.getName());
                        dbtable.setLabel(lableName);

                        if (useProvider()) {
                            metadataColumns = provider.returnMetadataColumnsFromTable(tableString, metadataconnection);
                        } else {
                            metadataColumns = ExtractMetaDataFromDataBase.returnColumns(metadataconnection, tableNode, true);
                        }
                    }
                    initExistingNames();

                    dbtable.setSourceName(tableString);
                    dbtable.setId(factory.getNextId());
                    dbtable.setTableType(tableNode.getItemType());

                    List<MetadataColumn> metadataColumnsValid = new ArrayList<MetadataColumn>();
                    Iterator iterate = metadataColumns.iterator();
                    while (iterate.hasNext()) {
                        MetadataColumn metadataColumn = (MetadataColumn) iterate.next();
                        if (metadataColumn.getTalendType() != null) {
                            if (metadataColumn.getTalendType().equals(JavaTypesManager.DATE.getId())
                                    || metadataColumn.getTalendType().equals(PerlTypesManager.DATE)) {
                                if ("".equals(metadataColumn.getPattern())) { //$NON-NLS-1$
                                    metadataColumn.setPattern(TalendQuoteUtils.addQuotes("dd-MM-yyyy")); //$NON-NLS-1$
                                }
                            }
                        }
                        // Check the label and add it to the table
                        metadataColumnsValid.add(metadataColumn);
                        dbtable.getColumns().add(EcoreUtil.copy(metadataColumn));
                    }
                    if (!ConnectionHelper.getTables(getConnection()).contains(dbtable) && !limitTemplateTable(dbtable)) {
                        String catalog = "";
                        String schema = "";
                        if (useAllSynonyms || isAccess) {

                            // if use_all_synonyms and oracle,just create a fake schema to store all the synonyms.see
                            // bug
                            // TDI-18353
                            if (!isAccess) {
                                schema = MetadataConnectionUtils.FAKE_SCHEMA_SYNONYMS;
                                boolean findFakeSynonymsSchema = isSynonymsSchemaExsist(schema);
                                if (!findFakeSynonymsSchema) {
                                    Schema SynonymsFakeSchema = SchemaHelper.createSchema(schema);
                                    ConnectionHelper.addSchema(SynonymsFakeSchema, temConnection);
                                }

                            } else { // TDI-19114:access db should be connection's label,do not need get again.
                                schema = metadataconnection.getLabel();
                            }
                        } else {
                            TableNode parent = tableNode.getParent();
                            if (parent.getType() == TableNode.CATALOG) {
                                catalog = parent.getValue();
                            } else if (parent.getType() == TableNode.SCHEMA) {
                                schema = parent.getValue();
                                TableNode catalogNode = parent.getParent();
                                if (catalogNode != null) {
                                    catalog = catalogNode.getValue();
                                }
                            }
                        }

                        if (isCanceled()) {
                            return;
                        }

                        try {
                            ProjectNodeHelper.addTableForTemCatalogOrSchema(catalog, schema, getConnection(), dbtable,
                                    metadataconnection);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            checkConnectionIsDone = true;

            Display.getDefault().syncExec(new Runnable() {

                @Override
                public void run() {
                    if (isCanceled()) {
                        return;
                    }
                    updateUIInThreadIfThread();
                }
            });

        }

        private boolean isSynonymsSchemaExsist(String schema) {
            boolean findFakeSynonymsSchema = false;
            List<Schema> schemasExsit = ConnectionHelper.getSchema(temConnection);
            for (Schema s : schemasExsit) {
                if (s.getName() != null && s.getName().equals(schema)) {
                    findFakeSynonymsSchema = true;
                    break;
                }
            }
            return findFakeSynonymsSchema;
        }

        public void updateUIInThreadIfThread() {
            if (treeItem.isDisposed()) {
                return;
            }

            if (checkConnectionIsDone) {
                treeItem.setText(2, "" + metadataColumns.size()); //$NON-NLS-1$
                // metadataColumns.size() won't be 0 unless SQL execution failed.
                if (metadataColumns.size() > 0) {
                    treeItem.setText(3, Messages.getString("SelectorTableForm.Success")); //$NON-NLS-1$
                } else {
                    treeItem.setText(3, Messages.getString("SelectorTableForm.Failed")); //$NON-NLS-1$
                }
                tableColumnNums.put(treeItem.getText(0), metadataColumns.size());
            } else {
                updateStatus(IStatus.WARNING, Messages.getString("DatabaseTableForm.connectionFailure")); //$NON-NLS-1$
                new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("DatabaseTableForm.connectionFailure"), //$NON-NLS-1$
                        managerConnection.getMessageException());

            }
            refreshColumnData(tableNode, treeItem);
            
            isFinished = true;
            countSuccess++;
            updateStatus(IStatus.OK, null);
            // selectNoneTablesButton.setEnabled(true);
            // checkConnectionButton.setEnabled(true);
            parentWizardPage.setPageComplete(isAllTaskFinished());
        }
    }
    
    private boolean isAllTaskFinished() {
        return threadExecutor.getQueue().isEmpty()
                && (threadExecutor.getActiveCount() == 0 || countSuccess == countPending);
    }

    /**
     * refreshTable. This Methos execute the CreateTable in a Thread task.
     * 
     * @param tableItem
     * @param size
     */
    private void refreshTable(final TreeItem treeItem, final int size) {
        if (threadExecutor == null) {
            return;
        }

        if (!threadExecutor.isThreadRunning(treeItem) && !useProvider()) {
            TableNode node = (TableNode) treeItem.getData();
            if (node.getType() == TableNode.TABLE) {
                IMetadataConnection metadataConn = getIMetadataConnection();
                // Added by Marvin Wang on Mar. 15, 2013 for loading hive jars dynamically, refer to TDI-25072.
                if (EDatabaseTypeName.HIVE.getXmlName().equalsIgnoreCase(metadataConn.getDbType())) {
                    try {
                        HiveConnectionManager.getInstance().checkConnection(metadataConn);
                        RetrieveColumnRunnable runnable = new RetrieveColumnRunnable(treeItem);
                        String value = node.getValue();
                        if (!(isExistingNames(value))) {
                            countPending++;
                            threadExecutor.execute(runnable);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (managerConnection.check(getIMetadataConnection(), true)) {
                        RetrieveColumnRunnable runnable = new RetrieveColumnRunnable(treeItem);
                        String value = node.getValue();
                        if (!(isExistingNames(value))) {
                            countPending++;
                            threadExecutor.execute(runnable);
                        }
                    }
                }
            }
        } else if (useProvider() && !threadExecutor.isThreadRunning(treeItem)) {
            TableNode node = (TableNode) treeItem.getData();
            if (node.getType() == provider.getRunnableAccessNodeType()) {
                RetrieveColumnRunnable runnable = new RetrieveColumnRunnable(treeItem) {

                    @Override
                    public void run() {
                        if (isCanceled()) {
                            return;
                        }
                        // will use executeMethod from extension IDBMetadataExtractor
                        checkConnectionIsDone = true;

                        Display.getDefault().syncExec(new Runnable() {

                            @Override
                            public void run() {
                                provider.executeInRunnable(metadataconnection, treeItem.getData(), getConnection());
                                if (isCanceled()) {
                                    return;
                                }
                                updateUIInThreadIfThread();
                            }
                        });

                    }

                    @Override
                    public void updateUIInThreadIfThread() {
                        if (treeItem.isDisposed()) {
                            return;
                        }

                        if (checkConnectionIsDone) {
                            //                            treeItem.setText(2, "" + metadataColumns.size()); //$NON-NLS-1$
                            treeItem.setText(3, Messages.getString("SelectorTableForm.Success")); //$NON-NLS-1$
                            // tableColumnNums.put(treeItem.getText(0), metadataColumns.size());
                        } else {
                            updateStatus(IStatus.WARNING, Messages.getString("DatabaseTableForm.connectionFailure")); //$NON-NLS-1$
                            new ErrorDialogWidthDetailArea(getShell(), PID,
                                    Messages.getString("DatabaseTableForm.connectionFailure"), //$NON-NLS-1$
                                    managerConnection.getMessageException());

                        }

                        refreshColumnData(tableNode, treeItem);
                        isFinished = true;
                        countSuccess++;
                        updateStatus(IStatus.OK, null);
                        // selectNoneTablesButton.setEnabled(true);
                        // checkConnectionButton.setEnabled(true);

                        parentWizardPage.setPageComplete(isAllTaskFinished());
                    }

                };
                String value = node.getValue();
                if (!(isExistingNames(value))) {
                    countPending++;
                    threadExecutor.execute(runnable);
                }
            }

        } else {
            RetrieveColumnRunnable runnable = threadExecutor.getRunnable(treeItem);
            runnable.setCanceled(false);
        }
    }

    private void clearTableItem(TreeItem item) {
        clearTableItem(item, true);
    }

    private void clearTableItem(TreeItem item, boolean deleteFromConnection) {
        if (item == null) {
            return;
        }
        Object data = item.getData();
        if (data != null && data instanceof TableNode) {
            TableNode node = (TableNode) data;
            if (useProvider()) {
                if (node.getType() == provider.getRunnableAccessNodeType()) {
                    if (deleteFromConnection) {
                        provider.deleteMetadataFromConnection(node, getConnection());
                    }
                    item.setText(2, ""); //$NON-NLS-1$
                    item.setText(3, ""); //$NON-NLS-1$
                }
            } else if (node.getType() == TableNode.TABLE) {
                if (deleteFromConnection) {
                    deleteTable(item);
                }
                item.setText(2, ""); //$NON-NLS-1$
                item.setText(3, ""); //$NON-NLS-1$
            }
            refreshColumnData(node, item);
            RetrieveColumnRunnable runnable = threadExecutor.getRunnable(item);
            if (runnable != null) {
                runnable.setCanceled(true);
            }
        }
    }

    private void clearTableItem(TableNode item, boolean deleteFromConnection) {
        if (item == null) {
            return;
        }
        if (item != null && item instanceof TableNode) {
            TableNode node = item;
            if (node.getType() == TableNode.TABLE) {
                if (deleteFromConnection) {
                    deleteTable(item);
                }
            }
        }
    }

    /**
     * DOC ocarbone Comment method "initExistingNames".
     * 
     * @param connection
     * @param metadataTable
     */
    private synchronized void initExistingNames() {
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
                viewer.refresh();
            }

        });
    }

    private void refreshItem(TreeItem item) {
        if (item != null && item.getData() != null) {
            int type = ((TableNode) item.getData()).getType();
            if (type == TableNode.TABLE) {
                item.setChecked(false);
                if (item.getData() != null) {
                    TableNode node = (TableNode) item.getData();
                    if (isExistTable(node)) {
                        item.setChecked(true);
                        Integer num = tableColumnNums.get(item.getText(0));
                        if (num != null) {
                            // get column num from previous result
                            item.setText(2, num.toString());
                            item.setText(3, Messages.getString("SelectorTableForm.Success")); //$NON-NLS-1$ 
                            refreshColumnData(node, item);
                        } else {
                            // retrieve column num again
                            refreshTable(item, -1);
                        }
                    }
                }
            }
        }
    }

    private void refreshExistItem(final MetadataTable existTable, final TreeItem item) {
        Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {
                orgomg.cwm.objectmodel.core.Package pack = (orgomg.cwm.objectmodel.core.Package) existTable.eContainer();
                boolean confirm = MessageDialog.openConfirm(Display.getDefault().getActiveShell(), "Confirm", "Another '"
                        + existTable.getLabel() + "' already exist in \"" + pack.getName() + "\", do you want to cover it?");
                if (confirm) {
                    TreeItem existItem = getExistItem(existTable);
                    TableNode existTableItem = getExistCheckedTable((TableNode) item.getData());
                    if (existItem == null) { // if the TreeItem has not been expanded,the existItem will always be
                                             // null,so will find the same name table by model
                        if (existTableItem == null) {
                            return;
                        } else {
                            clearTableItem(existTableItem, true);
                        }
                    } else {
                        clearTableItem(existItem);
                        existItem.setChecked(false);
                    }
                    item.setText(3, Messages.getString("SelectorTableForm.Pending")); //$NON-NLS-1$
                    refreshColumnData(existTableItem, item);
                    parentWizardPage.setPageComplete(false);
                    refreshTable(item, -1);
                    item.setChecked(true);
                } else {
                    item.setChecked(false);
                    boolean hasCheckedItem = false;
                    TreeItem parentItem = item.getParentItem();
                    if (parentItem != null) {
                        for (TreeItem i : parentItem.getItems()) {
                            if (i.getChecked()) {
                                hasCheckedItem = true;
                                break;
                            }
                        }
                    }
                    if (!hasCheckedItem && parentItem != null) {
                        parentItem.setChecked(false);
                    }
                }
            }
        });
    }

    private MetadataTable getExistTable(String name) {
        if (name != null) {
            for (Object obj : ConnectionHelper.getTables(getConnection())) {
                if (obj == null) {
                    continue;
                }
                MetadataTable table = (MetadataTable) obj;
                if (table.getLabel().equals(name)) {
                    return table;
                }
            }
        }
        return null;
    }

    private TreeItem getExistItem(MetadataTable table) {
        if (!tree.isDisposed() && table != null && table.eContainer() != null) {
            String name = ((orgomg.cwm.objectmodel.core.Package) table.eContainer()).getName();
            TreeItem[] items = tree.getItems();
            for (TreeItem treeItem : items) {
                if (treeItem.getData() != null) {
                    int type = ((TableNode) treeItem.getData()).getType();
                    if (type == TableNode.CATALOG) {
                        for (TreeItem item : treeItem.getItems()) {
                            if (item.getData() != null) {
                                int t = ((TableNode) item.getData()).getType();
                                if (t == TableNode.SCHEMA) {
                                    for (TreeItem i : item.getItems()) {
                                        if (i.getText(0).equals(table.getLabel()) && i.getChecked()) {
                                            if (item.getText(0).equals(name)) {
                                                String parentName = ((orgomg.cwm.objectmodel.core.Package) table.eContainer()
                                                        .eContainer()).getName();
                                                if (parentName.equals(treeItem.getText(0))) {
                                                    return i;
                                                }
                                            }
                                        }
                                    }
                                } else if (t == TableNode.TABLE) {
                                    if (item.getText(0).equals(table.getLabel()) && treeItem.getText(0).equals(name)
                                            && item.getChecked()) {
                                        return item;
                                    }
                                }
                            }
                        }
                    } else if (type == TableNode.SCHEMA) {
                        for (TreeItem item : treeItem.getItems()) {
                            if (item.getText(0).equals(table.getLabel()) && treeItem.getText(0).equals(name) && item.getChecked()) {
                                return item;
                            }
                        }
                    }
                }
            }
        }
        return null;

    }

    /**
     * 
     * wzhang Comment method "isExistTable".
     * 
     * @param tableNode
     * @return
     */
    private boolean isExistTable(TableNode tableNode) {
        if (tableNode != null && tableNode.getType() == TableNode.TABLE) {
            TableNode parent = tableNode.getParent();
            if (parent == null) {
                for (Object obj : ConnectionHelper.getTables(getConnection())) {
                    if (obj == null) {
                        continue;
                    }
                    MetadataTable table = (MetadataTable) obj;
                    if (table.getLabel().equals(tableNode.getValue())) {
                        return true;
                    }
                }
            } else {
                int type = parent.getType();
                EList<ModelElement> ownedElement = null;
                if (type == TableNode.CATALOG) {
                    Catalog c = (Catalog) ConnectionHelper.getPackage(parent.getValue(), getConnection(), Catalog.class);
                    if (c != null) {
                        ownedElement = c.getOwnedElement();
                    }
                } else if (type == TableNode.SCHEMA) {
                    TableNode p = parent.getParent();
                    Schema s = null;
                    if (p == null) {
                        s = (Schema) ConnectionHelper.getPackage(parent.getValue(), getConnection(), Schema.class);
                    } else {
                        Catalog c = (Catalog) ConnectionHelper.getPackage(p.getValue(), getConnection(), Catalog.class);
                        if (c != null) {
                            List<Schema> schemas = CatalogHelper.getSchemas(c);
                            for (Schema schema : schemas) {
                                if (schema.getName().equals(parent.getValue())) {
                                    s = schema;
                                    break;
                                }
                            }
                        }
                    }
                    if (s != null) {
                        ownedElement = s.getOwnedElement();
                    }
                }
                if (ownedElement != null) {
                    if (isContainModelElement(ownedElement, tableNode.getValue())) {
                        return true;
                    }
                } else {
                    for (Object obj : ConnectionHelper.getTables(getConnection())) {
                        if (obj == null) {
                            continue;
                        }
                        MetadataTable table = (MetadataTable) obj;
                        if (table.getLabel().equals(tableNode.getValue())) {
                            return true;
                        }
                    }
                }
                if (ETableTypes.TABLETYPE_SYNONYM.getName().equals(tableNode.getItemType())) {
                    // synonym table may also be in schema MetadataConnectionUtils.FAKE_SCHEMA_SYNONYMS if user
                    // checked "All synonyms" when retrieving schema last time, so need to check too.
                    Schema s = (Schema) ConnectionHelper.getPackage(MetadataConnectionUtils.FAKE_SCHEMA_SYNONYMS,
                            getConnection(), Schema.class);
                    if (s != null) {
                        ownedElement = s.getOwnedElement();
                        if (isContainModelElement(ownedElement, tableNode.getValue())) {
                            return true;
                        }
                    }
                }
            }
        } else if (useProvider()) {
            return provider.isMetadataExsit(tableNode, getConnection());
        }
        return false;
    }

    private boolean isContainModelElement(EList<ModelElement> ownedElement, String tableName) {
        if (ownedElement == null) {
            return false;
        }
        for (ModelElement m : ownedElement) {
            if (m instanceof MetadataTable) {
                String label = ((MetadataTable) m).getName();
                if (label.equals(tableName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private TableNode transferTableToTableNode(MetadataTable tableItem, TableNode parentNode) {
        TableNode existTableNode = null;
        if (tableItem instanceof TdTable) {
            TdTable td = (TdTable) tableItem;
            existTableNode = new TableNode();
            existTableNode.setType(TableNode.TABLE);
            existTableNode.setValue(td.getLabel());
            existTableNode.setItemType(td.getTableType());
            existTableNode.setTable(td);
            if (parentNode != null) {
                existTableNode.setParent(parentNode);
                parentNode.addChild(existTableNode);
            }
        } else if (tableItem instanceof TdView) {
            TdView tv = (TdView) tableItem;
            existTableNode = new TableNode();
            existTableNode.setType(TableNode.TABLE);
            existTableNode.setValue(tv.getLabel());
            existTableNode.setItemType(tv.getTableType());
            existTableNode.setView(tv);
            if (parentNode != null) {
                existTableNode.setParent(parentNode);
                parentNode.addChild(existTableNode);
            }
        }
        return existTableNode;
    }

    /**
     * 
     * ldong Comment method "getExistCheckedTable".
     * 
     * @param tableNode
     * @return
     */
    private TableNode getExistCheckedTable(TableNode tableNode) {
        if (tableNode != null && tableNode.getType() == TableNode.TABLE) {
            EList<ModelElement> ownedElement = null;
            List<TableNode> tableNodes = getTableNodeInfo();
            TableNode parent = tableNode.getParent();
            if (parent != null) {
                int type = parent.getType();
                if (type == TableNode.CATALOG) {
                    for (TableNode catalogNode : tableNodes) { // two level catalog->table find same name table in
                                                               // different
                                                               // catalog
                        if (catalogNode.getType() == type) {
                            if (!catalogNode.getValue().equals(parent.getValue())) {
                                if (catalogNode.getCatalog() != null) {
                                    ownedElement = catalogNode.getCatalog().getOwnedElement();
                                    for (ModelElement m : ownedElement) {
                                        if (m instanceof MetadataTable) {
                                            String label = ((MetadataTable) m).getLabel();
                                            if (label.equals(tableNode.getValue())) {
                                                if (((CatalogImpl) m.eContainer()).getName().equals(catalogNode.getValue())) {
                                                    return transferTableToTableNode((MetadataTable) m, catalogNode);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else if (type == TableNode.SCHEMA) { // two level schema->table find same name table in different
                                                       // schema
                    TableNode p = parent.getParent();
                    if (p == null) {
                        for (TableNode schemaNode : tableNodes) {
                            if (schemaNode.getType() == type) {
                                if (!schemaNode.getValue().equals(parent.getValue())) {
                                    if (schemaNode.getSchema() != null) {
                                        ownedElement = schemaNode.getSchema().getOwnedElement();
                                        for (ModelElement m : ownedElement) {
                                            if (m instanceof MetadataTable) {
                                                String label = ((MetadataTable) m).getLabel();
                                                if (label.equals(tableNode.getValue())) {
                                                    if (((SchemaImpl) m.eContainer()).getName().equals(schemaNode.getValue())) {
                                                        return transferTableToTableNode((MetadataTable) m, schemaNode);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else { // three level catalog->schema->table
                        for (TableNode catalogNode : tableNodes) {
                            if (catalogNode.getType() == TableNode.CATALOG) {
                                if (catalogNode.getCatalog() != null) {
                                    if (catalogNode.getValue().equals(p.getValue())) { // same catalog,different Schema
                                        List<Schema> schemas = CatalogHelper.getSchemas(catalogNode.getCatalog());
                                        for (Schema schs : schemas) {
                                            ownedElement = schs.getOwnedElement();
                                            for (ModelElement m : ownedElement) {
                                                if (m instanceof MetadataTable) {
                                                    String label = ((MetadataTable) m).getLabel();
                                                    if (label.equals(tableNode.getValue())) {
                                                        List<TableNode> schemaNodeList = catalogNode.getChildren();
                                                        for (TableNode parentSchema : schemaNodeList) {
                                                            if (((SchemaImpl) m.eContainer()).getName().equals(
                                                                    parentSchema.getValue())
                                                                    && !parentSchema.getValue().equals(parent.getValue())) {
                                                                return transferTableToTableNode((MetadataTable) m, parentSchema);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        List<Schema> schemas = CatalogHelper.getSchemas(catalogNode.getCatalog());
                                        for (Schema schs : schemas) {
                                            ownedElement = schs.getOwnedElement();
                                            for (ModelElement m : ownedElement) {
                                                if (m instanceof MetadataTable) {
                                                    String label = ((MetadataTable) m).getLabel();
                                                    if (label.equals(tableNode.getValue())) {
                                                        List<TableNode> schemaNodeList = catalogNode.getChildren();
                                                        for (TableNode parentSchema : schemaNodeList) {
                                                            if (((SchemaImpl) m.eContainer()).getName().equals(
                                                                    parentSchema.getValue())) {
                                                                return transferTableToTableNode((MetadataTable) m, parentSchema);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else { // no catalog,no schema,such as Access
            for (Object obj : ConnectionHelper.getTables(getConnection())) {
                if (obj == null) {
                    continue;
                }
                MetadataTable table = (MetadataTable) obj;
                if (table.getLabel().equals(tableNode.getValue())) {
                    return transferTableToTableNode(table, null);
                }
            }
        }
        return null;
    }

    /**
     * DOC hcw Comment method "restoreCheckItems".
     */
    protected void restoreCheckItems() {
        Set<String> checkedItems = new HashSet<String>();
        for (Object obj : ConnectionHelper.getTables(getConnection())) {
            if (obj == null) {
                continue;
            }
            MetadataTable table = (MetadataTable) obj;
            checkedItems.add(table.getLabel());
        }
        for (TreeItem treeItem : tree.getItems()) {
            if (treeItem.getData() != null) {
                int type = ((TableNode) treeItem.getData()).getType();
                if (type == TableNode.CATALOG) {
                    for (TreeItem item : treeItem.getItems()) {
                        if (item.getData() != null) {
                            int t = ((TableNode) item.getData()).getType();
                            if (t == TableNode.SCHEMA) {
                                for (TreeItem i : item.getItems()) {
                                    refreshItem(i);
                                }
                            } else if (t == TableNode.TABLE) {
                                refreshItem(item);
                            }
                        }
                    }
                } else if (type == TableNode.SCHEMA) {
                    for (TreeItem item : treeItem.getItems()) {
                        refreshItem(item);
                    }
                } else if (type == TableNode.TABLE) {
                    refreshItem(treeItem);
                } else if (type == TableNode.COLUMN_FAMILY) {
                    refreshItem(treeItem);
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
        if (!tableNodeList.isEmpty()) {
            threadExecutor = new CustomThreadPoolExecutor(5, new ThreadPoolExecutor.CallerRunsPolicy());
        }
    }

    public void initControlData(boolean flag) {
        checkConnection(flag);
        checkTreeRoot();

        if (tableNodeList != null && tableNodeList.size() > 0) {
            threadExecutor = new CustomThreadPoolExecutor(5, new ThreadPoolExecutor.CallerRunsPolicy());
        }
    }

    /**
     * Check if root node is Table_Type, then judge if each node has been retrieved or not. DOC JKWANG Comment method
     * "checkTreeRoot".
     */
    private void checkTreeRoot() {

        TableNode node = null;

        for (TreeItem treeItem : this.tree.getItems()) {

            node = (TableNode) treeItem.getData();

            if (node.getType() == TableNode.TABLE) {

                if (this.isExistTable(node)) {

                    treeItem.setChecked(true);

                } else {

                    treeItem.setChecked(false);

                }
            }
        }
    }

    protected DatabaseConnection getConnection() {
        if (temConnection != null) {
            return temConnection;
        } else {
            if (forTemplate) {
                return (DatabaseConnection) templateConntion.getConnection();
            } else {
                return (DatabaseConnection) connectionItem.getConnection();
            }
        }
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
        return this.metadataconnection;
    }

    public void setIMetadataConnection(IMetadataConnection metadataConnection) {
        this.metadataconnection = metadataConnection;
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

    public Set<String> getCheckedItem() {
        Set<String> checkedItems = new HashSet<String>();
        for (Object obj : ConnectionHelper.getTables(getConnection())) {
            if (obj == null) {
                continue;
            }
            MetadataTable table = (MetadataTable) obj;
            checkedItems.add(table.getLabel());
        }
        return checkedItems;
    }

    public List<TableNode> getTableNodeList() {
        return this.tableNodeList;
    }

    public String getNameFilter() {
        return this.nameFilter.getText().trim();
    }

    class checkStateProvider implements ICheckStateProvider {

        @Override
        public boolean isChecked(Object element) {
            if (element instanceof TableNode) {
                TableNode node = (TableNode) element;
                if (node.getType() == TableNode.TABLE) {
                    if (isExistTable(node)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            return false;

        }

        @Override
        public boolean isGrayed(Object element) {
            return false;
        }
    }

    /**
     * 
     * wzhang SelectorTableForm class global comment. Detailled comment
     */
    class selectorViewerFilter extends ViewerFilter {

        @Override
        public boolean select(Viewer viewer, Object parentElement, Object element) {
            String pattern = getNameFilter();
            SearchPattern matcher = new SearchPattern();
            matcher.setPattern(pattern);
            if (element instanceof TableNode) {
                TableNode node = (TableNode) element;
                if (node.getType() == TableNode.TABLE) {
                    if (!matcher.matches(node.getValue())) {
                        return false;
                    }
                }
            }
            return true;
        }

    }

    public List<TableNode> filterItemContextSchema(List<TableNode> tableList) {
        List<TableNode> allDisplayNodes = tableList;
        TableNode contextItemNode = null;
        for (TableNode tn : allDisplayNodes) {
            if (tn.getSchema() instanceof Schema) {
                Schema s = tn.getSchema();
                if ((s.getName().equals("") || s.getName().contains("context")) && s.getOwnedElement().size() > 0) {
                    contextItemNode = tn;
                }
            }
        }
        if (contextItemNode != null) {
            allDisplayNodes.remove(contextItemNode);
        }
        return allDisplayNodes;
    }

    protected static void refreshColumnData(TableNode tableNode, TreeItem item) {
        if (tableNode == null || item == null) {
            return;
        }
        List<Object> columnDatas = tableNode.getColumnDataList();
        if (columnDatas == null) {
            return;
        }
        columnDatas.clear();
        columnDatas.add(item.getText(0));
        columnDatas.add(item.getText(1));
        columnDatas.add(item.getText(2));
        columnDatas.add(item.getText(3));
    }
}
