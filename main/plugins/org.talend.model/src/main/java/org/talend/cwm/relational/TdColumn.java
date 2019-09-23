/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.cwm.relational;

import org.talend.core.model.metadata.builder.connection.MetadataColumn;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Td Column</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * defines a DB related column
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.cwm.relational.TdColumn#getSqlDataType <em>Sql Data Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.cwm.relational.RelationalPackage#getTdColumn()
 * @model
 * @generated
 */
public interface TdColumn extends MetadataColumn {

    /**
     * Returns the value of the '<em><b>Sql Data Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Sql Data Type</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Sql Data Type</em>' containment reference.
     * @see #setSqlDataType(TdSqlDataType)
     * @see org.talend.cwm.relational.RelationalPackage#getTdColumn_SqlDataType()
     * @model containment="true" resolveProxies="true"
     * @generated
     */
    TdSqlDataType getSqlDataType();

    /**
     * Sets the value of the '{@link org.talend.cwm.relational.TdColumn#getSqlDataType <em>Sql Data Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Sql Data Type</em>' containment reference.
     * @see #getSqlDataType()
     * @generated
     */
    void setSqlDataType(TdSqlDataType value);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The type of the content of the column. This type is a meta-information either set by the user who knows what type of data is contained in the column, or infered from the data.
     * This is used for Datamining may be Nominal, Interval,....
     * <!-- end-model-doc -->
     * @model contentTypeDataType="orgomg.cwm.objectmodel.core.String"
     * @generated
     */
    void setContentType(String contentType);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The type of the content of the column. This type is a meta-information either set by the user who knows what type of data is contained in the column, or infered from the data.
     * <!-- end-model-doc -->
     * @model kind="operation" dataType="orgomg.cwm.objectmodel.core.String"
     * @generated
     */
    String getContentType();

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * @deprecated use getTdSqlDataType.javaDataType
     * SQL data type from java.sql.Types.
     * <!-- end-model-doc -->
     * @model kind="operation"
     * @generated
     */
    int getJavaType();

} // TdColumn
