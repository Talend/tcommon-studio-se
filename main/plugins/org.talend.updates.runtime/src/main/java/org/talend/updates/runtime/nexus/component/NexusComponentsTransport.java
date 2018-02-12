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
package org.talend.updates.runtime.nexus.component;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.nexus.HttpClientTransport;
import org.talend.core.nexus.NexusConstants;
import org.talend.core.nexus.NexusServerUtils;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.utils.PomUtil;

public class NexusComponentsTransport {

    private static final int BUFFER_SIZE = 8192;

    private String nexusURL, nexusUser;

    private char[] nexusPass;

    public NexusComponentsTransport(String nexusURL, String nexusUser, char[] nexusPass) {
        this.nexusURL = nexusURL;
        this.nexusUser = nexusUser;
        this.nexusPass = nexusPass;
    }

    protected String getNexusURL() {
        return nexusURL;
    }

    protected String getNexusUser() {
        return nexusUser;
    }

    public char[] getNexusPass() {
        return nexusPass;
    }

    protected String getNexusPassStr() {
        if (nexusPass == null) {
            return null;
        }
        return new String(nexusPass);

    }

    public NexusComponentsTransport(String nexusUrl) {
        this(nexusUrl, null, null);
    }

    public boolean isAvailable() {
        if (getNexusURL() == null || getNexusURL().isEmpty()) {
            return false;
        }
        return NexusServerUtils.checkConnectionStatus(getNexusURL(), getNexusUser(), getNexusPassStr());
    }

    public void downloadFile(IProgressMonitor monitor, String mvnURI, File target) throws Exception {
        MavenArtifact artifact = MavenUrlHelper.parseMvnUrl(mvnURI);
        downloadFile(monitor, artifact, target);
    }

