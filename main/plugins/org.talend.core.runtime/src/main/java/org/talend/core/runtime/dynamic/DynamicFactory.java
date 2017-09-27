// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.runtime.dynamic;

import org.talend.core.runtime.dynamic.impl.DynamicPlugin;

import us.monoid.json.JSONObject;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class DynamicFactory {

    private static DynamicFactory instance;

    private DynamicFactory() {
        // nothing to do
    }

    public static DynamicFactory getInstance() {
        if (instance == null) {
            instance = new DynamicFactory();
        }
        return instance;
    }

    public static IDynamicPlugin createPluginFromJson(String jsonString) throws Exception {
        JSONObject json = new JSONObject(jsonString);
        return DynamicPlugin.fromXmlJson(json);
    }

}
