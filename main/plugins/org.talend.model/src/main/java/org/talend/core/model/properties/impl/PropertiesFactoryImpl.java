/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.properties.impl;

import java.util.Map;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.talend.core.model.properties.*;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!-- end-user-doc -->
 * @generated
 */
public class PropertiesFactoryImpl extends EFactoryImpl implements PropertiesFactory {

    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public static PropertiesFactory init() {
        try {
            PropertiesFactory thePropertiesFactory = (PropertiesFactory)EPackage.Registry.INSTANCE.getEFactory(PropertiesPackage.eNS_URI);
            if (thePropertiesFactory != null) {
                return thePropertiesFactory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new PropertiesFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public PropertiesFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            case PropertiesPackage.PROJECT: return createProject();
            case PropertiesPackage.PROJECT_COMPONENT_AUTHORISATION: return createProjectComponentAuthorisation();
            case PropertiesPackage.PROJECT_REFERENCE: return createProjectReference();
            case PropertiesPackage.STATUS: return createStatus();
            case PropertiesPackage.ITEM_STATE: return createItemState();
            case PropertiesPackage.PROPERTY: return createProperty();
            case PropertiesPackage.LINK_DOCUMENTATION_ITEM: return createLinkDocumentationItem();
            case PropertiesPackage.LINK_TYPE: return createLinkType();
            case PropertiesPackage.BUSINESS_PROCESS_ITEM: return createBusinessProcessItem();
            case PropertiesPackage.BYTE_ARRAY: return createByteArray();
            case PropertiesPackage.DOCUMENTATION_ITEM: return createDocumentationItem();
            case PropertiesPackage.ROUTINE_ITEM: return createRoutineItem();
            case PropertiesPackage.CONNECTION_ITEM: return createConnectionItem();
            case PropertiesPackage.SNIPPET_VARIABLE: return createSnippetVariable();
            case PropertiesPackage.SNIPPET_ITEM: return createSnippetItem();
            case PropertiesPackage.DELIMITED_FILE_CONNECTION_ITEM: return createDelimitedFileConnectionItem();
            case PropertiesPackage.POSITIONAL_FILE_CONNECTION_ITEM: return createPositionalFileConnectionItem();
            case PropertiesPackage.REG_EX_FILE_CONNECTION_ITEM: return createRegExFileConnectionItem();
            case PropertiesPackage.CSV_FILE_CONNECTION_ITEM: return createCSVFileConnectionItem();
            case PropertiesPackage.DATABASE_CONNECTION_ITEM: return createDatabaseConnectionItem();
            case PropertiesPackage.SAP_CONNECTION_ITEM: return createSAPConnectionItem();
            case PropertiesPackage.XML_FILE_CONNECTION_ITEM: return createXmlFileConnectionItem();
            case PropertiesPackage.LDIF_FILE_CONNECTION_ITEM: return createLdifFileConnectionItem();
            case PropertiesPackage.EXCEL_FILE_CONNECTION_ITEM: return createExcelFileConnectionItem();
            case PropertiesPackage.EBCDIC_CONNECTION_ITEM: return createEbcdicConnectionItem();
            case PropertiesPackage.MDM_CONNECTION_ITEM: return createMDMConnectionItem();
            case PropertiesPackage.VALIDATION_RULES_CONNECTION_ITEM: return createValidationRulesConnectionItem();
            case PropertiesPackage.PROCESS_ITEM: return createProcessItem();
            case PropertiesPackage.USER_ROLE: return createUserRole();
            case PropertiesPackage.USER: return createUser();
            case PropertiesPackage.FOLDER_ITEM: return createFolderItem();
            case PropertiesPackage.COMPONENT: return createComponent();
            case PropertiesPackage.NOTATION_HOLDER: return createNotationHolder();
            case PropertiesPackage.USER_PROJECT_AUTHORIZATION: return createUserProjectAuthorization();
            case PropertiesPackage.CONTEXT_ITEM: return createContextItem();
            case PropertiesPackage.SPAGO_BI_SERVER: return createSpagoBiServer();
            case PropertiesPackage.LICENSE: return createLicense();
            case PropertiesPackage.GENERIC_SCHEMA_CONNECTION_ITEM: return createGenericSchemaConnectionItem();
            case PropertiesPackage.USER_MODULE_AUTHORIZATION: return createUserModuleAuthorization();
            case PropertiesPackage.LDAP_SCHEMA_CONNECTION_ITEM: return createLDAPSchemaConnectionItem();
            case PropertiesPackage.SALESFORCE_SCHEMA_CONNECTION_ITEM: return createSalesforceSchemaConnectionItem();
            case PropertiesPackage.DASHBOARD_CONNECTION: return createDashboardConnection();
            case PropertiesPackage.EXECUTION_PLAN: return createExecutionPlan();
            case PropertiesPackage.EXECUTION_PLAN_PART: return createExecutionPlanPart();
            case PropertiesPackage.EXECUTION_PLAN_PRM: return createExecutionPlanPrm();
            case PropertiesPackage.EXECUTION_PLAN_PART_CMD_PRM: return createExecutionPlanPartCmdPrm();
            case PropertiesPackage.EXECUTION_PLAN_PART_JOB_PRM: return createExecutionPlanPartJobPrm();
            case PropertiesPackage.EXECUTION_TASK: return createExecutionTask();
            case PropertiesPackage.EXECUTION_TASK_PROPERTIES: return createExecutionTaskProperties();
            case PropertiesPackage.EXECUTION_TASK_CMD_PRM: return createExecutionTaskCmdPrm();
            case PropertiesPackage.EXECUTION_TASK_JOB_PRM: return createExecutionTaskJobPrm();
            case PropertiesPackage.TASK_EXECUTION_HISTORY: return createTaskExecutionHistory();
            case PropertiesPackage.PLAN_EXECUTION_HISTORY: return createPlanExecutionHistory();
            case PropertiesPackage.TALEND_TRIGGER: return createTalendTrigger();
            case PropertiesPackage.CRON_TALEND_TRIGGER: return createCronTalendTrigger();
            case PropertiesPackage.CRON_UI_TALEND_TRIGGER: return createCronUITalendTrigger();
            case PropertiesPackage.SIMPLE_TALEND_TRIGGER: return createSimpleTalendTrigger();
            case PropertiesPackage.FILE_TRIGGER: return createFileTrigger();
            case PropertiesPackage.FILE_TRIGGER_MASK: return createFileTriggerMask();
            case PropertiesPackage.EXECUTION_SERVER: return createExecutionServer();
            case PropertiesPackage.EXECUTION_VIRTUAL_SERVER: return createExecutionVirtualServer();
            case PropertiesPackage.JOBLET_PROCESS_ITEM: return createJobletProcessItem();
            case PropertiesPackage.JOB_DOCUMENTATION_ITEM: return createJobDocumentationItem();
            case PropertiesPackage.JOBLET_DOCUMENTATION_ITEM: return createJobletDocumentationItem();
            case PropertiesPackage.WSDL_SCHEMA_CONNECTION_ITEM: return createWSDLSchemaConnectionItem();
            case PropertiesPackage.SCHEMA_INFORMATION: return createSchemaInformation();
            case PropertiesPackage.INFORMATION: return createInformation();
            case PropertiesPackage.SQL_PATTERN_ITEM: return createSQLPatternItem();
            case PropertiesPackage.COMPONENT_SETTING: return createComponentSetting();
            case PropertiesPackage.STAT_AND_LOGS_SETTINGS: return createStatAndLogsSettings();
            case PropertiesPackage.IMPLICIT_CONTEXT_SETTINGS: return createImplicitContextSettings();
            case PropertiesPackage.SOA_OPERATION: return createSoaOperation();
            case PropertiesPackage.SOA_INPUT_PARAMETER: return createSoaInputParameter();
            case PropertiesPackage.SOA_SERVICE: return createSoaService();
            case PropertiesPackage.RULES_ITEM: return createRulesItem();
            case PropertiesPackage.USER_RIGHT: return createUserRight();
            case PropertiesPackage.ROLE_RIGHT: return createRoleRight();
            case PropertiesPackage.SVG_BUSINESS_PROCESS_ITEM: return createSVGBusinessProcessItem();
            case PropertiesPackage.LINK_RULES_ITEM: return createLinkRulesItem();
            case PropertiesPackage.ITEM_RELATIONS: return createItemRelations();
            case PropertiesPackage.ITEM_RELATION: return createItemRelation();
            case PropertiesPackage.NOTIFICATION: return createNotification();
            case PropertiesPackage.HL7_CONNECTION_ITEM: return createHL7ConnectionItem();
            case PropertiesPackage.USER_ROLE_REFERENCE: return createUserRoleReference();
            case PropertiesPackage.HEADER_FOOTER_CONNECTION_ITEM: return createHeaderFooterConnectionItem();
            case PropertiesPackage.FTP_CONNECTION_ITEM: return createFTPConnectionItem();
            case PropertiesPackage.CUSTOM_COMPONENT_SETTING: return createCustomComponentSetting();
            case PropertiesPackage.TDQ_ITEM: return createTDQItem();
            case PropertiesPackage.BRMS_CONNECTION_ITEM: return createBRMSConnectionItem();
            case PropertiesPackage.JOB_SCRIPT_ITEM: return createJobScriptItem();
            case PropertiesPackage.EDIFACT_CONNECTION_ITEM: return createEDIFACTConnectionItem();
            case PropertiesPackage.TECHNICAL_VARIABLE: return createTechnicalVariable();
            case PropertiesPackage.REFERENCE_FILE_ITEM: return createReferenceFileItem();
            case PropertiesPackage.REFERENCE_ITEM: return createReferenceItem();
            case PropertiesPackage.EXCHANGE_USER: return createExchangeUser();
            case PropertiesPackage.ARTIFACT_NOTIFICATION: return createArtifactNotification();
            case PropertiesPackage.ADDITIONAL_INFO_MAP: return (EObject)createAdditionalInfoMap();
            case PropertiesPackage.MIGRATION_TASK: return createMigrationTask();
            case PropertiesPackage.PIGUDF_ITEM: return createPigudfItem();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Object createFromString(EDataType eDataType, String initialValue) {
        switch (eDataType.getClassifierID()) {
            case PropertiesPackage.FOLDER_TYPE:
                return createFolderTypeFromString(eDataType, initialValue);
            case PropertiesPackage.USER_PROJECT_AUTHORIZATION_TYPE:
                return createUserProjectAuthorizationTypeFromString(eDataType, initialValue);
            case PropertiesPackage.USER_MODULE_AUTHORIZATION_TYPE:
                return createUserModuleAuthorizationTypeFromString(eDataType, initialValue);
            case PropertiesPackage.INFORMATION_LEVEL:
                return createInformationLevelFromString(eDataType, initialValue);
            case PropertiesPackage.MIGRATION_STATUS:
                return createMigrationStatusFromString(eDataType, initialValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String convertToString(EDataType eDataType, Object instanceValue) {
        switch (eDataType.getClassifierID()) {
            case PropertiesPackage.FOLDER_TYPE:
                return convertFolderTypeToString(eDataType, instanceValue);
            case PropertiesPackage.USER_PROJECT_AUTHORIZATION_TYPE:
                return convertUserProjectAuthorizationTypeToString(eDataType, instanceValue);
            case PropertiesPackage.USER_MODULE_AUTHORIZATION_TYPE:
                return convertUserModuleAuthorizationTypeToString(eDataType, instanceValue);
            case PropertiesPackage.INFORMATION_LEVEL:
                return convertInformationLevelToString(eDataType, instanceValue);
            case PropertiesPackage.MIGRATION_STATUS:
                return convertMigrationStatusToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Project createProject() {
        ProjectImpl project = new ProjectImpl();
        return project;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ProjectComponentAuthorisation createProjectComponentAuthorisation() {
        ProjectComponentAuthorisationImpl projectComponentAuthorisation = new ProjectComponentAuthorisationImpl();
        return projectComponentAuthorisation;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ProjectReference createProjectReference() {
        ProjectReferenceImpl projectReference = new ProjectReferenceImpl();
        return projectReference;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Status createStatus() {
        StatusImpl status = new StatusImpl();
        return status;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ItemState createItemState() {
        ItemStateImpl itemState = new ItemStateImpl();
        return itemState;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Property createProperty() {
        PropertyImpl property = new PropertyImpl();
        return property;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public LinkDocumentationItem createLinkDocumentationItem() {
        LinkDocumentationItemImpl linkDocumentationItem = new LinkDocumentationItemImpl();
        return linkDocumentationItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public LinkType createLinkType() {
        LinkTypeImpl linkType = new LinkTypeImpl();
        return linkType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    public BusinessProcessItem createBusinessProcessItem() {
        BusinessProcessItemImpl businessProcessItem = new BusinessProcessItemImpl();
        businessProcessItem.setNotationHolder(createNotationHolder());
        return businessProcessItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ByteArray createByteArray() {
        ByteArrayImpl byteArray = new ByteArrayImpl();
        return byteArray;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public DocumentationItem createDocumentationItem() {
        DocumentationItemImpl documentationItem = new DocumentationItemImpl();
        return documentationItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public RoutineItem createRoutineItem() {
        RoutineItemImpl routineItem = new RoutineItemImpl();
        return routineItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ConnectionItem createConnectionItem() {
        ConnectionItemImpl connectionItem = new ConnectionItemImpl();
        return connectionItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public SnippetVariable createSnippetVariable() {
        SnippetVariableImpl snippetVariable = new SnippetVariableImpl();
        return snippetVariable;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public SnippetItem createSnippetItem() {
        SnippetItemImpl snippetItem = new SnippetItemImpl();
        return snippetItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public DelimitedFileConnectionItem createDelimitedFileConnectionItem() {
        DelimitedFileConnectionItemImpl delimitedFileConnectionItem = new DelimitedFileConnectionItemImpl();
        return delimitedFileConnectionItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public PositionalFileConnectionItem createPositionalFileConnectionItem() {
        PositionalFileConnectionItemImpl positionalFileConnectionItem = new PositionalFileConnectionItemImpl();
        return positionalFileConnectionItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public RegExFileConnectionItem createRegExFileConnectionItem() {
        RegExFileConnectionItemImpl regExFileConnectionItem = new RegExFileConnectionItemImpl();
        return regExFileConnectionItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public CSVFileConnectionItem createCSVFileConnectionItem() {
        CSVFileConnectionItemImpl csvFileConnectionItem = new CSVFileConnectionItemImpl();
        return csvFileConnectionItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public DatabaseConnectionItem createDatabaseConnectionItem() {
        DatabaseConnectionItemImpl databaseConnectionItem = new DatabaseConnectionItemImpl();
        return databaseConnectionItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public SAPConnectionItem createSAPConnectionItem() {
        SAPConnectionItemImpl sapConnectionItem = new SAPConnectionItemImpl();
        return sapConnectionItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public XmlFileConnectionItem createXmlFileConnectionItem() {
        XmlFileConnectionItemImpl xmlFileConnectionItem = new XmlFileConnectionItemImpl();
        return xmlFileConnectionItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public LdifFileConnectionItem createLdifFileConnectionItem() {
        LdifFileConnectionItemImpl ldifFileConnectionItem = new LdifFileConnectionItemImpl();
        return ldifFileConnectionItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ExcelFileConnectionItem createExcelFileConnectionItem() {
        ExcelFileConnectionItemImpl excelFileConnectionItem = new ExcelFileConnectionItemImpl();
        return excelFileConnectionItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EbcdicConnectionItem createEbcdicConnectionItem() {
        EbcdicConnectionItemImpl ebcdicConnectionItem = new EbcdicConnectionItemImpl();
        return ebcdicConnectionItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public MDMConnectionItem createMDMConnectionItem() {
        MDMConnectionItemImpl mdmConnectionItem = new MDMConnectionItemImpl();
        return mdmConnectionItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ValidationRulesConnectionItem createValidationRulesConnectionItem() {
        ValidationRulesConnectionItemImpl validationRulesConnectionItem = new ValidationRulesConnectionItemImpl();
        return validationRulesConnectionItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ProcessItem createProcessItem() {
        ProcessItemImpl processItem = new ProcessItemImpl();
        return processItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public UserRole createUserRole() {
        UserRoleImpl userRole = new UserRoleImpl();
        return userRole;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public User createUser() {
        UserImpl user = new UserImpl();
        return user;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public FolderItem createFolderItem() {
        FolderItemImpl folderItem = new FolderItemImpl();
        return folderItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Component createComponent() {
        ComponentImpl component = new ComponentImpl();
        return component;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public NotationHolder createNotationHolder() {
        NotationHolderImpl notationHolder = new NotationHolderImpl();
        return notationHolder;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public UserProjectAuthorization createUserProjectAuthorization() {
        UserProjectAuthorizationImpl userProjectAuthorization = new UserProjectAuthorizationImpl();
        return userProjectAuthorization;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ContextItem createContextItem() {
        ContextItemImpl contextItem = new ContextItemImpl();
        return contextItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public SpagoBiServer createSpagoBiServer() {
        SpagoBiServerImpl spagoBiServer = new SpagoBiServerImpl();
        return spagoBiServer;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public License createLicense() {
        LicenseImpl license = new LicenseImpl();
        return license;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public GenericSchemaConnectionItem createGenericSchemaConnectionItem() {
        GenericSchemaConnectionItemImpl genericSchemaConnectionItem = new GenericSchemaConnectionItemImpl();
        return genericSchemaConnectionItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public UserModuleAuthorization createUserModuleAuthorization() {
        UserModuleAuthorizationImpl userModuleAuthorization = new UserModuleAuthorizationImpl();
        return userModuleAuthorization;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public LDAPSchemaConnectionItem createLDAPSchemaConnectionItem() {
        LDAPSchemaConnectionItemImpl ldapSchemaConnectionItem = new LDAPSchemaConnectionItemImpl();
        return ldapSchemaConnectionItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public SalesforceSchemaConnectionItem createSalesforceSchemaConnectionItem() {
        SalesforceSchemaConnectionItemImpl salesforceSchemaConnectionItem = new SalesforceSchemaConnectionItemImpl();
        return salesforceSchemaConnectionItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public DashboardConnection createDashboardConnection() {
        DashboardConnectionImpl dashboardConnection = new DashboardConnectionImpl();
        return dashboardConnection;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ExecutionPlan createExecutionPlan() {
        ExecutionPlanImpl executionPlan = new ExecutionPlanImpl();
        return executionPlan;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ExecutionPlanPart createExecutionPlanPart() {
        ExecutionPlanPartImpl executionPlanPart = new ExecutionPlanPartImpl();
        return executionPlanPart;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ExecutionPlanPrm createExecutionPlanPrm() {
        ExecutionPlanPrmImpl executionPlanPrm = new ExecutionPlanPrmImpl();
        return executionPlanPrm;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ExecutionPlanPartCmdPrm createExecutionPlanPartCmdPrm() {
        ExecutionPlanPartCmdPrmImpl executionPlanPartCmdPrm = new ExecutionPlanPartCmdPrmImpl();
        return executionPlanPartCmdPrm;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ExecutionPlanPartJobPrm createExecutionPlanPartJobPrm() {
        ExecutionPlanPartJobPrmImpl executionPlanPartJobPrm = new ExecutionPlanPartJobPrmImpl();
        return executionPlanPartJobPrm;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ExecutionTask createExecutionTask() {
        ExecutionTaskImpl executionTask = new ExecutionTaskImpl();
        return executionTask;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExecutionTaskProperties createExecutionTaskProperties() {
        ExecutionTaskPropertiesImpl executionTaskProperties = new ExecutionTaskPropertiesImpl();
        return executionTaskProperties;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ExecutionTaskCmdPrm createExecutionTaskCmdPrm() {
        ExecutionTaskCmdPrmImpl executionTaskCmdPrm = new ExecutionTaskCmdPrmImpl();
        return executionTaskCmdPrm;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ExecutionTaskJobPrm createExecutionTaskJobPrm() {
        ExecutionTaskJobPrmImpl executionTaskJobPrm = new ExecutionTaskJobPrmImpl();
        return executionTaskJobPrm;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public TaskExecutionHistory createTaskExecutionHistory() {
        TaskExecutionHistoryImpl taskExecutionHistory = new TaskExecutionHistoryImpl();
        return taskExecutionHistory;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public PlanExecutionHistory createPlanExecutionHistory() {
        PlanExecutionHistoryImpl planExecutionHistory = new PlanExecutionHistoryImpl();
        return planExecutionHistory;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public TalendTrigger createTalendTrigger() {
        TalendTriggerImpl talendTrigger = new TalendTriggerImpl();
        return talendTrigger;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public CronTalendTrigger createCronTalendTrigger() {
        CronTalendTriggerImpl cronTalendTrigger = new CronTalendTriggerImpl();
        return cronTalendTrigger;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public CronUITalendTrigger createCronUITalendTrigger() {
        CronUITalendTriggerImpl cronUITalendTrigger = new CronUITalendTriggerImpl();
        return cronUITalendTrigger;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public SimpleTalendTrigger createSimpleTalendTrigger() {
        SimpleTalendTriggerImpl simpleTalendTrigger = new SimpleTalendTriggerImpl();
        return simpleTalendTrigger;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public FileTrigger createFileTrigger() {
        FileTriggerImpl fileTrigger = new FileTriggerImpl();
        return fileTrigger;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public FileTriggerMask createFileTriggerMask() {
        FileTriggerMaskImpl fileTriggerMask = new FileTriggerMaskImpl();
        return fileTriggerMask;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ExecutionServer createExecutionServer() {
        ExecutionServerImpl executionServer = new ExecutionServerImpl();
        return executionServer;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ExecutionVirtualServer createExecutionVirtualServer() {
        ExecutionVirtualServerImpl executionVirtualServer = new ExecutionVirtualServerImpl();
        return executionVirtualServer;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public JobletProcessItem createJobletProcessItem() {
        JobletProcessItemImpl jobletProcessItem = new JobletProcessItemImpl();
        return jobletProcessItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public JobDocumentationItem createJobDocumentationItem() {
        JobDocumentationItemImpl jobDocumentationItem = new JobDocumentationItemImpl();
        return jobDocumentationItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public JobletDocumentationItem createJobletDocumentationItem() {
        JobletDocumentationItemImpl jobletDocumentationItem = new JobletDocumentationItemImpl();
        return jobletDocumentationItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public WSDLSchemaConnectionItem createWSDLSchemaConnectionItem() {
        WSDLSchemaConnectionItemImpl wsdlSchemaConnectionItem = new WSDLSchemaConnectionItemImpl();
        return wsdlSchemaConnectionItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public SchemaInformation createSchemaInformation() {
        SchemaInformationImpl schemaInformation = new SchemaInformationImpl();
        return schemaInformation;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Information createInformation() {
        InformationImpl information = new InformationImpl();
        return information;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public SQLPatternItem createSQLPatternItem() {
        SQLPatternItemImpl sqlPatternItem = new SQLPatternItemImpl();
        return sqlPatternItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ComponentSetting createComponentSetting() {
        ComponentSettingImpl componentSetting = new ComponentSettingImpl();
        return componentSetting;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public StatAndLogsSettings createStatAndLogsSettings() {
        StatAndLogsSettingsImpl statAndLogsSettings = new StatAndLogsSettingsImpl();
        return statAndLogsSettings;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ImplicitContextSettings createImplicitContextSettings() {
        ImplicitContextSettingsImpl implicitContextSettings = new ImplicitContextSettingsImpl();
        return implicitContextSettings;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public SoaOperation createSoaOperation() {
        SoaOperationImpl soaOperation = new SoaOperationImpl();
        return soaOperation;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public SoaInputParameter createSoaInputParameter() {
        SoaInputParameterImpl soaInputParameter = new SoaInputParameterImpl();
        return soaInputParameter;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public SoaService createSoaService() {
        SoaServiceImpl soaService = new SoaServiceImpl();
        return soaService;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public RulesItem createRulesItem() {
        RulesItemImpl rulesItem = new RulesItemImpl();
        return rulesItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public UserRight createUserRight() {
        UserRightImpl userRight = new UserRightImpl();
        return userRight;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public RoleRight createRoleRight() {
        RoleRightImpl roleRight = new RoleRightImpl();
        return roleRight;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public SVGBusinessProcessItem createSVGBusinessProcessItem() {
        SVGBusinessProcessItemImpl svgBusinessProcessItem = new SVGBusinessProcessItemImpl();
        return svgBusinessProcessItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public LinkRulesItem createLinkRulesItem() {
        LinkRulesItemImpl linkRulesItem = new LinkRulesItemImpl();
        return linkRulesItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ItemRelations createItemRelations() {
        ItemRelationsImpl itemRelations = new ItemRelationsImpl();
        return itemRelations;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ItemRelation createItemRelation() {
        ItemRelationImpl itemRelation = new ItemRelationImpl();
        return itemRelation;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Notification createNotification() {
        NotificationImpl notification = new NotificationImpl();
        return notification;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public HL7ConnectionItem createHL7ConnectionItem() {
        HL7ConnectionItemImpl hl7ConnectionItem = new HL7ConnectionItemImpl();
        return hl7ConnectionItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public UserRoleReference createUserRoleReference() {
        UserRoleReferenceImpl userRoleReference = new UserRoleReferenceImpl();
        return userRoleReference;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public HeaderFooterConnectionItem createHeaderFooterConnectionItem() {
        HeaderFooterConnectionItemImpl headerFooterConnectionItem = new HeaderFooterConnectionItemImpl();
        return headerFooterConnectionItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public FTPConnectionItem createFTPConnectionItem() {
        FTPConnectionItemImpl ftpConnectionItem = new FTPConnectionItemImpl();
        return ftpConnectionItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public CustomComponentSetting createCustomComponentSetting() {
        CustomComponentSettingImpl customComponentSetting = new CustomComponentSettingImpl();
        return customComponentSetting;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public TDQItem createTDQItem() {
        TDQItemImpl tdqItem = new TDQItemImpl();
        return tdqItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public BRMSConnectionItem createBRMSConnectionItem() {
        BRMSConnectionItemImpl brmsConnectionItem = new BRMSConnectionItemImpl();
        return brmsConnectionItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public JobScriptItem createJobScriptItem() {
        JobScriptItemImpl jobScriptItem = new JobScriptItemImpl();
        return jobScriptItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EDIFACTConnectionItem createEDIFACTConnectionItem() {
        EDIFACTConnectionItemImpl edifactConnectionItem = new EDIFACTConnectionItemImpl();
        return edifactConnectionItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public TechnicalVariable createTechnicalVariable() {
        TechnicalVariableImpl technicalVariable = new TechnicalVariableImpl();
        return technicalVariable;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ReferenceItem createReferenceItem() {
        ReferenceItemImpl referenceItem = new ReferenceItemImpl();
        return referenceItem;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExchangeUser createExchangeUser() {
        ExchangeUserImpl exchangeUser = new ExchangeUserImpl();
        return exchangeUser;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ArtifactNotification createArtifactNotification() {
        ArtifactNotificationImpl artifactNotification = new ArtifactNotificationImpl();
        return artifactNotification;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Map.Entry createAdditionalInfoMap() {
        AdditionalInfoMapImpl additionalInfoMap = new AdditionalInfoMapImpl();
        return additionalInfoMap;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MigrationTask createMigrationTask() {
        MigrationTaskImpl migrationTask = new MigrationTaskImpl();
        return migrationTask;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PigudfItem createPigudfItem() {
        PigudfItemImpl pigudfItem = new PigudfItemImpl();
        return pigudfItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ReferenceFileItem createReferenceFileItem() {
        ReferenceFileItemImpl referenceFileItem = new ReferenceFileItemImpl();
        return referenceFileItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public FolderType createFolderTypeFromString(EDataType eDataType, String initialValue) {
        FolderType result = FolderType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String convertFolderTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public UserProjectAuthorizationType createUserProjectAuthorizationTypeFromString(EDataType eDataType, String initialValue) {
        UserProjectAuthorizationType result = UserProjectAuthorizationType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String convertUserProjectAuthorizationTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public UserModuleAuthorizationType createUserModuleAuthorizationTypeFromString(EDataType eDataType, String initialValue) {
        UserModuleAuthorizationType result = UserModuleAuthorizationType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String convertUserModuleAuthorizationTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public InformationLevel createInformationLevelFromString(EDataType eDataType, String initialValue) {
        InformationLevel result = InformationLevel.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String convertInformationLevelToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MigrationStatus createMigrationStatusFromString(EDataType eDataType, String initialValue) {
        MigrationStatus result = MigrationStatus.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertMigrationStatusToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public PropertiesPackage getPropertiesPackage() {
        return (PropertiesPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    public static PropertiesPackage getPackage() {
        return PropertiesPackage.eINSTANCE;
    }

} // PropertiesFactoryImpl
