// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.nexus.IRepositoryArtifactHandler;
import org.talend.core.nexus.RepositoryArtifactHandlerManager;
import org.talend.core.nexus.TalendLibsServerManager;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.librariesmanager.i18n.Messages;
import org.talend.librariesmanager.model.service.LocalLibraryManager;
import org.talend.librariesmanager.nexus.utils.ShareLibrariesUtil;

/**
 * created by Talend on 2015年7月31日 Detailled comment
 *
 */
public abstract class ShareLibrareisHelper {

    private final String TYPE_NEXUS = "nexus";

    protected MavenArtifactsHandler deployer = new MavenArtifactsHandler();

    public IStatus shareLibs(Job job, IProgressMonitor monitor) {
        Map<ModuleNeeded, File> filesToShare = null;
        IStatus status = Status.OK_STATUS;
        // deploy to maven if needed and share to custom nexus
        try {
            setJobName(job, Messages.getString("ShareLibsJob.message", TYPE_NEXUS));
            ArtifactRepositoryBean customNexusServer = TalendLibsServerManager.getInstance().getCustomNexusServer();
            IRepositoryArtifactHandler customerRepHandler = RepositoryArtifactHandlerManager
                    .getRepositoryHandler(customNexusServer);
            if (customerRepHandler != null) {
                filesToShare = getFilesToShare(monitor);
                if (filesToShare == null) {
                    return Status.CANCEL_STATUS;
                }

                // collect groupId to search
                Set<String> groupIds = new HashSet<String>();
                Map<String, List<MavenArtifact>> snapshotArtifactMap = new HashMap<String, List<MavenArtifact>>();
                Map<String, List<MavenArtifact>> releaseArtifactMap = new HashMap<String, List<MavenArtifact>>();
                Set<String> snapshotGroupIdSet = new HashSet<String>();
                Set<String> releaseGroupIdSet = new HashSet<String>();
                for (ModuleNeeded module : filesToShare.keySet()) {
                    checkCancel(monitor);
                    if (module.getMavenUri() != null) {
                        MavenArtifact parseMvnUrl = MavenUrlHelper.parseMvnUrl(module.getMavenUri());
                        if (parseMvnUrl != null) {
                            groupIds.add(parseMvnUrl.getGroupId());
                            if (isSnapshotVersion(parseMvnUrl.getVersion())) {
                                snapshotGroupIdSet.add(parseMvnUrl.getGroupId());
                            } else {
                                releaseGroupIdSet.add(parseMvnUrl.getGroupId());
                            }
                        }
                    }
                }
                List<MavenArtifact> searchResults = new ArrayList<MavenArtifact>();
                for (String groupId : groupIds) {
                    checkCancel(monitor);
                    if (releaseGroupIdSet.contains(groupId)) {
                        searchResults = customerRepHandler.search(groupId, null, null, true, false);
                        if (searchResults != null) {
                            for (MavenArtifact result : searchResults) {
                                checkCancel(monitor);
                                ShareLibrariesUtil.putArtifactToMap(result, releaseArtifactMap, false);
                            }
                        }
                    }
                    checkCancel(monitor);
                    if (snapshotGroupIdSet.contains(groupId)) {
                        searchResults = customerRepHandler.search(groupId, null, null, false, true);
                        if (searchResults != null) {
                            for (MavenArtifact result : searchResults) {
                                checkCancel(monitor);
                                ShareLibrariesUtil.putArtifactToMap(result, snapshotArtifactMap, true);
                            }
                        }
                    }
                }
                Iterator<ModuleNeeded> iterator = filesToShare.keySet().iterator();
                Map<File, MavenArtifact> shareFiles = new HashMap<>();
                while (iterator.hasNext()) {
                    checkCancel(monitor);
                    ModuleNeeded next = iterator.next();
                    File file = filesToShare.get(next);
                    MavenArtifact artifact = MavenUrlHelper.parseMvnUrl(next.getMavenUri());
                    if (artifact == null) {
                        continue;
                    }
                    // If from custom component definition file
                    if (LocalLibraryManager.isSystemCacheFile(file.getName())
                            || (LocalLibraryManager.isComponentDefinitionFileType(file.getName())
                                    && isTalendLibraryGroupId(artifact))) {
                        continue;
                    }
                    try {
                        Integer.parseInt(artifact.getType());
                        // FIXME unexpected type if it's an integer, should fix it in component module definition.
                        continue;
                    } catch (NumberFormatException e) {
                        //
                    }
                    boolean isSnapshotVersion = isSnapshotVersion(artifact.getVersion());
                    String key = ShareLibrariesUtil.getArtifactKey(artifact, isSnapshotVersion);
                    List<MavenArtifact> artifactList = null;
                    if (isSnapshotVersion) {
                        artifactList = snapshotArtifactMap.get(key);
                    } else {
                        artifactList = releaseArtifactMap.get(key);
                        // skip checksum for release artifact.
                        if (artifactList != null && artifactList.contains(artifact)
                                && !Boolean.getBoolean("force_libs_release_update")) {
                            continue;
                        }
                    }
                    if (artifactList != null && artifactList.size() > 0) {
                        if (ShareLibrariesUtil.isSameFileWithRemote(file, artifactList, customNexusServer, customerRepHandler,
                                isSnapshotVersion)) {
                            continue;
                        }
                    }
                    shareFiles.put(file, artifact);
                }
                SubMonitor mainSubMonitor = SubMonitor.convert(monitor, shareFiles.size());
                for (Map.Entry<File, MavenArtifact> entry : shareFiles.entrySet()) {
                    checkCancel(monitor);
                    try {
                        File k = entry.getKey();
                        MavenArtifact v = entry.getValue();
                        mainSubMonitor.setTaskName(Messages.getString("ShareLibsJob.sharingLibraries", k.getName()));
                        shareToRepository(k, v);
                        mainSubMonitor.worked(1);
                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }
                }
            }
        } catch (InterruptedException e) {
            ExceptionHandler.process(e);
            status = Status.CANCEL_STATUS;
        } catch (Exception e) {
            status = new Status(IStatus.ERROR, "unknown", IStatus.ERROR, "Share libraries failed !", e);
        }

        return status;

    }

    private boolean isTalendLibraryGroupId(MavenArtifact artifact) {
        if ("org.talend.libraries".equalsIgnoreCase(artifact.getGroupId())) {
            return true;
        }
        return false;
    }

    private boolean isSnapshotVersion(String version) {
        if (version != null && version.toUpperCase().endsWith(MavenUrlHelper.VERSION_SNAPSHOT)) {
            return true;
        }
        return false;
    }
    private void setJobName(Job job, String jobName) {
        if (job != null) {
            job.setName(jobName);
        }
    }

    protected void checkCancel(IProgressMonitor monitor) throws InterruptedException {
        if (monitor.isCanceled()) {
            throw new InterruptedException(Messages.getString("ShareLibsJob.monitor.cancelled"));
        }
    }

    public abstract Map<ModuleNeeded, File> getFilesToShare(IProgressMonitor monitor);

    public abstract void shareToRepository(File jarFile, MavenArtifact module) throws Exception;
}
