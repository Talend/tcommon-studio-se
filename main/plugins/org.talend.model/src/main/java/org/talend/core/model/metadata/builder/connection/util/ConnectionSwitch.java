/**
 * <copyright> </copyright>
 * 
 * $Id: ConnectionSwitch.java 89342 2012-08-20 03:46:34Z ycbai $
 */
package org.talend.core.model.metadata.builder.connection.util;

import java.util.List;

import java.util.Map;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.talend.core.model.metadata.builder.connection.*;
import org.talend.core.model.metadata.builder.connection.AbstractMetadataObject;
import org.talend.core.model.metadata.builder.connection.CDCConnection;
import org.talend.core.model.metadata.builder.connection.CDCType;
import org.talend.core.model.metadata.builder.connection.Concept;
import org.talend.core.model.metadata.builder.connection.ConceptTarget;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.EbcdicConnection;
import org.talend.core.model.metadata.builder.connection.FileConnection;
import org.talend.core.model.metadata.builder.connection.FileExcelConnection;
import org.talend.core.model.metadata.builder.connection.GenericPackage;
import org.talend.core.model.metadata.builder.connection.GenericSchemaConnection;
import org.talend.core.model.metadata.builder.connection.HL7Connection;
import org.talend.core.model.metadata.builder.connection.HL7FileNode;
import org.talend.core.model.metadata.builder.connection.HeaderFooterConnection;
import org.talend.core.model.metadata.builder.connection.InputSAPFunctionParameterTable;
import org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection;
import org.talend.core.model.metadata.builder.connection.LdifFileConnection;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.core.model.metadata.builder.connection.Metadata;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.OutputSAPFunctionParameterTable;
import org.talend.core.model.metadata.builder.connection.PositionalFileConnection;
import org.talend.core.model.metadata.builder.connection.QueriesConnection;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.core.model.metadata.builder.connection.RegexpFileConnection;
import org.talend.core.model.metadata.builder.connection.SAPConnection;
import org.talend.core.model.metadata.builder.connection.SAPFunctionParameterColumn;
import org.talend.core.model.metadata.builder.connection.SAPFunctionParameterTable;
import org.talend.core.model.metadata.builder.connection.SAPFunctionUnit;
import org.talend.core.model.metadata.builder.connection.SAPIDocUnit;
import org.talend.core.model.metadata.builder.connection.SAPTestInputParameterTable;
import org.talend.core.model.metadata.builder.connection.SalesforceSchemaConnection;
import org.talend.core.model.metadata.builder.connection.SchemaTarget;
import org.talend.core.model.metadata.builder.connection.SubscriberTable;
import org.talend.core.model.metadata.builder.connection.WSDLParameter;
import org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection;
import org.talend.core.model.metadata.builder.connection.XMLFileNode;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.model.metadata.builder.connection.XmlXPathLoopDescriptor;
import org.talend.cwm.relational.TdTable;
import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.foundation.softwaredeployment.DataProvider;
import orgomg.cwm.foundation.softwaredeployment.DeployedComponent;
import orgomg.cwm.objectmodel.core.Attribute;
import orgomg.cwm.objectmodel.core.Classifier;
import orgomg.cwm.objectmodel.core.Element;
import orgomg.cwm.objectmodel.core.Feature;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Namespace;
import orgomg.cwm.objectmodel.core.StructuralFeature;
import orgomg.cwm.resource.record.Field;
import orgomg.cwm.resource.relational.ColumnSet;
import orgomg.cwm.resource.relational.NamedColumnSet;
import orgomg.cwm.resource.relational.Table;

/**
 * <!-- begin-user-doc --> The <b>Switch</b> for the model's inheritance hierarchy. It supports the call
 * {@link #doSwitch(EObject) doSwitch(object)} to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object and proceeding up the inheritance hierarchy until a non-null result is
 * returned, which is the result of the switch. <!-- end-user-doc -->
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage
 * @generated
 */
public class ConnectionSwitch<T> {

