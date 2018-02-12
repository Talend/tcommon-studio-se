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
package org.talend.repository.mdm.action;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.model.metadata.builder.connection.Concept;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.MDMConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.xml.XmlArray;
import org.talend.core.repository.model.ProjectRepositoryNode;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.ui.actions.metadata.AbstractCreateAction;
import org.talend.cwm.helper.TableHelper;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;
import org.talend.repository.ProjectManager;
import org.talend.repository.mdm.i18n.Messages;
import org.talend.repository.mdm.ui.wizard.concept.CreateConceptWizard;
import org.talend.repository.mdm.util.MDMUtil;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * DOC hwang class global comment. Detailled comment
 */
public class RetrieveMDMSchemaAction extends AbstractCreateAction {

    protected static final int WIZARD_WIDTH = 800;

    protected static final int WIZARD_HEIGHT = 495;

    protected static final String CREATE_LABEL = Messages.getString("RetrieveMDMSchemaAction_retrive_entity"); //$NON-NLS-1$

    protected static final String EDIT_LABEL = Messages.getString("RetrieveMDMSchemaAction_edit_entity"); //$NON-NLS-1$

    private RepositoryNode node;

    protected boolean readOnly = false;

    public RetrieveMDMSchemaAction() {
        super();

        this.setText(CREATE_LABEL);
        this.setToolTipText(CREATE_LABEL);
        this.setImageDescriptor(ImageProvider.getImageDesc(ECoreImage.METADATA_TABLE_ICON));
    }

    public RetrieveMDMSchemaAction(RepositoryNode node) {
        this();
        this.node = node;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.ui.actions.metadata.AbstractCreateAction#init(org.talend.repository.model.RepositoryNode)
     */
    @Override
    protected void init(RepositoryNode node) {
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        if (factory.isUserReadOnlyOnCurrentProject() || !ProjectManager.getInstance().isInCurrentMainProject(node)) {
            setEnabled(false);
        } else {
            if (ENodeType.REPOSITORY_ELEMENT.equals(node.getType())) {
                ERepositoryStatus status = node.getObject().getRepositoryStatus();
                if (status == ERepositoryStatus.DELETED || status == ERepositoryStatus.LOCK_BY_OTHER) {
                    setEnabled(false);
                    return;
                }

                ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
                if (ERepositoryObjectType.METADATA_CON_TABLE.equals(nodeType)) {
                    setText(EDIT_LABEL);
                    if (node.getObject() != null) {
                        Item item = node.getObject().getProperty().getItem();
                        if (item instanceof MDMConnectionItem) {
                            collectSiblingNames(node);
                            setEnabled(true);
                            return;
                        }
                    }
                } else if (ERepositoryObjectType.METADATA_CON_COLUMN.equals(nodeType)) {
                    setText(EDIT_LABEL);
                    if (node.getObject() != null) {
                        Item item = node.getObject().getProperty().getItem();
                        if (item instanceof MDMConnectionItem) {
                            collectSiblingNames(node);
                            setEnabled(true);
                            return;
                        }
                    }
                }

                if (ERepositoryObjectType.METADATA_MDMCONNECTION.equals(nodeType)) {
                    setText(CREATE_LABEL);
                    collectChildNames(node);
                    setEnabled(true);
                    return;
                }
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.AContextualAction#doRun()
     */
    @Override
    protected void doRun() {
        node = getCurrentRepositoryNode();
        // Force focus to the repositoryView and open Metadata and DbConnection nodes
        IRepositoryView viewPart = getViewPart();
        if (viewPart != null) {
            viewPart.setFocus();
            viewPart.expand(node.getParent(), true);
        }

        // Init the content of the Wizard
        init(node);

        ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);

        if (ERepositoryObjectType.METADATA_CON_TABLE.equals(nodeType)) {
            final IRepositoryViewObject object = node.getObject();

            ConnectionItem connectionItem = (ConnectionItem) object.getProperty().getItem();
            nodeType = ERepositoryObjectType.getItemType(connectionItem);

        } else if (ERepositoryObjectType.METADATA_CON_COLUMN.equals(nodeType)) {
            this.node = node.getParent().getParent();
            final IRepositoryViewObject object = node.getObject();

            ConnectionItem connectionItem = (ConnectionItem) object.getProperty().getItem();
            nodeType = ERepositoryObjectType.getItemType(connectionItem);
        }
        createMDMTableWizard(node, readOnly);

    }

    protected void createMDMTableWizard(final RepositoryNode node, final boolean forceReadOnly) {

        // Define the repositoryObject
        MDMConnection connection = null;
        MetadataTable metadataTable = null;
        Concept concept = null;

        boolean creation = false;
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            String metadataTableLabel = (String) node.getProperties(EProperties.LABEL);

            MDMConnectionItem item = null;
            if (nodeType == ERepositoryObjectType.METADATA_CON_TABLE || nodeType == ERepositoryObjectType.METADATA_CON_COLUMN) {
                item = (MDMConnectionItem) node.getObject().getProperty().getItem();
                connection = (MDMConnection) item.getConnection();
                metadataTable = TableHelper.findByLabel(connection, metadataTableLabel);
                concept = MDMUtil.getConcept(connection, metadataTable);
                creation = false;
            } else if (nodeType == ERepositoryObjectType.METADATA_MDMCONNECTION) {
                item = (MDMConnectionItem) node.getObject().getProperty().getItem();
                connection = (MDMConnection) item.getConnection();
                creation = true;

                concept = ConnectionFactory.eINSTANCE.createConcept();
                connection.getSchemas().add(concept);
                if (concept.getLoopLimit() == null) {
                    concept.setLoopLimit(-1);
                    XmlArray.setLimitToDefault();
                    concept.setLoopLimit(XmlArray.getRowLimit());

                }
            }
            ConnectionContextHelper.checkContextMode(item);

            openMDMTableWizard(item, metadataTable, forceReadOnly, node, creation);
        }
    }

    private void openMDMTableWizard(final MDMConnectionItem item, final MetadataTable metadataTable, final boolean forceReadOnly,
            final RepositoryNode node, final boolean creation) {
        boolean skipStep = !creation;
        CreateConceptWizard conceptWizard = new CreateConceptWizard(PlatformUI.getWorkbench(), creation, getExistingNames(),
                node, item, metadataTable, readOnly);
        WizardDialog wizardDialog = new WizardDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), conceptWizard);
        wizardDialog.setBlockOnOpen(true);
        wizardDialog.setPageSize(WIZARD_WIDTH, WIZARD_HEIGHT);
        wizardDialog.create();
        if (wizardDialog.open() == wizardDialog.OK) {
            IRepositoryView viewPart = getViewPart();
            if (viewPart != null) {
                viewPart.expand(node, true);
            }
            refresh(node);
        } else {
            IRepositoryView viewPart = getViewPart();
            if (viewPart != null) {
                RepositoryNode rootNode = ProjectRepositoryNode.getInstance().getRootRepositoryNode(node, false);
                if (rootNode != null) {
                    rootNode.getChildren().clear();
                    rootNode.setInitialized(false);
                    viewPart.refresh(rootNode);
                }
            }
        }
        // node = null;
    }

    @Override
    public Class getClassForDoubleClick() {
        return MDMConnection.class;
    }

}
