/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.SalesforceModuleUnit;
import org.talend.core.model.metadata.builder.connection.SalesforceSchemaConnection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Salesforce Schema Connection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SalesforceSchemaConnectionImpl#getWebServiceUrl <em>Web Service Url</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SalesforceSchemaConnectionImpl#getUserName <em>User Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SalesforceSchemaConnectionImpl#getPassword <em>Password</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SalesforceSchemaConnectionImpl#getModuleName <em>Module Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SalesforceSchemaConnectionImpl#getQueryCondition <em>Query Condition</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SalesforceSchemaConnectionImpl#isUseCustomModuleName <em>Use Custom Module Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SalesforceSchemaConnectionImpl#isUseProxy <em>Use Proxy</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SalesforceSchemaConnectionImpl#getProxyHost <em>Proxy Host</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SalesforceSchemaConnectionImpl#getProxyPort <em>Proxy Port</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SalesforceSchemaConnectionImpl#getProxyUsername <em>Proxy Username</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SalesforceSchemaConnectionImpl#getProxyPassword <em>Proxy Password</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SalesforceSchemaConnectionImpl#getBatchSize <em>Batch Size</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SalesforceSchemaConnectionImpl#isUseHttpProxy <em>Use Http Proxy</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SalesforceSchemaConnectionImpl#isUseAlphbet <em>Use Alphbet</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SalesforceSchemaConnectionImpl#getTimeOut <em>Time Out</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SalesforceSchemaConnectionImpl#getModules <em>Modules</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SalesforceSchemaConnectionImpl#getWebServiceUrlTextForOAuth <em>Web Service Url Text For OAuth</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SalesforceSchemaConnectionImpl#getConsumeKey <em>Consume Key</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SalesforceSchemaConnectionImpl#getConsumeSecret <em>Consume Secret</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SalesforceSchemaConnectionImpl#getCallbackHost <em>Callback Host</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SalesforceSchemaConnectionImpl#getCallbackPort <em>Callback Port</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SalesforceSchemaConnectionImpl#getSalesforceVersion <em>Salesforce Version</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SalesforceSchemaConnectionImpl#getToken <em>Token</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SalesforceSchemaConnectionImpl#getLoginType <em>Login Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SalesforceSchemaConnectionImpl extends ConnectionImpl implements SalesforceSchemaConnection {

    /**
     * The default value of the '{@link #getWebServiceUrl() <em>Web Service Url</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getWebServiceUrl()
     * @generated
     * @ordered
     */
    protected static final String WEB_SERVICE_URL_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getWebServiceUrl() <em>Web Service Url</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getWebServiceUrl()
     * @generated
     * @ordered
     */
    protected String webServiceUrl = WEB_SERVICE_URL_EDEFAULT;

    /**
     * The default value of the '{@link #getUserName() <em>User Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUserName()
     * @generated
     * @ordered
     */
    protected static final String USER_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getUserName() <em>User Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUserName()
     * @generated
     * @ordered
     */
    protected String userName = USER_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getPassword() <em>Password</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPassword()
     * @generated
     * @ordered
     */
    protected static final String PASSWORD_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getPassword() <em>Password</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPassword()
     * @generated
     * @ordered
     */
    protected String password = PASSWORD_EDEFAULT;

    /**
     * The default value of the '{@link #getModuleName() <em>Module Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getModuleName()
     * @generated
     * @ordered
     */
    protected static final String MODULE_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getModuleName() <em>Module Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getModuleName()
     * @generated
     * @ordered
     */
    protected String moduleName = MODULE_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getQueryCondition() <em>Query Condition</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getQueryCondition()
     * @generated
     * @ordered
     */
    protected static final String QUERY_CONDITION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getQueryCondition() <em>Query Condition</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getQueryCondition()
     * @generated
     * @ordered
     */
    protected String queryCondition = QUERY_CONDITION_EDEFAULT;

    /**
     * The default value of the '{@link #isUseCustomModuleName() <em>Use Custom Module Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUseCustomModuleName()
     * @generated
     * @ordered
     */
    protected static final boolean USE_CUSTOM_MODULE_NAME_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isUseCustomModuleName() <em>Use Custom Module Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUseCustomModuleName()
     * @generated
     * @ordered
     */
    protected boolean useCustomModuleName = USE_CUSTOM_MODULE_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #isUseProxy() <em>Use Proxy</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUseProxy()
     * @generated
     * @ordered
     */
    protected static final boolean USE_PROXY_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isUseProxy() <em>Use Proxy</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUseProxy()
     * @generated
     * @ordered
     */
    protected boolean useProxy = USE_PROXY_EDEFAULT;

    /**
     * The default value of the '{@link #getProxyHost() <em>Proxy Host</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProxyHost()
     * @generated
     * @ordered
     */
    protected static final String PROXY_HOST_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getProxyHost() <em>Proxy Host</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProxyHost()
     * @generated
     * @ordered
     */
    protected String proxyHost = PROXY_HOST_EDEFAULT;

    /**
     * The default value of the '{@link #getProxyPort() <em>Proxy Port</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProxyPort()
     * @generated
     * @ordered
     */
    protected static final String PROXY_PORT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getProxyPort() <em>Proxy Port</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProxyPort()
     * @generated
     * @ordered
     */
    protected String proxyPort = PROXY_PORT_EDEFAULT;

    /**
     * The default value of the '{@link #getProxyUsername() <em>Proxy Username</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProxyUsername()
     * @generated
     * @ordered
     */
    protected static final String PROXY_USERNAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getProxyUsername() <em>Proxy Username</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProxyUsername()
     * @generated
     * @ordered
     */
    protected String proxyUsername = PROXY_USERNAME_EDEFAULT;

    /**
     * The default value of the '{@link #getProxyPassword() <em>Proxy Password</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProxyPassword()
     * @generated
     * @ordered
     */
    protected static final String PROXY_PASSWORD_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getProxyPassword() <em>Proxy Password</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProxyPassword()
     * @generated
     * @ordered
     */
    protected String proxyPassword = PROXY_PASSWORD_EDEFAULT;

    /**
     * The default value of the '{@link #getBatchSize() <em>Batch Size</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBatchSize()
     * @generated
     * @ordered
     */
    protected static final String BATCH_SIZE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getBatchSize() <em>Batch Size</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBatchSize()
     * @generated
     * @ordered
     */
    protected String batchSize = BATCH_SIZE_EDEFAULT;

    /**
     * The default value of the '{@link #isUseHttpProxy() <em>Use Http Proxy</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUseHttpProxy()
     * @generated
     * @ordered
     */
    protected static final boolean USE_HTTP_PROXY_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isUseHttpProxy() <em>Use Http Proxy</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUseHttpProxy()
     * @generated
     * @ordered
     */
    protected boolean useHttpProxy = USE_HTTP_PROXY_EDEFAULT;

    /**
     * The default value of the '{@link #isUseAlphbet() <em>Use Alphbet</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUseAlphbet()
     * @generated
     * @ordered
     */
    protected static final boolean USE_ALPHBET_EDEFAULT = true;

    /**
     * The cached value of the '{@link #isUseAlphbet() <em>Use Alphbet</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUseAlphbet()
     * @generated
     * @ordered
     */
    protected boolean useAlphbet = USE_ALPHBET_EDEFAULT;

    /**
     * The default value of the '{@link #getTimeOut() <em>Time Out</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTimeOut()
     * @generated
     * @ordered
     */
    protected static final String TIME_OUT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTimeOut() <em>Time Out</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTimeOut()
     * @generated
     * @ordered
     */
    protected String timeOut = TIME_OUT_EDEFAULT;

    /**
     * The cached value of the '{@link #getModules() <em>Modules</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getModules()
     * @generated
     * @ordered
     */
    protected EList<SalesforceModuleUnit> modules;

    /**
     * The default value of the '{@link #getWebServiceUrlTextForOAuth() <em>Web Service Url Text For OAuth</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getWebServiceUrlTextForOAuth()
     * @generated
     * @ordered
     */
    protected static final String WEB_SERVICE_URL_TEXT_FOR_OAUTH_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getWebServiceUrlTextForOAuth() <em>Web Service Url Text For OAuth</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getWebServiceUrlTextForOAuth()
     * @generated
     * @ordered
     */
    protected String webServiceUrlTextForOAuth = WEB_SERVICE_URL_TEXT_FOR_OAUTH_EDEFAULT;

    /**
     * The default value of the '{@link #getConsumeKey() <em>Consume Key</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getConsumeKey()
     * @generated
     * @ordered
     */
    protected static final String CONSUME_KEY_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getConsumeKey() <em>Consume Key</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getConsumeKey()
     * @generated
     * @ordered
     */
    protected String consumeKey = CONSUME_KEY_EDEFAULT;

    /**
     * The default value of the '{@link #getConsumeSecret() <em>Consume Secret</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getConsumeSecret()
     * @generated
     * @ordered
     */
    protected static final String CONSUME_SECRET_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getConsumeSecret() <em>Consume Secret</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getConsumeSecret()
     * @generated
     * @ordered
     */
    protected String consumeSecret = CONSUME_SECRET_EDEFAULT;

    /**
     * The default value of the '{@link #getCallbackHost() <em>Callback Host</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCallbackHost()
     * @generated
     * @ordered
     */
    protected static final String CALLBACK_HOST_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getCallbackHost() <em>Callback Host</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCallbackHost()
     * @generated
     * @ordered
     */
    protected String callbackHost = CALLBACK_HOST_EDEFAULT;

    /**
     * The default value of the '{@link #getCallbackPort() <em>Callback Port</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCallbackPort()
     * @generated
     * @ordered
     */
    protected static final String CALLBACK_PORT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getCallbackPort() <em>Callback Port</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCallbackPort()
     * @generated
     * @ordered
     */
    protected String callbackPort = CALLBACK_PORT_EDEFAULT;

    /**
     * The default value of the '{@link #getSalesforceVersion() <em>Salesforce Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSalesforceVersion()
     * @generated
     * @ordered
     */
    protected static final String SALESFORCE_VERSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSalesforceVersion() <em>Salesforce Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSalesforceVersion()
     * @generated
     * @ordered
     */
    protected String salesforceVersion = SALESFORCE_VERSION_EDEFAULT;

    /**
     * The default value of the '{@link #getToken() <em>Token</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getToken()
     * @generated
     * @ordered
     */
    protected static final String TOKEN_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getToken() <em>Token</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getToken()
     * @generated
     * @ordered
     */
    protected String token = TOKEN_EDEFAULT;

    /**
     * The default value of the '{@link #getLoginType() <em>Login Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLoginType()
     * @generated
     * @ordered
     */
    protected static final String LOGIN_TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLoginType() <em>Login Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLoginType()
     * @generated
     * @ordered
     */
    protected String loginType = LOGIN_TYPE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SalesforceSchemaConnectionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.SALESFORCE_SCHEMA_CONNECTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getWebServiceUrl() {
        return webServiceUrl;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setWebServiceUrl(String newWebServiceUrl) {
        String oldWebServiceUrl = webServiceUrl;
        webServiceUrl = newWebServiceUrl;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET,
                    ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__WEB_SERVICE_URL, oldWebServiceUrl, webServiceUrl));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getUserName() {
        return userName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUserName(String newUserName) {
        String oldUserName = userName;
        userName = newUserName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__USER_NAME,
                    oldUserName, userName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getPassword() {
        return password;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPassword(String newPassword) {
        String oldPassword = password;
        password = newPassword;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__PASSWORD,
                    oldPassword, password));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setModuleName(String newModuleName) {
        String oldModuleName = moduleName;
        moduleName = newModuleName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__MODULE_NAME,
                    oldModuleName, moduleName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getQueryCondition() {
        return queryCondition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setQueryCondition(String newQueryCondition) {
        String oldQueryCondition = queryCondition;
        queryCondition = newQueryCondition;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET,
                    ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__QUERY_CONDITION, oldQueryCondition, queryCondition));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isUseCustomModuleName() {
        return useCustomModuleName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUseCustomModuleName(boolean newUseCustomModuleName) {
        boolean oldUseCustomModuleName = useCustomModuleName;
        useCustomModuleName = newUseCustomModuleName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET,
                    ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__USE_CUSTOM_MODULE_NAME, oldUseCustomModuleName,
                    useCustomModuleName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isUseProxy() {
        return useProxy;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUseProxy(boolean newUseProxy) {
        boolean oldUseProxy = useProxy;
        useProxy = newUseProxy;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__USE_PROXY,
                    oldUseProxy, useProxy));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getProxyHost() {
        return proxyHost;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setProxyHost(String newProxyHost) {
        String oldProxyHost = proxyHost;
        proxyHost = newProxyHost;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__PROXY_HOST,
                    oldProxyHost, proxyHost));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getProxyPort() {
        return proxyPort;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setProxyPort(String newProxyPort) {
        String oldProxyPort = proxyPort;
        proxyPort = newProxyPort;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__PROXY_PORT,
                    oldProxyPort, proxyPort));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getProxyUsername() {
        return proxyUsername;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setProxyUsername(String newProxyUsername) {
        String oldProxyUsername = proxyUsername;
        proxyUsername = newProxyUsername;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__PROXY_USERNAME,
                    oldProxyUsername, proxyUsername));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getProxyPassword() {
        return proxyPassword;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setProxyPassword(String newProxyPassword) {
        String oldProxyPassword = proxyPassword;
        proxyPassword = newProxyPassword;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__PROXY_PASSWORD,
                    oldProxyPassword, proxyPassword));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getBatchSize() {
        return batchSize;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBatchSize(String newBatchSize) {
        String oldBatchSize = batchSize;
        batchSize = newBatchSize;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__BATCH_SIZE,
                    oldBatchSize, batchSize));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isUseHttpProxy() {
        return useHttpProxy;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUseHttpProxy(boolean newUseHttpProxy) {
        boolean oldUseHttpProxy = useHttpProxy;
        useHttpProxy = newUseHttpProxy;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__USE_HTTP_PROXY,
                    oldUseHttpProxy, useHttpProxy));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isUseAlphbet() {
        return useAlphbet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUseAlphbet(boolean newUseAlphbet) {
        boolean oldUseAlphbet = useAlphbet;
        useAlphbet = newUseAlphbet;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__USE_ALPHBET,
                    oldUseAlphbet, useAlphbet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getTimeOut() {
        return timeOut;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTimeOut(String newTimeOut) {
        String oldTimeOut = timeOut;
        timeOut = newTimeOut;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__TIME_OUT,
                    oldTimeOut, timeOut));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<SalesforceModuleUnit> getModules() {
        if (modules == null) {
            modules = new EObjectContainmentWithInverseEList.Resolving<SalesforceModuleUnit>(SalesforceModuleUnit.class, this,
                    ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__MODULES, ConnectionPackage.SALESFORCE_MODULE_UNIT__CONNECTION);
        }
        return modules;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getWebServiceUrlTextForOAuth() {
        return webServiceUrlTextForOAuth;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setWebServiceUrlTextForOAuth(String newWebServiceUrlTextForOAuth) {
        String oldWebServiceUrlTextForOAuth = webServiceUrlTextForOAuth;
        webServiceUrlTextForOAuth = newWebServiceUrlTextForOAuth;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET,
                    ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__WEB_SERVICE_URL_TEXT_FOR_OAUTH, oldWebServiceUrlTextForOAuth,
                    webServiceUrlTextForOAuth));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getConsumeKey() {
        return consumeKey;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setConsumeKey(String newConsumeKey) {
        String oldConsumeKey = consumeKey;
        consumeKey = newConsumeKey;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__CONSUME_KEY,
                    oldConsumeKey, consumeKey));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getConsumeSecret() {
        return consumeSecret;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setConsumeSecret(String newConsumeSecret) {
        String oldConsumeSecret = consumeSecret;
        consumeSecret = newConsumeSecret;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__CONSUME_SECRET,
                    oldConsumeSecret, consumeSecret));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getCallbackHost() {
        return callbackHost;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCallbackHost(String newCallbackHost) {
        String oldCallbackHost = callbackHost;
        callbackHost = newCallbackHost;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__CALLBACK_HOST,
                    oldCallbackHost, callbackHost));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getCallbackPort() {
        return callbackPort;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCallbackPort(String newCallbackPort) {
        String oldCallbackPort = callbackPort;
        callbackPort = newCallbackPort;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__CALLBACK_PORT,
                    oldCallbackPort, callbackPort));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSalesforceVersion() {
        return salesforceVersion;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSalesforceVersion(String newSalesforceVersion) {
        String oldSalesforceVersion = salesforceVersion;
        salesforceVersion = newSalesforceVersion;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET,
                    ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__SALESFORCE_VERSION, oldSalesforceVersion, salesforceVersion));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getToken() {
        return token;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setToken(String newToken) {
        String oldToken = token;
        token = newToken;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__TOKEN,
                    oldToken, token));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getLoginType() {
        return loginType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLoginType(String newLoginType) {
        String oldLoginType = loginType;
        loginType = newLoginType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__LOGIN_TYPE,
                    oldLoginType, loginType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__MODULES:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getModules()).basicAdd(otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__MODULES:
            return ((InternalEList<?>) getModules()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__WEB_SERVICE_URL:
            return getWebServiceUrl();
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__USER_NAME:
            return getUserName();
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__PASSWORD:
            return getPassword();
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__MODULE_NAME:
            return getModuleName();
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__QUERY_CONDITION:
            return getQueryCondition();
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__USE_CUSTOM_MODULE_NAME:
            return isUseCustomModuleName();
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__USE_PROXY:
            return isUseProxy();
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__PROXY_HOST:
            return getProxyHost();
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__PROXY_PORT:
            return getProxyPort();
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__PROXY_USERNAME:
            return getProxyUsername();
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__PROXY_PASSWORD:
            return getProxyPassword();
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__BATCH_SIZE:
            return getBatchSize();
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__USE_HTTP_PROXY:
            return isUseHttpProxy();
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__USE_ALPHBET:
            return isUseAlphbet();
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__TIME_OUT:
            return getTimeOut();
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__MODULES:
            return getModules();
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__WEB_SERVICE_URL_TEXT_FOR_OAUTH:
            return getWebServiceUrlTextForOAuth();
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__CONSUME_KEY:
            return getConsumeKey();
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__CONSUME_SECRET:
            return getConsumeSecret();
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__CALLBACK_HOST:
            return getCallbackHost();
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__CALLBACK_PORT:
            return getCallbackPort();
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__SALESFORCE_VERSION:
            return getSalesforceVersion();
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__TOKEN:
            return getToken();
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__LOGIN_TYPE:
            return getLoginType();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__WEB_SERVICE_URL:
            setWebServiceUrl((String) newValue);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__USER_NAME:
            setUserName((String) newValue);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__PASSWORD:
            setPassword((String) newValue);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__MODULE_NAME:
            setModuleName((String) newValue);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__QUERY_CONDITION:
            setQueryCondition((String) newValue);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__USE_CUSTOM_MODULE_NAME:
            setUseCustomModuleName((Boolean) newValue);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__USE_PROXY:
            setUseProxy((Boolean) newValue);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__PROXY_HOST:
            setProxyHost((String) newValue);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__PROXY_PORT:
            setProxyPort((String) newValue);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__PROXY_USERNAME:
            setProxyUsername((String) newValue);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__PROXY_PASSWORD:
            setProxyPassword((String) newValue);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__BATCH_SIZE:
            setBatchSize((String) newValue);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__USE_HTTP_PROXY:
            setUseHttpProxy((Boolean) newValue);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__USE_ALPHBET:
            setUseAlphbet((Boolean) newValue);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__TIME_OUT:
            setTimeOut((String) newValue);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__MODULES:
            getModules().clear();
            getModules().addAll((Collection<? extends SalesforceModuleUnit>) newValue);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__WEB_SERVICE_URL_TEXT_FOR_OAUTH:
            setWebServiceUrlTextForOAuth((String) newValue);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__CONSUME_KEY:
            setConsumeKey((String) newValue);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__CONSUME_SECRET:
            setConsumeSecret((String) newValue);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__CALLBACK_HOST:
            setCallbackHost((String) newValue);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__CALLBACK_PORT:
            setCallbackPort((String) newValue);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__SALESFORCE_VERSION:
            setSalesforceVersion((String) newValue);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__TOKEN:
            setToken((String) newValue);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__LOGIN_TYPE:
            setLoginType((String) newValue);
            return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__WEB_SERVICE_URL:
            setWebServiceUrl(WEB_SERVICE_URL_EDEFAULT);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__USER_NAME:
            setUserName(USER_NAME_EDEFAULT);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__PASSWORD:
            setPassword(PASSWORD_EDEFAULT);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__MODULE_NAME:
            setModuleName(MODULE_NAME_EDEFAULT);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__QUERY_CONDITION:
            setQueryCondition(QUERY_CONDITION_EDEFAULT);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__USE_CUSTOM_MODULE_NAME:
            setUseCustomModuleName(USE_CUSTOM_MODULE_NAME_EDEFAULT);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__USE_PROXY:
            setUseProxy(USE_PROXY_EDEFAULT);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__PROXY_HOST:
            setProxyHost(PROXY_HOST_EDEFAULT);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__PROXY_PORT:
            setProxyPort(PROXY_PORT_EDEFAULT);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__PROXY_USERNAME:
            setProxyUsername(PROXY_USERNAME_EDEFAULT);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__PROXY_PASSWORD:
            setProxyPassword(PROXY_PASSWORD_EDEFAULT);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__BATCH_SIZE:
            setBatchSize(BATCH_SIZE_EDEFAULT);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__USE_HTTP_PROXY:
            setUseHttpProxy(USE_HTTP_PROXY_EDEFAULT);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__USE_ALPHBET:
            setUseAlphbet(USE_ALPHBET_EDEFAULT);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__TIME_OUT:
            setTimeOut(TIME_OUT_EDEFAULT);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__MODULES:
            getModules().clear();
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__WEB_SERVICE_URL_TEXT_FOR_OAUTH:
            setWebServiceUrlTextForOAuth(WEB_SERVICE_URL_TEXT_FOR_OAUTH_EDEFAULT);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__CONSUME_KEY:
            setConsumeKey(CONSUME_KEY_EDEFAULT);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__CONSUME_SECRET:
            setConsumeSecret(CONSUME_SECRET_EDEFAULT);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__CALLBACK_HOST:
            setCallbackHost(CALLBACK_HOST_EDEFAULT);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__CALLBACK_PORT:
            setCallbackPort(CALLBACK_PORT_EDEFAULT);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__SALESFORCE_VERSION:
            setSalesforceVersion(SALESFORCE_VERSION_EDEFAULT);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__TOKEN:
            setToken(TOKEN_EDEFAULT);
            return;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__LOGIN_TYPE:
            setLoginType(LOGIN_TYPE_EDEFAULT);
            return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__WEB_SERVICE_URL:
            return WEB_SERVICE_URL_EDEFAULT == null ? webServiceUrl != null : !WEB_SERVICE_URL_EDEFAULT.equals(webServiceUrl);
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__USER_NAME:
            return USER_NAME_EDEFAULT == null ? userName != null : !USER_NAME_EDEFAULT.equals(userName);
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__PASSWORD:
            return PASSWORD_EDEFAULT == null ? password != null : !PASSWORD_EDEFAULT.equals(password);
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__MODULE_NAME:
            return MODULE_NAME_EDEFAULT == null ? moduleName != null : !MODULE_NAME_EDEFAULT.equals(moduleName);
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__QUERY_CONDITION:
            return QUERY_CONDITION_EDEFAULT == null ? queryCondition != null : !QUERY_CONDITION_EDEFAULT.equals(queryCondition);
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__USE_CUSTOM_MODULE_NAME:
            return useCustomModuleName != USE_CUSTOM_MODULE_NAME_EDEFAULT;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__USE_PROXY:
            return useProxy != USE_PROXY_EDEFAULT;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__PROXY_HOST:
            return PROXY_HOST_EDEFAULT == null ? proxyHost != null : !PROXY_HOST_EDEFAULT.equals(proxyHost);
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__PROXY_PORT:
            return PROXY_PORT_EDEFAULT == null ? proxyPort != null : !PROXY_PORT_EDEFAULT.equals(proxyPort);
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__PROXY_USERNAME:
            return PROXY_USERNAME_EDEFAULT == null ? proxyUsername != null : !PROXY_USERNAME_EDEFAULT.equals(proxyUsername);
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__PROXY_PASSWORD:
            return PROXY_PASSWORD_EDEFAULT == null ? proxyPassword != null : !PROXY_PASSWORD_EDEFAULT.equals(proxyPassword);
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__BATCH_SIZE:
            return BATCH_SIZE_EDEFAULT == null ? batchSize != null : !BATCH_SIZE_EDEFAULT.equals(batchSize);
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__USE_HTTP_PROXY:
            return useHttpProxy != USE_HTTP_PROXY_EDEFAULT;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__USE_ALPHBET:
            return useAlphbet != USE_ALPHBET_EDEFAULT;
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__TIME_OUT:
            return TIME_OUT_EDEFAULT == null ? timeOut != null : !TIME_OUT_EDEFAULT.equals(timeOut);
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__MODULES:
            return modules != null && !modules.isEmpty();
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__WEB_SERVICE_URL_TEXT_FOR_OAUTH:
            return WEB_SERVICE_URL_TEXT_FOR_OAUTH_EDEFAULT == null ? webServiceUrlTextForOAuth != null
                    : !WEB_SERVICE_URL_TEXT_FOR_OAUTH_EDEFAULT.equals(webServiceUrlTextForOAuth);
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__CONSUME_KEY:
            return CONSUME_KEY_EDEFAULT == null ? consumeKey != null : !CONSUME_KEY_EDEFAULT.equals(consumeKey);
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__CONSUME_SECRET:
            return CONSUME_SECRET_EDEFAULT == null ? consumeSecret != null : !CONSUME_SECRET_EDEFAULT.equals(consumeSecret);
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__CALLBACK_HOST:
            return CALLBACK_HOST_EDEFAULT == null ? callbackHost != null : !CALLBACK_HOST_EDEFAULT.equals(callbackHost);
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__CALLBACK_PORT:
            return CALLBACK_PORT_EDEFAULT == null ? callbackPort != null : !CALLBACK_PORT_EDEFAULT.equals(callbackPort);
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__SALESFORCE_VERSION:
            return SALESFORCE_VERSION_EDEFAULT == null ? salesforceVersion != null : !SALESFORCE_VERSION_EDEFAULT
                    .equals(salesforceVersion);
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__TOKEN:
            return TOKEN_EDEFAULT == null ? token != null : !TOKEN_EDEFAULT.equals(token);
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__LOGIN_TYPE:
            return LOGIN_TYPE_EDEFAULT == null ? loginType != null : !LOGIN_TYPE_EDEFAULT.equals(loginType);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy())
            return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (webServiceUrl: ");
        result.append(webServiceUrl);
        result.append(", userName: ");
        result.append(userName);
        result.append(", password: ");
        result.append(password);
        result.append(", moduleName: ");
        result.append(moduleName);
        result.append(", queryCondition: ");
        result.append(queryCondition);
        result.append(", useCustomModuleName: ");
        result.append(useCustomModuleName);
        result.append(", useProxy: ");
        result.append(useProxy);
        result.append(", proxyHost: ");
        result.append(proxyHost);
        result.append(", proxyPort: ");
        result.append(proxyPort);
        result.append(", proxyUsername: ");
        result.append(proxyUsername);
        result.append(", proxyPassword: ");
        result.append(proxyPassword);
        result.append(", batchSize: ");
        result.append(batchSize);
        result.append(", useHttpProxy: ");
        result.append(useHttpProxy);
        result.append(", useAlphbet: ");
        result.append(useAlphbet);
        result.append(", timeOut: ");
        result.append(timeOut);
        result.append(", webServiceUrlTextForOAuth: ");
        result.append(webServiceUrlTextForOAuth);
        result.append(", consumeKey: ");
        result.append(consumeKey);
        result.append(", consumeSecret: ");
        result.append(consumeSecret);
        result.append(", callbackHost: ");
        result.append(callbackHost);
        result.append(", callbackPort: ");
        result.append(callbackPort);
        result.append(", salesforceVersion: ");
        result.append(salesforceVersion);
        result.append(", token: ");
        result.append(token);
        result.append(", loginType: ");
        result.append(loginType);
        result.append(')');
        return result.toString();
    }

} //SalesforceSchemaConnectionImpl
