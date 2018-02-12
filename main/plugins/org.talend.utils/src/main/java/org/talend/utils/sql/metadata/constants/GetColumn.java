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
package org.talend.utils.sql.metadata.constants;

/**
 * @author scorreia
 * 
 * Constants describing the columns' name of the result set got from
 * {@link java.sql.DatabaseMetaData#getColumns(String, String, String, String)}
 */
public enum GetColumn {
    TABLE_CAT,
    TABLE_SCHEM,
    TABLE_NAME,
    COLUMN_NAME,
    DATA_TYPE,
    TYPE_NAME,
    COLUMN_SIZE,
    DECIMAL_DIGITS,
    NUM_PREC_RADIX,
    NULLABLE,
    REMARKS,
    COLUMN_DEF,
    SQL_DATA_TYPE,
    SQL_DATETIME_SUB,
    CHAR_OCTET_LENGTH,
    ORDINAL_POSITION,
    IS_NULLABLE,
    SCOPE_CATLOG,
    SCOPE_SCHEMA,
    SCOPE_TABLE,
    SOURCE_DATA_TYPE;
}
