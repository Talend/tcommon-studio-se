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

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase.ETableTypes;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.metadata.builder.database.TableInfoParameters;
import org.talend.core.model.metadata.builder.database.extractots.IDBMetadataProviderObject;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.metadata.managment.ui.wizard.AbstractForm;
import org.talend.repository.metadata.i18n.Messages;

/**
 * DOC qzhang class global comment. Detailled comment <br/>
 * 
 */
public class DatabaseTableFilterForm extends AbstractForm {

    public static final String PREFS_NAMEFILTER = "DatabaseTableFilterForm.NameFilter"; //$NON-NLS-1$

    public static final String PREFS_SEQ = ";"; //$NON-NLS-1$

    protected static Logger log = Logger.getLogger(DatabaseTableFilterForm.class);

    protected static final String PID = CoreRuntimePlugin.PLUGIN_ID;

    private final TableInfoParameters tableInfoParameters;

    private Button tableCheck;

    private Button viewCheck;

    private Button synonymCheck;

    private Button calculationViewCheck;

    // hide for the bug 7959

    private Button publicSynonymCheck;

    private Button usedName;

    private Button usedSql;

    private Label setNamelabel;

    private Text sqlFilter;

    private Label sqllabel;

    private org.eclipse.swt.widgets.List nameFilter;

    private Button removeButton;

    private Button editButton;

    private Button newButton;

