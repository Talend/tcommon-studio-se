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
package org.talend.core.model.process;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.process.IProcess2;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public final class ReplaceNodesInProcessProvider {

    public static final String EXTENSION_ID = "org.talend.designer.core.replace_nodes"; //$NON-NLS-1$

    public static final String ATTR_CLASS = "class"; //$NON-NLS-1$

    private static List<IReplaceNodeInProcess> providers = new ArrayList<IReplaceNodeInProcess>();

    public static List<IReplaceNodeInProcess> findReplaceNodesProvider() {
        if (providers.isEmpty()) {
            IConfigurationElement[] elems = Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSION_ID);
            for (IConfigurationElement elem : elems) {
                IReplaceNodeInProcess createExecutableExtension;
                try {
                    createExecutableExtension = (IReplaceNodeInProcess) elem.createExecutableExtension(ATTR_CLASS);
                    providers.add(createExecutableExtension);
                } catch (CoreException e) {
                    ExceptionHandler.process(e);
                }
            }
        }
        return providers;
    }

    public static boolean isNeedForceRebuild(IProcess2 process) {
        List<IReplaceNodeInProcess> providers = ReplaceNodesInProcessProvider.findReplaceNodesProvider();
        for (IReplaceNodeInProcess provider : providers) {
            if (provider.isNeedForceRebuild(process)) {
                return true;
            }
        }
        return false;
    }

    public static boolean beforeRunJobInGUI(IProcess2 process) {
        List<IReplaceNodeInProcess> providers = ReplaceNodesInProcessProvider.findReplaceNodesProvider();
        for (IReplaceNodeInProcess provider : providers) {
            provider.beforeRunJobInGUI(process);
        }
        return false;
    }
}
