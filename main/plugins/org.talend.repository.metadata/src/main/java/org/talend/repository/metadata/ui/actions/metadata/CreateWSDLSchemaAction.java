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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.runtime.image.OverlayImageProvider;
import org.talend.core.model.properties.WSDLSchemaConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.ui.actions.metadata.AbstractCreateAction;
import org.talend.repository.ProjectManager;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.wizards.metadata.connection.wsdl.WSDLSchemaWizard;

/**
 * DOC qwei class global comment. Detailled comment
 */
public class CreateWSDLSchemaAction extends AbstractCreateAction {

    private final String createLabel;

    private final ImageDescriptor defaultImage, createImage;

    private final ERepositoryObjectType currentNodeType;

    private boolean creation = false;

    private final String openLabel;

    private final String editLabel;

    protected static final int WIZARD_WIDTH = 800;

    protected static final int WIZARD_HEIGHT = 540;

    public CreateWSDLSchemaAction() {
        super();

        // TODO: should to fix the I18N issue.
        createLabel = Messages.getString("CreateWSDLSchemaAction.createWSDL"); //$NON-NLS-1$
        editLabel = Messages.getString("CreateWSDLSchemaAction.editWSDL"); //$NON-NLS-1$
        openLabel = Messages.getString("CreateWSDLSchemaAction.openWSDL"); //$NON-NLS-1$
        // TODO: should change to another icon.
        defaultImage = ImageProvider.getImageDesc(ECoreImage.METADATA_WSDL_SCHEMA_ICON);
        createImage = OverlayImageProvider.getImageWithNew(ImageProvider.getImage(ECoreImage.METADATA_WSDL_SCHEMA_ICON));

        setText(createLabel);
        setToolTipText(createLabel);
        setImageDescriptor(defaultImage);

        currentNodeType = ERepositoryObjectType.METADATA_WSDL_SCHEMA;
    }

    public CreateWSDLSchemaAction(boolean isToolbar) {
        this();
        setToolbar(isToolbar);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.ui.actions.metadata.AbstractCreateAction#init(org.talend.repository.model.RepositoryNode)
     */
    @Override
    protected void init(RepositoryNode node) {
        ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
        if (!currentNodeType.equals(nodeType)) {
            return;
        }

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
            this.setText(createLabel);
            collectChildNames(node);
            this.setImageDescriptor(createImage);
            creation = true;
            break;
        case REPOSITORY_ELEMENT:
            if (factory.isPotentiallyEditable(node.getObject())) {
                this.setText(editLabel);
                this.setImageDescriptor(defaultImage);
                collectSiblingNames(node);
            } else {
                this.setText(openLabel);
                this.setImageDescriptor(defaultImage);
            }
            collectSiblingNames(node);
            creation = false;
            break;
        default:
            return;
        }
        setEnabled(true);
    }

    @Override
    public Class getClassForDoubleClick() {
        return WSDLSchemaConnectionItem.class;
    }

    @Override
    protected void doRun() {
        if (repositoryNode == null) {
            repositoryNode = getCurrentRepositoryNode();
        }
        if (isToolbar()) {
            if (repositoryNode != null && repositoryNode.getContentType() != currentNodeType) {
                repositoryNode = null;
            }
            if (repositoryNode == null) {
                repositoryNode = getRepositoryNodeForDefault(currentNodeType);
            }
        }

        WizardDialog wizardDialog;
        if (isToolbar()) {
            init(repositoryNode);
            WSDLSchemaWizard wsdlSchemaWizard = new WSDLSchemaWizard(PlatformUI.getWorkbench(), creation, repositoryNode,
                    getExistingNames(), false);
            wsdlSchemaWizard.setToolbar(true);
            wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), wsdlSchemaWizard);
        } else {
            // selection = getSelection();
            wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), new WSDLSchemaWizard(
                    PlatformUI.getWorkbench(), creation, repositoryNode, getExistingNames(), false));
        }

        wizardDialog.setPageSize(WIZARD_WIDTH, WIZARD_HEIGHT);
        wizardDialog.create();

        wizardDialog.open();

    }
}
