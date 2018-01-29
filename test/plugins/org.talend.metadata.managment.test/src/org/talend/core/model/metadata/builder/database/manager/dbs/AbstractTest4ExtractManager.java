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

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.talend.core.database.EDatabase4DriverClassName;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.database.DriverShim;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase.ETableTypes;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.metadata.builder.database.TableInfoParameters;
import org.talend.core.model.metadata.builder.database.TableNode;
import org.talend.core.model.metadata.builder.database.manager.ExtractManager;
import org.talend.core.model.metadata.builder.database.manager.ExtractManagerFactory;
import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdTable;
import orgomg.cwm.objectmodel.core.Feature;

/**
 * created by ggu on Jul 4, 2012 Detailled comment
 * 
 */
@PrepareForTest({ ExtractMetaDataUtils.class })
@Ignore
public class AbstractTest4ExtractManager {

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    private ExtractManager extractManger;

    private ExtractMetaDataUtils extractMeta;

    private EDatabaseTypeName type;

    protected void init(EDatabaseTypeName dbType) {
        this.type = dbType;
        Assert.assertNotNull(this.type);
        this.extractManger = spy(ExtractManagerFactory.create(this.type));
        this.extractMeta = ExtractMetaDataUtils.getInstance();
        Assert.assertNotNull(this.extractManger);
    }

    @After
    public void tearDown() throws Exception {
        extractManger = null;
        extractMeta = null;
    }

    protected ExtractManager getExtractManger() {
        return this.extractManger;
    }

    protected ExtractMetaDataUtils getExtractMeta() {
        return this.extractMeta;
    }

    protected void PTODO() {
        // When finished all tests, will remove this PTODO
        Assert.fail("PTODO: Please finish this test.");
    }

    /**
     * 
     * DOC ggu Comment method "testGetDbType".
     */
    @Test
    public void testGetDbType() {
        Assert.assertNotNull(getExtractManger());
        Assert.assertEquals(getExtractManger().getDbType(), this.type);
    }

    /**
     * 
     * DOC ggu Comment method "testCloseConnect4Null".
     * 
     * @throws Exception
     */
    @Test
    public void testCloseConnect4Null() throws Exception {
        Assert.assertNotNull(getExtractManger());

        // all are null
        Assert.assertFalse(getExtractManger().closeConnection(null, null));
        // null metadataconncion
        DriverShim driverShim = mock(DriverShim.class);
        Assert.assertFalse(getExtractManger().closeConnection(null, driverShim));

        // null driver
        IMetadataConnection metadataConn = mock(IMetadataConnection.class);
        Assert.assertFalse(getExtractManger().closeConnection(metadataConn, null));

        // null driver class
        Assert.assertFalse(getExtractManger().closeConnection(metadataConn, driverShim));
    }

    /**
     * 
     * DOC ggu Comment method "testCloseConnect".
     * 
     * @throws Exception
     */
    @Test
    public void testCloseConnect() throws Exception {
        Assert.assertNotNull(getExtractManger());

        IMetadataConnection metadataConn = mock(IMetadataConnection.class);
        EDatabase4DriverClassName db4DriverClass = EDatabase4DriverClassName.indexOfByDbType(getExtractManger().getDbType()
                .getXmlName());
        Assert.assertNotNull(db4DriverClass);
        when(metadataConn.getDriverClass()).thenReturn(db4DriverClass.getDriverClass());

        Driver driver = mock(Driver.class);
        DriverShim wapperDriver = spy(new DriverShim(driver));
        Connection connection = mock(Connection.class);
        doReturn(connection).when(driver).connect(anyString(), (Properties) Mockito.isNull());

        testCloseConnect_Verify(metadataConn, wapperDriver);
    }

    protected void testCloseConnect_Verify(IMetadataConnection metadataConn, DriverShim wapperDriver) throws Exception {
        Assert.assertFalse(getExtractManger().closeConnection(metadataConn, wapperDriver));
        // by default, no close, so don't verify wapperDriver.connect
    }

