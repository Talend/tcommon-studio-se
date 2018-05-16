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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * created by zshen on Apr 12, 2013 Detailled comment
 * 
 */
public class AS400DatabaseMetaData extends PackageFakeDatabaseMetadata {

    // private static final String[] TABLE_META = {
    //            "ID", "TABLE_SCHEM", "TABLE_NAME", "TABLE_TYPE", "REMARKS", "TABLE_NAME", "SYSTEM_TABLE_NAME", "TABLE_SCHEMA", "SYSTEM_TABLE_SCHEMA" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

    private static final String[] TABLE_META = {
            "TABLE_TYPE", "TABLE_NAME", "SYSTEM_TABLE_NAME", "TABLE_SCHEMA", "SYSTEM_TABLE_SCHEMA" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

    private String T = "T";//$NON-NLS-1$

    private String V = "V";//$NON-NLS-1$

    private String S = "S";//$NON-NLS-1$

    private String A = "A";//$NON-NLS-1$

    private String P = "P";//$NON-NLS-1$

    private String TABLE = "TABLE"; //$NON-NLS-1$

    private String EXTERNAL_TABLE = "EXTERNAL_TABLE"; //$NON-NLS-1$

    private String VIEW = "VIEW"; //$NON-NLS-1$

    private String SYNONYM = "SYNONYM"; //$NON-NLS-1$

    private String ALIAS = "ALIAS"; //$NON-NLS-1$

    public AS400DatabaseMetaData(Connection conn) throws SQLException {
        super(conn);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.fakejdbc.FakeDatabaseMetaData#getCatalogs()
     */
    @Override
    public ResultSet getCatalogs() throws SQLException {
        // TODO Auto-generated method stub
        return super.getCatalogs();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.fakejdbc.FakeDatabaseMetaData#getColumns(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern)
            throws SQLException {
        // TODO Auto-generated method stub
        return super.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.fakejdbc.FakeDatabaseMetaData#getConnection()
     */
    @Override
    public Connection getConnection() {
        // TODO Auto-generated method stub
        return super.getConnection();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.fakejdbc.FakeDatabaseMetaData#getTables(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String[])
     */
    @Override
    public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
        String sql;
        String and;
        if (schemaPattern != null) {
            sql = "SELECT  TYPE,TABLE_NAME, SYSTEM_TABLE_NAME, TABLE_SCHEMA , SYSTEM_TABLE_SCHEMA  FROM QSYS2.SYSTABLES  WHERE TABLE_SCHEMA = '"
                    + schemaPattern + "'";
            and = " and ";
        } else {
            sql = "SELECT  TYPE,TABLE_NAME, SYSTEM_TABLE_NAME, TABLE_SCHEMA , SYSTEM_TABLE_SCHEMA  FROM QSYS2.SYSTABLES";
            and = " where ";
        }
        sql = addTypesToSql(sql, types, and);
        if (!StringUtils.isEmpty(tableNamePattern)) {
            sql = sql + " and NAME like ?";//$NON-NLS-1$
        }

        ResultSet rs = null;
        PreparedStatement stmt = null;
        List<String[]> list = new ArrayList<String[]>();
        try {
            stmt = connection.prepareStatement(sql);
            if (!StringUtils.isEmpty(tableNamePattern)) {
                stmt.setString(1, tableNamePattern);
            }// ~
            rs = stmt.executeQuery();
            while (rs.next()) {
                String type = rs.getString("TYPE"); //$NON-NLS-1$
                String table_name = rs.getString("TABLE_NAME"); //$NON-NLS-1$
                String system_table_name = rs.getString("SYSTEM_TABLE_NAME"); //$NON-NLS-1$
                String table_schema = rs.getString("TABLE_SCHEMA"); //$NON-NLS-1$
                String system_table_schema = rs.getString("SYSTEM_TABLE_SCHEMA"); //$NON-NLS-1$

                String[] r = new String[] { type, table_name, system_table_name, table_schema, system_table_schema };
                list.add(r);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                rs.close();
                stmt.close();
            } catch (Exception e) {
            }
        }

        AS400ResultSet tableResultSet = new AS400ResultSet();
        tableResultSet.setMetadata(TABLE_META);
        tableResultSet.setData(list);
        return tableResultSet;
    }

    private String getTypeNameType(String typeName) {
        String result = typeName;
        if (T.equals(typeName)) {
            result = TABLE;
        } else if (V.equals(typeName)) {
            result = VIEW;
        } else if (S.equals(typeName)) {
            result = SYNONYM;
        } else if (A.equals(typeName)) {
            result = ALIAS;
        }
        return result;
    }

    private String addTypesToSql(String sql, String[] types, String and) {
        String result = sql;
        if (types != null && types.length > 0) {
            String typeClause = " type in("; //$NON-NLS-1$
            int len = types.length;
            for (int i = 0; i < len; ++i) {
                String comma = ""; //$NON-NLS-1$
                if (i > 0) {
                    comma = " , "; //$NON-NLS-1$
                }
                typeClause = typeClause + comma + "'" + getTypeName(types[i]) + "'";//$NON-NLS-1$ //$NON-NLS-2$
            }
            typeClause = typeClause + ")"; //$NON-NLS-1$
            result = sql + and + typeClause;
        }
        return result;
    }

    private String getTypeName(String typeName) {
        String result = typeName;
        if (TABLE.equals(typeName)) {
            result = T;
        } else if (VIEW.equals(typeName)) {
            result = V;
        } else if (SYNONYM.equals(typeName)) {
            result = S;
        } else if (ALIAS.equals(typeName)) {
            result = A;
        } else if (EXTERNAL_TABLE.equals(typeName)) {
            result = P;
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.fakejdbc.FakeDatabaseMetaData#getSchemas(java.lang.String, java.lang.String)
     */
    @Override
    public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
        ResultSet resultSet = null;
        try {
            resultSet = this.metaData.getSchemas(catalog, schemaPattern);
        } catch (SQLException e) {
            // get resultSet of schema by sql mode
            resultSet = this.metaData.getTables(catalog, schemaPattern, null, null);
        }
        return resultSet;
    }

}
