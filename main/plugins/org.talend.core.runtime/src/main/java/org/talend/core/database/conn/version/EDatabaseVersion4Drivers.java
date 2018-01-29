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
package org.talend.core.database.conn.version;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.database.conn.DatabaseConnConstants;

/**
 * cli class global comment. Detailled comment
 */
public enum EDatabaseVersion4Drivers {
    // access
    ACCESS_JDBC(new DbVersion4Drivers(EDatabaseTypeName.ACCESS, new String[] {
            "jackcess-2.1.0.jar", "ucanaccess-2.0.9.5.jar", "commons-lang-2.6.jar", "commons-logging-1.1.1.jar", "hsqldb.jar", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            "jackcess-encrypt-2.1.0.jar", "bcprov-jdk15on-1.51.jar", "talend-ucanaccess-utils-1.0.0.jar" })),
    ACCESS_2003(new DbVersion4Drivers(EDatabaseTypeName.ACCESS, "Access 2003", "Access_2003")), //$NON-NLS-1$ //$NON-NLS-2$
    ACCESS_2007(new DbVersion4Drivers(EDatabaseTypeName.ACCESS, "Access 2007", "Access_2007")), //$NON-NLS-1$ //$NON-NLS-2$
    // oracle
    ORACLE_12(new DbVersion4Drivers(new EDatabaseTypeName[] { EDatabaseTypeName.ORACLEFORSID, EDatabaseTypeName.ORACLESN,
            EDatabaseTypeName.ORACLE_OCI, EDatabaseTypeName.ORACLE_CUSTOM }, "Oracle 12", "ORACLE_12", "ojdbc7.jar")),
    ORACLE_11(new DbVersion4DriversForOracle11(new EDatabaseTypeName[] { EDatabaseTypeName.ORACLEFORSID,
            EDatabaseTypeName.ORACLESN, EDatabaseTypeName.ORACLE_OCI, EDatabaseTypeName.ORACLE_CUSTOM },
            "Oracle 11", "ORACLE_11", new String[] { DbVersion4DriversForOracle11.DRIVER_1_5, //$NON-NLS-1$ //$NON-NLS-2$
                    DbVersion4DriversForOracle11.DRIVER_1_6 })),
    ORACLE_10(new DbVersion4Drivers(new EDatabaseTypeName[] { EDatabaseTypeName.ORACLEFORSID, EDatabaseTypeName.ORACLESN,
            EDatabaseTypeName.ORACLE_OCI, EDatabaseTypeName.ORACLE_CUSTOM }, "Oracle 10", "ORACLE_10", "ojdbc14.jar")), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    ORACLE_9(new DbVersion4Drivers(new EDatabaseTypeName[] { EDatabaseTypeName.ORACLEFORSID, EDatabaseTypeName.ORACLESN,
            EDatabaseTypeName.ORACLE_OCI, EDatabaseTypeName.ORACLE_CUSTOM }, "Oracle 9", "ORACLE_9", "ojdbc14-9i.jar")), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    ORACLE_8(new DbVersion4Drivers(new EDatabaseTypeName[] { EDatabaseTypeName.ORACLEFORSID, EDatabaseTypeName.ORACLESN,
            EDatabaseTypeName.ORACLE_OCI, EDatabaseTypeName.ORACLE_CUSTOM }, "Oracle 8", "ORACLE_8", "ojdbc12.jar")), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    // AS400
    AS400_V6R1_V7R2(new DbVersion4Drivers(EDatabaseTypeName.AS400, "V6R1 to V7R2", "AS400_V6R1_V7R2", "jt400_V6R1.jar")), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    AS400_V5R3_V6R1(new DbVersion4Drivers(EDatabaseTypeName.AS400, "V5R3 to V6R1", "AS400_V5R3_V6R1", "jt400_V5R3.jar")), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    AS400_V5R2_V5R4(new DbVersion4Drivers(EDatabaseTypeName.AS400, "V5R2 to V5R4", "AS400_V5R2_V5R4", "jt400_V5R2.jar")), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    //
    INTERBASE(new DbVersion4Drivers(EDatabaseTypeName.INTERBASE, "interclient.jar")), //$NON-NLS-1$

    //
    HSQLDB(new DbVersion4Drivers(EDatabaseTypeName.HSQLDB, "hsqldb.jar")), //$NON-NLS-1$
    HSQLDB_IN_PROGRESS(new DbVersion4Drivers(EDatabaseTypeName.HSQLDB_IN_PROGRESS, "hsqldb.jar")), //$NON-NLS-1$
    HSQLDB_SERVER(new DbVersion4Drivers(EDatabaseTypeName.HSQLDB_SERVER, "hsqldb.jar")), //$NON-NLS-1$
    HSQLDB_WEBSERVER(new DbVersion4Drivers(EDatabaseTypeName.HSQLDB_WEBSERVER, "hsqldb.jar")), //$NON-NLS-1$

