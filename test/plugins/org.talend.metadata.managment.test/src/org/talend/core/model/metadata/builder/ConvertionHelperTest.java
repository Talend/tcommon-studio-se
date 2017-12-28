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
package org.talend.core.model.metadata.builder;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.emf.common.util.EList;
import org.junit.Test;
import org.talend.core.IRepositoryContextService;
import org.talend.core.model.metadata.DiSchemaConstants;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataColumn;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.metadata.MetadataToolHelper;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.FileConnection;
import org.talend.core.model.metadata.builder.connection.SAPBWTable;
import org.talend.core.model.repository.IRepositoryPrefConstants;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.utils.ReflectionUtils;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SAPBWTableHelper;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.repository.model.IProxyRepositoryFactory;

import orgomg.cwm.objectmodel.core.TaggedValue;

public class ConvertionHelperTest {

    @Test
    public void testConvertMetadataTable() {
        SAPBWTable table = ConnectionFactory.eINSTANCE.createSAPBWTable();
        table.setSourceSystemName("TALEND");
        table.setInnerIOType(SAPBWTableHelper.IO_INNERTYPE_HIERARCHY);
        IMetadataTable newTable = ConvertionHelper.convert(table);
        String sourceSysName = newTable.getAdditionalProperties().get(SAPBWTableHelper.SAP_DATASOURCE_SOURCESYSNAME);
        String innerIOType = newTable.getAdditionalProperties().get(SAPBWTableHelper.SAP_INFOOBJECT_INNER_TYPE);
        assertEquals("TALEND", sourceSysName);
        assertEquals(SAPBWTableHelper.IO_INNERTYPE_HIERARCHY, innerIOType);
    }

    @Test
    public void testConvertDatabaseConnectionBooleanString() {
        DatabaseConnection dbProvider = ConnectionFactory.eINSTANCE.createDatabaseConnection();

        final IRepositoryContextService oldRepositoryContextService = ConvertionHelper.getRepositoryContextService();

        try {
            // no clone, so return self always

            ReflectionUtils.setStaticFieldValue(ConvertionHelper.class.getName(), ConvertionHelper.class.getClassLoader(),
                    "repositoryContextService", new IRepositoryContextService() {

                        @Override
                        public DatabaseConnection cloneOriginalValueConnection(DatabaseConnection dbConn) {
                            return dbConn;
                        }

                        @Override
                        public DatabaseConnection cloneOriginalValueConnection(DatabaseConnection dbConn, boolean defaultContext) {
                            return dbConn;
                        }

                        @Override
                        public DatabaseConnection cloneOriginalValueConnection(DatabaseConnection dbConn, boolean defaultContext,
                                String selectedContext) {
                            return dbConn;
                        }

                        @Override
                        public void setMetadataConnectionParameter(DatabaseConnection dbConn, IMetadataConnection metaConn) {

                        }

                        @Override
                        public FileConnection cloneOriginalValueConnection(FileConnection fileConn) {
                            return fileConn;
                        }

                    });
            setJDBCMysqlConnection(dbProvider);
            IMetadataConnection convertMetadata = ConvertionHelper.convert(dbProvider, false, null);
            validJDBCMysqlConnection(convertMetadata, dbProvider);
        } finally {
            ReflectionUtils.setStaticFieldValue(ConvertionHelper.class.getName(), ConvertionHelper.class.getClassLoader(),
                    "repositoryContextService", oldRepositoryContextService);
            // ConvertionHelper.repositoryContextService = oldRepositoryContextService;
        }
    }

