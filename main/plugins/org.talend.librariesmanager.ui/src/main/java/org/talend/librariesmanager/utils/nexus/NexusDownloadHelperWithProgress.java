// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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
import org.ops4j.pax.url.mvn.MavenResolver;
import org.talend.core.download.DownloadHelperWithProgress;
import org.talend.core.download.IDownloadHelper;
import org.talend.core.model.general.ModuleToInstall;
import org.talend.core.nexus.NexusServerBean;
import org.talend.core.nexus.TalendLibsServerManager;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;

public class NexusDownloadHelperWithProgress extends DownloadHelperWithProgress {

    private ModuleToInstall toInstall;

    public NexusDownloadHelperWithProgress(ModuleToInstall toInstall) {
        this.toInstall = toInstall;
    }

    @Override
    public void download(URL componentUrl, File destination, IProgressMonitor progressMonitor) throws Exception {
        File resolved = null;
        if (toInstall.isFromCustomNexus()) {
            final NexusServerBean customNexusServer = TalendLibsServerManager.getInstance().getCustomNexusServer();
            if (customNexusServer != null) {
                String mvnUri = componentUrl.toExternalForm();
                MavenArtifact parseMvnUrl = MavenUrlHelper.parseMvnUrl(mvnUri);
                if (parseMvnUrl != null) {
                    progressMonitor.subTask("Downloading " + toInstall.getName() + ": " + mvnUri + " from "
                            + customNexusServer.getServer());
                    final MavenResolver mavenResolver = TalendLibsServerManager.getInstance().getMavenResolver();
                    resolved = mavenResolver.resolve(componentUrl.toExternalForm());
                }
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
