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
package org.talend.core.model.metadata.builder.database.manager.dbs;

import static org.mockito.Mockito.*;

import java.sql.DatabaseMetaData;
import java.text.MessageFormat;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.database.TableNode;
import org.talend.core.model.metadata.builder.database.manager.ExtractManager;
import org.talend.core.model.metadata.builder.database.manager.ExtractManagerFactory;
import org.talend.cwm.relational.TdColumn;

/**
 * created by ggu on Jul 5, 2012 Detailled comment
 * 
 * 
 * only test the normal null, so set mysql db as default, (no need care)
 */
public class ExtractManager4NormalTest {

    @Test
    public void testGetDbType() {
        ExtractManager extractManger = null;
        for (EDatabaseTypeName type : EDatabaseTypeName.values()) {
            extractManger = ExtractManagerFactory.create(type);
            Assert.assertNotNull(extractManger);
            Assert.assertEquals(MessageFormat.format("Should be {0}, not {1}", //$NON-NLS-1$
                    type, extractManger.getDbType()), extractManger.getDbType(), type);
        }

    }

    @Test
    public void testGetDbType4Mock() {
        ExtractManager extractManger = mock(ExtractManager.class);
        EDatabaseTypeName[] values = EDatabaseTypeName.values();
        for (int i = 0; i < values.length; i++) {
            EDatabaseTypeName type = values[i];
            when(extractManger.getDbType()).thenReturn(type);
            Assert.assertEquals(extractManger.getDbType(), type);
            verify(extractManger, times(i + 1)).getDbType();
        }
    }

    /**
     * 
     * DOC ggu Comment method "testExtractTablesFromDB4Null".
     * 
     * @throws Exception
     */
    @Test
    public void testExtractTablesFromDB4Null() throws Exception {
        // only test null, don't care the db type
        ExtractManager extractManager = ExtractManagerFactory.create(EDatabaseTypeName.MYSQL);
        Assert.assertNotNull(extractManager);

        List<IMetadataTable> tables1 = extractManager.extractTablesFromDB(null, null);
        Assert.assertNotNull(tables1);
        Assert.assertTrue(tables1.isEmpty());

        List<IMetadataTable> tables2 = extractManager.extractTablesFromDB(null, null, null);
        Assert.assertNotNull(tables2);
        Assert.assertTrue(tables2.isEmpty());

        DatabaseMetaData dbMetadata = mock(DatabaseMetaData.class);

        List<IMetadataTable> tables3 = extractManager.extractTablesFromDB(dbMetadata, null, null);
        Assert.assertNotNull(tables3);
        Assert.assertTrue(tables3.isEmpty());

        IMetadataConnection metadataConn = mock(IMetadataConnection.class);

        List<IMetadataTable> tables4 = extractManager.extractTablesFromDB(null, metadataConn, null);
        Assert.assertNotNull(tables4);
        Assert.assertTrue(tables4.isEmpty());
    }

    /**
     * 
     * DOC ggu Comment method "testReturnColumns4Null".
     */
    @Test
    public void testReturnColumns4Null() {
        // only test null, don't care the db type, just normal
        ExtractManager extractManager = ExtractManagerFactory.create(EDatabaseTypeName.MYSQL);
        Assert.assertNotNull(extractManager);

        List<TdColumn> columns1 = extractManager.returnColumns(null, null);
        Assert.assertNotNull(columns1);
        Assert.assertTrue(columns1.isEmpty());

        // null table node
        IMetadataConnection metadataConnection = mock(IMetadataConnection.class);
        List<TdColumn> columns2 = extractManager.returnColumns(metadataConnection, null);
        Assert.assertNotNull(columns2);
        Assert.assertTrue(columns2.isEmpty());

        // null table type
        TableNode tableNode = mock(TableNode.class);
        List<TdColumn> columns3 = extractManager.returnColumns(metadataConnection, tableNode);
        Assert.assertNotNull(columns3);
        Assert.assertTrue(columns3.isEmpty());
        verify(tableNode).getType();

        // table type
        when(tableNode.getType()).thenReturn(TableNode.SCHEMA);
        List<TdColumn> columns4 = extractManager.returnColumns(metadataConnection, tableNode);
        Assert.assertNotNull(columns4);
        Assert.assertTrue(columns4.isEmpty());
        verify(tableNode, times(2)).getType();

        // null table and null view
        when(tableNode.getType()).thenReturn(TableNode.TABLE);
        List<TdColumn> columns5 = extractManager.returnColumns(metadataConnection, tableNode);
        Assert.assertNotNull(columns5);
        Assert.assertTrue(columns5.isEmpty());
        // because have one time front
        verify(tableNode, times(3)).getType();
        verify(tableNode).getTable();
        verify(tableNode).getView();
    }
}
