/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.cwm.relational;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import orgomg.cwm.objectmodel.core.CorePackage;

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
 * @see org.talend.cwm.relational.RelationalFactory
 * @model kind="package"
 * @generated
 */
public interface RelationalPackage extends EPackage {

    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "relational";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://www.talend.org/cwm/resource/relational/2010";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "relational";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    RelationalPackage eINSTANCE = org.talend.cwm.relational.impl.RelationalPackageImpl.init();

    /**
     * The meta object id for the '{@link org.talend.cwm.relational.impl.TdTableImpl <em>Td Table</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.cwm.relational.impl.TdTableImpl
     * @see org.talend.cwm.relational.impl.RelationalPackageImpl#getTdTable()
     * @generated
     */
    int TD_TABLE = 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__NAME = ConnectionPackage.METADATA_TABLE__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__VISIBILITY = ConnectionPackage.METADATA_TABLE__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__CLIENT_DEPENDENCY = ConnectionPackage.METADATA_TABLE__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__SUPPLIER_DEPENDENCY = ConnectionPackage.METADATA_TABLE__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__CONSTRAINT = ConnectionPackage.METADATA_TABLE__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__NAMESPACE = ConnectionPackage.METADATA_TABLE__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__IMPORTER = ConnectionPackage.METADATA_TABLE__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__STEREOTYPE = ConnectionPackage.METADATA_TABLE__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__TAGGED_VALUE = ConnectionPackage.METADATA_TABLE__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__DOCUMENT = ConnectionPackage.METADATA_TABLE__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__DESCRIPTION = ConnectionPackage.METADATA_TABLE__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__RESPONSIBLE_PARTY = ConnectionPackage.METADATA_TABLE__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__ELEMENT_NODE = ConnectionPackage.METADATA_TABLE__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__SET = ConnectionPackage.METADATA_TABLE__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__RENDERED_OBJECT = ConnectionPackage.METADATA_TABLE__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__VOCABULARY_ELEMENT = ConnectionPackage.METADATA_TABLE__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__MEASUREMENT = ConnectionPackage.METADATA_TABLE__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__CHANGE_REQUEST = ConnectionPackage.METADATA_TABLE__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__DASDL_PROPERTY = ConnectionPackage.METADATA_TABLE__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Properties</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__PROPERTIES = ConnectionPackage.METADATA_TABLE__PROPERTIES;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__ID = ConnectionPackage.METADATA_TABLE__ID;

    /**
     * The feature id for the '<em><b>Comment</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__COMMENT = ConnectionPackage.METADATA_TABLE__COMMENT;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__LABEL = ConnectionPackage.METADATA_TABLE__LABEL;

    /**
     * The feature id for the '<em><b>Read Only</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__READ_ONLY = ConnectionPackage.METADATA_TABLE__READ_ONLY;

    /**
     * The feature id for the '<em><b>Synchronised</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__SYNCHRONISED = ConnectionPackage.METADATA_TABLE__SYNCHRONISED;

    /**
     * The feature id for the '<em><b>Divergency</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__DIVERGENCY = ConnectionPackage.METADATA_TABLE__DIVERGENCY;

    /**
     * The feature id for the '<em><b>Owned Element</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__OWNED_ELEMENT = ConnectionPackage.METADATA_TABLE__OWNED_ELEMENT;

    /**
     * The feature id for the '<em><b>Is Abstract</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__IS_ABSTRACT = ConnectionPackage.METADATA_TABLE__IS_ABSTRACT;

    /**
     * The feature id for the '<em><b>Feature</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__FEATURE = ConnectionPackage.METADATA_TABLE__FEATURE;

    /**
     * The feature id for the '<em><b>Structural Feature</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__STRUCTURAL_FEATURE = ConnectionPackage.METADATA_TABLE__STRUCTURAL_FEATURE;

    /**
     * The feature id for the '<em><b>Parameter</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__PARAMETER = ConnectionPackage.METADATA_TABLE__PARAMETER;

    /**
     * The feature id for the '<em><b>Generalization</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__GENERALIZATION = ConnectionPackage.METADATA_TABLE__GENERALIZATION;

    /**
     * The feature id for the '<em><b>Specialization</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__SPECIALIZATION = ConnectionPackage.METADATA_TABLE__SPECIALIZATION;

    /**
     * The feature id for the '<em><b>Instance</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__INSTANCE = ConnectionPackage.METADATA_TABLE__INSTANCE;

    /**
     * The feature id for the '<em><b>Alias</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__ALIAS = ConnectionPackage.METADATA_TABLE__ALIAS;

    /**
     * The feature id for the '<em><b>Expression Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__EXPRESSION_NODE = ConnectionPackage.METADATA_TABLE__EXPRESSION_NODE;

    /**
     * The feature id for the '<em><b>Mapping From</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__MAPPING_FROM = ConnectionPackage.METADATA_TABLE__MAPPING_FROM;

    /**
     * The feature id for the '<em><b>Mapping To</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__MAPPING_TO = ConnectionPackage.METADATA_TABLE__MAPPING_TO;

    /**
     * The feature id for the '<em><b>Classifier Map</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__CLASSIFIER_MAP = ConnectionPackage.METADATA_TABLE__CLASSIFIER_MAP;

    /**
     * The feature id for the '<em><b>Cf Map</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__CF_MAP = ConnectionPackage.METADATA_TABLE__CF_MAP;

    /**
     * The feature id for the '<em><b>Domain</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__DOMAIN = ConnectionPackage.METADATA_TABLE__DOMAIN;

    /**
     * The feature id for the '<em><b>Simple Dimension</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__SIMPLE_DIMENSION = ConnectionPackage.METADATA_TABLE__SIMPLE_DIMENSION;

    /**
     * The feature id for the '<em><b>Index</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__INDEX = ConnectionPackage.METADATA_TABLE__INDEX;

    /**
     * The feature id for the '<em><b>Source Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__SOURCE_NAME = ConnectionPackage.METADATA_TABLE__SOURCE_NAME;

    /**
     * The feature id for the '<em><b>Table Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__TABLE_TYPE = ConnectionPackage.METADATA_TABLE__TABLE_TYPE;

    /**
     * The feature id for the '<em><b>Attached CDC</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__ATTACHED_CDC = ConnectionPackage.METADATA_TABLE__ATTACHED_CDC;

    /**
     * The feature id for the '<em><b>Activated CDC</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__ACTIVATED_CDC = ConnectionPackage.METADATA_TABLE__ACTIVATED_CDC;

    /**
     * The feature id for the '<em><b>Columns</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__COLUMNS = ConnectionPackage.METADATA_TABLE__COLUMNS;

    /**
     * The feature id for the '<em><b>Connection</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__CONNECTION = ConnectionPackage.METADATA_TABLE__CONNECTION;

    /**
     * The feature id for the '<em><b>Additional Properties</b></em>' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__ADDITIONAL_PROPERTIES = ConnectionPackage.METADATA_TABLE__ADDITIONAL_PROPERTIES;

    /**
     * The feature id for the '<em><b>Using Trigger</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__USING_TRIGGER = ConnectionPackage.METADATA_TABLE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Type</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__TYPE = ConnectionPackage.METADATA_TABLE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Option Scope Column</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__OPTION_SCOPE_COLUMN = ConnectionPackage.METADATA_TABLE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Is Temporary</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__IS_TEMPORARY = ConnectionPackage.METADATA_TABLE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Temporary Scope</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__TEMPORARY_SCOPE = ConnectionPackage.METADATA_TABLE_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Is System</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__IS_SYSTEM = ConnectionPackage.METADATA_TABLE_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Trigger</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE__TRIGGER = ConnectionPackage.METADATA_TABLE_FEATURE_COUNT + 6;

    /**
     * The number of structural features of the '<em>Td Table</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TABLE_FEATURE_COUNT = ConnectionPackage.METADATA_TABLE_FEATURE_COUNT + 7;

    /**
     * The meta object id for the '{@link org.talend.cwm.relational.impl.TdViewImpl <em>Td View</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.cwm.relational.impl.TdViewImpl
     * @see org.talend.cwm.relational.impl.RelationalPackageImpl#getTdView()
     * @generated
     */
    int TD_VIEW = 1;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__NAME = ConnectionPackage.METADATA_TABLE__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__VISIBILITY = ConnectionPackage.METADATA_TABLE__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__CLIENT_DEPENDENCY = ConnectionPackage.METADATA_TABLE__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__SUPPLIER_DEPENDENCY = ConnectionPackage.METADATA_TABLE__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__CONSTRAINT = ConnectionPackage.METADATA_TABLE__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__NAMESPACE = ConnectionPackage.METADATA_TABLE__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__IMPORTER = ConnectionPackage.METADATA_TABLE__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__STEREOTYPE = ConnectionPackage.METADATA_TABLE__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__TAGGED_VALUE = ConnectionPackage.METADATA_TABLE__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__DOCUMENT = ConnectionPackage.METADATA_TABLE__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__DESCRIPTION = ConnectionPackage.METADATA_TABLE__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__RESPONSIBLE_PARTY = ConnectionPackage.METADATA_TABLE__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__ELEMENT_NODE = ConnectionPackage.METADATA_TABLE__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__SET = ConnectionPackage.METADATA_TABLE__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__RENDERED_OBJECT = ConnectionPackage.METADATA_TABLE__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__VOCABULARY_ELEMENT = ConnectionPackage.METADATA_TABLE__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__MEASUREMENT = ConnectionPackage.METADATA_TABLE__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__CHANGE_REQUEST = ConnectionPackage.METADATA_TABLE__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__DASDL_PROPERTY = ConnectionPackage.METADATA_TABLE__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Properties</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__PROPERTIES = ConnectionPackage.METADATA_TABLE__PROPERTIES;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__ID = ConnectionPackage.METADATA_TABLE__ID;

