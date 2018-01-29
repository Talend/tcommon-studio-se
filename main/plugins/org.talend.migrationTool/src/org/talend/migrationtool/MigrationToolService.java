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
package org.talend.migrationtool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.emf.provider.EmfResourcesFactoryReader;
import org.talend.commons.runtime.model.emf.provider.ResourceOption;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.exception.MessageBoxExceptionHandler;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ICoreService;
import org.talend.core.model.general.Project;
import org.talend.core.model.migration.IMigrationToolService;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.MigrationStatus;
import org.talend.core.model.properties.MigrationTask;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryObject;
import org.talend.core.model.utils.MigrationUtil;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.utils.RoutineUtils;
import org.talend.core.repository.utils.URIHelper;
import org.talend.designer.codegen.ICodeGeneratorService;
import org.talend.designer.codegen.ITalendSynchronizer;
import org.talend.migration.IMigrationTask;
import org.talend.migration.IMigrationTask.ExecutionResult;
import org.talend.migration.IProjectMigrationTask;
import org.talend.migration.IWorkspaceMigrationTask;
import org.talend.migrationtool.i18n.Messages;
import org.talend.migrationtool.model.GetTasksHelper;
import org.talend.migrationtool.model.summary.AlertUserOnLogin;
import org.talend.repository.ProjectManager;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;
import org.talend.utils.ProductVersion;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class MigrationToolService implements IMigrationToolService {

    private static Logger log = Logger.getLogger(MigrationToolService.class);

    private static final String RELATION_TASK = "org.talend.repository.model.migration.AutoUpdateRelationsMigrationTask"; //$NON-NLS-1$ 

    private List<IProjectMigrationTask> doneThisSession;

    private static String FULL_LOG_FILE = "migration.log"; //$NON-NLS-1$

    private boolean migrationOnNewProject = false;

    private String taskId;

    public MigrationToolService() {
        doneThisSession = new ArrayList<IProjectMigrationTask>();
    }

    private ICoreService getCoreService() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ICoreService.class)) {
            return (ICoreService) GlobalServiceRegister.getDefault().getService(ICoreService.class);
        }
        return null;
    }

    @Override
    public void executeMigrationTasksForImport(Project project, Item item, List<MigrationTask> migrationTasksToApply,
            final IProgressMonitor monitor) throws Exception {
        final ResourceOption migrationOption = ResourceOption.MIGRATION;
        try {
            EmfResourcesFactoryReader.INSTANCE.addOption(migrationOption, false);
            EmfResourcesFactoryReader.INSTANCE.addOption(migrationOption, true);

            delegateExecuteMigrationTasksForImport(project, item, migrationTasksToApply, monitor);
        } finally {
            EmfResourcesFactoryReader.INSTANCE.removOption(migrationOption, false);
            EmfResourcesFactoryReader.INSTANCE.removOption(migrationOption, true);
        }
    }

    private void delegateExecuteMigrationTasksForImport(Project project, Item item, List<MigrationTask> migrationTasksToApply,
            final IProgressMonitor monitor) throws Exception {
        if (item == null || migrationTasksToApply == null) {
            return;
        }

        String itemName = item.getProperty().getLabel();
        List<IProjectMigrationTask> toExecute = new ArrayList<IProjectMigrationTask>();
        for (MigrationTask task : migrationTasksToApply) {
            IProjectMigrationTask projectTask = GetTasksHelper.getInstance().getProjectTask(task.getId());
            if (projectTask == null) {
                log.warn(Messages.getString("MigrationToolService.taskNotExist", task.getId())); //$NON-NLS-1$
            } else if (!projectTask.isDeprecated()) {
                toExecute.add(projectTask);
            }

        }
        sortMigrationTasks(toExecute);
        ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        for (IProjectMigrationTask task : toExecute) {
            monitor.subTask(Messages.getString("MigrationToolService.taskMonitor", task.getName(), itemName)); //$NON-NLS-1$
            try {
                // in case the resource has been modified (see MergeTosMetadataMigrationTask for example)
                if ((item.getProperty().eResource() == null || item.eResource() == null)) {
                    Property updatedProperty = factory.reload(item.getProperty());
                    item = updatedProperty.getItem();
                }
                if (item != null) {
                    ExecutionResult executionResult = task.execute(project, item);
                    if (executionResult == ExecutionResult.FAILURE) {
                        log.warn(Messages.getString("MigrationToolService.itemLogWarn", itemName, task.getName())); //$NON-NLS-1$
                    }
                }
            } catch (Exception e) {
                log.warn(Messages.getString("MigrationToolService.itemLogException", itemName, task.getName()), e); //$NON-NLS-1$
                try {
                    factory.deleteObjectPhysical(new RepositoryObject(item.getProperty()));
                    break;// stop migrating the object it has be deleted
                } catch (PersistenceException e1) {
                    log.error(Messages.getString("MigrationToolService.itemDeleteException", itemName));
                }
            }
        }

        try {
            ICodeGeneratorService service = (ICodeGeneratorService) GlobalServiceRegister.getDefault().getService(
                    ICodeGeneratorService.class);
            ITalendSynchronizer routineSynchronizer = service.createJavaRoutineSynchronizer();
            if (item != null && item instanceof RoutineItem) {
                RoutineUtils.changeRoutinesPackage(item);
                RoutineItem routineItem = (RoutineItem) item;
                routineSynchronizer.forceSyncRoutine(routineItem);
                routineSynchronizer.syncRoutine(routineItem, true);
                routineSynchronizer.getFile(routineItem);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void executeMigrationTasksForLogon(final Project project, final boolean beforeLogon, final IProgressMonitor monitorWrap) {
        final ResourceOption migrationOption = ResourceOption.MIGRATION;
        try {
            EmfResourcesFactoryReader.INSTANCE.addOption(migrationOption, false);
            EmfResourcesFactoryReader.INSTANCE.addOption(migrationOption, true);

            delateExecuteMigrationTasksForLogon(project, beforeLogon, monitorWrap);
        } finally {
            EmfResourcesFactoryReader.INSTANCE.removOption(migrationOption, false);
            EmfResourcesFactoryReader.INSTANCE.removOption(migrationOption, true);
        }
    }

    private void delateExecuteMigrationTasksForLogon(final Project project, final boolean beforeLogon,
            final IProgressMonitor monitorWrap) {
        String taskDesc = "Migration tool: project [" + project.getLabel() + "] tasks"; //$NON-NLS-1$ //$NON-NLS-2$
        log.trace(taskDesc);

        final List<IProjectMigrationTask> toExecute = GetTasksHelper.getProjectTasks(beforeLogon);
        final List<MigrationTask> done = new ArrayList<MigrationTask>(project.getEmfProject().getMigrationTask());

        if (beforeLogon) {
            setMigrationOnNewProject(done.isEmpty());
        }
        sortMigrationTasks(toExecute);

        int nbMigrationsToDo = 0;
        for (IProjectMigrationTask task : toExecute) {
            MigrationTask mgTask = MigrationUtil.findMigrationTask(done, task);
            if (mgTask == null && !task.isDeprecated()) {
                nbMigrationsToDo++;
            }
        }

        // force to redo the migration task for the relations only if user ask for "clean" or if relations is empty
        // or if there is at least another migration to do.
        if (!beforeLogon
                && (!RelationshipItemBuilder.INDEX_VERSION.equals(project.getEmfProject().getItemsRelationVersion()) || nbMigrationsToDo > 0)) {
            // force to redo this migration task, to make sure the relationship is done correctly
            // done.remove(RELATION_TASK);
            MigrationUtil.removeMigrationTaskById(done, RELATION_TASK);
            RelationshipItemBuilder.getInstance().unloadRelations();

            // because will update for PROCESS and JOBLET
            RelationshipItemBuilder.getInstance().cleanTypeRelations(RelationshipItemBuilder.JOB_RELATION,
                    RelationshipItemBuilder.JOB_RELATION, false);
            RelationshipItemBuilder.getInstance().cleanTypeRelations(RelationshipItemBuilder.JOBLET_RELATION,
                    RelationshipItemBuilder.JOBLET_RELATION, true);
            // reset
            RelationshipItemBuilder.getInstance().unloadRelations();

            nbMigrationsToDo++;
        }
        if (nbMigrationsToDo == 0) {
            return;
        }

        // force execute migration in case user copy-past items with diffrent path on the file system and refresh
        // the studio,it may cause bug TDI-19229
        MigrationUtil.removeMigrationTaskById(done, "org.talend.repository.model.migration.FixProjectResourceLink");

        boolean haveAnyBinFolder = false; // to avoid some problems of migration, sometimes
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IProject fsProject = workspace.getRoot().getProject(project.getTechnicalLabel());
        for (ERepositoryObjectType type : (ERepositoryObjectType[]) ERepositoryObjectType.values()) {
            if (!type.hasFolder()) {
                continue;
            }
            String folderName = ERepositoryObjectType.getFolderName(type);
            if (folderName == null || "".equals(folderName)) {
                continue;
            }
            IFolder folder = fsProject.getFolder(folderName);
            if (folder.exists() && folder.getFolder("bin").exists()) { //$NON-NLS-1$
                haveAnyBinFolder = true;
                break;
            }
        }
        if (haveAnyBinFolder) {
            MigrationUtil.removeMigrationTaskById(done, "org.talend.repository.model.migration.RemoveBinFolderMigrationTask");
        }

        final SubProgressMonitor subProgressMonitor = new SubProgressMonitor(monitorWrap, toExecute.size());
        IRepositoryService service = (IRepositoryService) GlobalServiceRegister.getDefault().getService(IRepositoryService.class);
        final IProxyRepositoryFactory repFactory = service.getProxyRepositoryFactory();

        RepositoryWorkUnit repositoryWorkUnit = new RepositoryWorkUnit(project, taskDesc) {

            @Override
            public void run() throws PersistenceException {
                final IWorkspaceRunnable op = new IWorkspaceRunnable() {

                    @Override
                    public void run(IProgressMonitor monitor) throws CoreException {
                        if (!isMigrationOnNewProject() && beforeLogon) {
                            appendToLogFile(project, " ---=== Start Migration of project " + project.getLabel() + " ===---\n"); //$NON-NLS-1$//$NON-NLS-2$
                        }

                        try {
                            boolean needSave = false;
                            if (!isMigrationOnNewProject()) {
                                if (!beforeLogon) {
                                    ERepositoryObjectType[] types = (ERepositoryObjectType[]) ERepositoryObjectType.values();
                                    Arrays.sort(types, new Comparator<ERepositoryObjectType>() {

                                        @Override
                                        public int compare(ERepositoryObjectType arg0, ERepositoryObjectType arg1) {
                                            if (arg0 == ERepositoryObjectType.PROCESS) {
                                                return 1;
                                            }
                                            if (arg0 == ERepositoryObjectType.JOBLET) {
                                                return 1;
                                            }
                                            return 0;
                                        }
                                    });

                                    for (ERepositoryObjectType type : types) {
                                        if (!type.isResourceItem()) {
                                            continue;
                                        }
                                        List<IRepositoryViewObject> objects = repFactory.getAll(project, type, true, true);

                                        for (IRepositoryViewObject object : objects) {
                                            Item item = object.getProperty().getItem();
                                            monitorWrap.subTask("Migrate... " + item.getProperty().getLabel());
                                            boolean hadFailed = false;
                                            subProgressMonitor.worked(1);
                                            for (IProjectMigrationTask task : toExecute) {
                                                if (monitorWrap.isCanceled()) {
                                                    throw new OperationCanceledException(Messages.getString(
                                                            "MigrationToolService.migrationCancel", task.getName())); //$NON-NLS-1$
                                                }
                                                MigrationTask mgTask = MigrationUtil.findMigrationTask(done, task);
                                                if (mgTask == null && !task.isDeprecated()) {
                                                    try {
                                                        ExecutionResult status = task.execute(project, item);
                                                        switch (status) {
                                                        case SUCCESS_WITH_ALERT:
                                                            if (task.getStatus() != ExecutionResult.FAILURE) {
                                                                task.setStatus(status);
                                                            }
                                                            //$FALL-THROUGH$
                                                        case SUCCESS_NO_ALERT:
                                                            if (task.getStatus() != ExecutionResult.FAILURE) {
                                                                task.setStatus(status);
                                                            }
                                                            //$FALL-THROUGH$
                                                        case NOTHING_TO_DO:
                                                            if (task.getStatus() != ExecutionResult.SUCCESS_WITH_ALERT
                                                                    && task.getStatus() != ExecutionResult.SUCCESS_NO_ALERT
                                                                    && task.getStatus() != ExecutionResult.FAILURE) {
                                                                task.setStatus(status);
                                                            }
                                                            break;
                                                        case SKIPPED:
                                                            if (task.getStatus() != ExecutionResult.SUCCESS_WITH_ALERT
                                                                    && task.getStatus() != ExecutionResult.SUCCESS_NO_ALERT
                                                                    && task.getStatus() != ExecutionResult.FAILURE) {
                                                                task.setStatus(status);
                                                            }
                                                            break;
                                                        case FAILURE:
                                                            task.setStatus(status);
                                                            //$FALL-THROUGH$
                                                        default:
                                                            task.setStatus(status);
                                                            if (!isMigrationOnNewProject()) {
                                                                if (!hadFailed) {
                                                                    hadFailed = true;
                                                                    Property prop = object.getProperty();
                                                                    Resource resource = prop.eResource();
                                                                    String itemInfo = null;
                                                                    if (resource != null) {
                                                                        IPath path = URIHelper.convert(resource.getURI());
                                                                        if (path != null) {
                                                                            itemInfo = path.toPortableString();
                                                                        }
                                                                    }
                                                                    if (itemInfo == null) {
                                                                        itemInfo = prop.toString();
                                                                    }
                                                                    appendToLogFile(project,
                                                                            " * FAILED Task(s) on item: " + itemInfo + "\n"); //$NON-NLS-1$//$NON-NLS-2$ 
                                                                }
                                                                appendToLogFile(project, "      " + task.getName() + "\n"); //$NON-NLS-1$//$NON-NLS-2$ 
                                                            }

                                                            break;
                                                        }
                                                    } catch (Exception e) {
                                                        doneThisSession.add(task);
                                                        ExceptionHandler.process(e);
                                                        if (!isMigrationOnNewProject()) {
                                                            if (!hadFailed) {
                                                                hadFailed = true;
                                                                Property prop = object.getProperty();
                                                                Resource resource = prop.eResource();
                                                                String itemInfo = null;
                                                                if (resource != null) {
                                                                    IPath path = URIHelper.convert(resource.getURI());
                                                                    if (path != null) {
                                                                        itemInfo = path.toPortableString();
                                                                    }
                                                                }
                                                                if (itemInfo == null) {
                                                                    itemInfo = prop.toString();
                                                                }
                                                                appendToLogFile(project,
                                                                        " * FAILED Task(s) on item: " + itemInfo + "\n"); //$NON-NLS-1$//$NON-NLS-2$ 
                                                            }
                                                            appendToLogFile(project, "      " + task.getName() + "\n"); //$NON-NLS-1$//$NON-NLS-2$ 
                                                        }
                                                        log.debug("Task \"" + task.getName() + "\" failed"); //$NON-NLS-1$ //$NON-NLS-2$
                                                    }
                                                }
                                            }

                                            if (object instanceof RepositoryObject) {
                                                ((RepositoryObject) object).unload();
                                            }
                                        }
                                        monitorWrap.subTask(""); //$NON-NLS-1$
                                    }
                                }
                                for (IProjectMigrationTask task : toExecute) {
                                    MigrationTask mgTask = MigrationUtil.findMigrationTask(done, task);
                                    if (mgTask == null && !task.isDeprecated()) {
                                        try {
                                            ExecutionResult status;
                                            if (beforeLogon) {
                                                status = task.execute(project);
                                                task.setStatus(status);
                                            } else {
                                                status = task.getStatus();
                                            }
                                            switch (status) {
                                            case SUCCESS_WITH_ALERT:
                                                if (!isMigrationOnNewProject()) { // if it's a new project, no need to
                                                                                  // display any
                                                    // alert,
                                                    // since no real
                                                    // migration.
                                                    doneThisSession.add(task);
                                                }
                                                //$FALL-THROUGH$
                                            case SUCCESS_NO_ALERT:
                                                if (!isMigrationOnNewProject()) {
                                                    log.debug("Task \"" + task.getName() + "\" done"); //$NON-NLS-1$ //$NON-NLS-2$
                                                    appendToLogFile(project,
                                                            " * Task [" + task.getName() + "] : Applied successfully\n"); //$NON-NLS-1$//$NON-NLS-2$
                                                }
                                                //$FALL-THROUGH$
                                            case NOTHING_TO_DO:
                                                if (!isMigrationOnNewProject()
                                                        && task.getStatus() == ExecutionResult.NOTHING_TO_DO) {
                                                    appendToLogFile(project, " * Task [" + task.getName() + "] : Nothing to do\n"); //$NON-NLS-1$//$NON-NLS-2$
                                                }
                                                break;
                                            case SKIPPED:
                                                log.debug("Task \"" + task.getName() + "\" skipped"); //$NON-NLS-1$ //$NON-NLS-2$
                                                if (!isMigrationOnNewProject()) {
                                                    appendToLogFile(project, " * Task [" + task.getName() + "] : Skipped\n"); //$NON-NLS-1$//$NON-NLS-2$
                                                }
                                                break;
                                            case FAILURE:
                                                doneThisSession.add(task);
                                                //$FALL-THROUGH$
                                            default:
                                                log.debug("Task \"" + task.getName() + "\" failed"); //$NON-NLS-1$ //$NON-NLS-2$
                                                break;
                                            }
                                        } catch (Exception e) {
                                            doneThisSession.add(task);
                                            ExceptionHandler.process(e);
                                            log.debug("Task \"" + task.getName() + "\" failed"); //$NON-NLS-1$ //$NON-NLS-2$
                                        }
                                        done.add(MigrationUtil.convertMigrationTask(task));
                                    }
                                    needSave = true;
                                }
                            } else {
                                // new project
                                for (IProjectMigrationTask task : toExecute) {
                                    task.setStatus(ExecutionResult.NOTHING_TO_DO);
                                    done.add(MigrationUtil.convertMigrationTask(task));
                                }
                                needSave = true;
                            }
                            if (needSave) {
                                saveProjectMigrationTasksDone(project, done);
                            }
                            if (!RelationshipItemBuilder.INDEX_VERSION.equals(project.getEmfProject().getItemsRelationVersion())) {
                                project.getEmfProject().setItemsRelationVersion(RelationshipItemBuilder.INDEX_VERSION);
                            }
                            if (!isMigrationOnNewProject()) {
                                RelationshipItemBuilder.getInstance().saveRelations();
                            }
                        } catch (PersistenceException e) {
                            throw new CoreException(new Status(Status.ERROR, "org.talend.migrationTool", e.getMessage(), e)); //$NON-NLS-1$
                        }
                        if (!isMigrationOnNewProject() && !beforeLogon) {
                            appendToLogFile(project, " ---=== End of migration ===---\n"); //$NON-NLS-1$
                        }
                    }
                };
                try {
                    IWorkspace workspace1 = ResourcesPlugin.getWorkspace();
                    ISchedulingRule schedulingRule = workspace1.getRoot();
                    // the update the project files need to be done in the workspace runnable to
                    // avoid all notification of changes before the end of the modifications.
                    workspace1.run(op, schedulingRule, IWorkspace.AVOID_UPDATE, monitorWrap);
                } catch (CoreException e) {
                    throw new PersistenceException(e);
                }
            }
        };
        repositoryWorkUnit.setAvoidUnloadResources(true);
        repFactory.executeRepositoryWorkUnit(repositoryWorkUnit);
        if (!beforeLogon) {
            setMigrationOnNewProject(false);
        }
        // repositoryWorkUnit.throwPersistenceExceptionIfAny();
    }

    private void appendToLogFile(Project sourceProject, String logTxt) {
        IProject project = ProjectManager.getInstance().getResourceProject(sourceProject.getEmfProject());
        File fullLogFile = new File(project.getFile(FULL_LOG_FILE).getLocation().toPortableString());

        FileWriter writer = null;
        try {
            writer = new FileWriter(fullLogFile, true);
            writer.append(logTxt);
        } catch (IOException e) {
            // nothing
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
        }

    }

    @Override
    public boolean checkMigrationTasks(org.talend.core.model.properties.Project project) {
        EList<MigrationTask> migrationTasks = project.getMigrationTask();
        ProductVersion topTaskVersion = new ProductVersion(0, 0, 0);
        ProductVersion topTaskBreaks = new ProductVersion(0, 0, 0);
        for (MigrationTask task : migrationTasks) {
            IProjectMigrationTask productMigrationTask = GetTasksHelper.getInstance().getProjectTask(task.getId());
            if (productMigrationTask != null) { // If the the migration task already applyed before, ignore it.
                continue;
            }
            String version = task.getVersion();
            if (version == null) {
                log.warn(Messages.getString("MigrationToolService.taskVersionIsNull", task.getId())); //$NON-NLS-1$
            } else {
                ProductVersion taskVersion = ProductVersion.fromString(version);
                if (taskVersion.compareTo(topTaskVersion) > 0) {
                    topTaskVersion = taskVersion;
                    taskId = task.getId();
                }
            }
            String breaks = task.getBreaks();
            MigrationStatus status = task.getStatus();
            if (breaks == null) {
                log.warn(Messages.getString("MigrationToolService.taskBreaksIsNull", task.getId())); //$NON-NLS-1$
            } else if (status != MigrationStatus.NOIMPACT_LITERAL) {
                ProductVersion taskBreaks = ProductVersion.fromString(breaks);
                if (taskBreaks != null && taskBreaks.compareTo(topTaskBreaks) > 0) {
                    topTaskBreaks = taskBreaks;
                }
            }
        }
        ProductVersion productVersion = ProductVersion.fromString(VersionUtils.getTalendVersion());
        if (topTaskBreaks.compareTo(productVersion) >= 0) {
            int dataVersionMajor = topTaskVersion.getMajor();
            int dataVersionMinor = topTaskVersion.getMinor();
            int dataVersionSystem = topTaskVersion.getMicro();
            int productVersionMajor = productVersion.getMajor();
            int productVersionMinor = productVersion.getMinor();
            int productVersionSystem = productVersion.getMicro();
            if (dataVersionMajor == productVersionMajor && dataVersionMinor == productVersionMinor
                    && dataVersionSystem <= productVersionSystem) {
                return true;
            }
            log.warn((Messages.getString("MigrationToolService.projectCanNotOpen", taskId))); //$NON-NLS-1$
            return false;
        }

        return true;
    }

    @Override
    public void updateMigrationSystem(org.talend.core.model.properties.Project project, boolean persistence) {
        if (!MigrationUtil.containsTask(project.getMigrationTask(), MigrationUtil.ADAPT_NEW_MIGRATION_TASK_SYSTEM_ID)) {
            IProjectMigrationTask task = GetTasksHelper.getInstance().getProjectTask(
                    MigrationUtil.ADAPT_NEW_MIGRATION_TASK_SYSTEM_ID);
            task.execute(new org.talend.core.model.general.Project(project), persistence);
        }
    }

    private void sortMigrationTasks(List<? extends IMigrationTask> tasks) {
        Collections.sort(tasks, new Comparator<IMigrationTask>() {

            @Override
            public int compare(IMigrationTask t1, IMigrationTask t2) {
                return t1.getOrder().compareTo(t2.getOrder());
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.IMigrationToolService#initNewProjectTasks()
     */
    @Override
    public void initNewProjectTasks(Project project) {
        List<IProjectMigrationTask> toExecute = GetTasksHelper.getProjectTasks(true);
        toExecute.addAll(GetTasksHelper.getProjectTasks(false));
        List<MigrationTask> done = new ArrayList<MigrationTask>();

        for (IProjectMigrationTask task : toExecute) {
            done.add(MigrationUtil.convertMigrationTask(task));
        }

        project.getEmfProject().setItemsRelationVersion(RelationshipItemBuilder.INDEX_VERSION);
        saveProjectMigrationTasksDone(project, done);
    }

    /**
     * DOC smallet Comment method "saveProjectMigrationTasksDone".
     * 
     * @param project
     * @param done
     */
    private void saveProjectMigrationTasksDone(Project project, List<MigrationTask> done) {
        IRepositoryService service = (IRepositoryService) GlobalServiceRegister.getDefault().getService(IRepositoryService.class);
        IProxyRepositoryFactory repFactory = service.getProxyRepositoryFactory();
        try {
            repFactory.setMigrationTasksDone(project, done);
        } catch (PersistenceException e) {
            MessageBoxExceptionHandler.process(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.IMigrationToolService#executeWorspaceTasks()
     */
    @Override
    public void executeWorspaceTasks() {
        log.trace("Migration tool: workspace tasks"); //$NON-NLS-1$

        // PreferenceManipulator prefManipulator = new
        // PreferenceManipulator(CorePlugin.getDefault().getPreferenceStore());
        List<IWorkspaceMigrationTask> toExecute = GetTasksHelper.getWorkspaceTasks();
        final ICoreService coreService = getCoreService();
        if (coreService == null) {
            return;
        }
        // List<String> done = prefManipulator.readWorkspaceTasksDone();
        List<String> done = coreService.readWorkspaceTasksDone();

        // --------------------------------------------------------------------------------------------------
        // This code part aim is to know if we have a new workspace or one from an old Talend version:
        // --------------------------------------------------------------------------------------------------
        // String lastUser = prefManipulator.getLastUser();
        String lastUser = coreService.getLastUser();
        if (lastUser == null || lastUser.length() == 0) {
            if (done.isEmpty()) {
                // We are sure on a initialized or new workspace:
                initNewWorkspaceTasks();
                // done = prefManipulator.readWorkspaceTasksDone();
                done = coreService.readWorkspaceTasksDone();
            }
        }
        // --------------------------------------------------------------------------------------------------

        Collections.sort(toExecute, new Comparator<IWorkspaceMigrationTask>() {

            @Override
            public int compare(IWorkspaceMigrationTask o1, IWorkspaceMigrationTask o2) {
                return o1.getOrder().compareTo(o2.getOrder());
            }
        });

        for (IWorkspaceMigrationTask task : toExecute) {
            if (!done.contains(task.getId())) {
                if (task.execute()) {
                    // prefManipulator.addWorkspaceTaskDone(task.getId());
                    coreService.addWorkspaceTaskDone(task.getId());
                    log.debug("Task \"" + task.getName() + "\" done"); //$NON-NLS-1$ //$NON-NLS-2$
                } else {
                    log.debug("Task \"" + task.getName() + "\" failed"); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.IMigrationToolService#initNewWorkspaceTasks()
     */
    public void initNewWorkspaceTasks() {
        final ICoreService coreService = getCoreService();
        if (coreService == null) {
            return;
        }
        List<IWorkspaceMigrationTask> toExecute = GetTasksHelper.getWorkspaceTasks();
        // PreferenceManipulator prefManipulator = new
        // PreferenceManipulator(CorePlugin.getDefault().getPreferenceStore());
        for (IWorkspaceMigrationTask task : toExecute) {
            // prefManipulator.addWorkspaceTaskDone(task.getId());
            coreService.addWorkspaceTaskDone(task.getId());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.IMigrationToolService#executeMigration()
     */
    @Override
    public void executeMigration(boolean underPluginModel) {
        new AlertUserOnLogin().startup(underPluginModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.IMigrationToolService#needExecutemigration()
     */
    @Override
    public boolean needExecutemigration() {
        return !AlertUserOnLogin.executed;
    }

    public boolean isMigrationOnNewProject() {
        return migrationOnNewProject;
    }

    public void setMigrationOnNewProject(boolean migrationOnNewProject) {
        this.migrationOnNewProject = migrationOnNewProject;
    }

    /**
     * Getter for doneThisSession.
     * 
     * @return the doneThisSession
     */
    public List<IProjectMigrationTask> getDoneThisSession() {
        Comparator comparator = new Comparator<IProjectMigrationTask>() {

            @Override
            public int compare(IProjectMigrationTask o1, IProjectMigrationTask o2) {
                if (o1.getStatus() == ExecutionResult.FAILURE && o2.getStatus() == ExecutionResult.SUCCESS_WITH_ALERT) {
                    return -1;
                } else {
                    return 1;
                }
            }

        };
        Collections.sort(doneThisSession, comparator);
        return this.doneThisSession;
    }

    /**
     * Getter for taskId.
     * 
     * @return the taskId
     */
    @Override
    public String getTaskId() {
        return this.taskId;
    }

}
