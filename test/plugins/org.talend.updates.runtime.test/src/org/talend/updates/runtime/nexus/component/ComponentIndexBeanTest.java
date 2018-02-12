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

import org.junit.Assert;
import org.junit.Test;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ComponentIndexBeanTest {

    class ComponentIndexBeanTestClass extends ComponentIndexBean {

    }

    @Test
    public void test_setValue_nullName() {
        ComponentIndexBeanTestClass indexBean = new ComponentIndexBeanTestClass();
        boolean set = indexBean.setValue((String) null, null);
        Assert.assertFalse(set);
        Assert.assertEquals(0, indexBean.size());

        set = indexBean.setValue((String) null, "");
        Assert.assertFalse(set);
        Assert.assertEquals(0, indexBean.size());

        set = indexBean.setValue((String) null, "abc");
        Assert.assertFalse(set);
        Assert.assertEquals(0, indexBean.size());
    }

    @Test
    public void test_setValue_emptyName() {
        ComponentIndexBeanTestClass indexBean = new ComponentIndexBeanTestClass();
        boolean set = indexBean.setValue("", null);
        Assert.assertFalse(set);
        Assert.assertEquals(0, indexBean.size());

        set = indexBean.setValue("", "");
        Assert.assertFalse(set);
        Assert.assertEquals(0, indexBean.size());

        set = indexBean.setValue("", "abc");
        Assert.assertFalse(set);
        Assert.assertEquals(0, indexBean.size());
    }

    @Test
    public void test_setValue_spacesName() {
        ComponentIndexBeanTestClass indexBean = new ComponentIndexBeanTestClass();
        boolean set = indexBean.setValue(" ", "xyz");
        Assert.assertTrue(set);
        Assert.assertEquals(1, indexBean.size());
        Assert.assertEquals("xyz", indexBean.getValue(" "));
    }

    @Test
    public void test_setValue_nullValue_removeExisted() {
        ComponentIndexBeanTestClass indexBean = new ComponentIndexBeanTestClass();
        boolean set = indexBean.setValue("abc", "xyz");
        Assert.assertTrue(set);
        Assert.assertEquals(1, indexBean.size());

        set = indexBean.setValue("abc", null);
        Assert.assertTrue(set);
        Assert.assertEquals(0, indexBean.size());
    }

    @Test
    public void test_setValue_nullValue_nonExistedName() {
        ComponentIndexBeanTestClass indexBean = new ComponentIndexBeanTestClass();
        boolean set = indexBean.setValue("abc", "xyz");
        Assert.assertTrue(set);
        Assert.assertEquals(1, indexBean.size());

        set = indexBean.setValue("def", null);
        Assert.assertFalse(set);
        Assert.assertEquals(1, indexBean.size());
        Assert.assertEquals("xyz", indexBean.getValue("abc"));
    }

    @Test
    public void test_setValue_emptyValue() {
        ComponentIndexBeanTestClass indexBean = new ComponentIndexBeanTestClass();
        boolean set = indexBean.setValue("abc", "");
        Assert.assertTrue(set);
        Assert.assertEquals(1, indexBean.size());
        Assert.assertEquals("", indexBean.getValue("abc"));
    }

    @Test
    public void test_setValue_spacesValue() {
        ComponentIndexBeanTestClass indexBean = new ComponentIndexBeanTestClass();
        boolean set = indexBean.setValue("abc", "  ");
        Assert.assertTrue(set);
        Assert.assertEquals(1, indexBean.size());
        Assert.assertEquals("  ", indexBean.getValue("abc"));
    }

    @Test
    public void test_setValue() {
        ComponentIndexBeanTestClass indexBean = new ComponentIndexBeanTestClass();
        boolean set = indexBean.setValue("abc", "123");
        Assert.assertTrue(set);
        Assert.assertEquals(1, indexBean.size());
        Assert.assertEquals("123", indexBean.getValue("abc"));

        set = indexBean.setValue("xyz", "456");
        Assert.assertTrue(set);
        Assert.assertEquals(2, indexBean.size());
        Assert.assertEquals("123", indexBean.getValue("abc"));
        Assert.assertEquals("456", indexBean.getValue("xyz"));
    }

    @Test
    public void test_getValue_nullName() {
        ComponentIndexBeanTestClass indexBean = new ComponentIndexBeanTestClass();
        final String value = indexBean.getValue((String) null);
        Assert.assertNull(value);
        Assert.assertEquals(0, indexBean.size());
    }

    @Test
    public void test_getValue_emptyName() {
        ComponentIndexBeanTestClass indexBean = new ComponentIndexBeanTestClass();
        final String value = indexBean.getValue("");
        Assert.assertNull(value);
        Assert.assertEquals(0, indexBean.size());
    }

    @Test
    public void test_getValue_spacesName() {
        ComponentIndexBeanTestClass indexBean = new ComponentIndexBeanTestClass();
        String value = indexBean.getValue(" ");
        Assert.assertNull(value);

        boolean set = indexBean.setValue(" ", "abc");
        Assert.assertTrue(set);
        Assert.assertEquals("abc", indexBean.getValue(" "));
        Assert.assertEquals(1, indexBean.size());
    }

    @Test
    public void test_getValue_nonExistedName() {
        ComponentIndexBeanTestClass indexBean = new ComponentIndexBeanTestClass();
        String value = indexBean.getValue("abc");
        Assert.assertNull(value);
        Assert.assertEquals(0, indexBean.size());
    }

    @Test
    public void test_getValue_existedName() {
        ComponentIndexBeanTestClass indexBean = new ComponentIndexBeanTestClass();
        boolean set = indexBean.setValue("name", "abc");
        Assert.assertTrue(set);
        Assert.assertEquals("abc", indexBean.getValue("name"));
        Assert.assertEquals(1, indexBean.size());
    }

    @Test
    public void test_remove_nullName() {
        ComponentIndexBeanTestClass indexBean = new ComponentIndexBeanTestClass();
        boolean remove = indexBean.remove((String) null);
        Assert.assertFalse(remove);
        Assert.assertEquals(0, indexBean.size());
    }

    @Test
    public void test_remove_emptyName() {
        ComponentIndexBeanTestClass indexBean = new ComponentIndexBeanTestClass();
        boolean remove = indexBean.remove("");
        Assert.assertFalse(remove);
        Assert.assertEquals(0, indexBean.size());
    }

    @Test
    public void test_remove_spacesName() {
        ComponentIndexBeanTestClass indexBean = new ComponentIndexBeanTestClass();
        indexBean.setValue(" ", "abc");
        Assert.assertEquals(1, indexBean.size());
        boolean remove = indexBean.remove(" ");
        Assert.assertTrue(remove);
        Assert.assertEquals(0, indexBean.size());
    }

    @Test
    public void test_remove_nonExistedName() {
        ComponentIndexBeanTestClass indexBean = new ComponentIndexBeanTestClass();
        Assert.assertEquals(0, indexBean.size());
        boolean remove = indexBean.remove("key");
        Assert.assertFalse(remove);
        Assert.assertEquals(0, indexBean.size());
    }

    @Test
    public void test_remove_existedName() {
        ComponentIndexBeanTestClass indexBean = new ComponentIndexBeanTestClass();
        indexBean.setValue("name", "abc");
        Assert.assertEquals(1, indexBean.size());
        boolean remove = indexBean.remove("name");
        Assert.assertTrue(remove);
        Assert.assertEquals(0, indexBean.size());
    }

    @Test
    public void test_getProducts_empty() {
        ComponentIndexBeanTestClass indexBean = new ComponentIndexBeanTestClass();
        boolean set = indexBean.setValue(ComponentIndexNames.product, "");
        Assert.assertTrue(set);
        Assert.assertEquals(1, indexBean.size());
        final String[] products = indexBean.getProducts();
        Assert.assertEquals(0, products.length);
    }

    @Test
    public void test_getProducts_spaces() {
        ComponentIndexBeanTestClass indexBean = new ComponentIndexBeanTestClass();
        boolean set = indexBean.setValue(ComponentIndexNames.product, "  ");
        Assert.assertTrue(set);
        Assert.assertEquals(1, indexBean.size());
        final String[] products = indexBean.getProducts();
        Assert.assertEquals(0, products.length);
    }

    @Test
    public void test_getProducts_one() {
        ComponentIndexBeanTestClass indexBean = new ComponentIndexBeanTestClass();
        boolean set = indexBean.setValue(ComponentIndexNames.product, "tos");
        Assert.assertTrue(set);
        Assert.assertEquals(1, indexBean.size());
        final String[] products = indexBean.getProducts();
        Assert.assertEquals(1, products.length);
        Assert.assertEquals("tos", products[0]);
    }

    @Test
    public void test_getProducts_multi() {
        ComponentIndexBeanTestClass indexBean = new ComponentIndexBeanTestClass();
        boolean set = indexBean.setValue(ComponentIndexNames.product, "tos,tis");
        Assert.assertTrue(set);
        Assert.assertEquals(1, indexBean.size());
        final String[] products = indexBean.getProducts();
        Assert.assertEquals(2, products.length);
        Assert.assertEquals("tos", products[0]);
        Assert.assertEquals("tis", products[1]);
    }

    @Test
    public void test_validRequired_missing() {
        ComponentIndexBeanTestClass indexBean = new ComponentIndexBeanTestClass();
        indexBean.setValue(ComponentIndexNames.name, "Test");
        indexBean.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean.setValue(ComponentIndexNames.version, "1.2.3");
        final boolean validRequired = indexBean.validRequired();
        Assert.assertFalse("need some required fields", validRequired);
    }

    @Test
    public void test_validRequired_all() {
        ComponentIndexBeanTestClass indexBean = new ComponentIndexBeanTestClass();
        indexBean.setValue(ComponentIndexNames.name, "Test");
        indexBean.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean.setValue(ComponentIndexNames.version, "1.2.3");
        indexBean.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/1.2.3/zip");
        final boolean validRequired = indexBean.validRequired();
        Assert.assertTrue("Still need some required fields", validRequired);
    }

    @Test
    public void test_hashCode_required_same() {
        ComponentIndexBeanTestClass indexBean1 = new ComponentIndexBeanTestClass();
        indexBean1.setValue(ComponentIndexNames.name, "Test");
        indexBean1.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean1.setValue(ComponentIndexNames.version, "1.2.3");
        indexBean1.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/1.2.3/zip");

        ComponentIndexBeanTestClass indexBean2 = new ComponentIndexBeanTestClass();
        indexBean2.setValue(ComponentIndexNames.name, "Test");
        indexBean2.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean2.setValue(ComponentIndexNames.version, "1.2.3");
        indexBean2.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/1.2.3/zip");

        Assert.assertEquals("Should have same hashCode for same name, bundleId, version and mvnURI", indexBean1.hashCode(),
                indexBean1.hashCode());
    }

    @Test
    public void test_hashCode_required_diff() {
        ComponentIndexBeanTestClass indexBean1 = new ComponentIndexBeanTestClass();
        indexBean1.setValue(ComponentIndexNames.name, "Abc");
        indexBean1.setValue(ComponentIndexNames.bundle_id, "org.talend.abc");
        indexBean1.setValue(ComponentIndexNames.version, "1.2.3");
        indexBean1.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/1.2.3/zip");

        // diff name, bundleId, mvnURI
        ComponentIndexBeanTestClass indexBean2 = new ComponentIndexBeanTestClass();
        indexBean2.setValue(ComponentIndexNames.name, "Test");
        indexBean2.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean2.setValue(ComponentIndexNames.version, "1.2.3");
        indexBean2.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/1.2.3/zip");
        Assert.assertNotEquals("Shouldn't be same for different component", indexBean1.hashCode(), indexBean2.hashCode());

        // diff version and mvnURI
        ComponentIndexBeanTestClass indexBean3 = new ComponentIndexBeanTestClass();
        indexBean3.setValue(ComponentIndexNames.name, "Abc");
        indexBean3.setValue(ComponentIndexNames.bundle_id, "org.talend.abc");
        indexBean3.setValue(ComponentIndexNames.version, "1.2.4");
        indexBean3.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/1.2.4/zip");
        Assert.assertNotEquals("Shouldn't be same for different component", indexBean1.hashCode(), indexBean3.hashCode());

        // diff name
        ComponentIndexBeanTestClass indexBean4 = new ComponentIndexBeanTestClass();
        indexBean4.setValue(ComponentIndexNames.name, "myAbc");
        indexBean4.setValue(ComponentIndexNames.bundle_id, "org.talend.abc");
        indexBean4.setValue(ComponentIndexNames.version, "1.2.3");
        indexBean4.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/1.2.3/zip");
        Assert.assertNotEquals("Diff name should be different component", indexBean1.hashCode(), indexBean4.hashCode());

        // diff bundleId
        ComponentIndexBeanTestClass indexBean5 = new ComponentIndexBeanTestClass();
        indexBean5.setValue(ComponentIndexNames.name, "Abc");
        indexBean5.setValue(ComponentIndexNames.bundle_id, "org.talend.xxx");
        indexBean5.setValue(ComponentIndexNames.version, "1.2.3");
        indexBean5.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/1.2.3/zip");
        Assert.assertNotEquals("Diff bundleId should be different component", indexBean1.hashCode(), indexBean5.hashCode());
    }

    @Test
    public void test_hashCode_diffOther() {
        ComponentIndexBeanTestClass indexBean1 = new ComponentIndexBeanTestClass();
        indexBean1.setValue(ComponentIndexNames.name, "Test");
        indexBean1.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean1.setValue(ComponentIndexNames.version, "1.2.3");
        indexBean1.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/1.2.3/zip");

        ComponentIndexBeanTestClass indexBean2 = new ComponentIndexBeanTestClass();
        indexBean2.setValue(ComponentIndexNames.name, "Test");
        indexBean2.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean2.setValue(ComponentIndexNames.version, "1.2.3");
        indexBean2.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/1.2.3/zip");
        indexBean2.setValue(ComponentIndexNames.product, "tos"); // have more product

        Assert.assertEquals("Should have same hashCode for same name, bundleId, version and mvnURI", indexBean1.hashCode(),
                indexBean1.hashCode());
    }

    @Test
    public void test_equals_required_same() {
        ComponentIndexBeanTestClass indexBean1 = new ComponentIndexBeanTestClass();
        indexBean1.setValue(ComponentIndexNames.name, "Test");
        indexBean1.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean1.setValue(ComponentIndexNames.version, "1.2.3");
        indexBean1.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/1.2.3/zip");

        ComponentIndexBeanTestClass indexBean2 = new ComponentIndexBeanTestClass();
        indexBean2.setValue(ComponentIndexNames.name, "Test");
        indexBean2.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean2.setValue(ComponentIndexNames.version, "1.2.3");
        indexBean2.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/1.2.3/zip");

        Assert.assertEquals("Should be equals for same name, bundleId, version and mvnURI", indexBean1, indexBean2);
    }

    @Test
    public void test_equals_diff() {
        ComponentIndexBeanTestClass indexBean1 = new ComponentIndexBeanTestClass();
        indexBean1.setValue(ComponentIndexNames.name, "Test");
        indexBean1.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean1.setValue(ComponentIndexNames.version, "1.2.3");
        indexBean1.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/1.2.3/zip");

        ComponentIndexBeanTestClass indexBean2 = new ComponentIndexBeanTestClass();
        indexBean2.setValue(ComponentIndexNames.name, "Abc");
        indexBean2.setValue(ComponentIndexNames.bundle_id, "org.talend.abc");
        indexBean2.setValue(ComponentIndexNames.version, "1.2.3");
        indexBean2.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/1.2.3/zip");

        Assert.assertNotEquals("Should be different components", indexBean1.equals(indexBean2));
    }

    @Test
    public void test_sameComponent_null() {
        ComponentIndexBeanTestClass indexBean1 = new ComponentIndexBeanTestClass();
        final boolean same = indexBean1.sameComponent(null);
        Assert.assertFalse(same);
    }

    @Test
    public void test_sameComponent_sameBundleIdOnly() {
        ComponentIndexBeanTestClass indexBean1 = new ComponentIndexBeanTestClass();
        indexBean1.setValue(ComponentIndexNames.name, "Test");
        indexBean1.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean1.setValue(ComponentIndexNames.version, "1.2.3");
        indexBean1.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/1.2.3/zip");

        ComponentIndexBeanTestClass indexBean2 = new ComponentIndexBeanTestClass();
        indexBean2.setValue(ComponentIndexNames.name, "Abc");
        indexBean2.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean2.setValue(ComponentIndexNames.version, "1.2.3");
        indexBean2.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/1.2.3/zip");

        final boolean same = indexBean1.sameComponent(indexBean2);
        Assert.assertTrue("Only check the bundleId", same);
    }

    @Test
    public void test_sameComponent_diff_bundleId() {
        ComponentIndexBeanTestClass indexBean1 = new ComponentIndexBeanTestClass();
        indexBean1.setValue(ComponentIndexNames.name, "Test");
        indexBean1.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean1.setValue(ComponentIndexNames.version, "1.2.3");
        indexBean1.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/1.2.3/zip");

        ComponentIndexBeanTestClass indexBean2 = new ComponentIndexBeanTestClass();
        indexBean2.setValue(ComponentIndexNames.name, "Abc");
        indexBean2.setValue(ComponentIndexNames.bundle_id, "org.talend.abc");
        indexBean2.setValue(ComponentIndexNames.version, "1.2.3");
        indexBean2.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/1.2.3/zip");

        final boolean same = indexBean1.sameComponent(indexBean2);
        Assert.assertFalse("different components", same);
    }

    @Test
    public void test_compareVersion_null() {
        ComponentIndexBeanTestClass indexBean1 = new ComponentIndexBeanTestClass();
        final int compare = indexBean1.compareVersion(null);
        Assert.assertEquals("Not same component", -2, compare);
    }

    @Test
    public void test_compareVersion_diffComp() {
        ComponentIndexBeanTestClass indexBean1 = new ComponentIndexBeanTestClass();
        indexBean1.setValue(ComponentIndexNames.name, "Test");
        indexBean1.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean1.setValue(ComponentIndexNames.version, "1.2.3");
        indexBean1.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/1.2.3/zip");

        ComponentIndexBeanTestClass indexBean2 = new ComponentIndexBeanTestClass();
        indexBean2.setValue(ComponentIndexNames.name, "Abc");
        indexBean2.setValue(ComponentIndexNames.bundle_id, "org.talend.abc");
        indexBean2.setValue(ComponentIndexNames.version, "1.2.3");
        indexBean2.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/1.2.3/zip");

        final int compare = indexBean1.compareVersion(indexBean2);
        Assert.assertEquals("Not same component", -2, compare);
    }

    @Test
    public void test_compareVersion_diffQualifier() {
        ComponentIndexBeanTestClass indexBean1 = new ComponentIndexBeanTestClass();
        indexBean1.setValue(ComponentIndexNames.name, "Test");
        indexBean1.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean1.setValue(ComponentIndexNames.version, "1.2.3.ABC");
        indexBean1.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/1.2.3/zip/ABC");

        ComponentIndexBeanTestClass indexBean2 = new ComponentIndexBeanTestClass();
        indexBean2.setValue(ComponentIndexNames.name, "Test");
        indexBean2.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean2.setValue(ComponentIndexNames.version, "1.2.3.XYZ");
        indexBean2.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/1.2.3/zip/XYZ");

        final int compare = indexBean1.compareVersion(indexBean2);
        Assert.assertTrue("The version 1.2.3.ABC should be less then 1.2.3.XYZ", compare == -1);
    }

    @Test
    public void test_compareVersion_diffQualifier_Date() {
        ComponentIndexBeanTestClass indexBean1 = new ComponentIndexBeanTestClass();
        indexBean1.setValue(ComponentIndexNames.name, "Test");
        indexBean1.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean1.setValue(ComponentIndexNames.version, "6.4.1.20170515_2024-SNAPSHOT");
        indexBean1.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/1.2.3/zip/ABC");

        ComponentIndexBeanTestClass indexBean2 = new ComponentIndexBeanTestClass();
        indexBean2.setValue(ComponentIndexNames.name, "Test");
        indexBean2.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean2.setValue(ComponentIndexNames.version, "6.4.1.20170522_2024-SNAPSHOT");
        indexBean2.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/1.2.3/zip/XYZ");

        final int compare = indexBean1.compareVersion(indexBean2);
        Assert.assertTrue("The version 6.4.1.20170515_2024-SNAPSHOT should be less then 6.4.1.20170522_2024-SNAPSHOT",
                compare == -1);
    }

    @Test
    public void test_compareVersion_diffM() {
        ComponentIndexBeanTestClass indexBean1 = new ComponentIndexBeanTestClass();
        indexBean1.setValue(ComponentIndexNames.name, "Test");
        indexBean1.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean1.setValue(ComponentIndexNames.version, "1.2.3");
        indexBean1.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/1.2.3/zip");

        // major
        ComponentIndexBeanTestClass indexBean2 = new ComponentIndexBeanTestClass();
        indexBean2.setValue(ComponentIndexNames.name, "Test");
        indexBean2.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean2.setValue(ComponentIndexNames.version, "3.1.1");
        indexBean2.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/3.1.1/zip");

        Assert.assertTrue("The version 1.2.3 should be less then 3.1.1", indexBean1.compareVersion(indexBean2) == -1);
        Assert.assertTrue("The version 1.2.3 should be less then 3.1.1", indexBean2.compareVersion(indexBean1) == 1);

        // minor
        ComponentIndexBeanTestClass indexBean3 = new ComponentIndexBeanTestClass();
        indexBean3.setValue(ComponentIndexNames.name, "Test");
        indexBean3.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean3.setValue(ComponentIndexNames.version, "1.3.0");
        indexBean3.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/1.3.0/zip");

        Assert.assertTrue("The version 1.2.3 should be less then 1.3.0", indexBean1.compareVersion(indexBean3) == -1);
        Assert.assertTrue("The version 1.2.3 should be less then 1.3.0", indexBean3.compareVersion(indexBean1) == 1);

        // micro
        ComponentIndexBeanTestClass indexBean4 = new ComponentIndexBeanTestClass();
        indexBean4.setValue(ComponentIndexNames.name, "Test");
        indexBean4.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean4.setValue(ComponentIndexNames.version, "1.2.4");
        indexBean4.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/1.2.4/zip");

        Assert.assertTrue("The version 1.2.3 should be less then 1.2.4", indexBean1.compareVersion(indexBean4) == -1);
        Assert.assertTrue("The version 1.2.3 should be less then 1.2.4", indexBean4.compareVersion(indexBean1) == 1);
    }

    @Test
    public void test_isSnapshot_DotOne() {
        ComponentIndexBeanTestClass indexBean = new ComponentIndexBeanTestClass();
        indexBean.setValue(ComponentIndexNames.name, "Test");
        indexBean.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean.setValue(ComponentIndexNames.version, "1.2.4.SNAPSHOT");
        indexBean.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/1.2.4.SNAPSHOT/zip");
        Assert.assertTrue("It should be SNAPSHOT", indexBean.isSnapshot());
    }

    @Test
    public void test_isSnapshot_WithOne() {
        ComponentIndexBeanTestClass indexBean = new ComponentIndexBeanTestClass();
        indexBean.setValue(ComponentIndexNames.name, "Test");
        indexBean.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean.setValue(ComponentIndexNames.version, "1.2.4-SNAPSHOT");
        indexBean.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/1.2.4-SNAPSHOT/zip");
        Assert.assertTrue("It should be SNAPSHOT", indexBean.isSnapshot());
    }

    @Test
    public void test_isSnapshot() {
        ComponentIndexBeanTestClass indexBean = new ComponentIndexBeanTestClass();
        indexBean.setValue(ComponentIndexNames.name, "Test");
        indexBean.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean.setValue(ComponentIndexNames.version, "1.2.4");
        indexBean.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/1.2.4/zip");
        Assert.assertFalse("It's not SNAPSHOT", indexBean.isSnapshot());
    }

    @Test
    public void test_clone() throws CloneNotSupportedException {
        ComponentIndexBeanTestClass indexBean = new ComponentIndexBeanTestClass();
        indexBean.setValue(ComponentIndexNames.name, "Test");
        indexBean.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean.setValue(ComponentIndexNames.version, "1.2.4");
        indexBean.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/1.2.4/zip");

        final ComponentIndexBean clone = indexBean.clone();
        Assert.assertFalse("Should be not same Object", indexBean == clone);
        Assert.assertEquals("Should have same settings", indexBean, clone);
    }

    @Test
    public void test_clone_SNAPSHOT() throws CloneNotSupportedException {
        ComponentIndexBeanTestClass indexBean = new ComponentIndexBeanTestClass();
        indexBean.setValue(ComponentIndexNames.name, "Test");
        indexBean.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean.setValue(ComponentIndexNames.version, "6.4.1.20170522_2024-SNAPSHOT");
        indexBean.setValue(ComponentIndexNames.mvn_uri,
                "mvn:org.talend.components/components-test/6.4.1.20170522_2024-SNAPSHOT/zip");

        final ComponentIndexBean clone = indexBean.clone();
        Assert.assertFalse("Should be not same Object", indexBean == clone);
        Assert.assertEquals("Should have same settings", indexBean, clone);
    }

    @Test
    public void test_cloneWithoutSnapshot() throws CloneNotSupportedException {
        ComponentIndexBeanTestClass indexBean = new ComponentIndexBeanTestClass();
        indexBean.setValue(ComponentIndexNames.name, "Test");
        indexBean.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean.setValue(ComponentIndexNames.version, "1.2.4-SNAPSHOT");
        indexBean.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/1.2.4-SNAPSHOT/zip");

        final ComponentIndexBean clone = indexBean.cloneWithoutSnapshot();
        Assert.assertFalse("Should be not same Object", indexBean == clone);
        Assert.assertNotEquals("Should have same settings", indexBean, clone);
        Assert.assertEquals("1.2.4", clone.getVersion());
    }

    @Test
    public void test_cloneWithoutDotSnapshot() throws CloneNotSupportedException {
        ComponentIndexBeanTestClass indexBean = new ComponentIndexBeanTestClass();
        indexBean.setValue(ComponentIndexNames.name, "Test");
        indexBean.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean.setValue(ComponentIndexNames.version, "1.2.4.SNAPSHOT");
        indexBean.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/1.2.4.SNAPSHOT/zip");

        final ComponentIndexBean clone = indexBean.cloneWithoutSnapshot();
        Assert.assertFalse("Should be not same Object", indexBean == clone);
        Assert.assertNotEquals("Should have same settings", indexBean, clone);
        Assert.assertEquals("1.2.4", clone.getVersion());
    }
}