    /**
     * The feature id for the '<em><b>Comment</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__COMMENT = ConnectionPackage.METADATA_TABLE__COMMENT;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__LABEL = ConnectionPackage.METADATA_TABLE__LABEL;

    /**
     * The feature id for the '<em><b>Read Only</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__READ_ONLY = ConnectionPackage.METADATA_TABLE__READ_ONLY;

    /**
     * The feature id for the '<em><b>Synchronised</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__SYNCHRONISED = ConnectionPackage.METADATA_TABLE__SYNCHRONISED;

    /**
     * The feature id for the '<em><b>Divergency</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__DIVERGENCY = ConnectionPackage.METADATA_TABLE__DIVERGENCY;

    /**
     * The feature id for the '<em><b>Owned Element</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__OWNED_ELEMENT = ConnectionPackage.METADATA_TABLE__OWNED_ELEMENT;

    /**
     * The feature id for the '<em><b>Is Abstract</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__IS_ABSTRACT = ConnectionPackage.METADATA_TABLE__IS_ABSTRACT;

    /**
     * The feature id for the '<em><b>Feature</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__FEATURE = ConnectionPackage.METADATA_TABLE__FEATURE;

    /**
     * The feature id for the '<em><b>Structural Feature</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__STRUCTURAL_FEATURE = ConnectionPackage.METADATA_TABLE__STRUCTURAL_FEATURE;

    /**
     * The feature id for the '<em><b>Parameter</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__PARAMETER = ConnectionPackage.METADATA_TABLE__PARAMETER;

    /**
     * The feature id for the '<em><b>Generalization</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__GENERALIZATION = ConnectionPackage.METADATA_TABLE__GENERALIZATION;

    /**
     * The feature id for the '<em><b>Specialization</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__SPECIALIZATION = ConnectionPackage.METADATA_TABLE__SPECIALIZATION;

    /**
     * The feature id for the '<em><b>Instance</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__INSTANCE = ConnectionPackage.METADATA_TABLE__INSTANCE;

    /**
     * The feature id for the '<em><b>Alias</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__ALIAS = ConnectionPackage.METADATA_TABLE__ALIAS;

    /**
     * The feature id for the '<em><b>Expression Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__EXPRESSION_NODE = ConnectionPackage.METADATA_TABLE__EXPRESSION_NODE;

    /**
     * The feature id for the '<em><b>Mapping From</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__MAPPING_FROM = ConnectionPackage.METADATA_TABLE__MAPPING_FROM;

    /**
     * The feature id for the '<em><b>Mapping To</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__MAPPING_TO = ConnectionPackage.METADATA_TABLE__MAPPING_TO;

    /**
     * The feature id for the '<em><b>Classifier Map</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__CLASSIFIER_MAP = ConnectionPackage.METADATA_TABLE__CLASSIFIER_MAP;

    /**
     * The feature id for the '<em><b>Cf Map</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__CF_MAP = ConnectionPackage.METADATA_TABLE__CF_MAP;

    /**
     * The feature id for the '<em><b>Domain</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__DOMAIN = ConnectionPackage.METADATA_TABLE__DOMAIN;

    /**
     * The feature id for the '<em><b>Simple Dimension</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__SIMPLE_DIMENSION = ConnectionPackage.METADATA_TABLE__SIMPLE_DIMENSION;

    /**
     * The feature id for the '<em><b>Index</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__INDEX = ConnectionPackage.METADATA_TABLE__INDEX;

    /**
     * The feature id for the '<em><b>Source Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__SOURCE_NAME = ConnectionPackage.METADATA_TABLE__SOURCE_NAME;

    /**
     * The feature id for the '<em><b>Table Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__TABLE_TYPE = ConnectionPackage.METADATA_TABLE__TABLE_TYPE;

    /**
     * The feature id for the '<em><b>Attached CDC</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__ATTACHED_CDC = ConnectionPackage.METADATA_TABLE__ATTACHED_CDC;

    /**
     * The feature id for the '<em><b>Activated CDC</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__ACTIVATED_CDC = ConnectionPackage.METADATA_TABLE__ACTIVATED_CDC;

    /**
     * The feature id for the '<em><b>Columns</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__COLUMNS = ConnectionPackage.METADATA_TABLE__COLUMNS;

    /**
     * The feature id for the '<em><b>Connection</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__CONNECTION = ConnectionPackage.METADATA_TABLE__CONNECTION;

    /**
     * The feature id for the '<em><b>Additional Properties</b></em>' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__ADDITIONAL_PROPERTIES = ConnectionPackage.METADATA_TABLE__ADDITIONAL_PROPERTIES;

    /**
     * The feature id for the '<em><b>Using Trigger</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__USING_TRIGGER = ConnectionPackage.METADATA_TABLE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Type</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__TYPE = ConnectionPackage.METADATA_TABLE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Option Scope Column</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__OPTION_SCOPE_COLUMN = ConnectionPackage.METADATA_TABLE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Is Read Only</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__IS_READ_ONLY = ConnectionPackage.METADATA_TABLE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Check Option</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__CHECK_OPTION = ConnectionPackage.METADATA_TABLE_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Query Expression</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW__QUERY_EXPRESSION = ConnectionPackage.METADATA_TABLE_FEATURE_COUNT + 5;

    /**
     * The number of structural features of the '<em>Td View</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_VIEW_FEATURE_COUNT = ConnectionPackage.METADATA_TABLE_FEATURE_COUNT + 6;

    /**
     * The meta object id for the '{@link org.talend.cwm.relational.impl.TdColumnImpl <em>Td Column</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.cwm.relational.impl.TdColumnImpl
     * @see org.talend.cwm.relational.impl.RelationalPackageImpl#getTdColumn()
     * @generated
     */
    int TD_COLUMN = 2;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__NAME = ConnectionPackage.METADATA_COLUMN__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__VISIBILITY = ConnectionPackage.METADATA_COLUMN__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__CLIENT_DEPENDENCY = ConnectionPackage.METADATA_COLUMN__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__SUPPLIER_DEPENDENCY = ConnectionPackage.METADATA_COLUMN__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__CONSTRAINT = ConnectionPackage.METADATA_COLUMN__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__NAMESPACE = ConnectionPackage.METADATA_COLUMN__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__IMPORTER = ConnectionPackage.METADATA_COLUMN__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__STEREOTYPE = ConnectionPackage.METADATA_COLUMN__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__TAGGED_VALUE = ConnectionPackage.METADATA_COLUMN__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__DOCUMENT = ConnectionPackage.METADATA_COLUMN__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__DESCRIPTION = ConnectionPackage.METADATA_COLUMN__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__RESPONSIBLE_PARTY = ConnectionPackage.METADATA_COLUMN__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__ELEMENT_NODE = ConnectionPackage.METADATA_COLUMN__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__SET = ConnectionPackage.METADATA_COLUMN__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__RENDERED_OBJECT = ConnectionPackage.METADATA_COLUMN__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__VOCABULARY_ELEMENT = ConnectionPackage.METADATA_COLUMN__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__MEASUREMENT = ConnectionPackage.METADATA_COLUMN__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__CHANGE_REQUEST = ConnectionPackage.METADATA_COLUMN__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__DASDL_PROPERTY = ConnectionPackage.METADATA_COLUMN__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Properties</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__PROPERTIES = ConnectionPackage.METADATA_COLUMN__PROPERTIES;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__ID = ConnectionPackage.METADATA_COLUMN__ID;

