// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.runtime.utils.io.IOUtils;
import org.talend.commons.ui.runtime.update.PreferenceKeys;
import org.talend.commons.utils.resource.UpdatesHelper;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.core.services.ICoreTisService;
import org.talend.updates.runtime.P2UpdateConstants;
import org.talend.utils.io.FilesUtils;

public class P2UpdateHelper {

    private static Object clearOsgiLock = new Object();

    public static File backupConfigFile() throws IOException {
        try {
            File configurationFile = PathUtils.getStudioConfigFile();
            File tempFile = File.createTempFile(UpdatesHelper.FILE_CONFIG_INI, null);
            FilesUtils.copyFile(new FileInputStream(configurationFile), tempFile);
            return tempFile;
        } catch (Exception e) {
            if (e instanceof IOException) {
                throw (IOException) e;
            }
            throw new IOException(e);
        }
    }

    public static void restoreConfigFile(File toResore) throws IOException {
        try {
            File configurationFile = PathUtils.getStudioConfigFile();
            if (!IOUtils.contentEquals(new FileInputStream(configurationFile), new FileInputStream(toResore))) {
                if (GlobalServiceRegister.getDefault().isServiceRegistered(ICoreTisService.class)) {
                    ICoreTisService coreTisService = (ICoreTisService) GlobalServiceRegister.getDefault()
                            .getService(ICoreTisService.class);
                    coreTisService.updateConfiguratorBundles(configurationFile, toResore);
                }
            }
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            if (toResore != null && toResore.exists()) {
                toResore.delete();
            }
        }
    }

    public static URI getP2RepositoryURI() {
        String updatesite = null;
        try (InputStream fis = new FileInputStream(PathUtils.getStudioConfigFile())) {
            Properties properties = new Properties();
            properties.load(fis);
            updatesite = properties.getProperty(P2UpdateConstants.KEY_UPDATESITE_PATH);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        if (updatesite == null) {
            return null;
        }
        File file = new File(updatesite);
        if (file.isFile() || file.isDirectory()) {
            return file.toURI();
        }
        return URI.create(updatesite);
    }

    public static List<String> getConfigFeatures(String type) {
        String targetFeatures = null;
        try (InputStream fis = new FileInputStream(PathUtils.getStudioConfigFile())) {
            Properties properties = new Properties();
            properties.load(fis);
            targetFeatures = properties.getProperty(type);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        if (StringUtils.isBlank(targetFeatures)) {
            return Collections.emptyList();
        }
        return Arrays.asList(targetFeatures.trim().split(",")); //$NON-NLS-1$
    }

    public static void clearConfigFeatures(String type) {
        Properties properties = new Properties();
        try (InputStream fis = new FileInputStream(PathUtils.getStudioConfigFile())) {
            properties.load(fis);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        try (OutputStream fos = new FileOutputStream(PathUtils.getStudioConfigFile())) {
            properties.setProperty(type, StringUtils.EMPTY);
            properties.store(fos, "Configuration File"); //$NON-NLS-1$
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    public static void updateProductVersion(IProgressMonitor monitor, IProfile profile) throws IOException {
        Properties properties = new Properties();
        File eclipseProductFile = new File(
                Platform.getInstallLocation().getDataArea(UpdatesHelper.FILE_ECLIPSE_PRODUCT).getPath());
        try (InputStream in = new FileInputStream(eclipseProductFile)) {
            properties.load(in);
        }
        String oldVersion = properties.getProperty("version"); //$NON-NLS-1$
        Set<IInstallableUnit> queryResult = profile.query(QueryUtil.createIUQuery(P2UpdateConstants.STUDIO_CORE_FEATURE_ID), monitor)
                .toUnmodifiableSet();
        IInstallableUnit newCoreFeature = queryResult.stream().findFirst().get();
        String newVersion = newCoreFeature.getVersion().toString();
        if (oldVersion.endsWith(MavenConstants.SNAPSHOT)) {
            // for nightly build only.
            newVersion += MavenConstants.SNAPSHOT;
        }
        properties.setProperty("version", newVersion); //$NON-NLS-1$
        try (OutputStream out = new FileOutputStream(eclipseProductFile)) {
            properties.store(out, "Configuration File"); //$NON-NLS-1$
        }
    }

    public static void clearOsgiCache() {
        synchronized (clearOsgiLock) {
            PlatformUI.getPreferenceStore().setValue(PreferenceKeys.NEED_OSGI_CLEAN, true);
        }
    }
}
