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
package org.talend.commons.utils.resource;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.FrameworkUtil;
import org.talend.utils.files.FileUtils;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class UpdatesHelperTest {

    File workFolder = null;

    @Before
    public void setup() {
        workFolder = FileUtils.createTmpFolder("UpdatesHelperTest", "");
    }

    @After
    public void clean() {
        if (workFolder != null) {
            FilesUtils.deleteFolder(workFolder, true);
        }
    }

    protected File getTestDataFile(String bundlePath) throws IOException {
        URL dataUrl = FileLocator.find(FrameworkUtil.getBundle(this.getClass()), new Path(bundlePath), null);
        if (dataUrl != null) {
            dataUrl = FileLocator.toFileURL(dataUrl);
        }

        File zipFile = new File(dataUrl.getFile());
        if (zipFile.exists()) {
            return zipFile;
        }
        return null;
    }

    @Test
    public void test_isComponentUpdateSite_null() {
        boolean iscomponentUpdateSite = UpdatesHelper.isComponentUpdateSite(null);
        Assert.assertFalse(iscomponentUpdateSite);
    }

    @Test
    public void test_isComponentUpdateSite_nonExisted() {
        boolean isComponentUpdateSite = UpdatesHelper.isComponentUpdateSite(new File("abc"));
        Assert.assertFalse(isComponentUpdateSite);
    }

    @Test
    public void test_isComponentUpdateSite_nonZipFile() throws IOException {
        File testFile = new File(workFolder, "abc.txt");
        testFile.createNewFile();
        boolean isComponentUpdateSite = UpdatesHelper.isComponentUpdateSite(testFile);
        Assert.assertFalse(isComponentUpdateSite);
    }

    @Test
    public void test_isComponentUpdateSite_NormalZipFile() throws IOException {
        File testDataFile = getTestDataFile("resources/update/test.zip");
        Assert.assertTrue(testDataFile.exists());
        boolean isComponentUpdateSite = UpdatesHelper.isComponentUpdateSite(testDataFile);
        Assert.assertFalse(isComponentUpdateSite);
    }

    @Test
    public void test_isComponentUpdateSite_PatchUpdateSiteZipFile() throws IOException {
        File testDataFile = getTestDataFile("resources/update/Patch_20160415_TPS-1357_v1-6.1.1.zip");
        Assert.assertTrue(testDataFile.exists());
        boolean isComponentUpdateSite = UpdatesHelper.isComponentUpdateSite(testDataFile);
        Assert.assertFalse(isComponentUpdateSite);
    }

    @Test
    public void test_isComponentUpdateSite_ComponentZipFile() throws IOException {
        File testDataFile = getTestDataFile("resources/update/components-myjira-0.16.0-SNAPSHOT-updatesite.zip");
        Assert.assertTrue(testDataFile.exists());
        boolean isComponentUpdateSite = UpdatesHelper.isComponentUpdateSite(testDataFile);
        Assert.assertTrue(isComponentUpdateSite);
    }

    @Test
    public void test_isComponentUpdateSite_NormalFolder() throws Exception {
        File testDataFile = getTestDataFile("resources/update/test.zip");
        Assert.assertTrue(testDataFile.exists());

        FilesUtils.unzip(testDataFile.getAbsolutePath(), workFolder.getAbsolutePath());
        boolean isComponentUpdateSite = UpdatesHelper.isComponentUpdateSite(workFolder);
        Assert.assertFalse(isComponentUpdateSite);
    }

    @Test
    public void test_isComponentUpdateSite_PatchUpdateSiteFolder() throws Exception {
        File testDataFile = getTestDataFile("resources/update/Patch_20160415_TPS-1357_v1-6.1.1.zip");
        Assert.assertTrue(testDataFile.exists());

        FilesUtils.unzip(testDataFile.getAbsolutePath(), workFolder.getAbsolutePath());
        boolean isComponentUpdateSite = UpdatesHelper.isComponentUpdateSite(workFolder);
        Assert.assertFalse(isComponentUpdateSite);
    }

    @Test
    public void test_isComponentUpdateSite_ComponentFolder() throws Exception {
        File testDataFile = getTestDataFile("resources/update/components-myjira-0.16.0-SNAPSHOT-updatesite.zip");
        Assert.assertTrue(testDataFile.exists());

        FilesUtils.unzip(testDataFile.getAbsolutePath(), workFolder.getAbsolutePath());
        boolean isComponentUpdateSite = UpdatesHelper.isComponentUpdateSite(workFolder);
        Assert.assertTrue(isComponentUpdateSite);
    }

    @Test
    public void test_isOldComponent_null() {
        Assert.assertFalse(UpdatesHelper.isOldComponent(null));
    }

    @Test
    public void test_isOldComponent_nonExisted() {
        Assert.assertFalse(UpdatesHelper.isOldComponent(new File("abc")));
    }

    @Test
    public void test_isOldComponent_file() throws IOException {
        File testFile = new File(workFolder, "abc.txt");
        testFile.createNewFile();
        Assert.assertFalse(UpdatesHelper.isOldComponent(testFile));
    }

    @Test
    public void test_isOldComponent_empty() throws IOException {
        Assert.assertFalse(UpdatesHelper.isOldComponent(workFolder));
    }

    @Test
    public void test_isOldComponent_withXML() throws IOException {
        File compDefineFolder = new File(workFolder, "tABC");
        compDefineFolder.mkdirs();
        File compDefineFile = new File(compDefineFolder, "tABC_java.xml");
        compDefineFile.createNewFile();
        Assert.assertTrue(UpdatesHelper.isOldComponent(compDefineFolder));
    }
}
