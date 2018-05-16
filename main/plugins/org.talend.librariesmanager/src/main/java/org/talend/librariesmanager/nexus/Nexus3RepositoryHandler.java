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
package org.talend.librariesmanager.nexus;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.eclipse.m2e.core.MavenPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.nexus.IRepositoryArtifactHandler;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.designer.maven.aether.RepositorySystemFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * created by wchen on Aug 2, 2017 Detailled comment
 *
 */
public class Nexus3RepositoryHandler extends AbstractArtifactRepositoryHandler {

    private String SEARCH_SERVICE = "service/rest/v1/script/search/run";

    private String REP_PREFIX_PATH = "/repository/";

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
        Request request = Request.Post(searchUrl);
        String userPass = serverBean.getUserName() + ":" + serverBean.getPassword();
        String basicAuth = "Basic " + new String(new Base64().encode(userPass.getBytes()));
        Header authority = new BasicHeader("Authorization", basicAuth);
        Header contentType = new BasicHeader("Content-Type", "text/plain");
        request.addHeader(contentType);
        request.addHeader(authority);
        List<MavenArtifact> resultList = new ArrayList<MavenArtifact>();
        if (fromRelease) {
            resultList.addAll(doSearch(request, serverBean.getRepositoryId(), groupIdToSearch, artifactId, versionToSearch));
        }
        if (fromSnapshot) {
            resultList.addAll(doSearch(request, serverBean.getSnapshotRepId(), groupIdToSearch, artifactId, versionToSearch));
        }

        return resultList;
    }

    private List<MavenArtifact> doSearch(Request request, String repositoryId, String groupIdToSearch, String artifactId,
            String versionToSearch) throws Exception {
        List<MavenArtifact> resultList = new ArrayList<MavenArtifact>();
        JSONObject body = new JSONObject();
        body.put("repositoryId", repositoryId);
        if (groupIdToSearch != null) {
            body.put("g", groupIdToSearch);
        }
        if (artifactId != null) {
            body.put("a", artifactId);
        }
        if (versionToSearch != null) {
            body.put("v", versionToSearch);
        }
        request.bodyString(body.toString(),
                ContentType.create(ContentType.APPLICATION_JSON.getMimeType(), StandardCharsets.UTF_8));
        HttpResponse response = request.execute().returnResponse();
        String content = EntityUtils.toString(response.getEntity());
        if (content.isEmpty()) {
            return resultList;
        }
        JSONObject responseObject = new JSONObject().fromObject(content);
        String resultStr = responseObject.getString("result");
        JSONArray resultArray = null;
        try {
            resultArray = new JSONArray().fromObject(resultStr);
        } catch (Exception e) {
            throw new Exception(resultStr);
        }
        if (resultArray != null) {
            for (int i = 0; i < resultArray.size(); i++) {
                JSONObject jsonObject = resultArray.getJSONObject(i);
                MavenArtifact artifact = new MavenArtifact();
                artifact.setGroupId(jsonObject.getString("groupId"));
                artifact.setArtifactId(jsonObject.getString("artifactId"));
                artifact.setVersion(jsonObject.getString("version"));
                artifact.setType(jsonObject.getString("extension"));
                artifact.setDescription(jsonObject.getString("description"));
                artifact.setLastUpdated(jsonObject.getString("last_updated"));
                // artifact.setLicense(jsonObject.getString("license"));
                // artifact.setLicenseUrl(jsonObject.getString("licenseUrl"));
                // artifact.setUrl(jsonObject.getString("url"));
                resultList.add(artifact);
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
