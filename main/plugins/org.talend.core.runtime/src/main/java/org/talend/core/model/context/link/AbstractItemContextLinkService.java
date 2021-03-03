package org.talend.core.model.context.link;

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
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.context.ContextUtils;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.Item;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IRepositoryService;


public abstract class AbstractItemContextLinkService implements IItemContextLinkService {

    @Override
    public boolean mergeItemLink(Item item, ItemContextLink backupContextLink, InputStream remoteLinkFileInput) throws PersistenceException {
        ItemContextLink remoteContextLink = ContextLinkService.getInstance().doLoadContextLinkFromFile(remoteLinkFileInput);
        List<ContextType> contextTypeList = ContextUtils.getAllContextType(item);
        return saveContextLink(contextTypeList, item, backupContextLink, remoteContextLink);
    }
    
    public synchronized boolean saveContextLink(List<ContextType> contextTypeList, Item item,
            ItemContextLink backupContextLink, ItemContextLink remoteContextLink)
            throws PersistenceException {
        boolean hasLinkFile = false;
        String itemId = item.getProperty().getId();
        ItemContextLink itemContextLink = new ItemContextLink();
        itemContextLink.setItemId(itemId);
        Map<String, Item> tempCache = new HashMap<String, Item>();
        if (contextTypeList != null && contextTypeList.size() > 0) {
            for (Object object : contextTypeList) {
                if (object instanceof ContextType) {
                    ContextType jobContextType = (ContextType) object;
                    for (Object o : jobContextType.getContextParameter()) {
                        if (o instanceof ContextParameterType) {
                            ContextParameterType contextParameterType = (ContextParameterType) o;
                            String repositoryContextId = contextParameterType.getRepositoryContextId();
                            if (!ContextUtils.isBuildInParameter(contextParameterType)) {
                                ContextLink contextLink = itemContextLink
                                        .findContextLink(contextParameterType.getRepositoryContextId(), jobContextType.getName());
                                if (contextLink == null) {
                                    contextLink = new ContextLink();
                                    contextLink.setContextName(jobContextType.getName());
                                    contextLink.setRepoId(repositoryContextId);
                                }
                                ContextParamLink contextParamLink = createParamLink(repositoryContextId, jobContextType.getName(),
                                        contextParameterType.getName(), contextParameterType.getInternalId(), tempCache,
                                        backupContextLink, remoteContextLink);
                                if (contextParamLink != null) {
                                    contextLink.getParameterList().add(contextParamLink);
                                }

                                if (contextLink.getParameterList().size() > 0
                                        && !itemContextLink.getContextList().contains(contextLink)) {
                                    itemContextLink.getContextList().add(contextLink);
                                }
                            }

                        }
                    }
                }
            }
        }
        if (itemContextLink.getContextList().size() > 0) {
            ContextLinkService.getInstance().saveContextLinkToJson(item, itemContextLink);
            hasLinkFile = true;
        } else {
            ContextLinkService.getInstance().deleteContextLinkJsonFile(item);
        }
        return hasLinkFile;
    }

    public synchronized boolean saveContextLink(List<ContextType> contextTypeList, Item item) throws PersistenceException {
        ItemContextLink backupContextLink = ContextLinkService.getInstance().loadContextLinkFromJson(item);
        return saveContextLink(contextTypeList, item, backupContextLink, null);
    }

