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
package org.talend.designer.maven.launch;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.RefreshUtil;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.VMRunnerConfiguration;
import org.eclipse.m2e.internal.launch.MavenLaunchDelegate;
import org.eclipse.m2e.internal.launch.MavenRuntimeLaunchSupport;
import org.eclipse.osgi.util.NLS;
import org.talend.commons.CommonsPlugin;

/**
 * created by ycbai on 2015年5月27日 Detailled comment
 *
 */
@SuppressWarnings("restriction")
public class TalendMavenLaunchDelegate extends MavenLaunchDelegate {

    static final Logger log = Logger.getLogger(TalendMavenLaunchDelegate.class);

    /*
     * FIXME, enable refresh in main thread, so set false for bug TUP-2987.
     */
    public static final boolean FLAG_REFRESH_BACKGROUND = false;

    @Override
    public String getProgramArguments(ILaunchConfiguration configuration) throws CoreException {
        String programArguments = super.getProgramArguments(configuration);
        String arguments = configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, ""); //$NON-NLS-1$
        if (StringUtils.isNotEmpty(arguments)) {
            programArguments += " " + arguments; //$NON-NLS-1$
        }

        return programArguments;
    }

    public IVMRunner getVMRunner(final ILaunchConfiguration configuration, String mode) throws CoreException {
        if (FLAG_REFRESH_BACKGROUND) {
            return super.getVMRunner(configuration, mode);
        } else {
            /*
             * copied from AbstractJavaLaunchConfigurationDelegate.getVMRunner
             */
            IVMInstall vm = verifyVMInstall(configuration);
            final IVMRunner runner = vm.getVMRunner(mode);
            if (runner == null) {
                abort(NLS.bind(
                        org.eclipse.jdt.internal.launching.LaunchingMessages.JavaLocalApplicationLaunchConfigurationDelegate_0,
                        new String[] { vm.getName(), mode }), null,
                        IJavaLaunchConfigurationConstants.ERR_VM_RUNNER_DOES_NOT_EXIST);
            }
            /*
             * copied from MavenRuntimeLaunchSupport.decorateVMRunner
             */
            return new IVMRunner() {

                public void run(VMRunnerConfiguration runnerConfiguration, ILaunch launch, IProgressMonitor monitor)
                        throws CoreException {
                    runner.run(runnerConfiguration, launch, monitor);

                    IProcess[] processes = launch.getProcesses();
                    if (processes != null && processes.length > 0) {
                        ILaunchConfiguration configuration = launch.getLaunchConfiguration();
                        /*
                         * FIXME, 1, do refresh in main thread(Foreground). 2, refresh without GUI(Debug UI, Workbench
                         * or such.)
                         */

                        // BackgroundResourceRefresher refresher = new BackgroundResourceRefresher(configuration,
                        // launch);
                        // refresher.init();

                        ForegroundResourceRefresher refresher = new ForegroundResourceRefresher(configuration, launch);
                        refresher.init();
                    } else {
                        MavenRuntimeLaunchSupport.removeTempFiles(launch);
                    }
                }
            };
        }
    }
}

/**
 * 
 * DOC ggu class global comment. Detailled comment
 */
class ForegroundResourceRefresher implements IDebugEventSetListener {

    final ILaunchConfiguration configuration;

    final IProcess process;

    final ILaunch launch;

    public ForegroundResourceRefresher(ILaunchConfiguration configuration, ILaunch launch) {
        this.configuration = configuration;
        this.process = launch.getProcesses()[0];
        this.launch = launch;
    }

    /**
     * If the process has already terminated, resource refreshing is done immediately in the current thread. Otherwise,
     * refreshing is done when the process terminates.
     */
    public void init() {
        synchronized (process) {
            if (process.isTerminated()) {
                processResources();
            } else {
                DebugPlugin.getDefault().addDebugEventListener(this);
            }
        }
    }

    public void handleDebugEvents(DebugEvent[] events) {
        for (int i = 0; i < events.length; i++) {
            DebugEvent event = events[i];
            if (event.getSource() == process && event.getKind() == DebugEvent.TERMINATE) {
                DebugPlugin.getDefault().removeDebugEventListener(this);
                processResources();
                break;
            }
        }
    }

    @SuppressWarnings("restriction")
    protected void processResources() {
        IProgressMonitor monitor = new NullProgressMonitor();

        MavenRuntimeLaunchSupport.removeTempFiles(launch);

        if (CommonsPlugin.isHeadless()) { // no used for commandline to refresh.
            return;
        }
        try {

            /*
             * FIXME, replace to use non-UI API, and refresh the project directly.
             */

            // RefreshTab.refreshResources(configuration, monitor);

            boolean refreshed = false;
            String projectName = configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, (String) null);
            String refreshScope = configuration.getAttribute(RefreshUtil.ATTR_REFRESH_SCOPE, (String) null);
            // refresh project
            if (projectName != null && refreshScope != null && RefreshUtil.MEMENTO_SELECTED_PROJECT.equals(refreshScope)) {
                IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
                if (project.exists()) {
                    int depth = RefreshUtil.isRefreshRecursive(configuration) ? IResource.DEPTH_INFINITE : IResource.DEPTH_ONE;
                    RefreshUtil.refreshResources(new IResource[] { project }, depth, monitor);
                    refreshed = true;
                }
            }
            if (!refreshed) {
                // will call the "${selected_resource_path}" with SelectedResourceResolver(SelectedResourceManager) for
                // DebugUIPlugin still.
                RefreshUtil.refreshResources(configuration, monitor);
            }
        } catch (CoreException e) {
            TalendMavenLaunchDelegate.log.error(e.getMessage(), e);
        }

    }
}
