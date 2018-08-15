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
package org.talend.updates.runtime.ui.feature.model.runtime;

import org.talend.updates.runtime.feature.FeaturesManager;
import org.talend.updates.runtime.ui.feature.form.listener.ICheckListener;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class FeaturesManagerRuntimeData {

    private FeaturesManager componentsManager;

    private ICheckListener checkListener;

    public FeaturesManager getComponentsManager() {
        return this.componentsManager;
    }

    public void setComponentsManager(FeaturesManager componentsManager) {
        this.componentsManager = componentsManager;
    }

    public ICheckListener getCheckListener() {
        return this.checkListener;
    }

    public void setCheckListener(ICheckListener checkListener) {
        this.checkListener = checkListener;
    }

}
