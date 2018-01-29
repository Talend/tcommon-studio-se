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
package org.talend.utils.database;

/**
 * DOC wuwei class global comment. Detailled comment
 */
public class Constants {

    public static final String JDBC_MYSQL_URL = "jdbc:mysql://";

    public static final String JDBC_MSSQL_URL = "jdbc:jtds:sqlserver://";

    public static final String JDBC_ORACLE_URL = "jdbc:oracle:thin:";

    public static final String JDBC_H2_URL = "jdbc:h2";

    public static final String JDBC_POSTGRESQL_URL = "jdbc:postgresql";

    public static final String JDBC_MYSQL_DRIVER = "org.gjt.mm.mysql.Driver";

    public static final String JDBC_MSSQL_DRIVER = "net.sourceforge.jtds.jdbc.Driver";

    public static final String JDBC_ORACLE_DRIVER = "oracle.jdbc.OracleDriver";

    public static final String JDBC_H2_DRIVER = "org.h2.Driver";

    public static final String JDBC_POSTGRESQL_DRIVER = "org.postgresql.Driver";

    public static final String JDBC_MARIADB_URL = "jdbc:mariadb://";

    public static final String JDBC_MARIADB_DRIVER = "org.mariadb.jdbc.Driver";

    public static String getDriverFromUrl(String url) {
        if (url.startsWith(JDBC_MYSQL_URL)) {
            return JDBC_MYSQL_DRIVER;
        }
        if (url.startsWith(JDBC_MSSQL_URL)) {
            return JDBC_MSSQL_DRIVER;
        }
        if (url.startsWith(JDBC_ORACLE_URL)) {
            return JDBC_ORACLE_DRIVER;
        }
        if (url.startsWith(JDBC_H2_URL)) {
            return JDBC_H2_DRIVER;
        }
        if (url.startsWith(JDBC_POSTGRESQL_URL)) {
            return JDBC_POSTGRESQL_DRIVER;
        }
        if (url.startsWith(JDBC_MARIADB_URL)) {
            return JDBC_MARIADB_DRIVER;
        }
        throw new IllegalStateException("unknown url type");
    }

}
