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
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Assert;
import org.junit.Test;
import org.talend.commons.utils.resource.BundleFileUtil;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.updates.runtime.engine.component.ComponentNexusP2ExtraFeature;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.nexus.component.ComponentIndexManager;
import org.talend.updates.runtime.nexus.component.ComponentIndexManagerTest;
import org.talend.updates.runtime.nexus.component.ComponentIndexNames;
import org.talend.updates.runtime.nexus.component.NexusServerManager;
import org.talend.updates.runtime.storage.IFeatureStorage;
import org.talend.updates.runtime.storage.impl.NexusFeatureStorage;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ComponentsNexusInstallFactoryTest {

    class ComponentsNexusInstallFactoryTestClass extends ComponentsNexusInstallFactory {

        public ComponentsNexusInstallFactoryTestClass() {
            super();
        }

    }

    @Test
    public void test_createFeatures_emptyDoc() {
        ComponentsNexusInstallFactoryTestClass factory = new ComponentsNexusInstallFactoryTestClass();

        final Set<ExtraFeature> set = factory.createFeatures(null, null, null, false);
        Assert.assertNotNull(set);
        Assert.assertTrue(set.isEmpty());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void doTestForProduct(String acronym, String product) throws Exception {
        final File indexFile = BundleFileUtil.getBundleFile(this.getClass(), ComponentIndexManagerTest.PATH_641JIRA_INDEX_FILE);
        Assert.assertNotNull(indexFile);
        Assert.assertTrue(indexFile.exists());

        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(indexFile);

        // change the doc for special product set
        final Element rootElement = document.getRootElement();
        final List selectNodes = rootElement.selectNodes(ComponentIndexManager.XPATH_INDEX_COMPONENT);
        Assert.assertNotNull(selectNodes);
        for (Iterator iter = selectNodes.iterator(); iter.hasNext();) {
            Element element = (Element) iter.next();
            if (product == null) { // no attribute, remove it
                final List attributes = element.attributes();
                for (Attribute attr : (List<Attribute>) attributes) {
                    if (attr.getName().equals(ComponentIndexNames.product.getName())) {
                        element.remove(attr);
                        break;
                    }
                }
            } else {
                // set the product
                Attribute attribute = element.attribute(ComponentIndexNames.product.getName());
                if (attribute == null) {
                    final DocumentFactory docFactory = DocumentFactory.getInstance();
                    attribute = docFactory.createAttribute(element, ComponentIndexNames.product.getName(), product);
                    element.add(attribute);
                } else {
                    attribute.setValue(product);
                }
            }
        }
        ComponentsNexusInstallFactoryTestClass factory = new ComponentsNexusInstallFactoryTestClass() {

            @Override
            protected String getAcronym() {
                return acronym;
            }

        };

        ArtifactRepositoryBean serverSetting = factory.getServerSetting();
        if (serverSetting == null) {
            serverSetting = new ArtifactRepositoryBean();
        } else {
            serverSetting = serverSetting.clone();
        }
        serverSetting.setServer("http://abc");
        serverSetting.setUserName("admin");
        serverSetting.setPassword("admin123");

        final Set<ExtraFeature> set = factory.createFeatures(new NullProgressMonitor(), serverSetting, document, false);
        Assert.assertNotNull(set);
        // same as createFeatures to check
        if (StringUtils.isNotBlank(product) && !Arrays.asList(product.split(",")).contains(factory.getAcronym())) {
            Assert.assertEquals(0, set.size()); // no valid
            return;
        }

        Assert.assertEquals(3, set.size());

        //
        List<ExtraFeature> list = new ArrayList<ExtraFeature>(set);
        for (int i = 0; i < list.size(); i++) {
            final ExtraFeature f = list.get(i);

            Assert.assertTrue(f instanceof ComponentNexusP2ExtraFeature);
            ComponentNexusP2ExtraFeature compFeature = (ComponentNexusP2ExtraFeature) f;

            IFeatureStorage storage = compFeature.getStorage();
            Assert.assertTrue(storage instanceof NexusFeatureStorage);
            ArtifactRepositoryBean serverBean = ((NexusFeatureStorage) storage).getServerBean();
            // same the nexus settings
            Assert.assertEquals("http://abc", serverBean.getServer());
            Assert.assertEquals("admin", serverBean.getUserName());
            Assert.assertEquals("admin123", serverBean.getPassword());

            String ver = "0.18." + i;

            Assert.assertEquals("JIRA", compFeature.getName());
            Assert.assertEquals(ver, compFeature.getVersion());
            if (StringUtils.isBlank(product)) {
                Assert.assertNull(compFeature.getProduct());
            } else {
                Assert.assertEquals(product, compFeature.getProduct());
            }
            Assert.assertEquals("mvn:org.talend.components/org.talend.components.jira/" + ver + "/zip", compFeature.getMvnUri());
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

    @Test
    public void test_getNexusURL() {
        ComponentsNexusInstallFactoryTestClass factory = new ComponentsNexusInstallFactoryTestClass();
        ArtifactRepositoryBean serverSetting = factory.getServerSetting();
        Assert.assertNull(serverSetting);

        final String KEY = NexusServerManager.PROP_KEY_NEXUS_URL;
        String oldValue = System.getProperty(KEY);
        try {
            System.setProperty(KEY, "http://abc.com:8081/nexus");
            Assert.assertNotNull(factory.getServerSetting());
            Assert.assertNotNull(factory.getServerSetting().getServer());
            Assert.assertEquals("http://abc.com:8081/nexus/content/repositories/releases/",
                    factory.getServerSetting().getRepositoryURL());
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
        ComponentsNexusInstallFactoryTestClass factory = new ComponentsNexusInstallFactoryTestClass();
        ArtifactRepositoryBean serverSetting = factory.getServerSetting();
        Assert.assertNull(serverSetting);

        final String KEY_SERVER = NexusServerManager.PROP_KEY_NEXUS_URL;
        String oldServerValue = System.getProperty(KEY_SERVER);
        final String KEY_REPO = NexusServerManager.PROP_KEY_NEXUS_REPOSITORY;
        String oldRepoValue = System.getProperty(KEY_REPO);
        try {
            System.setProperty(KEY_SERVER, "http://abc.com:8081/nexus");// must set the nexus url
            System.setProperty(KEY_REPO, "myrepo");
            Assert.assertNotNull(factory.getServerSetting());
            Assert.assertEquals("http://abc.com:8081/nexus/content/repositories/myrepo/",
                    factory.getServerSetting().getRepositoryURL());
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
        ComponentsNexusInstallFactoryTestClass factory = new ComponentsNexusInstallFactoryTestClass();
        ArtifactRepositoryBean serverSetting = factory.getServerSetting();
        Assert.assertNull(serverSetting);

        final String KEY_SERVER = NexusServerManager.PROP_KEY_NEXUS_URL;
        String oldServerValue = System.getProperty(KEY_SERVER);
        final String KEY_USER = NexusServerManager.PROP_KEY_NEXUS_USER;
        String oldValue = System.getProperty(KEY_USER);
        try {
            System.setProperty(KEY_SERVER, "http://abc.com:8081/nexus");// must set the nexus url
            System.setProperty(KEY_USER, "admin");
            Assert.assertNotNull(factory.getServerSetting());
            Assert.assertNotNull(factory.getServerSetting().getUserName());
            Assert.assertEquals("admin", factory.getServerSetting().getUserName());
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
        ComponentsNexusInstallFactoryTestClass factory = new ComponentsNexusInstallFactoryTestClass();
        ArtifactRepositoryBean serverSetting = factory.getServerSetting();
        Assert.assertNull(serverSetting);

        final String KEY_SERVER = NexusServerManager.PROP_KEY_NEXUS_URL;
        String oldServerValue = System.getProperty(KEY_SERVER);
        final String KEY_PASS = NexusServerManager.PROP_KEY_NEXUS_PASS;
        String oldValue = System.getProperty(KEY_PASS);
        try {
            System.setProperty(KEY_SERVER, "http://abc.com:8081/nexus"); // must set the nexus url
            System.setProperty(KEY_PASS, "talend");
            Assert.assertNotNull(factory.getServerSetting());
            Assert.assertEquals("talend", factory.getServerSetting().getPassword());
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

}
