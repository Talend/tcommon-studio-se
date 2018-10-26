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
package org.talend.core.utils;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

/**
 * DOC cmeng class global comment. Detailled comment
 */
@SuppressWarnings("nls")
public class TalendQuoteUtilsTest {

    private static final String QUOTES = "\"";

    private static final String QUOTES_2 = "'";

    /**
     * case: addQuotesIfNotExist(null value, ")<br>
     * expect: null
     */
    @Test
    public void testAddQuotesIfNotExist_Case_1() {
        String input = null;
        // String expect = null;
        String output = TalendQuoteUtils.addQuotesIfNotExist(input, QUOTES);
        Assert.assertTrue(output == null);
    }

    /**
     * case: addQuotesIfNotExist(empty value(length == 0), ")<br>
     * expect: [""]
     */
    @Test
    public void testAddQuotesIfNotExist_Case_2() {
        String input = "";
        String expect = QUOTES + "" + QUOTES;
        String output = TalendQuoteUtils.addQuotesIfNotExist(input, QUOTES);
        Assert.assertTrue(expect.equals(output));
    }

    /**
     * case: addQuotesIfNotExist(["], ")<br>
     * expect: [""]
     */
    @Test
    public void testAddQuotesIfNotExist_Case_3() {
        String input = QUOTES;
        String expect = QUOTES + QUOTES;
        String ouput = TalendQuoteUtils.addQuotesIfNotExist(input, QUOTES);
        Assert.assertTrue(expect.equals(ouput));
    }

    /**
     * case: addQuotesIfNotExist([""], ")<br>
     * expect: [""]
     */
    @Test
    public void testAddQuotesIfNotExist_Case_4() {
        String input = QUOTES + QUOTES;
        String expect = QUOTES + QUOTES;
        String ouput = TalendQuoteUtils.addQuotesIfNotExist(input, QUOTES);
        Assert.assertTrue(expect.equals(ouput));
    }

    /**
     * case: addQuotesIfNotExist(["JUnit], ")<br>
     * expect: ["JUnit"]
     */
    @Test
    public void testAddQuotesIfNotExist_Case_5() {
        String input = QUOTES + "JUnit";
        String expect = QUOTES + "JUnit" + QUOTES;
        String ouput = TalendQuoteUtils.addQuotesIfNotExist(input, QUOTES);
        Assert.assertTrue(expect.equals(ouput));
    }

    /**
     * case: addQuotesIfNotExist([JUnit"], ")<br>
     * expect: ["JUnit"]
     */
    @Test
    public void testAddQuotesIfNotExist_Case_6() {
        String input = "JUnit" + QUOTES;
        String expect = QUOTES + "JUnit" + QUOTES;
        String ouput = TalendQuoteUtils.addQuotesIfNotExist(input, QUOTES);
        Assert.assertTrue(expect.equals(ouput));
    }

    /**
     * case: addQuotesIfNotExist(["JUnit"], ")<br>
     * expect: ["JUnit"]
     */
    @Test
    public void testAddQuotesIfNotExist_Case_7() {
        String input = QUOTES + "JUnit" + QUOTES;
        String expect = QUOTES + "JUnit" + QUOTES;
        String ouput = TalendQuoteUtils.addQuotesIfNotExist(input, QUOTES);
        Assert.assertTrue(expect.equals(ouput));
    }

    /**
     * case: addQuotesIfNotExist([JUnit], ")<br>
     * expect: ["JUnit"]
     */
    @Test
    public void testAddQuotesIfNotExist_Case_8() {
        String input = "JUnit";
        String expect = QUOTES + "JUnit" + QUOTES;
        String ouput = TalendQuoteUtils.addQuotesIfNotExist(input, QUOTES);
        Assert.assertTrue(expect.equals(ouput));
    }

    /**
     * case: addQuotesIfNotExist(["JUnit"], ')<br>
     * expect: ['"JUnit"']
     */
    @Test
    public void testAddQuotesIfNotExist_Case_9() {
        String input = QUOTES + "JUnit" + QUOTES;
        String expect = QUOTES_2 + QUOTES + "JUnit" + QUOTES + QUOTES_2;
        String output = TalendQuoteUtils.addQuotesIfNotExist(input, QUOTES_2);
        Assert.assertTrue(expect.equals(output));
    }

    /**
     * case: addQuotesIfNotExist(['"JUnit"], ')<br>
     * expect: ['"JUnit"']
     */
    @Test
    public void testAddQuotesIfNotExist_Case_10() {
        String input = QUOTES_2 + QUOTES + "JUnit" + QUOTES;
        String expect = QUOTES_2 + QUOTES + "JUnit" + QUOTES + QUOTES_2;
        String output = TalendQuoteUtils.addQuotesIfNotExist(input, QUOTES_2);
        Assert.assertTrue(expect.equals(output));
    }

