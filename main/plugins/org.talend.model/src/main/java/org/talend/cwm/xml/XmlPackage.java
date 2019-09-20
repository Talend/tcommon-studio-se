/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.cwm.xml;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

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
 * @see org.talend.cwm.xml.XmlFactory
 * @model kind="package"
 * @generated
 */
public interface XmlPackage extends EPackage {

    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "xml";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://www.talend.org/cwm/resource/xml/2010";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "org.talend.cwm.xml";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    XmlPackage eINSTANCE = org.talend.cwm.xml.impl.XmlPackageImpl.init();

    /**
     * The meta object id for the '{@link org.talend.cwm.xml.impl.TdXmlElementTypeImpl <em>Td Xml Element Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.cwm.xml.impl.TdXmlElementTypeImpl
     * @see org.talend.cwm.xml.impl.XmlPackageImpl#getTdXmlElementType()
     * @generated
     */
    int TD_XML_ELEMENT_TYPE = 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__NAME = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__VISIBILITY = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__CLIENT_DEPENDENCY = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__SUPPLIER_DEPENDENCY = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__CONSTRAINT = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__NAMESPACE = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__IMPORTER = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__STEREOTYPE = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__TAGGED_VALUE = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__DOCUMENT = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__DESCRIPTION = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__RESPONSIBLE_PARTY = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__ELEMENT_NODE = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__SET = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__RENDERED_OBJECT = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__VOCABULARY_ELEMENT = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__MEASUREMENT = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__CHANGE_REQUEST = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__DASDL_PROPERTY = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Owned Element</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__OWNED_ELEMENT = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__OWNED_ELEMENT;

    /**
     * The feature id for the '<em><b>Is Abstract</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__IS_ABSTRACT = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__IS_ABSTRACT;

    /**
     * The feature id for the '<em><b>Feature</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__FEATURE = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__FEATURE;

    /**
     * The feature id for the '<em><b>Structural Feature</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__STRUCTURAL_FEATURE = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__STRUCTURAL_FEATURE;

    /**
     * The feature id for the '<em><b>Parameter</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__PARAMETER = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__PARAMETER;

    /**
     * The feature id for the '<em><b>Generalization</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__GENERALIZATION = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__GENERALIZATION;

    /**
     * The feature id for the '<em><b>Specialization</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__SPECIALIZATION = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__SPECIALIZATION;

    /**
     * The feature id for the '<em><b>Instance</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__INSTANCE = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__INSTANCE;

    /**
     * The feature id for the '<em><b>Alias</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__ALIAS = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__ALIAS;

    /**
     * The feature id for the '<em><b>Expression Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__EXPRESSION_NODE = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__EXPRESSION_NODE;

    /**
     * The feature id for the '<em><b>Mapping From</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__MAPPING_FROM = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__MAPPING_FROM;

    /**
     * The feature id for the '<em><b>Mapping To</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__MAPPING_TO = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__MAPPING_TO;

    /**
     * The feature id for the '<em><b>Classifier Map</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__CLASSIFIER_MAP = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__CLASSIFIER_MAP;

    /**
     * The feature id for the '<em><b>Cf Map</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__CF_MAP = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__CF_MAP;

    /**
     * The feature id for the '<em><b>Domain</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__DOMAIN = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__DOMAIN;

    /**
     * The feature id for the '<em><b>Simple Dimension</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__SIMPLE_DIMENSION = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__SIMPLE_DIMENSION;

    /**
     * The feature id for the '<em><b>Index</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__INDEX = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__INDEX;

    /**
     * The feature id for the '<em><b>Content</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__CONTENT = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE__CONTENT;

    /**
     * The feature id for the '<em><b>Xsd Element Declaration</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__XSD_ELEMENT_DECLARATION = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Owned Document</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__OWNED_DOCUMENT = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Java Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__JAVA_TYPE = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Xml Content</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE__XML_CONTENT = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Td Xml Element Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_ELEMENT_TYPE_FEATURE_COUNT = orgomg.cwm.resource.xml.XmlPackage.ELEMENT_TYPE_FEATURE_COUNT + 4;

    /**
     * The meta object id for the '{@link org.talend.cwm.xml.impl.TdXmlContentImpl <em>Td Xml Content</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.cwm.xml.impl.TdXmlContentImpl
     * @see org.talend.cwm.xml.impl.XmlPackageImpl#getTdXmlContent()
     * @generated
     */
    int TD_XML_CONTENT = 1;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_CONTENT__NAME = orgomg.cwm.resource.xml.XmlPackage.CONTENT__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_CONTENT__VISIBILITY = orgomg.cwm.resource.xml.XmlPackage.CONTENT__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_CONTENT__CLIENT_DEPENDENCY = orgomg.cwm.resource.xml.XmlPackage.CONTENT__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_CONTENT__SUPPLIER_DEPENDENCY = orgomg.cwm.resource.xml.XmlPackage.CONTENT__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_CONTENT__CONSTRAINT = orgomg.cwm.resource.xml.XmlPackage.CONTENT__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_CONTENT__NAMESPACE = orgomg.cwm.resource.xml.XmlPackage.CONTENT__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_CONTENT__IMPORTER = orgomg.cwm.resource.xml.XmlPackage.CONTENT__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_CONTENT__STEREOTYPE = orgomg.cwm.resource.xml.XmlPackage.CONTENT__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_CONTENT__TAGGED_VALUE = orgomg.cwm.resource.xml.XmlPackage.CONTENT__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_CONTENT__DOCUMENT = orgomg.cwm.resource.xml.XmlPackage.CONTENT__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_CONTENT__DESCRIPTION = orgomg.cwm.resource.xml.XmlPackage.CONTENT__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_CONTENT__RESPONSIBLE_PARTY = orgomg.cwm.resource.xml.XmlPackage.CONTENT__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_CONTENT__ELEMENT_NODE = orgomg.cwm.resource.xml.XmlPackage.CONTENT__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_CONTENT__SET = orgomg.cwm.resource.xml.XmlPackage.CONTENT__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_CONTENT__RENDERED_OBJECT = orgomg.cwm.resource.xml.XmlPackage.CONTENT__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_CONTENT__VOCABULARY_ELEMENT = orgomg.cwm.resource.xml.XmlPackage.CONTENT__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_CONTENT__MEASUREMENT = orgomg.cwm.resource.xml.XmlPackage.CONTENT__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_CONTENT__CHANGE_REQUEST = orgomg.cwm.resource.xml.XmlPackage.CONTENT__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_CONTENT__DASDL_PROPERTY = orgomg.cwm.resource.xml.XmlPackage.CONTENT__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_CONTENT__TYPE = orgomg.cwm.resource.xml.XmlPackage.CONTENT__TYPE;

