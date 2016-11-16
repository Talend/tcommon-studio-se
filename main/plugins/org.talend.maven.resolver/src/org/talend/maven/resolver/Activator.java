package org.talend.maven.resolver;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.talend.osgi.hook.maven.MavenResolver;

public class Activator implements BundleActivator {

    private static BundleContext context;

    static BundleContext getContext() {
        return context;
    }

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        Activator.context = bundleContext;
        context.registerService(MavenResolver.class.getCanonicalName(), new PaxMavenResolver(), null);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        Activator.context = null;
    }

}
