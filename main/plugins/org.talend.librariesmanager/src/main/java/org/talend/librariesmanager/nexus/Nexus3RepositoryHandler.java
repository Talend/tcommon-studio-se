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
package org.talend.librariesmanager.nexus;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.log4j.Logger;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.network.IProxySelectorProvider;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.nexus.HttpClientTransport;
import org.talend.core.nexus.IRepositoryArtifactHandler;
import org.talend.core.nexus.NexusServerUtils;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.designer.maven.aether.RepositorySystemFactory;
import org.talend.librariesmanager.i18n.Messages;
import org.talend.librariesmanager.nexus.nexus3.handler.AbsNexus3SearchHandler;
import org.talend.librariesmanager.nexus.nexus3.handler.INexus3SearchHandler;
import org.talend.librariesmanager.nexus.nexus3.handler.Nexus3BetaSearchHandler;
import org.talend.librariesmanager.nexus.nexus3.handler.Nexus3ScriptSearchHandler;
import org.talend.librariesmanager.nexus.nexus3.handler.Nexus3V1SearchHandler;
import org.talend.utils.sugars.TypedReturnCode;

/**
 * created by wchen on Aug 2, 2017 Detailled comment
 *
 */
public class Nexus3RepositoryHandler extends AbstractArtifactRepositoryHandler {

    private static Logger LOGGER = Logger.getLogger(Nexus3RepositoryHandler.class);

    private String REP_PREFIX_PATH = "/repository/";

    private ThreadLocal<INexus3SearchHandler> currentQueryHandlerCopy = new ThreadLocal<INexus3SearchHandler>();

    private static final ConcurrentHashMap<ArtifactRepositoryBean, INexus3SearchHandler> LAST_HANDLER_MAP = new ConcurrentHashMap<ArtifactRepositoryBean, INexus3SearchHandler>();

