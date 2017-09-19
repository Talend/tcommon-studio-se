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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.INexusService;
import org.talend.core.nexus.IRepositoryArtifactHandler;
import org.talend.core.nexus.NexusConstants;
import org.talend.core.nexus.NexusServerUtils;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.designer.maven.utils.PomUtil;

/**
 * created by wchen on Aug 2, 2017 Detailled comment
 *
 */
public class Nexus2RepositoryHandler extends AbstractArtifactRepositoryHandler {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.nexus.IArtifactHandler#checkConnection(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)
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
        boolean releaseStatus = false;
        boolean snapshotStatus = false;
        if (checkRelease && serverBean.getRepositoryId() != null) {
            releaseStatus = NexusServerUtils.checkConnectionStatus(serverBean.getServer(), serverBean.getRepositoryId(),
                    serverBean.getUserName(), serverBean.getPassword());
        }
        if (checkSnapshot && serverBean.getSnapshotRepId() != null) {
            snapshotStatus = NexusServerUtils.checkConnectionStatus(serverBean.getServer(), serverBean.getSnapshotRepId(),
                    serverBean.getUserName(), serverBean.getPassword());
        }
        boolean result = (checkRelease ? releaseStatus : true) && (checkSnapshot ? snapshotStatus : true);
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.nexus.IArtifactHandler#search(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public List<MavenArtifact> search(String groupIdToSearch, String artifactId, String versionToSearch, boolean fromRelease,
            boolean fromSnapshot) throws Exception {
        List<MavenArtifact> results = new ArrayList<MavenArtifact>();
        if (fromRelease && serverBean.getRepositoryId() != null) {
            results.addAll(NexusServerUtils.search(serverBean.getServer(), serverBean.getUserName(), serverBean.getPassword(),
                    serverBean.getRepositoryId(), groupIdToSearch, artifactId, versionToSearch));
        }
        if (fromSnapshot && serverBean.getSnapshotRepId() != null) {
            results.addAll(NexusServerUtils.search(serverBean.getServer(), serverBean.getUserName(), serverBean.getPassword(),
                    serverBean.getSnapshotRepId(), groupIdToSearch, artifactId, versionToSearch));
        }
        return results;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.nexus.IArtifactHandler#install(java.io.File, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void deploy(File content, String groupId, String artifactId, String classifier, String extension, String version)
            throws IOException {
        try {
            // TODO keep the nexus2 work as before, but need to remove the delete code latter
            MavenArtifact artifact = new MavenArtifact();
            artifact.setArtifactId(artifactId);
            artifact.setGroupId(groupId);
            artifact.setClassifier(classifier);
            artifact.setType(version);
            artifact.setVersion(version);
            deleteOldEntity(artifact, extension);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        if (GlobalServiceRegister.getDefault().isServiceRegistered(INexusService.class)) {
            INexusService nexusService = (INexusService) GlobalServiceRegister.getDefault().getService(INexusService.class);
            nexusService.upload(serverBean, groupId, artifactId, version, content.toURI().toURL());
        }
    }

    @Override
    protected String getRepositoryPrefixPath() {
        return NexusConstants.CONTENT_REPOSITORIES;
    }

    @Override
    public IRepositoryArtifactHandler clone() {
        return new Nexus2RepositoryHandler();
    }

    private void deleteOldEntity(MavenArtifact artifact, String type) throws Exception {
        String target = null;
        if (artifact.getVersion() != null && !artifact.getVersion().endsWith(MavenConstants.SNAPSHOT)) {
            target = getRepositoryURL(true);
        }
        if (target == null) {
            return;
        }
        String artifactPath = PomUtil.getArtifactPath(artifact);
        if (!artifactPath.endsWith(type)) {
            if (artifactPath.lastIndexOf(".") != -1) {
                artifactPath = artifactPath.substring(0, artifactPath.lastIndexOf(".") + 1) + type;
            } else {
                artifactPath = artifactPath + "." + type;
            }
        }

        target = target + artifactPath;
        URL targetURL = new URL(target);

        DefaultHttpClient httpClient = new DefaultHttpClient();
        try {
            HttpHead httpHead = null;
            HttpResponse response = null;
            StatusLine statusLine = null;
            if (targetURL.getFile() != null && !targetURL.getFile().endsWith("SNAPSHOT.jar")) {
                httpClient.getCredentialsProvider().setCredentials(new AuthScope(targetURL.getHost(), targetURL.getPort()),
                        new UsernamePasswordCredentials(serverBean.getUserName(), serverBean.getPassword()));
                httpHead = new HttpHead(targetURL.toString());
                response = httpClient.execute(httpHead);
                statusLine = response.getStatusLine();
                int responseResult = statusLine.getStatusCode();
                if (responseResult == 200) {
                    HttpDelete httpDelete = new HttpDelete(targetURL.toString());
                    httpClient.execute(httpDelete);
                }
            }
        } catch (Exception e) {
            throw new Exception(targetURL.toString(), e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

}
