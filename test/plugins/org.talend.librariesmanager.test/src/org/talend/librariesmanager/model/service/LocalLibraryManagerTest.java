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
package org.talend.librariesmanager.model.service;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.m2e.core.MavenPlugin;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.database.conn.version.EDatabaseVersion4Drivers;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.components.IComponentsService;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.ModuleNeeded.ELibraryInstallStatus;
import org.talend.core.model.general.Project;
import org.talend.core.nexus.NexusServerBean;
import org.talend.core.nexus.NexusServerUtils;
import org.talend.core.nexus.TalendLibsServerManager;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.librariesmanager.emf.librariesindex.LibrariesIndex;
import org.talend.librariesmanager.maven.ArtifactsDeployer;
import org.talend.librariesmanager.model.ModulesNeededProvider;
import org.talend.librariesmanager.prefs.LibrariesManagerUtils;
import org.talend.repository.ProjectManager;

/**
 * DOC hwang class global comment. Detailled comment
 */
public class LocalLibraryManagerTest {

    private ILibraryManagerService localLibraryManager;

    private List<String> notDilivers = new ArrayList<String>();

    /**
     * DOC Administrator Comment method "setUp".
     *
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        localLibraryManager = new LocalLibraryManager();
        notDilivers.add("nzjdbc.jar");
        notDilivers.add("sas.svc.connection.jar");
        notDilivers.add("sas.core.jar");
        notDilivers.add("sas.intrnet.javatools.jar");
        notDilivers.add("sas.security.sspi.jar");
        notDilivers.add("db2jcc.jar");
        notDilivers.add("interclient.jar");
        notDilivers.add("db2jcc_license_cisuz.jar");
        notDilivers.add("ifxjdbc.jar");
        notDilivers.add("jconn3.jar");
        notDilivers.add("sapdbc.jar");
        notDilivers.add("db2jcc_license_cu.jar");
    }

    /**
     * Test method for
     * {@link org.talend.librariesmanager.model.service.LocalLibraryManagerTest#deploy(java.net.URI, org.eclipse.core.runtime.IProgressMonitor[])}
     * .
     *
     * @throws IOException
     */
    @Test
    public void testDeployURIIProgressMonitorArray() throws IOException {
        String librariesPath = LibrariesManagerUtils.getLibrariesPath(ECodeLanguage.JAVA);
        File storageDir = new File(librariesPath);
        String installLocation = storageDir.getAbsolutePath();
        IComponentsService service = (IComponentsService) GlobalServiceRegister.getDefault().getService(IComponentsService.class);
        Map<String, File> componentsFolders = service.getComponentsFactory().getComponentsProvidersFolder();
        Set<String> contributeIdSet = componentsFolders.keySet();
        String jarFileUri = new Path(ResourcesPlugin.getWorkspace().getRoot().getLocationURI().getPath()).removeLastSegments(1)
                .toOSString() + File.separator + "temp" + File.separator + "classpath.jar";
        File file = new File(jarFileUri);
        String contributeID = "";
        for (String contributor : contributeIdSet) {
            if (file.getAbsolutePath().contains(contributor)) {
                contributeID = contributor;
                break;
            }
        }
        if (file == null || !file.exists()) {
            return;
        }
        if (contributeID.equals("")) {
            if (file.isDirectory()) {
                FilesUtils.copyFolder(new File(jarFileUri), storageDir, false, FilesUtils.getExcludeSystemFilesFilter(),
                        FilesUtils.getAcceptJARFilesFilter(), false, new NullProgressMonitor());
            } else {
                File target = new File(storageDir.getAbsolutePath(), file.getName());
                FilesUtils.copyFile(file, target);
            }
        } else {
            if ("org.talend.designer.components.model.UserComponentsProvider".contains(contributeID)
                    || "org.talend.designer.components.exchange.ExchangeComponentsProvider".contains(contributeID)) {
                if (file.isDirectory()) {
                    FilesUtils.copyFolder(new File(jarFileUri), storageDir, false, FilesUtils.getExcludeSystemFilesFilter(),
                            FilesUtils.getAcceptJARFilesFilter(), false, new NullProgressMonitor());
                } else {
                    File target = new File(storageDir.getAbsolutePath(), file.getName());
                    FilesUtils.copyFile(file, target);
                }
            } else {
                LibrariesIndex index = LibrariesIndexManager.getInstance().getStudioLibIndex();
                EMap<String, String> jarsToRelativePath = index.getJarsToRelativePath();
                List<File> jarFiles = FilesUtils.getJarFilesFromFolder(file, null);
                boolean modified = false;
                if (jarFiles.size() > 0) {
                    for (File jarFile : jarFiles) {
                        String name = jarFile.getName();
                        String fullPath = jarFile.getAbsolutePath();
                        // caculate the relative path
                        if (fullPath.indexOf(contributeID) != -1) {
                            fullPath = new Path(fullPath).toPortableString();
                            String relativePath = fullPath.substring(fullPath.indexOf(contributeID));
                            if (!jarsToRelativePath.keySet().contains(name)) {
                                jarsToRelativePath.put(name, relativePath);
                                modified = true;
                            }
                        }
                    }
                    if (modified) {
                        LibrariesIndexManager.getInstance().saveStudioIndexResource();
                    }
                }
            }
        }
    }

