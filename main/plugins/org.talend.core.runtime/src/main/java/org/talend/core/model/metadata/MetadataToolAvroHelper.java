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

import java.util.ArrayList;
import java.util.List;

import org.apache.avro.LogicalTypes;
import org.apache.avro.LogicalTypes.Decimal;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.SchemaBuilder.BaseFieldTypeBuilder;
import org.apache.avro.SchemaBuilder.FieldAssembler;
import org.apache.avro.SchemaBuilder.FieldBuilder;
import org.apache.avro.SchemaBuilder.PropBuilder;
import org.apache.avro.SchemaBuilder.RecordBuilder;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.daikon.avro.DynamicTypeFactory;
import org.talend.daikon.avro.SchemaConstants;
import org.talend.daikon.avro.util.AvroUtils;
import org.talend.daikon.talend6.Talend6SchemaConstants;
import orgomg.cwm.objectmodel.core.Expression;
import orgomg.cwm.objectmodel.core.TaggedValue;

/**
 * Utility classes for conversion between a {@link MetadataTable} and an Avro {@link Schema}.
 */
public final class MetadataToolAvroHelper {

    /**
     * @return An Avro schema with enriched properties from the incoming metadata table.
     */
    public static org.apache.avro.Schema convertToAvro(MetadataTable in) {
        RecordBuilder<Schema> builder = SchemaBuilder.builder().record(in.getLabel());
        copyTableProperties(builder, in);

        FieldAssembler<Schema> fa = builder.fields();
        for (org.talend.core.model.metadata.builder.connection.MetadataColumn column : in.getColumns()) {
            fa = convertToAvro(fa, column);
        }
        return fa.endRecord();
    }

    /**
     * Copy all of the information from the MetadataTable in the form of key/value properties into an Avro object.
     * 
     * @param builder Any Avro builder capable of taking key/value in the form of strings.
     * @param in The element to copy information from.
     * @return the instance of the builder passed in.
     */
    private static <T extends PropBuilder<T>> PropBuilder<T> copyTableProperties(PropBuilder<T> builder, MetadataTable in) {

        // Properties common to tables and columns.
        if (in.getId() != null) {
            builder.prop(Talend6SchemaConstants.TALEND6_ID, in.getId());
        }
        if (in.getComment() != null) {
            builder.prop(Talend6SchemaConstants.TALEND6_COMMENT, in.getComment());
        }
        if (in.getLabel() != null) {
            builder.prop(Talend6SchemaConstants.TALEND6_LABEL, in.getLabel());
        }
        if (in.isReadOnly()) {
            builder.prop(Talend6SchemaConstants.TALEND6_IS_READ_ONLY, "true"); //$NON-NLS-1$
        }
        for (TaggedValue tv : in.getTaggedValue()) {
            String additionalTag = tv.getTag();
            if (tv.getValue() != null) {
                builder.prop(Talend6SchemaConstants.TALEND6_ADDITIONAL_PROPERTIES + additionalTag, tv.getValue());
            }
        }

        // Table-specific properties.
        if (in.getName() != null) {
            builder.prop(Talend6SchemaConstants.TALEND6_TABLE_NAME, in.getName());
        }
        if (in.getTableType() != null) {
            builder.prop(Talend6SchemaConstants.TALEND6_TABLE_TYPE, in.getTableType());
        }

        return builder;
    }

