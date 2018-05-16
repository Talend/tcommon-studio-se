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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.Version;
import org.talend.commons.utils.resource.BundleFileUtil;
import org.talend.commons.utils.resource.UpdatesHelper;
import org.talend.updates.runtime.nexus.component.ComponentIndexManagerTest;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class OsgiBundleInstallerTest {

    public static final String TEST_BUNDLE_100 = "resources/bundles/org.talend.bundle.test20170607_1.0.0.jar";

    public static final String TEST_BUNDLE_200 = "resources/bundles/org.talend.bundle.test20170607_2.0.0.jar";

    public static final String TEST_BUNDLE_201 = "resources/bundles/org.talend.bundle.test20170607_2.0.1.jar";

    public static final String TEST_SYMBOLIC_NAME = "org.talend.bundle.test20170607";

    public static final String TEST_PROP_KEY = TEST_SYMBOLIC_NAME + ".helloworld";

    private static File pluginsFolder;

    private static List<File> testBundles;

    @BeforeClass
    public static void preStart() throws IOException {
        testBundles = new ArrayList<File>();

        File testBundle100File = BundleFileUtil.getBundleFile(OsgiBundleInstallerTest.class, TEST_BUNDLE_100);
        File testBundle200File = BundleFileUtil.getBundleFile(OsgiBundleInstallerTest.class, TEST_BUNDLE_200);
        File testBundle201File = BundleFileUtil.getBundleFile(OsgiBundleInstallerTest.class, TEST_BUNDLE_201);
        Assert.assertNotNull(testBundle100File);
        Assert.assertTrue(testBundle100File.exists());
        Assert.assertNotNull(testBundle200File);
        Assert.assertTrue(testBundle200File.exists());
        Assert.assertNotNull(testBundle201File);
        Assert.assertTrue(testBundle201File.exists());

        pluginsFolder = new File(Platform.getInstallLocation().getDataArea(UpdatesHelper.FOLDER_PLUGINS).getPath());

        copyBundle(testBundle100File);
        copyBundle(testBundle200File);
        copyBundle(testBundle201File);
    }

    private static void copyBundle(File src) throws IOException {
        File target = new File(pluginsFolder, src.getName());
        FilesUtils.copyFile(src, target);
        Assert.assertTrue("Copy the bundle failure", target.exists());
        testBundles.add(target);

    }

    @AfterClass
    public static void cleanup() throws Exception {
        uninstallTestBundle();

        if (testBundles != null) {
            for (File b : testBundles) {
                b.delete();
            }
        }
        testBundles = null;
    }

    File tmpFolder = null;

    @Before
    public void prepare() throws Exception {
        tmpFolder = org.talend.utils.files.FileUtils.createTmpFolder("test", "bundle"); //$NON-NLS-1$  //$NON-NLS-2$
    }

    @After
    public void clean() throws Exception {
        if (tmpFolder != null) {
            FilesUtils.deleteFolder(tmpFolder, true);
        }
        uninstallTestBundle();
    }

    private static void uninstallTestBundle() throws BundleException {
        Bundle bundle = Platform.getBundle(TEST_SYMBOLIC_NAME);
        if (bundle != null) {
            bundle.stop();
            bundle.uninstall();
        }
        bundle = Platform.getBundle(TEST_SYMBOLIC_NAME);
        Assert.assertNull("The test bundle should be uninstalled or never existed.", bundle);

        // remove the test key
        if (System.getProperties().containsKey(TEST_PROP_KEY)) {
            System.getProperties().remove(TEST_PROP_KEY);
        }
    }

    private void doTestBundle(String version) {
        Bundle bundle = Platform.getBundle(TEST_SYMBOLIC_NAME);
        Assert.assertNotNull("The bundle is not loaded", bundle);
        Assert.assertEquals("The loaded bundle is different version", new Version(version), bundle.getVersion());
    }

    @Test
    public void test_installAndStartBundle_null() throws Exception {
        boolean started = OsgiBundleInstaller.installAndStartBundle(null);
        Assert.assertFalse("Can't start the bundle", started);
        Assert.assertFalse("No property set yet", System.getProperties().containsKey(TEST_PROP_KEY));
    }

    @Test
    public void test_installAndStartBundle_nonExisted() throws Exception {
        boolean started = OsgiBundleInstaller.installAndStartBundle(new File("abc.txt"));
        Assert.assertFalse("Can't start the bundle", started);
        Assert.assertFalse("No property set yet", System.getProperties().containsKey(TEST_PROP_KEY));
    }

    @Test
    public void test_installAndStartBundle_dir() throws Exception {
        boolean started = OsgiBundleInstaller.installAndStartBundle(tmpFolder);
        Assert.assertFalse("Can't start the bundle", started);
        Assert.assertFalse("No property set yet", System.getProperties().containsKey(TEST_PROP_KEY));
    }

    @Test
    public void test_installAndStartBundle_nonJar() throws Exception {
        File testFile = new File(tmpFolder, "abc.txt");
        testFile.createNewFile();
        boolean started = OsgiBundleInstaller.installAndStartBundle(testFile);
        Assert.assertFalse("Can't start the bundle", started);
        Assert.assertFalse("No property set yet", System.getProperties().containsKey(TEST_PROP_KEY));
    }

    @Test(expected = IOException.class)
    public void test_installAndStartBundle_wrongJar() throws Exception {
        File dataFile = BundleFileUtil.getBundleFile(this.getClass(), ComponentIndexManagerTest.PATH_640_INDEX_FILE);
        File testFile = new File(tmpFolder, "abc.jar");
        FilesUtils.copyFile(dataFile, testFile);
        Assert.assertTrue("Copy test file failure", testFile.exists());

        OsgiBundleInstaller.installAndStartBundle(testFile);
    }

    @Test
    public void test_installAndStartBundle_single() throws Exception {
        Assert.assertEquals("Init the test bundle error", 3, testBundles.size());

        boolean started = OsgiBundleInstaller.installAndStartBundle(testBundles.get(0)); // 1.0.0
        Assert.assertTrue("Can't start the bundle", started);
        doTestBundle("1.0.0");
        Assert.assertFalse("No property set yet", System.getProperties().containsKey(TEST_PROP_KEY));
    }

    @Test
    public void test_installAndStartBundle_sameVersion() throws Exception {
        Assert.assertEquals("Init the test bundle error", 3, testBundles.size());

        boolean started = OsgiBundleInstaller.installAndStartBundle(testBundles.get(0)); // 1.0.0
        Assert.assertTrue("Can't start the bundle", started);
        doTestBundle("1.0.0");
        Assert.assertFalse("No property set yet", System.getProperties().containsKey(TEST_PROP_KEY));

        started = OsgiBundleInstaller.installAndStartBundle(testBundles.get(0)); // 1.0.0
        Assert.assertFalse("Can't start the bundle with same version again", started);
        doTestBundle("1.0.0");
        Assert.assertFalse("No property set yet", System.getProperties().containsKey(TEST_PROP_KEY));

    }

    @Test
    public void test_installAndStartBundle_newVersion() throws Exception {
        test_installAndStartBundle_single();

        boolean started = OsgiBundleInstaller.installAndStartBundle(testBundles.get(1)); // 2.0.0
        Assert.assertTrue("Can't start the bundle", started);
        doTestBundle("2.0.0");
        Assert.assertEquals("Should be set the value", "Hello World", System.getProperty(TEST_PROP_KEY));

        started = OsgiBundleInstaller.installAndStartBundle(testBundles.get(2)); // 2.0.1
        Assert.assertTrue("Can't start the bundle", started);
        doTestBundle("2.0.1");
        Assert.assertEquals("Should update the value", "Hello World!", System.getProperty(TEST_PROP_KEY));
    }

    @Test
    public void test_installAndStartBundle_oldVersion() throws Exception {
        test_installAndStartBundle_single();

        boolean started = OsgiBundleInstaller.installAndStartBundle(testBundles.get(2)); // 2.0.1
        Assert.assertTrue("Can't start the bundle", started);
        doTestBundle("2.0.1");
        Assert.assertEquals("Should be set the value", "Hello World!", System.getProperty(TEST_PROP_KEY));

        started = OsgiBundleInstaller.installAndStartBundle(testBundles.get(0)); // 1.0.0
        Assert.assertFalse("Shouldn't start old version", started);
        doTestBundle("2.0.1"); // still be latest version
        // still same latest value
        Assert.assertEquals("Should update the value", "Hello World!", System.getProperty(TEST_PROP_KEY));
    }
}
