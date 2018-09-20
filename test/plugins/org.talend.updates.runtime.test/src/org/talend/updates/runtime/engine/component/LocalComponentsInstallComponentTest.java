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
package org.talend.updates.runtime.engine.component;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.utils.resource.BundleFileUtil;
import org.talend.updates.runtime.engine.P2InstallerTest;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.model.ExtraFeatureException;
import org.talend.updates.runtime.model.P2ExtraFeatureException;
import org.talend.updates.runtime.nexus.component.ComponentIndexBean;
import org.talend.updates.runtime.nexus.component.ComponentIndexManager;
import org.talend.updates.runtime.utils.PathUtils;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class LocalComponentsInstallComponentTest {

    class LocalComponentsInstallComponentTestClass extends LocalComponentsInstallComponent {

    }

    File tmpFolder = null;

    @Before
    public void prepare() throws IOException {
        tmpFolder = org.talend.utils.files.FileUtils.createTmpFolder("test", "comp"); //$NON-NLS-1$  //$NON-NLS-2$
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
        final File tempM2RepoFolder = PathUtils.getComponentsM2TempFolder();
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

        ComponentIndexBean index = new ComponentIndexManager().create(testDataFile);
        Assert.assertNotNull(index);

        final File target = new File(tmpFolder, testDataFile.getName());
        FilesUtils.copyFile(testDataFile, target);
        final File installedFolder = new File(tmpFolder, "installed");

        try {
            LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponentTestClass() {

                @Override
                protected ComponentP2ExtraFeature createComponentFeature(File f) {
                    return new ComponentP2ExtraFeature(f) {

                        @Override
                        public IStatus install(IProgressMonitor progress, List<URI> allRepoUris) throws ExtraFeatureException {
                            setUseLegacyP2Install(false); // no need change the config.ini for test
                            return super.install(progress, allRepoUris);
                        }

                        @Override
                        public void shareComponent(IProgressMonitor progress, File installedCompFile) {

                            File sharedCompFile = new File(installedFolder, installedCompFile.getName());
                            try {
                                FilesUtils.copyFile(installedCompFile, sharedCompFile);
                            } catch (IOException e) {
                                //
                            }
                            boolean deleted = installedCompFile.delete();
                            if (!deleted) {// failed to delete in time
                                installedCompFile.deleteOnExit(); // try to delete when exit
                            }

                        }

                    };
                }

            };
            installComp.installFromFolder(tmpFolder);

            final List<File> failedComponents = installComp.getFailedComponents();
            Assert.assertNotNull(failedComponents);
            Assert.assertTrue(failedComponents.isEmpty());

            // move to installed
            Assert.assertTrue(installedFolder.exists());

            File installedCompFile = new File(installedFolder, testDataFile.getName());
            Assert.assertTrue(installedCompFile.exists());
            Assert.assertFalse(target.exists());
            Assert.assertEquals(testDataFile.getName(), installedCompFile.getName());

            final long originalChecksumAlder32 = FilesUtils.getChecksumAlder32(testDataFile);
            final long checksumAlder32 = FilesUtils.getChecksumAlder32(installedCompFile);
            Assert.assertEquals(originalChecksumAlder32, checksumAlder32);

            Bundle bundle = Platform.getBundle(index.getBundleId());
            Assert.assertNotNull("Component should be loaded", bundle);
        } finally {
            // uninstall the bundle, make sure the test component, won't break the product, just for test
            Bundle bundle = Platform.getBundle(index.getBundleId());
            if (bundle != null) {
                if (bundle.getState() == Bundle.ACTIVE) {
                    bundle.stop();
                }
                bundle.uninstall();
            }
        }
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
            protected void installFromFolder(File componentBaseFolder) {
                Assert.assertFalse("Shouldn't deal with the patches folder", componentBaseFolder.equals(patchesFolder));
                super.installFromFolder(componentBaseFolder);
            }

            @Override
            protected ComponentP2ExtraFeature createComponentFeature(File f) {
                return new ComponentP2ExtraFeature(f) {

                    @Override
                    public boolean canBeInstalled(IProgressMonitor progress) throws P2ExtraFeatureException {
                        return true;
                    }

                    @Override
                    public IStatus install(IProgressMonitor progress, List<URI> allRepoUris) throws P2ExtraFeatureException {
                        return Messages.createOkStatus("sucessfull.install.of.components", getP2IuId(), getVersion()); //$NON-NLS-1$
                    }

                    @Override
                    public boolean needRestart() {
                        return false;
                    }
                };
            }

        };
        installComp.setLogin(false); // only for user component

        final boolean install = installComp.doInstall();
        Assert.assertTrue("Install failure", install);

        Assert.assertFalse("Install success, no need restart", installComp.needRelaunch());
        Assert.assertNotNull(installComp.getInstalledMessages());

        final List<File> failedComponents = installComp.getFailedComponents();
        Assert.assertNotNull(failedComponents);
        Assert.assertTrue(failedComponents.isEmpty());
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

        final List<File> doneList = new ArrayList<File>();
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
            protected void installFromFolder(File componentBaseFolder) {
                doneList.add(componentBaseFolder);
                super.installFromFolder(componentBaseFolder);
            }

            @Override
            protected ComponentP2ExtraFeature createComponentFeature(File f) {
                return new ComponentP2ExtraFeature(f) {

                    @Override
                    public boolean canBeInstalled(IProgressMonitor progress) throws P2ExtraFeatureException {
                        return true;
                    }

                    @Override
                    public IStatus install(IProgressMonitor progress, List<URI> allRepoUris) throws P2ExtraFeatureException {
                        return Messages.createOkStatus("sucessfull.install.of.components", getP2IuId(), getVersion()); //$NON-NLS-1$
                    }

                    @Override
                    public boolean needRestart() {
                        return false;
                    }
                };
            }
        };
        try {
            installComp.setLogin(true); // add patches folder

            final boolean install = installComp.doInstall();
            Assert.assertTrue("Install failure", install);

            Assert.assertFalse("Install success, no need restart", installComp.needRelaunch());
            Assert.assertNotNull(installComp.getInstalledMessages());

            final List<File> failedComponents = installComp.getFailedComponents();
            Assert.assertNotNull(failedComponents);
            Assert.assertTrue(failedComponents.isEmpty());

            Assert.assertEquals("Must check all of the user and patches folder", 2, doneList.size());
            Assert.assertTrue("Must deal with the user component folder", doneList.contains(userCompFolder));
            Assert.assertTrue("Must deal with the patches folder", doneList.contains(patchesFolder));
        } finally {
            installComp.setLogin(false);
        }
    }

    @Test
    public void test_doInstall_hasFailure() throws Exception {
        LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponentTestClass() {

            @Override
            protected void installFromFolder(File componentBaseFolder) {
                throw new RuntimeException();
            }
        };
        boolean installed = installComp.doInstall();
        Assert.assertFalse("Should install failure", installed);
    }

    @Test
    public void test_doInstall_hasPatchFolderFailure() throws Exception {
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

        final URI patchURI = PathUtils.getP2RepURIFromCompFile(patchFile);

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
            protected ComponentP2ExtraFeature createComponentFeature(File f) {
                return new ComponentP2ExtraFeature(f) {

                    @Override
                    public boolean canBeInstalled(IProgressMonitor progress) throws P2ExtraFeatureException {
                        return true;
                    }

                    @Override
                    public IStatus install(IProgressMonitor progress, List<URI> allRepoUris) throws P2ExtraFeatureException {
                        if (allRepoUris.contains(patchURI)) {
                            throw new RuntimeException();
                        }
                        return Messages.createOkStatus("sucessfull.install.of.components", getP2IuId(), getVersion()); //$NON-NLS-1$
                    }

                    @Override
                    public boolean needRestart() {
                        return false;
                    }
                };
            }
        };
        try {
            installComp.setLogin(true); // when install patch folder will have error

            final boolean install = installComp.doInstall();
            Assert.assertTrue("Install failure", install);

            Assert.assertFalse("Install success, no need restart", installComp.needRelaunch());
            Assert.assertNotNull(installComp.getInstalledMessages());

            final List<File> failedComponents = installComp.getFailedComponents();
            Assert.assertNotNull(failedComponents);
            Assert.assertEquals(1, failedComponents.size());
            final File file = failedComponents.get(0);
            Assert.assertEquals(patchFile, file); // install failure file
        } finally {
            installComp.setLogin(false);
        }

    }

}