    public DatabaseTableFilterForm(Composite parent, DatabaseTableFilterWizardPage page, IMetadataConnection metadataconnection) {
        super(parent, SWT.NONE);
        tableInfoParameters = page.getTableInfoParameters();
        this.connectionItem = page.getConnectionItem(); // hywang add
        this.metadataconnection = metadataconnection;
        this.typeName = EDatabaseTypeName.getTypeFromDbType(metadataconnection.getDbType());
        /* use provider for the databse didn't use JDBC,for example: HBase */
        if (typeName != null && typeName.isUseProvider()) {
            this.provider = ExtractMetaDataFromDataBase.getProviderByDbType(this.metadataconnection.getDbType());
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
        getTableInfoParameters().setSqlFiter(sqlFilter.getText());
        getTableInfoParameters().changeType(ETableTypes.TABLETYPE_TABLE, tableCheck.getSelection());
        getTableInfoParameters().changeType(ETableTypes.EXTERNAL_TABLE, tableCheck.getSelection());
        getTableInfoParameters().changeType(ETableTypes.TABLETYPE_VIEW, viewCheck.getSelection());
        getTableInfoParameters().changeType(ETableTypes.TABLETYPE_SYNONYM, synonymCheck.getSelection());
        if (EDatabaseTypeName.HIVE.getDisplayName().equals(metadataconnection.getDbType())) {
            getTableInfoParameters().changeType(ETableTypes.MANAGED_TABLE, tableCheck.getSelection());
            getTableInfoParameters().changeType(ETableTypes.INDEX_TABLE, tableCheck.getSelection());
            getTableInfoParameters().changeType(ETableTypes.VIRTUAL_VIEW, viewCheck.getSelection());
        } else if (EDatabaseTypeName.SAPHana.getDisplayName().equals(metadataconnection.getDbType())) {
            getTableInfoParameters().changeType(ETableTypes.TABLETYPE_CALCULATION_VIEW, calculationViewCheck.getSelection());
        }
        // hide for the bug 7959
        if (isOracle()) {
            getTableInfoParameters().changeType(ETableTypes.TABLETYPE_ALL_SYNONYM, publicSynonymCheck.getSelection());
        }

        switchFilter();
        /** need to see which controls will be hide or diabled.From metadata_provider **/
        IDBMetadataProviderObject providerObjectByDbType = ExtractMetaDataFromDataBase
                .getProviderObjectByDbType(metadataconnection.getDbType());
        if (typeName != null && typeName.isUseProvider() && !providerObjectByDbType.isSupportJDBC()) {
            disableAllJDBCControls();
        }
    }

    /**
     * DOC qzhang Comment method "switchFilter".
     */
    private void switchFilter() {
        usedName.setSelection(getTableInfoParameters().isUsedName());
        usedSql.setSelection(!getTableInfoParameters().isUsedName());

        sqlFilter.setEnabled(!getTableInfoParameters().isUsedName());
        sqllabel.setEnabled(!getTableInfoParameters().isUsedName());

        tableCheck.setEnabled(getTableInfoParameters().isUsedName());
        viewCheck.setEnabled(getTableInfoParameters().isUsedName());
        synonymCheck.setEnabled(getTableInfoParameters().isUsedName());
        if (isOracle()) {
            publicSynonymCheck.setEnabled(getTableInfoParameters().isUsedName());
            ExtractMetaDataUtils.getInstance().setUseAllSynonyms(publicSynonymCheck.getSelection());
        } else if (EDatabaseTypeName.SAPHana.getDisplayName().equals(metadataconnection.getDbType())) {
            calculationViewCheck.setEnabled(getTableInfoParameters().isUsedName());
        }

        removeButton.setEnabled(getTableInfoParameters().isUsedName());
        editButton.setEnabled(getTableInfoParameters().isUsedName());
        newButton.setEnabled(getTableInfoParameters().isUsedName());

        setNamelabel.setEnabled(getTableInfoParameters().isUsedName());
        nameFilter.setEnabled(getTableInfoParameters().isUsedName());

    }

    @Override
    protected void addFields() {

        Composite composite = new Composite(this, SWT.NONE);

        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        gridLayout.marginBottom = 0;
        gridLayout.marginWidth = 0;
        gridLayout.marginTop = 0;
        gridLayout.marginRight = 0;
        gridLayout.marginLeft = 0;
        gridLayout.marginHeight = 0;

        gridLayout.horizontalSpacing = 0;
        gridLayout.verticalSpacing = 0;
        composite.setLayout(gridLayout);

        GridData gridData = new GridData(GridData.FILL_BOTH);
        composite.setLayoutData(gridData);

        createSwitchComposite(composite);
        createNameFiterComposite(composite);

        Composite sqlcomposite = new Composite(composite, SWT.NONE);
        gridLayout = new GridLayout();
        gridLayout.numColumns = 1;

        sqlcomposite.setLayout(gridLayout);

        gridData = new GridData(GridData.FILL_HORIZONTAL);
        sqlcomposite.setLayoutData(gridData);

        sqllabel = new Label(sqlcomposite, SWT.NONE);
        sqllabel.setText(Messages.getString("DatabaseTableFilterForm.setSqlFilter")); //$NON-NLS-1$
        sqlFilter = new Text(sqlcomposite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
        sqlFilter.setEditable(true);
        sqlFilter.setText("SELECT TNAME FROM TAB WHERE TNAME LIKE \'BAL%\'"); //$NON-NLS-1$
        gridData = new GridData(GridData.FILL_BOTH);
        gridData.heightHint = 60;
        sqlFilter.setLayoutData(gridData);

    }

    /**
     * DOC qzhang Comment method "createSwitchComposite".
     * 
     * @param composite
     */
    private void createSwitchComposite(Composite composite) {
        GridLayout gridLayout;
        GridData gridData;
        Group switchcomposite = new Group(composite, SWT.NONE);
        switchcomposite.setText(Messages.getString("DatabaseTableFilterForm.selectCondition")); //$NON-NLS-1$
        gridLayout = new GridLayout(2, false);
        gridLayout.marginBottom = 0;
        gridLayout.marginWidth = 0;
        gridLayout.marginTop = 0;
        gridLayout.marginRight = 0;
        gridLayout.marginLeft = 0;
        gridLayout.marginHeight = 0;

        gridLayout.horizontalSpacing = 0;
        gridLayout.verticalSpacing = 0;
        switchcomposite.setLayout(gridLayout);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        switchcomposite.setLayoutData(gridData);
        usedName = new Button(switchcomposite, SWT.RADIO);
        usedName.setText(Messages.getString("DatabaseTableFilterForm.useNameFilter")); //$NON-NLS-1$
        SelectionAdapter selectionAdapter = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                getTableInfoParameters().setUsedName(usedName.getSelection());
                switchFilter();
            }

        };
        usedName.addSelectionListener(selectionAdapter);
        usedSql = new Button(switchcomposite, SWT.RADIO);
        usedSql.setText(Messages.getString("DatabaseTableFilterForm.useSqlFilter")); //$NON-NLS-1$
        usedSql.addSelectionListener(selectionAdapter);
    }

