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
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.junit.Ignore;
import org.junit.Test;
import org.talend.model.migration.TosMetadataMigrationFrom400to410;

/**
 * DOC sgandon class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class Tos_metadata400to410Test {

    /**
     * 
     */
    private static final String MIGRATION_FILE_EXT = ".mig";

    @Test
    public void TestMigrationOnSamplesFolder() throws ATLCoreException, IOException, URISyntaxException {
        TosMetadataMigrationFrom400to410 metadata400to410 = new TosMetadataMigrationFrom400to410();
        // URI samplefolderURI = URL
        //                .create("platform:/plugin/org.talend.model.migration.test/samples/oldPositionalFileConnection.xmi");//$NON-NLS-1$
        URL sampleFolderURL = FileLocator.toFileURL(new URL("platform:/plugin/org.talend.model.migration.test/samples/tos400/")); //$NON-NLS-1$
        URI escapedURI = new URI(sampleFolderURL.getProtocol(), sampleFolderURL.getPath(), sampleFolderURL.getQuery());
        File sampleFolder = URIUtil.toFile(escapedURI);
        TestMigrationOnAllItemsInFolder(sampleFolder, false);
    }

    @Ignore("refering to local path")
    @Test
    public void TestMigrationOnAllItemsInFolder() throws Throwable {
        TestMigrationOnAllItemsInFolder(new File("E:\\temp\\migration\\YO\\metadata"), true); //$NON-NLS-1$
        // TestMigrationOnAllItemsInFolder(new File(
        //                "E:\\temp\\nicolas haumont\\TIS_POOL_CONNEXION_3.0\\TIS_POOL_CONNEXION\\metadata"), false); //$NON-NLS-1$
        //        TestMigrationOnAllItemsInFolder(new File("E:\\temp\\nicolas haumont\\D_EDI_4.0.0\\D_EDI\\trunk\\metadata"), false); //$NON-NLS-1$
    }

    public void TestMigrationOnAllItemsInFolder(File sampleFolder, boolean rename) throws IOException, ATLCoreException {
        TosMetadataMigrationFrom400to410 metadata400to410 = new TosMetadataMigrationFrom400to410();

        ArrayList<File> fileList = new ArrayList<File>();
        getAllFilesFromFolder(sampleFolder, fileList, new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith(".item") || name.endsWith(".xmi");
            }
        });
        System.out.println("-------------- Migrating " + fileList.size() + " files");
        int counter = 0;
        int errorCounter = 0;
        ATLCoreException error = null;
        for (File sample : fileList) {
            System.out.println("-------------- Migrating (" + counter++ + ") : " + sample.getAbsolutePath());
            Resource migratedResource;
            try {
                migratedResource = metadata400to410.migrate(sample.toURI().toString(), new NullProgressMonitor());
                HashMap options = new HashMap(2);
                options.put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$
                options.put(XMLResource.OPTION_XML_VERSION, "1.1"); //$NON-NLS-1$
                //migratedResource.save(System.out, options); //$NON-NLS-1$
                FileOutputStream os = new FileOutputStream(new File(sample.getAbsolutePath() + MIGRATION_FILE_EXT)); //$NON-NLS-1$
                try {
                    migratedResource.save(os, options);
                } finally {
                    os.close();
                }
            } catch (ATLCoreException e) {
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
