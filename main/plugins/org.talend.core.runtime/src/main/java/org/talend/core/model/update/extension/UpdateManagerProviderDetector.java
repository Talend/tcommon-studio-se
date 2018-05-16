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
package org.talend.core.model.update.extension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.talend.core.model.metadata.builder.connection.AbstractMetadataObject;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.core.model.metadata.builder.connection.SAPFunctionUnit;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.relationship.Relation;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.ISubRepositoryObject;
import org.talend.core.model.update.IUpdateItemType;
import org.talend.core.model.update.RepositoryUpdateManager;
import org.talend.core.model.update.UpdateResult;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment
 */
public enum UpdateManagerProviderDetector {
    INSTANCE;

    private UpdateManagerProviderReader reader = new UpdateManagerProviderReader();

    public IUpdateItemType[] getAllUpdateItemTypes() {
        return reader.getExtensionUpdateItemTypes().values().toArray(new IUpdateItemType[0]);
    }

    public IUpdateItemType getUpdateItemType(String name) {
        return reader.getExtensionUpdateItemTypes().get(name);
    }

    /**
     * 
     * DOC ggu Comment method "retrieveProcessUpdateResults".
     * 
     * @param process check the process to detect the different.
     * @param type
     * @return
     */
    public List<UpdateResult> retrieveProcessUpdateResults(IProcess process, IUpdateItemType type) {
        List<UpdateResult> results = new ArrayList<UpdateResult>();
        IProcessUpdateManagerProvider[] processProviders = reader.getProcessProviders();
        for (IProcessUpdateManagerProvider provider : processProviders) {
            if (provider.validate(type)) {
                List<UpdateResult> processResults = provider.retrieveUpdateResults(process);
                if (processResults != null) {
                    results.addAll(processResults);
                }
            }
        }
        return results;
    }

    /**
     * 
     * DOC ggu Comment method "getDisplayImage".
     * 
     * mainly for label provider of update manager dialog.
     * 
     * @param type
     * @return
     */
    public Object getDisplayImage(Object element, int columnIndex) {
        IProcessUpdateManagerProvider[] processProviders = reader.getProcessProviders();
        for (IProcessUpdateManagerProvider provider : processProviders) {
            Object displayImage = provider.getDisplayImage(element, columnIndex);
            if (displayImage != null) {
                return displayImage;
            }
        }
        return null;
    }

    /**
     * 
     * DOC ggu Comment method "getDisplayText".
     * 
     * mainly for label provider of update manager dialog.
     * 
     * @param element
     * @param columnIndex
     * @return
     */
    public String getDisplayText(Object element, int columnIndex) {
        IProcessUpdateManagerProvider[] processProviders = reader.getProcessProviders();
        for (IProcessUpdateManagerProvider provider : processProviders) {
            String displayText = provider.getDisplayText(element, columnIndex);
            if (displayText != null) {
                return displayText;
            }
        }
        return null;
    }

    /**
     * 
     * DOC ggu Comment method "getDisplayCategory".
     * 
     * mainly for label provider of update manager dialog.
     * 
     * @param result
     * @return
     */
    public String getDisplayCategory(UpdateResult result) {
        IProcessUpdateManagerProvider[] processProviders = reader.getProcessProviders();
        for (IProcessUpdateManagerProvider provider : processProviders) {
            String displayLabel = provider.getDisplayCategory(result);
            if (displayLabel != null) {
                return displayLabel;
            }
        }
        return null;
    }

    /**
     * 
     * DOC ggu Comment method "getResultName".
     * 
     * the label of result.
     * 
     * @param result
     * @return
     */
    public String getResultName(UpdateResult result) {
        IProcessUpdateManagerProvider[] processProviders = reader.getProcessProviders();
        for (IProcessUpdateManagerProvider provider : processProviders) {
            String displayLabel = provider.getResultName(result);
            if (displayLabel != null) {
                return displayLabel;
            }
        }
        return null;
    }

    /**
     * 
     * DOC ggu Comment method "doUpdate".
     * 
     * @param monitor
     * @param result
     * @return
     */
    public boolean doUpdate(IProgressMonitor monitor, UpdateResult result) {
        boolean updated = false;
        if (result != null) {
            IProcessUpdateManagerProvider[] processProviders = reader.getProcessProviders();
            for (IProcessUpdateManagerProvider provider : processProviders) {
                if (provider.doUpdate(monitor, result)) {
                    updated = true;
                }
            }
        }
        return updated;
    }

    /**
     * 
     * DOC ggu Comment method "needRefreshRelatedViews".
     * 
     * @param results
     * @return
     */
    public Set<String> needRefreshRelatedViews(List<UpdateResult> results) {
        Set<String> viewIds = new HashSet<String>();
        IProcessUpdateManagerProvider[] processProviders = reader.getProcessProviders();
        for (IProcessUpdateManagerProvider provider : processProviders) {
            Set<String> viewSet = provider.needRefreshRelatedViews(results);
            if (viewSet != null) {
                viewIds.addAll(viewSet);
            }
        }
        return viewIds;
    }

    /**
     * 
     * DOC ggu Comment method "postUpdate".
     * 
     * after update, will do something.
     * 
     * @param results
     */
    public void postUpdate(List<UpdateResult> results) {
        IProcessUpdateManagerProvider[] processProviders = reader.getProcessProviders();
        for (IProcessUpdateManagerProvider provider : processProviders) {
            provider.postUpdate(results);
        }
    }

