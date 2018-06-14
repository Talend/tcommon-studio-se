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
package org.talend.designer.runprocess;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.ui.IEditorPart;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.commons.utils.time.TimeMeasure;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ICoreService;
import org.talend.core.ITDQItemService;
import org.talend.core.PluginChecker;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.hadoop.HadoopConstants;
import org.talend.core.i18n.Messages;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.components.ComponentCategory;
import org.talend.core.model.components.EComponentType;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.process.JobInfo;
import org.talend.core.model.process.ProcessUtils;
import org.talend.core.model.process.ReplaceNodesInProcessProvider;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.relationship.Relation;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryManager;
import org.talend.core.model.repository.job.JobResource;
import org.talend.core.model.repository.job.JobResourceManager;
import org.talend.core.model.utils.JavaResourcesHelper;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.core.runtime.process.LastGenerationInfo;
import org.talend.core.runtime.process.TalendProcessArgumentConstant;
import org.talend.core.runtime.process.TalendProcessOptionConstants;
import org.talend.core.runtime.repository.build.BuildExportManager;
import org.talend.core.services.ISVNProviderService;
import org.talend.core.ui.IJobletProviderService;
import org.talend.core.ui.ITestContainerProviderService;
import org.talend.core.utils.BitwiseOptionUtils;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.repository.ProjectManager;
import org.talend.repository.documentation.ExportFileResource;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;
import org.talend.utils.io.FilesUtils;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 *
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 *
 */
public class ProcessorUtilities {

    private static Logger log = Logger.getLogger(ProcessorUtilities.class);

    public static final String PROP_MAPPINGS_URL = "talend.mappings.url"; //$NON-NLS-1$

    public static final int GENERATE_MAIN_ONLY = TalendProcessOptionConstants.GENERATE_MAIN_ONLY;

    public static final int GENERATE_WITH_FIRST_CHILD = TalendProcessOptionConstants.GENERATE_WITH_FIRST_CHILD;

    public static final int GENERATE_ALL_CHILDS = TalendProcessOptionConstants.GENERATE_ALL_CHILDS;

    public static final int GENERATE_TESTS = TalendProcessOptionConstants.GENERATE_TESTS;

    public static final int GENERATE_WITHOUT_COMPILING = TalendProcessOptionConstants.GENERATE_WITHOUT_COMPILING;

    private static String interpreter, codeLocation, libraryPath;

    private static boolean exportConfig = false;

    private static List<IEditorPart> openedEditors = new ArrayList<IEditorPart>();

    private static boolean codeModified;

    private static boolean needContextInCurrentGeneration = true;

    private static boolean exportAsOSGI = false;

    private static boolean exportJobAsMicroService = false;

    private static IDesignerCoreService designerCoreService = (IDesignerCoreService) GlobalServiceRegister.getDefault()
            .getService(IDesignerCoreService.class);

    private static Map<String, Integer> lastGeneratedWithStatsOrTrace = new HashMap<String, Integer>();

    private static Date exportTimeStamp;// time stamp create when exporting a job and reset when export ends.

    private static final int GENERATED_WITH_STATS = 1;

    private static final int GENERATED_WITH_TRACES = 2;

    private static final String COMMA = ";"; //$NON-NLS-1$

    private static final Set<ModuleNeeded> retrievedJarsForCurrentBuild = new HashSet<ModuleNeeded>();

    private static final Set<String> esbJobs = new HashSet<String>();

    public static void addOpenEditor(IEditorPart editor) {
        openedEditors.add(editor);
    }

    public static void editorClosed(IEditorPart editor) {
        openedEditors.remove(editor);
    }

    public static List<IEditorPart> getOpenedEditors() {
        return openedEditors;
    }

    // this character is used only when exporting a job in java, this will be
    // replaced by the correct separator
    // corresponding to the selected platform.
    public static final String TEMP_JAVA_CLASSPATH_SEPARATOR = "@"; //$NON-NLS-1$

    /**
     * Process need to be loaded to use this function.
     *
     * @param process
     * @param selectedContext
     * @return
     */
    public static IContext getContext(IProcess process, String selectedContext) {
        return process.getContextManager().getContext(selectedContext);
    }

    public static void setExportConfig(String exportInterpreter, String exportCodeLocation, String exportLibraryPath) {
        interpreter = exportInterpreter;
        codeLocation = exportCodeLocation;
        libraryPath = exportLibraryPath;
        exportConfig = true;
        exportTimeStamp = new Date();
    }

    public static void setExportConfig(String exportInterpreter, String exportCodeLocation, String exportLibraryPath,
            boolean export, Date timeStamp) {
        interpreter = exportInterpreter;
        codeLocation = exportCodeLocation;
        libraryPath = exportLibraryPath;
        exportConfig = export;
        exportTimeStamp = timeStamp;
    }

    public static void setExportConfig(String directory, boolean old) {
        String libPath = calculateLibraryPathFromDirectory(directory);
        String routinesJars = ""; //$NON-NLS-1$
        if (old) { // ../lib/systemRoutines.jar@../lib/userRoutines.jar@.
            // use character @ as temporary classpath separator, this one will be replaced during the export.
            routinesJars += libPath + JavaUtils.PATH_SEPARATOR + JavaUtils.SYSTEM_ROUTINE_JAR + TEMP_JAVA_CLASSPATH_SEPARATOR;
            routinesJars += libPath + JavaUtils.PATH_SEPARATOR + JavaUtils.USER_ROUTINE_JAR + TEMP_JAVA_CLASSPATH_SEPARATOR;
        } else { // ../lib/routines.jar@.
            routinesJars += libPath + JavaUtils.PATH_SEPARATOR + JavaUtils.ROUTINES_JAR + TEMP_JAVA_CLASSPATH_SEPARATOR;
        }
        routinesJars += '.';
        setExportConfig(JavaUtils.JAVA_APP_NAME, routinesJars, libPath);
    }

    private static String calculateLibraryPathFromDirectory(String directory) {
        int nb = directory.split(JavaUtils.PATH_SEPARATOR).length - 1;
        final String parentPath = "../";//$NON-NLS-1$
        String path = parentPath;
        for (int i = 0; i < nb; i++) {
            path = path.concat(parentPath);
        }
        return path + JavaUtils.JAVA_LIB_DIRECTORY;
    }

    public static Date getExportTimestamp() {
        return exportTimeStamp;
    }

    public static boolean isExportConfig() {
        return exportConfig;
    }

    public static void resetExportConfig() {
        interpreter = null;
        codeLocation = null;
        libraryPath = null;
        exportConfig = false;
        exportAsOSGI = false;
        exportTimeStamp = null;
    }

    public static String getInterpreter() {
        return interpreter;
    }

    public static String getLibraryPath() {
        return libraryPath;
    }

    public static String getCodeLocation() {
        return codeLocation;
    }

    private static boolean generatePomOnly = false;

    public static void setGeneratePomOnly(boolean generatePomOnly) {
        ProcessorUtilities.generatePomOnly = generatePomOnly;
    }

    public static boolean isGeneratePomOnly() {
        return generatePomOnly;
    }

    /**
     * Process need to be loaded first to use this function.
     *
     * @param process
     * @param context
     * @return
     */
    public static IProcessor getProcessor(IProcess process, Property property, IContext context) {
        IProcessor processor = getProcessor(process, property);
        processor.setContext(context);
        return processor;
    }

    /**
     * Process need to be loaded first to use this function.
     *
     * @param process
     * @param context
     * @return
     */
    public static IProcessor getProcessor(IProcess process, Property property) {
        Property curProperty = property;
        if (property == null && process instanceof IProcess2) {
            curProperty = ((IProcess2) process).getProperty();
        }

        IRunProcessService service = CorePlugin.getDefault().getRunProcessService();
        IProcessor processor = service.createCodeProcessor(process, curProperty,
                ((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY)).getProject()
                        .getLanguage(),
                true);
        return processor;
    }

