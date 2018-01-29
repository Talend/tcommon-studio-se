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
package org.talend.repository.ui.wizards.metadata.connection.files.excel;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.IMetadataContextModeManager;
import org.talend.core.model.metadata.builder.connection.FileExcelConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.repository.model.preview.ExcelSchemaBean;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.TableHelper;
import org.talend.metadata.managment.ui.wizard.AbstractForm;
import org.talend.repository.metadata.ui.wizards.form.AbstractExcelFileStepForm;

/**
 * DOC yexiaowei class global comment. Detailled comment
 */
public class ExcelFileWizardPage extends WizardPage {

    private ConnectionItem connectionItem;

    private int step;

    private AbstractExcelFileStepForm currentComposite;

    private final String[] existingNames;

    private boolean isRepositoryObjectEditable;

    private IMetadataContextModeManager contextModeManager;

    private ExcelSchemaBean bean;

    /**
     * DOC ocarbone LdifFileWizardPage constructor comment.
     * 
     * @param step
     * @param connection
     * @param isRepositoryObjectEditable
     * @param existingNames
     */
    public ExcelFileWizardPage(int step, ConnectionItem connectionItem, boolean isRepositoryObjectEditable,
            String[] existingNames, IMetadataContextModeManager contextModeManager, ExcelSchemaBean bean) {
        super("wizardPage"); //$NON-NLS-1$
        this.step = step;
        this.connectionItem = connectionItem;
        this.existingNames = existingNames;
        this.isRepositoryObjectEditable = isRepositoryObjectEditable;
        this.contextModeManager = contextModeManager;
        this.bean = bean;
    }

    /**
     * 
     * @see IDialogPage#createControl(Composite)
     */
    public void createControl(final Composite parent) {
        currentComposite = null;

        if (step == 1) {
            currentComposite = new ExcelFileStep1Form(parent, connectionItem, existingNames, contextModeManager, bean);
        } else if (step == 2) {
            currentComposite = new ExcelFileStep2Form(parent, connectionItem, contextModeManager, bean);
        } else if (step == 3) {
            MetadataTable metadataTable = ConnectionHelper.getTables((FileExcelConnection) connectionItem.getConnection())
                    .toArray(new MetadataTable[0])[0]; // hywang
            currentComposite = new ExcelFileStep3Form(parent, connectionItem, metadataTable, TableHelper.getTableNames(
                    ((FileExcelConnection) connectionItem.getConnection()), metadataTable.getLabel()), contextModeManager, bean);
        }

        currentComposite.setReadOnly(!isRepositoryObjectEditable);

        AbstractForm.ICheckListener listener = new AbstractForm.ICheckListener() {

            public void checkPerformed(final AbstractForm source) {

                if (source.isStatusOnError()) {
                    ExcelFileWizardPage.this.setPageComplete(false);
                    setErrorMessage(source.getStatus());
                } else {
                    ExcelFileWizardPage.this.setPageComplete(isRepositoryObjectEditable);
                    setErrorMessage(null);
                    setMessage(source.getStatus());
                }
            }
        };

        currentComposite.setListener(listener);
        setControl((Composite) currentComposite);
    }
}
