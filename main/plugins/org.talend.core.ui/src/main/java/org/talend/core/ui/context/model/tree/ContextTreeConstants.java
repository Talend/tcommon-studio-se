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
package org.talend.core.ui.context.model.tree;

import org.talend.core.ui.i18n.Messages;

/**
 * cli class global comment. Detailled comment
 */
public class ContextTreeConstants {

    public static final int VARIABLE_COLUMN_INDEX = 4;

    public static final String PRESENTATION_TYPE_KEY = "PRESENTATION_TYPE_KEY"; //$NON-NLS-1$

    /*
     * columns label
     */
    public static String getVariableColumnName() {
        return Messages.getString("ContextTreeConstants.variableName"); //$NON-NLS-1$
    }

    public static String getContextColumnName() {
        return Messages.getString("ContextTreeConstants.contextName"); //$NON-NLS-1$
    }

    /*
     * column properties
     */

    public static final String VARIABLE_COLUMN_PROPERTY = "VARIABLE"; //$NON-NLS-1$

    public static final String CONTEXT_COLUMN_PROPERTY = "CONTEXT"; //$NON-NLS-1$

    public static final String PROMPTNEEDED_COLUMN_PROPERTY = "PROMPTNEEDED"; //$NON-NLS-1$

    public static final String PROMPT_COLUMN_PROPERTY = "PROMPT"; //$NON-NLS-1$

    public static final String VALUE_COLUMN_PROPERTY = "VALUE"; //$NON-NLS-1$

    public static final String[] GROUP_BY_VARIABLE_COLUMN_PROPERTIES = new String[] { VARIABLE_COLUMN_PROPERTY,
            CONTEXT_COLUMN_PROPERTY, PROMPTNEEDED_COLUMN_PROPERTY, PROMPT_COLUMN_PROPERTY, VALUE_COLUMN_PROPERTY };

    public static final String[] GROUP_BY_CONTEXT_COLUMN_PROPERTIES = new String[] { CONTEXT_COLUMN_PROPERTY,
            VARIABLE_COLUMN_PROPERTY, PROMPTNEEDED_COLUMN_PROPERTY, PROMPT_COLUMN_PROPERTY, VALUE_COLUMN_PROPERTY };

}
