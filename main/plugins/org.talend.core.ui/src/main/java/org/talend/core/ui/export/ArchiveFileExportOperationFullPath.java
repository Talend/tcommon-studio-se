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
package org.talend.core.ui.export;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.operation.ModalContext;
import org.eclipse.osgi.util.NLS;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.exception.MessageBoxExceptionHandler;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.prefs.IDEWorkbenchPlugin;
import org.talend.core.ui.i18n.Messages;
import org.talend.repository.documentation.ExportFileResource;

/**
 * Operation for exporting a resource and its children to a new .zip or .tar.gz file.
 * 
 * @since 3.1
 */
public class ArchiveFileExportOperationFullPath implements IRunnableWithProgress {

    private static final String SEPARATOR = "/"; //$NON-NLS-1$

    private IFileExporterFullPath exporter;

    private String destinationFilename;

    private IProgressMonitor monitor;

    private List errorTable = new ArrayList(1); // IStatus

    private boolean useCompression = true;

    private boolean useTarFormat = false;

    private boolean createLeadupStructure = true;

    private String regEx = "*"; //$NON-NLS-1$

    private List<ExportFileResource> resourcesListToExport;

    /**
     * Sets the regEx.
     * 
     * @param regEx the regEx to set
     */
    public void setRegEx(String regEx) {
        this.regEx = regEx;
    }

    /**
     * Getter for regEx.
     * 
     * @return the regEx
     */
    public String getRegEx() {
        return this.regEx;
    }

    /**
     * Create an instance of this class. Use this constructor if you wish to recursively export a single resource.
     * 
     * @param res org.eclipse.core.resources.IResource;
     * @param filename java.lang.String
     */
    private ArchiveFileExportOperationFullPath(String filename) {
        super();
        destinationFilename = filename;
    }

    /**
     * Create an instance of this class. Use this constructor if you wish to export specific resources with a common
     * parent resource (affects container directory creation)
     * 
     * @param res org.eclipse.core.resources.IResource
     * @param resources java.util.Vector
     * @param filename java.lang.String
     */

    public ArchiveFileExportOperationFullPath(List<ExportFileResource> resourcesListToExport, String destinationValue) {
        this(destinationValue);
        this.resourcesListToExport = resourcesListToExport;
    }

    /**
     * Add a new entry to the error table with the passed information.
     */
    protected void addError(String message, Throwable e) {
        ExceptionHandler.process(e);
        errorTable.add(new Status(IStatus.ERROR, IDEWorkbenchPlugin.IDE_WORKBENCH, 0, message, e));
    }

    /**
     * Answer a boolean indicating the number of file resources that were specified for export.
     * 
     * @return int
     */
    protected int countSelectedResources() throws CoreException {
        int result = 0;
        for (ExportFileResource fileList : resourcesListToExport) {
            result += fileList.getFilesCount();
        }
        return result;
    }

