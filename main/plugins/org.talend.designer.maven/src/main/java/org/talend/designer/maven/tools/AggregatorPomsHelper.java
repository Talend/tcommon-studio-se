// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.m2e.core.MavenPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.core.runtime.process.TalendProcessArgumentConstant;
import org.talend.designer.core.ICamelDesignerCoreService;
import org.talend.designer.maven.launch.MavenPomCommandLauncher;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.template.MavenTemplateManager;
import org.talend.designer.maven.tools.creator.CreateMavenBeanPom;
import org.talend.designer.maven.tools.creator.CreateMavenPigUDFPom;
import org.talend.designer.maven.tools.creator.CreateMavenRoutinePom;
import org.talend.designer.maven.utils.PomIdsHelper;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.repository.ProjectManager;

/**
 * DOC zwxue class global comment. Detailled comment
 */
public class AggregatorPomsHelper {

    private Project project;

    public AggregatorPomsHelper(Project project) {
        this.project = project;
    }

    public void createRootPom(IFolder folder, IProgressMonitor monitor) throws Exception {
        IFile pomFile = folder.getFile(TalendMavenConstants.POM_FILE_NAME);
        if (!pomFile.exists()) {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put(MavenTemplateManager.KEY_PROJECT_NAME, project.getTechnicalLabel());
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

    public boolean isRootPomInstalled() {
        return isPomInstalled(PomIdsHelper.getProjectGroupId(project), PomIdsHelper.getProjectArtifactId(),
                PomIdsHelper.getProjectVersion(project));
    }

    public boolean isPomInstalled(String groupId, String artifactId, String version) {
        String mvnUrl = MavenUrlHelper.generateMvnUrl(groupId, artifactId, version, MavenConstants.PACKAGING_POM, null);
        return PomUtil.isAvailable(mvnUrl);
    }

    public IFolder getProjectPomsFolder() {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        return workspace.getRoot().getFolder(new Path(project.getTechnicalLabel() + "/" + DIR_POMS)); //$NON-NLS-1$
    }

    public void createAggregatorFolderPom(IFolder folder, String folderName, String groupId, IProgressMonitor monitor)
            throws Exception {
        if (folder != null) {
            IFile pomFile = folder.getFile(TalendMavenConstants.POM_FILE_NAME);
            Model model = MavenTemplateManager.getAggregatorFolderTemplateModel(pomFile, groupId, folderName,
                    project.getTechnicalLabel());
            PomUtil.savePom(monitor, model, pomFile);
        }
    }

    public static void updatePomIfCreate(IProgressMonitor monitor, IFile pomFile, Property property) {
        if (pomFile != null && pomFile.exists()) {
            Dependency dependency = null;
            String projectTechName = ProjectManager.getInstance().getCurrentProject().getTechnicalLabel();
            String codeVersion = PomIdsHelper.getCodesVersion();
            Item item = property.getItem();
            if (item instanceof ProcessItem) {
                String routinesGroupId = PomIdsHelper.getCodesGroupId(projectTechName, TalendMavenConstants.DEFAULT_CODE);
                String routinesArtifactId = TalendMavenConstants.DEFAULT_ROUTINES_ARTIFACT_ID;
                dependency = PomUtil.createDependency(routinesGroupId, routinesArtifactId, codeVersion, null);
            } else {
                if (GlobalServiceRegister.getDefault().isServiceRegistered(ICamelDesignerCoreService.class)) {
                    ICamelDesignerCoreService service = (ICamelDesignerCoreService) GlobalServiceRegister.getDefault()
                            .getService(ICamelDesignerCoreService.class);
                    if (service.isInstanceofCamelRoutes(item)) {
                        String beansGroupId = PomIdsHelper.getCodesGroupId(projectTechName, TalendMavenConstants.DEFAULT_BEAN);
                        String beansArtifactId = TalendMavenConstants.DEFAULT_BEANS_ARTIFACT_ID;
                        dependency = PomUtil.createDependency(beansGroupId, beansArtifactId, codeVersion, null);
                    }
                }
            }
            if (dependency != null) {
                try {
                    Model model = MavenPlugin.getMavenModelManager().readMavenModel(pomFile);
                    List<Dependency> dependencies = model.getDependencies();
                    if (dependencies == null) {
                        dependencies = new ArrayList<>();
                        model.setDependencies(dependencies);
                    }
                    dependencies.add(dependency);
                    PomUtil.savePom(monitor, model, pomFile);
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }

            }
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

    public void createUserDefinedFolderPom(IFile pomFile, String folderName, String groupId, IProgressMonitor monitor) {
        // TODO get model like createAggregatorFolderPom(), but calculate parent's relative path.
        // PomUtil.getPomRelativePath(container, baseFolder);
    }

    public static void addToParentModules(IFile pomFile) throws Exception {
        File parentPom = getParentModulePomFile(pomFile);
        if (parentPom != null) {
            IPath relativePath = pomFile.getLocation().makeRelativeTo(new Path(parentPom.getParentFile().getAbsolutePath()));
            Model model = MavenPlugin.getMaven().readModel(parentPom);
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
        File parentPom = getParentModulePomFile(pomFile);
        if (parentPom != null) {
            IPath relativePath = pomFile.getLocation().makeRelativeTo(new Path(parentPom.getParentFile().getAbsolutePath()));
            Model model = MavenPlugin.getMaven().readModel(parentPom);
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

    private static File getParentModulePomFile(IFile pomFile) {
        File parentPom = null;
        if (pomFile == null || pomFile.getParent() == null || pomFile.getParent().getParent() == null) {
            return null;
        }
        if (pomFile.getParent().getName().equals(TalendMavenConstants.PROJECT_NAME)) {
            // ignore .Java project
            return null;
        }
        File pom = pomFile.getLocation().toFile();
        File parentPomFolder = null;
        if (pom.getParentFile() != null) {
            parentPomFolder = pom.getParentFile().getParentFile();
        }
        if (parentPomFolder != null) {
            for (File file : parentPomFolder.listFiles()) {
                if (file.getName().equals(TalendMavenConstants.POM_FILE_NAME)) {
                    parentPom = file;
                    break;
                }
            }
        }
        return parentPom;
    }

    public void refreshAggregatorFolderPom(IFile pomFile) throws Exception {
        boolean isModified = false;
        File pom = pomFile.getLocation().toFile();
        Model model = MavenPlugin.getMaven().readModel(pom);
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

}
