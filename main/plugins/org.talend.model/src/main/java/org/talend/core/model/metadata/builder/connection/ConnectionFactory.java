/**
 * <copyright> </copyright>
 * 
 * $Id: ConnectionFactory.java 58072 2011-04-07 03:20:55Z hywang $
 */
package org.talend.core.model.metadata.builder.connection;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a create method for each non-abstract class of
 * the model. <!-- end-user-doc -->
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage
 * @generated
 */
public interface ConnectionFactory extends EFactory {

    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    ConnectionFactory eINSTANCE = org.talend.core.model.metadata.builder.connection.impl.ConnectionFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Metadata</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Metadata</em>'.
     * @generated
     */
    Metadata createMetadata();

    /**
     * Returns a new object of class '<em>Connection</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Connection</em>'.
     * @generated
     */
    Connection createConnection();

    /**
     * Returns a new object of class '<em>Metadata Column</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Metadata Column</em>'.
     * @generated
     */
    MetadataColumn createMetadataColumn();

    /**
     * Returns a new object of class '<em>Metadata Table</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Metadata Table</em>'.
     * @generated
     */
    MetadataTable createMetadataTable();

    /**
     * Returns a new object of class '<em>Delimited File Connection</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Delimited File Connection</em>'.
     * @generated
     */
    DelimitedFileConnection createDelimitedFileConnection();

    /**
     * Returns a new object of class '<em>Positional File Connection</em>'. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @return a new object of class '<em>Positional File Connection</em>'.
     * @generated
     */
    PositionalFileConnection createPositionalFileConnection();

    /**
     * Returns a new object of class '<em>Ebcdic Connection</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Ebcdic Connection</em>'.
     * @generated
     */
    EbcdicConnection createEbcdicConnection();

    /**
     * Returns a new object of class '<em>MDM Connection</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>MDM Connection</em>'.
     * @generated
     */
    MDMConnection createMDMConnection();

    /**
     * Returns a new object of class '<em>Database Connection</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Database Connection</em>'.
     * @generated
     */
    DatabaseConnection createDatabaseConnection();

    /**
     * Returns a new object of class '<em>SAP Connection</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>SAP Connection</em>'.
     * @generated
     */
    SAPConnection createSAPConnection();

    /**
     * Returns a new object of class '<em>SAP Function Unit</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>SAP Function Unit</em>'.
     * @generated
     */
    SAPFunctionUnit createSAPFunctionUnit();

    /**
     * Returns a new object of class '<em>SAPI Doc Unit</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>SAPI Doc Unit</em>'.
     * @generated
     */
    SAPIDocUnit createSAPIDocUnit();

    /**
     * Returns a new object of class '<em>SAP Function Parameter Column</em>'. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @return a new object of class '<em>SAP Function Parameter Column</em>'.
     * @generated
     */
    SAPFunctionParameterColumn createSAPFunctionParameterColumn();

    /**
     * Returns a new object of class '<em>SAP Function Parameter Table</em>'. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @return a new object of class '<em>SAP Function Parameter Table</em>'.
     * @generated
     */
    SAPFunctionParameterTable createSAPFunctionParameterTable();

    /**
     * Returns a new object of class '<em>Input SAP Function Parameter Table</em>'.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @return a new object of class '<em>Input SAP Function Parameter Table</em>'.
     * @generated
     */
    InputSAPFunctionParameterTable createInputSAPFunctionParameterTable();

    /**
     * Returns a new object of class '<em>Output SAP Function Parameter Table</em>'.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @return a new object of class '<em>Output SAP Function Parameter Table</em>'.
     * @generated
     */
    OutputSAPFunctionParameterTable createOutputSAPFunctionParameterTable();

    /**
     * Returns a new object of class '<em>Regexp File Connection</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Regexp File Connection</em>'.
     * @generated
     */
    RegexpFileConnection createRegexpFileConnection();

    /**
     * Returns a new object of class '<em>Xml File Connection</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Xml File Connection</em>'.
     * @generated
     */
    XmlFileConnection createXmlFileConnection();

    /**
     * Returns a new object of class '<em>Schema Target</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Schema Target</em>'.
     * @generated
     */
    SchemaTarget createSchemaTarget();

    /**
     * Returns a new object of class '<em>Queries Connection</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Queries Connection</em>'.
     * @generated
     */
    QueriesConnection createQueriesConnection();

    /**
     * Returns a new object of class '<em>Query</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Query</em>'.
     * @generated
     */
    Query createQuery();

    /**
     * Returns a new object of class '<em>Ldif File Connection</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Ldif File Connection</em>'.
     * @generated
     */
    LdifFileConnection createLdifFileConnection();

    /**
     * Returns a new object of class '<em>File Excel Connection</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>File Excel Connection</em>'.
     * @generated
     */
    FileExcelConnection createFileExcelConnection();

    /**
     * Returns a new object of class '<em>Xml XPath Loop Descriptor</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Xml XPath Loop Descriptor</em>'.
     * @generated
     */
    XmlXPathLoopDescriptor createXmlXPathLoopDescriptor();

    /**
     * Returns a new object of class '<em>Generic Schema Connection</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Generic Schema Connection</em>'.
     * @generated
     */
    GenericSchemaConnection createGenericSchemaConnection();