    /**
     * DOC qzhang Comment method "createNameFiterComposite".
     * 
     * @param composite
     */
    private void createNameFiterComposite(Composite composite) {
        GridLayout gridLayout;
        GridData gridData;
        Composite composite2 = new Composite(composite, SWT.NONE);
        gridLayout = new GridLayout(1, false);
        gridLayout.marginBottom = 0;
        gridLayout.marginWidth = 0;
        gridLayout.marginTop = 0;
        gridLayout.marginRight = 0;
        gridLayout.marginLeft = 0;
        gridLayout.marginHeight = 0;

        gridLayout.horizontalSpacing = 0;
        gridLayout.verticalSpacing = 0;
        composite2.setLayout(gridLayout);

        gridData = new GridData(GridData.FILL_HORIZONTAL);
        composite2.setLayoutData(gridData);

        Group typesFilter = new Group(composite2, SWT.NONE);
        typesFilter.setText(Messages.getString("DatabaseTableFilterForm.selectType")); //$NON-NLS-1$
        gridLayout = new GridLayout();
        gridLayout.numColumns = 4;

        typesFilter.setLayout(gridLayout);

        gridData = new GridData(GridData.FILL_HORIZONTAL);
        typesFilter.setLayoutData(gridData);

        tableCheck = new Button(typesFilter, SWT.CHECK);
        tableCheck.setSelection(true);
        tableCheck.setText(Messages.getString("DatabaseTableFilterForm.table")); //$NON-NLS-1$
        viewCheck = new Button(typesFilter, SWT.CHECK);
        viewCheck.setText(Messages.getString("DatabaseTableFilterForm.view")); //$NON-NLS-1$
        viewCheck.setSelection(true);
        synonymCheck = new Button(typesFilter, SWT.CHECK);
        synonymCheck.setText(Messages.getString("DatabaseTableFilterForm.synonym")); //$NON-NLS-1$
        synonymCheck.setSelection(true);
        // hide for the bug 7959
        if (isOracle()) {
            publicSynonymCheck = new Button(typesFilter, SWT.CHECK);
            publicSynonymCheck.setText(Messages.getString("DatabaseTableFilterForm.allSynonyms")); //$NON-NLS-1$
            publicSynonymCheck.setSelection(false);
            // ExtractMetaDataUtils.setVale(publicSynonymCheck.getSelection());
        } else if (EDatabaseTypeName.SAPHana.getDisplayName().equals(metadataconnection.getDbType())) {
            calculationViewCheck = new Button(typesFilter, SWT.CHECK);
            calculationViewCheck.setText(Messages.getString("DatabaseTableFilterForm.calculationView")); //$NON-NLS-1$
            calculationViewCheck.setSelection(true);
        }

        Composite namecomposite = new Composite(composite2, SWT.NONE);
        gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        namecomposite.setLayout(gridLayout);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        namecomposite.setLayoutData(gridData);

        setNamelabel = new Label(namecomposite, SWT.NONE);
        setNamelabel.setText(Messages.getString("DatabaseTableFilterForm.setNameFilter")); //$NON-NLS-1$

        Composite nameFiltercomposite = new Composite(namecomposite, SWT.NONE);
        gridLayout = new GridLayout(2, false);
        gridLayout.marginBottom = 0;
        gridLayout.marginWidth = 0;
        gridLayout.marginTop = 0;
        gridLayout.marginRight = 0;
        gridLayout.marginLeft = 0;
        gridLayout.marginHeight = 0;

        gridLayout.horizontalSpacing = 5;
        gridLayout.verticalSpacing = 0;
        nameFiltercomposite.setLayout(gridLayout);

        gridData = new GridData(GridData.FILL_BOTH);
        nameFiltercomposite.setLayoutData(gridData);

        nameFilter = new org.eclipse.swt.widgets.List(nameFiltercomposite, SWT.BORDER | SWT.SINGLE);
        gridData = new GridData(GridData.FILL_BOTH);
        gridData.widthHint = 600;
        nameFilter.setLayoutData(gridData);
        nameFilter.setItems(getNameFilters());
        nameFilter.setSelection(0);

        Composite nameFilterBtncomposite = new Composite(nameFiltercomposite, SWT.NONE);
        gridLayout = new GridLayout(1, false);
        gridLayout.marginBottom = 0;
        gridLayout.marginWidth = 0;
        gridLayout.marginTop = 0;
        gridLayout.marginRight = 0;
        gridLayout.marginLeft = 0;
        gridLayout.marginHeight = 0;

        gridLayout.horizontalSpacing = 0;
        gridLayout.verticalSpacing = 0;
        nameFilterBtncomposite.setLayout(gridLayout);

        gridData = new GridData(GridData.FILL_BOTH);
        nameFilterBtncomposite.setLayoutData(gridData);

        newButton = new Button(nameFilterBtncomposite, SWT.PUSH);
        gridData = new GridData(GridData.FILL_BOTH);
        newButton.setLayoutData(gridData);
        newButton.setText(Messages.getString("DatabaseTableFilterForm.new")); //$NON-NLS-1$
        editButton = new Button(nameFilterBtncomposite, SWT.PUSH);
        editButton.setText(Messages.getString("DatabaseTableFilterForm.edit")); //$NON-NLS-1$
        gridData = new GridData(GridData.FILL_BOTH);
        editButton.setLayoutData(gridData);
        removeButton = new Button(nameFilterBtncomposite, SWT.PUSH);
        removeButton.setText(Messages.getString("DatabaseTableFilterForm.remove")); //$NON-NLS-1$
        gridData = new GridData(GridData.FILL_BOTH);
        removeButton.setLayoutData(gridData);
    }

