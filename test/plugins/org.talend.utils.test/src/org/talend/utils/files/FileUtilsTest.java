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
package org.talend.utils.files;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.talend.utils.sugars.ReturnCode;

public class FileUtilsTest {

    @Rule
    public TemporaryFolder folder1 = new TemporaryFolder();

    /**
     * Test method for {@link org.talend.utils.files.FileUtils#checkBracketsInFile(java.lang.String)}.
     */
    @Test
    @Ignore("Test is looking expecting a file that does not exists in the source.")
    public void testCheckBracketsInFile() {
        String testFile = "test/checkbrackets.txt"; //$NON-NLS-1$
        try {
            List<ReturnCode> ok = FileUtils.checkBracketsInFile(testFile);
            for (ReturnCode returnCode : ok) {
                System.out.println(returnCode.getMessage());
            }
            assertEquals(5, ok.size());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

    }

    /**
     * Test method for
     * {@link org.talend.utils.files.FileUtils#getAllFilesFromFolder(java.io.File, java.util.List, java.io.FilenameFilter)}
     * 
     * @throws IOException
     */
    @Test
    public void getAllFilesFromFolder1() throws IOException {

        List<File> files = new ArrayList<>();
        FilenameFilter filenameFilter1 = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".txt"); //$NON-NLS-1$
            }

        };

        // Should not fail
        FileUtils.getAllFilesFromFolder(null, null, null);

        FileUtils.getAllFilesFromFolder(null, files, null);
        assertEquals(files.size(), 0);

        files = new ArrayList<>();
        FileUtils.getAllFilesFromFolder(folder1.getRoot(), files, filenameFilter1);
        assertEquals(files.size(), 0);

        folder1.newFile("myfile.txt"); //$NON-NLS-1$
        folder1.newFile("myfile2.txt"); //$NON-NLS-1$
        folder1.newFile("myfile2.zip"); //$NON-NLS-1$

        files = new ArrayList<>();
        FileUtils.getAllFilesFromFolder(folder1.getRoot(), files, filenameFilter1);
        assertEquals(files.size(), 2);
    }

    /**
     * Test method for
     * {@link org.talend.utils.files.FileUtils#getAllFilesFromFolder(java.io.File, java.io.FilenameFilter)}
     * 
     * @throws IOException
     */
    @Test
    public void getAllFilesFromFolder2() throws IOException {

        List<File> files;
        FilenameFilter filenameFilter1 = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".txt"); //$NON-NLS-1$
            }

        };
        FilenameFilter filenameFilter2 = null;

        // Should not fail
        files = FileUtils.getAllFilesFromFolder(null, filenameFilter2);

        files = FileUtils.getAllFilesFromFolder(folder1.getRoot(), filenameFilter1);
        assertEquals(files.size(), 0);

        folder1.newFile("myfile.txt"); //$NON-NLS-1$
        folder1.newFile("myfile2.txt"); //$NON-NLS-1$
        folder1.newFile("myfile2.zip"); //$NON-NLS-1$

        files = FileUtils.getAllFilesFromFolder(folder1.getRoot(), filenameFilter1);
        assertEquals(files.size(), 2);
    }

    /**
     * Test method for {@link org.talend.utils.files.FileUtils#getAllFilesFromFolder(java.io.File, java.util.Map)}
     * 
     * @throws IOException
     */
    @Test
    public void getAllFilesFromFolder3() throws IOException {
        List<File> files = null;
        Set<FilterInfo> fileInfos = null;

        files = FileUtils.getAllFilesFromFolder(null, fileInfos);
        assertEquals(files.size(), 0);

        files = FileUtils.getAllFilesFromFolder(folder1.getRoot(), fileInfos);
        assertEquals(files.size(), 0);

        fileInfos = new HashSet<>();
        fileInfos.add(new FilterInfo("my", "txt")); //$NON-NLS-1$ //$NON-NLS-2$
        fileInfos.add(new FilterInfo("your", "jar")); //$NON-NLS-1$ //$NON-NLS-2$
        files = FileUtils.getAllFilesFromFolder(null, fileInfos);
        assertEquals(files.size(), 0);

        List<File> expectedFiles = new ArrayList<>();
        expectedFiles.add(folder1.newFile("myfile.txt")); //$NON-NLS-1$
        expectedFiles.add(folder1.newFile("myfile2.txt")); //$NON-NLS-1$
        folder1.newFile("myfile2.jar"); //$NON-NLS-1$
        expectedFiles.add(folder1.newFile("yourfile.jar")); //$NON-NLS-1$
        folder1.newFile("yourfile.txt"); //$NON-NLS-1$

        files = FileUtils.getAllFilesFromFolder(folder1.getRoot(), fileInfos);
        assertEquals(files.size(), 3);
        assertTrue(files.containsAll(expectedFiles));

    }

    @Test
    public void deleteFilesTest() throws IOException {

        String zipFileName = "myfile2.zip"; //$NON-NLS-1$

        folder1.newFile("myfile.txt"); //$NON-NLS-1$
        folder1.newFile("myfile2.txt"); //$NON-NLS-1$
        folder1.newFile(zipFileName);

        File folder1File = folder1.getRoot();

        FileUtils.deleteFiles(folder1File, name -> false);
        assertEquals(folder1File.listFiles().length, 3);

        FileUtils.deleteFiles(folder1File, name -> name.endsWith(".txt")); //$NON-NLS-1$
        assertEquals(folder1File.listFiles().length, 1);
        assertEquals(folder1File.listFiles()[0].getName(), zipFileName);

        FileUtils.deleteFiles(null, name -> name.endsWith(".txt")); //$NON-NLS-1$
        assertEquals(folder1File.listFiles().length, 1);

        FileUtils.deleteFiles(folder1File, null);
        assertEquals(folder1File.listFiles().length, 1);
    }
}
