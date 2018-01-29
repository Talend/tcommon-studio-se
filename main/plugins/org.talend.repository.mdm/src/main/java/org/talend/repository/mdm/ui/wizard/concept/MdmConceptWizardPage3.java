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
package org.talend.repository.mdm.ui.wizard.concept;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.builder.connection.MdmConceptType;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.metadata.managment.ui.wizard.AbstractForm;
import org.talend.repository.mdm.i18n.Messages;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC hwang class global comment. Detailled comment
 */
public class MdmConceptWizardPage3 extends AbstractRetrieveConceptPage {

    // private MDMSchemaForm mdmSchemaForm;
    private AbstractMDMFileStepForm xsdFileForm;

    private boolean isRepositoryObjectEditable;

    private MetadataTable metadataTable;

    /**
     * DOC Administrator MDMSchemaWizardPage constructor comment.
     * 
     * @param pageName
     */
    protected MdmConceptWizardPage3(RepositoryNode node, ConnectionItem connectionItem, MetadataTable metadataTable,
            boolean isRepositoryObjectEditable, boolean creation) {
        super(node, connectionItem, metadataTable, creation);
        this.metadataTable = metadataTable;
        this.isRepositoryObjectEditable = isRepositoryObjectEditable;
        this.setTitle(Messages.getString("MdmConceptWizardPage3_mdm_entity")); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
        if (getConcept() != null) {
            if (MdmConceptType.INPUT.equals(getConcept().getConceptType())) {
                if (getPreviousPage() instanceof MdmConceptWizardPage2) {
                    xsdFileForm = new MDMXSDFileForm(parent, connectionItem, metadataTable, getConcept(), this, creation);
                }
            } else if (MdmConceptType.OUTPUT.equals(getConcept().getConceptType())) {
                xsdFileForm = new MDMOutputSchemaForm(parent, connectionItem, metadataTable, getConcept(), this, creation);
            } else if (MdmConceptType.RECEIVE.equals(getConcept().getConceptType())) {
                xsdFileForm = new MdmReceiveForm(parent, connectionItem, metadataTable, getConcept(), this, creation);
            }
            xsdFileForm.setReadOnly(!isRepositoryObjectEditable);

            AbstractForm.ICheckListener listener = new AbstractForm.ICheckListener() {

                public void checkPerformed(final AbstractForm source) {
                    if (source.isStatusOnError()) {
                        MdmConceptWizardPage3.this.setPageComplete(false);
                        setErrorMessage(source.getStatus());
                    } else {
                        MdmConceptWizardPage3.this.setPageComplete(true);
                        setErrorMessage(null);
                        setMessage(source.getStatus(), source.getStatusLevel());
                    }
                }
            };
            xsdFileForm.setListener(listener);
            this.setPageComplete(false);
            setControl(xsdFileForm);
            xsdFileForm.setPage(this);
        }
    }

    // public void setConceptName(String name) {
    // xsdFileForm.setConceptName(name);
    // }
    //
    public void createMetadataTable() {
        xsdFileForm.createTable();
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            ((CreateConceptWizard) getWizard()).setCurrentPage(this);
        }
    }

    @Override
    public IWizardPage getNextPage() {
        if (!creation) {
            createMetadataTable();
        }
        return super.getNextPage();
    }

}
