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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemContextLink {

    @JsonProperty("itemId")
    private String itemId;

    @JsonProperty("contextList")
    private List<ContextLink> contextList = new ArrayList<ContextLink>();

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public List<ContextLink> getContextList() {
        return contextList;
    }

    public void setContextList(List<ContextLink> contextList) {
        this.contextList = contextList;
    }

    public ContextParamLink findContextParamLinkByName(String repoContextId, String contextName, String paramName) {
        ContextLink contextLink = findContextLink(repoContextId, contextName);
        if (contextLink != null) {
            return contextLink.getParamLinkByName(paramName);
        }
        return null;
    }

    public ContextParamLink findContextParamLinkById(String repoContextId, String contextName, String paramId) {
        ContextLink contextLink = findContextLink(repoContextId, contextName);
        if (contextLink != null) {
            return contextLink.getParamLinkById(paramId);
        }
        return null;
    }

    public ContextParamLink findContextParamLinkByIdIgnoreContextName(String repoContextId, String paramId) {
        for (ContextLink contextLink : contextList) {
            if (StringUtils.equals(repoContextId, contextLink.getRepoId())) {
                ContextParamLink paramLink = contextLink.getParamLinkById(paramId);
                if (paramLink != null) {
                    return paramLink;
                }
            }
        }
        return null;
    }

    public ContextLink findContextLink(String repoContextId, String contextName) {
        for (ContextLink contextLink : contextList) {
            if ((repoContextId == null || StringUtils.equals(repoContextId, contextLink.getRepoId()))
                    && StringUtils.equals(contextName, contextLink.getContextName())) {
                return contextLink;
            }
        }
        return null;
    }

    public ItemContextLink cloneObj() {
        ItemContextLink obj = new ItemContextLink();
        obj.setItemId(itemId);
        for (ContextLink c : contextList) {
            obj.getContextList().add(c.cloneObj());
        }
        return obj;
    }
}