    /**
     * 
     * DOC ggu Comment method "testExtractTablesFromDB4NullSchema".
     * 
     * @throws Exception
     */
    // @Test
    public void testExtractTablesFromDB4NullSchema() throws Exception {
        Assert.assertNotNull(getExtractManger());
        IMetadataConnection mockedMetadataConn = mockMetadataConnection4ExtractTablesFromDB();
        mockSchema4ExtractTablesFromDB(mockedMetadataConn, null);
        mockGetTablesToFilter4ExtractTablesFromDB(mockedMetadataConn);

        // PTODO seems can't test for schema
        PTODO();
    }

    /**
     * 
     * DOC ggu Comment method "testExtractTablesFromDB4EmptySchema".
     * 
     * @throws Exception
     */
    // @Test
    public void testExtractTablesFromDB4EmptySchema() throws Exception {
        Assert.assertNotNull(getExtractManger());
        IMetadataConnection mockedMetadataConn = mockMetadataConnection4ExtractTablesFromDB();
        mockSchema4ExtractTablesFromDB(mockedMetadataConn, ""); //$NON-NLS-1$
        mockGetTablesToFilter4ExtractTablesFromDB(mockedMetadataConn);

        // PTODO seems can't test for schema
        PTODO();
    }

    /**
     * 
     * DOC ggu Comment method "testExtractTablesFromDB".
     * 
     * @throws Exception
     */
    @Test
    public void testExtractTablesFromDB() throws Exception {
        Assert.assertNotNull(getExtractManger());
        IMetadataConnection mockedMetadataConn = mockMetadataConnection4ExtractTablesFromDB();
        mockSchema4ExtractTablesFromDB(mockedMetadataConn, "ABC"); //$NON-NLS-1$
        mockGetTablesToFilter4ExtractTablesFromDB(mockedMetadataConn);

        DatabaseMetaData mockedDBMetadata = mock(DatabaseMetaData.class);
        // table type
        ResultSet mockedTableTypeResultSet = mockGetTableTypesResultSet4ExtractTablesFromDB();
        when(mockedDBMetadata.getTableTypes()).thenReturn(mockedTableTypeResultSet);
        // tables
        ResultSet mockedGetTablesResultSet = mockGetTablesResultSet4ExtractTablesFromDB();
        when(mockedDBMetadata.getTables(anyString(), anyString(), anyString(), any(String[].class))).thenReturn(
                mockedGetTablesResultSet);

        // because only test one table, so limit 1 to make sure "break" the "while"
        List<IMetadataTable> tables = getExtractManger().extractTablesFromDB(mockedDBMetadata, mockedMetadataConn, 1);

        verify4ExtractTablesFromDB(mockedDBMetadata, mockedGetTablesResultSet);

        Assert.assertNotNull(tables);
        Assert.assertFalse(tables.isEmpty());
        Assert.assertTrue(tables.size() == 1); // because only one table to test

        IMetadataTable metadataTable = tables.get(0);
        Assert.assertNotNull(metadataTable);
        Assert.assertEquals("1", metadataTable.getId()); //$NON-NLS-1$
        Assert.assertEquals("testTable1", metadataTable.getLabel()); //$NON-NLS-1$
        Assert.assertEquals("testTable1", metadataTable.getTableName()); //$NON-NLS-1$
        Assert.assertEquals("table comment", metadataTable.getComment()); //$NON-NLS-1$

    }

    protected void verify4ExtractTablesFromDB(DatabaseMetaData mockedDBMetadata, ResultSet mockedGetTablesResultSet)
            throws SQLException {
        /*
         * because do mock the "getAvailableTableTypes" in mockTableTypeResultSet(). so no need verify
         */
        // verify(mockedDBMetadata).getTableTypes();
        // because false, so only one time
        // verify(mockedTableTypeResultSet).next();
        // verify(mockedTableTypeResultSet).close();

        verify(mockedDBMetadata).getTables(anyString(), anyString(), anyString(), any(String[].class));
        // same
        verify(mockedDBMetadata).getTables((String) isNull(), anyString(), (String) isNull(), any(String[].class));

        verify(mockedDBMetadata).supportsSchemasInTableDefinitions();
        // because limit is 1, so will call twince
        verify(mockedGetTablesResultSet, times(2)).next();
        // because table name, remarks, schema, table type
        verify(mockedGetTablesResultSet, times(4)).getString(anyString());
    }

