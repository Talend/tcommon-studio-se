/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Salesforce Module Unit</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SalesforceModuleUnit#getMetadataTable <em>Metadata Table</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SalesforceModuleUnit#getConnection <em>Connection</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SalesforceModuleUnit#getTables <em>Tables</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SalesforceModuleUnit#getModuleName <em>Module Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSalesforceModuleUnit()
 * @model
 * @generated
 */
public interface SalesforceModuleUnit extends AbstractMetadataObject {

    /**
     * Returns the value of the '<em><b>Metadata Table</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Metadata Table</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Metadata Table</em>' containment reference.
     * @see #setMetadataTable(MetadataTable)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSalesforceModuleUnit_MetadataTable()
     * @model containment="true" resolveProxies="true"
     * @generated
     */
    MetadataTable getMetadataTable();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SalesforceModuleUnit#getMetadataTable <em>Metadata Table</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Metadata Table</em>' containment reference.
     * @see #getMetadataTable()
     * @generated
     */
    void setMetadataTable(MetadataTable value);

    /**
     * Returns the value of the '<em><b>Connection</b></em>' container reference.
     * It is bidirectional and its opposite is '{@link org.talend.core.model.metadata.builder.connection.SalesforceSchemaConnection#getModules <em>Modules</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Connection</em>' container reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Connection</em>' container reference.
     * @see #setConnection(SalesforceSchemaConnection)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSalesforceModuleUnit_Connection()
     * @see org.talend.core.model.metadata.builder.connection.SalesforceSchemaConnection#getModules
     * @model opposite="modules" transient="false"
     * @generated
     */
    SalesforceSchemaConnection getConnection();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SalesforceModuleUnit#getConnection <em>Connection</em>}' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Connection</em>' container reference.
     * @see #getConnection()
     * @generated
     */
    void setConnection(SalesforceSchemaConnection value);

    /**
     * Returns the value of the '<em><b>Tables</b></em>' containment reference list.
     * The list contents are of type {@link org.talend.core.model.metadata.builder.connection.MetadataTable}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Tables</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Tables</em>' containment reference list.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSalesforceModuleUnit_Tables()
     * @model containment="true" resolveProxies="true"
     * @generated
     */
    EList<MetadataTable> getTables();

    /**
     * Returns the value of the '<em><b>Module Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Module Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Module Name</em>' attribute.
     * @see #setModuleName(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSalesforceModuleUnit_ModuleName()
     * @model
     * @generated
     */
    String getModuleName();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SalesforceModuleUnit#getModuleName <em>Module Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Module Name</em>' attribute.
     * @see #getModuleName()
     * @generated
     */
    void setModuleName(String value);

} // SalesforceModuleUnit
