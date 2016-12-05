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

import org.eclipse.core.runtime.Platform;
import org.junit.Assert;
import org.junit.Test;
import org.talend.commons.utils.resource.BundleFileUtil;
import org.talend.librariesmanager.prefs.LibrariesManagerUtils;
import org.talend.updates.runtime.engine.P2InstallerTest;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class LocalComponentsInstallComponentTest {

    @Test
    public void test_getInstallingComponentFolder() {
        LocalComponentsInstallComponent installComp = new LocalComponentsInstallComponent();
        final File installingComponentFolder = installComp.getInstallingComponentFolder();
        Assert.assertEquals(new File(Platform.getInstallLocation().getURL().getFile(),
                LocalComponentsInstallComponent.FOLDER_COMPS), installingComponentFolder);
    }

    @Test
    public void test_syncLibraries() throws IOException {
        final File tmpFolder = org.talend.utils.files.FileUtils.createTmpFolder("test", "comp"); //$NON-NLS-1$  //$NON-NLS-2$
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
            FilesUtils.deleteFolder(tmpFolder, true);
            if (oldLibPath != null) {
                System.setProperty(LibrariesManagerUtils.TALEND_LIBRARY_PATH, oldLibPath);
            } else {
                System.clearProperty(LibrariesManagerUtils.TALEND_LIBRARY_PATH);
            }

        }
    }
}
