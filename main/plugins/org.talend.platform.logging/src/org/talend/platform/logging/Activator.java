package org.talend.platform.logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
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

    private static String version = null;// studio version

    /**
     * The constructor
     */
    public Activator() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    @SuppressWarnings("unchecked")
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        version = null;
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

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        version = null;
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
        if (version == null) {
            try {
                File file = null;
                try {
                    file = new File(Platform.getInstallLocation().getDataArea(".eclipseproduct").getPath());
                } catch (IOException e1) {
                    //
                }
                Properties prop = new Properties();
                if (file != null && file.exists()) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        prop.load(fis);
                    } catch (Exception e) {
                        //
                    }
                }
                version = prop.getProperty("version");

                if (StringUtils.isEmpty(version)) {
                    version = System.getProperty("talend.studio.version"); //$NON-NLS-1$
                }

                if (StringUtils.isEmpty(version)) {
                    Bundle b = Platform.getBundle("org.talend.commons.runtime");
                    if (b != null) {
                        version = b.getHeaders().get(org.osgi.framework.Constants.BUNDLE_VERSION);
                    }
                }
            } catch (Exception e) {
                //
            }

            if (version == null || "".equals(version.trim())) { //$NON-NLS-1$
                version = (String) getDefault().getBundle().getHeaders().get(org.osgi.framework.Constants.BUNDLE_VERSION);
            }
        }

        return version;
    }
}
