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

public class ContextLink {

    @JsonProperty("contextName")
    private String contextName;

    @JsonProperty("repoId")
    private String repoId;

    @JsonProperty("parameterList")
    private List<ContextParamLink> parameterList = new ArrayList<ContextParamLink>();

    public String getRepoId() {
        return repoId;
    }

    public void setRepoId(String repoId) {
        this.repoId = repoId;
    }

    public List<ContextParamLink> getParameterList() {
        return parameterList;
    }

    public void setParameterList(List<ContextParamLink> parameterList) {
        this.parameterList = parameterList;
    }

    public String getContextName() {
        return contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    public ContextParamLink getParamLinkByName(String paramName) {
        for (ContextParamLink paramLink : parameterList) {
            if (StringUtils.equals(paramLink.getName(), paramName)) {
                return paramLink;
            }
        }
        return null;
    }

    public ContextParamLink getParamLinkById(String id) {
        for (ContextParamLink paramLink : parameterList) {
            if (StringUtils.equals(paramLink.getId(), id)) {
                return paramLink;
            }
        }
        return null;
    }

    public ContextLink cloneObj() {
        ContextLink obj = new ContextLink();
        obj.setContextName(contextName);
        obj.setRepoId(repoId);
        for (ContextParamLink p : parameterList) {
            obj.getParameterList().add(p.cloneObj());
        }
        return obj;
    }

}
