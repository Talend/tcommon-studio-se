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
package org.talend.commons.utils.resource;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * DOC ggu class global comment. Detailled comment
 */
public final class BundleFileUtil {

    public static File getBundleFile(Class<?> bundleClass, String bundlePath) throws IOException {
        if (bundleClass == null) {
            return null;
        }
        return getBundleFile(FrameworkUtil.getBundle(bundleClass), bundlePath);
    }

    public static File getBundleFile(Bundle bundle, String bundlePath) throws IOException {
        if (bundle == null) {
            return null;
        }
        URL url = FileLocator.find(bundle, new Path(bundlePath), null);
        if (url != null) {
            url = FileLocator.toFileURL(url);
        }
        if (url != null) {
            File file = new File(url.getFile());
            if (file.exists()) {
                return file;
            }
        }
        return null;
    }

    public static Manifest getManifest(File f) throws IOException {
        if (f == null || !f.exists() || !f.isFile() || !f.getName().endsWith(FileExtensions.JAR_FILE_SUFFIX)) {
            return null;
        }
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(f);
            return jarFile.getManifest();
        } finally {
            if (jarFile != null) {
                try {
                    jarFile.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static String getBundleSymbolicName(Manifest manifest) {
        if (manifest == null) {
            return null;
        }
        Attributes mainAttributes = manifest.getMainAttributes();
        if (mainAttributes == null) {
            return null;
        }
        String name = mainAttributes.getValue("Bundle-SymbolicName"); //$NON-NLS-1$
        if (name == null) {
            return null;
        }
        final int indexOf = name.indexOf(';');
        if (indexOf > 0)
            name = name.substring(0, indexOf);
        return name;
    }

    public static String getBundleVersion(Manifest manifest) {
        if (manifest == null) {
            return null;
        }
        Attributes mainAttributes = manifest.getMainAttributes();
        if (mainAttributes == null) {
            return null;
        }
        return mainAttributes.getValue("Bundle-Version"); //$NON-NLS-1$
    }

    public static String[] getBundleClassPath(Manifest manifest) {
        if (manifest == null) {
            return new String[0];
        }
        Attributes mainAttributes = manifest.getMainAttributes();
        if (mainAttributes == null) {
            return new String[0];
        }

        String value = mainAttributes.getValue("Bundle-ClassPath"); //$NON-NLS-1$
        if (value == null) {
            return new String[0];
        }

        List<String> bundleCPs = new ArrayList<String>();
        final String[] entries = value.split(","); //$NON-NLS-1$
        for (String entry : entries) {
            bundleCPs.add(entry);
        }
        return bundleCPs.toArray(new String[0]);
    }

    public static boolean isInBundleClassPath(Manifest manifest, String libPath) {
        if (StringUtils.isBlank(libPath)) {
            return false;
        }
        final String[] bundleClassPath = getBundleClassPath(manifest);
        if (bundleClassPath != null && bundleClassPath.length > 0) {
            for (String cp : bundleClassPath) {
                if (cp.equals(libPath)) {
                    return true;
                }
            }
        }
        return false;
    }
}
