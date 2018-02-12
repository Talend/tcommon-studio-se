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
package org.talend.core.nexus;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.exception.ExceptionHandler;

/**
 * created by wchen on Aug 3, 2017 Detailled comment
 *
 */
public class RepositoryArtifactHandlerManager {

    private static Map<String, IRepositoryArtifactHandler> handlers = null;

    private synchronized static void initHandlers() {
        if (handlers == null) {
            handlers = new HashMap<String, IRepositoryArtifactHandler>();
            IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
            IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint("org.talend.core.runtime.artifact_handler"); //$NON-NLS-1$
            if (extensionPoint != null) {
                IExtension[] extensions = extensionPoint.getExtensions();
                for (IExtension extension : extensions) {
                    IConfigurationElement[] configurationElements = extension.getConfigurationElements();
                    for (IConfigurationElement configurationElement : configurationElements) {
                        try {
                            String type = configurationElement.getAttribute("type");
                            Object object = configurationElement.createExecutableExtension("class"); //$NON-NLS-1$
                            if (object instanceof IRepositoryArtifactHandler) {
                                IRepositoryArtifactHandler handler = (IRepositoryArtifactHandler) object;
                                handlers.put(type, handler);
                            }
                        } catch (CoreException e) {
                            ExceptionHandler.process(e);
                        }
                    }
                }
            }
        }
    }

    public static IRepositoryArtifactHandler getRepositoryHandler(NexusServerBean serverBean) {
        initHandlers();
        if (serverBean != null) {
            IRepositoryArtifactHandler repHandler = handlers.get(serverBean.getType());
            if (repHandler != null) {
                repHandler = repHandler.clone();
                repHandler.setArtifactServerBean(serverBean);
                return repHandler;
            }
        }
        return null;
    }

}
