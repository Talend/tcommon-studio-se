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
package org.talend.core.model.metadata.builder.database;

/**
 * DOC hywang.this enum is used to specific the relationship between schema and catalog and how they get their name from
 */
public enum EDatabaseSchemaOrCatalogMapping {
    // this kind of database do not need schema/catalog
    None,
    // don't know if refing to schema or catalog? represent the use the valuse of Connection.getSID()
    Sid,
    // schema definition : represent the use the valuse of Connection.getUiSchema()
    Schema,
    // catalog or schema definition : represent the use the valuse of Connection.getUserName() ,this is only used for
    // as400.
    Login,
    // for hbase,column family can be considered as the schema of the db
    Column_Family,
    // schema definition : use the connection.getName()
    Default_Name
    // this is for databases like micosoft access which didn't require the schema or catalog
}
