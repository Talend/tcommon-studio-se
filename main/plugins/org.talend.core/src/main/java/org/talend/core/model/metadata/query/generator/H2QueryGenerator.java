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
import org.talend.core.model.metadata.query.AbstractQueryGenerator;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.core.utils.TalendQuoteUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class H2QueryGenerator extends AbstractQueryGenerator {

    public H2QueryGenerator(EDatabaseTypeName dbType) {
        super(dbType);
    }

    @Override
    public String generateQueryDelegate() {
        if (getDBType() != null) {
            String quoteByDBType = TalendTextUtils.getQuoteByDBType(getDBType());
            String tableName = TalendQuoteUtils.addQuotesForSQLString(realTableName, quoteByDBType, true);
            tableName = TalendTextUtils.removeQuotes(tableName);

            StringBuffer sql = new StringBuffer(100);
            sql.append(TalendTextUtils.getQuoteChar());
            sql.append(SQL_SELECT);
            sql.append(SPACE);
            // columns
            sql.append(generateColumnFields(tableName));
            //
            sql.append(ENTER);
            sql.append(SPACE);
            sql.append(SQL_FROM);
            sql.append(SPACE);
            sql.append(tableName);

            sql.append(TalendTextUtils.getQuoteChar());

            return processResultSQL(sql.toString());

        }

        return EMPTY;

    }

    @Override
    protected String addQuotesForSQL(String field) {
        String quoteByDBType = TalendTextUtils.getQuoteByDBType(getDBType());
        field = TalendQuoteUtils.addQuotesForSQLString(field, quoteByDBType, true);
        field = TalendTextUtils.removeQuotes(field);
        return field;
    }
}
