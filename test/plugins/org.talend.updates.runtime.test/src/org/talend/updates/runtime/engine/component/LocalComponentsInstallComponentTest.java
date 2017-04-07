// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.updates.runtime.engine.component;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.model.Model;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.commons.runtime.service.ComponentsInstallComponent;
import org.talend.commons.utils.resource.BundleFileUtil;
import org.talend.librariesmanager.maven.ArtifactsDeployer;
import org.talend.librariesmanager.prefs.LibrariesManagerUtils;
import org.talend.updates.runtime.engine.InstalledUnit;
import org.talend.updates.runtime.engine.P2Installer;
import org.talend.updates.runtime.engine.P2InstallerTest;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class LocalComponentsInstallComponentTest {

    class LocalComponentsInstallComponentTestClass extends LocalComponentsInstallComponent {

    }

    File tmpFolder = null;

    @Before
    public void prepare() {
        tmpFolder = org.talend.utils.files.FileUtils.createTmpFolder("test", "comp"); //$NON-NLS-1$  //$NON-NLS-2$
    }

    @After
    public void clean() {
        if (tmpFolder != null) {
            FilesUtils.deleteFolder(tmpFolder, true);
        }
    }

    @Test
    public void test_getUserComponentFolder_setCustom() {
        LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponentTestClass();
        installComp.setComponentFolder(tmpFolder);

        final File installingComponentFolder = installComp.getUserComponentFolder();
        Assert.assertEquals(tmpFolder, installingComponentFolder);
    }

    @Test
    public void test_getUserComponentFolder_setPreference() {
        LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponentTestClass();

        ScopedPreferenceStore preferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, "org.talend.designer.codegen");
        final String name = "USER_COMPONENTS_FOLDER";
        String oldSetting = preferenceStore.getString(name); //$NON-NLS-1$
        try {
            preferenceStore.setValue(name, tmpFolder.getAbsolutePath());

            final File installingComponentFolder = installComp.getUserComponentFolder();
            Assert.assertEquals(tmpFolder, installingComponentFolder);
        } finally {
            preferenceStore.setValue(name, oldSetting);
        }

    }

    @Test
    public void test_getUserComponentFolder_setPreference_notExisted() throws IOException {
        LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponentTestClass();

        ScopedPreferenceStore preferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, "org.talend.designer.codegen");
        final String name = "USER_COMPONENTS_FOLDER";
        String oldSetting = preferenceStore.getString(name);
        try {
            preferenceStore.setValue(name, "abc");

            final File installingComponentFolder = installComp.getUserComponentFolder();

            // use product folder as default
            final File prodFolder = new File(Platform.getInstallLocation()
                    .getDataArea(LocalComponentsInstallComponent.FOLDER_COMPS).getPath());
            Assert.assertEquals(prodFolder, installingComponentFolder);
        } finally {
            preferenceStore.setValue(name, oldSetting);
        }

    }

    @Test
    public void test_getUserComponentFolder_noPreference() throws IOException {
        ScopedPreferenceStore preferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, "org.talend.designer.codegen");
        final String name = "USER_COMPONENTS_FOLDER";
        String oldSetting = preferenceStore.getString(name);
        try {
            preferenceStore.setValue(name, "");

            LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponentTestClass();
            final File installingComponentFolder = installComp.getUserComponentFolder();
            final File prodFolder = new File(Platform.getInstallLocation()
                    .getDataArea(LocalComponentsInstallComponent.FOLDER_COMPS).getPath());
            Assert.assertEquals(prodFolder, installingComponentFolder);
        } finally {
            preferenceStore.setValue(name, oldSetting);
        }
    }

    @Test
    public void test_installFromFolder_null() {
        LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponentTestClass();

        final Map<File, Set<InstalledUnit>> installed = installComp.installFromFolder(null);
        Assert.assertNotNull(installed);
        Assert.assertTrue(installed.isEmpty());

        final List<File> failedComponents = installComp.getFailedComponents();
        Assert.assertNotNull(failedComponents);
        Assert.assertTrue(failedComponents.isEmpty());

        Assert.assertNull(installComp.getInstalledMessages());

        File installedFolder = new File(tmpFolder, ComponentsInstallComponent.FOLDER_INSTALLED);
        Assert.assertFalse(installedFolder.exists());
    }

    @Test
    public void test_installFromFolder_notExisted() {
        LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponentTestClass();

        final Map<File, Set<InstalledUnit>> installed = installComp.installFromFolder(new File("xyz"));
        Assert.assertNotNull(installed);
        Assert.assertTrue(installed.isEmpty());

        final List<File> failedComponents = installComp.getFailedComponents();
        Assert.assertNotNull(failedComponents);
        Assert.assertTrue(failedComponents.isEmpty());

        Assert.assertNull(installComp.getInstalledMessages());

        File installedFolder = new File(tmpFolder, ComponentsInstallComponent.FOLDER_INSTALLED);
        Assert.assertFalse(installedFolder.exists());
    }

    @Test
    public void test_installFromFolder_file() throws IOException {
        File file = new File(tmpFolder, "abc.txt");
        file.createNewFile();
        LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponentTestClass();

        final Map<File, Set<InstalledUnit>> installed = installComp.installFromFolder(file);
        Assert.assertNotNull(installed);
        Assert.assertTrue(installed.isEmpty());

        final List<File> failedComponents = installComp.getFailedComponents();
        Assert.assertNotNull(failedComponents);
        Assert.assertTrue(failedComponents.isEmpty());

        Assert.assertNull(installComp.getInstalledMessages());

        File installedFolder = new File(tmpFolder, ComponentsInstallComponent.FOLDER_INSTALLED);
        Assert.assertFalse(installedFolder.exists());
    }

    @Test
    public void test_installFromFolder_empty() throws IOException {
        LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponentTestClass();

        final Map<File, Set<InstalledUnit>> installed = installComp.installFromFolder(tmpFolder);
        Assert.assertNotNull(installed);
        Assert.assertTrue(installed.isEmpty());

        final List<File> failedComponents = installComp.getFailedComponents();
        Assert.assertNotNull(failedComponents);
        Assert.assertTrue(failedComponents.isEmpty());

        Assert.assertNull(installComp.getInstalledMessages());

        File installedFolder = new File(tmpFolder, ComponentsInstallComponent.FOLDER_INSTALLED);
        Assert.assertFalse(installedFolder.exists());
    }

    @Test
    public void test_installFromFolder_updatesite_folder() throws Exception {
        final File testDataFile = BundleFileUtil.getBundleFile(this.getClass(), P2InstallerTest.TEST_COMP_MYJIRA);
        Assert.assertNotNull(testDataFile);
        Assert.assertTrue(testDataFile.exists());

        FilesUtils.unzip(testDataFile.getAbsolutePath(), tmpFolder.getAbsolutePath());

        LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponentTestClass();

        final Map<File, Set<InstalledUnit>> installed = installComp.installFromFolder(tmpFolder);
        Assert.assertNotNull(installed);
        Assert.assertTrue(installed.isEmpty());

        final List<File> failedComponents = installComp.getFailedComponents();
        Assert.assertNotNull(failedComponents);
        Assert.assertTrue(failedComponents.isEmpty());

        Assert.assertNull(installComp.getInstalledMessages());

        File installedFolder = new File(tmpFolder, ComponentsInstallComponent.FOLDER_INSTALLED);
        Assert.assertFalse(installedFolder.exists());
    }

    @Test
    public void test_installFromFolder_updatesite_folder_componentName() throws Exception {
        final File testDataFile = BundleFileUtil.getBundleFile(this.getClass(), P2InstallerTest.TEST_COMP_MYJIRA);
        Assert.assertNotNull(testDataFile);
        Assert.assertTrue(testDataFile.exists());

        FilesUtils.unzip(testDataFile.getAbsolutePath(), new File(tmpFolder, new Path(testDataFile.getName())
                .removeFileExtension().lastSegment()).getAbsolutePath());

        LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponentTestClass();

        final Map<File, Set<InstalledUnit>> installed = installComp.installFromFolder(tmpFolder);
        Assert.assertNotNull(installed);
        Assert.assertTrue(installed.isEmpty());

        final List<File> failedComponents = installComp.getFailedComponents();
        Assert.assertNotNull(failedComponents);
        Assert.assertTrue(failedComponents.isEmpty());

        Assert.assertNull(installComp.getInstalledMessages());

        File installedFolder = new File(tmpFolder, ComponentsInstallComponent.FOLDER_INSTALLED);
        Assert.assertFalse(installedFolder.exists());
    }

    @Test
    public void test_installFromFolder_updatesite_file() throws Exception {
        final File testDataFile = BundleFileUtil.getBundleFile(this.getClass(), P2InstallerTest.TEST_COMP_MYJIRA);
        Assert.assertNotNull(testDataFile);
        Assert.assertTrue(testDataFile.exists());

        final File target = new File(tmpFolder, testDataFile.getName());
        FilesUtils.copyFile(testDataFile, target);

        LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponentTestClass() {

            @Override
            protected P2Installer createInstaller() {
                return new P2Installer() {

                    @Override
                    public Set<InstalledUnit> installPatchFile(File updatesiteFile, boolean keepChangeConfigIni)
                            throws Exception, ProvisionException {
                        Set<InstalledUnit> installedUnits = new HashSet<InstalledUnit>();
                        installedUnits.add(new InstalledUnit("org.talend.components.mytest", "1.0.1"));
                        return installedUnits;
                    }

                };
            }

        };
        final Map<File, Set<InstalledUnit>> installed = installComp.installFromFolder(tmpFolder);
        Assert.assertNotNull(installed);
        Assert.assertEquals(1, installed.size());
        final File installingFile = installed.keySet().iterator().next();
        Assert.assertEquals(target, installingFile);
        final Set<InstalledUnit> set = installed.get(installingFile);
        Assert.assertEquals(1, set.size());
        final InstalledUnit iu = set.iterator().next();
        Assert.assertEquals("org.talend.components.mytest", iu.getBundle());
        Assert.assertEquals("1.0.1", iu.getVersion());

        final List<File> failedComponents = installComp.getFailedComponents();
        Assert.assertNotNull(failedComponents);
        Assert.assertTrue(failedComponents.isEmpty());

        // Assert.assertNotNull(installComp.getInstalledMessages());

        // move to installed
        File installedFolder = new File(tmpFolder, ComponentsInstallComponent.FOLDER_INSTALLED);
        Assert.assertTrue(installedFolder.exists());

        File installedCompFile = new File(installedFolder, testDataFile.getName());
        Assert.assertTrue(installedCompFile.exists());
        Assert.assertFalse(target.exists());
        Assert.assertEquals(testDataFile.getName(), installedCompFile.getName());

        final long originalChecksumAlder32 = FilesUtils.getChecksumAlder32(testDataFile);
        final long checksumAlder32 = FilesUtils.getChecksumAlder32(installedCompFile);
        Assert.assertEquals(originalChecksumAlder32, checksumAlder32);
    }

    @Test
    public void test_doInstall_withoutLogin() throws Exception {
        final File testDataFile = BundleFileUtil.getBundleFile(this.getClass(), P2InstallerTest.TEST_COMP_MYJIRA);
        Assert.assertNotNull(testDataFile);
        Assert.assertTrue(testDataFile.exists());

        final File userCompFolder = new File(tmpFolder, "user");
        userCompFolder.mkdir();
        final File userCompFile = new File(userCompFolder, testDataFile.getName());
        FilesUtils.copyFile(testDataFile, userCompFile);

        final File patchesFolder = new File(tmpFolder, "patches");
        patchesFolder.mkdir();
        final File patchFile = new File(patchesFolder, testDataFile.getName());
        FilesUtils.copyFile(testDataFile, patchFile);

        LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponentTestClass() {

            @Override
            protected File getUserComponentFolder() {
                return userCompFolder;
            }

            @Override
            protected File getPatchesFolder() {
                return patchesFolder;
            }

            @Override
            protected P2Installer createInstaller() {
                return new P2Installer() {

                    @Override
                    public Set<InstalledUnit> installPatchFile(File updatesiteFile, boolean keepChangeConfigIni)
                            throws Exception, ProvisionException {
                        Set<InstalledUnit> installedUnits = new HashSet<InstalledUnit>();
                        if (updatesiteFile.equals(userCompFile)) {
                            installedUnits.add(new InstalledUnit("org.talend.components.mytest1", "1.0.1"));
                        } else if (updatesiteFile.equals(patchFile)) {
                            installedUnits.add(new InstalledUnit("org.talend.components.mytest2", "1.0.2"));
                        }
                        return installedUnits;
                    }

                };
            }

        };
        installComp.setLogin(false); // only for user component

        final boolean install = installComp.doInstall();
        Assert.assertTrue("Install failure", install);

        Assert.assertTrue(installComp.needRelaunch());
        Assert.assertNotNull(installComp.getInstalledMessages());

        final List<File> failedComponents = installComp.getFailedComponents();
        Assert.assertNotNull(failedComponents);
        Assert.assertTrue(failedComponents.isEmpty());

        // move to installed
        final long originalChecksumAlder32 = FilesUtils.getChecksumAlder32(testDataFile);

        File userCompInstalledFolder = new File(userCompFolder, ComponentsInstallComponent.FOLDER_INSTALLED);
        Assert.assertTrue(userCompInstalledFolder.exists());
        File userInstalledCompFile = new File(userCompInstalledFolder, testDataFile.getName());
        Assert.assertTrue(userInstalledCompFile.exists());
        Assert.assertFalse(userCompFile.exists());
        Assert.assertEquals(testDataFile.getName(), userInstalledCompFile.getName());
        Assert.assertEquals(originalChecksumAlder32, FilesUtils.getChecksumAlder32(userInstalledCompFile));

        File patchInstalledFolder = new File(patchesFolder, ComponentsInstallComponent.FOLDER_INSTALLED);
        Assert.assertFalse(patchInstalledFolder.exists());
        File patchInstalledCompFile = new File(patchInstalledFolder, testDataFile.getName());
        Assert.assertFalse(patchInstalledCompFile.exists());
        Assert.assertTrue(patchFile.exists()); // still existed

    }

    @Test
    public void test_doInstall_withLogin() throws Exception {
        final File testDataFile = BundleFileUtil.getBundleFile(this.getClass(), P2InstallerTest.TEST_COMP_MYJIRA);
        Assert.assertNotNull(testDataFile);
        Assert.assertTrue(testDataFile.exists());

        final File userCompFolder = new File(tmpFolder, "user");
        userCompFolder.mkdir();
        final File userCompFile = new File(userCompFolder, testDataFile.getName());
        FilesUtils.copyFile(testDataFile, userCompFile);

        final File patchesFolder = new File(tmpFolder, "patches");
        patchesFolder.mkdir();
        final File patchFile = new File(patchesFolder, testDataFile.getName());
        FilesUtils.copyFile(testDataFile, patchFile);

        LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponentTestClass() {

            @Override
            protected File getUserComponentFolder() {
                return userCompFolder;
            }

            @Override
            protected File getPatchesFolder() {
                return patchesFolder;
            }

            @Override
            protected P2Installer createInstaller() {
                return new P2Installer() {

                    @Override
                    public Set<InstalledUnit> installPatchFile(File updatesiteFile, boolean keepChangeConfigIni)
                            throws Exception, ProvisionException {
                        Set<InstalledUnit> installedUnits = new HashSet<InstalledUnit>();
                        if (updatesiteFile.equals(userCompFile)) {
                            installedUnits.add(new InstalledUnit("org.talend.components.mytest1", "1.0.1"));
                        } else if (updatesiteFile.equals(patchFile)) {
                            installedUnits.add(new InstalledUnit("org.talend.components.mytest2", "1.0.2"));
                        }
                        return installedUnits;
                    }

                };
            }

        };
        try {
            installComp.setLogin(true); // add patches folder

            final boolean install = installComp.doInstall();
            Assert.assertTrue("Install failure", install);

            Assert.assertTrue(installComp.needRelaunch());
            Assert.assertNotNull(installComp.getInstalledMessages());

            final List<File> failedComponents = installComp.getFailedComponents();
            Assert.assertNotNull(failedComponents);
            Assert.assertTrue(failedComponents.isEmpty());

            // move to installed
            final long originalChecksumAlder32 = FilesUtils.getChecksumAlder32(testDataFile);

            File userCompInstalledFolder = new File(userCompFolder, ComponentsInstallComponent.FOLDER_INSTALLED);
            Assert.assertTrue(userCompInstalledFolder.exists());
            File userInstalledCompFile = new File(userCompInstalledFolder, testDataFile.getName());
            Assert.assertTrue(userInstalledCompFile.exists());
            Assert.assertFalse(userCompFile.exists());
            Assert.assertEquals(testDataFile.getName(), userInstalledCompFile.getName());
            Assert.assertEquals(originalChecksumAlder32, FilesUtils.getChecksumAlder32(userInstalledCompFile));

            File patchInstalledFolder = new File(patchesFolder, ComponentsInstallComponent.FOLDER_INSTALLED);
            Assert.assertTrue(patchInstalledFolder.exists());
            File patchInstalledCompFile = new File(patchInstalledFolder, testDataFile.getName());
            Assert.assertTrue(patchInstalledCompFile.exists());
            Assert.assertFalse(patchFile.exists());
            Assert.assertEquals(testDataFile.getName(), patchInstalledCompFile.getName());
            Assert.assertEquals(originalChecksumAlder32, FilesUtils.getChecksumAlder32(patchInstalledCompFile));
        } finally {
            installComp.setLogin(false);
        }
    }

    @Test
    public void test_doInstall_hasFailure() throws Exception {
        final File testDataFile = BundleFileUtil.getBundleFile(this.getClass(), P2InstallerTest.TEST_COMP_MYJIRA);
        Assert.assertNotNull(testDataFile);
        Assert.assertTrue(testDataFile.exists());

        final File userCompFolder = new File(tmpFolder, "user");
        userCompFolder.mkdir();
        final File userCompFile = new File(userCompFolder, testDataFile.getName());
        FilesUtils.copyFile(testDataFile, userCompFile);

        final File patchesFolder = new File(tmpFolder, "patches");
        patchesFolder.mkdir();
        final File patchFile = new File(patchesFolder, testDataFile.getName());
        FilesUtils.copyFile(testDataFile, patchFile);

        LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponentTestClass() {

            @Override
            protected File getUserComponentFolder() {
                return userCompFolder;
            }

            @Override
            protected File getPatchesFolder() {
                return patchesFolder;
            }

            @Override
            protected P2Installer createInstaller() {
                return new P2Installer() {

                    @Override
                    public Set<InstalledUnit> installPatchFile(File updatesiteFile, boolean keepChangeConfigIni)
                            throws Exception, ProvisionException {
                        Set<InstalledUnit> installedUnits = new HashSet<InstalledUnit>();
                        if (updatesiteFile.equals(userCompFile)) {
                            installedUnits.add(new InstalledUnit("org.talend.components.mytest1", "1.0.1"));
                        } else if (updatesiteFile.equals(patchFile)) {
                            throw new RuntimeException("install failure from patch");
                        }
                        return installedUnits;
                    }

                };
            }

        };
        try {
            installComp.setLogin(true); // only for user component

            final boolean install = installComp.doInstall();
            Assert.assertTrue("Install failure", install);

            Assert.assertTrue(installComp.needRelaunch());
            Assert.assertNotNull(installComp.getInstalledMessages());

            final List<File> failedComponents = installComp.getFailedComponents();
            Assert.assertNotNull(failedComponents);
            Assert.assertEquals(1, failedComponents.size());
            final File file = failedComponents.get(0);
            Assert.assertEquals(patchFile, file); // install failure file

            // move to installed
            final long originalChecksumAlder32 = FilesUtils.getChecksumAlder32(testDataFile);

            File userCompInstalledFolder = new File(userCompFolder, ComponentsInstallComponent.FOLDER_INSTALLED);
            Assert.assertTrue(userCompInstalledFolder.exists());
            File userInstalledCompFile = new File(userCompInstalledFolder, testDataFile.getName());
            Assert.assertTrue(userInstalledCompFile.exists());
            Assert.assertFalse(userCompFile.exists());
            Assert.assertEquals(testDataFile.getName(), userInstalledCompFile.getName());
            Assert.assertEquals(originalChecksumAlder32, FilesUtils.getChecksumAlder32(userInstalledCompFile));

            File patchInstalledFolder = new File(patchesFolder, ComponentsInstallComponent.FOLDER_INSTALLED);
            Assert.assertFalse(patchInstalledFolder.exists());
            File patchInstalledCompFile = new File(patchInstalledFolder, testDataFile.getName());
            Assert.assertFalse(patchInstalledCompFile.exists());
            Assert.assertTrue(patchFile.exists()); // still existed
        } finally {
            installComp.setLogin(false);
        }

    }

    @Test
    public void test_syncLibraries_nonExisted() throws IOException {
        final String oldLibPath = System.getProperty(LibrariesManagerUtils.TALEND_LIBRARY_PATH);
        try {
            // set prod lib
            final File testProdLibFolder = new File(tmpFolder, "prod/" + LibrariesManagerUtils.LIB_JAVA_SUB_FOLDER);
            System.setProperty(LibrariesManagerUtils.TALEND_LIBRARY_PATH, testProdLibFolder.getAbsolutePath());
            Assert.assertFalse(testProdLibFolder.exists());

            final File testFile = new File(tmpFolder, "test");

            LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponentTestClass();
            installComp.syncLibraries(testFile);

            Assert.assertFalse(testProdLibFolder.exists());

        } finally {
            if (oldLibPath != null) {
                System.setProperty(LibrariesManagerUtils.TALEND_LIBRARY_PATH, oldLibPath);
            } else {
                System.clearProperty(LibrariesManagerUtils.TALEND_LIBRARY_PATH);
            }

        }
    }

    @Test
    public void test_syncLibraries_nonFolder() throws IOException {
        final String oldLibPath = System.getProperty(LibrariesManagerUtils.TALEND_LIBRARY_PATH);
        try {
            // set prod lib
            final File testProdLibFolder = new File(tmpFolder, "prod/" + LibrariesManagerUtils.LIB_JAVA_SUB_FOLDER);
            System.setProperty(LibrariesManagerUtils.TALEND_LIBRARY_PATH, testProdLibFolder.getAbsolutePath());
            Assert.assertFalse(testProdLibFolder.exists());

            final File testFile = new File(tmpFolder, "test.txt");
            testFile.createNewFile();

            LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponentTestClass();
            installComp.syncLibraries(testFile);

            Assert.assertFalse(testProdLibFolder.exists());

        } finally {
            if (oldLibPath != null) {
                System.setProperty(LibrariesManagerUtils.TALEND_LIBRARY_PATH, oldLibPath);
            } else {
                System.clearProperty(LibrariesManagerUtils.TALEND_LIBRARY_PATH);
            }

        }
    }

    @Test
    public void test_syncLibraries() throws IOException {
        final File testDataFile = BundleFileUtil.getBundleFile(this.getClass(), P2InstallerTest.TEST_COMP_MYJIRA);
        Assert.assertNotNull(testDataFile);
        Assert.assertTrue(testDataFile.exists());
        final String oldLibPath = System.getProperty(LibrariesManagerUtils.TALEND_LIBRARY_PATH);
        try {
            // set prod lib
            final File testProdLibFolder = new File(tmpFolder, "prod/" + LibrariesManagerUtils.LIB_JAVA_SUB_FOLDER);
            System.setProperty(LibrariesManagerUtils.TALEND_LIBRARY_PATH, testProdLibFolder.getAbsolutePath());

            final File testFolder = new File(tmpFolder, "test");
            final File testLibFolder = new File(testFolder, LibrariesManagerUtils.LIB_JAVA_SUB_FOLDER);
            FilesUtils.copyFile(testDataFile, new File(testLibFolder, testDataFile.getName()));

            Assert.assertFalse(testProdLibFolder.exists());

            LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponentTestClass();
            installComp.syncLibraries(testFolder);

            Assert.assertTrue(testProdLibFolder.exists());
            final String[] list = testProdLibFolder.list();
            Assert.assertEquals(1, list.length);
            Assert.assertEquals(testDataFile.getName(), list[0]);

        } finally {
            if (oldLibPath != null) {
                System.setProperty(LibrariesManagerUtils.TALEND_LIBRARY_PATH, oldLibPath);
            } else {
                System.clearProperty(LibrariesManagerUtils.TALEND_LIBRARY_PATH);
            }

        }
    }

    @Test
    public void test_installM2RepositoryLibs() throws Exception {
        final File testDataFile = BundleFileUtil.getBundleFile(this.getClass(), P2InstallerTest.TEST_COMP_MYJIRA);
        Assert.assertNotNull(testDataFile);
        Assert.assertTrue(testDataFile.exists());

        FilesUtils.unzip(testDataFile.getAbsolutePath(), tmpFolder.getAbsolutePath());

        File updatesiteLibFolder = new File(tmpFolder, ComponentsInstallComponent.FOLDER_M2_REPOSITORY); // m2/repositroy
        Assert.assertTrue(updatesiteLibFolder.exists());
        Assert.assertTrue(updatesiteLibFolder.isDirectory());

        // check from local
        final String localRepositoryPath = MavenPlugin.getMaven().getLocalRepositoryPath();
        Assert.assertNotNull(localRepositoryPath);
        File localRepoFolder = new File(localRepositoryPath);
        Assert.assertTrue(localRepoFolder.exists());

        File libFile = new File(localRepoFolder, "org/talend/libraries/mytest/6.0.0/mytest-6.0.0.jar");
        if (libFile.exists()) {
            libFile.delete();
        }
        File pomFile = new File(localRepoFolder, "org/talend/libraries/mytest/6.0.0/mytest-6.0.0.pom");
        if (pomFile.exists()) {
            pomFile.delete();
        }

        LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponentTestClass();
        installComp.installM2RepositoryLibs(updatesiteLibFolder, new ArtifactsDeployer());

        Assert.assertTrue("sync lib for M2 repository failure", libFile.exists());
        Assert.assertTrue(pomFile.exists());

        // make sure the pom is original one. not generated new one.
        Model model = MavenPlugin.getMaven().readModel(pomFile);
        Assert.assertNotNull(model);
        Assert.assertNotNull(model.getDescription());
        Assert.assertEquals("It's a test jar", model.getDescription()); // same description

    }

    @Test
    public void test_syncM2Repository() throws Exception {
        final File testDataFile = BundleFileUtil.getBundleFile(this.getClass(), P2InstallerTest.TEST_COMP_MYJIRA);
        Assert.assertNotNull(testDataFile);
        Assert.assertTrue(testDataFile.exists());

        FilesUtils.unzip(testDataFile.getAbsolutePath(), tmpFolder.getAbsolutePath());

        // check from local
        final String localRepositoryPath = MavenPlugin.getMaven().getLocalRepositoryPath();
        Assert.assertNotNull(localRepositoryPath);
        File localRepoFolder = new File(localRepositoryPath);
        Assert.assertTrue(localRepoFolder.exists());

        File libFile = new File(localRepoFolder, "org/talend/libraries/mytest/6.0.0/mytest-6.0.0.jar");
        if (libFile.exists()) {
            libFile.delete();
        }
        File pomFile = new File(localRepoFolder, "org/talend/libraries/mytest/6.0.0/mytest-6.0.0.pom");
        if (pomFile.exists()) {
            pomFile.delete();
        }

        LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponentTestClass();
        installComp.syncM2Repository(tmpFolder);

        Assert.assertTrue("sync lib for M2 repository failure", libFile.exists());
        Assert.assertTrue(pomFile.exists());

    }

    @Test
    public void test_afterInstall_null() throws IOException {
        LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponentTestClass();
        installComp.afterInstall(tmpFolder, null);

        File installedFolder = new File(tmpFolder, ComponentsInstallComponent.FOLDER_INSTALLED);
        Assert.assertFalse(installedFolder.exists());
    }

    @Test
    public void test_afterInstall_notExisted() throws IOException {
        LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponentTestClass();
        installComp.afterInstall(tmpFolder, new File("abc.txt"));

        File installedFolder = new File(tmpFolder, ComponentsInstallComponent.FOLDER_INSTALLED);
        Assert.assertFalse(installedFolder.exists());
    }

    @Test
    public void test_afterInstall_notFile() throws IOException {
        File folder = new File(tmpFolder, "abc");
        folder.mkdir();
        LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponentTestClass();
        installComp.afterInstall(tmpFolder, folder);

        File installedFolder = new File(tmpFolder, ComponentsInstallComponent.FOLDER_INSTALLED);
        Assert.assertFalse(installedFolder.exists());
    }

    @Test
    public void test_afterInstall_File() throws IOException {
        final File testDataFile = BundleFileUtil.getBundleFile(this.getClass(), P2InstallerTest.TEST_COMP_MYJIRA);
        Assert.assertNotNull(testDataFile);
        Assert.assertTrue(testDataFile.exists());

        final File target = new File(tmpFolder, testDataFile.getName());
        FilesUtils.copyFile(testDataFile, new File(tmpFolder, testDataFile.getName()));
        LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponentTestClass();
        installComp.afterInstall(tmpFolder, target);

        File installedFolder = new File(tmpFolder, ComponentsInstallComponent.FOLDER_INSTALLED);
        Assert.assertTrue(installedFolder.exists());

        File installedFile = new File(installedFolder, testDataFile.getName());
        Assert.assertTrue(installedFile.exists());
        Assert.assertFalse(target.exists());
        Assert.assertEquals(testDataFile.getName(), installedFile.getName());

        final long originalChecksumAlder32 = FilesUtils.getChecksumAlder32(testDataFile);
        final long checksumAlder32 = FilesUtils.getChecksumAlder32(installedFile);
        Assert.assertEquals(originalChecksumAlder32, checksumAlder32);
    }
}
