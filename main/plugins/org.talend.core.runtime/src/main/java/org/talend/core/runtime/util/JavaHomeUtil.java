// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.runtime.util;

import java.io.File;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMInstall2;
import org.eclipse.jdt.launching.JavaRuntime;
import org.talend.commons.utils.generation.JavaUtils;

/**
 * created by nrousseau on Jun 13, 2015 Detailled comment
 *
 */
public class JavaHomeUtil {

    /**
     * Initialize Java Home to the preferences if needed only.<br>
     * This will take take first JDK8 if existing.<br>
     * If not, take JDK7.<br>
     * If no any JDK found, it will let eclipse set the default one.
     * 
     * @throws CoreException
     */
    public static void initializeJavaHome() throws CoreException {
        IEclipsePreferences pref = InstanceScope.INSTANCE.getNode("org.eclipse.jdt.launching"); //$NON-NLS-1$
        String defaultVM = pref.get("org.eclipse.jdt.launching.PREF_DEFAULT_ENVIRONMENTS_XML", ""); //$NON-NLS-1$//$NON-NLS-2$
        boolean needSetupJVM = false;
        if (!"".equals(defaultVM)) { //$NON-NLS-1$
            if (isSetJdkHomeVariable() && !getJDKHomeVariable().equals(getCurrentJavaHomeString())) {
                needSetupJVM = true;
            }
        } else {
            needSetupJVM = true;
        }
        if (needSetupJVM) {
            IVMInstall currentVM = JavaRuntime.getDefaultVMInstall();
            if (isSetJdkHomeVariable()) {
                if (currentVM != null) {
                    File installLocation = new File(getJDKHomeVariable());
                    currentVM.setInstallLocation(installLocation);
                    currentVM.setName(installLocation.getName());
                }
            }
        }
    }
    /**
     * Only for TUJ to setup JDK version
     * Should invoke after initializeJavaHome()
     */
    public static String getSpecifiedJavaVersion() {
        if (isSetJdkHomeVariable()) {
            IVMInstall currentVM = JavaRuntime.getDefaultVMInstall();
            if (currentVM instanceof IVMInstall2) {
                return JavaUtils.getCompilerCompliance((IVMInstall2) currentVM, null);
            }
        }
        return null;
    }

    public static boolean isSetJdkHomeVariable() {
        String jdkHomeValue = getJDKHomeVariable();
        return jdkHomeValue != null && !"".equals(jdkHomeValue); //$NON-NLS-1$
    }

    public static String getJDKHomeVariable() {
        String jdkHome = System.getProperty("java.home"); //$NON-NLS-1$
        if (jdkHome == null || "".equals(jdkHome)) { //$NON-NLS-1$
            jdkHome = System.getProperty("jdk.home"); //$NON-NLS-1$
        }

        if (jdkHome == null || "".equals(jdkHome)) { //$NON-NLS-1$
            jdkHome = getJDKHomeFromEclipseVm();
        }

        if (jdkHome == null || "".equals(jdkHome)) { //$NON-NLS-1$
            jdkHome = System.getenv("JAVA_HOME"); //$NON-NLS-1$
        }
        return jdkHome;
    }

    private static String getJDKHomeFromEclipseVm() {
        String eclipseVm = System.getProperty("eclipse.vm"); //$NON-NLS-1$
        if (eclipseVm != null && !"".equals(eclipseVm)) {
            File javaexe = new File(eclipseVm);
            if (javaexe.exists()) {
                String jdk = getJDKPath(javaexe);
                return jdk;
            }

        }
        return null;
    }

    private static String getJDKPath(File file) {
        if (file == null) {
            return null;
        }
        if ("bin".equals(file.getName())) {//$NON-NLS-1$
            return file.getParent();
        } else {
            return getJDKPath(file.getParentFile());
        }
    }

    public static File getCurrentJavaHomeFile() {
        IVMInstall currentVM = JavaRuntime.getDefaultVMInstall();
        if (currentVM == null) {
            return null;
        }
        return currentVM.getInstallLocation();
    }

    public static String getCurrentJavaHomeString() {
        IVMInstall currentVM = JavaRuntime.getDefaultVMInstall();
        if (currentVM == null) {
            return null;
        }
        return currentVM.getInstallLocation().getAbsolutePath();
    }
}
