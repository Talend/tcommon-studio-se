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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.JobInfo;
import org.talend.core.model.process.ProcessUtils;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.utils.JavaResourcesHelper;
import org.talend.core.runtime.projectsetting.IProjectSettingTemplateConstants;
import org.talend.core.runtime.repository.build.IMavenPomCreator;
import org.talend.core.ui.ITestContainerProviderService;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.template.ETalendMavenVariables;
import org.talend.designer.maven.tools.ProcessorDependenciesManager;
import org.talend.designer.maven.utils.PomIdsHelper;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.repository.ProjectManager;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class AbstractMavenProcessorPom extends CreateMavenBundleTemplatePom implements IMavenPomCreator {

    private final IProcessor jobProcessor;

    private final ProcessorDependenciesManager processorDependenciesManager;

    private IFolder objectTypeFolder;

    private IPath itemRelativePath;

    private boolean syncCodesPoms;

    public AbstractMavenProcessorPom(IProcessor jobProcessor, IFile pomFile, String bundleTemplateName) {
        super(pomFile, IProjectSettingTemplateConstants.PATH_STANDALONE + '/' + bundleTemplateName);
        Assert.isNotNull(jobProcessor);
        this.jobProcessor = jobProcessor;
        this.processorDependenciesManager = new ProcessorDependenciesManager(jobProcessor);

        // always ignore case.
        this.setIgnoreFileNameCase(true);
        // should only base on template.
        this.setBaseOnTemplateOnly(true);
    }

    protected IProcessor getJobProcessor() {
        return this.jobProcessor;
    }

    protected ProcessorDependenciesManager getProcessorDependenciesManager() {
        return processorDependenciesManager;
    }

    public IFolder getObjectTypeFolder() {
        return objectTypeFolder;
    }

    public void setObjectTypeFolder(IFolder objectTypeFolder) {
        this.objectTypeFolder = objectTypeFolder;
    }

    public IPath getItemRelativePath() {
        return itemRelativePath;
    }

    public void setItemRelativePath(IPath itemRelativePath) {
        this.itemRelativePath = itemRelativePath;
    }

    @Override
    protected void setAttributes(Model model) {
        //
        final IProcessor jProcessor = getJobProcessor();
        IProcess process = jProcessor.getProcess();
        Property property = jProcessor.getProperty();
        
        if (ProcessUtils.isTestContainer(process)) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class)) {
                ITestContainerProviderService testService = (ITestContainerProviderService) GlobalServiceRegister.getDefault().getService(ITestContainerProviderService.class);
                try {
                    property = testService.getParentJobItem(property.getItem()).getProperty();
                    process = testService.getParentJobProcess(process);
                } catch (PersistenceException e) {
                    ExceptionHandler.process(e);
                }
            }
        }

        Map<ETalendMavenVariables, String> variablesValuesMap = new HashMap<ETalendMavenVariables, String>();
        // no need check property is null or not, because if null, will get default ids.
        variablesValuesMap.put(ETalendMavenVariables.JobGroupId, PomIdsHelper.getJobGroupId(property));
        variablesValuesMap.put(ETalendMavenVariables.JobArtifactId, PomIdsHelper.getJobArtifactId(property));
        variablesValuesMap.put(ETalendMavenVariables.JobVersion,PomIdsHelper.getJobVersion(property));
        variablesValuesMap.put(ETalendMavenVariables.TalendJobVersion, property.getVersion());
        final String jobName = JavaResourcesHelper.escapeFileName(process.getName());
        variablesValuesMap.put(ETalendMavenVariables.JobName, jobName);

        if (property != null) {
            Project currentProject = ProjectManager.getInstance().getProject(property);
            variablesValuesMap.put(ETalendMavenVariables.ProjectName, currentProject != null ? currentProject.getTechnicalLabel()
                    : null);

            Item item = property.getItem();
            if (item != null) {
                ERepositoryObjectType itemType = ERepositoryObjectType.getItemType(item);
                if (itemType != null) {
                    variablesValuesMap.put(ETalendMavenVariables.JobType, itemType.getLabel());
                }
            }
        }

        this.setGroupId(ETalendMavenVariables.replaceVariables(model.getGroupId(), variablesValuesMap));
        this.setArtifactId(ETalendMavenVariables.replaceVariables(model.getArtifactId(), variablesValuesMap));
        this.setVersion(ETalendMavenVariables.replaceVariables(model.getVersion(), variablesValuesMap));
        this.setName(ETalendMavenVariables.replaceVariables(model.getName(), variablesValuesMap));

        super.setAttributes(model);
    }

    @Override
    protected Model createModel() {
        Model model = super.createModel();
        if (model != null) {
            PomUtil.checkParent(model, this.getPomFile(), jobProcessor.getProperty());

            addDependencies(model);
        }
        return model;
    }

    protected void addDependencies(Model model) {
        try {
            getProcessorDependenciesManager().updateDependencies(null, model);
            
            final List<Dependency> dependencies = model.getDependencies();

            // add codes to dependencies
            // String projectTechName = ProjectManager.getInstance().getProject(getJobProcessor().getProperty()).getTechnicalLabel();
            // always use codes of main project for ref project
            String projectTechName = ProjectManager.getInstance().getCurrentProject().getTechnicalLabel();
            String codeVersion = PomIdsHelper.getCodesVersion();
            
            // routines
            String routinesGroupId = PomIdsHelper.getCodesGroupId(projectTechName, TalendMavenConstants.DEFAULT_CODE);
            String routinesArtifactId = TalendMavenConstants.DEFAULT_ROUTINES_ARTIFACT_ID;
            Dependency routinesDependency = PomUtil.createDependency(routinesGroupId, routinesArtifactId, codeVersion, null);
            dependencies.add(routinesDependency);
            
            // pigudfs
            if (ProcessUtils.isRequiredPigUDFs(jobProcessor.getProcess())) {
                String pigudfsGroupId = PomIdsHelper.getCodesGroupId(projectTechName, TalendMavenConstants.DEFAULT_PIGUDF);
                String pigudfsArtifactId = TalendMavenConstants.DEFAULT_PIGUDFS_ARTIFACT_ID;
                Dependency pigudfsDependency = PomUtil.createDependency(pigudfsGroupId, pigudfsArtifactId, codeVersion, null);
                dependencies.add(pigudfsDependency);
            }
            
            // beans
            if (ProcessUtils.isRequiredBeans(jobProcessor.getProcess())) {
                String beansGroupId = PomIdsHelper.getCodesGroupId(projectTechName, TalendMavenConstants.DEFAULT_BEAN);
                String beansArtifactId = TalendMavenConstants.DEFAULT_BEANS_ARTIFACT_ID;
                Dependency beansDependency = PomUtil.createDependency(beansGroupId, beansArtifactId, codeVersion, null);
                dependencies.add(beansDependency);
            }
            
            // add children jobs in dependencies
            String parentId = getJobProcessor().getProperty().getId();
            final Set<JobInfo> clonedChildrenJobInfors = getJobProcessor().getBuildChildrenJobs();
            for (JobInfo jobInfo : clonedChildrenJobInfors) {
                if (jobInfo.getFatherJobInfo() != null && jobInfo.getFatherJobInfo().getJobId().equals(parentId)) {
                    if (!validChildrenJob(jobInfo)) {
                        continue;
                    }
                    Property property = jobInfo.getProcessItem().getProperty();
                    String groupId = PomIdsHelper.getJobGroupId(property);
                    String artifactId = PomIdsHelper.getJobArtifactId(jobInfo);
                    String version = PomIdsHelper.getJobVersion(property);

                    // try to get the pom version of children job and load from the pom file.
                    String childPomFileName = PomUtil.getPomFileName(jobInfo.getJobName(), jobInfo.getJobVersion());
                    IProject codeProject = getJobProcessor().getCodeProject();
                    try {
                        codeProject.refreshLocal(IResource.DEPTH_ONE, null); // is it ok or needed here ???
                    } catch (CoreException e) {
                        ExceptionHandler.process(e);
                    }

                    IFile childPomFile = codeProject.getFile(new Path(childPomFileName));
                    if (childPomFile.exists()) {
                        try {
                            Model childModel = MODEL_MANAGER.readMavenModel(childPomFile);
                            // try to get the real groupId, artifactId, version.
                            groupId = childModel.getGroupId();
                            artifactId = childModel.getArtifactId();
                            version = childModel.getVersion();
                        } catch (CoreException e) {
                            ExceptionHandler.process(e);
                        }
                    }

                    Dependency d = PomUtil.createDependency(groupId, artifactId, version, null);
                    dependencies.add(d);
                }
            }

        } catch (ProcessorException e) {
            ExceptionHandler.process(e);
        }
    }

    protected boolean validChildrenJob(JobInfo jobInfo) {
        return true; // default, all are valid
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.runtime.repository.build.IMavenPomCreator#needsyncCodesPoms(boolean)
     */
    @Override
    public void setSyncCodesPoms(boolean isMainJob) {
        this.syncCodesPoms = isMainJob;
    }

    public boolean needSyncCodesPoms() {
        return this.syncCodesPoms;
    }
}
