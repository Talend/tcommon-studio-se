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
package org.talend.librariesmanager.ui.actions;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.ModuleToInstall;
import org.talend.core.model.general.Project;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.librariesmanager.ui.LibManagerUiPlugin;
import org.talend.librariesmanager.ui.i18n.Messages;
import org.talend.repository.ProjectManager;

/**
 * Imports the external jar files into talend.
 * 
 * $Id: ImportExternalJarAction.java Mar 15, 20075:58:30 PM bqian $
 * 
 */
public class ImportExternalJarAction extends Action {

    /**
     * DOC acer ImportExternalJarAction constructor comment.
     */
    public ImportExternalJarAction() {
        super();
        this.setText(Messages.getString("ImportExternalJarAction.title")); //$NON-NLS-1$
        this.setDescription(Messages.getString("ImportExternalJarAction.title")); //$NON-NLS-1$
        this.setImageDescriptor(ImageProvider.getImageDesc(ECoreImage.IMPORT_JAR));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {

        handleImportJarDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
    }

    /**
     * DOC sgandon Comment method "handleImportJarDialog".
     * 
     * @param shell, to display the dialog box
     * @return, list of imported file names, may be empty
     */
    public String[] handleImportJarDialog(Shell shell) {
        return handleImportJarDialog(shell, null);
    }

    public String[] handleImportJarDialog(Shell shell, ModuleToInstall module) {
        FileDialog fileDialog = new FileDialog(shell, SWT.OPEN | SWT.MULTI);
        fileDialog.setFilterExtensions(FilesUtils.getAcceptJARFilesSuffix());
        fileDialog.open();
        final String path = fileDialog.getFilterPath();
        final String[] fileNames = fileDialog.getFileNames();

        BusyIndicator.showWhile(Display.getDefault(), new Runnable() {

            @Override
            public void run() {
                String moduleName = null;
                String mvnUri = null;
                if (module != null) {
                    moduleName = module.getName();
                    mvnUri = module.getMavenUri();
                }
                for (int i = 0; i < fileNames.length; i++) {
                    String fileName = fileNames[i];
                    File file = new File(path + File.separatorChar + fileName);
                    File tempFile = null;
                    try {
                        if (moduleName != null && fileNames.length == 1 && !file.isDirectory()
                                && !file.getName().equals(moduleName)) {
                            Project project = ProjectManager.getInstance().getCurrentProject();
                            IProject fsProject = ResourceUtils.getProject(project);
                            IFolder tmpFolder = fsProject.getFolder("temp");
                            if (!tmpFolder.exists()) {
                                tmpFolder.create(true, true, null);
                            }
                            tempFile = new File(tmpFolder.getLocation().toPortableString(), moduleName);
                            FilesUtils.copyFile(file, tempFile);
                            file = tempFile;
                            fileNames[i] = file.getName();
                        }

                        LibManagerUiPlugin.getDefault().getLibrariesService().deployLibrary(file.toURL(), mvnUri);
                        if (tempFile != null) {
                            FilesUtils.deleteFile(tempFile, true);
                        }
                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                        continue;
                    }
                }
                // only clean the existed one
                cleanupLib(new HashSet<String>(Arrays.asList(fileNames)));
            }
        });
        return fileNames;
    }

    // private void emptyLibs() {
    // if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibrariesService.class)) {
    // ILibrariesService libService = (ILibrariesService) GlobalServiceRegister.getDefault().getService(
    // ILibrariesService.class);
    // if (libService != null) {
    // libService.cleanLibs();
    // }
    // }
    // }

    public static void cleanupLib(Set<String> installedModule) {
        for (String jarName : installedModule) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
                IRunProcessService runProcessService = (IRunProcessService) GlobalServiceRegister.getDefault().getService(
                        IRunProcessService.class);
                ITalendProcessJavaProject talendProcessJavaProject = runProcessService.getTalendProcessJavaProject();
                if (talendProcessJavaProject != null) {
                    IFile jarFile = talendProcessJavaProject.getLibFolder().getFile(jarName);
                    if (jarFile.exists()) {
                        try {
                            jarFile.delete(true, null);
                        } catch (CoreException e) {
                            //
                        }
                    }
                }
            }
        }
    }
}
