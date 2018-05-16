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
package org.talend.osgi.configurator;

import java.io.File;
import java.net.URISyntaxException;

import org.eclipse.osgi.service.datalocation.Location;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;
import org.talend.osgi.hook.URIUtil;
import org.talend.osgi.hook.notification.JarMissingObservable;

/**
 * register the jarMissingObservable service.
 *
 */
public class OsgiLoaderActivator implements BundleActivator {

    /**
     * the system property that contains the folder where external libas are stored
     */
    public static final String ORG_TALEND_EXTERNAL_LIB_FOLDER_SYS_PROP = "talend.library.path"; //$NON-NLS-1$

    /**
     * default subfolder path to look for missing jars
     */
    private static final String LIB_JAVA_SUB_FOLDER = "lib/java"; //$NON-NLS-1$

    private JarMissingObservable jarMissingObservable;

    private static BundleContext context;

    public static BundleContext getBundleContext() {
        return context;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    @Override
    public void start(BundleContext theContext) throws Exception {
        OsgiLoaderActivator.context = theContext;
        jarMissingObservable = new JarMissingObservable();
        context.registerService(JarMissingObservable.class.getCanonicalName(), jarMissingObservable, null);
        storeTalendLibFolder(context);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop(BundleContext theContext) throws Exception {
        // nothing to do, service should be un-registered when bundle is stopping automatically.
    }

    /**
     * DOC sgandon Comment method "storeTalendLibFolder".
     * 
     * @param context
     */
    private void storeTalendLibFolder(BundleContext theContext) {
        try {
            File talendLibFolder = getLibJavaFolderFile(theContext);
            System.setProperty(ORG_TALEND_EXTERNAL_LIB_FOLDER_SYS_PROP, talendLibFolder.getAbsolutePath());
        } catch (URISyntaxException e) {
            ServiceReference<LogService> logServRef = OsgiLoaderActivator.getBundleContext()
                    .getServiceReference(LogService.class);
            if (logServRef != null) {
                LogService logService = OsgiLoaderActivator.getBundleContext().getService(logServRef);
                logService.log(LogService.LOG_ERROR, "failed to initialize [" //$NON-NLS-1$ 
                        + ORG_TALEND_EXTERNAL_LIB_FOLDER_SYS_PROP + "]"); //$NON-NLS-1$
            } else {
                System.err.println("Failed to initialise the system property [" + ORG_TALEND_EXTERNAL_LIB_FOLDER_SYS_PROP + "]"); //$NON-NLS-1$//$NON-NLS-2$
                e.printStackTrace(System.err);// using syso caus I do not know how to get the loggin instance.
            }
        }
    }

    /**
     * return the folder where to find the missing libraries
     * 
     * @param theContext
     * */
    protected File getLibJavaFolderFile(BundleContext theContext) throws URISyntaxException {
        String libFolderSysProp = System.getProperty(ORG_TALEND_EXTERNAL_LIB_FOLDER_SYS_PROP);
        if (libFolderSysProp != null) {
            return new File(libFolderSysProp);
        } else {
            Location installLocation = getConfigurationLocation(theContext);
            File installFolder = URIUtil.toFile(URIUtil.toURI(installLocation.getURL()));
            return new File(installFolder, System.getProperty("org.talend.lib.subfolder", LIB_JAVA_SUB_FOLDER)); //$NON-NLS-1$
        }

    }

    /**
     * return the eclipse configuration location
     * 
     * @param bundleContext
     */
    private Location getConfigurationLocation(BundleContext bundleContext) {
        Filter filter = null;
        try {
            filter = bundleContext.createFilter(Location.CONFIGURATION_FILTER);
        } catch (InvalidSyntaxException e) {
            // ignore this. It should never happen as we have tested the above format.
        }
        ServiceTracker configurationLocation = new ServiceTracker(bundleContext, filter, null);
        configurationLocation.open();
        return (Location) configurationLocation.getService();
    }
}
