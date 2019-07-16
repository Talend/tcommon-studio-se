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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


/**
 * DOC xwen  class global comment. Detailled comment
 */
public class JaccardRealTest {

    @Test
    public void testTokenize1() {
        final String EXPECT = "last updated date";
        String testString = "Last_Updated_Date";
        String result = Jaccard.tokenize(testString);
        assertEquals(EXPECT, result);
    }

    @Test
    public void testTokenize2() {
        final String EXPECT = "phone number";
        String testString = "PhoneNumber";
        String result = Jaccard.tokenize(testString);
        assertEquals(EXPECT, result);
    }

    @Test
    public void testJaccardCompare1() {
        final double EXPECT = 0.0;
        String inputStr = "phone number";
        String outputStr = "name local c";
        double result = Jaccard.JaccardCompare(inputStr, outputStr);
        assertTrue((result == EXPECT));
    }

    @Test
    public void testJaccardCompare2() {
        final double EXPECT = 0.9357849740192014;
        String inputStr = "account";
        String outputStr = "account status c";
        double result = Jaccard.JaccardCompare(inputStr, outputStr);
        assertTrue((result == EXPECT));
    }

    @Test
    public void testJaccardCompare3() {
        final double EXPECT = 0.701838730514401;
        String inputStr = "account country";
        String outputStr = "physical country c";
        double result = Jaccard.JaccardCompare(inputStr, outputStr);
        assertTrue((result == EXPECT));
    }

}
