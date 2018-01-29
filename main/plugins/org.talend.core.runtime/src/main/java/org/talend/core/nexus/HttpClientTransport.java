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
package org.talend.core.nexus;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.talend.commons.exception.BusinessException;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class HttpClientTransport {

    private String baseURI;

    private String username, password;

    public HttpClientTransport(String baseURI, String username, String password) {
        super();
        this.baseURI = baseURI;
        this.username = username;
        this.password = password;
    }

    public URI createURI(MavenArtifact artifact) throws URISyntaxException {
        if (artifact == null) {
            return null;
        }
        // like https://talend-update.talend.com/nexus/content/repositories/components/
        String baseRepoURI = baseURI;
        if (baseRepoURI == null) {
            throw new IllegalArgumentException("Must provide the nexus base repository uri");
        }
        String artifactPath = MavenUrlHelper.getArtifactPath(artifact);
        if (artifactPath == null) {
            return null;
        }
        if (!baseRepoURI.endsWith(NexusConstants.SLASH)) {
            baseRepoURI += NexusConstants.SLASH;
        }
        final URI uri = new URI(baseRepoURI + artifactPath);
        return uri;
    }

    public void doRequest(IProgressMonitor monitor, MavenArtifact artifact) throws Exception {
        doRequest(monitor, createURI(artifact));
    }

    public void doRequest(IProgressMonitor monitor, URI requestURI) throws Exception {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }
        if (requestURI == null) {
            return;
        }
        DefaultHttpClient httpClient = new DefaultHttpClient();
        try {
            if (StringUtils.isNotBlank(username)) { // set username
                httpClient.getCredentialsProvider().setCredentials(new AuthScope(requestURI.getHost(), requestURI.getPort()),
                        new UsernamePasswordCredentials(username, password));
            }
            HttpResponse response = execute(monitor, httpClient, requestURI);

            processResponseCode(response);
        } catch (org.apache.http.conn.HttpHostConnectException e) {
            // connection failure
            throw e;
        } catch (Exception e) {
            throw new Exception(requestURI.toString(), e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    public void processResponseCode(HttpResponse response) throws BusinessException {
        StatusLine statusLine = response.getStatusLine();
        int responseCode = statusLine.getStatusCode();
        if (responseCode > 399) {
            if (responseCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) { // 500
                // ignore this error , if already exist on server and deploy again will get this error
            } else if (responseCode == HttpStatus.SC_BAD_REQUEST || // 400
                    responseCode == HttpStatus.SC_NOT_FOUND) { // 404
                throw new BusinessException(Integer.toString(responseCode) + ':' + statusLine.getReasonPhrase());
            } else if (responseCode == HttpStatus.SC_UNAUTHORIZED) { // 401
                throw new BusinessException("Authrity failed");
            } else {
                throw new BusinessException("Do request failed: " + responseCode + ' ' + statusLine.getReasonPhrase());
            }
        }
    }

    protected abstract HttpResponse execute(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI)
            throws Exception;

}
