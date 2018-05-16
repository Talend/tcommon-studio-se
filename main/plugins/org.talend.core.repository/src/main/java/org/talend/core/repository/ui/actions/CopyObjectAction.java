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

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.exception.SystemException;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.IESBService;
import org.talend.core.PluginChecker;
import org.talend.core.hadoop.IHadoopClusterService;
import org.talend.core.hadoop.repository.HadoopRepositoryUtil;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.PigudfItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.properties.SQLPatternItem;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.ui.dialog.PastSelectorDialog;
import org.talend.core.runtime.services.IGenericDBService;
import org.talend.core.ui.ICDCProviderService;
import org.talend.core.ui.ITestContainerProviderService;
import org.talend.designer.codegen.ICodeGeneratorService;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class CopyObjectAction {

    IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

    private static CopyObjectAction singleton = new CopyObjectAction();

    public static CopyObjectAction getInstance() {
        return singleton;
    }

    public boolean validateAction(RepositoryNode sourceNode, RepositoryNode targetNode) {
        if (sourceNode == null) {
            return false;
        }
        // Cannot copy folder or system folder :
        if (sourceNode.getType() != ENodeType.REPOSITORY_ELEMENT) {
            return false;
        }
        IRepositoryViewObject objectToCopy = sourceNode.getObject();
        if (objectToCopy.getId() == null) {
            return false;
        }
        // TDI-18273:if the paste item in clipboard has been deleted physically from recycle bin,can not copy again.
        try {
            if (ProxyRepositoryFactory.getInstance().getLastVersion(objectToCopy.getId()) == null) {
                return false;
            }
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }

        // Cannot move logically deleted objects :
        // try {
        if (objectToCopy != null && objectToCopy.getId() == null) {
            return false;
        }
        // Cannot copy for refProject
        // if (objectToCopy != null && factory.getStatus(objectToCopy) == ERepositoryStatus.READ_ONLY) {
        // return false;
        // }

        // if copied item has been deleted
        if (objectToCopy == null || objectToCopy.getRepositoryStatus() == ERepositoryStatus.DELETED
                || ProxyRepositoryFactory.getInstance().getStatus(sourceNode.getObject()) == ERepositoryStatus.DELETED) {
            return false;
        }

        // } catch (PersistenceException e) {
        // ExceptionHandler.process(e);
        // return false;
        // }

        // Cannot copy system routines:
        if (objectToCopy.getRepositoryObjectType() == ERepositoryObjectType.ROUTINES) {
            Property property = objectToCopy.getProperty();
            RoutineItem item = (RoutineItem) property.getItem();
            return !item.isBuiltIn();
        }
        // Cannot copy system sql pattern:
        if (objectToCopy.getRepositoryObjectType() == ERepositoryObjectType.SQLPATTERNS) {
            Property property = objectToCopy.getProperty();
            SQLPatternItem item = (SQLPatternItem) property.getItem();
            return !item.isSystem();
        }
        // for cdc
        if (PluginChecker.isCDCPluginLoaded()) {
            ICDCProviderService cdcService = (ICDCProviderService) GlobalServiceRegister.getDefault().getService(
                    ICDCProviderService.class);
            if (cdcService != null
                    && (cdcService.isSubscriberTableNode(sourceNode) || cdcService.isSystemSubscriberTable(sourceNode))) {
                return false;
            }
        }
        // Special rule : temp ?
        if (targetNode == null) {
            return true;
        }

        // for bug 0005454: Copy paste with keyboard in the repository view doesn't work.
        if (targetNode.getType() == ENodeType.REPOSITORY_ELEMENT || targetNode.getType() == ENodeType.SIMPLE_FOLDER
                || targetNode.getType() == ENodeType.SYSTEM_FOLDER) {
            ERepositoryObjectType targetType = ((ERepositoryObjectType) targetNode.getProperties(EProperties.CONTENT_TYPE));
            ERepositoryObjectType sourceType = getSourceType(sourceNode);
            if (sourceType.equals(ERepositoryObjectType.PROCESS_STORM)) {
                return targetType.equals(ERepositoryObjectType.PROCESS) || targetType.equals(ERepositoryObjectType.PROCESS_STORM)
                        || targetType.equals(ERepositoryObjectType.PROCESS_MR);
            }
            if (sourceType.equals(ERepositoryObjectType.PROCESS_MR)) {
                return targetType.equals(ERepositoryObjectType.PROCESS_STORM)
                        || targetType.equals(ERepositoryObjectType.PROCESS_MR);
            }

            return targetType.equals(sourceType);
        }
        return false;
    }
    
    private ERepositoryObjectType getSourceType(RepositoryNode sourceNode){
        List<ERepositoryObjectType> extraTypes = new ArrayList<ERepositoryObjectType>();
        IGenericDBService dbService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IGenericDBService.class)) {
            dbService = (IGenericDBService) GlobalServiceRegister.getDefault().getService(
                    IGenericDBService.class);
        }
        if(dbService != null){
            extraTypes.addAll(dbService.getExtraTypes());
        }
        ERepositoryObjectType sourceType = sourceNode.getObjectType();
        if(sourceType != null && extraTypes.contains(sourceType)){
            return sourceNode.getContentType();
        }
        return sourceType;
    }

    public void execute(final RepositoryNode sourceNode, RepositoryNode targetNode) throws Exception {
        if (!validateAction(sourceNode, targetNode)) {
            return;
        }

        // for bug 0005454: Copy paste with keyboard in the repository view doesn't work.
        if (targetNode.getType() == ENodeType.REPOSITORY_ELEMENT) {
            targetNode = targetNode.getParent();
        }

        final IPath path = RepositoryNodeUtilities.getPath(targetNode);

        if (sourceNode.getType().equals(ENodeType.REPOSITORY_ELEMENT)) {
            // Source is an repository element :
            // wzhang modified to fix bug 12349 and 11535
            final Item originalItem = sourceNode.getObject().getProperty().getItem();
            List<IRepositoryViewObject> allVersion = factory.getAllVersion(originalItem.getProperty().getId());

            if (allVersion.size() == 1) {
                Item curItem = originalItem;
                for (IRepositoryViewObject obj : allVersion) {
                    if (obj.getVersion().equals(originalItem.getProperty().getVersion())) {
                        curItem = obj.getProperty().getItem();
                        break;
                    }
                }
                copySingleVersionItem(curItem, path, sourceNode);
            } else if (allVersion.size() > 1) {
                PastSelectorDialog dialog = new PastSelectorDialog(Display.getCurrent().getActiveShell(), allVersion, sourceNode);
                if (dialog.open() == Window.OK) {
                    final Set<IRepositoryViewObject> selectedVersionItems = dialog.getSelectedVersionItems();

                    final IWorkspaceRunnable op = new IWorkspaceRunnable() {

                        @Override
                        public void run(IProgressMonitor monitor) throws CoreException {
                            try {
                                Iterator<IRepositoryViewObject> iterator = selectedVersionItems.iterator();
                                while (iterator.hasNext()) {
                                    IRepositoryViewObject repObj = iterator.next();
                                    Item selectedItem = repObj.getProperty().getItem();
                                    if (!iterator.hasNext() && isHadoopClusterItem(selectedItem)) {
                                        copyHadoopClusterItem(selectedItem, path);
                                        return;
                                    }
                                }

                                String id = null;
                                String label = null;
                                boolean isfirst = true;
                                boolean needSys = true;
                                List newItems = new ArrayList();
                                for (IRepositoryViewObject object : selectedVersionItems) {
                                    Item selectedItem = object.getProperty().getItem();

                                    Item copy = null;
                                    if (isfirst) {
                                        copy = factory.copy(selectedItem, path);
                                        id = copy.getProperty().getId();
                                        label = copy.getProperty().getLabel();
                                        isfirst = false;
                                    } else {
                                        copy = factory.copy(selectedItem, path, label);
                                    }
                                    copy.getProperty().setId(id);
                                    if (needSys && originalItem instanceof RoutineItem) {
                                        String lastestVersion = getLastestVersion(selectedVersionItems);
                                        if (lastestVersion.equals(copy.getProperty().getVersion())) {
                                            synDuplicatedRoutine((RoutineItem) copy, selectedItem.getProperty().getLabel());
                                            needSys = false;
                                        }
                                    }
                                    if (copy instanceof ProcessItem) {
                                        RelationshipItemBuilder.getInstance().addOrUpdateItem(copy);
                                    }
                                    newItems.add(copy);
                                    factory.save(copy);
                                }
                                if (newItems.size() > 0) {
                                    Collections.sort(newItems, new Comparator() {

                                        @Override
                                        public int compare(Object o1, Object o2) {
                                            Item i1 = (Item) o1;
                                            Item i2 = (Item) o2;
                                            return i1.getProperty().getVersion().compareTo(i2.getProperty().getVersion());
                                        }

                                    });
                                    Item newItem = (Item) newItems.get(newItems.size() - 1);
                                    copyDataServiceRelateJob(newItem);
                                    copyTestCases(newItem, sourceNode, false);
                                }
                            } catch (PersistenceException e) {
                                ExceptionHandler.process(e);
                            } catch (BusinessException e) {
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
                                // the update the project files need to be done in the workspace runnable to avoid
                                // all
                                // notification
                                // of changes before the end of the modifications.
                                workspace.run(op, schedulingRule, IWorkspace.AVOID_UPDATE, monitor);
                            } catch (CoreException e) {
                                throw new InvocationTargetException(e);
                            }

                        }
                    };

                    try {
                        new ProgressMonitorDialog(null).run(false, false, iRunnableWithProgress);
                    } catch (InvocationTargetException e) {
                        ExceptionHandler.process(e);
                    } catch (InterruptedException e) {
                        //
                    }

                    // }

                }
            }
        }

    }

    private void copyDataServiceRelateJob(Item newItem) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IESBService.class)) {
            IESBService service = (IESBService) GlobalServiceRegister.getDefault().getService(IESBService.class);
            if (service.isServiceItem(newItem.eClass().getClassifierID())) {
                service.copyDataServiceRelateJob(newItem);
            }
        }
    }

    private void copySingleVersionItem(final Item item, final IPath path, final RepositoryNode sourceNode) {
        final RepositoryWorkUnit<Object> workUnit = new RepositoryWorkUnit<Object>("", this) {//$NON-NLS-1$

            @Override
            protected void run() throws LoginException, PersistenceException {
                final IWorkspaceRunnable op = new IWorkspaceRunnable() {

                    @Override
                    public void run(IProgressMonitor monitor) throws CoreException {
                        try {
                            if (isHadoopClusterItem(item)) {
                                copyHadoopClusterItem(item, path);
                                return;
                            }
                            Item newItem = factory.copy(item, path, true);
                            // qli modified to fix the bug 5400 and 6185.
                            if (newItem instanceof RoutineItem) {
                                synDuplicatedRoutine((RoutineItem) newItem, item.getProperty().getLabel());
                            }
                            if (newItem instanceof ProcessItem || newItem instanceof JobletProcessItem) {
                                RelationshipItemBuilder.getInstance().addOrUpdateItem(newItem);
                            }
                            if (newItem instanceof ConnectionItem) {
                                ConnectionItem connectionItem = (ConnectionItem) newItem;
                                connectionItem.getConnection().getSupplierDependency().clear();
                            }
                            factory.save(newItem);
                            copyDataServiceRelateJob(newItem);
                            copyTestCases(newItem, sourceNode, false);
                        } catch (Exception e) {
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
                            workspace.run(op, schedulingRule, IWorkspace.AVOID_UPDATE, monitor);
                        } catch (CoreException e) {
                            throw new InvocationTargetException(e);
                        }

                    }
                };
                try {
                    new ProgressMonitorDialog(null).run(false, false, iRunnableWithProgress);
                } catch (InvocationTargetException e) {
                    ExceptionHandler.process(e);
                } catch (InterruptedException e) {
                    //
                }
            }
        };

        workUnit.setAvoidUnloadResources(true);
        factory.executeRepositoryWorkUnit(workUnit);
    }

    public void copyTestCases(Item newItem, RepositoryNode sourceNode, boolean isDuplicate) {
        if (!isAllowedToCopyTestCase(newItem, sourceNode)) {
            return;
        }
        final IPath path = getTestCasePath(newItem, sourceNode);

        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class)) {
            ITestContainerProviderService testContainerService = (ITestContainerProviderService) GlobalServiceRegister
                    .getDefault().getService(ITestContainerProviderService.class);
            if (testContainerService != null) {
                testContainerService.copyDataFiles(newItem, sourceNode.getId());
                testContainerService.copyTestCase(newItem, sourceNode.getObject().getProperty().getItem(), path, null, false);
            }
        }
    }

    public IPath getTestCasePath(Item newItem, RepositoryNode sourceNode) {
        StringBuffer pathName = new StringBuffer();
        if (sourceNode.getObjectType() != null) {
            pathName.append(sourceNode.getObjectType().getFolder());
        } else {
            pathName.append("process");
        }
        pathName.append(File.separator).append(newItem.getProperty().getId());
        final Path path = new Path(pathName.toString());
        return path;
    }

    public boolean isAllowedToCopyTestCase(Item newItem, RepositoryNode sourceNode) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class)) {
            ITestContainerProviderService testContainerService = (ITestContainerProviderService) GlobalServiceRegister
                    .getDefault().getService(ITestContainerProviderService.class);
            if (testContainerService != null) {
                if (!testContainerService.isDuplicateTestCaseOptionSelected()) {
                    return false;
                }
            }
        }
        if (sourceNode.getObjectType() != ERepositoryObjectType.PROCESS
                && sourceNode.getObjectType() != ERepositoryObjectType.PROCESS_MR
                && sourceNode.getObjectType() != ERepositoryObjectType.PROCESS_STORM) {
            return false;
        }
        if (!(newItem instanceof ProcessItem)) {
            return false;
        }
        return true;
    }

    private String getLastestVersion(Set<IRepositoryViewObject> set) {
        if (set.isEmpty()) {
            return null;
        }
        String version = null;
        for (IRepositoryViewObject obj : set) {
            String curVersion = obj.getProperty().getVersion();
            if (version == null) {
                version = curVersion;
            } else {
                Double dVersion = Double.valueOf(version);
                Double dCurVersion = Double.valueOf(curVersion);
                if (dCurVersion > dVersion) {
                    version = curVersion;
                }
            }
        }
        return version;
    }

    private void synDuplicatedRoutine(RoutineItem item, String oldLabel) {
        ICodeGeneratorService codeGenService = (ICodeGeneratorService) GlobalServiceRegister.getDefault().getService(
                ICodeGeneratorService.class);
        if (codeGenService != null) {
            if (item instanceof PigudfItem) {
                codeGenService.createRoutineSynchronizer().renamePigudfClass((PigudfItem) item, oldLabel);
            } else {
                codeGenService.createRoutineSynchronizer().renameRoutineClass(item);
            }
            try {
                codeGenService.createRoutineSynchronizer().syncRoutine(item, true);
            } catch (SystemException e) {
                ExceptionHandler.process(e);
            }
        }
    }

    private void synDuplicatedPigudf(PigudfItem item, String oldLabel) {
        ICodeGeneratorService codeGenService = (ICodeGeneratorService) GlobalServiceRegister.getDefault().getService(
                ICodeGeneratorService.class);
        if (codeGenService != null) {
            codeGenService.createRoutineSynchronizer().renamePigudfClass(item, oldLabel);
            try {
                codeGenService.createRoutineSynchronizer().syncRoutine(item, true);
            } catch (SystemException e) {
                ExceptionHandler.process(e);
            }
        }
    }

    private boolean isHadoopClusterItem(Item item) {
        IHadoopClusterService hadoopClusterService = HadoopRepositoryUtil.getHadoopClusterService();
        return hadoopClusterService != null && hadoopClusterService.isHadoopClusterItem(item);
    }

    public void copyHadoopClusterItem(final Item item, final IPath path) throws PersistenceException, BusinessException {
        IHadoopClusterService hadoopClusterService = HadoopRepositoryUtil.getHadoopClusterService();
        if (hadoopClusterService != null) {
            hadoopClusterService.copyHadoopCluster(item, path);
        }
    }

}
