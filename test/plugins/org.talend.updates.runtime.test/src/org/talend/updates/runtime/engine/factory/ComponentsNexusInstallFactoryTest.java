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
package org.talend.updates.runtime.engine.factory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.junit.Assert;
import org.junit.Test;
import org.talend.commons.utils.resource.BundleFileUtil;
import org.talend.updates.runtime.engine.component.ComponentNexusP2ExtraFeature;
import org.talend.updates.runtime.model.P2ExtraFeature;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ComponentsNexusInstallFactoryTest {

    class ComponentsNexusInstallFactoryTestClass extends ComponentsNexusInstallFactory {

    }

    @Test
    public void test_createFeatures_emptyDoc() {
        ComponentsNexusInstallFactoryTestClass factory = new ComponentsNexusInstallFactoryTestClass();

        final Set<P2ExtraFeature> set = factory.createFeatures(new ComponentNexusP2ExtraFeature(), null);
        Assert.assertNotNull(set);
        Assert.assertTrue(set.isEmpty());
    }

    @Test
    public void test_createFeatures() throws Exception {
        final File indexFile = BundleFileUtil.getBundleFile(this.getClass(), "resources/components/index-6.4.0.xml");
        Assert.assertNotNull(indexFile);
        Assert.assertTrue(indexFile.exists());

        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(indexFile);
        ComponentsNexusInstallFactoryTestClass factory = new ComponentsNexusInstallFactoryTestClass();

        final ComponentNexusP2ExtraFeature defaultFeature = new ComponentNexusP2ExtraFeature();
        defaultFeature.setNexusURL("http://abc");
        defaultFeature.setNexusUser("admin");
        defaultFeature.setNexusPass("admin123");

        final Set<P2ExtraFeature> set = factory.createFeatures(defaultFeature, document);
        Assert.assertNotNull(set);
        Assert.assertEquals(3, set.size());

        //
        List<P2ExtraFeature> list = new ArrayList<P2ExtraFeature>(set);
        for (int i = 0; i < list.size(); i++) {
            final P2ExtraFeature f = list.get(i);

            Assert.assertTrue(f instanceof ComponentNexusP2ExtraFeature);
            ComponentNexusP2ExtraFeature compFeature = (ComponentNexusP2ExtraFeature) f;

            // same the nexus settings
            Assert.assertEquals("http://abc", compFeature.getNexusURL());
            Assert.assertEquals("admin", compFeature.getNexusUser());
            Assert.assertEquals("admin123", new String(compFeature.getNexusPass()));

            String ver = "0.18." + i;

            Assert.assertEquals("JIRA", compFeature.getName());
            Assert.assertEquals(ver, compFeature.getVersion());
            Assert.assertEquals("tos_di,tos_bd", compFeature.getProduct());
            Assert.assertEquals("mvn:org.talend.components/org.talend.components.jira/" + ver + "/zip", compFeature.getMvnURI());
            Assert.assertNotNull(compFeature.getDescription());
        }
    }
}
