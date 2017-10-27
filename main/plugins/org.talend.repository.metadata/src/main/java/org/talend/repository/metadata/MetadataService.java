// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.metadata;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.IProviderService;
import org.talend.core.PluginChecker;
import org.talend.core.model.metadata.designerproperties.ComponentToRepositoryProperty;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.LinkRulesItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RulesItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryContentHandler;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryContentManager;
import org.talend.core.repository.seeker.RepositorySeekerManager;
import org.talend.core.ui.IHeaderFooterProviderService;
import org.talend.core.ui.IMDMProviderService;
import org.talend.core.ui.metadata.celleditor.EProcessTypeForRule;
import org.talend.core.ui.rule.AbstractRlueOperationChoice;
import org.talend.metadata.managment.ui.wizard.RepositoryWizard;
import org.talend.repository.metadata.ui.actions.metadata.AbstractCreateTableAction;
import org.talend.repository.metadata.ui.actions.metadata.CreateTableAction;
import org.talend.repository.metadata.ui.dialog.RuleOperationChoiceDialog;
import org.talend.repository.model.IMetadataService;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.ui.wizards.metadata.connection.database.DatabaseWizard;
import org.talend.repository.ui.wizards.metadata.connection.files.delimited.DelimitedFileWizard;
import org.talend.repository.ui.wizards.metadata.connection.files.excel.ExcelFileWizard;
import org.talend.repository.ui.wizards.metadata.connection.files.ldif.LdifFileWizard;
import org.talend.repository.ui.wizards.metadata.connection.files.positional.FilePositionalWizard;
import org.talend.repository.ui.wizards.metadata.connection.files.regexp.RegexpFileWizard;
import org.talend.repository.ui.wizards.metadata.connection.files.salesforce.SalesforceSchemaWizard;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.XmlFileWizard;
import org.talend.repository.ui.wizards.metadata.connection.genericshema.GenericSchemaWizard;
import org.talend.repository.ui.wizards.metadata.connection.ldap.LDAPSchemaWizard;
import org.talend.repository.ui.wizards.metadata.connection.wsdl.WSDLSchemaWizard;

import net.sourceforge.jtds.jdbc.JtdsConnection;

/**
 * DOC hwang class global comment. Detailled comment
 */
public class MetadataService implements IMetadataService {

    private static Logger log = Logger.getLogger(MetadataService.class);

