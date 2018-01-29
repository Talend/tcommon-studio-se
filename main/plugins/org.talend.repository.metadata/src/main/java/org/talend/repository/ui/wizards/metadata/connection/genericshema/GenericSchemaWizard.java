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
package org.talend.repository.ui.wizards.metadata.connection.genericshema;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
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
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.GenericPackage;
import org.talend.core.model.metadata.builder.connection.GenericSchemaConnection;
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
import org.talend.metadata.managment.ui.wizard.CheckLastVersionRepositoryWizard;
import org.talend.metadata.managment.ui.wizard.PropertiesWizardPage;
import org.talend.metadata.managment.ui.wizard.metadata.connection.Step0WizardPage;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;

/**
 * ftang class global comment. Detailled comment <br/>
 * 
 */
public class GenericSchemaWizard extends CheckLastVersionRepositoryWizard implements INewWizard {

    private static Logger log = Logger.getLogger(GenericSchemaWizard.class);

    private PropertiesWizardPage genericSchemaWizardPage0;

    private GenericSchemaWizardPage genericSchemaWizardPage1;

    private GenericSchemaConnection connection;

    private Property connectionProperty;

    private boolean isSinglePageOnly;

    private Map<String, String> oldTableMap;

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
     * Constructor for FileWizard.
     * 
     * @param workbench
     * @param selection
     * @param strings
     */
    @SuppressWarnings("unchecked")
    public GenericSchemaWizard(IWorkbench workbench, boolean creation, ISelection selection, String[] existingNames,
            boolean isSinglePageOnly) {
        super(workbench, creation);
        this.selection = selection;
        this.existingNames = existingNames;
        this.isSinglePageOnly = isSinglePageOnly;
        setNeedsProgressMonitor(true);
        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(ECoreImage.DEFAULT_WIZ));

