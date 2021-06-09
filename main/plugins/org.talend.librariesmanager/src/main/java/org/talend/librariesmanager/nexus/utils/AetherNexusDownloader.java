// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.librariesmanager.nexus.utils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.talend.core.download.DownloadListener;
import org.talend.core.download.IDownloadHelper;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.nexus.TalendLibsServerManager;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.designer.maven.aether.util.AetherNexusDownloadProvider;
import org.talend.librariesmanager.maven.MavenArtifactsHandler;

public class AetherNexusDownloader implements IDownloadHelper,DownloadListener {

    private List<DownloadListener> fListeners = new ArrayList<DownloadListener>();

    private boolean fCancel = false;

    private ArtifactRepositoryBean nexusServer;

    private URL downloadingURL = null;

    private long contentLength = -1l;

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.download.IDownloadHelper#download(java.net.URL, java.io.File)
     */
    @Override
    public void download(URL url, File desc) throws Exception {
        this.downloadingURL = url;
        String mavenUri = url.toExternalForm();
        MavenArtifact parseMvnUrl = MavenUrlHelper.parseMvnUrl(mavenUri);
        if (parseMvnUrl != null) {
            ArtifactRepositoryBean nServer = getNexusServer();
            AetherNexusDownloadProvider resolver = new AetherNexusDownloadProvider();
            resolver.addDownloadListener(this);
            File downloadedFile = resolver.resolveArtifact(parseMvnUrl, nServer);
            MavenArtifactsHandler deployer = new MavenArtifactsHandler();
            boolean canGetNexusServer = TalendLibsServerManager.getInstance().getCustomNexusServer() != null;
            // if proxy artifact repository was configured, then do not deploy
            boolean deploy = canGetNexusServer && !TalendLibsServerManager.getInstance().isProxyArtifactRepoConfigured();
            if (this.isCancel()) {
                return;
            }
            if (deploy) {
                deployer.deploy(downloadedFile, parseMvnUrl);
            }
        }

    }

    /**
     * Return true if the user cancel download process.
     *
     * @return the cancel
     */
    public boolean isCancel() {
        return fCancel;
    }

    /**
     * Set true if the user cacel download process.
     *
     * @param cancel the cancel to set
     */
    @Override
    public void setCancel(boolean cancel) {
        fCancel = cancel;
    }

    /**
     * Add listener to observe the download process.
     *
     * @param listener
     */
    public void addDownloadListener(DownloadListener listener) {
        fListeners.add(listener);
    }

    public void removeDownloadListener(DownloadListener listener) {
        fListeners.remove(listener);
    }

    public ArtifactRepositoryBean getNexusServer() {
        if (this.nexusServer == null) {
            return TalendLibsServerManager.getInstance().getTalentArtifactServer();
        }
        return this.nexusServer;
    }

    public void setTalendlibServer(ArtifactRepositoryBean talendlibServer) {
        this.nexusServer = talendlibServer;
    }

    @Override
    public URL getDownloadingURL() {
        return downloadingURL;
    }

    @Override
    public long getContentLength() {
        return contentLength;
    }

    @Override
    public void downloadStart(int totalSize) {
        this.contentLength = totalSize;
        for (DownloadListener listener : fListeners) {
            listener.downloadStart(totalSize);
        } 
    }

    @Override
    public void downloadProgress(IDownloadHelper downloader, int bytesDownloaded) {
        for (DownloadListener listener : fListeners) {
            listener.downloadProgress(this, bytesDownloaded);
        }      
    }

    @Override
    public void downloadComplete() {
        for (DownloadListener listener : fListeners) {
            listener.downloadComplete();
        }      
    }
}
