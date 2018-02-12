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

import java.util.Iterator;
import java.util.Set;

import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.IMetadataContextModeManager;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.GenericPackage;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.PackageHelper;
import org.talend.metadata.managment.ui.wizard.AbstractForm;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * DOC gldu class global comment. Detailled comment
 */
public class WebServiceSchemaWizardPage extends WSDLSchemaWizardPage {

    /**
     * DOC Administrator WebServiceSchemaWizardPage constructor comment.
     * 
     * @param pageName
     */
    public int step;

    private AbstractForm currentComposite;

    private IMetadataContextModeManager contextModeManager;

    public WebServiceSchemaWizardPage(Boolean creation, int step, ConnectionItem connectionItem,
            boolean isRepositoryObjectEditable, String[] existingNames, IMetadataContextModeManager contextModeManager) {
        super(creation, step, connectionItem, isRepositoryObjectEditable, existingNames, contextModeManager);
        // TODO Auto-generated constructor stub
        this.step = step;
        this.contextModeManager = contextModeManager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createControl(Composite parent) {
        // TODO Auto-generated method stub
        currentComposite = null;
        WSDLSchemaConnection connection = ((WSDLSchemaConnection) connectionItem.getConnection());
        if (ConnectionHelper.getTables(connection).size() < 2) {
            MetadataTable metadataTable = ConnectionHelper.getTables(connection).toArray(new MetadataTable[0])[0];
            metadataTable.setLabel("Output");
            MetadataTable inPutMetadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            inPutMetadataTable.setLabel("Input");
            inPutMetadataTable.setId(factory.getNextId());
            GenericPackage g = (GenericPackage) ConnectionHelper.getPackage(connection.getName(), connection,
                    GenericPackage.class);
            if (g != null) { // hywang
                g.getOwnedElement().add(inPutMetadataTable);
                ConnectionHelper.getTables(connection).add(inPutMetadataTable);
            } else {
                GenericPackage gpkg = ConnectionFactory.eINSTANCE.createGenericPackage();
                PackageHelper.addMetadataTable(inPutMetadataTable, gpkg);
                ConnectionHelper.addPackage(gpkg, connection);
            }
            // connection.getTables().add(inPutMetadataTable);
        }

        switch (step) {
        case 2:
            currentComposite = new WebServiceStep1Form(parent, connectionItem, null, new String[] {}, contextModeManager, this);
            break;
        case 3:
            MetadataTable metadataTable2 = null;
            Set<MetadataTable> tables = ConnectionHelper.getTables(connection);
            Iterator<MetadataTable> it = tables.iterator();
            while (it.hasNext()) {
                MetadataTable table = it.next();
                if (table.getLabel().equals("Output")) {
                    metadataTable2 = table;
                }
            }
            currentComposite = new WebServiceStep2Form(parent, connectionItem, contextModeManager, metadataTable2);
            break;
        default:
            System.out.println("error...");
        }
        currentComposite.setReadOnly(!isRepositoryObjectEditable);
        AbstractForm.ICheckListener listener = new AbstractForm.ICheckListener() {

            @Override
            public void checkPerformed(final AbstractForm source) {

                if (source.isStatusOnError()) {
                    WebServiceSchemaWizardPage.this.setPageComplete(false);
                    setErrorMessage(source.getStatus());
                } else {
                    WebServiceSchemaWizardPage.this.setPageComplete(isRepositoryObjectEditable);
                    setErrorMessage(null);
                    setMessage(source.getStatus());
                }
            }
        };
        currentComposite.setListener(listener);
        setControl(currentComposite);
    }
}
