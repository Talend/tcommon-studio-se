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
package org.talend.repository.metadata.ui.wizards.form;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataContextModeManager;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.SalesforceSchemaConnection;
import org.talend.core.model.process.AbstractNode;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.utils.RepositoryManagerHelper;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.metadata.managment.ui.wizard.AbstractForm;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.ui.wizards.metadata.connection.files.salesforce.ISalesforceModuleParser;
import org.talend.repository.ui.wizards.metadata.connection.files.salesforce.SalesforceModuleParseAPI;
import org.talend.repository.ui.wizards.metadata.connection.files.salesforce.SalesforceModuleParserPartner;
import org.talend.salesforce.SforceConnection;
import org.talend.salesforce.SforceManagement;
import org.talend.salesforce.SforceSessionConnection;
import org.talend.salesforce.oauth.OAuthClient;
import org.talend.salesforce.oauth.Token;

import com.salesforce.soap.partner.DescribeSObjectResult;
import com.salesforce.soap.partner.Field;
import com.sforce.soap.enterprise.DescribeGlobalResult;
import com.sforce.soap.enterprise.SoapBindingStub;
import com.sforce.soap.enterprise.fault.UnexpectedErrorFault;

/**
 * DOC YeXiaowei class global comment. Detailled comment <br/>
 * 
 */
public abstract class AbstractSalesforceStepForm extends AbstractForm {

    protected int maximumRowsToPreview = RepositoryManagerHelper.getMaximumRowsToPreview();

    protected SalesforceSchemaConnection connection;

    protected AbstractNode fakeSalesforceNode = null;

    private final String tSalesforceUniqueName = "tSalesforceInput"; //$NON-NLS-1$

    private final String BASIC = "basic";

    private SalesforceModuleParseAPI salesforceAPI = null;

    private IMetadataContextModeManager contextModeManager;

    private SoapBindingStub binding = null;

    private SforceManagement sforceMgr = null;

    // private com.salesforce.soap.partner.SoapBindingStub bindingPartner = null;

    public static final String TSALESFORCE_INPUT_URL = "https://login.salesforce.com/services/Soap/u/34.0"; //$NON-NLS-1$

    public static final String TSALESFORCE_INPUT_URL_OAUTH = "https://login.salesforce.com/services/oauth2"; //$NON-NLS-1$

    public static final String TSALESFORCE_VERSION = "34.0";

    public static final String TSALESFORCE_PARTNER_INPUT_URL = "https://test.salesforce.com/services/Soap/u/10.0"; //$NON-NLS-1$

    // note that tSalesforceInput use a different url, if the web service is called by wizard we should use
    // DEFAULT_WEB_SERVICE_URL, if the web service is called by tSalesforceInput we should use TSALESFORCE_INPUT_URL
    public static final String DEFAULT_WEB_SERVICE_URL = "https://www.salesforce.com/services/Soap/u/8.0"; //$NON-NLS-1$

    public static final String DEFAULT_WEB_SERVICE_FOR_SOQL_URL = "https://www.salesforce.com/services/Soap/c/8.0"; //$NON-NLS-1$

    public static final String TSALESFORCE_CUSTOM_MODULE = "org.talend.salesforce.custom.module"; //$NON-NLS-1$

    public static final String TSALESFORCE_CUSTOM_MODULE_SPILT = ","; //$NON-NLS-1$

    public boolean useAlphbet;

    public IMetadataTable metadataTableOrder;

    public IMetadataTable metadataTableClone;

    public AbstractSalesforceStepForm(Composite parent, ConnectionItem connectionItem, String[] existingNames,
            SalesforceModuleParseAPI salesforceAPI) {
        super(parent, SWT.NONE, existingNames);
        setConnectionItem(connectionItem);
        this.salesforceAPI = salesforceAPI;
    }

    public AbstractSalesforceStepForm(Composite parent, ConnectionItem connectionItem, SalesforceModuleParseAPI salesforceAPI) {
        this(parent, connectionItem, null, salesforceAPI);
    }

