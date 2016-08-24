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
package org.talend.designer.maven.tools.creator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.JobInfo;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.utils.JavaResourcesHelper;
import org.talend.core.runtime.process.LastGenerationInfo;
import org.talend.core.runtime.projectsetting.IProjectSettingTemplateConstants;
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
public abstract class AbstractMavenProcessorPom extends CreateMavenBundleTemplatePom {

    private final IProcessor jobProcessor;

    private final ProcessorDependenciesManager processorDependenciesManager;

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

    protected void setAttributes(Model model) {
        //
        final IProcessor jProcessor = getJobProcessor();
        final IProcess process = jProcessor.getProcess();
        final Property property = jProcessor.getProperty();

        Map<ETalendMavenVariables, String> variablesValuesMap = new HashMap<ETalendMavenVariables, String>();
        // no need check property is null or not, because if null, will get default ids.
        variablesValuesMap.put(ETalendMavenVariables.JobGroupId, PomIdsHelper.getJobGroupId(property));
        variablesValuesMap.put(ETalendMavenVariables.JobArtifactId, PomIdsHelper.getJobArtifactId(property));
        variablesValuesMap.put(ETalendMavenVariables.JobVersion, PomIdsHelper.getJobVersion(property));
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
            PomUtil.checkParent(model, this.getPomFile(), getDeployVersion());

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
                    String version = PomIdsHelper.getJobVersion(jobInfo);
                    if (getDeployVersion() != null) {
                        version = getDeployVersion();
                    }
                    // same group as main job.
                    Dependency d = PomUtil.createDependency(model.getGroupId(), PomIdsHelper.getJobArtifactId(jobInfo),
                            version, null);
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

}
