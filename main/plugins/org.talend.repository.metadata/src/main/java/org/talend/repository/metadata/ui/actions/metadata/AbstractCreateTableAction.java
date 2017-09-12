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
package org.talend.repository.metadata.ui.actions.metadata;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.UIJob;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.IRepositoryContextService;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.database.conn.ConnParameterKeys;
import org.talend.core.database.conn.DatabaseConnStrUtil;
import org.talend.core.database.conn.template.EDatabaseConnTemplate;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.FileExcelConnection;
import org.talend.core.model.metadata.builder.connection.GenericPackage;
import org.talend.core.model.metadata.builder.connection.GenericSchemaConnection;
import org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection;
import org.talend.core.model.metadata.builder.connection.LdifFileConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.PositionalFileConnection;
import org.talend.core.model.metadata.builder.connection.RegexpFileConnection;
import org.talend.core.model.metadata.builder.connection.SalesforceModuleUnit;
import org.talend.core.model.metadata.builder.connection.SalesforceSchemaConnection;
import org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.metadata.builder.database.JavaSqlFactory;
import org.talend.core.model.metadata.connection.hive.HiveModeInfo;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.DelimitedFileConnectionItem;
import org.talend.core.model.properties.ExcelFileConnectionItem;
import org.talend.core.model.properties.GenericSchemaConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.LDAPSchemaConnectionItem;
import org.talend.core.model.properties.LdifFileConnectionItem;
import org.talend.core.model.properties.PositionalFileConnectionItem;
import org.talend.core.model.properties.RegExFileConnectionItem;
import org.talend.core.model.properties.SAPConnectionItem;
import org.talend.core.model.properties.SalesforceSchemaConnectionItem;
import org.talend.core.model.properties.WSDLSchemaConnectionItem;
import org.talend.core.model.properties.XmlFileConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryContentHandler;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryContentManager;
import org.talend.core.repository.model.ProjectRepositoryNode;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.ui.actions.metadata.AbstractCreateAction;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.service.ISAPProviderService;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.PackageHelper;
import org.talend.cwm.helper.TableHelper;
import org.talend.metadata.managment.connection.manager.HiveConnectionManager;
import org.talend.metadata.managment.repository.ManagerConnection;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;
import org.talend.metadata.managment.ui.utils.SwitchContextGroupNameImpl;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.views.IRepositoryView;
import org.talend.repository.ui.wizards.metadata.connection.files.salesforce.SalesforceModulesWizard;
import org.talend.repository.ui.wizards.metadata.connection.files.salesforce.SalesforceSchemaTableWizard;
import org.talend.repository.ui.wizards.metadata.connection.files.salesforce.SalesforceSchemasWizard;
import org.talend.repository.ui.wizards.metadata.connection.genericshema.GenericSchemaTableWizard;
import org.talend.repository.ui.wizards.metadata.connection.ldap.LDAPSchemaTableWizard;
import org.talend.repository.ui.wizards.metadata.connection.wsdl.WSDLSchemaTableWizard;
import org.talend.repository.ui.wizards.metadata.table.database.DatabaseTableWizard;
import org.talend.repository.ui.wizards.metadata.table.files.FileDelimitedTableWizard;
import org.talend.repository.ui.wizards.metadata.table.files.FileExcelTableWizard;
import org.talend.repository.ui.wizards.metadata.table.files.FileLdifTableWizard;
import org.talend.repository.ui.wizards.metadata.table.files.FilePositionalTableWizard;
import org.talend.repository.ui.wizards.metadata.table.files.FileRegexpTableWizard;
import org.talend.repository.ui.wizards.metadata.table.files.FileXmlTableWizard;

import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.record.RecordFactory;
import orgomg.cwm.resource.record.RecordFile;

