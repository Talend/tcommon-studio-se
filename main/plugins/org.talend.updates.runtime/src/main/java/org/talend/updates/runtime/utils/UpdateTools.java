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
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.internal.p2.metadata.InstallableUnit;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.query.IQuery;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.runtime.utils.io.FileCopyUtils;
import org.talend.commons.runtime.utils.io.IOUtils;
import org.talend.commons.utils.resource.UpdatesHelper;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.services.ICoreTisService;
import org.talend.librariesmanager.prefs.LibrariesManagerUtils;
import org.talend.updates.runtime.service.ITaCoKitUpdateService;
import org.talend.utils.io.FilesUtils;

public class UpdateTools {

    public static String FILE_EXTRA_FEATURE_INDEX = "extra_feature.index"; //$NON-NLS-1$

    public static String FILE_PATCH_PROPERTIES = "patch.properties"; //$NON-NLS-1$

    public static String PATCH_PROPUCT_VERSION = "product.version"; //$NON-NLS-1$

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    
    private static final String SIGNATURE_FILE_NAME_SUFFIX = ".sig";

    public static void backupConfigFile() throws IOException {
        File configurationFile = getConfigurationFile();
        File tempFile = getTempConfigurationFile();
        if (tempFile.exists()) {
            tempFile.delete();
        }
        FilesUtils.copyFile(new FileInputStream(configurationFile), tempFile);
    }

