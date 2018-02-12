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
import org.talend.core.database.conn.EDatabaseConnVar;

/**
 * cli class global comment. Detailled comment
 */
public class DbConnStrForGeneralJDBC extends DbConnStr {

    DbConnStrForGeneralJDBC(EDatabaseTypeName dbType, String urlTemplate, String defaultPort) {
        super(dbType, urlTemplate, defaultPort);
    }

    @Override
    protected String replaceVarToPattern(String url) {

        url = url.replaceFirst(EDatabaseConnVar.XXX.getVariable(), EDatabaseConnVar.HOST.getPattern());
        url = url.replaceAll(EDatabaseConnVar.XXX.getVariable(), EDatabaseConnVar.PORT.getPattern());

        return url;
    }

}
