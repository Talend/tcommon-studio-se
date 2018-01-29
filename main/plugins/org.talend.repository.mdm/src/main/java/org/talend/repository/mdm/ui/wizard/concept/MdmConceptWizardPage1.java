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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.talend.core.model.metadata.builder.connection.MdmConceptType;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.repository.mdm.i18n.Messages;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC wchen class global comment. Detailled comment
 */
public class MdmConceptWizardPage1 extends AbstractRetrieveConceptPage {

    private Label label;

    private Button inputModeButton;

    private Button outputModeButton;

    private Button mdmReceiveBtn;

    /**
     * DOC wchen MDMInOutSelectPage constructor comment.
     * 
     * @param pageName
     * @param title
     * @param titleImage
     */
    public MdmConceptWizardPage1(RepositoryNode node, ConnectionItem connectionItem, MetadataTable metadataTable, boolean creation) {
        super(node, connectionItem, metadataTable, creation);
        this.setTitle(Messages.getString("MdmConceptWizardPage1_mdm_model")); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginWidth = 10;
        layout.marginHeight = 10;
        layout.numColumns = 1;
        composite.setLayout(layout);

        label = new Label(composite, SWT.NONE);
        GridData labelData = new GridData();
        labelData.verticalSpan = 8;
        label.setLayoutData(labelData);
        label.setText(Messages.getString("MdmConceptWizardPage1_select_model")); //$NON-NLS-1$

        // boolean inputModel = ((MDMConnection) connectionItem.getConnection()).isInputModel();
        getConcept();
        inputModeButton = new Button(composite, SWT.RADIO);
        inputModeButton.setText(Messages.getString("MdmConceptWizardPage1_input_mdm")); //$NON-NLS-1$
        if (concept != null)
            inputModeButton.setSelection(MdmConceptType.INPUT.equals(concept.getConceptType()));
        inputModeButton.setEnabled(creation);

        outputModeButton = new Button(composite, SWT.RADIO);
        outputModeButton.setText(Messages.getString("MdmConceptWizardPage1_output_mdm")); //$NON-NLS-1$
        if (concept != null)
            outputModeButton.setSelection(MdmConceptType.OUTPUT.equals(concept.getConceptType()));
        outputModeButton.setEnabled(creation);

        mdmReceiveBtn = new Button(composite, SWT.RADIO);
        mdmReceiveBtn.setText(Messages.getString("MdmConceptWizardPage1_receive_mdm")); //$NON-NLS-1$
        if (concept != null)
            mdmReceiveBtn.setSelection(MdmConceptType.RECEIVE.equals(concept.getConceptType()));
        mdmReceiveBtn.setEnabled(creation);

        setControl(composite);
        addListeners();

    }

    private void addListeners() {
        inputModeButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (concept != null) {
                    concept.setConceptType(MdmConceptType.INPUT);
                }
            }

        });
        outputModeButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (concept != null) {
                    concept.setConceptType(MdmConceptType.OUTPUT);
                }
            }

        });

        mdmReceiveBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (concept != null) {
                    concept.setConceptType(MdmConceptType.RECEIVE);
                }
            }
        });
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            ((CreateConceptWizard) getWizard()).setCurrentPage(this);
        }
    }

}
