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
package org.talend.repository.ui.wizards.metadata.table.files;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ITDQRepositoryService;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.update.RepositoryUpdateManager;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.metadata.managment.ui.wizard.AbstractRepositoryFileTableWizard;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * DOC ocarbone class global comment.
 * 
 * $Id: FileDelimitedTableWizard.java 51998 2010-12-01 05:33:08Z hywang $
 * 
 */
public class FileDelimitedTableWizard extends AbstractRepositoryFileTableWizard implements INewWizard {

    private static Logger log = Logger.getLogger(FileDelimitedTableWizard.class);

    private FileTableWizardPage tableWizardpage;

    private Map<String, String> oldTableMap;

    private IMetadataTable oldMetadataTable;

    // Added TDQ-8360 20140327 yyin: if cancel, should restore the old columns
    private List<MetadataColumn> oldColumns = new ArrayList<MetadataColumn>();

    private Observable updateSchemaObservable;

    private Observer schemaObserver;

    /**
     * Constructor for TableWizard.
     * 
     * @param ISelection
     */
    public FileDelimitedTableWizard(IWorkbench workbench, boolean creation, ConnectionItem connectionItem,
            MetadataTable metadataTable, boolean forceReadOnly) {
        super(workbench, creation, forceReadOnly);
        this.connectionItem = connectionItem;
        this.metadataTable = metadataTable;
        if (connectionItem != null) {
            oldTableMap = RepositoryUpdateManager.getOldTableIdAndNameMap(connectionItem, metadataTable, creation);
            oldMetadataTable = ConvertionHelper.convert(metadataTable);
            oldColumns.addAll(metadataTable.getColumns());
        }
        setNeedsProgressMonitor(true);

        isRepositoryObjectEditable();
        initLockStrategy();
    }

    /**
     * Adding the page to the wizard.
     */

    @Override
    public void addPages() {
        setWindowTitle(Messages.getString("SchemaWizard.windowTitle")); //$NON-NLS-1$

        tableWizardpage = new FileTableWizardPage(connectionItem, metadataTable, isRepositoryObjectEditable());

        if (creation) {
            tableWizardpage.setTitle(Messages.getString(
                    "FileTableWizardPage.titleCreate", connectionItem.getProperty().getLabel())); //$NON-NLS-1$
            tableWizardpage.setDescription(Messages.getString("FileTableWizardPage.descriptionCreate")); //$NON-NLS-1$
            tableWizardpage.setPageComplete(false);
        } else {
            tableWizardpage.setTitle(Messages.getString("FileTableWizardPage.titleUpdate", metadataTable.getLabel())); //$NON-NLS-1$
            tableWizardpage.setDescription(Messages.getString("FileTableWizardPage.descriptionUpdate")); //$NON-NLS-1$
            tableWizardpage.setPageComplete(isRepositoryObjectEditable());

            // Added TDQ-8360 20140410 yyin: to observe if the schema is changed or not
            updateSchemaObservable = new SchemaObservable();
            schemaObserver = new SchemaObserver();
            updateSchemaObservable.addObserver(schemaObserver);
            tableWizardpage.addSchemaObservable(updateSchemaObservable);
        }
        addPage(tableWizardpage);
    }

    private class SchemaObservable extends Observable {

        @Override
        public void notifyObservers() {
            setChanged();
            super.notifyObservers();
        }

    }

    private class SchemaObserver implements Observer {

        private boolean isUpdated = false;

        /*
         * from the Observable's nodify, will call this update.
         */
        @Override
        public void update(Observable source, Object arg1) {
            isUpdated = true;
        }

        public boolean isUpdated() {
            return this.isUpdated;
        }
    }

    /**
     * This method determine if the 'Finish' button is enable This method is called when 'Finish' button is pressed in
     * the wizard. We will create an operation and run it using wizard as execution context.
     */
    @Override
    public boolean performFinish() {
        if (tableWizardpage.isPageComplete()) {
            // MOD qiongli 2011-11-23 TDQ-3930,TDQ-3797.pop a question dialog when there are anlaysises in TDQ need to
            // update.if user click cancel,will return and stop the retive action.
            ITDQRepositoryService tdqRepositoryService = null;
            // only when updating the existing metadata need to check related DQ, no need for creating a new metadata
            if (!creation && GlobalServiceRegister.getDefault().isServiceRegistered(ITDQRepositoryService.class)) {
                tdqRepositoryService = (ITDQRepositoryService) org.talend.core.GlobalServiceRegister.getDefault().getService(
                        ITDQRepositoryService.class);

                // ADD TDQ-8360, yyin , use the EMF compare to keep the existing elements.
                if (((SchemaObserver) schemaObserver).isUpdated()
                        && tdqRepositoryService.hasClientDependences((ConnectionItem) repositoryObject.getProperty().getItem())) {
                    if (MessageDialog.openConfirm(getShell(), Messages.getString("FileStep3.guessConfirmation"), Messages //$NON-NLS-1$
                            .getString("FileStep3.guessConfirmationMessageWithDQ"))) {//$NON-NLS-1$
                        try {
                            tdqRepositoryService.reloadMetadataOfDelimitedFile(this.metadataTable);
                        } catch (BusinessException e) {
                            popupError(e);
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }

            // update
            RepositoryUpdateManager.updateSingleSchema(connectionItem, metadataTable, oldMetadataTable, oldTableMap);

            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            try {
                factory.save(repositoryObject.getProperty().getItem());
                closeLockStrategy();
                // MOD qiongli 2011-10-21 TDQ-3797,if need to update analysises for DQ,update them after saving DB.
                if (tdqRepositoryService != null && ((SchemaObserver) schemaObserver).isUpdated()) {
                    tdqRepositoryService.updateImpactOnAnalysis((ConnectionItem) repositoryObject.getProperty().getItem());
                }// ~
            } catch (PersistenceException e) {
                popupError(e);
            }
            return true;
        } else {
            return false;
        }

    }

    private void popupError(Exception e) {
        String detailError = e.toString();
        new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("CommonWizard.persistenceException"), detailError); //$NON-NLS-1$
        log.error(Messages.getString("CommonWizard.persistenceException") + "\n" + detailError); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public boolean performCancel() {
        if (metadataTable != null && oldMetadataTable != null) {
            // Added TDQ-8360 20140327 yyin: if cancel, should restore the old columns(otherwise the used column in
            // opened analysis will not able to find its parent)
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ITDQRepositoryService.class)) {
                metadataTable.getColumns().clear();
                metadataTable.getColumns().addAll(oldColumns);
            }// ~
            if (metadataTable.getLabel() != null && !metadataTable.getLabel().equals(oldMetadataTable.getLabel())) {
                this.metadataTable.setLabel(oldMetadataTable.getLabel());
            }
        }
        return super.performCancel();
    }

    /**
     * We will accept the selection in the workbench to see if we can initialize from it.
     * 
     * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
     */
    @Override
    public void init(final IWorkbench workbench, final IStructuredSelection selection) {
        this.selection = selection;
    }

    @Override
    public ConnectionItem getConnectionItem() {
        return this.connectionItem;
    }

}
