package org.talend.commons.utils.database;
//============================================================================
//
//Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
//This source code is available under agreement available at
//%InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
//You should have received a copy of the agreement
//along with this program; if not, write to Talend SA
//9 rue Pages 92150 Suresnes, France
//
//============================================================================
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JtdsDatabaseMetadata extends PackageFakeDatabaseMetadata {

    public static final boolean JDBC3 = "1.4".compareTo(System.getProperty("java.specification.version")) <= 0;

    public JtdsDatabaseMetadata(Connection connection) throws SQLException {
        super(connection);
    }

    @Override
    public ResultSet getSchemas() throws SQLException {
        java.sql.Statement statement = connection.createStatement();
        String sql;
        if (((PackageFakeDatabaseMetadata) connection).getDatabaseMajorVersion() >= 9) {
            sql = JDBC3 ? "SELECT name AS TABLE_SCHEM, NULL as TABLE_CATALOG FROM " + connection.getCatalog() + ".sys.schemas"
                    : "SELECT name AS TABLE_SCHEM FROM " + connection.getCatalog() + ".sys.schemas";
        } else {
            sql = JDBC3 ? "SELECT name AS TABLE_SCHEM, NULL as TABLE_CATALOG FROM dbo.sysusers"
                    : "SELECT name AS TABLE_SCHEM FROM dbo.sysusers";
        }

        sql += " ORDER BY TABLE_SCHEM";
        return statement.executeQuery(sql);
    }
}
