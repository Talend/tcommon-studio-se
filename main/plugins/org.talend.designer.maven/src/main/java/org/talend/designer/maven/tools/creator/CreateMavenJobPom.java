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
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Activation;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.Profile;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.process.JobInfo;
import org.talend.core.model.process.ProcessUtils;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.Property;
import org.talend.core.model.utils.JavaResourcesHelper;
import org.talend.core.repository.utils.ItemResourceUtil;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.core.runtime.process.JobInfoProperties;
import org.talend.core.runtime.process.LastGenerationInfo;
import org.talend.core.runtime.process.TalendProcessArgumentConstant;
import org.talend.core.runtime.process.TalendProcessOptionConstants;
import org.talend.core.runtime.projectsetting.IProjectSettingPreferenceConstants;
import org.talend.core.runtime.projectsetting.IProjectSettingTemplateConstants;
import org.talend.core.runtime.projectsetting.ProjectPreferenceManager;
import org.talend.core.ui.ITestContainerProviderService;
import org.talend.core.utils.TemplateFileUtils;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.template.ETalendMavenVariables;
import org.talend.designer.maven.template.MavenTemplateManager;
import org.talend.designer.maven.utils.PomIdsHelper;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.designer.maven.utils.SortableDependency;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.RepositoryConstants;
import org.talend.utils.io.FilesUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

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

    public String getWindowsClasspathForPs1() {
        return "\'" + getWindowsClasspath() + "\'";
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
        File templateFile = PomUtil
                .getTemplateFile(getObjectTypeFolder(), getItemRelativePath(), TalendMavenConstants.POM_FILE_NAME);
        if (!FilesUtils.allInSameFolder(templateFile, TalendMavenConstants.ASSEMBLY_FILE_NAME)) {
            templateFile = null; // force to set null, in order to use the template from other places.
        }
        try {
            final Map<String, Object> templateParameters = PomUtil.getTemplateParameters(getJobProcessor());
            return MavenTemplateManager
                    .getTemplateStream(templateFile, IProjectSettingPreferenceConstants.TEMPLATE_STANDALONE_JOB_POM,
                            JOB_TEMPLATE_BUNDLE, getBundleTemplatePath(), templateParameters);
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
                ITestContainerProviderService testService = (ITestContainerProviderService) GlobalServiceRegister
                        .getDefault()
                        .getService(ITestContainerProviderService.class);
                try {
                    property = testService.getParentJobItem(property.getItem()).getProperty();
                    process = testService.getParentJobProcess(process);
                } catch (PersistenceException e) {
                    ExceptionHandler.process(e);
                }
            }
        }

        Project project = ProjectManager.getInstance().getProject(property);
        if (project == null) { // current project
            project = ProjectManager.getInstance().getCurrentProject().getEmfProject();
        }
        String talendJobPath = project.getTechnicalLabel().toLowerCase();
        checkPomProperty(properties, "talend.job.path", ETalendMavenVariables.JobPath, talendJobPath);
        IPath jobFolderPath = ItemResourceUtil.getItemRelativePath(property);
        String jobFolder = "";
        if (jobFolderPath != null && !StringUtils.isEmpty(jobFolderPath.toPortableString())) {
            jobFolder = jobFolderPath.toPortableString();
            // like f1/f2/f3/
            jobFolder = StringUtils.strip(jobFolder, "/") + "/";
            jobFolder = jobFolder.toLowerCase();
        }
        checkPomProperty(properties, "talend.job.folder", ETalendMavenVariables.JobFolder, jobFolder);

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
                project.getTechnicalLabel());
        checkPomProperty(properties, "talend.project.name.lowercase", ETalendMavenVariables.ProjectName,
                project.getTechnicalLabel().toLowerCase());
        checkPomProperty(properties, "talend.routine.groupid", ETalendMavenVariables.RoutineGroupId,
                PomIdsHelper.getCodesGroupId(TalendMavenConstants.DEFAULT_CODE));
        checkPomProperty(properties, "talend.pigudf.groupid", ETalendMavenVariables.PigudfGroupId,
                PomIdsHelper.getCodesGroupId(TalendMavenConstants.DEFAULT_PIGUDF));
        checkPomProperty(properties, "talend.project.id", ETalendMavenVariables.ProjectId,
                jobInfoProp.getProperty(JobInfoProperties.PROJECT_ID, String.valueOf(project.getId())));

        checkPomProperty(properties, "talend.job.name", ETalendMavenVariables.JobName,
                jobInfoProp.getProperty(JobInfoProperties.JOB_NAME, property.getLabel()));

        checkPomProperty(properties, "talend.job.version", ETalendMavenVariables.TalendJobVersion,
                property.getVersion());

        checkPomProperty(properties, "maven.build.timestamp.format", ETalendMavenVariables.JobDateFormat,
                JobInfoProperties.JOB_DATE_FORMAT);

        checkPomProperty(properties, "talend.job.context", ETalendMavenVariables.JobContext,
                jobInfoProp.getProperty(JobInfoProperties.CONTEXT_NAME, context.getName()));
        checkPomProperty(properties, "talend.job.id", ETalendMavenVariables.JobId,
                jobInfoProp.getProperty(JobInfoProperties.JOB_ID, process.getId()));
        checkPomProperty(properties, "talend.job.type", ETalendMavenVariables.JobType,
                jobInfoProp.getProperty(JobInfoProperties.JOB_TYPE));

        boolean publishAsSnapshot = BooleanUtils
                .toBoolean((String) property.getAdditionalProperties().get(MavenConstants.NAME_PUBLISH_AS_SNAPSHOT));

        checkPomProperty(properties, "project.distribution-management.repository.id",
                ETalendMavenVariables.ProjectDistributionManagementRepositoryId,
                publishAsSnapshot ? "${project.distributionManagement.snapshotRepository.id}"
                        : "${project.distributionManagement.repository.id}");
        checkPomProperty(properties, "project.distribution-management.repository.url",
                ETalendMavenVariables.ProjectDistributionManagementRepositoryUrl,
                publishAsSnapshot ? "${project.distributionManagement.snapshotRepository.url}"
                        : "${project.distributionManagement.repository.url}");

        if (process instanceof IProcess2) {
            String framework = (String) ((IProcess2) process).getAdditionalProperties().get("FRAMEWORK"); //$NON-NLS-1$
            if (framework == null) {
                framework = ""; //$NON-NLS-1$
            }
            checkPomProperty(properties, "talend.job.framework", ETalendMavenVariables.Framework, framework); //$NON-NLS-1$
        }
        checkPomProperty(properties, "talend.job.stat", ETalendMavenVariables.JobStat,
                jobInfoProp.getProperty(JobInfoProperties.ADD_STATIC_CODE, Boolean.FALSE.toString()));
        checkPomProperty(properties, "talend.job.applyContextToChildren",
                ETalendMavenVariables.JobApplyContextToChildren,
                jobInfoProp.getProperty(JobInfoProperties.APPLY_CONTEXY_CHILDREN, Boolean.FALSE.toString()));
        checkPomProperty(properties, "talend.product.version", ETalendMavenVariables.ProductVersion,
                jobInfoProp.getProperty(JobInfoProperties.COMMANDLINE_VERSION, VersionUtils.getVersion()));
        String finalNameStr = JavaResourcesHelper.getJobJarName(property.getLabel(), property.getVersion());
        checkPomProperty(properties, "talend.job.finalName", ETalendMavenVariables.JobFinalName, finalNameStr);

        if (getJobProcessor() != null) {
            String[] jvmArgs = getJobProcessor().getJVMArgs();
            if (jvmArgs != null && jvmArgs.length > 0) {
                StringBuilder jvmArgsStr = new StringBuilder();
                for (String arg : jvmArgs) {
                    jvmArgsStr.append(arg + " ");
                }
                checkPomProperty(properties, "talend.job.jvmargs", null, jvmArgsStr.toString());
            }
        }
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
        generateAssemblyFile(monitor, null);

        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            IRunProcessService service =
                    (IRunProcessService) GlobalServiceRegister.getDefault().getService(IRunProcessService.class);
            if (!service.isGeneratePomOnly()) {
                generateTemplates(true);
            }
        }

    }

    protected void generateAssemblyFile(IProgressMonitor monitor, final Set<JobInfo> clonedChildrenJobInfors)
            throws Exception {
        IFile assemblyFile = this.getAssemblyFile();
        if (assemblyFile != null) {
            boolean set = false;
            // read template from project setting
            try {
                File templateFile = PomUtil
                        .getTemplateFile(getObjectTypeFolder(), getItemRelativePath(),
                                TalendMavenConstants.ASSEMBLY_FILE_NAME);
                if (!FilesUtils.allInSameFolder(templateFile, TalendMavenConstants.POM_FILE_NAME)) {
                    templateFile = null; // force to set null, in order to use the template from other places.
                }

                IProcessor processor = getJobProcessor();
                final Map<String, Object> templateParameters = PomUtil.getTemplateParameters(processor);
                String launcherName = null;
                if (processor.getArguments() != null) {
                    Object needLauncher = processor.getArguments().get(TalendProcessArgumentConstant.ARG_NEED_LAUNCHER);
                    if (needLauncher != null) {
                        if ((Boolean) needLauncher) {
                            Object launcherObj =
                                    processor.getArguments().get(TalendProcessArgumentConstant.ARG_LAUNCHER_NAME);
                            if (launcherObj != null) {
                                launcherName = (String) launcherObj;
                            }
                        }
                    }
                }
                String content = MavenTemplateManager
                        .getTemplateContent(templateFile,
                                IProjectSettingPreferenceConstants.TEMPLATE_STANDALONE_JOB_ASSEMBLY,
                                JOB_TEMPLATE_BUNDLE,
                                IProjectSettingTemplateConstants.PATH_STANDALONE + '/'
                                        + IProjectSettingTemplateConstants.ASSEMBLY_JOB_TEMPLATE_FILE_NAME,
                                templateParameters);
                if (content != null) {
                    content = TemplateFileUtils.handleAssemblyJobTemplate(content, launcherName);
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

    public void generateTemplates(boolean overwrite) throws Exception {
        IProcessor processor = getJobProcessor();
        if (processor == null) {
            return;
        }
        ITalendProcessJavaProject codeProject = getJobProcessor().getTalendJavaProject();
        if (codeProject == null) {
            return;
        }
        Property property = codeProject.getPropery() != null ? codeProject.getPropery() : processor.getProperty();
        if (property == null) {
            return;
        }
        String contextName = getOptionString(TalendProcessArgumentConstant.ARG_CONTEXT_NAME);
        if (contextName == null) {
            contextName = processor.getContext().getName();
        }
        String jobClass = JavaResourcesHelper.getJobClassPackageName(property.getItem()) + "." + property.getLabel();

        StringBuffer windowsScriptAdditionValue = new StringBuffer(50);
        StringBuffer unixScriptAdditionValue = new StringBuffer(50);
        if (StringUtils.isNotEmpty(this.getWindowsScriptAddition())) {
            windowsScriptAdditionValue.append(this.getWindowsScriptAddition());
        }
        if (StringUtils.isNotEmpty(this.getUnixScriptAddition())) {
            unixScriptAdditionValue.append(this.getUnixScriptAddition());
        }

        // context
        if (isOptionChecked(TalendProcessArgumentConstant.ARG_NEED_CONTEXT)) {
            final String contextPart = TalendProcessArgumentConstant.CMD_ARG_CONTEXT_NAME + contextName;
            addScriptAddition(windowsScriptAdditionValue, contextPart);
            addScriptAddition(unixScriptAdditionValue, contextPart);
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
        String[] jvmArgs = processor.getJVMArgs();
        StringBuilder jvmArgsStr = new StringBuilder();
        StringBuilder jvmArgsStrPs1 = new StringBuilder();
        if (jvmArgs != null && jvmArgs.length > 0) {
            for (String arg : jvmArgs) {
                jvmArgsStr.append(arg + " "); //$NON-NLS-1$
                jvmArgsStrPs1.append("\'" + arg + "\' "); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        final Map<String, Object> templateParameters = PomUtil.getTemplateParameters(property);
        String batContent = MavenTemplateManager.getProjectSettingValue(IProjectSettingPreferenceConstants.TEMPLATE_BAT,
                templateParameters);
        batContent = StringUtils
                .replaceEach(batContent,
                        new String[] { "${talend.job.jvmargs}", "${talend.job.bat.classpath}", "${talend.job.class}",
                                "${talend.job.bat.addition}" },
                        new String[] { jvmArgsStr.toString().trim(), getWindowsClasspath(), jobClass,
                                windowsScriptAdditionValue.toString() });

        String shContent = MavenTemplateManager.getProjectSettingValue(IProjectSettingPreferenceConstants.TEMPLATE_SH,
                templateParameters);
        shContent = StringUtils
                .replaceEach(shContent,
                        new String[] { "${talend.job.jvmargs}", "${talend.job.sh.classpath}", "${talend.job.class}",
                                "${talend.job.sh.addition}" },
                        new String[] { jvmArgsStr.toString().trim(), getUnixClasspath(), jobClass,
                                unixScriptAdditionValue.toString() });

        String psContent = MavenTemplateManager.getProjectSettingValue(IProjectSettingPreferenceConstants.TEMPLATE_PS,
                templateParameters);
        psContent = StringUtils
                .replaceEach(psContent,
                        new String[] { "${talend.job.jvmargs.ps1}", "${talend.job.ps1.classpath}",
                                "${talend.job.class}", "${talend.job.bat.addition}" },
                        new String[] { jvmArgsStrPs1.toString().trim(), getWindowsClasspathForPs1(), jobClass,
                                windowsScriptAdditionValue.toString() });

        String jobInfoContent = MavenTemplateManager
                .getProjectSettingValue(IProjectSettingPreferenceConstants.TEMPLATE_JOB_INFO, templateParameters);
        String projectTechName = ProjectManager.getInstance().getProject(property).getTechnicalLabel();
        org.talend.core.model.general.Project project =
                ProjectManager.getInstance().getProjectFromProjectTechLabel(projectTechName);
        if (project == null) {
            project = ProjectManager.getInstance().getCurrentProject();
        }
        String mainProjectBranch = ProjectManager.getInstance().getMainProjectBranch(project);
        if (mainProjectBranch == null) {
            ProjectPreferenceManager preferenceManager =
                    new ProjectPreferenceManager(project, "org.talend.repository", false);
            mainProjectBranch = preferenceManager.getValue(RepositoryConstants.PROJECT_BRANCH_ID);
            if (mainProjectBranch == null) {
                mainProjectBranch = "";
            }
        }

        if (!isOptionChecked(TalendProcessArgumentConstant.ARG_AVOID_BRANCH_NAME)) {
            jobInfoContent = StringUtils.replace(jobInfoContent, "${talend.project.branch}", mainProjectBranch);
        }

        IFolder templateFolder = codeProject.getTemplatesFolder();
        IFile shFile = templateFolder.getFile(IProjectSettingTemplateConstants.JOB_RUN_SH_TEMPLATE_FILE_NAME);
        IFile batFile = templateFolder.getFile(IProjectSettingTemplateConstants.JOB_RUN_BAT_TEMPLATE_FILE_NAME);
        IFile psFile = templateFolder.getFile(IProjectSettingTemplateConstants.JOB_RUN_PS_TEMPLATE_FILE_NAME);
        IFile infoFile = templateFolder.getFile(IProjectSettingTemplateConstants.JOB_INFO_TEMPLATE_FILE_NAME);

        MavenTemplateManager.saveContent(batFile, batContent, overwrite);
        MavenTemplateManager.saveContent(shFile, shContent, overwrite);
        MavenTemplateManager.saveContent(psFile, psContent, overwrite);
        MavenTemplateManager.saveContent(infoFile, jobInfoContent, overwrite);
    }

    protected ITalendProcessJavaProject getTalendJobJavaProject(JobInfo jobInfo) {
        IProcessor processor = jobInfo.getProcessor();
        if (processor == null) {
            processor = getJobProcessor();
        }
        ITalendProcessJavaProject talendProcessJavaProject = processor.getTalendJavaProject();

        if (talendProcessJavaProject == null) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
                IRunProcessService service =
                        (IRunProcessService) GlobalServiceRegister.getDefault().getService(IRunProcessService.class);
                if (processor.getProperty() == null) {
                    processor = jobInfo.getReloadedProcessor();
                }
                talendProcessJavaProject = service.getTalendJobJavaProject(processor.getProperty());

            }
        }
        return talendProcessJavaProject;
    }

    protected ITalendProcessJavaProject getTalendJobJavaProject(IProcessor processor) {
        ITalendProcessJavaProject talendProcessJavaProject = processor.getTalendJavaProject();

        if (talendProcessJavaProject == null) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
                IRunProcessService service =
                        (IRunProcessService) GlobalServiceRegister.getDefault().getService(IRunProcessService.class);
                talendProcessJavaProject = service.getTalendJobJavaProject(processor.getProperty());

            }
        }
        return talendProcessJavaProject;
    }

    protected void updateDependencySet(IFile assemblyFile) {
        Map<String, Dependency> jobCoordinateMap = new HashMap<String, Dependency>();
        Set<JobInfo> childrenJobInfo = new HashSet<>();
        if (!hasLoopDependency()) {
            childrenJobInfo = getJobProcessor().getBuildChildrenJobs();
        }

        // add children jobs
        for (JobInfo jobInfo : childrenJobInfo) {
            Property property = jobInfo.getProcessItem().getProperty();
            String coordinate =
                    getCoordinate(PomIdsHelper.getJobGroupId(property), PomIdsHelper.getJobArtifactId(jobInfo),
                            MavenConstants.PACKAGING_JAR, PomIdsHelper.getJobVersion(property));
            Dependency dependency = getDependencyObject(PomIdsHelper.getJobGroupId(property), PomIdsHelper.getJobArtifactId(jobInfo), PomIdsHelper.getJobVersion(property),
                            MavenConstants.PACKAGING_JAR, null);
            jobCoordinateMap.put(coordinate, dependency);
        }

        // add parent job
        Property parentProperty = this.getJobProcessor().getProperty();
        String parentCoordinate =
                getCoordinate(PomIdsHelper.getJobGroupId(parentProperty), PomIdsHelper.getJobArtifactId(parentProperty),
                        MavenConstants.PACKAGING_JAR, PomIdsHelper.getJobVersion(parentProperty));
        Dependency parentDependency = getDependencyObject(PomIdsHelper.getJobGroupId(parentProperty), PomIdsHelper.getJobArtifactId(parentProperty), PomIdsHelper.getJobVersion(parentProperty),
                        MavenConstants.PACKAGING_JAR, null);
        jobCoordinateMap.put(parentCoordinate, parentDependency);

        // add talend libraries and codes
        Map<String, Dependency> talendLibCoordinateMap = new HashMap<String, Dependency>();
        String projectTechName = ProjectManager.getInstance().getProject(parentProperty).getTechnicalLabel();
        String projectGroupId = PomIdsHelper.getProjectGroupId(projectTechName);

        // codes
        List<Dependency> dependencies = new ArrayList<>();
        addCodesDependencies(dependencies);
        for (Dependency dependency : dependencies) {
            talendLibCoordinateMap.put(getCoordinate(dependency), dependency);
        }

        // libraries
        dependencies.clear();
        Set<ModuleNeeded> modules = getJobProcessor()
                .getNeededModules(TalendProcessOptionConstants.MODULES_WITH_JOBLET
                        | TalendProcessOptionConstants.MODULES_EXCLUDE_SHADED);
        for (ModuleNeeded module : modules) {
            String mavenUri = module.getMavenUri();
            Dependency dependency = PomUtil.createModuleDependency(mavenUri);
            dependencies.add(dependency);
        }
        for (Dependency dependency : dependencies) {
            if (MavenConstants.PACKAGING_POM.equals(dependency.getType())) {
                continue;
            }
            String dependencyGroupId = dependency.getGroupId();
            String coordinate = getCoordinate(dependency);
            if (!jobCoordinateMap.containsKey(coordinate)) {
                if (MavenConstants.DEFAULT_LIB_GROUP_ID.equals(dependencyGroupId)) {
                    talendLibCoordinateMap.put(coordinate, dependency);
                }
            }
        }

        // add 3rd party libraries
        Map<String, Dependency> _3rdDepLibMap = new HashMap<String, Dependency>();
        Map<String, Set<Dependency>> duplicateLibs = new HashMap<>();
        for (Dependency dependency : dependencies) {
            if (MavenConstants.PACKAGING_POM.equals(dependency.getType())) {
                continue;
            }
            String coordinate = getCoordinate(dependency);
            if (!jobCoordinateMap.containsKey(coordinate) && !talendLibCoordinateMap.containsKey(coordinate)) {
                _3rdDepLibMap.put(coordinate, dependency);
                addToDuplicateLibs(duplicateLibs, dependency);
            }
        }

        // add missing modules from the job generation of children
        Set<ModuleNeeded> fullModulesList = new HashSet<>();
        for (JobInfo jobInfo : childrenJobInfo) {
            fullModulesList
                    .addAll(LastGenerationInfo
                            .getInstance()
                            .getModulesNeededWithSubjobPerJob(jobInfo.getJobId(), jobInfo.getJobVersion()));
        }
        for (ModuleNeeded moduleNeeded : fullModulesList) {
            if (moduleNeeded.isExcluded()) {
                continue;
            }
            MavenArtifact artifact = MavenUrlHelper.parseMvnUrl(moduleNeeded.getMavenUri());
            String coordinate = getCoordinate(artifact.getGroupId(), artifact.getArtifactId(), artifact.getType(),
                    artifact.getVersion());
            if (!jobCoordinateMap.containsKey(coordinate) && !talendLibCoordinateMap.containsKey(coordinate)
                    && !_3rdDepLibMap.containsKey(coordinate)) {
                Dependency dependencyObject = getDependencyObject(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(), artifact.getType(), artifact.getClassifier());
                if (MavenConstants.DEFAULT_LIB_GROUP_ID.equals(artifact.getGroupId())
                        || artifact.getGroupId().startsWith(projectGroupId)) {
                    talendLibCoordinateMap.put(coordinate, dependencyObject);
                } else {
                    _3rdDepLibMap.put(coordinate, dependencyObject);
                    Dependency dependency = PomUtil
                            .createDependency(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(),
                                    artifact.getType(), artifact.getClassifier());
                    addToDuplicateLibs(duplicateLibs, dependency);
                }
            }
        }

        Iterator<String> iterator = duplicateLibs.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Set<Dependency> dupDependencies = duplicateLibs.get(key);
            if (dupDependencies.size() < 2) {
                // remove non-duplicated dependencies
                iterator.remove();
            } else {
                // remove duplicated dependencies from 3rd lib list
                for (Dependency dependency : dupDependencies) {
                    _3rdDepLibMap.remove(getCoordinate(dependency));
                }
            }
        }

        try {
            Document document = PomUtil.loadAssemblyFile(null, assemblyFile);
            // add talend libs & codes
            setupDependencySetNode(document, talendLibCoordinateMap, "lib", "${artifact.artifactId}.${artifact.extension}",
                    false, false);
            // add 3rd party libs <dependencySet>
            setupDependencySetNode(document, _3rdDepLibMap, "lib", null, false, false);
            // add jobs
            setupDependencySetNode(document, jobCoordinateMap, "${talend.job.name}",
                    "${artifact.build.finalName}.${artifact.extension}", true, false);
            // add duplicate dependencies if exists
            setupFileNode(document, duplicateLibs);

            PomUtil.saveAssemblyFile(assemblyFile, document);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    private String getCoordinate(Dependency dependency) {
        return getCoordinate(dependency.getGroupId(), dependency.getArtifactId(), dependency.getType(),
                dependency.getVersion());
    }

    protected String getCoordinate(String groupId, String artifactId, String type, String version) {
        String separator = ":"; //$NON-NLS-1$
        String coordinate = groupId + separator;
        coordinate += artifactId + separator;
        if (type != null) {
            coordinate += type;
        }

        if (version != null) {
            coordinate += separator + version;
        }

        return coordinate;
    }

    protected String getAssemblyCoordinate(Dependency dependency) {
        String separator = ":"; //$NON-NLS-1$
        String coordinate = dependency.getGroupId() + separator;
        coordinate += dependency.getArtifactId() + separator;
        if (dependency.getType() != null) {
            coordinate += dependency.getType();
        }
        if (dependency.getClassifier() != null) {
            coordinate += separator + "*";
        }
        if (dependency.getVersion() != null) {
            coordinate += separator + dependency.getVersion();
        }

        return coordinate;
    }

    protected Dependency getDependencyObject(String groupId, String artifactId, String version, String type, String classifier) {
        Dependency object = new SortableDependency();
        object.setGroupId(groupId);
        object.setArtifactId(artifactId);
        object.setVersion(version);
        object.setType(type);
        object.setClassifier(classifier);

        return object;
    }

    private void addToDuplicateLibs(Map<String, Set<Dependency>> map, Dependency dependency) {
        String coordinate =
                getCoordinate(dependency.getGroupId(), dependency.getArtifactId(), dependency.getType(), null);
        if (!map.containsKey(coordinate)) {
            Set<Dependency> set = new HashSet<>();
            map.put(coordinate, set);
        }
        map.get(coordinate).add(dependency);
    }

    protected void setupDependencySetNode(Document document, Map<String, Dependency> libIncludes, String outputDir,
            String fileNameMapping, boolean useProjectArtifact, boolean unpack) {
        if (libIncludes.isEmpty()) {
            return;
        }
        Node dependencySetsNode = document.getElementsByTagName("dependencySets").item(0);
        if (dependencySetsNode == null) {
            return;
        }
        Node dependencySetNode = document.createElement("dependencySet");
        dependencySetsNode.appendChild(dependencySetNode);

        if (StringUtils.isNotBlank(outputDir)) {
            Node outputDirNode = document.createElement("outputDirectory");
            outputDirNode.setTextContent(outputDir);
            dependencySetNode.appendChild(outputDirNode);
        }

        Node includesNode = document.createElement("includes");
        dependencySetNode.appendChild(includesNode);

        for (Dependency dependency : libIncludes.values()) {
            Node includeNode = document.createElement("include");
            includeNode.setTextContent(getAssemblyCoordinate(dependency));
            includesNode.appendChild(includeNode);
        }

        if (StringUtils.isNotBlank(fileNameMapping)) {
            Node fileNameMappingNode = document.createElement("outputFileNameMapping");
            fileNameMappingNode.setTextContent(fileNameMapping);
            dependencySetNode.appendChild(fileNameMappingNode);
        }

        Node useProjectArtifactNode = document.createElement("useProjectArtifact");
        useProjectArtifactNode.setTextContent(Boolean.toString(useProjectArtifact));
        dependencySetNode.appendChild(useProjectArtifactNode);

        if (unpack) {
            Node unpackNode = document.createElement("unpack");
            unpackNode.setTextContent(Boolean.TRUE.toString());
            dependencySetNode.appendChild(unpackNode);
        }

    }

    private void setupFileNode(Document document, Map<String, Set<Dependency>> duplicateDependencies) {
        if (duplicateDependencies.isEmpty()) {
            return;
        }
        try {
            IMaven maven = MavenPlugin.getMaven();
            ArtifactRepository repository = maven.getLocalRepository();
            Node filesNode = document.getElementsByTagName("files").item(0);
            for (Entry<String, Set<Dependency>> entry : duplicateDependencies.entrySet()) {
                Set<Dependency> dependencies = entry.getValue();
                for (Dependency dependency : dependencies) {
                    String sourceLocation = maven
                            .getArtifactPath(repository, dependency.getGroupId(), dependency.getArtifactId(),
                                    dependency.getVersion(), dependency.getType(), dependency.getClassifier());
                    Path path = new File(repository.getBasedir()).toPath().resolve(sourceLocation);
                    sourceLocation = path.toString();
                    String destName = path.getFileName().toString();
                    Node fileNode = document.createElement("file");
                    filesNode.appendChild(fileNode);

                    Node sourcesNode = document.createElement("source");
                    sourcesNode.setTextContent(sourceLocation);
                    fileNode.appendChild(sourcesNode);

                    Node outputDirNode = document.createElement("outputDirectory");
                    outputDirNode.setTextContent("lib");
                    fileNode.appendChild(outputDirNode);

                    Node destNameNode = document.createElement("destName");
                    destNameNode.setTextContent(destName);
                    fileNode.appendChild(destNameNode);
                }
            }
        } catch (CoreException e) {
            ExceptionHandler.process(e);
        }
    }

    protected Plugin addSkipDockerMavenPlugin() {
        Plugin plugin = new Plugin();

        plugin.setGroupId("io.fabric8");
        plugin.setArtifactId("fabric8-maven-plugin");
        plugin.setVersion("4.0.0");

        Xpp3Dom skip = new Xpp3Dom("skip");
        // skip.setValue("${docker.skip}");
        skip.setValue("true");

        Xpp3Dom configuration = new Xpp3Dom("configuration");
        configuration.addChild(skip);

        List<PluginExecution> pluginExecutions = new ArrayList<PluginExecution>();
        PluginExecution pluginExecutionStart = new PluginExecution();
        pluginExecutionStart.setId("start");
        pluginExecutionStart.setPhase("none");
        pluginExecutionStart.setConfiguration(configuration);

        pluginExecutions.add(pluginExecutionStart);

        PluginExecution pluginExecutionPushImage = new PluginExecution();
        pluginExecutionPushImage.setId("push-image");
        pluginExecutionPushImage.setPhase("none");
        pluginExecutionPushImage.setConfiguration(configuration);

        pluginExecutions.add(pluginExecutionPushImage);

        plugin.setExecutions(pluginExecutions);
        plugin.setConfiguration(configuration);

        return plugin;

    }

}
