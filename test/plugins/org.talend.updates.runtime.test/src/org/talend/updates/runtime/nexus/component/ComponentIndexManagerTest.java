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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Node;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.commons.utils.resource.BundleFileUtil;
import org.talend.updates.runtime.engine.P2InstallerTest;
import org.talend.updates.runtime.nexus.component.ComponentIndexManager.MissingSettingException;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ComponentIndexManagerTest {

    public static final String PATH_640_INDEX_FILE = "resources/components/index-6.4.0.xml"; //$NON-NLS-1$

    public static final String PATH_641_INDEX_FILE = "resources/components/index-6.4.1.xml"; //$NON-NLS-1$

    public static final String PATH_641JIRA_INDEX_FILE = "resources/components/index-6.4.1-jira.xml"; //$NON-NLS-1$

    File tmpFolder = null;

    @Before
    public void prepare() throws Exception {
        tmpFolder = org.talend.utils.files.FileUtils.createTmpFolder("test", "index"); //$NON-NLS-1$  //$NON-NLS-2$
    }

    @After
    public void clean() {
        if (tmpFolder != null) {
            FilesUtils.deleteFolder(tmpFolder, true);
        }
    }

    @Test
    public void test_parse_file_null() {
        final List<ComponentIndexBean> indexBeans = new ComponentIndexManager().parse((File) null);
        Assert.assertNotNull(indexBeans);
        Assert.assertEquals(0, indexBeans.size());
    }

    @Test
    public void test_parse_file_notExisted() {
        final List<ComponentIndexBean> indexBeans = new ComponentIndexManager().parse(new File("aaaaaa"));
        Assert.assertNotNull(indexBeans);
        Assert.assertEquals(0, indexBeans.size());
    }

    @Test
    public void test_parse_file_dir() {
        ComponentIndexManager manager = new ComponentIndexManager();
        final List<ComponentIndexBean> indexBeans = manager.parse(tmpFolder);
        Assert.assertNotNull(indexBeans);
        Assert.assertEquals(0, indexBeans.size());
    }

    @Test
    public void test_parse_file_notXml() throws IOException {
        File txtFile = new File(tmpFolder, "abc.txt");
        txtFile.createNewFile();
        final List<ComponentIndexBean> indexBeans = new ComponentIndexManager().parse(txtFile);
        Assert.assertNotNull(indexBeans);
        Assert.assertEquals(0, indexBeans.size());
    }

    @Test
    public void test_parse_file() throws Exception {
        final File indexFile = BundleFileUtil.getBundleFile(this.getClass(), PATH_641_INDEX_FILE);
        Assert.assertNotNull(indexFile);
        Assert.assertTrue(indexFile.exists());

        final List<ComponentIndexBean> indexBeans = new ComponentIndexManager().parse(indexFile);
        Assert.assertNotNull(indexBeans);
        Assert.assertEquals(3, indexBeans.size());

        ComponentIndexBean bean0 = indexBeans.get(0);
        Assert.assertEquals("JIRA", bean0.getName());
        Assert.assertEquals("0.18.0", bean0.getVersion());
        Assert.assertEquals("org.talend.components.jira", bean0.getBundleId());
        Assert.assertEquals("tos_di,tos_bd", bean0.getProduct());
        Assert.assertEquals("mvn:org.talend.components/components-jira/0.18.0/zip", bean0.getMvnURI());
        Assert.assertNotNull(bean0.getDescription());
        Assert.assertNull(bean0.getLicense());
        Assert.assertNull(bean0.getLicenseURI());

        ComponentIndexBean bean1 = indexBeans.get(1);
        Assert.assertEquals("File", bean1.getName());
        Assert.assertEquals("0.19.0", bean1.getVersion());
        Assert.assertEquals("org.talend.components.file", bean1.getBundleId());
        Assert.assertNull(bean1.getProduct());
        Assert.assertEquals("mvn:org.talend.components/components-file/0.19.0/zip", bean1.getMvnURI());
        Assert.assertNotNull(bean1.getDescription());
        Assert.assertNull(bean1.getLicense());
        Assert.assertEquals("https://www.apache.org/licenses/LICENSE-2.0", bean1.getLicenseURI());

        ComponentIndexBean bean2 = indexBeans.get(2);
        Assert.assertEquals("JDBC", bean2.getName());
        Assert.assertEquals("0.19.0", bean2.getVersion());
        Assert.assertEquals("org.talend.components.jdbc", bean2.getBundleId());
        Assert.assertNull(bean2.getProduct());
        Assert.assertEquals("mvn:org.talend.components/components-jdbc/0.19.0/zip", bean2.getMvnURI());
        Assert.assertNull(bean2.getDescription());
        Assert.assertNull(bean2.getLicenseURI());
        Assert.assertNotNull(bean2.getLicense());
        // for quote test
        Assert.assertTrue(bean2.getLicense().contains("\"Apache\""));
        Assert.assertTrue(bean2.getLicense().contains("<http://www.apache.org/>."));
    }

    @Test
    public void test_parse_doc_null() {
        ComponentIndexManager manager = new ComponentIndexManager();
        final List<ComponentIndexBean> indexBeans = manager.parse((Document) null);
        Assert.assertNotNull(indexBeans);
        Assert.assertEquals(0, indexBeans.size());
    }

    @Test
    public void test_parse_doc_emptyList() {
        Document doc = DocumentFactory.getInstance().createDocument();
        final List<ComponentIndexBean> indexBeans = new ComponentIndexManager().parse(doc);
        Assert.assertNotNull(indexBeans);
        Assert.assertEquals(0, indexBeans.size());
    }

    @Test(expected = MissingSettingException.class)
    public void test_parse_doc_missingRequired() {
        final DocumentFactory docFactory = DocumentFactory.getInstance();
        final Element components = docFactory.createElement(ComponentIndexManager.ELEM_COMPONENTS);
        Document doc = docFactory.createDocument(components);

        final Element component = docFactory.createElement(ComponentIndexManager.ELEM_COMPONENT);
        components.add(component);

        component.add(docFactory.createAttribute(component, "name", "myTest"));

        final List<ComponentIndexBean> indexBeans = new ComponentIndexManager().parse(doc);
        Assert.assertNotNull(indexBeans);
        Assert.assertEquals(0, indexBeans.size());
    }

    @Test
    public void test_parse_doc_emptyProduct() throws Exception {
        final DocumentFactory docFactory = DocumentFactory.getInstance();
        final Element components = docFactory.createElement(ComponentIndexManager.ELEM_COMPONENTS);
        Document doc = docFactory.createDocument(components);

        final Element component = docFactory.createElement(ComponentIndexManager.ELEM_COMPONENT);
        components.add(component);

        component.add(docFactory.createAttribute(component, "name", "myTest"));
        component.add(docFactory.createAttribute(component, "bundle_id", "org.talend.components.mytest"));
        component.add(docFactory.createAttribute(component, "version", "1.2.3"));
        component.add(docFactory.createAttribute(component, "mvn_uri", "mvn:org.talend.components/components-mytest/1.2.3/zip"));

        // empty
        final Attribute product = docFactory.createAttribute(component, "product", "");
        component.add(product);

        List<ComponentIndexBean> indexBeans = new ComponentIndexManager().parse(doc);
        Assert.assertNotNull(indexBeans);
        Assert.assertEquals(1, indexBeans.size());
        ComponentIndexBean indexBean = indexBeans.get(0);
        Assert.assertEquals("myTest", indexBean.getName());
        Assert.assertEquals("org.talend.components.mytest", indexBean.getBundleId());
        Assert.assertEquals("1.2.3", indexBean.getVersion());
        Assert.assertEquals("mvn:org.talend.components/components-mytest/1.2.3/zip", indexBean.getMvnURI());
        Assert.assertNull(indexBean.getProduct());
        Assert.assertEquals(0, indexBean.getProducts().length);

        // spaces
        product.setValue("     ");
        indexBeans = new ComponentIndexManager().parse(doc);
        Assert.assertNotNull(indexBeans);
        Assert.assertEquals(1, indexBeans.size());
        indexBean = indexBeans.get(0);
        Assert.assertEquals("myTest", indexBean.getName());
        Assert.assertEquals("org.talend.components.mytest", indexBean.getBundleId());
        Assert.assertEquals("1.2.3", indexBean.getVersion());
        Assert.assertEquals("mvn:org.talend.components/components-mytest/1.2.3/zip", indexBean.getMvnURI());
        Assert.assertNull(indexBean.getProduct());
        Assert.assertEquals(0, indexBean.getProducts().length);
    }

    @Test
    public void test_parse_doc() throws Exception {
        final DocumentFactory docFactory = DocumentFactory.getInstance();
        final Element components = docFactory.createElement(ComponentIndexManager.ELEM_COMPONENTS);
        Document doc = docFactory.createDocument(components);

        final Element component = docFactory.createElement(ComponentIndexManager.ELEM_COMPONENT);
        components.add(component);

        component.add(docFactory.createAttribute(component, "name", "myTest"));
        component.add(docFactory.createAttribute(component, "bundle_id", "org.talend.components.mytest"));
        component.add(docFactory.createAttribute(component, "version", "1.2.3"));
        component.add(docFactory.createAttribute(component, "mvn_uri", "mvn:org.talend.components/components-mytest/1.2.3/zip"));
        component.add(docFactory.createAttribute(component, "product", "tos_di,tos_bd,xxx"));
        component.add(docFactory.createAttribute(component, "license_uri", "https://www.apache.org/licenses/LICENSE-2.0"));
        final Element license = docFactory.createElement("license");
        license.setText("abcdef");
        component.add(license);
        final Element description = docFactory.createElement("description");
        description.setText("xyz");
        component.add(description);

        List<ComponentIndexBean> indexBeans = new ComponentIndexManager().parse(doc);
        Assert.assertNotNull(indexBeans);
        Assert.assertEquals(1, indexBeans.size());
        ComponentIndexBean indexBean = indexBeans.get(0);
        Assert.assertEquals("myTest", indexBean.getName());
        Assert.assertEquals("org.talend.components.mytest", indexBean.getBundleId());
        Assert.assertEquals("1.2.3", indexBean.getVersion());
        Assert.assertEquals("mvn:org.talend.components/components-mytest/1.2.3/zip", indexBean.getMvnURI());

        Assert.assertEquals("tos_di,tos_bd,xxx", indexBean.getProduct());
        Assert.assertEquals(3, indexBean.getProducts().length);
        Assert.assertEquals("https://www.apache.org/licenses/LICENSE-2.0", indexBean.getLicenseURI());
        Assert.assertEquals("abcdef", indexBean.getLicense());
        Assert.assertEquals("xyz", indexBean.getDescription());
    }

    @Test
    public void test_readAttribute_notExisted() {
        final DocumentFactory docFactory = DocumentFactory.getInstance();
        final Element component = docFactory.createElement(ComponentIndexManager.ELEM_COMPONENT);

        ComponentIndexBean indexBean = new ComponentIndexBean();
        new ComponentIndexManager().readAttribute(ComponentIndexNames.product, component, indexBean);

        final String product = indexBean.getValue(ComponentIndexNames.product);
        Assert.assertNull(product);
    }

    @Test
    public void test_readAttribute_blankValue() {
        final DocumentFactory docFactory = DocumentFactory.getInstance();
        final Element component = docFactory.createElement(ComponentIndexManager.ELEM_COMPONENT);
        component.add(docFactory.createAttribute(component, ComponentIndexNames.product.getName(), "  "));

        ComponentIndexBean indexBean = new ComponentIndexBean();
        new ComponentIndexManager().readAttribute(ComponentIndexNames.product, component, indexBean);

        final String product = indexBean.getValue(ComponentIndexNames.product);
        Assert.assertNull(product);
    }

    @Test(expected = MissingSettingException.class)
    public void test_readAttribute_required() {
        final DocumentFactory docFactory = DocumentFactory.getInstance();
        final Element component = docFactory.createElement(ComponentIndexManager.ELEM_COMPONENT);

        ComponentIndexBean indexBean = new ComponentIndexBean();
        new ComponentIndexManager().readAttribute(ComponentIndexNames.name, component, indexBean);

    }

    @Test
    public void test_readAttribute() {
        final DocumentFactory docFactory = DocumentFactory.getInstance();
        final Element component = docFactory.createElement(ComponentIndexManager.ELEM_COMPONENT);
        component.add(docFactory.createAttribute(component, ComponentIndexNames.name.getName(), "myTest"));

        ComponentIndexBean indexBean = new ComponentIndexBean();
        new ComponentIndexManager().readAttribute(ComponentIndexNames.name, component, indexBean);

        final String name = indexBean.getValue(ComponentIndexNames.name);
        Assert.assertNotNull(name);
        Assert.assertEquals("myTest", name);

    }

    @Test
    public void test_readChildContent_notExisted() {
        final DocumentFactory docFactory = DocumentFactory.getInstance();
        final Element component = docFactory.createElement(ComponentIndexManager.ELEM_COMPONENT);

        ComponentIndexBean indexBean = new ComponentIndexBean();
        new ComponentIndexManager().readChildContent(ComponentIndexNames.description, component, indexBean);

        final String description = indexBean.getValue(ComponentIndexNames.description);
        Assert.assertNull(description);
    }

    @Test
    public void test_readChildContent_blankValue() {
        final DocumentFactory docFactory = DocumentFactory.getInstance();
        final Element component = docFactory.createElement(ComponentIndexManager.ELEM_COMPONENT);
        final Element child = docFactory.createElement(ComponentIndexNames.description.getName());
        child.setText("   ");
        component.add(child);

        ComponentIndexBean indexBean = new ComponentIndexBean();
        new ComponentIndexManager().readAttribute(ComponentIndexNames.description, component, indexBean);

        final String description = indexBean.getValue(ComponentIndexNames.description);
        Assert.assertNull(description);
    }

    // @Test(expected = MissingSettingException.class)
    public void test_readChildContent_required() {
        // not child elem is required, so no test
    }

    @Test
    public void test_readChildContent() {
        final DocumentFactory docFactory = DocumentFactory.getInstance();
        final Element component = docFactory.createElement(ComponentIndexManager.ELEM_COMPONENT);
        final Element child = docFactory.createElement(ComponentIndexNames.description.getName());
        child.setText("Hello");
        component.add(child);

        ComponentIndexBean indexBean = new ComponentIndexBean();
        new ComponentIndexManager().readChildContent(ComponentIndexNames.description, component, indexBean);

        final String description = indexBean.getValue(ComponentIndexNames.description);
        Assert.assertNotNull(description);
        Assert.assertEquals("Hello", description);

    }

    @Test
    public void test_createXmlElement_null() {
        final Element elem = new ComponentIndexManager().createXmlElement(null);
        Assert.assertNull(elem);
    }

    @Test
    public void test_createXmlElement_empty() {
        ComponentIndexBean indexBean = new ComponentIndexBean();
        indexBean.setValue(ComponentIndexNames.name, "abc");
        indexBean.setValue(ComponentIndexNames.bundle_id, "org.talend.components.abc");
        indexBean.setValue(ComponentIndexNames.version, "0.1.0");
        indexBean.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/0.1.0/zip");

        // empty
        indexBean.setValue(ComponentIndexNames.product, "     ");

        Element elem = new ComponentIndexManager().createXmlElement(indexBean);
        Assert.assertNotNull(elem); // all required are set

        String mvn_uri = elem.attributeValue(ComponentIndexNames.mvn_uri.getName());
        Assert.assertNotNull(mvn_uri);
        Assert.assertEquals("mvn:org.talend.components/components-abc/0.1.0/zip", mvn_uri);

        final Attribute attribute = elem.attribute(ComponentIndexNames.product.getName());
        Assert.assertNull(attribute); // not set
    }

    @Test
    public void test_createXmlElement_invalid() {
        ComponentIndexBean indexBean = new ComponentIndexBean();

        Element elem = new ComponentIndexManager().createXmlElement(indexBean);
        Assert.assertNull(elem);

        indexBean.setValue(ComponentIndexNames.name, "abc");
        elem = new ComponentIndexManager().createXmlElement(indexBean);
        Assert.assertNull(elem);

        indexBean.setValue(ComponentIndexNames.bundle_id, "org.talend.components.abc");
        elem = new ComponentIndexManager().createXmlElement(indexBean);
        Assert.assertNull(elem);

        indexBean.setValue(ComponentIndexNames.version, "0.1.0");
        elem = new ComponentIndexManager().createXmlElement(indexBean);
        Assert.assertNull(elem);

        indexBean.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/0.1.0/zip");
        elem = new ComponentIndexManager().createXmlElement(indexBean);
        Assert.assertNotNull(elem); // all required are set
    }

    @Test
    public void test_createXmlElement() {
        ComponentIndexBean indexBean = new ComponentIndexBean();
        indexBean.setValue(ComponentIndexNames.name, "abc");
        indexBean.setValue(ComponentIndexNames.bundle_id, "org.talend.components.abc");
        indexBean.setValue(ComponentIndexNames.version, "0.1.0");
        indexBean.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/0.1.0/zip");

        indexBean.setValue(ComponentIndexNames.product, "xxx");
        indexBean.setValue(ComponentIndexNames.description, "hello world");
        indexBean.setValue(ComponentIndexNames.license_uri, "https://www.apache.org/licenses/LICENSE-2.0");
        indexBean.setValue(ComponentIndexNames.license, "It's my abc component");

        Element elem = new ComponentIndexManager().createXmlElement(indexBean);
        Assert.assertNotNull(elem);

        final String name = elem.attributeValue(ComponentIndexNames.name.getName());
        Assert.assertNotNull(name);
        Assert.assertEquals("abc", name);

        final String bundle_id = elem.attributeValue(ComponentIndexNames.bundle_id.getName());
        Assert.assertNotNull(bundle_id);
        Assert.assertEquals("org.talend.components.abc", bundle_id);

        final String version = elem.attributeValue(ComponentIndexNames.version.getName());
        Assert.assertNotNull(version);
        Assert.assertEquals("0.1.0", version);

        final String mvn_uri = elem.attributeValue(ComponentIndexNames.mvn_uri.getName());
        Assert.assertNotNull(mvn_uri);
        Assert.assertEquals("mvn:org.talend.components/components-abc/0.1.0/zip", mvn_uri);

        final String product = elem.attributeValue(ComponentIndexNames.product.getName());
        Assert.assertNotNull(product);
        Assert.assertEquals("xxx", product);

        final String license_uri = elem.attributeValue(ComponentIndexNames.license_uri.getName());
        Assert.assertNotNull(license_uri);
        Assert.assertEquals("https://www.apache.org/licenses/LICENSE-2.0", license_uri);

        final Node descriptionNode = elem.selectSingleNode(ComponentIndexNames.description.getName());
        final String description = descriptionNode != null ? descriptionNode.getText() : null;
        Assert.assertNotNull(description);
        Assert.assertEquals("hello world", description);

        final Node licenseNode = elem.selectSingleNode(ComponentIndexNames.license.getName());
        final String license = licenseNode != null ? licenseNode.getText() : null;
        Assert.assertNotNull(license);
        Assert.assertEquals("It's my abc component", license);
    }

    @Test
    public void test_updateIndexFile_null() {
        final boolean added = new ComponentIndexManager().updateIndexFile(null, null);
        Assert.assertFalse(added);
    }

    @Test
    public void test_updateIndexFile_notExisted() {
        final boolean added = new ComponentIndexManager().updateIndexFile(new File("xyz.txt"), null);
        Assert.assertFalse(added);
    }

    @Test
    public void test_updateIndexFile_dir() {
        final boolean added = new ComponentIndexManager().updateIndexFile(tmpFolder, null);
        Assert.assertFalse(added);
    }

    @Test
    public void test_updateIndexFile_nonXml() throws IOException {
        File txtFile = new File(tmpFolder, "abc.txt");
        txtFile.createNewFile();

        final boolean added = new ComponentIndexManager().updateIndexFile(txtFile, null);
        Assert.assertFalse(added);
    }

    @Test
    public void test_updateIndexFile_bean_null() throws IOException {
        final File dataFile = BundleFileUtil.getBundleFile(this.getClass(), PATH_641_INDEX_FILE);
        Assert.assertNotNull(dataFile);
        Assert.assertTrue(dataFile.exists());

        File indexFile = new File(tmpFolder, dataFile.getName());
        FilesUtils.copyFile(dataFile, indexFile);
        Assert.assertTrue(indexFile.exists());

        final boolean added = new ComponentIndexManager().updateIndexFile(indexFile, null);
        Assert.assertFalse(added);
    }

    @Test
    public void test_updateIndexFile_sort() throws IOException {
        final File dataFile = BundleFileUtil.getBundleFile(this.getClass(), PATH_641_INDEX_FILE);
        Assert.assertNotNull(dataFile);
        Assert.assertTrue(dataFile.exists());
        File indexFile = new File(tmpFolder, dataFile.getName());
        FilesUtils.copyFile(dataFile, indexFile);
        Assert.assertTrue(indexFile.exists());

        ComponentIndexBean indexBean = new ComponentIndexBean();

        indexBean.setValue(ComponentIndexNames.name, "abc");
        indexBean.setValue(ComponentIndexNames.bundle_id, "org.talend.components.abc");
        indexBean.setValue(ComponentIndexNames.version, "0.1.0");
        indexBean.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/0.1.0/zip");

        final ComponentIndexManager componentIndexManager = new ComponentIndexManager();
        final boolean added = componentIndexManager.updateIndexFile(indexFile, indexBean);
        Assert.assertTrue(added);

        final List<ComponentIndexBean> newIndexBeans = componentIndexManager.parse(indexFile);
        Assert.assertNotNull(newIndexBeans);
        Assert.assertEquals("Add new component failure", 4, newIndexBeans.size());

        // test sort
        Assert.assertEquals("Add new component index with wrong order", new String[] { "JIRA", "File", "JDBC", "abc" },
                new String[] { newIndexBeans.get(0).getName(), newIndexBeans.get(1).getName(), newIndexBeans.get(2).getName(),
                        newIndexBeans.get(3).getName() });

        final ComponentIndexBean abcBean = newIndexBeans.get(3);
        Assert.assertEquals("abc", abcBean.getName());
        Assert.assertEquals("0.1.0", abcBean.getVersion());
        Assert.assertEquals("org.talend.components.abc", abcBean.getBundleId());
        Assert.assertEquals("mvn:org.talend.components/components-abc/0.1.0/zip", abcBean.getMvnURI());
        Assert.assertNull(abcBean.getProduct());
        Assert.assertNull(abcBean.getDescription());
        Assert.assertNull(abcBean.getLicenseURI());
        Assert.assertNull(abcBean.getLicense());
    }

    @Test
    public void test_updateIndexFile_withQuoteInLicense() throws IOException {
        final File dataFile = BundleFileUtil.getBundleFile(this.getClass(), PATH_641_INDEX_FILE);
        Assert.assertNotNull(dataFile);
        Assert.assertTrue(dataFile.exists());
        File indexFile = new File(tmpFolder, dataFile.getName());
        FilesUtils.copyFile(dataFile, indexFile);
        Assert.assertTrue(indexFile.exists());

        ComponentIndexBean indexBean = new ComponentIndexBean();
        indexBean.setValue(ComponentIndexNames.name, "abc");
        indexBean.setValue(ComponentIndexNames.bundle_id, "org.talend.components.abc");
        indexBean.setValue(ComponentIndexNames.version, "0.1.0");
        indexBean.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/0.1.0/zip");

        indexBean.setValue(ComponentIndexNames.product, "xxx");
        indexBean.setValue(ComponentIndexNames.description, "hello world");
        indexBean.setValue(ComponentIndexNames.license_uri, "https://www.apache.org/licenses/LICENSE-2.0");
        indexBean.setValue(ComponentIndexNames.license, "It's my abc component for \"Talend\" , <http://www.talend.com/>.");

        final ComponentIndexManager componentIndexManager = new ComponentIndexManager();
        final boolean added = componentIndexManager.updateIndexFile(indexFile, indexBean);
        Assert.assertTrue(added);

        final List<ComponentIndexBean> newIndexBeans = componentIndexManager.parse(indexFile);
        Assert.assertNotNull(newIndexBeans);
        Assert.assertEquals(4, newIndexBeans.size());

    }

    @Test
    public void test_updateIndexFile_updateExistedComponent() throws IOException {
        final File dataFile = BundleFileUtil.getBundleFile(this.getClass(), PATH_641_INDEX_FILE);
        Assert.assertNotNull(dataFile);
        Assert.assertTrue(dataFile.exists());
        File indexFile = new File(tmpFolder, dataFile.getName());
        FilesUtils.copyFile(dataFile, indexFile);
        Assert.assertTrue(indexFile.exists());

        ComponentIndexBean indexBean = new ComponentIndexBean();
        indexBean.setValue(ComponentIndexNames.name, "JIRA");
        indexBean.setValue(ComponentIndexNames.bundle_id, "org.talend.components.jira");
        indexBean.setValue(ComponentIndexNames.version, "0.18.0");
        indexBean.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-jira/0.18.0/zip");

        indexBean.setValue(ComponentIndexNames.product, "xxx");
        indexBean.setValue(ComponentIndexNames.description, "hello world");
        indexBean.setValue(ComponentIndexNames.license_uri, "https://www.apache.org/licenses/LICENSE-2.0");
        indexBean.setValue(ComponentIndexNames.license, "It's my abc component for \"Talend\" , <http://www.talend.com/>.");

        final ComponentIndexManager componentIndexManager = new ComponentIndexManager();
        final boolean added = componentIndexManager.updateIndexFile(indexFile, indexBean);
        Assert.assertTrue(added);

        final List<ComponentIndexBean> newIndexBeans = componentIndexManager.parse(indexFile);
        Assert.assertNotNull(newIndexBeans);
        Assert.assertEquals(3, newIndexBeans.size());

        boolean found = false;
        for (ComponentIndexBean bean : newIndexBeans) {
            if (bean.equals(indexBean)) {
                Assert.assertEquals("xxx", bean.getProduct());
                Assert.assertEquals("hello world", bean.getDescription());
                Assert.assertEquals("https://www.apache.org/licenses/LICENSE-2.0", bean.getLicenseURI());
                Assert.assertNotNull(bean.getLicense());
                found = true;
                break;
            }
        }
        Assert.assertTrue("Update failure", found);
    }

    @Test
    public void test_createIndexFile_bean_nullFile() {
        final boolean created = new ComponentIndexManager().createIndexFile(null, (ComponentIndexBean) null);
        Assert.assertFalse(created);
    }

    @Test
    public void test_createIndexFile_bean_null() {
        final boolean created = new ComponentIndexManager().createIndexFile(new File("abc.txt"), (ComponentIndexBean) null);
        Assert.assertFalse(created);
    }

    @Test
    public void test_createIndexFile_bean_createFailure() {
        ComponentIndexBean indexBean = new ComponentIndexBean();
        indexBean.setValue(ComponentIndexNames.name, "abc");
        indexBean.setValue(ComponentIndexNames.bundle_id, "org.talend.components.abc");
        indexBean.setValue(ComponentIndexNames.version, "0.1.0");
        indexBean.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/0.1.0/zip");

        File indexFile = new File(tmpFolder, "index.xml");

        final boolean created = new ComponentIndexManager() {

            @Override
            public boolean createIndexFile(File indexFile, List<ComponentIndexBean> newIndexList) throws IOException {
                throw new FileNotFoundException();
            }

        }.createIndexFile(indexFile, indexBean);
        Assert.assertFalse(created);
    }

    @Test
    public void test_createIndexFile_bean_existedFile() throws IOException {
        final File dataFile = BundleFileUtil.getBundleFile(this.getClass(), PATH_641_INDEX_FILE);
        Assert.assertNotNull(dataFile);
        Assert.assertTrue(dataFile.exists());
        File indexFile = new File(tmpFolder, dataFile.getName());
        FilesUtils.copyFile(dataFile, indexFile);
        Assert.assertTrue(indexFile.exists());

        final ComponentIndexManager componentIndexManager = new ComponentIndexManager();

        final List<ComponentIndexBean> oldIndexBeans = componentIndexManager.parse(indexFile);
        Assert.assertNotNull(oldIndexBeans);
        Assert.assertEquals(3, oldIndexBeans.size());

        ComponentIndexBean indexBean = new ComponentIndexBean();
        indexBean.setValue(ComponentIndexNames.name, "abc");
        indexBean.setValue(ComponentIndexNames.bundle_id, "org.talend.components.abc");
        indexBean.setValue(ComponentIndexNames.version, "0.1.0");
        indexBean.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/0.1.0/zip");

        indexBean.setValue(ComponentIndexNames.product, "xxx");
        indexBean.setValue(ComponentIndexNames.description, "hello world");
        indexBean.setValue(ComponentIndexNames.license_uri, "https://www.apache.org/licenses/LICENSE-2.0");
        indexBean.setValue(ComponentIndexNames.license, "It's my abc component for \"Talend\" , <http://www.talend.com/>.");

        final boolean created = componentIndexManager.createIndexFile(indexFile, indexBean);
        Assert.assertTrue("Index file created failure", created);
        Assert.assertTrue("Index file created failure", indexFile.exists());

        final List<ComponentIndexBean> newIndexBeans = componentIndexManager.parse(indexFile);
        Assert.assertNotNull(newIndexBeans);
        Assert.assertEquals(1, newIndexBeans.size());
        final ComponentIndexBean componentIndexBean = newIndexBeans.get(0);
        Assert.assertEquals(indexBean, componentIndexBean); // only compare the required

        Assert.assertEquals("xxx", componentIndexBean.getProduct());
        Assert.assertEquals("hello world", componentIndexBean.getDescription());
        Assert.assertEquals("https://www.apache.org/licenses/LICENSE-2.0", componentIndexBean.getLicenseURI());
        Assert.assertTrue("Should contain \"Talend\"", componentIndexBean.getLicense().contains("\"Talend\""));
        Assert.assertTrue("Should contain Talend site", componentIndexBean.getLicense().contains("<http://www.talend.com/>"));
    }

    @Test
    public void test_createIndexFile_bean_nonExistedFile() {
        File indexFile = new File(tmpFolder, "index.xml");
        Assert.assertFalse(indexFile.exists());

        ComponentIndexBean indexBean = new ComponentIndexBean();

        indexBean.setValue(ComponentIndexNames.name, "abc");
        indexBean.setValue(ComponentIndexNames.bundle_id, "org.talend.components.abc");
        indexBean.setValue(ComponentIndexNames.version, "0.1.0");
        indexBean.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/0.1.0/zip");

        indexBean.setValue(ComponentIndexNames.product, "xxx");
        indexBean.setValue(ComponentIndexNames.description, "hello world");
        indexBean.setValue(ComponentIndexNames.license_uri, "https://www.apache.org/licenses/LICENSE-2.0");
        indexBean.setValue(ComponentIndexNames.license, "It's my abc component for \"Talend\" , <http://www.talend.com/>.");

        final ComponentIndexManager componentIndexManager = new ComponentIndexManager();
        final boolean created = componentIndexManager.createIndexFile(indexFile, indexBean);
        Assert.assertTrue("Index file created failure", created);
        Assert.assertTrue("Index file created failure", indexFile.exists());

        final List<ComponentIndexBean> newIndexBeans = componentIndexManager.parse(indexFile);
        Assert.assertNotNull(newIndexBeans);
        Assert.assertEquals(1, newIndexBeans.size());
        final ComponentIndexBean componentIndexBean = newIndexBeans.get(0);
        Assert.assertEquals(indexBean, componentIndexBean); // only compare the required

        Assert.assertEquals("xxx", componentIndexBean.getProduct());
        Assert.assertEquals("hello world", componentIndexBean.getDescription());
        Assert.assertEquals("https://www.apache.org/licenses/LICENSE-2.0", componentIndexBean.getLicenseURI());
        Assert.assertTrue("Should contain \"Talend\"", componentIndexBean.getLicense().contains("\"Talend\""));
        Assert.assertTrue("Should contain Talend site", componentIndexBean.getLicense().contains("<http://www.talend.com/>"));
    }

    @Test
    public void test_createIndexFile_list_nullFile() throws IOException {
        final boolean created = new ComponentIndexManager().createIndexFile(null, (List<ComponentIndexBean>) null);
        Assert.assertFalse(created);
    }

    @Test
    public void test_createIndexFile_list_null() throws IOException {
        final boolean created = new ComponentIndexManager().createIndexFile(new File("abc.txt"), (List<ComponentIndexBean>) null);
        Assert.assertFalse(created);
    }

    @Test
    public void test_createIndexFile_list_empty() throws IOException {
        final boolean created = new ComponentIndexManager().createIndexFile(new File("abc.txt"), Collections.emptyList());
        Assert.assertFalse(created);
    }

    @Test(expected = RuntimeException.class)
    public void test_createIndexFile_list_createFailure() throws Exception {
        try {
            ComponentIndexBean indexBean = new ComponentIndexBean();
            indexBean.setValue(ComponentIndexNames.name, "test");
            indexBean.setValue(ComponentIndexNames.bundle_id, "org.talend.components.test");
            indexBean.setValue(ComponentIndexNames.version, "0.1.0");
            indexBean.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/0.1.0/zip");

            List<ComponentIndexBean> list = new ArrayList<ComponentIndexBean>();
            list.add(indexBean);

            new ComponentIndexManager() {

                @Override
                Element createXmlElement(ComponentIndexBean indexBean) {
                    throw new RuntimeException("XXXXX");
                }

            }.createIndexFile(new File("abc.txt"), list);

            Assert.assertTrue("Shouldn't be here", false);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof RuntimeException);
            Assert.assertEquals("XXXXX", e.getMessage());
            throw e;
        }
    }

    @Test
    public void test_createIndexFile_list_existedFile() throws IOException {
        final File dataFile = BundleFileUtil.getBundleFile(this.getClass(), PATH_641_INDEX_FILE);
        Assert.assertNotNull(dataFile);
        Assert.assertTrue(dataFile.exists());
        File indexFile = new File(tmpFolder, dataFile.getName());
        FilesUtils.copyFile(dataFile, indexFile);
        Assert.assertTrue(indexFile.exists());

        final ComponentIndexManager componentIndexManager = new ComponentIndexManager();

        final List<ComponentIndexBean> oldIndexBeans = componentIndexManager.parse(indexFile);
        Assert.assertNotNull(oldIndexBeans);
        Assert.assertEquals(3, oldIndexBeans.size());

        ComponentIndexBean indexBean1 = new ComponentIndexBean();
        indexBean1.setValue(ComponentIndexNames.name, "abc");
        indexBean1.setValue(ComponentIndexNames.bundle_id, "org.talend.components.abc");
        indexBean1.setValue(ComponentIndexNames.version, "0.1.0");
        indexBean1.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/0.1.0/zip");

        ComponentIndexBean indexBean2 = new ComponentIndexBean();
        indexBean2.setValue(ComponentIndexNames.name, "test");
        indexBean2.setValue(ComponentIndexNames.bundle_id, "org.talend.components.test");
        indexBean2.setValue(ComponentIndexNames.version, "0.1.0");
        indexBean2.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/0.1.0/zip");

        List<ComponentIndexBean> list = new ArrayList<ComponentIndexBean>();
        list.add(indexBean1);
        list.add(indexBean2);

        final boolean created = componentIndexManager.createIndexFile(indexFile, list);
        Assert.assertTrue("Index file created failure", created);
        Assert.assertTrue("Index file created failure", indexFile.exists());

        final List<ComponentIndexBean> newIndexBeans = componentIndexManager.parse(indexFile);
        Assert.assertNotNull(newIndexBeans);
        Assert.assertEquals(2, newIndexBeans.size());
        Assert.assertEquals(indexBean1, newIndexBeans.get(0)); // only compare the required
        Assert.assertEquals(indexBean2, newIndexBeans.get(1)); // only compare the required
    }

    @Test
    public void test_createIndexFile_list_nonExistedFile() throws IOException {
        File indexFile = new File(tmpFolder, "index.xml");
        Assert.assertFalse(indexFile.exists());

        ComponentIndexBean indexBean1 = new ComponentIndexBean();
        indexBean1.setValue(ComponentIndexNames.name, "abc");
        indexBean1.setValue(ComponentIndexNames.bundle_id, "org.talend.components.abc");
        indexBean1.setValue(ComponentIndexNames.version, "0.1.0");
        indexBean1.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/0.1.0/zip");

        ComponentIndexBean indexBean2 = new ComponentIndexBean();
        indexBean2.setValue(ComponentIndexNames.name, "test");
        indexBean2.setValue(ComponentIndexNames.bundle_id, "org.talend.components.test");
        indexBean2.setValue(ComponentIndexNames.version, "0.1.0");
        indexBean2.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/0.1.0/zip");

        List<ComponentIndexBean> list = new ArrayList<ComponentIndexBean>();
        list.add(indexBean1);
        list.add(indexBean2);

        final ComponentIndexManager componentIndexManager = new ComponentIndexManager();
        final boolean created = componentIndexManager.createIndexFile(indexFile, list);
        Assert.assertTrue("Index file created failure", created);
        Assert.assertTrue("Index file created failure", indexFile.exists());

        final List<ComponentIndexBean> newIndexBeans = componentIndexManager.parse(indexFile);
        Assert.assertNotNull(newIndexBeans);
        Assert.assertEquals(2, newIndexBeans.size());
        Assert.assertEquals(indexBean1, newIndexBeans.get(0)); // only compare the required
        Assert.assertEquals(indexBean2, newIndexBeans.get(1)); // only compare the required
    }

    @Test
    public void test_create_null() {
        final ComponentIndexBean indexBean = new ComponentIndexManager().create(null);
        Assert.assertNull(indexBean);
    }

    @Test
    public void test_create_notExisted() {
        final ComponentIndexBean indexBean = new ComponentIndexManager().create(new File("aaaaa"));
        Assert.assertNull(indexBean);
    }

    @Test
    public void test_create_dir() {
        final ComponentIndexBean indexBean = new ComponentIndexManager().create(tmpFolder);
        Assert.assertNull(indexBean);
    }

    @Test
    public void test_create_notZip() throws IOException {
        File txtFile = new File(tmpFolder, "abc.txt");
        txtFile.createNewFile();
        final ComponentIndexBean indexBean = new ComponentIndexManager().create(txtFile);
        Assert.assertNull(indexBean);
    }

    @Test
    public void test_create_wrongZip() throws IOException {
        final File compFile = BundleFileUtil.getBundleFile(this.getClass(), "resources/content.zip");
        Assert.assertNotNull(compFile);
        Assert.assertTrue(compFile.exists());

        final ComponentIndexBean indexBean = new ComponentIndexManager().create(compFile);
        Assert.assertNull(indexBean);
    }

    @Test
    public void test_create() throws IOException {
        final File compFile = BundleFileUtil.getBundleFile(this.getClass(), P2InstallerTest.TEST_COMP_MYJIRA);
        Assert.assertNotNull(compFile);
        Assert.assertTrue(compFile.exists());

        final ComponentIndexBean indexBean = new ComponentIndexManager().create(compFile);
        Assert.assertNotNull(indexBean);
        Assert.assertEquals("myJira", indexBean.getName());
        Assert.assertEquals("org.talend.components.myjira", indexBean.getBundleId());
        Assert.assertEquals("0.16.0.SNAPSHOT", indexBean.getVersion());
        Assert.assertEquals("mvn:org.talend.components/components-myjira/0.16.0.SNAPSHOT/zip", indexBean.getMvnURI());
    }
}