    private static boolean isCodeGenerationNeeded(JobInfo jobInfo, boolean statistics, boolean trace) {
        // if we do any export, the code generation will always be needed.
        if (exportConfig || (!(jobInfo.getProcess() instanceof IProcess2))) {
            return true;
        }
        if (jobInfo.isForceRegenerate()) {
            return true;
        }

        if (ReplaceNodesInProcessProvider.isNeedForceRebuild((IProcess2) jobInfo.getProcess())) {
            return true;
        }

        // end
        IProcess attachedProcess = jobInfo.getProcess();
        if (attachedProcess != null && attachedProcess instanceof IProcess2) {
            IProcess2 process = (IProcess2) attachedProcess;
            if (process.isNeedRegenerateCode()) {
                return true;
            }

            Date modificationDate = process.getModificationDate();
            Date originalDate = designerCoreService.getLastGeneratedJobsDateMap().get(jobInfo.getJobId());
            if (originalDate == null || modificationDate == null || modificationDate.compareTo(originalDate) != 0) {
                if (jobInfo.getFatherJobInfo() != null) {
                    jobInfo.getFatherJobInfo().setForceRegenerate(true);
                }
                return true;
            }

            Integer previousInfos = lastGeneratedWithStatsOrTrace.get(jobInfo.getJobId());
            Integer infos = new Integer(0);
            infos += statistics ? GENERATED_WITH_STATS : 0;
            infos += trace ? GENERATED_WITH_TRACES : 0;
            if (previousInfos != infos) {
                if (jobInfo.getFatherJobInfo() != null) {
                    jobInfo.getFatherJobInfo().setForceRegenerate(true);
                }
                return true;
            }
        }
        JobResourceManager manager = JobResourceManager.getInstance();
        ECodeLanguage language = LanguageManager.getCurrentLanguage();
        JobResource jobR = manager.getJobResource(jobInfo);
        if (jobR == null) {
            return true;
        }
        List<IResource> rList = jobR.getResource();
        if (rList.size() == 0) {
            return true;
        }
        for (IResource resource : rList) {
            if (resource == null) {
                return true;
            }
            if (language == ECodeLanguage.JAVA) {
                if (resource.getType() == IResource.FOLDER) {
                    IFolder f = (IFolder) resource;
                    String jobName = jobInfo.getJobName() + ".java"; //$NON-NLS-1$
                    IFile codeFile = f.getFile(jobName);
                    if (!isFileExist(codeFile)) {
                        return true;
                    }
                }
            } else if (language == ECodeLanguage.PERL) {
                if (resource.getType() == IResource.FILE) {
                    IFile codeFile = (IFile) resource;
                    if (!isFileExist(codeFile)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isFileExist(IFile codeFile) {
        if (codeFile != null && codeFile.exists()) {
            try {
                InputStream io = codeFile.getContents(false);
                if (io.read() == -1) {
                    return false;
                }
                io.close();
            } catch (IOException e) {
                ExceptionHandler.process(e);
            } catch (CoreException e) {
                ExceptionHandler.process(e);
            }
            return true;
        }
        return false;
    }

    private static IProcessor generateCode(IProcessor processor2, JobInfo jobInfo, String selectedContextName, boolean statistics,
            boolean trace, boolean needContext, int option, IProgressMonitor progressMonitor) throws ProcessorException {
        needContextInCurrentGeneration = needContext;
        if (progressMonitor == null) {
            progressMonitor = new NullProgressMonitor();
        }
        if (progressMonitor.isCanceled()) {
            return null;
        }

        boolean isMainJob = false;
        List<IClasspathAdjuster> classPathAdjusters = ClasspathAdjusterProvider.getClasspathAdjuster();
        if (jobInfo.getFatherJobInfo() == null) {
            // In order to avoid eclipse to compile the code at each change in the workspace, we deactivate the
            // auto-build feature during the whole build time.
            // It will be reactivated at the end if the auto-build is activated in the workspace preferences.
            final IWorkspace workspace = ResourcesPlugin.getWorkspace();
            final IWorkspaceDescription wsDescription = workspace.getDescription();
            if (wsDescription.isAutoBuilding()) { // only do it when enabled
                try {
                    wsDescription.setAutoBuilding(false); // set to false always
                    workspace.setDescription(wsDescription);
                } catch (CoreException e) {
                    CommonExceptionHandler.warn(e.getMessage());
                }
            }

            isMainJob = true;
            codeModified = false;

            // this cache only keep the last main job's generation, so clear it since we regenerate a new job.
            LastGenerationInfo.getInstance().getLastGeneratedjobs().clear();
            LastGenerationInfo.getInstance().getHighPriorityModuleNeeded().clear();
            retrievedJarsForCurrentBuild.clear();

            // if it's the father, reset the processMap to ensure to have a good
            // code generation
            ItemCacheManager.clearCache();

            for (IClasspathAdjuster adjuster : classPathAdjusters) {
                adjuster.initialize();
            }
        }

        IProcess currentProcess = null;
        jobList.add(jobInfo);
        ProcessItem selectedProcessItem;

        selectedProcessItem = jobInfo.getProcessItem();
        String currentJobName = jobInfo.getJobName();
        if (selectedProcessItem == null && jobInfo.getJobVersion() == null) {
            // child job
            selectedProcessItem = ItemCacheManager.getProcessItem(jobInfo.getJobId());
        }

        if (jobInfo.getJobVersion() != null) {
            selectedProcessItem = ItemCacheManager.getProcessItem(jobInfo.getJobId(), jobInfo.getJobVersion());
        }

        if (selectedProcessItem == null && jobInfo.getProcess() == null) {
            return null;
        }
        if (selectedProcessItem != null) {
            currentJobName = selectedProcessItem.getProperty().getLabel();
        }
        progressMonitor
                .subTask(Messages.getString("ProcessorUtilities.loadingJob") + currentJobName == null ? "" : currentJobName); //$NON-NLS-1$

        if (jobInfo.getProcess() == null) {
            if (selectedProcessItem != null) {
                IDesignerCoreService service = CorePlugin.getDefault().getDesignerCoreService();
                currentProcess = service.getProcessFromProcessItem(selectedProcessItem);
                jobInfo.setProcess(currentProcess);
                if (currentProcess instanceof IProcess2) {
                    ((IProcess2) currentProcess).setProperty(selectedProcessItem.getProperty());
                }
            }
            if (currentProcess == null) {
                return null;
            }
        } else {
            currentProcess = jobInfo.getProcess();
        }

        IProcessor processor = null;
        if (processor2 != null) {
            processor = processor2;
        } else {
            if (selectedProcessItem == null) { // shadow process
                processor = getProcessor(currentProcess, null);
            } else {
                processor = getProcessor(currentProcess, selectedProcessItem.getProperty());
            }
        }
        jobInfo.setProcessor(processor);

        if (isMainJob && selectedProcessItem != null) {
            Relation mainRelation = new Relation();
            mainRelation.setId(jobInfo.getJobId());
            mainRelation.setVersion(jobInfo.getJobVersion());
            mainRelation.setType(RelationshipItemBuilder.JOB_RELATION);
            hasLoopDependency = checkLoopDependencies(mainRelation);
            // clean the previous code in case it has deleted subjob
            cleanSourceFolder(progressMonitor, currentProcess, processor);
        }

        // processor.cleanBeforeGenerate(TalendProcessOptionConstants.CLEAN_JAVA_CODES |
        // TalendProcessOptionConstants.CLEAN_CONTEXTS
        // | TalendProcessOptionConstants.CLEAN_DATA_SETS);

        generateJobInfo(jobInfo, isMainJob, currentProcess, selectedProcessItem);
        // pigudf
        Set<String> neededpigudf = currentProcess.getNeededPigudf();
        if (neededpigudf != null) {
            LastGenerationInfo.getInstance().setPigudfNeededPerJob(jobInfo.getJobId(), jobInfo.getJobVersion(), neededpigudf);
            LastGenerationInfo.getInstance().setPigudfNeededWithSubjobPerJob(jobInfo.getJobId(), jobInfo.getJobVersion(),
                    neededpigudf);
        }

        Set<String> neededRoutines = currentProcess.getNeededRoutines();
        if (neededRoutines != null) {
            // item can be null in case of job preview
            LastGenerationInfo.getInstance().setRoutinesNeededPerJob(jobInfo.getJobId(), jobInfo.getJobVersion(), neededRoutines);
            LastGenerationInfo.getInstance().setRoutinesNeededWithSubjobPerJob(jobInfo.getJobId(), jobInfo.getJobVersion(),
                    neededRoutines);
        }

        boolean codeGenerationNeeded = isCodeGenerationNeeded(jobInfo, statistics, trace);
        if (currentProcess != null) {
            if (codeGenerationNeeded && (currentProcess instanceof IProcess2) && exportConfig) {
                ((IProcess2) currentProcess).setProcessModified(true);
            }
            // TDI-26513:For the Dynamic schema,need to check the currentProcess(job or joblet)
            checkMetadataDynamic(currentProcess, jobInfo);
            checkUsePigUDFs(currentProcess, jobInfo);
        }
        Set<ModuleNeeded> neededLibraries = CorePlugin.getDefault().getDesignerCoreService()
                .getNeededLibrariesForProcess(currentProcess, false);
        if (neededLibraries != null) {
            LastGenerationInfo.getInstance().setModulesNeededWithSubjobPerJob(jobInfo.getJobId(), jobInfo.getJobVersion(),
                    neededLibraries);
            LastGenerationInfo.getInstance().setModulesNeededPerJob(jobInfo.getJobId(), jobInfo.getJobVersion(), neededLibraries);
            // must install the needed libraries before generate codes with poms.
            CorePlugin.getDefault().getRunProcessService().updateLibraries(neededLibraries, currentProcess,
                    retrievedJarsForCurrentBuild);

        }
        resetRunJobComponentParameterForContextApply(jobInfo, currentProcess, selectedContextName);

        generateNodeInfo(jobInfo, selectedContextName, statistics, needContext, option, progressMonitor, currentProcess);

        if (neededLibraries != null) {
            Set<ModuleNeeded> adjustClassPath = new HashSet<>(LastGenerationInfo.getInstance()
                    .getModulesNeededWithSubjobPerJob(jobInfo.getJobId(), jobInfo.getJobVersion()));
            for (IClasspathAdjuster adjuster : classPathAdjusters) {
                adjuster.collectInfo(currentProcess, adjustClassPath);
                adjustClassPath = adjuster.adjustClassPath(currentProcess, adjustClassPath);
            }
            LastGenerationInfo.getInstance().setModulesNeededWithSubjobPerJob(jobInfo.getJobId(), jobInfo.getJobVersion(),
                    adjustClassPath);
        }
        final Map<String, Object> argumentsMap = new HashMap<String, Object>();
        argumentsMap.put(TalendProcessArgumentConstant.ARG_ENABLE_STATS, statistics);
        argumentsMap.put(TalendProcessArgumentConstant.ARG_ENABLE_TRACS, trace);
        argumentsMap.put(TalendProcessArgumentConstant.ARG_ENABLE_APPLY_CONTEXT_TO_CHILDREN, jobInfo.isApplyContextToChildren());
        argumentsMap.put(TalendProcessArgumentConstant.ARG_GENERATE_OPTION, option);

        setNeededResources(argumentsMap, jobInfo);

        processor.setArguments(argumentsMap);

        copyDQDroolsToSrc(selectedProcessItem);
        // generate the code of the father after the childrens
        // so the code won't have any error during the check, and it will help to check
        // if the generation is really needed.
        generateContextInfo(jobInfo, selectedContextName, statistics, trace, needContext, progressMonitor, currentProcess,
                currentJobName, processor, isMainJob, codeGenerationNeeded);

        // for testContainer dataSet
        generateDataSet(currentProcess, processor);

        generatePigudfInfor(jobInfo, selectedProcessItem, currentProcess, processor, neededLibraries);

        /*
         * Set classpath for current job. If current job include some child-jobs, the child job SHARE farther job
         * libraries.
         */
        generateBuildInfo(jobInfo, progressMonitor, isMainJob, currentProcess, currentJobName, processor, option);

        return processor;
    }

    private static boolean checkLoopDependencies(Relation mainJobInfo) throws ProcessorException {
        List<Relation> itemsJobRelatedTo = getItemsRelation(mainJobInfo);
        List<Relation> relationChecked = new ArrayList<>();
        relationChecked.add(mainJobInfo);
        return checkLoopDependencies(mainJobInfo, itemsJobRelatedTo, relationChecked);
    }

    private static boolean checkLoopDependencies(Relation mainRelation, List<Relation> itemsJobRelatedTo,
            List<Relation> relationChecked) throws ProcessorException {
        boolean hasDependency = false;
        for (Relation relation : itemsJobRelatedTo) {
            hasDependency = relation.getId().equals(mainRelation.getId())
                    && relation.getVersion().equals(mainRelation.getVersion());
            if (!hasDependency) {
                List<Relation> itemsChildJob = getItemsRelation(relation);
                if (!relationChecked.contains(relation)) {
                    relationChecked.add(relation);
                    hasDependency = checkLoopDependencies(mainRelation, itemsChildJob, relationChecked);
                }
                if (!hasDependency) {
                    for (Relation childRelation : itemsChildJob) {
                        hasDependency = checkLoopDependencies(childRelation);
                        if (hasDependency) {
                            break;
                        }
                    }
                }
            }
            if (hasDependency) {
                break;
            }
        }

        return hasDependency;
    }

    private static List<Relation> getItemsRelation(Relation mainJobInfo) throws ProcessorException {
        List<Relation> itemsJobRelatedTo = new ArrayList<Relation>();
        try {
            List<Project> allProjects = new ArrayList<Project>();
            allProjects.add(ProjectManager.getInstance().getCurrentProject());
            allProjects.addAll(ProjectManager.getInstance().getAllReferencedProjects());
            RelationshipItemBuilder instance = RelationshipItemBuilder.getInstance();
            if (instance != null) {
                itemsJobRelatedTo.addAll(instance.getItemsChildRelatedTo(mainJobInfo.getId(), mainJobInfo.getVersion(),
                        mainJobInfo.getType(), RelationshipItemBuilder.JOB_RELATION));
                itemsJobRelatedTo.addAll(instance.getItemsChildRelatedTo(mainJobInfo.getId(), mainJobInfo.getVersion(),
                        mainJobInfo.getType(), RelationshipItemBuilder.JOBLET_RELATION));
                for (Relation relation : itemsJobRelatedTo) {
                    if (relation.getVersion().equals(RelationshipItemBuilder.LATEST_VERSION)) {
                        for (Project project : allProjects) {
                            IRepositoryViewObject lastVersion = ProxyRepositoryFactory.getInstance().getLastVersion(project,
                                    relation.getId());
                            if (lastVersion != null) {
                                relation.setVersion(lastVersion.getVersion());
                                break;
                            }
                        }
                    }
                }
            }
        } catch (PersistenceException e) {
            throw new ProcessorException(e);
        }

        return itemsJobRelatedTo;
    }

    private static void setNeededResources(final Map<String, Object> argumentsMap, JobInfo jobInfo) {
        argumentsMap.put(TalendProcessArgumentConstant.ARG_NEED_XMLMAPPINGS,
                LastGenerationInfo.getInstance().isUseDynamic(jobInfo.getJobId(), jobInfo.getJobVersion()));
        argumentsMap.put(TalendProcessArgumentConstant.ARG_NEED_RULES,
                LastGenerationInfo.getInstance().isUseRules(jobInfo.getJobId(), jobInfo.getJobVersion()));
        argumentsMap.put(TalendProcessArgumentConstant.ARG_NEED_PIGUDFS,
                LastGenerationInfo.getInstance().isUsePigUDFs(jobInfo.getJobId(), jobInfo.getJobVersion()));
    }

    private static void deleteGeneratedResources(ITalendProcessJavaProject javaProject, String projectPackage,
            IFolder sourceFolder, IProgressMonitor monitor) throws ProcessorException {
        IFolder sourcesFilesFolder = sourceFolder.getFolder(projectPackage);
        try {
            javaProject.cleanFolder(monitor, sourceFolder);
            sourcesFilesFolder.refreshLocal(IResource.DEPTH_INFINITE, monitor);
        } catch (CoreException e) {
            throw new ProcessorException(e);
        }
    }

    private static void generatePigudfInfor(JobInfo jobInfo, ProcessItem selectedProcessItem, IProcess currentProcess,
            IProcessor processor, Set<ModuleNeeded> neededLibraries) throws ProcessorException {
        // generate pigudf.jar before generate code
        // update calss path before export pigudf
        Set<ModuleNeeded> neededModules = LastGenerationInfo.getInstance().getModulesNeededWithSubjobPerJob(jobInfo.getJobId(),
                jobInfo.getJobVersion());
        Set<String> pigudfNeededWithSubjobPerJob = LastGenerationInfo.getInstance()
                .getPigudfNeededWithSubjobPerJob(jobInfo.getJobId(), jobInfo.getJobVersion());
        String pigModuleName = null;
        if (selectedProcessItem != null && !pigudfNeededWithSubjobPerJob.isEmpty()) {
            CorePlugin.getDefault().getRunProcessService().updateLibraries(neededModules, currentProcess);
            IRepositoryService service = CorePlugin.getDefault().getRepositoryService();
            pigModuleName = service.exportPigudf(processor, selectedProcessItem.getProperty(), exportConfig);
        }
        Set<ModuleNeeded> test = LastGenerationInfo.getInstance().getModulesNeededWithSubjobPerJob(jobInfo.getJobId(),
                jobInfo.getJobVersion());
        if (test != null && pigModuleName != null) {
            test.add(new ModuleNeeded(null, pigModuleName, null, true));
        }
    }

    /**
     *
     * This method is used when export job or joblet , check if one of the database component node use dynamic metadata
     */
    private static void checkMetadataDynamic(IProcess currentProcess, JobInfo jobInfo) {
        boolean hasDynamicMetadata = hasMetadataDynamic(currentProcess, jobInfo);
        LastGenerationInfo.getInstance().setUseDynamic(jobInfo.getJobId(), jobInfo.getJobVersion(), hasDynamicMetadata);
        if (hasDynamicMetadata) {
            try {
                URL url = MetadataTalendType.getProjectForderURLOfMappingsFile();
                if (url != null) {
                    // set the project mappings url
                    System.setProperty(ProcessorUtilities.PROP_MAPPINGS_URL, url.toString()); // $NON-NLS-1$

                    IFolder xmlMappingFolder = jobInfo.getProcessor().getTalendJavaProject().getResourceSubFolder(null,
                            JavaUtils.JAVA_XML_MAPPING);
                    if (xmlMappingFolder.members().length == 0
                            && GlobalServiceRegister.getDefault().isServiceRegistered(ICoreService.class)) {
                        ICoreService coreService = (ICoreService) GlobalServiceRegister.getDefault()
                                .getService(ICoreService.class);
                        coreService.synchronizeMapptingXML(jobInfo.getProcessor().getTalendJavaProject());
                    }
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
    }

    private static void checkUsePigUDFs(IProcess currentProcess, JobInfo jobInfo) {
        // FIXME, after remove all PigUDFs, won't update the cache. so comment it.
        // if (!LastGenerationInfo.getInstance().isUsePigUDFs(jobInfo.getJobId(), jobInfo.getJobVersion())) {
        boolean usePigUDFs = ProcessUtils.isRequiredPigUDFs(currentProcess);
        LastGenerationInfo.getInstance().setUsePigUDFs(jobInfo.getJobId(), jobInfo.getJobVersion(), usePigUDFs);
        // }
    }

    public static boolean hasMetadataDynamic(IProcess currentProcess, JobInfo jobInfo) {
        boolean hasDynamicMetadata = false;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerCoreService.class)) {
            IDesignerCoreService designerCoreService = (IDesignerCoreService) GlobalServiceRegister.getDefault()
                    .getService(IDesignerCoreService.class);
            for (INode node : currentProcess.getGraphicalNodes()) {
                if (designerCoreService.isDelegateNode(node)) { // for jdbc, currently
                    return true;
                }
            }
        }

        out: for (INode node : (List<? extends INode>) currentProcess.getGeneratingNodes()) {
            if (node.getComponent() != null && node.getComponent().getComponentType() == EComponentType.GENERIC) {
                // generic component, true always
                return true;
            }
            // to check if node is db component , maybe need modification
            boolean isDbNode = false;
            for (IElementParameter param : (List<? extends IElementParameter>) node.getElementParameters()) {
                if (EParameterFieldType.TEXT == param.getFieldType() && param.getValue() != null) {
                    if (("TYPE".equals(param.getName()) && !"".equals(param.getValue()))
                            || ("FAMILY".equals(param.getName()) && param.getValue().toString().startsWith("Database"))) {
                        isDbNode = true;
                        break;
                    }
                }
            }
            if (isDbNode) {
                for (IMetadataTable metadataTable : node.getMetadataList()) {
                    for (IMetadataColumn column : metadataTable.getListColumns()) {
                        if ("id_Dynamic".equals(column.getTalendType())) {
                            hasDynamicMetadata = true;
                            break out;
                        }
                    }
                }
            }
        }
        return hasDynamicMetadata;
    }

    private static void generateBuildInfo(JobInfo jobInfo, IProgressMonitor progressMonitor, boolean isMainJob,
            IProcess currentProcess, String currentJobName, IProcessor processor, int option) throws ProcessorException {
        IFile pomFile = jobInfo.getProcessor().getTalendJavaProject().getProjectPom();
        IFile codeFile = jobInfo.getProcessor().getTalendJavaProject().getProject()
                .getFile(jobInfo.getProcessor().getSrcCodePath());
        jobInfo.setPomFile(pomFile);
        jobInfo.setCodeFile(codeFile);
        jobInfo.setProcess(null);
        jobInfo.setProcessor(null);
        progressMonitor.subTask(Messages.getString("ProcessorUtilities.finalizeBuild") + currentJobName); //$NON-NLS-1$

        final String timeMeasureGenerateCodesId = "Generate job source codes for " //$NON-NLS-1$
                + (jobInfo.getJobName() != null ? jobInfo.getJobName() : jobInfo.getJobId());
        TimeMeasure.step(timeMeasureGenerateCodesId, "Generated all source codes with children jobs (if have)");
        if (codeModified && !BitwiseOptionUtils.containOption(option, GENERATE_WITHOUT_COMPILING)) {
            try {
                if (isMainJob && hasLoopDependency) {
                    IRunProcessService service = CorePlugin.getDefault().getRunProcessService();
                    service.handleJobDependencyLoop(jobInfo, jobList, progressMonitor);

                }
                if (!hasLoopDependency || (isMainJob && hasLoopDependency)) {
                    processor.build(progressMonitor);
                }
            } catch (Exception e) {
                throw new ProcessorException(e);
            }
            TimeMeasure.step(timeMeasureGenerateCodesId, "Compile all source codes");
            processor.syntaxCheck();

            // TDI-36930, just after compile, need check the compile errors first.
            // only check current build
            if (isMainJob) {
                CorePlugin.getDefault().getRunProcessService().checkLastGenerationHasCompilationError(true);
            }
        }
        codeModified = false;
        if (isMainJob) {
            retrievedJarsForCurrentBuild.clear();
            needContextInCurrentGeneration = true;
        }
    }

    private static void generateContextInfo(JobInfo jobInfo, String selectedContextName, boolean statistics, boolean trace,
            boolean needContext, IProgressMonitor progressMonitor, IProcess currentProcess, String currentJobName,
            IProcessor processor, boolean isMain, boolean codeGenerationNeeded) throws ProcessorException {
        LastGenerationInfo.getInstance().setCurrentBuildJob(jobInfo);
        if (codeGenerationNeeded) {
            codeModified = true;
            if ((currentProcess instanceof IProcess2) && exportConfig) {
                resetRunJobComponentParameterForContextApply(jobInfo, currentProcess, selectedContextName);
            }
            progressMonitor.subTask(Messages.getString("ProcessorUtilities.generatingJob") + currentJobName); //$NON-NLS-1$
            IContext currentContext;
            if (jobInfo.getContext() == null) {
                currentContext = getContext(currentProcess, jobInfo.getContextName());
            } else {
                currentContext = jobInfo.getContext();
            }

            // always generate all context files.
            if (needContext) {

                List<IContext> list = currentProcess.getContextManager().getListContext();
                for (IContext context : list) {
                    if (context.getName().equals(currentContext.getName())) {
                        processor.setContext(currentContext); // generate current context.
                    } else {
                        processor.setContext(context);
                    }
                    LastGenerationInfo.getInstance().getContextPerJob(jobInfo.getJobId(), jobInfo.getJobVersion())
                            .add(context.getName());
                    try {
                        processor.generateContextCode();
                    } catch (ProcessorException pe) {
                        ExceptionHandler.process(pe);
                    }
                }
            }

            processor.setContext(currentContext);

            // main job will use stats / traces
            int option = TalendProcessOptionConstants.GENERATE_WITHOUT_FORMAT;
            if (isMain) {
                // only format for main job
                option = TalendProcessOptionConstants.GENERATE_IS_MAINJOB;
            }
            processor.generateCode(statistics, trace, true, option);
            if (currentProcess instanceof IProcess2 && ((IProcess2) currentProcess).getProperty() != null) {
                designerCoreService.getLastGeneratedJobsDateMap().put(currentProcess.getId(),
                        ((IProcess2) currentProcess).getModificationDate());
            }
            Integer infos = new Integer(0);
            infos += statistics ? GENERATED_WITH_STATS : 0;
            infos += trace ? GENERATED_WITH_TRACES : 0;
            lastGeneratedWithStatsOrTrace.put(jobInfo.getJobId(), infos);

            if (currentProcess instanceof IProcess2) {
                processor.generateEsbFiles();
                ((IProcess2) currentProcess).setNeedRegenerateCode(false);
            }
        } else {
            processor.setCodeGenerated(true);
        }
    }

    private static void generateDataSet(IProcess process, IProcessor processor) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class)) {
            ITestContainerProviderService testContainerService = (ITestContainerProviderService) GlobalServiceRegister
                    .getDefault().getService(ITestContainerProviderService.class);
            if (testContainerService != null) {
                if (!testContainerService.isTestContainerProcess(process)) {
                    return;
                }
                testContainerService.copyDataSetFiles(process, processor.getDataSetPath());
            }
        }
    }

    private static IProcessor generateCode(JobInfo jobInfo, String selectedContextName, boolean statistics, boolean trace,
            boolean needContext, int option, IProgressMonitor progressMonitor) throws ProcessorException {
        if (!BitwiseOptionUtils.containOption(option, GENERATE_WITHOUT_COMPILING)) {
            CorePlugin.getDefault().getRunProcessService().buildCodesJavaProject(progressMonitor);
        }
        return generateCode(jobInfo, selectedContextName, statistics, trace, needContext, true, option, progressMonitor);
    }

    private static IProcessor generateCode(JobInfo jobInfo, String selectedContextName, boolean statistics, boolean trace,
            boolean needContext, boolean isNeedLoadmodules, int option, IProgressMonitor progressMonitor)
            throws ProcessorException {
        needContextInCurrentGeneration = needContext;

        final boolean oldMeasureActived = TimeMeasure.measureActive;
        if (!oldMeasureActived) { // not active before.
            TimeMeasure.display = TimeMeasure.displaySteps = TimeMeasure.measureActive = CommonsPlugin.isDebugMode();
        }

        boolean timerStarted = false;
        String idTimer = "generateCode for job: <job not loaded yet>";
        if (jobInfo.getJobName() != null) {
            idTimer = "generateCode for job: " + jobInfo.getJobName();
            timerStarted = true;
            TimeMeasure.begin(idTimer);
        }
        final String timeMeasureGenerateCodesId = "Generate job source codes for " //$NON-NLS-1$
                + (jobInfo.getJobName() != null ? jobInfo.getJobName() : jobInfo.getJobId());
        TimeMeasure.begin(timeMeasureGenerateCodesId);
        try {
            if (progressMonitor == null) {
                progressMonitor = new NullProgressMonitor();
            }
            if (progressMonitor.isCanceled()) {
                return null;
            }
            boolean isMainJob = false;
            List<IClasspathAdjuster> classPathAdjusters = ClasspathAdjusterProvider.getClasspathAdjuster();
            if (jobInfo.getFatherJobInfo() == null) {
                isMainJob = true;
                codeModified = false;

                // this cache only keep the last main job's generation, so clear it since we regenerate a new job.
                LastGenerationInfo.getInstance().getLastGeneratedjobs().clear();
                LastGenerationInfo.getInstance().getHighPriorityModuleNeeded().clear();
                retrievedJarsForCurrentBuild.clear();

                // if it's the father, reset the processMap to ensure to have a good
                // code generation
                ItemCacheManager.clearCache();

                for (IClasspathAdjuster adjuster : classPathAdjusters) {
                    adjuster.initialize();
                }
            }

            IProcess currentProcess = null;
            jobList.add(jobInfo);
            ProcessItem selectedProcessItem;

            selectedProcessItem = jobInfo.getProcessItem();
            String currentJobName = null;
            if (selectedProcessItem == null && jobInfo.getJobVersion() == null) {
                // child job
                selectedProcessItem = ItemCacheManager.getProcessItem(jobInfo.getJobId());
            }

            if (selectedProcessItem == null && jobInfo.getJobVersion() != null) {
                selectedProcessItem = ItemCacheManager.getProcessItem(jobInfo.getJobId(), jobInfo.getJobVersion());
                jobInfo.setProcessItem(selectedProcessItem);
            }

            if (selectedProcessItem == null && jobInfo.getProcess() == null) {
                return null;
            }
            if (selectedProcessItem != null) {
                currentJobName = selectedProcessItem.getProperty().getLabel();
            }
            progressMonitor.subTask(Messages.getString("ProcessorUtilities.loadingJob") + currentJobName); //$NON-NLS-1$

            if (jobInfo.getProcess() == null) {
                if (selectedProcessItem != null) {
                    IDesignerCoreService service = CorePlugin.getDefault().getDesignerCoreService();
                    currentProcess = service.getProcessFromProcessItem(selectedProcessItem);
                    jobInfo.setProcess(currentProcess);
                    if (currentProcess instanceof IProcess2) {
                        ((IProcess2) currentProcess).setProperty(selectedProcessItem.getProperty());
                    }
                }
                if (currentProcess == null) {
                    return null;
                }
            } else {
                currentProcess = jobInfo.getProcess();
            }

            IProcessor processor = null;
            if (selectedProcessItem == null) { // shadow process
                processor = getProcessor(currentProcess, null);
            } else {
                processor = getProcessor(currentProcess, selectedProcessItem.getProperty());
            }

            if (isMainJob && selectedProcessItem != null) {
                Relation mainRelation = new Relation();
                mainRelation.setId(jobInfo.getJobId());
                mainRelation.setVersion(jobInfo.getJobVersion());
                mainRelation.setType(RelationshipItemBuilder.JOB_RELATION);
                hasLoopDependency = checkLoopDependencies(mainRelation);
                // clean the previous code in case it has deleted subjob
                cleanSourceFolder(progressMonitor, currentProcess, processor);
            }

            // processor.cleanBeforeGenerate(TalendProcessOptionConstants.CLEAN_JAVA_CODES
            // | TalendProcessOptionConstants.CLEAN_CONTEXTS | TalendProcessOptionConstants.CLEAN_DATA_SETS);
            jobInfo.setProcessor(processor);
            JobInfo parentJob = jobInfo.getFatherJobInfo();
            if (parentJob != null && (parentJob.getProcessor() != null)) {
                for (JobInfo subJob : parentJob.getProcessor().getBuildChildrenJobs()) {
                    if (subJob.getJobId().equals(jobInfo.getJobId())) {
                        subJob.setProcessor(processor);
                    }
                }
            }
            if (!timerStarted) {
                idTimer = "generateCode for job: " + currentProcess.getName();
                TimeMeasure.begin(idTimer);
            } else {
                TimeMeasure.step(idTimer, "Loading job");
            }
            if (currentProcess instanceof IProcess2) {
                ((IProcess2) currentProcess).setNeedLoadmodules(isNeedLoadmodules);
            }
            generateJobInfo(jobInfo, isMainJob, currentProcess, selectedProcessItem);
            TimeMeasure.step(idTimer, "generateJobInfo");
            // pigudf
            Set<String> neededpigudf = currentProcess.getNeededPigudf();
            if (neededpigudf != null) {
                LastGenerationInfo.getInstance().setPigudfNeededPerJob(jobInfo.getJobId(), jobInfo.getJobVersion(), neededpigudf);
                LastGenerationInfo.getInstance().setPigudfNeededWithSubjobPerJob(jobInfo.getJobId(), jobInfo.getJobVersion(),
                        neededpigudf);
            }

            Set<String> neededRoutines = currentProcess.getNeededRoutines();
            if (neededRoutines != null) {
                // item can be null in case of job preview

                LastGenerationInfo.getInstance().setRoutinesNeededPerJob(jobInfo.getJobId(), jobInfo.getJobVersion(),
                        neededRoutines);
                LastGenerationInfo.getInstance().setRoutinesNeededWithSubjobPerJob(jobInfo.getJobId(), jobInfo.getJobVersion(),
                        neededRoutines);
            }
            boolean codeGenerationNeeded = isCodeGenerationNeeded(jobInfo, statistics, trace);
            if (currentProcess != null) {
                if (codeGenerationNeeded && (currentProcess instanceof IProcess2) && exportConfig) {
                    ((IProcess2) currentProcess).setProcessModified(true);
                }
                checkMetadataDynamic(currentProcess, jobInfo);
                checkUsePigUDFs(currentProcess, jobInfo);
            }

            Set<ModuleNeeded> neededLibraries = CorePlugin.getDefault().getDesignerCoreService()
                    .getNeededLibrariesForProcess(currentProcess, false);
            if (neededLibraries != null) {
                LastGenerationInfo.getInstance().setModulesNeededWithSubjobPerJob(jobInfo.getJobId(), jobInfo.getJobVersion(),
                        neededLibraries);
                LastGenerationInfo.getInstance().setModulesNeededPerJob(jobInfo.getJobId(), jobInfo.getJobVersion(),
                        neededLibraries);
                // must install the needed libraries before generate codes with poms.
                CorePlugin.getDefault().getRunProcessService().updateLibraries(neededLibraries, currentProcess,
                        retrievedJarsForCurrentBuild);

            }
            resetRunJobComponentParameterForContextApply(jobInfo, currentProcess, selectedContextName);

            generateNodeInfo(jobInfo, selectedContextName, statistics, needContext, option, progressMonitor, currentProcess);
            TimeMeasure.step(idTimer, "generateNodeInfo");

            if (neededLibraries != null) {
                if (isNeedLoadmodules) {
                    Set<ModuleNeeded> adjustClassPath = new HashSet<>(LastGenerationInfo.getInstance()
                            .getModulesNeededWithSubjobPerJob(jobInfo.getJobId(), jobInfo.getJobVersion()));
                    for (IClasspathAdjuster adjuster : classPathAdjusters) {
                        adjuster.collectInfo(currentProcess, adjustClassPath);
                        adjustClassPath = adjuster.adjustClassPath(currentProcess, adjustClassPath);
                    }
                    LastGenerationInfo.getInstance().setModulesNeededWithSubjobPerJob(jobInfo.getJobId(), jobInfo.getJobVersion(),
                            adjustClassPath);
                }
            }

            Map<String, Object> argumentsMap = jobInfo.getArgumentsMap();
            if (argumentsMap != null) {
                processor.setArguments(argumentsMap);
            } else {
                argumentsMap = new HashMap<String, Object>();
                argumentsMap.put(TalendProcessArgumentConstant.ARG_ENABLE_STATS, statistics);
                argumentsMap.put(TalendProcessArgumentConstant.ARG_ENABLE_TRACS, trace);
                argumentsMap.put(TalendProcessArgumentConstant.ARG_ENABLE_APPLY_CONTEXT_TO_CHILDREN,
                        jobInfo.isApplyContextToChildren());
                argumentsMap.put(TalendProcessArgumentConstant.ARG_GENERATE_OPTION, option);
                processor.setArguments(argumentsMap);
            }
            setNeededResources(argumentsMap, jobInfo);

            processor.setArguments(argumentsMap);

            copyDQDroolsToSrc(selectedProcessItem);

            generateContextInfo(jobInfo, selectedContextName, statistics, trace, needContext, progressMonitor, currentProcess,
                    currentJobName, processor, isMainJob, codeGenerationNeeded);

            // for testContainer dataSet
            generateDataSet(currentProcess, processor);

            generatePigudfInfor(jobInfo, selectedProcessItem, currentProcess, processor, neededLibraries);

            TimeMeasure.step(idTimer, "generateContextInfo");

            /*
             * Set classpath for current job. If current job include some child-jobs, the child job SHARE farther job
             * libraries.
             */
            generateBuildInfo(jobInfo, progressMonitor, isMainJob, currentProcess, currentJobName, processor, option);
            TimeMeasure.step(idTimer, "generateBuildInfo");

            return processor;
        } finally {
            TimeMeasure.end(timeMeasureGenerateCodesId);
            TimeMeasure.end(idTimer);
            // if active before, not disable and active still.
            if (!oldMeasureActived) {
                TimeMeasure.display = TimeMeasure.displaySteps = TimeMeasure.measureActive = false;
            }
        }
    }

    /**
     * DOC nrousseau Comment method "cleanSourceFolder".
     * 
     * @param progressMonitor
     * @param currentProcess
     * @param processor
     */
    public static void cleanSourceFolder(IProgressMonitor progressMonitor, IProcess currentProcess, IProcessor processor) {
        try {
            IPath codePath = processor.getSrcCodePath().removeLastSegments(2);
            IFolder srcFolder = processor.getTalendJavaProject().getProject().getFolder(codePath);
            String jobPackageFolder = JavaResourcesHelper.getJobClassPackageFolder(currentProcess);
            for (IResource resource : srcFolder.members()) {
                if (resource.getProjectRelativePath().toPortableString().endsWith(jobPackageFolder)) {
                    break;
                }
                resource.delete(true, progressMonitor);
            }
        } catch (CoreException e) {
            ExceptionHandler.process(e);
        }
    }

    private static void generateJobInfo(JobInfo jobInfo, boolean isMainJob, IProcess currentProcess,
            ProcessItem selectedProcessItem) {
        if (!CommonsPlugin.isHeadless()) {
            if (currentProcess instanceof IProcess2) {
                // code below will check the process and update the Problems view.
                // it was executed before in JobErrorsChecker (moved for performances issues)
                IProcess2 process2 = (IProcess2) currentProcess;
                process2.setActivate(true);
                process2.checkProcess();
            }
        }
        // set the last jobinfos to be able to set check the errors in the problems view (errors of compilations
        // only)
        // here we recreate a new JobInfo, to be sure to don't have link in memory to Emf or IProcess
        JobInfo generatedJobInfo = cloneJobInfo(jobInfo);
        String projectFolderName = JavaResourcesHelper.getProjectFolderName(selectedProcessItem);
        generatedJobInfo.setProjectFolderName(projectFolderName);
        LastGenerationInfo.getInstance().getLastGeneratedjobs().add(generatedJobInfo);
        if (isMainJob) {
            LastGenerationInfo.getInstance().setLastMainJob(generatedJobInfo);
        }
    }

    /**
     * 
     * copy the current item's drools file from 'workspace/metadata/survivorship' to '.Java/src/resources'
     * 
     * @param processItem
     */
    private static void copyDQDroolsToSrc(ProcessItem processItem) {
        // 1.TDQ-12474 copy the "metadata/survivorship/rulePackage" to ".Java/src/main/resources/". so that it will be
        // used by
        // maven command 'include-survivorship-rules' to export.
        // 2.TDQ-14308 current drools file in 'src/resourcesmetadata/survivorship/' should be included to job jar.
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITDQItemService.class)) {
            ITDQItemService tdqItemService = (ITDQItemService) GlobalServiceRegister.getDefault()
                    .getService(ITDQItemService.class);
            if (tdqItemService == null) {
                return;
            }
            try {
                ExportFileResource resouece = new ExportFileResource();
                BuildExportManager.getInstance().exportDependencies(resouece, processItem);
                if (resouece.getAllResources().isEmpty()) {
                    return;
                }
                final Iterator<String> relativepath = resouece.getRelativePathList().iterator();
                String pathStr = "metadata/survivorship"; //$NON-NLS-1$
                IRunProcessService runProcessService = CorePlugin.getDefault().getRunProcessService();
                ITalendProcessJavaProject talendProcessJavaProject = runProcessService
                        .getTalendJobJavaProject(processItem.getProperty());
                IFolder targetFolder = talendProcessJavaProject.getResourcesFolder();
                if (targetFolder.exists()) {
                    IFolder survFolder = targetFolder.getFolder(new Path(pathStr));
                    // only copy self job rules, clear the 'survivorship' folder before copy.
                    if (survFolder.exists()) {
                        survFolder.delete(true, null);
                    }
                    while (relativepath.hasNext()) {
                        String relativePath = relativepath.next();
                        Set<URL> sources = resouece.getResourcesByRelativePath(relativePath);
                        for (URL sourceUrl : sources) {
                            File currentResource = new File(
                                    org.talend.commons.utils.io.FilesUtils.getFileRealPath(sourceUrl.getPath()));
                            if (currentResource.exists()) {
                                FilesUtils.copyDirectory(currentResource,
                                        new File(targetFolder.getLocation().toPortableString() + File.separator + pathStr));
                            }
                        }
                    }
                }
            } catch (Exception exc) {
                log.error(exc);
            }
        }

    }

    /**
     * DOC nrousseau Comment method "cloneJobInfo".
     *
     * @param jobInfo
     * @return
     */
    private static JobInfo cloneJobInfo(JobInfo jobInfo) {
        if (jobInfo == null) {
            return null;
        }
        JobInfo generatedJobInfo = new JobInfo(jobInfo.getJobId(), jobInfo.getContextName(), jobInfo.getJobVersion());
        generatedJobInfo.setJobName(jobInfo.getJobName());
        generatedJobInfo.setTestContainer(jobInfo.isTestContainer());
        generatedJobInfo.setFatherJobInfo(cloneJobInfo(jobInfo.getFatherJobInfo()));
        generatedJobInfo.setProcessor(jobInfo.getProcessor());
        return generatedJobInfo;
    }

    private static void generateNodeInfo(JobInfo jobInfo, String selectedContextName, boolean statistics, boolean properties,
            int option, IProgressMonitor progressMonitor, IProcess currentProcess) throws ProcessorException {
        if (BitwiseOptionUtils.containOption(option, GENERATE_TESTS) && jobInfo.getProcessItem() != null) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class)) {
                ITestContainerProviderService testContainerService = (ITestContainerProviderService) GlobalServiceRegister
                        .getDefault().getService(ITestContainerProviderService.class);
                if (testContainerService != null) {
                    List<ProcessItem> testsItems = testContainerService.getTestContainersByVersion(jobInfo.getProcessItem());
                    for (ProcessItem testItem : testsItems) {
                        JobInfo subJobInfo = new JobInfo(testItem, testItem.getProcess().getDefaultContext());
                        subJobInfo.setTestContainer(true);
                        subJobInfo.setFatherJobInfo(jobInfo);

                        if (BitwiseOptionUtils.containOption(option, GENERATE_WITH_FIRST_CHILD)) {
                            generateCode(subJobInfo, selectedContextName, statistics, false, properties, GENERATE_MAIN_ONLY,
                                    progressMonitor);
                        } else {
                            generateCode(subJobInfo, selectedContextName, statistics, false, properties, GENERATE_ALL_CHILDS,
                                    progressMonitor);
                            currentProcess.setNeedRegenerateCode(true);
                        }
                        addSubjobModuleNeededToParentJob(jobInfo, subJobInfo);
                    }
                }
            }
        }
        jobInfo.setProcessItem(null);
        if (!BitwiseOptionUtils.containOption(option, GENERATE_MAIN_ONLY)) {
            // handle subjob in joblet. see bug 004937: tRunJob in a Joblet
            for (INode node : currentProcess.getGeneratingNodes()) {
                String componentName = node.getComponent().getName();
                if ((node != null) && (componentName.equals("tRunJob") || componentName.equals("cTalendJob") //$NON-NLS-1$ //$NON-NLS-2$
                        || "Routelets".equals(node.getComponent().getOriginalFamilyName()))) { //$NON-NLS-1$
                    // if the cTalendJob is configured by external Jar, then ignore it
                    if ("cTalendJob".equals(componentName)) { //$NON-NLS-1$
                        if ((Boolean) node.getElementParameter("FROM_EXTERNAL_JAR").getValue()) { //$NON-NLS-1$
                            continue;
                        }
                    }
                    // IElementParameter indepPara = node.getElementParameter("USE_INDEPENDENT_PROCESS");
                    boolean isNeedLoadmodules = true;
                    // if (indepPara != null) {
                    // isNeedLoadmodules = !(boolean) indepPara.getValue();
                    // }
                    IElementParameter processIdparam = node.getElementParameter("PROCESS_TYPE_PROCESS"); //$NON-NLS-1$
                    // feature 19312
                    final String jobIds = (String) processIdparam.getValue();
                    for (String jobId : jobIds.split(ProcessorUtilities.COMMA)) {
                        if (StringUtils.isNotEmpty(jobId)) {
                            String context = (String) node.getElementParameter("PROCESS_TYPE_CONTEXT").getValue(); //$NON-NLS-1$
                            String version = (String) node.getElementParameter("PROCESS_TYPE_VERSION").getValue(); //$NON-NLS-1$
                            final JobInfo subJobInfo = new JobInfo(jobId, context, version);

                            // get processitem from job
                            final ProcessItem processItem = ItemCacheManager.getProcessItem(jobId, version);

                            if (processItem == null) {
                                throw new ProcessorException(node.getUniqueName()
                                        + " not setup or child job not found in the job:" + currentProcess.getName());
                            }

                            subJobInfo.setJobVersion(processItem.getProperty().getVersion());
                            subJobInfo.setJobName(processItem.getProperty().getLabel());

                            subJobInfo.setFatherJobInfo(jobInfo);
                            if (!jobList.contains(subJobInfo)) {
                                if (!isNeedLoadmodules) {
                                    LastGenerationInfo.getInstance().setModulesNeededWithSubjobPerJob(subJobInfo.getJobId(),
                                            subJobInfo.getJobVersion(), Collections.<ModuleNeeded> emptySet());
                                }

                                if (jobInfo.isApplyContextToChildren()) {
                                    subJobInfo.setApplyContextToChildren(jobInfo.isApplyContextToChildren());
                                    // see bug 0003862: Export job with the flag "Apply to children" if the child don't
                                    // have the same context fails.
                                    if (checkIfContextExisted(processItem, selectedContextName)) {
                                        subJobInfo.setContextName(selectedContextName);
                                    } else {
                                        // use the default context of subjob
                                        String defaultContext = processItem.getProcess().getDefaultContext();
                                        node.getElementParameter("PROCESS_TYPE_CONTEXT").setValue(defaultContext); //$NON-NLS-1$
                                        subJobInfo.setContextName(defaultContext);
                                    }
                                }

                                int subJobOption = GENERATE_ALL_CHILDS;
                                if (BitwiseOptionUtils.containOption(option, GENERATE_WITH_FIRST_CHILD)) {
                                    subJobOption = GENERATE_MAIN_ONLY;
                                }
                                // if need tests, need check for all child jobs.
                                if (BitwiseOptionUtils.containOption(option, GENERATE_TESTS)) {
                                    subJobOption |= GENERATE_TESTS;
                                }
                                if (BitwiseOptionUtils.containOption(option, GENERATE_WITHOUT_COMPILING)) {
                                    subJobOption |= GENERATE_WITHOUT_COMPILING;
                                }
                                // children won't have stats / traces
                                generateCode(subJobInfo, selectedContextName, statistics, false, properties, isNeedLoadmodules,
                                        subJobOption, progressMonitor);

                                if (!BitwiseOptionUtils.containOption(option, GENERATE_WITH_FIRST_CHILD)) {
                                    currentProcess.setNeedRegenerateCode(true);
                                }
                            }

                            setGenerationInfoWithChildrenJob(node, jobInfo, subJobInfo);
                        }
                    }
                }
                if (isEsbComponentName(componentName)) {
                    addEsbJob(jobInfo);
                }
            }
        }
    }

    static void setGenerationInfoWithChildrenJob(INode node, JobInfo jobInfo, final JobInfo subJobInfo) {
        final LastGenerationInfo generationInfo = LastGenerationInfo.getInstance();

        // always check the using function for dynamic type of metadata column, PigUDF, Rules.
        if (!generationInfo.isUseDynamic(jobInfo.getJobId(), jobInfo.getJobVersion())) {
            generationInfo.setUseDynamic(jobInfo.getJobId(), jobInfo.getJobVersion(),
                    generationInfo.isUseDynamic(subJobInfo.getJobId(), subJobInfo.getJobVersion()));
        }
        if (!generationInfo.isUsePigUDFs(jobInfo.getJobId(), jobInfo.getJobVersion())) {
            generationInfo.setUsePigUDFs(jobInfo.getJobId(), jobInfo.getJobVersion(),
                    generationInfo.isUsePigUDFs(subJobInfo.getJobId(), subJobInfo.getJobVersion()));
        }
        if (!generationInfo.isUseRules(jobInfo.getJobId(), jobInfo.getJobVersion())) {
            generationInfo.setUseRules(jobInfo.getJobId(), jobInfo.getJobVersion(),
                    generationInfo.isUseRules(subJobInfo.getJobId(), subJobInfo.getJobVersion()));
        }

        // TUP-5624,
        // no need to add the modules of children job, when using dynamic job or independent
        if (node != null) {
            boolean needChildrenModules = true;
            IElementParameter useDynamicJobParam = node.getElementParameter("USE_DYNAMIC_JOB"); //$NON-NLS-1$
            // true, use dynamic job
            if (useDynamicJobParam != null && useDynamicJobParam.getValue() != null
                    && Boolean.parseBoolean(useDynamicJobParam.getValue().toString())) {
                needChildrenModules = false;
            }
            if (needChildrenModules) { // check another param
                IElementParameter useIndependentParam = node.getElementParameter("USE_INDEPENDENT_PROCESS"); //$NON-NLS-1$
                // true, independent child job
                if (useIndependentParam != null && useIndependentParam.getValue() != null
                        && Boolean.parseBoolean(useIndependentParam.getValue().toString())) {
                    needChildrenModules = false;
                }
            }

            if (needChildrenModules) {
                addSubjobModuleNeededToParentJob(jobInfo, subJobInfo);

            }
        }

    }

    private static void addSubjobModuleNeededToParentJob(JobInfo jobInfo, JobInfo subJobInfo) {
        LastGenerationInfo generationInfo = LastGenerationInfo.getInstance();
        Set<ModuleNeeded> subjobModules = generationInfo.getModulesNeededWithSubjobPerJob(subJobInfo.getJobId(),
                subJobInfo.getJobVersion());
        generationInfo.getModulesNeededWithSubjobPerJob(jobInfo.getJobId(), jobInfo.getJobVersion()).addAll(subjobModules);

        Set<String> subjobRoutineModules = generationInfo.getRoutinesNeededWithSubjobPerJob(subJobInfo.getJobId(),
                subJobInfo.getJobVersion());
        generationInfo.getRoutinesNeededWithSubjobPerJob(jobInfo.getJobId(), jobInfo.getJobVersion())
                .addAll(subjobRoutineModules);

        Set<String> subjobPigUDFModules = generationInfo.getPigudfNeededWithSubjobPerJob(subJobInfo.getJobId(),
                subJobInfo.getJobVersion());
        generationInfo.getPigudfNeededWithSubjobPerJob(jobInfo.getJobId(), jobInfo.getJobVersion()).addAll(subjobPigUDFModules);
    }

    /**
     * Return true if we can find a context name from the child job that matches the selected context name. see bug
     * 0003862: Export job with the flag "Apply to children" if the child don't have the same context fails.
     *
     * @param processItem
     * @param selectedContextName
     * @return
     */
    public static boolean checkIfContextExisted(ProcessItem processItem, String selectedContextName) {
        for (Object o : processItem.getProcess().getContext()) {
            if (o instanceof ContextType) {
                ContextType context = (ContextType) o;
                if (context.getName().equals(selectedContextName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method is used to reset the tRunJob component's context,see feature 1625.
     *
     * @param jobInfo
     * @param currentProcess
     * @param selectedContextName
     */
    private static void resetRunJobComponentParameterForContextApply(JobInfo jobInfo, IProcess currentProcess,
            String selectedContextName) {

        if (jobInfo.isApplyContextToChildren()) {
            for (INode node : currentProcess.getGeneratingNodes()) {
                if ((node != null) && node.getComponent().getName().equals("tRunJob")) { //$NON-NLS-1$
                    // the corresponding parameter is
                    // EParameterName.PROCESS_TYPE_CONTEXT
                    node.getElementParameter("PROCESS_TYPE_CONTEXT").setValue(selectedContextName); //$NON-NLS-1$
                }
            }
        }
    }

    static List<JobInfo> jobList = new ArrayList<JobInfo>();

    static boolean hasLoopDependency = false;

    /**
     * This function will generate the code of the process and all of this sub process.
     *
     * @param processName
     * @param contextName
     * @param version null if no specific version required
     * @throws ProcessorException
     */
    public static IProcessor generateCode(String processName, String contextName, String version, boolean statistics,
            boolean trace, IProgressMonitor... monitors) throws ProcessorException {
        IProgressMonitor monitor = null;
        if (monitors == null) {
            monitor = new NullProgressMonitor();
        }
        jobList.clear();
        esbJobs.clear();
        hasLoopDependency = false;
        JobInfo jobInfo = new JobInfo(processName, contextName, version);
        IProcessor process = generateCode(jobInfo, contextName, statistics, trace, true, GENERATE_ALL_CHILDS, monitor);
        jobList.clear();
        hasLoopDependency = false;
        return process;
    }

    /**
     * This function will generate the code of the process and all of this sub process.
     *
     * @param processName
     * @param contextName
     * @param version null if no specific version required
     * @throws ProcessorException
     */
    public static IProcessor generateCode(String processId, String contextName, String version, boolean statistics, boolean trace,
            boolean applyContextToChildren, IProgressMonitor... monitors) throws ProcessorException {
        IProgressMonitor monitor = null;
        if (monitors == null) {
            monitor = new NullProgressMonitor();
        }
        JobInfo jobInfo = new JobInfo(processId, contextName, version);
        jobInfo.setApplyContextToChildren(applyContextToChildren);
        jobList.clear();
        esbJobs.clear();
        hasLoopDependency = false;
        IProcessor process = generateCode(jobInfo, contextName, statistics, trace, true, GENERATE_ALL_CHILDS, monitor);
        jobList.clear();
        hasLoopDependency = false;
        return process;
    }

    public static IProcessor generateCode(ProcessItem process, String contextName, boolean statistics, boolean trace,
            boolean applyContextToChildren, IProgressMonitor... monitors) throws ProcessorException {
        IProgressMonitor monitor = null;
        if (monitors != null && monitors.length > 0) {
            monitor = monitors[0];
        }
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        JobInfo jobInfo = new JobInfo(process, contextName);
        jobInfo.setApplyContextToChildren(applyContextToChildren);
        jobList.clear();
        esbJobs.clear();
        hasLoopDependency = false;
        IProcessor result = generateCode(jobInfo, contextName, statistics, trace, true, GENERATE_ALL_CHILDS, monitor);
        jobList.clear();
        hasLoopDependency = false;
        return result;
    }

    public static IProcessor generateCode(ProcessItem process, String contextName, String version, boolean statistics,
            boolean trace, boolean applyContextToChildren, boolean needContext, IProgressMonitor... monitors)
            throws ProcessorException {
        IProgressMonitor monitor = null;
        if (monitors == null) {
            monitor = new NullProgressMonitor();
        } else {
            monitor = monitors[0];
        }
        JobInfo jobInfo = new JobInfo(process, contextName, version);
        jobInfo.setApplyContextToChildren(applyContextToChildren);
        jobList.clear();
        esbJobs.clear();
        hasLoopDependency = false;
        IProcessor result = generateCode(jobInfo, contextName, statistics, trace, needContext, GENERATE_ALL_CHILDS, monitor);
        jobList.clear();
        hasLoopDependency = false;
        return result;
    }

    public static IProcessor generateCode(ProcessItem process, String contextName, String version,
            final Map<String, Object> argumentsMap, IProgressMonitor... monitors) throws ProcessorException {
        IProgressMonitor monitor = null;
        if (monitors == null) {
            monitor = new NullProgressMonitor();
        } else {
            monitor = monitors[0];
        }

        JobInfo jobInfo = new JobInfo(process, contextName, version);
        jobInfo.setApplyContextToChildren(
                ProcessUtils.isOptionChecked(argumentsMap, TalendProcessArgumentConstant.ARG_ENABLE_APPLY_CONTEXT_TO_CHILDREN));
        jobInfo.setArgumentsMap(argumentsMap);

        boolean statistics = ProcessUtils.isOptionChecked(argumentsMap, TalendProcessArgumentConstant.ARG_ENABLE_STATS);
        boolean trace = ProcessUtils.isOptionChecked(argumentsMap, TalendProcessArgumentConstant.ARG_ENABLE_TRACS);
        boolean needContext = ProcessUtils.isOptionChecked(argumentsMap, TalendProcessArgumentConstant.ARG_NEED_CONTEXT);
        int option = ProcessUtils.getOptionValue(argumentsMap, TalendProcessArgumentConstant.ARG_GENERATE_OPTION, 0);

        jobList.clear();
        esbJobs.clear();
        hasLoopDependency = false;
        IProcessor result = generateCode(jobInfo, contextName, statistics, trace, needContext, option, monitor);
        jobList.clear();
        hasLoopDependency = false;
        return result;
    }

    public static IProcessor generateCode(ProcessItem process, IContext context, String version, boolean statistics,
            boolean trace, boolean applyContextToChildren, IProgressMonitor... monitors) throws ProcessorException {
        IProgressMonitor monitor = null;
        if (monitors == null) {
            monitor = new NullProgressMonitor();
        } else {
            monitor = monitors[0];
        }
        IProcessor result = null;
        String contextName = context.getName();
        if (contextName != null) {
            JobInfo jobInfo = new JobInfo(process, contextName, version);
            jobInfo.setContext(context);
            jobInfo.setApplyContextToChildren(applyContextToChildren);
            jobList.clear();
            esbJobs.clear();
            hasLoopDependency = false;
            result = generateCode(jobInfo, contextName, statistics, trace, true, GENERATE_ALL_CHILDS, monitor);
            jobList.clear();
            hasLoopDependency = false;
        }
        return result;
    }

    public static IProcessor generateCode(ProcessItem process, String contextName, boolean statistics, boolean trace)
            throws ProcessorException {
        jobList.clear();
        esbJobs.clear();
        hasLoopDependency = false;
        IProcessor returnValue = generateCode(process, contextName, statistics, trace, false);
        jobList.clear();
        hasLoopDependency = false;
        return returnValue;
    }

    public static IProcessor generateCode(IProcess process, IContext context, boolean statistics, boolean trace,
            boolean contextProperties, boolean applyToChildren) throws ProcessorException {
        ISVNProviderService service = null;
        if (PluginChecker.isSVNProviderPluginLoaded()) {
            service = (ISVNProviderService) GlobalServiceRegister.getDefault().getService(ISVNProviderService.class);
        }
        if (service != null && service.isProjectInSvnMode()) {
            RepositoryManager.syncRoutineAndJoblet(ERepositoryObjectType.ROUTINES);
            RepositoryManager.syncRoutineAndJoblet(ERepositoryObjectType.PIG_UDF);
        }
        // achen modify to fix 0006107
        ProcessItem pItem = null;

        if (process instanceof IProcess2) {
            pItem = (ProcessItem) ((IProcess2) process).getProperty().getItem();
        }
        JobInfo jobInfo;
        if (pItem != null) { // ProcessItem is null for shadow process
            jobInfo = new JobInfo(pItem, context.getName());
            jobInfo.setProcess(process);
            jobInfo.setContext(context);
        } else {
            jobInfo = new JobInfo(process, context);
        }
        jobInfo.setApplyContextToChildren(applyToChildren);
        jobList.clear();
        esbJobs.clear();
        hasLoopDependency = false;
        IProcessor genCode = generateCode(jobInfo, context.getName(), statistics, trace, contextProperties, GENERATE_ALL_CHILDS,
                new NullProgressMonitor());
        jobList.clear();
        hasLoopDependency = false;
        return genCode;
    }

    public static IProcessor generateCode(IProcess process, IContext context, boolean statistics, boolean trace,
            boolean properties) throws ProcessorException {
        jobList.clear();
        esbJobs.clear();
        hasLoopDependency = false;
        IProcessor returnValue = generateCode(process, context, statistics, trace, properties, new NullProgressMonitor());
        jobList.clear();
        hasLoopDependency = false;
        return returnValue;
    }

    public static IProcessor generateCode(IProcess process, IContext context, boolean statistics, boolean trace,
            boolean properties, IProgressMonitor progressMonitor) throws ProcessorException {
        // added by nma, to refresh routines when generating code in SVN mode. 10225.
        ISVNProviderService service = null;
        if (PluginChecker.isSVNProviderPluginLoaded()) {
            service = (ISVNProviderService) GlobalServiceRegister.getDefault().getService(ISVNProviderService.class);
        }
        if (service != null && service.isProjectInSvnMode()) {
            RepositoryManager.syncRoutineAndJoblet(ERepositoryObjectType.ROUTINES);
            RepositoryManager.syncRoutineAndJoblet(ERepositoryObjectType.PIG_UDF);
        }
        // achen modify to fix 0006107
        ProcessItem pItem = null;

        if (process instanceof IProcess2) {
            pItem = (ProcessItem) ((IProcess2) process).getProperty().getItem();
        }
        JobInfo jobInfo;
        if (pItem != null) { // ProcessItem is null for shadow process
            jobInfo = new JobInfo(pItem, context.getName());
            jobInfo.setProcess(process);
            jobInfo.setContext(context);
        } else {
            jobInfo = new JobInfo(process, context);
        }
        jobList.clear();
        esbJobs.clear();
        hasLoopDependency = false;
        IProcessor genCode = generateCode(jobInfo, context.getName(), statistics, trace, properties, GENERATE_ALL_CHILDS,
                progressMonitor);
        jobList.clear();
        hasLoopDependency = false;
        return genCode;
    }

    public static IProcessor generateCode(IProcessor processor, IProcess process, IContext context, boolean statistics,
            boolean trace, boolean properties, IProgressMonitor progressMonitor) throws ProcessorException {

        ISVNProviderService service = null;
        if (PluginChecker.isSVNProviderPluginLoaded()) {
            service = (ISVNProviderService) GlobalServiceRegister.getDefault().getService(ISVNProviderService.class);
        }
        if (service != null && service.isProjectInSvnMode()) {
            RepositoryManager.syncRoutineAndJoblet(ERepositoryObjectType.ROUTINES);
            RepositoryManager.syncRoutineAndJoblet(ERepositoryObjectType.PIG_UDF);
        }

        CorePlugin.getDefault().getRunProcessService().buildCodesJavaProject(progressMonitor);

        // achen modify to fix 0006107
        ProcessItem pItem = null;

        if (process instanceof IProcess2) {
            Item item = ((IProcess2) process).getProperty().getItem();
            if (item instanceof ProcessItem) {
                pItem = (ProcessItem) ((IProcess2) process).getProperty().getItem();
            }
        }
        JobInfo jobInfo;
        if (pItem != null) { // ProcessItem is null for shadow process
            jobInfo = new JobInfo(pItem, context.getName());
            jobInfo.setProcess(process);
            jobInfo.setContext(context);
        } else {
            jobInfo = new JobInfo(process, context);
        }
        final boolean oldMeasureActived = TimeMeasure.measureActive;
        if (!oldMeasureActived) { // not active before.
            TimeMeasure.display = TimeMeasure.displaySteps = TimeMeasure.measureActive = CommonsPlugin.isDebugMode();
        }
        final String timeMeasureGenerateCodesId = "Generate job source codes for " //$NON-NLS-1$
                + (jobInfo.getJobName() != null ? jobInfo.getJobName() : jobInfo.getJobId());
        TimeMeasure.begin(timeMeasureGenerateCodesId);

        jobList.clear();
        esbJobs.clear();
        hasLoopDependency = false;
        IProcessor genCode = generateCode(processor, jobInfo, context.getName(), statistics, trace, properties,
                GENERATE_ALL_CHILDS, progressMonitor);
        jobList.clear();
        hasLoopDependency = false;

        TimeMeasure.end(timeMeasureGenerateCodesId);
        // if active before, not disable and active still.
        if (!oldMeasureActived) {
            TimeMeasure.display = TimeMeasure.displaySteps = TimeMeasure.measureActive = false;
        }

        return genCode;
    }

    public static IProcessor generateCode(IProcess process, IContext context, boolean statistics, boolean trace,
            boolean properties, int option) throws ProcessorException {
        // added by nma, to refresh routines when generating code in SVN mode. 10225.
        ISVNProviderService service = null;
        if (PluginChecker.isSVNProviderPluginLoaded()) {
            service = (ISVNProviderService) GlobalServiceRegister.getDefault().getService(ISVNProviderService.class);
        }
        if (service != null && service.isProjectInSvnMode()) {
            RepositoryManager.syncRoutineAndJoblet(ERepositoryObjectType.ROUTINES);
            RepositoryManager.syncRoutineAndJoblet(ERepositoryObjectType.PIG_UDF);
        }
        // achen modify to fix 0006107
        JobInfo jobInfo = new JobInfo(process, context);
        jobList.clear();
        esbJobs.clear();
        hasLoopDependency = false;
        IProcessor genCode = generateCode(jobInfo, context.getName(), statistics, trace, properties, option,
                new NullProgressMonitor());
        jobList.clear();
        hasLoopDependency = false;
        return genCode;
    }

    /**
     *
     * @deprecated seems never use this one
     */
    @Deprecated
    public static String[] getCommandLine(boolean externalUse, String processName, String contextName, int statisticPort,
            int tracePort, String... codeOptions) throws ProcessorException {
        return getCommandLine(null, externalUse, processName, contextName, statisticPort, tracePort, codeOptions);
    }

    /**
     *
     * @deprecated seems never use this one
     */
    @Deprecated
    public static String[] getCommandLine(boolean externalUse, String processName, String contextName, String version,
            int statisticPort, int tracePort, String... codeOptions) throws ProcessorException {
        return getCommandLine(null, externalUse, processName, contextName, version, statisticPort, tracePort, codeOptions);
    }

    /**
     *
     * jet code generator to get original classpath
     */
    public static String[] getCommandLine(String targetPlatform, boolean externalUse, String processId, String contextName,
            int statisticPort, int tracePort, String... codeOptions) throws ProcessorException {
        return getCommandLine(targetPlatform, true, externalUse, processId, contextName, statisticPort, tracePort, codeOptions);
    }

    /**
     *
     * jet code generator to especially for tRunJob to get classpath with classpath.jar
     */
    public static String[] getCommandLine(String targetPlatform, boolean skipClasspathJar, boolean externalUse, String processId,
            String contextName, int statisticPort, int tracePort, String... codeOptions) throws ProcessorException {

        IProcessor processor = findProcessorFromJobList(processId, contextName, null);
        if (processor != null && targetPlatform.equals(processor.getTargetPlatform())) {
            boolean oldSkipClasspathJar = processor.isSkipClasspathJar();
            processor.setSkipClasspathJar(skipClasspathJar);
            try {
                return processor.getCommandLine(true, externalUse, statisticPort, tracePort, codeOptions);
            } finally {
                processor.setSkipClasspathJar(oldSkipClasspathJar);
            }
        }

        ProcessItem selectedProcessItem = ItemCacheManager.getProcessItem(processId);
        if (selectedProcessItem == null) {
            return new String[] {};
        }
        IDesignerCoreService service = CorePlugin.getDefault().getDesignerCoreService();

        IProcess process = service.getProcessFromProcessItem(selectedProcessItem);
        if (process == null) {
            return new String[] {};
        }
        // because all jobs are based one new way, set the flag "oldBuildJob" to false.
        return getCommandLine(false, skipClasspathJar, targetPlatform, externalUse, process, selectedProcessItem.getProperty(),
                contextName, true, statisticPort, tracePort, codeOptions);
    }

    /**
     *
     * @deprecated seems never use this one
     */
    @Deprecated
    public static String[] getCommandLine(String targetPlatform, boolean externalUse, String processId, String contextName,
            String version, int statisticPort, int tracePort, String... codeOptions) throws ProcessorException {
        ProcessItem selectedProcessItem = ItemCacheManager.getProcessItem(processId, version);
        return getCommandLine(targetPlatform, externalUse, selectedProcessItem, contextName, true, statisticPort, tracePort,
                codeOptions);
    }

    /**
     *
     * @deprecated seems never use this one
     */
    @Deprecated
    public static String[] getCommandLine(String targetPlatform, boolean externalUse, ProcessItem processItem, String contextName,
            boolean needContext, int statisticPort, int tracePort, String... codeOptions) throws ProcessorException {
        IProcess currentProcess = null;
        IDesignerCoreService service = CorePlugin.getDefault().getDesignerCoreService();

        if (processItem == null) {
            return new String[] {};
        }
        currentProcess = service.getProcessFromProcessItem(processItem);

        return getCommandLine(targetPlatform, externalUse, currentProcess, processItem.getProperty(), contextName, needContext,
                statisticPort, tracePort, codeOptions);
    }

    /**
     *
     * @deprecated seems never use this one
     */
    @Deprecated
    public static String[] getCommandLine(String targetPlatform, boolean externalUse, IProcess currentProcess, String contextName,
            boolean needContext, int statisticPort, int tracePort, String... codeOptions) throws ProcessorException {
        Property curProperty = null;
        if (currentProcess instanceof IProcess2) {
            curProperty = ((IProcess2) currentProcess).getProperty();
        }
        return getCommandLine(targetPlatform, externalUse, currentProcess, curProperty, contextName, needContext, statisticPort,
                tracePort, codeOptions);
    }

    /**
     *
     * @deprecated seems never use this one
     */
    @Deprecated
    public static String[] getCommandLine(String targetPlatform, boolean externalUse, IProcess currentProcess, Property property,
            String contextName, boolean needContext, int statisticPort, int tracePort, String... codeOptions)
            throws ProcessorException {
        return getCommandLine(true, targetPlatform, externalUse, currentProcess, property, contextName, needContext,
                statisticPort, tracePort, codeOptions);
    }

    public static String[] getCommandLine(boolean oldBuildJob, String targetPlatform, boolean externalUse,
            IProcess currentProcess, Property property, String contextName, boolean needContext, int statisticPort, int tracePort,
            String... codeOptions) throws ProcessorException {
        return getCommandLine(oldBuildJob, false, targetPlatform, externalUse, currentProcess, property, contextName, needContext,
                statisticPort, tracePort, codeOptions);
    }

    public static String[] getCommandLine(boolean oldBuildJob, boolean skipClasspathJar, String targetPlatform,
            boolean externalUse, IProcess currentProcess, Property property, String contextName, boolean needContext,
            int statisticPort, int tracePort, String... codeOptions) throws ProcessorException {
        if (currentProcess == null) {
            return new String[] {};
        }
        Property curProperty = property;
        if (curProperty == null && currentProcess instanceof IProcess2) {
            curProperty = ((IProcess2) currentProcess).getProperty();
        }
        IContext currentContext = getContext(currentProcess, contextName);
        IProcessor processor = getProcessor(currentProcess, curProperty, currentContext);
        processor.setSkipClasspathJar(skipClasspathJar);
        processor.setTargetPlatform(targetPlatform);
        processor.setOldBuildJob(oldBuildJob);
        return processor.getCommandLine(needContext, externalUse, statisticPort, tracePort, codeOptions);
    }

    public static String[] getCommandLine(boolean oldBuildJob, String targetPlatform, boolean externalUse, IProcessor processor,
            Property property, String contextName, boolean needContext, int statisticPort, int tracePort, String... codeOptions)
            throws ProcessorException {
        processor.setTargetPlatform(targetPlatform);
        processor.setOldBuildJob(oldBuildJob);
        return processor.getCommandLine(needContext, externalUse, statisticPort, tracePort, codeOptions);
    }

    /**
     *
     * Seems only work for jet code generator.
     */
    public static String[] getMainCommand(String processName, String processVersion, String contextName, int statisticPort,
            int tracePort, String... codeOptions) throws ProcessorException {
        IProcessor processor = findProcessorFromJobList(processName, contextName, processVersion);
        IProcess currentProcess = null;
        Property property = null;
        if (processor == null) {
            ProcessItem selectedProcessItem = null;
            selectedProcessItem = ItemCacheManager.getProcessItem(processName, processVersion);
            if (selectedProcessItem != null) {
                IDesignerCoreService service = CorePlugin.getDefault().getDesignerCoreService();
                currentProcess = service.getProcessFromProcessItem(selectedProcessItem);
                property = selectedProcessItem.getProperty();
            }
            if (currentProcess == null) {
                return new String[] {};
            }

        } else if (processor.getMainClass() == null) {
            currentProcess = processor.getProcess();
            property = processor.getProperty();
        }
        if (currentProcess != null) {
            IContext currentContext = getContext(currentProcess, contextName);
            processor = getProcessor(currentProcess, property, currentContext);
        }

        String[] cmd = new String[] { processor.getMainClass() };
        if (codeOptions != null) {
            for (String string : codeOptions) {
                if (string != null) {
                    cmd = (String[]) ArrayUtils.add(cmd, string);
                }
            }
        }
        if (needContextInCurrentGeneration && contextName != null && !contextName.equals("")) {
            cmd = (String[]) ArrayUtils.add(cmd, TalendProcessArgumentConstant.CMD_ARG_CONTEXT_NAME + contextName);
        }
        if (statisticPort != -1) {
            cmd = (String[]) ArrayUtils.add(cmd, TalendProcessArgumentConstant.CMD_ARG_STATS_PORT + statisticPort);
        }
        if (tracePort != -1) {
            cmd = (String[]) ArrayUtils.add(cmd, TalendProcessArgumentConstant.CMD_ARG_TRACE_PORT + tracePort);
        }
        return cmd;
    }

    /**
     *
     * ggu Comment method "getAllVersionProcessList".
     *
     * @param processId
     * @return
     */
    public static List<IRepositoryViewObject> getAllVersionObjectById(String id) {
        if (id == null || "".equals(id)) { //$NON-NLS-1$
            return null;
        }
        IProxyRepositoryFactory factory = CorePlugin.getDefault().getProxyRepositoryFactory();
        try {
            final List<IRepositoryViewObject> allVersion = factory.getAllVersion(id);
            final IRepositoryViewObject lastVersion = factory.getLastVersion(id);
            if (lastVersion != null && factory.getStatus(lastVersion) != ERepositoryStatus.DELETED) {
                return allVersion;
            }
        } catch (PersistenceException e) {
            //
        }

        return null;
    }

    public static String getParameterValue(EList listParamType, String paramName) {
        for (int j = 0; j < listParamType.size(); j++) {
            ElementParameterType pType = (ElementParameterType) listParamType.get(j);
            if (pType != null && paramName.equals(pType.getName())) {
                return pType.getValue();
            }
        }
        return null;
    }

    // see bug 0004939: making tRunjobs work loop will cause a error of "out of memory" .
    private static Set<JobInfo> getAllJobInfo(ProcessType ptype, JobInfo parentJobInfo, Set<JobInfo> jobInfos,
            boolean firstChildOnly) {
        if (ptype == null) {
            return jobInfos;
        }
        // trunjob component
        EList<NodeType> nodes = ptype.getNode();
        String jobletPaletteType = null;
        String frameWork = ptype.getFramework();
        if (frameWork == null) {
            jobletPaletteType = ComponentCategory.CATEGORY_4_DI.getName();
        } else if (frameWork.equals(HadoopConstants.FRAMEWORK_SPARK)) {
            jobletPaletteType = ComponentCategory.CATEGORY_4_SPARK.getName();
        } else if (frameWork.equals(HadoopConstants.FRAMEWORK_SPARK_STREAMING)) {
            jobletPaletteType = ComponentCategory.CATEGORY_4_SPARKSTREAMING.getName();
        }
        for (NodeType node : nodes) {
            boolean activate = true;
            // check if node is active at least.
            for (Object o : node.getElementParameter()) {
                ElementParameterType param = (ElementParameterType) o;
                if ("ACTIVATE".equals(param.getName())) {
                    activate = "true".equals(param.getValue());
                    break;
                }
            }
            if (!activate) {
                continue;
            }

            if (isEsbComponentName(node.getComponentName())) {
                addEsbJob(parentJobInfo);
            }

            boolean isCTalendJob = "cTalendJob".equalsIgnoreCase(node.getComponentName());
            boolean isRoutelet = isRouteletNode(node);
            if ("tRunJob".equalsIgnoreCase(node.getComponentName()) || isCTalendJob || isRoutelet) { //$NON-NLS-1$

                String jobIds = getParameterValue(node.getElementParameter(),
                        isCTalendJob ? "SELECTED_JOB_NAME:PROCESS_TYPE_PROCESS" //$NON-NLS-1$
                                : "PROCESS" + (isRoutelet ? "_TYPE" : "") + ":PROCESS_TYPE_PROCESS");
                String jobContext = getParameterValue(node.getElementParameter(),
                        isCTalendJob ? "SELECTED_JOB_NAME:PROCESS_TYPE_CONTEXT" //$NON-NLS-1$
                                : "PROCESS" + (isRoutelet ? "_TYPE" : "") + ":PROCESS_TYPE_CONTEXT");
                String jobVersion = getParameterValue(node.getElementParameter(),
                        isCTalendJob ? "SELECTED_JOB_NAME:PROCESS_TYPE_VERSION" //$NON-NLS-1$
                                : "PROCESS" + (isRoutelet ? "_TYPE" : "") + ":PROCESS_TYPE_VERSION");
                // feature 19312
                String[] jobsArr = jobIds.split(ProcessorUtilities.COMMA);
                for (String jobId : jobsArr) {
                    if (StringUtils.isNotEmpty(jobId)) {
                        ProcessItem processItem = ItemCacheManager.getProcessItem(jobId, jobVersion);
                        if (processItem != null) {
                            JobInfo jobInfo = new JobInfo(processItem, jobContext);
                            if (!jobInfos.contains(jobInfo)) {
                                jobInfos.add(jobInfo);
                                jobInfo.setFatherJobInfo(parentJobInfo);
                                if (!firstChildOnly) {
                                    getAllJobInfo(processItem.getProcess(), jobInfo, jobInfos, firstChildOnly);
                                }
                            }
                        }
                    }
                }
            } else {
                // for joblet node
                if (jobletPaletteType != null && PluginChecker.isJobLetPluginLoaded()) {
                    IJobletProviderService service = (IJobletProviderService) GlobalServiceRegister.getDefault()
                            .getService(IJobletProviderService.class);
                    if (service != null) {
                        IComponent jobletComponent = service.getJobletComponent(node, jobletPaletteType);
                        ProcessType jobletProcess = service.getJobletProcess(jobletComponent);
                        if (jobletComponent != null) {
                            if (!firstChildOnly) {
                                getAllJobInfo(jobletProcess, parentJobInfo, jobInfos, firstChildOnly);
                            } else {
                                Project project = null;
                                String componentName = node.getComponentName();
                                String[] array = componentName.split(":"); //$NON-NLS-1$
                                if (array.length == 2) {
                                    // from ref project
                                    String projectTechName = array[0];
                                    project = ProjectManager.getInstance().getProjectFromProjectTechLabel(projectTechName);
                                } else {
                                    project = ProjectManager.getInstance().getCurrentProject();
                                }
                                Property property = service.getJobletComponentItem(jobletComponent);
                                Project currentProject = ProjectManager.getInstance().getCurrentProject();
                                if (project != null && !project.getTechnicalLabel().equals(currentProject.getTechnicalLabel())) {
                                    try {
                                        property = ProxyRepositoryFactory.getInstance()
                                                .getSpecificVersion(project, property.getId(), property.getVersion(), true)
                                                .getProperty();
                                    } catch (PersistenceException e) {
                                        ExceptionHandler.process(e);
                                    }
                                }
                                JobInfo jobInfo = new JobInfo(property, jobletProcess.getDefaultContext());
                                if (!jobInfos.contains(jobInfo)) {
                                    jobInfos.add(jobInfo);
                                    jobInfo.setFatherJobInfo(parentJobInfo);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!parentJobInfo.isTestContainer() && !parentJobInfo.isJoblet()
                && GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class)) {
            ITestContainerProviderService testContainerService = (ITestContainerProviderService) GlobalServiceRegister
                    .getDefault().getService(ITestContainerProviderService.class);
            if (testContainerService != null) {
                List<ProcessItem> testsItems = testContainerService.getTestContainersByVersion(parentJobInfo.getProcessItem());
                for (ProcessItem testItem : testsItems) {
                    ProcessType testProcess = testContainerService.getTestContainerProcess(testItem);
                    if (testItem.getProcess() == null) {
                        testItem.setProcess(testProcess);
                    }
                    if (testProcess == null) {
                        log.warn(Messages.getString("ProcessorUtilities.nullProcess")); //$NON-NLS-1$
                        continue;
                    }
                    JobInfo jobInfo = new JobInfo(testItem, testProcess.getDefaultContext());
                    jobInfo.setTestContainer(true);
                    jobInfos.add(jobInfo);
                    jobInfo.setFatherJobInfo(parentJobInfo);
                }
            }
        }
        return jobInfos;
    }

    private static boolean isRouteletNode(NodeType node) {
        String jobIds = getParameterValue(node.getElementParameter(), "PROCESS_TYPE:PROCESS_TYPE_PROCESS");
        String jobVersion = getParameterValue(node.getElementParameter(), "PROCESS_TYPE:PROCESS_TYPE_VERSION"); //$NON-NLS-1$
        ProcessItem processItem = ItemCacheManager.getProcessItem(jobIds, jobVersion);
        if (processItem != null) {
            return ERepositoryObjectType.getType(processItem.getProperty()).equals(ERepositoryObjectType.PROCESS_ROUTELET);
        }
        return false;
    }

    public static Set<JobInfo> getChildrenJobInfo(ProcessItem processItem) {
        return getChildrenJobInfo(processItem, false);
    }

    public static Set<JobInfo> getChildrenJobInfo(Item processItem, boolean firstChildOnly) {
        // delegate to the new method, prevent dead loop method call. see bug 0004939: making tRunjobs work loop will
        // cause a error of "out of memory" .
        JobInfo parentJobInfo = null;
        ProcessType processType = null;
        if (processItem instanceof ProcessItem) {
            parentJobInfo = new JobInfo((ProcessItem) processItem,
                    ((ProcessItem) processItem).getProcess().getDefaultContext());
            processType = ((ProcessItem) processItem).getProcess();
        } else {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IJobletProviderService.class)) {
                IJobletProviderService jobletService = (IJobletProviderService) GlobalServiceRegister.getDefault().getService(IJobletProviderService.class);
                if (jobletService.isJobletItem(processItem)) {
                    processType = jobletService.getJobletProcess(processItem);
                    parentJobInfo = new JobInfo(processItem.getProperty(), processType.getDefaultContext());
                }
            }
        }
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class)) {
            ITestContainerProviderService testContainerService = (ITestContainerProviderService) GlobalServiceRegister
                    .getDefault().getService(ITestContainerProviderService.class);
            if (testContainerService.isTestContainerItem(processItem)) {
                parentJobInfo.setTestContainer(true);
            }
        }
        if (parentJobInfo != null && processType != null) {
            return getAllJobInfo(processType, parentJobInfo, new HashSet<JobInfo>(), firstChildOnly);
        }
        return new HashSet<JobInfo>();
    }

    /**
     * DOC xtan. for bug:15299
     *
     * @param jobId
     * @return
     */
    public static boolean getLastGeneratedWithStats(String jobId) {
        // posible value is: null, 0, 1, 2, 3
        Integer previousInfos = lastGeneratedWithStatsOrTrace.get(jobId);

        if (previousInfos != null) {
            int flagStats = previousInfos & GENERATED_WITH_STATS;
            return flagStats != 0;
        }

        return false;
    }

    /**
     * DOC xtan. for bug:15299
     *
     * @param jobId
     * @return
     */
    public static boolean getLastGeneratedWithTrace(String jobId) {
        // posible value is: null, 0, 1, 2, 3
        Integer previousInfos = lastGeneratedWithStatsOrTrace.get(jobId);

        if (previousInfos != null) {
            int flagTraces = previousInfos & GENERATED_WITH_TRACES;
            return flagTraces != 0;
        }

        return false;
    }

    public static String generateCmdByTalendJob(String[] cmd) {
        StringBuffer sb = new StringBuffer();
        sb.append(""); //$NON-NLS-1$
        if (cmd != null && cmd.length > 0) {
            for (String s : cmd) {
                sb.append(s).append(' ');
            }
        }
        return sb.toString();
    }

    private static IProcessor findProcessorFromJobList(String processId, String contextName, String version) {
        for (JobInfo jobInfo : jobList) {
            if (jobInfo.getJobId().equals(processId)) {
                if (contextName != null && !contextName.equals("") && !jobInfo.getContextName().equals(contextName)) {
                    continue;
                }
                if (version != null && !version.equals(jobInfo.getJobVersion())) {
                    continue;
                }
                // job found from jobList;
                return jobInfo.getProcessor();
            }
        }
        return null;
    }

    public static File getJavaProjectLibFolder() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            IRunProcessService processService = (IRunProcessService) GlobalServiceRegister.getDefault()
                    .getService(IRunProcessService.class);
            return processService.getJavaProjectLibFolder().getLocation().toFile();
        }
        return null;
    }

    public static String getJavaProjectLibFolderPath() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            IRunProcessService processService = (IRunProcessService) GlobalServiceRegister.getDefault()
                    .getService(IRunProcessService.class);
            return processService.getJavaProjectLibFolder().getLocation().toPortableString();
        }
        return null;
    }

    public static boolean isExportAsOSGI() {
        return exportAsOSGI;
    }

    public static void setExportAsOSGI(boolean toOSGI) {
        exportAsOSGI = toOSGI;
    }

    public static boolean isExportJobAsMicroService() {
        return exportJobAsMicroService;
    }

    public static void setExportJobAsMicroSerivce(boolean toMicroService) {
        exportJobAsMicroService = toMicroService;
    }

    /**
     * Getter for hasLoopDependency.
     * 
     * @return the hasLoopDependency
     */
    public static boolean hasLoopDependency() {
        return hasLoopDependency;
    }

    /**
     * The dynamic loading of the hadoop configuration library is supported in DI, MapReduce and Spark (batch and
     * streaming).
     *
     * @param property the {@link Property} used to retrieve the {@link ComponentCategory}
     * @return true if the hadoop configuration can be loaded dynamically
     */
    private static boolean doSupportDynamicHadoopConfLoading(Property property) {
        if (property != null) {
            ComponentCategory itemCategory = ComponentCategory.getComponentCategoryFromItem(property.getItem());
            return ComponentCategory.CATEGORY_4_DI.equals(itemCategory)
                    || ComponentCategory.CATEGORY_4_MAPREDUCE.equals(itemCategory)
                    || ComponentCategory.CATEGORY_4_SPARK.equals(itemCategory)
                    || ComponentCategory.CATEGORY_4_SPARKSTREAMING.equals(itemCategory);
        } else {
            return false;
        }

    }

    /**
     * The dynamic loading of the hadoop configuration library is only supported if the job is not experted as OSGI and
     * in a restricted list of palettes.
     *
     * @param property the {@link Property} used to identify the palette
     * @return true if the hadoop configuration can be loaded dynamically
     */
    public static boolean hadoopConfJarCanBeLoadedDynamically(Property property) {
        return doSupportDynamicHadoopConfLoading(property) && !isExportAsOSGI();
    }


    public static boolean isEsbJob(String processId, String version) {
        return esbJobs.contains(esbJobKey(processId, version));
    }

    private static void addEsbJob(JobInfo jobInfo) {
        if (esbJobs.contains(esbJobKey(jobInfo.getJobId(), jobInfo.getJobVersion()))) {
            return;
         }

        esbJobs.add(esbJobKey(jobInfo.getJobId(), jobInfo.getJobVersion()));
        if (jobInfo.getFatherJobInfo() != null) {
            addEsbJob(jobInfo.getFatherJobInfo());
        }
    }

    private static String esbJobKey(String processId, String version) {
        return processId + "_" + version;
    }

    private static boolean isEsbComponentName(String componentName) {
        if (componentName.equals("tESBConsumer")
                || componentName.equals("tESBProviderRequest")
                || componentName.equals("tRouteInput")
                || componentName.equals("tESBProviderRequestIn")
                || componentName.equals("tESBProviderRequestLoop")) {
            return true;
        }
        return false;
    }

}
