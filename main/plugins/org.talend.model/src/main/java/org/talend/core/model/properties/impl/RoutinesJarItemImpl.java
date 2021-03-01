/**
 */
package org.talend.core.model.properties.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.RoutinesJarItem;
import org.talend.core.model.properties.RoutinesJarType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Routines Jar Item</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.properties.impl.RoutinesJarItemImpl#getRoutinesJarType <em>Routines Jar Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RoutinesJarItemImpl extends ItemImpl implements RoutinesJarItem {
    /**
     * The cached value of the '{@link #getRoutinesJarType() <em>Routines Jar Type</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRoutinesJarType()
     * @generated
     * @ordered
     */
    protected RoutinesJarType routinesJarType;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected RoutinesJarItemImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return PropertiesPackage.Literals.ROUTINES_JAR_ITEM;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RoutinesJarType getRoutinesJarType() {
        if (routinesJarType != null && routinesJarType.eIsProxy()) {
            InternalEObject oldRoutinesJarType = (InternalEObject)routinesJarType;
            routinesJarType = (RoutinesJarType)eResolveProxy(oldRoutinesJarType);
            if (routinesJarType != oldRoutinesJarType) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, PropertiesPackage.ROUTINES_JAR_ITEM__ROUTINES_JAR_TYPE, oldRoutinesJarType, routinesJarType));
            }
        }
        return routinesJarType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RoutinesJarType basicGetRoutinesJarType() {
        return routinesJarType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRoutinesJarType(RoutinesJarType newRoutinesJarType) {
        RoutinesJarType oldRoutinesJarType = routinesJarType;
        routinesJarType = newRoutinesJarType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.ROUTINES_JAR_ITEM__ROUTINES_JAR_TYPE, oldRoutinesJarType, routinesJarType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case PropertiesPackage.ROUTINES_JAR_ITEM__ROUTINES_JAR_TYPE:
                if (resolve) return getRoutinesJarType();
                return basicGetRoutinesJarType();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case PropertiesPackage.ROUTINES_JAR_ITEM__ROUTINES_JAR_TYPE:
                setRoutinesJarType((RoutinesJarType)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void eUnset(int featureID) {
        switch (featureID) {
            case PropertiesPackage.ROUTINES_JAR_ITEM__ROUTINES_JAR_TYPE:
                setRoutinesJarType((RoutinesJarType)null);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case PropertiesPackage.ROUTINES_JAR_ITEM__ROUTINES_JAR_TYPE:
                return routinesJarType != null;
        }
        return super.eIsSet(featureID);
    }

} //RoutinesJarItemImpl
