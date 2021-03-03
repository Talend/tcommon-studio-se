// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.repository.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryObject;
import org.talend.core.repository.model.provider.ICheckDeleteItemReference;
import org.talend.core.repository.ui.actions.DeleteActionCache;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public abstract class AbstractCheckDeleteItemReference implements ICheckDeleteItemReference {

    protected IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

    protected List<? extends IRepositoryNode> deleteNodes;

    protected List<IRepositoryObject> deleteRepositoryObjects;

    @Override
	public Set<ItemReferenceBean> getItemReferenceBeans(List<? extends IRepositoryNode> deleteNodes,
            DeleteActionCache deleteActionCache) {
        this.deleteNodes = deleteNodes;
        setDeleteRepositoryObjects(deleteNodes);
        Set<ItemReferenceBean> refBeans = new HashSet<ItemReferenceBean>();

        if (deleteActionCache == null) {
            deleteActionCache = DeleteActionCache.getInstance();
            deleteActionCache.createRecords();
        }

        for (IRepositoryNode repositoryNode : deleteNodes) {
        	IRepositoryViewObject repoObject = repositoryNode.getObject();
        	if(repoObject!=null) {
        		refBeans.addAll(checkItemReferenceBeans(factory, deleteActionCache, new RepositoryObject(repoObject.getProperty())));
        	}
        }

        return refBeans;
    }

    @Override
	public Set<ItemReferenceBean> getItemReferenceBeans(IRepositoryViewObject convertNode,
            DeleteActionCache deleteActionCache) {
        Set<ItemReferenceBean> refBeans = new HashSet<ItemReferenceBean>();

        if (deleteActionCache == null) {
            deleteActionCache = DeleteActionCache.getInstance();
            deleteActionCache.createRecords();
        }

        if(convertNode!=null) {
        	refBeans.addAll(checkItemReferenceBeans(factory, deleteActionCache, new RepositoryObject(convertNode.getProperty())));
        }

        return refBeans;
    }

    /**
     * Find reference beans.
     *
     * @param factory the factory, not null
     * @param deleteActionCache the delete action cache, not null
     * @param repoObject the repository object, not null.
     * @return the reference beans collection
     */
    protected abstract Collection<ItemReferenceBean> checkItemReferenceBeans(IProxyRepositoryFactory factory,
			DeleteActionCache deleteActionCache,IRepositoryViewObject repoObject);

	protected boolean isItemInDeleteList(ItemReferenceBean bean, boolean isRefer) {
        if (deleteRepositoryObjects == null) {
            return false;
        }

        String itemName;
        String itemVersion;
        ERepositoryObjectType itemType;
        for (IRepositoryObject object : deleteRepositoryObjects) {
            Property property = object.getProperty();
            String label = property.getLabel();
            String version = property.getVersion();
            ERepositoryObjectType type = object.getRepositoryObjectType();
            if (isRefer) {
                itemName = bean.getReferenceItemName();
                itemVersion = bean.getReferenceItemVersion();
                itemType = bean.getReferenceItemType();
            } else {
                itemName = bean.getItemName();
                itemVersion = bean.getItemVersion();
                itemType = bean.getItemType();
            }
            if (label.equals(itemName) && version.equals(itemVersion) && type == itemType) {
                return true;
            }
        }

        return false;
    }

    public void setDeleteRepositoryObjects(List<? extends IRepositoryNode> deleteNodes) {
        deleteRepositoryObjects = new ArrayList<>();
        for (IRepositoryNode node : deleteNodes) {
            deleteRepositoryObjects.add(new RepositoryObject(node.getObject().getProperty()));
        }
    }
}