    /**
     * The feature id for the '<em><b>Occurrence</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_CONTENT__OCCURRENCE = orgomg.cwm.resource.xml.XmlPackage.CONTENT__OCCURRENCE;

    /**
     * The feature id for the '<em><b>Element Type</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_CONTENT__ELEMENT_TYPE = orgomg.cwm.resource.xml.XmlPackage.CONTENT__ELEMENT_TYPE;

    /**
     * The feature id for the '<em><b>Owned Element Type</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_CONTENT__OWNED_ELEMENT_TYPE = orgomg.cwm.resource.xml.XmlPackage.CONTENT__OWNED_ELEMENT_TYPE;

    /**
     * The feature id for the '<em><b>Xml Elements</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_CONTENT__XML_ELEMENTS = orgomg.cwm.resource.xml.XmlPackage.CONTENT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Td Xml Content</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_CONTENT_FEATURE_COUNT = orgomg.cwm.resource.xml.XmlPackage.CONTENT_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.talend.cwm.xml.impl.TdXmlSchemaImpl <em>Td Xml Schema</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.cwm.xml.impl.TdXmlSchemaImpl
     * @see org.talend.cwm.xml.impl.XmlPackageImpl#getTdXmlSchema()
     * @generated
     */
    int TD_XML_SCHEMA = 2;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_SCHEMA__NAME = orgomg.cwm.resource.xml.XmlPackage.SCHEMA__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_SCHEMA__VISIBILITY = orgomg.cwm.resource.xml.XmlPackage.SCHEMA__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_SCHEMA__CLIENT_DEPENDENCY = orgomg.cwm.resource.xml.XmlPackage.SCHEMA__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_SCHEMA__SUPPLIER_DEPENDENCY = orgomg.cwm.resource.xml.XmlPackage.SCHEMA__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_SCHEMA__CONSTRAINT = orgomg.cwm.resource.xml.XmlPackage.SCHEMA__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_SCHEMA__NAMESPACE = orgomg.cwm.resource.xml.XmlPackage.SCHEMA__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_SCHEMA__IMPORTER = orgomg.cwm.resource.xml.XmlPackage.SCHEMA__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_SCHEMA__STEREOTYPE = orgomg.cwm.resource.xml.XmlPackage.SCHEMA__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_SCHEMA__TAGGED_VALUE = orgomg.cwm.resource.xml.XmlPackage.SCHEMA__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_SCHEMA__DOCUMENT = orgomg.cwm.resource.xml.XmlPackage.SCHEMA__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_SCHEMA__DESCRIPTION = orgomg.cwm.resource.xml.XmlPackage.SCHEMA__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_SCHEMA__RESPONSIBLE_PARTY = orgomg.cwm.resource.xml.XmlPackage.SCHEMA__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_SCHEMA__ELEMENT_NODE = orgomg.cwm.resource.xml.XmlPackage.SCHEMA__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_SCHEMA__SET = orgomg.cwm.resource.xml.XmlPackage.SCHEMA__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_SCHEMA__RENDERED_OBJECT = orgomg.cwm.resource.xml.XmlPackage.SCHEMA__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_SCHEMA__VOCABULARY_ELEMENT = orgomg.cwm.resource.xml.XmlPackage.SCHEMA__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_SCHEMA__MEASUREMENT = orgomg.cwm.resource.xml.XmlPackage.SCHEMA__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_SCHEMA__CHANGE_REQUEST = orgomg.cwm.resource.xml.XmlPackage.SCHEMA__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_SCHEMA__DASDL_PROPERTY = orgomg.cwm.resource.xml.XmlPackage.SCHEMA__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Owned Element</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_SCHEMA__OWNED_ELEMENT = orgomg.cwm.resource.xml.XmlPackage.SCHEMA__OWNED_ELEMENT;

