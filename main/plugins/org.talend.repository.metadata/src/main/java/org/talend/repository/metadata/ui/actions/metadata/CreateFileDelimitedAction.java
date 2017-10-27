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
import org.talend.core.model.properties.DelimitedFileConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.ui.actions.metadata.AbstractCreateAction;
import org.talend.repository.ProjectManager;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.wizards.metadata.connection.files.delimited.DelimitedFileWizard;

/**
 * Action used to create a new "file delimited" metadata.<br/>
 * 
 * $Id: CreateFileDelimitedAction.java 77219 2012-01-24 01:14:15Z mhirt $
 * 
 */
public class CreateFileDelimitedAction extends AbstractCreateAction {

    private static final String EDIT_LABEL = Messages.getString("CreateFileDelimitedAction.action.editTitle"); //$NON-NLS-1$

    private static final String CREATE_LABEL = Messages.getString("CreateFileDelimitedAction.action.createTitle"); //$NON-NLS-1$

    private static final String OPEN_LABEL = Messages.getString("CreateFileDelimitedAction.action.openTitle"); //$NON-NLS-1$

    protected static final int WIZARD_WIDTH = 920;

    protected static final int WIZARD_HEIGHT = 600;

    private boolean creation = false;

    ImageDescriptor defaultImage = ImageProvider.getImageDesc(ECoreImage.METADATA_FILE_DELIMITED_ICON);

    ImageDescriptor createImage = OverlayImageProvider.getImageWithNew(ImageProvider
            .getImage(ECoreImage.METADATA_FILE_DELIMITED_ICON));

    public CreateFileDelimitedAction() {
        super();

        this.setText(CREATE_LABEL);
        this.setToolTipText(CREATE_LABEL);
        this.setImageDescriptor(defaultImage);
    }

    public CreateFileDelimitedAction(boolean isToolbar) {
        super();
        setToolbar(isToolbar);
        this.setText(CREATE_LABEL);
        this.setToolTipText(CREATE_LABEL);
        this.setImageDescriptor(defaultImage);
    }

    @Override
    protected void doRun() {
        // RepositoryNode metadataNode = getViewPart().getRoot().getChildren().get(6);
        // RepositoryNode fileDelimitedNode = metadataNode.getChildren().get(1);
        if (repositoryNode == null) {
            repositoryNode = getCurrentRepositoryNode();
        }

        if (isToolbar()) {
            if (repositoryNode != null && repositoryNode.getContentType() != ERepositoryObjectType.METADATA_FILE_DELIMITED) {
                repositoryNode = null;
            }
            if (repositoryNode == null) {
                repositoryNode = getRepositoryNodeForDefault(ERepositoryObjectType.METADATA_FILE_DELIMITED);
            }

        }

        WizardDialog wizardDialog;
        if (isToolbar()) {
            init(repositoryNode);
            DelimitedFileWizard delimitedFileWizard = new DelimitedFileWizard(PlatformUI.getWorkbench(), creation,
                    repositoryNode, getExistingNames());
            delimitedFileWizard.setToolbar(true);
            wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), delimitedFileWizard);
        } else {
            wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), new DelimitedFileWizard(
                    PlatformUI.getWorkbench(), creation, repositoryNode, getExistingNames()));
        }

        wizardDialog.setPageSize(WIZARD_WIDTH, WIZARD_HEIGHT);
        wizardDialog.create();
        wizardDialog.open();
    }

    @Override
    protected void init(RepositoryNode node) {
        ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
        if (ERepositoryObjectType.METADATA_FILE_DELIMITED != null
                && !ERepositoryObjectType.METADATA_FILE_DELIMITED.equals(nodeType)) {
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
            this.setText(CREATE_LABEL);
            collectChildNames(node);
            this.setImageDescriptor(createImage);
            creation = true;
            break;
        case REPOSITORY_ELEMENT:
            if (factory.isPotentiallyEditable(node.getObject())) {
                this.setText(EDIT_LABEL);
                this.setImageDescriptor(defaultImage);
                collectSiblingNames(node);
            } else {
                this.setText(OPEN_LABEL);
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
        return DelimitedFileConnectionItem.class;
    }

}
