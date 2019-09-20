/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>SAP Test Input Parameter Table</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPTestInputParameterTable#getFunctionUnit <em>Function Unit</em>}</li>
 * </ul>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPTestInputParameterTable()
 * @model
 * @generated
 */
public interface SAPTestInputParameterTable extends SAPFunctionParameterTable {

    /**
     * Returns the value of the '<em><b>Function Unit</b></em>' container reference.
     * It is bidirectional and its opposite is '{@link org.talend.core.model.metadata.builder.connection.SAPFunctionUnit#getTestInputParameterTable <em>Test Input Parameter Table</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Function Unit</em>' container reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Function Unit</em>' container reference.
     * @see #setFunctionUnit(SAPFunctionUnit)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPTestInputParameterTable_FunctionUnit()
     * @see org.talend.core.model.metadata.builder.connection.SAPFunctionUnit#getTestInputParameterTable
     * @model opposite="TestInputParameterTable" transient="false"
     * @generated
     */
    SAPFunctionUnit getFunctionUnit();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPTestInputParameterTable#getFunctionUnit <em>Function Unit</em>}' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Function Unit</em>' container reference.
     * @see #getFunctionUnit()
     * @generated
     */
    void setFunctionUnit(SAPFunctionUnit value);

} // SAPTestInputParameterTable
