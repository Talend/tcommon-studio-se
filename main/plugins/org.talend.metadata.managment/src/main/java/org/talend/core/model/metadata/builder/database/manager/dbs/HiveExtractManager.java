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
package org.talend.core.model.metadata.builder.database.manager.dbs;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.builder.database.manager.ExtractManager;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class HiveExtractManager extends ExtractManager {

    public HiveExtractManager() {
        super(EDatabaseTypeName.HIVE);
    }

    @Override
    protected Map<String, String> retrievePrimaryKeys(DatabaseMetaData dbMetaData, String catalogName, String schemaName,
            String tableName) throws SQLException {
        // no primary key for hive
        return Collections.emptyMap();
    }

}
