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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorNotModifiable.LAYOUT_MODE;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledCombo;
import org.talend.commons.ui.swt.formtools.LabelledText;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.utils.data.list.IListenableListListener;
import org.talend.commons.utils.data.list.ListenableListEvent;
import org.talend.commons.utils.data.text.IndiceHelper;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.MetadataToolHelper;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase;
import org.talend.core.model.metadata.builder.database.TableInfoParameters;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.PerlTypesManager;
import org.talend.core.model.metadata.types.TypesManager;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.repository.IRepositoryPrefConstants;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.core.ui.metadata.editor.MetadataEmfTableEditor;
import org.talend.core.ui.metadata.editor.MetadataEmfTableEditorView;
import org.talend.core.utils.CsvArray;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.TableHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.metadata.managment.repository.ManagerConnection;
import org.talend.metadata.managment.ui.model.ProjectNodeHelper;
import org.talend.metadata.managment.ui.wizard.AbstractForm;
import org.talend.metadata.managment.ui.wizard.metadata.connection.GuessSchemaUtil;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * @author ocarbone
 *
 */
/**
 * DOC Administrator class global comment. Detailled comment <br/>
 *
 */
public class DatabaseTableForm extends AbstractForm {

    protected static Logger log = Logger.getLogger(DatabaseTableForm.class);

    protected static final String PID = CoreRuntimePlugin.PLUGIN_ID;

    /**
     * FormTable Settings.
     */
    private static final int WIDTH_GRIDDATA_PIXEL = 750;

    private static final boolean STREAM_DETACH_IS_VISIBLE = false;

    private static final String GUESS_SCHEMA_TOOLTIP = Messages.getString("DatabaseTableForm.getRoughSchema"); //$NON-NLS-1$

    private static final String RETRIEVE_SCHEMA_TOOLTIP = Messages.getString("DatabaseTableForm.getDetailedSchema"); //$NON-NLS-1$

    /**
     * FormTable Var.
     */
    private ManagerConnection managerConnection;

    private List<String> itemTableName;

    private MetadataTable metadataTable;

    private MetadataEmfTableEditor metadataEditor;

    private MetadataEmfTableEditorView tableEditorView;

    /**
     * Buttons.
     */
    private UtilsButton retreiveSchemaButton;

    // private UtilsButton checkConnectionButton;

    private UtilsButton guessSchemaButton; // hyWang add

    /**
     * Anothers Fields.
     */
    private String tableString;

    private Label tableSettingsInfoLabel;

    private Label typeText;

    /**
     * Main Fields.
     */
    private LabelledText nameText;

    private LabelledText commentText;

    private LabelledCombo tableCombo;

    private Button button;

    private Button streamDetachCheckbox;

    /**
     * Flag.
     */

    boolean readOnly;

    private boolean creation;

    private TableViewerCreator tableViewerCreator;

    private Table tableNavigator;

    private UtilsButton addTableButton;

    private UtilsButton removeTableButton;

    private final IWizardPage parentWizardPage;

    private String editSchemaTableName;

    private DatabaseConnection temConnection;

    private Map<String, Map<String, String>> labelChanged;

    private List<MetadataTable> initTables;

    /**
     * TableForm Constructor to use by RCP Wizard.
     *
     * @param parent
     * @param connection
     * @param page
     * @param metadataTable
     * @param page
     * @param managerConnection2
     */
    public DatabaseTableForm(Composite parent, ConnectionItem connectionItem, MetadataTable metadataTable,
            ManagerConnection managerConnection, IWizardPage page, DatabaseConnection temConnection,
            IMetadataConnection metadataconnection) {
        super(parent, SWT.NONE);
        this.managerConnection = managerConnection;
        this.connectionItem = connectionItem;
        this.parentWizardPage = page;
        this.temConnection = temConnection;
        this.metadataTable = metadataTable;
        this.metadataconnection = metadataconnection;
        final Set<MetadataTable> tables = ConnectionHelper.getTables(temConnection);
        for (MetadataTable t : tables) {
            if ((metadataTable != null && t.getLabel().equals(metadataTable.getLabel())) || metadataTable == null) {
                this.metadataTable = t;
                break;
            }
        }
        this.typeName = EDatabaseTypeName.getTypeFromDbType(metadataconnection.getDbType());
        /* use provider for the databse didn't use JDBC,for example: HBase */
        if (typeName != null && typeName.isUseProvider()) {
            this.provider = ExtractMetaDataFromDataBase.getProviderByDbType(metadataconnection.getDbType());
        }
        labelChanged = new HashMap<String, Map<String, String>>();
        setupForm();
    }

    public DatabaseTableForm(Composite parent, ConnectionItem connectionItem, MetadataTable metadataTable,
            ManagerConnection managerConnection, IWizardPage page, DatabaseConnection temConnection,
            IMetadataConnection metadataconnection, boolean creation) {
        this(parent, connectionItem, metadataTable, managerConnection, page, temConnection, metadataconnection);
        this.creation = creation;
    }

    /**
     * DOC ocarbone Comment method "initExistingNames".
     *
     * @param connection
     * @param metadataTable
     *
     */
    private void initExistingNames() {
        String[] exisNames;
        if (metadataTable != null) {
            exisNames = TableHelper.getTableNames(getConnection(), metadataTable.getLabel());
        } else {
            exisNames = TableHelper.getTableNames(getConnection());
        }
        this.existingNames = existingNames == null ? Collections.EMPTY_LIST : Arrays.asList(exisNames);
    }

    /**
     *
     * Initialize value, forceFocus first field for right Click (new Table).
     *
     */
    @Override
    public void initialize() {

    }

    /**
     *
     * Initialize value, forceFocus first field for right Click (new Table).
     *
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     *
     */
    public void initializeForm() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

