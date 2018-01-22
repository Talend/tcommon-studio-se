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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.model.Activation;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.Profile;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ICoreService;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.process.JobInfo;
import org.talend.core.model.process.ProcessUtils;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.SVNConstant;
import org.talend.core.model.utils.JavaResourcesHelper;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.process.JobInfoProperties;
import org.talend.core.runtime.process.LastGenerationInfo;
import org.talend.core.runtime.process.TalendProcessArgumentConstant;
import org.talend.core.runtime.projectsetting.IProjectSettingPreferenceConstants;
import org.talend.core.runtime.projectsetting.IProjectSettingTemplateConstants;
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
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.utils.io.FilesUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
        final IProcess process = jProcessor.getProcess();
        final IContext context = jProcessor.getContext();
        final Property property = jProcessor.getProperty();

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
        checkPomProperty(properties, "talend.routine.groupid", ETalendMavenVariables.RoutineGroupId,
                PomIdsHelper.getCodesGroupId(TalendMavenConstants.DEFAULT_CODE));
        checkPomProperty(properties, "talend.pigudf.groupid", ETalendMavenVariables.PigudfGroupId,
                PomIdsHelper.getCodesGroupId(TalendMavenConstants.DEFAULT_PIGUDF));
        checkPomProperty(properties, "talend.project.id", ETalendMavenVariables.ProjectId,
                jobInfoProp.getProperty(JobInfoProperties.PROJECT_ID, String.valueOf(project.getId())));
        checkPomProperty(properties, "talend.project.branch", ETalendMavenVariables.ProjectBranch,
                jobInfoProp.getProperty(JobInfoProperties.BRANCH, mainProjectBranch));

        checkPomProperty(properties, "talend.job.name", ETalendMavenVariables.JobName,
                jobInfoProp.getProperty(JobInfoProperties.JOB_NAME, property.getLabel()));

        checkPomProperty(properties, "talend.job.version", ETalendMavenVariables.TalendJobVersion, property.getVersion());

        checkPomProperty(properties, "talend.job.date", ETalendMavenVariables.JobDate,
                jobInfoProp.getProperty(JobInfoProperties.DATE, JobInfoProperties.DATAFORMAT.format(new Date())));

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
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ICoreService.class)) {
                ICoreService coreService = (ICoreService) GlobalServiceRegister.getDefault().getService(ICoreService.class);
                coreService.synchronizeMapptingXML();
                coreService.syncLog4jSettings();
            }
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

        // check for children jobs
        Set<String> childrenGroupIds = new HashSet<>();
        final Set<JobInfo> clonedChildrenJobInfors = getJobProcessor().getBuildChildrenJobs();
        // main job built, should never be in the children list, even if recursive
        clonedChildrenJobInfors.remove(LastGenerationInfo.getInstance().getLastMainJob());

        for (JobInfo child : clonedChildrenJobInfors) {
            if (child.getFatherJobInfo() != null) {
                Property childProperty = null;
                ProcessItem childItem = child.getProcessItem();
                if (childItem != null) {
                    childProperty = childItem.getProperty();
                } else {
                    String jobId = child.getJobId();
                    if (jobId != null) {
                        IProxyRepositoryFactory proxyRepositoryFactory = CoreRuntimePlugin.getInstance()
                                .getProxyRepositoryFactory();
                        IRepositoryViewObject specificVersion = proxyRepositoryFactory.getSpecificVersion(jobId,
                                child.getJobVersion(), true);
                        if (specificVersion != null) {
                            childProperty = specificVersion.getProperty();
                        }
                    }
                }

                if (childProperty != null) {
                    final String childGroupId = PomIdsHelper.getJobGroupId(childProperty);
                    if (childGroupId != null) {
                        childrenGroupIds.add(childGroupId);
                    }
                }
            }
        }

        generateAssemblyFile(monitor, clonedChildrenJobInfors);

        final IProcess process = getJobProcessor().getProcess();
        Map<String, Object> args = new HashMap<String, Object>();
        args.put(IPomJobExtension.KEY_PROCESS, process);
        args.put(IPomJobExtension.KEY_ASSEMBLY_FILE, getAssemblyFile());
        args.put(IPomJobExtension.KEY_CHILDREN_JOBS_GROUP_IDS, childrenGroupIds);

        PomJobExtensionRegistry.getInstance().updatePom(monitor, getPomFile(), args);

        // generate routines
        MavenPomSynchronizer pomSync = new MavenPomSynchronizer(this.getJobProcessor());
        pomSync.setArgumentsMap(getArgumentsMap());
        if (needSyncCodesPoms()) {
            // only sync pom for main job
            pomSync.syncCodesPoms(monitor, getJobProcessor(), true);
        }
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

            try {
                checkCreatingFile(monitor, assemblyFile);
            } catch (Exception e) {
                ExceptionHandler.process(e);
                return;
            }

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
                    set = true;
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }

            if (set) {
                // add children resources in assembly.
                addChildrenJobsInAssembly(monitor, assemblyFile, clonedChildrenJobInfors);
            }
        }
    }

    @SuppressWarnings("nls")
    protected void addChildrenJobsInAssembly(IProgressMonitor monitor, IFile assemblyFile,
            final Set<JobInfo> clonedChildrenJobInfors) throws Exception {
        if (!assemblyFile.exists()) {
            return;
        }
        Document document = PomUtil.loadAssemblyFile(monitor, assemblyFile);
        if (document == null) {
            throw new IOException("Can't parse the file: " + assemblyFile.getLocation());
        }

        // files
        Node filesElem = getElement(document.getDocumentElement(), "files", 1);

        // fileSets
        Node fileSetsElem = getElement(document.getDocumentElement(), "fileSets", 1);
        if (fileSetsElem == null) {
            fileSetsElem = document.createElement("fileSets");
            document.appendChild(fileSetsElem);
        }

        List<String> childrenPomsIncludes = new ArrayList<String>();
        List<String> childrenFolderResourcesIncludes = new ArrayList<String>();

        for (JobInfo child : clonedChildrenJobInfors) {
            if (child.getFatherJobInfo() != null) {

                String jobClassPackageFolder = null;
                if (child.getProcessItem() != null) {
                    jobClassPackageFolder = JavaResourcesHelper.getJobClassPackageFolder(child.getProcessItem());
                } else {
                    String projectName = null;
                    String jobId = child.getJobId();
                    if (jobId != null) {
                        IProxyRepositoryFactory proxyRepositoryFactory = CoreRuntimePlugin.getInstance()
                                .getProxyRepositoryFactory();
                        IRepositoryViewObject lastVersion = proxyRepositoryFactory.getLastVersion(jobId);
                        if (lastVersion != null) {
                            Property property = lastVersion.getProperty();
                            if (property != null) {
                                Project project = ProjectManager.getInstance().getProject(property.getItem());
                                projectName = project.getTechnicalLabel();
                            }
                        }
                    }
                    if (projectName == null) {// use current one
                        projectName = ProjectManager.getInstance().getCurrentProject().getTechnicalLabel();
                    }
                    jobClassPackageFolder = JavaResourcesHelper.getJobClassPackageFolder(projectName, child.getJobName(),
                            child.getJobVersion());
                }
                // children poms
                childrenPomsIncludes.add(PomUtil.getPomFileName(child.getJobName(), child.getJobVersion()));

                if (!child.isTestContainer()) { // for test, it have add the in assembly, so no need.
                    // conext resources
                    childrenFolderResourcesIncludes.add(jobClassPackageFolder + "/**"); // add all context
                }

            }
        }
        /*
         * FIXME, if change the profiles setting for directory, must need change this parts.
         */
        if (!clonedChildrenJobInfors.isEmpty()) {
            // poms
            addAssemblyFileSets(fileSetsElem, "${poms.dir}", "${talend.job.name}", false, childrenPomsIncludes, null, null, null,
                    null, false, "add children pom files.");

            if (!childrenFolderResourcesIncludes.isEmpty()) { // only for standard job, not for test.
                // src
                addAssemblyFileSets(fileSetsElem, "${sourcecodes.dir}", "${talend.job.name}/src/main/java/", false,
                        childrenFolderResourcesIncludes, null, null, null, null, false, "add children src resources files.");

                // contexts
                addAssemblyFileSets(fileSetsElem, "${resources.dir}", "${talend.job.name}/src/main/resources/", false,
                        childrenFolderResourcesIncludes, null, null, null, null, false,
                        "add children context files to resources.");
                addAssemblyFileSets(fileSetsElem, "${contexts.running.dir}", "${talend.job.name}", false,
                        childrenFolderResourcesIncludes, null, null, null, null, false, "add children context files for running.");
            }

            PomUtil.saveAssemblyFile(monitor, assemblyFile, document);

            // clean for children poms
            cleanChildrenPomSettings(monitor, childrenPomsIncludes);
        }

    }

    protected void cleanChildrenPomSettings(IProgressMonitor monitor, List<String> childrenPomsIncludes) throws Exception {
        for (String childPomFile : childrenPomsIncludes) {
            IFile childPom = assemblyFile.getProject().getFile(childPomFile);
            if (childPom.exists()) {
                Model childModel = MODEL_MANAGER.readMavenModel(childPom);
                List<Plugin> childPlugins = new ArrayList<Plugin>(childModel.getBuild().getPlugins());
                Iterator<Plugin> childIterator = childPlugins.iterator();
                while (childIterator.hasNext()) {
                    Plugin p = childIterator.next();
                    if (p.getArtifactId().equals("maven-assembly-plugin")) { //$NON-NLS-1$
                        // must remove the assembly plugins for children poms, else will be some errors.
                        childIterator.remove();
                    } else if (p.getArtifactId().equals("maven-antrun-plugin")) { //$NON-NLS-1$
                        // because no assembly, so no need copy the scripts and rename it.
                        childIterator.remove();
                    }

                }

                childModel.getBuild().setPlugins(childPlugins);

                /*
                 * FIXME, Won't have assembly, maybe also move the profiles, because so far, the profiles are useful for
                 * assembly only. If later, the profiles will use by other tasks, should remove this codes.
                 */
                childModel.getProfiles().clear();

                PomUtil.savePom(monitor, childModel, childPom);
            }
        }
    }

    private Node getElement(Node parent, String elemName, int level) {
        NodeList childrenNodeList = parent.getChildNodes();
        for (int i = 0; i < childrenNodeList.getLength(); i++) {
            Node child = childrenNodeList.item(i);
            if (child != null && child.getNodeType() == Node.ELEMENT_NODE) {
                if (child.getNodeName().equals(elemName)) {
                    return child;
                }
            }
            if (level > 1) {
                Node element = getElement(child, elemName, --level);
                if (element != null) {
                    return element;
                }
            }
        }
        return null;
    }

    @SuppressWarnings("nls")
    private void addAssemblyFiles(Node filesElem, String source, String outputDirectory, String destName, String fileMode,
            String lineEnding, boolean filtered, String comment) {
        Assert.isNotNull(filesElem);
        Assert.isNotNull(source);
        Assert.isNotNull(outputDirectory);

        Document doc = filesElem.getOwnerDocument();

        Element fileEle = doc.createElement("file");
        filesElem.appendChild(fileEle);

        if (comment != null) {
            fileEle.appendChild(doc.createComment(comment));
        }

        Element sourceElement = doc.createElement("source");
        sourceElement.setTextContent(source);
        fileEle.appendChild(sourceElement);

        Element outputDirectoryElement = doc.createElement("outputDirectory");
        outputDirectoryElement.setTextContent(outputDirectory);
        fileEle.appendChild(outputDirectoryElement);

        if (destName != null) { // if not set, will be same as source
            Element destNameElement = doc.createElement("destName");
            destNameElement.setTextContent(destName);
            fileEle.appendChild(destNameElement);
        }
        if (fileMode != null) {
            Element fileModeElement = doc.createElement("fileMode");
            fileModeElement.setTextContent(fileMode);
            fileEle.appendChild(fileModeElement);
        }
        if (lineEnding != null) {
            Element lineEndingElement = doc.createElement("lineEnding");
            lineEndingElement.setTextContent(lineEnding);
            fileEle.appendChild(lineEndingElement);
        }
        if (filtered) { // by default is false
            Element filteredElement = doc.createElement("filtered");
            filteredElement.setTextContent(Boolean.TRUE.toString());
            fileEle.appendChild(filteredElement);
        }
    }

    @SuppressWarnings("nls")
    private void addAssemblyFileSets(Node fileSetsNode, String directory, String outputDirectory, boolean useDefaultExcludes,
            List<String> includes, List<String> excludes, String fileMode, String directoryMode, String lineEnding,
            boolean filtered, String comment) {
        Assert.isNotNull(fileSetsNode);
        Assert.isNotNull(outputDirectory);
        Assert.isNotNull(directory);

        Document doc = fileSetsNode.getOwnerDocument();

        Element fileSetEle = doc.createElement("fileSet");
        fileSetsNode.appendChild(fileSetEle);

        if (comment != null) {
            fileSetEle.appendChild(doc.createComment(comment));
        }

        Element outputDirectoryElement = doc.createElement("outputDirectory");
        outputDirectoryElement.setTextContent(outputDirectory);
        fileSetEle.appendChild(outputDirectoryElement);

        Element directoryElement = doc.createElement("directory");
        directoryElement.setTextContent(directory);
        fileSetEle.appendChild(directoryElement);

        if (useDefaultExcludes) { // false by default
            Element useDefaultExcludesElement = doc.createElement("useDefaultExcludes");
            useDefaultExcludesElement.setTextContent(Boolean.TRUE.toString());
            fileSetEle.appendChild(useDefaultExcludesElement);
        }

        if (includes != null && !includes.isEmpty()) {

            Element includesEle = doc.createElement("includes");
            fileSetEle.appendChild(includesEle);
            for (String in : includes) {
                Element includeElement = doc.createElement("include");
                includeElement.setTextContent(in);
                includesEle.appendChild(includeElement);
            }
        }

        if (excludes != null && !excludes.isEmpty()) {
            Element excludesEle = doc.createElement("excludes");
            fileSetEle.appendChild(excludesEle);
            for (String ex : excludes) {
                Element excludeElement = doc.createElement("exclude");
                excludeElement.setTextContent(ex);
                excludesEle.appendChild(excludeElement);

            }
        }
        if (fileMode != null) {
            Element fileModeElement = doc.createElement("fileMode");
            fileModeElement.setTextContent(fileMode);
            fileSetEle.appendChild(fileModeElement);
        }
        if (directoryMode != null) {
            Element directoryModeElement = doc.createElement("directoryMode");
            directoryModeElement.setTextContent(directoryMode);
            fileSetEle.appendChild(directoryModeElement);
        }

        if (lineEnding != null) {
            Element lineEndingElement = doc.createElement("lineEnding");
            lineEndingElement.setTextContent(lineEnding);
            fileSetEle.appendChild(lineEndingElement);
        }
        if (filtered) { // by default is false
            Element filteredElement = doc.createElement("filtered");
            filteredElement.setTextContent(Boolean.TRUE.toString());
            fileSetEle.appendChild(filteredElement);
        }
    }

}
