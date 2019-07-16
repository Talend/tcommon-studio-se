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
package org.talend.utils.string;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class StringUtilitiesRealTest {

    /**
     * Test method for {@link org.talend.utils.string.StringUtilities#getRandomString(java.lang.Integer)}
     */
    @Test
    public void testGetRandomString() {
        int strLen = 6;
        int strSize = 100;
        Set<String> strs = new HashSet<String>();
        for (int i = 0; i < strSize; ++i) {
            String str = StringUtilities.getRandomString(strLen);
            assertEquals(str.length(), strLen);
            strs.add(str);
        }
        assertEquals(strs.size(), strSize);
    }

    @SuppressWarnings("nls")
    @Test
    public void testRemoveEndingString1() {
        final String EXPECT = "removeEnding";
        final String ENDING = "/";
        String testString = EXPECT;
        String result = StringUtilities.removeEndingString(testString, ENDING);
        assertTrue(EXPECT.equals(result));

    }

    @SuppressWarnings("nls")
    @Test
    public void testRemoveEndingString2() {
        final String EXPECT = "removeEnding";
        final String ENDING = "/";
        String testString = ENDING + EXPECT;
        String result = StringUtilities.removeEndingString(testString, ENDING);
        assertTrue(testString.equals(result));
    }

    @SuppressWarnings("nls")
    @Test
    public void testRemoveEndingString3() {
        final String EXPECT = "removeEnding";
        final String ENDING = "/";
        String testString = ENDING + ENDING + EXPECT;
        String result = StringUtilities.removeEndingString(testString, ENDING);
        assertTrue(testString.equals(result));
    }

    @SuppressWarnings("nls")
    @Test
    public void testRemoveEndingString4() {
        final String EXPECT = "removeEnding";
        final String ENDING = "/";
        String testString = EXPECT + ENDING;
        String result = StringUtilities.removeEndingString(testString, ENDING);
        assertTrue(EXPECT.equals(result));
    }

    @SuppressWarnings("nls")
    @Test
    public void testRemoveEndingString5() {
        final String EXPECT = "removeEnding";
        final String ENDING = "/";
        String testString = EXPECT + ENDING + ENDING;
        String result = StringUtilities.removeEndingString(testString, ENDING);
        assertTrue(EXPECT.equals(result));
    }

    @SuppressWarnings("nls")
    @Test
    public void testRemoveEndingString6() {
        final String EXPECT = "removeEnding";
        final String ENDING = "/";
        String testString = ENDING + EXPECT + ENDING;
        String result = StringUtilities.removeEndingString(testString, ENDING);
        assertTrue((ENDING + EXPECT).equals(result));
    }

    @SuppressWarnings("nls")
    @Test
    public void testRemoveEndingString7() {
        final String EXPECT = "removeEnding";
        final String ENDING = "/";
        String testString = ENDING + ENDING + EXPECT + ENDING + ENDING;
        String result = StringUtilities.removeEndingString(testString, ENDING);
        assertTrue((ENDING + ENDING + EXPECT).equals(result));
    }

    @SuppressWarnings("nls")
    @Test
    public void testRemoveStartingString1() {
        final String EXPECT = "removeStarting";
        final String STARTING = "/";
        String testString = EXPECT;
        String result = StringUtilities.removeStartingString(testString, STARTING);
        assertTrue(EXPECT.equals(result));
    }

    @SuppressWarnings("nls")
    @Test
    public void testRemoveStartingString2() {
        final String EXPECT = "removeStarting";
        final String STARTING = "/";
        String testString = STARTING + EXPECT;
        String result = StringUtilities.removeStartingString(testString, STARTING);
        assertTrue(EXPECT.equals(result));
    }

    @SuppressWarnings("nls")
    @Test
    public void testRemoveStartingString3() {
        final String EXPECT = "removeStarting";
        final String STARTING = "/";
        String testString = STARTING + STARTING + EXPECT;
        String result = StringUtilities.removeStartingString(testString, STARTING);
        assertTrue(EXPECT.equals(result));
    }

    @SuppressWarnings("nls")
    @Test
    public void testRemoveStartingString4() {
        final String EXPECT = "removeStarting";
        final String STARTING = "/";
        String testString = EXPECT + STARTING;
        String result = StringUtilities.removeStartingString(testString, STARTING);
        assertTrue(testString.equals(result));
    }

    @SuppressWarnings("nls")
    @Test
    public void testRemoveStartingString5() {
        final String EXPECT = "removeStarting";
        final String STARTING = "/";
        String testString = EXPECT + STARTING + STARTING;
        String result = StringUtilities.removeStartingString(testString, STARTING);
        assertTrue(testString.equals(result));
    }

    @SuppressWarnings("nls")
    @Test
    public void testRemoveStartingString6() {
        final String EXPECT = "removeStarting";
        final String STARTING = "/";
        String testString = STARTING + EXPECT + STARTING;
        String result = StringUtilities.removeStartingString(testString, STARTING);
        assertTrue((EXPECT + STARTING).equals(result));
    }

    @SuppressWarnings("nls")
    @Test
    public void testRemoveStartingString7() {
        final String EXPECT = "removeStarting";
        final String STARTING = "/";
        String testString = STARTING + STARTING + EXPECT + STARTING + STARTING;
        String result = StringUtilities.removeStartingString(testString, STARTING);
        assertTrue((EXPECT + STARTING + STARTING).equals(result));
    }
}
