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
package org.talend.repository.ui.wizards.metadata.connection.files.excel;

import org.junit.Assert;
import org.junit.Test;

/**
 * created by hcyi on Jun 7, 2017 Detailled comment
 *
 */
public class ExcelReaderTest {

    @Test
    public void testGetColumnsTitle4Negative() {
        String[] res = ExcelReader.getColumnsTitle(-1);
        Assert.assertEquals(0, res.length);
    }

    @Test
    public void testGetColumnsTitle4Zero() {
        String[] res = ExcelReader.getColumnsTitle(0);
        Assert.assertEquals(0, res.length);
    }

    @Test
    public void testGetColumnsTitle4Xls() {
        String[] res = ExcelReader.getColumnsTitle(26);
        Assert.assertEquals("Z", res[25]); //$NON-NLS-1$

        res = ExcelReader.getColumnsTitle(27);
        Assert.assertEquals("AA", res[26]); //$NON-NLS-1$

        res = ExcelReader.getColumnsTitle(100);
        Assert.assertEquals("CV", res[99]); //$NON-NLS-1$

        res = ExcelReader.getColumnsTitle(254);
        Assert.assertEquals("IT", res[253]); //$NON-NLS-1$

        res = ExcelReader.getColumnsTitle(675);
        Assert.assertEquals("YY", res[674]); //$NON-NLS-1$

        res = ExcelReader.getColumnsTitle(676);
        Assert.assertEquals("YZ", res[675]); //$NON-NLS-1$

        res = ExcelReader.getColumnsTitle(677);
        Assert.assertEquals("ZA", res[676]); //$NON-NLS-1$

        res = ExcelReader.getColumnsTitle(701);
        Assert.assertEquals("ZY", res[700]); //$NON-NLS-1$

        res = ExcelReader.getColumnsTitle(702);
        Assert.assertEquals("ZZ", res[701]); //$NON-NLS-1$
    }

    @Test
    public void testGetColumnsTitle4Xlsx() {
        String[] res = ExcelReader.getColumnsTitle(703);
        Assert.assertEquals("AAA", res[702]); //$NON-NLS-1$

        res = ExcelReader.getColumnsTitle(1000);
        Assert.assertEquals("ALL", res[999]); //$NON-NLS-1$

        res = ExcelReader.getColumnsTitle(16384);
        Assert.assertEquals("XFD", res[16383]); //$NON-NLS-1$

        res = ExcelReader.getColumnsTitle(17576);
        Assert.assertEquals("YYZ", res[17575]); //$NON-NLS-1$

        res = ExcelReader.getColumnsTitle(17602);
        Assert.assertEquals("YZZ", res[17601]); //$NON-NLS-1$

        res = ExcelReader.getColumnsTitle(17603);
        Assert.assertEquals("ZAA", res[17602]); //$NON-NLS-1$
    }

    @Test
    public void testGetColumnsTitleIndex4Negative() {
        String[] res = ExcelReader.getColumnsTitle(-1, -1);
        Assert.assertEquals(0, res.length);
    }

    @Test
    public void testGetColumnsTitleIndex4Zero() {
        String[] res = ExcelReader.getColumnsTitle(0, 0);
        Assert.assertEquals(0, res.length);
    }

    @Test
    public void testGetColumnsTitleIndex4Xls() {
        String[] res = ExcelReader.getColumnsTitle(1, 26);
        Assert.assertEquals("Z", res[25]); //$NON-NLS-1$

        res = ExcelReader.getColumnsTitle(2, 27);
        Assert.assertEquals("AB", res[26]); //$NON-NLS-1$

        res = ExcelReader.getColumnsTitle(3, 100);
        Assert.assertEquals("CX", res[99]); //$NON-NLS-1$

        res = ExcelReader.getColumnsTitle(4, 254);
        Assert.assertEquals("IW", res[253]); //$NON-NLS-1$

        res = ExcelReader.getColumnsTitle(5, 675);
        Assert.assertEquals("ZC", res[674]); //$NON-NLS-1$

        res = ExcelReader.getColumnsTitle(1, 676);
        Assert.assertEquals("YZ", res[675]); //$NON-NLS-1$

        res = ExcelReader.getColumnsTitle(6, 677);
        Assert.assertEquals("ZF", res[676]); //$NON-NLS-1$

        res = ExcelReader.getColumnsTitle(7, 701);
        Assert.assertEquals("AAE", res[700]); //$NON-NLS-1$

        res = ExcelReader.getColumnsTitle(1, 702);
        Assert.assertEquals("ZZ", res[701]); //$NON-NLS-1$
    }

    @Test
    public void testGetColumnsTitleIndex4Xlsx() {
        String[] res = ExcelReader.getColumnsTitle(1, 703);
        Assert.assertEquals("AAA", res[702]); //$NON-NLS-1$

        res = ExcelReader.getColumnsTitle(2, 1000);
        Assert.assertEquals("ALM", res[999]); //$NON-NLS-1$

        res = ExcelReader.getColumnsTitle(3, 16384);
        Assert.assertEquals("XFF", res[16383]); //$NON-NLS-1$

        res = ExcelReader.getColumnsTitle(4, 17576);
        Assert.assertEquals("YZC", res[17575]); //$NON-NLS-1$

        res = ExcelReader.getColumnsTitle(5, 17602);
        Assert.assertEquals("ZAD", res[17601]); //$NON-NLS-1$

        res = ExcelReader.getColumnsTitle(6, 17603);
        Assert.assertEquals("ZAF", res[17602]); //$NON-NLS-1$
    }
}
