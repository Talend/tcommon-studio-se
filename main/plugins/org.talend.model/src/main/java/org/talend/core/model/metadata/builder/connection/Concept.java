/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

import org.eclipse.emf.common.util.EList;
import org.talend.cwm.relational.TdTable;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Concept</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.Concept#getLoopExpression <em>Loop Expression</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.Concept#getLoopLimit <em>Loop Limit</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.Concept#getConceptTargets <em>Concept Targets</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.Concept#isInputModel <em>Input Model</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.Concept#getGroup <em>Group</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.Concept#getRoot <em>Root</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.Concept#getLoop <em>Loop</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.Concept#getConceptType <em>Concept Type</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.Concept#getXPathPrefix <em>XPath Prefix</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getConcept()
 * @model
 * @generated
 */
public interface Concept extends TdTable {

    /**
     * Returns the value of the '<em><b>Loop Expression</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Loop Expression</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Loop Expression</em>' attribute.
     * @see #setLoopExpression(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getConcept_LoopExpression()
     * @model
     * @generated
     */
    String getLoopExpression();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.Concept#getLoopExpression <em>Loop Expression</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Loop Expression</em>' attribute.
     * @see #getLoopExpression()
     * @generated
     */
    void setLoopExpression(String value);

    /**
     * Returns the value of the '<em><b>Loop Limit</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Loop Limit</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Loop Limit</em>' attribute.
     * @see #setLoopLimit(Integer)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getConcept_LoopLimit()
     * @model
     * @generated
     */
    Integer getLoopLimit();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.Concept#getLoopLimit <em>Loop Limit</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Loop Limit</em>' attribute.
     * @see #getLoopLimit()
     * @generated
     */
    void setLoopLimit(Integer value);

    /**
     * Returns the value of the '<em><b>Concept Targets</b></em>' containment reference list.
     * The list contents are of type {@link org.talend.core.model.metadata.builder.connection.ConceptTarget}.
     * It is bidirectional and its opposite is '{@link org.talend.core.model.metadata.builder.connection.ConceptTarget#getSchema <em>Schema</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Concept Targets</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Concept Targets</em>' containment reference list.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getConcept_ConceptTargets()
     * @see org.talend.core.model.metadata.builder.connection.ConceptTarget#getSchema
     * @model opposite="schema" containment="true" resolveProxies="true"
     * @generated
     */
    EList<ConceptTarget> getConceptTargets();

    /**
     * Returns the value of the '<em><b>Input Model</b></em>' attribute.
     * The default value is <code>"true"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Input Model</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Input Model</em>' attribute.
     * @see #setInputModel(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getConcept_InputModel()
     * @model default="true"
     * @generated
     */
    boolean isInputModel();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.Concept#isInputModel <em>Input Model</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Input Model</em>' attribute.
     * @see #isInputModel()
     * @generated
     */
    void setInputModel(boolean value);

    /**
     * Returns the value of the '<em><b>Group</b></em>' containment reference list.
     * The list contents are of type {@link org.talend.core.model.metadata.builder.connection.XMLFileNode}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Group</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Group</em>' containment reference list.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getConcept_Group()
     * @model containment="true"
     * @generated
     */
    EList<XMLFileNode> getGroup();

    /**
     * Returns the value of the '<em><b>Root</b></em>' containment reference list.
     * The list contents are of type {@link org.talend.core.model.metadata.builder.connection.XMLFileNode}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Root</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Root</em>' containment reference list.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getConcept_Root()
     * @model containment="true" resolveProxies="true" ordered="false"
     * @generated
     */
    EList<XMLFileNode> getRoot();

    /**
     * Returns the value of the '<em><b>Loop</b></em>' containment reference list.
     * The list contents are of type {@link org.talend.core.model.metadata.builder.connection.XMLFileNode}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Loop</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Loop</em>' containment reference list.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getConcept_Loop()
     * @model containment="true"
     * @generated
     */
    EList<XMLFileNode> getLoop();

    /**
     * Returns the value of the '<em><b>Concept Type</b></em>' attribute.
     * The default value is <code>"INPUT"</code>.
     * The literals are from the enumeration {@link org.talend.core.model.metadata.builder.connection.MdmConceptType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Concept Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Concept Type</em>' attribute.
     * @see org.talend.core.model.metadata.builder.connection.MdmConceptType
     * @see #setConceptType(MdmConceptType)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getConcept_ConceptType()
     * @model default="INPUT" required="true"
     * @generated
     */
    MdmConceptType getConceptType();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.Concept#getConceptType <em>Concept Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Concept Type</em>' attribute.
     * @see org.talend.core.model.metadata.builder.connection.MdmConceptType
     * @see #getConceptType()
     * @generated
     */
    void setConceptType(MdmConceptType value);

    /**
     * Returns the value of the '<em><b>XPath Prefix</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>XPath Prefix</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>XPath Prefix</em>' attribute.
     * @see #setXPathPrefix(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getConcept_XPathPrefix()
     * @model
     * @generated
     */
    String getXPathPrefix();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.Concept#getXPathPrefix <em>XPath Prefix</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>XPath Prefix</em>' attribute.
     * @see #getXPathPrefix()
     * @generated
     */
    void setXPathPrefix(String value);
} // Concept
