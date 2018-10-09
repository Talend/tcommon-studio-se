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
package org.talend.updates.runtime.ui.feature.model.impl;

import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.ui.feature.model.IFeatureItem;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public abstract class AbstractFeatureItem implements IFeatureItem {

    private String title;

    private ExtraFeature feature;

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public ExtraFeature getFeature() {
        return feature;
    }

    public void setFeature(ExtraFeature feature) {
        this.feature = feature;
    }
}
