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
package org.talend.core.repository.ui.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.model.metadata.builder.connection.AbstractMetadataObject;
import org.talend.core.model.metadata.builder.connection.CDCConnection;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.SubscriberTable;
import org.talend.core.model.metadata.builder.connection.impl.SalesforceModuleUnitImpl;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.SalesforceSchemaConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.ISubRepositoryObject;
import org.talend.core.model.repository.RepositoryManager;
import org.talend.core.repository.i18n.Messages;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.model.repositoryObject.MetadataTableRepositoryObject;
import org.talend.core.repository.utils.AbstractResourceChangesService;
import org.talend.core.repository.utils.TDQServiceRegister;
import org.talend.cwm.helper.SubItemHelper;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.actions.AContextualAction;

/**
 * DOC tguiu class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class DeleteTableAction extends AContextualAction {

    private static final String DELETE_LOGICAL_TITLE = Messages.getString("DeleteAction.action.logicalTitle"); //$NON-NLS-1$

    private static final String DELETE_FOREVER_TITLE = Messages.getString("DeleteAction.action.foreverTitle"); //$NON-NLS-1$

    private static final String DELETE_LOGICAL_TOOLTIP = Messages.getString("DeleteAction.action.logicalToolTipText"); //$NON-NLS-1$

    private static final String DELETE_FOREVER_TOOLTIP = Messages.getString("DeleteAction.action.logicalToolTipText"); //$NON-NLS-1$

    private List<Object> objectsNeedToBeDeleted;

    /**
     * will delete all the user selected objects on UI <br>
     */
    public DeleteTableAction() {
        this(null);
    }

    /**
     * only delete the caller's specified objects, in case the caller already obtains all the selected objects and also
     * wants to delete them
     * 
     * @param objsNeedToBeDeleted
     */
    public DeleteTableAction(List<Object> objsNeedToBeDeleted) {
        super();
        this.objectsNeedToBeDeleted = objsNeedToBeDeleted;
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.DELETE_ICON));
    }

    public List<Object> getObjectsNeedToBeDeleted() {
        return this.objectsNeedToBeDeleted;
    }

    public void setObjectsNeedToBeDeleted(List<Object> objectsNeedToBeDeleted) {
        this.objectsNeedToBeDeleted = objectsNeedToBeDeleted;
    }

    @Override
    protected void doRun() {
        if (objectsNeedToBeDeleted == null || objectsNeedToBeDeleted.isEmpty()) {
            ISelection selection = getSelection();
            if (!selection.isEmpty()) {
                objectsNeedToBeDeleted = Arrays.asList(((IStructuredSelection) selection).toArray());
            }
        }
        if (objectsNeedToBeDeleted == null || objectsNeedToBeDeleted.isEmpty()) {
            return;
        }
        Boolean confirm = null;

        // used to store the database connection object that are used to notify the sqlBuilder.
        final List<IRepositoryViewObject> connections = new ArrayList<IRepositoryViewObject>();
        final Set<ERepositoryObjectType> types = new HashSet<ERepositoryObjectType>();
        Map<String, Item> procItems = new HashMap<String, Item>();

        // need to clear the objectsNeedToBeDeleted in this execution, in case this delete action is a singleton
        List<Object> objsNeedToBeDeleted = objectsNeedToBeDeleted;
        objectsNeedToBeDeleted = null;

        for (Object obj : objsNeedToBeDeleted) {
            if (obj instanceof RepositoryNode) {
                RepositoryNode node = (RepositoryNode) obj;
                ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
                if (node.getType() == ENodeType.REPOSITORY_ELEMENT && nodeType.isSubItem()) {
                    IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

                    IRepositoryViewObject nodeObject = node.getObject();

                    boolean locked = false;

                    if (!factory.getRepositoryContext().isEditableAsReadOnly()) {
                        if (nodeObject.getRepositoryStatus() == ERepositoryStatus.LOCK_BY_OTHER
                                || nodeObject.getRepositoryStatus() == ERepositoryStatus.LOCK_BY_USER) {
                            locked = true;
                        }
                    }
                    // Avoid to delete node which is locked.
                    if ((locked || RepositoryManager.isOpenedItemInEditor(nodeObject))
                            && !(DELETE_FOREVER_TITLE.equals(getText()))) {

                        final String title = Messages.getString("DeleteAction.error.title"); //$NON-NLS-1$
                        String nodeName = nodeObject.getRepositoryObjectType().getLabel();
                        final String message = Messages.getString("DeleteAction.error.lockedOrOpenedObject.newMessage", nodeName);//$NON-NLS-1$
                        Display.getDefault().syncExec(new Runnable() {

                            @Override
                            public void run() {
                                MessageDialog dialog = new MessageDialog(new Shell(), title, null, message, MessageDialog.ERROR,
                                        new String[] { IDialogConstants.OK_LABEL }, 0);
                                dialog.open();
                            }
                        });
                        return;
                    }

                    Connection connection = null;
                    ERepositoryObjectType parentNodeType = (ERepositoryObjectType) node.getParent().getProperties(
                            EProperties.CONTENT_TYPE);
                    if (parentNodeType == null) {
                        parentNodeType = node.getParent().getParent().getObjectType(); // for db connection
                    }
                    if (parentNodeType != null) {
                        types.add(parentNodeType);
                    }
                    ConnectionItem item = (ConnectionItem) node.getObject().getProperty().getItem();
                    connection = (item).getConnection();
                    ISubRepositoryObject subRepositoryObject = (ISubRepositoryObject) node.getObject();
                    // this one is the old metadataObject
                    AbstractMetadataObject abstractMetadataObject = subRepositoryObject.getAbstractMetadataObject();
                    if (abstractMetadataObject instanceof SubscriberTable) {
                        return;
                    }

                    // for (Object table : connection.getTables()) {
                    // if (table instanceof AbstractMetadataObject) {
                    // AbstractMetadataObject metadataTable = (AbstractMetadataObject) table;
                    // if (metadataTable.getLabel() != null
                    // && metadataTable.getLabel().equals(abstractMetadataObject.getLabel())) {
                    // abstractMetadataObject = metadataTable;
                    // }
                    // }
                    // }
                    if (abstractMetadataObject == null) {
                        return;
                    }
                    boolean isSave = true;
                    if (SubItemHelper.isDeleted(abstractMetadataObject)) {
                        AbstractResourceChangesService resChangeService = TDQServiceRegister.getInstance()
                                .getResourceChangeService(AbstractResourceChangesService.class);
                        if (resChangeService != null) {
                            List<IRepositoryNode> dependentNodes = resChangeService.getDependentNodes(node);
                            if (dependentNodes != null && !dependentNodes.isEmpty()) {
                                resChangeService.openDependcesDialog(dependentNodes);
                                isSave = false;
                            }
                        }
                    }
                    if (isSave) {
                        //
                        String sfm = null;
                        String sf = null;
                        EObject eContainer = abstractMetadataObject.eContainer();
                        if (eContainer != null && eContainer instanceof SalesforceModuleUnitImpl) {
                            sfm = ((SalesforceModuleUnitImpl) eContainer).getModuleName();
                            sf = abstractMetadataObject.getLabel();
                        }
                        if (SubItemHelper.isDeleted(abstractMetadataObject) && isSave) {
                            if (confirm == null) {
                                String title = Messages.getString("DeleteAction.dialog.title"); //$NON-NLS-1$
                                String message = Messages.getString("DeleteAction.dialog.message1") + "\n" //$NON-NLS-1$ //$NON-NLS-2$
                                        + Messages.getString("DeleteAction.dialog.message2"); //$NON-NLS-1$
                                confirm = (MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), title, message));
                            }
                            if (confirm) {
                                subRepositoryObject.removeFromParent();
                                ProxyRepositoryFactory.getInstance().setSubItemDeleted(item, abstractMetadataObject, false);
                            }
                        }
                        // bug 20963
                        else if (item instanceof SalesforceSchemaConnectionItem && parentNodeType.getType() != null
                                && parentNodeType.getType().equals("METADATA_SALESFORCE_MODULE") && sfm != null && sf != null
                                && sfm.equals(sf)) {
                            // Nothing to do
                        } else {
                            ProxyRepositoryFactory.getInstance().setSubItemDeleted(item, abstractMetadataObject, true);
                        }
                        final String id = item.getProperty().getId();
                        Item tmpItem = procItems.get(id);
                        if (tmpItem == null) {
                            procItems.put(id, item);
                        }
                        connections.add(node.getObject());
                    }
                }
            }
        }
        try {
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            for (String id : procItems.keySet()) {
                Item item = procItems.get(id);
                factory.save(item);
            }
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }

        notifySQLBuilder(connections);
        // IViewPart viewPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(
        // RepositoryView.VIEW_ID);
        // IRepositoryView repositoryView = (IRepositoryView) viewPart;

        // // Find Metadata node
        // RepositoryNode recycleBinNode = repositoryView.getRoot().getChildren().get(8);
        //
        // // Force focus to the repository View ans erase the current user selection
        // viewPart.setFocus();
        // repositoryView.getViewSite().getSelectionProvider().setSelection(null);
        // repositoryView.expand(recycleBinNode, true);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.ITreeContextualAction#init(org.eclipse.jface.viewers.TreeViewer,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    @Override
    public void init(TreeViewer viewer, IStructuredSelection selection) {
        boolean canWork = false;
        setText(null);
        for (Object o : (selection).toArray()) {
            RepositoryNode node = (RepositoryNode) o;
            switch (node.getType()) {
            case STABLE_SYSTEM_FOLDER:
            case SYSTEM_FOLDER:
            case SIMPLE_FOLDER:
                canWork = false;
                break;
            case REPOSITORY_ELEMENT:
                IRepositoryViewObject repObj = node.getObject();

                ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
                if (!nodeType.isSubItem()) {
                    canWork = false;
                    break;
                }
                if (node.getObjectType() == ERepositoryObjectType.METADATA_CON_TABLE) {
                    canWork = true;
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
                                    boolean is = RepositoryConstants.TABLE.equals(tableType);
                                    canWork = is && !table.isAttachedCDC();
                                }
                            }
                        } else if (item2 instanceof SalesforceSchemaConnectionItem) {
                            IRepositoryViewObject parent = node.getParent().getObject();
                            if (parent != null && parent.getLabel().equals(repositoryObject.getLabel())) {
                                canWork = false;
                            }
                        }
                    }
                } else if (node.getObjectType() == ERepositoryObjectType.METADATA_CON_CDC) {
                    canWork = false;
                } else if (node.getObjectType() == ERepositoryObjectType.METADATA_CON_QUERY) {
                    canWork = true;
                }

                if (!canWork) {
                    break;
                }
                IProxyRepositoryFactory repFactory = ProxyRepositoryFactory.getInstance();
                boolean isLocked = false;
                boolean isDeleted = false;
                isLocked = !repFactory.isPotentiallyEditable(repObj);
                isDeleted = (repFactory.getStatus(node.getObject()) == ERepositoryStatus.DELETED);
                if (isLocked) {
                    canWork = false;
                } else if (isDeleted) {
                    if (getText() == null || DELETE_FOREVER_TITLE.equals(getText())) {
                        this.setText(DELETE_FOREVER_TITLE);
                        this.setToolTipText(DELETE_FOREVER_TOOLTIP);
                    } else {
                        canWork = false;
                    }
                } else {
                    setText(DELETE_LOGICAL_TITLE);
                    setToolTipText(DELETE_LOGICAL_TOOLTIP);
                }
                break;
            default:
                // Nothing to do
                break;
            }
            if (!canWork) {
                break;
            }
        }
        setEnabled(canWork);
    }

}
