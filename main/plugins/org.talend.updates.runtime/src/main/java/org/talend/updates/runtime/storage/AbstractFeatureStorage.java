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
package org.talend.updates.runtime.storage;

import java.io.File;

import org.eclipse.core.runtime.IProgressMonitor;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public abstract class AbstractFeatureStorage implements IFeatureStorage {

    public static final String TEMP_FOLDER_PREFIX = "TalendFeatures_"; //$NON-NLS-1$

    private File featDownloadFolder;

    final private Object featDownloadFolderLock = new Object();

    private File featureFile;

    final private Object featureFileLock = new Object();

    private File imageDownloadFolder;

    private File imageFile;

    final private Object imageFileLock = new Object();

    private boolean autoCleanFiles = false;

    public File getFeatDownloadFolder() throws Exception {
        if (this.featDownloadFolder != null) {
            return this.featDownloadFolder;
        }
        synchronized (featDownloadFolderLock) {
            if (this.featDownloadFolder == null) {
                this.featDownloadFolder = File.createTempFile(TEMP_FOLDER_PREFIX, ""); //$NON-NLS-1$
                if (this.featDownloadFolder.exists()) {
                    this.featDownloadFolder.delete();
                    this.featDownloadFolder.mkdir();
                }
            }
        }
        return this.featDownloadFolder;
    }

    public void setFeatDownloadFolder(File featDownloadFolder) {
        synchronized (featDownloadFolderLock) {
            this.featDownloadFolder = featDownloadFolder;
        }
    }

    public File getImageDownloadFolder() throws Exception {
        if (this.imageDownloadFolder == null) {
            return File.createTempFile(TEMP_FOLDER_PREFIX, ""); //$NON-NLS-1$
        }
        return this.imageDownloadFolder;
    }

    public void setImageDownloadFolder(File imageDownloadFolder) {
        this.imageDownloadFolder = imageDownloadFolder;
    }

    @Override
    public File getFeatureFile(IProgressMonitor monitor) throws Exception {
        if (featureFile != null) {
            return featureFile;
        }
        synchronized (featureFileLock) {
            if (featureFile == null) {
                featureFile = downloadFeatureFile(monitor);
                if (isAutoCleanFiles() && featureFile != null) {
                    featureFile.deleteOnExit();
                }
            }
        }
        return featureFile;
    }

    @Override
    public File getImageFile(IProgressMonitor monitor) throws Exception {
        if (imageFile != null) {
            return imageFile;
        }
        synchronized (imageFileLock) {
            if (imageFile == null) {
                imageFile = downloadImageFile(monitor);
                if (isAutoCleanFiles() && imageFile != null) {
                    imageFile.deleteOnExit();
                }
            }
        }
        return imageFile;
    }

    public void close(IProgressMonitor monitor) throws Exception {
        if (isAutoCleanFiles()) {
            // nothing to do
        }
    }

    public boolean isAutoCleanFiles() {
        return this.autoCleanFiles;
    }

    public void setAutoCleanFiles(boolean autoCleanFiles) {
        this.autoCleanFiles = autoCleanFiles;
    }

    abstract protected File downloadFeatureFile(IProgressMonitor monitor) throws Exception;

    abstract protected File downloadImageFile(IProgressMonitor monitor) throws Exception;
}
