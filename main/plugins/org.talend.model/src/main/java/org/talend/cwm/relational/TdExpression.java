/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.cwm.relational;

import java.util.HashMap;

import orgomg.cwm.objectmodel.core.Expression;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Td Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.talend.cwm.relational.TdExpression#getVersion <em>Version</em>}</li>
 * <li>{@link org.talend.cwm.relational.TdExpression#getModificationDate <em>Modification Date</em>}</li>
 * <li>{@link org.talend.cwm.relational.TdExpression#getName <em>Name</em>}</li>
 * <li>{@link org.talend.cwm.relational.TdExpression#getExpressionVariableMap <em>Expression Variable Map</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.cwm.relational.RelationalPackage#getTdExpression()
 * @model
 * @generated
 */
public interface TdExpression extends Expression {

    /**
     * Returns the value of the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Version</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Version</em>' attribute.
     * @see #setVersion(String)
     * @see org.talend.cwm.relational.RelationalPackage#getTdExpression_Version()
     * @model dataType="orgomg.cwm.objectmodel.core.String"
     * @generated
     */
    String getVersion();

    /**
     * Sets the value of the '{@link org.talend.cwm.relational.TdExpression#getVersion <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Version</em>' attribute.
     * @see #getVersion()
     * @generated
     */
    void setVersion(String value);

    /**
     * Returns the value of the '<em><b>Modification Date</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Modification Date</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Modification Date</em>' attribute.
     * @see #setModificationDate(String)
     * @see org.talend.cwm.relational.RelationalPackage#getTdExpression_ModificationDate()
     * @model dataType="orgomg.cwm.objectmodel.core.Time"
     * @generated
     */
    String getModificationDate();

    /**
     * Sets the value of the '{@link org.talend.cwm.relational.TdExpression#getModificationDate <em>Modification
     * Date</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Modification Date</em>' attribute.
     * @see #getModificationDate()
     * @generated
     */
    void setModificationDate(String value);

    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(String)
     * @see org.talend.cwm.relational.RelationalPackage#getTdExpression_Name()
     * @model dataType="orgomg.cwm.objectmodel.core.String"
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link org.talend.cwm.relational.TdExpression#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

    /**
     * Returns the value of the '<em><b>Expression Variable Map</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Expression Variable Map</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Expression Variable Map</em>' attribute.
     * @see #setExpressionVariableMap(HashMap)
     * @see org.talend.cwm.relational.RelationalPackage#getTdExpression_ExpressionVariableMap()
     * @model dataType="org.talend.cwm.relational.javaHashMap"
     * @generated
     */
    HashMap<String, String> getExpressionVariableMap();

    /**
     * Sets the value of the '{@link org.talend.cwm.relational.TdExpression#getExpressionVariableMap <em>Expression
     * Variable Map</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Expression Variable Map</em>' attribute.
     * @see #getExpressionVariableMap()
     * @generated
     */
    void setExpressionVariableMap(HashMap<String, String> value);

    /**
     * Judge whether two expression take same database type and same version
     * 
     * @generated not
     */
    boolean sameVersionAndType(TdExpression targetEx);

} // TdExpression
