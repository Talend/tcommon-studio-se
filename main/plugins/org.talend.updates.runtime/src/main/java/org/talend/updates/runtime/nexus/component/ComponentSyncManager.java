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
package org.talend.updates.runtime.nexus.component;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.talend.commons.CommonsPlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.nexus.IRepositoryArtifactHandler;
import org.talend.core.nexus.RepositoryArtifactHandlerManager;
import org.talend.core.nexus.TalendMavenResolver;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.core.runtime.services.IMavenUIService;

/**
 * DOC cmeng class global comment. Detailled comment
 */
public class ComponentSyncManager {

    private static final String SNAPSHOT_SUFFIX = "-" + MavenUrlHelper.VERSION_SNAPSHOT;

    private static boolean isTalendDebug = CommonsPlugin.isDebugMode();

    private static Logger log = Logger.getLogger(ComponentSyncManager.class);

    private IRepositoryArtifactHandler releaseRepositoryHandler;

    private IRepositoryArtifactHandler snapshotRepositoryHandler;

    private ArtifactRepositoryBean serverBean;

    public ComponentSyncManager(ArtifactRepositoryBean serverBean) {
        this.serverBean = serverBean;
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

    public boolean isRepositoryServerAvailable(IProgressMonitor progress, MavenArtifact artifact) throws Exception {
        if (getServerBean() == null) {
            return false;
        }
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

    public File resolve(IProgressMonitor monitor, MavenArtifact artifact) throws Exception {
        IProgressMonitor progress = monitor;
        if (progress == null) {
            progress = new NullProgressMonitor();
        }
        IRepositoryArtifactHandler artifactHandler = null;
        boolean isSnapshot = isSnapshot(artifact);
        if (isSnapshot) {
            artifactHandler = getSnapshotRepositoryHandler();
        } else {
            artifactHandler = getReleaseRepositoryHandler();
        }

        return artifactHandler.resolve(artifact);
    }

    /**
     * 
     * @throws FileNotFoundException if file is not exists on server
     */
    public File downloadIndexFile(IProgressMonitor monitor, MavenArtifact artifact) throws Exception {
        IProgressMonitor progress = monitor;
        if (progress == null) {
            progress = new NullProgressMonitor();
        }
        IRepositoryArtifactHandler artifactHandler = null;
        boolean isSnapshot = isSnapshot(artifact);
        if (isSnapshot) {
            artifactHandler = getSnapshotRepositoryHandler();
        } else {
            artifactHandler = getReleaseRepositoryHandler();
        }

        ArtifactRepositoryBean artifactServerBean = artifactHandler.getArtifactServerBean();
        File indexFile = null;
        if (isSnapshot) {
            File originalFile = artifactHandler.resolve(artifact);
            if (originalFile != null) {
                indexFile = File.createTempFile("index", ".xml"); //$NON-NLS-1$ //$NON-NLS-2$
                indexFile.delete();
                Files.copy(originalFile.toPath(), indexFile.toPath());
            }
        } else {
            char[] passwordChars = null;
            String password = artifactServerBean.getPassword();
            if (password != null) {
                passwordChars = password.toCharArray();
            }

            /**
             * don't use mvn.resolve to get the index file here, since the resolved file may come from local mvn
             * repository instead of nexus server
             */
            final NexusComponentsTransport transport = new NexusComponentsTransport(artifactServerBean.getRepositoryURL(),
                    artifactServerBean.getUserName(), passwordChars);
            boolean isAvailable = transport.isAvailable(progress, artifact);
            if (isAvailable) {
                indexFile = File.createTempFile("index", ".xml"); //$NON-NLS-1$ //$NON-NLS-2$
                transport.downloadFile(progress, artifact, indexFile);
            }
        }
        if (indexFile == null) {
            throw new FileNotFoundException("Index file is not exists on server: " + artifactServerBean);
        }
        return indexFile;
    }

    private boolean isSnapshot(MavenArtifact artifact) {
        return artifact.getVersion().endsWith(SNAPSHOT_SUFFIX);
    }

    private IRepositoryArtifactHandler getReleaseRepositoryHandler() throws Exception {
        if (releaseRepositoryHandler == null) {
            ArtifactRepositoryBean artifactRepository = getServerBean().clone();
            if (artifactRepository == null) {
                debugLog("Can't get artifactRepository from server.");
                return null;
            }
            String repoId = getReleaseRepositoryId();
            artifactRepository.setRepositoryId(repoId);
            artifactRepository.setSnapshotRepId(repoId);
            releaseRepositoryHandler = RepositoryArtifactHandlerManager.getRepositoryHandler(artifactRepository);
            File localRepositoryFolder = Files.createTempDirectory("ReleaseLocalRepository_").toFile();
            String localRepositoryPath = localRepositoryFolder.getCanonicalPath();
            debugLog("ReleaseLocalRepository: " + localRepositoryPath);
            releaseRepositoryHandler.setLocalRepositoryPath(localRepositoryPath);
            localRepositoryFolder.deleteOnExit();
        }
        updateMavenSettings(releaseRepositoryHandler);
        return releaseRepositoryHandler;
    }

    private IRepositoryArtifactHandler getSnapshotRepositoryHandler() throws Exception {
        if (snapshotRepositoryHandler == null) {
            ArtifactRepositoryBean artifactRepository = getServerBean().clone();
            if (artifactRepository == null) {
                debugLog("Can't get artifactRepository from server.");
                return null;
            }
            String repoId = getSnapshotRepositoryId();
            artifactRepository.setRepositoryId(repoId);
            artifactRepository.setSnapshotRepId(repoId);
            snapshotRepositoryHandler = RepositoryArtifactHandlerManager.getRepositoryHandler(artifactRepository);
            File localRepositoryFolder = Files.createTempDirectory("SnapshotLocalRepository_").toFile();
            String localRepositoryPath = localRepositoryFolder.getCanonicalPath();
            debugLog("SnapshotLocalRepository: " + localRepositoryPath);
            snapshotRepositoryHandler.setLocalRepositoryPath(localRepositoryPath);
            localRepositoryFolder.deleteOnExit();
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
        return getServerBean().getRepositoryId();
    }

    private String getSnapshotRepositoryId() {
        return getServerBean().getSnapshotRepId();
    }

    public ArtifactRepositoryBean getServerBean() {
        return this.serverBean;
    }

    private void debugLog(String message) {
        if (isTalendDebug) {
            log.info(message);
        }
    }

}
