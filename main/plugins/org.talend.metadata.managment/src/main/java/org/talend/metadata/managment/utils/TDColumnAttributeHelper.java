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
package org.talend.metadata.managment.utils;

import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.talend.commons.utils.database.DB2ForZosDataBaseMetadata;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.MappingTypeRetriever;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.TableHelper;
import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdExpression;
import org.talend.cwm.relational.TdSqlDataType;
import org.talend.cwm.relational.TdTable;
import org.talend.utils.sql.ConnectionUtils;
import org.talend.utils.sql.metadata.constants.GetColumn;
import org.talend.utils.sugars.ReturnCode;
import org.talend.utils.sugars.TypedReturnCode;

import orgomg.cwm.resource.relational.enumerations.NullableType;

/**
 * DOC zshen class global comment. Detailled comment
 */
public class TDColumnAttributeHelper {

    private static final String ODBC_DRIVER_NAME = "jdbc-odbc";

    private static final String ODBC_MSSQL_PRODUCT_NAME = "Microsoft SQL Server";

    private static final String IBM_DB2_ZOS_PRODUCT_NAME = "DB2";

    private static DatabaseConnection databaseconnection = null;

    private static Logger log = Logger.getLogger(TDColumnAttributeHelper.class);

    public static TdTable addTagVale(ResultSet resutSet, TdTable table) {
        if (table == null) {
            table = RelationalFactory.eINSTANCE.createTdTable();
        }
        String colComment = null;
        try {
            colComment = resutSet.getString(GetColumn.REMARKS.name());
        } catch (Exception e) {
            log.warn(e, e);
        }
        if (colComment == null) {
            colComment = ""; //$NON-NLS-1$
        }
        TableHelper.setComment(colComment, table);
        return table;
    }

    /**
     * 
     * DOC Administrator Comment method "addColumnAttribute".
     * 
     * @deprecated
     * @param resutSet
     * @param column
     * @return
     * @throws SQLException
     * 
     */
    @Deprecated
    public static TdColumn addColumnAttribute(ResultSet resutSet, TdColumn column) throws SQLException {
        Connection conn = ConnectionHelper.getConnection(column);
        boolean isMssql = isMssql(createConnection((DatabaseConnection) conn).getObject());
        return addColumnAttribute(null, null, null, -1, -1, null, resutSet, column, (java.sql.Connection) null, isMssql);
    }

    public static TdColumn addColumnAttribute(String label, String columnName, String typeName, int columnSize, int decimalDigts,
            String columnRemark, ResultSet resutSet, TdColumn column, DatabaseConnection conn) throws SQLException {
        databaseconnection = conn;
        return addColumnAttribute(label, columnName, typeName, columnSize, decimalDigts, columnRemark, resutSet, column,
                createConnection(conn).getObject(), isMssql());

    }

    public static TdColumn addColumnAttribute(ResultSet resutSet, TdColumn column, DatabaseConnection conn) throws SQLException {
        databaseconnection = conn;
        return addColumnAttribute(null, null, null, -1, -1, null, resutSet, column, createConnection(conn).getObject(), isMssql());

    }

    public static TdColumn addColumnAttribute(ResultSet resutSet, TdColumn column, java.sql.Connection conn) throws SQLException {
        return addColumnAttribute(null, null, null, -1, -1, null, resutSet, column, conn, isMssql(conn));
    }

    public static TdColumn addColumnAttribute(String label, String columnName, String typeName, int columnSize,
            int decimalDigits, String columnRemark, ResultSet resutSet, TdColumn column, java.sql.Connection conn)
            throws SQLException {
        return addColumnAttribute(label, columnName, typeName, columnSize, decimalDigits, columnRemark, resutSet, column, conn,
                isMssql());

    }

