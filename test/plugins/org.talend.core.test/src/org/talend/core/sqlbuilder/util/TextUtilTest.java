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
package org.talend.core.sqlbuilder.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.talend.core.database.EDatabaseTypeName;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class TextUtilTest {

    /**
     * Test method for {@link org.talend.core.sqlbuilder.util.TextUtil#TextUtil()}.
     */
    @Test
    public void testTextUtil() {
    }

    /**
     * Test method for {@link org.talend.core.sqlbuilder.util.TextUtil#getNewQueryLabel()}.
     */
    @Test
    public void testGetNewQueryLabel() {
    }

    /**
     * Test method for {@link org.talend.core.sqlbuilder.util.TextUtil#removeLineBreaks(java.lang.String)}.
     */
    @Test
    public void testRemoveLineBreaks() {
        String testString = "select \n from\t test where \r id='4' ";

        String expertString = "select   from  test where  id='4' ";
        assertNull(TextUtil.removeLineBreaks(null));

        assertEquals(TextUtil.removeLineBreaks(testString), expertString);
    }

    /**
     * Test method for {@link org.talend.core.sqlbuilder.util.TextUtil#getWrappedText(java.lang.String)}.
     */
    @Test
    public void testGetWrappedTextString() {
        StringBuilder testString = new StringBuilder();

        String testString1 = null;

        StringBuilder testString2 = new StringBuilder();

        for (int i = 0; i < TextUtil.DEFAULT_WRAPLENGTH; i++) {
            testString.append("a");
        }

        for (int i = 0; i < 200; i++) {
            testString2.append("a");
        }

        assertEquals(TextUtil.DEFAULT_WRAPLENGTH, TextUtil.getWrappedText(testString.toString()).length());

        assertEquals(testString.substring(0, testString.length()),
                TextUtil.getWrappedText(testString.toString()).substring(0, testString.length()));

        assertEquals("", TextUtil.getWrappedText(testString1));

        assertEquals(TextUtil.DEFAULT_WRAPLENGTH, TextUtil.getWrappedText(testString2.toString()).length() - 2);

        assertEquals(testString2.substring(0, TextUtil.DEFAULT_WRAPLENGTH + 1),
                TextUtil.getWrappedText(testString2.toString()).substring(0, TextUtil.DEFAULT_WRAPLENGTH + 1));
    }

    /**
     * Test method for {@link org.talend.core.sqlbuilder.util.TextUtil#getWrappedText(java.lang.String, int)}.
     */
    @Test
    public void testGetWrappedTextStringInt() {
    }

    /**
     * Test method for
     * {@link org.talend.core.sqlbuilder.util.TextUtil#replaceChar(java.lang.String, char, java.lang.String)}.
     */
    @Test
    public void testReplaceChar() {
        String testString1 = "abcdedfwersdf";

        String testString2 = "";

        String testString3 = null;

        TextUtil TextUtil = mock(TextUtil.class);
        assertEquals("", TextUtil.replaceChar(testString2, 'a', ""));

        assertNull(TextUtil.replaceChar(testString3, 'a', ""));

        assertEquals("abwhatdedfwersdf", TextUtil.replaceChar(testString1, 'c', "what"));
    }

    /**
     * Test method for
     * {@link org.talend.core.sqlbuilder.util.TextUtil#addSqlQuots(java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testAddSqlQuots() {
    }

    /**
     * Test method for {@link org.talend.core.sqlbuilder.util.TextUtil#removeQuots(java.lang.String)}.
     */
    @Test
    public void testRemoveQuots() {
        String expectString = "select * from test";

        assertEquals("", TextUtil.removeQuots(null));

        assertNotNull(TextUtil.removeQuots("select * from test"));

        assertEquals(expectString, TextUtil.removeQuots("select * from test"));

        assertNotNull(TextUtil.removeQuots("\"select * from test\""));

        assertEquals(expectString, TextUtil.removeQuots("\"select * from test\""));
    }

    /**
     * Test method for {@link org.talend.core.sqlbuilder.util.TextUtil#getDialogTitle()}.
     */
    @Test
    public void testGetDialogTitle() {
    }

    /**
     * Test method for {@link org.talend.core.sqlbuilder.util.TextUtil#setDialogTitle(java.lang.String)}.
     */
    @Test
    public void testSetDialogTitleString() {
    }

    /**
     * Test method for
     * {@link org.talend.core.sqlbuilder.util.TextUtil#setDialogTitle(java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testSetDialogTitleStringStringString() {
    }

    /**
     * Test method for {@link org.talend.core.sqlbuilder.util.TextUtil#isDoubleQuotesNeededDbType(java.lang.String)}.
     */
    @Test
    public void testIsDoubleQuotesNeededDbType() {
        assertTrue(TextUtil.isDoubleQuotesNeededDbType(EDatabaseTypeName.PSQL.getXmlName()));

        assertTrue(TextUtil.isDoubleQuotesNeededDbType(EDatabaseTypeName.GREENPLUM.getXmlName()));

        assertTrue(TextUtil.isDoubleQuotesNeededDbType(EDatabaseTypeName.PARACCEL.getXmlName()));

        assertTrue(TextUtil.isDoubleQuotesNeededDbType(EDatabaseTypeName.H2.getXmlName()));

        assertFalse(TextUtil.isDoubleQuotesNeededDbType(EDatabaseTypeName.MYSQL.getXmlName()));

        assertFalse(TextUtil.isDoubleQuotesNeededDbType("test"));
    }

    /**
     * Test method for {@link org.talend.core.sqlbuilder.util.TextUtil#isOracleDbType(java.lang.String)}.
     */
    @Test
    public void testIsOracleDbType() {
        assertTrue(TextUtil.isOracleDbType(EDatabaseTypeName.ORACLEFORSID.getXmlName()));

        assertTrue(TextUtil.isOracleDbType(EDatabaseTypeName.ORACLEFORSID.getDisplayName()));

        assertTrue(TextUtil.isOracleDbType(EDatabaseTypeName.ORACLESN.getXmlName()));

        assertTrue(TextUtil.isOracleDbType(EDatabaseTypeName.ORACLESN.getDisplayName()));

        assertTrue(TextUtil.isOracleDbType(EDatabaseTypeName.ORACLE_OCI.getXmlName()));
        assertTrue(TextUtil.isOracleDbType(EDatabaseTypeName.ORACLE_OCI.getDisplayName()));

        assertFalse(TextUtil.isOracleDbType(EDatabaseTypeName.MYSQL.getDisplayName()));

        assertFalse(TextUtil.isOracleDbType("test"));
    }

    /**
     * Test method for {@link org.talend.core.sqlbuilder.util.TextUtil#calEscapeValue(java.lang.String)}.
     */
    @Test
    public void testCalEscapeValue() {
        String testString = null;

        String testString1 = "select * \\n \\t \\r from test";

        String testString2 = "select * from test";

        assertNull(TextUtil.calEscapeValue(testString));

        assertNotNull(TextUtil.calEscapeValue(testString1));

        assertEquals("select * \n \t \r from test", TextUtil.calEscapeValue(testString1));

        assertEquals(testString2, TextUtil.calEscapeValue(testString2));
    }

}
