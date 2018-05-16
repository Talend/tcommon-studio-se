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
package org.talend.repository.mdm.ui.wizard.concept;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.metadata.managment.ui.wizard.AbstractForm;
import org.talend.repository.mdm.i18n.Messages;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC hwang class global comment. Detailled comment
 */
public class MdmConceptWizardPage2 extends AbstractRetrieveConceptPage {

    private SetNameForm form;

    private String[] existingNames;

    private boolean isRepositoryObjectEditable;

    protected MdmConceptWizardPage2(RepositoryNode node, ConnectionItem connectionItem, MetadataTable metadataTable,
            boolean isRepositoryObjectEditable, boolean creation, String[] existingNames) {
        super(node, connectionItem, metadataTable, creation); //$NON-NLS-1$
        this.setTitle(Messages.getString("MdmConceptWizardPage2_mdm_entity")); //$NON-NLS-1$

        this.existingNames = existingNames;
        this.isRepositoryObjectEditable = isRepositoryObjectEditable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {

        if (getConcept() != null) {
            form = new SetNameForm(parent, connectionItem, getConcept(), existingNames, creation);
            form.setReadOnly(!isRepositoryObjectEditable);

            AbstractForm.ICheckListener listener = new AbstractForm.ICheckListener() {

                public void checkPerformed(final AbstractForm source) {
                    if (source.isStatusOnError()) {
                        MdmConceptWizardPage2.this.setPageComplete(false);
                        setErrorMessage(source.getStatus());
                    } else {
                        MdmConceptWizardPage2.this.setPageComplete(isRepositoryObjectEditable);
                        setErrorMessage(null);
                        setMessage(source.getStatus(), source.getStatusLevel());
                    }
                }
            };
            form.setListener(listener);
            setControl(form);
            if (connectionItem.getProperty().getLabel() != null && !connectionItem.getProperty().getLabel().equals("")) { //$NON-NLS-1$
                form.checkFieldsValue();
            }
        }
    }

    public String getName() {
        return form.getName();
    }

    public String getSelectedEntity() {
        return form.getSelectedEntity();
    }

    @Override
    public IWizardPage getPreviousPage() {
        return null;
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            ((CreateConceptWizard) getWizard()).setCurrentPage(this);
        }
    }

}
