/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Delimited File Connection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.DelimitedFileConnection#getFieldSeparatorType <em>Field Separator Type</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.DelimitedFileConnection#isSplitRecord <em>Split Record</em>}</li>
 * </ul>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getDelimitedFileConnection()
 * @model
 * @generated
 */
public interface DelimitedFileConnection extends FileConnection {

    /**
     * Returns the value of the '<em><b>Field Separator Type</b></em>' attribute.
     * The default value is <code>"Semicolon"</code>.
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
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getDelimitedFileConnection_FieldSeparatorType()
     * @model default="Semicolon" required="true"
     * @generated
     */
    FieldSeparator getFieldSeparatorType();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.DelimitedFileConnection#getFieldSeparatorType <em>Field Separator Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Field Separator Type</em>' attribute.
     * @see org.talend.core.model.metadata.builder.connection.FieldSeparator
     * @see #getFieldSeparatorType()
     * @generated
     */
    void setFieldSeparatorType(FieldSeparator value);

    /**
     * Returns the value of the '<em><b>Split Record</b></em>' attribute.
     * The default value is <code>"false"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Split Record</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Split Record</em>' attribute.
     * @see #setSplitRecord(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getDelimitedFileConnection_SplitRecord()
     * @model default="false"
     * @generated
     */
    boolean isSplitRecord();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.DelimitedFileConnection#isSplitRecord <em>Split Record</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Split Record</em>' attribute.
     * @see #isSplitRecord()
     * @generated
     */
    void setSplitRecord(boolean value);

} // DelimitedFileConnection
