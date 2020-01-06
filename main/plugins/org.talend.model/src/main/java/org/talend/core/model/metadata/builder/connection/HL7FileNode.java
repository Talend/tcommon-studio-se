/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>HL7 File Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.HL7FileNode#getFilePath <em>File Path</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.HL7FileNode#getOrder <em>Order</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.HL7FileNode#getAttribute <em>Attribute</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.HL7FileNode#getDefaultValue <em>Default Value</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.HL7FileNode#getRelatedColumn <em>Related Column</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.HL7FileNode#isRepeatable <em>Repeatable</em>}</li>
 * </ul>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getHL7FileNode()
 * @model
 * @generated
 */
public interface HL7FileNode extends EObject {

    /**
     * Returns the value of the '<em><b>File Path</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>File Path</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>File Path</em>' attribute.
     * @see #setFilePath(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getHL7FileNode_FilePath()
     * @model
     * @generated
     */
    String getFilePath();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.HL7FileNode#getFilePath <em>File Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>File Path</em>' attribute.
     * @see #getFilePath()
     * @generated
     */
    void setFilePath(String value);

    /**
     * Returns the value of the '<em><b>Order</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Order</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Order</em>' attribute.
     * @see #setOrder(int)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getHL7FileNode_Order()
     * @model
     * @generated
     */
    int getOrder();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.HL7FileNode#getOrder <em>Order</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Order</em>' attribute.
     * @see #getOrder()
     * @generated
     */
    void setOrder(int value);

    /**
     * Returns the value of the '<em><b>Attribute</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Attribute</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Attribute</em>' attribute.
     * @see #setAttribute(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getHL7FileNode_Attribute()
     * @model
     * @generated
     */
    String getAttribute();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.HL7FileNode#getAttribute <em>Attribute</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Attribute</em>' attribute.
     * @see #getAttribute()
     * @generated
     */
    void setAttribute(String value);

    /**
     * Returns the value of the '<em><b>Default Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Default Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Default Value</em>' attribute.
     * @see #setDefaultValue(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getHL7FileNode_DefaultValue()
     * @model
     * @generated
     */
    String getDefaultValue();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.HL7FileNode#getDefaultValue <em>Default Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Default Value</em>' attribute.
     * @see #getDefaultValue()
     * @generated
     */
    void setDefaultValue(String value);

    /**
     * Returns the value of the '<em><b>Related Column</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Related Column</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Related Column</em>' attribute.
     * @see #setRelatedColumn(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getHL7FileNode_RelatedColumn()
     * @model
     * @generated
     */
    String getRelatedColumn();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.HL7FileNode#getRelatedColumn <em>Related Column</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Related Column</em>' attribute.
     * @see #getRelatedColumn()
     * @generated
     */
    void setRelatedColumn(String value);

    /**
     * Returns the value of the '<em><b>Repeatable</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Repeatable</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Repeatable</em>' attribute.
     * @see #setRepeatable(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getHL7FileNode_Repeatable()
     * @model
     * @generated
     */
    boolean isRepeatable();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.HL7FileNode#isRepeatable <em>Repeatable</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Repeatable</em>' attribute.
     * @see #isRepeatable()
     * @generated
     */
    void setRepeatable(boolean value);

} // HL7FileNode
