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
package org.talend.designer.maven.tools.creator;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.maven.model.Model;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.internal.Messages;
import org.eclipse.m2e.core.internal.project.ProjectConfigurationManager;
import org.eclipse.osgi.util.NLS;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.general.TalendJobNature;
import org.talend.core.runtime.projectsetting.IProjectSettingTemplateConstants;
import org.talend.designer.maven.model.MavenSystemFolders;
import org.talend.designer.maven.model.ProjectSystemFolder;
import org.talend.designer.maven.model.TalendJavaProjectConstants;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.tools.AggregatorPomsHelper;
import org.talend.designer.maven.utils.MavenProjectUtils;
import org.talend.designer.maven.utils.PomIdsHelper;

/**
 * created by ggu on 22 Jan 2015 Detailled comment
 *
 */
public class CreateMavenCodeProject extends AbstractMavenGeneralTemplatePom {

    public static final String IS_ALREADY_SET_ECLIPSE_COMPLIANCE = "IS_ALREADY_SET_ECLIPSE_COMPLIANCE"; //$NON-NLS-1$

    private IProject project;

    private IFile pomFile;

    private IPath location;

    private boolean enbleMavenNature;

    public CreateMavenCodeProject(IProject project) {
        this(project, true);
    }

    public CreateMavenCodeProject(IProject project, boolean enbleMavenNature) {
        super(project.getFile(TalendMavenConstants.POM_FILE_NAME), IProjectSettingTemplateConstants.PROJECT_TEMPLATE_FILE_NAME);
        Assert.isNotNull(project);
        this.project = project;
        this.enbleMavenNature = enbleMavenNature;
    }

    public IProject getProject() {
        return this.project;
    }

    @Override
    protected Model createModel() {
        // temp model.
        Model templateModel = new Model();
        templateModel.setModelVersion("4.0.0"); //$NON-NLS-1$
        templateModel.setGroupId("org.talend.temp.job"); //$NON-NLS-1$
        templateModel.setArtifactId(TalendJavaProjectConstants.TEMP_POM_ARTIFACT_ID);
        templateModel.setVersion(PomIdsHelper.getProjectVersion());
        templateModel.setPackaging(TalendMavenConstants.PACKAGING_JAR);
        return templateModel;
    }

    /**
     * 
     * By default, it's current workspace.
     * 
     */
    protected IPath getBaseLocation() {
        return ResourcesPlugin.getWorkspace().getRoot().getLocation();
    }

    public void setProjectLocation(IPath location) {
        this.location = location;
    }

    public void setPomFile(IFile pomFile) {
        this.pomFile = pomFile;
    }

    /**
     * 
     * By default, create the all maven folders.
     * 
     */
    protected String[] getFolders() {
        ProjectSystemFolder[] mavenDirectories = MavenSystemFolders.ALL_DIRS;

        String[] directories = new String[mavenDirectories.length];
        for (int i = 0; i < directories.length; i++) {
            directories[i] = mavenDirectories[i].getPath();
        }

        return directories;
    }

    /**
     * 
     * can do something before create operation.
     */
    protected void beforeCreate(IProgressMonitor monitor, IResource res) throws Exception {
        //
    }

    /**
     * 
     * after create operation, can do something, like add some natures.
     */
    protected void afterCreate(IProgressMonitor monitor, IResource res) throws Exception {
        IProject p = res.getProject();
        if (!p.isOpen()) {
            p.open(monitor);
        }
        addTalendNature(p, TalendJobNature.ID, monitor);
        // convertJavaProjectToPom(monitor, p);
        AggregatorPomsHelper.addToParentModules(pomFile);
    }

    @Override
    public void create(IProgressMonitor monitor) throws Exception {
        IProgressMonitor pMoniter = monitor;
        if (monitor == null) {
            pMoniter = new NullProgressMonitor();
        }
        IProgressMonitor subMonitor = new SubProgressMonitor(pMoniter, 100);

        IProject p = ResourcesPlugin.getWorkspace().getRoot().getProject(project.getName());

        beforeCreate(subMonitor, p);

        subMonitor.worked(10);

        createSimpleProject(subMonitor, p);

        subMonitor.worked(80);

        afterCreate(subMonitor, p);

        project = p;

        subMonitor.done();
    }

    public static void addTalendNature(IProject project, String natureId, IProgressMonitor monitor) throws CoreException {
        if (!project.hasNature(natureId)) {
            IProjectDescription description = project.getDescription();
            String[] prevNatures = description.getNatureIds();
            String[] newNatures = new String[prevNatures.length + 1];
            System.arraycopy(prevNatures, 0, newNatures, 1, prevNatures.length);
            newNatures[0] = natureId;
            description.setNatureIds(newNatures);
            project.setDescription(description, monitor);
        }
    }

    @SuppressWarnings("restriction")
    private void createSimpleProject(IProgressMonitor monitor, IProject p) throws CoreException {
        final String[] directories = getFolders();

        String projectName = p.getName();
        monitor.beginTask(NLS.bind(Messages.ProjectConfigurationManager_task_creating, projectName), 5);

        monitor.subTask(Messages.ProjectConfigurationManager_task_creating_workspace);
        IProjectDescription description = ResourcesPlugin.getWorkspace().newProjectDescription(projectName);
        description.setLocation(location);
        p.create(description, monitor);
        p.open(monitor);
        monitor.worked(1);

        hideNestedProjectsFromParents(Collections.singletonList(p));

        monitor.worked(1);

        monitor.subTask(Messages.ProjectConfigurationManager_task_creating_pom);
        IFile pomFile = p.getFile(TalendMavenConstants.POM_FILE_NAME);
        if (!pomFile.exists()) {
            // always use temp model to avoid classpath problem?
            Model model = createModel();
            MavenPlugin.getMavenModelManager().createMavenModel(pomFile, model);
        }
        monitor.worked(1);

        monitor.subTask(Messages.ProjectConfigurationManager_task_creating_folders);
        for (int i = 0; i < directories.length; i++) {
            ProjectConfigurationManager.createFolder(p.getFolder(directories[i]), false);
        }
        monitor.worked(1);

        monitor.subTask(Messages.ProjectConfigurationManager_task_creating_project);

        if (enbleMavenNature) {
            MavenProjectUtils.enableMavenNature(monitor, p);
        }
        monitor.worked(1);

        if (this.pomFile == null) {
            this.pomFile = pomFile;
        }
    }

    private void hideNestedProjectsFromParents(List<IProject> projects) {

        if (!MavenPlugin.getMavenConfiguration().isHideFoldersOfNestedProjects()) {
            return;
        }

        // Prevent child project folders from showing up in parent project folders.

        HashMap<File, IProject> projectFileMap = new HashMap<File, IProject>();

        for (IProject project : projects) {
            projectFileMap.put(project.getLocation().toFile(), project);
        }
        for (IProject project : projects) {
            File projectFile = project.getLocation().toFile();
            IProject physicalParentProject = projectFileMap.get(projectFile.getParentFile());
            if (physicalParentProject == null) {
                continue;
            }
            IFolder folder = physicalParentProject.getFolder(projectFile.getName());
            if (folder.exists()) {
                try {
                    folder.setHidden(true);
                } catch (Exception ex) {
                    // log.error("Failed to hide resource; " + resource.getLocation().toOSString(), ex);
                    ExceptionHandler.process(ex);
                }
            }
        }
    }

}
