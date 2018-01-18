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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.model.Activation;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.Profile;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.process.JobInfo;
import org.talend.core.model.process.ProcessUtils;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.SVNConstant;
import org.talend.core.model.utils.JavaResourcesHelper;
import org.talend.core.runtime.process.JobInfoProperties;
import org.talend.core.runtime.process.LastGenerationInfo;
import org.talend.core.runtime.process.TalendProcessArgumentConstant;
import org.talend.core.runtime.projectsetting.IProjectSettingPreferenceConstants;
import org.talend.core.runtime.projectsetting.IProjectSettingTemplateConstants;
import org.talend.core.ui.ITestContainerProviderService;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.template.ETalendMavenVariables;
import org.talend.designer.maven.template.MavenTemplateManager;
import org.talend.designer.maven.tools.MavenPomSynchronizer;
import org.talend.designer.maven.utils.PomIdsHelper;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.designer.runprocess.IProcessor;
import org.talend.repository.ProjectManager;
import org.talend.utils.io.FilesUtils;

/**
 * created by ggu on 4 Feb 2015 Detailled comment
 *
 */
public class CreateMavenJobPom extends AbstractMavenProcessorPom {

    private String windowsClasspath, unixClasspath;

    private String windowsScriptAddition, unixScriptAddition;

    private IFile assemblyFile;

    public CreateMavenJobPom(IProcessor jobProcessor, IFile pomFile) {
        super(jobProcessor, pomFile, IProjectSettingTemplateConstants.POM_JOB_TEMPLATE_FILE_NAME);
    }

    public String getWindowsClasspath() {
        return this.windowsClasspath;
    }

    public void setWindowsClasspath(String windowsClasspath) {
        this.windowsClasspath = windowsClasspath;
    }

    public String getUnixClasspath() {
        return this.unixClasspath;
    }

    public void setUnixClasspath(String unixClasspath) {
        this.unixClasspath = unixClasspath;
    }

    public String getWindowsScriptAddition() {
        return windowsScriptAddition;
    }

    public void setWindowsScriptAddition(String windowsScriptAddition) {
        this.windowsScriptAddition = windowsScriptAddition;
    }

    public String getUnixScriptAddition() {
        return unixScriptAddition;
    }

    public void setUnixCcriptAddition(String unixScriptAddition) {
        this.unixScriptAddition = unixScriptAddition;
    }

    public IFile getAssemblyFile() {
        return assemblyFile;
    }

