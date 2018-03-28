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
package org.talend.designer.maven.tools;

import static org.talend.designer.maven.model.TalendJavaProjectConstants.*;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.maven.model.Model;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.ProcessUtils;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProjectReference;
import org.talend.core.model.properties.Property;
import org.talend.core.model.relationship.Relation;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.utils.ItemResourceUtil;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.core.runtime.process.TalendProcessArgumentConstant;
import org.talend.core.runtime.services.IFilterService;
import org.talend.core.ui.ITestContainerProviderService;
import org.talend.designer.core.ICamelDesignerCoreService;
import org.talend.designer.maven.DesignerMavenPlugin;
import org.talend.designer.maven.launch.MavenPomCommandLauncher;
import org.talend.designer.maven.model.TalendJavaProjectConstants;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.template.MavenTemplateManager;
import org.talend.designer.maven.tools.creator.CreateMavenBeanPom;
import org.talend.designer.maven.tools.creator.CreateMavenPigUDFPom;
import org.talend.designer.maven.tools.creator.CreateMavenRoutinePom;
import org.talend.designer.maven.utils.PomIdsHelper;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.ProjectManager;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.repository.model.RepositoryConstants;

/**
 * DOC zwxue class global comment. Detailled comment
 */
public class AggregatorPomsHelper {

    private String projectTechName;

    public AggregatorPomsHelper() {
        projectTechName = ProjectManager.getInstance().getCurrentProject().getTechnicalLabel();
    }

    public AggregatorPomsHelper(String projectTechName) {
        this.projectTechName = projectTechName;
    }

    public void createRootPom(IFolder folder, List<String> modules, boolean force, IProgressMonitor monitor)
            throws Exception {
        IFile pomFile = folder.getFile(TalendMavenConstants.POM_FILE_NAME);
        if (force || !pomFile.exists()) {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put(MavenTemplateManager.KEY_PROJECT_NAME, projectTechName);
            Model model = MavenTemplateManager.getCodeProjectTemplateModel(parameters);
            if (modules != null && !modules.isEmpty()) {
                model.setModules(modules);
            }
            PomUtil.savePom(monitor, model, pomFile);
        }
    }

    public void createRootPom(IFolder folder, IProgressMonitor monitor) throws Exception {
        createRootPom(folder, null, false, monitor);
    }

    public void installRootPom(boolean current) throws Exception {
        IFile pomFile = getProjectPomsFolder().getFile(TalendMavenConstants.POM_FILE_NAME);
        installPom(pomFile, current);
    }

    public void installPom(IFile pomFile, boolean current) throws Exception {
        Model model = MavenPlugin.getMaven().readModel(pomFile.getLocation().toFile());
        if (!isPomInstalled(model.getGroupId(), model.getArtifactId(), model.getVersion())) {
            MavenPomCommandLauncher launcher = new MavenPomCommandLauncher(pomFile, TalendMavenConstants.GOAL_INSTALL);
            if (current) {
                Map<String, Object> argumentsMap = new HashMap<>();
                argumentsMap.put(TalendProcessArgumentConstant.ARG_PROGRAM_ARGUMENTS, "-N"); // $NON-NLS-N$
                launcher.setArgumentsMap(argumentsMap);
            }
            launcher.execute(new NullProgressMonitor());
        }
    }

    public boolean isPomInstalled(String groupId, String artifactId, String version) {
        String mvnUrl = MavenUrlHelper.generateMvnUrl(groupId, artifactId, version, MavenConstants.PACKAGING_POM, null);
        return PomUtil.isAvailable(mvnUrl);
    }

    public IFolder getProjectPomsFolder() {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        return workspace.getRoot().getFolder(new Path(projectTechName + "/" + DIR_POMS)); //$NON-NLS-1$
    }

    public void createAggregatorFolderPom(IFolder folder, String folderName, String groupId, IProgressMonitor monitor)
            throws Exception {
        if (folder != null) {
            IFile pomFile = folder.getFile(TalendMavenConstants.POM_FILE_NAME);
            Model model = MavenTemplateManager.getAggregatorFolderTemplateModel(pomFile, groupId, folderName, projectTechName);
            PomUtil.savePom(monitor, model, pomFile);
        }
    }

    @Deprecated
    public IFolder getDeploymentsFolder() {
        return getProjectPomsFolder().getFolder(DIR_AGGREGATORS);
    }