    /**
     * The feature id for the '<em><b>Comment</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__COMMENT = ConnectionPackage.METADATA_COLUMN__COMMENT;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__LABEL = ConnectionPackage.METADATA_COLUMN__LABEL;

    /**
     * The feature id for the '<em><b>Read Only</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__READ_ONLY = ConnectionPackage.METADATA_COLUMN__READ_ONLY;

    /**
     * The feature id for the '<em><b>Synchronised</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__SYNCHRONISED = ConnectionPackage.METADATA_COLUMN__SYNCHRONISED;

    /**
     * The feature id for the '<em><b>Divergency</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__DIVERGENCY = ConnectionPackage.METADATA_COLUMN__DIVERGENCY;

    /**
     * The feature id for the '<em><b>Owner Scope</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__OWNER_SCOPE = ConnectionPackage.METADATA_COLUMN__OWNER_SCOPE;

    /**
     * The feature id for the '<em><b>Owner</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__OWNER = ConnectionPackage.METADATA_COLUMN__OWNER;

    /**
     * The feature id for the '<em><b>Feature Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__FEATURE_NODE = ConnectionPackage.METADATA_COLUMN__FEATURE_NODE;

    /**
     * The feature id for the '<em><b>Feature Map</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__FEATURE_MAP = ConnectionPackage.METADATA_COLUMN__FEATURE_MAP;

    /**
     * The feature id for the '<em><b>Cf Map</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__CF_MAP = ConnectionPackage.METADATA_COLUMN__CF_MAP;

    /**
     * The feature id for the '<em><b>Changeability</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__CHANGEABILITY = ConnectionPackage.METADATA_COLUMN__CHANGEABILITY;

    /**
     * The feature id for the '<em><b>Multiplicity</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__MULTIPLICITY = ConnectionPackage.METADATA_COLUMN__MULTIPLICITY;

    /**
     * The feature id for the '<em><b>Ordering</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__ORDERING = ConnectionPackage.METADATA_COLUMN__ORDERING;

    /**
     * The feature id for the '<em><b>Target Scope</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__TARGET_SCOPE = ConnectionPackage.METADATA_COLUMN__TARGET_SCOPE;

    /**
     * The feature id for the '<em><b>Type</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__TYPE = ConnectionPackage.METADATA_COLUMN__TYPE;

    /**
     * The feature id for the '<em><b>Slot</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__SLOT = ConnectionPackage.METADATA_COLUMN__SLOT;

    /**
     * The feature id for the '<em><b>Discriminated Union</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__DISCRIMINATED_UNION = ConnectionPackage.METADATA_COLUMN__DISCRIMINATED_UNION;

    /**
     * The feature id for the '<em><b>Indexed Feature</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__INDEXED_FEATURE = ConnectionPackage.METADATA_COLUMN__INDEXED_FEATURE;

    /**
     * The feature id for the '<em><b>Key Relationship</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__KEY_RELATIONSHIP = ConnectionPackage.METADATA_COLUMN__KEY_RELATIONSHIP;

    /**
     * The feature id for the '<em><b>Unique Key</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__UNIQUE_KEY = ConnectionPackage.METADATA_COLUMN__UNIQUE_KEY;

    /**
     * The feature id for the '<em><b>Data Item</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__DATA_ITEM = ConnectionPackage.METADATA_COLUMN__DATA_ITEM;

    /**
     * The feature id for the '<em><b>Remap</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__REMAP = ConnectionPackage.METADATA_COLUMN__REMAP;

    /**
     * The feature id for the '<em><b>Initial Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__INITIAL_VALUE = ConnectionPackage.METADATA_COLUMN__INITIAL_VALUE;

    /**
     * The feature id for the '<em><b>Length</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__LENGTH = ConnectionPackage.METADATA_COLUMN__LENGTH;

    /**
     * The feature id for the '<em><b>Precision</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__PRECISION = ConnectionPackage.METADATA_COLUMN__PRECISION;

    /**
     * The feature id for the '<em><b>Scale</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__SCALE = ConnectionPackage.METADATA_COLUMN__SCALE;

    /**
     * The feature id for the '<em><b>Source Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__SOURCE_TYPE = ConnectionPackage.METADATA_COLUMN__SOURCE_TYPE;

    /**
     * The feature id for the '<em><b>Default Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__DEFAULT_VALUE = ConnectionPackage.METADATA_COLUMN__DEFAULT_VALUE;

    /**
     * The feature id for the '<em><b>Talend Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__TALEND_TYPE = ConnectionPackage.METADATA_COLUMN__TALEND_TYPE;

    /**
     * The feature id for the '<em><b>Key</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__KEY = ConnectionPackage.METADATA_COLUMN__KEY;

    /**
     * The feature id for the '<em><b>Nullable</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__NULLABLE = ConnectionPackage.METADATA_COLUMN__NULLABLE;

    /**
     * The feature id for the '<em><b>Table</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__TABLE = ConnectionPackage.METADATA_COLUMN__TABLE;

    /**
     * The feature id for the '<em><b>Original Field</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__ORIGINAL_FIELD = ConnectionPackage.METADATA_COLUMN__ORIGINAL_FIELD;

    /**
     * The feature id for the '<em><b>Pattern</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__PATTERN = ConnectionPackage.METADATA_COLUMN__PATTERN;

    /**
     * The feature id for the '<em><b>Display Field</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__DISPLAY_FIELD = ConnectionPackage.METADATA_COLUMN__DISPLAY_FIELD;

    /**
     * The feature id for the '<em><b>Original Length</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__ORIGINAL_LENGTH = ConnectionPackage.METADATA_COLUMN__ORIGINAL_LENGTH;

    /**
     * The feature id for the '<em><b>Related Entity</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__RELATED_ENTITY = ConnectionPackage.METADATA_COLUMN__RELATED_ENTITY;

    /**
     * The feature id for the '<em><b>Relationship Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__RELATIONSHIP_TYPE = ConnectionPackage.METADATA_COLUMN__RELATIONSHIP_TYPE;

    /**
     * The feature id for the '<em><b>Sql Data Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN__SQL_DATA_TYPE = ConnectionPackage.METADATA_COLUMN_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Td Column</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_COLUMN_FEATURE_COUNT = ConnectionPackage.METADATA_COLUMN_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.talend.cwm.relational.impl.TdSqlDataTypeImpl <em>Td Sql Data Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.cwm.relational.impl.TdSqlDataTypeImpl
     * @see org.talend.cwm.relational.impl.RelationalPackageImpl#getTdSqlDataType()
     * @generated
     */
    int TD_SQL_DATA_TYPE = 3;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__NAME = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__VISIBILITY = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__CLIENT_DEPENDENCY = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__SUPPLIER_DEPENDENCY = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__CONSTRAINT = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__NAMESPACE = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__IMPORTER = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__STEREOTYPE = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__TAGGED_VALUE = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__DOCUMENT = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__DESCRIPTION = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__RESPONSIBLE_PARTY = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__ELEMENT_NODE = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__SET = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__RENDERED_OBJECT = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__VOCABULARY_ELEMENT = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__MEASUREMENT = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__CHANGE_REQUEST = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__DASDL_PROPERTY = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Owned Element</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__OWNED_ELEMENT = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__OWNED_ELEMENT;

