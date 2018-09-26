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
package org.talend.commons.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.URIUtil;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.talend.commons.i18n.internal.Messages;

/**
 * Represents a version. Contents a major and a minor version.<br/>
 * 
 * $Id: Version.java 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class VersionUtils {

    public static final String DEFAULT_VERSION = "0.1"; //$NON-NLS-1$

    public static final String STUDIO_VERSION_PROP = "studio.version"; //$NON-NLS-1$

    public static final String TALEND_VERSION_PROP = "talend.version"; //$NON-NLS-1$

    private static final String COMMONS_PLUGIN_ID = "org.talend.commons.runtime"; //$NON-NLS-1$

    private static final String TALEND_PROPERTIES_FILE = "/talend.properties"; //$NON-NLS-1$

    private static Logger log = Logger.getLogger(VersionUtils.class);

    private static String talendVersion;

    private static String productVersion;

    public static int compareTo(String arg0, String arg1) {
        return new Version(arg0).compareTo(new Version(arg1));
    }

    public static String upMinor(String version) {
        Version toReturn = new Version(version);
        toReturn.upMinor();
        return toReturn.toString();
    }

    public static String upMajor(String version) {
        Version toReturn = new Version(version);
        toReturn.upMajor();
        return toReturn.toString();
    }

    public static String getDisplayVersion() {
        String version = System.getProperty(STUDIO_VERSION_PROP);
        if (version == null || "".equals(version.trim())) { //$NON-NLS-1$
            version = getInternalVersion();
        }
        return version;
    }

    /**
     * 
     * DOC ggu Comment method "getEclipseProductFile".
     * 
     * @return
     * @throws URISyntaxException
     */
    private static File getEclipseProductFile() throws URISyntaxException {
        File installFolder = URIUtil.toFile(URIUtil.toURI(Platform.getInstallLocation().getURL()));
        // .eclipseproduct file
        File eclipseproductFile = new File(installFolder, ".eclipseproduct");//$NON-NLS-1$
        return eclipseproductFile;
    }

    public static String getInternalVersion() {
        if (Platform.inDevelopmentMode()) {
            return getDisplayVersion();
        }
        if (productVersion == null) {
            synchronized (VersionUtils.class) {
                if (productVersion == null) {
                    Bundle bundle = FrameworkUtil.getBundle(VersionUtils.class);
                    if (bundle != null) {
                        productVersion = bundle.getHeaders().get(org.osgi.framework.Constants.BUNDLE_VERSION);
                    }

                    FileInputStream in = null;
                    try {
                        File eclipseProductFile = getEclipseProductFile();
                        if (eclipseProductFile != null && eclipseProductFile.exists()) {
                            Properties p = new Properties();
                            in = new FileInputStream(eclipseProductFile);
                            p.load(in);
                            String productFileVersion = p.getProperty("version"); //$NON-NLS-1$
                            if (productFileVersion != null && !"".equals(productFileVersion)) { //$NON-NLS-1$
                                productVersion = productFileVersion;
                            }
                        }
                    } catch (Exception e) {
                        //
                    } finally {
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e) {
                                //
                            }
                        }
                    }
                }
            }
        }
        return productVersion;
    }

    /**
     * DOC ycbai Comment method "getVersion".
     * 
     * @deprecated Please use either getInternalVersion() or getDisplayVersion()
     * @return the studio version.
     */
    @Deprecated
    public static String getVersion() {
        return getDisplayVersion();
    }

    /**
     * DOC ycbai Comment method "getTalendVersion".
     * 
     * return the internal version of the studio that may be different from the Studio version in OEM distribution. look
     * for version in org.talend.commons.runtime/talend.properties with the key talend.version if none found then return
     * the Studio version.
     * 
     * @return the talend version or the Studio version or null if none found.
     * 
     */
    public static String getTalendVersion() {
        if (talendVersion == null) {
            synchronized (VersionUtils.class) {
                if (talendVersion == null) {
                    Bundle b = Platform.getBundle(COMMONS_PLUGIN_ID);
                    if (b != null) {
                        try {
                            URL fileUrl = FileLocator.find(b, new Path(TALEND_PROPERTIES_FILE), null);
                            if (fileUrl != null) {
                                URL url = FileLocator.toFileURL(fileUrl);
                                if (url != null) {
                                    FileInputStream in = new FileInputStream(url.getPath());
                                    try {
                                        Properties props = new Properties();
                                        props.load(in);
                                        talendVersion = props.getProperty(TALEND_VERSION_PROP);
                                    } finally {
                                        in.close();
                                    }
                                }
                            }
                        } catch (IOException e) {
                            log.error(Messages.getString("VersionUtils.readPropertyFileError"), e);
                        }
                    }
                }
            }
        }
        if (talendVersion == null) {
            return getDisplayVersion();
        }

        return talendVersion;
    }

    public static String getPublishVersion(String version) {
        if (version != null) {
            // if using job version.
            if (version.matches("\\d+\\.\\d+")) { //$NON-NLS-1$
                // set the version format as "#.#.#"
                String[] split = version.split("\\."); //$NON-NLS-1$
                for (int i = 0; i < 3 - split.length; i++) {
                    version += ".0"; //$NON-NLS-1$
                }
            }
        }
        return version;
    }

    public static void clearCache() {
        synchronized (VersionUtils.class) {
            productVersion = null;
            talendVersion = null;
        }
    }
}
