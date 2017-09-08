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
package org.talend.core.database.conn.template;

import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.database.conn.version.EDatabaseVersion4Drivers;

/**
 * @author hWang
 * @version 1.0 jdk1.6
 * @date Aug 8, 2016
 */
public class DbConnStrForMSSQL extends DbConnStr {

    private static final String URL_MSSQL = "jdbc:jtds:sqlserver://<host>:<port>/<sid>;<property>"; //$NON-NLS-1$

    public static final String URL_MSSQL_2012 = "jdbc:jtds:sqlserver://<host>:<port>/<sid>;<property>";//$NON-NLS-1$

    private static final String URL_MSSQL_PROP = "jdbc:sqlserver://<host>:<port>;DatabaseName=<sid>;<property>";//$NON-NLS-1$

    /**
     * DOC Marvin DbConnStrForHive constructor comment.
     * 
     * @param dbType
     * @param urlTemplate
     */
    DbConnStrForMSSQL(EDatabaseTypeName dbType, String urlTemplate) {
        super(dbType, urlTemplate);
    }

    public DbConnStrForMSSQL(EDatabaseTypeName dbType, String urlTemplate,String port, EDatabaseVersion4Drivers[] hiveModes) {
        super(dbType, urlTemplate,port, hiveModes);
    }

    @Override
    String getUrlTemplate(EDatabaseVersion4Drivers version) {
        if (version == null) {
            return super.getUrlTemplate(version);
        }
        switch (version) {
        case MSSQL:
            return URL_MSSQL;
        case MSSQL_2012:
            return URL_MSSQL_2012;
        case MSSQL_PROP:
            return URL_MSSQL_PROP;
        default:
            return super.getUrlTemplate(version);
        }
    }

    @Override
    String getUrlPattern(EDatabaseVersion4Drivers version) {
        switch (version) {
        case MSSQL:
            return calcPattern(URL_MSSQL);
        case MSSQL_2012:
            return calcPattern(URL_MSSQL_2012);
        case MSSQL_PROP:
            return calcPattern(URL_MSSQL_PROP);
        default:
            return super.getUrlPattern(version);
        }
    }
}
