/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>HL7 Connection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.HL7Connection#getStartChar <em>Start Char</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.HL7Connection#getEndChar <em>End Char</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.HL7Connection#getRoot <em>Root</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.HL7Connection#getOutputFilePath <em>Output File Path</em>}</li>
 * </ul>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getHL7Connection()
 * @model
 * @generated
 */
public interface HL7Connection extends FileConnection {

    /**
     * Returns the value of the '<em><b>Start Char</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Start Char</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Start Char</em>' attribute.
     * @see #setStartChar(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getHL7Connection_StartChar()
     * @model
     * @generated
     */
    String getStartChar();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.HL7Connection#getStartChar <em>Start Char</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Start Char</em>' attribute.
     * @see #getStartChar()
     * @generated
     */
    void setStartChar(String value);

    /**
     * Returns the value of the '<em><b>End Char</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>End Char</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>End Char</em>' attribute.
     * @see #setEndChar(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getHL7Connection_EndChar()
     * @model
     * @generated
     */
    String getEndChar();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.HL7Connection#getEndChar <em>End Char</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>End Char</em>' attribute.
     * @see #getEndChar()
     * @generated
     */
    void setEndChar(String value);

    /**
     * Returns the value of the '<em><b>Root</b></em>' containment reference list.
     * The list contents are of type {@link org.talend.core.model.metadata.builder.connection.HL7FileNode}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Root</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Root</em>' containment reference list.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getHL7Connection_Root()
     * @model containment="true" resolveProxies="true" ordered="false"
     * @generated
     */
    EList<HL7FileNode> getRoot();

    /**
     * Returns the value of the '<em><b>Output File Path</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Output File Path</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Output File Path</em>' attribute.
     * @see #setOutputFilePath(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getHL7Connection_OutputFilePath()
     * @model
     * @generated
     */
    String getOutputFilePath();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.HL7Connection#getOutputFilePath <em>Output File Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Output File Path</em>' attribute.
     * @see #getOutputFilePath()
     * @generated
     */
    void setOutputFilePath(String value);

} // HL7Connection
