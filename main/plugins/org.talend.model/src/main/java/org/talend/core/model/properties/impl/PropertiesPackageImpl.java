/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.core.model.properties.impl;

import java.util.Map;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.eclipse.gmf.runtime.notation.NotationPackage;

import org.talend.core.model.metadata.builder.connection.ConnectionPackage;

import org.talend.core.model.properties.ArtifactNotification;
import org.talend.core.model.properties.BRMSConnectionItem;
import org.talend.core.model.properties.BusinessProcessItem;
import org.talend.core.model.properties.ByteArray;
import org.talend.core.model.properties.CSVFileConnectionItem;
import org.talend.core.model.properties.Component;
import org.talend.core.model.properties.ComponentSetting;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.CronTalendTrigger;
import org.talend.core.model.properties.CronUITalendTrigger;
import org.talend.core.model.properties.CustomComponentSetting;
import org.talend.core.model.properties.DashboardConnection;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.DelimitedFileConnectionItem;
import org.talend.core.model.properties.DocumentationItem;
import org.talend.core.model.properties.EDIFACTConnectionItem;
import org.talend.core.model.properties.EbcdicConnectionItem;
import org.talend.core.model.properties.ExcelFileConnectionItem;
import org.talend.core.model.properties.ExchangeUser;
import org.talend.core.model.properties.ExecutionPlan;
import org.talend.core.model.properties.ExecutionPlanPart;
import org.talend.core.model.properties.ExecutionPlanPartCmdPrm;
import org.talend.core.model.properties.ExecutionPlanPartJobPrm;
import org.talend.core.model.properties.ExecutionPlanPrm;
import org.talend.core.model.properties.ExecutionServer;
import org.talend.core.model.properties.ExecutionTask;
import org.talend.core.model.properties.ExecutionTaskCmdPrm;
import org.talend.core.model.properties.ExecutionTaskJobPrm;
import org.talend.core.model.properties.ExecutionTaskProperties;
import org.talend.core.model.properties.ExecutionTriggerable;
import org.talend.core.model.properties.ExecutionVirtualServer;
import org.talend.core.model.properties.FTPConnectionItem;
import org.talend.core.model.properties.FileItem;
import org.talend.core.model.properties.FileTrigger;
import org.talend.core.model.properties.FileTriggerMask;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.FolderType;
import org.talend.core.model.properties.GenericSchemaConnectionItem;
import org.talend.core.model.properties.HL7ConnectionItem;
import org.talend.core.model.properties.HeaderFooterConnectionItem;
import org.talend.core.model.properties.ImplicitContextSettings;
import org.talend.core.model.properties.Information;
import org.talend.core.model.properties.InformationLevel;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ItemRelation;
import org.talend.core.model.properties.ItemRelations;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.JobDocumentationItem;
import org.talend.core.model.properties.JobScriptItem;
import org.talend.core.model.properties.JobletDocumentationItem;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.LDAPSchemaConnectionItem;
import org.talend.core.model.properties.LdifFileConnectionItem;
import org.talend.core.model.properties.License;
import org.talend.core.model.properties.LinkDocumentationItem;
import org.talend.core.model.properties.LinkRulesItem;
import org.talend.core.model.properties.LinkType;
import org.talend.core.model.properties.MDMConnectionItem;
import org.talend.core.model.properties.MigrationStatus;
import org.talend.core.model.properties.MigrationTask;
import org.talend.core.model.properties.NotationHolder;
import org.talend.core.model.properties.Notification;
import org.talend.core.model.properties.PigudfItem;
import org.talend.core.model.properties.PlanExecutionHistory;
import org.talend.core.model.properties.PositionalFileConnectionItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.ProjectComponentAuthorisation;
import org.talend.core.model.properties.ProjectReference;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.ReferenceFileItem;
import org.talend.core.model.properties.ReferenceItem;
import org.talend.core.model.properties.RegExFileConnectionItem;
import org.talend.core.model.properties.RoleRight;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.properties.RulesItem;
import org.talend.core.model.properties.SAPConnectionItem;
import org.talend.core.model.properties.SQLPatternItem;
import org.talend.core.model.properties.SVGBusinessProcessItem;
import org.talend.core.model.properties.SalesforceSchemaConnectionItem;
import org.talend.core.model.properties.SchemaInformation;
import org.talend.core.model.properties.SimpleTalendTrigger;
import org.talend.core.model.properties.SnippetItem;
import org.talend.core.model.properties.SnippetVariable;
import org.talend.core.model.properties.SoaInputParameter;
import org.talend.core.model.properties.SoaOperation;
import org.talend.core.model.properties.SoaService;
import org.talend.core.model.properties.SpagoBiServer;
import org.talend.core.model.properties.StatAndLogsSettings;
import org.talend.core.model.properties.Status;
import org.talend.core.model.properties.TDQItem;
import org.talend.core.model.properties.TalendTrigger;
import org.talend.core.model.properties.TaskExecutionHistory;
import org.talend.core.model.properties.TechnicalVariable;
import org.talend.core.model.properties.User;
import org.talend.core.model.properties.UserModuleAuthorization;
import org.talend.core.model.properties.UserModuleAuthorizationType;
import org.talend.core.model.properties.UserProjectAuthorization;
import org.talend.core.model.properties.UserProjectAuthorizationType;
import org.talend.core.model.properties.UserRight;
import org.talend.core.model.properties.UserRole;
import org.talend.core.model.properties.UserRoleReference;
import org.talend.core.model.properties.ValidationRulesConnectionItem;
import org.talend.core.model.properties.WSDLSchemaConnectionItem;
import org.talend.core.model.properties.XmlFileConnectionItem;

import org.talend.designer.business.model.business.BusinessPackage;

import org.talend.designer.core.model.utils.emf.component.ComponentPackage;

import org.talend.designer.core.model.utils.emf.talendfile.TalendFilePackage;

