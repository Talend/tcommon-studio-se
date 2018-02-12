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

import org.talend.salesforce.oauth.OAuthClient;
import org.talend.salesforce.oauth.Token;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.talend.core.model.metadata.IMetadataColumn;

/**
 * Maybe need a long connection ...
 * <p>
 * DOC YeXiaowei class global comment. Detailled comment <br/>
 * 
 */
public class SalesforceModuleParseAPI {

    public static final String SOCKS_PROXY_HOST = "socksProxyHost"; //$NON-NLS-1$

    public static final String SOCKS_PROXY_PORT = "socksProxyPort"; //$NON-NLS-1$

    public static final String SOCKS_PROXY_USERNAME = "java.net.socks.username"; //$NON-NLS-1$

    public static final String SOCKS_PROXY_PASSWORD = "java.net.socks.password"; //$NON-NLS-1$

    public static final String HTTP_PROXY_HOST = "http.proxyHost"; //$NON-NLS-1$

    public static final String HTTP_PROXY_PORT = "http.proxyPort"; //$NON-NLS-1$

    public static final String HTTP_PROXY_USER = "http.proxyUser";//$NON-NLS-1$

    public static final String HTTP_PROXY_PASSWORD = "http.proxyPassword";//$NON-NLS-1$

    public static final String HTTP_PROXY_SET = "http.proxySet";//$NON-NLS-1$

    public static final String USE_SOCKS_PROXY = "useProxyBtn";

    public static final String USE_HTTP_PROXY = "useHttpBtn";

    public static final String HTTPS_PROXY_HOST = "https.proxyHost"; //$NON-NLS-1$

    public static final String HTTPS_PROXY_PORT = "https.proxyPort"; //$NON-NLS-1$

    public static final String HTTPS_PROXY_USER = "https.proxyUser";//$NON-NLS-1$

    public static final String HTTPS_PROXY_PASSWORD = "https.proxyPassword";//$NON-NLS-1$

    public static final String HTTPS_PROXY_SET = "https.proxySet";//$NON-NLS-1$

    //    public static final String FTP_PROXY_HOST = "FtpproxyHost"; //$NON-NLS-1$
    //
    //    public static final String FTP_PROXY_PORT = "Ftpproxyport"; //$NON-NLS-1$
    //
    //    public static final String FTP_PROXY_USER = "FtpproxyUser";//$NON-NLS-1$
    //
    //    public static final String FTP_PROXY_PASSWORD = "FtpproxyPassword";//$NON-NLS-1$
    //
    //    public static final String FTP_PROXY_SET = "FtpproxySet";//$NON-NLS-1$

    private String oldProxyHost;

    private String oldProxyPort;

    private String oldProxyUser;

    private String oldProxyPwd;

    private String oldHttpProxySet;

    private String oldHttpsProxySet;

    private boolean oldSocksProxySet;

    private boolean login;

    private ISalesforceModuleParser currentAPI;

    public Token login(String endPointForAuth, String consumerKey, String consumeSecret, String callbackHost,
            String callbackPort, String salesforceVersion, String tokenProperties, String timeOut) throws Exception {
        OAuthClient client = new OAuthClient();
        client.setBaseOAuthURL(endPointForAuth);
        client.setCallbackHost(callbackHost);
        client.setCallbackPort(Integer.parseInt(callbackPort));
        client.setClientID(consumerKey);
        client.setClientSecret(consumeSecret);

        File tokenFile = new File(tokenProperties);
        if (tokenFile.exists()) {
            InputStream inputStream = null;
            InputStreamReader input = null;
            BufferedReader reader = null;
            String tokenMessage = null;
            java.util.Properties properties = new java.util.Properties();
            FileInputStream inStream = new FileInputStream(tokenProperties);
            properties.load(inStream);
            tokenMessage = properties.getProperty("refreshtoken");
            inStream.close();
            Token token = client.refreshToken(tokenMessage);
            return token;
        }
        return null;
    }

