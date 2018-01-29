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
package org.talend.commons.utils.io;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;
import org.talend.commons.utils.help.UnitTestBuildCommonHelper;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class FilesUtilsRealTest {

    /**
     * Test method for {@link org.talend.commons.utils.io.FilesUtils#renameFolder(java.io.File, java.io.File)}
     */
    @Test
    public void testRenameFolder() {
        String folderName1 = "test_folder_name"; //$NON-NLS-1$
        String folderName2 = "test_folder_name_rename"; //$NON-NLS-1$
        String fileName = "test_file_name.tdq"; //$NON-NLS-1$

        File folder1 = UnitTestBuildCommonHelper.createRealFolder(null, folderName1, true);
        File file = UnitTestBuildCommonHelper.createRealFile(folder1.getAbsolutePath(), fileName, true);

        File[] listFiles = folder1.listFiles();
        boolean b = false;
        for (File f : listFiles) {
            if (f.getAbsolutePath().equals(file.getAbsolutePath())) {
                b = true;
                break;
            }
        }
        assertTrue(b);

        String folder1Path = folder1.getAbsolutePath();
        int i = folder1Path.lastIndexOf(File.separator);
        File folder2 = new File(folder1Path.substring(0, i) + File.separator + folderName2);

        FilesUtils.renameFolder(folder1, folder2);

        assertFalse(folder1.exists());
        assertTrue(folder2.exists());

        File[] listFiles2 = folder2.listFiles();
        boolean b2 = false;
        for (File f : listFiles2) {
            if (f.getName().equals(file.getName())) {
                b2 = true;
                break;
            }
        }
        assertTrue(b2);

        FilesUtils.deleteFile(folder2, true);
        assertFalse(folder2.exists());
    }
}
