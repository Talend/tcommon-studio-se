/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.cwm.softwaredeployment;

import org.eclipse.emf.ecore.EClass;
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
 * @see org.talend.cwm.softwaredeployment.SoftwaredeploymentFactory
 * @model kind="package"
 * @generated
 */
public interface SoftwaredeploymentPackage extends EPackage {

    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "softwaredeployment";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://www.talend.org/cwm/foundation/softwaredeployment/2010";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "softwaredeployment";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    SoftwaredeploymentPackage eINSTANCE = org.talend.cwm.softwaredeployment.impl.SoftwaredeploymentPackageImpl.init();

    /**
     * The meta object id for the '{@link org.talend.cwm.softwaredeployment.impl.TdDataManagerImpl <em>Td Data Manager</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.cwm.softwaredeployment.impl.TdDataManagerImpl
     * @see org.talend.cwm.softwaredeployment.impl.SoftwaredeploymentPackageImpl#getTdDataManager()
     * @generated
     */
    int TD_DATA_MANAGER = 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_DATA_MANAGER__NAME = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.DATA_MANAGER__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_DATA_MANAGER__VISIBILITY = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.DATA_MANAGER__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_DATA_MANAGER__CLIENT_DEPENDENCY = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.DATA_MANAGER__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_DATA_MANAGER__SUPPLIER_DEPENDENCY = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.DATA_MANAGER__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_DATA_MANAGER__CONSTRAINT = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.DATA_MANAGER__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_DATA_MANAGER__NAMESPACE = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.DATA_MANAGER__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_DATA_MANAGER__IMPORTER = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.DATA_MANAGER__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_DATA_MANAGER__STEREOTYPE = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.DATA_MANAGER__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_DATA_MANAGER__TAGGED_VALUE = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.DATA_MANAGER__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_DATA_MANAGER__DOCUMENT = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.DATA_MANAGER__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_DATA_MANAGER__DESCRIPTION = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.DATA_MANAGER__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_DATA_MANAGER__RESPONSIBLE_PARTY = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.DATA_MANAGER__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_DATA_MANAGER__ELEMENT_NODE = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.DATA_MANAGER__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_DATA_MANAGER__SET = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.DATA_MANAGER__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_DATA_MANAGER__RENDERED_OBJECT = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.DATA_MANAGER__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_DATA_MANAGER__VOCABULARY_ELEMENT = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.DATA_MANAGER__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_DATA_MANAGER__MEASUREMENT = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.DATA_MANAGER__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_DATA_MANAGER__CHANGE_REQUEST = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.DATA_MANAGER__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_DATA_MANAGER__DASDL_PROPERTY = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.DATA_MANAGER__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Owned Element</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_DATA_MANAGER__OWNED_ELEMENT = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.DATA_MANAGER__OWNED_ELEMENT;

    /**
     * The feature id for the '<em><b>Imported Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_DATA_MANAGER__IMPORTED_ELEMENT = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.DATA_MANAGER__IMPORTED_ELEMENT;

    /**
     * The feature id for the '<em><b>Data Manager</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_DATA_MANAGER__DATA_MANAGER = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.DATA_MANAGER__DATA_MANAGER;

    /**
     * The feature id for the '<em><b>Pathname</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_DATA_MANAGER__PATHNAME = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.DATA_MANAGER__PATHNAME;

    /**
     * The feature id for the '<em><b>Machine</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_DATA_MANAGER__MACHINE = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.DATA_MANAGER__MACHINE;

    /**
     * The feature id for the '<em><b>Deployed Software System</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_DATA_MANAGER__DEPLOYED_SOFTWARE_SYSTEM = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.DATA_MANAGER__DEPLOYED_SOFTWARE_SYSTEM;

    /**
     * The feature id for the '<em><b>Component</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_DATA_MANAGER__COMPONENT = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.DATA_MANAGER__COMPONENT;

    /**
     * The feature id for the '<em><b>Is Case Sensitive</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_DATA_MANAGER__IS_CASE_SENSITIVE = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.DATA_MANAGER__IS_CASE_SENSITIVE;

    /**
     * The feature id for the '<em><b>Client Connection</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_DATA_MANAGER__CLIENT_CONNECTION = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.DATA_MANAGER__CLIENT_CONNECTION;

    /**
     * The feature id for the '<em><b>Data Package</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_DATA_MANAGER__DATA_PACKAGE = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.DATA_MANAGER__DATA_PACKAGE;

    /**
     * The number of structural features of the '<em>Td Data Manager</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_DATA_MANAGER_FEATURE_COUNT = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.DATA_MANAGER_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.cwm.softwaredeployment.impl.TdSoftwareSystemImpl <em>Td Software System</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.cwm.softwaredeployment.impl.TdSoftwareSystemImpl
     * @see org.talend.cwm.softwaredeployment.impl.SoftwaredeploymentPackageImpl#getTdSoftwareSystem()
     * @generated
     */
    int TD_SOFTWARE_SYSTEM = 1;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__NAME = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__VISIBILITY = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__CLIENT_DEPENDENCY = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__SUPPLIER_DEPENDENCY = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__CONSTRAINT = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__NAMESPACE = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__IMPORTER = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__STEREOTYPE = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__TAGGED_VALUE = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__DOCUMENT = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__DESCRIPTION = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__RESPONSIBLE_PARTY = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__ELEMENT_NODE = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__SET = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__RENDERED_OBJECT = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__VOCABULARY_ELEMENT = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__MEASUREMENT = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__CHANGE_REQUEST = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__DASDL_PROPERTY = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Owned Element</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__OWNED_ELEMENT = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__OWNED_ELEMENT;

