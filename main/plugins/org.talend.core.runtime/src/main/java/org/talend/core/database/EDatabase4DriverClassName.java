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
package org.talend.core.database;

import java.util.ArrayList;
import java.util.List;

/**
 * cli class global comment. Detailled comment
 */
public enum EDatabase4DriverClassName {
    ACCESS(EDatabaseTypeName.ACCESS, "net.ucanaccess.jdbc.UcanaccessDriver"), //$NON-NLS-1$
    AS400(EDatabaseTypeName.AS400, "com.ibm.as400.access.AS400JDBCDriver"), //$NON-NLS-1$
    FIREBIRD(EDatabaseTypeName.FIREBIRD, "org.firebirdsql.jdbc.FBDriver"), //$NON-NLS-1$
    GODBC(EDatabaseTypeName.GODBC, "sun.jdbc.odbc.JdbcOdbcDriver"), //$NON-NLS-1$

    HSQLDB(EDatabaseTypeName.HSQLDB, "org.hsqldb.jdbcDriver"), //$NON-NLS-1$
    HSQLDB_IN_PROGRESS(EDatabaseTypeName.HSQLDB_IN_PROGRESS, "org.hsqldb.jdbcDriver"), //$NON-NLS-1$
    HSQLDB_SERVER(EDatabaseTypeName.HSQLDB_SERVER, "org.hsqldb.jdbcDriver"), //$NON-NLS-1$
    HSQLDB_WEBSERVER(EDatabaseTypeName.HSQLDB_WEBSERVER, "org.hsqldb.jdbcDriver"), //$NON-NLS-1$

    IBMDB2(EDatabaseTypeName.IBMDB2, "com.ibm.db2.jcc.DB2Driver"), //$NON-NLS-1$
    IBMDB2ZOS(EDatabaseTypeName.IBMDB2ZOS, "COM.ibm.db2os390.sqlj.jdbc.DB2SQLJDriver"), //$NON-NLS-1$

    INFORMIX(EDatabaseTypeName.INFORMIX, "com.informix.jdbc.IfxDriver"), //$NON-NLS-1$
    INGRES(EDatabaseTypeName.INGRES, "com.ingres.jdbc.IngresDriver"), //$NON-NLS-1$
    INTERBASE(EDatabaseTypeName.INTERBASE, "interbase.interclient.Driver"), //$NON-NLS-1$
    VECTORWISE(EDatabaseTypeName.VECTORWISE, "com.ingres.jdbc.IngresDriver"), //$NON-NLS-1$

    JAVADB_DERBYCLIENT(EDatabaseTypeName.JAVADB_DERBYCLIENT, "org.apache.derby.jdbc.ClientDriver"), //$NON-NLS-1$
    JAVADB_EMBEDED(EDatabaseTypeName.JAVADB_EMBEDED, "org.apache.derby.jdbc.EmbeddedDriver"), //$NON-NLS-1$
    JAVADB_JCCJDBC(EDatabaseTypeName.JAVADB_JCCJDBC, "com.ibm.db2.jcc.DB2Driver"), //$NON-NLS-1$

    MAXDB(EDatabaseTypeName.MAXDB, "com.sap.dbtech.jdbc.DriverSapDB"), //$NON-NLS-1$

    MSODBC(EDatabaseTypeName.MSODBC, "sun.jdbc.odbc.JdbcOdbcDriver"), //$NON-NLS-1$
    MSSQL(EDatabaseTypeName.MSSQL, "net.sourceforge.jtds.jdbc.Driver"), //$NON-NLS-1$
    MSSQL2(EDatabaseTypeName.MSSQL05_08, "com.microsoft.sqlserver.jdbc.SQLServerDriver"), //$NON-NLS-1$

