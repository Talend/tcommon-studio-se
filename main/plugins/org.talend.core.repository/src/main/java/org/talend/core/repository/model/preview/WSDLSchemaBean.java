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
package org.talend.core.repository.model.preview;

import java.util.ArrayList;

/**
 * DOC qwei class global comment. Detailled comment
 */
public class WSDLSchemaBean {

    private String wslUrl;

    private String endpointURI;

    private String method;

    private boolean needAuth;

    private String userName;

    private String password;

    private ArrayList parameters;

    private boolean useProxy;

    private String proxyHost;

    private String proxyPort;

    private String proxyUser;

    private String proxyPassword;

    private int timeOut;

    private String serverName;

    private String serverNS;

    private String portName;

    private String portNS;

    private Boolean isInputModel;

    private ArrayList outParameters;

    /**
     * Getter for wslUrl.
     * 
     * @return the wslUrl
     */
    public String getWslUrl() {
        return this.wslUrl;
    }

    /**
     * Sets the wslUrl.
     * 
     * @param wslUrl the wslUrl to set
     */
    public void setWslUrl(String wslUrl) {
        this.wslUrl = wslUrl;
    }

    /**
     * Getter for method.
     * 
     * @return the method
     */
    public String getMethod() {
        return this.method;
    }

    /**
     * Sets the method.
     * 
     * @param method the method to set
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * Getter for needAuth.
     * 
     * @return the needAuth
     */
    public boolean isNeedAuth() {
        return this.needAuth;
    }

    /**
     * Sets the needAuth.
     * 
     * @param needAuth the needAuth to set
     */
    public void setNeedAuth(boolean needAuth) {
        this.needAuth = needAuth;
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
     * Getter for parameters.
     * 
     * @return the parameters
     */
    public ArrayList getParameters() {
        return this.parameters;
    }

    /**
     * Sets the parameters.
     * 
     * @param parameters the parameters to set
     */
    public void setParameters(ArrayList parameters) {
        this.parameters = parameters;
    }

    /**
     * Getter for useProxy.
     * 
     * @return the useProxy
     */
    public boolean isUseProxy() {
        return this.useProxy;
    }

    /**
     * Sets the useProxy.
     * 
     * @param useProxy the useProxy to set
     */
    public void setUseProxy(boolean useProxy) {
        this.useProxy = useProxy;
    }

    /**
     * Getter for proxyHost.
     * 
     * @return the proxyHost
     */
    public String getProxyHost() {
        return this.proxyHost;
    }

    /**
     * Sets the proxyHost.
     * 
     * @param proxyHost the proxyHost to set
     */
    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    /**
     * Getter for proxyPort.
     * 
     * @return the proxyPort
     */
    public String getProxyPort() {
        return this.proxyPort;
    }

    /**
     * Sets the proxyPort.
     * 
     * @param proxyPort the proxyPort to set
     */
    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }

    /**
     * Getter for proxyUser.
     * 
     * @return the proxyUser
     */
    public String getProxyUser() {
        return this.proxyUser;
    }

    /**
     * Sets the proxyUser.
     * 
     * @param proxyUser the proxyUser to set
     */
    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    /**
     * Getter for proxyPassword.
     * 
     * @return the proxyPassword
     */
    public String getProxyPassword() {
        return this.proxyPassword;
    }

    /**
     * DOC gcui Comment method "getTimeOut".
     * 
     * @return
     */
    public int getTimeOut() {
        return this.timeOut;
    }

    /**
     * DOC gcui Comment method "setTimeOut".
     * 
     * @param timeOut
     */
    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    /**
     * Sets the proxyPassword.
     * 
     * @param proxyPassword the proxyPassword to set
     */
    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    /**
     * Getter for endpointURI.
     * 
     * @return the endpointURI
     */
    public String getEndpointURI() {
        return this.endpointURI;
    }

    /**
     * Sets the endpointURI.
     * 
     * @param endpointURI the endpointURI to set
     */
    public void setEndpointURI(String endpointURI) {
        this.endpointURI = endpointURI;
    }

    public String getServerName() {
        return this.serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerNS() {
        return this.serverNS;
    }

    public void setServerNS(String serverNS) {
        this.serverNS = serverNS;
    }

    public String getPortName() {
        return this.portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public String getPortNS() {
        return this.portNS;
    }

    public void setPortNS(String portNS) {
        this.portNS = portNS;
    }

    public Boolean getIsInputModel() {
        return this.isInputModel;
    }

    public void setIsInputModel(Boolean isInputModel) {
        this.isInputModel = isInputModel;
    }

    public ArrayList getOutParameters() {
        return this.outParameters;
    }

    public void setOutParameters(ArrayList outParameters) {
        this.outParameters = outParameters;
    }

}
