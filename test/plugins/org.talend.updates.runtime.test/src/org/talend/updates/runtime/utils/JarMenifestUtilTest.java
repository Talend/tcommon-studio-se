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
package org.talend.updates.runtime.utils;

import java.io.File;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.talend.commons.utils.resource.BundleFileUtil;
import org.talend.updates.runtime.nexus.component.ComponentIndexManagerTest;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class JarMenifestUtilTest {

    File tmpFolder = null;

    @Before
    public void prepare() throws Exception {
        tmpFolder = org.talend.utils.files.FileUtils.createTmpFolder("test", "jar"); //$NON-NLS-1$  //$NON-NLS-2$
    }

    @After
    public void clean() throws Exception {
        if (tmpFolder != null) {
            FilesUtils.deleteFolder(tmpFolder, true);
        }
    }

    @Test
    public void test_getManifest_null() throws Exception {
        Manifest manifest = JarMenifestUtil.getManifest(null);
        Assert.assertNull(manifest);
    }

    @Test
    public void test_getManifest_nonExisted() throws Exception {
        Manifest manifest = JarMenifestUtil.getManifest(new File("abc.txt"));
        Assert.assertNull(manifest);
    }

    @Test
    public void test_getManifest_dir() throws Exception {
        Manifest manifest = JarMenifestUtil.getManifest(tmpFolder);
        Assert.assertNull(manifest);
    }

    @Test
    public void test_getManifest_nonJar() throws Exception {
        File testFile = new File(tmpFolder, "abc.txt");
        testFile.createNewFile();
        Manifest manifest = JarMenifestUtil.getManifest(testFile);
        Assert.assertNull(manifest);
    }

    @Test(expected = IOException.class)
    public void test_getManifest_wrongJar() throws Exception {
        File dataFile = BundleFileUtil.getBundleFile(this.getClass(), ComponentIndexManagerTest.PATH_640_INDEX_FILE);
        File testFile = new File(tmpFolder, "abc.jar");
        FilesUtils.copyFile(dataFile, testFile);
        Assert.assertTrue("Copy test file failure", testFile.exists());

        Manifest manifest = JarMenifestUtil.getManifest(testFile);
        Assert.assertNull(manifest);
    }

    @Test
    public void test_getManifest() throws Exception {
        File testBundle100File = BundleFileUtil.getBundleFile(OsgiBundleInstallerTest.class,
                OsgiBundleInstallerTest.TEST_BUNDLE_100);
        Assert.assertTrue(testBundle100File.exists());
        Manifest manifest = JarMenifestUtil.getManifest(testBundle100File);
        Assert.assertNotNull(manifest);

        String bundleSymbolicName = JarMenifestUtil.getBundleSymbolicName(manifest);
        Assert.assertEquals("org.talend.bundle.test20170607", bundleSymbolicName);

        String bundleVersion = JarMenifestUtil.getBundleVersion(manifest);
        Assert.assertEquals("1.0.0", bundleVersion);
    }

    @Test
    public void test_getBundleSymbolicName_singleton() {
        Manifest manifest = Mockito.mock(Manifest.class);
        Attributes attrs = new Attributes();
        attrs.put(new Attributes.Name("Bundle-SymbolicName"), "org.talend.test;singleton:=true");
        Mockito.doReturn(attrs).when(manifest).getMainAttributes();

        String bundleSymbolicName = JarMenifestUtil.getBundleSymbolicName(manifest);
        Assert.assertEquals("org.talend.test", bundleSymbolicName);
    }

    @Test
    public void test_getBundleVersion() {
        Manifest manifest = Mockito.mock(Manifest.class);
        Attributes attrs = new Attributes();
        attrs.put(new Attributes.Name("Bundle-Version"), "1.2.3");
        Mockito.doReturn(attrs).when(manifest).getMainAttributes();

        String bundleVersion = JarMenifestUtil.getBundleVersion(manifest);
        Assert.assertEquals("1.2.3", bundleVersion);
    }
}