    protected IMetadataConnection mockMetadataConnection4ExtractTablesFromDB() {
        IMetadataConnection metadataConn = mock(IMetadataConnection.class);
        return metadataConn;
    }

    protected void mockSchema4ExtractTablesFromDB(IMetadataConnection metadataConnection, String schema) {
        when(metadataConnection.getSchema()).thenReturn(schema);
    }

    protected void mockGetTablesToFilter4ExtractTablesFromDB(IMetadataConnection metadataConnection) {
        //
    }

    protected ResultSet mockGetTablesResultSet4ExtractTablesFromDB() throws SQLException {
        ResultSet getTablesResultSet = mock(ResultSet.class);
        doNothing().when(getTablesResultSet).close();
        // must set a value for limit, when extractTablesFromDB, if on limit, can't break the while
        when(getTablesResultSet.next()).thenReturn(true);

        doReturn("testTable1").when(getTablesResultSet).getString(ExtractManager.TABLE_NAME); //$NON-NLS-1$
        doReturn("table comment").when(getTablesResultSet).getString(ExtractManager.REMARKS); //$NON-NLS-1$
        // seems don't use the schema, so can't test, but must set it
        doReturn("abc").when(getTablesResultSet).getString(ExtractManager.TABLE_SCHEMA); //$NON-NLS-1$
        doReturn(ExtractManager.TABLETYPE_TABLE).when(getTablesResultSet).getString(ExtractManager.TABLE_TYPE);

        return getTablesResultSet;
    }

    protected ResultSet mockGetTableTypesResultSet4ExtractTablesFromDB() throws SQLException {
        // mock for table types
        ResultSet tableTypeResultSet = mock(ResultSet.class);

        when(tableTypeResultSet.next()).thenReturn(false);
        // so far, don't test the exeception here，will be in testExtractTablesFromDB4DBMetadataClose_SQLException
        doNothing().when(tableTypeResultSet).close();

        // for table type, so far only test the TABLETYPE_TABLE, don't test the TABLETYPE_VIEW, TABLETYPE_SYNONYM
        Set<String> availableTableTypes = new HashSet<String>();
        availableTableTypes.add(ETableTypes.TABLETYPE_TABLE.getName());
        doReturn(availableTableTypes).when(getExtractManger()).getAvailableTableTypes(any(DatabaseMetaData.class));
        return tableTypeResultSet;
    }

    /**
     * 
     * DOC ggu Comment method "testExtractTablesFromDB4SQLException".
     * 
     * @throws Exception
     */
    @Test(expected = RuntimeException.class)
    public void testExtractTablesFromDB4GetTableTypes_SQLException() throws Exception {
        Assert.assertNotNull(getExtractManger());

        IMetadataConnection mockedMetadataConn = mockMetadataConnection4ExtractTablesFromDB();
        // no need
        //  mockSchema4ExtractTablesFromDB(mockedMetadataConn,"ABC"); //$NON-NLS-1$

        DatabaseMetaData dbMetadata = mock(DatabaseMetaData.class);
        doThrow(new SQLException()).when(dbMetadata).getTableTypes();

        List<IMetadataTable> tables = getExtractManger().extractTablesFromDB(dbMetadata, mockedMetadataConn);

        // won't run this line
        Assert.assertTrue(tables.isEmpty());
    }

    @Test(expected = RuntimeException.class)
    public void testExtractTablesFromDB4DBMetadataClose_SQLException() throws Exception {
        Assert.assertNotNull(getExtractManger());

        IMetadataConnection mockedMetadataConn = mockMetadataConnection4ExtractTablesFromDB();
        // no need
        //  mockSchema4ExtractTablesFromDB(mockedMetadataConn,"ABC"); //$NON-NLS-1$

        DatabaseMetaData dbMetadata = mock(DatabaseMetaData.class);

        ResultSet tableTypeResultSet = mock(ResultSet.class);
        when(dbMetadata.getTableTypes()).thenReturn(tableTypeResultSet);
        when(tableTypeResultSet.next()).thenReturn(false);
        doThrow(new SQLException()).when(tableTypeResultSet).close();

        List<IMetadataTable> tables = getExtractManger().extractTablesFromDB(dbMetadata, mockedMetadataConn);

        // won't run this line
        Assert.assertTrue(tables.isEmpty());
    }

