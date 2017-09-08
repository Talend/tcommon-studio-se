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
package org.talend.core.model.metadata;

import org.junit.Test;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.utils.TalendQuoteUtils;

import static org.junit.Assert.*;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class QueryUtilTest {

    /**
     * Test method for {@link org.talend.core.model.metadata.QueryUtil#needFormatSQL(java.lang.String)}.
     */
    @Test
    public void testNeedFormatSQL() {
        assertFalse(QueryUtil.needFormatSQL(null));

        assertFalse(QueryUtil.needFormatSQL(EDatabaseTypeName.NETEZZA.getDisplayName()));

        assertFalse(QueryUtil.needFormatSQL(EDatabaseTypeName.ORACLE_OCI.getDisplayName()));

        assertFalse(QueryUtil.needFormatSQL(EDatabaseTypeName.ORACLEFORSID.getDisplayName()));

        assertFalse(QueryUtil.needFormatSQL(EDatabaseTypeName.ORACLESN.getDisplayName()));

        assertFalse(QueryUtil.needFormatSQL(EDatabaseTypeName.PSQL.getDisplayName()));

        assertFalse(QueryUtil.needFormatSQL(EDatabaseTypeName.PLUSPSQL.getDisplayName()));

        assertFalse(QueryUtil.needFormatSQL(EDatabaseTypeName.AS400.getDisplayName()));

        assertFalse(QueryUtil.needFormatSQL(EDatabaseTypeName.ACCESS.getDisplayName()));

        assertFalse(QueryUtil.needFormatSQL(EDatabaseTypeName.MYSQL.getDisplayName()));

        assertFalse(QueryUtil.needFormatSQL(EDatabaseTypeName.IBMDB2.getDisplayName()));

        assertFalse(QueryUtil.needFormatSQL(EDatabaseTypeName.IBMDB2ZOS.getDisplayName()));

        assertTrue(QueryUtil.needFormatSQL(EDatabaseTypeName.H2.getDisplayName()));
    }

    /**
     * Test method for
     * {@link org.talend.core.model.metadata.QueryUtil#generateNewQuery(org.talend.core.model.process.IElement, org.talend.core.model.metadata.IMetadataTable, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testGenerateNewQueryIElementIMetadataTableStringStringString() {
    }

    /**
     * Test method for
     * {@link org.talend.core.model.metadata.QueryUtil#generateNewQuery(org.talend.core.model.process.IElement, org.talend.core.model.metadata.IMetadataTable, boolean, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testGenerateNewQueryIElementIMetadataTableBooleanStringStringString() {
    }

    /**
     * Test method for
     * {@link org.talend.core.model.metadata.QueryUtil#generateNewQuery(org.talend.core.model.process.IElement, org.talend.core.model.metadata.IMetadataTable, java.lang.String, java.lang.String, java.lang.String, boolean)}
     * .
     */
    @Test
    public void testGenerateNewQueryIElementIMetadataTableStringStringStringBoolean() {
    }

    /**
     * Test method for
     * {@link org.talend.core.model.metadata.QueryUtil#generateNewQueryDelegate(org.talend.core.model.process.IElement, org.talend.core.model.metadata.IMetadataTable, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testGenerateNewQueryDelegateIElementIMetadataTableStringStringString() {
    }

    /**
     * Test method for
     * {@link org.talend.core.model.metadata.QueryUtil#generateNewQueryDelegate(org.talend.core.model.process.IElement, org.talend.core.model.metadata.IMetadataTable, java.lang.String, java.lang.String, java.lang.String, boolean)}
     * .
     */
    @Test
    public void testGenerateNewQueryDelegateIElementIMetadataTableStringStringStringBoolean() {
    }

    /**
     * Test method for
     * {@link org.talend.core.model.metadata.QueryUtil#generateNewQuery(org.talend.core.model.metadata.IMetadataTable, java.lang.String, java.lang.String, boolean)}
     * .
     */
    @Test
    public void testGenerateNewQueryIMetadataTableStringStringBoolean() {
    }

    /**
     * Test method for
     * {@link org.talend.core.model.metadata.QueryUtil#generateNewQuery(org.talend.core.model.metadata.IMetadataTable, java.lang.String, java.lang.String, java.lang.String[])}
     * .
     */
    @Test
    public void testGenerateNewQueryIMetadataTableStringStringStringArray() {
    }

    /**
     * Test method for
     * {@link org.talend.core.model.metadata.QueryUtil#getTableName(org.talend.core.model.process.IElement, org.talend.core.model.metadata.IMetadataTable, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testGetTableName() {
    }

    /**
     * Test method for {@link org.talend.core.model.metadata.QueryUtil#checkAndAddQuotes(java.lang.String)}.
     */
    @Test
    public void testCheckAndAddQuotes() {
        String testQuery = "select mytable.ID from mytable";

        String expectQuery = "\"select mytable.ID from mytable\"";

        assertNotNull(QueryUtil.checkAndAddQuotes(testQuery));

        assertEquals(QueryUtil.checkAndAddQuotes(testQuery), expectQuery);

        assertNotNull(QueryUtil.checkAndAddQuotes(expectQuery));

        assertEquals(QueryUtil.checkAndAddQuotes(expectQuery), expectQuery);

    }

    /**
     * Test method for {@link org.talend.core.model.metadata.QueryUtil#checkAndRemoveQuotes(java.lang.String)}.
     */
    @Test
    public void testCheckAndRemoveQuotes() {
        String testQuery = "select mytable.ID from mytable";

        String expectQuery = "\"select mytable.ID from mytable\"";

        assertNotNull(TalendQuoteUtils.checkAndRemoveQuotes(testQuery));

        assertEquals(TalendQuoteUtils.checkAndRemoveQuotes(testQuery), testQuery);

        assertNotNull(TalendQuoteUtils.checkAndRemoveQuotes(expectQuery));

        assertEquals(TalendQuoteUtils.checkAndRemoveQuotes(expectQuery), testQuery);
    }

    /**
     * Test method for {@link org.talend.core.model.metadata.QueryUtil#checkIfIsNoQuotesAtAll(java.lang.String)}.
     */
    @Test
    public void testCheckIfIsNoQuotesAtAll() {
        String testQuery = "select mytable.ID from mytable";

        String testQuery1 = "select mytable.\\\"ID\\\" form mytable";

        assertTrue(QueryUtil.checkIfIsNoQuotesAtAll(testQuery));

        assertTrue(!QueryUtil.checkIfIsNoQuotesAtAll(testQuery1));
    }

    /**
     * Test method for {@link org.talend.core.model.metadata.QueryUtil#checkIfHasSpecialEscapeValue(java.lang.String)}.
     */
    @Test
    public void testCheckIfHasSpecialEscapeValue() {
        assertTrue(QueryUtil.checkIfHasSpecialEscapeValue("select \\n"));

        assertTrue(QueryUtil.checkIfHasSpecialEscapeValue("select \\r"));

        assertTrue(QueryUtil.checkIfHasSpecialEscapeValue("select \\t"));

        assertFalse(QueryUtil.checkIfHasSpecialEscapeValue("test"));
    }

}
