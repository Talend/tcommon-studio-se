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

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.m2e.core.MavenPlugin;
import org.talend.core.runtime.maven.MavenArtifact;

public class MavenHelper {

    public static String getLocalRepositoryPath() {
        return MavenPlugin.getMaven().getLocalRepositoryPath();
    }

    public static String getArtifactPath(MavenArtifact artifact) throws CoreException {
        ArtifactRepository localRepository = MavenPlugin.getMaven().getLocalRepository();
        String repositoryPath = localRepository.getBasedir();
        String artifactPath = MavenPlugin.getMaven().getArtifactPath(localRepository, artifact.getGroupId(),
                artifact.getArtifactId(),
                artifact.getVersion(), artifact.getType(), artifact.getClassifier());
        return repositoryPath + "/" + artifactPath;
    }

}
