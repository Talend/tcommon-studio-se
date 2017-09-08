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
package org.talend.core.ui.context.model.template;

import org.talend.core.model.metadata.types.JavaTypesManager;

/**
 * ggu class global comment. Detailled comment
 */
public class ContextConstant {

    /*
     * 
     */
    public static final String DOWNWARDS_STRING = "|"; //$NON-NLS-1$

    public static final String SPLIT_CHAR = "\\|"; //$NON-NLS-1$

    public static final String NULL_STRING = "null"; //$NON-NLS-1$

    public static final String LINE_STRING = "-"; //$NON-NLS-1$

    public static final String SPACE_CHAR = " "; //$NON-NLS-1$

    public static final String REF_CHAR = "@"; //$NON-NLS-1$

    /*
     * java type
     */
    private static final String[] ITEMS = new String[] { "boolean | Boolean", "char | Character", //$NON-NLS-1$ //$NON-NLS-2$
            "Date", "double | Double", //$NON-NLS-1$ //$NON-NLS-2$
            "float | Float", "int | Integer", "long | Long", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            "short | Short", "String", //$NON-NLS-1$ //$NON-NLS-2$
            "BigDecimal", "Object", JavaTypesManager.FILE.getLabel(), //$NON-NLS-1$ //$NON-NLS-2$
            JavaTypesManager.DIRECTORY.getLabel(), JavaTypesManager.VALUE_LIST.getLabel(), JavaTypesManager.PASSWORD.getLabel() };

    /*
     * columns
     */
    public static final String NAME_COLUMN_NAME = "NAME"; //$NON-NLS-1$

    public static final String SOURCE_COLUMN_NAME = "SOURCE"; //$NON-NLS-1$

    public static final String TYPE_COLUMN_NAME = "TYPE"; //$NON-NLS-1$

    public static final String SCRIPTCODE_COLUMN_NAME = "SCRIPT_CODE"; //$NON-NLS-1$

    public static final String COMMENT_COLUMN_NAME = "COMMENT"; //$NON-NLS-1$

    public static final String CONNECTION_FIELD_NAME = "FIELD"; //$NON-NLS-1$

    public static final String VARIABLE_COLUMN_NAME = "VARIABLE"; //$NON-NLS-1$

    public static final String CUSTOM_VARIABLE_NAME = "NEW_VARIABLE_NAME"; //$NON-NLS-1$

    public static final String VARIABLE_COLUMN_VALUE = "VALUE"; //$NON-NLS-1$

    public static final String NEW_VARIABLE = "<New Variable>"; //$NON-NLS-1$

    /*
     * column properties
     */
    public static final String[] GROUP_BY_SOURCE_COLUMN_PROPERTIES = new String[] { NAME_COLUMN_NAME, SOURCE_COLUMN_NAME,
            TYPE_COLUMN_NAME, SCRIPTCODE_COLUMN_NAME, COMMENT_COLUMN_NAME };

    public static final String[] GROUP_BY_REPOSITORYSOURCE_COLUMN_PROPERTIES = new String[] { NAME_COLUMN_NAME, TYPE_COLUMN_NAME,
            SCRIPTCODE_COLUMN_NAME, COMMENT_COLUMN_NAME };
}
