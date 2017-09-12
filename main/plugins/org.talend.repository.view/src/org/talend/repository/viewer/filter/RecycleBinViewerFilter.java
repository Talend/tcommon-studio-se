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
package org.talend.repository.viewer.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.navigator.INavigatorContentService;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.hadoop.IHadoopClusterService;
import org.talend.core.hadoop.repository.HadoopRepositoryUtil;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IExtendedRepositoryNodeHandler;
import org.talend.core.model.repository.RepositoryContentManager;
import org.talend.core.runtime.services.IGenericDBService;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.nodes.IProjectRepositoryNode;
import org.talend.repository.viewer.ui.provider.INavigatorContentServiceProvider;

/**
 * DOC ggu class global comment. Detailled comment
 * 
 * TDI-20859, Only filter the nodes under recycle bin. do same function like perspective filter.
 */
public class RecycleBinViewerFilter extends ViewerFilter {

    /**
     * Only accept nodes if their type is matching some enabled/visible content provider type
     */
    @SuppressWarnings("rawtypes")
    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        INavigatorContentService navigatorContentService = null;
        if (viewer instanceof INavigatorContentServiceProvider) {
            navigatorContentService = ((INavigatorContentServiceProvider) viewer).getNavigatorContentService();
        }
        if (navigatorContentService != null && element instanceof RepositoryNode) {
            final RepositoryNode node = (RepositoryNode) element;

            if (isUnderRecycleBinNode(node)) { // olny process the nodes are under Recycle Bin.
                ERepositoryObjectType contextType = findRealContextType(node);
                if (contextType != null) { // don't check the SubItems, like schema, query, etc.
                    contextType = ERepositoryObjectType.METADATA_CONNECTIONS;
                    IRepositoryNode contextNode = node.getRoot().getRootRepositoryNode(contextType);
                    Set contentExtensions = navigatorContentService.findContentExtensionsByTriggerPoint(contextNode);
                    if (contentExtensions.isEmpty()) { // deactive or invisible
                        return false;
                    }
                }
            }

        }
        return true; // don't filter others
    }

    /**
     * 
     * DOC ggu Comment method "isUnderRecycleBinNode".
     * 
     * check the node is recycle bin or the deleted node is under the recycle bin.
     * 
     * @param node
     * @return
     */
    private boolean isUnderRecycleBinNode(final RepositoryNode node) {
        if (node != null) {
            if (node instanceof IProjectRepositoryNode) {
                return false;
            }
            // recycle bin
            if (node.isBin()/* ||node instanceof BinRepositoryNode. */) {
                return true;
            }
            // check parent node
            return isUnderRecycleBinNode(node.getParent());
        }
        return false;
    }

    /**
     * 
     * DOC ggu Comment method "findRealContextType".
     * 
     * Find the context type for the node.
     * 
     * If element, return element type.
     * 
     * If other, return content type.
     * 
     * Also, if the type is sub type, need find out the item type.
     * 
     * @param node
     * @return
     */
    private ERepositoryObjectType findRealContextType(final RepositoryNode node) {
        // Check hadoop subitems...
        IHadoopClusterService hadoopClusterService = HadoopRepositoryUtil.getHadoopClusterService();
        if (hadoopClusterService != null && hadoopClusterService.isHadoopSubnode(node)) {
            return hadoopClusterService.getHadoopClusterType();
        }

        for (IExtendedRepositoryNodeHandler nodeHandler : RepositoryContentManager.getExtendedNodeHandler()) {
            ERepositoryObjectType objectType = nodeHandler.getRootType(node);
            if (objectType != null) {
                return objectType;
            }
        }

        ERepositoryObjectType contentType = getNodeType(node);
        // bin is null
        if (contentType != null) {
            // SubItems
            if (contentType.isSubItem()) {
                ERepositoryObjectType itemType = null;
                if (node.getObject() != null) {
                    // get property of SubItem.
                    Property property = node.getObject().getProperty();
                    if (property != null) {
                        try {
                            itemType = ERepositoryObjectType.getItemType(property.getItem());
                        } catch (IllegalStateException e) { // can't find the item type.
                            // nothing to do
                        }
                    }
                }
                contentType = itemType;
            }
        }

        return contentType;
    }
    
    private ERepositoryObjectType getNodeType(final RepositoryNode node){
        List<ERepositoryObjectType> extraTypes = new ArrayList<ERepositoryObjectType>();
        IGenericDBService dbService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IGenericDBService.class)) {
            dbService = (IGenericDBService) GlobalServiceRegister.getDefault().getService(
                    IGenericDBService.class);
        }
        if(dbService != null){
            extraTypes.addAll(dbService.getExtraTypes());
        }
        ERepositoryObjectType contentType = node.getObjectType();
        if(contentType != null && extraTypes.contains(contentType)){
            return node.getContentType();
        }
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            contentType = node.getObjectType();
        }
        if (contentType == null) {
            contentType = node.getContentType();
        }
        return contentType;
    }
}
