// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.utils.collections;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

/**
 * created by zhao on Aug 15, 2014 Detailled comment
 * 
 */
public class BidiMultiMapTest {

    @Test
    public void testGetKeys() {
        BidiMultiMap<String, String> map = new BidiMultiMap<String, String>();
        map.put("A", "B");
        map.put("B", "D");
        map.put("C", "D");

        assertEquals("B", map.get("A"));
        List<String> bs = map.getKeys("B");
        assertEquals(1, bs.size());
        assertEquals("A", bs.get(0));

        assertEquals("D", map.get("B"));
        List<String> ds = map.getKeys("D");
        assertEquals(2, ds.size());
        assertEquals("B", ds.get(0));
        assertEquals("C", ds.get(1));

        assertEquals("D", map.get("C"));
        List<String> cs = map.getKeys("C");
        assertNull(cs);

        map.clear();
        assertNull(map.getKeys("B"));

        map.put("A", "C");
        map.put("B", "C");
        map.put("C", "D");
        List<String> ab = map.getKeys("C");
        for (String key : ab) {
            map.put(key, "D");
        }
        // Then will be <A,D>,<B,D>,<C,D>
        assertEquals(3, map.size());
        assertEquals("D", map.get("A"));
        assertEquals("D", map.get("B"));
        assertEquals("D", map.get("C"));
        List<String> abc = map.getKeys("D");
        assertEquals(3, abc.size());
        assertEquals("C", abc.get(0));
        assertEquals("A", abc.get(1));
        assertEquals("B", abc.get(2));

    }
}
