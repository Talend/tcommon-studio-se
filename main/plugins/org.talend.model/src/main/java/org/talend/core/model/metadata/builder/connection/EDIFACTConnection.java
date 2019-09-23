/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EDIFACT Connection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.EDIFACTConnection#getXmlName <em>Xml Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.EDIFACTConnection#getFileName <em>File Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.EDIFACTConnection#getXmlPath <em>Xml Path</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getEDIFACTConnection()
 * @model
 * @generated
 */
public interface EDIFACTConnection extends Connection {

    /**
     * Returns the value of the '<em><b>Xml Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Xml Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Xml Name</em>' attribute.
     * @see #setXmlName(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getEDIFACTConnection_XmlName()
     * @model
     * @generated
     */
    String getXmlName();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.EDIFACTConnection#getXmlName <em>Xml Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Xml Name</em>' attribute.
     * @see #getXmlName()
     * @generated
     */
    void setXmlName(String value);

    /**
     * Returns the value of the '<em><b>File Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>File Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>File Name</em>' attribute.
     * @see #setFileName(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getEDIFACTConnection_FileName()
     * @model
     * @generated
     */
    String getFileName();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.EDIFACTConnection#getFileName <em>File Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>File Name</em>' attribute.
     * @see #getFileName()
     * @generated
     */
    void setFileName(String value);

    /**
     * Returns the value of the '<em><b>Xml Path</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Xml Path</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Xml Path</em>' attribute.
     * @see #setXmlPath(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getEDIFACTConnection_XmlPath()
     * @model
     * @generated
     */
    String getXmlPath();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.EDIFACTConnection#getXmlPath <em>Xml Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Xml Path</em>' attribute.
     * @see #getXmlPath()
     * @generated
     */
    void setXmlPath(String value);

} // EDIFACTConnection
