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
package org.talend.repository.ui.wizards.metadata.connection.wsdl;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledCombo;
import org.talend.commons.ui.swt.thread.SWTUIThreadProcessor;
import org.talend.core.PluginChecker;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.EMetadataEncoding;
import org.talend.core.model.metadata.IMetadataContextModeManager;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.utils.CsvArray;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.metadata.managment.ui.model.TableViewerContentProvider;
import org.talend.metadata.managment.ui.preview.ProcessDescription;
import org.talend.metadata.managment.ui.preview.ShadowProcessPreview;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;
import org.talend.metadata.managment.ui.utils.ShadowProcessHelper;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.metadata.ui.wizards.form.AbstractWSDLSchemaStepForm;
import org.talend.repository.ui.wizards.metadata.connection.ldap.BaseWidgetUtils;

/**
 * DOC qwei class global comment. Detailled comment
 */
public class WSDLSchemaStep1Form extends AbstractWSDLSchemaStepForm {

    public static final int TIMEOUT = 20;

    /** Endpoint URI */
    private Text endPointURI;

    private LabelledCombo encodingCombo;

    /** WSDL text. */
    private Text wsdlText;

    /** Method text. */
    private Text methodText;

    /** username text. */
    private Text userNameText;

    private Label useLabel;

    /** Password text. */
    private Text passWordText;

    private Label password;

    /** Need Auth */
    private Button needAuth;

    /** Parameters */
    private Table table;

    private Button buttonAdd, buttonRemove;

    private TableViewer valueTableViewer;

    private static final String VALUE_PROPERTY = "Value"; //$NON-NLS-1$

    // private Button firstRowIsCaptionCheckbox;

    private Button previewButton;

    private Label previewInformationLabel;

    private ShadowProcessPreview wsdlPreview;

    /** http proxy */
    private Button useProxy;

    private Text proxyHost;

    private Label hostLabel;

    private Text proxyPort;

    private Label portLabel;

    private Text proxyUser;

    private Label userProLabel;

    private Text proxyPassword;

    private Label passwordProLabel;

    private Text timeOut;

    private Label timeOutLabel;

    SWTUIThreadProcessor processor = new PreviewProcessor();

    private static Logger log = Logger.getLogger(WSDLSchemaStep1Form.class);

    /**
     * Output tab.
     */
    private CTabFolder tabFolder;

    private CTabItem previewTabItem;

    private CTabItem outputTabItem;

    private Composite outputComposite;

