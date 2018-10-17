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
package org.talend.core.model.general;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import org.talend.core.IService;
import org.talend.core.nexus.ArtifactRepositoryBean;

public interface INexusService extends IService {

    Map upload(ArtifactRepositoryBean artifactRepositoryBean, String groupId, String artifactId, String version, URL source);

    Map getMavenMetadata(ArtifactRepositoryBean artifactRepositoryBean, String groupId, String artifactId, String version);

    InputStream getContentInputStream(ArtifactRepositoryBean artifactRepositoryBean, String r, String g, String a, String v, String e);

    ArtifactRepositoryBean getPublishNexusServerBean(String repositoryId);

    ArtifactRepositoryBean getArtifactRepositoryFromServer();
}
