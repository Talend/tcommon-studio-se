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
package org.talend.core.model.utils;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public final class TalendPropertiesUtil {

    public static boolean isEnabled(String key) {
        String value = System.getProperty(key);
        return Boolean.parseBoolean(value);
    }

    /**
     * 
     * DOC ggu Comment method "isHideExchange".
     * 
     * 
     * @return
     */
    public static boolean isHideExchange() {
        return isEnabled("talend.hide.exchange"); //$NON-NLS-1$
    }

    /**
     * 
     * DOC ggu Comment method "isHideBuildNumber".
     * 
     * @return
     */
    public static boolean isHideBuildNumber() {
        return isEnabled("talend.hide.buildNumber"); //$NON-NLS-1$
    }

    /**
     * 
     * DOC ggu Comment method "isEnabledMultiBranchesInWorkspacee".
     * 
     * @return
     */
    public static boolean isEnabledMultiBranchesInWorkspace() {
        return isEnabled("talend.enable.multiBranchesInWorkspace"); //$NON-NLS-1$
    }

    /**
     * 
     * DOC ggu Comment method "isEnabledMultiBranchesInWorkspacee".
     * 
     * @return
     */
    public static boolean isCleanCache() {
        return isEnabled("talend.clean.cache"); //$NON-NLS-1$
    }

    public static boolean isEnabledCsvFormat4Exchange() {
        return isEnabled("talend.exchange.csv"); //$NON-NLS-1$
    }

    public static boolean isEnabledUseBrowser() {
        return isEnabled("USE_BROWSER"); //$NON-NLS-1$
    }

}
