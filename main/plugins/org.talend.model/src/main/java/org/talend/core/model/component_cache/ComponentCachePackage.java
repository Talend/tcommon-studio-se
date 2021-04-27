/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.core.model.component_cache;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.talend.core.model.component_cache.ComponentCacheFactory
 * @model kind="package"
 * @generated
 */
public interface ComponentCachePackage extends EPackage {
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "component_cache";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://www.talend.org/component_cache";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "component_cache";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    ComponentCachePackage eINSTANCE = org.talend.core.model.component_cache.impl.ComponentCachePackageImpl.init();

    /**
     * The meta object id for the '{@link org.talend.core.model.component_cache.impl.ComponentsCacheImpl <em>Components Cache</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.core.model.component_cache.impl.ComponentsCacheImpl
     * @see org.talend.core.model.component_cache.impl.ComponentCachePackageImpl#getComponentsCache()
     * @generated
     */
    int COMPONENTS_CACHE = 0;

    /**
     * The feature id for the '<em><b>Component Entry Map</b></em>' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENTS_CACHE__COMPONENT_ENTRY_MAP = 0;

    /**
     * The number of structural features of the '<em>Components Cache</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENTS_CACHE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link org.talend.core.model.component_cache.impl.ComponentInfoImpl <em>Component Info</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.core.model.component_cache.impl.ComponentInfoImpl
     * @see org.talend.core.model.component_cache.impl.ComponentCachePackageImpl#getComponentInfo()
     * @generated
     */
    int COMPONENT_INFO = 1;

    /**
     * The feature id for the '<em><b>Original Family Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_INFO__ORIGINAL_FAMILY_NAME = 0;

    /**
     * The feature id for the '<em><b>Plugin Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_INFO__PLUGIN_EXTENSION = 1;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_INFO__VERSION = 2;

    /**
     * The feature id for the '<em><b>Translated Family Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_INFO__TRANSLATED_FAMILY_NAME = 3;

    /**
     * The feature id for the '<em><b>Is Technical</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_INFO__IS_TECHNICAL = 4;

    /**
     * The feature id for the '<em><b>Plugin Dependencies</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_INFO__PLUGIN_DEPENDENCIES = 5;

    /**
     * The feature id for the '<em><b>Component Names</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_INFO__COMPONENT_NAMES = 6;

    /**
     * The feature id for the '<em><b>Import Type</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_INFO__IMPORT_TYPE = 7;

    /**
     * The feature id for the '<em><b>Is Visible In Component Definition</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_INFO__IS_VISIBLE_IN_COMPONENT_DEFINITION = 8;

    /**
     * The feature id for the '<em><b>Uri String</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_INFO__URI_STRING = 9;

    /**
     * The feature id for the '<em><b>Path Source</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_INFO__PATH_SOURCE = 10;

    /**
     * The feature id for the '<em><b>Repository Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_INFO__REPOSITORY_TYPE = 11;

    /**
     * The feature id for the '<em><b>Source Bundle Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_INFO__SOURCE_BUNDLE_NAME = 12;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_INFO__TYPE = 13;

    /**
     * The feature id for the '<em><b>Sha1</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_INFO__SHA1 = 14;

    /**
     * The feature id for the '<em><b>Long Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_INFO__LONG_NAME = 15;

    /**
     * The feature id for the '<em><b>Provider Class</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_INFO__PROVIDER_CLASS = 16;

    /**
     * The number of structural features of the '<em>Component Info</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_INFO_FEATURE_COUNT = 17;

    /**
     * The meta object id for the '{@link org.talend.core.model.component_cache.impl.ComponentEntryMapImpl <em>Component Entry Map</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.core.model.component_cache.impl.ComponentEntryMapImpl
     * @see org.talend.core.model.component_cache.impl.ComponentCachePackageImpl#getComponentEntryMap()
     * @generated
     */
    int COMPONENT_ENTRY_MAP = 2;

