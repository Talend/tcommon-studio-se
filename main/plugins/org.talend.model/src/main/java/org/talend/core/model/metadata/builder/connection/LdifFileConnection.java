/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Ldif File Connection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.LdifFileConnection#getValue <em>Value</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.LdifFileConnection#getFilePath <em>File Path</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.LdifFileConnection#getLimitEntry <em>Limit Entry</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.LdifFileConnection#isUseLimit <em>Use Limit</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.LdifFileConnection#getServer <em>Server</em>}</li>
 * </ul>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getLdifFileConnection()
 * @model
 * @generated
 */
public interface LdifFileConnection extends Connection {

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
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getLdifFileConnection_Value()
     * @model
     * @generated
     */
    EList<String> getValue();

    /**
     * Returns the value of the '<em><b>File Path</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>File Path</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>File Path</em>' attribute.
     * @see #setFilePath(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getLdifFileConnection_FilePath()
     * @model required="true"
     * @generated
     */
    String getFilePath();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.LdifFileConnection#getFilePath <em>File Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>File Path</em>' attribute.
     * @see #getFilePath()
     * @generated
     */
    void setFilePath(String value);

    /**
     * Returns the value of the '<em><b>Limit Entry</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Limit Entry</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Limit Entry</em>' attribute.
     * @see #setLimitEntry(int)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getLdifFileConnection_LimitEntry()
     * @model
     * @generated
     */
    int getLimitEntry();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.LdifFileConnection#getLimitEntry <em>Limit Entry</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Limit Entry</em>' attribute.
     * @see #getLimitEntry()
     * @generated
     */
    void setLimitEntry(int value);

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
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getLdifFileConnection_UseLimit()
     * @model
     * @generated
     */
    boolean isUseLimit();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.LdifFileConnection#isUseLimit <em>Use Limit</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Use Limit</em>' attribute.
     * @see #isUseLimit()
     * @generated
     */
    void setUseLimit(boolean value);

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
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getLdifFileConnection_Server()
     * @model required="true"
     * @generated
     */
    String getServer();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.LdifFileConnection#getServer <em>Server</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Server</em>' attribute.
     * @see #getServer()
     * @generated
     */
    void setServer(String value);

} // LdifFileConnection
