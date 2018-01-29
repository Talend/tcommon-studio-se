/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.librariesmanager.emf.librariesindex.impl;

import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.talend.librariesmanager.emf.librariesindex.CustomURIMap;
import org.talend.librariesmanager.emf.librariesindex.LibrariesIndex;
import org.talend.librariesmanager.emf.librariesindex.LibrariesindexFactory;
import org.talend.librariesmanager.emf.librariesindex.LibrariesindexPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class LibrariesindexPackageImpl extends EPackageImpl implements LibrariesindexPackage {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass librariesIndexEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass jarToRelativePathEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass customURIMapEClass = null;

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
     * @see org.talend.librariesmanager.emf.librariesindex.LibrariesindexPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private LibrariesindexPackageImpl() {
        super(eNS_URI, LibrariesindexFactory.eINSTANCE);
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
     * <p>This method is used to initialize {@link LibrariesindexPackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static LibrariesindexPackage init() {
        if (isInited) return (LibrariesindexPackage)EPackage.Registry.INSTANCE.getEPackage(LibrariesindexPackage.eNS_URI);

        // Obtain or create and register package
        LibrariesindexPackageImpl theLibrariesindexPackage = (LibrariesindexPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof LibrariesindexPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new LibrariesindexPackageImpl());

        isInited = true;

        // Create package meta-data objects
        theLibrariesindexPackage.createPackageContents();

        // Initialize created meta-data
        theLibrariesindexPackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theLibrariesindexPackage.freeze();

  
        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(LibrariesindexPackage.eNS_URI, theLibrariesindexPackage);
        return theLibrariesindexPackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getLibrariesIndex() {
        return librariesIndexEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLibrariesIndex_Initialized() {
        return (EAttribute)librariesIndexEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLibrariesIndex_JarsToRelativePath() {
        return (EReference)librariesIndexEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getjarToRelativePath() {
        return jarToRelativePathEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getjarToRelativePath_Key() {
        return (EAttribute)jarToRelativePathEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getjarToRelativePath_Value() {
        return (EAttribute)jarToRelativePathEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCustomURIMap() {
        return customURIMapEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCustomURIMap_UriMap() {
        return (EReference)customURIMapEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LibrariesindexFactory getLibrariesindexFactory() {
        return (LibrariesindexFactory)getEFactoryInstance();
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
        librariesIndexEClass = createEClass(LIBRARIES_INDEX);
        createEAttribute(librariesIndexEClass, LIBRARIES_INDEX__INITIALIZED);
        createEReference(librariesIndexEClass, LIBRARIES_INDEX__JARS_TO_RELATIVE_PATH);

        jarToRelativePathEClass = createEClass(JAR_TO_RELATIVE_PATH);
        createEAttribute(jarToRelativePathEClass, JAR_TO_RELATIVE_PATH__KEY);
        createEAttribute(jarToRelativePathEClass, JAR_TO_RELATIVE_PATH__VALUE);

        customURIMapEClass = createEClass(CUSTOM_URI_MAP);
        createEReference(customURIMapEClass, CUSTOM_URI_MAP__URI_MAP);
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

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes

        // Initialize classes and features; add operations and parameters
        initEClass(librariesIndexEClass, LibrariesIndex.class, "LibrariesIndex", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getLibrariesIndex_Initialized(), ecorePackage.getEBoolean(), "initialized", null, 0, 1, LibrariesIndex.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getLibrariesIndex_JarsToRelativePath(), this.getjarToRelativePath(), null, "jarsToRelativePath", null, 0, -1, LibrariesIndex.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(jarToRelativePathEClass, Map.Entry.class, "jarToRelativePath", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getjarToRelativePath_Key(), ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getjarToRelativePath_Value(), ecorePackage.getEString(), "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(customURIMapEClass, CustomURIMap.class, "CustomURIMap", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCustomURIMap_UriMap(), this.getjarToRelativePath(), null, "uriMap", null, 0, -1, CustomURIMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

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
          (jarToRelativePathEClass, 
           source, 
           new String[] {
           });
    }

} //LibrariesindexPackageImpl
