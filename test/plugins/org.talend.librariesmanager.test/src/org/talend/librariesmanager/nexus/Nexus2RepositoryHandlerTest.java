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
package org.talend.librariesmanager.nexus;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.nexus.ArtifactRepositoryBean.NexusType;

/**
 * created by wchen on Aug 18, 2017 Detailled comment
 *
 */
public class Nexus2RepositoryHandlerTest {

    @Test
    public void testGetRepositoryURL() {
        ArtifactRepositoryBean serverBean = new ArtifactRepositoryBean();
        serverBean.setServer("http://localhost:8081/nexus");
        serverBean.setRepositoryId("release-repository");
        serverBean.setSnapshotRepId("snapshot-repository");
        serverBean.setType(NexusType.NEXUS_2.name());
        Nexus2RepositoryHandler handler = new Nexus2RepositoryHandler();
        handler.setArtifactServerBean(serverBean);
        String releaseUrl = handler.getRepositoryURL(true);
        Assert.assertEquals("http://localhost:8081/nexus/content/repositories/release-repository/", releaseUrl);
        String snapshotUrl = handler.getRepositoryURL(false);
        Assert.assertEquals("http://localhost:8081/nexus/content/repositories/snapshot-repository/", snapshotUrl);
    }

    private final String NEXUS_SERVER = "http://localhost:8081/nexus";

    @Test
    public void testGetNexusDefaultReleaseRepoUrl1() {
        final String RELEASE = "release";
        ArtifactRepositoryBean bean = new ArtifactRepositoryBean();
        bean.setServer(NEXUS_SERVER);
        bean.setRepositoryId(RELEASE);
        Nexus2RepositoryHandler handler = new Nexus2RepositoryHandler();
        String expect = NEXUS_SERVER + handler.getRepositoryPrefixPath() + RELEASE + "/";
        assertTrue(expect.equals(bean.getRepositoryURL()));
    }

    @Test
    public void testGetNexusDefaultReleaseRepoUrl2() {
        final String RELEASE = "release";
        ArtifactRepositoryBean bean = new ArtifactRepositoryBean();
        bean.setServer(NEXUS_SERVER + "/");
        bean.setRepositoryId(RELEASE);
        Nexus2RepositoryHandler handler = new Nexus2RepositoryHandler();
        String expect = NEXUS_SERVER + handler.getRepositoryPrefixPath() + RELEASE + "/";
        assertTrue(expect.equals(bean.getRepositoryURL()));
    }

    @Test
    public void testGetNexusDefaultSnapshotRepoUrl1() {
        final String SNAPSHOT = "snapshot";
        ArtifactRepositoryBean bean = new ArtifactRepositoryBean();
        bean.setServer(NEXUS_SERVER);
        bean.setSnapshotRepId(SNAPSHOT);
        Nexus2RepositoryHandler handler = new Nexus2RepositoryHandler();
        String expect = NEXUS_SERVER + handler.getRepositoryPrefixPath() + SNAPSHOT + "/";
        assertTrue(expect.equals(bean.getRepositoryURL(false)));
    }

    @Test
    public void testGetNexusDefaultSnapshotRepoUrl2() {
        final String SNAPSHOT = "snapshot";
        ArtifactRepositoryBean bean = new ArtifactRepositoryBean();
        bean.setServer(NEXUS_SERVER + "/");
        bean.setSnapshotRepId(SNAPSHOT);
        Nexus2RepositoryHandler handler = new Nexus2RepositoryHandler();
        String expect = NEXUS_SERVER + handler.getRepositoryPrefixPath() + SNAPSHOT + "/";
        assertTrue(expect.equals(bean.getRepositoryURL(false)));
    }

}
