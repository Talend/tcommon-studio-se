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
package org.talend.designer.maven.tools.creator;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.maven.model.Model;
import org.apache.maven.project.MavenProject;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;
import org.eclipse.m2e.core.embedder.MavenModelManager;
import org.eclipse.m2e.core.project.IProjectConfigurationManager;
import org.eclipse.m2e.core.project.ProjectImportConfiguration;
import org.osgi.service.prefs.BackingStoreException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.runtime.projectsetting.IProjectSettingTemplateConstants;
import org.talend.designer.maven.DesignerMavenPlugin;
import org.talend.designer.maven.model.MavenSystemFolders;
import org.talend.designer.maven.model.ProjectSystemFolder;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.template.MavenTemplateManager;
import org.talend.designer.maven.utils.PomUtil;

/**
 * created by ggu on 22 Jan 2015 Detailled comment
 *
 */
public class CreateMavenCodeProject extends AbstractMavenGeneralTemplatePom {

    public static final String IS_ALREADY_SET_ECLIPSE_COMPLIANCE = "IS_ALREADY_SET_ECLIPSE_COMPLIANCE"; //$NON-NLS-1$

    private IProject project;

    public CreateMavenCodeProject(IProject project) {
        super(project.getFile(TalendMavenConstants.POM_FILE_NAME), IProjectSettingTemplateConstants.PROJECT_TEMPLATE_FILE_NAME);
        Assert.isNotNull(project);
        this.project = project;
    }

    public IProject getProject() {
        return this.project;
    }

    @Override
    protected Model createModel() {
        Model templateModel = MavenTemplateManager.getCodeProjectTemplateModel();

        // The groupId and artifactId are temp, will change it after create project.
        setGroupId(project.getName());
        setArtifactId(project.getName());
        setVersion(templateModel.getVersion());
        // Must be jar, if pom, won't create the classpath or such for jdt.
        setPackaging(TalendMavenConstants.PACKAGING_JAR);

        setAttributes(templateModel);
        addProperties(templateModel);

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
        cleanLastUpdatedFiles();
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
        convertJavaProjectToPom(monitor, p);
        changeClasspath(monitor, p);

        IJavaProject javaProject = JavaCore.create(p);
        clearProjectIndenpendComplianceSettings(javaProject);
        // unregist listeners, release resources
        javaProject.close();
    }

    @Override
    public void create(IProgressMonitor monitor) throws Exception {
        IProgressMonitor pMoniter = monitor;
        if (monitor == null) {
            pMoniter = new NullProgressMonitor();
        }
        IProgressMonitor subMonitor = new SubProgressMonitor(pMoniter, 100);

        final Model model = createModel();
        final ProjectImportConfiguration importConfiguration = new ProjectImportConfiguration();
        final IProject p = importConfiguration.getProject(ResourcesPlugin.getWorkspace().getRoot(), model);
        final IPath location = getBaseLocation();
        final String[] folders = getFolders();

        beforeCreate(subMonitor, p);
        subMonitor.worked(10);

        IProjectConfigurationManager projectConfigurationManager = MavenPlugin.getProjectConfigurationManager();
        projectConfigurationManager.createSimpleProject(p, location.append(p.getName()), model, folders, importConfiguration,
                subMonitor);

        subMonitor.worked(80);

        afterCreate(subMonitor, p);
        subMonitor.done();

        // update the project
        this.project = p;
    }

