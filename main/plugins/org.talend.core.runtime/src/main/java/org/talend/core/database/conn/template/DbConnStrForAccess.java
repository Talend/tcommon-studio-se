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
import org.talend.core.database.conn.DatabaseConnConstants;
import org.talend.core.database.conn.version.EDatabaseVersion4Drivers;

/**
 * cli class global comment. Detailled comment
 */
public class DbConnStrForAccess extends DbConnStr {

    private static final String ACCDB_PATTERN = ",\\s\\*\\.accdb"; //$NON-NLS-1$

    DbConnStrForAccess(EDatabaseTypeName dbType, String urlTemplate, EDatabaseVersion4Drivers[] dbVersions) {
        super(dbType, urlTemplate, null, null, dbVersions);
    }

    @Override
    String getUrlPattern(EDatabaseVersion4Drivers version) {
        if (version == EDatabaseVersion4Drivers.ACCESS_2003) {
            return this.urlPattern.replaceFirst(ACCDB_PATTERN, DatabaseConnConstants.EMPTY);
        }
        return super.getUrlPattern(version);
    }

    @Override
    String getUrlTemplate(EDatabaseVersion4Drivers version) {
        if (version == EDatabaseVersion4Drivers.ACCESS_2003) {
            return this.urlTemplate.replaceFirst(ACCDB_PATTERN, DatabaseConnConstants.EMPTY);
        }
        return super.getUrlTemplate(version);
    }

    @Override
    String processStr(EDatabaseVersion4Drivers version, String str) {
        if (str != null) {
            if (version == EDatabaseVersion4Drivers.ACCESS_2003) {
                return str.replaceFirst(ACCDB_PATTERN, DatabaseConnConstants.EMPTY);
            }
        }
        return super.processStr(version, str);
    }

}
