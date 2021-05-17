// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.maven.model.Model;
import org.eclipse.core.resources.IFile;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.utils.JavaResourcesHelper;
import org.talend.core.runtime.projectsetting.IProjectSettingTemplateConstants;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.maven.template.ETalendMavenVariables;
import org.talend.designer.maven.template.MavenTemplateManager;
import org.talend.designer.maven.utils.PomIdsHelper;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.repository.ProjectManager;

public class CreateMavenJobletPom extends AbstractMavenProcessorPom {

    private static final String JOBLET_PLUGIN_ID = "org.talend.designer.joblet"; //$NON-NLS-1$

    public CreateMavenJobletPom(IProcessor processor, IFile pomFile) {
        super(processor, pomFile, ""); // $NON-NLS-1$
    }

    @Override
    protected String getBundleTemplatePath() {
        return IProjectSettingTemplateConstants.PATH_RESOURCES_TEMPLATES + '/' // $NON-NLS-1$
                + IProjectSettingTemplateConstants.POM_JOBLET_TEMPLATE_FILE_NAME;
    }

    @Override
    protected InputStream getTemplateStream() throws IOException {
        try {
            return MavenTemplateManager.getBundleTemplateStream(JOBLET_PLUGIN_ID, getBundleTemplatePath());
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    protected void setAttributes(Model model) {
        final IProcessor jProcessor = getJobProcessor();
        IProcess process = jProcessor.getProcess();
        Property property = jProcessor.getProperty();

        Map<ETalendMavenVariables, String> variablesValuesMap = new HashMap<ETalendMavenVariables, String>();
        variablesValuesMap.put(ETalendMavenVariables.JobletGroupId, PomIdsHelper.getJobletGroupId(property));
        variablesValuesMap.put(ETalendMavenVariables.JobletArtifactId, PomIdsHelper.getJobletArtifactId(property));
        variablesValuesMap.put(ETalendMavenVariables.JobletVersion, PomIdsHelper.getJobletVersion(property));
        variablesValuesMap.put(ETalendMavenVariables.TalendJobVersion, property.getVersion());
        String jobletName = JavaResourcesHelper.escapeFileName(process.getName());
        variablesValuesMap.put(ETalendMavenVariables.JobletName, jobletName);

        if (property != null) {
            Project currentProject = ProjectManager.getInstance().getProject(property);
            variablesValuesMap.put(ETalendMavenVariables.ProjectName,
                    currentProject != null ? currentProject.getTechnicalLabel() : null);

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

        if (this.getGroupId() != null) {
            model.setGroupId(this.getGroupId());
        }
        if (this.getArtifactId() != null) {
            model.setArtifactId(this.getArtifactId());
        }
        if (this.getVersion() != null) {
            model.setVersion(this.getVersion());
        }
        if (this.getName() != null) {
            model.setName(this.getName());
        }
        if (this.getDesc() != null) {
            model.setDescription(this.getDesc());
        }
    }

    @Override
    protected void addProperties(Model model) {
        super.addProperties(model);

        Properties properties = model.getProperties();
        Property property = getJobProcessor().getProperty();

        checkPomProperty(properties, "talend.joblet.id", ETalendMavenVariables.JobletId, property.getId()); //$NON-NLS-1$
        checkPomProperty(properties, "talend.joblet.version", ETalendMavenVariables.JobletVersion, //$NON-NLS-1$
                property.getVersion());
    }

    @Override
    protected void addDependencies(Model model) {
        try {
            if (ERepositoryObjectType.JOBLET == ERepositoryObjectType.getType(getJobProcessor().getProperty())) {
                getProcessorDependenciesManager().updateDependencies(null, model);
            }
            model.getDependencies().addAll(getCodesJarDependencies());
            addChildrenDependencies(model.getDependencies());
        } catch (ProcessorException e) {
            ExceptionHandler.process(e);
        }
    }

    @Override
    protected ProcessType getProcessType() {
        Item item = getJobProcessor().getProperty().getItem();
        return ((JobletProcessItem) item).getJobletProcess();
    }

}
