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
package org.talend.core;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.runtime.i18n.Messages;

/**
 * DOC qian class global comment. A global service register provides the service registration and acquirement. <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (鏄熸湡浜� 29 涔?鏈�2006) nrousseau $
 * 
 */
// TODO remove this class and use OSGI dynamic services
public class GlobalServiceRegister {

    // The shared instance
    private static GlobalServiceRegister instance = new GlobalServiceRegister();

    public static GlobalServiceRegister getDefault() {
        return instance;
    }

    private Map<Class, IService> services = new HashMap<Class, IService>();

    private Map<Class<?>, AbstractDQModelService> dqModelServices = new HashMap<Class<?>, AbstractDQModelService>();

    private static IConfigurationElement[] configurationElements = null;

    private static IConfigurationElement[] configurationDQModelElements = null;

    private static IConfigurationElement[] configurationDQDriverElements = null;

    private IConfigurationElement[] getConfigurationElements() {
        if (configurationElements == null) {
            IExtensionRegistry registry = Platform.getExtensionRegistry();
            configurationElements = (registry == null ? null : registry
                    .getConfigurationElementsFor("org.talend.core.runtime.service")); //$NON-NLS-1$
        }
        return configurationElements;
    }

    private IConfigurationElement[] getConfigurationDQModelElements() {
        if (configurationDQModelElements == null) {
            IExtensionRegistry registry = Platform.getExtensionRegistry();
            configurationDQModelElements = (registry == null ? null : registry
                    .getConfigurationElementsFor("org.talend.core.runtime.dq_EMFModel_provider")); //$NON-NLS-1$
        }
        return configurationDQModelElements;
    }

    private IConfigurationElement[] getConfigurationDQDriverElements() {
        if (configurationDQDriverElements == null) {
            IExtensionRegistry registry = Platform.getExtensionRegistry();
            configurationDQDriverElements = (registry == null ? null : registry
                    .getConfigurationElementsFor("org.talend.metadata.managment.DBDriver_extension")); //$NON-NLS-1$
        }
        return configurationDQDriverElements;
    }

    public AbstractDQModelService getDQModelService(Class<?> klass) {
        AbstractDQModelService dqModelserviceInst = dqModelServices.get(klass);
        if (dqModelserviceInst == null) {
            dqModelserviceInst = findDQModelService(klass);
            if (dqModelserviceInst != null) {
                dqModelServices.put(klass, dqModelserviceInst);
            }
        }
        return dqModelserviceInst;
    }

    public IService getDQDriverService(Class<?> klass) {
        IService dqModelserviceInst = services.get(klass);
        if (dqModelserviceInst == null) {
            dqModelserviceInst = findDQModelService(klass);
            if (dqModelserviceInst != null) {
                services.put(klass, dqModelserviceInst);
            }
        }
        return dqModelserviceInst;
    }

    public boolean isDQModelServiceRegistered(Class klass) {
        AbstractDQModelService service = dqModelServices.get(klass);
        if (service == null) {
            service = findDQModelService(klass);
            if (service == null) {
                return false;
            }
            dqModelServices.put(klass, service);
        }
        return true;
    }

    public boolean isDQDriverServiceRegistered(Class klass) {
        IService service = services.get(klass);
        if (service == null) {
            service = findDQDriverService(klass);
            if (service == null) {
                return false;
            }
            services.put(klass, service);
        }
        return true;
    }

    public boolean isServiceRegistered(Class klass) {
        IService service = services.get(klass);
        if (service == null) {
            service = findService(klass);
            if (service == null) {
                return false;
            }
            services.put(klass, service);
        }
        return true;
    }

    /**
     * DOC qian Comment method "getService".Gets the specific IService.
     * 
     * @param klass the Service type you want to get
     * @return IService IService
     */
    public IService getService(Class klass) {
        IService service = services.get(klass);
        if (service == null && getConfigurationElements() != null) {
            service = findService(klass);
            if (service == null) {

                throw new RuntimeException(Messages.getString("GlobalServiceRegister.ServiceNotRegistered", klass.getName())); //$NON-NLS-1$ 
            }
            services.put(klass, service);
        }
        return service;
    }

    /**
     * DOC qian Comment method "findService".Finds the specific service from the list.
     * 
     * @param klass the interface type want to find.
     * @return IService
     */
    private IService findService(Class klass) {
        String key = klass.getName();
        IConfigurationElement[] configElements = getConfigurationElements();
        if (configElements != null) {
            for (IConfigurationElement element : configElements) {
                if (element.isValid()) {
                    String id = element.getAttribute("serviceId"); //$NON-NLS-1$
                    if (!key.endsWith(id)) {
                        continue;
                    }
                    try {
                        Object service = element.createExecutableExtension("class"); //$NON-NLS-1$
                        if (klass.isInstance(service)) {
                            return (IService) service;
                        }
                    } catch (CoreException e) {
                        ExceptionHandler.process(e);
                    }
                }// else element is not valid because the bundle may have been stoped or uninstalled and the extension
                 // point
                 // registry is still holding values
                 // has mentionned in the class TODO, this class should be removed and OSGI dynamic services used.
            }
        }
        return null;
    }

    /**
     * DOC hwang Comment method "findService".Finds the specific service from the list.
     * 
     * @param klass the interface type want to find.
     * @return IService
     */
    public IProviderService findService(String key) {
        IConfigurationElement[] configElements = getConfigurationElements();
        if (configElements != null) {
            for (IConfigurationElement element : configElements) {
                if (element.isValid()) {
                    String id = element.getAttribute("serviceId"); //$NON-NLS-1$
                    if (!key.equals(id)) {
                        continue;
                    }
                    try {
                        Object service = element.createExecutableExtension("class"); //$NON-NLS-1$
                        if (service instanceof IProviderService) {
                            return (IProviderService) service;
                        }
                    } catch (CoreException e) {
                        ExceptionHandler.process(e);
                    }
                }
            }
        }
        return null;
    }

    private AbstractDQModelService findDQModelService(Class<?> klass) {
        IConfigurationElement[] configDQModelElements = getConfigurationDQModelElements();
        if (configDQModelElements != null) {
            for (IConfigurationElement element : configDQModelElements) {
                try {
                    Object service = element.createExecutableExtension("class"); //$NON-NLS-1$
                    if (klass.isInstance(service)) {
                        return (AbstractDQModelService) service;
                    }
                } catch (CoreException e) {
                    ExceptionHandler.process(e);
                }
            }
        }
        return null;
    }

    private IService findDQDriverService(Class<?> klass) {
        IConfigurationElement[] configDQModelElements = getConfigurationDQDriverElements();
        if (configDQModelElements != null) {
            for (IConfigurationElement element : configDQModelElements) {
                try {
                    Object service = element.createExecutableExtension("class"); //$NON-NLS-1$
                    if (klass.isInstance(service)) {
                        return (IService) service;
                    }
                } catch (CoreException e) {
                    ExceptionHandler.process(e);
                }
            }
        }
        return null;
    }
}
