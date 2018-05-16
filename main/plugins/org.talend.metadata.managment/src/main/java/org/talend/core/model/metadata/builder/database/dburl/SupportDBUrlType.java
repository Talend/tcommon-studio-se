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
package org.talend.core.model.metadata.builder.database.dburl;

import org.apache.commons.lang.StringUtils;

/**
 * DOC rli class global comment. Detailled comment <br/>
 * 
 */
public enum SupportDBUrlType {
    /**
     * @deprecated odbc is not supported in java8, better hide it
     */
    ODBCDEFAULTURL("Generic ODBC", //$NON-NLS-1$
            null,
            null,
            null,
            null,
            "sun.jdbc.odbc.JdbcOdbcDriver", //$NON-NLS-1$
            "datasourceName", "ODBC"), //$NON-NLS-1$ //$NON-NLS-2$
    MYSQLDEFAULTURL("MySQL", //$NON-NLS-1$
            "localhost", //$NON-NLS-1$
            "3306", //$NON-NLS-1$
            "dbname", //$NON-NLS-1$
            "?", //$NON-NLS-1$
            "org.gjt.mm.mysql.Driver", //$NON-NLS-1$
            null,
            "MySQL"), //$NON-NLS-1$
    ORACLEWITHSIDDEFAULTURL("Oracle with SID", //$NON-NLS-1$
            "localhost", //$NON-NLS-1$
            "1521", //$NON-NLS-1$
            "dbname", //$NON-NLS-1$
            null,
            // MOD scorreia 2008-08-22: oracle.jdbc.driver package is not supported anymore: replaced
            "oracle.jdbc.OracleDriver", //$NON-NLS-1$
            null,
            "Oracle"), //$NON-NLS-1$
    ORACLEWITHSERVICENAMEDEFAULTURL("Oracle with service name", //$NON-NLS-1$
            "localhost", //$NON-NLS-1$
            "1521", //$NON-NLS-1$
            "dbname", //$NON-NLS-1$
            null, // replaced
            "oracle.jdbc.OracleDriver", //$NON-NLS-1$
            null,
            "Oracle"), //$NON-NLS-1$
    ORACLEOCIDEFAULTURL("Oracle OCI", //$NON-NLS-1$
            "", //$NON-NLS-1$
            "", //$NON-NLS-1$
            "dbname", //$NON-NLS-1$
            null,
            "oracle.jdbc.OracleDriver", //$NON-NLS-1$
            null,
            "Oracle"), //$NON-NLS-1$
    ORACLECUSTOMDEFAULTURL("Oracle Custom", //$NON-NLS-1$
            "", //$NON-NLS-1$
            "", //$NON-NLS-1$
            "dbname", //$NON-NLS-1$
            null,
            "oracle.jdbc.OracleDriver", //$NON-NLS-1$
            null,
            "Oracle"), //$NON-NLS-1$
    MSSQLDEFAULTURL("Microsoft SQL Server", //$NON-NLS-1$
            "localhost", //$NON-NLS-1$
            "1433", //$NON-NLS-1$
            "dbname", //$NON-NLS-1$
            ";", //$NON-NLS-1$
            "net.sourceforge.jtds.jdbc.Driver", //$NON-NLS-1$
            null,
            "Microsoft SQL Server"), //$NON-NLS-1$
    MSSQL2008URL("Microsoft SQL Server 2005/2008", //$NON-NLS-1$
            "localhost", //$NON-NLS-1$
            "1433", //$NON-NLS-1$
            "dbname", //$NON-NLS-1$
            ";", //$NON-NLS-1$
            "com.microsoft.sqlserver.jdbc.SQLServerDriver", //$NON-NLS-1$
            null,
            "Microsoft SQL Server 2005/2008"), //$NON-NLS-1$
    DB2DEFAULTURL("IBM DB2", //$NON-NLS-1$
            "localhost", //$NON-NLS-1$
            "50000", //$NON-NLS-1$
            "dbname", //$NON-NLS-1$
            null,
            "com.ibm.db2.jcc.DB2Driver", //$NON-NLS-1$
            null,
            "DB2"), //$NON-NLS-1$
    DB2ZOSDEFAULTURL("IBM DB2 ZOS", //$NON-NLS-1$
            "localhost", //$NON-NLS-1$
            "557", //$NON-NLS-1$
            "dbname", //$NON-NLS-1$
            null,
            "COM.ibm.db2os390.sqlj.jdbc.DB2SQLJDriver", //$NON-NLS-1$
            null,
            "DB2"), //$NON-NLS-1$
    POSTGRESQLEFAULTURL("PostgreSQL", //$NON-NLS-1$
            "localhost", //$NON-NLS-1$
            "5432", //$NON-NLS-1$
            "dbname", //$NON-NLS-1$
            null,
            "org.postgresql.Driver", //$NON-NLS-1$
            null,
            "PostgreSQL"), //$NON-NLS-1$
    INTERBASEDEFAULTURL("Interbase", //$NON-NLS-1$
            "localhost", //$NON-NLS-1$
            "3060", //$NON-NLS-1$
            "dbname", //$NON-NLS-1$
            null,
            "interbase.interclient.Driver", //$NON-NLS-1$
            null,
            "Interbase"), //$NON-NLS-1$
    SYBASEDEFAULTURL("Sybase (ASE and IQ)", //$NON-NLS-1$
            "localhost", //$NON-NLS-1$
            "5007", //$NON-NLS-1$
            "dbname", //$NON-NLS-1$
            null,
            "com.sybase.jdbc3.jdbc.SybDriver", //$NON-NLS-1$
            null,
            "Adaptive Server Enterprise | Sybase Adaptive Server IQ"), //$NON-NLS-1$
    INFORMIXDEFAULTURL("Informix", //$NON-NLS-1$
            "localhost", //$NON-NLS-1$
            "1533", //$NON-NLS-1$
            "dbname", //$NON-NLS-1$
            ";", //$NON-NLS-1$
            "com.informix.jdbc.IfxDriver", //$NON-NLS-1$
            "myserver", "Informix"), //$NON-NLS-1$ //$NON-NLS-2$
    FIREBIRDDEFAULTURL("FireBird", //$NON-NLS-1$
            "localhost", //$NON-NLS-1$
            null,
            "dbname", //$NON-NLS-1$
            null,
            "org.firebirdsql.jdbc.FBDriver", //$NON-NLS-1$
            null,
            "FireBird"), //$NON-NLS-1$
    INGRESDEFAULTURL("Ingres", "localhost", "II7", "dbname", null, "com.ingres.jdbc.IngresDriver", null, "Ingres"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
    SQLITE3DEFAULTURL("SQLite", "localhost", "", "dbname", null, "org.sqlite.JDBC", null, "SQLite"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
    GENERICJDBCDEFAULTURL("General JDBC", "", "", "", null, "", null, "General JDBC"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
    // MOD klliu 2010-06-04 bug 12819: upgrade jdbc driver class used in sql explorer
    TERADATADEFAULTURL("Teradata", "localhost", null, "dbname", null, "com.teradata.jdbc.TeraDriver", null, "Teradata"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
    AS400DEFAULTURL("AS400", //$NON-NLS-1$
            "localhost", //$NON-NLS-1$
            null,
            "dbname", //$NON-NLS-1$
            null,
            "com.ibm.as400.access.AS400JDBCDriver", //$NON-NLS-1$
            null,
            "AS/400"), //$NON-NLS-1$
    // MOD klliu 2010-09-17 bug 14742 support Netezza database url as :jdbc:netezza://127.0.1.10:5480/talend
    NETEZZADEFAULTURL("Netezza", //$NON-NLS-1$
            "localhost", //$NON-NLS-1$
            "5480", //$NON-NLS-1$
            "dbname", //$NON-NLS-1$
            "?", //$NON-NLS-1$
            "org.netezza.Driver", //$NON-NLS-1$
            null,
            "Netezza"), //$NON-NLS-1$
    XML_eXist("eXist", //$NON-NLS-1$
            "localhost", //$NON-NLS-1$
            "8080", //$NON-NLS-1$
            "exist/xmlrpc/db", //$NON-NLS-1$
            null,
            "org.exist.xmldb.DatabaseImpl", //$NON-NLS-1$
            null,
            "eXist"), //$NON-NLS-1$
    MDM("MDM", //$NON-NLS-1$
            "localhost", //$NON-NLS-1$
            "8080", //$NON-NLS-1$
            "talend/TalendPort", //$NON-NLS-1$
            null,
            "", //$NON-NLS-1$
            null,
            "MDM"), //$NON-NLS-1$
    DELIMITEDFILE("DELIMITEDFILE", //$NON-NLS-1$
            "", //$NON-NLS-1$
            "", //$NON-NLS-1$
            "", //$NON-NLS-1$
            null,
            "", //$NON-NLS-1$
            null,
            "DELIMITEDFILE"), //$NON-NLS-1$
    JAVADEFAULTURL("Java", "localhost", null, "dbname", null, null, null, "Java"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    ACCESS("ACCESS", //$NON-NLS-1$
            null,
            null,
            null,
            null,
            "sun.jdbc.odbc.JdbcOdbcDriver", //$NON-NLS-1$
            "datasourceName", "Access"), //$NON-NLS-1$ //$NON-NLS-2$
    // MOD qiongli 2012-7-30 TDQ-5907 support Hive
    HIVEDEFAULTURL("Hive", //$NON-NLS-1$
            "localhost", //$NON-NLS-1$
            "10000", //$NON-NLS-1$
            null,
            null,
            "org.apache.hadoop.hive.jdbc.HiveDriver", //$NON-NLS-1$
            null,
            "Hive"), //$NON-NLS-1$
    IMPALA("Impala", //$NON-NLS-1$
            "localhost", //$NON-NLS-1$
            "10000", //$NON-NLS-1$
            null,
            null,
            "org.apache.hive.jdbc.HiveDriver", //$NON-NLS-1$
            null,
            "Impala"), //$NON-NLS-1$
    REDSHIFT("Redshift", //$NON-NLS-1$
            "localhost", //$NON-NLS-1$
            "5439", //$NON-NLS-1$
            null,
            null,
            "com.paraccel.Driver", //$NON-NLS-1$
            null,
            "Redshift"), //$NON-NLS-1$
    EXASOL("Exasol", //$NON-NLS-1$
            "localhost", //$NON-NLS-1$
            "8563", //$NON-NLS-1$
            null,
            null,
            "com.paraccel.Driver", //$NON-NLS-1$
            null,
            "EXASolution"), //$NON-NLS-1$
    VERTICA("Vertica", //$NON-NLS-1$
            null,
            "5433", //$NON-NLS-1$
            "dbname", //$NON-NLS-1$
            null,
            "com.vertica.Driver", //$NON-NLS-1$
            "", "Vertica Database"), //$NON-NLS-1$ //$NON-NLS-2$
    VERTICA2("Vertica", //$NON-NLS-1$
            null,
            "5433", //$NON-NLS-1$
            "dbname", //$NON-NLS-1$
            null,
            "com.vertica.jdbc.Driver", //$NON-NLS-1$
            "", "Vertica Database");//$NON-NLS-1$ //$NON-NLS-2$

    private final String dbKey;

    private final String hostName;

    private final String port;

    private String dbName;

    private final String paramSeprator;

    private final String dbDriver;

    private final String dataSource;

    private final String language;

    /**
     * SupportDBUrlType constructor.
     * 
     * @param dbKey the key identifying the connection string in dburl.properties file
     * @param hostName the hostname
     * @param port the port
     * @param dbName the database name
     * @param paramSeprator the seperator of parameters
     * @param dbDriver the driver
     * @param datasource the data source
     * @param language the specific language used by the database
     */
    SupportDBUrlType(String dbKey, String hostName, String port, String dbName, String paramSeprator, String dbDriver,
            String datasource, String language) {
        this.dbKey = dbKey;
        this.hostName = hostName;
        this.port = port;
        this.dbName = dbName;
        this.paramSeprator = paramSeprator;
        this.dbDriver = dbDriver;
        this.dataSource = datasource;
        this.language = language;
    }

    public String getHostName() {
        return hostName;
    }

    public String getPort() {
        return port;
    }

    public void setDBName(String dbName) {
        this.dbName = dbName;
    }

    public String getDBName() {
        return dbName;
    }

    public String getDBKey() {
        return dbKey;
    }

    /**
     * Getter for dbDriver.
     * 
     * @return the dbDriver
     */
    public String getDbDriver() {
        return dbDriver;
    }

    public String getDataSource() {
        return dataSource;
    }

    public String getParamSeprator() {
        return paramSeprator;
    }

    public String getLanguage() {
        return language;
    }

    /**
     * DOC bZhou Comment method "getDBTypeByKey".
     * 
     * @param dbKey
     * @return
     */
    public static SupportDBUrlType getDBTypeByKey(String dbKey) {
        assert dbKey != null;

        for (SupportDBUrlType type : values()) {
            if (StringUtils.equals(dbKey, type.getDBKey())) {
                return type;
            }
        }

        return null;
    }

    public static boolean isOracle(String dbKey) {
        SupportDBUrlType dbTypeByKey = getDBTypeByKey(dbKey);
        return dbTypeByKey != null && (dbTypeByKey == ORACLEWITHSIDDEFAULTURL || dbTypeByKey == ORACLEWITHSERVICENAMEDEFAULTURL);
    }

    /**
     * DOC msjian Comment method "isMssql".
     * 
     * @param dbKey
     * @return
     */
    public static boolean isMssql(String dbKey) {
        SupportDBUrlType dbTypeByKey = getDBTypeByKey(dbKey);
        return dbTypeByKey != null && (dbTypeByKey == MSSQLDEFAULTURL || dbTypeByKey == MSSQL2008URL);
    }

    /**
     * DOC msjian Comment method "hasCatalog". (just have catalog: mysql)
     * 
     * @param dbKey
     * @return
     */
    public static boolean justHaveCatalog(String dbKey) {
        SupportDBUrlType dbTypeByKey = getDBTypeByKey(dbKey);
        return dbTypeByKey != null && (dbTypeByKey == MYSQLDEFAULTURL);
    }

    /**
     * DOC msjian Comment method "hasSchema". (just have schema: oracle,teredata,ingres,DB2)
     * 
     * @param dbKey
     * @return
     */
    public static boolean justHaveSchema(String dbKey) {
        SupportDBUrlType dbTypeByKey = getDBTypeByKey(dbKey);
        return dbTypeByKey != null
                && (isOracle(dbKey) || dbTypeByKey == TERADATADEFAULTURL || dbTypeByKey == INGRESDEFAULTURL
                        || dbTypeByKey == DB2DEFAULTURL || dbTypeByKey == DB2ZOSDEFAULTURL);
    }
}
