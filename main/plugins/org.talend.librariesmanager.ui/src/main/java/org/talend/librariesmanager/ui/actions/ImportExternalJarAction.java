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
package org.talend.librariesmanager.ui.actions;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
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
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.ModuleToInstall;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.librariesmanager.model.ModulesNeededProvider;
import org.talend.librariesmanager.model.service.CustomUriManager;
import org.talend.librariesmanager.ui.LibManagerUiPlugin;
import org.talend.librariesmanager.ui.i18n.Messages;

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
                Set<String> modulesNeededNames = ModulesNeededProvider.getAllManagedModuleNames();
                if (fileNames.length > 0) {
                    boolean modified = false;
                    if (fileNames.length > 1) {
                        for (String fileName : fileNames) {
                            File file = new File(path + File.separatorChar + fileName);
                            try {
                                String mvnUri = null;
                                if (module != null && module.getName().equals(fileName)) {
                                    mvnUri = module.getMavenUri();
                                }
                                modified = installFile(modulesNeededNames, file, file.getName(), mvnUri) || modified;
                            } catch (Exception e) {
                                ExceptionHandler.process(e);
                                continue;
                            }
                        }
                    } else {
                        File file = new File(path + File.separatorChar + fileNames[0]);
                        String mvnUri = null;
                        String moduleName = file.getName();
                        if (module != null) {
                            mvnUri = module.getMavenUri();
                            // in case selected file name is not the same as module name
                            moduleName = module.getName();
                            fileNames[0] = moduleName;
                        }
                        try {
                            modified = installFile(modulesNeededNames, file, moduleName, mvnUri) || modified;
                        } catch (Exception e) {
                            ExceptionHandler.process(e);
                        }
                    }
                }
                LibManagerUiPlugin.getDefault().getLibrariesService().checkLibraries();
                // only clean the existed one
                cleanupLib(new HashSet<String>(Arrays.asList(fileNames)));
            }
        });
        return fileNames;
    }

    private boolean installFile(Set<String> modulesNeededNames, File file, String moduleName, String mvnURI)
            throws MalformedURLException, IOException {
        LibManagerUiPlugin.getDefault().getLibrariesService().deployLibrary(file.toURL(), mvnURI, false);
        if (!modulesNeededNames.contains(moduleName)) {
            String mavenUri = MavenUrlHelper.generateMvnUrlForJarName(moduleName, true, true);
            CustomUriManager.getInstance().put(mavenUri, mavenUri);
            ModulesNeededProvider.addUnknownModules(moduleName, mavenUri, true);
            return true;
        }
        return false;
    }

    public static void cleanupLib(Set<String> installedModule) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            IRunProcessService runProcessService = (IRunProcessService) GlobalServiceRegister.getDefault().getService(
                    IRunProcessService.class);
            IFolder libFolder = runProcessService.getJavaProjectLibFolder();
            if (libFolder != null && libFolder.exists()) {
                for (String jarName : installedModule) {
                    IFile jarFile = libFolder.getFile(jarName);
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
