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
package org.talend.repository.example.viewer.node;

import org.talend.core.model.repository.ERepositoryObjectType;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ExampleDemoRepositoryNodeType {

    /**
     * this value is same the attribute "type" of extension point "org.talend.core.repository.repository_node_provider"
     */
    public static final String EXAMPLE_DEMO = "EXAMPLE_DEMO"; //$NON-NLS-1$

    public static ERepositoryObjectType repositoryExampleDemoType = ERepositoryObjectType.valueOf(ERepositoryObjectType.class,
            EXAMPLE_DEMO);

    public static final String EXTENDED_EXAMPLE_DEMO = "EXTENDED_EXAMPLE_DEMO"; //$NON-NLS-1$

    public static ERepositoryObjectType repositoryExtendedExampleDemoType = ERepositoryObjectType.valueOf(
            ERepositoryObjectType.class, EXTENDED_EXAMPLE_DEMO);

}
