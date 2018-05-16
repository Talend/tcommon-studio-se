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

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * created by cmeng on Mar 16, 2017
 * Detailled comment
 *
 */
@SuppressWarnings("nls")
public class ArtifactRepositoryBeanTest {

    private static final String NEXUS_SERVER = "http://localhost:8081/nexus";

    @Test
    public void testGetNexusDefaultReleaseRepoUrl1() {
        final String RELEASE = "release";
        ArtifactRepositoryBean bean = new ArtifactRepositoryBean();
        bean.setNexusUrl(NEXUS_SERVER);
        bean.setNexusDefaultReleaseRepo(RELEASE);
        String expect = NEXUS_SERVER + ArtifactRepositoryBean.REPO2_MIDDLE_PATH + RELEASE;
        assertTrue(expect.equals(bean.getNexusDefaultReleaseRepoUrl()));
    }

    @Test
    public void testGetNexusDefaultReleaseRepoUrl2() {
        final String RELEASE = "release";
        ArtifactRepositoryBean bean = new ArtifactRepositoryBean();
        bean.setNexusUrl(NEXUS_SERVER + "/");
        bean.setNexusDefaultReleaseRepo(RELEASE);
        String expect = NEXUS_SERVER + ArtifactRepositoryBean.REPO2_MIDDLE_PATH + RELEASE;
        assertTrue(expect.equals(bean.getNexusDefaultReleaseRepoUrl()));
    }

    @Test
    public void testGetNexusDefaultReleaseRepoUrl3() {
        final String RELEASE = "release";
        ArtifactRepositoryBean bean = new ArtifactRepositoryBean();
        bean.setNexusUrl(NEXUS_SERVER + "//");
        bean.setNexusDefaultReleaseRepo(RELEASE);
        String expect = NEXUS_SERVER + ArtifactRepositoryBean.REPO2_MIDDLE_PATH + RELEASE;
        assertTrue(expect.equals(bean.getNexusDefaultReleaseRepoUrl()));
    }

    @Test
    public void testGetNexusDefaultReleaseRepoUrl4() {
        final String RELEASE = "release";
        ArtifactRepositoryBean bean = new ArtifactRepositoryBean();
        bean.setNexusUrl(NEXUS_SERVER);
        bean.setNexusDefaultReleaseRepo("/" + RELEASE);
        String expect = NEXUS_SERVER + ArtifactRepositoryBean.REPO2_MIDDLE_PATH + RELEASE;
        assertTrue(expect.equals(bean.getNexusDefaultReleaseRepoUrl()));
    }

    @Test
    public void testGetNexusDefaultReleaseRepoUrl5() {
        final String RELEASE = "release";
        ArtifactRepositoryBean bean = new ArtifactRepositoryBean();
        bean.setNexusUrl(NEXUS_SERVER);
        bean.setNexusDefaultReleaseRepo("//" + RELEASE);
        String expect = NEXUS_SERVER + ArtifactRepositoryBean.REPO2_MIDDLE_PATH + RELEASE;
        assertTrue(expect.equals(bean.getNexusDefaultReleaseRepoUrl()));
    }

    @Test
    public void testGetNexusDefaultReleaseRepoUrl6() {
        final String RELEASE = "release";
        ArtifactRepositoryBean bean = new ArtifactRepositoryBean();
        bean.setNexusUrl(NEXUS_SERVER + "/");
        bean.setNexusDefaultReleaseRepo("/" + RELEASE);
        String expect = NEXUS_SERVER + ArtifactRepositoryBean.REPO2_MIDDLE_PATH + RELEASE;
        assertTrue(expect.equals(bean.getNexusDefaultReleaseRepoUrl()));
    }

    @Test
    public void testGetNexusDefaultReleaseRepoUrl7() {
        final String RELEASE = "release";
        ArtifactRepositoryBean bean = new ArtifactRepositoryBean();
        bean.setNexusUrl(NEXUS_SERVER + "//");
        bean.setNexusDefaultReleaseRepo("//" + RELEASE);
        String expect = NEXUS_SERVER + ArtifactRepositoryBean.REPO2_MIDDLE_PATH + RELEASE;
        assertTrue(expect.equals(bean.getNexusDefaultReleaseRepoUrl()));
    }

    @Test
    public void testGetNexusDefaultReleaseRepoUrl8() {
        final String RELEASE = "release";
        final String FULL_RELEASE_URL = NEXUS_SERVER + ArtifactRepositoryBean.REPO2_MIDDLE_PATH + RELEASE;
        ArtifactRepositoryBean bean = new ArtifactRepositoryBean();
        bean.setNexusUrl(null);
        bean.setNexusDefaultReleaseRepo(FULL_RELEASE_URL);
        String expect = FULL_RELEASE_URL;
        assertTrue(expect.equals(bean.getNexusDefaultReleaseRepoUrl()));
    }

    @Test
    public void testGetNexusDefaultReleaseRepoUrl9() {
        final String RELEASE = "release";
        final String FULL_RELEASE_URL = NEXUS_SERVER + ArtifactRepositoryBean.REPO2_MIDDLE_PATH + RELEASE;
        ArtifactRepositoryBean bean = new ArtifactRepositoryBean();
        bean.setNexusUrl(FULL_RELEASE_URL);
        bean.setNexusDefaultReleaseRepo(null);
        String expect = FULL_RELEASE_URL;
        assertTrue(expect.equals(bean.getNexusDefaultReleaseRepoUrl()));
    }

