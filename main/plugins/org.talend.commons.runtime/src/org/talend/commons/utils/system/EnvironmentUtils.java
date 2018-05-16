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
package org.talend.commons.utils.system;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 */
public class EnvironmentUtils {

    public static boolean isWindowsSystem() {
        return getEnvOs().startsWith("Windows"); //$NON-NLS-1$
    }

    public static boolean isLinuxUnixSystem() {
        return !isWindowsSystem() && !isMacOsSytem();
    }

    /**
     * DOC amaumont Comment method "isMacOsSytem".
     * 
     * @return
     */
    public static boolean isMacOsSytem() {
        return getEnvOs().startsWith("Mac"); //$NON-NLS-1$ 
    }

    /**
     * DOC amaumont Comment method "getEnv".
     */
    public static String getEnvOs() {
        return System.getProperty("os.name"); //$NON-NLS-1$ 
    }

}