    private ContextParamLink createParamLink(String repositoryContextId, String contextName, String paramName, String internalId,
            Map<String, Item> tempCache, ItemContextLink oldContextLink, ItemContextLink remoteContextLink) {
        ContextParamLink contextParamLink = null;

        if (StringUtils.isNotBlank(internalId)) {
            contextParamLink = new ContextParamLink();
            contextParamLink.setName(paramName);
            contextParamLink.setId(internalId);
        }

        Item contextItem = tempCache.get(repositoryContextId);
        IRepositoryService repositoryService = GlobalServiceRegister.getDefault().getService(IRepositoryService.class);
        if (contextItem == null) {
            contextItem = ContextUtils.getRepositoryContextItemById(repositoryContextId);
            tempCache.put(repositoryContextId, contextItem);
            if (contextItem != null && !(contextItem instanceof ContextItem)
                    && ProjectManager.getInstance().isInCurrentMainProject(contextItem)
                    && checkRepoItemContextParamInternalId(contextItem)) {
                try {
                    repositoryService.getProxyRepositoryFactory().save(contextItem, true); // This should only using for migration phase                                                         // case the internal id is null
                } catch (Exception ex) {
                    ExceptionHandler.process(ex);
                }
            }
        }
        if (contextItem != null) {
            ContextType contextType = ContextUtils.getContextTypeByName(contextItem, contextName);
            ContextParameterType repoContextParameterType = ContextUtils.getContextParameterTypeByName(contextType, paramName);
            if (repoContextParameterType != null && repoContextParameterType.eResource() == null) {
                // processItem save before than contextItem, caused eResource null

                try {
                    repositoryService.getProxyRepositoryFactory().save(contextItem, false);
                } catch (PersistenceException e) {
                    ExceptionHandler.process(e);
                }
            }
            String uuID = null;
            boolean isFromContextItem = (contextItem instanceof ContextItem);
            if (repoContextParameterType != null) {
                if (isFromContextItem) {
                    uuID = ResourceHelper.getUUID(repoContextParameterType);
                } else if (repoContextParameterType.getInternalId() != null) {
                    uuID = repoContextParameterType.getInternalId();
                }
            }
            if (repoContextParameterType == null && remoteContextLink != null) {
                ContextParamLink remoteParamLink = remoteContextLink.findContextParamLinkByName(repositoryContextId, contextName,
                        paramName);
                if (remoteParamLink != null && remoteParamLink.getId() != null) {
                    repoContextParameterType = findContextParamTypeById(contextType, isFromContextItem, remoteParamLink.getId());
                    uuID = remoteParamLink.getId();
                }
            }
            if (repoContextParameterType == null && oldContextLink != null) {
                ContextParamLink oldParamLink = oldContextLink.findContextParamLinkByName(repositoryContextId, contextName,
                        paramName);
                if (oldParamLink != null) {
                    uuID = oldParamLink.getId();
                }
            }

            if (StringUtils.isNotBlank(uuID)) {
                contextParamLink = new ContextParamLink();
                contextParamLink.setName(paramName);
                contextParamLink.setId(uuID);
            }
        }
        return contextParamLink;
    }

    protected ContextParameterType findContextParamTypeById(ContextType contextType, boolean isFromContextItem, String id) {
        for (Object obj : contextType.getContextParameter()) {
            if (obj instanceof ContextParameterType) {
                ContextParameterType repoParam = (ContextParameterType) obj;
                if (isFromContextItem && StringUtils.equals(id, ResourceHelper.getUUID(repoParam))) {
                    return repoParam;
                }
                if (!isFromContextItem && StringUtils.equals(id, repoParam.getInternalId())) {
                    return repoParam;
                }
            }
        }
        return null;
    }
    
    protected boolean checkRepoItemContextParamInternalId(Item item) {
        boolean isModified = false;
        EList<?> contextTypeList = ContextUtils.getAllContextType(item);
        if (contextTypeList != null) {
            for (Object typeObj : contextTypeList) {
                if (typeObj instanceof ContextType) {
                    ContextType type = (ContextType) typeObj;
                    for (Object obj : type.getContextParameter()) {
                        if (obj instanceof ContextParameterType) {
                            ContextParameterType contextParam = (ContextParameterType) obj;
                            if (ContextUtils.isBuildInParameter(contextParam)
                                    && StringUtils.isEmpty(contextParam.getInternalId())) {
                                contextParam
                                        .setInternalId(CoreRuntimePlugin.getInstance().getProxyRepositoryFactory().getNextId());
                                isModified = true;
                            }
                        }
                    }
                }
            }
        }
        return isModified;
    }
}


