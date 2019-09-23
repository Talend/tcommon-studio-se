/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MDM Connection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.MDMConnection#getUsername <em>Username</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.MDMConnection#getPassword <em>Password</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.MDMConnection#getPort <em>Port</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.MDMConnection#getServer <em>Server</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.MDMConnection#getUniverse <em>Universe</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.MDMConnection#getDatamodel <em>Datamodel</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.MDMConnection#getDatacluster <em>Datacluster</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.MDMConnection#getSchemas <em>Schemas</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.MDMConnection#getProtocol <em>Protocol</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.MDMConnection#getContext <em>Context</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.MDMConnection#getServerUrl <em>Server Url</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMDMConnection()
 * @model
 * @generated
 */
public interface MDMConnection extends Connection {

    /**
     * Returns the value of the '<em><b>Username</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Username</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Username</em>' attribute.
     * @see #setUsername(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMDMConnection_Username()
     * @model
     * @generated
     */
    String getUsername();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.MDMConnection#getUsername <em>Username</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Username</em>' attribute.
     * @see #getUsername()
     * @generated
     */
    void setUsername(String value);

    /**
     * Returns the value of the '<em><b>Password</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Password</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Password</em>' attribute.
     * @see #setPassword(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMDMConnection_Password()
     * @model
     * @generated
     */
    String getPassword();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.MDMConnection#getPassword <em>Password</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Password</em>' attribute.
     * @see #getPassword()
     * @generated
     */
    void setPassword(String value);

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
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMDMConnection_Port()
     * @model
     * @generated
     */
    String getPort();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.MDMConnection#getPort <em>Port</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Port</em>' attribute.
     * @see #getPort()
     * @generated
     */
    void setPort(String value);

    /**
     * Returns the value of the '<em><b>Server</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Server</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Server</em>' attribute.
     * @see #setServer(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMDMConnection_Server()
     * @model
     * @generated
     */
    String getServer();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.MDMConnection#getServer <em>Server</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Server</em>' attribute.
     * @see #getServer()
     * @generated
     */
    void setServer(String value);

    /**
     * Returns the value of the '<em><b>Universe</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Universe</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Universe</em>' attribute.
     * @see #setUniverse(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMDMConnection_Universe()
     * @model
     * @generated
     */
    String getUniverse();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.MDMConnection#getUniverse <em>Universe</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Universe</em>' attribute.
     * @see #getUniverse()
     * @generated
     */
    void setUniverse(String value);

    /**
     * Returns the value of the '<em><b>Datamodel</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Datamodel</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Datamodel</em>' attribute.
     * @see #setDatamodel(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMDMConnection_Datamodel()
     * @model
     * @generated
     */
    String getDatamodel();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.MDMConnection#getDatamodel <em>Datamodel</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Datamodel</em>' attribute.
     * @see #getDatamodel()
     * @generated
     */
    void setDatamodel(String value);

    /**
     * Returns the value of the '<em><b>Datacluster</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Datacluster</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Datacluster</em>' attribute.
     * @see #setDatacluster(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMDMConnection_Datacluster()
     * @model
     * @generated
     */
    String getDatacluster();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.MDMConnection#getDatacluster <em>Datacluster</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Datacluster</em>' attribute.
     * @see #getDatacluster()
     * @generated
     */
    void setDatacluster(String value);

    /**
     * Returns the value of the '<em><b>Schemas</b></em>' containment reference list.
     * The list contents are of type {@link org.talend.core.model.metadata.builder.connection.Concept}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Schemas</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Schemas</em>' containment reference list.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMDMConnection_Schemas()
     * @model containment="true" resolveProxies="true"
     * @generated
     */
    EList<Concept> getSchemas();

    /**
     * Returns the value of the '<em><b>Protocol</b></em>' attribute.
     * The default value is <code>"http"</code>.
     * The literals are from the enumeration {@link org.talend.core.model.metadata.builder.connection.MDMConnectionProtocol}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * protocol used for connecting to MDM server, initial protocol is HTTP
     * <!-- end-model-doc -->
     * @return the value of the '<em>Protocol</em>' attribute.
     * @see org.talend.core.model.metadata.builder.connection.MDMConnectionProtocol
     * @see #setProtocol(MDMConnectionProtocol)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMDMConnection_Protocol()
     * @model default="http" required="true"
     * @generated
     */
    MDMConnectionProtocol getProtocol();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.MDMConnection#getProtocol <em>Protocol</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Protocol</em>' attribute.
     * @see org.talend.core.model.metadata.builder.connection.MDMConnectionProtocol
     * @see #getProtocol()
     * @generated
     */
    void setProtocol(MDMConnectionProtocol value);

    /**
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Connection String</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * return the connection string to connect to the MDM server,
     * it is a concatenation of protocol, server, port and context.
     * the connection string returned may not be a valid URL if some of the concatenated elements are not properly set.
     * No checking is done.
     * <!-- end-model-doc -->
     * @model kind="operation" dataType="orgomg.cwm.objectmodel.core.String"
     * @generated
     */
    String getConnectionString();

    /**
     * Returns the value of the '<em><b>Context</b></em>' attribute.
     * The default value is <code>"talend/TalendPort"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * part of the url for connecting to the server, 
     * the last part that defined the MDM web app context
     * <!-- end-model-doc -->
     * @return the value of the '<em>Context</em>' attribute.
     * @see #setContext(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMDMConnection_Context()
     * @model default="talend/TalendPort" dataType="orgomg.cwm.objectmodel.core.String" required="true"
     * @generated
     */
    String getContext();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.MDMConnection#getContext <em>Context</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Context</em>' attribute.
     * @see #getContext()
     * @generated
     */
    void setContext(String value);

    /**
     * Returns the value of the '<em><b>Server Url</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Server Url</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Server Url</em>' attribute.
     * @see #setServerUrl(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMDMConnection_ServerUrl()
     * @model required="true"
     * @generated
     */
    String getServerUrl();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.MDMConnection#getServerUrl <em>Server Url</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Server Url</em>' attribute.
     * @see #getServerUrl()
     * @generated
     */
    void setServerUrl(String value);

} // MDMConnection
