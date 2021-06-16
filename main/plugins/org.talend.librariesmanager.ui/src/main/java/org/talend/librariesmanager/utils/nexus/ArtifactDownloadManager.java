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
package org.talend.librariesmanager.utils.nexus;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.ops4j.pax.url.mvn.Handler;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.download.DownloadListener;
import org.talend.core.download.IDownloadHelper;
import org.talend.core.model.general.ModuleNeeded.ELibraryInstallStatus;
import org.talend.core.model.general.ModuleStatusProvider;
import org.talend.core.model.general.ModuleToInstall;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.nexus.TalendLibsServerManager;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.librariesmanager.nexus.utils.AetherArtifactDownloader;

public class ArtifactDownloadManager implements DownloadListener {

    protected IProgressMonitor progressMonitor;

    private int maxThreadNum = 10;

    private int retryTime = 3;

    private ThreadPoolExecutor executor;

    private List<ModuleToInstall> moduleList = new ArrayList<ModuleToInstall>();

    private List<ModuleToInstall> downloadFinishedList = new ArrayList<ModuleToInstall>();

    private Map<ModuleToInstall, Exception> downloadFailedMap = new HashMap<ModuleToInstall, Exception>();

    public ArtifactDownloadManager(List<ModuleToInstall> moduleList, IProgressMonitor progressMonitor) {
        this.moduleList = moduleList;
        this.progressMonitor = progressMonitor;
    }

    public void start() {
        executor = new ThreadPoolExecutor(maxThreadNum, maxThreadNum, 0, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(), new ArtifactDownloadThreadFactory());
        List<Runnable> taskList = getTasks();
        if (progressMonitor != null) {
            progressMonitor.beginTask("Downloading", taskList.size());
        }
        for (Runnable task : taskList) {
            executor.execute(task);
        }
        while (true) {
            if (executor.getTaskCount() == executor.getCompletedTaskCount()) {
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ExceptionHandler.process(ex);
            }
        }
        stop();
    }

    public void stop() {
        if (!executor.isShutdown()) {
            List<Runnable> jobList = executor.shutdownNow();
            for (Runnable runnable : jobList) {
                if (runnable instanceof AbsArtifactDownLoaderRunnable) {
                    this.downloadFailed(((AbsArtifactDownLoaderRunnable) runnable).getModule(),
                            ((AbsArtifactDownLoaderRunnable) runnable).getUrl(), new UserCanceledException("User canceled"));
                }
            }
        }
        progressMonitor.done();
    }

    private List<Runnable> getTasks() {
        List<Runnable> taskList = new ArrayList<Runnable>();
        for (ModuleToInstall module : moduleList) {
            if (!module.getMavenUris().isEmpty()) {
                for (String mvnUri : module.getMavenUris()) {
                    if (ELibraryInstallStatus.INSTALLED == ModuleStatusProvider.getStatus(mvnUri)) {
                        continue;
                    }
                    taskList.add(getTask(module, mvnUri));

                }
            } else {
                if (ELibraryInstallStatus.INSTALLED == ModuleStatusProvider.getStatus(module.getMavenUri())) {
                    continue;
                }
                taskList.add(getTask(module, module.getMavenUri()));
            }
        }
        return taskList;
    }

    private Runnable getTask(ModuleToInstall module, String mvnUri) {
        ArtifactRepositoryBean serverBean = getServerFromModule(mvnUri, module.isFromCustomNexus());
        return new ArtifactDownloaderRunnable(module, mvnUri, this, serverBean);
    }

    private ArtifactRepositoryBean getServerFromModule(String mvnUri, boolean isFromCustomNexus) {
        if (isFromCustomNexus) {
            return TalendLibsServerManager.getInstance().getCustomNexusServer();
        } else {
            MavenArtifact artifact = MavenUrlHelper.parseMvnUrl(mvnUri, false);
            if (artifact != null && StringUtils.isNotEmpty(artifact.getRepositoryUrl())) {
                ArtifactRepositoryBean customNexusServer = new ArtifactRepositoryBean(false);
                customNexusServer.setServer(artifact.getRepositoryUrl());
                customNexusServer.setAbsoluteURL(true);
                String username = artifact.getUsername();
                String password = artifact.getPassword();
                if (StringUtils.isNotEmpty(username)) {
                    customNexusServer.setUserName(username);
                    customNexusServer.setPassword(password);
                }
                return customNexusServer;
            }
        }
        return null;
    }

