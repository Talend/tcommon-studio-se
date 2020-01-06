/**
 */
package org.talend.core.model.metadata.builder.connection;

import java.util.Date;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>SAPBW Table</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPBWTable#getModelType <em>Model Type</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPBWTable#isActive <em>Active</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPBWTable#getSourceSystemName <em>Source System Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPBWTable#getInfoAreaName <em>Info Area Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPBWTable#getInnerIOType <em>Inner IO Type</em>}</li>
 * </ul>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPBWTable()
 * @model
 * @generated
 */
public interface SAPBWTable extends SAPTable {

    /**
     * Returns the value of the '<em><b>Model Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Model Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Model Type</em>' attribute.
     * @see #setModelType(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPBWTable_ModelType()
     * @model
     * @generated
     */
    String getModelType();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPBWTable#getModelType <em>Model Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Model Type</em>' attribute.
     * @see #getModelType()
     * @generated
     */
    void setModelType(String value);

    /**
     * Returns the value of the '<em><b>Active</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Active</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Active</em>' attribute.
     * @see #setActive(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPBWTable_Active()
     * @model
     * @generated
     */
    boolean isActive();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPBWTable#isActive <em>Active</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Active</em>' attribute.
     * @see #isActive()
     * @generated
     */
    void setActive(boolean value);

    /**
     * Returns the value of the '<em><b>Source System Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Source System Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Source System Name</em>' attribute.
     * @see #setSourceSystemName(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPBWTable_SourceSystemName()
     * @model
     * @generated
     */
    String getSourceSystemName();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPBWTable#getSourceSystemName <em>Source System Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Source System Name</em>' attribute.
     * @see #getSourceSystemName()
     * @generated
     */
    void setSourceSystemName(String value);

    /**
     * Returns the value of the '<em><b>Info Area Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Info Area Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Info Area Name</em>' attribute.
     * @see #setInfoAreaName(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPBWTable_InfoAreaName()
     * @model
     * @generated
     */
    String getInfoAreaName();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPBWTable#getInfoAreaName <em>Info Area Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Info Area Name</em>' attribute.
     * @see #getInfoAreaName()
     * @generated
     */
    void setInfoAreaName(String value);

    /**
     * Returns the value of the '<em><b>Inner IO Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Inner IO Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Inner IO Type</em>' attribute.
     * @see #setInnerIOType(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPBWTable_InnerIOType()
     * @model
     * @generated
     */
    String getInnerIOType();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPBWTable#getInnerIOType <em>Inner IO Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Inner IO Type</em>' attribute.
     * @see #getInnerIOType()
     * @generated
     */
    void setInnerIOType(String value);

} // SAPBWTable
