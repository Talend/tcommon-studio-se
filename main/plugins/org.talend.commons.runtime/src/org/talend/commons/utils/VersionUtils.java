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
package org.talend.commons.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.URIUtil;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;
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

    public static final String TALEND_STUDIO_VERSION_PROP = "talend.studio.version"; //$NON-NLS-1$

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
            String version = getDisplayVersion();
            updateTalendStudioVersionProp(version);
            return version;
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
            updateTalendStudioVersionProp(productVersion);
        }
        return productVersion;
    }

    private static void updateTalendStudioVersionProp(String version) {
        System.setProperty(TALEND_STUDIO_VERSION_PROP, version == null ? "" : version);
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

    public static String getProductVersionWithoutBranding(String fullProductVersion) {
        String[] splitStr = fullProductVersion.split("-"); //$NON-NLS-1$
        Pattern pattern = Pattern.compile("((\\d+\\.){2}\\d.*)"); //$NON-NLS-1$
        StringBuffer versionStr = new StringBuffer();
        boolean find = false;
        for (String str : splitStr) {
            if (find) {
                versionStr.append("-").append(str); //$NON-NLS-1$
            }else {
                Matcher matcher = pattern.matcher(str);
                if (matcher.find()) {
                    find = true;
                    versionStr.append(str); // $NON-NLS-1$
                }
            }
        }

        return versionStr.toString();
    }

    public static String getTalendPureVersion(String fullProductVersion) {
        return getTalendVersion(getProductVersionWithoutBranding(fullProductVersion));
    }

    /**
     * Check if studio version < other studio version record in remote project.
     */
    public static boolean isInvalidProductVersion(String remoteFullProductVersion) {
        return isInvalidProductVersion(getInternalVersion(), remoteFullProductVersion);
    }

    protected static boolean isInvalidProductVersion(String localProductVersion, String remoteFullProductVersion) {
        if (remoteFullProductVersion == null) {
            return false;
        }
        if (localProductVersion == null) {
            localProductVersion = getInternalVersion();
        }
        if (skipCheckingNightlyBuilds(localProductVersion, remoteFullProductVersion)) {
            return false;
        }
        return localProductVersion.compareTo(getProductVersionWithoutBranding(remoteFullProductVersion)) < 0;
    }

    public static boolean productVersionIsNewer(String remoteFullProductVersion) {
        String localProductVersion = getInternalVersion();
        return productVersionIsNewer(localProductVersion, remoteFullProductVersion);
    }

    protected static boolean productVersionIsNewer(String localProductVersion, String remoteFullProductVersion) {
        if (remoteFullProductVersion == null) {
            return false;
        }
        if (skipCheckingNightlyBuilds(localProductVersion, remoteFullProductVersion)) {
            return false;
        }
        return localProductVersion.compareTo(getProductVersionWithoutBranding(remoteFullProductVersion)) > 0;
    }

    private static boolean skipCheckingNightlyBuilds(String localProductVersion, String remoteFullProductVersion) {
        String separator = "-"; //$NON-NLS-1$
        String localSuffix = StringUtils.substringAfterLast(localProductVersion, separator);

        String remoteProductVersion = getProductVersionWithoutBranding(remoteFullProductVersion);
        String remoteSuffix = StringUtils.substringAfterLast(remoteProductVersion, separator);

        String nightly = "SNAPSHOT"; //$NON-NLS-1$
        String milestone = "M"; //$NON-NLS-1$
        if ((localSuffix.equals(nightly) || localSuffix.startsWith(milestone))
                && (remoteSuffix.equals(nightly) || remoteSuffix.startsWith(milestone))) {
            // skip checking between nightly/milestone build.
            return true;
        }
        return false;
    }

    public static String getTalendVersion(String productVersion) {
        try {
            org.osgi.framework.Version v = new org.osgi.framework.Version(productVersion);
            // only get major.minor.micro
            org.osgi.framework.Version simpleVersion = new org.osgi.framework.Version(v.getMajor(), v.getMinor(), v.getMicro());
            productVersion = simpleVersion.toString();
        } catch (IllegalArgumentException e) {
            productVersion = getTalendVersion();
        }
        return productVersion;
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

    public static String getMojoVersion(MojoType mojoType) {
        String mojoKey = mojoType.getVersionKey();
        String version = System.getProperty(mojoKey);
        if (StringUtils.isNotBlank(version)) {
            return version;
        }
        String talendVersion = getTalendVersion();
        String majorVersion = StringUtils.substringBeforeLast(talendVersion, "."); //$NON-NLS-1$
        String artifactIdFolder = mojoType.getMojoArtifactIdFolder();
        Optional<File> optional = Stream.of(new File(artifactIdFolder).listFiles())
                .filter(f -> f.isDirectory() && f.getName().startsWith(majorVersion))
                .sorted((f1, f2) -> new DefaultArtifactVersion(f2.getName()).compareTo(new DefaultArtifactVersion(f1.getName())))
                .findFirst();
        if (optional.isPresent()) {
            File latestArtifact = optional.get();
            String fileName = mojoType.getArtifactId() + "-" + latestArtifact.getName(); //$NON-NLS-1$
            if (Stream.of(latestArtifact.listFiles())
                    .filter(f -> f.getName().equals(fileName + ".jar") || f.getName().equals(fileName + ".pom")) //$NON-NLS-1$ //$NON-NLS-2$
                    .count() != 2) {
                ExceptionHandler.process(new Exception("Can't find plugin artifact " + mojoType.getMojoGAV())); //$NON-NLS-1$
            }
            version = latestArtifact.getName();
        }
        // default version
        if (StringUtils.isBlank(version)) {
            version = talendVersion;
            if (CommonsPlugin.isJUnitTest()) {
                productVersion = null;
            }
            String productVersion = getInternalVersion();
            String revision = StringUtils.substringAfterLast(productVersion, "-"); //$NON-NLS-1$
            if (("SNAPSHOT").equals(revision) || Pattern.matches("M\\d{1}", revision)) { //$NON-NLS-1$ //$NON-NLS-2$
                version += "-" + revision; //$NON-NLS-1$
            }
        }
        System.setProperty(mojoKey, version);
        return version;
    }

    public static void clearCache() {
        synchronized (VersionUtils.class) {
            productVersion = null;
            talendVersion = null;
        }
    }

    public static File getStudioConfigFile() throws Exception {
        URL configLocation = new URL("platform:/config/config.ini"); //$NON-NLS-1$
        URL fileUrl = FileLocator.toFileURL(configLocation);
        return URIUtil.toFile(new URI(fileUrl.getProtocol(), fileUrl.getPath(), fileUrl.getQuery()));
    }

    public static String getSimplifiedPatchName(String projectPatchName) {

        if (projectPatchName != null) {
            String result = null;
            if (projectPatchName.contains("_") && projectPatchName.split("_").length >= 3) {
                result = projectPatchName.split("_")[2];
                if (!result.startsWith("R")) {
                    return null;
                }
            }
            if (projectPatchName.contains("-")) {
                String[] split = projectPatchName.split("-");
                if (split != null && split.length > 0) {
                    return result + "-" + split[split.length - 1];
                }
            }
        }
        return null;
    }

}
