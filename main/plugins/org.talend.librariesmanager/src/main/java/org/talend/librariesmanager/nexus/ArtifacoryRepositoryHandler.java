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
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.network.IProxySelectorProvider;
import org.talend.core.nexus.HttpClientTransport;
import org.talend.core.nexus.IRepositoryArtifactHandler;
import org.talend.core.nexus.NexusConstants;
import org.talend.core.nexus.NexusServerUtils;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.designer.maven.aether.RepositorySystemFactory;
import org.talend.librariesmanager.i18n.Messages;
import org.talend.librariesmanager.nexus.utils.ShareLibrariesUtil;
import org.talend.utils.sugars.TypedReturnCode;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * created by wchen on May 16, 2018 Detailled comment
 *
 */
public class ArtifacoryRepositoryHandler extends AbstractArtifactRepositoryHandler {

    private static Logger LOGGER = Logger.getLogger(ArtifacoryRepositoryHandler.class);

    private String SEARCH_SERVICE = "api/search/gavc?"; //$NON-NLS-1$

    private static final String SEARCH_NAME = "api/search/artifact?";
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
        String userPass = serverBean.getUserName() + ":" + serverBean.getPassword(); //$NON-NLS-1$
        String basicAuth = "Basic " + new String(new Base64().encode(userPass.getBytes())); //$NON-NLS-1$
        Header authority = new BasicHeader("Authorization", basicAuth); //$NON-NLS-1$
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

    private HttpResponse getConnectionResponse(String repositoryUrl)
            throws ClientProtocolException, IOException {
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

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.nexus.IRepositoryArtifactHandler#search(java.lang.String, java.lang.String,
     * java.lang.String, boolean, boolean)
     */
    @Override
    public List<MavenArtifact> search(String groupIdToSearch, String artifactId, String versionToSearch, boolean fromRelease,
            boolean fromSnapshot) throws Exception {
        return requestSearch(groupIdToSearch, artifactId, versionToSearch, fromRelease, fromSnapshot, false);
    }

    protected List<MavenArtifact> requestSearch(String groupIdToSearch, String artifactId, String versionToSearch,
            boolean fromRelease, boolean fromSnapshot, boolean useSnapshotVersion) throws Exception {
        String serverUrl = serverBean.getServer();
        if (!serverUrl.endsWith("/")) { //$NON-NLS-1$
            serverUrl = serverUrl + "/"; //$NON-NLS-1$
        }
        String searchUrl = serverUrl + SEARCH_SERVICE;

        String repositoryId = ""; //$NON-NLS-1$
        if (fromRelease) {
            repositoryId = serverBean.getRepositoryId();
        }
        if (fromSnapshot) {
            if ("".equals(repositoryId)) { //$NON-NLS-1$
                repositoryId = serverBean.getSnapshotRepId();
            } else {
                repositoryId = repositoryId + "," + serverBean.getSnapshotRepId(); //$NON-NLS-1$
            }
        }
        String query = "";//$NON-NLS-1$
        if (!"".equals(repositoryId)) { //$NON-NLS-1$
            query = "repos=" + repositoryId;//$NON-NLS-1$
        }
        if (groupIdToSearch != null) {
            if (!"".equals(query)) { //$NON-NLS-1$
                query = query + "&"; //$NON-NLS-1$
            }
            query = query + "g=" + groupIdToSearch;//$NON-NLS-1$
        }
        if (artifactId != null) {
            if (!"".equals(query)) { //$NON-NLS-1$
                query = query + "&"; //$NON-NLS-1$
            }
            query = query + "a=" + artifactId;//$NON-NLS-1$
        }

        if (versionToSearch != null) {
            if (!"".equals(query)) { //$NON-NLS-1$
                query = query + "&"; //$NON-NLS-1$
            }
            query = query + "v=" + versionToSearch;//$NON-NLS-1$
        }
        List<MavenArtifact> resultList = new ArrayList<MavenArtifact>();
        searchUrl = searchUrl + query;
        String content = doRequest(searchUrl);//
        if (content.isEmpty()) {
            return resultList;
        }
        JSONObject responseObject = JSONObject.fromObject(content);
        String resultStr = responseObject.getString("results"); //$NON-NLS-1$
        JSONArray resultArray = null;
        try {
            resultArray = JSONArray.fromObject(resultStr);
        } catch (Exception e) {
            throw new Exception(resultStr);
        }
        if (resultArray != null) {
            for (int i = 0; i < resultArray.size(); i++) {
                JSONObject jsonObject = resultArray.getJSONObject(i);
                String lastUpdated = jsonObject.getString("lastUpdated"); //$NON-NLS-1$
                String artifactPath = jsonObject.getString("path"); //$NON-NLS-1$
                String[] split = artifactPath.split("/"); //$NON-NLS-1$
                if (split.length > 4) {
                    String fileName = split[split.length - 1];
                    if (!fileName.endsWith("pom")) { //$NON-NLS-1$
                        String type = null;
                        int dotIndex = fileName.lastIndexOf('.');
                        if (dotIndex > 0) {
                            type = fileName.substring(dotIndex + 1);
                        }
                        if (type != null) {
                            MavenArtifact artifact = new MavenArtifact();
                            String g = ""; //$NON-NLS-1$
                            String a = split[split.length - 3];
                            String v = split[split.length - 2];
                            if (fromSnapshot && useSnapshotVersion) {
                                String jarName = split[split.length - 1];
                                if (jarName.contains("-")) {
                                    v = jarName.substring(jarName.indexOf("-") + 1);
                                    v = v.substring(0, v.lastIndexOf("."));
                                } else {
                                    if (CommonsPlugin.isDebugMode()) {
                                        ExceptionHandler
                                                .process(new Exception("the jar name is not the usual style: " + jarName));
                                    }
                                }
                            }
                            for (int j = 1; j < split.length - 3; j++) {
                                if ("".equals(g)) { //$NON-NLS-1$
                                    g = split[j];
                                } else {
                                    g = g + "." + split[j]; //$NON-NLS-1$
                                }
                            }
                            artifact.setGroupId(g);
                            artifact.setArtifactId(a);
                            artifact.setVersion(v);
                            artifact.setType(type);
                            artifact.setLastUpdated(lastUpdated);
                            String regex = a + "-" + v;
                            String classifier = ShareLibrariesUtil.getMavenClassifier(artifactPath, regex, type);
                            artifact.setClassifier(classifier);
                            fillChecksumData(jsonObject, artifact);
                            resultList.add(artifact);
                        }
                    }
                }
            }
        }
        return resultList;
    }

