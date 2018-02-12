// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
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
import java.util.List;
import java.util.Set;

import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.metadata.builder.database.manager.ExtractManager;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class AS400ExtractManager extends ExtractManager {

    public AS400ExtractManager() {
        super(EDatabaseTypeName.AS400);
    }

    @Override
    public String getSchema(IMetadataConnection metadataConnection) {
        String schema = super.getSchema(metadataConnection);
        if (schema == null || EMPTY.equals(schema.trim())) {
            String url = metadataConnection != null ? metadataConnection.getUrl() : null;
            if (url != null) {
                schema = ExtractMetaDataUtils.getInstance().retrieveSchemaPatternForAS400(url);
            }
        }
        return schema;
    }

    @Override
    protected void retrieveTables(DatabaseMetaData dbMetaData, String schema, List<IMetadataTable> medataTables,
            Set<String> availableTableTypes, List<String> tablesToFilter, int... limit) throws SQLException {
        boolean supportsSchemasInTableDefinitions = dbMetaData.supportsSchemasInTableDefinitions();
        if (supportsSchemasInTableDefinitions && schema != null && !EMPTY.equals(schema)) {
            String[] multiSchems = ExtractMetaDataUtils.getInstance().getMultiSchems(schema);
            if (multiSchems != null) {
                ResultSet rsTables = null;
                for (String s : multiSchems) {
                    rsTables = dbMetaData.getTables(null, s.trim(), null, availableTableTypes.toArray(new String[] {}));
                    try {
                        getMetadataTables(medataTables, rsTables, supportsSchemasInTableDefinitions, tablesToFilter, limit);
                    } finally {
                        rsTables.close();
                    }
                }
            }
        } else {
            super.retrieveTables(dbMetaData, schema, medataTables, availableTableTypes, tablesToFilter, limit);
        }
    }

}
