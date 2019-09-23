/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Xml XPath Loop Descriptor</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.XmlXPathLoopDescriptor#getLimitBoucle <em>Limit Boucle</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.XmlXPathLoopDescriptor#getAbsoluteXPathQuery <em>Absolute XPath Query</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.XmlXPathLoopDescriptor#getConnection <em>Connection</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.XmlXPathLoopDescriptor#getSchemaTargets <em>Schema Targets</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getXmlXPathLoopDescriptor()
 * @model
 * @generated
 */
public interface XmlXPathLoopDescriptor extends EObject {

    /**
     * Returns the value of the '<em><b>Limit Boucle</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Limit Boucle</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Limit Boucle</em>' attribute.
     * @see #setLimitBoucle(Integer)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getXmlXPathLoopDescriptor_LimitBoucle()
     * @model
     * @generated
     */
    Integer getLimitBoucle();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.XmlXPathLoopDescriptor#getLimitBoucle <em>Limit Boucle</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Limit Boucle</em>' attribute.
     * @see #getLimitBoucle()
     * @generated
     */
    void setLimitBoucle(Integer value);

    /**
     * Returns the value of the '<em><b>Absolute XPath Query</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Absolute XPath Query</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Absolute XPath Query</em>' attribute.
     * @see #setAbsoluteXPathQuery(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getXmlXPathLoopDescriptor_AbsoluteXPathQuery()
     * @model
     * @generated
     */
    String getAbsoluteXPathQuery();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.XmlXPathLoopDescriptor#getAbsoluteXPathQuery <em>Absolute XPath Query</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Absolute XPath Query</em>' attribute.
     * @see #getAbsoluteXPathQuery()
     * @generated
     */
    void setAbsoluteXPathQuery(String value);

    /**
     * Returns the value of the '<em><b>Connection</b></em>' container reference.
     * It is bidirectional and its opposite is '{@link org.talend.core.model.metadata.builder.connection.XmlFileConnection#getSchema <em>Schema</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Connection</em>' container reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Connection</em>' container reference.
     * @see #setConnection(XmlFileConnection)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getXmlXPathLoopDescriptor_Connection()
     * @see org.talend.core.model.metadata.builder.connection.XmlFileConnection#getSchema
     * @model opposite="schema" transient="false"
     * @generated
     */
    XmlFileConnection getConnection();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.XmlXPathLoopDescriptor#getConnection <em>Connection</em>}' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Connection</em>' container reference.
     * @see #getConnection()
     * @generated
     */
    void setConnection(XmlFileConnection value);

    /**
     * Returns the value of the '<em><b>Schema Targets</b></em>' containment reference list.
     * The list contents are of type {@link org.talend.core.model.metadata.builder.connection.SchemaTarget}.
     * It is bidirectional and its opposite is '{@link org.talend.core.model.metadata.builder.connection.SchemaTarget#getSchema <em>Schema</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Schema Targets</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Schema Targets</em>' containment reference list.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getXmlXPathLoopDescriptor_SchemaTargets()
     * @see org.talend.core.model.metadata.builder.connection.SchemaTarget#getSchema
     * @model opposite="schema" containment="true" resolveProxies="true"
     * @generated
     */
    EList<SchemaTarget> getSchemaTargets();

} // XmlXPathLoopDescriptor
