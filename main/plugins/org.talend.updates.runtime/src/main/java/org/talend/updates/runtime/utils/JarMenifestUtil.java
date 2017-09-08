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
package org.talend.updates.runtime.utils;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.talend.commons.utils.resource.FileExtensions;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class JarMenifestUtil {

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
        String name = manifest.getMainAttributes().getValue("Bundle-SymbolicName"); //$NON-NLS-1$
        if (name != null) {
            final int indexOf = name.indexOf(';');
            if (indexOf > 0)
                name = name.substring(0, indexOf);
            return name;
        }
        return null;
    }

    public static String getBundleVersion(Manifest manifest) {
        return manifest.getMainAttributes().getValue("Bundle-Version"); //$NON-NLS-1$
    }
}
