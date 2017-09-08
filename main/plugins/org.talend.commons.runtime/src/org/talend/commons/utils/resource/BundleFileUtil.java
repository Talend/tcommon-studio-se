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
package org.talend.commons.utils.resource;

import java.io.File;
import java.io.IOException;
import java.net.URL;

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
}
