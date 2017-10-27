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
package org.talend.commons.utils.database;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.talend.utils.sql.metadata.constants.TableType;

/**
 * created by zshen on Apr 7, 2013 Detailled comment
 * 
 */
public class DB2ForZosDataBaseMetadataTest {

    /**
     * DOC zshen Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * Test method for
     * {@link org.talend.commons.utils.database.DB2ForZosDataBaseMetadata#getTables(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])}
     * 
     * case1: schema is not null, only contain Table
     */
    @Test
    public void testGetTablescase1() {
        String catalog = "tbi"; //$NON-NLS-1$
        String schema = "dbo"; //$NON-NLS-1$
        String tableNamePattern = null;
        String[] types = new String[] { TableType.TABLE.name() };
        ResultSet resultSet = mockResultSet();

        // java.sql.Statment mock
        PreparedStatement sqlStatement = mockPreparedStatement(resultSet);
        // ~java.sql.Statment
        // java.sql.Connection mock
        Connection sqlConnection = mockConnection(sqlStatement);
        // ~java.sql.Connection

        ResultSet tablesResult = mockMetadata(catalog, schema, tableNamePattern, types, sqlConnection);
        Assert.assertTrue(tablesResult != null);
        Assert.assertTrue(tablesResult instanceof DB2ForZosResultSet);
        if (tablesResult instanceof DB2ForZosResultSet) {
            try {
                Assert.assertTrue(((DB2ForZosResultSet) tablesResult).next() == true);
                Assert.assertTrue(((DB2ForZosResultSet) tablesResult).next() == false);
            } catch (SQLException e) {
                fail(e.getMessage());
            }

        }
    }

    /**
     * Test method for
     * {@link org.talend.commons.utils.database.DB2ForZosDataBaseMetadata#getTables(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])}
     * 
     * case2:schema is not null, not only contain Table
     */
    @Test
    public void testGetTablesCase2() {
        String catalog = "tbi"; //$NON-NLS-1$
        String schema = "dbo"; //$NON-NLS-1$
        String tableNamePattern = null;
        String[] types = new String[] { TableType.TABLE.name(), TableType.VIEW.name(), TableType.SYNONYM.name(),
                TableType.ALIAS.name() };
        ResultSet resultSet = mockResultSet();

        // java.sql.Statment mock
        PreparedStatement sqlStatement = mockPreparedStatement(resultSet);
        // ~java.sql.Statment
        // java.sql.Connection mock
        Connection sqlConnection = mockConnection(sqlStatement);
        // ~java.sql.Connection

        ResultSet tablesResult = mockMetadata(catalog, schema, tableNamePattern, types, sqlConnection);
        Assert.assertTrue(tablesResult != null);
        Assert.assertTrue(tablesResult instanceof DB2ForZosResultSet);
        if (tablesResult instanceof DB2ForZosResultSet) {
            try {
                Assert.assertTrue(((DB2ForZosResultSet) tablesResult).next() == true);
                Assert.assertTrue(((DB2ForZosResultSet) tablesResult).next() == false);
            } catch (SQLException e) {
                fail(e.getMessage());
            }

        }
    }

    /**
     * Test method for
     * {@link org.talend.commons.utils.database.DB2ForZosDataBaseMetadata#getTables(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])}
     * 
     * case3 :schema is null, not only contain Table
     */
    @Test
    public void testGetTablesCase3() {
        String catalog = "tbi"; //$NON-NLS-1$
        String schema = null;
        String tableNamePattern = null;
        String[] types = new String[] { TableType.TABLE.name(), TableType.VIEW.name(), TableType.SYNONYM.name(),
                TableType.ALIAS.name() };
        ResultSet resultSet = mockResultSet();

        // java.sql.Statment mock
        PreparedStatement sqlStatement = mockPreparedStatement(resultSet);
        // ~java.sql.Statment
        // java.sql.Connection mock
        Connection sqlConnection = mockConnection(sqlStatement);
        // ~java.sql.Connection
        ResultSet tablesResult = mockMetadata(catalog, schema, tableNamePattern, types, sqlConnection);
        Assert.assertTrue(tablesResult != null);
        Assert.assertTrue(tablesResult instanceof DB2ForZosResultSet);
        if (tablesResult instanceof DB2ForZosResultSet) {
            try {
                Assert.assertTrue(((DB2ForZosResultSet) tablesResult).next() == true);
                Assert.assertTrue(((DB2ForZosResultSet) tablesResult).next() == false);
            } catch (SQLException e) {
                fail(e.getMessage());
            }

        }
    }

