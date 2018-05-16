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
package org.talend.repository.ui.wizards.metadata.connection.files.salesforce;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.IMetadataContextModeManager;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.SalesforceSchemaConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.cwm.helper.TableHelper;
import org.talend.cwm.relational.RelationalFactory;
import org.talend.metadata.managment.ui.wizard.AbstractForm;
import org.talend.repository.metadata.ui.wizards.form.AbstractSalesforceStepForm;

/**
 * DOC yexiaowei class global comment. Detailled comment
 */
public class SalesforceWizardPage extends WizardPage {

    private ConnectionItem connectionItem;

    private int step;

    private AbstractSalesforceStepForm currentComposite;

    private final String[] existingNames;

    private boolean isRepositoryObjectEditable;

    private final SalesforceModuleParseAPI salesforceAPI;

    private IMetadataContextModeManager contextModeManager;

    private SalesforceSchemaConnection temConnection;

    private String moduleName;

    /**
     * 
     * DOC YeXiaowei SalesforceWizardPage constructor comment.
     * 
     * @param step
     * @param connectionItem
     * @param isRepositoryObjectEditable
     * @param existingNames
     */
    public SalesforceWizardPage(int step, ConnectionItem connectionItem, boolean isRepositoryObjectEditable,
            String[] existingNames, SalesforceModuleParseAPI salesforceAPI, IMetadataContextModeManager contextModeManager) {
        super("wizardPage"); //$NON-NLS-1$
        this.step = step;
        this.connectionItem = connectionItem;
        this.existingNames = existingNames;
        this.isRepositoryObjectEditable = isRepositoryObjectEditable;
        this.salesforceAPI = salesforceAPI;
        this.contextModeManager = contextModeManager;
    }

    public SalesforceWizardPage(int step, ConnectionItem connectionItem, SalesforceSchemaConnection temConnection,
            boolean isRepositoryObjectEditable, String[] existingNames, SalesforceModuleParseAPI salesforceAPI,
            IMetadataContextModeManager contextModeManager, String moduleName) {
        super("wizardPage"); //$NON-NLS-1$
        this.step = step;
        this.temConnection = temConnection;
        this.connectionItem = connectionItem;
        this.existingNames = existingNames;
        this.isRepositoryObjectEditable = isRepositoryObjectEditable;
        this.salesforceAPI = salesforceAPI;
        this.contextModeManager = contextModeManager;
        this.moduleName = moduleName;
    }

    /**
     * 
     * @see IDialogPage#createControl(Composite)
     */
    public void createControl(final Composite parent) {
        currentComposite = null;

        if (step == 1) {
            currentComposite = new SalesforceStep1Form(parent, connectionItem, existingNames, salesforceAPI, contextModeManager);
        } else if (step == 2) {
            currentComposite = new SalesforceStep2Form(parent, connectionItem, temConnection, salesforceAPI, contextModeManager,
                    moduleName);
        } else if (step == 3) {
            MetadataTable metadataTable = RelationalFactory.eINSTANCE.createTdTable();
            currentComposite = new SalesforceStep4Form(parent, connectionItem, temConnection, metadataTable,
                    TableHelper.getTableNames(((SalesforceSchemaConnection) connectionItem.getConnection()),
                            metadataTable.getLabel()), salesforceAPI, contextModeManager, moduleName);
        }
        currentComposite.setReadOnly(!isRepositoryObjectEditable);

        AbstractForm.ICheckListener listener = new AbstractForm.ICheckListener() {

            public void checkPerformed(final AbstractForm source) {

                if (source.isStatusOnError()) {
                    SalesforceWizardPage.this.setPageComplete(false);
                    setErrorMessage(source.getStatus());
                } else {
                    SalesforceWizardPage.this.setPageComplete(isRepositoryObjectEditable);
                    setErrorMessage(null);
                    setMessage(source.getStatus());
                }
            }
        };
        currentComposite.setListener(listener);
        setControl((Composite) currentComposite);
    }

    public IDialogSettings getDialogSetting() {
        return getDialogSettings();
    }
}
