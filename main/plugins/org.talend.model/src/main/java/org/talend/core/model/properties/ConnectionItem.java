/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.properties;

import org.talend.core.model.metadata.builder.connection.Connection;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Connection Item</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.properties.ConnectionItem#getConnection <em>Connection</em>}</li>
 *   <li>{@link org.talend.core.model.properties.ConnectionItem#getTypeName <em>Type Name</em>}</li>
 * </ul>
 *
 * @see org.talend.core.model.properties.PropertiesPackage#getConnectionItem()
 * @model
 * @generated
 */
public interface ConnectionItem extends Item {

    /**
     * Returns the value of the '<em><b>Connection</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Connection</em>' reference isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Connection</em>' reference.
     * @see #setConnection(Connection)
     * @see org.talend.core.model.properties.PropertiesPackage#getConnectionItem_Connection()
     * @model
     * @generated
     */
    Connection getConnection();

    /**
     * Sets the value of the '{@link org.talend.core.model.properties.ConnectionItem#getConnection <em>Connection</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Connection</em>' reference.
     * @see #getConnection()
     * @generated
     */
    void setConnection(Connection value);

    /**
     * Returns the value of the '<em><b>Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Type Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type Name</em>' attribute.
     * @see #setTypeName(String)
     * @see org.talend.core.model.properties.PropertiesPackage#getConnectionItem_TypeName()
     * @model required="true"
     * @generated
     */
    String getTypeName();

    /**
     * Sets the value of the '{@link org.talend.core.model.properties.ConnectionItem#getTypeName <em>Type Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type Name</em>' attribute.
     * @see #getTypeName()
     * @generated
     */
    void setTypeName(String value);

} // ConnectionItem
