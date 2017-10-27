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

import java.sql.ResultSet;
import java.sql.SQLException;

import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.database.DriverShim;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.metadata.builder.database.manager.ExtractManager;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class HSQLDBExtractManager extends ExtractManager {

    public HSQLDBExtractManager(EDatabaseTypeName dbType) {
        super(dbType);
    }

    @Override
    public boolean closeConnection(IMetadataConnection metadataConnection, DriverShim wapperDriver) {
        boolean closed = super.closeConnection(metadataConnection, wapperDriver);
        // bug 22619
        ExtractMetaDataUtils.getInstance().closeConnection();

        if (wapperDriver != null && EDatabaseTypeName.HSQLDB_IN_PROGRESS.equals(this.getDbType())) {
            closed = closeConnectionForDerby(wapperDriver);
        }
        return closed;
    }

    @Override
    public String getTableComment(IMetadataConnection metadataConnection, ResultSet resultSet, String nameKey)
            throws SQLException {
        if (metadataConnection != null
                && EDatabaseTypeName.HSQLDB_IN_PROGRESS.getDisplayName().equals(metadataConnection.getDbType())) {
            return EMPTY; // no commit for derby
        }
        return super.getTableComment(metadataConnection, resultSet, nameKey);
    }
}
