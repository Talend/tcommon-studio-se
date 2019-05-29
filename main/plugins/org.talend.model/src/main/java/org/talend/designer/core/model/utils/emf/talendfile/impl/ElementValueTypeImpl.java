/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.designer.core.model.utils.emf.talendfile.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.talend.daikon.security.CryptoHelper;
import org.talend.designer.core.model.utils.emf.talendfile.ElementValueType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFilePackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Element Value Type</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.talend.designer.core.model.utils.emf.talendfile.impl.ElementValueTypeImpl#getElementRef <em>Element
 * Ref</em>}</li>
 * <li>{@link org.talend.designer.core.model.utils.emf.talendfile.impl.ElementValueTypeImpl#getValue <em>Value</em>}</li>
 * <li>{@link org.talend.designer.core.model.utils.emf.talendfile.impl.ElementValueTypeImpl#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class ElementValueTypeImpl extends EObjectImpl implements ElementValueType {

    /**
     * The default value of the '{@link #getElementRef() <em>Element Ref</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getElementRef()
     * @generated
     * @ordered
     */
    protected static final String ELEMENT_REF_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getElementRef() <em>Element Ref</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getElementRef()
     * @generated
     * @ordered
     */
    protected String elementRef = ELEMENT_REF_EDEFAULT;

    /**
     * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected static final String VALUE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getValue() <em>Value</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected String value = VALUE_EDEFAULT;

    /**
     * The default value of the '{@link #getType() <em>Type</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @see #getType()
     * @generated
     * @ordered
     */
    protected static final String TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getType() <em>Type</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @see #getType()
     * @generated
     * @ordered
     */
    protected String type = TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #isHexValue() <em>Hex Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isHexValue()
     * @generated
     * @ordered
     */
    protected static final boolean HEX_VALUE_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isHexValue() <em>Hex Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isHexValue()
     * @generated
     * @ordered
     */
    protected boolean hexValue = HEX_VALUE_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected ElementValueTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return TalendFilePackage.Literals.ELEMENT_VALUE_TYPE;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getElementRef() {
        return elementRef;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setElementRef(String newElementRef) {
        String oldElementRef = elementRef;
        elementRef = newElementRef;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TalendFilePackage.ELEMENT_VALUE_TYPE__ELEMENT_REF, oldElementRef, elementRef));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getValue() {
        return value;
    }

    public String getRawValue() {
        if (value != null && value.length() > 0) {
            String decrypt = CryptoHelper.getDefault().decrypt(value);
            if (decrypt != null) {
                return decrypt;
            }
        }
        return value;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setValue(String newValue) {
        String oldValue = value;
        value = newValue;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TalendFilePackage.ELEMENT_VALUE_TYPE__VALUE, oldValue, value));
    }

    public void setValue(String value, boolean encrypt) {
        if (encrypt && value != null && value.length() > 0) {
            String encryptValue = CryptoHelper.getDefault().encrypt(value);
            if (encryptValue != null) {
                setValue(encryptValue);
                return;
            }
        }
        setValue(value);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getType() {
        return type;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setType(String newType) {
        String oldType = type;
        type = newType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TalendFilePackage.ELEMENT_VALUE_TYPE__TYPE, oldType, type));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isHexValue() {
        return hexValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setHexValue(boolean newHexValue) {
        boolean oldHexValue = hexValue;
        hexValue = newHexValue;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TalendFilePackage.ELEMENT_VALUE_TYPE__HEX_VALUE, oldHexValue, hexValue));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case TalendFilePackage.ELEMENT_VALUE_TYPE__ELEMENT_REF:
                return getElementRef();
            case TalendFilePackage.ELEMENT_VALUE_TYPE__VALUE:
                return getValue();
            case TalendFilePackage.ELEMENT_VALUE_TYPE__TYPE:
                return getType();
            case TalendFilePackage.ELEMENT_VALUE_TYPE__HEX_VALUE:
                return isHexValue() ? Boolean.TRUE : Boolean.FALSE;
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case TalendFilePackage.ELEMENT_VALUE_TYPE__ELEMENT_REF:
                setElementRef((String)newValue);
                return;
            case TalendFilePackage.ELEMENT_VALUE_TYPE__VALUE:
                setValue((String)newValue);
                return;
            case TalendFilePackage.ELEMENT_VALUE_TYPE__TYPE:
                setType((String)newValue);
                return;
            case TalendFilePackage.ELEMENT_VALUE_TYPE__HEX_VALUE:
                setHexValue(((Boolean)newValue).booleanValue());
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void eUnset(int featureID) {
        switch (featureID) {
            case TalendFilePackage.ELEMENT_VALUE_TYPE__ELEMENT_REF:
                setElementRef(ELEMENT_REF_EDEFAULT);
                return;
            case TalendFilePackage.ELEMENT_VALUE_TYPE__VALUE:
                setValue(VALUE_EDEFAULT);
                return;
            case TalendFilePackage.ELEMENT_VALUE_TYPE__TYPE:
                setType(TYPE_EDEFAULT);
                return;
            case TalendFilePackage.ELEMENT_VALUE_TYPE__HEX_VALUE:
                setHexValue(HEX_VALUE_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case TalendFilePackage.ELEMENT_VALUE_TYPE__ELEMENT_REF:
                return ELEMENT_REF_EDEFAULT == null ? elementRef != null : !ELEMENT_REF_EDEFAULT.equals(elementRef);
            case TalendFilePackage.ELEMENT_VALUE_TYPE__VALUE:
                return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
            case TalendFilePackage.ELEMENT_VALUE_TYPE__TYPE:
                return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
            case TalendFilePackage.ELEMENT_VALUE_TYPE__HEX_VALUE:
                return hexValue != HEX_VALUE_EDEFAULT;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (elementRef: ");
        result.append(elementRef);
        result.append(", value: ");
        result.append(value);
        result.append(", type: ");
        result.append(type);
        result.append(", hexValue: ");
        result.append(hexValue);
        result.append(')');
        return result.toString();
    }

} // ElementValueTypeImpl
