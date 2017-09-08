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
package org.talend.repository.mdm.ui.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.metadata.managment.ui.wizard.AbstractForm;

/**
 * DOC hwang class global comment. Detailled comment
 */
public class MDMWizardPage extends WizardPage {

    private ConnectionItem connectionItem;

    private MDMForm mdmForm;

    private final String[] existingNames;

    private final boolean isRepositoryObjectEditable;

    private boolean creation;

    /**
     * DOC Administrator MDMWizardPage constructor comment.
     * 
     * @param pageName
     * @param title
     * @param titleImage
     */
    protected MDMWizardPage(ConnectionItem connectionItem, boolean isRepositoryObjectEditable, boolean creation,
            String[] existingNames) {
        super("Talend MDM"); //$NON-NLS-1$
        this.connectionItem = connectionItem;
        this.existingNames = existingNames;
        this.isRepositoryObjectEditable = isRepositoryObjectEditable;
        this.creation = creation;
        this.setTitle("Talend MDM"); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createControl(Composite parent) {
        mdmForm = new MDMForm(parent, connectionItem, existingNames, this, creation);
        mdmForm.setReadOnly(!isRepositoryObjectEditable);

        AbstractForm.ICheckListener listener = new AbstractForm.ICheckListener() {

            @Override
            public void checkPerformed(final AbstractForm source) {
                if (source.isStatusOnError()) {
                    MDMWizardPage.this.setPageComplete(false);
                    setErrorMessage(source.getStatus());
                } else {
                    MDMWizardPage.this.setPageComplete(isRepositoryObjectEditable);
                    setErrorMessage(null);
                    setMessage(source.getStatus(), source.getStatusLevel());
                }
            }
        };
        mdmForm.setListener(listener);
        setControl(mdmForm);
        if (connectionItem.getProperty().getLabel() != null && !connectionItem.getProperty().getLabel().equals("")) { //$NON-NLS-1$
            mdmForm.checkFieldsValue();
        }
    }

    public Object getXtentisBindingStub() {
        return mdmForm.getXtentisBindingStub();
    }

    public String getUserName() {
        return mdmForm.getUserName();
    }

    public String getPassword() {
        return mdmForm.getPassword();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            ((MDMWizard) getWizard()).setCurrentPage(this);
        }
    }
}
