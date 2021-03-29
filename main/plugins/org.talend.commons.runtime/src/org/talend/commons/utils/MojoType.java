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
package org.talend.commons.utils;

import java.io.File;
import java.nio.file.Path;

import org.eclipse.m2e.core.MavenPlugin;

public enum MojoType {

    CI_BUILDER("org.talend.ci", "builder-maven-plugin", "ci.builder.version"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    CLOUD_PUBLISHER("org.talend.ci", "cloudpublisher-maven-plugin", "cloud.publisher.version"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    SIGNER("org.talend.ci", "signer-maven-plugin", "signer.version"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    OSGI_HELPER("org.talend.ci", "osgihelper-maven-plugin", "osgihelper.version"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    private String groupId;

    private String artifactId;

    private String versionKey;

    private MojoType(String groupId, String artifactId, String versionKey) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.versionKey = versionKey;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersionKey() {
        return versionKey;
    }

    public String getMojoArtifactIdFolder() {
        Path basePath = new File(MavenPlugin.getMaven().getLocalRepositoryPath()).toPath();
        return basePath.resolve(getGroupId().replaceAll("\\.", "/")).resolve(getArtifactId()).toString(); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public String getMojoGAV() {
        return getGroupId() + ":" + getArtifactId() + ":" + getVersionKey(); //$NON-NLS-1$ //$NON-NLS-2$
    }

}
