/**
 */
package org.talend.core.model.metadata.builder.connection;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>SAP Function Parameter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameter#getName <em>Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameter#getType <em>Type</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameter#getDescription <em>Description</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameter#getLength <em>Length</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameter#isChanging <em>Changing</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameter#getTestValue <em>Test Value</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameter#getChildren <em>Children</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameter#isTableResideInTables <em>Table Reside In Tables</em>}</li>
 * </ul>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPFunctionParameter()
 * @model
 * @generated
 */
public interface SAPFunctionParameter extends EObject {

    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPFunctionParameter_Name()
     * @model
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameter#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

    /**
     * Returns the value of the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type</em>' attribute.
     * @see #setType(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPFunctionParameter_Type()
     * @model
     * @generated
     */
    String getType();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameter#getType <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type</em>' attribute.
     * @see #getType()
     * @generated
     */
    void setType(String value);

    /**
     * Returns the value of the '<em><b>Description</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Description</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Description</em>' attribute.
     * @see #setDescription(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPFunctionParameter_Description()
     * @model
     * @generated
     */
    String getDescription();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameter#getDescription <em>Description</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Description</em>' attribute.
     * @see #getDescription()
     * @generated
     */
    void setDescription(String value);

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
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPFunctionParameter_Length()
     * @model
     * @generated
     */
    String getLength();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameter#getLength <em>Length</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Length</em>' attribute.
     * @see #getLength()
     * @generated
     */
    void setLength(String value);

    /**
     * Returns the value of the '<em><b>Changing</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Changing</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Changing</em>' attribute.
     * @see #setChanging(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPFunctionParameter_Changing()
     * @model
     * @generated
     */
    boolean isChanging();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameter#isChanging <em>Changing</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Changing</em>' attribute.
     * @see #isChanging()
     * @generated
     */
    void setChanging(boolean value);

    /**
     * Returns the value of the '<em><b>Test Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Test Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Test Value</em>' attribute.
     * @see #setTestValue(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPFunctionParameter_TestValue()
     * @model
     * @generated
     */
    String getTestValue();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameter#getTestValue <em>Test Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Test Value</em>' attribute.
     * @see #getTestValue()
     * @generated
     */
    void setTestValue(String value);

    /**
     * Returns the value of the '<em><b>Children</b></em>' containment reference list.
     * The list contents are of type {@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameter}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Children</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Children</em>' containment reference list.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPFunctionParameter_Children()
     * @model containment="true" resolveProxies="true"
     * @generated
     */
    EList<SAPFunctionParameter> getChildren();

    /**
     * Returns the value of the '<em><b>Table Reside In Tables</b></em>' attribute.
     * The default value is <code>"true"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Table Reside In Tables</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Table Reside In Tables</em>' attribute.
     * @see #setTableResideInTables(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPFunctionParameter_TableResideInTables()
     * @model default="true"
     * @generated
     */
    boolean isTableResideInTables();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameter#isTableResideInTables <em>Table Reside In Tables</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Table Reside In Tables</em>' attribute.
     * @see #isTableResideInTables()
     * @generated
     */
    void setTableResideInTables(boolean value);

} // SAPFunctionParameter
