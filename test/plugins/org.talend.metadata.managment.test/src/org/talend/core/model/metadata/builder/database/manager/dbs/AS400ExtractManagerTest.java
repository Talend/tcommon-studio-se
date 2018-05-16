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
package org.talend.core.model.metadata.builder.database.manager.dbs;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.talend.core.database.EDatabaseTypeName;

/**
 * created by ggu on Jul 6, 2012 Detailled comment
 * 
 */
public class AS400ExtractManagerTest extends AbstractTest4ExtractManager {

    @Before
    public void setUp() throws Exception {
        init(EDatabaseTypeName.AS400);
    }

    /**
     * 
     * DOC ggu Comment method "testGetSchema".
     * 
     * @see AS400ExtractManager.getSchema(IMetadataConnection)
     */
    @Override
    @Test
    @Ignore
    public void testGetSchema() {
        PTODO();
    }

    @Override
    protected void verify4ExtractTablesFromDB(DatabaseMetaData mockedDBMetadata, ResultSet mockedGetTablesResultSet)
            throws SQLException {
        verify(mockedDBMetadata).getTables(anyString(), anyString(), anyString(), any(String[].class));
        verify(mockedDBMetadata).getTables((String) isNull(), anyString(), (String) isNull(), any(String[].class));
        // special
        verify(mockedDBMetadata, times(2)).supportsSchemasInTableDefinitions();
        // because limit is 1, so will call twince
        verify(mockedGetTablesResultSet, times(2)).next();
        // because table name, remarks, schema, table type
        verify(mockedGetTablesResultSet, times(4)).getString(anyString());
    }

    /**
     * 
     * DOC ggu Comment method "testExtractTablesFromDB4MultiSchemas".
     * 
     * because there are many libs to be supported by AS400
     * 
     * @throws Exception
     */
    // @Test
    public void testExtractTablesFromDB4MultiSchemas() throws Exception {
        PTODO();
    }

    /**
     * 
     * DOC ggu Comment method "testExtractTablesFromDB4SupportsSchemasInTableDefinitions".
     * 
     * because when supportsSchemasInTableDefinitions for AS400
     * 
     * @throws Exception
     */
    // @Test
    public void testExtractTablesFromDB4SupportsSchemasInTableDefinitions() throws Exception {
        PTODO();
    }
}
