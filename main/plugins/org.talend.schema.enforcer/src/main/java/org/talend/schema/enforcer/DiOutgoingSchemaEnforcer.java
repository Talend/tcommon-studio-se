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
package org.talend.schema.enforcer;

import java.util.*;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Field;
import org.apache.avro.generic.IndexedRecord;
import org.talend.daikon.avro.AvroUtils;
import org.talend.daikon.avro.SchemaConstants;
import org.talend.daikon.avro.converter.IndexedRecordConverter.UnmodifiableAdapterException;
import org.talend.daikon.avro.converter.SingleColumnIndexedRecordConverter;

/**
 * This class acts as a wrapper around an arbitrary Avro {@link IndexedRecord} to coerce the output type to the exact
 * Java objects expected by the Talend 6 Studio (which will copy the fields into a POJO in generated code).
 * <p>
 * A wrapper like this should be attached to an input component, for example, to ensure that its outgoing data meets the
 * Schema constraints imposed by the Studio, including:
 * <ul>
 * <li>Coercing the types of the returned objects to *exactly* the type required by the Talend POJO.</li>
 * <li>Placing all of the unresolved columns between the wrapped schema and the output schema in the Dynamic column.
 * </li>
 * </ul>
 * <p>
 * One instance of this object can be created per outgoing schema and reused via the {@link #setWrapped(IndexedRecord)}
 * method.
 */
public class DiOutgoingSchemaEnforcer implements IndexedRecord {

    /**
     * True if columns from the incoming schema are matched to the outgoing schema exclusively by position.
     */
    private boolean byIndex;

    /**
     * The outgoing schema that determines which Java objects are produced.
     */
    private final Schema outgoing;

    /**
     * The incoming IndexedRecord currently wrapped by this enforcer. This can be swapped out for new data as long as
     * they keep the same schema.
     */
    private IndexedRecord wrapped;

    /**
     * The position of the dynamic column in the outgoing schema. This is -1 if there is no dynamic column. There can be
     * a maximum of one dynamic column in the schema.
     */
    private final int outgoingDynamicColumn;

    /**
     * The {@link Schema} of dynamic column, it will be calculated only once and be used for initial
     * routines.system.DynamicMetadata
     */
    private Schema outgoingDynamicRuntimeSchema;

    /**
     * The name and position of fields in the wrapped record that need to be put into the dynamic column of the output
     * record.
     */
    private Map<String, Integer> dynamicColumnSources;

    public DiOutgoingSchemaEnforcer(Schema outgoing, boolean byIndex) {
        this.outgoing = outgoing;
        this.byIndex = byIndex;

        // Find the dynamic column, if any.
        outgoingDynamicColumn = AvroUtils.isIncludeAllFields(outgoing)
                ? Integer.valueOf(outgoing.getProp(DiSchemaConstants.TALEND6_DYNAMIC_COLUMN_POSITION)) : -1;
    }

    /**
     * @param wrapped The internal, actual data represented as an IndexedRecord.
     */
    public void setWrapped(IndexedRecord wrapped) {
        // TODO: This matches the salesforce and file-input single output components. Is this sufficient logic
        // for all components?
        if (wrapped instanceof SingleColumnIndexedRecordConverter.PrimitiveAsIndexedRecordAdapter) {
            byIndex = true;
        }
        this.wrapped = wrapped;
        if (outgoingDynamicRuntimeSchema == null && outgoingDynamicColumn != -1) {
            List<Schema.Field> copyFieldList;
            if (byIndex) {
                copyFieldList = getDynamicSchemaByIndex();
            } else {
                copyFieldList = getDynamicSchemaByName();
            }
            outgoingDynamicRuntimeSchema = Schema.createRecord("dynamic", null, null, false);
            outgoingDynamicRuntimeSchema.setFields(copyFieldList);
        }
    }

    @Override
    public Schema getSchema() {
        return outgoing;
    }

    public Schema getOutgoingDynamicRuntimeSchema() {
        return outgoingDynamicRuntimeSchema;
    }

    @Override
    public void put(int i, Object v) {
        throw new UnmodifiableAdapterException();
    }

    @Override
    public Object get(int i) {
        if (outgoingDynamicColumn != -1) {
            // If we are asking for the dynamic column, then all of the fields that don't match the outgoing schema are
            // added to a map.
            if (i == outgoingDynamicColumn) {
                if (byIndex) {
                    return getDynamicMapByIndex();
                } else {
                    return getDynamicMapByName();
                }
            }

            if (i > outgoingDynamicColumn) {
                i--;
            }
        }

        // We should never ask for an index outside of the outgoing schema.
        if (i >= outgoing.getFields().size()) {
            throw new ArrayIndexOutOfBoundsException(i);
        }

        Field outField = getSchema().getFields().get(i);
        Field wrappedField;

        // If we are not asking for the dynamic column, then get the input field that corresponds to the position.
        int wrappedIndex;
        if (byIndex) {
            if (outgoingDynamicColumn != -1 && i >= outgoingDynamicColumn) {
                // If the requested index is after the dynamic column and we are matching by index, then the actual
                // index should be counted from the end of the fields.
                wrappedIndex = getNumberOfDynamicColumns() + i;
            } else {
                wrappedIndex = i;
            }
            // If the wrappedIndex is out of bounds, then return the default value.
            if (wrappedIndex >= wrapped.getSchema().getFields().size()) {
                return transformValue(null, null, outField);
            }
            wrappedField = wrapped.getSchema().getFields().get(wrappedIndex);
        } else {
            // Matching fields by name.
            wrappedField = wrapped.getSchema().getField(outField.name());
            if (wrappedField == null) {
                return transformValue(null, null, outField);
            }
            wrappedIndex = wrappedField.pos();
        }

        Object value = wrapped.get(wrappedIndex);
        return transformValue(value, wrappedField, outField);
    }