    @Test
    public void testGetNexusDefaultSnapshotRepoUrl1() {
        final String SNAPSHOT = "snapshot";
        ArtifactRepositoryBean bean = new ArtifactRepositoryBean();
        bean.setNexusUrl(NEXUS_SERVER);
        bean.setNexusDefaultSnapshotRepo(SNAPSHOT);
        String expect = NEXUS_SERVER + ArtifactRepositoryBean.REPO2_MIDDLE_PATH + SNAPSHOT;
        assertTrue(expect.equals(bean.getNexusDefaultSnapshotRepoUrl()));
    }

    @Test
    public void testGetNexusDefaultSnapshotRepoUrl2() {
        final String SNAPSHOT = "snapshot";
        ArtifactRepositoryBean bean = new ArtifactRepositoryBean();
        bean.setNexusUrl(NEXUS_SERVER + "/");
        bean.setNexusDefaultSnapshotRepo(SNAPSHOT);
        String expect = NEXUS_SERVER + ArtifactRepositoryBean.REPO2_MIDDLE_PATH + SNAPSHOT;
        assertTrue(expect.equals(bean.getNexusDefaultSnapshotRepoUrl()));
    }

    @Test
    public void testGetNexusDefaultSnapshotRepoUrl3() {
        final String SNAPSHOT = "snapshot";
        ArtifactRepositoryBean bean = new ArtifactRepositoryBean();
        bean.setNexusUrl(NEXUS_SERVER + "//");
        bean.setNexusDefaultSnapshotRepo(SNAPSHOT);
        String expect = NEXUS_SERVER + ArtifactRepositoryBean.REPO2_MIDDLE_PATH + SNAPSHOT;
        assertTrue(expect.equals(bean.getNexusDefaultSnapshotRepoUrl()));
    }

    @Test
    public void testGetNexusDefaultSnapshotRepoUrl4() {
        final String SNAPSHOT = "snapshot";
        ArtifactRepositoryBean bean = new ArtifactRepositoryBean();
        bean.setNexusUrl(NEXUS_SERVER);
        bean.setNexusDefaultSnapshotRepo("/" + SNAPSHOT);
        String expect = NEXUS_SERVER + ArtifactRepositoryBean.REPO2_MIDDLE_PATH + SNAPSHOT;
        assertTrue(expect.equals(bean.getNexusDefaultSnapshotRepoUrl()));
    }

    @Test
    public void testGetNexusDefaultSnapshotRepoUrl5() {
        final String SNAPSHOT = "snapshot";
        ArtifactRepositoryBean bean = new ArtifactRepositoryBean();
        bean.setNexusUrl(NEXUS_SERVER);
        bean.setNexusDefaultSnapshotRepo("//" + SNAPSHOT);
        String expect = NEXUS_SERVER + ArtifactRepositoryBean.REPO2_MIDDLE_PATH + SNAPSHOT;
        assertTrue(expect.equals(bean.getNexusDefaultSnapshotRepoUrl()));
    }

    @Test
    public void testGetNexusDefaultSnapshotRepoUrl6() {
        final String SNAPSHOT = "snapshot";
        ArtifactRepositoryBean bean = new ArtifactRepositoryBean();
        bean.setNexusUrl(NEXUS_SERVER + "/");
        bean.setNexusDefaultSnapshotRepo("/" + SNAPSHOT);
        String expect = NEXUS_SERVER + ArtifactRepositoryBean.REPO2_MIDDLE_PATH + SNAPSHOT;
        assertTrue(expect.equals(bean.getNexusDefaultSnapshotRepoUrl()));
    }

    @Test
    public void testGetNexusDefaultSnapshotRepoUrl7() {
        final String SNAPSHOT = "snapshot";
        ArtifactRepositoryBean bean = new ArtifactRepositoryBean();
        bean.setNexusUrl(NEXUS_SERVER + "//");
        bean.setNexusDefaultSnapshotRepo("//" + SNAPSHOT);
        String expect = NEXUS_SERVER + ArtifactRepositoryBean.REPO2_MIDDLE_PATH + SNAPSHOT;
        assertTrue(expect.equals(bean.getNexusDefaultSnapshotRepoUrl()));
    }

    @Test
    public void testGetNexusDefaultSnapshotRepoUrl8() {
        final String SNAPSHOT = "snapshot";
        String FULL_RELEASE_URL = NEXUS_SERVER + ArtifactRepositoryBean.REPO2_MIDDLE_PATH + SNAPSHOT;
        ArtifactRepositoryBean bean = new ArtifactRepositoryBean();
        bean.setNexusUrl(null);
        bean.setNexusDefaultSnapshotRepo(FULL_RELEASE_URL);
        String expect = FULL_RELEASE_URL;
        assertTrue(expect.equals(bean.getNexusDefaultSnapshotRepoUrl()));
    }

    @Test
    public void testGetNexusDefaultSnapshotRepoUrl9() {
        final String SNAPSHOT = "snapshot";
        String FULL_RELEASE_URL = NEXUS_SERVER + ArtifactRepositoryBean.REPO2_MIDDLE_PATH + SNAPSHOT;
        ArtifactRepositoryBean bean = new ArtifactRepositoryBean();
        bean.setNexusUrl(FULL_RELEASE_URL);
        bean.setNexusDefaultSnapshotRepo(null);
        String expect = FULL_RELEASE_URL;
        assertTrue(expect.equals(bean.getNexusDefaultSnapshotRepoUrl()));
    }
}
