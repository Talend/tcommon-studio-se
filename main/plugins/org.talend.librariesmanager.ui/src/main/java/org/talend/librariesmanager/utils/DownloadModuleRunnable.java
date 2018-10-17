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
package org.talend.librariesmanager.utils;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.ops4j.pax.url.mvn.Handler;
import org.talend.commons.ui.runtime.exception.MessageBoxExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.general.ModuleNeeded.ELibraryInstallStatus;
import org.talend.core.model.general.ModuleStatusProvider;
import org.talend.core.model.general.ModuleToInstall;
import org.talend.librariesmanager.ui.LibManagerUiPlugin;
import org.talend.librariesmanager.ui.i18n.Messages;
import org.talend.librariesmanager.ui.wizards.AcceptModuleLicensesWizard;
import org.talend.librariesmanager.ui.wizards.AcceptModuleLicensesWizardDialog;
import org.talend.librariesmanager.utils.nexus.NexusDownloadHelperWithProgress;

abstract public class DownloadModuleRunnable implements IRunnableWithProgress {

    protected List<ModuleToInstall> toDownload;

    protected Set<String> downloadFailed;

    protected Set<String> installedModules;

    private boolean checkLibraries;

    /**
     * DOC sgandon DownloadModuleRunnable constructor comment.
     * 
     * @param shell, never null, used to ask the user to accept the licenses
     * @param toDownload
     */
    public DownloadModuleRunnable(List<ModuleToInstall> toDownload) {
        this.toDownload = toDownload;
        downloadFailed = new HashSet<String>();
        installedModules = new HashSet<String>();
        checkLibraries = true;
    }

    public DownloadModuleRunnable(List<ModuleToInstall> toDownload, boolean checkLibraries) {
        this(toDownload);
        this.checkLibraries = checkLibraries;
    }

    @Override
    public void run(final IProgressMonitor monitor) {
        SubMonitor subMonitor = SubMonitor.convert(monitor,
                Messages.getString("ExternalModulesInstallDialog.downloading2") + " (" + toDownload.size() + ")", //$NON-NLS-1$
                toDownload.size() * 10 + 5);
        if (checkAndAcceptLicenses(subMonitor)) {
            downLoad(subMonitor);
        }
        if (monitor != null) {
            monitor.setCanceled(subMonitor.isCanceled());
            monitor.done();
        }
    }

    private void downLoad(final IProgressMonitor monitor) {
        SubMonitor subMonitor = SubMonitor.convert(monitor,
                Messages.getString("ExternalModulesInstallDialog.downloading2") + " (" + toDownload.size() + ")", //$NON-NLS-1$
                toDownload.size());

        // TUP-3135 : stop to try to download at the first timeout.
        boolean connectionTimeOut = false;
        for (final ModuleToInstall module : toDownload) {
            if (!monitor.isCanceled() && !connectionTimeOut) {
                monitor.subTask(module.getName());
                boolean canDownload;
                try {
                    // check license
                    boolean isLicenseAccepted = module.isFromCustomNexus()
                            || (LibManagerUiPlugin.getDefault().getPreferenceStore().contains(module.getLicenseType())
                                    && LibManagerUiPlugin.getDefault().getPreferenceStore().getBoolean(module.getLicenseType()));

                    canDownload = isLicenseAccepted;
                    if (!canDownload) {
                        subMonitor.worked(1);
                        continue;
                    }
                    NexusDownloadHelperWithProgress downloader = new NexusDownloadHelperWithProgress(module);
                    if (!module.getMavenUris().isEmpty()) {
                        for (String mvnUri : module.getMavenUris()) {
                            if (ELibraryInstallStatus.INSTALLED == ModuleStatusProvider.getStatus(mvnUri)) {
                                continue;
                            }
                            downloader.download(new URL(null, mvnUri, new Handler()), null, subMonitor.newChild(1));

                        }
                    } else {
                        if (ELibraryInstallStatus.INSTALLED == ModuleStatusProvider.getStatus(module.getMavenUri())) {
                            continue;
                        }
                        downloader.download(new URL(null, module.getMavenUri(), new Handler()), null, subMonitor.newChild(1));
                    }

                    // deploy to index as snapshot
                    installedModules.add(module.getName());
                } catch (Exception e) {
                    downloadFailed.add(module.getName());
                    connectionTimeOut = true;
                    MessageBoxExceptionHandler.process(new Exception("Download " + module.getName() + " failed!", e));
                    continue;
                }
                canDownload = false;
            } else {
                downloadFailed.add(module.getName());
            }
        }
        if (checkLibraries) {
            ILibrariesService librariesService = (ILibrariesService) GlobalServiceRegister.getDefault()
                    .getService(ILibrariesService.class);
            librariesService.checkLibraries();
        }
    }

    protected boolean hasLicensesToAccept() {
        if (toDownload != null && toDownload.size() > 0) {
            for (ModuleToInstall module : toDownload) {
                // no need accept license if it is from custom nexus
                if (module.isFromCustomNexus()) {
                    continue;
                }
                String licenseType = module.getLicenseType();
                if (licenseType != null) {
                    boolean isLicenseAccepted = LibManagerUiPlugin.getDefault().getPreferenceStore()
                            .getBoolean(module.getLicenseType());
                    if (!isLicenseAccepted) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    protected boolean checkAndAcceptLicenses(final IProgressMonitor monitor) {
        final AtomicBoolean accepted = new AtomicBoolean(true);
        if (hasLicensesToAccept()) {
            Display.getDefault().syncExec(new Runnable() {

                @Override
                public void run() {
                    AcceptModuleLicensesWizard licensesWizard = new AcceptModuleLicensesWizard(toDownload);
                    AcceptModuleLicensesWizardDialog wizardDialog = new AcceptModuleLicensesWizardDialog(
                            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), licensesWizard, toDownload, monitor);
                    wizardDialog.setPageSize(700, 380);
                    wizardDialog.create();
                    if (wizardDialog.open() != Window.OK) {
                        accepted.set(false);
                    }
                }
            });

        }

        return accepted.get();
    }

    /**
     * DOC sgandon Comment method "acceptLicence".
     * 
     * @param module
     */
    abstract protected boolean acceptLicence(ModuleToInstall module);

    /**
     * Getter for downloadFailed.
     * 
     * @return the downloadFailed
     */
    public Set<String> getDownloadFailed() {
        return this.downloadFailed;
    }

    /**
     * Getter for installedModules.
     * 
     * @return the installedModules
     */
    public Set<String> getInstalledModules() {
        return this.installedModules;
    }
}
