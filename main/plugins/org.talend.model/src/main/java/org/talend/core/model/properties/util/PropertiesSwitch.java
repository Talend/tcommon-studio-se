/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.properties.util;

import java.util.List;

import java.util.Map;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.talend.core.model.properties.*;
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
import org.talend.core.model.properties.EbcdicConnectionItem;
import org.talend.core.model.properties.ExcelFileConnectionItem;
import org.talend.core.model.properties.ExecutionPlan;
import org.talend.core.model.properties.ExecutionPlanPart;
import org.talend.core.model.properties.ExecutionPlanPartCmdPrm;
import org.talend.core.model.properties.ExecutionPlanPartJobPrm;
import org.talend.core.model.properties.ExecutionPlanPrm;
import org.talend.core.model.properties.ExecutionServer;
import org.talend.core.model.properties.ExecutionTask;
import org.talend.core.model.properties.ExecutionTaskCmdPrm;
import org.talend.core.model.properties.ExecutionTaskJobPrm;
import org.talend.core.model.properties.ExecutionTriggerable;
import org.talend.core.model.properties.ExecutionVirtualServer;
import org.talend.core.model.properties.FTPConnectionItem;
import org.talend.core.model.properties.FileItem;
import org.talend.core.model.properties.FileTrigger;
import org.talend.core.model.properties.FileTriggerMask;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.GenericSchemaConnectionItem;
import org.talend.core.model.properties.HL7ConnectionItem;
import org.talend.core.model.properties.HeaderFooterConnectionItem;
import org.talend.core.model.properties.ImplicitContextSettings;
import org.talend.core.model.properties.Information;
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
import org.talend.core.model.properties.NotationHolder;
import org.talend.core.model.properties.Notification;
import org.talend.core.model.properties.PlanExecutionHistory;
import org.talend.core.model.properties.PositionalFileConnectionItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.ProjectComponentAuthorisation;
import org.talend.core.model.properties.ProjectReference;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.Property;
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
import org.talend.core.model.properties.User;
import org.talend.core.model.properties.UserModuleAuthorization;
import org.talend.core.model.properties.UserProjectAuthorization;
import org.talend.core.model.properties.UserRight;
import org.talend.core.model.properties.UserRole;
import org.talend.core.model.properties.UserRoleReference;
import org.talend.core.model.properties.WSDLSchemaConnectionItem;
import org.talend.core.model.properties.XmlFileConnectionItem;

/**
 * <!-- begin-user-doc --> The <b>Switch</b> for the model's inheritance hierarchy. It supports the call
 * {@link #doSwitch(EObject) doSwitch(object)} to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object and proceeding up the inheritance hierarchy until a non-null result is
 * returned, which is the result of the switch. <!-- end-user-doc -->
 * @see org.talend.core.model.properties.PropertiesPackage
 * @generated
 */
public class PropertiesSwitch {

