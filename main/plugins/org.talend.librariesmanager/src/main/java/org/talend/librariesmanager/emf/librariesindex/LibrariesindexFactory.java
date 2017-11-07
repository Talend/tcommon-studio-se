/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.librariesmanager.emf.librariesindex;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.talend.librariesmanager.emf.librariesindex.LibrariesindexPackage
 * @generated
 */
public interface LibrariesindexFactory extends EFactory {
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    LibrariesindexFactory eINSTANCE = org.talend.librariesmanager.emf.librariesindex.impl.LibrariesindexFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Libraries Index</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Libraries Index</em>'.
     * @generated
     */
    LibrariesIndex createLibrariesIndex();

    /**
     * Returns a new object of class '<em>Custom URI Map</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Custom URI Map</em>'.
     * @generated
     */
    CustomURIMap createCustomURIMap();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    LibrariesindexPackage getLibrariesindexPackage();

} //LibrariesindexFactory
