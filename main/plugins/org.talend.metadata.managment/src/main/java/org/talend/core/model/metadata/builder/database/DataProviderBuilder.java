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
package org.talend.core.model.metadata.builder.database;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;

import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.cwm.helper.ConnectionHelper;

/**
 * DOC scorreia class global comment. Detailled comment
 * 
 * @deprecated this class does not seem to be used anymore. Remove it?
 */
public class DataProviderBuilder extends CwmBuilder {

    private final DatabaseConnection dataProvider;

    /**
     * DataProviderBuilder constructor.
     * 
     * @param conn the connection
     * @param driver the JDBC driver
     * @param databaseUrl the database connection string (must not be null)
     * @param driverProperties the properties passed to the driver (could be null)
     * @throws SQLException
     */
    public DataProviderBuilder(DatabaseConnection databaseConn, Connection conn, Driver driver, String databaseUrl)
            throws SQLException {
        super(databaseConn);
        dataProvider = databaseConn;

        // MOD xqliu 2009-07-13 bug 7888
        String identifierQuote = org.talend.utils.sql.ConnectionUtils.getConnectionMetadata(conn).getIdentifierQuoteString();
        // ~
        // MOD xqliu 2009-11-24 bug 7888
        ConnectionHelper.setIdentifierQuoteString(identifierQuote == null ? "" : identifierQuote, dataProvider);
        // ~
    }

}
