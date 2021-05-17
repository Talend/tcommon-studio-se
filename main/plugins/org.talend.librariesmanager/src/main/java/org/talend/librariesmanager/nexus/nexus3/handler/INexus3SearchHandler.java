package org.talend.librariesmanager.nexus.nexus3.handler;

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
import java.util.List;

import org.talend.core.runtime.maven.MavenArtifact;

public interface INexus3SearchHandler {

    public List<MavenArtifact> search(String repositoryId, String groupIdToSearch, String artifactId, String versionToSearch)
            throws Exception;

    public String getHandlerVersion();

    public List<MavenArtifact> search(String repositoryId, String name) throws Exception;
}