    /**
     * The feature id for the '<em><b>Is Abstract</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__IS_ABSTRACT = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__IS_ABSTRACT;

    /**
     * The feature id for the '<em><b>Feature</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__FEATURE = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__FEATURE;

    /**
     * The feature id for the '<em><b>Structural Feature</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__STRUCTURAL_FEATURE = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__STRUCTURAL_FEATURE;

    /**
     * The feature id for the '<em><b>Parameter</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__PARAMETER = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__PARAMETER;

    /**
     * The feature id for the '<em><b>Generalization</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__GENERALIZATION = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__GENERALIZATION;

    /**
     * The feature id for the '<em><b>Specialization</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__SPECIALIZATION = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__SPECIALIZATION;

    /**
     * The feature id for the '<em><b>Instance</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__INSTANCE = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__INSTANCE;

    /**
     * The feature id for the '<em><b>Alias</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__ALIAS = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__ALIAS;

    /**
     * The feature id for the '<em><b>Expression Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__EXPRESSION_NODE = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__EXPRESSION_NODE;

    /**
     * The feature id for the '<em><b>Mapping From</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__MAPPING_FROM = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__MAPPING_FROM;

    /**
     * The feature id for the '<em><b>Mapping To</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__MAPPING_TO = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__MAPPING_TO;

    /**
     * The feature id for the '<em><b>Classifier Map</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__CLASSIFIER_MAP = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__CLASSIFIER_MAP;

    /**
     * The feature id for the '<em><b>Cf Map</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__CF_MAP = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__CF_MAP;

    /**
     * The feature id for the '<em><b>Domain</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__DOMAIN = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__DOMAIN;

    /**
     * The feature id for the '<em><b>Simple Dimension</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__SIMPLE_DIMENSION = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__SIMPLE_DIMENSION;

    /**
     * The feature id for the '<em><b>Type Number</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__TYPE_NUMBER = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__TYPE_NUMBER;

    /**
     * The feature id for the '<em><b>Character Maximum Length</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__CHARACTER_MAXIMUM_LENGTH = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__CHARACTER_MAXIMUM_LENGTH;

    /**
     * The feature id for the '<em><b>Character Octet Length</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__CHARACTER_OCTET_LENGTH = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__CHARACTER_OCTET_LENGTH;

    /**
     * The feature id for the '<em><b>Numeric Precision</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__NUMERIC_PRECISION = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__NUMERIC_PRECISION;

    /**
     * The feature id for the '<em><b>Numeric Precision Radix</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__NUMERIC_PRECISION_RADIX = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__NUMERIC_PRECISION_RADIX;

    /**
     * The feature id for the '<em><b>Numeric Scale</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__NUMERIC_SCALE = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__NUMERIC_SCALE;

    /**
     * The feature id for the '<em><b>Date Time Precision</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__DATE_TIME_PRECISION = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__DATE_TIME_PRECISION;

    /**
     * The feature id for the '<em><b>Sql Distinct Type</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__SQL_DISTINCT_TYPE = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE__SQL_DISTINCT_TYPE;

    /**
     * The feature id for the '<em><b>Java Data Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__JAVA_DATA_TYPE = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Nullable</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__NULLABLE = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Unsigned Attribute</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__UNSIGNED_ATTRIBUTE = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Case Sensitive</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__CASE_SENSITIVE = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Auto Increment</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__AUTO_INCREMENT = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Local Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__LOCAL_TYPE_NAME = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Searchable</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE__SEARCHABLE = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE_FEATURE_COUNT + 6;

    /**
     * The number of structural features of the '<em>Td Sql Data Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_SQL_DATA_TYPE_FEATURE_COUNT = orgomg.cwm.resource.relational.RelationalPackage.SQL_SIMPLE_TYPE_FEATURE_COUNT + 7;

    /**
     * The meta object id for the '{@link org.talend.cwm.relational.impl.TdTriggerImpl <em>Td Trigger</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.cwm.relational.impl.TdTriggerImpl
     * @see org.talend.cwm.relational.impl.RelationalPackageImpl#getTdTrigger()
     * @generated
     */
    int TD_TRIGGER = 4;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TRIGGER__NAME = orgomg.cwm.resource.relational.RelationalPackage.TRIGGER__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TRIGGER__VISIBILITY = orgomg.cwm.resource.relational.RelationalPackage.TRIGGER__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TRIGGER__CLIENT_DEPENDENCY = orgomg.cwm.resource.relational.RelationalPackage.TRIGGER__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TRIGGER__SUPPLIER_DEPENDENCY = orgomg.cwm.resource.relational.RelationalPackage.TRIGGER__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TRIGGER__CONSTRAINT = orgomg.cwm.resource.relational.RelationalPackage.TRIGGER__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TRIGGER__NAMESPACE = orgomg.cwm.resource.relational.RelationalPackage.TRIGGER__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TRIGGER__IMPORTER = orgomg.cwm.resource.relational.RelationalPackage.TRIGGER__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TRIGGER__STEREOTYPE = orgomg.cwm.resource.relational.RelationalPackage.TRIGGER__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TRIGGER__TAGGED_VALUE = orgomg.cwm.resource.relational.RelationalPackage.TRIGGER__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TRIGGER__DOCUMENT = orgomg.cwm.resource.relational.RelationalPackage.TRIGGER__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TRIGGER__DESCRIPTION = orgomg.cwm.resource.relational.RelationalPackage.TRIGGER__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TRIGGER__RESPONSIBLE_PARTY = orgomg.cwm.resource.relational.RelationalPackage.TRIGGER__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TRIGGER__ELEMENT_NODE = orgomg.cwm.resource.relational.RelationalPackage.TRIGGER__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TRIGGER__SET = orgomg.cwm.resource.relational.RelationalPackage.TRIGGER__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TRIGGER__RENDERED_OBJECT = orgomg.cwm.resource.relational.RelationalPackage.TRIGGER__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TRIGGER__VOCABULARY_ELEMENT = orgomg.cwm.resource.relational.RelationalPackage.TRIGGER__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TRIGGER__MEASUREMENT = orgomg.cwm.resource.relational.RelationalPackage.TRIGGER__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TRIGGER__CHANGE_REQUEST = orgomg.cwm.resource.relational.RelationalPackage.TRIGGER__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TRIGGER__DASDL_PROPERTY = orgomg.cwm.resource.relational.RelationalPackage.TRIGGER__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Event Manipulation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TRIGGER__EVENT_MANIPULATION = orgomg.cwm.resource.relational.RelationalPackage.TRIGGER__EVENT_MANIPULATION;

