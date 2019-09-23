/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

import org.talend.cwm.relational.TdTable;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Subscriber Table</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SubscriberTable#isSystem <em>System</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSubscriberTable()
 * @model
 * @generated
 */
public interface SubscriberTable extends TdTable {

    /**
     * Returns the value of the '<em><b>System</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>System</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>System</em>' attribute.
     * @see #setSystem(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSubscriberTable_System()
     * @model
     * @generated
     */
    boolean isSystem();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SubscriberTable#isSystem <em>System</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>System</em>' attribute.
     * @see #isSystem()
     * @generated
     */
    void setSystem(boolean value);

} // SubscriberTable
