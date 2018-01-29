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
package org.talend.repository.ui.wizards.metadata.connection.ldap;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.metadata.IMetadataContextModeManager;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.GenericPackage;
import org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.update.RepositoryUpdateManager;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.PackageHelper;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;
import org.talend.metadata.managment.ui.wizard.CheckLastVersionRepositoryWizard;
import org.talend.metadata.managment.ui.wizard.PropertiesWizardPage;
import org.talend.metadata.managment.ui.wizard.metadata.MetadataContextModeManager;
import org.talend.metadata.managment.ui.wizard.metadata.connection.Step0WizardPage;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;

/**
 * The class is used for LDAP schema on Repository View. <br/>
 * 
 * @author ftang, 18/09/2007
 * 
 */
public class LDAPSchemaWizard extends CheckLastVersionRepositoryWizard implements INewWizard {

    private static Logger log = Logger.getLogger(LDAPSchemaWizard.class);

    private PropertiesWizardPage ldapSchemaWizardPage0;

    private LDAPSchemaWizardPage ldapSchemaWizardPage1;

    private LDAPSchemaWizardPage ldapSchemaWizardPage2;

    private LDAPSchemaWizardPage ldapSchemaWizardPage3;

    private LDAPSchemaWizardPage ldapSchemaWizardPage4;

    private LDAPSchemaConnection connection;

    private Property connectionProperty;

    private boolean isSinglePageOnly;

    private static final String ALL_STEPS = "5"; //$NON-NLS-1$

    private Map<String, String> oldTableMap;

    private IMetadataContextModeManager contextModeManager;

    private List<IMetadataTable> oldMetadataTable;

    private boolean isToolbar;

    private String originaleObjectLabel;

    private String originalVersion;

    private String originalPurpose;

    private String originalDescription;

    private String originalStatus;

    /**
     * Sets the isToolbar.
     * 
     * @param isToolbar the isToolbar to set
     */
    public void setToolbar(boolean isToolbar) {
        this.isToolbar = isToolbar;
    }

    /**
     * LDAPSchemaWizard constructor comment.
     * 
     * @param workbench
     * @param creation
     * @param selection
     * @param existingNames
     * @param b
     */
    public LDAPSchemaWizard(IWorkbench workbench, boolean creation, ISelection selection, String[] existingNames,
            boolean isSinglePageOnly) {
        super(workbench, creation);
        this.selection = selection;
        this.existingNames = existingNames;
        this.isSinglePageOnly = isSinglePageOnly;
        setNeedsProgressMonitor(true);

        // TODO: should to changed icon.
        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(ECoreImage.DEFAULT_WIZ));

        if (selection == null || existingNames == null) {
            connection = ConnectionFactory.eINSTANCE.createLDAPSchemaConnection();
            MetadataTable metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            metadataTable.setId(factory.getNextId());
            GenericPackage g = (GenericPackage) ConnectionHelper.getPackage(connection.getName(), connection,
                    GenericPackage.class);
            if (g != null) { // hywang
                g.getOwnedElement().add(metadataTable);
            } else {
                GenericPackage gpkg = ConnectionFactory.eINSTANCE.createGenericPackage();
                PackageHelper.addMetadataTable(metadataTable, gpkg);
                ConnectionHelper.addPackage(gpkg, connection);

            }
            connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
            connectionProperty.setAuthor(((RepositoryContext) CoreRuntimePlugin.getInstance().getContext()
                    .getProperty(Context.REPOSITORY_CONTEXT_KEY)).getUser());
            connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
            connectionProperty.setStatusCode(""); //$NON-NLS-1$

            connectionItem = PropertiesFactory.eINSTANCE.createLDAPSchemaConnectionItem();
            connectionItem.setProperty(connectionProperty);
            connectionItem.setConnection(connection);
            initTable();
            return;
        }

        Object obj = ((IStructuredSelection) selection).getFirstElement();
        RepositoryNode node = (RepositoryNode) obj;
        switch (node.getType()) {
        case SIMPLE_FOLDER:
        case REPOSITORY_ELEMENT:
            pathToSave = RepositoryNodeUtilities.getPath(node);
            break;
        case SYSTEM_FOLDER:
            pathToSave = new Path(""); //$NON-NLS-1$
            break;
        }

