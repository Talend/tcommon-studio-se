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
package org.talend.core.utils;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * DOC hcyi class global comment. Detailled comment
 */
public class KeywordsValidatorTest {

    /**
     * DOC hfchang Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * DOC hfchang Comment method "tearDown".
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link org.talend.core.utils.KeywordsValidator#isKeyword(java.lang.String)}.
     */
    @Test
    public void testIsKeywordString() {
        assertEquals(true, KeywordsValidator.isKeyword("org"));
        assertEquals(true, KeywordsValidator.isKeyword("com"));
        assertEquals(true, KeywordsValidator.isKeyword("net"));
        assertEquals(true, KeywordsValidator.isKeyword("fr"));
        assertEquals(true, KeywordsValidator.isKeyword("sf"));
        assertEquals(true, KeywordsValidator.isKeyword("routines"));
        assertEquals(true, KeywordsValidator.isKeyword("javax"));
        assertEquals(true, KeywordsValidator.isKeyword("java"));
        assertEquals(true, KeywordsValidator.isKeyword("etc"));
        assertEquals(false, KeywordsValidator.isKeyword("abcd"));

    }

    /**
     * Test method for {@link org.talend.core.utils.KeywordsValidator#isSqlKeyword(java.lang.String)}.
     */
    @Test
    public void testIsSqlKeywordString() {
        assertEquals(true, KeywordsValidator.isSqlKeyword("update"));
        assertEquals(true, KeywordsValidator.isSqlKeyword("delete"));
        assertEquals(true, KeywordsValidator.isSqlKeyword("create"));
        assertEquals(false, KeywordsValidator.isSqlKeyword("abcd"));
    }

    /**
     * Test method for {@link org.talend.core.utils.KeywordsValidator#isSqlKeyword(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testIsSqlKeywordStringString() {
        assertEquals(true, KeywordsValidator.isSqlKeyword(null, null));
        assertEquals(true, KeywordsValidator.isSqlKeyword("create", ""));
        assertEquals(true, KeywordsValidator.isSqlKeyword("create", null));
        assertEquals(true, KeywordsValidator.isSqlKeyword("update", "MYSQL"));
        assertEquals(true, KeywordsValidator.isSqlKeyword("drop", "ORACLE"));
        assertEquals(true, KeywordsValidator.isSqlKeyword("update", "HBASE"));
        assertEquals(true, KeywordsValidator.isSqlKeyword("delete", "SQL_SERVER"));
    }

}
