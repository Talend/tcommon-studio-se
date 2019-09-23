/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Concept Target</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.ConceptTarget#getSchema <em>Schema</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.ConceptTarget#getTargetName <em>Target Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.ConceptTarget#getRelativeLoopExpression <em>Relative Loop Expression</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getConceptTarget()
 * @model
 * @generated
 */
public interface ConceptTarget extends EObject {

    /**
     * Returns the value of the '<em><b>Schema</b></em>' container reference.
     * It is bidirectional and its opposite is '{@link org.talend.core.model.metadata.builder.connection.Concept#getConceptTargets <em>Concept Targets</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Schema</em>' container reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Schema</em>' container reference.
     * @see #setSchema(Concept)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getConceptTarget_Schema()
     * @see org.talend.core.model.metadata.builder.connection.Concept#getConceptTargets
     * @model opposite="conceptTargets" transient="false"
     * @generated
     */
    Concept getSchema();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.ConceptTarget#getSchema <em>Schema</em>}' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Schema</em>' container reference.
     * @see #getSchema()
     * @generated
     */
    void setSchema(Concept value);

    /**
     * Returns the value of the '<em><b>Target Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Target Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Target Name</em>' attribute.
     * @see #setTargetName(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getConceptTarget_TargetName()
     * @model
     * @generated
     */
    String getTargetName();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.ConceptTarget#getTargetName <em>Target Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Target Name</em>' attribute.
     * @see #getTargetName()
     * @generated
     */
    void setTargetName(String value);

    /**
     * Returns the value of the '<em><b>Relative Loop Expression</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Relative Loop Expression</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Relative Loop Expression</em>' attribute.
     * @see #setRelativeLoopExpression(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getConceptTarget_RelativeLoopExpression()
     * @model
     * @generated
     */
    String getRelativeLoopExpression();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.ConceptTarget#getRelativeLoopExpression <em>Relative Loop Expression</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Relative Loop Expression</em>' attribute.
     * @see #getRelativeLoopExpression()
     * @generated
     */
    void setRelativeLoopExpression(String value);

} // ConceptTarget