    /**
     * 
     * DOC ggu Comment method "testGetTableNameBySynonyms".
     * 
     * @see ExtractManager.getTableNameBySynonyms(Connection,String)
     * @throws Exception
     */
    @Test
    public void testGetTableNameBySynonyms() throws Exception {
        Assert.assertNotNull(getExtractManger());

        String tableName1 = getExtractManger().getTableNameBySynonyms(null, "abc"); //$NON-NLS-1$
        Assert.assertEquals("abc", tableName1); //$NON-NLS-1$

        Connection conn = mock(Connection.class);
        String tableName2 = getExtractManger().getTableNameBySynonyms(conn, "xyz"); //$NON-NLS-1$
        Assert.assertEquals("xyz", tableName2); //$NON-NLS-1$
    }

    /**
     * 
     * DOC ggu Comment method "testGetSchema".
     * 
     * @see ExtractManager.getSchema(IMetadataConnection)
     */
    @Test
    public void testGetSchema() {
        Assert.assertNotNull(getExtractManger());

        Assert.assertNull(getExtractManger().getSchema(null));

        // null schema
        IMetadataConnection metadataConn = mock(IMetadataConnection.class);
        Assert.assertNull(getExtractManger().getSchema(metadataConn));

        doReturn("abc").when(metadataConn).getSchema(); //$NON-NLS-1$
        String schema = getExtractManger().getSchema(metadataConn);
        Assert.assertEquals("abc", schema); //$NON-NLS-1$
    }

    /**
     * 
     * DOC ggu Comment method "testReturnMetadataColumnsFormTable".
     * 
     * @see ExtractManager.returnMetadataColumnsFormTable(IMetadataConnection, String, boolean...)
     * @throws Exception
     */
    // @Test
    @Deprecated
    public void testReturnMetadataColumnsFormTable() throws Exception {
        PTODO();
    }

    /**
     * 
     * DOC ggu Comment method "testReturnColumns4DontCreateConnection".
     * 
     * @see ExtractManager.returnColumns(IMetadataConnection, TableNode, boolean...)
     * 
     * don't recreate the connection.
     * @throws Exception
     */
    @Test
    public void testReturnColumns4DontCreateConnection() throws Exception {
        Assert.assertNotNull(getExtractManger());

        Connection conn = mockConnection4ReturnColumns();
        extractMeta.setConn(conn);

        DatabaseMetaData dbMetadata = mockDatabaseMetaData4ReturnColumns();
        when(conn.getMetaData()).thenReturn(dbMetadata);

        // getColumns
        ResultSet getColumnsResultSet = mockGetColumnsResultSet4ReturnColumns();
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
        verifyTdTable4ReturnColumns4DontCreateConnection(tdTable);
        verifyMeatadataConnection4ReturnColumns4DontCreateConnection(metadataConn);
        verifyConnection4ReturnColumns4DontCreateConnection(conn);
        // verifyDbMetadata4ReturnColumns4DontCreateConnection(dbMetadata);
        // verifyColumnsResultSet4ReturnColumns4DontCreateConnection(getColumnsResultSet);

        extractMeta.setConn(null);
    }

    protected IMetadataConnection mockMetadataConnection4ReturnColumns() {
        IMetadataConnection metadataConn = mock(IMetadataConnection.class);

        String dbType = this.getExtractManger().getDbType().getXmlName();
        when(metadataConn.getDbType()).thenReturn(dbType);
        // only for TERADATA
        when(metadataConn.isSqlMode()).thenReturn(false);
        when(metadataConn.getDatabase()).thenReturn("talend"); //$NON-NLS-1$
        // PTODO
        return metadataConn;
    }

