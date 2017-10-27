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

/**
 * This class store all the constant of current plugin.
 * 
 */
public final class PluginConstant {

    private PluginConstant() {
    }

    public static final String EMPTY_STRING = ""; //$NON-NLS-1$

    public static final String HOSTNAME_PROPERTY = "hostname"; //$NON-NLS-1$

    public static final String PORT_PROPERTY = "port"; //$NON-NLS-1$

    public static final String DBTYPE_PROPERTY = "dbtype"; //$NON-NLS-1$

    // MOD mzhao 2009-05-11 bug:7280, Default charset UTF-8
    public static final String DEFAULT_PARAMETERS = "zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&characterEncoding=UTF-8"; //$NON-NLS-1$

    public static final String CONNECTION_TIMEOUT = "CONNECTION_TIMEOUT"; //$NON-NLS-1$

    public static final String FILTER_TABLE_VIEW_COLUMN = "FILTER_TABLE_VIEW_COLUMN"; //$NON-NLS-1$

    public static final String MDM_PATH = "metadata/MDMconnections";//$NON-NLS-1$

    public static final String CONN_PATH = "metadata/connections";//$NON-NLS-1$

}
