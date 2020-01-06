/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>CDC Connection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * defining Change Data Capture for a given connection
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.CDCConnection#getConnection <em>Connection</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.CDCConnection#getCdcTypes <em>Cdc Types</em>}</li>
 * </ul>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getCDCConnection()
 * @model
 * @generated
 */
public interface CDCConnection extends EObject {

    /**
     * Returns the value of the '<em><b>Connection</b></em>' container reference.
     * It is bidirectional and its opposite is '{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getCdcConns <em>Cdc Conns</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Connection</em>' container reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * the connection this CDC relates to
     * <!-- end-model-doc -->
     * @return the value of the '<em>Connection</em>' container reference.
     * @see #setConnection(DatabaseConnection)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getCDCConnection_Connection()
     * @see org.talend.core.model.metadata.builder.connection.DatabaseConnection#getCdcConns
     * @model opposite="cdcConns" transient="false"
     * @generated
     */
    DatabaseConnection getConnection();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.CDCConnection#getConnection <em>Connection</em>}' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Connection</em>' container reference.
     * @see #getConnection()
     * @generated
     */
    void setConnection(DatabaseConnection value);

    /**
     * Returns the value of the '<em><b>Cdc Types</b></em>' containment reference list.
     * The list contents are of type {@link org.talend.core.model.metadata.builder.connection.CDCType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Cdc Types</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Cdc Types</em>' containment reference list.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getCDCConnection_CdcTypes()
     * @model containment="true" resolveProxies="true"
     * @generated
     */
    EList<CDCType> getCdcTypes();

} // CDCConnection