        if (selection == null || existingNames == null) {
            connection = ConnectionFactory.eINSTANCE.createGenericSchemaConnection();
            connection.setName(ERepositoryObjectType.METADATA_GENERIC_SCHEMA.getKey());
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

            connectionItem = PropertiesFactory.eINSTANCE.createGenericSchemaConnectionItem();
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
            connection = ConnectionFactory.eINSTANCE.createGenericSchemaConnection();
            connection.setName(ERepositoryObjectType.METADATA_GENERIC_SCHEMA.getKey());
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

            connectionItem = PropertiesFactory.eINSTANCE.createGenericSchemaConnectionItem();
            connectionItem.setProperty(connectionProperty);
            connectionItem.setConnection(connection);
            break;

        case REPOSITORY_ELEMENT:
            connection = (GenericSchemaConnection) ((ConnectionItem) node.getObject().getProperty().getItem()).getConnection();
            connectionProperty = node.getObject().getProperty();
            connectionItem = (ConnectionItem) node.getObject().getProperty().getItem();
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            setRepositoryObject(node.getObject());
            isRepositoryObjectEditable();
            initLockStrategy();
            break;
        }
        if (!creation) {
            this.originaleObjectLabel = this.connectionItem.getProperty().getDisplayName();
            this.originalVersion = this.connectionItem.getProperty().getVersion();
            this.originalDescription = this.connectionItem.getProperty().getDescription();
            this.originalPurpose = this.connectionItem.getProperty().getPurpose();
            this.originalStatus = this.connectionItem.getProperty().getStatusCode();
        }
        initTable();
    }

    public GenericSchemaWizard(IWorkbench workbench, boolean creation, RepositoryNode node, String[] existingNames,
            boolean isSinglePageOnly) {
        super(workbench, creation);
        this.existingNames = existingNames;
        this.isSinglePageOnly = isSinglePageOnly;
        setNeedsProgressMonitor(true);
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
            connection = ConnectionFactory.eINSTANCE.createGenericSchemaConnection();
            connection.setName(ERepositoryObjectType.METADATA_GENERIC_SCHEMA.getKey());
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

            connectionItem = PropertiesFactory.eINSTANCE.createGenericSchemaConnectionItem();
            connectionItem.setProperty(connectionProperty);
            connectionItem.setConnection(connection);
            break;

        case REPOSITORY_ELEMENT:
            connection = (GenericSchemaConnection) ((ConnectionItem) node.getObject().getProperty().getItem()).getConnection();
            connectionProperty = node.getObject().getProperty();
            connectionItem = (ConnectionItem) node.getObject().getProperty().getItem();
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            setRepositoryObject(node.getObject());
            isRepositoryObjectEditable();
            initLockStrategy();
            break;
        }
        if (!creation) {
            this.originaleObjectLabel = this.connectionItem.getProperty().getDisplayName();
            this.originalVersion = this.connectionItem.getProperty().getVersion();
            this.originalDescription = this.connectionItem.getProperty().getDescription();
            this.originalPurpose = this.connectionItem.getProperty().getPurpose();
            this.originalStatus = this.connectionItem.getProperty().getStatusCode();
        }
        initTable();
    }

    private void initTable() {
        if (connectionItem != null) {
            oldTableMap = RepositoryUpdateManager.getTableIdAndNameMap(connectionItem);
            oldMetadataTable = RepositoryUpdateManager.getConversionMetadataTables(connectionItem.getConnection());
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
     * Adding the page to the wizard.
     */
    @Override
    public void addPages() {

        genericSchemaWizardPage0 = null;
        genericSchemaWizardPage1 = null;
        if (isToolbar) {
            pathToSave = null;
        }

        if (!creation) {
            if (ConnectionHelper.getTables(connectionItem.getConnection()).isEmpty()) {
                this.isSinglePageOnly = true;
            }
        }

        if (creation) {
            setWindowTitle(Messages.getString("GenericSchemaWizard.CreateNewGenericSchema"));// Messages.getString( //$NON-NLS-1$
            // "DelimitedFileWizard.windowTitleCreate"
            // ));
            // //$NON-NLS-1$
            // //$NON-NLS-1$
            genericSchemaWizardPage0 = new Step0WizardPage(connectionProperty, pathToSave,
                    ERepositoryObjectType.METADATA_GENERIC_SCHEMA, !isRepositoryObjectEditable(), creation);
            genericSchemaWizardPage0.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 1 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " 2"); //$NON-NLS-1$ //$NON-NLS-2$
            genericSchemaWizardPage0.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep0")); //$NON-NLS-1$
            addPage(genericSchemaWizardPage0);

            genericSchemaWizardPage1 = new GenericSchemaWizardPage(2, connectionItem, isRepositoryObjectEditable(), null);
            genericSchemaWizardPage1.setTitle(Messages.getString("GenericSchemaWizard.CreateNewGenericSchema") // Messages //$NON-NLS-1$
                    // .
                    // getString
                    // (
                    // "FileWizardPage.titleCreate"
                    // )
                    // //$NON-NLS-1$
                    // + " 2 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " 2"); //$NON-NLS-1$ //$NON-NLS-2$
            genericSchemaWizardPage1.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep3")); //$NON-NLS-1$
            addPage(genericSchemaWizardPage1);

            genericSchemaWizardPage1.setPageComplete(false);

        } else if (this.isSinglePageOnly == false) {

            setWindowTitle(Messages.getString("GenericSchemaWizard.UpdateGenericSchema"));// Messages.getString( //$NON-NLS-1$
            // "DelimitedFileWizard.windowTitleUpdate"
            // ));
            // //$NON-NLS-1$
            // //$NON-NLS-1$

            genericSchemaWizardPage0 = new Step0WizardPage(connectionProperty, pathToSave,
                    ERepositoryObjectType.METADATA_GENERIC_SCHEMA, !isRepositoryObjectEditable(), creation);

            genericSchemaWizardPage0.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 1 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " 2"); //$NON-NLS-1$ //$NON-NLS-2$
            genericSchemaWizardPage0.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep0")); //$NON-NLS-1$
            addPage(genericSchemaWizardPage0);
            genericSchemaWizardPage1 = new GenericSchemaWizardPage(2, connectionItem, isRepositoryObjectEditable(), null);
            genericSchemaWizardPage1.setTitle(Messages.getString("FileWizardPage.titleUpdate") + " 2 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " 2"); //$NON-NLS-1$ //$NON-NLS-2$
            genericSchemaWizardPage1.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep0")); //$NON-NLS-1$
            addPage(genericSchemaWizardPage1);
            genericSchemaWizardPage1.setPageComplete(true);

            genericSchemaWizardPage0.setPageComplete(true);
            genericSchemaWizardPage1.setPageComplete(isRepositoryObjectEditable());
        } else {
            setWindowTitle(Messages.getString("GenericSchemaWizard.SaveAsGenericSchema"));// Messages.getString( //$NON-NLS-1$
            // "DelimitedFileWizard.windowTitleUpdate"
            // ));
            // //$NON-NLS-1$
            // //$NON-NLS-1$
            genericSchemaWizardPage0 = new Step0WizardPage(connectionProperty, pathToSave,
                    ERepositoryObjectType.METADATA_GENERIC_SCHEMA, !isRepositoryObjectEditable(), true);
            genericSchemaWizardPage0.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 1 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " 1"); //$NON-NLS-1$ //$NON-NLS-2$
            genericSchemaWizardPage0.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep0")); //$NON-NLS-1$

            addPage(genericSchemaWizardPage0);
            genericSchemaWizardPage0.setPageComplete(true);

        }
    }

    /**
     * This method determine if the 'Finish' button is enable This method is called when 'Finish' button is pressed in
     * the wizard. We will create an operation and run it using wizard as execution context.
     */
    @Override
    public boolean performFinish() {
        if (isSinglePageOnly) {

            return true;
        }

        boolean formIsPerformed;
        if (genericSchemaWizardPage1 == null) {
            formIsPerformed = genericSchemaWizardPage0.isPageComplete();
        } else {
            formIsPerformed = genericSchemaWizardPage1.isPageComplete();
        }

        if (formIsPerformed) {
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            try {
                if (creation) {
                    String nextId = factory.getNextId();
                    connectionProperty.setId(nextId);
                    factory.create(connectionItem, genericSchemaWizardPage0.getDestinationPath());
                } else {
                    // update
                    RepositoryUpdateManager.updateMultiSchema(connectionItem, oldMetadataTable, oldTableMap);
                    updateConnectionItem();
                    // 0005170: Schema renamed - new name not pushed out to dependant jobs
                    boolean isModified = genericSchemaWizardPage0.isNameModifiedByUser();
                    if (isModified) {
                        CoreRuntimePlugin.getInstance().getDesignerCoreService().refreshComponentView(connectionItem);
                    }
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

    /**
     * Getter for destinationPath.
     * 
     * @return the destinationPath
     */
    public IPath getPathForSaveAsGenericSchema() {
        return this.genericSchemaWizardPage0.getPathForSaveAsGenericSchema();
    }

    @Override
    public ConnectionItem getConnectionItem() {
        return this.connectionItem;
    }

}