    /**
     * Build a field into a schema using enriched properties from the incoming column.
     */
    private static FieldAssembler<Schema> convertToAvro(FieldAssembler<Schema> fa,
            org.talend.core.model.metadata.builder.connection.MetadataColumn in) {
        FieldBuilder<Schema> fb = fa.name(in.getLabel());
        copyColumnProperties(fb, in);
        BaseFieldTypeBuilder<Schema> ftb = in.isNullable() ? fb.type() : fb.type().nullable();

        String defaultValue = null;
        Expression initialValue = in.getInitialValue();
        if (initialValue != null) {
            defaultValue = initialValue.getBody();
        }

        String tt = in.getTalendType();

        // Numeric types.
        if (JavaTypesManager.LONG.getId().equals(tt)) {
            return defaultValue == null //
            ? ftb.longType().noDefault()
                    : ftb.longType().longDefault(Long.parseLong(defaultValue));
        }
        if (JavaTypesManager.INTEGER.getId().equals(tt) || JavaTypesManager.SHORT.getId().equals(tt)
                || JavaTypesManager.BYTE.getId().equals(tt)) {
            return defaultValue == null //
            ? ftb.intType().noDefault()
                    : ftb.intType().intDefault(Integer.parseInt(defaultValue));
        }
        if (JavaTypesManager.DOUBLE.getId().equals(tt)) {
            return defaultValue == null //
            ? ftb.doubleType().noDefault()
                    : ftb.doubleType().doubleDefault(Double.parseDouble(defaultValue));
        }
        if (JavaTypesManager.FLOAT.getId().equals(tt)) {
            return defaultValue == null //
            ? ftb.floatType().noDefault()
                    : ftb.floatType().floatDefault(Float.parseFloat(defaultValue));
        }
        if (JavaTypesManager.BIGDECIMAL.getId().equals(tt)) {
            // decimal(precision, scale) == column length and precision?
            Decimal d = LogicalTypes.decimal((int) in.getLength(), (int) in.getPrecision());
            Schema bigdecimal = d.addToSchema(Schema.create(Schema.Type.BYTES));
            return defaultValue == null ? fb.type(bigdecimal).noDefault() : fb.type(bigdecimal).withDefault(defaultValue);
        }

        // Other primitive types that map directly to Avro.
        if (JavaTypesManager.BOOLEAN.getId().equals(tt)) {
            return defaultValue == null //
            ? ftb.booleanType().noDefault()
                    : ftb.booleanType().booleanDefault(Boolean.parseBoolean(defaultValue));
        }
        if (JavaTypesManager.BYTE_ARRAY.getId().equals(tt)) {
            return defaultValue == null //
            ? ftb.bytesType().noDefault()
                    : ftb.bytesType().bytesDefault(defaultValue);
        }
        if (JavaTypesManager.DATE.getId().equals(tt)) {
            // Date is saved as a long, like System.currentTimeMillis()
            return ftb.longType().noDefault();
        }
        // String-ish types.
        if (JavaTypesManager.STRING.getId().equals(tt) || JavaTypesManager.FILE.getId().equals(tt)
                || JavaTypesManager.DIRECTORY.getId().equals(tt) || JavaTypesManager.VALUE_LIST.getId().equals(tt)
                || JavaTypesManager.CHARACTER.getId().equals(tt) || JavaTypesManager.PASSWORD.getId().equals(tt)) {
            return defaultValue == null //
            ? ftb.stringType().noDefault()
                    : ftb.stringType().stringDefault(defaultValue);
        }

        // Types with unknown elements, store as binary
        if (JavaTypesManager.OBJECT.getId().equals(tt)) {
            return defaultValue == null //
            ? ftb.bytesType().noDefault()
                    : ftb.bytesType().bytesDefault(defaultValue);
        }

        if ("id_Dynamic".equals(tt)) { //$NON-NLS-1$
            return defaultValue == null ? fb.type(DynamicTypeFactory.getDynamic()).noDefault() : fb.type(
                    DynamicTypeFactory.getDynamic()).withDefault(defaultValue);
        }

        if (JavaTypesManager.LIST.getId().equals(tt)) {
            return ftb.array().items().bytesType().noDefault();
        }

        // Can this occur?
        throw new UnsupportedOperationException("Unrecognized type " + tt); //$NON-NLS-1$
    }

