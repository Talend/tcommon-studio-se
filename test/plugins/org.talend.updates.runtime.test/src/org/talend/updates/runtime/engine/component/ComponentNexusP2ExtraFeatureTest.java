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
package org.talend.updates.runtime.engine.component;

import org.eclipse.core.runtime.Platform;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.commons.CommonsPlugin;
import org.talend.updates.runtime.engine.P2Manager;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ComponentNexusP2ExtraFeatureTest {

    static class ComponentNexusP2ExtraFeatureTestClass extends ComponentNexusP2ExtraFeature {

        public ComponentNexusP2ExtraFeatureTestClass() {
            super();
        }

        public ComponentNexusP2ExtraFeatureTestClass(String name, String version, String p2IuId) {
            this(name, version, null, null, null, null, p2IuId);
        }

        public ComponentNexusP2ExtraFeatureTestClass(String name, String version, String description, String product,
                String mvnURI, String imageMvnURI, String p2IuId) {
            super(name, version, description, mvnURI, imageMvnURI, product, null, p2IuId, null, null, false);
        }

    }

    @Before
    public void beforeTest() {
        P2Manager.getInstance().reset();
    }

    @Test
    public void test_isInstalled_emptyInstallVersion() throws Exception {
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
    public void test_isInstalled_installed() throws Exception {
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
