import java.io.IOException;
import java.util.List;

import jxl.read.biff.BiffException;

import org.junit.Assert;
import org.junit.Test;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.repository.ui.wizards.metadata.connection.files.excel.ExcelReader;

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

/**
 * DOC Administrator class global comment. Detailed comment
 */
public class TestExcelReader {

    /**
     * Test method for
     * {@link org.talend.repository.ui.wizards.metadata.connection.files.excel.ExcelReader#readSheet(java.lang.String)}.
     */
    @Test
    public void testReadSheet() {
        try {
            ExcelReader reader = new ExcelReader("/home/yexiaowei/testdata/test.xls"); //$NON-NLS-1$
            List res = reader.readSheet("Sheet1"); //$NON-NLS-1$
            Assert.assertEquals(7, res.size());

        } catch (BiffException e) {
            // e.printStackTrace();
            ExceptionHandler.process(e);
        } catch (IOException e) {
            // e.printStackTrace();
            ExceptionHandler.process(e);
        }
    }

    /**
     * Test method for
     * {@link org.talend.repository.ui.wizards.metadata.connection.files.excel.ExcelReader#getColumnsTitle(int)}.
     */
    @Test
    public void testGetColumnsTitleInt() {
        String[] res = ExcelReader.getColumnsTitle(26);
        Assert.assertEquals("Z", res[25]); //$NON-NLS-1$

        res = ExcelReader.getColumnsTitle(27);
        Assert.assertEquals("AA", res[26]); //$NON-NLS-1$

        res = ExcelReader.getColumnsTitle(100);
        for (String name : res) {
            System.out.println(name);
        }
    }

}
