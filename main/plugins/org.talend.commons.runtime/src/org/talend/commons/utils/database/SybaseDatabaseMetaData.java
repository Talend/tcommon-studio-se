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
package org.talend.commons.utils.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

/**
 * created by xqliu on Oct 26, 2012 Detailled comment
 * 
 */
public class SybaseDatabaseMetaData extends PackageFakeDatabaseMetadata {

    private static final String[] TABLE_META = { "ID", "TABLE_SCHEM", "TABLE_NAME", "TABLE_TYPE", "REMARKS" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

    private static final String[] NEEDED_TYPES = { "TABLE", "VIEW" }; //$NON-NLS-1$ //$NON-NLS-2$

    private static Logger log = Logger.getLogger(SybaseDatabaseMetaData.class);

    /**
     * DOC xqliu SybaseDatabaseMetaData constructor comment.
     */
    public SybaseDatabaseMetaData(Connection connection) throws SQLException {
        super(connection);
    }

    public ResultSet getCatalogs(String login) throws SQLException {
        List<String[]> list = new ArrayList<String[]>();

        List<String> catList = new ArrayList<String>();
        ResultSet rsCat = this.metaData.getCatalogs();
        while (rsCat.next()) {
            catList.add(rsCat.getString("TABLE_CAT")); //$NON-NLS-1$
        }
        rsCat.close();

        for (String catalogName : catList) {
            String sql = createSqlByLoginAndCatalog(login, catalogName);
            ResultSet rs = null;
            Statement stmt = null;
            try {
                stmt = connection.createStatement();
                rs = stmt.executeQuery(sql);

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
    public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
        String sql = "SELECT DISTINCT name FROM " + catalog + ".dbo.sysusers where suid > 0"; //$NON-NLS-1$ //$NON-NLS-2$
        ResultSet rs = null;
        Statement stmt = null;
        List<String[]> list = new ArrayList<String[]>();
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String name = rs.getString("name"); //$NON-NLS-1$

                String[] r = new String[] { name.trim() };
                list.add(r);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
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

        SybaseResultSet tableResultSet = new SybaseResultSet();
        tableResultSet.setMetadata(new String[] { "TABLE_SCHEM" }); //$NON-NLS-1$
        tableResultSet.setData(list);
        return tableResultSet;
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
        return "select count(*) from " + catalogName //$NON-NLS-1$
                + ".dbo.sysusers where suid in (select suid from master.dbo.syslogins where name = '" + loginName //$NON-NLS-1$
                + "') or suid in (select altsuid from " + catalogName //$NON-NLS-1$
                + ".dbo.sysalternates a, master.dbo.syslogins b where b.name = '" + loginName + "' and a.suid = b.suid)"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
        // TDI-24905:rewrite this function for sybase in case any NPE for result set
        ResultSet sybaseRS = super.getExportedKeys(catalog, schema, table);
        if (sybaseRS == null) {
            if (this.metaData != null) {
                sybaseRS = this.metaData.getExportedKeys(catalog, schema, table);
            } else {
                sybaseRS = new SybaseResultSet();
            }
        }
        return sybaseRS;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.utils.database.PackageFakeDatabaseMetadata#getTables(java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String[])
     */
    @Override
    public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
        String[] neededTypes = getNeededTypes(types);
        ResultSet sybaseRS = super.getTables(catalog, schemaPattern, tableNamePattern, neededTypes);
        List<String[]> list = new ArrayList<String[]>();
        while (sybaseRS.next()) {
            String name = sybaseRS.getString("TABLE_NAME"); //$NON-NLS-1$
            String schema = sybaseRS.getString("TABLE_SCHEM"); //$NON-NLS-1$
            String type = sybaseRS.getString("TABLE_TYPE"); //$NON-NLS-1$

            String id = ""; //$NON-NLS-1$
            String remarks = ""; //$NON-NLS-1$
            try {
                remarks = sybaseRS.getString("REMARKS"); //$NON-NLS-1$
            } catch (Exception e) {
                // nothing
            }

            if (ArrayUtils.contains(neededTypes, type)) {
                // check if the type is contained is in the types needed.
                // since sybase can return some system views as "SYSTEM VIEW" instead of "VIEW/TABLE" from the request.
                String[] r = new String[] { id, schema, name, type, remarks };
                list.add(r);
            }
        }
        SybaseResultSet tableResultSet = new SybaseResultSet();
        tableResultSet.setMetadata(TABLE_META);
        tableResultSet.setData(list);
        return tableResultSet;
    }

    private String[] getNeededTypes(String[] types) {
        List<String> list = new ArrayList<String>();
        if (types != null && types.length > 0) {
            for (String type : types) {
                if (ArrayUtils.contains(NEEDED_TYPES, type)) {
                    list.add(type);
                }
            }
        }
        return list.toArray(new String[list.size()]);
    }
}