    protected List<MavenArtifact> doSearch(String query, String apiPath, boolean fromRelease, boolean fromSnapshot)
            throws Exception {
        String q = query;
        if (q == null || q.trim().isEmpty()) {
            q = "";
        }
        String serverUrl = serverBean.getServer();
        if (!serverUrl.endsWith("/")) { //$NON-NLS-1$
            serverUrl = serverUrl + "/"; //$NON-NLS-1$
        }
        String searchUrl = serverUrl + apiPath;

        String repositoryId = ""; //$NON-NLS-1$
        if (fromRelease) {
            repositoryId = serverBean.getRepositoryId();
        }
        if (fromSnapshot) {
            if ("".equals(repositoryId)) { //$NON-NLS-1$
                repositoryId = serverBean.getSnapshotRepId();
            } else {
                repositoryId = repositoryId + "," + serverBean.getSnapshotRepId(); //$NON-NLS-1$
            }
        }
        if (!"".equals(repositoryId)) { //$NON-NLS-1$
            if (!q.isEmpty()) {
                q += "&";
            }
            q += "repos=" + repositoryId;//$NON-NLS-1$
        }
        List<MavenArtifact> resultList = new ArrayList<MavenArtifact>();
        searchUrl = searchUrl + q;
        String content = doRequest(searchUrl);//
        if (content.isEmpty()) {
            return resultList;
        }
        JSONObject responseObject = JSONObject.fromObject(content);
        String resultStr = responseObject.getString("results"); //$NON-NLS-1$
        JSONArray resultArray = null;
        try {
            resultArray = JSONArray.fromObject(resultStr);
        } catch (Exception e) {
            throw new Exception(resultStr);
        }
        if (resultArray != null) {
            for (int i = 0; i < resultArray.size(); i++) {
                JSONObject jsonObject = resultArray.getJSONObject(i);
                String lastUpdated = jsonObject.getString("lastUpdated"); //$NON-NLS-1$
                String artifactPath = jsonObject.getString("path"); //$NON-NLS-1$
                String[] split = artifactPath.split("/"); //$NON-NLS-1$
                if (split.length > 4) {
                    String fileName = split[split.length - 1];
                    if (!fileName.endsWith("pom")) { //$NON-NLS-1$
                        String type = null;
                        int dotIndex = fileName.lastIndexOf('.');
                        if (dotIndex > 0) {
                            type = fileName.substring(dotIndex + 1);
                        }
                        if (type != null) {
                            MavenArtifact artifact = new MavenArtifact();
                            String g = ""; //$NON-NLS-1$
                            String a = split[split.length - 3];
                            String v = split[split.length - 2];
                            for (int j = 1; j < split.length - 3; j++) {
                                if ("".equals(g)) { //$NON-NLS-1$
                                    g = split[j];
                                } else {
                                    g = g + "." + split[j]; //$NON-NLS-1$
                                }
                            }
                            artifact.setGroupId(g);
                            artifact.setArtifactId(a);
                            artifact.setVersion(v);
                            artifact.setType(type);
                            artifact.setLastUpdated(lastUpdated);
                            String regex = a + "-" + v;
                            String classifier = ShareLibrariesUtil.getMavenClassifier(artifactPath, regex, type);
                            artifact.setClassifier(classifier);
                            fillChecksumData(jsonObject, artifact);
                            resultList.add(artifact);
                        }
                    }
                }
            }
        }
        return resultList;
    }

