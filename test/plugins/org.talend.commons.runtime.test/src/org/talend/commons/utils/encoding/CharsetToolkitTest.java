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
package org.talend.commons.utils.encoding;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.junit.Assert;
import org.junit.Test;

/**
 * created by scorreia on Jul 3, 2012 Detailled comment
 * 
 */
public class CharsetToolkitTest {

    /**
     * Test method for
     * {@link org.talend.commons.utils.resource.ResourceUtil#getCharset(java.io.File)}.
     * 
     */
    @Test
    public void testGetCharset() {
        String line = null;
        try {
            String charset = null;
            line = "§ANAL§ASS§ASS";
            charset = CharsetToolkit.getCharset(line.getBytes("ISO-8859-1"));
            Assert.assertTrue(charset!=null);
            Assert.assertTrue(charset.equalsIgnoreCase("ISO-8859-1"));
            
            line = "中華傳統漢字";
            charset = CharsetToolkit.getCharset(line.getBytes("BIG5"));
            Assert.assertTrue(charset!=null);
            Assert.assertTrue(charset.equalsIgnoreCase("BIG5"));
            
            line = "訳が可能な無料のサービスです";
            charset = CharsetToolkit.getCharset(line.getBytes("Shift_JIS"));
            Assert.assertTrue(charset!=null);
            Assert.assertTrue(charset.equalsIgnoreCase("Shift_JIS"));
            
            line = "Je t'adore";
            charset = CharsetToolkit.getCharset(line.getBytes("Unicode")); 
            Assert.assertTrue(charset!=null);
            Assert.assertTrue(charset.equalsIgnoreCase("UTF-16BE"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

}