    private boolean validJDBCMysqlConnection(IMetadataConnection convertMetadata, DatabaseConnection dbProvider) {
        assertEquals(convertMetadata.getComment(), ""); //$NON-NLS-1$
        assertEquals(convertMetadata.getDatabase(), ""); //$NON-NLS-1$
        assertEquals(convertMetadata.getDataSourceName(), ""); //$NON-NLS-1$
        assertEquals(convertMetadata.getDbType(), "General JDBC"); //$NON-NLS-1$
        assertEquals(convertMetadata.getDriverJarPath(), "E:\\tools\\jdbc\\mysql-connector-java-5.1.12-bin.jar"); //$NON-NLS-1$
        assertEquals(convertMetadata.getDriverClass(), "org.gjt.mm.mysql.Driver"); //$NON-NLS-1$
        assertEquals(convertMetadata.getId(), "_9bw28cccEeGQNaw_qcyMFw"); //$NON-NLS-1$
        assertEquals(convertMetadata.getLabel(), "jdbcmysql1"); //$NON-NLS-1$
        assertEquals(convertMetadata.getPassword(), "shenze"); //$NON-NLS-1$
        assertEquals(convertMetadata.getPort(), "3306"); //$NON-NLS-1$
        assertEquals(convertMetadata.getSqlSyntax(), "SQL Syntax"); //$NON-NLS-1$
        assertEquals(convertMetadata.getUrl(), "jdbc:mysql://192.168.30.151:3306/?noDatetimeStringSync=true"); //$NON-NLS-1$
        assertEquals(convertMetadata.getUsername(), "shenze"); //$NON-NLS-1$
        assertEquals(convertMetadata.getMapping(), "mysql_id"); //$NON-NLS-1$
        assertEquals(convertMetadata.getProduct(), "JDBC"); //$NON-NLS-1$
        assertEquals(convertMetadata.isSqlMode(), false);
        assertEquals(convertMetadata.isContentModel(), false);
        assertEquals(convertMetadata.getCurrentConnection(), dbProvider); // clone via service always
        assertNotNull(convertMetadata.getPurpose());
        assertEquals(convertMetadata.getPurpose(), "my test purpose"); //$NON-NLS-1$
        assertNotNull(convertMetadata.getDescription());
        assertEquals(convertMetadata.getDescription(), "my test description"); //$NON-NLS-1$
        assertNotNull(convertMetadata.getAuthor());
        assertEquals(convertMetadata.getAuthor(), "test@talend.com"); //$NON-NLS-1$
        assertNotNull(convertMetadata.getStatus());
        assertEquals(convertMetadata.getStatus(), "DEV"); //$NON-NLS-1$
        assertNotNull(convertMetadata.getVersion());
        assertEquals(convertMetadata.getVersion(), "1.1"); //$NON-NLS-1$
        assertNotNull(convertMetadata.getUniverse());
        assertEquals(convertMetadata.getUniverse(), ""); //$NON-NLS-1$
        return true;
    }

    private boolean setJDBCMysqlConnection(DatabaseConnection dbProvider) {
        // General JDBC case
        dbProvider.setComment(""); //$NON-NLS-1$
        dbProvider.setSID(""); //$NON-NLS-1$
        dbProvider.setDatasourceName(""); //$NON-NLS-1$
        dbProvider.setDatabaseType("General JDBC"); //$NON-NLS-1$
        dbProvider.setDriverJarPath("E:\\tools\\jdbc\\mysql-connector-java-5.1.12-bin.jar"); //$NON-NLS-1$
        dbProvider.setDbVersionString(""); //$NON-NLS-1$
        dbProvider.setDriverClass("org.gjt.mm.mysql.Driver"); //$NON-NLS-1$
        dbProvider.setFileFieldName(""); //$NON-NLS-1$
        dbProvider.setId("_9bw28cccEeGQNaw_qcyMFw"); //$NON-NLS-1$
        dbProvider.setLabel("jdbcmysql1"); //$NON-NLS-1$
        dbProvider.setNullChar(""); //$NON-NLS-1$
        dbProvider.setRawPassword("shenze"); //$NON-NLS-1$
        dbProvider.setPort("3306"); //$NON-NLS-1$
        dbProvider.setServerName(""); //$NON-NLS-1$
        dbProvider.setSqlSynthax("SQL Syntax"); //$NON-NLS-1$
        dbProvider.setUiSchema(""); //$NON-NLS-1$
        dbProvider.setStringQuote(""); //$NON-NLS-1$
        dbProvider.setURL("jdbc:mysql://192.168.30.151:3306/?noDatetimeStringSync=true"); //$NON-NLS-1$
        dbProvider.setAdditionalParams(""); //$NON-NLS-1$
        dbProvider.setUsername("shenze"); //$NON-NLS-1$
        dbProvider.setDbmsId("mysql_id"); //$NON-NLS-1$
        dbProvider.setProductId("JDBC"); //$NON-NLS-1$
        dbProvider.setDBRootPath(""); //$NON-NLS-1$
        dbProvider.setSQLMode(false);
        dbProvider.setContextMode(false);
        dbProvider.setContextId(""); //$NON-NLS-1$
        dbProvider.setContextName(""); //$NON-NLS-1$
        ConnectionHelper.setPurpose("my test purpose", dbProvider); //$NON-NLS-1$
        ConnectionHelper.setDescription("my test description", dbProvider); //$NON-NLS-1$
        ConnectionHelper.setAuthor("test@talend.com", dbProvider); //$NON-NLS-1$
        ConnectionHelper.setDevStatus("DEV", dbProvider); //$NON-NLS-1$
        ConnectionHelper.setVersion("1.1", dbProvider); //$NON-NLS-1$
        ConnectionHelper.setUniverse("", dbProvider); //$NON-NLS-1$
        return true;
    }

