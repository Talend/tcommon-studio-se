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
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.util.ISO8601Utils;
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
import org.talend.librariesmanager.nexus.utils.VersionUtil;

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
                    if (releaseGroupIdSet.contains(groupId)) {
                        searchResults = customerRepHandler.search(groupId, null, null, true, false);
                        if (searchResults != null) {
                            for (MavenArtifact result : searchResults) {
                                putArtifactToMap(result, releaseArtifactMap, false);
                            }
                        }
                    }
                    if (snapshotGroupIdSet.contains(groupId)) {
                        searchResults = customerRepHandler.search(groupId, null, null, false, true);
                        if (searchResults != null) {
                            for (MavenArtifact result : searchResults) {
                                putArtifactToMap(result, snapshotArtifactMap, true);
                            }
                        }
                    }
                }
                Iterator<ModuleNeeded> iterator = filesToShare.keySet().iterator();
                Map<File, MavenArtifact> shareFiles = new HashMap<>();
                while (iterator.hasNext()) {
                    if (monitor.isCanceled()) {
                        return Status.CANCEL_STATUS;
                    }
                    ModuleNeeded next = iterator.next();
                    File file = filesToShare.get(next);
                    MavenArtifact artifact = MavenUrlHelper.parseMvnUrl(next.getMavenUri());
                    if (artifact == null) {
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
                    String key = getArtifactKey(artifact, isSnapshotVersion);
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
                        if (isSameFileWithRemote(file, artifactList, customNexusServer)) {
                            continue;
                        }
                    }
                    shareFiles.put(file, artifact);
                }
                SubMonitor mainSubMonitor = SubMonitor.convert(monitor, shareFiles.size());
                shareFiles.forEach((k, v) -> {
                    try {
                        mainSubMonitor.setTaskName(Messages.getString("ShareLibsJob.sharingLibraries", k.getName()));
                        shareToRepository(k, v);
                        mainSubMonitor.worked(1);
                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }
                });
            }
        } catch (Exception e) {
            status = new Status(IStatus.ERROR, "unknown", IStatus.ERROR, "Share libraries failed !", e);
        }

        return status;

    }

    public void putArtifactToMap(MavenArtifact artifact, Map<String, List<MavenArtifact>> map, boolean isShapshot) {
        String key = getArtifactKey(artifact, isShapshot);
        List<MavenArtifact> list = map.get(key);
        if (list == null) {
            list = new ArrayList<MavenArtifact>();
            map.put(key, list);
        }
        list.add(artifact);
    }

    private String getArtifactKey(MavenArtifact artifact, boolean isShapshot) {
        StringBuilder sb = new StringBuilder();
        sb.append(artifact.getGroupId()).append("-");
        sb.append(artifact.getArtifactId()).append("-");
        String version = artifact.getVersion();
        if (isShapshot) {
            version = VersionUtil.getSNAPSHOTVersion(version);
        }
        sb.append(version);
        if (StringUtils.isNotEmpty(artifact.getClassifier())) {
            sb.append("-").append(artifact.getClassifier());
        }
        return sb.toString();
    }

    private boolean isSameFileWithRemote(File localFile, List<MavenArtifact> artifactList,
            ArtifactRepositoryBean customNexusServer) throws Exception {
        String localFileShaCode = DigestUtils.shaHex(new FileInputStream(localFile));
        MavenArtifact lastUpdatedArtifact = null;
        if (ArtifactRepositoryBean.NexusType.ARTIFACTORY.name().equalsIgnoreCase(customNexusServer.getType())) {
            lastUpdatedArtifact = getLateUpdatedMavenArtifact(artifactList);
        } else {
            lastUpdatedArtifact = artifactList.stream().max(Comparator.comparing(e -> e.getVersion())).get();
        }
        if (lastUpdatedArtifact != null && StringUtils.equals(localFileShaCode, lastUpdatedArtifact.getSha1())) {
            return true;
        }
        return false;
    }

    private MavenArtifact getLateUpdatedMavenArtifact(List<MavenArtifact> artifactList) {
        if (artifactList.size() == 1) {
            return artifactList.get(0);
        }
        MavenArtifact latestVersion = null;
        Date lastUpdate = null;
        for (MavenArtifact art : artifactList) {
            if (latestVersion == null) {
                if (art.getLastUpdated() != null) {
                    latestVersion = art;
                    lastUpdate = parsetDate(art.getLastUpdated());
                }
            } else if (art.getLastUpdated() != null && lastUpdate != null) {
                Date artLastUpdate = parsetDate(art.getLastUpdated());
                if (artLastUpdate != null && lastUpdate.getTime() < artLastUpdate.getTime()) {
                    latestVersion = art;
                    lastUpdate = artLastUpdate;
                }
            }
        }
        if (latestVersion != null) {
            return latestVersion;
        } else {
            return artifactList.get(artifactList.size() - 1);
        }
    }

    private Date parsetDate(String strDate) {
        Date date = null;
        if (strDate != null) {
            try {
                date = ISO8601Utils.parse(strDate);
            } catch (Exception ex) {
                ExceptionHandler.process(ex);
            }
        }
        return date;
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

    public abstract Map<ModuleNeeded, File> getFilesToShare(IProgressMonitor monitor);

    public abstract void shareToRepository(File jarFile, MavenArtifact module) throws Exception;
}
