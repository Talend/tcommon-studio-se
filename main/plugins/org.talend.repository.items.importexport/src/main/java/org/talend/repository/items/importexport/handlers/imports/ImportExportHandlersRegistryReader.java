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
package org.talend.repository.items.importexport.handlers.imports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.SafeRunner;
import org.talend.core.utils.RegistryReader;
import org.talend.repository.items.importexport.handlers.model.internal.BasicRegistry;
import org.talend.repository.items.importexport.handlers.model.internal.EPriority;
import org.talend.repository.items.importexport.handlers.model.internal.ImportItemsProviderRegistry;
import org.talend.repository.items.importexport.handlers.model.internal.ImportResourcesProviderRegistry;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ImportExportHandlersRegistryReader extends RegistryReader {

    private static Logger log = Logger.getLogger(ImportExportHandlersRegistryReader.class);

    private Map<String, ImportItemsProviderRegistry> imortRegistries = new HashMap<String, ImportItemsProviderRegistry>();

    private Map<String, ImportResourcesProviderRegistry> resImportResistries = new HashMap<String, ImportResourcesProviderRegistry>();

    private IImportItemsHandler[] importItemsHandlers = null;

    private IImportResourcesHandler[] importResourcesHandlers = null;

    private static ImportExportHandlersRegistryReader instance;

    public static ImportExportHandlersRegistryReader getInstance() {
        if (instance == null) {
            instance = new ImportExportHandlersRegistryReader();
            instance.init();
        }
        return instance;
    }

    private ImportExportHandlersRegistryReader() {
        super("org.talend.repository.items.importexport", "handler"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private void clearCache() {
        imortRegistries.clear();
        resImportResistries.clear();
        importItemsHandlers = null;
        importResourcesHandlers = null;
    }

    public void init() {
        clearCache();
        readRegistry();

        // check for override
        List<BasicRegistry> availableImportItemsProviderRegistries = getAvailableImportProviderRegistries(new HashMap<String, BasicRegistry>(
                this.imortRegistries));
        List<BasicRegistry> availableImportResourcesProviderRegistries = getAvailableImportProviderRegistries(new HashMap<String, BasicRegistry>(
                this.resImportResistries));

        // according to the priority to sort
        final Comparator<BasicRegistry> comparator = new Comparator<BasicRegistry>() {

            @Override
            public int compare(BasicRegistry arg0, BasicRegistry arg1) {
                return arg1.getPriority().compareTo(arg0.getPriority());
            }
        };
        Collections.sort(availableImportItemsProviderRegistries, comparator);
        Collections.sort(availableImportResourcesProviderRegistries, comparator);

        // init arrays
        List<IImportItemsHandler> itemsHanders = new ArrayList<IImportItemsHandler>();
        for (BasicRegistry reg : availableImportItemsProviderRegistries) {
            if (reg instanceof ImportItemsProviderRegistry) {
                itemsHanders.add(((ImportItemsProviderRegistry) reg).getImportItemsHandler());
            }
        }
        importItemsHandlers = itemsHanders.toArray(new IImportItemsHandler[0]);

        List<IImportResourcesHandler> resourcesHanders = new ArrayList<IImportResourcesHandler>();
        for (BasicRegistry reg : availableImportResourcesProviderRegistries) {
            if (reg instanceof ImportResourcesProviderRegistry) {
                resourcesHanders.add(((ImportResourcesProviderRegistry) reg).getImportResourcesHandler());
            }
        }
        importResourcesHandlers = resourcesHanders.toArray(new IImportResourcesHandler[0]);

    }

    private List<BasicRegistry> getAvailableImportProviderRegistries(Map<String, BasicRegistry> registries) {
        Map<String, BasicRegistry> availableItemProviders = new HashMap<String, BasicRegistry>(registries);
        // find the override ids
        List<String> overrideItemsProviderIds = new ArrayList<String>();
        for (String id : availableItemProviders.keySet()) {
            BasicRegistry registry = availableItemProviders.get(id);
            String overrideId = registry.getOverrideId();
            if (overrideId != null && overrideId.length() > 0) {
                overrideItemsProviderIds.add(overrideId);
            }
        }
        // remove the override registry
        for (String id : overrideItemsProviderIds) {
            availableItemProviders.remove(id);
        }
        return new ArrayList<BasicRegistry>(availableItemProviders.values());
    }

    public IImportItemsHandler[] getImportHandlers() {
        if (importItemsHandlers == null) {
            return new IImportItemsHandler[0];
        }
        return importItemsHandlers;
    }

    public IImportResourcesHandler[] getImportResourcesHandlers() {
        if (importResourcesHandlers == null) {
            return new IImportResourcesHandler[0];
        }
        return importResourcesHandlers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.utils.RegistryReader#readElement(org.eclipse.core.runtime.IConfigurationElement)
     */
    @Override
    protected boolean readElement(final IConfigurationElement element) {
        final String id = element.getAttribute("id"); //$NON-NLS-1$
        final String name = element.getAttribute("name"); //$NON-NLS-1$
        final String description = element.getAttribute("description"); //$NON-NLS-1$
        final String overrideId = element.getAttribute("override"); //$NON-NLS-1$

        String priorityString = element.getAttribute("priority"); //$NON-NLS-1$
        final EPriority priority = (priorityString != null && priorityString.length() > 0) ? EPriority.valueOf(priorityString
                .toUpperCase()) : EPriority.NORMAL;

        if ("importItemsProvider".equals(element.getName())) { //$NON-NLS-1$
            SafeRunner.run(new RegistryReader.RegistrySafeRunnable() {

                @Override
                public void run() throws Exception {
                    try {

                        IImportItemsHandler importItemsHandler = (IImportItemsHandler) element
                                .createExecutableExtension("importItemsHandler"); //$NON-NLS-1$
                        if (importItemsHandler == null) {
                            log.error("Can't create handlder for " + name); //$NON-NLS-1$
                            return;
                        }

                        ImportItemsProviderRegistry registry = new ImportItemsProviderRegistry(
                                element.getContributor().getName(), id);
                        registry.setName(name);
                        registry.setDescription(description);
                        registry.setImportItemsHandler(importItemsHandler);
                        registry.setPriority(priority);
                        registry.setOverrideId(overrideId);

                        String id2 = registry.getId();
                        if (imortRegistries.containsKey(id2)) {

                        } else {
                            imortRegistries.put(id2, registry);
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }

                }

            });
            return true;
        } else if ("importResourcesProvider".equals(element.getName())) { //$NON-NLS-1$
            SafeRunner.run(new RegistryReader.RegistrySafeRunnable() {

                @Override
                public void run() throws Exception {
                    try {
                        IImportResourcesHandler resImportHandler = (IImportResourcesHandler) element
                                .createExecutableExtension("importResoucesHandler");//$NON-NLS-1$

                        ImportResourcesProviderRegistry registry = new ImportResourcesProviderRegistry(element.getContributor()
                                .getName(), id);
                        registry.setDescription(description);
                        registry.setPriority(priority);
                        registry.setImportResourcesHandler(resImportHandler);
                        registry.setOverrideId(overrideId);

                        String id2 = registry.getId();
                        if (resImportResistries.containsKey(id2)) {

                        } else {
                            resImportResistries.put(id2, registry);
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }

                }

            });
            return true;
        }
        return false;
    }

}
