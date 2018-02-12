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

import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.database.manager.ExtractManager;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class TeradataExtractManager extends ExtractManager {

    public TeradataExtractManager() {
        super(EDatabaseTypeName.TERADATA);
    }

    @Override
    public String getSchema(IMetadataConnection metadataConnection) {
        String schema = super.getSchema(metadataConnection);
        if (schema == null || EMPTY.equals(schema.trim())) {
            schema = metadataConnection != null ? metadataConnection.getDatabase() : null;
        }
        return schema;
    }

}
