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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.metadata.managment.ui.wizard.AbstractForm;

/**
 * DOC Hwang class global comment. Detailled comment
 */
public class UniversePage extends WizardPage {

    private UniverseForm universeForm = null;

    // private MDMWizardPage mdmWizardPage;

    private List<String> list = new ArrayList<String>();

    private ConnectionItem connectionItem;

    /**
     * DOC Administrator UniseverPage constructor comment.
     * 
     * @param pageName
     */
    protected UniversePage(ConnectionItem connectionItem, String pageName) {
        super(pageName);
        this.connectionItem = connectionItem;
        this.setTitle("Talend MDM"); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
        universeForm = new UniverseForm(parent, SWT.NONE, null, connectionItem, this);
        universeForm.setReadOnly(false);// (!isRepositoryObjectEditable);

        AbstractForm.ICheckListener listener = new AbstractForm.ICheckListener() {

            public void checkPerformed(final AbstractForm source) {
                if (source.isStatusOnError()) {
                    UniversePage.this.setPageComplete(false);
                    setErrorMessage(source.getStatus());
                } else {
                    UniversePage.this.setPageComplete(true);// (isRepositoryObjectEditable);
                    setErrorMessage(null);
                    setMessage(source.getStatus(), source.getStatusLevel());
                }
            }
        };
        universeForm.setListener(listener);
        setControl(universeForm);
        //        if (connectionItem.getProperty().getLabel() != null && !connectionItem.getProperty().getLabel().equals("")) { //$NON-NLS-1$
        // universeForm.checkFieldsValue();
        // }
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            ((MDMWizard) getWizard()).setCurrentPage(this);
        }
    }

}
