/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.librariesmanager.emf.librariesindex.impl;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.talend.librariesmanager.emf.librariesindex.*;
import org.talend.librariesmanager.emf.librariesindex.LibrariesIndex;
import org.talend.librariesmanager.emf.librariesindex.LibrariesindexFactory;
import org.talend.librariesmanager.emf.librariesindex.LibrariesindexPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class LibrariesindexFactoryImpl extends EFactoryImpl implements LibrariesindexFactory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static LibrariesindexFactory init() {
        try {
            LibrariesindexFactory theLibrariesindexFactory = (LibrariesindexFactory)EPackage.Registry.INSTANCE.getEFactory(LibrariesindexPackage.eNS_URI);
            if (theLibrariesindexFactory != null) {
                return theLibrariesindexFactory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new LibrariesindexFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LibrariesindexFactoryImpl() {
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
            case LibrariesindexPackage.LIBRARIES_INDEX: return createLibrariesIndex();
            case LibrariesindexPackage.JAR_TO_RELATIVE_PATH: return (EObject)createjarToRelativePath();
            case LibrariesindexPackage.CUSTOM_URI_MAP: return createCustomURIMap();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LibrariesIndex createLibrariesIndex() {
        LibrariesIndexImpl librariesIndex = new LibrariesIndexImpl();
        return librariesIndex;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Map.Entry<String, String> createjarToRelativePath() {
        jarToRelativePathImpl jarToRelativePath = new jarToRelativePathImpl();
        return jarToRelativePath;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CustomURIMap createCustomURIMap() {
        CustomURIMapImpl customURIMap = new CustomURIMapImpl();
        return customURIMap;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LibrariesindexPackage getLibrariesindexPackage() {
        return (LibrariesindexPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static LibrariesindexPackage getPackage() {
        return LibrariesindexPackage.eINSTANCE;
    }

} //LibrariesindexFactoryImpl
