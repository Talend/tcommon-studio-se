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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.talend.core.runtime.dynamic.IDynamicConfiguration;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONObject;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
@SuppressWarnings("nls")
public class DynamicExtensionTest {

    @Test
    public void testFromXmlJson() throws Exception {
        String extensionPointTag = "point";
        String extensionPointValue = "org.talend.example.ext1";
        String idTag = "id";
        String idValue = "id1";

        String library = "library";
        String attr3 = "attr3";
        String value3 = "value3";

        JSONObject json = new JSONObject();
        json.put(AbstractDynamicElement.XML_TAG_NAME, DynamicExtension.TAG_NAME);
        json.put(extensionPointTag, extensionPointValue);
        json.put(idTag, idValue);
        JSONArray children = new JSONArray();
        json.put(AbstractDynamicElement.XML_ELEMENTS, children);

        JSONObject childJson = new JSONObject();
        childJson.put(AbstractDynamicElement.XML_TAG_NAME, library);
        childJson.put(attr3, value3);

        children.put(childJson);

        DynamicExtension dynamicExtension = DynamicExtension.fromXmlJson(json);
        assertEquals(dynamicExtension.getTagName(), DynamicExtension.TAG_NAME);
        assertEquals(dynamicExtension.getExtensionPoint(), extensionPointValue);
        assertEquals(dynamicExtension.getExtensionId(), idValue);
        List<IDynamicConfiguration> childConfigurations = dynamicExtension.getConfigurations();
        assertEquals(childConfigurations.size(), 1);
        IDynamicConfiguration child = childConfigurations.get(0);
        assertEquals(child.getTagName(), library);
        assertEquals(child.getAttribute(attr3), value3);
    }

    @Test
    public void testFromXmlJsonException() throws Exception {
        String libraries = "libraries";
        String idTag = "id";
        String idValue = "id1";

        JSONObject json = new JSONObject();
        json.put(AbstractDynamicElement.XML_TAG_NAME, libraries);
        json.put(idTag, idValue);
        JSONArray children = new JSONArray();
        json.put(AbstractDynamicElement.XML_ELEMENTS, children);

        try {
            DynamicExtension dynamicExtension = DynamicExtension.fromXmlJson(json);
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }

    }

    @Test
    public void testToXmlString() throws Exception {
        String extensionPointValue = "org.talend.example.ext1";
        String idValue = "id1";

        String library = "library";
        String attr3 = "attr3";
        String value3 = "value3";

        DynamicExtension dynamicExtension = new DynamicExtension();
        dynamicExtension.setExtensionId(idValue);
        dynamicExtension.setExtensionPoint(extensionPointValue);

        DynamicConfiguration childConfiguration = new DynamicConfiguration();
        childConfiguration.setConfigurationName(library);
        childConfiguration.setAttribute(attr3, value3);

        dynamicExtension.addConfiguration(childConfiguration);

        String xmlString = dynamicExtension.toXmlString();
        String expected = "<extension id=\"id1\" point=\"org.talend.example.ext1\"><library attr3=\"value3\"></library></extension>";

        assertEquals(expected, xmlString);
    }
}