    /**
     * Export the passed resource to the destination .zip.
     * 
     * @param exportResource org.eclipse.core.resources.IResource
     * @param leadupDepth the number of resource levels to be included in the path including the resourse itself.
     */
    public void exportResource(String rootName, String directory, String exportResource, int leadupDepth)
            throws InterruptedException {

        File file = new File(exportResource);
        if (file.isFile()) {

            String destinationName = file.getName();
            if (!"".equals(directory)) { //$NON-NLS-1$
                if (directory.endsWith(SEPARATOR)) {
                    destinationName = directory + file.getName();
                } else {
                    destinationName = directory + SEPARATOR + file.getName();
                }
            }

            if (createLeadupStructure) {
                if (rootName != null && !"".equals(destinationName)) { //$NON-NLS-1$
                    if (file.getName().equals("spagic.properties")) { //$NON-NLS-1$
                        destinationName = rootName.substring(0, rootName.indexOf("/")) + SEPARATOR + destinationName; //$NON-NLS-1$
                    } else if (!"".equals(rootName) && !rootName.equals(SEPARATOR)) { //$NON-NLS-1$
                        if (rootName.endsWith(SEPARATOR)) {
                            destinationName = rootName + destinationName;
                        } else {
                            destinationName = rootName + SEPARATOR + destinationName;
                        }
                    }
                }
            }

            destinationName = destinationName.replace("//", SEPARATOR); //$NON-NLS-1$

            monitor.subTask(destinationName);

            try {
                exporter.write(exportResource, destinationName);
            } catch (IOException e) {
                addError(NLS.bind("", exportResource, e.getMessage()), e); //$NON-NLS-1$
            } catch (CoreException e) {
                addError(NLS.bind("", exportResource, e.getMessage()), e); //$NON-NLS-1$
            }

            monitor.worked(1);
            ModalContext.checkCanceled(monitor);
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
                addError(NLS.bind("", exportResource), e); //$NON-NLS-1$
            }

            for (File element : children) {
                exportResource(rootName, directory + file.getName() + SEPARATOR, element.getPath(), leadupDepth + 1);
            }

        }
    }

    /**
     * Export the resources contained in the previously-defined resourcesToExport collection.
     */
    protected void exportSpecifiedResources() throws InterruptedException {
        for (ExportFileResource fileResource : resourcesListToExport) {
            String rootName = fileResource.getDirectoryName();

            Set<String> paths = fileResource.getRelativePathList();
            for (Object element : paths) {
                String relativePath = (String) element;
                Set<URL> resource = fileResource.getResourcesByRelativePath(relativePath);
                for (URL url : resource) {
                    String currentResource = FilesUtils.getFileRealPath(url.getPath());
                    exportResource(rootName, relativePath, currentResource, 1);
                }
            }

        }
    }

    /**
     * Answer the error table.
     * 
     * @return Vector of IStatus
     */
    public List getResult() {
        return errorTable;
    }

    /**
     * Returns the status of the operation. If there were any errors, the result is a status object containing
     * individual status objects for each error. If there were no errors, the result is a status object with error code
     * <code>OK</code>.
     * 
     * @return the status
     */
    public IStatus getStatus() {
        IStatus[] errors = new IStatus[errorTable.size()];
        errorTable.toArray(errors);
        return new MultiStatus(IDEWorkbenchPlugin.IDE_WORKBENCH, IStatus.OK, errors, "", null); //$NON-NLS-1$
    }

    /**
     * Initialize this operation.
     * 
     * @exception java.io.IOException
     */
    protected void initialize() throws IOException {
        checkDestinationParentFolder();
        if (useTarFormat) {
            exporter = new TarFileExporterFullPath(destinationFilename, useCompression);
        } else {
            exporter = new ZipFileExporterFullPath(destinationFilename, useCompression);
        }
    }

    /**
     * 
     * DOC ggu Comment method "checkDestinationParentFolder".
     * 
     * Check and create the parent folder.
     * 
     * @throws IOException
     */
    private void checkDestinationParentFolder() throws IOException {
        File destFile = new File(destinationFilename);
        File parentFile = destFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        if (!parentFile.exists()) {
            throw new IOException(Messages.getString("ArchiveFileExportOperationFullPath.cannotCreateDir", parentFile)); //$NON-NLS-1$
        }
    }

    /**
     * Answer a boolean indicating whether the passed child is a descendent of one or more members of the passed.
     * resources collection
     * 
     * @return boolean
     * @param resources java.util.Vector
     * @param child org.eclipse.core.resources.IResource
     */
    protected boolean isDescendent(List resources, IResource child) {
        if (child.getType() == IResource.PROJECT) {
            return false;
        }

        IResource parent = child.getParent();
        if (resources.contains(parent)) {
            return true;
        }

        return isDescendent(resources, parent);
    }

    /**
     * Export the resources that were previously specified for export (or if a single resource was specified then
     * export. it recursively)
     */
    @Override
    public void run(IProgressMonitor progressMonitor) throws InvocationTargetException, InterruptedException {
        this.monitor = progressMonitor;

        try {
            initialize();
        } catch (IOException e) {
            MessageBoxExceptionHandler.process(e);
            throw new InvocationTargetException(e, NLS.bind("", e.getMessage())); //$NON-NLS-1$
        }

        try {
            // ie.- a single resource for recursive export was specified
            int totalWork = IProgressMonitor.UNKNOWN;
            try {
                if (resourcesListToExport == null) {
                    // FIXME here nerver happen
                    // totalWork = countChildrenOf(resource);
                } else {
                    totalWork = countSelectedResources();
                }
            } catch (CoreException e) {
                // Should not happen
            }
            monitor.beginTask("", totalWork); //$NON-NLS-1$
            if (resourcesListToExport == null) {
                // FIXME here nerver happen
                // exportResource(resource);
            } else {
                // ie.- a list of specific resources to export was specified
                exportSpecifiedResources();
            }

            try {
                exporter.finished();
            } catch (IOException e) {
                throw new InvocationTargetException(e, NLS.bind("", e //$NON-NLS-1$
                        .getMessage()));
            }
        } finally {
            monitor.done();
        }
    }

    /**
     * Set this boolean indicating whether each exported resource's path should include containment hierarchies as.
     * dictated by its parents
     * 
     * @param value boolean
     */
    public void setCreateLeadupStructure(boolean value) {
        createLeadupStructure = value;
    }

    /**
     * Set this boolean indicating whether exported resources should be compressed (as opposed to simply being stored).
     * 
     * @param value boolean
     */
    public void setUseCompression(boolean value) {
        useCompression = value;
    }

    /**
     * Set this boolean indicating whether the file should be output in tar.gz format rather than .zip format.
     * 
     * @param value boolean
     */
    public void setUseTarFormat(boolean value) {
        useTarFormat = value;
    }

    public String getDestinationFilename() {
        return destinationFilename;
    }
}