/**
 * DOC smallet class global comment. Detailed comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class AbstractCreateTableAction extends AbstractCreateAction {

    protected static final int WIZARD_WIDTH = 900;

    protected static final int WIZARD_HEIGHT = 495;

    private static Logger log = Logger.getLogger(AbstractCreateTableAction.class);

    /**
     * DOC mhelleboid Comment method "handleWizard".
     * 
     * @param node
     * @param wizardDialog
     */
    protected void handleWizard(RepositoryNode node, WizardDialog wizardDialog) {
        wizardDialog.setPageSize(WIZARD_WIDTH, WIZARD_HEIGHT);
        wizardDialog.create();
        int result = wizardDialog.open();
        IRepositoryView viewPart = getViewPart();
        if (viewPart != null) {
            if (WizardDialog.CANCEL == result) {
                RepositoryNode rootNode = ProjectRepositoryNode.getInstance().getRootRepositoryNode(node, false);
                if (rootNode != null) {
                    rootNode.getChildren().clear();
                    rootNode.setInitialized(false);
                    viewPart.refresh(rootNode);
                }
            }
            viewPart.expand(node, true);
        }
        ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
        if (nodeType.isSubItem()) { // edit table
            RepositoryNode parent = node.getParent();
            if (parent.getObject() == null) { // db
                parent = parent.getParent();
            }
        }
    }

    /**
     * DOC mhelleboid Comment method "handleWizard".
     * 
     * @param node
     * @param wizardDialog
     */
    protected void handleWizard(RepositoryNode node, WizardDialog wizardDialog, boolean notSetSize) {
        if (!notSetSize) {
            wizardDialog.setPageSize(WIZARD_WIDTH, WIZARD_HEIGHT);
        }
        wizardDialog.create();
        if (wizardDialog.open() == wizardDialog.OK) {
            IRepositoryView viewPart = getViewPart();
            if (viewPart != null) {
                viewPart.expand(node, true);
            }
            refresh(node);
        } else {
            IRepositoryView viewPart = getViewPart();
            if (viewPart != null) {
                RepositoryNode rootNode = ProjectRepositoryNode.getInstance().getRootRepositoryNode(node, false);
                if (rootNode != null) {
                    rootNode.getChildren().clear();
                    rootNode.setInitialized(false);
                    viewPart.refresh(rootNode);
                }
            }
        }
    }

    protected String getStringIndexed(String string) {
        int indiceIndex = string.length();

        for (int f = 0; f <= string.length() - 1; f++) {
            try {
                String s = string.substring(f, string.length());
                if (String.valueOf(Integer.parseInt(s)).equals(s)) {
                    indiceIndex = f;
                    f = string.length();
                }
            } catch (Exception e) {
                // by default : indiceIndex = input.length()
            }
        }

        // validate the value is unique and indice then if needed
        while (!isUniqLabel(string)) {
            try {
                String indiceString = string.substring(indiceIndex, string.length());
                string = string.substring(0, indiceIndex) + (Integer.parseInt(indiceString) + 1);
            } catch (Exception e) {
                string = string + "1"; //$NON-NLS-1$
            }
        }
        return string;
    }

    /**
     * DOC ocarbone Comment method "isUniqLabel".
     * 
     * @param label
     * @return boolean
     */
    private boolean isUniqLabel(String label) {
        // Find the existings Metadata Name of Node
        String[] existingLabel = getExistingNames();
        if (existingLabel == null) {
            return true;
        } else {
            for (int i = 0; i < existingLabel.length; i++) {
                if (label.equals(existingLabel[i])) {
                    i = existingLabel.length;
                    return false;
                }
            }
        }
        return true;
    }

    protected void initContextMode(ConnectionItem item) {
        ConnectionContextHelper.checkContextMode(item);
    }

    /**
     * DOC ocarbone Comment method "createFilePositionalTableWizard".
     * 
     * @param selection
     * @return
     */
    @SuppressWarnings("unchecked")
    protected void createFilePositionalTableWizard(RepositoryNode node, boolean forceReadOnly) {

        PositionalFileConnection connection = null;
        MetadataTable metadataTable = null;

        boolean creation = false;
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            String tableLabel = (String) node.getProperties(EProperties.LABEL);

            PositionalFileConnectionItem item = null;

            if (nodeType == ERepositoryObjectType.METADATA_CON_TABLE) {
                if (node.getParent().isBin() && node.getParent().getObject() == null) {
                    item = (PositionalFileConnectionItem) node.getObject().getProperty().getItem();
                } else {
                    item = (PositionalFileConnectionItem) node.getParent().getObject().getProperty().getItem();
                }
                connection = (PositionalFileConnection) item.getConnection();
                metadataTable = TableHelper.findByLabel(connection, tableLabel);
                creation = false;
            } else if (nodeType == ERepositoryObjectType.METADATA_FILE_POSITIONAL) {
                item = (PositionalFileConnectionItem) node.getObject().getProperty().getItem();
                connection = (PositionalFileConnection) item.getConnection();
                metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
                // PTODO OCA : replace getStringIndexed by IndiceHelper.getIndexedLabel(metadataTable.getLabel(),
                // existingNames)
                // PTODO OCA : use IndiceHelper on multiple tableRefect
                metadataTable.setLabel(getStringIndexed(metadataTable.getLabel()));
                String nextId = ProxyRepositoryFactory.getInstance().getNextId();
                metadataTable.setId(nextId);
                RecordFile record = (RecordFile) ConnectionHelper.getPackage(connection.getName(), connection, RecordFile.class);
                if (record != null) { // hywang
                    PackageHelper.addMetadataTable(metadataTable, record);
                } else {
                    RecordFile newrecord = RecordFactory.eINSTANCE.createRecordFile();
                    ConnectionHelper.addPackage(newrecord, connection);
                    PackageHelper.addMetadataTable(metadataTable, newrecord);
                }
                creation = true;
            } else {
                return;
            }

            initContextMode(item);
            FilePositionalTableWizard filePositionalTableWizard = new FilePositionalTableWizard(PlatformUI.getWorkbench(),
                    creation, item, metadataTable, forceReadOnly);
            filePositionalTableWizard.setRepositoryObject(node.getObject());

            WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), filePositionalTableWizard);
            handleWizard(node, wizardDialog);
        }
    }

    /**
     * DOC ocarbone Comment method "createFileRegexpTableWizard".
     * 
     * @param selection
     * @return
     */
    @SuppressWarnings("unchecked")
    protected void createFileRegexpTableWizard(RepositoryNode node, boolean forceReadOnly) {
        RegexpFileConnection connection = null;
        MetadataTable metadataTable = null;

        boolean creation = false;
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            String tableLabel = (String) node.getProperties(EProperties.LABEL);

            RegExFileConnectionItem item = null;

            if (nodeType == ERepositoryObjectType.METADATA_CON_TABLE) {
                if (node.getParent().isBin() && node.getParent().getObject() == null) {
                    item = (RegExFileConnectionItem) node.getObject().getProperty().getItem();
                } else {
                    item = (RegExFileConnectionItem) node.getParent().getObject().getProperty().getItem();
                }
                connection = (RegexpFileConnection) item.getConnection();
                metadataTable = TableHelper.findByLabel(connection, tableLabel);
                creation = false;
            } else if (nodeType == ERepositoryObjectType.METADATA_FILE_REGEXP) {
                item = (RegExFileConnectionItem) node.getObject().getProperty().getItem();
                connection = (RegexpFileConnection) item.getConnection();
                metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
                String nextId = ProxyRepositoryFactory.getInstance().getNextId();
                metadataTable.setId(nextId);
                metadataTable.setLabel(getStringIndexed(metadataTable.getLabel()));
                RecordFile record = (RecordFile) ConnectionHelper.getPackage(connection.getName(), connection, RecordFile.class);
                if (record != null) { // hywang
                    PackageHelper.addMetadataTable(metadataTable, record);
                } else {
                    RecordFile newrecord = RecordFactory.eINSTANCE.createRecordFile();
                    ConnectionHelper.addPackage(newrecord, connection);
                    PackageHelper.addMetadataTable(metadataTable, newrecord);
                }
                creation = true;
            } else {
                return;
            }
            initContextMode(item);
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            FileRegexpTableWizard fileRegexpTableWizard = new FileRegexpTableWizard(PlatformUI.getWorkbench(), creation, item,
                    metadataTable, forceReadOnly);
            fileRegexpTableWizard.setRepositoryObject(node.getObject());

            WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), fileRegexpTableWizard);
            handleWizard(node, wizardDialog);
        }
    }

    /**
     * DOC cantoine Comment method "createFileXmlTableWizard".
     * 
     * @param selection
     * @return
     */
    @SuppressWarnings("unchecked")
    protected void createFileXmlTableWizard(RepositoryNode node, boolean forceReadOnly) {
        XmlFileConnection connection = null;
        MetadataTable metadataTable = null;

        boolean creation = false;
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            String tableLabel = (String) node.getProperties(EProperties.LABEL);

            XmlFileConnectionItem item = null;

            if (nodeType == ERepositoryObjectType.METADATA_CON_TABLE) {
                if (node.getParent().isBin() && node.getParent().getObject() == null) {
                    item = (XmlFileConnectionItem) node.getObject().getProperty().getItem();
                } else {
                    item = (XmlFileConnectionItem) node.getParent().getObject().getProperty().getItem();
                }
                connection = (XmlFileConnection) item.getConnection();
                metadataTable = TableHelper.findByLabel(connection, tableLabel);
                creation = false;
            } else if (nodeType == ERepositoryObjectType.METADATA_FILE_XML) {
                item = (XmlFileConnectionItem) node.getObject().getProperty().getItem();
                connection = (XmlFileConnection) item.getConnection();
                metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
                String nextId = ProxyRepositoryFactory.getInstance().getNextId();
                metadataTable.setId(nextId);
                metadataTable.setLabel(getStringIndexed(metadataTable.getLabel()));
                RecordFile record = (RecordFile) ConnectionHelper.getPackage(connection.getName(), connection, RecordFile.class);
                if (record != null) { // hywang
                    PackageHelper.addMetadataTable(metadataTable, record);
                } else {
                    RecordFile newrecord = RecordFactory.eINSTANCE.createRecordFile();
                    ConnectionHelper.addPackage(newrecord, connection);
                    PackageHelper.addMetadataTable(metadataTable, newrecord);
                }
                creation = true;
            } else {
                return;
            }
            initContextMode(item);
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            FileXmlTableWizard fileXmlTableWizard = new FileXmlTableWizard(PlatformUI.getWorkbench(), creation, item,
                    metadataTable, forceReadOnly);
            fileXmlTableWizard.setRepositoryObject(node.getObject());

            WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), fileXmlTableWizard);
            handleWizard(node, wizardDialog);
        }
    }

    /**
     * DOC ocarbone Comment method "createFileDelimitedTableWizard".
     * 
     * @param selection
     * @return
     */
    @SuppressWarnings("unchecked")
    protected void createFileDelimitedTableWizard(RepositoryNode node, boolean forceReadOnly) {
        DelimitedFileConnection connection = null;
        MetadataTable metadataTable = null;

        boolean creation = false;
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            String tableLabel = (String) node.getProperties(EProperties.LABEL);

            DelimitedFileConnectionItem item = null;
            if (nodeType == ERepositoryObjectType.METADATA_CON_TABLE) {
                if (node.getParent().isBin() && node.getParent().getObject() == null) {
                    item = (DelimitedFileConnectionItem) node.getObject().getProperty().getItem();
                } else {
                    item = (DelimitedFileConnectionItem) node.getParent().getObject().getProperty().getItem();
                }
                connection = (DelimitedFileConnection) item.getConnection();
                metadataTable = TableHelper.findByLabel(connection, tableLabel);
                creation = false;
            } else if (nodeType == ERepositoryObjectType.METADATA_FILE_DELIMITED) {
                item = (DelimitedFileConnectionItem) node.getObject().getProperty().getItem();
                connection = (DelimitedFileConnection) item.getConnection();
                metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
                String nextId = ProxyRepositoryFactory.getInstance().getNextId();
                metadataTable.setId(nextId);
                metadataTable.setLabel(getStringIndexed(metadataTable.getLabel()));
                RecordFile record = (RecordFile) ConnectionHelper.getPackage(connection.getName(), connection, RecordFile.class);
                if (record != null) { // hywang
                    PackageHelper.addMetadataTable(metadataTable, record);
                } else {
                    RecordFile newrecord = RecordFactory.eINSTANCE.createRecordFile();
                    ConnectionHelper.addPackage(newrecord, connection);
                    PackageHelper.addMetadataTable(metadataTable, newrecord);
                }
                creation = true;
            } else {
                return;
            }
            initContextMode(item);
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            FileDelimitedTableWizard fileDelimitedTableWizard = new FileDelimitedTableWizard(PlatformUI.getWorkbench(), creation,
                    item, metadataTable, forceReadOnly);
            fileDelimitedTableWizard.setRepositoryObject(node.getObject());

            WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), fileDelimitedTableWizard);
            handleWizard(node, wizardDialog);
        }
    }

    /**
     * DOC cantoine Comment method "createFileLdifTableWizard".
     * 
     * @param selection
     * @return
     */
    @SuppressWarnings("unchecked")
    protected void createFileLdifTableWizard(RepositoryNode node, boolean forceReadOnly) {
        LdifFileConnection connection = null;
        MetadataTable metadataTable = null;

        boolean creation = false;
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            String tableLabel = (String) node.getProperties(EProperties.LABEL);

            LdifFileConnectionItem item = null;
            if (nodeType == ERepositoryObjectType.METADATA_CON_TABLE) {
                if (node.getParent().isBin() && node.getParent().getObject() == null) {
                    item = (LdifFileConnectionItem) node.getObject().getProperty().getItem();
                } else {
                    item = (LdifFileConnectionItem) node.getParent().getObject().getProperty().getItem();
                }
                connection = (LdifFileConnection) item.getConnection();
                metadataTable = TableHelper.findByLabel(connection, tableLabel);
                creation = false;
            } else if (nodeType == ERepositoryObjectType.METADATA_FILE_LDIF) {
                item = (LdifFileConnectionItem) node.getObject().getProperty().getItem();
                connection = (LdifFileConnection) item.getConnection();
                metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
                String nextId = ProxyRepositoryFactory.getInstance().getNextId();
                metadataTable.setId(nextId);
                metadataTable.setLabel(getStringIndexed(metadataTable.getLabel()));
                RecordFile record = (RecordFile) ConnectionHelper.getPackage(connection.getName(), connection, RecordFile.class);
                if (record != null) { // hywang
                    PackageHelper.addMetadataTable(metadataTable, record);
                } else {
                    RecordFile newrecord = RecordFactory.eINSTANCE.createRecordFile();
                    ConnectionHelper.addPackage(newrecord, connection);
                    PackageHelper.addMetadataTable(metadataTable, newrecord);
                }
                creation = true;
            } else {
                return;
            }
            initContextMode(item);
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            FileLdifTableWizard fileLdifTableWizard = new FileLdifTableWizard(PlatformUI.getWorkbench(), creation, item,
                    metadataTable, forceReadOnly);
            fileLdifTableWizard.setRepositoryObject(node.getObject());

            WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), fileLdifTableWizard);
            handleWizard(node, wizardDialog);
        }
    }

    /**
     * 
     * DOC yexiaowei Comment method "createFileExcelTableWizard".
     * 
     * @param selection
     * @param forceReadOnly
     */
    @SuppressWarnings("unchecked")
    protected void createFileExcelTableWizard(RepositoryNode node, boolean forceReadOnly) {
        FileExcelConnection connection = null;
        MetadataTable metadataTable = null;

        boolean creation = false;
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            String tableLabel = (String) node.getProperties(EProperties.LABEL);

            ExcelFileConnectionItem item = null;
            if (nodeType == ERepositoryObjectType.METADATA_CON_TABLE) {
                if (node.getParent().isBin() && node.getParent().getObject() == null) {
                    item = (ExcelFileConnectionItem) node.getObject().getProperty().getItem();
                } else {
                    item = (ExcelFileConnectionItem) node.getParent().getObject().getProperty().getItem();
                }
                connection = (FileExcelConnection) item.getConnection();
                metadataTable = TableHelper.findByLabel(connection, tableLabel);
                creation = false;
            } else if (nodeType == ERepositoryObjectType.METADATA_FILE_EXCEL) {
                item = (ExcelFileConnectionItem) node.getObject().getProperty().getItem();
                connection = (FileExcelConnection) item.getConnection();
                metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
                String nextId = ProxyRepositoryFactory.getInstance().getNextId();
                metadataTable.setId(nextId);
                metadataTable.setLabel(getStringIndexed(metadataTable.getLabel()));
                RecordFile record = (RecordFile) ConnectionHelper.getPackage(connection.getName(), connection, RecordFile.class);
                if (record != null) { // hywang
                    PackageHelper.addMetadataTable(metadataTable, record);
                } else {
                    RecordFile newrecord = RecordFactory.eINSTANCE.createRecordFile();
                    ConnectionHelper.addPackage(newrecord, connection);
                    PackageHelper.addMetadataTable(metadataTable, newrecord);
                }
                creation = true;
            } else {
                return;
            }
            initContextMode(item);
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            FileExcelTableWizard fileExcelTableWizard = new FileExcelTableWizard(PlatformUI.getWorkbench(), creation, item,
                    metadataTable, forceReadOnly);
            fileExcelTableWizard.setRepositoryObject(node.getObject());

            WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), fileExcelTableWizard);
            handleWizard(node, wizardDialog);
        }
    }

    protected void createGenericSchemaWizard(RepositoryNode node, final boolean forceReadOnly) {
        GenericSchemaConnection connection = null;
        MetadataTable metadataTable = null;

        boolean creation = false;
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            String tableLabel = (String) node.getProperties(EProperties.LABEL);

            GenericSchemaConnectionItem item = null;

            if (nodeType == ERepositoryObjectType.METADATA_CON_TABLE) {
                if (node.getParent().isBin() && node.getParent().getObject() == null) {
                    item = (GenericSchemaConnectionItem) node.getObject().getProperty().getItem();
                } else {
                    item = (GenericSchemaConnectionItem) node.getParent().getObject().getProperty().getItem();
                }
                connection = (GenericSchemaConnection) item.getConnection();
                metadataTable = TableHelper.findByLabel(connection, tableLabel);
                creation = false;
            } else if (nodeType == ERepositoryObjectType.METADATA_GENERIC_SCHEMA) {
                item = (GenericSchemaConnectionItem) node.getObject().getProperty().getItem();
                connection = (GenericSchemaConnection) item.getConnection();
                metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
                String nextId = ProxyRepositoryFactory.getInstance().getNextId();
                metadataTable.setId(nextId);
                metadataTable.setLabel(getStringIndexed(metadataTable.getLabel()));
                GenericPackage g = (GenericPackage) ConnectionHelper.getPackage(connection.getName(), connection,
                        GenericPackage.class);
                if (g != null) { // hywang
                    g.getOwnedElement().add(metadataTable);
                } else {
                    GenericPackage gpkg = ConnectionFactory.eINSTANCE.createGenericPackage();
                    PackageHelper.addMetadataTable(metadataTable, gpkg);
                    ConnectionHelper.addPackage(gpkg, connection);
                }
                creation = true;
            } else {
                return;
            }
            initContextMode(item);
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            GenericSchemaTableWizard genericSchemaWizard = new GenericSchemaTableWizard(PlatformUI.getWorkbench(), creation,
                    item, metadataTable, forceReadOnly);
            genericSchemaWizard.setRepositoryObject(node.getObject());

            WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), genericSchemaWizard);
            handleWizard(node, wizardDialog);
        }
    }

    /**
     * DOC Administrator Comment method "createLDAPSchemaWizard".
     * 
     * @param selection
     * @param b
     */
    public void createLDAPSchemaWizard(RepositoryNode node, final boolean forceReadOnly) {
        LDAPSchemaConnection connection = null;
        MetadataTable metadataTable = null;

        boolean creation = false;
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            String tableLabel = (String) node.getProperties(EProperties.LABEL);

            LDAPSchemaConnectionItem item = null;

            if (nodeType == ERepositoryObjectType.METADATA_CON_TABLE) {
                if (node.getParent().isBin() && node.getParent().getObject() == null) {
                    item = (LDAPSchemaConnectionItem) node.getObject().getProperty().getItem();
                } else {
                    item = (LDAPSchemaConnectionItem) node.getParent().getObject().getProperty().getItem();
                }
                connection = (LDAPSchemaConnection) item.getConnection();
                metadataTable = TableHelper.findByLabel(connection, tableLabel);
                creation = false;
            } else if (nodeType == ERepositoryObjectType.METADATA_LDAP_SCHEMA) {
                item = (LDAPSchemaConnectionItem) node.getObject().getProperty().getItem();
                connection = (LDAPSchemaConnection) item.getConnection();
                metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
                String nextId = ProxyRepositoryFactory.getInstance().getNextId();
                metadataTable.setId(nextId);
                metadataTable.setLabel(getStringIndexed(metadataTable.getLabel()));
                GenericPackage g = (GenericPackage) ConnectionHelper.getPackage(connection.getName(), connection,
                        GenericPackage.class);
                if (g != null) { // hywang
                    g.getOwnedElement().add(metadataTable);
                } else {
                    GenericPackage gpkg = ConnectionFactory.eINSTANCE.createGenericPackage();
                    PackageHelper.addMetadataTable(metadataTable, gpkg);
                    ConnectionHelper.addPackage(gpkg, connection);

                }
                creation = true;
            } else {
                return;
            }
            initContextMode(item);
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            LDAPSchemaTableWizard ldapSchemaWizard = new LDAPSchemaTableWizard(PlatformUI.getWorkbench(), creation, item,
                    metadataTable, forceReadOnly);
            ldapSchemaWizard.setRepositoryObject(node.getObject());

            WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), ldapSchemaWizard);
            handleWizard(node, wizardDialog);
        }

    }

    /**
     * 
     * DOC YeXiaowei Comment method "createSalesforceSchemaWizard".
     * 
     * @param selection
     * @param forceReadOnly
     */
    public void createSalesforceSchemaWizard(RepositoryNode node, final boolean forceReadOnly) {
        SalesforceSchemaConnection connection = null;
        MetadataTable metadataTable = null;

        boolean creation = false;
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            String tableLabel = (String) node.getProperties(EProperties.LABEL);

            SalesforceSchemaConnectionItem item = null;
            if (nodeType == ERepositoryObjectType.METADATA_CON_TABLE) {
                if (node.getParent().isBin() && node.getParent().getObject() == null) {
                    item = (SalesforceSchemaConnectionItem) node.getObject().getProperty().getItem();
                } else {
                    item = (SalesforceSchemaConnectionItem) node.getParent().getObject().getProperty().getItem();
                }
                connection = (SalesforceSchemaConnection) item.getConnection();
                metadataTable = TableHelper.findByLabel(connection, tableLabel);
                creation = false;
            } else if (nodeType == ERepositoryObjectType.METADATA_SALESFORCE_SCHEMA) {
                item = (SalesforceSchemaConnectionItem) node.getObject().getProperty().getItem();
                connection = (SalesforceSchemaConnection) item.getConnection();
                metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
                String nextId = ProxyRepositoryFactory.getInstance().getNextId();
                metadataTable.setId(nextId);
                metadataTable.setLabel(getStringIndexed(metadataTable.getLabel()));
                GenericPackage g = (GenericPackage) ConnectionHelper.getPackage(connection.getName(), connection,
                        GenericPackage.class);
                if (g != null) { // hywang
                    g.getOwnedElement().add(metadataTable);
                } else {
                    GenericPackage gpkg = ConnectionFactory.eINSTANCE.createGenericPackage();
                    PackageHelper.addMetadataTable(metadataTable, gpkg);
                    ConnectionHelper.addPackage(gpkg, connection);

                }
                creation = true;
            } else {
                return;
            }
            initContextMode(item);

            if (metadataTable.eContainer() instanceof SalesforceModuleUnit) {
                SalesforceModuleUnit unit = (SalesforceModuleUnit) metadataTable.eContainer();
                connection.setModuleName(unit.getModuleName());
            }
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            SalesforceSchemaTableWizard salesforceSchemaWizard = new SalesforceSchemaTableWizard(PlatformUI.getWorkbench(),
                    creation, item, metadataTable, forceReadOnly);
            salesforceSchemaWizard.setRepositoryObject(node.getObject());

            WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), salesforceSchemaWizard);
            handleWizard(node, wizardDialog);
        }
    }

    /**
     * 
     * DOC YeXiaowei Comment method "createSalesforceSchemaWizard".
     * 
     * @param selection
     * @param forceReadOnly
     */
    public void createSalesforceSchemasWizard(RepositoryNode node, final boolean forceReadOnly) {
        SalesforceSchemaConnection connection = null;
        MetadataTable metadataTable = null;
        boolean creation = false;
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            String tableLabel = (String) node.getProperties(EProperties.LABEL);

            SalesforceSchemaConnectionItem item = null;
            // if (nodeType == ERepositoryObjectType.METADATA_CON_TABLE) {
            //
            // item = (SalesforceSchemaConnectionItem) node.getParent().getObject().getProperty().getItem();
            // connection = (SalesforceSchemaConnection) item.getConnection();
            // metadataTable = TableHelper.findByLabel(connection, tableLabel);
            // creation = false;
            // } else if (nodeType == ERepositoryObjectType.METADATA_SALESFORCE_SCHEMA) {
            // item = (SalesforceSchemaConnectionItem) node.getObject().getProperty().getItem();
            // connection = (SalesforceSchemaConnection) item.getConnection();
            // metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
            // String nextId = ProxyRepositoryFactory.getInstance().getNextId();
            // metadataTable.setId(nextId);
            // metadataTable.setLabel(getStringIndexed(metadataTable.getLabel()));
            // creation = true;
            // } else
            if (nodeType == ERepositoryObjectType.METADATA_SALESFORCE_MODULE) {
                item = (SalesforceSchemaConnectionItem) node.getObject().getProperty().getItem();
                connection = (SalesforceSchemaConnection) item.getConnection();
                metadataTable = TableHelper.findByLabel(connection, tableLabel);
                creation = false;
            } else {
                return;

            }
            initContextMode(item);
            if (metadataTable.eContainer() instanceof SalesforceModuleUnit) {
                SalesforceModuleUnit unit = (SalesforceModuleUnit) metadataTable.eContainer();
                connection.setModuleName(unit.getModuleName());
            }
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            SalesforceSchemasWizard salesforceSchemasWizard = new SalesforceSchemasWizard(PlatformUI.getWorkbench(), creation,
                    node.getObject(), metadataTable, getExistingNames(), forceReadOnly, null, null, node.getProperties(
                            EProperties.LABEL).toString());
            // salesforceSchemaWizard.setRepositoryObject(node.getObject());

            WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), salesforceSchemasWizard);
            handleWizard(node, wizardDialog);
        }
    }

    public void createSalesforceModuleWizard(RepositoryNode node, final boolean forceReadOnly) {
        SalesforceSchemaConnection connection = null;
        MetadataTable metadataTable = null;
        boolean creation = false;
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            String tableLabel = (String) node.getProperties(EProperties.LABEL);

            SalesforceSchemaConnectionItem item = null;
            if (nodeType == ERepositoryObjectType.METADATA_CON_TABLE) {
                if (node.getParent().isBin() && node.getParent().getObject() == null) {
                    item = (SalesforceSchemaConnectionItem) node.getObject().getProperty().getItem();
                } else {
                    item = (SalesforceSchemaConnectionItem) node.getParent().getObject().getProperty().getItem();
                }
                connection = (SalesforceSchemaConnection) item.getConnection();
                metadataTable = TableHelper.findByLabel(connection, tableLabel);
                creation = false;
            } else if (nodeType == ERepositoryObjectType.METADATA_SALESFORCE_SCHEMA) {
                item = (SalesforceSchemaConnectionItem) node.getObject().getProperty().getItem();
                connection = (SalesforceSchemaConnection) item.getConnection();
                metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
                String nextId = ProxyRepositoryFactory.getInstance().getNextId();
                metadataTable.setId(nextId);
                metadataTable.setLabel(getStringIndexed(metadataTable.getLabel()));
                creation = false;
            } else if (nodeType == ERepositoryObjectType.METADATA_SALESFORCE_MODULE) {
                item = (SalesforceSchemaConnectionItem) node.getObject().getProperty().getItem();
                connection = (SalesforceSchemaConnection) item.getConnection();
                metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
                String nextId = ProxyRepositoryFactory.getInstance().getNextId();
                metadataTable.setId(nextId);
                metadataTable.setLabel(getStringIndexed(metadataTable.getLabel()));
                creation = false;
            } else {
                return;

            }
            initContextMode(item);
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            SalesforceModulesWizard salesforceSchemaWizard = new SalesforceModulesWizard(PlatformUI.getWorkbench(), creation,
                    node.getObject(), metadataTable, getExistingNames(), forceReadOnly, null, null);
            // salesforceSchemaWizard.setRepositoryObject(node.getObject());

            WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), salesforceSchemaWizard);
            handleWizard(node, wizardDialog);
        }
    }

    /**
     * 
     * DOC hwang Comment method "createSAPSchemaWizard".
     * 
     * @param selection
     * @param forceReadOnly
     */
    public void createSAPSchemaWizard(RepositoryNode node, final boolean forceReadOnly) {
        boolean creation = false;
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);

            SAPConnectionItem item = null;
            if (nodeType == ERepositoryObjectType.METADATA_CON_TABLE) {
                item = (SAPConnectionItem) node.getObject().getProperty().getItem();
                creation = false;
            } else if (nodeType == ERepositoryObjectType.METADATA_SAPCONNECTIONS) {
                item = (SAPConnectionItem) node.getObject().getProperty().getItem();
                creation = true;
            } else {
                return;
            }
            initContextMode(item);

            ISAPProviderService sapService = null;
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ISAPProviderService.class)) {
                sapService = (ISAPProviderService) GlobalServiceRegister.getDefault().getService(ISAPProviderService.class);
                if (sapService != null) {
                    IWizard sapWizard = sapService.newWizard(PlatformUI.getWorkbench(), creation, node, getExistingNames());
                    if (sapWizard != null) {
                        WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), sapWizard);
                        handleWizard(node, wizardDialog);
                    }
                }
            }
        }
    }

    /**
     * DOC qzhang Comment method "createWSDLSchemaWizard".
     * 
     * @param selection
     * @param forceReadOnly
     */
    public void createWSDLSchemaWizard(RepositoryNode node, final boolean forceReadOnly) {
        WSDLSchemaConnection connection = null;
        MetadataTable metadataTable = null;

        boolean creation = false;
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            String tableLabel = (String) node.getProperties(EProperties.LABEL);

            WSDLSchemaConnectionItem item = null;
            if (nodeType == ERepositoryObjectType.METADATA_CON_TABLE) {
                if (node.getParent().isBin() && node.getParent().getObject() == null) {
                    item = (WSDLSchemaConnectionItem) node.getObject().getProperty().getItem();
                } else {
                    item = (WSDLSchemaConnectionItem) node.getParent().getObject().getProperty().getItem();
                }
                connection = (WSDLSchemaConnection) item.getConnection();
                metadataTable = TableHelper.findByLabel(connection, tableLabel);
                creation = false;
            } else if (nodeType == ERepositoryObjectType.METADATA_WSDL_SCHEMA) {
                item = (WSDLSchemaConnectionItem) node.getObject().getProperty().getItem();
                connection = (WSDLSchemaConnection) item.getConnection();
                metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
                String nextId = ProxyRepositoryFactory.getInstance().getNextId();
                metadataTable.setId(nextId);
                metadataTable.setLabel(getStringIndexed(metadataTable.getLabel()));
                GenericPackage g = (GenericPackage) ConnectionHelper.getPackage(connection.getName(), connection,
                        GenericPackage.class);
                if (g != null) { // hywang
                    g.getOwnedElement().add(metadataTable);
                } else {
                    GenericPackage gpkg = ConnectionFactory.eINSTANCE.createGenericPackage();
                    PackageHelper.addMetadataTable(metadataTable, gpkg);
                    ConnectionHelper.addPackage(gpkg, connection);

                }
                creation = true;
            } else {
                return;
            }
            initContextMode(item);
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            WSDLSchemaTableWizard ldapSchemaWizard = new WSDLSchemaTableWizard(PlatformUI.getWorkbench(), creation, item,
                    metadataTable, forceReadOnly);
            ldapSchemaWizard.setRepositoryObject(node.getObject());

            WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), ldapSchemaWizard);
            handleWizard(node, wizardDialog);
        }

    }

    /**
     * DOC ocarbone Comment method "creataDatabaseTableWizard".
     * 
     * @param selection
     * @return
     */
    @SuppressWarnings("unchecked")
    protected void createDatabaseTableWizard(final RepositoryNode node, final boolean forceReadOnly) {

        // Define the repositoryObject
        DatabaseConnection connection = null;
        MetadataTable metadataTable = null;

        boolean creation = false;
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            String metadataTableLabel = (String) node.getProperties(EProperties.LABEL);

            DatabaseConnectionItem connItem = null;
            Item item = node.getObject().getProperty().getItem();
            if(!(item instanceof DatabaseConnectionItem)){
                return;
            }
            connItem = (DatabaseConnectionItem) item;
            connection = (DatabaseConnection) connItem.getConnection();
            if (nodeType == ERepositoryObjectType.METADATA_CON_TABLE) {
                metadataTable = TableHelper.findByLabel(connection, metadataTableLabel);
                creation = false;
            } else if (nodeType == ERepositoryObjectType.METADATA_CONNECTIONS) {
                creation = true;
            } else {
                return;
            }

            initContextMode(connItem);
            openDatabaseTableWizard(connItem, metadataTable, forceReadOnly, node, creation);
        }
    }

    private void openDatabaseTableWizard(final DatabaseConnectionItem item, final MetadataTable metadataTable,
            final boolean forceReadOnly, final RepositoryNode node, final boolean creation) {
        UIJob job = new UIJob(Messages.getString("CreateTableAction.action.createTitle")) { //$NON-NLS-1$

            @Override
            public IStatus runInUIThread(final IProgressMonitor monitor) {
                String name = "User action : " + getText(); //$NON-NLS-1$
                RepositoryWorkUnit<Object> repositoryWorkUnit = new RepositoryWorkUnit<Object>(name, this) {

                    @Override
                    protected void run() throws LoginException, PersistenceException {

                        monitor.beginTask(Messages.getString("CreateTableAction.action.createTitle"), IProgressMonitor.UNKNOWN); //$NON-NLS-1$

                        if (!monitor.isCanceled()) {
                            final ManagerConnection managerConnection = new ManagerConnection();

                            DatabaseConnection connection = (DatabaseConnection) item.getConnection();
                            boolean useKrb = Boolean.valueOf(connection.getParameters().get(
                                    ConnParameterKeys.CONN_PARA_KEY_USE_KRB));
                            // TUP-596 : Update the context name in connection when the user does a context switch in DI
                            String oldContextName = connection.getContextName();
                            IMetadataConnection metadataConnection = ConvertionHelper.convert(connection, false, null);
                            String newContextName = connection.getContextName();
                            if (oldContextName != null && newContextName != null && !oldContextName.equals(newContextName)) {
                                if (node != null && node.getObject() != null && node.getObject().getProperty() != null) {
                                    Item itemTemp = node.getObject().getProperty().getItem();
                                    if (itemTemp != null && itemTemp instanceof ConnectionItem) {
                                        ConnectionItem connItem = (ConnectionItem) itemTemp;
                                        SwitchContextGroupNameImpl.getInstance().updateContextGroup(connItem, newContextName);
                                    }
                                }
                            }
                            if (!metadataConnection.getDbType().equals(EDatabaseConnTemplate.GODBC.getDBDisplayName())
                                    && !metadataConnection.getDbType().equals(EDatabaseConnTemplate.ACCESS.getDBDisplayName())
                                    && !metadataConnection.getDbType().equals(
                                            EDatabaseConnTemplate.GENERAL_JDBC.getDBDisplayName())) {
                                // TODO 1. To identify if it is hive connection.
                                String hiveMode = (String) metadataConnection
                                        .getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_MODE);
                                if (EDatabaseTypeName.HIVE.getDisplayName().equals(metadataConnection.getDbType())) {
                                    // metadataConnection.setDriverJarPath((String)metadataConnection
                                    // .getParameter(ConnParameterKeys.CONN_PARA_KEY_METASTORE_CONN_DRIVER_JAR));
                                    if (HiveModeInfo.get(hiveMode) == HiveModeInfo.EMBEDDED) {
                                        JavaSqlFactory.doHivePreSetup((DatabaseConnection) metadataConnection
                                                .getCurrentConnection());
                                    }
                                } else if (EDatabaseTypeName.IMPALA.getDisplayName().equals(metadataConnection.getDbType())) {
                                    DatabaseConnection originalValueConnection = null;
                                    IRepositoryContextService repositoryContextService = CoreRuntimePlugin.getInstance()
                                            .getRepositoryContextService();
                                    if (repositoryContextService != null) {
                                        originalValueConnection = repositoryContextService
                                                .cloneOriginalValueConnection(connection, false, null);
                                    }
                                    if (originalValueConnection != null) {
                                        metadataConnection.setUrl(originalValueConnection.getURL());
                                    }
                                } else {
                                    String genUrl = DatabaseConnStrUtil.getURLString(metadataConnection.getDbType(),
                                            metadataConnection.getDbVersionString(), metadataConnection.getServerName(),
                                            metadataConnection.getUsername(), metadataConnection.getPassword(),
                                            metadataConnection.getPort(), metadataConnection.getDatabase(),
                                            metadataConnection.getFileFieldName(), metadataConnection.getDataSourceName(),
                                            metadataConnection.getDbRootPath(), metadataConnection.getAdditionalParams());
                                    if (!(metadataConnection.getDbType().equals(EDatabaseConnTemplate.IMPALA.getDBDisplayName()) && useKrb)) {
                                        metadataConnection.setUrl(genUrl);
                                    }
                                }

                            }
                            // bug 23508:even open type is metaTable,not connection,we always need the connection's
                            // datapackage to find the table schema when click the retrieve schema button
                            if (connection != null) {
                                EList<orgomg.cwm.objectmodel.core.Package> dp = connection.getDataPackage();
                                Collection<Package> newDataPackage = EcoreUtil.copyAll(dp);
                                ConnectionHelper.addPackages(newDataPackage,
                                        (DatabaseConnection) metadataConnection.getCurrentConnection());
                            }
                            if (creation) {
                                String hiveMode = (String) metadataConnection
                                        .getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_MODE);
                                if (EDatabaseTypeName.HIVE.getDisplayName().equals(metadataConnection.getDbType())) {
                                    try {
                                        HiveConnectionManager.getInstance().checkConnection(metadataConnection);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    managerConnection.check(metadataConnection);
                                }

                                // ExtractMetaDataUtils.metadataCon = metadataConnection;
                                // when open,set use synonyms false.
                                ExtractMetaDataUtils.getInstance().setUseAllSynonyms(false);

                                IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
                                boolean repositoryObjectEditable = factory.isEditableAndLockIfPossible(node.getObject());
                                if (!repositoryObjectEditable) {
                                    boolean flag = MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                                            .getShell(), Messages.getString("CreateTableAction.action.Warning"),
                                            Messages.getString("CreateTableAction.action.NotLockMessage"));
                                    if (flag) {
                                        DatabaseTableWizard databaseTableWizard = new DatabaseTableWizard(
                                                PlatformUI.getWorkbench(), creation, node.getObject(), metadataTable,
                                                getExistingNames(), forceReadOnly, managerConnection, metadataConnection);

                                        WizardDialog wizardDialog = new WizardDialog(PlatformUI.getWorkbench()
                                                .getActiveWorkbenchWindow().getShell(), databaseTableWizard);
                                        wizardDialog.setBlockOnOpen(true);
                                        handleWizard(node, wizardDialog);
                                    }
                                } else {
                                    DatabaseTableWizard databaseTableWizard = new DatabaseTableWizard(PlatformUI.getWorkbench(),
                                            creation, node.getObject(), metadataTable, getExistingNames(), forceReadOnly,
                                            managerConnection, metadataConnection);

                                    WizardDialog wizardDialog = new WizardDialog(PlatformUI.getWorkbench()
                                            .getActiveWorkbenchWindow().getShell(), databaseTableWizard);
                                    wizardDialog.setBlockOnOpen(true);
                                    handleWizard(node, wizardDialog);
                                }
                            } else {
                                // added for bug 16595
                                // no need connect to database when double click one schema.
                                final boolean skipStep = true;

                                DatabaseTableWizard databaseTableWizard = new DatabaseTableWizard(PlatformUI.getWorkbench(),
                                        creation, node.getObject(), metadataTable, getExistingNames(), forceReadOnly,
                                        managerConnection, metadataConnection);
                                databaseTableWizard.setSkipStep(skipStep);
                                WizardDialog wizardDialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                                        .getShell(), databaseTableWizard);
                                handleWizard(node, wizardDialog);
                            }

                        }
                    }
                };
                repositoryWorkUnit.setAvoidUnloadResources(isAvoidUnloadResources());
                IRepositoryService repositoryService = (IRepositoryService) GlobalServiceRegister.getDefault().getService(
                        IRepositoryService.class);
                repositoryService.getProxyRepositoryFactory().executeRepositoryWorkUnit(repositoryWorkUnit);
                monitor.done();
                return Status.OK_STATUS;
            };
        };
        job.setUser(true);
        job.addJobChangeListener(new JobChangeAdapter() {

            @Override
            public void done(IJobChangeEvent event) {
                if (!event.getResult().isOK()) {
                    log.error(event.getResult().getMessage(), event.getResult().getException());
                } // else eveything is fine so do not log anything
            }
        });
        job.schedule();

    }

    /**
     * 
     * DOC ycbai Comment method "createExtenseNodeSchemaWizard".
     * 
     * <p>
     * Create and open a wizard associated with the node.
     * </p>
     * 
     * @param nodeItemType the type of item which the schema node belongs to.
     * @param node the schema node.
     * @param forceReadOnly whether or not set the wizard as readonly.
     */
    protected void createExtenseNodeSchemaWizard(final ERepositoryObjectType nodeItemType, final RepositoryNode node,
            final boolean forceReadOnly) {
        MetadataTable metadataTable = null;
        boolean creation = false;
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            ConnectionItem item = null;
            if (nodeType == ERepositoryObjectType.METADATA_CON_TABLE) {
                if (node.getParent().isBin() && node.getParent().getObject() == null) {
                    item = (ConnectionItem) node.getObject().getProperty().getItem();
                } else {
                    IRepositoryViewObject repObj = node.getParent().getObject();
                    if (repObj != null) {
                        item = (ConnectionItem) repObj.getProperty().getItem();
                    }
                }
                if (item != null) {
                    Connection connection = item.getConnection();
                    String tableLabel = (String) node.getProperties(EProperties.LABEL);
                    metadataTable = TableHelper.findByLabel(connection, tableLabel);
                }
                creation = false;
            } else if (nodeType == nodeItemType) {
                item = (ConnectionItem) node.getObject().getProperty().getItem();
                creation = true;
            } else {
                return;
            }
            initContextMode(item);
            for (IRepositoryContentHandler handler : RepositoryContentManager.getHandlers()) {
                if (handler.isRepObjType(nodeItemType)) {
                    IWizard schemaWizard = handler.newSchemaWizard(PlatformUI.getWorkbench(), creation, node.getObject(),
                            metadataTable, getExistingNames(), forceReadOnly);
                    WizardDialog wizardDialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                            schemaWizard);
                    handleWizard(node, wizardDialog);
                    return;
                }
            }
        }
    }

    public boolean checkConnectStatus(ManagerConnection managerConnection, IMetadataConnection metadataConnection) {
        boolean skipStep = false;
        managerConnection.check(metadataConnection);
        if (managerConnection.getIsValide()) {
            List<String> itemTableName = null;
            // hyWang remove the second parameter of method returnTablesFormConnection for bug7374
            try {
                itemTableName = ExtractMetaDataFromDataBase.returnTablesFormConnection(metadataConnection);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (itemTableName == null || itemTableName.isEmpty()) {
                skipStep = true;
            }
        } else {
            skipStep = true;
        }
        return skipStep;
    }

    /**
     * DOC zli Comment method "checkConnectStatus".
     * 
     * @param check
     * @param itemTableName
     * @return
     */
    public boolean checkConnectStatus(Boolean check, List<String> itemTableName) {
        boolean skipStep = false;
        if (check) {
            if (itemTableName == null || itemTableName.isEmpty()) {
                skipStep = true;
            }
        } else {
            skipStep = true;
        }
        return skipStep;
    }

    /**
     * DOC zli Comment method "noTableExistInDB".
     * 
     * @param managerConnection
     * @param metadataConnection
     * @return
     */

    public boolean noTableExistInDB(Boolean check, List<String> itemTableName) {
        if (check && (itemTableName == null || itemTableName.isEmpty())) {
            return true;
        }
        return false;
    }

    protected RepositoryNode getMetadataNode(RepositoryNode node) {
        RepositoryNode parent = node.getParent();
        if (parent != null && parent.getObject() != null) {
            IRepositoryViewObject object = parent.getObject();
            Item item = object.getProperty().getItem();
            if (item instanceof ConnectionItem) {
                return parent;
            }
        }
        if (parent != null && parent.getParent() == null) {
            return parent;
        }
        return getMetadataNode(parent);
    }
}
