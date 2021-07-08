// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.nexus;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.m2e.core.MavenPlugin;
import org.ops4j.pax.url.mvn.MavenResolver;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ManagedService;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.network.IProxySelectorProvider;
import org.talend.commons.utils.network.TalendProxySelector;
import org.talend.core.runtime.CoreRuntimePlugin;

/**
 * created by wchen on Aug 3, 2017 Detailled comment
 *
 */
public class TalendMavenResolver {

    public static final String SOFTWARE_UPDATE_RESOLVER = "talend-software-update";

    public static final String TALEND_DEFAULT_LIBRARIES_RESOLVER = "talend-default-libraries-resolver";

    public static final String TALEND_ARTIFACT_LIBRARIES_RESOLVER = "talend-custom-artifact-repository";

    public static final String COMPONENT_MANANGER_RESOLVER = "component_mananger";

    private static String talendResolverKey = "";

    private static MavenResolver mavenResolver = null;

    private static final String MVN_USER_SETTING_KEY = "org.ops4j.pax.url.mvn.settings";

    public static void updateMavenResolver(String resolverKey, Dictionary<String, String> props) throws Exception {
        if (!needUpdate(resolverKey)) {
            return;
        }
        if (props == null) {
            props = new Hashtable<String, String>();
        }
        // https://jira.talendforge.org/browse/TUP-26752
        String configFile = props.get(MVN_USER_SETTING_KEY);
        if (configFile == null || configFile.trim().isEmpty()) {
            // set existing user settings file
            String studioUserSettingsFile = MavenPlugin.getMavenConfiguration().getUserSettingsFile();
            props.put(MVN_USER_SETTING_KEY, studioUserSettingsFile);
        }
        final BundleContext context = CoreRuntimePlugin.getInstance().getBundle().getBundleContext();
        Collection<ServiceReference<ManagedService>> managedServiceRefs = context.getServiceReferences(ManagedService.class,
                "(service.pid=org.ops4j.pax.url.mvn)");
        for (ServiceReference<ManagedService> managedServiceRef : managedServiceRefs) {
            if (managedServiceRef != null) {
                ManagedService managedService = context.getService(managedServiceRef);

                managedService.updated(props);
                talendResolverKey = resolverKey;
                mavenResolver = null;
            } else {
                throw new RuntimeException("Failed to load the service :" + ManagedService.class.getCanonicalName()); //$NON-NLS-1$
            }
        }

    }

    public static File resolve(String mvnUri) throws IOException {
        TalendProxySelector selectorInstance = null;
        IProxySelectorProvider proxySelector = null;
        try {
            try {
                selectorInstance = TalendProxySelector.getInstance();
                proxySelector = selectorInstance.createDefaultProxySelectorProvider();
                if (proxySelector != null) {
                    selectorInstance.addProxySelectorProvider(proxySelector);
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
            return getMavenResolver().resolve(mvnUri);
        } catch (IOException e) {
            throw ResolverExceptionHandler.hideCredential(e);
        } finally {
            if (proxySelector != null && selectorInstance != null) {
                selectorInstance.removeProxySelectorProvider(proxySelector);
            }
        }
    }

    public static void upload(String groupId, String artifactId, String classifier, String extension, String version,
            File artifact) throws IOException {
        getMavenResolver().upload(groupId, artifactId, classifier, extension, version, artifact);
    }

    public static void initMavenResovler() throws RuntimeException {
        getMavenResolver();
    }

    public static MavenResolver getMavenResolver() throws RuntimeException {
        if (mavenResolver == null) {
            final BundleContext context = CoreRuntimePlugin.getInstance().getBundle().getBundleContext();
            ServiceReference<org.ops4j.pax.url.mvn.MavenResolver> mavenResolverService = context
                    .getServiceReference(org.ops4j.pax.url.mvn.MavenResolver.class);
            if (mavenResolverService != null) {
                mavenResolver = context.getService(mavenResolverService);
            } else {
                throw new RuntimeException("Unable to acquire org.ops4j.pax.url.mvn.MavenResolver");
            }
        }

        return mavenResolver;
    }

    public static boolean needUpdate(String resolverKey) {
        if (resolverKey == null || talendResolverKey.equals(resolverKey)) {
            return false;
        }
        return true;
    }
}
