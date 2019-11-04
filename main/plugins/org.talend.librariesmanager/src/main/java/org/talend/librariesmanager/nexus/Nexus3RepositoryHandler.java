// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.log4j.Logger;
import org.eclipse.m2e.core.MavenPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.nexus.IRepositoryArtifactHandler;
import org.talend.core.nexus.NexusServerUtils;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.designer.maven.aether.RepositorySystemFactory;
import org.talend.librariesmanager.nexus.nexus3.handler.INexus3SearchHandler;
import org.talend.librariesmanager.nexus.nexus3.handler.Nexus3BetaSearchHandler;
import org.talend.librariesmanager.nexus.nexus3.handler.Nexus3ScriptSearchHandler;
import org.talend.librariesmanager.nexus.nexus3.handler.Nexus3V1SearchHandler;

/**
 * created by wchen on Aug 2, 2017 Detailled comment
 *
 */
public class Nexus3RepositoryHandler extends AbstractArtifactRepositoryHandler {

    private static Logger LOGGER = Logger.getLogger(Nexus3RepositoryHandler.class);

    private String REP_PREFIX_PATH = "/repository/";

    private INexus3SearchHandler currentQueryHandler = null;

    private static List<INexus3SearchHandler> queryHandlerList = new ArrayList<INexus3SearchHandler>();

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
        
        HttpResponse response = httpclient.execute(get);
        if (response.getStatusLine().getStatusCode() == 200) {
            return true;
        }
        return false;
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
        if (currentQueryHandler == null) {
            currentQueryHandler = getQueryHandler();
        }
        List<MavenArtifact> result = new ArrayList<MavenArtifact>();
        try {
            result = currentQueryHandler.search(repositoryId, groupIdToSearch, artifactId, versionToSearch);
        } catch (Exception ex) {
            for (int i = 0; i < queryHandlerList.size(); i++) {// Try to other version
                INexus3SearchHandler handler = queryHandlerList.get(i);
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
        return result;
    }

    private INexus3SearchHandler getQueryHandler() {
        if (queryHandlerList.size() == 0) {
            queryHandlerList.add(new Nexus3V1SearchHandler(serverBean));
            queryHandlerList.add(new Nexus3BetaSearchHandler(serverBean));
            queryHandlerList.add(new Nexus3ScriptSearchHandler(serverBean));
        }
        return queryHandlerList.get(0);
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
        String localRepository = MavenPlugin.getMaven().getLocalRepositoryPath();
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
        String localRepository = MavenPlugin.getMaven().getLocalRepositoryPath();
        RepositorySystemFactory.deployWithPOM(content, pomFile, localRepository, repositoryId, repositoryurl,
                serverBean.getUserName(), serverBean.getPassword(), groupId, artifactId, classifier, extension, version);

    }

}
