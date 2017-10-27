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

public class ImpalaQueryGenerator extends AbstractQueryGenerator {

    public ImpalaQueryGenerator(EDatabaseTypeName dbType) {
        super(dbType);
    }

    @Override
    public String generateQueryDelegate() {

        if (getMetadataTable() != null && !getMetadataTable().getListColumns().isEmpty()) {
            String tableName = null;
            String schemaName = schema;
            if (getElement() != null) {
                tableName = getDBTableName(getElement());
            } else {
                tableName = realTableName;
            }
            final String tableNameWithDBAndSchema = getTableNameWithDBAndSchema("", schemaName, tableName);

            StringBuffer sql = new StringBuffer(100);
            sql.append(TalendTextUtils.getQuoteChar());
            sql.append(SQL_SELECT);
            sql.append(SPACE);
            // columns
            sql.append(generateColumnFields(tableNameWithDBAndSchema));
            sql.append(ENTER);
            sql.append(SQL_FROM);
            sql.append(SPACE);
            sql.append(tableNameWithDBAndSchema);
            sql.append(TalendTextUtils.getQuoteChar());

            return processResultSQL(sql.toString());
        }
        return EMPTY;
    }

}
