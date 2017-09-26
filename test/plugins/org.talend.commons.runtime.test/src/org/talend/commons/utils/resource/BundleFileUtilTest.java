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
package org.talend.commons.utils.resource;

import java.io.File;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.eclipse.core.runtime.Platform;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class BundleFileUtilTest {

    File tmpFolder = null;

    @Before
    public void prepare() throws Exception {
        tmpFolder = org.talend.utils.files.FileUtils.createTmpFolder("bundle", "test"); //$NON-NLS-1$  //$NON-NLS-2$
    }

    @After
    public void clean() throws Exception {
        if (tmpFolder != null) {
            FilesUtils.deleteFolder(tmpFolder, true);
        }
    }

    @Test
    public void test_getBundleFile4Class() throws IOException {
        File bundleFile = BundleFileUtil.getBundleFile((Class) null, "");
        Assert.assertNull(bundleFile);

        bundleFile = BundleFileUtil.getBundleFile(this.getClass(), "data/TextFile.txt");
        Assert.assertTrue(bundleFile.exists());

        testPath(bundleFile);
    }

    private void testPath(File bundleFile) {
        final String path = bundleFile.getAbsolutePath().replace('\\', '/');
        if (Platform.inDevelopmentMode()) {
            Assert.assertTrue(path.endsWith("/org.talend.commons.runtime.test/data/TextFile.txt"));
        } else {
            // if jar
            if (path.contains("org.eclipse.osgi")) {
                Assert.assertTrue(path.contains("/configuration/org.eclipse.osgi/")); // in osgi
            } else {
                // if foler, becasue the folder contained version.
                Assert.assertTrue(path.contains("/plugins/org.talend.commons.runtime.test"));
            }
        }
    }

    @Test
    public void test_getBundleFile4Bundle() throws IOException {
        File bundleFile = BundleFileUtil.getBundleFile((Bundle) null, "");
        Assert.assertNull(bundleFile);

        bundleFile = BundleFileUtil.getBundleFile(FrameworkUtil.getBundle(this.getClass()), "data/TextFile.txt");
        Assert.assertTrue(bundleFile.exists());

        testPath(bundleFile);
    }

    @Test
    public void test_getManifest_null() throws Exception {
        Manifest manifest = BundleFileUtil.getManifest(null);
        Assert.assertNull(manifest);
    }

    @Test
    public void test_getManifest_nonExisted() throws Exception {
        Manifest manifest = BundleFileUtil.getManifest(new File("abc.txt"));
        Assert.assertNull(manifest);
    }

    @Test
    public void test_getManifest_dir() throws Exception {
        Manifest manifest = BundleFileUtil.getManifest(tmpFolder);
        Assert.assertNull(manifest);
    }

    @Test
    public void test_getManifest_nonJar() throws Exception {
        File testFile = new File(tmpFolder, "abc.txt");
        testFile.createNewFile();
        Manifest manifest = BundleFileUtil.getManifest(testFile);
        Assert.assertNull(manifest);
    }

    @Test
    public void test_getBundleSymbolicName_null() {
        String bundleSymbolicName = BundleFileUtil.getBundleSymbolicName(null);
        Assert.assertNull(bundleSymbolicName);
    }

    @Test
    public void test_getBundleSymbolicName_noMainAttr() {
        Manifest manifest = Mockito.mock(Manifest.class);
        String bundleSymbolicName = BundleFileUtil.getBundleSymbolicName(manifest);
        Assert.assertNull(bundleSymbolicName);
    }

    @Test
    public void test_getBundleSymbolicName_noAttr() {
        Manifest manifest = Mockito.mock(Manifest.class);
        Attributes attrs = new Attributes();
        Mockito.doReturn(attrs).when(manifest).getMainAttributes();

        String bundleSymbolicName = BundleFileUtil.getBundleSymbolicName(manifest);
        Assert.assertNull(bundleSymbolicName);
    }

    @Test
    public void test_getBundleSymbolicName_normal() {
        Manifest manifest = Mockito.mock(Manifest.class);
        Attributes attrs = new Attributes();
        attrs.put(new Attributes.Name("Bundle-SymbolicName"), "org.talend.test");
        Mockito.doReturn(attrs).when(manifest).getMainAttributes();

        String bundleSymbolicName = BundleFileUtil.getBundleSymbolicName(manifest);
        Assert.assertEquals("org.talend.test", bundleSymbolicName);
    }

    @Test
    public void test_getBundleSymbolicName_singleton() {
        Manifest manifest = Mockito.mock(Manifest.class);
        Attributes attrs = new Attributes();
        attrs.put(new Attributes.Name("Bundle-SymbolicName"), "org.talend.test;singleton:=true");
        Mockito.doReturn(attrs).when(manifest).getMainAttributes();

        String bundleSymbolicName = BundleFileUtil.getBundleSymbolicName(manifest);
        Assert.assertEquals("org.talend.test", bundleSymbolicName);
    }

    @Test
    public void test_getBundleVersion_null() {
        String bundleVersion = BundleFileUtil.getBundleVersion(null);
        Assert.assertNull(bundleVersion);
    }

    @Test
    public void test_getBundleVersion_noMainAttr() {
        Manifest manifest = Mockito.mock(Manifest.class);
        String bundleVersion = BundleFileUtil.getBundleVersion(manifest);
        Assert.assertNull(bundleVersion);
    }

    @Test
    public void test_getBundleVersion_noAttr() {
        Manifest manifest = Mockito.mock(Manifest.class);
        Attributes attrs = new Attributes();
        Mockito.doReturn(attrs).when(manifest).getMainAttributes();
        String bundleVersion = BundleFileUtil.getBundleVersion(manifest);
        Assert.assertNull(bundleVersion);
    }

    @Test
    public void test_getBundleVersion_normal() {
        Manifest manifest = Mockito.mock(Manifest.class);
        Attributes attrs = new Attributes();
        attrs.put(new Attributes.Name("Bundle-Version"), "1.2.3");
        Mockito.doReturn(attrs).when(manifest).getMainAttributes();

        String bundleVersion = BundleFileUtil.getBundleVersion(manifest);
        Assert.assertEquals("1.2.3", bundleVersion);
    }

    @Test
    public void test_getBundleClassPath_null() {
        final String[] bundleClassPath = BundleFileUtil.getBundleClassPath(null);
        Assert.assertNotNull(bundleClassPath);
        Assert.assertEquals(0, bundleClassPath.length);
    }

    @Test
    public void test_getBundleClassPath_noMainAttr() {
        Manifest manifest = Mockito.mock(Manifest.class);
        final String[] bundleClassPath = BundleFileUtil.getBundleClassPath(manifest);
        Assert.assertNotNull(bundleClassPath);
        Assert.assertEquals(0, bundleClassPath.length);
    }

    @Test
    public void test_getBundleClassPath_noAttr() {
        Manifest manifest = Mockito.mock(Manifest.class);
        Attributes attrs = new Attributes();
        Mockito.doReturn(attrs).when(manifest).getMainAttributes();
        final String[] bundleClassPath = BundleFileUtil.getBundleClassPath(manifest);
        Assert.assertNotNull(bundleClassPath);
        Assert.assertEquals(0, bundleClassPath.length);
    }

    @Test
    public void test_getBundleClassPath_one() {
        Manifest manifest = Mockito.mock(Manifest.class);
        Attributes attrs = new Attributes();
        attrs.put(new Attributes.Name("Bundle-ClassPath"), "abc.jar");
        Mockito.doReturn(attrs).when(manifest).getMainAttributes();

        final String[] bundleClassPath = BundleFileUtil.getBundleClassPath(manifest);
        Assert.assertNotNull(bundleClassPath);
        Assert.assertEquals(1, bundleClassPath.length);
        Assert.assertEquals("abc.jar", bundleClassPath[0]);
    }

    @Test
    public void test_getBundleClassPath_withPath() {
        Manifest manifest = Mockito.mock(Manifest.class);
        Attributes attrs = new Attributes();
        attrs.put(new Attributes.Name("Bundle-ClassPath"), "lib/abc.jar");
        Mockito.doReturn(attrs).when(manifest).getMainAttributes();

        final String[] bundleClassPath = BundleFileUtil.getBundleClassPath(manifest);
        Assert.assertNotNull(bundleClassPath);
        Assert.assertEquals(1, bundleClassPath.length);
        Assert.assertEquals("lib/abc.jar", bundleClassPath[0]);
    }

    @Test
    public void test_getBundleClassPath_multi() {
        Manifest manifest = Mockito.mock(Manifest.class);
        Attributes attrs = new Attributes();
        attrs.put(new Attributes.Name("Bundle-ClassPath"), "lib/abc.jar,lib/xyz.jar");
        Mockito.doReturn(attrs).when(manifest).getMainAttributes();

        final String[] bundleClassPath = BundleFileUtil.getBundleClassPath(manifest);
        Assert.assertNotNull(bundleClassPath);
        Assert.assertEquals(2, bundleClassPath.length);
        Assert.assertEquals("lib/abc.jar", bundleClassPath[0]);
        Assert.assertEquals("lib/xyz.jar", bundleClassPath[1]);
    }

    @Test
    public void test_getBundleClassPath_multiWithCurrent_start() {
        Manifest manifest = Mockito.mock(Manifest.class);
        Attributes attrs = new Attributes();
        attrs.put(new Attributes.Name("Bundle-ClassPath"), ".,lib/abc.jar,lib/xyz.jar");
        Mockito.doReturn(attrs).when(manifest).getMainAttributes();

        final String[] bundleClassPath = BundleFileUtil.getBundleClassPath(manifest);
        Assert.assertNotNull(bundleClassPath);
        Assert.assertEquals(3, bundleClassPath.length);
        Assert.assertEquals(".", bundleClassPath[0]);
        Assert.assertEquals("lib/abc.jar", bundleClassPath[1]);
        Assert.assertEquals("lib/xyz.jar", bundleClassPath[2]);
    }

    @Test
    public void test_getBundleClassPath_multiWithCurrent_mid() {
        Manifest manifest = Mockito.mock(Manifest.class);
        Attributes attrs = new Attributes();
        attrs.put(new Attributes.Name("Bundle-ClassPath"), "lib/abc.jar,.,lib/xyz.jar");
        Mockito.doReturn(attrs).when(manifest).getMainAttributes();

        final String[] bundleClassPath = BundleFileUtil.getBundleClassPath(manifest);
        Assert.assertNotNull(bundleClassPath);
        Assert.assertEquals(3, bundleClassPath.length);
        Assert.assertEquals("lib/abc.jar", bundleClassPath[0]);
        Assert.assertEquals(".", bundleClassPath[1]);
        Assert.assertEquals("lib/xyz.jar", bundleClassPath[2]);
    }

    @Test
    public void test_getBundleClassPath_multiWithCurrent_end() {
        Manifest manifest = Mockito.mock(Manifest.class);
        Attributes attrs = new Attributes();
        attrs.put(new Attributes.Name("Bundle-ClassPath"), "lib/abc.jar,lib/xyz.jar,.");
        Mockito.doReturn(attrs).when(manifest).getMainAttributes();

        final String[] bundleClassPath = BundleFileUtil.getBundleClassPath(manifest);
        Assert.assertNotNull(bundleClassPath);
        Assert.assertEquals(3, bundleClassPath.length);
        Assert.assertEquals("lib/abc.jar", bundleClassPath[0]);
        Assert.assertEquals("lib/xyz.jar", bundleClassPath[1]);
        Assert.assertEquals(".", bundleClassPath[2]);
    }

    @Test
    public void test_isInBundleClassPath_emptyPath() {
        Manifest manifest = Mockito.mock(Manifest.class);

        boolean inBundleClassPath = BundleFileUtil.isInBundleClassPath(manifest, null);
        Assert.assertFalse(inBundleClassPath);

        inBundleClassPath = BundleFileUtil.isInBundleClassPath(manifest, "");
        Assert.assertFalse(inBundleClassPath);

        inBundleClassPath = BundleFileUtil.isInBundleClassPath(manifest, "  ");
        Assert.assertFalse(inBundleClassPath);
    }

    @Test
    public void test_isInBundleClassPath_with() {
        Manifest manifest = Mockito.mock(Manifest.class);
        Attributes attrs = new Attributes();
        attrs.put(new Attributes.Name("Bundle-ClassPath"), "lib/abc.jar,lib/xyz.jar,.");
        Mockito.doReturn(attrs).when(manifest).getMainAttributes();

        boolean inBundleClassPath = BundleFileUtil.isInBundleClassPath(manifest, "lib/abc.jar");
        Assert.assertTrue(inBundleClassPath);

        inBundleClassPath = BundleFileUtil.isInBundleClassPath(manifest, "lib/xyz.jar");
        Assert.assertTrue(inBundleClassPath);

        inBundleClassPath = BundleFileUtil.isInBundleClassPath(manifest, ".");
        Assert.assertTrue(inBundleClassPath);
    }

    @Test
    public void test_isInBundleClassPath_without() {
        Manifest manifest = Mockito.mock(Manifest.class);
        Attributes attrs = new Attributes();
        attrs.put(new Attributes.Name("Bundle-ClassPath"), "lib/abc.jar,lib/xyz.jar,.");
        Mockito.doReturn(attrs).when(manifest).getMainAttributes();

        boolean inBundleClassPath = BundleFileUtil.isInBundleClassPath(manifest, "lib/xxx.jar");
        Assert.assertFalse(inBundleClassPath);
    }
}
