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
package org.talend.core.ui.export;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.eclipse.core.runtime.CoreException;

/**
 * Exports resources to a .zip file
 */
public class ZipFileExporterFullPath implements IFileExporterFullPath {

    private ZipOutputStream outputStream;

    private boolean useCompression = true;

    /**
     * Create an instance of this class.
     * 
     * @param filename java.lang.String
     * @param compress boolean
     * @exception java.io.IOException
     */
    public ZipFileExporterFullPath(String filename, boolean compress) throws IOException {
        outputStream = new ZipOutputStream(new FileOutputStream(filename));
        useCompression = compress;
    }

    /**
     * Do all required cleanup now that we're finished with the currently-open .zip
     * 
     * @exception java.io.IOException
     */
    @Override
    public void finished() throws IOException {
        outputStream.close();
    }

    /**
     * Write the contents of the file to the tar archive.
     * 
     * @param entry
     * @param contents
     * @exception java.io.IOException
     * @exception org.eclipse.core.runtime.CoreException
     */
    private void write(ZipEntry entry, String contents) throws IOException, CoreException {
        byte[] readBuffer = new byte[4096];

        // If the contents are being compressed then we get the below for free.

        if (!useCompression) {
            entry.setMethod(ZipEntry.STORED);
            InputStream contentStream = new FileInputStream(contents);
            int length = 0;
            CRC32 checksumCalculator = new CRC32();
            try {
                int n;
                while ((n = contentStream.read(readBuffer)) > 0) {
                    checksumCalculator.update(readBuffer, 0, n);
                    length += n;
                }
            } finally {
                if (contentStream != null) {
                    contentStream.close();
                }
            }

            entry.setSize(length);
            entry.setCrc(checksumCalculator.getValue());
        }

        outputStream.putNextEntry(entry);
        InputStream contentStream = new FileInputStream(contents);
        try {
            int n;
            while ((n = contentStream.read(readBuffer)) > 0) {
                outputStream.write(readBuffer, 0, n);
            }
        } finally {
            if (contentStream != null) {
                contentStream.close();
            }
        }
        outputStream.closeEntry();
    }

    /**
     * Write the passed resource to the current archive.
     * 
     * @param resource org.eclipse.core.resources.IFile
     * @param destinationPath java.lang.String
     * @exception java.io.IOException
     * @exception org.eclipse.core.runtime.CoreException
     */
    @Override
    public void write(String resource, String destinationPath) throws IOException, CoreException {
        ZipEntry newEntry = new ZipEntry(destinationPath);
        write(newEntry, resource);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.export.IFileExporterFullPath#writeFolder(java.lang.String)
     */
    @Override
    public void writeFolder(String destinationPath) throws IOException, CoreException {
        ZipEntry newEntry = new ZipEntry(destinationPath);
        if (!newEntry.isDirectory()) {
            newEntry = new ZipEntry(destinationPath + "/");
        }
        outputStream.putNextEntry(newEntry);
        outputStream.closeEntry();
    }
}
