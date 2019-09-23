/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

import java.util.Map;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Validation Rules Connection</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.ValidationRulesConnection#isIsSelect <em>Is Select</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.ValidationRulesConnection#isIsInsert <em>Is Insert</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.ValidationRulesConnection#isIsUpdate <em>Is Update</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.ValidationRulesConnection#isIsDelete <em>Is Delete</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.ValidationRulesConnection#getType <em>Type</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.ValidationRulesConnection#getBaseSchema <em>Base Schema</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.ValidationRulesConnection#getBaseColumnNames <em>Base Column Names</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.ValidationRulesConnection#getRefSchema <em>Ref Schema</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.ValidationRulesConnection#getRefColumnNames <em>Ref Column Names</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.ValidationRulesConnection#getJavaCondition <em>Java Condition</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.ValidationRulesConnection#getSqlCondition <em>Sql Condition</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.ValidationRulesConnection#getLogicalOperator <em>Logical Operator</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.ValidationRulesConnection#getConditions <em>Conditions</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.ValidationRulesConnection#getInnerJoins <em>Inner Joins</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.ValidationRulesConnection#isIsDisallow <em>Is Disallow</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.ValidationRulesConnection#isIsRejectLink <em>Is Reject Link</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getValidationRulesConnection()
 * @model
 * @generated
 */
public interface ValidationRulesConnection extends Connection {

    /**
     * Returns the value of the '<em><b>Is Select</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Is Select</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Is Select</em>' attribute.
     * @see #setIsSelect(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getValidationRulesConnection_IsSelect()
     * @model
     * @generated
     */
    boolean isIsSelect();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.ValidationRulesConnection#isIsSelect <em>Is Select</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Is Select</em>' attribute.
     * @see #isIsSelect()
     * @generated
     */
    void setIsSelect(boolean value);

    /**
     * Returns the value of the '<em><b>Is Insert</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Is Insert</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Is Insert</em>' attribute.
     * @see #setIsInsert(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getValidationRulesConnection_IsInsert()
     * @model
     * @generated
     */
    boolean isIsInsert();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.ValidationRulesConnection#isIsInsert <em>Is Insert</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Is Insert</em>' attribute.
     * @see #isIsInsert()
     * @generated
     */
    void setIsInsert(boolean value);

    /**
     * Returns the value of the '<em><b>Is Update</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Is Update</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Is Update</em>' attribute.
     * @see #setIsUpdate(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getValidationRulesConnection_IsUpdate()
     * @model
     * @generated
     */
    boolean isIsUpdate();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.ValidationRulesConnection#isIsUpdate <em>Is Update</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Is Update</em>' attribute.
     * @see #isIsUpdate()
     * @generated
     */
    void setIsUpdate(boolean value);

    /**
     * Returns the value of the '<em><b>Is Delete</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Is Delete</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Is Delete</em>' attribute.
     * @see #setIsDelete(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getValidationRulesConnection_IsDelete()
     * @model
     * @generated
     */
    boolean isIsDelete();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.ValidationRulesConnection#isIsDelete <em>Is Delete</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Is Delete</em>' attribute.
     * @see #isIsDelete()
     * @generated
     */
    void setIsDelete(boolean value);

    /**
     * Returns the value of the '<em><b>Type</b></em>' attribute.
     * The literals are from the enumeration {@link org.talend.core.model.metadata.builder.connection.RuleType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Type</em>' attribute isn't clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type</em>' attribute.
     * @see org.talend.core.model.metadata.builder.connection.RuleType
     * @see #setType(RuleType)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getValidationRulesConnection_Type()
     * @model
     * @generated
     */
    RuleType getType();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.ValidationRulesConnection#getType <em>Type</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Type</em>' attribute.
     * @see org.talend.core.model.metadata.builder.connection.RuleType
     * @see #getType()
     * @generated
     */
    void setType(RuleType value);

    /**
     * Returns the value of the '<em><b>Base Schema</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Base Schema</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Base Schema</em>' attribute.
     * @see #setBaseSchema(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getValidationRulesConnection_BaseSchema()
     * @model
     * @generated
     */
    String getBaseSchema();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.ValidationRulesConnection#getBaseSchema <em>Base Schema</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Base Schema</em>' attribute.
     * @see #getBaseSchema()
     * @generated
     */
    void setBaseSchema(String value);

    /**
     * Returns the value of the '<em><b>Base Column Names</b></em>' attribute list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Base Column Names</em>' attribute list isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Base Column Names</em>' attribute list.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getValidationRulesConnection_BaseColumnNames()
     * @model
     * @generated
     */
    EList<String> getBaseColumnNames();

