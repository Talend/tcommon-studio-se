// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.wizards.metadata.connection.wsdl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.PluginChecker;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.metadata.IMetadataContextModeManager;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.GenericPackage;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.update.RepositoryUpdateManager;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.ui.webService.WebServiceTosSaveManager;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.PackageHelper;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;
import org.talend.metadata.managment.ui.wizard.CheckLastVersionRepositoryWizard;
import org.talend.metadata.managment.ui.wizard.PropertiesWizardPage;
import org.talend.metadata.managment.ui.wizard.metadata.MetadataContextModeManager;
import org.talend.metadata.managment.ui.wizard.metadata.connection.Step0WizardPage;
import org.talend.repository.ProjectManager;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;

/**
 * DOC qwei class global comment. Detailled comment
 */
public class WSDLSchemaWizard extends CheckLastVersionRepositoryWizard implements INewWizard {

    private static Logger log = Logger.getLogger(WSDLSchemaWizard.class);

    private PropertiesWizardPage wsdlSchemaWizardPage0;

    private WSDLSchemaWizardPage wsdlSchemaWizardPage1;

    private WSDLSchemaWizardPage wsdlSchemaWizardPage2;

    private Property connectionProperty;

    private WSDLSchemaConnection connection;

    private boolean isSinglePageOnly;

    private static final String ALL_STEPS = "4"; //$NON-NLS-1$

    private static final String TOS_STEP = "3";

    private Map<String, String> oldTableMap;

    private IMetadataContextModeManager contextModeManager;

    private List<IMetadataTable> oldMetadataTable;

    private WSDLSchemaSelectWizardPage wsdlSelectPage;

    private boolean isToolbar;

    private List<IWizardPage> dynamicWizardPages;

    private String originaleObjectLabel;

    private String originalVersion;

    private String originalPurpose;

    private String originalDescription;

    private String originalStatus;

    private final WebServiceTosSaveManager manager = WebServiceTosSaveManager.getInstance();

    /**
     * Sets the isToolbar.
     * 
     * @param isToolbar the isToolbar to set
     */
    public void setToolbar(boolean isToolbar) {
        this.isToolbar = isToolbar;
    }

    /**
     * WSDLSchemaWizard constructor comment.
     * 
     * @param workbench
     * @param creation
     * @param selection
     * @param existingNames
     * @param b
     */
    public WSDLSchemaWizard(IWorkbench workbench, boolean creation, ISelection selection, String[] existingNames,
            boolean isSinglePageOnly) {
        super(workbench, creation);
        this.selection = selection;
        this.existingNames = existingNames;
        this.isSinglePageOnly = isSinglePageOnly;
        setNeedsProgressMonitor(true);

        // TODO: should to changed icon.
        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(ECoreImage.DEFAULT_WIZ));

