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
package org.talend.utils.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.talend.utils.security.CryptoHelper;
import org.talend.utils.sugars.ReturnCode;

/**
 * Utility class for database connection handling.
 */
public final class ConnectionUtils {

    private static Logger log = Logger.getLogger(ConnectionUtils.class);

    public static final String PASSPHRASE = CryptoHelper.PASSPHRASE;

    private static List<String> sybaseDBProductsNames;

    public static final String IBM_DB2_ZOS_PRODUCT_NAME = "DB2"; //$NON-NLS-1$

    public static final String POSTGRESQL_PRODUCT_NAME = "POSTGRESQL"; //$NON-NLS-1$

    public static final String SYBASE_PRODUCT_NAME = "SYBASE"; //$NON-NLS-1$

    public static final String SYBASE_LANGUAGE = "Adaptive Server Enterprise | Sybase Adaptive Server IQ"; //$NON-NLS-1$

    private static final String ACCESS_DRIVER = "Microsoft Access Driver"; //$NON-NLS-1$

    public static final String SHUTDOWN_PARAM = ";shutdown=true"; //$NON-NLS-1$

    /**
     * private constructor.
     */
    private ConnectionUtils() {
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
     * @deprecated use 'ExtractMetaDataUtils.connect(...)' instead.
     */
    @Deprecated
    public static Connection createConnection(String url, String driverClassName, Properties props) throws SQLException,
            InstantiationException, IllegalAccessException, ClassNotFoundException {
        Driver driver = (Driver) Class.forName(driverClassName).newInstance();
        // bug TDI-23197 is caused by commit 92079
        return createConnection(url, driver, props);
    }

    /**
     * zshen Method "createConnection".
     * 
     * @param url the database url
     * @param driver Driver of database and can't be null
     * @param props properties passed to the driver manager for getting the connection (normally at least a "user" and
     * "password" property should be included)
     * @return the connection,it is can be null when the driver is null
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @deprecated use 'ExtractMetaDataUtils.connect(...)' instead.
     */
    @Deprecated
    public static Connection createConnection(String url, Driver driver, Properties props) throws SQLException,
            InstantiationException, IllegalAccessException, ClassNotFoundException {
        // MOD sizhaoliu TDI-23409 revert this method to previeus version of 92079 to fix the bug.
        // TDI-23197 was fixed separately and can be resolved by this revert too. the method isAccess() is kept.
        Connection connection = null;
        if (driver != null) {
            try {
                DriverManager.registerDriver(driver);
                Class.forName(driver.getClass().getName());
                if (isMsSqlServer(url) || isSybase(url) || isHiveServer(url)) {
                    connection = driver.connect(url, props);
                } else {
                    connection = DriverManager.getConnection(url, props);
                }
            } catch (ClassNotFoundException e) {// MOD zshen for mssql2008
                try {
                    connection = driver.connect(url, props);
                } catch (Exception exception) {
                    log.info(exception);
                }
            }
        } else {
            // add for odbc mssql: driver is null
            if (isODBCServer(url)) {
                connection = DriverManager.getConnection(url, props);
            }
        }
        return connection;
    }

    /**
     * if the url include "odbc", return true, else return false
     * 
     * @param url
     * @return
     */
    private static boolean isODBCServer(String url) {
        return url.startsWith("jdbc:odbc:"); //$NON-NLS-1$
    }

    /**
     * 
     * from the url to check whether it is jdbc.
     * 
     * @param url
     * @return
     */
    public static boolean isJDBCURL(String url) {
        return url != null && url.startsWith("jdbc:"); //$NON-NLS-1$
    }

    /**
     * from the database type to check whether it is JDBC
     * 
     * @param dbType
     * @return
     */
    public static boolean isJDBCType(String dbType) {
        return "JDBC".equals(dbType); //$NON-NLS-1$
    }

    /**
     * if the url include "sybase", return true, else return false
     * 
     * @param url
     * @return
     */
    public static boolean isSybase(String url) {
        return url.indexOf("sybase") > -1; //$NON-NLS-1$
    }

    /**
     * if the url include "sqlserver", return true, else return false
     * 
     * @param url
     * @return
     */
    public static boolean isMsSqlServer(String url) {
        return url.indexOf("sqlserver") > -1; //$NON-NLS-1$
    }

    public static boolean isHiveServer(String url) {
        return url.indexOf("hive") > -1; //$NON-NLS-1$
    }

    public static boolean isHiveConnection(String url) {
        return url != null && url.startsWith("jdbc:hive"); //$NON-NLS-1$
    }

    /**
     * Method "isAccess".
     * 
     * @param url
     * @return if the url is a Access url
     */
    public static boolean isAccess(String url) {
        if (url != null && url.contains(ACCESS_DRIVER)) {
            return true;
        }
        return false;
    }

    /**
     * from the url to check whether it is hsql.
     * 
     * @param url
     * @return
     */
    public static boolean isHsql(String url) {
        return url != null && url.startsWith("jdbc:hsqldb"); //$NON-NLS-1$
    }

    public static boolean isInProcessModeHsql(String url) {
        return url != null && url.startsWith("jdbc:hsqldb:file"); //$NON-NLS-1$
    }

    /**
     * add ";shutdown=true" to the end of the hsql url when the url don't contain it. this is only used for hsql
     * database.(ConnectionUtils.isHsql(url))
     * 
     * @param url
     * @param AdditionalParams
     * @return String the url after added
     */
    public static String addShutDownForHSQLUrl(String url, String AdditionalParams) {
        String dbUrl = url;
        if (AdditionalParams.indexOf(SHUTDOWN_PARAM) == -1) {
            dbUrl = dbUrl + SHUTDOWN_PARAM;
        }
        return dbUrl;
    }

    /**
     * execute ShutDown For HSQL connection.
     * 
     * @param connection
     * @throws SQLException
     */
    public static void executeShutDownForHSQL(java.sql.Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("SHUTDOWN;");//$NON-NLS-1$
        statement.close();
    }

    /**
     * from the url to check whether the hsql is Server Mode.
     * 
     * @param url
     * @return
     */
    public static boolean isServerModeHsql(String url) {
        return url != null && url.startsWith("jdbc:hsqldb:hsql"); //$NON-NLS-1$
    }

    public static boolean isTeradata(String url) {
        return url != null && url.startsWith("jdbc:teradata"); //$NON-NLS-1$
    }

    public static boolean isVertica(String url) {
        return url != null && url.startsWith("jdbc:vertica"); //$NON-NLS-1$
    }

    /**
     * Method "isValid".
     * 
     * @param connection the connection to test
     * @return a return code with the appropriate message (never null)
     */
    public static ReturnCode isValid(final Connection connection) {
        ReturnCode retCode = new ReturnCode();
        if (connection == null) {
            retCode.setReturnCode("Connection is null!", false); //$NON-NLS-1$
            return retCode;
        }

        ResultSet ping = null;
        try {
            if (connection.isClosed()) {
                retCode.setReturnCode("Connection is closed", false); //$NON-NLS-1$
                return retCode;
            }

            // do something so that exception is thrown is database connection failed
            connection.getAutoCommit();

            return retCode;
        } catch (SQLException sqle) {
            retCode.setReturnCode("SQLException caught:" + sqle.getMessage() + " SQL error code: " + sqle.getErrorCode(), false); //$NON-NLS-1$ //$NON-NLS-2$
            return retCode;
        } finally {
            if (ping != null) {
                try {
                    ping.close();
                } catch (Exception e) {
                    // do nothing
                }
            }
        }

    }

    /**
     * Method "closeConnection".
     * 
     * @param connection the connection to close.
     * @return a ReturnCode with true if ok, false if problem. {@link ReturnCode#getMessage()} gives the error message
     * when there is a problem.
     */
    public static ReturnCode closeConnection(final Connection connection) {
        assert connection != null;
        ReturnCode rc = new ReturnCode(true);
        try {
            if (connection != null && !connection.isClosed()) {
                if (connection.getMetaData() != null) {
                    String url = connection.getMetaData().getURL();
                    // when it is In HSQL server mode, we don't execute ShutDown.
                    // only shutdown in-process mode
                    if (isInProcessModeHsql(url)) {
                        executeShutDownForHSQL(connection);
                    }
                }
            }
        } catch (SQLException e) {
            rc.setReturnCode("Failed to close connection. Reason: " + e.getMessage(), false); //$NON-NLS-1$
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                log.warn(e, e);
            }
        }
        return rc;
    }

