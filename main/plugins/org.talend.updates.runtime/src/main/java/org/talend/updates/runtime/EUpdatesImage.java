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
package org.talend.updates.runtime;

import org.talend.commons.ui.runtime.image.IImage;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
@SuppressWarnings("nls")
public enum EUpdatesImage implements IImage {
    COMPONENTS_MANAGER_BANNER("/icons/componentsManager.png"),

    LOADING("/icons/featureDefaultIcon.png"),

    UPDATE_BIG("/icons/update.png"),

    FIND_16("/icons/find_16.png"),

    ;

    private String path;

    EUpdatesImage(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public Class getLocation() {
        return this.getClass();
    }
}