    H2(new DbVersion4Drivers(EDatabaseTypeName.H2, "h2-1.3.160.jar")), //$NON-NLS-1$ 

    //
    JAVADB_EMBEDED(new DbVersion4Drivers(EDatabaseTypeName.JAVADB_EMBEDED, "derby.jar")), //$NON-NLS-1$
    SQLITE(new DbVersion4Drivers(EDatabaseTypeName.SQLITE, "sqlitejdbc-v056.jar")), //$NON-NLS-1$
    FIREBIRD(new DbVersion4Drivers(EDatabaseTypeName.FIREBIRD, "jaybird-full-2.1.1.jar")), //$NON-NLS-1$
    TERADATA(new DbVersion4Drivers(EDatabaseTypeName.TERADATA,
            new String[] { "terajdbc4-15.10.00.14.jar", "tdgssconfig-15.10.00.14.jar" })), //$NON-NLS-1$ //$NON-NLS-2$
    JAVADB_DERBYCLIENT(new DbVersion4Drivers(EDatabaseTypeName.JAVADB_DERBYCLIENT, "derbyclient.jar")), //$NON-NLS-1$
    NETEZZA(new DbVersion4Drivers(EDatabaseTypeName.NETEZZA, "nzjdbc.jar")), //$NON-NLS-1$
    INFORMIX(new DbVersion4Drivers(EDatabaseTypeName.INFORMIX, "ifxjdbc.jar")), //$NON-NLS-1$

