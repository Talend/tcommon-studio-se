/**
 */
package org.talend.core.model.resources;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.talend.core.model.resources.ResourcesPackage
 * @generated
 */
public interface ResourcesFactory extends EFactory {
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    ResourcesFactory eINSTANCE = org.talend.core.model.resources.impl.ResourcesFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Resource Item</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Resource Item</em>'.
     * @generated
     */
    ResourceItem createResourceItem();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    ResourcesPackage getResourcesPackage();

} //ResourcesFactory