    /**
     * Test method for
     * {@link org.talend.librariesmanager.model.service.LocalLibraryManagerTest#retrieve(java.lang.String, java.lang.String, org.eclipse.core.runtime.IProgressMonitor[])}
     * .
     *
     * @throws IOException
     */
    @Test
    public void testRetrieveStringStringIProgressMonitorArray() throws IOException {
        String pathToStore = LibrariesManagerUtils.getLibrariesPath(ECodeLanguage.JAVA);
        String jarNeeded = "mysql-connector-java-5.1.0-bin.jar";
        String sourcePath = null, targetPath = pathToStore;
        List<File> jarFiles = FilesUtils.getJarFilesFromFolder(getStorageDirectory(), jarNeeded);
        if (jarFiles.size() > 0) {
            File jarFile = jarFiles.get(0);
            File target = new File(StringUtils.trimToEmpty(pathToStore));
            if (!target.exists()) {
                target.mkdirs();
            }
            sourcePath = jarFile.getAbsolutePath();
            FilesUtils.copyFile(jarFile, new File(pathToStore, jarFile.getName()));
            return;
        }
        // retrieve jar from the index.xml if not find in lib/java
        else {
            EMap<String, String> jarsToRelative = LibrariesIndexManager.getInstance().getStudioLibIndex().getJarsToRelativePath();
            String relativePath = jarsToRelative.get(jarNeeded);
            if (relativePath == null) {
                return;
            }
            String bundleLocation = "";
            String jarLocation = "";
            IComponentsService service = (IComponentsService) GlobalServiceRegister.getDefault().getService(
                    IComponentsService.class);
            Map<String, File> componentsFolders = service.getComponentsFactory().getComponentsProvidersFolder();
            Set<String> contributeIdSet = componentsFolders.keySet();
            boolean jarFound = false;
            for (String contributor : contributeIdSet) {
                if (relativePath.contains(contributor)) {
                    // caculate the the absolute path of the jar
                    bundleLocation = componentsFolders.get(contributor).getAbsolutePath();
                    int index = bundleLocation.indexOf(contributor);
                    jarLocation = new Path(bundleLocation.substring(0, index)).append(relativePath).toPortableString();
                    jarFound = true;
                    break;
                }
            }
            sourcePath = jarLocation;
            if (!jarFound) {
                CommonExceptionHandler.process(new Exception("Jar: " + jarNeeded + " not found, not in the plugins available:"
                        + contributeIdSet));
                return;
            }
            FilesUtils.copyFile(new File(jarLocation), new File(pathToStore, jarNeeded));
            return;
        }

    }

    /**
     * Test method for
     * {@link org.talend.librariesmanager.model.service.LocalLibraryManagerTest#list(org.eclipse.core.runtime.IProgressMonitor[])}
     * .
     */
    @Test
    public void testList() throws MalformedURLException {
        Set<String> names = new HashSet<String>();
        List<File> jarFiles = FilesUtils.getJarFilesFromFolder(getStorageDirectory(), null);
        if (jarFiles.size() > 0) {
            for (File file : jarFiles) {
                names.add(file.getName());
            }
        }

        EMap<String, String> jarsToRelative = LibrariesIndexManager.getInstance().getStudioLibIndex().getJarsToRelativePath();
        names.addAll(jarsToRelative.keySet());

        assertTrue(names.size() > 0);

    }

