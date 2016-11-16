package org.talend.platform.logging;

import org.apache.log4j.Logger;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.talend.utils.format.PresentableBox;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    private static Logger log = Logger.getLogger(Activator.class);

    // The plug-in ID
    public static final String PLUGIN_ID = "org.talend.platform.logging"; //$NON-NLS-1$

    // The shared instance
    private static Activator plugin;

    /**
     * The constructor
     */
    public Activator() {
    }

    @SuppressWarnings("unchecked")
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        if (log.isInfoEnabled()) {
            Object version = getVersion();
            String mess = "Starting Talend's platform log system."; //$NON-NLS-1$
            if (version != null) {
                mess += ("VERSION= " + version); //$NON-NLS-1$
            }
            PresentableBox box = new PresentableBox("TALEND", mess, 0); //$NON-NLS-1$
            log.info(box.getFullBox());
        }
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
    public static Activator getDefault() {
        return plugin;
    }

    public static String getVersion() {
        String version = System.getProperty("talend.studio.version"); //$NON-NLS-1$
        if (version == null || "".equals(version.trim())) { //$NON-NLS-1$
            version = (String) getDefault().getBundle().getHeaders().get(org.osgi.framework.Constants.BUNDLE_VERSION);
        }
        return version;
    }
}
