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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.maven.model.Model;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.core.model.components.ComponentCategory;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.Property;
import org.talend.core.model.utils.JavaResourcesHelper;
import org.talend.core.runtime.process.TalendProcessArgumentConstant;
import org.talend.core.runtime.projectsetting.IProjectSettingTemplateConstants;
import org.talend.designer.maven.template.ETalendMavenVariables;
import org.talend.designer.maven.tools.MavenPomSynchronizer;
import org.talend.designer.runprocess.IProcessor;
import org.talend.repository.ProjectManager;

/**
 * created by ggu on 4 Feb 2015 Detailled comment
 *
 */
public class CreateMavenTestPom extends AbstractMavenProcessorPom {

    public CreateMavenTestPom(IProcessor jobProcessor, IFile pomFile) {
        // TBD-3480: Use a separate POM for camel route test case
        super(
                jobProcessor,
                pomFile,
                ComponentCategory.CATEGORY_4_CAMEL.getName().equals(jobProcessor.getProcess().getComponentsType()) ? IProjectSettingTemplateConstants.POM_TEST_ROUTE_TEMPLATE_FILE_NAME
                        : IProjectSettingTemplateConstants.POM_TEST_TEMPLATE_FILE_NAME);
    }

    @Override
    protected InputStream getTemplateStream() throws IOException {
        return super.getTemplateStream();
    }

    @Override
    protected void configModel(Model model) {
        if (getDeployVersion() != null) {
            model.setVersion(getDeployVersion());
        }
        super.configModel(model);
    }

    /**
     * 
     * Add the properties for job.
     */
    @Override
    @SuppressWarnings("nls")
    protected void addProperties(Model model) {
        super.addProperties(model);

        Properties properties = model.getProperties();

        final IProcessor jProcessor = getJobProcessor();
        final Property property = jProcessor.getProperty();

        // same as JavaProcessor.initCodePath
        String jobClassPackageFolder = JavaResourcesHelper.getJobClassPackageFolder(property.getItem(), true);

        Project project = ProjectManager.getInstance().getProject(property);
        if (project == null) { // current project
            project = ProjectManager.getInstance().getCurrentProject().getEmfProject();
        }
        checkPomProperty(properties, "talend.project.name", ETalendMavenVariables.ProjectName, project.getTechnicalLabel());
        checkPomProperty(properties, "talend.job.path", ETalendMavenVariables.JobPath, jobClassPackageFolder);
        checkPomProperty(properties, "talend.job.name", ETalendMavenVariables.JobName, "${project.artifactId}");
        String jobVersion = property.getVersion();
        if (getArgumentsMap().get(TalendProcessArgumentConstant.ARG_DEPLOY_VERSION) == null) {
            // if deploy version does not set
            jobVersion = "${project.version}";
        }
        checkPomProperty(properties, "talend.job.version", ETalendMavenVariables.JobVersion, jobVersion);
    }

    protected void afterCreate(IProgressMonitor monitor) throws Exception {
        // generate routines
        MavenPomSynchronizer pomSync = new MavenPomSynchronizer(this.getJobProcessor().getTalendJavaProject());
        pomSync.syncCodesPoms(monitor, this.getJobProcessor().getProcess(), true);
    }
}
