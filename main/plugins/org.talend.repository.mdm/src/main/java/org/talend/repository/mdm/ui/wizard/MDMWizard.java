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
package org.talend.repository.mdm.ui.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ITDQRepositoryService;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.MDMConnectionItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.update.RepositoryUpdateManager;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;
import org.talend.metadata.managment.ui.wizard.PropertiesWizardPage;
import org.talend.metadata.managment.ui.wizard.RepositoryWizard;
import org.talend.metadata.managment.utils.MetadataConnectionUtils;
import org.talend.repository.mdm.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;

/**
 * DOC hwang class global comment. Detailled comment
 */
public class MDMWizard extends RepositoryWizard implements INewWizard {

    private PropertiesWizardPage propertiesWizardPage;

    private MDMWizardPage mdmWizardPage;

    private UniversePage universePage;

    private MDMConnection connection;

    private Property connectionProperty;

    private ConnectionItem connectionItem;

    private boolean isToolBar;

    private final RepositoryNode node;

    private WizardPage currentPage;

    private String originaleObjectLabel;

    private String originalVersion;

    private String originalPurpose;

    private String originalDescription;

    private String originalStatus;

    private IMetadataConnection iMetadataConn;

    /**
     * DOC hwang MDMWizard constructor comment.
     * 
     * @param workbench
     * @param creation
     */
    public MDMWizard(IWorkbench workbench, boolean creation, RepositoryNode node, String[] existingNames) {
        super(workbench, creation);
        this.existingNames = existingNames;
        this.node = node;
        setNeedsProgressMonitor(true);
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
            connection = ConnectionFactory.eINSTANCE.createMDMConnection();
            connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
            connectionProperty.setAuthor(((RepositoryContext) CoreRuntimePlugin.getInstance().getContext()
                    .getProperty(Context.REPOSITORY_CONTEXT_KEY)).getUser());
            connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
            connectionProperty.setStatusCode(""); //$NON-NLS-1$

            connectionItem = PropertiesFactory.eINSTANCE.createMDMConnectionItem();
            connectionItem.setProperty(connectionProperty);
            connectionItem.setConnection(connection);
            break;

        case REPOSITORY_ELEMENT:
            connection = (MDMConnection) ((ConnectionItem) node.getObject().getProperty().getItem()).getConnection();
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
            this.iMetadataConn = ConvertionHelper.convert(((MDMConnectionItem) this.connectionItem).getConnection());
        }
        // initialize the context mode
        ConnectionContextHelper.checkContextMode(connectionItem);
    }

    public MDMWizard(IWorkbench workbench, boolean creation, ISelection selection, String[] existingNames) {
        super(workbench, creation);
        this.selection = selection;
        this.existingNames = existingNames;
        setNeedsProgressMonitor(true);

        Object obj = ((IStructuredSelection) selection).getFirstElement();
        node = (RepositoryNode) obj;
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
            connection = ConnectionFactory.eINSTANCE.createMDMConnection();
            connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
            connectionProperty.setAuthor(((RepositoryContext) CoreRuntimePlugin.getInstance().getContext()
                    .getProperty(Context.REPOSITORY_CONTEXT_KEY)).getUser());
            connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
            connectionProperty.setStatusCode(""); //$NON-NLS-1$

            connectionItem = PropertiesFactory.eINSTANCE.createMDMConnectionItem();
            connectionItem.setProperty(connectionProperty);
            connectionItem.setConnection(connection);
            break;

        case REPOSITORY_ELEMENT:
            connection = (MDMConnection) ((ConnectionItem) node.getObject().getProperty().getItem()).getConnection();
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
            this.iMetadataConn = ConvertionHelper.convert(((MDMConnectionItem) this.connectionItem).getConnection());
        }
        // initialize the context mode
        ConnectionContextHelper.checkContextMode(connectionItem);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        // if (mdmWizardPage.isPageComplete()) {
        final IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

        ITDQRepositoryService tdqRepService = null;

        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITDQRepositoryService.class)) {
            tdqRepService = (ITDQRepositoryService) GlobalServiceRegister.getDefault().getService(ITDQRepositoryService.class);
        }

        try {
            if (creation) {
                String nextId = factory.getNextId();
                connectionProperty.setId(nextId);
                factory.create(connectionItem, propertiesWizardPage.getDestinationPath());

                // feature 17159
                if (tdqRepService != null) {
                    tdqRepService.fillMetadata(connectionItem);
                }
            } else {
                connectionItem.getConnection().setLabel(connectionProperty.getDisplayName());
                connectionItem.getConnection().setName(connectionProperty.getDisplayName());
                RepositoryUpdateManager.updateFileConnection(connectionItem);
                // factory.save(connectionItem);
                boolean isModified = propertiesWizardPage.isNameModifiedByUser();
                if (isModified) {
                    if (GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerCoreService.class)) {
                        IDesignerCoreService service = (IDesignerCoreService) GlobalServiceRegister.getDefault().getService(
                                IDesignerCoreService.class);
                        if (service != null) {
                            service.refreshComponentView(connectionItem);
                        }
                    }
                }
                boolean isNameModified = propertiesWizardPage.isNameModifiedByUser();
                if (isNameModified && tdqRepService != null) {
                    tdqRepService.saveConnectionWithDependency(connectionItem);
                    closeLockStrategy();
                } else {
                    IWorkspace workspace = ResourcesPlugin.getWorkspace();
                    IWorkspaceRunnable operation = new IWorkspaceRunnable() {

                        @Override
                        public void run(IProgressMonitor monitor) throws CoreException {
                            try {
                                factory.save(connectionItem);
                                closeLockStrategy();
                            } catch (PersistenceException e) {
                                throw new CoreException(new Status(IStatus.ERROR, "", "", e));
                            }
                        }
                    };
                    workspace.run(operation, null);
                }
            }
        } catch (Exception e) {
            String detailError = e.toString();
            new ErrorDialogWidthDetailArea(getShell(), PID, "", //$NON-NLS-1$
                    detailError);
            //                log.error(Messages.getString("CommonWizard.persistenceException") + "\n" + detailError); //$NON-NLS-1$ //$NON-NLS-2$
            return false;
        }
        List<IRepositoryViewObject> list = new ArrayList<IRepositoryViewObject>();
        list.add(repositoryObject);

        if (tdqRepService != null) {
            // MOD qiongli 2012-11-23 TDQ-6287
            if (creation) {
                tdqRepService.openConnectionEditor(connectionItem);
            } else {
                // refresh the opened connection editor whatever is in DI or DQ perspective.
                tdqRepService.refreshConnectionEditor(connectionItem);
            }
            if (CoreRuntimePlugin.getInstance().isDataProfilePerspectiveSelected()) {
                tdqRepService.refresh(node.getParent());
            }
        }

        return true;
    }

    @Override
    public boolean performCancel() {
        if (!creation) {
            connectionItem.getProperty().setVersion(this.originalVersion);
            connectionItem.getProperty().setDisplayName(this.originaleObjectLabel);
            connectionItem.getProperty().setDescription(this.originalDescription);
            connectionItem.getProperty().setPurpose(this.originalPurpose);
            connectionItem.getProperty().setStatusCode(this.originalStatus);
            MetadataConnectionUtils.setMDMConnectionParameter(connection, iMetadataConn);
        }
        return super.performCancel();
    }

    @Override
    public void createPageControls(Composite pageContainer) {
        // do nothing to create pages lazily
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addPages() {
        setWindowTitle(Messages.getString("MDMWizardConnection"));//$NON-NLS-1$
        // setDefaultPageImageDescriptor(ImageProvider.getImageDesc(ECoreImage.METADATA_MDM_CONNECTION_WIZ));
        if (isToolBar) {
            pathToSave = null;
        }
        propertiesWizardPage = new MdmStep0WizardPage(connectionProperty, pathToSave,
                ERepositoryObjectType.METADATA_MDMCONNECTION, !isRepositoryObjectEditable(), creation);
        propertiesWizardPage.setTitle("Talend MDM"); //$NON-NLS-1$
        propertiesWizardPage.setDescription(Messages.getString("MDMWizard_create_mdm_conn")); //$NON-NLS-1$
        mdmWizardPage = new MDMWizardPage(connectionItem, isRepositoryObjectEditable(), creation, existingNames);
        universePage = new UniversePage(connectionItem, "Talend MDM"); //$NON-NLS-1$
        addPage(propertiesWizardPage);
        addPage(mdmWizardPage);
        addPage(universePage);
    }

    public void setToolBar(boolean isToolbar) {
        this.isToolBar = isToolbar;
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

    @Override
    public boolean canFinish() {
        if (currentPage instanceof UniversePage && currentPage.isPageComplete()) {
            return true;
        }
        return false;
    }

    public void setCurrentPage(WizardPage currentPage) {
        this.currentPage = currentPage;
    }

    @Override
    /**
     * TDQ-7217 if the related connection editor is opened in DQ side,shoud not unlock.
     */
    public void closeLockStrategy() {
        ITDQRepositoryService tdqRepService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITDQRepositoryService.class)) {
            tdqRepService = (ITDQRepositoryService) GlobalServiceRegister.getDefault().getService(ITDQRepositoryService.class);
        }
        if (tdqRepService != null && tdqRepService.isDQEditorOpened(connectionItem)) {
            tdqRepService.refreshConnectionEditor(connectionItem);
            return;
        }
        super.closeLockStrategy();
    }
}
