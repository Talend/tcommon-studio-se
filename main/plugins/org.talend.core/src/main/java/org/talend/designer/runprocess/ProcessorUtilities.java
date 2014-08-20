// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.ui.IEditorPart;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.time.TimeMeasure;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.PluginChecker;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.i18n.Messages;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.process.JobInfo;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryManager;
import org.talend.core.model.utils.JavaResourcesHelper;
import org.talend.core.model.utils.PerlResourcesHelper;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.ui.IJobletProviderService;
import org.talend.core.ui.ISVNProviderService;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.designer.core.ReplaceNodesInProcessProvider;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.repository.job.deletion.JobResource;
import org.talend.repository.job.deletion.JobResourceManager;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class ProcessorUtilities {

    public static final int GENERATE_MAIN_ONLY = 1;

    public static final int GENERATE_WITH_FIRST_CHILD = 2;

    public static final int GENERATE_ALL_CHILDS = 3;

    private static String interpreter, codeLocation, libraryPath;

    private static boolean exportConfig = false;

    private static List<IEditorPart> openedEditors = new ArrayList<IEditorPart>();

    private static boolean codeModified;

    private static boolean needContextInCurrentGeneration = true;

    private static IDesignerCoreService designerCoreService = (IDesignerCoreService) GlobalServiceRegister.getDefault()
            .getService(IDesignerCoreService.class);

    private static Map<String, Integer> lastGeneratedWithStatsOrTrace = new HashMap<String, Integer>();

    private static final int GENERATED_WITH_STATS = 1;

    private static final int GENERATED_WITH_TRACES = 2;

    private static final String COMMA = ";";

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
    }

    public static boolean isExportConfig() {
        return exportConfig;
    }

    public static void resetExportConfig() {
        interpreter = null;
        codeLocation = null;
        libraryPath = null;
        exportConfig = false;
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
        IProcessor processor = service.createCodeProcessor(process, curProperty, ((RepositoryContext) CorePlugin.getContext()
                .getProperty(Context.REPOSITORY_CONTEXT_KEY)).getProject().getLanguage(), true);
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

            Date modificationDate = process.getProperty().getModificationDate();
            Date originalDate = designerCoreService.getLastGeneratedJobsDateMap().get(jobInfo.getJobId());
            if (originalDate == null || modificationDate.compareTo(originalDate) != 0) {
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

    private static IProcessor generateCode(IProcessor processor2, JobInfo jobInfo, String selectedContextName,
            boolean statistics, boolean trace, boolean needContext, int option, IProgressMonitor progressMonitor)
            throws ProcessorException {
        needContextInCurrentGeneration = needContext;
        if (progressMonitor == null) {
            progressMonitor = new NullProgressMonitor();
        }
        if (progressMonitor.isCanceled()) {
            return null;
        }
        boolean isMainJob = false;
        if (jobInfo.getFatherJobInfo() == null) {
            isMainJob = true;
            codeModified = false;

            // this cache only keep the last main job's generation, so clear it since we regenerate a new job.
            LastGenerationInfo.getInstance().getLastGeneratedjobs().clear();

            // if it's the father, reset the processMap to ensure to have a good
            // code generation
            ItemCacheManager.clearCache();
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

        if (jobInfo.getJobVersion() != null) {
            selectedProcessItem = ItemCacheManager.getProcessItem(jobInfo.getJobId(), jobInfo.getJobVersion());
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

        if (currentProcess != null) {
            // TDI-26513:For the Dynamic schema,need to check the currentProcess(job or joblet)
            checkMetadataDynamic(currentProcess, jobInfo);
            jobInfo.setProcessItem(null);
        }
        Set<ModuleNeeded> neededLibraries = CorePlugin.getDefault().getDesignerCoreService()
                .getNeededLibrariesForProcess(currentProcess, false);
        if (neededLibraries != null) {
            LastGenerationInfo.getInstance().setModulesNeededWithSubjobPerJob(jobInfo.getJobId(), jobInfo.getJobVersion(),
                    neededLibraries);
            LastGenerationInfo.getInstance().setModulesNeededPerJob(jobInfo.getJobId(), jobInfo.getJobVersion(), neededLibraries);
        }
        resetRunJobComponentParameterForContextApply(jobInfo, currentProcess, selectedContextName);

        generateNodeInfo(jobInfo, selectedContextName, statistics, needContext, option, progressMonitor, currentProcess);

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

        // generate the code of the father after the childrens
        // so the code won't have any error during the check, and it will help to check
        // if the generation is really needed.
        generateContextInfo(jobInfo, selectedContextName, statistics, trace, needContext, progressMonitor, currentProcess,
                currentJobName, processor);

        // ADDED for TESB-7887 By GangLiu
        generateSpringInfo(jobInfo, selectedContextName, statistics, trace, needContext, progressMonitor, currentProcess,
                currentJobName, processor);

        generatePigudfInfor(jobInfo, selectedProcessItem, currentProcess, processor, neededLibraries);

        /*
         * Set classpath for current job. If current job include some child-jobs, the child job SHARE farther job
         * libraries.
         */
        jobInfo.setProcess(null);
        generateBuildInfo(jobInfo, progressMonitor, isMainJob, currentProcess, currentJobName, processor);
        return processor;
    }

    private static void generatePigudfInfor(JobInfo jobInfo, ProcessItem selectedProcessItem, IProcess currentProcess,
            IProcessor processor, Set<ModuleNeeded> neededLibraries) throws ProcessorException {
        // generate pigudf.jar before generate code
        // update calss path before export pigudf
        Set<String> jarList = new HashSet<String>();
        Set<ModuleNeeded> neededModules = LastGenerationInfo.getInstance().getModulesNeededWithSubjobPerJob(jobInfo.getJobId(),
                jobInfo.getJobVersion());
        for (ModuleNeeded module : neededModules) {
            jarList.add(module.getModuleName());
        }
        Set<String> pigudfNeededWithSubjobPerJob = LastGenerationInfo.getInstance().getPigudfNeededWithSubjobPerJob(
                jobInfo.getJobId(), jobInfo.getJobVersion());
        String pigModuleName = null;
        if (selectedProcessItem != null && !pigudfNeededWithSubjobPerJob.isEmpty()) {
            CorePlugin.getDefault().getRunProcessService().updateLibraries(jarList, currentProcess);
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
        if (exportConfig && !LastGenerationInfo.getInstance().isUseDynamic(jobInfo.getJobId(), jobInfo.getJobVersion())) {
            boolean hasDynamicMetadata = false;
            out: for (INode node : (List<? extends INode>) currentProcess.getGeneratingNodes()) {
                // to check if node is db component , maybe need modification
                boolean isDbNode = false;
                for (IElementParameter param : (List<? extends IElementParameter>) node.getElementParameters()) {
                    if ("TYPE".equals(param.getName()) && "TEXT".equals(param.getFieldType().getName())
                            && param.getValue() != null && !"".equals(param.getValue())) {
                        isDbNode = true;
                        break;
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
            LastGenerationInfo.getInstance().setUseDynamic(jobInfo.getJobId(), jobInfo.getJobVersion(), hasDynamicMetadata);
        }
    }

    private static void generateBuildInfo(JobInfo jobInfo, IProgressMonitor progressMonitor, boolean isMainJob,
            IProcess currentProcess, String currentJobName, IProcessor processor) throws ProcessorException {
        if (isMainJob) {
            progressMonitor.subTask(Messages.getString("ProcessorUtilities.finalizeBuild") + currentJobName); //$NON-NLS-1$
            Set<String> jarList = new HashSet<String>();
            Set<ModuleNeeded> neededModules = LastGenerationInfo.getInstance().getModulesNeededWithSubjobPerJob(
                    jobInfo.getJobId(), jobInfo.getJobVersion());
            for (ModuleNeeded module : neededModules) {
                jarList.add(module.getModuleName());
            }
            CorePlugin.getDefault().getRunProcessService().updateLibraries(jarList, currentProcess);
            if (codeModified) {
                try {
                    IProject project = CorePlugin.getDefault().getRunProcessService().getJavaProject().getProject();
                    if (!project.isSynchronized(IResource.DEPTH_INFINITE)) {
                        project.refreshLocal(IResource.DEPTH_INFINITE, progressMonitor);
                    }
                    project.build(IncrementalProjectBuilder.AUTO_BUILD, null);
                } catch (CoreException e) {
                    throw new ProcessorException(e);
                }
                processor.syntaxCheck();
            }
            needContextInCurrentGeneration = true;
            codeModified = false;
        }
    }

    /*
     * used to generate spring file for RouteBuilder ADDED for TESB-7887 By GangLiu
     */
    private static void generateSpringInfo(JobInfo jobInfo, String selectedContextName, boolean statistics, boolean trace,
            boolean needContext, IProgressMonitor progressMonitor, IProcess currentProcess, String currentJobName,
            IProcessor processor) throws ProcessorException {
        if (!(currentProcess instanceof IProcess2)) {
            return;
        }
        IProcess2 p = (IProcess2) currentProcess;
        if (!p.needsSpring()) {
            return;
        }
        String springContent = p.getSpringContent();
        if (springContent == null) {
            return;
        }
        processor.generateSpringContent();
    }

    private static void generateContextInfo(JobInfo jobInfo, String selectedContextName, boolean statistics, boolean trace,
            boolean needContext, IProgressMonitor progressMonitor, IProcess currentProcess, String currentJobName,
            IProcessor processor) throws ProcessorException {
        if (isCodeGenerationNeeded(jobInfo, statistics, trace)) {
            codeModified = true;
            if ((currentProcess instanceof IProcess2) && exportConfig) {
                // to force to regenerate the data nodes
                ((IProcess2) currentProcess).setProcessModified(true);
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
            processor.generateCode(statistics, trace, true, exportAsOSGI);
            if (currentProcess instanceof IProcess2 && ((IProcess2) currentProcess).getProperty() != null) {
                designerCoreService.getLastGeneratedJobsDateMap().put(currentProcess.getId(),
                        ((IProcess2) currentProcess).getProperty().getModificationDate());
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

    private static IProcessor generateCode(JobInfo jobInfo, String selectedContextName, boolean statistics, boolean trace,
            boolean needContext, int option, IProgressMonitor progressMonitor) throws ProcessorException {
        needContextInCurrentGeneration = needContext;
        TimeMeasure.display = CommonsPlugin.isDebugMode();
        TimeMeasure.displaySteps = CommonsPlugin.isDebugMode();
        TimeMeasure.measureActive = CommonsPlugin.isDebugMode();

        boolean timerStarted = false;
        String idTimer = "generateCode for job: <job not loaded yet>";
        if (jobInfo.getJobName() != null) {
            idTimer = "generateCode for job: " + jobInfo.getJobName();
            timerStarted = true;
            TimeMeasure.begin(idTimer);
        }
        try {
            if (progressMonitor == null) {
                progressMonitor = new NullProgressMonitor();
            }
            if (progressMonitor.isCanceled()) {
                return null;
            }
            boolean isMainJob = false;
            if (jobInfo.getFatherJobInfo() == null) {
                isMainJob = true;
                codeModified = false;

                // this cache only keep the last main job's generation, so clear it since we regenerate a new job.
                LastGenerationInfo.getInstance().getLastGeneratedjobs().clear();

                // if it's the father, reset the processMap to ensure to have a good
                // code generation
                ItemCacheManager.clearCache();
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

            if (jobInfo.getJobVersion() != null) {
                selectedProcessItem = ItemCacheManager.getProcessItem(jobInfo.getJobId(), jobInfo.getJobVersion());
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
            if (!timerStarted) {
                idTimer = "generateCode for job: " + currentProcess.getName();
                TimeMeasure.begin(idTimer);
            } else {
                TimeMeasure.step(idTimer, "Loading job");
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
            if (currentProcess != null) {
                checkMetadataDynamic(currentProcess, jobInfo);
                jobInfo.setProcessItem(null);
            }

            Set<ModuleNeeded> neededLibraries = CorePlugin.getDefault().getDesignerCoreService()
                    .getNeededLibrariesForProcess(currentProcess, false);
            if (neededLibraries != null) {
                LastGenerationInfo.getInstance().setModulesNeededWithSubjobPerJob(jobInfo.getJobId(), jobInfo.getJobVersion(),
                        neededLibraries);
                LastGenerationInfo.getInstance().setModulesNeededPerJob(jobInfo.getJobId(), jobInfo.getJobVersion(),
                        neededLibraries);
            }
            resetRunJobComponentParameterForContextApply(jobInfo, currentProcess, selectedContextName);

            generateNodeInfo(jobInfo, selectedContextName, statistics, needContext, option, progressMonitor, currentProcess);
            TimeMeasure.step(idTimer, "generateNodeInfo");

            IProcessor processor = null;
            if (selectedProcessItem == null) { // shadow process
                processor = getProcessor(currentProcess, null);
            } else {
                processor = getProcessor(currentProcess, selectedProcessItem.getProperty());
            }

            generateContextInfo(jobInfo, selectedContextName, statistics, trace, needContext, progressMonitor, currentProcess,
                    currentJobName, processor);

            // ADDED for TESB-7887 By GangLiu
            generateSpringInfo(jobInfo, selectedContextName, statistics, trace, needContext, progressMonitor, currentProcess,
                    currentJobName, processor);

            generatePigudfInfor(jobInfo, selectedProcessItem, currentProcess, processor, neededLibraries);

            TimeMeasure.step(idTimer, "generateContextInfo");

            /*
             * Set classpath for current job. If current job include some child-jobs, the child job SHARE farther job
             * libraries.
             */
            jobInfo.setProcess(null);
            generateBuildInfo(jobInfo, progressMonitor, isMainJob, currentProcess, currentJobName, processor);
            TimeMeasure.step(idTimer, "generateBuildInfo");
            return processor;
        } finally {
            TimeMeasure.end(idTimer);
            // don't set the value false here ,there will be TimeMeasure outside need to display
            // TimeMeasure.display = false;
            // TimeMeasure.displaySteps = false;
            // TimeMeasure.measureActive = false;
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
        JobInfo generatedJobInfo = new JobInfo(jobInfo.getJobId(), jobInfo.getContextName(), jobInfo.getJobVersion());
        generatedJobInfo.setJobName(currentProcess.getName());
        String projectFolderName;
        if (LanguageManager.getCurrentLanguage() == ECodeLanguage.JAVA) {
            projectFolderName = JavaResourcesHelper.getProjectFolderName(selectedProcessItem);
        } else {
            projectFolderName = PerlResourcesHelper.getRootProjectName(selectedProcessItem);
        }
        generatedJobInfo.setProjectFolderName(projectFolderName);
        LastGenerationInfo.getInstance().getLastGeneratedjobs().add(generatedJobInfo);
        if (isMainJob) {
            LastGenerationInfo.getInstance().setLastMainJob(generatedJobInfo);
        }
    }

    private static void generateNodeInfo(JobInfo jobInfo, String selectedContextName, boolean statistics, boolean properties,
            int option, IProgressMonitor progressMonitor, IProcess currentProcess) throws ProcessorException {
        if (option != GENERATE_MAIN_ONLY) {
            // handle subjob in joblet. see bug 004937: tRunJob in a Joblet
            List<? extends INode> graphicalNodes = currentProcess.getGeneratingNodes();
            for (INode node : graphicalNodes) {
                String componentName = node.getComponent().getName();
                if ((node != null) && (componentName.equals("tRunJob") || componentName.equals("cTalendJob"))) { //$NON-NLS-1$
                    // if the cTalendJob is configured by external Jar, then ignore it
                    if ("cTalendJob".equals(componentName)) {
                        IElementParameter fromExternal = node.getElementParameter("FROM_EXTERNAL_JAR");
                        Object value = fromExternal.getValue();
                        if (value != null && "true".equals(value.toString())) {
                            continue;
                        }
                    }

                    IElementParameter processIdparam = node.getElementParameter("PROCESS_TYPE_PROCESS"); //$NON-NLS-1$
                    // feature 19312
                    String jobIds = (String) processIdparam.getValue();
                    String[] jobsArr = jobIds.split(ProcessorUtilities.COMMA);
                    for (String jobId : jobsArr) {
                        if (StringUtils.isNotEmpty(jobId)) {

                            ProcessItem processItem = null;
                            String context = (String) node.getElementParameter("PROCESS_TYPE_CONTEXT").getValue(); //$NON-NLS-1$
                            String version = (String) node.getElementParameter("PROCESS_TYPE_VERSION").getValue(); //$NON-NLS-1$
                            JobInfo subJobInfo = null;
                            subJobInfo = new JobInfo(jobId, context, version);
                            // get processitem from job
                            processItem = ItemCacheManager.getProcessItem(jobId, version);

                            if (processItem == null) {
                                throw new ProcessorException(node.getUniqueName()
                                        + " not setup or child job not found in the job:" + currentProcess.getName());
                            }

                            subJobInfo.setJobVersion(processItem.getProperty().getVersion());

                            if (jobInfo.isApplyContextToChildren()) {
                                subJobInfo.setApplyContextToChildren(jobInfo.isApplyContextToChildren());
                                // see bug 0003862: Export job with the flag "Apply to children" if the child don't have
                                // the
                                // same context fails.
                                if (checkIfContextExisted(processItem, selectedContextName)) {
                                    subJobInfo.setContextName(selectedContextName);
                                } else {
                                    // use the default context of subjob
                                    String defaultContext = processItem.getProcess().getDefaultContext();
                                    node.getElementParameter("PROCESS_TYPE_CONTEXT").setValue(defaultContext); //$NON-NLS-1$
                                    subJobInfo.setContextName(defaultContext);
                                }
                            }
                            subJobInfo.setFatherJobInfo(jobInfo);
                            if (!jobList.contains(subJobInfo)) {
                                // children won't have stats / traces
                                if (option == GENERATE_WITH_FIRST_CHILD) {
                                    generateCode(subJobInfo, selectedContextName, statistics, false, properties,
                                            GENERATE_MAIN_ONLY, progressMonitor);
                                } else {
                                    generateCode(subJobInfo, selectedContextName, statistics, false, properties,
                                            GENERATE_ALL_CHILDS, progressMonitor);
                                    currentProcess.setNeedRegenerateCode(true);
                                }
                            }
                            LastGenerationInfo
                                    .getInstance()
                                    .getModulesNeededWithSubjobPerJob(jobInfo.getJobId(), jobInfo.getJobVersion())
                                    .addAll(LastGenerationInfo.getInstance().getModulesNeededWithSubjobPerJob(
                                            subJobInfo.getJobId(), subJobInfo.getJobVersion()));
                            LastGenerationInfo
                                    .getInstance()
                                    .getPigudfNeededWithSubjobPerJob(jobInfo.getJobId(), jobInfo.getJobVersion())
                                    .addAll(LastGenerationInfo.getInstance().getPigudfNeededWithSubjobPerJob(
                                            subJobInfo.getJobId(), subJobInfo.getJobVersion()));

                            if (exportAsOSGI) {
                                LastGenerationInfo
                                        .getInstance()
                                        .getModulesNeededWithSubjobPerJob(jobInfo.getJobId() + "-osgi", jobInfo.getJobVersion())
                                        .addAll(LastGenerationInfo.getInstance().getModulesNeededWithSubjobPerJob(
                                                subJobInfo.getJobId() + "-osgi", subJobInfo.getJobVersion()));
                                LastGenerationInfo
                                        .getInstance()
                                        .getPigudfNeededWithSubjobPerJob(jobInfo.getJobId() + "-osgi", jobInfo.getJobVersion())
                                        .addAll(LastGenerationInfo.getInstance().getPigudfNeededWithSubjobPerJob(
                                                subJobInfo.getJobId() + "-osgi", subJobInfo.getJobVersion()));
                            }

                            LastGenerationInfo
                                    .getInstance()
                                    .getRoutinesNeededWithSubjobPerJob(jobInfo.getJobId(), jobInfo.getJobVersion())
                                    .addAll(LastGenerationInfo.getInstance().getRoutinesNeededWithSubjobPerJob(
                                            subJobInfo.getJobId(), subJobInfo.getJobVersion()));

                            if (!LastGenerationInfo.getInstance().isUseDynamic(jobInfo.getJobId(), jobInfo.getJobVersion())) {
                                LastGenerationInfo.getInstance().setUseDynamic(
                                        jobInfo.getJobId(),
                                        jobInfo.getJobVersion(),
                                        LastGenerationInfo.getInstance().isUseDynamic(subJobInfo.getJobId(),
                                                subJobInfo.getJobVersion()));
                            }
                        }
                    }
                }
            }
        }
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

    static boolean exportAsOSGI = false;

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
        JobInfo jobInfo = new JobInfo(processName, contextName, version);
        IProcessor process = generateCode(jobInfo, contextName, statistics, trace, true, GENERATE_ALL_CHILDS, monitor);
        jobList.clear();
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
    public static IProcessor generateCode(String processId, String contextName, String version, boolean statistics,
            boolean trace, boolean applyContextToChildren, IProgressMonitor... monitors) throws ProcessorException {
        IProgressMonitor monitor = null;
        if (monitors == null) {
            monitor = new NullProgressMonitor();
        }
        JobInfo jobInfo = new JobInfo(processId, contextName, version);
        jobInfo.setApplyContextToChildren(applyContextToChildren);
        jobList.clear();
        IProcessor process = generateCode(jobInfo, contextName, statistics, trace, true, GENERATE_ALL_CHILDS, monitor);
        jobList.clear();
        return process;
    }

    public static IProcessor generateCode(ProcessItem process, String contextName, boolean statistics, boolean trace,
            boolean applyContextToChildren, IProgressMonitor... monitors) throws ProcessorException {
        IProgressMonitor monitor = null;
        if (monitors == null) {
            monitor = new NullProgressMonitor();
        }
        JobInfo jobInfo = new JobInfo(process, contextName);
        jobInfo.setApplyContextToChildren(applyContextToChildren);
        jobList.clear();
        IProcessor result = generateCode(jobInfo, contextName, statistics, trace, true, GENERATE_ALL_CHILDS, monitor);
        jobList.clear();
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
        IProcessor result = generateCode(jobInfo, contextName, statistics, trace, needContext, GENERATE_ALL_CHILDS, monitor);
        jobList.clear();
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
            result = generateCode(jobInfo, contextName, statistics, trace, true, GENERATE_ALL_CHILDS, monitor);
            jobList.clear();
        }
        return result;
    }

    public static IProcessor generateCode(ProcessItem process, String contextName, boolean statistics, boolean trace)
            throws ProcessorException {
        jobList.clear();
        IProcessor returnValue = generateCode(process, contextName, statistics, trace, false);
        jobList.clear();
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
        IProcessor genCode = generateCode(jobInfo, context.getName(), statistics, trace, contextProperties, GENERATE_ALL_CHILDS,
                new NullProgressMonitor());
        jobList.clear();
        return genCode;
    }

    public static IProcessor generateCode(IProcess process, IContext context, boolean statistics, boolean trace,
            boolean properties) throws ProcessorException {
        jobList.clear();
        IProcessor returnValue = generateCode(process, context, statistics, trace, properties, new NullProgressMonitor());
        jobList.clear();
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
        IProcessor genCode = generateCode(jobInfo, context.getName(), statistics, trace, properties, GENERATE_ALL_CHILDS,
                progressMonitor);
        jobList.clear();
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
        IProcessor genCode = generateCode(processor, jobInfo, context.getName(), statistics, trace, properties,
                GENERATE_ALL_CHILDS, progressMonitor);
        jobList.clear();
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
        IProcessor genCode = generateCode(jobInfo, context.getName(), statistics, trace, properties, option,
                new NullProgressMonitor());
        jobList.clear();
        return genCode;
    }

    /**
     * 
     * Get the command line to launch the job.
     * 
     * @param externalUse if true, will add "" around path and change \ to /
     * @param processName
     * @param contextName
     * @param codeOptions
     * @return
     * @throws ProcessorException
     */
    public static String[] getCommandLine(boolean externalUse, String processName, String contextName, int statisticPort,
            int tracePort, String... codeOptions) throws ProcessorException {
        return getCommandLine(null, externalUse, processName, contextName, statisticPort, tracePort, codeOptions);
    }

    public static String[] getCommandLine(boolean externalUse, String processName, String contextName, String version,
            int statisticPort, int tracePort, String... codeOptions) throws ProcessorException {
        return getCommandLine(null, externalUse, processName, contextName, version, statisticPort, tracePort, codeOptions);
    }

    /**
     * Get the command line to launch the job.
     * 
     * @param targetPlatform for example Platform.OS_WIN32 / Platform.OS_LINUX
     * @param externalUse
     * @param processName
     * @param contextName
     * @param statisticPort
     * @param tracePort
     * @param codeOptions
     * @return
     * @throws ProcessorException
     */
    public static String[] getCommandLine(String targetPlatform, boolean externalUse, String processId, String contextName,
            int statisticPort, int tracePort, String... codeOptions) throws ProcessorException {
        ProcessItem selectedProcessItem = ItemCacheManager.getProcessItem(processId);
        return getCommandLine(targetPlatform, externalUse, selectedProcessItem, contextName, true, statisticPort, tracePort,
                codeOptions);
    }

    public static String[] getCommandLine(String targetPlatform, boolean externalUse, String processId, String contextName,
            String version, int statisticPort, int tracePort, String... codeOptions) throws ProcessorException {
        ProcessItem selectedProcessItem = ItemCacheManager.getProcessItem(processId, version);
        return getCommandLine(targetPlatform, externalUse, selectedProcessItem, contextName, true, statisticPort, tracePort,
                codeOptions);
    }

    public static String[] getCommandLine(String targetPlatform, boolean externalUse, ProcessItem processItem,
            String contextName, boolean needContext, int statisticPort, int tracePort, String... codeOptions)
            throws ProcessorException {
        IProcess currentProcess = null;
        IDesignerCoreService service = CorePlugin.getDefault().getDesignerCoreService();

        if (processItem == null) {
            return new String[] {};
        }
        currentProcess = service.getProcessFromProcessItem(processItem);

        return getCommandLine(targetPlatform, externalUse, currentProcess, processItem.getProperty(), contextName, needContext,
                statisticPort, tracePort, codeOptions);
    }

    public static String[] getCommandLine(String targetPlatform, boolean externalUse, IProcess currentProcess,
            String contextName, boolean needContext, int statisticPort, int tracePort, String... codeOptions)
            throws ProcessorException {
        Property curProperty = null;
        if (currentProcess instanceof IProcess2) {
            curProperty = ((IProcess2) currentProcess).getProperty();
        }
        return getCommandLine(targetPlatform, externalUse, currentProcess, curProperty, contextName, needContext, statisticPort,
                tracePort, codeOptions);
    }

    public static String[] getCommandLine(String targetPlatform, boolean externalUse, IProcess currentProcess, Property property,
            String contextName, boolean needContext, int statisticPort, int tracePort, String... codeOptions)
            throws ProcessorException {
        if (currentProcess == null) {
            return new String[] {};
        }
        IContext currentContext = getContext(currentProcess, contextName);
        IProcessor processor = getProcessor(currentProcess, property, currentContext);
        processor.setTargetPlatform(targetPlatform);
        return processor.getCommandLine(needContext, externalUse, statisticPort, tracePort, codeOptions);
    }

    public static String[] getMainCommand(String processName, String processVersion, String contextName, int statisticPort,
            int tracePort, String... codeOptions) throws ProcessorException {
        IProcess currentProcess = null;
        ProcessItem selectedProcessItem = null;
        selectedProcessItem = ItemCacheManager.getProcessItem(processName, processVersion);
        if (selectedProcessItem != null) {
            IDesignerCoreService service = CorePlugin.getDefault().getDesignerCoreService();
            currentProcess = service.getProcessFromProcessItem(selectedProcessItem);
        }
        if (currentProcess == null) {
            return new String[] {};
        }
        IContext currentContext = getContext(currentProcess, contextName);
        IProcessor processor = getProcessor(currentProcess, selectedProcessItem.getProperty(), currentContext);
        String[] cmd = new String[] { processor.getCodePath().removeFirstSegments(1).toString().replace("/", ".") }; //$NON-NLS-1$ //$NON-NLS-2$
        if (codeOptions != null) {
            for (String string : codeOptions) {
                if (string != null) {
                    cmd = (String[]) ArrayUtils.add(cmd, string);
                }
            }
        }
        if (needContextInCurrentGeneration && contextName != null && !contextName.equals("")) {
            cmd = (String[]) ArrayUtils.add(cmd, "--context=" + contextName); //$NON-NLS-1$
        }
        if (statisticPort != -1) {
            cmd = (String[]) ArrayUtils.add(cmd, "--stat_port=" + statisticPort); //$NON-NLS-1$
        }
        if (tracePort != -1) {
            cmd = (String[]) ArrayUtils.add(cmd, "--trace_port=" + tracePort); //$NON-NLS-1$
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
    private static Set<JobInfo> getAllJobInfo(ProcessType ptype, Set<JobInfo> jobInfos) {
        if (ptype == null) {
            return jobInfos;
        }
        // trunjob component
        EList<NodeType> nodes = ptype.getNode();
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
            if ("tRunJob".equalsIgnoreCase(node.getComponentName())) { //$NON-NLS-1$
                String jobIds = getParameterValue(node.getElementParameter(), "PROCESS:PROCESS_TYPE_PROCESS"); //$NON-NLS-1$
                String jobContext = getParameterValue(node.getElementParameter(), "PROCESS:PROCESS_TYPE_CONTEXT"); //$NON-NLS-1$
                String jobVersion = getParameterValue(node.getElementParameter(), "PROCESS:PROCESS_TYPE_VERSION"); //$NON-NLS-1$
                // feature 19312
                String[] jobsArr = jobIds.split(ProcessorUtilities.COMMA);
                for (String jobId : jobsArr) {
                    if (StringUtils.isNotEmpty(jobId)) {
                        ProcessItem processItem = ItemCacheManager.getProcessItem(jobId, jobVersion);
                        if (processItem != null) {
                            JobInfo jobInfo = new JobInfo(processItem, jobContext);
                            if (!jobInfos.contains(jobInfo)) {
                                jobInfos.add(jobInfo);

                                getAllJobInfo(processItem.getProcess(), jobInfos);

                            }
                        }
                    }
                }
            } else {
                // for joblet node
                if (PluginChecker.isJobLetPluginLoaded()) {
                    IJobletProviderService service = (IJobletProviderService) GlobalServiceRegister.getDefault().getService(
                            IJobletProviderService.class);
                    if (service != null) {
                        ProcessType jobletProcess = service.getJobletProcess(node);
                        if (jobletProcess != null) {
                            getAllJobInfo(jobletProcess, jobInfos);
                        }
                    }
                }
            }
        }
        return jobInfos;
    }

    public static Set<JobInfo> getChildrenJobInfo(ProcessItem processItem) {
        // delegate to the new method, prevent dead loop method call. see bug 0004939: making tRunjobs work loop will
        // cause a error of "out of memory" .
        return getAllJobInfo(processItem.getProcess(), new HashSet<JobInfo>());

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
        String commandStr = CorePlugin.getDefault().getPreferenceStore().getString(ITalendCorePrefConstants.COMMAND_STR);
        String finalCommand = commandStr.replace(ITalendCorePrefConstants.DEFAULT_COMMAND_STR, sb.toString());
        return finalCommand;
    }

    /**
     * Getter for exportAsOSGI.
     * 
     * @return the exportAsOSGI
     */
    public static boolean isExportAsOSGI() {
        return exportAsOSGI;
    }

    /**
     * Sets the exportAsOSGI.
     * 
     * @param exportAsOSGI the exportAsOSGI to set
     */
    public static void setExportAsOSGI(boolean exportAsOSGI) {
        ProcessorUtilities.exportAsOSGI = exportAsOSGI;
    }

}
