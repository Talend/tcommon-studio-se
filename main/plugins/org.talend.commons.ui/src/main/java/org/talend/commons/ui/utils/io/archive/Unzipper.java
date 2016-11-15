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
package org.talend.commons.ui.utils.io.archive;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.talend.utils.io.FilesUtils;

public class Unzipper extends AbstractUnarchiver {

    private String archiveFilePath;

    private String parentAbsolutePath;

    public Unzipper(String archiveFilePath) throws FileNotFoundException {
        super();
        this.archiveFilePath = archiveFilePath;
        File file = new File(archiveFilePath);
        parentAbsolutePath = file.getParentFile().getAbsolutePath();
    }

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
            FileOutputStream fout = new FileOutputStream(filePath);
            org.talend.commons.runtime.utils.io.StreamCopier.copy(zin, fout);
            zin.closeEntry();
            fout.close();
        }
        zin.close();
    }

    public void unarchive() throws IOException {
        unarchive(null);
    }

}
