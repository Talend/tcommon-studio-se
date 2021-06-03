// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.runtime.image.OverlayImageProvider;
import org.talend.commons.ui.swt.actions.ITreeContextualAction;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.properties.SalesforceSchemaConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.ui.actions.metadata.AbstractCreateAction;
import org.talend.core.runtime.services.IGenericWizardService;
import org.talend.repository.ProjectManager;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC yexiaowei class global comment. Detailled comment
 */

public class CreateSalesforceSchemaAction extends AbstractCreateAction {

    private static final String CREATE_LABEL = Messages.getString("CreateSalesforceSchemaAction.createConnection"); //$NON-NLS-1$

    ImageDescriptor defaultImage = ImageProvider.getImageDesc(ECoreImage.METADATA_SALESFORCE_SCHEMA_ICON);

    ImageDescriptor createImage = OverlayImageProvider.getImageWithNew(ImageProvider
            .getImage(ECoreImage.METADATA_SALESFORCE_SCHEMA_ICON));

    private AbstractCreateAction createAction;

    public CreateSalesforceSchemaAction() {
        super();

        this.setText(CREATE_LABEL);
        this.setToolTipText(CREATE_LABEL);
        this.setImageDescriptor(defaultImage);

    }

    public CreateSalesforceSchemaAction(boolean isToolbar) {
        super();
        setToolbar(isToolbar);
        this.setText(CREATE_LABEL);
        this.setToolTipText(CREATE_LABEL);
        this.setImageDescriptor(defaultImage);
    }

    @Override
    protected void doRun() {
        if (repositoryNode == null) {
            repositoryNode = getCurrentRepositoryNode();
        }

        if (isToolbar()) {
            ERepositoryObjectType salesforceType = ERepositoryObjectType.getType("salesforce");
            if (repositoryNode != null && repositoryNode.getContentType() != salesforceType) {
                repositoryNode = null;
            }
            if (repositoryNode == null || (repositoryNode.getType() != ENodeType.SIMPLE_FOLDER
                    && repositoryNode.getType() != ENodeType.SYSTEM_FOLDER)) {
                repositoryNode = getRepositoryNodeForDefault(salesforceType);
            }

            init(repositoryNode);

            ITreeContextualAction defaultAction = getGenericAction(repositoryNode);
            if (defaultAction instanceof AbstractCreateAction) {
                createAction = (AbstractCreateAction) defaultAction;
                createAction.setCurrentRepositoryNode(repositoryNode);
                createAction.init(null, new StructuredSelection(repositoryNode));
                createAction.run();
            }
        }
    }

    private ITreeContextualAction getGenericAction(RepositoryNode repositoryNode) {
        IGenericWizardService wizardService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IGenericWizardService.class)) {
            wizardService = (IGenericWizardService) GlobalServiceRegister.getDefault().getService(IGenericWizardService.class);
        }
        ITreeContextualAction defaultAction = null;
        if (wizardService != null) {
            ERepositoryObjectType repObjType = (ERepositoryObjectType) repositoryNode.getProperties(EProperties.CONTENT_TYPE);
            defaultAction = wizardService.getGenericAction(repObjType.getType(), null);
        }
        return defaultAction;
    }

    @Override
    protected void init(RepositoryNode node) {
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        switch (node.getType()) {
        case SIMPLE_FOLDER:
            if (node.getObject() != null && node.getObject().getProperty().getItem().getState().isDeleted()) {
                setEnabled(false);
                return;
            }
        case SYSTEM_FOLDER:
            if (factory.isUserReadOnlyOnCurrentProject() || !ProjectManager.getInstance().isInCurrentMainProject(node)) {
                setEnabled(false);
                return;
            }
            this.setText(CREATE_LABEL);
            collectChildNames(node);
            this.setImageDescriptor(createImage);
            break;
        default:
            return;
        }
        setEnabled(true);
    }

    @Override
    public Class getClassForDoubleClick() {
        return SalesforceSchemaConnectionItem.class;
    }
}