    SAS_9_1(new DbVersion4Drivers(EDatabaseTypeName.SAS, "SAS 9.1", "SAS_9.1", new String[] { "sas.core.jar", //$NON-NLS-1$
            "sas.intrnet.javatools.jar", "sas.svc.connection.jar" })), //$NON-NLS-1$ //$NON-NLS-2$
    SAS_9_2(new DbVersion4Drivers(EDatabaseTypeName.SAS,
            "SAS 9.2", "SAS_9.2", new String[] { "sas.core.jar", "sas.security.sspi.jar", "sas.svc.connection.jar" })), //$NON-NLS-1$ //$NON-NLS-2$
    SAPHana(new DbVersion4Drivers(EDatabaseTypeName.SAPHana, "HDB 1.0", "HDB_1_0", "ngdbc.jar")), //$NON-NLS-1$
    // MYSQL, add for 9594
    MYSQL_5(new DbVersion4Drivers(EDatabaseTypeName.MYSQL, "MySQL 5", "MYSQL_5", "mysql-connector-java-5.1.30-bin.jar")), //$NON-NLS-1$  //$NON-NLS-2$ //$NON-NLS-3$
    MYSQL_4(new DbVersion4Drivers(EDatabaseTypeName.MYSQL, "MySQL 4", "MYSQL_4", "mysql-connector-java-3.1.14-bin.jar")), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    MARIADB(new DbVersion4Drivers(EDatabaseTypeName.MYSQL, "MariaDB", "MARIADB", "mariadb-java-client-1.1.7.jar")), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    // add for 9594
    MSSQL(new DbVersion4Drivers(EDatabaseTypeName.MSSQL,"Open source JTDS", "JTDS", "jtds-1.3.1-patch.jar")), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    MSSQL_2012(new DbVersion4Drivers(EDatabaseTypeName.MSSQL,
            "Microsoft SQL Server 2012", "Microsoft SQL Server 2012", "jtds-1.3.1-patch.jar")), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    MSSQL_PROP(new DbVersion4Drivers(EDatabaseTypeName.MSSQL,
            "Microsoft", "MSSQL_PROP", "mssql-jdbc.jar")), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    
    VERTICA_9(new DbVersion4Drivers(EDatabaseTypeName.VERTICA, "VERTICA 9.0", "VERTICA_9_0", "vertica-jdbc-9.0.0-0.jar")), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    VERTICA_7(new DbVersion4Drivers(EDatabaseTypeName.VERTICA, "VERTICA 7.0.x", "VERTICA_7_0_X", "vertica-jdbc-7.0.1-0.jar")), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    VERTICA_6_1_X(new DbVersion4Drivers(EDatabaseTypeName.VERTICA, "VERTICA 6.1.x", "VERTICA_6_1_X", "vertica-jdk5-6.1.2-0.jar")), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    VERTICA_6(new DbVersion4Drivers(EDatabaseTypeName.VERTICA, "VERTICA 6.0", "VERTICA_6_0", "vertica-jdk5-6.0.0-0.jar")), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    VERTICA_5_1(new DbVersion4Drivers(EDatabaseTypeName.VERTICA, "VERTICA 5.1", "VERTICA_5_1", "vertica_5.1.6_jdk_5.jar")), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    VERTICA_5(new DbVersion4Drivers(EDatabaseTypeName.VERTICA, "VERTICA 5", "VERTICA_5", "vertica_4.1.14_jdk_5.jar")), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    VERTICA_4_1(new DbVersion4Drivers(EDatabaseTypeName.VERTICA, "VERTICA 4.1", "VERTICA_4_1", "vertica_4.1.7_jdk_5.jar")), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    VERTICA_4(new DbVersion4Drivers(EDatabaseTypeName.VERTICA, "VERTICA 4", "VERTICA_4", "vertica_4.0_jdk_5.jar")), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    VERTICA_3(new DbVersion4Drivers(EDatabaseTypeName.VERTICA, "VERTICA 3", "VERTICA_3", "vertica_3.0_jdk_5.jar")), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    VERTICA_3_5(new DbVersion4Drivers(EDatabaseTypeName.VERTICA, "VERTICA 3.5", "VERTICA_3.5", "vertica_3.5_jdk_5.jar")), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    // for bug 0017930
    GREENPLUM(new DbVersion4Drivers(EDatabaseTypeName.GREENPLUM, "postgresql-8.3-603.jdbc3.jar")), //$NON-NLS-1$ 
    PSQL_PRIOR_TO_V9(new DbVersion4Drivers(EDatabaseTypeName.PSQL, "Prior to v9", "PRIOR_TO_V9", "postgresql-8.3-603.jdbc3.jar")), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
    PSQL_V9_X(new DbVersion4Drivers(EDatabaseTypeName.PSQL, "v9.X", "V9_X", "postgresql-9.4-1201.jdbc41.jar")), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
    PLUSPSQL_PRIOR_TO_V9(new DbVersion4Drivers(EDatabaseTypeName.PLUSPSQL,
            "Prior to v9", "PRIOR_TO_V9", "postgresql-8.3-603.jdbc3.jar")), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    PLUSPSQL_V9_X(new DbVersion4Drivers(EDatabaseTypeName.PLUSPSQL, "v9.X", "V9_X", "postgresql-9.4-1201.jdbc41.jar")), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    IBMDB2(new DbVersion4Drivers(EDatabaseTypeName.IBMDB2, new String[] { "db2jcc4.jar", "db2jcc_license_cu.jar", //$NON-NLS-1$ //$NON-NLS-2$
            "db2jcc_license_cisuz.jar" })), //$NON-NLS-1$ 
    IBMDB2ZOS(new DbVersion4Drivers(EDatabaseTypeName.IBMDB2ZOS, new String[] { "db2jcc4.jar", "db2jcc_license_cu.jar", //$NON-NLS-1$ //$NON-NLS-2$
            "db2jcc_license_cisuz.jar" })), //$NON-NLS-1$ 
    SYBASEASE(new DbVersion4Drivers(EDatabaseTypeName.SYBASEASE, "Sybase 12/15", "SYBSEIQ_12_15", "jconn3.jar")), //$NON-NLS-1$ 
    SYBASEIQ_16(new DbVersion4Drivers(EDatabaseTypeName.SYBASEASE, "Sybase 16", "SYBSEIQ_16", "jconn4.jar")), //$NON-NLS-1$ 
    SYBASEIQ(new DbVersion4Drivers(EDatabaseTypeName.SYBASEIQ, "jconn3.jar")), //$NON-NLS-1$ 
    
    // for bug 0013127
    PARACCEL(new DbVersion4Drivers(EDatabaseTypeName.PARACCEL, "paraccel-jdbc.jar")), //$NON-NLS-1$
    VECTORWISE(new DbVersion4Drivers(EDatabaseTypeName.VECTORWISE, "iijdbc.jar")), //$NON-NLS-1$

    EXASOL(new DbVersion4Drivers(EDatabaseTypeName.EXASOL, "exajdbc-6.0.9302.jar")), //$NON-NLS-1$
    MAXDB(new DbVersion4Drivers(EDatabaseTypeName.MAXDB, "sapdbc.jar")), //$NON-NLS-1$

