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
package org.talend.designer.maven.launch;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.Launch;
import org.eclipse.debug.core.RefreshUtil;
import org.eclipse.debug.core.model.IPersistableSourceLocator;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.RuntimeProcess;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.m2e.actions.MavenLaunchConstants;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.internal.IMavenConstants;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.project.IMavenProjectRegistry;
import org.eclipse.m2e.core.project.ResolverConfiguration;
import org.eclipse.m2e.internal.launch.MavenLaunchDelegate;
import org.eclipse.m2e.internal.launch.MavenLaunchUtils;
import org.eclipse.osgi.util.NLS;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.runtime.debug.TalendDebugHandler;
import org.talend.commons.ui.runtime.CommonUIPlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.core.runtime.process.TalendProcessArgumentConstant;
import org.talend.designer.runprocess.IRunProcessService;

/**
 * DOC ggu class global comment. Detailled comment
 * 
 * most codes are copied from @see ExecutePomAction. just in order to set the debug is in foreground.
 */
@SuppressWarnings("restriction")
public class MavenCommandLauncher {

    private String mavenArgs = "mavenArgs"; //$NON-NLS-1$

    private final String goals;

    /*
     * Use the M2E API to launch the maven with goal, run mode by default.
     */
    private String launcherMode = ILaunchManager.RUN_MODE;

    /*
     * By default for Launch Configuration , will capture the output, and try to open Console view to show maven logs.
     * 
     * Here, false, by default, don't cpture output. maybe later can add this option in the preference to enable show
     * the logs in Console View.
     */
    private boolean captureOutputInConsoleView = false;

    private boolean debugOutput;

    /*
     * Won't skip test by default.
     */
    private boolean skipTests = false;

    private Map<String, Object> argumentsMap;

    public MavenCommandLauncher(String goals) {
        super();
        Assert.isNotNull(goals);
        this.goals = goals;
        // by default same as preference settings.
        this.debugOutput = MavenPlugin.getMavenConfiguration().isDebugOutput();
    }

    protected String getGoals() {
        return goals;
    }

    public void setDebugOutput(boolean debugOutput) {
        this.debugOutput = debugOutput;
    }

    public void setCaptureOutputInConsoleView(boolean captureOutputInConsoleView) {
        this.captureOutputInConsoleView = captureOutputInConsoleView;
    }

    protected boolean isCaptureOutputInConsoleView() {
        if (CommonUIPlugin.isFullyHeadless()) { // headless will be false always.
            return false;
        }
        // when debug, capture always.
        if (TalendDebugHandler.isEclipseDebug() || CommonsPlugin.isDebugMode()) {
            return true;
        }
        return captureOutputInConsoleView;
    }

    public void setSkipTests(boolean skipTests) {
        this.skipTests = skipTests;
    }

    public void setArgumentsMap(Map<String, Object> argumentsMap) {
        this.argumentsMap = argumentsMap;
    }

    protected Object getArgumentMapValue(String key) {
        if (this.argumentsMap != null) {
            return this.argumentsMap.get(key);
        }
        return null;
    }

    protected String getArgumentValue(String key) {
        return (String) getArgumentMapValue(key);
    }

