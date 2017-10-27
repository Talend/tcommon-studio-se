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
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.utils.JavaResourcesHelper;
import org.talend.core.runtime.projectsetting.IProjectSettingTemplateConstants;
import org.talend.core.runtime.repository.build.IMavenPomCreator;
import org.talend.core.ui.ITestContainerProviderService;
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
        final IProcess process = jProcessor.getProcess();
        final Property property = jProcessor.getProperty();

        Property jobProperty = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class)) {
            ITestContainerProviderService service = (ITestContainerProviderService) GlobalServiceRegister.getDefault()
                    .getService(ITestContainerProviderService.class);
            if (service.isTestContainerProcess(process)) {
                try {
                    // for test container need to inherit version from job.
                    jobProperty = service.getParentJobItem(property.getItem()).getProperty();
                } catch (PersistenceException e) {
                    ExceptionHandler.process(e);
                }
            }
        }

        Map<ETalendMavenVariables, String> variablesValuesMap = new HashMap<ETalendMavenVariables, String>();
        // no need check property is null or not, because if null, will get default ids.
        variablesValuesMap.put(ETalendMavenVariables.JobGroupId,
                PomIdsHelper.getJobGroupId(jobProperty == null ? property : jobProperty));
        variablesValuesMap.put(ETalendMavenVariables.JobArtifactId, PomIdsHelper.getJobArtifactId(property));
        variablesValuesMap.put(
                ETalendMavenVariables.JobVersion,
                getDeployVersion() != null ? getDeployVersion() : PomIdsHelper.getJobVersion(jobProperty == null ? property
                        : jobProperty));
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
            PomUtil.checkParent(model, this.getPomFile(), jobProcessor, getDeployVersion());

            addDependencies(model);
        }
        return model;
    }

    protected void addDependencies(Model model) {
        try {
            getProcessorDependenciesManager().updateDependencies(null, model);

            // add children jobs in dependencies
            final List<Dependency> dependencies = model.getDependencies();
            String parentId = getJobProcessor().getProperty().getId();
            final Set<JobInfo> clonedChildrenJobInfors = getJobProcessor().getBuildChildrenJobs();
            for (JobInfo jobInfo : clonedChildrenJobInfors) {
                if (jobInfo.getFatherJobInfo() != null && jobInfo.getFatherJobInfo().getJobId().equals(parentId)) {
                    if (!validChildrenJob(jobInfo)) {
                        continue;
                    }
                    String groupId = model.getGroupId();
                    String artifactId = PomIdsHelper.getJobArtifactId(jobInfo);
                    String version = PomIdsHelper.getJobVersion(jobInfo);
                    if (getDeployVersion() != null) {
                        version = getDeployVersion();
                    }

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
