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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.graphics.Image;
import org.talend.updates.runtime.ui.feature.model.IFeatureInfo;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public abstract class AbstractFeatureInfo extends AbstractFeatureItem implements IFeatureInfo {

    private String description;

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    @Override
    public Image getImage(IProgressMonitor monitor) throws Exception {
        return getFeature().getImage(monitor);
    }

}
