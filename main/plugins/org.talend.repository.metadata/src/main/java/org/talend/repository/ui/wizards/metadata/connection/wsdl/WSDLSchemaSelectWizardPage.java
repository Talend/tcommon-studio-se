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
package org.talend.repository.ui.wizards.metadata.connection.wsdl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.talend.core.model.metadata.IMetadataContextModeManager;
import org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.service.IWebServiceTos;
import org.talend.repository.metadata.i18n.Messages;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class WSDLSchemaSelectWizardPage extends WSDLSchemaWizardPage {

    private static final String ALL_STEPS = "4";

    private Label label;

    private Button simpleButton;

    private Button advanceButton;

    private Boolean isWsdl = true;

    private WSDLSchemaWizardPage wsdlSchemaWizardPage1;

    private WSDLSchemaWizardPage wsdlSchemaWizardPage2;

    private WebServiceSchemaWizardPage webServiceStepPage1;

    private WebServiceSchemaWizardPage webServiceStepPage2;

    private WebServiceSchemaWizardPage webServiceStepPage3;

    private IWebServiceTos webService = CoreRuntimePlugin.getInstance().getWebServiceTos();;

    private boolean isSinglePageOnly;

    private boolean isRepositoryObjectEditable;

    public WSDLSchemaSelectWizardPage(Boolean creation, int step, ConnectionItem connectionItem,
            boolean isRepositoryObjectEditable, String[] existingNames, IMetadataContextModeManager contextModeManager,
            boolean isSinglePageOnly) {
        super(creation, step, connectionItem, isRepositoryObjectEditable, existingNames, contextModeManager);
        this.isSinglePageOnly = isSinglePageOnly;
        this.isRepositoryObjectEditable = isRepositoryObjectEditable;
        // TODO Auto-generated constructor stub
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
        label.setText("Select one model to create WebService metadata");

        simpleButton = new Button(composite, SWT.RADIO);
        simpleButton.setText("Simple WSDL");
        // simpleButton.setSelection(true);
        advanceButton = new Button(composite, SWT.RADIO);
        advanceButton.setText("Advanced WebService");

        IWizard wizard = getWizard();
        boolean lockSelectButton = false;
        if (wizard instanceof WSDLSchemaWizard) {
            INode node = ((WSDLSchemaWizard) wizard).getNode();
            if (node != null) {
                IElementParameter elementParameter = node.getElementParameter("PROPERTY"); //$NON-NLS-1$
                if (elementParameter != null) {
                    if ("WSDL".equals(elementParameter.getRepositoryValue())) { //$NON-NLS-1$
                        lockSelectButton = true;
                        isWsdl = true;
                        ((WSDLSchemaConnection) connectionItem.getConnection()).setIsInputModel(true);
                    } else if ("WEBSERVICE".equals(elementParameter.getRepositoryValue())) { //$NON-NLS-1$
                        lockSelectButton = true;
                        isWsdl = false;
                        ((WSDLSchemaConnection) connectionItem.getConnection()).setIsInputModel(false);
                    }
                }
            }
        }

        boolean inputModel = ((WSDLSchemaConnection) connectionItem.getConnection()).isIsInputModel();
        if (creation) {
            simpleButton.setSelection(isWsdl);
        } else {
            simpleButton.setSelection(inputModel);
        }
        simpleButton.setEnabled(creation);
        if (creation) {
            advanceButton.setSelection(!isWsdl);
        } else {
            advanceButton.setSelection(!inputModel);
        }
        advanceButton.setEnabled(creation);
        if (lockSelectButton) {
            simpleButton.setEnabled(false);
            advanceButton.setEnabled(false);
        }
        setControl(composite);
        addListeners();
    }

    private void addListeners() {
        simpleButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                isWsdl = true;
                ((WSDLSchemaConnection) connectionItem.getConnection()).setIsInputModel(true);
            }

        });
        advanceButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                isWsdl = false;
                ((WSDLSchemaConnection) connectionItem.getConnection()).setIsInputModel(false);
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
        if (creation) {
            if (isWsdl) {
                wsdlSchemaWizardPage1 = new WSDLSchemaWizardPage(2, connectionItem, isRepositoryObjectEditable, null,
                        contextModeManager);
                wsdlSchemaWizardPage1.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 3 "
                        + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS);
                wsdlSchemaWizardPage1.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep1"));
                wizardPages.add(wsdlSchemaWizardPage1);
                wsdlSchemaWizardPage1.setPageComplete(!creation);
                wsdlSchemaWizardPage1.setWizard(getWizard());

                wsdlSchemaWizardPage2 = new WSDLSchemaWizardPage(3, connectionItem, isRepositoryObjectEditable, null,
                        contextModeManager);
                wsdlSchemaWizardPage2.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 4 "
                        + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS);
                wsdlSchemaWizardPage2.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep2"));
                wizardPages.add(wsdlSchemaWizardPage2);
                wsdlSchemaWizardPage2.setPageComplete(!creation);
                wsdlSchemaWizardPage2.setWizard(getWizard());
            } else {

                webServiceStepPage1 = new WebServiceSchemaWizardPage(creation, 2, connectionItem, isRepositoryObjectEditable,
                        null, contextModeManager);
                webServiceStepPage1.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 3 "
                        + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS);
                webServiceStepPage1.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep1"));
                wizardPages.add(webServiceStepPage1);
                webServiceStepPage1.setPageComplete(true);
                webServiceStepPage1.setWizard(getWizard());

                webServiceStepPage2 = new WebServiceSchemaWizardPage(creation, 3, connectionItem, isRepositoryObjectEditable,
                        null, contextModeManager);
                webServiceStepPage2.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 4 "
                        + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS);
                webServiceStepPage2.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep1"));
                wizardPages.add(webServiceStepPage2);
                webServiceStepPage2.setPageComplete(!creation);
                webServiceStepPage2.setWizard(getWizard());

            }
        } else if (this.isSinglePageOnly == false) {
            if (((WSDLSchemaConnection) connectionItem.getConnection()).isIsInputModel()) {
                wsdlSchemaWizardPage1 = new WSDLSchemaWizardPage(2, connectionItem, isRepositoryObjectEditable, null,
                        contextModeManager);
                wsdlSchemaWizardPage1.setTitle(Messages.getString("FileWizardPage.titleUpdate") + " 2 "
                        + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS);
                wsdlSchemaWizardPage1.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep1"));
                // addPage(wsdlSchemaWizardPage1);
                wsdlSchemaWizardPage1.setWizard(getWizard());
                wizardPages.add(wsdlSchemaWizardPage1);
                wsdlSchemaWizardPage2 = new WSDLSchemaWizardPage(3, connectionItem, isRepositoryObjectEditable, null,
                        contextModeManager);
                wsdlSchemaWizardPage2.setTitle(Messages.getString("FileWizardPage.titleUpdate") + " 3 "
                        + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS);
                wsdlSchemaWizardPage2.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep2"));
                // addPage(wsdlSchemaWizardPage2);
                wsdlSchemaWizardPage2.setWizard(getWizard());
                wizardPages.add(wsdlSchemaWizardPage2);
            } else {
                webServiceStepPage1 = new WebServiceSchemaWizardPage(creation, 2, connectionItem, isRepositoryObjectEditable,
                        null, contextModeManager);
                webServiceStepPage1.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 3 "
                        + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS);
                webServiceStepPage1.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep1"));
                wizardPages.add(webServiceStepPage1);
                webServiceStepPage1.setPageComplete(true);
                webServiceStepPage1.setWizard(getWizard());

                webServiceStepPage2 = new WebServiceSchemaWizardPage(creation, 3, connectionItem, isRepositoryObjectEditable,
                        null, contextModeManager);
                webServiceStepPage2.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 4 "
                        + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS);
                webServiceStepPage2.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep1"));
                wizardPages.add(webServiceStepPage2);
                webServiceStepPage2.setPageComplete(creation);
                webServiceStepPage2.setWizard(getWizard());
            }
        } else {
            if (((WSDLSchemaConnection) connectionItem.getConnection()).isIsInputModel()) {
                wsdlSchemaWizardPage1 = new WSDLSchemaWizardPage(2, connectionItem, isRepositoryObjectEditable, null,
                        contextModeManager);
                wsdlSchemaWizardPage1.setTitle(Messages.getString("FileWizardPage.titleUpdate") + " 2 "
                        + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS);
                wsdlSchemaWizardPage1.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep1"));
                wsdlSchemaWizardPage1.setWizard(getWizard());
                wizardPages.add(wsdlSchemaWizardPage1);

                wsdlSchemaWizardPage2 = new WSDLSchemaWizardPage(3, connectionItem, isRepositoryObjectEditable, null,
                        contextModeManager);
                wsdlSchemaWizardPage2.setTitle(Messages.getString("FileWizardPage.titleUpdate") + " 3 "
                        + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS);
                wsdlSchemaWizardPage2.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep2")); //$NON-NLS-1$
                wsdlSchemaWizardPage2.setWizard(getWizard());
                wizardPages.add(wsdlSchemaWizardPage2);
            } else {
                webServiceStepPage1 = new WebServiceSchemaWizardPage(creation, 2, connectionItem, isRepositoryObjectEditable,
                        null, contextModeManager);
                webServiceStepPage1.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 3 "
                        + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS);
                webServiceStepPage1.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep1"));
                wizardPages.add(webServiceStepPage1);
                webServiceStepPage1.setPageComplete(true);
                webServiceStepPage1.setWizard(getWizard());

                webServiceStepPage2 = new WebServiceSchemaWizardPage(creation, 3, connectionItem, isRepositoryObjectEditable,
                        null, contextModeManager);
                webServiceStepPage2.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 4 "
                        + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS);
                webServiceStepPage2.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep1"));
                wizardPages.add(webServiceStepPage2);
                webServiceStepPage2.setPageComplete(creation);
                webServiceStepPage2.setWizard(getWizard());

            }
        }
        ((WSDLSchemaWizard) getWizard()).setDynamicWizardPages(wizardPages);
    }
}