    private void convertJavaProjectToPom(IProgressMonitor monitor, IProject p) {
        IFile pomFile = p.getFile(TalendMavenConstants.POM_FILE_NAME);
        if (pomFile.exists()) {
            try {
                MavenModelManager mavenModelManager = MavenPlugin.getMavenModelManager();
                MavenProject mavenProject = mavenModelManager.readMavenProject(pomFile, monitor);
                if (mavenProject != null) {
                    Model model = mavenProject.getOriginalModel();
                    // if not pom, change to pom
                    if (!TalendMavenConstants.PACKAGING_POM.equals(model.getPackaging())) {

                        Model codeProjectTemplateModel = MavenTemplateManager.getCodeProjectTemplateModel();
                        model.setGroupId(codeProjectTemplateModel.getGroupId());
                        model.setArtifactId(codeProjectTemplateModel.getArtifactId());
                        model.setVersion(codeProjectTemplateModel.getVersion());
                        model.setName(codeProjectTemplateModel.getName());
                        model.setPackaging(codeProjectTemplateModel.getPackaging());

                        PomUtil.savePom(monitor, model, pomFile);

                        p.refreshLocal(IResource.DEPTH_ONE, monitor);
                    }
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }

        }
    }

    public static void changeClasspath(IProgressMonitor monitor, IProject p) {
        try {
            IJavaProject javaProject = JavaCore.create(p);
            IClasspathEntry[] rawClasspathEntries = javaProject.getRawClasspath();
            boolean changed = false;

            for (int index = 0; index < rawClasspathEntries.length; index++) {
                IClasspathEntry entry = rawClasspathEntries[index];

                IClasspathEntry newEntry = null;
                if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
                    IPath path = entry.getPath();
                    if (p.getFullPath().isPrefixOf(path)) {
                        path = path.removeFirstSegments(1);
                    }

                    // src/main/resources, in order to removing the 'excluding="**"'.
                    if (MavenSystemFolders.RESOURCES.getPath().equals(path.toString())) {
                        newEntry = JavaCore.newSourceEntry(entry.getPath(), new IPath[0], new IPath[0], //
                                entry.getOutputLocation(), entry.getExtraAttributes());
                    }

                    // src/test/resources, in order to removing the 'excluding="**"'.
                    if (MavenSystemFolders.RESOURCES_TEST.getPath().equals(path.toString())) {
                        newEntry = JavaCore.newSourceEntry(entry.getPath(), new IPath[0], new IPath[0], //
                                entry.getOutputLocation(), entry.getExtraAttributes());
                    }

                } else if (entry.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {
                    // remove the special version of jre in container.
                    IPath defaultJREContainerPath = JavaRuntime.newDefaultJREContainerPath();
                    if (defaultJREContainerPath.isPrefixOf(entry.getPath())) {
                        // JavaRuntime.getDefaultJREContainerEntry(); //missing properties
                        newEntry = JavaCore.newContainerEntry(defaultJREContainerPath, entry.getAccessRules(),
                                entry.getExtraAttributes(), entry.isExported());
                    }
                }
                if (newEntry != null) {
                    rawClasspathEntries[index] = newEntry;
                    changed = true;
                }

            }
            if (changed) {
                javaProject.setRawClasspath(rawClasspathEntries, monitor);
            }
        } catch (CoreException e) {
            ExceptionHandler.process(e);
        }
    }

    /**
     * 
     * FIXME, Maybe need find another way to remove the lastUpdated files. seems the
     * MavenUpdateRequest.isForceDependencyUpdate is not useful when create this .Java project.
     */
    private void cleanLastUpdatedFiles() {
        final IMaven maven = MavenPlugin.getMaven();
        String localRepositoryPath = maven.getLocalRepositoryPath();
        if (localRepositoryPath == null) {
            return;
        }
        File localRepoFolder = new File(localRepositoryPath);
        cleanLastUpdatedFile(localRepoFolder);
    }

    private final static FileFilter lastUpdatedFilter = new FileFilter() {

        @Override
        public boolean accept(File pathname) {
            return pathname.isDirectory() || pathname.getName().endsWith(".lastUpdated") //$NON-NLS-1$
                    || pathname.getName().equals("m2e-lastUpdated.properties"); //$NON-NLS-1$
        }
    };

    private void cleanLastUpdatedFile(final File file) {
        if (file != null && file.exists()) {
            if (file.isDirectory()) {
                File[] list = file.listFiles(lastUpdatedFilter);
                if (list != null) {
                    for (File f : list) {
                        cleanLastUpdatedFile(f);
                    }
                }
            } else if (file.isFile() && lastUpdatedFilter.accept(file)) {
                file.delete();
            }
        }
    }

    /**
     * Clear compliance settings from project, and set them into Eclipse compliance settings
     * 
     * @param javaProject
     */
    private static void clearProjectIndenpendComplianceSettings(IJavaProject javaProject) {

        Map<String, String> projectComplianceOptions = javaProject.getOptions(false);
        if (projectComplianceOptions == null || projectComplianceOptions.isEmpty()) {
            return;
        }
        String compilerCompliance = javaProject.getOption(JavaCore.COMPILER_COMPLIANCE, false);
        // clear compliance settings from project
        Set<String> keySet = projectComplianceOptions.keySet();
        for (String key : keySet) {
            javaProject.setOption(key, null);
        }

        IEclipsePreferences pluginPreferences = InstanceScope.INSTANCE.getNode(DesignerMavenPlugin.PLUGIN_ID);
        boolean isAlreadySetEclipsePreferences = pluginPreferences.getBoolean(IS_ALREADY_SET_ECLIPSE_COMPLIANCE, false);
        // if already setted them, then can't modify them anymore since user can customize them.
        if (!isAlreadySetEclipsePreferences) {
            pluginPreferences.putBoolean(IS_ALREADY_SET_ECLIPSE_COMPLIANCE, true);
            if (compilerCompliance != null) {
                IEclipsePreferences eclipsePreferences = InstanceScope.INSTANCE.getNode(JavaCore.PLUGIN_ID);
                // set compliance settings to Eclipse
                Map<String, String> complianceOptions = new HashMap<String, String>();
                JavaCore.setComplianceOptions(compilerCompliance, complianceOptions);
                if (!complianceOptions.isEmpty()) {
                    Set<Entry<String, String>> entrySet = complianceOptions.entrySet();
                    for (Entry<String, String> entry : entrySet) {
                        eclipsePreferences.put(entry.getKey(), entry.getValue());
                    }
                }
                try {
                    // save changes
                    eclipsePreferences.flush();
                    pluginPreferences.flush();
                } catch (BackingStoreException e) {
                    ExceptionHandler.process(e);
                }
            }
        }
    }

}
