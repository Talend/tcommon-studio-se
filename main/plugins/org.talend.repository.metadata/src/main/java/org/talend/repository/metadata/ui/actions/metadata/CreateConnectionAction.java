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

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.runtime.image.OverlayImageProvider;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.RepositoryObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.ui.actions.metadata.AbstractCreateAction;
import org.talend.core.runtime.services.IGenericDBService;
import org.talend.repository.ProjectManager;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.ui.views.IRepositoryView;
import org.talend.repository.ui.wizards.metadata.connection.database.DatabaseWizard;

/**
 * Action used to create a new connection.<br/>
 * 
 * $Id: CreateConnectionAction.java 77219 2012-01-24 01:14:15Z mhirt $
 * 
 */
public class CreateConnectionAction extends AbstractCreateAction {

    protected static Logger log = Logger.getLogger(CreateConnectionAction.class);

    protected static final String PID = "org.talend.repository"; //$NON-NLS-1$

    private static final String EDIT_LABEL = Messages.getString("CreateConnectionAction.action.editTitle"); //$NON-NLS-1$

    private static final String OPEN_LABEL = Messages.getString("CreateConnectionAction.action.openTitle"); //$NON-NLS-1$

    private static final String CREATE_LABEL = Messages.getString("CreateConnectionAction.action.createTitle"); //$NON-NLS-1$

    ImageDescriptor defaultImage = ImageProvider.getImageDesc(ECoreImage.METADATA_CONNECTION_ICON);

    ImageDescriptor createImage = OverlayImageProvider.getImageWithNew(ImageProvider
            .getImage(ECoreImage.METADATA_CONNECTION_ICON));

    private ConnectionItem connItem;

    public CreateConnectionAction() {
        super();

        this.setText(CREATE_LABEL);
        this.setToolTipText(CREATE_LABEL);
        this.setImageDescriptor(defaultImage);
    }