    @Override
    public File resolve(MavenArtifact ma) throws Exception {
        boolean isRelease = true;
        String version = ma.getVersion();
        if (StringUtils.isNotBlank(version)) {
            isRelease = !version.endsWith("-" + MavenUrlHelper.VERSION_SNAPSHOT);
        }
        List<MavenArtifact> result = requestSearch(ma.getGroupId(), ma.getArtifactId(), ma.getVersion(), isRelease, !isRelease,
                true);
        if (result == null || result.isEmpty()) {
            return null;
        }
        List<MavenArtifact> sortResult = new LinkedList<>(result);
        sortResult.sort((a1, a2) -> {
            return -1 * MavenArtifact.compareVersion(a1.getVersion(), a2.getVersion());
        });
        return resolve(sortResult.get(0), isRelease);
    }

    private void fillChecksumData(JSONObject responseObject, MavenArtifact artifact) {
        if (responseObject.containsKey("checksums")) { //$NON-NLS-1$
            JSONObject checkSum = responseObject.getJSONObject("checksums"); //$NON-NLS-1$
            if (checkSum != null && checkSum.containsKey("sha1")) { //$NON-NLS-1$
                artifact.setSha1(checkSum.getString("sha1")); //$NON-NLS-1$
            }
            if (checkSum != null && checkSum.containsKey("md5")) { //$NON-NLS-1$
                artifact.setMd5(checkSum.getString("md5")); //$NON-NLS-1$
            }
        }
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
        String repositoryId = ""; //$NON-NLS-1$
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

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.nexus.IRepositoryArtifactHandler#deployWithPOM(java.io.File, java.io.File, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void deployWithPOM(File content, File pomFile, String groupId, String artifactId, String classifier, String extension,
            String version) throws Exception {
        String repositoryId = ""; //$NON-NLS-1$
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
            rc.setMessage(response.getStatusLine().getReasonPhrase());
        } catch (Exception e) {
            rc.setOk(false);
            rc.setMessage(e.getMessage());
            return rc;
        }
        return rc;
    }

    public List<MavenArtifact> search(String name, boolean fromSnapshot) throws Exception {
        String query = "name=" + name;
        return doSearch(query, SEARCH_NAME, true, fromSnapshot);
    }

    public String doRequest(final String url) throws URISyntaxException, Exception {
        final StringBuffer sb = new StringBuffer();
        new HttpClientTransport(url, serverBean.getUserName(), serverBean.getPassword()) {

            @Override
            protected HttpResponse execute(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI)
                    throws Exception {
                HttpGet httpGet = new HttpGet(targetURI);
                String userName = serverBean.getUserName();
                if (StringUtils.isNotBlank(userName) && !Boolean.getBoolean("talend.studio.search.nexus3.disableBasicAuth")) {
                    String auth = userName + ":" + serverBean.getPassword();
                    String authHeader = "Basic " + new String(Base64.encodeBase64(auth.getBytes(StandardCharsets.UTF_8)));
                    httpGet.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
                    Header resultDetailHeader = new BasicHeader("X-Result-Detail", "info"); //$NON-NLS-1$ //$NON-NLS-2$
                    httpGet.addHeader(resultDetailHeader);
                    httpClient.setCredentialsProvider(null);
                }
                HttpResponse response = httpClient.execute(httpGet);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    sb.append(EntityUtils.toString(response.getEntity()));
                } else {
                    LOGGER.info("Try to search " + url + " failed!");
                    throw new Exception(response.toString());
                }
                return response;
            }

        }.doRequest(null, new URI(url));
        return sb.toString();
    }
}
