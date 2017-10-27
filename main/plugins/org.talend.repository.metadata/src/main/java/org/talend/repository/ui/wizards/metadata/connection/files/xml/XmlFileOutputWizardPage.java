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
package org.talend.repository.ui.wizards.metadata.connection.files.xml;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.TableHelper;
import org.talend.metadata.managment.ui.wizard.AbstractForm;

/**
 * wzhang class global comment. Detailled comment
 */
public class XmlFileOutputWizardPage extends XmlFileWizardPage {

    public XmlFileOutputWizardPage(boolean creation, int step, ConnectionItem connectionItem, boolean isRepositoryObjectEditable,
            String[] existingNames) {
        super(creation, step, connectionItem, isRepositoryObjectEditable, existingNames);
    }

    @Override
    public void createControl(final Composite parent) {
        currentComposite = null;

        if (step == 1) {
            currentComposite = new XmlFileOutputStep1Form(creation, parent, connectionItem, existingNames);
        } else if (step == 2) {
            currentComposite = new XmlFileOutputStep2Form(creation, parent, connectionItem);
        } else if (step == 3) {
            MetadataTable metadataTable = ConnectionHelper.getTables(connectionItem.getConnection())
                    .toArray(new MetadataTable[0])[0];
            currentComposite = new XmlFileOutputStep3Form(parent, connectionItem, metadataTable, TableHelper.getTableNames(
                    connectionItem.getConnection(), metadataTable.getLabel()));
        }

        currentComposite.setReadOnly(!isRepositoryObjectEditable);
        currentComposite.setPage(this);

        AbstractForm.ICheckListener listener = new AbstractForm.ICheckListener() {

            @Override
            public void checkPerformed(final AbstractForm source) {

                if (source.isStatusOnError()) {
                    XmlFileOutputWizardPage.this.setPageComplete(false);
                    setErrorMessage(source.getStatus());
                } else {
                    XmlFileOutputWizardPage.this.setPageComplete(isRepositoryObjectEditable);
                    setErrorMessage(null);
                    setMessage(source.getStatus(), source.getStatusLevel());
                }
            }
        };

        currentComposite.setListener(listener);
        setControl(currentComposite);
    }

    @Override
    public IWizardPage getPreviousPage() {
        if (step == 1) {
            return null;
        }
        return super.getPreviousPage();
    }

}
