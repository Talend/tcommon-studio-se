/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>LDAP Schema Connection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.LDAPSchemaConnectionImpl#getHost <em>Host</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.LDAPSchemaConnectionImpl#getPort <em>Port</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.LDAPSchemaConnectionImpl#getProtocol <em>Protocol</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.LDAPSchemaConnectionImpl#getFilter <em>Filter</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.LDAPSchemaConnectionImpl#getSeparator <em>Separator</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.LDAPSchemaConnectionImpl#isUseAdvanced <em>Use Advanced</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.LDAPSchemaConnectionImpl#getStorePath <em>Store Path</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.LDAPSchemaConnectionImpl#isUseLimit <em>Use Limit</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.LDAPSchemaConnectionImpl#isUseAuthen <em>Use Authen</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.LDAPSchemaConnectionImpl#getBindPrincipal <em>Bind Principal</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.LDAPSchemaConnectionImpl#getBindPassword <em>Bind Password</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.LDAPSchemaConnectionImpl#getLimitValue <em>Limit Value</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.LDAPSchemaConnectionImpl#getEncryptionMethodName <em>Encryption Method Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.LDAPSchemaConnectionImpl#getValue <em>Value</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.LDAPSchemaConnectionImpl#isSavePassword <em>Save Password</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.LDAPSchemaConnectionImpl#getAliases <em>Aliases</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.LDAPSchemaConnectionImpl#getReferrals <em>Referrals</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.LDAPSchemaConnectionImpl#getCountLimit <em>Count Limit</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.LDAPSchemaConnectionImpl#getTimeOutLimit <em>Time Out Limit</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.LDAPSchemaConnectionImpl#getBaseDNs <em>Base DNs</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.LDAPSchemaConnectionImpl#isGetBaseDNsFromRoot <em>Get Base DNs From Root</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.LDAPSchemaConnectionImpl#getReturnAttributes <em>Return Attributes</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.LDAPSchemaConnectionImpl#getSelectedDN <em>Selected DN</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LDAPSchemaConnectionImpl extends ConnectionImpl implements LDAPSchemaConnection {

    /**
     * The default value of the '{@link #getHost() <em>Host</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHost()
     * @generated
     * @ordered
     */
    protected static final String HOST_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getHost() <em>Host</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHost()
     * @generated
     * @ordered
     */
    protected String host = HOST_EDEFAULT;

    /**
     * The default value of the '{@link #getPort() <em>Port</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPort()
     * @generated
     * @ordered
     */
    protected static final String PORT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getPort() <em>Port</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPort()
     * @generated
     * @ordered
     */
    protected String port = PORT_EDEFAULT;

    /**
     * The default value of the '{@link #getProtocol() <em>Protocol</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProtocol()
     * @generated
     * @ordered
     */
    protected static final String PROTOCOL_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getProtocol() <em>Protocol</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProtocol()
     * @generated
     * @ordered
     */
    protected String protocol = PROTOCOL_EDEFAULT;

    /**
     * The default value of the '{@link #getFilter() <em>Filter</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFilter()
     * @generated
     * @ordered
     */
    protected static final String FILTER_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFilter() <em>Filter</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFilter()
     * @generated
     * @ordered
     */
    protected String filter = FILTER_EDEFAULT;

    /**
     * The default value of the '{@link #getSeparator() <em>Separator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSeparator()
     * @generated
     * @ordered
     */
    protected static final String SEPARATOR_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSeparator() <em>Separator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSeparator()
     * @generated
     * @ordered
     */
    protected String separator = SEPARATOR_EDEFAULT;

    /**
     * The default value of the '{@link #isUseAdvanced() <em>Use Advanced</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUseAdvanced()
     * @generated
     * @ordered
     */
    protected static final boolean USE_ADVANCED_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isUseAdvanced() <em>Use Advanced</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUseAdvanced()
     * @generated
     * @ordered
     */
    protected boolean useAdvanced = USE_ADVANCED_EDEFAULT;

    /**
     * The default value of the '{@link #getStorePath() <em>Store Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStorePath()
     * @generated
     * @ordered
     */
    protected static final String STORE_PATH_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getStorePath() <em>Store Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStorePath()
     * @generated
     * @ordered
     */
    protected String storePath = STORE_PATH_EDEFAULT;

    /**
     * The default value of the '{@link #isUseLimit() <em>Use Limit</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUseLimit()
     * @generated
     * @ordered
     */
    protected static final boolean USE_LIMIT_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isUseLimit() <em>Use Limit</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUseLimit()
     * @generated
     * @ordered
     */
    protected boolean useLimit = USE_LIMIT_EDEFAULT;

    /**
     * The default value of the '{@link #isUseAuthen() <em>Use Authen</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUseAuthen()
     * @generated
     * @ordered
     */
    protected static final boolean USE_AUTHEN_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isUseAuthen() <em>Use Authen</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUseAuthen()
     * @generated
     * @ordered
     */
    protected boolean useAuthen = USE_AUTHEN_EDEFAULT;

    /**
     * The default value of the '{@link #getBindPrincipal() <em>Bind Principal</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBindPrincipal()
     * @generated
     * @ordered
     */
    protected static final String BIND_PRINCIPAL_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getBindPrincipal() <em>Bind Principal</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBindPrincipal()
     * @generated
     * @ordered
     */
    protected String bindPrincipal = BIND_PRINCIPAL_EDEFAULT;

    /**
     * The default value of the '{@link #getBindPassword() <em>Bind Password</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBindPassword()
     * @generated
     * @ordered
     */
    protected static final String BIND_PASSWORD_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getBindPassword() <em>Bind Password</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBindPassword()
     * @generated
     * @ordered
     */
    protected String bindPassword = BIND_PASSWORD_EDEFAULT;

    /**
     * The default value of the '{@link #getLimitValue() <em>Limit Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLimitValue()
     * @generated
     * @ordered
     */
    protected static final int LIMIT_VALUE_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getLimitValue() <em>Limit Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLimitValue()
     * @generated
     * @ordered
     */
    protected int limitValue = LIMIT_VALUE_EDEFAULT;

    /**
     * The default value of the '{@link #getEncryptionMethodName() <em>Encryption Method Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEncryptionMethodName()
     * @generated
     * @ordered
     */
    protected static final String ENCRYPTION_METHOD_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getEncryptionMethodName() <em>Encryption Method Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEncryptionMethodName()
     * @generated
     * @ordered
     */
    protected String encryptionMethodName = ENCRYPTION_METHOD_NAME_EDEFAULT;

    /**
     * The cached value of the '{@link #getValue() <em>Value</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected EList<String> value;

    /**
     * The default value of the '{@link #isSavePassword() <em>Save Password</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSavePassword()
     * @generated
     * @ordered
     */
    protected static final boolean SAVE_PASSWORD_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isSavePassword() <em>Save Password</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSavePassword()
     * @generated
     * @ordered
     */
    protected boolean savePassword = SAVE_PASSWORD_EDEFAULT;

    /**
     * The default value of the '{@link #getAliases() <em>Aliases</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAliases()
     * @generated
     * @ordered
     */
    protected static final String ALIASES_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getAliases() <em>Aliases</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAliases()
     * @generated
     * @ordered
     */
    protected String aliases = ALIASES_EDEFAULT;

    /**
     * The default value of the '{@link #getReferrals() <em>Referrals</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getReferrals()
     * @generated
     * @ordered
     */
    protected static final String REFERRALS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getReferrals() <em>Referrals</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getReferrals()
     * @generated
     * @ordered
     */
    protected String referrals = REFERRALS_EDEFAULT;

    /**
     * The default value of the '{@link #getCountLimit() <em>Count Limit</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCountLimit()
     * @generated
     * @ordered
     */
    protected static final String COUNT_LIMIT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getCountLimit() <em>Count Limit</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCountLimit()
     * @generated
     * @ordered
     */
    protected String countLimit = COUNT_LIMIT_EDEFAULT;

    /**
     * The default value of the '{@link #getTimeOutLimit() <em>Time Out Limit</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTimeOutLimit()
     * @generated
     * @ordered
     */
    protected static final String TIME_OUT_LIMIT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTimeOutLimit() <em>Time Out Limit</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTimeOutLimit()
     * @generated
     * @ordered
     */
    protected String timeOutLimit = TIME_OUT_LIMIT_EDEFAULT;

    /**
     * The cached value of the '{@link #getBaseDNs() <em>Base DNs</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBaseDNs()
     * @generated
     * @ordered
     */
    protected EList<String> baseDNs;

    /**
     * The default value of the '{@link #isGetBaseDNsFromRoot() <em>Get Base DNs From Root</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isGetBaseDNsFromRoot()
     * @generated
     * @ordered
     */
    protected static final boolean GET_BASE_DNS_FROM_ROOT_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isGetBaseDNsFromRoot() <em>Get Base DNs From Root</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isGetBaseDNsFromRoot()
     * @generated
     * @ordered
     */
    protected boolean getBaseDNsFromRoot = GET_BASE_DNS_FROM_ROOT_EDEFAULT;

    /**
     * The cached value of the '{@link #getReturnAttributes() <em>Return Attributes</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getReturnAttributes()
     * @generated
     * @ordered
     */
    protected EList<String> returnAttributes;

    /**
     * The default value of the '{@link #getSelectedDN() <em>Selected DN</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSelectedDN()
     * @generated
     * @ordered
     */
    protected static final String SELECTED_DN_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSelectedDN() <em>Selected DN</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSelectedDN()
     * @generated
     * @ordered
     */
    protected String selectedDN = SELECTED_DN_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected LDAPSchemaConnectionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.LDAP_SCHEMA_CONNECTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getHost() {
        return host;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setHost(String newHost) {
        String oldHost = host;
        host = newHost;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.LDAP_SCHEMA_CONNECTION__HOST, oldHost, host));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getPort() {
        return port;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPort(String newPort) {
        String oldPort = port;
        port = newPort;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.LDAP_SCHEMA_CONNECTION__PORT, oldPort, port));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setProtocol(String newProtocol) {
        String oldProtocol = protocol;
        protocol = newProtocol;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.LDAP_SCHEMA_CONNECTION__PROTOCOL,
                    oldProtocol, protocol));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFilter() {
        return filter;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFilter(String newFilter) {
        String oldFilter = filter;
        filter = newFilter;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.LDAP_SCHEMA_CONNECTION__FILTER, oldFilter,
                    filter));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSeparator() {
        return separator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSeparator(String newSeparator) {
        String oldSeparator = separator;
        separator = newSeparator;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.LDAP_SCHEMA_CONNECTION__SEPARATOR,
                    oldSeparator, separator));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isUseAdvanced() {
        return useAdvanced;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUseAdvanced(boolean newUseAdvanced) {
        boolean oldUseAdvanced = useAdvanced;
        useAdvanced = newUseAdvanced;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.LDAP_SCHEMA_CONNECTION__USE_ADVANCED,
                    oldUseAdvanced, useAdvanced));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getStorePath() {
        return storePath;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setStorePath(String newStorePath) {
        String oldStorePath = storePath;
        storePath = newStorePath;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.LDAP_SCHEMA_CONNECTION__STORE_PATH,
                    oldStorePath, storePath));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isUseLimit() {
        return useLimit;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUseLimit(boolean newUseLimit) {
        boolean oldUseLimit = useLimit;
        useLimit = newUseLimit;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.LDAP_SCHEMA_CONNECTION__USE_LIMIT,
                    oldUseLimit, useLimit));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isUseAuthen() {
        return useAuthen;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUseAuthen(boolean newUseAuthen) {
        boolean oldUseAuthen = useAuthen;
        useAuthen = newUseAuthen;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.LDAP_SCHEMA_CONNECTION__USE_AUTHEN,
                    oldUseAuthen, useAuthen));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getBindPrincipal() {
        return bindPrincipal;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBindPrincipal(String newBindPrincipal) {
        String oldBindPrincipal = bindPrincipal;
        bindPrincipal = newBindPrincipal;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.LDAP_SCHEMA_CONNECTION__BIND_PRINCIPAL,
                    oldBindPrincipal, bindPrincipal));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getBindPassword() {
        return bindPassword;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBindPassword(String newBindPassword) {
        String oldBindPassword = bindPassword;
        bindPassword = newBindPassword;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.LDAP_SCHEMA_CONNECTION__BIND_PASSWORD,
                    oldBindPassword, bindPassword));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public int getLimitValue() {
        return limitValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLimitValue(int newLimitValue) {
        int oldLimitValue = limitValue;
        limitValue = newLimitValue;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.LDAP_SCHEMA_CONNECTION__LIMIT_VALUE,
                    oldLimitValue, limitValue));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getEncryptionMethodName() {
        return encryptionMethodName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setEncryptionMethodName(String newEncryptionMethodName) {
        String oldEncryptionMethodName = encryptionMethodName;
        encryptionMethodName = newEncryptionMethodName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET,
                    ConnectionPackage.LDAP_SCHEMA_CONNECTION__ENCRYPTION_METHOD_NAME, oldEncryptionMethodName,
                    encryptionMethodName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<String> getValue() {
        if (value == null) {
            value = new EDataTypeUniqueEList<String>(String.class, this, ConnectionPackage.LDAP_SCHEMA_CONNECTION__VALUE);
        }
        return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSavePassword() {
        return savePassword;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSavePassword(boolean newSavePassword) {
        boolean oldSavePassword = savePassword;
        savePassword = newSavePassword;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.LDAP_SCHEMA_CONNECTION__SAVE_PASSWORD,
                    oldSavePassword, savePassword));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getAliases() {
        return aliases;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAliases(String newAliases) {
        String oldAliases = aliases;
        aliases = newAliases;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.LDAP_SCHEMA_CONNECTION__ALIASES, oldAliases,
                    aliases));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getReferrals() {
        return referrals;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setReferrals(String newReferrals) {
        String oldReferrals = referrals;
        referrals = newReferrals;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.LDAP_SCHEMA_CONNECTION__REFERRALS,
                    oldReferrals, referrals));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getCountLimit() {
        return countLimit;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCountLimit(String newCountLimit) {
        String oldCountLimit = countLimit;
        countLimit = newCountLimit;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.LDAP_SCHEMA_CONNECTION__COUNT_LIMIT,
                    oldCountLimit, countLimit));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getTimeOutLimit() {
        return timeOutLimit;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTimeOutLimit(String newTimeOutLimit) {
        String oldTimeOutLimit = timeOutLimit;
        timeOutLimit = newTimeOutLimit;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.LDAP_SCHEMA_CONNECTION__TIME_OUT_LIMIT,
                    oldTimeOutLimit, timeOutLimit));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<String> getBaseDNs() {
        if (baseDNs == null) {
            baseDNs = new EDataTypeUniqueEList<String>(String.class, this, ConnectionPackage.LDAP_SCHEMA_CONNECTION__BASE_DNS);
        }
        return baseDNs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isGetBaseDNsFromRoot() {
        return getBaseDNsFromRoot;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGetBaseDNsFromRoot(boolean newGetBaseDNsFromRoot) {
        boolean oldGetBaseDNsFromRoot = getBaseDNsFromRoot;
        getBaseDNsFromRoot = newGetBaseDNsFromRoot;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET,
                    ConnectionPackage.LDAP_SCHEMA_CONNECTION__GET_BASE_DNS_FROM_ROOT, oldGetBaseDNsFromRoot, getBaseDNsFromRoot));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<String> getReturnAttributes() {
        if (returnAttributes == null) {
            returnAttributes = new EDataTypeUniqueEList<String>(String.class, this,
                    ConnectionPackage.LDAP_SCHEMA_CONNECTION__RETURN_ATTRIBUTES);
        }
        return returnAttributes;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSelectedDN() {
        return selectedDN;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSelectedDN(String newSelectedDN) {
        String oldSelectedDN = selectedDN;
        selectedDN = newSelectedDN;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.LDAP_SCHEMA_CONNECTION__SELECTED_DN,
                    oldSelectedDN, selectedDN));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__HOST:
            return getHost();
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__PORT:
            return getPort();
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__PROTOCOL:
            return getProtocol();
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__FILTER:
            return getFilter();
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__SEPARATOR:
            return getSeparator();
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__USE_ADVANCED:
            return isUseAdvanced();
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__STORE_PATH:
            return getStorePath();
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__USE_LIMIT:
            return isUseLimit();
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__USE_AUTHEN:
            return isUseAuthen();
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__BIND_PRINCIPAL:
            return getBindPrincipal();
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__BIND_PASSWORD:
            return getBindPassword();
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__LIMIT_VALUE:
            return getLimitValue();
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__ENCRYPTION_METHOD_NAME:
            return getEncryptionMethodName();
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__VALUE:
            return getValue();
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__SAVE_PASSWORD:
            return isSavePassword();
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__ALIASES:
            return getAliases();
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__REFERRALS:
            return getReferrals();
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__COUNT_LIMIT:
            return getCountLimit();
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__TIME_OUT_LIMIT:
            return getTimeOutLimit();
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__BASE_DNS:
            return getBaseDNs();
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__GET_BASE_DNS_FROM_ROOT:
            return isGetBaseDNsFromRoot();
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__RETURN_ATTRIBUTES:
            return getReturnAttributes();
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__SELECTED_DN:
            return getSelectedDN();
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
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__HOST:
            setHost((String) newValue);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__PORT:
            setPort((String) newValue);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__PROTOCOL:
            setProtocol((String) newValue);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__FILTER:
            setFilter((String) newValue);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__SEPARATOR:
            setSeparator((String) newValue);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__USE_ADVANCED:
            setUseAdvanced((Boolean) newValue);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__STORE_PATH:
            setStorePath((String) newValue);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__USE_LIMIT:
            setUseLimit((Boolean) newValue);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__USE_AUTHEN:
            setUseAuthen((Boolean) newValue);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__BIND_PRINCIPAL:
            setBindPrincipal((String) newValue);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__BIND_PASSWORD:
            setBindPassword((String) newValue);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__LIMIT_VALUE:
            setLimitValue((Integer) newValue);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__ENCRYPTION_METHOD_NAME:
            setEncryptionMethodName((String) newValue);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__VALUE:
            getValue().clear();
            getValue().addAll((Collection<? extends String>) newValue);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__SAVE_PASSWORD:
            setSavePassword((Boolean) newValue);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__ALIASES:
            setAliases((String) newValue);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__REFERRALS:
            setReferrals((String) newValue);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__COUNT_LIMIT:
            setCountLimit((String) newValue);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__TIME_OUT_LIMIT:
            setTimeOutLimit((String) newValue);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__BASE_DNS:
            getBaseDNs().clear();
            getBaseDNs().addAll((Collection<? extends String>) newValue);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__GET_BASE_DNS_FROM_ROOT:
            setGetBaseDNsFromRoot((Boolean) newValue);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__RETURN_ATTRIBUTES:
            getReturnAttributes().clear();
            getReturnAttributes().addAll((Collection<? extends String>) newValue);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__SELECTED_DN:
            setSelectedDN((String) newValue);
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
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__HOST:
            setHost(HOST_EDEFAULT);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__PORT:
            setPort(PORT_EDEFAULT);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__PROTOCOL:
            setProtocol(PROTOCOL_EDEFAULT);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__FILTER:
            setFilter(FILTER_EDEFAULT);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__SEPARATOR:
            setSeparator(SEPARATOR_EDEFAULT);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__USE_ADVANCED:
            setUseAdvanced(USE_ADVANCED_EDEFAULT);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__STORE_PATH:
            setStorePath(STORE_PATH_EDEFAULT);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__USE_LIMIT:
            setUseLimit(USE_LIMIT_EDEFAULT);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__USE_AUTHEN:
            setUseAuthen(USE_AUTHEN_EDEFAULT);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__BIND_PRINCIPAL:
            setBindPrincipal(BIND_PRINCIPAL_EDEFAULT);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__BIND_PASSWORD:
            setBindPassword(BIND_PASSWORD_EDEFAULT);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__LIMIT_VALUE:
            setLimitValue(LIMIT_VALUE_EDEFAULT);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__ENCRYPTION_METHOD_NAME:
            setEncryptionMethodName(ENCRYPTION_METHOD_NAME_EDEFAULT);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__VALUE:
            getValue().clear();
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__SAVE_PASSWORD:
            setSavePassword(SAVE_PASSWORD_EDEFAULT);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__ALIASES:
            setAliases(ALIASES_EDEFAULT);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__REFERRALS:
            setReferrals(REFERRALS_EDEFAULT);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__COUNT_LIMIT:
            setCountLimit(COUNT_LIMIT_EDEFAULT);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__TIME_OUT_LIMIT:
            setTimeOutLimit(TIME_OUT_LIMIT_EDEFAULT);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__BASE_DNS:
            getBaseDNs().clear();
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__GET_BASE_DNS_FROM_ROOT:
            setGetBaseDNsFromRoot(GET_BASE_DNS_FROM_ROOT_EDEFAULT);
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__RETURN_ATTRIBUTES:
            getReturnAttributes().clear();
            return;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__SELECTED_DN:
            setSelectedDN(SELECTED_DN_EDEFAULT);
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
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__HOST:
            return HOST_EDEFAULT == null ? host != null : !HOST_EDEFAULT.equals(host);
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__PORT:
            return PORT_EDEFAULT == null ? port != null : !PORT_EDEFAULT.equals(port);
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__PROTOCOL:
            return PROTOCOL_EDEFAULT == null ? protocol != null : !PROTOCOL_EDEFAULT.equals(protocol);
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__FILTER:
            return FILTER_EDEFAULT == null ? filter != null : !FILTER_EDEFAULT.equals(filter);
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__SEPARATOR:
            return SEPARATOR_EDEFAULT == null ? separator != null : !SEPARATOR_EDEFAULT.equals(separator);
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__USE_ADVANCED:
            return useAdvanced != USE_ADVANCED_EDEFAULT;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__STORE_PATH:
            return STORE_PATH_EDEFAULT == null ? storePath != null : !STORE_PATH_EDEFAULT.equals(storePath);
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__USE_LIMIT:
            return useLimit != USE_LIMIT_EDEFAULT;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__USE_AUTHEN:
            return useAuthen != USE_AUTHEN_EDEFAULT;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__BIND_PRINCIPAL:
            return BIND_PRINCIPAL_EDEFAULT == null ? bindPrincipal != null : !BIND_PRINCIPAL_EDEFAULT.equals(bindPrincipal);
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__BIND_PASSWORD:
            return BIND_PASSWORD_EDEFAULT == null ? bindPassword != null : !BIND_PASSWORD_EDEFAULT.equals(bindPassword);
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__LIMIT_VALUE:
            return limitValue != LIMIT_VALUE_EDEFAULT;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__ENCRYPTION_METHOD_NAME:
            return ENCRYPTION_METHOD_NAME_EDEFAULT == null ? encryptionMethodName != null : !ENCRYPTION_METHOD_NAME_EDEFAULT
                    .equals(encryptionMethodName);
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__VALUE:
            return value != null && !value.isEmpty();
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__SAVE_PASSWORD:
            return savePassword != SAVE_PASSWORD_EDEFAULT;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__ALIASES:
            return ALIASES_EDEFAULT == null ? aliases != null : !ALIASES_EDEFAULT.equals(aliases);
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__REFERRALS:
            return REFERRALS_EDEFAULT == null ? referrals != null : !REFERRALS_EDEFAULT.equals(referrals);
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__COUNT_LIMIT:
            return COUNT_LIMIT_EDEFAULT == null ? countLimit != null : !COUNT_LIMIT_EDEFAULT.equals(countLimit);
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__TIME_OUT_LIMIT:
            return TIME_OUT_LIMIT_EDEFAULT == null ? timeOutLimit != null : !TIME_OUT_LIMIT_EDEFAULT.equals(timeOutLimit);
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__BASE_DNS:
            return baseDNs != null && !baseDNs.isEmpty();
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__GET_BASE_DNS_FROM_ROOT:
            return getBaseDNsFromRoot != GET_BASE_DNS_FROM_ROOT_EDEFAULT;
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__RETURN_ATTRIBUTES:
            return returnAttributes != null && !returnAttributes.isEmpty();
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION__SELECTED_DN:
            return SELECTED_DN_EDEFAULT == null ? selectedDN != null : !SELECTED_DN_EDEFAULT.equals(selectedDN);
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
        result.append(" (Host: ");
        result.append(host);
        result.append(", Port: ");
        result.append(port);
        result.append(", Protocol: ");
        result.append(protocol);
        result.append(", Filter: ");
        result.append(filter);
        result.append(", Separator: ");
        result.append(separator);
        result.append(", UseAdvanced: ");
        result.append(useAdvanced);
        result.append(", StorePath: ");
        result.append(storePath);
        result.append(", UseLimit: ");
        result.append(useLimit);
        result.append(", UseAuthen: ");
        result.append(useAuthen);
        result.append(", BindPrincipal: ");
        result.append(bindPrincipal);
        result.append(", BindPassword: ");
        result.append(bindPassword);
        result.append(", LimitValue: ");
        result.append(limitValue);
        result.append(", EncryptionMethodName: ");
        result.append(encryptionMethodName);
        result.append(", Value: ");
        result.append(value);
        result.append(", SavePassword: ");
        result.append(savePassword);
        result.append(", Aliases: ");
        result.append(aliases);
        result.append(", Referrals: ");
        result.append(referrals);
        result.append(", CountLimit: ");
        result.append(countLimit);
        result.append(", TimeOutLimit: ");
        result.append(timeOutLimit);
        result.append(", BaseDNs: ");
        result.append(baseDNs);
        result.append(", GetBaseDNsFromRoot: ");
        result.append(getBaseDNsFromRoot);
        result.append(", ReturnAttributes: ");
        result.append(returnAttributes);
        result.append(", SelectedDN: ");
        result.append(selectedDN);
        result.append(')');
        return result.toString();
    }

} //LDAPSchemaConnectionImpl
