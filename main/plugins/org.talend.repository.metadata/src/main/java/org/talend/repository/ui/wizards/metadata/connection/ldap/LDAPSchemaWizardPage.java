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
package org.talend.repository.ui.wizards.metadata.connection.ldap;

import java.util.Set;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.IMetadataContextModeManager;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.GenericPackage;
import org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.PackageHelper;
import org.talend.cwm.helper.TableHelper;
import org.talend.metadata.managment.ui.wizard.AbstractForm;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * The class is used for LDAP schema on Repository View. <br/>
 * 
 * @author ftang, 18/09/2007
 * 
 */
public class LDAPSchemaWizardPage extends WizardPage {

    private ConnectionItem connectionItem;

    private int step;

    private AbstractForm currentComposite;

    private final String[] existingNames;

    private boolean isRepositoryObjectEditable;

    private MetadataTable metadataTable;

    private IMetadataContextModeManager contextModeManager;

    /**
     * DelimitedFileWizardPage constructor comment.
     * 
     * @param step
     * @param connection
     * @param isRepositoryObjectEditable
     * @param existingNames
     */
    public LDAPSchemaWizardPage(int step, ConnectionItem connectionItem, boolean isRepositoryObjectEditable,
            String[] existingNames, IMetadataContextModeManager contextModeManager) {
        super("wizardPage"); //$NON-NLS-1$
        this.step = step;
        this.connectionItem = connectionItem;
        this.existingNames = existingNames;
        this.isRepositoryObjectEditable = isRepositoryObjectEditable;
        this.contextModeManager = contextModeManager;
    }

    /**
     * 
     * @see IDialogPage#createControl(Composite)
     */
    public void createControl(final Composite parent) {
        currentComposite = null;
        Connection connection = connectionItem.getConnection();
        LDAPSchemaConnection ldapConnection = (LDAPSchemaConnection) connection;
        Set tables = ConnectionHelper.getTables(ldapConnection);
        switch (step) {
        case 1:
            // add for bug 7914
            if (tables.size() == 0) {
                metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
                IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
                metadataTable.setId(factory.getNextId());
                GenericPackage g = (GenericPackage) ConnectionHelper.getPackage(connection.getName(), (Connection) connection,
                        GenericPackage.class);
                if (g != null) { // hywang
                    g.getOwnedElement().add(metadataTable);
                } else {
                    GenericPackage gpkg = ConnectionFactory.eINSTANCE.createGenericPackage();
                    PackageHelper.addMetadataTable(metadataTable, gpkg);
                    ConnectionHelper.addPackage(gpkg, connection);

                }
            } else {
                metadataTable = (MetadataTable) tables.toArray(new MetadataTable[0])[0];
            }

            currentComposite = new LDAPSchemaStep1Form(parent, connectionItem, metadataTable, TableHelper.getTableNames(
                    (ldapConnection), metadataTable.getLabel()), contextModeManager);
            break;

        case 2:
            metadataTable = (MetadataTable) tables.toArray(new MetadataTable[0])[0];
            currentComposite = new LDAPSchemaStep2Form(parent, connectionItem, metadataTable, TableHelper.getTableNames(
                    (ldapConnection), metadataTable.getLabel()), contextModeManager);
            break;
        case 3:
            metadataTable = (MetadataTable) tables.toArray(new MetadataTable[0])[0];
            currentComposite = new LDAPSchemaStep3Form(parent, connectionItem, contextModeManager);
            break;
        case 4:
            metadataTable = (MetadataTable) tables.toArray(new MetadataTable[0])[0];
            currentComposite = new LDAPSchemaStep4Form(parent, connectionItem, contextModeManager);
            break;
        default:
            System.out.println("error..."); //$NON-NLS-1$
        }

        currentComposite.setReadOnly(!isRepositoryObjectEditable);

        AbstractForm.ICheckListener listener = new AbstractForm.ICheckListener() {

            public void checkPerformed(final AbstractForm source) {

                if (source.isStatusOnError()) {
                    LDAPSchemaWizardPage.this.setPageComplete(false);
                    setErrorMessage(source.getStatus());
                } else {
                    LDAPSchemaWizardPage.this.setPageComplete(isRepositoryObjectEditable);
                    setErrorMessage(null);
                    setMessage(source.getStatus());
                }
            }
        };

        currentComposite.setListener(listener);
        setControl((Composite) currentComposite);
    }
}
