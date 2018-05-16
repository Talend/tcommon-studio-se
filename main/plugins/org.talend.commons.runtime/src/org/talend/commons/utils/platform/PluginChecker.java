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
package org.talend.commons.utils.platform;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public final class PluginChecker {

    private static final String TDQ_ID = "org.talend.dataprofiler.core.tdq"; //$NON-NLS-1$

    private static final String TDCP_ID = "org.talend.datacleansing.core"; //$NON-NLS-1$

    private static final String TOP_BRANDING = "org.talend.rcp.branding.top"; //$NON-NLS-1$

    private PluginChecker() {
    }

    /**
     * Check if specific plug-in is loaded.
     * 
     * @return isLoaded
     */
    public static boolean isPluginLoaded(String pluginID) {
        boolean isLoaded = true;
        Bundle bundle = Platform.getBundle(pluginID);
        if (bundle == null || (bundle != null && bundle.getState() == Bundle.UNINSTALLED)) {
            isLoaded = false;
        }
        return isLoaded;
    }

    /**
     * DOC bZhou Comment method "isTDQLoaded".
     * 
     * use it to test if the platform start by TDQ.
     * 
     * @return
     */
    public static boolean isTDQLoaded() {
        return isPluginLoaded(TDQ_ID);
    }

    /**
     * DOC bZhou Comment method "isTDCPLoaded".
     * 
     * use it to test if the platform start by TDCP.
     * 
     * @return
     */
    public static boolean isTDCPLoaded() {
        return isPluginLoaded(TDCP_ID);
    }

    /**
     * Method "isOnlyTopLoaded".
     * 
     * @return true when TOP is used standalone.
     */
    public static boolean isOnlyTopLoaded() {
        return isPluginLoaded(TOP_BRANDING) && !isTDCPLoaded() && !isTDQLoaded();
    }
}
