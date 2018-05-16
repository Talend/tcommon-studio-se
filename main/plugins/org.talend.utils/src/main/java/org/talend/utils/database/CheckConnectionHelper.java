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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DOC wuwei class global comment. Detailled comment
 */
public class CheckConnectionHelper {

    public static void connect(String login, String passwd, String url) throws InstantiationException, IllegalAccessException,
            ClassNotFoundException, SQLException {
        Class.forName(Constants.getDriverFromUrl(url)).newInstance();
        Connection connection = DriverManager.getConnection(url, login, passwd);
        connection.close();
    }

    public static Connection getConnection(String login, String passwd, String url) throws InstantiationException,
            IllegalAccessException, ClassNotFoundException, SQLException {
        Class.forName(Constants.getDriverFromUrl(url)).newInstance();
        return DriverManager.getConnection(url, login, passwd);
    }

}
