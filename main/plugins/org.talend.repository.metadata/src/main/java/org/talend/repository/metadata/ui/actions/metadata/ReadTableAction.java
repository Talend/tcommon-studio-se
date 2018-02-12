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
package org.talend.repository.metadata.ui.actions.metadata;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.model.metadata.builder.connection.CDCConnection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.BRMSConnectionItem;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.EDIFACTConnectionItem;
import org.talend.core.model.properties.EbcdicConnectionItem;
import org.talend.core.model.properties.HL7ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.MDMConnectionItem;
import org.talend.core.model.properties.SAPConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.core.repository.model.repositoryObject.MetadataTableRepositoryObject;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * DOC smallet class global comment. Detailed comment <br/>
 * 
 * $Id$
 * 
 */
public class ReadTableAction extends AbstractCreateTableAction {

    protected static final String LABEL = Messages.getString("CreateTableAction.action.readTitle"); //$NON-NLS-1$

    public ReadTableAction() {
        super();
        this.setText(LABEL);
        this.setToolTipText(LABEL);
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.READ_ICON));
    }

    @Override
    protected void init(RepositoryNode node) {
        setEnabled(false);
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        if (ENodeType.REPOSITORY_ELEMENT.equals(node.getType())) {
            if (node.getObject().getRepositoryStatus() == ERepositoryStatus.DELETED) {
                return;
            }
            ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            if (ERepositoryObjectType.METADATA_CON_TABLE.equals(nodeType)) {
                IRepositoryViewObject repositoryObject = node.getObject();
                if (repositoryObject != null) {
                    Item item2 = repositoryObject.getProperty().getItem();
                    if (item2 instanceof DatabaseConnectionItem) {
                        DatabaseConnectionItem item = (DatabaseConnectionItem) repositoryObject.getProperty().getItem();
                        DatabaseConnection connection = (DatabaseConnection) item.getConnection();
                        CDCConnection cdcConns = connection.getCdcConns();
                        if (cdcConns != null) {
                            if (repositoryObject instanceof MetadataTableRepositoryObject) {
                                MetadataTable table = ((MetadataTableRepositoryObject) repositoryObject).getTable();
                                String tableType = table.getTableType();
                                setEnabled(RepositoryConstants.TABLE.equals(tableType));
                                return;
                            }
                        }
                    }
                    if (item2 instanceof SAPConnectionItem) {
                        setEnabled(false);
                        return;
                    }

                    if (item2 instanceof HL7ConnectionItem) {
                        setEnabled(false);
                        return;
                    }
                    if (item2 instanceof BRMSConnectionItem) {
                        setEnabled(false);
                        return;
                    }
                    if (item2 instanceof EbcdicConnectionItem) {
                        setEnabled(false);
                        return;
                    }

                    if (item2 instanceof MDMConnectionItem) {
                        setEnabled(false);
                        return;
                    }

                    if (item2 instanceof EDIFACTConnectionItem) {
                        setEnabled(false);
                        return;
                    }
                }

                setEnabled(true);
                return;
            }

        }
    }

    @Override
    protected void doRun() {
        // RepositoryNode metadataNode = getViewPart().getRoot().getChildren().get(6);
        RepositoryNode metadataNode = getMetadataNode(getCurrentRepositoryNode());
        // Force focus to the repositoryView and open Metadata and DbConnection nodes
        IRepositoryView viewPart = getViewPart();
        if (viewPart != null) {
            viewPart.setFocus();
            viewPart.expand(metadataNode, true);
        }

        IStructuredSelection selection = (IStructuredSelection) getSelection();
        RepositoryNode node = (RepositoryNode) selection.getFirstElement();

        // Init the content of the Wizard
        // init(node);

        ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
        
        if (ERepositoryObjectType.METADATA_CON_COLUMN.equals(nodeType)) {
            IRepositoryViewObject obj = node.getObject();
            if (obj instanceof MetadataColumnRepositoryObject) {
                node = node.getParent().getParent();
                nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            }
        } 

        if (ERepositoryObjectType.METADATA_CON_TABLE.equals(nodeType)) {
            ConnectionItem connectionItem = (ConnectionItem) node.getObject().getProperty().getItem();
            nodeType = ERepositoryObjectType.getItemType(connectionItem);
        }

        if (ERepositoryObjectType.METADATA_FILE_POSITIONAL.equals(nodeType)) {
            createFilePositionalTableWizard(node, true);
        } else if (ERepositoryObjectType.METADATA_FILE_DELIMITED.equals(nodeType)) {
            createFileDelimitedTableWizard(node, true);
        } else if (ERepositoryObjectType.METADATA_CONNECTIONS.equals(nodeType)) {
            createDatabaseTableWizard(node, true);
        } else if (ERepositoryObjectType.METADATA_FILE_REGEXP.equals(nodeType)) {
            createFileRegexpTableWizard(node, true);
        } else if (ERepositoryObjectType.METADATA_FILE_XML.equals(nodeType)) {
            createFileXmlTableWizard(node, true);
        } else if (ERepositoryObjectType.METADATA_FILE_EXCEL.equals(nodeType)) {
            createFileExcelTableWizard(node, true);
        } else if (ERepositoryObjectType.METADATA_FILE_LDIF.equals(nodeType)) {
            createFileLdifTableWizard(node, true);
        } else if (ERepositoryObjectType.METADATA_GENERIC_SCHEMA.equals(nodeType)) {
            createGenericSchemaWizard(node, true);
        } else if (ERepositoryObjectType.METADATA_LDAP_SCHEMA.equals(nodeType)) {
            createLDAPSchemaWizard(node, true);
        } else if (ERepositoryObjectType.METADATA_SALESFORCE_SCHEMA.equals(nodeType)) {
            createSalesforceSchemaWizard(node, true);
        } else { // handle the schemas of extensive nodes.
            createExtenseNodeSchemaWizard(nodeType, node, true);
        }
    }
}
