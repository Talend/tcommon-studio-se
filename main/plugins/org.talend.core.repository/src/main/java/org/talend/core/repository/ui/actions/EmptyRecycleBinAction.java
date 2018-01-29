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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.exception.MessageBoxExceptionHandler;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ITDQRepositoryService;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryContentHandler;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.ISubRepositoryObject;
import org.talend.core.model.repository.RepositoryContentManager;
import org.talend.core.repository.i18n.Messages;
import org.talend.core.repository.link.IRepoViewLinker;
import org.talend.core.repository.link.RepoViewLinkerRegistryReader;
import org.talend.core.repository.model.ItemReferenceBean;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.ui.dialog.ItemReferenceDialog;
import org.talend.core.repository.utils.AbstractResourceChangesService;
import org.talend.core.repository.utils.RepositoryNodeDeleteManager;
import org.talend.core.repository.utils.TDQServiceRegister;
import org.talend.designer.core.ICamelDesignerCoreService;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.ui.actions.AContextualAction;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * Action used to empty the recycle bin.<br/>
 * 
 * $Id$
 * 
 */
public class EmptyRecycleBinAction extends AContextualAction {

    private boolean forceBuild = false;

    public EmptyRecycleBinAction() {
        super();
        this.setText(Messages.getString("EmptyRecycleBinAction.action.title")); //$NON-NLS-1$
        this.setToolTipText(Messages.getString("EmptyRecycleBinAction.action.toolTipText")); //$NON-NLS-1$
        this.setImageDescriptor(ImageProvider.getImageDesc(ECoreImage.RECYCLE_BIN_EMPTY_ICON));
    }

    @Override
    protected void doRun() {
        ISelection selection = getSelection();
        Object obj = ((IStructuredSelection) selection).getFirstElement();
        final RepositoryNode node = (RepositoryNode) obj;

        final String title = Messages.getString("EmptyRecycleBinAction.dialog.title"); //$NON-NLS-1$
        String message = null;
        // TDI-20542
        List<IRepositoryNode> originalChildren = node.getChildren();
        List<IRepositoryNode> childrenNeedToDelete = new ArrayList<IRepositoryNode>(originalChildren);

        // MOD qiongli 2012-11-23 TUP-273 if a connection in recycle bin which depended by DQ analysis,should give a
        // warning then return.
        if (childrenNeedToDelete.size() == 0) {
            return;
        }
        AbstractResourceChangesService resChangeService = TDQServiceRegister.getInstance().getResourceChangeService(
                AbstractResourceChangesService.class);
        if (resChangeService != null) {
            List<IRepositoryNode> dependentNodes = resChangeService.getDependentConnNodesInRecycleBin(childrenNeedToDelete);
            if (dependentNodes != null && !dependentNodes.isEmpty()) {
                resChangeService.openDependcesDialog(dependentNodes);
                return;
            }
        }
        if (childrenNeedToDelete.size() > 1) {
            message = Messages.getString("DeleteAction.dialog.messageAllElements") + "\n" + //$NON-NLS-1$ //$NON-NLS-2$
                    Messages.getString("DeleteAction.dialog.message2"); //$NON-NLS-1$;
        } else {
            message = Messages.getString("DeleteAction.dialog.message1") + "\n" //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("DeleteAction.dialog.message2"); //$NON-NLS-1$
        }

        final Shell shell = getShell();
        if (!(MessageDialog.openQuestion(shell, title, message))) {
            DeleteActionCache.getInstance().revertParameters();
            return;
        }

        final List<ItemReferenceBean> unDeleteItems = RepositoryNodeDeleteManager.getInstance()
                .getUnDeleteItems(childrenNeedToDelete, null);

        if (unDeleteItems.size() > 0) {
            final boolean isForceDelete[] = new boolean[1];
            Display.getDefault().syncExec(new Runnable() {

                @Override
                public void run() {
                    ItemReferenceDialog dialog = new ItemReferenceDialog(
                            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), unDeleteItems);
                    dialog.showForceDeleteButton(true);
                    int userChoice = dialog.open();
                    if (ItemReferenceDialog.FORCE_DELETE_ID == userChoice) {
                        isForceDelete[0] = true;
                    } else {
                        isForceDelete[0] = false;
                    }
                }
            });
            if (isForceDelete[0]) {
                childrenNeedToDelete = originalChildren;
            }
        }