    /**
     * The feature id for the '<em><b>Key</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_ENTRY_MAP__KEY = 0;

    /**
     * The feature id for the '<em><b>Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_ENTRY_MAP__VALUE = 1;

    /**
     * The number of structural features of the '<em>Component Entry Map</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_ENTRY_MAP_FEATURE_COUNT = 2;


    /**
     * Returns the meta object for class '{@link org.talend.core.model.component_cache.ComponentsCache <em>Components Cache</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Components Cache</em>'.
     * @see org.talend.core.model.component_cache.ComponentsCache
     * @generated
     */
    EClass getComponentsCache();

    /**
     * Returns the meta object for the map '{@link org.talend.core.model.component_cache.ComponentsCache#getComponentEntryMap <em>Component Entry Map</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the map '<em>Component Entry Map</em>'.
     * @see org.talend.core.model.component_cache.ComponentsCache#getComponentEntryMap()
     * @see #getComponentsCache()
     * @generated
     */
    EReference getComponentsCache_ComponentEntryMap();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.component_cache.ComponentInfo <em>Component Info</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Component Info</em>'.
     * @see org.talend.core.model.component_cache.ComponentInfo
     * @generated
     */
    EClass getComponentInfo();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.component_cache.ComponentInfo#getOriginalFamilyName <em>Original Family Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Original Family Name</em>'.
     * @see org.talend.core.model.component_cache.ComponentInfo#getOriginalFamilyName()
     * @see #getComponentInfo()
     * @generated
     */
    EAttribute getComponentInfo_OriginalFamilyName();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.component_cache.ComponentInfo#getPluginExtension <em>Plugin Extension</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Plugin Extension</em>'.
     * @see org.talend.core.model.component_cache.ComponentInfo#getPluginExtension()
     * @see #getComponentInfo()
     * @generated
     */
    EAttribute getComponentInfo_PluginExtension();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.component_cache.ComponentInfo#getVersion <em>Version</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Version</em>'.
     * @see org.talend.core.model.component_cache.ComponentInfo#getVersion()
     * @see #getComponentInfo()
     * @generated
     */
    EAttribute getComponentInfo_Version();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.component_cache.ComponentInfo#getTranslatedFamilyName <em>Translated Family Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Translated Family Name</em>'.
     * @see org.talend.core.model.component_cache.ComponentInfo#getTranslatedFamilyName()
     * @see #getComponentInfo()
     * @generated
     */
    EAttribute getComponentInfo_TranslatedFamilyName();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.component_cache.ComponentInfo#isIsTechnical <em>Is Technical</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Is Technical</em>'.
     * @see org.talend.core.model.component_cache.ComponentInfo#isIsTechnical()
     * @see #getComponentInfo()
     * @generated
     */
    EAttribute getComponentInfo_IsTechnical();

    /**
     * Returns the meta object for the attribute list '{@link org.talend.core.model.component_cache.ComponentInfo#getPluginDependencies <em>Plugin Dependencies</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Plugin Dependencies</em>'.
     * @see org.talend.core.model.component_cache.ComponentInfo#getPluginDependencies()
     * @see #getComponentInfo()
     * @generated
     */
    EAttribute getComponentInfo_PluginDependencies();

    /**
     * Returns the meta object for the attribute list '{@link org.talend.core.model.component_cache.ComponentInfo#getComponentNames <em>Component Names</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Component Names</em>'.
     * @see org.talend.core.model.component_cache.ComponentInfo#getComponentNames()
     * @see #getComponentInfo()
     * @generated
     */
    EAttribute getComponentInfo_ComponentNames();

