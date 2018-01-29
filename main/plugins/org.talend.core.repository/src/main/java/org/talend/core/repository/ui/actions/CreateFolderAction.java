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
package org.talend.core.repository.ui.actions;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.runtime.image.OverlayImageProvider;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.i18n.Messages;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.ui.wizard.folder.FolderWizard;
import org.talend.designer.core.ICamelDesignerCoreService;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.ui.actions.AContextualAction;

/**
 * Action used to create a new folder in repository.<br/>
 * 
 * $Id: CreateFolderAction.java 82396 2012-04-24 09:28:34Z zwzhao $
 * 
 */
public class CreateFolderAction extends AContextualAction {

    public CreateFolderAction() {
        super();
        this.setText(Messages.getString("CreateFolderAction.action.title")); //$NON-NLS-1$
        this.setToolTipText(Messages.getString("CreateFolderAction.action.toolTipText")); //$NON-NLS-1$

        Image folderImg = ImageProvider.getImage(ECoreImage.FOLDER_CLOSE_ICON);
        this.setImageDescriptor(OverlayImageProvider.getImageWithNew(folderImg));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    @Override
    protected void doRun() {
        ISelection selection = getSelection();
        Object obj = ((IStructuredSelection) selection).getFirstElement();
        RepositoryNode node = (RepositoryNode) obj;

        ERepositoryObjectType objectType = null;
        IPath path = null;

        path = RepositoryNodeUtilities.getPath(node);
        if (RepositoryConstants.isSystemFolder(path.toString())) {
            return;
        }
        objectType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);

        if (objectType != null) {
            FolderWizard processWizard = new FolderWizard(path, objectType, null);
            Shell activeShell = Display.getCurrent().getActiveShell();
            WizardDialog dialog = new WizardDialog(activeShell, processWizard);
            dialog.setPageSize(400, 60);
            dialog.create();
            dialog.open();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.ITreeContextualAction#init(org.eclipse.jface.viewers.TreeViewer,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    @Override
    public void init(TreeViewer viewer, IStructuredSelection selection) {
        boolean canWork = !selection.isEmpty() && selection.size() == 1;
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        if (factory.isUserReadOnlyOnCurrentProject()) {
            canWork = false;
        }
        if (canWork) {
            Object o = selection.getFirstElement();
            RepositoryNode node = (RepositoryNode) o;
            Object property = node.getProperties(EProperties.CONTENT_TYPE);
            switch (node.getType()) {
            case REPOSITORY_ELEMENT:
            case STABLE_SYSTEM_FOLDER:
                canWork = false;
                break;
            case SYSTEM_FOLDER:
                if (property == null || property.equals(ERepositoryObjectType.GENERATED)|| property.equals(ERepositoryObjectType.JOBLET_DESIGNS)
                        || property.equals(ERepositoryObjectType.JOB_DOC) || property.equals(ERepositoryObjectType.JOBLET_DOC)
                        || ERepositoryObjectType.SQLPATTERNS.equals(property)
                        || ERepositoryObjectType.REFERENCED_PROJECTS.equals(property)
                        || ERepositoryObjectType.SVN_ROOT.equals(property)) {
                    canWork = false;
                } else if (property != null
                        && GlobalServiceRegister.getDefault().isServiceRegistered(ICamelDesignerCoreService.class)) {
                    ICamelDesignerCoreService camelService = (ICamelDesignerCoreService) GlobalServiceRegister.getDefault()
                            .getService(ICamelDesignerCoreService.class);
                    if (property.equals(camelService.getRouteDocsType())) {
                        canWork = false;
                    }
                }
                break;
            case SIMPLE_FOLDER:
                if (property.equals(ERepositoryObjectType.JOB_DOC) || property.equals(ERepositoryObjectType.JOBLET_DOC)
                        || (ERepositoryObjectType.SQLPATTERNS.equals(property) && !isUnderUserDefined(node))) {
                    canWork = false;
                } else if (property != null
                        && GlobalServiceRegister.getDefault().isServiceRegistered(ICamelDesignerCoreService.class)) {
                    ICamelDesignerCoreService camelService = (ICamelDesignerCoreService) GlobalServiceRegister.getDefault()
                            .getService(ICamelDesignerCoreService.class);
                    if (property.equals(camelService.getRouteDocType())) {
                        canWork = false;
                    }
                }
                if (node.getObject().isDeleted()) {
                    canWork = false;
                }
                break;
            default:
                // Nothing to do
            }

            if (canWork && !ProjectManager.getInstance().isInCurrentMainProject(node)) {
                canWork = false;
            }
        }
        setEnabled(canWork);
    }

}
