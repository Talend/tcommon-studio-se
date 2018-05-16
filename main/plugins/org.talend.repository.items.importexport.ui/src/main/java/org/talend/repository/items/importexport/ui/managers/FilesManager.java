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
package org.talend.repository.items.importexport.ui.managers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.wizards.datatransfer.IImportStructureProvider;
import org.talend.core.repository.constants.FileConstants;
import org.talend.repository.items.importexport.handlers.model.ImportItem;
import org.talend.utils.io.FilesUtils;

/**
 */
public class FilesManager extends AbstractImportResourcesManager {

    @Override
    public InputStream getStream(IPath path, ImportItem importItem) throws IOException {
        return new BufferedInputStream(new FileInputStream((File) path2Object.get(path)));
    }

    @Override
    public boolean collectPath2Object(Object root) {
        return collectPath2Object(root, false);
    }

    public boolean collectPath2Object(Object root, boolean interruptable) {
        if (interruptable && Thread.currentThread().isInterrupted()) {
            return false;
        }
        return doCollectItemFiles((File) root, (File) root, (File) root, interruptable);
    }

    public boolean collectPath2Object(File original, File root, File current, boolean interruptable) {
        if (interruptable && Thread.currentThread().isInterrupted()) {
            return false;
        }
        return doCollectItemFiles(original, root, current, interruptable);
    }

    private boolean doCollectItemFiles(File original, File root, File directory, boolean interruptable) {
        File[] contents = directory.listFiles();
        Thread currentThread = Thread.currentThread();

        if (contents != null) {
            for (File content : contents) {
                if (interruptable && currentThread.isInterrupted()) {
                    return false;
                }
                File file = content;

                if (file.isFile()) {
                    add(file.getAbsolutePath(), file);
                }
                if (file.isDirectory()) {
                    if ((!FilesUtils.isSVNFolder(file))) {
                        collectPath2Object(original, root, content, interruptable);
                    }
                    IPath folderPath = getFolderPath(original, root, file);
                    if (folderPath != null) {
                        addFolder(folderPath.toPortableString());
                    }
                }
            }
        }
        return true;
    }

    private IPath getFolderPath(File original, File tempDirectoryRoot, File directory) {
        File projectFile = findProjectFile(directory);
        if (projectFile != null) {
            String projectName = "";
            if (tempDirectoryRoot.getAbsolutePath().equals(projectFile.getParentFile().getAbsolutePath())) {
                projectName = original.getName();
            }
            IPath currentPath = new Path(directory.getAbsolutePath());
            IPath basePath = new Path(tempDirectoryRoot.getAbsolutePath());
            IPath absPath = currentPath.makeRelativeTo(basePath);

            IPath folderPath = new Path(projectName);
            folderPath = folderPath.append(absPath);
            return folderPath;
        }
        return null;
    }

    private File findProjectFile(File directory) {
        File parentFile = directory.getParentFile();
        File[] listFiles = parentFile.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return FileConstants.LOCAL_PROJECT_FILENAME.equals(pathname.getName());
            }
        });
        if (listFiles.length == 1) {
            return listFiles[0];
        } else if (parentFile.getParentFile() != null) {
            return findProjectFile(parentFile);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.localprovider.imports.ResourcesManager#getProvider()
     */
    @Override
    public IImportStructureProvider getProvider() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.localprovider.imports.ResourcesManager#closeResource()
     */
    @Override
    public void closeResource() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.items.importexport.manager.ResourcesManager#getRoot()
     */
    @Override
    public Object getRoot() {
        return null;
    }
}
