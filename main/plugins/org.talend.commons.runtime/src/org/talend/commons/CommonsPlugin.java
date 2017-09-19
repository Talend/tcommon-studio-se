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
package org.talend.commons;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.talend.commons.exception.ExceptionService;
import org.talend.commons.runtime.debug.TalendDebugHandler;

/**
 * Activator for Code Generator.
 * 
 * $Id$
 * 
 */
public class CommonsPlugin implements BundleActivator {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.talend.commons.runtime"; //$NON-NLS-1$

    // The shared instance
    private static CommonsPlugin plugin;

    private static boolean headless = false;

    private static boolean storeLibsInWorkspace = false;

    private static boolean isSameProjectLogonCommline = false;

    private static boolean useCommandLineRepository = false;

    private static boolean isWorkbenchCreated = false;

    // TESB-17856: For commandline builds ESB Micorservice bundle
    private static boolean isESBMicorservice = false;

    public static boolean isWorkbenchCreated() {
        return isWorkbenchCreated;
    }

    public static void setWorkbenchCreated(boolean isWorkbenchCreated) {
        CommonsPlugin.isWorkbenchCreated = isWorkbenchCreated;
    }

    public static boolean isStoreLibsInWorkspace() {
        return storeLibsInWorkspace;
    }

    public static void setStoreLibsInWorkspace(boolean storeLibsInWorkspace) {
        CommonsPlugin.storeLibsInWorkspace = storeLibsInWorkspace;
    }

    public static boolean isUseCommandLineRepository() {
        return useCommandLineRepository;
    }

    public static void setUseCommandLineRepository(boolean useCommandLineRepository) {
        CommonsPlugin.useCommandLineRepository = useCommandLineRepository;
    }

    private ExceptionService service;

    private BundleContext context;

    /**
     * Default Constructor.
     */
    public CommonsPlugin() {
        plugin = this;
    }

    public static CommonsPlugin getDefault() {
        return plugin;
    }

    public static boolean isHeadless() {
        return headless;
    }

    public static void setHeadless(boolean headless) {
        CommonsPlugin.headless = headless;
    }

    public static boolean isDebugMode() {
        return ArrayUtils.contains(Platform.getApplicationArgs(), TalendDebugHandler.TALEND_DEBUG);
    }
    
    public static boolean isJUnitTest() {
        return Boolean.getBoolean("junit_test"); //$NON-NLS-1$
    }


    /**
     * Answer the file associated with name. This handles the case of running as a plugin and running standalone which
     * happens during testing.
     * 
     * @param filename
     * @return File
     */
    public static InputStream getFileInputStream(String filename) throws IOException {
        URL url = Platform.getBundle(PLUGIN_ID).getEntry(filename);
        return url != null ? url.openStream() : null;

    }

    public static boolean isSameProjectLogonCommline() {
        return isSameProjectLogonCommline;
    }

    public static void setSameProjectLogonCommline(boolean isSameProjectLogonCommline) {
        CommonsPlugin.isSameProjectLogonCommline = isSameProjectLogonCommline;
    }

    @Override
    public void start(BundleContext context) throws Exception {
        this.context = context;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
    }

    public ExceptionService getExceptionService() {
        if (service == null) {
            ServiceReference sr = context.getServiceReference(ExceptionService.class.getName());
            if (sr != null) {
                service = (ExceptionService) context.getService(sr);
            }
        }
        return service;
    }

    public static boolean isESBMicorservice() {
        return isESBMicorservice;
    }

    public static void setESBMicorservice(boolean isESBMicorservice) {
        CommonsPlugin.isESBMicorservice = isESBMicorservice;
    }

}