    /**
     * The feature id for the '<em><b>Action Condition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TRIGGER__ACTION_CONDITION = orgomg.cwm.resource.relational.RelationalPackage.TRIGGER__ACTION_CONDITION;

    /**
     * The feature id for the '<em><b>Action Statement</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TRIGGER__ACTION_STATEMENT = orgomg.cwm.resource.relational.RelationalPackage.TRIGGER__ACTION_STATEMENT;

    /**
     * The feature id for the '<em><b>Action Orientation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TRIGGER__ACTION_ORIENTATION = orgomg.cwm.resource.relational.RelationalPackage.TRIGGER__ACTION_ORIENTATION;

    /**
     * The feature id for the '<em><b>Condition Timing</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TRIGGER__CONDITION_TIMING = orgomg.cwm.resource.relational.RelationalPackage.TRIGGER__CONDITION_TIMING;

    /**
     * The feature id for the '<em><b>Condition Reference New Table</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TRIGGER__CONDITION_REFERENCE_NEW_TABLE = orgomg.cwm.resource.relational.RelationalPackage.TRIGGER__CONDITION_REFERENCE_NEW_TABLE;

    /**
     * The feature id for the '<em><b>Condition Reference Old Table</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TRIGGER__CONDITION_REFERENCE_OLD_TABLE = orgomg.cwm.resource.relational.RelationalPackage.TRIGGER__CONDITION_REFERENCE_OLD_TABLE;

    /**
     * The feature id for the '<em><b>Used Column Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TRIGGER__USED_COLUMN_SET = orgomg.cwm.resource.relational.RelationalPackage.TRIGGER__USED_COLUMN_SET;

    /**
     * The feature id for the '<em><b>Table</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TRIGGER__TABLE = orgomg.cwm.resource.relational.RelationalPackage.TRIGGER__TABLE;

    /**
     * The number of structural features of the '<em>Td Trigger</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_TRIGGER_FEATURE_COUNT = orgomg.cwm.resource.relational.RelationalPackage.TRIGGER_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.cwm.relational.impl.TdProcedureImpl <em>Td Procedure</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.cwm.relational.impl.TdProcedureImpl
     * @see org.talend.cwm.relational.impl.RelationalPackageImpl#getTdProcedure()
     * @generated
     */
    int TD_PROCEDURE = 5;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_PROCEDURE__NAME = orgomg.cwm.resource.relational.RelationalPackage.PROCEDURE__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_PROCEDURE__VISIBILITY = orgomg.cwm.resource.relational.RelationalPackage.PROCEDURE__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_PROCEDURE__CLIENT_DEPENDENCY = orgomg.cwm.resource.relational.RelationalPackage.PROCEDURE__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_PROCEDURE__SUPPLIER_DEPENDENCY = orgomg.cwm.resource.relational.RelationalPackage.PROCEDURE__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_PROCEDURE__CONSTRAINT = orgomg.cwm.resource.relational.RelationalPackage.PROCEDURE__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_PROCEDURE__NAMESPACE = orgomg.cwm.resource.relational.RelationalPackage.PROCEDURE__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_PROCEDURE__IMPORTER = orgomg.cwm.resource.relational.RelationalPackage.PROCEDURE__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_PROCEDURE__STEREOTYPE = orgomg.cwm.resource.relational.RelationalPackage.PROCEDURE__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_PROCEDURE__TAGGED_VALUE = orgomg.cwm.resource.relational.RelationalPackage.PROCEDURE__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_PROCEDURE__DOCUMENT = orgomg.cwm.resource.relational.RelationalPackage.PROCEDURE__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_PROCEDURE__DESCRIPTION = orgomg.cwm.resource.relational.RelationalPackage.PROCEDURE__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_PROCEDURE__RESPONSIBLE_PARTY = orgomg.cwm.resource.relational.RelationalPackage.PROCEDURE__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_PROCEDURE__ELEMENT_NODE = orgomg.cwm.resource.relational.RelationalPackage.PROCEDURE__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_PROCEDURE__SET = orgomg.cwm.resource.relational.RelationalPackage.PROCEDURE__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_PROCEDURE__RENDERED_OBJECT = orgomg.cwm.resource.relational.RelationalPackage.PROCEDURE__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_PROCEDURE__VOCABULARY_ELEMENT = orgomg.cwm.resource.relational.RelationalPackage.PROCEDURE__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_PROCEDURE__MEASUREMENT = orgomg.cwm.resource.relational.RelationalPackage.PROCEDURE__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_PROCEDURE__CHANGE_REQUEST = orgomg.cwm.resource.relational.RelationalPackage.PROCEDURE__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_PROCEDURE__DASDL_PROPERTY = orgomg.cwm.resource.relational.RelationalPackage.PROCEDURE__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Owner Scope</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_PROCEDURE__OWNER_SCOPE = orgomg.cwm.resource.relational.RelationalPackage.PROCEDURE__OWNER_SCOPE;

