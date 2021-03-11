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
package org.talend.core.model.context.link;

import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.context.ContextUtils;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.Item;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;


public class ConnectionItemContextLinkService extends AbstractItemContextLinkService {

    @Override
    public boolean accept(Item item) {
        if (item instanceof ConnectionItem) {
            return true;
        }
        return false;
    }

    @Override
    public boolean saveItemLink(Item item) throws PersistenceException {
        if (item instanceof ConnectionItem) {
            ConnectionItem connectionItem = (ConnectionItem) item;
            return saveContextLink(connectionItem.getConnection(), item, null, null);
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private synchronized boolean saveContextLink(Connection connection, Item item, ItemContextLink backupContextLink,
            ItemContextLink remoteContextLink) throws PersistenceException {
        boolean hasLinkFile = false;
        ItemContextLink itemContextLink = new ItemContextLink();
        itemContextLink.setItemId(item.getProperty().getId());
        if (connection.isContextMode()) {
            String contextId = connection.getContextId();
            if (StringUtils.isEmpty(contextId) || IContextParameter.BUILT_IN.equals(contextId)) {
                return hasLinkFile;
            }
            ContextLink contextLink = new ContextLink();
            contextLink.setContextName(connection.getContextName());
            contextLink.setRepoId(contextId);
            itemContextLink.getContextList().add(contextLink);

            ContextItem contextItem = ContextUtils.getContextItemById2(contextId);
            if (contextItem != null) {
                ContextType contextType = ContextUtils.getContextTypeByName(contextItem.getContext(),
                        connection.getContextName());
                if (contextType != null) {
                    for (Object o : contextType.getContextParameter()) {
                        if (o instanceof ContextParameterType) {
                            ContextParameterType contextParameterType = (ContextParameterType) o;
                            ContextParamLink contextParamLink = new ContextParamLink();
                            contextParamLink.setName(contextParameterType.getName());
                            contextParamLink.setId(ResourceHelper.getUUID(contextParameterType));
                            contextLink.getParameterList().add(contextParamLink);
                        }
                    }
                }
            }
            // Because we don't know db connection using which values, we save all for merge operation
            if (backupContextLink != null) {
                for (ContextLink cl : backupContextLink.getContextList()) {
                    if (StringUtils.equals(cl.getRepoId(), contextId)
                            && StringUtils.equals(cl.getContextName(), connection.getContextName())) {
                        for (ContextParamLink cpl : cl.getParameterList()) {
                            if (contextLink.getParamLinkByName(cpl.getName()) == null) {
                                contextLink.getParameterList().add(cpl);
                            }
                        }
                    }
                }
            }
            if (remoteContextLink != null) {
                for (ContextLink cl : remoteContextLink.getContextList()) {
                    if (StringUtils.equals(cl.getRepoId(), contextId)
                            && StringUtils.equals(cl.getContextName(), connection.getContextName())) {
                        for (ContextParamLink cpl : cl.getParameterList()) {
                            if (contextLink.getParamLinkByName(cpl.getName()) == null) {
                                contextLink.getParameterList().add(cpl);
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

    public ItemContextLink loadItemLink(Item item) throws PersistenceException {
        return ContextLinkService.getInstance().doLoadContextLinkFromJson(item);
    }

    @Override
    public boolean mergeItemLink(Item item, ItemContextLink backupContextLink, InputStream remoteLinkFileInput)
            throws PersistenceException {
        if (item instanceof ConnectionItem) {
            ConnectionItem connectionItem = (ConnectionItem) item;
            ItemContextLink remoteContextLink = ContextLinkService.getInstance().doLoadContextLinkFromFile(remoteLinkFileInput);
            return saveContextLink(connectionItem.getConnection(), item, backupContextLink, remoteContextLink);
        }
        return false;
    }

}
