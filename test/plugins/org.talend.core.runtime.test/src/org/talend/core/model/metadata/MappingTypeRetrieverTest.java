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

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class MappingTypeRetrieverTest {

    /**
     * DOC Administrator Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * DOC Administrator Comment method "tearDown".
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for
     * {@link org.talend.core.model.metadata.MappingTypeRetriever#isLengthIgnored(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testIsLengthIgnored() {
        String dbmsId = "id_MSSQL";
        String dbType = "int";
        MappingTypeRetriever mappingType = MetadataTalendType.getMappingTypeRetriever(dbmsId);
        assertTrue(mappingType.isLengthIgnored(dbmsId, dbType));

    }

    /**
     * Test method for
     * {@link org.talend.core.model.metadata.MappingTypeRetriever#isPrecisionIgnored(java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testIsPrecisionIgnored() {
        String dbmsId = "id_MSSQL";
        String dbType = "int";
        MappingTypeRetriever mappingType = MetadataTalendType.getMappingTypeRetriever(dbmsId);
        assertTrue(mappingType.isPrecisionIgnored(dbmsId, dbType));
    }

}
