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
package org.talend.core.model.metadata.builder.database;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.database.AS400DatabaseMetaData;
import org.talend.commons.utils.database.DB2ForZosDataBaseMetadata;
import org.talend.commons.utils.database.EXASOLDatabaseMetaData;
import org.talend.commons.utils.database.SAPHanaDataBaseMetadata;
import org.talend.commons.utils.database.SASDataBaseMetadata;
import org.talend.commons.utils.database.SybaseDatabaseMetaData;
import org.talend.commons.utils.database.SybaseIQDatabaseMetaData;
import org.talend.commons.utils.database.TeradataDataBaseMetadata;
import org.talend.commons.utils.encoding.CharsetToolkit;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.commons.utils.platform.PluginChecker;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.ILibraryManagerUIService;
import org.talend.core.IService;
import org.talend.core.database.EDatabase4DriverClassName;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.database.conn.ConnParameterKeys;
import org.talend.core.database.conn.HiveConfKeysForTalend;
import org.talend.core.database.conn.version.EDatabaseVersion4Drivers;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.connection.hive.HiveModeInfo;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.prefs.SSLPreferenceConstants;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.metadata.managment.connection.manager.HiveConnectionManager;
import org.talend.metadata.managment.hive.EmbeddedHiveDataBaseMetadata;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IMetadataService;
import org.talend.utils.exceptions.MissingDriverException;
import org.talend.utils.sql.ConnectionUtils;

import metadata.managment.i18n.Messages;
import orgomg.cwm.objectmodel.core.Expression;

/**
 * DOC cantoine. Extract Meta Data Table. Contains all the Table and Metadata about a DB Connection. <br/>
 * 
 * $Id: ExtractMetaDataUtils.java 38999 2010-03-24 03:33:58Z cli $
 * 
 */
public class ExtractMetaDataUtils {

    public static final String MSSQL_CONN_CLASS = "net.sourceforge.jtds.jdbc.ConnectionJDBC3"; //$NON-NLS-1$

    private static Logger log = Logger.getLogger(ExtractMetaDataUtils.class);

    private static final char SPLIT_CHAR = ',';

    private static final String SYBASE_DATABASE_PRODUCT_NAME = "Adaptive Server Enterprise"; //$NON-NLS-1$

    private static ExtractMetaDataUtils singleton = null;

    // FIXME scorreia don't use public static fields
    private Connection conn;

    private String schema;

    private boolean isReconnect = true;

    private final Map<String, DriverShim> DRIVER_CACHE = new HashMap<String, DriverShim>();

    private boolean ignoreTimeout = false;

    private String[] ORACLE_SSL_JARS = new String[] { "oraclepki-12.2.0.1.jar", "osdt_cert-12.2.0.1.jar", //$NON-NLS-1$//$NON-NLS-2$
            "osdt_core-12.2.0.1.jar" }; //$NON-NLS-1$

    private ExtractMetaDataUtils() {
    }

    public static ExtractMetaDataUtils getInstance() {
        if (singleton == null) {
            singleton = new ExtractMetaDataUtils();
        }
        return singleton;
    }

