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
package org.talend.core.model.metadata;

import org.apache.avro.Schema;
import org.talend.daikon.avro.SchemaConstants;

/**
 * Constants that can be used as keys in an Avro {@link Schema} properties in order to remain feature-equivalent to the
 * existing Talend 6 IMetadataTable.
 *
 * Values that are null are always omitted from the properties. The type of the value is always String.
 */
public interface DiSchemaConstants {

    /*
     * Can apply to table and columns. --------------------------------------
     */

    public final static String TALEND6_ID = "di.column.id"; //$NON-NLS-1$

    public final static String TALEND6_COMMENT = "di.table.comment"; //$NON-NLS-1$

    public final static String TALEND6_LABEL = "di.table.label"; //$NON-NLS-1$

    /** Property is present if readonly, otherwise not present. */
    public final static String TALEND6_IS_READ_ONLY = SchemaConstants.TALEND_IS_LOCKED; 

    /** The key will have this as a prefix, pointing to the value. */
    public final static String TALEND6_ADDITIONAL_PROPERTIES = "di.prop."; //$NON-NLS-1$

    /*
     * Table custom properties. ---------------------------------------------
     */

    public final static String TALEND6_TABLE_NAME = "di.table.name"; //$NON-NLS-1$

    public final static String TALEND6_TABLE_TYPE = "di.table.type"; //$NON-NLS-1$

    /*
     * Column custom properties. --------------------------------------------
     */

    /** Property is present if key, otherwise not present. */
    public final static String TALEND6_COLUMN_IS_KEY = SchemaConstants.TALEND_COLUMN_IS_KEY; 

    public final static String TALEND6_COLUMN_SOURCE_TYPE = SchemaConstants.TALEND_COLUMN_DB_TYPE;

    public final static String TALEND6_COLUMN_TALEND_TYPE = "di.column.talendType"; //$NON-NLS-1$

    public final static String TALEND6_COLUMN_PATTERN = SchemaConstants.TALEND_COLUMN_PATTERN;

    /** String representation of an int. */
    public final static String TALEND6_COLUMN_LENGTH = SchemaConstants.TALEND_COLUMN_DB_LENGTH;

    /** String representation of an int. */
    public final static String TALEND6_COLUMN_ORIGINAL_LENGTH = "di.column.originalLength"; //$NON-NLS-1$

    /** Property is present if nullable, otherwise not present. */
    public final static String TALEND6_COLUMN_IS_NULLABLE = "di.column.isNullable"; //$NON-NLS-1$

    /** String representation of an int. */
    public final static String TALEND6_COLUMN_PRECISION = SchemaConstants.TALEND_COLUMN_PRECISION;

    /** String representation of an int. */
    public final static String TALEND6_COLUMN_SCALE = SchemaConstants.TALEND_COLUMN_SCALE;

    public final static String TALEND6_COLUMN_DEFAULT = SchemaConstants.TALEND_COLUMN_DEFAULT;

    /** cf TDKN-36. to link to the custom fields of the studio */
    public final static String TALEND6_COLUMN_CUSTOM = SchemaConstants.TALEND_FIELD_GENERATED;

    public final static String TALEND6_COLUMN_ORIGINAL_DB_COLUMN_NAME = SchemaConstants.TALEND_COLUMN_DB_COLUMN_NAME;

    public final static String TALEND6_COLUMN_RELATED_ENTITY = "di.column.relatedEntity"; //$NON-NLS-1$

    public final static String TALEND6_COLUMN_RELATIONSHIP_TYPE = "di.column.relationshipType"; //$NON-NLS-1$

    public final static String TALEND6_DYNAMIC_COLUMN_POSITION = "di.dynamic.column.position"; //$NON-NLS-1$

    public final static String TALEND6_DYNAMIC_COLUMN_NAME = "di.dynamic.column.name"; //$NON-NLS-1$

    public final static String TALEND6_DYNAMIC_COLUMN_ID = "di.dynamic.column.id"; //$NON-NLS-1$

    public final static String TALEND6_DYNAMIC_COLUMN_COMMENT = "di.dynamic.column.comment"; //$NON-NLS-1$

    /** Property is present if readonly, otherwise not present. */
    public final static String TALEND6_DYNAMIC_IS_READ_ONLY = "di.dynamic.readOnly"; //$NON-NLS-1$

    /** The key will have this as a prefix, pointing to the value. */
    public final static String TALEND6_DYNAMIC_ADDITIONAL_PROPERTIES = "di.dynamic.prop."; //$NON-NLS-1$

    /** Original Avro logical type date */
    public final static String TALEND6_COLUMN_DATE_DATE = "di.date.date";

    /** Original Avro logical type timestamp */
    public final static String TALEND6_COLUMN_DATE_TIMESTAMP = "di.date.timestamp";

    /** Original Avro date, but no logical type */
    // FIXME - this one should go away once we move the all of the components to use the Avro logical types
    public final static String TALEND6_COLUMN_DATE_NO_LOGICAL_TYPE = "di.date.noLogicalType";

    public final static String TALEND6_COLUMN_LOGICAL_TYPE = "di.column.logicalType"; //$NON-NLS-1$

}
