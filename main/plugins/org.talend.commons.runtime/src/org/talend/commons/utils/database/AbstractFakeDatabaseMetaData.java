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

import org.talend.fakejdbc.FakeDatabaseMetaData;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public abstract class AbstractFakeDatabaseMetaData extends FakeDatabaseMetaData {

    protected static final String[] TABLE_META = { "ID", "TABLE_SCHEM", "TABLE_NAME", "TABLE_TYPE", "REMARKS" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

    protected static final String[] COLUMN_META = { "TABLE_NAME", "COLUMN_NAME", "TYPE_NAME", "COLUMN_SIZE", "DECIMAL_DIGITS", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
            "NUM_PREC_RADIX", "IS_NULLABLE", "REMARKS", "COLUMN_DEF" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    private Connection connection;

    public AbstractFakeDatabaseMetaData() {
        super();
    }

    /**
     * 
     * DOC ggu AbstractFakeDatabaseMetaData constructor comment.
     * 
     * @param connection
     */
    public AbstractFakeDatabaseMetaData(Connection connection) {
        this.connection = connection;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.utils.database.FakeDatabaseMetaData#getConnection()
     */
    @Override
    public Connection getConnection() throws SQLException {
        return connection;
    }
}
