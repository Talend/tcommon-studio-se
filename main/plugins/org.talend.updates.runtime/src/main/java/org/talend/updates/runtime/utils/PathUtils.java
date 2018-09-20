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
package org.talend.updates.runtime.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.equinox.p2.metadata.Version;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.runtime.service.ComponentsInstallComponent;
import org.talend.commons.runtime.service.PatchComponent;
import org.talend.commons.utils.VersionUtils;
import org.talend.commons.utils.resource.FileExtensions;
import org.talend.updates.runtime.feature.model.Category;
import org.talend.updates.runtime.feature.model.Type;
import org.talend.updates.runtime.model.InstallationStatus;
import org.talend.updates.runtime.model.InstallationStatus.Status;

/**
 * created by ycbai on 2017年5月23日 Detailled comment
 *
 */
public class PathUtils {

    public static final String FOLDER_COMPS = "components"; //$NON-NLS-1$

    public static final String FOLDER_INSTALLED = "installed"; //$NON-NLS-1$

    public static final String FOLDER_SHARED = "shared"; //$NON-NLS-1$

    public static final String FOLDER_DOWNLOADED = "downloaded"; //$NON-NLS-1$

    public static final String FOLDER_M2TEMP = "m2temp"; //$NON-NLS-1$

    public static final String FOLDER_PATCHES = PatchComponent.FOLDER_PATCHES;

    public static final String FOLDER_M2_REPOSITORY = ComponentsInstallComponent.FOLDER_M2_REPOSITORY;

    private static final String P2_REP_FILE_URI_PATTERN = "^jar:(.+)!\\/$"; //$NON-NLS-1$

    public static File getStudioConfigFile() throws Exception {
        URL configLocation = new URL("platform:/config/config.ini"); //$NON-NLS-1$
        URL fileUrl = FileLocator.toFileURL(configLocation);
        return URIUtil.toFile(new URI(fileUrl.getProtocol(), fileUrl.getPath(), fileUrl.getQuery()));
    }

    public static Properties readProperties(final File config) {
        final Properties configuration = new Properties();
        try (final InputStream stream = new FileInputStream(config)) {
            configuration.load(stream);
        } catch (final IOException e) {
            throw new IllegalArgumentException(e);
        }
        return configuration;
    }

    public static File getComponentsFolder() throws IOException {
        File componentsFolder = new File(Platform.getConfigurationLocation().getDataArea(FOLDER_COMPS).getPath());
        if (!componentsFolder.exists()) {
            componentsFolder.mkdirs();
        }
        return componentsFolder;
    }

