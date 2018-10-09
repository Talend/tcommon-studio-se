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

import java.util.List;
import java.util.Set;

import org.apache.log4j.Level;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.talend.core.IService;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.ICodeProblemsChecker;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.process.JobInfo;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.core.runtime.projectsetting.ProjectPreferenceManager;

/**
 * DOC qian class global comment. Detailled comment <br/>
 *
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (星期五, 29 九月 2006) nrousseau $
 *
 */
public interface IRunProcessService extends IService {

    public ICodeProblemsChecker getSyntaxChecker(ECodeLanguage codeLanguage);

    public int getPauseTime();

    /**
     * Sets the activeProcess.
     *
     * @param activeContext the activeContext to set
     */
    public void setActiveProcess(IProcess2 activeProcess);

    public void setActiveProcess(IProcess2 activeProcess, boolean refreshUI);

    public void removeProcess(IProcess activeProcess);

    /**
     * Code Execution, used, when you know where the code stands.
     *
     * @param Perl Absolute Code Path
     * @param Context Name
     * @param Port Statistics
     * @param Port Trace
     * @return Command Process Launched
     * @throws ProcessorException
     */
    public int perlExec(StringBuffer out, StringBuffer err, IPath absCodePath, String contextName, Level level,
            String perlInterpreterLibOption, String perlModuleDirectoryOption, int statOption, int traceOption,
            String... codeOptions) throws ProcessorException;

    /**
     * DOC xue Comment method "createCodeProcessor".
     *
     * @param process
     * @param language
     * @param filenameFromLabel
     * @return
     */
    public IProcessor createCodeProcessor(IProcess process, Property property, ECodeLanguage language, boolean filenameFromLabel);

    /**
     * DOC qian Comment method "createPerformanceData".
     *
     * @param data IPerformanceData
     * @return
     */
    public IPerformanceData createPerformanceData(String data);

    /**
     * DOC qian Gets routine filename extension.
     *
     * @return
     */
    public String getRoutineFilenameExt();

    /**
     *
     * @deprecated getTalendProcessJavaProject instead.
     */
    @Deprecated
    public IProject getProject(ECodeLanguage language) throws CoreException;

    /**
     *
     * @deprecated getTalendProcessJavaProject instead.
     */
    @Deprecated
    public IJavaProject getJavaProject() throws CoreException;

    /**
     * Setter for a custom delegate service. This method should'nt be called from a delegate class, only in the
     * RunProcessService class.
     *
     * @param delegateService
     */
    public void setDelegateService(IRunProcessService delegateService);

    public void updateLibraries(Set<ModuleNeeded> jobModuleList, IProcess process);

    public void updateLibraries(Set<ModuleNeeded> jobModuleList, IProcess process, Set<ModuleNeeded> alreadyRetrievedModules)
            throws ProcessorException;

    public void updateLibraries(RoutineItem routineItem);

    public void refreshView();

    public void switchToCurProcessView();

    public boolean needDeleteAllJobs();

    public void deleteAllJobs(boolean fromPluginModel);

    public IAction getRunProcessAction();

    public IContext getSelectedContext();

    public boolean enableTraceForActiveRunProcess();

    public void saveJobBeforeRun(IProcess activeProcess);

    public IPreferenceStore getPreferenceStore();

    public IProcess getActiveProcess();

    public boolean checkExportProcess(IStructuredSelection selection, boolean isJob);

    public void checkLastGenerationHasCompilationError(boolean updateProblemsView) throws ProcessorException;

    /**
     * DOC ycbai Comment method "getResourceFilePath".
     *
     * get the absolute path of file which under resource folder by relative path.
     *
     * @param filePath the path relative resource folder.
     * @return
     */
    public String getResourceFilePath(String filePath);

    /**
     * DOC ycbai Comment method "getTemplateStrFromPreferenceStore".
     *
     * @param templateType
     * @return
     */
    public String getTemplateStrFromPreferenceStore(String templateType);

    /**
     * DOC ycbai Comment method "updateLogFiles".
     *
     * Create or update common-logging.properties and log4j.properties files under the project.
     *
     * @param project
     */
    public void updateLogFiles(ITalendProcessJavaProject talendJavaProject, boolean isLogForJob);

    public String getLogTemplate(String path);

    public boolean isJobRunning();

    @Deprecated
    public void buildJavaProject();

    public void buildCodesJavaProject(IProgressMonitor monitor);

    /**
     * @deprecated use {@link IRunProcessService#getTalendJobJavaProject(Property)} instead
     */
    @Deprecated
    ITalendProcessJavaProject getTalendProcessJavaProject();

    ProjectPreferenceManager getProjectPreferenceManager();

    Set<String> getLibJarsForBD(IProcess process);

    void updateProjectPomWithTemplate();

    void storeProjectPreferences(IPreferenceStore preferenceStore);

    public IFolder getJavaProjectLibFolder();

    void initMavenJavaProject(IProgressMonitor monitor, Project project);

    ITalendProcessJavaProject getTalendCodeJavaProject(ERepositoryObjectType type);

    ITalendProcessJavaProject getTalendCodeJavaProject(ERepositoryObjectType type, String projectTechName);

    ITalendProcessJavaProject getTalendJobJavaProject(Property property);

    ITalendProcessJavaProject getTempJavaProject();

    void clearProjectRelatedSettings();

    boolean isExportConfig();

    void generatePom(Item item);

    void generatePom(Item item, int option);

    void initializeRootPoms(IProgressMonitor monitor);

    boolean isGeneratePomOnly();

    public void handleJobDependencyLoop(JobInfo mainJobInfo, List<JobInfo> listJobs, IProgressMonitor progressMonitor)
            throws Exception;

}