    public AbstractSalesforceStepForm(Composite parent, ConnectionItem connectionItem, MetadataTable metadataTable,
            String[] existingNames, SalesforceModuleParseAPI salesforceAPI) {
        super(parent, SWT.NONE, existingNames);
        setConnectionItem(connectionItem);
        this.salesforceAPI = salesforceAPI;
    }

    protected SalesforceSchemaConnection getConnection() {
        return (SalesforceSchemaConnection) connectionItem.getConnection();
    }

    public boolean isPerlProject() {
        ECodeLanguage codeLanguage = LanguageManager.getCurrentLanguage();
        return (codeLanguage == ECodeLanguage.PERL);
    }

    /**
     * 
     * DOC YeXiaowei Comment method "getSalesforceComponent".
     * 
     * @return Always not null
     */
    public INode getSalesforceNode() {
        return CoreRuntimePlugin.getInstance().getDesignerCoreService().getRefrenceNode(tSalesforceUniqueName);
    }

    public IMetadataTable getMetadatasForSalesforce(String endPoint, String user, String pass, String timeOut, String moduleName,
            String betchSize, boolean useProxy, boolean useHttp, String proxyHost, String proxyPort, String proxyUsername,
            String proxyPassword, boolean update) {

        IMetadataTable result = null;
        String proxy = null;
        if (useProxy) {
            proxy = SalesforceModuleParseAPI.USE_SOCKS_PROXY;
        } else if (useHttp) {
            proxy = SalesforceModuleParseAPI.USE_HTTP_PROXY;
        }
        if (!moduleName.equals(salesforceAPI.getCurrentModuleName())) {
            result = getMetadataTableBySalesforceServerAPI(endPoint, user, pass, timeOut, moduleName, proxy, proxyHost,
                    proxyPort, proxyUsername, proxyPassword);
            if (result == null) {
                result = getMetadataTableFromConfigFile(moduleName);
            }
            return result;
        } else {
            if (update) {
                result = getMetadataTableBySalesforceServerAPI(endPoint, user, pass, timeOut, moduleName, proxy, proxyHost,
                        proxyPort, proxyUsername, proxyPassword);
                if (result == null) {
                    result = getMetadataTableFromConfigFile(moduleName);
                }
                return result;
            } else {
                IMetadataTable metadataTable = new org.talend.core.model.metadata.MetadataTable();
                metadataTable.setListColumns(salesforceAPI.getCurrentMetadataColumns());
                return metadataTable;
            }
        }
    }

