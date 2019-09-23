/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.cwm.xml;

import org.eclipse.emf.ecore.EObject;

import orgomg.cwm.resource.xml.ElementType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Td Xml Element Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.cwm.xml.TdXmlElementType#getXsdElementDeclaration <em>Xsd Element Declaration</em>}</li>
 *   <li>{@link org.talend.cwm.xml.TdXmlElementType#getOwnedDocument <em>Owned Document</em>}</li>
 *   <li>{@link org.talend.cwm.xml.TdXmlElementType#getJavaType <em>Java Type</em>}</li>
 *   <li>{@link org.talend.cwm.xml.TdXmlElementType#getXmlContent <em>Xml Content</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.cwm.xml.XmlPackage#getTdXmlElementType()
 * @model
 * @generated
 */
public interface TdXmlElementType extends ElementType {

    /**
     * Returns the value of the '<em><b>Xsd Element Declaration</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Xsd Element Declaration</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Xsd Element Declaration</em>' reference.
     * @see #setXsdElementDeclaration(EObject)
     * @see org.talend.cwm.xml.XmlPackage#getTdXmlElementType_XsdElementDeclaration()
     * @model
     * @generated
     */
    EObject getXsdElementDeclaration();

    /**
     * Sets the value of the '{@link org.talend.cwm.xml.TdXmlElementType#getXsdElementDeclaration <em>Xsd Element Declaration</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Xsd Element Declaration</em>' reference.
     * @see #getXsdElementDeclaration()
     * @generated
     */
    void setXsdElementDeclaration(EObject value);

    /**
     * Returns the value of the '<em><b>Owned Document</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Owned Document</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Owned Document</em>' reference.
     * @see #setOwnedDocument(TdXmlSchema)
     * @see org.talend.cwm.xml.XmlPackage#getTdXmlElementType_OwnedDocument()
     * @model
     * @generated
     */
    TdXmlSchema getOwnedDocument();

    /**
     * Sets the value of the '{@link org.talend.cwm.xml.TdXmlElementType#getOwnedDocument <em>Owned Document</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Owned Document</em>' reference.
     * @see #getOwnedDocument()
     * @generated
     */
    void setOwnedDocument(TdXmlSchema value);

    /**
     * Returns the value of the '<em><b>Java Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Java Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Java Type</em>' attribute.
     * @see #setJavaType(String)
     * @see org.talend.cwm.xml.XmlPackage#getTdXmlElementType_JavaType()
     * @model dataType="orgomg.cwm.objectmodel.core.String"
     * @generated
     */
    String getJavaType();

    /**
     * Sets the value of the '{@link org.talend.cwm.xml.TdXmlElementType#getJavaType <em>Java Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Java Type</em>' attribute.
     * @see #getJavaType()
     * @generated
     */
    void setJavaType(String value);

    /**
     * Returns the value of the '<em><b>Xml Content</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Xml Content</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Xml Content</em>' containment reference.
     * @see #setXmlContent(TdXmlContent)
     * @see org.talend.cwm.xml.XmlPackage#getTdXmlElementType_XmlContent()
     * @model containment="true" resolveProxies="true"
     * @generated
     */
    TdXmlContent getXmlContent();

    /**
     * Sets the value of the '{@link org.talend.cwm.xml.TdXmlElementType#getXmlContent <em>Xml Content</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Xml Content</em>' containment reference.
     * @see #getXmlContent()
     * @generated
     */
    void setXmlContent(TdXmlContent value);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The type of the content of the xml element. This type is a meta-information either set by the user who knows what type of data is contained in the xml element, or infered from the data.
     * <!-- end-model-doc -->
     * @model contentTypeDataType="orgomg.cwm.objectmodel.core.String"
     * @generated
     */
    void setContentType(String contentType);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The type of the content of the xml element. This type is a meta-information either set by the user who knows what type of data is contained in the xml element, or infered from the data.
     * <!-- end-model-doc -->
     * @model kind="operation" dataType="orgomg.cwm.objectmodel.core.String"
     * @generated
     */
    String getContentType();

} // TdXmlElementType
