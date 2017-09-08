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
package org.talend.utils.sql.metadata.constants;

/**
 * @author scorreia
 * 
 * Constants describing the columns' name of the result set got from
 * {@link java.sql.DatabaseMetaData#getTables(String, String, String, String[])}
 */
public enum GetTable {
    TABLE_CAT,
    TABLE_SCHEM,
    // for cdh4 hive2
    TABLE_SCHEMA,
    TABLE_NAME,
    TABLE_TYPE,
    REMARKS,
    TYPE_CAT,
    TYPE_SCHEM,
    TYPE_NAME,
    SELF_REFERENCING_COL_NAME,
    REF_GENERATION;
}
