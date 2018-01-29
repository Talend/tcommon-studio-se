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
package org.talend.core.model.metadata.query.generator;

import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.QueryUtil;
import org.talend.core.model.metadata.query.AbstractQueryGenerator;

/**
 * ggu class global comment. Detailled comment
 */
public class NetezzaQueryGenerator extends AbstractQueryGenerator {

    public NetezzaQueryGenerator() {
        super(EDatabaseTypeName.NETEZZA);
    }

    /**
     * like "database..table".
     */
    protected String getTableNameWithDBAndSchema(final String dbName, final String schema, String tableName) {
        if (tableName == null || EMPTY.equals(tableName.trim())) {
            tableName = QueryUtil.DEFAULT_TABLE_NAME;
        }

        final StringBuffer tableNameWithDBAndSchema = new StringBuffer();

        //
        if (dbName != null && !EMPTY.equals(dbName)) {
            /*
             * should not add quote always.
             */
            setForceAddQuote(false);
            tableNameWithDBAndSchema.append(checkContextAndAddQuote(dbName));
            revertAddQuoteSetting();
            tableNameWithDBAndSchema.append(getSQLFieldConnector());

            // schema is special empty "."
            tableNameWithDBAndSchema.append(getSQLFieldConnector());
        }
        //
        // setForceAddQuote(true); //FIXME, if add quote always, should be opened
        tableNameWithDBAndSchema.append(checkContextAndAddQuote(tableName));
        // revertAddQuoteSetting();

        return tableNameWithDBAndSchema.toString();
    }

    /**
     * like "SELECT   talend..\"table\".\"column\",talend..\"table\".newColumn1 FROM talend..\"table\"" .
     */
    @Override
    public String generateQueryDelegate() {
        return super.generateQueryDelegate();
    }

}
