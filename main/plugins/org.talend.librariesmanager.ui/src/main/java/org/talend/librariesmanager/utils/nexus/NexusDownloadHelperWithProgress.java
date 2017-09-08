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
package org.talend.librariesmanager.utils.nexus;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.download.DownloadHelperWithProgress;
import org.talend.core.download.IDownloadHelper;
import org.talend.core.model.general.ModuleToInstall;
import org.talend.core.nexus.NexusServerBean;
import org.talend.core.nexus.TalendLibsServerManager;

/**
 * created by wchen on Apr 24, 2015 Detailled comment
 *
 */
public class NexusDownloadHelperWithProgress extends DownloadHelperWithProgress {

    private ModuleToInstall toInstall;

    public NexusDownloadHelperWithProgress(ModuleToInstall toInstall) {
        this.toInstall = toInstall;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.download.DownloadHelperWithProgress#download(java.net.URL, java.io.File,
     * org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void download(URL componentUrl, File destination, IProgressMonitor progressMonitor) throws Exception {
        File resolved = null;
        if (toInstall.isFromCustomNexus()) {
            TalendLibsServerManager manager = TalendLibsServerManager.getInstance();
            final NexusServerBean customNexusServer = manager.getCustomNexusServer();
            if (customNexusServer != null) {
                String mvnUri = componentUrl.toExternalForm();
                progressMonitor.subTask("Downloading " + toInstall.getName() + ": " + mvnUri + " from "
                        + customNexusServer.getServer());
                ILibraryManagerService libManager = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                        ILibraryManagerService.class);
                resolved = libManager.resolveJar(manager, customNexusServer, mvnUri);
            }
        }
        if (resolved != null && resolved.exists()) {
            return;
        }
        super.download(componentUrl, destination, progressMonitor);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.download.DownloadHelperWithProgress#createDownloadHelperDelegate(org.talend.core.download.
     * DownloadHelperWithProgress.DownloadListenerImplementation)
     */
    @Override
    protected IDownloadHelper createDownloadHelperDelegate(final DownloadListenerImplementation downloadProgress) {

        NexusDownloader downloadHelper = new NexusDownloader() {

            /*
             * (non-Javadoc)
             * 
             * @see org.talend.core.download.DownloadHelper#isCancel()
             */
            @Override
            public boolean isCancel() {
                return downloadProgress.isCanceled();
            }
        };
        downloadHelper.addDownloadListener(downloadProgress);
        return downloadHelper;

    }

}
