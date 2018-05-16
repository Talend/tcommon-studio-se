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
package org.talend.core.database.conn.template;

import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.database.conn.version.EDatabaseVersion4Drivers;

/**
 * @author Marvin Wang
 * @version 1.0 jdk1.6
 * @date Aug 8, 2012
 */
public class DbConnStrForHive extends DbConnStr {

    private static final String URL_HIVE_1_STANDALONE = "jdbc:hive://<host>:<port>/<sid>"; //$NON-NLS-1$

    public static final String URL_HIVE_1_TEMPLATE = "jdbc:hive://";//$NON-NLS-1$

    private static final String URL_HIVE_2_STANDALONE = "jdbc:hive2://<host>:<port>/<sid>";//$NON-NLS-1$

    public static final String URL_HIVE_2_TEMPLATE = "jdbc:hive2://";//$NON-NLS-1$

    /**
     * DOC Marvin DbConnStrForHive constructor comment.
     * 
     * @param dbType
     * @param urlTemplate
     */
    DbConnStrForHive(EDatabaseTypeName dbType, String urlTemplate) {
        super(dbType, urlTemplate);
    }

    public DbConnStrForHive(EDatabaseTypeName dbType, String urlTemplate, EDatabaseVersion4Drivers[] hiveModes) {
        super(dbType, urlTemplate, null, null, hiveModes);
    }
    
    public DbConnStrForHive(EDatabaseTypeName dbType, String urlTemplate, EDatabaseVersion4Drivers[] hiveModes, String defaultServer, String defaultDB) {
        super(dbType, urlTemplate, null, null, hiveModes, defaultServer, defaultDB);
    }

    @Override
    String getUrlTemplate(EDatabaseVersion4Drivers version) {
        if (version == null) {
            return super.getUrlTemplate(version);
        }
        switch (version) {
        case HIVE:
            return URL_HIVE_1_STANDALONE;
        case HIVE_EMBEDDED:
            return URL_HIVE_1_TEMPLATE;
        case HIVE_2_EMBEDDED:
            return URL_HIVE_2_TEMPLATE;
        case HIVE_2_STANDALONE:
            return URL_HIVE_2_STANDALONE;
        default:
            return super.getUrlTemplate(version);
        }
    }

    @Override
    String getUrlPattern(EDatabaseVersion4Drivers version) {
        switch (version) {
        case HIVE:
            return calcPattern(URL_HIVE_1_STANDALONE);
        case HIVE_EMBEDDED:
            return calcPattern(URL_HIVE_1_TEMPLATE);
        case HIVE_2_EMBEDDED:
            return calcPattern(URL_HIVE_2_TEMPLATE);
        case HIVE_2_STANDALONE:
            return calcPattern(URL_HIVE_2_STANDALONE);
        default:
            return super.getUrlPattern(version);
        }
    }
}
