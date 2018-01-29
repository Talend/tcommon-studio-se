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
package org.talend.core.database.conn;

/**
 * cli class global comment. Detailled comment
 */
public enum EDatabaseConnVar {
    HOST("<host>", DatabaseConnConstants.PATTERN_HOST), //$NON-NLS-1$
    PORT("<port>", DatabaseConnConstants.PATTERN_PORT), //$NON-NLS-1$
    SID("<sid>", DatabaseConnConstants.PATTERN_SID), //$NON-NLS-1$
    PROPERTY("<property>", DatabaseConnConstants.PATTERN_ADDPARAM), //$NON-NLS-1$
    SERVICE_NAME("<service_name>", DatabaseConnConstants.PATTERN_SID), //$NON-NLS-1$
    DATASOURCE("<datasource>", DatabaseConnConstants.PATTERN_WORD), //$NON-NLS-1$
    FILENAME("<filename>", DatabaseConnConstants.PATTERN_FILE), //$NON-NLS-1$
    DBROOTPATH("<dbRootPath>", DatabaseConnConstants.PATTERN_WORD), //$NON-NLS-1$

    LOGIN("<login>"), //$NON-NLS-1$
    PASSWORD("<password>"), //$NON-NLS-1$
    XXX("<xxx>", "xxxx"), //$NON-NLS-1$ //$NON-NLS-2$
    //
    ;

    private String variable;

    private String pattern;

    EDatabaseConnVar(String variable) {
        this.variable = variable;
    }

    EDatabaseConnVar(String variable, String pattern) {
        assert (variable.matches(DatabaseConnConstants.PATTERN_VAR));
        this.variable = variable;
        this.pattern = pattern;
    }

    public String getVariable() {
        return this.variable;
    }

    public String getPattern() {
        return this.pattern;
    }

    public String getName() {
        return this.name();
    }
}
