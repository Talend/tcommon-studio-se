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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
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

    public static final String PATH_640_INDEX_FILE = "resources/components/index-6.4.0.xml"; //$NON-NLS-1$

    class ComponentsNexusInstallFactoryTestClass extends ComponentsNexusInstallFactory {

        public ComponentsNexusInstallFactoryTestClass() {
            super();
        }

    }

    @Test
    public void test_createFeatures_emptyDoc() {
        ComponentsNexusInstallFactoryTestClass factory = new ComponentsNexusInstallFactoryTestClass();

        final Set<P2ExtraFeature> set = factory.createFeatures(new ComponentNexusP2ExtraFeature(), null);
        Assert.assertNotNull(set);
        Assert.assertTrue(set.isEmpty());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void doTestForProduct(String acronym, String product) throws Exception {
        final File indexFile = BundleFileUtil.getBundleFile(this.getClass(), PATH_640_INDEX_FILE);
        Assert.assertNotNull(indexFile);
        Assert.assertTrue(indexFile.exists());

        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(indexFile);

        // change the doc for special product set
        final Element rootElement = document.getRootElement();
        final List selectNodes = rootElement.selectNodes(ComponentsNexusInstallFactory.XPATH_INDEX_COMPONENT);
        Assert.assertNotNull(selectNodes);
        for (Iterator iter = selectNodes.iterator(); iter.hasNext();) {
            Element element = (Element) iter.next();
            if (product == null) { // no attribute, remove it
                final List attributes = element.attributes();
                for (Attribute attr : (List<Attribute>) attributes) {
                    if (attr.getName().equals(ComponentsNexusInstallFactory.ATTR_INDEX_PRODUCT)) {
                        element.remove(attr);
                        break;
                    }
                }
            } else {
                // set the product
                element.attribute(ComponentsNexusInstallFactory.ATTR_INDEX_PRODUCT).setValue(product);
            }
        }
        ComponentsNexusInstallFactoryTestClass factory = new ComponentsNexusInstallFactoryTestClass() {

            @Override
            protected String getAcronym() {
                return acronym;
            }

        };

        final ComponentNexusP2ExtraFeature defaultFeature = new ComponentNexusP2ExtraFeature();
        defaultFeature.setNexusURL("http://abc");
        defaultFeature.setNexusUser("admin");
        defaultFeature.setNexusPass("admin123");

        final Set<P2ExtraFeature> set = factory.createFeatures(defaultFeature, document);
        Assert.assertNotNull(set);
        // same as createFeatures to check
        if (StringUtils.isNotEmpty(product) && !Arrays.asList(product.split(",")).contains(factory.getAcronym())) {
            Assert.assertEquals(0, set.size()); // no valid
            return;
        }

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
            if (StringUtils.isEmpty(product)) {
                Assert.assertNull(compFeature.getProduct());
            } else {
                Assert.assertEquals(product, compFeature.getProduct());
            }
            Assert.assertEquals("mvn:org.talend.components/org.talend.components.jira/" + ver + "/zip", compFeature.getMvnURI());
            Assert.assertNotNull(compFeature.getDescription());
        }
    }

    @Test
    public void test_createFeatures_emptyProduct() throws Exception {
        doTestForProduct("abc", null);
        doTestForProduct("abc", "");
        doTestForProduct("abc", "   ");
    }

    @Test
    public void test_createFeatures_withProduct() throws Exception {
        doTestForProduct("tos_di", "tos_di");
    }

    @Test
    public void test_createFeatures_withMultiProducts() throws Exception {
        doTestForProduct("tos_di", "tos_di,tos_bd,abc");
        doTestForProduct("tos_bd", "tos_di,tos_bd,abc");
        doTestForProduct("abc", "tos_di,tos_bd,abc");
    }

    @Test
    public void test_createFeatures_invalidProduct() throws Exception {
        doTestForProduct("abc", "tos_di,tos_bd");
    }
}
