// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


/**
 * created by qiongli on 2013-11-13 Detailled comment
 *
 */
public class Sybase16SADatabaseMetaData extends SybaseDatabaseMetaData {

    private static Logger log = Logger.getLogger(Sybase16SADatabaseMetaData.class);

    /**
     * DOC qiongli SybaseIQDatabaseMetaData constructor comment.
     *
     * @param connection
     * @throws SQLException
     */
    public Sybase16SADatabaseMetaData(Connection connection) throws SQLException {
        super(connection);
    }

    public ResultSet getCatalogs(String login, String database) throws SQLException {
        List<String[]> list = new ArrayList<String[]>();

        List<String> catList = new ArrayList<String>();
        if (!StringUtils.isEmpty(database)) {
            catList.add(database);
        }
        
        for (String catalogName : catList) {
            String sql = createSqlByLoginAndCatalog(login, catalogName);
            ResultSet rs = null;
            PreparedStatement stmt = null;
            try {
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, login);

                rs = stmt.executeQuery();

                while (rs.next()) {
                    int temp = rs.getInt(1);
                    if (temp > 0) {
                        String[] r = new String[] { catalogName };
                        list.add(r);
                    }
                }
            } catch (SQLException e) {
                log.error(e);
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (Exception e) {
                    log.error(e);
                }
            }
        }

        SybaseResultSet tableResultSet = new SybaseResultSet();
        tableResultSet.setMetadata(new String[] { "TABLE_CAT" }); //$NON-NLS-1$
        tableResultSet.setData(list);
        return tableResultSet;
    }
    
    @Override
    public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern)
            throws SQLException {
        return super.getColumns(null, schemaPattern, tableNamePattern, columnNamePattern);
    }

    @Override
    public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
        return super.getPrimaryKeys(null, schema, table);
    }

    @Override
    public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types)
            throws SQLException {
        return super.getTables(null, schemaPattern, tableNamePattern, types);
    }

    /**
     *
     * get a sql query by login name and catalog name.
     *
     * @param loginName
     * @param catalogName
     * @return
     */
    protected String createSqlByLoginAndCatalog(String loginName, String catalogName) {
        String sql = "select count(*) from " + catalogName
                + ".dbo.sysusers where suid in (select suid from " + catalogName + ".dbo.syslogins where name = ? )";
        return sql;
    }

}