    protected TableNode mockTableNode4ReturnColumns() throws Exception {
        TableNode tableNode = spy(new TableNode());
        // must type for table
        when(tableNode.getType()).thenReturn(TableNode.TABLE);

        // because want to test the replace for "/"
        // will do assert getValue, so don't stub it.
        //        when(tableNode.getValue()).thenReturn("/table1"); //$NON-NLS-1$
        tableNode.setValue("/table1"); //$NON-NLS-1$

        // only test table type if SYNONYM, will do it in following test method
        // "testReturnColumns4DontCreateConnection2TableTypeSynonym"
        when(tableNode.getItemType()).thenReturn(ETableTypes.TABLETYPE_TABLE.getName());
        // PTODO
        return tableNode;
    }

    protected TdTable mockTable4ReturnColumns() throws Exception {

        TdTable tdTable = RelationalFactory.eINSTANCE.createTdTable();

        tdTable.setName("TestTable"); //$NON-NLS-1$

        // ResourceSet resourceSet = new ResourceSetImpl();
        //        Resource resource = resourceSet.createResource(URI.createFileURI("tdtable.xml")); //$NON-NLS-1$
        // resource.getContents().add(tdTable);

        return spy(tdTable);
    }

    protected Connection mockConnection4ReturnColumns() throws Exception {
        Connection conn = mock(Connection.class);
        // make sure use the mock connection
        when(conn.isClosed()).thenReturn(false);
        doNothing().when(conn).close();
        return conn;

    }

    protected DatabaseMetaData mockDatabaseMetaData4ReturnColumns() throws Exception {
        DatabaseMetaData dbMetadata = mock(DatabaseMetaData.class);
        // PTODO
        return dbMetadata;
    }

    protected ResultSet mockGetColumnsResultSet4ReturnColumns() throws Exception {
        ResultSet resultSet = mock(ResultSet.class);

        // maybe need test some SQLException
        doNothing().when(resultSet).close();

        // FIXME, later, should find a way to do it,when true
        when(resultSet.next()).thenReturn(false);

        return resultSet;
    }

    protected void verifyTableNode4ReturnColumns4DontCreateConnection(TableNode tableNode) {
        verify(tableNode).getType();
        verify(tableNode).getTable();
        // verify(tableNode).getValue();
        //        Assert.assertEquals(tableNode.getValue(), "/table1"); //$NON-NLS-1$
        verify(tableNode).getItemType();
    }

    protected void verifyTdTable4ReturnColumns4DontCreateConnection(TdTable tdTable) {
        Assert.assertTrue(tdTable.getFeature().isEmpty());
        Assert.assertTrue(tdTable.getOwnedElement().isEmpty());
    }

    protected void verifyMeatadataConnection4ReturnColumns4DontCreateConnection_Synonym(IMetadataConnection metadataConn) {
        verify(metadataConn).getDbType();
        verify(metadataConn).isSqlMode();
        verify(metadataConn).getDatabase();
    }

    protected void verifyMeatadataConnection4ReturnColumns4DontCreateConnection(IMetadataConnection metadataConn) {
        verify(metadataConn, times(2)).getDbType();
        verify(metadataConn).isSqlMode();
        verify(metadataConn).getDatabase();
    }

    protected void verifyConnection4ReturnColumns4DontCreateConnection(Connection conn) throws SQLException {
        verify(conn, times(2)).isClosed();
    }

    protected void verifyDbMetadata4ReturnColumns4DontCreateConnection(DatabaseMetaData dbMetadata) throws SQLException {
        verify(dbMetadata).getColumns(anyString(), anyString(), anyString(), anyString());
    }

    protected void verifyColumnsResultSet4ReturnColumns4DontCreateConnection(ResultSet getColumnsResultSet) throws SQLException {
        verify(getColumnsResultSet).next();
        verify(getColumnsResultSet).close();
    }

