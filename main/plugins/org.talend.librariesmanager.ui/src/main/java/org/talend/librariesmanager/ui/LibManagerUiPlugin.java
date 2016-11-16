package org.talend.librariesmanager.ui;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.ILibrariesService;

public class LibManagerUiPlugin extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.talend.librariesmanager.ui"; //$NON-NLS-1$

    public static final Bundle BUNDLE = Platform.getBundle(PLUGIN_ID);

    // The shared instance
    private static LibManagerUiPlugin plugin;

    /**
     * The constructor
     */
    public LibManagerUiPlugin() {
        plugin = this;
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static LibManagerUiPlugin getDefault() {
        return plugin;
    }

    public ILibrariesService getLibrariesService() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibrariesService.class)) {
            return (ILibrariesService) GlobalServiceRegister.getDefault().getService(ILibrariesService.class);
        }
        return null;
    }
}