    private void disableAllJDBCControls() {
        tableCheck.setEnabled(false);
        viewCheck.setEnabled(false);
        viewCheck.setSelection(false);
        synonymCheck.setEnabled(false);
        synonymCheck.setSelection(false);
        setNamelabel.setEnabled(false);
        nameFilter.setEnabled(false);
        newButton.setEnabled(false);
        newButton.setEnabled(false);
        editButton.setEnabled(false);
        removeButton.setEnabled(false);
        usedName.setEnabled(false);
        usedSql.setEnabled(false);
        sqllabel.setEnabled(false);
        sqlFilter.setEnabled(false);
        if (EDatabaseTypeName.SAPHana.getDisplayName().equals(metadataconnection.getDbType())) {
            calculationViewCheck.setEnabled(false);
            calculationViewCheck.setSelection(false);
        }
    }

    /**
     * DOC qzhang Comment method "getNameFilters".
     * 
     * @return
     */
    private String[] getNameFilters() {
        String[] items = null;
        String string = CoreRuntimePlugin.getInstance().getCoreService().getPreferenceStore().getString(PREFS_NAMEFILTER);
        if (string == null || "".equals(string)) { //$NON-NLS-1$
            items = new String[] { TableInfoParameters.DEFAULT_FILTER };
        } else {
            items = string.split(PREFS_SEQ);
        }
        return items;
    }

    String addName = ""; //$NON-NLS-1$

