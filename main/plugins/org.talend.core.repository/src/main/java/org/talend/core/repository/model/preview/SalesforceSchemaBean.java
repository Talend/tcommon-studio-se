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
package org.talend.core.repository.model.preview;

/**
 * DOC YeXiaowei class global comment. Detailled comment <br/>
 * 
 */
public class SalesforceSchemaBean {

    public static final int DEFAULT_TIME_OUT = 60000;

    public static final int DEFAULT_BATCH_SIZE = 250;

    private String webServerUrl = null;

    private String userName = null;

    private String password = null;

    private String batchSize = String.valueOf(DEFAULT_BATCH_SIZE);

    private String moduleName = null;

    private String queryCondition = null;

    private boolean useCustomModule = false;

    private boolean useProxy = false;

    private boolean uesHttp = false;

    private String proxyHost = null;

    private String proxyPort = null;

    private String proxyUsername = null;

    private String proxyPassword = null;

    private int timeOut = DEFAULT_TIME_OUT;

    /**
     * Getter for webServerUrl.
     * 
     * @return the webServerUrl
     */
    public String getWebServerUrl() {
        return this.webServerUrl;
    }

    /**
     * Sets the webServerUrl.
     * 
     * @param webServerUrl the webServerUrl to set
     */
    public void setWebServerUrl(String webServerUrl) {
        this.webServerUrl = webServerUrl;
    }

    /**
     * Getter for userName.
     * 
     * @return the userName
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * Sets the userName.
     * 
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Getter for password.
     * 
     * @return the password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Sets the password.
     * 
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter for moduleName.
     * 
     * @return the moduleName
     */
    public String getModuleName() {
        return this.moduleName;
    }

    /**
     * Sets the moduleName.
     * 
     * @param moduleName the moduleName to set
     */
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    /**
     * Getter for queryCondition.
     * 
     * @return the queryCondition
     */
    public String getQueryCondition() {
        return this.queryCondition;
    }

    /**
     * Sets the queryCondition.
     * 
     * @param queryCondition the queryCondition to set
     */
    public void setQueryCondition(String queryCondition) {
        this.queryCondition = queryCondition;
    }

    public boolean isUseCustomModule() {
        return useCustomModule;
    }

    public void setUseCustomModule(boolean useCustomModule) {
        this.useCustomModule = useCustomModule;
    }

    // add for feature 7507
    public String getBatchSize() {
        return this.batchSize;
    }

    public void setBatchSize(String batchSize) {
        this.batchSize = batchSize;
    }

    public boolean isUseProxy() {
        return this.useProxy;
    }

    public void setUseProxy(boolean useProxy) {
        this.useProxy = useProxy;
    }

    public String getProxyHost() {
        return this.proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public String getProxyPort() {
        return this.proxyPort;
    }

    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyUsername() {
        return this.proxyUsername;
    }

    public void setProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
    }

    public String getProxyPassword() {
        return this.proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public boolean isUesHttp() {
        return this.uesHttp;
    }

    public void setUesHttp(boolean uesHttp) {
        this.uesHttp = uesHttp;
    }

    public int getTimeOut() {
        return this.timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

}
