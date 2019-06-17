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
package org.talend.designer.maven.utils;

import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.talend.core.runtime.maven.MavenConstants;

public class MavenVersionHelper {

    private static final String SEGMENT = "-";

    /**
     * compare maven artifact version
     */
    public static int compareTo(String versionStr, String otherVersionStr) {
        DefaultArtifactVersion version = new DefaultArtifactVersion(versionStr);
        DefaultArtifactVersion otherVersion = new DefaultArtifactVersion(otherVersionStr);
        return version.compareTo(otherVersion);
    }

    public static String getJarOriginalVersion(String jarName) {
        String artifactId = jarName;
        if (jarName.endsWith(MavenConstants.TYPE_JAR)) {
            artifactId = jarName.substring(0, jarName.lastIndexOf(MavenConstants.TYPE_JAR) - 1);
        }
        String version = artifactId.substring(artifactId.lastIndexOf(SEGMENT) + 1);
        return version;
    }

}
