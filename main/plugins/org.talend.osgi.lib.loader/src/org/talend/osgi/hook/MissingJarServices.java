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
package org.talend.osgi.hook;

import java.io.File;

import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.talend.osgi.configurator.OsgiLoaderActivator;
import org.talend.osgi.hook.maven.MavenResolver;
import org.talend.osgi.hook.notification.JarMissingObservable;

/**
 * created by sgandon on 17 oct. 2014 Detailled comment
 */
/**
 * created by sgandon on 27 mai 2015 Detailled comment
 *
 */
/**
 * created by sgandon on 27 mai 2015 Detailled comment
 *
 */
public class MissingJarServices {

    static final boolean DEBUG = false;// we canot use the PDE debug feature cause we are a osgi fragment loaded way
                                       // before eclipse stuff.

    static public File javaLibFolder;

    /**
     * return the folder where to find the missing libraries or null if none was defined
     * */
    static public File getLibJavaFolderFile() {
        if (javaLibFolder == null) {
            String property = System.getProperty(OsgiLoaderActivator.ORG_TALEND_EXTERNAL_LIB_FOLDER_SYS_PROP);
            if (property != null) {
                javaLibFolder = new File(property);
            }// else keep javaLibFolder null
        }
        return javaLibFolder;

    }

    private static JarMissingObservable jarMissingObservable;// do not use this use the getter bellow

    public static JarMissingObservable getJarMissingObservable() {
        if (jarMissingObservable == null) {
            ServiceReference<JarMissingObservable> jarMissObsServRef = OsgiLoaderActivator.getBundleContext()
                    .getServiceReference(JarMissingObservable.class);
            if (jarMissObsServRef != null) {
                jarMissingObservable = OsgiLoaderActivator.getBundleContext().getService(jarMissObsServRef);
            } else {// should not happend caus the serive is registered by this bundle activator.
                throw new RuntimeException("Failed to load the service :" + JarMissingObservable.class.getCanonicalName()); //$NON-NLS-1$
            }
        }
        return jarMissingObservable;
    }

    private static MavenResolver mavenResolver;

    /**
     * get the MavenResolver service
     * 
     * @return the resolver if found or null if not fould
     */
    public static MavenResolver getMavenResolver() {
        if (mavenResolver == null) {
            ServiceReference<MavenResolver> jarMissObsServRef = OsgiLoaderActivator.getBundleContext().getServiceReference(
                    MavenResolver.class);
            if (jarMissObsServRef != null) {
                mavenResolver = OsgiLoaderActivator.getBundleContext().getService(jarMissObsServRef);
            } // else { //after opened workbench, the maven resolver will be registered and use it. so no need catch.
        }
        return mavenResolver;
    }

    private static LogService logService;// do not use this use the getter bellow

    static public LogService getLogService() {
        ServiceReference<LogService> logServRef = OsgiLoaderActivator.getBundleContext().getServiceReference(LogService.class);
        if (logServRef != null) {
            logService = OsgiLoaderActivator.getBundleContext().getService(logServRef);
        }// else log is set to null
        return logService;
    }

    static public void logDebugInfo(String message) {
        if (DEBUG) {
            LogService log = getLogService();
            if (log != null) {
                log.log(LogService.LOG_ERROR, message);

            }
        }
    }
}
