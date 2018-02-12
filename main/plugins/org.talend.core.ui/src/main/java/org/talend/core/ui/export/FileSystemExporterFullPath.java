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

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.repository.documentation.ExportFileResource;

/**
 * bqian Exports resources to file system.
 * 
 * @since 3.1
 */
public class FileSystemExporterFullPath implements IFileExporterFullPath {

    File rootFolder = null;

    private String regEx = "*"; //$NON-NLS-1$

    private List<ExportFileResource> resourcesListToExport;

    /**
     * Create an instance of this class.
     * 
     * @param filename java.lang.String
     * @param compress boolean
     * @exception java.io.IOException
     */
    public FileSystemExporterFullPath(String filename) throws IOException {
        rootFolder = new File(filename);
    }

    public FileSystemExporterFullPath(List<ExportFileResource> resourcesListToExport, String filename) throws IOException {
        rootFolder = new File(filename);
        this.resourcesListToExport = resourcesListToExport;
    }

    /**
     * Do all required cleanup now that we're finished with the currently-open .tar.gz
     * 
     * @exception java.io.IOException
     */
    @Override
    public void finished() throws IOException {
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
        Path path = new Path(destinationPath);
        if (path.segmentCount() == 1) {
            writeFile(resource, new File(rootFolder, path.segment(0)));
        } else {
            File destination = new File(rootFolder, path.toOSString());
            createFolder(destination.getAbsolutePath());
            writeFile(resource, destination);
        }
    }

    /**
     * DOC bqian Comment method "createFolder".
     * 
     * @param path
     */
    private void createFolder(String path) throws IOException {
        FilesUtils.createFoldersIfNotExists(path, true);
    }

    /**
     * Writes the passed file resource to the specified destination on the local file system
     */
    protected void writeFile(String resource, File destinationPath) throws IOException, CoreException {
        FilesUtils.copyFile(new File(resource), destinationPath);
    }

    public static void main(String[] args) {
        try {
            FileSystemExporterFullPath fs = new FileSystemExporterFullPath("c:/qqq"); //$NON-NLS-1$
            fs.write("C:\\RavBin\\DESKTOP.INI", "aaa/bbb/ccc/DESKTOP.INI"); //$NON-NLS-1$ //$NON-NLS-2$
        } catch (Exception e) {
            // e.printStackTrace();
            ExceptionHandler.process(e);
        }
        // this test is ok.
    }

    /**
     * Sets the regEx.
     * 
     * @param regEx the regEx to set
     */
    public void setRegEx(String regEx) {
        this.regEx = regEx;
    }

    /**
     * Export the resources contained in the previously-defined resourcesToExport collection.
     */
    public void exportSpecifiedResources() throws InterruptedException {
        for (ExportFileResource fileResource : resourcesListToExport) {
            String rootName = fileResource.getDirectoryName();

            Set<String> paths = fileResource.getRelativePathList();
            for (Object element : paths) {
                String relativePath = (String) element;
                Set<URL> resource = fileResource.getResourcesByRelativePath(relativePath);
                for (URL url : resource) {
                    String currentResource = url.getPath();
                    exportResource(rootName, relativePath, currentResource, 1);
                }
            }

        }
    }

    public void exportResource(String rootName, String directory, String exportResource, int leadupDepth)
            throws InterruptedException {
        final String separator = "/"; //$NON-NLS-1$
        File file = new File(exportResource);
        if (file.isFile()) {
            String destinationName = file.getName();
            if (!"".equals(directory)) { //$NON-NLS-1$
                if (directory.endsWith(separator)) {
                    destinationName = directory + file.getName();
                } else {
                    destinationName = directory + separator + file.getName();
                }
            }
            if (rootName != null && !"".equals(destinationName)) { //$NON-NLS-1$
                destinationName = rootName + separator + destinationName;
            }
            try {
                this.write(exportResource, destinationName);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                // e.printStackTrace();
                ExceptionHandler.process(e);
            } catch (CoreException e) {
                // TODO Auto-generated catch block
                // e.printStackTrace();
                ExceptionHandler.process(e);
            }

        } else if (file.isDirectory()) {
            File[] children = null;
            try {
                children = file.listFiles(new FileFilter() {

                    @Override
                    public boolean accept(File pathname) {

                        boolean result = true;
                        if (pathname != null && pathname.isFile()) {
                            try {

                                result = Pattern.compile(regEx).matcher(pathname.getName()).find();
                            } catch (PatternSyntaxException e) {
                                // here do nothing
                            }
                        }
                        return result;
                    }
                });
            } catch (Exception e) {
                // this should never happen because an #isAccessible check is done before #members is invoked
            }
            for (File element : children) {
                exportResource(rootName, directory + file.getName() + separator, element.getPath(), leadupDepth + 1);
            }

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.export.IFileExporterFullPath#writeFolder(java.lang.String)
     */
    @Override
    public void writeFolder(String destinationPath) throws IOException, CoreException {
        createFolder(destinationPath);
    }
}
