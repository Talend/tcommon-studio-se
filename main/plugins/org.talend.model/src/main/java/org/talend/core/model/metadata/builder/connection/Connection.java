/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

import orgomg.cwm.foundation.softwaredeployment.DataProvider;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Connection</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * base class tha represent a connection, may be to a database or a file or else
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.Connection#getVersion <em>Version</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.Connection#getQueries <em>Queries</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.Connection#isContextMode <em>Context Mode</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.Connection#getContextId <em>Context Id</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.Connection#getContextName <em>Context Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.Connection#getCompProperties <em>Comp Properties</em>}</li>
 * </ul>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getConnection()
 * @model
 * @generated
 */
public interface Connection extends AbstractMetadataObject, DataProvider {

    /**
     * Returns the value of the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Version</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Version</em>' attribute.
     * @see #setVersion(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getConnection_Version()
     * @model
     * @generated
     */
    String getVersion();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.Connection#getVersion <em>Version</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Version</em>' attribute.
     * @see #getVersion()
     * @generated
     */
    void setVersion(String value);

    /**
     * Returns the value of the '<em><b>Queries</b></em>' containment reference. It is bidirectional and its opposite is
     * '{@link org.talend.core.model.metadata.builder.connection.QueriesConnection#getConnection <em>Connection</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Queries</em>' containment reference list isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc --> <!-- begin-model-doc --> This defines the SQL queries related to this connection <!--
     * end-model-doc -->
     * 
     * @return the value of the '<em>Queries</em>' containment reference.
     * @see #setQueries(QueriesConnection)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getConnection_Queries()
     * @see org.talend.core.model.metadata.builder.connection.QueriesConnection#getConnection
     * @model opposite="connection" containment="true" resolveProxies="true"
     * @generated
     */
    QueriesConnection getQueries();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.Connection#getQueries <em>Queries</em>}' containment reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Queries</em>' containment reference.
     * @see #getQueries()
     * @generated
     */
    void setQueries(QueriesConnection value);

    /**
     * Returns the value of the '<em><b>Context Mode</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Context Mode</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc --> <!-- begin-model-doc --> whether this connection is defined using a context or is
     * standalone <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Context Mode</em>' attribute.
     * @see #setContextMode(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getConnection_ContextMode()
     * @model
     * @generated
     */
    boolean isContextMode();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.Connection#isContextMode <em>Context Mode</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Context Mode</em>' attribute.
     * @see #isContextMode()
     * @generated
     */
    void setContextMode(boolean value);

    /**
     * Returns the value of the '<em><b>Context Id</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Context Id</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc --> <!-- begin-model-doc --> Id of the context this connection is linked to, only used when
     * ContextMode attribute is true <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Context Id</em>' attribute.
     * @see #setContextId(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getConnection_ContextId()
     * @model
     * @generated
     */
    String getContextId();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.Connection#getContextId <em>Context Id</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Context Id</em>' attribute.
     * @see #getContextId()
     * @generated
     */
    void setContextId(String value);

    /**
     * Returns the value of the '<em><b>Context Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Context Name</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Context Name</em>' attribute.
     * @see #setContextName(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getConnection_ContextName()
     * @model
     * @generated
     */
    String getContextName();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.Connection#getContextName <em>Context Name</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Context Name</em>' attribute.
     * @see #getContextName()
     * @generated
     */
    void setContextName(String value);

    /**
     * Returns the value of the '<em><b>Comp Properties</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Comp Properties</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Comp Properties</em>' attribute.
     * @see #setCompProperties(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getConnection_CompProperties()
     * @model
     * @generated
     */
    String getCompProperties();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.Connection#getCompProperties <em>Comp Properties</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Comp Properties</em>' attribute.
     * @see #getCompProperties()
     * @generated
     */
    void setCompProperties(String value);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @model kind="operation" dataType="orgomg.cwm.objectmodel.core.String"
     * @generated
     */
    String getConnectionTypeName();

    /**
     * encrypt is true, will try to encrypt the value, else, if false, will try to decrypt the value,
     * 
     * @generated NOT
     */
    String getValue(String value, boolean encrypt);

} // Connection
