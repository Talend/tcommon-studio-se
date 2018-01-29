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
package org.talend.repository.ui.wizards.metadata.table.files;

import java.util.Observable;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.FileExcelConnection;
import org.talend.core.model.metadata.builder.connection.GenericSchemaConnection;
import org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection;
import org.talend.core.model.metadata.builder.connection.LdifFileConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.PositionalFileConnection;
import org.talend.core.model.metadata.builder.connection.RegexpFileConnection;
import org.talend.core.model.metadata.builder.connection.SalesforceSchemaConnection;
import org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.model.metadata.builder.connection.util.ConnectionSwitch;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.cwm.helper.TableHelper;
import org.talend.metadata.managment.ui.wizard.AbstractForm;
import org.talend.repository.ui.wizards.metadata.connection.files.delimited.DelimitedFileStep3Form;
import org.talend.repository.ui.wizards.metadata.connection.files.excel.ExcelFileStep3Form;
import org.talend.repository.ui.wizards.metadata.connection.files.ldif.LdifFileStep3Form;
import org.talend.repository.ui.wizards.metadata.connection.files.positional.FileStep3Form;
import org.talend.repository.ui.wizards.metadata.connection.files.regexp.RegexpFileStep3Form;
import org.talend.repository.ui.wizards.metadata.connection.files.salesforce.SalesforceModuleParseAPI;
import org.talend.repository.ui.wizards.metadata.connection.files.salesforce.SalesforceStep3Form;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.XmlFileOutputStep3Form;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.XmlFileStep3Form;
import org.talend.repository.ui.wizards.metadata.connection.genericshema.GenericSchemaStep2Form;
import org.talend.repository.ui.wizards.metadata.connection.ldap.LDAPSchemaStep4Form;
import org.talend.repository.ui.wizards.metadata.connection.wsdl.WSDLSchemaStep2Form;

import orgomg.cwm.objectmodel.core.Package;

/**
 * TableWizard present the TableForm width the MetaDataTable. Use to create a new table (need a connection to a DB).
 * Page allows setting a table.
 */
public class FileTableWizardPage extends WizardPage {

    private MetadataTable metadataTable;

    private ConnectionItem connectionItem;

    private boolean isRepositoryObjectEditable;

    private Observable schemaObservable;

    /**
     * DatabaseWizardPage constructor (to instance IMetadataConnection OR MetaDataTableType). If MetaDataTableType
     * exist, it's an update of existing metadata else it's a new metadata.
     * 
     * @param existingNames
     * 
     * @param ISelection
     */
    public FileTableWizardPage(ConnectionItem connectionItem, MetadataTable metadataTable, boolean isRepositoryObjectEditable) {
        super("wizardPage"); //$NON-NLS-1$
        this.connectionItem = connectionItem;
        this.metadataTable = metadataTable;
        this.isRepositoryObjectEditable = isRepositoryObjectEditable;
    }

