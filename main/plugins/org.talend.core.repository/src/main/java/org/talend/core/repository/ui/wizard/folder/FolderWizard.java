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
package org.talend.core.repository.ui.wizard.folder;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.osgi.framework.FrameworkUtil;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.i18n.Messages;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * Wizard for the creation of a new project. <br/>
 * 
 * $Id: FolderWizard.java 83889 2012-05-19 08:18:10Z nrousseau $
 * 
 */
public class FolderWizard extends Wizard {

    /** Main page. */
    private FolderWizardPage mainPage;

    /**
     * Getter for mainPage.
     * 
     * @return the mainPage
     */
    public FolderWizardPage getMainPage() {
        return mainPage;
    }

    private IPath path;

    private ERepositoryObjectType type;

    private final String defaultLabel;

    private String folderName;

    /**
     * Constructs a new NewProjectWizard.
     * 
     * @param author Project author.
     * @param server
     * @param password
     */
    public FolderWizard(IPath path, ERepositoryObjectType type, String defaultLabel) {
        super();
        this.path = path;
        this.type = type;
        this.defaultLabel = defaultLabel;
        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(ECoreImage.FOLDER_WIZ));
    }

    /**
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
    public void addPages() {
        // route/tdm resources are considered as plain folder currently
        boolean isPlainFolder = type == null ? false : type.isAllowPlainFolder();

        mainPage = new FolderWizardPage(defaultLabel, isPlainFolder);
        addPage(mainPage);
        if (defaultLabel != null) {
            setWindowTitle(Messages.getString("RenameFolderAction.action.title")); //$NON-NLS-1$
        } else {
            setWindowTitle(Messages.getString("NewFolderWizard.windowTitle")); //$NON-NLS-1$
        }
    }

    /**
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {

        folderName = mainPage.getName();
        final IProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();

        if (defaultLabel == null) {
            final IWorkspaceRunnable op = new IWorkspaceRunnable() {

                public void run(IProgressMonitor monitor) throws CoreException {
                    try {
                        repositoryFactory.createFolder(type, path, folderName);
                    } catch (PersistenceException e) {
                        throw new CoreException(new Status(IStatus.ERROR, FrameworkUtil.getBundle(this.getClass())
                                .getSymbolicName(), "Error", e));
                    }
                };

            };
            IRunnableWithProgress iRunnableWithProgress = new IRunnableWithProgress() {

                public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    IWorkspace workspace = ResourcesPlugin.getWorkspace();
                    try {
                        ISchedulingRule schedulingRule = workspace.getRoot();
                        // the update the project files need to be done in the workspace runnable to avoid all
                        // notification
                        // of changes before the end of the modifications.
                        workspace.run(op, schedulingRule, IWorkspace.AVOID_UPDATE, monitor);
                    } catch (CoreException e) {
                        throw new InvocationTargetException(e);
                    }
                }
            };

            try {
                new ProgressMonitorDialog(getShell()).run(true, true, iRunnableWithProgress);
                return true;
            } catch (InvocationTargetException e1) {
                Throwable targetException = e1.getTargetException();
                MessageDialog.openError(getShell(), Messages.getString("NewFolderWizard.failureTitle"), Messages //$NON-NLS-1$
                        .getString("NewFolderWizard.failureText")); //$NON-NLS-1$ //$NON-NLS-2$
                ExceptionHandler.process(targetException);
            } catch (InterruptedException e1) {
            }
        } else {
            try {
                repositoryFactory.renameFolder(type, path, folderName);
                return true;
            } catch (PersistenceException e) {
                MessageDialog.openError(getShell(), Messages.getString("NewFolderWizard.failureTitle"), //$NON-NLS-1$
                        e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
                ExceptionHandler.process(e);
            }

        }
        return false;
    }

    public boolean isValid(String folderName) {
        IProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();
        try {
            if (defaultLabel == null) {
                return repositoryFactory.isPathValid(type, path, folderName);
            } else {
                return repositoryFactory.isPathValid(type, path.removeLastSegments(1), folderName);
            }
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
            return false;
        }
    }

    @Override
    public boolean canFinish() {
        return super.canFinish() && !mainPage.getName().equals(defaultLabel);
    }

    public String getFolderNewName() {
        return this.folderName;
    }

}
