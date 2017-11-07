package org.talend.librariesmanager.ui;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.talend.commons.ui.gmf.util.DisplayUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.ISVNProviderServiceInCoreRuntime;
import org.talend.core.PluginChecker;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.ModuleNeeded.ELibraryInstallStatus;
import org.talend.librariesmanager.model.ModulesNeededProvider;
import org.talend.librariesmanager.ui.dialogs.ExternalModulesInstallDialogWithProgress;
import org.talend.librariesmanager.ui.i18n.Messages;
import org.talend.osgi.hook.notification.JarMissingObservable;
import org.talend.osgi.hook.notification.JarMissingObservable.JarMissingEvent;

/**
 * created by sgandon on 17 sept. 2013 This register a listener to be notified when a jar is missing when activating an
 * OSGI bundle. When the notification happend, it show the Modules dialog box to allow the user to download the required
 * jars.
 * 
 */
public class InitializeMissingJarHandler implements IStartup, Observer {

    private static Logger log = Logger.getLogger(InitializeMissingJarHandler.class);

    private List<ModuleNeeded> allModulesNeededExtensionsForPlugin;

    // list of bundle ID that where already handle by this handler.
    private Set<Long> bundlesAlreadyHandled = new HashSet<Long>();

    private ILibrariesService librariesService;

    private ILibraryManagerService libraryManagerService;