    /**
     * Copy all of the information from the IMetadataColumn in the form of key/value properties into an Avro object.
     * 
     * @param builder Any Avro builder capable of taking key/value in the form of strings.
     * @param in The element to copy information from.
     * @return the instance of the builder passed in.
     */
    private static <T extends PropBuilder<T>> PropBuilder<T> copyColumnProperties(PropBuilder<T> builder,
            org.talend.core.model.metadata.builder.connection.MetadataColumn in) {
        // Properties common to tables and columns.
        if (in.getId() != null) {
            builder.prop(Talend6SchemaConstants.TALEND6_ID, in.getId());
        }
        if (in.getComment() != null) {
            builder.prop(Talend6SchemaConstants.TALEND6_COMMENT, in.getComment());
        }
        if (in.getLabel() != null) {
            builder.prop(Talend6SchemaConstants.TALEND6_LABEL, in.getLabel());
        }
        if (in.isReadOnly()) {
            builder.prop(Talend6SchemaConstants.TALEND6_IS_READ_ONLY, "true"); //$NON-NLS-1$
        }
        for (TaggedValue tv : in.getTaggedValue()) {
            String additionalTag = tv.getTag();
            if (tv.getValue() != null) {
                builder.prop(Talend6SchemaConstants.TALEND6_ADDITIONAL_PROPERTIES + additionalTag, tv.getValue());
            }
        }

        // Column-specific properties.
        if (in.isKey()) {
            builder.prop(Talend6SchemaConstants.TALEND6_COLUMN_IS_KEY, "true"); //$NON-NLS-1$
        }
        if (in.getType() != null) {
            builder.prop(Talend6SchemaConstants.TALEND6_COLUMN_SOURCE_TYPE, in.getSourceType());
        }
        if (in.getTalendType() != null) {
            builder.prop(Talend6SchemaConstants.TALEND6_COLUMN_TALEND_TYPE, in.getTalendType());
        }
        if (in.getPattern() != null) {
            builder.prop(Talend6SchemaConstants.TALEND6_COLUMN_PATTERN, TalendQuoteUtils.addQuotesIfNotExist(in.getPattern()));
        }
        if (in.getLength() >= 0) {
            builder.prop(Talend6SchemaConstants.TALEND6_COLUMN_LENGTH, String.valueOf((int) in.getLength()));
        }
        if (in.getOriginalLength() >= 0) {
            builder.prop(Talend6SchemaConstants.TALEND6_COLUMN_ORIGINAL_LENGTH, String.valueOf(in.getOriginalLength()));
        }
        if (in.isNullable()) {
            builder.prop(Talend6SchemaConstants.TALEND6_COLUMN_IS_NULLABLE, "true"); //$NON-NLS-1$
        }
        if (in.getPrecision() >= 0) {
            builder.prop(Talend6SchemaConstants.TALEND6_COLUMN_PRECISION, String.valueOf(in.getPrecision()));
        }
        if (in.getInitialValue() != null && in.getInitialValue().getBody() != null) {
            builder.prop(Talend6SchemaConstants.TALEND6_COLUMN_DEFAULT, in.getInitialValue().getBody());
        }
        if (in.getName() != null) {
            // keyword fixes?
            builder.prop(Talend6SchemaConstants.TALEND6_COLUMN_ORIGINAL_DB_COLUMN_NAME, in.getName());
        }
        if (in.getRelatedEntity() != null) {
            builder.prop(Talend6SchemaConstants.TALEND6_COLUMN_RELATED_ENTITY, in.getRelatedEntity());
        }
        if (in.getRelationshipType() != null) {
            builder.prop(Talend6SchemaConstants.TALEND6_COLUMN_RELATIONSHIP_TYPE, in.getRelationshipType());
        }
        return builder;
    }