    public void setAssemblyFile(IFile assemblyFile) {
        this.assemblyFile = assemblyFile;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.maven.tools.creator.CreateMavenBundleTemplatePom#getTemplateStream()
     */
    @Override
    protected InputStream getTemplateStream() throws IOException {
        File templateFile = PomUtil.getTemplateFile(getObjectTypeFolder(), getItemRelativePath(),
                TalendMavenConstants.POM_FILE_NAME);
        if (!FilesUtils.allInSameFolder(templateFile, TalendMavenConstants.ASSEMBLY_FILE_NAME)) {
            templateFile = null; // force to set null, in order to use the template from other places.
        }
        try {
            final Map<String, Object> templateParameters = PomUtil.getTemplateParameters(getJobProcessor());
            return MavenTemplateManager.getTemplateStream(templateFile,
                    IProjectSettingPreferenceConstants.TEMPLATE_STANDALONE_JOB_POM, JOB_TEMPLATE_BUNDLE, getBundleTemplatePath(),
                    templateParameters);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    protected void configModel(Model model) {
        super.configModel(model);
        setProfiles(model);
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
        IProcess process = jProcessor.getProcess();
        final IContext context = jProcessor.getContext();
        Property property = jProcessor.getProperty();

        if (ProcessUtils.isTestContainer(process)) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class)) {
                ITestContainerProviderService testService = (ITestContainerProviderService) GlobalServiceRegister.getDefault()
                        .getService(ITestContainerProviderService.class);
                try {
                    property = testService.getParentJobItem(property.getItem()).getProperty();
                    process = testService.getParentJobProcess(process);
                } catch (PersistenceException e) {
                    ExceptionHandler.process(e);
                }
            }
        }

        // same as JavaProcessor.initCodePath
        String jobClassPackageFolder = JavaResourcesHelper.getJobClassPackageFolder(property.getItem());
        String jobClassPackage = JavaResourcesHelper.getJobClassPackageName(property.getItem());
        String jobFolderName = JavaResourcesHelper.getJobFolderName(property.getLabel(), property.getVersion());

        Project project = ProjectManager.getInstance().getProject(property);
        if (project == null) { // current project
            project = ProjectManager.getInstance().getCurrentProject().getEmfProject();
        }
        String mainProjectBranch = ProjectManager.getInstance().getMainProjectBranch(project);
        if (mainProjectBranch == null) {
            mainProjectBranch = SVNConstant.NAME_TRUNK;
        }

        checkPomProperty(properties, "talend.job.path", ETalendMavenVariables.JobPath, jobClassPackageFolder);
        checkPomProperty(properties, "talend.job.package", ETalendMavenVariables.JobPackage, jobClassPackage);

        /*
         * for jobInfo.properties
         * 
         * should be same as JobInfoBuilder
         */
        String contextName = getOptionString(TalendProcessArgumentConstant.ARG_CONTEXT_NAME);
        if (contextName == null) {
            contextName = context.getName();
        }

        JobInfoProperties jobInfoProp = new JobInfoProperties((ProcessItem) property.getItem(), contextName,
                isOptionChecked(TalendProcessArgumentConstant.ARG_ENABLE_APPLY_CONTEXT_TO_CHILDREN),
                isOptionChecked(TalendProcessArgumentConstant.ARG_ENABLE_STATS));

        checkPomProperty(properties, "talend.project.name", ETalendMavenVariables.ProjectName,
                jobInfoProp.getProperty(JobInfoProperties.PROJECT_NAME, project.getTechnicalLabel()));
        checkPomProperty(properties, "talend.project.name.lowercase", ETalendMavenVariables.ProjectName,
                jobInfoProp.getProperty(JobInfoProperties.PROJECT_NAME, project.getTechnicalLabel()).toLowerCase());
        checkPomProperty(properties, "talend.project.id", ETalendMavenVariables.ProjectId,
                jobInfoProp.getProperty(JobInfoProperties.PROJECT_ID, String.valueOf(project.getId())));
        checkPomProperty(properties, "talend.project.branch", ETalendMavenVariables.ProjectBranch,
                jobInfoProp.getProperty(JobInfoProperties.BRANCH, mainProjectBranch));

        checkPomProperty(properties, "talend.job.name", ETalendMavenVariables.JobName,
                jobInfoProp.getProperty(JobInfoProperties.JOB_NAME, property.getLabel()));

        checkPomProperty(properties, "talend.job.version", ETalendMavenVariables.TalendJobVersion, property.getVersion());

        checkPomProperty(properties, "maven.build.timestamp.format", ETalendMavenVariables.JobDateFormat,
                JobInfoProperties.JOB_DATE_FORMAT);

        checkPomProperty(properties, "talend.job.context", ETalendMavenVariables.JobContext,
                jobInfoProp.getProperty(JobInfoProperties.CONTEXT_NAME, context.getName()));
        checkPomProperty(properties, "talend.job.id", ETalendMavenVariables.JobId,
                jobInfoProp.getProperty(JobInfoProperties.JOB_ID, process.getId()));
        checkPomProperty(properties, "talend.job.type", ETalendMavenVariables.JobType,
                jobInfoProp.getProperty(JobInfoProperties.JOB_TYPE));
        if (process instanceof IProcess2) {
            String framework = (String) ((IProcess2) process).getAdditionalProperties().get("FRAMEWORK"); //$NON-NLS-1$
            if (framework == null) {
                framework = ""; //$NON-NLS-1$
            }
            checkPomProperty(properties, "talend.job.framework", ETalendMavenVariables.Framework, framework); //$NON-NLS-1$
        }

        // checkPomProperty(properties, "talend.job.class", ETalendMavenVariables.JobClass, jProcessor.getMainClass());
        checkPomProperty(properties, "talend.job.class", ETalendMavenVariables.JobClass,
                "${talend.job.package}.${talend.job.name}");

        checkPomProperty(properties, "talend.job.stat", ETalendMavenVariables.JobStat,
                jobInfoProp.getProperty(JobInfoProperties.ADD_STATIC_CODE, Boolean.FALSE.toString()));
        checkPomProperty(properties, "talend.job.applyContextToChildren", ETalendMavenVariables.JobApplyContextToChildren,
                jobInfoProp.getProperty(JobInfoProperties.APPLY_CONTEXY_CHILDREN, Boolean.FALSE.toString()));
        checkPomProperty(properties, "talend.product.version", ETalendMavenVariables.ProductVersion,
                jobInfoProp.getProperty(JobInfoProperties.COMMANDLINE_VERSION, VersionUtils.getVersion()));
        /*
         * for bat/sh in assembly
         */
        StringBuffer windowsScriptAdditionValue = new StringBuffer(50);
        StringBuffer unixScriptAdditionValue = new StringBuffer(50);

        //
        addScriptAddition(windowsScriptAdditionValue, this.getWindowsScriptAddition());
        addScriptAddition(unixScriptAdditionValue, this.getUnixScriptAddition());

        // context
        if (isOptionChecked(TalendProcessArgumentConstant.ARG_NEED_CONTEXT)) {
            final String contextPart = TalendProcessArgumentConstant.CMD_ARG_CONTEXT_NAME + contextName;
            addScriptAddition(windowsScriptAdditionValue, contextPart);
            addScriptAddition(unixScriptAdditionValue, contextPart);
        }
        // context params
        List paramsList = ProcessUtils.getOptionValue(getArgumentsMap(), TalendProcessArgumentConstant.ARG_CONTEXT_PARAMS,
                (List) null);
        if (paramsList != null && !paramsList.isEmpty()) {
            StringBuffer contextParamPart = new StringBuffer(100);
            // do codes same as JobScriptsManager.getSettingContextParametersValue
            for (Object param : paramsList) {
                if (param instanceof ContextParameterType) {
                    ContextParameterType contextParamType = (ContextParameterType) param;
                    contextParamPart.append(' ');
                    contextParamPart.append(TalendProcessArgumentConstant.CMD_ARG_CONTEXT_PARAMETER);
                    contextParamPart.append(' ');
                    contextParamPart.append(contextParamType.getName());
                    contextParamPart.append('=');

                    String value = contextParamType.getRawValue();
                    if (!contextParamType.getType().equals("id_Password")) { //$NON-NLS-1$
                        value = StringEscapeUtils.escapeJava(value);
                    }
                    if (value == null) {
                        contextParamPart.append((String) null);
                    } else {
                        value = TalendQuoteUtils.addPairQuotesIfNotExist(value);
                        contextParamPart.append(value);
                    }
                }
            }
            if (contextParamPart.length() > 0) {
                addScriptAddition(windowsScriptAdditionValue, contextParamPart.toString());
                addScriptAddition(unixScriptAdditionValue, contextParamPart.toString());
            }
        }

        // log4j level
        if (isOptionChecked(TalendProcessArgumentConstant.ARG_ENABLE_LOG4J)
                && isOptionChecked(TalendProcessArgumentConstant.ARG_NEED_LOG4J_LEVEL)) {
            String log4jLevel = getOptionString(TalendProcessArgumentConstant.ARG_LOG4J_LEVEL);
            if (StringUtils.isNotEmpty(log4jLevel)) {
                String log4jLevelPart = log4jLevel;
                if (!log4jLevel.startsWith(TalendProcessArgumentConstant.CMD_ARG_LOG4J_LEVEL)) {
                    log4jLevelPart = TalendProcessArgumentConstant.CMD_ARG_LOG4J_LEVEL + log4jLevel;
                }
                addScriptAddition(windowsScriptAdditionValue, log4jLevelPart);
                addScriptAddition(unixScriptAdditionValue, log4jLevelPart);
            }
        }

        // stats
        if (isOptionChecked(TalendProcessArgumentConstant.ARG_ENABLE_STATS)) {
            String statsPort = getOptionString(TalendProcessArgumentConstant.ARG_PORT_STATS);
            if (StringUtils.isNotEmpty(statsPort)) {
                String statsPortPart = TalendProcessArgumentConstant.CMD_ARG_STATS_PORT + statsPort;
                addScriptAddition(windowsScriptAdditionValue, statsPortPart);
                addScriptAddition(unixScriptAdditionValue, statsPortPart);
            }
        }
        // tracs
        if (isOptionChecked(TalendProcessArgumentConstant.ARG_ENABLE_TRACS)) {
            String tracPort = getOptionString(TalendProcessArgumentConstant.ARG_PORT_TRACS);
            if (StringUtils.isNotEmpty(tracPort)) {
                String tracPortPart = TalendProcessArgumentConstant.CMD_ARG_TRACE_PORT + tracPort;
                addScriptAddition(windowsScriptAdditionValue, tracPortPart);
                addScriptAddition(unixScriptAdditionValue, tracPortPart);
            }
        }
        // watch
        String watchParam = getOptionString(TalendProcessArgumentConstant.ARG_ENABLE_WATCH);
        if (StringUtils.isNotEmpty(watchParam)) {
            addScriptAddition(windowsScriptAdditionValue, TalendProcessArgumentConstant.CMD_ARG_WATCH);
            addScriptAddition(unixScriptAdditionValue, TalendProcessArgumentConstant.CMD_ARG_WATCH);
        }

        String[] jvmArgs = jProcessor.getJVMArgs();
        StringBuffer jvmArgsStr = new StringBuffer();
        if (jvmArgs != null && jvmArgs.length > 0) {
            for (String arg : jvmArgs) {
                jvmArgsStr.append(arg);
                jvmArgsStr.append(' ');
            }
        }
        checkPomProperty(properties, "talend.job.jvmargs", ETalendMavenVariables.JobJvmArgs, jvmArgsStr.toString());

        checkPomProperty(properties, "talend.job.bat.classpath", ETalendMavenVariables.JobBatClasspath,
                this.getWindowsClasspath());
        checkPomProperty(properties, "talend.job.bat.addition", ETalendMavenVariables.JobBatAddition,
                windowsScriptAdditionValue.toString());

        checkPomProperty(properties, "talend.job.sh.classpath", ETalendMavenVariables.JobShClasspath, this.getUnixClasspath());
        checkPomProperty(properties, "talend.job.sh.addition", ETalendMavenVariables.JobShAddition,
                unixScriptAdditionValue.toString());

        String finalNameStr = JavaResourcesHelper.getJobJarName(property.getLabel(), property.getVersion());
        checkPomProperty(properties, "talend.job.finalName", ETalendMavenVariables.JobFinalName, finalNameStr);

    }

