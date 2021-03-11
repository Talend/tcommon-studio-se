// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.model.metadata.query.generator;

import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.QueryUtil;
import org.talend.core.model.metadata.query.AbstractQueryGenerator;
import org.talend.core.model.process.IElement;

/**
 * ggu class global comment. Detailled comment
 */
public class PostgreQueryGenerator extends AbstractQueryGenerator {

    public PostgreQueryGenerator(EDatabaseTypeName dbType) {
        super(dbType);
    }

    @Override
    protected boolean forceAddQuote() {
        // return super.forceAddQuote();
        return true; // always quote
    }

    protected String getSchema(IElement elem) {
        // bug 20365
        if (schema != null) {
            return schema;
        }
        return super.getSchema(elem);
    }

    @Override
    protected String getTableNameWithDBAndSchema(final String dbName, final String schema, String tableName) {
        if (tableName == null || EMPTY.equals(tableName.trim())) {
            tableName = QueryUtil.DEFAULT_TABLE_NAME;
        }

        final StringBuffer tableNameWithDBAndSchema = new StringBuffer();

        // postgres do not support db.schema.table_name, so only need schema.table_name
        if (schema != null && !EMPTY.equals(schema)) {
            tableNameWithDBAndSchema.append(checkContextAndAddQuote(schema));
            tableNameWithDBAndSchema.append(getSQLFieldConnector());
        }
        //
        tableNameWithDBAndSchema.append(checkContextAndAddQuote(tableName));

        return tableNameWithDBAndSchema.toString();
    }
}
