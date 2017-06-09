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
package routines.system;

/**
 * Provides utility methods related to system environment. 
 */
public abstract class EnvironmentUtils {

    public static final String OS_NAME;

    static {
        OS_NAME = System.getProperty("os.name");
    }

    /**
     * Check whether this is Windows system.
     * 
     * @return {@code true} if this is Windows, {@code false} otherwise
     */
    public static boolean isWindows() {
        return matchOsNamePrefix("Windows");
    }
    
    /**
     * Check whether this is Linux system.
     * 
     * @return {@code true} if this is Linux, {@code false} otherwise
     */
    public static boolean isLinux() {
        return matchOsNamePrefix("Linux") || matchOsNamePrefix("LINUX");
    }
    
    /**
     * Check whether this is Mac or Mac OS X system.
     * 
     * @return {@code true} if this is Mac or Mac OS X, {@code false} otherwise
     */
    public static boolean isMacOs() {
        return matchOsNamePrefix("Mac");
    }
    
    /**
     * Check whether this is Linux or Unix system.
     * 
     * @return {@code true} if this is Linux or Unix, {@code false} otherwise
     */
    public static boolean isLinuxOrUnix() {
        return !isWindows() && !isMacOs();
    }
    
    private static boolean matchOsNamePrefix(String prefix) {
        return matchOsNamePrefix(OS_NAME, prefix);
    }

    private static boolean matchOsNamePrefix(String name, String prefix) {
        return name != null ? name.startsWith(prefix) : null;
    }

}
