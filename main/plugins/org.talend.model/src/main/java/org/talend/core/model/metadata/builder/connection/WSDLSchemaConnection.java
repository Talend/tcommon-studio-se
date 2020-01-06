/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>WSDL Schema Connection</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#getWSDL <em>WSDL</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#isNeedAuth <em>Need Auth</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#getMethodName <em>Method Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#getParameters <em>Parameters</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#getUserName <em>User Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#getPassword <em>Password</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#isUseProxy <em>Use Proxy</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#getProxyHost <em>Proxy Host</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#getProxyPort <em>Proxy Port</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#getProxyUser <em>Proxy User</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#getProxyPassword <em>Proxy Password</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#getValue <em>Value</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#getEndpointURI <em>Endpoint URI</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#getEncoding <em>Encoding</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#getTimeOut <em>Time Out</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#isIsInputModel <em>Is Input Model</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#getServerNameSpace <em>Server Name Space</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#getServerName <em>Server Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#getPortNameSpace <em>Port Name Space</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#getPortName <em>Port Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#getParameterValue <em>Parameter Value</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#getOutputParameter <em>Output Parameter</em>}</li>
 * </ul>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getWSDLSchemaConnection()
 * @model
 * @generated
 */
public interface WSDLSchemaConnection extends Connection {

    /**
     * Returns the value of the '<em><b>WSDL</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>WSDL</em>' attribute isn't clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>WSDL</em>' attribute.
     * @see #setWSDL(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getWSDLSchemaConnection_WSDL()
     * @model
     * @generated
     */
    String getWSDL();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#getWSDL <em>WSDL</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>WSDL</em>' attribute.
     * @see #getWSDL()
     * @generated
     */
    void setWSDL(String value);

    /**
     * Returns the value of the '<em><b>Need Auth</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Need Auth</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Need Auth</em>' attribute.
     * @see #setNeedAuth(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getWSDLSchemaConnection_NeedAuth()
     * @model
     * @generated
     */
    boolean isNeedAuth();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#isNeedAuth <em>Need Auth</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Need Auth</em>' attribute.
     * @see #isNeedAuth()
     * @generated
     */
    void setNeedAuth(boolean value);

    /**
     * Returns the value of the '<em><b>Method Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Method Name</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Method Name</em>' attribute.
     * @see #setMethodName(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getWSDLSchemaConnection_MethodName()
     * @model
     * @generated
     */
    String getMethodName();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#getMethodName <em>Method Name</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Method Name</em>' attribute.
     * @see #getMethodName()
     * @generated
     */
    void setMethodName(String value);

    /**
     * Returns the value of the '<em><b>Parameters</b></em>' attribute. The default value is <code>""</code>. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Parameters</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Parameters</em>' attribute.
     * @see #setParameters(ArrayList)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getWSDLSchemaConnection_Parameters()
     * @model default="" dataType="org.talend.core.model.metadata.builder.connection.List" required="true"
     * @generated
     */
    ArrayList getParameters();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#getParameters <em>Parameters</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Parameters</em>' attribute.
     * @see #getParameters()
     * @generated
     */
    void setParameters(ArrayList value);

    /**
     * Returns the value of the '<em><b>User Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>User Name</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>User Name</em>' attribute.
     * @see #setUserName(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getWSDLSchemaConnection_UserName()
     * @model
     * @generated
     */
    String getUserName();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#getUserName <em>User Name</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>User Name</em>' attribute.
     * @see #getUserName()
     * @generated
     */
    void setUserName(String value);

    /**
     * Returns the value of the '<em><b>Password</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Password</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Password</em>' attribute.
     * @see #setPassword(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getWSDLSchemaConnection_Password()
     * @model
     * @generated
     */
    String getPassword();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#getPassword <em>Password</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Password</em>' attribute.
     * @see #getPassword()
     * @generated
     */
    void setPassword(String value);

    /**
     * Returns the value of the '<em><b>Use Proxy</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Use Proxy</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Use Proxy</em>' attribute.
     * @see #setUseProxy(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getWSDLSchemaConnection_UseProxy()
     * @model
     * @generated
     */
    boolean isUseProxy();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#isUseProxy <em>Use Proxy</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Use Proxy</em>' attribute.
     * @see #isUseProxy()
     * @generated
     */
    void setUseProxy(boolean value);

    /**
     * Returns the value of the '<em><b>Proxy Host</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Proxy Host</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Proxy Host</em>' attribute.
     * @see #setProxyHost(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getWSDLSchemaConnection_ProxyHost()
     * @model
     * @generated
     */
    String getProxyHost();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#getProxyHost <em>Proxy Host</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Proxy Host</em>' attribute.
     * @see #getProxyHost()
     * @generated
     */
    void setProxyHost(String value);