    public void downloadFile(IProgressMonitor monitor, MavenArtifact artifact, File target) throws Exception {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        if (artifact == null) {
            return;
        }
        final String reletivePath = PomUtil.getArtifactPath(artifact);
        if (reletivePath == null) {
            return;
        }
        final boolean success[] = { false };

        URI targetURI = getTargetURI(getNexusURL(), reletivePath);
        new HttpClientTransport(getNexusURL(), getNexusUser(), getNexusPassStr()) {

            @Override
            protected HttpResponse execute(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI)
                    throws Exception {
                if (monitor.isCanceled()) {
                    throw new OperationCanceledException();
                }
                HttpResponse response = httpGet(monitor, httpClient, targetURI);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    // final String fileName = getFileName(response);
                    BufferedInputStream bis = null;
                    BufferedOutputStream bos = null;
                    try {
                        HttpEntity entity = response.getEntity();
                        InputStream inputStream = entity.getContent();

                        bis = new BufferedInputStream(inputStream);
                        bos = new BufferedOutputStream(new FileOutputStream(target));

                        byte[] buf = new byte[BUFFER_SIZE];
                        int bytesRead = -1;
                        while ((bytesRead = bis.read(buf)) != -1) {
                            if (monitor.isCanceled()) {
                                throw new OperationCanceledException();
                            }
                            bos.write(buf, 0, bytesRead);
                        }
                        bos.flush();
                        success[0] = true;
                    } finally {
                        //
                        if (bis != null) {
                            try {
                                bis.close();
                            } catch (IOException e) {
                                ExceptionHandler.process(e);
                            }
                        }

                        //
                        if (bos != null) {
                            try {
                                bos.close();
                            } catch (IOException e) {
                                ExceptionHandler.process(e);
                            }
                        }

                        // delete the failed file
                        if (!success[0] && target.exists() && target.isFile()) {
                            target.delete();
                        }

                    }
                }
                return response;
            }
        }.doRequest(monitor, targetURI);

    }

    private URI getTargetURI(String nexusUrl, String relativePath) throws URISyntaxException {
        if (!nexusUrl.endsWith(NexusConstants.SLASH)) {
            nexusUrl = nexusUrl + NexusConstants.SLASH;
        }
        return new URI(nexusUrl + relativePath);
    }

    public Document downloadXMLDocument(IProgressMonitor monitor, MavenArtifact artifact) throws Exception {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        if (artifact == null) {
            return null;
        }
        final String relativePath = PomUtil.getArtifactPath(artifact);
        if (relativePath == null) {
            return null;
        }

        URI requestURI = getTargetURI(getNexusURL(), relativePath);
        Document document = NexusServerUtils.downloadDocument(requestURI, getNexusUser(), getNexusPassStr());
        return document;

    }

    /**
     * 
     * DOC ggu Comment method "doHttpUpload".
     * 
     * upload the file to remote nexus, and base one the maven artifact.
     * 
     */
    public void doHttpUpload(IProgressMonitor monitor, final File uploadFile, final MavenArtifact artifact) throws Exception {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }
        if (uploadFile == null) {
            throw new IllegalArgumentException("Must provide the upload file, can't be null"); //$NON-NLS-1$
        }
        if (!uploadFile.exists() || !uploadFile.isFile()) {
            throw new IllegalArgumentException("The upload file is invalid: " + uploadFile); //$NON-NLS-1$
        }
        if (artifact == null) {
            throw new IllegalArgumentException("Must provide the maven artifact, can't be null"); //$NON-NLS-1$
        }

        // existed in nexus server, try to delete old one
        if (isAvailable(monitor, artifact)) {
            doHttpDelete(monitor, artifact);
        }

        new HttpClientTransport(getNexusURL(), getNexusUser(), getNexusPassStr()) {

            @Override
            protected HttpResponse execute(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI)
                    throws Exception {
                if (monitor.isCanceled()) {
                    throw new OperationCanceledException();
                }
                return httpPut(monitor, httpClient, targetURI, uploadFile);
            }
        }.doRequest(monitor, artifact);

        // upload one default pom also
        final MavenArtifact pomArtifact = artifact.clone();
        pomArtifact.setType(TalendMavenConstants.PACKAGING_POM);

        if (isAvailable(monitor, pomArtifact)) {
            doHttpDelete(monitor, pomArtifact);
        }

        new HttpClientTransport(getNexusURL(), getNexusUser(), getNexusPassStr()) {

            @Override
            protected HttpResponse execute(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI)
                    throws Exception {
                if (monitor.isCanceled()) {
                    throw new OperationCanceledException();
                }
                final File pomFile = new File(PomUtil.generatePom2(artifact));
                try {
                    return httpPut(monitor, httpClient, targetURI, pomFile);
                } finally {
                    pomFile.delete(); // finish to upload, delete the temp pom file.
                }
            }
        }.doRequest(monitor, pomArtifact);
    }

    protected HttpResponse httpPut(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI, File uploadFile)
            throws IOException {
        HttpPut httpPut = new HttpPut(targetURI.toString());
        FileEntity fileEntity = new FileEntity(uploadFile);
        httpPut.setEntity(fileEntity);

        HttpResponse execute = httpClient.execute(httpPut);

        EntityUtils.consume(fileEntity);
        return execute;
    }

    /**
     * 
     * DOC ggu Comment method "doHttpDelete".
     * 
     * delete/remove from nexus.
     */
    public void doHttpDelete(IProgressMonitor monitor, final MavenArtifact artifact) throws Exception {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }
        if (artifact == null) {
            throw new IllegalArgumentException("Must provide the maven artifact, can't be null"); //$NON-NLS-1$
        }

        if (isAvailable(monitor, artifact)) {
            new HttpClientTransport(getNexusURL(), getNexusUser(), getNexusPassStr()) {

                @Override
                protected HttpResponse execute(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI)
                        throws Exception {
                    if (monitor.isCanceled()) {
                        throw new OperationCanceledException();
                    }
                    return httpDelete(monitor, httpClient, targetURI);
                }
            }.doRequest(monitor, artifact);
        }

        // remove pom also
        final MavenArtifact pomArtifact = artifact.clone();
        pomArtifact.setType(TalendMavenConstants.PACKAGING_POM);
        if (isAvailable(monitor, pomArtifact)) {
            new HttpClientTransport(getNexusURL(), getNexusUser(), getNexusPassStr()) {

                @Override
                protected HttpResponse execute(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI)
                        throws Exception {
                    if (monitor.isCanceled()) {
                        throw new OperationCanceledException();
                    }
                    return httpDelete(monitor, httpClient, targetURI);
                }
            }.doRequest(monitor, pomArtifact);
        }
    }

    protected HttpResponse httpDelete(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI) throws IOException {
        HttpDelete httpDelete = new HttpDelete(targetURI);
        return httpClient.execute(httpDelete);
    }

    /**
     * 
     * DOC ggu Comment method "doHttpDownload".
     * 
     * download the maven artifact from the remote nexus to target file, if download falure, will remove the unfinished
     * file.
     */
    public void doHttpDownload(IProgressMonitor monitor, File targetFile, MavenArtifact artifact) throws Exception {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }
        if (targetFile == null) {
            throw new IllegalArgumentException("Must provide the target file, can't be null"); //$NON-NLS-1$
        }
        if (artifact == null) {
            throw new IllegalArgumentException("Must provide the download artifact, can't be null"); //$NON-NLS-1$
        }

        new HttpClientTransport(getNexusURL(), getNexusUser(), getNexusPassStr()) {

            @Override
            protected HttpResponse execute(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI)
                    throws Exception {
                if (monitor.isCanceled()) {
                    throw new OperationCanceledException();
                }
                HttpResponse response = httpGet(monitor, httpClient, targetURI);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    // final String fileName = getFileName(response);

                    BufferedOutputStream outStream = null;
                    boolean downloaded = false;
                    try {
                        HttpEntity entity = response.getEntity();
                        InputStream httpStream = entity.getContent();

                        outStream = new BufferedOutputStream(new FileOutputStream(targetFile));

                        downloaded = writeStream(monitor, httpStream, outStream);
                    } finally {
                        if (outStream != null) {
                            try {
                                outStream.close();
                            } catch (IOException e) {
                                //
                            }
                        }
                        if (!downloaded && targetFile.exists()) { // download failure
                            targetFile.delete(); // remove the wrong file.
                        }
                    }
                }
                return response;
            }
        }.doRequest(monitor, artifact);
    }

    protected HttpResponse httpGet(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI) throws IOException {
        HttpGet httpGet = new HttpGet(targetURI);
        return httpClient.execute(httpGet);
    }

    protected boolean writeStream(IProgressMonitor monitor, InputStream httpStream, OutputStream fileStream) throws IOException {
        boolean success = false;
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            int ch = 0;
            while ((ch = httpStream.read(buffer)) != -1) {
                fileStream.write(buffer, 0, ch);

                if (monitor.isCanceled()) {
                    throw new OperationCanceledException();
                }
            }
            fileStream.flush();

            success = true;
        } finally {
            if (fileStream != null) {
                try {
                    fileStream.close();
                } catch (IOException e) {
                    //
                }
            }
            if (httpStream != null) {
                try {
                    httpStream.close();
                } catch (IOException e) {
                    //
                }
            }
        }
        return success;
    }

    private String getFileName(HttpResponse response) {
        Header contentHeader = response.getFirstHeader("Content-Disposition");//$NON-NLS-1$
        String filename = null;
        if (contentHeader != null) {
            HeaderElement[] values = contentHeader.getElements();
            if (values.length == 1) {
                NameValuePair param = values[0].getParameterByName("filename"); //$NON-NLS-1$
                if (param != null) {
                    // filename = new String(param.getValue().toString().getBytes(), "utf-8");
                    // filename=URLDecoder.decode(param.getValue(),"utf-8");
                    filename = param.getValue();
                }
            }
        }
        return filename;
    }

    /**
     * 
     * DOC ggu Comment method "isAvailable".
     * 
     * check the maven artifact existed on remote nexus or not.
     * 
     * @param monitor
     * @param artifact
     * @return
     */
    public boolean isAvailable(IProgressMonitor monitor, final MavenArtifact artifact) {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }
        if (artifact == null) {
            return false;
        }

        try {
            new HttpClientTransport(getNexusURL(), getNexusUser(), getNexusPassStr()) {

                @Override
                protected HttpResponse execute(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI)
                        throws Exception {
                    if (monitor.isCanceled()) {
                        throw new OperationCanceledException();
                    }
                    HttpResponse response = httpGet(monitor, httpClient, targetURI);
                    final int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == HttpStatus.SC_OK) { // 200
                        // ok
                    } else if (statusCode == HttpStatus.SC_BAD_REQUEST) { // 400?
                        throw new BusinessException("The URI is not existed ");
                    }

                    return response;
                }
            }.doRequest(monitor, artifact);

            return true; // no any exception?
        } catch (Exception e) {
            if (CommonsPlugin.isDebugMode()) {
                ExceptionHandler.process(e);
            }
        }
        return false;
    }

}