    /**
     * Gets an instance of <code>DatabaseMetadata</code> by the given argument. The reason why it provides this method
     * is sometimes the database metadata you want to get is not from the <code>Connection</code>, in talend product
     * maybe a "fake" database metadata is created like "HiveDatabaseMetadata". Added by Marvin Wang on Mar 13, 2013.
     * 
     * @param metadataConn
     * @return
     */
    public DatabaseMetaData getDatabaseMetaData(IMetadataConnection metadataConn) {
        DatabaseMetaData databaseMetadata = null;
        String dbType = metadataConn.getDbType();
        if (EDatabaseTypeName.HIVE.getXmlName().equalsIgnoreCase(dbType)) {
            Object hiveModelObj = metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_MODE);
            if (hiveModelObj != null && (HiveModeInfo.get(hiveModelObj.toString()) == HiveModeInfo.EMBEDDED)) {
                databaseMetadata = new EmbeddedHiveDataBaseMetadata(metadataConn);
            }
        }
        return databaseMetadata;
    }

    /**
     * DOC cantoine. Method to return DatabaseMetaData of a DB connection.
     * 
     * FIXME sizhaoliu remove repetitions of specific DB conditions.
     * 
     * @param Connection conn
     * @param Connection dbType
     * @return DatabaseMetaData
     * 
     * MOD by zshen this method don't care about sqlMode
     */
    public DatabaseMetaData getDatabaseMetaData(Connection conn, String dbType) {
        return getDatabaseMetaData(conn, dbType, false, null);
    }

    public Map<String, DriverShim> getDriverCache() {
        return DRIVER_CACHE;
    }

    /**
     * DOC cantoine. Method to return DatabaseMetaData of a DB connection.
     * 
     * @param Connection conn
     * @param DatabaseConnection dbConn
     * @return DatabaseMetaData
     * 
     * MOD by zshen this method don't care about sqlMode
     */
    public DatabaseMetaData getDatabaseMetaData(Connection connection, DatabaseConnection dbConnection) {
        return getDatabaseMetaData(connection, dbConnection, (dbConnection != null ? dbConnection.isSQLMode() : false));
    }

    /**
     * DOC cantoine. Method to return DatabaseMetaData of a DB connection.
     * 
     * @param Connection conn
     * @param isSqlMode
     * @param DatabaseConnection dbConn
     * @return DatabaseMetaData
     * 
     * 
     */
    public DatabaseMetaData getDatabaseMetaData(Connection connection, DatabaseConnection dbConnection, boolean isSqlMode) {
        IMetadataConnection metadataConnection = ConvertionHelper.convert(dbConnection);
        if (metadataConnection != null && EDatabaseTypeName.HIVE.getXmlName().equalsIgnoreCase(metadataConnection.getDbType())) {
            DatabaseMetaData hiveDatabaseMetaData = null;
            try {
                hiveDatabaseMetaData = HiveConnectionManager.getInstance().extractDatabaseMetaData(metadataConnection);
            } catch (ClassNotFoundException e) {
                log.error(e);
            } catch (InstantiationException e) {
                log.error(e);
            } catch (IllegalAccessException e) {
                log.error(e);
            } catch (SQLException e) {
                log.error(e);
            }
            return hiveDatabaseMetaData;
        }
        return getDatabaseMetaData(connection, (dbConnection != null ? dbConnection.getDatabaseType() : null), isSqlMode,
                (dbConnection != null ? dbConnection.getSID() : null));
    }

    /**
     * 
     * 
     * @param conn
     * @return
     * @throws SQLException
     * 
     * this method will pass a mistake databaseType to get DatabaseMetDdata(e.x. db2->db2 for zos) if you can, you
     * should use getDatabaseMetaData() instead of this method
     * 
     */
    public DatabaseMetaData getConnectionMetadata(java.sql.Connection conn) throws SQLException {
        if (conn != null) {
            DatabaseMetaData dbMetaData = conn.getMetaData();
            // MOD xqliu 2009-11-17 bug 7888
            if (dbMetaData != null && dbMetaData.getDatabaseProductName() != null) {
                // because there is no sqlMode and the database arguments. so sqlMode is false, database is null.
                return getDatabaseMetaData(conn, dbMetaData.getDatabaseProductName(), false, null);
            }

            return dbMetaData;
        }
        return null;
    }

    /**
     * DOC cantoine. Method to return DatabaseMetaData of a DB connection.
     * 
     * @param Connection conn
     * @param isSqlMode whether is sqlMode
     * @param String dbType
     * @return DatabaseMetaData
     * 
     * 
     */
    public DatabaseMetaData getDatabaseMetaData(Connection conn, String dbType, boolean isSqlMode, String database) {
        DatabaseMetaData dbMetaData = null;
        try {
            if (conn != null && !conn.isClosed()) {
                // MOD sizhaoliu 2012-5-21 TDQ-4884
                if (MSSQL_CONN_CLASS.equals(conn.getClass().getName())) {
                    dbMetaData = createJtdsDatabaseMetaData(conn);
                } else if (EDatabaseTypeName.IBMDB2ZOS.getXmlName().equals(dbType)
                        || EDatabaseTypeName.IBMDB2.getXmlName().equals(dbType)) {
                    dbMetaData = createDB2ForZosFakeDatabaseMetaData(conn);
                } else if (EDatabaseTypeName.TERADATA.getXmlName().equals(dbType) && isSqlMode) {
                    dbMetaData = createTeradataFakeDatabaseMetaData(conn);
                    // add by wzhang for bug 8106. set database name for teradata.
                    TeradataDataBaseMetadata teraDbmeta = (TeradataDataBaseMetadata) dbMetaData;
                    teraDbmeta.setDatabaseName(database);
                } else if (EDatabaseTypeName.SAS.getXmlName().equals(dbType)) {
                    dbMetaData = createSASFakeDatabaseMetaData(conn);
                } else if (EDatabaseTypeName.SYBASEASE.getDisplayName().equals(dbType)
                        || SYBASE_DATABASE_PRODUCT_NAME.equals(dbType)) {
                    // TDQ-8300 make sybaseIQ work well,currently,the dbType of sybaseIQ is still
                    // "Sybase (ASE and IQ)",so use dbMetaData.getDatabaseProductName() to judge if it is sybaseIQ .
                    dbMetaData = conn.getMetaData();
                    if (dbMetaData != null
                            && EDatabaseTypeName.SYBASEIQ.getDisplayName().equals(dbMetaData.getDatabaseProductName())) {
                        dbMetaData = createSybaseIQFakeDatabaseMetaData(conn);
                    } else {
                        dbMetaData = createSybaseFakeDatabaseMetaData(conn);
                    }
                } else if (EDatabaseTypeName.HIVE.getDisplayName().equals(dbType) && isHiveEmbeddedConn(conn)) {
                    // dbMetaData = new EmbeddedHiveDataBaseMetadata(conn);
                } else if (EDatabaseTypeName.AS400.getXmlName().equals(dbType)) {
                    dbMetaData = createAS400FakeDatabaseMetaData(conn);
                } else if (EDatabaseTypeName.EXASOL.getXmlName().equals(dbType)) {
                    dbMetaData = createEXASOLFakeDatabaseMetaData(conn);
                } else if (EDatabaseTypeName.SAPHana.getXmlName().equals(dbType)) {
                    dbMetaData = createSAPHanaFakeDatabaseMetaData(conn);
                } else {
                    dbMetaData = conn.getMetaData();
                }
            }
        } catch (SQLException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
        return dbMetaData;
    }

    public boolean isHiveConnection(Connection hiveConn) {
        if (hiveConn != null) {
            Class<?> clazz = hiveConn.getClass();
            if ("HiveConnection".equals(clazz.getSimpleName())) { //$NON-NLS-1$
                return true;
            }
        }
        return false;
    }

    public boolean isHiveEmbeddedConn(Connection hiveConn) {
        if (hiveConn != null) {
            Class<?> clazz = hiveConn.getClass();
            if ("HiveConnection".equals(clazz.getSimpleName())) { //$NON-NLS-1$
                try {
                    Field clientField = clazz.getDeclaredField("client"); //$NON-NLS-1$
                    clientField.setAccessible(true);
                    Object clientObj = clientField.get(hiveConn);
                    if (clientObj != null) {
                        Class<?> clientClass = clientObj.getClass();
                        if ("HiveServerHandler".equals(clientClass.getSimpleName())) { //$NON-NLS-1$
                            return true;
                        }
                    }
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 
     * Added by Marvin Wang on Dec 6, 2012.
     * 
     * @param metadataConnection
     * @return
     */
    public boolean needFakeDatabaseMetaData(IMetadataConnection metadataConnection) {
        String dbType = metadataConnection.getDbType();
        boolean isSqlMode = metadataConnection.isSqlMode();
        String dbVersion = metadataConnection.getDbVersionString();
        if (EDatabaseTypeName.IBMDB2.getXmlName().equals(dbType) || EDatabaseTypeName.IBMDB2ZOS.getXmlName().equals(dbType)
                || EDatabaseTypeName.SAPHana.getXmlName().equals(dbType)) {
            return true;
        } else if (EDatabaseTypeName.TERADATA.getXmlName().equals(dbType) && isSqlMode) {
            return true;
        } else if (EDatabaseTypeName.SAS.getXmlName().equals(dbType)) {
            return true;
        } else if (EDatabaseTypeName.HIVE.getDisplayName().equals(dbType)
                && EDatabaseVersion4Drivers.HIVE_EMBEDDED.getVersionDisplay().equals(dbVersion)) {
            return true;
        }

        return false;
    }

    /**
     * Added by Marvin Wang on Dec 6, 2012.
     * 
     * @param dbType
     * @param isSqlMode
     * @return
     * @deprecated
     */
    @Deprecated
    public boolean needFakeDatabaseMetaData(String dbType, boolean isSqlMode) {
        // FIXME, maybe, it's not good way, need check later.
        final boolean isHiveEmbedded = Boolean.parseBoolean(System
                .getProperty(HiveConfKeysForTalend.HIVE_CONF_KEY_TALEND_HIVE_MODE.getKey()));
        if (EDatabaseTypeName.IBMDB2ZOS.getXmlName().equals(dbType)) {
            return true;
        } else if (EDatabaseTypeName.TERADATA.getXmlName().equals(dbType) && isSqlMode) {
            return true;
        } else if (EDatabaseTypeName.SAS.getXmlName().equals(dbType)) {
            return true;
        } else if (EDatabaseTypeName.HIVE.getDisplayName().equals(dbType) && isHiveEmbedded) {
            return true;
        } else if (EDatabaseTypeName.AS400.getDisplayName().equals(dbType)) {
            return true;
        }
        return false;
    }

    /**
     * only for db2 on z/os right now.
     * 
     * @param conn2
     * @return
     * @throws SQLException
     */
    public DatabaseMetaData createDB2ForZosFakeDatabaseMetaData(Connection conn) throws SQLException {
        DB2ForZosDataBaseMetadata dmd = new DB2ForZosDataBaseMetadata(conn);
        return dmd;
    }

    private DatabaseMetaData createAS400FakeDatabaseMetaData(Connection conn) throws SQLException {
        AS400DatabaseMetaData dmd = new AS400DatabaseMetaData(conn);
        return dmd;
    }

    private DatabaseMetaData createEXASOLFakeDatabaseMetaData(Connection conn) throws SQLException {
        EXASOLDatabaseMetaData dmd = new EXASOLDatabaseMetaData(conn);
        return dmd;
    }

    private DatabaseMetaData createTeradataFakeDatabaseMetaData(Connection conn) {
        TeradataDataBaseMetadata tmd = new TeradataDataBaseMetadata(conn);
        return tmd;
    }

    private DatabaseMetaData createSAPHanaFakeDatabaseMetaData(Connection conn) {
        SAPHanaDataBaseMetadata tmd = new SAPHanaDataBaseMetadata(conn);
        return tmd;
    }

    private DatabaseMetaData createSASFakeDatabaseMetaData(Connection conn) {
        SASDataBaseMetadata tmd = new SASDataBaseMetadata(conn);
        return tmd;
    }

    private DatabaseMetaData createSybaseFakeDatabaseMetaData(Connection conn) throws SQLException {
        SybaseDatabaseMetaData dmd = new SybaseDatabaseMetaData(conn);
        return dmd;
    }

    private DatabaseMetaData createSybaseIQFakeDatabaseMetaData(Connection conn) throws SQLException {
        SybaseIQDatabaseMetaData dmd = new SybaseIQDatabaseMetaData(conn);
        return dmd;
    }

    private DatabaseMetaData createJtdsDatabaseMetaData(Connection conn) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IMetadataService.class)) {
            IService service = GlobalServiceRegister.getDefault().getService(IMetadataService.class);
            if (service != null) {
                return ((IMetadataService) service).findCustomizedJTDSDBMetadata(conn);
            }
        }
        try {
            return conn.getMetaData();
        } catch (SQLException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    /**
     * DOC cantoine. Method to return MetaDataInfo on Column DataBaseConnection.
     * 
     * @param ResultSet columns
     * @param String infoType
     * @return String : the result of column's information MetaData
     */
    public String getStringMetaDataInfo(ResultSet columns, String infoType, DatabaseMetaData dbMetaData) {
        String metaDataInfo = null;
        if (columns != null && infoType != null) {
            try {
                metaDataInfo = columns.getString(infoType);
                // hywang modified it for bug 7038
                // List<String> funcions = getAllDBFuctions(dbMetaData);
                // if (metaDataInfo != null) {
                // if ((dbMetaData != null && funcions != null && !funcions.contains(metaDataInfo))) {
                // metaDataInfo = ManagementTextUtils.QUOTATION_MARK + metaDataInfo +
                // ManagementTextUtils.QUOTATION_MARK;
                // }
                // }
                // Replace ALL ' in the retrieveSchema, cause PB for Default Value.
                // metaDataInfo = metaDataInfo.replaceAll("'", ""); //$NON-NLS-1$
                // //$NON-NLS-2$
            } catch (SQLException e) {
                // log.error(e.toString());
                return metaDataInfo;
            } catch (Exception e) {
                // log.error(e.toString());
                return metaDataInfo;
            }
        }
        return metaDataInfo;
    }

    /**
     * 
     * DOC xye Comment method "getStringMetaDataInfo".
     * 
     * @param columns
     * @param infoType
     * @return
     */
    public String getStringMetaDataInfo(ResultSet columns, int infoType) {
        String metaDataInfo = null;
        try {
            metaDataInfo = columns.getString(infoType);
            // Replace ALL ' in the retrieveSchema, cause PB for Default Value.
            // metaDataInfo = metaDataInfo.replaceAll("'", ""); //$NON-NLS-1$
            // //$NON-NLS-2$
        } catch (SQLException e) {
            // log.error(e.toString());
            return metaDataInfo;
        } catch (Exception e) {
            // log.error(e.toString());
            return metaDataInfo;
        }
        return metaDataInfo;
    }

    /**
     * DOC cantoine. Method to return MetaDataInfo on Column DataBaseConnection.
     * 
     * @param ResultSet columns
     * @param String infoType
     * @return int : the result of column's information MetaData
     */
    public Integer getIntMetaDataInfo(ResultSet columns, String infoType) {
        Integer metaDataInfo = new Integer(0);
        try {
            metaDataInfo = new Integer(columns.getInt(infoType));
        } catch (SQLException e) {
            // log.error(e.toString());
            return metaDataInfo;
        } catch (Exception e) {
            // log.error(e.toString());
            return metaDataInfo;
        }
        return metaDataInfo;
    }

    public Integer getOracleIntMatadataInfo(ResultSet columns, String infoType) {
        Integer metaDataInfo = new Integer(0);
        try {
            metaDataInfo = new Integer(columns.getInt(infoType));
            if (columns.getString("CHAR_USED").equals("C")) {
                metaDataInfo = metaDataInfo / 2;
            }
        } catch (SQLException e) {
            // log.error(e.toString());
            return metaDataInfo;
        } catch (Exception e) {
            // log.error(e.toString());
            return metaDataInfo;
        }
        return metaDataInfo;
    }

    public Integer getMysqlIntMetaDataInfo(ResultSetMetaData rMetadata, int columnIndex) {
        Integer metaDataInfo = new Integer(0);
        try {
            metaDataInfo = rMetadata.getPrecision(columnIndex);
        } catch (SQLException e) {
            // log.error(e.toString());
            return metaDataInfo;
        } catch (Exception e) {
            // log.error(e.toString());
            return metaDataInfo;
        }
        return metaDataInfo;
    }

    /**
     * Return boolean value, used only to check if one field is nullable or not. If used to something else than nullable
     * check, take care of the default values returned. Actually returns null if there is any error during the check.
     * 
     * @param ResultSet columns
     * @param String infoType
     * @return boolean : the result of column's information MetaData
     */
    public boolean getBooleanMetaDataInfo(ResultSet columns, String infoType) {
        boolean metaDataInfo = false;
        try {
            String result = columns.getString(infoType);
            if (result != null && result.equals("YES")) { //$NON-NLS-1$
                metaDataInfo = true;
            }
        } catch (SQLException e) {
            // log.error(e.toString());
            return true;
        } catch (Exception e) {
            // log.error(e.toString());
            return true;
        }
        return metaDataInfo;
    }

    // PTODO cantoine : Be careful : Integrate in properties or preferences of
    // Talend Product
    // OCA : save connectionString and associated regex in the same place.
    /**
     * DOC cantoine. Method to return MetaDataInfo on Column DataBaseConnection.
     * 
     * @param ResultSet columns
     * @param String infoType
     * @return String : the result of column's information MetaData
     */
    public String getDriverClassByDbType(String dbType) {

        EDatabase4DriverClassName t4d = EDatabase4DriverClassName.indexOfByDbType(dbType);
        if (t4d != null) {
            return t4d.getDriverClass();
        }
        return null;
    }

    // hywang add for bug 7575
    public String getDbTypeByClassName(String driverClassName) {
        return getDbTypeByClassNameAndDriverJar(driverClassName, null);
    }

    public String getDbTypeByClassNameAndDriverJar(String driverClassName, String driverJar) {
        return ConvertionHelper.getDbTypeByClassNameAndDriverJar(driverClassName, driverJar);
    }

    /**
     * 
     * DOC YeXiaowei Comment method "isValidJarFile".
     * 
     * @param driverJarPath
     * @return
     */
    private boolean isValidJarFile(final String[] driverJarPath) {
        boolean a = false;
        for (String element : driverJarPath) {
            if (element == null || element.equals("")) { //$NON-NLS-1$
                return a;
            }
            File jarFile = new File(element);
            a = jarFile.exists() && jarFile.isFile();
        }
        return a;

    }

    /**
     * 
     * cli Comment method "retrieveSchemaPatternForAS400".
     * 
     * bug 12179
     */
    public String retrieveSchemaPatternForAS400(String url) {
        if (url == null || "".equals(url)) {
            return null;
        }
        String libsPattern = "libraries\\s*=\\s*"; //$NON-NLS-1$
        Pattern regex = Pattern.compile(libsPattern + "(([\\w.]+?)\\s*,?\\s*)*\\s*;?", //$NON-NLS-1$
                Pattern.CANON_EQ | Pattern.CASE_INSENSITIVE);
        Matcher regexMatcher = regex.matcher(url);
        Set<String> libs = new HashSet<String>();
        while (regexMatcher.find()) {
            String str = regexMatcher.group();
            if (str != null && !"".equals(str.trim())) { //$NON-NLS-1$
                Pattern libP = Pattern.compile(libsPattern + "(.*)"); //$NON-NLS-1$
                Matcher libMatcher = libP.matcher(str.trim());
                if (libMatcher.find()) {
                    String libStr = libMatcher.group(1);
                    if (libStr != null) {
                        libStr = libStr.trim();
                        if (libStr.endsWith(";")) { //$NON-NLS-1$
                            libStr = libStr.substring(0, libStr.length() - 1);
                        }
                        libStr = libStr.trim();
                        if (!libStr.equals("")) { //$NON-NLS-1$
                            String[] multiSchems = getMultiSchems(libStr);
                            if (multiSchems != null) {
                                for (String s : multiSchems) {
                                    if (s != null) {
                                        libs.add(s.trim());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (!libs.isEmpty()) {
            StringBuffer sb = new StringBuffer();
            int index = 0;
            for (String lib : libs) {
                sb.append(lib);
                if (index < libs.size() - 1) {
                    sb.append(SPLIT_CHAR);
                }
                index++;
            }
            return sb.toString();
        } else {
            return null;
        }
    }

    /**
     * 
     * cli Comment method "getMultiSchems".
     * 
     * bug 12179
     */
    public String[] getMultiSchems(String schemas) {
        if (schemas != null) {
            String[] split = schemas.split(String.valueOf(SPLIT_CHAR));
            return split;
        }
        return null;
    }

    /**
     * DOC cantoine. Method to close connect to DataBase.
     */
    public void closeConnection() {
        closeConnection(false);
    }

    public void closeConnection(boolean force) {
        try {
            if (conn != null && !conn.isClosed()) {
                if (isReconnect || force) {
                    if (conn.getMetaData() != null) {
                        String url = null;
                        try {
                            url = conn.getMetaData().getURL();
                        } catch (Exception e) {
                            // nothing to do
                        }
                        boolean isInProcessModeHsql = ConnectionUtils.isInProcessModeHsql(url);
                        // we hold on hsql server's status when it is server mode and not In-Process mode.
                        // only shutdown in-process mode
                        if (isInProcessModeHsql) {
                            ConnectionUtils.executeShutDownForHSQL(conn);
                        }
                    }
                    conn.close();
                }
            }
        } catch (SQLException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    public void checkAccessDbq(String connString) throws Exception {
        for (String s : connString.split(";")) { //$NON-NLS-1$
            s = s.toLowerCase();
            int pos = s.indexOf("dbq"); //$NON-NLS-1$
            if (pos > -1) {
                s = s.substring(pos + 3).replaceAll("=", "").trim(); //$NON-NLS-1$ //$NON-NLS-2$
                // check the value of dbp
                if (!s.endsWith(".mdb") && !s.endsWith(".accdb")) { //$NON-NLS-1$ //$NON-NLS-2$
                    throw new Exception(Messages.getString("ExtractMetaDataUtils.noData")); //$NON-NLS-1$
                }
                return;
            }
        }
    }

    /**
     * 
     * ggu Comment method "checkDBConnectionTimeout".
     * 
     * there is no effect for oracle.
     */
    public void checkDBConnectionTimeout() {
        DriverManager.setLoginTimeout(getDBConnectionTimeout());
    }

    private int getDBConnectionTimeout() {
        if (Platform.isRunning()) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerCoreService.class)) {
                IDesignerCoreService designerCoreService = CoreRuntimePlugin.getInstance().getDesignerCoreService();
                return designerCoreService.getDBConnectionTimeout();
            }
        }
        // disable timeout
        return 0;
    }

    /**
     * 
     * ggu Comment method "setQueryStatementTimeout".
     * 
     * @param statement
     */
    public void setQueryStatementTimeout(Statement statement) {
        if (statement != null) {
            try {
                if (isIgnoreTimeout()) {
                    statement.setQueryTimeout(0);
                } else {
                    statement.setQueryTimeout(getDBConnectionTimeout());
                }
            } catch (SQLException e) {
                // nothing, some db doesn't support the timeout
                // no need to throw exception or this one will be throw all the time
            }
        }
    }

    /**
     * Getter for ignoreTimeout.
     * 
     * @return the ignoreTimeout
     */
    public boolean isIgnoreTimeout() {
        return this.ignoreTimeout;
    }

    /**
     * Sets the ignoreTimeout.
     * 
     * @param ignoreTimeout the ignoreTimeout to set
     */
    public void setIgnoreTimeout(boolean ignoreTimeout) {
        this.ignoreTimeout = ignoreTimeout;
    }

    /**
     * DOC cantoine. Method to connect to DataBase.
     * 
     * @param String driverClass
     * @param String urlString pwdT
     * @param String username
     * @param String pwd
     * @param String schemaBase
     */
    public List getConnection(String dbType, String url, String username, String pwd, String dataBase, String schemaBase,
            final String driverClassName, final String driverJarPath, String dbVersion, String additionalParams) {
        boolean isColsed = false;
        List conList = new ArrayList();
        try {
            if (conn != null) {
                isColsed = conn.isClosed();
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        // bug 9162
        List list = new ArrayList();
        DriverShim wapperDriver = null;
        if (isReconnect || conn == null || isColsed) {
            try {
                closeConnection(true); // colse before connection.
                checkDBConnectionTimeout();

                list = connect(dbType, url, username, pwd, driverClassName, driverJarPath, dbVersion, additionalParams);
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i) instanceof Connection) {
                            conn = (Connection) list.get(i);
                        }
                        if (list.get(i) instanceof DriverShim) {
                            wapperDriver = (DriverShim) list.get(i);
                        }
                    }
                }
                if (schemaBase != null && !schemaBase.equals("")) { //$NON-NLS-1$
                    final boolean equals = EDatabaseTypeName.getTypeFromDbType(dbType).getProduct()
                            .equals(EDatabaseTypeName.ORACLEFORSID.getProduct());
                    if (!ExtractMetaDataFromDataBase.checkSchemaConnection(schemaBase, conn, equals, dbType)) {
                        schema = null;
                    }
                } else {
                    boolean teradata = EDatabaseTypeName.getTypeFromDbType(dbType).getProduct()
                            .equals(EDatabaseTypeName.TERADATA.getProduct());
                    // add for bug 10644
                    boolean as400 = EDatabaseTypeName.getTypeFromDbType(dbType).getProduct()
                            .equals(EDatabaseTypeName.AS400.getProduct());
                    if (teradata) {
                        schema = dataBase;
                    } else if (as400) {
                        schema = retrieveSchemaPatternForAS400(url);
                    } else if (EDatabaseTypeName.SAS.getProduct()
                            .equals(EDatabaseTypeName.getTypeFromDbType(dbType).getProduct())) {
                        schema = dataBase;
                    } else {
                        schema = null;
                    }
                }
                conList.add(conn);
                if (wapperDriver != null) {
                    conList.add(wapperDriver);
                }
            } catch (MissingDriverException e) {
                throw e;
            } catch (SQLException e) {
                log.error(e.toString());
                throw new RuntimeException(e);
            } catch (Exception e) {
                log.error(e.toString());
                throw new RuntimeException(e);
            }
        }
        return conList;
    }

    /**
     * Load the jars required by Hive Embedded mode, but this method should be here. It is better to put it in
     * JDBCDriverLoader.java. Due to the limitation on code structure, I have to put it here, so if the part of metadata
     * connection will be refactored later, then developer could remove it to anyway they want. Added by Marvin Wang on
     * Oct 24, 2012.
     * 
     * @param dbType
     * @param dbVersion
     */
    public void loadJarRequiredByDriver(String dbType, String dbVersion) {
        JDBCDriverLoader loader = new JDBCDriverLoader();
        List<String> jarPathList = new ArrayList<String>();
        ILibraryManagerService librairesManagerService = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                ILibraryManagerService.class);
        List<String> driverNames = EDatabaseVersion4Drivers.getDrivers(dbType, dbVersion);
        if (driverNames != null) {
            for (String jar : driverNames) {
                if (!new File(getJavaLibPath() + jar).exists()) {
                    librairesManagerService.retrieve(jar, getJavaLibPath(), new NullProgressMonitor());
                }
                jarPathList.add(getJavaLibPath() + jar);
            }
        }

        loader.loadForHiveEmbedded(jarPathList, dbType, dbVersion);
    }

    /**
     * DOC xye Comment method "connect".
     * 
     * @param dbType
     * @param url
     * @param username
     * @param pwd
     * @param driverClassName
     * @param driverJarPath
     * @param dbVersionString
     * @return
     * @throws Exception
     */
    public List connect(String dbType, String url, String username, String pwd, final String driverClassNameArg,
            final String driverJarPathArg, String dbVersion, String additionalParams) throws Exception {
        Connection connection = null;
        DriverShim wapperDriver = null;
        List conList = new ArrayList();

        String driverClassName = driverClassNameArg;
        List<String> jarPathList = new ArrayList<String>();

        ILibraryManagerService librairesManagerService = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                ILibraryManagerService.class);
        if (librairesManagerService != null) {
            // see feature 4720&4722
            if ((driverJarPathArg == null || driverJarPathArg.equals(""))) { //$NON-NLS-1$
                List<String> driverNames = EDatabaseVersion4Drivers.getDrivers(dbType, dbVersion);
                if (driverNames != null) {
                    if (EDatabaseTypeName.ORACLE_CUSTOM.getDisplayName().equals(dbType)
                            && StringUtils.isNotEmpty(additionalParams)) {
                        if (additionalParams.contains(SSLPreferenceConstants.TRUSTSTORE_TYPE)) {
                             driverNames.addAll(Arrays.asList(ORACLE_SSL_JARS));
                        }
                    }
                    // fix for TUP-857 , to retreive needed jar one time
                    librairesManagerService.retrieve(driverNames, getJavaLibPath(), new NullProgressMonitor());
                    for (String jar : driverNames) {
                        jarPathList.add(getJavaLibPath() + jar);
                    }
                    driverClassName = getDriverClassByDbType(dbType);
                    // feature TDI-22108
                    if (EDatabaseTypeName.VERTICA.getXmlName().equals(dbType)
                            && (EDatabaseVersion4Drivers.VERTICA_6.getVersionValue().equals(dbVersion)
                                    || EDatabaseVersion4Drivers.VERTICA_5_1.getVersionValue().equals(dbVersion)
                                    || EDatabaseVersion4Drivers.VERTICA_6_1_X.getVersionValue().equals(dbVersion) || EDatabaseVersion4Drivers.VERTICA_7
                                    .getVersionValue().equals(dbVersion) || EDatabaseVersion4Drivers.VERTICA_9
                                    .getVersionValue().equals(dbVersion))) {
                        driverClassName = EDatabase4DriverClassName.VERTICA2.getDriverClass();
                    } else if (EDatabaseTypeName.MYSQL.getXmlName().equals(dbType)
                            && (EDatabaseVersion4Drivers.MARIADB.getVersionValue().equals(dbVersion))) {
                        driverClassName = EDatabase4DriverClassName.MARIADB.getDriverClass();
                    }else if(EDatabaseTypeName.MSSQL.getDisplayName().equals(dbType) 
                            && EDatabaseVersion4Drivers.MSSQL_PROP.getVersionValue().equals(dbVersion)){
                        driverClassName = EDatabase4DriverClassName.MSSQL2.getDriverClass();
                    }else if(EDatabaseTypeName.SYBASEASE.getDisplayName().equals(dbType) 
                            && EDatabaseVersion4Drivers.SYBASEIQ_16.getVersionValue().equals(dbVersion)){
                        driverClassName = EDatabase4DriverClassName.SYBASEIQ_16.getDriverClass();
                    }
                }
            } else {
                Set<String> jarsAvailable = librairesManagerService.list(new NullProgressMonitor());
                // add another test with start with / in case of linux OS.
                if (driverJarPathArg.contains("\\") || driverJarPathArg.startsWith("/")) { //$NON-NLS-1$
                    if (driverJarPathArg.contains(";")) {
                        String jars[] = driverJarPathArg.split(";");
                        List<String> jarsToRetreive = new ArrayList<String>();
                        for (String jar : jars) {
                            Path path = new Path(jar);
                            if (!checkFileCRCCode(new File(getJavaLibPath() + path.lastSegment()), new File(jar))) {
                                String librariesPath = getLibrariesPath(ECodeLanguage.JAVA);
                                File existJar = new File(librariesPath + File.separator + path.lastSegment());
                                // here will overwrite the original jar if exist(TDI-27784)
                                if (existJar.exists()) {
                                    existJar.delete();
                                    FilesUtils.copyFile(new File(jar), existJar);
                                }
                            }
                            // fix for 19020
                            if (jarsAvailable.contains(path.lastSegment())) {
                                if (!new File(getJavaLibPath() + path.lastSegment()).exists()) {
                                    jarsToRetreive.add(path.lastSegment());
                                }
                                jarPathList.add(getJavaLibPath() + path.lastSegment());
                            } else {
                                jarsToRetreive.add(path.lastSegment());
                                jarPathList.add(jar);
                            }
                        }
                        librairesManagerService.retrieve(jarsToRetreive, getJavaLibPath(), new NullProgressMonitor());
                    } else {
                        Path path = new Path(driverJarPathArg);
                        File driverFile = new File(driverJarPathArg);
                        boolean isExist = driverFile.exists();
                        // if (!isExist || !driverJarPathArg.contains(":")) { Removed by Marvin Wang for TDI-25766.
                        if (!isExist) {
                            jarPathList.add("");
                        } else {
                            // removed by fwang for bug TDI-24594
                            // if (jarsAvailable.contains(path.lastSegment())) {
                            // String jarUnderLib = getJavaLibPath() + path.lastSegment();
                            // File file = new File(jarUnderLib);
                            // if (!file.exists()) {
                            // librairesManagerService.retrieve(path.lastSegment(), getJavaLibPath(), new
                            // NullProgressMonitor());
                            // }
                            // jarPathList.add(jarUnderLib);
                            // } else {
                            jarPathList.add(driverJarPathArg);
                        }
                    }
                }else if(driverJarPathArg.contains("/")){
                    if (driverJarPathArg.contains(";")) {
                        String jars[] = driverJarPathArg.split(";");
                        for (String jar : jars) {
                            String jarName = jar.split("/")[1]+".jar";
                            if (!new File(getJavaLibPath() + jarName).exists()) {
                                librairesManagerService.retrieve(jarName, getJavaLibPath(), new NullProgressMonitor());
                            }
                            jarPathList.add(getJavaLibPath() + jarName);
                        }
                    }else{
                        String jarName = driverJarPathArg.split("/")[1]+".jar";
                        if (!new File(getJavaLibPath() + jarName).exists()) {
                            librairesManagerService.retrieve(jarName, getJavaLibPath(), new NullProgressMonitor());
                        }
                        jarPathList.add(getJavaLibPath() + jarName);
                    }
                } else {
                    if (driverJarPathArg.contains(";")) {
                        String jars[] = driverJarPathArg.split(";");
                        librairesManagerService.retrieve(Arrays.asList(jars), getJavaLibPath(), new NullProgressMonitor());
                        for (String jar : jars) {
                            jarPathList.add(getJavaLibPath() + jar);
                        }
                    } else {
                        if (!new File(getJavaLibPath() + driverJarPathArg).exists()) {
                            librairesManagerService.retrieve(driverJarPathArg, getJavaLibPath(), new NullProgressMonitor());
                        }
                        jarPathList.add(getJavaLibPath() + driverJarPathArg);
                    }
                }
            }
        }

        final String[] driverJarPath = jarPathList.toArray(new String[0]);

        /*
         * For general jdbc, driver class is specific by user.
         */
        if (driverClassName == null || driverClassName.equals("")) { //$NON-NLS-1$
            driverClassName = getDriverClassByDbType(dbType);
            // see bug 4404: Exit TOS when Edit Access Schema in repository
            if (dbType.equals(EDatabaseTypeName.ACCESS.getXmlName())) {
                // throw exception to prevent getting connection, which may crash
                checkAccessDbq(url);
            }
        }
        // bug 9162
        List list = new ArrayList();
        checkDBConnectionTimeout();
        if (dbType != null && dbType.equalsIgnoreCase(EDatabaseTypeName.GENERAL_JDBC.getXmlName())) {
            JDBCDriverLoader loader = new JDBCDriverLoader();
            list = loader.getConnection(driverJarPath, driverClassName, url, username, pwd, dbType, dbVersion, additionalParams);
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i) instanceof Connection) {
                        connection = (Connection) list.get(i);
                    }
                    if (list.get(i) instanceof DriverShim) {
                        wapperDriver = (DriverShim) list.get(i);
                    }
                }
            }

        } else if (dbType != null && dbType.equalsIgnoreCase(EDatabaseTypeName.MSSQL.getDisplayName()) && "".equals(username)) {
            // the jtds mode to connect sqlserver database only Instance driver once
            if (DRIVER_CACHE.containsKey(EDatabase4DriverClassName.MSSQL.getDriverClass())) {
                wapperDriver = DRIVER_CACHE.get(EDatabase4DriverClassName.MSSQL.getDriverClass());
                Properties info = new Properties();
                // to avoid NPE
                username = username != null ? username : "";
                pwd = pwd != null ? pwd : "";
                info.put("user", username);
                info.put("password", pwd);
                connection = wapperDriver.connect(url, info);
            } else {
                JDBCDriverLoader loader = new JDBCDriverLoader();
                list = loader.getConnection(driverJarPath, driverClassName, url, username, pwd, dbType, dbVersion,
                        additionalParams);
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i) instanceof Connection) {
                            connection = (Connection) list.get(i);
                        }
                        if (list.get(i) instanceof DriverShim) {
                            wapperDriver = (DriverShim) list.get(i);
                        }
                    }
                    // setDriverCache(wapperDriver);
                }
            }
        } else if (dbType != null
                && (isValidJarFile(driverJarPath) || dbType.equalsIgnoreCase(EDatabaseTypeName.GODBC.getXmlName()))) {
            // Load jdbc driver class dynamicly
            JDBCDriverLoader loader = new JDBCDriverLoader();
            // The procedure to load jars for hive embedded mode is added by Marvin Wang on Oct. 24. Bug is
            // TDI-23400.
            if (EDatabaseTypeName.HIVE.getDisplayName().equals(dbType) && "EMBEDDED".equalsIgnoreCase(dbVersion)) {
                loadJarRequiredByDriver(dbType, dbVersion);
            }
            list = loader.getConnection(driverJarPath, driverClassName, url, username, pwd, dbType, dbVersion, additionalParams);
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i) instanceof Connection) {
                        connection = (Connection) list.get(i);
                    }
                    if (list.get(i) instanceof DriverShim) {
                        wapperDriver = (DriverShim) list.get(i);
                    }
                }
            }
        } else {
            // Don't use DriverManager

            try {
                Class<?> klazz = Class.forName(driverClassName);

                Properties info = new Properties();
                info.put("user", username); //$NON-NLS-1$
                info.put("password", pwd); //$NON-NLS-1$
                if (dbType.equals(EDatabaseTypeName.ACCESS.getXmlName()) || dbType.equals(EDatabaseTypeName.GODBC.getXmlName())) {
                    Charset systemCharset = CharsetToolkit.getInternalSystemCharset();
                    if (systemCharset != null && systemCharset.displayName() != null) {
                        info.put("charSet", systemCharset.displayName()); //$NON-NLS-1$
                    }
                }
                connection = ((Driver) klazz.newInstance()).connect(url, info);
            } catch (ClassNotFoundException e) {
                String errorMessage = Messages.getString("ExtractMetaDataUtils.missDriver.1") + driverClassName; //$NON-NLS-1$
                if (driverJarPath != null && driverJarPath.length > 0) {
                    errorMessage = errorMessage + "\n" + Messages.getString("ExtractMetaDataUtils.missDriver.2"); //$NON-NLS-1$//$NON-NLS-2$
                    for (String jarName : driverJarPath) {

                        errorMessage = errorMessage + "\n" + new Path(jarName).lastSegment(); //$NON-NLS-1$
                    }
                }
                throw new MissingDriverException(errorMessage);
            }
        }

        // throw a new exception.
        if (connection == null) {
            throw new Exception(Messages.getString("ExtractMetaDataUtils.1")); //$NON-NLS-1$
        }
        conList.add(connection);
        if (wapperDriver != null) {
            conList.add(wapperDriver);
        }

        return conList;
    }

    /**
     * DOC PLV Comment method "setDriverCache".
     * 
     * @param wapperDriver
     */
    public void setDriverCache(DriverShim wapperDriver) {
        DRIVER_CACHE.put(EDatabase4DriverClassName.MSSQL.getDriverClass(), wapperDriver);
    }

    public String getLibrariesPath(ECodeLanguage language) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerUIService.class)) {
            ILibraryManagerUIService libUiService = (ILibraryManagerUIService) GlobalServiceRegister.getDefault().getService(
                    ILibraryManagerUIService.class);
            return libUiService.getLibrariesPath(language);

        }
        return Platform.getInstallLocation().getURL().getFile() + "lib" + File.separator + "java";
    }

    public boolean checkFileCRCCode(File targetFile, File sourceFile) throws Exception {
        // Cyclic Redundancy Check(CRC)
        if (!targetFile.exists() || !sourceFile.exists()) {
            return true;
        }
        FileInputStream tagetFilestream = new FileInputStream(targetFile);
        CRC32 targertCrc32 = new CRC32();
        for (CheckedInputStream checkedinputstream = new CheckedInputStream(tagetFilestream, targertCrc32); checkedinputstream
                .read() != -1;) {
            //
        }
        FileInputStream sourceFilestream = new FileInputStream(sourceFile);
        CRC32 sourceCrc32 = new CRC32();
        for (CheckedInputStream checkedinputstream = new CheckedInputStream(sourceFilestream, sourceCrc32); checkedinputstream
                .read() != -1;) {
            //
        }
        tagetFilestream.close();
        sourceFilestream.close();
        return Long.toHexString(targertCrc32.getValue()).equals(Long.toHexString(sourceCrc32.getValue()));

    }

    public List getConnectionList(IMetadataConnection metadataConnection) {
        List list = getConnection(metadataConnection.getDbType(), metadataConnection.getUrl(), metadataConnection.getUsername(),
                metadataConnection.getPassword(), metadataConnection.getDatabase(), metadataConnection.getSchema(),
                metadataConnection.getDriverClass(), metadataConnection.getDriverJarPath(),
                metadataConnection.getDbVersionString(), metadataConnection.getAdditionalParams());
        return list;
    }

    /**
     * 
     * DOC YeXiaowei Comment method "getJavaLibPath".
     * 
     * @return
     */
    public String getJavaLibPath() {
        Project project = ProjectManager.getInstance().getCurrentProject();
        IProject physProject;
        String tmpFolder = System.getProperty("user.dir"); //$NON-NLS-1$
        try {
            physProject = ResourceUtils.getProject(project);
            if (physProject != null) {
                tmpFolder = physProject.getFolder("temp").getLocation().toPortableString(); //$NON-NLS-1$
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        tmpFolder = tmpFolder + "/dbWizard"; //$NON-NLS-1$
        File file = new File(tmpFolder);
        if (!file.exists()) {
            file.mkdirs();
        }
        return tmpFolder + "/"; //$NON-NLS-1$
    }

    // hywang added for bug 7038
    private List<String> getAllDBFuctions(DatabaseMetaData dbMetadata) {
        List<String> functionlist = new ArrayList<String>();
        if (dbMetadata == null) {
            return functionlist;
        }
        try {
            final String CommaSplit = ",\\s*"; //$NON-NLS-1$

            String[] systemFunctions = null;
            if (dbMetadata.getSystemFunctions() != null) {
                systemFunctions = dbMetadata.getSystemFunctions().split(CommaSplit);
            }
            String[] numericFunctions = null;
            if (dbMetadata.getNumericFunctions() != null) {
                numericFunctions = dbMetadata.getNumericFunctions().split(CommaSplit);
            }

            String[] stringFunctions = null;
            if (dbMetadata.getStringFunctions() != null) {
                stringFunctions = dbMetadata.getStringFunctions().split(CommaSplit);
            }

            String[] timeFunctions = null;
            if (dbMetadata.getTimeDateFunctions() != null) {
                timeFunctions = dbMetadata.getTimeDateFunctions().split(CommaSplit);
            }

            convertFunctions2Array(functionlist, systemFunctions);

            convertFunctions2Array(functionlist, numericFunctions);

            convertFunctions2Array(functionlist, stringFunctions);

            convertFunctions2Array(functionlist, timeFunctions);

        } catch (SQLException e) {
            ExceptionHandler.process(e);
        }
        return functionlist;
    }

    // hywang added for bug 7038
    private List<String> convertFunctions2Array(List<String> functionlist, String[] functions) {
        if (functions != null) {
            for (String function : functions) {
                functionlist.add(function);
            }

        }
        return functionlist;
    }

    private boolean useAllSynonyms;

    public boolean isUseAllSynonyms() {
        return useAllSynonyms;
    }

    public void setUseAllSynonyms(boolean val) {
        useAllSynonyms = val;
    }

    /**
     * DOC ycbai Comment method "getMeataConnectionSchema".
     * 
     * @param metadataConnection
     * @return
     */
    public String getMeataConnectionSchema(IMetadataConnection metadataConnection) {
        return ConvertionHelper.getMeataConnectionSchema(metadataConnection);
    }

    /**
     * DOC ycbai Comment method "getDBConnectionSchema".
     * 
     * @param dbConnection
     * @return
     */
    public String getDBConnectionSchema(DatabaseConnection dbConnection) {
        IMetadataConnection imetadataConnection = ConvertionHelper.convert(dbConnection, true);
        return imetadataConnection.getSchema();
    }

    public boolean isOLAPConnection(DatabaseConnection connection) {
        if (connection != null && EDatabaseTypeName.GENERAL_JDBC.getProduct().equals(connection.getProductId())) {
            String url = connection.getURL();
            if (url != null) {
                // String value = url.substring(url.indexOf(":") + 1);
                // String substring = value.substring(0, value.indexOf(":"));
                // if (substring.contains("olap")) {
                // return true;
                // }

                // FIXME why not to use like this
                if (url.toLowerCase().startsWith("jdbc:olap:")) { //$NON-NLS-1$
                    return true;
                }

            }
        }
        return false;
    }

    public boolean haveLoadMetadataNode() {
        boolean loadMetadata = PluginChecker.isPluginLoaded("org.talend.repository.metadata");
        return loadMetadata;
        // IRepositoryView repoView = RepositoryManagerHelper.findRepositoryView();
        // if (repoView != null) {
        // IProjectRepositoryNode root = repoView.getRoot();
        // final IRepositoryNode rootRepositoryNode = root.getRootRepositoryNode(ERepositoryObjectType.METADATA);
        // if (rootRepositoryNode != null) {
        // return true;
        // }
        // }
        // return false;
    }

    /**
     * DOC ycbai Comment method "handleDefaultValue".
     * 
     * @param column
     */
    public void handleDefaultValue(MetadataColumn column, DatabaseMetaData dbMetaData) {
        if (column == null) {
            return;
        }
        String talendType = column.getTalendType();
        if (talendType == null) {
            return;
        }
        Expression initialValue = column.getInitialValue();
        if (initialValue == null) {
            return;
        }
        String defautVal = initialValue.getBody();
        if (StringUtils.isEmpty(defautVal)) {
            return;
        }
        defautVal = defautVal.trim();
        boolean defaultValueIsFunction = false;
        List<String> functions = getAllDBFuctions(dbMetaData);
        if (functions.contains(defautVal)) {
            defaultValueIsFunction = true;
        }
        if (!defaultValueIsFunction) {
            if (talendType.equals(JavaTypesManager.INTEGER.getId()) || talendType.equals(JavaTypesManager.FLOAT.getId())
                    || talendType.equals(JavaTypesManager.DOUBLE.getId()) || talendType.equals(JavaTypesManager.LONG.getId())
                    || talendType.equals(JavaTypesManager.SHORT.getId())
                    || talendType.equals(JavaTypesManager.BIGDECIMAL.getId())
                    || talendType.equals(JavaTypesManager.CHARACTER.getId())) {
                defautVal = TalendQuoteUtils.removeQuotes(defautVal);
                if (column.getTalendType().equals(JavaTypesManager.CHARACTER.getId())) {
                    defautVal = TalendQuoteUtils.addQuotes(defautVal, TalendQuoteUtils.SINGLE_QUOTE);
                }
            } else {
                defautVal = TalendQuoteUtils.addSingleQuotesIfNotExist(defautVal);
            }
        }
        initialValue.setBody(defautVal);
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public boolean isReconnect() {
        return isReconnect;
    }

    public void setReconnect(boolean isReconnect) {
        this.isReconnect = isReconnect;
    }
}
