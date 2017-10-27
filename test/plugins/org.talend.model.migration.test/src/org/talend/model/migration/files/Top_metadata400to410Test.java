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

package org.talend.model.migration.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.URIUtil;
import org.junit.Ignore;
import org.junit.Test;
import org.talend.model.migration.TopMetadataMigrationFrom400to410usingGenericVM;

/**
 * DOC sgandon class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class Top_metadata400to410Test {

    /**
     * 
     */
    private static final String MIGRATION_FILE_EXT = ".mig";

    @Test
    public void TestMigrationOnSamplesFolder() throws Throwable {
        String[] paths = { "platform:/plugin/org.talend.model.migration.test/samples/top323/",
                "platform:/plugin/org.talend.model.migration.test/samples/top400/" };
        for (String path : paths) {
            runMigrationOnSinglePath(path);
        }
    }

    private void runMigrationOnSinglePath(String path) throws IOException, MalformedURLException, URISyntaxException, Throwable {
        URL sampleFolderUnEscapedURL = FileLocator.toFileURL(new URL(
                path)); //$NON-NLS-1$
        // FileLocator.toFileURL will not escape special chars when creating the URL see bug
        // https://bugs.eclipse.org/bugs/show_bug.cgi?id=145096
        // so we use the URI constructor to create an escaped URI
        URI escapedUri = new URI(sampleFolderUnEscapedURL.getProtocol(), sampleFolderUnEscapedURL.getPath(), null);// calling
                                                                                                                   // the
                                                                                                                   // URI(String)
        // constructor does not escape
        // the URL

        File sampleFolder = URIUtil.toFile(escapedUri);
        TestMigrationOnAllItemsInFolder(sampleFolder, false);
    }

    @Ignore("refering to local path")
    @Test
    public void TestMigrationOnAllItemsInFolder() throws Throwable {
        TestMigrationOnAllItemsInFolder(new File("E:\\temp\\from sebastiao\\metadata TOP400\\AA"), true); //$NON-NLS-1$
    }

    public void TestMigrationOnAllItemsInFolder(File sampleFolder, boolean rename) throws Throwable {
        TopMetadataMigrationFrom400to410usingGenericVM metadata400to410 = new TopMetadataMigrationFrom400to410usingGenericVM();

        ArrayList<File> fileList = new ArrayList<File>();
        getAllFilesFromFolder(sampleFolder, fileList, new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith(".prv") || name.endsWith(".softwaresystem.softwaredeployment");
            }
        });
        System.out.println("-------------- Migrating " + fileList.size() + " files");
        int counter = 0;
        int errorCounter = 0;
        Throwable error = null;
        for (File sample : fileList) {
            System.out.println("-------------- Migrating (" + counter++ + ") : " + sample.getAbsolutePath());
            try {
                metadata400to410.migrate(sample.toURI().toString(), new File(sample.getAbsolutePath() + MIGRATION_FILE_EXT)
                        .toURI().toString(), new NullProgressMonitor());

            } catch (Exception e) {
                error = e;
                errorCounter++;
                // display the file in the console
                System.out.println("!!!!!!!!!!!  Error transforming (" + sample.getAbsolutePath() + ")");
                System.out.println("Error dump :" + e.getMessage());
                System.out.println("file dump :" + e.getMessage());
                BufferedReader fileReader = new BufferedReader(new FileReader(sample));
                while (fileReader.ready()) {
                    System.out.println(fileReader.readLine());
                }
                // } catch (IOException e) {
                // error = e;
            }
            System.out.println("-------------- Migration done of " + counter + " files"
                    + (errorCounter != 0 ? (",  there are " + errorCounter + " files in error.") : "."));
        }
        if (error != null) {// rethrow the last error
            throw error;
        } else {
            if (rename) {
                // remove original files and rename new ones to old ones
                for (File sample : fileList) {
                    boolean isDeleted = sample.delete();
                    System.out.println(sample.getAbsolutePath() + (isDeleted ? " is deleted." : "failed to delete."));
                    boolean isrenamed = new File(sample.getAbsolutePath() + MIGRATION_FILE_EXT).renameTo(sample); //$NON-NLS-1$
                    System.out.println(sample.getAbsolutePath() + ".mig " + (isrenamed ? " is renamed." : "failed to rename."));
                }
            }
        }
    }

    /**
     * DOC sgandon Comment method "getAllFilesFromFolder".
     * 
     * @param sampleFolder
     * @param arrayList
     * @param filenameFilter
     */
    private void getAllFilesFromFolder(File sampleFolder, ArrayList<File> fileList, FilenameFilter filenameFilter) {
        File[] folderFiles = sampleFolder.listFiles(filenameFilter);
        Collections.addAll(fileList, folderFiles);
        File[] allFolders = sampleFolder.listFiles(new FileFilter() {

            public boolean accept(File arg0) {
                return arg0.isDirectory();
            }
        });
        for (File folder : allFolders) {
            getAllFilesFromFolder(folder, fileList, filenameFilter);
        }
    }

}
