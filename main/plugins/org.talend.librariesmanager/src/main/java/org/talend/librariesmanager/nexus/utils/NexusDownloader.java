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
package org.talend.librariesmanager.nexus.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.download.DownloadListener;
import org.talend.core.download.IDownloadHelper;
import org.talend.core.model.general.Project;
import org.talend.core.nexus.HttpClientTransport;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.nexus.TalendLibsServerManager;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.librariesmanager.maven.MavenArtifactsHandler;
import org.talend.repository.ProjectManager;
import org.talend.utils.io.FilesUtils;

/**
 * created by wchen on Apr 24, 2015 Detailled comment
 *
 */
public class NexusDownloader implements IDownloadHelper {

    private List<DownloadListener> fListeners = new ArrayList<DownloadListener>();

    private boolean fCancel = false;

    private static final int BUFFER_SIZE = 8192;

    private ArtifactRepositoryBean nexusServer;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.download.IDownloadHelper#download(java.net.URL, java.io.File)
     */
    @Override
    public void download(URL url, File desc) throws Exception {
        String mavenUri = url.toExternalForm();
        MavenArtifact parseMvnUrl = MavenUrlHelper.parseMvnUrl(mavenUri);
        if (parseMvnUrl != null) {
            String tempPath = getTmpFolderPath();
            File createTempFile = File.createTempFile("talend_official", "");
            createTempFile.delete();
            File tempFolder = new File(tempPath + File.separator + createTempFile.getName());
            if (tempFolder.exists()) {
                tempFolder.delete();
            }
            tempFolder.mkdirs();

            String name = parseMvnUrl.getArtifactId();
            String type = parseMvnUrl.getType();
            if (type == null || "".equals(type)) {
                type = MavenConstants.PACKAGING_JAR;
            }
            name = name + "." + type;
            File downloadedFile = new File(tempFolder, name);

            NullProgressMonitor monitor = new NullProgressMonitor();
            ArtifactRepositoryBean nServer = getNexusServer();
            new HttpClientTransport(nServer.getRepositoryURL(), nServer.getUserName(), nServer.getPassword()) {

                @Override
                protected HttpResponse execute(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI)
                        throws Exception {
                    HttpGet httpGet = new HttpGet(targetURI);
                    HttpResponse response = httpClient.execute(httpGet);
                    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        HttpEntity entity = response.getEntity();
                        InputStream inputStream = entity.getContent();
                        BufferedInputStream bis = null;
                        BufferedOutputStream bos = null;
                        try {
                            bis = new BufferedInputStream(inputStream);
                            bos = new BufferedOutputStream(new FileOutputStream(downloadedFile));
                            long contentLength = entity.getContentLength();
                            fireDownloadStart(new Long(contentLength).intValue());

                            long refreshInterval = 1000;
                            if (contentLength < BUFFER_SIZE * 10) {
                                refreshInterval = contentLength / 200;
                            }
                            int bytesDownloaded = 0;
                            byte[] buf = new byte[BUFFER_SIZE];
                            int bytesRead = -1;
                            long startTime = new Date().getTime();
                            int byteReadInloop = 0;
                            while ((bytesRead = bis.read(buf)) != -1) {
                                bos.write(buf, 0, bytesRead);
                                long currentTime = new Date().getTime();
                                byteReadInloop = byteReadInloop + bytesRead;
                                if (currentTime - startTime > refreshInterval) {
                                    startTime = currentTime;
                                    fireDownloadProgress(byteReadInloop);
                                    byteReadInloop = 0;
                                }
                                bytesDownloaded += bytesRead;
                                if (isCancel()) {
                                    return response;
                                }
                            }
                            bos.flush();
                            if (bytesDownloaded == contentLength) {
                                MavenArtifactsHandler deployer = new MavenArtifactsHandler();
                                deployer.install(downloadedFile.getAbsolutePath(), mavenUri, nServer.isOfficial());
                            }
                            fireDownloadComplete();
                        } finally {
                            if (bis != null) {
                                bis.close();
                            }
                            if (bos != null) {
                                bos.close();
                            }
                            if (tempFolder != null) {
                                FilesUtils.deleteFile(tempFolder, true);
                            }
                        }

                    }
                    return response;
                }

            }.doRequest(monitor, parseMvnUrl);
        }

    }

    private RepositoryContext getRepositoryContext() {
        Context ctx = CoreRuntimePlugin.getInstance().getContext();
        return (RepositoryContext) ctx.getProperty(Context.REPOSITORY_CONTEXT_KEY);
    }

    private String getTmpFolderPath() {
        Project project = ProjectManager.getInstance().getCurrentProject();
        String tmpFolder;
        try {
            IProject physProject = ResourceUtils.getProject(project);
            tmpFolder = physProject.getFolder("temp").getLocation().toPortableString(); //$NON-NLS-1$
        } catch (Exception e) {
            tmpFolder = System.getProperty("user.dir"); //$NON-NLS-1$
        }
        return tmpFolder;
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
     * Notify listeners about progress.
     * 
     * @param bytesRead
     */
    private void fireDownloadProgress(int bytesRead) {
        for (DownloadListener listener : fListeners) {
            listener.downloadProgress(this, bytesRead);
        }
    }

    /**
     * Notify listeners at the end of download process.
     */
    private void fireDownloadComplete() {
        for (DownloadListener listener : fListeners) {
            listener.downloadComplete();
        }
    }

    /**
     * Notify listeners at the begining of download process.
     */
    private void fireDownloadStart(int contentLength) {
        for (DownloadListener listener : fListeners) {
            listener.downloadStart(contentLength);
        }

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
}
