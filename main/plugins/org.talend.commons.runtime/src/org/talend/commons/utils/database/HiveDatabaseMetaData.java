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
package org.talend.commons.utils.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.talend.fakejdbc.FakeDatabaseMetaData;

/**
 * 
 * Created by Marvin Wang on Nov 29, 2012.
 */
public class HiveDatabaseMetaData extends FakeDatabaseMetaData {

    private Connection connection;

    private DatabaseMetaData metaData;

    public HiveDatabaseMetaData(Connection connection) throws SQLException {
        this.connection = connection;
        metaData = connection.getMetaData();
    }

    @Override
    public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
        return this.metaData.getTables(catalog, schemaPattern, tableNamePattern, types);
    }

}
