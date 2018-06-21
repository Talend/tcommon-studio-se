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
package org.talend.librariesmanager.utils.nexus;

import java.io.File;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.download.DownloadHelperWithProgress;
import org.talend.core.download.IDownloadHelper;
import org.talend.core.model.general.ModuleToInstall;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.nexus.TalendLibsServerManager;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.librariesmanager.nexus.utils.NexusDownloader;

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
        boolean downloadFromCustomNexus = toInstall.isFromCustomNexus();

        String mvnUri = componentUrl.toExternalForm();
        MavenArtifact mArtifact = MavenUrlHelper.parseMvnUrl(mvnUri, false);
        if (mArtifact != null) {
            String repositoryUrl = mArtifact.getRepositoryUrl();
            if (StringUtils.isNotEmpty(repositoryUrl)) {
                // TalendLibsServerManager manager = TalendLibsServerManager.getInstance();
                final ArtifactRepositoryBean customNexusServer = new ArtifactRepositoryBean(false);
                customNexusServer.setServer(repositoryUrl);
                customNexusServer.setAbsoluteURL(true);
                String username = mArtifact.getUsername();
                String password = mArtifact.getPassword();
                if (StringUtils.isNotEmpty(username)) {
                    customNexusServer.setUserName(username);
                    customNexusServer.setPassword(password);
                }
                String resolvedMvnUri = MavenUrlHelper.generateMvnUrl(mArtifact.getGroupId(), mArtifact.getArtifactId(),
                        mArtifact.getVersion(), mArtifact.getType(), mArtifact.getClassifier());
                progressMonitor.subTask(
                        "Downloading " + toInstall.getName() + ": " + resolvedMvnUri + " from " + customNexusServer.getServer());
                ILibraryManagerService libManager = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                        ILibraryManagerService.class);
                // seems the customNexusServer is not used in resolveJar function, so still need to provide
                // user/password in the mvn uri
                String decryptedMvnUri = MavenUrlHelper.generateMvnUrl(mArtifact);
                resolved = libManager.resolveJar(customNexusServer, decryptedMvnUri);
                if (resolved != null && resolved.exists()) {
                    return;
                }
            }
        }
        if (downloadFromCustomNexus) {
            TalendLibsServerManager manager = TalendLibsServerManager.getInstance();
            final ArtifactRepositoryBean customNexusServer = manager.getCustomNexusServer();
            if (customNexusServer != null) {
                // String mvnUri = componentUrl.toExternalForm();
                progressMonitor.subTask("Downloading " + toInstall.getName() + ": " + mvnUri + " from "
                        + customNexusServer.getServer());
                ILibraryManagerService libManager = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                        ILibraryManagerService.class);
                resolved = libManager.resolveJar(customNexusServer, mvnUri);
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