    /**
     * addButtonControls.
     * 
     */
    @Override
    protected void addUtilsButtonListeners() {
        tableCheck.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                getTableInfoParameters().changeType(ETableTypes.TABLETYPE_TABLE, tableCheck.getSelection());
                // Added by Marvin Wang on Feb. 5, 2012 for bug TDI-24413, in fact the type is added here is not a good
                // way.
                // Suggestion: We could abstract the TableInfoParameters as a reference in DatabaseTableWizardPage. Each
                // child like "HiveTableInfoParameters" could implement "getTypes()" to return the private types. When
                // types are reqired, it could invoke like
                // "DatabaseTableWizardPage.getTableInfoParameters().getTypes()".
                getTableInfoParameters().changeType(ETableTypes.EXTERNAL_TABLE, tableCheck.getSelection());
                if (EDatabaseTypeName.HIVE.getDisplayName().equals(metadataconnection.getDbType())) {
                    getTableInfoParameters().changeType(ETableTypes.MANAGED_TABLE, tableCheck.getSelection());
                    getTableInfoParameters().changeType(ETableTypes.INDEX_TABLE, tableCheck.getSelection());
                }
            }

        });

        viewCheck.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                getTableInfoParameters().changeType(ETableTypes.TABLETYPE_VIEW, viewCheck.getSelection());
                getTableInfoParameters().changeType(ETableTypes.VIRTUAL_VIEW, viewCheck.getSelection());
            }

        });

        synonymCheck.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                getTableInfoParameters().changeType(ETableTypes.TABLETYPE_SYNONYM, synonymCheck.getSelection());
            }

        });
        // hide for the bug 7959

        if (isOracle()) {
            publicSynonymCheck.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    getTableInfoParameters().changeType(ETableTypes.TABLETYPE_ALL_SYNONYM, publicSynonymCheck.getSelection());
                    ExtractMetaDataUtils.getInstance().setUseAllSynonyms(publicSynonymCheck.getSelection());
                    if (publicSynonymCheck.getSelection()) {
                        tableCheck.setEnabled(false);

                        viewCheck.setEnabled(false);

                        synonymCheck.setEnabled(false);
                    } else {
                        tableCheck.setEnabled(true);

                        viewCheck.setEnabled(true);

                        synonymCheck.setEnabled(true);
                    }
                }

            });
        } else if (EDatabaseTypeName.SAPHana.getDisplayName().equals(metadataconnection.getDbType())) {
            calculationViewCheck.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    getTableInfoParameters().changeType(ETableTypes.TABLETYPE_CALCULATION_VIEW,
                            calculationViewCheck.getSelection());
                }

            });
        }
        SelectionAdapter selectionAdapter = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                addName = ""; //$NON-NLS-1$
                Dialog dialog = new Dialog(getShell()) {

                    Text addText;

                    @Override
                    protected Control createDialogArea(Composite parent) {
                        Composite createDialogArea = (Composite) super.createDialogArea(parent);
                        Label addLabel = new Label(createDialogArea, SWT.NONE);
                        addLabel.setText(Messages.getString("DatabaseTableFilterForm.filterName")); //$NON-NLS-1$
                        addText = new Text(createDialogArea, SWT.BORDER);
                        GridData gridData = new GridData(GridData.FILL_BOTH);
                        addText.setLayoutData(gridData);
                        addText.setText(addName);
                        return createDialogArea;
                    }

                    /*
                     * (non-Javadoc)
                     * 
                     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
                     */
                    @Override
                    protected void configureShell(Shell newShell) {
                        super.configureShell(newShell);
                        newShell.setText(Messages.getString("DatabaseTableFilterForm.newFilterName")); //$NON-NLS-1$
                    }

                    /*
                     * (non-Javadoc)
                     * 
                     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
                     */
                    @Override
                    protected void okPressed() {
                        addName = addText.getText();
                        super.okPressed();
                    }

                };
                if (dialog.open() == Dialog.OK) {
                    nameFilter.add(addName, 0);
                    nameFilter.select(0);
                    removeButton.setEnabled(true);
                    editButton.setEnabled(true);
                }
            }
        };
        newButton.addSelectionListener(selectionAdapter);

        selectionAdapter = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                final int index = nameFilter.getSelectionIndex();
                if (nameFilter.getSelection().length > 0) {
                    addName = nameFilter.getSelection()[0];
                }
                Dialog dialog = new Dialog(getShell()) {

                    Text addText;

                    @Override
                    protected Control createDialogArea(Composite parent) {
                        Composite createDialogArea = (Composite) super.createDialogArea(parent);
                        Label addLabel = new Label(createDialogArea, SWT.NONE);
                        addLabel.setText(Messages.getString("DatabaseTableFilterForm.filterName")); //$NON-NLS-1$
                        addText = new Text(createDialogArea, SWT.BORDER);
                        GridData gridData = new GridData(GridData.FILL_BOTH);
                        addText.setLayoutData(gridData);
                        addText.setText(addName);
                        return createDialogArea;
                    }

                    /*
                     * (non-Javadoc)
                     * 
                     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
                     */
                    @Override
                    protected void configureShell(Shell newShell) {
                        super.configureShell(newShell);
                        newShell.setText(Messages.getString("DatabaseTableFilterForm.editFilterName")); //$NON-NLS-1$
                    }

                    /*
                     * (non-Javadoc)
                     * 
                     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
                     */
                    @Override
                    protected void okPressed() {
                        addName = addText.getText();
                        super.okPressed();
                    }

                };
                if (dialog.open() == Dialog.OK) {
                    nameFilter.setItem(index, addName);
                    nameFilter.select(index);
                }
            }
        };
        editButton.addSelectionListener(selectionAdapter);
        selectionAdapter = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (nameFilter.getSelection().length > 0) {
                    addName = nameFilter.getSelection()[0];
                    nameFilter.remove(addName);
                }
            }
        };
        removeButton.addSelectionListener(selectionAdapter);

    }

    /**
     * Main Fields addControls.
     */
    @Override
    protected void addFieldsListeners() {
        sqlFilter.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                getTableInfoParameters().setSqlFiter(sqlFilter.getText());
            }
        });
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

    /**
     * Getter for tableInfoParameters.
     * 
     * @return the tableInfoParameters
     */
    public TableInfoParameters getTableInfoParameters() {
        return this.tableInfoParameters;
    }

    /**
     * Getter for nameFilter.
     * 
     * @return the nameFilter
     */
    public String getNameFilter() {

        StringBuilder s = new StringBuilder();

        if (getFilters().length > 0) {
            for (String string : getFilters()) {
                s.append(string + PREFS_SEQ);
            }
            s.deleteCharAt(s.length() - 1);
        }
        return s.toString();
    }

    /**
     * DOC qzhang Comment method "getFilters".
     * 
     * @return
     */
    public String[] getFilters() {
        return nameFilter.getItems();
    }

    private boolean isOracle() { // hywang add for 0007959
        if (this.connectionItem != null) {
            if (this.connectionItem instanceof DatabaseConnectionItem) {
                DatabaseConnectionItem dbConnItem = (DatabaseConnectionItem) this.connectionItem;
                DatabaseConnection dbConn = null;
                String dbtype = null;
                if (dbConnItem.getConnection() instanceof DatabaseConnection) {
                    dbConn = (DatabaseConnection) dbConnItem.getConnection();
                    dbtype = dbConn.getDatabaseType();
                }
                if (EDatabaseTypeName.ORACLEFORSID.getDisplayName().equals(dbtype)
                        || EDatabaseTypeName.ORACLESN.getDisplayName().equals(dbtype)) {
                    return true;
                } else if (EDatabaseTypeName.GENERAL_JDBC.getDisplayName().equals(dbtype)) {
                    String driver = dbConn.getDriverClass();
                    dbtype = ExtractMetaDataUtils.getInstance().getDbTypeByClassName(driver);
                    if (EDatabaseTypeName.ORACLEFORSID.getDisplayName().equals(dbtype)
                            || EDatabaseTypeName.ORACLESN.getDisplayName().equals(dbtype)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
