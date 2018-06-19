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
package org.talend.updates.runtime.nexus.component;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.INexusService;
import org.talend.core.nexus.ArtifactRepositoryBean;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class NexusServerManagerTest {

    private static String oldNexusServer, oldRepo, oldUser, oldPass;

    @BeforeClass
    public static void recordOldSettings() {
        oldNexusServer = System.getProperty(NexusServerManager.PROP_KEY_NEXUS_URL);
        oldRepo = System.getProperty(NexusServerManager.PROP_KEY_NEXUS_REPOSITORY);
        oldUser = System.getProperty(NexusServerManager.PROP_KEY_NEXUS_USER);
        oldPass = System.getProperty(NexusServerManager.PROP_KEY_NEXUS_PASS);
    }

    @AfterClass
    public static void setBackOldSettings() {
        if (oldNexusServer != null) {
            System.setProperty(NexusServerManager.PROP_KEY_NEXUS_URL, oldNexusServer);
        }
        if (oldRepo != null) {
            System.setProperty(NexusServerManager.PROP_KEY_NEXUS_REPOSITORY, oldRepo);
        }
        if (oldUser != null) {
            System.setProperty(NexusServerManager.PROP_KEY_NEXUS_USER, oldUser);
        }
        if (oldPass != null) {
            System.setProperty(NexusServerManager.PROP_KEY_NEXUS_PASS, oldPass);
        }
    }

    @Before
    @After
    public void cleanProperties() {
        System.getProperties().remove(NexusServerManager.PROP_KEY_NEXUS_REPOSITORY);
        System.getProperties().remove(NexusServerManager.PROP_KEY_NEXUS_URL);
        System.getProperties().remove(NexusServerManager.PROP_KEY_NEXUS_USER);
        System.getProperties().remove(NexusServerManager.PROP_KEY_NEXUS_PASS);
    }

    @Test
    public void test_getLocalNexusServer_local_defaultRepo() {
        ArtifactRepositoryBean localNexusServer = NexusServerManager.getInstance().getLocalNexusServer();
        Assert.assertNotNull(localNexusServer);

        validateNexusServerInfo(getNexusServerInfo("releases"), localNexusServer);
    }

    @Test
    public void test_getLocalNexusServer_local_customRepo() {
        System.setProperty(NexusServerManager.PROP_KEY_NEXUS_REPOSITORY, "mytest");

        ArtifactRepositoryBean localNexusServer = NexusServerManager.getInstance().getLocalNexusServer();
        Assert.assertNotNull(localNexusServer);

        validateNexusServerInfo(getNexusServerInfo("mytest"), localNexusServer);
    }

    @Test
    public void test_getLocalNexusServer_remote_customRepo() {
        System.setProperty(NexusServerManager.PROP_KEY_NEXUS_REPOSITORY, "myremote");

        ArtifactRepositoryBean localNexusServer = NexusServerManager.getInstance().getLocalNexusServer();
        Assert.assertNotNull(localNexusServer);

        validateNexusServerInfo(getNexusServerInfo("myremote"), localNexusServer);
    }

    private void validateNexusServerInfo(ArtifactRepositoryBean expectNexusServerInfo, ArtifactRepositoryBean localNexusServerInfo) {
        Assert.assertEquals(expectNexusServerInfo.getServer(), localNexusServerInfo.getServer());
        Assert.assertEquals(expectNexusServerInfo.getRepositoryId(), localNexusServerInfo.getRepositoryId());
        Assert.assertEquals(expectNexusServerInfo.getRepositoryURL(), localNexusServerInfo.getRepositoryURL());
        Assert.assertEquals(expectNexusServerInfo.getUserName(), localNexusServerInfo.getUserName());
        Assert.assertEquals(expectNexusServerInfo.getPassword(), localNexusServerInfo.getPassword());
    }

    @Test
    public void test_getPropertyNexusServer_notSetNexus() {
        ArtifactRepositoryBean propertyNexusServer = NexusServerManager.getInstance().getPropertyNexusServer();
        Assert.assertNull(propertyNexusServer);
    }

    @Test
    public void test_getPropertyNexusServer_setNexusURLOnly() {
        System.setProperty(NexusServerManager.PROP_KEY_NEXUS_URL, "http://127.0.0.1:8081/nexus");

        ArtifactRepositoryBean propertyNexusServer = NexusServerManager.getInstance().getPropertyNexusServer();
        Assert.assertNotNull(propertyNexusServer);

        Assert.assertEquals("http://127.0.0.1:8081/nexus", propertyNexusServer.getServer());
        Assert.assertEquals("releases", propertyNexusServer.getRepositoryId());
        Assert.assertEquals("http://127.0.0.1:8081/nexus/content/repositories/releases/", propertyNexusServer.getRepositoryURL());
        Assert.assertNull(propertyNexusServer.getUserName());
        Assert.assertNull(propertyNexusServer.getPassword());
    }

    @Test
    public void test_getPropertyNexusServer_customProperties() {
        System.setProperty(NexusServerManager.PROP_KEY_NEXUS_URL, "http://127.0.0.1:8081/nexus");
        System.setProperty(NexusServerManager.PROP_KEY_NEXUS_REPOSITORY, "mycomp");
        System.setProperty(NexusServerManager.PROP_KEY_NEXUS_USER, "talend");
        System.setProperty(NexusServerManager.PROP_KEY_NEXUS_PASS, "1234");

        ArtifactRepositoryBean propertyNexusServer = NexusServerManager.getInstance().getPropertyNexusServer();
        Assert.assertNotNull(propertyNexusServer);

        Assert.assertEquals("http://127.0.0.1:8081/nexus", propertyNexusServer.getServer());
        Assert.assertEquals("mycomp", propertyNexusServer.getRepositoryId());
        Assert.assertEquals("http://127.0.0.1:8081/nexus/content/repositories/mycomp/", propertyNexusServer.getRepositoryURL());
        Assert.assertEquals("talend", propertyNexusServer.getUserName());
        Assert.assertEquals("1234", propertyNexusServer.getPassword());
    }

    public ArtifactRepositoryBean getNexusServerInfo(String repoId) {
        INexusService nexusService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(INexusService.class)) {
            nexusService = (INexusService) GlobalServiceRegister.getDefault().getService(INexusService.class);
        }
        if (nexusService == null) {
            return null;
        }
        return nexusService.getPublishNexusServerBean(repoId);
    }

}
