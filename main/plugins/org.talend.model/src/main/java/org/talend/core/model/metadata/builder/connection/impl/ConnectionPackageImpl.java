/**
 * <copyright> </copyright>
 * 
 * $Id: ConnectionPackageImpl.java 113672 2014-01-26 10:46:50Z msjian $
 */
package org.talend.core.model.metadata.builder.connection.impl;

import static org.talend.core.model.metadata.builder.connection.ConnectionPackage.ESCAPE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.talend.core.model.metadata.builder.connection.AbstractMetadataObject;
import org.talend.core.model.metadata.builder.connection.AdditionalConnectionProperty;
import org.talend.core.model.metadata.builder.connection.BRMSConnection;
import org.talend.core.model.metadata.builder.connection.CDCConnection;
import org.talend.core.model.metadata.builder.connection.CDCType;
import org.talend.core.model.metadata.builder.connection.Concept;
import org.talend.core.model.metadata.builder.connection.ConceptTarget;
import org.talend.core.model.metadata.builder.connection.ConditionType;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.EDIFACTColumn;
import org.talend.core.model.metadata.builder.connection.EDIFACTConnection;
import org.talend.core.model.metadata.builder.connection.EbcdicConnection;
import org.talend.core.model.metadata.builder.connection.Escape;
import org.talend.core.model.metadata.builder.connection.FTPConnection;
import org.talend.core.model.metadata.builder.connection.FieldSeparator;
import org.talend.core.model.metadata.builder.connection.FileConnection;
import org.talend.core.model.metadata.builder.connection.FileExcelConnection;
import org.talend.core.model.metadata.builder.connection.FileFormat;
import org.talend.core.model.metadata.builder.connection.Function;
import org.talend.core.model.metadata.builder.connection.GenericPackage;
import org.talend.core.model.metadata.builder.connection.GenericSchemaConnection;
import org.talend.core.model.metadata.builder.connection.HL7Connection;
import org.talend.core.model.metadata.builder.connection.HL7FileNode;
import org.talend.core.model.metadata.builder.connection.HeaderFooterConnection;
import org.talend.core.model.metadata.builder.connection.InputSAPFunctionParameterTable;
import org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection;
import org.talend.core.model.metadata.builder.connection.LdifFileConnection;
import org.talend.core.model.metadata.builder.connection.LogicalOperator;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.core.model.metadata.builder.connection.MDMConnectionProtocol;
import org.talend.core.model.metadata.builder.connection.MdmConceptType;
import org.talend.core.model.metadata.builder.connection.Metadata;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.Operator;
import org.talend.core.model.metadata.builder.connection.OutputSAPFunctionParameterTable;
import org.talend.core.model.metadata.builder.connection.PositionalFileConnection;
import org.talend.core.model.metadata.builder.connection.QueriesConnection;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.core.model.metadata.builder.connection.RegexpFileConnection;
import org.talend.core.model.metadata.builder.connection.RowSeparator;
import org.talend.core.model.metadata.builder.connection.RuleType;
import org.talend.core.model.metadata.builder.connection.SAPBWTable;
import org.talend.core.model.metadata.builder.connection.SAPBWTableField;
import org.talend.core.model.metadata.builder.connection.SAPConnection;
import org.talend.core.model.metadata.builder.connection.SAPFunctionParamData;
import org.talend.core.model.metadata.builder.connection.SAPFunctionParameter;
import org.talend.core.model.metadata.builder.connection.SAPFunctionParameterColumn;
import org.talend.core.model.metadata.builder.connection.SAPFunctionParameterTable;
import org.talend.core.model.metadata.builder.connection.SAPFunctionUnit;
import org.talend.core.model.metadata.builder.connection.SAPIDocUnit;
import org.talend.core.model.metadata.builder.connection.SAPTable;
import org.talend.core.model.metadata.builder.connection.SAPTableField;
import org.talend.core.model.metadata.builder.connection.SAPTestInputParameterTable;
import org.talend.core.model.metadata.builder.connection.SalesforceModuleUnit;
import org.talend.core.model.metadata.builder.connection.SalesforceSchemaConnection;
import org.talend.core.model.metadata.builder.connection.SchemaTarget;
import org.talend.core.model.metadata.builder.connection.SubscriberTable;
import org.talend.core.model.metadata.builder.connection.ValidationRulesConnection;
import org.talend.core.model.metadata.builder.connection.WSDLParameter;
import org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection;
import org.talend.core.model.metadata.builder.connection.XMLFileNode;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.model.metadata.builder.connection.XmlXPathLoopDescriptor;
import org.talend.cwm.constants.ConstantsPackage;
import org.talend.cwm.constants.impl.ConstantsPackageImpl;
import org.talend.cwm.relational.impl.RelationalPackageImpl;
import org.talend.cwm.softwaredeployment.impl.SoftwaredeploymentPackageImpl;
import org.talend.cwm.xml.impl.XmlPackageImpl;
import orgomg.cwm.analysis.businessnomenclature.BusinessnomenclaturePackage;
import orgomg.cwm.analysis.datamining.DataminingPackage;
import orgomg.cwm.analysis.informationvisualization.InformationvisualizationPackage;
import orgomg.cwm.analysis.olap.OlapPackage;
import orgomg.cwm.analysis.transformation.TransformationPackage;
import orgomg.cwm.foundation.businessinformation.BusinessinformationPackage;
import orgomg.cwm.foundation.datatypes.DatatypesPackage;
import orgomg.cwm.foundation.expressions.ExpressionsPackage;
import orgomg.cwm.foundation.keysindexes.KeysindexesPackage;
import orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage;
import orgomg.cwm.foundation.typemapping.TypemappingPackage;
import orgomg.cwm.management.warehouseoperation.WarehouseoperationPackage;
import orgomg.cwm.management.warehouseprocess.WarehouseprocessPackage;
import orgomg.cwm.objectmodel.behavioral.BehavioralPackage;
import orgomg.cwm.objectmodel.core.CorePackage;
import orgomg.cwm.objectmodel.instance.InstancePackage;
import orgomg.cwm.objectmodel.relationships.RelationshipsPackage;
import orgomg.cwm.resource.multidimensional.MultidimensionalPackage;
import orgomg.cwm.resource.record.RecordPackage;
import orgomg.cwm.resource.relational.RelationalPackage;
import orgomg.cwm.resource.xml.XmlPackage;
import orgomg.cwmmip.CwmmipPackage;
import orgomg.cwmx.analysis.informationreporting.InformationreportingPackage;
import orgomg.cwmx.analysis.informationset.InformationsetPackage;
import orgomg.cwmx.foundation.er.ErPackage;
import orgomg.cwmx.resource.coboldata.CoboldataPackage;
import orgomg.cwmx.resource.dmsii.DmsiiPackage;
import orgomg.cwmx.resource.essbase.EssbasePackage;
import orgomg.cwmx.resource.express.ExpressPackage;
import orgomg.cwmx.resource.imsdatabase.ImsdatabasePackage;
import orgomg.mof.model.ModelPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!-- end-user-doc -->
 * @generated
 */
