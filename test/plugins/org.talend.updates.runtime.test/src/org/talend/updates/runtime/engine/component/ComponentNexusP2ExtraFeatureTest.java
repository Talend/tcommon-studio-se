// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

import org.junit.Assert;
import org.junit.Test;
import org.talend.updates.runtime.model.P2ExtraFeatureException;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ComponentNexusP2ExtraFeatureTest {

    static class ComponentNexusP2ExtraFeatureTestClass extends ComponentNexusP2ExtraFeature {

        public ComponentNexusP2ExtraFeatureTestClass() {
            super();
        }

        public ComponentNexusP2ExtraFeatureTestClass(String name, String version) {
            super(name, version, null, null, null);
        }

        public ComponentNexusP2ExtraFeatureTestClass(String name, String version, String description, String product,
                String mvnURI) {
            super(name, version, description, product, mvnURI);
        }

    }

    @Test
    public void test_getNexusURL() {
        ComponentNexusP2ExtraFeatureTestClass feature = new ComponentNexusP2ExtraFeatureTestClass();
        Assert.assertNull(feature.getNexusURL());

        final String KEY = ComponentNexusP2ExtraFeatureTestClass.PROP_KEY_NEXUS_URL;
        String oldValue = System.getProperty(KEY);
        try {
            System.setProperty(KEY, "http://abc.com");
            Assert.assertNotNull(feature.getNexusURL());
            Assert.assertEquals("http://abc.com", feature.getNexusURL());
        } finally {
            if (oldValue == null) {
                System.getProperties().remove(KEY);
            } else {
                System.setProperty(KEY, oldValue);
            }
        }
    }

    @Test
    public void test_getNexusUser() {
        ComponentNexusP2ExtraFeatureTestClass feature = new ComponentNexusP2ExtraFeatureTestClass();
        Assert.assertNull(feature.getNexusUser());

        final String KEY = ComponentNexusP2ExtraFeatureTestClass.PROP_KEY_NEXUS_USER;
        String oldValue = System.getProperty(KEY);
        try {
            System.setProperty(KEY, "admin");
            Assert.assertNotNull(feature.getNexusUser());
            Assert.assertEquals("admin", feature.getNexusUser());
        } finally {
            if (oldValue == null) {
                System.getProperties().remove(KEY);
            } else {
                System.setProperty(KEY, oldValue);
            }
        }
    }

    @Test
    public void test_getNexusPass() {
        ComponentNexusP2ExtraFeatureTestClass feature = new ComponentNexusP2ExtraFeatureTestClass();
        Assert.assertNull(feature.getNexusPass());

        final String KEY = ComponentNexusP2ExtraFeatureTestClass.PROP_KEY_NEXUS_PASS;
        String oldValue = System.getProperty(KEY);
        try {
            System.setProperty(KEY, "talend");
            Assert.assertNotNull(feature.getNexusPass());
            Assert.assertEquals("talend", new String(feature.getNexusPass()));
        } finally {
            if (oldValue == null) {
                System.getProperties().remove(KEY);
            } else {
                System.setProperty(KEY, oldValue);
            }
        }
    }

    @Test
    public void test_findExistedComponentVersion() {
        ComponentNexusP2ExtraFeatureTestClass feature = new ComponentNexusP2ExtraFeatureTestClass();
        feature.setName("ABC111111");
        String findExistedComponentVersion = feature.findExistedComponentVersion();
        Assert.assertNull(findExistedComponentVersion);

        feature.setName("tJava");
        findExistedComponentVersion = feature.findExistedComponentVersion();
        Assert.assertNotNull(findExistedComponentVersion); // not found
    }

    @Test
    public void test_isInstalled_emptyInstallVersion() throws P2ExtraFeatureException {
        // null
        ComponentNexusP2ExtraFeatureTestClass feature = new ComponentNexusP2ExtraFeatureTestClass("tJava", null) {

            @Override
            String findExistedComponentVersion() {
                return "1.0";
            }

        };

        Assert.assertFalse(feature.isInstalled(null));

        // emtpy version
        feature = new ComponentNexusP2ExtraFeatureTestClass("tJava", "") {

            @Override
            String findExistedComponentVersion() {
                return "1.0";
            }

        };

        Assert.assertFalse(feature.isInstalled(null));
    }

    @Test
    public void test_isInstalled_emptyCompVersion() throws P2ExtraFeatureException {
        // null version
        ComponentNexusP2ExtraFeatureTestClass feature = new ComponentNexusP2ExtraFeatureTestClass("tJava", "1.0") {

            @Override
            String findExistedComponentVersion() {
                return null;
            }

        };

        Assert.assertFalse(feature.isInstalled(null));

        // empty version
        feature = new ComponentNexusP2ExtraFeatureTestClass("tJava", "1.0") {

            @Override
            String findExistedComponentVersion() {
                return "";
            }

        };

        Assert.assertFalse(feature.isInstalled(null));
    }

    @Test
    public void test_isInstalled_lowInstallVersion() throws P2ExtraFeatureException {
        ComponentNexusP2ExtraFeatureTestClass feature = new ComponentNexusP2ExtraFeatureTestClass("tJava", "0.1") {

            @Override
            String findExistedComponentVersion() {
                return "1.0";
            }
        };
        Assert.assertTrue(feature.isInstalled(null));

        feature = new ComponentNexusP2ExtraFeatureTestClass("Jms", "0.16.0.SNAPSHOT") {

            @Override
            String findExistedComponentVersion() {
                return "0.18.0";
            }
        };
        Assert.assertTrue(feature.isInstalled(null));

        feature = new ComponentNexusP2ExtraFeatureTestClass("Jms", "0.16.0.SNAPSHOT") {

            @Override
            String findExistedComponentVersion() {
                return "0.18.0.SNAPSHOT";
            }
        };
        Assert.assertTrue(feature.isInstalled(null));

        feature = new ComponentNexusP2ExtraFeatureTestClass("Jms", "0.16.0.SNAPSHOT") {

            @Override
            String findExistedComponentVersion() {
                return "0.16.1.SNAPSHOT";
            }
        };
        Assert.assertTrue(feature.isInstalled(null));

        feature = new ComponentNexusP2ExtraFeatureTestClass("Jms", "0.16.0.ABC") {

            @Override
            String findExistedComponentVersion() {
                return "0.16.0.SNAPSHOT";
            }
        };
        Assert.assertTrue(feature.isInstalled(null));

        feature = new ComponentNexusP2ExtraFeatureTestClass("Jms", "0.16.0") {

            @Override
            String findExistedComponentVersion() {
                return "0.16.1.SNAPSHOT";
            }
        };
        Assert.assertTrue(feature.isInstalled(null));
    }

    @Test
    public void test_isInstalled_EqVersion() throws P2ExtraFeatureException {
        ComponentNexusP2ExtraFeatureTestClass feature = new ComponentNexusP2ExtraFeatureTestClass("tJava", "1.0") {

            @Override
            String findExistedComponentVersion() {
                return "1.0";
            }
        };
        Assert.assertTrue(feature.isInstalled(null));

        feature = new ComponentNexusP2ExtraFeatureTestClass("Jms", "0.18.0") {

            @Override
            String findExistedComponentVersion() {
                return "0.18.0";
            }
        };
        Assert.assertTrue(feature.isInstalled(null));

        feature = new ComponentNexusP2ExtraFeatureTestClass("Jms", "0.18.0.SNAPSHOT") {

            @Override
            String findExistedComponentVersion() {
                return "0.18.0.SNAPSHOT";
            }
        };
        Assert.assertTrue(feature.isInstalled(null));
    }

    @Test
    public void test_isInstalled_highInstallVersion() throws P2ExtraFeatureException {
        ComponentNexusP2ExtraFeatureTestClass feature = new ComponentNexusP2ExtraFeatureTestClass("tJava", "10.0") {

            @Override
            String findExistedComponentVersion() {
                return "1.0";
            }
        };
        Assert.assertFalse(feature.isInstalled(null));

        feature = new ComponentNexusP2ExtraFeatureTestClass("Jms", "0.18.1.SNAPSHOT") {

            @Override
            String findExistedComponentVersion() {
                return "0.18.0.SNAPSHOT";
            }
        };
        Assert.assertFalse(feature.isInstalled(null));

        feature = new ComponentNexusP2ExtraFeatureTestClass("Jms", "0.18.1") {

            @Override
            String findExistedComponentVersion() {
                return "0.18.0.SNAPSHOT";
            }
        };
        Assert.assertFalse(feature.isInstalled(null));

        feature = new ComponentNexusP2ExtraFeatureTestClass("Jms", "0.18.1.SNAPSHOT") {

            @Override
            String findExistedComponentVersion() {
                return "0.18.1";
            }
        };
        Assert.assertFalse(feature.isInstalled(null));

        feature = new ComponentNexusP2ExtraFeatureTestClass("Jms", "0.18.1") {

            @Override
            String findExistedComponentVersion() {
                return "0.18.0";
            }
        };
        Assert.assertFalse(feature.isInstalled(null));

    }

    @Test
    public void test_needUpgrade_emptyInstallVersion() throws P2ExtraFeatureException {
        // null version
        ComponentNexusP2ExtraFeatureTestClass feature = new ComponentNexusP2ExtraFeatureTestClass() {

            @Override
            String findExistedComponentVersion() {
                return "1.1"; // empty
            }
        };
        Assert.assertFalse(feature.needUpgrade(null)); // new install

        // empty version
        feature = new ComponentNexusP2ExtraFeatureTestClass("ABC", "") {

            @Override
            String findExistedComponentVersion() {
                return "1.1";
            }
        };
        Assert.assertFalse(feature.needUpgrade(null)); // new install
    }

    @Test
    public void test_needUpgrade_emptyCompVersion() throws P2ExtraFeatureException {
        // null version
        ComponentNexusP2ExtraFeatureTestClass feature = new ComponentNexusP2ExtraFeatureTestClass("tJava", "1.0") {

            @Override
            String findExistedComponentVersion() {
                return ""; // empty
            }

        };
        Assert.assertFalse(feature.needUpgrade(null));

        // empty version
        feature = new ComponentNexusP2ExtraFeatureTestClass("tJava", "1.0") {

            @Override
            String findExistedComponentVersion() {
                return ""; // empty
            }

        };
        Assert.assertFalse(feature.needUpgrade(null));
    }

    @Test
    public void test_needUpgrade_lowVersion() throws P2ExtraFeatureException {
        ComponentNexusP2ExtraFeatureTestClass feature = new ComponentNexusP2ExtraFeatureTestClass("tJava", "0.1") {

            @Override
            String findExistedComponentVersion() {
                return "1.0";
            }
        };
        Assert.assertFalse(feature.needUpgrade(null));

        // with SNAPSHOT
        feature = new ComponentNexusP2ExtraFeatureTestClass("Jms", "0.15.0") {

            @Override
            String findExistedComponentVersion() {
                return "0.16.0.SNAPSHOT";
            }
        };
        Assert.assertFalse(feature.needUpgrade(null));

        //
        feature = new ComponentNexusP2ExtraFeatureTestClass("Jms", "0.16.0") {

            @Override
            String findExistedComponentVersion() {
                return "0.16.1.SNAPSHOT";
            }
        };
        Assert.assertFalse(feature.needUpgrade(null));

        // same version but one is SNAPSHOT
        feature = new ComponentNexusP2ExtraFeatureTestClass("Jms", "0.16.0") {

            @Override
            String findExistedComponentVersion() {
                return "0.16.0.SNAPSHOT";
            }
        };
        Assert.assertFalse(feature.needUpgrade(null));

    }

    @Test
    public void test_needUpgrade_eqVersion() throws P2ExtraFeatureException {
        ComponentNexusP2ExtraFeatureTestClass feature = new ComponentNexusP2ExtraFeatureTestClass("tJava", "1.0") {

            @Override
            String findExistedComponentVersion() {
                return "1.0";
            }
        };
        Assert.assertFalse(feature.needUpgrade(null));

        feature = new ComponentNexusP2ExtraFeatureTestClass("Jms", "0.16.0") {

            @Override
            String findExistedComponentVersion() {
                return "0.16.0";
            }
        };
        Assert.assertFalse(feature.needUpgrade(null));

        feature = new ComponentNexusP2ExtraFeatureTestClass("Jms", "0.16.1.SNAPSHOT") {

            @Override
            String findExistedComponentVersion() {
                return "0.16.1.SNAPSHOT";
            }
        };
        Assert.assertFalse(feature.needUpgrade(null));
    }

    @Test
    public void test_needUpgrade_highVersion() throws P2ExtraFeatureException {
        ComponentNexusP2ExtraFeatureTestClass feature = new ComponentNexusP2ExtraFeatureTestClass("tJava", "10.0") {

            @Override
            String findExistedComponentVersion() {
                return "1.0";
            }
        };
        Assert.assertTrue(feature.needUpgrade(null));

        feature = new ComponentNexusP2ExtraFeatureTestClass("Jms", "0.18.0") {

            @Override
            String findExistedComponentVersion() {
                return "0.16.0";
            }
        };
        Assert.assertTrue(feature.needUpgrade(null));

        feature = new ComponentNexusP2ExtraFeatureTestClass("Jms", "0.16.1.SNAPSHOT") {

            @Override
            String findExistedComponentVersion() {
                return "0.16.0";
            }
        };
        Assert.assertTrue(feature.needUpgrade(null));

        feature = new ComponentNexusP2ExtraFeatureTestClass("Jms", "0.16.2.SNAPSHOT") {

            @Override
            String findExistedComponentVersion() {
                return "0.16.1.SNAPSHOT";
            }
        };
        Assert.assertTrue(feature.needUpgrade(null));

        feature = new ComponentNexusP2ExtraFeatureTestClass("Jms", "0.16.0.SNAPSHOT") {

            @Override
            String findExistedComponentVersion() {
                return "0.16.0";
            }
        };
        Assert.assertTrue(feature.needUpgrade(null));
    }
}
