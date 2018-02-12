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
package org.talend.core.model.relationship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.SafeRunner;
import org.osgi.framework.FrameworkUtil;
import org.talend.core.utils.RegistryReader;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class RelationshipRegistryReader extends RegistryReader {

    /**
     * 
     * DOC ggu ItemRelationshipRegistry class global comment. Detailled comment.
     */
    static class ItemRelationshipRegistry extends RelationshipRegistry {

        private IItemRelationshipHandler itemRelationshipHandler;

        public ItemRelationshipRegistry(String bundleId, String id) {
            super(bundleId, id);
        }

        public IItemRelationshipHandler getItemRelationshipHandler() {
            return this.itemRelationshipHandler;
        }

        public void setItemRelationshipHandler(IItemRelationshipHandler itemRelationshipHandler) {
            this.itemRelationshipHandler = itemRelationshipHandler;
        }

    }

    /**
     * 
     * DOC ggu ParameterRelationshipRegistry class global comment. Detailled comment.
     */
    static class ParameterRelationshipRegistry extends RelationshipRegistry {

        private IParameterRelationshipHandler parameterRelationshipHandler;

        public ParameterRelationshipRegistry(String bundleId, String id) {
            super(bundleId, id);
        }

        public IParameterRelationshipHandler getParameterRelationshipHandler() {
            return this.parameterRelationshipHandler;
        }

        public void setParameterRelationshipHandler(IParameterRelationshipHandler parameterRelationshipHandler) {
            this.parameterRelationshipHandler = parameterRelationshipHandler;
        }

    }

    private static Logger log = Logger.getLogger(RelationshipRegistryReader.class);

    private static final RelationshipRegistryReader INSTANCE = new RelationshipRegistryReader();

    private Map<String, ItemRelationshipRegistry> itemRelationshipRegistries;

    private Map<String, ParameterRelationshipRegistry> parameterRelationshipRegistries;

    public static RelationshipRegistryReader getInstance() {
        return INSTANCE;
    }

    private RelationshipRegistryReader() {
        super(FrameworkUtil.getBundle(RelationshipRegistryReader.class).getSymbolicName(), "itemRelationship"); //$NON-NLS-1$
        init();
    }

    public IItemRelationshipHandler[] getItemRelationshipHandlers() {
        List<IItemRelationshipHandler> handler = new ArrayList<IItemRelationshipHandler>();
        for (String id : itemRelationshipRegistries.keySet()) {
            ItemRelationshipRegistry itemRelationshipRegistry = itemRelationshipRegistries.get(id);
            IItemRelationshipHandler itemRelationshipHandler = itemRelationshipRegistry.getItemRelationshipHandler();
            if (itemRelationshipHandler != null) {
                handler.add(itemRelationshipHandler);
            }
        }
        return handler.toArray(new IItemRelationshipHandler[0]);
    }

    public IParameterRelationshipHandler[] getParameterRelationshipHandlers() {
        List<IParameterRelationshipHandler> handler = new ArrayList<IParameterRelationshipHandler>();
        for (String id : parameterRelationshipRegistries.keySet()) {
            ParameterRelationshipRegistry parameterRelationshipRegistry = parameterRelationshipRegistries.get(id);
            IParameterRelationshipHandler parameterRelationshipHandler = parameterRelationshipRegistry
                    .getParameterRelationshipHandler();
            if (parameterRelationshipHandler != null) {
                handler.add(parameterRelationshipHandler);
            }
        }
        return handler.toArray(new IParameterRelationshipHandler[0]);
    }

    private void init() {
        if (itemRelationshipRegistries == null) {
            synchronized (RelationshipRegistryReader.class) {

                itemRelationshipRegistries = new HashMap<String, ItemRelationshipRegistry>();
                parameterRelationshipRegistries = new HashMap<String, ParameterRelationshipRegistry>();

                readRegistry();

                checkOverride(itemRelationshipRegistries);
                checkOverride(parameterRelationshipRegistries);

            }
        }
    }

    private void checkOverride(Map<String, ? extends RelationshipRegistry> relationRegistries) {
        if (relationRegistries == null) {
            return;
        }
        List<String> overrideIds = new ArrayList<String>();
        for (String id : relationRegistries.keySet()) {
            RelationshipRegistry relationshipRegistry = relationRegistries.get(id);
            String overrideId = relationshipRegistry.getOverrideId();
            if (StringUtils.isNotEmpty(overrideId)) {
                overrideIds.add(overrideId);
            }
        }
        // remove override
        for (String id : overrideIds) {
            relationRegistries.remove(id);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.utils.RegistryReader#readElement(org.eclipse.core.runtime.IConfigurationElement)
     */
    @Override
    protected boolean readElement(final IConfigurationElement element) {
        if ("itemHandler".equals(element.getName())) { //$NON-NLS-1$
            SafeRunner.run(new RegistryReader.RegistrySafeRunnable() {

                @Override
                public void run() throws Exception {
                    try {
                        String id = element.getAttribute("id"); //$NON-NLS-1$
                        String name = element.getAttribute("name"); //$NON-NLS-1$
                        String description = element.getAttribute("description"); //$NON-NLS-1$

                        String overrideId = element.getAttribute("override"); //$NON-NLS-1$

                        IItemRelationshipHandler handler = (IItemRelationshipHandler) element.createExecutableExtension("class"); //$NON-NLS-1$

                        ItemRelationshipRegistry registry = new ItemRelationshipRegistry(element.getContributor().getName(), id);
                        registry.setName(name);
                        registry.setDescription(description);
                        registry.setItemRelationshipHandler(handler);
                        if (StringUtils.isNotEmpty(overrideId)) {
                            registry.setOverrideId(overrideId);
                        }
                        itemRelationshipRegistries.put(id, registry);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }

                }

            });
            return true;
        } else if ("parameterHandler".equals(element.getName())) { //$NON-NLS-1$
            SafeRunner.run(new RegistryReader.RegistrySafeRunnable() {

                @Override
                public void run() throws Exception {
                    try {
                        String id = element.getAttribute("id"); //$NON-NLS-1$
                        String name = element.getAttribute("name"); //$NON-NLS-1$
                        String description = element.getAttribute("description"); //$NON-NLS-1$

                        String overrideId = element.getAttribute("override"); //$NON-NLS-1$

                        IParameterRelationshipHandler handler = (IParameterRelationshipHandler) element
                                .createExecutableExtension("class"); //$NON-NLS-1$

                        ParameterRelationshipRegistry registry = new ParameterRelationshipRegistry(element.getContributor()
                                .getName(), id);
                        registry.setName(name);
                        registry.setDescription(description);
                        registry.setParameterRelationshipHandler(handler);
                        if (StringUtils.isNotEmpty(overrideId)) {
                            registry.setOverrideId(overrideId);
                        }
                        parameterRelationshipRegistries.put(id, registry);
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
