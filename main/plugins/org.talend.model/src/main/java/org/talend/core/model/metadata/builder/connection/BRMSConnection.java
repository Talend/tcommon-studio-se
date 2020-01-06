/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

import org.eclipse.emf.common.util.EList;
import java.util.ArrayList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>BRMS Connection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.BRMSConnection#getXmlField <em>Xml Field</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.BRMSConnection#getUrlName <em>Url Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.BRMSConnection#getTacWebappName <em>Tac Webapp Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.BRMSConnection#getClassName <em>Class Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.BRMSConnection#getModuleUsed <em>Module Used</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.BRMSConnection#getRoot <em>Root</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.BRMSConnection#getGroup <em>Group</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.BRMSConnection#getLoop <em>Loop</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.BRMSConnection#getPackage <em>Package</em>}</li>
 * </ul>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getBRMSConnection()
 * @model
 * @generated
 */
public interface BRMSConnection extends Connection {

    /**
     * Returns the value of the '<em><b>Xml Field</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Xml Field</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Xml Field</em>' attribute.
     * @see #setXmlField(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getBRMSConnection_XmlField()
     * @model
     * @generated
     */
    String getXmlField();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.BRMSConnection#getXmlField <em>Xml Field</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Xml Field</em>' attribute.
     * @see #getXmlField()
     * @generated
     */
    void setXmlField(String value);

    /**
     * Returns the value of the '<em><b>Url Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Url Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Url Name</em>' attribute.
     * @see #setUrlName(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getBRMSConnection_UrlName()
     * @model
     * @generated
     */
    String getUrlName();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.BRMSConnection#getUrlName <em>Url Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Url Name</em>' attribute.
     * @see #getUrlName()
     * @generated
     */
    void setUrlName(String value);

    /**
     * Returns the value of the '<em><b>Tac Webapp Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Tac Webapp Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Tac Webapp Name</em>' attribute.
     * @see #setTacWebappName(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getBRMSConnection_TacWebappName()
     * @model
     * @generated
     */
    String getTacWebappName();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.BRMSConnection#getTacWebappName <em>Tac Webapp Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Tac Webapp Name</em>' attribute.
     * @see #getTacWebappName()
     * @generated
     */
    void setTacWebappName(String value);

    /**
     * Returns the value of the '<em><b>Class Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Class Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Class Name</em>' attribute.
     * @see #setClassName(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getBRMSConnection_ClassName()
     * @model
     * @generated
     */
    String getClassName();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.BRMSConnection#getClassName <em>Class Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Class Name</em>' attribute.
     * @see #getClassName()
     * @generated
     */
    void setClassName(String value);

    /**
     * Returns the value of the '<em><b>Module Used</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Module Used</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Module Used</em>' attribute.
     * @see #setModuleUsed(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getBRMSConnection_ModuleUsed()
     * @model
     * @generated
     */
    String getModuleUsed();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.BRMSConnection#getModuleUsed <em>Module Used</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Module Used</em>' attribute.
     * @see #getModuleUsed()
     * @generated
     */
    void setModuleUsed(String value);

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
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getBRMSConnection_Root()
     * @model containment="true" resolveProxies="true"
     * @generated
     */
    EList<XMLFileNode> getRoot();

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
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getBRMSConnection_Group()
     * @model containment="true" resolveProxies="true"
     * @generated
     */
    EList<XMLFileNode> getGroup();

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
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getBRMSConnection_Loop()
     * @model containment="true" resolveProxies="true"
     * @generated
     */
    EList<XMLFileNode> getLoop();

    /**
     * Returns the value of the '<em><b>Package</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Package</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Package</em>' attribute.
     * @see #setPackage(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getBRMSConnection_Package()
     * @model
     * @generated
     */
    String getPackage();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.BRMSConnection#getPackage <em>Package</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Package</em>' attribute.
     * @see #getPackage()
     * @generated
     */
    void setPackage(String value);

} // BRMSConnection