        switch (node.getType()) {
        case SIMPLE_FOLDER:
        case SYSTEM_FOLDER:
            connection = ConnectionFactory.eINSTANCE.createLDAPSchemaConnection();
            connection.setName(ERepositoryObjectType.METADATA_LDAP_SCHEMA.getKey());
            MetadataTable metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            metadataTable.setId(factory.getNextId());
            GenericPackage g = (GenericPackage) ConnectionHelper.getPackage(connection.getName(), connection,
                    GenericPackage.class);
            if (g != null) { // hywang
                g.getOwnedElement().add(metadataTable);
            } else {
                GenericPackage gpkg = ConnectionFactory.eINSTANCE.createGenericPackage();
                gpkg.setName(connection.getName());
                PackageHelper.addMetadataTable(metadataTable, gpkg);
                ConnectionHelper.addPackage(gpkg, connection);

            }
            connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
            connectionProperty.setAuthor(((RepositoryContext) CoreRuntimePlugin.getInstance().getContext()
                    .getProperty(Context.REPOSITORY_CONTEXT_KEY)).getUser());
            connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
            connectionProperty.setStatusCode(""); //$NON-NLS-1$

            connectionItem = PropertiesFactory.eINSTANCE.createLDAPSchemaConnectionItem();
            connectionItem.setProperty(connectionProperty);
            connectionItem.setConnection(connection);
            break;

        case REPOSITORY_ELEMENT:
            connection = (LDAPSchemaConnection) ((ConnectionItem) node.getObject().getProperty().getItem()).getConnection();
            connectionProperty = node.getObject().getProperty();
            connectionItem = (ConnectionItem) node.getObject().getProperty().getItem();
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            setRepositoryObject(node.getObject());
            isRepositoryObjectEditable();
            initLockStrategy();
            break;
        }
        initTable();
        initConnection();
        if (!creation) {
            this.originaleObjectLabel = this.connectionItem.getProperty().getDisplayName();
            this.originalVersion = this.connectionItem.getProperty().getVersion();
            this.originalDescription = this.connectionItem.getProperty().getDescription();
            this.originalPurpose = this.connectionItem.getProperty().getPurpose();
            this.originalStatus = this.connectionItem.getProperty().getStatusCode();
        }
    }

    public LDAPSchemaWizard(IWorkbench workbench, boolean creation, RepositoryNode node, String[] existingNames,
            boolean isSinglePageOnly) {
        super(workbench, creation);
        this.existingNames = existingNames;
        this.isSinglePageOnly = isSinglePageOnly;
        setNeedsProgressMonitor(true);

        // TODO: should to changed icon.
        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(ECoreImage.DEFAULT_WIZ));
        switch (node.getType()) {
        case SIMPLE_FOLDER:
        case REPOSITORY_ELEMENT:
            pathToSave = RepositoryNodeUtilities.getPath(node);
            break;
        case SYSTEM_FOLDER:
            pathToSave = new Path(""); //$NON-NLS-1$
            break;
        }

        switch (node.getType()) {
        case SIMPLE_FOLDER:
        case SYSTEM_FOLDER:
            connection = ConnectionFactory.eINSTANCE.createLDAPSchemaConnection();
            connection.setName(ERepositoryObjectType.METADATA_LDAP_SCHEMA.getKey());
            MetadataTable metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            metadataTable.setId(factory.getNextId());
            GenericPackage g = (GenericPackage) ConnectionHelper.getPackage(connection.getName(), connection,
                    GenericPackage.class);
            if (g != null) { // hywang
                g.getOwnedElement().add(metadataTable);
            } else {
                GenericPackage gpkg = ConnectionFactory.eINSTANCE.createGenericPackage();
                gpkg.setName(connection.getName());
                PackageHelper.addMetadataTable(metadataTable, gpkg);
                ConnectionHelper.addPackage(gpkg, connection);

            }
            connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
            connectionProperty.setAuthor(((RepositoryContext) CoreRuntimePlugin.getInstance().getContext()
                    .getProperty(Context.REPOSITORY_CONTEXT_KEY)).getUser());
            connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
            connectionProperty.setStatusCode(""); //$NON-NLS-1$

            connectionItem = PropertiesFactory.eINSTANCE.createLDAPSchemaConnectionItem();
            connectionItem.setProperty(connectionProperty);
            connectionItem.setConnection(connection);
            break;

        case REPOSITORY_ELEMENT:
            connection = (LDAPSchemaConnection) ((ConnectionItem) node.getObject().getProperty().getItem()).getConnection();
            connectionProperty = node.getObject().getProperty();
            connectionItem = (ConnectionItem) node.getObject().getProperty().getItem();
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            setRepositoryObject(node.getObject());
            isRepositoryObjectEditable();
            initLockStrategy();
            break;
        }
        initTable();
        initConnection();
        if (!creation) {
            this.originaleObjectLabel = this.connectionItem.getProperty().getDisplayName();
            this.originalVersion = this.connectionItem.getProperty().getVersion();
            this.originalDescription = this.connectionItem.getProperty().getDescription();
            this.originalPurpose = this.connectionItem.getProperty().getPurpose();
            this.originalStatus = this.connectionItem.getProperty().getStatusCode();
        }
    }

    private void initTable() {
        if (connectionItem != null) {
            oldTableMap = RepositoryUpdateManager.getTableIdAndNameMap(connectionItem);
            oldMetadataTable = RepositoryUpdateManager.getConversionMetadataTables(connectionItem.getConnection());
        }
    }

    private void initConnection() {
        ConnectionContextHelper.checkContextMode(connectionItem);
        contextModeManager = new MetadataContextModeManager();
        if (connectionItem.getConnection().isContextMode()) {
            ContextType contextTypeForContextMode = ConnectionContextHelper.getContextTypeForContextMode(
                    connectionItem.getConnection(), true);
            contextModeManager.setSelectedContextType(contextTypeForContextMode);
        }
    }

    /**
     * Adding the page to the wizard.
     */
    @Override
    public void addPages() {

        ldapSchemaWizardPage0 = null;
        ldapSchemaWizardPage1 = null;
        ldapSchemaWizardPage2 = null;
        ldapSchemaWizardPage3 = null;
        ldapSchemaWizardPage4 = null;

        if (creation) {
            setWindowTitle(Messages.getString("LDAPSchemaWizard.CreateNewLdapSchema"));// Messages.getString( //$NON-NLS-1$
            // "DelimitedFileWizard.windowTitleCreate"
            // ));
            // //$NON-NLS-1$
            // //$NON-NLS-1$
            if (isToolbar) {
                pathToSave = null;
            }
            ldapSchemaWizardPage0 = new Step0WizardPage(connectionProperty, pathToSave,
                    ERepositoryObjectType.METADATA_GENERIC_SCHEMA, !isRepositoryObjectEditable(), creation);
            ldapSchemaWizardPage0.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 1 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS); //$NON-NLS-1$ //$NON-NLS-2$
            ldapSchemaWizardPage0.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep0")); //$NON-NLS-1$
            addPage(ldapSchemaWizardPage0);

            ldapSchemaWizardPage1 = new LDAPSchemaWizardPage(1, connectionItem, isRepositoryObjectEditable(), null,
                    contextModeManager);
            ldapSchemaWizardPage1.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 2 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS); //$NON-NLS-1$ //$NON-NLS-2$
            ldapSchemaWizardPage1.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep1")); //$NON-NLS-1$
            addPage(ldapSchemaWizardPage1);

            ldapSchemaWizardPage2 = new LDAPSchemaWizardPage(2, connectionItem, isRepositoryObjectEditable(), null,
                    contextModeManager);
            ldapSchemaWizardPage2.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 3 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS); //$NON-NLS-1$ //$NON-NLS-2$
            ldapSchemaWizardPage2.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep2")); //$NON-NLS-1$
            addPage(ldapSchemaWizardPage2);

            ldapSchemaWizardPage3 = new LDAPSchemaWizardPage(3, connectionItem, isRepositoryObjectEditable(), null,
                    contextModeManager);
            ldapSchemaWizardPage3.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 4 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS); //$NON-NLS-1$ //$NON-NLS-2$
            ldapSchemaWizardPage3.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep2")); //$NON-NLS-1$
            addPage(ldapSchemaWizardPage3);

            ldapSchemaWizardPage4 = new LDAPSchemaWizardPage(4, connectionItem, isRepositoryObjectEditable(), null,
                    contextModeManager);
            ldapSchemaWizardPage4.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 5 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS); //$NON-NLS-1$ //$NON-NLS-2$
            ldapSchemaWizardPage4.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep2")); //$NON-NLS-1$
            addPage(ldapSchemaWizardPage4);

            ldapSchemaWizardPage0.setPageComplete(false);
            ldapSchemaWizardPage1.setPageComplete(false);
            ldapSchemaWizardPage2.setPageComplete(false);
            ldapSchemaWizardPage3.setPageComplete(false);
            ldapSchemaWizardPage4.setPageComplete(false);

        } else {

            setWindowTitle(Messages.getString("LDAPSchemaWizard.UpdateLdapSchema"));// Messages.getString( //$NON-NLS-1$
            // "DelimitedFileWizard.windowTitleUpdate"
            // ));
            // //$NON-NLS-1$
            // //$NON-NLS-1$

            ldapSchemaWizardPage0 = new Step0WizardPage(connectionProperty, pathToSave,
                    ERepositoryObjectType.METADATA_LDAP_SCHEMA, !isRepositoryObjectEditable(), creation);

            ldapSchemaWizardPage0.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 1 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " 5"); //$NON-NLS-1$ //$NON-NLS-2$
            ldapSchemaWizardPage0.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep0")); //$NON-NLS-1$
            addPage(ldapSchemaWizardPage0);

            ldapSchemaWizardPage1 = new LDAPSchemaWizardPage(1, connectionItem, isRepositoryObjectEditable(), null,
                    contextModeManager);
            ldapSchemaWizardPage1.setTitle(Messages.getString("FileWizardPage.titleUpdate") + " 2 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " 5"); //$NON-NLS-1$ //$NON-NLS-2$
            ldapSchemaWizardPage1.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep0")); //$NON-NLS-1$
            addPage(ldapSchemaWizardPage1);

            ldapSchemaWizardPage2 = new LDAPSchemaWizardPage(2, connectionItem, isRepositoryObjectEditable(), null,
                    contextModeManager);
            ldapSchemaWizardPage2.setTitle(Messages.getString("FileWizardPage.titleUpdate") + " 3 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " 5"); //$NON-NLS-1$ //$NON-NLS-2$
            ldapSchemaWizardPage2.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep0")); //$NON-NLS-1$
            addPage(ldapSchemaWizardPage2);

            ldapSchemaWizardPage3 = new LDAPSchemaWizardPage(3, connectionItem, isRepositoryObjectEditable(), null,
                    contextModeManager);
            ldapSchemaWizardPage3.setTitle(Messages.getString("FileWizardPage.titleUpdate") + " 4 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " 5"); //$NON-NLS-1$ //$NON-NLS-2$
            ldapSchemaWizardPage3.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep0")); //$NON-NLS-1$
            addPage(ldapSchemaWizardPage3);

            ldapSchemaWizardPage4 = new LDAPSchemaWizardPage(4, connectionItem, isRepositoryObjectEditable(), null,
                    contextModeManager);
            ldapSchemaWizardPage4.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 5 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS); //$NON-NLS-1$ //$NON-NLS-2$
            ldapSchemaWizardPage4.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep2")); //$NON-NLS-1$
            addPage(ldapSchemaWizardPage4);

            ldapSchemaWizardPage0.setPageComplete(true);
            ldapSchemaWizardPage1.setPageComplete(true);
            ldapSchemaWizardPage2.setPageComplete(true);
            ldapSchemaWizardPage3.setPageComplete(true);
            ldapSchemaWizardPage4.setPageComplete(true);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        if (isSinglePageOnly) {
            return true;
        }

        boolean formIsPerformed;
        if (ldapSchemaWizardPage4 == null) {
            formIsPerformed = ldapSchemaWizardPage3.isPageComplete();
        } else {
            formIsPerformed = ldapSchemaWizardPage4.isPageComplete();
        }

        if (formIsPerformed) {
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            try {
                if (creation) {
                    String nextId = factory.getNextId();
                    connectionProperty.setId(nextId);
                    factory.create(connectionItem, ldapSchemaWizardPage0.getDestinationPath());
                } else {
                    // update
                    RepositoryUpdateManager.updateMultiSchema(connectionItem, oldMetadataTable, oldTableMap);
                    refreshInFinish(ldapSchemaWizardPage0.isNameModifiedByUser());
                    updateConnectionItem();
                }
            } catch (Exception e) {
                String detailError = e.toString();
                new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("CommonWizard.persistenceException"), //$NON-NLS-1$
                        detailError);
                log.error(Messages.getString("CommonWizard.persistenceException") + "\n" + detailError); //$NON-NLS-1$ //$NON-NLS-2$
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean performCancel() {
        if (!creation) {
            connectionItem.getProperty().setVersion(this.originalVersion);
            connectionItem.getProperty().setDisplayName(this.originaleObjectLabel);
            connectionItem.getProperty().setDescription(this.originalDescription);
            connectionItem.getProperty().setPurpose(this.originalPurpose);
            connectionItem.getProperty().setStatusCode(this.originalStatus);
        }
        return super.performCancel();
    }

    /**
     * We will accept the selection in the workbench to see if we can initialize from it.
     * 
     * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
     */
    @Override
    public void init(final IWorkbench workbench, final IStructuredSelection selection2) {
        this.selection = selection2;
    }

    /**
     * Getter for connectionProperty.
     * 
     * @return the connectionProperty
     */
    public Property getConnectionProperty() {
        return this.connectionProperty;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.RepositoryWizard#getConnectionItem()
     */
    @Override
    public ConnectionItem getConnectionItem() {
        return this.connectionItem;
    }

}
