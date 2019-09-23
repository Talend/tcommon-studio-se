/**
 */
package org.talend.core.model.metadata.builder.connection;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>SAP Function Param Data</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParamData#getInputRoot <em>Input Root</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParamData#getOutputRoot <em>Output Root</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPFunctionParamData()
 * @model
 * @generated
 */
public interface SAPFunctionParamData extends EObject {

    /**
     * Returns the value of the '<em><b>Input Root</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Input Root</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Input Root</em>' containment reference.
     * @see #setInputRoot(SAPFunctionParameter)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPFunctionParamData_InputRoot()
     * @model containment="true" resolveProxies="true"
     * @generated
     */
    SAPFunctionParameter getInputRoot();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParamData#getInputRoot <em>Input Root</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Input Root</em>' containment reference.
     * @see #getInputRoot()
     * @generated
     */
    void setInputRoot(SAPFunctionParameter value);

    /**
     * Returns the value of the '<em><b>Output Root</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Output Root</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Output Root</em>' containment reference.
     * @see #setOutputRoot(SAPFunctionParameter)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPFunctionParamData_OutputRoot()
     * @model containment="true" resolveProxies="true"
     * @generated
     */
    SAPFunctionParameter getOutputRoot();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParamData#getOutputRoot <em>Output Root</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Output Root</em>' containment reference.
     * @see #getOutputRoot()
     * @generated
     */
    void setOutputRoot(SAPFunctionParameter value);

} // SAPFunctionParamData
