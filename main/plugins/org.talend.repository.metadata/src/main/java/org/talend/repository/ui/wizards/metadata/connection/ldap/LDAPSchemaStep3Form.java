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
package org.talend.repository.ui.wizards.metadata.connection.ldap;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.ui.swt.thread.SWTUIThreadProcessor;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.core.model.metadata.IMetadataContextModeManager;
import org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.repository.model.EEncryptionMethod;
import org.talend.core.utils.CsvArray;
import org.talend.metadata.managment.ui.preview.ProcessDescription;
import org.talend.metadata.managment.ui.preview.ShadowProcessPreview;
import org.talend.metadata.managment.ui.utils.ShadowProcessHelper;
import org.talend.metadata.managment.ui.wizard.IRefreshable;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.metadata.ui.wizards.form.AbstractLDAPSchemaStepForm;

import com.ca.commons.jndi.ConnectionData;
import com.ca.commons.jndi.SchemaOps;
import com.ca.directory.jxplorer.broker.CBGraphicsOps;
import com.ca.directory.jxplorer.broker.JNDIBroker;
import com.ca.directory.jxplorer.broker.JNDIBroker.DataConnectionQuery;

/**
 * The class is used for LDAP schema on Repository View. <br/>
 * 
 * @author ftang, 18/09/2007
 * 
 */
public class LDAPSchemaStep3Form extends AbstractLDAPSchemaStepForm implements IRefreshable {

    private static Logger log = Logger.getLogger(LDAPSchemaStep3Form.class);

    public static List<String> itemTableNameList;

    private Button previewButton;

    private Label previewInformationLabel;

    private ShadowProcessPreview ldapSchemaPreview;

    static CBGraphicsOps dirOps;

    /**
     * Another.
     */

    private UtilsButton cancelButton;

    private boolean readOnly;

    private ExtendedTableModel<String> attributeModel;

    private AbstractExtendedTableViewer<String> tableEditorView;

    SWTUIThreadProcessor processor = new PreviewProcessor();

    private Text filterText;

    /**
     * Output tab.
     */
    private CTabFolder tabFolder;

    private CTabItem previewTabItem;

    private CTabItem outputTabItem;

    private Composite outputComposite;

