// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.utils.io;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Test;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class FilesUtilsTest {

    /**
     * Test method for {@link org.talend.utils.io.FilesUtils#isSVNFolder(java.io.File)}.
     */
    @Test
    public void testIsSVNFolderFile() {
        File file = new File("META-INF");
        assertTrue(file.exists());
        assertFalse(FilesUtils.isSVNFolder(file));
    }

    /**
     * Test method for {@link org.talend.utils.io.FilesUtils#isEmptyFolder(java.lang.String)}.
     */
    @Test
    public void testIsEmptyFolder() {
        File file = new File("temp");
        file.mkdirs();
        assertTrue(file.exists());
        assertTrue(FilesUtils.isEmptyFolder("temp"));
    }

    /**
     * Test method for
     * {@link org.talend.utils.io.FilesUtils#copyFolder(java.io.File, java.io.File, boolean, java.io.FileFilter, java.io.FileFilter, boolean)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public void testCopyFolder() throws IOException {
        File source = new File("data");
        File target = new File("temp");
        assertTrue(source.exists());
        assertTrue(target.exists());
        FilesUtils.copyFolder(source, target, false, null, null, false);
        assertTrue(new File("temp//testfile").exists());
    }

    private void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (File file2 : files) {
                    this.deleteFile(file2);
                }
            }
            file.delete();
        }
    }

    /**
     * 
     * DOC Administrator Comment method "clearFolder".
     * 
     * @throws IOException
     */
    @After
    public void clearFolder() throws IOException {
        File file = new File("temp");
        if (file.isDirectory()) {
            File files[] = file.listFiles();
            for (File file2 : files) {
                deleteFile(file2);
            }
        }
    }

    /**
     * Test method for {@link org.talend.utils.io.FilesUtils#copyDirectory(java.io.File, java.io.File)}.
     */
    @Test
    public void testCopyDirectory() {
        File source = new File("data");
        File target = new File("temp");
        assertTrue(source.isDirectory() && target.isDirectory());
        FilesUtils.copyDirectory(source, target);
        assertTrue(new File("temp//data").exists());
    }

    /**
     * Test method for {@link org.talend.utils.io.FilesUtils#createFolder(java.io.File)}.
     */
    @Test
    public void testCreateFolderFile() {
        File file = new File("temp//testfolder");
        assertFalse(file.exists());
        FilesUtils.createFolder(file);
        assertTrue(file.exists());
    }

    /**
     * Test method for {@link org.talend.utils.io.FilesUtils#createFoldersIfNotExists(java.lang.String)}.
     * 
     * @throws IOException
     */
    @Test
    public void testCreateFoldersIfNotExistsString() throws IOException {
        File file = new File("temp//testfolder1/testfolder2");
        assertFalse(file.exists());
        FilesUtils.createFoldersIfNotExists(file.getAbsolutePath());
        assertTrue(file.exists());
    }

    /**
     * Test method for {@link org.talend.utils.io.FilesUtils#createFoldersIfNotExists(java.lang.String)}.
     * 
     * @throws IOException
     */
    @Test
    public void testCreateFoldersIfNotExistsStringBooleanIsFilePath() {
        String baseDirectory = "temp//testfolder1/testfolder2";
        File directory = new File(baseDirectory);
        assertFalse(directory.exists());
        File filePath = new File(baseDirectory + "/myfile");
        boolean pathIsFilePath = true;
        FilesUtils.createFoldersIfNotExists(filePath.getAbsolutePath(), pathIsFilePath);
        assertTrue(directory.exists());
        assertFalse(filePath.exists());
    }

    /**
     * Test method for {@link org.talend.utils.io.FilesUtils#createFoldersIfNotExists(java.lang.String)}.
     * 
     * @throws IOException
     */
    @Test
    public void testCreateFoldersIfNotExistsStringBooleanIsNotFilePath() {
        String baseDirectory = "temp//testfolder1/testfolder2";
        File directory = new File(baseDirectory);
        assertFalse(directory.exists());
        boolean pathIsFilePath = false;
        FilesUtils.createFoldersIfNotExists(directory.getAbsolutePath(), pathIsFilePath);
        assertTrue(directory.exists());
    }

    /**
     * Test method for {@link org.talend.utils.io.FilesUtils#deleteFile(java.io.File, boolean)}.
     * 
     * @throws IOException
     */
    @Test
    public void testDeleteFile() throws IOException {
        File file = new File("temp//testFiles");
        assertFalse(file.exists());
        file.createNewFile();
        assertTrue(file.exists());
        FilesUtils.deleteFile(file, true);
        assertFalse(file.exists());
    }

    @Test
    public void testDeleteFolder() throws IOException {
        File tmpFolder = new File("temp");
        File testFolder = new File(tmpFolder, "test1111111111111");
        FilesUtils.deleteFolder(testFolder, true);
        assertFalse(testFolder.exists());

        testFolder.mkdirs();
        assertTrue(testFolder.exists());

        FilesUtils.deleteFolder(testFolder, false);
        assertTrue(testFolder.exists()); // still existed

        FilesUtils.deleteFolder(testFolder, true);
        assertFalse(testFolder.exists()); // has been deleted

        File folder = new File(testFolder, "folder1");
        File abcFile = new File(folder, "abc.txt");
        File subFolderFile = new File(folder, "subfolder1");
        File subFile = new File(subFolderFile, "sub.txt");
        subFolderFile.mkdirs();
        abcFile.createNewFile();
        subFile.createNewFile();
        assertTrue(folder.exists());
        assertTrue(abcFile.exists());
        assertTrue(subFolderFile.exists());
        assertTrue(subFile.exists());

        FilesUtils.deleteFolder(testFolder, false);
        assertTrue(testFolder.exists()); // still existed
        assertFalse(folder.exists()); // has been deleted
        assertFalse(abcFile.exists()); // has been deleted
        assertFalse(subFolderFile.exists()); // has been deleted
        assertFalse(subFile.exists()); // has been deleted

        // create again
        subFolderFile.mkdirs();
        abcFile.createNewFile();
        subFile.createNewFile();
        FilesUtils.deleteFolder(testFolder, true);
        assertFalse(testFolder.exists()); // has been deleted
        assertFalse(folder.exists()); // has been deleted
        assertFalse(abcFile.exists()); // has been deleted
        assertFalse(subFolderFile.exists()); // has been deleted
        assertFalse(subFile.exists()); // has been deleted

    }

    @Test
    public void testAllInSameFolder() throws IOException {
        assertFalse(FilesUtils.allInSameFolder(null));
        assertFalse(FilesUtils.allInSameFolder(new File("abc")));

        File tmpFolder = new File("temp");
        assertTrue(FilesUtils.allInSameFolder(tmpFolder));

        File abcFile = new File(tmpFolder, "abc.txt");
        assertFalse(FilesUtils.allInSameFolder(tmpFolder, "abc.txt"));
        assertFalse(FilesUtils.allInSameFolder(abcFile, "abc.txt"));

        if (!abcFile.exists()) {
            abcFile.createNewFile();
        }
        assertTrue(FilesUtils.allInSameFolder(tmpFolder, "abc.txt"));
        assertTrue(FilesUtils.allInSameFolder(abcFile, "abc.txt"));

        File xyzFile = new File(tmpFolder, "xyz.txt");
        if (!xyzFile.exists()) {
            xyzFile.createNewFile();
        }
        assertTrue(FilesUtils.allInSameFolder(tmpFolder, "abc.txt", "xyz.txt"));
        assertTrue(FilesUtils.allInSameFolder(abcFile, "abc.txt", "xyz.txt"));
        assertTrue(FilesUtils.allInSameFolder(xyzFile, "abc.txt", "xyz.txt"));

        assertFalse(FilesUtils.allInSameFolder(tmpFolder, "abc.txt", "xyz.txt", "XXXX123.txt"));
        assertFalse(FilesUtils.allInSameFolder(abcFile, "abc.txt", "xyz.txt", "XXXX123.txt"));
        assertFalse(FilesUtils.allInSameFolder(xyzFile, "abc.txt", "xyz.txt", "XXXX123.txt"));
    }
}