    /**
     * DOC zshen Comment method "isSybase".
     * 
     * @param connection
     * @return decide to whether is sybase connection
     * @throws SQLException
     */
    public static boolean isSybase(java.sql.Connection connection) throws SQLException {
        // DatabaseMetaData connectionMetadata = getConnectionMetadata(connection);
        DatabaseMetaData connectionMetadata = connection.getMetaData();
        if (connectionMetadata.getDriverName() != null && connectionMetadata.getDatabaseProductName() != null) {
            for (String keyString : getSybaseDBProductsName()) {
                if (keyString.equals(connectionMetadata.getDatabaseProductName().trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @deprecated use ExtractMetaDataUtils#getConnectionMetadata(java.sql.Connection conn) instead.
     * 
     * xqliu method "getConnectionMetadata". 2009-07-13 bug 7888.
     * 
     * @param conn
     * @return
     * @throws SQLException
     */
    @Deprecated
    public static DatabaseMetaData getConnectionMetadata(java.sql.Connection conn) throws SQLException {
        DatabaseMetaData dbMetaData = conn.getMetaData();
        // MOD xqliu 2009-11-17 bug 7888
        if (dbMetaData != null && dbMetaData.getDatabaseProductName() != null
                && dbMetaData.getDatabaseProductName().equals(IBM_DB2_ZOS_PRODUCT_NAME)) {
            dbMetaData = conn.getMetaData(); // FIXME why do we call this method a second time?
            log.info("IBM DB2 for z/OS"); //$NON-NLS-1$
        }
        // ~
        return dbMetaData;
    }

    /**
     * Comment method "isDB2".
     * 
     * @param metadata
     * @return
     * @throws SQLException
     */
    public static boolean isDB2(DatabaseMetaData metadata) throws SQLException {
        if (metadata != null && metadata.getDatabaseProductName() != null
                && metadata.getDatabaseProductName().indexOf(IBM_DB2_ZOS_PRODUCT_NAME) > -1) {
            return true;
        }
        return false;
    }

    public static boolean isOracleForSid(DatabaseMetaData metadata, String oracleProduct) throws SQLException {
        if (metadata != null && metadata.getDatabaseProductName() != null
                && metadata.getDatabaseProductName().indexOf(oracleProduct) > -1) {
            return true;
        }
        return false;
    }

    public static boolean isSqlite(DatabaseMetaData metadata, String sqliteProduct) throws SQLException {
        if (metadata != null && metadata.getDatabaseProductName() != null
                && metadata.getDatabaseProductName().toUpperCase().indexOf(sqliteProduct) > -1) {
            return true;
        }
        return false;
    }

    public static boolean isPostgresql(DatabaseMetaData metadata) {
        boolean result = false;
        try {
            if (metadata != null && metadata.getDatabaseProductName() != null
                    && metadata.getDatabaseProductName().toUpperCase().indexOf(POSTGRESQL_PRODUCT_NAME) > -1) {
                result = true;
            }
        } catch (SQLException e) {
            result = false;
        }
        return result;
    }

    public static boolean isSybase(DatabaseMetaData metadata) {
        boolean result = false;
        // if it is sybase database, it will be the fake DatabaseMetaData
        if (metadata != null && "org.talend.commons.utils.database.SybaseDatabaseMetaData".equals(metadata.getClass().getName())) {//$NON-NLS-1$
            result = true;
        }
        return result;
    }

    /**
     * 
     * DOC Comment method "isExasol".
     * 
     * @param metadata
     * @return
     * @throws SQLException
     */
    public static boolean isExasol(DatabaseMetaData metadata) throws SQLException {
        if (metadata != null && metadata.getDriverName() != null
                && metadata.getDriverName().toLowerCase().startsWith("exasol") //$NON-NLS-1$
                && metadata.getDatabaseProductName() != null
                && metadata.getDatabaseProductName().toLowerCase().startsWith("exasol")) { //$NON-NLS-1$
            return true;
        }
        return false;
    }

    /**
     * yyi 2010-08-25 for 14851, Sybase DB has several names with different productions and versions. For example the
     * Sybase IQ with version 12.6 is called 'Sybase' getting by JDBC but the version 15+ it is changed to 'Sybase IQ'.
     * it is user by org.talend.cwm.db.connection.ConnectionUtils.isSybase
     * 
     * @return All Sybase DB products name ,"Adaptive Server Enterprise","Sybase Adaptive Server IQ"
     * ,"Sybase IQ","Sybase"
     */
    public static String[] getSybaseDBProductsName() {
        if (null == sybaseDBProductsNames) {
            sybaseDBProductsNames = new ArrayList<String>();
            for (String name : SYBASE_LANGUAGE.split("\\|")) { //$NON-NLS-1$
                sybaseDBProductsNames.add(name.trim());
            }
            sybaseDBProductsNames.add("Sybase"); //$NON-NLS-1$
            sybaseDBProductsNames.add("Sybase IQ"); //$NON-NLS-1$
            sybaseDBProductsNames.add("Adaptive Server Enterprise | Sybase Adaptive Server IQ"); //$NON-NLS-1$
        }
        return sybaseDBProductsNames.toArray(new String[sybaseDBProductsNames.size()]);
    }

    /**
     * DOC klliu Comment method "isOdbcTeradata".
     * 
     * @param metadata
     * @return
     * @throws SQLException
     */
    public static boolean isOdbcTeradata(DatabaseMetaData metadata) throws SQLException {
        if (metadata.getDriverName() != null
                && metadata.getDriverName().toLowerCase().startsWith("jdbc-odbc bridge (tdata32.dll)") //$NON-NLS-1$
                && metadata.getDatabaseProductName() != null
                && metadata.getDatabaseProductName().toLowerCase().indexOf("teradata") > -1) { //$NON-NLS-1$
            return true;
        }
        return false;
    }

    public static boolean isOdbcHyperFileSQL(DatabaseMetaData metadata) {
        try {
            if (metadata.getDriverName() != null
                    && metadata.getDriverName().toLowerCase().startsWith("jdbc-odbc bridge") //$NON-NLS-1$
                    && metadata.getDatabaseProductName() != null
                    && metadata.getDatabaseProductName().toLowerCase().indexOf("hyperfilesql") > -1) { //$NON-NLS-1$
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    /**
     * return true if it is netezza.
     * 
     * @param databaseMetaData
     * @return
     * @throws SQLException
     */
    public static boolean isNetezza(DatabaseMetaData databaseMetaData) {

        try {
            if (databaseMetaData != null
                    && databaseMetaData.getDriverName() != null
                    && databaseMetaData.getDriverName().toLowerCase().startsWith("netezza") //$NON-NLS-1$
                    && databaseMetaData.getDatabaseProductName() != null
                    && databaseMetaData.getDatabaseProductName().toLowerCase().startsWith("netezza")) { //$NON-NLS-1$
                return true;
            }
        } catch (SQLException e) {
            log.warn(e);
        }
        return false;
    }

    public static boolean isMysql(DatabaseMetaData metadata) throws SQLException {
        boolean result = false;
        try {
            if (metadata != null && metadata.getDriverName() != null && metadata.getDatabaseProductName() != null
                    && metadata.getDatabaseProductName().toUpperCase().indexOf("MYSQL") > -1) { //$NON-NLS-1$
                result = true;
            }
        } catch (SQLException e) {
            result = false;
        }
        return result;
    }
}
