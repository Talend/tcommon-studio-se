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
package org.talend.repository.ui.wizards.metadata.table.database;

import java.util.List;
import java.util.Set;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.swt.composites.StateComposite;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.database.TableInfoParameters;
import org.talend.core.model.metadata.builder.database.TableNode;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.metadata.managment.ui.wizard.AbstractForm;

import orgomg.cwm.resource.relational.Catalog;

/**
 * TableWizard present the TableForm width the MetaDataTable. Use to create a new table (need a connection to a DB).
 * Page allows setting a table.
 */
public class SelectorTableWizardPage extends TemplateWizardPage {

    private Composite container;

    private SelectorTableForm tableForm;

    private StateComposite stateCom;

    private final ConnectionItem connectionItem;

    private ConnectionItem templateConnection;

    private boolean isRepositoryObjectEditable;

    private final TableInfoParameters tableInfoParameters;

    private IMetadataConnection metadataConnection;

    private boolean isCreateTemplate = false;

    private DatabaseConnection temConnection;

    private Set<MetadataTable> oldTables;

    /**
     * SelectorTableWizardPage constructor (to instance IMetadataConnection OR MetaDataTableType). If MetaDataTableType
     * exist, it's an update of existing metadata else it's a new metadata.
     * 
     * @param tableInfoParameters
     * 
     * @param ISelection
     */
    public SelectorTableWizardPage(ConnectionItem connectionItem, boolean isRepositoryObjectEditable,
            TableInfoParameters tableInfoParameters, IMetadataConnection metadataConnection, DatabaseConnection temConnection) {
        super("wizardPage"); //$NON-NLS-1$
        this.connectionItem = connectionItem;
        this.isRepositoryObjectEditable = isRepositoryObjectEditable;
        this.tableInfoParameters = tableInfoParameters;
        this.metadataConnection = metadataConnection;
        this.temConnection = temConnection;
    }

    public SelectorTableWizardPage(ConnectionItem connectionItem, boolean isRepositoryObjectEditable,
            TableInfoParameters tableInfoParameters, IMetadataConnection metadataConnection, boolean isCreateTemplate) {
        super("wizardPage"); //$NON-NLS-1$
        this.connectionItem = connectionItem;
        this.templateConnection = connectionItem;
        this.isRepositoryObjectEditable = isRepositoryObjectEditable;
        this.tableInfoParameters = tableInfoParameters;
        this.metadataConnection = metadataConnection;
        this.isCreateTemplate = isCreateTemplate;
        oldTables = ConnectionHelper.getTables(templateConnection.getConnection());
    }

    /**
     * Create the first composite, addComponentsAndControls and initialize TableWizardPage.
     * 
     * @see IDialogPage#createControl(Composite)
     */
    public void createControl(final Composite parent) {
        container = new Composite(parent, SWT.NONE);

        GridData data = new GridData(GridData.FILL_BOTH);

        container.setLayoutData(data);
        container.setLayout(new FillLayout());
        addFields(container);

        setControl(container);
    }

    private void addFields(Composite container) {
        int lines = 7;
        if (isCreateTemplate) {
            lines = 9;
        }
        container.setLayout(new GridLayout(lines, true));
        GridData data = new GridData(GridData.FILL_BOTH);
        if (isCreateTemplate) {
            data.horizontalSpan = 2;
            stateCom = new StateComposite(container, false, SWT.NONE);
            stateCom.setLayoutData(data);
            stateCom.refreshState("step3");//$NON-NLS-1$
        }

        data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 7;
        if (isCreateTemplate) {
            // Catalog c = (Catalog) ConnectionHelper.getPackage(((DatabaseConnection)
            // templateConnection.getConnection()).getSID(),
            // templateConnection.getConnection(), Catalog.class);
            // if (c != null) { // hywang
            // c.getOwnedElement().clear();
            // }
            tableForm = new SelectorTableForm(container, templateConnection, this, isCreateTemplate, temConnection,
                    metadataConnection);
        } else {
            tableForm = new SelectorTableForm(container, connectionItem, this, isCreateTemplate, temConnection,
                    metadataConnection);
        }

        tableForm.setLayoutData(data);
        tableForm.setReadOnly(!isRepositoryObjectEditable);

        AbstractForm.ICheckListener listener = new AbstractForm.ICheckListener() {

            public void checkPerformed(final AbstractForm source) {
                if (source.isStatusOnError()) {
                    SelectorTableWizardPage.this.setPageComplete(false);
                }
                if (source.isStatusOnValid()) {
                    SelectorTableWizardPage.this.setPageComplete(true);
                }
            }
        };
        tableForm.setListener(listener);
        setControl(container);
        if (isCreateTemplate) {
            tableForm.initControlData(false);
        }
    }

    /**
     * DOC nrousseau Comment method "performCancel".
     */
    public void performCancel() {
        if (tableForm != null) {
            tableForm.performCancel();
        }
    }

    /**
     * Getter for tableInfoParameters.
     * 
     * @return the tableInfoParameters
     */
    public TableInfoParameters getTableInfoParameters() {
        return this.tableInfoParameters;
    }

    public void initControlData() {
        tableForm.initControlData(true);
    }

    public DatabaseConnection getDatabaseConnection() {
        if (tableForm == null) {
            return null;
        }
        return tableForm.getConnection();
    }

    public void setTemplateConnection(ConnectionItem templateConnection, IMetadataConnection metadataConnection) {
        if (this.templateConnection == templateConnection) {// && this.metadataConnection == metadataConnection) {
            return;
        }
        this.templateConnection = templateConnection;
        this.metadataConnection = metadataConnection;
        if (tableForm != null) {
            tableForm.setTemplateConntion(templateConnection);
            tableForm.setIMetadataConnection(metadataConnection);
            Catalog c = (Catalog) ConnectionHelper.getPackage(((DatabaseConnection) tableForm.getConnection()).getSID(),
                    tableForm.getConnection(), Catalog.class);
            if (c != null) { // hywang
                c.getOwnedElement().clear();
            }
            if (isCreateTemplate) {
                tableForm.initControlData(false);
            }
        }
    }

    public void restoreCheckItems() {
        tableForm.restoreCheckItems();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.metadata.table.database.TemplateWizardPage#getConnection()
     */
    public Connection getConnection() {
        return getDatabaseConnection();
    }

    public List<TableNode> getTableNodeList() {
        return tableForm.getTableNodeList();
    }

    public Set<MetadataTable> getOldTables() {
        return this.oldTables;
    }

}
