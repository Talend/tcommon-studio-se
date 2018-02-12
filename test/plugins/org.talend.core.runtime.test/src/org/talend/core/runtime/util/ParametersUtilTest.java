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
package org.talend.core.runtime.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ParametersUtilTest {

    @Test
    public void test_hasBoolFlag_empty() {
        assertFalse(ParametersUtil.hasBoolFlag(null, null));
        assertFalse(ParametersUtil.hasBoolFlag(Collections.emptyMap(), null));
        assertFalse(ParametersUtil.hasBoolFlag(Collections.emptyMap(), ""));
        assertFalse(ParametersUtil.hasBoolFlag(Collections.emptyMap(), "abc"));
    }

    @Test
    public void test_hasBoolFlag_nonExisted() {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("key1", null);

        assertFalse(ParametersUtil.hasBoolFlag(parameters, null));
        assertFalse(ParametersUtil.hasBoolFlag(parameters, "abc"));
        assertFalse(ParametersUtil.hasBoolFlag(parameters, "key1"));
    }

    @Test
    public void test_hasBoolFlag_existed() {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("key1", "abc");
        assertFalse(ParametersUtil.hasBoolFlag(parameters, "key1"));

        parameters.put("key2", false);
        assertFalse(ParametersUtil.hasBoolFlag(parameters, "key2"));

        parameters.put("key3", true);
        assertTrue(ParametersUtil.hasBoolFlag(parameters, "key3"));

        parameters.put("key4", Boolean.TRUE);
        assertTrue(ParametersUtil.hasBoolFlag(parameters, "key4"));
    }

    @Test
    public void test_getObject_empty() {
        assertNull(ParametersUtil.getObject(null, null, null));
        assertNull(ParametersUtil.getObject(Collections.emptyMap(), null, null));
        assertNull(ParametersUtil.getObject(Collections.emptyMap(), "", null));
        assertNull(ParametersUtil.getObject(Collections.emptyMap(), "abc", null));
    }

    @Test
    public void test_getObject_String() {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("key1", "abc");

        Object object1 = ParametersUtil.getObject(parameters, "key1", null);
        assertNotNull(object1);
        assertEquals("abc", object1);

        parameters.put("key2", "xyz");
        Object object2 = ParametersUtil.getObject(parameters, "key2", String.class);
        assertNotNull(object2);
        assertEquals("xyz", object2);
    }

    @Test
    public void test_getObject_List() {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("key1", Arrays.asList("abc", "xyz"));

        Object object1 = ParametersUtil.getObject(parameters, "key1", List.class);
        assertNotNull(object1);
        assertTrue(object1 instanceof List);
        List list = (List) object1;
        assertEquals("abc", list.get(0));
        assertEquals("xyz", list.get(1));
    }

    static class A {

    }

    static interface X {

    }

    static class B extends A implements X {

    }

    static class C extends A implements X {

    }

    static class D extends C {

    }

    @Test
    public void test_getObject_Object() {
        Map<String, Object> parameters = new HashMap<String, Object>();
        A a = new A();
        B b = new B();
        C c = new C();
        D d = new D();

        parameters.put("keyA", a);
        parameters.put("keyB", b);
        parameters.put("keyC", c);
        parameters.put("keyD", d);

        // parent class
        Object objectA = ParametersUtil.getObject(parameters, "keyA", A.class);
        assertNotNull(objectA);
        assertEquals(a, objectA);

        Object objectB = ParametersUtil.getObject(parameters, "keyB", A.class);
        assertNotNull(objectB);
        assertEquals(b, objectB);

        Object objectC = ParametersUtil.getObject(parameters, "keyC", A.class);
        assertNotNull(objectC);
        assertEquals(c, objectC);

        Object objectD = ParametersUtil.getObject(parameters, "keyD", A.class);
        assertNotNull(objectD);
        assertEquals(d, objectD);

        // interface
        Object objectB2 = ParametersUtil.getObject(parameters, "keyB", X.class);
        assertNotNull(objectB2);
        assertEquals(b, objectB2);

        Object objectC2 = ParametersUtil.getObject(parameters, "keyC", X.class);
        assertNotNull(objectC2);
        assertEquals(c, objectC2);

        Object objectD2 = ParametersUtil.getObject(parameters, "keyD", X.class);
        assertNotNull(objectD2);
        assertEquals(d, objectD2);

        //
        Object objectDc = ParametersUtil.getObject(parameters, "keyD", C.class);
        assertNotNull(objectDc);
        assertEquals(d, objectDc);
    }
}
