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
package org.talend.librariesmanager.model.service;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.nexus.IRepositoryArtifactHandler;
import org.talend.core.nexus.RepositoryArtifactHandlerManager;
import org.talend.core.nexus.TalendLibsServerManager;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.librariesmanager.maven.MavenArtifactsHandler;
import org.talend.librariesmanager.nexus.utils.ShareLibrariesUtil;

/**
 * created by bhe
 */
public class ShareComponentsLibsJob extends Job {

    private MavenArtifactsHandler deployer;

    private Map<File, Set<MavenArtifact>> needToDeploy;

    public ShareComponentsLibsJob(String name, Map<File, Set<MavenArtifact>> needToDeploy, MavenArtifactsHandler deployer) {
        super(name);
        if (needToDeploy == null || deployer == null) {
            throw new IllegalArgumentException("needToDeploy or deployer is null!");
        }
        this.deployer = deployer;
        this.needToDeploy = needToDeploy;
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        deploy();
        return Status.OK_STATUS;
    }

    private void deploy() {

        // deploy needed jars for User and Exchange component providers
        Map<String, List<MavenArtifact>> snapshotArtifactMap = new HashMap<String, List<MavenArtifact>>();
        Map<String, List<MavenArtifact>> releaseArtifactMap = new HashMap<String, List<MavenArtifact>>();
        if (!needToDeploy.isEmpty()) {

            // collect groupId to search
            Set<String> snapshotGroupIdSet = new HashSet<String>();
            Set<String> releaseGroupIdSet = new HashSet<String>();
            Set<Entry<File, Set<MavenArtifact>>> entries = needToDeploy.entrySet();

            for (Entry<File, Set<MavenArtifact>> entry : entries) {
                for (MavenArtifact art : entry.getValue()) {
                    if (ShareLibrariesUtil.isSnapshotVersion(art.getVersion())) {
                        snapshotGroupIdSet.add(art.getGroupId());
                    } else {
                        releaseGroupIdSet.add(art.getGroupId());
                    }
                }

            }

            // search on nexus to avoid deploy the jar many times
            Map<File, Set<MavenArtifact>> shareFiles = new HashMap<File, Set<MavenArtifact>>();
            ArtifactRepositoryBean customNexusServer = TalendLibsServerManager.getInstance().getCustomNexusServer();
            IRepositoryArtifactHandler customerRepHandler = RepositoryArtifactHandlerManager
                    .getRepositoryHandler(customNexusServer);
            if (customerRepHandler != null) {

                try {
                    ShareLibrariesUtil.seachArtifacts(null, customerRepHandler, snapshotArtifactMap, releaseArtifactMap,
                            snapshotGroupIdSet, releaseGroupIdSet);
                } catch (Exception e1) {
                    ExceptionHandler.process(e1);
                }

                for (Entry<File, Set<MavenArtifact>> entry : entries) {
                    Set<MavenArtifact> toShareArtifacts = new HashSet<MavenArtifact>();

                    for (MavenArtifact art : entry.getValue()) {
                        boolean isSnapshotVersion = ShareLibrariesUtil.isSnapshotVersion(art.getVersion());
                        String key = ShareLibrariesUtil.getArtifactKey(art, isSnapshotVersion);

                        List<MavenArtifact> artifactList = null;
                        boolean toShare = true;
                        if (isSnapshotVersion) {
                            artifactList = snapshotArtifactMap.get(key);
                            if (artifactList != null && artifactList.size() > 0) {
                                try {
                                    if (ShareLibrariesUtil.isSameFileWithRemote(entry.getKey(), artifactList, customNexusServer,
                                            customerRepHandler, isSnapshotVersion)) {
                                        toShare = false;
                                    }
                                } catch (Exception e) {
                                    ExceptionHandler.process(e);
                                }
                            }
                        } else {
                            artifactList = releaseArtifactMap.get(key);
                            // skip checksum for release artifact.
                            if (artifactList != null && artifactList.contains(art)) {
                                toShare = false;
                            }
                        }
                        if (toShare) {
                            toShareArtifacts.add(art);
                        }
                    }

                    if (!toShareArtifacts.isEmpty()) {
                        shareFiles.put(entry.getKey(), toShareArtifacts);
                    }
                }

                // share to remote
                Set<Entry<File, Set<MavenArtifact>>> entriesToShare = shareFiles.entrySet();
                for (Entry<File, Set<MavenArtifact>> entry : entriesToShare) {
                    for (MavenArtifact art : entry.getValue()) {
                        try {
                            deployer.deploy(entry.getKey().getAbsoluteFile(), art);
                        } catch (Exception e) {
                            ExceptionHandler.process(e);
                        }
                    }
                }
            }
        }
    }

}