    public static void restoreConfigFile() throws IOException {
        File tempFile = getTempConfigurationFile();
        if (!tempFile.exists()) {
            return;
        }
        try {
            File configurationFile = getConfigurationFile();
            if (CommonsPlugin.isJUnitTest()) {
                FilesUtils.copyFile(new FileInputStream(tempFile), configurationFile);
            } else {
                if (!IOUtils.contentEquals(new FileInputStream(configurationFile), new FileInputStream(tempFile))) {
                    if (GlobalServiceRegister.getDefault().isServiceRegistered(ICoreTisService.class)) {
                        ICoreTisService coreTisService = (ICoreTisService) GlobalServiceRegister.getDefault()
                                .getService(ICoreTisService.class);
                        coreTisService.updateConfiguratorBundles(configurationFile, tempFile);
                    }
                }
            }
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    public static File getConfigurationFile() throws IOException {
        return getConfigurationFile(true);
    }

    public static File getConfigurationFile(boolean create) throws IOException {
        File configFile = getConfigurationFolder().toPath().resolve(UpdatesHelper.FILE_CONFIG_INI).toFile();
        if (CommonsPlugin.isJUnitTest()) {
            File configFile4Test = getProductTempFolder().toPath().resolve("configuration").resolve(UpdatesHelper.FILE_CONFIG_INI) //$NON-NLS-1$
                    .toFile();
            if (create && !configFile4Test.exists()) {
                FilesUtils.copyFile(configFile, configFile4Test);
            }
            return configFile4Test;
        }
        return configFile;
    }

    public static File getTempConfigurationFile() {
        String folderName;
        if (CommonsPlugin.isJUnitTest()) {
            folderName = "Junit"; //$NON-NLS-1$
        } else {
            folderName = "PatchInstaller"; //$NON-NLS-1$
        }
        return getProductTempFolder().toPath().resolve(folderName).resolve(UpdatesHelper.FILE_CONFIG_INI)
                .toFile();
    }

    /**
     * look for all {@link IInstallableUnit} and check that they are of type {@link InstallableUnit}. If that is so,
     * then their singleton state is set to false. WARNING : internal APIs of p2 are used because I could not find any
     * way around the limitation of P2 that does not allow 2 singletons to be deployed at the same time
     *
     * @param toInstall a set of {@link IInstallableUnit} to be set as not a singleton
     */
    public static void setIuSingletonToFalse(Set<IInstallableUnit> toInstall) {
        for (IInstallableUnit iu : toInstall) {
            if (iu instanceof InstallableUnit) {
                ((InstallableUnit) iu).setSingleton(false);
            } // else not a IU supporting singleton so ignore.
        }
    }

    /**
     * look for all {@link IInstallableUnit} in the current installed p2 profile that have the same id as the toInstall
     * IUs. then their state is forced to be singleton=false so that multiple singleton may be installed.
     *
     * @param toInstall a set of {@link IInstallableUnit} to get the Id from
     * @param agent to get the current installed IUs
     */
    public static Set<IInstallableUnit> makeInstalledIuSingletonFrom(Set<IInstallableUnit> toInstall, IProvisioningAgent agent) {
        IProfileRegistry profRegistry = (IProfileRegistry) agent.getService(IProfileRegistry.SERVICE_NAME);
        IProfile profile = profRegistry.getProfile("_SELF_"); //$NON-NLS-1$
        HashSet<IQuery<IInstallableUnit>> queryCollection = new HashSet<IQuery<IInstallableUnit>>();
        for (IInstallableUnit toBeInstalled : toInstall) {
            IQuery<IInstallableUnit> iuQuery = QueryUtil.createIUQuery(toBeInstalled.getId());
            queryCollection.add(iuQuery);
        }
        IQueryResult<IInstallableUnit> profileIUToBeUpdated = profile.query(QueryUtil.createCompoundQuery(queryCollection, false),
                new NullProgressMonitor());
        final Set<IInstallableUnit> unmodifiableSet = profileIUToBeUpdated.toUnmodifiableSet();
        setIuSingletonToFalse(unmodifiableSet);
        return unmodifiableSet;
    }

    public static String readProductVersionFromPatch(File installingFile) throws IOException {
        try (InputStream in = new FileInputStream(new File(installingFile, FILE_PATCH_PROPERTIES))) {
            Properties properties = new Properties();
            properties.load(in);
            return properties.getProperty(PATCH_PROPUCT_VERSION, StringUtils.EMPTY);
        }
    }

    public static void syncExtraFeatureIndex(final File installingPatchFolder) throws IOException {
        File sourceFile = new File(installingPatchFolder, FILE_EXTRA_FEATURE_INDEX);
        File targetFile = new File(
                Platform.getConfigurationLocation().getURL().getPath() + File.separator + FILE_EXTRA_FEATURE_INDEX);
        if (targetFile.exists()) {
            targetFile.delete();
        }
        FilesUtils.copyFile(sourceFile, targetFile);
        // sync signature
        File sourceSigFile = new File(sourceFile.getAbsolutePath() + SIGNATURE_FILE_NAME_SUFFIX);
        File targetSigFile = new File(targetFile.getAbsolutePath() + SIGNATURE_FILE_NAME_SUFFIX);
        if (targetSigFile.exists()) {
            targetSigFile.delete();
        }
        FilesUtils.copyFile(sourceSigFile, targetSigFile);
    }

    public static File getProductRootFolder() {
        return new File(Platform.getInstallLocation().getURL().getPath());
    }

    public static File getConfigurationFolder() {
        return new File(Platform.getConfigurationLocation().getURL().getPath());
    }

    public static File getProductTempFolder() {
        return getProductRootFolder().toPath().resolve("temp").toFile(); //$NON-NLS-1$
    }

    public static boolean installCars(IProgressMonitor monitor, File installingPatchFolder, boolean cancellable)
            throws Exception {
        if (installingPatchFolder != null && installingPatchFolder.exists()) {
            File carFolder = new File(installingPatchFolder, ITaCoKitUpdateService.FOLDER_CAR);
            TaCoKitCarUtils.installCars(carFolder, monitor, cancellable);
        }
        return true;
    }

    public static void syncLibraries(File installingPatchFolder) throws IOException {
        // sync to product lib/java
        File libFolder = new File(installingPatchFolder, LibrariesManagerUtils.LIB_JAVA_SUB_FOLDER);
        if (libFolder.exists() && libFolder.isDirectory() && libFolder.listFiles().length > 0) {
            FilesUtils.copyFolder(libFolder, new File(LibrariesManagerUtils.getLibrariesPath()), false, null, null, false);
        }
    }

    public static void syncM2Repository(File installingPatchFolder) throws IOException {
        // sync to the local m2 repository, if need try to deploy to remote TAC Nexus.
        File m2Folder = new File(installingPatchFolder, PathUtils.FOLDER_M2_REPOSITORY);
        if (m2Folder.exists() && m2Folder.isDirectory() && m2Folder.listFiles().length > 0) {
            // if have remote nexus, install component too early and before logon project , will cause the problem
            // (TUP-17604)
            // prepare to install lib after logon. so copy all to temp folder also.
            FileCopyUtils.copyFolder(m2Folder, new File(PathUtils.getComponentsM2TempFolder(), PathUtils.FOLDER_M2_REPOSITORY));
        }
    }

    public static void collectDropBundles(Set<IInstallableUnit> validInstall, Map<String, String> extraBundles,
            Map<String, String> dropBundles) throws IOException {
        File pluginFolderFile = getProductRootFolder().toPath().resolve("plugins").toFile();
        if (!pluginFolderFile.exists() || !pluginFolderFile.isDirectory()) {
            return;
        }
        Set<File> plugins = Stream.of(pluginFolderFile.listFiles()).collect(Collectors.toSet());
        List<String> dropList = new ArrayList<>();
        // skip if from third-party bundles(version is not empty) in index, clean will be done by drop.bundle.info
        validInstall.stream().filter(iu -> !extraBundles.containsKey(iu.getId()) || extraBundles.get(iu.getId()).isEmpty())
                .forEach(iu -> {
            List<String> list = plugins.stream()
                    .filter(f -> f.exists() && f.getName().startsWith(iu.getId() + "_")
                            && !f.getName().contains(iu.getId() + "_" + iu.getVersion()))
                    .map(File::getAbsolutePath).collect(Collectors.toList());
            dropList.addAll(list);
        });
        // collect from drop.bundle.info
        dropBundles.forEach((b, v) -> plugins.stream().filter(f -> f.getName().contains(b + "_" + v))
                .forEach(f -> dropList.add(f.getAbsolutePath())));
        File dropFile = new File(pluginFolderFile, "droplist");
        if (!dropFile.exists()) {
            dropFile.createNewFile();
        }
        StringBuilder builder = new StringBuilder();
        Stream.concat(dropList.stream(), Files.readAllLines(dropFile.toPath()).stream()).distinct()
                .forEach(s -> builder.append(s).append(LINE_SEPARATOR));
        Files.write(dropFile.toPath(), builder.toString().getBytes());
    }

    public static void cleanUpDropBundles() throws IOException {
        File pluginFolderFile = getProductRootFolder().toPath().resolve("plugins").toFile();
        File dropFile = new File(pluginFolderFile, "droplist");
        if (dropFile.exists()) {
            StringBuilder builder = new StringBuilder();
            Files.lines(dropFile.toPath()).map(File::new).filter(f -> deleteBundle(f))
                    .forEach(f -> builder.append(f.getAbsolutePath()).append(LINE_SEPARATOR));
            if (builder.length() > 0) {
                // if deletion for some bundle failed
                Files.write(dropFile.toPath(), builder.toString().getBytes());
            } else {
                dropFile.delete();
            }
        }
    }

    private static boolean deleteBundle(File file) {
        if (!file.exists()) {
            return false;
        }
        if (file.isDirectory()) {
            FilesUtils.deleteFolder(file, true);
            return file.exists();
        }
        return !file.delete();
    }

}
