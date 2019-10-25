// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.model.Build;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.Profile;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.IESBService;
import org.talend.core.PluginChecker;
import org.talend.core.model.process.JobInfo;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.relationship.Relation;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.core.runtime.process.TalendProcessArgumentConstant;
import org.talend.core.runtime.process.TalendProcessOptionConstants;
import org.talend.core.runtime.projectsetting.IProjectSettingPreferenceConstants;
import org.talend.core.runtime.projectsetting.IProjectSettingTemplateConstants;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.template.MavenTemplateManager;
import org.talend.designer.maven.tools.AggregatorPomsHelper;
import org.talend.designer.maven.utils.PomIdsHelper;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.designer.runprocess.IProcessor;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.utils.io.FilesUtils;
import org.w3c.dom.Document;

/**
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

    @Override
    public Map<String, Object> getArgumentsMap() {
        Map<String, Object> argumentsMap = new HashMap<String, Object>(super.getArgumentsMap());
        argumentsMap.put(TalendProcessArgumentConstant.ARG_GENERATE_OPTION,
                TalendProcessOptionConstants.GENERATE_WITHOUT_COMPILING);
        return argumentsMap;
    }

    @Override
    protected Model createModel() {
        Model model = super.createModel();
        
        boolean isServiceOperation = isServiceOperation(getJobProcessor().getProperty());

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

            // remove deploy plugin for service operation
            if (isServiceOperation && profile.getId().equals("deploy-nexus")) {
                model.removeProfile(profile);
                break;
            }
        }
        model.setName(model.getName() + " Bundle");
        model.addProperty("talend.job.finalName", "${talend.job.name}-bundle-${project.version}");
        Build build = model.getBuild();
        
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IESBService.class)) {
            IESBService service = (IESBService) GlobalServiceRegister.getDefault().getService(IESBService.class);

            if (isServiceOperation || service.isRESTService((ProcessItem) getJobProcessor().getProperty().getItem())
                    || isRouteOperation(getJobProcessor().getProperty())) {
                build.addPlugin(addSkipDockerMavenPlugin());
            }
        }
        
        if (isServiceOperation) {
            model.addProperty("cloud.publisher.skip", "true");
            
            build = model.getBuild();

            List<Plugin> removePlugins = new ArrayList<Plugin>();
            if (build != null) {

                List<Plugin> plugins = build.getPlugins();
                for (Plugin p : plugins) {
                    if (p.getArtifactId().equals("maven-deploy-plugin")) {
                        removePlugins.add(p);
                    }
                    if (p.getArtifactId().equals("maven-bundle-plugin")) {
                        removePlugins.add(p);
                    }
                }
            }

            for (Plugin p : removePlugins) {
                build.removePlugin(p);
            }
        } else {
            model.setPackaging("bundle");
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

    @Override
    protected void updateDependencySet(IFile assemblyFile) {
        Map<String, Dependency> jobCoordinateMap = new HashMap<String, Dependency>();
        if (!hasLoopDependency()) {
            // add children jobs
            Set<JobInfo> childrenJobInfo = getJobProcessor().getBuildChildrenJobs();
            for (JobInfo jobInfo : childrenJobInfo) {
                Property property = jobInfo.getProcessItem().getProperty();
                String coordinate = getCoordinate(PomIdsHelper.getJobGroupId(property), PomIdsHelper.getJobArtifactId(jobInfo),
                        MavenConstants.PACKAGING_JAR, PomIdsHelper.getJobVersion(property));
                Dependency dependency = getDependencyObject(PomIdsHelper.getJobGroupId(property), PomIdsHelper.getJobArtifactId(jobInfo), PomIdsHelper.getJobVersion(property),
                                MavenConstants.PACKAGING_JAR, null);
                jobCoordinateMap.put(coordinate, dependency);
            }
        }
        // add parent job
        Property parentProperty = this.getJobProcessor().getProperty();
        String parentCoordinate = getCoordinate(PomIdsHelper.getJobGroupId(parentProperty),
                PomIdsHelper.getJobArtifactId(parentProperty), MavenConstants.PACKAGING_JAR,
                PomIdsHelper.getJobVersion(parentProperty));
        Dependency parentDependency = getDependencyObject(PomIdsHelper.getJobGroupId(parentProperty), PomIdsHelper.getJobArtifactId(parentProperty), PomIdsHelper.getJobVersion(parentProperty),
                        MavenConstants.PACKAGING_JAR, null);
        jobCoordinateMap.put(parentCoordinate, parentDependency);
        try {
            Document document = PomUtil.loadAssemblyFile(null, assemblyFile);
            // add jobs
            setupDependencySetNode(document, jobCoordinateMap, null, "${artifact.build.finalName}.${artifact.extension}", true,
                    true);
            PomUtil.saveAssemblyFile(assemblyFile, document);
        } catch (Exception e) {
            ExceptionHandler.process(e);
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
        List<IRepositoryViewObject> serviceRepoList = null;

        boolean isDataServiceOperation = false;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IESBService.class)) {
            IESBService service = (IESBService) GlobalServiceRegister.getDefault().getService(IESBService.class);
            try {
                IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
                serviceRepoList = factory.getAll(ERepositoryObjectType.valueOf(ERepositoryObjectType.class, "SERVICES"));

                for (IRepositoryViewObject serviceItem : serviceRepoList) {
                    if (service != null) {
                        List<String> jobIds = service.getSerivceRelatedJobIds(serviceItem.getProperty().getItem());
                        if (jobIds.contains(property.getId())) {
                            isDataServiceOperation = true;
                            break;
                        }
                    }
                }

            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }
        }

        return isDataServiceOperation;
    }

    public boolean isRouteOperation(Property property) {
        List<IRepositoryViewObject> routeRepoList = null;

        boolean isRouteOperation = false;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IESBService.class)) {

            IESBService service = (IESBService) GlobalServiceRegister.getDefault().getService(IESBService.class);

            try {
                IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
                routeRepoList = factory.getAll(ERepositoryObjectType.valueOf(ERepositoryObjectType.class, "ROUTE"));

                for (IRepositoryViewObject routeItem : routeRepoList) {
                    if (service != null) {

                        List<Relation> relations = RelationshipItemBuilder.getInstance().getItemsRelatedTo(routeItem.getId(),
                                routeItem.getVersion(), RelationshipItemBuilder.JOB_RELATION);
                        for (Relation relation : relations) {
                            if (relation.getType() == RelationshipItemBuilder.JOB_RELATION) {
                                if (relation.getId().equals(property.getId())) {
                                    isRouteOperation = true;
                                }
                            }
                        }
                    }
                }

            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }
        }
        return isRouteOperation;
    }
}
