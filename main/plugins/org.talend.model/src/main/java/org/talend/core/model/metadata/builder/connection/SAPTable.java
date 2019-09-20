/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>SAP Table</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPTable#getTableSearchType <em>Table Search Type</em>}</li>
 * </ul>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPTable()
 * @model
 * @generated
 */
public interface SAPTable extends MetadataTable {

    /**
     * Returns the value of the '<em><b>Table Search Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Table Search Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Table Search Type</em>' attribute.
     * @see #setTableSearchType(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPTable_TableSearchType()
     * @model
     * @generated
     */
    String getTableSearchType();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPTable#getTableSearchType <em>Table Search Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Table Search Type</em>' attribute.
     * @see #getTableSearchType()
     * @generated
     */
    void setTableSearchType(String value);
} // SAPTable
