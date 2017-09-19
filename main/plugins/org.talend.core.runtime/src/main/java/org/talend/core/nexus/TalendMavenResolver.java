// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
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
import java.util.Dictionary;
import java.util.Hashtable;

import org.ops4j.pax.url.mvn.MavenResolver;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ManagedService;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.talend.core.runtime.CoreRuntimePlugin;

/**
 * created by wchen on Aug 3, 2017 Detailled comment
 *
 */
public class TalendMavenResolver {

    private static MavenResolver mavenResolver = null;

    /**
     * 
     * DOC wchen TalendMavenResolver constructor comment.
     */
    static {
        // the tracker is use in case the service is modifed
        final BundleContext context = CoreRuntimePlugin.getInstance().getBundle().getBundleContext();
        ServiceTracker<org.ops4j.pax.url.mvn.MavenResolver, org.ops4j.pax.url.mvn.MavenResolver> serviceTracker = new ServiceTracker<org.ops4j.pax.url.mvn.MavenResolver, org.ops4j.pax.url.mvn.MavenResolver>(
                context, org.ops4j.pax.url.mvn.MavenResolver.class,
                new ServiceTrackerCustomizer<org.ops4j.pax.url.mvn.MavenResolver, org.ops4j.pax.url.mvn.MavenResolver>() {

                    @Override
                    public org.ops4j.pax.url.mvn.MavenResolver addingService(
                            ServiceReference<org.ops4j.pax.url.mvn.MavenResolver> reference) {
                        return context.getService(reference);
                    }

                    @Override
                    public void modifiedService(ServiceReference<org.ops4j.pax.url.mvn.MavenResolver> reference,
                            org.ops4j.pax.url.mvn.MavenResolver service) {
                        mavenResolver = null;

                    }

                    @Override
                    public void removedService(ServiceReference<org.ops4j.pax.url.mvn.MavenResolver> reference,
                            org.ops4j.pax.url.mvn.MavenResolver service) {
                        mavenResolver = null;
                    }
                });
        serviceTracker.open();
    }

    public static void updateMavenResolver(Dictionary<String, String> props) throws Exception {
        if (props == null) {
            props = new Hashtable<String, String>();
        }
        final BundleContext context = CoreRuntimePlugin.getInstance().getBundle().getBundleContext();
        ServiceReference<ManagedService> managedServiceRef = context.getServiceReference(ManagedService.class);
        if (managedServiceRef != null) {
            ManagedService managedService = context.getService(managedServiceRef);

            managedService.updated(props);
            mavenResolver = null;
        } else {
            throw new RuntimeException("Failed to load the service :" + ManagedService.class.getCanonicalName()); //$NON-NLS-1$
        }

    }

    public static File resolve(String mvnUri) throws IOException, RuntimeException {
        return getMavenResolver().resolve(mvnUri);
    }

    public static void upload(String groupId, String artifactId, String classifier, String extension, String version,
            File artifact) throws IOException {
        getMavenResolver().upload(groupId, artifactId, classifier, extension, version, artifact);
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
}
