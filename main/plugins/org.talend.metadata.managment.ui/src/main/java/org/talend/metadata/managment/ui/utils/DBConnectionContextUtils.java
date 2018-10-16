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
package org.talend.metadata.managment.ui.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EMap;
import org.talend.commons.ui.utils.PathUtils;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.database.conn.ConnParameterKeys;
import org.talend.core.database.conn.DatabaseConnStrUtil;
import org.talend.core.database.conn.template.DbConnStrForHive;
import org.talend.core.database.conn.template.EDatabaseConnTemplate;
import org.talend.core.hadoop.IHadoopClusterService;
import org.talend.core.hadoop.repository.HadoopRepositoryUtil;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.context.ContextUtils;
import org.talend.core.model.metadata.Dbms;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.connection.hive.HiveServerVersionInfo;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.ui.context.model.table.ConectionAdaptContextVariableModel;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SchemaHelper;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.metadata.managment.repository.ManagerConnection;
import org.talend.metadata.managment.ui.model.IConnParamName;
import org.talend.model.bridge.ReponsitoryContextBridge;

import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * ggu class global comment. Detailled comment
 */
public final class DBConnectionContextUtils {

    private static final ECodeLanguage LANGUAGE = LanguageManager.getCurrentLanguage();

    /**
     * 
     */
    public enum EDBParamName implements IConnParamName {
        Login,
        Password,
        // hshen
        JdbcUrl,
        DriverJar,
        ClassName,
        MappingFile,
        Port,
        Server,
        Schema,
        AdditionalParams,
        Datasource,
        DBRootPath,
        File,
        // DQ datamart
        OutputFolder,
        LogoFile,
        //
        Sid,
        Database,
        ServiceName,
        // hive
        NameNode,
        JobTrackerOrResourceManager,
        HivePrincipal,
        HiveMetastore,
        HiveDriverJar,
        HiveDriveClass,
        HiveUserName,
        HivePassword,
        HiveKeyTabPrincipal,
        HiveKeyTab,
        HiveMetastorePort,
        hiveAdditionalJDBCParameters,
        hiveEnableHa,
        hiveMetastoreUris,
        hiveUseSSL,
        hiveSSLTrustStorePath,
        hiveSSLTrustStorePassword,

        // hbase
        MasterPrincipal,
        RegionPrincipal,
        HbaseKeyTabPrincipal,
        HbaseKeyTab,

        // maprdb
        MaprdbKeyTabPrincipal,
        MaprdbKeyTab,
        TableNSMapping,

        Username,
        Maprticket_Password,
        Maprticket_Cluster,
        Maprticket_Duration,
        Znode_Parent,
        // impala
        ImpalaPrincipal,

        // Oracle Custom SSL Encryption
        SSLTrustStorePath,
        SSLTrustStorePassword,
        SSLKeyStorePath,
        SSLKeyStorePassword,
    }

