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
package org.talend.repository.ui.wizards.metadata.connection.files.salesforce;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.SalesforceSchemaConnection;
import org.talend.core.model.metadata.builder.database.TableInfoParameters;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.metadata.managment.repository.ManagerConnection;
import org.talend.metadata.managment.ui.wizard.CheckLastVersionRepositoryWizard;
import org.talend.repository.ProjectManager;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;

import orgomg.cwm.objectmodel.core.Package;

/**
 * TableWizard present the TableForm width the MetaDataTable. Use to create a new table (need a connection to a DB).
 */

public class SalesforceModulesWizard extends CheckLastVersionRepositoryWizard implements INewWizard {

    private static Logger log = Logger.getLogger(SalesforceModulesWizard.class);

    private SelectorModulesWizardPage selectorWizardPage;

    private SalesforceSchemaConnection temConnection;

    protected String[] existingNames;

    private SalesforceModuleParseAPI salesforceAPI = new SalesforceModuleParseAPI();

    private IMetadataConnection metadataConnection;

    /**
     * DOC ocarbone DatabaseTableWizard constructor comment.
     * 
     * @param workbench
     * @param idNodeDbConnection
     * @param metadataTable
     * @param existingNames
     * @param managerConnection
     */
    public SalesforceModulesWizard(IWorkbench workbench, boolean creation, IRepositoryViewObject object,
            MetadataTable metadataTable, String[] existingNames, boolean forceReadOnly, ManagerConnection managerConnection,
            IMetadataConnection metadataConnection) {
        super(workbench, creation, forceReadOnly);
        this.existingNames = existingNames;
        this.metadataConnection = metadataConnection;
        setNeedsProgressMonitor(true);

        // set the repositoryObject, lock and set isRepositoryObjectEditable
        setRepositoryObject(object);
        isRepositoryObjectEditable();
        initLockStrategy();
        this.connectionItem = (ConnectionItem) object.getProperty().getItem();
        if (connectionItem != null) {
            cloneBaseConnection((SalesforceSchemaConnection) connectionItem.getConnection());
        }
    }

    /**
     * Adding the page to the wizard.
     */
    @Override
    public void addPages() {
        setWindowTitle(Messages.getString("TableWizard.windowTitle")); //$NON-NLS-1$
        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(ECoreImage.METADATA_TABLE_WIZ));
        TableInfoParameters tableInfoParameters = new TableInfoParameters();
        selectorWizardPage = new SelectorModulesWizardPage(connectionItem, isRepositoryObjectEditable(), tableInfoParameters,
                metadataConnection, temConnection, salesforceAPI);

        selectorWizardPage
                .setTitle(Messages.getString("TableWizardPage.titleCreate") + " \"" + connectionItem.getProperty().getLabel() //$NON-NLS-1$ //$NON-NLS-2$
                        + "\""); //$NON-NLS-1$
        selectorWizardPage.setDescription(Messages.getString("TableWizardPage.descriptionCreate")); //$NON-NLS-1$
        selectorWizardPage.setPageComplete(true);
        addPage(selectorWizardPage);
    }

    /**
     * This method determine if the 'Finish' button is enable This method is called when 'Finish' button is pressed in
     * the wizard. We will create an operation and run it using wizard as execution context.
     */
    @Override
    public boolean performFinish() {

        boolean formIsPerformed = false;
        formIsPerformed = true;
        connectionItem.setConnection((SalesforceSchemaConnection) EcoreUtil.copy(temConnection));

        if (formIsPerformed) {
            final IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            final IWorkspaceRunnable op = new IWorkspaceRunnable() {

                public void run(IProgressMonitor monitor) throws CoreException {
                    try {
                        factory.save(connectionItem);
                        closeLockStrategy();
                        ProxyRepositoryFactory.getInstance().saveProject(ProjectManager.getInstance().getCurrentProject());
                    } catch (Exception e) {
                        String detailError = e.toString();
                        log.error(Messages.getString("CommonWizard.persistenceException") + "\n" + detailError); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                }

            };
            IRunnableWithProgress iRunnableWithProgress = new IRunnableWithProgress() {

                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
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
                new ProgressMonitorDialog(getShell()).run(true, false, iRunnableWithProgress);
            } catch (InvocationTargetException e) {
                // already logged
            } catch (InterruptedException e) {
                // nothing to do, since even no cancel here.
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * We will accept the selection in the workbench to see if we can initialize from it.
     * 
     * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
     */
    public void init(final IWorkbench workbench, final IStructuredSelection selection2) {
        this.selection = selection2;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.RepositoryWizard#performCancel()
     */
    @Override
    public boolean performCancel() {
        selectorWizardPage.performCancel();
        temConnection = null;
        return super.performCancel();
    }

    /**
     * clone a new DB connection
     */
    private void cloneBaseConnection(SalesforceSchemaConnection connection) {
        temConnection = (SalesforceSchemaConnection) EcoreUtil.copy(connection);
        EList<Package> dataPackage = connection.getDataPackage();
        Collection<Package> newDataPackage = EcoreUtil.copyAll(dataPackage);
        ConnectionHelper.addPackages(newDataPackage, temConnection);
    }
}