    /**
     * 
     * DOC ggu Comment method "testReturnColumns4DontCreateConnection2TableTypeSynonym".
     * 
     * test for the Synonym
     * 
     * @throws Exception
     */
    @Test
    public void testReturnColumns4DontCreateConnection2TableTypeSynonym() throws Exception {
        Assert.assertNotNull(getExtractManger());

        Connection conn = mockConnection4ReturnColumns();
        extractMeta.setConn(conn);

        DatabaseMetaData dbMetadata = mockDatabaseMetaData4ReturnColumns();
        when(conn.getMetaData()).thenReturn(dbMetadata);

        // getColumns
        ResultSet getColumnsResultSet = mockGetColumnsResultSet4ReturnColumns();
        doReturn(getColumnsResultSet).when(dbMetadata).getColumns(anyString(), anyString(), anyString(), anyString());

        IMetadataConnection metadataConn = mockMetadataConnection4ReturnColumns();
        TableNode tableNode = mockTableNode4ReturnColumns2TableTypeSynonym();
        TdTable tdTable = mockTable4ReturnColumns();
        when(tableNode.getTable()).thenReturn(tdTable);
        // PTODO

        List<TdColumn> columns = getExtractManger().returnColumns(metadataConn, tableNode, true);

        Assert.assertNotNull(columns);
        Assert.assertTrue(columns.isEmpty());
        // verify
        verifyTableNode4ReturnColumns2TableTypeSynonym(tableNode);
        // verifyTdTable4ReturnColumns4DontCreateConnection(tdTable);
        verifyMeatadataConnection4ReturnColumns4DontCreateConnection_Synonym(metadataConn);
        verifyConnection4ReturnColumns4DontCreateConnection(conn);
        // verifyDbMetadata4ReturnColumns4DontCreateConnection(dbMetadata);
        // verifyColumnsResultSet4ReturnColumns4DontCreateConnection(getColumnsResultSet);

        extractMeta.setConn(null);
    }

    protected TableNode mockTableNode4ReturnColumns2TableTypeSynonym() throws Exception {
        TableNode tableNode = spy(new TableNode());
        // must type for table
        when(tableNode.getType()).thenReturn(TableNode.TABLE);

        // because want to test the replace for "/"
        // will do assert getValue, so don't stub it.
        //        when(tableNode.getValue()).thenReturn("/table1"); //$NON-NLS-1$
        tableNode.setValue("/table2"); //$NON-NLS-1$

        // only test table type if SYNONYM, will do it in following test method
        // "testReturnColumns4DontCreateConnection2TableTypeSynonym"
        when(tableNode.getItemType()).thenReturn(ETableTypes.TABLETYPE_SYNONYM.getName());
        // PTODO
        return tableNode;
    }

    protected void verifyTableNode4ReturnColumns2TableTypeSynonym(TableNode tableNode) {
        verify(tableNode).getType();
        verify(tableNode).getTable();
        verify(tableNode, times(1)).getValue();
        Assert.assertEquals(tableNode.getValue(), "/table2"); //$NON-NLS-1$
        verify(tableNode).getItemType();
    }

