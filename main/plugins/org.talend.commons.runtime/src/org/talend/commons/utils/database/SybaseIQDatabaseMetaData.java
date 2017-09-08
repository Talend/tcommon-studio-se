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
package org.talend.commons.utils.database;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * created by qiongli on 2013-11-13 Detailled comment
 * 
 */
public class SybaseIQDatabaseMetaData extends SybaseDatabaseMetaData {

    /**
     * DOC qiongli SybaseIQDatabaseMetaData constructor comment.
     * 
     * @param connection
     * @throws SQLException
     */
    public SybaseIQDatabaseMetaData(Connection connection) throws SQLException {
        super(connection);
    }

    /*
     * Override this method,because sybase iq doesn't support sysalternates.
     */
    @Override
    protected String createSqlByLoginAndCatalog(String loginName, String catalogName) {
        return "select count(*) from " + catalogName //$NON-NLS-1$
                + ".dbo.sysusers where suid in (select suid from master.dbo.syslogins where name = '" + loginName + "')"; //$NON-NLS-1$ //$NON-NLS-2$ 

    }

}
