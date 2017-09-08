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
package org.talend.librariesmanager.ui.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.routines.IRoutineProviderCreator;
import org.talend.core.model.routines.IRoutinesProvider;
import org.talend.librariesmanager.ui.i18n.Messages;

/**
 * DOC bqian class global comment. Detailled comment
 */
public class RoutineProviderManager {

    private static Logger log = Logger.getLogger(RoutineProviderManager.class);

    private static final RoutineProviderManager INSTANCE = new RoutineProviderManager();

    private Collection<IRoutineProviderCreator> providerCreators;

    private RoutineProviderManager() {
    }

    public static RoutineProviderManager getInstance() {
        return INSTANCE;
    }

    public Collection<IRoutinesProvider> getProviders(ECodeLanguage lan) {
        if (providerCreators == null) {
            loadRoutinesProvidersFromExtension();
        }

        List<IRoutinesProvider> providers = new ArrayList<IRoutinesProvider>();
        for (IRoutineProviderCreator creator : providerCreators) {
            IRoutinesProvider provider = creator.createIRoutinesProviderByLanguage(lan);
            if (provider != null) {
                providers.add(provider);
            }
        }

        return providers;
    }

    private void loadRoutinesProvidersFromExtension() {
        providerCreators = new ArrayList<IRoutineProviderCreator>();

        IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
        IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint("org.talend.core.routines_provider"); //$NON-NLS-1$
        IExtension[] extensions = extensionPoint.getExtensions();
        for (IExtension extension : extensions) {
            IConfigurationElement[] configurationElements = extension.getConfigurationElements();
            for (IConfigurationElement configurationElement : configurationElements) {
                try {
                    IRoutineProviderCreator componentsProviderCreator = (IRoutineProviderCreator) configurationElement
                            .createExecutableExtension("class"); //$NON-NLS-1$
                    providerCreators.add(componentsProviderCreator);
                } catch (CoreException e) {
                    log.error(Messages.getString("RoutineProviderManager.unableLoad"), e); //$NON-NLS-1$
                }
            }
        }
    }
}
