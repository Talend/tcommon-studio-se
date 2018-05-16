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
package org.talend.repository.ui.wizards.metadata.connection.files.xml;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.repository.metadata.i18n.Messages;

/**
 * wzhang class global comment. Detailled comment
 */
public class XmlFileSelectWizardPage extends XmlFileWizardPage {

    private XmlFileWizardPage xmlFileInputStep1;

    private XmlFileWizardPage xmlFileInputStep2;

    private XmlFileWizardPage xmlFileInputStep3;

    private XmlFileOutputWizardPage xmlFileOutputStep1;

    private XmlFileOutputWizardPage xmlFileOutputStep2;

    private XmlFileOutputWizardPage xmlFileOutputStep3;

    private Label label;

    private Button inputModeButton;

    private Button outputModeButton;

    private boolean isInputModel = true;

    public XmlFileSelectWizardPage(boolean creation, ConnectionItem connectionItem, boolean isRepositoryObjectEditable,
            String[] existingNames) {
        super(creation, connectionItem, isRepositoryObjectEditable, existingNames);
    }

    @Override
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
        label.setText("Select one model to create XML metadata");

        boolean inputModel = ((XmlFileConnection) connectionItem.getConnection()).isInputModel();
        inputModeButton = new Button(composite, SWT.RADIO);
        inputModeButton.setText(Messages.getString("XmlFileSelectWizardPage.Input_XML"));//$NON-NLS-1$
        if (creation) {
            inputModeButton.setSelection(isInputModel);
        } else {
            inputModeButton.setSelection(inputModel);
        }
        inputModeButton.setEnabled(creation);

        outputModeButton = new Button(composite, SWT.RADIO);
        outputModeButton.setText(Messages.getString("XmlFileSelectWizardPage.OutPut_XML"));//$NON-NLS-1$
        if (creation) {
            outputModeButton.setSelection(!isInputModel);
        } else {
            outputModeButton.setSelection(!inputModel);
        }
        outputModeButton.setEnabled(creation);
        setControl(composite);
        addListeners();
    }

    private void addListeners() {
        inputModeButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                isInputModel = true;
                ((XmlFileConnection) connectionItem.getConnection()).setInputModel(true);
            }

        });
        outputModeButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                isInputModel = false;
                ((XmlFileConnection) connectionItem.getConnection()).setInputModel(false);
            }

        });
    }

    @Override
    public IWizardPage getNextPage() {
        generateDynamicWizardPage();
        return super.getNextPage();
    }

    private void generateDynamicWizardPage() {
        List<IWizardPage> wizardPages = new ArrayList<IWizardPage>();
        boolean inputModel = false;
        if (creation) {
            inputModel = isInputModel;
        } else {
            inputModel = ((XmlFileConnection) connectionItem.getConnection()).isInputModel();
        }
        if (inputModel) {
            xmlFileInputStep1 = new XmlFileWizardPage(creation, 1, connectionItem, isRepositoryObjectEditable, existingNames);
            xmlFileInputStep1.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 3 "
                    + Messages.getString("FileWizardPage.of") + " 5");
            xmlFileInputStep1.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep1"));
            wizardPages.add(xmlFileInputStep1);
            xmlFileInputStep1.setPageComplete(!creation);
            xmlFileInputStep1.setWizard(getWizard());

            xmlFileInputStep2 = new XmlFileWizardPage(creation, 2, connectionItem, isRepositoryObjectEditable, existingNames);
            xmlFileInputStep2.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 4 "
                    + Messages.getString("FileWizardPage.of") + " 5");
            xmlFileInputStep2.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep2"));
            wizardPages.add(xmlFileInputStep2);
            xmlFileInputStep2.setPageComplete(!creation);
            xmlFileInputStep2.setWizard(getWizard());

            if (creation) {
                xmlFileInputStep3 = new XmlFileWizardPage(creation, 3, connectionItem, isRepositoryObjectEditable, null);
                xmlFileInputStep3.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 5 "
                        + Messages.getString("FileWizardPage.of") + " 5");
                xmlFileInputStep3.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep3"));
                wizardPages.add(xmlFileInputStep3);
                xmlFileInputStep3.setPageComplete(!creation);
                xmlFileInputStep3.setWizard(getWizard());
            }

        } else {
            xmlFileOutputStep1 = new XmlFileOutputWizardPage(creation, 1, connectionItem, isRepositoryObjectEditable,
                    existingNames);
            xmlFileOutputStep1.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 3 "
                    + Messages.getString("FileWizardPage.of") + " 5");
            xmlFileOutputStep1.setDescription("Select Create manually or Create from a file\nDefine the output file");
            wizardPages.add(xmlFileOutputStep1);
            xmlFileOutputStep1.setPageComplete(!creation);
            xmlFileOutputStep1.setWizard(getWizard());

            xmlFileOutputStep2 = new XmlFileOutputWizardPage(creation, 2, connectionItem, isRepositoryObjectEditable,
                    existingNames);
            xmlFileOutputStep2.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 4 "
                    + Messages.getString("FileWizardPage.of") + " 5");
            xmlFileOutputStep2.setDescription("Add or edit schema and tree\nDrag&Drop to define columns");
            wizardPages.add(xmlFileOutputStep2);
            xmlFileOutputStep2.setPageComplete(!creation);
            xmlFileOutputStep2.setWizard(getWizard());

            if (creation) {
                xmlFileOutputStep3 = new XmlFileOutputWizardPage(creation, 3, connectionItem, isRepositoryObjectEditable,
                        existingNames);
                xmlFileOutputStep3.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 5 "
                        + Messages.getString("FileWizardPage.of") + " 5");
                xmlFileOutputStep2.setDescription("Add a Schema on repository\nDefine the Schema");
                wizardPages.add(xmlFileOutputStep3);
                xmlFileOutputStep3.setPageComplete(!creation);
                xmlFileOutputStep3.setWizard(getWizard());
            }
        }
        ((XmlFileWizard) getWizard()).setDynamicWizardPages(wizardPages);
    }
}
