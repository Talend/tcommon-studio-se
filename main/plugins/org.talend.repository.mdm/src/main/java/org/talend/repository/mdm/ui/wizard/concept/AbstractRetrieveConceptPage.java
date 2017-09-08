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
package org.talend.repository.mdm.ui.wizard.concept;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.MetadataToolHelper;
import org.talend.core.model.metadata.builder.connection.Concept;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.repository.model.repositoryObject.MetadataTableRepositoryObject;
import org.talend.repository.mdm.i18n.Messages;
import org.talend.repository.mdm.util.MDMUtil;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC talend class global comment. Detailled comment
 */
public abstract class AbstractRetrieveConceptPage extends WizardPage {

    private RepositoryNode node;

    protected ConnectionItem connectionItem;

    protected Concept concept;

    protected boolean creation;

    protected MetadataTable metadataTable;

    /**
     * DOC talend AbstractRetrieveConceptPage constructor comment.
     * 
     * @param pageName
     */
    protected AbstractRetrieveConceptPage(RepositoryNode node, ConnectionItem connectionItem, MetadataTable metadataTable,
            boolean creation) {
        super(Messages.getString("AbstractRetrieveConceptPage_wizard_page")); //$NON-NLS-1$
        this.node = node;
        this.connectionItem = connectionItem;
        this.metadataTable = metadataTable;
        this.creation = creation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
        // TODO Auto-generated method stub

    }

    protected MDMConnection getConnection() {
        return (MDMConnection) connectionItem.getConnection();
    }

    protected Concept getConcept() {
        if (getConnection().getSchemas().size() > 0) {
            if (creation) {
                concept = (Concept) getConnection().getSchemas().get(getConnection().getSchemas().size() - 1);
            } else {
                concept = MDMUtil.getConcept(getConnection(), metadataTable);
            }
        } else if (node.getObject() instanceof MetadataTableRepositoryObject) {
            MetadataTable table = (MetadataTable) ((MetadataTableRepositoryObject) node.getObject()).getTable();
            concept = MDMUtil.getConcept(getConnection(), MetadataToolHelper.getMetadataTableFromRepository(table.getId()));
        }

        return concept;
    }
}
