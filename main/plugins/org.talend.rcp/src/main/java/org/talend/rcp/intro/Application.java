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
package org.talend.rcp.intro;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.utils.system.EclipseCommandLine;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.migration.IMigrationToolService;
import org.talend.core.repository.CoreRepositoryPlugin;
import org.talend.core.tis.ICoreTisService;
import org.talend.core.ui.TalendBrowserLaunchHelper;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.rcp.i18n.Messages;
import org.talend.rcp.intro.workspace.ChooseWorkspaceData;
import org.talend.rcp.intro.workspace.ChooseWorkspaceDialog;
import org.talend.repository.RegistrationPlugin;
import org.talend.repository.license.LicenseManagement;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.ui.login.LoginComposite;
import org.talend.repository.ui.wizards.license.LicenseWizard;
import org.talend.repository.ui.wizards.license.LicenseWizardDialog;

/**
 * This class controls all aspects of the application's execution.
 */
public class Application implements IApplication {

    /**
     * 
     */
    private static final String TALEND_FORCE_INITIAL_WORKSPACE_PROMPT_SYS_PROP = "talend.force.initial.workspace.prompt"; //$NON-NLS-1$

    private static Logger log = Logger.getLogger(Application.class);

    /**
     * pref node to store the first launch status.
     */
    private static final String INITIAL_WORKSPACE_SHOWN = "INITIAL_WORKSPACE_SHOWN"; //$NON-NLS-1$

    @Override
    public Object start(IApplicationContext context) throws Exception {
        Display display = PlatformUI.createDisplay();

        try {
            Shell shell = new Shell(display, SWT.ON_TOP);
            // To show that the studio does not fully support java 8 yet
            if (checkUnSupportJavaVersion(shell)) {
                shell.dispose();
                return EXIT_OK;
            }
            Object instanceLocationCheck = acquireWorkspaceLock(shell);
            if (instanceLocationCheck != null) {// no workspace selected so return.
                shell.dispose();
                return instanceLocationCheck;
            }

            /*
             * setSqlpatternUsibility(context); setRefProjectUsibility(context);
             */
            CoreRepositoryPlugin.getDefault().setRCPMode();

            checkBrowserSupport();

            if (!ArrayUtils.contains(Platform.getApplicationArgs(), EclipseCommandLine.TALEND_DISABLE_LOGINDIALOG_COMMAND)
                    && !Boolean.parseBoolean(System.getProperty("talend.project.reload"))) {//$NON-NLS-1$ 
                openLicenseAndRegister(shell);
            }

            IMigrationToolService service = (IMigrationToolService) GlobalServiceRegister.getDefault().getService(
                    IMigrationToolService.class);
            service.executeWorspaceTasks();
            // saveConnectionBean(email);

            boolean logUserOnProject = logUserOnProject(display.getActiveShell());
            try {
                if (!logUserOnProject) {
                    // MOD qiongli 2010-11-1,bug 16723: Code Cleansing
                    // Platform.endSplash();
                    context.applicationRunning();
                    // ~
                    return EXIT_OK;
                }
            } finally {
                shell.dispose();
            }

            // if some commands are set to relaunch (not restart) the eclipse then relaunch it
            // this happens when project type does not match the running product type
            if (System.getProperty(org.eclipse.equinox.app.IApplicationContext.EXIT_DATA_PROPERTY) != null) {
                return IApplication.EXIT_RELAUNCH;
            }

            boolean afterUpdate = false;
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ICoreTisService.class)) {
                ICoreTisService tisService = (ICoreTisService) GlobalServiceRegister.getDefault().getService(
                        ICoreTisService.class);
                afterUpdate = tisService.needRestartAfterUpdate();
            }

            // common restart
            if (LoginComposite.isRestart) {
                // if after update,need to lauch the product by loading all new version plugins
                if (afterUpdate) {
                    EclipseCommandLine.updateOrCreateExitDataPropertyWithCommand(EclipseCommandLine.TALEND_RELOAD_COMMAND,
                            Boolean.TRUE.toString(), false);
                    // if relaunch, should delete the "disableLoginDialog" argument in eclipse data for bug TDI-19214
                    EclipseCommandLine.updateOrCreateExitDataPropertyWithCommand(
                            EclipseCommandLine.TALEND_DISABLE_LOGINDIALOG_COMMAND, null, true);
                    return IApplication.EXIT_RELAUNCH;
                }
                return IApplication.EXIT_RESTART;
            }

            IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                    IBrandingService.class);

            // for talend product only to add the links on the left of the coolbar
            // other products will simply reuse the default presentation factory.
            if (brandingService.isPoweredbyTalend()) {
                // setup the presentation factory, which is defined in the plugin.xml of the org.talend.rcp
                IPreferenceStore store = PlatformUI.getPreferenceStore();
                store.putValue(IWorkbenchPreferenceConstants.PRESENTATION_FACTORY_ID, "org.talend.rcp.presentationfactory"); //$NON-NLS-1$
            }
            int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
            if (returnCode == PlatformUI.RETURN_RESTART) {
                // use relaunch instead of restart to remove change the restart property that may have been added in the
                // previous
                // relaunch
                EclipseCommandLine.updateOrCreateExitDataPropertyWithCommand(EclipseCommandLine.TALEND_RELOAD_COMMAND,
                        Boolean.FALSE.toString(), false);
                EclipseCommandLine.updateOrCreateExitDataPropertyWithCommand(EclipseCommandLine.TALEND_PROJECT_TYPE_COMMAND,
                        null, true);
                // if relaunch, should delete the "disableLoginDialog" argument in eclipse data for bug TDI-19214
                EclipseCommandLine.updateOrCreateExitDataPropertyWithCommand(
                        EclipseCommandLine.TALEND_DISABLE_LOGINDIALOG_COMMAND, null, true, true);
                // TDI-8426, fix the swith project failure, when in dev also.
                // if dev, can't be restart, so specially for dev.
                if (Platform.inDevelopmentMode()) {
                    return IApplication.EXIT_RESTART;
                }
                return IApplication.EXIT_RELAUNCH;
            } else {
                return IApplication.EXIT_OK;
            }
        } finally {
            display.dispose();
            Location instanceLoc = Platform.getInstanceLocation();
            if (instanceLoc != null) { // release workspace lock for current app only, not for anothers.
                instanceLoc.release();
            }
        }

    }

    /**
     * 
     * DOC ggu Comment method "checkForBrowser".
     */
    private void checkBrowserSupport() {
        Shell shell = new Shell();
        try {
            Browser browser = new Browser(shell, SWT.BORDER);
            System.setProperty("USE_BROWSER", Boolean.TRUE.toString()); //$NON-NLS-1$ 
            browser.dispose();
        } catch (Throwable t) {
            System.setProperty("USE_BROWSER", Boolean.FALSE.toString()); //$NON-NLS-1$ 
        } finally {
            shell.dispose();

        }
    }

    private void openLicenseAndRegister(Shell shell) {
        if (!LicenseManagement.isLicenseValidated()) {
            LicenseWizard licenseWizard = new LicenseWizard();
            LicenseWizardDialog dialog = new LicenseWizardDialog(shell, licenseWizard);
            dialog.setTitle(""); //$NON-NLS-1$
            if (dialog.open() == WizardDialog.OK) {
                try {
                    LicenseManagement.acceptLicense();
                } catch (BusinessException e) {
                    ErrorDialogWidthDetailArea errorDialog = new ErrorDialogWidthDetailArea(shell, RegistrationPlugin.PLUGIN_ID,
                            "", e.getMessage()); //$NON-NLS-1$
                    System.exit(0);
                }

            } else {
                System.exit(0);
            }
        }

    }

    /**
     * Return <code>true</code> if the lock could be acquired.
     * 
     * @param shell
     * @return null if lock was aquired or some exit status else
     * @throws IOException if lock acquisition fails somehow
     */
    private Object acquireWorkspaceLock(Shell shell) throws IOException {
        // -data @none was specified but an ide requires workspace
        Location instanceLoc = Platform.getInstanceLocation();
        if (instanceLoc == null) {
            MessageDialog.openError(shell, Messages.getString("Application_workspaceMandatoryTitle"), //$NON-NLS-1$
                    Messages.getString("Application.Application_workspaceMandatoryMessage")); //$NON-NLS-1$
            return EXIT_OK;
        }
        // -data "/valid/path", workspace already set
        if (instanceLoc.isSet()) {
            // at this point its valid, so try to lock it and update the
            // metadata version information if successful
            try {
                if (instanceLoc.lock()) {
                    return null;
                }

                // we failed to create the directory.
                // Two possibilities:
                // 1. directory is already in use
                // 2. directory could not be created
                File workspaceDirectory = new File(instanceLoc.getURL().getFile());
                if (workspaceDirectory.exists()) {
                    MessageDialog.openError(shell, Messages.getString("Application.WorkspaceInuseTitle"), //$NON-NLS-1$
                            Messages.getString("Application.WorkspaceInuseMessage", workspaceDirectory)); //$NON-NLS-1$
                } else {
                    MessageDialog.openError(shell, Messages.getString("Application.WorkspaceCannotBeSetTitle"), //$NON-NLS-1$
                            Messages.getString("Application.WorkspaceCannotBeSetMessage")); //$NON-NLS-1$
                }
            } catch (IOException e) {
                log.error("Could not obtain lock for workspace location", //$NON-NLS-1$
                        e);
                MessageDialog.openError(shell, "internal error", e.getMessage());
            }
            return EXIT_OK;
        }

        // -data @noDefault or -data not specified, prompt and set
        ChooseWorkspaceData launchData = new ChooseWorkspaceData(instanceLoc.getDefault());
        boolean force = false;
        while (true) {
            // check if it is first launch and the workspace is forced to be shown, otherwise do not display the prompt
            Preferences node = new ConfigurationScope().getNode(ChooseWorkspaceData.ORG_TALEND_WORKSPACE_PREF_NODE);
            boolean workspaceAlreadyShown = node.getBoolean(INITIAL_WORKSPACE_SHOWN, false);
            URL workspaceUrl = null;
            if (!workspaceAlreadyShown && !Boolean.getBoolean(TALEND_FORCE_INITIAL_WORKSPACE_PROMPT_SYS_PROP)) {
                workspaceUrl = instanceLoc.getDefault();
                launchData.setShowDialog(false);// prevent the prompt to be shown next restart
                node.putBoolean(INITIAL_WORKSPACE_SHOWN, true);
                try {
                    node.flush();
                } catch (BackingStoreException e) {
                    log.error("failed to store workspace location in preferences :", e); //$NON-NLS-1$
                }
                // keep going to force the promp to appear
            } else {

                workspaceUrl = promptForWorkspace(shell, launchData, force);
                if (workspaceUrl == null) {
                    return EXIT_OK;
                }
            }
            // if there is an error with the first selection, then force the
            // dialog to open to give the user a chance to correct
            force = true;

            try {
                // the operation will fail if the url is not a valid
                // instance data area, so other checking is unneeded
                if (instanceLoc.setURL(workspaceUrl, true)) {
                    launchData.writePersistedData();
                    return null;
                }
            } catch (IllegalStateException e) {
                log.error(e);
                MessageDialog.openError(shell, Messages.getString("Application.WorkspaceCannotBeSetTitle"), //$NON-NLS-1$
                        Messages.getString("Application.WorkspaceCannotBeSetMessage", workspaceUrl.getFile()));
                return EXIT_OK;
            }

            // by this point it has been determined that the workspace is
            // already in use -- force the user to choose again
            MessageDialog.openError(shell, Messages.getString("Application.WorkspaceInuseTitle"), //$NON-NLS-1$
                    Messages.getString("Application.WorkspaceInuseMessage"));
        }
    }

    /**
     * Open a workspace selection dialog on the argument shell, populating the argument data with the user's selection.
     * Perform first level validation on the selection by comparing the version information. This method does not
     * examine the runtime state (e.g., is the workspace already locked?).
     * 
     * @param shell
     * @param launchData
     * @param force setting to true makes the dialog open regardless of the showDialog value
     * @return An URL storing the selected workspace or null if the user has canceled the launch operation.
     */
    private URL promptForWorkspace(Shell shell, ChooseWorkspaceData launchData, boolean force) {
        URL url = null;
        boolean doForce = force;
        do {
            // okay to use the shell now - this is the splash shell
            new ChooseWorkspaceDialog(shell, launchData, false, true).prompt(doForce);
            String instancePath = launchData.getSelection();
            if (instancePath == null) {
                return null;
            }

            // the dialog is not forced on the first iteration, but is on every
            // subsequent one -- if there was an error then the user needs to be
            // allowed to fix it
            doForce = true;

            // 70576: don't accept empty input
            if (instancePath.length() <= 0) {
                MessageDialog.openError(shell, Messages.getString("Application.workspaceEmptyTitle"), //$NON-NLS-1$
                        Messages.getString("Application.workspaceEmptyMessage")); //$NON-NLS-1$
                continue;
            }

            // create the workspace if it does not already exist
            File workspace = new File(instancePath);
            if (!workspace.exists()) {
                workspace.mkdir();
            }

            try {
                // Don't use File.toURL() since it adds a leading slash that Platform does not
                // handle properly. See bug 54081 for more details.
                String path = workspace.getAbsolutePath().replace(File.separatorChar, '/');
                url = new URL("file", null, path); //$NON-NLS-1$
            } catch (MalformedURLException e) {
                MessageDialog.openError(shell, Messages.getString("Application.workspaceInvalidTitle"), //$NON-NLS-1$
                        Messages.getString("Application.workspaceInvalidMessage")); //$NON-NLS-1$
                continue;
            }
        } while (url == null);
        return url;
    }

    private boolean logUserOnProject(Shell shell) {
        IRepositoryService service = (IRepositoryService) GlobalServiceRegister.getDefault().getService(IRepositoryService.class);
        return service.openLoginDialog(shell);
    }

    @Override
    public void stop() {
        final IWorkbench workbench = PlatformUI.getWorkbench();
        if (workbench == null) {
            return;
        }
        final Display display = workbench.getDisplay();
        display.syncExec(new Runnable() {

            @Override
            public void run() {
                if (!display.isDisposed()) {
                    workbench.close();
                }
            }
        });
    }

    public boolean checkUnSupportJavaVersion(Shell shell) {
        IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                IBrandingService.class);
        String javaVersion = System.getProperty("java.version");
        if (javaVersion != null) {
            org.talend.commons.utils.Version v = new org.talend.commons.utils.Version(javaVersion);
            if (v.getMajor() == 1 && v.getMinor() > 7) { // more than JDK 1.7
                if (brandingService.isPoweredbyTalend()) {
                    OpenLinkMessageDialog dialog = new OpenLinkMessageDialog(shell, "", shell.getBackgroundImage(),
                            Messages.getString("Application.doNotSupportJavaVersionYetPoweredbyTalend"), MessageDialog.WARNING,
                            new String[] { "Quit" }, 0);
                    dialog.open();
                    return true;
                } else {
                    MessageDialog dialog = new MessageDialog(shell, "", shell.getBackgroundImage(),
                            Messages.getString("Application.doNotSupportJavaVersionYetNoPoweredbyTalend"), MessageDialog.WARNING,
                            new String[] { "Quit" }, 0);
                    dialog.open();
                    return true;
                }
            }
        }
        return false;
    }

    private static class OpenLinkMessageDialog extends MessageDialog {

        public OpenLinkMessageDialog(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage,
                int dialogImageType, String[] dialogButtonLabels, int defaultIndex) {
            super(parentShell, dialogTitle, dialogTitleImage, dialogMessage, dialogImageType, dialogButtonLabels, defaultIndex);
        }

        @Override
        protected Control createDialogArea(Composite parent) {
            // create message area
            createMessageArea(parent);
            // add custom controls
            Composite composite = new Composite(parent, SWT.NONE);
            GridLayout layout = new GridLayout();
            layout.marginHeight = 0;
            layout.marginWidth = 43;
            composite.setLayout(layout);
            GridData data = new GridData(GridData.FILL_BOTH);
            data.horizontalSpan = 2;
            composite.setLayoutData(data);
            Hyperlink link = new Hyperlink(composite, SWT.WRAP);
            link.setText("https://help.talend.com/display/KB/Java+8+Support");
            link.setBackground(parent.getBackground());
            link.setUnderlined(true);
            link.addHyperlinkListener(new HyperlinkAdapter() {

                @Override
                public void linkActivated(HyperlinkEvent e) {
                    String url = "https://help.talend.com/display/KB/Java+8+Support";
                    TalendBrowserLaunchHelper.openURL(url);
                }
            });
            return composite;
        }
    }
}