    @Override
    public IRepositoryArtifactHandler clone() {
        return new Nexus3RepositoryHandler();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.nexus.IRepositoryArtifactHandler#checkConnection()
     */
    @Override
    public boolean checkConnection() {
        return checkConnection(true, true);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.nexus.IRepositoryArtifactHandler#checkConnection(boolean, boolean)
     */
    @Override
    public boolean checkConnection(boolean checkRelease, boolean checkSnapshot) {
        boolean connectionOk = true;
        try {
            if (checkRelease) {
                connectionOk = doConnectionCheck(getRepositoryURL(true));
            }
            if (checkSnapshot && connectionOk) {
                connectionOk = doConnectionCheck(getRepositoryURL(false));
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
            connectionOk = false;
        }
        return connectionOk;
    }

    private boolean doConnectionCheck(String repositoryUrl) throws ClientProtocolException, IOException {
        String userPass = serverBean.getUserName() + ":" + serverBean.getPassword();
        String basicAuth = "Basic " + new String(new Base64().encode(userPass.getBytes()));
        Header authority = new BasicHeader("Authorization", basicAuth);
        HttpGet get = new HttpGet(repositoryUrl);
        get.addHeader(authority);
        DefaultHttpClient httpclient = new DefaultHttpClient();

        httpclient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, NexusServerUtils.getTimeout());
        httpclient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, NexusServerUtils.getTimeout());
        IProxySelectorProvider proxySelector = null;
        try {
            try {
                URI uri = new URI(repositoryUrl);
                uri.toURL();
                proxySelector = HttpClientTransport.addProxy(httpclient, new URI(repositoryUrl));
            } catch (Exception e) {
                ExceptionHandler.process(e);
                return false;
            }
            HttpResponse response = httpclient.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                return true;
            }
            return false;
        } finally {
            HttpClientTransport.removeProxy(proxySelector);
            httpclient.getConnectionManager().shutdown();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.nexus.IRepositoryArtifactHandler#search(java.lang.String, java.lang.String,
     * java.lang.String, boolean, boolean)
     */
    @Override
    public List<MavenArtifact> search(String groupIdToSearch, String artifactId, String versionToSearch, boolean fromRelease,
            boolean fromSnapshot) throws Exception {

        List<MavenArtifact> resultList = new ArrayList<MavenArtifact>();
        if (fromRelease) {
            resultList.addAll(doSearch(serverBean.getRepositoryId(), groupIdToSearch, artifactId, versionToSearch));
        }
        if (fromSnapshot) {
            resultList.addAll(doSearch(serverBean.getSnapshotRepId(), groupIdToSearch, artifactId, versionToSearch));
        }

        return resultList;
    }

    private List<MavenArtifact> doSearch(String repositoryId, String groupIdToSearch, String artifactId, String versionToSearch)
            throws Exception {
        INexus3SearchHandler currentQueryHandler = currentQueryHandlerCopy.get();
        if (currentQueryHandler == null) {
            currentQueryHandler = LAST_HANDLER_MAP.get(serverBean);
            if (currentQueryHandler == null) {
                currentQueryHandler = createQueryHandler(0);
            }
        }
        List<MavenArtifact> result = new ArrayList<MavenArtifact>();
        try {
            result = currentQueryHandler.search(repositoryId, groupIdToSearch, artifactId, versionToSearch);
        } catch (Exception ex) {
            for (int i = 0; i < 3; i++) {// Try to other version
                INexus3SearchHandler handler = createQueryHandler(i);
                if (handler != currentQueryHandler) {
                    try {
                        result = handler.search(repositoryId, groupIdToSearch, artifactId, versionToSearch);
                        currentQueryHandler = handler;
                        LOGGER.info(
                                "Switch to new search handler,the handler version is:" + currentQueryHandler.getHandlerVersion());
                        break;
                    } catch (Exception e) {
                        LOGGER.info("Try to switch search handler failed" + e.getMessage());
                    }
                }
            }
        }
        currentQueryHandlerCopy.set(currentQueryHandler);
        LAST_HANDLER_MAP.put(serverBean, currentQueryHandler);
        return result;
    }

    private AbsNexus3SearchHandler createQueryHandler(int seq) {
        AbsNexus3SearchHandler retHandler;
        switch (seq) {
        case 1:
            retHandler = new Nexus3BetaSearchHandler(serverBean);
            break;
        case 2:
            retHandler = new Nexus3ScriptSearchHandler(serverBean);
            break;
        default:
            retHandler = new Nexus3V1SearchHandler(serverBean);
        }
        return retHandler;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.nexus.IRepositoryArtifactHandler#deploy(java.io.File, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void deploy(File content, String groupId, String artifactId, String classifier, String extension, String version)
            throws Exception {
        String repositoryId = "";
        boolean isRelease = !version.endsWith(MavenUrlHelper.VERSION_SNAPSHOT);
        if (isRelease) {
            repositoryId = serverBean.getRepositoryId();
        } else {
            repositoryId = serverBean.getSnapshotRepId();
        }
        String repositoryurl = getRepositoryURL(isRelease);
        String localRepository = getLocalRepositoryPath();
        RepositorySystemFactory.deploy(content, localRepository, repositoryId, repositoryurl, serverBean.getUserName(),
                serverBean.getPassword(), groupId, artifactId, classifier, extension, version);

    }

    @Override
    protected String getRepositoryPrefixPath() {
        return REP_PREFIX_PATH;
    }

    @Override
    public void deployWithPOM(File content, File pomFile, String groupId, String artifactId, String classifier, String extension,
            String version) throws Exception {
        String repositoryId = "";
        boolean isRelease = !version.endsWith(MavenUrlHelper.VERSION_SNAPSHOT);
        if (isRelease) {
            repositoryId = serverBean.getRepositoryId();
        } else {
            repositoryId = serverBean.getSnapshotRepId();
        }
        String repositoryurl = getRepositoryURL(isRelease);
        String localRepository = getLocalRepositoryPath();
        RepositorySystemFactory.deployWithPOM(content, pomFile, localRepository, repositoryId, repositoryurl,
                serverBean.getUserName(), serverBean.getPassword(), groupId, artifactId, classifier, extension, version);

    }

    @Override
    public TypedReturnCode<HttpResponse> getConnectionResultAndCode() {
        HttpResponse response = null;
        TypedReturnCode<HttpResponse> rc = new TypedReturnCode<HttpResponse>();
        rc.setOk(false);
        try {
            response = getConnectionResponse(getRepositoryURL(true));
            if (200 == response.getStatusLine().getStatusCode()) {
                rc.setOk(true);
                rc.setObject(response);
                rc.setMessage(Messages.getString("NexusRepository.checkConnection.successMsg"));
                return rc;
            }
            rc.setMessage(response.getStatusLine().toString());
        } catch (Exception e) {
            rc.setOk(false);
            rc.setMessage(e.getMessage());
            return rc;
        }
        return rc;
    }

    private HttpResponse getConnectionResponse(String repositoryUrl) throws ClientProtocolException, IOException {
        String userPass = serverBean.getUserName() + ":" + serverBean.getPassword();
        String basicAuth = "Basic " + new String(new Base64().encode(userPass.getBytes()));
        Header authority = new BasicHeader("Authorization", basicAuth);
        HttpGet get = new HttpGet(repositoryUrl);
        get.addHeader(authority);
        DefaultHttpClient httpclient = new DefaultHttpClient();

        httpclient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, NexusServerUtils.getTimeout());
        httpclient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, NexusServerUtils.getTimeout());
        IProxySelectorProvider proxySelector = null;
        try {
            try {
                proxySelector = HttpClientTransport.addProxy(httpclient, new URI(repositoryUrl));
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
            HttpResponse response = httpclient.execute(get);
            return response;
        } finally {
            HttpClientTransport.removeProxy(proxySelector);
            httpclient.getConnectionManager().shutdown();
        }
    }

    public List<MavenArtifact> search(String name, boolean fromSnapshot) throws Exception {
        List<MavenArtifact> resultList = new ArrayList<MavenArtifact>();
        resultList.addAll(doSearch(serverBean.getRepositoryId(), name));
        if (fromSnapshot) {
            resultList.addAll(doSearch(serverBean.getSnapshotRepId(), name));
        }

        return resultList;
    }

    private List<MavenArtifact> doSearch(String repositoryId, String name) throws Exception {
        INexus3SearchHandler currentQueryHandler = currentQueryHandlerCopy.get();
        if (currentQueryHandler == null) {
            currentQueryHandler = createQueryHandler(0);
        }
        List<MavenArtifact> result = new ArrayList<MavenArtifact>();
        try {
            result = currentQueryHandler.search(repositoryId, name);
        } catch (Exception ex) {
            for (int i = 0; i < 3; i++) {// Try to other version
                INexus3SearchHandler handler = createQueryHandler(i);
                if (handler != currentQueryHandler) {
                    try {
                        result = handler.search(repositoryId, name);
                        currentQueryHandler = handler;
                        LOGGER.info(
                                "Switch to new search handler,the handler version is:" + currentQueryHandler.getHandlerVersion());
                        break;
                    } catch (Exception e) {
                        LOGGER.info("Try to switch search handler failed" + e.getMessage());
                    }
                }
            }
        }
        currentQueryHandlerCopy.set(currentQueryHandler);
        return result;
    }

}