    /**
     * DOC YeXiaowei Comment method "login".
     */
    public ArrayList login(String endPoint, String username, String password) throws Exception {
        ArrayList returnValues;
        currentAPI = new SalesforceModuleParseEnterprise();
        currentAPI.setLogin(login);
        try {
            returnValues = currentAPI.login(endPoint, username, password);
        } catch (IOException e) {
            currentAPI = new SalesforceModuleParserPartner();
            currentAPI.setLogin(login);
            returnValues = currentAPI.login(endPoint, username, password);
        }
        return returnValues;
    }

    public ArrayList login(String endPoint, String username, String password, String timeOut) throws Exception {
        ArrayList returnValues;
        currentAPI = new SalesforceModuleParseEnterprise();
        currentAPI.setLogin(login);
        try {
            returnValues = currentAPI.login(endPoint, username, password);
        } catch (IOException e) {
            currentAPI = new SalesforceModuleParserPartner();
            currentAPI.setLogin(login);
            returnValues = currentAPI.login(endPoint, username, password, timeOut);
        }
        login = true;
        return returnValues;
    }

    /**
     * Fetch a module from SF and transfor to Talend metadata data type. DOC YeXiaowei Comment method
     * "fetchMetaDataColumns".
     * 
     * @param module
     * @return
     */
    public List<IMetadataColumn> fetchMetaDataColumns(String module) {
        return currentAPI.fetchMetaDataColumns(module);
    }

    /**
     * Getter for login.
     * 
     * @return the login
     */
    public boolean isLogin() {
        return login;
    }

    /**
     * Sets the login.
     * 
     * @param login the login to set
     */
    public void setLogin(boolean login) {
        this.login = login;
    }

    /**
     * Getter for currentModuleName.
     * 
     * @return the currentModuleName
     */
    public String getCurrentModuleName() {
        if (currentAPI != null) {
            return currentAPI.getCurrentModuleName();
        }
        return null;
    }

    /**
     * Sets the currentModuleName.
     * 
     * @param currentModuleName the currentModuleName to set
     */
    public void setCurrentModuleName(String currentModuleName) {
        currentAPI.setCurrentModuleName(currentModuleName);
    }

    /**
     * Getter for currentMetadataColumns.
     * 
     * @return the currentMetadataColumns
     */
    public List<IMetadataColumn> getCurrentMetadataColumns() {
        return currentAPI.getCurrentMetadataColumns();
    }

    /**
     * Sets the currentMetadataColumns.
     * 
     * @param currentMetadataColumns the currentMetadataColumns to set
     */
    public void setCurrentMetadataColumns(List<IMetadataColumn> currentMetadataColumns) {
        currentAPI.setCurrentMetadataColumns(currentMetadataColumns);
    }

    public ISalesforceModuleParser getCurrentAPI() {
        return this.currentAPI;
    }