    private static ITalendProcessJavaProject getCodesProject(ERepositoryObjectType codeType) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            IRunProcessService runProcessService = (IRunProcessService) GlobalServiceRegister.getDefault()
                    .getService(IRunProcessService.class);
            return runProcessService.getTalendCodeJavaProject(codeType);
        }
        return null;
    }

    public void updateCodeProjects(IProgressMonitor monitor) {
        updateCodeProjects(monitor, false);
    }

    public void updateCodeProjects(IProgressMonitor monitor, boolean forceBuild) {
        RepositoryWorkUnit workUnit = new RepositoryWorkUnit<Object>("update code project") { //$NON-NLS-1$

            @Override
            protected void run() {
                updateCodeProject(monitor, ERepositoryObjectType.ROUTINES, forceBuild);
                if (ProcessUtils.isRequiredPigUDFs(null)) {
                    updateCodeProject(monitor, ERepositoryObjectType.PIG_UDF, forceBuild);
                }
                if (ProcessUtils.isRequiredBeans(null)) {
                    updateCodeProject(monitor, ERepositoryObjectType.valueOf("BEANS"), forceBuild); //$NON-NLS-1$
                }
            }
        };
        workUnit.setAvoidUnloadResources(true);
        ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(workUnit);
    }

    private void updateCodeProject(IProgressMonitor monitor, ERepositoryObjectType codeType, boolean forceBuild) {
        try {
            ITalendProcessJavaProject codeProject = getCodesProject(codeType);
            updateCodeProjectPom(monitor, codeType, codeProject.getProjectPom());
            buildAndInstallCodesProject(monitor, codeType, true, forceBuild);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    public void updateCodeProjectPom(IProgressMonitor monitor, ERepositoryObjectType type, IFile pomFile)
            throws Exception {
        if (type != null) {
            if (ERepositoryObjectType.ROUTINES == type) {
                createRoutinesPom(pomFile, monitor);
            } else if (ERepositoryObjectType.PIG_UDF == type) {
                createPigUDFsPom(pomFile, monitor);
            } else {
                if (GlobalServiceRegister.getDefault().isServiceRegistered(ICamelDesignerCoreService.class)) {
                    ICamelDesignerCoreService service = (ICamelDesignerCoreService) GlobalServiceRegister.getDefault()
                            .getService(ICamelDesignerCoreService.class);
                    ERepositoryObjectType beanType = service.getBeansType();
                    if (beanType != null && beanType == type) {
                        createBeansPom(pomFile, monitor);
                    }
                }
            }
        }
    }

    public void createRoutinesPom(IFile pomFile, IProgressMonitor monitor) throws Exception {
        CreateMavenRoutinePom createTemplatePom = new CreateMavenRoutinePom(pomFile);
        createTemplatePom.setProjectName(projectTechName);
        createTemplatePom.create(monitor);
    }

    public void createPigUDFsPom(IFile pomFile, IProgressMonitor monitor) throws Exception {
        CreateMavenPigUDFPom createTemplatePom = new CreateMavenPigUDFPom(pomFile);
        createTemplatePom.setProjectName(projectTechName);
        createTemplatePom.create(monitor);
    }

    public void createBeansPom(IFile pomFile, IProgressMonitor monitor) throws Exception {
        CreateMavenBeanPom createTemplatePom = new CreateMavenBeanPom(pomFile);
        createTemplatePom.setProjectName(projectTechName);
        createTemplatePom.create(monitor);
    }

    public static void buildAndInstallCodesProject(IProgressMonitor monitor, ERepositoryObjectType codeType) throws Exception {
        buildAndInstallCodesProject(monitor, codeType, true, false);
    }

    public static void buildAndInstallCodesProject(IProgressMonitor monitor, ERepositoryObjectType codeType, boolean install,
            boolean forceBuild) throws Exception {
        if (forceBuild || !BuildCacheManager.getInstance().isCodesBuild(codeType)) {
            if (!CommonsPlugin.isHeadless()) {
                Job job = new Job("Install " + codeType.getLabel()) {

                    @Override
                    protected IStatus run(IProgressMonitor monitor) {
                        try {
                            build(codeType, install, forceBuild, monitor);
                            return org.eclipse.core.runtime.Status.OK_STATUS;
                        } catch (Exception e) {
                            return new org.eclipse.core.runtime.Status(IStatus.ERROR, DesignerMavenPlugin.PLUGIN_ID, 1,
                                    e.getMessage(), e);
                        }
                    }

                };
                job.setUser(false);
                job.setPriority(Job.INTERACTIVE);
                job.schedule();
            } else {
                synchronized (codeType) {
                    build(codeType, install, forceBuild, monitor);
                }
            }
        }
    }

    private static void build(ERepositoryObjectType codeType, boolean install, boolean forceBuild, IProgressMonitor monitor)
            throws Exception {
        if (forceBuild || !BuildCacheManager.getInstance().isCodesBuild(codeType)) {
            ITalendProcessJavaProject codeProject = getCodesProject(codeType);
            codeProject.buildModules(monitor, null, null);
            if (install) {
                Map<String, Object> argumentsMap = new HashMap<>();
                argumentsMap.put(TalendProcessArgumentConstant.ARG_GOAL, TalendMavenConstants.GOAL_INSTALL);
                argumentsMap.put(TalendProcessArgumentConstant.ARG_PROGRAM_ARGUMENTS, "-Dmaven.main.skip=true"); //$NON-NLS-1$
                codeProject.buildModules(monitor, null, argumentsMap);
                BuildCacheManager.getInstance().updateCodeLastBuildDate(codeType);
            }
        }
    }

    public void createUserDefinedFolderPom(IFile pomFile, String folderName, String groupId, IProgressMonitor monitor) {
        // TODO
    }

    public static void updateRefProjectModules(List<ProjectReference> references) {
        if (!needUpdateRefProjectModules()) {
            return;
        }
        try {
            List<String> modules = new ArrayList<>();
            for (ProjectReference reference : references) {
                String refProjectTechName = reference.getReferencedProject().getTechnicalLabel();
                String modulePath = "../../" + refProjectTechName + "/" + TalendJavaProjectConstants.DIR_POMS; //$NON-NLS-1$ //$NON-NLS-2$
                modules.add(modulePath);
            }

            Project mainProject = ProjectManager.getInstance().getCurrentProject();
            IFolder mainPomsFolder = new AggregatorPomsHelper(mainProject.getTechnicalLabel()).getProjectPomsFolder();
            IFile mainPomFile = mainPomsFolder.getFile(TalendMavenConstants.POM_FILE_NAME);

            Model model = MavenPlugin.getMavenModelManager().readMavenModel(mainPomFile);
            List<String> oldModules = model.getModules();
            if (oldModules == null) {
                oldModules = new ArrayList<>();
            }
            ListIterator<String> iterator = oldModules.listIterator();
            while (iterator.hasNext()) {
                String modulePath = iterator.next();
                if (modulePath.startsWith("../../")) { //$NON-NLS-1$
                    iterator.remove();
                }
            }
            oldModules.addAll(modules);

            PomUtil.savePom(null, model, mainPomFile);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    public static void addToParentModules(IFile pomFile, Property property) throws Exception {
        // Check relation for ESB service job, should not be added into main pom
        if (property != null) {
            List<Relation> relations = RelationshipItemBuilder.getInstance().getItemsRelatedTo(property.getId(),
                    property.getVersion(), RelationshipItemBuilder.JOB_RELATION);
            for (Relation relation : relations) {
                if (RelationshipItemBuilder.SERVICES_RELATION.equals(relation.getType())) {
                    return;
                }
            }
        }

        if (GlobalServiceRegister.getDefault().isServiceRegistered(IFilterService.class)) {
            IFilterService filterService =
                    (IFilterService) GlobalServiceRegister.getDefault().getService(IFilterService.class);
            if (property != null && !filterService.isFilterAccepted(property.getItem(), PomIdsHelper.getPomFilter())) {
                return;
            }
        }
		IFile parentPom = getParentModulePomFile(pomFile);
		if (parentPom != null) {
			IPath relativePath = pomFile.getParent().getLocation().makeRelativeTo(parentPom.getParent().getLocation());
			Model model = MavenPlugin.getMaven().readModel(parentPom.getContents());
			List<String> modules = model.getModules();
			if (modules == null) {
				modules = new ArrayList<>();
				model.setModules(modules);
			}
			if (!modules.contains(relativePath.toPortableString())) {
				modules.add(relativePath.toPortableString());
				PomUtil.savePom(null, model, parentPom);
			}
		}
    }

    public static void removeFromParentModules(IFile pomFile) throws Exception {
        IFile parentPom = getParentModulePomFile(pomFile);
        if (parentPom != null) {
            IPath relativePath = pomFile.getParent().getLocation().makeRelativeTo(parentPom.getParent().getLocation());
            Model model = MavenPlugin.getMaven().readModel(parentPom.getContents());
            List<String> modules = model.getModules();
            if (modules == null) {
                modules = new ArrayList<>();
                model.setModules(modules);
            }
            if (modules != null && modules.contains(relativePath.toPortableString())) {
                modules.remove(relativePath.toPortableString());
                PomUtil.savePom(null, model, parentPom);
            }
        }
    }

    private static IFile getParentModulePomFile(IFile pomFile) {
        IFile parentPom = null;
        if (pomFile == null || pomFile.getParent() == null || pomFile.getParent().getParent() == null) {
            return null;
        }
        if (pomFile.getParent().getName().equals(TalendMavenConstants.PROJECT_NAME)) {
            // ignore .Java project
            return null;
        }
        IContainer parentPomFolder = pomFile.getParent();
        int nb = 10;
        while (parentPomFolder != null && !parentPomFolder.getName().equals(RepositoryConstants.POMS_DIRECTORY)) {
            parentPomFolder = parentPomFolder.getParent();
            nb--;
            if (nb < 0) {
                // only to avoid infinite loop in case there is some folder issues (poms folder not found)
                return null;
            }
        }
        if (parentPomFolder != null) {
            try {
                for (IResource file : parentPomFolder.members()) {
                    if (file.getName().equals(TalendMavenConstants.POM_FILE_NAME)) {
                        parentPom = (IFile) file;
                        break;
                    }
                }
            } catch (CoreException e) {
                ExceptionHandler.process(e);
            }
        }
        return parentPom;
    }

    public void refreshAggregatorFolderPom(IFile pomFile) throws Exception {
        boolean isModified = false;
        Model model = MavenPlugin.getMaven().readModel(pomFile.getContents());
        List<String> modules = model.getModules();
        if (modules != null) {
            ListIterator<String> iterator = modules.listIterator();
            while (iterator.hasNext()) {
                String module = iterator.next();
                File modulePomFile = pomFile.getLocation().removeLastSegments(1).append(module).toFile();
                if (!modulePomFile.exists()) {
                    iterator.remove();
                    isModified = true;
                }
            }
            if (isModified) {
                PomUtil.savePom(null, model, pomFile);
            }
        }
    }

    public void createCIPom(IFile pomFile, IProgressMonitor monitor) throws Exception {
        Model model = new Model();
        model.setModelVersion("4.0.0"); //$NON-NLS-1$
        model.setGroupId(TalendMavenConstants.DEFAULT_GROUP_ID);
        model.setArtifactId("sources.generator"); //$NON-NLS-1$
        model.setVersion(PomIdsHelper.getProjectVersion());
        model.setPackaging(TalendMavenConstants.PACKAGING_POM);

        MavenTemplateManager.addCIBuilder(model);

        PomUtil.savePom(null, model, pomFile);
    }

    public IFile getProjectRootPom(Project project) {
        if (project == null) {
            project = ProjectManager.getInstance().getCurrentProject();
        }
        return getProjectPomsFolder().getFile(TalendMavenConstants.POM_FILE_NAME);
    }

    public IFolder getProcessesFolder() {
        return getProjectPomsFolder().getFolder(DIR_JOBS);
    }

    public IFolder getCodesFolder() {
        return getProjectPomsFolder().getFolder(DIR_CODES);
    }

    public IFolder getCodeFolder(ERepositoryObjectType codeType) {
        IFolder codesFolder = getCodesFolder();
        if (codeType == ERepositoryObjectType.ROUTINES) {
            return codesFolder.getFolder(DIR_ROUTINES);
        }
        if (codeType == ERepositoryObjectType.PIG_UDF) {
            return codesFolder.getFolder(DIR_PIGUDFS);
        }
        if (codeType == ERepositoryObjectType.valueOf("BEANS")) { //$NON-NLS-1$
            return codesFolder.getFolder(DIR_BEANS);
        }
        return null;
    }

    public IFolder getProcessFolder(ERepositoryObjectType type) {
        return getProcessesFolder().getFolder(type.getFolder());
    }

    public static IPath getJobProjectPath(Property property, String realVersion) {
        // without create/open project
        String projectTechName = ProjectManager.getInstance().getProject(property).getTechnicalLabel();
        String version = realVersion == null ? property.getVersion() : realVersion;
        IPath path = ItemResourceUtil.getItemRelativePath(property);
        IFolder processTypeFolder = new AggregatorPomsHelper(projectTechName)
                .getProcessFolder(ERepositoryObjectType.getItemType(property.getItem()));
        path = processTypeFolder.getLocation().append(path);
        path = path.append(AggregatorPomsHelper.getJobProjectFolderName(property.getLabel(), version));
        return path;
    }

    public String getJobProjectName(Property property) {
        return projectTechName + "_" + getJobProjectFolderName(property).toUpperCase(); //$NON-NLS-1$
    }

    public static String getJobProjectFolderName(Property property) {
        return getJobProjectFolderName(property.getLabel(), property.getVersion());
    }

    public static String getJobProjectFolderName(String label, String version) {
        return label.toLowerCase() + "_" + version; //$NON-NLS-1$
    }

    public static String getJobProjectId(String projectTechName, String id, String version) {
        return projectTechName + "|" + id + "|" + version; //$NON-NLS-1$ //$NON-NLS-2$
    }

    public static IFolder getItemPomFolder(Property property) throws Exception {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class)) {
            ITestContainerProviderService testContainerService = (ITestContainerProviderService) GlobalServiceRegister
                    .getDefault()
                    .getService(ITestContainerProviderService.class);
            if (testContainerService.isTestContainerItem(property.getItem())) {
                Item jobItem = testContainerService.getParentJobItem(property.getItem());
                if (jobItem != null) {
                    property = jobItem.getProperty();
                }
            }
        }
        String projectTechName = ProjectManager.getInstance().getProject(property).getTechnicalLabel();
        AggregatorPomsHelper helper = new AggregatorPomsHelper(projectTechName);
        IPath itemRelativePath = ItemResourceUtil.getItemRelativePath(property);
        String jobFolderName = AggregatorPomsHelper.getJobProjectFolderName(property);
        ERepositoryObjectType type = ERepositoryObjectType.getItemType(property.getItem());
        IFolder jobFolder = helper.getProcessFolder(type).getFolder(itemRelativePath).getFolder(jobFolderName);
        createFoldersIfNeeded(jobFolder);
        return jobFolder;
    }

    private static void createFoldersIfNeeded(IFolder folder) throws CoreException {
        if (!folder.exists()) {
            if (folder.getParent() instanceof IFolder) {
                createFoldersIfNeeded((IFolder) folder.getParent());
            }
            folder.create(true, true, null);
        }
    }

    public static String getJobProjectId(Property property) {
        String _projectTechName = ProjectManager.getInstance().getProject(property).getTechnicalLabel();
        return getJobProjectId(_projectTechName, property.getId(), property.getVersion());
    }

    public static String getCodeProjectId(ERepositoryObjectType codeType, String projectTechName) {
        return projectTechName + "|" + codeType.name(); //$NON-NLS-1$
    }

    public static void checkJobPomCreation(ITalendProcessJavaProject jobProject) throws CoreException {
        Model model = MavenPlugin.getMavenModelManager().readMavenModel(jobProject.getProjectPom());
        boolean useTempPom = TalendJavaProjectConstants.TEMP_POM_ARTIFACT_ID.equals(model.getArtifactId());
        jobProject.setUseTempPom(useTempPom);
    }
    
    public void syncAllPoms() throws Exception {
    	
        IRunnableWithProgress runnableWithProgress = new IRunnableWithProgress() {

            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                RepositoryWorkUnit<Object> workUnit = new RepositoryWorkUnit<Object>("Synchronize all poms") { //$NON-NLS-1$

                    @Override
                    protected void run() {
                        final IWorkspaceRunnable op = new IWorkspaceRunnable() {

                            @Override
                            public void run(final IProgressMonitor monitor) throws CoreException {
                                try {
                                    IRunProcessService runProcessService = getRunProcessService();
                                    List<IRepositoryViewObject> objects = new ArrayList<>();
                                    if (runProcessService != null) {
                                        for (ERepositoryObjectType type : ERepositoryObjectType.getAllTypesOfProcess2()) {
                                            objects.addAll(
                                                    ProxyRepositoryFactory.getInstance().getAll(type, true, true));
                                        }
                                    }
                                    BuildCacheManager.getInstance().clearAllCaches();
                                    int size = 3 + (objects == null ? 0 : objects.size());
                                    monitor.setTaskName("Synchronize all poms"); //$NON-NLS-1$
                                    monitor.beginTask("", size); //$NON-NLS-1$
                                    // codes pom
                                    monitor.subTask("Synchronize code poms"); //$NON-NLS-1$
                                    updateCodeProjects(monitor, true);
                                    monitor.worked(1);
                                    if (monitor.isCanceled()) {
                                        return;
                                    }
                                    // all jobs pom
                                    List<String> modules = new ArrayList<>();
                                    if (objects != null) {
                                    	IFilterService filterService = null;
                                    	if(GlobalServiceRegister.getDefault().isServiceRegistered(IFilterService.class)) {
                                    		filterService = (IFilterService) GlobalServiceRegister.getDefault().getService(IFilterService.class);
                                    	}
                                        String pomFilter = PomIdsHelper.getPomFilter();
                                        for (IRepositoryViewObject object : objects) {
                                            if (filterService != null) {
                                                if (!filterService.isFilterAccepted(object.getProperty().getItem(),
                                                        pomFilter)) {
                                                    continue;
                                                }
                                            }
                                            if (object.getProperty() != null
                                                    && object.getProperty().getItem() != null) {
                                                Item item = object.getProperty().getItem();
                                                if (ProjectManager.getInstance().isInCurrentMainProject(item)) {
                                                    monitor.subTask(
                                                            "Synchronize job pom: " + item.getProperty().getLabel() //$NON-NLS-1$
                                                                    + "_" + item.getProperty().getVersion()); //$NON-NLS-1$
                                                    runProcessService.generatePom(item);
                                                    IFile pomFile = getItemPomFolder(item.getProperty())
                                                            .getFile(TalendMavenConstants.POM_FILE_NAME);
                                                    modules.add(getModulePath(pomFile));
                                                }
                                            }
                                            monitor.worked(1);
                                            if (monitor.isCanceled()) {
                                                return;
                                            }
                                        }
                                    }
                                    // project pom
                                    monitor.subTask("Synchronize project pom"); //$NON-NLS-1$
                                    collectModules(modules);
                                    createRootPom(getProjectPomsFolder(), modules, true, monitor);
                                    monitor.worked(1);
                                    monitor.subTask("Install project pom"); //$NON-NLS-1$
                                    installRootPom(true);
                                    monitor.worked(1);
                                    if (monitor.isCanceled()) {
                                        return;
                                    }
                                    monitor.done();
                                } catch (Exception e) {
                                    ExceptionHandler.process(e);
                                }
                            }

                        };
                        IWorkspace workspace = ResourcesPlugin.getWorkspace();
                        try {
                            ISchedulingRule schedulingRule = workspace.getRoot();
                            // the update the project files need to be done in the workspace runnable to avoid
                            // all
                            // notification
                            // of changes before the end of the modifications.
                            workspace.run(op, schedulingRule, IWorkspace.AVOID_UPDATE, monitor);
                        } catch (CoreException e) {
                            ExceptionHandler.process(e);
                        }
                    }

                };
                workUnit.setAvoidUnloadResources(true);
                ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(workUnit);
            }
        };
        new ProgressMonitorDialog(Display.getDefault().getActiveShell()).run(true, true, runnableWithProgress);
    }

    public void syncJobPoms(List<Item> jobItems) throws Exception {
        IRunnableWithProgress runnableWithProgress = new IRunnableWithProgress() {

            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                RepositoryWorkUnit<Object> workUnit = new RepositoryWorkUnit<Object>("Synchronize job poms") { //$NON-NLS-1$

                    @Override
                    protected void run() {
                        final IWorkspaceRunnable op = new IWorkspaceRunnable() {

                            @Override
                            public void run(final IProgressMonitor monitor) throws CoreException {
                                try {
                                    monitor.setTaskName("Synchronize job poms"); //$NON-NLS-1$
                                    monitor.beginTask("", jobItems.size()); //$NON-NLS-1$
                                    IRunProcessService runProcessService = getRunProcessService();
                                    for (Item item : jobItems) {
                                        if (ProjectManager.getInstance().isInCurrentMainProject(item)) {
                                            monitor.subTask("Synchronize job pom: " + item.getProperty().getLabel() //$NON-NLS-1$
                                                    + "_" + item.getProperty().getVersion()); //$NON-NLS-1$
                                            runProcessService.generatePom(item);
                                        }
                                        monitor.worked(1);
                                        if (monitor.isCanceled()) {
                                            return;
                                        }
                                    }
                                    monitor.done();
                                } catch (Exception e) {
                                    ExceptionHandler.process(e);
                                }
                            }
                        };
                        IWorkspace workspace = ResourcesPlugin.getWorkspace();
                        try {
                            ISchedulingRule schedulingRule = workspace.getRoot();
                            // the update the project files need to be done in the workspace runnable to avoid
                            // all
                            // notification
                            // of changes before the end of the modifications.
                            workspace.run(op, schedulingRule, IWorkspace.AVOID_UPDATE, monitor);
                        } catch (CoreException e) {
                            ExceptionHandler.process(e);
                        }
                    }

                };
                workUnit.setAvoidUnloadResources(true);
                ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(workUnit);
            }
        };
        new ProgressMonitorDialog(Display.getDefault().getActiveShell()).run(true, true, runnableWithProgress);
    }

    private String getModulePath(IFile pomFile) {
        IFile parentPom = getProjectPomsFolder().getFile(TalendMavenConstants.POM_FILE_NAME);
        if (parentPom != null) {
            IPath relativePath = pomFile.getParent().getLocation().makeRelativeTo(parentPom.getParent().getLocation());
            return relativePath.toPortableString();
        }
        return null;
    }

    private void collectModules(List<String> modules) {
        IRunProcessService service = getRunProcessService();
        if (service != null) {
            modules.add(getModulePath(service.getTalendCodeJavaProject(ERepositoryObjectType.ROUTINES).getProjectPom()));
            if (ProcessUtils.isRequiredPigUDFs(null)) {
                modules.add(getModulePath(service.getTalendCodeJavaProject(ERepositoryObjectType.PIG_UDF).getProjectPom()));
            }
            if (ProcessUtils.isRequiredBeans(null)) {
                modules.add(getModulePath(service
                        .getTalendCodeJavaProject(ERepositoryObjectType.valueOf("BEANS")) //$NON-NLS-1$
                        .getProjectPom()));
            }
        }
        if (needUpdateRefProjectModules()) {
            List<ProjectReference> references = ProjectManager.getInstance().getCurrentProject().getProjectReferenceList(true);
            for (ProjectReference reference : references) {
                String refProjectTechName = reference.getReferencedProject().getTechnicalLabel();
                String modulePath = "../../" + refProjectTechName + "/" + TalendJavaProjectConstants.DIR_POMS; //$NON-NLS-1$ //$NON-NLS-2$
                modules.add(modulePath);
            }
        } else {
            Project mainProject = ProjectManager.getInstance().getCurrentProject();
            IFolder mainPomsFolder = new AggregatorPomsHelper(mainProject.getTechnicalLabel()).getProjectPomsFolder();
            IFile mainPomFile = mainPomsFolder.getFile(TalendMavenConstants.POM_FILE_NAME);
            try {
                Model model = MavenPlugin.getMavenModelManager().readMavenModel(mainPomFile);
                List<String> oldModules = model.getModules();
                if (oldModules != null) {
                    for (String modulePath : oldModules) {
                        if (modulePath.startsWith("../../")) { //$NON-NLS-1$
                            modules.add(modulePath);
                        }
                    }
                }
            } catch (CoreException e) {
                ExceptionHandler.process(e);
            }
        }
    }

    public static boolean needUpdateRefProjectModules() {
        try {
            boolean isLocalProject = ProxyRepositoryFactory.getInstance().isLocalConnectionProvider();
            boolean isOffline = false;
            if (!isLocalProject) {
                RepositoryContext repositoryContext = (RepositoryContext) CoreRuntimePlugin
                        .getInstance()
                        .getContext()
                        .getProperty(Context.REPOSITORY_CONTEXT_KEY);
                isOffline = repositoryContext.isOffline();
            }
            return !isLocalProject && !isOffline;
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
        return false;
    }

    private static IRunProcessService getRunProcessService() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            IRunProcessService runProcessService = (IRunProcessService) GlobalServiceRegister.getDefault()
                    .getService(IRunProcessService.class);
            return runProcessService;
        }
        return null;
    }

}
