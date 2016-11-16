package org.talend.libraries.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class LibrariesUIPlugin extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.talend.libraries.ui"; //$NON-NLS-1$

    // The shared instance
    private static LibrariesUIPlugin plugin;

    /**
     * The constructor
     */
    public LibrariesUIPlugin() {
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static LibrariesUIPlugin getDefault() {
        return plugin;
    }

}
