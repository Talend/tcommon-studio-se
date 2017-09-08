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
 * @link java.sql.DatabaseMetaData#getImportedKeys(String, String, String)
 * <P>
 * <OL>
 * <LI><B>PKTABLE_CAT</B> String => primary key table catalog being imported (may be <code>null</code>)
 * <LI><B>PKTABLE_SCHEM</B> String => primary key table schema being imported (may be <code>null</code>)
 * <LI><B>PKTABLE_NAME</B> String => primary key table name being imported
 * <LI><B>PKCOLUMN_NAME</B> String => primary key column name being imported
 * <LI><B>FKTABLE_CAT</B> String => foreign key table catalog (may be <code>null</code>)
 * <LI><B>FKTABLE_SCHEM</B> String => foreign key table schema (may be <code>null</code>)
 * <LI><B>FKTABLE_NAME</B> String => foreign key table name
 * <LI><B>FKCOLUMN_NAME</B> String => foreign key column name
 * <LI><B>KEY_SEQ</B> short => sequence number within a foreign key
 * <LI><B>UPDATE_RULE</B> short => What happens to a foreign key when the primary key is updated:
 * <UL>
 * <LI>importedNoAction - do not allow update of primary key if it has been imported
 * <LI>importedKeyCascade - change imported key to agree with primary key update
 * <LI>importedKeySetNull - change imported key to <code>NULL</code> if its primary key has been updated
 * <LI>importedKeySetDefault - change imported key to default values if its primary key has been updated
 * <LI>importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility)
 * </UL>
 * <LI><B>DELETE_RULE</B> short => What happens to the foreign key when primary is deleted.
 * <UL>
 * <LI>importedKeyNoAction - do not allow delete of primary key if it has been imported
 * <LI>importedKeyCascade - delete rows that import a deleted key
 * <LI>importedKeySetNull - change imported key to NULL if its primary key has been deleted
 * <LI>importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility)
 * <LI>importedKeySetDefault - change imported key to default if its primary key has been deleted
 * </UL>
 * <LI><B>FK_NAME</B> String => foreign key name (may be <code>null</code>)
 * <LI><B>PK_NAME</B> String => primary key name (may be <code>null</code>)
 * <LI><B>DEFERRABILITY</B> short => can the evaluation of foreign key constraints be deferred until commit
 * <UL>
 * <LI>importedKeyInitiallyDeferred - see SQL92 for definition
 * <LI>importedKeyInitiallyImmediate - see SQL92 for definition
 * <LI>importedKeyNotDeferrable - see SQL92 for definition
 * </UL>
 * </OL>
 */
public enum GetForeignKey {
    PKTABLE_CAT,
    PKTABLE_SCHEM,
    PKTABLE_NAME,
    PKCOLUMN_NAME,
    FKTABLE_CAT,
    FKTABLE_SCHEM,
    FKTABLE_NAME,
    FKCOLUMN_NAME,
    KEY_SEQ,
    UPDATE_RULE,
    DELETE_RULE,
    FK_NAME,
    PK_NAME,
    DEFERRABILITY
}