    /**
     * The feature id for the '<em><b>Is Abstract</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__IS_ABSTRACT = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__IS_ABSTRACT;

    /**
     * The feature id for the '<em><b>Feature</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__FEATURE = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__FEATURE;

    /**
     * The feature id for the '<em><b>Structural Feature</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__STRUCTURAL_FEATURE = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__STRUCTURAL_FEATURE;

    /**
     * The feature id for the '<em><b>Parameter</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__PARAMETER = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__PARAMETER;

    /**
     * The feature id for the '<em><b>Generalization</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__GENERALIZATION = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__GENERALIZATION;

    /**
     * The feature id for the '<em><b>Specialization</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__SPECIALIZATION = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__SPECIALIZATION;

    /**
     * The feature id for the '<em><b>Instance</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__INSTANCE = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__INSTANCE;

    /**
     * The feature id for the '<em><b>Alias</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__ALIAS = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__ALIAS;

    /**
     * The feature id for the '<em><b>Expression Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__EXPRESSION_NODE = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__EXPRESSION_NODE;

    /**
     * The feature id for the '<em><b>Mapping From</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__MAPPING_FROM = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__MAPPING_FROM;

    /**
     * The feature id for the '<em><b>Mapping To</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__MAPPING_TO = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__MAPPING_TO;

    /**
     * The feature id for the '<em><b>Classifier Map</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__CLASSIFIER_MAP = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__CLASSIFIER_MAP;

    /**
     * The feature id for the '<em><b>Cf Map</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__CF_MAP = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__CF_MAP;

    /**
     * The feature id for the '<em><b>Domain</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__DOMAIN = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__DOMAIN;

    /**
     * The feature id for the '<em><b>Simple Dimension</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__SIMPLE_DIMENSION = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__SIMPLE_DIMENSION;

    /**
     * The feature id for the '<em><b>Imported Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__IMPORTED_ELEMENT = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__IMPORTED_ELEMENT;

    /**
     * The feature id for the '<em><b>Data Manager</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__DATA_MANAGER = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__DATA_MANAGER;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__TYPE = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__TYPE;

    /**
     * The feature id for the '<em><b>Subtype</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__SUBTYPE = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__SUBTYPE;

    /**
     * The feature id for the '<em><b>Supplier</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__SUPPLIER = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__SUPPLIER;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__VERSION = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__VERSION;

    /**
     * The feature id for the '<em><b>Deployment</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__DEPLOYMENT = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__DEPLOYMENT;

    /**
     * The feature id for the '<em><b>Typespace</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM__TYPESPACE = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM__TYPESPACE;

    /**
     * The number of structural features of the '<em>Td Software System</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SOFTWARE_SYSTEM_FEATURE_COUNT = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.SOFTWARE_SYSTEM_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.cwm.softwaredeployment.impl.TdMachineImpl <em>Td Machine</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.cwm.softwaredeployment.impl.TdMachineImpl
     * @see org.talend.cwm.softwaredeployment.impl.SoftwaredeploymentPackageImpl#getTdMachine()
     * @generated
     */
    int TD_MACHINE = 2;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_MACHINE__NAME = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.MACHINE__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_MACHINE__VISIBILITY = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.MACHINE__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_MACHINE__CLIENT_DEPENDENCY = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.MACHINE__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_MACHINE__SUPPLIER_DEPENDENCY = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.MACHINE__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_MACHINE__CONSTRAINT = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.MACHINE__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_MACHINE__NAMESPACE = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.MACHINE__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_MACHINE__IMPORTER = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.MACHINE__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_MACHINE__STEREOTYPE = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.MACHINE__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_MACHINE__TAGGED_VALUE = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.MACHINE__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_MACHINE__DOCUMENT = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.MACHINE__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_MACHINE__DESCRIPTION = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.MACHINE__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_MACHINE__RESPONSIBLE_PARTY = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.MACHINE__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_MACHINE__ELEMENT_NODE = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.MACHINE__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_MACHINE__SET = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.MACHINE__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_MACHINE__RENDERED_OBJECT = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.MACHINE__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_MACHINE__VOCABULARY_ELEMENT = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.MACHINE__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_MACHINE__MEASUREMENT = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.MACHINE__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_MACHINE__CHANGE_REQUEST = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.MACHINE__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_MACHINE__DASDL_PROPERTY = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.MACHINE__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Owned Element</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_MACHINE__OWNED_ELEMENT = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.MACHINE__OWNED_ELEMENT;

