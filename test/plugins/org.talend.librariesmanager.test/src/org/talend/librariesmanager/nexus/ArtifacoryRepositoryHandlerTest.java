// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.librariesmanager.nexus;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.nexus.ArtifactRepositoryBean.NexusType;
import org.talend.core.nexus.TalendLibsServerManager;
import org.talend.core.runtime.maven.MavenArtifact;

/**
 * created by wchen on May 17, 2018 Detailled comment
 *
 */
public class ArtifacoryRepositoryHandlerTest {

    @Test
    public void testGetRepositoryURL() {
        ArtifactRepositoryBean serverBean = new ArtifactRepositoryBean();
        serverBean.setServer("http://localhost:8081/artifactory");
        serverBean.setRepositoryId("talend-custom-libs-release");
        serverBean.setSnapshotRepId("snapshot-repository");
        serverBean.setType(NexusType.ARTIFACTORY.name());
        ArtifacoryRepositoryHandler handler = new ArtifacoryRepositoryHandler();
        handler.setArtifactServerBean(serverBean);
        String releaseUrl = handler.getRepositoryURL(true);
        Assert.assertEquals("http://localhost:8081/artifactory/talend-custom-libs-release/", releaseUrl);
        String snapshotUrl = handler.getRepositoryURL(false);
        Assert.assertEquals("http://localhost:8081/artifactory/snapshot-repository/", snapshotUrl);
    }

    @Test
    @Ignore
    public void testSearch() throws Exception {
        ArtifactRepositoryBean customNexusServer = TalendLibsServerManager.getInstance().getCustomNexusServer();
        if (customNexusServer == null || !customNexusServer.getType().equals(NexusType.ARTIFACTORY.name())) {
            fail("Test not possible since Aritfactory is not setup");
        }
        ArtifacoryRepositoryHandler handler = new ArtifacoryRepositoryHandler();
        handler.setArtifactServerBean(customNexusServer);
        Bundle bundle = Platform.getBundle("org.talend.librariesmanager.test");
        URL entry = bundle.getEntry("/lib/test_jar1.jar");
        File jarFile = new File(FileLocator.toFileURL(entry).toURI());
        handler.deploy(jarFile, "org.talend.test", "test_jar1", null, "jar", "1.0.0");

        List<MavenArtifact> search = handler.search("org.talend.test", "test_jar1", "1.0.0", true, false);
        Assert.assertNotNull(getExpectedArtifact(search, "org.talend.test", "test_jar1", "1.0.0"));

        handler.deploy(jarFile, "org.talend.test", "test_jar1", null, "jar", "2.0.0-SNAPSHOT");
        search = handler.search("org.talend.test", "test_jar1", "2.0.0-SNAPSHOT", false, true);
        Assert.assertNotNull(getExpectedArtifact(search, "org.talend.test", "test_jar1", "2.0.0-SNAPSHOT"));
    }

    private MavenArtifact getExpectedArtifact(List<MavenArtifact> search, String groupId, String artifactId, String version) {
        for (MavenArtifact artifact : search) {
            if (artifact.getGroupId().equals(groupId) && artifact.getArtifactId().equals(artifactId)
                    && artifact.getVersion().equals(version)) {
                return artifact;
            }
        }
        return null;
    }

}