    static List<IContextParameter> getDBVariables(String prefixName, DatabaseConnection conn,
            Set<IConnParamName> paramSet) {
        if (conn == null || prefixName == null || paramSet == null || paramSet.isEmpty()) {
            return Collections.emptyList();
        }

        List<IContextParameter> varList = new ArrayList<IContextParameter>();
        String paramPrefix = prefixName + ConnectionContextHelper.LINE;
        String paramName = null;
        for (IConnParamName param : paramSet) {
            if (param instanceof EDBParamName) {
                EDBParamName dbParam = (EDBParamName) param;
                paramName = paramPrefix + dbParam;
                switch (dbParam) {
                case AdditionalParams:
                    ConnectionContextHelper.createParameters(varList, paramName, conn.getAdditionalParams());
                    break;
                case Datasource:
                    ConnectionContextHelper.createParameters(varList, paramName, conn.getDatasourceName());
                    break;
                case DBRootPath:
                case OutputFolder:
                    ConnectionContextHelper.createParameters(varList, paramName, conn.getDBRootPath(),
                            JavaTypesManager.DIRECTORY);
                    break;
                case File:
                case LogoFile:
                    ConnectionContextHelper.createParameters(varList, paramName, conn.getFileFieldName(),
                            JavaTypesManager.FILE);
                    break;
                case Password:
                    // for context, need raw password, because it's for IContextParameter with raw value in mem.
                    ConnectionContextHelper.createParameters(varList, paramName, conn.getRawPassword(),
                            JavaTypesManager.PASSWORD);
                    break;
                // hshen
                case JdbcUrl:

                    String url = conn.getURL();
                    String h2Prefix = "jdbc:h2:";
                    if (url.startsWith(h2Prefix)) {
                        String path = url.substring(h2Prefix.length(), url.length());
                        path = PathUtils.getPortablePath(path);
                        url = h2Prefix + path;
                    }

                    ConnectionContextHelper.createParameters(varList, paramName, url);
                    break;
                case DriverJar:
                    // fix for 23031
                    String path = conn.getDriverJarPath();
                    PathUtils.getOSPath(path);
                    if (path.contains("\\")) {
                        Path p = new Path(path);
                        String newPath = p.getDevice();
                        for (int i = 0; i < p.segmentCount(); i++) {
                            newPath = newPath + "\\\\" + p.segment(i);
                        }
                        path = newPath;
                    }
                    ConnectionContextHelper.createParameters(varList, paramName, path);
                    break;
                case MappingFile:
                    ConnectionContextHelper.createParameters(varList, paramName, conn.getDbmsId());
                    break;
                case ClassName:
                    ConnectionContextHelper.createParameters(varList, paramName, conn.getDriverClass());
                    break;
                case Port:
                    ConnectionContextHelper.createParameters(varList, paramName, conn.getPort(),
                            JavaTypesManager.STRING);
                    break;
                case Schema:
                    if (conn.getProductId().equals(EDatabaseTypeName.ORACLEFORSID.getProduct())) {
                        String schema = conn.getUiSchema();
                        if (schema != null) {
                            conn.setUiSchema(schema.toUpperCase());
                        }
                    }
                    ConnectionContextHelper.createParameters(varList, paramName, conn.getUiSchema());
                    break;
                case Server:
                    ConnectionContextHelper.createParameters(varList, paramName, conn.getServerName());
                    break;
                case Sid:
                case Database:
                case ServiceName:
                    ConnectionContextHelper.createParameters(varList, paramName, conn.getSID());
                    break;
                case Login:
                    ConnectionContextHelper.createParameters(varList, paramName, conn.getUsername());
                    break;
                case JobTrackerOrResourceManager:
                    String value = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_JOB_TRACKER_URL);
                    ConnectionContextHelper.createParameters(varList, paramName, value);
                    break;
                case NameNode:
                    value = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_NAME_NODE_URL);
                    ConnectionContextHelper.createParameters(varList, paramName, value);
                    break;
                case HivePrincipal:
                    value = conn.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_HIVEPRINCIPLA);
                    ConnectionContextHelper.createParameters(varList, paramName, value);
                    break;
                case HiveMetastore:
                    value = conn.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_METASTOREURL);
                    ConnectionContextHelper.createParameters(varList, paramName, value);
                    break;
                case HiveDriverJar:
                    value = conn.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_DRIVERJAR_PATH);
                    ConnectionContextHelper.createParameters(varList, paramName, value);
                    break;
                case HiveDriveClass:
                    value = conn.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_DRIVERCLASS);
                    ConnectionContextHelper.createParameters(varList, paramName, value);
                    break;
                case HiveUserName:
                    value = conn.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_USERNAME);
                    ConnectionContextHelper.createParameters(varList, paramName, value);
                    break;
                case HivePassword:
                    value = conn.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_PASSWORD);
                    ConnectionContextHelper.createParameters(varList, paramName, value, JavaTypesManager.PASSWORD);
                    break;
                case HiveKeyTabPrincipal:
                    value = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL);
                    ConnectionContextHelper.createParameters(varList, paramName, value);
                    break;
                case HiveKeyTab:
                    value = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_KEYTAB);
                    ConnectionContextHelper.createParameters(varList, paramName, value);
                    break;
                case HiveMetastorePort:
                    value = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_THRIFTPORT);
                    ConnectionContextHelper.createParameters(varList, paramName, value);
                    break;
                case hiveAdditionalJDBCParameters:
                    value = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_ADDITIONAL_JDBC_SETTINGS);
                    ConnectionContextHelper.createParameters(varList, paramName, value);
                    break;
                case hiveMetastoreUris:
                    value = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_METASTORE_URIS);
                    ConnectionContextHelper.createParameters(varList, paramName, value);
                    break;
                case hiveSSLTrustStorePath:
                    value = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PATH);
                    ConnectionContextHelper.createParameters(varList, paramName, value);
                    break;
                case hiveSSLTrustStorePassword:
                    value = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PASSWORD);
                    value = conn.getValue(value, false);
                    ConnectionContextHelper.createParameters(varList, paramName, value, JavaTypesManager.PASSWORD);
                    break;
                case TableNSMapping:
                    String key = ConnParameterKeys.CONN_PARA_KEY_HBASE_TABLE_NS_MAPPING;
                    if (EDatabaseTypeName.MAPRDB.getDisplayName().equals(conn.getDatabaseType())) {
                        key = ConnParameterKeys.CONN_PARA_KEY_MAPRDB_TABLE_NS_MAPPING;
                    }
                    value = conn.getParameters().get(key);
                    ConnectionContextHelper.createParameters(varList, paramName, value);
                    break;
                case MasterPrincipal:
                    key = ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MASTERPRINCIPAL;
                    if (EDatabaseTypeName.MAPRDB.getDisplayName().equals(conn.getDatabaseType())) {
                        key = ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MASTERPRINCIPAL;
                    }
                    value = conn.getParameters().get(key);
                    ConnectionContextHelper.createParameters(varList, paramName, value);
                    break;
                case RegionPrincipal:
                    key = ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_REGIONSERVERPRINCIPAL;
                    if (EDatabaseTypeName.MAPRDB.getDisplayName().equals(conn.getDatabaseType())) {
                        key = ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_REGIONSERVERPRINCIPAL;
                    }
                    value = conn.getParameters().get(key);
                    ConnectionContextHelper.createParameters(varList, paramName, value);
                    break;
                case Znode_Parent:
                    value = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_ZNODE_PARENT);
                    ConnectionContextHelper.createParameters(varList, paramName, value);
                    break;
                case HbaseKeyTabPrincipal:
                case MaprdbKeyTabPrincipal:
                    value = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL);
                    ConnectionContextHelper.createParameters(varList, paramName, value);
                    break;
                case HbaseKeyTab:
                case MaprdbKeyTab:
                    value = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_KEYTAB);
                    ConnectionContextHelper.createParameters(varList, paramName, value);
                    break;
                case ImpalaPrincipal:
                    value = conn.getParameters().get(ConnParameterKeys.IMPALA_AUTHENTICATION_PRINCIPLA);
                    ConnectionContextHelper.createParameters(varList, paramName, value);
                    break;
                case Username:
                    key = ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_USERNAME;
                    if (EDatabaseTypeName.HBASE.getDisplayName().equals(conn.getDatabaseType())) {
                        key = ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_USERNAME;
                    } else if (EDatabaseTypeName.MAPRDB.getDisplayName().equals(conn.getDatabaseType())) {
                        key = ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_USERNAME;
                    }
                    value = conn.getParameters().get(key);
                    ConnectionContextHelper.createParameters(varList, paramName, value);
                    break;
                case Maprticket_Password:
                    key = ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_PASSWORD;
                    if (EDatabaseTypeName.HBASE.getDisplayName().equals(conn.getDatabaseType())) {
                        key = ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_PASSWORD;
                    } else if (EDatabaseTypeName.MAPRDB.getDisplayName().equals(conn.getDatabaseType())) {
                        key = ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_PASSWORD;
                    }
                    value = conn.getParameters().get(key);
                    ConnectionContextHelper.createParameters(varList, paramName, value, JavaTypesManager.PASSWORD);
                    break;
                case Maprticket_Cluster:
                    key = ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_CLUSTER;
                    if (EDatabaseTypeName.HBASE.getDisplayName().equals(conn.getDatabaseType())) {
                        key = ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_CLUSTER;
                    } else if (EDatabaseTypeName.MAPRDB.getDisplayName().equals(conn.getDatabaseType())) {
                        key = ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_CLUSTER;
                    }
                    value = conn.getParameters().get(key);
                    ConnectionContextHelper.createParameters(varList, paramName, value);
                    break;
                case Maprticket_Duration:
                    key = ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_DURATION;
                    if (EDatabaseTypeName.HBASE.getDisplayName().equals(conn.getDatabaseType())) {
                        key = ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_DURATION;
                    } else if (EDatabaseTypeName.MAPRDB.getDisplayName().equals(conn.getDatabaseType())) {
                        key = ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_DURATION;
                    }
                    value = conn.getParameters().get(key);
                    ConnectionContextHelper.createParameters(varList, paramName, value, JavaTypesManager.LONG);
                    break;
                case SSLTrustStorePath:
                    value = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PATH);
                    ConnectionContextHelper.createParameters(varList, paramName, value);
                    break;
                case SSLTrustStorePassword:
                    value = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PASSWORD);
                    value = conn.getValue(value, false);
                    ConnectionContextHelper.createParameters(varList, paramName, value, JavaTypesManager.PASSWORD);
                    break;
                case SSLKeyStorePath:
                    value = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_KEY_STORE_PATH);
                    ConnectionContextHelper.createParameters(varList, paramName, value);
                    break;
                case SSLKeyStorePassword:
                    value = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_KEY_STORE_PASSWORD);
                    value = conn.getValue(value, false);
                    ConnectionContextHelper.createParameters(varList, paramName, value, JavaTypesManager.PASSWORD);
                    break;
                default:
                }
            }
        }
        createAdditionalContextParameters(prefixName, varList, getHiveOrHbaseHadoopProperties(conn));
        createAdditionalContextParameters(prefixName, varList, getHiveJdbcProperties(conn));
        return varList;
    }

    static List<Map<String, Object>> getHiveOrHbaseHadoopProperties(DatabaseConnection dbConn) {
        String dbType = dbConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_DB_TYPE);
        String databaseType = dbType != null ? dbType : dbConn.getDatabaseType();
        EMap<String, String> parameters = dbConn.getParameters();
        String hadoopProperties = "";
        if (EDatabaseConnTemplate.HIVE.getDBDisplayName().equals(databaseType)) {
            hadoopProperties = parameters.get(ConnParameterKeys.CONN_PARA_KEY_HIVE_PROPERTIES);
        } else if (EDatabaseConnTemplate.HBASE.getDBDisplayName().equals(databaseType)) {
            hadoopProperties = parameters.get(ConnParameterKeys.CONN_PARA_KEY_HBASE_PROPERTIES);
        } else if (EDatabaseConnTemplate.MAPRDB.getDBDisplayName().equals(databaseType)) {
            hadoopProperties = parameters.get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_PROPERTIES);
        }
        List<Map<String, Object>> hadoopPropertiesList = HadoopRepositoryUtil.getHadoopPropertiesList(hadoopProperties);
        return hadoopPropertiesList;
    }

    static List<Map<String, Object>> getHiveJdbcProperties(DatabaseConnection dbConn) {
        String dbType = dbConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_DB_TYPE);
        String databaseType = dbType != null ? dbType : dbConn.getDatabaseType();
        EMap<String, String> parameters = dbConn.getParameters();
        String hadoopProperties = "";
        if (EDatabaseConnTemplate.HIVE.getDBDisplayName().equals(databaseType)) {
            hadoopProperties = parameters.get(ConnParameterKeys.CONN_PARA_KEY_HIVE_JDBC_PROPERTIES);
        }
        List<Map<String, Object>> hiveJdbcPropertiesList =
                HadoopRepositoryUtil.getHadoopPropertiesList(hadoopProperties);
        return hiveJdbcPropertiesList;
    }

    static void createAdditionalContextParameters(String prefixName, List<IContextParameter> varList,
            List<Map<String, Object>> additionalProperteisHiveJdbcPros) {
        if (!additionalProperteisHiveJdbcPros.isEmpty()) {
            for (Map<String, Object> propertyMap : additionalProperteisHiveJdbcPros) {
                String propertyKey = (String) propertyMap.get("PROPERTY");
                String propertyValue = (String) propertyMap.get("VALUE");
                String keyWithPrefix =
                        prefixName + ConnectionContextHelper.LINE
                                + ContextParameterUtils.getValidParameterName(propertyKey);
                ConnectionContextHelper.createParameters(varList, keyWithPrefix, propertyValue);
            }
        }
    }

    static void setPropertiesForContextMode(String prefixName, DatabaseConnection conn, ContextItem contextItem,
            Set<IConnParamName> paramSet, Map<String, String> map) {
        if (conn == null || contextItem == null || prefixName == null || paramSet == null || paramSet.isEmpty()) {
            return;
        }
        String paramPrefix = prefixName + ConnectionContextHelper.LINE;
        String originalVariableName = null;
        for (IConnParamName param : paramSet) {
            if (param instanceof EDBParamName) {
                EDBParamName dbParam = (EDBParamName) param;
                originalVariableName = paramPrefix + dbParam;
                if (map != null && map.size() > 0) {
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        if (originalVariableName.equals(entry.getValue())) {
                            originalVariableName = entry.getKey();
                            break;
                        }
                    }
                }
                originalVariableName = getCorrectVariableName(contextItem, originalVariableName, dbParam);
                matchContextPropertiesForDbConnection(dbParam, conn, originalVariableName);
            }
        }
        String hadoopJsonProperties =
                transformAdditionalPropertiesWithContextMode(prefixName, getHiveOrHbaseHadoopProperties(conn));
        setAdditionalHadoopProperties(conn, hadoopJsonProperties);

        String hiveJsonProperties =
                transformAdditionalPropertiesWithContextMode(prefixName, getHiveJdbcProperties(conn));
        setAdditionalHiveJdbcProperties(conn, hiveJsonProperties);
    }

    private static String transformAdditionalPropertiesWithContextMode(String prefixName,
            List<Map<String, Object>> additionalProperties) {
        String finalContextPropertiesJson = "";
        if (!additionalProperties.isEmpty()) {
            for (Map<String, Object> propertyMap : additionalProperties) {
                String propertyKey = (String) propertyMap.get("PROPERTY");
                propertyMap.put(
                        "VALUE",
                        ContextParameterUtils.getNewScriptCode(prefixName + ConnectionContextHelper.LINE
                                + ContextParameterUtils.getValidParameterName(propertyKey), LANGUAGE));
            }
            finalContextPropertiesJson = HadoopRepositoryUtil.getHadoopPropertiesJsonStr(additionalProperties);
        }
        return finalContextPropertiesJson;
    }

    static void setPropertiesForExistContextMode(DatabaseConnection conn, Set<IConnParamName> paramSet,
            Map<ContextItem, List<ConectionAdaptContextVariableModel>> map) {
        String originalVariableName = null;
        for (IConnParamName param : paramSet) {
            if (param instanceof EDBParamName) {
                originalVariableName = "";
                EDBParamName dbParam = (EDBParamName) param;
                if (map != null && map.size() > 0) {
                    for (Map.Entry<ContextItem, List<ConectionAdaptContextVariableModel>> entry : map.entrySet()) {
                        List<ConectionAdaptContextVariableModel> modelList = entry.getValue();
                        for (ConectionAdaptContextVariableModel model : modelList) {
                            if (model.getValue().equals(dbParam.name())) {
                                originalVariableName = model.getName();
                                break;
                            }
                        }
                    }
                }
                matchContextPropertiesForDbConnection(dbParam, conn, originalVariableName);
            }
        }
        if (map != null && !map.isEmpty()) {
            String hadoopPropertiesJson =
                    transformAdditionalPropertiesWithExistContext(getHiveOrHbaseHadoopProperties(conn), map);
            setAdditionalHadoopProperties(conn, hadoopPropertiesJson);

            String hiveJdbcdPropertiesJson =
                    transformAdditionalPropertiesWithExistContext(getHiveJdbcProperties(conn), map);
            setAdditionalHiveJdbcProperties(conn, hiveJdbcdPropertiesJson);
        }
    }

    private static String transformAdditionalPropertiesWithExistContext(List<Map<String, Object>> additionalProperties,
            Map<ContextItem, List<ConectionAdaptContextVariableModel>> map) {
        String finalContextPropertiesJson = "";
        if (!additionalProperties.isEmpty()) {
            Set<String> keys = getConAdditionProperties(additionalProperties);
            for (Map.Entry<ContextItem, List<ConectionAdaptContextVariableModel>> entry : map.entrySet()) {
                List<ConectionAdaptContextVariableModel> modelList = entry.getValue();
                for (ConectionAdaptContextVariableModel model : modelList) {
                    String propertyKey = model.getValue();
                    if (keys.contains(propertyKey)) {
                        // the reuse context match model find the property key need to do context
                        for (Map<String, Object> propertyMap : additionalProperties) {
                            String hadoopPropertyKey = (String) propertyMap.get("PROPERTY");
                            if (propertyKey.equals(hadoopPropertyKey)) {
                                propertyMap.put("VALUE",
                                        ContextParameterUtils.getNewScriptCode(model.getName(), LANGUAGE));
                            }
                        }
                    }
                }
            }
            finalContextPropertiesJson = HadoopRepositoryUtil.getHadoopPropertiesJsonStr(additionalProperties);
        }
        return finalContextPropertiesJson;
    }

    private static Set<String> getConAdditionProperties(List<Map<String, Object>> propertiesList) {
        Set<String> varList = new HashSet<String>();
        if (!propertiesList.isEmpty()) {
            for (Map<String, Object> propertyMap : propertiesList) {
                varList.add((String) propertyMap.get("PROPERTY"));
            }
        }
        return varList;
    }

    private static void setAdditionalHadoopProperties(DatabaseConnection conn, String hadoopPropertiesJson) {
        if (EDatabaseConnTemplate.HIVE.getDBDisplayName().equals(conn.getDatabaseType())) {
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_PROPERTIES, hadoopPropertiesJson);
        } else if (EDatabaseConnTemplate.HBASE.getDBDisplayName().equals(conn.getDatabaseType())) {
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_PROPERTIES, hadoopPropertiesJson);
        } else if (EDatabaseConnTemplate.MAPRDB.getDBDisplayName().equals(conn.getDatabaseType())) {
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_PROPERTIES, hadoopPropertiesJson);
        }
    }

    private static void setAdditionalHiveJdbcProperties(DatabaseConnection conn, String hivePropertiesJson) {
        if (EDatabaseConnTemplate.HIVE.getDBDisplayName().equals(conn.getDatabaseType())
                && StringUtils.isNotEmpty(hivePropertiesJson)) {
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_JDBC_PROPERTIES, hivePropertiesJson);
        }
    }

    static void matchContextPropertiesForDbConnection(EDBParamName dbParam, DatabaseConnection conn,
            String originalVariableName) {
        switch (dbParam) {
        case AdditionalParams:
            conn.setAdditionalParams(ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case Datasource:
            conn.setDatasourceName(ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case DBRootPath:
        case OutputFolder:
            conn.setDBRootPath(ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case File:
        case LogoFile:
            conn.setFileFieldName(ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case Password:
            conn.setPassword(ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        // hshen
        case JdbcUrl:
            conn.setURL(ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case DriverJar:
            conn.setDriverJarPath(ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case MappingFile:
            conn.setDbmsId(ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case ClassName:
            conn.setDriverClass(ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case Port:
            conn.setPort(ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case Schema:
            conn.setUiSchema(ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case Server:
            conn.setServerName(ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case Sid:
        case Database:
        case ServiceName:
            conn.setSID(ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;

        case Login:
            conn.setUsername(ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_USERNAME,
                    ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case JobTrackerOrResourceManager:
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_JOB_TRACKER_URL,
                    ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case NameNode:
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_NAME_NODE_URL,
                    ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case HivePrincipal:
            conn.getParameters().put(ConnParameterKeys.HIVE_AUTHENTICATION_HIVEPRINCIPLA,
                    ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case HiveMetastore:
            conn.getParameters().put(ConnParameterKeys.HIVE_AUTHENTICATION_METASTOREURL,
                    ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case HiveDriverJar:
            conn.getParameters().put(ConnParameterKeys.HIVE_AUTHENTICATION_DRIVERJAR_PATH,
                    ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case HiveDriveClass:
            conn.getParameters().put(ConnParameterKeys.HIVE_AUTHENTICATION_DRIVERCLASS,
                    ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case HiveUserName:
            conn.getParameters().put(ConnParameterKeys.HIVE_AUTHENTICATION_USERNAME,
                    ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case HivePassword:
            conn.getParameters().put(ConnParameterKeys.HIVE_AUTHENTICATION_PASSWORD,
                    ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case HiveKeyTabPrincipal:
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL,
                    ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case HiveKeyTab:
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_KEYTAB,
                    ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case HiveMetastorePort:
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_THRIFTPORT,
                    ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case hiveAdditionalJDBCParameters:
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_ADDITIONAL_JDBC_SETTINGS,
                    ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case hiveMetastoreUris:
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_METASTORE_URIS,
                    ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case hiveSSLTrustStorePath:
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PATH,
                    ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case hiveSSLTrustStorePassword:
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PASSWORD,
                    ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case TableNSMapping:
            String key = ConnParameterKeys.CONN_PARA_KEY_HBASE_TABLE_NS_MAPPING;
            if (EDatabaseTypeName.MAPRDB.getDisplayName().equals(conn.getDatabaseType())) {
                key = ConnParameterKeys.CONN_PARA_KEY_MAPRDB_TABLE_NS_MAPPING;
            }
            conn.getParameters().put(key, ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case MasterPrincipal:
            key = ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MASTERPRINCIPAL;
            if (EDatabaseTypeName.MAPRDB.getDisplayName().equals(conn.getDatabaseType())) {
                key = ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MASTERPRINCIPAL;
            }
            conn.getParameters().put(key, ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case RegionPrincipal:
            key = ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_REGIONSERVERPRINCIPAL;
            if (EDatabaseTypeName.MAPRDB.getDisplayName().equals(conn.getDatabaseType())) {
                key = ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_REGIONSERVERPRINCIPAL;
            }
            conn.getParameters().put(key, ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case Znode_Parent:
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_ZNODE_PARENT,
                    ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case HbaseKeyTabPrincipal:
        case MaprdbKeyTabPrincipal:
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL,
                    ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case HbaseKeyTab:
        case MaprdbKeyTab:
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_KEYTAB,
                    ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case Username:
            key = ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_USERNAME;
            if (EDatabaseTypeName.HBASE.getDisplayName().equals(conn.getDatabaseType())) {
                key = ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_USERNAME;
            } else if (EDatabaseTypeName.MAPRDB.getDisplayName().equals(conn.getDatabaseType())) {
                key = ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_USERNAME;
            }
            conn.getParameters().put(key, ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case Maprticket_Password:
            key = ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_PASSWORD;
            if (EDatabaseTypeName.HBASE.getDisplayName().equals(conn.getDatabaseType())) {
                key = ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_PASSWORD;
            } else if (EDatabaseTypeName.MAPRDB.getDisplayName().equals(conn.getDatabaseType())) {
                key = ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_PASSWORD;
            }
            conn.getParameters().put(key, ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case Maprticket_Cluster:
            key = ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_CLUSTER;
            if (EDatabaseTypeName.HBASE.getDisplayName().equals(conn.getDatabaseType())) {
                key = ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_CLUSTER;
            } else if (EDatabaseTypeName.MAPRDB.getDisplayName().equals(conn.getDatabaseType())) {
                key = ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_CLUSTER;
            }
            conn.getParameters().put(key, ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case Maprticket_Duration:
            key = ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_DURATION;
            if (EDatabaseTypeName.HBASE.getDisplayName().equals(conn.getDatabaseType())) {
                key = ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_DURATION;
            } else if (EDatabaseTypeName.MAPRDB.getDisplayName().equals(conn.getDatabaseType())) {
                key = ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_DURATION;
            }
            conn.getParameters().put(key, ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case ImpalaPrincipal:
            conn.getParameters().put(ConnParameterKeys.IMPALA_AUTHENTICATION_PRINCIPLA,
                    ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case SSLTrustStorePath:
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PATH,
                    ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case SSLTrustStorePassword:
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PASSWORD,
                    ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case SSLKeyStorePath:
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_SSL_KEY_STORE_PATH,
                    ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        case SSLKeyStorePassword:
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_SSL_KEY_STORE_PASSWORD,
                    ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
            break;
        default:
        }
    }

    private static String getCorrectVariableName(ContextItem contextItem, String originalVariableName,
            EDBParamName dbParam) {
        Set<String> contextVarNames = ContextUtils.getContextVarNames(contextItem);
        // if not contains it ,will get originalVariableName from the context
        if (contextVarNames != null && !contextVarNames.contains(originalVariableName)) {
            for (String varName : contextVarNames) {
                if (varName.endsWith(dbParam.name())) {
                    return varName;
                }
            }
        }
        return originalVariableName;
    }

    /**
     * 
     * ggu Comment method "setManagerConnectionValues".
     * 
     * set the ManagerConnection parameter and return the url string connection.
     */
    public static String setManagerConnectionValues(ManagerConnection managerConnection, ConnectionItem connectionItem,
            ContextType contextType, final String dbType) {
        if (managerConnection == null || connectionItem == null || dbType == null || dbType.length() < 0) {
            return null;
        }
        DatabaseConnection dbConn = (DatabaseConnection) connectionItem.getConnection();

        if (contextType == null) {
            contextType = ConnectionContextHelper.getContextTypeForContextMode(dbConn);
        }

        String server = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getServerName());
        String username = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getUsername());
        String password = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getRawPassword());
        String port = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getPort());
        String sidOrDatabase = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getSID());
        String datasource = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getDatasourceName());
        String filePath = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getFileFieldName());
        String schemaOracle = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getUiSchema());
        String dbRootPath = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getDBRootPath());
        String additionParam = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getAdditionalParams());
        String driverClassName = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getDriverClass());
        String driverJarPath = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getDriverJarPath());
        String dbVersionString = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getDbVersionString());

        filePath = TalendQuoteUtils.removeQuotes(filePath);
        dbRootPath = TalendQuoteUtils.removeQuotes(dbRootPath);
        // url

        String urlConnection;
        if (dbConn.getDatabaseType().equals(EDatabaseConnTemplate.GENERAL_JDBC.getDBDisplayName())) {
            urlConnection = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getURL());
        } else {
            urlConnection =
                    DatabaseConnStrUtil.getURLString(dbConn.getDatabaseType(), dbConn.getDbVersionString(), server,
                            username, password, port, sidOrDatabase, filePath.toLowerCase(), datasource, dbRootPath,
                            additionParam);
        }

        if (dbConn.getProductId().equals(EDatabaseTypeName.ORACLEFORSID.getProduct())) {
            schemaOracle = schemaOracle.toUpperCase();
        }
        if (EDatabaseTypeName.ORACLE_CUSTOM.getDisplayName().equals(dbConn.getDatabaseType())) {
            additionParam = ConvertionHelper.convertAdditionalParameters(dbConn);
        }
        // set the value
        managerConnection.setValue(0, dbType, urlConnection, server, username, password, sidOrDatabase, port, filePath,
                datasource, schemaOracle, additionParam, driverClassName, driverJarPath, dbVersionString);
        managerConnection.setDbRootPath(dbRootPath);

        return urlConnection;
    }

    public static String setManagerConnectionValues(ManagerConnection managerConnection, ConnectionItem connectionItem,
            final String dbType) {
        return setManagerConnectionValues(managerConnection, connectionItem, null, dbType);
    }

    /**
     * 
     * ggu Comment method "getUrlConnectionString".
     * 
     * if display is false, the string connection will be returned by default context.
     */
    public static String getUrlConnectionString(ConnectionItem connectionItem, boolean defaultContext) {
        if (connectionItem == null) {
            return null;
        }
        DatabaseConnection dbConn = (DatabaseConnection) connectionItem.getConnection();
        ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(dbConn, defaultContext);

        String server = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getServerName());
        String username = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getUsername());
        String password = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getRawPassword());
        String port = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getPort());
        String sidOrDatabase = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getSID());
        String datasource = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getDatasourceName());
        String filePath = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getFileFieldName());
        String schemaOracle = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getUiSchema());
        String dbRootPath = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getDBRootPath());
        String additionParam = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getAdditionalParams());
        // hshen
        String jdbcUrl = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getURL());
        String driverJar = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getDriverJarPath());
        String className = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getDbmsId());
        String mappingFile = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getFileFieldName());

        filePath = TalendQuoteUtils.removeQuotes(filePath);
        dbRootPath = TalendQuoteUtils.removeQuotes(dbRootPath);

        // for hive :
        if (EDatabaseTypeName.HIVE.equals(EDatabaseTypeName.getTypeFromDbType(dbConn.getDatabaseType()))) {
            String template = null;
            EMap<String, String> parameters = dbConn.getParameters();
            String contextedHivePrinciple = null;
            String nonContextedHivePrinciple = null;
            if (parameters != null) {
                contextedHivePrinciple = parameters.get(ConnParameterKeys.HIVE_AUTHENTICATION_HIVEPRINCIPLA);
                if (contextedHivePrinciple != null && !contextedHivePrinciple.isEmpty()) {
                    nonContextedHivePrinciple =
                            ConnectionContextHelper.getOriginalValue(contextType, contextedHivePrinciple);
                    if (nonContextedHivePrinciple != null && !nonContextedHivePrinciple.isEmpty()) {
                        parameters.put(ConnParameterKeys.HIVE_AUTHENTICATION_HIVEPRINCIPLA, nonContextedHivePrinciple);
                    }
                }

            }
            if (dbConn.getURL() != null && dbConn.getURL().startsWith(DbConnStrForHive.URL_HIVE_2_TEMPLATE)) {
                template = DbConnStrForHive.URL_HIVE_2_TEMPLATE;
            } else {
                template = DbConnStrForHive.URL_HIVE_1_TEMPLATE;
            }
            String urlString = DatabaseConnStrUtil.getHiveURLString(dbConn, server, port, sidOrDatabase, template);

            if (parameters != null && nonContextedHivePrinciple != null && !nonContextedHivePrinciple.isEmpty()) {
                parameters.put(ConnParameterKeys.HIVE_AUTHENTICATION_HIVEPRINCIPLA, contextedHivePrinciple);
            }

            return urlString;
        }

        String newUrl =
                DatabaseConnStrUtil.getURLString(dbConn.getDatabaseType(), dbConn.getDbVersionString(), server,
                        username, password, port, sidOrDatabase, filePath, datasource, dbRootPath, additionParam,
                        jdbcUrl, driverJar, className, mappingFile);
        return newUrl;

    }

    /**
     * 
     * ggu Comment method "cloneOriginalValueConnection".
     * 
     * perhaps, if connection is in context mode, will open dialog to choose context sets.
     */
    public static DatabaseConnection cloneOriginalValueConnection(DatabaseConnection dbConn) {
        return cloneOriginalValueConnection(dbConn, false, null);
    }

    /**
     * 
     * ggu Comment method "cloneOriginalValueConnection".
     * 
     * only clone the properties of connection.
     * 
     * @param selectedContext
     */
    public static DatabaseConnection cloneOriginalValueConnection(DatabaseConnection dbConn, boolean defaultContext,
            String selectedContext) {
        if (dbConn == null) {
            return null;
        }
        ContextType contextType =
                ConnectionContextHelper.getContextTypeForContextMode(null, dbConn, selectedContext, defaultContext);
        DatabaseConnection cloneConn = ConnectionFactory.eINSTANCE.createDatabaseConnection();
        // get values
        String server = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getServerName());
        String username = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getUsername());
        String password = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getRawPassword());
        String port = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getPort());
        String sidOrDatabase = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getSID());
        String datasource = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getDatasourceName());
        String filePath = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getFileFieldName());
        String schemaOracle = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getUiSchema());
        String dbRootPath = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getDBRootPath());
        String additionParam = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getAdditionalParams());
        String url = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getURL());
        String className = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getDriverClass());
        String jarPath = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getDriverJarPath());

        // hyWang add for bug 0007252
        String dbmsID = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getDbmsId());

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

        ContextType hadoopClusterContextType = getHadoopClusterContextType(dbConn, defaultContext);

        // for hive :
        if (EDatabaseTypeName.HIVE.equals(EDatabaseTypeName.getTypeFromDbType(dbConn.getDatabaseType()))) {
            String hadoopUserName = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_USERNAME);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_USERNAME,
                    getOriginalValue(hadoopClusterContextType, contextType, hadoopUserName));
            String jobTracker = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_JOB_TRACKER_URL);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_JOB_TRACKER_URL,
                    getOriginalValue(hadoopClusterContextType, contextType, jobTracker));
            String nameNode = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_NAME_NODE_URL);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_NAME_NODE_URL,
                    getOriginalValue(hadoopClusterContextType, contextType, nameNode));
            String hivePrincipal = cloneConn.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_HIVEPRINCIPLA);
            cloneConn.getParameters().put(ConnParameterKeys.HIVE_AUTHENTICATION_HIVEPRINCIPLA,
                    getOriginalValue(hadoopClusterContextType, contextType, hivePrincipal));

            String hiveMetadata = cloneConn.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_METASTOREURL);
            cloneConn.getParameters().put(ConnParameterKeys.HIVE_AUTHENTICATION_METASTOREURL,
                    getOriginalValue(hadoopClusterContextType, contextType, hiveMetadata));

            String driverPath = cloneConn.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_DRIVERJAR_PATH);
            cloneConn.getParameters().put(ConnParameterKeys.HIVE_AUTHENTICATION_DRIVERJAR_PATH,
                    getOriginalValue(hadoopClusterContextType, contextType, driverPath));

            String driverClass = cloneConn.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_DRIVERCLASS);
            cloneConn.getParameters().put(ConnParameterKeys.HIVE_AUTHENTICATION_DRIVERCLASS,
                    getOriginalValue(hadoopClusterContextType, contextType, driverClass));

            String hiveUserName = cloneConn.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_USERNAME);
            cloneConn.getParameters().put(ConnParameterKeys.HIVE_AUTHENTICATION_USERNAME,
                    getOriginalValue(hadoopClusterContextType, contextType, hiveUserName));

            String hivePassword = cloneConn.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_PASSWORD);
            cloneConn.getParameters().put(ConnParameterKeys.HIVE_AUTHENTICATION_PASSWORD,
                    getOriginalValue(hadoopClusterContextType, contextType, hivePassword));

            String ktPrincipal = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL,
                    getOriginalValue(hadoopClusterContextType, contextType, ktPrincipal));

            String keytab = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_KEYTAB);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_KEYTAB,
                    getOriginalValue(hadoopClusterContextType, contextType, keytab));

            String maprticket_Username =
                    cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_USERNAME);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_USERNAME,
                    getOriginalValue(hadoopClusterContextType, contextType, maprticket_Username));

            String maprticket_Password =
                    cloneConn.getParameters().get(
                            ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_PASSWORD);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_PASSWORD,
                    getOriginalValue(hadoopClusterContextType, contextType, maprticket_Password));

            String maprticket_Cluster =
                    cloneConn.getParameters().get(
                            ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_CLUSTER);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_CLUSTER,
                    getOriginalValue(hadoopClusterContextType, contextType, maprticket_Cluster));

            String maprticket_Duration =
                    cloneConn.getParameters().get(
                            ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_DURATION);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_DURATION,
                    getOriginalValue(hadoopClusterContextType, contextType, maprticket_Duration));

            String jdbcPropertiesString =
                    cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_JDBC_PROPERTIES);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_JDBC_PROPERTIES,
                    HadoopRepositoryUtil.getOriginalValueOfProperties(jdbcPropertiesString, contextType));

            String hiveMetastorePort = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_THRIFTPORT);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_THRIFTPORT,
                    getOriginalValue(hadoopClusterContextType, contextType, hiveMetastorePort));

            String additionalJDBCSettings =
                    cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_ADDITIONAL_JDBC_SETTINGS);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_ADDITIONAL_JDBC_SETTINGS,
                    getOriginalValue(hadoopClusterContextType, contextType, additionalJDBCSettings));

            String hiveEnableHa = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_ENABLE_HA);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_ENABLE_HA,
                    getOriginalValue(hadoopClusterContextType, contextType, hiveEnableHa));

            String hiveMetastoreUris = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_METASTORE_URIS);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_METASTORE_URIS,
                    getOriginalValue(hadoopClusterContextType, contextType, hiveMetastoreUris));

            String propertiesString = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_PROPERTIES);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_PROPERTIES,
                    HadoopRepositoryUtil.getOriginalValueOfProperties(propertiesString, contextType));

            String trustStorePath = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PATH);
            if (trustStorePath != null) {
                cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PATH,
                        getOriginalValue(hadoopClusterContextType, contextType, trustStorePath));
            }

            String trustStorePassword =
                    cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PASSWORD);
            if (trustStorePassword != null) {
                cloneConn.getParameters().put(
                        ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PASSWORD,
                        cloneConn.getValue(cloneConn.getValue(
                                getOriginalValue(hadoopClusterContextType, contextType, trustStorePassword), false),
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
            String hbaseMasterPrin =
                    cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MASTERPRINCIPAL);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MASTERPRINCIPAL,
                    getOriginalValue(hadoopClusterContextType, contextType, hbaseMasterPrin));

            String hbaseRegionPrin =
                    cloneConn.getParameters().get(
                            ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_REGIONSERVERPRINCIPAL);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_REGIONSERVERPRINCIPAL,
                    getOriginalValue(hadoopClusterContextType, contextType, hbaseRegionPrin));

            String hbaseKeyTabPrin = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL,
                    getOriginalValue(hadoopClusterContextType, contextType, hbaseKeyTabPrin));

            String hbaseKeyTab = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_KEYTAB);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_KEYTAB,
                    getOriginalValue(hadoopClusterContextType, contextType, hbaseKeyTab));

            String maprticket_Username =
                    cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_USERNAME);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_USERNAME,
                    getOriginalValue(hadoopClusterContextType, contextType, maprticket_Username));

            String maprticket_Password =
                    cloneConn.getParameters().get(
                            ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_PASSWORD);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_PASSWORD,
                    getOriginalValue(hadoopClusterContextType, contextType, maprticket_Password));

            String maprticket_Cluster =
                    cloneConn.getParameters().get(
                            ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_CLUSTER);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_CLUSTER,
                    getOriginalValue(hadoopClusterContextType, contextType, maprticket_Cluster));

            String maprticket_Duration =
                    cloneConn.getParameters().get(
                            ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_DURATION);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_DURATION,
                    getOriginalValue(hadoopClusterContextType, contextType, maprticket_Duration));

            String tableNSMapping =
                    cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_TABLE_NS_MAPPING);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_TABLE_NS_MAPPING,
                    getOriginalValue(hadoopClusterContextType, contextType, tableNSMapping));
        }

        // for Maprdb
        if (EDatabaseTypeName.MAPRDB.equals(EDatabaseTypeName.getTypeFromDbType(cloneConn.getDatabaseType()))) {
            String maprdbMasterPrin =
                    cloneConn
                            .getParameters()
                            .get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MASTERPRINCIPAL);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MASTERPRINCIPAL,
                    getOriginalValue(hadoopClusterContextType, contextType, maprdbMasterPrin));

            String maprdbRegionPrin =
                    cloneConn.getParameters().get(
                            ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_REGIONSERVERPRINCIPAL);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_REGIONSERVERPRINCIPAL,
                    getOriginalValue(hadoopClusterContextType, contextType, maprdbRegionPrin));

            String znodeParent = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_ZNODE_PARENT);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_ZNODE_PARENT,
                    getOriginalValue(hadoopClusterContextType, contextType, znodeParent));

            String maprdbKeyTabPrin = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL,
                    getOriginalValue(hadoopClusterContextType, contextType, maprdbKeyTabPrin));

            String maprdbKeyTab = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_KEYTAB);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_KEYTAB,
                    getOriginalValue(hadoopClusterContextType, contextType, maprdbKeyTab));

            String maprticket_Username =
                    cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_USERNAME);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_USERNAME,
                    getOriginalValue(hadoopClusterContextType, contextType, maprticket_Username));

            String maprticket_Password =
                    cloneConn.getParameters().get(
                            ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_PASSWORD);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_PASSWORD,
                    getOriginalValue(hadoopClusterContextType, contextType, maprticket_Password));

            String maprticket_Cluster =
                    cloneConn.getParameters().get(
                            ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_CLUSTER);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_CLUSTER,
                    getOriginalValue(hadoopClusterContextType, contextType, maprticket_Cluster));

            String maprticket_Duration =
                    cloneConn.getParameters().get(
                            ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_DURATION);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_DURATION,
                    getOriginalValue(hadoopClusterContextType, contextType, maprticket_Duration));

            String tableNSMapping =
                    cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_TABLE_NS_MAPPING);
            cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_TABLE_NS_MAPPING,
                    getOriginalValue(hadoopClusterContextType, contextType, tableNSMapping));
        }

        // TDI-28124:tdb2input can't guess schema from join sql on system table
        if (EDatabaseTypeName.IBMDB2.equals(EDatabaseTypeName.getTypeFromDbType(dbConn.getDatabaseType()))) {
            String cursorForDb2 = ":cursorSensitivity=2;";
            String database = sidOrDatabase + cursorForDb2;
            String newURL =
                    DatabaseConnStrUtil.getURLString(cloneConn.getDatabaseType(), dbConn.getDbVersionString(), server,
                            username, password, port, database, filePath.toLowerCase(), datasource, dbRootPath,
                            additionParam);
            cloneConn.setURL(newURL);
            return cloneConn;
        }

        if (EDatabaseTypeName.IMPALA.equals(EDatabaseTypeName.getTypeFromDbType(dbConn.getDatabaseType()))) {
            String impalaAuthPrinciple =
                    cloneConn.getParameters().get(ConnParameterKeys.IMPALA_AUTHENTICATION_PRINCIPLA);
            if (impalaAuthPrinciple != null) {
                cloneConn.getParameters().put(ConnParameterKeys.IMPALA_AUTHENTICATION_PRINCIPLA,
                        ContextParameterUtils.getOriginalValue(contextType, impalaAuthPrinciple));
            }
        }

        if (EDatabaseTypeName.ORACLE_CUSTOM.equals(EDatabaseTypeName.getTypeFromDbType(dbConn.getDatabaseType()))) {
            String trustStorePath = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PATH);
            if (trustStorePath != null) {
                cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PATH,
                        getOriginalValue(hadoopClusterContextType, contextType, trustStorePath));
            }

            String trustStorePassword =
                    cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PASSWORD);
            if (trustStorePassword != null) {
                cloneConn.getParameters().put(
                        ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PASSWORD,
                        cloneConn.getValue(cloneConn.getValue(
                                getOriginalValue(hadoopClusterContextType, contextType, trustStorePassword), false),
                                true));
            }

            String keyStorePath = cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_KEY_STORE_PATH);
            if (keyStorePath != null) {
                cloneConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_SSL_KEY_STORE_PATH,
                        getOriginalValue(hadoopClusterContextType, contextType, keyStorePath));
            }

            String keyStorePassword =
                    cloneConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_KEY_STORE_PASSWORD);
            if (keyStorePassword != null) {
                cloneConn
                        .getParameters()
                        .put(ConnParameterKeys.CONN_PARA_KEY_SSL_KEY_STORE_PASSWORD,
                                cloneConn.getValue(cloneConn.getValue(
                                        getOriginalValue(hadoopClusterContextType, contextType, keyStorePassword),
                                        false), true));
            }
        }

        // Added 20130311 TDQ-7000, when it is context mode and not general jdbc, reset the url.
        if (contextType != null
                && !EDatabaseTypeName.GENERAL_JDBC
                        .equals(EDatabaseTypeName.getTypeFromDbType(dbConn.getDatabaseType()))) {
            String newURL = null;
            if (EDatabaseTypeName.IMPALA.equals(EDatabaseTypeName.getTypeFromDbType(dbConn.getDatabaseType()))) {
                newURL =
                        DatabaseConnStrUtil.getImpalaString(cloneConn, cloneConn.getServerName(), cloneConn.getPort(),
                                cloneConn.getSID(), DbConnStrForHive.URL_HIVE_2_TEMPLATE);
            } else {
                newURL =
                        DatabaseConnStrUtil.getURLString(cloneConn.getDatabaseType(), dbConn.getDbVersionString(),
                                server, username, password, port, sidOrDatabase, filePath.toLowerCase(), datasource,
                                dbRootPath, additionParam);
            }
            cloneConn.setURL(newURL);
            return cloneConn;
        }// ~

        if (dbConn.getURL() != null && !dbConn.getURL().equals("") && EDatabaseTypeName.GENERAL_JDBC
                .equals(EDatabaseTypeName.getTypeFromDbType(dbConn.getDatabaseType()))) { //$NON-NLS-1$
            cloneConn.setURL(url);
            return cloneConn;
        }
        String newURL =
                DatabaseConnStrUtil.getURLString(cloneConn.getDatabaseType(), dbConn.getDbVersionString(), server,
                        username, password, port, sidOrDatabase, filePath.toLowerCase(), datasource, dbRootPath,
                        additionParam);
        cloneConn.setURL(newURL);
        return cloneConn;
    }

    private static String getOriginalValue(ContextType parentContextType, ContextType contextType, final String value) {
        ContextType ct = contextType;
        boolean isContextParam = ContextParameterUtils.isContextParamOfContextType(ct, value);
        if (!isContextParam && parentContextType != null) {
            ct = parentContextType;
        }
        return ContextParameterUtils.getOriginalValue(ct, value);
    }

    private static ContextType getHadoopClusterContextType(DatabaseConnection dbConn, boolean useDefaultContext) {
        ContextType hadoopClusterContextType = null;
        IHadoopClusterService hadoopClusterService = HadoopRepositoryUtil.getHadoopClusterService();
        if (hadoopClusterService != null) {
            EMap<String, String> parameters = dbConn.getParameters();
            if (parameters != null && parameters.size() > 0) {
                ConnectionItem hadoopClusterItem =
                        (ConnectionItem) hadoopClusterService.getHadoopClusterItemById(parameters
                                .get(ConnParameterKeys.CONN_PARA_KEY_HADOOP_CLUSTER_ID));
                if (hadoopClusterItem != null) {
                    Connection hadoopClusterConnection = hadoopClusterItem.getConnection();
                    hadoopClusterContextType =
                            ConnectionContextHelper.getContextTypeForContextMode(null, hadoopClusterConnection,
                                    hadoopClusterConnection.getContextName(), useDefaultContext);
                }
            }
        }
        return hadoopClusterContextType;
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

    static void revertPropertiesForContextMode(DatabaseConnection conn, ContextType contextType) {
        if (conn == null || contextType == null) {
            return;
        }
        String server = ContextParameterUtils.getOriginalValue(contextType, conn.getServerName());
        String username = ContextParameterUtils.getOriginalValue(contextType, conn.getUsername());
        String password = ContextParameterUtils.getOriginalValue(contextType, conn.getRawPassword());
        String port = ContextParameterUtils.getOriginalValue(contextType, conn.getPort());
        String sidOrDatabase = ContextParameterUtils.getOriginalValue(contextType, conn.getSID());
        String datasource = ContextParameterUtils.getOriginalValue(contextType, conn.getDatasourceName());
        String filePath = ContextParameterUtils.getOriginalValue(contextType, conn.getFileFieldName());
        String schemaOracle = ContextParameterUtils.getOriginalValue(contextType, conn.getUiSchema());
        String dbRootPath = ContextParameterUtils.getOriginalValue(contextType, conn.getDBRootPath());
        String additionParam = ContextParameterUtils.getOriginalValue(contextType, conn.getAdditionalParams());
        // hshen
        String jdbcUrl = ContextParameterUtils.getOriginalValue(contextType, conn.getURL());
        String driverJar = ContextParameterUtils.getOriginalValue(contextType, conn.getDriverJarPath());
        String className = ContextParameterUtils.getOriginalValue(contextType, conn.getDriverClass());
        String mappingFile = ContextParameterUtils.getOriginalValue(contextType, conn.getDbmsId());

        filePath = TalendQuoteUtils.removeQuotes(filePath);
        dbRootPath = TalendQuoteUtils.removeQuotes(dbRootPath);
        conn.setAdditionalParams(additionParam);
        conn.setDatasourceName(datasource);
        conn.setDBRootPath(dbRootPath);
        conn.setFileFieldName(filePath);
        conn.setRawPassword(password);
        conn.setPort(port);
        conn.setUiSchema(schemaOracle);
        conn.setServerName(server);

        // fix for 19315 , catalog name is exported to context model in some previous version
        if (conn.getDataPackage() != null) {
            for (orgomg.cwm.objectmodel.core.Package dataPackage : conn.getDataPackage()) {
                if (dataPackage instanceof ModelElement) {
                    ModelElement element = dataPackage;
                    if (conn.getSID() != null && conn.getSID().equals(element.getName())) {
                        element.setName(sidOrDatabase);
                    }
                }
            }
        }

        conn.setSID(sidOrDatabase);
        conn.setUsername(username);
        // hshen
        conn.setURL(jdbcUrl);
        conn.setDriverJarPath(driverJar);
        conn.setDriverClass(className);
        conn.setDbmsId(mappingFile);
        revertConnSubElement(conn, contextType);

        // for hive
        if (EDatabaseTypeName.HIVE.equals(EDatabaseTypeName.getTypeFromDbType(conn.getDatabaseType()))) {
            String hadoopUserName = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_USERNAME);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_USERNAME,
                    ContextParameterUtils.getOriginalValue(contextType, hadoopUserName));
            String jobTracker = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_JOB_TRACKER_URL);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_JOB_TRACKER_URL,
                    ContextParameterUtils.getOriginalValue(contextType, jobTracker));
            String nameNode = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_NAME_NODE_URL);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_NAME_NODE_URL,
                    ContextParameterUtils.getOriginalValue(contextType, nameNode));
            String hivePrincipal = conn.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_HIVEPRINCIPLA);
            conn.getParameters().put(ConnParameterKeys.HIVE_AUTHENTICATION_HIVEPRINCIPLA,
                    ContextParameterUtils.getOriginalValue(contextType, hivePrincipal));

            String hiveMetadata = conn.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_METASTOREURL);
            conn.getParameters().put(ConnParameterKeys.HIVE_AUTHENTICATION_METASTOREURL,
                    ContextParameterUtils.getOriginalValue(contextType, hiveMetadata));

            String driverPath = conn.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_DRIVERJAR_PATH);
            conn.getParameters().put(ConnParameterKeys.HIVE_AUTHENTICATION_DRIVERJAR_PATH,
                    ContextParameterUtils.getOriginalValue(contextType, driverPath));

            String driverClass = conn.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_DRIVERCLASS);
            conn.getParameters().put(ConnParameterKeys.HIVE_AUTHENTICATION_DRIVERCLASS,
                    ContextParameterUtils.getOriginalValue(contextType, driverClass));

            String hiveUserName = conn.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_USERNAME);
            conn.getParameters().put(ConnParameterKeys.HIVE_AUTHENTICATION_USERNAME,
                    ContextParameterUtils.getOriginalValue(contextType, hiveUserName));

            String hivePassword = conn.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_PASSWORD);
            conn.getParameters().put(ConnParameterKeys.HIVE_AUTHENTICATION_PASSWORD,
                    ContextParameterUtils.getOriginalValue(contextType, hivePassword));

            String ktPrincipal = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL,
                    ContextParameterUtils.getOriginalValue(contextType, ktPrincipal));

            String keytab = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_KEYTAB);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_KEYTAB,
                    ContextParameterUtils.getOriginalValue(contextType, keytab));

            String addtionalJDBCParameters =
                    conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_ADDITIONAL_JDBC_SETTINGS);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_ADDITIONAL_JDBC_SETTINGS,
                    ContextParameterUtils.getOriginalValue(contextType, addtionalJDBCParameters));

            String hiveEnableHa = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_ENABLE_HA);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_ENABLE_HA,
                    ContextParameterUtils.getOriginalValue(contextType, hiveEnableHa));

            String hiveMetastoreUris = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_METASTORE_URIS);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_METASTORE_URIS,
                    ContextParameterUtils.getOriginalValue(contextType, hiveMetastoreUris));

            String metastoreport = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_THRIFTPORT);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_THRIFTPORT,
                    ContextParameterUtils.getOriginalValue(contextType, metastoreport));

            String sslTrustStorePath = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PATH);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PATH,
                    ContextParameterUtils.getOriginalValue(contextType, sslTrustStorePath));

            String sslTrustStorePassword =
                    conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PASSWORD);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PASSWORD,
                    conn.getValue(ContextParameterUtils.getOriginalValue(contextType, sslTrustStorePassword), true));

            String maprticket_Username =
                    conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_USERNAME);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_USERNAME,
                    ContextParameterUtils.getOriginalValue(contextType, maprticket_Username));

            String maprticket_Password =
                    conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_PASSWORD);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_PASSWORD,
                    ContextParameterUtils.getOriginalValue(contextType, maprticket_Password));

            String maprticket_Cluster =
                    conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_CLUSTER);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_CLUSTER,
                    ContextParameterUtils.getOriginalValue(contextType, maprticket_Cluster));

            String maprticket_Duration =
                    conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_DURATION);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_DURATION,
                    ContextParameterUtils.getOriginalValue(contextType, maprticket_Duration));
        }
        // for Hbase
        if (EDatabaseTypeName.HBASE.equals(EDatabaseTypeName.getTypeFromDbType(conn.getDatabaseType()))) {
            String hbaseMasterPrin =
                    conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MASTERPRINCIPAL);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MASTERPRINCIPAL,
                    ContextParameterUtils.getOriginalValue(contextType, hbaseMasterPrin));

            String hbaseRegionPrin =
                    conn.getParameters()
                            .get(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_REGIONSERVERPRINCIPAL);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_REGIONSERVERPRINCIPAL,
                    ContextParameterUtils.getOriginalValue(contextType, hbaseRegionPrin));
            String znode_Parent = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_ZNODE_PARENT);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_ZNODE_PARENT,
                    ContextParameterUtils.getOriginalValue(contextType, znode_Parent));
            String hbaseKeyTabPrin = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL,
                    ContextParameterUtils.getOriginalValue(contextType, hbaseKeyTabPrin));

            String hbaseKeyTab = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_KEYTAB);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_KEYTAB,
                    ContextParameterUtils.getOriginalValue(contextType, hbaseKeyTab));

            String maprticket_Username =
                    conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_USERNAME);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_USERNAME,
                    ContextParameterUtils.getOriginalValue(contextType, maprticket_Username));

            String maprticket_Password =
                    conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_PASSWORD);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_PASSWORD,
                    ContextParameterUtils.getOriginalValue(contextType, maprticket_Password));

            String maprticket_Cluster =
                    conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_CLUSTER);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_CLUSTER,
                    ContextParameterUtils.getOriginalValue(contextType, maprticket_Cluster));

            String maprticket_Duration =
                    conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_DURATION);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_DURATION,
                    ContextParameterUtils.getOriginalValue(contextType, maprticket_Duration));

            String maprdbTableNSMapping =
                    conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_TABLE_NS_MAPPING);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_TABLE_NS_MAPPING,
                    ContextParameterUtils.getOriginalValue(contextType, maprdbTableNSMapping));
        }

        // for Maprdb
        if (EDatabaseTypeName.MAPRDB.equals(EDatabaseTypeName.getTypeFromDbType(conn.getDatabaseType()))) {
            String maprdbMasterPrin =
                    conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MASTERPRINCIPAL);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MASTERPRINCIPAL,
                    ContextParameterUtils.getOriginalValue(contextType, maprdbMasterPrin));

            String maprdbRegionPrin =
                    conn.getParameters().get(
                            ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_REGIONSERVERPRINCIPAL);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_REGIONSERVERPRINCIPAL,
                    ContextParameterUtils.getOriginalValue(contextType, maprdbRegionPrin));

            String znode_Parent = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_ZNODE_PARENT);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_ZNODE_PARENT,
                    ContextParameterUtils.getOriginalValue(contextType, znode_Parent));
            String maprdbKeyTabPrin = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL,
                    ContextParameterUtils.getOriginalValue(contextType, maprdbKeyTabPrin));

            String maprdbKeyTab = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_KEYTAB);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_KEYTAB,
                    ContextParameterUtils.getOriginalValue(contextType, maprdbKeyTab));

            String maprticket_Username =
                    conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_USERNAME);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_USERNAME,
                    ContextParameterUtils.getOriginalValue(contextType, maprticket_Username));

            String maprticket_Password =
                    conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_PASSWORD);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_PASSWORD,
                    ContextParameterUtils.getOriginalValue(contextType, maprticket_Password));

            String maprticket_Cluster =
                    conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_CLUSTER);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_CLUSTER,
                    ContextParameterUtils.getOriginalValue(contextType, maprticket_Cluster));

            String maprticket_Duration =
                    conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_DURATION);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_DURATION,
                    ContextParameterUtils.getOriginalValue(contextType, maprticket_Duration));

            String maprdbTableNSMapping =
                    conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_TABLE_NS_MAPPING);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_TABLE_NS_MAPPING,
                    ContextParameterUtils.getOriginalValue(contextType, maprdbTableNSMapping));
        }

        // for Impala
        if (EDatabaseTypeName.IMPALA.equals(EDatabaseTypeName.getTypeFromDbType(conn.getDatabaseType()))) {
            String impalaPrin = conn.getParameters().get(ConnParameterKeys.IMPALA_AUTHENTICATION_PRINCIPLA);
            conn.getParameters().put(ConnParameterKeys.IMPALA_AUTHENTICATION_PRINCIPLA,
                    ContextParameterUtils.getOriginalValue(contextType, impalaPrin));
        }

        if (EDatabaseTypeName.ORACLE_CUSTOM.equals(EDatabaseTypeName.getTypeFromDbType(conn.getDatabaseType()))) {
            String sslTrustStorePath = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PATH);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PATH,
                    ContextParameterUtils.getOriginalValue(contextType, sslTrustStorePath));

            String sslTrustStorePassword =
                    conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PASSWORD);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PASSWORD,
                    conn.getValue(ContextParameterUtils.getOriginalValue(contextType, sslTrustStorePassword), true));

            String sslKeyStorePath = conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_KEY_STORE_PATH);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_SSL_KEY_STORE_PATH,
                    ContextParameterUtils.getOriginalValue(contextType, sslKeyStorePath));

            String sslKeyStorePassword =
                    conn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_KEY_STORE_PASSWORD);
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_SSL_KEY_STORE_PASSWORD,
                    conn.getValue(ContextParameterUtils.getOriginalValue(contextType, sslKeyStorePassword), true));
        }
        // revert the context value in the hadoop properties dialog if it is hive or hbase
        String hadoopPropertiesJson = transformContextModeToOriginal(getHiveOrHbaseHadoopProperties(conn), contextType);
        setAdditionalHadoopProperties(conn, hadoopPropertiesJson);

        String hivePropertiesJson = transformContextModeToOriginal(getHiveJdbcProperties(conn), contextType);
        setAdditionalHiveJdbcProperties(conn, hivePropertiesJson);
    }

    private static String transformContextModeToOriginal(List<Map<String, Object>> additionalProperties,
            ContextType contextType) {
        String finalJsonProperties = "";
        if (!additionalProperties.isEmpty()) {
            for (Map<String, Object> propertyMap : additionalProperties) {
                String contextPropertyValue =
                        TalendQuoteUtils.removeQuotes(ContextParameterUtils.getOriginalValue(contextType,
                                (String) propertyMap.get("VALUE")));
                propertyMap.put("VALUE", contextPropertyValue);
            }
            finalJsonProperties = HadoopRepositoryUtil.getHadoopPropertiesJsonStr(additionalProperties);
        }
        return finalJsonProperties;
    }

    /**
     * revert the name of Catalog or schema
     * 
     * @param conn the target which need to revert
     * @param contextType the type of Context
     */
    private static void revertConnSubElement(DatabaseConnection conn, ContextType contextType) {
        if (conn.getSID() != null && !(conn.getSID().equals(""))) { //$NON-NLS-1$
            String sidOrDatabase = ConnectionContextHelper.getOriginalValue(contextType, conn.getSID());
            Catalog catalog = CatalogHelper.getCatalog(conn, conn.getSID());
            if (catalog != null) {
                catalog.setName(sidOrDatabase);
            }
        } else if (conn.getUiSchema() != null && !(conn.getUiSchema().equals(""))) { //$NON-NLS-1$
            String schemName = ConnectionContextHelper.getOriginalValue(contextType, conn.getUiSchema());
            Schema schema = SchemaHelper.getSchema(conn, conn.getUiSchema());
            if (schema != null) {
                schema.setName(schemName);
            }
        }
    }

    public static void setMetadataConnectionParameter(DatabaseConnection conn, IMetadataConnection metadataConnection) {
        if (conn == null || metadataConnection == null) {
            return;
        }
        ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(conn, conn.getContextName());
        if (contextType == null) {
            return;
        }

        // driverPath
        metadataConnection.setDriverJarPath(ConnectionContextHelper.getOriginalValue(contextType,
                conn.getDriverJarPath()));

        // set dbType
        metadataConnection.setDbType(ConnectionContextHelper.getOriginalValue(contextType, conn.getDatabaseType()));
        // set product(ProductId) and Schema(UISchema)
        EDatabaseTypeName edatabasetypeInstance =
                EDatabaseTypeName.getTypeFromDisplayName(ConnectionContextHelper.getOriginalValue(contextType,
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
        metadataConnection.setDbVersionString(ConnectionContextHelper.getOriginalValue(contextType,
                conn.getDbVersionString()));

        // filePath
        metadataConnection.setFileFieldName(ConnectionContextHelper.getOriginalValue(contextType,
                conn.getFileFieldName()));
        // jdbcUrl
        metadataConnection.setUrl(ConnectionContextHelper.getOriginalValue(contextType, conn.getURL()));
        // aDDParameter
        metadataConnection.setAdditionalParams(ConnectionContextHelper.getOriginalValue(contextType,
                conn.getAdditionalParams()));
        // driverClassName
        metadataConnection.setDriverClass(ConnectionContextHelper.getOriginalValue(contextType, conn.getDriverClass()));
        // host
        metadataConnection.setServerName(ConnectionContextHelper.getOriginalValue(contextType, conn.getServerName()));
        // port
        metadataConnection.setPort(ConnectionContextHelper.getOriginalValue(contextType, conn.getPort()));
        // dbName
        metadataConnection.setDatabase(ConnectionContextHelper.getOriginalValue(contextType, conn.getSID()));
        // otherParameter
        metadataConnection.setOtherParameter(ConnectionContextHelper.getOriginalValue(contextType,
                ConnectionHelper.getOtherParameter(conn)));
        // password
        metadataConnection.setPassword(ConnectionContextHelper.getOriginalValue(contextType,
                ConnectionHelper.getPassword(conn)));
        // user
        metadataConnection.setUsername(ConnectionContextHelper.getOriginalValue(contextType, conn.getUsername()));
        // dbName
        metadataConnection.setDataSourceName(ConnectionContextHelper.getOriginalValue(contextType,
                conn.getDatasourceName()));
        // schema
        metadataConnection.setSchema(conn.getUiSchema());
        // dbmsId
        if (metadataConnection.getMapping() == null) {
            metadataConnection.setMapping(ConnectionContextHelper.getOriginalValue(contextType, conn.getDbmsId()));
        }

    }

    /**
     * 
     * DOC zshen Comment method "setDatabaseConnectionParameter".
     * 
     * @param conn
     * @param metadataConnection
     * 
     * set parameter from metadataConnection to DatabaseConnection
     */
    public static void setDatabaseConnectionParameter(DatabaseConnection conn, IMetadataConnection metadataConnection) {
        if (conn == null || metadataConnection == null) {
            return;
        }

        // driverPath
        conn.setDriverJarPath(metadataConnection.getDriverJarPath());

        // set dbType
        conn.setDatabaseType(metadataConnection.getDbType());

        conn.setProductId(metadataConnection.getProduct());

        conn.setDbmsId(metadataConnection.getMapping());

        // set dbVersionString
        conn.setDbVersionString(metadataConnection.getDbVersionString());

        // filePath
        conn.setFileFieldName(metadataConnection.getFileFieldName());

        // jdbcUrl
        conn.setURL(metadataConnection.getUrl());

        // aDDParameter
        conn.setAdditionalParams(metadataConnection.getAdditionalParams());

        // driverClassName
        conn.setDriverClass(metadataConnection.getDriverClass());

        // host
        conn.setServerName(metadataConnection.getServerName());

        // port
        conn.setPort(metadataConnection.getPort());

        // dbName
        conn.setSID(metadataConnection.getDatabase());

        // otherParameter
        ConnectionHelper.setOtherParameter(metadataConnection.getOtherParameter(), conn);

        // password
        ConnectionHelper.setPassword(conn, metadataConnection.getPassword());

        // user
        conn.setUsername(metadataConnection.getUsername());

        // dbName
        conn.setDatasourceName(metadataConnection.getDataSourceName());

        // schema
        String uischema =
                metadataConnection.getUiSchema() == null ? metadataConnection.getSchema() : metadataConnection
                        .getUiSchema();
        conn.setUiSchema(uischema);

        conn.setContextMode(metadataConnection.isContentModel());
        conn.setContextId(metadataConnection.getContextId());
        conn.setContextName(metadataConnection.getContextName());
    }
}
