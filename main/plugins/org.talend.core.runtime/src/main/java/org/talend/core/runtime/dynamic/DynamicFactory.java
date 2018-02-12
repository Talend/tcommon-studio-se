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
package org.talend.core.runtime.dynamic;

import org.talend.core.runtime.dynamic.impl.DynamicConfiguration;
import org.talend.core.runtime.dynamic.impl.DynamicExtension;
import org.talend.core.runtime.dynamic.impl.DynamicPlugin;
import org.talend.core.runtime.dynamic.impl.DynamicPluginConfiguration;

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

    public IDynamicPlugin createPluginFromJson(String jsonString) throws Exception {
        JSONObject json = new JSONObject(jsonString);
        return DynamicPlugin.fromXmlJson(json);
    }

    public IDynamicPlugin createDynamicPlugin() {
        return new DynamicPlugin();
    }

    public IDynamicPluginConfiguration createDynamicPluginConfiguration() {
        return new DynamicPluginConfiguration();
    }

    public IDynamicPluginConfiguration createDynamicPluginConfiguration(String jsonString) throws Exception {
        JSONObject json = new JSONObject(jsonString);
        return DynamicPluginConfiguration.fromXmlJson(json);
    }

    public IDynamicExtension createDynamicExtension() {
        return new DynamicExtension();
    }

    public IDynamicConfiguration createDynamicConfiguration() {
        return new DynamicConfiguration();
    }

}