    /**
     * Returns the meta object for the containment reference list '{@link org.talend.core.model.component_cache.ComponentInfo#getImportType <em>Import Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Import Type</em>'.
     * @see org.talend.core.model.component_cache.ComponentInfo#getImportType()
     * @see #getComponentInfo()
     * @generated
     */
    EReference getComponentInfo_ImportType();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.component_cache.ComponentInfo#isIsVisibleInComponentDefinition <em>Is Visible In Component Definition</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Is Visible In Component Definition</em>'.
     * @see org.talend.core.model.component_cache.ComponentInfo#isIsVisibleInComponentDefinition()
     * @see #getComponentInfo()
     * @generated
     */
    EAttribute getComponentInfo_IsVisibleInComponentDefinition();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.component_cache.ComponentInfo#getUriString <em>Uri String</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Uri String</em>'.
     * @see org.talend.core.model.component_cache.ComponentInfo#getUriString()
     * @see #getComponentInfo()
     * @generated
     */
    EAttribute getComponentInfo_UriString();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.component_cache.ComponentInfo#getPathSource <em>Path Source</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Path Source</em>'.
     * @see org.talend.core.model.component_cache.ComponentInfo#getPathSource()
     * @see #getComponentInfo()
     * @generated
     */
    EAttribute getComponentInfo_PathSource();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.component_cache.ComponentInfo#getRepositoryType <em>Repository Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Repository Type</em>'.
     * @see org.talend.core.model.component_cache.ComponentInfo#getRepositoryType()
     * @see #getComponentInfo()
     * @generated
     */
    EAttribute getComponentInfo_RepositoryType();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.component_cache.ComponentInfo#getSourceBundleName <em>Source Bundle Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Source Bundle Name</em>'.
     * @see org.talend.core.model.component_cache.ComponentInfo#getSourceBundleName()
     * @see #getComponentInfo()
     * @generated
     */
    EAttribute getComponentInfo_SourceBundleName();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.component_cache.ComponentInfo#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Type</em>'.
     * @see org.talend.core.model.component_cache.ComponentInfo#getType()
     * @see #getComponentInfo()
     * @generated
     */
    EAttribute getComponentInfo_Type();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.component_cache.ComponentInfo#getSha1 <em>Sha1</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Sha1</em>'.
     * @see org.talend.core.model.component_cache.ComponentInfo#getSha1()
     * @see #getComponentInfo()
     * @generated
     */
    EAttribute getComponentInfo_Sha1();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.component_cache.ComponentInfo#getLongName <em>Long Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Long Name</em>'.
     * @see org.talend.core.model.component_cache.ComponentInfo#getLongName()
     * @see #getComponentInfo()
     * @generated
     */
    EAttribute getComponentInfo_LongName();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.component_cache.ComponentInfo#getProviderClass <em>Provider Class</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Provider Class</em>'.
     * @see org.talend.core.model.component_cache.ComponentInfo#getProviderClass()
     * @see #getComponentInfo()
     * @generated
     */
    EAttribute getComponentInfo_ProviderClass();

    /**
     * Returns the meta object for class '{@link java.util.Map.Entry <em>Component Entry Map</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Component Entry Map</em>'.
     * @see java.util.Map.Entry
     * @model keyDataType="org.eclipse.emf.ecore.EString"
     *        valueType="org.talend.core.model.component_cache.ComponentInfo" valueContainment="true" valueMany="true"
     *        annotation="MapEntry"
     * @generated
     */
    EClass getComponentEntryMap();

    /**
     * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Key</em>'.
     * @see java.util.Map.Entry
     * @see #getComponentEntryMap()
     * @generated
     */
    EAttribute getComponentEntryMap_Key();

    /**
     * Returns the meta object for the containment reference list '{@link java.util.Map.Entry <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Value</em>'.
     * @see java.util.Map.Entry
     * @see #getComponentEntryMap()
     * @generated
     */
    EReference getComponentEntryMap_Value();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    ComponentCacheFactory getComponentCacheFactory();

    /**
     * <!-- begin-user-doc -->
     * Defines literals for the meta objects that represent
     * <ul>
     *   <li>each class,</li>
     *   <li>each feature of each class,</li>
     *   <li>each enum,</li>
     *   <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
     * @generated
     */
    interface Literals {
        /**
         * The meta object literal for the '{@link org.talend.core.model.component_cache.impl.ComponentsCacheImpl <em>Components Cache</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.core.model.component_cache.impl.ComponentsCacheImpl
         * @see org.talend.core.model.component_cache.impl.ComponentCachePackageImpl#getComponentsCache()
         * @generated
         */
        EClass COMPONENTS_CACHE = eINSTANCE.getComponentsCache();

