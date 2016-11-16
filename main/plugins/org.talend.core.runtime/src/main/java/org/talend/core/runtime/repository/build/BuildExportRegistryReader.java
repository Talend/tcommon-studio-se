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
package org.talend.core.runtime.repository.build;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.SafeRunner;
import org.osgi.framework.FrameworkUtil;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.utils.RegistryReader;

public class BuildExportRegistryReader extends RegistryReader {

    private List<IBuildExportDependenciesProvider> dependenciesProviders;

    private IBuildExportDependenciesProvider[] dependenciesProviderArrays;

    BuildExportRegistryReader() {
        super(FrameworkUtil.getBundle(BuildExportRegistryReader.class).getSymbolicName(), "buildExport_provider"); //$NON-NLS-1$
    }

    void init() {
        if (dependenciesProviderArrays == null || dependenciesProviderArrays.length == 0) {
            synchronized (BuildExportRegistryReader.class) {
                dependenciesProviders = new ArrayList<IBuildExportDependenciesProvider>();
                readRegistry();
                dependenciesProviderArrays = dependenciesProviders.toArray(new IBuildExportDependenciesProvider[0]);
            }
        }
    }

    IBuildExportDependenciesProvider[] getDependenciesProviders() {
        init();
        return dependenciesProviderArrays;
    }

    @Override
    protected boolean readElement(final IConfigurationElement element) {
        if ("dependenciesProvider".equals(element.getName())) { //$NON-NLS-1$
            SafeRunner.run(new RegistryReader.RegistrySafeRunnable() {

                @Override
                public void run() throws Exception {
                    try {
                        //String name = element.getAttribute("name"); //$NON-NLS-1$
                        IBuildExportDependenciesProvider provider = (IBuildExportDependenciesProvider) element
                                .createExecutableExtension("class"); //$NON-NLS-1$
                        dependenciesProviders.add(provider);
                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }
                }

            });
            return true;

        }
        return false;
    }

}
