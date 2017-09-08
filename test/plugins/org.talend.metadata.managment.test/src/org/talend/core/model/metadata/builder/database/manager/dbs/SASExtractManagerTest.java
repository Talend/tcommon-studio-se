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

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.talend.commons.utils.database.SASDataBaseMetadata;
import org.talend.commons.utils.database.SASResultSet;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.database.TableNode;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdTable;

/**
 * created by ggu on Jul 11, 2012 Detailled comment
 * 
 */
public class SASExtractManagerTest extends AbstractTest4ExtractManager {

    @Before
    public void setUp() throws Exception {
        init(EDatabaseTypeName.SAS);
    }

    @Override
    @Test
    public void testReturnColumns4DontCreateConnection() throws Exception {
        Assert.assertNotNull(getExtractManger());

        Connection conn = mockConnection4ReturnColumns();
        getExtractMeta().setConn(conn);

        SASDataBaseMetadata dbMetadata = mockDatabaseMetaData4ReturnColumns();
        when(conn.getMetaData()).thenReturn(dbMetadata);

        // getColumns
        SASResultSet getColumnsResultSet = mockGetColumnsResultSet4ReturnColumns();
        doReturn(getColumnsResultSet).when(dbMetadata).getColumns(anyString(), anyString(), anyString(), anyString());

        IMetadataConnection metadataConn = mockMetadataConnection4ReturnColumns();
        TableNode tableNode = mockTableNode4ReturnColumns();
        TdTable tdTable = mockTable4ReturnColumns();
        when(tableNode.getTable()).thenReturn(tdTable);
        // PTODO

        List<TdColumn> columns = getExtractManger().returnColumns(metadataConn, tableNode, true);

        Assert.assertNotNull(columns);
        Assert.assertTrue(columns.isEmpty());
        // verify
        verifyTableNode4ReturnColumns4DontCreateConnection(tableNode);
        // verifyTdTable4ReturnColumns4DontCreateConnection(tdTable);
        verifyMeatadataConnection4ReturnColumns4DontCreateConnection(metadataConn);
        verifyConnection4ReturnColumns4DontCreateConnection(conn);
        // verifyDbMetadata4ReturnColumns4DontCreateConnection(dbMetadata);
        // verifyColumnsResultSet4ReturnColumns4DontCreateConnection(getColumnsResultSet);

        getExtractMeta().setConn(null);
    }

    @Override
    protected SASDataBaseMetadata mockDatabaseMetaData4ReturnColumns() throws Exception {
        // need care the FakeDatabaseMetaData for SAS
        SASDataBaseMetadata sasMetaData = mock(SASDataBaseMetadata.class);
        return sasMetaData;
    }

    @Override
    protected SASResultSet mockGetColumnsResultSet4ReturnColumns() throws Exception {
        SASResultSet resultSet = mock(SASResultSet.class);
        // maybe need test some SQLException
        doNothing().when(resultSet).close();
        // FIXME, later, should find a way to do it,when true
        when(resultSet.next()).thenReturn(false);
        return resultSet;
    }

    @Override
    protected void verifyTdTable4ReturnColumns4DontCreateConnection(TdTable tdTable) {
        //
        verify(tdTable, times(1)).getFeature();
        verify(tdTable, times(1)).eResource();

        Assert.assertTrue(tdTable.getFeature().isEmpty());
        Assert.assertTrue(tdTable.getOwnedElement().isEmpty());
    }

    protected void verifyDbMetadata4ReturnColumns4DontCreateConnection(SASDataBaseMetadata dbMetadata) throws SQLException {
        verify(dbMetadata).getColumns(anyString(), anyString(), anyString(), anyString());
    }

    protected void verifyColumnsResultSet4ReturnColumns4DontCreateConnection(SASResultSet getColumnsResultSet)
            throws SQLException {
        verify(getColumnsResultSet).next();
        verify(getColumnsResultSet).close();
    }

}
