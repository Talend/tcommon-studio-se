/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.core.model.component_cache.impl;

import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.talend.core.model.component_cache.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ComponentCacheFactoryImpl extends EFactoryImpl implements ComponentCacheFactory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static ComponentCacheFactory init() {
        try {
            ComponentCacheFactory theComponentCacheFactory = (ComponentCacheFactory)EPackage.Registry.INSTANCE.getEFactory(ComponentCachePackage.eNS_URI);
            if (theComponentCacheFactory != null) {
                return theComponentCacheFactory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new ComponentCacheFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ComponentCacheFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            case ComponentCachePackage.COMPONENTS_CACHE: return createComponentsCache();
            case ComponentCachePackage.COMPONENT_INFO: return createComponentInfo();
            case ComponentCachePackage.COMPONENT_ENTRY_MAP: return (EObject)createComponentEntryMap();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ComponentsCache createComponentsCache() {
        ComponentsCacheImpl componentsCache = new ComponentsCacheImpl();
        return componentsCache;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ComponentInfo createComponentInfo() {
        ComponentInfoImpl componentInfo = new ComponentInfoImpl();
        return componentInfo;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Map.Entry<String, EList<ComponentInfo>> createComponentEntryMap() {
        ComponentEntryMapImpl componentEntryMap = new ComponentEntryMapImpl();
        return componentEntryMap;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ComponentCachePackage getComponentCachePackage() {
        return (ComponentCachePackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static ComponentCachePackage getPackage() {
        return ComponentCachePackage.eINSTANCE;
    }

} //ComponentCacheFactoryImpl
