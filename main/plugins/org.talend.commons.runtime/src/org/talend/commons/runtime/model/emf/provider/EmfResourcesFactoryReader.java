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
package org.talend.commons.runtime.model.emf.provider;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.SafeRunner;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.runtime.extension.ExtensionRegistryReader;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class EmfResourcesFactoryReader extends ExtensionRegistryReader {

    public static final EmfResourcesFactoryReader INSTANCE = new EmfResourcesFactoryReader();

    /*
     * id==> bean
     */
    private Map<String, OptionProviderBean> saveOptionsBeans = new HashMap<String, OptionProviderBean>();

    private Map<String, OptionProviderBean> loadOptionsBeans = new HashMap<String, OptionProviderBean>();

    private Map<String, ResourceHandlerBean> resourceHandlerBeans = new HashMap<String, ResourceHandlerBean>();

    /*
     * id ==> provider
     */
    private Map<String, OptionProvider> saveOptionsProviders = new HashMap<String, OptionProvider>();

    private Map<String, OptionProvider> loadOptionsProviders = new HashMap<String, OptionProvider>();

    private Map<String, ResourceHandler> resourceHandlers = new HashMap<String, ResourceHandler>();

    class ExtensionBean {

        String id, description, overrideId;
    }

    class OptionProviderBean extends ExtensionBean {

        OptionProvider provider;
    }

    class ResourceHandlerBean extends ExtensionBean {

        ResourceHandler handler;
    }

    private EmfResourcesFactoryReader() {
        super(CommonsPlugin.PLUGIN_ID, "emfResourcesProvider"); //$NON-NLS-1$
        init();
    }

    void init() {
        readRegistry();

        cleanOverrideIdsForOption(saveOptionsProviders, saveOptionsBeans);
        cleanOverrideIdsForOption(loadOptionsProviders, loadOptionsBeans);
        cleanOverrideIdsForHandler(resourceHandlers, resourceHandlerBeans);
    }

    private void cleanOverrideIdsForOption(Map<String, OptionProvider> optionProviders,
            Map<String, ? extends ExtensionBean> extensionMap) {
        Map<String, ? extends ExtensionBean> withoutOverrideMap = cleanOverrideIds(extensionMap);
        for (Map.Entry<String, ? extends ExtensionBean> entry : withoutOverrideMap.entrySet()) {
            ExtensionBean value = entry.getValue();
            if (value instanceof OptionProviderBean) {
                final OptionProvider optionProvider = ((OptionProviderBean) value).provider;
                optionProviders.put(entry.getKey(), optionProvider);
            }
        }
    }

    private void cleanOverrideIdsForHandler(Map<String, ResourceHandler> handlers,
            Map<String, ? extends ExtensionBean> extensionMap) {
        Map<String, ? extends ExtensionBean> withoutOverrideMap = cleanOverrideIds(extensionMap);
        for (Map.Entry<String, ? extends ExtensionBean> entry : withoutOverrideMap.entrySet()) {
            ExtensionBean value = entry.getValue();
            if (value instanceof ResourceHandlerBean) {
                final ResourceHandler optionProvider = ((ResourceHandlerBean) value).handler;
                handlers.put(entry.getKey(), optionProvider);
            }
        }
    }

    private Map<String, ? extends ExtensionBean> cleanOverrideIds(Map<String, ? extends ExtensionBean> extensionMap) {
        Set<String> ids = new HashSet<String>();
        Map<String, ? extends ExtensionBean> withoutOverrideMap = new HashMap<>(extensionMap);

        for (Map.Entry<String, ? extends ExtensionBean> e : withoutOverrideMap.entrySet()) {
            final String overrideId = e.getValue().overrideId;
            if (overrideId != null) {
                ids.add(overrideId);
            }
        }

        // remove override ids
        final Iterator<String> removediterator = withoutOverrideMap.keySet().iterator();
        while (removediterator.hasNext()) {
            if (ids.contains(removediterator.next())) {
                removediterator.remove();
            }
        }

        return withoutOverrideMap;
    }

    public Map<String, Object> getSaveOptions(Object resource) {
        Map<String, Object> saveOptions = new HashMap<String, Object>();
        for (Map.Entry<String, OptionProvider> entry : saveOptionsProviders.entrySet()) {
            final OptionProvider provider = entry.getValue();
            if (provider.checkSave(resource)) {
                saveOptions.put(provider.getName(), provider.getValue());
            }
        }
        return saveOptions;
    }

    public Map<String, Object> getLoadOptions(Object resource) {
        Map<String, Object> loadOptions = new HashMap<String, Object>();
        for (Map.Entry<String, OptionProvider> entry : loadOptionsProviders.entrySet()) {
            final OptionProvider provider = entry.getValue();
            if (provider.checkLoad(resource)) {
                loadOptions.put(provider.getName(), provider.getValue());
            }
        }
        return loadOptions;
    }

    public Map<String, OptionProvider> getSaveOptionsProviders() {
        return saveOptionsProviders;
    }

    public boolean existedOption(ResourceOption option) {
        return existedSaveOption(option) || existedLoadOption(option);
    }

    public boolean existedSaveOption(ResourceOption option) {
        return getSaveOptionsProviders().containsKey(option.getName());
    }

    public Map<String, OptionProvider> getLoadOptionsProviders() {
        return loadOptionsProviders;
    }

    public boolean existedLoadOption(ResourceOption option) {
        return getLoadOptionsProviders().containsKey(option.getName());
    }

    /**
     * if load is false, will be for saving option.
     */
    public void addOption(IOptionProvider option, boolean load) {
        final Object value = option.getValue();
        if (value instanceof OptionProvider) {
            OptionProvider provider = (OptionProvider) value;
            if (load) {
                getLoadOptionsProviders().put(option.getName(), provider);
            } else {
                getSaveOptionsProviders().put(option.getName(), provider);
            }
        }
    }

    public void addOption(IOptionProvider option) {
        addOption(option, true);
        addOption(option, false);
    }

    public void removOption(IOptionProvider option, boolean load) {
        if (load) {
            getLoadOptionsProviders().remove(option.getName());
        } else {
            getSaveOptionsProviders().remove(option.getName());
        }
    }

    public void removOption(IOptionProvider option) {
        removOption(option, true);
        removOption(option, false);
    }

    public ResourceHandler[] getResourceHandlers() {
        return resourceHandlers.values().toArray(new ResourceHandler[resourceHandlers.size()]);
    }

    @Override
    protected boolean readElement(final IConfigurationElement element) {
        if ("saveOption".equals(element.getName())) { //$NON-NLS-1$
            SafeRunner.run(new RegistrySafeRunnable() {

                @Override
                public void run() throws Exception {
                    createProvider(saveOptionsBeans, element);
                }
            });
            return true;
        }
        if ("loadOption".equals(element.getName())) { //$NON-NLS-1$
            SafeRunner.run(new RegistrySafeRunnable() {

                @Override
                public void run() throws Exception {
                    createProvider(loadOptionsBeans, element);
                }
            });
            return true;
        }
        if ("resourceHandler".equals(element.getName())) { //$NON-NLS-1$
            SafeRunner.run(new RegistrySafeRunnable() {

                @Override
                public void run() throws Exception {
                    String id = element.getAttribute("id"); //$NON-NLS-1$
                    String description = element.getAttribute("description"); //$NON-NLS-1$
                    String overrideId = element.getAttribute("override"); //$NON-NLS-1$
                    ResourceHandler handler = (ResourceHandler) element.createExecutableExtension("handler");//$NON-NLS-1$
                    ResourceHandlerBean bean = new ResourceHandlerBean();
                    bean.id = id;
                    bean.description = description;
                    bean.overrideId = overrideId;
                    bean.handler = handler;
                    resourceHandlerBeans.put(id, bean);

                }
            });
            return true;
        }
        return false;
    }

    private void createProvider(Map<String, OptionProviderBean> map, IConfigurationElement element) throws CoreException {
        String id = element.getAttribute("id"); //$NON-NLS-1$
        String description = element.getAttribute("description"); //$NON-NLS-1$
        String overrideId = element.getAttribute("override"); //$NON-NLS-1$
        OptionProvider provider = (OptionProvider) element.createExecutableExtension("provider");//$NON-NLS-1$
        OptionProviderBean bean = new OptionProviderBean();
        bean.id = id;
        bean.description = description;
        bean.overrideId = overrideId;
        bean.provider = provider;
        map.put(id, bean);
    }

}