        /**
         * The meta object literal for the '<em><b>Component Entry Map</b></em>' map feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMPONENTS_CACHE__COMPONENT_ENTRY_MAP = eINSTANCE.getComponentsCache_ComponentEntryMap();

        /**
         * The meta object literal for the '{@link org.talend.core.model.component_cache.impl.ComponentInfoImpl <em>Component Info</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.core.model.component_cache.impl.ComponentInfoImpl
         * @see org.talend.core.model.component_cache.impl.ComponentCachePackageImpl#getComponentInfo()
         * @generated
         */
        EClass COMPONENT_INFO = eINSTANCE.getComponentInfo();

        /**
         * The meta object literal for the '<em><b>Original Family Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COMPONENT_INFO__ORIGINAL_FAMILY_NAME = eINSTANCE.getComponentInfo_OriginalFamilyName();

        /**
         * The meta object literal for the '<em><b>Plugin Extension</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COMPONENT_INFO__PLUGIN_EXTENSION = eINSTANCE.getComponentInfo_PluginExtension();

        /**
         * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COMPONENT_INFO__VERSION = eINSTANCE.getComponentInfo_Version();

        /**
         * The meta object literal for the '<em><b>Translated Family Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COMPONENT_INFO__TRANSLATED_FAMILY_NAME = eINSTANCE.getComponentInfo_TranslatedFamilyName();

        /**
         * The meta object literal for the '<em><b>Is Technical</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COMPONENT_INFO__IS_TECHNICAL = eINSTANCE.getComponentInfo_IsTechnical();

        /**
         * The meta object literal for the '<em><b>Plugin Dependencies</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COMPONENT_INFO__PLUGIN_DEPENDENCIES = eINSTANCE.getComponentInfo_PluginDependencies();

        /**
         * The meta object literal for the '<em><b>Component Names</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COMPONENT_INFO__COMPONENT_NAMES = eINSTANCE.getComponentInfo_ComponentNames();

        /**
         * The meta object literal for the '<em><b>Import Type</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMPONENT_INFO__IMPORT_TYPE = eINSTANCE.getComponentInfo_ImportType();

        /**
         * The meta object literal for the '<em><b>Is Visible In Component Definition</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COMPONENT_INFO__IS_VISIBLE_IN_COMPONENT_DEFINITION = eINSTANCE.getComponentInfo_IsVisibleInComponentDefinition();

        /**
         * The meta object literal for the '<em><b>Uri String</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COMPONENT_INFO__URI_STRING = eINSTANCE.getComponentInfo_UriString();

        /**
         * The meta object literal for the '<em><b>Path Source</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COMPONENT_INFO__PATH_SOURCE = eINSTANCE.getComponentInfo_PathSource();

        /**
         * The meta object literal for the '<em><b>Repository Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COMPONENT_INFO__REPOSITORY_TYPE = eINSTANCE.getComponentInfo_RepositoryType();

        /**
         * The meta object literal for the '<em><b>Source Bundle Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COMPONENT_INFO__SOURCE_BUNDLE_NAME = eINSTANCE.getComponentInfo_SourceBundleName();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COMPONENT_INFO__TYPE = eINSTANCE.getComponentInfo_Type();

        /**
         * The meta object literal for the '<em><b>Sha1</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COMPONENT_INFO__SHA1 = eINSTANCE.getComponentInfo_Sha1();

        /**
         * The meta object literal for the '<em><b>Long Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COMPONENT_INFO__LONG_NAME = eINSTANCE.getComponentInfo_LongName();

        /**
         * The meta object literal for the '<em><b>Provider Class</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COMPONENT_INFO__PROVIDER_CLASS = eINSTANCE.getComponentInfo_ProviderClass();

        /**
         * The meta object literal for the '{@link org.talend.core.model.component_cache.impl.ComponentEntryMapImpl <em>Component Entry Map</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.core.model.component_cache.impl.ComponentEntryMapImpl
         * @see org.talend.core.model.component_cache.impl.ComponentCachePackageImpl#getComponentEntryMap()
         * @generated
         */
        EClass COMPONENT_ENTRY_MAP = eINSTANCE.getComponentEntryMap();

        /**
         * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COMPONENT_ENTRY_MAP__KEY = eINSTANCE.getComponentEntryMap_Key();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMPONENT_ENTRY_MAP__VALUE = eINSTANCE.getComponentEntryMap_Value();

    }

} //ComponentCachePackage
