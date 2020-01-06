/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>LDAP Schema Connection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getHost <em>Host</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getPort <em>Port</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getProtocol <em>Protocol</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getFilter <em>Filter</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getSeparator <em>Separator</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#isUseAdvanced <em>Use Advanced</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getStorePath <em>Store Path</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#isUseLimit <em>Use Limit</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#isUseAuthen <em>Use Authen</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getBindPrincipal <em>Bind Principal</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getBindPassword <em>Bind Password</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getLimitValue <em>Limit Value</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getEncryptionMethodName <em>Encryption Method Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getValue <em>Value</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#isSavePassword <em>Save Password</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getAliases <em>Aliases</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getReferrals <em>Referrals</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getCountLimit <em>Count Limit</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getTimeOutLimit <em>Time Out Limit</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getBaseDNs <em>Base DNs</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#isGetBaseDNsFromRoot <em>Get Base DNs From Root</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getReturnAttributes <em>Return Attributes</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getSelectedDN <em>Selected DN</em>}</li>
 * </ul>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getLDAPSchemaConnection()
 * @model
 * @generated
 */
public interface LDAPSchemaConnection extends Connection {

    /**
     * Returns the value of the '<em><b>Host</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Host</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Host</em>' attribute.
     * @see #setHost(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getLDAPSchemaConnection_Host()
     * @model
     * @generated
     */
    String getHost();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getHost <em>Host</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Host</em>' attribute.
     * @see #getHost()
     * @generated
     */
    void setHost(String value);

    /**
     * Returns the value of the '<em><b>Port</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Port</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Port</em>' attribute.
     * @see #setPort(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getLDAPSchemaConnection_Port()
     * @model
     * @generated
     */
    String getPort();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getPort <em>Port</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Port</em>' attribute.
     * @see #getPort()
     * @generated
     */
    void setPort(String value);

    /**
     * Returns the value of the '<em><b>Protocol</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Protocol</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Protocol</em>' attribute.
     * @see #setProtocol(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getLDAPSchemaConnection_Protocol()
     * @model
     * @generated
     */
    String getProtocol();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getProtocol <em>Protocol</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Protocol</em>' attribute.
     * @see #getProtocol()
     * @generated
     */
    void setProtocol(String value);

    /**
     * Returns the value of the '<em><b>Filter</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Filter</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Filter</em>' attribute.
     * @see #setFilter(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getLDAPSchemaConnection_Filter()
     * @model
     * @generated
     */
    String getFilter();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getFilter <em>Filter</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Filter</em>' attribute.
     * @see #getFilter()
     * @generated
     */
    void setFilter(String value);

    /**
     * Returns the value of the '<em><b>Separator</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Separator</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Separator</em>' attribute.
     * @see #setSeparator(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getLDAPSchemaConnection_Separator()
     * @model
     * @generated
     */
    String getSeparator();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getSeparator <em>Separator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Separator</em>' attribute.
     * @see #getSeparator()
     * @generated
     */
    void setSeparator(String value);

    /**
     * Returns the value of the '<em><b>Use Advanced</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Use Advanced</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Use Advanced</em>' attribute.
     * @see #setUseAdvanced(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getLDAPSchemaConnection_UseAdvanced()
     * @model
     * @generated
     */
    boolean isUseAdvanced();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#isUseAdvanced <em>Use Advanced</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Use Advanced</em>' attribute.
     * @see #isUseAdvanced()
     * @generated
     */
    void setUseAdvanced(boolean value);

    /**
     * Returns the value of the '<em><b>Store Path</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Store Path</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Store Path</em>' attribute.
     * @see #setStorePath(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getLDAPSchemaConnection_StorePath()
     * @model
     * @generated
     */
    String getStorePath();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getStorePath <em>Store Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Store Path</em>' attribute.
     * @see #getStorePath()
     * @generated
     */
    void setStorePath(String value);

    /**
     * Returns the value of the '<em><b>Use Limit</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Use Limit</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Use Limit</em>' attribute.
     * @see #setUseLimit(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getLDAPSchemaConnection_UseLimit()
     * @model
     * @generated
     */
    boolean isUseLimit();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#isUseLimit <em>Use Limit</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Use Limit</em>' attribute.
     * @see #isUseLimit()
     * @generated
     */
    void setUseLimit(boolean value);

    /**
     * Returns the value of the '<em><b>Use Authen</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Use Authen</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Use Authen</em>' attribute.
     * @see #setUseAuthen(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getLDAPSchemaConnection_UseAuthen()
     * @model
     * @generated
     */
    boolean isUseAuthen();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#isUseAuthen <em>Use Authen</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Use Authen</em>' attribute.
     * @see #isUseAuthen()
     * @generated
     */
    void setUseAuthen(boolean value);

    /**
     * Returns the value of the '<em><b>Bind Principal</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Bind Principal</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Bind Principal</em>' attribute.
     * @see #setBindPrincipal(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getLDAPSchemaConnection_BindPrincipal()
     * @model
     * @generated
     */
    String getBindPrincipal();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getBindPrincipal <em>Bind Principal</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Bind Principal</em>' attribute.
     * @see #getBindPrincipal()
     * @generated
     */
    void setBindPrincipal(String value);

