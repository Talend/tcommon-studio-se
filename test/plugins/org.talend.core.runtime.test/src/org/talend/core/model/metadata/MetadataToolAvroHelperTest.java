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
package org.talend.core.model.metadata;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.avro.LogicalType;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.SchemaBuilder.FieldAssembler;
import org.apache.avro.SchemaBuilder.FieldBuilder;
import org.apache.avro.SchemaBuilder.RecordBuilder;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.junit.Test;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.repository.IRepositoryPrefConstants;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.daikon.avro.AvroUtils;
import org.talend.daikon.avro.SchemaConstants;
import org.talend.repository.model.IProxyRepositoryFactory;

import orgomg.cwm.objectmodel.core.TaggedValue;

/**
 * DOC hwang class global comment. Detailled comment <br/>
 *
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 *
 */
public class MetadataToolAvroHelperTest {

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
            column.setNullable(true);
            columns.add(column);
        }
        {
            org.talend.core.model.metadata.builder.connection.MetadataColumn column = ConnectionFactory.eINSTANCE
                    .createMetadataColumn();
            column.setLabel("name");
            column.setTalendType(JavaTypesManager.STRING.getId());
            column.setNullable(false);
            columns.add(column);
        }
        {
            org.talend.core.model.metadata.builder.connection.MetadataColumn column = ConnectionFactory.eINSTANCE
                    .createMetadataColumn();
            column.setLabel("valid");
            column.setTalendType(JavaTypesManager.BOOLEAN.getId());
            column.setNullable(false);
            columns.add(column);
        }
        {
            org.talend.core.model.metadata.builder.connection.MetadataColumn column = ConnectionFactory.eINSTANCE
                    .createMetadataColumn();
            column.setLabel("columnWithLogical");
            column.setTalendType(JavaTypesManager.LONG.getId());
            column.setNullable(false);
            TaggedValue tv = TaggedValueHelper.createTaggedValue(DiSchemaConstants.TALEND6_COLUMN_LOGICAL_TYPE,
                    LogicalTypes.timeMicros().getName());
            column.getTaggedValue().add(tv);
            columns.add(column);
        }
        {
            org.talend.core.model.metadata.builder.connection.MetadataColumn column = ConnectionFactory.eINSTANCE
                    .createMetadataColumn();
            column.setLabel("dyn");
            column.setTalendType("id_Dynamic");
            column.setNullable(false);
            columns.add(column);
        }
        table.getColumns().addAll(columns);

        Schema s = MetadataToolAvroHelper.convertToAvro(table);

        assertThat(s.getType(), is(Schema.Type.RECORD));
        assertThat(s.getName(), is("testTable"));
        assertThat(s.getFields().size(), is(4));
        // assertThat(s.getObjectProps().keySet(),
        // contains(DiSchemaConstants.TALEND6_LABEL, DiSchemaConstants.TALEND6_COMMENT));
        assertThat(s.getProp(DiSchemaConstants.TALEND6_LABEL), is("testTable"));
        assertThat(s.getProp(DiSchemaConstants.TALEND6_COMMENT), is("A comment about this table."));

        Schema.Field f = s.getFields().get(0);
        assertTrue(AvroUtils.isNullable(f.schema()));
        assertThat(AvroUtils.unwrapIfNullable(f.schema()).getType(), is(Schema.Type.INT));
        assertThat(f.name(), is("id"));
        // assertThat(s.getObjectProps().keySet(),
        // contains(DiSchemaConstants.TALEND6_LABEL, DiSchemaConstants.TALEND6_COLUMN_TALEND_TYPE));
        assertThat(f.getProp(DiSchemaConstants.TALEND6_LABEL), is("id"));
        assertThat(f.getProp(DiSchemaConstants.TALEND6_COLUMN_TALEND_TYPE), is("id_Integer"));

        f = s.getFields().get(1);
        assertThat(f.schema().getType(), is(Schema.Type.STRING));
        assertThat(f.name(), is("name"));
        assertFalse(AvroUtils.isNullable(f.schema()));
        // assertThat(s.getObjectProps().keySet(),
        // contains(DiSchemaConstants.TALEND6_LABEL, DiSchemaConstants.TALEND6_COLUMN_TALEND_TYPE));
        assertThat(f.getProp(DiSchemaConstants.TALEND6_LABEL), is("name"));
        assertThat(f.getProp(DiSchemaConstants.TALEND6_COLUMN_TALEND_TYPE), is("id_String"));

        f = s.getFields().get(2);
        assertThat(f.schema().getType(), is(Schema.Type.BOOLEAN));
        assertThat(f.name(), is("valid"));
        // assertThat(s.getObjectProps().keySet(),
        // contains(DiSchemaConstants.TALEND6_LABEL, DiSchemaConstants.TALEND6_COLUMN_TALEND_TYPE));
        assertThat(f.getProp(DiSchemaConstants.TALEND6_LABEL), is("valid"));
        assertThat(f.getProp(DiSchemaConstants.TALEND6_COLUMN_TALEND_TYPE), is("id_Boolean"));

        f = s.getFields().get(3);
        assertThat(f.schema().getType(), is(Schema.Type.LONG));
        assertThat(f.name(), is("columnWithLogical"));
        assertThat(f.getProp(DiSchemaConstants.TALEND6_LABEL), is("columnWithLogical"));
        assertThat(f.getProp(DiSchemaConstants.TALEND6_COLUMN_TALEND_TYPE), is("id_Long"));
        LogicalType logicalType = LogicalTypes.fromSchemaIgnoreInvalid(AvroUtils.unwrapIfNullable(f.schema()));
        assertEquals(LogicalTypes.timeMicros(), logicalType);

        assertThat(s.getProp(SchemaConstants.INCLUDE_ALL_FIELDS), is("true"));
        assertThat(s.getProp(DiSchemaConstants.TALEND6_DYNAMIC_COLUMN_NAME), is("dyn"));
        assertThat(s.getProp(DiSchemaConstants.TALEND6_DYNAMIC_COLUMN_POSITION), is("4"));
        assertThat(s.getProp(DiSchemaConstants.TALEND6_COLUMN_TALEND_TYPE), is("id_Dynamic"));
    }

    @Test
    public void testConvertFromAvro() {
        SortedMap<String, Schema> map = new TreeMap<>();
        map.put(JavaTypesManager.STRING.getId(), AvroUtils._string());
        map.put(JavaTypesManager.LONG.getId(), AvroUtils._long());
        map.put(JavaTypesManager.INTEGER.getId(), AvroUtils._int());
        map.put(JavaTypesManager.SHORT.getId(), AvroUtils._short());
        map.put(JavaTypesManager.BYTE.getId(), AvroUtils._byte());
        map.put(JavaTypesManager.DOUBLE.getId(), AvroUtils._double());
        map.put(JavaTypesManager.FLOAT.getId(), AvroUtils._float());
        map.put(JavaTypesManager.BIGDECIMAL.getId(), AvroUtils._decimal());
        map.put(JavaTypesManager.BOOLEAN.getId(), AvroUtils._boolean());
        map.put(JavaTypesManager.BYTE_ARRAY.getId(), AvroUtils._bytes());
        map.put(JavaTypesManager.DATE.getId(), AvroUtils._date());

        RecordBuilder<Schema> builder = SchemaBuilder.builder().record("MyTable"); //$NON-NLS-1$
        FieldAssembler<Schema> fa = builder.fields();
        for (String talendType : map.keySet()) {
            FieldBuilder<Schema> fb = fa.name(talendType.replace('[', '_').replace(']', '_'));
            fb.prop(DiSchemaConstants.TALEND6_LABEL, talendType);
            fa = fb.type(map.get(talendType)).noDefault();
        }

        // add a field with logical type.
        String logicalColumnName = "columnWithLogicalType"; //$NON-NLS-1$
        FieldBuilder<Schema> fb = fa.name(logicalColumnName);
        fb.prop(DiSchemaConstants.TALEND6_LABEL, logicalColumnName);
        fa = fb.type(AvroUtils._logicalTimeMicros()).noDefault();

        Schema schema = fa.endRecord();
        MetadataTable table = MetadataToolAvroHelper.convertFromAvro(schema);
        assertEquals(map.size() + 1, table.getColumns().size());
        int i = 0;
        for (String talendType : map.keySet()) {
            assertThat(table.getColumns().get(i).getLabel(), is(talendType.replace('[', '_').replace(']', '_')));
            assertThat(table.getColumns().get(i).getTalendType(), is(talendType));
            assertThat(table.getColumns().get(i).getPattern(), is("")); //$NON-NLS-1$
            assertThat(table.getColumns().get(i).getLength(), is(-1L));
            assertThat(table.getColumns().get(i).getOriginalLength(), is(-1L));
            assertThat(table.getColumns().get(i).getPrecision(), is(-1L));
            assertThat(table.getColumns().get(i).getScale(), is(-1L));
            i++;
        }

        // Test the column with logical type
        MetadataColumn logicalColumn = table.getColumns().get(i);
        assertThat(logicalColumn.getLabel(), is(logicalColumnName));
        assertThat(logicalColumn.getTalendType(), is(JavaTypesManager.LONG.getId()));
        assertThat(logicalColumn.getPattern(), is("")); //$NON-NLS-1$
        assertThat(logicalColumn.getLength(), is(-1L));
        assertThat(logicalColumn.getOriginalLength(), is(-1L));
        assertThat(logicalColumn.getPrecision(), is(-1L));
        assertThat(logicalColumn.getScale(), is(-1L));
        boolean foundLogicalTag = false;
        for (TaggedValue tv : logicalColumn.getTaggedValue()) {
            if (DiSchemaConstants.TALEND6_COLUMN_LOGICAL_TYPE.equals(tv.getTag())) {
                foundLogicalTag = true;
                assertEquals(LogicalTypes.timeMicros().getName(), tv.getValue());
            }
        }
        assertTrue(foundLogicalTag);
    }
    
    @Test
    public void testConvertFromAvro_2() {
        String schemaObj = "{\"type\":\"record\",\"name\":\"AccountContactRole\",\"fields\":[{\"name\":\"long\",\"type\":\"string\",\"talend.field.length\":\"18\",\"talend.field.dbColumnName\":\"long\"},"
                + "{\"name\":\"A\",\"type\":\"string\",\"talend.field.length\":\"18\",\"talend.field.dbColumnName\":\"A\"},{\"name\":\"B\",\"type\":\"string\",\"talend.field.length\":\"18\",\"talend.field.dbColumnName\":\"B\"},"
                + "{\"name\":\"_234\",\"type\":\"string\",\"talend.field.length\":\"18\",\"talend.field.dbColumnName\":\"1234\"},{\"name\":\"中文\",\"type\":\"string\",\"talend.field.length\":\"18\",\"talend.field.dbColumnName\":\"中文\"},"
                + "{\"name\":\"TEST\",\"type\":\"string\",\"talend.field.length\":\"18\",\"talend.field.dbColumnName\":\"TEST\"},{\"name\":\"TEST1\",\"type\":\"string\",\"talend.field.length\":\"18\",\"talend.field.dbColumnName\":\"TEST\"}]}";
        
        MetadataTable metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
        metadataTable.setId("123456789");
        metadataTable.setName("table1");
        metadataTable.setLabel("table1");
        metadataTable.setSourceName("table1");
        Schema avroSchema = new Schema.Parser().parse((String) schemaObj);
        
        IEclipsePreferences coreUIPluginNode = new InstanceScope().getNode(ITalendCorePrefConstants.CoreUIPlugin_ID);
        coreUIPluginNode.putBoolean(IRepositoryPrefConstants.ALLOW_SPECIFIC_CHARACTERS_FOR_SCHEMA_COLUMNS, true);
        for (Schema.Field field : avroSchema.getFields()) {
            MetadataColumn metadataColumn = MetadataToolAvroHelper.convertFromAvro(field, metadataTable);
            metadataTable.getColumns().add(metadataColumn);
        }
        
        assertTrue(metadataTable.getColumns().get(0).getLabel().equals("_long"));
        assertTrue(metadataTable.getColumns().get(0).getName().equals("long"));
        Iterator<TaggedValue> ite = metadataTable.getColumns().get(0).getTaggedValue().iterator();
        while (ite.hasNext()) {
            TaggedValue t = ite.next();
            if(t.getTag().equals(DiSchemaConstants.AVRO_TECHNICAL_KEY)){
                assertTrue(t.getValue().equals("long"));
                break;
            }
        }
        
        assertTrue(metadataTable.getColumns().get(1).getLabel().equals("A"));
        assertTrue(metadataTable.getColumns().get(1).getName().equals("A"));
        ite = metadataTable.getColumns().get(1).getTaggedValue().iterator();
        while (ite.hasNext()) {
            TaggedValue t = ite.next();
            if(t.getTag().equals(DiSchemaConstants.AVRO_TECHNICAL_KEY)){
                assertTrue(t.getValue().equals("A"));
                break;
            }
        }
        
        assertTrue(metadataTable.getColumns().get(2).getLabel().equals("B"));
        assertTrue(metadataTable.getColumns().get(2).getName().equals("B"));
        ite = metadataTable.getColumns().get(2).getTaggedValue().iterator();
        while (ite.hasNext()) {
            TaggedValue t = ite.next();
            if(t.getTag().equals(DiSchemaConstants.AVRO_TECHNICAL_KEY)){
                assertTrue(t.getValue().equals("B"));
                break;
            }
        }
        
        assertTrue(metadataTable.getColumns().get(3).getLabel().equals("_234"));
        assertTrue(metadataTable.getColumns().get(3).getName().equals("1234"));
        ite = metadataTable.getColumns().get(3).getTaggedValue().iterator();
        while (ite.hasNext()) {
            TaggedValue t = ite.next();
            if(t.getTag().equals(DiSchemaConstants.AVRO_TECHNICAL_KEY)){
                assertTrue(t.getValue().equals("_234"));
                break;
            }
        }
        
        assertTrue(metadataTable.getColumns().get(4).getLabel().equals("中文"));
        assertTrue(metadataTable.getColumns().get(4).getName().equals("中文"));
        ite = metadataTable.getColumns().get(4).getTaggedValue().iterator();
        while (ite.hasNext()) {
            TaggedValue t = ite.next();
            if(t.getTag().equals(DiSchemaConstants.AVRO_TECHNICAL_KEY)){
                assertTrue(t.getValue().equals("中文"));
                break;
            }
        }
        
        assertTrue(metadataTable.getColumns().get(5).getLabel().equals("TEST"));
        assertTrue(metadataTable.getColumns().get(5).getName().equals("TEST"));
        ite = metadataTable.getColumns().get(5).getTaggedValue().iterator();
        while (ite.hasNext()) {
            TaggedValue t = ite.next();
            if(t.getTag().equals(DiSchemaConstants.AVRO_TECHNICAL_KEY)){
                assertTrue(t.getValue().equals("TEST"));
                break;
            }
        }
        
        assertTrue(metadataTable.getColumns().get(6).getLabel().equals("TEST1"));
        assertTrue(metadataTable.getColumns().get(6).getName().equals("TEST"));
        ite = metadataTable.getColumns().get(6).getTaggedValue().iterator();
        while (ite.hasNext()) {
            TaggedValue t = ite.next();
            if(t.getTag().equals(DiSchemaConstants.AVRO_TECHNICAL_KEY)){
                assertTrue(t.getValue().equals("TEST1"));
                break;
            }
        }
        
        coreUIPluginNode.putBoolean(IRepositoryPrefConstants.ALLOW_SPECIFIC_CHARACTERS_FOR_SCHEMA_COLUMNS, false);
        metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
        metadataTable.setId("123456789");
        metadataTable.setName("table1");
        metadataTable.setLabel("table1");
        metadataTable.setSourceName("table1");
        for (Schema.Field field : avroSchema.getFields()) {
            MetadataColumn metadataColumn = MetadataToolAvroHelper.convertFromAvro(field, metadataTable);
            metadataTable.getColumns().add(metadataColumn);
        }
        
        assertTrue(metadataTable.getColumns().get(4).getLabel().equals("Column4"));
        assertTrue(metadataTable.getColumns().get(4).getName().equals("中文"));
        ite = metadataTable.getColumns().get(4).getTaggedValue().iterator();
        while (ite.hasNext()) {
            TaggedValue t = ite.next();
            if(t.getTag().equals(DiSchemaConstants.AVRO_TECHNICAL_KEY)){
                assertTrue(t.getValue().equals("中文"));
                break;
            }
        }
    }
    
    @Test
    public void testConvertToAvro() {
        MetadataTable metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
        metadataTable.setId("123456789");
        metadataTable.setName("table1");
        metadataTable.setLabel("table1");
        metadataTable.setSourceName("table1");
        
        MetadataColumn creatMetadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
        creatMetadataColumn.setId("111111");
        creatMetadataColumn.setLabel("_long");
        creatMetadataColumn.setName("long");
        creatMetadataColumn.setTalendType("id_String");
        TaggedValue tv = TaggedValueHelper.createTaggedValue(DiSchemaConstants.AVRO_TECHNICAL_KEY, "long");
        creatMetadataColumn.getTaggedValue().add(tv);
        metadataTable.getColumns().add(creatMetadataColumn);
        
        
        creatMetadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
        creatMetadataColumn.setId("222222");
        creatMetadataColumn.setLabel("A");
        creatMetadataColumn.setName("A");
        tv = TaggedValueHelper.createTaggedValue(DiSchemaConstants.AVRO_TECHNICAL_KEY, "A");
        creatMetadataColumn.getTaggedValue().add(tv);
        creatMetadataColumn.setTalendType("id_String");
        metadataTable.getColumns().add(creatMetadataColumn);
        
        creatMetadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
        creatMetadataColumn.setId("333333");
        creatMetadataColumn.setLabel("B");
        creatMetadataColumn.setName("B");
        tv = TaggedValueHelper.createTaggedValue(DiSchemaConstants.AVRO_TECHNICAL_KEY, "B");
        creatMetadataColumn.getTaggedValue().add(tv);
        creatMetadataColumn.setTalendType("id_String");
        metadataTable.getColumns().add(creatMetadataColumn);
        
        creatMetadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
        creatMetadataColumn.setId("444444");
        creatMetadataColumn.setLabel("_234");
        creatMetadataColumn.setName("1234");
        tv = TaggedValueHelper.createTaggedValue(DiSchemaConstants.AVRO_TECHNICAL_KEY, "1234");
        creatMetadataColumn.getTaggedValue().add(tv);
        creatMetadataColumn.setTalendType("id_String");
        metadataTable.getColumns().add(creatMetadataColumn);
        
        creatMetadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
        creatMetadataColumn.setId("555555");
        creatMetadataColumn.setLabel("中文");
        creatMetadataColumn.setName("中文");
        tv = TaggedValueHelper.createTaggedValue(DiSchemaConstants.AVRO_TECHNICAL_KEY, "中文");
        creatMetadataColumn.getTaggedValue().add(tv);
        creatMetadataColumn.setTalendType("id_String");
        metadataTable.getColumns().add(creatMetadataColumn);
        
        creatMetadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
        creatMetadataColumn.setId("666666");
        creatMetadataColumn.setLabel(MetadataToolHelper.validateColumnName("TEST", 0));
        creatMetadataColumn.setName("TEST");
        tv = TaggedValueHelper.createTaggedValue(DiSchemaConstants.AVRO_TECHNICAL_KEY, "TEST");
        creatMetadataColumn.getTaggedValue().add(tv);
        creatMetadataColumn.setTalendType("id_String");
        metadataTable.getColumns().add(creatMetadataColumn);
        
        creatMetadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
        creatMetadataColumn.setId("777777");
        creatMetadataColumn.setLabel("TEST1");
        creatMetadataColumn.setName("TEST");
        tv = TaggedValueHelper.createTaggedValue(DiSchemaConstants.AVRO_TECHNICAL_KEY, "TEST1");
        creatMetadataColumn.getTaggedValue().add(tv);
        creatMetadataColumn.setTalendType("id_String");
        metadataTable.getColumns().add(creatMetadataColumn);
        
        IEclipsePreferences coreUIPluginNode = new InstanceScope().getNode(ITalendCorePrefConstants.CoreUIPlugin_ID);
        coreUIPluginNode.putBoolean(IRepositoryPrefConstants.ALLOW_SPECIFIC_CHARACTERS_FOR_SCHEMA_COLUMNS, true);
        org.apache.avro.Schema schema =MetadataToolAvroHelper.convertToAvro(metadataTable);
        String s = "{\"type\":\"record\",\"name\":\"table1\",\"fields\":["
                + "{\"name\":\"_long\",\"type\":[\"string\",\"null\"],\"AVRO_TECHNICAL_KEY\":\"long\",\"di.column.talendType\":\"id_String\",\"talend.field.pattern\":\"\",\"di.table.label\":\"_long\",\"talend.field.precision\":\"0\",\"di.table.comment\":\"\",\"di.column.id\":\"111111\",\"talend.field.dbColumnName\":\"long\",\"di.column.isNullable\":\"true\",\"talend.field.length\":\"0\",\"di.column.relationshipType\":\"\",\"di.column.originalLength\":\"0\",\"di.column.relatedEntity\":\"\"},"
                + "{\"name\":\"A\",\"type\":[\"string\",\"null\"],\"AVRO_TECHNICAL_KEY\":\"A\",\"di.column.talendType\":\"id_String\",\"talend.field.pattern\":\"\",\"di.table.label\":\"A\",\"talend.field.precision\":\"0\",\"di.table.comment\":\"\",\"di.column.id\":\"222222\",\"talend.field.dbColumnName\":\"A\",\"di.column.isNullable\":\"true\",\"talend.field.length\":\"0\",\"di.column.relationshipType\":\"\",\"di.column.originalLength\":\"0\",\"di.column.relatedEntity\":\"\"},"
                + "{\"name\":\"B\",\"type\":[\"string\",\"null\"],\"AVRO_TECHNICAL_KEY\":\"B\",\"di.column.talendType\":\"id_String\",\"talend.field.pattern\":\"\",\"di.table.label\":\"B\",\"talend.field.precision\":\"0\",\"di.table.comment\":\"\",\"di.column.id\":\"333333\",\"talend.field.dbColumnName\":\"B\",\"di.column.isNullable\":\"true\",\"talend.field.length\":\"0\",\"di.column.relationshipType\":\"\",\"di.column.originalLength\":\"0\",\"di.column.relatedEntity\":\"\"},"
                + "{\"name\":\"_234\",\"type\":[\"string\",\"null\"],\"AVRO_TECHNICAL_KEY\":\"1234\",\"di.column.talendType\":\"id_String\",\"talend.field.pattern\":\"\",\"di.table.label\":\"_234\",\"talend.field.precision\":\"0\",\"di.table.comment\":\"\",\"di.column.id\":\"444444\",\"talend.field.dbColumnName\":\"1234\",\"di.column.isNullable\":\"true\",\"talend.field.length\":\"0\",\"di.column.relationshipType\":\"\",\"di.column.originalLength\":\"0\",\"di.column.relatedEntity\":\"\"},"
                + "{\"name\":\"中文\",\"type\":[\"string\",\"null\"],\"AVRO_TECHNICAL_KEY\":\"中文\",\"di.column.talendType\":\"id_String\",\"talend.field.pattern\":\"\",\"di.table.label\":\"中文\",\"talend.field.precision\":\"0\",\"di.table.comment\":\"\",\"di.column.id\":\"555555\",\"talend.field.dbColumnName\":\"中文\",\"di.column.isNullable\":\"true\",\"talend.field.length\":\"0\",\"di.column.relationshipType\":\"\",\"di.column.originalLength\":\"0\",\"di.column.relatedEntity\":\"\"},"
                + "{\"name\":\"TEST\",\"type\":[\"string\",\"null\"],\"AVRO_TECHNICAL_KEY\":\"TEST\",\"di.column.talendType\":\"id_String\",\"talend.field.pattern\":\"\",\"di.table.label\":\"TEST\",\"talend.field.precision\":\"0\",\"di.table.comment\":\"\",\"di.column.id\":\"666666\",\"talend.field.dbColumnName\":\"TEST\",\"di.column.isNullable\":\"true\",\"talend.field.length\":\"0\",\"di.column.relationshipType\":\"\",\"di.column.originalLength\":\"0\",\"di.column.relatedEntity\":\"\"},"
                + "{\"name\":\"TEST1\",\"type\":[\"string\",\"null\"],\"AVRO_TECHNICAL_KEY\":\"TEST1\",\"di.column.talendType\":\"id_String\",\"talend.field.pattern\":\"\",\"di.table.label\":\"TEST1\",\"talend.field.precision\":\"0\",\"di.table.comment\":\"\",\"di.column.id\":\"777777\",\"talend.field.dbColumnName\":\"TEST\",\"di.column.isNullable\":\"true\",\"talend.field.length\":\"0\",\"di.column.relationshipType\":\"\",\"di.column.originalLength\":\"0\",\"di.column.relatedEntity\":\"\"}],"
                + "\"di.table.comment\":\"\",\"di.table.name\":\"table1\",\"di.table.label\":\"table1\"}";
        assertTrue(schema.toString().equals(s));
        
        coreUIPluginNode.putBoolean(IRepositoryPrefConstants.ALLOW_SPECIFIC_CHARACTERS_FOR_SCHEMA_COLUMNS, false);
        schema =MetadataToolAvroHelper.convertToAvro(metadataTable);
        s = "{\"type\":\"record\",\"name\":\"table1\",\"fields\":["
           + "{\"name\":\"_long\",\"type\":[\"string\",\"null\"],\"AVRO_TECHNICAL_KEY\":\"long\",\"di.column.talendType\":\"id_String\",\"talend.field.pattern\":\"\",\"di.table.label\":\"_long\",\"talend.field.precision\":\"0\",\"di.table.comment\":\"\",\"di.column.id\":\"111111\",\"talend.field.dbColumnName\":\"long\",\"di.column.isNullable\":\"true\",\"talend.field.length\":\"0\",\"di.column.relationshipType\":\"\",\"di.column.originalLength\":\"0\",\"di.column.relatedEntity\":\"\"},"
           + "{\"name\":\"A\",\"type\":[\"string\",\"null\"],\"AVRO_TECHNICAL_KEY\":\"A\",\"di.column.talendType\":\"id_String\",\"talend.field.pattern\":\"\",\"di.table.label\":\"A\",\"talend.field.precision\":\"0\",\"di.table.comment\":\"\",\"di.column.id\":\"222222\",\"talend.field.dbColumnName\":\"A\",\"di.column.isNullable\":\"true\",\"talend.field.length\":\"0\",\"di.column.relationshipType\":\"\",\"di.column.originalLength\":\"0\",\"di.column.relatedEntity\":\"\"},"
           + "{\"name\":\"B\",\"type\":[\"string\",\"null\"],\"AVRO_TECHNICAL_KEY\":\"B\",\"di.column.talendType\":\"id_String\",\"talend.field.pattern\":\"\",\"di.table.label\":\"B\",\"talend.field.precision\":\"0\",\"di.table.comment\":\"\",\"di.column.id\":\"333333\",\"talend.field.dbColumnName\":\"B\",\"di.column.isNullable\":\"true\",\"talend.field.length\":\"0\",\"di.column.relationshipType\":\"\",\"di.column.originalLength\":\"0\",\"di.column.relatedEntity\":\"\"},"
           + "{\"name\":\"_234\",\"type\":[\"string\",\"null\"],\"AVRO_TECHNICAL_KEY\":\"1234\",\"di.column.talendType\":\"id_String\",\"talend.field.pattern\":\"\",\"di.table.label\":\"_234\",\"talend.field.precision\":\"0\",\"di.table.comment\":\"\",\"di.column.id\":\"444444\",\"talend.field.dbColumnName\":\"1234\",\"di.column.isNullable\":\"true\",\"talend.field.length\":\"0\",\"di.column.relationshipType\":\"\",\"di.column.originalLength\":\"0\",\"di.column.relatedEntity\":\"\"},"
           + "{\"name\":\"中文\",\"type\":[\"string\",\"null\"],\"AVRO_TECHNICAL_KEY\":\"中文\",\"di.column.talendType\":\"id_String\",\"talend.field.pattern\":\"\",\"di.table.label\":\"中文\",\"talend.field.precision\":\"0\",\"di.table.comment\":\"\",\"di.column.id\":\"555555\",\"talend.field.dbColumnName\":\"中文\",\"di.column.isNullable\":\"true\",\"talend.field.length\":\"0\",\"di.column.relationshipType\":\"\",\"di.column.originalLength\":\"0\",\"di.column.relatedEntity\":\"\"},"
           + "{\"name\":\"TEST\",\"type\":[\"string\",\"null\"],\"AVRO_TECHNICAL_KEY\":\"TEST\",\"di.column.talendType\":\"id_String\",\"talend.field.pattern\":\"\",\"di.table.label\":\"TEST\",\"talend.field.precision\":\"0\",\"di.table.comment\":\"\",\"di.column.id\":\"666666\",\"talend.field.dbColumnName\":\"TEST\",\"di.column.isNullable\":\"true\",\"talend.field.length\":\"0\",\"di.column.relationshipType\":\"\",\"di.column.originalLength\":\"0\",\"di.column.relatedEntity\":\"\"},"
           + "{\"name\":\"TEST1\",\"type\":[\"string\",\"null\"],\"AVRO_TECHNICAL_KEY\":\"TEST1\",\"di.column.talendType\":\"id_String\",\"talend.field.pattern\":\"\",\"di.table.label\":\"TEST1\",\"talend.field.precision\":\"0\",\"di.table.comment\":\"\",\"di.column.id\":\"777777\",\"talend.field.dbColumnName\":\"TEST\",\"di.column.isNullable\":\"true\",\"talend.field.length\":\"0\",\"di.column.relationshipType\":\"\",\"di.column.originalLength\":\"0\",\"di.column.relatedEntity\":\"\"}],"
           + "\"di.table.comment\":\"\",\"di.table.name\":\"table1\",\"di.table.label\":\"table1\"}";
        assertTrue(schema.toString().equals(s));
    }
}
