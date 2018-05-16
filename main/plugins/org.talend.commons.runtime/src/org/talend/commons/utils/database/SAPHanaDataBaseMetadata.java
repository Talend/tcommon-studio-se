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
import org.talend.fakejdbc.FakeDatabaseMetaData;

/**
 * 
 * created by hcyi on Nov 3, 2017 Detailled comment
 *
 */
public class SAPHanaDataBaseMetadata extends FakeDatabaseMetaData {

    private static final String[] TABLE_META = { "ID", "TABLE_SCHEM", "TABLE_NAME", "TABLE_TYPE", "REMARKS", "TABLE_CAT" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

    private static final String[] COLUMN_META = { "TABLE_NAME", "COLUMN_NAME", "TYPE_NAME", "COLUMN_SIZE", "DECIMAL_DIGITS", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
            "IS_NULLABLE", "REMARKS", "COLUMN_DEF", "NUM_PREC_RADIX" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

    private static final String[] NEEDED_TYPES = { "TABLE", "VIEW", "SYNONYM", "CALCULATION VIEW" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$//$NON-NLS-4$

    private Connection connection;

    public SAPHanaDataBaseMetadata(Connection connection) {
        this.connection = connection;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.utils.database.FakeDatabaseMetaData#getConnection()
     */
    @Override
    public Connection getConnection() throws SQLException {
        return connection;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.utils.database.FakeDatabaseMetaData#getSchemas()
     */
    @Override
    public ResultSet getSchemas() throws SQLException {
        return connection.getMetaData().getSchemas();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.utils.database.FakeDatabaseMetaData#getPrimaryKeys(java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
        return new SAPHanaResultSet();
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
        String[] s4 = new String[] { "CALCULATION VIEW" }; //$NON-NLS-1$

        List<String[]> list = new ArrayList<String[]>();

        list.add(s1);
        list.add(s2);
        list.add(s3);
        list.add(s4);

        SAPHanaResultSet tableResultSet = new SAPHanaResultSet();
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
        return new SAPHanaResultSet();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.utils.database.FakeDatabaseMetaData#getTables(java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String[])
     */
    @Override
    public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types)
            throws SQLException {
        String[] neededTypes = getNeededTypes(types);
        ResultSet rs = connection.getMetaData().getTables(catalog, schemaPattern, tableNamePattern, neededTypes);
        List<String[]> list = new ArrayList<String[]>();
        while (rs.next()) {
            String name = rs.getString("TABLE_NAME"); //$NON-NLS-1$
            String schema = rs.getString("TABLE_SCHEM"); //$NON-NLS-1$
            String type = rs.getString("TABLE_TYPE"); //$NON-NLS-1$

            String id = ""; //$NON-NLS-1$
            String remarks = ""; //$NON-NLS-1$
            try {
                remarks = rs.getString("REMARKS"); //$NON-NLS-1$
            } catch (Exception e) {
                // nothing
            }

            if (ArrayUtils.contains(neededTypes, type)) {
                // check if the type is contained is in the types needed.
                // since sybase can return some system views as "SYSTEM VIEW" instead of "VIEW/TABLE" from the request.
                String[] r = new String[] { id, schema, name, type, remarks, null };
                list.add(r);
            }
        }

        // For Calculation View
        if (ArrayUtils.contains(neededTypes, NEEDED_TYPES[3])) {
            // check if the type is contained is in the types needed.
            String sqlcv = "SELECT CATALOG_NAME,SCHEMA_NAME,CUBE_NAME, COLUMN_OBJECT,CUBE_TYPE,DESCRIPTION from _SYS_BI.BIMC_CUBES"; //$NON-NLS-1$
            ResultSet rscv = null;
            Statement stmtcv = null;
            List<String[]> listcv = new ArrayList<String[]>();
            try {
                stmtcv = connection.createStatement();
                rscv = stmtcv.executeQuery(sqlcv);
                while (rscv.next()) {
                    String catalogName = rscv.getString("CATALOG_NAME"); //$NON-NLS-1$
                    if (catalogName != null) {
                        catalogName = catalogName.trim();
                    }
                    String schemaName = rscv.getString("SCHEMA_NAME"); //$NON-NLS-1$
                    if (schemaName != null) {
                        schemaName = schemaName.trim();
                    }
                    String cubeName = rscv.getString("CUBE_NAME"); //$NON-NLS-1$
                    if (cubeName != null) {
                        cubeName = cubeName.trim();
                    }
                    String id = ""; //$NON-NLS-1$
                    // String type = rscv.getString("CUBE_TYPE"); //$NON-NLS-1$

                    String remarks = rscv.getString("DESCRIPTION"); //$NON-NLS-1$
                    String name = catalogName + "/" + cubeName;//$NON-NLS-1$

                    String[] r = new String[] { id, schemaName, name, NEEDED_TYPES[3], remarks, catalogName };
                    listcv.add(r);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    rscv.close();
                    stmtcv.close();
                } catch (Exception e) {
                }
            }
            list.addAll(listcv);
        }
        SAPHanaResultSet tableResultSet = new SAPHanaResultSet();
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
        boolean load = false;
        ResultSet rs = connection.getMetaData().getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
        List<String[]> list = new ArrayList<String[]>();
        try {
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME"); //$NON-NLS-1$
                if (tableName != null) {
                    tableName = tableName.trim();
                }
                String columnName = rs.getString("COLUMN_NAME"); //$NON-NLS-1$
                if (columnName != null) {
                    columnName = columnName.trim();
                }
                String typeName = rs.getString("TYPE_NAME"); //$NON-NLS-1$
                if (typeName != null) {
                    typeName = typeName.trim();
                }
                String columnSize = rs.getString("COLUMN_SIZE"); //$NON-NLS-1$
                if (columnSize != null) {
                    columnSize = columnSize.trim();
                }
                String decimalDigits = rs.getString("DECIMAL_DIGITS"); //$NON-NLS-1$
                if (decimalDigits != null) {
                    decimalDigits = decimalDigits.trim();
                }
                String isNullable = rs.getString("IS_NULLABLE");//$NON-NLS-1$
                if (isNullable != null) {
                    isNullable = isNullable.trim();
                }
                if (isNullable != null && isNullable.equalsIgnoreCase("NO")) { //$NON-NLS-1$
                    isNullable = "YES"; //$NON-NLS-1$
                } else {
                    isNullable = "NO"; //$NON-NLS-1$
                }
                // String keys = rs.getString("keys");
                String remarks = rs.getString("REMARKS");//$NON-NLS-1$
                if (remarks != null) {
                    remarks = remarks.trim();
                }
                String columnDef = rs.getString("COLUMN_DEF");//$NON-NLS-1$
                if (columnDef != null) {
                    columnDef = columnDef.trim();
                }
                String sourceDataType = rs.getString("SOURCE_DATA_TYPE");//$NON-NLS-1$
                if (sourceDataType != null) {
                    sourceDataType = sourceDataType.trim();
                }
                String[] r = new String[] { tableName, columnName, typeName, columnSize, decimalDigits, isNullable, // keys
                        // ,
                        remarks, columnDef, sourceDataType };
                list.add(r);
                load = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
        }

        // For Calculation View
        if (!load) {
            String sqlcv = "SELECT * from \"" + schemaPattern + "\".\"" + tableNamePattern + "\""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            ResultSet rscv = null;
            Statement stmtcv = null;
            List<String[]> listcv = new ArrayList<String[]>();
            try {
                stmtcv = connection.createStatement();
                rscv = stmtcv.executeQuery(sqlcv);
                int i = 1;
                while (rscv.next()) {
                    String tableName = tableNamePattern;
                    String columnName = rscv.getMetaData().getColumnName(i);
                    String typeName = rscv.getMetaData().getColumnTypeName(i);
                    int columnCount = rscv.getMetaData().getColumnCount();
                    String columnSize = String.valueOf(columnCount);
                    String decimalDigits = String.valueOf(rscv.getMetaData().getPrecision(i));
                    String isNullable = String.valueOf(rscv.getMetaData().isNullable(i));
                    // fill default value if null
                    if (typeName == null) {
                        typeName = "CV"; //$NON-NLS-1$
                    }
                    if (columnSize == null) {
                        columnSize = "255";//$NON-NLS-1$
                    }
                    if (decimalDigits == null) {
                        decimalDigits = "0";//$NON-NLS-1$
                    }
                    String remarks = ""; //$NON-NLS-1$
                    String columnDef = ""; //$NON-NLS-1$

                    String[] r = new String[] { tableName, columnName, typeName, columnSize, decimalDigits, isNullable, remarks,
                            columnDef };
                    listcv.add(r);
                    if (i == columnCount) {
                        break;
                    }
                    i++;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    rscv.close();
                    stmtcv.close();
                } catch (Exception e) {
                }
            }
            list.addAll(listcv);
        }

        SAPHanaResultSet tableResultSet = new SAPHanaResultSet();
        tableResultSet.setMetadata(COLUMN_META);
        tableResultSet.setData(list);
        return tableResultSet;
    }
}
