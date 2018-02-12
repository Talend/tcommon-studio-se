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
package org.talend.commons.runtime.debug;

import org.eclipse.osgi.service.debug.DebugOptions;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.CommonExceptionHandler;

/**
 * ggu class global comment. Detailled comment
 * 
 * add the function for debug "--talendDebug"
 */
public final class TalendDebugHandler {

    public static final String TALEND_DEBUG = "--talendDebug"; //$NON-NLS-1$

    /**
     * 
     * ggu Comment method "debug".
     * 
     * @param e
     */
    public static void debug(Throwable e) {
        debug(e, true, true);
    }

    public static void debug(Throwable e, boolean console, boolean log) {
        if (!CommonsPlugin.isDebugMode() || e == null) {
            return;
        }
        if (console && isEclipseDebug()) {
            e.printStackTrace(System.err);
        }
        if (log) {
            CommonExceptionHandler.process(e);
        }

    }

    /**
     * 
     * ggu Comment method "debug".
     * 
     * @param messages
     */
    public static void debug(String messages) {
        debug(messages, true, true);
    }

    public static void debug(String messages, boolean console, boolean log) {
        if (!CommonsPlugin.isDebugMode()) {
            return;
        }
        if (console) {
            System.out.println(messages);
        }
        if (log) {
            CommonExceptionHandler.log(messages);
        }
    }

    @SuppressWarnings("restriction")
    public static boolean isEclipseDebug() {
        boolean debuggerEnabled = false;
        BundleContext bundleContext = FrameworkUtil.getBundle(TalendDebugHandler.class).getBundleContext();
        if (bundleContext == null) {
            return false;
        }
        ServiceReference<DebugOptions> debugOptSR = bundleContext.getServiceReference(DebugOptions.class);
        if (debugOptSR != null) {
            DebugOptions debugOpt = bundleContext.getService(debugOptSR);
            debuggerEnabled = debugOpt.isDebugEnabled();
        }// else return false;
        return debuggerEnabled;
    }
}