    /**
     * @param in A schema which must be of type {@link Schema.Type#RECORD}.
     * @return A MetadataTable containing all the information from the Schema, including any information included the
     * schema as JSON property annotations for Talend 6 generated schemas.
     */
    public static MetadataTable convertFromAvro(org.apache.avro.Schema in) {
        MetadataTable table = ConnectionFactory.eINSTANCE.createMetadataTable();

        // Set the defaults values to the name (the only information guaranteed to be present in the schema.)
        table.setId(in.getName());
        table.setLabel(in.getName());
        table.setName(in.getName());
        table.setSourceName(in.getName());

        // Extract any properties that have been saved in the enriched schema.

        // Properties common to tables and columns.
        String prop;
        if (null != (prop = in.getProp(Talend6SchemaConstants.TALEND6_ID))) {
            table.setId(prop);
        }
        if (null != (prop = in.getProp(Talend6SchemaConstants.TALEND6_COMMENT))) {
            table.setComment(in.getProp(Talend6SchemaConstants.TALEND6_ID));
        }
        if (null != (prop = in.getProp(Talend6SchemaConstants.TALEND6_LABEL))) {
            table.setLabel(null);
        }
        if (null != (prop = in.getProp(Talend6SchemaConstants.TALEND6_IS_READ_ONLY))) {
            table.setReadOnly(Boolean.parseBoolean(prop));
        }
        for (String key : in.getJsonProps().keySet()) {
            if (key.startsWith(Talend6SchemaConstants.TALEND6_ADDITIONAL_PROPERTIES)) {
                String originalKey = key.substring(Talend6SchemaConstants.TALEND6_ADDITIONAL_PROPERTIES.length());
                TaggedValue tv = TaggedValueHelper.createTaggedValue(originalKey, in.getProp(key));
                table.getTaggedValue().add(tv);
            }
        }

        // Table-specific properties.
        if (null != (prop = in.getProp(Talend6SchemaConstants.TALEND6_TABLE_NAME))) {
            table.setName(prop);
        }
        if (null != (prop = in.getProp(Talend6SchemaConstants.TALEND6_TABLE_TYPE))) {
            table.setTableType(prop);
        }

        // Add the columns.
        List<org.talend.core.model.metadata.builder.connection.MetadataColumn> columns = new ArrayList<>(in.getFields().size());
        for (Schema.Field f : in.getFields()) {
            columns.add(convertFromAvro(f));
        }
        table.getColumns().addAll(columns);
        return table;
    }

