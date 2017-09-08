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
package org.talend.metadata.managment.ui.utils;

import java.util.Map;

import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;

/**
 * this interface is used for update context property values after switching context.
 */
public interface ISwitchContext {

    /**
     * update Context Group for one Connection Item.
     * 
     * @param connItem
     * @param selectedContext
     * @return
     */
    public boolean updateContextGroup(ConnectionItem connItem, String selectedContext);

    /**
     * update Context For all Connection Items.
     * 
     * @param contextGroupRanamedMap
     * @param contextItem
     * @return
     */
    public boolean updateContextForConnectionItems(Map<String, String> contextGroupRanamedMap, ContextItem contextItem);

}
