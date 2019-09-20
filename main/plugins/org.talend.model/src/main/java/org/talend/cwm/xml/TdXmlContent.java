/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.cwm.xml;

import org.eclipse.emf.common.util.EList;

import orgomg.cwm.resource.xml.Content;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Td Xml Content</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.talend.cwm.xml.TdXmlContent#getXmlElements <em>Xml Elements</em>}</li>
 * </ul>
 *
 * @see org.talend.cwm.xml.XmlPackage#getTdXmlContent()
 * @model
 * @generated
 */
public interface TdXmlContent extends Content {

    /**
     * Returns the value of the '<em><b>Xml Elements</b></em>' containment reference list.
     * The list contents are of type {@link org.talend.cwm.xml.TdXmlElementType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Xml Elements</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Xml Elements</em>' containment reference list.
     * @see org.talend.cwm.xml.XmlPackage#getTdXmlContent_XmlElements()
     * @model containment="true" resolveProxies="true"
     * @generated
     */
    EList<TdXmlElementType> getXmlElements();

} // TdXmlContent
