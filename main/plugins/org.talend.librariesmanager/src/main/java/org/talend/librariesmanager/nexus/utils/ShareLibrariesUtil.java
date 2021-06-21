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
package org.talend.librariesmanager.nexus.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.util.ISO8601Utils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.nexus.IRepositoryArtifactHandler;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.librariesmanager.i18n.Messages;

public class ShareLibrariesUtil {

    public static boolean isSameFileWithRemote(File localFile, List<MavenArtifact> artifactList,
            ArtifactRepositoryBean customNexusServer, IRepositoryArtifactHandler customerRepHandler, boolean isSnapshotVersion)
            throws Exception {
        String localFileShaCode = "";
        try (FileInputStream fi = new FileInputStream(localFile)) {
            localFileShaCode = DigestUtils.shaHex(fi);
        }
        String remoteSha1 = null;
        if (ArtifactRepositoryBean.NexusType.ARTIFACTORY.name().equalsIgnoreCase(customNexusServer.getType())) {
            MavenArtifact lastUpdatedArtifact = getLateUpdatedMavenArtifact(artifactList);
            if (lastUpdatedArtifact != null) {
                remoteSha1 = lastUpdatedArtifact.getSha1();
            }
        } else if (ArtifactRepositoryBean.NexusType.NEXUS_3.name().equalsIgnoreCase(customNexusServer.getType())) {
            MavenArtifact lastUpdatedArtifact = artifactList.stream().max(Comparator.comparing(e -> e.getVersion())).get();
            if (lastUpdatedArtifact != null) {
                remoteSha1 = lastUpdatedArtifact.getSha1();
            }
        } else {
            if (!isSnapshotVersion && !Boolean.getBoolean("force_libs_release_update")) {
                return true;
            }
            MavenArtifact lastUpdatedArtifact = artifactList.get(0);
            if (lastUpdatedArtifact != null) {
                remoteSha1 = customerRepHandler.resolveRemoteSha1(lastUpdatedArtifact, !isSnapshotVersion);
            }
        }
        if (StringUtils.equals(localFileShaCode, remoteSha1)) {
            return true;
        }
        return false;
    }

    private static MavenArtifact getLateUpdatedMavenArtifact(List<MavenArtifact> artifactList) {
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

    private static Date parsetDate(String strDate) {
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

    public static void putArtifactToMap(MavenArtifact artifact, Map<String, List<MavenArtifact>> map, boolean isShapshot) {
        String key = getArtifactKey(artifact, isShapshot);
        List<MavenArtifact> list = map.get(key);
        if (list == null) {
            list = new ArrayList<MavenArtifact>();
            map.put(key, list);
        }
        list.add(artifact);
    }

    public static String getArtifactKey(MavenArtifact artifact, boolean isShapshot) {
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

    public static String getMavenClassifier(String path, String regex, String packageType) {
        String classifier = null;
        // javax/xml/bind/acxb-test/2.2.6/acxb-test-2.2.6-jdk10.dll
        path = StringUtils.removeEnd(path, "." + packageType);
        // javax/xml/bind/acxb-test/2.2.6/acxb-test-2.2.6-jdk10
        path = StringUtils.substringAfter(path, regex);// -jdk10
        path = StringUtils.stripStart(path, "-");// jdk10
        if (StringUtils.isNotBlank(path)) {
            classifier = path;
        }
        return classifier;
    }

    /**
     * Search artifacts based on given snapshotGroupIdSet and releaseGroupIdSet from remote artifact repositories
     * represented by artifactHandler, the search results are put to snapshotArtifactMap and releaseArtifactMap
     */
    public static void seachArtifacts(IProgressMonitor monitor, IRepositoryArtifactHandler artifactHandler,
            Map<String, List<MavenArtifact>> snapshotArtifactMap, Map<String, List<MavenArtifact>> releaseArtifactMap,
            Set<String> snapshotGroupIdSet, Set<String> releaseGroupIdSet) {
        if (artifactHandler != null) {
            try {
                checkCancel(monitor);
                List<MavenArtifact> searchResults = new ArrayList<MavenArtifact>();
                for (String groupId : releaseGroupIdSet) {
                    searchResults = artifactHandler.search(groupId, null, null, true, false);
                    if (searchResults != null) {
                        for (MavenArtifact result : searchResults) {
                            checkCancel(monitor);
                            ShareLibrariesUtil.putArtifactToMap(result, releaseArtifactMap, false);
                        }
                    }
                }
                checkCancel(monitor);
                for (String groupId : snapshotGroupIdSet) {
                    searchResults = artifactHandler.search(groupId, null, null, false, true);
                    if (searchResults != null) {
                        for (MavenArtifact result : searchResults) {
                            checkCancel(monitor);
                            ShareLibrariesUtil.putArtifactToMap(result, snapshotArtifactMap, true);
                        }
                    }
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
    }

    public static boolean isTalendLibraryGroupId(MavenArtifact artifact) {
        if ("org.talend.libraries".equalsIgnoreCase(artifact.getGroupId())) {
            return true;
        }
        return false;
    }

    public static boolean isSnapshotVersion(String version) {
        if (version != null && version.toUpperCase().endsWith(MavenUrlHelper.VERSION_SNAPSHOT)) {
            return true;
        }
        return false;
    }

    public static void checkCancel(IProgressMonitor monitor) throws InterruptedException {
        if (monitor != null && monitor.isCanceled()) {
            throw new InterruptedException(Messages.getString("ShareLibsJob.monitor.cancelled"));
        }
    }
}
