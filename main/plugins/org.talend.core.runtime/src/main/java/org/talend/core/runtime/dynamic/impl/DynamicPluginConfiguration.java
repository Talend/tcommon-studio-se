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

import java.util.ArrayList;
import java.util.List;

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
        return (String) getAttribute(ATTR_ID);
    }

    @Override
    public void setId(String id) {
        setAttribute(ATTR_ID, id);
    }

    @Override
    public String getName() {
        return (String) getAttribute(ATTR_NAME);
    }

    @Override
    public void setName(String name) {
        setAttribute(ATTR_NAME, name);
    }

    @Override
    public String getVersion() {
        return (String) getAttribute(ATTR_VERSION);
    }

    @Override
    public void setVersion(String version) {
        setAttribute(ATTR_VERSION, version);
    }

    @Override
    public String getDescription() {
        return (String) getAttribute(ATTR_DESCRIPTION);
    }

    @Override
    public void setDescription(String description) {
        setAttribute(ATTR_DESCRIPTION, description);
    }

    @Override
    public String getDistribution() {
        return (String) getAttribute(ATTR_DISTRIBUTION);
    }

    @Override
    public void setDistribution(String destribution) {
        setAttribute(ATTR_DISTRIBUTION, destribution);
    }

    @Override
    public String getRepository() {
        return (String) getAttribute(ATTR_REPOSITORY);
    }

    @Override
    public void setRepository(String repository) {
        setAttribute(ATTR_REPOSITORY, repository);
    }

    @Override
    public List<String> getServices() {
        return (List<String>) getAttribute(ATTR_SERVICES);
    }

    @Override
    public void setServices(List<String> services) {
        setAttribute(ATTR_SERVICES, services);
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
    protected Object readFromJsonValue(JSONObject json, String key) throws Exception {
        if (ATTR_SERVICES.equals(key)) {
            List<String> services = new ArrayList<>();
            JSONArray arr = json.optJSONArray(key);
            if (arr != null && 0 < arr.length()) {
                for (int i = 0; i < arr.length(); ++i) {
                    String service = arr.getString(i);
                    services.add(service);
                }
            }
            return services;
        } else {
            return super.readFromJsonValue(json, key);
        }
    }

    @Override
    protected Object convert2JsonValue(String key) throws Exception {
        if (ATTR_SERVICES.equals(key)) {
            List<String> services = (List<String>) getAttribute(key);
            JSONArray arr = new JSONArray(services);
            return arr;
        } else {
            return super.convert2JsonValue(key);
        }
    }

    @Override
    public String toXmlString() throws Exception {
        return JSONML.toString(toXmlJson());
    }

}