    private static File createComponentFolder(String subName) {
        File folder = null;
        try {
            folder = new File(getComponentsFolder(), subName);
        } catch (IOException e) {
            folder = new File(System.getProperty("user.dir"), subName); //$NON-NLS-1$
        }
        if (folder != null && !folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }

    public static File getComponentsInstalledFolder() {
        return createComponentFolder(FOLDER_INSTALLED);
    }

    public static File getComponentsSharedFolder() {
        return createComponentFolder(FOLDER_SHARED);
    }

    public static File getComponentsDownloadedFolder() {
        return createComponentFolder(FOLDER_DOWNLOADED);
    }

    public static File getComponentsM2TempFolder() {
        return createComponentFolder(FOLDER_M2TEMP);
    }

    public static File getPatchesFolder() {
        try {
            return new File(Platform.getInstallLocation().getDataArea(FOLDER_PATCHES).getPath());
        } catch (IOException e) {
            //
        }
        return new File(System.getProperty("user.dir"), FOLDER_PATCHES); //$NON-NLS-1$
    }

    public static URI getP2RepURIFromCompFile(File compFile) {
        if (compFile == null) {
            return null;
        }
        final String name = compFile.getName().toLowerCase();

        if (name.endsWith(FileExtensions.JAR_FILE_SUFFIX) || name.endsWith(FileExtensions.ZIP_FILE_SUFFIX)
                || name.endsWith(FileExtensions.CAR_FILE_SUFFIX)) {
            return URI.create("jar:" + compFile.toURI().toString() + "!/");
        }
        return null;
    }

    public static File getCompFileFromP2RepURI(URI p2RepURI) throws MalformedURLException {
        if (p2RepURI == null) {
            return null;
        }
        String filePath = p2RepURI.toString();
        Matcher matcher = Pattern.compile(P2_REP_FILE_URI_PATTERN).matcher(filePath);
        if (matcher.find()) {
            filePath = matcher.group(1);
        }
        return new File(URI.create(filePath).toURL().getFile());
    }

    public static String getTalendVersionStr() {
        org.osgi.framework.Version studioVersion = new org.osgi.framework.Version(VersionUtils.getTalendVersion());

        StringBuffer result = new StringBuffer();
        result.append(studioVersion.getMajor());
        result.append('.');
        result.append(studioVersion.getMinor());
        result.append('.');
        result.append(studioVersion.getMicro());

        return result.toString();
    }

    public static Collection<Type> getAllTypeCategories(Collection<Type> types) {
        Collection<Type> allTypeCategories = new HashSet<>();

        if (types != null) {
            for (Type type : types) {
                Collection<Type> categories = type.getCategories();
                if (categories != null) {
                    allTypeCategories.addAll(categories);
                }
            }
        }

        return allTypeCategories;
    }

    public static Collection<Type> convert2Types(String types) {
        Collection<Type> typeList = new ArrayList<>();
        if (StringUtils.isNotBlank(types)) {
            String[] splitStrs = types.split(",");
            if (splitStrs != null && 0 < splitStrs.length) {
                for (String str : splitStrs) {
                    Type t = Type.valueOf(str.trim());
                    if (t != null) {
                        typeList.add(t);
                    } else {
                        ExceptionHandler.process(new Exception("Can't resolve feature type: " + str));
                    }
                }
            }
        }
        return typeList;
    }

    public static String convert2StringTypes(Collection<Type> types) {
        String stringTypes = null;
        if (types == null || types.isEmpty()) {
            return stringTypes;
        }
        StringBuilder builder = new StringBuilder();
        for (Type type : types) {
            builder.append(type.getKeyWord()).append(",");
        }
        stringTypes = StringUtils.removeEnd(builder.toString(), ",");
        return stringTypes;
    }

    public static Collection<Category> convert2Categories(String categories) {
        Collection<Category> categoryList = new ArrayList<>();
        if (StringUtils.isNotBlank(categories)) {
            String[] splitStrs = categories.split(",");
            if (splitStrs != null && 0 < splitStrs.length) {
                for (String str : splitStrs) {
                    Category c = Category.valueOf(str.trim());
                    if (c != null) {
                        categoryList.add(c);
                    } else {
                        ExceptionHandler.process(new Exception("Can't resolve feature category: " + str));
                    }
                }
            }
        }
        return categoryList;
    }

    @SuppressWarnings("nls")
    public static Version convert2Version(String vStr) throws Exception {
        if (vStr == null) {
            return null;
        }
        Version version = null;
        try {
            version = Version.create(vStr);
        } catch (Exception e) {
            // nothing to do
        }
        if (version != null) {
            return version;
        }
        String[] split = vStr.split("\\.", 4); //$NON-NLS-1$
        int length = split.length;
        int major = 0;
        int minor = 0;
        int micro = 0;
        String classifier = "";

        boolean classifierBegin = false;
        if (0 < length) {
            if (!classifierBegin) {
                try {
                    major = Integer.valueOf(split[0]);
                } catch (Exception e) {
                    classifierBegin = true;
                }
            }
            if (classifierBegin) {
                if (!StringUtils.isBlank(classifier)) {
                    classifier = classifier + ".";
                }
                classifier = classifier + split[0];
            }
        }
        if (1 < length) {
            if (!classifierBegin) {
                try {
                    minor = Integer.valueOf(split[1]);
                } catch (Exception e) {
                    classifierBegin = true;
                }
            }
            if (classifierBegin) {
                if (!StringUtils.isBlank(classifier)) {
                    classifier = classifier + ".";
                }
                classifier = classifier + split[1];
            }
        }
        if (2 < length) {
            if (!classifierBegin) {
                try {
                    micro = Integer.valueOf(split[2]);
                } catch (Exception e) {
                    classifierBegin = true;
                }
            }
            if (classifierBegin) {
                if (!StringUtils.isBlank(classifier)) {
                    classifier = classifier + ".";
                }
                classifier = classifier + split[2];
            }
        }

        if (3 < length) {
            if (classifierBegin) {
                if (!StringUtils.isBlank(classifier)) {
                    classifier = classifier + ".";
                }
                classifier = classifier + split[3];
            } else {
                classifier = split[3];
            }
        }
        return Version.createOSGi(major, minor, micro, classifier);
    }

    public static InstallationStatus getInstallationStatus(String installedLatestVersionStr, String installingVersionStr)
            throws Exception {
        Version installedLatestVersion = null;
        if (StringUtils.isNotBlank(installedLatestVersionStr)) {
            installedLatestVersion = convert2Version(installedLatestVersionStr);
        } else {
            InstallationStatus status = new InstallationStatus(Status.INSTALLABLE);
            return status;
        }
        if (StringUtils.isNotBlank(installingVersionStr)) {
            Version featVersion = PathUtils.convert2Version(installingVersionStr);
            if (featVersion != null) {
                if (featVersion.compareTo(installedLatestVersion) <= 0) {
                    InstallationStatus status = new InstallationStatus(Status.INSTALLED);
                    status.setInstalledVersion(installedLatestVersion.toString());
                    return status;
                } else {
                    InstallationStatus status = new InstallationStatus(Status.UPDATABLE);
                    status.setInstalledVersion(installedLatestVersion.toString());
                    return status;
                }
            }
        }
        InstallationStatus status = new InstallationStatus(Status.INSTALLED);
        status.setInstalledVersion(installedLatestVersion.toString());
        return status;
    }

    @SuppressWarnings("nls")
    public static String getMessage(IStatus status, boolean printException) {
        StringBuffer strBuff = new StringBuffer();

        if (status == null) {
            return "";
        }

        strBuff.append(status.getMessage());
        if (printException) {
            Throwable exception = status.getException();
            if (exception != null) {
                ExceptionHandler.process(exception);
            }
        }
        IStatus[] children = status.getChildren();
        if (children != null && 0 < children.length) {
            strBuff.append("\n");
            for (IStatus child : children) {
                strBuff.append(getMessage(child, printException)).append("\n");
            }
        }

        return strBuff.toString();
    }
}