    private void addScriptAddition(StringBuffer scripts, String value) {
        if (StringUtils.isNotEmpty(value)) {
            scripts.append(' '); // separator
            scripts.append(value);
        }
    }

    @Override
    protected boolean validChildrenJob(JobInfo jobInfo) {
        JobInfo fatherJobInfo = null;
        for (JobInfo lastGeneratedJobInfo : LastGenerationInfo.getInstance().getLastGeneratedjobs()) {
            if (lastGeneratedJobInfo.getJobId().equals(getJobProcessor().getProperty().getId())
                    && lastGeneratedJobInfo.getJobVersion().equals(getJobProcessor().getProperty().getVersion())) {
                fatherJobInfo = lastGeneratedJobInfo;
                break;
            }
        }
        while (fatherJobInfo != null) {
            if (fatherJobInfo.getJobId().equals(jobInfo.getJobId())
                    && fatherJobInfo.getJobVersion().equals(jobInfo.getJobVersion())) {
                return false;
            }
            fatherJobInfo = fatherJobInfo.getFatherJobInfo();
        }
        // for job, ignore test container for children.
        return jobInfo != null && !jobInfo.isTestContainer();
    }

    protected void setProfiles(Model model) {
        // log4j
        if (isOptionChecked(TalendProcessArgumentConstant.ARG_ENABLE_LOG4J)) {
            // enable it by default
            setDefaultActivationForProfile(model, TalendMavenConstants.PROFILE_INCLUDE_LOG4J, true);
            setDefaultActivationForProfile(model, TalendMavenConstants.PROFILE_INCLUDE_RUNNING_LOG4J, true);
        }
        // xmlMappings
        if (isOptionChecked(TalendProcessArgumentConstant.ARG_NEED_XMLMAPPINGS)) {
            setDefaultActivationForProfile(model, TalendMavenConstants.PROFILE_INCLUDE_XMLMAPPINGS, true);
            setDefaultActivationForProfile(model, TalendMavenConstants.PROFILE_INCLUDE_RUNNING_XMLMAPPINGS, true);
        }
        // rules
        if (isOptionChecked(TalendProcessArgumentConstant.ARG_NEED_RULES)) {
            setDefaultActivationForProfile(model, TalendMavenConstants.PROFILE_INCLUDE_RULES, true);
        }
        // pigudfs
        if (isOptionChecked(TalendProcessArgumentConstant.ARG_NEED_PIGUDFS)) {
            setDefaultActivationForProfile(model, TalendMavenConstants.PROFILE_INCLUDE_PIGUDFS_JAVA_SOURCES, true);
            setDefaultActivationForProfile(model, TalendMavenConstants.PROFILE_INCLUDE_PIGUDFS_BINARIES, true);
        }
    }

