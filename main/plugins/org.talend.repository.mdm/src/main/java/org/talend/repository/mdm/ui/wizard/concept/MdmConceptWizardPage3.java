// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.builder.connection.MdmConceptType;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.metadata.managment.ui.wizard.AbstractForm;
import org.talend.metadata.managment.ui.wizard.AbstractForm.ICheckListener;
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
            container = new Composite(parent, SWT.NONE);
            StackLayout stackLayout = getContainerLayout();
            container.setLayout(stackLayout);

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

            mdmXsdFileForm = new MDMXSDFileForm(container, connectionItem, metadataTable, getConcept(), this, creation);
            mdmOutputFileForm = new MDMOutputSchemaForm(container, connectionItem, metadataTable, getConcept(), this, creation);
            mdmReceiveFileForm = new MdmReceiveForm(container, connectionItem, metadataTable, getConcept(), this, creation);

            for (AbstractMDMFileStepForm fileForm : new AbstractMDMFileStepForm[] { mdmXsdFileForm, mdmOutputFileForm,
                    mdmReceiveFileForm }) {
                fileForm.setReadOnly(!isRepositoryObjectEditable);
                fileForm.setListener(listener);
                fileForm.setPage(this);
            }

            setTopControl();

            setControl(container);
            setPageComplete(false);
        }
    }

    private void setTopControl() {
        StackLayout stackLayout = getContainerLayout();
        if (MdmConceptType.INPUT.equals(getConcept().getConceptType())) {
            if (getPreviousPage() instanceof MdmConceptWizardPage2) {
                stackLayout.topControl = mdmXsdFileForm;
            }
        } else if (MdmConceptType.OUTPUT.equals(getConcept().getConceptType())) {
            stackLayout.topControl = mdmOutputFileForm;
        } else if (MdmConceptType.RECEIVE.equals(getConcept().getConceptType())) {
            stackLayout.topControl = mdmReceiveFileForm;
        }
        xsdFileForm = (AbstractMDMFileStepForm) stackLayout.topControl;
    }

    private StackLayout stackLayout;

    private MDMXSDFileForm mdmXsdFileForm;

    private MDMOutputSchemaForm mdmOutputFileForm;

    private MdmReceiveForm mdmReceiveFileForm;

    private Composite container;

    private StackLayout getContainerLayout() {
        if (stackLayout == null) {
            stackLayout = new StackLayout();
        }
        return stackLayout;
    }

    // public void setConceptName(String name) {
    // xsdFileForm.setConceptName(name);
    // }
    //
    public void createMetadataTable() {
        ICheckListener listener = xsdFileForm.getListener();
        xsdFileForm.setListener(null);
        xsdFileForm.createTable();
        xsdFileForm.setListener(listener);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            setTopControl();
            container.layout();
            ((CreateConceptWizard) getWizard()).setCurrentPage(this);
        }
    }

    @Override
    public boolean isPageComplete() {
        return super.isPageComplete() || !isRepositoryObjectEditable;
    }

    @Override
    public IWizardPage getNextPage() {
        if (!creation) {
            createMetadataTable();
        }
        return super.getNextPage();
    }

}
