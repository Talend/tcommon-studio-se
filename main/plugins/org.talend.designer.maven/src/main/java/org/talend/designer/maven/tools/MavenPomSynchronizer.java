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

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.maven.model.Model;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.MavenModelManager;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.ProcessUtils;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.core.runtime.process.TalendProcessArgumentConstant;
import org.talend.core.runtime.projectsetting.IProjectSettingPreferenceConstants;
import org.talend.core.runtime.projectsetting.IProjectSettingTemplateConstants;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.template.MavenTemplateManager;
import org.talend.designer.maven.tools.creator.CreateMavenBeanPom;
import org.talend.designer.maven.tools.creator.CreateMavenPigUDFPom;
import org.talend.designer.maven.tools.creator.CreateMavenRoutinePom;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.utils.io.FilesUtils;

/**
 * created by ggu on 2 Feb 2015 Detailled comment
 *
 */
public class MavenPomSynchronizer {

    private final ITalendProcessJavaProject codeProject;

    private IRunProcessService runProcessService;

    private static boolean isListenerAdded;

    public MavenPomSynchronizer(IProcessor processor) {
        this(processor.getTalendJavaProject());
    }

    public MavenPomSynchronizer(ITalendProcessJavaProject codeProject) {
        super();
        this.codeProject = codeProject;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            runProcessService = (IRunProcessService) GlobalServiceRegister.getDefault().getService(IRunProcessService.class);
        }
    }

    /**
     * generate routine pom.
     */
    @Deprecated
    public void syncRoutinesPom(Property property, boolean overwrite) throws Exception {
        ITalendProcessJavaProject routineProject = runProcessService.getTalendCodeJavaProject(ERepositoryObjectType.ROUTINES);
        IFile routinesPomFile = routineProject.getProjectPom();
        // generate new one
        CreateMavenRoutinePom createTemplatePom = new CreateMavenRoutinePom(routinesPomFile);
        createTemplatePom.setProperty(property);
        createTemplatePom.setOverwrite(overwrite);
        createTemplatePom.create(null);
    }

    @Deprecated
    public void syncBeansPom(Property property, boolean overwrite) throws Exception {
        ITalendProcessJavaProject beansProject = runProcessService
                .getTalendCodeJavaProject(ERepositoryObjectType.valueOf("BEANS")); //$NON-NLS-1$
        IFile beansPomFile = beansProject.getProjectPom();
        // generate new one
        CreateMavenBeanPom createTemplatePom = new CreateMavenBeanPom(beansPomFile);
        createTemplatePom.setProperty(property);
        createTemplatePom.setOverwrite(overwrite);
        createTemplatePom.create(null);
    }

    @Deprecated
    public void syncPigUDFsPom(Property property, boolean overwrite) throws Exception {
        ITalendProcessJavaProject pigudfsProject = runProcessService.getTalendCodeJavaProject(ERepositoryObjectType.PIG_UDF);
        IFile pigudfPomFile = pigudfsProject.getProjectPom();
        // generate new one
        CreateMavenPigUDFPom createTemplatePom = new CreateMavenPigUDFPom(pigudfPomFile);
        createTemplatePom.setProperty(property);
        createTemplatePom.setOverwrite(overwrite);
        createTemplatePom.create(null);
    }

    /**
     * 
     * sync the bat/sh/jobInfo to resources template folder.
     */
    public void syncTemplates(boolean overwrite) throws Exception {
        IFolder templateFolder = codeProject.getTemplatesFolder();

        IFile shFile = templateFolder.getFile(IProjectSettingTemplateConstants.JOB_RUN_SH_TEMPLATE_FILE_NAME);
        IFile batFile = templateFolder.getFile(IProjectSettingTemplateConstants.JOB_RUN_BAT_TEMPLATE_FILE_NAME);
        IFile psFile = templateFolder.getFile(IProjectSettingTemplateConstants.JOB_RUN_PS_TEMPLATE_FILE_NAME);
        IFile infoFile = templateFolder.getFile(IProjectSettingTemplateConstants.JOB_INFO_TEMPLATE_FILE_NAME);

        Property property = codeProject.getPropery();
        if (property != null) {
            final Map<String, Object> templateParameters = PomUtil.getTemplateParameters(property);
            String shContent = MavenTemplateManager.getProjectSettingValue(IProjectSettingPreferenceConstants.TEMPLATE_SH,
                    templateParameters);
            String batContent = MavenTemplateManager.getProjectSettingValue(IProjectSettingPreferenceConstants.TEMPLATE_BAT,
                    templateParameters);
            String psContent = MavenTemplateManager.getProjectSettingValue(IProjectSettingPreferenceConstants.TEMPLATE_PS,
                    templateParameters);
            String jobInfoContent = MavenTemplateManager.getProjectSettingValue(IProjectSettingPreferenceConstants.TEMPLATE_JOB_INFO,
                    templateParameters);
    
            MavenTemplateManager.saveContent(shFile, shContent, overwrite);
            MavenTemplateManager.saveContent(batFile, batContent, overwrite);
            MavenTemplateManager.saveContent(psFile, psContent, overwrite);
            MavenTemplateManager.saveContent(infoFile, jobInfoContent, overwrite);
        }
    }

    /**
     * 
     * add the job to the pom modules list of project.
     */
    public void addChildModules(boolean removeOld, String... childModules) throws Exception {
        IFile projectPomFile = codeProject.getProjectPom();

        MavenModelManager mavenModelManager = MavenPlugin.getMavenModelManager();
        Model projModel = mavenModelManager.readMavenModel(projectPomFile);
        List<String> modules = projModel.getModules();
        if (modules == null) {
            modules = new ArrayList<String>();
            projModel.setModules(modules);
        }

        boolean modifed = false;
        if (removeOld || childModules == null || childModules.length == 0) { // clean the modules
            if (!modules.isEmpty()) {
                modules.clear();
                modifed = true;
            }
        }

        final Iterator<String> iterator = modules.iterator();
        while (iterator.hasNext()) {
            String module = iterator.next();
            if (ArrayUtils.contains(childModules, module)) {
                iterator.remove(); // remove the exised one
            }
        }

        if (childModules != null) {
            // according to the arrays order to add the modules.
            for (String module : childModules) {
                if (module.length() > 0) {
                    modules.add(module);
                    modifed = true;
                }
            }
        }

        if (modifed) {
            // save pom.
            PomUtil.savePom(null, projModel, projectPomFile);
        }
    }

    /**
     * 
     * Clean the pom_xxx.xml and assembly_xxx.xml and target folder, also clean the module and dependencies.
     * 
     * another cleaning up for sources codes or such in @see DeleteAllJobWhenStartUp.
     */
    public void cleanMavenFiles(IProgressMonitor monitor) throws Exception {
        IProject jProject = codeProject.getProject();
        if (!jProject.isOpen()) {
            jProject.open(monitor);
        }
        // empty the src/main/java...
        IFolder srcFolder = codeProject.getSrcFolder();
        codeProject.cleanFolder(monitor, srcFolder);

        // empty resources
        IFolder resourcesFolder = codeProject.getExternalResourcesFolder();
        codeProject.cleanFolder(monitor, resourcesFolder);

        // empty the outputs, target
        IFolder targetFolder = codeProject.getTargetFolder();
        codeProject.cleanFolder(monitor, targetFolder);

        // empty the src/test/java
        IFolder testSrcFolder = codeProject.getTestSrcFolder();
        codeProject.cleanFolder(monitor, testSrcFolder);

        // empty the src/test/java (main for contexts)
        IFolder testResourcesFolder = codeProject.getTestResourcesFolder();
        codeProject.cleanFolder(monitor, testResourcesFolder);

        // rules
        IFolder rulesResFolder = codeProject.getResourceSubFolder(monitor, JavaUtils.JAVA_RULES_DIRECTORY);
        codeProject.cleanFolder(monitor, rulesResFolder);

        // sqltemplate
        IFolder sqlTemplateResFolder = codeProject.getResourceSubFolder(monitor, JavaUtils.JAVA_SQLPATTERNS_DIRECTORY);
        codeProject.cleanFolder(monitor, sqlTemplateResFolder);

        // clean all assemblies in src/main/assemblies
        // fullCleanupContainer(codeProject.getAssembliesFolder());

        // clean all items in src/main/items
        fullCleanupContainer(codeProject.getItemsFolder());

        // clean all items in tests
        fullCleanupContainer(codeProject.getTestsFolder());

        codeProject.getProject().refreshLocal(IResource.DEPTH_ONE, monitor);

        if (!isListenerAdded) {
            synchronized (this) {
                if (!isListenerAdded) {
                    if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibrariesService.class)) {
                        ILibrariesService libService = (ILibrariesService) GlobalServiceRegister.getDefault()
                                .getService(ILibrariesService.class);
                        libService.addChangeLibrariesListener(new ILibrariesService.IChangedLibrariesListener() {

                            @Override
                            public void afterChangingLibraries() {
                                try {
                                    // update the dependencies
                                    new AggregatorPomsHelper().updateCodeProjects(monitor);
                                } catch (Exception e) {
                                    ExceptionHandler.process(e);
                                }
                            }
                        });

                    }
                    isListenerAdded = true;
                }
            }

        }
    }

    private void fullCleanupContainer(IContainer container) {
        if (container != null && container.exists()) {
            FilesUtils.deleteFolder(container.getLocation().toFile(), false);
        }
    }

    private void cleanupContainer(IContainer container, FilenameFilter filter) {
        File folder = container.getLocation().toFile();
        if (filter != null) {
            deleteFiles(folder.listFiles(filter));
        } else {
            fullCleanupContainer(container);
        }
    }

    private void deleteFiles(File[] files) {
        if (files != null) {
            for (File f : files) {
                if (f.isFile()) {
                    f.delete();
                }
            }
        }
    }

    @Deprecated
    public void syncCodesPoms(IProgressMonitor monitor, IProcessor processor, boolean overwrite) throws Exception {
        final IProcess process = processor != null ? processor.getProcess() : null;
        Property property = null;
        if (processor != null) {
            property = processor.getProperty();
        }

        syncRoutinesPom(property, overwrite);
        // PigUDFs
        if (ProcessUtils.isRequiredPigUDFs(process)) {
            syncPigUDFsPom(property, overwrite);
        }
        // Beans
        if (ProcessUtils.isRequiredBeans(process)) {
            syncBeansPom(property, overwrite);
        }
    }

    private static File findTestContextFile(File file) {
        if (file != null) {
            if (file.getName().endsWith(JavaUtils.JAVA_CONTEXT_EXTENSION)
                    && file.getParentFile().getName().equals(JavaUtils.JAVA_CONTEXTS_DIRECTORY)) {
                return file;
            }
            if (file.isDirectory()) {
                File[] listFiles = file.listFiles();
                if (listFiles != null) {
                    for (File f : listFiles) {
                        File contextFile = findTestContextFile(f);
                        if (contextFile != null) {
                            return contextFile;
                        }
                    }
                }
            }
        }
        return null;
    }

}
