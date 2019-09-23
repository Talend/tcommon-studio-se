/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>SAP Function Parameter Column</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameterColumn#getParameterType <em>Parameter Type</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameterColumn#getStructureOrTableName <em>Structure Or Table Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameterColumn#getDataType <em>Data Type</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameterColumn#getLength <em>Length</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameterColumn#getValue <em>Value</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameterColumn#getParameterTable <em>Parameter Table</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPFunctionParameterColumn()
 * @model
 * @generated
 */
public interface SAPFunctionParameterColumn extends AbstractMetadataObject {

    /**
     * Returns the value of the '<em><b>Parameter Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Parameter Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Parameter Type</em>' attribute.
     * @see #setParameterType(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPFunctionParameterColumn_ParameterType()
     * @model
     * @generated
     */
    String getParameterType();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameterColumn#getParameterType <em>Parameter Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Parameter Type</em>' attribute.
     * @see #getParameterType()
     * @generated
     */
    void setParameterType(String value);

    /**
     * Returns the value of the '<em><b>Structure Or Table Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Structure Or Table Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Structure Or Table Name</em>' attribute.
     * @see #setStructureOrTableName(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPFunctionParameterColumn_StructureOrTableName()
     * @model
     * @generated
     */
    String getStructureOrTableName();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameterColumn#getStructureOrTableName <em>Structure Or Table Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Structure Or Table Name</em>' attribute.
     * @see #getStructureOrTableName()
     * @generated
     */
    void setStructureOrTableName(String value);

    /**
     * Returns the value of the '<em><b>Data Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Data Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Data Type</em>' attribute.
     * @see #setDataType(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPFunctionParameterColumn_DataType()
     * @model
     * @generated
     */
    String getDataType();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameterColumn#getDataType <em>Data Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Data Type</em>' attribute.
     * @see #getDataType()
     * @generated
     */
    void setDataType(String value);

    /**
     * Returns the value of the '<em><b>Length</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Length</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Length</em>' attribute.
     * @see #setLength(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPFunctionParameterColumn_Length()
     * @model
     * @generated
     */
    String getLength();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameterColumn#getLength <em>Length</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Length</em>' attribute.
     * @see #getLength()
     * @generated
     */
    void setLength(String value);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * @deprecated use ModelElementHelper.getFirstDescription().setBody()
     * <!-- end-model-doc -->
     * @model descriptionDataType="orgomg.cwm.objectmodel.core.String"
     * @generated
     */
    void setDescription(String description);

    /**
     * Returns the value of the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' attribute.
     * @see #setValue(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPFunctionParameterColumn_Value()
     * @model
     * @generated
     */
    String getValue();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameterColumn#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' attribute.
     * @see #getValue()
     * @generated
     */
    void setValue(String value);

    /**
     * Returns the value of the '<em><b>Parameter Table</b></em>' container reference.
     * It is bidirectional and its opposite is '{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameterTable#getColumns <em>Columns</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Parameter Table</em>' container reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Parameter Table</em>' container reference.
     * @see #setParameterTable(SAPFunctionParameterTable)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPFunctionParameterColumn_ParameterTable()
     * @see org.talend.core.model.metadata.builder.connection.SAPFunctionParameterTable#getColumns
     * @model opposite="columns" transient="false"
     * @generated
     */
    SAPFunctionParameterTable getParameterTable();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameterColumn#getParameterTable <em>Parameter Table</em>}' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Parameter Table</em>' container reference.
     * @see #getParameterTable()
     * @generated
     */
    void setParameterTable(SAPFunctionParameterTable value);

} // SAPFunctionParameterColumn
