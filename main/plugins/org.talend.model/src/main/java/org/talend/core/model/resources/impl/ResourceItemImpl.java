/**
 */
package org.talend.core.model.resources.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.talend.core.model.properties.impl.FileItemImpl;

import org.talend.core.model.resources.ResourceItem;
import org.talend.core.model.resources.ResourcesPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Resource Item</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.resources.impl.ResourceItemImpl#getBindingExtension <em>Binding Extension</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ResourceItemImpl extends FileItemImpl implements ResourceItem {
    /**
     * The default value of the '{@link #getBindingExtension() <em>Binding Extension</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBindingExtension()
     * @generated
     * @ordered
     */
    protected static final String BINDING_EXTENSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getBindingExtension() <em>Binding Extension</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBindingExtension()
     * @generated
     * @ordered
     */
    protected String bindingExtension = BINDING_EXTENSION_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ResourceItemImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ResourcesPackage.Literals.RESOURCE_ITEM;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getBindingExtension() {
        return bindingExtension;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBindingExtension(String newBindingExtension) {
        String oldBindingExtension = bindingExtension;
        bindingExtension = newBindingExtension;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ResourcesPackage.RESOURCE_ITEM__BINDING_EXTENSION, oldBindingExtension, bindingExtension));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case ResourcesPackage.RESOURCE_ITEM__BINDING_EXTENSION:
                return getBindingExtension();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case ResourcesPackage.RESOURCE_ITEM__BINDING_EXTENSION:
                setBindingExtension((String)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case ResourcesPackage.RESOURCE_ITEM__BINDING_EXTENSION:
                setBindingExtension(BINDING_EXTENSION_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case ResourcesPackage.RESOURCE_ITEM__BINDING_EXTENSION:
                return BINDING_EXTENSION_EDEFAULT == null ? bindingExtension != null : !BINDING_EXTENSION_EDEFAULT.equals(bindingExtension);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (bindingExtension: ");
        result.append(bindingExtension);
        result.append(')');
        return result.toString();
    }

} //ResourceItemImpl