        if (selection == null || existingNames == null) {
            connection = ConnectionFactory.eINSTANCE.createWSDLSchemaConnection();
            connection.setTimeOut(WSDLSchemaStep1Form.TIMEOUT);
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

            connectionItem = PropertiesFactory.eINSTANCE.createWSDLSchemaConnectionItem();
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
            connection = ConnectionFactory.eINSTANCE.createWSDLSchemaConnection();
            connection.setName(ERepositoryObjectType.METADATA_WSDL_SCHEMA.getKey());
            connection.setTimeOut(WSDLSchemaStep1Form.TIMEOUT);
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

            connectionItem = PropertiesFactory.eINSTANCE.createWSDLSchemaConnectionItem();
            connectionItem.setProperty(connectionProperty);
            connectionItem.setConnection(connection);
            break;

        case REPOSITORY_ELEMENT:
            connection = (WSDLSchemaConnection) ((ConnectionItem) node.getObject().getProperty().getItem()).getConnection();
            connectionProperty = node.getObject().getProperty();
            connectionItem = (ConnectionItem) node.getObject().getProperty().getItem();
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            setRepositoryObject(node.getObject());
            isRepositoryObjectEditable();
            initLockStrategy();
            break;
        }
        if (!creation) {
            this.originaleObjectLabel = this.connectionItem.getProperty().getLabel();
            this.originalVersion = this.connectionItem.getProperty().getVersion();
            this.originalDescription = this.connectionItem.getProperty().getDescription();
            this.originalPurpose = this.connectionItem.getProperty().getPurpose();
            this.originalStatus = this.connectionItem.getProperty().getStatusCode();
        }
        initTable();
        initConnection();
    }

    public WSDLSchemaWizard(IWorkbench workbench, boolean creation, RepositoryNode node, String[] existingNames,
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
            connection = ConnectionFactory.eINSTANCE.createWSDLSchemaConnection();
            connection.setName(ERepositoryObjectType.METADATA_WSDL_SCHEMA.getKey());
            connection.setTimeOut(WSDLSchemaStep1Form.TIMEOUT);
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

            connectionItem = PropertiesFactory.eINSTANCE.createWSDLSchemaConnectionItem();
            connectionItem.setProperty(connectionProperty);
            connectionItem.setConnection(connection);
            break;

        case REPOSITORY_ELEMENT:
            connection = (WSDLSchemaConnection) ((ConnectionItem) node.getObject().getProperty().getItem()).getConnection();
            connectionProperty = node.getObject().getProperty();
            connectionItem = (ConnectionItem) node.getObject().getProperty().getItem();
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            setRepositoryObject(node.getObject());
            isRepositoryObjectEditable();
            initLockStrategy();
            break;
        }
        if (!creation) {
            this.originaleObjectLabel = this.connectionItem.getProperty().getLabel();
            this.originalVersion = this.connectionItem.getProperty().getVersion();
            this.originalDescription = this.connectionItem.getProperty().getDescription();
            this.originalPurpose = this.connectionItem.getProperty().getPurpose();
            this.originalStatus = this.connectionItem.getProperty().getStatusCode();
        }

        initTable();
        initConnection();
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
        wsdlSchemaWizardPage0 = null;
        wsdlSchemaWizardPage1 = null;
        wsdlSchemaWizardPage2 = null;

        if (isToolbar) {
            pathToSave = null;
        }
        List<IWizardPage> wizardPages = new ArrayList<IWizardPage>();
        if (creation) {
            setWindowTitle(Messages.getString("WSDLSchemaWizard.CreateNewWSDLSchema"));// Messages.getString( //$NON-NLS-1$
            // "DelimitedFileWizard.windowTitleCreate"
            // ));
            // //$NON-NLS-1$
            // //$NON-NLS-1$

            wsdlSchemaWizardPage0 = new Step0WizardPage(connectionProperty, pathToSave,
                    ERepositoryObjectType.METADATA_WSDL_SCHEMA, !isRepositoryObjectEditable(), creation);
            wsdlSchemaWizardPage0.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 1 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS); //$NON-NLS-1$ //$NON-NLS-2$
            wsdlSchemaWizardPage0.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep0")); //$NON-NLS-1$
            addPage(wsdlSchemaWizardPage0);
            if (PluginChecker.isWebServicePluginLocaed()) {
                wsdlSelectPage = new WSDLSchemaSelectWizardPage(creation, 1, connectionItem, isRepositoryObjectEditable(), null,
                        contextModeManager, isSinglePageOnly);
                wsdlSelectPage
                        .setTitle(Messages.getString("FileWizardPage.titleCreate") + " 2 " + Messages.getString("FileWizardPage.of") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                                + ALL_STEPS);
                wsdlSelectPage.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep0"));
                addPage(wsdlSelectPage);
                wsdlSelectPage.setPageComplete(true);
            } else {
                wsdlSchemaWizardPage1 = new WSDLSchemaWizardPage(creation, 2, connectionItem, isRepositoryObjectEditable(), null,
                        contextModeManager);
                wsdlSchemaWizardPage1.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 2 " //$NON-NLS-1$ //$NON-NLS-2$
                        + Messages.getString("FileWizardPage.of") + " " + TOS_STEP); //$NON-NLS-1$ //$NON-NLS-2$
                wsdlSchemaWizardPage1.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep1")); //$NON-NLS-1$
                addPage(wsdlSchemaWizardPage1);
                wizardPages.add(wsdlSchemaWizardPage1);
                wsdlSchemaWizardPage2 = new WSDLSchemaWizardPage(creation, 3, connectionItem, isRepositoryObjectEditable(), null,
                        contextModeManager);
                wsdlSchemaWizardPage2.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 3 " //$NON-NLS-1$ //$NON-NLS-2$
                        + Messages.getString("FileWizardPage.of") + " " + TOS_STEP); //$NON-NLS-1$ //$NON-NLS-2$
                wsdlSchemaWizardPage2.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep2")); //$NON-NLS-1$
                addPage(wsdlSchemaWizardPage2);
                wizardPages.add(wsdlSchemaWizardPage2);
                wsdlSchemaWizardPage1.setPageComplete(false);
                wsdlSchemaWizardPage2.setPageComplete(isRepositoryObjectEditable());
                setDynamicWizardPages(wizardPages);
            }
            wsdlSchemaWizardPage0.setPageComplete(false);
        } else if (this.isSinglePageOnly == false) {
            setWindowTitle(Messages.getString("WSDLSchemaWizard.UpdateWSDLSchema"));// Messages.getString( //$NON-NLS-1$
            // "DelimitedFileWizard.windowTitleUpdate"
            // ));
            // //$NON-NLS-1$
            // //$NON-NLS-1$

            wsdlSchemaWizardPage0 = new Step0WizardPage(connectionProperty, pathToSave,
                    ERepositoryObjectType.METADATA_WSDL_SCHEMA, !isRepositoryObjectEditable(), creation);

            wsdlSchemaWizardPage0.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 1 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS); //$NON-NLS-1$ //$NON-NLS-2$
            wsdlSchemaWizardPage0.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep0")); //$NON-NLS-1$
            addPage(wsdlSchemaWizardPage0);
            if (PluginChecker.isWebServicePluginLocaed()) {
                wsdlSelectPage = new WSDLSchemaSelectWizardPage(creation, 1, connectionItem, isRepositoryObjectEditable(), null,
                        contextModeManager, isSinglePageOnly);
                wsdlSelectPage
                        .setTitle(Messages.getString("FileWizardPage.titleCreate") + " 2 " + Messages.getString("FileWizardPage.of") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                                + ALL_STEPS);
                wsdlSelectPage.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep0"));
                addPage(wsdlSelectPage);
            } else {
                // wsdlSchemaWizardPage0.setWizard(this);

                // wizardPages.add(wsdlSchemaWizardPage0);
                wsdlSchemaWizardPage1 = new WSDLSchemaWizardPage(2, connectionItem, isRepositoryObjectEditable(), null,
                        contextModeManager);
                wsdlSchemaWizardPage1.setTitle(Messages.getString("FileWizardPage.titleUpdate") + " 2 " //$NON-NLS-1$ //$NON-NLS-2$
                        + Messages.getString("FileWizardPage.of") + " " + TOS_STEP); //$NON-NLS-1$ //$NON-NLS-2$
                wsdlSchemaWizardPage1.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep1")); //$NON-NLS-1$
                addPage(wsdlSchemaWizardPage1);
                wizardPages.add(wsdlSchemaWizardPage1);
                // wizardPages.add(wsdlSchemaWizardPage1);
                wsdlSchemaWizardPage2 = new WSDLSchemaWizardPage(3, connectionItem, isRepositoryObjectEditable(), null,
                        contextModeManager);
                wsdlSchemaWizardPage2.setTitle(Messages.getString("FileWizardPage.titleUpdate") + " 3 " //$NON-NLS-1$ //$NON-NLS-2$
                        + Messages.getString("FileWizardPage.of") + " " + TOS_STEP); //$NON-NLS-1$ //$NON-NLS-2$
                wsdlSchemaWizardPage2.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep2")); //$NON-NLS-1$
                addPage(wsdlSchemaWizardPage2);
                wizardPages.add(wsdlSchemaWizardPage2);
                setDynamicWizardPages(wizardPages);
                // wsdlSchemaWizardPage2.setWizard(this);
                // wizardPages.add(wsdlSchemaWizardPage2);
            }

        } else {
            setWindowTitle(Messages.getString("WSDLSchemaWizard.SaveAsWSDLSchema"));// Messages.getString( //$NON-NLS-1$
            // "DelimitedFileWizard.windowTitleUpdate"
            // ));
            // //$NON-NLS-1$
            // //$NON-NLS-1$
            wsdlSchemaWizardPage0 = new Step0WizardPage(connectionProperty, pathToSave,
                    ERepositoryObjectType.METADATA_WSDL_SCHEMA, !isRepositoryObjectEditable(), true);
            wsdlSchemaWizardPage0.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 1 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS); //$NON-NLS-1$ //$NON-NLS-2$
            wsdlSchemaWizardPage0.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep0")); //$NON-NLS-1$

            addPage(wsdlSchemaWizardPage0);
            if (PluginChecker.isWebServicePluginLocaed()) {
                wsdlSelectPage = new WSDLSchemaSelectWizardPage(creation, 1, connectionItem, isRepositoryObjectEditable(), null,
                        contextModeManager, isSinglePageOnly);
                wsdlSelectPage
                        .setTitle(Messages.getString("FileWizardPage.titleCreate") + " 2 " + Messages.getString("FileWizardPage.of") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                                + " 4");
                wsdlSelectPage.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep0"));
                addPage(wsdlSelectPage);
            } else {
                wsdlSchemaWizardPage1 = new WSDLSchemaWizardPage(1, connectionItem, isRepositoryObjectEditable(), null,
                        contextModeManager);
                wsdlSchemaWizardPage1.setTitle(Messages.getString("FileWizardPage.titleUpdate") + " 2 " //$NON-NLS-1$ //$NON-NLS-2$
                        + Messages.getString("FileWizardPage.of") + " " + TOS_STEP); //$NON-NLS-1$ //$NON-NLS-2$
                wsdlSchemaWizardPage1.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep1")); //$NON-NLS-1$
                addPage(wsdlSchemaWizardPage1);
                wizardPages.add(wsdlSchemaWizardPage1);
                wsdlSchemaWizardPage2 = new WSDLSchemaWizardPage(2, connectionItem, isRepositoryObjectEditable(), null,
                        contextModeManager);
                wsdlSchemaWizardPage2.setTitle(Messages.getString("FileWizardPage.titleUpdate") + " 3 " //$NON-NLS-1$ //$NON-NLS-2$
                        + Messages.getString("FileWizardPage.of") + " " + TOS_STEP); //$NON-NLS-1$ //$NON-NLS-2$
                wsdlSchemaWizardPage2.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep2")); //$NON-NLS-1$
                addPage(wsdlSchemaWizardPage2);
                wizardPages.add(wsdlSchemaWizardPage2);
                setDynamicWizardPages(wizardPages);
            }
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

        boolean formIsPerformed = true;
        IWizardPage finalPage = null;
        if (finalPage == null) {
            finalPage = wsdlSchemaWizardPage0;
        }

        if (((WSDLSchemaConnection) connectionItem.getConnection()).isIsInputModel()) {
            if (finalPage instanceof WSDLSchemaWizardPage) {
                int step = ((WSDLSchemaWizardPage) finalPage).step;
                if (step == 2) {
                    formIsPerformed = finalPage.isPageComplete();
                } else {
                    formIsPerformed = finalPage.isPageComplete();
                }
            } else {
                formIsPerformed = finalPage.isPageComplete();
            }

        } else {
            formIsPerformed = finalPage.isPageComplete();
        }
        manager.saveValue();
        // if (wsdlSchemaWizardPage2 == null) {
        // formIsPerformed = wsdlSchemaWizardPage1.isPageComplete();
        // } else {
        // formIsPerformed = wsdlSchemaWizardPage2.isPageComplete();
        // }

        if (formIsPerformed) {
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            try {
                if (creation) {
                    String nextId = factory.getNextId();
                    connectionProperty.setId(nextId);
                    factory.create(connectionItem, wsdlSchemaWizardPage0.getDestinationPath());
                } else {
                    // update
                    RepositoryUpdateManager.updateWSDLConnection(connectionItem, false, false);
                    refreshInFinish(wsdlSchemaWizardPage0.isNameModifiedByUser());
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
            connectionItem.getProperty().setLabel(this.originaleObjectLabel);
            connectionItem.getProperty().setDisplayName(this.originaleObjectLabel);
            connectionItem.getProperty().setDescription(this.originalDescription);
            connectionItem.getProperty().setPurpose(this.originalPurpose);
            connectionItem.getProperty().setStatusCode(this.originalStatus);
        }
        return super.performCancel();
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

    public List<IWizardPage> getDynamicWizardPages() {
        return this.dynamicWizardPages;
    }

    public void setDynamicWizardPages(List<IWizardPage> dynamicWizardPages) {
        this.dynamicWizardPages = dynamicWizardPages;
    }

    @Override
    public IWizardPage getNextPage(IWizardPage page) {
        IWizardPage nextPage = null;
        if (page instanceof WSDLSchemaWizardPage) {
            if (page instanceof WSDLSchemaSelectWizardPage) {
                nextPage = dynamicWizardPages.get(0);
            } else if (page instanceof WebServiceSchemaWizardPage) {
                int curStep = ((WebServiceSchemaWizardPage) page).step;
                for (IWizardPage curPage : dynamicWizardPages) {
                    if (curPage instanceof WebServiceSchemaWizardPage) {
                        manager.saveValue();
                        if (((WebServiceSchemaWizardPage) curPage).step == curStep + 1) {
                            nextPage = curPage;

                        }
                    }
                }
            } else {
                int curStep = ((WSDLSchemaWizardPage) page).step;
                for (IWizardPage curPage : dynamicWizardPages) {
                    if (curPage instanceof WSDLSchemaWizardPage && !(curPage instanceof WSDLSchemaSelectWizardPage)
                            && !(curPage instanceof WebServiceSchemaWizardPage)) {
                        if (((WSDLSchemaWizardPage) curPage).step == curStep + 1) {
                            nextPage = curPage;

                        }
                    }
                }
            }
        }

        if (nextPage != null) {
            return nextPage;
        }

        return super.getNextPage(page);
    }

    private void warningDialog(String title) {
        MessageBox box = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_WARNING | SWT.OK);
        box.setText("WARNING"); //$NON-NLS-1$
        box.setMessage(title);
        box.open();
    }

    @Override
    public boolean canFinish() {
        boolean flag = ProjectManager.getInstance().isInCurrentMainProject(this.connectionItem);
        if (!flag) {
            return false;
        }

        if (dynamicWizardPages != null) {
            for (IWizardPage wizardPage : dynamicWizardPages) {
                if (!wizardPage.isPageComplete()) {
                    return false;
                }
            }
        }

        return super.canFinish();
    }
}
