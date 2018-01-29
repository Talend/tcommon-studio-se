// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.updates.runtime.engine.component;

import org.eclipse.core.runtime.Platform;
import org.junit.Assert;
import org.junit.Test;
import org.talend.commons.CommonsPlugin;
import org.talend.core.nexus.NexusServerBean;
import org.talend.updates.runtime.model.P2ExtraFeatureException;
import org.talend.updates.runtime.nexus.component.NexusServerManager;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ComponentNexusP2ExtraFeatureTest {

    static class ComponentNexusP2ExtraFeatureTestClass extends ComponentNexusP2ExtraFeature {

        public ComponentNexusP2ExtraFeatureTestClass() {
            super();
        }

        public ComponentNexusP2ExtraFeatureTestClass(String name, String version, String p2IuId) {
            this(name, version, null, null, null, p2IuId);
        }

        public ComponentNexusP2ExtraFeatureTestClass(String name, String version, String description, String product,
                String mvnURI, String p2IuId) {
            super(name, version, description, product, mvnURI, p2IuId);
        }

        @Override
        public NexusServerBean getServerSetting() {
            // always the new one when test
            return NexusServerManager.getInstance().getPropertyNexusServer();
        }
    }

    @Test
    public void test_getNexusURL() {
        ComponentNexusP2ExtraFeatureTestClass feature = new ComponentNexusP2ExtraFeatureTestClass();
        Assert.assertNull(feature.getNexusURL());

        final String KEY = NexusServerManager.PROP_KEY_NEXUS_URL;
        String oldValue = System.getProperty(KEY);
        try {
            System.setProperty(KEY, "http://abc.com:8081/nexus");
            Assert.assertNotNull(feature.getNexusURL());
            Assert.assertEquals("http://abc.com:8081/nexus/content/repositories/releases/", feature.getNexusURL());
        } finally {
            if (oldValue == null) {
                System.getProperties().remove(KEY);
            } else {
                System.setProperty(KEY, oldValue);
            }
        }
    }

    @Test
    public void test_getNexusRepository() {
        ComponentNexusP2ExtraFeatureTestClass feature = new ComponentNexusP2ExtraFeatureTestClass();
        Assert.assertNull(feature.getNexusURL());

        final String KEY_SERVER = NexusServerManager.PROP_KEY_NEXUS_URL;
        String oldServerValue = System.getProperty(KEY_SERVER);
        final String KEY_REPO = NexusServerManager.PROP_KEY_NEXUS_REPOSITORY;
        String oldRepoValue = System.getProperty(KEY_REPO);
        try {
            System.setProperty(KEY_SERVER, "http://abc.com:8081/nexus");// must set the nexus url
            System.setProperty(KEY_REPO, "myrepo");
            Assert.assertNotNull(feature.getNexusURL());
            Assert.assertEquals("http://abc.com:8081/nexus/content/repositories/myrepo/", feature.getNexusURL());
        } finally {
            if (oldServerValue == null) {
                System.getProperties().remove(KEY_SERVER);
            } else {
                System.setProperty(KEY_SERVER, oldServerValue);
            }
            if (oldRepoValue == null) {
                System.getProperties().remove(KEY_REPO);
            } else {
                System.setProperty(KEY_REPO, oldRepoValue);
            }
        }
    }

    @Test
    public void test_getNexusUser() {
        ComponentNexusP2ExtraFeatureTestClass feature = new ComponentNexusP2ExtraFeatureTestClass();
        Assert.assertNull(feature.getNexusUser());

        final String KEY_SERVER = NexusServerManager.PROP_KEY_NEXUS_URL;
        String oldServerValue = System.getProperty(KEY_SERVER);
        final String KEY_USER = NexusServerManager.PROP_KEY_NEXUS_USER;
        String oldValue = System.getProperty(KEY_USER);
        try {
            System.setProperty(KEY_SERVER, "http://abc.com:8081/nexus");// must set the nexus url
            System.setProperty(KEY_USER, "admin");
            Assert.assertNotNull(feature.getNexusUser());
            Assert.assertEquals("admin", feature.getNexusUser());
        } finally {
            if (oldValue == null) {
                System.getProperties().remove(KEY_USER);
            } else {
                System.setProperty(KEY_USER, oldValue);
            }
            if (oldServerValue == null) {
                System.getProperties().remove(KEY_SERVER);
            } else {
                System.setProperty(KEY_SERVER, oldServerValue);
            }
        }
    }

    @Test
    public void test_getNexusPass() {
        ComponentNexusP2ExtraFeatureTestClass feature = new ComponentNexusP2ExtraFeatureTestClass();
        Assert.assertNull(feature.getNexusPass());

        final String KEY_SERVER = NexusServerManager.PROP_KEY_NEXUS_URL;
        String oldServerValue = System.getProperty(KEY_SERVER);
        final String KEY_PASS = NexusServerManager.PROP_KEY_NEXUS_PASS;
        String oldValue = System.getProperty(KEY_PASS);
        try {
            System.setProperty(KEY_SERVER, "http://abc.com:8081/nexus"); // must set the nexus url
            System.setProperty(KEY_PASS, "talend");
            Assert.assertNotNull(feature.getNexusPass());
            Assert.assertEquals("talend", new String(feature.getNexusPass()));
        } finally {
            if (oldValue == null) {
                System.getProperties().remove(KEY_PASS);
            } else {
                System.setProperty(KEY_PASS, oldValue);
            }
            if (oldServerValue == null) {
                System.getProperties().remove(KEY_SERVER);
            } else {
                System.setProperty(KEY_SERVER, oldServerValue);
            }
        }
    }

    @Test
    public void test_isInstalled_emptyInstallVersion() throws P2ExtraFeatureException {
        if (!CommonsPlugin.isDebugMode() && Platform.inDevelopmentMode()) {
            return; // only enable to test in product
        }

        // null
        ComponentNexusP2ExtraFeatureTestClass feature = new ComponentNexusP2ExtraFeatureTestClass("Test", null,
                "org.talend.test.abc");
        Assert.assertFalse(feature.isInstalled(null));

        // emtpy version
        feature = new ComponentNexusP2ExtraFeatureTestClass("Test", "", "org.talend.test.abc");
        Assert.assertFalse(feature.isInstalled(null));
    }

    @Test
    public void test_isInstalled_installed() throws P2ExtraFeatureException {
        if (!CommonsPlugin.isDebugMode() && Platform.inDevelopmentMode()) {
            return; // only enable to test in product
        }

        // null
        ComponentNexusP2ExtraFeatureTestClass feature = new ComponentNexusP2ExtraFeatureTestClass("Test", null,
                CommonsPlugin.PLUGIN_ID);
        Assert.assertTrue(feature.isInstalled(null));

        // emtpy version
        feature = new ComponentNexusP2ExtraFeatureTestClass("Test", "", CommonsPlugin.PLUGIN_ID);
        Assert.assertTrue(feature.isInstalled(null));
    }
}
