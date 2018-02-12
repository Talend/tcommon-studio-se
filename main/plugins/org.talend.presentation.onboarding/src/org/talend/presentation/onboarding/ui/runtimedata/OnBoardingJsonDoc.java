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
package org.talend.presentation.onboarding.ui.runtimedata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talend.presentation.onboarding.exceptions.OnBoardingExceptionHandler;
import org.talend.presentation.onboarding.i18n.Messages;

/**
 * created by cmeng on Sep 25, 2015 Detailled comment
 *
 */
public class OnBoardingJsonDoc {

    private String id;

    private String description;

    private String defaultPerspId;

    private OnBoardingCommandBean onOpen;

    private OnBoardingCommandBean onClose;

    private List<OnBoardingPerspectiveBean> contents;

    private Map<String, OnBoardingPerspectiveBean> contentMap = new HashMap<String, OnBoardingPerspectiveBean>();

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
        if (this.id != null) {
            this.id = this.id.trim();
        }
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
        if (this.description != null) {
            this.description = this.description.trim();
        }
    }

    public String getDefaultPerspId() {
        return this.defaultPerspId;
    }

    public void setDefaultPerspId(String defaultPerspId) {
        this.defaultPerspId = defaultPerspId;
        if (this.defaultPerspId != null) {
            this.defaultPerspId = this.defaultPerspId.trim();
        }
    }

    public OnBoardingCommandBean getOnOpen() {
        return this.onOpen;
    }

    public void setOnOpen(OnBoardingCommandBean onOpen) {
        this.onOpen = onOpen;
    }

    public OnBoardingCommandBean getOnClose() {
        return this.onClose;
    }

    public void setOnClose(OnBoardingCommandBean onClose) {
        this.onClose = onClose;
    }

    public Map<String, OnBoardingPerspectiveBean> getContentMap() {
        return this.contentMap;
    }

    public void setContentMap(Map<String, OnBoardingPerspectiveBean> contentMap) {
        this.contentMap = contentMap;
    }

    public List<OnBoardingPerspectiveBean> getContents() {
        return this.contents;
    }

    public void setContents(List<OnBoardingPerspectiveBean> contents) {
        this.contents = contents;
        buildContentMap();
    }

    public void buildContentMap() {
        contentMap.clear();
        if (contents == null) {
            return;
        }
        for (OnBoardingPerspectiveBean content : contents) {
            String perspId = content.getPerspId();
            if (perspId == null || perspId.trim().isEmpty()) {
                continue;
            }
            perspId = perspId.trim();
            if (contentMap.get(perspId) != null) {
                OnBoardingExceptionHandler.warn(Messages.getString("OnBoardingJsonDoc.buildContentMap.perspIdExists", perspId)); //$NON-NLS-1$
            }
            contentMap.put(perspId, content);
        }
    }

    public OnBoardingPerspectiveBean getPerspectiveBean(String perspId) {
        return contentMap.get(perspId);
    }

}
