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
package org.talend.maven.resolver;

import java.io.File;
import java.io.IOException;

import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.talend.osgi.hook.maven.MavenResolver;

/**
 * created by sgandon on 27 mai 2015 Detailled comment
 *
 */
final class PaxMavenResolver implements MavenResolver {

    private org.ops4j.pax.url.mvn.MavenResolver paxResolver;

    @Override
    public File resolve(String mavenURI) throws IOException {
        if (paxResolver == null) {
            ServiceReference<org.ops4j.pax.url.mvn.MavenResolver> mavenResolverService = Activator.getContext()
                    .getServiceReference( org.ops4j.pax.url.mvn.MavenResolver.class );
            if (mavenResolverService != null) {
                paxResolver = Activator.getContext().getService( mavenResolverService );
                // the tracker is use in case the service is modifed which happens a lot during the Unit tests.
                ServiceTracker<org.ops4j.pax.url.mvn.MavenResolver, org.ops4j.pax.url.mvn.MavenResolver> serviceTracker = new ServiceTracker<org.ops4j.pax.url.mvn.MavenResolver, org.ops4j.pax.url.mvn.MavenResolver>(
                        Activator.getContext(), org.ops4j.pax.url.mvn.MavenResolver.class,
                        new ServiceTrackerCustomizer<org.ops4j.pax.url.mvn.MavenResolver, org.ops4j.pax.url.mvn.MavenResolver>() {

                            @Override
                            public org.ops4j.pax.url.mvn.MavenResolver addingService(
                                    ServiceReference<org.ops4j.pax.url.mvn.MavenResolver> reference) {
                                return Activator.getContext().getService( reference );
                            }

                            @Override
                            public void modifiedService(ServiceReference<org.ops4j.pax.url.mvn.MavenResolver> reference,
                                    org.ops4j.pax.url.mvn.MavenResolver service) {
                                paxResolver = null;

                            }

                            @Override
                            public void removedService(ServiceReference<org.ops4j.pax.url.mvn.MavenResolver> reference,
                                    org.ops4j.pax.url.mvn.MavenResolver service) {
                                paxResolver = null;
                            }
                        } );
                serviceTracker.open();
            } else {
                throw new RuntimeException(
                        "Failed to load the service :" + org.ops4j.pax.url.mvn.MavenResolver.class.getCanonicalName() ); //$NON-NLS-1$
            }
        }// else already loaded.
        return paxResolver.resolve( mavenURI );
    }
}