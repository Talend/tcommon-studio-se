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
import java.util.ArrayList;
import java.util.List;

import org.eclipse.m2e.core.MavenPlugin;
import org.talend.core.nexus.IRepositoryArtifactHandler;
import org.talend.core.nexus.NexusConstants;
import org.talend.core.nexus.NexusServerUtils;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.designer.maven.aether.RepositorySystemFactory;

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
        return NexusConstants.CONTENT_REPOSITORIES;
    }

    @Override
    public IRepositoryArtifactHandler clone() {
        return new Nexus2RepositoryHandler();
    }

    /* (non-Javadoc)
     * @see org.talend.core.nexus.IRepositoryArtifactHandler#deployWithPOM(java.io.File, java.io.File, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
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
        RepositorySystemFactory.deployWithPOM(content, pomFile, localRepository, repositoryId, repositoryurl, serverBean.getUserName(),
                serverBean.getPassword(), groupId, artifactId, classifier, extension, version);
    }

}
