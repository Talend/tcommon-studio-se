/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.librariesmanager.emf.librariesindex;

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
 * @see org.talend.librariesmanager.emf.librariesindex.LibrariesindexFactory
 * @model kind="package"
 * @generated
 */
public interface LibrariesindexPackage extends EPackage {
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "librariesindex";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "www.talend.com/librariesindex";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "librariesindex";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    LibrariesindexPackage eINSTANCE = org.talend.librariesmanager.emf.librariesindex.impl.LibrariesindexPackageImpl.init();

    /**
     * The meta object id for the '{@link org.talend.librariesmanager.emf.librariesindex.impl.LibrariesIndexImpl <em>Libraries Index</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.librariesmanager.emf.librariesindex.impl.LibrariesIndexImpl
     * @see org.talend.librariesmanager.emf.librariesindex.impl.LibrariesindexPackageImpl#getLibrariesIndex()
     * @generated
     */
    int LIBRARIES_INDEX = 0;

    /**
     * The feature id for the '<em><b>Initialized</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LIBRARIES_INDEX__INITIALIZED = 0;

    /**
     * The feature id for the '<em><b>Jars To Relative Path</b></em>' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LIBRARIES_INDEX__JARS_TO_RELATIVE_PATH = 1;

    /**
     * The number of structural features of the '<em>Libraries Index</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LIBRARIES_INDEX_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link org.talend.librariesmanager.emf.librariesindex.impl.jarToRelativePathImpl <em>jar To Relative Path</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.librariesmanager.emf.librariesindex.impl.jarToRelativePathImpl
     * @see org.talend.librariesmanager.emf.librariesindex.impl.LibrariesindexPackageImpl#getjarToRelativePath()
     * @generated
     */
    int JAR_TO_RELATIVE_PATH = 1;

    /**
     * The feature id for the '<em><b>Key</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JAR_TO_RELATIVE_PATH__KEY = 0;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JAR_TO_RELATIVE_PATH__VALUE = 1;

    /**
     * The number of structural features of the '<em>jar To Relative Path</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JAR_TO_RELATIVE_PATH_FEATURE_COUNT = 2;


    /**
     * The meta object id for the '{@link org.talend.librariesmanager.emf.librariesindex.impl.CustomURIMapImpl <em>Custom URI Map</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.librariesmanager.emf.librariesindex.impl.CustomURIMapImpl
     * @see org.talend.librariesmanager.emf.librariesindex.impl.LibrariesindexPackageImpl#getCustomURIMap()
     * @generated
     */
    int CUSTOM_URI_MAP = 2;

    /**
     * The feature id for the '<em><b>Uri Map</b></em>' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CUSTOM_URI_MAP__URI_MAP = 0;

    /**
     * The number of structural features of the '<em>Custom URI Map</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CUSTOM_URI_MAP_FEATURE_COUNT = 1;


    /**
     * Returns the meta object for class '{@link org.talend.librariesmanager.emf.librariesindex.LibrariesIndex <em>Libraries Index</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Libraries Index</em>'.
     * @see org.talend.librariesmanager.emf.librariesindex.LibrariesIndex
     * @generated
     */
    EClass getLibrariesIndex();

    /**
     * Returns the meta object for the attribute '{@link org.talend.librariesmanager.emf.librariesindex.LibrariesIndex#isInitialized <em>Initialized</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Initialized</em>'.
     * @see org.talend.librariesmanager.emf.librariesindex.LibrariesIndex#isInitialized()
     * @see #getLibrariesIndex()
     * @generated
     */
    EAttribute getLibrariesIndex_Initialized();

    /**
     * Returns the meta object for the map '{@link org.talend.librariesmanager.emf.librariesindex.LibrariesIndex#getJarsToRelativePath <em>Jars To Relative Path</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the map '<em>Jars To Relative Path</em>'.
     * @see org.talend.librariesmanager.emf.librariesindex.LibrariesIndex#getJarsToRelativePath()
     * @see #getLibrariesIndex()
     * @generated
     */
    EReference getLibrariesIndex_JarsToRelativePath();

