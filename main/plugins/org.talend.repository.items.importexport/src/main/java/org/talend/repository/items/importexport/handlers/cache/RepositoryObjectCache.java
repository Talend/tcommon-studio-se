// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.items.importexport.handlers.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.services.IGenericWizardService;
import org.talend.repository.items.importexport.handlers.model.ImportItem;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class RepositoryObjectCache {

    static ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

    private final Set<ERepositoryObjectType> types = new HashSet<ERepositoryObjectType>();

    private final Map<String, Boolean> lockState = new HashMap<String, Boolean>();

    // key is id of IRepositoryObject, value is a list of IRepositoryObject
    // with same id
    private final Map<String, List<IRepositoryViewObject>> cache = new HashMap<String, List<IRepositoryViewObject>>();

    private final Map<ERepositoryObjectType, List<IRepositoryViewObject>> itemsFromRepository = new HashMap<ERepositoryObjectType, List<IRepositoryViewObject>>();

    public List<IRepositoryViewObject> findObjectsByItem(ImportItem itemRecord) throws PersistenceException {
        Item item = itemRecord.getItem();
        ERepositoryObjectType type = ERepositoryObjectType.getItemType(item);
        initialize(type);
        List<IRepositoryViewObject> result = cache.get(itemRecord.getProperty().getId());
        if (result == null) {
            result = Collections.EMPTY_LIST;
        }
        return result;
    }

    public void addToCache(Item tmpItem) {
        ERepositoryObjectType itemType = ERepositoryObjectType.getItemType(tmpItem);
        IRepositoryViewObject newObject = new RepositoryViewObject(tmpItem.getProperty(), true);
        List<IRepositoryViewObject> items = cache.get(newObject.getId());
        if (items == null) {
            items = new ArrayList<IRepositoryViewObject>();
            cache.put(newObject.getId(), items);
        }
        items.add(newObject);
        List<IRepositoryViewObject> list = itemsFromRepository.get(itemType);
        if (list != null) {
            list.add(newObject);
        } else {
            List<IRepositoryViewObject> newList = new ArrayList<IRepositoryViewObject>();
            newList.add(newObject);
            itemsFromRepository.put(itemType, newList);
        }
    }

    public void initialize(ERepositoryObjectType itemType) throws PersistenceException {
        if (!types.contains(itemType)) {
            types.add(itemType);
            // load object by type
            List<IRepositoryViewObject> list = factory.getAll(itemType, true, false);
            if (list == null || list.isEmpty()) {
                ERepositoryObjectType newRepType = getNewRepType(itemType);
                if (newRepType != null) {
                    list = factory.getAll(newRepType, true, false);
                }
            }
            // change to RepositoryViewObject to save memory
            // (could be enhanced directly in repository for future versions)
            List<IRepositoryViewObject> newList = new ArrayList<IRepositoryViewObject>();
            for (IRepositoryViewObject obj : list) {
                IRepositoryViewObject newObject = new RepositoryViewObject(obj.getProperty(), true);
                // items with same id
                List<IRepositoryViewObject> items = cache.get(newObject.getId());
                if (items == null) {
                    items = new ArrayList<IRepositoryViewObject>();
                    cache.put(newObject.getId(), items);
                }
                items.add(newObject);
                newList.add(newObject);
            }
            itemsFromRepository.put(itemType, newList);
        }
    }

    private ERepositoryObjectType getNewRepType(ERepositoryObjectType oldType) {
        if (oldType == null) {
            return null;
        }
        ERepositoryObjectType newRepType = null;
        IGenericWizardService wizardService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IGenericWizardService.class)) {
            wizardService = (IGenericWizardService) GlobalServiceRegister.getDefault().getService(IGenericWizardService.class);
        }
        if (wizardService != null && !wizardService.isGenericType(oldType)) {
            newRepType = wizardService.getNewRepType(oldType.getType());
        }
        return newRepType;
    }

    public void setItemLockState(ImportItem itemRecord, boolean state) {
        lockState.put(itemRecord.getProperty().getId(), state);
    }

    public Boolean getItemLockState(ImportItem itemRecord) {
        return lockState.get(itemRecord.getProperty().getId());
    }

    public void clear() {
        types.clear();
        cache.clear();
        lockState.clear();
        itemsFromRepository.clear();
    }

    public Map<ERepositoryObjectType, List<IRepositoryViewObject>> getItemsFromRepository() {
        return itemsFromRepository;
    }
}