    /**
     * Constructor to use by RCP Wizard.
     * 
     * @param Composite
     * @param Wizard
     * @param Style
     */
    public LDAPSchemaStep3Form(Composite parent, ConnectionItem connectionItem, IMetadataContextModeManager contextModeManager) {
        super(parent, connectionItem, null, null);
        setContextModeManager(contextModeManager);
        setupForm();
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

        // compositeFile Main Fields
        Composite mainComposite = Form.startNewGridLayout(this, 2);
        addGroupAttributes(mainComposite, 300, 115);
        addFilter(mainComposite, 300, 85);
        addGroupFileViewer(this, 700, 180);

        if (!isInWizard()) {
            // Bottom Button
            Composite compositeBottomButton = Form.startNewGridLayout(this, 2, false, SWT.CENTER, SWT.CENTER);
            // Button Cancel
            cancelButton = new UtilsButton(compositeBottomButton, Messages.getString("CommonWizard.cancel"), WIDTH_BUTTON_PIXEL, //$NON-NLS-1$
                    HEIGHT_BUTTON_PIXEL);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFieldsListeners()
     */
    @Override
    protected void addFieldsListeners() {
        filterText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    String filter = filterText.getText();
                    if (filter != null && filter.length() > 0) {
                        getConnection().setFilter(filterText.getText().trim());
                    }
                }
            }
        });
    }

    /**
     * addButtonControls.
     * 
     * @param cancelButton
     */
    @Override
    protected void addUtilsButtonListeners() {

        // Event PreviewButton
        previewButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {

                refreshPreview();
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

        // Event checkBox action
        final Table table = tableEditorView.getTableViewerCreator().getTable();
        table.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (e.detail == SWT.CHECK) {
                    TableItem tableItem = (TableItem) e.item;
                    boolean promptNeeded = tableItem.getChecked();
                    if (promptNeeded) {
                        getConnection().getValue().add(tableItem.getText());
                    } else {
                        getConnection().getValue().remove(tableItem.getText());
                    }
                }
                checkFieldsValue();
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#checkFieldsValue()
     */
    @Override
    protected boolean checkFieldsValue() {
        previewInformationLabel.setText(""); //$NON-NLS-1$
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
        String filter = getConnection().getFilter();
        if (filter != null) {
            this.filterText.setText(filter);
        } else {
            this.filterText.setText(ConnectionUIConstants.DEFAULT_FILTER);
        }

        checkFieldsValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.IRefreshable#refresh()
     */
    @Override
    public void refresh() {
        refreshPreview();
    }

    /**
     * Subclass of SWTUIThreadProcessor to process the preview event. <br/>
     * 
     * $Id: DelimitedFileStep2Form.java 4837 2007-07-27 05:40:31Z bqian $
     * 
     */
    class PreviewProcessor extends SWTUIThreadProcessor {

        CsvArray csvArray = null;

        ProcessDescription processDescription = null;

        @Override
        public boolean preProcessStart() {
            previewButton.setText(Messages.getString("FileStep2.stop")); //$NON-NLS-1$

            clearPreview();

            // if incomplete settings, , the process don't be executed
            if (!checkFieldsValue()) {
                previewInformationLabel.setText(" " + Messages.getString("FileStep2.settingsIncomplete")); //$NON-NLS-1$ //$NON-NLS-2$
                //$NON-NLS-2$
                return false;
            }

            previewInformationLabel.setText(" " + Messages.getString("FileStep2.previewProgress")); //$NON-NLS-1$ //$NON-NLS-2$
            //$NON-NLS-2$
            processDescription = getProcessDescription();
            updateStatus(IStatus.ERROR, null);
            return true;
        }

        @Override
        public void nonUIProcessInThread() {
            // get the CsvArray width an adapt ProcessDescription
            try {
                csvArray = ShadowProcessHelper.getCsvArray(processDescription, "LDAP_SCHEMA", true); //$NON-NLS-1$

            } catch (Exception e) {
                setException(e);
                log.error(Messages.getString("FileStep2.previewFailure") + " " + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
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
            // LDAPSchemaConnection connection = (LDAPSchemaConnection) connectionItem.getConnection();
            if (getException() != null) {
                previewInformationLabel.setText(" " + Messages.getString("FileStep2.previewFailure")); //$NON-NLS-1$ //$NON-NLS-2$
                //$NON-NLS-2$
                Display.getDefault().asyncExec(new Runnable() {

                    @Override
                    public void run() {
                        handleErrorOutput(outputComposite, tabFolder, outputTabItem);
                    }

                });
                return;
            }

            if (csvArray == null || csvArray.getRows() == null || csvArray.getRows().size() == 0) {
                previewInformationLabel.setText(" " + Messages.getString("FileStep2.previewFailure")); //$NON-NLS-1$ //$NON-NLS-2$
                // MessageDialog.openError(getShell(), "Error", "Preview refresh failed, please check attributes and
                // filter.");

                String errorInfo = Messages.getString("LDAPSchemaStep3Form.errorMessage"); //$NON-NLS-1$

                final RuntimeException e = new RuntimeException(errorInfo);

                Display.getDefault().asyncExec(new Runnable() {

                    @Override
                    public void run() {
                        handleErrorOutput(outputComposite, tabFolder, outputTabItem, e);
                    }

                });

                if (!isContextMode()) {
                    filterText.setText(ConnectionUIConstants.DEFAULT_FILTER);
                    connection.setFilter(ConnectionUIConstants.DEFAULT_FILTER);
                }
            } else {
                previewInformationLabel.setText(" " + Messages.getString("FileStep2.previewIsDone")); //$NON-NLS-1$ //$NON-NLS-2$
                //$NON-NLS-2$

                // refresh TablePreview on this step
                try {
                    ldapSchemaPreview.refreshTablePreview(csvArray, false, processDescription);
                } catch (final Exception e) {
                    Display.getDefault().asyncExec(new Runnable() {

                        @Override
                        public void run() {
                            handleErrorOutput(outputComposite, tabFolder, outputTabItem, e);
                        }

                    });

                    if (!isContextMode()) {
                        filterText.setText(ConnectionUIConstants.DEFAULT_FILTER);
                        connection.setFilter(ConnectionUIConstants.DEFAULT_FILTER);
                    }
                }
                previewInformationLabel.setText(""); //$NON-NLS-1$
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

    /**
     * 
     * addGroupAttributes.
     */
    /**
     * DOC Administrator Comment method "addGroupAttributes".
     * 
     * @param mainComposite
     * @param width
     * @param height
     */
    private void addGroupAttributes(final Composite mainComposite, final int width, final int height) {
        // Group Schema Viewer
        Group group = Form.createGroup(mainComposite, 1, "List attributes of LDAP Schema", height); //$NON-NLS-1$

        attributeModel = new ExtendedTableModel<String>();
        attributeModel.registerDataList(itemTableNameList);
        tableEditorView = new AbstractExtendedTableViewer<String>(attributeModel, group) {

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.talend.commons.ui.swt.extended.macrotable.AbstractExtendedTableViewer#setTableViewerCreatorOptions
             * (org.talend.commons.ui.swt.tableviewer.TableViewerCreator)
             */
            @Override
            protected void setTableViewerCreatorOptions(TableViewerCreator<String> newTableViewerCreator) {
                super.setTableViewerCreatorOptions(newTableViewerCreator);
                newTableViewerCreator.setFirstColumnMasked(false);
                newTableViewerCreator.setCheckboxInFirstColumn(true);
            }

            @Override
            protected void createColumns(TableViewerCreator<String> tableViewerCreator, Table table) {
                TableViewerCreatorColumn column = new TableViewerCreatorColumn(tableViewerCreator);
                column.setBeanPropertyAccessors(new IBeanPropertyAccessors<String, String>() {

                    @Override
                    public String get(String bean) {
                        return bean.toString();
                    }

                    @Override
                    public void set(String bean, String value) {
                    }

                });
                column.setTitle(Messages.getString("LdifFileStep2Form.columnTtitle.attributes")); //$NON-NLS-1$
                column.setWeight(100);

            }

        };

        // see the feature 6651,qli comment.
        Composite buttonComposite = new Composite(group, SWT.NONE);
        RowLayout buttonLayout = new RowLayout();
        buttonComposite.setLayout(buttonLayout);
        // select all
        final Button selectAllButton = new Button(buttonComposite, SWT.NONE);
        selectAllButton.setText(Messages.getString("LDAPSchemaStep3Form.selectAllText")); //$NON-NLS-1$
        selectAllButton.setToolTipText(Messages.getString("LDAPSchemaStep3Form.selectAllTipText")); //$NON-NLS-1$
        selectAllButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                TableItem[] tableItems = tableEditorView.getTable().getItems();
                if (tableItems != null && tableItems.length > 0) {
                    for (TableItem item : tableItems) {
                        item.setChecked(true);
                        getConnection().getValue().add(item.getText());
                        checkFieldsValue();
                    }
                }
            }
        });
        // select none
        final Button selectNoneButton = new Button(buttonComposite, SWT.NONE);
        selectNoneButton.setText(Messages.getString("LDAPSchemaStep3Form.selectNoneText")); //$NON-NLS-1$
        selectNoneButton.setToolTipText(Messages.getString("LDAPSchemaStep3Form.selectNoneTipText")); //$NON-NLS-1$
        selectNoneButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                TableItem[] tableItems = tableEditorView.getTable().getItems();
                if (tableItems != null && tableItems.length > 0) {
                    for (TableItem item : tableItems) {
                        item.setChecked(false);
                        getConnection().getValue().remove(item.getText());
                        checkFieldsValue();
                    }
                }
            }
        });
    }

    /**
     * add Field to Group File Viewer.
     * 
     * @param parent
     * @param form
     * @param width
     * @param height
     */
    private void addGroupFileViewer(final Composite parent, final int width, int height) {
        // composite LDAP Schema Preview
        // previewGroup = Form.createGroup(parent, 1, Messages.getString("FileStep2.groupPreview"), height);
        // //$NON-NLS-1$

        tabFolder = new CTabFolder(this, SWT.BORDER);
        tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));

        previewTabItem = new CTabItem(tabFolder, SWT.BORDER);
        previewTabItem.setText(Messages.getString("LDAPSchemaStep3Form.preview")); //$NON-NLS-1$
        outputTabItem = new CTabItem(tabFolder, SWT.BORDER);
        outputTabItem.setText(Messages.getString("LDAPSchemaStep3Form.output")); //$NON-NLS-1$

        Composite previewComposite = Form.startNewGridLayout(tabFolder, 1);
        outputComposite = Form.startNewGridLayout(tabFolder, 1);

        Composite compositeLdifFilePreviewButton = Form.startNewDimensionnedGridLayout(previewComposite, 4, width,
                HEIGHT_BUTTON_PIXEL);
        height = height - HEIGHT_BUTTON_PIXEL - 15;

        // LDAP Schema Preview Info
        previewButton = new Button(compositeLdifFilePreviewButton, SWT.NONE);
        previewButton.setText(Messages.getString("FileStep2.refreshPreview")); //$NON-NLS-1$
        previewButton.setSize(WIDTH_BUTTON_PIXEL, HEIGHT_BUTTON_PIXEL);

        // simple space
        new Label(compositeLdifFilePreviewButton, SWT.NONE);
        // Information Label
        previewInformationLabel = new Label(compositeLdifFilePreviewButton, SWT.NONE);
        previewInformationLabel
                .setText("                                                                                                                        "); //$NON-NLS-1$
        previewInformationLabel.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLUE));

        Composite compositeLDAPSchemaPreview = Form.startNewDimensionnedGridLayout(previewComposite, 1, width, height);

        // LDAP Schema Preview
        ldapSchemaPreview = new ShadowProcessPreview(compositeLDAPSchemaPreview, null, width, height - 10);
        ldapSchemaPreview.newTablePreview();

        previewTabItem.setControl(previewComposite);
        outputTabItem.setControl(outputComposite);
        tabFolder.setSelection(previewTabItem);
        tabFolder.pack();
    }

    /**
     * add field to Group Limit.
     * 
     * @param mainComposite
     * @param form
     * @param width
     * @param height
     */
    private void addFilter(final Composite mainComposite, final int width, final int height) {
        // Composite filter rows
        Group group = Form.createGroup(mainComposite, 1, "Filter", height); //$NON-NLS-1$
        Composite compositeFilter = Form.startNewDimensionnedGridLayout(group, 3, width, height);

        // Information Text
        filterText = new Text(compositeFilter, SWT.MULTI | SWT.V_SCROLL);

        GridData gridData = new GridData(GridData.FILL_BOTH);
        filterText.setLayoutData(gridData);
    }

    /**
     * refreshPreview use ShadowProcess to refresh the preview.
     */
    void refreshPreview() {
        processor.execute();
    }

    /**
     * populateLDAPSchemaAttributes method to populate the Table of Attributes to read the ldap schema
     * 
     */
    protected void populateLDAPSchemaAttributes() {

        itemTableNameList = new ArrayList<String>();

        // qli modified to fix the bug "6648".
        // 7349&7545
        final LDAPSchemaConnection originalValueConnection = getOriginalValueConnection();

        SchemaOps schemaOps = getSchema(originalValueConnection);
        Object[] attributeList = LDAPConnectionUtils.getAttributes(schemaOps, originalValueConnection);

        if (attributeList != null && attributeList.length > 0) {
            for (Object object : attributeList) {
                String str = (String) object;
                itemTableNameList.add(str);
            }
        }
    }

    /**
     * 
     * qli comment the method "getSchema".
     * 
     * @param talendLDAPConnection
     * 
     * */
    private SchemaOps getSchema(LDAPSchemaConnection talendLDAPConnection) {
        if (talendLDAPConnection == null) {
            return null;
        }
        SchemaOps schemaOps = null;
        ConnectionData newCon = new ConnectionData();
        newCon.protocol = ConnectionData.LDAP;
        String url = null;
        try {
            url = getURL(talendLDAPConnection.getHost(), talendLDAPConnection.getPort());
        } catch (NumberFormatException e1) {
            e1.printStackTrace();
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
        newCon.setURL(url);
        newCon.referralType = "ignore";//$NON-NLS-1$
        newCon.version = 3;
        newCon.useGSSAPI = false;
        newCon.userDN = talendLDAPConnection.getBindPrincipal();
        String bindPassword = talendLDAPConnection.getValue(talendLDAPConnection.getBindPassword(), false);
        if (bindPassword != null) {
            newCon.pwd = bindPassword.toCharArray();
        }
        newCon.useSSL = false;
        EList baseDNs = talendLDAPConnection.getBaseDNs();
        if (baseDNs != null) {
            newCon.baseDN = (String) baseDNs.get(0);
        }
        if (EEncryptionMethod.SSL_ENCRYPTION_METHOD.getName().equals(talendLDAPConnection.getEncryptionMethodName())) {
            String keystorePath = System.getProperty("java.home") + "\\lib\\security\\cacerts"; //$NON-NLS-1$ //$NON-NLS-2$
            newCon.cacerts = keystorePath;
            newCon.useSSL = true;
        }
        // finish the connection

        JNDIBroker jndiBroker = new JNDIBroker();

        DataConnectionQuery request = (DataConnectionQuery) jndiBroker.connect(newCon);
        ConnectionData cData = request.conData;
        DirContext ctx;
        try {
            dirOps = new CBGraphicsOps(cData);
            ctx = dirOps.getContext();
            schemaOps = new SchemaOps(ctx);
        } catch (NamingException e) {
            ExceptionHandler.process(e);
        }
        return schemaOps;

    }

    /**
     * 
     * qli comment the method "getURL".
     * 
     * @param host, portString
     * 
     * */
    private String getURL(String host, String portString) throws NumberFormatException, URISyntaxException {

        if (host == null || host.length() < 1) {
            return null;
        }

        if (portString == null || portString.length() < 1) {
            return null;
        }

        if (host != null) {
            host = host.trim();
        }

        if (portString != null) {
            portString = portString.trim();
        }

        int port = Integer.parseInt(portString);

        if (port < 0) {
            return null;
        }

        if (port > 65536) {
            return null;
        }

        return "ldap://" + host + ":" + port;//$NON-NLS-1$//$NON-NLS-2$
    }

    /**
     * clear the table preview.
     */
    void clearPreview() {
        ldapSchemaPreview.clearTablePreview();
    }

    /**
     * create ProcessDescription and set it.
     * 
     * WARNING ::field FieldSeparator, RowSeparator, EscapeChar and TextEnclosure are surround by double quote.
     * 
     * @param getConnection()
     * 
     * @return processDescription
     */
    private ProcessDescription getProcessDescription() {

        ProcessDescription processDescription = ShadowProcessHelper.getProcessDescription(getOriginalValueConnection());
        return processDescription;
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

            try {
                populateLDAPSchemaAttributes();
            } catch (Exception e) {
                new ErrorDialogWidthDetailArea(getShell(), PID,
                        Messages.getString("LdifFileStep2.previewFailure"), e.getMessage()); //$NON-NLS-1$
                log.error(Messages.getString("LdifFileStep2.previewFailure") + " " + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
                updateStatus(IStatus.ERROR, Messages.getString("LdifFileStep2.previewFailure")); //$NON-NLS-1$
            }
            attributeModel.registerDataList(itemTableNameList);

            EList attributeValueList = getConnection().getValue();
            boolean isRefreshingPreview = false;
            if (attributeValueList != null && !attributeValueList.isEmpty()) {
                isRefreshingPreview = true;
                refreshPreview();
                checkTheRightAttributes(attributeValueList);
            }
            if (isReadOnly() != readOnly) {
                adaptFormToReadOnly();
            }
            initialize();
            adaptFormToEditable();
            if (isRefreshingPreview && processor != null && !processor.isStopped()) {
                updateStatus(IStatus.ERROR, null);
            }
        }
    }

    /**
     * checkTheRightAttributes.
     * 
     * @param getConnection().getValue() Checked Attribute Checked in EMF model
     */
    protected void checkTheRightAttributes(List<String> attribute) {

        TableItem[] tableItems = tableEditorView.getTableViewerCreator().getTable().getItems();
        for (TableItem tableItem : tableItems) {
            for (int i = 0; i < attribute.size(); i++) {
                String attributeName = attribute.get(i);
                if (attributeName != null && !("").equals(attributeName)) { //$NON-NLS-1$
                    if (tableItem.getText().equals(attributeName)) {
                        tableItem.setChecked(true);
                        break;
                    }
                }
            }
        }
    }

    @Override
    protected void adaptFormToEditable() {
        super.adaptFormToEditable();
        filterText.setEditable(!isContextMode());
    }

    @Override
    protected void processWhenDispose() {
        if (processor != null) {
            processor.forceStop();
        }
    }
}
