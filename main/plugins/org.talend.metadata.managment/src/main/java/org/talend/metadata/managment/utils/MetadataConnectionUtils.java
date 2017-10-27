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
package org.talend.metadata.managment.utils;

import java.io.UnsupportedEncodingException;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.rpc.ServiceException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.database.SybaseDatabaseMetaData;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.IRepositoryContextService;
import org.talend.core.database.EDatabase4DriverClassName;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.MetadataConnection;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.database.DriverShim;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.metadata.builder.database.IDriverService;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdSqlDataType;
import org.talend.cwm.xml.TdXmlElementType;
import org.talend.metadata.managment.connection.manager.HiveConnectionManager;
import org.talend.metadata.managment.connection.manager.ImpalaConnectionManager;
import org.talend.metadata.managment.mdm.AbsMdmConnectionHelper;
import org.talend.metadata.managment.mdm.S60MdmConnectionHelper;
import org.talend.metadata.managment.model.DBConnectionFillerImpl;
import org.talend.metadata.managment.model.MetadataFillFactory;
import org.talend.metadata.managment.repository.ManagerConnection;
import org.talend.utils.exceptions.MissingDriverException;
import org.talend.utils.sql.ConnectionUtils;
import org.talend.utils.sugars.ReturnCode;
import org.talend.utils.sugars.TypedReturnCode;

import orgomg.cwm.foundation.softwaredeployment.DataProvider;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC zshen class global comment. Detailled comment
 */
public class MetadataConnectionUtils {

    private static Logger log = Logger.getLogger(MetadataConnectionUtils.class);

    // MOD mzhao 2009-06-05 Bug 7571
    private static final Map<String, Driver> DRIVER_CACHE = new HashMap<String, Driver>();

    private static final SimpleDateFormat SMPL_DATE_FMT = new SimpleDateFormat("yyyyMMddhhmmss"); //$NON-NLS-1$

    private static final char CHAR_TO_REMOVE = '/';

    private static final char REPLACEMENT_CHAR = '_';

    private static final String DRIVER_EXTENSION_POINT_ID = "org.talend.metadata.managment.DBDriver_extension"; //$NON-NLS-1$

    private static final String TOP_DRIVER_EXTENSION_ID = "org.talend.dataprofiler.core.TOPDriverService"; //$NON-NLS-1$

    private static List<String> sybaseDBProductsNames;

    // MOD by zshen don't needed can replaced by ExtractMetaDataUtils.metadataCon
    // private static IMetadataConnection metadataCon;

    private static Driver derbyDriver;

    public static final String FAKE_SCHEMA_SYNONYMS = "AllSynonyms"; //$NON-NLS-1$

    /**
     * check a Connection and at last will close the connection.
     * 
     * @param metadataBean
     * @return
     */
    public static TypedReturnCode<java.sql.Connection> checkConnection(IMetadataConnection metadataBean) {
        return createConnection(metadataBean, true);
    }

    /**
     * create a Connection and without close it.
     * 
     * @param metadataBean
     * @return
     */
    public static TypedReturnCode<java.sql.Connection> createConnection(IMetadataConnection metadataBean) {
        return createConnection(metadataBean, false);
    }