    /**
     * 
     * DOC ggu Comment method "testReturnColumns4reCreateConnection".
     * 
     * test when the dontCreateClose is true
     * 
     * @throws Exception
     */
    @Test
    public void testReturnColumns4reCreateConnection() throws Exception {
        Assert.assertNotNull(getExtractManger());

        Connection conn = mockConnection4ReturnColumns4reCreateConnection();
        extractMeta.setConn(conn);
        extractMeta.setReconnect(false);

        IMetadataConnection metadataConn = mockMetadataConnection4ReturnColumns4reCreateConnection();
        //
        DatabaseMetaData dbMetadata = mockDatabaseMetaData4ReturnColumns4reCreateConnection();
        when(conn.getMetaData()).thenReturn(dbMetadata);

        // getColumns
        ResultSet getColumnsResultSet = mockGetColumnsResultSet4ReturnColumns4reCreateConnection();
        doReturn(getColumnsResultSet).when(dbMetadata).getColumns(anyString(), anyString(), anyString(), anyString());

        TableNode tableNode = mockTableNode4ReturnColumns4reCreateConnection();
        TdTable tdTable = mockTable4ReturnColumns4reCreateConnection();
        when(tableNode.getTable()).thenReturn(tdTable);

        List list = extractMeta.getConnection(metadataConn.getDbType(), metadataConn.getUrl(), metadataConn.getUsername(),
                metadataConn.getPassword(), metadataConn.getDatabase(), metadataConn.getSchema(), metadataConn.getDriverClass(),
                metadataConn.getDriverJarPath(), metadataConn.getDbVersionString(), metadataConn.getAdditionalParams());
        assertTrue(list.size() == 0);
        list.add(conn);
        assertTrue(list.size() != 0);

        List conMockList = mock(List.class);
        conMockList.add(conn);
        DriverShim wapperDriver = mock(DriverShim.class);
        conMockList.add(wapperDriver);

        when(conMockList.get(0)).thenReturn(conn);
        when(conMockList.get(1)).thenReturn(wapperDriver);
        when(conMockList.get(2)).thenThrow(new RuntimeException());

        verify(conMockList, times(1)).add(conn);
        verify(conMockList, times(1)).add(wapperDriver);
        //
        List<TdColumn> columns = getExtractManger().returnColumns(metadataConn, tableNode, false);
        Assert.assertNotNull(columns);
        Assert.assertTrue(columns.isEmpty());
        // verify
        // PTODO

        extractMeta.setConn(null);

    }

    protected Connection mockConnection4ReturnColumns4reCreateConnection() throws Exception {
        Connection conn = mock(Connection.class);
        // make sure use the mock connection
        when(conn.isClosed()).thenReturn(false);
        doNothing().when(conn).close();
        return conn;
    }

    protected IMetadataConnection mockMetadataConnection4ReturnColumns4reCreateConnection() {
        IMetadataConnection metadataConn = mock(IMetadataConnection.class);

        String dbType = this.getExtractManger().getDbType().getXmlName();
        when(metadataConn.getDbType()).thenReturn(dbType);
        when(metadataConn.getUsername()).thenReturn("root");
        when(metadataConn.getPassword()).thenReturn("root");
        // only for TERADATA
        when(metadataConn.isSqlMode()).thenReturn(false);
        when(metadataConn.getDatabase()).thenReturn("talend"); //$NON-NLS-1$
        // PTODO
        return metadataConn;
    }

    protected DatabaseMetaData mockDatabaseMetaData4ReturnColumns4reCreateConnection() throws Exception {
        DatabaseMetaData dbMetadata = mock(DatabaseMetaData.class);
        // PTODO
        return dbMetadata;
    }

    protected ResultSet mockGetColumnsResultSet4ReturnColumns4reCreateConnection() throws Exception {
        ResultSet resultSet = mock(ResultSet.class);
        // maybe need test some SQLException
        doNothing().when(resultSet).close();

        // FIXME, later, should find a way to do it,when true
        when(resultSet.next()).thenReturn(false);

        return resultSet;
    }

    protected TableNode mockTableNode4ReturnColumns4reCreateConnection() throws Exception {
        TableNode tableNode = spy(new TableNode());
        // must type for table
        when(tableNode.getType()).thenReturn(TableNode.TABLE);

        // because want to test the replace for "/"
        // will do assert getValue, so don't stub it.
        //        when(tableNode.getValue()).thenReturn("/table2"); //$NON-NLS-1$
        tableNode.setValue("/table2"); //$NON-NLS-1$

        // only test table type if SYNONYM, will do it in following test method
        // "testReturnColumns4DontCreateConnection2TableTypeSynonym"
        when(tableNode.getItemType()).thenReturn(ETableTypes.TABLETYPE_TABLE.getName());
        // PTODO
        return tableNode;
    }

    protected TdTable mockTable4ReturnColumns4reCreateConnection() throws Exception {
        TdTable tdTable = mock(TdTable.class);
        when(tdTable.getName()).thenReturn("TestTable2");
        ResourceSet resourceSet = new ResourceSetImpl();
        Resource resource = resourceSet.createResource(URI.createFileURI("tdtable.xml")); //$NON-NLS-1$
        if (resource != null) {
            resource.getContents().add(tdTable);
        }
        when(tdTable.getFeature()).thenReturn(new BasicEList<Feature>());
        return tdTable;
    }

