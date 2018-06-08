// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
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
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.eclipse.m2e.core.MavenPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.nexus.IRepositoryArtifactHandler;
import org.talend.core.nexus.NexusConstants;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.designer.maven.aether.RepositorySystemFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * created by wchen on May 16, 2018 Detailled comment
 *
 */
public class ArtifacoryRepositoryHandler extends AbstractArtifactRepositoryHandler {

    private String SEARCH_SERVICE = "api/search/gavc?";

    private String SEARCH_RESULT_PREFIX = "api/storage/";

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
        String serverUrl = serverBean.getServer();
        if (!serverUrl.endsWith("/")) {
            serverUrl = serverUrl + "/";
        }
        String searchUrl = serverUrl + SEARCH_SERVICE;

        String repositoryId = "";
        if (fromRelease) {
            repositoryId = serverBean.getRepositoryId();
        }
        if (fromSnapshot) {
            if ("".equals(repositoryId)) {
                repositoryId = serverBean.getSnapshotRepId();
            } else {
                repositoryId = repositoryId + "," + serverBean.getSnapshotRepId();
            }
        }
        String query = "";//$NON-NLS-1$
        if (!"".equals(repositoryId)) {
            query = "repos=" + repositoryId;//$NON-NLS-1$
        }
        if (groupIdToSearch != null) {
            if (!"".equals(query)) {
                query = query + "&";
            }
            query = query + "g=" + groupIdToSearch;//$NON-NLS-1$
        }
        if (artifactId != null) {
            if (!"".equals(query)) {
                query = query + "&";
            }
            query = query + "a=" + artifactId;//$NON-NLS-1$
        }

        if (versionToSearch != null) {
            if (!"".equals(query)) {
                query = query + "&";
            }
            query = query + "v=" + versionToSearch;//$NON-NLS-1$
        }
        searchUrl = searchUrl + query;
        Request request = Request.Get(searchUrl);
        String userPass = serverBean.getUserName() + ":" + serverBean.getPassword();
        String basicAuth = "Basic " + new String(new Base64().encode(userPass.getBytes()));
        Header authority = new BasicHeader("Authorization", basicAuth);
        request.addHeader(authority);
        List<MavenArtifact> resultList = new ArrayList<MavenArtifact>();

        HttpResponse response = request.execute().returnResponse();
        String content = EntityUtils.toString(response.getEntity());
        if (content.isEmpty()) {
            return resultList;
        }
        JSONObject responseObject = new JSONObject().fromObject(content);
        String resultStr = responseObject.getString("results");
        JSONArray resultArray = null;
        try {
            resultArray = new JSONArray().fromObject(resultStr);
        } catch (Exception e) {
            throw new Exception(resultStr);
        }
        if (resultArray != null) {
            String resultUrl = serverUrl + SEARCH_RESULT_PREFIX;
            for (int i = 0; i < resultArray.size(); i++) {
                JSONObject jsonObject = resultArray.getJSONObject(i);
                String uri = jsonObject.getString("uri");
                uri = uri.substring(resultUrl.length(), uri.length());
                String[] split = uri.split("/");
                if (split.length > 4) {
                    String fileName = split[split.length - 1];
                    if (!fileName.endsWith("pom")) {
                        String type = null;
                        int dotIndex = fileName.lastIndexOf('.');
                        if (dotIndex > 0) {
                            type = fileName.substring(dotIndex + 1);

                        }
                        if (type != null) {
                            MavenArtifact artifact = new MavenArtifact();
                            String v = split[split.length - 2];
                            String a = split[split.length - 3];
                            String g = "";
                            for (int j = 1; j < split.length - 3; j++) {
                                if ("".equals(g)) {
                                    g = split[j];
                                } else {
                                    g = g + "." + split[j];
                                }
                            }
                            artifact.setGroupId(g);
                            artifact.setArtifactId(a);
                            artifact.setVersion(v);
                            artifact.setType(type);
                            resultList.add(artifact);
                        }
                    }

                }

            }
        }

        return resultList;
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.nexus.IRepositoryArtifactHandler#deployWithPOM(java.io.File, java.io.File, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.librariesmanager.nexus.AbstractArtifactRepositoryHandler#getRepositoryPrefixPath()
     */
    @Override
    protected String getRepositoryPrefixPath() {
        return NexusConstants.SLASH;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.librariesmanager.nexus.AbstractArtifactRepositoryHandler#clone()
     */
    @Override
    public IRepositoryArtifactHandler clone() {
        return new ArtifacoryRepositoryHandler();
    }

}