    /**
     * The feature id for the '<em><b>Owner</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_PROCEDURE__OWNER = orgomg.cwm.resource.relational.RelationalPackage.PROCEDURE__OWNER;

    /**
     * The feature id for the '<em><b>Feature Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_PROCEDURE__FEATURE_NODE = orgomg.cwm.resource.relational.RelationalPackage.PROCEDURE__FEATURE_NODE;

    /**
     * The feature id for the '<em><b>Feature Map</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_PROCEDURE__FEATURE_MAP = orgomg.cwm.resource.relational.RelationalPackage.PROCEDURE__FEATURE_MAP;

    /**
     * The feature id for the '<em><b>Cf Map</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_PROCEDURE__CF_MAP = orgomg.cwm.resource.relational.RelationalPackage.PROCEDURE__CF_MAP;

    /**
     * The feature id for the '<em><b>Is Query</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_PROCEDURE__IS_QUERY = orgomg.cwm.resource.relational.RelationalPackage.PROCEDURE__IS_QUERY;

    /**
     * The feature id for the '<em><b>Parameter</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_PROCEDURE__PARAMETER = orgomg.cwm.resource.relational.RelationalPackage.PROCEDURE__PARAMETER;

    /**
     * The feature id for the '<em><b>Body</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_PROCEDURE__BODY = orgomg.cwm.resource.relational.RelationalPackage.PROCEDURE__BODY;

    /**
     * The feature id for the '<em><b>Specification</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_PROCEDURE__SPECIFICATION = orgomg.cwm.resource.relational.RelationalPackage.PROCEDURE__SPECIFICATION;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_PROCEDURE__TYPE = orgomg.cwm.resource.relational.RelationalPackage.PROCEDURE__TYPE;

    /**
     * The number of structural features of the '<em>Td Procedure</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_PROCEDURE_FEATURE_COUNT = orgomg.cwm.resource.relational.RelationalPackage.PROCEDURE_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.cwm.relational.impl.TdExpressionImpl <em>Td Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.cwm.relational.impl.TdExpressionImpl
     * @see org.talend.cwm.relational.impl.RelationalPackageImpl#getTdExpression()
     * @generated
     */
    int TD_EXPRESSION = 6;

