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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.MavenModelManager;
import org.talend.core.model.process.JobInfo;
import org.talend.core.runtime.process.TalendProcessArgumentConstant;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.template.MavenTemplateManager;
import org.talend.designer.maven.utils.PomIdsHelper;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.designer.runprocess.IProcessor;
import org.talend.repository.ProjectManager;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ProjectPomManager {

    protected static final MavenModelManager MODEL_MANAGER = MavenPlugin.getMavenModelManager();

    private IFile projectPomFile;

    /**
     * true by default, update all
     */
    private boolean updateModules = true;

    private boolean updateDependencies = true;

    private Map<String, Object> argumentsMap = new HashMap<String, Object>();

    public ProjectPomManager(IProject project) {
        super();
        Assert.isNotNull(project);
        this.projectPomFile = project.getFile(TalendMavenConstants.POM_FILE_NAME);
    }

    public void setUpdateModules(boolean updateModules) {
        this.updateModules = updateModules;
    }

    public void setUpdateDependencies(boolean updateDependencies) {
        this.updateDependencies = updateDependencies;
    }

    protected boolean isUpdateModules() {
        return updateModules;
    }

    protected boolean isUpdateDependencies() {
        return updateDependencies;
    }

    public void update(IProgressMonitor monitor, IProcessor processor) throws Exception {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        if (!projectPomFile.exists()) {// delete by user manually?
            // create it or nothing to do?
            return;
        }
        Model projectModel = MODEL_MANAGER.readMavenModel(projectPomFile);

        // attributes
        updateAttributes(monitor, processor, projectModel);
        // modules
        updateModulesList(monitor, processor, projectModel);
        // dependencies
        updateDependencies(monitor, processor, projectModel);

        PomUtil.savePom(monitor, projectModel, projectPomFile);
    }

    public void updateFromTemplate(IProgressMonitor monitor) throws Exception {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        Model templateModel = MavenTemplateManager.getCodeProjectTemplateModel();

        if (projectPomFile.exists()) {
            Model projectModel = MODEL_MANAGER.readMavenModel(projectPomFile);

            // modules, add existed
            templateModel.getModules().addAll(projectModel.getModules());
            // dependencies
            templateModel.getDependencies().addAll(projectModel.getDependencies());
        }

        PomUtil.savePom(monitor, templateModel, projectPomFile);
    }

    /**
     * 
     * update the main attributes for project pom.
     * 
     */
    protected void updateAttributes(IProgressMonitor monitor, IProcessor processor, Model projectModel) throws Exception {
        final Map<String, Object> templateParameters = PomUtil.getTemplateParameters(processor);
        Model templateModel = MavenTemplateManager.getCodeProjectTemplateModel(templateParameters);

        String deployVersion = (String) argumentsMap.get(TalendProcessArgumentConstant.ARG_DEPLOY_VERSION);
        if (deployVersion != null) {
            templateModel.setVersion(deployVersion);
        }

        projectModel.setGroupId(templateModel.getGroupId());
        projectModel.setArtifactId(templateModel.getArtifactId());
        projectModel.setVersion(templateModel.getVersion());
        projectModel.setName(templateModel.getName());
        projectModel.setPackaging(templateModel.getPackaging());
    }

    /**
     * 
     * update the modules list for project pom.
     * 
     * The routines should be added always.
     */
    protected void updateModulesList(IProgressMonitor monitor, IProcessor processor, Model projectModel) throws Exception {
        if (!isUpdateModules()) {
            return;
        }
        List<String> modulesList = new ArrayList<String>();

        List<String> codesModules = PomUtil.getMavenCodesModules(processor != null ? processor.getProcess() : null);
        for (String module : codesModules) {
            modulesList.add(module);
        }

        if (processor != null) {
            String mainPom = PomUtil.getPomFileName(processor.getProperty().getLabel(), processor.getProperty().getVersion());
            for (JobInfo childJob : processor.getBuildChildrenJobs()) {
                String childPom = PomUtil.getPomFileName(childJob.getJobName(), childJob.getJobVersion());
                if (!childPom.equals(mainPom)) {
                    modulesList.add(childPom);
                }
            }
            modulesList.add(mainPom);
        }
        // check the modules
        List<String> modules = projectModel.getModules();
        if (modules == null) {
            modules = new ArrayList<String>();
            projectModel.setModules(modules);
        } else if (!modules.isEmpty()) {
            modules.clear(); // clean all?
        }

        modules.addAll(modulesList);

        /*
         * need update the parent for each modules.
         */
        IProject project = projectPomFile.getProject();

        for (String module : modules) {
            IFile file = project.getFile(module);
            if (file.exists()) {
                Model model = MODEL_MANAGER.readMavenModel(file);
                Parent parent = model.getParent();
                // only check same parent.
                if (parent != null) {
                    if (parent.getGroupId().equals(projectModel.getGroupId())) {
                        continue; // not same
                    }

                    PomUtil.checkParent(model, file, processor,
                            (String) argumentsMap.get(TalendProcessArgumentConstant.ARG_DEPLOY_VERSION));
                    PomUtil.savePom(monitor, model, file);
                }
            }
        }
    }

    /**
     * If standard job and base job pom file existed, will use the dependences of job pom directly.
     */
    protected void updateDependencies(IProgressMonitor monitor, IProcessor processor, Model projectModel) throws Exception {
        if (!isUpdateDependencies()) {
            return;
        }
        IFile basePomFile = getBasePomFile();
        if (isStandardJob() && basePomFile != null && basePomFile.getLocation().toFile().exists()) {
            Model jobModel = MODEL_MANAGER.readMavenModel(basePomFile);

            List<Dependency> withoutChildrenJobDependencies = new ArrayList<Dependency>(jobModel.getDependencies());
            // org.talend.job
            final String jobGroupPrefix = PomIdsHelper.getJobGroupId((String) null);
            // org.talend.test
            final String testGroupPrefix = PomIdsHelper.getTestGroupId((String) null);
            Iterator<Dependency> iterator = withoutChildrenJobDependencies.iterator();
            while (iterator.hasNext()) {
                Dependency d = iterator.next();
                if (d.getGroupId().startsWith(jobGroupPrefix) || d.getGroupId().startsWith(testGroupPrefix)) {
                    // remove the children job's dependencies
                    iterator.remove();
                }
            }

            // fresh is false, make sure all jobs can be compile ok
            ProcessorDependenciesManager.updateDependencies(monitor, projectModel, withoutChildrenJobDependencies, false);

        } else if (processor != null) {// try get the dependencies from processor directly.
            ProcessorDependenciesManager processorDependenciesManager = new ProcessorDependenciesManager(processor);
            processorDependenciesManager.updateDependencies(monitor, projectModel);
        } else {
            // if no processor and without base pom, just read codes dependencies.
            final String technicalLabel = ProjectManager.getInstance().getCurrentProject().getTechnicalLabel();
            List<Dependency> routinesDependencies = new ArrayList<Dependency>(PomUtil.getCodesDependencies(projectPomFile,
                    technicalLabel));
            ProcessorDependenciesManager.updateDependencies(monitor, projectModel, routinesDependencies, true);
        }
    }

    protected boolean isStandardJob() {
        return true;
    }

    protected IFile getBasePomFile() {
        return null;
    }

    public void setArgumentsMap(Map<String, Object> argumentsMap) {
        this.argumentsMap = argumentsMap;
    }

}
