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
package org.talend.utils.collections;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.talend.utils.collections.MapValueSorter;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public class MapValueSorterTest {

    /**
     * Test method for {@link org.talend.utils.collections.MapValueSorter#getMostFrequent(java.util.Map, int)}.
     */
    @Test
    public void testGetMostFrequent() {
        final List<Object> lessFrequent = new MapValueSorter().getMostFrequent(getMap(), 2);
        Assert.assertEquals(lessFrequent.size(), 2);
        Assert.assertEquals(lessFrequent.get(0), "nine");
        Assert.assertEquals(lessFrequent.get(1), "eight");
    }

    /**
     * DOC scorreia Comment method "getMap".
     *
     * @return
     */
    private Map<Object, Long> getMap() {
        Map<Object, Long> m = new HashMap<Object, Long>();
        m.put("one", 1L);
        m.put("two", 2L);
        m.put("three", 3L);
        m.put("four", 4L);
        m.put("five", 5L);
        m.put("six", 6L);
        m.put("seven", 7L);
        m.put("eight", 8L);
        m.put("nine", 9L);
        return m;
    }

    /**
     * Test method for {@link org.talend.utils.collections.MapValueSorter#getLessFrequent(java.util.Map, int)}.
     */
    @Test
    public void testGetLessFrequent() {
        final List<Object> lessFrequent = new MapValueSorter().getLessFrequent(getMap(), 3);
        Assert.assertEquals(lessFrequent.size(), 3);
        Assert.assertEquals(lessFrequent.get(0), "one");
        Assert.assertEquals(lessFrequent.get(1), "two");
        Assert.assertEquals(lessFrequent.get(2), "three");

    }

    /**
     * Test method for {@link org.talend.utils.collections.MapValueSorter#sortMap(java.util.Map, boolean)}.
     */
    @Test
    public void testSortMap() {
        final List<Object> lessFrequent = new MapValueSorter().sortMap(getMap(), true);
        System.out.println(lessFrequent);
    }

}