    /**
     * case: addQuotesIfNotExist(["JUnit"'], ')<br>
     * expect: ['"JUnit"']
     */
    @Test
    public void testAddQuotesIfNotExist_Case_11() {
        String input = QUOTES + "JUnit" + QUOTES + QUOTES_2;
        String expect = QUOTES_2 + QUOTES + "JUnit" + QUOTES + QUOTES_2;
        String output = TalendQuoteUtils.addQuotesIfNotExist(input, QUOTES_2);
        Assert.assertTrue(expect.equals(output));
    }

    /**
     * case: addQuotesIfNotExist(['"JUnit"'], ')<br>
     * expect: ['"JUnit"']
     */
    @Test
    public void testAddQuotesIfNotExist_Case_12() {
        String input = QUOTES_2 + QUOTES + "JUnit" + QUOTES + QUOTES_2;
        String expect = QUOTES_2 + QUOTES + "JUnit" + QUOTES + QUOTES_2;
        String output = TalendQuoteUtils.addQuotesIfNotExist(input, QUOTES_2);
        Assert.assertTrue(expect.equals(output));
    }

    @Test
    public void testAddPairQuotesIfNotExist() {
        String testStr = "abcd";
        String resultStr = TalendQuoteUtils.addPairQuotesIfNotExist(testStr);
        assertTrue((QUOTES + testStr + QUOTES).equals(resultStr));

        testStr = "\"abcd";
        resultStr = TalendQuoteUtils.addPairQuotesIfNotExist(testStr);
        assertEquals("\"\\\"abcd\"", resultStr);

        testStr = "abcd\"";
        resultStr = TalendQuoteUtils.addPairQuotesIfNotExist(testStr);
        assertEquals("\"abcd\\\"\"", resultStr);

        testStr = "\"abcd\"";
        resultStr = TalendQuoteUtils.addPairQuotesIfNotExist(testStr);
        assertTrue(testStr.equals(resultStr));

        testStr = "ab\"cd\"";
        resultStr = TalendQuoteUtils.addPairQuotesIfNotExist(testStr);
        assertEquals("\"ab\\\"cd\\\"\"", resultStr);

        testStr = "\"\"";
        resultStr = TalendQuoteUtils.addPairQuotesIfNotExist(testStr);
        assertEquals(testStr, resultStr);

        testStr = "\"";
        resultStr = TalendQuoteUtils.addPairQuotesIfNotExist(testStr);
        assertEquals("\"\\\"\"", resultStr);
    }
    
    @Test
    public void testAddQuotesForComplexusString() {
        String input = "\"abcd\"";
        String pid = "test";
        
        String resultStr = TalendQuoteUtils.addQuotesForComplexusString(input);
        assertTrue(input.equals(resultStr));
        
        input = "\"abcd\""+ "+" + pid;
        resultStr = TalendQuoteUtils.addQuotesForComplexusString(input);
        assertTrue(input.equals(resultStr));
        
        input = "abcd"+ "+" + pid;
        resultStr = TalendQuoteUtils.addQuotesForComplexusString(input);
        assertTrue("\"abcd+test\"".equals(resultStr));
        
        input = pid + "+" + "\"abcd\"";
        resultStr = TalendQuoteUtils.addQuotesForComplexusString(input);
        assertTrue(input.equals(resultStr));
        
        input = pid + "+" + "abcd";
        resultStr = TalendQuoteUtils.addQuotesForComplexusString(input);
        assertTrue("\"test+abcd\"".equals(resultStr));
        
        input = pid + "+" + "\"abcd";
        resultStr = TalendQuoteUtils.addQuotesForComplexusString(input);
        assertTrue("\"test+\\\"abcd\"".equals(resultStr));
        
        input = pid + "+" + "abcd\"";
        resultStr = TalendQuoteUtils.addQuotesForComplexusString(input);
        assertTrue("\"test+abcd\\\"\"".equals(resultStr));
        
        input = "abcd\"";
        resultStr = TalendQuoteUtils.addQuotesForComplexusString(input);
        assertTrue("\"abcd\\\"\"".equals(resultStr));
        
        input = "\"abcd";
        resultStr = TalendQuoteUtils.addQuotesForComplexusString(input);
        assertTrue("\"\\\"abcd\"".equals(resultStr));
        
        input = "abcd";
        resultStr = TalendQuoteUtils.addQuotesForComplexusString(input);
        assertTrue("\"abcd\"".equals(resultStr));
    }

}
