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
package org.talend.core.repository.utils;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;

/**
 * 
 * DOC mzhao Get extensions of service:org.talend.core.repository.resource_unload
 */
public class TDQServiceRegister {

    private static IConfigurationElement[] configurationElements = new IConfigurationElement[0];

    private static TDQServiceRegister instance = null;

    private TDQServiceRegister() {

    }

    public static TDQServiceRegister getInstance() {
        if (instance == null) {
            instance = new TDQServiceRegister();
        }
        return instance;
    }

    static {
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        if (registry != null) {
            configurationElements = registry.getConfigurationElementsFor("org.talend.core.repository.resource_change"); //$NON-NLS-1$
        }
    }

    private Map<Class<?>, AbstractResourceChangesService> services = new HashMap<Class<?>, AbstractResourceChangesService>();

    public AbstractResourceChangesService getResourceChangeService(Class<?> klass) {
        AbstractResourceChangesService service = services.get(klass);
        if (service == null) {
            service = findService(klass);
            if (service != null) {
                services.put(klass, service);
            }
        }
        return service;
    }

    /**
     * DOC mzhao find the unload resource service.
     * 
     * @param klass the class type want to find.
     * @return IService
     */
    private AbstractResourceChangesService findService(Class<?> klass) {
        for (IConfigurationElement element : configurationElements) {
            try {
                Object service = element.createExecutableExtension("class"); //$NON-NLS-1$
                if (klass.isInstance(service)) {
                    return (AbstractResourceChangesService) service;
                }
            } catch (CoreException e) {
                ExceptionHandler.process(e);
            }
        }
        return null;
    }

}