    /**
     * Test method for
     * {@link org.talend.commons.utils.database.DB2ForZosDataBaseMetadata#getTables(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])}
     * 
     * case3 :schema is null, only contain Table
     */
    @Test
    public void testGetTablesCase4() {
        String catalog = "tbi"; //$NON-NLS-1$
        String schema = null;
        String tableNamePattern = null;
        String[] types = new String[] { TableType.TABLE.name() };
        // java.sql.ResultSet mock
        ResultSet resultSet = mockResultSet();
        // ~java.sql.ResultSet

        // java.sql.Statment mock
        PreparedStatement sqlStatement = mockPreparedStatement(resultSet);
        // ~java.sql.Statment
        // java.sql.Connection mock
        Connection sqlConnection = mockConnection(sqlStatement);
        // ~java.sql.Connection
        ResultSet tablesResult = mockMetadata(catalog, schema, tableNamePattern, types, sqlConnection);
        Assert.assertTrue(tablesResult != null);
        Assert.assertTrue(tablesResult instanceof DB2ForZosResultSet);
        if (tablesResult instanceof DB2ForZosResultSet) {
            try {
                Assert.assertTrue(((DB2ForZosResultSet) tablesResult).next() == true);
                Assert.assertTrue(((DB2ForZosResultSet) tablesResult).next() == false);
            } catch (SQLException e) {
                fail(e.getMessage());
            }

        }
    }

    /**
     * DOC yyin Comment method "mockMetadata".
     * 
     * @param catalog
     * @param schema
     * @param tableNamePattern
     * @param types
     * @param sqlConnection
     * @return
     */
    private ResultSet mockMetadata(String catalog, String schema, String tableNamePattern, String[] types,
            Connection sqlConnection) {
        DB2ForZosDataBaseMetadata db2ZosMetadata = null;
        ResultSet tablesResult = null;
        try {
            db2ZosMetadata = new DB2ForZosDataBaseMetadata(sqlConnection);
            tablesResult = db2ZosMetadata.getTables(catalog, schema, tableNamePattern, types);
        } catch (SQLException e) {
            fail(e.getMessage());
        }
        return tablesResult;
    }

    /**
     * DOC yyin Comment method "mockResultSet".
     * 
     * @return
     */
    private ResultSet mockResultSet() {
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        try {
            Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
            Mockito.when(resultSet.getString("NAME")).thenReturn("tableName"); //$NON-NLS-1$ //$NON-NLS-2$
            Mockito.when(resultSet.getString("CREATOR")).thenReturn("schemaName"); //$NON-NLS-1$ //$NON-NLS-2$
            Mockito.when(resultSet.getString("TYPE")).thenReturn("string"); //$NON-NLS-1$ //$NON-NLS-2$
        } catch (SQLException e3) {
            fail(e3.getMessage());
        }
        return resultSet;
    }

    /**
     * DOC yyin Comment method "mockPreparedStatement".
     * 
     * @param resultSet
     * @return
     */
    private PreparedStatement mockPreparedStatement(ResultSet resultSet) {
        PreparedStatement sqlStatement = Mockito.mock(PreparedStatement.class);
        try {
            Mockito.when(sqlStatement.executeQuery()).thenReturn(resultSet);
        } catch (SQLException e2) {
            fail(e2.getMessage());
        }
        return sqlStatement;
    }

    /**
     * DOC yyin Comment method "mockConnection".
     * 
     * @param exceptSql
     * @param sqlStatement
     * @return
     */
    private Connection mockConnection(PreparedStatement sqlStatement) {
        Connection sqlConnection = Mockito.mock(Connection.class);
        try {
            Mockito.when(sqlConnection.prepareStatement(Mockito.anyString())).thenReturn(sqlStatement);
        } catch (SQLException e1) {
            fail(e1.getMessage());
        }
        return sqlConnection;
    }
}
