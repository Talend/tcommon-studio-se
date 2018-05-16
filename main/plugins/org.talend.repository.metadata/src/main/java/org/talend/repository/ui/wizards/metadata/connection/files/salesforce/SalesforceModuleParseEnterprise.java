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

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.repository.metadata.i18n.Messages;

import com.sforce.soap.enterprise.DescribeGlobalResult;
import com.sforce.soap.enterprise.DescribeSObjectResult;
import com.sforce.soap.enterprise.Field;
import com.sforce.soap.enterprise.LoginResult;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.SessionHeader;
import com.sforce.soap.enterprise.SforceServiceLocator;
import com.sforce.soap.enterprise.SoapBindingStub;
import com.sforce.soap.enterprise.fault.UnexpectedErrorFault;
import com.sforce.soap.enterprise.sobject.Account;
import com.sforce.soap.enterprise.sobject.SObject;

/**
 * Maybe need a long connection ...
 * <p>
 * DOC YeXiaowei class global comment. Detailled comment <br/>
 * 
 */
public class SalesforceModuleParseEnterprise implements ISalesforceModuleParser {

    final String useProxy = "useProxyBtn"; //$NON-NLS-1$

    private String url = null;

    private String name = null;

    private String pwd = null;

    private String proxy = null;

    private boolean loginOk = false;

    /*
     * 
     */
    // private String proxyHost = null;
    //
    // private String proxyPort = null;
    //
    // private String proxyUsername = null;
    //
    // private String proxyPassword = null;

    private SoapBindingStub binding = null;

    private LoginResult loginResult = null; // maintain the login results

    private Account[] accounts = null;

    private String currentModuleName = null;

    private List<IMetadataColumn> currentMetadataColumns = null;

    private List list = null;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.ui.wizards.metadata.connection.files.salesforce.ISalesforceModuleParser#login(java.lang
     * .String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public ArrayList login(String endPoint, String username, String password) throws Exception {
        if (endPoint == null) {
            throw new RemoteException(Messages.getString("SalesforceModuleParseAPI.URLInvalid")); //$NON-NLS-1$
        }
        if (username == null || password == null) {
            throw new Exception(Messages.getString("SalesforceModuleParseAPI.lostUsernameOrPass")); //$NON-NLS-1$
        }
        ArrayList doLoginList = null;
        if (name != null && pwd != null && url != null) {
            if (!url.equals(endPoint) || !name.equals(username) || !pwd.equals(password)) {
                doLoginList = doLogin(endPoint, username, password);

            } else {
                if (isLogin()) {
                    return doLoginList;
                }
            }
        } else {
            doLoginList = doLogin(endPoint, username, password);
        }

        this.name = username;
        this.pwd = password;
        this.url = endPoint;
        return doLoginList;
    }

    @Override
    public ArrayList login(String endPoint, String username, String password, String timeOut) throws Exception {
        if (endPoint == null) {
            throw new RemoteException(Messages.getString("SalesforceModuleParseAPI.URLInvalid")); //$NON-NLS-1$
        }
        if (username == null || password == null) {
            throw new Exception(Messages.getString("SalesforceModuleParseAPI.lostUsernameOrPass")); //$NON-NLS-1$
        }
        ArrayList doLoginList = null;
        if (name != null && pwd != null && url != null) {
            if (!url.equals(endPoint) || !name.equals(username) || !pwd.equals(password)) {
                doLoginList = doLogin(endPoint, username, password);

            } else {
                if (isLogin()) {
                    return doLoginList;
                }
            }
        } else {
            doLoginList = doLogin(endPoint, username, password);
        }

        this.name = username;
        this.pwd = password;
        this.url = endPoint;
        return doLoginList;
    }

    private boolean checkString(String str1, String str2) {
        if (str1 == str2) {
            return true;
        }
        if (str1 != null && str2 != null) {
            return str1.equals(str2);
        }
        return false;
    }