    MYSQL(EDatabaseTypeName.MYSQL, "org.gjt.mm.mysql.Driver"), //$NON-NLS-1$
    MYSQL2(EDatabaseTypeName.MYSQL, "com.mysql.jdbc.Driver"), //$NON-NLS-1$
    MYSQL8(EDatabaseTypeName.MYSQL, "com.mysql.cj.jdbc.Driver"), //$NON-NLS-1$
    MARIADB(EDatabaseTypeName.MYSQL, "org.mariadb.jdbc.Driver"), //$NON-NLS-1$
    AMAZON_AURORA(EDatabaseTypeName.AMAZON_AURORA, "org.gjt.mm.mysql.Driver"), //$NON-NLS-1$
    NETEZZA(EDatabaseTypeName.NETEZZA, "org.netezza.Driver"), //$NON-NLS-1$

    ORACLEFORSID(EDatabaseTypeName.ORACLEFORSID, "oracle.jdbc.OracleDriver", "oracle.jdbc.driver.OracleDriver"), //$NON-NLS-1$ //$NON-NLS-2$
    ORACLESN(EDatabaseTypeName.ORACLESN, "oracle.jdbc.OracleDriver", "oracle.jdbc.driver.OracleDriver"), //$NON-NLS-1$ //$NON-NLS-2$
    ORACLE_OCI(EDatabaseTypeName.ORACLE_OCI, "oracle.jdbc.OracleDriver", "oracle.jdbc.driver.OracleDriver"), //$NON-NLS-1$ //$NON-NLS-2$
    ORACLE_CUSTOM(EDatabaseTypeName.ORACLE_CUSTOM, "oracle.jdbc.OracleDriver", "oracle.jdbc.driver.OracleDriver"), //$NON-NLS-1$ //$NON-NLS-2$

    PARACCEL(EDatabaseTypeName.PARACCEL, "com.paraccel.Driver"), //$NON-NLS-1$
    REDSHIFT(EDatabaseTypeName.REDSHIFT, "com.amazon.redshift.jdbc42.Driver"), //$NON-NLS-1$
    REDSHIFT_SSO(EDatabaseTypeName.REDSHIFT_SSO, "com.amazon.redshift.jdbc42.Driver"),

    // Take care, PSQL is set as first here since actually the audit will take the first db with the postgresql driver
    // if change the order, it might break the audit for PSQL.
    PSQL(EDatabaseTypeName.PSQL, "org.postgresql.Driver"), //$NON-NLS-1$
    PLUSPSQL(EDatabaseTypeName.PLUSPSQL, "org.postgresql.Driver"), //$NON-NLS-1$
    GREENPLUM(EDatabaseTypeName.GREENPLUM, "org.postgresql.Driver"), //$NON-NLS-1$

    SAS(EDatabaseTypeName.SAS, "com.sas.rio.MVADriver"), //$NON-NLS-1$
    SAPHana(EDatabaseTypeName.SAPHana, "com.sap.db.jdbc.Driver"), //$NON-NLS-1$
    SQLITE(EDatabaseTypeName.SQLITE, "org.sqlite.JDBC"), //$NON-NLS-1$

    SYBASEASE(EDatabaseTypeName.SYBASEASE, new String[] {
            "com.sybase.jdbc3.jdbc.SybDriver", "com.sybase.jdbc3.jdbc.SybDataSource" }, new String[] {}), //$NON-NLS-1$
    SYBASEIQ(
             EDatabaseTypeName.SYBASEIQ,
             new String[] { "com.sybase.jdbc3.jdbc.SybDriver", "com.sybase.jdbc3.jdbc.SybDataSource" }, new String[] {}), //$NON-NLS-1$
    SYBASEIQ_16(
            EDatabaseTypeName.SYBASEASE,
            new String[] { "com.sybase.jdbc4.jdbc.SybDriver", "com.sybase.jdbc4.jdbc.SybDataSource" }, new String[] {}), //$NON-NLS-1$
    SYBASEIQ_16_SA(
    		EDatabaseTypeName.SYBASEASE,
    		new String[] { "sap.jdbc4.sqlanywhere.IDriver"}, new String[] {}), //$NON-NLS-1$
    EXASOLUTION(EDatabaseTypeName.EXASOL, "com.exasol.jdbc.EXADriver"), //$NON-NLS-1$
    // MOD klliu 2010-06-04 bug 12819: upgrade jdbc driver class used in sql explorer
    TERADATA(EDatabaseTypeName.TERADATA, "com.teradata.jdbc.TeraDriver"), //$NON-NLS-1$

