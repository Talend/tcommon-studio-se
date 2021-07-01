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

import org.apache.commons.lang.StringUtils;
import org.talend.core.download.DownloadListener;
import org.talend.core.download.IDownloadHelper;
import org.talend.core.model.general.ModuleStatusProvider;
import org.talend.core.model.general.ModuleToInstall;
import org.talend.core.model.general.ModuleNeeded.ELibraryInstallStatus;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.nexus.TalendLibsServerManager;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.designer.maven.aether.util.AetherArtifactDownloadProvider;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.librariesmanager.maven.MavenArtifactsHandler;

public class AetherArtifactDownloader implements IDownloadHelper, DownloadListener {

    private List<DownloadListener> fListeners = new ArrayList<DownloadListener>();

    private boolean fCancel = false;

    private ArtifactRepositoryBean nexusServer;

    private URL downloadingURL = null;

    private long contentLength = -1l;

    private File resolvedFile = null;

    private ModuleToInstall module;

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
            AetherArtifactDownloadProvider resolver = new AetherArtifactDownloadProvider();
            resolver.addDownloadListener(this);
            try {
                resolvedFile = resolver.resolveArtifact(parseMvnUrl, nServer);
            } catch (Exception ex) {
                deleteResolvedFileIfExist();
                throw ex;
            }
            resolver.removeDownloadListener(this);
            if (resolvedFile.getAbsolutePath().toLowerCase().endsWith(TalendMavenConstants.PACKAGING_JAR)) {
                String pomFilePath = resolvedFile.getAbsolutePath().substring(0,
                        resolvedFile.getAbsolutePath().length() - TalendMavenConstants.PACKAGING_JAR.length())
                        + TalendMavenConstants.PACKAGING_POM;
                File pomFile = new File(pomFilePath);
                if (!pomFile.exists()) {
                    PomUtil.generatePomFile(pomFile, parseMvnUrl);
                }
            }
            ModuleStatusProvider.putDeployStatus(mavenUri, ELibraryInstallStatus.DEPLOYED);
            ModuleStatusProvider.putStatus(mavenUri, ELibraryInstallStatus.INSTALLED);
            if (this.isCancel()) {
                return;
            }
            boolean canGetNexusServer = TalendLibsServerManager.getInstance().getCustomNexusServer() != null;
            // if proxy artifact repository was configured, then do not deploy
            boolean deploy = canGetNexusServer && !TalendLibsServerManager.getInstance().isProxyArtifactRepoConfigured();
            if (deploy && StringUtils.isEmpty(parseMvnUrl.getRepositoryUrl()) && (module != null
                    && !module.isFromCustomNexus())) {
                MavenArtifactsHandler deployer = new MavenArtifactsHandler();
                deployer.deploy(resolvedFile, parseMvnUrl);
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

    @Override
    public void downloadFailed(Exception ex) {
        deleteResolvedFileIfExist();
        for (DownloadListener listener : fListeners) {
            listener.downloadFailed(ex);
        }
    }

    private void deleteResolvedFileIfExist() {
        if (resolvedFile != null && resolvedFile.exists()) {
            resolvedFile.delete();
        }
    }

    public ModuleToInstall getModule() {
        return module;
    }

    public void setModule(ModuleToInstall module) {
        this.module = module;
    }

}
