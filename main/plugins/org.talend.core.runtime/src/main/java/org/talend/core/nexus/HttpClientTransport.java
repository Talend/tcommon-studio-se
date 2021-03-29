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
package org.talend.core.nexus;

import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URI;
import java.net.URISyntaxException;
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
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.utils.network.IProxySelectorProvider;
import org.talend.commons.utils.network.TalendProxySelector;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.designer.core.IDesignerCoreService;

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

    public void doRequest(IProgressMonitor monitor, final URI requestURI) throws Exception {
        int retries = 5;
        long waitMillis = 20000;

        int timeout = NexusServerUtils.getTimeout();
        boolean fTimeout = false;

        for(int t = 1; t <= retries; t++){
            try{
                fTimeout = false;
                doRequestOne(monitor, requestURI);
            } catch (java.net.SocketTimeoutException e){
                // Read timed out
                fTimeout = true;
                if(t == retries){
                    throw new Exception(e);
                }
            } catch (Exception e) {
                // DEBUG //
                System.err.printf("[%d] Exception occured for %s\n", t, requestURI);
                e.printStackTrace(System.err);
                // DEBUG //
                throw e;
            }
            if(!fTimeout){
                break;
            }
            // DEBUG //
            System.err.printf("[%d] Read timeout (in %d millisecs) occured for %s\n", t, timeout, requestURI);
            // DEBUG //

            Thread.sleep(waitMillis * t);
        }
    }

    public void doRequestOne(IProgressMonitor monitor, final URI requestURI) throws Exception {
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

        IProxySelectorProvider proxySelectorProvider = null;
        try {
            if (StringUtils.isNotBlank(username)) { // set username
                httpClient.getCredentialsProvider().setCredentials(new AuthScope(requestURI.getHost(), requestURI.getPort()),
                        new UsernamePasswordCredentials(username, password));
            }
            int timeout = NexusServerUtils.getTimeout();
            IDesignerCoreService designerCoreService = CoreRuntimePlugin.getInstance().getDesignerCoreService();
            if (designerCoreService != null) {
                timeout = designerCoreService.getTACConnectionTimeout() * 1000;
            }
            HttpParams params = httpClient.getParams();
            params.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
            params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);

            proxySelectorProvider = addProxy(httpClient, requestURI);
            HttpResponse response = execute(monitor, httpClient, requestURI);

            processResponseCode(response);
        } catch (org.apache.http.conn.HttpHostConnectException e) {
            // connection failure
            throw e;
        } catch (java.net.SocketTimeoutException e){
            // Read timed out
            throw e;
        } catch (Exception e) {
            throw new Exception(requestURI.toString(), e);
        } finally {
            httpClient.getConnectionManager().shutdown();
            removeProxy(proxySelectorProvider);
        }
    }

    public static IProxySelectorProvider addProxy(final DefaultHttpClient httpClient, URI requestURI) {
        IProxySelectorProvider proxySelectorProvider = null;
        try {
            if (Boolean.valueOf(System.getProperty(PROP_PROXY_HTTP_CLIENT_USE_DEFAULT_SETTINGS, Boolean.FALSE.toString()))) {
                return proxySelectorProvider;
            }
            final List<Proxy> proxyList = TalendProxySelector.getInstance().getDefaultProxySelector().select(requestURI);
            Proxy usedProxy = null;
            if (proxyList != null && !proxyList.isEmpty()) {
                usedProxy = proxyList.get(0);
            }

            if (usedProxy != null) {
                if (Type.DIRECT.equals(usedProxy.type())) {
                    return proxySelectorProvider;
                }
                final Proxy finalProxy = usedProxy;
                InetSocketAddress address = (InetSocketAddress) finalProxy.address();
                String proxyServer = address.getHostString();
                int proxyPort = address.getPort();
                TalendProxySelector proxySelector = TalendProxySelector.getInstance();
                PasswordAuthentication proxyAuthentication = proxySelector.getHttpPasswordAuthentication();
                if (proxyAuthentication != null) {
                    String proxyUser = proxyAuthentication.getUserName();
                    if(StringUtils.isNotBlank(proxyUser)){
                        String proxyPassword = "";
                        char[] passwordChars = proxyAuthentication.getPassword();
                        if (passwordChars != null) {
                            proxyPassword = new String(passwordChars);
                        }
                        httpClient.getCredentialsProvider().setCredentials(new AuthScope(proxyServer, proxyPort),
                                new UsernamePasswordCredentials(proxyUser, proxyPassword));
                    }
                }
                HttpHost proxyHost = new HttpHost(proxyServer, proxyPort);
                httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxyHost);
                proxySelectorProvider = proxySelector.createDefaultProxySelectorProvider();
            }
            return proxySelectorProvider;
        } finally {
            if (proxySelectorProvider != null) {
                TalendProxySelector.getInstance().addProxySelectorProvider(proxySelectorProvider);
            }
        }
    }

    public static void removeProxy(IProxySelectorProvider proxySelectorProvider) {
        if (proxySelectorProvider != null) {
            TalendProxySelector.getInstance().removeProxySelectorProvider(proxySelectorProvider);
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
