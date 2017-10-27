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
 * This class contains the strings used by the java.sql.DatabaseMetaData to get the columns from the result sets.
 * @see java.sql.DatabaseMetaData#getCatalogs()
 * @see java.sql.DatabaseMetaData#getSchemas()
 * @see java.sql.ResultSet.ResultSet#getString(String)
 * @see TypeInfoColumns
 */
public enum MetaDataConstants {
    /**
     * Used when calling {@link java.sql.ResultSet#getString(String)} on the result set from
     * {@link java.sql.DatabaseMetaData#getCatalogs()}.
     */
    TABLE_CAT,
    /**
     * Used when calling {@link java.sql.ResultSet#getString(String)} on the result set from
     * {@link java.sql.DatabaseMetaData#getSchemas()}.
     */
    TABLE_SCHEM,
    /**
     * Used when calling java.sql.ResultSet#getString(String) on the result set from
     * java.sql.DatabaseMetaData#getCatalogs().
     */
    TABLE_CATALOG;
}