    /**
     * Test method for studio have all the lib for the system of db connection .
     */
    @Test
    @Ignore
    public void testMissingJar() throws MalformedURLException {
        Set<String> names = new HashSet<String>();
        List<File> jarFiles = FilesUtils.getJarFilesFromFolder(getStorageDirectory(), null);
        if (jarFiles.size() > 0) {
            for (File file : jarFiles) {
                names.add(file.getName());
            }
        }
        EMap<String, String> jarsToRelative = LibrariesIndexManager.getInstance().getStudioLibIndex().getJarsToRelativePath();
        names.addAll(jarsToRelative.keySet());

        List<String> allJars = new ArrayList<String>();
        EDatabaseVersion4Drivers[] values = EDatabaseVersion4Drivers.values();
        for (EDatabaseVersion4Drivers driver : values) {
            Set<String> providerDrivers = driver.getProviderDrivers();
            allJars.addAll(providerDrivers);
        }

        Set<String> missJars = new HashSet<String>();
        for (String jar : allJars) {
            boolean hadInstalled = false;
            for (String installJar : names) {
                if (jar.equals(installJar)) {
                    hadInstalled = true;
                }
            }
            if (!hadInstalled) {
                missJars.add(jar);
            }
        }

        if (missJars.size() > 0) {
            for (String notDiliver : notDilivers) {
                if (missJars.contains(notDiliver)) {
                    missJars.remove(notDiliver);
                }
            }
        }
        if (missJars.size() > 0) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("db system missing jars! \n");
            for (String missJar : missJars) {
                buffer.append(missJar + "\n");
            }
            throw new RuntimeException(buffer.toString());
        }
        assertTrue(missJars.size() == 0);
    }

    @Test @Ignore
    public void testUnusedJars() throws URISyntaxException {

        Bundle currentBundle = Platform.getBundle("org.talend.librariesmanager"); //$NON-NLS-1$
        Bundle[] bundles = currentBundle.getBundleContext().getBundles();
        Long totalSize = 0L;
        StringBuffer strBuff = new StringBuffer();
        List<String> neededJars = new ArrayList<String>();
        ModulesNeededProvider.reset();
        for (ModuleNeeded module : ModulesNeededProvider.getModulesNeeded()) {
            if (module.getStatus() != ELibraryInstallStatus.UNUSED) {
                neededJars.add(module.getModuleName());
            }
        }
        for (Bundle bundle : bundles) {
            String name = bundle.getSymbolicName();
            if (name.startsWith("org.talend.libraries")) {
                String classpath = bundle.getHeaders().get("Bundle-ClassPath");
                List<URL> urls = FilesUtils.getFilesFromFolder(bundle, "/lib", ".jar", true, true);
                for (URL url : urls) {
                    String jarFile = new Path(url.getFile()).lastSegment();
                    if (!neededJars.contains(jarFile) && (classpath == null || !classpath.contains(jarFile))) {
                        File file = new File(url.toURI());
                        Long fileSize = file.length();
                        String path = url.getPath().substring(url.getPath().indexOf("/plugins/") + 9);
                        strBuff.append(path + " : " + fileSize + "\n");
                        totalSize += fileSize;
                    }

                }
            }
            if (name.contains(".components.")) {
                List<URL> urls = FilesUtils.getFilesFromFolder(bundle, "/components", ".jar", true, true);
                for (URL url : urls) {
                    String jarFile = new Path(url.getFile()).lastSegment();
                    if (!neededJars.contains(jarFile)) {
                        File file = new File(url.toURI());
                        Long fileSize = file.length();
                        String path = url.getPath().substring(url.getPath().indexOf("/plugins/") + 9);
                        strBuff.append(path + " : " + fileSize + "\n");
                        totalSize += fileSize;
                    }
                }
            }
        }

        if (strBuff.length() != 0) {
            fail("Unused jars still in product (total byte size: " + totalSize + "):\n" + strBuff);
        }
    }

    /**
     * Test method for
     * {@link org.talend.librariesmanager.model.service.LocalLibraryManagerTest#delete(java.lang.String)}.
     */
    @Test
    public void testDelete() throws MalformedURLException {
        String jarName = "classpath.jar";
        List<File> jarFiles = FilesUtils.getJarFilesFromFolder(getStorageDirectory(), null);
        if (jarFiles.size() > 0) {
            for (File file : jarFiles) {
                if (file.getName().equals(jarName)) {
                    file.delete();
                }
            }
        }
    }

    private File getStorageDirectory() {
        String librariesPath = LibrariesManagerUtils.getLibrariesPath(ECodeLanguage.JAVA);
        File storageDir = new File(librariesPath);
        return storageDir;
    }

    @Test
    public void testRetrieveFromLocal() throws Exception {
        ILibraryManagerService libraryManagerService = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                ILibraryManagerService.class);
        String baseLocation = "platform:/plugin/org.talend.librariesmanager.test/";

        IEclipsePreferences node = InstanceScope.INSTANCE.getNode(NexusServerUtils.ORG_TALEND_DESIGNER_CORE);
        int originalValue = node.getInt(ITalendCorePrefConstants.NEXUS_REFRESH_FREQUENCY, 0);
        node.putInt(ITalendCorePrefConstants.NEXUS_REFRESH_FREQUENCY, 0);

        List<ModuleNeeded> modules = new ArrayList<ModuleNeeded>();
        ModuleNeeded module1 = new ModuleNeeded("module context", "test.jar", "test", true, null, null,
                "mvn:org.talend.libraries/test/6.0.0/jar");
        module1.setModuleLocaion(baseLocation + "/lib/old/test.jar");
        modules.add(module1);
        // snapshot mvnuri
        ModuleNeeded module2 = new ModuleNeeded("module context", "test_jar1.jar", "test", true, null, null,
                "mvn:org.apache/test_jar1/6.0.0-SNAPSHOT/jar");
        module2.setModuleLocaion(baseLocation + "/lib/test_jar1.jar");
        modules.add(module2);
        // name diffirent from mavenuri artifactid
        ModuleNeeded module3 = new ModuleNeeded("module context", "test_jar2.jar", "test", true, null, null,
                "mvn:org.apache/test_jar2.v201012070815/6.0.0/jar");
        module3.setModuleLocaion(baseLocation + "/lib/test_jar2.jar");
        modules.add(module3);
        // without mvnuri
        ModuleNeeded module4 = new ModuleNeeded("module context", "test_jar3.jar", "test", true, null, null, null);
        module4.setModuleLocaion(baseLocation + "/lib/test_jar3.jar");
        modules.add(module4);
        // package exe
        ModuleNeeded module5 = new ModuleNeeded("module context", "winutils-hadoop-2.6.0.exe", "test", true, null, null,
                "mvn:org.talend.libraries/winutils-hadoop-2.6.0/6.0.0-SNAPSHOT/exe");
        module5.setModuleLocaion(baseLocation + "/lib/winutils-hadoop-2.6.0.exe");
        modules.add(module5);

        // deploy to local maven
        libraryManagerService.deployModules(modules, new NullProgressMonitor());

        boolean retrieve1 = libraryManagerService.retrieve(module1, null, false, null);
        assertTrue(retrieve1);
        assertTrue(new File(libraryManagerService.getJarPathFromMaven(module1.getMavenUri())).exists());
        assertEquals(module1.getStatus(), ELibraryInstallStatus.INSTALLED);

        retrieve1 = libraryManagerService.retrieve(module2, null, false, null);
        assertTrue(retrieve1);
        assertTrue(new File(libraryManagerService.getJarPathFromMaven(module2.getMavenUri())).exists());
        assertEquals(module2.getStatus(), ELibraryInstallStatus.INSTALLED);

        retrieve1 = libraryManagerService.retrieve(module3, null, false, null);
        assertTrue(retrieve1);
        assertTrue(new File(libraryManagerService.getJarPathFromMaven(module3.getMavenUri())).exists());
        assertEquals(module3.getStatus(), ELibraryInstallStatus.INSTALLED);

        retrieve1 = libraryManagerService.retrieve(module4, null, false, null);
        assertTrue(retrieve1);
        assertTrue(new File(libraryManagerService.getJarPathFromMaven(module4.getMavenUri())).exists());
        assertEquals(module4.getStatus(), ELibraryInstallStatus.INSTALLED);

        retrieve1 = libraryManagerService.retrieve(module5, null, false, null);
        assertTrue(retrieve1);
        assertTrue(new File(libraryManagerService.getJarPathFromMaven(module5.getMavenUri())).exists());
        assertEquals(module5.getStatus(), ELibraryInstallStatus.INSTALLED);

        node.putInt(ITalendCorePrefConstants.NEXUS_REFRESH_FREQUENCY, originalValue);
    }

    @Test @Ignore
    public void testRetrieveFromRemote() throws Exception {
        ILibraryManagerService libraryManagerService = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                ILibraryManagerService.class);
        TalendLibsServerManager manager = TalendLibsServerManager.getInstance();
        final NexusServerBean customNexusServer = manager.getCustomNexusServer();
        if (customNexusServer == null) {
            fail("Test not possible since Nexus is not setup");
        }
        IEclipsePreferences node = InstanceScope.INSTANCE.getNode(NexusServerUtils.ORG_TALEND_DESIGNER_CORE);
        int originalValue = node.getInt(ITalendCorePrefConstants.NEXUS_REFRESH_FREQUENCY, 0);
        try {
            node.putInt(ITalendCorePrefConstants.NEXUS_REFRESH_FREQUENCY, 0);
            Bundle bundle = Platform.getBundle("org.talend.librariesmanager.test");
            String repPath = MavenPlugin.getMaven().getLocalRepositoryPath();
            if (!repPath.endsWith("/") && !repPath.endsWith("\\")) {
                repPath = repPath + "/";
            }

            ModuleNeeded module1 = new ModuleNeeded("module context", "test.jar", "test", true, null, null,
                    "mvn:org.talend.libraries/test/6.0.0/jar");
            URL entry1 = bundle.getEntry("/lib/old/test.jar");
            File jarInM2 = new File(repPath + "org/talend/libraries/test");
            if (jarInM2.exists()) {
                FilesUtils.deleteFile(jarInM2, true);
            }
            // snapshot mvnuri
            ModuleNeeded module2 = new ModuleNeeded("module context", "test_jar1.jar", "test", true, null, null,
                    "mvn:org.apache/test_jar1/6.0.0-SNAPSHOT/jar");
            URL entry2 = bundle.getEntry("/lib/test_jar1.jar");
            jarInM2 = new File(repPath + "org/apache/test_jar1");
            if (jarInM2.exists()) {
                FilesUtils.deleteFile(jarInM2, true);
            }
            // name diffirent from mavenuri artifactid
            ModuleNeeded module3 = new ModuleNeeded("module context", "test_jar2.jar", "test", true, null, null,
                    "mvn:org.apache/test_jar2.v201012070815/6.0.0/jar");
            URL entry3 = bundle.getEntry("/lib/test_jar2.jar");
            jarInM2 = new File(repPath + "org/apache/test_jar2.v201012070815");
            if (jarInM2.exists()) {
                FilesUtils.deleteFile(jarInM2, true);
            }
            // without mvnuri
            ModuleNeeded module4 = new ModuleNeeded("module context", "test_jar3.jar", "test", true, null, null, null);
            URL entry4 = bundle.getEntry("/lib/test_jar3.jar");
            jarInM2 = new File(repPath + "org/talend/libraries/test_jar3");
            if (jarInM2.exists()) {
                FilesUtils.deleteFile(jarInM2, true);
            }
            // package exe
            ModuleNeeded module5 = new ModuleNeeded("module context", "winutils-hadoop-2.6.0.exe", "test", true, null, null,
                    "mvn:org.talend.libraries/winutils-hadoop-2.6.0/6.0.0-SNAPSHOT/exe");
            URL entry5 = bundle.getEntry("/lib/winutils-hadoop-2.6.0.exe");
            jarInM2 = new File(repPath + "org/talend/libraries/winutils-hadoop-2.6.0");
            if (jarInM2.exists()) {
                FilesUtils.deleteFile(jarInM2, true);
            }

            // install to nexus
            ArtifactsDeployer deployer = new ArtifactsDeployer();
            MavenArtifact artifact = MavenUrlHelper.parseMvnUrl(module1.getMavenUri(true));
            deployer.installToRemote(new File(FileLocator.toFileURL(entry1).getFile()), artifact, artifact.getType());
            artifact = MavenUrlHelper.parseMvnUrl(module2.getMavenUri(true));
            deployer.installToRemote(new File(FileLocator.toFileURL(entry2).getFile()), artifact, artifact.getType());
            artifact = MavenUrlHelper.parseMvnUrl(module3.getMavenUri(true));
            deployer.installToRemote(new File(FileLocator.toFileURL(entry3).getFile()), artifact, artifact.getType());
            artifact = MavenUrlHelper.parseMvnUrl(module4.getMavenUri(true));
            deployer.installToRemote(new File(FileLocator.toFileURL(entry4).getFile()), artifact, artifact.getType());
            artifact = MavenUrlHelper.parseMvnUrl(module5.getMavenUri(true));
            deployer.installToRemote(new File(FileLocator.toFileURL(entry5).getFile()), artifact, artifact.getType());

            boolean retrieve1 = libraryManagerService.retrieve(module1, null, false, null);
            assertTrue(retrieve1);
            assertTrue(new File(libraryManagerService.getJarPathFromMaven(module1.getMavenUri())).exists());
            assertEquals(module1.getStatus(), ELibraryInstallStatus.INSTALLED);

            retrieve1 = libraryManagerService.retrieve(module2, null, false, null);
            assertTrue(retrieve1);
            assertTrue(new File(libraryManagerService.getJarPathFromMaven(module2.getMavenUri())).exists());
            assertEquals(module2.getStatus(), ELibraryInstallStatus.INSTALLED);

            retrieve1 = libraryManagerService.retrieve(module3, null, false, null);
            assertTrue(retrieve1);
            assertTrue(new File(libraryManagerService.getJarPathFromMaven(module3.getMavenUri())).exists());
            assertEquals(module3.getStatus(), ELibraryInstallStatus.INSTALLED);

            retrieve1 = libraryManagerService.retrieve(module4, null, false, null);
            assertTrue(retrieve1);
            assertTrue(new File(libraryManagerService.getJarPathFromMaven(module4.getMavenUri())).exists());
            assertEquals(module4.getStatus(), ELibraryInstallStatus.INSTALLED);

            retrieve1 = libraryManagerService.retrieve(module5, null, false, null);
            assertTrue(retrieve1);
            assertTrue(new File(libraryManagerService.getJarPathFromMaven(module5.getMavenUri())).exists());
            assertEquals(module5.getStatus(), ELibraryInstallStatus.INSTALLED);
        } finally {
            node.putInt(ITalendCorePrefConstants.NEXUS_REFRESH_FREQUENCY, originalValue);
        }
    }

    @Test
    public void testDaysBetween() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Date sDate = sdf.parse("01/03/2016 13:00:00");
        Date eDate = sdf.parse("01/03/2016 14:00:00");
        Calendar sc = Calendar.getInstance();
        sc.setTime(sDate);

        Calendar ec = Calendar.getInstance();
        ec.setTime(eDate);

        LocalLibraryManager lm = new LocalLibraryManager();

        assertEquals(lm.daysBetween(sc, ec), 0);

        ec.setTime(sdf.parse("02/03/2016 12:00:00"));

        assertEquals(lm.daysBetween(sc, ec), 0);

        ec.setTime(sdf.parse("02/03/2016 15:00:00"));

        assertEquals(lm.daysBetween(sc, ec), 1);

        ec.setTime(sdf.parse("11/03/2016 15:00:00"));

        assertEquals(lm.daysBetween(sc, ec), 10);
    }

    @Test
    public void testResolvedAllowed() throws Exception {
        IEclipsePreferences node = InstanceScope.INSTANCE.getNode(NexusServerUtils.ORG_TALEND_DESIGNER_CORE);
        node.putInt(ITalendCorePrefConstants.NEXUS_REFRESH_FREQUENCY, -1);

        LocalLibraryManager lm = new LocalLibraryManager();

        assertFalse(lm.isResolveAllowed(null));

        node.putInt(ITalendCorePrefConstants.NEXUS_REFRESH_FREQUENCY, 0);
        assertTrue(lm.isResolveAllowed(null));

        node.putInt(ITalendCorePrefConstants.NEXUS_REFRESH_FREQUENCY, 1);
        IEclipsePreferences prefSetting = ConfigurationScope.INSTANCE.getNode("org.talend.librariesmanager");
        prefSetting.remove("lastUpdate");

        // never resolved, so will be true
        assertTrue(lm.isResolveAllowed("a")); //$NON-NLS-1$
        // last resolve not updated, so should be true still
        assertTrue(lm.isResolveAllowed("a")); //$NON-NLS-1$

        lm.updateLastResolveDate("a"); //$NON-NLS-1$
        // already resolved, should not allow the resolve again.
        assertFalse(lm.isResolveAllowed("a")); //$NON-NLS-1$
    }

    @Test @Ignore
    public void testNexusUpdateJar() throws Exception {
        String uri = "mvn:org.talend.libraries/test/6.0.0-SNAPSHOT/jar";
        TalendLibsServerManager manager = TalendLibsServerManager.getInstance();
        final NexusServerBean customNexusServer = manager.getCustomNexusServer();
        if (customNexusServer == null) {
            fail("Test not possible since Nexus is not setup");
        }

        String jarNeeded = "test.jar";

        LocalLibraryManager localLibraryManager = new LocalLibraryManager();
        Bundle bundle = Platform.getBundle("org.talend.librariesmanager.test");

        URL entry = bundle.getEntry("/lib/old/test.jar");
        File originalJarFile = new File(FileLocator.toFileURL(entry).getFile());

        entry = bundle.getEntry("/lib/new/test.jar");
        File newJarFile = new File(FileLocator.toFileURL(entry).getFile());

        // deploy jar on local + nexus
        localLibraryManager.deploy(originalJarFile.toURI(), null);
        String originalSHA1 = getSha1(originalJarFile);
        String newJarSHA1 = getSha1(newJarFile);

        MavenArtifact artifact = MavenUrlHelper.parseMvnUrl(uri);

        String remoteSha1 = NexusServerUtils.resolveSha1(customNexusServer.getServer(), customNexusServer.getUserName(),
                customNexusServer.getPassword(), customNexusServer.getSnapshotRepId(), artifact.getGroupId(),
                artifact.getArtifactId(), artifact.getVersion(), artifact.getType());
        assertEquals(originalSHA1, remoteSha1);
        // deploy the new jar to nexus (without update the local jar)
        new ArtifactsDeployer().installToRemote(newJarFile, artifact, "jar");
        remoteSha1 = NexusServerUtils.resolveSha1(customNexusServer.getServer(), customNexusServer.getUserName(),
                customNexusServer.getPassword(), customNexusServer.getSnapshotRepId(), artifact.getGroupId(),
                artifact.getArtifactId(), artifact.getVersion(), artifact.getType());
        assertEquals(newJarSHA1, remoteSha1);

        File resolvedFile = localLibraryManager.resolveJar(manager, customNexusServer, uri);
        assertNotNull(resolvedFile);
        String finalJarSHA1 = getSha1(resolvedFile);
        assertEquals(newJarSHA1, finalJarSHA1);
    }

    @Test @Ignore
    public void testNexusInstallNewJar() throws Exception {
        String uri = "mvn:org.talend.libraries/test/6.0.0-SNAPSHOT/jar";
        TalendLibsServerManager manager = TalendLibsServerManager.getInstance();
        final NexusServerBean customNexusServer = manager.getCustomNexusServer();
        if (customNexusServer == null) {
            fail("Test not possible since Nexus is not setup");
        }

        String jarNeeded = "test.jar";

        LocalLibraryManager localLibraryManager = new LocalLibraryManager();
        String localJarPath = localLibraryManager.getJarPathFromMaven(uri);
        // force to delete the jar to have a valid test
        if (localJarPath != null) {
            org.talend.utils.io.FilesUtils.deleteFolder(new File(localJarPath).getParentFile(), true);
        }
        // jar should not exist anymore
        assertNull(localLibraryManager.getJarPathFromMaven(uri));

        Bundle bundle = Platform.getBundle("org.talend.librariesmanager.test");
        MavenArtifact artifact = MavenUrlHelper.parseMvnUrl(uri);

        URL entry = bundle.getEntry("/lib/old/test.jar");
        File originalJarFile = new File(FileLocator.toFileURL(entry).getFile());
        // deploy the new jar to nexus (without update the local jar)
        new ArtifactsDeployer().installToRemote(originalJarFile, artifact, "jar");
        String originalSHA1 = getSha1(originalJarFile);

        // jar should not exist still on local
        assertNull(localLibraryManager.getJarPathFromMaven(uri));

        File resolvedFile = localLibraryManager.resolveJar(manager, customNexusServer, uri);
        assertNotNull(resolvedFile);
        String finalJarSHA1 = getSha1(resolvedFile);
        assertEquals(originalSHA1, finalJarSHA1);
    }

    @Test @Ignore
    public void testResolveSha1NotExist() throws Exception {
        String uri = "mvn:org.talend.libraries/not-existing/6.0.0-SNAPSHOT/jar";
        TalendLibsServerManager manager = TalendLibsServerManager.getInstance();
        final NexusServerBean customNexusServer = manager.getCustomNexusServer();
        if (customNexusServer == null) {
            fail("Test not possible since Nexus is not setup");
        }
        MavenArtifact artifact = MavenUrlHelper.parseMvnUrl(uri);
        String remoteSha1 = manager.resolveSha1(customNexusServer.getServer(), customNexusServer.getUserName(),
                customNexusServer.getPassword(), customNexusServer.getRepositoryId(), artifact.getGroupId(),
                artifact.getArtifactId(), artifact.getVersion(), artifact.getType());
        assertNull(remoteSha1);
    }

    private String getSha1(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        String sha1 = DigestUtils.shaHex(fis);
        fis.close();
        return sha1;
    }

    private String getSha1(String file) throws IOException {
        return getSha1(new File(file));
    }

    @Test
    public void testIsJarExistInLibFolder() throws IOException {
        File testJarFile = File.createTempFile("testLM", "jar");
        testJarFile.deleteOnExit();
        assertFalse(localLibraryManager.isJarExistInLibFolder(testJarFile));

        String librariesPath = LibrariesManagerUtils.getLibrariesPath(ECodeLanguage.JAVA);
        File storageDir = new File(librariesPath);
        FileUtils.copyFileToDirectory(testJarFile, storageDir);
        assertTrue(localLibraryManager.isJarExistInLibFolder(testJarFile));

        FileUtils.writeStringToFile(testJarFile, "Hello");
        assertFalse(localLibraryManager.isJarExistInLibFolder(testJarFile));
    }

    @Test @Ignore
    public void testIsLocalJarSameAsNexus() throws IOException {
        String uri = "mvn:org.talend.libraries/test/6.0.0-SNAPSHOT/jar";
        TalendLibsServerManager manager = TalendLibsServerManager.getInstance();
        final NexusServerBean customNexusServer = manager.getCustomNexusServer();
        if (customNexusServer == null) {
            fail("Test not possible since Nexus is not setup");
        }

        String localJarPath = localLibraryManager.getJarPathFromMaven(uri);
        // force to delete the jar to have a valid test
        if (localJarPath != null) {
            org.talend.utils.io.FilesUtils.deleteFolder(new File(localJarPath).getParentFile(), true);
        }
        // jar should not exist anymore
        assertNull(localLibraryManager.getJarPathFromMaven(uri));

        String testJarName = "test.jar";
        File jarFile = new File(getTmpFolder(), testJarName);
        FileUtils.writeStringToFile(jarFile, "Hello");
        assertFalse(localLibraryManager.isLocalJarSameAsNexus(manager, customNexusServer, testJarName));

        // deploy jar on local + nexus
        localLibraryManager.deploy(jarFile.toURI(), null);
        assertTrue(localLibraryManager.isLocalJarSameAsNexus(manager, customNexusServer, testJarName));

        File localJarFile = new File(localJarPath);
        FileUtils.writeStringToFile(localJarFile, "Talend");
        assertFalse(localLibraryManager.isLocalJarSameAsNexus(manager, customNexusServer, testJarName));
    }
    
    @Test
    public void testIsJarNeedToBeDeployed() throws Exception {
        assertFalse(localLibraryManager.isSvnLibSetup());
        Field field = null;
        Boolean originValue = null;
        TalendLibsServerManager libServerManager = TalendLibsServerManager.getInstance();
        if (libServerManager.getCustomNexusServer() != null) {
            field = TalendLibsServerManager.class.getDeclaredField("lastConnectionValid");
            field.setAccessible(true);
            originValue = (Boolean) field.get(libServerManager);
            field.set(libServerManager, false);
            assertNull(libServerManager.getCustomNexusServer());
        }
        String uri = "mvn:org.talend.libraries/test/6.0.0-SNAPSHOT/jar";
        String localJarPath = localLibraryManager.getJarPathFromMaven(uri);
        // force to delete the jar to have a valid test
        if (localJarPath != null) {
            org.talend.utils.io.FilesUtils.deleteFolder(new File(localJarPath).getParentFile(), true);
        }
        // jar should not exist anymore
        assertNull(localLibraryManager.getJarPathFromMaven(uri));
        
        String testJarName = "test.jar";
        File jarFile = new File(getTmpFolder(), testJarName);
        FileUtils.writeStringToFile(jarFile, "Hello");
        assertTrue(localLibraryManager.isJarNeedToBeDeployed(jarFile));
        
        localLibraryManager.deploy(jarFile.toURI(), null);
        assertFalse(localLibraryManager.isJarNeedToBeDeployed(jarFile));
        
        if (field != null) {
            field.set(libServerManager, originValue);
        }
    }

    private File getTmpFolder() {
        Project project = ProjectManager.getInstance().getCurrentProject();
        IProject physProject;
        String tmpFolderPath = System.getProperty("user.dir"); //$NON-NLS-1$
        try {
            physProject = ResourceUtils.getProject(project.getTechnicalLabel());
            tmpFolderPath = physProject.getFolder("temp").getLocation().toPortableString(); //$NON-NLS-1$
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        tmpFolderPath = tmpFolderPath + "/testLib"; //$NON-NLS-1$

        return new File(tmpFolderPath);
    }

}