    protected ArrayList doLogin(String endPoint, String userName, String pwd) throws RemoteException, ServiceException,
            MalformedURLException {

        try {
            URL soapAddress = new java.net.URL(endPoint);
            binding = (SoapBindingStub) new SforceServiceLocator().getSoap(soapAddress);

            loginResult = binding.login(userName, pwd);

        } catch (ArrayIndexOutOfBoundsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        setLogin(true);
        // on a successful login, you should always set up your session id
        // and the url for subsequent calls

        // reset the url endpoint property, this will cause subsequent calls
        // to made to the serverURL from the login result
        binding._setProperty(SoapBindingStub.ENDPOINT_ADDRESS_PROPERTY, loginResult.getServerUrl());

        // create a session head object
        SessionHeader sh = new SessionHeader();
        // set the sessionId property on the header object using
        // the value from the login result
        sh.setSessionId(loginResult.getSessionId());
        // add the header to the binding stub
        String sforceURI = new SforceServiceLocator().getServiceName().getNamespaceURI();
        binding.setHeader(sforceURI, "SessionHeader", sh); //$NON-NLS-1$

        ArrayList arrayList = new ArrayList();
        arrayList.add(binding);

        return arrayList;
    }

    private void doGetAccounts() {
        // check to see if we are already logged in
        if (loginResult == null) {
            System.out.println("Run the login sample before the others.\n"); //$NON-NLS-1$
            System.out.println("\n"); //$NON-NLS-1$
            return;
        }
        // create a variable to hold the query result
        QueryResult qr = null;
        // call the query saving the results in qr
        try {
            qr = binding.query("select Name, numberOfEmployees, Id, Industry from Account"); //$NON-NLS-1$
        } catch (UnexpectedErrorFault uef) {
            System.out.println(uef.getExceptionMessage() + "\n\n"); //$NON-NLS-1$
            return;
        } catch (Exception e) {
            // e.printStackTrace();
            ExceptionHandler.process(e);
            System.out.println("\n\n"); //$NON-NLS-1$
            return;
        }
        // always a good idea
        if (qr != null) {
            SObject[] records = qr.getRecords();
            accounts = new Account[records.length];
            // we can loop through the returned records
            for (int i = 0; i < records.length; i++) {
                // Because we asked for accounts we will convert
                // the SObject for each record into an Account object
                Account account = (Account) records[i];
                accounts[i] = account;
                // Now we can access any of the fields we had in the query
                // select clause directly from the account variable
                System.out.print(new Integer(i).toString() + ". "); //$NON-NLS-1$
                System.out.print(account.getName() + " - "); //$NON-NLS-1$
                System.out.println(account.getId());
            }
            System.out.println(""); //$NON-NLS-1$
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.ui.wizards.metadata.connection.files.salesforce.ISalesforceModuleParser#describeGlobalSample
     * ()
     */
    @Override
    public void describeGlobalSample() {
        try {
            DescribeGlobalResult describeGlobalResult = null;
            describeGlobalResult = binding.describeGlobal();
            String[] types = describeGlobalResult.getTypes();
            for (String type : types) {
                System.out.println(type);
            }
            System.out.println("\nDescribe global was successful...\r\n"); //$NON-NLS-1$
        } catch (Exception ex) {
            System.out.println("\nFailed to return types, error message was: \n" + ex.getMessage()); //$NON-NLS-1$
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.ui.wizards.metadata.connection.files.salesforce.ISalesforceModuleParser#fetchMetaDataColumns
     * (java.lang.String)
     */
    @Override
    public List<IMetadataColumn> fetchMetaDataColumns(String module) {
        Field[] fields = fetchSFDescriptionField(module);

        if (fields == null || fields.length <= 0) {
            return null;
        }

        List<IMetadataColumn> res = new ArrayList<IMetadataColumn>();
        for (Field field : fields) {
            res.add(parseFieldToMetadataColumn(field));
        }

        setCurrentMetadataColumns(res);
        return res;
    }

    /**
     * Fetch module fields from SF service. Make sure login sucess before do this.
     * <p>
     * DOC YeXiaowei Comment method "fetchSFDescriptionField".
     * 
     * @param module
     * @return
     */
    private Field[] fetchSFDescriptionField(String module) {
        try {
            // Invoke describeSObject and save results in DescribeSObjectResult
            DescribeSObjectResult describeSObjectResult = binding.describeSObject(module);
            // Determine whether the describeSObject call succeeded.
            if (!(describeSObjectResult == null)) {
                // Retrieve fields from the results
                Field[] fields = describeSObjectResult.getFields();
                // Get the name of the object
                String objectName = describeSObjectResult.getName();
                // Get some flags
                boolean isActivateable = describeSObjectResult.isActivateable();
                // Many other values are accessible
                setCurrentModuleName(module);
                return fields;
            }
            setCurrentModuleName(null);
            return null;
        } catch (Exception ex) {
            setCurrentModuleName(null);
            return null;
        }
    }

    /**
     * Parse SF field to Talend data type
     * <p>
     * DOC YeXiaowei Comment method "parseFieldToMetadataColumn".
     * 
     * @param field
     * @return
     */
    private IMetadataColumn parseFieldToMetadataColumn(Field field) {

        if (field == null) {
            return null;
        }

        IMetadataColumn mdColumn = new org.talend.core.model.metadata.MetadataColumn();

        mdColumn.setLabel(field.getName());
        mdColumn.setKey(false);

        // public static final java.lang.String _value1 = "string";
        // public static final java.lang.String _value2 = "picklist";
        // public static final java.lang.String _value3 = "multipicklist";
        // public static final java.lang.String _value4 = "combobox";
        // public static final java.lang.String _value5 = "reference";
        // public static final java.lang.String _value6 = "base64";
        // public static final java.lang.String _value7 = "boolean";
        // public static final java.lang.String _value8 = "currency";
        // public static final java.lang.String _value9 = "textarea";
        // public static final java.lang.String _value10 = "int";
        // public static final java.lang.String _value11 = "double";
        // public static final java.lang.String _value12 = "percent";
        // public static final java.lang.String _value13 = "phone";
        // public static final java.lang.String _value14 = "id";
        // public static final java.lang.String _value15 = "date";
        // public static final java.lang.String _value16 = "datetime";
        // public static final java.lang.String _value17 = "url";
        // public static final java.lang.String _value18 = "email";
        // public static final java.lang.String _value19 = "anyType";

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
        mdColumn.setNullable(field.isNillable());

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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.metadata.connection.files.salesforce.ISalesforceModuleParser#isLogin()
     */
    @Override
    public boolean isLogin() {
        return this.loginOk;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.ui.wizards.metadata.connection.files.salesforce.ISalesforceModuleParser#setLogin(boolean)
     */
    @Override
    public void setLogin(boolean login) {
        this.loginOk = login;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.ui.wizards.metadata.connection.files.salesforce.ISalesforceModuleParser#getCurrentModuleName
     * ()
     */
    @Override
    public String getCurrentModuleName() {
        return this.currentModuleName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.ui.wizards.metadata.connection.files.salesforce.ISalesforceModuleParser#setCurrentModuleName
     * (java.lang.String)
     */
    @Override
    public void setCurrentModuleName(String currentModuleName) {
        this.currentModuleName = currentModuleName;
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.repository.ui.wizards.metadata.connection.files.salesforce.ISalesforceModuleParser#
     * getCurrentMetadataColumns()
     */
    @Override
    public List<IMetadataColumn> getCurrentMetadataColumns() {
        return this.currentMetadataColumns;
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.repository.ui.wizards.metadata.connection.files.salesforce.ISalesforceModuleParser#
     * setCurrentMetadataColumns(java.util.List)
     */
    @Override
    public void setCurrentMetadataColumns(List<IMetadataColumn> currentMetadataColumns) {
        this.currentMetadataColumns = currentMetadataColumns;
    }

}
