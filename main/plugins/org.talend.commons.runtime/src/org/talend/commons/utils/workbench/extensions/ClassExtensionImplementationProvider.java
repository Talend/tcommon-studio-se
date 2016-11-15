// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

public class ClassExtensionImplementationProvider<T> extends ExtensionImplementationProvider<T> {

    public ClassExtensionImplementationProvider(IExtensionPointLimiter extensionPointLimiter, String plugInId) {
        super(extensionPointLimiter, plugInId);
    }

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
