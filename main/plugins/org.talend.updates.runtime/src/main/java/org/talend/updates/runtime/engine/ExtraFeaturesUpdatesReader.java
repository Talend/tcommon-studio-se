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
package org.talend.updates.runtime.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.SafeRunner;
import org.talend.core.utils.RegistryReader;
import org.talend.updates.runtime.Constants;
import org.talend.updates.runtime.engine.factory.AbstractExtraUpdatesFactory;

/**
 * created by ggu on Jul 17, 2014 Detailled comment
 *
 */
public class ExtraFeaturesUpdatesReader extends RegistryReader {

    private static Logger log = Logger.getLogger(ExtraFeaturesUpdatesReader.class);

    private Map<String, ExtraUpdatesFactoryRegistry> updatesFactoriesMap = new HashMap<String, ExtraUpdatesFactoryRegistry>();

    private AbstractExtraUpdatesFactory[] factories;

    public ExtraFeaturesUpdatesReader() {
        super(Constants.PLUGIN_ID, "extraUpdatesFactory"); //$NON-NLS-1$

        init();
    }

    private void init() {
        readRegistry();

        // remove the override helpers
        List<String> overrideIds = new ArrayList<String>();
        for (String key : updatesFactoriesMap.keySet()) {
            ExtraUpdatesFactoryRegistry registry = updatesFactoriesMap.get(key);
            String overrideId = registry.overrideId;
            if (overrideId != null && overrideId.length() > 0 && updatesFactoriesMap.containsKey(key)) {
                overrideIds.add(overrideId);
            }
        }
        for (String key : overrideIds) {
            updatesFactoriesMap.remove(key);
        }

        //
        List<AbstractExtraUpdatesFactory> updatesFactories = new ArrayList<AbstractExtraUpdatesFactory>();
        for (String key : updatesFactoriesMap.keySet()) {
            ExtraUpdatesFactoryRegistry registry = updatesFactoriesMap.get(key);
            updatesFactories.add(registry.factory);
        }

        factories = updatesFactories.toArray(new AbstractExtraUpdatesFactory[0]);
    }

    public AbstractExtraUpdatesFactory[] getUpdatesFactories() {
        return this.factories;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.utils.RegistryReader#readElement(org.eclipse.core.runtime.IConfigurationElement)
     */
    @Override
    protected boolean readElement(final IConfigurationElement element) {
        if ("factory".equals(element.getName())) { //$NON-NLS-1$
            SafeRunner.run(new RegistryReader.RegistrySafeRunnable() {

                @Override
                public void run() throws Exception {
                    try {
                        String id = element.getAttribute("id"); //$NON-NLS-1$
                        String name = element.getAttribute("name"); //$NON-NLS-1$
                        String description = element.getAttribute("description"); //$NON-NLS-1$
                        String overrideId = element.getAttribute("override"); //$NON-NLS-1$

                        AbstractExtraUpdatesFactory updatesFactory = (AbstractExtraUpdatesFactory) element
                                .createExecutableExtension("class"); //$NON-NLS-1$
                        if (updatesFactory == null) {
                            log.error("Can't create update factory for " + id); //$NON-NLS-1$
                            return;
                        }
                        if (updatesFactoriesMap.containsKey(id)) {
                            log.error("Can't set the id same as existed id " + id); //$NON-NLS-1$
                            return;
                        }
                        ExtraUpdatesFactoryRegistry registry = new ExtraUpdatesFactoryRegistry();
                        registry.id = id;
                        registry.name = name;
                        registry.factory = updatesFactory;
                        registry.description = description;
                        registry.overrideId = overrideId;
                        updatesFactoriesMap.put(id, registry);

                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }

                }

            });
            return true;

        }
        return false;
    }

    /**
     * 
     * created by ggu on Jul 17, 2014 Detailled comment
     *
     */
    class ExtraUpdatesFactoryRegistry {

        String name, id, description, overrideId;

        AbstractExtraUpdatesFactory factory;

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            ExtraUpdatesFactoryRegistry other = (ExtraUpdatesFactoryRegistry) obj;
            if (this.id == null) {
                if (other.id != null) {
                    return false;
                }
            } else if (!this.id.equals(other.id)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return this.id;
        }

    }
}