    /**
     * The cached model package
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected static ConnectionPackage modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ConnectionSwitch() {
        if (modelPackage == null) {
            modelPackage = ConnectionPackage.eINSTANCE;
        }
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    public T doSwitch(EObject theEObject) {
        return doSwitch(theEObject.eClass(), theEObject);
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    protected T doSwitch(EClass theEClass, EObject theEObject) {
        if (theEClass.eContainer() == modelPackage) {
            return doSwitch(theEClass.getClassifierID(), theEObject);
        } else {
            List<EClass> eSuperTypes = theEClass.getESuperTypes();
            return eSuperTypes.isEmpty() ? defaultCase(theEObject) : doSwitch(eSuperTypes.get(0), theEObject);
        }
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    protected T doSwitch(int classifierID, EObject theEObject) {
        switch (classifierID) {
        case ConnectionPackage.METADATA: {
            Metadata metadata = (Metadata) theEObject;
            T result = caseMetadata(metadata);
            if (result == null)
                result = caseAbstractMetadataObject(metadata);
            if (result == null)
                result = caseModelElement(metadata);
            if (result == null)
                result = caseElement(metadata);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.CONNECTION: {
            Connection connection = (Connection) theEObject;
            T result = caseConnection(connection);
            if (result == null)
                result = caseAbstractMetadataObject(connection);
            if (result == null)
                result = caseDataProvider(connection);
            if (result == null)
                result = caseDataManager(connection);
            if (result == null)
                result = caseElement(connection);
            if (result == null)
                result = caseDeployedComponent(connection);
            if (result == null)
                result = casePackage(connection);
            if (result == null)
                result = caseNamespace(connection);
            if (result == null)
                result = caseModelElement(connection);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.METADATA_COLUMN: {
            MetadataColumn metadataColumn = (MetadataColumn) theEObject;
            T result = caseMetadataColumn(metadataColumn);
            if (result == null)
                result = caseAbstractMetadataObject(metadataColumn);
            if (result == null)
                result = caseField(metadataColumn);
            if (result == null)
                result = caseAttribute(metadataColumn);
            if (result == null)
                result = caseElement(metadataColumn);
            if (result == null)
                result = caseStructuralFeature(metadataColumn);
            if (result == null)
                result = caseFeature(metadataColumn);
            if (result == null)
                result = caseModelElement(metadataColumn);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.ABSTRACT_METADATA_OBJECT: {
            AbstractMetadataObject abstractMetadataObject = (AbstractMetadataObject) theEObject;
            T result = caseAbstractMetadataObject(abstractMetadataObject);
            if (result == null)
                result = caseModelElement(abstractMetadataObject);
            if (result == null)
                result = caseElement(abstractMetadataObject);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.METADATA_TABLE: {
            MetadataTable metadataTable = (MetadataTable) theEObject;
            T result = caseMetadataTable(metadataTable);
            if (result == null)
                result = caseAbstractMetadataObject(metadataTable);
            if (result == null)
                result = caseClass(metadataTable);
            if (result == null)
                result = caseClassifier(metadataTable);
            if (result == null)
                result = caseElement(metadataTable);
            if (result == null)
                result = caseNamespace(metadataTable);
            if (result == null)
                result = caseModelElement(metadataTable);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.FILE_CONNECTION: {
            FileConnection fileConnection = (FileConnection) theEObject;
            T result = caseFileConnection(fileConnection);
            if (result == null)
                result = caseConnection(fileConnection);
            if (result == null)
                result = caseAbstractMetadataObject(fileConnection);
            if (result == null)
                result = caseDataProvider(fileConnection);
            if (result == null)
                result = caseDataManager(fileConnection);
            if (result == null)
                result = caseElement(fileConnection);
            if (result == null)
                result = caseDeployedComponent(fileConnection);
            if (result == null)
                result = casePackage(fileConnection);
            if (result == null)
                result = caseNamespace(fileConnection);
            if (result == null)
                result = caseModelElement(fileConnection);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.DELIMITED_FILE_CONNECTION: {
            DelimitedFileConnection delimitedFileConnection = (DelimitedFileConnection) theEObject;
            T result = caseDelimitedFileConnection(delimitedFileConnection);
            if (result == null)
                result = caseFileConnection(delimitedFileConnection);
            if (result == null)
                result = caseConnection(delimitedFileConnection);
            if (result == null)
                result = caseAbstractMetadataObject(delimitedFileConnection);
            if (result == null)
                result = caseDataProvider(delimitedFileConnection);
            if (result == null)
                result = caseDataManager(delimitedFileConnection);
            if (result == null)
                result = caseElement(delimitedFileConnection);
            if (result == null)
                result = caseDeployedComponent(delimitedFileConnection);
            if (result == null)
                result = casePackage(delimitedFileConnection);
            if (result == null)
                result = caseNamespace(delimitedFileConnection);
            if (result == null)
                result = caseModelElement(delimitedFileConnection);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.POSITIONAL_FILE_CONNECTION: {
            PositionalFileConnection positionalFileConnection = (PositionalFileConnection) theEObject;
            T result = casePositionalFileConnection(positionalFileConnection);
            if (result == null)
                result = caseFileConnection(positionalFileConnection);
            if (result == null)
                result = caseConnection(positionalFileConnection);
            if (result == null)
                result = caseAbstractMetadataObject(positionalFileConnection);
            if (result == null)
                result = caseDataProvider(positionalFileConnection);
            if (result == null)
                result = caseDataManager(positionalFileConnection);
            if (result == null)
                result = caseElement(positionalFileConnection);
            if (result == null)
                result = caseDeployedComponent(positionalFileConnection);
            if (result == null)
                result = casePackage(positionalFileConnection);
            if (result == null)
                result = caseNamespace(positionalFileConnection);
            if (result == null)
                result = caseModelElement(positionalFileConnection);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.EBCDIC_CONNECTION: {
            EbcdicConnection ebcdicConnection = (EbcdicConnection) theEObject;
            T result = caseEbcdicConnection(ebcdicConnection);
            if (result == null)
                result = caseFileConnection(ebcdicConnection);
            if (result == null)
                result = caseConnection(ebcdicConnection);
            if (result == null)
                result = caseAbstractMetadataObject(ebcdicConnection);
            if (result == null)
                result = caseDataProvider(ebcdicConnection);
            if (result == null)
                result = caseDataManager(ebcdicConnection);
            if (result == null)
                result = caseElement(ebcdicConnection);
            if (result == null)
                result = caseDeployedComponent(ebcdicConnection);
            if (result == null)
                result = casePackage(ebcdicConnection);
            if (result == null)
                result = caseNamespace(ebcdicConnection);
            if (result == null)
                result = caseModelElement(ebcdicConnection);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.MDM_CONNECTION: {
            MDMConnection mdmConnection = (MDMConnection) theEObject;
            T result = caseMDMConnection(mdmConnection);
            if (result == null)
                result = caseConnection(mdmConnection);
            if (result == null)
                result = caseAbstractMetadataObject(mdmConnection);
            if (result == null)
                result = caseDataProvider(mdmConnection);
            if (result == null)
                result = caseDataManager(mdmConnection);
            if (result == null)
                result = caseElement(mdmConnection);
            if (result == null)
                result = caseDeployedComponent(mdmConnection);
            if (result == null)
                result = casePackage(mdmConnection);
            if (result == null)
                result = caseNamespace(mdmConnection);
            if (result == null)
                result = caseModelElement(mdmConnection);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.DATABASE_CONNECTION: {
            DatabaseConnection databaseConnection = (DatabaseConnection) theEObject;
            T result = caseDatabaseConnection(databaseConnection);
            if (result == null)
                result = caseConnection(databaseConnection);
            if (result == null)
                result = caseAbstractMetadataObject(databaseConnection);
            if (result == null)
                result = caseDataProvider(databaseConnection);
            if (result == null)
                result = caseDataManager(databaseConnection);
            if (result == null)
                result = caseElement(databaseConnection);
            if (result == null)
                result = caseDeployedComponent(databaseConnection);
            if (result == null)
                result = casePackage(databaseConnection);
            if (result == null)
                result = caseNamespace(databaseConnection);
            if (result == null)
                result = caseModelElement(databaseConnection);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.SAP_CONNECTION: {
            SAPConnection sapConnection = (SAPConnection) theEObject;
            T result = caseSAPConnection(sapConnection);
            if (result == null)
                result = caseConnection(sapConnection);
            if (result == null)
                result = caseAbstractMetadataObject(sapConnection);
            if (result == null)
                result = caseDataProvider(sapConnection);
            if (result == null)
                result = caseDataManager(sapConnection);
            if (result == null)
                result = caseElement(sapConnection);
            if (result == null)
                result = caseDeployedComponent(sapConnection);
            if (result == null)
                result = casePackage(sapConnection);
            if (result == null)
                result = caseNamespace(sapConnection);
            if (result == null)
                result = caseModelElement(sapConnection);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.SAP_FUNCTION_UNIT: {
            SAPFunctionUnit sapFunctionUnit = (SAPFunctionUnit) theEObject;
            T result = caseSAPFunctionUnit(sapFunctionUnit);
            if (result == null)
                result = caseAbstractMetadataObject(sapFunctionUnit);
            if (result == null)
                result = caseModelElement(sapFunctionUnit);
            if (result == null)
                result = caseElement(sapFunctionUnit);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.SAPI_DOC_UNIT: {
            SAPIDocUnit sapiDocUnit = (SAPIDocUnit) theEObject;
            T result = caseSAPIDocUnit(sapiDocUnit);
            if (result == null)
                result = caseAbstractMetadataObject(sapiDocUnit);
            if (result == null)
                result = caseModelElement(sapiDocUnit);
            if (result == null)
                result = caseElement(sapiDocUnit);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN: {
            SAPFunctionParameterColumn sapFunctionParameterColumn = (SAPFunctionParameterColumn) theEObject;
            T result = caseSAPFunctionParameterColumn(sapFunctionParameterColumn);
            if (result == null)
                result = caseAbstractMetadataObject(sapFunctionParameterColumn);
            if (result == null)
                result = caseModelElement(sapFunctionParameterColumn);
            if (result == null)
                result = caseElement(sapFunctionParameterColumn);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_TABLE: {
            SAPFunctionParameterTable sapFunctionParameterTable = (SAPFunctionParameterTable) theEObject;
            T result = caseSAPFunctionParameterTable(sapFunctionParameterTable);
            if (result == null)
                result = caseAbstractMetadataObject(sapFunctionParameterTable);
            if (result == null)
                result = caseModelElement(sapFunctionParameterTable);
            if (result == null)
                result = caseElement(sapFunctionParameterTable);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.INPUT_SAP_FUNCTION_PARAMETER_TABLE: {
            InputSAPFunctionParameterTable inputSAPFunctionParameterTable = (InputSAPFunctionParameterTable) theEObject;
            T result = caseInputSAPFunctionParameterTable(inputSAPFunctionParameterTable);
            if (result == null)
                result = caseSAPFunctionParameterTable(inputSAPFunctionParameterTable);
            if (result == null)
                result = caseAbstractMetadataObject(inputSAPFunctionParameterTable);
            if (result == null)
                result = caseModelElement(inputSAPFunctionParameterTable);
            if (result == null)
                result = caseElement(inputSAPFunctionParameterTable);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.OUTPUT_SAP_FUNCTION_PARAMETER_TABLE: {
            OutputSAPFunctionParameterTable outputSAPFunctionParameterTable = (OutputSAPFunctionParameterTable) theEObject;
            T result = caseOutputSAPFunctionParameterTable(outputSAPFunctionParameterTable);
            if (result == null)
                result = caseSAPFunctionParameterTable(outputSAPFunctionParameterTable);
            if (result == null)
                result = caseAbstractMetadataObject(outputSAPFunctionParameterTable);
            if (result == null)
                result = caseModelElement(outputSAPFunctionParameterTable);
            if (result == null)
                result = caseElement(outputSAPFunctionParameterTable);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.REGEXP_FILE_CONNECTION: {
            RegexpFileConnection regexpFileConnection = (RegexpFileConnection) theEObject;
            T result = caseRegexpFileConnection(regexpFileConnection);
            if (result == null)
                result = caseFileConnection(regexpFileConnection);
            if (result == null)
                result = caseConnection(regexpFileConnection);
            if (result == null)
                result = caseAbstractMetadataObject(regexpFileConnection);
            if (result == null)
                result = caseDataProvider(regexpFileConnection);
            if (result == null)
                result = caseDataManager(regexpFileConnection);
            if (result == null)
                result = caseElement(regexpFileConnection);
            if (result == null)
                result = caseDeployedComponent(regexpFileConnection);
            if (result == null)
                result = casePackage(regexpFileConnection);
            if (result == null)
                result = caseNamespace(regexpFileConnection);
            if (result == null)
                result = caseModelElement(regexpFileConnection);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.XML_FILE_CONNECTION: {
            XmlFileConnection xmlFileConnection = (XmlFileConnection) theEObject;
            T result = caseXmlFileConnection(xmlFileConnection);
            if (result == null)
                result = caseConnection(xmlFileConnection);
            if (result == null)
                result = caseAbstractMetadataObject(xmlFileConnection);
            if (result == null)
                result = caseDataProvider(xmlFileConnection);
            if (result == null)
                result = caseDataManager(xmlFileConnection);
            if (result == null)
                result = caseElement(xmlFileConnection);
            if (result == null)
                result = caseDeployedComponent(xmlFileConnection);
            if (result == null)
                result = casePackage(xmlFileConnection);
            if (result == null)
                result = caseNamespace(xmlFileConnection);
            if (result == null)
                result = caseModelElement(xmlFileConnection);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.SCHEMA_TARGET: {
            SchemaTarget schemaTarget = (SchemaTarget) theEObject;
            T result = caseSchemaTarget(schemaTarget);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.QUERIES_CONNECTION: {
            QueriesConnection queriesConnection = (QueriesConnection) theEObject;
            T result = caseQueriesConnection(queriesConnection);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.QUERY: {
            Query query = (Query) theEObject;
            T result = caseQuery(query);
            if (result == null)
                result = caseAbstractMetadataObject(query);
            if (result == null)
                result = caseModelElement(query);
            if (result == null)
                result = caseElement(query);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.LDIF_FILE_CONNECTION: {
            LdifFileConnection ldifFileConnection = (LdifFileConnection) theEObject;
            T result = caseLdifFileConnection(ldifFileConnection);
            if (result == null)
                result = caseConnection(ldifFileConnection);
            if (result == null)
                result = caseAbstractMetadataObject(ldifFileConnection);
            if (result == null)
                result = caseDataProvider(ldifFileConnection);
            if (result == null)
                result = caseDataManager(ldifFileConnection);
            if (result == null)
                result = caseElement(ldifFileConnection);
            if (result == null)
                result = caseDeployedComponent(ldifFileConnection);
            if (result == null)
                result = casePackage(ldifFileConnection);
            if (result == null)
                result = caseNamespace(ldifFileConnection);
            if (result == null)
                result = caseModelElement(ldifFileConnection);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.FILE_EXCEL_CONNECTION: {
            FileExcelConnection fileExcelConnection = (FileExcelConnection) theEObject;
            T result = caseFileExcelConnection(fileExcelConnection);
            if (result == null)
                result = caseFileConnection(fileExcelConnection);
            if (result == null)
                result = caseConnection(fileExcelConnection);
            if (result == null)
                result = caseAbstractMetadataObject(fileExcelConnection);
            if (result == null)
                result = caseDataProvider(fileExcelConnection);
            if (result == null)
                result = caseDataManager(fileExcelConnection);
            if (result == null)
                result = caseElement(fileExcelConnection);
            if (result == null)
                result = caseDeployedComponent(fileExcelConnection);
            if (result == null)
                result = casePackage(fileExcelConnection);
            if (result == null)
                result = caseNamespace(fileExcelConnection);
            if (result == null)
                result = caseModelElement(fileExcelConnection);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.XML_XPATH_LOOP_DESCRIPTOR: {
            XmlXPathLoopDescriptor xmlXPathLoopDescriptor = (XmlXPathLoopDescriptor) theEObject;
            T result = caseXmlXPathLoopDescriptor(xmlXPathLoopDescriptor);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.GENERIC_SCHEMA_CONNECTION: {
            GenericSchemaConnection genericSchemaConnection = (GenericSchemaConnection) theEObject;
            T result = caseGenericSchemaConnection(genericSchemaConnection);
            if (result == null)
                result = caseConnection(genericSchemaConnection);
            if (result == null)
                result = caseAbstractMetadataObject(genericSchemaConnection);
            if (result == null)
                result = caseDataProvider(genericSchemaConnection);
            if (result == null)
                result = caseDataManager(genericSchemaConnection);
            if (result == null)
                result = caseElement(genericSchemaConnection);
            if (result == null)
                result = caseDeployedComponent(genericSchemaConnection);
            if (result == null)
                result = casePackage(genericSchemaConnection);
            if (result == null)
                result = caseNamespace(genericSchemaConnection);
            if (result == null)
                result = caseModelElement(genericSchemaConnection);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.LDAP_SCHEMA_CONNECTION: {
            LDAPSchemaConnection ldapSchemaConnection = (LDAPSchemaConnection) theEObject;
            T result = caseLDAPSchemaConnection(ldapSchemaConnection);
            if (result == null)
                result = caseConnection(ldapSchemaConnection);
            if (result == null)
                result = caseAbstractMetadataObject(ldapSchemaConnection);
            if (result == null)
                result = caseDataProvider(ldapSchemaConnection);
            if (result == null)
                result = caseDataManager(ldapSchemaConnection);
            if (result == null)
                result = caseElement(ldapSchemaConnection);
            if (result == null)
                result = caseDeployedComponent(ldapSchemaConnection);
            if (result == null)
                result = casePackage(ldapSchemaConnection);
            if (result == null)
                result = caseNamespace(ldapSchemaConnection);
            if (result == null)
                result = caseModelElement(ldapSchemaConnection);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.WSDL_SCHEMA_CONNECTION: {
            WSDLSchemaConnection wsdlSchemaConnection = (WSDLSchemaConnection) theEObject;
            T result = caseWSDLSchemaConnection(wsdlSchemaConnection);
            if (result == null)
                result = caseConnection(wsdlSchemaConnection);
            if (result == null)
                result = caseAbstractMetadataObject(wsdlSchemaConnection);
            if (result == null)
                result = caseDataProvider(wsdlSchemaConnection);
            if (result == null)
                result = caseDataManager(wsdlSchemaConnection);
            if (result == null)
                result = caseElement(wsdlSchemaConnection);
            if (result == null)
                result = caseDeployedComponent(wsdlSchemaConnection);
            if (result == null)
                result = casePackage(wsdlSchemaConnection);
            if (result == null)
                result = caseNamespace(wsdlSchemaConnection);
            if (result == null)
                result = caseModelElement(wsdlSchemaConnection);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION: {
            SalesforceSchemaConnection salesforceSchemaConnection = (SalesforceSchemaConnection) theEObject;
            T result = caseSalesforceSchemaConnection(salesforceSchemaConnection);
            if (result == null)
                result = caseConnection(salesforceSchemaConnection);
            if (result == null)
                result = caseAbstractMetadataObject(salesforceSchemaConnection);
            if (result == null)
                result = caseDataProvider(salesforceSchemaConnection);
            if (result == null)
                result = caseDataManager(salesforceSchemaConnection);
            if (result == null)
                result = caseElement(salesforceSchemaConnection);
            if (result == null)
                result = caseDeployedComponent(salesforceSchemaConnection);
            if (result == null)
                result = casePackage(salesforceSchemaConnection);
            if (result == null)
                result = caseNamespace(salesforceSchemaConnection);
            if (result == null)
                result = caseModelElement(salesforceSchemaConnection);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.CDC_CONNECTION: {
            CDCConnection cdcConnection = (CDCConnection) theEObject;
            T result = caseCDCConnection(cdcConnection);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.CDC_TYPE: {
            CDCType cdcType = (CDCType) theEObject;
            T result = caseCDCType(cdcType);
            if (result == null)
                result = caseAbstractMetadataObject(cdcType);
            if (result == null)
                result = caseModelElement(cdcType);
            if (result == null)
                result = caseElement(cdcType);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.SUBSCRIBER_TABLE: {
            SubscriberTable subscriberTable = (SubscriberTable) theEObject;
            T result = caseSubscriberTable(subscriberTable);
            if (result == null)
                result = caseTdTable(subscriberTable);
            if (result == null)
                result = caseMetadataTable(subscriberTable);
            if (result == null)
                result = caseTable(subscriberTable);
            if (result == null)
                result = caseAbstractMetadataObject(subscriberTable);
            if (result == null)
                result = caseNamedColumnSet(subscriberTable);
            if (result == null)
                result = caseClassifier(subscriberTable);
            if (result == null)
                result = caseColumnSet(subscriberTable);
            if (result == null)
                result = caseElement(subscriberTable);
            if (result == null)
                result = caseClass(subscriberTable);
            if (result == null)
                result = caseNamespace(subscriberTable);
            if (result == null)
                result = caseModelElement(subscriberTable);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.SAP_TEST_INPUT_PARAMETER_TABLE: {
            SAPTestInputParameterTable sapTestInputParameterTable = (SAPTestInputParameterTable) theEObject;
            T result = caseSAPTestInputParameterTable(sapTestInputParameterTable);
            if (result == null)
                result = caseSAPFunctionParameterTable(sapTestInputParameterTable);
            if (result == null)
                result = caseAbstractMetadataObject(sapTestInputParameterTable);
            if (result == null)
                result = caseModelElement(sapTestInputParameterTable);
            if (result == null)
                result = caseElement(sapTestInputParameterTable);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.CONCEPT: {
            Concept concept = (Concept) theEObject;
            T result = caseConcept(concept);
            if (result == null)
                result = caseTdTable(concept);
            if (result == null)
                result = caseMetadataTable(concept);
            if (result == null)
                result = caseTable(concept);
            if (result == null)
                result = caseAbstractMetadataObject(concept);
            if (result == null)
                result = caseNamedColumnSet(concept);
            if (result == null)
                result = caseClassifier(concept);
            if (result == null)
                result = caseColumnSet(concept);
            if (result == null)
                result = caseElement(concept);
            if (result == null)
                result = caseClass(concept);
            if (result == null)
                result = caseNamespace(concept);
            if (result == null)
                result = caseModelElement(concept);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.CONCEPT_TARGET: {
            ConceptTarget conceptTarget = (ConceptTarget) theEObject;
            T result = caseConceptTarget(conceptTarget);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.HL7_CONNECTION: {
            HL7Connection hl7Connection = (HL7Connection) theEObject;
            T result = caseHL7Connection(hl7Connection);
            if (result == null)
                result = caseFileConnection(hl7Connection);
            if (result == null)
                result = caseConnection(hl7Connection);
            if (result == null)
                result = caseAbstractMetadataObject(hl7Connection);
            if (result == null)
                result = caseDataProvider(hl7Connection);
            if (result == null)
                result = caseDataManager(hl7Connection);
            if (result == null)
                result = caseElement(hl7Connection);
            if (result == null)
                result = caseDeployedComponent(hl7Connection);
            if (result == null)
                result = casePackage(hl7Connection);
            if (result == null)
                result = caseNamespace(hl7Connection);
            if (result == null)
                result = caseModelElement(hl7Connection);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.HEADER_FOOTER_CONNECTION: {
            HeaderFooterConnection headerFooterConnection = (HeaderFooterConnection) theEObject;
            T result = caseHeaderFooterConnection(headerFooterConnection);
            if (result == null)
                result = caseConnection(headerFooterConnection);
            if (result == null)
                result = caseAbstractMetadataObject(headerFooterConnection);
            if (result == null)
                result = caseDataProvider(headerFooterConnection);
            if (result == null)
                result = caseDataManager(headerFooterConnection);
            if (result == null)
                result = caseElement(headerFooterConnection);
            if (result == null)
                result = caseDeployedComponent(headerFooterConnection);
            if (result == null)
                result = casePackage(headerFooterConnection);
            if (result == null)
                result = caseNamespace(headerFooterConnection);
            if (result == null)
                result = caseModelElement(headerFooterConnection);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.XML_FILE_NODE: {
            XMLFileNode xmlFileNode = (XMLFileNode) theEObject;
            T result = caseXMLFileNode(xmlFileNode);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.WSDL_PARAMETER: {
            WSDLParameter wsdlParameter = (WSDLParameter) theEObject;
            T result = caseWSDLParameter(wsdlParameter);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.GENERIC_PACKAGE: {
            GenericPackage genericPackage = (GenericPackage) theEObject;
            T result = caseGenericPackage(genericPackage);
            if (result == null)
                result = casePackage(genericPackage);
            if (result == null)
                result = caseNamespace(genericPackage);
            if (result == null)
                result = caseModelElement(genericPackage);
            if (result == null)
                result = caseElement(genericPackage);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.HL7_FILE_NODE: {
            HL7FileNode hl7FileNode = (HL7FileNode) theEObject;
            T result = caseHL7FileNode(hl7FileNode);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.FTP_CONNECTION: {
            FTPConnection ftpConnection = (FTPConnection) theEObject;
            T result = caseFTPConnection(ftpConnection);
            if (result == null)
                result = caseConnection(ftpConnection);
            if (result == null)
                result = caseAbstractMetadataObject(ftpConnection);
            if (result == null)
                result = caseDataProvider(ftpConnection);
            if (result == null)
                result = caseDataManager(ftpConnection);
            if (result == null)
                result = caseElement(ftpConnection);
            if (result == null)
                result = caseDeployedComponent(ftpConnection);
            if (result == null)
                result = casePackage(ftpConnection);
            if (result == null)
                result = caseNamespace(ftpConnection);
            if (result == null)
                result = caseModelElement(ftpConnection);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.BRMS_CONNECTION: {
            BRMSConnection brmsConnection = (BRMSConnection) theEObject;
            T result = caseBRMSConnection(brmsConnection);
            if (result == null)
                result = caseConnection(brmsConnection);
            if (result == null)
                result = caseAbstractMetadataObject(brmsConnection);
            if (result == null)
                result = caseDataProvider(brmsConnection);
            if (result == null)
                result = caseDataManager(brmsConnection);
            if (result == null)
                result = caseElement(brmsConnection);
            if (result == null)
                result = caseDeployedComponent(brmsConnection);
            if (result == null)
                result = casePackage(brmsConnection);
            if (result == null)
                result = caseNamespace(brmsConnection);
            if (result == null)
                result = caseModelElement(brmsConnection);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.VALIDATION_RULES_CONNECTION: {
            ValidationRulesConnection validationRulesConnection = (ValidationRulesConnection) theEObject;
            T result = caseValidationRulesConnection(validationRulesConnection);
            if (result == null)
                result = caseConnection(validationRulesConnection);
            if (result == null)
                result = caseAbstractMetadataObject(validationRulesConnection);
            if (result == null)
                result = caseDataProvider(validationRulesConnection);
            if (result == null)
                result = caseDataManager(validationRulesConnection);
            if (result == null)
                result = caseElement(validationRulesConnection);
            if (result == null)
                result = caseDeployedComponent(validationRulesConnection);
            if (result == null)
                result = casePackage(validationRulesConnection);
            if (result == null)
                result = caseNamespace(validationRulesConnection);
            if (result == null)
                result = caseModelElement(validationRulesConnection);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.CONDITION_TYPE: {
            ConditionType conditionType = (ConditionType) theEObject;
            T result = caseConditionType(conditionType);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.INNER_JOIN_MAP: {
            @SuppressWarnings("unchecked")
            Map.Entry<String, String> innerJoinMap = (Map.Entry<String, String>) theEObject;
            T result = caseInnerJoinMap(innerJoinMap);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.EDIFACT_CONNECTION: {
            EDIFACTConnection edifactConnection = (EDIFACTConnection) theEObject;
            T result = caseEDIFACTConnection(edifactConnection);
            if (result == null)
                result = caseConnection(edifactConnection);
            if (result == null)
                result = caseAbstractMetadataObject(edifactConnection);
            if (result == null)
                result = caseDataProvider(edifactConnection);
            if (result == null)
                result = caseDataManager(edifactConnection);
            if (result == null)
                result = caseElement(edifactConnection);
            if (result == null)
                result = caseDeployedComponent(edifactConnection);
            if (result == null)
                result = casePackage(edifactConnection);
            if (result == null)
                result = caseNamespace(edifactConnection);
            if (result == null)
                result = caseModelElement(edifactConnection);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.EDIFACT_COLUMN: {
            EDIFACTColumn edifactColumn = (EDIFACTColumn) theEObject;
            T result = caseEDIFACTColumn(edifactColumn);
            if (result == null)
                result = caseMetadataColumn(edifactColumn);
            if (result == null)
                result = caseAbstractMetadataObject(edifactColumn);
            if (result == null)
                result = caseField(edifactColumn);
            if (result == null)
                result = caseAttribute(edifactColumn);
            if (result == null)
                result = caseElement(edifactColumn);
            if (result == null)
                result = caseStructuralFeature(edifactColumn);
            if (result == null)
                result = caseFeature(edifactColumn);
            if (result == null)
                result = caseModelElement(edifactColumn);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.SALESFORCE_MODULE_UNIT: {
            SalesforceModuleUnit salesforceModuleUnit = (SalesforceModuleUnit) theEObject;
            T result = caseSalesforceModuleUnit(salesforceModuleUnit);
            if (result == null)
                result = caseAbstractMetadataObject(salesforceModuleUnit);
            if (result == null)
                result = caseModelElement(salesforceModuleUnit);
            if (result == null)
                result = caseElement(salesforceModuleUnit);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.ADDITIONAL_PROPERTIES: {
            @SuppressWarnings("unchecked")
            Map.Entry<String, String> additionalProperties = (Map.Entry<String, String>) theEObject;
            T result = caseAdditionalProperties(additionalProperties);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.SAP_TABLE: {
            SAPTable sapTable = (SAPTable) theEObject;
            T result = caseSAPTable(sapTable);
            if (result == null)
                result = caseMetadataTable(sapTable);
            if (result == null)
                result = caseAbstractMetadataObject(sapTable);
            if (result == null)
                result = caseClass(sapTable);
            if (result == null)
                result = caseClassifier(sapTable);
            if (result == null)
                result = caseElement(sapTable);
            if (result == null)
                result = caseNamespace(sapTable);
            if (result == null)
                result = caseModelElement(sapTable);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.SAP_TABLE_FIELD: {
            SAPTableField sapTableField = (SAPTableField) theEObject;
            T result = caseSAPTableField(sapTableField);
            if (result == null)
                result = caseMetadataColumn(sapTableField);
            if (result == null)
                result = caseAbstractMetadataObject(sapTableField);
            if (result == null)
                result = caseField(sapTableField);
            if (result == null)
                result = caseAttribute(sapTableField);
            if (result == null)
                result = caseElement(sapTableField);
            if (result == null)
                result = caseStructuralFeature(sapTableField);
            if (result == null)
                result = caseFeature(sapTableField);
            if (result == null)
                result = caseModelElement(sapTableField);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.SAP_FUNCTION_PARAMETER: {
            SAPFunctionParameter sapFunctionParameter = (SAPFunctionParameter) theEObject;
            T result = caseSAPFunctionParameter(sapFunctionParameter);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.SAP_FUNCTION_PARAM_DATA: {
            SAPFunctionParamData sapFunctionParamData = (SAPFunctionParamData) theEObject;
            T result = caseSAPFunctionParamData(sapFunctionParamData);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.ADDITIONAL_CONNECTION_PROPERTY: {
            AdditionalConnectionProperty additionalConnectionProperty = (AdditionalConnectionProperty) theEObject;
            T result = caseAdditionalConnectionProperty(additionalConnectionProperty);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.SAPBW_TABLE: {
            SAPBWTable sapbwTable = (SAPBWTable) theEObject;
            T result = caseSAPBWTable(sapbwTable);
            if (result == null)
                result = caseSAPTable(sapbwTable);
            if (result == null)
                result = caseMetadataTable(sapbwTable);
            if (result == null)
                result = caseAbstractMetadataObject(sapbwTable);
            if (result == null)
                result = caseClass(sapbwTable);
            if (result == null)
                result = caseClassifier(sapbwTable);
            if (result == null)
                result = caseElement(sapbwTable);
            if (result == null)
                result = caseNamespace(sapbwTable);
            if (result == null)
                result = caseModelElement(sapbwTable);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case ConnectionPackage.SAPBW_TABLE_FIELD: {
            SAPBWTableField sapbwTableField = (SAPBWTableField) theEObject;
            T result = caseSAPBWTableField(sapbwTableField);
            if (result == null)
                result = caseSAPTableField(sapbwTableField);
            if (result == null)
                result = caseMetadataColumn(sapbwTableField);
            if (result == null)
                result = caseAbstractMetadataObject(sapbwTableField);
            if (result == null)
                result = caseField(sapbwTableField);
            if (result == null)
                result = caseAttribute(sapbwTableField);
            if (result == null)
                result = caseElement(sapbwTableField);
            if (result == null)
                result = caseStructuralFeature(sapbwTableField);
            if (result == null)
                result = caseFeature(sapbwTableField);
            if (result == null)
                result = caseModelElement(sapbwTableField);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        default:
            return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Metadata</em>'.
     * <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Metadata</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMetadata(Metadata object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Connection</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Connection</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConnection(Connection object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Metadata Table</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Metadata Table</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMetadataTable(MetadataTable object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Metadata Column</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Metadata Column</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMetadataColumn(MetadataColumn object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Metadata Object</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Metadata Object</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractMetadataObject(AbstractMetadataObject object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>File Connection</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>File Connection</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFileConnection(FileConnection object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Delimited File Connection</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Delimited File Connection</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDelimitedFileConnection(DelimitedFileConnection object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Positional File Connection</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Positional File Connection</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePositionalFileConnection(PositionalFileConnection object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Ebcdic Connection</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Ebcdic Connection</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEbcdicConnection(EbcdicConnection object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>MDM Connection</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>MDM Connection</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMDMConnection(MDMConnection object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Database Connection</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Database Connection</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDatabaseConnection(DatabaseConnection object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>SAP Connection</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>SAP Connection</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSAPConnection(SAPConnection object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>SAP Function Unit</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>SAP Function Unit</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSAPFunctionUnit(SAPFunctionUnit object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>SAPI Doc Unit</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>SAPI Doc Unit</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSAPIDocUnit(SAPIDocUnit object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>SAP Function Parameter Column</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>SAP Function Parameter Column</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSAPFunctionParameterColumn(SAPFunctionParameterColumn object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>SAP Function Parameter Table</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>SAP Function Parameter Table</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSAPFunctionParameterTable(SAPFunctionParameterTable object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Input SAP Function Parameter Table</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Input SAP Function Parameter Table</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseInputSAPFunctionParameterTable(InputSAPFunctionParameterTable object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Output SAP Function Parameter Table</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Output SAP Function Parameter Table</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOutputSAPFunctionParameterTable(OutputSAPFunctionParameterTable object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Regexp File Connection</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Regexp File Connection</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRegexpFileConnection(RegexpFileConnection object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Xml File Connection</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Xml File Connection</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseXmlFileConnection(XmlFileConnection object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Schema Target</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Schema Target</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSchemaTarget(SchemaTarget object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Queries Connection</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Queries Connection</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseQueriesConnection(QueriesConnection object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Query</em>'.
     * <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Query</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseQuery(Query object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Ldif File Connection</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Ldif File Connection</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLdifFileConnection(LdifFileConnection object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>File Excel Connection</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>File Excel Connection</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFileExcelConnection(FileExcelConnection object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Xml XPath Loop Descriptor</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Xml XPath Loop Descriptor</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseXmlXPathLoopDescriptor(XmlXPathLoopDescriptor object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Generic Schema Connection</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Generic Schema Connection</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGenericSchemaConnection(GenericSchemaConnection object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>LDAP Schema Connection</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>LDAP Schema Connection</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLDAPSchemaConnection(LDAPSchemaConnection object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>WSDL Schema Connection</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>WSDL Schema Connection</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseWSDLSchemaConnection(WSDLSchemaConnection object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Salesforce Schema Connection</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Salesforce Schema Connection</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSalesforceSchemaConnection(SalesforceSchemaConnection object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>CDC Connection</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>CDC Connection</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCDCConnection(CDCConnection object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>CDC Type</em>'.
     * <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>CDC Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCDCType(CDCType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Subscriber Table</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Subscriber Table</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSubscriberTable(SubscriberTable object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>SAP Test Input Parameter Table</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>SAP Test Input Parameter Table</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSAPTestInputParameterTable(SAPTestInputParameterTable object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Concept</em>'.
     * <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Concept</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConcept(Concept object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Concept Target</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Concept Target</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConceptTarget(ConceptTarget object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>HL7 Connection</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>HL7 Connection</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseHL7Connection(HL7Connection object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Generic Package</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Generic Package</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGenericPackage(GenericPackage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>HL7 File Node</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>HL7 File Node</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseHL7FileNode(HL7FileNode object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>FTP Connection</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>FTP Connection</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFTPConnection(FTPConnection object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>BRMS Connection</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>BRMS Connection</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBRMSConnection(BRMSConnection object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Condition Type</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Condition Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConditionType(ConditionType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Inner Join Map</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Inner Join Map</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseInnerJoinMap(Map.Entry<String, String> object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EDIFACT Connection</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EDIFACT Connection</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEDIFACTConnection(EDIFACTConnection object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Salesforce Module Unit</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Salesforce Module Unit</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSalesforceModuleUnit(SalesforceModuleUnit object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Additional Properties</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Additional Properties</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAdditionalProperties(Map.Entry<String, String> object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>SAP Table Field</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>SAP Table Field</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSAPTableField(SAPTableField object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>SAP Function Parameter</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>SAP Function Parameter</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSAPFunctionParameter(SAPFunctionParameter object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>SAP Function Param Data</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>SAP Function Param Data</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSAPFunctionParamData(SAPFunctionParamData object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Additional Connection Property</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Additional Connection Property</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAdditionalConnectionProperty(AdditionalConnectionProperty object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>SAPBW Table</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>SAPBW Table</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSAPBWTable(SAPBWTable object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>SAPBW Table Field</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>SAPBW Table Field</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSAPBWTableField(SAPBWTableField object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>SAP Table</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>SAP Table</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSAPTable(SAPTable object) {
        return null;
    }

    /**
     * ======= Returns the result of interpreting the object as an instance of '<em>EDIFACT Column</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EDIFACT Column</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEDIFACTColumn(EDIFACTColumn object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Validation Rules Connection</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Validation Rules Connection</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseValidationRulesConnection(ValidationRulesConnection object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Element</em>'.
     * <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseElement(Element object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Model Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Model Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseModelElement(ModelElement object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Namespace</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Namespace</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNamespace(Namespace object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Package</em>'.
     * <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Package</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePackage(orgomg.cwm.objectmodel.core.Package object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Deployed Component</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Deployed Component</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDeployedComponent(DeployedComponent object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Data Manager</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Data Manager</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDataManager(DataManager object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Data Provider</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Data Provider</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDataProvider(DataProvider object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Feature</em>'.
     * <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Feature</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFeature(Feature object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Structural Feature</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Structural Feature</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStructuralFeature(StructuralFeature object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Attribute</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Attribute</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAttribute(Attribute object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Field</em>'.
     * <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Field</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseField(Field object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Classifier</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Classifier</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseClassifier(Classifier object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Class</em>'.
     * <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Class</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseClass(orgomg.cwm.objectmodel.core.Class object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Column Set</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Column Set</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseColumnSet(ColumnSet object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Named Column Set</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Named Column Set</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNamedColumnSet(NamedColumnSet object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Table</em>'.
     * <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Table</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTable(Table object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Td Table</em>'.
     * <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Td Table</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTdTable(TdTable object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Header Footer Connection</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Header Footer Connection</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseHeaderFooterConnection(HeaderFooterConnection object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>XML File Node</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>XML File Node</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseXMLFileNode(XMLFileNode object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>WSDL Parameter</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>WSDL Parameter</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseWSDLParameter(WSDLParameter object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
     * <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch, but this is the last case
     * anyway. <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject)
     * @generated
     */
    public T defaultCase(EObject object) {
        return null;
    }

} // ConnectionSwitch
