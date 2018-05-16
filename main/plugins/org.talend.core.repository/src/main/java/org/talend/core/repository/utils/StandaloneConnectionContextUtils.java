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
package org.talend.core.repository.utils;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Properties;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.database.conn.ConnParameterKeys;
import org.talend.core.database.conn.DatabaseConnStrUtil;
import org.talend.core.database.conn.template.DbConnStrForHive;
import org.talend.core.model.metadata.Dbms;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.EbcdicConnection;
import org.talend.core.model.metadata.builder.connection.FileConnection;
import org.talend.core.model.metadata.builder.connection.FileExcelConnection;
import org.talend.core.model.metadata.builder.connection.HL7Connection;
import org.talend.core.model.metadata.builder.connection.PositionalFileConnection;
import org.talend.core.model.metadata.builder.connection.RegexpFileConnection;
import org.talend.core.model.metadata.connection.hive.HiveServerVersionInfo;
import org.talend.core.model.utils.CloneConnectionUtils;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.model.bridge.ReponsitoryContextBridge;

/**
 * ADD sizhaoliu 2013-04-16
 * 
 */
public class StandaloneConnectionContextUtils {

    private static final String EMPTY_STRING = "";

    /**
     * DOC sizhaoliu Comment method "cloneOriginalValueConnection".
     * 
     * @param dbConn
     * @param contextProperties
     * @return
     */
    public static DatabaseConnection cloneOriginalValueConnection(DatabaseConnection dbConn, Properties contextProperties) {
        if (dbConn == null) {
            return null;
        }
        DatabaseConnection cloneConn = ConnectionFactory.eINSTANCE.createDatabaseConnection();
        // get values
        String server = getOriginalValue(contextProperties, dbConn.getServerName());
        String username = getOriginalValue(contextProperties, dbConn.getUsername());
        String password = getOriginalValue(contextProperties, dbConn.getRawPassword());
        String port = getOriginalValue(contextProperties, dbConn.getPort());
        String sidOrDatabase = getOriginalValue(contextProperties, dbConn.getSID());
        String datasource = getOriginalValue(contextProperties, dbConn.getDatasourceName());
        String filePath = getOriginalValue(contextProperties, dbConn.getFileFieldName());
        String schemaOracle = getOriginalValue(contextProperties, dbConn.getUiSchema());
        String dbRootPath = getOriginalValue(contextProperties, dbConn.getDBRootPath());
        String additionParam = getOriginalValue(contextProperties, dbConn.getAdditionalParams());
        String url = getOriginalValue(contextProperties, dbConn.getURL());
        String className = getOriginalValue(contextProperties, dbConn.getDriverClass());
        String jarPath = getOriginalValue(contextProperties, dbConn.getDriverJarPath());

        // hyWang add for bug 0007252
        String dbmsID = getOriginalValue(contextProperties, dbConn.getDbmsId());

        filePath = TalendQuoteUtils.removeQuotes(filePath);
        dbRootPath = TalendQuoteUtils.removeQuotes(dbRootPath);
        cloneConn.setAdditionalParams(additionParam);
        cloneConn.setDatasourceName(datasource);
        cloneConn.setDBRootPath(dbRootPath);
        cloneConn.setFileFieldName(filePath);
        cloneConn.setRawPassword(password); // the password is raw.
        cloneConn.setPort(port);
        cloneConn.setUiSchema(schemaOracle);
        cloneConn.setServerName(server);
        cloneConn.setSID(sidOrDatabase);
        cloneConn.setUsername(username);
        cloneConn.setDriverJarPath(jarPath);

        cloneConn.setComment(dbConn.getComment());
        cloneConn.setDatabaseType(dbConn.getDatabaseType());
        cloneConn.setDbmsId(dbmsID);
        cloneConn.setDivergency(dbConn.isDivergency());
        cloneConn.setDbVersionString(dbConn.getDbVersionString());
        cloneConn.setId(dbConn.getId());
        cloneConn.setLabel(dbConn.getLabel());
        cloneConn.setNullChar(dbConn.getNullChar());
        cloneConn.setProductId(dbConn.getProductId());
        cloneConn.setSqlSynthax(dbConn.getSqlSynthax());
        cloneConn.setStandardSQL(dbConn.isStandardSQL());
        cloneConn.setStringQuote(dbConn.getStringQuote());
        cloneConn.setSynchronised(dbConn.isSynchronised());
        cloneConn.setSystemSQL(dbConn.isSystemSQL());
        cloneConn.setVersion(dbConn.getVersion());
        cloneConn.setReadOnly(dbConn.isReadOnly());
        cloneConn.setDriverClass(className);
        cloneConn.setName(dbConn.getName());
        cloneOtherParameters(dbConn, cloneConn);
        if (dbConn.isSetSQLMode()) {
            cloneConn.setSQLMode(dbConn.isSQLMode());
        } else {
            // set true by default as it's only used actually for teradata.
            // should be modified if default value is changed later.
            cloneConn.setSQLMode(true);
        }

        // cloneConn.setProperties(dbConn.getProperties());
        // cloneConn.setCdcConns(dbConn.getCdcConns());
        // cloneConn.setQueries(dbConn.getQueries());
        // cloneConn.getTables().addAll(dbConn.getTables());
        /*
         * mustn't be set, is flag for method convert in class ConvertionHelper.
         * 
         * working for sql builder especially.
         */
        // cloneConn.setContextId(dbConn.getContextId());
        // cloneConn.setContextMode(dbConn.isContextMode()); // if use context

        // for hive :
        if (EDatabaseTypeName.HIVE.equals(EDatabaseTypeName.getTypeFromDbType(dbConn.getDatabaseType()))) {
            String hadoopUserName = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_USERNAME);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_USERNAME,
                    getOriginalValue(contextProperties, hadoopUserName));
            String jobTracker = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_JOB_TRACKER_URL);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_JOB_TRACKER_URL,
                    getOriginalValue(contextProperties, jobTracker));
            String nameNode = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_NAME_NODE_URL);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_NAME_NODE_URL,
                    getOriginalValue(contextProperties, nameNode));
            String hivePrincipal = cloneConn.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_HIVEPRINCIPLA);
            cloneConn.getParameters().put(ConnParameterKeys.HIVE_AUTHENTICATION_HIVEPRINCIPLA,
                    getOriginalValue(contextProperties, hivePrincipal));

            String hiveMetadata = cloneConn.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_METASTOREURL);
            cloneConn.getParameters().put(ConnParameterKeys.HIVE_AUTHENTICATION_METASTOREURL,
                    getOriginalValue(contextProperties, hiveMetadata));

            String driverPath = cloneConn.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_DRIVERJAR_PATH);
            cloneConn.getParameters().put(ConnParameterKeys.HIVE_AUTHENTICATION_DRIVERJAR_PATH,
                    getOriginalValue(contextProperties, driverPath));

            String driverClass = cloneConn.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_DRIVERCLASS);
            cloneConn.getParameters().put(ConnParameterKeys.HIVE_AUTHENTICATION_DRIVERCLASS,
                    getOriginalValue(contextProperties, driverClass));

            String hiveUserName = cloneConn.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_USERNAME);
            cloneConn.getParameters().put(ConnParameterKeys.HIVE_AUTHENTICATION_USERNAME,
                    getOriginalValue(contextProperties, hiveUserName));

            String hivePassword = cloneConn.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_PASSWORD);
            cloneConn.getParameters().put(ConnParameterKeys.HIVE_AUTHENTICATION_PASSWORD,
                    getOriginalValue(contextProperties, hivePassword));

            String ktPrincipal = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL,
                    getOriginalValue(contextProperties, ktPrincipal));

            String keytab = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_KEYTAB);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_KEYTAB, getOriginalValue(contextProperties, keytab));

            String maprticket_Username = cloneConn.getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_USERNAME);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_USERNAME,
                    getOriginalValue(contextProperties, maprticket_Username));

            String maprticket_Password = cloneConn.getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_PASSWORD);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_PASSWORD,
                    getOriginalValue(contextProperties, maprticket_Password));

            String maprticket_Cluster = cloneConn.getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_CLUSTER);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_CLUSTER,
                    getOriginalValue(contextProperties, maprticket_Cluster));

            String maprticket_Duration = cloneConn.getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_DURATION);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_DURATION,
                    getOriginalValue(contextProperties, maprticket_Duration));

            String jdbcPropertiesString = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_JDBC_PROPERTIES);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_JDBC_PROPERTIES,
                    getOriginalValue(contextProperties, jdbcPropertiesString));

            String additionalJDBCSettings = cloneConn.getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_HIVE_ADDITIONAL_JDBC_SETTINGS);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_ADDITIONAL_JDBC_SETTINGS,
                    getOriginalValue(contextProperties, additionalJDBCSettings));

            String propertiesString = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_PROPERTIES);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_PROPERTIES,
                    getOriginalValue(contextProperties, propertiesString));

            String trustStorePath = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PATH);
            if (trustStorePath != null) {
                cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PATH,
                        getOriginalValue(contextProperties, trustStorePath));
            }

            String trustStorePassword = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PASSWORD);
            if (trustStorePassword != null) {
                cloneConn.getParameters().put(
                        ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PASSWORD,
                        cloneConn.getValue(cloneConn.getValue(getOriginalValue(contextProperties, trustStorePassword), false),
                                true));
            }

            String template = null;
            String hiveServerVersion = HiveServerVersionInfo.HIVE_SERVER_1.getKey();
            EMap<String, String> parameterMap = dbConn.getParameters();
            if (parameterMap != null) {
                hiveServerVersion = parameterMap.get(ConnParameterKeys.HIVE_SERVER_VERSION);
            }

            if (HiveServerVersionInfo.HIVE_SERVER_2.getKey().equals(hiveServerVersion)) {
                template = DbConnStrForHive.URL_HIVE_2_TEMPLATE;
            } else {
                template = DbConnStrForHive.URL_HIVE_1_TEMPLATE;
            }
            // the original value is a contexted value
            String newURl = DatabaseConnStrUtil.getHiveURLString(cloneConn, server, port, sidOrDatabase, template);

            cloneConn.setURL(newURl);
            return cloneConn;
        }

        // for Hbase
        if (EDatabaseTypeName.HBASE.equals(EDatabaseTypeName.getTypeFromDbType(cloneConn.getDatabaseType()))) {
            String hbaseMasterPrin = cloneConn.getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MASTERPRINCIPAL);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MASTERPRINCIPAL,
                    getOriginalValue(contextProperties, hbaseMasterPrin));

            String hbaseRegionPrin = cloneConn.getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_REGIONSERVERPRINCIPAL);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_REGIONSERVERPRINCIPAL,
                    getOriginalValue(contextProperties, hbaseRegionPrin));

            String hbaseKeyTabPrin = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL,
                    getOriginalValue(contextProperties, hbaseKeyTabPrin));

            String hbaseKeyTab = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_KEYTAB);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_KEYTAB,
                    getOriginalValue(contextProperties, hbaseKeyTab));

            String maprticket_Username = cloneConn.getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_USERNAME);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_USERNAME,
                    getOriginalValue(contextProperties, maprticket_Username));

            String maprticket_Password = cloneConn.getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_PASSWORD);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_PASSWORD,
                    getOriginalValue(contextProperties, maprticket_Password));

            String maprticket_Cluster = cloneConn.getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_CLUSTER);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_CLUSTER,
                    getOriginalValue(contextProperties, maprticket_Cluster));

            String maprticket_Duration = cloneConn.getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_DURATION);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_DURATION,
                    getOriginalValue(contextProperties, maprticket_Duration));

            String tableNSMapping = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_TABLE_NS_MAPPING);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_TABLE_NS_MAPPING,
                    getOriginalValue(contextProperties, tableNSMapping));
        }

        // for Maprdb
        if (EDatabaseTypeName.MAPRDB.equals(EDatabaseTypeName.getTypeFromDbType(cloneConn.getDatabaseType()))) {
            String maprdbMasterPrin = cloneConn.getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MASTERPRINCIPAL);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MASTERPRINCIPAL,
                    getOriginalValue(contextProperties, maprdbMasterPrin));

            String maprdbRegionPrin = cloneConn.getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_REGIONSERVERPRINCIPAL);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_REGIONSERVERPRINCIPAL,
                    getOriginalValue(contextProperties, maprdbRegionPrin));

            String znodeParent = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_ZNODE_PARENT);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_ZNODE_PARENT,
                    getOriginalValue(contextProperties, znodeParent));

            String maprdbKeyTabPrin = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL,
                    getOriginalValue(contextProperties, maprdbKeyTabPrin));

            String maprdbKeyTab = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_KEYTAB);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_KEYTAB,
                    getOriginalValue(contextProperties, maprdbKeyTab));

            String maprticket_Username = cloneConn.getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_USERNAME);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_USERNAME,
                    getOriginalValue(contextProperties, maprticket_Username));

            String maprticket_Password = cloneConn.getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_PASSWORD);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_PASSWORD,
                    getOriginalValue(contextProperties, maprticket_Password));

            String maprticket_Cluster = cloneConn.getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_CLUSTER);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_CLUSTER,
                    getOriginalValue(contextProperties, maprticket_Cluster));

            String maprticket_Duration = cloneConn.getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_DURATION);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_DURATION,
                    getOriginalValue(contextProperties, maprticket_Duration));

            String tableNSMapping = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_TABLE_NS_MAPPING);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_TABLE_NS_MAPPING,
                    getOriginalValue(contextProperties, tableNSMapping));
        }

        // TDI-28124:tdb2input can't guess schema from join sql on system table
        if (EDatabaseTypeName.IBMDB2.equals(EDatabaseTypeName.getTypeFromDbType(dbConn.getDatabaseType()))) {
            String cursorForDb2 = ":cursorSensitivity=2;";
            String database = sidOrDatabase + cursorForDb2;
            String newURL = DatabaseConnStrUtil.getURLString(cloneConn.getDatabaseType(), dbConn.getDbVersionString(), server,
                    username, password, port, database, filePath.toLowerCase(), datasource, dbRootPath, additionParam);
            cloneConn.setURL(newURL);
            return cloneConn;
        }

        if (EDatabaseTypeName.IMPALA.equals(EDatabaseTypeName.getTypeFromDbType(dbConn.getDatabaseType()))) {
            String impalaAuthPrinciple = cloneConn.getParameters().get(ConnParameterKeys.IMPALA_AUTHENTICATION_PRINCIPLA);
            if (impalaAuthPrinciple != null) {
                cloneConn.getParameters().put(ConnParameterKeys.IMPALA_AUTHENTICATION_PRINCIPLA,
                        getOriginalValue(contextProperties, impalaAuthPrinciple));
            }
        }

        if (EDatabaseTypeName.ORACLE_CUSTOM.equals(EDatabaseTypeName.getTypeFromDbType(dbConn.getDatabaseType()))) {
            String trustStorePath = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PATH);
            if (trustStorePath != null) {
                cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PATH,
                        getOriginalValue(contextProperties, trustStorePath));
            }

            String trustStorePassword = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PASSWORD);
            if (trustStorePassword != null) {
                cloneConn.getParameters().put(
                        ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PASSWORD,
                        cloneConn.getValue(cloneConn.getValue(getOriginalValue(contextProperties, trustStorePassword), false),
                                true));
            }

            String keyStorePath = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_KEY_STORE_PATH);
            if (keyStorePath != null) {
                cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_SSL_KEY_STORE_PATH,
                        getOriginalValue(contextProperties, keyStorePath));
            }

            String keyStorePassword = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_KEY_STORE_PASSWORD);
            if (keyStorePassword != null) {
                cloneConn.getParameters()
                        .put(ConnParameterKeys.CONN_PARA_KEY_SSL_KEY_STORE_PASSWORD,
                                cloneConn.getValue(
                                        cloneConn.getValue(getOriginalValue(contextProperties, keyStorePassword), false), true));
            }
        }

        // Added 20130311 TDQ-7000, when it is context mode and not general
        // jdbc, reset the url.
        if (dbConn.isContextMode()
                && !EDatabaseTypeName.GENERAL_JDBC.equals(EDatabaseTypeName.getTypeFromDbType(dbConn.getDatabaseType()))) {
            String newURL = DatabaseConnStrUtil.getURLString(cloneConn.getDatabaseType(), dbConn.getDbVersionString(), server,
                    username, password, port, sidOrDatabase, filePath == null ? filePath : filePath.toLowerCase(), datasource,
                    dbRootPath, additionParam);
            cloneConn.setURL(newURL);
            return cloneConn;
        }// ~

        if (dbConn.getURL() != null && !dbConn.getURL().equals("")) { //$NON-NLS-1$
            // for general db, url is given directly.
            cloneConn.setURL(url);
        } else {
            String newURL = DatabaseConnStrUtil.getURLString(cloneConn.getDatabaseType(), dbConn.getDbVersionString(), server,
                    username, password, port, sidOrDatabase, filePath.toLowerCase(), datasource, dbRootPath, additionParam);
            cloneConn.setURL(newURL);
        }
        return cloneConn;
    }

    /**
     * DOC sizhaoliu Comment method "getOriginalValue".
     * 
     * @param contextProperties
     * @param dbmsId
     * @return
     */
    private static String getOriginalValue(Properties contextProperties, String value) {
        if (value != null && ContextParameterUtils.containContextVariables(value)) {
            String var = ContextParameterUtils.getVariableFromCode(value);
            String res = contextProperties.getProperty(var);
            return res != null ? res : EMPTY_STRING;
        }
        return value;
    }

    /**
     * Clones other parameters from the original parameters. Added by Marvin Wang on Aug 13, 2012.
     * 
     * @param dbConn
     * @param cloneConn
     */
    protected static void cloneOtherParameters(DatabaseConnection dbConn, DatabaseConnection cloneConn) {
        EMap<String, String> originalParams = dbConn.getParameters();
        if (originalParams != null && originalParams.size() > 0) {
            EMap<String, String> clonedMap = cloneConn.getParameters();
            for (Entry<String, String> entry : originalParams.entrySet()) {
                clonedMap.put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * DOC sizhaoliu Comment method "setMetadataConnectionParameter".
     * 
     * @param dbConn
     * @param metaConn
     * @param contextProperties
     * @return
     */
    public static void setMetadataConnectionParameter(DatabaseConnection conn, IMetadataConnection metadataConnection,
            Properties contextProperties) {

        if (conn == null || metadataConnection == null) {
            return;
        }
        ContextType contextType = getContextTypeForContextMode(conn, conn.getContextName());
        if (contextType == null) {
            return;
        }

        // driverPath
        metadataConnection.setDriverJarPath(getOriginalValue(contextProperties, conn.getDriverJarPath()));

        // set dbType
        metadataConnection.setDbType(getOriginalValue(contextProperties, conn.getDatabaseType()));
        // set product(ProductId) and Schema(UISchema)
        EDatabaseTypeName edatabasetypeInstance = EDatabaseTypeName.getTypeFromDisplayName(getOriginalValue(contextProperties,
                conn.getDatabaseType()));
        String product = edatabasetypeInstance.getProduct();
        metadataConnection.setProduct(product);
        // set mapping(DbmsId)
        if (!ReponsitoryContextBridge.isDefautProject()) {
            Dbms defaultDbmsFromProduct = MetadataTalendType.getDefaultDbmsFromProduct(product);
            if (defaultDbmsFromProduct != null) {
                String mapping = defaultDbmsFromProduct.getId();
                metadataConnection.setMapping(mapping);
            }
        }
        // set dbVersionString
        metadataConnection.setDbVersionString(getOriginalValue(contextProperties, conn.getDbVersionString()));

        // filePath
        metadataConnection.setFileFieldName(getOriginalValue(contextProperties, conn.getFileFieldName()));
        // jdbcUrl
        metadataConnection.setUrl(getOriginalValue(contextProperties, conn.getURL()));
        // aDDParameter
        metadataConnection.setAdditionalParams(getOriginalValue(contextProperties, conn.getAdditionalParams()));
        // driverClassName
        metadataConnection.setDriverClass(getOriginalValue(contextProperties, conn.getDriverClass()));
        // host
        metadataConnection.setServerName(getOriginalValue(contextProperties, conn.getServerName()));
        // port
        metadataConnection.setPort(getOriginalValue(contextProperties, conn.getPort()));
        // dbName
        metadataConnection.setDatabase(getOriginalValue(contextProperties, conn.getSID()));
        // otherParameter
        metadataConnection.setOtherParameter(getOriginalValue(contextProperties, ConnectionHelper.getOtherParameter(conn)));
        // password
        metadataConnection.setPassword(getOriginalValue(contextProperties, ConnectionHelper.getPassword(conn)));
        // user
        metadataConnection.setUsername(getOriginalValue(contextProperties, conn.getUsername()));
        // dbName
        metadataConnection.setDataSourceName(getOriginalValue(contextProperties, conn.getDatasourceName()));
        // schema
        metadataConnection.setSchema(conn.getUiSchema());
        // dbmsId
        if (metadataConnection.getMapping() == null) {
            metadataConnection.setMapping(getOriginalValue(contextProperties, conn.getDbmsId()));
        }

    }

    private static ContextType getContextTypeForContextMode(DatabaseConnection conn, String contextName) {
        return null;
    }

    public static FileConnection cloneOriginalValueConnection(FileConnection fileConn, Properties contextProperties) {
        if (fileConn == null) {
            return null;
        }

        FileConnection cloneConn = null;
        if (fileConn instanceof DelimitedFileConnection) {
            cloneConn = ConnectionFactory.eINSTANCE.createDelimitedFileConnection();
        } else if (fileConn instanceof PositionalFileConnection) {
            cloneConn = ConnectionFactory.eINSTANCE.createPositionalFileConnection();
        } else if (fileConn instanceof RegexpFileConnection) {
            cloneConn = ConnectionFactory.eINSTANCE.createRegexpFileConnection();
        } else if (fileConn instanceof FileExcelConnection) {
            cloneConn = ConnectionFactory.eINSTANCE.createFileExcelConnection();
        } else if (fileConn instanceof HL7Connection) {
            cloneConn = ConnectionFactory.eINSTANCE.createHL7Connection();
        } else if (fileConn instanceof EbcdicConnection) {
            cloneConn = ConnectionFactory.eINSTANCE.createEbcdicConnection();
        }

        if (cloneConn != null) {
            String filePath = getOriginalValue(contextProperties, fileConn.getFilePath());
            String encoding = getOriginalValue(contextProperties, fileConn.getEncoding());
            String headValue = getOriginalValue(contextProperties, fileConn.getHeaderValue());
            String footerValue = getOriginalValue(contextProperties, fileConn.getFooterValue());
            String limitValue = getOriginalValue(contextProperties, fileConn.getLimitValue());

            filePath = TalendQuoteUtils.removeQuotes(filePath);
            // qli modified to fix the bug 6995.
            encoding = TalendQuoteUtils.removeQuotes(encoding);
            cloneConn.setFilePath(filePath);
            cloneConn.setEncoding(encoding);
            cloneConn.setHeaderValue(headValue);
            cloneConn.setFooterValue(footerValue);
            cloneConn.setLimitValue(limitValue);

            //
            if (fileConn instanceof DelimitedFileConnection || fileConn instanceof PositionalFileConnection
                    || fileConn instanceof RegexpFileConnection) {
                String fieldSeparatorValue = getOriginalValue(contextProperties, fileConn.getFieldSeparatorValue());
                String rowSeparatorValue = getOriginalValue(contextProperties, fileConn.getRowSeparatorValue());

                cloneConn.setFieldSeparatorValue(fieldSeparatorValue);
                cloneConn.setRowSeparatorValue(rowSeparatorValue);

                if (fileConn instanceof DelimitedFileConnection) {
                    ((DelimitedFileConnection) cloneConn).setFieldSeparatorType(((DelimitedFileConnection) fileConn)
                            .getFieldSeparatorType());
                }
            }
            // excel
            if (fileConn instanceof FileExcelConnection) {
                FileExcelConnection excelConnection = (FileExcelConnection) fileConn;
                FileExcelConnection cloneExcelConnection = (FileExcelConnection) cloneConn;

                String thousandSeparator = getOriginalValue(contextProperties, excelConnection.getThousandSeparator());
                String decimalSeparator = getOriginalValue(contextProperties, excelConnection.getDecimalSeparator());
                String firstColumn = getOriginalValue(contextProperties, excelConnection.getFirstColumn());
                String lastColumn = getOriginalValue(contextProperties, excelConnection.getLastColumn());

                cloneExcelConnection.setThousandSeparator(thousandSeparator);
                cloneExcelConnection.setDecimalSeparator(decimalSeparator);
                cloneExcelConnection.setFirstColumn(firstColumn);
                cloneExcelConnection.setLastColumn(lastColumn);

                cloneExcelConnection.setSelectAllSheets(excelConnection.isSelectAllSheets());
                cloneExcelConnection.setSheetName(excelConnection.getSheetName());

                ArrayList sheetList = excelConnection.getSheetList();
                cloneExcelConnection.setSheetList((ArrayList) sheetList.clone());

                EList sheetColumns = excelConnection.getSheetColumns();
                if (sheetColumns != null && sheetColumns instanceof BasicEList) {
                    cloneExcelConnection.getSheetColumns().addAll((EList) ((BasicEList) sheetColumns).clone());
                }

                cloneExcelConnection.setAdvancedSpearator(excelConnection.isAdvancedSpearator());

                cloneConn.setFieldSeparatorValue(fileConn.getFieldSeparatorValue());
                cloneConn.setRowSeparatorType(fileConn.getRowSeparatorType());
                cloneConn.setRowSeparatorValue(fileConn.getRowSeparatorValue());
            }

            cloneConn.setRowSeparatorType(fileConn.getRowSeparatorType());

            cloneConn.setCsvOption(fileConn.isCsvOption());
            cloneConn.setEscapeChar(fileConn.getEscapeChar());
            cloneConn.setEscapeType(fileConn.getEscapeType());
            cloneConn.setFirstLineCaption(fileConn.isFirstLineCaption());
            cloneConn.setFormat(fileConn.getFormat());
            cloneConn.setRemoveEmptyRow(fileConn.isRemoveEmptyRow());
            cloneConn.setServer(fileConn.getServer());
            cloneConn.setTextEnclosure(fileConn.getTextEnclosure());
            cloneConn.setTextIdentifier(fileConn.getTextIdentifier());
            cloneConn.setUseFooter(fileConn.isUseFooter());
            cloneConn.setUseHeader(fileConn.isUseHeader());
            cloneConn.setUseLimit(fileConn.isUseLimit());

            CloneConnectionUtils.cloneConnectionProperties(fileConn, cloneConn);
        }
        return cloneConn;
    }

}
