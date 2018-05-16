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
package org.talend.commons.utils.database;

import java.sql.SQLException;
import java.sql.Types;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class EmbeddedHiveResultSet extends AbstractFakeResultSet {

    /**
     * DOC ggu EmbeddedHiveResultSet constructor comment.
     */
    public EmbeddedHiveResultSet() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.utils.database.AbstractFakeResultSet#getInt(java.lang.String)
     */
    @Override
    public int getInt(String columnLabel) throws SQLException {
        if ("DATA_TYPE".equals(columnLabel)) {
            String sqlType = getString("TYPE_NAME");
            if (sqlType.trim().equalsIgnoreCase("TINYINT") || sqlType.trim().equalsIgnoreCase("SMALLINT")) { //$NON-NLS-1$
                return Types.SMALLINT;
            } else if (sqlType.trim().equalsIgnoreCase("INT")) { //$NON-NLS-1$
                return Types.INTEGER;
            } else if (sqlType.trim().equalsIgnoreCase("BIGINT")) { //$NON-NLS-1$ 
                return Types.BIGINT;
            } else if (sqlType.trim().equalsIgnoreCase("BOOLEAN")) { //$NON-NLS-1$
                return Types.BOOLEAN;
            } else if (sqlType.trim().equalsIgnoreCase("FLOAT")) { //$NON-NLS-1$ 
                return Types.FLOAT;
            } else if (sqlType.trim().equalsIgnoreCase("DOUBLE")) { //$NON-NLS-1$ 
                return Types.DOUBLE;
            } else if (sqlType.trim().equalsIgnoreCase("STRING")) { //$NON-NLS-1$
                return Types.VARCHAR;
            } else if (sqlType.trim().equalsIgnoreCase("BINARY")) { //$NON-NLS-1$ 
                return Types.BINARY;
            } else if (sqlType.trim().equalsIgnoreCase("TIMESTAMP")) { //$NON-NLS-1$
                return Types.TIMESTAMP;
            }
        }
        // TODO Auto-generated method stub
        return super.getInt(columnLabel);
    }

}
