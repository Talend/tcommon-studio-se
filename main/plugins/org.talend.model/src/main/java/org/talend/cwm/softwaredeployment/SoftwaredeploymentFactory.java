/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.cwm.softwaredeployment;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.talend.cwm.softwaredeployment.SoftwaredeploymentPackage
 * @generated
 */
public interface SoftwaredeploymentFactory extends EFactory {

    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    SoftwaredeploymentFactory eINSTANCE = org.talend.cwm.softwaredeployment.impl.SoftwaredeploymentFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Td Data Manager</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Td Data Manager</em>'.
     * @generated
     */
    TdDataManager createTdDataManager();

    /**
     * Returns a new object of class '<em>Td Software System</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Td Software System</em>'.
     * @generated
     */
    TdSoftwareSystem createTdSoftwareSystem();

    /**
     * Returns a new object of class '<em>Td Machine</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Td Machine</em>'.
     * @generated
     */
    TdMachine createTdMachine();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    SoftwaredeploymentPackage getSoftwaredeploymentPackage();

} //SoftwaredeploymentFactory
