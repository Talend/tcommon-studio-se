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
import java.util.List;

import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.utils.resource.BundleFileUtil;
import org.talend.updates.runtime.engine.P2InstallerTest;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class LocalComponentsInstallComponentTest {

    class LocalComponentsInstallComponentTestClass extends LocalComponentsInstallComponent {

    }

    File tmpFolder = null;

    File installedFolder = null;

    @Before
    public void prepare() throws IOException {
        tmpFolder = org.talend.utils.files.FileUtils.createTmpFolder("test", "comp"); //$NON-NLS-1$  //$NON-NLS-2$
        installedFolder = new File(Platform.getConfigurationLocation().getDataArea(LocalComponentsInstallComponent.FOLDER_COMPS)
                .getPath());
        cleanM2TempFolder();
    }

    @After
    public void clean() {
        if (tmpFolder != null) {
            FilesUtils.deleteFolder(tmpFolder, true);
        }
        cleanM2TempFolder();
    }

    private void cleanM2TempFolder() {
        LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponentTestClass();
        final File tempM2RepoFolder = installComp.getTempM2RepoFolder();
        if (tempM2RepoFolder != null) {
            FilesUtils.deleteFolder(tempM2RepoFolder, true);
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
        String oldSetting = preferenceStore.getString(name);
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

        installComp.installFromFolder(null);

        final List<File> failedComponents = installComp.getFailedComponents();
        Assert.assertNotNull(failedComponents);
        Assert.assertTrue(failedComponents.isEmpty());

        Assert.assertNull(installComp.getInstalledMessages());
    }

    @Test
    public void test_installFromFolder_notExisted() {
        LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponentTestClass();

        installComp.installFromFolder(new File("xyz"));

        final List<File> failedComponents = installComp.getFailedComponents();
        Assert.assertNotNull(failedComponents);
        Assert.assertTrue(failedComponents.isEmpty());

        Assert.assertNull(installComp.getInstalledMessages());
    }

    @Test
    public void test_installFromFolder_file() throws IOException {
        File file = new File(tmpFolder, "abc.txt");
        file.createNewFile();
        LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponentTestClass();

        installComp.installFromFolder(file);

        final List<File> failedComponents = installComp.getFailedComponents();
        Assert.assertNotNull(failedComponents);
        Assert.assertTrue(failedComponents.isEmpty());

        Assert.assertNull(installComp.getInstalledMessages());
    }

    @Test
    public void test_installFromFolder_empty() throws IOException {
        LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponentTestClass();

        installComp.installFromFolder(tmpFolder);

        final List<File> failedComponents = installComp.getFailedComponents();
        Assert.assertNotNull(failedComponents);
        Assert.assertTrue(failedComponents.isEmpty());

        Assert.assertNull(installComp.getInstalledMessages());
    }

    @Test
    public void test_installFromFolder_updatesite_folder() throws Exception {
        final File testDataFile = BundleFileUtil.getBundleFile(this.getClass(), P2InstallerTest.TEST_COMP_MYJIRA);
        Assert.assertNotNull(testDataFile);
        Assert.assertTrue(testDataFile.exists());

        FilesUtils.unzip(testDataFile.getAbsolutePath(), tmpFolder.getAbsolutePath());

        LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponentTestClass();

        installComp.installFromFolder(tmpFolder);

        final List<File> failedComponents = installComp.getFailedComponents();
        Assert.assertNotNull(failedComponents);
        Assert.assertTrue(failedComponents.isEmpty());

        Assert.assertNull(installComp.getInstalledMessages());
    }

    @Test
    public void test_installFromFolder_updatesite_folder_componentName() throws Exception {
        final File testDataFile = BundleFileUtil.getBundleFile(this.getClass(), P2InstallerTest.TEST_COMP_MYJIRA);
        Assert.assertNotNull(testDataFile);
        Assert.assertTrue(testDataFile.exists());

        FilesUtils.unzip(testDataFile.getAbsolutePath(), new File(tmpFolder, new Path(testDataFile.getName())
                .removeFileExtension().lastSegment()).getAbsolutePath());

        LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponentTestClass();

        installComp.installFromFolder(tmpFolder);

        final List<File> failedComponents = installComp.getFailedComponents();
        Assert.assertNotNull(failedComponents);
        Assert.assertTrue(failedComponents.isEmpty());

        Assert.assertNull(installComp.getInstalledMessages());
    }

    @Test
    public void test_installFromFolder_updatesite_file() throws Exception {
        if (!CommonsPlugin.isDebugMode() && Platform.inDevelopmentMode()) {
            return; // only enable to test in product
        }

        final File testDataFile = BundleFileUtil.getBundleFile(this.getClass(), P2InstallerTest.TEST_COMP_MYJIRA);
        Assert.assertNotNull(testDataFile);
        Assert.assertTrue(testDataFile.exists());

        final File target = new File(tmpFolder, testDataFile.getName());
        FilesUtils.copyFile(testDataFile, target);

        LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponentTestClass();
        installComp.installFromFolder(tmpFolder);

        final List<File> failedComponents = installComp.getFailedComponents();
        Assert.assertNotNull(failedComponents);
        Assert.assertTrue(failedComponents.isEmpty());

        // Assert.assertNotNull(installComp.getInstalledMessages());

        // move to installed
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
        if (!CommonsPlugin.isDebugMode() && Platform.inDevelopmentMode()) {
            return; // only enable to test in product
        }

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

        Assert.assertTrue(installedFolder.exists());

        File userInstalledCompFile = new File(installedFolder, testDataFile.getName());
        Assert.assertTrue(userInstalledCompFile.exists());
        Assert.assertFalse(userCompFile.exists());
        Assert.assertEquals(testDataFile.getName(), userInstalledCompFile.getName());
        Assert.assertEquals(originalChecksumAlder32, FilesUtils.getChecksumAlder32(userInstalledCompFile));

        File patchInstalledCompFile = new File(installedFolder, testDataFile.getName());
        Assert.assertFalse(patchInstalledCompFile.exists());
        Assert.assertTrue(patchFile.exists()); // still existed

    }

    @Test
    public void test_doInstall_withLogin() throws Exception {
        if (!CommonsPlugin.isDebugMode() && Platform.inDevelopmentMode()) {
            return; // only enable to test in product
        }

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

            Assert.assertTrue(installedFolder.exists());

            File userInstalledCompFile = new File(installedFolder, testDataFile.getName());
            Assert.assertTrue(userInstalledCompFile.exists());
            Assert.assertFalse(userCompFile.exists());
            Assert.assertEquals(testDataFile.getName(), userInstalledCompFile.getName());
            Assert.assertEquals(originalChecksumAlder32, FilesUtils.getChecksumAlder32(userInstalledCompFile));

            File patchInstalledCompFile = new File(installedFolder, testDataFile.getName());
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
        if (!CommonsPlugin.isDebugMode() && Platform.inDevelopmentMode()) {
            return; // only enable to test in product
        }

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

            Assert.assertTrue(installedFolder.exists());

            File userInstalledCompFile = new File(installedFolder, testDataFile.getName());
            Assert.assertTrue(userInstalledCompFile.exists());
            Assert.assertFalse(userCompFile.exists());
            Assert.assertEquals(testDataFile.getName(), userInstalledCompFile.getName());
            Assert.assertEquals(originalChecksumAlder32, FilesUtils.getChecksumAlder32(userInstalledCompFile));

            File patchInstalledCompFile = new File(installedFolder, testDataFile.getName());
            Assert.assertFalse(patchInstalledCompFile.exists());
            Assert.assertTrue(patchFile.exists()); // still existed
        } finally {
            installComp.setLogin(false);
        }

    }

}