    public void setProxy(String proxyHost, String proxyPort, String proxyUsername, String proxyPassword, boolean httpProxy,
            boolean socksProxy, boolean httpsProxy) {
        Properties properties = System.getProperties();
        if (socksProxy && proxyHost != null && proxyPort != null) { //$NON-NLS-1$ 
            if (properties.containsKey(SalesforceModuleParseAPI.SOCKS_PROXY_HOST)) {
                oldProxyHost = (String) properties.get(SalesforceModuleParseAPI.SOCKS_PROXY_HOST);
                oldProxyPort = (String) properties.get(SalesforceModuleParseAPI.SOCKS_PROXY_PORT);
                oldProxyUser = (String) properties.get(SalesforceModuleParseAPI.SOCKS_PROXY_USERNAME);
                oldProxyPwd = (String) properties.get(SalesforceModuleParseAPI.SOCKS_PROXY_PASSWORD);
                oldSocksProxySet = true;
            } else {
                oldSocksProxySet = false;
            }
            properties.put(SalesforceModuleParseAPI.SOCKS_PROXY_HOST, proxyHost);
            properties.put(SalesforceModuleParseAPI.SOCKS_PROXY_PORT, proxyPort);
            properties.put(SalesforceModuleParseAPI.SOCKS_PROXY_USERNAME, proxyUsername == null ? "" : proxyUsername); //$NON-NLS-1$
            properties.put(SalesforceModuleParseAPI.SOCKS_PROXY_PASSWORD, proxyPassword == null ? "" : proxyPassword); //$NON-NLS-1$
        } else if (httpProxy && proxyHost != null && proxyPort != null) { //$NON-NLS-1$ 
            if (properties.containsKey(SalesforceModuleParseAPI.HTTP_PROXY_HOST)) {
                oldHttpProxySet = (String) properties.get(SalesforceModuleParseAPI.HTTP_PROXY_SET);
                oldProxyHost = (String) properties.get(SalesforceModuleParseAPI.HTTP_PROXY_HOST);
                oldProxyPort = (String) properties.get(SalesforceModuleParseAPI.HTTP_PROXY_PORT);
                oldProxyUser = (String) properties.get(SalesforceModuleParseAPI.HTTP_PROXY_USER);
                oldProxyPwd = (String) properties.get(SalesforceModuleParseAPI.HTTP_PROXY_PASSWORD);
            } else {
                oldHttpProxySet = "false";
            }
            properties.put(SalesforceModuleParseAPI.HTTP_PROXY_SET, "true"); //$NON-NLS-1$
            properties.put(SalesforceModuleParseAPI.HTTP_PROXY_HOST, proxyHost);
            properties.put(SalesforceModuleParseAPI.HTTP_PROXY_PORT, proxyPort);
            properties.put(SalesforceModuleParseAPI.HTTP_PROXY_USER, proxyUsername == null ? "" : proxyUsername); //$NON-NLS-1$
            properties.put(SalesforceModuleParseAPI.HTTP_PROXY_PASSWORD, proxyPassword == null ? "" : proxyPassword); //$NON-NLS-1$
        } else if (httpsProxy && proxyHost != null && proxyPort != null) {
            if (properties.containsKey(SalesforceModuleParseAPI.HTTP_PROXY_HOST)) {
                oldHttpsProxySet = (String) properties.get(SalesforceModuleParseAPI.HTTPS_PROXY_SET);
                oldProxyHost = (String) properties.get(SalesforceModuleParseAPI.HTTPS_PROXY_HOST);
                oldProxyPort = (String) properties.get(SalesforceModuleParseAPI.HTTPS_PROXY_PORT);
                oldProxyUser = (String) properties.get(SalesforceModuleParseAPI.HTTPS_PROXY_USER);
                oldProxyPwd = (String) properties.get(SalesforceModuleParseAPI.HTTPS_PROXY_PASSWORD);
            } else {
                oldHttpsProxySet = "false";
            }
            properties.put(SalesforceModuleParseAPI.HTTPS_PROXY_SET, "true"); //$NON-NLS-1$
            properties.put(SalesforceModuleParseAPI.HTTPS_PROXY_HOST, proxyHost);
            properties.put(SalesforceModuleParseAPI.HTTPS_PROXY_PORT, proxyPort);
            properties.put(SalesforceModuleParseAPI.HTTPS_PROXY_USER, proxyUsername == null ? "" : proxyUsername); //$NON-NLS-1$
            properties.put(SalesforceModuleParseAPI.HTTPS_PROXY_PASSWORD, proxyPassword == null ? "" : proxyPassword); //$NON-NLS-1$

        }
    }

