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

import org.talend.commons.runtime.xml.XmlUtil;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public final class DatabaseConstant {

    private DatabaseConstant() {
    }

    public static final String ODBC_ORACLE_PRODUCT_NAME = "oracle";

    public static final String MYSQL_PRODUCT_NAME = "mysql";

    public static final String ODBC_ORACLE_SCHEMA_NAME = "TABLE_OWNER";

    public static final String POSTGRESQL_PRODUCT_NAME = "postgresql";

    public static final String ODBC_POSTGRESQL_CATALOG_NAME = "TABLE_QUALIFIER";

    public static final String IBM_DB2_ZOS_PRODUCT_NAME = "DB2";

    public static final String MSSQL_DRIVER_NAME_JDBC2_0 = "Microsoft SQL Server JDBC Driver 2.0";

    public static final String ODBC_DRIVER_NAME = "jdbc-odbc";

    public static final String INGRES_PRODUCT_NAME = "ingres";

    public static final String JDBC_INGRES_DEIVER_NAME = "Ingres Corporation - JDBC Driver";

    // feature 0010630 zshen: Tables are not found when using Excel with ODBC connection
    public static final String ODBC_EXCEL_PRODUCT_NAME = "EXCEL";

    public static final String ODBC_MSSQL_PRODUCT_NAME = "Microsoft SQL Server";

    public static final String XML_EXIST_DRIVER_NAME = "org.exist.xmldb.DatabaseImpl";

    public static final String MDM_DBNAME = "talend/TalendPort";

    public static final String MDM_VERSION = "1.0.0";

    public static final String XSD_SUFIX = XmlUtil.FILE_XSD_SUFFIX;

    public static final String ODBC_INGRES_DRIVER_NAME = "JDBC-ODBC Bridge (CAIIOD";

    public static final String ODBC_PROGRESS_PRODUCT_NAME = "progress";

    public static final String ODBC_TERADATA_PRODUCT_NAME = "teradata";

    public static final String MS_ACCESS_PRODUCT_NAME = "ACCESS";
}
