/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Input SAP Function Parameter Table</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.InputSAPFunctionParameterTable#getFunctionUnit <em>Function Unit</em>}</li>
 * </ul>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getInputSAPFunctionParameterTable()
 * @model
 * @generated
 */
public interface InputSAPFunctionParameterTable extends SAPFunctionParameterTable {

    /**
     * Returns the value of the '<em><b>Function Unit</b></em>' container reference.
     * It is bidirectional and its opposite is '{@link org.talend.core.model.metadata.builder.connection.SAPFunctionUnit#getInputParameterTable <em>Input Parameter Table</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Function Unit</em>' container reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Function Unit</em>' container reference.
     * @see #setFunctionUnit(SAPFunctionUnit)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getInputSAPFunctionParameterTable_FunctionUnit()
     * @see org.talend.core.model.metadata.builder.connection.SAPFunctionUnit#getInputParameterTable
     * @model opposite="InputParameterTable" transient="false"
     * @generated
     */
    SAPFunctionUnit getFunctionUnit();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.InputSAPFunctionParameterTable#getFunctionUnit <em>Function Unit</em>}' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Function Unit</em>' container reference.
     * @see #getFunctionUnit()
     * @generated
     */
    void setFunctionUnit(SAPFunctionUnit value);

} // InputSAPFunctionParameterTable
