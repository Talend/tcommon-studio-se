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
package org.talend.utils.database;

/**
 * DOC wuwei class global comment. Detailled comment
 */
public class DatabaseUrlCheck {

    public static boolean isH2DB(String url) {
        if (url.startsWith(Constants.JDBC_H2_URL)) {
            return true;
        }
        return false;
    }

    public static boolean isOracleDB(String url) {
        if (url.startsWith(Constants.JDBC_ORACLE_URL)) {
            return true;
        }
        return false;
    }

    public static boolean isMsDB(String url) {
        if (url.startsWith(Constants.JDBC_MSSQL_URL)) {
            return true;
        }
        return false;
    }

    public static boolean isMySQLDB(String url) {
        if (url.startsWith(Constants.JDBC_MYSQL_URL)) {
            return true;
        }
        return false;
    }

}