    /**
     * 
     * DOC ggu Comment method "testReturnTablesFormConnection".
     * 
     * @see ExtractManager.returnTablesFormConnection(IMetadataConnection, TableInfoParameters)
     * @throws Exception
     */
    @Test
    public void testReturnTablesFormConnection() throws Exception {
        Assert.assertNotNull(getExtractManger());

        IMetadataConnection metadataConn = mockMetadataConnection4ReturnTablesFormConnection();
        Connection conn = mockConnection4ReturnTablesFormConnection();
        extractMeta.setConn(conn);
        extractMeta.setReconnect(false);

        DatabaseMetaData dbMetadata = mockDatabaseMetaData4ReturnTablesFormConnection();
        when(conn.getMetaData()).thenReturn(dbMetadata);

        ResultSet resultSet = mockGetColumnsResultSet4ReturnTablesFormConnection();
        when(dbMetadata.getTableTypes()).thenReturn(resultSet);
        final String value = "abc";
        when(resultSet.getString(anyString())).thenReturn(value);
        //
        TableInfoParameters tableInfoParameters = mock(TableInfoParameters.class);
        when(tableInfoParameters.isUsedName()).thenReturn(false);
        when(tableInfoParameters.getSqlFiter()).thenReturn("");

        List connList = getExtractManger().returnTablesFormConnection(metadataConn, tableInfoParameters);
        Assert.assertNotNull(connList);
        Assert.assertTrue(connList.isEmpty());

        // verify
        // verifyMeatadataConnection4ReturnTablesFormConnection(metadataConn);
        // verifyConnection4ReturnTablesFormConnection(conn);
        // verifyDbMetadata4ReturnTablesFormConnection(dbMetadata);

        extractMeta.setConn(null);
    }

    protected IMetadataConnection mockMetadataConnection4ReturnTablesFormConnection() {
        IMetadataConnection metadataConn = mock(IMetadataConnection.class);

        String dbType = this.getExtractManger().getDbType().getXmlName();
        when(metadataConn.getDbType()).thenReturn(dbType);
        when(metadataConn.getUsername()).thenReturn("root");
        when(metadataConn.getPassword()).thenReturn("root");
        // only for TERADATA
        when(metadataConn.isSqlMode()).thenReturn(false);
        when(metadataConn.getDatabase()).thenReturn("talend"); //$NON-NLS-1$
        // PTODO
        return metadataConn;
    }

    protected Connection mockConnection4ReturnTablesFormConnection() throws Exception {
        Connection conn = mock(Connection.class);
        // make sure use the mock connection
        when(conn.isClosed()).thenReturn(false);
        doNothing().when(conn).close();
        return conn;
    }

    protected DatabaseMetaData mockDatabaseMetaData4ReturnTablesFormConnection() throws Exception {
        DatabaseMetaData dbMetadata = mock(DatabaseMetaData.class);
        return dbMetadata;
    }

    protected ResultSet mockGetColumnsResultSet4ReturnTablesFormConnection() throws Exception {
        ResultSet resultSet = mock(ResultSet.class);
        // maybe need test some SQLException
        doNothing().when(resultSet).close();
        // FIXME, later, should find a way to do it,when true
        when(resultSet.next()).thenReturn(false);
        return resultSet;
    }

    protected void verifyMeatadataConnection4ReturnTablesFormConnection(IMetadataConnection metadataConn) {
        verify(metadataConn).getDbType();
        verify(metadataConn).isSqlMode();
        verify(metadataConn).getDatabase();
    }

    protected void verifyConnection4ReturnTablesFormConnection(Connection conn) throws SQLException {
        verify(conn).isClosed();
    }

    protected void verifyDbMetadata4ReturnTablesFormConnection(DatabaseMetaData dbMetadata) throws SQLException {
        verify(dbMetadata).getColumns(anyString(), anyString(), anyString(), anyString());
    }
}
