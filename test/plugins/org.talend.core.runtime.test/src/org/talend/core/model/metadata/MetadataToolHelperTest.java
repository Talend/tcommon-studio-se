// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.apache.avro.Schema;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.QueriesConnection;
import org.talend.core.model.metadata.builder.connection.SAPConnection;
import org.talend.core.model.metadata.builder.connection.SAPFunctionUnit;
import org.talend.core.model.metadata.types.ContextParameterJavaTypeManager;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.SAPConnectionItem;
import org.talend.core.model.repository.IRepositoryPrefConstants;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.PackageHelper;
import org.talend.daikon.talend6.Talend6SchemaConstants;
import org.talend.repository.model.IProxyRepositoryFactory;
import orgomg.cwm.resource.record.RecordFactory;
import orgomg.cwm.resource.record.RecordFile;

/**
 * DOC hwang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class MetadataToolHelperTest {

    private static org.talend.core.model.metadata.builder.connection.Connection connection;

    private static DatabaseConnectionItem databaseConnItem;

    private static MetadataTable inputTable;

    /**
     * DOC Administrator Comment method "setUp".
     * 
     * @throws PersistenceException
     * 
     * 
     */
    @BeforeClass
    public static void setUp() throws PersistenceException {
        init();
    }

    private static void init() throws PersistenceException {
        connection = ConnectionFactory.eINSTANCE.createDatabaseConnection();
        connection.setName("mysql_1");

        IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
        connection.setId(factory.getNextId());
        databaseConnItem = PropertiesFactory.eINSTANCE.createDatabaseConnectionItem();
        Property myProperty = PropertiesFactory.eINSTANCE.createProperty();
        myProperty.setId(factory.getNextId());
        myProperty.setLabel("mysql_1");
        myProperty.setVersion("0.1");

        ItemState itemState = PropertiesFactory.eINSTANCE.createItemState();
        itemState.setPath("");

        databaseConnItem.setProperty(myProperty);
        databaseConnItem.setState(itemState);
        databaseConnItem.setConnection(connection);

        RecordFile record = (RecordFile) ConnectionHelper.getPackage(connection.getName(), connection, RecordFile.class);
        inputTable = ConnectionFactory.eINSTANCE.createMetadataTable();

        inputTable.setId(factory.getNextId());
        inputTable.setLabel("Input");

        if (record != null) {
            PackageHelper.addMetadataTable(inputTable, record);
        } else {
            RecordFile newrecord = RecordFactory.eINSTANCE.createRecordFile();
            newrecord.setName(connection.getName());
            ConnectionHelper.addPackage(newrecord, connection);
            PackageHelper.addMetadataTable(inputTable, newrecord);
        }

        factory.create(databaseConnItem, new Path(""));
        factory.save(databaseConnItem);
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
     * Test method for {@link org.talend.core.model.metadata.MetadataToolHelper#isBoolean(java.lang.String)}.
     */
    @Test
    public void testIsBoolean() {
        String value = "id_Boolean";
        assertEquals(value, JavaTypesManager.BOOLEAN.getId());
    }

    /**
     * Test method for {@link org.talend.core.model.metadata.MetadataToolHelper#isDirectory(java.lang.String)}.
     */
    @Test
    public void testIsDirectory() {
        ECodeLanguage codeLanguage = LanguageManager.getCurrentLanguage();
        String java_value = "id_Directory";
        String perl_value = "Directory";
        if (codeLanguage == ECodeLanguage.JAVA) {
            assertEquals(java_value, JavaTypesManager.DIRECTORY.getId());
        } else {
            assertEquals(perl_value, ContextParameterJavaTypeManager.PERL_DIRECTORY);
        }
    }

    /**
     * Test method for {@link org.talend.core.model.metadata.MetadataToolHelper#isList(java.lang.String)}.
     */
    @Test
    public void testIsList() {
        ECodeLanguage codeLanguage = LanguageManager.getCurrentLanguage();
        String java_value = "id_" + "List Of Value";
        String perl_value = "List Of Value";
        if (codeLanguage == ECodeLanguage.JAVA) {
            assertEquals(java_value, JavaTypesManager.VALUE_LIST.getId());
        } else {
            assertEquals(perl_value, ContextParameterJavaTypeManager.PERL_VALUE_LIST);
        }
    }

    /**
     * Test method for {@link org.talend.core.model.metadata.MetadataToolHelper#isDate(java.lang.String)}.
     */
    @Test
    public void testIsDate() {
        ECodeLanguage codeLanguage = LanguageManager.getCurrentLanguage();
        String java_value = "id_" + Date.class.getSimpleName();
        String perl_value = "Day";
        if (codeLanguage == ECodeLanguage.JAVA) {
            assertEquals(java_value, JavaTypesManager.DATE.getId());
        } else {
            assertEquals(perl_value, ContextParameterJavaTypeManager.PERL_DAY);
        }
    }

    /**
     * Test method for {@link org.talend.core.model.metadata.MetadataToolHelper#isFile(java.lang.String)}.
     */
    @Test
    public void testIsFile() {
        ECodeLanguage codeLanguage = LanguageManager.getCurrentLanguage();
        String java_value = "id_" + "File";
        String perl_value = "File";
        if (codeLanguage == ECodeLanguage.JAVA) {
            assertEquals(java_value, JavaTypesManager.FILE.getId());
        } else {
            assertEquals(perl_value, ContextParameterJavaTypeManager.PERL_FILE);
        }
    }

    /**
     * Test method for {@link org.talend.core.model.metadata.MetadataToolHelper#isPassword(java.lang.String)}.
     */
    @Test
    public void testIsPassword() {
        ECodeLanguage codeLanguage = LanguageManager.getCurrentLanguage();
        String java_value = "id_" + "Password";
        String perl_value = "Password";
        if (codeLanguage == ECodeLanguage.JAVA) {
            assertEquals(java_value, JavaTypesManager.PASSWORD.getId());
        } else {
            assertEquals(perl_value, ContextParameterJavaTypeManager.PERL_PASSWORD);
        }
    }

    /**
     * Test method for {@link org.talend.core.model.metadata.MetadataToolHelper#isValidSchemaName(java.lang.String)}.
     */
    @Test
    public void testIsValidSchemaName() {
        String name = "na me";
        assertFalse(name, false);
        name = "name";
        assertTrue(name, true);
    }

    /**
     * Test method for {@link org.talend.core.model.metadata.MetadataToolHelper#isValidColumnName(java.lang.String)}.
     */
    @Test
    public void testIsValidColumnName() {
        String name = null;
        assertFalse(name, false);
        name = "public";
        assertFalse(name, false);

        // return isAllowSpecificCharacters() || Pattern.matches(RepositoryConstants.COLUMN_NAME_PATTERN, name);
    }

    /**
     * Test method for
     * {@link org.talend.core.model.metadata.MetadataToolHelper#validateColumnName(java.lang.String, int)}.
     */
    @Test
    public void testValidateColumnName() {
        IEclipsePreferences preferences = new InstanceScope().getNode(ITalendCorePrefConstants.CoreUIPlugin_ID);
        preferences.putBoolean(IRepositoryPrefConstants.ALLOW_SPECIFIC_CHARACTERS_FOR_SCHEMA_COLUMNS, false);

        String columnName = MetadataToolHelper.validateColumnName("public", 0);
        assertEquals(columnName, "Column0");

        columnName = MetadataToolHelper.validateColumnName("id", 0);
        assertEquals(columnName, "id");

        columnName = MetadataToolHelper.validateColumnName("na&me", 0);
        assertEquals(columnName, "na_me");

        columnName = MetadataToolHelper.validateColumnName("addr_ess", 0);
        assertEquals(columnName, "addr_ess");

        columnName = MetadataToolHelper.validateColumnName("9city", 0);
        assertEquals(columnName, "_city");

        columnName = MetadataToolHelper.validateColumnName("country1", 0);
        assertEquals(columnName, "country1");

        columnName = MetadataToolHelper.validateColumnName("problème", 0);
        assertEquals(columnName, "probleme");

        columnName = MetadataToolHelper.validateColumnName("test(a)", 0);
        assertEquals(columnName, "test_a_");

        columnName = MetadataToolHelper.validateColumnName("MyColumn_你好", 0);
        assertEquals("MyColumn___", columnName);

        columnName = MetadataToolHelper.validateColumnName("你好", 0);
        assertEquals("Column0", columnName);

        preferences.putBoolean(IRepositoryPrefConstants.ALLOW_SPECIFIC_CHARACTERS_FOR_SCHEMA_COLUMNS, true);

        columnName = MetadataToolHelper.validateColumnName("你好", 0);
        assertEquals("你好", columnName);

        columnName = MetadataToolHelper.validateColumnName("My Strange (?) Column !", 0);
        assertEquals("My_Strange_____Column__", columnName);
    }

    /**
     * Test method for {@link org.talend.core.model.metadata.MetadataToolHelper#validateTableName(java.lang.String)}.
     */
    @Test
    public void testValidateTableName() {

        String tableName = "public";
        tableName = MetadataToolHelper.validateTableName(tableName);
        assertEquals(tableName, "public_1");

        tableName = "id";
        tableName = MetadataToolHelper.validateTableName(tableName);
        assertEquals(tableName, "id");

        tableName = "na&me";
        tableName = MetadataToolHelper.validateTableName(tableName);
        assertEquals(tableName, "na_me");

        tableName = "addr_ess";
        tableName = MetadataToolHelper.validateTableName(tableName);
        assertEquals(tableName, "addr_ess");

        tableName = "9city";
        tableName = MetadataToolHelper.validateTableName(tableName);
        assertEquals(tableName, "_city");

        tableName = "cou ntry1";
        tableName = MetadataToolHelper.validateTableName(tableName);
        assertEquals(tableName, "cou_ntry1");

        tableName = "_dic";
        tableName = MetadataToolHelper.validateTableName(tableName);
        assertEquals(tableName, "_dic");

    }

    /**
     * Test method for {@link org.talend.core.model.metadata.MetadataToolHelper#validateValue(java.lang.String)}.
     */
    @Test
    public void testValidateValue() {

        String columnName = "public";
        String resultName = MetadataToolHelper.validateValue(columnName);
        assertEquals(resultName, "public");

        columnName = "id";
        resultName = MetadataToolHelper.validateValue(columnName);
        assertEquals(resultName, "id");

        columnName = "na&me";
        resultName = MetadataToolHelper.validateValue(columnName);
        assertEquals(resultName, "na_me");

        columnName = "addr_ess";
        resultName = MetadataToolHelper.validateValue(columnName);
        assertEquals(resultName, "addr_ess");

        columnName = "9city";
        resultName = MetadataToolHelper.validateValue(columnName);
        assertEquals(resultName, "_9city");

        columnName = "cou ntry1";
        resultName = MetadataToolHelper.validateValue(columnName);
        assertEquals(resultName, "cou_ntry1");

        columnName = "_dic";
        resultName = MetadataToolHelper.validateValue(columnName);
        assertEquals(resultName, "_dic");

    }

    /**
     * Test method for
     * {@link org.talend.core.model.metadata.MetadataToolHelper#validateValueNoLengthLimit(java.lang.String)}.
     */
    @Test
    public void testValidateValueNoLengthLimit() {
        String columnName = "public";
        columnName = MetadataToolHelper.validateValueNoLengthLimit(columnName);
        assertEquals(columnName, "public");

        columnName = "id";
        columnName = MetadataToolHelper.validateValueNoLengthLimit(columnName);
        assertEquals(columnName, "id");

        columnName = "na&me";
        columnName = MetadataToolHelper.validateValueNoLengthLimit(columnName);
        assertEquals(columnName, "na_me");

        columnName = "addr_ess";
        columnName = MetadataToolHelper.validateValueNoLengthLimit(columnName);
        assertEquals(columnName, "addr_ess");

        columnName = "9city";
        columnName = MetadataToolHelper.validateValueNoLengthLimit(columnName);
        assertEquals(columnName, "_9city");

        columnName = "cou ntry1";
        columnName = MetadataToolHelper.validateValueNoLengthLimit(columnName);
        assertEquals(columnName, "cou_ntry1");

        columnName = "_dic";
        columnName = MetadataToolHelper.validateValueNoLengthLimit(columnName);
        assertEquals(columnName, "_dic");

    }

    /**
     * Test method for
     * {@link org.talend.core.model.metadata.MetadataToolHelper#validateValueForDBType(java.lang.String)}.
     */
    @Test
    public void testValidateValueForDBType() {

        String columnName = "public";
        String resultName = MetadataToolHelper.validateValueForDBType(columnName);
        assertEquals(resultName, "public");

        columnName = "id";
        resultName = MetadataToolHelper.validateValueForDBType(columnName);
        assertEquals(resultName, "id");

        columnName = "na&me";
        resultName = MetadataToolHelper.validateValueForDBType(columnName);
        assertEquals(resultName, "na_me");

        columnName = "addr_ess";
        resultName = MetadataToolHelper.validateValueForDBType(columnName);
        assertEquals(resultName, "addr_ess");

        columnName = "9city";
        resultName = MetadataToolHelper.validateValueForDBType(columnName);
        assertEquals(resultName, "_9city");

        columnName = "cou ntry1";
        resultName = MetadataToolHelper.validateValueForDBType(columnName);
        assertEquals(resultName, "cou ntry1");

        columnName = "_dic";
        resultName = MetadataToolHelper.validateValueForDBType(columnName);
        assertEquals(resultName, "_dic");

    }

    /**
     * Test method for
     * {@link org.talend.core.model.metadata.MetadataToolHelper#getMetadataTableFromRepository(java.lang.String)}.
     * 
     * 
     */
    @Test
    public void testGetMetadataTableFromRepository() {
        String metaRepositoryId = databaseConnItem.getProperty().getId() + " - " + inputTable.getLabel();
        MetadataTable table = MetadataToolHelper.getMetadataTableFromRepository(metaRepositoryId);
        assertEquals(inputTable.getLabel(), table.getLabel());

        metaRepositoryId = databaseConnItem.getProperty().getId();
        table = MetadataToolHelper.getMetadataTableFromRepository(metaRepositoryId);
        assertNull(table);
    }

    /**
     * Test method for
     * {@link org.talend.core.model.metadata.MetadataToolHelper#getConnectionFromRepository(java.lang.String)}.
     * 
     */
    @Test
    public void testGetConnectionFromRepository() {
        DatabaseConnection conn = (DatabaseConnection) MetadataToolHelper.getConnectionFromRepository(databaseConnItem
                .getProperty().getId());
        assertEquals(conn.getId(), connection.getId());
    }

    /**
     * Test method for
     * {@link org.talend.core.model.metadata.MetadataToolHelper#getConnectionItemFromRepository(java.lang.String)}.
     * 
     * 
     */
    @Test
    public void testGetConnectionItemFromRepository() {
        DatabaseConnectionItem item = (DatabaseConnectionItem) MetadataToolHelper
                .getConnectionItemFromRepository(databaseConnItem.getProperty().getId());
        assertEquals(item, databaseConnItem);

        String metaRepositoryId = databaseConnItem.getProperty().getId() + " - " + inputTable.getLabel();
        item = (DatabaseConnectionItem) MetadataToolHelper.getConnectionItemFromRepository(metaRepositoryId);
        assertEquals(item, databaseConnItem);
    }

    /**
     * Test method for
     * {@link org.talend.core.model.metadata.MetadataToolHelper#getQueryFromRepository(java.lang.String)}.
     * 
     * 
     */
    @Test
    public void testGetQueryFromRepository() {
        QueriesConnection queryConn = ConnectionFactory.eINSTANCE.createQueriesConnection();
        queryConn.setConnection(connection);
        org.talend.core.model.metadata.builder.connection.Query query = ConnectionFactory.eINSTANCE.createQuery();

        IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
        query.setName("query");
        query.setLabel("query");
        query.setId(factory.getNextId());
        queryConn.getQuery().add(query);

        String qid = databaseConnItem.getProperty().getId() + " - " + query.getLabel();
        org.talend.core.model.metadata.builder.connection.Query qu = MetadataToolHelper.getQueryFromRepository(qid);
        assertEquals(query.getId(), qu.getId());

        qid = databaseConnItem.getProperty().getId();
        qu = MetadataToolHelper.getQueryFromRepository(qid);
        assertNull(qu);
    }

    /**
     * Test method for
     * {@link org.talend.core.model.metadata.MetadataToolHelper#getSAPFunctionFromRepository(java.lang.String)}.
     * 
     * @throws PersistenceException
     */
    @Test
    public void testGetSAPFunctionFromRepository() throws PersistenceException {
        SAPConnection sapConn = ConnectionFactory.eINSTANCE.createSAPConnection();

        sapConn.setName("sap");

        IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
        sapConn.setId(factory.getNextId());
        SAPConnectionItem sapItem = PropertiesFactory.eINSTANCE.createSAPConnectionItem();
        Property myProperty = PropertiesFactory.eINSTANCE.createProperty();
        myProperty.setId(factory.getNextId());
        myProperty.setLabel("sap");
        myProperty.setVersion("0.1");

        ItemState itemState = PropertiesFactory.eINSTANCE.createItemState();
        itemState.setPath("");

        sapItem.setProperty(myProperty);
        sapItem.setState(itemState);
        sapItem.setConnection(sapConn);

        SAPFunctionUnit function = ConnectionFactory.eINSTANCE.createSAPFunctionUnit();

        sapConn.getFuntions().add(function);

        function.setId(factory.getNextId());
        function.setLabel("function");

        factory.create(sapItem, new Path(""));
        factory.save(sapItem);

        String id = sapItem.getProperty().getId() + " - " + function.getLabel();
        SAPFunctionUnit unit = MetadataToolHelper.getSAPFunctionFromRepository(id);
        assertEquals(unit, function);

        id = sapItem.getProperty().getId();
        unit = MetadataToolHelper.getSAPFunctionFromRepository(id);
        assertNull(unit);
    }

    /**
     * Unit tests for {@link org.talend.core.model.metadata.MetadataToolHelper#convertToAvro(IMetadataTable)}
     * 
     * Test a simple MetadataTable.
     */
    @Test
    public void testConvertToAvro_Basic() {
        // Setup with a test table.
        MetadataTable table = ConnectionFactory.eINSTANCE.createMetadataTable();
        table.setLabel("testTable");
        table.setComment("A comment about this table.");
        ArrayList<org.talend.core.model.metadata.builder.connection.MetadataColumn> columns = new ArrayList<>();
        {
            org.talend.core.model.metadata.builder.connection.MetadataColumn column = ConnectionFactory.eINSTANCE
                    .createMetadataColumn();
            column.setLabel("id");
            column.setTalendType(JavaTypesManager.INTEGER.getId());
            columns.add(column);
        }
        {
            org.talend.core.model.metadata.builder.connection.MetadataColumn column = ConnectionFactory.eINSTANCE
                    .createMetadataColumn();
            column.setLabel("name");
            column.setTalendType(JavaTypesManager.STRING.getId());
            columns.add(column);
        }
        {
            org.talend.core.model.metadata.builder.connection.MetadataColumn column = ConnectionFactory.eINSTANCE
                    .createMetadataColumn();
            column.setLabel("valid");
            column.setTalendType(JavaTypesManager.BOOLEAN.getId());
            columns.add(column);
        }
        table.getColumns().addAll(columns);

        Schema s = MetadataToolHelper.convertToAvro(table);

        assertThat(s.getType(), is(Schema.Type.RECORD));
        assertThat(s.getName(), is("testTable"));
        assertThat(s.getFields(), hasSize(3));
        assertThat(s.getObjectProps().keySet(),
                contains(Talend6SchemaConstants.TALEND6_LABEL, Talend6SchemaConstants.TALEND6_COMMENT));
        assertThat(s.getProp(Talend6SchemaConstants.TALEND6_LABEL), is("testTable"));
        assertThat(s.getProp(Talend6SchemaConstants.TALEND6_COMMENT), is("A comment about this table."));

        Schema.Field f = s.getFields().get(0);
        assertThat(f.schema().getType(), is(Schema.Type.INT));
        assertThat(s.getName(), is("id"));
        assertThat(s.getObjectProps().keySet(),
                contains(Talend6SchemaConstants.TALEND6_LABEL, Talend6SchemaConstants.TALEND6_COLUMN_TALEND_TYPE));
        assertThat(s.getProp(Talend6SchemaConstants.TALEND6_LABEL), is("id"));
        assertThat(s.getProp(Talend6SchemaConstants.TALEND6_COLUMN_TALEND_TYPE), is("id_Integer"));

        f = s.getFields().get(1);
        assertThat(f.schema().getType(), is(Schema.Type.STRING));
        assertThat(s.getName(), is("name"));
        assertThat(s.getObjectProps().keySet(),
                contains(Talend6SchemaConstants.TALEND6_LABEL, Talend6SchemaConstants.TALEND6_COLUMN_TALEND_TYPE));
        assertThat(s.getProp(Talend6SchemaConstants.TALEND6_LABEL), is("name"));
        assertThat(s.getProp(Talend6SchemaConstants.TALEND6_COLUMN_TALEND_TYPE), is("id_String"));

        f = s.getFields().get(2);
        assertThat(f.schema().getType(), is(Schema.Type.BOOLEAN));
        assertThat(s.getName(), is("valid"));
        assertThat(s.getObjectProps().keySet(),
                contains(Talend6SchemaConstants.TALEND6_LABEL, Talend6SchemaConstants.TALEND6_COLUMN_TALEND_TYPE));
        assertThat(s.getProp(Talend6SchemaConstants.TALEND6_LABEL), is("id"));
        assertThat(s.getProp(Talend6SchemaConstants.TALEND6_COLUMN_TALEND_TYPE), is("id_Boolean"));
    }
}