    protected ILaunchConfiguration createLaunchConfiguration(IContainer basedir, String goal) {
        try {
            ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
            ILaunchConfigurationType launchConfigurationType = launchManager
                    .getLaunchConfigurationType(MavenLaunchConstants.LAUNCH_CONFIGURATION_TYPE_ID);

            String launchSafeGoalName = goal.replace(':', '-');

            ILaunchConfigurationWorkingCopy workingCopy = launchConfigurationType.newInstance(null, //
                    NLS.bind(org.eclipse.m2e.internal.launch.Messages.ExecutePomAction_executing, launchSafeGoalName,
                            basedir.getLocation().toString().replace('/', '-')));
            workingCopy.setAttribute(MavenLaunchConstants.ATTR_POM_DIR, basedir.getLocation().toOSString());
            workingCopy.setAttribute(MavenLaunchConstants.ATTR_GOALS, goal);
            workingCopy.setAttribute(ILaunchManager.ATTR_PRIVATE, true);
            workingCopy.setAttribute(RefreshUtil.ATTR_REFRESH_SCOPE, RefreshUtil.MEMENTO_SELECTED_PROJECT);
            workingCopy.setAttribute(RefreshUtil.ATTR_REFRESH_RECURSIVE, true);
            if (CommonsPlugin.isHeadless() && !CommonsPlugin.isESBMicorservice()) {
                workingCopy.setAttribute(MavenLaunchConstants.ATTR_OFFLINE, true);
            }

            // seems no need refresh project, so won't set it.
            // workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
            // basedir.getProject().getName());

            // --------------Special settings for Talend----------
            if (isCaptureOutputInConsoleView()) {
                // by default will catch the output in console. so set null
                workingCopy.setAttribute(DebugPlugin.ATTR_CAPTURE_OUTPUT, (String) null);
            } else {
                workingCopy.setAttribute(DebugPlugin.ATTR_CAPTURE_OUTPUT, Boolean.FALSE.toString());
            }

            // not same, set it. Else use the preference directly.
            if (debugOutput != MavenPlugin.getMavenConfiguration().isDebugOutput()) {
                workingCopy.setAttribute(MavenLaunchConstants.ATTR_DEBUG_OUTPUT, this.debugOutput); // -X -e
            }

            // -Dmaven.test.skip=true -DskipTests
            workingCopy.setAttribute(MavenLaunchConstants.ATTR_SKIP_TESTS, this.skipTests);

            // ------------------------

            setProjectConfiguration(workingCopy, basedir);

            IPath path = getJREContainerPath(basedir);
            if (path != null) {
                workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_JRE_CONTAINER_PATH, path.toPortableString());
            }

            String vmargs = System.getProperty(mavenArgs);
            if (vmargs != null) {
                workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, vmargs);
            }

