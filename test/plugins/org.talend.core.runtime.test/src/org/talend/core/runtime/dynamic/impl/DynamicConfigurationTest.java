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

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.talend.core.runtime.dynamic.IDynamicConfiguration;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONObject;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
@SuppressWarnings("nls")
public class DynamicConfigurationTest {

    @Test
    public void testFromXmlJson() throws Exception {
        String libraries = "libraries";
        String attr1 = "attr1";
        String attr2 = "attr2";
        String value1 = "value1";
        String value2 = "value2";

        String library = "library";
        String attr3 = "attr3";
        String value3 = "value3";

        JSONObject json = new JSONObject();
        json.put(AbstractDynamicElement.XML_TAG_NAME, libraries);
        JSONArray children = new JSONArray();
        json.put(AbstractDynamicElement.XML_ELEMENTS, children);
        json.put(attr1, value1);
        json.put(attr2, value2);

        JSONObject childJson = new JSONObject();
        childJson.put(AbstractDynamicElement.XML_TAG_NAME, library);
        childJson.put(attr3, value3);

        children.put(childJson);

        DynamicConfiguration dynamicConfiguration = DynamicConfiguration.fromXmlJson(json);
        assertEquals(dynamicConfiguration.getTagName(), libraries);
        assertEquals(dynamicConfiguration.getAttribute(attr1), value1);
        assertEquals(dynamicConfiguration.getAttribute(attr2), value2);
        List<IDynamicConfiguration> childConfigurations = dynamicConfiguration.getChildConfigurations();
        assertEquals(childConfigurations.size(), 1);
        IDynamicConfiguration child = childConfigurations.get(0);
        assertEquals(child.getTagName(), library);
        assertEquals(child.getAttribute(attr3), value3);
    }

    @Test
    public void testToXmlString() throws Exception {
        String libraries = "libraries";
        String attr1 = "attr1";
        String attr2 = "attr2";
        String value1 = "value1";
        String value2 = "value2";

        String library = "library";
        String attr3 = "attr3";
        String value3 = "value3";
        DynamicConfiguration dynamicConfiguration = new DynamicConfiguration();
        dynamicConfiguration.setConfigurationName(libraries);
        dynamicConfiguration.setAttribute(attr1, value1);
        dynamicConfiguration.setAttribute(attr2, value2);

        DynamicConfiguration childConfiguration = new DynamicConfiguration();
        childConfiguration.setConfigurationName(library);
        childConfiguration.setAttribute(attr3, value3);

        dynamicConfiguration.addChild(childConfiguration);

        String xmlString = dynamicConfiguration.toXmlString();
        String expected = "<libraries attr2=\"value2\" attr1=\"value1\"><library attr3=\"value3\"></library></libraries>";

        assertEquals(expected, xmlString);

    }

}