        final List<IRepositoryNode> children = new ArrayList<IRepositoryNode>(childrenNeedToDelete);

        // TDQ-5359
        for (IRepositoryNode child : children) {
            // MOD klliu 2011-04-28 bug 20204 removing connection is synced to the connection view of SQL
            // explore
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ITDQRepositoryService.class)) {
                ITDQRepositoryService tdqRepService = (ITDQRepositoryService) GlobalServiceRegister.getDefault().getService(
                        ITDQRepositoryService.class);
                if (!tdqRepService.removeAliasInSQLExplorer(child)) {
                    MessageDialog.openWarning(shell, title, Messages.getString("EmptyRecycleBinAction.dialog.allDependencies"));
                    try {
                        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
                        factory.saveProject(ProjectManager.getInstance().getCurrentProject());
                    } catch (PersistenceException e) {
                        ExceptionHandler.process(e);
                    }
                    return;
                }
            }
        }

        final IWorkspaceRunnable op = new IWorkspaceRunnable() {

            @Override
            public void run(IProgressMonitor monitor) {
                IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
                for (IRepositoryNode child : children) {
                    try {
                        deleteElements(factory, (RepositoryNode) child);
                    } catch (Exception e) {
                        MessageBoxExceptionHandler.process(e);
                    }
                }
                try {
                    factory.saveProject(ProjectManager.getInstance().getCurrentProject());
                } catch (PersistenceException e) {
                    ExceptionHandler.process(e);
                }
            }
        };

        IRunnableWithProgress iRunnableWithProgress = new IRunnableWithProgress() {

            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                IWorkspace workspace = ResourcesPlugin.getWorkspace();
                try {
                    ISchedulingRule schedulingRule = workspace.getRoot();
                    // the update the project files need to be done in the workspace runnable to avoid all
                    // notification
                    // of changes before the end of the modifications.
                    workspace.run(op, schedulingRule, IWorkspace.AVOID_UPDATE, monitor);
                } catch (CoreException e) {
                    throw new InvocationTargetException(e);
                }

            }
        };
        try {
            PlatformUI.getWorkbench().getProgressService().run(false, true, iRunnableWithProgress);
            // fix for TDI-22986 , force build the .java if routine is deleted physical
            if (forceBuild) {
                IRunProcessService service = (IRunProcessService) GlobalServiceRegister.getDefault().getService(
                        IRunProcessService.class);
                service.buildJavaProject();
            }

        } catch (Exception e) {
            ExceptionHandler.process(e);
        }

        DeleteActionCache.getInstance().revertParameters();
    }

    protected Shell getShell() {
        Shell shell = null;

        IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (activeWorkbenchWindow != null) {
            shell = activeWorkbenchWindow.getShell();
        }
        if (shell == null) {
            Display dis = Display.getCurrent();
            if (dis == null) {
                dis = Display.getDefault();
            }
            if (dis != null) {
                shell = dis.getActiveShell();
            }
        }
        if (shell == null) {
            shell = new Shell();
        }
        return shell;
    }

    protected void deleteElements(final IProxyRepositoryFactory factory, final RepositoryNode currentNode)
            throws PersistenceException, BusinessException {
        if (!validElement(currentNode)) {
            return;
        }
        final IRepositoryViewObject objToDelete = currentNode.getObject();
        if (objToDelete == null) {
            return;
        }
        if (objToDelete instanceof ISubRepositoryObject) {
            ISubRepositoryObject subRepositoryObject = (ISubRepositoryObject) objToDelete;
            if (!isRootNodeDeleted(currentNode)) {
                Item item = subRepositoryObject.getProperty().getItem();
                subRepositoryObject.removeFromParent();
                if (item instanceof ConnectionItem) {
                    ProxyRepositoryFactory.getInstance().setSubItemDeleted((ConnectionItem) item,
                            subRepositoryObject.getAbstractMetadataObject(), false);
                }
                factory.save(item);
            }
        } else {

            Display.getDefault().syncExec(new Runnable() {

                @Override
                public void run() {
                    try {
                        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                        for (IEditorReference editors : page.getEditorReferences()) {
                            IEditorInput nameInEditor = editors.getEditorInput();
                            if (isRelation(nameInEditor, objToDelete.getId())) {
                                page.closeEditor(editors.getEditor(false), false);
                            }
                        }
                        if (objToDelete.getRepositoryObjectType() != ERepositoryObjectType.JOB_DOC
                                && objToDelete.getRepositoryObjectType() != ERepositoryObjectType.JOBLET_DOC) {
                            if (currentNode.getType() == ENodeType.SIMPLE_FOLDER) {
                                for (IRepositoryNode curNode : currentNode.getChildren()) {
                                    deleteElements(factory, (RepositoryNode) curNode);

                                }
                                factory.deleteFolder(ProjectManager.getInstance().getCurrentProject(),
                                        currentNode.getContentType(),
                                        RepositoryNodeUtilities.getFolderPath(currentNode.getObject().getProperty().getItem()),
                                        true);
                            } else {
                                // Handle nodes from extension point.
                                for (IRepositoryContentHandler handler : RepositoryContentManager.getHandlers()) {
                                    handler.deleteNode(objToDelete);
                                }
                                ERepositoryObjectType nodeType = (ERepositoryObjectType) currentNode
                                        .getProperties(EProperties.CONTENT_TYPE);
                                if (nodeType == ERepositoryObjectType.ROUTINES || nodeType == ERepositoryObjectType.PIG_UDF) {
                                    forceBuild = true;
                                }
                                if (!forceBuild) {
                                    if (GlobalServiceRegister.getDefault().isServiceRegistered(ICamelDesignerCoreService.class)) {
                                        ICamelDesignerCoreService camelService = (ICamelDesignerCoreService) GlobalServiceRegister
                                                .getDefault().getService(ICamelDesignerCoreService.class);
                                        if (nodeType == camelService.getBeansType()) {
                                            forceBuild = true;
                                        }
                                    }
                                }
                                factory.deleteObjectPhysical(ProjectManager.getInstance().getCurrentProject(), objToDelete, null,
                                        true);
                            }
                        }

                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }
                }
            });

        }
    }

    protected boolean isRelation(IEditorInput editorInput, String repoNodeId) {
        IRepoViewLinker[] allRepoViewLinkers = RepoViewLinkerRegistryReader.getInstance().getAllRepoViewLinkers();
        for (IRepoViewLinker linker : allRepoViewLinkers) {
            if (linker.isRelation(editorInput, repoNodeId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * DOC qzhang Comment method "getRootNode".
     * 
     * @param child
     * @return
     */
    private boolean isRootNodeDeleted(RepositoryNode child) {
        boolean isDeleted = false;
        if (child != null) {
            RepositoryNode parent = child.getParent();
            if (parent != null) {
                IRepositoryViewObject object = parent.getObject();
                if (object != null) {
                    ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

                    isDeleted = factory.getStatus(object) == ERepositoryStatus.DELETED;
                }

                if (!isDeleted) {
                    isDeleted = isRootNodeDeleted(parent);
                }
            }
        }
        return isDeleted;
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
        // TDI-23105:only for read-only(tag) project > also for offline, since TDI-23336
        if (factory.getRepositoryContext().isOffline() || factory.getRepositoryContext().isEditableAsReadOnly()) {
            canWork = false;
        }
        if (canWork) {
            Object o = selection.getFirstElement();
            RepositoryNode node = (RepositoryNode) o;
            switch (node.getType()) {
            case STABLE_SYSTEM_FOLDER:
                if (!node.isBin() || !node.hasChildren()) {
                    canWork = false;
                }
                break;
            default:
                canWork = false;
                break;
            }
            if (canWork && !ProjectManager.getInstance().isInCurrentMainProject(node)) {
                canWork = false;
            }
        }
        setEnabled(canWork);
    }

    private boolean validElement(IRepositoryNode node) {
        IRepositoryView viewPart = getViewPart();
        if (viewPart != null) {
            StructuredViewer viewer = viewPart.getViewer();
            if (viewer != null) {
                ViewerFilter[] filters = viewer.getFilters();
                if (filters != null) {
                    for (ViewerFilter vf : filters) {
                        if (!vf.select(viewer, node.getParent(), node)) {
                            return false;
                        }
                    }
                }
            }
        }

        return true; // other condition
    }
}