        // init the nodes of the left tree navigator
        initTreeNavigatorNodes();
        initMetadataForm();
        checkDefaultValue();
        if (useProvider()) {
            guessSchemaButton.setVisible(provider.isSupportGuessSchema());
            retreiveSchemaButton.setVisible(provider.isSupportRetrieveSchema());
        }
    }

    /**
     * DOC PLV Comment method "checkDefaultValue".
     */
    private void checkDefaultValue() {
        if (metadataTable != null) {
            EList<MetadataColumn> columns = metadataTable.getColumns();
            for (MetadataColumn column : columns) {
                String defaultValue = column.getDefaultValue();
                if (!StringUtils.isEmpty(defaultValue)) {
                    IPreferenceStore preferenceStore = CoreUIPlugin.getDefault().getPreferenceStore();
                    String preKey = "DatabaseTableForm.defaultValueShowAgain"; //$NON-NLS-1$
                    if (!preferenceStore.getBoolean(preKey)) {
                        MessageDialogWithToggle dialog = new MessageDialogWithToggle(this.getShell(), "", getBackgroundImage(), //$NON-NLS-1$
                                Messages.getString("DatabaseTableForm.checkDefaultValue"), //$NON-NLS-1$
                                MessageDialog.INFORMATION, new String[] { IDialogConstants.OK_LABEL }, 0,
                                Messages.getString("DatabaseTableForm.doNotShowMessage"), //$NON-NLS-1$
                                false);
                        dialog.setPrefKey(preKey);
                        dialog.setPrefStore(preferenceStore);
                        dialog.open();
                        preferenceStore.setValue(preKey, dialog.getToggleState());
                    }
                    break;
                }
            }
        }
    }

    /**
     * DOC ocarbone Comment method "initTreeNodes".
     *
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    private void initTreeNavigatorNodes() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            SQLException {
        initTables = ConnectionHelper.getTablesWithOrders(getConnection());

        if (initTables != null && !initTables.isEmpty()) {
            boolean isAllDeleted = true;
            for (int i = 0; i < initTables.size(); i++) {
                if (!TableHelper.isDeleted((MetadataTable) initTables.toArray()[i])) {
                    if ((metadataTable == null) || creation) {
                        metadataTable = (MetadataTable) initTables.toArray()[i];
                    }
                    isAllDeleted = false;
                }
            }
            if (isAllDeleted) {
                addMetadataTable(true);
            }
        } else {
            addMetadataTable(false);
        }

        tableNavigator.removeAll();

        List<String> tablenames = new ArrayList<String>();
        for (MetadataTable t : initTables) {
            tablenames.add(t.getLabel());
        }

        String[] allTableLabel = tablenames.toArray(new String[0]);
        Arrays.sort(allTableLabel);

        // update metadataTable
        if (metadataTable != null && initTables != null && !initTables.isEmpty()
                && !tablenames.contains(metadataTable.getLabel())) {
            int size = initTables.size();
            if (size >= 1) {
                String tableName = null;
                for (MetadataTable t : initTables) {
                    if (t.getLabel().equals(allTableLabel[0])) {
                        tableName = t.getLabel();
                        break;
                    }
                }
                for (Object obj : ConnectionHelper.getTables(getConnection())) {
                    if (obj instanceof MetadataTable) {
                        if (((MetadataTable) obj).getLabel().equals(tableName)) {
                            metadataTable = (MetadataTable) obj;
                        }
                    }
                }
            }
        }

        for (String element : allTableLabel) {
            if (element.equals(metadataTable.getLabel())) {
                TableItem subItem = new TableItem(tableNavigator, SWT.NONE);
                subItem.setText(element);
                tableNavigator.setSelection(subItem);
            } else /* if (!TableHelper.isDeleted(TableHelper.findByLabel(getConnection(), allTableLabel[i]))) */{
                TableItem subItem = new TableItem(tableNavigator, SWT.NULL);
                subItem.setText(element);
            }
        }
    }

    // en Character
    private boolean isCnorEn(String word) {
        boolean sign = true;
        for (int i = 0; i < word.length(); i++) {
            if (!(word.charAt(i) >= 0x0000 && word.charAt(i) <= 0x00FF)) {
                sign = false;
                break;
            }
        }
        return sign;
    }

    /**
     * DOC ocarbone Comment method "initMetadataForm".
     */
    private void initMetadataForm() {
        // init the metadata Table

        metadataEditor.setMetadataTable(metadataTable);

        Boolean flag = CoreUIPlugin.getDefault().getPreferenceStore()
                .getBoolean(IRepositoryPrefConstants.ALLOW_SPECIFIC_CHARACTERS_FOR_SCHEMA_COLUMNS);
        if (!flag.booleanValue()) {
            List<MetadataColumn> list = metadataEditor.getMetadataColumnList();
            for (MetadataColumn column : list) {
                if (!isCnorEn(column.getLabel())) {
                    String label = metadataEditor.getNextGeneratedColumnName("newColumn"); //$NON-NLS-1$
                    column.setLabel(label);
                }
            }
        }

        removeDoubleQuotes(metadataEditor.getMetadataColumnList());

        tableEditorView.setMetadataEditor(metadataEditor);
        tableEditorView.getTableViewerCreator().layout();

        // add listener to tableMetadata (listen the event of the toolbars)
        metadataEditor.addAfterOperationListListener(new IListenableListListener() {

            @Override
            public void handleEvent(ListenableListEvent event) {
                changeTableNavigatorStatus(checkFieldsValue());
            }
        });

        // init the fields
        // nameText.setText(MetadataToolHelper.validateValue(metadataTable.getLabel()));
        nameText.setText(MetadataToolHelper.validateTableName(metadataTable.getLabel()));
        commentText.setText(metadataTable.getComment());
        if (metadataTable.getTableType() != null) {
            typeText.setText(Messages.getString("DatabaseTableForm.type", metadataTable.getTableType())); //$NON-NLS-1$
        } else {
            typeText.setText(Messages.getString("DatabaseTableForm.typeTable")); //$NON-NLS-1$
        }
        String sourceName = metadataTable.getName();
        // tableCombo.setReadOnly(sourceName != null);
        tableCombo.setText(sourceName);
        updateRetreiveSchemaButton();
        nameText.forceFocus();
    }

    /**
     * DOC bqian Comment method "removeDoubleQuotes".
     *
     * @param metadataColumnList see bug 3738
     */
    private void removeDoubleQuotes(List<MetadataColumn> metadataColumnList) {
        for (MetadataColumn metadataColumn : metadataColumnList) {
            handleDefaultValue(metadataColumn);
        }

    }

    private void refreshTableComboItem() {
        ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(this.getShell());
        IRunnableWithProgress runnable = new IRunnableWithProgress() {

            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                try {
                    monitor.beginTask("", IProgressMonitor.UNKNOWN); //$NON-NLS-1$
                    if (!monitor.isCanceled()) {

                        TableInfoParameters parameter = new TableInfoParameters();
                        List<String> comboTableNames;
                        try {
                            if (useProvider()) {
                                comboTableNames = provider.returnTablesFormConnection(metadataconnection);
                            } else {
                                comboTableNames = ExtractMetaDataFromDataBase.returnTablesFormConnection(metadataconnection,
                                        parameter);
                            }
                        } catch (Exception e) {
                            comboTableNames = new ArrayList<String>();
                        }
                        if (comboTableNames != null && !comboTableNames.isEmpty()) {
                            int size = comboTableNames.size();
                            if (size > LabelledCombo.MAX_VISIBLE_ITEM_COUNT) {
                                size = LabelledCombo.MAX_VISIBLE_ITEM_COUNT;
                            }
                            final List<String> items = comboTableNames;
                            final int count = size;
                            Display.getDefault().syncExec(new Runnable() {

                                @Override
                                public void run() {
                                    tableCombo.setVisibleItemCount(count);
                                    tableCombo.removeAll();
                                    boolean selected = false;
                                    for (int i = 0; i < items.size(); i++) {
                                        tableCombo.add(items.get(i));
                                        if (items.get(i).equals(metadataTable.getName())) {
                                            tableCombo.select(i);
                                            selected = true;
                                        }
                                    }
                                    if (!selected) {
                                        tableCombo.select(0);
                                    }
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                } finally {
                    monitor.done();
                }
            }
        };
        try {
            progressDialog.run(true, true, runnable);
        } catch (InvocationTargetException e) {
            ExceptionHandler.process(e);
        } catch (InterruptedException e) {
            ExceptionHandler.process(e);
        }

    }

    /**
     * Adds double quotes if Talend type is Date or String.
     *
     * @param bean
     * @param value
     * @return
     */
    private void handleDefaultValue(MetadataColumn bean) {
        // Checks if Talend type is String or Date.
        switch (LanguageManager.getCurrentLanguage()) {
        case JAVA:
            String returnValue = bean.getDefaultValue();
            if (bean.getTalendType().equals(JavaTypesManager.STRING.getId())
                    || bean.getTalendType().equals(JavaTypesManager.DATE.getId())) {
                if (returnValue == null || returnValue.length() == 0) {
                    returnValue = null;
                } else if (returnValue.equalsIgnoreCase("null")) { //$NON-NLS-1$
                    returnValue = "null"; //$NON-NLS-1$
                }
                bean.setDefaultValue(returnValue);
            } else if (bean.getTalendType().equals(JavaTypesManager.BOOLEAN.getId())) {
                String returnBoolean = TalendQuoteUtils.removeQuotes(returnValue);
                // modified by nma, there maybe null pb.
                if (returnBoolean != null && returnBoolean.length() > 0 && returnBoolean.getBytes()[0] == 1) {
                    returnValue = TalendQuoteUtils.addQuotes("1"); //$NON-NLS-1$
                    bean.setDefaultValue(returnValue);
                }
            }
        default:
            // if (bean.getTalendType() != null && bean.getTalendType().equals("string")
            // || bean.getTalendType().equals("date")) {
            // if (returnValue == null) {
            // returnValue = "null";
            // } else if (returnValue.length() == 0) {
            // returnValue = "\"" + "\"";
            // } else if (returnValue.equalsIgnoreCase("null")) {
            // returnValue = "null";
            // } else {
            // returnValue = returnValue.replaceAll("\"", "");
            // returnValue = returnValue.replaceAll("\'", "");
            // returnValue = "\"" + returnValue + "\"";
            // }
            // }
        }
    }

    @Override
    protected void addFields() {
        int leftCompositeWidth = 125;
        int rightCompositeWidth = WIDTH_GRIDDATA_PIXEL - leftCompositeWidth;
        int headerCompositeHeight = 80;
        int tableSettingsCompositeHeight = 15;
        int tableCompositeHeight = 200;

        int height = headerCompositeHeight + tableSettingsCompositeHeight + tableCompositeHeight;

        // Main Composite : 2 columns
        Composite mainComposite = Form.startNewDimensionnedGridLayout(this, 2, WIDTH_GRIDDATA_PIXEL, height);
        mainComposite.setLayout(new GridLayout(2, false));
        GridData gridData = new GridData(GridData.FILL_BOTH);
        mainComposite.setLayoutData(gridData);

        // bug 17422 fixed
        SashForm sash = new SashForm(mainComposite, SWT.NONE);
        GridData sashData = new GridData(GridData.FILL_BOTH);
        sash.setLayoutData(sashData);

        Composite leftComposite = Form.startNewDimensionnedGridLayout(sash, 1, leftCompositeWidth, height);
        Composite rightComposite = Form.startNewDimensionnedGridLayout(sash, 1, rightCompositeWidth, height);
        // in the proportion of 1:4
        sash.setWeights(new int[] { 1, 4 });

        addTreeNavigator(leftComposite, leftCompositeWidth, height);

        gridData = new GridData(SWT.FILL, SWT.BOTTOM, true, false);
        gridData.widthHint = rightCompositeWidth;
        gridData.horizontalSpan = 4;

        // Header Fields
        Composite composite1 = Form.startNewDimensionnedGridLayout(rightComposite, 4, SWT.DEFAULT, SWT.DEFAULT, false);
        ((GridData) composite1.getLayoutData()).verticalAlignment = GridData.VERTICAL_ALIGN_CENTER;
        nameText = new LabelledText(composite1, Messages.getString("DatabaseTableForm.name"), 3); //$NON-NLS-1$
        commentText = new LabelledText(composite1, Messages.getString("DatabaseTableForm.comment"), 3); //$NON-NLS-1$

        typeText = new Label(composite1, SWT.NONE);
        typeText.setLayoutData(gridData);

        GridData gridData1 = new GridData(GridData.FILL_HORIZONTAL);
        gridData1.horizontalSpan = 4;

        Composite container = Form.startNewGridLayout(composite1, 4, false, SWT.CENTER, SWT.TOP);
        container.setLayoutData(gridData1);

        // Combo Table
        // Composite comboComposite = Form.startNewGridLayout(rightComposite, 3, false, SWT.LEFT, SWT.TOP);
        tableCombo = new LabelledCombo(container, Messages.getString("DatabaseTableForm.table"), Messages //$NON-NLS-1$
                        .getString("DatabaseTableForm.tableTip"), //$NON-NLS-1$
                itemTableName != null ? itemTableName.toArray(new String[0]) : null, 1, true, SWT.NONE);
        tableCombo.setEnabled(false);

        button = new Button(container, SWT.PUSH);
        button.setImage(ImageProvider.getImage(EImage.REFRESH_ICON));
        button.setToolTipText(Messages.getString("DatabaseTableForm.refresh.text")); //$NON-NLS-1$

        // Button retreiveSchema
        Composite compositeRetreiveSchemaButton = Form.startNewGridLayout(container, 3, false, SWT.CENTER, SWT.TOP);

        GC gc = new GC(compositeRetreiveSchemaButton);

        String displayStr = Messages.getString("DatabaseTableForm.retreiveSchema"); //$NON-NLS-1$
        Point buttonSize = gc.stringExtent(displayStr);
        retreiveSchemaButton = new UtilsButton(compositeRetreiveSchemaButton, displayStr, buttonSize.x + 12, HEIGHT_BUTTON_PIXEL);
        retreiveSchemaButton.setToolTipText(RETRIEVE_SCHEMA_TOOLTIP);
        // // Button Check Connection
        //        checkConnectionButton = new UtilsButton(compositeRetreiveSchemaButton, "" //$NON-NLS-1$
        // /*
        // * Messages.getString( "DatabaseTableForm.checkConnection" )
        //                 */, false); //$NON-NLS-1$

        tableSettingsInfoLabel = new Label(composite1, SWT.NONE);
        tableSettingsInfoLabel.setLayoutData(gridData);

        // Button guessSchema
        displayStr = Messages.getString("DatabaseTableForm.guessSchema"); //$NON-NLS-1$
        buttonSize = gc.stringExtent(displayStr);
        guessSchemaButton = new UtilsButton(compositeRetreiveSchemaButton, displayStr, buttonSize.x + 12, HEIGHT_BUTTON_PIXEL);
        guessSchemaButton.setToolTipText(GUESS_SCHEMA_TOOLTIP);
        gc.dispose();

        if (LanguageManager.getCurrentLanguage() == ECodeLanguage.JAVA) {
            guessSchemaButton.setVisible(true);
        } else {
            guessSchemaButton.setVisible(false);
        }

        // Checkbox streamDetach
        streamDetachCheckbox = new Button(composite1, SWT.CHECK);
        streamDetachCheckbox.setText(Messages.getString("DatabaseTableForm.streamDetach")); //$NON-NLS-1$
        streamDetachCheckbox.setAlignment(SWT.LEFT);
        streamDetachCheckbox.setVisible(STREAM_DETACH_IS_VISIBLE);

        // Group MetaData
        Group groupMetaData = Form.createGroup(rightComposite, 1, Messages.getString("DatabaseTableForm.groupMetaData"), //$NON-NLS-1$
                tableCompositeHeight);
        Composite compositeMetaData = Form.startNewGridLayout(groupMetaData, 1);

        Composite compositeTable = Form.startNewDimensionnedGridLayout(compositeMetaData, 1, rightCompositeWidth,
                tableCompositeHeight);
        compositeTable.setLayout(new FillLayout());
        metadataEditor = new MetadataEmfTableEditor(""); //$NON-NLS-1$
        tableEditorView = new MetadataEmfTableEditorView(compositeTable, SWT.NONE, false);
        tableEditorView.setShowDbTypeColumn(true, true, true);
        tableEditorView.setShowDbColumnName(true, false);
        String trueDbmsID = metadataconnection.getMapping();
        // 9846
        tableEditorView.setCurrentDbms(trueDbmsID);
        tableEditorView.initGraphicComponents();
        metadataEditor.setDefaultLabel(Messages.getString("DatabaseTableForm.metadataDefaultNewLabel")); //$NON-NLS-1$
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
        // changed by hqzhang for TDI 19113 start
        GC gc = new GC(group);
        String displayStr = Messages.getString("DatabaseTableForm.AddTable"); //$NON-NLS-1$
        Point buttonSize = gc.stringExtent(displayStr);
        GridData girdData = new GridData(buttonSize.x + 12, HEIGHT_BUTTON_PIXEL);
        girdData.horizontalAlignment = SWT.CENTER;
        // Button Add metadata Table
        addTableButton = new UtilsButton(group, displayStr, girdData);
        displayStr = Messages.getString("DatabaseTableForm.RemoveTable"); //$NON-NLS-1$
        buttonSize = gc.stringExtent(displayStr);
        if (buttonSize.x + 12 > girdData.widthHint) {
            girdData.widthHint = buttonSize.x + 12;
        }
        girdData = new GridData(buttonSize.x + 12, HEIGHT_BUTTON_PIXEL);
        girdData.horizontalAlignment = SWT.CENTER;
        removeTableButton = new UtilsButton(group, displayStr, girdData);
        gc.dispose();// TDI 19113 end

    }

    /**
     * addButtonControls.
     *
     */
    @Override
    protected void addUtilsButtonListeners() {
        button.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (button.getEnabled()) {

                    if (!managerConnection.check(getIMetadataConnection())) {
                        adaptFormToCheckConnection();
                        updateStatus(IStatus.WARNING, Messages.getString("DatabaseTableForm.connectionFailure")); //$NON-NLS-1$
                        new ErrorDialogWidthDetailArea(getShell(), PID,
                                Messages.getString("DatabaseTableForm.connectionFailure"), //$NON-NLS-1$
                                managerConnection.getMessageException());
                    } else {

                        refreshTableComboItem();
                        tableCombo.setEnabled(true);
                        metadataTable.setName(tableCombo.getText());
                    }
                }
            }
        });

        // Event retreiveSchemaButton
        retreiveSchemaButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (retreiveSchemaButton.getEnabled()) {
                    pressRetreiveSchemaButton();
                    metadataTable.setName(tableCombo.getText());
                }
            }
        });

        // Event guessSchemaButton
        guessSchemaButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (guessSchemaButton.getEnabled()) {
                    pressGuessSchemaButton();
                    metadataTable.setName(tableCombo.getText());
                }
            }
        });

        // Event addTable Button
        addTableButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (addTableButton.getEnabled()) {
                    addTableButton.setEnabled(true);
                    try {
                        addMetadataTable(false);
                    } catch (ClassNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (InstantiationException e1) {
                        e1.printStackTrace();
                    } catch (IllegalAccessException e1) {
                        e1.printStackTrace();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    addTableButton.setEnabled(false);
                }
            }
        });

        removeTableButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (removeTableButton.getEnabled()) {
                    removeTableButton.setEnabled(true);
                    TableItem[] selection = tableNavigator.getSelection();
                    if (selection != null && selection.length > 0) {
                        boolean openConfirm = MessageDialog.openConfirm(getShell(),
                                Messages.getString("DatabaseTableForm.confirm"), //$NON-NLS-1$
                                Messages.getString("DatabaseTableForm.detete_table")); //$NON-NLS-1$
                        if (openConfirm) {
                            for (TableItem item : selection) {
                                if (tableNavigator.indexOf(item) != -1) {
                                    ProjectNodeHelper.removeTables(item.getText(), null, getConnection());
                                    tableNavigator.remove(tableNavigator.indexOf(item));
                                    if (tableNavigator.getItemCount() > 1) {
                                        tableNavigator.setSelection(tableNavigator.getItem(tableNavigator.getItemCount() - 1));
                                    }
                                }
                            }
                            IWizardPage prePage = parentWizardPage.getPreviousPage();
                            if (prePage instanceof SelectorTableWizardPage) {
                                ((SelectorTableWizardPage) prePage).restoreCheckItems();
                            }
                            int size = tableNavigator.getItems().length;
                            int index = 0;
                            if (size >= 1) {
                                index = size - 1;
                                String tableName = tableNavigator.getItem(index).getText();
                                for (Object obj : ConnectionHelper.getTables(getConnection())) {
                                    if (obj instanceof MetadataTable) {
                                        if (((MetadataTable) obj).getLabel().equals(tableName)) {
                                            metadataTable = (MetadataTable) obj;
                                        }
                                    }
                                }
                                initMetadataForm();
                            }

                        }
                    }
                } else {
                    removeTableButton.setEnabled(false);
                }

            }

        });

    }

    /**
     * DOC ocarbone Comment method "addMetadataTable".
     *
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    protected void addMetadataTable(boolean flag) throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            SQLException {
        // Create a new metadata and Add it on the connection
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
        /* see 0016785 need to add the table to connection rather than the Set */
        if (flag) {
            ProjectNodeHelper.addTableForSpecifiedDataPackage(getConnection(), metadataTable);
        } else {
            ProjectNodeHelper.addDefaultTableForSpecifiedDataPackage(getConnection(), metadataTable);
        }
        metadataTable.setId(factory.getNextId());

        // initExistingNames();
        metadataTable.setLabel(IndiceHelper.getIndexedLabel(metadataTable.getLabel(), existingNames));
        metadataTable.setName(metadataTable.getLabel());
        // init TreeNavigator
        initTreeNavigatorNodes();
        // init The Form
        initMetadataForm();
        if (tableCombo.getItemCount() > 0) {
            tableCombo.setEnabled(true);
        }
    }

    /**
     * checkConnection and adapt the form.
     */
    private void adaptFormToCheckConnection() {
        // tableCombo.setEnabled(false);
        // checkConnectionButton.setVisible(true);

        tableSettingsInfoLabel.setText(""); //$NON-NLS-1$
        if (metadataTable != null) {
            String sourceName = this.metadataTable.getName();
            // tableCombo.setReadOnly(sourceName != null);
        } else {
            tableCombo.setReadOnly(true);
        }
        // checkConnectionButton.setVisible(false);

        retreiveSchemaButton.setEnabled(!isReadOnly());
        addTableButton.setEnabled(!isReadOnly());
        removeTableButton.setEnabled(!isReadOnly());
        guessSchemaButton.setEnabled(!isReadOnly());
        if (isReadOnly()) {

        } else if (!managerConnection.getIsValide()) {
            // Connection failure
            tableSettingsInfoLabel.setText(Messages.getString("DatabaseTableForm.connectionFailure")); //$NON-NLS-1$

        } else if (tableCombo.getItemCount() <= 0) {
            // Connection is done but no table exist
            tableSettingsInfoLabel.setText(Messages.getString("DatabaseTableForm.tableNoExist")); //$NON-NLS-1$
        }
        // else {
        // // Connection is done and table(s) exist
        // tableSettingsInfoLabel.setText(""); //$NON-NLS-1$
        // tableCombo.setEnabled(true);
        // tableSettingsInfoLabel.setText(Messages.getString("DatabaseTableForm.retreiveButtonAlert")); //$NON-NLS-1$
        // checkConnectionButton.setVisible(false);
        // }
        updateRetreiveSchemaButton();
    }

    /**
     * checkConnectionButton.
     *
     * @param displayMessageBox
     */

    /**
     * Main Fields addControls.
     */
    @Override
    protected void addFieldsListeners() {

        // Navigation : when the user select a table
        tableNavigator.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                String schemaLabel = tableNavigator.getSelection()[0].getText();
                metadataTable = TableHelper.findByLabel(getConnection(), schemaLabel);
                tableCombo.setText(schemaLabel);
                initMetadataForm();
                if (isReadOnly()) {
                    addTableButton.setEnabled(false);
                }
            }

        });

        // nameText : Event modifyText
        nameText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                String labelText = nameText.getText();
                MetadataToolHelper.validateSchema(labelText);
                changeTableNavigatorStatus(labelText);
                metadataTable.setLabel(labelText);
                if (tableNavigator.getSelection().length > 0) {
                    tableNavigator.getSelection()[0].setText(labelText);
                }
                changeTableNavigatorStatus(checkFieldsValue());
            }

        });
        // nameText : Event KeyListener
        nameText.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                MetadataToolHelper.checkSchema(getShell(), e);
            }
        });

        // commentText : Event modifyText
        commentText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                metadataTable.setComment(commentText.getText());
            }
        });

    }

    /**
     * Ensures that fields are set. Update checkEnable / use to checkTableSetting().
     */
    @Override
    protected boolean checkFieldsValue() {

        updateRetreiveSchemaButton();

        if (!checkAllTablesIsCorrect()) {
            return false;
        }

        updateStatus(IStatus.OK, null);
        return true;
    }

    /**
     * DOC ocarbone Comment method "allTableHaveItems".
     *
     * @return
     */
    private boolean checkAllTablesIsCorrect() {
        Set<MetadataTable> tableset = ConnectionHelper.getTables(getConnection());
        MetadataTable[] tables = tableset.toArray(new MetadataTable[0]);
        for (MetadataTable table2 : tables) {
            MetadataTable table = table2;

            String[] exisNames = TableHelper.getTableNames(getConnection(), table.getLabel());
            List existNames = existingNames == null ? Collections.EMPTY_LIST : Arrays.asList(exisNames);

            if (StringUtils.isEmpty(table.getLabel())) {
                updateStatus(IStatus.ERROR, Messages.getString("DatabaseTableForm.nameAlert")); //$NON-NLS-1$
                return false;

                // Comment this condition because table name was allowed including illegal characters such as "&" or
                // "#".
                /**
                 * } else if (!Pattern.matches(RepositoryConstants.REPOSITORY_ITEM_PATTERN, table.getLabel())) {
                 * updateStatus(IStatus.ERROR, Messages.getString("DatabaseTableForm.nameAlertIllegalChar") + " \""
                 * //$NON-NLS-1$ //$NON-NLS-2$ + table.getLabel() + "\""); //$NON-NLS-1$ return false;
                 */
            } else if (existNames.contains(table.getLabel())) {
                updateStatus(IStatus.ERROR, Messages.getString("CommonWizard.nameAlreadyExist") + " \"" + table.getLabel() + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                return false;
            } else if (!MetadataToolHelper.isValidSchemaName(table.getLabel())) {
                updateStatus(IStatus.ERROR, Messages.getString("DatabaseTableForm.illegalChar", table.getLabel())); //$NON-NLS-1$
                return false;
            }

            // if (table.getColumns().size() == 0) {// this one has been removed,see bug 0016029
            //                updateStatus(IStatus.ERROR, Messages.getString("FileStep3.itemAlert") + " \"" + table.getLabel() + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            // return false;
            // }
        }
        return true;
    }

    private void updateRetreiveSchemaButton() {
        boolean enable = false;

        if (tableCombo.getText() != null) {
            int index = tableCombo.getCombo().indexOf(tableCombo.getText());
            if (index > -1) {
                enable = true;
                if (!tableCombo.getText().equals(metadataTable.getName())) {
                    metadataTable.setName(tableCombo.getText());
                }
            }
        }
        // for bug 13099
        if (enable == false && metadataTable != null) {
            editSchemaTableName = metadataTable.getLabel();
            enable = true;
        }
        if (isReadOnly()) {
            retreiveSchemaButton.setEnabled(false);
        } else {
            retreiveSchemaButton.setEnabled(enable);
        }
        streamDetachCheckbox.setEnabled(enable);
        // manage infoLabel
        if (tableCombo.getItemCount() > 0) {
            if (!enable && tableCombo.isEnabled()) {
                tableSettingsInfoLabel.setText(Messages.getString("DatabaseTableForm.retreiveButtonAlert")); //$NON-NLS-1$
            } else if (tableEditorView.getMetadataEditor().getBeanCount() <= 0) {
                tableSettingsInfoLabel.setText(Messages.getString("DatabaseTableForm.retreiveButtonTip")); //$NON-NLS-1$
            }
        } else {
            tableSettingsInfoLabel.setText(Messages.getString("DatabaseTableForm.retreiveButtonUse")); //$NON-NLS-1$
        }
    }

    /**
     * RetreiveShema connection width value of nameText, serverText, loginText, passwordText, tableCombo.
     */
    private void pressRetreiveSchemaButton() {

        boolean checkConnectionIsDone = managerConnection.check(getIMetadataConnection());

        if (!checkConnectionIsDone) {
            adaptFormToCheckConnection();
            updateStatus(IStatus.WARNING, Messages.getString("DatabaseTableForm.connectionFailure")); //$NON-NLS-1$
            new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("DatabaseTableForm.connectionFailure"), //$NON-NLS-1$
                    managerConnection.getMessageException());
        } else {
            boolean doit = true;
            if (tableEditorView.getMetadataEditor().getBeanCount() > 0) {
                doit = MessageDialog.openConfirm(getShell(), Messages.getString("DatabaseTableForm.retreiveButtonConfirmation"), //$NON-NLS-1$
                        Messages.getString("DatabaseTableForm.retreiveButtonConfirmationMessage")); //$NON-NLS-1$
            }
            if (doit) {
                tableString = tableCombo.getText();
                if (tableCombo.isEnabled() && tableCombo.getCombo().indexOf(tableString) == -1) {
                    MessageDialog
                            .openError(
                                    getShell(),
                                    Messages.getString("DatabaseTableForm.no_such_table"), Messages.getString("DatabaseTableForm.type_another_name")); //$NON-NLS-1$ //$NON-NLS-2$
                    return;
                }

                List<TdColumn> metadataColumns = new ArrayList<TdColumn>();
                if (useProvider()) {
                    metadataColumns = provider.returnMetadataColumnsFromTable(tableString, metadataconnection);
                } else {
                    metadataColumns = ExtractMetaDataFromDataBase.returnMetadataColumnsFormTable(metadataconnection, tableString);
                }

                tableEditorView.getMetadataEditor().removeAll();

                List<MetadataColumn> metadataColumnsValid = new ArrayList<MetadataColumn>();
                Iterator iterate = metadataColumns.iterator();
                while (iterate.hasNext()) {
                    MetadataColumn metadataColumn = (MetadataColumn) iterate.next();
                    if (metadataColumn.getTalendType().equals(JavaTypesManager.DATE.getId())
                            || metadataColumn.getTalendType().equals(PerlTypesManager.DATE)) {
                        if ("".equals(metadataColumn.getPattern())) { //$NON-NLS-1$
                            metadataColumn.setPattern(TalendQuoteUtils.addQuotes("dd-MM-yyyy")); //$NON-NLS-1$
                            if (EDatabaseTypeName.MSSQL.getDisplayName().equals(metadataconnection.getDbType())) {
                                if ("TIME".equals(metadataColumn.getSourceType())) {
                                    metadataColumn.setPattern(TalendQuoteUtils.addQuotes("HH:mm:ss")); //$NON-NLS-1$
                                }
                            }
                        }
                    }

                    String columnLabel = metadataColumn.getLabel();
                    // Check the label and add it to the table
                    metadataColumn.setLabel(tableEditorView.getMetadataEditor().getNextGeneratedColumnName(columnLabel));
                    metadataColumnsValid.add(metadataColumn);
                }
                // see bug 3738
                removeDoubleQuotes(metadataColumnsValid);
                tableEditorView.getMetadataEditor().addAll(metadataColumnsValid);
            }
        }

        updateRetreiveSchemaButton();
        changeTableNavigatorStatus(checkFieldsValue());
    }

    // made by hyWang
    private void pressGuessSchemaButton() {
        IDesignerCoreService designerService = (IDesignerCoreService) GlobalServiceRegister.getDefault().getService(
                IDesignerCoreService.class);
        String tableName = tableCombo.getText();
        CsvArray array;
        try {
            if (tableCombo.isEnabled() && tableCombo.getCombo().indexOf(tableName) == -1) {
                MessageDialog
                        .openError(
                                getShell(),
                                Messages.getString("DatabaseTableForm.no_such_table"), Messages.getString("DatabaseTableForm.type_another_name")); //$NON-NLS-1$ //$NON-NLS-2$
                return;
            }
            // fix for TDI-23745 and related tasks ,use the converted connnection with selected context group to guess
            // query ,no need ask context group again
            array = designerService.convertNode(connectionItem, getIMetadataConnection(), tableName);
            tableEditorView.getMetadataEditor().removeAll();

            List<MetadataColumn> columns = new ArrayList<MetadataColumn>();

            columns = GuessSchemaUtil.guessDbSchemaFromArray(array, true, tableEditorView, 5);

            List<String[]> schemaContent = array.getRows();
            if (schemaContent.size() <= 0) {
                return;
            }
            boolean isHive = EDatabaseTypeName.HIVE.getDisplayName().equals(metadataconnection.getDbType());
            int numbOfColumn = schemaContent.get(0).length;
            for (int i = 1; i <= numbOfColumn; i++) {
                MetadataColumn oneColum = columns.get(i - 1);
                // get the column name from the temp file genenrated by GuessSchemaProcess.java
                String labelName = (schemaContent.get(0))[i - 1];
                if (isHive && labelName != null && labelName.indexOf(".") != -1) { //$NON-NLS-1$
                    labelName = labelName.substring(labelName.lastIndexOf(".") + 1); //$NON-NLS-1$
                }
                // oneColum.setLabel(labelName);
                if (!"".equals(labelName)) { //$NON-NLS-1$
                    oneColum.setOriginalField(labelName);
                }
                if (!"".equals(schemaContent.get(2)[i - 1])) { //$NON-NLS-1$
                    oneColum.setPrecision(Integer.parseInt(schemaContent.get(2)[i - 1]));
                }
                if (!"".equals(schemaContent.get(3)[i - 1])) { //$NON-NLS-1$
                    oneColum.setLength(Integer.parseInt(schemaContent.get(3)[i - 1]));
                }
                if (!"".equals(schemaContent.get(4)[i - 1])) { //$NON-NLS-1$
                    oneColum.setSourceType(schemaContent.get(4)[i - 1]);
                    String talendType = MetadataTalendType.getMappingTypeRetriever(tableEditorView.getCurrentDbms())
                            .getDefaultSelectedTalendType(schemaContent.get(4)[i - 1]);
                    oneColum.setTalendType(talendType);
                    String mappingID = metadataconnection.getMapping();
                    if (mappingID != null) {
                        if (!TypesManager.checkDBType(mappingID, oneColum.getTalendType(), oneColum.getSourceType())) {
                            oneColum.setSourceType(TypesManager.getDBTypeFromTalendType(mappingID, oneColum.getTalendType()));
                        }
                    }
                }
                // get if a column is nullable from the temp file genenrated by
                // GuessSchemaProcess.java
                if (!"".equals(schemaContent.get(1)[i - 1])) { //$NON-NLS-1$
                    oneColum.setNullable((schemaContent.get(1))[i - 1].equals(Boolean.TRUE.toString()) ? true : false);
                }
                // String talendType = null;
                // // to see if the language is java or perl
                // try {
                // if (LanguageManager.getCurrentLanguage() == ECodeLanguage.JAVA) {
                // talendType = JavaDataTypeHelper.getTalendTypeOfValue(schemaContent.get(5)[i - 1]);
                // } else {
                // talendType = PerlDataTypeHelper.getNewTalendTypeOfValue(schemaContent.get(5)[i - 1]);
                // }
                // oneColum.setTalendType(talendType);
                // columns.add((MetadataColumn) oneColum);
                // } catch (Exception e) {
                // /*
                // * the table have no data at all ,to do nothing
                // */
                // }
            }

            tableEditorView.getMetadataEditor().addAll(columns);
        } catch (ProcessorException e) {
            ExceptionHandler.process(e);
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.repository.ui.swt.AbstractForm#adaptFormToReadOnly()
     */
    @Override
    protected void adaptFormToReadOnly() {
        readOnly = isReadOnly();

        nameText.setReadOnly(isReadOnly());
        commentText.setReadOnly(isReadOnly());
        tableEditorView.setReadOnly(isReadOnly());
        addTableButton.setEnabled(!isReadOnly());
        addTableButton.setEnabled(!isReadOnly());
        retreiveSchemaButton.setEnabled(!isReadOnly());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.swt.widgets.Control#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        Adapter adaptor = new Adapter() {

            @Override
            public Notifier getTarget() {
                return null;
            }

            @Override
            public boolean isAdapterForType(Object arg0) {
                return false;
            }

            @Override
            public void notifyChanged(Notification arg0) {
                int type = arg0.getEventType();
                int featureId = arg0.getFeatureID(ConnectionPackage.class);
                if (type == Notification.SET && featureId == ConnectionPackage.ABSTRACT_METADATA_OBJECT__LABEL) {
                    MetadataColumn column = (MetadataColumn) arg0.getNotifier();
                    String tableName = column.getTable().getLabel();
                    Map<String, String> columnChanged = labelChanged.get(tableName);
                    if (columnChanged == null) {
                        columnChanged = new HashMap<String, String>();
                        labelChanged.put(tableName, columnChanged);
                    }
                    String originalColumnName = columnChanged.get(arg0.getOldStringValue());
                    if (originalColumnName == null) {
                        columnChanged.put(arg0.getNewStringValue(), arg0.getOldStringValue());
                    } else {
                        columnChanged.clear();
                        columnChanged.put(arg0.getNewStringValue(), originalColumnName);
                    }
                }
            }

            @Override
            public void setTarget(Notifier arg0) {
            }

        };

        if (visible) {
            try {
                initializeForm();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // bug 16595
            // checkConnection(false);

            if (metadataTable != null) {
                for (MetadataColumn column : metadataTable.getColumns()) {
                    column.eAdapters().add(adaptor);
                }
            }
        } else {
            if (metadataTable != null) {
                for (MetadataColumn column : metadataTable.getColumns()) {
                    if (column.eAdapters().contains(adaptor)) {
                        column.eAdapters().remove(adaptor);
                    }
                }
            }
        }
        if (isReadOnly() != readOnly) {
            adaptFormToReadOnly();
        }
    }

    protected DatabaseConnection getConnection() {
        if (temConnection != null) {
            return temConnection;
        } else {
            return (DatabaseConnection) connectionItem.getConnection();
        }
    }

    /**
     *
     * for featrue 2449
     */

    public IMetadataConnection getIMetadataConnection() {
        return this.metadataconnection;
    }

    public void setIMetadataConnection(IMetadataConnection metadataConnection) {
        this.metadataconnection = metadataConnection;
    }

    /**
     * Comment method "changeTableNavigatorStatus".
     *
     * @param schemaLabel
     */
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

    /**
     * DOC Administrator Comment method "changeControlStatus".
     *
     * @param children
     * @param status
     */
    private void changeControlStatus(Control[] children, boolean status) {
        for (Control control : children) {
            control.setEnabled(status);
            if (control instanceof Composite) {
                Control[] subChildren = ((Composite) control).getChildren();
                changeControlStatus(subChildren, status);
            }
        }
    }

    /**
     * Comment method "changeTableNavigatorStatus".
     *
     * @param isEnabled
     */
    private void changeTableNavigatorStatus(boolean isEnabled) {
        Composite leftGroup = tableNavigator.getParent().getParent().getParent();
        Control[] children = leftGroup.getChildren();
        leftGroup.setEnabled(isEnabled);
        changeControlStatus(children, isEnabled);
    }

    /**
     * DOC JKWANG Comment method "setButtonsVisibility".
     *
     * @param isRepositoryObjectEditable
     */
    public void setButtonsVisibility(boolean isVisible) {
        // TODO Auto-generated method stub

        this.tableCombo.setVisible(isVisible);
        this.button.setVisible(isVisible);
        this.addTableButton.setVisible(isVisible);
        this.removeTableButton.setVisible(isVisible);

    }

    /**
     * Getter for labelChanged.
     *
     * @return the labelChanged
     */
    public Map<String, Map<String, String>> getLabelChanged() {
        return this.labelChanged;
    }

}
