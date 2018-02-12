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
package org.talend.commons.utils.workbench.extensions;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.talend.commons.exception.IllegalPluginConfigurationException;

/**
 * 
 * DOC amaumont ExtensionImplementationProvider class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40Z nrousseau $
 * 
 * @param <T>
 */
public class ClassExtensionImplementationProvider<T> extends ExtensionImplementationProvider<T> {

    /**
     * DOC amaumont DefaultExtensionImplementationProvider constructor comment.
     * @param plugInId 
     */
    public ClassExtensionImplementationProvider(IExtensionPointLimiter extensionPointLimiter, String plugInId) {
        super(extensionPointLimiter, plugInId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.utils.workbench.extensions.ExtensionImplementationProvider#createImplementation(org.eclipse.core.runtime.IExtension,
     * org.talend.commons.utils.workbench.extensions.IExtensionPointLimiter)
     */
    @Override
    protected T createImplementation(IExtension extension, IExtensionPointLimiter extensionPointLimiter,
            IConfigurationElement configurationElement) {
        try {
            return (T) configurationElement.createExecutableExtension("class"); //$NON-NLS-1$
        } catch (CoreException e) {
            throw new IllegalPluginConfigurationException(e);
        }
    }

}
