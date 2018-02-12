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
package org.talend.repository.ui.wizards.metadata.table.database;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.database.TableInfoParameters;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.metadata.managment.ui.wizard.AbstractForm;

/**
 * TableWizard present the TableForm width the MetaDataTable. Use to create a new table (need a connection to a DB).
 * Page allows setting a table.
 */
public class DatabaseTableFilterWizardPage extends WizardPage {

    private DatabaseTableFilterForm tableForm;

    private final TableInfoParameters tableInfoParameters;

    private final ConnectionItem connectionItem; // hywang add

    private IMetadataConnection metadataConnection;

    /**
     * DatabaseWizardPage constructor (to instance IMetadataConnection OR MetaDataTableType). If MetaDataTableType
     * exist, it's an update of existing metadata else it's a new metadata.
     * 
     * @param tableInfoParameters
     * 
     * @param managerConnection
     * 
     * @param ISelection
     */
    public DatabaseTableFilterWizardPage(TableInfoParameters tableInfoParameters, ConnectionItem connectionItem,
            IMetadataConnection metadataconnection) {
        super("wizardPage"); //$NON-NLS-1$
        this.tableInfoParameters = tableInfoParameters;
        this.connectionItem = connectionItem;
        this.metadataConnection = metadataconnection;
    }

    public ConnectionItem getConnectionItem() {
        return this.connectionItem;
    }

    /**
     * Create the first composite, addComponentsAndControls and initialize TableWizardPage.
     * 
     * @see IDialogPage#createControl(Composite)
     */
    public void createControl(final Composite parent) {

        tableForm = new DatabaseTableFilterForm(parent, this, metadataConnection);

        AbstractForm.ICheckListener listener = new AbstractForm.ICheckListener() {

            public void checkPerformed(final AbstractForm source) {
                if (source.isStatusOnError()) {
                    DatabaseTableFilterWizardPage.this.setPageComplete(false);
                    setErrorMessage(source.getStatus());
                } else {
                    DatabaseTableFilterWizardPage.this.setPageComplete(false);
                    setErrorMessage(null);
                    setMessage(source.getStatus(), source.getStatusLevel());
                }
            }
        };
        tableForm.setListener(listener);
        setControl(tableForm);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.WizardPage#getNextPage()
     */
    @Override
    public IWizardPage getNextPage() {
        CoreRuntimePlugin.getInstance().getCoreService().getPreferenceStore()
                .setValue(DatabaseTableFilterForm.PREFS_NAMEFILTER, tableForm.getNameFilter());
        getTableInfoParameters().setNameFilters(tableForm.getFilters());
        IWizardPage nextPage = super.getNextPage();
        if (nextPage instanceof SelectorTableWizardPage) {
            ((SelectorTableWizardPage) nextPage).initControlData();
        }
        return nextPage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
     */
    @Override
    public boolean canFlipToNextPage() {
        return true;
    }

    /**
     * Getter for tableInfoParameters.
     * 
     * @return the tableInfoParameters
     */
    public TableInfoParameters getTableInfoParameters() {
        return this.tableInfoParameters;
    }

}
