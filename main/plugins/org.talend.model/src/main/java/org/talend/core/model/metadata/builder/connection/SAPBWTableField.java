/**
 */
package org.talend.core.model.metadata.builder.connection;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>SAPBW Table Field</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPBWTableField#getLogicalName <em>Logical Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPBWTableField()
 * @model
 * @generated
 */
public interface SAPBWTableField extends SAPTableField {

    /**
     * Returns the value of the '<em><b>Logical Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Logical Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Logical Name</em>' attribute.
     * @see #setLogicalName(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPBWTableField_LogicalName()
     * @model
     * @generated
     */
    String getLogicalName();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPBWTableField#getLogicalName <em>Logical Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Logical Name</em>' attribute.
     * @see #getLogicalName()
     * @generated
     */
    void setLogicalName(String value);

} // SAPBWTableField
