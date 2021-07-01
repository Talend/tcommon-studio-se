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

import org.apache.commons.lang3.StringUtils;
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
     * 
     * @throws CoreException
     */
    public static void initializeJavaHome() throws CoreException {
        IEclipsePreferences pref = InstanceScope.INSTANCE.getNode(JavaRuntime.ID_PLUGIN); //$NON-NLS-1$
        String defaultVM = pref.get(JavaRuntime.PREF_VM_XML, ""); //$NON-NLS-1$//$NON-NLS-2$
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
        String jdkHome = System.getProperty("job.compilation.jvm"); //$NON-NLS-1$
        if (jdkHome == null || "".equals(jdkHome)) { //$NON-NLS-1$
            jdkHome = System.getProperty("jdk.home"); //$NON-NLS-1$
        }
        if (StringUtils.isNoneEmpty(jdkHome)) {
            File jvmFile = new File(jdkHome);
            if (jvmFile.exists()) {
                return getJDKPath(jvmFile);
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
