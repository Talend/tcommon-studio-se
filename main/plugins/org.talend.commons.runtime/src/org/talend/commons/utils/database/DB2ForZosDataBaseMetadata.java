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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * DOC bqian class global comment. Detailled comment
 */
public class DB2ForZosDataBaseMetadata extends PackageFakeDatabaseMetadata {

    private static final String[] TABLE_META = { "ID", "TABLE_SCHEM", "TABLE_NAME", "TABLE_TYPE", "REMARKS" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

    private static final String[] COLUMN_META = { "TABLE_NAME", "COLUMN_NAME", "TYPE_NAME", "COLUMN_SIZE", "DECIMAL_DIGITS", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
            "IS_NULLABLE", "REMARKS", "COLUMN_DEF" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    private String T = "T";//$NON-NLS-1$

    private String V = "V";//$NON-NLS-1$

    private String S = "S";//$NON-NLS-1$

    private String A = "A";//$NON-NLS-1$

    private String TABLE = "TABLE"; //$NON-NLS-1$

    private String VIEW = "VIEW"; //$NON-NLS-1$

    private String SYNONYM = "SYNONYM"; //$NON-NLS-1$

    private String ALIAS = "ALIAS"; //$NON-NLS-1$

    /**
     * DOC xqliu Comment method "getDb2zosTypeName".
     * 
     * @param typeName
     * @return
     */
    private String getDb2zosTypeName(String typeName) {
        String result = typeName;
        if (TABLE.equals(typeName)) {
            result = T;
        } else if (VIEW.equals(typeName)) {
            result = V;
        } else if (SYNONYM.equals(typeName)) {
            result = S;
        } else if (ALIAS.equals(typeName)) {
            result = A;
        }
        return result;
    }

    /**
     * DOC xqliu Comment method "getTypeNameFromDb2zosType".
     * 
     * @param typeName
     * @return
     */
    private String getTypeNameFromDb2zosType(String typeName) {
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

    /**
     * DOC bqian DB2ForZosDataBaseMetadata constructor comment.
     * 
     * @param metaData
     * @throws SQLException
     */
    public DB2ForZosDataBaseMetadata(Connection connection) throws SQLException {
        super(connection);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.utils.database.FakeDatabaseMetaData#getSchemas()
     */
    @Override
    public ResultSet getSchemas() throws SQLException {
        // see the feature 5827
        // MOD yyin 2012-05-15 TDQ-5190
        String sql = "SELECT DISTINCT CREATOR FROM SYSIBM.SYSTABLES"; //$NON-NLS-1$
        ResultSet rs = null;
        Statement stmt = null;
        List<String[]> list = new ArrayList<String[]>();
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String creator = rs.getString("CREATOR"); //$NON-NLS-1$

                String[] r = new String[] { creator.trim() };
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

        DB2ForZosResultSet tableResultSet = new DB2ForZosResultSet();
        tableResultSet.setMetadata(new String[] { "TABLE_SCHEM" }); //$NON-NLS-1$
        tableResultSet.setData(list);
        return tableResultSet;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.utils.database.FakeDatabaseMetaData#getPrimaryKeys(java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
        return new DB2ForZosResultSet();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.utils.database.FakeDatabaseMetaData#getTableTypes()
     */
    @Override
    public ResultSet getTableTypes() throws SQLException {
        String[] s1 = new String[] { "TABLE" }; //$NON-NLS-1$
        String[] s2 = new String[] { "VIEW" }; //$NON-NLS-1$
        String[] s3 = new String[] { "SYNONYM" }; //$NON-NLS-1$
        String[] s4 = new String[] { "ALIAS" }; //$NON-NLS-1$

        List<String[]> list = new ArrayList<String[]>();

        list.add(s1);
        list.add(s2);
        list.add(s3);
        list.add(s4);

        DB2ForZosResultSet tableResultSet = new DB2ForZosResultSet();
        tableResultSet.setMetadata(new String[] { "TABLE_TYPE" }); //$NON-NLS-1$
        tableResultSet.setData(list);

        return tableResultSet;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.utils.database.FakeDatabaseMetaData#getExportedKeys(java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
        return new DB2ForZosResultSet();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.utils.database.FakeDatabaseMetaData#getTables(java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String[])
     */
    @Override
    public ResultSet getTables(String catalog, String schema, String tableNamePattern, String[] types) throws SQLException {
        String sql;
        String and;
        if (schema != null) {
            sql = "SELECT * FROM SYSIBM.SYSTABLES where CREATOR = '" + schema + "'"; //$NON-NLS-1$ //$NON-NLS-2$
            and = " and ";
        } else {
            sql = "SELECT * FROM SYSIBM.SYSTABLES"; //$NON-NLS-1$
            and = " where ";
        }
        // ADD xqliu 2012-04-17 TDQ-5118
        sql = addTypesToSql(sql, types, and);
        // ~ TDQ-5118
        // Added TDQ-8523 20140114 yyin, add the filter for table/views
        if (!StringUtils.isEmpty(tableNamePattern)) {
            sql = sql + " and NAME like ?";//$NON-NLS-1$
        }

        ResultSet rs = null;
        PreparedStatement stmt = null;
        List<String[]> list = new ArrayList<String[]>();
        try {
            stmt = connection.prepareStatement(sql);
            // Added TDQ-8523 20140114 yyin, add the filter for table/views
            if (!StringUtils.isEmpty(tableNamePattern)) {
                stmt.setString(1, tableNamePattern);
            }// ~
            rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("NAME"); //$NON-NLS-1$
                String creator = rs.getString("CREATOR"); //$NON-NLS-1$
                String type = rs.getString("TYPE"); //$NON-NLS-1$
                // String dbname = rs.getString("DBNAME");

                String[] r = new String[] { "", creator, name, getTypeNameFromDb2zosType(type), "" }; //$NON-NLS-1$ //$NON-NLS-2$
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

        DB2ForZosResultSet tableResultSet = new DB2ForZosResultSet();
        tableResultSet.setMetadata(TABLE_META);
        tableResultSet.setData(list);
        return tableResultSet;
    }

    /**
     * DOC xqliu Comment method "addTypesToSql".
     * 
     * @param sql
     * @param types
     * @param and
     * @return
     */
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
                typeClause = typeClause + comma + "'" + getDb2zosTypeName(types[i]) + "'";//$NON-NLS-1$ //$NON-NLS-2$
            }
            typeClause = typeClause + ")"; //$NON-NLS-1$
            result = sql + and + typeClause;
        }
        return result;
    }

    /**
     * DOC zshen Comment method "checkContainTable".
     * 
     * @param types
     */
    private boolean checkContainTable(String[] types) {
        if (types != null && types.length > 0) {
            int len = types.length;
            for (int i = 0; i < len; ++i) {
                if (TABLE.equals(types[i])) {
                    return true;
                }
            }
        }
        return false;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.utils.database.FakeDatabaseMetaData#supportsSchemasInDataManipulation()
     */
    @Override
    public boolean supportsSchemasInDataManipulation() throws SQLException {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.utils.database.FakeDatabaseMetaData#supportsSchemasInTableDefinitions()
     */
    @Override
    public boolean supportsSchemasInTableDefinitions() throws SQLException {
        return true;
    }

    @Override
    public String getIdentifierQuoteString() throws SQLException {
        return "\""; //$NON-NLS-1$ 
    }

    @Override
    public boolean supportsSchemasInIndexDefinitions() throws SQLException {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.utils.database.FakeDatabaseMetaData#getColumns(java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern)
            throws SQLException {
        // for real
        String sql = "SELECT * FROM SYSIBM.SYSCOLUMNS where TBNAME='" + tableNamePattern + "' AND  TBCREATOR = '" //$NON-NLS-1$ //$NON-NLS-2$
                + schemaPattern + "' ORDER BY TBCREATOR, TBNAME, COLNO"; //$NON-NLS-1$

        // for test
        // String sql = "SELECT * FROM SYSIBM.SYSCOLUMNS where NAME='NAME'";

        ResultSet rs = null;
        Statement stmt = null;
        List<String[]> list = new ArrayList<String[]>();
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {

                // For real db2 for zos, should use these code.
                String tableName = rs.getString("TBNAME"); //$NON-NLS-1$
                String columnName = rs.getString("NAME"); //$NON-NLS-1$
                String typeName = null;
                try {
                    typeName = rs.getString("TYPENAME"); //$NON-NLS-1$
                    if (typeName == null) {
                        typeName = rs.getString("COLTYPE"); //$NON-NLS-1$
                    }
                } catch (Exception e) {
                    typeName = rs.getString("COLTYPE"); //$NON-NLS-1$
                }
                String columnSize = rs.getString("LENGTH"); //$NON-NLS-1$
                String decimalDigits = rs.getString("SCALE"); //$NON-NLS-1$
                String isNullable;
                if (rs.getString("NULLS").equals("Y")) { //$NON-NLS-1$ //$NON-NLS-2$
                    isNullable = "YES"; //$NON-NLS-1$
                } else {
                    isNullable = rs.getString("NULLS"); //$NON-NLS-1$
                }
                // String keys = rs.getString("keys");
                String remarks = ""; //$NON-NLS-1$
                String columnDef = ""; //$NON-NLS-1$

                // this is for testing with DB2 on windows
                // String tableName = tableNamePattern;
                // String columnName = rs.getString("NAME");
                // String typeName = "datatype";
                // String columnSize = "10";
                // String decimalDigits = "10";
                // String isNullable = "true";
                // String remarks = "remarks";
                // String columnDef = "columnDef";

                String[] r = new String[] { tableName, columnName, typeName, columnSize, decimalDigits, isNullable, // keys
                        // ,
                        remarks, columnDef };
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

        DB2ForZosResultSet tableResultSet = new DB2ForZosResultSet();
        tableResultSet.setMetadata(COLUMN_META);
        tableResultSet.setData(list);
        return tableResultSet;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.fakejdbc.FakeDatabaseMetaData#getDatabaseProductName()
     */
    @Override
    public String getDatabaseProductName() throws SQLException {
        return connection.getMetaData().getDatabaseProductName();
    }

}
