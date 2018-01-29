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
package org.talend.registration;

import org.eclipse.core.net.proxy.IProxyService;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.talend.core.context.Context;
import org.talend.core.runtime.CoreRuntimePlugin;

/**
 * DOC zli class global comment. Detailled comment
 */
public class RegistrationPlugin extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.talend.registration"; //$NON-NLS-1$

    // The shared instance
    private static RegistrationPlugin plugin;

    private ServiceTracker proxyTracker;

    public RegistrationPlugin() {
        plugin = this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    public static RegistrationPlugin getDefault() {
        return plugin;
    }

    /**
     * Getter for context.
     * 
     * @return the context
     */
    public Context getContext() {
        return CoreRuntimePlugin.getInstance().getContext();
    }

    public IProxyService getProxyService() {
        if (proxyTracker == null) {
            proxyTracker = new ServiceTracker(getBundle().getBundleContext(), IProxyService.class.getName(), null);
            proxyTracker.open();
        }
        return (IProxyService) proxyTracker.getService();
    }

}
