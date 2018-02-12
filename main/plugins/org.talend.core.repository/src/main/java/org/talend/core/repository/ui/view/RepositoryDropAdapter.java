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
package org.talend.core.repository.ui.view;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.ui.navigator.CommonDropAdapter;
import org.eclipse.ui.navigator.INavigatorContentService;
import org.eclipse.ui.part.PluginDropAdapter;
import org.osgi.framework.FrameworkUtil;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.commons.ui.runtime.exception.MessageBoxExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.business.BusinessType;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.properties.SQLPatternItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.i18n.Messages;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.ui.actions.CopyObjectAction;
import org.talend.core.repository.ui.actions.MoveObjectAction;
import org.talend.core.repository.utils.AbstractResourceChangesService;
import org.talend.core.repository.utils.ConvertJobsUtil;
import org.talend.core.repository.utils.TDQServiceRegister;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.service.ITransformService;
import org.talend.designer.business.diagram.custom.IDiagramModelService;
import org.talend.designer.core.convert.ProcessConvertManager;
import org.talend.repository.ProjectManager;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class RepositoryDropAdapter extends PluginDropAdapter {

    private INavigatorContentService contentService;

    public RepositoryDropAdapter(StructuredViewer viewer) {
        super(viewer);
    }

    public RepositoryDropAdapter(StructuredViewer viewer, INavigatorContentService contentService) {
        super(viewer);
        this.contentService = contentService;
    }

    private DropTargetEvent event;

    @Override
    public void drop(DropTargetEvent event) {
        this.event = event;
        super.drop(event);
    }

    /**
     * Does the conversion between common jobs and m/r jobs. Added by Marvin Wang on Mar 19, 2013.
     * 
     * @param sourceSelections
     * @param targetNode that is <b>Standard Jobs</b> or <b>Map/Reduce Jobs</b>.
     */
    protected void doMapReduceConversion(IStructuredSelection sourceSelections, RepositoryNode targetNode) {
        final IProxyRepositoryFactory proxyRepositoryFactory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
        List<?> selectedRepNodes = sourceSelections.toList();
        if (selectedRepNodes != null && selectedRepNodes.size() > 0) {
            for (Object selectedRepNode : selectedRepNodes) {
                if (selectedRepNode instanceof RepositoryNode) {
                    final RepositoryNode sourceNode = (RepositoryNode) selectedRepNode;
                    final IRepositoryViewObject repositoryObject = sourceNode.getObject();
                    // if locked status, can not convert it
                    boolean isLock = MoveObjectAction.getInstance().isLock(sourceNode);
                    if (isLock) {
                        String errorMsg = null;
                        if (sourceNode.getObjectType().getType()
                                .equalsIgnoreCase(Messages.getString("RepositoryDropAdapter_folder"))) { //$NON-NLS-1$
                            errorMsg = Messages.getString("RepositoryDropAdapter_errorMsg"); //$NON-NLS-1$
                        }
                        if (ProxyRepositoryFactory.getInstance().getStatus(repositoryObject) == ERepositoryStatus.LOCK_BY_USER) {
                            errorMsg = Messages.getString("RepositoryDropAdapter_lockedByYou"); //$NON-NLS-1$
                        }
                        if (ProxyRepositoryFactory.getInstance().getStatus(repositoryObject) == ERepositoryStatus.LOCK_BY_OTHER) {
                            errorMsg = Messages.getString("RepositoryDropAdapter_lockedByOthers"); //$NON-NLS-1$
                        }
                        MessageDialog.openInformation(getViewer().getControl().getShell(),
                                Messages.getString("RepositoryDropAdapter_moveTitle"), //$NON-NLS-1$
                                errorMsg);
                        continue;
                    }

                    String jobTypeValue = null;
                    String frameworkNewValue = null;
                    if (targetNode.getContentType() == ERepositoryObjectType.PROCESS) {
                        jobTypeValue = ConvertJobsUtil.JobType.STANDARD.getDisplayName();
                    } else if (targetNode.getContentType() == ERepositoryObjectType.PROCESS_STORM) {
                        jobTypeValue = ConvertJobsUtil.JobType.BIGDATASTREAMING.getDisplayName();
                        frameworkNewValue = ConvertJobsUtil.JobStreamingFramework.SPARKSTREAMINGFRAMEWORK.getDisplayName();
                    } else if (targetNode.getContentType() == ERepositoryObjectType.PROCESS_MR) {
                        jobTypeValue = ConvertJobsUtil.JobType.BIGDATABATCH.getDisplayName();
                        frameworkNewValue = ConvertJobsUtil.JobBatchFramework.MAPREDUCEFRAMEWORK.getDisplayName();
                    }
                    final Item newItem = ConvertJobsUtil.createOperation(sourceNode.getObject().getLabel(), jobTypeValue,
                            frameworkNewValue, sourceNode.getObject());
                    //
                    RepositoryWorkUnit repositoryWorkUnit = new RepositoryWorkUnit("Drop job") { //$NON-NLS-1$

                        @Override
                        public void run() throws PersistenceException {
                            IWorkspaceRunnable runnable = new IWorkspaceRunnable() {

                                @Override
                                public void run(final IProgressMonitor monitor) throws CoreException {
                                    try {
                                        if (repositoryObject instanceof IProcess2) {
                                            boolean locked = proxyRepositoryFactory.getStatus(repositoryObject) == ERepositoryStatus.LOCK_BY_USER;
                                            if (locked) {
                                                proxyRepositoryFactory.unlock(repositoryObject);
                                            }
                                        }
                                        boolean isNewItemCreated = true;
                                        Property repositoryProperty = repositoryObject.getProperty();
                                        if (repositoryProperty != null) {
                                            isNewItemCreated = (repositoryProperty.getItem() != newItem);
                                        }
                                        if (isNewItemCreated) {
                                            // new Item created
                                            proxyRepositoryFactory.deleteObjectPhysical(repositoryObject);
                                            proxyRepositoryFactory.saveProject(ProjectManager.getInstance().getCurrentProject());
                                        } else if (repositoryObject.getProperty() != null) {
                                            proxyRepositoryFactory.save(ProjectManager.getInstance().getCurrentProject(),
                                                    repositoryObject.getProperty().getItem(), false);
                                        }
                                    } catch (PersistenceException e1) {
                                        e1.printStackTrace();
                                    } catch (LoginException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            // unlockObject();
                            // alreadyEditedByUser = true; // to avoid 2 calls of unlock
                            IWorkspace workspace = ResourcesPlugin.getWorkspace();
                            try {
                                ISchedulingRule schedulingRule = workspace.getRoot();
                                // the update the project files need to be done in the workspace runnable to
                                // avoid all
                                // notification
                                // of changes before the end of the modifications.
                                workspace.run(runnable, schedulingRule, IWorkspace.AVOID_UPDATE, null);
                            } catch (CoreException e1) {
                                MessageBoxExceptionHandler.process(e1.getCause());
                            }
                        }
                    };
                    repositoryWorkUnit.setAvoidSvnUpdate(true);
                    repositoryWorkUnit.setAvoidUnloadResources(true);
                    ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(repositoryWorkUnit);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.PluginDropAdapter#performDrop(java.lang.Object)
     */
    @Override
    public boolean performDrop(final Object data) {
        int operation = getCurrentOperation();
        Object object = getCurrentTarget();
        boolean toReturn = true;
        if (object instanceof RepositoryNode) {
            RepositoryNode targetNode = (RepositoryNode) object;
            toReturn = performDropRepositoryNode(targetNode, operation, data);
        } else if (object instanceof IResource) {
            boolean isSample = false;
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ITransformService.class)) {
                ITransformService transformService = (ITransformService) GlobalServiceRegister.getDefault().getService(
                        ITransformService.class);
                if (transformService.isSampleFileResource((IResource) object)) {
                    isSample = true;
                }
            }
            IResource targetNode = (IResource) object;
            if (!isSample) {
                toReturn = performDropTDMResource(targetNode, operation, data);
            } else {
                performDropSampleResource(targetNode, operation, data);
            }
        }
        return toReturn;
    }

    private boolean performDropRepositoryNode(RepositoryNode targetNode, int operation, final Object data) {
        boolean toReturn = true;

        try {
            switch (operation) {
            case DND.DROP_COPY:
                RunnableWithReturnValue runnable = new CopyRunnable(
                        Messages.getString("RepositoryDropAdapter_copyingItems"), data, targetNode); //$NON-NLS-1$
                // runInProgressDialog(runnable);
                // toReturn = (Boolean) runnable.getReturnValue();
                runCopy(data, targetNode);
                break;
            case DND.DROP_MOVE:
                // If m/r job drags and drops to Standard Jobs or folder, then convert it. Common job is the same as m/r
                // job.
                // TODO For the different type of repository in selection, need to consider the move and convert.
                IStructuredSelection selection = (IStructuredSelection) data;
                List<?> selectedRepNodes = selection.toList();
                if (selectedRepNodes != null && selectedRepNodes.size() > 0) {
                    RepositoryNode sourceNode = (RepositoryNode) selection.getFirstElement();
                    if (ProcessConvertManager.getInstance().CheckConvertProcess(sourceNode, targetNode)) {
                        doMapReduceConversion(selection, targetNode);
                    } else {
                        // Otherwise do removing job.
                        runnable = new MoveRunnable(Messages.getString("RepositoryDropAdapter_movingItems"), data, targetNode); //$NON-NLS-1$
                        runInProgressDialog(runnable);
                        toReturn = (Boolean) runnable.getReturnValue();
                    }
                }
                break;
            // TDI-20080 hqzhang, operation value(actually is event.detail) on drag enter state maybe different from
            // value on drop
            // state, this could be a bug of ViewerDropAdapter
            case DND.DROP_DEFAULT:
                if (event.detail == DND.DROP_COPY) {
                    runnable = new CopyRunnable(Messages.getString("RepositoryDropAdapter_copyingItems"), data, targetNode); //$NON-NLS-1$
                    runInProgressDialog(runnable);
                    toReturn = (Boolean) runnable.getReturnValue();
                    break;
                }
                if (event.detail == DND.DROP_MOVE) {
                    runnable = new MoveRunnable(Messages.getString("RepositoryDropAdapter_movingItems"), data, targetNode); //$NON-NLS-1$
                    runInProgressDialog(runnable);
                    toReturn = (Boolean) runnable.getReturnValue();
                    break;
                }
            default:
                // Nothing to do
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return toReturn;
    }

    private boolean performDropTDMResource(IResource targetNode, int operation, final Object data) {
        boolean toReturn = true;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITransformService.class)) {
            ITransformService transformService = (ITransformService) GlobalServiceRegister.getDefault().getService(
                    ITransformService.class);
            toReturn = transformService.performDrop(event, targetNode, operation, data);
        }
        return toReturn;
    }

    private boolean performDropSampleResource(IResource targetNode, int operation, final Object data) {
        boolean toReturn = true;
        CommonDropAdapter adapter = new CommonDropAdapter(contentService, (StructuredViewer) this.getViewer());
        adapter.dragEnter(getCurrentEvent());
        adapter.drop(getCurrentEvent());
        try {
            IStructuredSelection selection = (IStructuredSelection) data;
            List<?> selectedRepNodes = selection.toList();
            IResource parent = null;
            for (Object source : selectedRepNodes) {
                if (source instanceof IResource) {
                    parent = ((IResource) source).getParent();
                    break;
                }
            }
            if (parent != null) {
                parent.refreshLocal(IResource.DEPTH_ONE, new NullProgressMonitor());
            }
            targetNode.refreshLocal(IResource.DEPTH_ONE, new NullProgressMonitor());
        } catch (CoreException e) {
            ExceptionHandler.process(e);
        }
        final IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        RepositoryWorkUnit<Object> repositoryWorkUnit = new RepositoryWorkUnit<Object>("Save BaseMainNode", this) {

            @Override
            protected void run() throws LoginException, PersistenceException {
                // empty work unit to do svn update before any action from TDM
            }
        };
        factory.executeRepositoryWorkUnit(repositoryWorkUnit);
        return toReturn;
    }

    /**
     * ADD gdbu 2011-9-29 TDQ-3546
     * 
     * DOC gdbu(TDQ) Comment method "judgeCanMoveOrNotByDependency". If current connecton have dependencies in TDQ ,
     * then return false and that means can not move it.
     * 
     * @param sourceNode
     * @param resourceChangeService
     * @return
     */
    private boolean judgeCanMoveOrNotByDependency(RepositoryNode sourceNode, AbstractResourceChangesService resourceChangeService) {
        for (IRepositoryNode iRepositoryNode : sourceNode.getChildren()) {
            RepositoryNode repositoryNode = (RepositoryNode) iRepositoryNode;
            if (repositoryNode.getType().equals(ENodeType.SIMPLE_FOLDER)) {
                judgeCanMoveOrNotByDependency(repositoryNode, resourceChangeService);
            } else {
                // iRepositoryNode.getObject().getProperty().getItem().eResource().unload();
                Item item = repositoryNode.getObject() == null ? null : repositoryNode.getObject().getProperty().getItem();
                if (resourceChangeService != null && null != item) {
                    List<IRepositoryNode> dependentNodes = resourceChangeService.getDependentNodes(repositoryNode);
                    if (dependentNodes != null && !dependentNodes.isEmpty()) {
                        resourceChangeService.openDependcesDialog(dependentNodes);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.PluginDropAdapter#validateDrop(java.lang.Object, int, org.eclipse.swt.dnd.TransferData)
     */
    @Override
    public boolean validateDrop(Object target, int operation, TransferData transferType) {
        if (target == null) {
            return false;
        }
        super.validateDrop(target, operation, transferType);
        boolean isValid = true;
        Set<IResource> parents = new HashSet<IResource>();
        for (Object obj : ((StructuredSelection) getViewer().getSelection()).toArray()) {
            if (obj instanceof IResource) {
                parents.add(((IResource) obj).getParent());
            }
        }
        for (Object obj : ((StructuredSelection) getViewer().getSelection()).toArray()) {
            if (obj instanceof IResource) {
                if (parents.size() > 1) {
                    return false;
                }
                return validateTDMDrop(operation, (IResource) obj, target);
            }

            if (!(obj instanceof RepositoryNode)) {
                return false;
            }

            RepositoryNode sourceNode = (RepositoryNode) obj;

            if (sourceNode != null) {
                IRepositoryViewObject object = sourceNode.getObject();
                if (object == null) {
                    return false;
                }
                if (object.getRepositoryObjectType() == ERepositoryObjectType.JOB_DOC
                        || object.getRepositoryObjectType() == ERepositoryObjectType.JOBLET_DOC) {
                    if (GlobalServiceRegister.getDefault().isServiceRegistered(IDiagramModelService.class)) {
                        IDiagramModelService diagramModelService = (IDiagramModelService) GlobalServiceRegister.getDefault()
                                .getService(IDiagramModelService.class);
                        if (diagramModelService != null && BusinessType.SHAP == diagramModelService.getBusinessModelType(target)) {
                            return true;
                        }
                    }

                    return false;
                } else if (object.getRepositoryObjectType() == ERepositoryObjectType.ROUTINES) {
                    Property property = object.getProperty();
                    RoutineItem item = (RoutineItem) property.getItem();
                    if (item.isBuiltIn() && target instanceof RepositoryNode) {
                        return false;
                    }
                } else if (object.getRepositoryObjectType() == ERepositoryObjectType.SQLPATTERNS) {
                    Property property = object.getProperty();
                    SQLPatternItem item = (SQLPatternItem) property.getItem();
                    if (item.isSystem() && target instanceof RepositoryNode) {
                        return false;
                    }
                } else if (object.getRepositoryObjectType() == ERepositoryObjectType.PROCESS_MR) {
                    if (target instanceof RepositoryNode) {
                        RepositoryNode targetRN = (RepositoryNode) target;
                        if (ENodeType.SYSTEM_FOLDER == targetRN.getType() || ENodeType.SIMPLE_FOLDER == targetRN.getType()) {
                            if (targetRN.getContentType() == ERepositoryObjectType.PROCESS
                                    || targetRN.getContentType() == ERepositoryObjectType.PROCESS_STORM) {
                                return isValid = true;
                            }
                        }
                    }
                } else if (object.getRepositoryObjectType() == ERepositoryObjectType.PROCESS_STORM) {
                    if (target instanceof RepositoryNode) {
                        RepositoryNode targetRN = (RepositoryNode) target;
                        if (ENodeType.SYSTEM_FOLDER == targetRN.getType() || ENodeType.SIMPLE_FOLDER == targetRN.getType()) {
                            if (targetRN.getContentType() == ERepositoryObjectType.PROCESS
                                    || targetRN.getContentType() == ERepositoryObjectType.PROCESS_MR) {
                                return isValid = true;
                            }
                        }
                    }
                } else if (object.getRepositoryObjectType() == ERepositoryObjectType.PROCESS) {
                    if (target instanceof RepositoryNode) {
                        RepositoryNode targetRN = (RepositoryNode) target;
                        if (ENodeType.SYSTEM_FOLDER == targetRN.getType() || ENodeType.SIMPLE_FOLDER == targetRN.getType()) {
                            if (targetRN.getContentType() == ERepositoryObjectType.PROCESS_MR
                                    || targetRN.getContentType() == ERepositoryObjectType.PROCESS_STORM) {
                                return isValid = true;
                            }
                        }
                    }
                }

            }

            switch (operation) {
            case DND.DROP_COPY:
                isValid = CopyObjectAction.getInstance().validateAction(sourceNode, (RepositoryNode) target);
                break;
            // case DND.DROP_NONE:
            case DND.DROP_MOVE:
                isValid = MoveObjectAction.getInstance().validateAction(sourceNode, (RepositoryNode) target, true);
                break;
            case DND.DROP_DEFAULT:
            case DND.Drop: // hywang
                isValid = MoveObjectAction.getInstance().validateAction(sourceNode, (RepositoryNode) target, true);
                break;
            default:
                isValid = false;
            }
            // if (!isValid) {
            // return isValid;
            // }
        }

        return isValid;
    }

    private boolean validateTDMDrop(int operation, IResource obj, Object target) {
        if (obj == null) {
            return false;
        }
        boolean result = true;
        switch (operation) {
        case DND.DROP_COPY:
            if (obj instanceof IFolder) {
                return false;
            }
            break;
        case DND.DROP_MOVE:
            if (target instanceof IFolder) {
                if (obj.getParent().getFullPath().equals(((IFolder) target).getFullPath())) {
                    return false;
                }
            } else if (target instanceof IFile) {
                if (((IFile) target).getParent().getFullPath().equals(obj.getParent().getFullPath())) {
                    return false;
                }
            }
            break;
        case DND.DROP_DEFAULT:
        case DND.Drop:
            break;
        }
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITransformService.class)) {
            ITransformService transformService = (ITransformService) GlobalServiceRegister.getDefault().getService(
                    ITransformService.class);
            if (transformService.isTransformResource(obj)) {
                result = true;
            }
        }
        return result;
    }

    private void runCopy(final Object data, final RepositoryNode targetNode) {
        String copyName = "User action : Copy Object"; //$NON-NLS-1$
        RepositoryWorkUnit<Object> repositoryWorkUnit = new RepositoryWorkUnit<Object>(copyName, CopyObjectAction.getInstance()) {

            @Override
            protected void run() throws LoginException, PersistenceException {
                try {
                    for (Object obj : ((StructuredSelection) data).toArray()) {
                        final RepositoryNode sourceNode = (RepositoryNode) obj;
                        CopyObjectAction.getInstance().execute(sourceNode, targetNode);
                    }
                } catch (Exception e) {
                    throw new PersistenceException(e);
                }
            }
        };
        ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(repositoryWorkUnit);
    }

    private void runInProgressDialog(final IWorkspaceRunnable op) {
        final IRunnableWithProgress iRunnableWithProgress = new IRunnableWithProgress() {

            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                IWorkspace workspace = ResourcesPlugin.getWorkspace();
                try {
                    ISchedulingRule schedulingRule = workspace.getRoot();
                    // the update the project files need to be done in the workspace runnable to avoid all
                    // notifications.
                    // of changes before the end of the modifications.
                    workspace.run(op, schedulingRule, IWorkspace.AVOID_UPDATE, monitor);
                } catch (CoreException e) {
                    throw new InvocationTargetException(e);
                }
            }
        };

        RepositoryWorkUnit<Object> repositoryWorkUnit = new RepositoryWorkUnit<Object>("Move or Copy", this) { //$NON-NLS-1$

            @Override
            protected void run() throws LoginException, PersistenceException {
                try {
                    ProgressMonitorDialog progress = new ProgressMonitorDialog(getViewer().getControl().getShell());
                    progress.run(false, false, iRunnableWithProgress);
                } catch (InvocationTargetException e) {
                    ExceptionHandler.process(e);
                } catch (InterruptedException e) {
                    ExceptionHandler.process(e);
                }
            }
        };
        repositoryWorkUnit.setAvoidUnloadResources(true);
        CoreRuntimePlugin.getInstance().getProxyRepositoryFactory().executeRepositoryWorkUnit(repositoryWorkUnit);
    }

    /**
     * hqzhang.
     */
    class CopyRunnable extends RunnableWithReturnValue {

        private Object data;

        private RepositoryNode targetNode;

        public CopyRunnable(String taskName, Object data, RepositoryNode targetNode) {
            super(taskName);
            this.data = data;
            this.targetNode = targetNode;
        }

        @Override
        public void run(IProgressMonitor monitor) throws CoreException {
            monitor.beginTask(getTaskName(), IProgressMonitor.UNKNOWN);
            String copyName = "User action : Copy Object"; //$NON-NLS-1$
            RepositoryWorkUnit<Object> repositoryWorkUnit = new RepositoryWorkUnit<Object>(copyName,
                    CopyObjectAction.getInstance()) {

                @Override
                protected void run() throws LoginException, PersistenceException {
                    try {
                        for (Object obj : ((StructuredSelection) data).toArray()) {
                            final RepositoryNode sourceNode = (RepositoryNode) obj;
                            CopyObjectAction.getInstance().execute(sourceNode, targetNode);
                        }
                    } catch (Exception e) {
                        throw new PersistenceException(e);
                    }
                }
            };
            ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(repositoryWorkUnit);
            setReturnValue(true);
            monitor.done();
        }

    }

    /**
     * hqzhang.
     */
    class MoveRunnable extends RunnableWithReturnValue {

        private Object data;

        private RepositoryNode targetNode;

        public MoveRunnable(String taskName, Object data, RepositoryNode targetNode) {
            super(taskName);
            this.data = data;
            this.targetNode = targetNode;
        }

        @Override
        public void run(final IProgressMonitor monitor) throws CoreException {
            monitor.beginTask(getTaskName(), IProgressMonitor.UNKNOWN);
            // MOD gdbu 2011-10-9 TDQ-3545
            List<?> selectItems = ((org.eclipse.jface.viewers.TreeSelection) data).toList();
            final List<RepositoryNode> operationableItems = new ArrayList<RepositoryNode>();
            for (Object object : selectItems) {
                if (object instanceof RepositoryNode) {
                    RepositoryNode repositoryNode = (RepositoryNode) object;
                    boolean isLock = false;
                    monitor.subTask(Messages.getString(
                            "RepositoryDropAdapter.checkingLockStatus", repositoryNode.getObject().getLabel())); //$NON-NLS-1$
                    isLock = MoveObjectAction.getInstance().isLock(repositoryNode);
                    if (isLock) {
                        String errorMsg = null;
                        IRepositoryViewObject objectToCopy = repositoryNode.getObject();
                        // TDI-14680 add a warning message when move a directory that it has locked jobs.
                        IRepositoryNode node = objectToCopy.getRepositoryNode();
                        if (node.getObjectType().getType().equalsIgnoreCase(Messages.getString("RepositoryDropAdapter_folder"))) { //$NON-NLS-1$
                            errorMsg = Messages.getString("RepositoryDropAdapter_errorMsg"); //$NON-NLS-1$
                        }
                        if (ProxyRepositoryFactory.getInstance().getStatus(objectToCopy) == ERepositoryStatus.LOCK_BY_USER) {
                            errorMsg = Messages.getString("RepositoryDropAdapter_lockedByYou"); //$NON-NLS-1$
                        }
                        if (ProxyRepositoryFactory.getInstance().getStatus(objectToCopy) == ERepositoryStatus.LOCK_BY_OTHER) {
                            errorMsg = Messages.getString("RepositoryDropAdapter_lockedByOthers"); //$NON-NLS-1$
                        }
                        MessageDialog.openInformation(getViewer().getControl().getShell(),
                                Messages.getString("RepositoryDropAdapter_moveTitle"), //$NON-NLS-1$
                                errorMsg);
                        setReturnValue(false);
                        continue;
                        // return;
                    }
                    ERepositoryObjectType sourceType = (ERepositoryObjectType) repositoryNode
                            .getProperties(EProperties.CONTENT_TYPE);
                    if (sourceType == ERepositoryObjectType.METADATA_CONNECTIONS
                            || sourceType == ERepositoryObjectType.METADATA_FILE_DELIMITED
                            || sourceType == ERepositoryObjectType.METADATA_MDMCONNECTION) {

                        AbstractResourceChangesService resourceChangeService = TDQServiceRegister.getInstance()
                                .getResourceChangeService(AbstractResourceChangesService.class);
                        boolean judgeCanMoveOrNotByDependency = judgeCanMoveOrNotByDependency(repositoryNode,
                                resourceChangeService);
                        if (!judgeCanMoveOrNotByDependency) {
                            setReturnValue(false);
                            continue;
                            // return;
                        }
                    }

                    operationableItems.add(repositoryNode);
                }
            }
            // ~TDQ-3545

            if (operationableItems.isEmpty()) {
                return;
            }
            String moveName = "User action : Move Object"; //$NON-NLS-1$
            RepositoryWorkUnit<Object> repositoryWorkUnit = new RepositoryWorkUnit<Object>(moveName,
                    MoveObjectAction.getInstance()) {

                @Override
                protected void run() throws LoginException, PersistenceException {
                    final IWorkspaceRunnable op = new IWorkspaceRunnable() {

                        @Override
                        public void run(IProgressMonitor monitor) throws CoreException {
                            try {
                                // for (Object obj : ((StructuredSelection) data).toArray()) {
                                // final RepositoryNode sourceNode = (RepositoryNode) obj;
                                //                                    monitor.subTask(Messages.getString("RepositoryDropAdapter.moving") + sourceNode.getObject().getLabel()); //$NON-NLS-1$
                                // MoveObjectAction.getInstance().execute(sourceNode, targetNode, true);
                                // }
                                // RepositoryNode[] nodeArray = new RepositoryNode[((StructuredSelection)
                                // data).toArray().length];
                                // for (int i = 0; i < ((StructuredSelection) data).toArray().length; i++) {
                                // final RepositoryNode sourceNode = (RepositoryNode) ((StructuredSelection)
                                // data).toArray()[i];
                                // nodeArray[i] = sourceNode;
                                // }
                                RepositoryNode[] nodeArray = operationableItems.toArray(new RepositoryNode[operationableItems
                                        .size()]);
                                MoveObjectAction.getInstance().executeMulti(nodeArray, targetNode, null, true);
                            } catch (Exception e) {
                                throw new CoreException(new org.eclipse.core.runtime.Status(IStatus.ERROR, FrameworkUtil
                                        .getBundle(this.getClass()).getSymbolicName(), "Error", e)); //$NON-NLS-1$
                            }
                        };

                    };
                    IWorkspace workspace = ResourcesPlugin.getWorkspace();
                    try {
                        ISchedulingRule schedulingRule = workspace.getRoot();
                        // the update the project files need to be done in the workspace runnable to avoid all
                        // notification
                        // of changes before the end of the modifications.
                        workspace.run(op, schedulingRule, IWorkspace.AVOID_UPDATE, new NullProgressMonitor());
                    } catch (CoreException e) {
                        throw new PersistenceException(e.getCause());
                    }

                }
            };
            ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(repositoryWorkUnit);
            setReturnValue(true);
            monitor.done();

            setReturnValue(true);
            monitor.done();
        }
    }

    // public IStatus handleDrop(DropTargetEvent aDropTargetEvent, Object aTarget) {
    //
    // if (Policy.DEBUG_DND) {
    //            System.out.println("ResourceDropAdapterAssistant.handleDrop (begin)"); //$NON-NLS-1$
    // }
    //
    // // alwaysOverwrite = false;
    // if (aTarget == null || aDropTargetEvent.data == null) {
    // return Status.CANCEL_STATUS;
    // }
    // IStatus status = null;
    // IResource[] resources = null;
    // TransferData currentTransfer = this.getCurrentTransfer();
    // if (LocalSelectionTransfer.getTransfer().isSupportedType(currentTransfer)) {
    // resources = getSelectedResources();
    // aDropTargetEvent.detail = DND.DROP_NONE;
    // } else if (ResourceTransfer.getInstance().isSupportedType(currentTransfer)) {
    // resources = (IResource[]) aDropTargetEvent.data;
    // }
    //
    // // if (FileTransfer.getInstance().isSupportedType(currentTransfer)) {
    // // status = performFileDrop(aDropAdapter, aDropTargetEvent.data);
    // // } else
    // if (resources != null && resources.length > 0) {
    // if ((this.getCurrentOperation() == DND.DROP_COPY) || (this.getCurrentOperation() == DND.DROP_LINK)) {
    // if (Policy.DEBUG_DND) {
    //                    System.out.println("ResourceDropAdapterAssistant.handleDrop executing COPY."); //$NON-NLS-1$
    // }
    // status = performResourceCopy(this, getShell(), resources);
    // } else {
    // if (Policy.DEBUG_DND) {
    //                    System.out.println("ResourceDropAdapterAssistant.handleDrop executing MOVE."); //$NON-NLS-1$
    // }
    //
    // status = performResourceMove(this, resources);
    // }
    // }
    // openError(status);
    // IContainer target = getActualTarget((IResource) aTarget);
    // if (target != null && target.isAccessible()) {
    // try {
    // target.refreshLocal(IResource.DEPTH_ONE, null);
    // } catch (CoreException e) {
    // }
    // }
    // return status;
    // }

    // private IStatus performFileDrop(final RepositoryDropAdapter anAdapter, Object data) {
    // final int currentOperation = anAdapter.getCurrentOperation();
    // MultiStatus problems = new MultiStatus(PlatformUI.PLUGIN_ID, 0,
    // WorkbenchNavigatorMessages.DropAdapter_problemImporting, null);
    // mergeStatus(problems,
    // validateTarget(anAdapter.getCurrentTarget(), anAdapter
    // .getCurrentTransfer(), currentOperation));
    //
    // final IContainer target = getActualTarget((IResource) anAdapter
    // .getCurrentTarget());
    // final String[] names = (String[]) data;
    // // Run the import operation asynchronously.
    // // Otherwise the drag source (e.g., Windows Explorer) will be blocked
    // // while the operation executes. Fixes bug 16478.
    // Display.getCurrent().asyncExec(new Runnable() {
    // public void run() {
    // getShell().forceActive();
    // new CopyFilesAndFoldersOperation(getShell()).copyOrLinkFiles(names, target, currentOperation);
    // }
    // });
    // return problems;
    // }

}