    /**
     * @param in A field from an incoming schema
     * @return A MetadataColumn containing all the information from the Schema, including any information included the
     * schema as JSON property annotations for Talend 6 generated schemas.
     */
    public static org.talend.core.model.metadata.builder.connection.MetadataColumn convertFromAvro(Schema.Field field) {
        org.talend.core.model.metadata.builder.connection.MetadataColumn col = ConnectionFactory.eINSTANCE.createMetadataColumn();
        Schema in = field.schema();

        // Set the defaults values to the name (the only information guaranteed to be available in every field).
        col.setId(field.name());
        col.setLabel(field.name());
        col.setName(field.name());
        Schema nonnullable = AvroUtils.unwrapIfNullable(in);
        if (DynamicTypeFactory.isDynamic(nonnullable)) {
            col.setTalendType("id_Dynamic"); //$NON-NLS-1$
        } else {
            switch (nonnullable.getType()) {
            case ARRAY:
                col.setTalendType(JavaTypesManager.LIST.getId());
                break;
            case BOOLEAN:
                col.setTalendType(JavaTypesManager.BOOLEAN.getId());
                break;
            case BYTES:
            case FIXED:
                col.setTalendType(JavaTypesManager.BYTE_ARRAY.getId());
                break;
            case DOUBLE:
                col.setTalendType(JavaTypesManager.DOUBLE.getId());
                break;
            case FLOAT:
                col.setTalendType(JavaTypesManager.FLOAT.getId());
                break;
            case INT:
                col.setTalendType(JavaTypesManager.INTEGER.getId());
                break;
            case LONG:
                String prop = null;
                if (null != (prop = nonnullable.getProp(SchemaConstants.TALEND_COLUMN_PATTERN))) {
                    col.setTalendType(JavaTypesManager.DATE.getId());
                    col.setPattern(TalendQuoteUtils.addQuotesIfNotExist(prop));
                } else {
                    col.setTalendType(JavaTypesManager.LONG.getId());
                }
                break;
            case ENUM:
            case STRING:
                col.setTalendType(JavaTypesManager.STRING.getId());
                break;
            case RECORD:
            case NULL:
            case MAP:
            case UNION:
            default:
                // Can this occur in the studio?
                break;
            }
        }
        // TODO setSourceType from the field Schema type.
        col.setNullable(AvroUtils.isNullable(in));

        // Extract any properties that have been saved in the enriched schema.

        // Properties common to tables and columns.
        String prop;
        if (null != (prop = in.getProp(Talend6SchemaConstants.TALEND6_ID))) {
            col.setId(prop);
        }
        if (null != (prop = in.getProp(Talend6SchemaConstants.TALEND6_COMMENT))) {
            col.setComment(in.getProp(Talend6SchemaConstants.TALEND6_ID));
        }
        if (null != (prop = in.getProp(Talend6SchemaConstants.TALEND6_LABEL))) {
            col.setLabel(null);
        }
        if (null != (prop = in.getProp(Talend6SchemaConstants.TALEND6_IS_READ_ONLY))) {
            col.setReadOnly(Boolean.parseBoolean(prop));
        }
        for (String key : in.getJsonProps().keySet()) {
            if (key.startsWith(Talend6SchemaConstants.TALEND6_ADDITIONAL_PROPERTIES)) {
                String originalKey = key.substring(Talend6SchemaConstants.TALEND6_ADDITIONAL_PROPERTIES.length());
                TaggedValue tv = TaggedValueHelper.createTaggedValue(originalKey, in.getProp(key));
                col.getTaggedValue().add(tv);
            }
        }

        // Column-specific properties.
        if (null != (prop = in.getProp(Talend6SchemaConstants.TALEND6_COLUMN_IS_KEY))) {
            col.setKey(Boolean.parseBoolean(prop));
        }
        if (null != (prop = in.getProp(Talend6SchemaConstants.TALEND6_COLUMN_SOURCE_TYPE))) {
            col.setSourceType(prop);
        }
        if (null != (prop = in.getProp(Talend6SchemaConstants.TALEND6_COLUMN_TALEND_TYPE))) {
            col.setTalendType(prop);
        }
        if (null != (prop = in.getProp(Talend6SchemaConstants.TALEND6_COLUMN_PATTERN))) {
            col.setPattern(TalendQuoteUtils.addQuotesIfNotExist(prop));
        }
        if (null != (prop = in.getProp(Talend6SchemaConstants.TALEND6_COLUMN_LENGTH))) {
            Long value = Long.parseLong(prop);
            col.setLength(value > 0 ? value : -1);
        }
        if (null != (prop = in.getProp(Talend6SchemaConstants.TALEND6_COLUMN_ORIGINAL_LENGTH))) {
            Long value = Long.parseLong(prop);
            col.setOriginalLength(value > 0 ? value : -1);
        }
        if (null != (prop = in.getProp(Talend6SchemaConstants.TALEND6_COLUMN_IS_NULLABLE))) {
            col.setNullable(Boolean.parseBoolean(prop));
        }
        if (null != (prop = in.getProp(Talend6SchemaConstants.TALEND6_COLUMN_PRECISION))) {
            Long value = Long.parseLong(prop);
            col.setPrecision(value > 0 ? value : -1);
        }
        if (null != (prop = in.getProp(Talend6SchemaConstants.TALEND6_COLUMN_DEFAULT))) {
            col.setDefaultValue(prop);
        }
        if (null != (prop = in.getProp(Talend6SchemaConstants.TALEND6_COLUMN_ORIGINAL_DB_COLUMN_NAME))) {
            col.setName(prop);
        }
        if (null != (prop = in.getProp(Talend6SchemaConstants.TALEND6_COLUMN_RELATED_ENTITY))) {
            col.setRelatedEntity(prop);
        }
        if (null != (prop = in.getProp(Talend6SchemaConstants.TALEND6_COLUMN_RELATIONSHIP_TYPE))) {
            col.setRelationshipType(prop);
        }

        // If the source type wasn't set, there is an issue. Can this occur in the studio.
        if (col.getTalendType() == null) {
            throw new UnsupportedOperationException("Unrecognized type " + in); //$NON-NLS-1$
        }

        return col;
    }
}
