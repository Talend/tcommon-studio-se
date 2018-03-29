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
package org.talend.core.nexus;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.params.ConnRoutePNames;
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

    private static final String PROP_PROXY_HTTP_CLIENT_USE_DEFAULT_SETTINGS = "talend.proxy.HttpClient.useDefaultSettings"; //$NON-NLS-1$

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

        /**
         * Normally the default proxySelector is EclipseProxySelector
         */
        final ProxySelector proxySelector = AccessController.doPrivileged(new PrivilegedAction<ProxySelector>() {

            @Override
            public ProxySelector run() {
                return ProxySelector.getDefault();
            }
        });
        try {
            if (StringUtils.isNotBlank(username)) { // set username
                httpClient.getCredentialsProvider().setCredentials(new AuthScope(requestURI.getHost(), requestURI.getPort()),
                        new UsernamePasswordCredentials(username, password));
            }

            configureProxy(httpClient, requestURI);
            HttpResponse response = execute(monitor, httpClient, requestURI);

            processResponseCode(response);
        } catch (org.apache.http.conn.HttpHostConnectException e) {
            // connection failure
            throw e;
        } catch (Exception e) {
            throw new Exception(requestURI.toString(), e);
        } finally {
            httpClient.getConnectionManager().shutdown();
            AccessController.doPrivileged(new PrivilegedAction<Void>() {

                @Override
                public Void run() {
                    ProxySelector.setDefault(proxySelector);
                    return null;
                }
            });
        }
    }

    private void configureProxy(final DefaultHttpClient httpClient, URI requestURI) {
        if (Boolean.valueOf(System.getProperty(PROP_PROXY_HTTP_CLIENT_USE_DEFAULT_SETTINGS, Boolean.FALSE.toString()))) {
            return;
        }
        /**
         * Get proxy configured in Eclipse preference
         */
        ProxySelector proxySelector = AccessController.doPrivileged(new PrivilegedAction<ProxySelector>() {

            @Override
            public ProxySelector run() {
                return ProxySelector.getDefault();
            }
        });
        final List<Proxy> proxyList = proxySelector.select(requestURI);
        Proxy usedProxy = null;
        if (proxyList != null && !proxyList.isEmpty()) {
            usedProxy = proxyList.get(0);
        }

        if (usedProxy != null) {
            if (Type.DIRECT.equals(usedProxy.type())) {
                return;
            }
            final Proxy finalProxy = usedProxy;
            InetSocketAddress address = (InetSocketAddress) finalProxy.address();
            PasswordAuthentication proxyAuthentication = Authenticator.requestPasswordAuthentication(address.getHostName(),
                    address.getAddress(), address.getPort(), "Http Proxy", "Http proxy authentication", null);
            String proxyUser = proxyAuthentication.getUserName();
            String proxyPassword = new String(proxyAuthentication.getPassword());
            String proxyServer = address.getHostName();
            int proxyPort = address.getPort();
            httpClient.getCredentialsProvider().setCredentials(new AuthScope(proxyServer, proxyPort),
                    new UsernamePasswordCredentials(proxyUser, proxyPassword));
            HttpHost proxyHost = new HttpHost(proxyServer, proxyPort);
            httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxyHost);

            /**
             * set default proxySelector to null, since the EclipseProxySelector will use SOCKS proxy instead of HTTP
             */
            AccessController.doPrivileged(new PrivilegedAction<Void>() {

                @Override
                public Void run() {
                    ProxySelector.setDefault(null);
                    return null;
                }
            });
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
