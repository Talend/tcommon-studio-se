/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.cwm.xml;

import orgomg.cwm.resource.xml.Schema;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Td Xml Schema</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.talend.cwm.xml.TdXmlSchema#getXsdFilePath <em>Xsd File Path</em>}</li>
 * </ul>
 *
 * @see org.talend.cwm.xml.XmlPackage#getTdXmlSchema()
 * @model
 * @generated
 */
public interface TdXmlSchema extends Schema {

    /**
     * Returns the value of the '<em><b>Xsd File Path</b></em>' attribute.
     * The default value is <code>""</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Xsd File Path</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Xsd File Path</em>' attribute.
     * @see #setXsdFilePath(String)
     * @see org.talend.cwm.xml.XmlPackage#getTdXmlSchema_XsdFilePath()
     * @model default="" dataType="orgomg.cwm.objectmodel.core.String"
     * @generated
     */
    String getXsdFilePath();

    /**
     * Sets the value of the '{@link org.talend.cwm.xml.TdXmlSchema#getXsdFilePath <em>Xsd File Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Xsd File Path</em>' attribute.
     * @see #getXsdFilePath()
     * @generated
     */
    void setXsdFilePath(String value);

} // TdXmlSchema
