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
package org.talend.core.model.repository;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.exception.ExceptionHandler;

/**
 * DOC guanglong.du class global comment. Detailled comment
 */
public class RepositoryContentManager {

    static List<IRepositoryContentHandler> handlers = null;

    static List<IExtendedRepositoryNodeHandler> extendedNodeHandler = null;

    static List<IRepositoryReviewFilter> repositoryReviewFilter = null;

    public static List<IRepositoryContentHandler> getHandlers() {
        if (handlers == null) {
            handlers = new ArrayList<IRepositoryContentHandler>();
            IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
            IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint("org.talend.core.repository.repository_content"); //$NON-NLS-1$
            if (extensionPoint != null) {
                IExtension[] extensions = extensionPoint.getExtensions();
                for (IExtension extension : extensions) {
                    IConfigurationElement[] configurationElements = extension.getConfigurationElements();
                    for (IConfigurationElement configurationElement : configurationElements) {
                        try {
                            Object service = configurationElement.createExecutableExtension("class"); //$NON-NLS-1$
                            if (service instanceof IRepositoryContentHandler) {
                                handlers.add((IRepositoryContentHandler) service);
                            }
                        } catch (CoreException e) {
                            ExceptionHandler.process(e);
                        }
                    }
                }
            }
        }
        return handlers;
    }

    /**
     * Getter for extendedNodeHandler.
     * 
     * @return the extendedNodeHandler
     */
    public static List<IExtendedRepositoryNodeHandler> getExtendedNodeHandler() {
        if (extendedNodeHandler == null) {
            synchronized (RepositoryContentManager.class) {
                extendedNodeHandler = new ArrayList<IExtendedRepositoryNodeHandler>();
                IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
                IExtensionPoint extensionPoint = extensionRegistry
                        .getExtensionPoint("org.talend.core.repository.extended_repositorynode_handler"); //$NON-NLS-1$
                if (extensionPoint != null) {
                    IExtension[] extensions = extensionPoint.getExtensions();
                    for (IExtension extension : extensions) {
                        IConfigurationElement[] configurationElements = extension.getConfigurationElements();
                        for (IConfigurationElement configurationElement : configurationElements) {
                            try {
                                Object service = configurationElement.createExecutableExtension("class"); //$NON-NLS-1$
                                if (service instanceof IExtendedRepositoryNodeHandler) {
                                    extendedNodeHandler.add((IExtendedRepositoryNodeHandler) service);
                                }
                            } catch (CoreException e) {
                                ExceptionHandler.process(e);
                            }
                        }
                    }
                }
            }
        }

        return extendedNodeHandler;
    }

    /**
     * Getter for extendedNodeHandler.
     * 
     * @return the extendedNodeHandler
     */
    public static List<IRepositoryReviewFilter> getRepositoryReviewFilters() {
        if (repositoryReviewFilter == null) {
            synchronized (RepositoryContentManager.class) {
                repositoryReviewFilter = new ArrayList<IRepositoryReviewFilter>();
                IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
                IExtensionPoint extensionPoint = extensionRegistry
                        .getExtensionPoint("org.talend.core.repository.repository_review_filter"); //$NON-NLS-1$
                if (extensionPoint != null) {
                    IExtension[] extensions = extensionPoint.getExtensions();
                    for (IExtension extension : extensions) {
                        IConfigurationElement[] configurationElements = extension.getConfigurationElements();
                        for (IConfigurationElement configurationElement : configurationElements) {
                            try {
                                Object service = configurationElement.createExecutableExtension("class"); //$NON-NLS-1$
                                if (service instanceof IRepositoryReviewFilter) {
                                    repositoryReviewFilter.add((IRepositoryReviewFilter) service);
                                }
                            } catch (CoreException e) {
                                ExceptionHandler.process(e);
                            }
                        }
                    }
                }
            }
        }

        return repositoryReviewFilter;
    }
}
