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
package org.talend.core.repository.model.provider;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.repository.services.IGitContentService;

public class GitContentServiceProviderManager {

    public static IGitContentService getGitContentService() {
        IGitContentService service = null;
        IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
        IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint("org.talend.core.repository.git_content_service"); //$NON-NLS-1$
        if (extensionPoint != null) {
            IExtension[] extensions = extensionPoint.getExtensions();
            for (IExtension extension : extensions) {
                IConfigurationElement[] configurationElements = extension.getConfigurationElements();
                for (IConfigurationElement configurationElement : configurationElements) {
                    try {
                        Object object = configurationElement.createExecutableExtension("class"); //$NON-NLS-1$
                        if (object instanceof IGitContentService) {
                            service = (IGitContentService) object;
                            break;
                        }
                    } catch (CoreException e) {
                        ExceptionHandler.process(e);
                    }
                }
            }
        }
        return service;
    }
}
