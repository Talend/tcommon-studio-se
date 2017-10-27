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

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.salesforce.SforceBasicConnection;
import org.talend.salesforce.SforceConnection;
import org.talend.salesforce.SforceManagementImpl;

import com.salesforce.soap.partner.DescribeSObjectResult;
import com.salesforce.soap.partner.Field;

/**
 * Maybe need a long connection ...
 * <p>
 * DOC YeXiaowei class global comment. Detailled comment <br/>
 * 
 */
public class SalesforceModuleParserPartner implements ISalesforceModuleParser {

    final String useProxy = "useProxyBtn"; //$NON-NLS-1$

    private String url = null;

    private String name = null;

    private String pwd = null;

    // private String proxy = null;

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

    // private SoapBindingStub binding = null;

    private SforceManagementImpl sforceManagement = null;

    // private LoginResult loginResult = null; // maintain the login results

    private String currentModuleName = null;

    private List<IMetadataColumn> currentMetadataColumns = null;

    private List list = null;

    /**
     * DOC YeXiaowei Comment method "login".
     */
    @Override
    public ArrayList login(String endPoint, String username, String password) throws Exception {
        if (endPoint == null) {
            throw new RemoteException(
                    org.talend.repository.metadata.i18n.Messages.getString("SalesforceModuleParseAPI.URLInvalid")); //$NON-NLS-1$
        }
        if (username == null || password == null) {
            throw new Exception(Messages.getString("SalesforceModuleParseAPI.lostUsernameOrPass")); //$NON-NLS-1$
        }
        ArrayList doLoginList = null;
        sforceManagement = null;
        boolean login = false;
        if (name != null && pwd != null && url != null) {
            if (!url.equals(endPoint) || !name.equals(username) || !pwd.equals(password)) {
                // || !checkString(proxyHost, this.proxyHost)
                // || !checkString(proxyPort, this.proxyPort)
                // || !checkString(proxyUsername, this.proxyUsername)
                // || !checkString(proxyPassword, this.proxyPassword)
                // || (proxy != null && theProxy != null && !proxy.equals(theProxy) || (proxy != null && theProxy ==
                // null) || (proxy == null && theProxy != null))) {

                // doLoginList = doLogin(endPoint, username, password);
                SforceConnection sforceConn = new SforceBasicConnection.Builder(endPoint, username, password).setTimeout(10000)
                        .needCompression(false).build();
                login = true;
                sforceManagement = new SforceManagementImpl(sforceConn);

            } else {
                if (isLogin()) {
                    return doLoginList;
                }
            }
        } else {
            // doLoginList = doLogin(endPoint, username, password);
            SforceConnection sforceConn = new SforceBasicConnection.Builder(endPoint, username, password).setTimeout(10000)
                    .needCompression(false).build();
            login = true;
            sforceManagement = new SforceManagementImpl(sforceConn);
            doLoginList = new ArrayList();
            doLoginList.add(sforceManagement);
        }

        setLogin(login);
        setSforceManagement(sforceManagement);
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
        int time = Integer.valueOf(timeOut);
        ArrayList doLoginList = null;
        sforceManagement = null;
        boolean login = false;
        if (name != null && pwd != null && url != null) {
            if (!url.equals(endPoint) || !name.equals(username) || !pwd.equals(password)) {

                // doLoginList = doLogin(endPoint, username, password);
                SforceConnection sforceConn = new SforceBasicConnection.Builder(endPoint, username, password).setTimeout(time)
                        .needCompression(false).build();
                login = true;
                sforceManagement = new SforceManagementImpl(sforceConn);

            } else {
                if (isLogin()) {
                    return doLoginList;
                }
            }
        } else {
            // doLoginList = doLogin(endPoint, username, password);
            SforceConnection sforceConn = new SforceBasicConnection.Builder(endPoint, username, password).setTimeout(time)
                    .needCompression(false).build();
            login = true;
            sforceManagement = new SforceManagementImpl(sforceConn);
            doLoginList = new ArrayList();
            doLoginList.add(sforceManagement);
        }
        setLogin(login);
        setSforceManagement(sforceManagement);
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

    // protected ArrayList doLogin(String endPoint, String username, String password) throws RemoteException,
    // ServiceException,
    // MalformedURLException {
    // try {
    // URL soapAddress = new java.net.URL(endPoint);
    // binding = (SoapBindingStub) new SforceServiceLocator().getSoap(soapAddress);
    //
    // loginResult = binding.login(username, password);
    //
    // } catch (ArrayIndexOutOfBoundsException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // setLogin(true);
    // // on a successful login, you should always set up your session id
    // // and the url for subsequent calls
    //
    // // reset the url endpoint property, this will cause subsequent calls
    // // to made to the serverURL from the login result
    // binding._setProperty(SoapBindingStub.ENDPOINT_ADDRESS_PROPERTY, loginResult.getServerUrl());
    //
    // // create a session head object
    // SessionHeader sh = new SessionHeader();
    // // set the sessionId property on the header object using
    // // the value from the login result
    // sh.setSessionId(loginResult.getSessionId());
    // // add the header to the binding stub
    // String sforceURI = new SforceServiceLocator().getServiceName().getNamespaceURI();
    //        binding.setHeader(sforceURI, "SessionHeader", sh); //$NON-NLS-1$
    //
    // ArrayList arrayList = new ArrayList();
    // arrayList.add(binding);
    //
    // return arrayList;
    // }

    @Override
    public void describeGlobalSample() {
        // try {
        // DescribeGlobalResult describeGlobalResult = null;
        // describeGlobalResult = binding.describeGlobal();
        // String[] types = describeGlobalResult.getTypes();
        // for (int i = 0; i < types.length; i++)
        // System.out.println(types[i]);
        //            System.out.println("\nDescribe global was successful...\r\n"); //$NON-NLS-1$
        // } catch (Exception ex) {
        //            System.out.println("\nFailed to return types, error message was: \n" + ex.getMessage()); //$NON-NLS-1$
        // }
    }

    /**
     * Fetch a module from SF and transfor to Talend metadata data type. DOC YeXiaowei Comment method
     * "fetchMetaDataColumns".
     * 
     * @param module
     * @return
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

        DescribeSObjectResult r;
        try {
            r = sforceManagement.describeSObject(module);
            Field[] fields = r.getFields();
            setCurrentModuleName(module);
            return fields;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

        // try {
        // // Invoke describeSObject and save results in DescribeSObjectResult
        // DescribeSObjectResult describeSObjectResult = binding.describeSObject(module);
        // // Determine whether the describeSObject call succeeded.
        // if (!(describeSObjectResult == null)) {
        // // Retrieve fields from the results
        // Field[] fields = describeSObjectResult.getFields();
        // // Get the name of the object
        // String objectName = describeSObjectResult.getName();
        // // Get some flags
        // boolean isActivateable = describeSObjectResult.isActivateable();
        // // Many other values are accessible
        // setCurrentModuleName(module);
        // return fields;
        // }
        // setCurrentModuleName(null);
        // return null;
        // } catch (Exception ex) {
        // setCurrentModuleName(null);
        // return null;
        // }
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
        } else if (type.equals("double") || type.equals("currency") || type.equals("percent")) { //$NON-NLS-1$ //$NON-NLS-2$
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

    /**
     * Getter for login.
     * 
     * @return the login
     */
    @Override
    public boolean isLogin() {
        return this.loginOk;
    }

    /**
     * Sets the login.
     * 
     * @param login the login to set
     */
    @Override
    public void setLogin(boolean login) {
        this.loginOk = login;
    }

    /**
     * Getter for currentModuleName.
     * 
     * @return the currentModuleName
     */
    @Override
    public String getCurrentModuleName() {
        return this.currentModuleName;
    }

    /**
     * Sets the currentModuleName.
     * 
     * @param currentModuleName the currentModuleName to set
     */
    @Override
    public void setCurrentModuleName(String currentModuleName) {
        this.currentModuleName = currentModuleName;
    }

    /**
     * Getter for currentMetadataColumns.
     * 
     * @return the currentMetadataColumns
     */
    @Override
    public List<IMetadataColumn> getCurrentMetadataColumns() {
        return this.currentMetadataColumns;
    }

    /**
     * Sets the currentMetadataColumns.
     * 
     * @param currentMetadataColumns the currentMetadataColumns to set
     */
    @Override
    public void setCurrentMetadataColumns(List<IMetadataColumn> currentMetadataColumns) {
        this.currentMetadataColumns = currentMetadataColumns;
    }

    public SforceManagementImpl getSforceManagement() {
        return this.sforceManagement;
    }

    private void setSforceManagement(SforceManagementImpl sforceManagement) {
        this.sforceManagement = sforceManagement;
    }

}
