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
public class DatabaseConnConstants {

    public static final String EMPTY = ""; //$NON-NLS-1$

    /*
     * patterns
     */
    public static final String PATTERN_HOST = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.|[\\w\\.\\-_]{0,})"; //$NON-NLS-1$

    public static final String PATTERN_PORT = "(\\d{0,5})"; //$NON-NLS-1$

    public static final String PATTERN_WORD = "([\\w\\.\\-_]{0,})"; //$NON-NLS-1$

    public static final String PATTERN_SID = "([\\w\\.\\-_]{0,})"; //$NON-NLS-1$

    public static final String PATTERN_FILE = "([\\:\\/\\w\\.\\-_]{0,})"; //$NON-NLS-1$

    public static final String PATTERN_FILEMDB = "([\\w\\.\\-_]{0,}).mdb"; //$NON-NLS-1$

    public static final String PATTERN_ADDPARAM = "([\\w\\.\\-_=\\&*]{0,})"; //$NON-NLS-1$

    public static final String PATTERN_SCHEMA = PATTERN_SID;

    //
    public static final String PATTERN_VAR = "<[a-zA-Z_]+>"; //$NON-NLS-1$

}
