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
    COMPONENTS_MANAGER_BANNER("/icons/componentsManager_banner.png"),

    COMPONENTS_MANAGER_16("/icons/componentsManager_16.png"),

    COMPONENTS_MANAGER_32("/icons/componentsManager_32.png"),

    LOADING("/icons/componentsManager_banner.png"),

    UPDATE_BIG("/icons/update.png"),

    UPDATE_16("/icons/update_16.png"),

    UPDATE_32("/icons/update_32.png"),

    FIND_16("/icons/find_16.png"),

    FIND_32("/icons/find_32.png"),

    MESSAGE_INFO_16("/icons/message_info_16.png"),

    MESSAGE_INFO_32("/icons/message_info_32.png"),

    INSTALL_16("/icons/install_16.gif"),

    UNINSTALL_16("/icons/update_32.png"),

    NEWS_UPDATE_16("/icons/update_32.png");

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