    private static TdColumn addColumnAttribute(String label, String columnName, String typeName, int columnSize,
            int decimalDigits, String columnRemark, ResultSet resutSet, TdColumn column, java.sql.Connection conn, boolean isMssql)
            throws SQLException {
        boolean isIBMDB2ZOS = false;
        boolean isTeradataSqlModel = false;
        boolean isSAS = false;
        if (databaseconnection != null) {
            String dbMetaData = databaseconnection.getDatabaseType();
            if (dbMetaData != null && dbMetaData.equals(EDatabaseTypeName.IBMDB2ZOS.getDisplayName())) {
                isIBMDB2ZOS = true;
            }
            if (dbMetaData != null && dbMetaData.equals(EDatabaseTypeName.TERADATA.getDisplayName())
                    && databaseconnection.isSQLMode()) {
                isTeradataSqlModel = true;
            }
            if (dbMetaData != null && dbMetaData.equals(EDatabaseTypeName.SAS.getDisplayName())) {
                isSAS = true;
            }
        }
        // // --- add columns to table
        // ResultSet columns = getConnectionMetadata(connection).getColumns(catalogName, schemaPattern, tablePattern,
        // columnPattern);

        // TODO scorreia other informations for columns can be retrieved here
        // get the default value
        // MOD mzhao 2009-04-09,Bug 6840: fetch LONG or LONG RAW column first , as these kind of columns are read as
        // stream,if not read by select order, there will be "Stream has already been closed" error.
        // Don't move the below block ,if you move it that emerge up bug klliu 2010-09-07
        if (column == null) {
            column = RelationalFactory.eINSTANCE.createTdColumn();
        }
        Object defaultvalue = null;
        try {
            defaultvalue = resutSet.getObject(GetColumn.COLUMN_DEF.name());
        } catch (Exception e1) {
            log.warn(e1, e1);
        }
        String defaultStr = (defaultvalue != null) ? String.valueOf(defaultvalue) : null;
        TdExpression defExpression = createTdExpression(GetColumn.COLUMN_DEF.name(), defaultStr);

        // columnName
        // MOD xqliu 2009-10-29 bug 9838
        // MOD xqliu 2009-12-08 bug 9822, if use odbc connect to sqlserver the resultset is forward only!!!
        if (columnName == null || "".equals(columnName)) {
            try {
                columnName = resutSet.getString(GetColumn.COLUMN_NAME.name());
            } catch (Exception e1) {
                log.warn(e1, e1);
                if (columnName == null) {
                    columnName = e1.getMessage();
                }
            }
        }
        column.setLabel(label);
        column.setName(columnName);

        // dataType
        int dataType = 0;
        try {
            if (isIBMDB2ZOS || isTeradataSqlModel || isSAS) {
                dataType = resutSet.getInt(GetColumn.TYPE_NAME.name());
            } else {
                dataType = resutSet.getInt(GetColumn.DATA_TYPE.name());
            }
            // MOD scorreia 2010-07-24 removed the call to column.getSQLDataType() here because obviously the sql
            // data type it is null and results in a NPE
        } catch (Exception e) {
            log.warn(e, e);
        }

        // typeName
        if (typeName == null || "".equals(typeName)) {
            try {
                typeName = resutSet.getString(GetColumn.TYPE_NAME.name());
            } catch (Exception e1) {
                log.warn(e1, e1);
            }
        }
        // MOD zshen when the database is mssql,the datatype for "date" and "time" is "-9" and "-2"
        // ,respective.so change them to "91" and "92" for adapt to Java2SqlType.
        if (typeName != null && isMssql()) {
            if (typeName.toLowerCase().equals("date")) {
                dataType = 91;
                // MOD scorreia 2010-07-24 removed the call to column.getSQLDataType() here because obviously
                // the sql
                // data type it is null and results in a NPE
            } else if (typeName.toLowerCase().equals("time")) {
                dataType = 92;
                // MOD scorreia 2010-07-24 removed the call to column.getSQLDataType() here because obviously
                // the sql
                // data type it is null and results in a NPE
            }
        }

        // columnSize
        if (columnSize < 0) {
            try {
                columnSize = resutSet.getInt(GetColumn.COLUMN_SIZE.name());
            } catch (Exception e1) {
                log.warn(e1, e1);
            }
        }

        column.setLength(columnSize);

        // decimalDigts
        if (decimalDigits < 0) {
            try {
                decimalDigits = resutSet.getInt(GetColumn.DECIMAL_DIGITS.name());
            } catch (Exception e) {
                log.warn(e);
            }
        }

        //
        int numPrecRadix = 0;
        try {
            if (!isIBMDB2ZOS && !isTeradataSqlModel && !isSAS) {
                numPrecRadix = resutSet.getInt(GetColumn.NUM_PREC_RADIX.name());
            }
        } catch (Exception e) {
            log.warn(e);
        }

        // get column description (comment)
        if (columnRemark == null || "".equals(columnRemark)) {
            try {
                columnRemark = resutSet.getString(GetColumn.REMARKS.name());
            } catch (Exception e) {
                log.warn(e, e);
            }
            if (columnRemark == null) {
                columnRemark = "";
            }
            columnRemark = ManagementTextUtils.filterSpecialChar(columnRemark);
        }
        ColumnHelper.setComment(columnRemark, column);

        // --- create and set type of column
        // TODO scorreia get type of column on demand, not on creation of column
        // TdSqlDataType sqlDataType = DatabaseContentRetriever.createDataType(columns);
        TdSqlDataType sqlDataType = createDataType(dataType, typeName, decimalDigits, numPrecRadix);
        column.setSqlDataType(sqlDataType);
        // column.setType(sqlDataType); // it's only reference to previous sql data type
        try {
            if (isIBMDB2ZOS || isTeradataSqlModel || isSAS) {
                column.getSqlDataType().setNullable(NullableType.get(resutSet.getInt(GetColumn.IS_NULLABLE.name())));
            } else {
                column.getSqlDataType().setNullable(NullableType.get(resutSet.getInt(GetColumn.NULLABLE.name())));
            }
        } catch (Exception e1) {
            log.warn(e1, e1);
        }

        column.setInitialValue(defExpression);
        String mapping = databaseconnection == null ? null : databaseconnection.getDbmsId();
        if (databaseconnection != null && mapping != null) {
            MappingTypeRetriever mappingTypeRetriever = MetadataTalendType.getMappingTypeRetriever(mapping);
            // in case we're using the SQLBuilder, the mapping might be null (for JDBC)
            if (mappingTypeRetriever != null) {
                String talendType = mappingTypeRetriever
                        .getDefaultSelectedTalendType(
                                typeName,
                                ExtractMetaDataUtils.getInstance().getIntMetaDataInfo(resutSet, "COLUMN_SIZE"), ExtractMetaDataUtils.getInstance().getIntMetaDataInfo(resutSet, //$NON-NLS-1$
                                                "DECIMAL_DIGITS")); //$NON-NLS-1$
                column.setTalendType(talendType);
            }
            // ADD xqliu 2010-12-28 bug 16538
            /* should set truely source type,hywang */

            // for bug 16816

            //            String type = "TYPE_NAME"; //$NON-NLS-1$
            // if (ExtractMetaDataUtils.isUseAllSynonyms()) {
            //                type = "DATA_TYPE"; //$NON-NLS-1$
            // }
            //            String dbType = ExtractMetaDataUtils.getStringMetaDataInfo(resutSet, type, null).toUpperCase(); //$NON-NLS-1$
            // dbType = ManagementTextUtils.filterSpecialChar(dbType);
            // dbType = ExtractMetaDataFromDataBase.handleDBtype(dbType);
            // column.setSourceType(dbType);

            // column.setSourceType(MetadataTalendType.getMappingTypeRetriever(databaseconnection.getDbmsId())
            // .getDefaultSelectedDbType(talendType));
        }
        // ADD xqliu 2010-12-28 bug 16538
        try {
            column.setNullable("YES".equals(resutSet.getString(GetColumn.IS_NULLABLE.name()))); //$NON-NLS-1$ 
            // ~ 16538
        } catch (Exception e) {
            // for ORACLE synonyms
            column.setNullable(false);
        }
        return column;
    }