    /**
     * Returns the value of the '<em><b>Bind Password</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Bind Password</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Bind Password</em>' attribute.
     * @see #setBindPassword(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getLDAPSchemaConnection_BindPassword()
     * @model
     * @generated
     */
    String getBindPassword();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getBindPassword <em>Bind Password</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Bind Password</em>' attribute.
     * @see #getBindPassword()
     * @generated
     */
    void setBindPassword(String value);

    /**
     * Returns the value of the '<em><b>Limit Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Limit Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Limit Value</em>' attribute.
     * @see #setLimitValue(int)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getLDAPSchemaConnection_LimitValue()
     * @model
     * @generated
     */
    int getLimitValue();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getLimitValue <em>Limit Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Limit Value</em>' attribute.
     * @see #getLimitValue()
     * @generated
     */
    void setLimitValue(int value);

    /**
     * Returns the value of the '<em><b>Encryption Method Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Encryption Method Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Encryption Method Name</em>' attribute.
     * @see #setEncryptionMethodName(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getLDAPSchemaConnection_EncryptionMethodName()
     * @model
     * @generated
     */
    String getEncryptionMethodName();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getEncryptionMethodName <em>Encryption Method Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Encryption Method Name</em>' attribute.
     * @see #getEncryptionMethodName()
     * @generated
     */
    void setEncryptionMethodName(String value);

    /**
     * Returns the value of the '<em><b>Value</b></em>' attribute list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' attribute list.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getLDAPSchemaConnection_Value()
     * @model
     * @generated
     */
    EList<String> getValue();

    /**
     * Returns the value of the '<em><b>Save Password</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Save Password</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Save Password</em>' attribute.
     * @see #setSavePassword(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getLDAPSchemaConnection_SavePassword()
     * @model
     * @generated
     */
    boolean isSavePassword();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#isSavePassword <em>Save Password</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Save Password</em>' attribute.
     * @see #isSavePassword()
     * @generated
     */
    void setSavePassword(boolean value);

    /**
     * Returns the value of the '<em><b>Aliases</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Aliases</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Aliases</em>' attribute.
     * @see #setAliases(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getLDAPSchemaConnection_Aliases()
     * @model
     * @generated
     */
    String getAliases();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getAliases <em>Aliases</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Aliases</em>' attribute.
     * @see #getAliases()
     * @generated
     */
    void setAliases(String value);

    /**
     * Returns the value of the '<em><b>Referrals</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Referrals</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Referrals</em>' attribute.
     * @see #setReferrals(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getLDAPSchemaConnection_Referrals()
     * @model
     * @generated
     */
    String getReferrals();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getReferrals <em>Referrals</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Referrals</em>' attribute.
     * @see #getReferrals()
     * @generated
     */
    void setReferrals(String value);

    /**
     * Returns the value of the '<em><b>Count Limit</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Count Limit</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Count Limit</em>' attribute.
     * @see #setCountLimit(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getLDAPSchemaConnection_CountLimit()
     * @model
     * @generated
     */
    String getCountLimit();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getCountLimit <em>Count Limit</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Count Limit</em>' attribute.
     * @see #getCountLimit()
     * @generated
     */
    void setCountLimit(String value);

    /**
     * Returns the value of the '<em><b>Time Out Limit</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Time Out Limit</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Time Out Limit</em>' attribute.
     * @see #setTimeOutLimit(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getLDAPSchemaConnection_TimeOutLimit()
     * @model
     * @generated
     */
    String getTimeOutLimit();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getTimeOutLimit <em>Time Out Limit</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Time Out Limit</em>' attribute.
     * @see #getTimeOutLimit()
     * @generated
     */
    void setTimeOutLimit(String value);

    /**
     * Returns the value of the '<em><b>Base DNs</b></em>' attribute list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Base DNs</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Base DNs</em>' attribute list.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getLDAPSchemaConnection_BaseDNs()
     * @model
     * @generated
     */
    EList<String> getBaseDNs();

    /**
     * Returns the value of the '<em><b>Get Base DNs From Root</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Get Base DNs From Root</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Get Base DNs From Root</em>' attribute.
     * @see #setGetBaseDNsFromRoot(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getLDAPSchemaConnection_GetBaseDNsFromRoot()
     * @model
     * @generated
     */
    boolean isGetBaseDNsFromRoot();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#isGetBaseDNsFromRoot <em>Get Base DNs From Root</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Get Base DNs From Root</em>' attribute.
     * @see #isGetBaseDNsFromRoot()
     * @generated
     */
    void setGetBaseDNsFromRoot(boolean value);

    /**
     * Returns the value of the '<em><b>Return Attributes</b></em>' attribute list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Return Attributes</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Return Attributes</em>' attribute list.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getLDAPSchemaConnection_ReturnAttributes()
     * @model
     * @generated
     */
    EList<String> getReturnAttributes();

    /**
     * Returns the value of the '<em><b>Selected DN</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Selected DN</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Selected DN</em>' attribute.
     * @see #setSelectedDN(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getLDAPSchemaConnection_SelectedDN()
     * @model
     * @generated
     */
    String getSelectedDN();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection#getSelectedDN <em>Selected DN</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Selected DN</em>' attribute.
     * @see #getSelectedDN()
     * @generated
     */
    void setSelectedDN(String value);

} // LDAPSchemaConnection