            String programArgs = getArgumentValue(TalendProcessArgumentConstant.ARG_PROGRAM_ARGUMENTS);
            if (StringUtils.isNotEmpty(programArgs)) {
                workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, programArgs);
            }
            IPath generatedLog = basedir.getProject().getLocation().append("lastGenerated.log"); //$NON-NLS-1$
            workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
                    "-l " + MavenLaunchUtils.quote(generatedLog.toPortableString()) + " " //$NON-NLS-1$ //$NON-NLS-2$
                            + workingCopy.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, "")); //$NON-NLS-1$

            // TODO when launching Maven with debugger consider to add the following property
            // -Dmaven.surefire.debug="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000 -Xnoagent
            // -Djava.compiler=NONE"

            return workingCopy;
        } catch (CoreException ex) {
            ExceptionHandler.process(ex);
        }
        return null;
    }

    protected void setProjectConfiguration(ILaunchConfigurationWorkingCopy workingCopy, IContainer basedir) {
        IMavenProjectRegistry projectManager = MavenPlugin.getMavenProjectRegistry();
        IFile pomFile = basedir.getFile(new Path(IMavenConstants.POM_FILE_NAME));
        IMavenProjectFacade projectFacade = projectManager.create(pomFile, false, new NullProgressMonitor());
        if (projectFacade != null) {
            ResolverConfiguration configuration = projectFacade.getResolverConfiguration();

            String selectedProfiles = configuration.getSelectedProfiles();
            if (selectedProfiles != null && selectedProfiles.length() > 0) {
                workingCopy.setAttribute(MavenLaunchConstants.ATTR_PROFILES, selectedProfiles);
            }
        }
    }

    // TODO ideally it should use MavenProject, but it is faster to scan IJavaProjects
    protected IPath getJREContainerPath(IContainer basedir) throws CoreException {
        IProject project = basedir.getProject();
        if (project != null && project.hasNature(JavaCore.NATURE_ID)) {
            IJavaProject javaProject = JavaCore.create(project);
            IClasspathEntry[] entries = javaProject.getRawClasspath();
            for (IClasspathEntry entry : entries) {
                if (JavaRuntime.JRE_CONTAINER.equals(entry.getPath().segment(0))) {
                    return entry.getPath();
                }
            }
        }
        return null;
    }

    protected ILaunchConfiguration createLaunchConfiguration() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            IRunProcessService processService = (IRunProcessService) GlobalServiceRegister.getDefault()
                    .getService(IRunProcessService.class);
            ITalendProcessJavaProject talendProcessJavaProject = processService.getTalendProcessJavaProject();
            if (talendProcessJavaProject != null) {
                IProject project = talendProcessJavaProject.getProject();
                return createLaunchConfiguration(project, goals);
            }
        }
        return null;
    }

    public void execute(IProgressMonitor monitor) throws Exception {

        /*
         * use the ExecutePomAction directly.
         */
        // ExecutePomAction exePomAction = new ExecutePomAction();
        // exePomAction.setInitializationData(null, null, MavenConstants.GOAL_COMPILE);
        // exePomAction.launch(new StructuredSelection(launcherPomFile), ILaunchManager.RUN_MODE);

        /*
         * use launch way
         */

        // try{

        ILaunchConfiguration launchConfiguration = createLaunchConfiguration();
        if (launchConfiguration == null) {
            throw new Exception("Can't create maven command launcher.");
        }
        // if (launchConfiguration instanceof ILaunchConfigurationWorkingCopy) {
        // ILaunchConfigurationWorkingCopy copiedConfig = (ILaunchConfigurationWorkingCopy) launchConfiguration;
        // }
        TalendLauncherWaiter talendWaiter = new TalendLauncherWaiter(launchConfiguration, monitor);

        final ILaunch launch = buildAndLaunch(launchConfiguration, launcherMode, monitor);
        talendWaiter.waitFinish(launch);
        StringBuffer errors = new StringBuffer();
        for (IProcess process : launch.getProcesses()) {
            String log = process.getStreamsProxy().getOutputStreamMonitor().getContents();

            if (!isCaptureOutputInConsoleView()) {
                // specially for commandline. if studio, when debug model, will log it in console view, so no need this.
                TalendDebugHandler.debug("\n------------------ Talend Maven Launcher log START -----------------------\n");
                TalendDebugHandler.debug(log);
                TalendDebugHandler.debug("\n------------------ Talend Maven Launcher log END -----------------------\n");
            }
            for (String line : log.split("\n")) { //$NON-NLS-1$
                if (line.startsWith("[ERROR]")) { //$NON-NLS-1$
                    errors.append(line + "\n"); //$NON-NLS-1$
                }
            }
        }

        // }finally{
        // if (launch != null) {
        // if remove, after execute launch, will remove the console also. so shouldn't remove it.
        // DebugPlugin.getDefault().getLaunchManager().removeLaunch(launch);
        // }
        // }
    }

    protected ILaunch buildAndLaunch(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor)
            throws CoreException {
        monitor.beginTask("", 1); //$NON-NLS-1$
        try {
            MavenLaunchDelegate mvld = new TalendMavenLaunchDelegate();
            ILaunch launch = new Launch(configuration, mode, null);
            ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
            launch.setAttribute(DebugPlugin.ATTR_LAUNCH_TIMESTAMP, Long.toString(System.currentTimeMillis()));
            launch.setAttribute(DebugPlugin.ATTR_CONSOLE_ENCODING, launchManager.getEncoding(configuration));
            if (isCaptureOutputInConsoleView()) {
                launchManager.addLaunch(launch);
            }

            String type = "org.eclipse.m2e.launching.MavenSourceLocator"; //$NON-NLS-1$
            IPersistableSourceLocator locator = launchManager.newSourceLocator(type);
            locator.initializeDefaults(configuration);
            launch.setSourceLocator(locator);
            mvld.launch(configuration, mode, launch, monitor);
            return launch;
        } finally {
            monitor.done();
        }
    }

    /**
     * 
     * created by ggu on 16 Mar 2015 Detailled comment
     *
     */
    static class TalendLauncherWaiter implements IDebugEventSetListener {

        private ILaunchConfiguration launchConfig;

        private boolean launchFinished = false;

        private IProgressMonitor monitor;
        
        public TalendLauncherWaiter(ILaunchConfiguration launchConfig, IProgressMonitor monitor) {
            super();
            this.launchConfig = launchConfig;
            this.monitor = monitor;
            DebugPlugin.getDefault().addDebugEventListener(this);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.debug.core.IDebugEventSetListener#handleDebugEvents(org.eclipse.debug.core.DebugEvent[])
         */
        @Override
        public void handleDebugEvents(DebugEvent[] events) {
            for (DebugEvent event : events) {
                Object source = event.getSource();
                if (source instanceof RuntimeProcess
                        && ((RuntimeProcess) source).getLaunch().getLaunchConfiguration() == launchConfig
                        && event.getKind() == DebugEvent.TERMINATE) {
                    DebugPlugin.getDefault().removeDebugEventListener(this);
                    launchFinished = true;
                    break;
                }
            }

        }

        public void waitFinish(ILaunch launch) {

            try {
                while (!launchFinished) {
                    Thread.sleep(100);
                    // if terminated also
                    if (launch.getProcesses() != null && launch.getProcesses().length > 0) {
                        if (launch.getProcesses()[0].isTerminated()) {
                            break;
                        }
                    }
                    if (monitor != null && monitor.isCanceled()) {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                ExceptionHandler.process(e);
            }
        }
    }
}