    /**
     * Returns the value of the '<em><b>Proxy Port</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Proxy Port</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Proxy Port</em>' attribute.
     * @see #setProxyPort(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getWSDLSchemaConnection_ProxyPort()
     * @model
     * @generated
     */
    String getProxyPort();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#getProxyPort <em>Proxy Port</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Proxy Port</em>' attribute.
     * @see #getProxyPort()
     * @generated
     */
    void setProxyPort(String value);

    /**
     * Returns the value of the '<em><b>Proxy User</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Proxy User</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Proxy User</em>' attribute.
     * @see #setProxyUser(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getWSDLSchemaConnection_ProxyUser()
     * @model
     * @generated
     */
    String getProxyUser();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#getProxyUser <em>Proxy User</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Proxy User</em>' attribute.
     * @see #getProxyUser()
     * @generated
     */
    void setProxyUser(String value);

    /**
     * Returns the value of the '<em><b>Proxy Password</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Proxy Password</em>' attribute isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Proxy Password</em>' attribute.
     * @see #setProxyPassword(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getWSDLSchemaConnection_ProxyPassword()
     * @model
     * @generated
     */
    String getProxyPassword();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#getProxyPassword <em>Proxy Password</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Proxy Password</em>' attribute.
     * @see #getProxyPassword()
     * @generated
     */
    void setProxyPassword(String value);

    /**
     * Returns the value of the '<em><b>Value</b></em>' attribute list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' attribute list isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' attribute list.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getWSDLSchemaConnection_Value()
     * @model
     * @generated
     */
    EList<String> getValue();

    /**
     * Returns the value of the '<em><b>Endpoint URI</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Endpoint URI</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Endpoint URI</em>' attribute.
     * @see #setEndpointURI(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getWSDLSchemaConnection_EndpointURI()
     * @model
     * @generated
     */
    String getEndpointURI();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#getEndpointURI <em>Endpoint URI</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Endpoint URI</em>' attribute.
     * @see #getEndpointURI()
     * @generated
     */
    void setEndpointURI(String value);

    /**
     * Returns the value of the '<em><b>Encoding</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Encoding</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Encoding</em>' attribute.
     * @see #setEncoding(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getWSDLSchemaConnection_Encoding()
     * @model
     * @generated
     */
    String getEncoding();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#getEncoding <em>Encoding</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Encoding</em>' attribute.
     * @see #getEncoding()
     * @generated
     */
    void setEncoding(String value);

    /**
     * Returns the value of the '<em><b>Time Out</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Time Out</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Time Out</em>' attribute.
     * @see #setTimeOut(int)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getWSDLSchemaConnection_TimeOut()
     * @model
     * @generated
     */
    int getTimeOut();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#getTimeOut <em>Time Out</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Time Out</em>' attribute.
     * @see #getTimeOut()
     * @generated
     */
    void setTimeOut(int value);

    /**
     * Returns the value of the '<em><b>Is Input Model</b></em>' attribute.
     * The default value is <code>"true"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Is Input Model</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Is Input Model</em>' attribute.
     * @see #setIsInputModel(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getWSDLSchemaConnection_IsInputModel()
     * @model default="true"
     * @generated
     */
    boolean isIsInputModel();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection#isIsInputModel <em>Is Input Model</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Is Input Model</em>' attribute.
     * @see #isIsInputModel()
     * @generated
     */
    void setIsInputModel(boolean value);

    Boolean isWSDLModel();

    void setWSDLModel(Boolean value);

    /**
     * DOC Administrator Comment method "setPortName".
     * 
     * @param portName
     */
    void setPortName(String portName);

    /**
     * Returns the value of the '<em><b>Parameter Value</b></em>' containment reference list.
     * The list contents are of type {@link org.talend.core.model.metadata.builder.connection.WSDLParameter}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Parameter Value</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Parameter Value</em>' containment reference list.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getWSDLSchemaConnection_ParameterValue()
     * @model containment="true" resolveProxies="true"
     * @generated
     */
    EList<WSDLParameter> getParameterValue();

    /**
     * Returns the value of the '<em><b>Output Parameter</b></em>' containment reference list.
     * The list contents are of type {@link org.talend.core.model.metadata.builder.connection.WSDLParameter}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Output Parameter</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Output Parameter</em>' containment reference list.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getWSDLSchemaConnection_OutputParameter()
     * @model containment="true" resolveProxies="true"
     * @generated
     */
    EList<WSDLParameter> getOutputParameter();

    String getPortName();

    /**
     * DOC Administrator Comment method "setServerNameSpace".
     * 
     * @param serverNameSpace
     */
    void setServerNameSpace(String serverNameSpace);

    String getServerNameSpace();

    /**
     * DOC Administrator Comment method "setServerName".
     * 
     * @param serverName
     */
    void setServerName(String serverName);

    String getServerName();

    /**
     * DOC Administrator Comment method "setPortNameSpace".
     * 
     * @param serverNameSpace
     */
    void setPortNameSpace(String serverNameSpace);

    String getPortNameSpace();
} // WSDLSchemaConnection
