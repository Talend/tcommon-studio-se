// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.commons.utils;

import junit.framework.Assert;

import org.junit.Test;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class StringUtilsTest {

    @SuppressWarnings("nls")
    @Test
    public void testcompareStringDigit() {
        String s1 = "tDB2Output_1";
        String s2 = "tDB2Output_1";
        Assert.assertTrue(StringUtils.compareStringDigit(s1, s2) == 0);

        s1 = "tDB2Output_1";
        s2 = "tDB2Output_2";
        Assert.assertTrue(StringUtils.compareStringDigit(s1, s2) < 0);

        s1 = "tDB2Output_1";
        s2 = "tDB2Output_11";
        Assert.assertTrue(StringUtils.compareStringDigit(s1, s2) < 0);

        s1 = "tDB2Output_12";
        s2 = "tDB2Output_11";
        Assert.assertTrue(StringUtils.compareStringDigit(s1, s2) > 0);

        s1 = "tDB2Output_2";
        s2 = "tDB2Output_11";
        Assert.assertTrue(StringUtils.compareStringDigit(s1, s2) < 0);

        //
        s1 = "tDB2Output_2_input";
        s2 = "tDB2Output_1_input";
        Assert.assertTrue(StringUtils.compareStringDigit(s1, s2) > 0);

        s1 = "tDB2Output_2_input";
        s2 = "tDB2Output_11_input";
        Assert.assertTrue(StringUtils.compareStringDigit(s1, s2) < 0);

        s1 = "tDB2Output_12_input";
        s2 = "tDB2Output_111_input";
        Assert.assertTrue(StringUtils.compareStringDigit(s1, s2) < 0);

        s1 = "tDB2Output_1_input";
        s2 = "tDB2Output_2_input";
        Assert.assertTrue(StringUtils.compareStringDigit(s1, s2) < 0);

        //
        s1 = "tDB2Output_1_input1";
        s2 = "tDB2Output_1_input2";
        Assert.assertTrue(StringUtils.compareStringDigit(s1, s2) < 0);

        s1 = "tDB2Output_1_input11";
        s2 = "tDB2Output_1_input2";
        Assert.assertTrue(StringUtils.compareStringDigit(s1, s2) > 0);

        s1 = "tDB2Output_1_input11";
        s2 = "tDB2Output_1_input12";
        Assert.assertTrue(StringUtils.compareStringDigit(s1, s2) < 0);

        s1 = "tDB2Output_1_input12a";
        s2 = "tDB2Output_1_input11b";
        Assert.assertTrue(StringUtils.compareStringDigit(s1, s2) > 0);

        //
        s1 = "tDB2Output_1_input_11";
        s2 = "tDB2Output_1_input12";
        Assert.assertTrue(StringUtils.compareStringDigit(s1, s2) > 0);

        s1 = "tDB2Output_1_input11";
        s2 = "tDB2Output_1_input_12";
        Assert.assertTrue(StringUtils.compareStringDigit(s1, s2) < 0);

        s1 = "tDB2Output_1_input_11";
        s2 = "tDB2Output_1_input_12";
        Assert.assertTrue(StringUtils.compareStringDigit(s1, s2) < 0);

        s1 = "tDB2Output_1_input_11A";
        s2 = "tDB2Output_1_input_11B";
        Assert.assertTrue(StringUtils.compareStringDigit(s1, s2) < 0);

        s1 = "tDB2Output_1_input_11a";
        s2 = "tDB2Output_1_input_11b";
        Assert.assertTrue(StringUtils.compareStringDigit(s1, s2) < 0);

        s1 = "tDB2Output_1_input_11A1";
        s2 = "tDB2Output_1_input_11A2";
        Assert.assertTrue(StringUtils.compareStringDigit(s1, s2) < 0);

        s1 = "tDB2Output_1_input_11A2";
        s2 = "tDB2Output_1_input_11A12";
        Assert.assertTrue(StringUtils.compareStringDigit(s1, s2) < 0);

        //
        s1 = "tDB2Output_2_input_11A2";
        s2 = "tDB2Output_1_input_11A12";
        Assert.assertTrue(StringUtils.compareStringDigit(s1, s2) > 0);

        //
        s1 = "tdb2Output_1";
        s2 = "tDB2Output_1";
        Assert.assertTrue(StringUtils.compareStringDigit(s1, s2, false) > 0);

        s1 = "tDB2Output_1_input_11a2";
        s2 = "tDB2Output_1_input_11A12";
        Assert.assertTrue(StringUtils.compareStringDigit(s1, s2, false) > 0);
    }
}
