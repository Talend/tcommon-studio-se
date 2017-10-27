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
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.MavenModelManager;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.ProcessUtils;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.Property;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.core.runtime.process.TalendProcessArgumentConstant;
import org.talend.core.runtime.projectsetting.IProjectSettingPreferenceConstants;
import org.talend.core.runtime.projectsetting.IProjectSettingTemplateConstants;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.template.MavenTemplateManager;
import org.talend.designer.maven.tools.creator.CreateMavenBeanPom;
import org.talend.designer.maven.tools.creator.CreateMavenBundleTemplatePom;
import org.talend.designer.maven.tools.creator.CreateMavenPigUDFPom;
import org.talend.designer.maven.tools.creator.CreateMavenRoutinePom;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.designer.runprocess.IProcessor;
import org.talend.repository.ProjectManager;
import org.talend.utils.io.FilesUtils;

/**
 * created by ggu on 2 Feb 2015 Detailled comment
 *
 */
public class MavenPomSynchronizer {

    private final ITalendProcessJavaProject codeProject;

    private final IProcessor processor;

    private Map<String, Object> argumentsMap = new HashMap<String, Object>();

    public MavenPomSynchronizer(IProcessor processor) {
        super();
        this.processor = processor;
        this.codeProject = this.processor.getTalendJavaProject();
    }

    public MavenPomSynchronizer(ITalendProcessJavaProject codeProject) {
        super();
        this.processor = null;
        this.codeProject = codeProject;
    }

    /**
     * generate routine pom.
     */
    public void syncRoutinesPom(IProcessor processor,boolean overwrite) throws Exception {
        // pom_routines.xml
        IFile routinesPomFile = codeProject.getProject().getFile(
                PomUtil.getPomFileName(TalendMavenConstants.DEFAULT_ROUTINES_ARTIFACT_ID));
        // generate new one
        CreateMavenRoutinePom createTemplatePom = new CreateMavenRoutinePom(routinesPomFile);
        createTemplatePom.setProcessor(processor);
        createTemplatePom.setArgumentsMap(getArgumentsMap());
        createTemplatePom.setOverwrite(overwrite);
        createTemplatePom.create(null);
    }

    public void syncBeansPom(IProcessor processor,boolean overwrite) throws Exception {
        // pom_beans.xml
        IFile beansPomFile = codeProject.getProject().getFile(
                PomUtil.getPomFileName(TalendMavenConstants.DEFAULT_BEANS_ARTIFACT_ID));
        // generate new one
        CreateMavenBeanPom createTemplatePom = new CreateMavenBeanPom(beansPomFile);
        createTemplatePom.setProcessor(processor);
        createTemplatePom.setArgumentsMap(getArgumentsMap());
        createTemplatePom.setOverwrite(overwrite);
        createTemplatePom.create(null);
    }

    public void syncPigUDFsPom(IProcessor processor,boolean overwrite) throws Exception {
        // pom_pigudfs.xml
        IFile beansPomFile = codeProject.getProject().getFile(
                PomUtil.getPomFileName(TalendMavenConstants.DEFAULT_PIGUDFS_ARTIFACT_ID));
        // generate new one
        CreateMavenPigUDFPom createTemplatePom = new CreateMavenPigUDFPom(beansPomFile);
        createTemplatePom.setProcessor(processor);
        createTemplatePom.setArgumentsMap(getArgumentsMap());
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
        IFile infoFile = templateFolder.getFile(IProjectSettingTemplateConstants.JOB_INFO_TEMPLATE_FILE_NAME);

        final Map<String, Object> templateParameters = PomUtil.getTemplateParameters(processor);
        String shContent = MavenTemplateManager.getProjectSettingValue(IProjectSettingPreferenceConstants.TEMPLATE_SH,
                templateParameters);
        String batContent = MavenTemplateManager.getProjectSettingValue(IProjectSettingPreferenceConstants.TEMPLATE_BAT,
                templateParameters);
        String jobInfoContent = MavenTemplateManager.getProjectSettingValue(IProjectSettingPreferenceConstants.TEMPLATE_JOB_INFO,
                templateParameters);

        MavenTemplateManager.saveContent(shFile, shContent, overwrite);
        MavenTemplateManager.saveContent(batFile, batContent, overwrite);
        MavenTemplateManager.saveContent(infoFile, jobInfoContent, overwrite);
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

        // remove all job poms
        FilenameFilter filter = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                // pom_xxx.xml
                return name.startsWith(TalendMavenConstants.POM_NAME + '_') && name.endsWith(TalendMavenConstants.XML_EXT);
            }
        };
        cleanupContainer(codeProject.getProject(), filter);

        // clean all assemblies in src/main/assemblies
        fullCleanupContainer(codeProject.getAssembliesFolder());

        // clean all items in src/main/items
        fullCleanupContainer(codeProject.getItemsFolder());

        // clean all items in tests
        fullCleanupContainer(codeProject.getTestsFolder());

        codeProject.getProject().refreshLocal(IResource.DEPTH_ONE, monitor);
        
        // when clean, regenerate it.
        updateCodesPomWithProject(monitor);

        // try to compile it.
        final Map<String, Object> argumentsMap = new HashMap<String, Object>();
        argumentsMap.put(TalendProcessArgumentConstant.ARG_GOAL, TalendMavenConstants.GOAL_COMPILE);
        codeProject.buildModules(monitor, null, argumentsMap);

        //
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibrariesService.class)) {
            ILibrariesService libService = (ILibrariesService) GlobalServiceRegister.getDefault().getService(
                    ILibrariesService.class);
            libService.addChangeLibrariesListener(new ILibrariesService.IChangedLibrariesListener() {

                @Override
                public void afterChangingLibraries() {
                    try {
                        // update the dependencies
                        updateCodesPomWithProject(null);
                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }
                }
            });

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

    public void syncCodesPoms(IProgressMonitor monitor, IProcessor processor, boolean overwrite) throws Exception {
        final IProcess process =processor!=null? processor.getProcess():null;
        
        syncRoutinesPom(processor,overwrite);
        // PigUDFs
        if (ProcessUtils.isRequiredPigUDFs(process)) {
            syncPigUDFsPom(processor,overwrite);
        }
        // Beans
        if (ProcessUtils.isRequiredBeans(process)) {
            syncBeansPom(processor,overwrite);
        }
    }

    private void updateCodesPomWithProject(IProgressMonitor monitor) throws Exception {
        syncCodesPoms(monitor,null,  true);
        // finally, update project
        regenerateMainProjectPom(monitor, processor);
    }

    public void regenerateMainProjectPom(IProgressMonitor monitor, IProcessor processor) throws Exception {
        ProjectPomManager projectManager = new ProjectPomManager(codeProject.getProject());
        projectManager.update(monitor, processor);
    }

    public Map<String, Object> getArgumentsMap() {
        return argumentsMap;
    }

    public void setArgumentsMap(Map<String, Object> argumentsMap) {
        this.argumentsMap = argumentsMap;
    }
}
