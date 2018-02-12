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
package org.talend.core.model.repository;

import java.util.List;

import org.talend.commons.exception.IllegalPluginConfigurationException;
import org.talend.commons.utils.workbench.extensions.ExtensionImplementationProvider;
import org.talend.core.model.process.IExternalNode;
import org.talend.core.model.repository.extension.ExtensionPointFactory;

/**
 * Provides, using extension points, implementation of many factories.
 * 
 * <ul>
 * <li>IProcessFactory</li>
 * </ul>
 * 
 * $Id: ExternalNodesFactory.java 1 2006-09-29 17:06:40 +0000 (星期五, 29 九月 2006) nrousseau $
 */
public class ExternalNodesFactory {

    public static IExternalNode getInstance(final String extensionId) {
        List<IExternalNode> listComponents;
        try {
            listComponents = ExtensionImplementationProvider.getInstance(ExtensionPointFactory.EXTERNAL_COMPONENT, extensionId);
        } catch (IllegalPluginConfigurationException e) {
            throw new RuntimeException("plugin:" + extensionId + " not found", e); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return listComponents.get(0);
    }
}