    /**
     * @param value The incoming value for the field. This can be null when null is a valid value, or if there is no
     * corresponding wrapped field.
     * @param wrappedField The incoming field description (a valid Avro Schema). This can be null if there is no
     * corresponding wrapped field.
     * @param outField The outgoing field description that must be enforced. This must not be null.
     */
    private Object transformValue(Object value, Field wrappedField, Field outField) {
        String talendType = outField.getProp(DiSchemaConstants.TALEND6_COLUMN_TALEND_TYPE);
        String javaClass = AvroUtils.unwrapIfNullable(outField.schema()).getProp(SchemaConstants.JAVA_CLASS_FLAG);

        if (null == value) {
            return null;
        }

        // TODO(rskraba): A full list of type conversion to coerce to Talend-compatible types.
        if ("id_Short".equals(talendType)) { //$NON-NLS-1$
            return value instanceof Number ? ((Number) value).shortValue() : Short.parseShort(String.valueOf(value));
        } else if ("id_Date".equals(talendType) || "java.util.Date".equals(javaClass)) { //$NON-NLS-1$
            return value instanceof Date ? value : new Date((Long) value);
        } else if ("id_Byte".equals(talendType)) { //$NON-NLS-1$
            return value instanceof Number ? ((Number) value).byteValue() : Byte.parseByte(String.valueOf(value));
        }
        return value;
    }

    /**
     * @return the number of columns that will be placed in the dynamic holder.
     */
    private int getNumberOfDynamicColumns() {
        int dynColN = wrapped.getSchema().getFields().size() - getSchema().getFields().size();
        if (dynColN < 0) {
            throw new UnsupportedOperationException(
                    "The incoming data does not have sufficient columns to create a dynamic column."); //$NON-NLS-1$
        }
        return dynColN;
    }

    /**
     * @return A map of all of the unresolved columns, when the unresolved columns are determined by the position of the
     * Dynamic column in enforced schema.
     */
    private Map<String, Object> getDynamicMapByIndex() {
        int dynColN = getNumberOfDynamicColumns();
        Map<String, Object> result = new HashMap<>();
        for (int j = 0; j < dynColN; j++) {
            result.put(wrapped.getSchema().getFields().get(outgoingDynamicColumn + j).name(),
                    wrapped.get(outgoingDynamicColumn + j));
        }
        return result;
    }

    /**
     * @return A list of all of the unresolved columns's schema, when the unresolved columns are determined by the
     * position of the Dynamic column in enforced schema.
     */
    private List<Schema.Field> getDynamicSchemaByIndex() {
        List<Schema.Field> fields = new ArrayList<>();
        int dynColN = getNumberOfDynamicColumns();
        for (int j = 0; j < dynColN; j++) {
            Schema.Field se = wrapped.getSchema().getFields().get(outgoingDynamicColumn + j);
            Schema.Field field = new Schema.Field(se.name(), se.schema(), se.doc(), se.defaultVal());
            Map<String, Object> fieldProperties = se.getObjectProps();
            for (String propName : fieldProperties.keySet()) {
                Object propValue = fieldProperties.get(propName);
                if (propValue != null) {
                    field.addProp(propName, propValue);
                }
            }
            fields.add(field);
        }
        return fields;
    }

    /**
     * @return A map of all of the unresolved columns, when the unresolved columns are determined by the names of the
     * resolved column in enforced schema.
     */
    private Map<String, Object> getDynamicMapByName() {
        // Lazy initialization of source position by name.
        if (dynamicColumnSources == null) {
            dynamicColumnSources = new HashMap<>();
            for (Schema.Field wrappedField : wrapped.getSchema().getFields()) {
                Schema.Field outField = getSchema().getField(wrappedField.name());
                if (outField == null) {
                    dynamicColumnSources.put(wrappedField.name(), wrappedField.pos());
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, Integer> e : dynamicColumnSources.entrySet()) {
            result.put(e.getKey(), wrapped.get(e.getValue()));
        }
        return result;
    }

    /**
     * @return A list of all of the unresolved columns's schema, when the unresolved columns are determined by the names
     * of the resolved column in enforced schema.
     */
    private List<Schema.Field> getDynamicSchemaByName() {
        List<Schema.Field> fields = new ArrayList<>();
        List<String> designColumnsName = new ArrayList<>();
        for (Schema.Field se : outgoing.getFields()) {
            designColumnsName.add(se.name());
        }
        Schema runtimeSchema = wrapped.getSchema();
        for (Schema.Field se : runtimeSchema.getFields()) {
            if (designColumnsName.contains(se.name())) {
                continue;
            }
            Schema.Field field = new Schema.Field(se.name(), se.schema(), se.doc(), se.defaultVal());
            Map<String, Object> fieldProperties = se.getObjectProps();
            for (String propName : fieldProperties.keySet()) {
                Object propValue = fieldProperties.get(propName);
                if (propValue != null) {
                    field.addProp(propName, propValue);
                }
            }
            fields.add(field);
        }
        return fields;
    }

}