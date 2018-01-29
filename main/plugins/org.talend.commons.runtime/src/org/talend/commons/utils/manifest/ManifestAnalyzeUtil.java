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
package org.talend.commons.utils.manifest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

/**
 * created by ycbai on 2013-3-18 Detailled comment
 * 
 */
public class ManifestAnalyzeUtil {

    public final static String IMPLEMENTATION_TITLE = "Implementation-Title"; //$NON-NLS-1$

    public final static String IMPLEMENTATION_VENDOR = "Implementation-Vendor"; //$NON-NLS-1$

    public final static String IMPLEMENTATION_VENDOR_ID = "Implementation-Vendor-Id"; //$NON-NLS-1$

    public final static String IMPLEMENTATION_VERSION = "Implementation-Version"; //$NON-NLS-1$

    public final static String SPECIFICATION_TITLE = "Specification-Title"; //$NON-NLS-1$

    public final static String SPECIFICATION_VENDOR = "Specification-Vendor"; //$NON-NLS-1$

    public final static String SPECIFICATION_VERSION = "Specification-Version"; //$NON-NLS-1$

    public final static String BUNDLE_NAME = "Bundle-Name"; //$NON-NLS-1$

    public final static String BUNDLE_VENDOR = "Bundle-Vendor"; //$NON-NLS-1$

    public final static String BUNDLE_VERSION = "Bundle-Version"; //$NON-NLS-1$

    public final static String BUNDLE_SYMBOLICNAME = "Bundle-SymbolicName"; //$NON-NLS-1$

    private static Manifest getManifest(InputStream stream) throws IOException {
        JarInputStream jarStream = new JarInputStream(stream);
        return jarStream.getManifest();
    }

    private static Manifest getManifest(URL url) throws IOException {
        return getManifest(url.openStream());
    }

    public static String getManifestAttributeValue(URL url, String key) throws IOException {
        String value = null;
        Manifest manifest = getManifest(url);
        if (manifest != null) {
            Attributes attributes = manifest.getMainAttributes();
            if (attributes != null) {
                value = attributes.getValue(key);
            }
            if (value == null) {
                for (String entry : manifest.getEntries().keySet()) {
                    Attributes attr = manifest.getEntries().get(entry);
                    value = attr.getValue(key);
                }
            }
        }

        return value;
    }

}