    /**
     * Create the first composite, addComponentsAndControls and initialize TableWizardPage.
     * 
     * @see IDialogPage#createControl(Composite)
     */
    public void createControl(final Composite parent) {

        final AbstractForm.ICheckListener listener = new AbstractForm.ICheckListener() {

            public void checkPerformed(final AbstractForm source) {
                if (source.isStatusOnError()) {
                    FileTableWizardPage.this.setPageComplete(false);
                    setErrorMessage(source.getStatus());
                } else {
                    FileTableWizardPage.this.setPageComplete(isRepositoryObjectEditable);
                    setErrorMessage(null);
                    setMessage(source.getStatus(), source.getStatusLevel());
                }
            }
        };

        Composite theForm = null;
        Connection connection = null;
        if (metadataTable.getNamespace() != null) {
            if (metadataTable.getNamespace() instanceof Package) {
                Package pkg = (Package) metadataTable.getNamespace();
                if (!pkg.getDataManager().isEmpty()) {
                    connection = (Connection) pkg.getDataManager().get(0);
                }
            }
        }
        if (this.connectionItem.getConnection() instanceof SalesforceSchemaConnection) {
            connection = connectionItem.getConnection();
        }
        theForm = (Composite) new ConnectionSwitch() {

            @Override
            public Object caseDelimitedFileConnection(final DelimitedFileConnection object) {
                DelimitedFileStep3Form delimitedFileStep3Form = new DelimitedFileStep3Form(parent, connectionItem, metadataTable,
                        TableHelper.getTableNames(object, metadataTable.getLabel()));
                delimitedFileStep3Form.setReadOnly(!isRepositoryObjectEditable);
                delimitedFileStep3Form.setListener(listener);
                delimitedFileStep3Form.setSchemaObservable(schemaObservable);
                return delimitedFileStep3Form;
            }

            @Override
            public Object casePositionalFileConnection(final PositionalFileConnection object) {
                FileStep3Form fileStep3Form = new FileStep3Form(parent, connectionItem, metadataTable, TableHelper.getTableNames(
                        object, metadataTable.getLabel()));
                fileStep3Form.setReadOnly(!isRepositoryObjectEditable);
                fileStep3Form.setListener(listener);
                return fileStep3Form;
            }

            @Override
            public Object caseRegexpFileConnection(final RegexpFileConnection object) {
                RegexpFileStep3Form regexpFileStep3Form = new RegexpFileStep3Form(parent, connectionItem, metadataTable,
                        TableHelper.getTableNames(object, metadataTable.getLabel()));
                regexpFileStep3Form.setReadOnly(!isRepositoryObjectEditable);
                regexpFileStep3Form.setListener(listener);
                return regexpFileStep3Form;
            }

            public Object caseXmlFileConnection(final XmlFileConnection object) {
                XmlFileConnection xmlFileConnection = (XmlFileConnection) connectionItem.getConnection();
                boolean isInputModel = xmlFileConnection.isInputModel();
                if (isInputModel) {
                    XmlFileStep3Form xmlFileStep3Form = new XmlFileStep3Form(parent, connectionItem, metadataTable,
                            TableHelper.getTableNames(object, metadataTable.getLabel()));
                    xmlFileStep3Form.setReadOnly(!isRepositoryObjectEditable);
                    xmlFileStep3Form.setListener(listener);
                    return xmlFileStep3Form;
                } else {
                    XmlFileOutputStep3Form xmlFileOutputStep3Form = new XmlFileOutputStep3Form(parent, connectionItem,
                            metadataTable, TableHelper.getTableNames(object, metadataTable.getLabel()));
                    xmlFileOutputStep3Form.setReadOnly(!isRepositoryObjectEditable);
                    xmlFileOutputStep3Form.setListener(listener);
                    return xmlFileOutputStep3Form;
                }
            }

            @Override
            public Object caseLdifFileConnection(final LdifFileConnection object) {
                LdifFileStep3Form ldifFileStep3Form = new LdifFileStep3Form(parent, connectionItem, metadataTable,
                        TableHelper.getTableNames(object, metadataTable.getLabel()));
                ldifFileStep3Form.setReadOnly(!isRepositoryObjectEditable);
                ldifFileStep3Form.setListener(listener);
                return ldifFileStep3Form;
            }

            @Override
            public Object caseFileExcelConnection(final FileExcelConnection object) {
                ExcelFileStep3Form excelStep2Form = new ExcelFileStep3Form(parent, connectionItem, metadataTable,
                        TableHelper.getTableNames(object, metadataTable.getLabel()));
                excelStep2Form.setReadOnly(!isRepositoryObjectEditable);
                excelStep2Form.setListener(listener);
                return excelStep2Form;
            }

            @Override
            public Object caseGenericSchemaConnection(final GenericSchemaConnection object) {
                GenericSchemaStep2Form genericSchemaStep2Form = new GenericSchemaStep2Form(parent, connectionItem, metadataTable,
                        TableHelper.getTableNames(object, metadataTable.getLabel()));
                genericSchemaStep2Form.setReadOnly(!isRepositoryObjectEditable);
                genericSchemaStep2Form.setListener(listener);
                return genericSchemaStep2Form;
            }

            @Override
            public Object caseLDAPSchemaConnection(final LDAPSchemaConnection object) {
                LDAPSchemaStep4Form ldapSchemaStep4Form = new LDAPSchemaStep4Form(parent, connectionItem, metadataTable);
                ldapSchemaStep4Form.setReadOnly(!isRepositoryObjectEditable);
                ldapSchemaStep4Form.setListener(listener);
                return ldapSchemaStep4Form;
            }

            @Override
            public Object caseSalesforceSchemaConnection(final SalesforceSchemaConnection object) {
                SalesforceStep3Form salesforceStep3From = new SalesforceStep3Form(parent, connectionItem, metadataTable,
                        TableHelper.getTableNames(object, metadataTable.getLabel()), new SalesforceModuleParseAPI());
                salesforceStep3From.setReadOnly(!isRepositoryObjectEditable);
                salesforceStep3From.setListener(listener);
                return salesforceStep3From;
            }

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.talend.core.model.metadata.builder.connection.util.ConnectionSwitch#caseWSDLSchemaConnection(org.
             * talend.core.model.metadata.builder.connection.WSDLSchemaConnection)
             */
            @Override
            public Object caseWSDLSchemaConnection(WSDLSchemaConnection object) {
                WSDLSchemaStep2Form sadkSchemaStep2Form = new WSDLSchemaStep2Form(parent, connectionItem, metadataTable);
                sadkSchemaStep2Form.setReadOnly(!isRepositoryObjectEditable);
                sadkSchemaStep2Form.setListener(listener);
                return sadkSchemaStep2Form;
            }

        }.doSwitch(connection);
        setControl(theForm);
    }

    public void addSchemaObservable(Observable observable) {
        this.schemaObservable = observable;
    }
}