    /**
     * The feature id for the '<em><b>Body</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_EXPRESSION__BODY = CorePackage.EXPRESSION__BODY;

    /**
     * The feature id for the '<em><b>Language</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_EXPRESSION__LANGUAGE = CorePackage.EXPRESSION__LANGUAGE;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_EXPRESSION__VERSION = CorePackage.EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Modification Date</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_EXPRESSION__MODIFICATION_DATE = CorePackage.EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_EXPRESSION__NAME = CorePackage.EXPRESSION_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Expression Variable Map</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_EXPRESSION__EXPRESSION_VARIABLE_MAP = CorePackage.EXPRESSION_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Td Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_EXPRESSION_FEATURE_COUNT = CorePackage.EXPRESSION_FEATURE_COUNT + 4;

    /**
     * The meta object id for the '<em>java Hash Map</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.util.HashMap
     * @see org.talend.cwm.relational.impl.RelationalPackageImpl#getjavaHashMap()
     * @generated
     */
    int JAVA_HASH_MAP = 7;

    /**
     * Returns the meta object for class '{@link org.talend.cwm.relational.TdTable <em>Td Table</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Td Table</em>'.
     * @see org.talend.cwm.relational.TdTable
     * @generated
     */
    EClass getTdTable();

    /**
     * Returns the meta object for class '{@link org.talend.cwm.relational.TdView <em>Td View</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Td View</em>'.
     * @see org.talend.cwm.relational.TdView
     * @generated
     */
    EClass getTdView();

    /**
     * Returns the meta object for class '{@link org.talend.cwm.relational.TdColumn <em>Td Column</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Td Column</em>'.
     * @see org.talend.cwm.relational.TdColumn
     * @generated
     */
    EClass getTdColumn();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.cwm.relational.TdColumn#getSqlDataType <em>Sql Data Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Sql Data Type</em>'.
     * @see org.talend.cwm.relational.TdColumn#getSqlDataType()
     * @see #getTdColumn()
     * @generated
     */
    EReference getTdColumn_SqlDataType();

    /**
     * Returns the meta object for class '{@link org.talend.cwm.relational.TdSqlDataType <em>Td Sql Data Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Td Sql Data Type</em>'.
     * @see org.talend.cwm.relational.TdSqlDataType
     * @generated
     */
    EClass getTdSqlDataType();

    /**
     * Returns the meta object for the attribute '{@link org.talend.cwm.relational.TdSqlDataType#getJavaDataType <em>Java Data Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Java Data Type</em>'.
     * @see org.talend.cwm.relational.TdSqlDataType#getJavaDataType()
     * @see #getTdSqlDataType()
     * @generated
     */
    EAttribute getTdSqlDataType_JavaDataType();

    /**
     * Returns the meta object for the attribute '{@link org.talend.cwm.relational.TdSqlDataType#getNullable <em>Nullable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Nullable</em>'.
     * @see org.talend.cwm.relational.TdSqlDataType#getNullable()
     * @see #getTdSqlDataType()
     * @generated
     */
    EAttribute getTdSqlDataType_Nullable();

    /**
     * Returns the meta object for the attribute '{@link org.talend.cwm.relational.TdSqlDataType#isUnsignedAttribute <em>Unsigned Attribute</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Unsigned Attribute</em>'.
     * @see org.talend.cwm.relational.TdSqlDataType#isUnsignedAttribute()
     * @see #getTdSqlDataType()
     * @generated
     */
    EAttribute getTdSqlDataType_UnsignedAttribute();

    /**
     * Returns the meta object for the attribute '{@link org.talend.cwm.relational.TdSqlDataType#isCaseSensitive <em>Case Sensitive</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Case Sensitive</em>'.
     * @see org.talend.cwm.relational.TdSqlDataType#isCaseSensitive()
     * @see #getTdSqlDataType()
     * @generated
     */
    EAttribute getTdSqlDataType_CaseSensitive();

    /**
     * Returns the meta object for the attribute '{@link org.talend.cwm.relational.TdSqlDataType#isAutoIncrement <em>Auto Increment</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Auto Increment</em>'.
     * @see org.talend.cwm.relational.TdSqlDataType#isAutoIncrement()
     * @see #getTdSqlDataType()
     * @generated
     */
    EAttribute getTdSqlDataType_AutoIncrement();

    /**
     * Returns the meta object for the attribute '{@link org.talend.cwm.relational.TdSqlDataType#getLocalTypeName <em>Local Type Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Local Type Name</em>'.
     * @see org.talend.cwm.relational.TdSqlDataType#getLocalTypeName()
     * @see #getTdSqlDataType()
     * @generated
     */
    EAttribute getTdSqlDataType_LocalTypeName();

    /**
     * Returns the meta object for the attribute '{@link org.talend.cwm.relational.TdSqlDataType#getSearchable <em>Searchable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Searchable</em>'.
     * @see org.talend.cwm.relational.TdSqlDataType#getSearchable()
     * @see #getTdSqlDataType()
     * @generated
     */
    EAttribute getTdSqlDataType_Searchable();

    /**
     * Returns the meta object for class '{@link org.talend.cwm.relational.TdTrigger <em>Td Trigger</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Td Trigger</em>'.
     * @see org.talend.cwm.relational.TdTrigger
     * @generated
     */
    EClass getTdTrigger();

    /**
     * Returns the meta object for class '{@link org.talend.cwm.relational.TdProcedure <em>Td Procedure</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Td Procedure</em>'.
     * @see org.talend.cwm.relational.TdProcedure
     * @generated
     */
    EClass getTdProcedure();

    /**
     * Returns the meta object for class '{@link org.talend.cwm.relational.TdExpression <em>Td Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Td Expression</em>'.
     * @see org.talend.cwm.relational.TdExpression
     * @generated
     */
    EClass getTdExpression();

    /**
     * Returns the meta object for the attribute '{@link org.talend.cwm.relational.TdExpression#getVersion <em>Version</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Version</em>'.
     * @see org.talend.cwm.relational.TdExpression#getVersion()
     * @see #getTdExpression()
     * @generated
     */
    EAttribute getTdExpression_Version();

