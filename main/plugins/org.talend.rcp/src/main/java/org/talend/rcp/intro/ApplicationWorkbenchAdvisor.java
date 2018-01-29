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
package org.talend.rcp.intro;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.internal.ide.application.IDEWorkbenchAdvisor;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.utils.system.EclipseCommandLine;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.repository.utils.LoginTaskRegistryReader;
import org.talend.core.ui.branding.IBrandingConfiguration;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.designer.codegen.CodeGeneratorActivator;
import org.talend.designer.runprocess.RunProcessPlugin;
import org.talend.login.ILoginTask;
import org.talend.rcp.TalendSplashHandler;
import org.talend.registration.register.RegisterManagement;

/**
 * DOC ccarbone class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ApplicationWorkbenchAdvisor extends IDEWorkbenchAdvisor {

    private static Logger log = Logger.getLogger(ApplicationWorkbenchAdvisor.class);

    /*
     * @Override public void preStartup() { WorkbenchAdapterBuilder.registerAdapters(); super.preStartup(); }
     */

    private static final String PERSPECTIVE_ID = "org.talend.rcp.perspective"; //$NON-NLS-1$

    @Override
    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

    @Override
    public void initialize(IWorkbenchConfigurer configurer) {
        super.initialize(configurer);
        configurer.setSaveAndRestore(false);
        TrayDialog.setDialogHelpAvailable(false);

        PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.CLOSE_EDITORS_ON_EXIT, true);
        PlatformUI.getPreferenceStore().setDefault(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS, false);
        PlatformUI.getPreferenceStore().setDefault(IWorkbenchPreferenceConstants.DOCK_PERSPECTIVE_BAR,
                IWorkbenchPreferenceConstants.TOP_RIGHT);
    }

    @Override
    public String getInitialWindowPerspectiveId() {
        IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                IBrandingService.class);
        if (brandingService != null) {
            IBrandingConfiguration brandingConfiguration = brandingService.getBrandingConfiguration();
            if (brandingConfiguration != null) {
                String perspectiveId = brandingConfiguration.getInitialWindowPerspectiveId();
                if (perspectiveId != null) {
                    //
                    IPerspectiveDescriptor pd = PlatformUI.getWorkbench().getPerspectiveRegistry()
                            .findPerspectiveWithId(perspectiveId);
                    if (pd != null) {
                        return perspectiveId;
                    }
                }
            }
        }
        return PERSPECTIVE_ID;
    }

    @SuppressWarnings("restriction")
    @Override
    public void preStartup() {

        // Fix bug 329,control the startup sequence of the plugin.
        // Promise the following plugin register themselves before system loaded.
        RunProcessPlugin.getDefault();
        CodeGeneratorActivator.getDefault();

        // get all login task to execut at the end but is needed here for monitor count
        LoginTaskRegistryReader loginTaskRegistryReader = new LoginTaskRegistryReader();
        ILoginTask[] allLoginTasks = loginTaskRegistryReader.getAllTaskListInstance();
        IProgressMonitor monitor = TalendSplashHandler.instance != null ? TalendSplashHandler.instance.getBundleProgressMonitor()
                : new NullProgressMonitor();

        SubMonitor subMonitor = SubMonitor.convert(monitor, allLoginTasks.length + 1);

        // handle the login tasks created using the extension point org.talend.core.repository.login.task
        for (ILoginTask toBeRun : allLoginTasks) {
            try {
                toBeRun.run(subMonitor.newChild(1, SubMonitor.SUPPRESS_NONE));
            } catch (Exception e) {
                log.error("Error while execution a login task.", e); //$NON-NLS-1$
            }
        }

        super.preStartup();

    }

    @Override
    public void postStartup() {
        super.postStartup();

        if (!ArrayUtils.contains(Platform.getApplicationArgs(), EclipseCommandLine.TALEND_DISABLE_LOGINDIALOG_COMMAND)) {
            RegisterManagement.getInstance().validateRegistration();
        }

        // PerspectiveReviewUtil.checkPerspectiveDisplayItems();
        CommonsPlugin.setWorkbenchCreated(true);
    }

}
