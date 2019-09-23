/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Condition Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.ConditionType#getInputColumn <em>Input Column</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.ConditionType#getFunction <em>Function</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.ConditionType#getOperator <em>Operator</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.ConditionType#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getConditionType()
 * @model
 * @generated
 */
public interface ConditionType extends EObject {

    /**
     * Returns the value of the '<em><b>Input Column</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Input Column</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Input Column</em>' attribute.
     * @see #setInputColumn(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getConditionType_InputColumn()
     * @model required="true"
     * @generated
     */
    String getInputColumn();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.ConditionType#getInputColumn <em>Input Column</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Input Column</em>' attribute.
     * @see #getInputColumn()
     * @generated
     */
    void setInputColumn(String value);

    /**
     * Returns the value of the '<em><b>Function</b></em>' attribute.
     * The literals are from the enumeration {@link org.talend.core.model.metadata.builder.connection.Function}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Function</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Function</em>' attribute.
     * @see org.talend.core.model.metadata.builder.connection.Function
     * @see #setFunction(Function)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getConditionType_Function()
     * @model required="true"
     * @generated
     */
    Function getFunction();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.ConditionType#getFunction <em>Function</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Function</em>' attribute.
     * @see org.talend.core.model.metadata.builder.connection.Function
     * @see #getFunction()
     * @generated
     */
    void setFunction(Function value);

    /**
     * Returns the value of the '<em><b>Operator</b></em>' attribute.
     * The literals are from the enumeration {@link org.talend.core.model.metadata.builder.connection.Operator}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Operator</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Operator</em>' attribute.
     * @see org.talend.core.model.metadata.builder.connection.Operator
     * @see #setOperator(Operator)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getConditionType_Operator()
     * @model required="true"
     * @generated
     */
    Operator getOperator();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.ConditionType#getOperator <em>Operator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Operator</em>' attribute.
     * @see org.talend.core.model.metadata.builder.connection.Operator
     * @see #getOperator()
     * @generated
     */
    void setOperator(Operator value);

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
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getConditionType_Value()
     * @model
     * @generated
     */
    String getValue();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.ConditionType#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' attribute.
     * @see #getValue()
     * @generated
     */
    void setValue(String value);

} // ConditionType
