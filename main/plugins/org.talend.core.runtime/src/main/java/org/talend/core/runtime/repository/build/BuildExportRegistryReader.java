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
package org.talend.core.runtime.repository.build;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.SafeRunner;
import org.osgi.framework.FrameworkUtil;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.utils.RegistryReader;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class BuildExportRegistryReader extends RegistryReader {

    private Object flag;

    private List<IBuildExportDependenciesProvider> dependenciesProviders;

    private IBuildExportDependenciesProvider[] dependenciesProviderArrays;

    Map<String, BuildProviderRegistry> buildProvidersMap;

    Map<String, BuildResourcesProviderRegistry> buildResourcesProvidersMap;

    AbstractBuildProvider[] buildProviders;

    IBuildResourcesProvider[] buildResourcesProviders;

    BuildExportRegistryReader() {
        super(FrameworkUtil.getBundle(BuildExportRegistryReader.class).getSymbolicName(), "buildExport_provider"); //$NON-NLS-1$
    }

    void init() {
        if (flag == null) {
            synchronized (BuildExportRegistryReader.class) {
                flag = new Object();

                dependenciesProviders = new ArrayList<IBuildExportDependenciesProvider>();
                buildProvidersMap = new HashMap<String, BuildProviderRegistry>();
                buildResourcesProvidersMap = new HashMap<String, BuildResourcesProviderRegistry>();

                readRegistry();
                dependenciesProviderArrays = dependenciesProviders.toArray(new IBuildExportDependenciesProvider[0]);

                processBuildProviders();
                processBuildResourcesProviders();
            }
        }
    }

    void processBuildProviders() {
        final Collection<BuildProviderRegistry> registries = buildProvidersMap.values();

        // collect override
        // List<String> overrideIds=registries.stream().filter(r -> r.overrideId != null).map(r ->
        // r.overrideId).collect(Collectors.toList());
        List<String> overrideIds = new ArrayList<String>();
        for (BuildProviderRegistry r : registries) {
            if (r.overrideId != null) {
                overrideIds.add(r.overrideId);
            }
        }

        List<BuildProviderRegistry> validRegistries = new ArrayList<BuildProviderRegistry>();
        for (BuildProviderRegistry r : registries) {
            if (!overrideIds.contains(r.id)) { // filter override
                validRegistries.add(r);
            }
        }
        Collections.sort(validRegistries, new Comparator<BuildProviderRegistry>() { // sort

                    @Override
                    public int compare(BuildProviderRegistry r1, BuildProviderRegistry r2) {
                        return r1.getOrder() - r2.getOrder();
                    }

                });

        List<AbstractBuildProvider> providers = new ArrayList<AbstractBuildProvider>();
        for (BuildProviderRegistry r : validRegistries) {
            providers.add(r.provider);
        }
        buildProviders = providers.toArray(new AbstractBuildProvider[0]);
    }

    void processBuildResourcesProviders() {
        final Collection<BuildResourcesProviderRegistry> registries = buildResourcesProvidersMap.values();

        // collect override
        // List<String> overrideIds=registries.stream().filter(r -> r.overrideId != null).map(r ->
        // r.overrideId).collect(Collectors.toList());
        List<String> overrideIds = new ArrayList<String>();
        for (BuildResourcesProviderRegistry r : registries) {
            if (r.overrideId != null) {
                overrideIds.add(r.overrideId);
            }
        }

        List<BuildResourcesProviderRegistry> validRegistries = new ArrayList<BuildResourcesProviderRegistry>();
        for (BuildResourcesProviderRegistry r : registries) {
            if (!overrideIds.contains(r.id)) { // filter override
                validRegistries.add(r);
            }
        }

        List<IBuildResourcesProvider> providers = new ArrayList<IBuildResourcesProvider>();
        for (BuildResourcesProviderRegistry r : validRegistries) {
            providers.add(r.provider);
        }
        buildResourcesProviders = providers.toArray(new IBuildResourcesProvider[0]);
    }

    IBuildExportDependenciesProvider[] getDependenciesProviders() {
        init();
        return dependenciesProviderArrays;
    }

    AbstractBuildProvider[] getBuildProviders() {
        init();
        return buildProviders != null ? buildProviders : new AbstractBuildProvider[0];
    }

    IBuildResourcesProvider[] getResourcesProviders() {
        init();
        return buildResourcesProviders != null ? buildResourcesProviders : new IBuildResourcesProvider[0];
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.utils.RegistryReader#readElement(org.eclipse.core.runtime.IConfigurationElement)
     */
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
        if ("buildProvider".equals(element.getName())) { //$NON-NLS-1$
            SafeRunner.run(new RegistryReader.RegistrySafeRunnable() {

                @Override
                public void run() throws Exception {
                    try {
                        BuildProviderRegistry resgistry = new BuildProviderRegistry();
                        resgistry.id = element.getAttribute("id"); //$NON-NLS-1$
                        resgistry.description = element.getAttribute("description"); //$NON-NLS-1$
                        resgistry.overrideId = element.getAttribute("override"); //$NON-NLS-1$

                        resgistry.provider = (AbstractBuildProvider) element.createExecutableExtension("provider"); //$NON-NLS-1$

                        // the build type can be empty for some case, like test case, only for pom creator, so no type.
                        // or will use it for all.
                        IConfigurationElement[] buidTypeChildren = element.getChildren("buildType"); //$NON-NLS-1$
                        if (buidTypeChildren != null && buidTypeChildren.length > 0) {
                            if (buidTypeChildren.length > 1) {
                                throw new IllegalArgumentException(
                                        "The build type should only can be set only one, can't support more.");
                            }

                            // build type
                            String buildTypeName = buidTypeChildren[0].getAttribute("name"); //$NON-NLS-1$
                            String buildTypeLabel = buidTypeChildren[0].getAttribute("label"); //$NON-NLS-1$

                            int order = 0;
                            String orderStr = buidTypeChildren[0].getAttribute("order"); //$NON-NLS-1$
                            if (orderStr != null && !orderStr.isEmpty()) {
                                order = Integer.parseInt(orderStr);
                            }
                            BuildType buildType = new BuildType(buildTypeName, buildTypeLabel, order);
                            resgistry.provider.buildType = buildType;
                        }

                        buildProvidersMap.put(resgistry.id, resgistry);
                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }
                }

            });
            return true;

        }
        if ("resourcesProvider".equals(element.getName())) { //$NON-NLS-1$
            SafeRunner.run(new RegistryReader.RegistrySafeRunnable() {

                @Override
                public void run() throws Exception {
                    try {
                        BuildResourcesProviderRegistry resgistry = new BuildResourcesProviderRegistry();
                        resgistry.id = element.getAttribute("id"); //$NON-NLS-1$
                        resgistry.description = element.getAttribute("description"); //$NON-NLS-1$
                        resgistry.overrideId = element.getAttribute("override"); //$NON-NLS-1$

                        resgistry.provider = (IBuildResourcesProvider) element.createExecutableExtension("provider"); //$NON-NLS-1$

                        buildResourcesProvidersMap.put(resgistry.id, resgistry);
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
