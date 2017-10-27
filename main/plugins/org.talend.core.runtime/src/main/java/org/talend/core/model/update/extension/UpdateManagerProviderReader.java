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
package org.talend.core.model.update.extension;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.SafeRunner;
import org.osgi.framework.FrameworkUtil;
import org.talend.core.model.update.EUpdateItemType;
import org.talend.core.model.update.IUpdateItemType;
import org.talend.core.utils.RegistryReader;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class UpdateManagerProviderReader extends RegistryReader {

    private static Logger log = Logger.getLogger(UpdateManagerProviderReader.class);

    /**
     * key is name of type.
     */
    private Map<String, IUpdateItemType> extensionUpdateItemTypes = new HashMap<String, IUpdateItemType>();

    /**
     * key is id of provider.
     */
    private Map<String, UpdateManagerProviderExtension> processProvidersMap = new HashMap<String, UpdateManagerProviderExtension>();

    private Map<String, UpdateManagerProviderExtension> repositorProvidersMap = new HashMap<String, UpdateManagerProviderExtension>();

    private IProcessUpdateManagerProvider[] processProviders;

    private IRepositoryUpdateManagerProvider[] repositoryProviders;

    protected UpdateManagerProviderReader() {
        super(FrameworkUtil.getBundle(UpdateManagerProviderReader.class).getSymbolicName(), "updateManager_provider"); //$NON-NLS-1$
        init();
    }

    Map<String, IUpdateItemType> getExtensionUpdateItemTypes() {
        return this.extensionUpdateItemTypes;
    }

    IProcessUpdateManagerProvider[] getProcessProviders() {
        return this.processProviders;
    }

    IRepositoryUpdateManagerProvider[] getRepositoryProviders() {
        return this.repositoryProviders;
    }

    private void init() {
        // init types
        for (EUpdateItemType type : EUpdateItemType.values()) {
            extensionUpdateItemTypes.put(type.getName(), type);
        }

        readRegistry();

        //
        List<IUpdateManagerProvider> validProcessProviders = processProviders(new HashMap<String, UpdateManagerProviderExtension>(
                processProvidersMap));
        Iterator<IUpdateManagerProvider> processItr = validProcessProviders.iterator();
        while (processItr.hasNext()) {
            IUpdateManagerProvider provider = processItr.next();
            // make sure the provider is for process only.
            if (!IProcessUpdateManagerProvider.class.isInstance(provider)) {
                processItr.remove();
            }
        }
        processProviders = validProcessProviders.toArray(new IProcessUpdateManagerProvider[0]);

        //
        List<IUpdateManagerProvider> validRepProviders = processProviders(new HashMap<String, UpdateManagerProviderExtension>(
                repositorProvidersMap));
        Iterator<IUpdateManagerProvider> repItr = validRepProviders.iterator();
        while (repItr.hasNext()) {
            IUpdateManagerProvider provider = repItr.next();
            // make sure the provider is for reposiory only.
            if (!IRepositoryUpdateManagerProvider.class.isInstance(provider)) {
                repItr.remove();
            }
        }
        repositoryProviders = validRepProviders.toArray(new IRepositoryUpdateManagerProvider[0]);
    }

    private List<IUpdateManagerProvider> processProviders(Map<String, UpdateManagerProviderExtension> providersMap) {

        // process override and wrong provider setting.
        List<String> wrongProviders = new ArrayList<String>();
        List<String> overrideIds = new ArrayList<String>();
        for (String key : providersMap.keySet()) {
            UpdateManagerProviderExtension prividerExtension = providersMap.get(key);
            String overrideId = prividerExtension.getOverrideId();
            if (overrideId != null) {
                overrideIds.add(overrideId);
            }
            //
            IUpdateManagerProvider provider = prividerExtension.getProvider();
            if (provider == null) {
                wrongProviders.add(prividerExtension.getId());
            }

        }
        // override ids
        for (String id : overrideIds) {
            providersMap.remove(id);
        }
        // wrong providers
        for (String id : wrongProviders) {
            providersMap.remove(id);
        }
        // sort
        List<UpdateManagerProviderExtension> providerExtensions = new ArrayList<UpdateManagerProviderExtension>(
                providersMap.values());

        Collections.sort(providerExtensions, new Comparator<UpdateManagerProviderExtension>() {

            @Override
            public int compare(UpdateManagerProviderExtension arg1, UpdateManagerProviderExtension arg2) {
                return arg2.getPriority().compareTo(arg1.getPriority());
            }
        });

        //
        List<IUpdateManagerProvider> providers = new ArrayList<IUpdateManagerProvider>();
        for (UpdateManagerProviderExtension extension : providerExtensions) {
            IUpdateManagerProvider provider = extension.getProvider();
            if (provider != null) {
                providers.add(provider);
            }
        }
        return providers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.utils.RegistryReader#readElement(org.eclipse.core.runtime.IConfigurationElement)
     */
    @Override
    protected boolean readElement(final IConfigurationElement element) {
        if ("UpdateItemType".equals(element.getName())) { //$NON-NLS-1$
            SafeRunner.run(new RegistryReader.RegistrySafeRunnable() {

                @Override
                public void run() throws Exception {
                    try {
                        final String name = element.getAttribute("name"); //$NON-NLS-1$
                        final String description = element.getAttribute("description"); //$NON-NLS-1$
                        final String displayLabel = element.getAttribute("displayLabel"); //$NON-NLS-1$
                        ExtensionUpdateItemType type = new ExtensionUpdateItemType(name, element.getContributor().getName(),
                                displayLabel);
                        type.setDescription(description);

                        IUpdateItemType existedType = extensionUpdateItemTypes.get(name);
                        if (existedType != null) {
                            if (existedType instanceof ExtensionUpdateItemType) {
                                log.error(MessageFormat.format("There is a same type {0} with different bundles {1} and {2}.", //$NON-NLS-1$
                                        name, type.getBundleId(), ((ExtensionUpdateItemType) existedType).getBundleId()));
                            } else {
                                log.error(MessageFormat.format("There is a same type {0} in EUpdateItemType for bundle {1}.", //$NON-NLS-1$
                                        name, type.getBundleId()));
                            }
                        } else {
                            extensionUpdateItemTypes.put(name, type);
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }

                }

            });
            return true;
        } else if ("ProcessUpdateManagerProvider".equals(element.getName())) {//$NON-NLS-1$
            readProviderElement(element, processProvidersMap);
            return true;
        } else if ("RepositoryUpdateManagerProvider".equals(element.getName())) { //$NON-NLS-1$ 
            readProviderElement(element, repositorProvidersMap);
            return true;
        }
        return false;
    }

    /**
     * DOC ggu Comment method "readProviderElement".
     * 
     * @param element
     */
    private void readProviderElement(final IConfigurationElement element,
            final Map<String, UpdateManagerProviderExtension> providersMap) {
        SafeRunner.run(new RegistryReader.RegistrySafeRunnable() {

            @Override
            public void run() throws Exception {
                try {
                    final String id = element.getAttribute("id"); //$NON-NLS-1$
                    final String name = element.getAttribute("name"); //$NON-NLS-1$
                    final String description = element.getAttribute("description"); //$NON-NLS-1$
                    final String overrideId = element.getAttribute("override"); //$NON-NLS-1$
                    String priorityString = element.getAttribute("priority"); //$NON-NLS-1$
                    final EPriority priority = (priorityString != null && priorityString.length() > 0) ? EPriority
                            .valueOf(priorityString.toUpperCase()) : EPriority.NORMAL;

                    UpdateManagerProviderExtension providerExtension = new UpdateManagerProviderExtension(id);
                    providerExtension.setName(name);
                    providerExtension.setDescription(description);
                    providerExtension.setPriority(priority);
                    providerExtension.setOverrideId(overrideId);
                    IUpdateManagerProvider provider = (IUpdateManagerProvider) element.createExecutableExtension("provider"); //$NON-NLS-1$
                    providerExtension.setProvider(provider);

                    providersMap.put(id, providerExtension);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }

            }

        });
    }
}
