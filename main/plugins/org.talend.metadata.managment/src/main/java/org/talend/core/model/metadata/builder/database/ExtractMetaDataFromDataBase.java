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
package org.talend.core.model.metadata.builder.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.workbench.extensions.ExtensionImplementationProvider;
import org.talend.commons.utils.workbench.extensions.ExtensionPointLimiterImpl;
import org.talend.commons.utils.workbench.extensions.IExtensionPointLimiter;
import org.talend.core.database.EDatabase4DriverClassName;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.database.conn.template.EDatabaseConnTemplate;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.database.extractots.DBMetadataProviderObject;
import org.talend.core.model.metadata.builder.database.extractots.IDBMetadataProviderObject;
import org.talend.core.model.metadata.builder.database.manager.ExtractManager;
import org.talend.core.model.metadata.builder.database.manager.ExtractManagerFactory;
import org.talend.core.model.metadata.builder.database.manager.dbs.IBMDB2ExtractManager;
import org.talend.core.model.metadata.builder.database.manager.dbs.OracleExtractManager;
import org.talend.core.repository.model.connection.ConnectionStatus;
import org.talend.core.repository.model.provider.IDBMetadataProvider;
import org.talend.core.sqlbuilder.util.TextUtil;
import org.talend.cwm.relational.TdColumn;
import org.talend.metadata.managment.connection.manager.HiveConnectionManager;
import org.talend.metadata.managment.repository.ManagerConnection;
import org.talend.utils.sql.ConnectionUtils;
import org.talend.utils.sql.metadata.constants.GetTable;

import metadata.managment.i18n.Messages;

/**
 * DOC cantoine. Extract Meta Data Table. Contains all the Table and Metadata about a DB Connection. <br/>
 * 
 * $Id: ExtractMetaDataFromDataBase.java 44018 2010-06-12 02:22:29Z zli $
 * 
 */
public class ExtractMetaDataFromDataBase {

    public static List<IDBMetadataProviderObject> providerObjects = null;

    /**
     * 
     */

    /**
     * qzhang TableInfoParameters class global comment. Detailled comment <br/>
     * 
     */
    public enum ETableTypes {
        TABLETYPE_TABLE("TABLE"), //$NON-NLS-1$
        TABLETYPE_BASE_TABLE("BASE TABLE"), //for mariadb //$NON-NLS-1$ 
        TABLETYPE_VIEW("VIEW"), //$NON-NLS-1$
        TABLETYPE_SYNONYM("SYNONYM"), //$NON-NLS-1$
        TABLETYPE_CALCULATION_VIEW("CALCULATION VIEW"), //$NON-NLS-1$
        TABLETYPE_ALL_SYNONYM("ALL_SYNONYM"), //$NON-NLS-1$
        TABLETYPE_ALIAS("ALIAS"), //$NON-NLS-1$
        EXTERNAL_TABLE("EXTERNAL_TABLE"), //$NON-NLS-1$
        MANAGED_TABLE("MANAGED_TABLE"), //$NON-NLS-1$ // for hive
        INDEX_TABLE("INDEX_TABLE"), //$NON-NLS-1$ // for hive
        VIRTUAL_VIEW("VIRTUAL_VIEW"), //$NON-NLS-1$ // for hive
        SYSTEM_TABLE("SYSTEM TABLE"); //$NON-NLS-1$ //added for amazon redshift TDQ-11558 by msjian 

        private final String name;

        /**
         * qzhang TableInfoParameters constructor comment.
         */
        ETableTypes(String name) {
            this.name = name;
        }

        /**
         * Getter for name.
         * 
         * @return the name
         */
        public String getName() {
            return this.name;
        }

        public static ETableTypes getTableTypeFromName(String name) {
            for (ETableTypes type : values()) {
                if (type.name.equals(name)) {
                    return type;
                }
            }
            return null;
        }
    }

    private static Logger log = Logger.getLogger(ExtractMetaDataFromDataBase.class);

    /**
     * This map represents sets of table type and table name key value pair.
     */
    private static Map<String, String> tableTypeMap = new Hashtable<String, String>();

    public static Map<String, String> tableCommentsMap = new HashMap<String, String>();

    private static DatabaseMetaData oldMetadata = null;

    private static String oldSchema = null;

    private static int[] oldLimit;

    private static boolean oldUseAllSynonyms = false;

    private static List<IMetadataTable> oldMetadataRetrieved = null;