    /**
     * Returns the meta object for class '{@link java.util.Map.Entry <em>jar To Relative Path</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>jar To Relative Path</em>'.
     * @see java.util.Map.Entry
     * @model keyDataType="org.eclipse.emf.ecore.EString"
     *        valueDataType="org.eclipse.emf.ecore.EString"
     * @generated
     */
    EClass getjarToRelativePath();

    /**
     * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Key</em>'.
     * @see java.util.Map.Entry
     * @see #getjarToRelativePath()
     * @generated
     */
    EAttribute getjarToRelativePath_Key();

    /**
     * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see java.util.Map.Entry
     * @see #getjarToRelativePath()
     * @generated
     */
    EAttribute getjarToRelativePath_Value();

    /**
     * Returns the meta object for class '{@link org.talend.librariesmanager.emf.librariesindex.CustomURIMap <em>Custom URI Map</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Custom URI Map</em>'.
     * @see org.talend.librariesmanager.emf.librariesindex.CustomURIMap
     * @generated
     */
    EClass getCustomURIMap();

    /**
     * Returns the meta object for the map '{@link org.talend.librariesmanager.emf.librariesindex.CustomURIMap#getUriMap <em>Uri Map</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the map '<em>Uri Map</em>'.
     * @see org.talend.librariesmanager.emf.librariesindex.CustomURIMap#getUriMap()
     * @see #getCustomURIMap()
     * @generated
     */
    EReference getCustomURIMap_UriMap();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    LibrariesindexFactory getLibrariesindexFactory();

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
         * The meta object literal for the '{@link org.talend.librariesmanager.emf.librariesindex.impl.LibrariesIndexImpl <em>Libraries Index</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.librariesmanager.emf.librariesindex.impl.LibrariesIndexImpl
         * @see org.talend.librariesmanager.emf.librariesindex.impl.LibrariesindexPackageImpl#getLibrariesIndex()
         * @generated
         */
        EClass LIBRARIES_INDEX = eINSTANCE.getLibrariesIndex();

        /**
         * The meta object literal for the '<em><b>Initialized</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute LIBRARIES_INDEX__INITIALIZED = eINSTANCE.getLibrariesIndex_Initialized();

        /**
         * The meta object literal for the '<em><b>Jars To Relative Path</b></em>' map feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference LIBRARIES_INDEX__JARS_TO_RELATIVE_PATH = eINSTANCE.getLibrariesIndex_JarsToRelativePath();

        /**
         * The meta object literal for the '{@link org.talend.librariesmanager.emf.librariesindex.impl.jarToRelativePathImpl <em>jar To Relative Path</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.librariesmanager.emf.librariesindex.impl.jarToRelativePathImpl
         * @see org.talend.librariesmanager.emf.librariesindex.impl.LibrariesindexPackageImpl#getjarToRelativePath()
         * @generated
         */
        EClass JAR_TO_RELATIVE_PATH = eINSTANCE.getjarToRelativePath();

        /**
         * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute JAR_TO_RELATIVE_PATH__KEY = eINSTANCE.getjarToRelativePath_Key();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute JAR_TO_RELATIVE_PATH__VALUE = eINSTANCE.getjarToRelativePath_Value();

        /**
         * The meta object literal for the '{@link org.talend.librariesmanager.emf.librariesindex.impl.CustomURIMapImpl <em>Custom URI Map</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.librariesmanager.emf.librariesindex.impl.CustomURIMapImpl
         * @see org.talend.librariesmanager.emf.librariesindex.impl.LibrariesindexPackageImpl#getCustomURIMap()
         * @generated
         */
        EClass CUSTOM_URI_MAP = eINSTANCE.getCustomURIMap();

        /**
         * The meta object literal for the '<em><b>Uri Map</b></em>' map feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CUSTOM_URI_MAP__URI_MAP = eINSTANCE.getCustomURIMap_UriMap();

    }

} //LibrariesindexPackage