    INGRES(new DbVersion4Drivers(EDatabaseTypeName.INGRES, "iijdbc.jar")), //$NON-NLS-1$

    // HIVE(new DbVersion4Drivers(EDatabaseTypeName.HIVE, "STANDALONE", "STANDALONE", new String[] {
    // "hive-jdbc-0.8.1.jar",
    // "hive-metastore-0.8.1.jar", "hive-exec-0.8.1.jar", "hive-service-0.8.1.jar", "libfb303_new.jar",
    // "hadoop-core-1.0.0.jar", "commons-logging-1.0.4.jar", "log4j-1.2.15.jar", "slf4j-api-1.6.1.jar",
    // "slf4j-log4j12-1.6.1.jar" })),

    HIVE_2_EMBEDDED(new DbVersion4Drivers(EDatabaseTypeName.HIVE, "STANDALONE", "STANDALONE", new String[] {})),

    HIVE_2_STANDALONE(new DbVersion4Drivers(EDatabaseTypeName.HIVE, "STANDALONE", "STANDALONE", new String[] {})),

    HIVE(new DbVersion4Drivers(EDatabaseTypeName.HIVE, "STANDALONE", "STANDALONE", new String[] {})),

    /**
     * Added by Marvin Wang on Aug. 7, 2012 for feature TDI-22130. It is just to add a new EMBEDDED mode for Hive.
     */
    // HIVE_EMBEDDED(new DbVersion4Drivers(EDatabaseTypeName.HIVE, "EMBEDDED", "EMBEDDED", new String[] {
    // "antlr-runtime-3.0.1.jar",
    // "commons-dbcp-1.4.jar", "commons-lang-2.4.jar", "commons-logging-1.0.4.jar",
    // "commons-pool-1.5.4.jar","datanucleus-api-jdo-3.0.7.jar",
    // "datanucleus-connectionpool-2.0.3.jar", "datanucleus-core-3.0.7.jar", "datanucleus-enhancer-2.0.3.jar",
    // "datanucleus-rdbms-3.0.8.jar", "derby-10.4.2.0.jar", "guava-r09.jar", "hadoop-auth-2.0.0-cdh4.0.1.jar",
    // // "hadoop-common-2.0.0-cdh4.0.1.jar",
    // "hadoop-core-1.0.3.jar", "hive-builtins-0.9.0.jar",
    // "hive-exec-0.9.0.jar", "hive-jdbc-0.9.0.jar",
    // "hive-metastore-0.9.0.jar","commons-configuration-1.6.jar","aspectjtools-1.6.5.jar",
    // "hive-service-0.9.0.jar", "jdo2-api-2.3-eb.jar", "libfb303-0.7.0.jar", "libthrift-0.7.0.jar",
    // "log4j-1.2.16.jar", "slf4j-api-1.6.1.jar", "slf4j-log4j12-1.6.1.jar",
    // "mysql-connector-java-5.1.21-bin.jar","jackson-core-asl-1.8.8.jar"
    // ,"jackson-mapper-asl-1.8.8.jar"})),

    // All jars are referred from tHiveConnection_java.xml
    HIVE_EMBEDDED(new DbVersion4Drivers(EDatabaseTypeName.HIVE, "EMBEDDED", "EMBEDDED", new String[] {})),
    // Changed by Marvin Wang on Oct.9, 2012, just because the libs checking can not pass, Remy updated some jars from
    HBASE(new DbVersion4Drivers(EDatabaseTypeName.HBASE, new String[] {})),

    MAPRDB(new DbVersion4Drivers(EDatabaseTypeName.MAPRDB, new String[] {})),

    REDSHIFT(new DbVersion4Drivers(EDatabaseTypeName.REDSHIFT, "RedshiftJDBC41-1.2.10.1009.jar")), //$NON-NLS-1$

    AMAZON_AURORA(new DbVersion4Drivers(EDatabaseTypeName.AMAZON_AURORA, "mysql-connector-java-5.1.30-bin.jar")); //$NON-NLS-1$

    private DbVersion4Drivers dbVersionBean;

    EDatabaseVersion4Drivers(DbVersion4Drivers dbVersionBean) {
        this.dbVersionBean = dbVersionBean;
    }

    public String getVersionDisplay() {
        return this.dbVersionBean.getVersionDisplayName();
    }

    public String getVersionValue() {
        return this.dbVersionBean.getVersionValue();
    }

    public Set<String> getProviderDrivers() {
        return this.dbVersionBean.getDrivers();
    }

    private List<EDatabaseTypeName> getSupportDbTypes() {
        return this.dbVersionBean.getDbTypes();
    }