    /**
     * WSDLSchemaStep2Form constructor comment.
     * 
     * @param parent
     * @param connectionItem
     * @param metadataTable
     * @param tableNames
     */
    public WSDLSchemaStep1Form(Composite parent, ConnectionItem connectionItem, MetadataTable metadataTable, String[] tableNames,
            IMetadataContextModeManager contextModeManager) {
        super(parent, connectionItem, metadataTable, tableNames);
        setConnectionItem(connectionItem);
        setContextModeManager(contextModeManager);
        setupForm(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFields()
     */
    @Override
    protected void addFields() {

        Composite composite = BaseWidgetUtils.createColumnContainer(this, 1, 1);
        BaseWidgetUtils.createSpacer(composite, 1);
        Group group = BaseWidgetUtils.createGroup(composite, Messages.getString("WSDLSchemaStep1Form.WSDLGroupParameter"), 1); //$NON-NLS-1$
        Composite groupComposite = BaseWidgetUtils.createColumnContainer(group, 4, 1);
        // ((GridData) group.getLayoutData()).heightHint = 260;
        BaseWidgetUtils.createLabel(groupComposite, Messages.getString("WSDLSchemaStep1Form.WSDLName"), 1); //$NON-NLS-1$
        wsdlText = BaseWidgetUtils.createText(groupComposite, Messages.getString("WSDLSchemaStep1Form.WSDLURL"), 3); //$NON-NLS-1$
        if (LanguageManager.getCurrentLanguage().equals(ECodeLanguage.JAVA)) {
            needAuth = BaseWidgetUtils.createCheckbox(groupComposite, Messages.getString("WSDLSchemaStep1Form.NeedAuth"), 4); //$NON-NLS-1$
            useLabel = BaseWidgetUtils.createLabel(groupComposite, Messages.getString("WSDLSchemaStep1Form.UserName"), 1); //$NON-NLS-1$
            userNameText = BaseWidgetUtils.createText(groupComposite, "", 1); //$NON-NLS-1$
            password = BaseWidgetUtils.createLabel(groupComposite, Messages.getString("WSDLSchemaStep1Form.Password"), 1); //$NON-NLS-1$
            passWordText = BaseWidgetUtils.createText(groupComposite, "", 1); //$NON-NLS-1$
            useProxy = BaseWidgetUtils.createCheckbox(groupComposite, Messages.getString("WSDLSchemaStep1Form.UseProxy"), 4); //$NON-NLS-1$
            hostLabel = BaseWidgetUtils.createLabel(groupComposite, Messages.getString("WSDLSchemaStep1Form.ProxyHost"), 1); //$NON-NLS-1$
            proxyHost = BaseWidgetUtils.createText(groupComposite, "", 1); //$NON-NLS-1$
            portLabel = BaseWidgetUtils.createLabel(groupComposite, Messages.getString("WSDLSchemaStep1Form.ProxyPort"), 1); //$NON-NLS-1$
            proxyPort = BaseWidgetUtils.createText(groupComposite, "", 1); //$NON-NLS-1$
            userProLabel = BaseWidgetUtils.createLabel(groupComposite, Messages.getString("WSDLSchemaStep1Form.ProxyUser"), 1); //$NON-NLS-1$
            proxyUser = BaseWidgetUtils.createText(groupComposite, "", 1); //$NON-NLS-1$
            passwordProLabel = BaseWidgetUtils.createLabel(groupComposite,
                    Messages.getString("WSDLSchemaStep1Form.ProxyPassword"), 1); //$NON-NLS-1$
            proxyPassword = BaseWidgetUtils.createText(groupComposite, "", 1); //$NON-NLS-1$
        } else if (LanguageManager.getCurrentLanguage().equals(ECodeLanguage.PERL)) {
            BaseWidgetUtils.createLabel(groupComposite, Messages.getString("WSDLSchemaStep1Form.ENDPOINTURI"), 1); //$NON-NLS-1$
            endPointURI = BaseWidgetUtils.createText(groupComposite, "", 3); //$NON-NLS-1$
            EMetadataEncoding[] values = EMetadataEncoding.values();
            String[] encodingData = new String[values.length];
            for (int j = 0; j < values.length; j++) {
                encodingData[j] = values[j].getName();
            }

            encodingCombo = new LabelledCombo(groupComposite, Messages.getString("FileStep2.encoding"), Messages //$NON-NLS-1$
                    .getString("FileStep2.encodingTip"), encodingData, 3, true, SWT.NONE); //$NON-NLS-1$
        }
        BaseWidgetUtils.createLabel(groupComposite, Messages.getString("WSDLSchemaStep1Form.MethodName"), 1); //$NON-NLS-1$
        methodText = BaseWidgetUtils.createText(groupComposite, "", 3); //$NON-NLS-1$
        timeOutLabel = BaseWidgetUtils.createLabel(groupComposite, Messages.getString("WSDLSchemaStep1Form.TimeOutTitle"), 1); //$NON-NLS-1$
        timeOut = BaseWidgetUtils.createText(groupComposite, String.valueOf(TIMEOUT), 1);
        BaseWidgetUtils.createLabel(groupComposite, Messages.getString("WSDLSchemaStep1Form.Parameters"), 4); //$NON-NLS-1$
        valueTableViewer = new TableViewer(groupComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
        TableViewerContentProvider provider = new TableViewerContentProvider();
        valueTableViewer.setContentProvider(provider);
        valueTableViewer.setLabelProvider(provider);
        valueTableViewer.setCellModifier(new ICellModifier() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
             */
            @Override
            public boolean canModify(Object element, String property) {
                return true;
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
             */
            @Override
            public Object getValue(Object element, String property) {
                if (VALUE_PROPERTY.equals(property)) {
                    return element;
                }
                return ""; //$NON-NLS-1$
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
             */
            @Override
            public void modify(Object element, String property, Object value) {
                if (VALUE_PROPERTY.equals(property)) {
                    ArrayList list = (ArrayList) valueTableViewer.getInput();
                    int index = valueTableViewer.getTable().getSelectionIndex();
                    if (index > -1) {
                        list.set(index, value);
                    }
                    // var.setValue((String) value);
                }
                valueTableViewer.refresh();
            }

        });
        table = valueTableViewer.getTable();
        valueTableViewer.setCellEditors(new CellEditor[] { new TextCellEditor(table), new TextCellEditor(table) });
        valueTableViewer.setColumnProperties(new String[] { VALUE_PROPERTY });
        valueTableViewer.setInput(getConnection().getParameters());
        // valueTableViewer.setInput(new HashMap<ColumnValue,ColumnValue>());
        table.setHeaderVisible(true);

        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 4;
        gd.heightHint = 50;
        table.setLayoutData(gd);
        final TableColumn valueColumn = new TableColumn(table, SWT.NONE);
        valueColumn.setWidth(200);
        valueColumn.setText(Messages.getString("WSDLSchemaStep1Form.ParColumnValue")); //$NON-NLS-1$
        Composite buttonPart = new Composite(group, SWT.NONE);
        buttonPart.setLayout(new GridLayout(2, false));
        buttonPart.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
        buttonAdd = new Button(buttonPart, SWT.NONE);
        buttonAdd.setText(Messages.getString("WSDLSchemaStep1Form.ParameterAdd")); //$NON-NLS-1$
        buttonAdd.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
        buttonRemove = new Button(buttonPart, SWT.NONE);
        buttonRemove.setText(Messages.getString("WSDLSchemaStep1Form.ParameterRemove")); //$NON-NLS-1$
        buttonRemove.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
        addGroupFileViewer(this, 700, 135);

    }

    /**
     * DOC qwei Comment method "addadvancedWSDLExplorer".
     */
    private void addadvancedWSDLExplorer(final Composite parent) {

        if (!PluginChecker.isWSDLPluginLoaded()) {
            return;
        }

        Button wsdlButton = new Button(parent, SWT.NONE);
        wsdlButton.setText("Web Service Explorer"); //$NON-NLS-1$
        wsdlButton.setAlignment(SWT.RIGHT);
        wsdlButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Plugin plugin = PluginChecker.getWSDLPlugin();
                if (plugin instanceof IWSDLExplorerAccessor) {
                    IWSDLExplorerAccessor accessor = (IWSDLExplorerAccessor) plugin;
                    // Shell shell = new Shell(getParent().getShell().getDisplay());
                    accessor.openWSExplorer(getShell());
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

        tabFolder = new CTabFolder(parent, SWT.BORDER);
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = height;
        tabFolder.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        previewTabItem = new CTabItem(tabFolder, SWT.BORDER);
        previewTabItem.setText(Messages.getString("WSDLSchemaStep1Form.preview")); //$NON-NLS-1$
        outputTabItem = new CTabItem(tabFolder, SWT.BORDER);
        outputTabItem.setText(Messages.getString("WSDLSchemaStep1Form.output")); //$NON-NLS-1$

        Composite previewComposite = Form.startNewGridLayout(tabFolder, 1);
        outputComposite = Form.startNewGridLayout(tabFolder, 1);

        // composite Delimited File Preview
        // previewGroup = Form.createGroup(parent, 1, Messages.getString("FileStep2.groupPreview"), height);
        // //$NON-NLS-1$
        Composite compositeDelimitedFilePreviewButton = Form.startNewDimensionnedGridLayout(previewComposite, 4, width,
                HEIGHT_BUTTON_PIXEL);
        height = height - HEIGHT_BUTTON_PIXEL - 15;

        // Delimited File Preview Info
        // firstRowIsCaptionCheckbox = new Button(compositeDelimitedFilePreviewButton, SWT.CHECK);
        // firstRowIsCaptionCheckbox.setText(Messages.getString("FileStep2.firstRowsIsCaption")); //$NON-NLS-1$
        // firstRowIsCaptionCheckbox.setAlignment(SWT.LEFT);
        previewButton = new Button(compositeDelimitedFilePreviewButton, SWT.NONE);
        previewButton.setText(Messages.getString("FileStep2.refreshPreview")); //$NON-NLS-1$
        previewButton.setSize(WIDTH_BUTTON_PIXEL, HEIGHT_BUTTON_PIXEL);
        addadvancedWSDLExplorer(compositeDelimitedFilePreviewButton);

        // simple space
        new Label(compositeDelimitedFilePreviewButton, SWT.NONE);
        // Information Label
        previewInformationLabel = new Label(compositeDelimitedFilePreviewButton, SWT.NONE);
        previewInformationLabel
                .setText("                                                                                                                        "); //$NON-NLS-1$
        previewInformationLabel.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLUE));

        Composite compositeDelimitedFilePreview = Form.startNewDimensionnedGridLayout(previewComposite, 1, width, height);

        // Delimited File Preview
        wsdlPreview = new ShadowProcessPreview(compositeDelimitedFilePreview, null, width, height - 10);
        wsdlPreview.newTablePreview();

        previewTabItem.setControl(previewComposite);
        outputTabItem.setControl(outputComposite);
        tabFolder.setSelection(previewTabItem);
        tabFolder.pack();
    }

    private void addJavaFieldsListeners() {
        needAuth.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                checkFieldsValue();
                setNeedAuthEnable(needAuth.getSelection());
                getConnection().setNeedAuth(needAuth.getSelection());
            }

        });
        userNameText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    checkFieldsValue();
                    getConnection().setUserName(userNameText.getText());
                }
            }

        });
        passWordText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    checkFieldsValue();
                    WSDLSchemaConnection conn = getConnection();
                    conn.setPassword(conn.getValue(passWordText.getText(), true));
                }
            }

        });
        useProxy.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                checkFieldsValue();
                setUseProxyEnable(useProxy.getSelection());
                getConnection().setUseProxy(useProxy.getSelection());
            }

        });
        proxyHost.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    checkFieldsValue();
                    getConnection().setProxyHost(proxyHost.getText());
                }

            }

        });
        proxyPort.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    checkFieldsValue();
                    getConnection().setProxyPort(proxyPort.getText());
                }

            }

        });
        proxyUser.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    checkFieldsValue();
                    getConnection().setProxyUser(proxyUser.getText());
                }
            }

        });
        proxyPassword.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    checkFieldsValue();
                    WSDLSchemaConnection wsdlConn = getConnection();
                    wsdlConn.setProxyPassword(wsdlConn.getValue(proxyPassword.getText(), true));
                }
            }

        });
        timeOut.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    checkFieldsValue();
                    if (timeOut.getText() != null && !"".equals(timeOut.getText())) { //$NON-NLS-1$ 
                        try {
                            int timeInt = Integer.valueOf(timeOut.getText());
                            if (timeInt < 0) {
                                getConnection().setTimeOut(0);
                            } else {
                                getConnection().setTimeOut(timeInt);
                            }
                        } catch (Exception e1) {
                            getConnection().setTimeOut(TIMEOUT);
                        }
                    } else {
                        getConnection().setTimeOut(TIMEOUT);
                    }
                }
            }

        });
    }

    private void addPerlFieldsListeners() {
        endPointURI.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    checkFieldsValue();
                    getConnection().setEndpointURI(endPointURI.getText());
                    if (methodText.getText() != null && !methodText.getText().equals("") && wsdlText.getText() != null //$NON-NLS-1$
                            && !wsdlText.getText().equals("")) { //$NON-NLS-1$
                        updateStatus(IStatus.OK, null);
                    }
                }
            }

        });
        encodingCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().setEncoding(encodingCombo.getText());
                    checkFieldsValue();
                    if (methodText.getText() != null && !methodText.getText().equals("") && wsdlText.getText() != null //$NON-NLS-1$
                            && !wsdlText.getText().equals("")) { //$NON-NLS-1$
                        updateStatus(IStatus.OK, null);
                    }
                }
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFieldsListeners()
     */
    @Override
    protected void addFieldsListeners() {

        if (LanguageManager.getCurrentLanguage().equals(ECodeLanguage.JAVA)) {
            addJavaFieldsListeners();
        } else if (LanguageManager.getCurrentLanguage().equals(ECodeLanguage.PERL)) {
            addPerlFieldsListeners();
        }

        wsdlText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    boolean noError = checkFieldsValue();
                    getConnection().setWSDL(wsdlText.getText());
                    if (noError && methodText.getText() != null && !methodText.getText().equals("")) { //$NON-NLS-1$
                        updateStatus(IStatus.OK, null);
                    }
                }
            }

        });
        methodText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    boolean noError = checkFieldsValue();
                    getConnection().setMethodName(methodText.getText());
                    if (noError && wsdlText.getText() != null && !wsdlText.getText().equals("")) { //$NON-NLS-1$
                        updateStatus(IStatus.OK, null);
                    }
                }
            }

        });
        buttonAdd.addMouseListener(new MouseAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.MouseAdapter#mouseUp(org.eclipse.swt.events.MouseEvent)
             */
            @Override
            public void mouseUp(MouseEvent e) {
                String unName = "newLine_"; //$NON-NLS-1$
                ArrayList hashmap = getConnection().getParameters();
                if (hashmap == null) {
                    hashmap = new ArrayList();
                }
                hashmap.add(unName + hashmap.size());
                // for (ColumnValue columnValue : input) {
                // hashmap.put(columnValue, columnValue.getValue());
                // }
                getConnection().setParameters(hashmap);
                valueTableViewer.setInput(hashmap);
                valueTableViewer.refresh();
            }
        });
        buttonRemove.addMouseListener(new MouseAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.MouseAdapter#mouseUp(org.eclipse.swt.events.MouseEvent)
             */
            @Override
            public void mouseUp(MouseEvent e) {
                ISelection selection = valueTableViewer.getSelection();
                ArrayList hashmap = getConnection().getParameters();
                if (!selection.isEmpty() && selection instanceof StructuredSelection) {
                    Object[] vars = ((StructuredSelection) selection).toArray();
                    for (Object var : vars) {
                        hashmap.remove(var);
                        valueTableViewer.refresh();
                    }
                } else if (!hashmap.isEmpty()) {
                    hashmap.remove(hashmap.size() - 1);
                    valueTableViewer.refresh();
                }
                // HashMap<ColumnValue, String> hashmap = new HashMap<ColumnValue, String>();
                // for (ColumnValue columnValue : list) {
                // hashmap.put(columnValue, columnValue.getValue());
                // }
                getConnection().setParameters(hashmap);
            }
        });

        previewButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                refreshPreview();
                // updateStatus(IStatus.OK, null);
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
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#initialize()
     */
    @Override
    protected void initialize() {
        WSDLSchemaConnection wsdlConn = getConnection();
        String wsdlUrl = wsdlConn.getWSDL();
        this.wsdlText.setText(wsdlUrl == null ? "" : wsdlUrl); //$NON-NLS-1$
        if (LanguageManager.getCurrentLanguage().equals(ECodeLanguage.JAVA)) {
            boolean needAuth2 = wsdlConn.isNeedAuth();
            String userName = wsdlConn.getUserName();
            this.userNameText.setText(userName == null ? "" : userName); //$NON-NLS-1$
            String password2 = wsdlConn.getValue(wsdlConn.getPassword(), false);
            this.passWordText.setText(password2 == null ? "" : password2); //$NON-NLS-1$
            setNeedAuthEnable(needAuth2);
            boolean useproxy = wsdlConn.isUseProxy();

            String proxyHost = wsdlConn.getProxyHost();
            this.proxyHost.setText(proxyHost == null ? "" : proxyHost); //$NON-NLS-1$
            String proxyPort = wsdlConn.getProxyPort();
            this.proxyPort.setText(proxyPort == null ? "" : proxyPort); //$NON-NLS-1$
            String proxyUser = wsdlConn.getProxyUser();
            this.proxyUser.setText(proxyUser == null ? "" : proxyUser); //$NON-NLS-1$
            String proxyPass = wsdlConn.getValue(wsdlConn.getProxyPassword(), false);
            this.proxyPassword.setText(proxyPass == null ? "" : proxyPass); //$NON-NLS-1$
            int timeOut = wsdlConn.getTimeOut();
            this.timeOut.setText(timeOut < 0 ? "0" : String.valueOf(timeOut)); //$NON-NLS-1$
            setUseProxyEnable(useproxy);
        } else if (LanguageManager.getCurrentLanguage().equals(ECodeLanguage.PERL)) {
            String endPointURI = wsdlConn.getEndpointURI();
            this.endPointURI.setText(endPointURI == null ? "" : endPointURI); //$NON-NLS-1$
            if (wsdlConn.getEncoding() != null && !wsdlConn.getEncoding().equals("")) { //$NON-NLS-1$
                encodingCombo.setText(wsdlConn.getEncoding());
            } else {
                encodingCombo.select(0);
            }
            encodingCombo.clearSelection();
        }
        String method = wsdlConn.getMethodName();
        this.methodText.setText(method == null ? "" : method); //$NON-NLS-1$
        ArrayList hashparameter = wsdlConn.getParameters();
        // Object[] objs = hashparameter.values().toArray();
        // LinkedList<ColumnValue> list = new LinkedList<ColumnValue>();
        // for (Object object : objs) {
        // list.add((ColumnValue) object);
        // }
        this.valueTableViewer.setInput(hashparameter);
        checkFieldsValue();

    }

    private void setNeedAuthEnable(boolean b) {
        this.needAuth.setSelection(b);
        this.useLabel.setEnabled(b);
        this.password.setEnabled(b);
        if (!isContextMode()) {
            this.userNameText.setEnabled(b);
            this.passWordText.setEnabled(b);
        }
    }

    private void setUseProxyEnable(boolean b) {
        this.useProxy.setSelection(b);
        this.hostLabel.setEnabled(b);
        this.portLabel.setEnabled(b);
        this.userProLabel.setEnabled(b);
        this.passwordProLabel.setEnabled(b);
        if (!isContextMode()) {
            this.proxyHost.setEnabled(b);
            this.proxyPort.setEnabled(b);
            this.proxyUser.setEnabled(b);
            this.proxyPassword.setEnabled(b);
        }
    }

    /**
     * refreshPreview use ShadowProcess to refresh the preview.
     */
    void refreshPreview() {
        processor.execute();

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#adaptFormToReadOnly()
     */
    @Override
    protected void adaptFormToReadOnly() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#checkFieldsValue()
     */
    @Override
    protected boolean checkFieldsValue() {
        // TODO Auto-generated method stub
        if (LanguageManager.getCurrentLanguage().equals(ECodeLanguage.JAVA)) {
            return checkJavaFieldsValue();
        } else if (LanguageManager.getCurrentLanguage().equals(ECodeLanguage.PERL)) {
            return checkPerlFieldsValue();
        } else {
            updateStatus(IStatus.ERROR, null);
            return false;
        }

    }

    private boolean checkJavaFieldsValue() {
        if (wsdlText.getText() == null || wsdlText.getText().equals("")) { //$NON-NLS-1$
            updateStatus(IStatus.ERROR, "WSDL URL must be specified."); //$NON-NLS-1$
            return false;
        } else if (methodText.getText() == null || methodText.getText().equals("")) { //$NON-NLS-1$
            updateStatus(IStatus.ERROR, "Method must be specified."); //$NON-NLS-1$
            return false;
        } else if (needAuth.getSelection() && (userNameText.getText() == null || userNameText.getText().equals(""))) { //$NON-NLS-1$
            updateStatus(IStatus.ERROR, "User Name must be specified."); //$NON-NLS-1$
            return false;
        } else if (needAuth.getSelection() && (passWordText.getText() == null || passWordText.getText().equals(""))) { //$NON-NLS-1$
            updateStatus(IStatus.ERROR, "Password must be specified."); //$NON-NLS-1$
            return false;
        } else if (useProxy.getSelection() && (proxyHost.getText() == null || proxyHost.getText().equals(""))) { //$NON-NLS-1$
            updateStatus(IStatus.ERROR, "Proxy host must be specified."); //$NON-NLS-1$
            return false;
        } else if (useProxy.getSelection() && (proxyPort.getText() == null || proxyPort.getText().equals(""))) { //$NON-NLS-1$
            updateStatus(IStatus.ERROR, "Proxy port must be specified."); //$NON-NLS-1$
            return false;
        } else if (useProxy.getSelection() && (proxyUser.getText() == null || proxyUser.getText().equals(""))) { //$NON-NLS-1$
            updateStatus(IStatus.ERROR, "Proxy user must be specified."); //$NON-NLS-1$
            return false;
        } else if (useProxy.getSelection() && (proxyPassword.getText() == null || proxyPassword.getText().equals(""))) { //$NON-NLS-1$
            updateStatus(IStatus.ERROR, "Proxy password must be specified."); //$NON-NLS-1$
            return false;
        } else {
            updateStatus(IStatus.OK, null);
            return true;
        }

    }

    private boolean checkPerlFieldsValue() {
        if (wsdlText.getText() == null || wsdlText.getText().equals("")) { //$NON-NLS-1$
            updateStatus(IStatus.ERROR, "WSDL URL must be specified."); //$NON-NLS-1$
            return false;
        } else if (endPointURI.getText() == null || endPointURI.getText().equals("")) { //$NON-NLS-1$
            updateStatus(IStatus.ERROR, "Endpoint URI must be specified."); //$NON-NLS-1$
            return false;
        } else if (methodText.getText() == null || methodText.getText().equals("")) { //$NON-NLS-1$
            updateStatus(IStatus.ERROR, "Method must be specified."); //$NON-NLS-1$
            return false;
        } else {
            updateStatus(IStatus.OK, null);
            return true;
        }
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
                if (!previewButton.isDisposed()) {
                    previewButton.setText(Messages.getString("FileStep2.refreshPreview")); //$NON-NLS-1$
                    previewButton.setEnabled(true);
                }
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
                csvArray = ShadowProcessHelper.getCsvArray(processDescription, "WSDL_SCHEMA", true); //$NON-NLS-1$

            } catch (Exception e) {
                setException(e);
                log.error(Messages.getString("FileStep2.previewFailure") + " " + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }

        @Override
        public void updateUIInThreadIfThreadIsNotCanceled() {
            if (previewInformationLabel.isDisposed()) {
                return;
            }
            if (getException() != null) {
                previewInformationLabel.setText(" " + Messages.getString("FileStep2.previewFailure")); //$NON-NLS-1$ //$NON-NLS-2$
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
                //$NON-NLS-2$
                // MessageDialog.openError(getShell(), "Error", "Preview refresh failed, please check attributes and
                // filter.");
            } else {
                previewInformationLabel.setText(" " + Messages.getString("FileStep2.previewIsDone")); //$NON-NLS-1$ //$NON-NLS-2$
                //$NON-NLS-2$

                // refresh TablePreview on this step
                try {
                    wsdlPreview.refreshTablePreview(csvArray, false, processDescription);
                } catch (Exception e) {
                    // MessageDialog.openError(getShell(), "Error", "Preview refresh failed, please check attributes and
                    // filter.");
                }
                previewInformationLabel.setText(""); //$NON-NLS-1$
            }
        }

        @Override
        public void updateUIInThreadIfThreadIsCanceled() {
            if (!previewInformationLabel.isDisposed()) {
                previewInformationLabel.setText(""); //$NON-NLS-1$
            }

            if (getException() != null) {
                Display.getDefault().syncExec(new Runnable() {

                    @Override
                    public void run() {
                        handleErrorOutput(outputComposite, tabFolder, outputTabItem);
                    }

                });
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
     * clear the table preview.
     */
    void clearPreview() {
        wsdlPreview.clearTablePreview();
    }

    private ProcessDescription getProcessDescription() {
        if (isContextMode() && getContextModeManager() != null) {
            ContextType contextTypeForContextMode = ConnectionContextHelper.getContextTypeForContextMode(getShell(),
                    connectionItem.getConnection());
            getContextModeManager().setSelectedContextType(contextTypeForContextMode);
        }
        ProcessDescription processDescription = ShadowProcessHelper.getProcessDescription(getOriginalValueConnection());
        return processDescription;
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            initialize();
            adaptFormToEditable();
            collectContextParams(visible);
        }
        if (visible) {
            updateStatus(getStatusLevel(), getStatus());
        }

    }

    @Override
    protected void adaptFormToEditable() {
        super.adaptFormToEditable();
        wsdlText.setEditable(!isContextMode());
        methodText.setEditable(!isContextMode());
        if (userNameText != null) {
            userNameText.setEditable(!isContextMode());
        }
        if (passWordText != null) {
            passWordText.setEditable(!isContextMode());
            if (isContextMode()) {
                passWordText.setEchoChar('\0');
            } else {
                passWordText.setEchoChar('*');
            }
        }
        if (proxyHost != null) {
            proxyHost.setEditable(!isContextMode());
        }
        if (proxyPort != null) {
            proxyPort.setEditable(!isContextMode());
        }
        if (proxyUser != null) {
            proxyUser.setEditable(!isContextMode());
        }
        if (proxyPassword != null) {
            proxyPassword.setEditable(!isContextMode());
            if (isContextMode()) {
                proxyPassword.setEchoChar('\0');
            } else {
                proxyPassword.setEchoChar('*');
            }
        }
        if (endPointURI != null) {
            endPointURI.setEditable(!isContextMode());
        }
        if (encodingCombo != null) {
            encodingCombo.setReadOnly(isContextMode());
        }
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
}
