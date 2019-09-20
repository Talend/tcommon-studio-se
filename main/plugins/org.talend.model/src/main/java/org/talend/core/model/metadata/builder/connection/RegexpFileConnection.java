/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Regexp File Connection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.RegexpFileConnection#getFieldSeparatorType <em>Field Separator Type</em>}</li>
 * </ul>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getRegexpFileConnection()
 * @model
 * @generated
 */
public interface RegexpFileConnection extends FileConnection {

    /**
     * Returns the value of the '<em><b>Field Separator Type</b></em>' attribute.
     * The literals are from the enumeration {@link org.talend.core.model.metadata.builder.connection.FieldSeparator}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Field Separator Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Field Separator Type</em>' attribute.
     * @see org.talend.core.model.metadata.builder.connection.FieldSeparator
     * @see #setFieldSeparatorType(FieldSeparator)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getRegexpFileConnection_FieldSeparatorType()
     * @model required="true"
     * @generated
     */
    FieldSeparator getFieldSeparatorType();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.RegexpFileConnection#getFieldSeparatorType <em>Field Separator Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Field Separator Type</em>' attribute.
     * @see org.talend.core.model.metadata.builder.connection.FieldSeparator
     * @see #getFieldSeparatorType()
     * @generated
     */
    void setFieldSeparatorType(FieldSeparator value);

} // RegexpFileConnection
