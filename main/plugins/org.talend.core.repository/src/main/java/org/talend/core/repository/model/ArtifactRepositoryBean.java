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
package org.talend.core.repository.model;

import org.talend.core.nexus.NexusServerBean;
import org.talend.utils.string.StringUtilities;

/**
 * created by cmeng on Mar 2, 2017 Detailled comment
 *
 */
public class ArtifactRepositoryBean implements IArtifactRepositoryBean {

    protected static final String REPO2_MIDDLE_PATH = "/content/repositories/"; //$NON-NLS-1$

    protected static final String REPO3_MIDDLE_PATH = "/repository/"; //$NON-NLS-1$

    private String nexusUrl = ""; //$NON-NLS-1$

    private String nexusUsername = ""; //$NON-NLS-1$

    private String nexusPassword = ""; //$NON-NLS-1$

    private String nexusDefaultReleaseRepo = ""; //$NON-NLS-1$

    private String nexusDefaultSnapshotRepo = ""; //$NON-NLS-1$

    private String nexusDefaultGroupID = ""; //$NON-NLS-1$

    private String nexusType = NexusServerBean.NexusType.NEXUS_2.name(); // $NON-NLS-1$

    @Override
    public String getNexusUrl() {
        return this.nexusUrl;
    }

    public void setNexusUrl(String nexusUrl) {
        this.nexusUrl = nexusUrl;
    }

    @Override
    public String getNexusUsername() {
        return this.nexusUsername;
    }

    public void setNexusUsername(String nexusUsername) {
        this.nexusUsername = nexusUsername;
    }

    @Override
    public String getNexusPassword() {
        return this.nexusPassword;
    }

    public void setNexusPassword(String nexusPassword) {
        this.nexusPassword = nexusPassword;
    }

    @Override
    public String getNexusDefaultReleaseRepoUrl() {
        String serverUrl = getNexusUrl();
        if (serverUrl == null) {
            serverUrl = ""; //$NON-NLS-1$
        }
        serverUrl = StringUtilities.removeEndingString(serverUrl, "/"); //$NON-NLS-1$
        String releaseRepo = getNexusDefaultReleaseRepo();
        if (releaseRepo == null) {
            releaseRepo = ""; //$NON-NLS-1$
        }
        if (serverUrl.isEmpty()) {
            return releaseRepo;
        }
        releaseRepo = StringUtilities.removeStartingString(releaseRepo, "/"); //$NON-NLS-1$
        if (releaseRepo.isEmpty()) {
            return serverUrl;
        }
        return nexusType.equals("NEXUS 3") ? serverUrl + REPO3_MIDDLE_PATH + releaseRepo
                : serverUrl + REPO2_MIDDLE_PATH + releaseRepo;
    }

    @Override
    public String getNexusDefaultReleaseRepo() {
        return this.nexusDefaultReleaseRepo;
    }

    public void setNexusDefaultReleaseRepo(String nexusDefaultReleaseRepo) {
        this.nexusDefaultReleaseRepo = nexusDefaultReleaseRepo;
    }

    @Override
    public String getNexusDefaultSnapshotRepoUrl() {
        String serverUrl = getNexusUrl();
        if (serverUrl == null) {
            serverUrl = ""; //$NON-NLS-1$
        }
        serverUrl = StringUtilities.removeEndingString(serverUrl, "/"); //$NON-NLS-1$
        String snapshotRepo = getNexusDefaultSnapshotRepo();
        if (snapshotRepo == null) {
            snapshotRepo = ""; //$NON-NLS-1$
        }
        if (serverUrl.isEmpty()) {
            return snapshotRepo;
        }
        snapshotRepo = StringUtilities.removeStartingString(snapshotRepo, "/"); //$NON-NLS-1$
        if (snapshotRepo.isEmpty()) {
            return serverUrl;
        }

        return nexusType.equals("NEXUS 3") ? serverUrl + REPO3_MIDDLE_PATH + snapshotRepo
                : serverUrl + REPO2_MIDDLE_PATH + snapshotRepo;
    }

    @Override
    public String getNexusDefaultSnapshotRepo() {
        return this.nexusDefaultSnapshotRepo;
    }

    public void setNexusDefaultSnapshotRepo(String nexusDefaultSnapshotRepo) {
        this.nexusDefaultSnapshotRepo = nexusDefaultSnapshotRepo;
    }

    @Override
    public String getNexusDefaultGroupID() {
        return this.nexusDefaultGroupID;
    }

    public void setNexusDefaultGroupID(String nexusDefaultGroupID) {
        this.nexusDefaultGroupID = nexusDefaultGroupID;
    }

    @Override
    public String getNexusType() {
        return nexusType;
    }

    public void setNexusType(String nexusType) {
        this.nexusType = nexusType;
    }

}
