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
package org.talend.designer.maven.aether.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.repository.Authentication;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.repository.RepositoryPolicy;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.transfer.TransferCancelledException;
import org.eclipse.aether.transfer.TransferEvent;
import org.eclipse.aether.transfer.TransferListener;
import org.eclipse.aether.util.repository.AuthenticationBuilder;
import org.talend.core.download.DownloadListener;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.designer.maven.aether.util.exception.ResolveFailedException;

public class AetherArtifactDownloadProvider implements TransferListener {

    private RepositorySystem repositorySystem;

    private RepositorySystemSession repositorySystemSession;

    private static Exception initFailedException = null;

    private List<DownloadListener> downloadListeners = new ArrayList<DownloadListener>();

    public AetherArtifactDownloadProvider() throws Exception {
        this.repositorySystem = MavenLibraryResolverProvider.newRepositorySystemForResolver();
        this.repositorySystemSession = MavenLibraryResolverProvider.newSession(repositorySystem,
                MavenLibraryResolverProvider.getLocalMVNRepository());
        if (this.repositorySystemSession instanceof DefaultRepositorySystemSession) {
            ((DefaultRepositorySystemSession) this.repositorySystemSession).setChecksumPolicy(getChecksumPolicy());
            ((DefaultRepositorySystemSession) this.repositorySystemSession).setUpdatePolicy(getUpdatePolicy());
            ((DefaultRepositorySystemSession) this.repositorySystemSession).setTransferListener(this);
        }
    }

    public File resolveArtifact(MavenArtifact aritfact, ArtifactRepositoryBean nexusServer) throws Exception {
        if (repositorySystem == null || repositorySystemSession == null) {
            throw initFailedException;
        }
        ArtifactRequest artifactRequest = new ArtifactRequest();
        RemoteRepository defaultRemoteRepository = null;
        if (nexusServer.getUserName() == null && nexusServer.getPassword() == null) {
            defaultRemoteRepository = new RemoteRepository.Builder("talend", "default", nexusServer.getRepositoryURL()).build(); //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            Authentication authentication = new AuthenticationBuilder().addUsername(nexusServer.getUserName())
                    .addPassword(nexusServer.getPassword()).build();
            defaultRemoteRepository = new RemoteRepository.Builder("talend", "default", nexusServer.getRepositoryURL()) //$NON-NLS-1$ //$NON-NLS-2$
                    .setAuthentication(authentication).build();
        }
        defaultRemoteRepository = new RemoteRepository.Builder(defaultRemoteRepository)
                .setProxy(new TalendAetherProxySelector().getProxy(defaultRemoteRepository)).build();
        artifactRequest.addRepository(defaultRemoteRepository);

        Artifact artifact = new DefaultArtifact(aritfact.getGroupId(), aritfact.getArtifactId(), aritfact.getClassifier(),
                aritfact.getType(), aritfact.getVersion());
        artifactRequest.setArtifact(artifact);
        ArtifactResult artifactResult = repositorySystem.resolveArtifact(repositorySystemSession, artifactRequest);
        if (artifactResult.isResolved()) {
            return artifactResult.getArtifact().getFile();
        } else {
            throw new ResolveFailedException(artifactResult.getExceptions());
        }
    }

    public static String getChecksumPolicy() {
        return RepositoryPolicy.CHECKSUM_POLICY_FAIL;
    }

    public static String getUpdatePolicy() {
        return RepositoryPolicy.UPDATE_POLICY_ALWAYS;
    }

    @Override
    public void transferInitiated(TransferEvent event) throws TransferCancelledException {
    }

    @Override
    public void transferStarted(TransferEvent event) throws TransferCancelledException {
        if (event != null) {
            for (DownloadListener listener : downloadListeners) {
                listener.downloadStart(new Long(event.getResource().getContentLength()).intValue());
            }
        }
    }

    @Override
    public void transferProgressed(TransferEvent event) throws TransferCancelledException {
        if (event != null) {
            for (DownloadListener listener : downloadListeners) {
                listener.downloadProgress(null, new Long(event.getTransferredBytes()).intValue());
            }
        }
    }

    @Override
    public void transferCorrupted(TransferEvent event) throws TransferCancelledException {
        if (event != null) {
            deleteTransferedData(event);
            for (DownloadListener listener : downloadListeners) {
                listener.downloadFailed(event.getException());
            }
        }
    }

    @Override
    public void transferSucceeded(TransferEvent event) {
        if (event != null) {
            for (DownloadListener listener : downloadListeners) {
                listener.downloadComplete();
            }
        }
    }

    @Override
    public void transferFailed(TransferEvent event) {
        if (event != null) {
            deleteTransferedData(event);
            for (DownloadListener listener : downloadListeners) {
                listener.downloadFailed(event.getException());
            }
        }
    }

    private void deleteTransferedData(TransferEvent event) {
        if (event != null && event.getResource() != null && event.getResource().getFile() != null
                && event.getResource().getFile().exists()) {
            event.getResource().getFile().delete();
        }
    }

    public void addDownloadListener(DownloadListener listener) {
        this.downloadListeners.add(listener);
    }

    public void removeDownloadListener(DownloadListener listener) {
        this.downloadListeners.remove(listener);
    }
}
