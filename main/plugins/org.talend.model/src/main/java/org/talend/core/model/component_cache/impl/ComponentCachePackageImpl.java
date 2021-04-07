/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.core.model.component_cache.impl;

import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.talend.core.model.component_cache.ComponentCacheFactory;
import org.talend.core.model.component_cache.ComponentCachePackage;
import org.talend.core.model.component_cache.ComponentInfo;
import org.talend.core.model.component_cache.ComponentsCache;

import org.talend.designer.core.model.utils.emf.component.ComponentPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ComponentCachePackageImpl extends EPackageImpl implements ComponentCachePackage {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass componentsCacheEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass componentInfoEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass componentEntryMapEClass = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with
     * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
     * package URI value.
     * <p>Note: the correct way to create the package is via the static
     * factory method {@link #init init()}, which also performs
     * initialization of the package, or returns the registered package,
     * if one already exists.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see org.talend.core.model.component_cache.ComponentCachePackage#eNS_URI
     * @see #init()
     * @generated
     */
    private ComponentCachePackageImpl() {
        super(eNS_URI, ComponentCacheFactory.eINSTANCE);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
     *
     * <p>This method is used to initialize {@link ComponentCachePackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static ComponentCachePackage init() {
        if (isInited) return (ComponentCachePackage)EPackage.Registry.INSTANCE.getEPackage(ComponentCachePackage.eNS_URI);

        // Obtain or create and register package
        Object registeredComponentCachePackage = EPackage.Registry.INSTANCE.get(eNS_URI);
        ComponentCachePackageImpl theComponentCachePackage = registeredComponentCachePackage instanceof ComponentCachePackageImpl ? (ComponentCachePackageImpl)registeredComponentCachePackage : new ComponentCachePackageImpl();

        isInited = true;

        // Initialize simple dependencies
        ComponentPackage.eINSTANCE.eClass();

        // Create package meta-data objects
        theComponentCachePackage.createPackageContents();

        // Initialize created meta-data
        theComponentCachePackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theComponentCachePackage.freeze();

        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(ComponentCachePackage.eNS_URI, theComponentCachePackage);
        return theComponentCachePackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getComponentsCache() {
        return componentsCacheEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getComponentsCache_ComponentEntryMap() {
        return (EReference)componentsCacheEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getComponentInfo() {
        return componentInfoEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getComponentInfo_OriginalFamilyName() {
        return (EAttribute)componentInfoEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getComponentInfo_PluginExtension() {
        return (EAttribute)componentInfoEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getComponentInfo_Version() {
        return (EAttribute)componentInfoEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getComponentInfo_TranslatedFamilyName() {
        return (EAttribute)componentInfoEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getComponentInfo_IsTechnical() {
        return (EAttribute)componentInfoEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getComponentInfo_PluginDependencies() {
        return (EAttribute)componentInfoEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getComponentInfo_ComponentNames() {
        return (EAttribute)componentInfoEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getComponentInfo_ImportType() {
        return (EReference)componentInfoEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getComponentInfo_IsVisibleInComponentDefinition() {
        return (EAttribute)componentInfoEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getComponentInfo_UriString() {
        return (EAttribute)componentInfoEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getComponentInfo_PathSource() {
        return (EAttribute)componentInfoEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getComponentInfo_RepositoryType() {
        return (EAttribute)componentInfoEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getComponentInfo_SourceBundleName() {
        return (EAttribute)componentInfoEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getComponentInfo_Type() {
        return (EAttribute)componentInfoEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getComponentInfo_Sha1() {
        return (EAttribute)componentInfoEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getComponentInfo_LongName() {
        return (EAttribute)componentInfoEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getComponentInfo_ProviderClass() {
        return (EAttribute)componentInfoEClass.getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getComponentInfo_Owner() {
        return (EAttribute)componentInfoEClass.getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getComponentEntryMap() {
        return componentEntryMapEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getComponentEntryMap_Key() {
        return (EAttribute)componentEntryMapEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getComponentEntryMap_Value() {
        return (EReference)componentEntryMapEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ComponentCacheFactory getComponentCacheFactory() {
        return (ComponentCacheFactory)getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package.  This method is
     * guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void createPackageContents() {
        if (isCreated) return;
        isCreated = true;

        // Create classes and their features
        componentsCacheEClass = createEClass(COMPONENTS_CACHE);
        createEReference(componentsCacheEClass, COMPONENTS_CACHE__COMPONENT_ENTRY_MAP);

        componentInfoEClass = createEClass(COMPONENT_INFO);
        createEAttribute(componentInfoEClass, COMPONENT_INFO__ORIGINAL_FAMILY_NAME);
        createEAttribute(componentInfoEClass, COMPONENT_INFO__PLUGIN_EXTENSION);
        createEAttribute(componentInfoEClass, COMPONENT_INFO__VERSION);
        createEAttribute(componentInfoEClass, COMPONENT_INFO__TRANSLATED_FAMILY_NAME);
        createEAttribute(componentInfoEClass, COMPONENT_INFO__IS_TECHNICAL);
        createEAttribute(componentInfoEClass, COMPONENT_INFO__PLUGIN_DEPENDENCIES);
        createEAttribute(componentInfoEClass, COMPONENT_INFO__COMPONENT_NAMES);
        createEReference(componentInfoEClass, COMPONENT_INFO__IMPORT_TYPE);
        createEAttribute(componentInfoEClass, COMPONENT_INFO__IS_VISIBLE_IN_COMPONENT_DEFINITION);
        createEAttribute(componentInfoEClass, COMPONENT_INFO__URI_STRING);
        createEAttribute(componentInfoEClass, COMPONENT_INFO__PATH_SOURCE);
        createEAttribute(componentInfoEClass, COMPONENT_INFO__REPOSITORY_TYPE);
        createEAttribute(componentInfoEClass, COMPONENT_INFO__SOURCE_BUNDLE_NAME);
        createEAttribute(componentInfoEClass, COMPONENT_INFO__TYPE);
        createEAttribute(componentInfoEClass, COMPONENT_INFO__SHA1);
        createEAttribute(componentInfoEClass, COMPONENT_INFO__LONG_NAME);
        createEAttribute(componentInfoEClass, COMPONENT_INFO__PROVIDER_CLASS);
        createEAttribute(componentInfoEClass, COMPONENT_INFO__OWNER);

        componentEntryMapEClass = createEClass(COMPONENT_ENTRY_MAP);
        createEAttribute(componentEntryMapEClass, COMPONENT_ENTRY_MAP__KEY);
        createEReference(componentEntryMapEClass, COMPONENT_ENTRY_MAP__VALUE);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model.  This
     * method is guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void initializePackageContents() {
        if (isInitialized) return;
        isInitialized = true;

        // Initialize package
        setName(eNAME);
        setNsPrefix(eNS_PREFIX);
        setNsURI(eNS_URI);

        // Obtain other dependent packages
        ComponentPackage theComponentPackage = (ComponentPackage)EPackage.Registry.INSTANCE.getEPackage(ComponentPackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes

        // Initialize classes and features; add operations and parameters
        initEClass(componentsCacheEClass, ComponentsCache.class, "ComponentsCache", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getComponentsCache_ComponentEntryMap(), this.getComponentEntryMap(), null, "componentEntryMap", null, 0, -1, ComponentsCache.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(componentInfoEClass, ComponentInfo.class, "ComponentInfo", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getComponentInfo_OriginalFamilyName(), ecorePackage.getEString(), "originalFamilyName", "", 0, 1, ComponentInfo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getComponentInfo_PluginExtension(), ecorePackage.getEString(), "pluginExtension", null, 0, 1, ComponentInfo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getComponentInfo_Version(), ecorePackage.getEString(), "version", null, 0, 1, ComponentInfo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getComponentInfo_TranslatedFamilyName(), ecorePackage.getEString(), "translatedFamilyName", null, 0, 1, ComponentInfo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getComponentInfo_IsTechnical(), ecorePackage.getEBoolean(), "isTechnical", "false", 0, 1, ComponentInfo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getComponentInfo_PluginDependencies(), ecorePackage.getEString(), "PluginDependencies", null, 0, -1, ComponentInfo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getComponentInfo_ComponentNames(), ecorePackage.getEString(), "componentNames", null, 0, -1, ComponentInfo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getComponentInfo_ImportType(), theComponentPackage.getIMPORTType(), null, "importType", null, 0, -1, ComponentInfo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getComponentInfo_IsVisibleInComponentDefinition(), ecorePackage.getEBoolean(), "isVisibleInComponentDefinition", "true", 0, 1, ComponentInfo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getComponentInfo_UriString(), ecorePackage.getEString(), "uriString", null, 0, 1, ComponentInfo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getComponentInfo_PathSource(), ecorePackage.getEString(), "pathSource", null, 0, 1, ComponentInfo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getComponentInfo_RepositoryType(), ecorePackage.getEString(), "repositoryType", null, 0, 1, ComponentInfo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getComponentInfo_SourceBundleName(), ecorePackage.getEString(), "sourceBundleName", null, 0, 1, ComponentInfo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getComponentInfo_Type(), ecorePackage.getEString(), "type", null, 0, 1, ComponentInfo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getComponentInfo_Sha1(), ecorePackage.getEString(), "sha1", null, 0, 1, ComponentInfo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getComponentInfo_LongName(), ecorePackage.getEString(), "longName", null, 0, 1, ComponentInfo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getComponentInfo_ProviderClass(), ecorePackage.getEString(), "providerClass", null, 0, 1, ComponentInfo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getComponentInfo_Owner(), ecorePackage.getEInt(), "owner", "1", 0, 1, ComponentInfo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(componentEntryMapEClass, Map.Entry.class, "ComponentEntryMap", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getComponentEntryMap_Key(), ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getComponentEntryMap_Value(), this.getComponentInfo(), null, "value", null, 0, -1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        // Create resource
        createResource(eNS_URI);

        // Create annotations
        // MapEntry
        createMapEntryAnnotations();
    }

    /**
     * Initializes the annotations for <b>MapEntry</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createMapEntryAnnotations() {
        String source = "MapEntry";
        addAnnotation
          (componentEntryMapEClass,
           source,
           new String[] {
           });
    }

} //ComponentCachePackageImpl
