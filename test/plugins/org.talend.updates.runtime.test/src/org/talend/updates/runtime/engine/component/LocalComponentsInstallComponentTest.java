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

import org.apache.maven.model.Model;
import org.eclipse.core.runtime.Platform;
import org.eclipse.m2e.core.MavenPlugin;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.commons.runtime.service.ComponentsInstallComponent;
import org.talend.commons.utils.resource.BundleFileUtil;
import org.talend.librariesmanager.maven.ArtifactsDeployer;
import org.talend.librariesmanager.prefs.LibrariesManagerUtils;
import org.talend.updates.runtime.engine.P2InstallerTest;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class LocalComponentsInstallComponentTest {

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
    public void test_getUserComponentFolder() {
        LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponent();
        final File installingComponentFolder = installComp.getUserComponentFolder();
        Assert.assertEquals(new File(Platform.getInstallLocation().getURL().getFile(),
                LocalComponentsInstallComponent.FOLDER_COMPS), installingComponentFolder);
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

            LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponent();
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

            LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponent();
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

            LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponent();
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

        LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponent();
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

        LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponent();
        installComp.syncM2Repository(tmpFolder);

        Assert.assertTrue("sync lib for M2 repository failure", libFile.exists());
        Assert.assertTrue(pomFile.exists());

    }

}
