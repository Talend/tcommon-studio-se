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
package org.talend.repository.metadata;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.sourceforge.jtds.jdbc.JtdsConnection;
import net.sourceforge.jtds.jdbc.JtdsDatabaseMetaData;

/**
 * DOC sizhaoliu class global comment. Detailled comment
 */
public class JtdsMetadataAdapter extends JtdsDatabaseMetaData {

    public static final boolean JDBC3 = "1.4".compareTo(System.getProperty("java.specification.version")) <= 0;

    private JtdsConnection connection;

    /**
     * DOC sizhaoliu JtdsMetadataAdapter constructor comment.
     * 
     * @param connection
     */
    public JtdsMetadataAdapter(JtdsConnection connection) {
        super(connection);
        this.connection = connection;
        // TODO Auto-generated constructor stub
    }

    public ResultSet getSchemas() throws SQLException {
        java.sql.Statement statement = connection.createStatement();
        String sql;
        if (connection.getDatabaseMajorVersion() >= 9) {
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