    /**
     * create a Connection with the parameter whether close it.
     * 
     * @param metadataBean
     * @param boolean close Connection or not
     * @return
     */
    public static TypedReturnCode<java.sql.Connection> createConnection(IMetadataConnection metadataBean, boolean closeConnection) {
        TypedReturnCode<java.sql.Connection> rc = new TypedReturnCode<java.sql.Connection>();
        rc.setOk(false);
        if (metadataBean == null) {
            rc.setMessage("connection information can not be null"); //$NON-NLS-1$
            return rc;
        }

        if (EDatabaseTypeName.HIVE.getXmlName().equalsIgnoreCase(metadataBean.getDbType())) {
            try {
                // HiveConnectionManager.getInstance().checkConnection(metadataBean);
                java.sql.Connection createConnection = HiveConnectionManager.getInstance().createConnection(metadataBean);
                rc.setOk(true);
                rc.setObject(createConnection);
                rc.setMessage("Check hive connection successful!"); //$NON-NLS-1$
            } catch (Exception e) {
                rc.setOk(false);
                rc.setMessage("Check hive connection failed!"); //$NON-NLS-1$
                CommonExceptionHandler.process(e);
            }
        } else if (EDatabaseTypeName.IMPALA.getXmlName().equalsIgnoreCase(metadataBean.getDbType())) {
            try {
                java.sql.Connection createConnection = ImpalaConnectionManager.getInstance().createConnection(metadataBean);
                rc.setOk(true);
                rc.setObject(createConnection);
                rc.setMessage("Check Impala connection successful!"); //$NON-NLS-1$
            } catch (Exception e) {
                rc.setOk(false);
                rc.setMessage("Check Impala connection failed!"); //$NON-NLS-1$
                CommonExceptionHandler.process(e);
            }
        } else if (EDatabaseTypeName.HBASE.getXmlName().equalsIgnoreCase(metadataBean.getDbType())
                || EDatabaseTypeName.MAPRDB.getXmlName().equalsIgnoreCase(metadataBean.getDbType())) {
            rc.setOk(true);
        } else {
            String dbUrl = metadataBean.getUrl();
            if (ConnectionUtils.isHsql(dbUrl)) {
                dbUrl = ConnectionUtils.addShutDownForHSQLUrl(dbUrl, metadataBean.getAdditionalParams());
            }
            // TDQ-8893 Don't use ConnectionUtils.createConnection(...) to get a java sql connetcion,replace it with
            // ExtractMetaDataUtils.connect(...).
            java.sql.Connection sqlConn = null;
            List<Object> list = new ArrayList<Object>();
            try {
                list = ExtractMetaDataUtils.getInstance().connect(metadataBean.getDbType(), metadataBean.getUrl(),
                        metadataBean.getUsername(), metadataBean.getPassword(), metadataBean.getDriverClass(),
                        metadataBean.getDriverJarPath(), metadataBean.getDbVersionString(), metadataBean.getAdditionalParams());
            } catch (Exception e) {
                rc.setMessage("fail to connect database!"); //$NON-NLS-1$
                CommonExceptionHandler.process(e);
                return rc;
            }

            Driver driver = null;
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i) instanceof Driver) {
                        driver = (Driver) list.get(i);
                    }
                    if (list.get(i) instanceof java.sql.Connection) {
                        sqlConn = (java.sql.Connection) list.get(i);
                    }
                }
                ReturnCode varc = ConnectionUtils.isValid(sqlConn);
                if (varc.isOk()) {
                    derbyDriver = null;
                    if (driver != null && isDerbyRelatedDb(metadataBean.getDriverClass(), metadataBean.getDbType())) {
                        DBConnectionFillerImpl.setDriver(driver);
                        derbyDriver = driver;
                    }
                    rc.setObject(sqlConn);
                    rc.setMessage(varc.getMessage());
                    rc.setOk(true);
                }
            }
        }

        if (closeConnection) {
            if (rc.getObject() != null) {
                ConnectionUtils.closeConnection(rc.getObject());
            }
        }

        return rc;
    }

    /**
     * check a Connection and will at last close connection.
     * 
     * @param databaseConnection
     * @return
     */
    public static TypedReturnCode<java.sql.Connection> checkConnection(DatabaseConnection databaseConnection) {
        return createConnection(databaseConnection, true);
    }

    /**
     * create a Connection and will at last not close connection.
     * 
     * @param databaseConnection
     * @return
     */
    public static TypedReturnCode<java.sql.Connection> createConnection(DatabaseConnection databaseConnection) {
        return createConnection(databaseConnection, false);
    }

    /**
     * create a Connection whether close it depends on closeConnection.
     * 
     * @param databaseConnection
     * @param closeConnection
     * @return
     */
    public static TypedReturnCode<java.sql.Connection> createConnection(DatabaseConnection databaseConnection,
            boolean closeConnection) {

        // TODO: can we reuse the ConvertionHelper.convert method to get a metadataConnection
        IMetadataConnection metadataConnection;
        if (EDatabaseTypeName.HIVE.getXmlName().equalsIgnoreCase(databaseConnection.getDatabaseType())) {
            metadataConnection = ConvertionHelper.convert(databaseConnection);
        } else {
            metadataConnection = new MetadataConnection();
            String dbUrl = databaseConnection.getURL();
            String password = databaseConnection.getRawPassword();
            String userName = databaseConnection.getUsername();
            String dbType = databaseConnection.getDatabaseType();
            String driverClass = databaseConnection.getDriverClass() == null ? EDatabase4DriverClassName
                    .getDriverClassByDbType(dbType) : databaseConnection.getDriverClass();
            String driverJarPath = databaseConnection.getDriverJarPath();
            String dataBase = databaseConnection.getSID();
            String dbVersionString = databaseConnection.getDbVersionString();
            String additionalParams = databaseConnection.getAdditionalParams();

            // MOD qiongli 2011-9-6,TDQ 3317.handle context mode
            if (databaseConnection.isContextMode()) {
                IRepositoryContextService repositoryContextService = CoreRuntimePlugin.getInstance()
                        .getRepositoryContextService();
                if (repositoryContextService != null) {
                    String contextName = databaseConnection.getContextName();
                    DatabaseConnection origValueConn = null;
                    if (contextName == null) {
                        origValueConn = repositoryContextService.cloneOriginalValueConnection(databaseConnection, true);
                    } else {
                        origValueConn = repositoryContextService.cloneOriginalValueConnection(databaseConnection, false,
                                contextName);
                    }

                    if (origValueConn != null) {
                        dbUrl = origValueConn.getURL();
                        password = origValueConn.getRawPassword();
                        userName = origValueConn.getUsername();
                        driverClass = origValueConn.getDriverClass();
                        driverJarPath = origValueConn.getDriverJarPath();
                        dbType = origValueConn.getDatabaseType();
                        dataBase = origValueConn.getSID();
                        dbVersionString = origValueConn.getDbVersionString();
                        additionalParams = origValueConn.getAdditionalParams();
                    }

                }
            }// ~

            metadataConnection.setAdditionalParams(additionalParams);
            metadataConnection.setDbVersionString(dbVersionString);
            metadataConnection.setDatabase(dataBase);
            metadataConnection.setDbType(dbType);
            metadataConnection.setDriverJarPath(driverJarPath);
            metadataConnection.setDriverClass(driverClass);
            metadataConnection.setUsername(userName);
            metadataConnection.setPassword(password);
            metadataConnection.setUrl(dbUrl);

            // TDQ-12299: transfer the OtherParameters to metadataConnection, because create impala connection use that
            // values
            ConvertionHelper.convertOtherParameters(metadataConnection, databaseConnection);
        }
        return createConnection(metadataConnection, closeConnection);
    }

    /**
     * DOC xqliu Comment method "isOdbcPostgresql".
     * 
     * @param connection
     * @return
     * @throws SQLException
     */
    public static boolean isOdbcPostgresql(java.sql.Connection connection) throws SQLException {
        return isOdbcPostgresql(ExtractMetaDataUtils.getInstance().getConnectionMetadata(connection));
    }

    public static boolean isOdbcPostgresql(DatabaseMetaData connectionMetadata) throws SQLException {
        if (connectionMetadata.getDriverName() != null
                && connectionMetadata.getDriverName().toLowerCase().startsWith(DatabaseConstant.ODBC_DRIVER_NAME)
                && connectionMetadata.getDatabaseProductName() != null
                && connectionMetadata.getDatabaseProductName().toLowerCase().indexOf(DatabaseConstant.POSTGRESQL_PRODUCT_NAME) > -1) {
            return true;
        }
        return false;
    }

    /**
     * DOC zshen Comment method "isOdbcMssql". feature 10630
     * 
     * @param connection
     * @return
     * @throws SQLException
     */
    public static boolean isOdbcExcel(java.sql.Connection connection) throws SQLException {
        return isOdbcExcel(ExtractMetaDataUtils.getInstance().getConnectionMetadata(connection));
    }

    public static boolean isOdbcExcel(DatabaseMetaData connectionMetadata) throws SQLException {
        if (connectionMetadata.getDriverName() != null
                && connectionMetadata.getDriverName().toLowerCase().startsWith(DatabaseConstant.ODBC_DRIVER_NAME)
                && connectionMetadata.getDatabaseProductName() != null
                && connectionMetadata.getDatabaseProductName().equals(DatabaseConstant.ODBC_EXCEL_PRODUCT_NAME)) {
            return true;
        }
        return false;
    }

    /**
     * 
     * DOC qiongli Comment method "isAccess".
     * 
     * @param connection
     * @return
     * @throws SQLException
     */
    public static boolean isAccess(java.sql.Connection connection) throws SQLException {
        return isAccess(ExtractMetaDataUtils.getInstance().getConnectionMetadata(connection));
    }

    public static boolean isAccess(DatabaseMetaData connectionMetadata) throws SQLException {
        if (connectionMetadata.getDriverName() != null && connectionMetadata.getDatabaseProductName() != null
                && connectionMetadata.getDatabaseProductName().equals(DatabaseConstant.MS_ACCESS_PRODUCT_NAME)) {
            return true;
        }
        return false;
    }

    /**
     * DOC zshen Comment method "isSybase".
     * 
     * @param connection
     * @return decide to whether is sybase connection
     * @throws SQLException
     */
    public static boolean isSybase(java.sql.Connection connection) throws SQLException {
        return isSybase(ExtractMetaDataUtils.getInstance().getConnectionMetadata(connection));
    }

    public static boolean isSybase(DatabaseMetaData connectionMetadata) throws SQLException {
        if (connectionMetadata instanceof SybaseDatabaseMetaData) {
            return true;
        }
        if (connectionMetadata.getDriverName() != null && connectionMetadata.getDatabaseProductName() != null) {
            for (String keyString : getSybaseDBProductsName()) {
                if (keyString.trim().equals(connectionMetadata.getDatabaseProductName().trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * DOC xqliu Comment method "isTeradata".
     * 
     * @param connection
     * @return
     */
    public static boolean isTeradata(DatabaseConnection connection) {
        EDatabaseTypeName dbType = EDatabaseTypeName.getTypeFromDbType(connection.getDatabaseType());
        if (dbType == EDatabaseTypeName.TERADATA) {
            return true;
        }
        return false;
    }

    public static boolean isTeradataSQLMode(IMetadataConnection metadataConnection) {
        if (metadataConnection != null) {
            Object connection = metadataConnection.getCurrentConnection();
            if (connection != null && connection instanceof DatabaseConnection) {
                DatabaseConnection dbConn = (DatabaseConnection) connection;
                if (dbConn != null && isTeradata(dbConn) && dbConn.isSQLMode()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * zshen Comment method "isOdbcConnection". feature 10630
     * 
     * @param connection
     * @return
     * @throws SQLException
     */
    public static boolean isOdbcConnection(DatabaseMetaData connectionMetadata) throws SQLException {
        if (connectionMetadata.getDriverName() != null
                && connectionMetadata.getDriverName().toLowerCase().startsWith(DatabaseConstant.ODBC_DRIVER_NAME)) {
            return true;
        }
        return false;
    }

    public static boolean isOdbcConnection(java.sql.Connection connection) throws SQLException {
        return isOdbcConnection(ExtractMetaDataUtils.getInstance().getConnectionMetadata(connection));
    }

    /**
     * zshen Comment method "isODBCCatalog".
     * 
     * @param catalogName the name for need to be decided.
     * @param connection
     * @return if connection is a ODBC connection and catalogName isn't which be found then return ture, else return
     * true.
     * @throws SQLException
     */
    public static boolean isODBCCatalog(String catalogName, DatabaseMetaData connectionMetadata) throws SQLException {
        if (isOdbcConnection(connectionMetadata)) {
            String userCatalogName = connectionMetadata.getConnection().getCatalog();
            if (catalogName != null
                    && (catalogName.equals(userCatalogName) || userCatalogName == null || "null".equals(userCatalogName))) { //$NON-NLS-1$

                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public static boolean isOracle8i(Connection connection) {
        if (connection != null && connection instanceof DatabaseConnection) {
            DatabaseConnection dbConn = (DatabaseConnection) connection;
            String version = dbConn.getDbVersionString();
            if (version != null && version.equals("ORACLE_8")) { //$NON-NLS-1$
                return true;
            }
        }
        return false;
    }

    public static boolean isOracle(Connection connection) {
        if (connection != null && connection instanceof DatabaseConnection) {
            DatabaseConnection dbConn = (DatabaseConnection) connection;
            if (EDatabaseTypeName.ORACLEFORSID.getDisplayName().equals(dbConn.getDatabaseType())
                    || EDatabaseTypeName.ORACLESN.getDisplayName().equals(dbConn.getDatabaseType())
                    || EDatabaseTypeName.ORACLE_CUSTOM.getDisplayName().equals(dbConn.getDatabaseType())
                    || EDatabaseTypeName.ORACLE_OCI.getDisplayName().equals(dbConn.getDatabaseType())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isOracleJDBC(Connection connection) {
        if (connection != null && connection instanceof DatabaseConnection) {
            DatabaseConnection dbConn = (DatabaseConnection) connection;
            if (EDatabaseTypeName.GENERAL_JDBC.getDisplayName().equals(dbConn.getDatabaseType())) {
                if (dbConn.getDriverClass() != null
                        && dbConn.getDriverClass().startsWith(DatabaseConstant.ODBC_ORACLE_PRODUCT_NAME)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isMssql(DatabaseMetaData connectionMetadata) throws SQLException {
        if (connectionMetadata.getDriverName() != null && connectionMetadata.getDatabaseProductName() != null) {
            if (EDataBaseType.Microsoft_SQL_Server.getProductName().equals(connectionMetadata.getDatabaseProductName().trim())) {
                return true;

            }
        }
        return false;
    }

    public static boolean isMssql(java.sql.Connection connection) throws SQLException {
        return isMssql(ExtractMetaDataUtils.getInstance().getConnectionMetadata(connection));
    }

    public static boolean isMysql(java.sql.Connection connection) throws SQLException {
        return isMysql(ExtractMetaDataUtils.getInstance().getConnectionMetadata(connection));
    }

    public static boolean isMysql(DatabaseMetaData connectionMetadata) throws SQLException {
        if (connectionMetadata.getDriverName() != null && connectionMetadata.getDatabaseProductName() != null) {
            if (EDataBaseType.MySQL.getProductName().equals(connectionMetadata.getDatabaseProductName().trim())) {
                return true;

            }
        }
        return false;
    }

    /**
     * yyi 2010-08-25 for 14851, Sybase DB has several names with different productions and versions. For example the
     * Sybase IQ with version 12.6 is called 'Sybase' getting by JDBC but the version 15+ it is changed to 'Sybase IQ'.
     * it is user by org.talend.cwm.db.connection.ConnectionUtils.isSybase
     * 
     * @return All Sybase DB products name
     * ,"Adaptive Server Enterprise","Sybase Adaptive Server IQ","Sybase IQ","Sybase"
     */
    public static String[] getSybaseDBProductsName() {
        if (null == sybaseDBProductsNames) {
            sybaseDBProductsNames = new ArrayList<String>();
            sybaseDBProductsNames.add("Adaptive Server Enterprise"); //$NON-NLS-1$
            sybaseDBProductsNames.add(" Sybase Adaptive Server IQ"); //$NON-NLS-1$
            sybaseDBProductsNames.add("Sybase"); //$NON-NLS-1$
            sybaseDBProductsNames.add("Sybase IQ"); //$NON-NLS-1$
            sybaseDBProductsNames.add("Adaptive Server Enterprise | Sybase Adaptive Server IQ"); //$NON-NLS-1$
        }
        return sybaseDBProductsNames.toArray(new String[sybaseDBProductsNames.size()]);
    }

    /**
     * DOC qzhang Comment method "getClassDriver".
     * 
     * @param driverClassName
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public static Driver getClassDriver(IMetadataConnection metadataBean) throws InstantiationException, IllegalAccessException,
            ClassNotFoundException, RuntimeException {
        String driverClassName = metadataBean.getDriverClass();
        // MOD mzhao 2009-06-05,Bug 7571 Get driver from catch first, if not
        // exist then get a new instance.
        if (driverClassName.equals("org.apache.derby.jdbc.EmbeddedDriver")) { //$NON-NLS-1$
            DRIVER_CACHE.remove(driverClassName);
        } else if (EDatabase4DriverClassName.HIVE.getDriverClass().equals(driverClassName)) {
            // Added by Marvin Wang on Dec. 6, 2012 for bug TDI-24027, for hive it should load all driver jars each time
            // no matter embedded or standalone.
            DRIVER_CACHE.remove(driverClassName);
        }
        Driver driver = DRIVER_CACHE.get(driverClassName);

        if (!Platform.isRunning()) {
            driver = (Driver) Class.forName(driverClassName).newInstance();
        }

        // The case for generalJDBC
        String driverPath = metadataBean.getDriverJarPath();

        if (StringUtils.isEmpty(driverPath)) {
            if (driver != null) {
                return driver;
            }
        }

        driver = getDriver(metadataBean);

        // MOD mzhao 2009-06-05,Bug 7571 Get driver from catch first, if not
        // exist then get a new instance.
        if (driver != null) {
            DRIVER_CACHE.put(driverClassName, driver);
        }
        return driver;
    }

    public static Map<String, Driver> getDriverCache() {
        return DRIVER_CACHE;
    }

    /**
     * get driver.
     * 
     * @param metadataBean
     * @param driver
     * @return
     * @throws Exception
     */
    private static Driver getDriver(IMetadataConnection metadataBean) {
        Driver driver = null;
        List<?> connList = null;
        try {
            connList = getConnection(metadataBean);
            if (connList != null && !connList.isEmpty()) { // FIXME unnecessary check !connList.isEmpty()
                // FIXME scorreia 2011-03-31 why do we loop here? Is it possible to have several drivers. If
                // yes,
                // what do we do?
                for (int i = 0; i < connList.size(); i++) {
                    if (connList.get(i) instanceof Driver) {
                        driver = (DriverShim) connList.get(i); // FIXME scorreia 2011-03-31 strange cast here.
                    }
                }
            }
        } catch (MissingDriverException e) {
            throw e;
        } catch (Exception e) {
            log.error(e, e);
            // Added 20130521 TUP-735 yyin,when connection error should throw the exception to transfer the error
            // message
            throw new RuntimeException(e);
        } finally {
            // ADD msjian TDQ-5952: we should close the unused connection at once.
            if (connList != null && !connList.isEmpty()) {
                for (int i = 0; i < connList.size(); i++) {
                    if (connList.get(i) instanceof java.sql.Connection) {
                        java.sql.Connection con = (java.sql.Connection) connList.get(i);
                        ConnectionUtils.closeConnection(con);
                    }
                }
            }
            // TDQ-5952~
        }
        return driver;
    }

    /**
     * This method to get all database template supported by TDQ.
     * 
     * @return
     */
    public static List<String> getTDQSupportDBTemplate() {
        try {
            if (GlobalServiceRegister.getDefault().isDQDriverServiceRegistered(IDriverService.class)) {
                IDriverService driverService = (IDriverService) GlobalServiceRegister.getDefault().getDQDriverService(
                        IDriverService.class);

                return driverService.getTDQSupportDBTemplate();
            }
        } catch (Exception e) {
            log.error(e, e);
        }
        return new ArrayList<String>();
    }

    /**
     * This method try to return whether the conn is supported by TDQ.
     * 
     * @return
     */
    public static boolean isTDQSupportDBTemplate(Connection conn) {
        if (conn == null) {
            return false;
        }
        if (conn instanceof DatabaseConnection) {
            String databaseType = ((DatabaseConnection) conn).getDatabaseType();
            return getTDQSupportDBTemplate().contains(databaseType);
        }

        return false;
    }

    /**
     * 
     * DOC zshen Comment method "getCommonQueryStr".
     * 
     * @param sqlConn
     * @return null only if conn is null.or can not find the
     * @deprecated {@link #getCommentQueryStr(String, String, String)} instead.
     */
    @Deprecated
    public static String getCommonQueryStr(String productName, String tableName) {
        if (productName == null) {
            return null;
        }
        productName = productName.replaceAll(" ", "_"); //$NON-NLS-1$ //$NON-NLS-2$
        EDataBaseType eDataBaseType = null;
        try {
            eDataBaseType = EDataBaseType.valueOf(productName);
        } catch (Exception e) {
            eDataBaseType = EDataBaseType.Microsoft_SQL_Server;
        }
        String sqlStr = ""; //$NON-NLS-1$
        switch (eDataBaseType) {
        case Oracle:
            sqlStr = "SELECT COMMENTS FROM USER_TAB_COMMENTS WHERE TABLE_NAME='" + tableName + "'"; //$NON-NLS-1$ //$NON-NLS-2$
            break;
        case MySQL:
            sqlStr = "SELECT TABLE_COMMENT FROM information_schema.TABLES WHERE TABLE_NAME='" + tableName + "'"; //$NON-NLS-1$ //$NON-NLS-2$
            break;
        default:
            sqlStr = null;

        }
        return sqlStr;

    }

    /**
     * get Comment Query Str.
     * 
     * @param productName
     * @param tableName
     * @param catalogName
     * @param schemaPattern
     * @return null only if conn is null.or can not find
     */
    public static String getCommentQueryStr(String productName, String tableName, String catalogName, String schemaPattern) {
        if (productName == null) {
            return null;
        }
        productName = productName.replaceAll(" ", "_"); //$NON-NLS-1$ //$NON-NLS-2$
        EDataBaseType eDataBaseType = null;
        try {
            eDataBaseType = EDataBaseType.valueOf(productName);
        } catch (Exception e) {
            eDataBaseType = EDataBaseType.Microsoft_SQL_Server;
        }
        String sqlStr = ""; //$NON-NLS-1$
        switch (eDataBaseType) {
        case Oracle:
            sqlStr = "SELECT COMMENTS FROM ALL_TAB_COMMENTS WHERE TABLE_NAME='" + tableName + "' AND OWNER='" + schemaPattern.toUpperCase() + "'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            break;
        case MySQL:
            sqlStr = "SELECT TABLE_COMMENT FROM information_schema.TABLES WHERE TABLE_NAME='" + tableName + "' AND TABLE_SCHEMA='" + catalogName + "'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            break;
        default:
            sqlStr = null;

        }
        return sqlStr;

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
    public static TdSqlDataType createDataType(int dataType, String typeName, int decimalDigits, int numPrecRadix) {
        TdSqlDataType sqlDataType = RelationalFactory.eINSTANCE.createTdSqlDataType();
        sqlDataType.setName(typeName);
        sqlDataType.setJavaDataType(dataType);
        sqlDataType.setNumericPrecision(decimalDigits);
        sqlDataType.setNumericPrecisionRadix(numPrecRadix);
        return sqlDataType;
    }

    /**
     * 
     * zshen Comment method "getXtentisBindingStub".
     * 
     * @param metadataBean
     * @return
     * @throws ServiceException
     * @noreference This method is not intended to be referenced by clients.
     */
    public static Object getXtentisBindingStub(IMetadataConnection metadataBean) throws Exception {

        AbsMdmConnectionHelper connectionHelper = null;
        connectionHelper = new S60MdmConnectionHelper();
        return connectionHelper.checkConnection(metadataBean.getUrl(), metadataBean.getUniverse(), metadataBean.getUsername(),
                metadataBean.getPassword());

        // return getXtentisBindingStub(dataProvider.getPathname(), ConnectionHelper.getUniverse(dataProvider),
        // dataProvider.getUsername(), dataProvider.getValue(dataProvider.getPassword(), false));

        // return getXtentisBindingStub(metadataBean.getUrl(), metadataBean.getUniverse(), metadataBean.getUsername(),
        // metadataBean.getPassword());
    }

    /**
     * 
     * zshen Comment method "getXtentisBindingStub".
     * 
     * @param dataProvider
     * @return
     * @throws ServiceException
     */
    public static Object getXtentisBindingStub(MDMConnection dataProvider) throws Exception {
        AbsMdmConnectionHelper connectionHelper = null;
        connectionHelper = new S60MdmConnectionHelper();
        return connectionHelper.checkConnection(dataProvider.getPathname(), ConnectionHelper.getUniverse(dataProvider),
                dataProvider.getUsername(), dataProvider.getValue(dataProvider.getPassword(), false));

        // return getXtentisBindingStub(dataProvider.getPathname(), ConnectionHelper.getUniverse(dataProvider),
        // dataProvider.getUsername(), dataProvider.getValue(dataProvider.getPassword(), false));
    }

    /**
     * 
     * zshen Comment method "getXtentisBindingStub".
     * 
     * @param url
     * @param universe
     * @param userName
     * @param password
     * @return
     * @throws ServiceException
     */
    // private static XtentisBindingStub getXtentisBindingStub(String url, String universe, String userName, String
    // password)
    // throws ServiceException {
    //
    // // initialization Web Service calling
    // XtentisServiceLocator xtentisService = new XtentisServiceLocator();
    // xtentisService.setXtentisPortEndpointAddress(url);
    // XtentisPort_PortType xtentisWS = xtentisService.getXtentisPort();
    // XtentisBindingStub stub = (XtentisBindingStub) xtentisWS;
    //
    // // authorization
    // if (universe == null || universe.trim().length() == 0) {
    // stub.setUsername(userName);
    // } else {
    //            stub.setUsername(universe + "/" + userName); //$NON-NLS-1$
    // }
    // stub.setPassword(password);
    // return stub;
    // }

    public static void main(String[] args) {
        MetadataConnectionUtils.getCommonQueryStr(EDataBaseType.Microsoft_SQL_Server.getProductName(), "tableName"); //$NON-NLS-1$
    }

    /**
     * Method "createTechnicalName" creates a technical name used for file system storage. MOD mzhao make this method as
     * public access.
     * 
     * @param functionalName the user friendly name
     * @return the technical name created from the user given name.
     */
    public static String createTechnicalName(final String functionalName) {
        String techname = "no_name"; //$NON-NLS-1$
        if (functionalName == null) {
            log.warn("A functional name should not be null"); //$NON-NLS-1$
            return techname;
        }
        // encode in base 64 so that all characters such white spaces, accents, everything that is dangerous when used
        // for file names are removed
        try {
            // encode
            String b64 = new String(Base64.encodeBase64(functionalName.getBytes()), "UTF-8"); //$NON-NLS-1$
            // replace special characters
            String date = SMPL_DATE_FMT.format(new Date(System.currentTimeMillis()));
            techname = b64.replace(CHAR_TO_REMOVE, REPLACEMENT_CHAR) + date;
        } catch (UnsupportedEncodingException e) {
            log.error(e, e);
        } // .replaceAll(B64ID, PREFIX);
        if (log.isDebugEnabled()) {
            log.debug("Functional name: " + functionalName + " -> techname: " + techname); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return techname;
    }

    public static List<String> getPackageFilter(Connection connection, DatabaseMetaData dbMetaData, boolean isCatalog) {
        List<String> packageFilter = new ArrayList<String>();
        try {
            if (isMdmConnection(connection)) {
                // MDMConnection mdmConnection = (MDMConnection) connection;
            } else {
                DatabaseConnection dbConnection = (DatabaseConnection) connection;
                // MOD qiongli 2011-9-23 handle context mod.
                IRepositoryContextService repositoryContextService = null;
                DatabaseConnection origValueConn = null;
                if (dbConnection.isContextMode()) {
                    repositoryContextService = CoreRuntimePlugin.getInstance().getRepositoryContextService();
                    if (repositoryContextService != null) {
                        // get the original value and select the defalut context
                        String contextName = dbConnection.getContextName();
                        origValueConn = repositoryContextService.cloneOriginalValueConnection(dbConnection,
                                contextName == null ? true : false, contextName);
                    }
                }
                if (isCatalog) {
                    boolean isHsql = dbConnection.getDatabaseType().equals(EDatabaseTypeName.HSQLDB_IN_PROGRESS.getDisplayName());
                    boolean isInformix = dbConnection.getDatabaseType().equalsIgnoreCase(EDatabaseTypeName.INFORMIX.name());
                    if (dbMetaData.supportsCatalogsInIndexDefinitions() && !isHsql || isInformix) {
                        String sid = dbConnection.getSID();
                        if (origValueConn != null) {
                            sid = origValueConn.getSID();
                        }
                        if (!StringUtils.isEmpty(sid) && !packageFilter.contains(sid)) {
                            packageFilter.add(sid);
                        }
                    }
                } else {
                    String uiSchema = null;
                    if (dbMetaData.supportsSchemasInIndexDefinitions()) {
                        uiSchema = dbConnection.getUiSchema();
                        if (origValueConn != null) {
                            uiSchema = origValueConn.getUiSchema();
                        }

                    }
                    // TDQ-12219 consider the schema from SID or Database.
                    if (uiSchema == null
                            && ManagerConnection.isSchemaFromSidOrDatabase(EDatabaseTypeName.getTypeFromDbType(dbConnection
                                    .getDatabaseType()))) {
                        uiSchema = dbConnection.getSID();
                        dbConnection.setUiSchema(uiSchema);
                    }
                    if (!StringUtils.isEmpty(uiSchema) && !packageFilter.contains(uiSchema)) {
                        packageFilter.add(uiSchema);
                    }
                }
            }
        } catch (SQLException e) {
            log.error(e, e);
        }
        return packageFilter;
    }

    public static boolean isMdmConnection(DataProvider dataprovider) {
        return dataprovider instanceof MDMConnection;
    }

    public static Connection fillConnectionInformation(ConnectionItem connItem) {
        return fillConnectionInformation(connItem, null);
    }

    /**
     * DOC connection created by TOS need to fill the basic information for useing in TOP.<br>
     * 
     * 
     * @param conn
     * @return
     */
    public static Connection fillConnectionInformation(ConnectionItem connItem, IMetadataConnection metadataConnection) {
        boolean saveFlag = false;
        Connection conn = connItem.getConnection();
        // // fill metadata of connection
        // if (conn.getName() == null || conn.getLabel() == null) {
        // saveFlag = true;
        // conn = fillConnectionMetadataInformation(conn);
        // }
        // fill structure of connection
        List<Catalog> catalogs = ConnectionHelper.getCatalogs(conn);
        List<Schema> schemas = ConnectionHelper.getSchema(conn);
        // MOD xqliu 2010-10-19 bug 16441: case insensitive
        boolean isNeedToFill = false;
        if (conn instanceof DatabaseConnection) {
            String dbProductID = ((DatabaseConnection) conn).getProductId();
            if (ConnectionHelper.getAllSchemas(conn).isEmpty()
                    && (EDatabaseTypeName.MSSQL05_08.getProduct().equals(dbProductID) || EDatabaseTypeName.MSSQL.getProduct()
                            .equals(dbProductID))) {
                isNeedToFill = true;
            } else if (EDatabaseTypeName.AS400.getProduct().equals(dbProductID)) {
                isNeedToFill = true;
            } else if (EDatabaseTypeName.PSQL.getProduct().equals(dbProductID)) {
                isNeedToFill = true;
            }
        }
        if ((catalogs.isEmpty() && schemas.isEmpty()) || isNeedToFill) {

            // ~ 16441
            DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(conn);
            if (dbConn != null) {
                saveFlag = true;

                conn = fillDbConnectionInformation(dbConn, metadataConnection);
            }
            // else {
            // MDMConnection mdmConn = SwitchHelpers.MDMCONNECTION_SWITCH.doSwitch(conn);
            // if (mdmConn != null) {
            // if (mdmConn.getDataPackage().isEmpty()) {
            // saveFlag = true;
            // conn = fillMdmConnectionInformation(mdmConn);
            // }
            // }
            // }
        }
        if (saveFlag && conn != null) {
            try {
                ProxyRepositoryFactory.getInstance().save(connItem);
            } catch (PersistenceException e) {
                log.error(e, e);
            }
        }
        // updateRetrieveAllFlag(conn);
        return conn;
    }

    /**
     * 
     * DOC zshen Comment method "setMDMConnectionParameter".
     * 
     * @param conn
     * @param metadataConnection
     * 
     * set the parameter of MDMConnection from metadataConnection
     */
    public static void setMDMConnectionParameter(MDMConnection conn, IMetadataConnection metadataConnection) {
        if (conn == null || metadataConnection == null) {
            return;
        }
        // host
        conn.setServer(metadataConnection.getServerName());

        // port
        conn.setPort(metadataConnection.getPort());
        // password
        ConnectionHelper.setPassword(conn, metadataConnection.getPassword());
        // user
        conn.setUsername(metadataConnection.getUsername());
        conn.setComment(metadataConnection.getComment());
        conn.setId(metadataConnection.getId());
        conn.setLabel(metadataConnection.getLabel());
        conn.setUniverse(metadataConnection.getUniverse());
        conn.setDatamodel(metadataConnection.getDatamodel());
        conn.setDatacluster(metadataConnection.getDatacluster());

    }

    /**
     * DOC xqliu Comment method "fillDbConnectionInformation".
     * 
     * @param dbConn
     * @return
     */
    public static DatabaseConnection fillDbConnectionInformation(DatabaseConnection dbConn, IMetadataConnection metadataConnection) {

        boolean noStructureExists = ConnectionHelper.getAllCatalogs(dbConn).isEmpty()
                && ConnectionHelper.getAllSchemas(dbConn).isEmpty();

        java.sql.Connection sqlConn = null;
        // only for derby related driver
        try {
            if (noStructureExists) { // do no override existing catalogs or
                                     // schemas
                // Map<String, String> paramMap =
                // ParameterUtil.toMap(ConnectionUtils.createConnectionParam(dbConn));
                IMetadataConnection metaConnection = metadataConnection;
                if (metadataConnection == null) {
                    metaConnection = ConvertionHelper.convert(dbConn);
                }
                EDatabaseTypeName currentEDatabaseType = EDatabaseTypeName.getTypeFromDbType(metaConnection.getDbType());
                if (currentEDatabaseType != null) {
                    MetadataFillFactory dbInstance = MetadataFillFactory.getDBInstance(metaConnection);
                    dbInstance.fillUIConnParams(metaConnection, dbConn);
                    sqlConn = MetadataConnectionUtils.createConnection(metaConnection).getObject();
                    if (sqlConn != null) {
                        DatabaseMetaData databaseMetaData = null;
                        // Added by Marvin Wang on Mar. 21, 2013 for loading hive jars dynamically, refer to TDI-25072.
                        if (EDatabaseTypeName.HIVE.getXmlName().equalsIgnoreCase(metaConnection.getDbType())) {
                            databaseMetaData = HiveConnectionManager.getInstance().extractDatabaseMetaData(metaConnection);
                        } else {
                            databaseMetaData = ExtractMetaDataUtils.getInstance().getDatabaseMetaData(sqlConn, dbConn, false);
                        }

                        if (sqlConn != null) {
                            dbInstance.fillCatalogs(dbConn, databaseMetaData, metaConnection,
                                    MetadataConnectionUtils.getPackageFilter(dbConn, databaseMetaData, true));
                            dbInstance.fillSchemas(dbConn, databaseMetaData, metaConnection,
                                    MetadataConnectionUtils.getPackageFilter(dbConn, databaseMetaData, false));
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e, e);
        } finally {
            if (sqlConn != null) {
                ConnectionUtils.closeConnection(sqlConn);
            }
            closeDerbyDriver();
        }
        return dbConn;
    }

    /**
     * DOC PLV Comment method "closeDerbyDriver". shutdown derby
     */
    public static void closeDerbyDriver() {
        if (derbyDriver != null) {
            try {
                derbyDriver.connect("jdbc:derby:;shutdown=true", null); //$NON-NLS-1$
            } catch (SQLException e) {
                // exception of shutdown success. no need to catch.
            }
        }
    }

    public static boolean isPostgresql(java.sql.Connection connection) throws SQLException {
        return isPostgresql(ExtractMetaDataUtils.getInstance().getConnectionMetadata(connection));
    }

    public static boolean isPostgresql(DatabaseMetaData connectionMetadata) throws SQLException {
        if (connectionMetadata != null) {
            String databaseProductName = connectionMetadata.getDatabaseProductName();
            if (databaseProductName != null) {
                return databaseProductName.toLowerCase().indexOf(DatabaseConstant.POSTGRESQL_PRODUCT_NAME) > -1;
            }
        }
        return false;
    }

    public static boolean isDerbyRelatedDb(String driverClassname, String dbType) {
        return (driverClassname != null && driverClassname.equals(EDatabase4DriverClassName.JAVADB_EMBEDED.getDriverClass()))
                || (dbType != null && dbType.equals(EDatabaseTypeName.JAVADB_EMBEDED.getDisplayName())
                        || dbType.equals(EDatabaseTypeName.JAVADB_DERBYCLIENT.getDisplayName())
                        || dbType.equals(EDatabaseTypeName.JAVADB_DERBYCLIENT.getDisplayName())
                        || dbType.equals(EDatabaseTypeName.JAVADB_JCCJDBC.getDisplayName()) || dbType
                            .equals(EDatabaseTypeName.HSQLDB_IN_PROGRESS.getDisplayName()));
    }

    public static boolean isHsqlInprocess(IMetadataConnection metadataConnection) {
        if (metadataConnection != null) {
            String dbType = metadataConnection.getDbType();
            if (dbType != null && dbType.equals(EDatabaseTypeName.HSQLDB_IN_PROGRESS.getDisplayName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSybase(IMetadataConnection metadataConnection) {
        if (metadataConnection != null) {
            String dbType = metadataConnection.getDbType();
            if (dbType != null && dbType.equals(EDatabaseTypeName.SYBASEASE.getDisplayName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSQLite(IMetadataConnection metadataConnection) {
        if (metadataConnection != null) {
            String dbType = metadataConnection.getDbType();
            if (dbType != null && dbType.equals(EDatabaseTypeName.SQLITE.getDisplayName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isGeneralJDBC(IMetadataConnection metadataConnection) {
        if (metadataConnection != null) {
            String dbType = metadataConnection.getDbType();
            if (dbType != null && dbType.equals(EDatabaseTypeName.GENERAL_JDBC.getDisplayName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isHive(DatabaseMetaData metadata) {
        if (metadata != null) {
            if (metadata.getClass().getSimpleName().equals("HiveDatabaseMetaData")) {
                return true;
            }
            try {
                String name = metadata.getDatabaseProductName();
                if (name != null && name.equals(EDatabaseTypeName.HIVE.getDisplayName())) {
                    return true;
                }
            } catch (SQLException e) {
                ExceptionHandler.process(e);
            }
        }
        return false;
    }

    public static boolean isOracle(DatabaseMetaData metadata) {
        if (metadata != null) {
            try {
                String name = metadata.getDatabaseProductName();
                if (name != null && name.toUpperCase().equals(EDatabaseTypeName.ORACLEFORSID.getProduct().toUpperCase())) {
                    return true;
                }
            } catch (SQLException e) {
                ExceptionHandler.process(e);
            }
        }
        return false;
    }

    public static void setDerbyDriver(Driver driver) {
        derbyDriver = driver;
    }

    public static Driver getDerbyDriver() {
        return derbyDriver;
    }

    /**
     * Check a package is the one of AS400. return false if it's not.
     * 
     * @param packge
     * @return
     */
    public static boolean isAS400(Package packge) {
        Connection connection = ConnectionHelper.getConnection(packge);
        if (connection instanceof DatabaseConnection) {
            DatabaseConnection dbConnection = (DatabaseConnection) connection;
            return StringUtils.equalsIgnoreCase(dbConnection.getDatabaseType(), EDatabaseTypeName.AS400.getDisplayName());
        }

        return false;
    }

    public static List getConnection(IMetadataConnection metadataBean) {
        return ExtractMetaDataUtils.getInstance().getConnection(metadataBean.getDbType(), metadataBean.getUrl(),
                metadataBean.getUsername(), metadataBean.getPassword(), metadataBean.getDatabase(), metadataBean.getSchema(),
                metadataBean.getDriverClass(), metadataBean.getDriverJarPath(), metadataBean.getDbVersionString(),
                metadataBean.getAdditionalParams());
    }

    /**
     * return the MetadataColumn's name, the input object should be MetadataColumn, TdColumn or TdXmlElementType, if
     * not, return null.
     * 
     * @param element MetadataColumn, TdColumn or TdXmlElementType
     * @return
     */
    public static String getMetadataColumnName(ModelElement element) {
        String name = null;

        MetadataColumn mdColumn = SwitchHelpers.METADATA_COLUMN_SWITCH.doSwitch(element);
        TdColumn tdColumn = SwitchHelpers.COLUMN_SWITCH.doSwitch(element);
        TdXmlElementType xmlElementType = SwitchHelpers.XMLELEMENTTYPE_SWITCH.doSwitch(element);

        if (tdColumn != null) {
            name = tdColumn.getName();
        } else if (mdColumn != null) {
            name = mdColumn.getId();
        } else if (xmlElementType != null) {
            name = xmlElementType.getName();
        }

        return name;
    }

    /**
     * Get column type name by {@link ResultSetMetaData}.
     * 
     * @param connection is a jdbc connection.
     * @param tableName is the name of table which the column belongs to.
     * @param colIndex is the column index in the table (start with 1).
     * @return the column type name.
     */
    public static String getColumnTypeName(java.sql.Connection connection, String tableName, int colIndex) {
        String columnTypeName = null;
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT FIRST 1 * FROM " + tableName + ";"); //$NON-NLS-1$ //$NON-NLS-2$
            ResultSetMetaData rsMetaData = resultSet.getMetaData();
            columnTypeName = rsMetaData.getColumnTypeName(colIndex);
        } catch (Exception e) {
            // Do nothing.
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace(); // only for debug
                }
            }
        }
        return columnTypeName;
    }

}
