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
 * @link java.sql.DatabaseMetaData#getPrimaryKeys(String, String, String)
 * 
 * 
 * <P>
 * Each primary key column description has the following columns:
 * <OL>
 * <LI><B>TABLE_CAT</B> String => table catalog (may be <code>null</code>)
 * <LI><B>TABLE_SCHEM</B> String => table schema (may be <code>null</code>)
 * <LI><B>TABLE_NAME</B> String => table name
 * <LI><B>COLUMN_NAME</B> String => column name
 * <LI><B>KEY_SEQ</B> short => sequence number within primary key
 * <LI><B>PK_NAME</B> String => primary key name (may be <code>null</code>)
 * </OL>
 */
public enum GetPrimaryKey {
    TABLE_CAT,
    TABLE_SCHEM,
    TABLE_NAME,
    COLUMN_NAME,
    KEY_SEQ,
    PK_NAME
}