    public CreateConnectionAction(boolean isToolbar) {
        super();
        setToolbar(isToolbar);
        this.setText(CREATE_LABEL);
        this.setToolTipText(CREATE_LABEL);
        this.setImageDescriptor(defaultImage);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void doRun() {
        // RepositoryNode metadataNode = getViewPart().getRoot().getChildren().get(6);
        // RepositoryNode dbConnectionNode = metadataNode.getChildren().get(0);
        if (this.repositoryNode == null) {
            repositoryNode = getCurrentRepositoryNode();
        }
        if (isToolbar()) {
            if (repositoryNode != null && repositoryNode.getContentType() != ERepositoryObjectType.METADATA_CONNECTIONS) {
                repositoryNode = null;
            }
            if (repositoryNode == null) {
                repositoryNode = getRepositoryNodeForDefault(ERepositoryObjectType.METADATA_CONNECTIONS);
            }
        }
        // TDI-21143 : Studio repository view : remove all refresh call to repo view
        // IRepositoryView view = getViewPart();
        // if (view != null) {
        // view.refresh();
        // }
        RepositoryNode metadataNode = repositoryNode.getParent();
        if (metadataNode != null) {
            // Force focus to the repositoryView and open Metadata and DbConnection nodes
            IRepositoryView viewPart = getViewPart();
            if (viewPart != null) {
                viewPart.setFocus();
                viewPart.expand(metadataNode, true);
                viewPart.expand(repositoryNode, true);
            }
        }

        Connection connection = null;
        IPath pathToSave = null;

        // 16670
        if (repositoryNode.getObject() != null && repositoryNode.getObject().getClass().equals(RepositoryObject.class)) {
            try {
                ((RepositoryObject) repositoryNode.getObject()).setProperty(ProxyRepositoryFactory.getInstance()
                        .getUptodateProperty(repositoryNode.getObject().getProperty()));
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }
        }

        // Define the RepositoryNode, by default Metadata/DbConnection
        RepositoryNode node = repositoryNode;
        // When the userSelection is an element of metadataNode, use it !
        // if (!isToolbar()) {
        // Object userSelection = ((IStructuredSelection) getSelection()).getFirstElement();
        // if (userSelection instanceof RepositoryNode) {
        // switch (((RepositoryNode) userSelection).getType()) {
        // case REPOSITORY_ELEMENT:
        // case SIMPLE_FOLDER:
        // case SYSTEM_FOLDER:
        // node = (RepositoryNode) userSelection;
        // break;
        // }
        // }
        // }
        boolean creation = false;
        // Define the repositoryObject DatabaseConnection and his pathToSave
        switch (node.getType()) {
        case REPOSITORY_ELEMENT:
            // pathToSave = null;
            connection = ((ConnectionItem) node.getObject().getProperty().getItem()).getConnection();
            creation = false;
            break;
        case SIMPLE_FOLDER:
            pathToSave = RepositoryNodeUtilities.getPath(node);
            connection = ConnectionFactory.eINSTANCE.createDatabaseConnection();
            creation = true;
            break;
        case SYSTEM_FOLDER:
            pathToSave = new Path(""); //$NON-NLS-1$
            connection = ConnectionFactory.eINSTANCE.createDatabaseConnection();
            creation = true;
            break;
        }

        if (!creation) {
            Property property = node.getObject().getProperty();
            Property updatedProperty = null;
            try {
                updatedProperty = ProxyRepositoryFactory.getInstance().getUptodateProperty(
                        new Project(ProjectManager.getInstance().getProject(property.getItem())), property);
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }

        }

        DatabaseWizard databaseWizard;
        if (isToolbar()) {
            databaseWizard = new DatabaseWizard(PlatformUI.getWorkbench(), creation, node, getExistingNames());
            databaseWizard.setToolBar(true);
        } else {
            databaseWizard = new DatabaseWizard(PlatformUI.getWorkbench(), creation, node, getExistingNames());
        }

        // Open the Wizard
        WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), databaseWizard);
        wizardDialog.setPageSize(780, 540);
        wizardDialog.create();
        wizardDialog.open();
        connItem = databaseWizard.getConnectionItem();

    }

    @Override
    protected void init(RepositoryNode node) {
        ERepositoryObjectType nodeType = node.getObjectType();
        if(nodeType == null || node.getType() != ENodeType.REPOSITORY_ELEMENT){
            nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
        }
        boolean isExtraType = false;
        IGenericDBService dbService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IGenericDBService.class)) {
            dbService = (IGenericDBService) GlobalServiceRegister.getDefault().getService(
                    IGenericDBService.class);
        }
        if(dbService != null){
            isExtraType = dbService.getExtraTypes().contains(nodeType);
        }
        if (!ERepositoryObjectType.METADATA_CONNECTIONS.equals(nodeType) && !isExtraType) {
            setEnabled(false);
            return;
        }
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        switch (node.getType()) {
        case SIMPLE_FOLDER:
        case SYSTEM_FOLDER:
            if (factory.isUserReadOnlyOnCurrentProject() || !ProjectManager.getInstance().isInCurrentMainProject(node)) {
                setEnabled(false);
                return;
            }
            if (node.getObject() != null && node.getObject().getProperty().getItem().getState().isDeleted()) {
                setEnabled(false);
                return;
            }
            this.setText(CREATE_LABEL);
            this.setImageDescriptor(createImage);
            collectChildNames(node);
            break;
        case REPOSITORY_ELEMENT:
            if (factory.isPotentiallyEditable(node.getObject()) && isLastVersion(node)) {
                this.setText(EDIT_LABEL);
                this.setImageDescriptor(defaultImage);
                collectSiblingNames(node);
            } else {
                this.setText(OPEN_LABEL);
                this.setImageDescriptor(defaultImage);
            }
            break;
        default:
            return;
        }
        setEnabled(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.AContextualView#getClassForDoubleClick()
     */
    @Override
    public Class getClassForDoubleClick() {
        return DatabaseConnectionItem.class;
    }

    public ConnectionItem getConnectionItem() {
        return connItem;
    }
}