    public boolean supportDatabase(EDatabaseTypeName dbType) {
        if (dbType != null) {
            return getSupportDbTypes().contains(dbType);
        }
        return false;
    }

    public boolean supportDatabase(String dbType) {
        if (dbType != null) {
            for (EDatabaseTypeName type : getSupportDbTypes()) {
                if (type.getXmlName().equalsIgnoreCase(dbType) || type.getDisplayName().equalsIgnoreCase(dbType)
                        || type.getXMLType().equalsIgnoreCase(dbType)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static EDatabaseVersion4Drivers indexOfByVersionDisplay(String displayName) {
        return indexOf(displayName, true);
    }

    public static EDatabaseVersion4Drivers indexOfByVersion(String value) {
        return indexOf(value, false);
    }

    private static EDatabaseVersion4Drivers indexOf(String name, boolean display) {
        if (name != null) {
            for (EDatabaseVersion4Drivers version : EDatabaseVersion4Drivers.values()) {
                if (display) {
                    if (name.equalsIgnoreCase(version.getVersionDisplay())) {
                        return version;
                    }
                } else {
                    if (name.equalsIgnoreCase(version.getVersionValue())) {
                        return version;
                    }
                }
            }
        }
        return null;
    }

    public static List<EDatabaseVersion4Drivers> indexOfByDbType(String dbType) {
        List<EDatabaseVersion4Drivers> v4dList = new ArrayList<EDatabaseVersion4Drivers>();
        if (dbType != null) {
            for (EDatabaseVersion4Drivers v4d : EDatabaseVersion4Drivers.values()) {
                if (v4d.supportDatabase(dbType)) {
                    v4dList.add(v4d);
                }
            }
        }
        return v4dList;
    }

    public static String getDbVersionName(final EDatabaseTypeName dbType, final String driverName) {
        if (dbType != null) {
            return getDbVersionName(dbType.getXmlName(), driverName);
        } else {
            return getDbVersionName((String) null, driverName);
        }
    }

    public static String getDbVersionName(final String dbType, final String driverName) {
        for (EDatabaseVersion4Drivers v4d : EDatabaseVersion4Drivers.values()) {
            if (driverName != null && dbType != null) {
                if (v4d.supportDatabase(dbType)
                        && (v4d.getProviderDrivers().contains(driverName) || driverName.equals(v4d.getVersionValue()))) {
                    return v4d.getVersionValue();
                }
            } else if (driverName != null && dbType == null) {
                if (v4d.getProviderDrivers().contains(driverName)) {
                    return v4d.getVersionValue();
                }
            } else if (driverName == null && dbType != null) {
                if (v4d.supportDatabase(dbType)) {
                    return v4d.getVersionValue();
                }
            }
        }
        return null;
    }

    public static String getDriversStr(final String dbType, final String version) {
        List<String> drivers = getDrivers(dbType, version);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < drivers.size(); i++) {
            String str = drivers.get(i);
            if (str != null && !DatabaseConnConstants.EMPTY.equals(str.trim())) {
                if (i < drivers.size() - 1) {
                    sb.append(str);
                    sb.append(';');
                } else {
                    sb.append(str);
                }
            }
        }
        return sb.toString();
    }

    public static List<String> getDrivers(final String dbType, final String version) {
        List<String> drivers = new ArrayList<String>();
        for (EDatabaseVersion4Drivers v4d : EDatabaseVersion4Drivers.values()) {
            if (dbType != null) {
                if (v4d.supportDatabase(dbType)) {
                    if (version == null || v4d.getVersionValue() == null || version.equals("")) { // add all for this db
                                                                                                  // type
                        drivers.addAll(v4d.getProviderDrivers());
                    } else
                    // check both db type and version value.
                    if (version.equalsIgnoreCase(v4d.getVersionValue())) {
                        drivers.addAll(v4d.getProviderDrivers());
                    }
                }
            } else {
                // only check the version value
                if (version != null && version.equalsIgnoreCase(v4d.getVersionValue())) {
                    drivers.addAll(v4d.getProviderDrivers());
                }
            }
        }
        return drivers;
    }

    public static boolean containTypeAndVersion(final String dbType, final String version) {
        if (version == null) {
            return false;
        }
        for (EDatabaseVersion4Drivers v4d : EDatabaseVersion4Drivers.values()) {
            if (v4d.supportDatabase(dbType)) {
                if (version.equalsIgnoreCase(v4d.getVersionValue())) {
                    return true;
                }
            }
        }
        return false;
    }
}
