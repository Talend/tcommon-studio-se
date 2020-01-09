// ============================================================================
//
// Copyright (C) 2006-2020 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.updates.runtime.nexus.component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.commons.CommonsPlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.nexus.IRepositoryArtifactHandler;
import org.talend.core.nexus.RepositoryArtifactHandlerManager;
import org.talend.core.nexus.TalendMavenResolver;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.services.IMavenUIService;

/**
 * DOC cmeng class global comment. Detailled comment
 */
public class ComponentSyncManager {

    private static final String REPOSITORY_ID_RELEASE = "releases";

    private static final String REPOSITORY_ID_SNAPSHOT = "snapshots";

    private static final String SNAPSHOT_SUFFIX = "-SNAPSHOT";

    private static boolean isTalendDebug = CommonsPlugin.isDebugMode();

    private static Logger log = Logger.getLogger(ComponentSyncManager.class);

    private IRepositoryArtifactHandler releaseRepositoryHandler;

    private IRepositoryArtifactHandler snapshotRepositoryHandler;

    public ArtifactRepositoryBean getRepositoryServerBean(MavenArtifact artifact) {
        if (isSnapshot(artifact)) {
            return getSnapshotRepositoryHandler().getArtifactServerBean();
        } else {
            return getReleaseRepositoryHandler().getArtifactServerBean();
        }
    }

    public List<MavenArtifact> search(MavenArtifact artifact) throws Exception {
        if (isSnapshot(artifact)) {
            return searchSnapshot(artifact);
        } else {
            return searchRelease(artifact);
        }
    }

    private List<MavenArtifact> searchRelease(MavenArtifact artifact) throws Exception {
        return getReleaseRepositoryHandler().search(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(), true,
                false);
    }

    private List<MavenArtifact> searchSnapshot(MavenArtifact artifact) throws Exception {
        return getSnapshotRepositoryHandler().search(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(),
                false, true);
    }

    public void deploy(File file, MavenArtifact artifact) throws Exception {
        if (isSnapshot(artifact)) {
            deploySnapshot(file, artifact);
        } else {
            deployRelease(file, artifact);
        }
    }

    private void deployRelease(File file, MavenArtifact artifact) throws Exception {
        getReleaseRepositoryHandler().deploy(file, artifact.getGroupId(), artifact.getArtifactId(), artifact.getClassifier(),
                artifact.getType(), artifact.getVersion());
    }

    private void deploySnapshot(File file, MavenArtifact artifact) throws Exception {
        getSnapshotRepositoryHandler().deploy(file, artifact.getGroupId(), artifact.getArtifactId(), artifact.getClassifier(),
                artifact.getType(), artifact.getVersion());
    }

    public boolean isRepositoryServerAvailable(IProgressMonitor progress, MavenArtifact artifact) {
        IRepositoryArtifactHandler artifactHandler = null;
        boolean isSnapshot = isSnapshot(artifact);
        if (isSnapshot) {
            artifactHandler = getSnapshotRepositoryHandler();
        } else {
            artifactHandler = getReleaseRepositoryHandler();
        }
        if (artifactHandler == null) {
            debugLog("repository is not available. isSnapshot: " + isSnapshot);
            return false;
        }
        boolean isAvailable = artifactHandler.checkConnection(!isSnapshot, isSnapshot);
        debugLog("check artifact server connection result: " + isAvailable);
        return isAvailable;
    }

    /**
     * 
     * @throws FileNotFoundException if file is not exists on server
     */
    public File downloadIndexFile(IProgressMonitor progress, MavenArtifact artifact) throws Exception {
        IRepositoryArtifactHandler artifactHandler = null;
        boolean isSnapshot = isSnapshot(artifact);
        if (isSnapshot) {
            artifactHandler = getSnapshotRepositoryHandler();
        } else {
            artifactHandler = getReleaseRepositoryHandler();
        }
        ArtifactRepositoryBean artifactServerBean = artifactHandler.getArtifactServerBean();
        char[] passwordChars = null;
        String password = artifactServerBean.getPassword();
        if (password != null) {
            passwordChars = password.toCharArray();
        }

        /**
         * don't use mvn.resolve to get the index file here, since the resolved file may come from local mvn repository
         * instead of nexus server
         */
        final NexusComponentsTransport transport = new NexusComponentsTransport(artifactServerBean.getRepositoryURL(),
                artifactServerBean.getUserName(), passwordChars);
        boolean isAvailable = transport.isAvailable(progress, artifact);
        if (isAvailable) {
            File indexFile = File.createTempFile("index", ".xml"); //$NON-NLS-1$ //$NON-NLS-2$
            transport.downloadFile(progress, artifact, indexFile);
            return indexFile;
        } else {
            throw new FileNotFoundException("Index file is not exists on server: " + artifactServerBean);
        }
    }

    private boolean isSnapshot(MavenArtifact artifact) {
        return artifact.getVersion().endsWith(SNAPSHOT_SUFFIX);
    }

    private IRepositoryArtifactHandler getReleaseRepositoryHandler() {
        if (releaseRepositoryHandler == null) {
            ArtifactRepositoryBean artifactRepository = NexusServerManager.getInstance().getArtifactRepositoryFromTac();
            if (artifactRepository == null) {
                debugLog("Can't get artifactRepository from server.");
                return null;
            }
            String repoId = getReleaseRepositoryId();
            artifactRepository.setRepositoryId(repoId);
            artifactRepository.setSnapshotRepId(repoId);
            releaseRepositoryHandler = RepositoryArtifactHandlerManager.getRepositoryHandler(artifactRepository);
        }
        updateMavenSettings(releaseRepositoryHandler);
        return releaseRepositoryHandler;
    }

    private IRepositoryArtifactHandler getSnapshotRepositoryHandler() {
        if (snapshotRepositoryHandler == null) {
            ArtifactRepositoryBean artifactRepository = NexusServerManager.getInstance().getArtifactRepositoryFromTac();
            if (artifactRepository == null) {
                debugLog("Can't get artifactRepository from server.");
                return null;
            }
            String repoId = getSnapshotRepositoryId();
            artifactRepository.setRepositoryId(repoId);
            artifactRepository.setSnapshotRepId(repoId);
            snapshotRepositoryHandler = RepositoryArtifactHandlerManager.getRepositoryHandler(artifactRepository);
        }
        updateMavenSettings(snapshotRepositoryHandler);
        return snapshotRepositoryHandler;
    }

    private void updateMavenSettings(IRepositoryArtifactHandler repositoryHandler) {
        Dictionary<String, String> properties = new Hashtable<String, String>();
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IMavenUIService.class)) {
            IMavenUIService mavenUIService = (IMavenUIService) GlobalServiceRegister.getDefault()
                    .getService(IMavenUIService.class);
            if (mavenUIService != null) {
                properties = mavenUIService.getTalendMavenSetting();
            }
        }
        repositoryHandler.updateMavenResolver(TalendMavenResolver.COMPONENT_MANANGER_RESOLVER, properties);
    }

    private String getReleaseRepositoryId() {
        return REPOSITORY_ID_RELEASE;
    }

    private String getSnapshotRepositoryId() {
        return REPOSITORY_ID_SNAPSHOT;
    }

    private void debugLog(String message) {
        if (isTalendDebug) {
            log.info(message);
        }
    }

}