    VERTICA(EDatabaseTypeName.VERTICA, "com.vertica.Driver"), //$NON-NLS-1$
    VERTICA2(EDatabaseTypeName.VERTICA, "com.vertica.jdbc.Driver"), //$NON-NLS-1$

    HIVE(EDatabaseTypeName.HIVE, "org.apache.hadoop.hive.jdbc.HiveDriver"), //$NON-NLS-1$

    HIVE2(EDatabaseTypeName.HIVE, "org.apache.hive.jdbc.HiveDriver"), //$NON-NLS-1$

    IMPALA(EDatabaseTypeName.IMPALA, "org.apache.hive.jdbc.HiveDriver"), //$NON-NLS-1$ 

    H2(EDatabaseTypeName.H2, "org.h2.Driver")//$NON-NLS-1$
    //
    ;

    private EDatabaseTypeName dbType;

    private String[] usingDriverClasses;

    private String[] deprecatedDriverClasses;

    EDatabase4DriverClassName(EDatabaseTypeName dbType, String usingDriverClasses[], String deprecatedDriverClasses[]) {
        this.dbType = dbType;
        this.usingDriverClasses = usingDriverClasses;
        this.deprecatedDriverClasses = deprecatedDriverClasses;
    }

    EDatabase4DriverClassName(EDatabaseTypeName dbType, String driverClass, String deprecatedDriverClass) {
        this(dbType, new String[] { driverClass }, new String[] { deprecatedDriverClass });
    }

    EDatabase4DriverClassName(EDatabaseTypeName dbType, String driverClass) {
        this(dbType, new String[] { driverClass }, new String[0]);
    }

    public EDatabaseTypeName getDbType() {
        return this.dbType;
    }

    public String getDbTypeName() {
        return this.dbType.getXmlName();
    }

    public String getDriverClass() {
        if (usingDriverClasses != null && usingDriverClasses.length > 0) {
            return usingDriverClasses[0]; // use the first one by default
        }
        return null;
    }

    public String[] getDriverClasses() {
        return this.usingDriverClasses;
    }

    public String[] getDeprecatedDriverClasses() {
        return this.deprecatedDriverClasses;
    }

    public static EDatabase4DriverClassName indexOfByDbType(String dbType) {
        if (dbType != null) {
            EDatabaseTypeName db = EDatabaseTypeName.getTypeFromDbType(dbType);
            if (db != null) {
                for (EDatabase4DriverClassName t4d : EDatabase4DriverClassName.values()) {
                    if (t4d.getDbType() == db) {
                        return t4d;
                    }
                }
            }
        }
        return null;
    }

    public static List<EDatabase4DriverClassName> indexOfByDriverClass(String driverClass) {
        List<EDatabase4DriverClassName> dbType4Drivers = new ArrayList<EDatabase4DriverClassName>();
        for (EDatabase4DriverClassName t4d : EDatabase4DriverClassName.values()) {
            String[] drivers = t4d.getDriverClasses();
            if (drivers != null) {
                for (String driver : drivers) {
                    if (driver.equals(driverClass)) {
                        dbType4Drivers.add(t4d);
                        break;
                    }
                }
            }
            // from deprecated drivers
            drivers = t4d.getDeprecatedDriverClasses();
            if (drivers != null && !dbType4Drivers.contains(t4d)) {
                for (String driver : drivers) {
                    if (driver.equals(driverClass)) {
                        dbType4Drivers.add(t4d);
                        break;
                    }
                }
            }
        }
        return dbType4Drivers;
    }

    public static String getDriverClassByDbType(String dbType) {
        EDatabase4DriverClassName t4d = indexOfByDbType(dbType);
        if (t4d != null) {
            return t4d.getDriverClass();
        }
        return null;
    }

}
