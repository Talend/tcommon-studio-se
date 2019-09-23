/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.cwm.constants;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.talend.cwm.constants.ConstantsPackage
 * @generated
 */
public interface ConstantsFactory extends EFactory {

    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    ConstantsFactory eINSTANCE = org.talend.cwm.constants.impl.ConstantsFactoryImpl.init();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    ConstantsPackage getConstantsPackage();

} //ConstantsFactory
