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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.database.manager.ExtractManager;
import org.talend.cwm.relational.TdColumn;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class AccessExtractManager extends ExtractManager {

    private static Logger log = Logger.getLogger(AccessExtractManager.class);

    public AccessExtractManager() {
        super(EDatabaseTypeName.ACCESS);
    }

    @Override
    protected String checkTableLabel(String tableLabel) {
        return tableLabel; // no need
    }

    @Override
    protected Map<String, String> retrievePrimaryKeys(DatabaseMetaData dbMetaData, String catalogName, String schemaName,
            String tableName) throws SQLException {
        Map<String, String> primaryKeys = new HashMap<String, String>();

        if (dbMetaData != null) {
            try {
                ResultSet keys = null;
                try {
                    keys = dbMetaData.getIndexInfo(catalogName, schemaName, tableName, true, true);
                    while (keys.next()) {
                        primaryKeys.put(keys.getString("COLUMN_NAME"), "PRIMARY KEY"); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                } finally {
                    if (keys != null) {
                        keys.close();
                    }
                }
            } catch (Exception e) {
                log.error(e.toString());
            }
            checkUniqueKeyConstraint(tableName, primaryKeys, dbMetaData.getConnection());
        }
        return primaryKeys;
    }

    @Override
    protected ResultSet getColumnsResultSet(DatabaseMetaData dbMetaData, String catalogName, String schemaName, String tableName)
            throws SQLException {
        if (schemaName != null) {
            // The Access's schema should be null here otherwise the columns will be null
            // will be null always
            schemaName = null;
        }
        return super.getColumnsResultSet(dbMetaData, catalogName, schemaName, tableName);
    }

    @Override
    protected void addColumnAttributes(IMetadataConnection metadataConnection, ResultSet columns, TdColumn metadataColumn,
            String label, String label2, String dbType, Integer columnSize, Integer intMetaDataInfo, String commentInfo)
            throws SQLException {
        // nothing to do for access
    }

}
