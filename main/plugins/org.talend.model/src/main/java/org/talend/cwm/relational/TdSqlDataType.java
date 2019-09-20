/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.cwm.relational;

import orgomg.cwm.resource.relational.SQLSimpleType;

import orgomg.cwm.resource.relational.enumerations.NullableType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Td Sql Data Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * defines the DB and java types and attributes of the column
 * the Name attribute is set to the JDBC TYPE_NAME value
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.talend.cwm.relational.TdSqlDataType#getJavaDataType <em>Java Data Type</em>}</li>
 *   <li>{@link org.talend.cwm.relational.TdSqlDataType#getNullable <em>Nullable</em>}</li>
 *   <li>{@link org.talend.cwm.relational.TdSqlDataType#isUnsignedAttribute <em>Unsigned Attribute</em>}</li>
 *   <li>{@link org.talend.cwm.relational.TdSqlDataType#isCaseSensitive <em>Case Sensitive</em>}</li>
 *   <li>{@link org.talend.cwm.relational.TdSqlDataType#isAutoIncrement <em>Auto Increment</em>}</li>
 *   <li>{@link org.talend.cwm.relational.TdSqlDataType#getLocalTypeName <em>Local Type Name</em>}</li>
 *   <li>{@link org.talend.cwm.relational.TdSqlDataType#getSearchable <em>Searchable</em>}</li>
 * </ul>
 *
 * @see org.talend.cwm.relational.RelationalPackage#getTdSqlDataType()
 * @model
 * @generated
 */
public interface TdSqlDataType extends SQLSimpleType {

    /**
     * Returns the value of the '<em><b>Java Data Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * SQL data type from java.sql.Types.
     * this may not be changed by the user
     * <!-- end-model-doc -->
     * @return the value of the '<em>Java Data Type</em>' attribute.
     * @see #setJavaDataType(int)
     * @see org.talend.cwm.relational.RelationalPackage#getTdSqlDataType_JavaDataType()
     * @model
     * @generated
     */
    int getJavaDataType();

    /**
     * Sets the value of the '{@link org.talend.cwm.relational.TdSqlDataType#getJavaDataType <em>Java Data Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Java Data Type</em>' attribute.
     * @see #getJavaDataType()
     * @generated
     */
    void setJavaDataType(int value);

    /**
     * Returns the value of the '<em><b>Nullable</b></em>' attribute.
     * The literals are from the enumeration {@link orgomg.cwm.resource.relational.enumerations.NullableType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Is this column nullable, or not or unknow.
     * The value in one of
     * java.sql.DatabaseMetaData.columnNoNulls 
     * java.sql.DatabaseMetaData.columnNullable 
     * java.sql.DatabaseMetaData.columnNullableUnknown 
     * 
     * This may not be changed by the user
     * <!-- end-model-doc -->
     * @return the value of the '<em>Nullable</em>' attribute.
     * @see orgomg.cwm.resource.relational.enumerations.NullableType
     * @see #setNullable(NullableType)
     * @see org.talend.cwm.relational.RelationalPackage#getTdSqlDataType_Nullable()
     * @model
     * @generated
     */
    NullableType getNullable();

    /**
     * Sets the value of the '{@link org.talend.cwm.relational.TdSqlDataType#getNullable <em>Nullable</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Nullable</em>' attribute.
     * @see orgomg.cwm.resource.relational.enumerations.NullableType
     * @see #getNullable()
     * @generated
     */
    void setNullable(NullableType value);

    /**
     * Returns the value of the '<em><b>Unsigned Attribute</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * is it unsigned?
     * <!-- end-model-doc -->
     * @return the value of the '<em>Unsigned Attribute</em>' attribute.
     * @see #setUnsignedAttribute(boolean)
     * @see org.talend.cwm.relational.RelationalPackage#getTdSqlDataType_UnsignedAttribute()
     * @model dataType="orgomg.cwm.objectmodel.core.Boolean"
     * @generated
     */
    boolean isUnsignedAttribute();

    /**
     * Sets the value of the '{@link org.talend.cwm.relational.TdSqlDataType#isUnsignedAttribute <em>Unsigned Attribute</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Unsigned Attribute</em>' attribute.
     * @see #isUnsignedAttribute()
     * @generated
     */
    void setUnsignedAttribute(boolean value);

    /**
     * Returns the value of the '<em><b>Case Sensitive</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Case Sensitive</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Case Sensitive</em>' attribute.
     * @see #setCaseSensitive(boolean)
     * @see org.talend.cwm.relational.RelationalPackage#getTdSqlDataType_CaseSensitive()
     * @model dataType="orgomg.cwm.objectmodel.core.Boolean"
     * @generated
     */
    boolean isCaseSensitive();

    /**
     * Sets the value of the '{@link org.talend.cwm.relational.TdSqlDataType#isCaseSensitive <em>Case Sensitive</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Case Sensitive</em>' attribute.
     * @see #isCaseSensitive()
     * @generated
     */
    void setCaseSensitive(boolean value);

    /**
     * Returns the value of the '<em><b>Auto Increment</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * can it be used for an auto-increment value?
     * <!-- end-model-doc -->
     * @return the value of the '<em>Auto Increment</em>' attribute.
     * @see #setAutoIncrement(boolean)
     * @see org.talend.cwm.relational.RelationalPackage#getTdSqlDataType_AutoIncrement()
     * @model dataType="orgomg.cwm.objectmodel.core.Boolean"
     * @generated
     */
    boolean isAutoIncrement();

    /**
     * Sets the value of the '{@link org.talend.cwm.relational.TdSqlDataType#isAutoIncrement <em>Auto Increment</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Auto Increment</em>' attribute.
     * @see #isAutoIncrement()
     * @generated
     */
    void setAutoIncrement(boolean value);

    /**
     * Returns the value of the '<em><b>Local Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * localized version of the type name (may be null)
     * <!-- end-model-doc -->
     * @return the value of the '<em>Local Type Name</em>' attribute.
     * @see #setLocalTypeName(String)
     * @see org.talend.cwm.relational.RelationalPackage#getTdSqlDataType_LocalTypeName()
     * @model dataType="orgomg.cwm.objectmodel.core.String"
     * @generated
     */
    String getLocalTypeName();

    /**
     * Sets the value of the '{@link org.talend.cwm.relational.TdSqlDataType#getLocalTypeName <em>Local Type Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Local Type Name</em>' attribute.
     * @see #getLocalTypeName()
     * @generated
     */
    void setLocalTypeName(String value);

    /**
     * Returns the value of the '<em><b>Searchable</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * can you use "WHERE" based on this type.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Searchable</em>' attribute.
     * @see #setSearchable(short)
     * @see org.talend.cwm.relational.RelationalPackage#getTdSqlDataType_Searchable()
     * @model
     * @generated
     */
    short getSearchable();

    /**
     * Sets the value of the '{@link org.talend.cwm.relational.TdSqlDataType#getSearchable <em>Searchable</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Searchable</em>' attribute.
     * @see #getSearchable()
     * @generated
     */
    void setSearchable(short value);

} // TdSqlDataType