    private GenericSchemaWizard genericSchemaWizard = null;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#getGenericSchemaWizardDialog(org.eclipse.swt.widgets.Shell,
     * org.eclipse.ui.IWorkbench, boolean, org.eclipse.jface.viewers.ISelection, java.lang.String[], boolean)
     */
    @Override
    public WizardDialog getGenericSchemaWizardDialog(Shell shell, IWorkbench workbench, boolean creation, ISelection selection,
            String[] existingNames, boolean isSinglePageOnly) {

        genericSchemaWizard = new GenericSchemaWizard(workbench, creation, selection, existingNames, isSinglePageOnly);
        return new WizardDialog(shell, genericSchemaWizard);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#getPropertyFromWizardDialog()
     */
    @Override
    public Property getPropertyFromWizardDialog() {
        if (this.genericSchemaWizard != null) {
            return this.genericSchemaWizard.getConnectionProperty();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#getPathForSaveAsGenericSchema()
     */
    @Override
    public IPath getPathForSaveAsGenericSchema() {
        if (this.genericSchemaWizard != null) {
            return this.genericSchemaWizard.getPathForSaveAsGenericSchema();
        }
        return null;
    }

    @Override
    public void openMetadataConnection(IRepositoryViewObject o, INode node) {
        if (o != null && o.getProperty() != null) {
            IRepositoryNode realNode = RepositorySeekerManager.getInstance().searchRepoViewNode(o.getProperty().getId());
            openMetadataConnection(false, realNode, node);
        }
    }

    @Override
    public ConnectionItem openMetadataConnection(boolean creation, IRepositoryNode repoNode, INode node) {
        RepositoryNode realNode;
        if (repoNode instanceof RepositoryNode) {
            realNode = (RepositoryNode) repoNode;
            IWizard relatedWizard = null;
            ERepositoryObjectType objectType = null;
            if (creation) {
                objectType = realNode.getContentType();
            } else {
                objectType = realNode.getObjectType();
            }
            if (objectType.equals(ERepositoryObjectType.METADATA_CONNECTIONS)) {
                relatedWizard = new DatabaseWizard(PlatformUI.getWorkbench(), creation, realNode, null);
            } else if (objectType.equals(ERepositoryObjectType.METADATA_FILE_DELIMITED)) {
                relatedWizard = new DelimitedFileWizard(PlatformUI.getWorkbench(), creation, realNode, null);
            } else if (objectType.equals(ERepositoryObjectType.METADATA_FILE_LDIF)) {
                relatedWizard = new LdifFileWizard(PlatformUI.getWorkbench(), creation, realNode, null);
            } else if (objectType.equals(ERepositoryObjectType.METADATA_FILE_POSITIONAL)) {
                relatedWizard = new FilePositionalWizard(PlatformUI.getWorkbench(), creation, realNode, null);
            } else if (objectType.equals(ERepositoryObjectType.METADATA_FILE_REGEXP)) {
                relatedWizard = new RegexpFileWizard(PlatformUI.getWorkbench(), creation, realNode, null);
            } else if (objectType.equals(ERepositoryObjectType.METADATA_FILE_XML)) {
                relatedWizard = new XmlFileWizard(PlatformUI.getWorkbench(), creation, realNode, null);
            } else if (objectType.equals(ERepositoryObjectType.METADATA_GENERIC_SCHEMA)) {
                relatedWizard = new GenericSchemaWizard(PlatformUI.getWorkbench(), creation, realNode, null, true);
            } else if (objectType.equals(ERepositoryObjectType.METADATA_WSDL_SCHEMA)) {
                relatedWizard = new WSDLSchemaWizard(PlatformUI.getWorkbench(), creation, realNode, null, false);
            } else if (objectType.equals(ERepositoryObjectType.METADATA_LDAP_SCHEMA)) {
                relatedWizard = new LDAPSchemaWizard(PlatformUI.getWorkbench(), creation, realNode, null, false);
            } else if (objectType.equals(ERepositoryObjectType.METADATA_FILE_EXCEL)) {
                relatedWizard = new ExcelFileWizard(PlatformUI.getWorkbench(), creation, realNode, null);
            } else if (objectType.equals(ERepositoryObjectType.METADATA_SALESFORCE_SCHEMA)) {
                relatedWizard = new SalesforceSchemaWizard(PlatformUI.getWorkbench(), creation, realNode, null, false);
            } else if (objectType.equals(ERepositoryObjectType.METADATA_FILE_EBCDIC)) {
                if (PluginChecker.isEBCDICPluginLoaded()) {
                    IProviderService iebcdicService = GlobalServiceRegister.getDefault().findService("IEBCDICProviderService");
                    if (iebcdicService != null) {
                        relatedWizard = iebcdicService.newWizard(PlatformUI.getWorkbench(), creation, realNode, null);
                    }
                }
            } else if (objectType.equals(ERepositoryObjectType.METADATA_FILE_HL7)) {
                if (PluginChecker.isHL7PluginLoaded()) {
                    IProviderService service = GlobalServiceRegister.getDefault().findService("IHL7ProviderService");
                    if (service != null) {
                        relatedWizard = service.newWizard(PlatformUI.getWorkbench(), creation, realNode, null);
                    }
                }
            } else if (objectType.equals(ERepositoryObjectType.METADATA_MDMCONNECTION)) {
                if (PluginChecker.isMDMPluginLoaded()) {
                    IMDMProviderService service = (IMDMProviderService) GlobalServiceRegister.getDefault().getService(
                            IMDMProviderService.class);
                    if (service != null) {
                        relatedWizard = service.newWizard(PlatformUI.getWorkbench(), creation, realNode, null);
                    }
                }
            } else if (objectType.equals(ERepositoryObjectType.METADATA_SAPCONNECTIONS)) {
                if (PluginChecker.isSAPWizardPluginLoaded()) {
                    IProviderService service = GlobalServiceRegister.getDefault().findService("ISAPProviderService");
                    if (service != null) {
                        relatedWizard = service.newWizard(PlatformUI.getWorkbench(), creation, realNode, null);
                    }
                }
            } else if (objectType.equals(ERepositoryObjectType.METADATA_HEADER_FOOTER)) {
                if (GlobalServiceRegister.getDefault().isServiceRegistered(IHeaderFooterProviderService.class)) {
                    IHeaderFooterProviderService service = (IHeaderFooterProviderService) GlobalServiceRegister.getDefault()
                            .getService(IHeaderFooterProviderService.class);
                    if (service != null) {
                        relatedWizard = service.newWizard(PlatformUI.getWorkbench(), creation, realNode, null);
                    }
                }
            } else if (objectType.equals(ERepositoryObjectType.METADATA_FILE_FTP)) {
                if (PluginChecker.isFTPPluginLoaded()) {
                    IProviderService service = GlobalServiceRegister.getDefault().findService(
                            "org.talend.core.ui.IFTPProviderService");
                    if (service != null) {
                        relatedWizard = service.newWizard(PlatformUI.getWorkbench(), creation, realNode, null);
                    }
                }
            } else if (objectType.equals(ERepositoryObjectType.METADATA_FILE_BRMS)) {
                if (PluginChecker.isBRMSPluginLoaded()) {
                    IProviderService service = GlobalServiceRegister.getDefault().findService("IBRMSProviderService");
                    if (service != null) {
                        relatedWizard = service.newWizard(PlatformUI.getWorkbench(), creation, realNode, null);
                    }
                }
            }

            // Handle the extention node wizards.
            if (relatedWizard == null) {
                for (IRepositoryContentHandler handler : RepositoryContentManager.getHandlers()) {
                    if (handler.isRepObjType(objectType)) {
                        relatedWizard = handler.newWizard(PlatformUI.getWorkbench(), creation, realNode, null);
                        if (relatedWizard != null) {
                            break;
                        }
                    }
                }
            }

            boolean changed = false;
            if (relatedWizard != null) {
                ConnectionItem connItem = null;
                if (node != null && relatedWizard instanceof RepositoryWizard) {// creation && node != null
                    ((RepositoryWizard) relatedWizard).setNode(node);
                    connItem = ((RepositoryWizard) relatedWizard).getConnectionItem();
                    if (connItem != null) {
                        changed = ComponentToRepositoryProperty.setValue(connItem, node);
                    }
                }
                if (connItem != null && changed) {
                    // Open the Wizard
                    WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), relatedWizard);
                    wizardDialog.setPageSize(600, 540);
                    wizardDialog.create();
                    if (wizardDialog.open() == wizardDialog.OK) {
                        return connItem;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void openEditSchemaWizard(String value) {
        final RepositoryNode realNode = RepositoryNodeUtilities.getMetadataTableFromConnection(value);
        if (realNode != null) {
            AbstractCreateTableAction action = new CreateTableAction() {

                /*
                 * (non-Javadoc)
                 * 
                 * @see org.talend.repository.ui.actions.AContextualAction#getSelection()
                 */
                @Override
                public ISelection getSelection() {
                    return new StructuredSelection(realNode);
                }
            };
            action.run();
        }
    }

    @Override
    public void runCreateTableAction(RepositoryNode metadataNode) {
        AbstractCreateTableAction action = new CreateTableAction(metadataNode);
        action.setAvoidUnloadResources(true);
        action.run();
    }

    @Override
    public AbstractRlueOperationChoice getOperationChoice(Shell shell, INode node, RulesItem[] repositoryRuleItems,
            LinkRulesItem[] linkRuleItems, EProcessTypeForRule rule, String ruleToEdit, boolean readOnly) {
        return new RuleOperationChoiceDialog(shell, node, repositoryRuleItems, linkRuleItems, rule, ruleToEdit, readOnly);
    }

    @Override
    public DatabaseMetaData findCustomizedJTDSDBMetadata(Connection jtdsConn) {
        if (jtdsConn instanceof JtdsConnection) {
            return new JtdsMetadataAdapter((JtdsConnection) jtdsConn);
        } else {
            try {
                return jtdsConn.getMetaData();
            } catch (SQLException e) {
                log.error(e);
                return null;
            }
        }

    }

}