    /**
     * Returns the value of the '<em><b>Ref Schema</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Ref Schema</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Ref Schema</em>' attribute.
     * @see #setRefSchema(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getValidationRulesConnection_RefSchema()
     * @model
     * @generated
     */
    String getRefSchema();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.ValidationRulesConnection#getRefSchema <em>Ref Schema</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Ref Schema</em>' attribute.
     * @see #getRefSchema()
     * @generated
     */
    void setRefSchema(String value);

    /**
     * Returns the value of the '<em><b>Ref Column Names</b></em>' attribute list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Ref Column Names</em>' attribute list isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Ref Column Names</em>' attribute list.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getValidationRulesConnection_RefColumnNames()
     * @model
     * @generated
     */
    EList<String> getRefColumnNames();

    /**
     * Returns the value of the '<em><b>Java Condition</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Java Condition</em>' attribute isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Java Condition</em>' attribute.
     * @see #setJavaCondition(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getValidationRulesConnection_JavaCondition()
     * @model
     * @generated
     */
    String getJavaCondition();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.ValidationRulesConnection#getJavaCondition <em>Java Condition</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Java Condition</em>' attribute.
     * @see #getJavaCondition()
     * @generated
     */
    void setJavaCondition(String value);

    /**
     * Returns the value of the '<em><b>Sql Condition</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Sql Condition</em>' attribute isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Sql Condition</em>' attribute.
     * @see #setSqlCondition(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getValidationRulesConnection_SqlCondition()
     * @model
     * @generated
     */
    String getSqlCondition();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.ValidationRulesConnection#getSqlCondition <em>Sql Condition</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Sql Condition</em>' attribute.
     * @see #getSqlCondition()
     * @generated
     */
    void setSqlCondition(String value);

    /**
     * Returns the value of the '<em><b>Logical Operator</b></em>' attribute.
     * The literals are from the enumeration {@link org.talend.core.model.metadata.builder.connection.LogicalOperator}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Logical Operator</em>' attribute isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Logical Operator</em>' attribute.
     * @see org.talend.core.model.metadata.builder.connection.LogicalOperator
     * @see #setLogicalOperator(LogicalOperator)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getValidationRulesConnection_LogicalOperator()
     * @model
     * @generated
     */
    LogicalOperator getLogicalOperator();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.ValidationRulesConnection#getLogicalOperator <em>Logical Operator</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Logical Operator</em>' attribute.
     * @see org.talend.core.model.metadata.builder.connection.LogicalOperator
     * @see #getLogicalOperator()
     * @generated
     */
    void setLogicalOperator(LogicalOperator value);

    /**
     * Returns the value of the '<em><b>Conditions</b></em>' containment reference list.
     * The list contents are of type {@link org.talend.core.model.metadata.builder.connection.ConditionType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Conditions</em>' reference list isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Conditions</em>' containment reference list.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getValidationRulesConnection_Conditions()
     * @model containment="true" resolveProxies="true"
     * @generated
     */
    EList<ConditionType> getConditions();

    /**
     * Returns the value of the '<em><b>Inner Joins</b></em>' map.
     * The key is of type {@link java.lang.String},
     * and the value is of type {@link java.lang.String},
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Inner Joins</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Inner Joins</em>' map.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getValidationRulesConnection_InnerJoins()
     * @model mapType="org.talend.core.model.metadata.builder.connection.InnerJoinMap<org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString>"
     * @generated
     */
    EMap<String, String> getInnerJoins();

    /**
     * Returns the value of the '<em><b>Is Disallow</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Is Disallow</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Is Disallow</em>' attribute.
     * @see #setIsDisallow(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getValidationRulesConnection_IsDisallow()
     * @model
     * @generated
     */
    boolean isIsDisallow();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.ValidationRulesConnection#isIsDisallow <em>Is Disallow</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Is Disallow</em>' attribute.
     * @see #isIsDisallow()
     * @generated
     */
    void setIsDisallow(boolean value);

    /**
     * Returns the value of the '<em><b>Is Reject Link</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Is Reject Link</em>' attribute isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Is Reject Link</em>' attribute.
     * @see #setIsRejectLink(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getValidationRulesConnection_IsRejectLink()
     * @model
     * @generated
     */
    boolean isIsRejectLink();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.ValidationRulesConnection#isIsRejectLink <em>Is Reject Link</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Is Reject Link</em>' attribute.
     * @see #isIsRejectLink()
     * @generated
     */
    void setIsRejectLink(boolean value);

} // ValidationRulesConnection
