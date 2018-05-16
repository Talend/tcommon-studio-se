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
package org.talend.core.repository.ui.actions;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.i18n.Messages;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.ui.editor.RepositoryEditorInput;
import org.talend.core.repository.ui.wizard.folder.FolderWizard;
import org.talend.designer.core.ICamelDesignerCoreService;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.ui.actions.AContextualAction;

/**
 * Action used to create a new folder in repository.<br/>
 * 
 * $Id: CreateFolderAction.java 1100 2006-12-19 14:27:01Z amaumont $
 * 
 */
public class RenameFolderAction extends AContextualAction {

    // MOD yyin 20130128 TDQ-5392, to get the new name of the folder
    private FolderWizard processWizard;

    public RenameFolderAction() {
        super();
        this.setText(Messages.getString("RenameFolderAction.action.title")); //$NON-NLS-1$
        this.setToolTipText(Messages.getString("RenameFolderAction.action.toolTipText")); //$NON-NLS-1$
        this.setImageDescriptor(ImageProvider.getImageDesc(ECoreImage.FOLDER_CLOSE_ICON));
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
        if (obj == null) {
            MessageDialog.openWarning(Display.getCurrent().getActiveShell(),
                    Messages.getString("RenameFolderAction.warning.cannotFind.title"), Messages //$NON-NLS-1$
                            .getString("RenameFolderAction.warning.cannotFind.message")); //$NON-NLS-1$
            return;
        }

        RepositoryNode node = (RepositoryNode) obj;

        // Check if some jobs in the folder are currently opened:
        String firstChildOpen = getFirstOpenedChild(node);
        if (firstChildOpen != null) {
            MessageDialog.openWarning(Display.getCurrent().getActiveShell(),
                    Messages.getString("RenameFolderAction.warning.editorOpen.title"), Messages //$NON-NLS-1$
                            .getString("RenameFolderAction.warning.editorOpen.message", firstChildOpen, //$NON-NLS-1$
                                    getLabelOfNode(node)));
            return;
        }

        ERepositoryObjectType objectType = null;
        IPath path = null;

        path = RepositoryNodeUtilities.getPath(node);
        objectType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);

        openFolderWizard(node, objectType, path);
    }

    // Extracted to get different type of label to show, yyin 2013 TDQ-7143
    protected Object getLabelOfNode(RepositoryNode node) {
        return node.getProperties(EProperties.LABEL);
    }

    protected void openFolderWizard(RepositoryNode node, ERepositoryObjectType objectType, IPath path) {
        if (objectType != null) {
            processWizard = new FolderWizard(path, objectType, node.getObject().getLabel());
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
            Object obj = getLabelOfNode(node);
            switch (node.getType()) {
            case SIMPLE_FOLDER:
                String label = null;
                if (obj instanceof String) {
                    label = (String) obj;
                }
                if (node.getContentType() == ERepositoryObjectType.JOB_DOC
                        || node.getContentType() == ERepositoryObjectType.JOBLET_DOC
                        || (node.getContentType() == ERepositoryObjectType.SQLPATTERNS && !isUnderUserDefined(node))
                        || RepositoryConstants.USER_DEFINED.equals(label)) {
                    canWork = false;
                } else if (node.getContentType() != null
                        && GlobalServiceRegister.getDefault().isServiceRegistered(ICamelDesignerCoreService.class)) {
                    ICamelDesignerCoreService camelService = (ICamelDesignerCoreService) GlobalServiceRegister.getDefault()
                            .getService(ICamelDesignerCoreService.class);
                    if (node.getContentType().equals(camelService.getRouteDocsType())
                            || node.getContentType().equals(camelService.getRouteDocType())) {
                        canWork = false;

                    }
                }
                if (node.getObject() != null && node.getObject().isDeleted()) {
                    canWork = false;
                }
                break;
            default:
                canWork = false;
            }
            if (canWork && !ProjectManager.getInstance().isInCurrentMainProject(node)) {
                canWork = false;
            }
        }
        setEnabled(canWork);
    }

    protected String getFirstOpenedChild(IRepositoryNode node) {
        if (node.hasChildren()) {
            IWorkbenchPage page = getActivePage();
            IEditorReference[] editorReferences = page.getEditorReferences();
            List<String> openEditor = new ArrayList<String>();
            for (IEditorReference tmp : editorReferences) {
                try {
                    IEditorInput editorInput = tmp.getEditorInput();

                    if (editorInput instanceof RepositoryEditorInput) {
                        RepositoryEditorInput rei = (RepositoryEditorInput) editorInput;
                        openEditor.add(rei.getItem().getProperty().getId());
                    }
                } catch (PartInitException e) {
                    ExceptionHandler.process(e, Level.WARN);
                }
            }

            List<IRepositoryNode> children = node.getChildren();
            for (IRepositoryNode currentNode : children) {
                if (currentNode.getType() == ENodeType.REPOSITORY_ELEMENT) {
                    if (openEditor.contains(currentNode.getObject().getId())) {
                        return currentNode.getObject().getLabel();
                    }
                } else if (currentNode.getType() == ENodeType.SIMPLE_FOLDER) {
                    String childOpen = getFirstOpenedChild(currentNode);
                    if (childOpen != null) {
                        return childOpen;
                    }
                }
            }
        }
        return null;
    }

    // Added yyin 20130128 TDQ-5392, to get the new name of the folder
    public String getNewNameOfFolder() {
        if (processWizard != null) {
            return processWizard.getFolderNewName();
        }
        return null;
    }
}
