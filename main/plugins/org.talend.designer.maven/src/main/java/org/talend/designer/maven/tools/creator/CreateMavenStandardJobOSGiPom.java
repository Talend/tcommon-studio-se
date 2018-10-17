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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.Profile;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.PluginChecker;
import org.talend.core.model.process.JobInfo;
import org.talend.core.model.properties.Property;
import org.talend.core.model.relationship.Relation;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.core.runtime.process.TalendProcessArgumentConstant;
import org.talend.core.runtime.process.TalendProcessOptionConstants;
import org.talend.core.runtime.projectsetting.IProjectSettingPreferenceConstants;
import org.talend.core.runtime.projectsetting.IProjectSettingTemplateConstants;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.template.MavenTemplateManager;
import org.talend.designer.maven.tools.AggregatorPomsHelper;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.designer.runprocess.IProcessor;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 * 
 * @see OSGIJavaScriptForESBWithMavenManager to build job
 */
public class CreateMavenStandardJobOSGiPom extends CreateMavenJobPom {

    /**
     * DOC yyan CreateMavenStandardJobOSGiPom constructor comment.
     * 
     * @param jobProcessor
     * @param pomFile
     */
    public CreateMavenStandardJobOSGiPom(IProcessor jobProcessor, IFile pomFile) {
        super(jobProcessor, pomFile);
    }

    private Model model;

    protected String getBundleTemplatePath() {
        return IProjectSettingTemplateConstants.PATH_OSGI_BUNDLE + '/'
                + IProjectSettingTemplateConstants.POM_JOB_TEMPLATE_FILE_NAME;
    }

    @Override
    protected InputStream getTemplateStream() throws IOException {

        File templateFile = PomUtil.getTemplateFile(getObjectTypeFolder(), getItemRelativePath(),
                IProjectSettingTemplateConstants.OSGI_POM_FILE_NAME);
        try {

            InputStream bundleTemplateStream = MavenTemplateManager.getBundleTemplateStream(JOB_TEMPLATE_BUNDLE,
                    getBundleTemplatePath());
            if (bundleTemplateStream != null) {
                return bundleTemplateStream;
            }

            final Map<String, Object> templateParameters = PomUtil.getTemplateParameters(getJobProcessor());
            return MavenTemplateManager.getTemplateStream(templateFile,
                    IProjectSettingPreferenceConstants.TEMPLATE_OSGI_BUNDLE_POM, PluginChecker.MAVEN_JOB_PLUGIN_ID,
                    getBundleTemplatePath(), templateParameters);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.maven.tools.creator.CreateMaven#getArgumentsMap()
     */
    @Override
    public Map<String, Object> getArgumentsMap() {
        Map<String, Object> argumentsMap = new HashMap<String, Object>(super.getArgumentsMap());
        argumentsMap.put(TalendProcessArgumentConstant.ARG_GENERATE_OPTION,
                TalendProcessOptionConstants.GENERATE_WITHOUT_COMPILING);
        return argumentsMap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.maven.tools.creator.AbstractMavenProcessorPom#createModel()
     */
    @Override
    protected Model createModel() {
        Model model = super.createModel();

        List<Profile> profiles = model.getProfiles();

        for (Profile profile : profiles) {

            if (profile.getId().equals("packaging-and-assembly")) {
                List<Plugin> plugins = profile.getBuild().getPlugins();

                for (Plugin plugin : plugins) {
                    if (plugin.getArtifactId().equals("maven-assembly-plugin")) {
                        PluginExecution pluginExecution = plugin.getExecutionsAsMap().get("default");
                        Xpp3Dom configuration = (Xpp3Dom) pluginExecution.getConfiguration();

                        Xpp3Dom archive = new Xpp3Dom("archive");
                        Xpp3Dom manifestFile = new Xpp3Dom("manifestFile");
                        manifestFile.setValue("${current.bundle.resources.dir}/META-INF/MANIFEST.MF");

                        archive.addChild(manifestFile);

                        configuration.addChild(archive);
                    }
                }
            }
        }
        model.setName(model.getName() + " Bundle");
        if (isServiceOperation(getJobProcessor().getProperty())) {
            model.addProperty("cloud.publisher.skip", "true");
            Build build = model.getBuild();
            if(build != null) {
                List<Plugin> plugins = build.getPlugins();
                for(Plugin p : plugins) {
                    if(p.getArtifactId().equals("maven-deploy-plugin")) {
                        build.removePlugin(p);
                        break;
                    }
                }
            }
        }

        return model;
    }

    protected void generateAssemblyFile(IProgressMonitor monitor, final Set<JobInfo> clonedChildrenJobInfors) throws Exception {
        IFile assemblyFile = this.getAssemblyFile();
        if (assemblyFile != null) {
            boolean set = false;
            // read template from project setting
            try {
                File templateFile = PomUtil.getTemplateFile(getObjectTypeFolder(), getItemRelativePath(),
                        TalendMavenConstants.ASSEMBLY_FILE_NAME);
                if (!FilesUtils.allInSameFolder(templateFile, TalendMavenConstants.POM_FILE_NAME)) {
                    templateFile = null; // force to set null, in order to use the template from other places.
                }

                final Map<String, Object> templateParameters = PomUtil.getTemplateParameters(getJobProcessor());
                String content = MavenTemplateManager.getTemplateContent(templateFile, null, JOB_TEMPLATE_BUNDLE,
                        IProjectSettingTemplateConstants.PATH_OSGI_BUNDLE + '/'
                                + IProjectSettingTemplateConstants.ASSEMBLY_JOB_TEMPLATE_FILE_NAME,
                        templateParameters);
                if (content != null) {
                    ByteArrayInputStream source = new ByteArrayInputStream(content.getBytes());
                    if (assemblyFile.exists()) {
                        assemblyFile.setContents(source, true, false, monitor);
                    } else {
                        assemblyFile.create(source, true, monitor);
                    }
                    updateDependencySet(assemblyFile);
                    set = true;
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.designer.maven.tools.creator.CreateMavenJobPom#afterCreate(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    protected void afterCreate(IProgressMonitor monitor) throws Exception {
        ITalendProcessJavaProject jobProject = getJobProcessor().getTalendJavaProject();
        if (jobProject != null) {
            PomUtil.backupPomFile(jobProject);
        } else {
            IFolder jobPomFolder = AggregatorPomsHelper.getItemPomFolder(getJobProcessor().getProperty());
            PomUtil.backupPomFile(jobPomFolder);
        }
        super.afterCreate(monitor);
    }

    /**
     * Find service relation for ESB data service
     * 
     * @param property
     * @return
     */
    public boolean isServiceOperation(Property property) {
        List<Relation> relations = RelationshipItemBuilder.getInstance().getItemsRelatedTo(property.getId(),
                property.getVersion(), RelationshipItemBuilder.JOB_RELATION);

        for (Relation relation : relations) {
            if (RelationshipItemBuilder.SERVICES_RELATION.equals(relation.getType())) {
                return true;
            }
        }
        return false;
    }
}