    /**
	 * The cached model package
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    protected static PropertiesPackage modelPackage;

    /**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public PropertiesSwitch() {
		if (modelPackage == null) {
			modelPackage = PropertiesPackage.eINSTANCE;
		}
	}

    /**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
    public Object doSwitch(EObject theEObject) {
		return doSwitch(theEObject.eClass(), theEObject);
	}

    /**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
    protected Object doSwitch(EClass theEClass, EObject theEObject) {
		if (theEClass.eContainer() == modelPackage) {
			return doSwitch(theEClass.getClassifierID(), theEObject);
		}
		else {
			List eSuperTypes = theEClass.getESuperTypes();
			return
				eSuperTypes.isEmpty() ?
					defaultCase(theEObject) :
					doSwitch((EClass)eSuperTypes.get(0), theEObject);
		}
	}

    /**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
    protected Object doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case PropertiesPackage.PROJECT: {
				Project project = (Project)theEObject;
				Object result = caseProject(project);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.PROJECT_COMPONENT_AUTHORISATION: {
				ProjectComponentAuthorisation projectComponentAuthorisation = (ProjectComponentAuthorisation)theEObject;
				Object result = caseProjectComponentAuthorisation(projectComponentAuthorisation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.PROJECT_REFERENCE: {
				ProjectReference projectReference = (ProjectReference)theEObject;
				Object result = caseProjectReference(projectReference);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.STATUS: {
				Status status = (Status)theEObject;
				Object result = caseStatus(status);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.ITEM_STATE: {
				ItemState itemState = (ItemState)theEObject;
				Object result = caseItemState(itemState);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.PROPERTY: {
				Property property = (Property)theEObject;
				Object result = caseProperty(property);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.ITEM: {
				Item item = (Item)theEObject;
				Object result = caseItem(item);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.LINK_DOCUMENTATION_ITEM: {
				LinkDocumentationItem linkDocumentationItem = (LinkDocumentationItem)theEObject;
				Object result = caseLinkDocumentationItem(linkDocumentationItem);
				if (result == null) result = caseItem(linkDocumentationItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.LINK_TYPE: {
				LinkType linkType = (LinkType)theEObject;
				Object result = caseLinkType(linkType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.BUSINESS_PROCESS_ITEM: {
				BusinessProcessItem businessProcessItem = (BusinessProcessItem)theEObject;
				Object result = caseBusinessProcessItem(businessProcessItem);
				if (result == null) result = caseItem(businessProcessItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.FILE_ITEM: {
				FileItem fileItem = (FileItem)theEObject;
				Object result = caseFileItem(fileItem);
				if (result == null) result = caseItem(fileItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.BYTE_ARRAY: {
				ByteArray byteArray = (ByteArray)theEObject;
				Object result = caseByteArray(byteArray);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.DOCUMENTATION_ITEM: {
				DocumentationItem documentationItem = (DocumentationItem)theEObject;
				Object result = caseDocumentationItem(documentationItem);
				if (result == null) result = caseFileItem(documentationItem);
				if (result == null) result = caseItem(documentationItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.ROUTINE_ITEM: {
				RoutineItem routineItem = (RoutineItem)theEObject;
				Object result = caseRoutineItem(routineItem);
				if (result == null) result = caseFileItem(routineItem);
				if (result == null) result = caseItem(routineItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.CONNECTION_ITEM: {
				ConnectionItem connectionItem = (ConnectionItem)theEObject;
				Object result = caseConnectionItem(connectionItem);
				if (result == null) result = caseItem(connectionItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.SNIPPET_VARIABLE: {
				SnippetVariable snippetVariable = (SnippetVariable)theEObject;
				Object result = caseSnippetVariable(snippetVariable);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.SNIPPET_ITEM: {
				SnippetItem snippetItem = (SnippetItem)theEObject;
				Object result = caseSnippetItem(snippetItem);
				if (result == null) result = caseItem(snippetItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.DELIMITED_FILE_CONNECTION_ITEM: {
				DelimitedFileConnectionItem delimitedFileConnectionItem = (DelimitedFileConnectionItem)theEObject;
				Object result = caseDelimitedFileConnectionItem(delimitedFileConnectionItem);
				if (result == null) result = caseConnectionItem(delimitedFileConnectionItem);
				if (result == null) result = caseItem(delimitedFileConnectionItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.POSITIONAL_FILE_CONNECTION_ITEM: {
				PositionalFileConnectionItem positionalFileConnectionItem = (PositionalFileConnectionItem)theEObject;
				Object result = casePositionalFileConnectionItem(positionalFileConnectionItem);
				if (result == null) result = caseConnectionItem(positionalFileConnectionItem);
				if (result == null) result = caseItem(positionalFileConnectionItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.REG_EX_FILE_CONNECTION_ITEM: {
				RegExFileConnectionItem regExFileConnectionItem = (RegExFileConnectionItem)theEObject;
				Object result = caseRegExFileConnectionItem(regExFileConnectionItem);
				if (result == null) result = caseConnectionItem(regExFileConnectionItem);
				if (result == null) result = caseItem(regExFileConnectionItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.CSV_FILE_CONNECTION_ITEM: {
				CSVFileConnectionItem csvFileConnectionItem = (CSVFileConnectionItem)theEObject;
				Object result = caseCSVFileConnectionItem(csvFileConnectionItem);
				if (result == null) result = caseDelimitedFileConnectionItem(csvFileConnectionItem);
				if (result == null) result = caseConnectionItem(csvFileConnectionItem);
				if (result == null) result = caseItem(csvFileConnectionItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.DATABASE_CONNECTION_ITEM: {
				DatabaseConnectionItem databaseConnectionItem = (DatabaseConnectionItem)theEObject;
				Object result = caseDatabaseConnectionItem(databaseConnectionItem);
				if (result == null) result = caseConnectionItem(databaseConnectionItem);
				if (result == null) result = caseItem(databaseConnectionItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.SAP_CONNECTION_ITEM: {
				SAPConnectionItem sapConnectionItem = (SAPConnectionItem)theEObject;
				Object result = caseSAPConnectionItem(sapConnectionItem);
				if (result == null) result = caseConnectionItem(sapConnectionItem);
				if (result == null) result = caseItem(sapConnectionItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.XML_FILE_CONNECTION_ITEM: {
				XmlFileConnectionItem xmlFileConnectionItem = (XmlFileConnectionItem)theEObject;
				Object result = caseXmlFileConnectionItem(xmlFileConnectionItem);
				if (result == null) result = caseConnectionItem(xmlFileConnectionItem);
				if (result == null) result = caseItem(xmlFileConnectionItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.LDIF_FILE_CONNECTION_ITEM: {
				LdifFileConnectionItem ldifFileConnectionItem = (LdifFileConnectionItem)theEObject;
				Object result = caseLdifFileConnectionItem(ldifFileConnectionItem);
				if (result == null) result = caseConnectionItem(ldifFileConnectionItem);
				if (result == null) result = caseItem(ldifFileConnectionItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.EXCEL_FILE_CONNECTION_ITEM: {
				ExcelFileConnectionItem excelFileConnectionItem = (ExcelFileConnectionItem)theEObject;
				Object result = caseExcelFileConnectionItem(excelFileConnectionItem);
				if (result == null) result = caseConnectionItem(excelFileConnectionItem);
				if (result == null) result = caseItem(excelFileConnectionItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.EBCDIC_CONNECTION_ITEM: {
				EbcdicConnectionItem ebcdicConnectionItem = (EbcdicConnectionItem)theEObject;
				Object result = caseEbcdicConnectionItem(ebcdicConnectionItem);
				if (result == null) result = caseConnectionItem(ebcdicConnectionItem);
				if (result == null) result = caseItem(ebcdicConnectionItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.MDM_CONNECTION_ITEM: {
				MDMConnectionItem mdmConnectionItem = (MDMConnectionItem)theEObject;
				Object result = caseMDMConnectionItem(mdmConnectionItem);
				if (result == null) result = caseConnectionItem(mdmConnectionItem);
				if (result == null) result = caseItem(mdmConnectionItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.VALIDATION_RULES_CONNECTION_ITEM: {
				ValidationRulesConnectionItem validationRulesConnectionItem = (ValidationRulesConnectionItem)theEObject;
				Object result = caseValidationRulesConnectionItem(validationRulesConnectionItem);
				if (result == null) result = caseConnectionItem(validationRulesConnectionItem);
				if (result == null) result = caseItem(validationRulesConnectionItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.PROCESS_ITEM: {
				ProcessItem processItem = (ProcessItem)theEObject;
				Object result = caseProcessItem(processItem);
				if (result == null) result = caseItem(processItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.USER_ROLE: {
				UserRole userRole = (UserRole)theEObject;
				Object result = caseUserRole(userRole);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.USER: {
				User user = (User)theEObject;
				Object result = caseUser(user);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.FOLDER_ITEM: {
				FolderItem folderItem = (FolderItem)theEObject;
				Object result = caseFolderItem(folderItem);
				if (result == null) result = caseItem(folderItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.COMPONENT: {
				Component component = (Component)theEObject;
				Object result = caseComponent(component);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.NOTATION_HOLDER: {
				NotationHolder notationHolder = (NotationHolder)theEObject;
				Object result = caseNotationHolder(notationHolder);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.USER_PROJECT_AUTHORIZATION: {
				UserProjectAuthorization userProjectAuthorization = (UserProjectAuthorization)theEObject;
				Object result = caseUserProjectAuthorization(userProjectAuthorization);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.CONTEXT_ITEM: {
				ContextItem contextItem = (ContextItem)theEObject;
				Object result = caseContextItem(contextItem);
				if (result == null) result = caseItem(contextItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.SPAGO_BI_SERVER: {
				SpagoBiServer spagoBiServer = (SpagoBiServer)theEObject;
				Object result = caseSpagoBiServer(spagoBiServer);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.LICENSE: {
				License license = (License)theEObject;
				Object result = caseLicense(license);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.GENERIC_SCHEMA_CONNECTION_ITEM: {
				GenericSchemaConnectionItem genericSchemaConnectionItem = (GenericSchemaConnectionItem)theEObject;
				Object result = caseGenericSchemaConnectionItem(genericSchemaConnectionItem);
				if (result == null) result = caseConnectionItem(genericSchemaConnectionItem);
				if (result == null) result = caseItem(genericSchemaConnectionItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.USER_MODULE_AUTHORIZATION: {
				UserModuleAuthorization userModuleAuthorization = (UserModuleAuthorization)theEObject;
				Object result = caseUserModuleAuthorization(userModuleAuthorization);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.LDAP_SCHEMA_CONNECTION_ITEM: {
				LDAPSchemaConnectionItem ldapSchemaConnectionItem = (LDAPSchemaConnectionItem)theEObject;
				Object result = caseLDAPSchemaConnectionItem(ldapSchemaConnectionItem);
				if (result == null) result = caseConnectionItem(ldapSchemaConnectionItem);
				if (result == null) result = caseItem(ldapSchemaConnectionItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.SALESFORCE_SCHEMA_CONNECTION_ITEM: {
				SalesforceSchemaConnectionItem salesforceSchemaConnectionItem = (SalesforceSchemaConnectionItem)theEObject;
				Object result = caseSalesforceSchemaConnectionItem(salesforceSchemaConnectionItem);
				if (result == null) result = caseConnectionItem(salesforceSchemaConnectionItem);
				if (result == null) result = caseItem(salesforceSchemaConnectionItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.DASHBOARD_CONNECTION: {
				DashboardConnection dashboardConnection = (DashboardConnection)theEObject;
				Object result = caseDashboardConnection(dashboardConnection);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.EXECUTION_TRIGGERABLE: {
				ExecutionTriggerable executionTriggerable = (ExecutionTriggerable)theEObject;
				Object result = caseExecutionTriggerable(executionTriggerable);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.EXECUTION_PLAN: {
				ExecutionPlan executionPlan = (ExecutionPlan)theEObject;
				Object result = caseExecutionPlan(executionPlan);
				if (result == null) result = caseExecutionTriggerable(executionPlan);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.EXECUTION_PLAN_PART: {
				ExecutionPlanPart executionPlanPart = (ExecutionPlanPart)theEObject;
				Object result = caseExecutionPlanPart(executionPlanPart);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.EXECUTION_PLAN_PRM: {
				ExecutionPlanPrm executionPlanPrm = (ExecutionPlanPrm)theEObject;
				Object result = caseExecutionPlanPrm(executionPlanPrm);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.EXECUTION_PLAN_PART_CMD_PRM: {
				ExecutionPlanPartCmdPrm executionPlanPartCmdPrm = (ExecutionPlanPartCmdPrm)theEObject;
				Object result = caseExecutionPlanPartCmdPrm(executionPlanPartCmdPrm);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.EXECUTION_PLAN_PART_JOB_PRM: {
				ExecutionPlanPartJobPrm executionPlanPartJobPrm = (ExecutionPlanPartJobPrm)theEObject;
				Object result = caseExecutionPlanPartJobPrm(executionPlanPartJobPrm);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.EXECUTION_TASK: {
				ExecutionTask executionTask = (ExecutionTask)theEObject;
				Object result = caseExecutionTask(executionTask);
				if (result == null) result = caseExecutionTriggerable(executionTask);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.EXECUTION_TASK_PROPERTIES: {
				ExecutionTaskProperties executionTaskProperties = (ExecutionTaskProperties)theEObject;
				Object result = caseExecutionTaskProperties(executionTaskProperties);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.EXECUTION_TASK_CMD_PRM: {
				ExecutionTaskCmdPrm executionTaskCmdPrm = (ExecutionTaskCmdPrm)theEObject;
				Object result = caseExecutionTaskCmdPrm(executionTaskCmdPrm);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.EXECUTION_TASK_JOB_PRM: {
				ExecutionTaskJobPrm executionTaskJobPrm = (ExecutionTaskJobPrm)theEObject;
				Object result = caseExecutionTaskJobPrm(executionTaskJobPrm);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.TASK_EXECUTION_HISTORY: {
				TaskExecutionHistory taskExecutionHistory = (TaskExecutionHistory)theEObject;
				Object result = caseTaskExecutionHistory(taskExecutionHistory);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.PLAN_EXECUTION_HISTORY: {
				PlanExecutionHistory planExecutionHistory = (PlanExecutionHistory)theEObject;
				Object result = casePlanExecutionHistory(planExecutionHistory);
				if (result == null) result = caseTaskExecutionHistory(planExecutionHistory);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.TALEND_TRIGGER: {
				TalendTrigger talendTrigger = (TalendTrigger)theEObject;
				Object result = caseTalendTrigger(talendTrigger);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.CRON_TALEND_TRIGGER: {
				CronTalendTrigger cronTalendTrigger = (CronTalendTrigger)theEObject;
				Object result = caseCronTalendTrigger(cronTalendTrigger);
				if (result == null) result = caseTalendTrigger(cronTalendTrigger);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.CRON_UI_TALEND_TRIGGER: {
				CronUITalendTrigger cronUITalendTrigger = (CronUITalendTrigger)theEObject;
				Object result = caseCronUITalendTrigger(cronUITalendTrigger);
				if (result == null) result = caseTalendTrigger(cronUITalendTrigger);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.SIMPLE_TALEND_TRIGGER: {
				SimpleTalendTrigger simpleTalendTrigger = (SimpleTalendTrigger)theEObject;
				Object result = caseSimpleTalendTrigger(simpleTalendTrigger);
				if (result == null) result = caseTalendTrigger(simpleTalendTrigger);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.FILE_TRIGGER: {
				FileTrigger fileTrigger = (FileTrigger)theEObject;
				Object result = caseFileTrigger(fileTrigger);
				if (result == null) result = caseSimpleTalendTrigger(fileTrigger);
				if (result == null) result = caseTalendTrigger(fileTrigger);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.FILE_TRIGGER_MASK: {
				FileTriggerMask fileTriggerMask = (FileTriggerMask)theEObject;
				Object result = caseFileTriggerMask(fileTriggerMask);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.EXECUTION_SERVER: {
				ExecutionServer executionServer = (ExecutionServer)theEObject;
				Object result = caseExecutionServer(executionServer);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.EXECUTION_VIRTUAL_SERVER: {
				ExecutionVirtualServer executionVirtualServer = (ExecutionVirtualServer)theEObject;
				Object result = caseExecutionVirtualServer(executionVirtualServer);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.JOBLET_PROCESS_ITEM: {
				JobletProcessItem jobletProcessItem = (JobletProcessItem)theEObject;
				Object result = caseJobletProcessItem(jobletProcessItem);
				if (result == null) result = caseItem(jobletProcessItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.JOB_DOCUMENTATION_ITEM: {
				JobDocumentationItem jobDocumentationItem = (JobDocumentationItem)theEObject;
				Object result = caseJobDocumentationItem(jobDocumentationItem);
				if (result == null) result = caseFileItem(jobDocumentationItem);
				if (result == null) result = caseItem(jobDocumentationItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.JOBLET_DOCUMENTATION_ITEM: {
				JobletDocumentationItem jobletDocumentationItem = (JobletDocumentationItem)theEObject;
				Object result = caseJobletDocumentationItem(jobletDocumentationItem);
				if (result == null) result = caseFileItem(jobletDocumentationItem);
				if (result == null) result = caseItem(jobletDocumentationItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.WSDL_SCHEMA_CONNECTION_ITEM: {
				WSDLSchemaConnectionItem wsdlSchemaConnectionItem = (WSDLSchemaConnectionItem)theEObject;
				Object result = caseWSDLSchemaConnectionItem(wsdlSchemaConnectionItem);
				if (result == null) result = caseConnectionItem(wsdlSchemaConnectionItem);
				if (result == null) result = caseItem(wsdlSchemaConnectionItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.SCHEMA_INFORMATION: {
				SchemaInformation schemaInformation = (SchemaInformation)theEObject;
				Object result = caseSchemaInformation(schemaInformation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.INFORMATION: {
				Information information = (Information)theEObject;
				Object result = caseInformation(information);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.SQL_PATTERN_ITEM: {
				SQLPatternItem sqlPatternItem = (SQLPatternItem)theEObject;
				Object result = caseSQLPatternItem(sqlPatternItem);
				if (result == null) result = caseFileItem(sqlPatternItem);
				if (result == null) result = caseItem(sqlPatternItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.COMPONENT_SETTING: {
				ComponentSetting componentSetting = (ComponentSetting)theEObject;
				Object result = caseComponentSetting(componentSetting);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.STAT_AND_LOGS_SETTINGS: {
				StatAndLogsSettings statAndLogsSettings = (StatAndLogsSettings)theEObject;
				Object result = caseStatAndLogsSettings(statAndLogsSettings);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.IMPLICIT_CONTEXT_SETTINGS: {
				ImplicitContextSettings implicitContextSettings = (ImplicitContextSettings)theEObject;
				Object result = caseImplicitContextSettings(implicitContextSettings);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.SOA_OPERATION: {
				SoaOperation soaOperation = (SoaOperation)theEObject;
				Object result = caseSoaOperation(soaOperation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.SOA_INPUT_PARAMETER: {
				SoaInputParameter soaInputParameter = (SoaInputParameter)theEObject;
				Object result = caseSoaInputParameter(soaInputParameter);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.SOA_SERVICE: {
				SoaService soaService = (SoaService)theEObject;
				Object result = caseSoaService(soaService);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.RULES_ITEM: {
				RulesItem rulesItem = (RulesItem)theEObject;
				Object result = caseRulesItem(rulesItem);
				if (result == null) result = caseFileItem(rulesItem);
				if (result == null) result = caseItem(rulesItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.USER_RIGHT: {
				UserRight userRight = (UserRight)theEObject;
				Object result = caseUserRight(userRight);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.ROLE_RIGHT: {
				RoleRight roleRight = (RoleRight)theEObject;
				Object result = caseRoleRight(roleRight);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.SVG_BUSINESS_PROCESS_ITEM: {
				SVGBusinessProcessItem svgBusinessProcessItem = (SVGBusinessProcessItem)theEObject;
				Object result = caseSVGBusinessProcessItem(svgBusinessProcessItem);
				if (result == null) result = caseFileItem(svgBusinessProcessItem);
				if (result == null) result = caseItem(svgBusinessProcessItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.LINK_RULES_ITEM: {
				LinkRulesItem linkRulesItem = (LinkRulesItem)theEObject;
				Object result = caseLinkRulesItem(linkRulesItem);
				if (result == null) result = caseItem(linkRulesItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.ITEM_RELATIONS: {
				ItemRelations itemRelations = (ItemRelations)theEObject;
				Object result = caseItemRelations(itemRelations);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.ITEM_RELATION: {
				ItemRelation itemRelation = (ItemRelation)theEObject;
				Object result = caseItemRelation(itemRelation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.NOTIFICATION: {
				Notification notification = (Notification)theEObject;
				Object result = caseNotification(notification);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.HL7_CONNECTION_ITEM: {
				HL7ConnectionItem hl7ConnectionItem = (HL7ConnectionItem)theEObject;
				Object result = caseHL7ConnectionItem(hl7ConnectionItem);
				if (result == null) result = caseConnectionItem(hl7ConnectionItem);
				if (result == null) result = caseItem(hl7ConnectionItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.USER_ROLE_REFERENCE: {
				UserRoleReference userRoleReference = (UserRoleReference)theEObject;
				Object result = caseUserRoleReference(userRoleReference);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.HEADER_FOOTER_CONNECTION_ITEM: {
				HeaderFooterConnectionItem headerFooterConnectionItem = (HeaderFooterConnectionItem)theEObject;
				Object result = caseHeaderFooterConnectionItem(headerFooterConnectionItem);
				if (result == null) result = caseConnectionItem(headerFooterConnectionItem);
				if (result == null) result = caseItem(headerFooterConnectionItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.FTP_CONNECTION_ITEM: {
				FTPConnectionItem ftpConnectionItem = (FTPConnectionItem)theEObject;
				Object result = caseFTPConnectionItem(ftpConnectionItem);
				if (result == null) result = caseConnectionItem(ftpConnectionItem);
				if (result == null) result = caseItem(ftpConnectionItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.CUSTOM_COMPONENT_SETTING: {
				CustomComponentSetting customComponentSetting = (CustomComponentSetting)theEObject;
				Object result = caseCustomComponentSetting(customComponentSetting);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.TDQ_ITEM: {
				TDQItem tdqItem = (TDQItem)theEObject;
				Object result = caseTDQItem(tdqItem);
				if (result == null) result = caseItem(tdqItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.BRMS_CONNECTION_ITEM: {
				BRMSConnectionItem brmsConnectionItem = (BRMSConnectionItem)theEObject;
				Object result = caseBRMSConnectionItem(brmsConnectionItem);
				if (result == null) result = caseConnectionItem(brmsConnectionItem);
				if (result == null) result = caseItem(brmsConnectionItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.JOB_SCRIPT_ITEM: {
				JobScriptItem jobScriptItem = (JobScriptItem)theEObject;
				Object result = caseJobScriptItem(jobScriptItem);
				if (result == null) result = caseFileItem(jobScriptItem);
				if (result == null) result = caseItem(jobScriptItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.EDIFACT_CONNECTION_ITEM: {
				EDIFACTConnectionItem edifactConnectionItem = (EDIFACTConnectionItem)theEObject;
				Object result = caseEDIFACTConnectionItem(edifactConnectionItem);
				if (result == null) result = caseConnectionItem(edifactConnectionItem);
				if (result == null) result = caseItem(edifactConnectionItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.TECHNICAL_VARIABLE: {
				TechnicalVariable technicalVariable = (TechnicalVariable)theEObject;
				Object result = caseTechnicalVariable(technicalVariable);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.REFERENCE_FILE_ITEM: {
				ReferenceFileItem referenceFileItem = (ReferenceFileItem)theEObject;
				Object result = caseReferenceFileItem(referenceFileItem);
				if (result == null) result = caseReferenceItem(referenceFileItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.REFERENCE_ITEM: {
				ReferenceItem referenceItem = (ReferenceItem)theEObject;
				Object result = caseReferenceItem(referenceItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.EXCHANGE_USER: {
				ExchangeUser exchangeUser = (ExchangeUser)theEObject;
				Object result = caseExchangeUser(exchangeUser);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.ARTIFACT_NOTIFICATION: {
				ArtifactNotification artifactNotification = (ArtifactNotification)theEObject;
				Object result = caseArtifactNotification(artifactNotification);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.ADDITIONAL_INFO_MAP: {
				Map.Entry additionalInfoMap = (Map.Entry)theEObject;
				Object result = caseAdditionalInfoMap(additionalInfoMap);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.MIGRATION_TASK: {
				MigrationTask migrationTask = (MigrationTask)theEObject;
				Object result = caseMigrationTask(migrationTask);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PropertiesPackage.PIGUDF_ITEM: {
				PigudfItem pigudfItem = (PigudfItem)theEObject;
				Object result = casePigudfItem(pigudfItem);
				if (result == null) result = caseRoutineItem(pigudfItem);
				if (result == null) result = caseFileItem(pigudfItem);
				if (result == null) result = caseItem(pigudfItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Project</em>'.
	 * <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Project</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseProject(Project object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Project Component Authorisation</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Project Component Authorisation</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseProjectComponentAuthorisation(ProjectComponentAuthorisation object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Project Reference</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Project Reference</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseProjectReference(ProjectReference object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Status</em>'.
	 * <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Status</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseStatus(Status object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Item State</em>'.
	 * <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Item State</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseItemState(ItemState object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Property</em>'.
	 * <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Property</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseProperty(Property object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Item</em>'.
	 * <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Item</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseItem(Item object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Link Documentation Item</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Link Documentation Item</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseLinkDocumentationItem(LinkDocumentationItem object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Link Type</em>'.
	 * <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Link Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseLinkType(LinkType object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Business Process Item</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Business Process Item</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseBusinessProcessItem(BusinessProcessItem object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>File Item</em>'.
	 * <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>File Item</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseFileItem(FileItem object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Byte Array</em>'.
	 * <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Byte Array</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseByteArray(ByteArray object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Documentation Item</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Documentation Item</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDocumentationItem(DocumentationItem object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Routine Item</em>'.
	 * <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Routine Item</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseRoutineItem(RoutineItem object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Connection Item</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Connection Item</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseConnectionItem(ConnectionItem object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Snippet Variable</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Snippet Variable</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseSnippetVariable(SnippetVariable object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Snippet Item</em>'.
	 * <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Snippet Item</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseSnippetItem(SnippetItem object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Delimited File Connection Item</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Delimited File Connection Item</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDelimitedFileConnectionItem(DelimitedFileConnectionItem object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Positional File Connection Item</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Positional File Connection Item</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object casePositionalFileConnectionItem(PositionalFileConnectionItem object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Reg Ex File Connection Item</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Reg Ex File Connection Item</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseRegExFileConnectionItem(RegExFileConnectionItem object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>CSV File Connection Item</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>CSV File Connection Item</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseCSVFileConnectionItem(CSVFileConnectionItem object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Database Connection Item</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Database Connection Item</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDatabaseConnectionItem(DatabaseConnectionItem object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>SAP Connection Item</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>SAP Connection Item</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseSAPConnectionItem(SAPConnectionItem object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Xml File Connection Item</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Xml File Connection Item</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseXmlFileConnectionItem(XmlFileConnectionItem object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Ldif File Connection Item</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Ldif File Connection Item</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseLdifFileConnectionItem(LdifFileConnectionItem object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Excel File Connection Item</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Excel File Connection Item</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseExcelFileConnectionItem(ExcelFileConnectionItem object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Ebcdic Connection Item</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Ebcdic Connection Item</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseEbcdicConnectionItem(EbcdicConnectionItem object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>MDM Connection Item</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>MDM Connection Item</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseMDMConnectionItem(MDMConnectionItem object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Validation Rules Connection Item</em>'.
	 * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Validation Rules Connection Item</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseValidationRulesConnectionItem(ValidationRulesConnectionItem object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Process Item</em>'.
	 * <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Process Item</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseProcessItem(ProcessItem object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>User Role</em>'.
	 * <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>User Role</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseUserRole(UserRole object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>User</em>'.
	 * <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>User</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseUser(User object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Folder Item</em>'.
	 * <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Folder Item</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseFolderItem(FolderItem object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Component</em>'.
	 * <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Component</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseComponent(Component object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Notation Holder</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Notation Holder</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseNotationHolder(NotationHolder object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>User Project Authorization</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>User Project Authorization</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseUserProjectAuthorization(UserProjectAuthorization object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Context Item</em>'.
	 * <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Context Item</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseContextItem(ContextItem object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Spago Bi Server</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Spago Bi Server</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseSpagoBiServer(SpagoBiServer object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>License</em>'.
	 * <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>License</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseLicense(License object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Generic Schema Connection Item</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Generic Schema Connection Item</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseGenericSchemaConnectionItem(GenericSchemaConnectionItem object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>User Module Authorization</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>User Module Authorization</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseUserModuleAuthorization(UserModuleAuthorization object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>LDAP Schema Connection Item</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>LDAP Schema Connection Item</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseLDAPSchemaConnectionItem(LDAPSchemaConnectionItem object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Salesforce Schema Connection Item</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Salesforce Schema Connection Item</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseSalesforceSchemaConnectionItem(SalesforceSchemaConnectionItem object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Dashboard Connection</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Dashboard Connection</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDashboardConnection(DashboardConnection object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Execution Triggerable</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Execution Triggerable</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseExecutionTriggerable(ExecutionTriggerable object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Execution Server</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Execution Server</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseExecutionServer(ExecutionServer object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Execution Task</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Execution Task</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseExecutionTask(ExecutionTask object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Execution Task Properties</em>'.
	 * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Execution Task Properties</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseExecutionTaskProperties(ExecutionTaskProperties object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Execution Task Cmd Prm</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Execution Task Cmd Prm</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseExecutionTaskCmdPrm(ExecutionTaskCmdPrm object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Execution Task Job Prm</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Execution Task Job Prm</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseExecutionTaskJobPrm(ExecutionTaskJobPrm object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Talend Trigger</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Talend Trigger</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseTalendTrigger(TalendTrigger object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Cron Talend Trigger</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Cron Talend Trigger</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseCronTalendTrigger(CronTalendTrigger object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Cron UI Talend Trigger</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Cron UI Talend Trigger</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseCronUITalendTrigger(CronUITalendTrigger object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Simple Talend Trigger</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Simple Talend Trigger</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseSimpleTalendTrigger(SimpleTalendTrigger object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Execution Virtual Server</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Execution Virtual Server</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseExecutionVirtualServer(ExecutionVirtualServer object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>File Trigger</em>'.
	 * <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>File Trigger</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseFileTrigger(FileTrigger object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>File Trigger Mask</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>File Trigger Mask</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseFileTriggerMask(FileTriggerMask object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Joblet Process Item</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Joblet Process Item</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseJobletProcessItem(JobletProcessItem object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Job Documentation Item</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Job Documentation Item</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseJobDocumentationItem(JobDocumentationItem object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Joblet Documentation Item</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Joblet Documentation Item</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseJobletDocumentationItem(JobletDocumentationItem object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>WSDL Schema Connection Item</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>WSDL Schema Connection Item</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseWSDLSchemaConnectionItem(WSDLSchemaConnectionItem object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Schema Information</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Schema Information</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseSchemaInformation(SchemaInformation object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Information</em>'.
	 * <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Information</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseInformation(Information object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>SQL Pattern Item</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>SQL Pattern Item</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseSQLPatternItem(SQLPatternItem object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Component Setting</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Component Setting</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseComponentSetting(ComponentSetting object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Stat And Logs Settings</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Stat And Logs Settings</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseStatAndLogsSettings(StatAndLogsSettings object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Implicit Context Settings</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Implicit Context Settings</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseImplicitContextSettings(ImplicitContextSettings object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Soa Operation</em>'.
	 * <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Soa Operation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseSoaOperation(SoaOperation object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Soa Input Parameter</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Soa Input Parameter</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseSoaInputParameter(SoaInputParameter object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Soa Service</em>'.
	 * <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Soa Service</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseSoaService(SoaService object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Rules Item</em>'.
	 * <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Rules Item</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseRulesItem(RulesItem object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>User Right</em>'.
	 * <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>User Right</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseUserRight(UserRight object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Role Right</em>'.
	 * <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Role Right</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseRoleRight(RoleRight object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>SVG Business Process Item</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>SVG Business Process Item</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseSVGBusinessProcessItem(SVGBusinessProcessItem object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Link Rules Item</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Link Rules Item</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseLinkRulesItem(LinkRulesItem object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Item Relations</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Item Relations</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseItemRelations(ItemRelations object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Item Relation</em>'.
	 * <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Item Relation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseItemRelation(ItemRelation object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Notification</em>'.
	 * <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Notification</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseNotification(Notification object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>HL7 Connection Item</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>HL7 Connection Item</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseHL7ConnectionItem(HL7ConnectionItem object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>User Role Reference</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>User Role Reference</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseUserRoleReference(UserRoleReference object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Header Footer Connection Item</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Header Footer Connection Item</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseHeaderFooterConnectionItem(HeaderFooterConnectionItem object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>FTP Connection Item</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>FTP Connection Item</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseFTPConnectionItem(FTPConnectionItem object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Custom Component Setting</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Custom Component Setting</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseCustomComponentSetting(CustomComponentSetting object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>TDQ Item</em>'.
	 * <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>TDQ Item</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseTDQItem(TDQItem object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>BRMS Connection Item</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>BRMS Connection Item</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseBRMSConnectionItem(BRMSConnectionItem object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Job Script Item</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Job Script Item</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseJobScriptItem(JobScriptItem object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>EDIFACT Connection Item</em>'.
	 * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EDIFACT Connection Item</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseEDIFACTConnectionItem(EDIFACTConnectionItem object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Technical Variable</em>'.
	 * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Technical Variable</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseTechnicalVariable(TechnicalVariable object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Reference Item</em>'.
	 * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Reference Item</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseReferenceItem(ReferenceItem object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Exchange User</em>'.
	 * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Exchange User</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseExchangeUser(ExchangeUser object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Artifact Notification</em>'.
	 * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Artifact Notification</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseArtifactNotification(ArtifactNotification object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Additional Info Map</em>'.
	 * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Additional Info Map</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseAdditionalInfoMap(Map.Entry object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Migration Task</em>'.
	 * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Migration Task</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseMigrationTask(MigrationTask object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Pigudf Item</em>'.
	 * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Pigudf Item</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object casePigudfItem(PigudfItem object) {
		return null;
	}

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Reference File Item</em>'.
	 * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Reference File Item</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public Object caseReferenceFileItem(ReferenceFileItem object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Execution Plan</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Execution Plan</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseExecutionPlan(ExecutionPlan object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Execution Plan Part</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Execution Plan Part</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseExecutionPlanPart(ExecutionPlanPart object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Execution Plan Prm</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Execution Plan Prm</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseExecutionPlanPrm(ExecutionPlanPrm object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Execution Plan Part Cmd Prm</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Execution Plan Part Cmd Prm</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseExecutionPlanPartCmdPrm(ExecutionPlanPartCmdPrm object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Execution Plan Part Job Prm</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Execution Plan Part Job Prm</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseExecutionPlanPartJobPrm(ExecutionPlanPartJobPrm object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Task Execution History</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Task Execution History</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseTaskExecutionHistory(TaskExecutionHistory object) {
		return null;
	}

    /**
     * Returns the result of interpreting the object as an instance of '<em>Plan Execution History</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Plan Execution History</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object casePlanExecutionHistory(PlanExecutionHistory object) {
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
    public Object defaultCase(EObject object) {
		return null;
	}

} // PropertiesSwitch
