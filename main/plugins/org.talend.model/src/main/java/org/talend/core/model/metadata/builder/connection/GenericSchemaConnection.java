/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Generic Schema Connection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.GenericSchemaConnection#isMappingTypeUsed <em>Mapping Type Used</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.GenericSchemaConnection#getMappingTypeId <em>Mapping Type Id</em>}</li>
 * </ul>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getGenericSchemaConnection()
 * @model
 * @generated
 */
public interface GenericSchemaConnection extends Connection {

    /**
     * Returns the value of the '<em><b>Mapping Type Used</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Mapping Type Used</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Mapping Type Used</em>' attribute.
     * @see #setMappingTypeUsed(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getGenericSchemaConnection_MappingTypeUsed()
     * @model
     * @generated
     */
    boolean isMappingTypeUsed();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.GenericSchemaConnection#isMappingTypeUsed <em>Mapping Type Used</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Mapping Type Used</em>' attribute.
     * @see #isMappingTypeUsed()
     * @generated
     */
    void setMappingTypeUsed(boolean value);

    /**
     * Returns the value of the '<em><b>Mapping Type Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Mapping Type Id</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Mapping Type Id</em>' attribute.
     * @see #setMappingTypeId(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getGenericSchemaConnection_MappingTypeId()
     * @model
     * @generated
     */
    String getMappingTypeId();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.GenericSchemaConnection#getMappingTypeId <em>Mapping Type Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Mapping Type Id</em>' attribute.
     * @see #getMappingTypeId()
     * @generated
     */
    void setMappingTypeId(String value);
} // GenericSchemaConnection
