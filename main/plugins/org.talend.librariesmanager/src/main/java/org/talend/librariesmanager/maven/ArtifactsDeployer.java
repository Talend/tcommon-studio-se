// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.librariesmanager.maven;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.ops4j.pax.url.mvn.MavenResolver;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.nexus.NexusConstants;
import org.talend.core.nexus.NexusServerBean;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.talendlib.TalendLibsServerManager;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.utils.io.FilesUtils;

/**
 * created by wchen on 2015-5-14 Detailled comment
 *
 */
public class ArtifactsDeployer {

    private static final String SLASH = "/";//$NON-NLS-1$ 

    private NexusServerBean nexusServer;

    private String repositoryUrl;

    public ArtifactsDeployer() {
        init();
    }

    private void init() {
        nexusServer = TalendLibsServerManager.getInstance().getCustomNexusServer();
        if (nexusServer != null) {
            String server = nexusServer.getServer();
            if (server.endsWith(NexusConstants.SLASH)) {
                server = server.substring(0, server.length() - 1);
            }
            repositoryUrl = server + NexusConstants.CONTENT_REPOSITORIES + nexusServer.getRepositoryId() + NexusConstants.SLASH;
        }
    }

    /**
     * 
     * DOC Talend Comment method "deployToLocalMaven".
     * 
     * @param jarSourceAndMavenUri a map with key : can be a filePath or platform uri , value :maven uri
     * @throws Exception
     */
    public void deployToLocalMaven(Map<String, String> jarSourceAndMavenUri) throws Exception {
        for (String mavenUri : jarSourceAndMavenUri.keySet()) {
            deployToLocalMaven(jarSourceAndMavenUri.get(mavenUri), mavenUri);
        }
    }

    public void deployToLocalMaven(String path, String mavenUri, boolean toRemoteNexus) throws Exception {
        MavenArtifact parseMvnUrl = MavenUrlHelper.parseMvnUrl(mavenUri);
        // change to snapshot version to deploy

        if (parseMvnUrl != null) {
            // install to local maven repository and create pom
            // repositoryManager.install(new File(path), parseMvnUrl);
            String artifactType = parseMvnUrl.getType();
            if (artifactType == null || "".equals(artifactType)) {
                artifactType = TalendMavenConstants.PACKAGING_JAR;
            }
            MavenResolver mvnResolver = TalendLibsServerManager.getInstance().getMavenResolver();
            mvnResolver.upload(parseMvnUrl.getGroupId(), parseMvnUrl.getArtifactId(), parseMvnUrl.getClassifier(), artifactType,
                    parseMvnUrl.getVersion(), new File(path));
            String pomType = TalendMavenConstants.PACKAGING_POM;
            String generatePom = PomUtil.generatePom(parseMvnUrl);
            if (generatePom != null) {
                mvnResolver.upload(parseMvnUrl.getGroupId(), parseMvnUrl.getArtifactId(), parseMvnUrl.getClassifier(), pomType,
                        parseMvnUrl.getVersion(), new File(generatePom));
            }

            // deploy to nexus server if it is not null and not official server
            if (toRemoteNexus && nexusServer != null && !nexusServer.isOfficial()) {
                // repositoryManager.deploy(new File(path), parseMvnUrl);
                installToRemote(new File(path), parseMvnUrl, artifactType);
                // deploy the pom
                if (new File(generatePom).exists()) {
                    installToRemote(new File(generatePom), parseMvnUrl, pomType);
                }
            }
            FilesUtils.deleteFolder(new File(generatePom).getParentFile(), true);
        }

    }

    /**
     * 
     * DOC Talend Comment method "deployToLocalMaven".
     * 
     * @param uriOrPath can be a filePath or platform uri
     * @param mavenUri maven uri
     * @throws Exception
     */
    public void deployToLocalMaven(String path, String mavenUri) throws Exception {
        deployToLocalMaven(path, mavenUri, true);
    }

    public void installToRemote(File content, MavenArtifact artifact, String type) throws Exception {
        URL targetURL;
        try {
            String artifactPath = PomUtil.getArtifactPath(artifact);
            if (!artifactPath.endsWith(type)) {
                if (artifactPath.lastIndexOf(".") != -1) {
                    artifactPath = artifactPath.substring(0, artifactPath.lastIndexOf(".") + 1) + type;
                } else {
                    artifactPath = artifactPath + "." + type;
                }
            }

            String target = repositoryUrl + artifactPath;
            targetURL = new URL(target);
            installToRemote(new FileEntity(content), targetURL);
        } catch (MalformedURLException e) {
            ExceptionHandler.process(e);
        }
    }

    private void installToRemote(HttpEntity entity, URL targetURL) throws Exception {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        try {
            httpClient.getCredentialsProvider().setCredentials(new AuthScope(targetURL.getHost(), targetURL.getPort()),
                    new UsernamePasswordCredentials(nexusServer.getUserName(), nexusServer.getPassword()));

            HttpPut httpPut = new HttpPut(targetURL.toString());
            httpPut.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPut);
            StatusLine statusLine = response.getStatusLine();
            int responseCode = statusLine.getStatusCode();
            EntityUtils.consume(entity);
            if (responseCode > 399) {
                if (responseCode == 500) {
                    // ignor this error , if .pom already exist on server and deploy again will get this error
                } else if (responseCode == 401) {
                    throw new BusinessException("Authrity failed");
                } else {
                    throw new BusinessException("Deploy failed: " + responseCode + ' ' + statusLine.getReasonPhrase());
                }
            }
        } catch (Exception e) {
            throw new Exception(targetURL.toString(), e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    // private void install(String path, MavenArtifact artifact) {
    // StringBuffer command = new StringBuffer();
    // // mvn -Dfile=E:\studio_code\.metadata\aaabbbb\lib\java\ojdbc6.jar -DgroupId=org.talend.libraries
    // // -DartifactId=ojdbc6 -Dversion=1.0.0 -Dpackaging=jar
    // // -B install:install-file
    // command.append(" mvn ");
    // command.append(" -Dfile=");
    // command.append(path);
    // command.append(" -DgroupId=");
    // command.append(artifact.getGroupId());
    // command.append(" -DartifactId=");
    // command.append(artifact.getArtifactId());
    // command.append(" -Dversion=");
    // command.append(artifact.getVersion());
    // command.append(" -Dpackaging=");
    // command.append(artifact.getType());
    // command.append(" -B install:install-file");
    // try {
    // Runtime.getRuntime().exec("cmd /c " + command.toString());
    // } catch (IOException e) {
    // ExceptionHandler.process(e);
    // }
    // }

}
