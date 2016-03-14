/**
 * <copyright> </copyright>
 * 
 * $Id: ConnectionAdapterFactory.java 89342 2012-08-20 03:46:34Z ycbai $
 */
package org.talend.core.model.metadata.builder.connection.util;

import java.util.Map;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
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
 * <!-- begin-user-doc --> The <b>Adapter Factory</b> for the model. It provides an adapter <code>createXXX</code>
 * method for each class of the model. <!-- end-user-doc -->
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage
 * @generated
 */
public class ConnectionAdapterFactory extends AdapterFactoryImpl {

    /**
     * The cached model package.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected static ConnectionPackage modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ConnectionAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = ConnectionPackage.eINSTANCE;
        }
    }

    /**
     * Returns whether this factory is applicable for the type of the object.
     * <!-- begin-user-doc --> This
     * implementation returns <code>true</code> if the object is either the model's package or is an instance object of
     * the model. <!-- end-user-doc -->
     * @return whether this factory is applicable for the type of the object.
     * @generated
     */
    @Override
    public boolean isFactoryForType(Object object) {
        if (object == modelPackage) {
            return true;
        }
        if (object instanceof EObject) {
            return ((EObject) object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
     * The switch that delegates to the <code>createXXX</code> methods.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */

    protected ConnectionSwitch<Adapter> modelSwitch = new ConnectionSwitch<Adapter>() {

        @Override
        public Adapter caseMetadata(Metadata object) {
            return createMetadataAdapter();
        }

        @Override
        public Adapter caseConnection(Connection object) {
            return createConnectionAdapter();
        }

        @Override
        public Adapter caseMetadataColumn(MetadataColumn object) {
            return createMetadataColumnAdapter();
        }

        @Override
        public Adapter caseAbstractMetadataObject(AbstractMetadataObject object) {
            return createAbstractMetadataObjectAdapter();
        }

        @Override
        public Adapter caseMetadataTable(MetadataTable object) {
            return createMetadataTableAdapter();
        }

        @Override
        public Adapter caseFileConnection(FileConnection object) {
            return createFileConnectionAdapter();
        }

        @Override
        public Adapter caseDelimitedFileConnection(DelimitedFileConnection object) {
            return createDelimitedFileConnectionAdapter();
        }

        @Override
        public Adapter casePositionalFileConnection(PositionalFileConnection object) {
            return createPositionalFileConnectionAdapter();
        }

        @Override
        public Adapter caseEbcdicConnection(EbcdicConnection object) {
            return createEbcdicConnectionAdapter();
        }

        @Override
        public Adapter caseMDMConnection(MDMConnection object) {
            return createMDMConnectionAdapter();
        }

        @Override
        public Adapter caseDatabaseConnection(DatabaseConnection object) {
            return createDatabaseConnectionAdapter();
        }

        @Override
        public Adapter caseSAPConnection(SAPConnection object) {
            return createSAPConnectionAdapter();
        }

        @Override
        public Adapter caseSAPFunctionUnit(SAPFunctionUnit object) {
            return createSAPFunctionUnitAdapter();
        }

        @Override
        public Adapter caseSAPIDocUnit(SAPIDocUnit object) {
            return createSAPIDocUnitAdapter();
        }

        @Override
        public Adapter caseSAPFunctionParameterColumn(SAPFunctionParameterColumn object) {
            return createSAPFunctionParameterColumnAdapter();
        }

        @Override
        public Adapter caseSAPFunctionParameterTable(SAPFunctionParameterTable object) {
            return createSAPFunctionParameterTableAdapter();
        }

        @Override
        public Adapter caseInputSAPFunctionParameterTable(InputSAPFunctionParameterTable object) {
            return createInputSAPFunctionParameterTableAdapter();
        }

        @Override
        public Adapter caseOutputSAPFunctionParameterTable(OutputSAPFunctionParameterTable object) {
            return createOutputSAPFunctionParameterTableAdapter();
        }

        @Override
        public Adapter caseRegexpFileConnection(RegexpFileConnection object) {
            return createRegexpFileConnectionAdapter();
        }

        @Override
        public Adapter caseXmlFileConnection(XmlFileConnection object) {
            return createXmlFileConnectionAdapter();
        }

        @Override
        public Adapter caseSchemaTarget(SchemaTarget object) {
            return createSchemaTargetAdapter();
        }

        @Override
        public Adapter caseQueriesConnection(QueriesConnection object) {
            return createQueriesConnectionAdapter();
        }

        @Override
        public Adapter caseQuery(Query object) {
            return createQueryAdapter();
        }

        @Override
        public Adapter caseLdifFileConnection(LdifFileConnection object) {
            return createLdifFileConnectionAdapter();
        }

        @Override
        public Adapter caseFileExcelConnection(FileExcelConnection object) {
            return createFileExcelConnectionAdapter();
        }

        @Override
        public Adapter caseXmlXPathLoopDescriptor(XmlXPathLoopDescriptor object) {
            return createXmlXPathLoopDescriptorAdapter();
        }

        @Override
        public Adapter caseGenericSchemaConnection(GenericSchemaConnection object) {
            return createGenericSchemaConnectionAdapter();
        }

        @Override
        public Adapter caseLDAPSchemaConnection(LDAPSchemaConnection object) {
            return createLDAPSchemaConnectionAdapter();
        }

        @Override
        public Adapter caseWSDLSchemaConnection(WSDLSchemaConnection object) {
            return createWSDLSchemaConnectionAdapter();
        }

        @Override
        public Adapter caseSalesforceSchemaConnection(SalesforceSchemaConnection object) {
            return createSalesforceSchemaConnectionAdapter();
        }

        @Override
        public Adapter caseCDCConnection(CDCConnection object) {
            return createCDCConnectionAdapter();
        }

        @Override
        public Adapter caseCDCType(CDCType object) {
            return createCDCTypeAdapter();
        }

        @Override
        public Adapter caseSubscriberTable(SubscriberTable object) {
            return createSubscriberTableAdapter();
        }

        @Override
        public Adapter caseSAPTestInputParameterTable(SAPTestInputParameterTable object) {
            return createSAPTestInputParameterTableAdapter();
        }

        @Override
        public Adapter caseConcept(Concept object) {
            return createConceptAdapter();
        }

        @Override
        public Adapter caseConceptTarget(ConceptTarget object) {
            return createConceptTargetAdapter();
        }

        @Override
        public Adapter caseHL7Connection(HL7Connection object) {
            return createHL7ConnectionAdapter();
        }

        @Override
        public Adapter caseHeaderFooterConnection(HeaderFooterConnection object) {
            return createHeaderFooterConnectionAdapter();
        }

        @Override
        public Adapter caseXMLFileNode(XMLFileNode object) {
            return createXMLFileNodeAdapter();
        }

        @Override
        public Adapter caseWSDLParameter(WSDLParameter object) {
            return createWSDLParameterAdapter();
        }

        @Override
        public Adapter caseGenericPackage(GenericPackage object) {
            return createGenericPackageAdapter();
        }

        @Override
        public Adapter caseHL7FileNode(HL7FileNode object) {
            return createHL7FileNodeAdapter();
        }

        @Override
        public Adapter caseFTPConnection(FTPConnection object) {
            return createFTPConnectionAdapter();
        }

        @Override
        public Adapter caseBRMSConnection(BRMSConnection object) {
            return createBRMSConnectionAdapter();
        }

        @Override
        public Adapter caseValidationRulesConnection(ValidationRulesConnection object) {
            return createValidationRulesConnectionAdapter();
        }

        @Override
        public Adapter caseConditionType(ConditionType object) {
            return createConditionTypeAdapter();
        }

        @Override
        public Adapter caseInnerJoinMap(Map.Entry<String, String> object) {
            return createInnerJoinMapAdapter();
        }

        @Override
        public Adapter caseEDIFACTConnection(EDIFACTConnection object) {
            return createEDIFACTConnectionAdapter();
        }

        @Override
        public Adapter caseEDIFACTColumn(EDIFACTColumn object) {
            return createEDIFACTColumnAdapter();
        }

        @Override
        public Adapter caseSalesforceModuleUnit(SalesforceModuleUnit object) {
            return createSalesforceModuleUnitAdapter();
        }

        @Override
        public Adapter caseAdditionalProperties(Map.Entry<String, String> object) {
            return createAdditionalPropertiesAdapter();
        }

        @Override
        public Adapter caseSAPTable(SAPTable object) {
            return createSAPTableAdapter();
        }

        @Override
        public Adapter caseSAPTableField(SAPTableField object) {
            return createSAPTableFieldAdapter();
        }

        @Override
        public Adapter caseSAPFunctionParameter(SAPFunctionParameter object) {
            return createSAPFunctionParameterAdapter();
        }

        @Override
        public Adapter caseSAPFunctionParamData(SAPFunctionParamData object) {
            return createSAPFunctionParamDataAdapter();
        }

        @Override
        public Adapter caseAdditionalConnectionProperty(AdditionalConnectionProperty object) {
            return createAdditionalConnectionPropertyAdapter();
        }

        @Override
        public Adapter caseSAPBWTable(SAPBWTable object) {
            return createSAPBWTableAdapter();
        }

        @Override
        public Adapter caseSAPBWTableField(SAPBWTableField object) {
            return createSAPBWTableFieldAdapter();
        }

        @Override
        public Adapter caseElement(Element object) {
            return createElementAdapter();
        }

        @Override
        public Adapter caseModelElement(ModelElement object) {
            return createModelElementAdapter();
        }

        @Override
        public Adapter caseNamespace(Namespace object) {
            return createNamespaceAdapter();
        }

        @Override
        public Adapter casePackage(orgomg.cwm.objectmodel.core.Package object) {
            return createPackageAdapter();
        }

        @Override
        public Adapter caseDeployedComponent(DeployedComponent object) {
            return createDeployedComponentAdapter();
        }

        @Override
        public Adapter caseDataManager(DataManager object) {
            return createDataManagerAdapter();
        }

        @Override
        public Adapter caseDataProvider(DataProvider object) {
            return createDataProviderAdapter();
        }

        @Override
        public Adapter caseFeature(Feature object) {
            return createFeatureAdapter();
        }

        @Override
        public Adapter caseStructuralFeature(StructuralFeature object) {
            return createStructuralFeatureAdapter();
        }

        @Override
        public Adapter caseAttribute(Attribute object) {
            return createAttributeAdapter();
        }

        @Override
        public Adapter caseField(Field object) {
            return createFieldAdapter();
        }

        @Override
        public Adapter caseClassifier(Classifier object) {
            return createClassifierAdapter();
        }

        @Override
        public Adapter caseClass(orgomg.cwm.objectmodel.core.Class object) {
            return createClassAdapter();
        }

        @Override
        public Adapter caseColumnSet(ColumnSet object) {
            return createColumnSetAdapter();
        }

        @Override
        public Adapter caseNamedColumnSet(NamedColumnSet object) {
            return createNamedColumnSetAdapter();
        }

        @Override
        public Adapter caseTable(Table object) {
            return createTableAdapter();
        }

        @Override
        public Adapter caseTdTable(TdTable object) {
            return createTdTableAdapter();
        }

        @Override
        public Adapter defaultCase(EObject object) {
            return createEObjectAdapter();
        }
    };

    /**
     * Creates an adapter for the <code>target</code>.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param target the object to adapt.
     * @return the adapter for the <code>target</code>.
     * @generated
     */
    @Override
    public Adapter createAdapter(Notifier target) {
        return modelSwitch.doSwitch((EObject) target);
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.Metadata
     * <em>Metadata</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can easily
     * ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc
     * -->
     * 
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.Metadata
     * @generated
     */
    public Adapter createMetadataAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.Connection <em>Connection</em>}'.
     * <!-- begin-user-doc
     * --> This default implementation returns null so that we can easily ignore cases; it's useful to ignore a case
     * when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.Connection
     * @generated
     */
    public Adapter createConnectionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.talend.core.model.metadata.builder.connection.MetadataColumn <em>Metadata Column</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.MetadataColumn
     * @generated
     */
    public Adapter createMetadataColumnAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.AbstractMetadataObject <em>Abstract Metadata Object</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we
     * can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.AbstractMetadataObject
     * @generated
     */
    public Adapter createAbstractMetadataObjectAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.talend.core.model.metadata.builder.connection.MetadataTable <em>Metadata Table</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.MetadataTable
     * @generated
     */
    public Adapter createMetadataTableAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.talend.core.model.metadata.builder.connection.FileConnection <em>File Connection</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.FileConnection
     * @generated
     */
    public Adapter createFileConnectionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.DelimitedFileConnection <em>Delimited File Connection</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we
     * can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.DelimitedFileConnection
     * @generated
     */
    public Adapter createDelimitedFileConnectionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.PositionalFileConnection <em>Positional File Connection</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that
     * we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.PositionalFileConnection
     * @generated
     */
    public Adapter createPositionalFileConnectionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.talend.core.model.metadata.builder.connection.EbcdicConnection <em>Ebcdic Connection</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.EbcdicConnection
     * @generated
     */
    public Adapter createEbcdicConnectionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.talend.core.model.metadata.builder.connection.MDMConnection <em>MDM Connection</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.MDMConnection
     * @generated
     */
    public Adapter createMDMConnectionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.talend.core.model.metadata.builder.connection.DatabaseConnection <em>Database Connection</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.DatabaseConnection
     * @generated
     */
    public Adapter createDatabaseConnectionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.talend.core.model.metadata.builder.connection.SAPConnection <em>SAP Connection</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.SAPConnection
     * @generated
     */
    public Adapter createSAPConnectionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.talend.core.model.metadata.builder.connection.SAPFunctionUnit <em>SAP Function Unit</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.SAPFunctionUnit
     * @generated
     */
    public Adapter createSAPFunctionUnitAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.talend.core.model.metadata.builder.connection.SAPIDocUnit <em>SAPI Doc Unit</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.SAPIDocUnit
     * @generated
     */
    public Adapter createSAPIDocUnitAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameterColumn <em>SAP Function Parameter Column</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so
     * that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.SAPFunctionParameterColumn
     * @generated
     */
    public Adapter createSAPFunctionParameterColumnAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameterTable <em>SAP Function Parameter Table</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that
     * we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.SAPFunctionParameterTable
     * @generated
     */
    public Adapter createSAPFunctionParameterTableAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.InputSAPFunctionParameterTable <em>Input SAP Function Parameter Table</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null
     * so that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases
     * anyway. <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.InputSAPFunctionParameterTable
     * @generated
     */
    public Adapter createInputSAPFunctionParameterTableAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.OutputSAPFunctionParameterTable <em>Output SAP Function Parameter Table</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null
     * so that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases
     * anyway. <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.OutputSAPFunctionParameterTable
     * @generated
     */
    public Adapter createOutputSAPFunctionParameterTableAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.RegexpFileConnection <em>Regexp File Connection</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful
     * to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.RegexpFileConnection
     * @generated
     */
    public Adapter createRegexpFileConnectionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.talend.core.model.metadata.builder.connection.XmlFileConnection <em>Xml File Connection</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.XmlFileConnection
     * @generated
     */
    public Adapter createXmlFileConnectionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.talend.core.model.metadata.builder.connection.SchemaTarget <em>Schema Target</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.SchemaTarget
     * @generated
     */
    public Adapter createSchemaTargetAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.talend.core.model.metadata.builder.connection.QueriesConnection <em>Queries Connection</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.QueriesConnection
     * @generated
     */
    public Adapter createQueriesConnectionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.Query <em>Query</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.Query
     * @generated
     */
    public Adapter createQueryAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.talend.core.model.metadata.builder.connection.LdifFileConnection <em>Ldif File Connection</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.LdifFileConnection
     * @generated
     */
    public Adapter createLdifFileConnectionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.FileExcelConnection <em>File Excel Connection</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful
     * to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.FileExcelConnection
     * @generated
     */
    public Adapter createFileExcelConnectionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.XmlXPathLoopDescriptor <em>Xml XPath Loop Descriptor</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we
     * can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.XmlXPathLoopDescriptor
     * @generated
     */
    public Adapter createXmlXPathLoopDescriptorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.GenericSchemaConnection <em>Generic Schema Connection</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we
     * can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.GenericSchemaConnection
     * @generated
     */
    public Adapter createGenericSchemaConnectionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection <em>LDAP Schema Connection</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful
     * to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection
     * @generated
     */
    public Adapter createLDAPSchemaConnectionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection <em>WSDL Schema Connection</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful
     * to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection
     * @generated
     */
    public Adapter createWSDLSchemaConnectionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.SalesforceSchemaConnection <em>Salesforce Schema Connection</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that
     * we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.SalesforceSchemaConnection
     * @generated
     */
    public Adapter createSalesforceSchemaConnectionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.talend.core.model.metadata.builder.connection.CDCConnection <em>CDC Connection</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.CDCConnection
     * @generated
     */
    public Adapter createCDCConnectionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.CDCType
     * <em>CDC Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can easily
     * ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc
     * -->
     * 
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.CDCType
     * @generated
     */
    public Adapter createCDCTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.talend.core.model.metadata.builder.connection.SubscriberTable <em>Subscriber Table</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.SubscriberTable
     * @generated
     */
    public Adapter createSubscriberTableAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.SAPTestInputParameterTable <em>SAP Test Input Parameter Table</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so
     * that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.SAPTestInputParameterTable
     * @generated
     */
    public Adapter createSAPTestInputParameterTableAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.Concept <em>Concept</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.Concept
     * @generated
     */
    public Adapter createConceptAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.talend.core.model.metadata.builder.connection.ConceptTarget <em>Concept Target</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.ConceptTarget
     * @generated
     */
    public Adapter createConceptTargetAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.talend.core.model.metadata.builder.connection.HL7Connection <em>HL7 Connection</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.HL7Connection
     * @generated
     */
    public Adapter createHL7ConnectionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.HeaderFooterConnection <em>Header Footer Connection</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we
     * can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.HeaderFooterConnection
     * @generated
     */
    public Adapter createHeaderFooterConnectionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.talend.core.model.metadata.builder.connection.XMLFileNode <em>XML File Node</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.XMLFileNode
     * @generated
     */
    public Adapter createXMLFileNodeAdapter() {
        return null;
    }

    /**
     * <<<<<<< .mine Creates a new adapter for an object of class '
     * {@link org.talend.core.model.metadata.builder.connection.WSDLParameter <em>WSDL Parameter</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.WSDLParameter
     * @generated
     */
    public Adapter createWSDLParameterAdapter() {
        return null;
    }

    /**
     * ======= Creates a new adapter for an object of class '
     * {@link org.talend.core.model.metadata.builder.connection.GenericPackage <em>Generic Package</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.GenericPackage
     * @generated
     */
    public Adapter createGenericPackageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.talend.core.model.metadata.builder.connection.HL7FileNode <em>HL7 File Node</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.HL7FileNode
     * @generated
     */
    public Adapter createHL7FileNodeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.talend.core.model.metadata.builder.connection.FTPConnection <em>FTP Connection</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.FTPConnection
     * @generated
     */
    public Adapter createFTPConnectionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.talend.core.model.metadata.builder.connection.BRMSConnection <em>BRMS Connection</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.BRMSConnection
     * @generated
     */
    public Adapter createBRMSConnectionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.talend.core.model.metadata.builder.connection.ConditionType <em>Condition Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.ConditionType
     * @generated
     */
    public Adapter createConditionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link java.util.Map.Entry <em>Inner Join Map</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see java.util.Map.Entry
     * @generated
     */
    public Adapter createInnerJoinMapAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.talend.core.model.metadata.builder.connection.EDIFACTConnection <em>EDIFACT Connection</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.EDIFACTConnection
     * @generated
     */
    public Adapter createEDIFACTConnectionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.SalesforceModuleUnit <em>Salesforce Module Unit</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful
     * to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.SalesforceModuleUnit
     * @generated
     */
    public Adapter createSalesforceModuleUnitAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link java.util.Map.Entry <em>Additional Properties</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see java.util.Map.Entry
     * @generated
     */
    public Adapter createAdditionalPropertiesAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.SAPTableField <em>SAP Table Field</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.SAPTableField
     * @generated
     */
    public Adapter createSAPTableFieldAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameter <em>SAP Function Parameter</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.SAPFunctionParameter
     * @generated
     */
    public Adapter createSAPFunctionParameterAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParamData <em>SAP Function Param Data</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.SAPFunctionParamData
     * @generated
     */
    public Adapter createSAPFunctionParamDataAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.AdditionalConnectionProperty <em>Additional Connection Property</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.AdditionalConnectionProperty
     * @generated
     */
    public Adapter createAdditionalConnectionPropertyAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.SAPBWTable <em>SAPBW Table</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.SAPBWTable
     * @generated
     */
    public Adapter createSAPBWTableAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.SAPBWTableField <em>SAPBW Table Field</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.SAPBWTableField
     * @generated
     */
    public Adapter createSAPBWTableFieldAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.SAPTable <em>SAP Table</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.SAPTable
     * @generated
     */
    public Adapter createSAPTableAdapter() {
        return null;
    }

    /**
     * ======= Creates a new adapter for an object of class '
     * {@link org.talend.core.model.metadata.builder.connection.EDIFACTColumn <em>EDIFACT Column</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.EDIFACTColumn
     * @generated
     */
    public Adapter createEDIFACTColumnAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.ValidationRulesConnection <em>Validation Rules Connection</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that
     * we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.ValidationRulesConnection
     * @generated
     */
    public Adapter createValidationRulesConnectionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.objectmodel.core.Element <em>Element</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see orgomg.cwm.objectmodel.core.Element
     * @generated
     */
    public Adapter createElementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.objectmodel.core.ModelElement
     * <em>Model Element</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can easily
     * ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc
     * -->
     * 
     * @return the new adapter.
     * @see orgomg.cwm.objectmodel.core.ModelElement
     * @generated
     */
    public Adapter createModelElementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.objectmodel.core.Namespace <em>Namespace</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful
     * to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.objectmodel.core.Namespace
     * @generated
     */
    public Adapter createNamespaceAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.objectmodel.core.Package <em>Package</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see orgomg.cwm.objectmodel.core.Package
     * @generated
     */
    public Adapter createPackageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.foundation.softwaredeployment.DeployedComponent <em>Deployed Component</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can
     * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.foundation.softwaredeployment.DeployedComponent
     * @generated
     */
    public Adapter createDeployedComponentAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.foundation.softwaredeployment.DataManager
     * <em>Data Manager</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can easily
     * ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc
     * -->
     * 
     * @return the new adapter.
     * @see orgomg.cwm.foundation.softwaredeployment.DataManager
     * @generated
     */
    public Adapter createDataManagerAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.foundation.softwaredeployment.DataProvider
     * <em>Data Provider</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can easily
     * ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc
     * -->
     * 
     * @return the new adapter.
     * @see orgomg.cwm.foundation.softwaredeployment.DataProvider
     * @generated
     */
    public Adapter createDataProviderAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.objectmodel.core.Feature <em>Feature</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see orgomg.cwm.objectmodel.core.Feature
     * @generated
     */
    public Adapter createFeatureAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.objectmodel.core.StructuralFeature <em>Structural Feature</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can
     * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.objectmodel.core.StructuralFeature
     * @generated
     */
    public Adapter createStructuralFeatureAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.objectmodel.core.Attribute <em>Attribute</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful
     * to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.objectmodel.core.Attribute
     * @generated
     */
    public Adapter createAttributeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.resource.record.Field <em>Field</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see orgomg.cwm.resource.record.Field
     * @generated
     */
    public Adapter createFieldAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.objectmodel.core.Classifier <em>Classifier</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's
     * useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.objectmodel.core.Classifier
     * @generated
     */
    public Adapter createClassifierAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.objectmodel.core.Class <em>Class</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see orgomg.cwm.objectmodel.core.Class
     * @generated
     */
    public Adapter createClassAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.resource.relational.ColumnSet
     * <em>Column Set</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can easily
     * ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc
     * -->
     * 
     * @return the new adapter.
     * @see orgomg.cwm.resource.relational.ColumnSet
     * @generated
     */
    public Adapter createColumnSetAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.resource.relational.NamedColumnSet <em>Named Column Set</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can
     * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.resource.relational.NamedColumnSet
     * @generated
     */
    public Adapter createNamedColumnSetAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.resource.relational.Table <em>Table</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see orgomg.cwm.resource.relational.Table
     * @generated
     */
    public Adapter createTableAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.cwm.relational.TdTable <em>Td Table</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.talend.cwm.relational.TdTable
     * @generated
     */
    public Adapter createTdTableAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for the default case.
     * <!-- begin-user-doc --> This default implementation returns null.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @generated
     */
    public Adapter createEObjectAdapter() {
        return null;
    }

} // ConnectionAdapterFactory