    public void resetAllProxy() {
        Properties properties = System.getProperties();
        if (properties.containsKey(SalesforceModuleParseAPI.SOCKS_PROXY_HOST) && oldSocksProxySet) {
            properties.put(SalesforceModuleParseAPI.SOCKS_PROXY_HOST, oldProxyHost); //$NON-NLS-1$
            properties.put(SalesforceModuleParseAPI.SOCKS_PROXY_PORT, oldProxyPort); //$NON-NLS-1$
            properties.put(SalesforceModuleParseAPI.SOCKS_PROXY_USERNAME, oldProxyUser); //$NON-NLS-1$
            properties.put(SalesforceModuleParseAPI.SOCKS_PROXY_PASSWORD, oldProxyPwd); //$NON-NLS-1$
        } else {
            properties.remove(SalesforceModuleParseAPI.SOCKS_PROXY_HOST); //$NON-NLS-1$
            properties.remove(SalesforceModuleParseAPI.SOCKS_PROXY_PORT); //$NON-NLS-1$
            properties.remove(SalesforceModuleParseAPI.SOCKS_PROXY_USERNAME); //$NON-NLS-1$
            properties.remove(SalesforceModuleParseAPI.SOCKS_PROXY_PASSWORD); //$NON-NLS-1$

        }

        if (properties.containsKey(SalesforceModuleParseAPI.HTTP_PROXY_SET) && "true".equals(oldHttpProxySet)) {
            properties.put(SalesforceModuleParseAPI.HTTP_PROXY_SET, oldHttpProxySet); //$NON-NLS-1$
            properties.put(SalesforceModuleParseAPI.HTTP_PROXY_HOST, oldProxyHost); //$NON-NLS-1$
            properties.put(SalesforceModuleParseAPI.HTTP_PROXY_PORT, oldProxyPort); //$NON-NLS-1$
            properties.put(SalesforceModuleParseAPI.HTTP_PROXY_USER, oldProxyUser); //$NON-NLS-1$
            properties.put(SalesforceModuleParseAPI.HTTP_PROXY_PASSWORD, oldProxyPwd); //$NON-NLS-1$
        } else {
            properties.remove(SalesforceModuleParseAPI.HTTP_PROXY_SET); //$NON-NLS-1$
            properties.remove(SalesforceModuleParseAPI.HTTP_PROXY_HOST); //$NON-NLS-1$
            properties.remove(SalesforceModuleParseAPI.HTTP_PROXY_PORT); //$NON-NLS-1$
            properties.remove(SalesforceModuleParseAPI.HTTP_PROXY_USER); //$NON-NLS-1$
            properties.remove(SalesforceModuleParseAPI.HTTP_PROXY_PASSWORD); //$NON-NLS-1$
        }

        if (properties.containsKey(SalesforceModuleParseAPI.HTTPS_PROXY_SET) && "true".equals(oldHttpsProxySet)) {
            properties.put(SalesforceModuleParseAPI.HTTPS_PROXY_SET, oldHttpsProxySet); //$NON-NLS-1$
            properties.put(SalesforceModuleParseAPI.HTTPS_PROXY_HOST, oldProxyHost); //$NON-NLS-1$
            properties.put(SalesforceModuleParseAPI.HTTPS_PROXY_PORT, oldProxyPort); //$NON-NLS-1$
            properties.put(SalesforceModuleParseAPI.HTTPS_PROXY_USER, oldProxyUser); //$NON-NLS-1$
            properties.put(SalesforceModuleParseAPI.HTTPS_PROXY_PASSWORD, oldProxyPwd); //$NON-NLS-1$
        } else {
            properties.remove(SalesforceModuleParseAPI.HTTPS_PROXY_SET); //$NON-NLS-1$
            properties.remove(SalesforceModuleParseAPI.HTTPS_PROXY_HOST); //$NON-NLS-1$
            properties.remove(SalesforceModuleParseAPI.HTTPS_PROXY_PORT); //$NON-NLS-1$
            properties.remove(SalesforceModuleParseAPI.HTTPS_PROXY_USER); //$NON-NLS-1$
            properties.remove(SalesforceModuleParseAPI.HTTPS_PROXY_PASSWORD); //$NON-NLS-1$
        }
    }

}
