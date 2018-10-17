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
package org.talend.core.database.conn;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.database.conn.version.EDatabaseVersion4Drivers;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.connection.hive.HiveModeInfo;

/**
 * created by cmeng on May 20, 2016
 * Detailled comment
 *
 */
@SuppressWarnings("nls")
public class DatabaseConnStrUtilTest {

    private static final String HIVE2_STANDARDLONE_URL = "jdbc:hive2://server:10000/default";

    private DatabaseConnection createDatabaseConnection() {
        DatabaseConnection databaseConnection = null;

        databaseConnection = ConnectionFactory.eINSTANCE.createDatabaseConnection();

        databaseConnection.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_USE_KRB, "false");

        return databaseConnection;
    }

    @Test
    public void testGetURLStringForStandardalone() {
        String server = "server";
        String port = "10000";
        String sidOrDatabase = "default";
        String trustStorePath = "/home/user/truststore";
        String trustStorePassword = "pwd123";
        String additionalJDBCSettings = "additionalJDBCSettings123";
        String expectValue = "jdbc:hive2://" + server + ":" + port + "/" + sidOrDatabase + ";ssl=true;sslTrustStore="
                + trustStorePath + ";trustStorePassword=encrypted;" + additionalJDBCSettings;
        DatabaseConnection dc = createDatabaseConnection();
        dc.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_USE_SSL, "true");
        dc.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PATH, trustStorePath);
        dc.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PASSWORD, trustStorePassword);
        dc.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_ADDITIONAL_JDBC_SETTINGS, additionalJDBCSettings);
        String realValue = DatabaseConnStrUtil.getHiveURLStringForStandardalone(expectValue, dc, server, port, sidOrDatabase);
        assertTrue(expectValue.equals(realValue));

        dc.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_ADDITIONAL_JDBC_SETTINGS, "");
        expectValue = "jdbc:hive2://" + server + ":" + port + "/" + sidOrDatabase + ";ssl=true;sslTrustStore=" + trustStorePath
                + ";trustStorePassword=encrypted";
        realValue = DatabaseConnStrUtil.getHiveURLStringForStandardalone(expectValue, dc, server, port, sidOrDatabase);
        assertTrue(expectValue.equals(realValue));

        dc.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_USE_SSL, "false");
        expectValue = "jdbc:hive2://" + server + ":" + port + "/" + sidOrDatabase;
        realValue = DatabaseConnStrUtil.getHiveURLStringForStandardalone(expectValue, dc, server, port, sidOrDatabase);
        assertTrue(expectValue.equals(realValue));

        dc.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_ADDITIONAL_JDBC_SETTINGS, additionalJDBCSettings);
        expectValue = "jdbc:hive2://" + server + ":" + port + "/" + sidOrDatabase + ";" + additionalJDBCSettings;
        realValue = DatabaseConnStrUtil.getHiveURLStringForStandardalone(expectValue, dc, server, port, sidOrDatabase);
        assertTrue(expectValue.equals(realValue));

    }

    @Test
    public void testGetHiveURLString() {
        String server = "server";
        String port = "10000";
        String sidOrDatabase = "default";
        String trustStorePath = "/home/user/truststore";
        String trustStorePassword = "pwd123";
        String additionalJDBCSettings = "additionalJDBCSettings123";
        String expectValue = "jdbc:hive2://" + server + ":" + port + "/" + sidOrDatabase + ";ssl=true;sslTrustStore="
                + trustStorePath + ";trustStorePassword=" + trustStorePassword + ";" + additionalJDBCSettings;

        DatabaseConnection dc = createDatabaseConnection();
        dc.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_USE_SSL, "true");
        dc.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PATH, trustStorePath);
        dc.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PASSWORD, trustStorePassword);
        dc.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_ADDITIONAL_JDBC_SETTINGS, additionalJDBCSettings);
        dc.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_MODE, HiveModeInfo.STANDALONE.getName());

        String realValue = DatabaseConnStrUtil.getHiveURLString(dc, server, port, sidOrDatabase, HIVE2_STANDARDLONE_URL);
        assertTrue(expectValue.equals(realValue));

        dc.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_ADDITIONAL_JDBC_SETTINGS, "");
        expectValue = "jdbc:hive2://" + server + ":" + port + "/" + sidOrDatabase + ";ssl=true;sslTrustStore=" + trustStorePath
                + ";trustStorePassword=" + trustStorePassword;
        realValue = DatabaseConnStrUtil.getHiveURLString(dc, server, port, sidOrDatabase, HIVE2_STANDARDLONE_URL);
        assertTrue(expectValue.equals(realValue));

        dc.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_USE_SSL, "false");
        expectValue = "jdbc:hive2://" + server + ":" + port + "/" + sidOrDatabase;
        realValue = DatabaseConnStrUtil.getHiveURLString(dc, server, port, sidOrDatabase, HIVE2_STANDARDLONE_URL);
        assertTrue(expectValue.equals(realValue));

        dc.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_ADDITIONAL_JDBC_SETTINGS, additionalJDBCSettings);
        expectValue = "jdbc:hive2://" + server + ":" + port + "/" + sidOrDatabase + ";" + additionalJDBCSettings;
        realValue = DatabaseConnStrUtil.getHiveURLString(dc, server, port, sidOrDatabase, HIVE2_STANDARDLONE_URL);
        assertTrue(expectValue.equals(realValue));
    }
    
    @Test 
    public void testAnalyseURLForVertica(){
        String url = "jdbc:vertica://localhost:5433/test_db?connectionTimeout=10000";
        String[] analyseURL = DatabaseConnStrUtil.analyseURL("Vertica", "VERTICA_7", url);
        Assert.assertEquals(analyseURL.length, 6);
        Assert.assertEquals(analyseURL[4], "connectionTimeout=10000");
        url = "jdbc:vertica://localhost:5433/test_db?connectionTimeout=10000&ConnectionLoadBalance=1";
        analyseURL = DatabaseConnStrUtil.analyseURL("Vertica", "VERTICA_7", url);
        Assert.assertEquals(analyseURL.length, 6);
        Assert.assertEquals(analyseURL[4], "connectionTimeout=10000&ConnectionLoadBalance=1");
        url = "jdbc:vertica://localhost:5433/test_db?";
        analyseURL = DatabaseConnStrUtil.analyseURL("Vertica", "VERTICA_7", url);
        Assert.assertEquals(analyseURL.length, 6);
        Assert.assertEquals(analyseURL[4], "");
        url = "jdbc:vertica://localhost:5433/test_db";
        analyseURL = DatabaseConnStrUtil.analyseURL("Vertica", "VERTICA_7", url);
        Assert.assertEquals(analyseURL.length, 6);
        Assert.assertEquals(analyseURL[4], "");
    }

    @Test
    public void testGetURLStringForMSSQL() {
        String dbType = EDatabaseTypeName.MSSQL.getDisplayName();
        String dbVersion = EDatabaseVersion4Drivers.MSSQL_PROP.getVersionValue();
        String host = "lcoalhost";
        String port = "";
        String sid = "master";
        String[] otherParam = new String[] {};
        String expectURL = "jdbc:sqlserver://" + host + ";DatabaseName=master;";
        String realValue = DatabaseConnStrUtil.getURLString(false, dbType, dbVersion, host, "", "", port, sid, "", "", "", "",
                otherParam);
        assertTrue(expectURL.equals(realValue));

        port = "1433";
        expectURL = "jdbc:sqlserver://" + host + ":" + port + ";DatabaseName=master;";
        realValue = DatabaseConnStrUtil.getURLString(false, dbType, dbVersion, host, "", "", port, sid, "", "", "", "",
                otherParam);
        assertTrue(expectURL.equals(realValue));
    }
}