    @Override
    public void downloadStart(int totalSize) {
    }

    @Override
    public void downloadProgress(IDownloadHelper downloader, int bytesDownloaded) {
        if (downloader != null && downloader.getDownloadingURL() != null) {
            progressMonitor.subTask(
                    bytesDownloaded + "/" + downloader.getContentLength() + " : " + downloader.getDownloadingURL().getFile());
        }
    }

    @Override
    public void downloadComplete() {
        if (progressMonitor != null) {
            progressMonitor.worked(1);
        }
    }

    public void downloadSuccessful(ModuleToInstall module, String mvnURL) {
        downloadFinishedList.add(module);
    }

    public void downloadFailed(ModuleToInstall module, String mvnURL, Exception ex) {
        this.downloadFailedMap.put(module, ex);
    }

    public IProgressMonitor getProgressMonitor() {
        return progressMonitor;
    }

    public int getRetryTime() {
        return retryTime;
    }

    public Map<ModuleToInstall, Exception> getDownloadFailedMap() {
        return downloadFailedMap;
    }

    public List<ModuleToInstall> getDownloadFinishedList() {
        return downloadFinishedList;
    }
}

abstract class AbsArtifactDownLoaderRunnable implements Runnable {

    private static Logger logger = Logger.getLogger(AbsArtifactDownLoaderRunnable.class);

    protected ModuleToInstall module;

    protected String url;

    protected ArtifactDownloadManager downloadManager;

    AbsArtifactDownLoaderRunnable(ModuleToInstall module, String url, ArtifactDownloadManager downloadManager) {
        this.module = module;
        this.url = url;
        this.downloadManager = downloadManager;
    }

    @Override
    public void run() {
        int downloadTimes = 0;
        if (downloadManager.getProgressMonitor() != null && downloadManager.getProgressMonitor().isCanceled()) {
            downloadManager.downloadFailed(module, url, new UserCanceledException("User canceled"));
            if (downloadManager.getProgressMonitor() != null) {
                downloadManager.getProgressMonitor().worked(1);
            }
            downloadManager.stop();
            return;
        }
        for (; downloadTimes < downloadManager.getRetryTime(); downloadTimes++) {
            try {
                doDownLoad();
                downloadManager.downloadSuccessful(module, url);
                break;
            } catch (Exception ex) {
                if (downloadTimes < downloadManager.getRetryTime() - 1) {
                    logger.warn("Download " + url + " failed, will try to download again", ex);
                } else {
                    logger.error("Download " + url + " failed.", ex);
                    downloadManager.downloadFailed(module, url, ex);
                }
            }
        }
    }

    protected abstract void doDownLoad() throws Exception;

    public ModuleToInstall getModule() {
        return module;
    }

    public String getUrl() {
        return url;
    }
}

class ArtifactDownloaderRunnable extends AbsArtifactDownLoaderRunnable {

    private ArtifactRepositoryBean serverBean;

    ArtifactDownloaderRunnable(ModuleToInstall module, String url, ArtifactDownloadManager downloadManager,
            ArtifactRepositoryBean serverBean) {
        super(module, url, downloadManager);
        this.serverBean = serverBean;
    }

    protected void doDownLoad() throws Exception {
        AetherArtifactDownloader downloadHelper = new AetherArtifactDownloader();
        downloadHelper.addDownloadListener(downloadManager);
        downloadHelper.setModule(getModule());
        if (serverBean != null) {
            downloadHelper.setTalendlibServer(serverBean);
        }
        downloadHelper.download(new URL(null, url, new Handler()), null);
    }
}

class ArtifactDownloadThreadFactory implements ThreadFactory {

    @Override
    public Thread newThread(Runnable r) {
        String name = "";
        if (r instanceof IDownloadHelper) {
            IDownloadHelper downloadHelper = (IDownloadHelper) r;
            if (downloadHelper.getDownloadingURL() != null) {
                name = downloadHelper.getDownloadingURL().getFile();
            }
        }
        String threadName = "Downloading " + name + " task";
        return new Thread(r, threadName);
    }

}