    @Test
    public void testReadonlyStatusInConvert() {
        String C1 = "C1"; //$NON-NLS-1$
        String C2 = "C2"; //$NON-NLS-1$
        IMetadataTable table = new MetadataTable();
        IMetadataColumn column = new MetadataColumn();
        column.setLabel(C1);
        table.getListColumns().add(column);
        column = new MetadataColumn();
        column.setLabel(C2);
        table.getListColumns().add(column);

        org.talend.core.model.metadata.builder.connection.MetadataTable newTable = ConvertionHelper.convert(table);
        assertFalse(isColumnTaggedAsReadonly(newTable, C2));

        table.getColumn(C2).setReadOnly(true);
        newTable = ConvertionHelper.convert(table);
        assertTrue(isColumnTaggedAsReadonly(newTable, C2));
    }

    private boolean isColumnTaggedAsReadonly(org.talend.core.model.metadata.builder.connection.MetadataTable table,
            String columnName) {
        if (table == null || columnName == null) {
            return false;
        }
        EList<org.talend.core.model.metadata.builder.connection.MetadataColumn> columns = table.getColumns();
        for (org.talend.core.model.metadata.builder.connection.MetadataColumn newColumn : columns) {
            if (columnName.equals(newColumn.getLabel())) {
                EList<TaggedValue> taggedValues = newColumn.getTaggedValue();
                for (TaggedValue taggedValue : taggedValues) {
                    if (DiSchemaConstants.TALEND6_IS_READ_ONLY.equals(taggedValue.getTag())) {
                        return Boolean.valueOf(taggedValue.getValue());
                    }
                }
            }
        }
        return false;
    }

    @Test
    public void testTaggedValueInConvert() {
        String C1 = "C1"; //$NON-NLS-1$
        String C2 = "C2"; //$NON-NLS-1$
        IMetadataTable table = new MetadataTable();
        IMetadataColumn column = new MetadataColumn();
        column.setLabel(C1);
        table.getListColumns().add(column);
        column = new MetadataColumn();
        column.setLabel(C2);
        table.getListColumns().add(column);

        org.talend.core.model.metadata.builder.connection.MetadataTable newTable = ConvertionHelper.convert(table);
        EList<org.talend.core.model.metadata.builder.connection.MetadataColumn> columns = newTable.getColumns();
        for (org.talend.core.model.metadata.builder.connection.MetadataColumn newColumn : columns) {
            assertEquals(0, newColumn.getTaggedValue().size());
        }

        String TAG1 = "TAG1"; //$NON-NLS-1$
        String TAG2 = "TAG2"; //$NON-NLS-1$
        String TAG1_VALUE = "TAG1_VALUE"; //$NON-NLS-1$
        String TAG2_VALUE = "TAG2_VALUE"; //$NON-NLS-1$
        table.getColumn(C1).getAdditionalField().put(TAG1, TAG1_VALUE);
        table.getColumn(C2).getAdditionalField().put(TAG2, TAG2_VALUE);

        newTable = ConvertionHelper.convert(table);
        columns = newTable.getColumns();
        for (org.talend.core.model.metadata.builder.connection.MetadataColumn newColumn : columns) {
            EList<TaggedValue> taggedValue = newColumn.getTaggedValue();
            assertEquals(1, taggedValue.size());
            TaggedValue tv = taggedValue.get(0);
            String tag = tv.getTag();
            String value = tv.getValue();
            if (C1.equals(newColumn.getName())) {
                assertEquals(TAG1, tag);
                assertEquals(TAG1_VALUE, value);
            } else if (C2.equals(newColumn.getName())) {
                assertEquals(TAG2, tag);
                assertEquals(TAG2_VALUE, value);
            }
        }
    }
    
