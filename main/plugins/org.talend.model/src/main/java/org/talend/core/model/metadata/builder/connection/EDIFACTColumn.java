/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EDIFACT Column</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.EDIFACTColumn#getEDIColumnName <em>EDI Column Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.EDIFACTColumn#getEDIXpath <em>EDI Xpath</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getEDIFACTColumn()
 * @model
 * @generated
 */
public interface EDIFACTColumn extends MetadataColumn {

    /**
     * Returns the value of the '<em><b>EDI Column Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>EDI Column Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>EDI Column Name</em>' attribute.
     * @see #setEDIColumnName(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getEDIFACTColumn_EDIColumnName()
     * @model
     * @generated
     */
    String getEDIColumnName();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.EDIFACTColumn#getEDIColumnName <em>EDI Column Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>EDI Column Name</em>' attribute.
     * @see #getEDIColumnName()
     * @generated
     */
    void setEDIColumnName(String value);

    /**
     * Returns the value of the '<em><b>EDI Xpath</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>EDI Xpath</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>EDI Xpath</em>' attribute.
     * @see #setEDIXpath(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getEDIFACTColumn_EDIXpath()
     * @model
     * @generated
     */
    String getEDIXpath();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.EDIFACTColumn#getEDIXpath <em>EDI Xpath</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>EDI Xpath</em>' attribute.
     * @see #getEDIXpath()
     * @generated
     */
    void setEDIXpath(String value);

} // EDIFACTColumn
