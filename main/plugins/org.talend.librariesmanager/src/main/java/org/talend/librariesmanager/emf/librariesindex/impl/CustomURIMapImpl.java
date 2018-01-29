/**
 */
package org.talend.librariesmanager.emf.librariesindex.impl;

import java.util.Collection;
import java.util.Map;

import java.util.Map.Entry;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;
import org.talend.librariesmanager.emf.librariesindex.CustomURIMap;
import org.talend.librariesmanager.emf.librariesindex.LibrariesindexPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Custom URI Map</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.librariesmanager.emf.librariesindex.impl.CustomURIMapImpl#getUriMap <em>Uri Map</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CustomURIMapImpl extends EObjectImpl implements CustomURIMap {
    /**
     * The cached value of the '{@link #getUriMap() <em>Uri Map</em>}' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUriMap()
     * @generated
     * @ordered
     */
    protected EMap<String, String> uriMap;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CustomURIMapImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return LibrariesindexPackage.Literals.CUSTOM_URI_MAP;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EMap<String, String> getUriMap() {
        if (uriMap == null) {
            uriMap = new EcoreEMap<String,String>(LibrariesindexPackage.Literals.JAR_TO_RELATIVE_PATH, jarToRelativePathImpl.class, this, LibrariesindexPackage.CUSTOM_URI_MAP__URI_MAP);
        }
        return uriMap;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case LibrariesindexPackage.CUSTOM_URI_MAP__URI_MAP:
                return ((InternalEList<?>)getUriMap()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case LibrariesindexPackage.CUSTOM_URI_MAP__URI_MAP:
                if (coreType) return getUriMap();
                else return getUriMap().map();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case LibrariesindexPackage.CUSTOM_URI_MAP__URI_MAP:
                ((EStructuralFeature.Setting)getUriMap()).set(newValue);
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
            case LibrariesindexPackage.CUSTOM_URI_MAP__URI_MAP:
                getUriMap().clear();
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
            case LibrariesindexPackage.CUSTOM_URI_MAP__URI_MAP:
                return uriMap != null && !uriMap.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //CustomURIMapImpl
