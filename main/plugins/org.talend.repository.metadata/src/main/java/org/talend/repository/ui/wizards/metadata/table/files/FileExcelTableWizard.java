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

import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.update.RepositoryUpdateManager;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.metadata.managment.ui.wizard.AbstractRepositoryFileTableWizard;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * 
 * DOC yexiaowei class global comment. Detailled comment
 */
public class FileExcelTableWizard extends AbstractRepositoryFileTableWizard implements INewWizard {

    private static Logger log = Logger.getLogger(FileExcelTableWizard.class);

    private FileTableWizardPage tableWizardpage;

    private Map<String, String> oldTableMap;

    private IMetadataTable oldMetadataTable;

    /**
     * 
     * DOC yexiaowei FileExcelTableWizard constructor comment.
     * 
     * @param workbench
     * @param creation
     * @param connectionItem
     * @param metadataTable
     * @param forceReadOnly
     */
    public FileExcelTableWizard(IWorkbench workbench, boolean creation, ConnectionItem connectionItem,
            MetadataTable metadataTable, boolean forceReadOnly) {
        super(workbench, creation, forceReadOnly);
        this.connectionItem = connectionItem;
        this.metadataTable = metadataTable;
        if (connectionItem != null) {
            oldTableMap = RepositoryUpdateManager.getOldTableIdAndNameMap(connectionItem, metadataTable, creation);
            oldMetadataTable = ConvertionHelper.convert(metadataTable);
            // initConnectionCopy(connectionItem.getConnection());
        }
        setNeedsProgressMonitor(true);

        isRepositoryObjectEditable();
        initLockStrategy();
    }

    /**
     * Adding the page to the wizard.
     */

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
        }
        addPage(tableWizardpage);
    }

    public boolean performFinish() {
        if (tableWizardpage.isPageComplete()) {
            // applyConnectionCopy();
            // update
            RepositoryUpdateManager.updateSingleSchema(connectionItem, metadataTable, oldMetadataTable, oldTableMap);

            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            try {
                factory.save(repositoryObject.getProperty().getItem());
                closeLockStrategy();
            } catch (PersistenceException e) {
                String detailError = e.toString();
                new ErrorDialogWidthDetailArea(getShell(), PID,
                        Messages.getString("CommonWizard.persistenceException"), detailError); //$NON-NLS-1$
                log.error(Messages.getString("CommonWizard.persistenceException") + "\n" + detailError); //$NON-NLS-1$ //$NON-NLS-2$
            }
            // connectionCopy = null;
            // metadataTableCopy = null;
            return true;
        } else {
            return false;
        }

    }

    public boolean performCancel() {
        if (metadataTable != null && oldMetadataTable != null && metadataTable.getLabel() != null
                && !metadataTable.getLabel().equals(oldMetadataTable.getLabel())) {
            this.metadataTable.setLabel(oldMetadataTable.getLabel());
        }
        return super.performCancel();
    }

    public void init(final IWorkbench workbench, final IStructuredSelection selection) {
        this.selection = selection;
    }

    @Override
    public ConnectionItem getConnectionItem() {
        return this.connectionItem;
    }

}