import org.talend.designer.joblet.model.JobletPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class PropertiesPackageImpl extends EPackageImpl implements PropertiesPackage {
    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass projectEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass projectComponentAuthorisationEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass projectReferenceEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass statusEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass itemStateEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass propertyEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass itemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass linkDocumentationItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass linkTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass businessProcessItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass fileItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass byteArrayEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass documentationItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass routineItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass connectionItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass snippetVariableEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass snippetItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass delimitedFileConnectionItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass positionalFileConnectionItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass regExFileConnectionItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass csvFileConnectionItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass databaseConnectionItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass sapConnectionItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass xmlFileConnectionItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass ldifFileConnectionItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass excelFileConnectionItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass ebcdicConnectionItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass mdmConnectionItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass validationRulesConnectionItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass processItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass userRoleEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass userEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass folderItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass componentEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass notationHolderEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass userProjectAuthorizationEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass contextItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass spagoBiServerEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass licenseEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass genericSchemaConnectionItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass userModuleAuthorizationEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass ldapSchemaConnectionItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass salesforceSchemaConnectionItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass dashboardConnectionEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass executionTriggerableEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass executionPlanEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass executionPlanPartEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass executionPlanPrmEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass executionPlanPartCmdPrmEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass executionPlanPartJobPrmEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass executionTaskEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass executionTaskPropertiesEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass executionTaskCmdPrmEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass executionTaskJobPrmEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass taskExecutionHistoryEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass planExecutionHistoryEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass talendTriggerEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass cronTalendTriggerEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass cronUITalendTriggerEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass simpleTalendTriggerEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass fileTriggerEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass fileTriggerMaskEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass executionServerEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass executionVirtualServerEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass jobletProcessItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass jobDocumentationItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass jobletDocumentationItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass wsdlSchemaConnectionItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass schemaInformationEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass informationEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass sqlPatternItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass componentSettingEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass statAndLogsSettingsEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass implicitContextSettingsEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass soaOperationEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass soaInputParameterEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass soaServiceEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass rulesItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass userRightEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass roleRightEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass svgBusinessProcessItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass linkRulesItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass itemRelationsEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass itemRelationEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass notificationEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass hl7ConnectionItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass userRoleReferenceEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass headerFooterConnectionItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass ftpConnectionItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass customComponentSettingEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass tdqItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass brmsConnectionItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass jobScriptItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass edifactConnectionItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass technicalVariableEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass referenceItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass exchangeUserEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass artifactNotificationEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass additionalInfoMapEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass migrationTaskEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass pigudfItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass referenceFileItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EEnum folderTypeEEnum = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EEnum userProjectAuthorizationTypeEEnum = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EEnum userModuleAuthorizationTypeEEnum = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EEnum informationLevelEEnum = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EEnum migrationStatusEEnum = null;

    /**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.talend.core.model.properties.PropertiesPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
    private PropertiesPackageImpl() {
		super(eNS_URI, PropertiesFactory.eINSTANCE);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private static boolean isInited = false;

    /**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link PropertiesPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
    public static PropertiesPackage init() {
		if (isInited) return (PropertiesPackage)EPackage.Registry.INSTANCE.getEPackage(PropertiesPackage.eNS_URI);

		// Obtain or create and register package
		PropertiesPackageImpl thePropertiesPackage = (PropertiesPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof PropertiesPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new PropertiesPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		BusinessPackage.eINSTANCE.eClass();
		ComponentPackage.eINSTANCE.eClass();
		JobletPackage.eINSTANCE.eClass();
		ConnectionPackage.eINSTANCE.eClass();
		NotationPackage.eINSTANCE.eClass();
		XMLTypePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		thePropertiesPackage.createPackageContents();

		// Initialize created meta-data
		thePropertiesPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		thePropertiesPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(PropertiesPackage.eNS_URI, thePropertiesPackage);
		return thePropertiesPackage;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getProject() {
		return projectEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getProject_TechnicalStatus() {
		return (EReference)projectEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getProject_DocumentationStatus() {
		return (EReference)projectEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProject_Id() {
		return (EAttribute)projectEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProject_Label() {
		return (EAttribute)projectEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProject_Description() {
		return (EAttribute)projectEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProject_Language() {
		return (EAttribute)projectEClass.getEStructuralFeatures().get(5);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProject_TechnicalLabel() {
		return (EAttribute)projectEClass.getEStructuralFeatures().get(6);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProject_Local() {
		return (EAttribute)projectEClass.getEStructuralFeatures().get(7);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getProject_Folders() {
		return (EReference)projectEClass.getEStructuralFeatures().get(8);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProject_Deleted() {
		return (EAttribute)projectEClass.getEStructuralFeatures().get(9);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProject_DeleteDate() {
		return (EAttribute)projectEClass.getEStructuralFeatures().get(10);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProject_CreationDate() {
		return (EAttribute)projectEClass.getEStructuralFeatures().get(11);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getProject_Author() {
		return (EReference)projectEClass.getEStructuralFeatures().get(12);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getProject_UserAuthorization() {
		return (EReference)projectEClass.getEStructuralFeatures().get(13);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getProject_AllowedComponents() {
		return (EReference)projectEClass.getEStructuralFeatures().get(14);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getProject_ReferencedProjects() {
		return (EReference)projectEClass.getEStructuralFeatures().get(15);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getProject_AvailableRefProject() {
		return (EReference)projectEClass.getEStructuralFeatures().get(16);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProject_MigrationTasks() {
		return (EAttribute)projectEClass.getEStructuralFeatures().get(32);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getProject_MigrationTask() {
		return (EReference)projectEClass.getEStructuralFeatures().get(33);
	}

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProject_BigData() {
		return (EAttribute)projectEClass.getEStructuralFeatures().get(34);
	}

				/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProject_MasterJobId() {
		return (EAttribute)projectEClass.getEStructuralFeatures().get(17);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getProject_SpagoBiServer() {
		return (EReference)projectEClass.getEStructuralFeatures().get(18);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProject_ProductVersion() {
		return (EAttribute)projectEClass.getEStructuralFeatures().get(19);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getProject_ComponentsSettings() {
		return (EReference)projectEClass.getEStructuralFeatures().get(20);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProject_Url() {
		return (EAttribute)projectEClass.getEStructuralFeatures().get(21);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getProject_StatAndLogsSettings() {
		return (EReference)projectEClass.getEStructuralFeatures().get(22);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getProject_ImplicitContextSettings() {
		return (EReference)projectEClass.getEStructuralFeatures().get(23);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProject_HidePassword() {
		return (EAttribute)projectEClass.getEStructuralFeatures().get(24);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getProject_ItemsRelations() {
		return (EReference)projectEClass.getEStructuralFeatures().get(25);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProject_Reference() {
		return (EAttribute)projectEClass.getEStructuralFeatures().get(26);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getProject_CustomComponentSettings() {
		return (EReference)projectEClass.getEStructuralFeatures().get(27);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProject_DeletedFolders() {
		return (EAttribute)projectEClass.getEStructuralFeatures().get(28);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProject_Type() {
		return (EAttribute)projectEClass.getEStructuralFeatures().get(29);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProject_ItemsRelationVersion() {
		return (EAttribute)projectEClass.getEStructuralFeatures().get(30);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getProject_ExchangeUser() {
		return (EReference)projectEClass.getEStructuralFeatures().get(31);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getProjectComponentAuthorisation() {
		return projectComponentAuthorisationEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getProjectComponentAuthorisation_Project() {
		return (EReference)projectComponentAuthorisationEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getProjectComponentAuthorisation_Component() {
		return (EReference)projectComponentAuthorisationEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getProjectReference() {
		return projectReferenceEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getProjectReference_Project() {
		return (EReference)projectReferenceEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProjectReference_Branch() {
		return (EAttribute)projectReferenceEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getProjectReference_ReferencedProject() {
		return (EReference)projectReferenceEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProjectReference_ReferencedBranch() {
		return (EAttribute)projectReferenceEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getStatus() {
		return statusEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getStatus_Label() {
		return (EAttribute)statusEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getStatus_Code() {
		return (EAttribute)statusEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getItemState() {
		return itemStateEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getItemState_Path() {
		return (EAttribute)itemStateEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getItemState_Deleted() {
		return (EAttribute)itemStateEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getItemState_Locked() {
		return (EAttribute)itemStateEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getItemState_Locker() {
		return (EReference)itemStateEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getItemState_LockDate() {
		return (EAttribute)itemStateEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getItemState_CommitDate() {
		return (EAttribute)itemStateEClass.getEStructuralFeatures().get(5);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getProperty() {
		return propertyEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProperty_Id() {
		return (EAttribute)propertyEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProperty_Label() {
		return (EAttribute)propertyEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProperty_Purpose() {
		return (EAttribute)propertyEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProperty_Description() {
		return (EAttribute)propertyEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProperty_CreationDate() {
		return (EAttribute)propertyEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProperty_ModificationDate() {
		return (EAttribute)propertyEClass.getEStructuralFeatures().get(5);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProperty_Version() {
		return (EAttribute)propertyEClass.getEStructuralFeatures().get(6);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProperty_StatusCode() {
		return (EAttribute)propertyEClass.getEStructuralFeatures().get(7);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getProperty_Item() {
		return (EReference)propertyEClass.getEStructuralFeatures().get(8);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getProperty_Author() {
		return (EReference)propertyEClass.getEStructuralFeatures().get(9);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getProperty_Informations() {
		return (EReference)propertyEClass.getEStructuralFeatures().get(10);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProperty_MaxInformationLevel() {
		return (EAttribute)propertyEClass.getEStructuralFeatures().get(11);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProperty_OldStatusCode() {
		return (EAttribute)propertyEClass.getEStructuralFeatures().get(12);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getProperty_AdditionalProperties() {
		return (EReference)propertyEClass.getEStructuralFeatures().get(13);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProperty_DisplayName() {
		return (EAttribute)propertyEClass.getEStructuralFeatures().get(14);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getItem() {
		return itemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getItem_Property() {
		return (EReference)itemEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getItem_State() {
		return (EReference)itemEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getItem_Parent() {
		return (EReference)itemEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getItem_ReferenceResources() {
		return (EReference)itemEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getItem_FileExtension() {
		return (EAttribute)itemEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getItem_NeedVersion() {
		return (EAttribute)itemEClass.getEStructuralFeatures().get(5);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getLinkDocumentationItem() {
		return linkDocumentationItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getLinkDocumentationItem_Name() {
		return (EAttribute)linkDocumentationItemEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getLinkDocumentationItem_Extension() {
		return (EAttribute)linkDocumentationItemEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getLinkDocumentationItem_Link() {
		return (EReference)linkDocumentationItemEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getLinkType() {
		return linkTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getLinkType_URI() {
		return (EAttribute)linkTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getLinkType_State() {
		return (EAttribute)linkTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getBusinessProcessItem() {
		return businessProcessItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getBusinessProcessItem_Notation() {
		return (EReference)businessProcessItemEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getBusinessProcessItem_Semantic() {
		return (EReference)businessProcessItemEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getBusinessProcessItem_NotationHolder() {
		return (EReference)businessProcessItemEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getBusinessProcessItem_SvgBusinessProcessItem() {
		return (EReference)businessProcessItemEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getFileItem() {
		return fileItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getFileItem_Name() {
		return (EAttribute)fileItemEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getFileItem_Extension() {
		return (EAttribute)fileItemEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getFileItem_Content() {
		return (EReference)fileItemEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getByteArray() {
		return byteArrayEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getByteArray_InnerContent() {
		return (EAttribute)byteArrayEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getDocumentationItem() {
		return documentationItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getRoutineItem() {
		return routineItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getRoutineItem_BuiltIn() {
		return (EAttribute)routineItemEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getRoutineItem_Imports() {
		return (EReference)routineItemEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getRoutineItem_PackageType() {
		return (EAttribute)routineItemEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getConnectionItem() {
		return connectionItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getConnectionItem_Connection() {
		return (EReference)connectionItemEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSnippetVariable() {
		return snippetVariableEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSnippetVariable_Name() {
		return (EAttribute)snippetVariableEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSnippetVariable_Value() {
		return (EAttribute)snippetVariableEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSnippetVariable_Description() {
		return (EAttribute)snippetVariableEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSnippetVariable_Id() {
		return (EAttribute)snippetVariableEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSnippetItem() {
		return snippetItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSnippetItem_Name() {
		return (EAttribute)snippetItemEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSnippetItem_Content() {
		return (EAttribute)snippetItemEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSnippetItem_Variables() {
		return (EReference)snippetItemEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getDelimitedFileConnectionItem() {
		return delimitedFileConnectionItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getPositionalFileConnectionItem() {
		return positionalFileConnectionItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getRegExFileConnectionItem() {
		return regExFileConnectionItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getCSVFileConnectionItem() {
		return csvFileConnectionItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getDatabaseConnectionItem() {
		return databaseConnectionItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSAPConnectionItem() {
		return sapConnectionItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getXmlFileConnectionItem() {
		return xmlFileConnectionItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getLdifFileConnectionItem() {
		return ldifFileConnectionItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getExcelFileConnectionItem() {
		return excelFileConnectionItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getEbcdicConnectionItem() {
		return ebcdicConnectionItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getMDMConnectionItem() {
		return mdmConnectionItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getValidationRulesConnectionItem() {
		return validationRulesConnectionItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getProcessItem() {
		return processItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getProcessItem_Process() {
		return (EReference)processItemEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getUserRole() {
		return userRoleEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getUserRole_Id() {
		return (EAttribute)userRoleEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getUserRole_Name() {
		return (EAttribute)userRoleEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getUserRole_LocalizedLabel() {
		return (EAttribute)userRoleEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getUserRole_Fixed() {
		return (EAttribute)userRoleEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getUserRole_RolesRights() {
		return (EReference)userRoleEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getUser() {
		return userEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getUser_Id() {
		return (EAttribute)userEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getUser_Login() {
		return (EAttribute)userEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getUser_Password() {
		return (EAttribute)userEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getUser_FirstName() {
		return (EAttribute)userEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getUser_LastName() {
		return (EAttribute)userEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getUser_CreationDate() {
		return (EAttribute)userEClass.getEStructuralFeatures().get(5);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getUser_DeleteDate() {
		return (EAttribute)userEClass.getEStructuralFeatures().get(6);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getUser_Deleted() {
		return (EAttribute)userEClass.getEStructuralFeatures().get(7);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getUser_AllowedToModifyComponents() {
		return (EAttribute)userEClass.getEStructuralFeatures().get(8);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getUser_Comment() {
		return (EAttribute)userEClass.getEStructuralFeatures().get(9);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getUser_ProjectAuthorization() {
		return (EReference)userEClass.getEStructuralFeatures().get(10);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getUser_ModuleAuthorization() {
		return (EReference)userEClass.getEStructuralFeatures().get(11);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getUser_PreferredDashboardConnection() {
		return (EReference)userEClass.getEStructuralFeatures().get(12);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getUser_LastAdminConnectionDate() {
		return (EAttribute)userEClass.getEStructuralFeatures().get(13);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getUser_LastStudioConnectionDate() {
		return (EAttribute)userEClass.getEStructuralFeatures().get(14);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getUser_FirstAdminConnectionDate() {
		return (EAttribute)userEClass.getEStructuralFeatures().get(15);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getUser_FirstStudioConnectionDate() {
		return (EAttribute)userEClass.getEStructuralFeatures().get(16);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getUser_AdminConnexionNumber() {
		return (EAttribute)userEClass.getEStructuralFeatures().get(17);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getUser_StudioConnexionNumber() {
		return (EAttribute)userEClass.getEStructuralFeatures().get(18);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getUser_AuthenticationInfo() {
		return (EAttribute)userEClass.getEStructuralFeatures().get(19);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getUser_LdapLogin() {
		return (EAttribute)userEClass.getEStructuralFeatures().get(20);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getUser_LdapId() {
		return (EAttribute)userEClass.getEStructuralFeatures().get(21);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getUser_Language() {
		return (EAttribute)userEClass.getEStructuralFeatures().get(22);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getUser_Type() {
		return (EAttribute)userEClass.getEStructuralFeatures().get(23);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getUser_AdditionnalData() {
		return (EAttribute)userEClass.getEStructuralFeatures().get(24);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getUser_Technical() {
		return (EAttribute)userEClass.getEStructuralFeatures().get(25);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getFolderItem() {
		return folderItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getFolderItem_Children() {
		return (EReference)folderItemEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getFolderItem_Type() {
		return (EAttribute)folderItemEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getComponent() {
		return componentEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getComponent_Id() {
		return (EAttribute)componentEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getComponent_Label() {
		return (EAttribute)componentEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getComponent_Version() {
		return (EAttribute)componentEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getComponent_LastUpdateDate() {
		return (EAttribute)componentEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getComponent_Author() {
		return (EReference)componentEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getComponent_CreationDate() {
		return (EAttribute)componentEClass.getEStructuralFeatures().get(5);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getComponent_DeleteDate() {
		return (EAttribute)componentEClass.getEStructuralFeatures().get(6);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getComponent_Deleted() {
		return (EAttribute)componentEClass.getEStructuralFeatures().get(7);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getComponent_FileDescriptor() {
		return (EAttribute)componentEClass.getEStructuralFeatures().get(8);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getComponent_Projects() {
		return (EReference)componentEClass.getEStructuralFeatures().get(9);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getNotationHolder() {
		return notationHolderEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getNotationHolder_NotationString() {
		return (EAttribute)notationHolderEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getUserProjectAuthorization() {
		return userProjectAuthorizationEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getUserProjectAuthorization_User() {
		return (EReference)userProjectAuthorizationEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getUserProjectAuthorization_Project() {
		return (EReference)userProjectAuthorizationEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getUserProjectAuthorization_Type() {
		return (EAttribute)userProjectAuthorizationEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getContextItem() {
		return contextItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getContextItem_Context() {
		return (EReference)contextItemEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getContextItem_DefaultContext() {
		return (EAttribute)contextItemEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSpagoBiServer() {
		return spagoBiServerEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSpagoBiServer_EngineName() {
		return (EAttribute)spagoBiServerEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSpagoBiServer_ShortDescription() {
		return (EAttribute)spagoBiServerEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSpagoBiServer_Host() {
		return (EAttribute)spagoBiServerEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSpagoBiServer_Port() {
		return (EAttribute)spagoBiServerEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSpagoBiServer_Login() {
		return (EAttribute)spagoBiServerEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSpagoBiServer_Password() {
		return (EAttribute)spagoBiServerEClass.getEStructuralFeatures().get(5);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSpagoBiServer_ApplicationContext() {
		return (EAttribute)spagoBiServerEClass.getEStructuralFeatures().get(6);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getLicense() {
		return licenseEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getLicense_License() {
		return (EAttribute)licenseEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getLicense_CustomerName() {
		return (EAttribute)licenseEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getLicense_Params() {
		return (EAttribute)licenseEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getLicense_Token() {
		return (EAttribute)licenseEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getLicense_DateTokenCheck() {
		return (EAttribute)licenseEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getGenericSchemaConnectionItem() {
		return genericSchemaConnectionItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getUserModuleAuthorization() {
		return userModuleAuthorizationEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getUserModuleAuthorization_User() {
		return (EReference)userModuleAuthorizationEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getUserModuleAuthorization_Type() {
		return (EAttribute)userModuleAuthorizationEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getLDAPSchemaConnectionItem() {
		return ldapSchemaConnectionItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSalesforceSchemaConnectionItem() {
		return salesforceSchemaConnectionItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getDashboardConnection() {
		return dashboardConnectionEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDashboardConnection_Id() {
		return (EAttribute)dashboardConnectionEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDashboardConnection_Label() {
		return (EAttribute)dashboardConnectionEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDashboardConnection_Dialect() {
		return (EAttribute)dashboardConnectionEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDashboardConnection_Host() {
		return (EAttribute)dashboardConnectionEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDashboardConnection_Port() {
		return (EAttribute)dashboardConnectionEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDashboardConnection_Database() {
		return (EAttribute)dashboardConnectionEClass.getEStructuralFeatures().get(5);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDashboardConnection_Username() {
		return (EAttribute)dashboardConnectionEClass.getEStructuralFeatures().get(6);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDashboardConnection_Password() {
		return (EAttribute)dashboardConnectionEClass.getEStructuralFeatures().get(7);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDashboardConnection_LogTable() {
		return (EAttribute)dashboardConnectionEClass.getEStructuralFeatures().get(8);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDashboardConnection_StatTable() {
		return (EAttribute)dashboardConnectionEClass.getEStructuralFeatures().get(9);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDashboardConnection_FlowMeterTable() {
		return (EAttribute)dashboardConnectionEClass.getEStructuralFeatures().get(10);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDashboardConnection_AdditionnalsParams() {
		return (EAttribute)dashboardConnectionEClass.getEStructuralFeatures().get(11);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDashboardConnection_Datasource() {
		return (EAttribute)dashboardConnectionEClass.getEStructuralFeatures().get(12);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getExecutionTriggerable() {
		return executionTriggerableEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTriggerable_Id() {
		return (EAttribute)executionTriggerableEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getExecutionTriggerable_Triggers() {
		return (EReference)executionTriggerableEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTriggerable_IdQuartzJob() {
		return (EAttribute)executionTriggerableEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTriggerable_Status() {
		return (EAttribute)executionTriggerableEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTriggerable_ErrorStatus() {
		return (EAttribute)executionTriggerableEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTriggerable_ConcurrentExecution() {
		return (EAttribute)executionTriggerableEClass.getEStructuralFeatures().get(5);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTriggerable_ProcessingState() {
		return (EAttribute)executionTriggerableEClass.getEStructuralFeatures().get(6);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTriggerable_RequestId() {
		return (EAttribute)executionTriggerableEClass.getEStructuralFeatures().get(7);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getExecutionPlan() {
		return executionPlanEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionPlan_Label() {
		return (EAttribute)executionPlanEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getExecutionPlan_ExecPlanParts() {
		return (EReference)executionPlanEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getExecutionPlan_ExecPlanPrms() {
		return (EReference)executionPlanEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionPlan_Description() {
		return (EAttribute)executionPlanEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getExecutionPlanPart() {
		return executionPlanPartEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionPlanPart_Id() {
		return (EAttribute)executionPlanPartEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getExecutionPlanPart_Task() {
		return (EReference)executionPlanPartEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getExecutionPlanPart_ExecutionPlan() {
		return (EReference)executionPlanPartEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getExecutionPlanPart_Parent() {
		return (EReference)executionPlanPartEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionPlanPart_Type() {
		return (EAttribute)executionPlanPartEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getExecutionPlanPart_JvmPrms() {
		return (EReference)executionPlanPartEClass.getEStructuralFeatures().get(5);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getExecutionPlanPart_ContextPrms() {
		return (EReference)executionPlanPartEClass.getEStructuralFeatures().get(6);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionPlanPart_Status() {
		return (EAttribute)executionPlanPartEClass.getEStructuralFeatures().get(7);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionPlanPart_StartDate() {
		return (EAttribute)executionPlanPartEClass.getEStructuralFeatures().get(8);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionPlanPart_EndDate() {
		return (EAttribute)executionPlanPartEClass.getEStructuralFeatures().get(9);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionPlanPart_RequestId() {
		return (EAttribute)executionPlanPartEClass.getEStructuralFeatures().get(10);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionPlanPart_UseParallel() {
		return (EAttribute)executionPlanPartEClass.getEStructuralFeatures().get(11);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionPlanPart_MaxThreads() {
		return (EAttribute)executionPlanPartEClass.getEStructuralFeatures().get(12);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getExecutionPlanPrm() {
		return executionPlanPrmEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionPlanPrm_Id() {
		return (EAttribute)executionPlanPrmEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getExecutionPlanPrm_ExecutionPlan() {
		return (EReference)executionPlanPrmEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionPlanPrm_Name() {
		return (EAttribute)executionPlanPrmEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionPlanPrm_Value() {
		return (EAttribute)executionPlanPrmEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getExecutionPlanPartCmdPrm() {
		return executionPlanPartCmdPrmEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionPlanPartCmdPrm_Id() {
		return (EAttribute)executionPlanPartCmdPrmEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getExecutionPlanPartCmdPrm_ExecutionPlanPart() {
		return (EReference)executionPlanPartCmdPrmEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionPlanPartCmdPrm_Name() {
		return (EAttribute)executionPlanPartCmdPrmEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionPlanPartCmdPrm_Value() {
		return (EAttribute)executionPlanPartCmdPrmEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getExecutionPlanPartJobPrm() {
		return executionPlanPartJobPrmEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionPlanPartJobPrm_Id() {
		return (EAttribute)executionPlanPartJobPrmEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getExecutionPlanPartJobPrm_ExecutionPlanPart() {
		return (EReference)executionPlanPartJobPrmEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionPlanPartJobPrm_Override() {
		return (EAttribute)executionPlanPartJobPrmEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionPlanPartJobPrm_Name() {
		return (EAttribute)executionPlanPartJobPrmEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionPlanPartJobPrm_CustomValue() {
		return (EAttribute)executionPlanPartJobPrmEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionPlanPartJobPrm_PartCustomValue() {
		return (EAttribute)executionPlanPartJobPrmEClass.getEStructuralFeatures().get(5);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getExecutionTask() {
		return executionTaskEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_Label() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_Description() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getExecutionTask_ExecutionServer() {
		return (EReference)executionTaskEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getExecutionTask_Project() {
		return (EReference)executionTaskEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_Branch() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_Context() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(5);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_JobVersion() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(6);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_RegenerateJobOnChange() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(7);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_Active() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(8);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_LastScriptGenerationDate() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(9);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_GeneratedSvnRevision() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(10);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_IdRemoteJob() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(11);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_IdRemoteJobExecution() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(12);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_ChecksumArchive() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(13);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_JobScriptArchiveFilename() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(14);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_LastRunDate() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(15);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_LastDeploymentDate() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(16);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_LastEndedRunDate() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(17);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getExecutionTask_CmdPrms() {
		return (EReference)executionTaskEClass.getEStructuralFeatures().get(18);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getExecutionTask_EsbPropertiesPrms() {
		return (EReference)executionTaskEClass.getEStructuralFeatures().get(19);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getExecutionTask_JobPrms() {
		return (EReference)executionTaskEClass.getEStructuralFeatures().get(20);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_JobId() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(21);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getExecutionTask_VirtualServer() {
		return (EReference)executionTaskEClass.getEStructuralFeatures().get(22);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_MaxConcurrentExecutions() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(23);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_GeneratedProjectName() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(24);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_GeneratedJobName() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(25);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_GeneratedJobVersion() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(26);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_ApplyContextToChildren() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(27);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_ErrorStackTrace() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(28);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_LastTriggeringDate() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(29);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_ExecStatisticsEnabled() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(30);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_AddStatisticsCodeEnabled() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(31);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_OwnerSchedulerInstanceId() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(32);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_OnUnknownStateJob() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(33);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_UseLatestVersion() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(34);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_ApplicationType() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(35);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_RepositoryName() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(36);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_FeaturesFileUrl() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(37);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_ApplicationGroup() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(40);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_BundleName() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(41);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_PropertyId() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(42);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_FeatureName() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(38);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTask_FeatureVersion() {
		return (EAttribute)executionTaskEClass.getEStructuralFeatures().get(39);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getExecutionTaskProperties() {
		return executionTaskPropertiesEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTaskProperties_Id() {
		return (EAttribute)executionTaskPropertiesEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTaskProperties_Name() {
		return (EAttribute)executionTaskPropertiesEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTaskProperties_Value() {
		return (EAttribute)executionTaskPropertiesEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getExecutionTaskProperties_ExecutionTask() {
		return (EReference)executionTaskPropertiesEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getExecutionTaskCmdPrm() {
		return executionTaskCmdPrmEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTaskCmdPrm_Id() {
		return (EAttribute)executionTaskCmdPrmEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTaskCmdPrm_Active() {
		return (EAttribute)executionTaskCmdPrmEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTaskCmdPrm_Parameter() {
		return (EAttribute)executionTaskCmdPrmEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTaskCmdPrm_Description() {
		return (EAttribute)executionTaskCmdPrmEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getExecutionTaskCmdPrm_ExecutionTask() {
		return (EReference)executionTaskCmdPrmEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getExecutionTaskJobPrm() {
		return executionTaskJobPrmEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTaskJobPrm_Id() {
		return (EAttribute)executionTaskJobPrmEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTaskJobPrm_Label() {
		return (EAttribute)executionTaskJobPrmEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTaskJobPrm_Override() {
		return (EAttribute)executionTaskJobPrmEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getExecutionTaskJobPrm_ExecutionTask() {
		return (EReference)executionTaskJobPrmEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTaskJobPrm_OriginalValue() {
		return (EAttribute)executionTaskJobPrmEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTaskJobPrm_DefaultValue() {
		return (EAttribute)executionTaskJobPrmEClass.getEStructuralFeatures().get(5);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionTaskJobPrm_ItemType() {
		return (EAttribute)executionTaskJobPrmEClass.getEStructuralFeatures().get(6);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getTaskExecutionHistory() {
		return taskExecutionHistoryEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_Id() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_BasicStatus() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_DetailedStatus() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_TaskLabel() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_TaskDescription() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_ProjectName() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(5);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_TalendJobName() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(6);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_TalendJobId() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(7);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_TalendJobVersion() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(8);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_ContextName() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(9);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_VirtualServerName() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(10);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_ExecutionServerName() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(11);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_ExecutionServerHost() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(12);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_ExecutionServerCmdPort() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(13);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_ExecutionServerFilePort() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(14);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_ExecutionServerMonitoringPort() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(15);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_ApplyContextToChildren() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(16);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_TriggeredBy() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(17);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_TriggerType() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(18);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_TriggerName() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(19);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_TriggerDescription() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(20);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_TaskErrorStackTrace() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(21);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_IdQuartzJob() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(22);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_IdQuartzTrigger() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(23);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_LastJobGenerationDate() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(24);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_JobArchiveFilename() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(25);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_FileTriggerFileMask() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(26);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_FileTriggerFileName() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(27);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_FileTriggerFolderPath() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(28);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_FileTriggerTriggeredFilePath() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(29);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_ExpectedTriggeringDate() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(30);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_TaskStartDate() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(31);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_TaskEndDate() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(32);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_ServerJobStartDate() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(33);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_ServerJobEndDate() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(34);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_IdRemoteJob() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(35);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_IdRemoteJobExecution() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(36);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_RequestId() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(37);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_ResumingMode() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(38);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_ContextValues() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(39);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_JvmValues() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(40);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_ParentTaskExecId() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(41);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTaskExecutionHistory_ParentPlanExecId() {
		return (EAttribute)taskExecutionHistoryEClass.getEStructuralFeatures().get(42);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getPlanExecutionHistory() {
		return planExecutionHistoryEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getPlanExecutionHistory_OriginalLabel() {
		return (EAttribute)planExecutionHistoryEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getPlanExecutionHistory_CurrentLabel() {
		return (EAttribute)planExecutionHistoryEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getTalendTrigger() {
		return talendTriggerEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTalendTrigger_Id() {
		return (EAttribute)talendTriggerEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTalendTrigger_Active() {
		return (EAttribute)talendTriggerEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTalendTrigger_Label() {
		return (EAttribute)talendTriggerEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTalendTrigger_Description() {
		return (EAttribute)talendTriggerEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTalendTrigger_TriggerType() {
		return (EAttribute)talendTriggerEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getTalendTrigger_ExecutionTriggerable() {
		return (EReference)talendTriggerEClass.getEStructuralFeatures().get(5);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTalendTrigger_StartTime() {
		return (EAttribute)talendTriggerEClass.getEStructuralFeatures().get(6);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTalendTrigger_EndTime() {
		return (EAttribute)talendTriggerEClass.getEStructuralFeatures().get(7);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTalendTrigger_PreviousFireTime() {
		return (EAttribute)talendTriggerEClass.getEStructuralFeatures().get(8);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTalendTrigger_FinalFireTime() {
		return (EAttribute)talendTriggerEClass.getEStructuralFeatures().get(9);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTalendTrigger_IdQuartzTrigger() {
		return (EAttribute)talendTriggerEClass.getEStructuralFeatures().get(10);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTalendTrigger_ResumePauseUpdated() {
		return (EAttribute)talendTriggerEClass.getEStructuralFeatures().get(11);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTalendTrigger_PreviouslyPaused() {
		return (EAttribute)talendTriggerEClass.getEStructuralFeatures().get(12);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getCronTalendTrigger() {
		return cronTalendTriggerEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getCronTalendTrigger_CronExpression() {
		return (EAttribute)cronTalendTriggerEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getCronUITalendTrigger() {
		return cronUITalendTriggerEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getCronUITalendTrigger_ListDaysOfWeek() {
		return (EAttribute)cronUITalendTriggerEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getCronUITalendTrigger_ListDaysOfMonth() {
		return (EAttribute)cronUITalendTriggerEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getCronUITalendTrigger_ListMonths() {
		return (EAttribute)cronUITalendTriggerEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getCronUITalendTrigger_ListYears() {
		return (EAttribute)cronUITalendTriggerEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getCronUITalendTrigger_ListHours() {
		return (EAttribute)cronUITalendTriggerEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getCronUITalendTrigger_ListMinutes() {
		return (EAttribute)cronUITalendTriggerEClass.getEStructuralFeatures().get(5);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSimpleTalendTrigger() {
		return simpleTalendTriggerEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSimpleTalendTrigger_RepeatCount() {
		return (EAttribute)simpleTalendTriggerEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSimpleTalendTrigger_RepeatInterval() {
		return (EAttribute)simpleTalendTriggerEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSimpleTalendTrigger_TimesTriggered() {
		return (EAttribute)simpleTalendTriggerEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getFileTrigger() {
		return fileTriggerEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getFileTrigger_FileTriggerMasks() {
		return (EReference)fileTriggerEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getFileTriggerMask() {
		return fileTriggerMaskEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getFileTriggerMask_Id() {
		return (EAttribute)fileTriggerMaskEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getFileTriggerMask_Active() {
		return (EAttribute)fileTriggerMaskEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getFileTriggerMask_Label() {
		return (EAttribute)fileTriggerMaskEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getFileTriggerMask_Description() {
		return (EAttribute)fileTriggerMaskEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getFileTriggerMask_FileTrigger() {
		return (EReference)fileTriggerMaskEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getFileTriggerMask_FolderPath() {
		return (EAttribute)fileTriggerMaskEClass.getEStructuralFeatures().get(5);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getFileTriggerMask_FileMask() {
		return (EAttribute)fileTriggerMaskEClass.getEStructuralFeatures().get(6);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getFileTriggerMask_ContextParameterBaseName() {
		return (EAttribute)fileTriggerMaskEClass.getEStructuralFeatures().get(7);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getFileTriggerMask_CheckFileServer() {
		return (EReference)fileTriggerMaskEClass.getEStructuralFeatures().get(8);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getFileTriggerMask_Exist() {
		return (EAttribute)fileTriggerMaskEClass.getEStructuralFeatures().get(9);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getFileTriggerMask_Created() {
		return (EAttribute)fileTriggerMaskEClass.getEStructuralFeatures().get(10);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getFileTriggerMask_Modified() {
		return (EAttribute)fileTriggerMaskEClass.getEStructuralFeatures().get(11);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getExecutionServer() {
		return executionServerEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionServer_Id() {
		return (EAttribute)executionServerEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionServer_Label() {
		return (EAttribute)executionServerEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionServer_Description() {
		return (EAttribute)executionServerEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionServer_Host() {
		return (EAttribute)executionServerEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionServer_Port() {
		return (EAttribute)executionServerEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionServer_FileTransfertPort() {
		return (EAttribute)executionServerEClass.getEStructuralFeatures().get(5);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionServer_Active() {
		return (EAttribute)executionServerEClass.getEStructuralFeatures().get(6);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionServer_MonitoringPort() {
		return (EAttribute)executionServerEClass.getEStructuralFeatures().get(7);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionServer_TimeoutUnknownState() {
		return (EAttribute)executionServerEClass.getEStructuralFeatures().get(8);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionServer_Username() {
		return (EAttribute)executionServerEClass.getEStructuralFeatures().get(9);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionServer_Password() {
		return (EAttribute)executionServerEClass.getEStructuralFeatures().get(10);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionServer_JmxUrl() {
		return (EAttribute)executionServerEClass.getEStructuralFeatures().get(11);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionServer_WebConsoleUrl() {
		return (EAttribute)executionServerEClass.getEStructuralFeatures().get(12);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionServer_TalendRuntime() {
		return (EAttribute)executionServerEClass.getEStructuralFeatures().get(13);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionServer_MgmtServerPort() {
		return (EAttribute)executionServerEClass.getEStructuralFeatures().get(14);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionServer_MgmtRegPort() {
		return (EAttribute)executionServerEClass.getEStructuralFeatures().get(15);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionServer_AdminConsolePort() {
		return (EAttribute)executionServerEClass.getEStructuralFeatures().get(16);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionServer_UseSSL() {
		return (EAttribute)executionServerEClass.getEStructuralFeatures().get(17);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionServer_Instance() {
		return (EAttribute)executionServerEClass.getEStructuralFeatures().get(18);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getExecutionVirtualServer() {
		return executionVirtualServerEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionVirtualServer_Id() {
		return (EAttribute)executionVirtualServerEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionVirtualServer_Label() {
		return (EAttribute)executionVirtualServerEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionVirtualServer_Description() {
		return (EAttribute)executionVirtualServerEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExecutionVirtualServer_Active() {
		return (EAttribute)executionVirtualServerEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getExecutionVirtualServer_ExecutionServers() {
		return (EReference)executionVirtualServerEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getJobletProcessItem() {
		return jobletProcessItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getJobletProcessItem_JobletProcess() {
		return (EReference)jobletProcessItemEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getJobletProcessItem_Icon() {
		return (EReference)jobletProcessItemEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getJobDocumentationItem() {
		return jobDocumentationItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getJobletDocumentationItem() {
		return jobletDocumentationItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getWSDLSchemaConnectionItem() {
		return wsdlSchemaConnectionItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSchemaInformation() {
		return schemaInformationEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSchemaInformation_Version() {
		return (EAttribute)schemaInformationEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getInformation() {
		return informationEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getInformation_Level() {
		return (EAttribute)informationEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getInformation_Type() {
		return (EAttribute)informationEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getInformation_Text() {
		return (EAttribute)informationEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSQLPatternItem() {
		return sqlPatternItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSQLPatternItem_System() {
		return (EAttribute)sqlPatternItemEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSQLPatternItem_EltName() {
		return (EAttribute)sqlPatternItemEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getComponentSetting() {
		return componentSettingEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getComponentSetting_Name() {
		return (EAttribute)componentSettingEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getComponentSetting_Hidden() {
		return (EAttribute)componentSettingEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getComponentSetting_Family() {
		return (EAttribute)componentSettingEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getStatAndLogsSettings() {
		return statAndLogsSettingsEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getStatAndLogsSettings_Parameters() {
		return (EReference)statAndLogsSettingsEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getImplicitContextSettings() {
		return implicitContextSettingsEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getImplicitContextSettings_Parameters() {
		return (EReference)implicitContextSettingsEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSoaOperation() {
		return soaOperationEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaOperation_Id() {
		return (EAttribute)soaOperationEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaOperation_Label() {
		return (EAttribute)soaOperationEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaOperation_Description() {
		return (EAttribute)soaOperationEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSoaOperation_Project() {
		return (EReference)soaOperationEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaOperation_Context() {
		return (EAttribute)soaOperationEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaOperation_JobVersion() {
		return (EAttribute)soaOperationEClass.getEStructuralFeatures().get(5);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaOperation_JobName() {
		return (EAttribute)soaOperationEClass.getEStructuralFeatures().get(6);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaOperation_Active() {
		return (EAttribute)soaOperationEClass.getEStructuralFeatures().get(7);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaOperation_LastScriptGenerationDate() {
		return (EAttribute)soaOperationEClass.getEStructuralFeatures().get(8);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaOperation_JobId() {
		return (EAttribute)soaOperationEClass.getEStructuralFeatures().get(9);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaOperation_ApplyContextToChildren() {
		return (EAttribute)soaOperationEClass.getEStructuralFeatures().get(10);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSoaOperation_InputParameters() {
		return (EReference)soaOperationEClass.getEStructuralFeatures().get(11);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaOperation_JvmParameters() {
		return (EAttribute)soaOperationEClass.getEStructuralFeatures().get(12);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaOperation_Xms() {
		return (EAttribute)soaOperationEClass.getEStructuralFeatures().get(13);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaOperation_Xmx() {
		return (EAttribute)soaOperationEClass.getEStructuralFeatures().get(14);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaOperation_MinJobInstances() {
		return (EAttribute)soaOperationEClass.getEStructuralFeatures().get(15);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaOperation_MaxJobInstances() {
		return (EAttribute)soaOperationEClass.getEStructuralFeatures().get(16);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaOperation_IdleTTL_forAllInstances() {
		return (EAttribute)soaOperationEClass.getEStructuralFeatures().get(17);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaOperation_IdleTTL_forAdditionalInstances() {
		return (EAttribute)soaOperationEClass.getEStructuralFeatures().get(18);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaOperation_QueueMaxSize() {
		return (EAttribute)soaOperationEClass.getEStructuralFeatures().get(19);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaOperation_RequestInQueueTTL() {
		return (EAttribute)soaOperationEClass.getEStructuralFeatures().get(20);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSoaOperation_Service() {
		return (EReference)soaOperationEClass.getEStructuralFeatures().get(21);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaOperation_ReturnStyle() {
		return (EAttribute)soaOperationEClass.getEStructuralFeatures().get(22);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaOperation_Branch() {
		return (EAttribute)soaOperationEClass.getEStructuralFeatures().get(23);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSoaInputParameter() {
		return soaInputParameterEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaInputParameter_Id() {
		return (EAttribute)soaInputParameterEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaInputParameter_Label() {
		return (EAttribute)soaInputParameterEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSoaInputParameter_Operation() {
		return (EReference)soaInputParameterEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaInputParameter_DefaultValue() {
		return (EAttribute)soaInputParameterEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaInputParameter_ExposedName() {
		return (EAttribute)soaInputParameterEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaInputParameter_Exposed() {
		return (EAttribute)soaInputParameterEClass.getEStructuralFeatures().get(5);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSoaService() {
		return soaServiceEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaService_Id() {
		return (EAttribute)soaServiceEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaService_Label() {
		return (EAttribute)soaServiceEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaService_NameSpace() {
		return (EAttribute)soaServiceEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaService_Contact() {
		return (EAttribute)soaServiceEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaService_Description() {
		return (EAttribute)soaServiceEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaService_Creation() {
		return (EAttribute)soaServiceEClass.getEStructuralFeatures().get(5);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaService_Modification() {
		return (EAttribute)soaServiceEClass.getEStructuralFeatures().get(6);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaService_Port() {
		return (EAttribute)soaServiceEClass.getEStructuralFeatures().get(7);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaService_Type() {
		return (EAttribute)soaServiceEClass.getEStructuralFeatures().get(8);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaService_Style() {
		return (EAttribute)soaServiceEClass.getEStructuralFeatures().get(9);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaService_UsedType() {
		return (EAttribute)soaServiceEClass.getEStructuralFeatures().get(10);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaService_ParamStyle() {
		return (EAttribute)soaServiceEClass.getEStructuralFeatures().get(11);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSoaService_Operations() {
		return (EReference)soaServiceEClass.getEStructuralFeatures().get(12);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSoaService_Status() {
		return (EAttribute)soaServiceEClass.getEStructuralFeatures().get(13);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getRulesItem() {
		return rulesItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getUserRight() {
		return userRightEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getUserRight_Id() {
		return (EAttribute)userRightEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getUserRight_Name() {
		return (EAttribute)userRightEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getUserRight_Description() {
		return (EAttribute)userRightEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getUserRight_RolesRights() {
		return (EReference)userRightEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getRoleRight() {
		return roleRightEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getRoleRight_Role() {
		return (EReference)roleRightEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getRoleRight_UserRight() {
		return (EReference)roleRightEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSVGBusinessProcessItem() {
		return svgBusinessProcessItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSVGBusinessProcessItem_BusinessProcessItem() {
		return (EReference)svgBusinessProcessItemEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getLinkRulesItem() {
		return linkRulesItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getLinkRulesItem_Name() {
		return (EAttribute)linkRulesItemEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getLinkRulesItem_Extension() {
		return (EAttribute)linkRulesItemEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getLinkRulesItem_Link() {
		return (EReference)linkRulesItemEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getItemRelations() {
		return itemRelationsEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getItemRelations_BaseItem() {
		return (EReference)itemRelationsEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getItemRelations_RelatedItems() {
		return (EReference)itemRelationsEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getItemRelation() {
		return itemRelationEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getItemRelation_Id() {
		return (EAttribute)itemRelationEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getItemRelation_Version() {
		return (EAttribute)itemRelationEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getItemRelation_Type() {
		return (EAttribute)itemRelationEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getNotification() {
		return notificationEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getNotification_Id() {
		return (EAttribute)notificationEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getNotification_Type() {
		return (EAttribute)notificationEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getNotification_Props() {
		return (EAttribute)notificationEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getNotification_Enabled() {
		return (EAttribute)notificationEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getHL7ConnectionItem() {
		return hl7ConnectionItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getUserRoleReference() {
		return userRoleReferenceEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getUserRoleReference_User() {
		return (EReference)userRoleReferenceEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getUserRoleReference_Role() {
		return (EReference)userRoleReferenceEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getHeaderFooterConnectionItem() {
		return headerFooterConnectionItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getFTPConnectionItem() {
		return ftpConnectionItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getCustomComponentSetting() {
		return customComponentSettingEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getCustomComponentSetting_Name() {
		return (EAttribute)customComponentSettingEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getCustomComponentSetting_Share() {
		return (EAttribute)customComponentSettingEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getTDQItem() {
		return tdqItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTDQItem_Filename() {
		return (EAttribute)tdqItemEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getBRMSConnectionItem() {
		return brmsConnectionItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getJobScriptItem() {
		return jobScriptItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getEDIFACTConnectionItem() {
		return edifactConnectionItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getTechnicalVariable() {
		return technicalVariableEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTechnicalVariable_Key() {
		return (EAttribute)technicalVariableEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTechnicalVariable_Value() {
		return (EAttribute)technicalVariableEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getReferenceItem() {
		return referenceItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getReferenceItem_State() {
		return (EReference)referenceItemEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getReferenceItem_Parent() {
		return (EReference)referenceItemEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getReferenceItem_ReloadFromFile() {
		return (EAttribute)referenceItemEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getExchangeUser() {
		return exchangeUserEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExchangeUser_Username() {
		return (EAttribute)exchangeUserEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExchangeUser_Login() {
		return (EAttribute)exchangeUserEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExchangeUser_Password() {
		return (EAttribute)exchangeUserEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getArtifactNotification() {
		return artifactNotificationEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getArtifactNotification_Name() {
		return (EAttribute)artifactNotificationEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getAdditionalInfoMap() {
		return additionalInfoMapEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getAdditionalInfoMap_Key() {
		return (EAttribute)additionalInfoMapEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getAdditionalInfoMap_Value() {
		return (EAttribute)additionalInfoMapEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getMigrationTask() {
		return migrationTaskEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getMigrationTask_Id() {
		return (EAttribute)migrationTaskEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getMigrationTask_Breaks() {
		return (EAttribute)migrationTaskEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getMigrationTask_Version() {
		return (EAttribute)migrationTaskEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getMigrationTask_Status() {
		return (EAttribute)migrationTaskEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getPigudfItem() {
		return pigudfItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getReferenceFileItem() {
		return referenceFileItemEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getReferenceFileItem_Name() {
		return (EAttribute)referenceFileItemEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getReferenceFileItem_Extension() {
		return (EAttribute)referenceFileItemEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getReferenceFileItem_Content() {
		return (EReference)referenceFileItemEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EEnum getFolderType() {
		return folderTypeEEnum;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EEnum getUserProjectAuthorizationType() {
		return userProjectAuthorizationTypeEEnum;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EEnum getUserModuleAuthorizationType() {
		return userModuleAuthorizationTypeEEnum;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EEnum getInformationLevel() {
		return informationLevelEEnum;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EEnum getMigrationStatus() {
		return migrationStatusEEnum;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public PropertiesFactory getPropertiesFactory() {
		return (PropertiesFactory)getEFactoryInstance();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private boolean isCreated = false;

    /**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		projectEClass = createEClass(PROJECT);
		createEReference(projectEClass, PROJECT__TECHNICAL_STATUS);
		createEReference(projectEClass, PROJECT__DOCUMENTATION_STATUS);
		createEAttribute(projectEClass, PROJECT__ID);
		createEAttribute(projectEClass, PROJECT__LABEL);
		createEAttribute(projectEClass, PROJECT__DESCRIPTION);
		createEAttribute(projectEClass, PROJECT__LANGUAGE);
		createEAttribute(projectEClass, PROJECT__TECHNICAL_LABEL);
		createEAttribute(projectEClass, PROJECT__LOCAL);
		createEReference(projectEClass, PROJECT__FOLDERS);
		createEAttribute(projectEClass, PROJECT__DELETED);
		createEAttribute(projectEClass, PROJECT__DELETE_DATE);
		createEAttribute(projectEClass, PROJECT__CREATION_DATE);
		createEReference(projectEClass, PROJECT__AUTHOR);
		createEReference(projectEClass, PROJECT__USER_AUTHORIZATION);
		createEReference(projectEClass, PROJECT__ALLOWED_COMPONENTS);
		createEReference(projectEClass, PROJECT__REFERENCED_PROJECTS);
		createEReference(projectEClass, PROJECT__AVAILABLE_REF_PROJECT);
		createEAttribute(projectEClass, PROJECT__MASTER_JOB_ID);
		createEReference(projectEClass, PROJECT__SPAGO_BI_SERVER);
		createEAttribute(projectEClass, PROJECT__PRODUCT_VERSION);
		createEReference(projectEClass, PROJECT__COMPONENTS_SETTINGS);
		createEAttribute(projectEClass, PROJECT__URL);
		createEReference(projectEClass, PROJECT__STAT_AND_LOGS_SETTINGS);
		createEReference(projectEClass, PROJECT__IMPLICIT_CONTEXT_SETTINGS);
		createEAttribute(projectEClass, PROJECT__HIDE_PASSWORD);
		createEReference(projectEClass, PROJECT__ITEMS_RELATIONS);
		createEAttribute(projectEClass, PROJECT__REFERENCE);
		createEReference(projectEClass, PROJECT__CUSTOM_COMPONENT_SETTINGS);
		createEAttribute(projectEClass, PROJECT__DELETED_FOLDERS);
		createEAttribute(projectEClass, PROJECT__TYPE);
		createEAttribute(projectEClass, PROJECT__ITEMS_RELATION_VERSION);
		createEReference(projectEClass, PROJECT__EXCHANGE_USER);
		createEAttribute(projectEClass, PROJECT__MIGRATION_TASKS);
		createEReference(projectEClass, PROJECT__MIGRATION_TASK);
		createEAttribute(projectEClass, PROJECT__BIG_DATA);

		projectComponentAuthorisationEClass = createEClass(PROJECT_COMPONENT_AUTHORISATION);
		createEReference(projectComponentAuthorisationEClass, PROJECT_COMPONENT_AUTHORISATION__PROJECT);
		createEReference(projectComponentAuthorisationEClass, PROJECT_COMPONENT_AUTHORISATION__COMPONENT);

		projectReferenceEClass = createEClass(PROJECT_REFERENCE);
		createEReference(projectReferenceEClass, PROJECT_REFERENCE__PROJECT);
		createEAttribute(projectReferenceEClass, PROJECT_REFERENCE__BRANCH);
		createEReference(projectReferenceEClass, PROJECT_REFERENCE__REFERENCED_PROJECT);
		createEAttribute(projectReferenceEClass, PROJECT_REFERENCE__REFERENCED_BRANCH);

		statusEClass = createEClass(STATUS);
		createEAttribute(statusEClass, STATUS__LABEL);
		createEAttribute(statusEClass, STATUS__CODE);

		itemStateEClass = createEClass(ITEM_STATE);
		createEAttribute(itemStateEClass, ITEM_STATE__PATH);
		createEAttribute(itemStateEClass, ITEM_STATE__DELETED);
		createEAttribute(itemStateEClass, ITEM_STATE__LOCKED);
		createEReference(itemStateEClass, ITEM_STATE__LOCKER);
		createEAttribute(itemStateEClass, ITEM_STATE__LOCK_DATE);
		createEAttribute(itemStateEClass, ITEM_STATE__COMMIT_DATE);

		propertyEClass = createEClass(PROPERTY);
		createEAttribute(propertyEClass, PROPERTY__ID);
		createEAttribute(propertyEClass, PROPERTY__LABEL);
		createEAttribute(propertyEClass, PROPERTY__PURPOSE);
		createEAttribute(propertyEClass, PROPERTY__DESCRIPTION);
		createEAttribute(propertyEClass, PROPERTY__CREATION_DATE);
		createEAttribute(propertyEClass, PROPERTY__MODIFICATION_DATE);
		createEAttribute(propertyEClass, PROPERTY__VERSION);
		createEAttribute(propertyEClass, PROPERTY__STATUS_CODE);
		createEReference(propertyEClass, PROPERTY__ITEM);
		createEReference(propertyEClass, PROPERTY__AUTHOR);
		createEReference(propertyEClass, PROPERTY__INFORMATIONS);
		createEAttribute(propertyEClass, PROPERTY__MAX_INFORMATION_LEVEL);
		createEAttribute(propertyEClass, PROPERTY__OLD_STATUS_CODE);
		createEReference(propertyEClass, PROPERTY__ADDITIONAL_PROPERTIES);
		createEAttribute(propertyEClass, PROPERTY__DISPLAY_NAME);

		itemEClass = createEClass(ITEM);
		createEReference(itemEClass, ITEM__PROPERTY);
		createEReference(itemEClass, ITEM__STATE);
		createEReference(itemEClass, ITEM__PARENT);
		createEReference(itemEClass, ITEM__REFERENCE_RESOURCES);
		createEAttribute(itemEClass, ITEM__FILE_EXTENSION);
		createEAttribute(itemEClass, ITEM__NEED_VERSION);

		linkDocumentationItemEClass = createEClass(LINK_DOCUMENTATION_ITEM);
		createEAttribute(linkDocumentationItemEClass, LINK_DOCUMENTATION_ITEM__NAME);
		createEAttribute(linkDocumentationItemEClass, LINK_DOCUMENTATION_ITEM__EXTENSION);
		createEReference(linkDocumentationItemEClass, LINK_DOCUMENTATION_ITEM__LINK);

		linkTypeEClass = createEClass(LINK_TYPE);
		createEAttribute(linkTypeEClass, LINK_TYPE__URI);
		createEAttribute(linkTypeEClass, LINK_TYPE__STATE);

		businessProcessItemEClass = createEClass(BUSINESS_PROCESS_ITEM);
		createEReference(businessProcessItemEClass, BUSINESS_PROCESS_ITEM__NOTATION);
		createEReference(businessProcessItemEClass, BUSINESS_PROCESS_ITEM__SEMANTIC);
		createEReference(businessProcessItemEClass, BUSINESS_PROCESS_ITEM__NOTATION_HOLDER);
		createEReference(businessProcessItemEClass, BUSINESS_PROCESS_ITEM__SVG_BUSINESS_PROCESS_ITEM);

		fileItemEClass = createEClass(FILE_ITEM);
		createEAttribute(fileItemEClass, FILE_ITEM__NAME);
		createEAttribute(fileItemEClass, FILE_ITEM__EXTENSION);
		createEReference(fileItemEClass, FILE_ITEM__CONTENT);

		byteArrayEClass = createEClass(BYTE_ARRAY);
		createEAttribute(byteArrayEClass, BYTE_ARRAY__INNER_CONTENT);

		documentationItemEClass = createEClass(DOCUMENTATION_ITEM);

		routineItemEClass = createEClass(ROUTINE_ITEM);
		createEAttribute(routineItemEClass, ROUTINE_ITEM__BUILT_IN);
		createEReference(routineItemEClass, ROUTINE_ITEM__IMPORTS);
		createEAttribute(routineItemEClass, ROUTINE_ITEM__PACKAGE_TYPE);

		connectionItemEClass = createEClass(CONNECTION_ITEM);
		createEReference(connectionItemEClass, CONNECTION_ITEM__CONNECTION);

		snippetVariableEClass = createEClass(SNIPPET_VARIABLE);
		createEAttribute(snippetVariableEClass, SNIPPET_VARIABLE__NAME);
		createEAttribute(snippetVariableEClass, SNIPPET_VARIABLE__VALUE);
		createEAttribute(snippetVariableEClass, SNIPPET_VARIABLE__DESCRIPTION);
		createEAttribute(snippetVariableEClass, SNIPPET_VARIABLE__ID);

		snippetItemEClass = createEClass(SNIPPET_ITEM);
		createEAttribute(snippetItemEClass, SNIPPET_ITEM__NAME);
		createEAttribute(snippetItemEClass, SNIPPET_ITEM__CONTENT);
		createEReference(snippetItemEClass, SNIPPET_ITEM__VARIABLES);

		delimitedFileConnectionItemEClass = createEClass(DELIMITED_FILE_CONNECTION_ITEM);

		positionalFileConnectionItemEClass = createEClass(POSITIONAL_FILE_CONNECTION_ITEM);

		regExFileConnectionItemEClass = createEClass(REG_EX_FILE_CONNECTION_ITEM);

		csvFileConnectionItemEClass = createEClass(CSV_FILE_CONNECTION_ITEM);

		databaseConnectionItemEClass = createEClass(DATABASE_CONNECTION_ITEM);

		sapConnectionItemEClass = createEClass(SAP_CONNECTION_ITEM);

		xmlFileConnectionItemEClass = createEClass(XML_FILE_CONNECTION_ITEM);

		ldifFileConnectionItemEClass = createEClass(LDIF_FILE_CONNECTION_ITEM);

		excelFileConnectionItemEClass = createEClass(EXCEL_FILE_CONNECTION_ITEM);

		ebcdicConnectionItemEClass = createEClass(EBCDIC_CONNECTION_ITEM);

		mdmConnectionItemEClass = createEClass(MDM_CONNECTION_ITEM);

		validationRulesConnectionItemEClass = createEClass(VALIDATION_RULES_CONNECTION_ITEM);

		processItemEClass = createEClass(PROCESS_ITEM);
		createEReference(processItemEClass, PROCESS_ITEM__PROCESS);

		userRoleEClass = createEClass(USER_ROLE);
		createEAttribute(userRoleEClass, USER_ROLE__ID);
		createEAttribute(userRoleEClass, USER_ROLE__NAME);
		createEAttribute(userRoleEClass, USER_ROLE__LOCALIZED_LABEL);
		createEAttribute(userRoleEClass, USER_ROLE__FIXED);
		createEReference(userRoleEClass, USER_ROLE__ROLES_RIGHTS);

		userEClass = createEClass(USER);
		createEAttribute(userEClass, USER__ID);
		createEAttribute(userEClass, USER__LOGIN);
		createEAttribute(userEClass, USER__PASSWORD);
		createEAttribute(userEClass, USER__FIRST_NAME);
		createEAttribute(userEClass, USER__LAST_NAME);
		createEAttribute(userEClass, USER__CREATION_DATE);
		createEAttribute(userEClass, USER__DELETE_DATE);
		createEAttribute(userEClass, USER__DELETED);
		createEAttribute(userEClass, USER__ALLOWED_TO_MODIFY_COMPONENTS);
		createEAttribute(userEClass, USER__COMMENT);
		createEReference(userEClass, USER__PROJECT_AUTHORIZATION);
		createEReference(userEClass, USER__MODULE_AUTHORIZATION);
		createEReference(userEClass, USER__PREFERRED_DASHBOARD_CONNECTION);
		createEAttribute(userEClass, USER__LAST_ADMIN_CONNECTION_DATE);
		createEAttribute(userEClass, USER__LAST_STUDIO_CONNECTION_DATE);
		createEAttribute(userEClass, USER__FIRST_ADMIN_CONNECTION_DATE);
		createEAttribute(userEClass, USER__FIRST_STUDIO_CONNECTION_DATE);
		createEAttribute(userEClass, USER__ADMIN_CONNEXION_NUMBER);
		createEAttribute(userEClass, USER__STUDIO_CONNEXION_NUMBER);
		createEAttribute(userEClass, USER__AUTHENTICATION_INFO);
		createEAttribute(userEClass, USER__LDAP_LOGIN);
		createEAttribute(userEClass, USER__LDAP_ID);
		createEAttribute(userEClass, USER__LANGUAGE);
		createEAttribute(userEClass, USER__TYPE);
		createEAttribute(userEClass, USER__ADDITIONNAL_DATA);
		createEAttribute(userEClass, USER__TECHNICAL);

		folderItemEClass = createEClass(FOLDER_ITEM);
		createEReference(folderItemEClass, FOLDER_ITEM__CHILDREN);
		createEAttribute(folderItemEClass, FOLDER_ITEM__TYPE);

		componentEClass = createEClass(COMPONENT);
		createEAttribute(componentEClass, COMPONENT__ID);
		createEAttribute(componentEClass, COMPONENT__LABEL);
		createEAttribute(componentEClass, COMPONENT__VERSION);
		createEAttribute(componentEClass, COMPONENT__LAST_UPDATE_DATE);
		createEReference(componentEClass, COMPONENT__AUTHOR);
		createEAttribute(componentEClass, COMPONENT__CREATION_DATE);
		createEAttribute(componentEClass, COMPONENT__DELETE_DATE);
		createEAttribute(componentEClass, COMPONENT__DELETED);
		createEAttribute(componentEClass, COMPONENT__FILE_DESCRIPTOR);
		createEReference(componentEClass, COMPONENT__PROJECTS);

		notationHolderEClass = createEClass(NOTATION_HOLDER);
		createEAttribute(notationHolderEClass, NOTATION_HOLDER__NOTATION_STRING);

		userProjectAuthorizationEClass = createEClass(USER_PROJECT_AUTHORIZATION);
		createEReference(userProjectAuthorizationEClass, USER_PROJECT_AUTHORIZATION__USER);
		createEReference(userProjectAuthorizationEClass, USER_PROJECT_AUTHORIZATION__PROJECT);
		createEAttribute(userProjectAuthorizationEClass, USER_PROJECT_AUTHORIZATION__TYPE);

		contextItemEClass = createEClass(CONTEXT_ITEM);
		createEReference(contextItemEClass, CONTEXT_ITEM__CONTEXT);
		createEAttribute(contextItemEClass, CONTEXT_ITEM__DEFAULT_CONTEXT);

		spagoBiServerEClass = createEClass(SPAGO_BI_SERVER);
		createEAttribute(spagoBiServerEClass, SPAGO_BI_SERVER__ENGINE_NAME);
		createEAttribute(spagoBiServerEClass, SPAGO_BI_SERVER__SHORT_DESCRIPTION);
		createEAttribute(spagoBiServerEClass, SPAGO_BI_SERVER__HOST);
		createEAttribute(spagoBiServerEClass, SPAGO_BI_SERVER__PORT);
		createEAttribute(spagoBiServerEClass, SPAGO_BI_SERVER__LOGIN);
		createEAttribute(spagoBiServerEClass, SPAGO_BI_SERVER__PASSWORD);
		createEAttribute(spagoBiServerEClass, SPAGO_BI_SERVER__APPLICATION_CONTEXT);

		licenseEClass = createEClass(LICENSE);
		createEAttribute(licenseEClass, LICENSE__LICENSE);
		createEAttribute(licenseEClass, LICENSE__CUSTOMER_NAME);
		createEAttribute(licenseEClass, LICENSE__PARAMS);
		createEAttribute(licenseEClass, LICENSE__TOKEN);
		createEAttribute(licenseEClass, LICENSE__DATE_TOKEN_CHECK);

		genericSchemaConnectionItemEClass = createEClass(GENERIC_SCHEMA_CONNECTION_ITEM);

		userModuleAuthorizationEClass = createEClass(USER_MODULE_AUTHORIZATION);
		createEReference(userModuleAuthorizationEClass, USER_MODULE_AUTHORIZATION__USER);
		createEAttribute(userModuleAuthorizationEClass, USER_MODULE_AUTHORIZATION__TYPE);

		ldapSchemaConnectionItemEClass = createEClass(LDAP_SCHEMA_CONNECTION_ITEM);

		salesforceSchemaConnectionItemEClass = createEClass(SALESFORCE_SCHEMA_CONNECTION_ITEM);

		dashboardConnectionEClass = createEClass(DASHBOARD_CONNECTION);
		createEAttribute(dashboardConnectionEClass, DASHBOARD_CONNECTION__ID);
		createEAttribute(dashboardConnectionEClass, DASHBOARD_CONNECTION__LABEL);
		createEAttribute(dashboardConnectionEClass, DASHBOARD_CONNECTION__DIALECT);
		createEAttribute(dashboardConnectionEClass, DASHBOARD_CONNECTION__HOST);
		createEAttribute(dashboardConnectionEClass, DASHBOARD_CONNECTION__PORT);
		createEAttribute(dashboardConnectionEClass, DASHBOARD_CONNECTION__DATABASE);
		createEAttribute(dashboardConnectionEClass, DASHBOARD_CONNECTION__USERNAME);
		createEAttribute(dashboardConnectionEClass, DASHBOARD_CONNECTION__PASSWORD);
		createEAttribute(dashboardConnectionEClass, DASHBOARD_CONNECTION__LOG_TABLE);
		createEAttribute(dashboardConnectionEClass, DASHBOARD_CONNECTION__STAT_TABLE);
		createEAttribute(dashboardConnectionEClass, DASHBOARD_CONNECTION__FLOW_METER_TABLE);
		createEAttribute(dashboardConnectionEClass, DASHBOARD_CONNECTION__ADDITIONNALS_PARAMS);
		createEAttribute(dashboardConnectionEClass, DASHBOARD_CONNECTION__DATASOURCE);

		executionTriggerableEClass = createEClass(EXECUTION_TRIGGERABLE);
		createEAttribute(executionTriggerableEClass, EXECUTION_TRIGGERABLE__ID);
		createEReference(executionTriggerableEClass, EXECUTION_TRIGGERABLE__TRIGGERS);
		createEAttribute(executionTriggerableEClass, EXECUTION_TRIGGERABLE__ID_QUARTZ_JOB);
		createEAttribute(executionTriggerableEClass, EXECUTION_TRIGGERABLE__STATUS);
		createEAttribute(executionTriggerableEClass, EXECUTION_TRIGGERABLE__ERROR_STATUS);
		createEAttribute(executionTriggerableEClass, EXECUTION_TRIGGERABLE__CONCURRENT_EXECUTION);
		createEAttribute(executionTriggerableEClass, EXECUTION_TRIGGERABLE__PROCESSING_STATE);
		createEAttribute(executionTriggerableEClass, EXECUTION_TRIGGERABLE__REQUEST_ID);

		executionPlanEClass = createEClass(EXECUTION_PLAN);
		createEAttribute(executionPlanEClass, EXECUTION_PLAN__LABEL);
		createEReference(executionPlanEClass, EXECUTION_PLAN__EXEC_PLAN_PARTS);
		createEReference(executionPlanEClass, EXECUTION_PLAN__EXEC_PLAN_PRMS);
		createEAttribute(executionPlanEClass, EXECUTION_PLAN__DESCRIPTION);

		executionPlanPartEClass = createEClass(EXECUTION_PLAN_PART);
		createEAttribute(executionPlanPartEClass, EXECUTION_PLAN_PART__ID);
		createEReference(executionPlanPartEClass, EXECUTION_PLAN_PART__TASK);
		createEReference(executionPlanPartEClass, EXECUTION_PLAN_PART__EXECUTION_PLAN);
		createEReference(executionPlanPartEClass, EXECUTION_PLAN_PART__PARENT);
		createEAttribute(executionPlanPartEClass, EXECUTION_PLAN_PART__TYPE);
		createEReference(executionPlanPartEClass, EXECUTION_PLAN_PART__JVM_PRMS);
		createEReference(executionPlanPartEClass, EXECUTION_PLAN_PART__CONTEXT_PRMS);
		createEAttribute(executionPlanPartEClass, EXECUTION_PLAN_PART__STATUS);
		createEAttribute(executionPlanPartEClass, EXECUTION_PLAN_PART__START_DATE);
		createEAttribute(executionPlanPartEClass, EXECUTION_PLAN_PART__END_DATE);
		createEAttribute(executionPlanPartEClass, EXECUTION_PLAN_PART__REQUEST_ID);
		createEAttribute(executionPlanPartEClass, EXECUTION_PLAN_PART__USE_PARALLEL);
		createEAttribute(executionPlanPartEClass, EXECUTION_PLAN_PART__MAX_THREADS);

		executionPlanPrmEClass = createEClass(EXECUTION_PLAN_PRM);
		createEAttribute(executionPlanPrmEClass, EXECUTION_PLAN_PRM__ID);
		createEReference(executionPlanPrmEClass, EXECUTION_PLAN_PRM__EXECUTION_PLAN);
		createEAttribute(executionPlanPrmEClass, EXECUTION_PLAN_PRM__NAME);
		createEAttribute(executionPlanPrmEClass, EXECUTION_PLAN_PRM__VALUE);

		executionPlanPartCmdPrmEClass = createEClass(EXECUTION_PLAN_PART_CMD_PRM);
		createEAttribute(executionPlanPartCmdPrmEClass, EXECUTION_PLAN_PART_CMD_PRM__ID);
		createEReference(executionPlanPartCmdPrmEClass, EXECUTION_PLAN_PART_CMD_PRM__EXECUTION_PLAN_PART);
		createEAttribute(executionPlanPartCmdPrmEClass, EXECUTION_PLAN_PART_CMD_PRM__NAME);
		createEAttribute(executionPlanPartCmdPrmEClass, EXECUTION_PLAN_PART_CMD_PRM__VALUE);

		executionPlanPartJobPrmEClass = createEClass(EXECUTION_PLAN_PART_JOB_PRM);
		createEAttribute(executionPlanPartJobPrmEClass, EXECUTION_PLAN_PART_JOB_PRM__ID);
		createEReference(executionPlanPartJobPrmEClass, EXECUTION_PLAN_PART_JOB_PRM__EXECUTION_PLAN_PART);
		createEAttribute(executionPlanPartJobPrmEClass, EXECUTION_PLAN_PART_JOB_PRM__OVERRIDE);
		createEAttribute(executionPlanPartJobPrmEClass, EXECUTION_PLAN_PART_JOB_PRM__NAME);
		createEAttribute(executionPlanPartJobPrmEClass, EXECUTION_PLAN_PART_JOB_PRM__CUSTOM_VALUE);
		createEAttribute(executionPlanPartJobPrmEClass, EXECUTION_PLAN_PART_JOB_PRM__PART_CUSTOM_VALUE);

		executionTaskEClass = createEClass(EXECUTION_TASK);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__LABEL);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__DESCRIPTION);
		createEReference(executionTaskEClass, EXECUTION_TASK__EXECUTION_SERVER);
		createEReference(executionTaskEClass, EXECUTION_TASK__PROJECT);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__BRANCH);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__CONTEXT);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__JOB_VERSION);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__REGENERATE_JOB_ON_CHANGE);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__ACTIVE);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__LAST_SCRIPT_GENERATION_DATE);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__GENERATED_SVN_REVISION);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__ID_REMOTE_JOB);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__ID_REMOTE_JOB_EXECUTION);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__CHECKSUM_ARCHIVE);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__JOB_SCRIPT_ARCHIVE_FILENAME);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__LAST_RUN_DATE);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__LAST_DEPLOYMENT_DATE);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__LAST_ENDED_RUN_DATE);
		createEReference(executionTaskEClass, EXECUTION_TASK__CMD_PRMS);
		createEReference(executionTaskEClass, EXECUTION_TASK__ESB_PROPERTIES_PRMS);
		createEReference(executionTaskEClass, EXECUTION_TASK__JOB_PRMS);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__JOB_ID);
		createEReference(executionTaskEClass, EXECUTION_TASK__VIRTUAL_SERVER);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__MAX_CONCURRENT_EXECUTIONS);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__GENERATED_PROJECT_NAME);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__GENERATED_JOB_NAME);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__GENERATED_JOB_VERSION);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__APPLY_CONTEXT_TO_CHILDREN);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__ERROR_STACK_TRACE);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__LAST_TRIGGERING_DATE);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__EXEC_STATISTICS_ENABLED);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__ADD_STATISTICS_CODE_ENABLED);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__OWNER_SCHEDULER_INSTANCE_ID);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__ON_UNKNOWN_STATE_JOB);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__USE_LATEST_VERSION);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__APPLICATION_TYPE);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__REPOSITORY_NAME);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__FEATURES_FILE_URL);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__FEATURE_NAME);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__FEATURE_VERSION);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__APPLICATION_GROUP);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__BUNDLE_NAME);
		createEAttribute(executionTaskEClass, EXECUTION_TASK__PROPERTY_ID);

		executionTaskPropertiesEClass = createEClass(EXECUTION_TASK_PROPERTIES);
		createEAttribute(executionTaskPropertiesEClass, EXECUTION_TASK_PROPERTIES__ID);
		createEAttribute(executionTaskPropertiesEClass, EXECUTION_TASK_PROPERTIES__NAME);
		createEAttribute(executionTaskPropertiesEClass, EXECUTION_TASK_PROPERTIES__VALUE);
		createEReference(executionTaskPropertiesEClass, EXECUTION_TASK_PROPERTIES__EXECUTION_TASK);

		executionTaskCmdPrmEClass = createEClass(EXECUTION_TASK_CMD_PRM);
		createEAttribute(executionTaskCmdPrmEClass, EXECUTION_TASK_CMD_PRM__ID);
		createEAttribute(executionTaskCmdPrmEClass, EXECUTION_TASK_CMD_PRM__ACTIVE);
		createEAttribute(executionTaskCmdPrmEClass, EXECUTION_TASK_CMD_PRM__PARAMETER);
		createEAttribute(executionTaskCmdPrmEClass, EXECUTION_TASK_CMD_PRM__DESCRIPTION);
		createEReference(executionTaskCmdPrmEClass, EXECUTION_TASK_CMD_PRM__EXECUTION_TASK);

		executionTaskJobPrmEClass = createEClass(EXECUTION_TASK_JOB_PRM);
		createEAttribute(executionTaskJobPrmEClass, EXECUTION_TASK_JOB_PRM__ID);
		createEAttribute(executionTaskJobPrmEClass, EXECUTION_TASK_JOB_PRM__LABEL);
		createEAttribute(executionTaskJobPrmEClass, EXECUTION_TASK_JOB_PRM__OVERRIDE);
		createEReference(executionTaskJobPrmEClass, EXECUTION_TASK_JOB_PRM__EXECUTION_TASK);
		createEAttribute(executionTaskJobPrmEClass, EXECUTION_TASK_JOB_PRM__ORIGINAL_VALUE);
		createEAttribute(executionTaskJobPrmEClass, EXECUTION_TASK_JOB_PRM__DEFAULT_VALUE);
		createEAttribute(executionTaskJobPrmEClass, EXECUTION_TASK_JOB_PRM__ITEM_TYPE);

		taskExecutionHistoryEClass = createEClass(TASK_EXECUTION_HISTORY);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__ID);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__BASIC_STATUS);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__DETAILED_STATUS);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__TASK_LABEL);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__TASK_DESCRIPTION);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__PROJECT_NAME);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__TALEND_JOB_NAME);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__TALEND_JOB_ID);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__TALEND_JOB_VERSION);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__CONTEXT_NAME);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__VIRTUAL_SERVER_NAME);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__EXECUTION_SERVER_NAME);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__EXECUTION_SERVER_HOST);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__EXECUTION_SERVER_CMD_PORT);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__EXECUTION_SERVER_FILE_PORT);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__EXECUTION_SERVER_MONITORING_PORT);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__APPLY_CONTEXT_TO_CHILDREN);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__TRIGGERED_BY);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__TRIGGER_TYPE);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__TRIGGER_NAME);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__TRIGGER_DESCRIPTION);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__TASK_ERROR_STACK_TRACE);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__ID_QUARTZ_JOB);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__ID_QUARTZ_TRIGGER);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__LAST_JOB_GENERATION_DATE);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__JOB_ARCHIVE_FILENAME);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__FILE_TRIGGER_FILE_MASK);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__FILE_TRIGGER_FILE_NAME);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__FILE_TRIGGER_FOLDER_PATH);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__FILE_TRIGGER_TRIGGERED_FILE_PATH);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__EXPECTED_TRIGGERING_DATE);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__TASK_START_DATE);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__TASK_END_DATE);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__SERVER_JOB_START_DATE);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__SERVER_JOB_END_DATE);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__ID_REMOTE_JOB);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__ID_REMOTE_JOB_EXECUTION);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__REQUEST_ID);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__RESUMING_MODE);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__CONTEXT_VALUES);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__JVM_VALUES);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__PARENT_TASK_EXEC_ID);
		createEAttribute(taskExecutionHistoryEClass, TASK_EXECUTION_HISTORY__PARENT_PLAN_EXEC_ID);

		planExecutionHistoryEClass = createEClass(PLAN_EXECUTION_HISTORY);
		createEAttribute(planExecutionHistoryEClass, PLAN_EXECUTION_HISTORY__ORIGINAL_LABEL);
		createEAttribute(planExecutionHistoryEClass, PLAN_EXECUTION_HISTORY__CURRENT_LABEL);

		talendTriggerEClass = createEClass(TALEND_TRIGGER);
		createEAttribute(talendTriggerEClass, TALEND_TRIGGER__ID);
		createEAttribute(talendTriggerEClass, TALEND_TRIGGER__ACTIVE);
		createEAttribute(talendTriggerEClass, TALEND_TRIGGER__LABEL);
		createEAttribute(talendTriggerEClass, TALEND_TRIGGER__DESCRIPTION);
		createEAttribute(talendTriggerEClass, TALEND_TRIGGER__TRIGGER_TYPE);
		createEReference(talendTriggerEClass, TALEND_TRIGGER__EXECUTION_TRIGGERABLE);
		createEAttribute(talendTriggerEClass, TALEND_TRIGGER__START_TIME);
		createEAttribute(talendTriggerEClass, TALEND_TRIGGER__END_TIME);
		createEAttribute(talendTriggerEClass, TALEND_TRIGGER__PREVIOUS_FIRE_TIME);
		createEAttribute(talendTriggerEClass, TALEND_TRIGGER__FINAL_FIRE_TIME);
		createEAttribute(talendTriggerEClass, TALEND_TRIGGER__ID_QUARTZ_TRIGGER);
		createEAttribute(talendTriggerEClass, TALEND_TRIGGER__RESUME_PAUSE_UPDATED);
		createEAttribute(talendTriggerEClass, TALEND_TRIGGER__PREVIOUSLY_PAUSED);

		cronTalendTriggerEClass = createEClass(CRON_TALEND_TRIGGER);
		createEAttribute(cronTalendTriggerEClass, CRON_TALEND_TRIGGER__CRON_EXPRESSION);

		cronUITalendTriggerEClass = createEClass(CRON_UI_TALEND_TRIGGER);
		createEAttribute(cronUITalendTriggerEClass, CRON_UI_TALEND_TRIGGER__LIST_DAYS_OF_WEEK);
		createEAttribute(cronUITalendTriggerEClass, CRON_UI_TALEND_TRIGGER__LIST_DAYS_OF_MONTH);
		createEAttribute(cronUITalendTriggerEClass, CRON_UI_TALEND_TRIGGER__LIST_MONTHS);
		createEAttribute(cronUITalendTriggerEClass, CRON_UI_TALEND_TRIGGER__LIST_YEARS);
		createEAttribute(cronUITalendTriggerEClass, CRON_UI_TALEND_TRIGGER__LIST_HOURS);
		createEAttribute(cronUITalendTriggerEClass, CRON_UI_TALEND_TRIGGER__LIST_MINUTES);

		simpleTalendTriggerEClass = createEClass(SIMPLE_TALEND_TRIGGER);
		createEAttribute(simpleTalendTriggerEClass, SIMPLE_TALEND_TRIGGER__REPEAT_COUNT);
		createEAttribute(simpleTalendTriggerEClass, SIMPLE_TALEND_TRIGGER__REPEAT_INTERVAL);
		createEAttribute(simpleTalendTriggerEClass, SIMPLE_TALEND_TRIGGER__TIMES_TRIGGERED);

		fileTriggerEClass = createEClass(FILE_TRIGGER);
		createEReference(fileTriggerEClass, FILE_TRIGGER__FILE_TRIGGER_MASKS);

		fileTriggerMaskEClass = createEClass(FILE_TRIGGER_MASK);
		createEAttribute(fileTriggerMaskEClass, FILE_TRIGGER_MASK__ID);
		createEAttribute(fileTriggerMaskEClass, FILE_TRIGGER_MASK__ACTIVE);
		createEAttribute(fileTriggerMaskEClass, FILE_TRIGGER_MASK__LABEL);
		createEAttribute(fileTriggerMaskEClass, FILE_TRIGGER_MASK__DESCRIPTION);
		createEReference(fileTriggerMaskEClass, FILE_TRIGGER_MASK__FILE_TRIGGER);
		createEAttribute(fileTriggerMaskEClass, FILE_TRIGGER_MASK__FOLDER_PATH);
		createEAttribute(fileTriggerMaskEClass, FILE_TRIGGER_MASK__FILE_MASK);
		createEAttribute(fileTriggerMaskEClass, FILE_TRIGGER_MASK__CONTEXT_PARAMETER_BASE_NAME);
		createEReference(fileTriggerMaskEClass, FILE_TRIGGER_MASK__CHECK_FILE_SERVER);
		createEAttribute(fileTriggerMaskEClass, FILE_TRIGGER_MASK__EXIST);
		createEAttribute(fileTriggerMaskEClass, FILE_TRIGGER_MASK__CREATED);
		createEAttribute(fileTriggerMaskEClass, FILE_TRIGGER_MASK__MODIFIED);

		executionServerEClass = createEClass(EXECUTION_SERVER);
		createEAttribute(executionServerEClass, EXECUTION_SERVER__ID);
		createEAttribute(executionServerEClass, EXECUTION_SERVER__LABEL);
		createEAttribute(executionServerEClass, EXECUTION_SERVER__DESCRIPTION);
		createEAttribute(executionServerEClass, EXECUTION_SERVER__HOST);
		createEAttribute(executionServerEClass, EXECUTION_SERVER__PORT);
		createEAttribute(executionServerEClass, EXECUTION_SERVER__FILE_TRANSFERT_PORT);
		createEAttribute(executionServerEClass, EXECUTION_SERVER__ACTIVE);
		createEAttribute(executionServerEClass, EXECUTION_SERVER__MONITORING_PORT);
		createEAttribute(executionServerEClass, EXECUTION_SERVER__TIMEOUT_UNKNOWN_STATE);
		createEAttribute(executionServerEClass, EXECUTION_SERVER__USERNAME);
		createEAttribute(executionServerEClass, EXECUTION_SERVER__PASSWORD);
		createEAttribute(executionServerEClass, EXECUTION_SERVER__JMX_URL);
		createEAttribute(executionServerEClass, EXECUTION_SERVER__WEB_CONSOLE_URL);
		createEAttribute(executionServerEClass, EXECUTION_SERVER__TALEND_RUNTIME);
		createEAttribute(executionServerEClass, EXECUTION_SERVER__MGMT_SERVER_PORT);
		createEAttribute(executionServerEClass, EXECUTION_SERVER__MGMT_REG_PORT);
		createEAttribute(executionServerEClass, EXECUTION_SERVER__ADMIN_CONSOLE_PORT);
		createEAttribute(executionServerEClass, EXECUTION_SERVER__USE_SSL);
		createEAttribute(executionServerEClass, EXECUTION_SERVER__INSTANCE);

		executionVirtualServerEClass = createEClass(EXECUTION_VIRTUAL_SERVER);
		createEAttribute(executionVirtualServerEClass, EXECUTION_VIRTUAL_SERVER__ID);
		createEAttribute(executionVirtualServerEClass, EXECUTION_VIRTUAL_SERVER__LABEL);
		createEAttribute(executionVirtualServerEClass, EXECUTION_VIRTUAL_SERVER__DESCRIPTION);
		createEAttribute(executionVirtualServerEClass, EXECUTION_VIRTUAL_SERVER__ACTIVE);
		createEReference(executionVirtualServerEClass, EXECUTION_VIRTUAL_SERVER__EXECUTION_SERVERS);

		jobletProcessItemEClass = createEClass(JOBLET_PROCESS_ITEM);
		createEReference(jobletProcessItemEClass, JOBLET_PROCESS_ITEM__JOBLET_PROCESS);
		createEReference(jobletProcessItemEClass, JOBLET_PROCESS_ITEM__ICON);

		jobDocumentationItemEClass = createEClass(JOB_DOCUMENTATION_ITEM);

		jobletDocumentationItemEClass = createEClass(JOBLET_DOCUMENTATION_ITEM);

		wsdlSchemaConnectionItemEClass = createEClass(WSDL_SCHEMA_CONNECTION_ITEM);

		schemaInformationEClass = createEClass(SCHEMA_INFORMATION);
		createEAttribute(schemaInformationEClass, SCHEMA_INFORMATION__VERSION);

		informationEClass = createEClass(INFORMATION);
		createEAttribute(informationEClass, INFORMATION__LEVEL);
		createEAttribute(informationEClass, INFORMATION__TYPE);
		createEAttribute(informationEClass, INFORMATION__TEXT);

		sqlPatternItemEClass = createEClass(SQL_PATTERN_ITEM);
		createEAttribute(sqlPatternItemEClass, SQL_PATTERN_ITEM__SYSTEM);
		createEAttribute(sqlPatternItemEClass, SQL_PATTERN_ITEM__ELT_NAME);

		componentSettingEClass = createEClass(COMPONENT_SETTING);
		createEAttribute(componentSettingEClass, COMPONENT_SETTING__NAME);
		createEAttribute(componentSettingEClass, COMPONENT_SETTING__HIDDEN);
		createEAttribute(componentSettingEClass, COMPONENT_SETTING__FAMILY);

		statAndLogsSettingsEClass = createEClass(STAT_AND_LOGS_SETTINGS);
		createEReference(statAndLogsSettingsEClass, STAT_AND_LOGS_SETTINGS__PARAMETERS);

		implicitContextSettingsEClass = createEClass(IMPLICIT_CONTEXT_SETTINGS);
		createEReference(implicitContextSettingsEClass, IMPLICIT_CONTEXT_SETTINGS__PARAMETERS);

		soaOperationEClass = createEClass(SOA_OPERATION);
		createEAttribute(soaOperationEClass, SOA_OPERATION__ID);
		createEAttribute(soaOperationEClass, SOA_OPERATION__LABEL);
		createEAttribute(soaOperationEClass, SOA_OPERATION__DESCRIPTION);
		createEReference(soaOperationEClass, SOA_OPERATION__PROJECT);
		createEAttribute(soaOperationEClass, SOA_OPERATION__CONTEXT);
		createEAttribute(soaOperationEClass, SOA_OPERATION__JOB_VERSION);
		createEAttribute(soaOperationEClass, SOA_OPERATION__JOB_NAME);
		createEAttribute(soaOperationEClass, SOA_OPERATION__ACTIVE);
		createEAttribute(soaOperationEClass, SOA_OPERATION__LAST_SCRIPT_GENERATION_DATE);
		createEAttribute(soaOperationEClass, SOA_OPERATION__JOB_ID);
		createEAttribute(soaOperationEClass, SOA_OPERATION__APPLY_CONTEXT_TO_CHILDREN);
		createEReference(soaOperationEClass, SOA_OPERATION__INPUT_PARAMETERS);
		createEAttribute(soaOperationEClass, SOA_OPERATION__JVM_PARAMETERS);
		createEAttribute(soaOperationEClass, SOA_OPERATION__XMS);
		createEAttribute(soaOperationEClass, SOA_OPERATION__XMX);
		createEAttribute(soaOperationEClass, SOA_OPERATION__MIN_JOB_INSTANCES);
		createEAttribute(soaOperationEClass, SOA_OPERATION__MAX_JOB_INSTANCES);
		createEAttribute(soaOperationEClass, SOA_OPERATION__IDLE_TTL_FOR_ALL_INSTANCES);
		createEAttribute(soaOperationEClass, SOA_OPERATION__IDLE_TTL_FOR_ADDITIONAL_INSTANCES);
		createEAttribute(soaOperationEClass, SOA_OPERATION__QUEUE_MAX_SIZE);
		createEAttribute(soaOperationEClass, SOA_OPERATION__REQUEST_IN_QUEUE_TTL);
		createEReference(soaOperationEClass, SOA_OPERATION__SERVICE);
		createEAttribute(soaOperationEClass, SOA_OPERATION__RETURN_STYLE);
		createEAttribute(soaOperationEClass, SOA_OPERATION__BRANCH);

		soaInputParameterEClass = createEClass(SOA_INPUT_PARAMETER);
		createEAttribute(soaInputParameterEClass, SOA_INPUT_PARAMETER__ID);
		createEAttribute(soaInputParameterEClass, SOA_INPUT_PARAMETER__LABEL);
		createEReference(soaInputParameterEClass, SOA_INPUT_PARAMETER__OPERATION);
		createEAttribute(soaInputParameterEClass, SOA_INPUT_PARAMETER__DEFAULT_VALUE);
		createEAttribute(soaInputParameterEClass, SOA_INPUT_PARAMETER__EXPOSED_NAME);
		createEAttribute(soaInputParameterEClass, SOA_INPUT_PARAMETER__EXPOSED);

		soaServiceEClass = createEClass(SOA_SERVICE);
		createEAttribute(soaServiceEClass, SOA_SERVICE__ID);
		createEAttribute(soaServiceEClass, SOA_SERVICE__LABEL);
		createEAttribute(soaServiceEClass, SOA_SERVICE__NAME_SPACE);
		createEAttribute(soaServiceEClass, SOA_SERVICE__CONTACT);
		createEAttribute(soaServiceEClass, SOA_SERVICE__DESCRIPTION);
		createEAttribute(soaServiceEClass, SOA_SERVICE__CREATION);
		createEAttribute(soaServiceEClass, SOA_SERVICE__MODIFICATION);
		createEAttribute(soaServiceEClass, SOA_SERVICE__PORT);
		createEAttribute(soaServiceEClass, SOA_SERVICE__TYPE);
		createEAttribute(soaServiceEClass, SOA_SERVICE__STYLE);
		createEAttribute(soaServiceEClass, SOA_SERVICE__USED_TYPE);
		createEAttribute(soaServiceEClass, SOA_SERVICE__PARAM_STYLE);
		createEReference(soaServiceEClass, SOA_SERVICE__OPERATIONS);
		createEAttribute(soaServiceEClass, SOA_SERVICE__STATUS);

		rulesItemEClass = createEClass(RULES_ITEM);

		userRightEClass = createEClass(USER_RIGHT);
		createEAttribute(userRightEClass, USER_RIGHT__ID);
		createEAttribute(userRightEClass, USER_RIGHT__NAME);
		createEAttribute(userRightEClass, USER_RIGHT__DESCRIPTION);
		createEReference(userRightEClass, USER_RIGHT__ROLES_RIGHTS);

		roleRightEClass = createEClass(ROLE_RIGHT);
		createEReference(roleRightEClass, ROLE_RIGHT__ROLE);
		createEReference(roleRightEClass, ROLE_RIGHT__USER_RIGHT);

		svgBusinessProcessItemEClass = createEClass(SVG_BUSINESS_PROCESS_ITEM);
		createEReference(svgBusinessProcessItemEClass, SVG_BUSINESS_PROCESS_ITEM__BUSINESS_PROCESS_ITEM);

		linkRulesItemEClass = createEClass(LINK_RULES_ITEM);
		createEAttribute(linkRulesItemEClass, LINK_RULES_ITEM__NAME);
		createEAttribute(linkRulesItemEClass, LINK_RULES_ITEM__EXTENSION);
		createEReference(linkRulesItemEClass, LINK_RULES_ITEM__LINK);

		itemRelationsEClass = createEClass(ITEM_RELATIONS);
		createEReference(itemRelationsEClass, ITEM_RELATIONS__BASE_ITEM);
		createEReference(itemRelationsEClass, ITEM_RELATIONS__RELATED_ITEMS);

		itemRelationEClass = createEClass(ITEM_RELATION);
		createEAttribute(itemRelationEClass, ITEM_RELATION__ID);
		createEAttribute(itemRelationEClass, ITEM_RELATION__VERSION);
		createEAttribute(itemRelationEClass, ITEM_RELATION__TYPE);

		notificationEClass = createEClass(NOTIFICATION);
		createEAttribute(notificationEClass, NOTIFICATION__ID);
		createEAttribute(notificationEClass, NOTIFICATION__TYPE);
		createEAttribute(notificationEClass, NOTIFICATION__PROPS);
		createEAttribute(notificationEClass, NOTIFICATION__ENABLED);

		hl7ConnectionItemEClass = createEClass(HL7_CONNECTION_ITEM);

		userRoleReferenceEClass = createEClass(USER_ROLE_REFERENCE);
		createEReference(userRoleReferenceEClass, USER_ROLE_REFERENCE__USER);
		createEReference(userRoleReferenceEClass, USER_ROLE_REFERENCE__ROLE);

		headerFooterConnectionItemEClass = createEClass(HEADER_FOOTER_CONNECTION_ITEM);

		ftpConnectionItemEClass = createEClass(FTP_CONNECTION_ITEM);

		customComponentSettingEClass = createEClass(CUSTOM_COMPONENT_SETTING);
		createEAttribute(customComponentSettingEClass, CUSTOM_COMPONENT_SETTING__NAME);
		createEAttribute(customComponentSettingEClass, CUSTOM_COMPONENT_SETTING__SHARE);

		tdqItemEClass = createEClass(TDQ_ITEM);
		createEAttribute(tdqItemEClass, TDQ_ITEM__FILENAME);

		brmsConnectionItemEClass = createEClass(BRMS_CONNECTION_ITEM);

		jobScriptItemEClass = createEClass(JOB_SCRIPT_ITEM);

		edifactConnectionItemEClass = createEClass(EDIFACT_CONNECTION_ITEM);

		technicalVariableEClass = createEClass(TECHNICAL_VARIABLE);
		createEAttribute(technicalVariableEClass, TECHNICAL_VARIABLE__KEY);
		createEAttribute(technicalVariableEClass, TECHNICAL_VARIABLE__VALUE);

		referenceFileItemEClass = createEClass(REFERENCE_FILE_ITEM);
		createEAttribute(referenceFileItemEClass, REFERENCE_FILE_ITEM__NAME);
		createEAttribute(referenceFileItemEClass, REFERENCE_FILE_ITEM__EXTENSION);
		createEReference(referenceFileItemEClass, REFERENCE_FILE_ITEM__CONTENT);

		referenceItemEClass = createEClass(REFERENCE_ITEM);
		createEReference(referenceItemEClass, REFERENCE_ITEM__STATE);
		createEReference(referenceItemEClass, REFERENCE_ITEM__PARENT);
		createEAttribute(referenceItemEClass, REFERENCE_ITEM__RELOAD_FROM_FILE);

		exchangeUserEClass = createEClass(EXCHANGE_USER);
		createEAttribute(exchangeUserEClass, EXCHANGE_USER__USERNAME);
		createEAttribute(exchangeUserEClass, EXCHANGE_USER__LOGIN);
		createEAttribute(exchangeUserEClass, EXCHANGE_USER__PASSWORD);

		artifactNotificationEClass = createEClass(ARTIFACT_NOTIFICATION);
		createEAttribute(artifactNotificationEClass, ARTIFACT_NOTIFICATION__NAME);

		additionalInfoMapEClass = createEClass(ADDITIONAL_INFO_MAP);
		createEAttribute(additionalInfoMapEClass, ADDITIONAL_INFO_MAP__KEY);
		createEAttribute(additionalInfoMapEClass, ADDITIONAL_INFO_MAP__VALUE);

		migrationTaskEClass = createEClass(MIGRATION_TASK);
		createEAttribute(migrationTaskEClass, MIGRATION_TASK__ID);
		createEAttribute(migrationTaskEClass, MIGRATION_TASK__BREAKS);
		createEAttribute(migrationTaskEClass, MIGRATION_TASK__VERSION);
		createEAttribute(migrationTaskEClass, MIGRATION_TASK__STATUS);

		pigudfItemEClass = createEClass(PIGUDF_ITEM);

		// Create enums
		folderTypeEEnum = createEEnum(FOLDER_TYPE);
		userProjectAuthorizationTypeEEnum = createEEnum(USER_PROJECT_AUTHORIZATION_TYPE);
		userModuleAuthorizationTypeEEnum = createEEnum(USER_MODULE_AUTHORIZATION_TYPE);
		informationLevelEEnum = createEEnum(INFORMATION_LEVEL);
		migrationStatusEEnum = createEEnum(MIGRATION_STATUS);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private boolean isInitialized = false;

    /**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);
		NotationPackage theNotationPackage = (NotationPackage)EPackage.Registry.INSTANCE.getEPackage(NotationPackage.eNS_URI);
		BusinessPackage theBusinessPackage = (BusinessPackage)EPackage.Registry.INSTANCE.getEPackage(BusinessPackage.eNS_URI);
		ComponentPackage theComponentPackage = (ComponentPackage)EPackage.Registry.INSTANCE.getEPackage(ComponentPackage.eNS_URI);
		ConnectionPackage theConnectionPackage = (ConnectionPackage)EPackage.Registry.INSTANCE.getEPackage(ConnectionPackage.eNS_URI);
		TalendFilePackage theTalendFilePackage = (TalendFilePackage)EPackage.Registry.INSTANCE.getEPackage(TalendFilePackage.eNS_URI);
		JobletPackage theJobletPackage = (JobletPackage)EPackage.Registry.INSTANCE.getEPackage(JobletPackage.eNS_URI);

		// Add supertypes to classes
		linkDocumentationItemEClass.getESuperTypes().add(this.getItem());
		businessProcessItemEClass.getESuperTypes().add(this.getItem());
		fileItemEClass.getESuperTypes().add(this.getItem());
		documentationItemEClass.getESuperTypes().add(this.getFileItem());
		routineItemEClass.getESuperTypes().add(this.getFileItem());
		connectionItemEClass.getESuperTypes().add(this.getItem());
		snippetItemEClass.getESuperTypes().add(this.getItem());
		delimitedFileConnectionItemEClass.getESuperTypes().add(this.getConnectionItem());
		positionalFileConnectionItemEClass.getESuperTypes().add(this.getConnectionItem());
		regExFileConnectionItemEClass.getESuperTypes().add(this.getConnectionItem());
		csvFileConnectionItemEClass.getESuperTypes().add(this.getDelimitedFileConnectionItem());
		databaseConnectionItemEClass.getESuperTypes().add(this.getConnectionItem());
		sapConnectionItemEClass.getESuperTypes().add(this.getConnectionItem());
		xmlFileConnectionItemEClass.getESuperTypes().add(this.getConnectionItem());
		ldifFileConnectionItemEClass.getESuperTypes().add(this.getConnectionItem());
		excelFileConnectionItemEClass.getESuperTypes().add(this.getConnectionItem());
		ebcdicConnectionItemEClass.getESuperTypes().add(this.getConnectionItem());
		mdmConnectionItemEClass.getESuperTypes().add(this.getConnectionItem());
		validationRulesConnectionItemEClass.getESuperTypes().add(this.getConnectionItem());
		processItemEClass.getESuperTypes().add(this.getItem());
		folderItemEClass.getESuperTypes().add(this.getItem());
		contextItemEClass.getESuperTypes().add(this.getItem());
		genericSchemaConnectionItemEClass.getESuperTypes().add(this.getConnectionItem());
		ldapSchemaConnectionItemEClass.getESuperTypes().add(this.getConnectionItem());
		salesforceSchemaConnectionItemEClass.getESuperTypes().add(this.getConnectionItem());
		executionPlanEClass.getESuperTypes().add(this.getExecutionTriggerable());
		executionTaskEClass.getESuperTypes().add(this.getExecutionTriggerable());
		planExecutionHistoryEClass.getESuperTypes().add(this.getTaskExecutionHistory());
		cronTalendTriggerEClass.getESuperTypes().add(this.getTalendTrigger());
		cronUITalendTriggerEClass.getESuperTypes().add(this.getTalendTrigger());
		simpleTalendTriggerEClass.getESuperTypes().add(this.getTalendTrigger());
		fileTriggerEClass.getESuperTypes().add(this.getSimpleTalendTrigger());
		jobletProcessItemEClass.getESuperTypes().add(this.getItem());
		jobDocumentationItemEClass.getESuperTypes().add(this.getFileItem());
		jobletDocumentationItemEClass.getESuperTypes().add(this.getFileItem());
		wsdlSchemaConnectionItemEClass.getESuperTypes().add(this.getConnectionItem());
		sqlPatternItemEClass.getESuperTypes().add(this.getFileItem());
		rulesItemEClass.getESuperTypes().add(this.getFileItem());
		svgBusinessProcessItemEClass.getESuperTypes().add(this.getFileItem());
		linkRulesItemEClass.getESuperTypes().add(this.getItem());
		hl7ConnectionItemEClass.getESuperTypes().add(this.getConnectionItem());
		headerFooterConnectionItemEClass.getESuperTypes().add(this.getConnectionItem());
		ftpConnectionItemEClass.getESuperTypes().add(this.getConnectionItem());
		tdqItemEClass.getESuperTypes().add(this.getItem());
		brmsConnectionItemEClass.getESuperTypes().add(this.getConnectionItem());
		jobScriptItemEClass.getESuperTypes().add(this.getFileItem());
		edifactConnectionItemEClass.getESuperTypes().add(this.getConnectionItem());
		referenceFileItemEClass.getESuperTypes().add(this.getReferenceItem());
		pigudfItemEClass.getESuperTypes().add(this.getRoutineItem());

		// Initialize classes and features; add operations and parameters
		initEClass(projectEClass, Project.class, "Project", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getProject_TechnicalStatus(), this.getStatus(), null, "technicalStatus", null, 0, -1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProject_DocumentationStatus(), this.getStatus(), null, "documentationStatus", null, 0, -1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProject_Id(), ecorePackage.getEInt(), "id", null, 1, 1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProject_Label(), ecorePackage.getEString(), "label", null, 1, 1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProject_Description(), ecorePackage.getEString(), "description", null, 0, 1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProject_Language(), ecorePackage.getEString(), "language", null, 1, 1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProject_TechnicalLabel(), ecorePackage.getEString(), "technicalLabel", null, 1, 1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProject_Local(), ecorePackage.getEBoolean(), "local", null, 1, 1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProject_Folders(), this.getFolderItem(), null, "folders", null, 0, -1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProject_Deleted(), ecorePackage.getEBoolean(), "deleted", null, 1, 1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProject_DeleteDate(), ecorePackage.getEDate(), "deleteDate", null, 0, 1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProject_CreationDate(), ecorePackage.getEDate(), "creationDate", null, 1, 1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProject_Author(), this.getUser(), null, "author", null, 0, 1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProject_UserAuthorization(), this.getUserProjectAuthorization(), this.getUserProjectAuthorization_Project(), "userAuthorization", null, 0, -1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		initEReference(getProject_AllowedComponents(), this.getProjectComponentAuthorisation(), this.getProjectComponentAuthorisation_Project(), "allowedComponents", null, 0, -1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		initEReference(getProject_ReferencedProjects(), this.getProjectReference(), this.getProjectReference_Project(), "referencedProjects", null, 0, -1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		initEReference(getProject_AvailableRefProject(), this.getProjectReference(), this.getProjectReference_ReferencedProject(), "availableRefProject", null, 0, -1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		initEAttribute(getProject_MasterJobId(), theXMLTypePackage.getString(), "masterJobId", null, 0, 1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProject_SpagoBiServer(), this.getSpagoBiServer(), null, "spagoBiServer", null, 0, -1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		initEAttribute(getProject_ProductVersion(), theEcorePackage.getEString(), "productVersion", null, 0, 1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProject_ComponentsSettings(), this.getComponentSetting(), null, "componentsSettings", null, 0, -1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		initEAttribute(getProject_Url(), theEcorePackage.getEString(), "url", null, 0, 1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProject_StatAndLogsSettings(), this.getStatAndLogsSettings(), null, "statAndLogsSettings", null, 0, 1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProject_ImplicitContextSettings(), this.getImplicitContextSettings(), null, "implicitContextSettings", null, 0, 1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProject_HidePassword(), ecorePackage.getEBoolean(), "hidePassword", "true", 0, 1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProject_ItemsRelations(), this.getItemRelations(), null, "ItemsRelations", null, 0, -1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProject_Reference(), ecorePackage.getEBoolean(), "reference", null, 1, 1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProject_CustomComponentSettings(), this.getCustomComponentSetting(), null, "customComponentSettings", null, 0, -1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		initEAttribute(getProject_DeletedFolders(), ecorePackage.getEString(), "deletedFolders", null, 0, -1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProject_Type(), theEcorePackage.getEString(), "type", null, 0, 1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProject_ItemsRelationVersion(), ecorePackage.getEString(), "itemsRelationVersion", null, 0, 1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProject_ExchangeUser(), this.getExchangeUser(), null, "exchangeUser", null, 0, 1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProject_MigrationTasks(), theEcorePackage.getEString(), "migrationTasks", null, 0, -1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProject_MigrationTask(), this.getMigrationTask(), null, "migrationTask", null, 0, -1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProject_BigData(), ecorePackage.getEBoolean(), "bigData", "true", 0, 1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(projectComponentAuthorisationEClass, ProjectComponentAuthorisation.class, "ProjectComponentAuthorisation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getProjectComponentAuthorisation_Project(), this.getProject(), this.getProject_AllowedComponents(), "project", null, 1, 1, ProjectComponentAuthorisation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProjectComponentAuthorisation_Component(), this.getComponent(), this.getComponent_Projects(), "component", null, 1, 1, ProjectComponentAuthorisation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(projectReferenceEClass, ProjectReference.class, "ProjectReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getProjectReference_Project(), this.getProject(), this.getProject_ReferencedProjects(), "project", null, 1, 1, ProjectReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProjectReference_Branch(), ecorePackage.getEString(), "branch", null, 0, 1, ProjectReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProjectReference_ReferencedProject(), this.getProject(), this.getProject_AvailableRefProject(), "referencedProject", null, 1, 1, ProjectReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProjectReference_ReferencedBranch(), ecorePackage.getEString(), "referencedBranch", null, 0, 1, ProjectReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(statusEClass, Status.class, "Status", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getStatus_Label(), ecorePackage.getEString(), "label", null, 0, 1, Status.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStatus_Code(), ecorePackage.getEString(), "code", null, 1, 1, Status.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(itemStateEClass, ItemState.class, "ItemState", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getItemState_Path(), ecorePackage.getEString(), "path", null, 0, 1, ItemState.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getItemState_Deleted(), ecorePackage.getEBoolean(), "deleted", null, 0, 1, ItemState.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getItemState_Locked(), ecorePackage.getEBoolean(), "locked", null, 0, 1, ItemState.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getItemState_Locker(), this.getUser(), null, "locker", null, 0, 1, ItemState.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getItemState_LockDate(), ecorePackage.getEDate(), "lockDate", null, 0, 1, ItemState.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getItemState_CommitDate(), ecorePackage.getEDate(), "commitDate", null, 0, 1, ItemState.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(propertyEClass, Property.class, "Property", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getProperty_Id(), ecorePackage.getEString(), "id", null, 0, 1, Property.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProperty_Label(), ecorePackage.getEString(), "label", null, 1, 1, Property.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProperty_Purpose(), ecorePackage.getEString(), "purpose", null, 0, 1, Property.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProperty_Description(), ecorePackage.getEString(), "description", null, 0, 1, Property.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProperty_CreationDate(), ecorePackage.getEDate(), "creationDate", null, 1, 1, Property.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProperty_ModificationDate(), ecorePackage.getEDate(), "modificationDate", null, 0, 1, Property.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProperty_Version(), ecorePackage.getEString(), "version", null, 1, 1, Property.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProperty_StatusCode(), ecorePackage.getEString(), "statusCode", null, 0, 1, Property.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProperty_Item(), this.getItem(), this.getItem_Property(), "item", null, 0, 1, Property.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProperty_Author(), this.getUser(), null, "author", null, 0, 1, Property.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProperty_Informations(), this.getInformation(), null, "informations", null, 0, -1, Property.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProperty_MaxInformationLevel(), this.getInformationLevel(), "maxInformationLevel", null, 0, 1, Property.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProperty_OldStatusCode(), theEcorePackage.getEString(), "oldStatusCode", "", 0, 1, Property.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProperty_AdditionalProperties(), this.getAdditionalInfoMap(), null, "additionalProperties", null, 0, -1, Property.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProperty_DisplayName(), theEcorePackage.getEString(), "displayName", null, 0, 1, Property.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(itemEClass, Item.class, "Item", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getItem_Property(), this.getProperty(), this.getProperty_Item(), "property", null, 0, 1, Item.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getItem_State(), this.getItemState(), null, "state", null, 0, 1, Item.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getItem_Parent(), theEcorePackage.getEObject(), null, "parent", null, 0, 1, Item.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getItem_ReferenceResources(), this.getReferenceFileItem(), null, "referenceResources", null, 0, -1, Item.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getItem_FileExtension(), ecorePackage.getEString(), "fileExtension", null, 0, 1, Item.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getItem_NeedVersion(), theEcorePackage.getEBoolean(), "needVersion", "true", 0, 1, Item.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(linkDocumentationItemEClass, LinkDocumentationItem.class, "LinkDocumentationItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getLinkDocumentationItem_Name(), theEcorePackage.getEString(), "name", null, 0, 1, LinkDocumentationItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLinkDocumentationItem_Extension(), theEcorePackage.getEString(), "extension", null, 0, 1, LinkDocumentationItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLinkDocumentationItem_Link(), this.getLinkType(), null, "link", null, 0, 1, LinkDocumentationItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(linkTypeEClass, LinkType.class, "LinkType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getLinkType_URI(), theEcorePackage.getEString(), "URI", null, 0, 1, LinkType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLinkType_State(), ecorePackage.getEBoolean(), "state", null, 0, 1, LinkType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(businessProcessItemEClass, BusinessProcessItem.class, "BusinessProcessItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBusinessProcessItem_Notation(), theNotationPackage.getDiagram(), null, "notation", null, 0, 1, BusinessProcessItem.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBusinessProcessItem_Semantic(), theBusinessPackage.getBusinessProcess(), null, "semantic", null, 0, 1, BusinessProcessItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBusinessProcessItem_NotationHolder(), this.getNotationHolder(), null, "notationHolder", null, 0, 1, BusinessProcessItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBusinessProcessItem_SvgBusinessProcessItem(), this.getSVGBusinessProcessItem(), this.getSVGBusinessProcessItem_BusinessProcessItem(), "svgBusinessProcessItem", null, 1, 1, BusinessProcessItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(fileItemEClass, FileItem.class, "FileItem", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFileItem_Name(), ecorePackage.getEString(), "name", null, 0, 1, FileItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFileItem_Extension(), ecorePackage.getEString(), "extension", null, 0, 1, FileItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFileItem_Content(), this.getByteArray(), null, "content", null, 0, 1, FileItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(byteArrayEClass, ByteArray.class, "ByteArray", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getByteArray_InnerContent(), ecorePackage.getEByteArray(), "innerContent", null, 0, 1, ByteArray.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(documentationItemEClass, DocumentationItem.class, "DocumentationItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(routineItemEClass, RoutineItem.class, "RoutineItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getRoutineItem_BuiltIn(), ecorePackage.getEBoolean(), "builtIn", "false", 0, 1, RoutineItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRoutineItem_Imports(), theComponentPackage.getIMPORTType(), null, "imports", null, 0, -1, RoutineItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRoutineItem_PackageType(), theXMLTypePackage.getString(), "packageType", "routines", 0, 1, RoutineItem.class, IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(connectionItemEClass, ConnectionItem.class, "ConnectionItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getConnectionItem_Connection(), theConnectionPackage.getConnection(), null, "connection", null, 0, 1, ConnectionItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(snippetVariableEClass, SnippetVariable.class, "SnippetVariable", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSnippetVariable_Name(), ecorePackage.getEString(), "name", null, 0, 1, SnippetVariable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSnippetVariable_Value(), ecorePackage.getEString(), "value", null, 0, 1, SnippetVariable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSnippetVariable_Description(), ecorePackage.getEString(), "description", null, 0, 1, SnippetVariable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSnippetVariable_Id(), ecorePackage.getEString(), "id", null, 0, 1, SnippetVariable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(snippetItemEClass, SnippetItem.class, "SnippetItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSnippetItem_Name(), ecorePackage.getEString(), "name", null, 0, 1, SnippetItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSnippetItem_Content(), ecorePackage.getEString(), "content", null, 0, 1, SnippetItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSnippetItem_Variables(), this.getSnippetVariable(), null, "variables", null, 0, -1, SnippetItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(delimitedFileConnectionItemEClass, DelimitedFileConnectionItem.class, "DelimitedFileConnectionItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(positionalFileConnectionItemEClass, PositionalFileConnectionItem.class, "PositionalFileConnectionItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(regExFileConnectionItemEClass, RegExFileConnectionItem.class, "RegExFileConnectionItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(csvFileConnectionItemEClass, CSVFileConnectionItem.class, "CSVFileConnectionItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(databaseConnectionItemEClass, DatabaseConnectionItem.class, "DatabaseConnectionItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(sapConnectionItemEClass, SAPConnectionItem.class, "SAPConnectionItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(xmlFileConnectionItemEClass, XmlFileConnectionItem.class, "XmlFileConnectionItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(ldifFileConnectionItemEClass, LdifFileConnectionItem.class, "LdifFileConnectionItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(excelFileConnectionItemEClass, ExcelFileConnectionItem.class, "ExcelFileConnectionItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(ebcdicConnectionItemEClass, EbcdicConnectionItem.class, "EbcdicConnectionItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(mdmConnectionItemEClass, MDMConnectionItem.class, "MDMConnectionItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(validationRulesConnectionItemEClass, ValidationRulesConnectionItem.class, "ValidationRulesConnectionItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(processItemEClass, ProcessItem.class, "ProcessItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getProcessItem_Process(), theTalendFilePackage.getProcessType(), null, "process", null, 0, 1, ProcessItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(userRoleEClass, UserRole.class, "UserRole", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getUserRole_Id(), ecorePackage.getEInt(), "id", null, 1, 1, UserRole.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUserRole_Name(), ecorePackage.getEString(), "name", null, 1, 1, UserRole.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUserRole_LocalizedLabel(), ecorePackage.getEString(), "localizedLabel", null, 1, 1, UserRole.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUserRole_Fixed(), ecorePackage.getEBoolean(), "fixed", null, 0, 1, UserRole.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getUserRole_RolesRights(), this.getRoleRight(), this.getRoleRight_Role(), "rolesRights", null, 0, -1, UserRole.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		initEClass(userEClass, User.class, "User", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getUser_Id(), ecorePackage.getEInt(), "id", null, 1, 1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUser_Login(), ecorePackage.getEString(), "login", null, 1, 1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUser_Password(), ecorePackage.getEByteArray(), "password", null, 1, 1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUser_FirstName(), ecorePackage.getEString(), "firstName", null, 1, 1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUser_LastName(), ecorePackage.getEString(), "lastName", null, 1, 1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUser_CreationDate(), ecorePackage.getEDate(), "creationDate", null, 1, 1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUser_DeleteDate(), ecorePackage.getEDate(), "deleteDate", null, 0, 1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUser_Deleted(), ecorePackage.getEBoolean(), "deleted", null, 0, 1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUser_AllowedToModifyComponents(), ecorePackage.getEBoolean(), "allowedToModifyComponents", null, 0, 1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUser_Comment(), ecorePackage.getEString(), "Comment", null, 0, 1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getUser_ProjectAuthorization(), this.getUserProjectAuthorization(), this.getUserProjectAuthorization_User(), "projectAuthorization", null, 0, -1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		initEReference(getUser_ModuleAuthorization(), this.getUserModuleAuthorization(), this.getUserModuleAuthorization_User(), "moduleAuthorization", null, 0, -1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		initEReference(getUser_PreferredDashboardConnection(), this.getDashboardConnection(), null, "preferredDashboardConnection", null, 0, 1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUser_LastAdminConnectionDate(), ecorePackage.getEDate(), "lastAdminConnectionDate", null, 0, 1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUser_LastStudioConnectionDate(), ecorePackage.getEDate(), "lastStudioConnectionDate", null, 0, 1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUser_FirstAdminConnectionDate(), ecorePackage.getEDate(), "firstAdminConnectionDate", null, 0, 1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUser_FirstStudioConnectionDate(), ecorePackage.getEDate(), "firstStudioConnectionDate", null, 0, 1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUser_AdminConnexionNumber(), theEcorePackage.getEInt(), "adminConnexionNumber", null, 0, 1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUser_StudioConnexionNumber(), theEcorePackage.getEInt(), "studioConnexionNumber", null, 0, 1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUser_AuthenticationInfo(), theEcorePackage.getEString(), "authenticationInfo", null, 0, 1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUser_LdapLogin(), ecorePackage.getEString(), "ldapLogin", null, 0, 1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUser_LdapId(), ecorePackage.getEString(), "ldapId", null, 0, 1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUser_Language(), ecorePackage.getEString(), "language", null, 0, 1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUser_Type(), ecorePackage.getEString(), "type", null, 0, 1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUser_AdditionnalData(), ecorePackage.getEString(), "additionnalData", null, 0, 1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUser_Technical(), theEcorePackage.getEBoolean(), "technical", null, 0, 1, User.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(folderItemEClass, FolderItem.class, "FolderItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFolderItem_Children(), this.getItem(), null, "children", null, 0, -1, FolderItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFolderItem_Type(), this.getFolderType(), "type", null, 1, 1, FolderItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(componentEClass, Component.class, "Component", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getComponent_Id(), ecorePackage.getEInt(), "id", null, 1, 1, Component.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getComponent_Label(), ecorePackage.getEString(), "label", null, 0, 1, Component.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getComponent_Version(), ecorePackage.getEFloat(), "version", null, 0, 1, Component.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getComponent_LastUpdateDate(), ecorePackage.getEDate(), "lastUpdateDate", null, 0, 1, Component.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getComponent_Author(), this.getUser(), null, "author", null, 1, 1, Component.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getComponent_CreationDate(), ecorePackage.getEDate(), "creationDate", null, 1, 1, Component.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getComponent_DeleteDate(), ecorePackage.getEDate(), "deleteDate", null, 0, 1, Component.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getComponent_Deleted(), ecorePackage.getEBoolean(), "deleted", null, 0, 1, Component.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getComponent_FileDescriptor(), ecorePackage.getEByteArray(), "fileDescriptor", null, 0, 1, Component.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getComponent_Projects(), this.getProjectComponentAuthorisation(), this.getProjectComponentAuthorisation_Component(), "projects", null, 0, -1, Component.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		initEClass(notationHolderEClass, NotationHolder.class, "NotationHolder", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getNotationHolder_NotationString(), theEcorePackage.getEString(), "notationString", null, 0, 1, NotationHolder.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(userProjectAuthorizationEClass, UserProjectAuthorization.class, "UserProjectAuthorization", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getUserProjectAuthorization_User(), this.getUser(), this.getUser_ProjectAuthorization(), "user", null, 0, 1, UserProjectAuthorization.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getUserProjectAuthorization_Project(), this.getProject(), this.getProject_UserAuthorization(), "project", null, 0, 1, UserProjectAuthorization.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUserProjectAuthorization_Type(), this.getUserProjectAuthorizationType(), "type", "ReadWrite", 0, 1, UserProjectAuthorization.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(contextItemEClass, ContextItem.class, "ContextItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getContextItem_Context(), theTalendFilePackage.getContextType(), null, "context", null, 0, -1, ContextItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContextItem_DefaultContext(), theXMLTypePackage.getString(), "defaultContext", null, 0, 1, ContextItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(spagoBiServerEClass, SpagoBiServer.class, "SpagoBiServer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSpagoBiServer_EngineName(), ecorePackage.getEString(), "engineName", null, 1, 1, SpagoBiServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpagoBiServer_ShortDescription(), ecorePackage.getEString(), "shortDescription", null, 0, 1, SpagoBiServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpagoBiServer_Host(), ecorePackage.getEString(), "host", null, 0, 1, SpagoBiServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpagoBiServer_Port(), ecorePackage.getEString(), "port", null, 0, 1, SpagoBiServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpagoBiServer_Login(), theEcorePackage.getEString(), "login", null, 0, 1, SpagoBiServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpagoBiServer_Password(), ecorePackage.getEString(), "password", null, 0, 1, SpagoBiServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpagoBiServer_ApplicationContext(), theEcorePackage.getEString(), "applicationContext", null, 0, 1, SpagoBiServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(licenseEClass, License.class, "License", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getLicense_License(), ecorePackage.getEByteArray(), "license", null, 0, 1, License.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLicense_CustomerName(), theXMLTypePackage.getString(), "customerName", null, 0, 1, License.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLicense_Params(), theXMLTypePackage.getString(), "params", null, 0, 1, License.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLicense_Token(), theXMLTypePackage.getString(), "token", null, 0, 1, License.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLicense_DateTokenCheck(), theXMLTypePackage.getString(), "dateTokenCheck", null, 0, 1, License.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(genericSchemaConnectionItemEClass, GenericSchemaConnectionItem.class, "GenericSchemaConnectionItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(userModuleAuthorizationEClass, UserModuleAuthorization.class, "UserModuleAuthorization", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getUserModuleAuthorization_User(), this.getUser(), this.getUser_ModuleAuthorization(), "user", null, 0, 1, UserModuleAuthorization.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUserModuleAuthorization_Type(), this.getUserModuleAuthorizationType(), "type", "Scheduler", 0, 1, UserModuleAuthorization.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(ldapSchemaConnectionItemEClass, LDAPSchemaConnectionItem.class, "LDAPSchemaConnectionItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(salesforceSchemaConnectionItemEClass, SalesforceSchemaConnectionItem.class, "SalesforceSchemaConnectionItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(dashboardConnectionEClass, DashboardConnection.class, "DashboardConnection", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDashboardConnection_Id(), ecorePackage.getEInt(), "id", null, 1, 1, DashboardConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDashboardConnection_Label(), ecorePackage.getEString(), "label", null, 0, 1, DashboardConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDashboardConnection_Dialect(), ecorePackage.getEString(), "dialect", null, 0, 1, DashboardConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDashboardConnection_Host(), ecorePackage.getEString(), "host", null, 0, 1, DashboardConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDashboardConnection_Port(), ecorePackage.getEString(), "port", null, 0, 1, DashboardConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDashboardConnection_Database(), ecorePackage.getEString(), "database", null, 0, 1, DashboardConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDashboardConnection_Username(), ecorePackage.getEString(), "username", null, 0, 1, DashboardConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDashboardConnection_Password(), ecorePackage.getEString(), "password", null, 0, 1, DashboardConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDashboardConnection_LogTable(), ecorePackage.getEString(), "logTable", null, 0, 1, DashboardConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDashboardConnection_StatTable(), ecorePackage.getEString(), "statTable", null, 0, 1, DashboardConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDashboardConnection_FlowMeterTable(), ecorePackage.getEString(), "flowMeterTable", null, 0, 1, DashboardConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDashboardConnection_AdditionnalsParams(), ecorePackage.getEString(), "additionnalsParams", null, 0, 1, DashboardConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDashboardConnection_Datasource(), ecorePackage.getEString(), "datasource", null, 0, 1, DashboardConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(executionTriggerableEClass, ExecutionTriggerable.class, "ExecutionTriggerable", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getExecutionTriggerable_Id(), theEcorePackage.getEInt(), "id", null, 1, 1, ExecutionTriggerable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getExecutionTriggerable_Triggers(), this.getTalendTrigger(), this.getTalendTrigger_ExecutionTriggerable(), "triggers", null, 0, -1, ExecutionTriggerable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		initEAttribute(getExecutionTriggerable_IdQuartzJob(), theEcorePackage.getEInt(), "idQuartzJob", null, 0, 1, ExecutionTriggerable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTriggerable_Status(), theEcorePackage.getEString(), "status", null, 0, 1, ExecutionTriggerable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTriggerable_ErrorStatus(), theEcorePackage.getEString(), "errorStatus", null, 0, 1, ExecutionTriggerable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTriggerable_ConcurrentExecution(), theEcorePackage.getEBoolean(), "concurrentExecution", null, 0, 1, ExecutionTriggerable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTriggerable_ProcessingState(), theEcorePackage.getEBoolean(), "processingState", null, 0, 1, ExecutionTriggerable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTriggerable_RequestId(), theEcorePackage.getEString(), "requestId", null, 0, 1, ExecutionTriggerable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(executionPlanEClass, ExecutionPlan.class, "ExecutionPlan", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getExecutionPlan_Label(), theEcorePackage.getEString(), "label", null, 0, 1, ExecutionPlan.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getExecutionPlan_ExecPlanParts(), this.getExecutionPlanPart(), null, "execPlanParts", null, 0, -1, ExecutionPlan.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		initEReference(getExecutionPlan_ExecPlanPrms(), this.getExecutionPlanPrm(), null, "execPlanPrms", null, 0, -1, ExecutionPlan.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		initEAttribute(getExecutionPlan_Description(), theEcorePackage.getEString(), "description", null, 0, 1, ExecutionPlan.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(executionPlanPartEClass, ExecutionPlanPart.class, "ExecutionPlanPart", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getExecutionPlanPart_Id(), theEcorePackage.getEInt(), "id", null, 1, 1, ExecutionPlanPart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getExecutionPlanPart_Task(), this.getExecutionTask(), null, "task", null, 0, 1, ExecutionPlanPart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getExecutionPlanPart_ExecutionPlan(), this.getExecutionPlan(), null, "executionPlan", null, 0, 1, ExecutionPlanPart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getExecutionPlanPart_Parent(), this.getExecutionPlanPart(), null, "parent", null, 0, 1, ExecutionPlanPart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionPlanPart_Type(), theEcorePackage.getEString(), "type", null, 0, 1, ExecutionPlanPart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getExecutionPlanPart_JvmPrms(), this.getExecutionPlanPartCmdPrm(), null, "jvmPrms", null, 0, -1, ExecutionPlanPart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		initEReference(getExecutionPlanPart_ContextPrms(), this.getExecutionPlanPartJobPrm(), null, "contextPrms", null, 0, -1, ExecutionPlanPart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		initEAttribute(getExecutionPlanPart_Status(), theEcorePackage.getEString(), "status", null, 0, 1, ExecutionPlanPart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionPlanPart_StartDate(), theEcorePackage.getEDate(), "startDate", null, 0, 1, ExecutionPlanPart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionPlanPart_EndDate(), theEcorePackage.getEDate(), "endDate", null, 0, 1, ExecutionPlanPart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionPlanPart_RequestId(), theEcorePackage.getEString(), "requestId", null, 0, 1, ExecutionPlanPart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionPlanPart_UseParallel(), theEcorePackage.getEBoolean(), "useParallel", null, 0, 1, ExecutionPlanPart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionPlanPart_MaxThreads(), theEcorePackage.getEIntegerObject(), "maxThreads", null, 0, 1, ExecutionPlanPart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(executionPlanPrmEClass, ExecutionPlanPrm.class, "ExecutionPlanPrm", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getExecutionPlanPrm_Id(), theEcorePackage.getEInt(), "id", null, 1, 1, ExecutionPlanPrm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getExecutionPlanPrm_ExecutionPlan(), this.getExecutionPlan(), null, "executionPlan", null, 0, 1, ExecutionPlanPrm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionPlanPrm_Name(), theEcorePackage.getEString(), "name", null, 0, 1, ExecutionPlanPrm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionPlanPrm_Value(), theEcorePackage.getEString(), "value", null, 0, 1, ExecutionPlanPrm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(executionPlanPartCmdPrmEClass, ExecutionPlanPartCmdPrm.class, "ExecutionPlanPartCmdPrm", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getExecutionPlanPartCmdPrm_Id(), theEcorePackage.getEInt(), "id", null, 1, 1, ExecutionPlanPartCmdPrm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getExecutionPlanPartCmdPrm_ExecutionPlanPart(), this.getExecutionPlanPart(), null, "executionPlanPart", null, 0, 1, ExecutionPlanPartCmdPrm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionPlanPartCmdPrm_Name(), theEcorePackage.getEString(), "name", null, 0, 1, ExecutionPlanPartCmdPrm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionPlanPartCmdPrm_Value(), theEcorePackage.getEString(), "value", null, 0, 1, ExecutionPlanPartCmdPrm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(executionPlanPartJobPrmEClass, ExecutionPlanPartJobPrm.class, "ExecutionPlanPartJobPrm", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getExecutionPlanPartJobPrm_Id(), theEcorePackage.getEInt(), "id", null, 1, 1, ExecutionPlanPartJobPrm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getExecutionPlanPartJobPrm_ExecutionPlanPart(), this.getExecutionPlanPart(), null, "executionPlanPart", null, 0, 1, ExecutionPlanPartJobPrm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionPlanPartJobPrm_Override(), theEcorePackage.getEBoolean(), "override", null, 0, 1, ExecutionPlanPartJobPrm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionPlanPartJobPrm_Name(), theEcorePackage.getEString(), "name", null, 0, 1, ExecutionPlanPartJobPrm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionPlanPartJobPrm_CustomValue(), theEcorePackage.getEString(), "customValue", null, 0, 1, ExecutionPlanPartJobPrm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionPlanPartJobPrm_PartCustomValue(), theEcorePackage.getEString(), "partCustomValue", null, 0, 1, ExecutionPlanPartJobPrm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(executionTaskEClass, ExecutionTask.class, "ExecutionTask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getExecutionTask_Label(), theEcorePackage.getEString(), "label", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_Description(), theEcorePackage.getEString(), "description", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getExecutionTask_ExecutionServer(), this.getExecutionServer(), null, "executionServer", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getExecutionTask_Project(), this.getProject(), null, "project", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_Branch(), theEcorePackage.getEString(), "branch", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_Context(), theEcorePackage.getEString(), "context", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_JobVersion(), theEcorePackage.getEString(), "jobVersion", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_RegenerateJobOnChange(), theEcorePackage.getEBoolean(), "regenerateJobOnChange", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_Active(), theEcorePackage.getEBoolean(), "active", "true", 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_LastScriptGenerationDate(), theEcorePackage.getEDate(), "lastScriptGenerationDate", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_GeneratedSvnRevision(), theEcorePackage.getELongObject(), "generatedSvnRevision", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_IdRemoteJob(), theEcorePackage.getEString(), "idRemoteJob", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_IdRemoteJobExecution(), theEcorePackage.getEString(), "idRemoteJobExecution", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_ChecksumArchive(), theEcorePackage.getEString(), "checksumArchive", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_JobScriptArchiveFilename(), theEcorePackage.getEString(), "jobScriptArchiveFilename", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_LastRunDate(), theEcorePackage.getEDate(), "lastRunDate", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_LastDeploymentDate(), theEcorePackage.getEDate(), "lastDeploymentDate", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_LastEndedRunDate(), theEcorePackage.getEDate(), "lastEndedRunDate", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getExecutionTask_CmdPrms(), this.getExecutionTaskCmdPrm(), this.getExecutionTaskCmdPrm_ExecutionTask(), "cmdPrms", null, 0, -1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		initEReference(getExecutionTask_EsbPropertiesPrms(), this.getExecutionTaskProperties(), this.getExecutionTaskProperties_ExecutionTask(), "esbPropertiesPrms", null, 0, -1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		initEReference(getExecutionTask_JobPrms(), this.getExecutionTaskJobPrm(), this.getExecutionTaskJobPrm_ExecutionTask(), "jobPrms", null, 0, -1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		initEAttribute(getExecutionTask_JobId(), ecorePackage.getEString(), "jobId", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getExecutionTask_VirtualServer(), this.getExecutionVirtualServer(), null, "virtualServer", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_MaxConcurrentExecutions(), ecorePackage.getEInt(), "maxConcurrentExecutions", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_GeneratedProjectName(), theEcorePackage.getEString(), "generatedProjectName", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_GeneratedJobName(), theEcorePackage.getEString(), "generatedJobName", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_GeneratedJobVersion(), theEcorePackage.getEString(), "generatedJobVersion", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_ApplyContextToChildren(), theEcorePackage.getEBoolean(), "applyContextToChildren", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_ErrorStackTrace(), theEcorePackage.getEString(), "errorStackTrace", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_LastTriggeringDate(), theEcorePackage.getEDate(), "lastTriggeringDate", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_ExecStatisticsEnabled(), theEcorePackage.getEBoolean(), "execStatisticsEnabled", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_AddStatisticsCodeEnabled(), theEcorePackage.getEBoolean(), "addStatisticsCodeEnabled", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_OwnerSchedulerInstanceId(), theEcorePackage.getEString(), "ownerSchedulerInstanceId", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_OnUnknownStateJob(), theEcorePackage.getEString(), "onUnknownStateJob", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_UseLatestVersion(), theEcorePackage.getEBoolean(), "useLatestVersion", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_ApplicationType(), theEcorePackage.getEString(), "applicationType", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_RepositoryName(), theEcorePackage.getEString(), "repositoryName", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_FeaturesFileUrl(), theEcorePackage.getEString(), "featuresFileUrl", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_FeatureName(), theEcorePackage.getEString(), "featureName", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_FeatureVersion(), theEcorePackage.getEString(), "featureVersion", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_ApplicationGroup(), theEcorePackage.getEString(), "applicationGroup", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_BundleName(), theEcorePackage.getEString(), "bundleName", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTask_PropertyId(), theEcorePackage.getEString(), "propertyId", null, 0, 1, ExecutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(executionTaskPropertiesEClass, ExecutionTaskProperties.class, "ExecutionTaskProperties", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getExecutionTaskProperties_Id(), ecorePackage.getEInt(), "id", null, 1, 1, ExecutionTaskProperties.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTaskProperties_Name(), theEcorePackage.getEString(), "name", null, 0, 1, ExecutionTaskProperties.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTaskProperties_Value(), theEcorePackage.getEString(), "value", null, 0, 1, ExecutionTaskProperties.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getExecutionTaskProperties_ExecutionTask(), this.getExecutionTask(), this.getExecutionTask_EsbPropertiesPrms(), "executionTask", null, 0, 1, ExecutionTaskProperties.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(executionTaskCmdPrmEClass, ExecutionTaskCmdPrm.class, "ExecutionTaskCmdPrm", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getExecutionTaskCmdPrm_Id(), ecorePackage.getEInt(), "id", null, 1, 1, ExecutionTaskCmdPrm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTaskCmdPrm_Active(), theEcorePackage.getEBoolean(), "active", null, 0, 1, ExecutionTaskCmdPrm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTaskCmdPrm_Parameter(), theEcorePackage.getEString(), "parameter", null, 0, 1, ExecutionTaskCmdPrm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTaskCmdPrm_Description(), theEcorePackage.getEString(), "description", null, 0, 1, ExecutionTaskCmdPrm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getExecutionTaskCmdPrm_ExecutionTask(), this.getExecutionTask(), this.getExecutionTask_CmdPrms(), "executionTask", null, 0, 1, ExecutionTaskCmdPrm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(executionTaskJobPrmEClass, ExecutionTaskJobPrm.class, "ExecutionTaskJobPrm", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getExecutionTaskJobPrm_Id(), ecorePackage.getEInt(), "id", null, 1, 1, ExecutionTaskJobPrm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTaskJobPrm_Label(), theEcorePackage.getEString(), "label", null, 0, 1, ExecutionTaskJobPrm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTaskJobPrm_Override(), theEcorePackage.getEBoolean(), "override", null, 0, 1, ExecutionTaskJobPrm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getExecutionTaskJobPrm_ExecutionTask(), this.getExecutionTask(), this.getExecutionTask_JobPrms(), "executionTask", null, 0, 1, ExecutionTaskJobPrm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTaskJobPrm_OriginalValue(), theEcorePackage.getEString(), "originalValue", null, 0, 1, ExecutionTaskJobPrm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTaskJobPrm_DefaultValue(), theEcorePackage.getEString(), "defaultValue", null, 0, 1, ExecutionTaskJobPrm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionTaskJobPrm_ItemType(), theEcorePackage.getEString(), "itemType", null, 0, 1, ExecutionTaskJobPrm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(taskExecutionHistoryEClass, TaskExecutionHistory.class, "TaskExecutionHistory", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTaskExecutionHistory_Id(), ecorePackage.getEInt(), "id", null, 1, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_BasicStatus(), theEcorePackage.getEString(), "basicStatus", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_DetailedStatus(), theEcorePackage.getEString(), "detailedStatus", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_TaskLabel(), theEcorePackage.getEString(), "taskLabel", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_TaskDescription(), theEcorePackage.getEString(), "taskDescription", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_ProjectName(), theEcorePackage.getEString(), "projectName", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_TalendJobName(), theEcorePackage.getEString(), "talendJobName", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_TalendJobId(), theEcorePackage.getEString(), "talendJobId", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_TalendJobVersion(), theEcorePackage.getEString(), "talendJobVersion", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_ContextName(), theEcorePackage.getEString(), "contextName", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_VirtualServerName(), theEcorePackage.getEString(), "virtualServerName", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_ExecutionServerName(), theEcorePackage.getEString(), "executionServerName", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_ExecutionServerHost(), theEcorePackage.getEString(), "executionServerHost", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_ExecutionServerCmdPort(), ecorePackage.getEInt(), "executionServerCmdPort", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_ExecutionServerFilePort(), ecorePackage.getEInt(), "executionServerFilePort", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_ExecutionServerMonitoringPort(), ecorePackage.getEInt(), "executionServerMonitoringPort", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_ApplyContextToChildren(), theEcorePackage.getEBoolean(), "applyContextToChildren", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_TriggeredBy(), theEcorePackage.getEString(), "triggeredBy", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_TriggerType(), theEcorePackage.getEString(), "triggerType", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_TriggerName(), theEcorePackage.getEString(), "triggerName", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_TriggerDescription(), theEcorePackage.getEString(), "triggerDescription", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_TaskErrorStackTrace(), theEcorePackage.getEString(), "taskErrorStackTrace", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_IdQuartzJob(), theEcorePackage.getEInt(), "idQuartzJob", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_IdQuartzTrigger(), theEcorePackage.getEIntegerObject(), "idQuartzTrigger", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_LastJobGenerationDate(), theEcorePackage.getEDate(), "lastJobGenerationDate", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_JobArchiveFilename(), theEcorePackage.getEString(), "jobArchiveFilename", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_FileTriggerFileMask(), theEcorePackage.getEString(), "fileTriggerFileMask", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_FileTriggerFileName(), theEcorePackage.getEString(), "fileTriggerFileName", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_FileTriggerFolderPath(), theEcorePackage.getEString(), "fileTriggerFolderPath", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_FileTriggerTriggeredFilePath(), theEcorePackage.getEString(), "fileTriggerTriggeredFilePath", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_ExpectedTriggeringDate(), theEcorePackage.getEDate(), "expectedTriggeringDate", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_TaskStartDate(), theEcorePackage.getEDate(), "taskStartDate", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_TaskEndDate(), theEcorePackage.getEDate(), "taskEndDate", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_ServerJobStartDate(), theEcorePackage.getEDate(), "serverJobStartDate", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_ServerJobEndDate(), theEcorePackage.getEDate(), "serverJobEndDate", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_IdRemoteJob(), theEcorePackage.getEString(), "idRemoteJob", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_IdRemoteJobExecution(), theEcorePackage.getEString(), "idRemoteJobExecution", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_RequestId(), theEcorePackage.getEString(), "requestId", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_ResumingMode(), theEcorePackage.getEBoolean(), "resumingMode", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_ContextValues(), theEcorePackage.getEString(), "contextValues", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_JvmValues(), theEcorePackage.getEString(), "jvmValues", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_ParentTaskExecId(), theEcorePackage.getEInt(), "parentTaskExecId", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskExecutionHistory_ParentPlanExecId(), theEcorePackage.getEInt(), "parentPlanExecId", null, 0, 1, TaskExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(planExecutionHistoryEClass, PlanExecutionHistory.class, "PlanExecutionHistory", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPlanExecutionHistory_OriginalLabel(), theEcorePackage.getEString(), "originalLabel", null, 0, 1, PlanExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPlanExecutionHistory_CurrentLabel(), theEcorePackage.getEString(), "currentLabel", null, 0, 1, PlanExecutionHistory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(talendTriggerEClass, TalendTrigger.class, "TalendTrigger", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTalendTrigger_Id(), ecorePackage.getEInt(), "id", null, 1, 1, TalendTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTalendTrigger_Active(), theEcorePackage.getEBoolean(), "active", null, 0, 1, TalendTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTalendTrigger_Label(), theEcorePackage.getEString(), "label", null, 0, 1, TalendTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTalendTrigger_Description(), theEcorePackage.getEString(), "description", null, 0, 1, TalendTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTalendTrigger_TriggerType(), theEcorePackage.getEString(), "triggerType", null, 0, 1, TalendTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTalendTrigger_ExecutionTriggerable(), this.getExecutionTriggerable(), this.getExecutionTriggerable_Triggers(), "executionTriggerable", null, 0, 1, TalendTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTalendTrigger_StartTime(), theEcorePackage.getEDate(), "startTime", null, 0, 1, TalendTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTalendTrigger_EndTime(), theEcorePackage.getEDate(), "endTime", null, 0, 1, TalendTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTalendTrigger_PreviousFireTime(), theEcorePackage.getEDate(), "previousFireTime", null, 0, 1, TalendTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTalendTrigger_FinalFireTime(), theEcorePackage.getEDate(), "finalFireTime", null, 0, 1, TalendTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTalendTrigger_IdQuartzTrigger(), theEcorePackage.getEInt(), "idQuartzTrigger", null, 0, 1, TalendTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTalendTrigger_ResumePauseUpdated(), theEcorePackage.getEDate(), "resumePauseUpdated", null, 0, 1, TalendTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTalendTrigger_PreviouslyPaused(), theEcorePackage.getEBoolean(), "previouslyPaused", null, 0, 1, TalendTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(cronTalendTriggerEClass, CronTalendTrigger.class, "CronTalendTrigger", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCronTalendTrigger_CronExpression(), ecorePackage.getEString(), "cronExpression", null, 0, 1, CronTalendTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(cronUITalendTriggerEClass, CronUITalendTrigger.class, "CronUITalendTrigger", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCronUITalendTrigger_ListDaysOfWeek(), theEcorePackage.getEString(), "listDaysOfWeek", null, 0, 1, CronUITalendTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCronUITalendTrigger_ListDaysOfMonth(), theEcorePackage.getEString(), "listDaysOfMonth", null, 0, 1, CronUITalendTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCronUITalendTrigger_ListMonths(), theEcorePackage.getEString(), "listMonths", null, 0, 1, CronUITalendTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCronUITalendTrigger_ListYears(), theEcorePackage.getEString(), "listYears", null, 0, 1, CronUITalendTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCronUITalendTrigger_ListHours(), theEcorePackage.getEString(), "listHours", null, 0, 1, CronUITalendTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCronUITalendTrigger_ListMinutes(), theEcorePackage.getEString(), "listMinutes", null, 0, 1, CronUITalendTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(simpleTalendTriggerEClass, SimpleTalendTrigger.class, "SimpleTalendTrigger", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSimpleTalendTrigger_RepeatCount(), theEcorePackage.getEIntegerObject(), "repeatCount", null, 0, 1, SimpleTalendTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSimpleTalendTrigger_RepeatInterval(), ecorePackage.getELong(), "repeatInterval", null, 0, 1, SimpleTalendTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSimpleTalendTrigger_TimesTriggered(), ecorePackage.getEInt(), "timesTriggered", null, 0, 1, SimpleTalendTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(fileTriggerEClass, FileTrigger.class, "FileTrigger", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFileTrigger_FileTriggerMasks(), this.getFileTriggerMask(), null, "fileTriggerMasks", null, 0, -1, FileTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		initEClass(fileTriggerMaskEClass, FileTriggerMask.class, "FileTriggerMask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFileTriggerMask_Id(), ecorePackage.getEInt(), "id", null, 1, 1, FileTriggerMask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFileTriggerMask_Active(), theEcorePackage.getEBoolean(), "active", null, 0, 1, FileTriggerMask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFileTriggerMask_Label(), theEcorePackage.getEString(), "label", null, 0, 1, FileTriggerMask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFileTriggerMask_Description(), theEcorePackage.getEString(), "description", null, 0, 1, FileTriggerMask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFileTriggerMask_FileTrigger(), this.getFileTrigger(), null, "fileTrigger", null, 0, 1, FileTriggerMask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFileTriggerMask_FolderPath(), theEcorePackage.getEString(), "folderPath", null, 0, 1, FileTriggerMask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFileTriggerMask_FileMask(), theEcorePackage.getEString(), "fileMask", null, 0, 1, FileTriggerMask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFileTriggerMask_ContextParameterBaseName(), theEcorePackage.getEString(), "contextParameterBaseName", null, 0, 1, FileTriggerMask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFileTriggerMask_CheckFileServer(), this.getExecutionServer(), null, "checkFileServer", null, 0, 1, FileTriggerMask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFileTriggerMask_Exist(), theEcorePackage.getEBoolean(), "exist", null, 0, 1, FileTriggerMask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFileTriggerMask_Created(), theEcorePackage.getEBoolean(), "created", null, 0, 1, FileTriggerMask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFileTriggerMask_Modified(), theEcorePackage.getEBoolean(), "modified", null, 0, 1, FileTriggerMask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(executionServerEClass, ExecutionServer.class, "ExecutionServer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getExecutionServer_Id(), ecorePackage.getEInt(), "id", null, 1, 1, ExecutionServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionServer_Label(), theEcorePackage.getEString(), "label", null, 0, 1, ExecutionServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionServer_Description(), theEcorePackage.getEString(), "description", null, 0, 1, ExecutionServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionServer_Host(), theEcorePackage.getEString(), "host", null, 0, 1, ExecutionServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionServer_Port(), theEcorePackage.getEInt(), "port", "-1", 0, 1, ExecutionServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionServer_FileTransfertPort(), theEcorePackage.getEInt(), "fileTransfertPort", "-1", 0, 1, ExecutionServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionServer_Active(), theEcorePackage.getEBoolean(), "active", "true", 0, 1, ExecutionServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionServer_MonitoringPort(), theEcorePackage.getEInt(), "monitoringPort", "-1", 0, 1, ExecutionServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionServer_TimeoutUnknownState(), theEcorePackage.getEInt(), "timeoutUnknownState", "120", 0, 1, ExecutionServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionServer_Username(), theEcorePackage.getEString(), "username", null, 0, 1, ExecutionServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionServer_Password(), theEcorePackage.getEString(), "password", null, 0, 1, ExecutionServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionServer_JmxUrl(), theEcorePackage.getEString(), "jmxUrl", null, 0, 1, ExecutionServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionServer_WebConsoleUrl(), theEcorePackage.getEString(), "webConsoleUrl", null, 0, 1, ExecutionServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionServer_TalendRuntime(), theEcorePackage.getEBoolean(), "talendRuntime", null, 0, 1, ExecutionServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionServer_MgmtServerPort(), theEcorePackage.getEInt(), "mgmtServerPort", "-1", 0, 1, ExecutionServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionServer_MgmtRegPort(), theEcorePackage.getEInt(), "mgmtRegPort", "-1", 0, 1, ExecutionServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionServer_AdminConsolePort(), theEcorePackage.getEInt(), "adminConsolePort", "-1", 0, 1, ExecutionServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionServer_UseSSL(), theEcorePackage.getEBoolean(), "useSSL", "false", 0, 1, ExecutionServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionServer_Instance(), theEcorePackage.getEString(), "instance", null, 0, 1, ExecutionServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(executionVirtualServerEClass, ExecutionVirtualServer.class, "ExecutionVirtualServer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getExecutionVirtualServer_Id(), ecorePackage.getEInt(), "id", null, 1, 1, ExecutionVirtualServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionVirtualServer_Label(), theEcorePackage.getEString(), "label", null, 0, 1, ExecutionVirtualServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionVirtualServer_Description(), theEcorePackage.getEString(), "description", null, 0, 1, ExecutionVirtualServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionVirtualServer_Active(), theEcorePackage.getEBoolean(), "active", null, 0, 1, ExecutionVirtualServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getExecutionVirtualServer_ExecutionServers(), this.getExecutionServer(), null, "executionServers", null, 0, -1, ExecutionVirtualServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, !IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		initEClass(jobletProcessItemEClass, JobletProcessItem.class, "JobletProcessItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getJobletProcessItem_JobletProcess(), theJobletPackage.getJobletProcess(), null, "jobletProcess", null, 0, 1, JobletProcessItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getJobletProcessItem_Icon(), this.getByteArray(), null, "icon", null, 0, 1, JobletProcessItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(jobDocumentationItemEClass, JobDocumentationItem.class, "JobDocumentationItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(jobletDocumentationItemEClass, JobletDocumentationItem.class, "JobletDocumentationItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(wsdlSchemaConnectionItemEClass, WSDLSchemaConnectionItem.class, "WSDLSchemaConnectionItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(schemaInformationEClass, SchemaInformation.class, "SchemaInformation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSchemaInformation_Version(), theEcorePackage.getEString(), "version", null, 0, 1, SchemaInformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(informationEClass, Information.class, "Information", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getInformation_Level(), this.getInformationLevel(), "level", null, 1, 1, Information.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInformation_Type(), theEcorePackage.getEString(), "type", null, 0, 1, Information.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInformation_Text(), theEcorePackage.getEString(), "text", null, 0, 1, Information.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(sqlPatternItemEClass, SQLPatternItem.class, "SQLPatternItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSQLPatternItem_System(), ecorePackage.getEBoolean(), "system", "false", 0, 1, SQLPatternItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSQLPatternItem_EltName(), theEcorePackage.getEString(), "eltName", null, 0, 1, SQLPatternItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(componentSettingEClass, ComponentSetting.class, "ComponentSetting", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getComponentSetting_Name(), theEcorePackage.getEString(), "name", null, 0, 1, ComponentSetting.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getComponentSetting_Hidden(), theEcorePackage.getEBoolean(), "hidden", null, 0, 1, ComponentSetting.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getComponentSetting_Family(), theEcorePackage.getEString(), "family", null, 0, 1, ComponentSetting.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(statAndLogsSettingsEClass, StatAndLogsSettings.class, "StatAndLogsSettings", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getStatAndLogsSettings_Parameters(), theTalendFilePackage.getParametersType(), null, "parameters", null, 0, 1, StatAndLogsSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(implicitContextSettingsEClass, ImplicitContextSettings.class, "ImplicitContextSettings", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getImplicitContextSettings_Parameters(), theTalendFilePackage.getParametersType(), null, "parameters", null, 0, 1, ImplicitContextSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(soaOperationEClass, SoaOperation.class, "SoaOperation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSoaOperation_Id(), ecorePackage.getEInt(), "id", null, 1, 1, SoaOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaOperation_Label(), theEcorePackage.getEString(), "label", null, 0, 1, SoaOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaOperation_Description(), theEcorePackage.getEString(), "description", null, 0, 1, SoaOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSoaOperation_Project(), this.getProject(), null, "project", null, 0, 1, SoaOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaOperation_Context(), theEcorePackage.getEString(), "context", null, 0, 1, SoaOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaOperation_JobVersion(), theEcorePackage.getEString(), "jobVersion", null, 0, 1, SoaOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaOperation_JobName(), theEcorePackage.getEString(), "jobName", null, 0, 1, SoaOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaOperation_Active(), theEcorePackage.getEBoolean(), "active", null, 0, 1, SoaOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaOperation_LastScriptGenerationDate(), theEcorePackage.getEDate(), "lastScriptGenerationDate", null, 0, 1, SoaOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaOperation_JobId(), ecorePackage.getEString(), "jobId", null, 0, 1, SoaOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaOperation_ApplyContextToChildren(), theEcorePackage.getEBoolean(), "applyContextToChildren", null, 0, 1, SoaOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSoaOperation_InputParameters(), this.getSoaInputParameter(), null, "inputParameters", null, 0, -1, SoaOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		initEAttribute(getSoaOperation_JvmParameters(), theEcorePackage.getEString(), "jvmParameters", null, 0, 1, SoaOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaOperation_Xms(), theEcorePackage.getEInt(), "xms", null, 0, 1, SoaOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaOperation_Xmx(), theEcorePackage.getEInt(), "xmx", null, 0, 1, SoaOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaOperation_MinJobInstances(), theEcorePackage.getEInt(), "minJobInstances", null, 0, 1, SoaOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaOperation_MaxJobInstances(), theEcorePackage.getEInt(), "maxJobInstances", null, 0, 1, SoaOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaOperation_IdleTTL_forAllInstances(), theEcorePackage.getEInt(), "idleTTL_forAllInstances", null, 0, 1, SoaOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaOperation_IdleTTL_forAdditionalInstances(), theEcorePackage.getEInt(), "idleTTL_forAdditionalInstances", null, 0, 1, SoaOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaOperation_QueueMaxSize(), theEcorePackage.getEInt(), "queueMaxSize", null, 0, 1, SoaOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaOperation_RequestInQueueTTL(), theEcorePackage.getEInt(), "requestInQueueTTL", null, 0, 1, SoaOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSoaOperation_Service(), this.getSoaService(), this.getSoaService_Operations(), "service", null, 0, 1, SoaOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaOperation_ReturnStyle(), ecorePackage.getEString(), "returnStyle", null, 0, 1, SoaOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaOperation_Branch(), ecorePackage.getEString(), "branch", null, 0, 1, SoaOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(soaInputParameterEClass, SoaInputParameter.class, "SoaInputParameter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSoaInputParameter_Id(), ecorePackage.getEInt(), "id", null, 1, 1, SoaInputParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaInputParameter_Label(), theEcorePackage.getEString(), "label", null, 0, 1, SoaInputParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSoaInputParameter_Operation(), this.getSoaOperation(), null, "operation", null, 0, 1, SoaInputParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaInputParameter_DefaultValue(), theEcorePackage.getEString(), "defaultValue", null, 0, 1, SoaInputParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaInputParameter_ExposedName(), theEcorePackage.getEString(), "exposedName", null, 0, 1, SoaInputParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaInputParameter_Exposed(), theEcorePackage.getEBoolean(), "exposed", null, 0, 1, SoaInputParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(soaServiceEClass, SoaService.class, "SoaService", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSoaService_Id(), ecorePackage.getEInt(), "id", null, 1, 1, SoaService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaService_Label(), theEcorePackage.getEString(), "label", null, 0, 1, SoaService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaService_NameSpace(), theEcorePackage.getEString(), "nameSpace", null, 0, 1, SoaService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaService_Contact(), theEcorePackage.getEString(), "contact", null, 0, 1, SoaService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaService_Description(), theEcorePackage.getEString(), "description", null, 0, 1, SoaService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaService_Creation(), theEcorePackage.getEDate(), "creation", null, 0, 1, SoaService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaService_Modification(), theEcorePackage.getEDate(), "modification", null, 0, 1, SoaService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaService_Port(), theEcorePackage.getEInt(), "port", null, 0, 1, SoaService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaService_Type(), theEcorePackage.getEString(), "type", null, 0, 1, SoaService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaService_Style(), theEcorePackage.getEString(), "style", null, 0, 1, SoaService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaService_UsedType(), theEcorePackage.getEString(), "usedType", null, 0, 1, SoaService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSoaService_ParamStyle(), theEcorePackage.getEString(), "paramStyle", null, 0, 1, SoaService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSoaService_Operations(), this.getSoaOperation(), this.getSoaOperation_Service(), "operations", null, 0, -1, SoaService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		initEAttribute(getSoaService_Status(), theEcorePackage.getEString(), "status", null, 0, 1, SoaService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(rulesItemEClass, RulesItem.class, "RulesItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(userRightEClass, UserRight.class, "UserRight", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getUserRight_Id(), ecorePackage.getEInt(), "id", null, 1, 1, UserRight.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUserRight_Name(), ecorePackage.getEString(), "name", null, 1, 1, UserRight.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUserRight_Description(), ecorePackage.getEString(), "description", null, 1, 1, UserRight.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getUserRight_RolesRights(), this.getRoleRight(), this.getRoleRight_UserRight(), "rolesRights", null, 0, -1, UserRight.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		initEClass(roleRightEClass, RoleRight.class, "RoleRight", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRoleRight_Role(), this.getUserRole(), this.getUserRole_RolesRights(), "role", null, 0, 1, RoleRight.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRoleRight_UserRight(), this.getUserRight(), this.getUserRight_RolesRights(), "userRight", null, 0, 1, RoleRight.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(svgBusinessProcessItemEClass, SVGBusinessProcessItem.class, "SVGBusinessProcessItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSVGBusinessProcessItem_BusinessProcessItem(), this.getBusinessProcessItem(), this.getBusinessProcessItem_SvgBusinessProcessItem(), "businessProcessItem", null, 0, 1, SVGBusinessProcessItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(linkRulesItemEClass, LinkRulesItem.class, "LinkRulesItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getLinkRulesItem_Name(), theEcorePackage.getEString(), "name", null, 0, 1, LinkRulesItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLinkRulesItem_Extension(), ecorePackage.getEString(), "extension", null, 0, 1, LinkRulesItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLinkRulesItem_Link(), this.getLinkType(), null, "link", null, 0, 1, LinkRulesItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(itemRelationsEClass, ItemRelations.class, "ItemRelations", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getItemRelations_BaseItem(), this.getItemRelation(), null, "baseItem", null, 0, 1, ItemRelations.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getItemRelations_RelatedItems(), this.getItemRelation(), null, "relatedItems", null, 0, -1, ItemRelations.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(itemRelationEClass, ItemRelation.class, "ItemRelation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getItemRelation_Id(), theEcorePackage.getEString(), "id", null, 0, 1, ItemRelation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getItemRelation_Version(), theEcorePackage.getEString(), "version", null, 0, 1, ItemRelation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getItemRelation_Type(), theEcorePackage.getEString(), "type", null, 0, 1, ItemRelation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(notificationEClass, Notification.class, "Notification", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getNotification_Id(), ecorePackage.getEInt(), "id", null, 1, 1, Notification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNotification_Type(), theEcorePackage.getEString(), "type", null, 1, 1, Notification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNotification_Props(), theEcorePackage.getEString(), "props", null, 0, 1, Notification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNotification_Enabled(), theEcorePackage.getEBoolean(), "enabled", null, 0, 1, Notification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(hl7ConnectionItemEClass, HL7ConnectionItem.class, "HL7ConnectionItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(userRoleReferenceEClass, UserRoleReference.class, "UserRoleReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getUserRoleReference_User(), this.getUser(), null, "user", null, 1, 1, UserRoleReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getUserRoleReference_Role(), this.getUserRole(), null, "role", null, 1, 1, UserRoleReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(headerFooterConnectionItemEClass, HeaderFooterConnectionItem.class, "HeaderFooterConnectionItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(ftpConnectionItemEClass, FTPConnectionItem.class, "FTPConnectionItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(customComponentSettingEClass, CustomComponentSetting.class, "CustomComponentSetting", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCustomComponentSetting_Name(), theEcorePackage.getEString(), "name", null, 0, 1, CustomComponentSetting.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCustomComponentSetting_Share(), theEcorePackage.getEBoolean(), "share", null, 0, 1, CustomComponentSetting.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(tdqItemEClass, TDQItem.class, "TDQItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTDQItem_Filename(), theEcorePackage.getEString(), "filename", null, 0, 1, TDQItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(brmsConnectionItemEClass, BRMSConnectionItem.class, "BRMSConnectionItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(jobScriptItemEClass, JobScriptItem.class, "JobScriptItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(edifactConnectionItemEClass, EDIFACTConnectionItem.class, "EDIFACTConnectionItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(technicalVariableEClass, TechnicalVariable.class, "TechnicalVariable", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTechnicalVariable_Key(), theEcorePackage.getEString(), "key", null, 0, 1, TechnicalVariable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTechnicalVariable_Value(), theEcorePackage.getEString(), "value", null, 0, 1, TechnicalVariable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(referenceFileItemEClass, ReferenceFileItem.class, "ReferenceFileItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getReferenceFileItem_Name(), ecorePackage.getEString(), "name", null, 0, 1, ReferenceFileItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReferenceFileItem_Extension(), ecorePackage.getEString(), "extension", null, 0, 1, ReferenceFileItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getReferenceFileItem_Content(), this.getByteArray(), null, "content", null, 0, 1, ReferenceFileItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(referenceItemEClass, ReferenceItem.class, "ReferenceItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getReferenceItem_State(), this.getItemState(), null, "state", null, 0, 1, ReferenceItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getReferenceItem_Parent(), theEcorePackage.getEObject(), null, "parent", null, 0, 1, ReferenceItem.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReferenceItem_ReloadFromFile(), theEcorePackage.getEBoolean(), "reloadFromFile", "true", 0, 1, ReferenceItem.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(exchangeUserEClass, ExchangeUser.class, "ExchangeUser", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getExchangeUser_Username(), ecorePackage.getEString(), "username", null, 0, 1, ExchangeUser.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExchangeUser_Login(), theXMLTypePackage.getString(), "login", null, 0, 1, ExchangeUser.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExchangeUser_Password(), theXMLTypePackage.getString(), "password", null, 0, 1, ExchangeUser.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(artifactNotificationEClass, ArtifactNotification.class, "ArtifactNotification", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getArtifactNotification_Name(), ecorePackage.getEString(), "name", null, 1, 1, ArtifactNotification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(additionalInfoMapEClass, Map.Entry.class, "AdditionalInfoMap", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAdditionalInfoMap_Key(), theEcorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAdditionalInfoMap_Value(), theEcorePackage.getEString(), "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(migrationTaskEClass, MigrationTask.class, "MigrationTask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMigrationTask_Id(), ecorePackage.getEString(), "id", null, 1, 1, MigrationTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMigrationTask_Breaks(), ecorePackage.getEString(), "breaks", "5.1.9", 0, 1, MigrationTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMigrationTask_Version(), ecorePackage.getEString(), "version", null, 1, 1, MigrationTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMigrationTask_Status(), this.getMigrationStatus(), "status", "0", 1, 1, MigrationTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(pigudfItemEClass, PigudfItem.class, "PigudfItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		// Initialize enums and add enum literals
		initEEnum(folderTypeEEnum, FolderType.class, "FolderType");
		addEEnumLiteral(folderTypeEEnum, FolderType.FOLDER_LITERAL);
		addEEnumLiteral(folderTypeEEnum, FolderType.SYSTEM_FOLDER_LITERAL);
		addEEnumLiteral(folderTypeEEnum, FolderType.STABLE_SYSTEM_FOLDER_LITERAL);

		initEEnum(userProjectAuthorizationTypeEEnum, UserProjectAuthorizationType.class, "UserProjectAuthorizationType");
		addEEnumLiteral(userProjectAuthorizationTypeEEnum, UserProjectAuthorizationType.READ_WRITE_LITERAL);
		addEEnumLiteral(userProjectAuthorizationTypeEEnum, UserProjectAuthorizationType.READ_ONLY_LITERAL);

		initEEnum(userModuleAuthorizationTypeEEnum, UserModuleAuthorizationType.class, "UserModuleAuthorizationType");
		addEEnumLiteral(userModuleAuthorizationTypeEEnum, UserModuleAuthorizationType.JOB_CONDUCTOR_LITERAL);
		addEEnumLiteral(userModuleAuthorizationTypeEEnum, UserModuleAuthorizationType.DASHBOARD_LITERAL);

		initEEnum(informationLevelEEnum, InformationLevel.class, "InformationLevel");
		addEEnumLiteral(informationLevelEEnum, InformationLevel.DEBUG_LITERAL);
		addEEnumLiteral(informationLevelEEnum, InformationLevel.INFO_LITERAL);
		addEEnumLiteral(informationLevelEEnum, InformationLevel.WARN_LITERAL);
		addEEnumLiteral(informationLevelEEnum, InformationLevel.ERROR_LITERAL);

		initEEnum(migrationStatusEEnum, MigrationStatus.class, "MigrationStatus");
		addEEnumLiteral(migrationStatusEEnum, MigrationStatus.DEFAULT_LITERAL);
		addEEnumLiteral(migrationStatusEEnum, MigrationStatus.OK_LITERAL);
		addEEnumLiteral(migrationStatusEEnum, MigrationStatus.FAILED_LITERAL);
		addEEnumLiteral(migrationStatusEEnum, MigrationStatus.NOIMPACT_LITERAL);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http:///org/eclipse/emf/ecore/util/ExtendedMetaData
		createExtendedMetaDataAnnotations();
		// MapEntry
		createMapEntryAnnotations();
	}

    /**
	 * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected void createExtendedMetaDataAnnotations() {
		String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";	
		addAnnotation
		  (getStatAndLogsSettings_Parameters(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "Parameters",
			 "namespace", "##targetNamespace"
		   });	
		addAnnotation
		  (getImplicitContextSettings_Parameters(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "Parameters",
			 "namespace", "##targetNamespace"
		   });
	}

    /**
	 * Initializes the annotations for <b>MapEntry</b>.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected void createMapEntryAnnotations() {
		String source = "MapEntry";	
		addAnnotation
		  (additionalInfoMapEClass, 
		   source, 
		   new String[] {
		   });
	}

} //PropertiesPackageImpl
