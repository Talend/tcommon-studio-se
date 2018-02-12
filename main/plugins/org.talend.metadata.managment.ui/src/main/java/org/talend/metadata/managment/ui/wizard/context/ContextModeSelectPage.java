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
package org.talend.metadata.managment.ui.wizard.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.metadata.managment.ui.i18n.Messages;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;
import org.talend.metadata.managment.ui.wizard.PropertiesWizardPage;
import org.talend.metadata.managment.ui.wizard.metadata.connection.Step0WizardPage;

public class ContextModeSelectPage extends WizardPage {

    protected boolean creation;

    private boolean isRepositoryObjectEditable;

    private IContextManager contextManager;

    private ContextItem contextItem;

    private Set<String> connectionVariables;

    private IPath pathToSave;

    private Button createContextButton;

    private Button resueContextButton;

    private boolean createContext = true;

    private PropertiesWizardPage contextWizardPage0;

    private ShowRepositoryContextsPage repositoryContextsPage;

    protected ContextModeSelectPage(IContextManager contextManager, ContextItem contextItem, Set<String> connectionVaraiables,
            boolean create, boolean isRepositoryObjectEditable, IPath pathToSave) {
        super(Messages.getString("ShowRepositoryContextPage.name")); //$NON-NLS-1$
        this.contextManager = contextManager;
        this.contextItem = contextItem;
        this.connectionVariables = connectionVaraiables;
        this.isRepositoryObjectEditable = isRepositoryObjectEditable;
        this.creation = create;
        this.pathToSave = pathToSave;
    }

    @Override
    public void createControl(Composite parent) {

        Composite selContextModeCom = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        layout.marginWidth = 10;
        layout.marginHeight = 10;
        layout.numColumns = 1;
        selContextModeCom.setLayout(layout);

        Label label = new Label(selContextModeCom, SWT.NONE);
        GridData labelData = new GridData();
        labelData.verticalSpan = 8;
        label.setLayoutData(labelData);
        label.setText(Messages.getString("ContextModeSelectPage.contextModes")); //$NON-NLS-1$

        createContextButton = new Button(selContextModeCom, SWT.RADIO);
        createContextButton.setText(Messages.getString("ContextModeSelectPage.createContext")); //$NON-NLS-1$
        createContextButton.setSelection(false);
        createContextButton.setEnabled(true);

        resueContextButton = new Button(selContextModeCom, SWT.RADIO);
        resueContextButton.setText(Messages.getString("ContextModeSelectPage.reuseContext")); //$NON-NLS-1$
        resueContextButton.setSelection(false);
        resueContextButton.setEnabled(true);
        setControl(selContextModeCom);
        addListeners();
    }

    private void addListeners() {
        createContextButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                ((ContextModeWizard) getWizard()).clearAdaptModels();
                createContext = true;
                setModeFlag();
            }

        });
        resueContextButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                createContext = false;
                setModeFlag();
            }

        });
    }

    private void setModeFlag() {
        ((ContextModeWizard) getWizard()).setCreateContext(createContext);
    }

    @Override
    public IWizardPage getNextPage() {
        collectDynamicPages();
        return super.getNextPage();
    }

    private void collectDynamicPages() {
        List<IWizardPage> dynamicPages = new ArrayList<IWizardPage>();
        if (createContext) {
            contextWizardPage0 = new Step0WizardPage(contextItem.getProperty(), pathToSave, ERepositoryObjectType.CONTEXT,
                    !isRepositoryObjectEditable, creation);
            contextWizardPage0.setTitle(Messages.getString("ContextWizard.step0Title")); //$NON-NLS-1$
            contextWizardPage0.setDescription(Messages.getString("ContextWizard.step0Description")); //$NON-NLS-1$
            dynamicPages.add(contextWizardPage0);
            contextWizardPage0.setPageComplete(false);
            contextWizardPage0.setWizard(getWizard());

            ContextPage contextPage = new ContextPage("test", contextManager, !isRepositoryObjectEditable); //$NON-NLS-1$
            contextPage.setTitle(Messages.getString("ContextWizard.contextPageTitle")); //$NON-NLS-1$
            contextPage.setDescription(Messages.getString("ContextWizard.contextPageDescription")); //$NON-NLS-1$
            dynamicPages.add(contextPage);
            contextPage.setPageComplete(true);
            contextPage.setWizard(getWizard());
        } else {
            repositoryContextsPage = new ShowRepositoryContextsPage(ConnectionContextHelper.getContextItems(), 1);
            dynamicPages.add(repositoryContextsPage);
            repositoryContextsPage.setPageComplete(false);
            repositoryContextsPage.setWizard(getWizard());

            ContextAdaptConectionSelectPage adaptSelectPage = new ContextAdaptConectionSelectPage(contextManager,
                    connectionVariables, 2);
            dynamicPages.add(adaptSelectPage);
            adaptSelectPage.setPageComplete(false);
            adaptSelectPage.setWizard(getWizard());

            ContextVariableValuePage valuesPage = new ContextVariableValuePage(contextManager, 3);
            dynamicPages.add(valuesPage);
            valuesPage.setPageComplete(true);
            valuesPage.setWizard(getWizard());
        }
        ((ContextModeWizard) getWizard()).setDynamicWizardPages(dynamicPages);
    }

    public IWizardPage getPropertiesPage() {
        return contextWizardPage0;
    }

    public ContextItem getReuseItem() {
        return repositoryContextsPage.getSelectedContextItem();
    }

}
