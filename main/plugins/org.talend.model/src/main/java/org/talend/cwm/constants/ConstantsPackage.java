/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.cwm.constants;

import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;

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
 * @see org.talend.cwm.constants.ConstantsFactory
 * @model kind="package"
 * @generated
 */
public interface ConstantsPackage extends EPackage {

    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "constants";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://www.talend.org/cwm/constants/constants/2010";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "constants";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    ConstantsPackage eINSTANCE = org.talend.cwm.constants.impl.ConstantsPackageImpl.init();

    /**
     * The meta object id for the '{@link org.talend.cwm.constants.DevelopmentStatus <em>Development Status</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.cwm.constants.DevelopmentStatus
     * @see org.talend.cwm.constants.impl.ConstantsPackageImpl#getDevelopmentStatus()
     * @generated
     */
    int DEVELOPMENT_STATUS = 0;

    /**
     * Returns the meta object for enum '{@link org.talend.cwm.constants.DevelopmentStatus <em>Development Status</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Development Status</em>'.
     * @see org.talend.cwm.constants.DevelopmentStatus
     * @generated
     */
    EEnum getDevelopmentStatus();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    ConstantsFactory getConstantsFactory();

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
         * The meta object literal for the '{@link org.talend.cwm.constants.DevelopmentStatus <em>Development Status</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.cwm.constants.DevelopmentStatus
         * @see org.talend.cwm.constants.impl.ConstantsPackageImpl#getDevelopmentStatus()
         * @generated
         */
        EEnum DEVELOPMENT_STATUS = eINSTANCE.getDevelopmentStatus();

    }

} //ConstantsPackage
