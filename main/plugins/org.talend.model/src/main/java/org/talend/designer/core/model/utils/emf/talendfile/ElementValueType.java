/**
 * <copyright> </copyright>
 *
 * $Id$
 */
package org.talend.designer.core.model.utils.emf.talendfile;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Element Value Type</b></em>'. <!-- end-user-doc
 * -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.talend.designer.core.model.utils.emf.talendfile.ElementValueType#getElementRef <em>Element Ref</em>}</li>
 * <li>{@link org.talend.designer.core.model.utils.emf.talendfile.ElementValueType#getValue <em>Value</em>}</li>
 * <li>{@link org.talend.designer.core.model.utils.emf.talendfile.ElementValueType#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.designer.core.model.utils.emf.talendfile.TalendFilePackage#getElementValueType()
 * @model extendedMetaData="name='ElementValue_._type' kind='empty'"
 * @generated
 */
public interface ElementValueType extends EObject {

    /**
     * Returns the value of the '<em><b>Element Ref</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Element Ref</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Element Ref</em>' attribute.
     * @see #setElementRef(String)
     * @see org.talend.designer.core.model.utils.emf.talendfile.TalendFilePackage#getElementValueType_ElementRef()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='elementRef' namespace='##targetNamespace'"
     * @generated
     */
    String getElementRef();

    /**
     * Sets the value of the '{@link org.talend.designer.core.model.utils.emf.talendfile.ElementValueType#getElementRef <em>Element Ref</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Element Ref</em>' attribute.
     * @see #getElementRef()
     * @generated
     */
    void setElementRef(String value);

    /**
     * Returns the value of the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' attribute.
     * @see #setValue(String)
     * @see org.talend.designer.core.model.utils.emf.talendfile.TalendFilePackage#getElementValueType_Value()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='value' namespace='##targetNamespace'"
     * @generated
     */
    String getValue();

    /**
     * Get the raw value for dencryption.
     * 
     */
    String getRawValue();

    /**
     * Sets the value of the '{@link org.talend.designer.core.model.utils.emf.talendfile.ElementValueType#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' attribute.
     * @see #getValue()
     * @generated
     */
    void setValue(String value);

    /**
     * Set the raw value and encrypt it.
     * 
     * @param value
     * @param encrypt if true, will encrypt the value
     */
    void setValue(String value, boolean encrypt);

    /**
     * Returns the value of the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Type</em>' attribute isn't clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type</em>' attribute.
     * @see #setType(String)
     * @see org.talend.designer.core.model.utils.emf.talendfile.TalendFilePackage#getElementValueType_Type()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     * @generated
     */
    String getType();

    /**
     * Sets the value of the '{@link org.talend.designer.core.model.utils.emf.talendfile.ElementValueType#getType <em>Type</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Type</em>' attribute.
     * @see #getType()
     * @generated
     */
    void setType(String value);

    /**
     * Returns the value of the '<em><b>Hex Value</b></em>' attribute.
     * The default value is <code>"false"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Hex Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Hex Value</em>' attribute.
     * @see #setHexValue(boolean)
     * @see org.talend.designer.core.model.utils.emf.talendfile.TalendFilePackage#getElementValueType_HexValue()
     * @model default="false" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
     * @generated
     */
    boolean isHexValue();

    /**
     * Sets the value of the '{@link org.talend.designer.core.model.utils.emf.talendfile.ElementValueType#isHexValue <em>Hex Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Hex Value</em>' attribute.
     * @see #isHexValue()
     * @generated
     */
    void setHexValue(boolean value);

} // ElementValueType