    /**
     * 
     * DOC ggu Comment method "validateAction".
     * 
     * Worked for DetecteViewImpactAction
     * 
     * @param viewer
     * @param selection
     * @return
     */
    public boolean validateAction(TreeViewer viewer, IStructuredSelection selection) {
        IRepositoryUpdateManagerProvider[] repositoryProviders = reader.getRepositoryProviders();
        for (IRepositoryUpdateManagerProvider provider : repositoryProviders) {
            if (provider.validateAction(viewer, selection)) { // if one is valid, will enable this action.
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * DOC ggu Comment method "updateForRepository".
     * 
     * Worked for DetecteViewImpactAction
     * 
     * @param node
     * @return
     */
    public boolean updateForRepository(IStructuredSelection selection, boolean needConfirm) {
        boolean forcePropagation = false;
        IRepositoryUpdateManagerProvider[] repositoryProviders = reader.getRepositoryProviders();
        try {
            for (IRepositoryUpdateManagerProvider provider : repositoryProviders) {
                if (provider.needForcePropagation(selection)) {
                    forcePropagation = true;
                }
            }
            
            if (!forcePropagation && needConfirm) {
                IDesignerCoreService designerCoreService = CoreRuntimePlugin.getInstance().getDesignerCoreService();
                boolean deactive = designerCoreService != null ? Boolean.parseBoolean(designerCoreService
                        .getPreferenceStore(ITalendCorePrefConstants.DEACTIVE_REPOSITORY_UPDATE)) : true;
                if (deactive) {// disable to do update
                    return false;
                }
                // Get all relations
                List<Relation> relations = getAllRelations(selection);
                if (relations != null && relations.size() > 0) {
                    boolean propagated = RepositoryUpdateManager.openPropagationDialog();
                    if (!propagated) {
                        return false;
                    }
                } else {
                    RepositoryUpdateManager.openNoModificationDialog();
                    return false;
                }
            }

            boolean updated = false;
            for (IRepositoryUpdateManagerProvider provider : repositoryProviders) {
                // fix for the joblet ,force to check closed jobs incese joblet context changed
                provider.setEnableCheckItem(true);
                // if one upate successfully, will return true.
                if (provider.updateForRepository(selection)) {
                    updated = true;
                }
            }
            // Old Updates
            if (doOldUpdates(selection)) {
                updated = true;
            }
            if (!updated) {
                // nothing to update
                RepositoryUpdateManager.openNoModificationDialog();
            }
            return updated;
        } finally {
            for (IRepositoryUpdateManagerProvider provider : repositoryProviders) {
                // change back to default value
                provider.setEnableCheckItem(false);
            }
        }
    }

    private List<Relation> getAllRelations(IStructuredSelection selection) {
        Object firstElement = selection.getFirstElement();
        if (firstElement == null || !(firstElement instanceof RepositoryNode)) {
            return null;
        }
        RepositoryNode node = (RepositoryNode) firstElement;
        Item item = node.getObject().getProperty().getItem();
        List<Relation> allRelations = new ArrayList<Relation>();
        if (node.getObject() != null) {
            List<Relation> relations = RelationshipItemBuilder.getInstance().getItemsRelatedTo(item.getProperty().getId(),
                    RelationshipItemBuilder.LATEST_VERSION, RelationshipItemBuilder.PROPERTY_RELATION);
            if (relations != null && relations.size() > 0) {
                allRelations.addAll(relations);
            }

            if (node.getObject() instanceof ISubRepositoryObject) {
                ISubRepositoryObject subObject = (ISubRepositoryObject) node.getObject();
                AbstractMetadataObject metadataObject = subObject.getAbstractMetadataObject();
                if (metadataObject instanceof SAPFunctionUnit) {
                    List<Relation> sapFunRelations = RelationshipItemBuilder.getInstance().getItemsRelatedTo(
                            metadataObject.getId(), RelationshipItemBuilder.LATEST_VERSION,
                            RelationshipItemBuilder.PROPERTY_RELATION);
                    if (sapFunRelations != null && sapFunRelations.size() > 0) {
                        allRelations.addAll(sapFunRelations);
                    }
                }
            }
        }
        return allRelations;
    }
    
    private boolean doOldUpdates(IStructuredSelection selection) {
        Object firstElement = selection.getFirstElement();
        if (firstElement == null || !(firstElement instanceof RepositoryNode)) {
            return false;
        }
        RepositoryNode node = (RepositoryNode) firstElement;
        if (node.getObject() instanceof ISubRepositoryObject) {
            ISubRepositoryObject subObject = (ISubRepositoryObject) node.getObject();
            if (subObject != null) {
                // schema
                AbstractMetadataObject metadataObject = subObject.getAbstractMetadataObject();
                if (metadataObject instanceof MetadataTable) {
                    return RepositoryUpdateManager.updateSchema((MetadataTable) metadataObject, node, false, false);
                } else
                // query
                if (metadataObject instanceof Query) {
                    return RepositoryUpdateManager.updateQuery((Query) metadataObject, node, false, false);
                } else
                // sap function
                if (metadataObject instanceof SAPFunctionUnit) {
                    return RepositoryUpdateManager.updateSAPFunction((SAPFunctionUnit) metadataObject, false, false);
                }
            }
        } else {
            IRepositoryViewObject object = node.getObject();
            if (object != null) {
                Item item = object.getProperty().getItem();
                if (item != null) {
                    // context
                    if (item instanceof ContextItem) {
                        return RepositoryUpdateManager.updateContext((ContextItem) item, false, false);
                    } else
                    // connection
                    if (item instanceof ConnectionItem) {
                        Connection connection = ((ConnectionItem) item).getConnection();
                        if (connection instanceof DatabaseConnection) {
                            return RepositoryUpdateManager.updateDBConnection((ConnectionItem) item, false, false);
                        } else {
                            return RepositoryUpdateManager.updateFileConnection((ConnectionItem) item, false, false);
                        }
                    }

                }
            }
        }
        return false;
    }
}
