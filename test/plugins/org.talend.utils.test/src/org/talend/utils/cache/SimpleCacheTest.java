// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.utils.cache;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.talend.utils.cache.SimpleCache;
import org.talend.utils.thread.ThreadUtils;

/**
 * 
 * Class SimpleCacheTest.
 */
public class SimpleCacheTest {

    class SimpleCacheTestClass extends SimpleCache<Integer, String> {

        public SimpleCacheTestClass(long maxTime, int maxItems) {
            super(maxTime, maxItems);
        }

        @Override
        public int internalTimeListSize() {
            return super.internalTimeListSize();
        }

    };

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCacheWithTimeLimit() throws Exception {
        logTestName();

        int maxTime = 200; // 25 ms
        int maxItems = -1;
        int itemsCountToPut = 5;
        int expectedSize = 2;

        long sleepTimeBetweenEachAdd = 150; // 20 ms
        SimpleCacheTestClass simpleCache = createAndFillCache(maxTime, maxItems, itemsCountToPut, sleepTimeBetweenEachAdd);

        assertThat(simpleCache.size(), is(expectedSize));
        assertThat(simpleCache.internalTimeListSize(), is(expectedSize));
        assertThat(simpleCache.get(1), is(nullValue()));
        assertThat(simpleCache.get(2), is(nullValue()));
        assertThat(simpleCache.get(3), is(nullValue()));
        assertThat(simpleCache.get(4), is("4"));
        assertThat(simpleCache.get(5), is("5"));
        assertThat(simpleCache.getAddTime(1), is(nullValue()));
        assertThat(simpleCache.getAddTime(2), is(nullValue()));
        assertThat(simpleCache.getAddTime(3), is(nullValue()));
        assertThat(simpleCache.getAddTime(4), is(notNullValue()));
        assertThat(simpleCache.getAddTime(5), is(notNullValue()));
        assertTrue(simpleCache.getAddTime(4) < simpleCache.getAddTime(5));
    }

    @Test
    public void testCacheWithCountLimit() throws Exception {
        logTestName();
        int maxTime = -1;
        int maxItems = 2;
        int itemsCountToPut = 5;
        int expectedSize = maxItems;

        long sleepTimeBetweenEachAdd = 5; // 5 ms
        SimpleCacheTestClass simpleCache = createAndFillCache(maxTime, maxItems, itemsCountToPut, sleepTimeBetweenEachAdd);

        assertThat(simpleCache.size(), is(expectedSize));
        assertThat(simpleCache.internalTimeListSize(), is(expectedSize));
        assertThat(simpleCache.get(1), is(nullValue()));
        assertThat(simpleCache.get(2), is(nullValue()));
        assertThat(simpleCache.get(3), is(nullValue()));
        assertThat(simpleCache.get(4), is("4"));
        assertThat(simpleCache.get(5), is("5"));
        assertThat(simpleCache.getAddTime(1), is(nullValue()));
        assertThat(simpleCache.getAddTime(2), is(nullValue()));
        assertThat(simpleCache.getAddTime(3), is(nullValue()));
        assertThat(simpleCache.getAddTime(4), is(notNullValue()));
        assertThat(simpleCache.getAddTime(5), is(notNullValue()));
        assertTrue(simpleCache.getAddTime(4) < simpleCache.getAddTime(5));

    }

    @Test
    public void testCacheWithCountLimitAndPutSameKeyTwice() throws Exception {
        logTestName();
        int maxTime = -1;
        int maxItems = 2;
        int expectedSize = maxItems;

        long sleepTimeBetweenEachAdd = 5; // 5 ms

        SimpleCacheTestClass simpleCache = new SimpleCacheTestClass(maxTime, maxItems);
        int itemsCount = 5;
        int value = 0;
        value = 1;
        simpleCache.put(value, String.valueOf(value));
        ThreadUtils.waitTimeBool(5);
        value = 2;
        simpleCache.put(value, String.valueOf(value));
        ThreadUtils.waitTimeBool(5);
        value = 1;
        simpleCache.put(value, String.valueOf(value));
        ThreadUtils.waitTimeBool(5);

        assertThat(simpleCache.size(), is(expectedSize));
        assertThat(simpleCache.internalTimeListSize(), is(expectedSize));
        assertThat(simpleCache.get(1), is("1"));
        assertThat(simpleCache.get(2), is("2"));
        assertTrue(simpleCache.getAddTime(1) > simpleCache.getAddTime(2));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testCacheNoLimit1() throws Exception {

        int maxTime = -1;
        int maxItems = -1;
        int itemsCountToPut = 5;

        createAndFillCache(maxTime, maxItems, itemsCountToPut, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCacheNoLimit2() throws Exception {

        long maxTime = Long.MIN_VALUE;
        int maxItems = Integer.MIN_VALUE;
        int itemsCountToPut = 5;

        createAndFillCache(maxTime, maxItems, itemsCountToPut, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCacheNoLimit3() throws Exception {

        long maxTime = Long.MAX_VALUE;
        int maxItems = Integer.MAX_VALUE;
        int itemsCountToPut = 5;

        createAndFillCache(maxTime, maxItems, itemsCountToPut, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCacheNoLimit4() throws Exception {

        long maxTime = Integer.MAX_VALUE;
        int maxItems = Integer.MAX_VALUE;
        int itemsCountToPut = 5;

        createAndFillCache(maxTime, maxItems, itemsCountToPut, null);
    }

    @Test
    public void testNoCache1() throws Exception {

        int maxTime = -1;
        int maxItems = 0;
        int itemsCountToPut = 5;
        int exepectedItemsCount = 0;

        SimpleCacheTestClass simpleCache = createAndFillCache(maxTime, maxItems, itemsCountToPut, null);

        assertThat(simpleCache.size(), is(exepectedItemsCount));
        assertThat(simpleCache.internalTimeListSize(), is(exepectedItemsCount));
    }

    @Test
    public void testNoCache2() throws Exception {

        int maxTime = 0;
        int maxItems = 0;
        int itemsCountToPut = 5;
        int exepectedItemsCount = 0;

        SimpleCacheTestClass simpleCache = createAndFillCache(maxTime, maxItems, itemsCountToPut, null);

        assertThat(simpleCache.size(), is(exepectedItemsCount));
        assertThat(simpleCache.internalTimeListSize(), is(exepectedItemsCount));
    }

    @Test
    public void testNoCache3() throws Exception {

        int maxTime = 0;
        int maxItems = -1;
        int itemsCountToPut = 5;
        int exepectedItemsCount = 0;

        SimpleCacheTestClass simpleCache = createAndFillCache(maxTime, maxItems, itemsCountToPut, null);

        assertThat(simpleCache.size(), is(exepectedItemsCount));
        assertThat(simpleCache.internalTimeListSize(), is(exepectedItemsCount));
    }

    private SimpleCacheTestClass createAndFillCache(long maxTime, int maxItems, int itemsCountToPut,
            Long sleepTimeBetweenEachAdd) {
        SimpleCacheTestClass simpleCache = new SimpleCacheTestClass(maxTime, maxItems);
        int itemsCount = 5;
        for (int i = 1; i <= itemsCount; i++) {
            simpleCache.put(i, String.valueOf(i));
            if (sleepTimeBetweenEachAdd != null) {
                ThreadUtils.waitTimeBool(sleepTimeBetweenEachAdd);
            }
        }
        return simpleCache;
    }

    private void logTestName() {
        System.out.println("------------------------------------------------------------------");
        System.out.println(Thread.currentThread().getStackTrace()[2].getMethodName() + "()...");
    }

}