    /**
     * Returns the meta object for the attribute '{@link org.talend.cwm.relational.TdExpression#getModificationDate <em>Modification Date</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Modification Date</em>'.
     * @see org.talend.cwm.relational.TdExpression#getModificationDate()
     * @see #getTdExpression()
     * @generated
     */
    EAttribute getTdExpression_ModificationDate();

    /**
     * Returns the meta object for the attribute '{@link org.talend.cwm.relational.TdExpression#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.talend.cwm.relational.TdExpression#getName()
     * @see #getTdExpression()
     * @generated
     */
    EAttribute getTdExpression_Name();

    /**
     * Returns the meta object for the attribute '{@link org.talend.cwm.relational.TdExpression#getExpressionVariableMap <em>Expression Variable Map</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Expression Variable Map</em>'.
     * @see org.talend.cwm.relational.TdExpression#getExpressionVariableMap()
     * @see #getTdExpression()
     * @generated
     */
    EAttribute getTdExpression_ExpressionVariableMap();

    /**
     * Returns the meta object for data type '{@link java.util.HashMap <em>java Hash Map</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>java Hash Map</em>'.
     * @see java.util.HashMap
     * @model instanceClass="java.util.HashMap<java.lang.String, java.lang.String>"
     * @generated
     */
    EDataType getjavaHashMap();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    RelationalFactory getRelationalFactory();

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
         * The meta object literal for the '{@link org.talend.cwm.relational.impl.TdTableImpl <em>Td Table</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.cwm.relational.impl.TdTableImpl
         * @see org.talend.cwm.relational.impl.RelationalPackageImpl#getTdTable()
         * @generated
         */
        EClass TD_TABLE = eINSTANCE.getTdTable();

        /**
         * The meta object literal for the '{@link org.talend.cwm.relational.impl.TdViewImpl <em>Td View</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.cwm.relational.impl.TdViewImpl
         * @see org.talend.cwm.relational.impl.RelationalPackageImpl#getTdView()
         * @generated
         */
        EClass TD_VIEW = eINSTANCE.getTdView();

        /**
         * The meta object literal for the '{@link org.talend.cwm.relational.impl.TdColumnImpl <em>Td Column</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.cwm.relational.impl.TdColumnImpl
         * @see org.talend.cwm.relational.impl.RelationalPackageImpl#getTdColumn()
         * @generated
         */
        EClass TD_COLUMN = eINSTANCE.getTdColumn();

        /**
         * The meta object literal for the '<em><b>Sql Data Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TD_COLUMN__SQL_DATA_TYPE = eINSTANCE.getTdColumn_SqlDataType();

        /**
         * The meta object literal for the '{@link org.talend.cwm.relational.impl.TdSqlDataTypeImpl <em>Td Sql Data Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.cwm.relational.impl.TdSqlDataTypeImpl
         * @see org.talend.cwm.relational.impl.RelationalPackageImpl#getTdSqlDataType()
         * @generated
         */
        EClass TD_SQL_DATA_TYPE = eINSTANCE.getTdSqlDataType();

        /**
         * The meta object literal for the '<em><b>Java Data Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TD_SQL_DATA_TYPE__JAVA_DATA_TYPE = eINSTANCE.getTdSqlDataType_JavaDataType();

        /**
         * The meta object literal for the '<em><b>Nullable</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TD_SQL_DATA_TYPE__NULLABLE = eINSTANCE.getTdSqlDataType_Nullable();

        /**
         * The meta object literal for the '<em><b>Unsigned Attribute</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TD_SQL_DATA_TYPE__UNSIGNED_ATTRIBUTE = eINSTANCE.getTdSqlDataType_UnsignedAttribute();

        /**
         * The meta object literal for the '<em><b>Case Sensitive</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TD_SQL_DATA_TYPE__CASE_SENSITIVE = eINSTANCE.getTdSqlDataType_CaseSensitive();

        /**
         * The meta object literal for the '<em><b>Auto Increment</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TD_SQL_DATA_TYPE__AUTO_INCREMENT = eINSTANCE.getTdSqlDataType_AutoIncrement();

        /**
         * The meta object literal for the '<em><b>Local Type Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TD_SQL_DATA_TYPE__LOCAL_TYPE_NAME = eINSTANCE.getTdSqlDataType_LocalTypeName();

        /**
         * The meta object literal for the '<em><b>Searchable</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TD_SQL_DATA_TYPE__SEARCHABLE = eINSTANCE.getTdSqlDataType_Searchable();

        /**
         * The meta object literal for the '{@link org.talend.cwm.relational.impl.TdTriggerImpl <em>Td Trigger</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.cwm.relational.impl.TdTriggerImpl
         * @see org.talend.cwm.relational.impl.RelationalPackageImpl#getTdTrigger()
         * @generated
         */
        EClass TD_TRIGGER = eINSTANCE.getTdTrigger();

        /**
         * The meta object literal for the '{@link org.talend.cwm.relational.impl.TdProcedureImpl <em>Td Procedure</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.cwm.relational.impl.TdProcedureImpl
         * @see org.talend.cwm.relational.impl.RelationalPackageImpl#getTdProcedure()
         * @generated
         */
        EClass TD_PROCEDURE = eINSTANCE.getTdProcedure();

        /**
         * The meta object literal for the '{@link org.talend.cwm.relational.impl.TdExpressionImpl <em>Td Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.cwm.relational.impl.TdExpressionImpl
         * @see org.talend.cwm.relational.impl.RelationalPackageImpl#getTdExpression()
         * @generated
         */
        EClass TD_EXPRESSION = eINSTANCE.getTdExpression();

        /**
         * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TD_EXPRESSION__VERSION = eINSTANCE.getTdExpression_Version();

        /**
         * The meta object literal for the '<em><b>Modification Date</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TD_EXPRESSION__MODIFICATION_DATE = eINSTANCE.getTdExpression_ModificationDate();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TD_EXPRESSION__NAME = eINSTANCE.getTdExpression_Name();

        /**
         * The meta object literal for the '<em><b>Expression Variable Map</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TD_EXPRESSION__EXPRESSION_VARIABLE_MAP = eINSTANCE.getTdExpression_ExpressionVariableMap();

        /**
         * The meta object literal for the '<em>java Hash Map</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.util.HashMap
         * @see org.talend.cwm.relational.impl.RelationalPackageImpl#getjavaHashMap()
         * @generated
         */
        EDataType JAVA_HASH_MAP = eINSTANCE.getjavaHashMap();

    }

} //RelationalPackage