    static {
        providerObjects = new ArrayList<IDBMetadataProviderObject>();
        if (Platform.isRunning()) { // MOD sizhaoliu 2013-8-6 for TDQ-7453
            IExtensionPointLimiter actionExtensionPoint = new ExtensionPointLimiterImpl(
                    "org.talend.core.repository.metadata_provider", //$NON-NLS-1$
                    "DBMetadataProvider"); //$NON-NLS-1$
            List<IConfigurationElement> extension = ExtensionImplementationProvider.getInstanceV2(actionExtensionPoint);
            for (IConfigurationElement element : extension) {
                try {
                    String dbType = element.getAttribute("dbType");
                    String dbVersion = element.getAttribute("dbVersion");
                    String useJDBC = element.getAttribute("supportJDBC");
                    IDBMetadataProvider provider = (IDBMetadataProvider) element.createExecutableExtension("class");
                    IDBMetadataProviderObject object = new DBMetadataProviderObject();
                    object.setProvider(provider);
                    object.setDbType(dbType);
                    object.setDbVersion(dbVersion);
                    object.setSupportJDBC(Boolean.valueOf(useJDBC));
                    providerObjects.add(object);
                } catch (CoreException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * DOC cantoine. Method to return a Collection of Tables for a DB connection.
     * 
     * @param DatabaseMetaData dbMetaData
     * @return Collection of MetadataTable
     */
    public static List<IMetadataTable> extractTablesFromDB(DatabaseMetaData dbMetaData, IMetadataConnection iMetadataConnection,
            int... limit) {
        String schema = iMetadataConnection.getSchema();
        ExtractManager extractManager = ExtractManagerFactory.createByDisplayName(iMetadataConnection.getDbType());
        if (extractManager != null) {
            schema = extractManager.getSchema(iMetadataConnection);
        }
        if (dbMetaData.equals(oldMetadata) && schema.equals(oldSchema)
                && (limit == null && oldLimit == null || Arrays.equals(limit, oldLimit))
                && (oldUseAllSynonyms == ExtractMetaDataUtils.getInstance().isUseAllSynonyms())) {
            return oldMetadataRetrieved;
        }
        List<IMetadataTable> medataTables = new ArrayList<IMetadataTable>();
        if (extractManager != null) {
            medataTables = extractManager.extractTablesFromDB(dbMetaData, iMetadataConnection, limit);
            //
            tableTypeMap.putAll(extractManager.getTableTypeMap());
        }
        oldMetadata = dbMetaData;
        oldSchema = schema;
        oldLimit = limit;
        oldUseAllSynonyms = ExtractMetaDataUtils.getInstance().isUseAllSynonyms();
        oldMetadataRetrieved = medataTables;
        return medataTables;
    }

    public static synchronized List<TdColumn> returnColumns(IMetadataConnection iMetadataConnection, TableNode tableNode,
            boolean... dontCreateClose) {
        ExtractManager extractManager = ExtractManagerFactory.createByDisplayName(iMetadataConnection.getDbType());
        if (extractManager != null) {
            return extractManager.returnColumns(iMetadataConnection, tableNode, dontCreateClose);
        }
        return Collections.emptyList();

    }

    /**
     * DOC cantoine. Method to return a Collection of Column about a Table for a DB connection.
     * 
     * @param IMetadataConnection iMetadataConnection
     * @param String tableLabel
     * @return Collection of MetadataColumn Object of a Table
     * @deprecated
     */
    @Deprecated
    public static synchronized List<TdColumn> returnMetadataColumnsFormTable(IMetadataConnection iMetadataConnection,
            String tableLabel, boolean... dontCreateClose) {
        ExtractManager extractManager = ExtractManagerFactory.createByDisplayName(iMetadataConnection.getDbType());
        if (extractManager != null) {
            List<TdColumn> columns = extractManager.returnMetadataColumnsFormTable(iMetadataConnection, tableLabel,
                    dontCreateClose);
            tableCommentsMap.clear();
            tableCommentsMap.putAll(extractManager.getTableCommentsMap());
            return columns;
        }
        return Collections.emptyList();

    }

    /**
     * Retrieves table name by synonym, this method is only for Oracle as we cannot get column informations by metadata
     * in Oracle.
     * 
     * @param connection
     * 
     * @param name synonym
     * @param tableType
     * @return table name
     */
    public static String getTableNameBySynonym(Connection conn, String name) {
        Statement sta = null;

        try {
            if (conn.getMetaData().getDatabaseProductName().startsWith(IBMDB2ExtractManager.DATABASE_PRODUCT_NAME)) {
                ExtractManager extractManager = ExtractManagerFactory.create(EDatabaseTypeName.IBMDB2);
                if (extractManager != null) {
                    return extractManager.getTableNameBySynonyms(conn, name);
                }
            }
        } catch (SQLException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }

        try {
            if (conn.getMetaData().getDatabaseProductName().equals(EDatabaseTypeName.MSSQL.getDisplayName())) {
                ExtractManager extractManager = ExtractManagerFactory.create(EDatabaseTypeName.MSSQL);
                if (extractManager != null) {
                    return extractManager.getTableNameBySynonyms(conn, name);
                }
            }
        } catch (SQLException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }

        try {
            if (conn.getMetaData().getDatabaseProductName().equals(OracleExtractManager.DATABASE_PRODUCT_NAME)) {

                ExtractManager extractManager = ExtractManagerFactory.create(EDatabaseTypeName.ORACLEFORSID);
                if (extractManager != null) {
                    return extractManager.getTableNameBySynonyms(conn, name);
                }
            }
        } catch (SQLException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * DOC cantoine. Method to test DataBaseConnection.
     * 
     * @param dbVersionString
     * 
     * @param String driverClass
     * @param String urlString pwd
     * @param String username
     * @param String pwd
     * @return ConnectionStatus : the result of connection(boolean Result, String messageException)
     */
    public static ConnectionStatus testConnection(String dbType, String url, String username, String pwd, String schema,
            final String driverClassName, final String driverJarPath, String dbVersionString, String additionalParam) {
        return testConnection(dbType, url, username, pwd, schema, driverClassName, driverJarPath, dbVersionString,
                additionalParam, null);
    }

    public static ConnectionStatus testConnection(String dbType, String url, String username, String pwd, String schema,
            final String driverClassName, final String driverJarPath, String dbVersionString, String additionalParam,
            StringBuffer retProposedSchema) {
        Connection connection = null;
        ConnectionStatus connectionStatus = new ConnectionStatus();
        connectionStatus.setResult(false);
        DriverShim wapperDriver = null;
        try {
            List list = new ArrayList();

            list = ExtractMetaDataUtils.getInstance().connect(dbType, url, username, pwd, driverClassName, driverJarPath,
                    dbVersionString, additionalParam);
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
            schema = TextUtil.removeQuots(schema);
            if ((schema != null) && (schema.compareTo("") != 0)) { //$NON-NLS-1$
                final boolean notCaseSensitive = !ManagerConnection.isSchemaCaseSensitive(EDatabaseTypeName
                        .getTypeFromDisplayName(dbType));
                // We have to check schema
                if (!checkSchemaConnection(schema, connection, notCaseSensitive, dbType, retProposedSchema)) {
                    connectionStatus.setMessageException(Messages.getString("ExtractMetaDataFromDataBase.SchemaNoPresent")); //$NON-NLS-1$
                    return connectionStatus;
                }
            }

            connectionStatus.setResult(true);
            connectionStatus.setMessageException(Messages.getString("ExtractMetaDataFromDataBase.connectionSuccessful")); //$NON-NLS-1$
        } catch (SQLException e) {
            ExceptionHandler.process(e);
            connectionStatus.setMessageException(ExceptionUtils.getFullStackTrace(e));
        } catch (Exception e) {
            connectionStatus.setMessageException(ExceptionUtils.getFullStackTrace(e));
        } finally {
            if (connection != null) {
                ConnectionUtils.closeConnection(connection);
            }
            ExtractManager extractManager = ExtractManagerFactory.createByDisplayName(dbType);
            if (extractManager != null) {
                extractManager.closeConnection(null, wapperDriver);
                if (driverClassName.equals(EDatabase4DriverClassName.JAVADB_EMBEDED.getDriverClass())) {
                    extractManager.closeConnectionForDerby(wapperDriver);
                }
            }

        }
        return connectionStatus;
    }

    /**
     * Get the database meta data.
     * 
     * @param schema
     * @param connection
     * @return
     * @throws SQLException
     */
    public static boolean checkSchemaConnection(String schema, Connection connection, boolean notCaseSensitive, String dbType)
            throws SQLException {
        return checkSchemaConnection(schema, connection, notCaseSensitive, dbType, null);
    }

    public static boolean checkSchemaConnection(final String schema, Connection connection, boolean notCaseSensitive,
            String dbType, final StringBuffer retPropsedSchema) throws SQLException {
        ExtractMetaDataUtils extractMeta = ExtractMetaDataUtils.getInstance();
        DatabaseMetaData dbMetaData = extractMeta.getDatabaseMetaData(connection, dbType);
        final StringBuffer proposeSchema = new StringBuffer();
        if (dbMetaData != null) {
            ResultSet rs = dbMetaData.getSchemas();
            while (rs.next()) {
                if (notCaseSensitive) {
                    if (rs.getString(1).toLowerCase().compareTo(schema.toLowerCase()) == 0) {
                        extractMeta.setSchema(rs.getString(1));
                        rs.close();
                        return (true);
                    }
                } else {
                    String schemaFromDB = rs.getString(1);
                    if (schemaFromDB.toLowerCase().compareTo(schema.toLowerCase()) == 0) {
                        if (schemaFromDB.compareTo(schema) == 0) {
                            extractMeta.setSchema(schema);
                            rs.close();
                            return (true);
                        } else {
                            proposeSchema.append(schemaFromDB);
                        }
                    }
                }
            }
            rs.close();
        }
        if (retPropsedSchema != null && 0 < proposeSchema.length()) {
            final StringBuffer userSelectResult = new StringBuffer();
            Display.getDefault().syncExec(new Runnable() {

                @Override
                public void run() {
                    String title = Messages.getString("CheckConnection.CheckSchema.ProposeSchema.title"); //$NON-NLS-1$
                    String proposeMessage = Messages.getString("CheckConnection.CheckSchema.ProposeSchema.message", new Object[] { //$NON-NLS-1$
                            schema, proposeSchema });
                    MessageDialog messageDialog = new MessageDialog(new Shell(), title, null, proposeMessage,
                            MessageDialog.CONFIRM, new String[] { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL }, 0);
                    if (messageDialog.open() == 0) {
                        retPropsedSchema.append(proposeSchema);
                        userSelectResult.append("true"); //$NON-NLS-1$
                    }
                }
            });
            if (0 < userSelectResult.length()) {
                return true;
            }
        }
        return false;
    }

    /**
     * DOC cantoine.
     * 
     * @param IMetadataConnection iMetadataConnection
     * @return Collection : return a String's collection of Table Name of a DB Connection
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public static List<String> returnTablesFormConnection(IMetadataConnection iMetadataConnection, int... limit)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        List<String> itemTablesName = new ArrayList<String>();
        // add by wzhang
        // ExtractMetaDataUtils.metadataCon = iMetadataConnection;
        // end
        // bug 9162 bug 12888
        String dbType = iMetadataConnection.getDbType();
        String url = iMetadataConnection.getUrl();

        ExtractMetaDataUtils metaData = ExtractMetaDataUtils.getInstance();

        String generalJDBCDisplayName = EDatabaseConnTemplate.GENERAL_JDBC.getDBDisplayName();
        if (dbType.equals(generalJDBCDisplayName) && url.contains("oracle")) {//$NON-NLS-1$
            iMetadataConnection.setSchema(iMetadataConnection.getUsername().toUpperCase());
        }

        List list = metaData.getConnection(iMetadataConnection.getDbType(), url, iMetadataConnection.getUsername(),
                iMetadataConnection.getPassword(), iMetadataConnection.getDatabase(), iMetadataConnection.getSchema(),
                iMetadataConnection.getDriverClass(), iMetadataConnection.getDriverJarPath(),
                iMetadataConnection.getDbVersionString(), iMetadataConnection.getAdditionalParams());
        Connection conn = null;
        DriverShim wapperDriver = null;

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

        DatabaseMetaData dbMetaData = null;
        // Added by Marvin Wang on Mar. 13, 2013 for loading hive jars dynamically, refer to TDI-25072.
        if (EDatabaseTypeName.HIVE.getXmlName().equalsIgnoreCase(dbType)) {
            dbMetaData = HiveConnectionManager.getInstance().extractDatabaseMetaData(iMetadataConnection);
        } else {
            dbMetaData = metaData.getDatabaseMetaData(metaData.getConn(), dbType, iMetadataConnection.isSqlMode(),
                    iMetadataConnection.getDatabase());
        }

        List<IMetadataTable> metadataTables = ExtractMetaDataFromDataBase.extractTablesFromDB(dbMetaData, iMetadataConnection,
                limit);

        metaData.closeConnection();
        ExtractManager extractManager = ExtractManagerFactory.createByDisplayName(dbType);
        if (extractManager != null) {
            extractManager.closeConnection(iMetadataConnection, wapperDriver);
        }

        Iterator<IMetadataTable> iterate = metadataTables.iterator();
        while (iterate.hasNext()) {
            IMetadataTable metadataTable = iterate.next();
            itemTablesName.add(metadataTable.getLabel());
        }

        return itemTablesName;
    }

    /**
     * For Hive embedded mode to fetch all tables by "Fake Database Metadata". Notice that you need to setup some
     * configurations by
     * {@link JavaSqlFactory#doHivePreSetup(org.talend.core.model.metadata.builder.connection.Connection)} and then
     * remove these by {@link JavaSqlFactory#doHiveConfigurationClear()}. Added by Marvin Wang on Jan 8, 2013.
     * 
     * @param iMetadataConnection
     * @return
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public static List<String> fetchAllTablesForHiveEmbeddedModel(IMetadataConnection iMetadataConnection) throws SQLException,
            ClassNotFoundException, InstantiationException, IllegalAccessException {
        List<String> allTables = new ArrayList<String>();
        DatabaseMetaData dbMetaData = HiveConnectionManager.getInstance().extractDatabaseMetaData(iMetadataConnection);
        ResultSet results = dbMetaData.getTables(null, null, "%", new String[] { "TABLE" }); //$NON-NLS-1$//$NON-NLS-2$ 
        while (results.next()) {
            allTables.add(results.getString(3));
        }
        return allTables;
    }

    /**
     * DOC qiang.zhang Comment method "returnMetaTablesFormConnection".
     * 
     * @param iMetadataConnection
     * @return
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public static List<org.talend.core.model.metadata.builder.connection.MetadataTable> returnMetaTablesFormConnection(
            IMetadataConnection iMetadataConnection, int limit) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, SQLException {
        List<org.talend.core.model.metadata.builder.connection.MetadataTable> itemTablesName = new ArrayList<org.talend.core.model.metadata.builder.connection.MetadataTable>();
        ExtractMetaDataUtils extractMeta = ExtractMetaDataUtils.getInstance();
        List list = extractMeta.getConnection(iMetadataConnection.getDbType(), iMetadataConnection.getUrl(),
                iMetadataConnection.getUsername(), iMetadataConnection.getPassword(), iMetadataConnection.getDatabase(),
                iMetadataConnection.getSchema(), iMetadataConnection.getDriverClass(), iMetadataConnection.getDriverJarPath(),
                iMetadataConnection.getDbVersionString(), iMetadataConnection.getAdditionalParams());
        DriverShim wapperDriver = null;
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) instanceof DriverShim) {
                    wapperDriver = (DriverShim) list.get(i);
                }
            }
        }
        String dbType = iMetadataConnection.getDbType();
        DatabaseMetaData dbMetaData = null;
        // Added by Marvin Wang on Mar. 13, 2013 for loading hive jars dynamically, refer to TDI-25072.
        if (EDatabaseTypeName.HIVE.getXmlName().equalsIgnoreCase(dbType)) {
            dbMetaData = HiveConnectionManager.getInstance().extractDatabaseMetaData(iMetadataConnection);
        } else {
            dbMetaData = extractMeta.getDatabaseMetaData(extractMeta.getConn(), dbType, iMetadataConnection.isSqlMode(),
                    iMetadataConnection.getDatabase());
        }

        List<IMetadataTable> metadataTables = null;
        if (limit > 0) {
            metadataTables = ExtractMetaDataFromDataBase.extractTablesFromDB(dbMetaData, iMetadataConnection, limit);
        } else {
            metadataTables = ExtractMetaDataFromDataBase.extractTablesFromDB(dbMetaData, iMetadataConnection);
        }
        extractMeta.closeConnection();

        ExtractManager extractManager = ExtractManagerFactory.createByDisplayName(dbType);
        if (extractManager != null) {
            extractManager.closeConnection(null, wapperDriver);
            if (iMetadataConnection.getDriverClass().equals(EDatabase4DriverClassName.JAVADB_EMBEDED.getDriverClass())) {
                extractManager.closeConnectionForDerby(wapperDriver);
            }
        }

        Iterator<IMetadataTable> iterate = metadataTables.iterator();
        while (iterate.hasNext()) {
            IMetadataTable metadataTable = iterate.next();
            itemTablesName.add(ConvertionHelper.convert(metadataTable));
        }

        return itemTablesName;
    }

    public static List<org.talend.core.model.metadata.builder.connection.MetadataTable> returnMetaTablesFormConnection(
            IMetadataConnection iMetadataConnection) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, SQLException {
        return returnMetaTablesFormConnection(iMetadataConnection, -1);

    }

    /**
     * This method is used for getting table type(table,view or synonym etc.) by table name.
     * 
     * @param tableName a string representing table name
     * @return a string representing table type
     */
    public static String getTableTypeByTableName(String tableName) {
        if (!"".equals(tableName) && tableName != null) { //$NON-NLS-1$
            if (tableTypeMap.containsKey(tableName)) {
                return tableTypeMap.get(tableName);
            }
        }

        return null;
    }

    /**
     * DOC qzhang Comment method "returnTablesFormConnection".
     * 
     * @param metadataConnection
     * @param tableInfoParameters
     * @return
     */

    public static List<String> returnTablesFormConnection(IMetadataConnection iMetadataConnection,
            TableInfoParameters tableInfoParameters) {
        List<String> itemTablesName = new ArrayList<String>();
        if (iMetadataConnection == null || tableInfoParameters == null) {
            return itemTablesName;
        }
        ExtractManager extractManager = ExtractManagerFactory.createByDisplayName(iMetadataConnection.getDbType());
        if (extractManager != null) {
            itemTablesName = extractManager.returnTablesFormConnection(iMetadataConnection, tableInfoParameters);
            tableTypeMap.putAll(extractManager.getTableTypeMap());
        }

        return itemTablesName;
    }

    /**
     * DOC qzhang Comment method "getTableNamesFromQuery".
     * 
     * @param rsTables
     * 
     * @return
     * @throws SQLException
     */
    public static List<String> getTableNamesFromQuery(ResultSet resultSet, Connection connection) throws SQLException {
        List<String> itemTablesName = new ArrayList<String>();
        tableCommentsMap.clear();
        while (resultSet.next()) {
            String nameKey = resultSet.getString(1).trim();
            String tableComment = getTableComment(nameKey, resultSet, false, connection);
            if (tableCommentsMap.containsKey(nameKey)) {
                if (tableComment == null) {
                    tableComment = "";
                }
                tableCommentsMap.remove(nameKey);
                tableCommentsMap.put(nameKey, tableComment);
            }
            itemTablesName.add(nameKey);
            tableCommentsMap.put(nameKey, tableComment);
        }
        return itemTablesName;
    }

    public static String getTableComment(String tableName, ResultSet tablesSet, boolean needRemark, Connection connection)
            throws SQLException {
        String tableComment = "";
        if (needRemark) {
            tableComment = tablesSet.getString(GetTable.REMARKS.name());
        }
        if (StringUtils.isBlank(tableComment)) {
            String selectRemarkOnTable = getSelectRemarkOnTable(tableName);
            if (selectRemarkOnTable != null && connection != null) {
                tableComment = executeGetCommentStatement(selectRemarkOnTable, connection);
            }
        }
        return tableComment;
    }

    private static String getSelectRemarkOnTable(String tableName) {
        return "SELECT TABLE_COMMENT FROM information_schema.TABLES WHERE TABLE_NAME='" + tableName + "'"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    private static String executeGetCommentStatement(String queryStmt, Connection connection) {
        String comment = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            statement.execute(queryStmt);

            // get the results
            resultSet = statement.getResultSet();
            if (resultSet != null) {
                while (resultSet.next()) {
                    comment = (String) resultSet.getObject(1);
                }
            }
        } catch (SQLException e) {
            // do nothing here
        } finally {
            // -- release resources
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    log.error(e, e);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    log.error(e, e);
                }
            }
        }
        return comment;
    }

    public static IDBMetadataProvider getProviderByDbType(String dbTypeString) {
        for (IDBMetadataProviderObject extractorObject : providerObjects) {
            if (extractorObject.getDbType().equals(dbTypeString)) {
                return extractorObject.getProvider();
            }
        }
        return null;
    }

    public static IDBMetadataProviderObject getProviderObjectByDbType(String dbTypeString) {
        for (IDBMetadataProviderObject extractorObject : providerObjects) {
            if (extractorObject.getDbType().equals(dbTypeString)) {
                return extractorObject;
            }
        }
        return null;
    }

}
