/**
 */
package org.talend.core.model.resources;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import org.talend.core.model.properties.PropertiesPackage;

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
 * @see org.talend.core.model.resources.ResourcesFactory
 * @model kind="package"
 * @generated
 */
public interface ResourcesPackage extends EPackage {
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "resources";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://www.talend.org/ResourcesProperties";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "ResourcesProperties";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    ResourcesPackage eINSTANCE = org.talend.core.model.resources.impl.ResourcesPackageImpl.init();

    /**
     * The meta object id for the '{@link org.talend.core.model.resources.impl.ResourceItemImpl <em>Resource Item</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.core.model.resources.impl.ResourceItemImpl
     * @see org.talend.core.model.resources.impl.ResourcesPackageImpl#getResourceItem()
     * @generated
     */
    int RESOURCE_ITEM = 0;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE_ITEM__PROPERTY = PropertiesPackage.FILE_ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE_ITEM__STATE = PropertiesPackage.FILE_ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE_ITEM__PARENT = PropertiesPackage.FILE_ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE_ITEM__REFERENCE_RESOURCES = PropertiesPackage.FILE_ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE_ITEM__FILE_EXTENSION = PropertiesPackage.FILE_ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE_ITEM__NEED_VERSION = PropertiesPackage.FILE_ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE_ITEM__NAME = PropertiesPackage.FILE_ITEM__NAME;

    /**
     * The feature id for the '<em><b>Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE_ITEM__EXTENSION = PropertiesPackage.FILE_ITEM__EXTENSION;

    /**
     * The feature id for the '<em><b>Content</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE_ITEM__CONTENT = PropertiesPackage.FILE_ITEM__CONTENT;

    /**
     * The feature id for the '<em><b>Binding Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE_ITEM__BINDING_EXTENSION = PropertiesPackage.FILE_ITEM_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Resource Item</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE_ITEM_FEATURE_COUNT = PropertiesPackage.FILE_ITEM_FEATURE_COUNT + 1;


    /**
     * Returns the meta object for class '{@link org.talend.core.model.resources.ResourceItem <em>Resource Item</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Resource Item</em>'.
     * @see org.talend.core.model.resources.ResourceItem
     * @generated
     */
    EClass getResourceItem();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.resources.ResourceItem#getBindingExtension <em>Binding Extension</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Binding Extension</em>'.
     * @see org.talend.core.model.resources.ResourceItem#getBindingExtension()
     * @see #getResourceItem()
     * @generated
     */
    EAttribute getResourceItem_BindingExtension();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    ResourcesFactory getResourcesFactory();

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
         * The meta object literal for the '{@link org.talend.core.model.resources.impl.ResourceItemImpl <em>Resource Item</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.core.model.resources.impl.ResourceItemImpl
         * @see org.talend.core.model.resources.impl.ResourcesPackageImpl#getResourceItem()
         * @generated
         */
        EClass RESOURCE_ITEM = eINSTANCE.getResourceItem();

        /**
         * The meta object literal for the '<em><b>Binding Extension</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute RESOURCE_ITEM__BINDING_EXTENSION = eINSTANCE.getResourceItem_BindingExtension();

    }

} //ResourcesPackage
