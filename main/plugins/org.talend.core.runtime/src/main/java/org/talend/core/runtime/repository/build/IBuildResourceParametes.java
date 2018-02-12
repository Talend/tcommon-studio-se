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
package org.talend.core.runtime.repository.build;

/**
 * DOC ggu class global comment. Detailled comment
 */
public interface IBuildResourceParametes {

    static final String EXPORT_OPTION = "option"; //$NON-NLS-1$

    static final String OPTION_ITEMS = EXPORT_OPTION + "_export_items"; //$NON-NLS-1$

    static final String OPTION_ITEMS_DEPENDENCIES = OPTION_ITEMS + "_dependencies"; //$NON-NLS-1$

    static final String EXPORT_OBJECT = "obj"; //$NON-NLS-1$

    static final String OBJ_PROCESS_ITEM = EXPORT_OBJECT + "_process_Item"; //$NON-NLS-1$

    static final String OBJ_PROCESS_JAVA_PROJECT = EXPORT_OBJECT + "_process_java_project"; //$NON-NLS-1$

    static final String OBJ_ITEM_DEPENDENCIES = EXPORT_OBJECT + "_item_dependencies"; //$NON-NLS-1$
}
