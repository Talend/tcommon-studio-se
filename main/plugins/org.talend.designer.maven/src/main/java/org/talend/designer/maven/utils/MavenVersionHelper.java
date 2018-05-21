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
package org.talend.designer.maven.utils;

import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

public class MavenVersionHelper {

    /**
     * compare maven artifact version
     */
    public static int compareTo(String versionStr, String otherVersionStr) {
        DefaultArtifactVersion version = new DefaultArtifactVersion(versionStr);
        DefaultArtifactVersion otherVersion = new DefaultArtifactVersion(otherVersionStr);
        return version.compareTo(otherVersion);
    }

}
