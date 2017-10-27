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
package org.talend.presentation.onboarding.ui.runtimedata;

import java.util.List;

/**
 * created by cmeng on Sep 25, 2015 Detailled comment
 *
 */
public class OnBoardingPerspectiveBean {

    private String perspId;

    private List<OnBoardingPageBean> pages;

    public String getPerspId() {
        return this.perspId;
    }

    public void setPerspId(String perspId) {
        this.perspId = perspId;
        if (this.perspId != null) {
            this.perspId = this.perspId.trim();
        }
    }

    public List<OnBoardingPageBean> getPages() {
        return this.pages;
    }

    public void setPages(List<OnBoardingPageBean> pages) {
        this.pages = pages;
    }

}
