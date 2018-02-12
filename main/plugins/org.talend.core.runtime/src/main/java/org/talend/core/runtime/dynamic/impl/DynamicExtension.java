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
package org.talend.core.runtime.dynamic.impl;

import java.util.List;

import org.talend.core.runtime.dynamic.IDynamicConfiguration;
import org.talend.core.runtime.dynamic.IDynamicExtension;
import org.talend.core.runtime.i18n.Messages;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONML;
import us.monoid.json.JSONObject;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class DynamicExtension extends AbstractDynamicElement implements IDynamicExtension {

    @Override
    public String toXmlString() throws Exception {
        return JSONML.toString(toXmlJson());
    }

    @Override
    public List<IDynamicConfiguration> getConfigurations() {
        List children = getChildren();
        return children;
    }

    @Override
    public void addConfiguration(IDynamicConfiguration config) {
        super.addChild((AbstractDynamicElement) config);
    }

    @Override
    public void removeConfiguration(IDynamicConfiguration config) {
        super.removeChild((AbstractDynamicElement) config);
    }

    @Override
    public IDynamicConfiguration createEmptyConfiguration() {
        return new DynamicConfiguration();
    }

    @Override
    public void setExtensionPoint(String extensionPoint) {
        super.setAttribute(ATTR_EXTENSION_POINT, extensionPoint);
    }

    @Override
    public String getExtensionPoint() {
        Object value = getAttribute(ATTR_EXTENSION_POINT);
        if (value != null) {
            return value.toString();
        } else {
            return null;
        }
    }

    @Override
    public void setExtensionId(String extensionId) {
        super.setAttribute(ATTR_EXTENSION_ID, extensionId);
    }

    @Override
    public String getExtensionId() {
        Object value = getAttribute(ATTR_EXTENSION_ID);
        if (value != null) {
            return value.toString();
        } else {
            return null;
        }
    }

    @Override
    public String getTagName() {
        return TAG_NAME;
    }

    public static DynamicExtension fromXmlJson(JSONObject json) throws Exception {

        String jsonTagName = getTagNameFrom(json);
        
        if (jsonTagName != null && !jsonTagName.isEmpty()) {
            if (!TAG_NAME.equals(jsonTagName)) {
                throw new Exception(Messages.getString("DynamicElement.incorrectInstance", TAG_NAME, jsonTagName)); //$NON-NLS-1$
            }
        }
        
        DynamicExtension dynamicExtension = new DynamicExtension();

        dynamicExtension.initAttributesFromXmlJson(json);

        JSONArray children = getChildrenFrom(json);
        if (children != null) {
            int length = children.length();
            for (int i = 0; i < length; ++i) {
                JSONObject jObj = children.getJSONObject(i);
                DynamicConfiguration config = DynamicConfiguration.fromXmlJson(jObj);
                dynamicExtension.addChild(config);
            }
        }

        return dynamicExtension;
    
    }

}