    /**
     * Returns a new object of class '<em>LDAP Schema Connection</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>LDAP Schema Connection</em>'.
     * @generated
     */
    LDAPSchemaConnection createLDAPSchemaConnection();

    /**
     * Returns a new object of class '<em>WSDL Schema Connection</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>WSDL Schema Connection</em>'.
     * @generated
     */
    WSDLSchemaConnection createWSDLSchemaConnection();

    /**
     * Returns a new object of class '<em>Salesforce Schema Connection</em>'. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @return a new object of class '<em>Salesforce Schema Connection</em>'.
     * @generated
     */
    SalesforceSchemaConnection createSalesforceSchemaConnection();

    /**
     * Returns a new object of class '<em>CDC Connection</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>CDC Connection</em>'.
     * @generated
     */
    CDCConnection createCDCConnection();

    /**
     * Returns a new object of class '<em>CDC Type</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>CDC Type</em>'.
     * @generated
     */
    CDCType createCDCType();

    /**
     * Returns a new object of class '<em>Subscriber Table</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Subscriber Table</em>'.
     * @generated
     */
    SubscriberTable createSubscriberTable();

    /**
     * Returns a new object of class '<em>SAP Test Input Parameter Table</em>'.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @return a new object of class '<em>SAP Test Input Parameter Table</em>'.
     * @generated
     */
    SAPTestInputParameterTable createSAPTestInputParameterTable();

    /**
     * Returns a new object of class '<em>Concept</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Concept</em>'.
     * @generated
     */
    Concept createConcept();

    /**
     * Returns a new object of class '<em>Concept Target</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Concept Target</em>'.
     * @generated
     */
    ConceptTarget createConceptTarget();

    /**
     * Returns a new object of class '<em>HL7 Connection</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>HL7 Connection</em>'.
     * @generated
     */
    HL7Connection createHL7Connection();

    /**
     * Returns a new object of class '<em>Header Footer Connection</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Header Footer Connection</em>'.
     * @generated
     */
    HeaderFooterConnection createHeaderFooterConnection();

    /**
     * Returns a new object of class '<em>XML File Node</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>XML File Node</em>'.
     * @generated
     */
    XMLFileNode createXMLFileNode();

    /**
     * Returns a new object of class '<em>WSDL Parameter</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>WSDL Parameter</em>'.
     * @generated
     */
    WSDLParameter createWSDLParameter();

    /**
     * Returns a new object of class '<em>Generic Package</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Generic Package</em>'.
     * @generated
     */
    GenericPackage createGenericPackage();

    /**
     * Returns a new object of class '<em>HL7 File Node</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>HL7 File Node</em>'.
     * @generated
     */
    HL7FileNode createHL7FileNode();

    /**
     * Returns a new object of class '<em>FTP Connection</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>FTP Connection</em>'.
     * @generated
     */
    FTPConnection createFTPConnection();

    /**
     * Returns a new object of class '<em>BRMS Connection</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>BRMS Connection</em>'.
     * @generated
     */
    BRMSConnection createBRMSConnection();

    /**
     * Returns a new object of class '<em>Condition Type</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Condition Type</em>'.
     * @generated
     */
    ConditionType createConditionType();

    /**
     * Returns a new object of class '<em>EDIFACT Connection</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>EDIFACT Connection</em>'.
     * @generated
     */
    EDIFACTConnection createEDIFACTConnection();

    /**
     * Returns a new object of class '<em>Salesforce Module Unit</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Salesforce Module Unit</em>'.
     * @generated
     */
    SalesforceModuleUnit createSalesforceModuleUnit();

    /**
     * Returns a new object of class '<em>SAP Table Field</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>SAP Table Field</em>'.
     * @generated
     */
    SAPTableField createSAPTableField();

    /**
     * Returns a new object of class '<em>SAP Function Parameter</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>SAP Function Parameter</em>'.
     * @generated
     */
    SAPFunctionParameter createSAPFunctionParameter();

    /**
     * Returns a new object of class '<em>SAP Function Param Data</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>SAP Function Param Data</em>'.
     * @generated
     */
    SAPFunctionParamData createSAPFunctionParamData();

    /**
     * Returns a new object of class '<em>Additional Connection Property</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Additional Connection Property</em>'.
     * @generated
     */
    AdditionalConnectionProperty createAdditionalConnectionProperty();

    /**
     * Returns a new object of class '<em>SAPBW Table</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>SAPBW Table</em>'.
     * @generated
     */
    SAPBWTable createSAPBWTable();

    /**
     * Returns a new object of class '<em>SAPBW Table Field</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>SAPBW Table Field</em>'.
     * @generated
     */
    SAPBWTableField createSAPBWTableField();

    /**
     * Returns a new object of class '<em>SAP Table</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>SAP Table</em>'.
     * @generated
     */
    SAPTable createSAPTable();

    /**
     * Returns a new object of class '<em>EDIFACT Column</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>EDIFACT Column</em>'.
     * @generated
     */
    EDIFACTColumn createEDIFACTColumn();

    /**
     * Returns a new object of class '<em>Validation Rules Connection</em>'. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @return a new object of class '<em>Validation Rules Connection</em>'.
     * @generated
     */
    ValidationRulesConnection createValidationRulesConnection();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    ConnectionPackage getConnectionPackage();

    /**
     * copy this MetadataColumn.
     * 
     * @return
     */
    MetadataColumn copy(MetadataColumn column, String newId);
} // ConnectionFactory
