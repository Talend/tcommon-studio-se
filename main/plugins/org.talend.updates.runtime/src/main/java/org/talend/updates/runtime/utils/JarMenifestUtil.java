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
import java.util.jar.Manifest;

import org.talend.commons.utils.resource.BundleFileUtil;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class JarMenifestUtil {

    public static Manifest getManifest(File f) throws IOException {
        return BundleFileUtil.getManifest(f);
    }

    public static String getBundleSymbolicName(Manifest manifest) {
        return BundleFileUtil.getBundleSymbolicName(manifest);
    }

    public static String getBundleVersion(Manifest manifest) {
        return BundleFileUtil.getBundleVersion(manifest);
    }
}
