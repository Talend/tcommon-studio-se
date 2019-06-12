// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.utils.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class JSONUtilTest {

    @Test
    public void testMerge_Null() throws JSONException {
        JSONObject mergedJson = JSONUtil.merge(null, null);
        assertNotNull(mergedJson);
        assertEquals(0, mergedJson.length()); // empty

        JSONObject source = new JSONObject();
        source.put("s_str1", "abc");
        mergedJson = JSONUtil.merge(source, null);
        assertNotNull(mergedJson);
        assertEquals("abc", mergedJson.getString("s_str1"));

        JSONObject target = new JSONObject();
        target.put("t_str1", "123");
        mergedJson = JSONUtil.merge(null, target);
        assertNotNull(mergedJson);
        assertEquals("123", mergedJson.getString("t_str1"));

        mergedJson = JSONUtil.merge(source, target);

        assertEquals(1, source.length());
        assertEquals(1, target.length());

        assertEquals("abc", mergedJson.getString("s_str1"));
        assertEquals("123", mergedJson.getString("t_str1"));
    }

    @Test
    public void testMerge_Flat() throws JSONException {
        Map<String, Object> s = new HashMap<String, Object>();
        s.put("s_str1", "abc");
        s.put("s_str2", "xyz");
        s.put("s_int1", 123);
        s.put("s_bool1", true);
        JSONObject source = new JSONObject(s);

        Map<String, Object> t = new HashMap<String, Object>();
        t.put("t_str1", "123");
        t.put("t_str2", "890");
        t.put("t_int1", 456);
        t.put("t_bool1", false);
        JSONObject target = new JSONObject(t);

        JSONObject mergedJson = JSONUtil.merge(source, target);

        assertEquals(4, source.length());
        assertEquals(4, target.length());

        assertEquals("abc", mergedJson.getString("s_str1"));
        assertEquals("xyz", mergedJson.getString("s_str2"));
        assertEquals(123, mergedJson.getInt("s_int1"));
        assertEquals(true, mergedJson.getBoolean("s_bool1"));

        assertEquals("123", mergedJson.getString("t_str1"));
        assertEquals("890", mergedJson.getString("t_str2"));
        assertEquals(456, mergedJson.getInt("t_int1"));
        assertEquals(false, mergedJson.getBoolean("t_bool1"));

    }

    @Test
    public void testMerge_Hierarchy() throws JSONException {
        Map<String, Object> s = new HashMap<String, Object>();
        s.put("s_int1", 123);
        s.put("s_bool1", true);
        JSONObject source = new JSONObject(s);
        JSONObject s2Children = new JSONObject();
        s2Children.put("s2_str1", "aaa");
        s2Children.put("s2_int1", 789);
        s2Children.put("s2_bool1", false);
        JSONObject s3Children = new JSONObject();
        s3Children.put("s3_str1", "xxx");
        s3Children.put("s3_int1", 111);
        s2Children.put("s3_children", s3Children);
        source.put("s2_children", s2Children);

        Map<String, Object> t = new HashMap<String, Object>();
        t.put("t_int1", 456);
        t.put("t_bool1", false);
        JSONObject target = new JSONObject(t);

        JSONObject mergedJson = JSONUtil.merge(source, target);

        assertEquals(3, source.length());
        assertEquals(2, target.length());

        assertEquals(123, mergedJson.getInt("s_int1"));
        assertEquals(true, mergedJson.getBoolean("s_bool1"));

        JSONObject children2 = mergedJson.getJSONObject("s2_children");
        assertNotNull(children2);
        assertEquals("aaa", children2.getString("s2_str1"));
        assertEquals(789, children2.getInt("s2_int1"));
        assertEquals(false, children2.getBoolean("s2_bool1"));

        JSONObject children3 = children2.getJSONObject("s3_children");
        assertNotNull(children3);
        assertEquals("xxx", children3.getString("s3_str1"));
        assertEquals(111, children3.getInt("s3_int1"));

        assertEquals(456, mergedJson.getInt("t_int1"));
        assertEquals(false, mergedJson.getBoolean("t_bool1"));

    }

    @Test
    public void testMerge_Hierarchy_SameKey() throws JSONException {
        Map<String, Object> s = new HashMap<String, Object>();
        s.put("s_int1", 123);
        s.put("s_bool1", true);
        JSONObject source = new JSONObject(s);
        JSONObject s2Children = new JSONObject();
        s2Children.put("s2_str1", "aaa");
        s2Children.put("s2_int1", 789);
        s2Children.put("s2_bool1", false);
        JSONObject s3Children = new JSONObject();
        s3Children.put("s3_str1", "ccc");
        s2Children.put("3children", s3Children);
        source.put("children", s2Children);

        Map<String, Object> t = new HashMap<String, Object>();
        t.put("t_int1", 456);
        t.put("t_bool1", false);
        JSONObject target = new JSONObject(t);
        JSONObject t2Children = new JSONObject();
        t2Children.put("t2_str1", "bbb");
        t2Children.put("t2_int1", 678);
        t2Children.put("t2_bool1", true);
        JSONObject t3Children = new JSONObject();
        t3Children.put("t3_str1", "zzz");
        t3Children.put("t3_int1", 222);
        t2Children.put("3children", t3Children);
        target.put("children", t2Children);

        JSONObject mergedJson = JSONUtil.merge(source, target);

        assertEquals(3, source.length());
        assertEquals(3, target.length());

        assertEquals(123, mergedJson.getInt("s_int1"));
        assertEquals(true, mergedJson.getBoolean("s_bool1"));
        JSONObject children = mergedJson.getJSONObject("children");
        assertNotNull(children);
        assertEquals("aaa", children.getString("s2_str1"));
        assertEquals(789, children.getInt("s2_int1"));
        assertEquals(false, children.getBoolean("s2_bool1"));
        assertEquals("bbb", children.getString("t2_str1"));
        assertEquals(678, children.getInt("t2_int1"));
        assertEquals(true, children.getBoolean("t2_bool1"));

        JSONObject children3 = children.getJSONObject("3children");
        assertNotNull(children3);
        assertEquals("ccc", children3.getString("s3_str1"));
        assertEquals("zzz", children3.getString("t3_str1"));
        assertEquals(222, children3.getInt("t3_int1"));

        assertEquals(456, mergedJson.getInt("t_int1"));
        assertEquals(false, mergedJson.getBoolean("t_bool1"));

    }

    @Test
    public void testMerge_Hierarchy_Overwrite() throws JSONException {
        Map<String, Object> s = new HashMap<String, Object>();
        s.put("s_int1", 123);
        s.put("s_bool1", true);
        JSONObject source = new JSONObject(s);
        JSONObject s2Children = new JSONObject();
        s2Children.put("s2_str1", "aaa");
        s2Children.put("s2_int1", 789);
        s2Children.put("t2_int1", 0);
        s2Children.put("s2_bool1", false);
        source.put("children", s2Children);

        Map<String, Object> t = new HashMap<String, Object>();
        t.put("t_int1", 456);
        t.put("t_bool1", false);
        JSONObject target = new JSONObject(t);
        JSONObject t2Children = new JSONObject();
        t2Children.put("t2_str1", "bbb");
        t2Children.put("t2_int1", 678);
        t2Children.put("t2_bool1", true);
        t2Children.put("s2_bool1", true);
        target.put("children", t2Children);

        JSONObject mergedJson = JSONUtil.merge(source, target);

        assertEquals(3, source.length());
        assertEquals(3, target.length());

        assertEquals(123, mergedJson.getInt("s_int1"));
        assertEquals(true, mergedJson.getBoolean("s_bool1"));
        JSONObject children = mergedJson.getJSONObject("children");
        assertNotNull(children);
        assertEquals("aaa", children.getString("s2_str1"));
        assertEquals(789, children.getInt("s2_int1"));
        assertEquals(false, children.getBoolean("s2_bool1")); // source will overwrite target one
        assertEquals("bbb", children.getString("t2_str1"));
        assertEquals(0, children.getInt("t2_int1")); // source will overwrite target one
        assertEquals(true, children.getBoolean("t2_bool1"));

        assertEquals(456, mergedJson.getInt("t_int1"));
        assertEquals(false, mergedJson.getBoolean("t_bool1"));

    }

    @Test
    public void testMerge_Array() throws JSONException {
        Map<String, Object> s = new HashMap<String, Object>();
        s.put("s_str1", "abc");
        JSONObject source = new JSONObject(s);
        JSONArray sArr = new JSONArray();
        sArr.put("s1");
        sArr.put("s2");
        sArr.put(123);
        source.put("s_arr", sArr);

        Map<String, Object> t = new HashMap<String, Object>();
        t.put("t_int1", 123);
        JSONObject target = new JSONObject(t);
        JSONArray tArr = new JSONArray();
        tArr.put("t1");
        tArr.put("t2");
        tArr.put("t3");
        target.put("t_arr", tArr);

        JSONObject mergedJson = JSONUtil.merge(source, target);

        assertEquals(2, source.length());
        assertEquals(2, target.length());

        assertEquals("abc", mergedJson.getString("s_str1"));
        JSONArray sa = mergedJson.getJSONArray("s_arr");
        assertNotNull(sa);
        assertEquals(3, sa.length());
        assertEquals("s1", sa.getString(0));
        assertEquals("s2", sa.getString(1));
        assertEquals(123, sa.getInt(2));

        assertEquals(123, mergedJson.getInt("t_int1"));
        JSONArray ta = mergedJson.getJSONArray("t_arr");
        assertNotNull(ta);
        assertEquals(3, ta.length());
        assertEquals("t1", ta.getString(0));
        assertEquals("t2", ta.getString(1));
        assertEquals("t3", ta.getString(2));

    }

    @Test
    public void testMerge_Array_SameKey() throws JSONException {
        Map<String, Object> s = new HashMap<String, Object>();
        s.put("s_str1", "abc");
        JSONObject source = new JSONObject(s);
        JSONArray sArr = new JSONArray();
        sArr.put("s1");
        sArr.put("s2");
        sArr.put(123);
        source.put("arr", sArr);

        Map<String, Object> t = new HashMap<String, Object>();
        t.put("t_int1", 123);
        JSONObject target = new JSONObject(t);
        JSONArray tArr = new JSONArray();
        tArr.put("t1");
        tArr.put("t2");
        tArr.put(true);
        target.put("arr", tArr);

        JSONObject mergedJson = JSONUtil.merge(source, target);

        assertEquals(2, source.length());
        assertEquals(2, target.length());

        assertEquals("abc", mergedJson.getString("s_str1"));
        JSONArray ta = mergedJson.getJSONArray("arr");
        assertNotNull(ta);
        assertEquals(6, ta.length());
        assertEquals("t1", ta.getString(0));
        assertEquals("t2", ta.getString(1));
        assertEquals(true, ta.getBoolean(2));
        assertEquals("s1", ta.getString(3));
        assertEquals("s2", ta.getString(4));
        assertEquals(123, ta.getInt(5));

        assertEquals(123, mergedJson.getInt("t_int1"));

    }

    @Test
    public void testMerge_Array_Object() throws JSONException {
        Map<String, Object> s = new HashMap<String, Object>();
        s.put("s_str1", "abc");
        JSONObject source = new JSONObject(s);
        JSONArray sArr = new JSONArray();
        sArr.put("s1");
        sArr.put(123);
        JSONObject sObj = new JSONObject();
        sObj.put("s_arr_obj", "1111");
        sArr.put(sObj);
        source.put("arr", sArr);

        Map<String, Object> t = new HashMap<String, Object>();
        t.put("t_int1", 123);
        JSONObject target = new JSONObject(t);
        JSONArray tArr = new JSONArray();
        tArr.put("t1");
        tArr.put(true);
        target.put("arr", tArr);

        JSONObject mergedJson = JSONUtil.merge(source, target);

        assertEquals(2, source.length());
        assertEquals(2, target.length());

        assertEquals("abc", mergedJson.getString("s_str1"));
        JSONArray ta = mergedJson.getJSONArray("arr");
        assertNotNull(ta);
        assertEquals(5, ta.length());
        assertEquals("t1", ta.getString(0));
        assertEquals(true, ta.getBoolean(1));
        assertEquals("s1", ta.getString(2));
        assertEquals(123, ta.getInt(3));
        JSONObject obj = ta.getJSONObject(4);
        assertNotNull(obj);
        assertEquals("1111", obj.getString("s_arr_obj"));

        assertEquals(123, mergedJson.getInt("t_int1"));

    }
}