public class ConnectionPackageImpl extends EPackageImpl implements ConnectionPackage {

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass metadataEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass connectionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass metadataColumnEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractMetadataObjectEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass metadataTableEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass fileConnectionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass delimitedFileConnectionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass positionalFileConnectionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass ebcdicConnectionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass mdmConnectionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass databaseConnectionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass sapConnectionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass sapFunctionUnitEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass sapiDocUnitEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass sapFunctionParameterColumnEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass sapFunctionParameterTableEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass inputSAPFunctionParameterTableEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass outputSAPFunctionParameterTableEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass regexpFileConnectionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass xmlFileConnectionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass schemaTargetEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass queriesConnectionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass queryEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass ldifFileConnectionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass fileExcelConnectionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass xmlXPathLoopDescriptorEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass genericSchemaConnectionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass ldapSchemaConnectionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass wsdlSchemaConnectionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass salesforceSchemaConnectionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass cdcConnectionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass cdcTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass subscriberTableEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass sapTestInputParameterTableEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass conceptEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass conceptTargetEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass hl7ConnectionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass headerFooterConnectionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass xmlFileNodeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass wsdlParameterEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc --> ======= <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass genericPackageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc --> >>>>>>> .r45916
     * 
     * @generated
     */
    private EClass hl7FileNodeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass ftpConnectionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass brmsConnectionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass conditionTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass innerJoinMapEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass edifactConnectionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass salesforceModuleUnitEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass additionalPropertiesEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass sapTableFieldEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass sapFunctionParameterEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass sapFunctionParamDataEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass additionalConnectionPropertyEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass sapbwTableEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass sapbwTableFieldEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass sapTableEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass edifactColumnEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass validationRulesConnectionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EEnum fileFormatEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EEnum fieldSeparatorEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EEnum escapeEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EEnum rowSeparatorEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EEnum mdmConnectionProtocolEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EEnum mdmConceptTypeEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EEnum ruleTypeEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EEnum functionEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EEnum operatorEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EEnum logicalOperatorEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EDataType mapEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EDataType listEDataType = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with {@link org.eclipse.emf.ecore.EPackage.Registry
     * EPackage.Registry} by the package package URI value.
     * <p>
     * Note: the correct way to create the package is via the static factory method {@link #init init()}, which also
     * performs initialization of the package, or returns the registered package, if one already exists. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private ConnectionPackageImpl() {
        super(eNS_URI, ConnectionFactory.eINSTANCE);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
     * 
     * <p>This method is used to initialize {@link ConnectionPackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static ConnectionPackage init() {
        if (isInited)
            return (ConnectionPackage) EPackage.Registry.INSTANCE.getEPackage(ConnectionPackage.eNS_URI);

        // Obtain or create and register package
        ConnectionPackageImpl theConnectionPackage = (ConnectionPackageImpl) (EPackage.Registry.INSTANCE
                .get(eNS_URI) instanceof ConnectionPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI)
                        : new ConnectionPackageImpl());

        isInited = true;

        // Initialize simple dependencies
        CorePackage.eINSTANCE.eClass();
        BehavioralPackage.eINSTANCE.eClass();
        RelationshipsPackage.eINSTANCE.eClass();
        InstancePackage.eINSTANCE.eClass();
        BusinessinformationPackage.eINSTANCE.eClass();
        DatatypesPackage.eINSTANCE.eClass();
        ExpressionsPackage.eINSTANCE.eClass();
        KeysindexesPackage.eINSTANCE.eClass();
        SoftwaredeploymentPackage.eINSTANCE.eClass();
        TypemappingPackage.eINSTANCE.eClass();
        RelationalPackage.eINSTANCE.eClass();
        RecordPackage.eINSTANCE.eClass();
        MultidimensionalPackage.eINSTANCE.eClass();
        XmlPackage.eINSTANCE.eClass();
        TransformationPackage.eINSTANCE.eClass();
        OlapPackage.eINSTANCE.eClass();
        DataminingPackage.eINSTANCE.eClass();
        InformationvisualizationPackage.eINSTANCE.eClass();
        BusinessnomenclaturePackage.eINSTANCE.eClass();
        WarehouseprocessPackage.eINSTANCE.eClass();
        WarehouseoperationPackage.eINSTANCE.eClass();
        ErPackage.eINSTANCE.eClass();
        CoboldataPackage.eINSTANCE.eClass();
        DmsiiPackage.eINSTANCE.eClass();
        ImsdatabasePackage.eINSTANCE.eClass();
        EssbasePackage.eINSTANCE.eClass();
        ExpressPackage.eINSTANCE.eClass();
        InformationsetPackage.eINSTANCE.eClass();
        InformationreportingPackage.eINSTANCE.eClass();
        CwmmipPackage.eINSTANCE.eClass();
        ModelPackage.eINSTANCE.eClass();

        // Obtain or create and register interdependencies
        RelationalPackageImpl theRelationalPackage_1 = (RelationalPackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(org.talend.cwm.relational.RelationalPackage.eNS_URI) instanceof RelationalPackageImpl
                        ? EPackage.Registry.INSTANCE.getEPackage(org.talend.cwm.relational.RelationalPackage.eNS_URI)
                        : org.talend.cwm.relational.RelationalPackage.eINSTANCE);
        SoftwaredeploymentPackageImpl theSoftwaredeploymentPackage_1 = (SoftwaredeploymentPackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(
                        org.talend.cwm.softwaredeployment.SoftwaredeploymentPackage.eNS_URI) instanceof SoftwaredeploymentPackageImpl
                                ? EPackage.Registry.INSTANCE
                                        .getEPackage(org.talend.cwm.softwaredeployment.SoftwaredeploymentPackage.eNS_URI)
                                : org.talend.cwm.softwaredeployment.SoftwaredeploymentPackage.eINSTANCE);
        ConstantsPackageImpl theConstantsPackage = (ConstantsPackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(ConstantsPackage.eNS_URI) instanceof ConstantsPackageImpl
                        ? EPackage.Registry.INSTANCE.getEPackage(ConstantsPackage.eNS_URI) : ConstantsPackage.eINSTANCE);
        XmlPackageImpl theXmlPackage_1 = (XmlPackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(org.talend.cwm.xml.XmlPackage.eNS_URI) instanceof XmlPackageImpl
                        ? EPackage.Registry.INSTANCE.getEPackage(org.talend.cwm.xml.XmlPackage.eNS_URI)
                        : org.talend.cwm.xml.XmlPackage.eINSTANCE);

        // Create package meta-data objects
        theConnectionPackage.createPackageContents();
        theRelationalPackage_1.createPackageContents();
        theSoftwaredeploymentPackage_1.createPackageContents();
        theConstantsPackage.createPackageContents();
        theXmlPackage_1.createPackageContents();

        // Initialize created meta-data
        theConnectionPackage.initializePackageContents();
        theRelationalPackage_1.initializePackageContents();
        theSoftwaredeploymentPackage_1.initializePackageContents();
        theConstantsPackage.initializePackageContents();
        theXmlPackage_1.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theConnectionPackage.freeze();

        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(ConnectionPackage.eNS_URI, theConnectionPackage);
        return theConnectionPackage;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getMetadata() {
        return metadataEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getMetadata_Connections() {
        return (EReference) metadataEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getConnection() {
        return connectionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConnection_Version() {
        return (EAttribute) connectionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getConnection_Queries() {
        return (EReference) connectionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConnection_ContextMode() {
        return (EAttribute) connectionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConnection_ContextId() {
        return (EAttribute) connectionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConnection_ContextName() {
        return (EAttribute) connectionEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getMetadataColumn() {
        return metadataColumnEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetadataColumn_SourceType() {
        return (EAttribute) metadataColumnEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetadataColumn_DefaultValue() {
        return (EAttribute) metadataColumnEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetadataColumn_TalendType() {
        return (EAttribute) metadataColumnEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetadataColumn_Key() {
        return (EAttribute) metadataColumnEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetadataColumn_Nullable() {
        return (EAttribute) metadataColumnEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getMetadataColumn_Table() {
        return (EReference) metadataColumnEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetadataColumn_OriginalField() {
        return (EAttribute) metadataColumnEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetadataColumn_Pattern() {
        return (EAttribute) metadataColumnEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetadataColumn_DisplayField() {
        return (EAttribute) metadataColumnEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetadataColumn_OriginalLength() {
        return (EAttribute) metadataColumnEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetadataColumn_RelatedEntity() {
        return (EAttribute) metadataColumnEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetadataColumn_RelationshipType() {
        return (EAttribute) metadataColumnEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractMetadataObject() {
        return abstractMetadataObjectEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractMetadataObject_Properties() {
        return (EAttribute) abstractMetadataObjectEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractMetadataObject_Id() {
        return (EAttribute) abstractMetadataObjectEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractMetadataObject_Comment() {
        return (EAttribute) abstractMetadataObjectEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractMetadataObject_Label() {
        return (EAttribute) abstractMetadataObjectEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractMetadataObject_ReadOnly() {
        return (EAttribute) abstractMetadataObjectEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractMetadataObject_Synchronised() {
        return (EAttribute) abstractMetadataObjectEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractMetadataObject_Divergency() {
        return (EAttribute) abstractMetadataObjectEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getMetadataTable() {
        return metadataTableEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetadataTable_SourceName() {
        return (EAttribute) metadataTableEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetadataTable_TableType() {
        return (EAttribute) metadataTableEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetadataTable_AttachedCDC() {
        return (EAttribute) metadataTableEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetadataTable_ActivatedCDC() {
        return (EAttribute) metadataTableEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getMetadataTable_Columns() {
        return (EReference) metadataTableEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getMetadataTable_Connection() {
        return (EReference) metadataTableEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMetadataTable_AdditionalProperties() {
        return (EReference) metadataTableEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getFileConnection() {
        return fileConnectionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileConnection_Server() {
        return (EAttribute) fileConnectionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileConnection_FilePath() {
        return (EAttribute) fileConnectionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileConnection_Format() {
        return (EAttribute) fileConnectionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileConnection_Encoding() {
        return (EAttribute) fileConnectionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileConnection_FieldSeparatorValue() {
        return (EAttribute) fileConnectionEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileConnection_RowSeparatorType() {
        return (EAttribute) fileConnectionEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileConnection_RowSeparatorValue() {
        return (EAttribute) fileConnectionEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileConnection_TextIdentifier() {
        return (EAttribute) fileConnectionEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileConnection_UseHeader() {
        return (EAttribute) fileConnectionEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileConnection_HeaderValue() {
        return (EAttribute) fileConnectionEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileConnection_UseFooter() {
        return (EAttribute) fileConnectionEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileConnection_FooterValue() {
        return (EAttribute) fileConnectionEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileConnection_UseLimit() {
        return (EAttribute) fileConnectionEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileConnection_LimitValue() {
        return (EAttribute) fileConnectionEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileConnection_FirstLineCaption() {
        return (EAttribute) fileConnectionEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileConnection_RemoveEmptyRow() {
        return (EAttribute) fileConnectionEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileConnection_EscapeType() {
        return (EAttribute) fileConnectionEClass.getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileConnection_EscapeChar() {
        return (EAttribute) fileConnectionEClass.getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileConnection_TextEnclosure() {
        return (EAttribute) fileConnectionEClass.getEStructuralFeatures().get(18);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileConnection_CsvOption() {
        return (EAttribute) fileConnectionEClass.getEStructuralFeatures().get(19);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getDelimitedFileConnection() {
        return delimitedFileConnectionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDelimitedFileConnection_FieldSeparatorType() {
        return (EAttribute) delimitedFileConnectionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDelimitedFileConnection_SplitRecord() {
        return (EAttribute) delimitedFileConnectionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getPositionalFileConnection() {
        return positionalFileConnectionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getEbcdicConnection() {
        return ebcdicConnectionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEbcdicConnection_MidFile() {
        return (EAttribute) ebcdicConnectionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEbcdicConnection_DataFile() {
        return (EAttribute) ebcdicConnectionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEbcdicConnection_CodePage() {
        return (EAttribute) ebcdicConnectionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEbcdicConnection_SourceFileStart() {
        return (EAttribute) ebcdicConnectionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEbcdicConnection_SourceFileEnd() {
        return (EAttribute) ebcdicConnectionEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getMDMConnection() {
        return mdmConnectionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMDMConnection_Username() {
        return (EAttribute) mdmConnectionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMDMConnection_Password() {
        return (EAttribute) mdmConnectionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMDMConnection_Port() {
        return (EAttribute) mdmConnectionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMDMConnection_Server() {
        return (EAttribute) mdmConnectionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMDMConnection_Universe() {
        return (EAttribute) mdmConnectionEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMDMConnection_Datamodel() {
        return (EAttribute) mdmConnectionEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMDMConnection_Datacluster() {
        return (EAttribute) mdmConnectionEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getMDMConnection_Schemas() {
        return (EReference) mdmConnectionEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMDMConnection_Protocol() {
        return (EAttribute) mdmConnectionEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMDMConnection_Context() {
        return (EAttribute) mdmConnectionEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMDMConnection_ServerUrl() {
        return (EAttribute) mdmConnectionEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getDatabaseConnection() {
        return databaseConnectionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatabaseConnection_DatabaseType() {
        return (EAttribute) databaseConnectionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatabaseConnection_DriverJarPath() {
        return (EAttribute) databaseConnectionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatabaseConnection_DriverClass() {
        return (EAttribute) databaseConnectionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatabaseConnection_URL() {
        return (EAttribute) databaseConnectionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatabaseConnection_DbVersionString() {
        return (EAttribute) databaseConnectionEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatabaseConnection_Port() {
        return (EAttribute) databaseConnectionEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatabaseConnection_Username() {
        return (EAttribute) databaseConnectionEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatabaseConnection_Password() {
        return (EAttribute) databaseConnectionEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatabaseConnection_ServerName() {
        return (EAttribute) databaseConnectionEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatabaseConnection_DatasourceName() {
        return (EAttribute) databaseConnectionEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatabaseConnection_FileFieldName() {
        return (EAttribute) databaseConnectionEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatabaseConnection_SID() {
        return (EAttribute) databaseConnectionEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatabaseConnection_SqlSynthax() {
        return (EAttribute) databaseConnectionEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatabaseConnection_StringQuote() {
        return (EAttribute) databaseConnectionEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatabaseConnection_NullChar() {
        return (EAttribute) databaseConnectionEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatabaseConnection_DbmsId() {
        return (EAttribute) databaseConnectionEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatabaseConnection_ProductId() {
        return (EAttribute) databaseConnectionEClass.getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatabaseConnection_DBRootPath() {
        return (EAttribute) databaseConnectionEClass.getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatabaseConnection_AdditionalParams() {
        return (EAttribute) databaseConnectionEClass.getEStructuralFeatures().get(18);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatabaseConnection_StandardSQL() {
        return (EAttribute) databaseConnectionEClass.getEStructuralFeatures().get(19);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatabaseConnection_SystemSQL() {
        return (EAttribute) databaseConnectionEClass.getEStructuralFeatures().get(20);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getDatabaseConnection_CdcConns() {
        return (EReference) databaseConnectionEClass.getEStructuralFeatures().get(21);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatabaseConnection_CdcTypeMode() {
        return (EAttribute) databaseConnectionEClass.getEStructuralFeatures().get(22);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatabaseConnection_SQLMode() {
        return (EAttribute) databaseConnectionEClass.getEStructuralFeatures().get(23);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatabaseConnection_UiSchema() {
        return (EAttribute) databaseConnectionEClass.getEStructuralFeatures().get(24);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDatabaseConnection_Parameters() {
        return (EReference) databaseConnectionEClass.getEStructuralFeatures().get(25);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getSAPConnection() {
        return sapConnectionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPConnection_Host() {
        return (EAttribute) sapConnectionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPConnection_Username() {
        return (EAttribute) sapConnectionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPConnection_Password() {
        return (EAttribute) sapConnectionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPConnection_Client() {
        return (EAttribute) sapConnectionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPConnection_SystemNumber() {
        return (EAttribute) sapConnectionEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPConnection_Language() {
        return (EAttribute) sapConnectionEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getSAPConnection_Funtions() {
        return (EReference) sapConnectionEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPConnection_CurrentFucntion() {
        return (EAttribute) sapConnectionEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getSAPConnection_IDocs() {
        return (EReference) sapConnectionEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPConnection_JcoVersion() {
        return (EAttribute) sapConnectionEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSAPConnection_AdditionalProperties() {
        return (EReference) sapConnectionEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSAPConnection_BWDataSources() {
        return (EReference) sapConnectionEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSAPConnection_BWDataStoreObjects() {
        return (EReference) sapConnectionEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSAPConnection_BWInfoCubes() {
        return (EReference) sapConnectionEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSAPConnection_BWInfoObjects() {
        return (EReference) sapConnectionEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getSAPFunctionUnit() {
        return sapFunctionUnitEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPFunctionUnit_OutputType() {
        return (EAttribute) sapFunctionUnitEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPFunctionUnit_OutputTableName() {
        return (EAttribute) sapFunctionUnitEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getSAPFunctionUnit_InputParameterTable() {
        return (EReference) sapFunctionUnitEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getSAPFunctionUnit_OutputParameterTable() {
        return (EReference) sapFunctionUnitEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getSAPFunctionUnit_MetadataTable() {
        return (EReference) sapFunctionUnitEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getSAPFunctionUnit_Connection() {
        return (EReference) sapFunctionUnitEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getSAPFunctionUnit_Tables() {
        return (EReference) sapFunctionUnitEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSAPFunctionUnit_InputTables() {
        return (EReference) sapFunctionUnitEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getSAPFunctionUnit_TestInputParameterTable() {
        return (EReference) sapFunctionUnitEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSAPFunctionUnit_ParamData() {
        return (EReference) sapFunctionUnitEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPFunctionUnit_AsXmlSchema() {
        return (EAttribute) sapFunctionUnitEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getSAPIDocUnit() {
        return sapiDocUnitEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getSAPIDocUnit_Connection() {
        return (EReference) sapiDocUnitEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPIDocUnit_ProgramId() {
        return (EAttribute) sapiDocUnitEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPIDocUnit_GatewayService() {
        return (EAttribute) sapiDocUnitEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPIDocUnit_UseXmlOutput() {
        return (EAttribute) sapiDocUnitEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPIDocUnit_XmlFile() {
        return (EAttribute) sapiDocUnitEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPIDocUnit_UseHtmlOutput() {
        return (EAttribute) sapiDocUnitEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPIDocUnit_HtmlFile() {
        return (EAttribute) sapiDocUnitEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getSAPFunctionParameterColumn() {
        return sapFunctionParameterColumnEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPFunctionParameterColumn_ParameterType() {
        return (EAttribute) sapFunctionParameterColumnEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPFunctionParameterColumn_StructureOrTableName() {
        return (EAttribute) sapFunctionParameterColumnEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPFunctionParameterColumn_DataType() {
        return (EAttribute) sapFunctionParameterColumnEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPFunctionParameterColumn_Length() {
        return (EAttribute) sapFunctionParameterColumnEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPFunctionParameterColumn_Value() {
        return (EAttribute) sapFunctionParameterColumnEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getSAPFunctionParameterColumn_ParameterTable() {
        return (EReference) sapFunctionParameterColumnEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getSAPFunctionParameterTable() {
        return sapFunctionParameterTableEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getSAPFunctionParameterTable_Columns() {
        return (EReference) sapFunctionParameterTableEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getInputSAPFunctionParameterTable() {
        return inputSAPFunctionParameterTableEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getInputSAPFunctionParameterTable_FunctionUnit() {
        return (EReference) inputSAPFunctionParameterTableEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getOutputSAPFunctionParameterTable() {
        return outputSAPFunctionParameterTableEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getOutputSAPFunctionParameterTable_FunctionUnit() {
        return (EReference) outputSAPFunctionParameterTableEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getRegexpFileConnection() {
        return regexpFileConnectionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRegexpFileConnection_FieldSeparatorType() {
        return (EAttribute) regexpFileConnectionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getXmlFileConnection() {
        return xmlFileConnectionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getXmlFileConnection_XsdFilePath() {
        return (EAttribute) xmlFileConnectionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getXmlFileConnection_XmlFilePath() {
        return (EAttribute) xmlFileConnectionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getXmlFileConnection_Guess() {
        return (EAttribute) xmlFileConnectionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getXmlFileConnection_MaskXPattern() {
        return (EAttribute) xmlFileConnectionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getXmlFileConnection_Schema() {
        return (EReference) xmlFileConnectionEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getXmlFileConnection_Encoding() {
        return (EAttribute) xmlFileConnectionEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getXmlFileConnection_Group() {
        return (EReference) xmlFileConnectionEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getXmlFileConnection_Root() {
        return (EReference) xmlFileConnectionEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getXmlFileConnection_Loop() {
        return (EReference) xmlFileConnectionEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getXmlFileConnection_InputModel() {
        return (EAttribute) xmlFileConnectionEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getXmlFileConnection_OutputFilePath() {
        return (EAttribute) xmlFileConnectionEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getXmlFileConnection_FileContent() {
        return (EAttribute) xmlFileConnectionEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getXmlFileConnection_TargetNameSpace() {
        return (EAttribute) xmlFileConnectionEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getSchemaTarget() {
        return schemaTargetEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSchemaTarget_RelativeXPathQuery() {
        return (EAttribute) schemaTargetEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSchemaTarget_TagName() {
        return (EAttribute) schemaTargetEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getSchemaTarget_Schema() {
        return (EReference) schemaTargetEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getQueriesConnection() {
        return queriesConnectionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getQueriesConnection_Connection() {
        return (EReference) queriesConnectionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getQueriesConnection_Query() {
        return (EReference) queriesConnectionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getQuery() {
        return queryEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getQuery_Value() {
        return (EAttribute) queryEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getQuery_Queries() {
        return (EReference) queryEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getQuery_ContextMode() {
        return (EAttribute) queryEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getLdifFileConnection() {
        return ldifFileConnectionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLdifFileConnection_Value() {
        return (EAttribute) ldifFileConnectionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLdifFileConnection_FilePath() {
        return (EAttribute) ldifFileConnectionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLdifFileConnection_LimitEntry() {
        return (EAttribute) ldifFileConnectionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLdifFileConnection_UseLimit() {
        return (EAttribute) ldifFileConnectionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLdifFileConnection_Server() {
        return (EAttribute) ldifFileConnectionEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getFileExcelConnection() {
        return fileExcelConnectionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileExcelConnection_SheetName() {
        return (EAttribute) fileExcelConnectionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileExcelConnection_SheetColumns() {
        return (EAttribute) fileExcelConnectionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileExcelConnection_FirstColumn() {
        return (EAttribute) fileExcelConnectionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileExcelConnection_LastColumn() {
        return (EAttribute) fileExcelConnectionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileExcelConnection_ThousandSeparator() {
        return (EAttribute) fileExcelConnectionEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileExcelConnection_DecimalSeparator() {
        return (EAttribute) fileExcelConnectionEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileExcelConnection_AdvancedSpearator() {
        return (EAttribute) fileExcelConnectionEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileExcelConnection_SelectAllSheets() {
        return (EAttribute) fileExcelConnectionEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileExcelConnection_SheetList() {
        return (EAttribute) fileExcelConnectionEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileExcelConnection_GenerationMode() {
        return (EAttribute) fileExcelConnectionEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getXmlXPathLoopDescriptor() {
        return xmlXPathLoopDescriptorEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getXmlXPathLoopDescriptor_LimitBoucle() {
        return (EAttribute) xmlXPathLoopDescriptorEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getXmlXPathLoopDescriptor_AbsoluteXPathQuery() {
        return (EAttribute) xmlXPathLoopDescriptorEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getXmlXPathLoopDescriptor_Connection() {
        return (EReference) xmlXPathLoopDescriptorEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getXmlXPathLoopDescriptor_SchemaTargets() {
        return (EReference) xmlXPathLoopDescriptorEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getGenericSchemaConnection() {
        return genericSchemaConnectionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGenericSchemaConnection_MappingTypeUsed() {
        return (EAttribute) genericSchemaConnectionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGenericSchemaConnection_MappingTypeId() {
        return (EAttribute) genericSchemaConnectionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getLDAPSchemaConnection() {
        return ldapSchemaConnectionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLDAPSchemaConnection_Host() {
        return (EAttribute) ldapSchemaConnectionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLDAPSchemaConnection_Port() {
        return (EAttribute) ldapSchemaConnectionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLDAPSchemaConnection_Protocol() {
        return (EAttribute) ldapSchemaConnectionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLDAPSchemaConnection_Filter() {
        return (EAttribute) ldapSchemaConnectionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLDAPSchemaConnection_Separator() {
        return (EAttribute) ldapSchemaConnectionEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLDAPSchemaConnection_UseAdvanced() {
        return (EAttribute) ldapSchemaConnectionEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLDAPSchemaConnection_StorePath() {
        return (EAttribute) ldapSchemaConnectionEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLDAPSchemaConnection_UseLimit() {
        return (EAttribute) ldapSchemaConnectionEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLDAPSchemaConnection_UseAuthen() {
        return (EAttribute) ldapSchemaConnectionEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLDAPSchemaConnection_BindPrincipal() {
        return (EAttribute) ldapSchemaConnectionEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLDAPSchemaConnection_BindPassword() {
        return (EAttribute) ldapSchemaConnectionEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLDAPSchemaConnection_LimitValue() {
        return (EAttribute) ldapSchemaConnectionEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLDAPSchemaConnection_EncryptionMethodName() {
        return (EAttribute) ldapSchemaConnectionEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLDAPSchemaConnection_Value() {
        return (EAttribute) ldapSchemaConnectionEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLDAPSchemaConnection_SavePassword() {
        return (EAttribute) ldapSchemaConnectionEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLDAPSchemaConnection_Aliases() {
        return (EAttribute) ldapSchemaConnectionEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLDAPSchemaConnection_Referrals() {
        return (EAttribute) ldapSchemaConnectionEClass.getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLDAPSchemaConnection_CountLimit() {
        return (EAttribute) ldapSchemaConnectionEClass.getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLDAPSchemaConnection_TimeOutLimit() {
        return (EAttribute) ldapSchemaConnectionEClass.getEStructuralFeatures().get(18);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLDAPSchemaConnection_BaseDNs() {
        return (EAttribute) ldapSchemaConnectionEClass.getEStructuralFeatures().get(19);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLDAPSchemaConnection_GetBaseDNsFromRoot() {
        return (EAttribute) ldapSchemaConnectionEClass.getEStructuralFeatures().get(20);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLDAPSchemaConnection_ReturnAttributes() {
        return (EAttribute) ldapSchemaConnectionEClass.getEStructuralFeatures().get(21);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLDAPSchemaConnection_SelectedDN() {
        return (EAttribute) ldapSchemaConnectionEClass.getEStructuralFeatures().get(22);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getWSDLSchemaConnection() {
        return wsdlSchemaConnectionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLSchemaConnection_WSDL() {
        return (EAttribute) wsdlSchemaConnectionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLSchemaConnection_NeedAuth() {
        return (EAttribute) wsdlSchemaConnectionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLSchemaConnection_MethodName() {
        return (EAttribute) wsdlSchemaConnectionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLSchemaConnection_Parameters() {
        return (EAttribute) wsdlSchemaConnectionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLSchemaConnection_UserName() {
        return (EAttribute) wsdlSchemaConnectionEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLSchemaConnection_Password() {
        return (EAttribute) wsdlSchemaConnectionEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLSchemaConnection_UseProxy() {
        return (EAttribute) wsdlSchemaConnectionEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLSchemaConnection_ProxyHost() {
        return (EAttribute) wsdlSchemaConnectionEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLSchemaConnection_ProxyPort() {
        return (EAttribute) wsdlSchemaConnectionEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLSchemaConnection_ProxyUser() {
        return (EAttribute) wsdlSchemaConnectionEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLSchemaConnection_ProxyPassword() {
        return (EAttribute) wsdlSchemaConnectionEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLSchemaConnection_Value() {
        return (EAttribute) wsdlSchemaConnectionEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLSchemaConnection_EndpointURI() {
        return (EAttribute) wsdlSchemaConnectionEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLSchemaConnection_Encoding() {
        return (EAttribute) wsdlSchemaConnectionEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLSchemaConnection_TimeOut() {
        return (EAttribute) wsdlSchemaConnectionEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLSchemaConnection_IsInputModel() {
        return (EAttribute) wsdlSchemaConnectionEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLSchemaConnection_ServerNameSpace() {
        return (EAttribute) wsdlSchemaConnectionEClass.getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLSchemaConnection_ServerName() {
        return (EAttribute) wsdlSchemaConnectionEClass.getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLSchemaConnection_PortNameSpace() {
        return (EAttribute) wsdlSchemaConnectionEClass.getEStructuralFeatures().get(18);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLSchemaConnection_PortName() {
        return (EAttribute) wsdlSchemaConnectionEClass.getEStructuralFeatures().get(19);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getWSDLSchemaConnection_ParameterValue() {
        return (EReference) wsdlSchemaConnectionEClass.getEStructuralFeatures().get(20);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getWSDLSchemaConnection_OutputParameter() {
        return (EReference) wsdlSchemaConnectionEClass.getEStructuralFeatures().get(21);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getSalesforceSchemaConnection() {
        return salesforceSchemaConnectionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSalesforceSchemaConnection_WebServiceUrl() {
        return (EAttribute) salesforceSchemaConnectionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSalesforceSchemaConnection_UserName() {
        return (EAttribute) salesforceSchemaConnectionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSalesforceSchemaConnection_Password() {
        return (EAttribute) salesforceSchemaConnectionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSalesforceSchemaConnection_ModuleName() {
        return (EAttribute) salesforceSchemaConnectionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSalesforceSchemaConnection_QueryCondition() {
        return (EAttribute) salesforceSchemaConnectionEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSalesforceSchemaConnection_UseCustomModuleName() {
        return (EAttribute) salesforceSchemaConnectionEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSalesforceSchemaConnection_UseProxy() {
        return (EAttribute) salesforceSchemaConnectionEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSalesforceSchemaConnection_ProxyHost() {
        return (EAttribute) salesforceSchemaConnectionEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSalesforceSchemaConnection_ProxyPort() {
        return (EAttribute) salesforceSchemaConnectionEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSalesforceSchemaConnection_ProxyUsername() {
        return (EAttribute) salesforceSchemaConnectionEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSalesforceSchemaConnection_ProxyPassword() {
        return (EAttribute) salesforceSchemaConnectionEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSalesforceSchemaConnection_BatchSize() {
        return (EAttribute) salesforceSchemaConnectionEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSalesforceSchemaConnection_UseHttpProxy() {
        return (EAttribute) salesforceSchemaConnectionEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSalesforceSchemaConnection_UseAlphbet() {
        return (EAttribute) salesforceSchemaConnectionEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSalesforceSchemaConnection_TimeOut() {
        return (EAttribute) salesforceSchemaConnectionEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getSalesforceSchemaConnection_Modules() {
        return (EReference) salesforceSchemaConnectionEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSalesforceSchemaConnection_WebServiceUrlTextForOAuth() {
        return (EAttribute) salesforceSchemaConnectionEClass.getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSalesforceSchemaConnection_ConsumeKey() {
        return (EAttribute) salesforceSchemaConnectionEClass.getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSalesforceSchemaConnection_ConsumeSecret() {
        return (EAttribute) salesforceSchemaConnectionEClass.getEStructuralFeatures().get(18);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSalesforceSchemaConnection_CallbackHost() {
        return (EAttribute) salesforceSchemaConnectionEClass.getEStructuralFeatures().get(19);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSalesforceSchemaConnection_CallbackPort() {
        return (EAttribute) salesforceSchemaConnectionEClass.getEStructuralFeatures().get(20);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSalesforceSchemaConnection_SalesforceVersion() {
        return (EAttribute) salesforceSchemaConnectionEClass.getEStructuralFeatures().get(21);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSalesforceSchemaConnection_Token() {
        return (EAttribute) salesforceSchemaConnectionEClass.getEStructuralFeatures().get(22);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSalesforceSchemaConnection_LoginType() {
        return (EAttribute) salesforceSchemaConnectionEClass.getEStructuralFeatures().get(23);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getCDCConnection() {
        return cdcConnectionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getCDCConnection_Connection() {
        return (EReference) cdcConnectionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getCDCConnection_CdcTypes() {
        return (EReference) cdcConnectionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getCDCType() {
        return cdcTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCDCType_LinkDB() {
        return (EAttribute) cdcTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getCDCType_Subscribers() {
        return (EReference) cdcTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getCDCType_CdcConnection() {
        return (EReference) cdcTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCDCType_JournalName() {
        return (EAttribute) cdcTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getSubscriberTable() {
        return subscriberTableEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSubscriberTable_System() {
        return (EAttribute) subscriberTableEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getSAPTestInputParameterTable() {
        return sapTestInputParameterTableEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getSAPTestInputParameterTable_FunctionUnit() {
        return (EReference) sapTestInputParameterTableEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getConcept() {
        return conceptEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConcept_LoopExpression() {
        return (EAttribute) conceptEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConcept_LoopLimit() {
        return (EAttribute) conceptEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getConcept_ConceptTargets() {
        return (EReference) conceptEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConcept_InputModel() {
        return (EAttribute) conceptEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getConcept_Group() {
        return (EReference) conceptEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getConcept_Root() {
        return (EReference) conceptEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getConcept_Loop() {
        return (EReference) conceptEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConcept_ConceptType() {
        return (EAttribute) conceptEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConcept_XPathPrefix() {
        return (EAttribute) conceptEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getConceptTarget() {
        return conceptTargetEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getConceptTarget_Schema() {
        return (EReference) conceptTargetEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConceptTarget_TargetName() {
        return (EAttribute) conceptTargetEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConceptTarget_RelativeLoopExpression() {
        return (EAttribute) conceptTargetEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getHL7Connection() {
        return hl7ConnectionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHL7Connection_StartChar() {
        return (EAttribute) hl7ConnectionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHL7Connection_EndChar() {
        return (EAttribute) hl7ConnectionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getHL7Connection_Root() {
        return (EReference) hl7ConnectionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHL7Connection_OutputFilePath() {
        return (EAttribute) hl7ConnectionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getHeaderFooterConnection() {
        return headerFooterConnectionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHeaderFooterConnection_IsHeader() {
        return (EAttribute) headerFooterConnectionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHeaderFooterConnection_Imports() {
        return (EAttribute) headerFooterConnectionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHeaderFooterConnection_MainCode() {
        return (EAttribute) headerFooterConnectionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHeaderFooterConnection_Libraries() {
        return (EAttribute) headerFooterConnectionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getXMLFileNode() {
        return xmlFileNodeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getXMLFileNode_XMLPath() {
        return (EAttribute) xmlFileNodeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getXMLFileNode_RelatedColumn() {
        return (EAttribute) xmlFileNodeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getXMLFileNode_DefaultValue() {
        return (EAttribute) xmlFileNodeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getXMLFileNode_Attribute() {
        return (EAttribute) xmlFileNodeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getXMLFileNode_Order() {
        return (EAttribute) xmlFileNodeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getXMLFileNode_Type() {
        return (EAttribute) xmlFileNodeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getGenericPackage() {
        return genericPackageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getHL7FileNode() {
        return hl7FileNodeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHL7FileNode_FilePath() {
        return (EAttribute) hl7FileNodeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHL7FileNode_Order() {
        return (EAttribute) hl7FileNodeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHL7FileNode_Attribute() {
        return (EAttribute) hl7FileNodeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHL7FileNode_DefaultValue() {
        return (EAttribute) hl7FileNodeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHL7FileNode_RelatedColumn() {
        return (EAttribute) hl7FileNodeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getWSDLParameter() {
        return wsdlParameterEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLParameter_Element() {
        return (EAttribute) wsdlParameterEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLParameter_Source() {
        return (EAttribute) wsdlParameterEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLParameter_Column() {
        return (EAttribute) wsdlParameterEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLParameter_Expression() {
        return (EAttribute) wsdlParameterEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLParameter_ParameterInfo() {
        return (EAttribute) wsdlParameterEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLParameter_ParameterInfoParent() {
        return (EAttribute) wsdlParameterEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc --> ======= <!-- begin-user-doc --> <!-- end-user-doc --> >>>>>>>
     * .r45916
     * 
     * @generated
     */
    public EAttribute getHL7FileNode_Repeatable() {
        return (EAttribute) hl7FileNodeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getFTPConnection() {
        return ftpConnectionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFTPConnection_Host() {
        return (EAttribute) ftpConnectionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFTPConnection_Port() {
        return (EAttribute) ftpConnectionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFTPConnection_Username() {
        return (EAttribute) ftpConnectionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFTPConnection_Password() {
        return (EAttribute) ftpConnectionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFTPConnection_Mode() {
        return (EAttribute) ftpConnectionEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFTPConnection_Ecoding() {
        return (EAttribute) ftpConnectionEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFTPConnection_SFTP() {
        return (EAttribute) ftpConnectionEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFTPConnection_FTPS() {
        return (EAttribute) ftpConnectionEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFTPConnection_Method() {
        return (EAttribute) ftpConnectionEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFTPConnection_Privatekey() {
        return (EAttribute) ftpConnectionEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFTPConnection_Passphrase() {
        return (EAttribute) ftpConnectionEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFTPConnection_KeystoreFile() {
        return (EAttribute) ftpConnectionEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFTPConnection_KeystorePassword() {
        return (EAttribute) ftpConnectionEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFTPConnection_Usesocks() {
        return (EAttribute) ftpConnectionEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFTPConnection_Proxyhost() {
        return (EAttribute) ftpConnectionEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFTPConnection_Proxyport() {
        return (EAttribute) ftpConnectionEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFTPConnection_Proxyuser() {
        return (EAttribute) ftpConnectionEClass.getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFTPConnection_Proxypassword() {
        return (EAttribute) ftpConnectionEClass.getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFTPConnection_CustomEncode() {
        return (EAttribute) ftpConnectionEClass.getEStructuralFeatures().get(18);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFTPConnection_UseFileNameEncoding() {
        return (EAttribute) ftpConnectionEClass.getEStructuralFeatures().get(19);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getBRMSConnection() {
        return brmsConnectionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBRMSConnection_XmlField() {
        return (EAttribute) brmsConnectionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBRMSConnection_UrlName() {
        return (EAttribute) brmsConnectionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBRMSConnection_TacWebappName() {
        return (EAttribute) brmsConnectionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBRMSConnection_ClassName() {
        return (EAttribute) brmsConnectionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBRMSConnection_ModuleUsed() {
        return (EAttribute) brmsConnectionEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getBRMSConnection_Root() {
        return (EReference) brmsConnectionEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getBRMSConnection_Group() {
        return (EReference) brmsConnectionEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getBRMSConnection_Loop() {
        return (EReference) brmsConnectionEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBRMSConnection_Package() {
        return (EAttribute) brmsConnectionEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getConditionType() {
        return conditionTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConditionType_InputColumn() {
        return (EAttribute) conditionTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConditionType_Function() {
        return (EAttribute) conditionTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConditionType_Operator() {
        return (EAttribute) conditionTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConditionType_Value() {
        return (EAttribute) conditionTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getInnerJoinMap() {
        return innerJoinMapEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getInnerJoinMap_Key() {
        return (EAttribute) innerJoinMapEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getInnerJoinMap_Value() {
        return (EAttribute) innerJoinMapEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getEDIFACTConnection() {
        return edifactConnectionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEDIFACTConnection_XmlName() {
        return (EAttribute) edifactConnectionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEDIFACTConnection_FileName() {
        return (EAttribute) edifactConnectionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEDIFACTConnection_XmlPath() {
        return (EAttribute) edifactConnectionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getEDIFACTColumn() {
        return edifactColumnEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEDIFACTColumn_EDIColumnName() {
        return (EAttribute) edifactColumnEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEDIFACTColumn_EDIXpath() {
        return (EAttribute) edifactColumnEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getSalesforceModuleUnit() {
        return salesforceModuleUnitEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getSalesforceModuleUnit_MetadataTable() {
        return (EReference) salesforceModuleUnitEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getSalesforceModuleUnit_Connection() {
        return (EReference) salesforceModuleUnitEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getSalesforceModuleUnit_Tables() {
        return (EReference) salesforceModuleUnitEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSalesforceModuleUnit_ModuleName() {
        return (EAttribute) salesforceModuleUnitEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAdditionalProperties() {
        return additionalPropertiesEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAdditionalProperties_Key() {
        return (EAttribute) additionalPropertiesEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAdditionalProperties_Value() {
        return (EAttribute) additionalPropertiesEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSAPTableField() {
        return sapTableFieldEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPTableField_BusinessName() {
        return (EAttribute) sapTableFieldEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPTableField_RefTable() {
        return (EAttribute) sapTableFieldEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSAPFunctionParameter() {
        return sapFunctionParameterEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPFunctionParameter_Name() {
        return (EAttribute) sapFunctionParameterEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPFunctionParameter_Type() {
        return (EAttribute) sapFunctionParameterEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPFunctionParameter_Description() {
        return (EAttribute) sapFunctionParameterEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPFunctionParameter_Length() {
        return (EAttribute) sapFunctionParameterEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPFunctionParameter_Changing() {
        return (EAttribute) sapFunctionParameterEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPFunctionParameter_TestValue() {
        return (EAttribute) sapFunctionParameterEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSAPFunctionParameter_Children() {
        return (EReference) sapFunctionParameterEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPFunctionParameter_TableResideInTables() {
        return (EAttribute) sapFunctionParameterEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSAPFunctionParamData() {
        return sapFunctionParamDataEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSAPFunctionParamData_InputRoot() {
        return (EReference) sapFunctionParamDataEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSAPFunctionParamData_OutputRoot() {
        return (EReference) sapFunctionParamDataEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAdditionalConnectionProperty() {
        return additionalConnectionPropertyEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAdditionalConnectionProperty_PropertyName() {
        return (EAttribute) additionalConnectionPropertyEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAdditionalConnectionProperty_Value() {
        return (EAttribute) additionalConnectionPropertyEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSAPBWTable() {
        return sapbwTableEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPBWTable_ModelType() {
        return (EAttribute) sapbwTableEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPBWTable_Active() {
        return (EAttribute) sapbwTableEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPBWTable_SourceSystemName() {
        return (EAttribute) sapbwTableEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPBWTable_InfoAreaName() {
        return (EAttribute) sapbwTableEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPBWTable_InnerIOType() {
        return (EAttribute) sapbwTableEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSAPBWTableField() {
        return sapbwTableFieldEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPBWTableField_LogicalName() {
        return (EAttribute) sapbwTableFieldEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSAPTable() {
        return sapTableEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSAPTable_TableSearchType() {
        return (EAttribute) sapTableEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getValidationRulesConnection() {
        return validationRulesConnectionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValidationRulesConnection_IsSelect() {
        return (EAttribute) validationRulesConnectionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValidationRulesConnection_IsInsert() {
        return (EAttribute) validationRulesConnectionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValidationRulesConnection_IsUpdate() {
        return (EAttribute) validationRulesConnectionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValidationRulesConnection_IsDelete() {
        return (EAttribute) validationRulesConnectionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValidationRulesConnection_Type() {
        return (EAttribute) validationRulesConnectionEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValidationRulesConnection_BaseSchema() {
        return (EAttribute) validationRulesConnectionEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValidationRulesConnection_BaseColumnNames() {
        return (EAttribute) validationRulesConnectionEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValidationRulesConnection_RefSchema() {
        return (EAttribute) validationRulesConnectionEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValidationRulesConnection_RefColumnNames() {
        return (EAttribute) validationRulesConnectionEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValidationRulesConnection_JavaCondition() {
        return (EAttribute) validationRulesConnectionEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValidationRulesConnection_SqlCondition() {
        return (EAttribute) validationRulesConnectionEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValidationRulesConnection_LogicalOperator() {
        return (EAttribute) validationRulesConnectionEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getValidationRulesConnection_Conditions() {
        return (EReference) validationRulesConnectionEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getValidationRulesConnection_InnerJoins() {
        return (EReference) validationRulesConnectionEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValidationRulesConnection_IsDisallow() {
        return (EAttribute) validationRulesConnectionEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValidationRulesConnection_IsRejectLink() {
        return (EAttribute) validationRulesConnectionEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EEnum getFileFormat() {
        return fileFormatEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EEnum getFieldSeparator() {
        return fieldSeparatorEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EEnum getEscape() {
        return escapeEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EEnum getRowSeparator() {
        return rowSeparatorEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EEnum getMDMConnectionProtocol() {
        return mdmConnectionProtocolEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EEnum getMdmConceptType() {
        return mdmConceptTypeEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EEnum getRuleType() {
        return ruleTypeEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EEnum getFunction() {
        return functionEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EEnum getOperator() {
        return operatorEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EEnum getLogicalOperator() {
        return logicalOperatorEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EDataType getMap() {
        return mapEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EDataType getList() {
        return listEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ConnectionFactory getConnectionFactory() {
        return (ConnectionFactory) getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package.  This method is
     * guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void createPackageContents() {
        if (isCreated)
            return;
        isCreated = true;

        // Create classes and their features
        metadataEClass = createEClass(METADATA);
        createEReference(metadataEClass, METADATA__CONNECTIONS);

        connectionEClass = createEClass(CONNECTION);
        createEAttribute(connectionEClass, CONNECTION__VERSION);
        createEReference(connectionEClass, CONNECTION__QUERIES);
        createEAttribute(connectionEClass, CONNECTION__CONTEXT_MODE);
        createEAttribute(connectionEClass, CONNECTION__CONTEXT_ID);
        createEAttribute(connectionEClass, CONNECTION__CONTEXT_NAME);

        metadataColumnEClass = createEClass(METADATA_COLUMN);
        createEAttribute(metadataColumnEClass, METADATA_COLUMN__SOURCE_TYPE);
        createEAttribute(metadataColumnEClass, METADATA_COLUMN__DEFAULT_VALUE);
        createEAttribute(metadataColumnEClass, METADATA_COLUMN__TALEND_TYPE);
        createEAttribute(metadataColumnEClass, METADATA_COLUMN__KEY);
        createEAttribute(metadataColumnEClass, METADATA_COLUMN__NULLABLE);
        createEReference(metadataColumnEClass, METADATA_COLUMN__TABLE);
        createEAttribute(metadataColumnEClass, METADATA_COLUMN__ORIGINAL_FIELD);
        createEAttribute(metadataColumnEClass, METADATA_COLUMN__PATTERN);
        createEAttribute(metadataColumnEClass, METADATA_COLUMN__DISPLAY_FIELD);
        createEAttribute(metadataColumnEClass, METADATA_COLUMN__ORIGINAL_LENGTH);
        createEAttribute(metadataColumnEClass, METADATA_COLUMN__RELATED_ENTITY);
        createEAttribute(metadataColumnEClass, METADATA_COLUMN__RELATIONSHIP_TYPE);

        abstractMetadataObjectEClass = createEClass(ABSTRACT_METADATA_OBJECT);
        createEAttribute(abstractMetadataObjectEClass, ABSTRACT_METADATA_OBJECT__PROPERTIES);
        createEAttribute(abstractMetadataObjectEClass, ABSTRACT_METADATA_OBJECT__ID);
        createEAttribute(abstractMetadataObjectEClass, ABSTRACT_METADATA_OBJECT__COMMENT);
        createEAttribute(abstractMetadataObjectEClass, ABSTRACT_METADATA_OBJECT__LABEL);
        createEAttribute(abstractMetadataObjectEClass, ABSTRACT_METADATA_OBJECT__READ_ONLY);
        createEAttribute(abstractMetadataObjectEClass, ABSTRACT_METADATA_OBJECT__SYNCHRONISED);
        createEAttribute(abstractMetadataObjectEClass, ABSTRACT_METADATA_OBJECT__DIVERGENCY);

        metadataTableEClass = createEClass(METADATA_TABLE);
        createEAttribute(metadataTableEClass, METADATA_TABLE__SOURCE_NAME);
        createEAttribute(metadataTableEClass, METADATA_TABLE__TABLE_TYPE);
        createEAttribute(metadataTableEClass, METADATA_TABLE__ATTACHED_CDC);
        createEAttribute(metadataTableEClass, METADATA_TABLE__ACTIVATED_CDC);
        createEReference(metadataTableEClass, METADATA_TABLE__COLUMNS);
        createEReference(metadataTableEClass, METADATA_TABLE__CONNECTION);
        createEReference(metadataTableEClass, METADATA_TABLE__ADDITIONAL_PROPERTIES);

        fileConnectionEClass = createEClass(FILE_CONNECTION);
        createEAttribute(fileConnectionEClass, FILE_CONNECTION__SERVER);
        createEAttribute(fileConnectionEClass, FILE_CONNECTION__FILE_PATH);
        createEAttribute(fileConnectionEClass, FILE_CONNECTION__FORMAT);
        createEAttribute(fileConnectionEClass, FILE_CONNECTION__ENCODING);
        createEAttribute(fileConnectionEClass, FILE_CONNECTION__FIELD_SEPARATOR_VALUE);
        createEAttribute(fileConnectionEClass, FILE_CONNECTION__ROW_SEPARATOR_TYPE);
        createEAttribute(fileConnectionEClass, FILE_CONNECTION__ROW_SEPARATOR_VALUE);
        createEAttribute(fileConnectionEClass, FILE_CONNECTION__TEXT_IDENTIFIER);
        createEAttribute(fileConnectionEClass, FILE_CONNECTION__USE_HEADER);
        createEAttribute(fileConnectionEClass, FILE_CONNECTION__HEADER_VALUE);
        createEAttribute(fileConnectionEClass, FILE_CONNECTION__USE_FOOTER);
        createEAttribute(fileConnectionEClass, FILE_CONNECTION__FOOTER_VALUE);
        createEAttribute(fileConnectionEClass, FILE_CONNECTION__USE_LIMIT);
        createEAttribute(fileConnectionEClass, FILE_CONNECTION__LIMIT_VALUE);
        createEAttribute(fileConnectionEClass, FILE_CONNECTION__FIRST_LINE_CAPTION);
        createEAttribute(fileConnectionEClass, FILE_CONNECTION__REMOVE_EMPTY_ROW);
        createEAttribute(fileConnectionEClass, FILE_CONNECTION__ESCAPE_TYPE);
        createEAttribute(fileConnectionEClass, FILE_CONNECTION__ESCAPE_CHAR);
        createEAttribute(fileConnectionEClass, FILE_CONNECTION__TEXT_ENCLOSURE);
        createEAttribute(fileConnectionEClass, FILE_CONNECTION__CSV_OPTION);

        delimitedFileConnectionEClass = createEClass(DELIMITED_FILE_CONNECTION);
        createEAttribute(delimitedFileConnectionEClass, DELIMITED_FILE_CONNECTION__FIELD_SEPARATOR_TYPE);
        createEAttribute(delimitedFileConnectionEClass, DELIMITED_FILE_CONNECTION__SPLIT_RECORD);

        positionalFileConnectionEClass = createEClass(POSITIONAL_FILE_CONNECTION);

        ebcdicConnectionEClass = createEClass(EBCDIC_CONNECTION);
        createEAttribute(ebcdicConnectionEClass, EBCDIC_CONNECTION__MID_FILE);
        createEAttribute(ebcdicConnectionEClass, EBCDIC_CONNECTION__DATA_FILE);
        createEAttribute(ebcdicConnectionEClass, EBCDIC_CONNECTION__CODE_PAGE);
        createEAttribute(ebcdicConnectionEClass, EBCDIC_CONNECTION__SOURCE_FILE_START);
        createEAttribute(ebcdicConnectionEClass, EBCDIC_CONNECTION__SOURCE_FILE_END);

        mdmConnectionEClass = createEClass(MDM_CONNECTION);
        createEAttribute(mdmConnectionEClass, MDM_CONNECTION__USERNAME);
        createEAttribute(mdmConnectionEClass, MDM_CONNECTION__PASSWORD);
        createEAttribute(mdmConnectionEClass, MDM_CONNECTION__PORT);
        createEAttribute(mdmConnectionEClass, MDM_CONNECTION__SERVER);
        createEAttribute(mdmConnectionEClass, MDM_CONNECTION__UNIVERSE);
        createEAttribute(mdmConnectionEClass, MDM_CONNECTION__DATAMODEL);
        createEAttribute(mdmConnectionEClass, MDM_CONNECTION__DATACLUSTER);
        createEReference(mdmConnectionEClass, MDM_CONNECTION__SCHEMAS);
        createEAttribute(mdmConnectionEClass, MDM_CONNECTION__PROTOCOL);
        createEAttribute(mdmConnectionEClass, MDM_CONNECTION__CONTEXT);
        createEAttribute(mdmConnectionEClass, MDM_CONNECTION__SERVER_URL);

        databaseConnectionEClass = createEClass(DATABASE_CONNECTION);
        createEAttribute(databaseConnectionEClass, DATABASE_CONNECTION__DATABASE_TYPE);
        createEAttribute(databaseConnectionEClass, DATABASE_CONNECTION__DRIVER_JAR_PATH);
        createEAttribute(databaseConnectionEClass, DATABASE_CONNECTION__DRIVER_CLASS);
        createEAttribute(databaseConnectionEClass, DATABASE_CONNECTION__URL);
        createEAttribute(databaseConnectionEClass, DATABASE_CONNECTION__DB_VERSION_STRING);
        createEAttribute(databaseConnectionEClass, DATABASE_CONNECTION__PORT);
        createEAttribute(databaseConnectionEClass, DATABASE_CONNECTION__USERNAME);
        createEAttribute(databaseConnectionEClass, DATABASE_CONNECTION__PASSWORD);
        createEAttribute(databaseConnectionEClass, DATABASE_CONNECTION__SERVER_NAME);
        createEAttribute(databaseConnectionEClass, DATABASE_CONNECTION__DATASOURCE_NAME);
        createEAttribute(databaseConnectionEClass, DATABASE_CONNECTION__FILE_FIELD_NAME);
        createEAttribute(databaseConnectionEClass, DATABASE_CONNECTION__SID);
        createEAttribute(databaseConnectionEClass, DATABASE_CONNECTION__SQL_SYNTHAX);
        createEAttribute(databaseConnectionEClass, DATABASE_CONNECTION__STRING_QUOTE);
        createEAttribute(databaseConnectionEClass, DATABASE_CONNECTION__NULL_CHAR);
        createEAttribute(databaseConnectionEClass, DATABASE_CONNECTION__DBMS_ID);
        createEAttribute(databaseConnectionEClass, DATABASE_CONNECTION__PRODUCT_ID);
        createEAttribute(databaseConnectionEClass, DATABASE_CONNECTION__DB_ROOT_PATH);
        createEAttribute(databaseConnectionEClass, DATABASE_CONNECTION__ADDITIONAL_PARAMS);
        createEAttribute(databaseConnectionEClass, DATABASE_CONNECTION__STANDARD_SQL);
        createEAttribute(databaseConnectionEClass, DATABASE_CONNECTION__SYSTEM_SQL);
        createEReference(databaseConnectionEClass, DATABASE_CONNECTION__CDC_CONNS);
        createEAttribute(databaseConnectionEClass, DATABASE_CONNECTION__CDC_TYPE_MODE);
        createEAttribute(databaseConnectionEClass, DATABASE_CONNECTION__SQL_MODE);
        createEAttribute(databaseConnectionEClass, DATABASE_CONNECTION__UI_SCHEMA);
        createEReference(databaseConnectionEClass, DATABASE_CONNECTION__PARAMETERS);

        sapConnectionEClass = createEClass(SAP_CONNECTION);
        createEAttribute(sapConnectionEClass, SAP_CONNECTION__HOST);
        createEAttribute(sapConnectionEClass, SAP_CONNECTION__USERNAME);
        createEAttribute(sapConnectionEClass, SAP_CONNECTION__PASSWORD);
        createEAttribute(sapConnectionEClass, SAP_CONNECTION__CLIENT);
        createEAttribute(sapConnectionEClass, SAP_CONNECTION__SYSTEM_NUMBER);
        createEAttribute(sapConnectionEClass, SAP_CONNECTION__LANGUAGE);
        createEReference(sapConnectionEClass, SAP_CONNECTION__FUNTIONS);
        createEAttribute(sapConnectionEClass, SAP_CONNECTION__CURRENT_FUCNTION);
        createEReference(sapConnectionEClass, SAP_CONNECTION__IDOCS);
        createEAttribute(sapConnectionEClass, SAP_CONNECTION__JCO_VERSION);
        createEReference(sapConnectionEClass, SAP_CONNECTION__ADDITIONAL_PROPERTIES);
        createEReference(sapConnectionEClass, SAP_CONNECTION__BW_DATA_SOURCES);
        createEReference(sapConnectionEClass, SAP_CONNECTION__BW_DATA_STORE_OBJECTS);
        createEReference(sapConnectionEClass, SAP_CONNECTION__BW_INFO_CUBES);
        createEReference(sapConnectionEClass, SAP_CONNECTION__BW_INFO_OBJECTS);

        sapFunctionUnitEClass = createEClass(SAP_FUNCTION_UNIT);
        createEAttribute(sapFunctionUnitEClass, SAP_FUNCTION_UNIT__OUTPUT_TYPE);
        createEAttribute(sapFunctionUnitEClass, SAP_FUNCTION_UNIT__OUTPUT_TABLE_NAME);
        createEReference(sapFunctionUnitEClass, SAP_FUNCTION_UNIT__INPUT_PARAMETER_TABLE);
        createEReference(sapFunctionUnitEClass, SAP_FUNCTION_UNIT__OUTPUT_PARAMETER_TABLE);
        createEReference(sapFunctionUnitEClass, SAP_FUNCTION_UNIT__METADATA_TABLE);
        createEReference(sapFunctionUnitEClass, SAP_FUNCTION_UNIT__CONNECTION);
        createEReference(sapFunctionUnitEClass, SAP_FUNCTION_UNIT__TABLES);
        createEReference(sapFunctionUnitEClass, SAP_FUNCTION_UNIT__INPUT_TABLES);
        createEReference(sapFunctionUnitEClass, SAP_FUNCTION_UNIT__TEST_INPUT_PARAMETER_TABLE);
        createEReference(sapFunctionUnitEClass, SAP_FUNCTION_UNIT__PARAM_DATA);
        createEAttribute(sapFunctionUnitEClass, SAP_FUNCTION_UNIT__AS_XML_SCHEMA);

        sapiDocUnitEClass = createEClass(SAPI_DOC_UNIT);
        createEReference(sapiDocUnitEClass, SAPI_DOC_UNIT__CONNECTION);
        createEAttribute(sapiDocUnitEClass, SAPI_DOC_UNIT__PROGRAM_ID);
        createEAttribute(sapiDocUnitEClass, SAPI_DOC_UNIT__GATEWAY_SERVICE);
        createEAttribute(sapiDocUnitEClass, SAPI_DOC_UNIT__USE_XML_OUTPUT);
        createEAttribute(sapiDocUnitEClass, SAPI_DOC_UNIT__XML_FILE);
        createEAttribute(sapiDocUnitEClass, SAPI_DOC_UNIT__USE_HTML_OUTPUT);
        createEAttribute(sapiDocUnitEClass, SAPI_DOC_UNIT__HTML_FILE);

        sapFunctionParameterColumnEClass = createEClass(SAP_FUNCTION_PARAMETER_COLUMN);
        createEAttribute(sapFunctionParameterColumnEClass, SAP_FUNCTION_PARAMETER_COLUMN__PARAMETER_TYPE);
        createEAttribute(sapFunctionParameterColumnEClass, SAP_FUNCTION_PARAMETER_COLUMN__STRUCTURE_OR_TABLE_NAME);
        createEAttribute(sapFunctionParameterColumnEClass, SAP_FUNCTION_PARAMETER_COLUMN__DATA_TYPE);
        createEAttribute(sapFunctionParameterColumnEClass, SAP_FUNCTION_PARAMETER_COLUMN__LENGTH);
        createEAttribute(sapFunctionParameterColumnEClass, SAP_FUNCTION_PARAMETER_COLUMN__VALUE);
        createEReference(sapFunctionParameterColumnEClass, SAP_FUNCTION_PARAMETER_COLUMN__PARAMETER_TABLE);

        sapFunctionParameterTableEClass = createEClass(SAP_FUNCTION_PARAMETER_TABLE);
        createEReference(sapFunctionParameterTableEClass, SAP_FUNCTION_PARAMETER_TABLE__COLUMNS);

        inputSAPFunctionParameterTableEClass = createEClass(INPUT_SAP_FUNCTION_PARAMETER_TABLE);
        createEReference(inputSAPFunctionParameterTableEClass, INPUT_SAP_FUNCTION_PARAMETER_TABLE__FUNCTION_UNIT);

        outputSAPFunctionParameterTableEClass = createEClass(OUTPUT_SAP_FUNCTION_PARAMETER_TABLE);
        createEReference(outputSAPFunctionParameterTableEClass, OUTPUT_SAP_FUNCTION_PARAMETER_TABLE__FUNCTION_UNIT);

        regexpFileConnectionEClass = createEClass(REGEXP_FILE_CONNECTION);
        createEAttribute(regexpFileConnectionEClass, REGEXP_FILE_CONNECTION__FIELD_SEPARATOR_TYPE);

        xmlFileConnectionEClass = createEClass(XML_FILE_CONNECTION);
        createEAttribute(xmlFileConnectionEClass, XML_FILE_CONNECTION__XSD_FILE_PATH);
        createEAttribute(xmlFileConnectionEClass, XML_FILE_CONNECTION__XML_FILE_PATH);
        createEAttribute(xmlFileConnectionEClass, XML_FILE_CONNECTION__GUESS);
        createEAttribute(xmlFileConnectionEClass, XML_FILE_CONNECTION__MASK_XPATTERN);
        createEReference(xmlFileConnectionEClass, XML_FILE_CONNECTION__SCHEMA);
        createEAttribute(xmlFileConnectionEClass, XML_FILE_CONNECTION__ENCODING);
        createEReference(xmlFileConnectionEClass, XML_FILE_CONNECTION__GROUP);
        createEReference(xmlFileConnectionEClass, XML_FILE_CONNECTION__ROOT);
        createEReference(xmlFileConnectionEClass, XML_FILE_CONNECTION__LOOP);
        createEAttribute(xmlFileConnectionEClass, XML_FILE_CONNECTION__INPUT_MODEL);
        createEAttribute(xmlFileConnectionEClass, XML_FILE_CONNECTION__OUTPUT_FILE_PATH);
        createEAttribute(xmlFileConnectionEClass, XML_FILE_CONNECTION__FILE_CONTENT);
        createEAttribute(xmlFileConnectionEClass, XML_FILE_CONNECTION__TARGET_NAME_SPACE);

        schemaTargetEClass = createEClass(SCHEMA_TARGET);
        createEAttribute(schemaTargetEClass, SCHEMA_TARGET__RELATIVE_XPATH_QUERY);
        createEAttribute(schemaTargetEClass, SCHEMA_TARGET__TAG_NAME);
        createEReference(schemaTargetEClass, SCHEMA_TARGET__SCHEMA);

        queriesConnectionEClass = createEClass(QUERIES_CONNECTION);
        createEReference(queriesConnectionEClass, QUERIES_CONNECTION__CONNECTION);
        createEReference(queriesConnectionEClass, QUERIES_CONNECTION__QUERY);

        queryEClass = createEClass(QUERY);
        createEAttribute(queryEClass, QUERY__VALUE);
        createEReference(queryEClass, QUERY__QUERIES);
        createEAttribute(queryEClass, QUERY__CONTEXT_MODE);

        ldifFileConnectionEClass = createEClass(LDIF_FILE_CONNECTION);
        createEAttribute(ldifFileConnectionEClass, LDIF_FILE_CONNECTION__VALUE);
        createEAttribute(ldifFileConnectionEClass, LDIF_FILE_CONNECTION__FILE_PATH);
        createEAttribute(ldifFileConnectionEClass, LDIF_FILE_CONNECTION__LIMIT_ENTRY);
        createEAttribute(ldifFileConnectionEClass, LDIF_FILE_CONNECTION__USE_LIMIT);
        createEAttribute(ldifFileConnectionEClass, LDIF_FILE_CONNECTION__SERVER);

        fileExcelConnectionEClass = createEClass(FILE_EXCEL_CONNECTION);
        createEAttribute(fileExcelConnectionEClass, FILE_EXCEL_CONNECTION__SHEET_NAME);
        createEAttribute(fileExcelConnectionEClass, FILE_EXCEL_CONNECTION__SHEET_COLUMNS);
        createEAttribute(fileExcelConnectionEClass, FILE_EXCEL_CONNECTION__FIRST_COLUMN);
        createEAttribute(fileExcelConnectionEClass, FILE_EXCEL_CONNECTION__LAST_COLUMN);
        createEAttribute(fileExcelConnectionEClass, FILE_EXCEL_CONNECTION__THOUSAND_SEPARATOR);
        createEAttribute(fileExcelConnectionEClass, FILE_EXCEL_CONNECTION__DECIMAL_SEPARATOR);
        createEAttribute(fileExcelConnectionEClass, FILE_EXCEL_CONNECTION__ADVANCED_SPEARATOR);
        createEAttribute(fileExcelConnectionEClass, FILE_EXCEL_CONNECTION__SELECT_ALL_SHEETS);
        createEAttribute(fileExcelConnectionEClass, FILE_EXCEL_CONNECTION__SHEET_LIST);
        createEAttribute(fileExcelConnectionEClass, FILE_EXCEL_CONNECTION__GENERATION_MODE);

        xmlXPathLoopDescriptorEClass = createEClass(XML_XPATH_LOOP_DESCRIPTOR);
        createEAttribute(xmlXPathLoopDescriptorEClass, XML_XPATH_LOOP_DESCRIPTOR__LIMIT_BOUCLE);
        createEAttribute(xmlXPathLoopDescriptorEClass, XML_XPATH_LOOP_DESCRIPTOR__ABSOLUTE_XPATH_QUERY);
        createEReference(xmlXPathLoopDescriptorEClass, XML_XPATH_LOOP_DESCRIPTOR__CONNECTION);
        createEReference(xmlXPathLoopDescriptorEClass, XML_XPATH_LOOP_DESCRIPTOR__SCHEMA_TARGETS);

        genericSchemaConnectionEClass = createEClass(GENERIC_SCHEMA_CONNECTION);
        createEAttribute(genericSchemaConnectionEClass, GENERIC_SCHEMA_CONNECTION__MAPPING_TYPE_USED);
        createEAttribute(genericSchemaConnectionEClass, GENERIC_SCHEMA_CONNECTION__MAPPING_TYPE_ID);

        ldapSchemaConnectionEClass = createEClass(LDAP_SCHEMA_CONNECTION);
        createEAttribute(ldapSchemaConnectionEClass, LDAP_SCHEMA_CONNECTION__HOST);
        createEAttribute(ldapSchemaConnectionEClass, LDAP_SCHEMA_CONNECTION__PORT);
        createEAttribute(ldapSchemaConnectionEClass, LDAP_SCHEMA_CONNECTION__PROTOCOL);
        createEAttribute(ldapSchemaConnectionEClass, LDAP_SCHEMA_CONNECTION__FILTER);
        createEAttribute(ldapSchemaConnectionEClass, LDAP_SCHEMA_CONNECTION__SEPARATOR);
        createEAttribute(ldapSchemaConnectionEClass, LDAP_SCHEMA_CONNECTION__USE_ADVANCED);
        createEAttribute(ldapSchemaConnectionEClass, LDAP_SCHEMA_CONNECTION__STORE_PATH);
        createEAttribute(ldapSchemaConnectionEClass, LDAP_SCHEMA_CONNECTION__USE_LIMIT);
        createEAttribute(ldapSchemaConnectionEClass, LDAP_SCHEMA_CONNECTION__USE_AUTHEN);
        createEAttribute(ldapSchemaConnectionEClass, LDAP_SCHEMA_CONNECTION__BIND_PRINCIPAL);
        createEAttribute(ldapSchemaConnectionEClass, LDAP_SCHEMA_CONNECTION__BIND_PASSWORD);
        createEAttribute(ldapSchemaConnectionEClass, LDAP_SCHEMA_CONNECTION__LIMIT_VALUE);
        createEAttribute(ldapSchemaConnectionEClass, LDAP_SCHEMA_CONNECTION__ENCRYPTION_METHOD_NAME);
        createEAttribute(ldapSchemaConnectionEClass, LDAP_SCHEMA_CONNECTION__VALUE);
        createEAttribute(ldapSchemaConnectionEClass, LDAP_SCHEMA_CONNECTION__SAVE_PASSWORD);
        createEAttribute(ldapSchemaConnectionEClass, LDAP_SCHEMA_CONNECTION__ALIASES);
        createEAttribute(ldapSchemaConnectionEClass, LDAP_SCHEMA_CONNECTION__REFERRALS);
        createEAttribute(ldapSchemaConnectionEClass, LDAP_SCHEMA_CONNECTION__COUNT_LIMIT);
        createEAttribute(ldapSchemaConnectionEClass, LDAP_SCHEMA_CONNECTION__TIME_OUT_LIMIT);
        createEAttribute(ldapSchemaConnectionEClass, LDAP_SCHEMA_CONNECTION__BASE_DNS);
        createEAttribute(ldapSchemaConnectionEClass, LDAP_SCHEMA_CONNECTION__GET_BASE_DNS_FROM_ROOT);
        createEAttribute(ldapSchemaConnectionEClass, LDAP_SCHEMA_CONNECTION__RETURN_ATTRIBUTES);
        createEAttribute(ldapSchemaConnectionEClass, LDAP_SCHEMA_CONNECTION__SELECTED_DN);

        wsdlSchemaConnectionEClass = createEClass(WSDL_SCHEMA_CONNECTION);
        createEAttribute(wsdlSchemaConnectionEClass, WSDL_SCHEMA_CONNECTION__WSDL);
        createEAttribute(wsdlSchemaConnectionEClass, WSDL_SCHEMA_CONNECTION__NEED_AUTH);
        createEAttribute(wsdlSchemaConnectionEClass, WSDL_SCHEMA_CONNECTION__METHOD_NAME);
        createEAttribute(wsdlSchemaConnectionEClass, WSDL_SCHEMA_CONNECTION__PARAMETERS);
        createEAttribute(wsdlSchemaConnectionEClass, WSDL_SCHEMA_CONNECTION__USER_NAME);
        createEAttribute(wsdlSchemaConnectionEClass, WSDL_SCHEMA_CONNECTION__PASSWORD);
        createEAttribute(wsdlSchemaConnectionEClass, WSDL_SCHEMA_CONNECTION__USE_PROXY);
        createEAttribute(wsdlSchemaConnectionEClass, WSDL_SCHEMA_CONNECTION__PROXY_HOST);
        createEAttribute(wsdlSchemaConnectionEClass, WSDL_SCHEMA_CONNECTION__PROXY_PORT);
        createEAttribute(wsdlSchemaConnectionEClass, WSDL_SCHEMA_CONNECTION__PROXY_USER);
        createEAttribute(wsdlSchemaConnectionEClass, WSDL_SCHEMA_CONNECTION__PROXY_PASSWORD);
        createEAttribute(wsdlSchemaConnectionEClass, WSDL_SCHEMA_CONNECTION__VALUE);
        createEAttribute(wsdlSchemaConnectionEClass, WSDL_SCHEMA_CONNECTION__ENDPOINT_URI);
        createEAttribute(wsdlSchemaConnectionEClass, WSDL_SCHEMA_CONNECTION__ENCODING);
        createEAttribute(wsdlSchemaConnectionEClass, WSDL_SCHEMA_CONNECTION__TIME_OUT);
        createEAttribute(wsdlSchemaConnectionEClass, WSDL_SCHEMA_CONNECTION__IS_INPUT_MODEL);
        createEAttribute(wsdlSchemaConnectionEClass, WSDL_SCHEMA_CONNECTION__SERVER_NAME_SPACE);
        createEAttribute(wsdlSchemaConnectionEClass, WSDL_SCHEMA_CONNECTION__SERVER_NAME);
        createEAttribute(wsdlSchemaConnectionEClass, WSDL_SCHEMA_CONNECTION__PORT_NAME_SPACE);
        createEAttribute(wsdlSchemaConnectionEClass, WSDL_SCHEMA_CONNECTION__PORT_NAME);
        createEReference(wsdlSchemaConnectionEClass, WSDL_SCHEMA_CONNECTION__PARAMETER_VALUE);
        createEReference(wsdlSchemaConnectionEClass, WSDL_SCHEMA_CONNECTION__OUTPUT_PARAMETER);

        salesforceSchemaConnectionEClass = createEClass(SALESFORCE_SCHEMA_CONNECTION);
        createEAttribute(salesforceSchemaConnectionEClass, SALESFORCE_SCHEMA_CONNECTION__WEB_SERVICE_URL);
        createEAttribute(salesforceSchemaConnectionEClass, SALESFORCE_SCHEMA_CONNECTION__USER_NAME);
        createEAttribute(salesforceSchemaConnectionEClass, SALESFORCE_SCHEMA_CONNECTION__PASSWORD);
        createEAttribute(salesforceSchemaConnectionEClass, SALESFORCE_SCHEMA_CONNECTION__MODULE_NAME);
        createEAttribute(salesforceSchemaConnectionEClass, SALESFORCE_SCHEMA_CONNECTION__QUERY_CONDITION);
        createEAttribute(salesforceSchemaConnectionEClass, SALESFORCE_SCHEMA_CONNECTION__USE_CUSTOM_MODULE_NAME);
        createEAttribute(salesforceSchemaConnectionEClass, SALESFORCE_SCHEMA_CONNECTION__USE_PROXY);
        createEAttribute(salesforceSchemaConnectionEClass, SALESFORCE_SCHEMA_CONNECTION__PROXY_HOST);
        createEAttribute(salesforceSchemaConnectionEClass, SALESFORCE_SCHEMA_CONNECTION__PROXY_PORT);
        createEAttribute(salesforceSchemaConnectionEClass, SALESFORCE_SCHEMA_CONNECTION__PROXY_USERNAME);
        createEAttribute(salesforceSchemaConnectionEClass, SALESFORCE_SCHEMA_CONNECTION__PROXY_PASSWORD);
        createEAttribute(salesforceSchemaConnectionEClass, SALESFORCE_SCHEMA_CONNECTION__BATCH_SIZE);
        createEAttribute(salesforceSchemaConnectionEClass, SALESFORCE_SCHEMA_CONNECTION__USE_HTTP_PROXY);
        createEAttribute(salesforceSchemaConnectionEClass, SALESFORCE_SCHEMA_CONNECTION__USE_ALPHBET);
        createEAttribute(salesforceSchemaConnectionEClass, SALESFORCE_SCHEMA_CONNECTION__TIME_OUT);
        createEReference(salesforceSchemaConnectionEClass, SALESFORCE_SCHEMA_CONNECTION__MODULES);
        createEAttribute(salesforceSchemaConnectionEClass, SALESFORCE_SCHEMA_CONNECTION__WEB_SERVICE_URL_TEXT_FOR_OAUTH);
        createEAttribute(salesforceSchemaConnectionEClass, SALESFORCE_SCHEMA_CONNECTION__CONSUME_KEY);
        createEAttribute(salesforceSchemaConnectionEClass, SALESFORCE_SCHEMA_CONNECTION__CONSUME_SECRET);
        createEAttribute(salesforceSchemaConnectionEClass, SALESFORCE_SCHEMA_CONNECTION__CALLBACK_HOST);
        createEAttribute(salesforceSchemaConnectionEClass, SALESFORCE_SCHEMA_CONNECTION__CALLBACK_PORT);
        createEAttribute(salesforceSchemaConnectionEClass, SALESFORCE_SCHEMA_CONNECTION__SALESFORCE_VERSION);
        createEAttribute(salesforceSchemaConnectionEClass, SALESFORCE_SCHEMA_CONNECTION__TOKEN);
        createEAttribute(salesforceSchemaConnectionEClass, SALESFORCE_SCHEMA_CONNECTION__LOGIN_TYPE);

        cdcConnectionEClass = createEClass(CDC_CONNECTION);
        createEReference(cdcConnectionEClass, CDC_CONNECTION__CONNECTION);
        createEReference(cdcConnectionEClass, CDC_CONNECTION__CDC_TYPES);

        cdcTypeEClass = createEClass(CDC_TYPE);
        createEAttribute(cdcTypeEClass, CDC_TYPE__LINK_DB);
        createEReference(cdcTypeEClass, CDC_TYPE__SUBSCRIBERS);
        createEReference(cdcTypeEClass, CDC_TYPE__CDC_CONNECTION);
        createEAttribute(cdcTypeEClass, CDC_TYPE__JOURNAL_NAME);

        subscriberTableEClass = createEClass(SUBSCRIBER_TABLE);
        createEAttribute(subscriberTableEClass, SUBSCRIBER_TABLE__SYSTEM);

        sapTestInputParameterTableEClass = createEClass(SAP_TEST_INPUT_PARAMETER_TABLE);
        createEReference(sapTestInputParameterTableEClass, SAP_TEST_INPUT_PARAMETER_TABLE__FUNCTION_UNIT);

        conceptEClass = createEClass(CONCEPT);
        createEAttribute(conceptEClass, CONCEPT__LOOP_EXPRESSION);
        createEAttribute(conceptEClass, CONCEPT__LOOP_LIMIT);
        createEReference(conceptEClass, CONCEPT__CONCEPT_TARGETS);
        createEAttribute(conceptEClass, CONCEPT__INPUT_MODEL);
        createEReference(conceptEClass, CONCEPT__GROUP);
        createEReference(conceptEClass, CONCEPT__ROOT);
        createEReference(conceptEClass, CONCEPT__LOOP);
        createEAttribute(conceptEClass, CONCEPT__CONCEPT_TYPE);
        createEAttribute(conceptEClass, CONCEPT__XPATH_PREFIX);

        conceptTargetEClass = createEClass(CONCEPT_TARGET);
        createEReference(conceptTargetEClass, CONCEPT_TARGET__SCHEMA);
        createEAttribute(conceptTargetEClass, CONCEPT_TARGET__TARGET_NAME);
        createEAttribute(conceptTargetEClass, CONCEPT_TARGET__RELATIVE_LOOP_EXPRESSION);

        hl7ConnectionEClass = createEClass(HL7_CONNECTION);
        createEAttribute(hl7ConnectionEClass, HL7_CONNECTION__START_CHAR);
        createEAttribute(hl7ConnectionEClass, HL7_CONNECTION__END_CHAR);
        createEReference(hl7ConnectionEClass, HL7_CONNECTION__ROOT);
        createEAttribute(hl7ConnectionEClass, HL7_CONNECTION__OUTPUT_FILE_PATH);

        headerFooterConnectionEClass = createEClass(HEADER_FOOTER_CONNECTION);
        createEAttribute(headerFooterConnectionEClass, HEADER_FOOTER_CONNECTION__IS_HEADER);
        createEAttribute(headerFooterConnectionEClass, HEADER_FOOTER_CONNECTION__IMPORTS);
        createEAttribute(headerFooterConnectionEClass, HEADER_FOOTER_CONNECTION__MAIN_CODE);
        createEAttribute(headerFooterConnectionEClass, HEADER_FOOTER_CONNECTION__LIBRARIES);

        xmlFileNodeEClass = createEClass(XML_FILE_NODE);
        createEAttribute(xmlFileNodeEClass, XML_FILE_NODE__XML_PATH);
        createEAttribute(xmlFileNodeEClass, XML_FILE_NODE__RELATED_COLUMN);
        createEAttribute(xmlFileNodeEClass, XML_FILE_NODE__DEFAULT_VALUE);
        createEAttribute(xmlFileNodeEClass, XML_FILE_NODE__ATTRIBUTE);
        createEAttribute(xmlFileNodeEClass, XML_FILE_NODE__ORDER);
        createEAttribute(xmlFileNodeEClass, XML_FILE_NODE__TYPE);

        wsdlParameterEClass = createEClass(WSDL_PARAMETER);
        createEAttribute(wsdlParameterEClass, WSDL_PARAMETER__ELEMENT);
        createEAttribute(wsdlParameterEClass, WSDL_PARAMETER__SOURCE);
        createEAttribute(wsdlParameterEClass, WSDL_PARAMETER__COLUMN);
        createEAttribute(wsdlParameterEClass, WSDL_PARAMETER__EXPRESSION);
        createEAttribute(wsdlParameterEClass, WSDL_PARAMETER__PARAMETER_INFO);
        createEAttribute(wsdlParameterEClass, WSDL_PARAMETER__PARAMETER_INFO_PARENT);

        genericPackageEClass = createEClass(GENERIC_PACKAGE);

        hl7FileNodeEClass = createEClass(HL7_FILE_NODE);
        createEAttribute(hl7FileNodeEClass, HL7_FILE_NODE__FILE_PATH);
        createEAttribute(hl7FileNodeEClass, HL7_FILE_NODE__ORDER);
        createEAttribute(hl7FileNodeEClass, HL7_FILE_NODE__ATTRIBUTE);
        createEAttribute(hl7FileNodeEClass, HL7_FILE_NODE__DEFAULT_VALUE);
        createEAttribute(hl7FileNodeEClass, HL7_FILE_NODE__RELATED_COLUMN);
        createEAttribute(hl7FileNodeEClass, HL7_FILE_NODE__REPEATABLE);

        ftpConnectionEClass = createEClass(FTP_CONNECTION);
        createEAttribute(ftpConnectionEClass, FTP_CONNECTION__HOST);
        createEAttribute(ftpConnectionEClass, FTP_CONNECTION__PORT);
        createEAttribute(ftpConnectionEClass, FTP_CONNECTION__USERNAME);
        createEAttribute(ftpConnectionEClass, FTP_CONNECTION__PASSWORD);
        createEAttribute(ftpConnectionEClass, FTP_CONNECTION__MODE);
        createEAttribute(ftpConnectionEClass, FTP_CONNECTION__ECODING);
        createEAttribute(ftpConnectionEClass, FTP_CONNECTION__SFTP);
        createEAttribute(ftpConnectionEClass, FTP_CONNECTION__FTPS);
        createEAttribute(ftpConnectionEClass, FTP_CONNECTION__METHOD);
        createEAttribute(ftpConnectionEClass, FTP_CONNECTION__PRIVATEKEY);
        createEAttribute(ftpConnectionEClass, FTP_CONNECTION__PASSPHRASE);
        createEAttribute(ftpConnectionEClass, FTP_CONNECTION__KEYSTORE_FILE);
        createEAttribute(ftpConnectionEClass, FTP_CONNECTION__KEYSTORE_PASSWORD);
        createEAttribute(ftpConnectionEClass, FTP_CONNECTION__USESOCKS);
        createEAttribute(ftpConnectionEClass, FTP_CONNECTION__PROXYHOST);
        createEAttribute(ftpConnectionEClass, FTP_CONNECTION__PROXYPORT);
        createEAttribute(ftpConnectionEClass, FTP_CONNECTION__PROXYUSER);
        createEAttribute(ftpConnectionEClass, FTP_CONNECTION__PROXYPASSWORD);
        createEAttribute(ftpConnectionEClass, FTP_CONNECTION__CUSTOM_ENCODE);
        createEAttribute(ftpConnectionEClass, FTP_CONNECTION__USE_FILE_NAME_ENCODING);

        brmsConnectionEClass = createEClass(BRMS_CONNECTION);
        createEAttribute(brmsConnectionEClass, BRMS_CONNECTION__XML_FIELD);
        createEAttribute(brmsConnectionEClass, BRMS_CONNECTION__URL_NAME);
        createEAttribute(brmsConnectionEClass, BRMS_CONNECTION__TAC_WEBAPP_NAME);
        createEAttribute(brmsConnectionEClass, BRMS_CONNECTION__CLASS_NAME);
        createEAttribute(brmsConnectionEClass, BRMS_CONNECTION__MODULE_USED);
        createEReference(brmsConnectionEClass, BRMS_CONNECTION__ROOT);
        createEReference(brmsConnectionEClass, BRMS_CONNECTION__GROUP);
        createEReference(brmsConnectionEClass, BRMS_CONNECTION__LOOP);
        createEAttribute(brmsConnectionEClass, BRMS_CONNECTION__PACKAGE);

        validationRulesConnectionEClass = createEClass(VALIDATION_RULES_CONNECTION);
        createEAttribute(validationRulesConnectionEClass, VALIDATION_RULES_CONNECTION__IS_SELECT);
        createEAttribute(validationRulesConnectionEClass, VALIDATION_RULES_CONNECTION__IS_INSERT);
        createEAttribute(validationRulesConnectionEClass, VALIDATION_RULES_CONNECTION__IS_UPDATE);
        createEAttribute(validationRulesConnectionEClass, VALIDATION_RULES_CONNECTION__IS_DELETE);
        createEAttribute(validationRulesConnectionEClass, VALIDATION_RULES_CONNECTION__TYPE);
        createEAttribute(validationRulesConnectionEClass, VALIDATION_RULES_CONNECTION__BASE_SCHEMA);
        createEAttribute(validationRulesConnectionEClass, VALIDATION_RULES_CONNECTION__BASE_COLUMN_NAMES);
        createEAttribute(validationRulesConnectionEClass, VALIDATION_RULES_CONNECTION__REF_SCHEMA);
        createEAttribute(validationRulesConnectionEClass, VALIDATION_RULES_CONNECTION__REF_COLUMN_NAMES);
        createEAttribute(validationRulesConnectionEClass, VALIDATION_RULES_CONNECTION__JAVA_CONDITION);
        createEAttribute(validationRulesConnectionEClass, VALIDATION_RULES_CONNECTION__SQL_CONDITION);
        createEAttribute(validationRulesConnectionEClass, VALIDATION_RULES_CONNECTION__LOGICAL_OPERATOR);
        createEReference(validationRulesConnectionEClass, VALIDATION_RULES_CONNECTION__CONDITIONS);
        createEReference(validationRulesConnectionEClass, VALIDATION_RULES_CONNECTION__INNER_JOINS);
        createEAttribute(validationRulesConnectionEClass, VALIDATION_RULES_CONNECTION__IS_DISALLOW);
        createEAttribute(validationRulesConnectionEClass, VALIDATION_RULES_CONNECTION__IS_REJECT_LINK);

        conditionTypeEClass = createEClass(CONDITION_TYPE);
        createEAttribute(conditionTypeEClass, CONDITION_TYPE__INPUT_COLUMN);
        createEAttribute(conditionTypeEClass, CONDITION_TYPE__FUNCTION);
        createEAttribute(conditionTypeEClass, CONDITION_TYPE__OPERATOR);
        createEAttribute(conditionTypeEClass, CONDITION_TYPE__VALUE);

        innerJoinMapEClass = createEClass(INNER_JOIN_MAP);
        createEAttribute(innerJoinMapEClass, INNER_JOIN_MAP__KEY);
        createEAttribute(innerJoinMapEClass, INNER_JOIN_MAP__VALUE);

        edifactConnectionEClass = createEClass(EDIFACT_CONNECTION);
        createEAttribute(edifactConnectionEClass, EDIFACT_CONNECTION__XML_NAME);
        createEAttribute(edifactConnectionEClass, EDIFACT_CONNECTION__FILE_NAME);
        createEAttribute(edifactConnectionEClass, EDIFACT_CONNECTION__XML_PATH);

        edifactColumnEClass = createEClass(EDIFACT_COLUMN);
        createEAttribute(edifactColumnEClass, EDIFACT_COLUMN__EDI_COLUMN_NAME);
        createEAttribute(edifactColumnEClass, EDIFACT_COLUMN__EDI_XPATH);

        salesforceModuleUnitEClass = createEClass(SALESFORCE_MODULE_UNIT);
        createEReference(salesforceModuleUnitEClass, SALESFORCE_MODULE_UNIT__METADATA_TABLE);
        createEReference(salesforceModuleUnitEClass, SALESFORCE_MODULE_UNIT__CONNECTION);
        createEReference(salesforceModuleUnitEClass, SALESFORCE_MODULE_UNIT__TABLES);
        createEAttribute(salesforceModuleUnitEClass, SALESFORCE_MODULE_UNIT__MODULE_NAME);

        additionalPropertiesEClass = createEClass(ADDITIONAL_PROPERTIES);
        createEAttribute(additionalPropertiesEClass, ADDITIONAL_PROPERTIES__KEY);
        createEAttribute(additionalPropertiesEClass, ADDITIONAL_PROPERTIES__VALUE);

        sapTableEClass = createEClass(SAP_TABLE);
        createEAttribute(sapTableEClass, SAP_TABLE__TABLE_SEARCH_TYPE);

        sapTableFieldEClass = createEClass(SAP_TABLE_FIELD);
        createEAttribute(sapTableFieldEClass, SAP_TABLE_FIELD__BUSINESS_NAME);
        createEAttribute(sapTableFieldEClass, SAP_TABLE_FIELD__REF_TABLE);

        sapFunctionParameterEClass = createEClass(SAP_FUNCTION_PARAMETER);
        createEAttribute(sapFunctionParameterEClass, SAP_FUNCTION_PARAMETER__NAME);
        createEAttribute(sapFunctionParameterEClass, SAP_FUNCTION_PARAMETER__TYPE);
        createEAttribute(sapFunctionParameterEClass, SAP_FUNCTION_PARAMETER__DESCRIPTION);
        createEAttribute(sapFunctionParameterEClass, SAP_FUNCTION_PARAMETER__LENGTH);
        createEAttribute(sapFunctionParameterEClass, SAP_FUNCTION_PARAMETER__CHANGING);
        createEAttribute(sapFunctionParameterEClass, SAP_FUNCTION_PARAMETER__TEST_VALUE);
        createEReference(sapFunctionParameterEClass, SAP_FUNCTION_PARAMETER__CHILDREN);
        createEAttribute(sapFunctionParameterEClass, SAP_FUNCTION_PARAMETER__TABLE_RESIDE_IN_TABLES);

        sapFunctionParamDataEClass = createEClass(SAP_FUNCTION_PARAM_DATA);
        createEReference(sapFunctionParamDataEClass, SAP_FUNCTION_PARAM_DATA__INPUT_ROOT);
        createEReference(sapFunctionParamDataEClass, SAP_FUNCTION_PARAM_DATA__OUTPUT_ROOT);

        additionalConnectionPropertyEClass = createEClass(ADDITIONAL_CONNECTION_PROPERTY);
        createEAttribute(additionalConnectionPropertyEClass, ADDITIONAL_CONNECTION_PROPERTY__PROPERTY_NAME);
        createEAttribute(additionalConnectionPropertyEClass, ADDITIONAL_CONNECTION_PROPERTY__VALUE);

        sapbwTableEClass = createEClass(SAPBW_TABLE);
        createEAttribute(sapbwTableEClass, SAPBW_TABLE__MODEL_TYPE);
        createEAttribute(sapbwTableEClass, SAPBW_TABLE__ACTIVE);
        createEAttribute(sapbwTableEClass, SAPBW_TABLE__SOURCE_SYSTEM_NAME);
        createEAttribute(sapbwTableEClass, SAPBW_TABLE__INFO_AREA_NAME);
        createEAttribute(sapbwTableEClass, SAPBW_TABLE__INNER_IO_TYPE);

        sapbwTableFieldEClass = createEClass(SAPBW_TABLE_FIELD);
        createEAttribute(sapbwTableFieldEClass, SAPBW_TABLE_FIELD__LOGICAL_NAME);

        // Create enums
        fileFormatEEnum = createEEnum(FILE_FORMAT);
        fieldSeparatorEEnum = createEEnum(FIELD_SEPARATOR);
        escapeEEnum = createEEnum(ESCAPE);
        rowSeparatorEEnum = createEEnum(ROW_SEPARATOR);
        mdmConnectionProtocolEEnum = createEEnum(MDM_CONNECTION_PROTOCOL);
        mdmConceptTypeEEnum = createEEnum(MDM_CONCEPT_TYPE);
        ruleTypeEEnum = createEEnum(RULE_TYPE);
        functionEEnum = createEEnum(FUNCTION);
        operatorEEnum = createEEnum(OPERATOR);
        logicalOperatorEEnum = createEEnum(LOGICAL_OPERATOR);

        // Create data types
        mapEDataType = createEDataType(MAP);
        listEDataType = createEDataType(LIST);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model.  This
     * method is guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void initializePackageContents() {
        if (isInitialized)
            return;
        isInitialized = true;

        // Initialize package
        setName(eNAME);
        setNsPrefix(eNS_PREFIX);
        setNsURI(eNS_URI);

        // Obtain other dependent packages
        org.talend.cwm.relational.RelationalPackage theRelationalPackage_1 = (org.talend.cwm.relational.RelationalPackage) EPackage.Registry.INSTANCE
                .getEPackage(org.talend.cwm.relational.RelationalPackage.eNS_URI);
        org.talend.cwm.softwaredeployment.SoftwaredeploymentPackage theSoftwaredeploymentPackage_1 = (org.talend.cwm.softwaredeployment.SoftwaredeploymentPackage) EPackage.Registry.INSTANCE
                .getEPackage(org.talend.cwm.softwaredeployment.SoftwaredeploymentPackage.eNS_URI);
        ConstantsPackage theConstantsPackage = (ConstantsPackage) EPackage.Registry.INSTANCE
                .getEPackage(ConstantsPackage.eNS_URI);
        org.talend.cwm.xml.XmlPackage theXmlPackage_1 = (org.talend.cwm.xml.XmlPackage) EPackage.Registry.INSTANCE
                .getEPackage(org.talend.cwm.xml.XmlPackage.eNS_URI);
        SoftwaredeploymentPackage theSoftwaredeploymentPackage = (SoftwaredeploymentPackage) EPackage.Registry.INSTANCE
                .getEPackage(SoftwaredeploymentPackage.eNS_URI);
        CorePackage theCorePackage = (CorePackage) EPackage.Registry.INSTANCE.getEPackage(CorePackage.eNS_URI);
        RecordPackage theRecordPackage = (RecordPackage) EPackage.Registry.INSTANCE.getEPackage(RecordPackage.eNS_URI);

        // Add subpackages
        getESubpackages().add(theRelationalPackage_1);
        getESubpackages().add(theSoftwaredeploymentPackage_1);
        getESubpackages().add(theConstantsPackage);
        getESubpackages().add(theXmlPackage_1);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        metadataEClass.getESuperTypes().add(this.getAbstractMetadataObject());
        connectionEClass.getESuperTypes().add(this.getAbstractMetadataObject());
        connectionEClass.getESuperTypes().add(theSoftwaredeploymentPackage.getDataProvider());
        metadataColumnEClass.getESuperTypes().add(this.getAbstractMetadataObject());
        metadataColumnEClass.getESuperTypes().add(theRecordPackage.getField());
        abstractMetadataObjectEClass.getESuperTypes().add(theCorePackage.getModelElement());
        metadataTableEClass.getESuperTypes().add(this.getAbstractMetadataObject());
        metadataTableEClass.getESuperTypes().add(theCorePackage.getClass_());
        fileConnectionEClass.getESuperTypes().add(this.getConnection());
        delimitedFileConnectionEClass.getESuperTypes().add(this.getFileConnection());
        positionalFileConnectionEClass.getESuperTypes().add(this.getFileConnection());
        ebcdicConnectionEClass.getESuperTypes().add(this.getFileConnection());
        mdmConnectionEClass.getESuperTypes().add(this.getConnection());
        databaseConnectionEClass.getESuperTypes().add(this.getConnection());
        sapConnectionEClass.getESuperTypes().add(this.getConnection());
        sapFunctionUnitEClass.getESuperTypes().add(this.getAbstractMetadataObject());
        sapiDocUnitEClass.getESuperTypes().add(this.getAbstractMetadataObject());
        sapFunctionParameterColumnEClass.getESuperTypes().add(this.getAbstractMetadataObject());
        sapFunctionParameterTableEClass.getESuperTypes().add(this.getAbstractMetadataObject());
        inputSAPFunctionParameterTableEClass.getESuperTypes().add(this.getSAPFunctionParameterTable());
        outputSAPFunctionParameterTableEClass.getESuperTypes().add(this.getSAPFunctionParameterTable());
        regexpFileConnectionEClass.getESuperTypes().add(this.getFileConnection());
        xmlFileConnectionEClass.getESuperTypes().add(this.getConnection());
        queryEClass.getESuperTypes().add(this.getAbstractMetadataObject());
        ldifFileConnectionEClass.getESuperTypes().add(this.getConnection());
        fileExcelConnectionEClass.getESuperTypes().add(this.getFileConnection());
        genericSchemaConnectionEClass.getESuperTypes().add(this.getConnection());
        ldapSchemaConnectionEClass.getESuperTypes().add(this.getConnection());
        wsdlSchemaConnectionEClass.getESuperTypes().add(this.getConnection());
        salesforceSchemaConnectionEClass.getESuperTypes().add(this.getConnection());
        cdcTypeEClass.getESuperTypes().add(this.getAbstractMetadataObject());
        subscriberTableEClass.getESuperTypes().add(theRelationalPackage_1.getTdTable());
        sapTestInputParameterTableEClass.getESuperTypes().add(this.getSAPFunctionParameterTable());
        conceptEClass.getESuperTypes().add(theRelationalPackage_1.getTdTable());
        hl7ConnectionEClass.getESuperTypes().add(this.getFileConnection());
        headerFooterConnectionEClass.getESuperTypes().add(this.getConnection());
        genericPackageEClass.getESuperTypes().add(theCorePackage.getPackage());
        ftpConnectionEClass.getESuperTypes().add(this.getConnection());
        brmsConnectionEClass.getESuperTypes().add(this.getConnection());
        validationRulesConnectionEClass.getESuperTypes().add(this.getConnection());
        edifactConnectionEClass.getESuperTypes().add(this.getConnection());
        edifactColumnEClass.getESuperTypes().add(this.getMetadataColumn());
        salesforceModuleUnitEClass.getESuperTypes().add(this.getAbstractMetadataObject());
        sapTableEClass.getESuperTypes().add(this.getMetadataTable());
        sapTableFieldEClass.getESuperTypes().add(this.getMetadataColumn());
        sapbwTableEClass.getESuperTypes().add(this.getSAPTable());
        sapbwTableFieldEClass.getESuperTypes().add(this.getSAPTableField());

        // Initialize classes and features; add operations and parameters
        initEClass(metadataEClass, Metadata.class, "Metadata", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getMetadata_Connections(), this.getConnection(), null, "connections", null, 0, -1, Metadata.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);

        initEClass(connectionEClass, Connection.class, "Connection", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getConnection_Version(), ecorePackage.getEString(), "version", null, 0, 1, Connection.class, !IS_TRANSIENT,
                !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getConnection_Queries(), this.getQueriesConnection(), this.getQueriesConnection_Connection(), "queries",
                null, 0, 1, Connection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConnection_ContextMode(), ecorePackage.getEBoolean(), "ContextMode", null, 0, 1, Connection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConnection_ContextId(), ecorePackage.getEString(), "ContextId", null, 0, 1, Connection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConnection_ContextName(), ecorePackage.getEString(), "contextName", null, 0, 1, Connection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        addEOperation(connectionEClass, theCorePackage.getString(), "getConnectionTypeName", 0, 1, IS_UNIQUE, IS_ORDERED);

        initEClass(metadataColumnEClass, MetadataColumn.class, "MetadataColumn", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getMetadataColumn_SourceType(), ecorePackage.getEString(), "sourceType", null, 0, 1, MetadataColumn.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMetadataColumn_DefaultValue(), ecorePackage.getEString(), "defaultValue", "", 0, 1,
                MetadataColumn.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getMetadataColumn_TalendType(), ecorePackage.getEString(), "talendType", null, 0, 1, MetadataColumn.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMetadataColumn_Key(), ecorePackage.getEBoolean(), "key", "false", 0, 1, MetadataColumn.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMetadataColumn_Nullable(), ecorePackage.getEBoolean(), "nullable", "true", 0, 1, MetadataColumn.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getMetadataColumn_Table(), this.getMetadataTable(), null, "table", null, 0, 1, MetadataColumn.class,
                IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
                IS_DERIVED, IS_ORDERED);
        initEAttribute(getMetadataColumn_OriginalField(), ecorePackage.getEString(), "originalField", "", 0, 1,
                MetadataColumn.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getMetadataColumn_Pattern(), ecorePackage.getEString(), "pattern", "", 0, 1, MetadataColumn.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMetadataColumn_DisplayField(), ecorePackage.getEString(), "displayField", null, 0, 1,
                MetadataColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getMetadataColumn_OriginalLength(), theCorePackage.getInteger(), "originalLength", null, 0, 1,
                MetadataColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getMetadataColumn_RelatedEntity(), ecorePackage.getEString(), "relatedEntity", "", 0, 1,
                MetadataColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getMetadataColumn_RelationshipType(), ecorePackage.getEString(), "relationshipType", "", 0, 1,
                MetadataColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);

        initEClass(abstractMetadataObjectEClass, AbstractMetadataObject.class, "AbstractMetadataObject", IS_ABSTRACT,
                !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAbstractMetadataObject_Properties(), this.getMap(), "properties", "", 1, 1,
                AbstractMetadataObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAbstractMetadataObject_Id(), ecorePackage.getEString(), "id", null, 0, 1, AbstractMetadataObject.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAbstractMetadataObject_Comment(), ecorePackage.getEString(), "comment", "", 0, 1,
                AbstractMetadataObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAbstractMetadataObject_Label(), ecorePackage.getEString(), "label", null, 0, 1,
                AbstractMetadataObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAbstractMetadataObject_ReadOnly(), ecorePackage.getEBoolean(), "readOnly", "false", 0, 1,
                AbstractMetadataObject.class, !IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAbstractMetadataObject_Synchronised(), ecorePackage.getEBoolean(), "synchronised", null, 0, 1,
                AbstractMetadataObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAbstractMetadataObject_Divergency(), ecorePackage.getEBoolean(), "divergency", null, 0, 1,
                AbstractMetadataObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);

        initEClass(metadataTableEClass, MetadataTable.class, "MetadataTable", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getMetadataTable_SourceName(), ecorePackage.getEString(), "sourceName", null, 0, 1, MetadataTable.class,
                IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMetadataTable_TableType(), ecorePackage.getEString(), "tableType", null, 0, 1, MetadataTable.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMetadataTable_AttachedCDC(), ecorePackage.getEBoolean(), "attachedCDC", null, 0, 1, MetadataTable.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMetadataTable_ActivatedCDC(), ecorePackage.getEBoolean(), "activatedCDC", null, 0, 1,
                MetadataTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEReference(getMetadataTable_Columns(), this.getMetadataColumn(), null, "columns", null, 0, -1, MetadataTable.class,
                IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
                IS_DERIVED, IS_ORDERED);
        initEReference(getMetadataTable_Connection(), this.getConnection(), null, "connection", null, 0, 1, MetadataTable.class,
                IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEReference(getMetadataTable_AdditionalProperties(), this.getAdditionalProperties(), null, "additionalProperties",
                null, 0, -1, MetadataTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(fileConnectionEClass, FileConnection.class, "FileConnection", IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getFileConnection_Server(), ecorePackage.getEString(), "Server", null, 1, 1, FileConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFileConnection_FilePath(), ecorePackage.getEString(), "FilePath", null, 1, 1, FileConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFileConnection_Format(), this.getFileFormat(), "Format", null, 1, 1, FileConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFileConnection_Encoding(), ecorePackage.getEString(), "Encoding", null, 1, 1, FileConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFileConnection_FieldSeparatorValue(), ecorePackage.getEString(), "FieldSeparatorValue", null, 1, 1,
                FileConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getFileConnection_RowSeparatorType(), this.getRowSeparator(), "RowSeparatorType", "Standart_EOL = 1", 1, 1,
                FileConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getFileConnection_RowSeparatorValue(), ecorePackage.getEString(), "RowSeparatorValue", null, 0, 1,
                FileConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getFileConnection_TextIdentifier(), ecorePackage.getEString(), "TextIdentifier", "", 0, 1,
                FileConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getFileConnection_UseHeader(), ecorePackage.getEBoolean(), "UseHeader", null, 0, 1, FileConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFileConnection_HeaderValue(), ecorePackage.getEString(), "HeaderValue", null, 0, 1,
                FileConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getFileConnection_UseFooter(), ecorePackage.getEBoolean(), "UseFooter", null, 0, 1, FileConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFileConnection_FooterValue(), ecorePackage.getEString(), "FooterValue", null, 0, 1,
                FileConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getFileConnection_UseLimit(), ecorePackage.getEBoolean(), "UseLimit", null, 0, 1, FileConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFileConnection_LimitValue(), ecorePackage.getEString(), "LimitValue", null, 0, 1, FileConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFileConnection_FirstLineCaption(), ecorePackage.getEBoolean(), "FirstLineCaption", null, 0, 1,
                FileConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getFileConnection_RemoveEmptyRow(), ecorePackage.getEBoolean(), "RemoveEmptyRow", null, 0, 1,
                FileConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getFileConnection_EscapeType(), this.getEscape(), "EscapeType", null, 1, 1, FileConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFileConnection_EscapeChar(), ecorePackage.getEString(), "EscapeChar", null, 0, 1, FileConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFileConnection_TextEnclosure(), ecorePackage.getEString(), "TextEnclosure", null, 0, 1,
                FileConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getFileConnection_CsvOption(), ecorePackage.getEBoolean(), "CsvOption", null, 0, 1, FileConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(delimitedFileConnectionEClass, DelimitedFileConnection.class, "DelimitedFileConnection", !IS_ABSTRACT,
                !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDelimitedFileConnection_FieldSeparatorType(), this.getFieldSeparator(), "FieldSeparatorType",
                "Semicolon", 1, 1, DelimitedFileConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
                !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDelimitedFileConnection_SplitRecord(), ecorePackage.getEBoolean(), "splitRecord", "false", 0, 1,
                DelimitedFileConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);

        initEClass(positionalFileConnectionEClass, PositionalFileConnection.class, "PositionalFileConnection", !IS_ABSTRACT,
                !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(ebcdicConnectionEClass, EbcdicConnection.class, "EbcdicConnection", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getEbcdicConnection_MidFile(), ecorePackage.getEString(), "MidFile", null, 0, 1, EbcdicConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getEbcdicConnection_DataFile(), ecorePackage.getEString(), "DataFile", null, 0, 1, EbcdicConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getEbcdicConnection_CodePage(), ecorePackage.getEString(), "CodePage", null, 0, 1, EbcdicConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getEbcdicConnection_SourceFileStart(), ecorePackage.getEString(), "SourceFileStart", "6", 0, 1,
                EbcdicConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getEbcdicConnection_SourceFileEnd(), ecorePackage.getEString(), "SourceFileEnd", "72", 0, 1,
                EbcdicConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);

        initEClass(mdmConnectionEClass, MDMConnection.class, "MDMConnection", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getMDMConnection_Username(), ecorePackage.getEString(), "Username", null, 0, 1, MDMConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMDMConnection_Password(), ecorePackage.getEString(), "Password", null, 0, 1, MDMConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMDMConnection_Port(), ecorePackage.getEString(), "Port", null, 0, 1, MDMConnection.class, !IS_TRANSIENT,
                !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMDMConnection_Server(), ecorePackage.getEString(), "Server", null, 0, 1, MDMConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMDMConnection_Universe(), ecorePackage.getEString(), "Universe", null, 0, 1, MDMConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMDMConnection_Datamodel(), ecorePackage.getEString(), "Datamodel", null, 0, 1, MDMConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMDMConnection_Datacluster(), ecorePackage.getEString(), "Datacluster", null, 0, 1, MDMConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getMDMConnection_Schemas(), this.getConcept(), null, "schemas", null, 0, -1, MDMConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMDMConnection_Protocol(), this.getMDMConnectionProtocol(), "protocol", "http", 1, 1,
                MDMConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getMDMConnection_Context(), theCorePackage.getString(), "context", "talend/TalendPort", 1, 1,
                MDMConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getMDMConnection_ServerUrl(), ecorePackage.getEString(), "serverUrl", null, 1, 1, MDMConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        addEOperation(mdmConnectionEClass, theCorePackage.getString(), "getConnectionString", 0, 1, IS_UNIQUE, IS_ORDERED);

        initEClass(databaseConnectionEClass, DatabaseConnection.class, "DatabaseConnection", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDatabaseConnection_DatabaseType(), ecorePackage.getEString(), "DatabaseType", null, 0, 1,
                DatabaseConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDatabaseConnection_DriverJarPath(), ecorePackage.getEString(), "DriverJarPath", null, 0, 1,
                DatabaseConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDatabaseConnection_DriverClass(), ecorePackage.getEString(), "DriverClass", null, 0, 1,
                DatabaseConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDatabaseConnection_URL(), ecorePackage.getEString(), "URL", null, 0, 1, DatabaseConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDatabaseConnection_DbVersionString(), ecorePackage.getEString(), "dbVersionString", null, 0, 1,
                DatabaseConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDatabaseConnection_Port(), ecorePackage.getEString(), "Port", null, 0, 1, DatabaseConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDatabaseConnection_Username(), ecorePackage.getEString(), "Username", null, 0, 1,
                DatabaseConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDatabaseConnection_Password(), ecorePackage.getEString(), "Password", null, 0, 1,
                DatabaseConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDatabaseConnection_ServerName(), ecorePackage.getEString(), "ServerName", null, 0, 1,
                DatabaseConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDatabaseConnection_DatasourceName(), ecorePackage.getEString(), "DatasourceName", null, 0, 1,
                DatabaseConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDatabaseConnection_FileFieldName(), ecorePackage.getEString(), "FileFieldName", null, 0, 1,
                DatabaseConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDatabaseConnection_SID(), ecorePackage.getEString(), "SID", null, 0, 1, DatabaseConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDatabaseConnection_SqlSynthax(), ecorePackage.getEString(), "SqlSynthax", null, 0, 1,
                DatabaseConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDatabaseConnection_StringQuote(), ecorePackage.getEString(), "StringQuote", "\"", 0, 1,
                DatabaseConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDatabaseConnection_NullChar(), ecorePackage.getEString(), "NullChar", "000", 0, 1,
                DatabaseConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDatabaseConnection_DbmsId(), ecorePackage.getEString(), "DbmsId", null, 0, 1, DatabaseConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDatabaseConnection_ProductId(), ecorePackage.getEString(), "ProductId", null, 0, 1,
                DatabaseConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDatabaseConnection_DBRootPath(), ecorePackage.getEString(), "DBRootPath", null, 0, 1,
                DatabaseConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDatabaseConnection_AdditionalParams(), ecorePackage.getEString(), "AdditionalParams", null, 0, 1,
                DatabaseConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDatabaseConnection_StandardSQL(), ecorePackage.getEBoolean(), "StandardSQL", null, 0, 1,
                DatabaseConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDatabaseConnection_SystemSQL(), ecorePackage.getEBoolean(), "SystemSQL", null, 0, 1,
                DatabaseConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEReference(getDatabaseConnection_CdcConns(), this.getCDCConnection(), this.getCDCConnection_Connection(), "cdcConns",
                null, 0, 1, DatabaseConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDatabaseConnection_CdcTypeMode(), ecorePackage.getEString(), "cdcTypeMode", null, 0, 1,
                DatabaseConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDatabaseConnection_SQLMode(), ecorePackage.getEBoolean(), "SQLMode", "true", 0, 1,
                DatabaseConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDatabaseConnection_UiSchema(), ecorePackage.getEString(), "UiSchema", null, 0, 1,
                DatabaseConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEReference(getDatabaseConnection_Parameters(), this.getAdditionalProperties(), null, "parameters", null, 0, -1,
                DatabaseConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(sapConnectionEClass, SAPConnection.class, "SAPConnection", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getSAPConnection_Host(), ecorePackage.getEString(), "Host", null, 0, 1, SAPConnection.class, !IS_TRANSIENT,
                !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSAPConnection_Username(), ecorePackage.getEString(), "Username", null, 0, 1, SAPConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSAPConnection_Password(), ecorePackage.getEString(), "Password", null, 0, 1, SAPConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSAPConnection_Client(), ecorePackage.getEString(), "Client", null, 0, 1, SAPConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSAPConnection_SystemNumber(), ecorePackage.getEString(), "SystemNumber", null, 0, 1,
                SAPConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getSAPConnection_Language(), ecorePackage.getEString(), "Language", null, 0, 1, SAPConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSAPConnection_Funtions(), this.getSAPFunctionUnit(), null, "Funtions", null, 0, -1, SAPConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSAPConnection_CurrentFucntion(), ecorePackage.getEString(), "currentFucntion", null, 0, 1,
                SAPConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEReference(getSAPConnection_IDocs(), this.getSAPIDocUnit(), this.getSAPIDocUnit_Connection(), "IDocs", null, 0, -1,
                SAPConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE,
                IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSAPConnection_JcoVersion(), ecorePackage.getEString(), "jcoVersion", null, 0, 1, SAPConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSAPConnection_AdditionalProperties(), this.getAdditionalConnectionProperty(), null,
                "additionalProperties", null, 0, -1, SAPConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSAPConnection_BWDataSources(), this.getSAPBWTable(), null, "BWDataSources", null, 0, -1,
                SAPConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE,
                IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSAPConnection_BWDataStoreObjects(), this.getSAPBWTable(), null, "BWDataStoreObjects", null, 0, -1,
                SAPConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE,
                IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSAPConnection_BWInfoCubes(), this.getSAPBWTable(), null, "BWInfoCubes", null, 0, -1,
                SAPConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE,
                IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSAPConnection_BWInfoObjects(), this.getSAPBWTable(), null, "BWInfoObjects", null, 0, -1,
                SAPConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE,
                IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(sapFunctionUnitEClass, SAPFunctionUnit.class, "SAPFunctionUnit", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getSAPFunctionUnit_OutputType(), ecorePackage.getEString(), "OutputType", null, 0, 1,
                SAPFunctionUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getSAPFunctionUnit_OutputTableName(), ecorePackage.getEString(), "OutputTableName", null, 0, 1,
                SAPFunctionUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEReference(getSAPFunctionUnit_InputParameterTable(), this.getInputSAPFunctionParameterTable(),
                this.getInputSAPFunctionParameterTable_FunctionUnit(), "InputParameterTable", null, 0, 1, SAPFunctionUnit.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEReference(getSAPFunctionUnit_OutputParameterTable(), this.getOutputSAPFunctionParameterTable(),
                this.getOutputSAPFunctionParameterTable_FunctionUnit(), "OutputParameterTable", null, 0, 1, SAPFunctionUnit.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEReference(getSAPFunctionUnit_MetadataTable(), this.getMetadataTable(), null, "MetadataTable", null, 0, 1,
                SAPFunctionUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSAPFunctionUnit_Connection(), this.getSAPConnection(), null, "connection", null, 0, 1,
                SAPFunctionUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSAPFunctionUnit_Tables(), this.getMetadataTable(), null, "tables", null, 0, -1, SAPFunctionUnit.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEReference(getSAPFunctionUnit_InputTables(), this.getMetadataTable(), null, "inputTables", null, 0, -1,
                SAPFunctionUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSAPFunctionUnit_TestInputParameterTable(), this.getSAPTestInputParameterTable(),
                this.getSAPTestInputParameterTable_FunctionUnit(), "TestInputParameterTable", null, 0, 1, SAPFunctionUnit.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEReference(getSAPFunctionUnit_ParamData(), this.getSAPFunctionParamData(), null, "paramData", null, 0, 1,
                SAPFunctionUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSAPFunctionUnit_AsXmlSchema(), ecorePackage.getEBoolean(), "asXmlSchema", null, 0, 1,
                SAPFunctionUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);

        EOperation op = addEOperation(sapFunctionUnitEClass, null, "setDocument", 0, 1, IS_UNIQUE, IS_ORDERED);
        addEParameter(op, theCorePackage.getString(), "document", 0, 1, IS_UNIQUE, IS_ORDERED);

        initEClass(sapiDocUnitEClass, SAPIDocUnit.class, "SAPIDocUnit", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSAPIDocUnit_Connection(), this.getSAPConnection(), this.getSAPConnection_IDocs(), "connection", null, 0,
                1, SAPIDocUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSAPIDocUnit_ProgramId(), ecorePackage.getEString(), "programId", null, 0, 1, SAPIDocUnit.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSAPIDocUnit_GatewayService(), ecorePackage.getEString(), "gatewayService", null, 0, 1,
                SAPIDocUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getSAPIDocUnit_UseXmlOutput(), ecorePackage.getEBoolean(), "useXmlOutput", null, 0, 1, SAPIDocUnit.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSAPIDocUnit_XmlFile(), ecorePackage.getEString(), "xmlFile", null, 0, 1, SAPIDocUnit.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSAPIDocUnit_UseHtmlOutput(), ecorePackage.getEBoolean(), "useHtmlOutput", null, 0, 1, SAPIDocUnit.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSAPIDocUnit_HtmlFile(), ecorePackage.getEString(), "htmlFile", null, 0, 1, SAPIDocUnit.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(sapFunctionParameterColumnEClass, SAPFunctionParameterColumn.class, "SAPFunctionParameterColumn", !IS_ABSTRACT,
                !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getSAPFunctionParameterColumn_ParameterType(), ecorePackage.getEString(), "ParameterType", null, 0, 1,
                SAPFunctionParameterColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSAPFunctionParameterColumn_StructureOrTableName(), ecorePackage.getEString(), "StructureOrTableName",
                null, 0, 1, SAPFunctionParameterColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSAPFunctionParameterColumn_DataType(), ecorePackage.getEString(), "DataType", null, 0, 1,
                SAPFunctionParameterColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSAPFunctionParameterColumn_Length(), ecorePackage.getEString(), "Length", null, 0, 1,
                SAPFunctionParameterColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSAPFunctionParameterColumn_Value(), ecorePackage.getEString(), "Value", null, 0, 1,
                SAPFunctionParameterColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEReference(getSAPFunctionParameterColumn_ParameterTable(), this.getSAPFunctionParameterTable(),
                this.getSAPFunctionParameterTable_Columns(), "ParameterTable", null, 0, 1, SAPFunctionParameterColumn.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);

        op = addEOperation(sapFunctionParameterColumnEClass, null, "setDescription", 0, 1, IS_UNIQUE, IS_ORDERED);
        addEParameter(op, theCorePackage.getString(), "description", 0, 1, IS_UNIQUE, IS_ORDERED);

        initEClass(sapFunctionParameterTableEClass, SAPFunctionParameterTable.class, "SAPFunctionParameterTable", !IS_ABSTRACT,
                !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSAPFunctionParameterTable_Columns(), this.getSAPFunctionParameterColumn(),
                this.getSAPFunctionParameterColumn_ParameterTable(), "columns", null, 0, -1, SAPFunctionParameterTable.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);

        initEClass(inputSAPFunctionParameterTableEClass, InputSAPFunctionParameterTable.class, "InputSAPFunctionParameterTable",
                !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getInputSAPFunctionParameterTable_FunctionUnit(), this.getSAPFunctionUnit(),
                this.getSAPFunctionUnit_InputParameterTable(), "functionUnit", null, 0, 1, InputSAPFunctionParameterTable.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);

        initEClass(outputSAPFunctionParameterTableEClass, OutputSAPFunctionParameterTable.class,
                "OutputSAPFunctionParameterTable", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getOutputSAPFunctionParameterTable_FunctionUnit(), this.getSAPFunctionUnit(),
                this.getSAPFunctionUnit_OutputParameterTable(), "functionUnit", null, 0, 1, OutputSAPFunctionParameterTable.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);

        initEClass(regexpFileConnectionEClass, RegexpFileConnection.class, "RegexpFileConnection", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getRegexpFileConnection_FieldSeparatorType(), this.getFieldSeparator(), "FieldSeparatorType", null, 1, 1,
                RegexpFileConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);

        initEClass(xmlFileConnectionEClass, XmlFileConnection.class, "XmlFileConnection", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getXmlFileConnection_XsdFilePath(), ecorePackage.getEString(), "XsdFilePath", null, 0, 1,
                XmlFileConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getXmlFileConnection_XmlFilePath(), ecorePackage.getEString(), "XmlFilePath", null, 0, 1,
                XmlFileConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getXmlFileConnection_Guess(), ecorePackage.getEBoolean(), "Guess", null, 0, 1, XmlFileConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getXmlFileConnection_MaskXPattern(), ecorePackage.getEString(), "MaskXPattern", null, 0, 1,
                XmlFileConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEReference(getXmlFileConnection_Schema(), this.getXmlXPathLoopDescriptor(),
                this.getXmlXPathLoopDescriptor_Connection(), "schema", null, 0, -1, XmlFileConnection.class, !IS_TRANSIENT,
                !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getXmlFileConnection_Encoding(), ecorePackage.getEString(), "Encoding", null, 0, 1,
                XmlFileConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEReference(getXmlFileConnection_Group(), this.getXMLFileNode(), null, "group", null, 0, -1, XmlFileConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEReference(getXmlFileConnection_Root(), this.getXMLFileNode(), null, "root", null, 0, -1, XmlFileConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
                !IS_DERIVED, !IS_ORDERED);
        initEReference(getXmlFileConnection_Loop(), this.getXMLFileNode(), null, "loop", null, 0, -1, XmlFileConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getXmlFileConnection_InputModel(), ecorePackage.getEBoolean(), "inputModel", "true", 0, 1,
                XmlFileConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getXmlFileConnection_OutputFilePath(), ecorePackage.getEString(), "outputFilePath", null, 0, 1,
                XmlFileConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getXmlFileConnection_FileContent(), ecorePackage.getEByteArray(), "fileContent", null, 0, 1,
                XmlFileConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getXmlFileConnection_TargetNameSpace(), ecorePackage.getEString(), "targetNameSpace", null, 0, 1,
                XmlFileConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);

        initEClass(schemaTargetEClass, SchemaTarget.class, "SchemaTarget", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getSchemaTarget_RelativeXPathQuery(), ecorePackage.getEString(), "RelativeXPathQuery", null, 0, 1,
                SchemaTarget.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getSchemaTarget_TagName(), ecorePackage.getEString(), "TagName", null, 0, 1, SchemaTarget.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSchemaTarget_Schema(), this.getXmlXPathLoopDescriptor(), this.getXmlXPathLoopDescriptor_SchemaTargets(),
                "schema", null, 0, 1, SchemaTarget.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(queriesConnectionEClass, QueriesConnection.class, "QueriesConnection", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEReference(getQueriesConnection_Connection(), this.getConnection(), this.getConnection_Queries(), "connection", null,
                0, 1, QueriesConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getQueriesConnection_Query(), this.getQuery(), this.getQuery_Queries(), "query", null, 0, -1,
                QueriesConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(queryEClass, Query.class, "Query", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getQuery_Value(), ecorePackage.getEString(), "value", null, 0, 1, Query.class, !IS_TRANSIENT, !IS_VOLATILE,
                IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getQuery_Queries(), this.getQueriesConnection(), this.getQueriesConnection_Query(), "queries", null, 0, 1,
                Query.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE,
                IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getQuery_ContextMode(), ecorePackage.getEBoolean(), "contextMode", null, 0, 1, Query.class, !IS_TRANSIENT,
                !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(ldifFileConnectionEClass, LdifFileConnection.class, "LdifFileConnection", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getLdifFileConnection_Value(), ecorePackage.getEString(), "value", null, 0, -1, LdifFileConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLdifFileConnection_FilePath(), ecorePackage.getEString(), "FilePath", null, 1, 1,
                LdifFileConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLdifFileConnection_LimitEntry(), ecorePackage.getEInt(), "LimitEntry", null, 0, 1,
                LdifFileConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLdifFileConnection_UseLimit(), ecorePackage.getEBoolean(), "UseLimit", null, 0, 1,
                LdifFileConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLdifFileConnection_Server(), ecorePackage.getEString(), "Server", null, 1, 1, LdifFileConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(fileExcelConnectionEClass, FileExcelConnection.class, "FileExcelConnection", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getFileExcelConnection_SheetName(), ecorePackage.getEString(), "SheetName", null, 1, 1,
                FileExcelConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFileExcelConnection_SheetColumns(), ecorePackage.getEString(), "sheetColumns", null, 0, -1,
                FileExcelConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFileExcelConnection_FirstColumn(), ecorePackage.getEString(), "firstColumn", null, 0, 1,
                FileExcelConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFileExcelConnection_LastColumn(), ecorePackage.getEString(), "lastColumn", null, 0, 1,
                FileExcelConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFileExcelConnection_ThousandSeparator(), ecorePackage.getEString(), "thousandSeparator", null, 0, 1,
                FileExcelConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFileExcelConnection_DecimalSeparator(), ecorePackage.getEString(), "decimalSeparator", null, 0, 1,
                FileExcelConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFileExcelConnection_AdvancedSpearator(), ecorePackage.getEBoolean(), "advancedSpearator", null, 0, 1,
                FileExcelConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFileExcelConnection_SelectAllSheets(), ecorePackage.getEBoolean(), "selectAllSheets", null, 0, 1,
                FileExcelConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFileExcelConnection_SheetList(), this.getList(), "sheetList", null, 0, 1, FileExcelConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFileExcelConnection_GenerationMode(), ecorePackage.getEString(), "generationMode", null, 0, 1,
                FileExcelConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);

        initEClass(xmlXPathLoopDescriptorEClass, XmlXPathLoopDescriptor.class, "XmlXPathLoopDescriptor", !IS_ABSTRACT,
                !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getXmlXPathLoopDescriptor_LimitBoucle(), ecorePackage.getEIntegerObject(), "LimitBoucle", null, 0, 1,
                XmlXPathLoopDescriptor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getXmlXPathLoopDescriptor_AbsoluteXPathQuery(), ecorePackage.getEString(), "AbsoluteXPathQuery", null, 0,
                1, XmlXPathLoopDescriptor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEReference(getXmlXPathLoopDescriptor_Connection(), this.getXmlFileConnection(), this.getXmlFileConnection_Schema(),
                "connection", null, 0, 1, XmlXPathLoopDescriptor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getXmlXPathLoopDescriptor_SchemaTargets(), this.getSchemaTarget(), this.getSchemaTarget_Schema(),
                "schemaTargets", null, 0, -1, XmlXPathLoopDescriptor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(genericSchemaConnectionEClass, GenericSchemaConnection.class, "GenericSchemaConnection", !IS_ABSTRACT,
                !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getGenericSchemaConnection_MappingTypeUsed(), ecorePackage.getEBoolean(), "mappingTypeUsed", null, 0, 1,
                GenericSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGenericSchemaConnection_MappingTypeId(), ecorePackage.getEString(), "mappingTypeId", null, 0, 1,
                GenericSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);

        initEClass(ldapSchemaConnectionEClass, LDAPSchemaConnection.class, "LDAPSchemaConnection", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getLDAPSchemaConnection_Host(), ecorePackage.getEString(), "Host", null, 0, 1, LDAPSchemaConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLDAPSchemaConnection_Port(), ecorePackage.getEString(), "Port", null, 0, 1, LDAPSchemaConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLDAPSchemaConnection_Protocol(), ecorePackage.getEString(), "Protocol", null, 0, 1,
                LDAPSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLDAPSchemaConnection_Filter(), ecorePackage.getEString(), "Filter", null, 0, 1,
                LDAPSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLDAPSchemaConnection_Separator(), ecorePackage.getEString(), "Separator", null, 0, 1,
                LDAPSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLDAPSchemaConnection_UseAdvanced(), ecorePackage.getEBoolean(), "UseAdvanced", null, 0, 1,
                LDAPSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLDAPSchemaConnection_StorePath(), ecorePackage.getEString(), "StorePath", null, 0, 1,
                LDAPSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLDAPSchemaConnection_UseLimit(), ecorePackage.getEBoolean(), "UseLimit", null, 0, 1,
                LDAPSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLDAPSchemaConnection_UseAuthen(), ecorePackage.getEBoolean(), "UseAuthen", null, 0, 1,
                LDAPSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLDAPSchemaConnection_BindPrincipal(), ecorePackage.getEString(), "BindPrincipal", null, 0, 1,
                LDAPSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLDAPSchemaConnection_BindPassword(), ecorePackage.getEString(), "BindPassword", null, 0, 1,
                LDAPSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLDAPSchemaConnection_LimitValue(), ecorePackage.getEInt(), "LimitValue", null, 0, 1,
                LDAPSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLDAPSchemaConnection_EncryptionMethodName(), ecorePackage.getEString(), "EncryptionMethodName", null, 0,
                1, LDAPSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLDAPSchemaConnection_Value(), ecorePackage.getEString(), "Value", null, 0, -1,
                LDAPSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLDAPSchemaConnection_SavePassword(), ecorePackage.getEBoolean(), "SavePassword", null, 0, 1,
                LDAPSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLDAPSchemaConnection_Aliases(), ecorePackage.getEString(), "Aliases", null, 0, 1,
                LDAPSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLDAPSchemaConnection_Referrals(), ecorePackage.getEString(), "Referrals", null, 0, 1,
                LDAPSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLDAPSchemaConnection_CountLimit(), ecorePackage.getEString(), "CountLimit", null, 0, 1,
                LDAPSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLDAPSchemaConnection_TimeOutLimit(), ecorePackage.getEString(), "TimeOutLimit", null, 0, 1,
                LDAPSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLDAPSchemaConnection_BaseDNs(), ecorePackage.getEString(), "BaseDNs", null, 0, -1,
                LDAPSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLDAPSchemaConnection_GetBaseDNsFromRoot(), ecorePackage.getEBoolean(), "GetBaseDNsFromRoot", null, 0, 1,
                LDAPSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLDAPSchemaConnection_ReturnAttributes(), ecorePackage.getEString(), "ReturnAttributes", null, 0, -1,
                LDAPSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLDAPSchemaConnection_SelectedDN(), ecorePackage.getEString(), "SelectedDN", null, 0, 1,
                LDAPSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);

        initEClass(wsdlSchemaConnectionEClass, WSDLSchemaConnection.class, "WSDLSchemaConnection", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getWSDLSchemaConnection_WSDL(), ecorePackage.getEString(), "WSDL", null, 0, 1, WSDLSchemaConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDLSchemaConnection_NeedAuth(), ecorePackage.getEBoolean(), "needAuth", null, 0, 1,
                WSDLSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDLSchemaConnection_MethodName(), ecorePackage.getEString(), "methodName", null, 0, 1,
                WSDLSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDLSchemaConnection_Parameters(), this.getList(), "parameters", null, 0, 1, WSDLSchemaConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDLSchemaConnection_UserName(), ecorePackage.getEString(), "UserName", null, 0, 1,
                WSDLSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDLSchemaConnection_Password(), ecorePackage.getEString(), "Password", null, 0, 1,
                WSDLSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDLSchemaConnection_UseProxy(), ecorePackage.getEBoolean(), "useProxy", null, 0, 1,
                WSDLSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDLSchemaConnection_ProxyHost(), ecorePackage.getEString(), "proxyHost", null, 0, 1,
                WSDLSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDLSchemaConnection_ProxyPort(), ecorePackage.getEString(), "proxyPort", null, 0, 1,
                WSDLSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDLSchemaConnection_ProxyUser(), ecorePackage.getEString(), "proxyUser", null, 0, 1,
                WSDLSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDLSchemaConnection_ProxyPassword(), ecorePackage.getEString(), "proxyPassword", null, 0, 1,
                WSDLSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDLSchemaConnection_Value(), ecorePackage.getEString(), "Value", null, 0, -1,
                WSDLSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDLSchemaConnection_EndpointURI(), ecorePackage.getEString(), "EndpointURI", null, 0, 1,
                WSDLSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDLSchemaConnection_Encoding(), ecorePackage.getEString(), "Encoding", null, 0, 1,
                WSDLSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDLSchemaConnection_TimeOut(), ecorePackage.getEInt(), "timeOut", null, 0, 1,
                WSDLSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDLSchemaConnection_IsInputModel(), ecorePackage.getEBoolean(), "isInputModel", "true", 0, 1,
                WSDLSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDLSchemaConnection_ServerNameSpace(), ecorePackage.getEString(), "serverNameSpace", "", 0, 1,
                WSDLSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDLSchemaConnection_ServerName(), ecorePackage.getEString(), "serverName", null, 0, 1,
                WSDLSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDLSchemaConnection_PortNameSpace(), ecorePackage.getEString(), "portNameSpace", null, 0, 1,
                WSDLSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDLSchemaConnection_PortName(), ecorePackage.getEString(), "portName", null, 0, 1,
                WSDLSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEReference(getWSDLSchemaConnection_ParameterValue(), this.getWSDLParameter(), null, "parameterValue", null, 0, -1,
                WSDLSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getWSDLSchemaConnection_OutputParameter(), this.getWSDLParameter(), null, "outputParameter", null, 0, -1,
                WSDLSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(salesforceSchemaConnectionEClass, SalesforceSchemaConnection.class, "SalesforceSchemaConnection", !IS_ABSTRACT,
                !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getSalesforceSchemaConnection_WebServiceUrl(), ecorePackage.getEString(), "webServiceUrl", null, 0, 1,
                SalesforceSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSalesforceSchemaConnection_UserName(), ecorePackage.getEString(), "userName", null, 0, 1,
                SalesforceSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSalesforceSchemaConnection_Password(), ecorePackage.getEString(), "password", null, 0, 1,
                SalesforceSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSalesforceSchemaConnection_ModuleName(), ecorePackage.getEString(), "moduleName", null, 0, 1,
                SalesforceSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSalesforceSchemaConnection_QueryCondition(), ecorePackage.getEString(), "queryCondition", null, 0, 1,
                SalesforceSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSalesforceSchemaConnection_UseCustomModuleName(), ecorePackage.getEBoolean(), "useCustomModuleName",
                null, 0, 1, SalesforceSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSalesforceSchemaConnection_UseProxy(), ecorePackage.getEBoolean(), "useProxy", null, 0, 1,
                SalesforceSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSalesforceSchemaConnection_ProxyHost(), ecorePackage.getEString(), "proxyHost", null, 0, 1,
                SalesforceSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSalesforceSchemaConnection_ProxyPort(), ecorePackage.getEString(), "proxyPort", null, 0, 1,
                SalesforceSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSalesforceSchemaConnection_ProxyUsername(), ecorePackage.getEString(), "proxyUsername", null, 0, 1,
                SalesforceSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSalesforceSchemaConnection_ProxyPassword(), ecorePackage.getEString(), "proxyPassword", null, 0, 1,
                SalesforceSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSalesforceSchemaConnection_BatchSize(), ecorePackage.getEString(), "batchSize", null, 0, 1,
                SalesforceSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSalesforceSchemaConnection_UseHttpProxy(), ecorePackage.getEBoolean(), "useHttpProxy", null, 0, 1,
                SalesforceSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSalesforceSchemaConnection_UseAlphbet(), ecorePackage.getEBoolean(), "useAlphbet", "true", 0, 1,
                SalesforceSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSalesforceSchemaConnection_TimeOut(), ecorePackage.getEString(), "timeOut", null, 0, 1,
                SalesforceSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEReference(getSalesforceSchemaConnection_Modules(), this.getSalesforceModuleUnit(),
                this.getSalesforceModuleUnit_Connection(), "modules", null, 0, -1, SalesforceSchemaConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSalesforceSchemaConnection_WebServiceUrlTextForOAuth(), ecorePackage.getEString(),
                "webServiceUrlTextForOAuth", null, 0, 1, SalesforceSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE,
                IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSalesforceSchemaConnection_ConsumeKey(), ecorePackage.getEString(), "consumeKey", null, 0, 1,
                SalesforceSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSalesforceSchemaConnection_ConsumeSecret(), ecorePackage.getEString(), "consumeSecret", null, 0, 1,
                SalesforceSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSalesforceSchemaConnection_CallbackHost(), ecorePackage.getEString(), "callbackHost", null, 0, 1,
                SalesforceSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSalesforceSchemaConnection_CallbackPort(), ecorePackage.getEString(), "callbackPort", null, 0, 1,
                SalesforceSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSalesforceSchemaConnection_SalesforceVersion(), ecorePackage.getEString(), "salesforceVersion", null, 0,
                1, SalesforceSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSalesforceSchemaConnection_Token(), ecorePackage.getEString(), "token", null, 0, 1,
                SalesforceSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSalesforceSchemaConnection_LoginType(), ecorePackage.getEString(), "loginType", null, 0, 1,
                SalesforceSchemaConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);

        initEClass(cdcConnectionEClass, CDCConnection.class, "CDCConnection", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCDCConnection_Connection(), this.getDatabaseConnection(), this.getDatabaseConnection_CdcConns(),
                "connection", null, 0, 1, CDCConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCDCConnection_CdcTypes(), this.getCDCType(), null, "cdcTypes", null, 0, -1, CDCConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);

        initEClass(cdcTypeEClass, CDCType.class, "CDCType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getCDCType_LinkDB(), ecorePackage.getEString(), "linkDB", null, 0, 1, CDCType.class, !IS_TRANSIENT,
                !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCDCType_Subscribers(), this.getSubscriberTable(), null, "subscribers", null, 0, -1, CDCType.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEReference(getCDCType_CdcConnection(), this.getCDCConnection(), null, "cdcConnection", null, 0, 1, CDCType.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCDCType_JournalName(), ecorePackage.getEString(), "journalName", null, 0, 1, CDCType.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(subscriberTableEClass, SubscriberTable.class, "SubscriberTable", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getSubscriberTable_System(), ecorePackage.getEBoolean(), "system", null, 0, 1, SubscriberTable.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(sapTestInputParameterTableEClass, SAPTestInputParameterTable.class, "SAPTestInputParameterTable", !IS_ABSTRACT,
                !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSAPTestInputParameterTable_FunctionUnit(), this.getSAPFunctionUnit(),
                this.getSAPFunctionUnit_TestInputParameterTable(), "functionUnit", null, 0, 1, SAPTestInputParameterTable.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);

        initEClass(conceptEClass, Concept.class, "Concept", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getConcept_LoopExpression(), ecorePackage.getEString(), "LoopExpression", null, 0, 1, Concept.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConcept_LoopLimit(), ecorePackage.getEIntegerObject(), "LoopLimit", null, 0, 1, Concept.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getConcept_ConceptTargets(), this.getConceptTarget(), this.getConceptTarget_Schema(), "conceptTargets",
                null, 0, -1, Concept.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConcept_InputModel(), ecorePackage.getEBoolean(), "inputModel", "true", 0, 1, Concept.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getConcept_Group(), this.getXMLFileNode(), null, "group", null, 0, -1, Concept.class, !IS_TRANSIENT,
                !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEReference(getConcept_Root(), this.getXMLFileNode(), null, "root", null, 0, -1, Concept.class, !IS_TRANSIENT,
                !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
                !IS_ORDERED);
        initEReference(getConcept_Loop(), this.getXMLFileNode(), null, "loop", null, 0, -1, Concept.class, !IS_TRANSIENT,
                !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getConcept_ConceptType(), this.getMdmConceptType(), "conceptType", "INPUT", 1, 1, Concept.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConcept_XPathPrefix(), ecorePackage.getEString(), "xPathPrefix", null, 0, 1, Concept.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(conceptTargetEClass, ConceptTarget.class, "ConceptTarget", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEReference(getConceptTarget_Schema(), this.getConcept(), this.getConcept_ConceptTargets(), "schema", null, 0, 1,
                ConceptTarget.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConceptTarget_TargetName(), ecorePackage.getEString(), "targetName", null, 0, 1, ConceptTarget.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConceptTarget_RelativeLoopExpression(), ecorePackage.getEString(), "RelativeLoopExpression", null, 0, 1,
                ConceptTarget.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);

        initEClass(hl7ConnectionEClass, HL7Connection.class, "HL7Connection", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getHL7Connection_StartChar(), ecorePackage.getEString(), "StartChar", null, 0, 1, HL7Connection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHL7Connection_EndChar(), ecorePackage.getEString(), "EndChar", null, 0, 1, HL7Connection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getHL7Connection_Root(), this.getHL7FileNode(), null, "root", null, 0, -1, HL7Connection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
                !IS_DERIVED, !IS_ORDERED);
        initEAttribute(getHL7Connection_OutputFilePath(), ecorePackage.getEString(), "outputFilePath", null, 0, 1,
                HL7Connection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);

        initEClass(headerFooterConnectionEClass, HeaderFooterConnection.class, "HeaderFooterConnection", !IS_ABSTRACT,
                !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getHeaderFooterConnection_IsHeader(), ecorePackage.getEBoolean(), "isHeader", null, 0, 1,
                HeaderFooterConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHeaderFooterConnection_Imports(), ecorePackage.getEString(), "imports", null, 0, 1,
                HeaderFooterConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHeaderFooterConnection_MainCode(), ecorePackage.getEString(), "mainCode", null, 0, 1,
                HeaderFooterConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHeaderFooterConnection_Libraries(), ecorePackage.getEString(), "libraries", null, 0, 1,
                HeaderFooterConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);

        initEClass(xmlFileNodeEClass, XMLFileNode.class, "XMLFileNode", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getXMLFileNode_XMLPath(), ecorePackage.getEString(), "XMLPath", null, 0, 1, XMLFileNode.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getXMLFileNode_RelatedColumn(), ecorePackage.getEString(), "RelatedColumn", null, 0, 1, XMLFileNode.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getXMLFileNode_DefaultValue(), ecorePackage.getEString(), "DefaultValue", null, 0, 1, XMLFileNode.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getXMLFileNode_Attribute(), ecorePackage.getEString(), "Attribute", null, 0, 1, XMLFileNode.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getXMLFileNode_Order(), ecorePackage.getEInt(), "Order", null, 0, 1, XMLFileNode.class, !IS_TRANSIENT,
                !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getXMLFileNode_Type(), ecorePackage.getEString(), "Type", null, 0, 1, XMLFileNode.class, !IS_TRANSIENT,
                !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(wsdlParameterEClass, WSDLParameter.class, "WSDLParameter", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getWSDLParameter_Element(), ecorePackage.getEString(), "Element", null, 0, 1, WSDLParameter.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDLParameter_Source(), ecorePackage.getEString(), "source", null, 0, 1, WSDLParameter.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDLParameter_Column(), ecorePackage.getEString(), "Column", null, 0, 1, WSDLParameter.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEAttribute(getWSDLParameter_Expression(), ecorePackage.getEString(), "Expression", null, 0, 1, WSDLParameter.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDLParameter_ParameterInfo(), ecorePackage.getEString(), "ParameterInfo", null, 0, 1,
                WSDLParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getWSDLParameter_ParameterInfoParent(), ecorePackage.getEString(), "ParameterInfoParent", null, 0, 1,
                WSDLParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);

        initEClass(genericPackageEClass, GenericPackage.class, "GenericPackage", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);

        initEClass(hl7FileNodeEClass, HL7FileNode.class, "HL7FileNode", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getHL7FileNode_FilePath(), ecorePackage.getEString(), "FilePath", null, 0, 1, HL7FileNode.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHL7FileNode_Order(), ecorePackage.getEInt(), "Order", null, 0, 1, HL7FileNode.class, !IS_TRANSIENT,
                !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHL7FileNode_Attribute(), ecorePackage.getEString(), "Attribute", null, 0, 1, HL7FileNode.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHL7FileNode_DefaultValue(), ecorePackage.getEString(), "DefaultValue", null, 0, 1, HL7FileNode.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHL7FileNode_RelatedColumn(), ecorePackage.getEString(), "RelatedColumn", null, 0, 1, HL7FileNode.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHL7FileNode_Repeatable(), ecorePackage.getEBoolean(), "Repeatable", null, 0, 1, HL7FileNode.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(ftpConnectionEClass, FTPConnection.class, "FTPConnection", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getFTPConnection_Host(), ecorePackage.getEString(), "Host", null, 0, 1, FTPConnection.class, !IS_TRANSIENT,
                !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFTPConnection_Port(), ecorePackage.getEString(), "Port", null, 0, 1, FTPConnection.class, !IS_TRANSIENT,
                !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFTPConnection_Username(), ecorePackage.getEString(), "Username", null, 0, 1, FTPConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFTPConnection_Password(), ecorePackage.getEString(), "Password", null, 0, 1, FTPConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFTPConnection_Mode(), ecorePackage.getEString(), "Mode", null, 0, 1, FTPConnection.class, !IS_TRANSIENT,
                !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFTPConnection_Ecoding(), ecorePackage.getEString(), "Ecoding", null, 0, 1, FTPConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFTPConnection_SFTP(), ecorePackage.getEBoolean(), "SFTP", null, 0, 1, FTPConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFTPConnection_FTPS(), ecorePackage.getEBoolean(), "FTPS", null, 0, 1, FTPConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFTPConnection_Method(), ecorePackage.getEString(), "Method", null, 0, 1, FTPConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFTPConnection_Privatekey(), ecorePackage.getEString(), "Privatekey", null, 0, 1, FTPConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFTPConnection_Passphrase(), ecorePackage.getEString(), "Passphrase", null, 0, 1, FTPConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFTPConnection_KeystoreFile(), ecorePackage.getEString(), "KeystoreFile", null, 0, 1,
                FTPConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getFTPConnection_KeystorePassword(), ecorePackage.getEString(), "KeystorePassword", null, 0, 1,
                FTPConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getFTPConnection_Usesocks(), ecorePackage.getEBoolean(), "Usesocks", null, 0, 1, FTPConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFTPConnection_Proxyhost(), ecorePackage.getEString(), "Proxyhost", null, 0, 1, FTPConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFTPConnection_Proxyport(), ecorePackage.getEString(), "Proxyport", null, 0, 1, FTPConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFTPConnection_Proxyuser(), ecorePackage.getEString(), "Proxyuser", null, 0, 1, FTPConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFTPConnection_Proxypassword(), ecorePackage.getEString(), "Proxypassword", null, 0, 1,
                FTPConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getFTPConnection_CustomEncode(), ecorePackage.getEString(), "CustomEncode", null, 0, 1,
                FTPConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getFTPConnection_UseFileNameEncoding(), ecorePackage.getEBoolean(), "UseFileNameEncoding", null, 0, 1,
                FTPConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);

        initEClass(brmsConnectionEClass, BRMSConnection.class, "BRMSConnection", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getBRMSConnection_XmlField(), ecorePackage.getEString(), "xmlField", null, 0, 1, BRMSConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBRMSConnection_UrlName(), ecorePackage.getEString(), "urlName", null, 0, 1, BRMSConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBRMSConnection_TacWebappName(), ecorePackage.getEString(), "tacWebappName", null, 0, 1,
                BRMSConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getBRMSConnection_ClassName(), ecorePackage.getEString(), "className", null, 0, 1, BRMSConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBRMSConnection_ModuleUsed(), ecorePackage.getEString(), "moduleUsed", null, 0, 1, BRMSConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getBRMSConnection_Root(), this.getXMLFileNode(), null, "root", null, 0, -1, BRMSConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEReference(getBRMSConnection_Group(), this.getXMLFileNode(), null, "group", null, 0, -1, BRMSConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEReference(getBRMSConnection_Loop(), this.getXMLFileNode(), null, "loop", null, 0, -1, BRMSConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBRMSConnection_Package(), ecorePackage.getEString(), "package", null, 0, 1, BRMSConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(validationRulesConnectionEClass, ValidationRulesConnection.class, "ValidationRulesConnection", !IS_ABSTRACT,
                !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getValidationRulesConnection_IsSelect(), ecorePackage.getEBoolean(), "isSelect", null, 0, 1,
                ValidationRulesConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getValidationRulesConnection_IsInsert(), ecorePackage.getEBoolean(), "isInsert", null, 0, 1,
                ValidationRulesConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getValidationRulesConnection_IsUpdate(), ecorePackage.getEBoolean(), "isUpdate", null, 0, 1,
                ValidationRulesConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getValidationRulesConnection_IsDelete(), ecorePackage.getEBoolean(), "isDelete", null, 0, 1,
                ValidationRulesConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getValidationRulesConnection_Type(), this.getRuleType(), "type", null, 0, 1,
                ValidationRulesConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getValidationRulesConnection_BaseSchema(), ecorePackage.getEString(), "baseSchema", null, 0, 1,
                ValidationRulesConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getValidationRulesConnection_BaseColumnNames(), ecorePackage.getEString(), "baseColumnNames", null, 0, -1,
                ValidationRulesConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getValidationRulesConnection_RefSchema(), ecorePackage.getEString(), "refSchema", null, 0, 1,
                ValidationRulesConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getValidationRulesConnection_RefColumnNames(), ecorePackage.getEString(), "refColumnNames", null, 0, -1,
                ValidationRulesConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getValidationRulesConnection_JavaCondition(), ecorePackage.getEString(), "javaCondition", null, 0, 1,
                ValidationRulesConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getValidationRulesConnection_SqlCondition(), ecorePackage.getEString(), "sqlCondition", null, 0, 1,
                ValidationRulesConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getValidationRulesConnection_LogicalOperator(), this.getLogicalOperator(), "logicalOperator", null, 0, 1,
                ValidationRulesConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEReference(getValidationRulesConnection_Conditions(), this.getConditionType(), null, "conditions", null, 0, -1,
                ValidationRulesConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getValidationRulesConnection_InnerJoins(), this.getInnerJoinMap(), null, "innerJoins", null, 0, -1,
                ValidationRulesConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getValidationRulesConnection_IsDisallow(), ecorePackage.getEBoolean(), "isDisallow", null, 0, 1,
                ValidationRulesConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getValidationRulesConnection_IsRejectLink(), ecorePackage.getEBoolean(), "isRejectLink", null, 0, 1,
                ValidationRulesConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);

        initEClass(conditionTypeEClass, ConditionType.class, "ConditionType", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getConditionType_InputColumn(), ecorePackage.getEString(), "inputColumn", null, 1, 1, ConditionType.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConditionType_Function(), this.getFunction(), "function", null, 1, 1, ConditionType.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConditionType_Operator(), this.getOperator(), "operator", null, 1, 1, ConditionType.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConditionType_Value(), ecorePackage.getEString(), "value", null, 0, 1, ConditionType.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(innerJoinMapEClass, Map.Entry.class, "InnerJoinMap", !IS_ABSTRACT, !IS_INTERFACE,
                !IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getInnerJoinMap_Key(), ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT,
                !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getInnerJoinMap_Value(), ecorePackage.getEString(), "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT,
                !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(edifactConnectionEClass, EDIFACTConnection.class, "EDIFACTConnection", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getEDIFACTConnection_XmlName(), ecorePackage.getEString(), "XmlName", null, 0, 1, EDIFACTConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getEDIFACTConnection_FileName(), ecorePackage.getEString(), "FileName", null, 0, 1,
                EDIFACTConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getEDIFACTConnection_XmlPath(), ecorePackage.getEString(), "XmlPath", null, 0, 1, EDIFACTConnection.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(edifactColumnEClass, EDIFACTColumn.class, "EDIFACTColumn", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getEDIFACTColumn_EDIColumnName(), ecorePackage.getEString(), "EDIColumnName", null, 0, 1,
                EDIFACTColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getEDIFACTColumn_EDIXpath(), ecorePackage.getEString(), "EDIXpath", null, 0, 1, EDIFACTColumn.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(salesforceModuleUnitEClass, SalesforceModuleUnit.class, "SalesforceModuleUnit", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSalesforceModuleUnit_MetadataTable(), this.getMetadataTable(), null, "MetadataTable", null, 0, 1,
                SalesforceModuleUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSalesforceModuleUnit_Connection(), this.getSalesforceSchemaConnection(),
                this.getSalesforceSchemaConnection_Modules(), "connection", null, 0, 1, SalesforceModuleUnit.class, !IS_TRANSIENT,
                !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEReference(getSalesforceModuleUnit_Tables(), this.getMetadataTable(), null, "tables", null, 0, -1,
                SalesforceModuleUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSalesforceModuleUnit_ModuleName(), ecorePackage.getEString(), "moduleName", null, 0, 1,
                SalesforceModuleUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);

        initEClass(additionalPropertiesEClass, Map.Entry.class, "AdditionalProperties", !IS_ABSTRACT, !IS_INTERFACE,
                !IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAdditionalProperties_Key(), ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAdditionalProperties_Value(), ecorePackage.getEString(), "value", null, 0, 1, Map.Entry.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(sapTableEClass, SAPTable.class, "SAPTable", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getSAPTable_TableSearchType(), ecorePackage.getEString(), "tableSearchType", null, 0, 1, SAPTable.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(sapTableFieldEClass, SAPTableField.class, "SAPTableField", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getSAPTableField_BusinessName(), ecorePackage.getEString(), "businessName", null, 0, 1,
                SAPTableField.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getSAPTableField_RefTable(), ecorePackage.getEString(), "refTable", null, 0, -1, SAPTableField.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(sapFunctionParameterEClass, SAPFunctionParameter.class, "SAPFunctionParameter", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getSAPFunctionParameter_Name(), ecorePackage.getEString(), "name", null, 0, 1, SAPFunctionParameter.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSAPFunctionParameter_Type(), ecorePackage.getEString(), "type", null, 0, 1, SAPFunctionParameter.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSAPFunctionParameter_Description(), ecorePackage.getEString(), "description", null, 0, 1,
                SAPFunctionParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSAPFunctionParameter_Length(), ecorePackage.getEString(), "length", null, 0, 1,
                SAPFunctionParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSAPFunctionParameter_Changing(), ecorePackage.getEBoolean(), "changing", null, 0, 1,
                SAPFunctionParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSAPFunctionParameter_TestValue(), ecorePackage.getEString(), "testValue", null, 0, 1,
                SAPFunctionParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEReference(getSAPFunctionParameter_Children(), this.getSAPFunctionParameter(), null, "children", null, 0, -1,
                SAPFunctionParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSAPFunctionParameter_TableResideInTables(), ecorePackage.getEBoolean(), "tableResideInTables", "true",
                0, 1, SAPFunctionParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);

        initEClass(sapFunctionParamDataEClass, SAPFunctionParamData.class, "SAPFunctionParamData", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSAPFunctionParamData_InputRoot(), this.getSAPFunctionParameter(), null, "inputRoot", null, 0, 1,
                SAPFunctionParamData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSAPFunctionParamData_OutputRoot(), this.getSAPFunctionParameter(), null, "outputRoot", null, 0, 1,
                SAPFunctionParamData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(additionalConnectionPropertyEClass, AdditionalConnectionProperty.class, "AdditionalConnectionProperty",
                !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAdditionalConnectionProperty_PropertyName(), ecorePackage.getEString(), "propertyName", null, 0, 1,
                AdditionalConnectionProperty.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAdditionalConnectionProperty_Value(), ecorePackage.getEString(), "Value", null, 0, 1,
                AdditionalConnectionProperty.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);

        initEClass(sapbwTableEClass, SAPBWTable.class, "SAPBWTable", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getSAPBWTable_ModelType(), ecorePackage.getEString(), "modelType", null, 0, 1, SAPBWTable.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSAPBWTable_Active(), ecorePackage.getEBoolean(), "active", null, 0, 1, SAPBWTable.class, !IS_TRANSIENT,
                !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSAPBWTable_SourceSystemName(), ecorePackage.getEString(), "sourceSystemName", null, 0, 1,
                SAPBWTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getSAPBWTable_InfoAreaName(), ecorePackage.getEString(), "infoAreaName", null, 0, 1, SAPBWTable.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSAPBWTable_InnerIOType(), ecorePackage.getEString(), "innerIOType", null, 0, 1, SAPBWTable.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(sapbwTableFieldEClass, SAPBWTableField.class, "SAPBWTableField", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getSAPBWTableField_LogicalName(), ecorePackage.getEString(), "logicalName", null, 0, 1,
                SAPBWTableField.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);

        // Initialize enums and add enum literals
        initEEnum(fileFormatEEnum, FileFormat.class, "FileFormat");
        addEEnumLiteral(fileFormatEEnum, FileFormat.UNIX_LITERAL);
        addEEnumLiteral(fileFormatEEnum, FileFormat.MAC_LITERAL);
        addEEnumLiteral(fileFormatEEnum, FileFormat.WINDOWS_LITERAL);

        initEEnum(fieldSeparatorEEnum, FieldSeparator.class, "FieldSeparator");
        addEEnumLiteral(fieldSeparatorEEnum, FieldSeparator.TABULATION_LITERAL);
        addEEnumLiteral(fieldSeparatorEEnum, FieldSeparator.SEMICOLON_LITERAL);
        addEEnumLiteral(fieldSeparatorEEnum, FieldSeparator.COMMA_LITERAL);
        addEEnumLiteral(fieldSeparatorEEnum, FieldSeparator.SPACE_LITERAL);
        addEEnumLiteral(fieldSeparatorEEnum, FieldSeparator.ALT_65_LITERAL);
        addEEnumLiteral(fieldSeparatorEEnum, FieldSeparator.CUSTOM_ANSI_LITERAL);
        addEEnumLiteral(fieldSeparatorEEnum, FieldSeparator.CUSTOM_UTF8_LITERAL);
        addEEnumLiteral(fieldSeparatorEEnum, FieldSeparator.CUSTOM_REG_EXP_LITERAL);

        initEEnum(escapeEEnum, Escape.class, "Escape");
        addEEnumLiteral(escapeEEnum, Escape.DELIMITED);
        addEEnumLiteral(escapeEEnum, Escape.CSV);

        initEEnum(rowSeparatorEEnum, RowSeparator.class, "RowSeparator");
        addEEnumLiteral(rowSeparatorEEnum, RowSeparator.CUSTOM_STRING_LITERAL);
        addEEnumLiteral(rowSeparatorEEnum, RowSeparator.STANDART_EOL_LITERAL);

        initEEnum(mdmConnectionProtocolEEnum, MDMConnectionProtocol.class, "MDMConnectionProtocol");
        addEEnumLiteral(mdmConnectionProtocolEEnum, MDMConnectionProtocol.HTTP);

        initEEnum(mdmConceptTypeEEnum, MdmConceptType.class, "MdmConceptType");
        addEEnumLiteral(mdmConceptTypeEEnum, MdmConceptType.INPUT);
        addEEnumLiteral(mdmConceptTypeEEnum, MdmConceptType.OUTPUT);
        addEEnumLiteral(mdmConceptTypeEEnum, MdmConceptType.RECEIVE);

        initEEnum(ruleTypeEEnum, RuleType.class, "RuleType");
        addEEnumLiteral(ruleTypeEEnum, RuleType.REFERENCE);
        addEEnumLiteral(ruleTypeEEnum, RuleType.BASIC);
        addEEnumLiteral(ruleTypeEEnum, RuleType.CUSTOM);

        initEEnum(functionEEnum, Function.class, "Function");
        addEEnumLiteral(functionEEnum, Function.EMPTY);
        addEEnumLiteral(functionEEnum, Function.LOWER_CASE);
        addEEnumLiteral(functionEEnum, Function.UPPER_CASE);
        addEEnumLiteral(functionEEnum, Function.LOWER_CASE_FIRST);
        addEEnumLiteral(functionEEnum, Function.UPPER_CASE_FIRST);
        addEEnumLiteral(functionEEnum, Function.LENGTH);
        addEEnumLiteral(functionEEnum, Function.MATCH);

        initEEnum(operatorEEnum, Operator.class, "Operator");
        addEEnumLiteral(operatorEEnum, Operator.EQUALS);
        addEEnumLiteral(operatorEEnum, Operator.NOT_EQUALS);
        addEEnumLiteral(operatorEEnum, Operator.GREATER);
        addEEnumLiteral(operatorEEnum, Operator.LOWER);
        addEEnumLiteral(operatorEEnum, Operator.GREATER_OR_EQUALS);
        addEEnumLiteral(operatorEEnum, Operator.LOWER_OR_EQUALS);

        initEEnum(logicalOperatorEEnum, LogicalOperator.class, "LogicalOperator");
        addEEnumLiteral(logicalOperatorEEnum, LogicalOperator.AND);
        addEEnumLiteral(logicalOperatorEEnum, LogicalOperator.OR);

        // Initialize data types
        initEDataType(mapEDataType, HashMap.class, "Map", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(listEDataType, ArrayList.class, "List", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

        // Create resource
        createResource(eNS_URI);

        // Create annotations
        // http://www.eclipse.org/emf/2002/GenModel
        createGenModelAnnotations();
        // MapEntry
        createMapEntryAnnotations();
    }

    /**
     * Initializes the annotations for <b>http://www.eclipse.org/emf/2002/GenModel</b>.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     */
    protected void createGenModelAnnotations() {
        String source = "http://www.eclipse.org/emf/2002/GenModel";
        addAnnotation(connectionEClass, source, new String[] { "documentation",
                "base class tha represent a connection, may be to a database or a file or else" });
        addAnnotation(getConnection_Queries(), source,
                new String[] { "documentation", "This defines the SQL queries related to this connection" });
        addAnnotation(getConnection_ContextMode(), source,
                new String[] { "documentation", "whether this connection is defined using a context or is standalone" });
        addAnnotation(getConnection_ContextId(), source, new String[] { "documentation",
                "Id of the context this connection is linked to, only used when ContextMode attribute is true" });
        addAnnotation(metadataColumnEClass, source, new String[] { "documentation",
                "represents a metada column which contains source (such as DB) definitions as weel as Talend mappings" });
        addAnnotation(getMetadataColumn_SourceType(), source, new String[] { "documentation",
                "Schema DB type (VARCHAR for example ), can be initialised from DB  column type and modified by the user.)\r\nThis is maintained in synch with the TalendType (at least in the Table schema editor).\r\n" });
        addAnnotation(getMetadataColumn_DefaultValue(), source, new String[] { "documentation",
                "@deprecated Use initialValue  instead\r\n(This represents the default value for column. This may be changed by the user.)\r\n\r\n" });
        addAnnotation(getMetadataColumn_TalendType(), source, new String[] { "documentation",
                "java type used by Talend for handling this column elements; This seems to be synched with the sourceType.\r\nThis must be the case for schema used for Table creation." });
        addAnnotation(getMetadataColumn_Key(), source, new String[] { "documentation",
                "Whether this column is a considered a key, in a business meaning (This is not technical).\r\nThis may apply to file, xml or dB columns.\r\nMay be changed by the user.\r\nWhen retrieving Metadata from DB this will be set to true if the column belong to the primary key." });
        addAnnotation(getMetadataColumn_Nullable(), source,
                new String[] { "documentation", "whether this column supports null values. May be changed by the user." });
        addAnnotation(getMetadataColumn_Table(), source,
                new String[] { "documentation", "reference to the containing table or view" });
        addAnnotation(getMetadataColumn_OriginalField(), source,
                new String[] { "documentation", "@deprecated use g(s)etName\r\nLogical name of the column" });
        addAnnotation(getMetadataColumn_Pattern(), source,
                new String[] { "documentation", "pattern mainly used for date parsing" });
        addAnnotation(abstractMetadataObjectEClass, source,
                new String[] { "documentation", "base class for all the metadata model" });
        addAnnotation(getAbstractMetadataObject_Properties(), source, new String[] { "documentation",
                "@deprecated Use taggedValue instead\r\n(map of general purpose key/value that is available to all classes of the metamodel.)\r\n" });
        addAnnotation(getAbstractMetadataObject_Id(), source, new String[] { "documentation", "logical identifier" });
        addAnnotation(getAbstractMetadataObject_Comment(), source,
                new String[] { "documentation", "free comment of this element, may be displayed to the user." });
        addAnnotation(getAbstractMetadataObject_Label(), source,
                new String[] { "documentation", "name to be displayed for the current object" });
        addAnnotation(metadataTableEClass, source, new String[] { "documentation", "representation of a of set of columns" });
        addAnnotation(getMetadataTable_SourceName(), source, new String[] { "documentation",
                "@deprecated use g(s)etName()\r\nname of the table, that is actual DB table name for DB tables" });
        addAnnotation(getMetadataTable_TableType(), source,
                new String[] { "documentation", "of value of TABLE, VIEW, SYNONYM, ALL_SYNONYM" });
        addAnnotation(getMetadataTable_AttachedCDC(), source,
                new String[] { "documentation", "whether a CDC table is attached to this table" });
        addAnnotation(getMetadataTable_ActivatedCDC(), source,
                new String[] { "documentation", "whether CDC is activated, that is the trigger are set to record the changes" });
        addAnnotation(getMetadataTable_Columns(), source, new String[] { "documentation",
                "List of columns related to this table, this is a derived attribute from the feature attribute, thus volatile and transiant" });
        addAnnotation(getMetadataTable_Connection(), source, new String[] { "documentation",
                "@deprecated use MetadataTableHelper.getFirstconnection()\r\nref to the connection that contains this table" });
        addAnnotation(mdmConnectionEClass.getEOperations().get(0), source, new String[] { "documentation",
                "return the connection string to connect to the MDM server,\r\nit is a concatenation of protocol, server, port and context.\r\nthe connection string returned may not be a valid URL if some of the concatenated elements are not properly set.\r\nNo checking is done." });
        addAnnotation(getMDMConnection_Protocol(), source,
                new String[] { "documentation", "protocol used for connecting to MDM server, initial protocol is HTTP" });
        addAnnotation(getMDMConnection_Context(), source, new String[] { "documentation",
                "part of the url for connecting to the server, \r\nthe last part that defined the MDM web app context" });
        addAnnotation(databaseConnectionEClass, source, new String[] { "documentation", "Defines a connection to a Database" });
        addAnnotation(getDatabaseConnection_DatabaseType(), source,
                new String[] { "documentation", "logical type of the DB (for instance MySQL)" });
        addAnnotation(getDatabaseConnection_DriverJarPath(), source,
                new String[] { "documentation", "absolute path to the jar that may be used for Generic JDBC connection" });
        addAnnotation(getDatabaseConnection_DriverClass(), source,
                new String[] { "documentation", "initial class for generic JDBC connection" });
        addAnnotation(getDatabaseConnection_URL(), source, new String[] { "documentation",
                "the connection base URL for JDBC protocol.\r\nIt is a concatenation of DatabaseType, ServerName, Port and other attributes of this class" });
        addAnnotation(getDatabaseConnection_DbVersionString(), source, new String[] { "documentation",
                "may hold the version of the Database for a given Database type (for instance MySQL_4 or MySQL_5)" });
        addAnnotation(getDatabaseConnection_Port(), source,
                new String[] { "documentation", "port used for the Database Connection" });
        addAnnotation(getDatabaseConnection_Username(), source,
                new String[] { "documentation", "user name used for DB connection authentification" });
        addAnnotation(getDatabaseConnection_Password(), source,
                new String[] { "documentation", "password used for DB connection authentification" });
        addAnnotation(getDatabaseConnection_ServerName(), source,
                new String[] { "documentation", "IP adress or machine name of the DB server to connect to." });
        addAnnotation(getDatabaseConnection_FileFieldName(), source,
                new String[] { "documentation", "Database file used for DB such as SqlLite" });
        addAnnotation(getDatabaseConnection_SID(), source, new String[] { "documentation", "Logical name of the Database" });
        addAnnotation(getDatabaseConnection_AdditionalParams(), source,
                new String[] { "documentation", "parameters that are to be added to the connection URL" });
        addAnnotation(getDatabaseConnection_CdcConns(), source,
                new String[] { "documentation", "reference to CDC definition for this connection" });
        addAnnotation(sapFunctionUnitEClass.getEOperations().get(0), source,
                new String[] { "documentation", "@deprecated use SAPFunctionHelper.getFirstDocument().g(s)etReference()" });
        addAnnotation(sapFunctionParameterColumnEClass.getEOperations().get(0), source,
                new String[] { "documentation", "@deprecated use ModelElementHelper.getFirstDescription().setBody()" });
        addAnnotation(cdcConnectionEClass, source,
                new String[] { "documentation", "defining Change Data Capture for a given connection" });
        addAnnotation(getCDCConnection_Connection(), source,
                new String[] { "documentation", "the connection this CDC relates to" });
        addAnnotation(getCDCType_LinkDB(), source,
                new String[] { "documentation", "ID of the .properties file related to the CDC database" });
        addAnnotation(genericPackageEClass, source, new String[] { "documentation",
                "Default CWM package to use when none of the existing cwm packages fit the metadata to describe.\r\nThis is a container between Connection and MetadataTable" });
    }

    /**
     * Initializes the annotations for <b>MapEntry</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createMapEntryAnnotations() {
        String source = "MapEntry";
        addAnnotation(additionalPropertiesEClass, source, new String[] {});
    }

} // ConnectionPackageImpl