    /**
     * The feature id for the '<em><b>Ip Address</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_MACHINE__IP_ADDRESS = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.MACHINE__IP_ADDRESS;

    /**
     * The feature id for the '<em><b>Host Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_MACHINE__HOST_NAME = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.MACHINE__HOST_NAME;

    /**
     * The feature id for the '<em><b>Machine ID</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_MACHINE__MACHINE_ID = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.MACHINE__MACHINE_ID;

    /**
     * The feature id for the '<em><b>Deployed Component</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_MACHINE__DEPLOYED_COMPONENT = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.MACHINE__DEPLOYED_COMPONENT;

    /**
     * The feature id for the '<em><b>Site</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_MACHINE__SITE = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.MACHINE__SITE;

    /**
     * The number of structural features of the '<em>Td Machine</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_MACHINE_FEATURE_COUNT = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.MACHINE_FEATURE_COUNT + 0;

    /**
     * Returns the meta object for class '{@link org.talend.cwm.softwaredeployment.TdDataManager <em>Td Data Manager</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Td Data Manager</em>'.
     * @see org.talend.cwm.softwaredeployment.TdDataManager
     * @generated
     */
    EClass getTdDataManager();

    /**
     * Returns the meta object for class '{@link org.talend.cwm.softwaredeployment.TdSoftwareSystem <em>Td Software System</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Td Software System</em>'.
     * @see org.talend.cwm.softwaredeployment.TdSoftwareSystem
     * @generated
     */
    EClass getTdSoftwareSystem();

    /**
     * Returns the meta object for class '{@link org.talend.cwm.softwaredeployment.TdMachine <em>Td Machine</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Td Machine</em>'.
     * @see org.talend.cwm.softwaredeployment.TdMachine
     * @generated
     */
    EClass getTdMachine();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    SoftwaredeploymentFactory getSoftwaredeploymentFactory();

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
         * The meta object literal for the '{@link org.talend.cwm.softwaredeployment.impl.TdDataManagerImpl <em>Td Data Manager</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.cwm.softwaredeployment.impl.TdDataManagerImpl
         * @see org.talend.cwm.softwaredeployment.impl.SoftwaredeploymentPackageImpl#getTdDataManager()
         * @generated
         */
        EClass TD_DATA_MANAGER = eINSTANCE.getTdDataManager();

        /**
         * The meta object literal for the '{@link org.talend.cwm.softwaredeployment.impl.TdSoftwareSystemImpl <em>Td Software System</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.cwm.softwaredeployment.impl.TdSoftwareSystemImpl
         * @see org.talend.cwm.softwaredeployment.impl.SoftwaredeploymentPackageImpl#getTdSoftwareSystem()
         * @generated
         */
        EClass TD_SOFTWARE_SYSTEM = eINSTANCE.getTdSoftwareSystem();

        /**
         * The meta object literal for the '{@link org.talend.cwm.softwaredeployment.impl.TdMachineImpl <em>Td Machine</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.cwm.softwaredeployment.impl.TdMachineImpl
         * @see org.talend.cwm.softwaredeployment.impl.SoftwaredeploymentPackageImpl#getTdMachine()
         * @generated
         */
        EClass TD_MACHINE = eINSTANCE.getTdMachine();

    }

} //SoftwaredeploymentPackage