    private Field[] fetchSFDescriptionField(String module, org.talend.salesforce.SforceManagement sforceManagement) {

        DescribeSObjectResult r;
        try {
            r = sforceManagement.describeSObject(module);
            Field[] fields = r.getFields();
            return fields;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private IMetadataColumn parseFieldToMetadataColumn(Field field) {

        if (field == null) {
            return null;
        }

        IMetadataColumn mdColumn = new org.talend.core.model.metadata.MetadataColumn();

        mdColumn.setLabel(field.getName());
        mdColumn.setKey(false);

        String type = field.getType().toString();
        String talendType = "String"; //$NON-NLS-1$
        if (type.equals("boolean")) { //$NON-NLS-1$
            talendType = "Boolean"; //$NON-NLS-1$
        } else if (type.equals("int")) { //$NON-NLS-1$
            talendType = "Integer"; //$NON-NLS-1$
        } else if (type.equals("date") || type.equals("datetime")) { //$NON-NLS-1$ //$NON-NLS-2$
            talendType = "Date"; //$NON-NLS-1$
        } else if (type.equals("double") || type.equals("currency")) { //$NON-NLS-1$ //$NON-NLS-2$
            talendType = "Double"; //$NON-NLS-1$
        } else {
            talendType = "String"; //$NON-NLS-1$
        }
        // mdColumn.setType(talendType);
        mdColumn.setTalendType("id_" + talendType); // How to transfer type? TODO //$NON-NLS-1$
        // mdColumn.setNullable(field.isNillable());
        mdColumn.setNullable(field.getNillable());

        if (type.equals("date")) { //$NON-NLS-1$
            mdColumn.setPattern("\"yyyy-MM-dd\""); //$NON-NLS-1$
        } else if (type.equals("datetime")) { //$NON-NLS-1$
            mdColumn.setPattern("\"yyyy-MM-dd\'T\'HH:mm:ss\'.000Z\'\""); //$NON-NLS-1$
        } else {
            mdColumn.setPattern(null);
        }
        if ("String".equals(talendType)) { //$NON-NLS-1$
            mdColumn.setLength(field.getLength());
            mdColumn.setPrecision(field.getPrecision());
        } else {
            mdColumn.setLength(field.getPrecision());
            mdColumn.setPrecision(field.getScale());
        }
        mdColumn.setDefault(field.getDefaultValueFormula());

        return mdColumn;

    }

    private IMetadataTable getMetadataTableBySalesforceServerAPIForOauth(final String endPoint, final String consumeKey,
            final String consumeSecret, final String callbackHost, final String callbackPort, final String salesforceVersion,
            final String token, final String timeOut, final String moduleName) {
        IMetadataTable metadataTable = new org.talend.core.model.metadata.MetadataTable();
        if (consumeKey == null || consumeSecret == null
                || consumeKey.equals("") || consumeSecret.equals("") || moduleName == null || moduleName.equals("")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            return null;
        }
        org.talend.salesforce.SforceManagement sforceManagement = null;
        try {
            OAuthClient client = new OAuthClient();
            client.setBaseOAuthURL(endPoint);
            client.setCallbackHost(callbackHost);
            client.setCallbackPort(Integer.parseInt(callbackPort));
            client.setClientID(consumeKey);
            client.setClientSecret(consumeSecret);
            Token tokenFile = salesforceAPI.login(endPoint, consumeKey, consumeSecret, callbackHost, callbackPort,
                    salesforceVersion, token, timeOut);
            String url = OAuthClient.getSOAPEndpoint(tokenFile, salesforceVersion);
            SforceConnection sforceConn = new SforceSessionConnection.Builder(url, tokenFile.getAccess_token())
                    .setTimeout(Integer.parseInt(timeOut)).needCompression(false).build();
            sforceManagement = new org.talend.salesforce.SforceManagementImpl(sforceConn);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        Field[] fields = fetchSFDescriptionField(moduleName, sforceManagement);
        List<IMetadataColumn> res = new ArrayList<IMetadataColumn>();
        for (Field field : fields) {
            res.add(parseFieldToMetadataColumn(field));
        }
        if (res.size() == 0) {
            return null;
        }
        metadataTable.setListColumns(res);
        return metadataTable;
    }

    private IMetadataTable getMetadataTableBySalesforceServerAPI(final String endPoint, final String user, final String pass,
            final String timeOut, final String moduleName, final String proxy, final String proxyHost, final String proxyPort,
            final String proxyUsername, final String proxyPassword) {
        IMetadataTable metadataTable = new org.talend.core.model.metadata.MetadataTable();

        if (user == null || pass == null || user.equals("") || pass.equals("") || moduleName == null || moduleName.equals("")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            return null;
        }

        // ProgressMonitorDialog dialog = new ProgressMonitorDialog(getShell());
        // try {
        // dialog.run(true, false, new IRunnableWithProgress() {
        //
        // public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        //
        //                    monitor.beginTask(Messages.getString("AbstractSalesforceStepForm.fetchModule", moduleName), //$NON-NLS-1$
        // IProgressMonitor.UNKNOWN);
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
        salesforceAPI.resetAllProxy();
        salesforceAPI.setProxy(proxyHost, proxyPort, proxyUsername, proxyPassword, httpProxy, socksProxy, httpsProxy);
        if (!salesforceAPI.isLogin()) {
            try {

                ArrayList loginList = salesforceAPI.login(endPoint, user, pass, timeOut);
                for (int i = 0; i < loginList.size(); i++) {
                    if (loginList.get(i) instanceof SoapBindingStub) {
                        binding = (SoapBindingStub) loginList.get(i);
                    }
                    if (loginList.get(i) instanceof SforceManagement) {
                        sforceMgr = (SforceManagement) loginList.get(i);
                    }
                }
            } catch (Throwable e) {
                ExceptionHandler.process(e);
            }
        }
        salesforceAPI.fetchMetaDataColumns(moduleName);
        salesforceAPI.resetAllProxy();
        // monitor.done();
        // }
        // });
        // } catch (InvocationTargetException e1) {
        // ExceptionHandler.process(e1);
        // } catch (InterruptedException e2) {
        // ExceptionHandler.process(e2);
        // }

        if (salesforceAPI.getCurrentMetadataColumns() == null) {
            return null;
        }

        metadataTable.setListColumns(salesforceAPI.getCurrentMetadataColumns());
        return metadataTable;
    }

    protected SalesforceModuleParseAPI checkSalesfoceLogin(final String proxy, final String endPoint, final String username,
            final String password, final String timeOut, final String proxyHost, final String proxyPort,
            final String proxyUsername, final String proxyPassword) {
        final List<String> errors = new ArrayList<String>();

        salesforceAPI.setLogin(false);

        ProgressMonitorDialog dialog = new ProgressMonitorDialog(getShell());
        try {
            dialog.run(true, false, new IRunnableWithProgress() {

                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

                    monitor.beginTask(Messages.getString("AbstractSalesforceStepForm.tryToLogin"), IProgressMonitor.UNKNOWN); //$NON-NLS-1$

                    if (salesforceAPI == null) {
                        try {
                            salesforceAPI = new SalesforceModuleParseAPI();
                        } catch (Throwable e) {
                            ExceptionHandler.process(e);
                        }
                    }
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
                    salesforceAPI.resetAllProxy();

                    salesforceAPI.setProxy(proxyHost, proxyPort, proxyUsername, proxyPassword, httpProxy, socksProxy, httpsProxy);
                    try {
                        // binding ;
                        ArrayList loginList = salesforceAPI.login(endPoint, username, password, timeOut);
                        if (loginList != null) {
                            for (int i = 0; i < loginList.size(); i++) {
                                if (loginList.get(i) instanceof SoapBindingStub) {
                                    binding = (SoapBindingStub) loginList.get(i);
                                }
                                if (loginList.get(i) instanceof SforceManagement) {
                                    sforceMgr = (SforceManagement) loginList.get(i);
                                }

                            }

                        }

                        salesforceAPI.setLogin(true);
                    } catch (Throwable e) {
                        errors.add(e.getMessage());
                        ExceptionHandler.process(e);
                    } finally {
                        salesforceAPI.resetAllProxy();
                    }
                    monitor.done();
                }
            });
        } catch (InvocationTargetException e1) {
            ExceptionHandler.process(e1);
        } catch (InterruptedException e2) {
            ExceptionHandler.process(e2);
        }

        if (salesforceAPI.isLogin()) {
            MessageDialog.openInformation(getShell(), Messages.getString("SalesforceForm.checkConnectionTitle"), //$NON-NLS-1$ 
                    Messages.getString("SalesforceForm.checkIsDone")); //$NON-NLS-1$ 
        } else {
            String mainMsg = Messages.getString("SalesforceForm.checkFailure") + " " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("SalesforceForm.checkFailureTip"); //$NON-NLS-1$
            String error = errors.size() > 0 ? errors.get(0) : ""; //$NON-NLS-1$
            new ErrorDialogWidthDetailArea(getShell(), PID, mainMsg, error);
        }

        return salesforceAPI;
    }

    protected DescribeGlobalResult describeGlobal() throws UnexpectedErrorFault, RemoteException {
        if (salesforceAPI.isLogin()) {
            if (binding != null) {
                return binding.describeGlobal();
            }
        }
        return null;
    }

    protected com.salesforce.soap.partner.DescribeGlobalResult describeGlobalPartner() throws RemoteException {
        if (salesforceAPI.isLogin()) {
            ISalesforceModuleParser currentAPI = salesforceAPI.getCurrentAPI();
            if (currentAPI instanceof SalesforceModuleParserPartner) {
                SalesforceModuleParserPartner partner = (SalesforceModuleParserPartner) currentAPI;
                try {
                    sforceMgr.describeSObjects(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private IMetadataTable getMetadataTableFromConfigFile(String moduleName) {

        INode node = getSalesforceNode();

        IElementParameter currentModuleNameParam = node.getElementParameter("MODULENAME"); //$NON-NLS-1$
        currentModuleNameParam.setValue(moduleName);

        node.getComponent().createElementParameters(node);

        IElementParameter schemaParam = node.getElementParameter("SCHEMA"); //$NON-NLS-1$

        if (schemaParam == null) {
            return null;
        }

        schemaParam.setValueToDefault(node.getElementParameters()); // Call this method to recompute some parameters
        // value.

        IMetadataTable metadataTable = (IMetadataTable) schemaParam.getValue();

        return metadataTable;
    }

    /**
     * DOC zli Comment method "readMetadataDetail".
     */
    public IMetadataTable readMetadataDetail() {
        SalesforceSchemaConnection connection2 = getConnection();
        String moduleName = connection2.getModuleName();

        if (moduleName == null || moduleName.equals("")) { //$NON-NLS-1$
            return null;
        }

        String webServiceUrl = connection2.getWebServiceUrl();
        String userName = connection2.getUserName();
        String password = connection2.getValue(connection2.getPassword(), false);
        String timeOut = connection2.getTimeOut();
        // add for feature 7507
        String betchSize = connection2.getBatchSize();
        boolean useProxy = connection2.isUseProxy();
        boolean useHttp = connection2.isUseHttpProxy();
        String proxyHost = connection2.getProxyHost();
        String proxyPort = connection2.getProxyPort();
        String proxyUsername = connection2.getProxyUsername();
        String proxyPassword = connection2.getValue(connection2.getProxyPassword(), false);

        String webServiceUrlForOauth = connection2.getWebServiceUrlTextForOAuth();
        String comsumeKey = connection2.getConsumeKey();
        String consumeSecret = connection2.getValue(connection2.getConsumeSecret(), false);
        String callbackHost = connection2.getCallbackHost();
        String callbackPort = connection2.getCallbackPort();
        String salesforceVersion = connection2.getSalesforceVersion();
        String token = connection2.getToken();
        String loginType = connection2.getLoginType();

        if (isContextMode() && getContextModeManager() != null) {
            webServiceUrl = getContextModeManager().getOriginalValue(webServiceUrl);
            userName = getContextModeManager().getOriginalValue(userName);
            password = getContextModeManager().getOriginalValue(password);
            timeOut = getContextModeManager().getOriginalValue(timeOut);
            betchSize = getContextModeManager().getOriginalValue(betchSize);
            useProxy = Boolean.valueOf(getContextModeManager().getOriginalValue(String.valueOf(useProxy)));
            useHttp = Boolean.valueOf(getContextModeManager().getOriginalValue(String.valueOf(useHttp)));
            proxyHost = getContextModeManager().getOriginalValue(proxyHost);
            proxyPort = getContextModeManager().getOriginalValue(proxyPort);
            proxyUsername = getContextModeManager().getOriginalValue(proxyUsername);
            proxyPassword = getContextModeManager().getOriginalValue(proxyPassword);

            webServiceUrlForOauth = getContextModeManager().getOriginalValue(webServiceUrlForOauth);
            comsumeKey = getContextModeManager().getOriginalValue(comsumeKey);
            consumeSecret = getContextModeManager().getOriginalValue(consumeSecret);
            callbackHost = getContextModeManager().getOriginalValue(callbackHost);
            callbackPort = getContextModeManager().getOriginalValue(callbackPort);
            salesforceVersion = getContextModeManager().getOriginalValue(salesforceVersion);
            token = getContextModeManager().getOriginalValue(token);
            loginType = getContextModeManager().getOriginalValue(loginType);
        }

        if (loginType.equalsIgnoreCase(BASIC)) {
            metadataTableOrder = getMetadatasForSalesforce(webServiceUrl, userName, password, timeOut, moduleName, betchSize,
                    useProxy, useHttp, proxyHost, proxyPort, proxyUsername, proxyPassword, true);
        } else {
            metadataTableOrder = getMetadataTableBySalesforceServerAPIForOauth(webServiceUrlForOauth, comsumeKey, consumeSecret,
                    callbackHost, callbackPort, salesforceVersion, token, timeOut, moduleName);
            if (metadataTableOrder == null) {
                metadataTableOrder = getMetadataTableFromConfigFile(moduleName);
            }
        }
        return metadataTableOrder;
    }

    /**
     * DOC zli Comment method "modifyMetadataTable".
     */
    public IMetadataTable modifyMetadataTable() {
        if (metadataTableOrder != null) {
            List<IMetadataColumn> listColumns = metadataTableOrder.getListColumns();
            if (listColumns != null) {

                Object[] array = listColumns.toArray();
                for (int i = 0; i < array.length; i++) {
                    for (int j = i + 1; j < array.length; j++) {

                        String labela = ((MetadataColumn) array[i]).getLabel();
                        String labelb = ((MetadataColumn) array[j]).getLabel();
                        if (labela.compareTo(labelb) > 0) {
                            MetadataColumn metadataColumn = (MetadataColumn) array[i];
                            array[i] = array[j];
                            array[j] = metadataColumn;
                        }
                    }
                }
                List<Object> asList = Arrays.asList(array);
                List<IMetadataColumn> aa = new ArrayList();
                if (asList != null && asList.size() > 0) {
                    Object object = asList.get(0);
                    if (object instanceof MetadataColumn) {
                        for (int i = 0; i < asList.size(); i++) {
                            aa.add(i, (MetadataColumn) asList.get(i));
                        }
                        metadataTableOrder.setListColumns(aa);
                    }
                }
            }
        }
        return metadataTableOrder;
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
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFields()
     */
    @Override
    protected void addFields() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFieldsListeners()
     */
    @Override
    protected void addFieldsListeners() {
        // TODO Auto-generated method stub

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
     * @see org.talend.repository.ui.swt.utils.AbstractForm#checkFieldsValue()
     */
    @Override
    protected boolean checkFieldsValue() {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#initialize()
     */
    @Override
    protected void initialize() {
        // TODO Auto-generated method stub
    }

    /**
     * Getter for salesforceAPI.
     * 
     * @return the salesforceAPI
     */
    public SalesforceModuleParseAPI getSalesforceAPI() {
        return this.salesforceAPI;
    }

    /**
     * Sets the salesforceAPI.
     * 
     * @param salesforceAPI the salesforceAPI to set
     */
    public void setSalesforceAPI(SalesforceModuleParseAPI salesforceAPI) {
        this.salesforceAPI = salesforceAPI;
    }

    public IMetadataContextModeManager getContextModeManager() {
        return this.contextModeManager;
    }

    public void setContextModeManager(IMetadataContextModeManager contextModeManager) {
        this.contextModeManager = contextModeManager;
    }

    /**
     * DOC Administrator Comment method "getTableByLabel".
     * 
     * @param label
     * @return
     */
    protected MetadataTable getTableByLabel(String label) {
        // TODO Auto-generated method stub
        return null;
    }
}