    /**
     * The feature id for the '<em><b>Imported Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_SCHEMA__IMPORTED_ELEMENT = orgomg.cwm.resource.xml.XmlPackage.SCHEMA__IMPORTED_ELEMENT;

    /**
     * The feature id for the '<em><b>Data Manager</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_SCHEMA__DATA_MANAGER = orgomg.cwm.resource.xml.XmlPackage.SCHEMA__DATA_MANAGER;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_SCHEMA__VERSION = orgomg.cwm.resource.xml.XmlPackage.SCHEMA__VERSION;

    /**
     * The feature id for the '<em><b>Xml Namespace</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_SCHEMA__XML_NAMESPACE = orgomg.cwm.resource.xml.XmlPackage.SCHEMA__XML_NAMESPACE;

    /**
     * The feature id for the '<em><b>Xsd File Path</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_SCHEMA__XSD_FILE_PATH = orgomg.cwm.resource.xml.XmlPackage.SCHEMA_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Td Xml Schema</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TD_XML_SCHEMA_FEATURE_COUNT = orgomg.cwm.resource.xml.XmlPackage.SCHEMA_FEATURE_COUNT + 1;

    /**
     * Returns the meta object for class '{@link org.talend.cwm.xml.TdXmlElementType <em>Td Xml Element Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Td Xml Element Type</em>'.
     * @see org.talend.cwm.xml.TdXmlElementType
     * @generated
     */
    EClass getTdXmlElementType();

    /**
     * Returns the meta object for the reference '{@link org.talend.cwm.xml.TdXmlElementType#getXsdElementDeclaration <em>Xsd Element Declaration</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Xsd Element Declaration</em>'.
     * @see org.talend.cwm.xml.TdXmlElementType#getXsdElementDeclaration()
     * @see #getTdXmlElementType()
     * @generated
     */
    EReference getTdXmlElementType_XsdElementDeclaration();

    /**
     * Returns the meta object for the reference '{@link org.talend.cwm.xml.TdXmlElementType#getOwnedDocument <em>Owned Document</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Owned Document</em>'.
     * @see org.talend.cwm.xml.TdXmlElementType#getOwnedDocument()
     * @see #getTdXmlElementType()
     * @generated
     */
    EReference getTdXmlElementType_OwnedDocument();