    @Override
    public void earlyStartup() {
        setupMissingJarLoadingObserver();
        librariesService = LibManagerUiPlugin.getDefault().getLibrariesService();
        libraryManagerService = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                ILibraryManagerService.class);
    }

    /**
     * looks for the OSGI service that notify that a jar is missing when loading a bundle and register this as a
     * listener
     */
    @SuppressWarnings("restriction")
    private void setupMissingJarLoadingObserver() {
        BundleContext bundleContext = getBundleContext();
        if (bundleContext != null) {
            ServiceReference serviceReference = bundleContext.getServiceReference(JarMissingObservable.class.getCanonicalName());
            if (serviceReference != null) {
                JarMissingObservable missingJarObservable = (JarMissingObservable) bundleContext.getService(serviceReference);
                missingJarObservable.addObserver(this);
            } else {// could not find the hook registry service so log it
                log.error("Could not find a registered OSGI service for : " + "java.util.Observable");
            }
        } else {// bundleContext is null should never happend but log it
            log.error("Could not get bundle context for : " + this.getClass());
        }
    }

    /**
     * @return the current bundle BundleContext
     */
    public BundleContext getBundleContext() {
        Bundle bundle = FrameworkUtil.getBundle(this.getClass());
        BundleContext bundleContext = bundle.getBundleContext();
        return bundleContext;
    }

    /**
     * called when the jar loadingin hook has failed to find the jar this is never called in a GUI thread.
     * 
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg != null && arg instanceof JarMissingEvent) {
            final JarMissingEvent jarMissingEvent = (JarMissingEvent) arg;
            // only show message box once par plugin, meaning that if user ignored it once, the bundle will not load
            // properly anyway.
            if (!bundlesAlreadyHandled.contains(jarMissingEvent.getBundleId())) {
                bundlesAlreadyHandled.add(jarMissingEvent.getBundleId());
                showMissingModuleDialog(jarMissingEvent);
            }// else already handled so ignors it.
        } else {// notification is not expected so log it
            IllegalArgumentException illegalArgumentException = new IllegalArgumentException(
                    "was expecting a type :" + JarMissingEvent.class.getCanonicalName()); //$NON-NLS-1$
            illegalArgumentException.fillInStackTrace();
            log.error("Could not find the proper JarMissing values", illegalArgumentException); //$NON-NLS-1$
        }
    }

    /**
     * look for all the required modules for a given bundle, and let the user decide to download it. this method is
     * blocked until the dialog box is closed.
     * 
     * @param jarMissingEvent, must never be null
     */
    protected void showMissingModuleDialog(final JarMissingEvent jarMissingEvent) {
        if (allModulesNeededExtensionsForPlugin == null) {
            this.allModulesNeededExtensionsForPlugin = ModulesNeededProvider.getAllModulesNeededExtensionsForPlugin();
        }
        List<ModuleNeeded> requiredModulesForBundle = ModulesNeededProvider.filterRequiredModulesForBundle(
                jarMissingEvent.getBundleSymbolicName(), allModulesNeededExtensionsForPlugin);
        final List<ModuleNeeded> filteredRequiredJars = new ArrayList<ModuleNeeded>(requiredModulesForBundle.size());
        // filter the jar that are already installed
        for (ModuleNeeded module : requiredModulesForBundle) {
            String moduleName = module.getModuleName();
            // if jar does not exist at expected folder then check if it is registered in the Studio
            boolean installed = false;
            if (!new File(jarMissingEvent.getExpectedLibFolder(), moduleName).exists()) {
                // check that library is already available and registered but not deployed to maven.
                try {
                    if (module.getStatus() == ELibraryInstallStatus.INSTALLED
                            && module.getDeployStatus() == ELibraryInstallStatus.NOT_DEPLOYED) {
                        // lib exist so deploy it
                        List<ModuleNeeded> allModuleNeeded = ModulesNeededProvider.getModulesNeededForName(moduleName);
                        for (ModuleNeeded sameModule : allModuleNeeded) {
                            String moduleLocation = sameModule.getModuleLocaion();
                            if (sameModule.getStatus() == ELibraryInstallStatus.INSTALLED && moduleLocation != null
                                    && !moduleLocation.isEmpty()) {
                                URI uri = new URI(moduleLocation);
                                URL url = FileLocator.toFileURL(uri.toURL());
                                if ("file".equals(url.getProtocol())) { //$NON-NLS-1$
                                    // TUP-4968 using 'URIUtil.toURI(url)' supports a path with special chars(like
                                    // as space).
                                    libraryManagerService.deploy(URIUtil.toURI(url), module.getMavenUri());
                                    installed = true;
                                }// else not a file so keep going
                                break;
                            }// else not an installed module or no url so keep so keep looking
                        }
                        if (PluginChecker.isSVNProviderPluginLoaded()) {
                            ISVNProviderServiceInCoreRuntime svnService = (ISVNProviderServiceInCoreRuntime) GlobalServiceRegister
                                    .getDefault().getService(ISVNProviderServiceInCoreRuntime.class);
                            if (svnService != null && svnService.isSvnLibSetupOnTAC()) {
                                svnService.syncLibs(null);
                            }
                        }
                    } else {
                        // try to retreive again incase it exist on custom nexus
                        final boolean retrieve = libraryManagerService.retrieve(module, null, false);
                        installed = retrieve;
                    }// else no installed so keep going and ask the user
                } catch (URISyntaxException e) {
                    log.warn("Could not get installade status for library:" + moduleName, e);
                } catch (IOException e) {
                    log.warn("Could not get installade status for library:" + moduleName, e);
                }
            }
            if (!installed && !new File(jarMissingEvent.getExpectedLibFolder(), moduleName).exists()) {
                filteredRequiredJars.add(module);
            }// else jar already installed to filter it by ignoring it.
        }
        if (!filteredRequiredJars.isEmpty()) {
            Display.getDefault().syncExec(new Runnable() {

                @Override
                public void run() {
                    ExternalModulesInstallDialogWithProgress dialog = new ExternalModulesInstallDialogWithProgress(
                            DisplayUtils.getDefaultShell(),
                            Messages.getString("ExternalModulesInstallDialog_Title_Missing_jars_for_plugin"), //$NON-NLS-1$
                            Messages.getString("ExternalModulesInstallDialog_description_jars_to_be_installed_in"), SWT.APPLICATION_MODAL); //$NON-NLS-1$
                    dialog.showDialog(true, filteredRequiredJars);
                }
            });
        }// else there is not extension point defining the required bundles so do not ask the user ignor.
    }
}