    /**
     * DOC xqliu Comment method "createDataType".
     * 
     * @param dataType
     * @param typeName
     * @param decimalDigits
     * @param numPrecRadix
     * @return
     */
    private static TdSqlDataType createDataType(int dataType, String typeName, int decimalDigits, int numPrecRadix) {
        TdSqlDataType sqlDataType = RelationalFactory.eINSTANCE.createTdSqlDataType();
        sqlDataType.setName(typeName);
        sqlDataType.setJavaDataType(dataType);
        sqlDataType.setNumericPrecision(decimalDigits);
        sqlDataType.setNumericPrecisionRadix(numPrecRadix);
        return sqlDataType;
    }

    /**
     * DOC zshen Comment method "isMssql".
     * 
     * @param connection
     * @return decide to whether is mssql connection
     * @throws SQLException
     */
    private static boolean isMssql(java.sql.Connection connection) throws SQLException {
        DatabaseMetaData connectionMetadata = getConnectionMetadata(connection);
        if (connectionMetadata.getDriverName() != null
                && !connectionMetadata.getDriverName().toLowerCase().startsWith(ODBC_DRIVER_NAME)
                && connectionMetadata.getDatabaseProductName() != null
                && connectionMetadata.getDatabaseProductName().equals(ODBC_MSSQL_PRODUCT_NAME)) {
            return true;
        }
        return false;
    }

