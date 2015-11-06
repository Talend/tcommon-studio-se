// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * created by hwang on Sep 18, 2015 Detailled comment
 *
 */
public class EXASOLDatabaseMetaData extends PackageFakeDatabaseMetadata {

	private static final Logger logger = Logger.getLogger(EXASOLDatabaseMetaData.class);

    private static final String[] TABLE_META = { "TABLE_TYPE", "TABLE_NAME", "TABLE_SCHEM", "REMARKS" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

    /**
     * DOC Talend EXASOLDatabaseMetaData constructor comment.
     * 
     * @param connection
     * @throws SQLException
     */
    public EXASOLDatabaseMetaData(Connection connection) throws SQLException {
        super(connection);
    }
    
	@Override
	public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
		return super.getSchemas(null, schemaPattern);
	}

	@Override
	public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
		// avoid using the catalog because the catalog will retrieved in a wrong way, we get here the user and not the database
		return super.getColumns(null, schemaPattern, tableNamePattern, columnNamePattern);
	}

	/*
     * (non-Javadoc)
     * 
     * @see org.talend.fakejdbc.FakeDatabaseMetaData#getTables(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String[])
     */
    @Override
    public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
    	ResultSet tables = super.getTables(null, schemaPattern, tableNamePattern, types);
        List<String[]> list = getTableList(tables);
        if (list.isEmpty()) {
        	// get the tables and views in the same way from the metadata API
        	StringBuilder sql = new StringBuilder();
        	sql.append("select\n");
        	sql.append(" a.TABLE_SCHEMA as TABLE_SCHEM,\n");
        	sql.append(" a.TABLE_NAME,\n");
        	sql.append(" 'TABLE' as TABLE_TYPE,\n");
        	sql.append(" TABLE_COMMENT as REMARKS\n");
        	sql.append("from SYS.EXA_ALL_TABLES a\n");
        	sql.append("union all\n");
        	sql.append("select \n");
        	sql.append(" a.VIEW_SCHEMA as TABLE_SCHEM,\n");
        	sql.append(" a.VIEW_NAME as TABLE_NAME,\n");
        	sql.append(" 'VIEW' as TABLE_TYPE,\n");
        	sql.append(" VIEW_COMMENT as REMARKS\n");
        	sql.append("from SYS.EXA_ALL_VIEWS a");
            ResultSet rs = null;
            PreparedStatement stmt = null;
            try {
                stmt = connection.prepareStatement(sql.toString());
                if (!StringUtils.isEmpty(tableNamePattern)) {
                    stmt.setString(1, tableNamePattern);
                }
                rs = stmt.executeQuery();
                list = getTableList(rs);
            } finally {
                try {
                	if (rs != null) {
                        rs.close();
                	}
                	if (stmt != null) {
                    	stmt.close();
                	}
                } catch (Exception e) {
                	// Intentionally left blank
                }
            }
        }
        EXASOLResultSet tableResultSet = new EXASOLResultSet();
        tableResultSet.setMetadata(TABLE_META);
        tableResultSet.setData(list);
        return tableResultSet;
    }

    private List<String[]> getTableList(ResultSet rs) throws SQLException {
        List<String[]> list = new ArrayList<String[]>();
        if (rs == null) {
            return list;
        }
        while (rs.next()) {
            String table_type = getStringFromResultSet(rs, "TABLE_TYPE");
            String table_name = getStringFromResultSet(rs, "TABLE_NAME");
            String table_schem = getStringFromResultSet(rs, "TABLE_SCHEM");
            String table_remarks = getStringFromResultSet(rs, "REMARKS");
            String[] r = new String[] { table_type, table_name, table_schem, table_remarks };
            list.add(r);
        }
        return list;
    }

    private String getStringFromResultSet(ResultSet resultSet, String nameOfString) {
        String valueOfString = null;
        try {
            valueOfString = resultSet.getString(nameOfString);
        } catch (SQLException e) {
        	logger.warn(e.getMessage(), e);
        }
        return valueOfString;
    }

}
