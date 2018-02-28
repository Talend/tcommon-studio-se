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
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.m2e.core.MavenPlugin;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.ProcessUtils;
import org.talend.core.model.properties.ProjectReference;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.utils.ItemResourceUtil;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.core.runtime.process.TalendProcessArgumentConstant;
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

    public void createRootPom(IFolder folder, IProgressMonitor monitor) throws Exception {
        IFile pomFile = folder.getFile(TalendMavenConstants.POM_FILE_NAME);
        if (!pomFile.exists()) {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put(MavenTemplateManager.KEY_PROJECT_NAME, projectTechName);
            Model model = MavenTemplateManager.getCodeProjectTemplateModel(parameters);
            PomUtil.savePom(monitor, model, pomFile);
        }
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

    public static void updateCodeProjects(IProgressMonitor monitor) {
        RepositoryWorkUnit workUnit = new RepositoryWorkUnit<Object>("update code project") { //$NON-NLS-1$

            @Override
            protected void run() {
                updateCodeProject(monitor, ERepositoryObjectType.ROUTINES);
                if (ProcessUtils.isRequiredPigUDFs(null)) {
                    updateCodeProject(monitor, ERepositoryObjectType.PIG_UDF);
                }
                if (ProcessUtils.isRequiredBeans(null)) {
                    updateCodeProject(monitor, ERepositoryObjectType.valueOf("BEANS")); //$NON-NLS-1$
                }
            }
        };
        workUnit.setAvoidUnloadResources(true);
        ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(workUnit);
    }

    private static void updateCodeProject(IProgressMonitor monitor, ERepositoryObjectType codeType) {
        try {
            ITalendProcessJavaProject codeProject = getCodesProject(codeType);
            updateCodeProjectPom(monitor, codeType, codeProject.getProjectPom());
            buildAndInstallCodesProject(monitor, codeType);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    public static void updateCodeProjectPom(IProgressMonitor monitor, ERepositoryObjectType type, IFile pomFile)
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

    public static void createRoutinesPom(IFile pomFile, IProgressMonitor monitor) throws Exception {
        CreateMavenRoutinePom createTemplatePom = new CreateMavenRoutinePom(pomFile);
        createTemplatePom.create(monitor);
    }

    public static void createPigUDFsPom(IFile pomFile, IProgressMonitor monitor) throws Exception {
        CreateMavenPigUDFPom createTemplatePom = new CreateMavenPigUDFPom(pomFile);
        createTemplatePom.create(monitor);
    }

    public static void createBeansPom(IFile pomFile, IProgressMonitor monitor) throws Exception {
        CreateMavenBeanPom createTemplatePom = new CreateMavenBeanPom(pomFile);
        createTemplatePom.create(monitor);
    }

    public static void buildAndInstallCodesProject(IProgressMonitor monitor, ERepositoryObjectType codeType) throws Exception {
        buildAndInstallCodesProject(monitor, codeType, true, false);
    }

    public static void buildAndInstallCodesProject(IProgressMonitor monitor, ERepositoryObjectType codeType, boolean install, boolean forceBuild)
            throws Exception {
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

    private static void build(ERepositoryObjectType codeType, boolean install, boolean forceBuild, IProgressMonitor monitor) throws Exception {
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
        RepositoryWorkUnit workUnit = new RepositoryWorkUnit<Object>("update ref project modules in project pom") { //$NON-NLS-1$

            @Override
            protected void run() {
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
        };
        workUnit.setAvoidUnloadResources(true);
        ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(workUnit);
    }

    public static void addToParentModules(IFile pomFile) throws Exception {
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
            if (!parentPomFolder.exists()) {
                if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
                    IRunProcessService runProcessService = (IRunProcessService) GlobalServiceRegister.getDefault()
                            .getService(IRunProcessService.class);
                    runProcessService.initMavenJavaProject(new NullProgressMonitor(),
                            ProjectManager.getInstance().getCurrentProject());
                }
            }
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

    public static String getJobProjectId(Property property) {
        String _projectTechName = ProjectManager.getInstance().getProject(property).getTechnicalLabel();
        return getJobProjectId(_projectTechName, property.getId(), property.getVersion());
    }

    public static void checkJobPomCreation(ITalendProcessJavaProject jobProject) throws CoreException {
        Model model = MavenPlugin.getMavenModelManager().readMavenModel(jobProject.getProjectPom());
        boolean useTempPom = TalendJavaProjectConstants.TEMP_POM_ARTIFACT_ID.equals(model.getArtifactId());
        jobProject.setUseTempPom(useTempPom);
    }

}
