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
package org.talend.repository.ui.wizards.metadata.connection.database;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.metadata.managment.ui.wizard.AbstractForm;
import org.talend.metadata.managment.ui.wizard.RepositoryWizard;

/**
 * DatabaseWizard present the DatabaseForm. Use to Use to manage the metadata connection. Page allows setting a
 * database.
 */
public class DatabaseWizardPage extends WizardPage {

    private ConnectionItem connectionItem;

    private DatabaseForm databaseForm;

    private final String[] existingNames;

    private final boolean isRepositoryObjectEditable;

    /**
     * DatabaseWizardPage constructor.
     * 
     * @param connection
     * @param isRepositoryObjectEditable
     * @param existingNames
     */
    public DatabaseWizardPage(ConnectionItem connectionItem, boolean isRepositoryObjectEditable, String[] existingNames) {
        super("wizardPage"); //$NON-NLS-1$
        this.connectionItem = connectionItem;
        this.existingNames = existingNames;
        this.isRepositoryObjectEditable = isRepositoryObjectEditable;
    }

    /**
     * Create the composites, initialize it and add controls.
     * 
     * @see IDialogPage#createControl(Composite)
     */
    @Override
    public void createControl(final Composite parent) {
        boolean isCreation = false;
        if (this.getWizard() instanceof RepositoryWizard) {
            isCreation = ((RepositoryWizard) getWizard()).isCreation();
        }
        databaseForm = new DatabaseForm(parent, connectionItem, existingNames, isCreation);
        databaseForm.setReadOnly(!isRepositoryObjectEditable);
        databaseForm.updateSpecialFieldsState();

        AbstractForm.ICheckListener listener = new AbstractForm.ICheckListener() {

            @Override
            public void checkPerformed(final AbstractForm source) {
                if (source.isStatusOnError()) {
                    DatabaseWizardPage.this.setPageComplete(false);
                    setErrorMessage(source.getStatus());
                } else {
                    DatabaseWizardPage.this.setPageComplete(isRepositoryObjectEditable);
                    setErrorMessage(null);
                    setMessage(source.getStatus(), source.getStatusLevel());
                }
            }
        };
        databaseForm.setListener(listener);
        setControl(databaseForm);
        if (connectionItem.getProperty().getLabel() != null && !connectionItem.getProperty().getLabel().equals("")) { //$NON-NLS-1$
            databaseForm.checkFieldsValue();
        }
    }

    /**
     * 
     * DOC zshen Comment method "getMetadataConnection".
     * 
     * @return
     */
    public IMetadataConnection getMetadataConnection() {
        return databaseForm.getMetadataConnection();
    }

    public ContextType getSelectedContextType() {
        return databaseForm.getSelectedContextType();
    }
}