    @Test
    public void testConvert1() {
        IMetadataTable source = new org.talend.core.model.metadata.MetadataTable();
        IMetadataColumn newColumn = new org.talend.core.model.metadata.MetadataColumn();
        newColumn.setId("000000");
        newColumn.setLabel("_long");
        newColumn.setOriginalDbColumnName("long");
        newColumn.getAdditionalField().put("AVRO_TECHNICAL_KEY", "long");
        source.getListColumns().add(newColumn);
        
        newColumn = new org.talend.core.model.metadata.MetadataColumn();
        newColumn.setId("111111");
        newColumn.setLabel("A");
        newColumn.setOriginalDbColumnName("A");
        newColumn.getAdditionalField().put("AVRO_TECHNICAL_KEY", "A");
        source.getListColumns().add(newColumn);
        
        newColumn = new org.talend.core.model.metadata.MetadataColumn();
        newColumn.setId("222222");
        newColumn.setLabel("B");
        newColumn.setOriginalDbColumnName("B");
        newColumn.getAdditionalField().put("AVRO_TECHNICAL_KEY", "B");
        source.getListColumns().add(newColumn);
        
        newColumn = new org.talend.core.model.metadata.MetadataColumn();
        newColumn.setId("333333");
        newColumn.setLabel("_234");
        newColumn.setOriginalDbColumnName("1234");
        newColumn.getAdditionalField().put("AVRO_TECHNICAL_KEY", "_234");
        source.getListColumns().add(newColumn);
        
        newColumn = new org.talend.core.model.metadata.MetadataColumn();
        newColumn.setId("444444");
        newColumn.setLabel("中文");
        newColumn.setOriginalDbColumnName("中文");
        newColumn.getAdditionalField().put("AVRO_TECHNICAL_KEY", "中文");
        source.getListColumns().add(newColumn);
        
        newColumn = new org.talend.core.model.metadata.MetadataColumn();
        newColumn.setId("555555");
        newColumn.setLabel("TEST");
        newColumn.setOriginalDbColumnName("TEST");
        newColumn.getAdditionalField().put("AVRO_TECHNICAL_KEY", "TEST");
        source.getListColumns().add(newColumn);
        
        newColumn = new org.talend.core.model.metadata.MetadataColumn();
        newColumn.setId("666666");
        newColumn.setLabel("TEST1");
        newColumn.setOriginalDbColumnName("TEST");
        newColumn.getAdditionalField().put("AVRO_TECHNICAL_KEY", "TEST1");
        source.getListColumns().add(newColumn);
        
        IEclipsePreferences coreUIPluginNode = new InstanceScope().getNode(ITalendCorePrefConstants.CoreUIPlugin_ID);
        coreUIPluginNode.putBoolean(IRepositoryPrefConstants.ALLOW_SPECIFIC_CHARACTERS_FOR_SCHEMA_COLUMNS, true);
        org.talend.core.model.metadata.builder.connection.MetadataTable metadataTable = ConvertionHelper.convert(source);
        assertTrue(metadataTable.getColumns().get(0).getLabel().equals("_long"));
        assertTrue(metadataTable.getColumns().get(0).getName().equals("long"));
        Iterator<TaggedValue> ite = metadataTable.getColumns().get(0).getTaggedValue().iterator();
        while (ite.hasNext()) {
            TaggedValue t = ite.next();
            if(t.getTag().equals("AVRO_TECHNICAL_KEY")){
                assertTrue(t.getValue().equals("long"));
                break;
            }
        }
        
        assertTrue(metadataTable.getColumns().get(1).getLabel().equals("A"));
        assertTrue(metadataTable.getColumns().get(1).getName().equals("A"));
        ite = metadataTable.getColumns().get(1).getTaggedValue().iterator();
        while (ite.hasNext()) {
            TaggedValue t = ite.next();
            if(t.getTag().equals("AVRO_TECHNICAL_KEY")){
                assertTrue(t.getValue().equals("A"));
                break;
            }
        }
        
        assertTrue(metadataTable.getColumns().get(2).getLabel().equals("B"));
        assertTrue(metadataTable.getColumns().get(2).getName().equals("B"));
        ite = metadataTable.getColumns().get(2).getTaggedValue().iterator();
        while (ite.hasNext()) {
            TaggedValue t = ite.next();
            if(t.getTag().equals("AVRO_TECHNICAL_KEY")){
                assertTrue(t.getValue().equals("B"));
                break;
            }
        }
        
        assertTrue(metadataTable.getColumns().get(3).getLabel().equals("_234"));
        assertTrue(metadataTable.getColumns().get(3).getName().equals("1234"));
        ite = metadataTable.getColumns().get(3).getTaggedValue().iterator();
        while (ite.hasNext()) {
            TaggedValue t = ite.next();
            if(t.getTag().equals("AVRO_TECHNICAL_KEY")){
                assertTrue(t.getValue().equals("_234"));
                break;
            }
        }
        
        assertTrue(metadataTable.getColumns().get(4).getLabel().equals("中文"));
        assertTrue(metadataTable.getColumns().get(4).getName().equals("中文"));
        ite = metadataTable.getColumns().get(4).getTaggedValue().iterator();
        while (ite.hasNext()) {
            TaggedValue t = ite.next();
            if(t.getTag().equals("AVRO_TECHNICAL_KEY")){
                assertTrue(t.getValue().equals("中文"));
                break;
            }
        }
        
        assertTrue(metadataTable.getColumns().get(5).getLabel().equals("TEST"));
        assertTrue(metadataTable.getColumns().get(5).getName().equals("TEST"));
        ite = metadataTable.getColumns().get(5).getTaggedValue().iterator();
        while (ite.hasNext()) {
            TaggedValue t = ite.next();
            if(t.getTag().equals("AVRO_TECHNICAL_KEY")){
                assertTrue(t.getValue().equals("TEST"));
                break;
            }
        }
        
        assertTrue(metadataTable.getColumns().get(6).getLabel().equals("TEST1"));
        assertTrue(metadataTable.getColumns().get(6).getName().equals("TEST"));
        ite = metadataTable.getColumns().get(6).getTaggedValue().iterator();
        while (ite.hasNext()) {
            TaggedValue t = ite.next();
            if(t.getTag().equals("AVRO_TECHNICAL_KEY")){
                assertTrue(t.getValue().equals("TEST1"));
                break;
            }
        }
    }
    
