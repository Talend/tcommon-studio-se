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
package org.talend.commons.runtime.model.components;

/**
 * DOC ggu class global comment. Detailled comment
 */
public interface IComponentConstants {

    public static final String UNIQUE_NAME = "UNIQUE_NAME"; //$NON-NLS-1$

    public static final String NORMAL = "normal"; //$NON-NLS-1$

    public static final String JOBLET_NAME_CHANGED = "joblet name changed"; //$NON-NLS-1$

    public static final String JOBLET_SCHEMA_CHANGED = "joblet schema changed"; //$NON-NLS-1$

    public static final String COMPONENT_PROPERTIES_TAG = "component.json.serialized"; //$NON-NLS-1$

    public static final String COMPONENT_SCHEMA_TAG = "component.property.schema"; //$NON-NLS-1$

    /**
     * Tag key is used by property to estimates if it supports context or not.
     */
    public static final String SUPPORT_CONTEXT = "SUPPORT_CONTEXT"; //$NON-NLS-1$

}
