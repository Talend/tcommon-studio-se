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

import static org.junit.Assert.assertTrue;

import org.junit.Test;


/**
 * DOC xwen  class global comment. Detailled comment
 */
public class LevenshteinRealTest {

    @Test
    public void testGetLevenshteinScore1() {
        final double EXPECT = 0.2857142857142857;
        String inputStr = "name";
        String outputStr = "created";
        double result = Levenshtein.getLevenshteinScore(inputStr, outputStr);
        assertTrue((result == EXPECT));

    }

    @Test
    public void testGetLevenshteinScore2() {
        final double EXPECT = 0.38461538461538464;
        String inputStr = "name";
        String outputStr = "account_name";
        double result = Levenshtein.getLevenshteinScore(inputStr, outputStr);
        assertTrue((result == EXPECT));

    }

    @Test
    public void testGetLevenshteinScore3() {
        final double EXPECT = 0.11764705882352944;
        String inputStr = "ZIPCODE";
        String outputStr = "BillingPostalCode";
        double result = Levenshtein.getLevenshteinScore(inputStr, outputStr);
        assertTrue((result == EXPECT));

    }

}
