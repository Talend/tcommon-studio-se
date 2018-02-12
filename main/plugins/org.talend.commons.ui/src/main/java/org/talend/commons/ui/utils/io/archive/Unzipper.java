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
package org.talend.commons.ui.utils.io.archive;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.talend.utils.io.FilesUtils;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 */
public class Unzipper extends AbstractUnarchiver {

    private String archiveFilePath;

    private String parentAbsolutePath;

    /**
     * DOC amaumont Unzipper constructor comment.
     * 
     * @throws FileNotFoundException
     */
    public Unzipper(String archiveFilePath) throws FileNotFoundException {
        super();
        this.archiveFilePath = archiveFilePath;
        File file = new File(archiveFilePath);
        parentAbsolutePath = file.getParentFile().getAbsolutePath();
    }

    /**
     * DOC amaumont Comment method "createInputStream".
     * 
     * @param archiveFilePath
     * @throws FileNotFoundException
     */
    private ZipInputStream createInputStream(String archiveFilePath) throws FileNotFoundException {
        FileInputStream fin = new FileInputStream(archiveFilePath);
        return new ZipInputStream(fin);
    }

    public long countEntries() throws IOException {
        long nbEntries = 0;
        ZipInputStream zin = createInputStream(archiveFilePath);
        while (zin.getNextEntry() != null) {
            nbEntries++;
        }
        zin.close();
        return nbEntries;
    }

    public void unarchive(String targetFolder) throws IOException {
        ZipInputStream zin = createInputStream(archiveFilePath);
        ZipEntry ze = null;
        long i = 0;
        while ((ze = zin.getNextEntry()) != null) {
            setCurrentEntryIndex(i++);
            String filePath = null;
            if (targetFolder != null) {
                filePath = targetFolder + "/" + ze.getName(); //$NON-NLS-1$
            } else {
                filePath = parentAbsolutePath + "/" + ze.getName(); //$NON-NLS-1$
            }
            FilesUtils.createFoldersIfNotExists(filePath, true);
            // System.out.println("Unzipping " + ze.getName());
            FileOutputStream fout = new FileOutputStream(filePath);
            org.talend.commons.runtime.utils.io.StreamCopier.copy(zin, fout);
            zin.closeEntry();
            fout.close();
            // try {
            // System.out.println("slepping");
            // Thread.sleep(100);
            // } catch (InterruptedException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
        }
        zin.close();
    }

    public void unarchive() throws IOException {
        unarchive(null);
    }

    // /**
    // * DOC amaumont Comment method "unarchive".
    // *
    // * @param i
    // * @throws IOException
    // * @throws FileNotFoundException
    // */
    // public static void unarchive(String archiveFilePath) throws IOException {
    // Unzipper unzipper = new Unzipper(archiveFilePath);
    // unzipper.unarchive();
    // // unzipper.close();
    // }

}