    /**
     * Returns the meta object for the attribute '{@link org.talend.cwm.xml.TdXmlElementType#getJavaType <em>Java Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Java Type</em>'.
     * @see org.talend.cwm.xml.TdXmlElementType#getJavaType()
     * @see #getTdXmlElementType()
     * @generated
     */
    EAttribute getTdXmlElementType_JavaType();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.cwm.xml.TdXmlElementType#getXmlContent <em>Xml Content</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Xml Content</em>'.
     * @see org.talend.cwm.xml.TdXmlElementType#getXmlContent()
     * @see #getTdXmlElementType()
     * @generated
     */
    EReference getTdXmlElementType_XmlContent();

    /**
     * Returns the meta object for class '{@link org.talend.cwm.xml.TdXmlContent <em>Td Xml Content</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Td Xml Content</em>'.
     * @see org.talend.cwm.xml.TdXmlContent
     * @generated
     */
    EClass getTdXmlContent();

    /**
     * Returns the meta object for the containment reference list '{@link org.talend.cwm.xml.TdXmlContent#getXmlElements <em>Xml Elements</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Xml Elements</em>'.
     * @see org.talend.cwm.xml.TdXmlContent#getXmlElements()
     * @see #getTdXmlContent()
     * @generated
     */
    EReference getTdXmlContent_XmlElements();

    /**
     * Returns the meta object for class '{@link org.talend.cwm.xml.TdXmlSchema <em>Td Xml Schema</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Td Xml Schema</em>'.
     * @see org.talend.cwm.xml.TdXmlSchema
     * @generated
     */
    EClass getTdXmlSchema();

    /**
     * Returns the meta object for the attribute '{@link org.talend.cwm.xml.TdXmlSchema#getXsdFilePath <em>Xsd File Path</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Xsd File Path</em>'.
     * @see org.talend.cwm.xml.TdXmlSchema#getXsdFilePath()
     * @see #getTdXmlSchema()
     * @generated
     */
    EAttribute getTdXmlSchema_XsdFilePath();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    XmlFactory getXmlFactory();

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
         * The meta object literal for the '{@link org.talend.cwm.xml.impl.TdXmlElementTypeImpl <em>Td Xml Element Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.cwm.xml.impl.TdXmlElementTypeImpl
         * @see org.talend.cwm.xml.impl.XmlPackageImpl#getTdXmlElementType()
         * @generated
         */
        EClass TD_XML_ELEMENT_TYPE = eINSTANCE.getTdXmlElementType();

        /**
         * The meta object literal for the '<em><b>Xsd Element Declaration</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TD_XML_ELEMENT_TYPE__XSD_ELEMENT_DECLARATION = eINSTANCE.getTdXmlElementType_XsdElementDeclaration();

        /**
         * The meta object literal for the '<em><b>Owned Document</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TD_XML_ELEMENT_TYPE__OWNED_DOCUMENT = eINSTANCE.getTdXmlElementType_OwnedDocument();

        /**
         * The meta object literal for the '<em><b>Java Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TD_XML_ELEMENT_TYPE__JAVA_TYPE = eINSTANCE.getTdXmlElementType_JavaType();

        /**
         * The meta object literal for the '<em><b>Xml Content</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TD_XML_ELEMENT_TYPE__XML_CONTENT = eINSTANCE.getTdXmlElementType_XmlContent();

        /**
         * The meta object literal for the '{@link org.talend.cwm.xml.impl.TdXmlContentImpl <em>Td Xml Content</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.cwm.xml.impl.TdXmlContentImpl
         * @see org.talend.cwm.xml.impl.XmlPackageImpl#getTdXmlContent()
         * @generated
         */
        EClass TD_XML_CONTENT = eINSTANCE.getTdXmlContent();

        /**
         * The meta object literal for the '<em><b>Xml Elements</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TD_XML_CONTENT__XML_ELEMENTS = eINSTANCE.getTdXmlContent_XmlElements();

        /**
         * The meta object literal for the '{@link org.talend.cwm.xml.impl.TdXmlSchemaImpl <em>Td Xml Schema</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.cwm.xml.impl.TdXmlSchemaImpl
         * @see org.talend.cwm.xml.impl.XmlPackageImpl#getTdXmlSchema()
         * @generated
         */
        EClass TD_XML_SCHEMA = eINSTANCE.getTdXmlSchema();

        /**
         * The meta object literal for the '<em><b>Xsd File Path</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TD_XML_SCHEMA__XSD_FILE_PATH = eINSTANCE.getTdXmlSchema_XsdFilePath();

    }

} //XmlPackage