    private void setDefaultActivationForProfile(Model model, String profileId, boolean activeByDefault) {
        if (profileId == null || model == null) {
            return;
        }
        Profile foundProfile = null;
        for (Profile p : model.getProfiles()) {
            if (profileId.equals(p.getId())) {
                foundProfile = p;
                break;
            }
        }
        if (foundProfile != null) {
            Activation activation = foundProfile.getActivation();
            if (activation == null) {
                activation = new Activation();
                foundProfile.setActivation(activation);
            }
            activation.setActiveByDefault(activeByDefault);
        }
    }

    @Override
    protected void afterCreate(IProgressMonitor monitor) throws Exception {
        setPomForHDInsight(monitor);

        // // check for children jobs
        // Set<String> childrenGroupIds = new HashSet<>();
        // final Set<JobInfo> clonedChildrenJobInfors = getJobProcessor().getBuildChildrenJobs();
        // // main job built, should never be in the children list, even if recursive
        // clonedChildrenJobInfors.remove(LastGenerationInfo.getInstance().getLastMainJob());

        // for (JobInfo child : clonedChildrenJobInfors) {
        // if (child.getFatherJobInfo() != null) {
        // Property childProperty = null;
        // ProcessItem childItem = child.getProcessItem();
        // if (childItem != null) {
        // childProperty = childItem.getProperty();
        // } else {
        // String jobId = child.getJobId();
        // if (jobId != null) {
        // IProxyRepositoryFactory proxyRepositoryFactory = CoreRuntimePlugin.getInstance()
        // .getProxyRepositoryFactory();
        // IRepositoryViewObject specificVersion = proxyRepositoryFactory.getSpecificVersion(jobId,
        // child.getJobVersion(), true);
        // if (specificVersion != null) {
        // childProperty = specificVersion.getProperty();
        // }
        // }
        // }
        //
        // if (childProperty != null) {
        // final String childGroupId = PomIdsHelper.getJobGroupId(childProperty);
        // if (childGroupId != null) {
        // childrenGroupIds.add(childGroupId);
        // }
        // }
        // }
        // }

        generateAssemblyFile(monitor, null);

        // final IProcess process = getJobProcessor().getProcess();
        // Map<String, Object> args = new HashMap<String, Object>();
        // args.put(IPomJobExtension.KEY_PROCESS, process);
        // args.put(IPomJobExtension.KEY_ASSEMBLY_FILE, getAssemblyFile());
        // args.put(IPomJobExtension.KEY_CHILDREN_JOBS_GROUP_IDS, childrenGroupIds);
        //
        // PomJobExtensionRegistry.getInstance().updatePom(monitor, getPomFile(), args);

        MavenPomSynchronizer pomSync = new MavenPomSynchronizer(this.getJobProcessor());
        // if (needSyncCodesPoms()) {
        // // only sync pom for main job
        // pomSync.syncCodesPoms(monitor, getJobProcessor(), true);
        // }
        // because need update the latest content for templates.
        pomSync.syncTemplates(true);

    }