    /**
     * DOC zshen Comment method "isMssql".
     * 
     * 
     * @return decide to whether is mssql connection
     * @throws SQLException
     */
    private static boolean isMssql() throws SQLException {
        String dbtype = databaseconnection == null ? null : databaseconnection.getDatabaseType();
        if (dbtype != null) {
            return dbtype.contains(ODBC_MSSQL_PRODUCT_NAME);
        }
        return false;
    }

    /**
     * DOC xqliu Comment method "getConnectionMetadata". 2009-07-13 bug 7888.
     * 
     * @param conn
     * @return
     * @throws SQLException
     */

    private static DatabaseMetaData getConnectionMetadata(java.sql.Connection conn) throws SQLException {
        DatabaseMetaData dbMetaData = conn.getMetaData();
        // MOD xqliu 2009-11-17 bug 7888
        if (dbMetaData != null && dbMetaData.getDatabaseProductName() != null
                && dbMetaData.getDatabaseProductName().equals(IBM_DB2_ZOS_PRODUCT_NAME)) {
            dbMetaData = createFakeDatabaseMetaData(conn);
            log.info("IBM DB2 for z/OS");
        }
        // ~
        return dbMetaData;
    }

    /**
     * only for db2 on z/os right now. 2009-07-13 bug 7888.
     * 
     * @param conn2
     * @return
     * @throws SQLException
     */
    private static DatabaseMetaData createFakeDatabaseMetaData(java.sql.Connection conn) throws SQLException {
        DB2ForZosDataBaseMetadata dmd = new DB2ForZosDataBaseMetadata(conn);
        return dmd;
    }

    /**
     * Method "createConnection" returns the connection with {@link ReturnCode#getObject()} if {@link ReturnCode#isOk()}
     * is true. This is the behaviour when everything goes ok.
     * <p>
     * When something goes wrong, {@link ReturnCode#isOk()} is false and {@link ReturnCode#getMessage()} gives the error
     * message.
     * <p>
     * The created connection must be closed by the caller. (use {@link ConnectionUtils#closeConnection(Connection)})
     * 
     * @param providerConnection the provider connection
     * @return a ReturnCode (never null)
     * 
     * getby JavaSqlFactory
     */
    private static TypedReturnCode<java.sql.Connection> createConnection(DatabaseConnection providerConnection) {
        TypedReturnCode localTypedReturnCode = new TypedReturnCode(false);
        return localTypedReturnCode;
    }

    /**
     * Method "createConnection".
     * 
     * @param url the database url
     * @param driverClassName the Driver classname
     * @param props properties passed to the driver manager for getting the connection (normally at least a "user" and
     * "password" property should be included)
     * @return the connection
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    private static java.sql.Connection createConnection(String url, String driverClassName, Properties props)
            throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        Driver driver = (Driver) Class.forName(driverClassName).newInstance();
        DriverManager.registerDriver(driver);
        java.sql.Connection connection = DriverManager.getConnection(url, props);
        return connection;
    }

    private static TdExpression createTdExpression(String language, String body) {
        TdExpression expression = RelationalFactory.eINSTANCE.createTdExpression();
        expression.setBody(body);
        expression.setLanguage(language);
        return expression;
    }
}
