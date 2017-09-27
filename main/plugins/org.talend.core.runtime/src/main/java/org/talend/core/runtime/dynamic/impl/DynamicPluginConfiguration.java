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
package org.talend.core.runtime.dynamic.impl;

import org.talend.core.runtime.dynamic.IDynamicPluginConfiguration;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONML;
import us.monoid.json.JSONObject;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class DynamicPluginConfiguration extends AbstractDynamicElement implements IDynamicPluginConfiguration {

    @Override
    public String getId() {
        Object id = getAttribute(ATTR_ID);
        if (id != null) {
            return id.toString();
        } else {
            return null;
        }
    }

    @Override
    public void setId(String id) {
        setAttribute(ATTR_ID, id);
    }

    @Override
    public String getName() {
        Object name = getAttribute(ATTR_NAME);
        if (name != null) {
            return name.toString();
        } else {
            return null;
        }
    }

    @Override
    public void setName(String name) {
        setAttribute(ATTR_NAME, name);
    }

    @Override
    public String getDescription() {
        Object description = getAttribute(ATTR_DESCRIPTION);
        if (description != null) {
            return description.toString();
        } else {
            return null;
        }
    }

    @Override
    public void setDescription(String description) {
        setAttribute(ATTR_DESCRIPTION, description);
    }

    @Override
    public String getTagName() {
        return TAG_NAME;
    }

    public static DynamicPluginConfiguration fromXmlJson(JSONObject json) throws Exception {
        DynamicPluginConfiguration dynamicPluginConfiguration = new DynamicPluginConfiguration();

        dynamicPluginConfiguration.initAttributesFromXmlJson(json);

        JSONArray children = getChildrenFrom(json);
        if (children != null) {
            int length = children.length();
            for (int i = 0; i < length; ++i) {
                JSONObject jObj = children.getJSONObject(i);
                DynamicConfiguration config = DynamicConfiguration.fromXmlJson(jObj);
                dynamicPluginConfiguration.addChild(config);
            }
        }

        return dynamicPluginConfiguration;
    }

    @Override
    public String toXmlString() throws Exception {
        return JSONML.toString(toXmlJson());
    }

}