    private void setPomForHDInsight(IProgressMonitor monitor) {
        if (ProcessUtils.jarNeedsToContainContext()) {
            try {
                Model model = MODEL_MANAGER.readMavenModel(getPomFile());
                List<Plugin> plugins = new ArrayList<Plugin>(model.getBuild().getPlugins());
                out: for (Plugin plugin : plugins) {
                    if (plugin.getArtifactId().equals("maven-jar-plugin")) { //$NON-NLS-1$
                        List<PluginExecution> pluginExecutions = plugin.getExecutions();
                        for (PluginExecution pluginExecution : pluginExecutions) {
                            if (pluginExecution.getId().equals("default-jar")) { //$NON-NLS-1$
                                Object object = pluginExecution.getConfiguration();
                                if (object instanceof Xpp3Dom) {
                                    Xpp3Dom configNode = (Xpp3Dom) object;
                                    Xpp3Dom includesNode = configNode.getChild("includes"); //$NON-NLS-1$
                                    Xpp3Dom includeNode = new Xpp3Dom("include"); //$NON-NLS-1$
                                    includeNode.setValue("${talend.job.path}/contexts/*.properties"); //$NON-NLS-1$
                                    includesNode.addChild(includeNode);

                                    model.getBuild().setPlugins(plugins);
                                    PomUtil.savePom(monitor, model, getPomFile());
                                    break out;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
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
                String content = MavenTemplateManager.getTemplateContent(templateFile,
                        IProjectSettingPreferenceConstants.TEMPLATE_STANDALONE_JOB_ASSEMBLY, JOB_TEMPLATE_BUNDLE,
                        IProjectSettingTemplateConstants.PATH_STANDALONE + '/'
                                + IProjectSettingTemplateConstants.ASSEMBLY_JOB_TEMPLATE_FILE_NAME, templateParameters);
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

    public void updateDependencySet(IFile assemblyFile) {
        final String SEPARATOR = System.getProperty("line.separator"); //$NON-NLS-1$

        String talendlibIncludesTag = "<!--@TalendLibIncludes@-->"; //$NON-NLS-1$
        String _3rdlibExcludesTag = "<!--@3rdPartyLibIncludes@-->"; //$NON-NLS-1$
        String jobIncludesTag = "<!--@JobIncludes@-->"; //$NON-NLS-1$

        StringBuilder talendlibIncludes = new StringBuilder();
        StringBuilder _3rdPartylibExcludes = new StringBuilder();
        StringBuilder jobIncludes = new StringBuilder();
        
        // add children jobs
        Set<JobInfo> childrenJobInfo = getJobProcessor().getBuildChildrenJobs();
        Set<String> childrenCoordinate = new HashSet<>();
        for (JobInfo jobInfo : childrenJobInfo) {
            Property property = jobInfo.getProcessItem().getProperty();
            String groupId = PomIdsHelper.getJobGroupId(property);
            String artifactId = PomIdsHelper.getJobArtifactId(jobInfo);
            String coordinate = groupId + ":" + artifactId; //$NON-NLS-1$
            addItem(jobIncludes, coordinate, SEPARATOR);
            childrenCoordinate.add(coordinate);
        }
        // add parent job
        Property parentProperty = this.getJobProcessor().getProperty();
        String parentCoordinate = PomIdsHelper.getJobGroupId(parentProperty) + ":" //$NON-NLS-1$
                + PomIdsHelper.getJobArtifactId(parentProperty);
        addItem(jobIncludes, parentCoordinate, SEPARATOR);
        
        
        try {
            Set<String> talendLibCoordinate = new HashSet<>();
            Model model = MavenPlugin.getMavenModelManager().readMavenModel(getPomFile());
            List<Dependency> dependencies = model.getDependencies();
            // add talend libraries and codes
            for (Dependency dependency : dependencies) {
                String coordinate = dependency.getGroupId() + ":" + dependency.getArtifactId(); //$NON-NLS-1$
                if (!childrenCoordinate.contains(coordinate)) {
                    addItem(talendlibIncludes, coordinate, SEPARATOR);
                    talendLibCoordinate.add(coordinate);
                }
            }
            // add 3rd party libraries
            for (Dependency dependency : dependencies) {
                String coordinate = dependency.getGroupId() + ":" + dependency.getArtifactId(); //$NON-NLS-1$
                if (!childrenCoordinate.contains(coordinate) && !talendLibCoordinate.contains(coordinate)) {
                    addItem(_3rdPartylibExcludes, coordinate, SEPARATOR);
                }
                if (_3rdPartylibExcludes.length() == 0) {
                    addItem(_3rdPartylibExcludes, "null:null", SEPARATOR);
                }
            }
        } catch (CoreException e) {
            ExceptionHandler.process(e);
        }
        
        String talendLibIncludesStr = StringUtils.removeEnd(talendlibIncludes.toString(), SEPARATOR);
        String _3rdPartylibExcludesStr = StringUtils.removeEnd(_3rdPartylibExcludes.toString(), SEPARATOR);
        String jobIncludesStr = StringUtils.removeEnd(jobIncludes.toString(), SEPARATOR);
        String content = org.talend.commons.utils.io.FilesUtils.readFileContent(assemblyFile);
        content = StringUtils.replaceEach(content, new String[] { talendlibIncludesTag, _3rdlibExcludesTag, jobIncludesTag },
                new String[] { talendLibIncludesStr, _3rdPartylibExcludesStr, jobIncludesStr });
        org.talend.commons.utils.io.FilesUtils.writeContentToFile(content, assemblyFile);
    }

    private void addItem(StringBuilder builder, String coordinate, String separator) {
        if(builder.length() > 0) {
            builder.append("\t\t\t\t"); //$NON-NLS-1$
        }
        builder.append("<include>"); //$NON-NLS-1$
        builder.append(coordinate);
        builder.append("</include>"); //$NON-NLS-1$
        builder.append(separator);
    }

}
