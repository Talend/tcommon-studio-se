// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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
import org.talend.core.model.general.ModuleNeeded.ELibraryInstallStatus;
import org.talend.core.model.general.ModuleStatusProvider;
import org.talend.core.nexus.NexusConstants;
import org.talend.core.nexus.NexusServerBean;
import org.talend.core.nexus.TalendLibsServerManager;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.utils.io.FilesUtils;

/**
 * created by wchen on 2015-5-14 Detailled comment
 *
 */
public class ArtifactsDeployer {

    private static final String SLASH = "/";//$NON-NLS-1$ 

    private boolean remoteNexusChecked = false;

    private NexusServerBean nexusServer;

    private String repositoryUrl;

    public ArtifactsDeployer() {
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
            try {
                deployToLocalMaven(mavenUri, jarSourceAndMavenUri.get(mavenUri), null, true);
            } catch (Exception e) {
                ExceptionHandler.process(e);
                continue;
            }
        }
    }

    public void deployToLocalMaven(Map<String, String> jarSourceAndMavenUri, boolean updateRemoteJar) throws Exception {
        for (String mavenUri : jarSourceAndMavenUri.keySet()) {
            try {
                deployToLocalMaven(mavenUri, jarSourceAndMavenUri.get(mavenUri), null, updateRemoteJar);
            } catch (Exception e) {
                ExceptionHandler.process(e);
                continue;
            }
        }
    }

    public void deployToLocalMaven(String path, String mavenUri, boolean toRemoteNexus) throws Exception {
        deployToLocalMaven(mavenUri, path, null, toRemoteNexus);
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

    /**
     * Deploy the lib with fixed pom file. if not set, will generate a default one.
     */
    public void deployToLocalMaven(String mavenUri, String libPath, String pomPath, boolean toRemoteNexus) throws Exception {
        MavenArtifact parseMvnUrl = MavenUrlHelper.parseMvnUrl(mavenUri);
        // change to snapshot version to deploy

        if (parseMvnUrl != null && libPath != null && libPath.length() > 0) {
            File libFile = new File(libPath);
            if (!libFile.exists()) {
                return;
            }
            // lib
            String artifactType = parseMvnUrl.getType();
            if (artifactType == null || "".equals(artifactType)) {
                artifactType = TalendMavenConstants.PACKAGING_JAR;
            }
            MavenResolver mvnResolver = TalendLibsServerManager.getInstance().getMavenResolver();
            mvnResolver.upload(parseMvnUrl.getGroupId(), parseMvnUrl.getArtifactId(), parseMvnUrl.getClassifier(), artifactType,
                    parseMvnUrl.getVersion(), libFile);

            ModuleStatusProvider.getDeployStatusMap().put(mavenUri, ELibraryInstallStatus.DEPLOYED);
            ModuleStatusProvider.getStatusMap().put(mavenUri, ELibraryInstallStatus.INSTALLED);

            // pom
            boolean generated = false;
            File pomFile = null;
            if (pomPath != null && pomPath.length() > 0) {
                pomFile = new File(pomPath);
                if (!pomFile.exists()) {
                    pomFile = null;
                }
            }
            if (pomFile == null) {
                pomFile = new File(PomUtil.generatePom2(parseMvnUrl));
                generated = true;
            }

            String pomType = TalendMavenConstants.PACKAGING_POM;
            if (pomFile != null && pomFile.exists()) {
                mvnResolver.upload(parseMvnUrl.getGroupId(), parseMvnUrl.getArtifactId(), parseMvnUrl.getClassifier(), pomType,
                        parseMvnUrl.getVersion(), pomFile);
            }

            if (toRemoteNexus) {
                installToRemote(libFile, parseMvnUrl, artifactType);
                // deploy the pom
                if (pomFile != null && pomFile.exists()) {
                    installToRemote(pomFile, parseMvnUrl, pomType);
                }
            }
            if (generated) { // only for generate pom
                FilesUtils.deleteFolder(pomFile.getParentFile(), true);
            }
        }

    }

    public void installToRemote(File content, MavenArtifact artifact, String type) throws Exception {
        if (nexusServer == null) {
            initCustomNexus();
        }
        if (nexusServer == null) {
            return;
        }
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
            String target = repositoryUrl;
            if (artifact.getVersion() != null && artifact.getVersion().endsWith(MavenConstants.SNAPSHOT)) {
                target = target + nexusServer.getSnapshotRepId() + NexusConstants.SLASH;
            } else {
                target = target + nexusServer.getRepositoryId() + NexusConstants.SLASH;
            }
            target = target + artifactPath;
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

    private void initCustomNexus() {
        if (!remoteNexusChecked) {
            remoteNexusChecked = true;
            nexusServer = TalendLibsServerManager.getInstance().getCustomNexusServer();
            if (nexusServer != null) {
                String server = nexusServer.getServer();
                if (server.endsWith(NexusConstants.SLASH)) {
                    server = server.substring(0, server.length() - 1);
                }
                repositoryUrl = server + NexusConstants.CONTENT_REPOSITORIES;
            }
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
