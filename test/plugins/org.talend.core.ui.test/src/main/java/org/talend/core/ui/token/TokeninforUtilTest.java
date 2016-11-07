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
package org.talend.core.ui.token;

import static org.junit.Assert.*;

import org.junit.Test;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONObject;


/**
 * created by nrousseau on Mar 28, 2016
 * Detailed comment
 *
 */
public class TokeninforUtilTest {

    @Test
    public void testMergeJSON() throws Exception {
        JSONObject source = new JSONObject();
        source.put("key1", "test");
        JSONObject sub1 = new JSONObject();
        sub1.put("myComponent", 5);
        JSONObject sub2 = new JSONObject();
        sub2.put("nb.test", 3);
        sub1.put("subkey1", sub2);
        source.put("key2", sub1);
        JSONArray array = new JSONArray();
        array.put("a");
        source.put("key3", array);
        JSONObject target = new JSONObject();
        
        TokenInforUtil.mergeJSON(source, target);
        assertEquals("test",target.get("key1"));
        assertEquals(5,target.getJSONObject("key2").getInt("myComponent"));
        assertEquals(3,target.getJSONObject("key2").getJSONObject("subkey1").getInt("nb.test"));
        assertEquals(1,target.getJSONArray("key3").length());
        assertEquals("a",target.getJSONArray("key3").get(0));

    
        TokenInforUtil.mergeJSON(source, target);
        assertEquals("test",target.get("key1"));
        assertEquals(10,target.getJSONObject("key2").getInt("myComponent"));
        assertEquals(6,target.getJSONObject("key2").getJSONObject("subkey1").getInt("nb.test"));
        assertEquals(1,target.getJSONArray("key3").length());
        assertEquals("a",target.getJSONArray("key3").get(0));
        
        source = new JSONObject();
        source.put("key1", "test2");
        source.put("key4", "test3");
        sub1 = new JSONObject();
        sub1.put("myComponent", 2);
        sub2 = new JSONObject();
        sub2.put("nb.test", 1);
        sub1.put("subkey1", sub2);
        source.put("key2", sub1);
        array = new JSONArray();
        array.put("b");
        source.put("key3", array);

        
        TokenInforUtil.mergeJSON(source, target);
        assertEquals("test2",target.get("key1"));
        assertEquals("test3",target.get("key4"));
        assertEquals(12,target.getJSONObject("key2").getInt("myComponent"));
        assertEquals(7,target.getJSONObject("key2").getJSONObject("subkey1").getInt("nb.test"));
        assertEquals(2,target.getJSONArray("key3").length());
        assertEquals("a",target.getJSONArray("key3").get(0));
        assertEquals("b",target.getJSONArray("key3").get(1));
    }
}
