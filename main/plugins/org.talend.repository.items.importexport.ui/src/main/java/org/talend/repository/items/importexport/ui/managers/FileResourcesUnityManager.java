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
package org.talend.repository.items.importexport.ui.managers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Set;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.internal.wizards.datatransfer.ArchiveFileManipulations;
import org.eclipse.ui.internal.wizards.datatransfer.ILeveledImportStructureProvider;
import org.eclipse.ui.internal.wizards.datatransfer.TarException;
import org.eclipse.ui.internal.wizards.datatransfer.TarFile;
import org.eclipse.ui.internal.wizards.datatransfer.TarLeveledStructureProvider;
import org.eclipse.ui.internal.wizards.datatransfer.ZipLeveledStructureProvider;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.runtime.utils.io.FileCopyUtils;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.repository.items.importexport.manager.ResourcesManager;

/**
 * DOC ggu class global comment. Detailled comment
 * 
 * even the file is zip or tar(.gz), will be used FilesManager always. for this requirement, main for DQ product with
 * some special operations in Folder only, non-archives.
 */
public class FileResourcesUnityManager extends FilesManager {

    private static final int BUFFER_SIZE = 1024;

    /**
     * Original file.
     */
    private final File originalFileResouce;

    /**
     * the flag for original file.
     */
    private boolean isArchiveFile = false;

    /**
     * Tmp work folder for FilesManager.
     */
    private File tmpResWorkFolder;

    public FileResourcesUnityManager(File originalResource) {
        super();
        this.originalFileResouce = originalResource;

    }

    public File getOriginalFileResouce() {
        return this.originalFileResouce;
    }

    @SuppressWarnings("nls")
    public File getTmpResWorkFolder() {
        if (this.tmpResWorkFolder == null) {
            try {
                File tmpFile = File.createTempFile("Import", null);
                tmpFile.delete();
                this.tmpResWorkFolder = tmpFile;
            } catch (IOException e) {
                this.tmpResWorkFolder = new File(Platform.getInstallLocation().getURL().getFile(), "tmp" + new Date().getTime());
            }
        }
        if (this.tmpResWorkFolder.exists()) {
            FilesUtils.removeFolder(tmpResWorkFolder, true);
        }
        this.tmpResWorkFolder.mkdirs();
        return this.tmpResWorkFolder;
    }

    public boolean isArchiveFile() {
        return this.isArchiveFile;
    }

    @SuppressWarnings("restriction")
    public ResourcesManager doUnify() throws TarException, ZipException, FileNotFoundException, IOException {
        return doUnify(false);
    }

    @SuppressWarnings("restriction")
    public ResourcesManager doUnify(boolean interruptable) throws TarException, ZipException, FileNotFoundException, IOException {
        final File originalFile = getOriginalFileResouce();
        Assert.isNotNull(originalFile);
        final String absolutePath = originalFile.getAbsolutePath();
        if (!originalFile.exists()) {
            throw new FileNotFoundException(absolutePath);
        }

        final File tmpWorkFolder = getTmpResWorkFolder();

        if (originalFile.isDirectory()) {
            try {
                // copy the resouce files to temp folder.
                FileCopyUtils.copyFolder(originalFile, tmpWorkFolder, interruptable);
            } catch (Throwable e) {
                if (e instanceof InterruptedException && interruptable) {
                    // if interrutable, needn't to process
                } else {
                    ExceptionHandler.process(e);
                }
            }

        } else if (originalFile.isFile()) {
            this.isArchiveFile = true;

            ProviderManager archiveProviderManager = null;
            ILeveledImportStructureProvider importProvider = null;

            if (ArchiveFileManipulations.isTarFile(absolutePath)) {
                // if is not real tar file, will throw exception.
                TarFile tarFile = new TarFile(originalFile);
                importProvider = new TarLeveledStructureProvider(tarFile);
                archiveProviderManager = ResourcesManagerFactory.getInstance().createResourcesManager(importProvider);
            } else if (ArchiveFileManipulations.isZipFile(absolutePath)) {
                // if is not real zip file, will throw exception.
                ZipFile zipFile = new ZipFile(originalFile);
                importProvider = new ZipLeveledStructureProvider(zipFile);
                archiveProviderManager = ResourcesManagerFactory.getInstance().createResourcesManager(importProvider);
            }
            // decompresss
            if (archiveProviderManager != null && importProvider != null) {
                if (!archiveProviderManager.collectPath2Object(importProvider.getRoot())) {
                    throw new IOException("Collect files failure.");
                }
                try {
                    decompress(archiveProviderManager, tmpWorkFolder, interruptable);
                } finally {
                    if (archiveProviderManager != null) {
                        archiveProviderManager.closeResource();
                    }
                }
            }
            this.getEmptyFolders().addAll(archiveProviderManager.getEmptyFolders());
        }
        collectPath2Object(originalFile, tmpWorkFolder, tmpWorkFolder, interruptable);
        return this;
    }

    private void decompress(ResourcesManager srcManager, File destRootFolder, boolean interrupable) throws IOException {
        Set<IPath> paths = srcManager.getPaths();
        Thread currentThread = Thread.currentThread();
        try {
            for (IPath path : paths) {
                if (interrupable && currentThread.isInterrupted()) {
                    throw new InterruptedException();
                }
                InputStream bis = srcManager.getStream(path);
                File destFile = new File(destRootFolder, path.toPortableString());
                File parentFile = destFile.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                BufferedOutputStream bos = null;
                try {
                    bos = new BufferedOutputStream(new FileOutputStream(destFile), BUFFER_SIZE);
                    int count;
                    byte data[] = new byte[BUFFER_SIZE];
                    while ((count = bis.read(data, 0, BUFFER_SIZE)) != -1) {
                        if (interrupable && currentThread.isInterrupted()) {
                            break;
                        }
                        bos.write(data, 0, count);
                    }
                } finally {
                    if (bos != null) {
                        bos.flush();
                        bos.close();
                    }
                    bis.close();
                }
            }
        } catch (Throwable e) {
            if (e instanceof InterruptedException && interrupable) {
                // if interrupable, needn't to process
            } else {
                ExceptionHandler.process(e);
            }
        }
    }
}