    @Test
    public void testConvert2(){
        IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
        org.talend.core.model.metadata.builder.connection.MetadataTable inputTable = ConnectionFactory.eINSTANCE.createMetadataTable();
        inputTable.setId(factory.getNextId());
        inputTable.setLabel("Input");
        
        org.talend.core.model.metadata.builder.connection.MetadataColumn creatMetadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
        creatMetadataColumn.setId(factory.getNextId());
        creatMetadataColumn.setLabel("long");
        creatMetadataColumn.setName("long");
        TaggedValue tv = TaggedValueHelper.createTaggedValue("AVRO_TECHNICAL_KEY", "long");
        creatMetadataColumn.getTaggedValue().add(tv);
        inputTable.getColumns().add(creatMetadataColumn);
        
        
        creatMetadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
        creatMetadataColumn.setId(factory.getNextId());
        creatMetadataColumn.setLabel("A");
        creatMetadataColumn.setName("A");
        tv = TaggedValueHelper.createTaggedValue("AVRO_TECHNICAL_KEY", "A");
        creatMetadataColumn.getTaggedValue().add(tv);
        inputTable.getColumns().add(creatMetadataColumn);
        
        creatMetadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
        creatMetadataColumn.setId(factory.getNextId());
        creatMetadataColumn.setLabel("B");
        creatMetadataColumn.setName("B");
        tv = TaggedValueHelper.createTaggedValue("AVRO_TECHNICAL_KEY", "B");
        creatMetadataColumn.getTaggedValue().add(tv);
        inputTable.getColumns().add(creatMetadataColumn);
        
        creatMetadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
        creatMetadataColumn.setId(factory.getNextId());
        creatMetadataColumn.setLabel("_234");
        creatMetadataColumn.setName("1234");
        tv = TaggedValueHelper.createTaggedValue("AVRO_TECHNICAL_KEY", "_234");
        creatMetadataColumn.getTaggedValue().add(tv);
        inputTable.getColumns().add(creatMetadataColumn);
        
        creatMetadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
        creatMetadataColumn.setId(factory.getNextId());
        creatMetadataColumn.setLabel("中文");
        creatMetadataColumn.setName("中文");
        tv = TaggedValueHelper.createTaggedValue("AVRO_TECHNICAL_KEY", "中文");
        creatMetadataColumn.getTaggedValue().add(tv);
        inputTable.getColumns().add(creatMetadataColumn);
        
        creatMetadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
        creatMetadataColumn.setId(factory.getNextId());
        creatMetadataColumn.setLabel("TEST");
        creatMetadataColumn.setName("TEST");
        tv = TaggedValueHelper.createTaggedValue("AVRO_TECHNICAL_KEY","TEST");
        creatMetadataColumn.getTaggedValue().add(tv);
        inputTable.getColumns().add(creatMetadataColumn);
        
        List<String> labels = new ArrayList<String>();
        for(org.talend.core.model.metadata.builder.connection.MetadataColumn c:inputTable.getColumns()){
            labels.add(c.getLabel());
        }
        creatMetadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
        creatMetadataColumn.setId(factory.getNextId());
        creatMetadataColumn.setLabel("TEST1");
        creatMetadataColumn.setName("TEST");
        tv = TaggedValueHelper.createTaggedValue("AVRO_TECHNICAL_KEY", "TEST1");
        creatMetadataColumn.getTaggedValue().add(tv);
        inputTable.getColumns().add(creatMetadataColumn);
        
        IEclipsePreferences coreUIPluginNode = new InstanceScope().getNode(ITalendCorePrefConstants.CoreUIPlugin_ID);
        coreUIPluginNode.putBoolean(IRepositoryPrefConstants.ALLOW_SPECIFIC_CHARACTERS_FOR_SCHEMA_COLUMNS, true);
        IMetadataTable targetTable = MetadataToolHelper.convert(inputTable);
        assertTrue(targetTable.getListColumns().get(0).getLabel().equals("_long"));
        assertTrue(targetTable.getListColumns().get(0).getOriginalDbColumnName().equals("long"));
        assertTrue(targetTable.getListColumns().get(0).getAdditionalField().get("AVRO_TECHNICAL_KEY").equals("long"));
        
        assertTrue(targetTable.getListColumns().get(1).getLabel().equals("A"));
        assertTrue(targetTable.getListColumns().get(1).getOriginalDbColumnName().equals("A"));
        assertTrue(targetTable.getListColumns().get(1).getAdditionalField().get("AVRO_TECHNICAL_KEY").equals("A"));
        
        assertTrue(targetTable.getListColumns().get(2).getLabel().equals("B"));
        assertTrue(targetTable.getListColumns().get(2).getOriginalDbColumnName().equals("B"));
        assertTrue(targetTable.getListColumns().get(2).getAdditionalField().get("AVRO_TECHNICAL_KEY").equals("B"));
        
        assertTrue(targetTable.getListColumns().get(3).getLabel().equals("_234"));
        assertTrue(targetTable.getListColumns().get(3).getOriginalDbColumnName().equals("1234"));
        assertTrue(targetTable.getListColumns().get(3).getAdditionalField().get("AVRO_TECHNICAL_KEY").equals("_234"));
        
        assertTrue(targetTable.getListColumns().get(4).getLabel().equals("中文"));
        assertTrue(targetTable.getListColumns().get(4).getOriginalDbColumnName().equals("中文"));
        assertTrue(targetTable.getListColumns().get(4).getAdditionalField().get("AVRO_TECHNICAL_KEY").equals("中文"));
        
        assertTrue(targetTable.getListColumns().get(5).getLabel().equals("TEST"));
        assertTrue(targetTable.getListColumns().get(5).getOriginalDbColumnName().equals("TEST"));
        assertTrue(targetTable.getListColumns().get(5).getAdditionalField().get("AVRO_TECHNICAL_KEY").equals("TEST"));
        
        assertTrue(targetTable.getListColumns().get(6).getLabel().equals("TEST1"));
        assertTrue(targetTable.getListColumns().get(6).getOriginalDbColumnName().equals("TEST"));
        assertTrue(targetTable.getListColumns().get(6).getAdditionalField().get("AVRO_TECHNICAL_KEY").equals("TEST1"));
        
    }

}
