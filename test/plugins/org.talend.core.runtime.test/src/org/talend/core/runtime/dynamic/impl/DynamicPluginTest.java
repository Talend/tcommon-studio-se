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
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.talend.core.runtime.dynamic.IDynamicConfiguration;
import org.talend.core.runtime.dynamic.IDynamicExtension;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONObject;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
@SuppressWarnings("nls")
public class DynamicPluginTest {

    @Test
    public void testFromXmlJson() throws Exception {
        String extensionPointTag = "point";
        String extensionPointValue = "org.talend.example.ext1";
        String idTag = "id";
        String idValue = "id1";

        String library = "library";
        String attr3 = "attr3";
        String value3 = "value3";

        JSONObject pluginJson = new JSONObject();
        pluginJson.put(AbstractDynamicElement.XML_TAG_NAME, DynamicPlugin.TAG_NAME);
        JSONArray extensionChildren = new JSONArray();
        pluginJson.put(AbstractDynamicElement.XML_ELEMENTS, extensionChildren);
        JSONObject json = new JSONObject();
        extensionChildren.put(json);
        json.put(AbstractDynamicElement.XML_TAG_NAME, DynamicExtension.TAG_NAME);
        json.put(extensionPointTag, extensionPointValue);
        json.put(idTag, idValue);
        JSONArray children = new JSONArray();
        json.put(AbstractDynamicElement.XML_ELEMENTS, children);
        JSONObject childJson = new JSONObject();
        childJson.put(AbstractDynamicElement.XML_TAG_NAME, library);
        childJson.put(attr3, value3);

        children.put(childJson);

        DynamicPlugin plugin = DynamicPlugin.fromXmlJson(pluginJson);
        assertEquals(DynamicPlugin.TAG_NAME, plugin.getTagName());

        List<IDynamicExtension> allExtensions = plugin.getAllExtensions();
        assertEquals(1, allExtensions.size());

        IDynamicExtension dynamicExtension = allExtensions.get(0);
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
            DynamicPlugin plugin = DynamicPlugin.fromXmlJson(json);
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }

    }

    @Test
    public void testToXmlString() throws Exception {
        String extensionPointValue1 = "org.talend.example.ext1";
        String idValue1 = "id1";
        String extensionPointValue2 = "org.talend.example.ext2";
        String idValue2 = "id2";

        String library = "library";
        String attr1 = "attr1";
        String value1 = "value1";
        String attr2 = "attr2";
        String value2 = "value2";
        String attr3 = "attr3";
        String value3 = "value3";

        DynamicPlugin plugin = new DynamicPlugin();

        IDynamicExtension extension1 = plugin.getExtension(extensionPointValue1, idValue1, true);

        DynamicConfiguration child1 = new DynamicConfiguration();
        child1.setConfigurationName(library);
        child1.setAttribute(attr1, value1);
        extension1.addConfiguration(child1);

        DynamicConfiguration child2 = new DynamicConfiguration();
        child2.setConfigurationName(library);
        child2.setAttribute(attr2, value2);
        extension1.addConfiguration(child2);

        IDynamicExtension extension2 = plugin.getExtension(extensionPointValue2, idValue2, true);

        DynamicConfiguration child3 = new DynamicConfiguration();
        child3.setConfigurationName(library);
        child3.setAttribute(attr3, value3);
        extension2.addConfiguration(child3);

        String xmlString = plugin.toXmlString();
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<?eclipse version=\"3.2\"?>\n"
                + "<plugin><extension id=\"id1\" point=\"org.talend.example.ext1\"><library attr1=\"value1\"></library><library attr2=\"value2\"></library></extension><extension id=\"id2\" point=\"org.talend.example.ext2\"><library attr3=\"value3\"></library></extension></plugin>";
        System.out.println(xmlString);
        assertEquals(expected, xmlString);
    }

    @Test
    public void testToXmlString2() throws Exception {
        String extensionPointValue1 = "org.talend.example.ext1";
        String idValue1 = "id1";
        String extensionPointValue2 = "org.talend.example.ext2";
        String idValue2 = "id2";

        String library = "library";
        String attr1 = "attr1";
        String value1 = "value1";
        String attr2 = "attr2";
        String value2 = "value2";
        String attr3 = "attr3";
        String value3 = "value3";

        DynamicPlugin plugin = new DynamicPlugin();

        IDynamicExtension extension1 = plugin.getExtension(extensionPointValue1, idValue1, true);

        DynamicConfiguration child1 = new DynamicConfiguration();
        child1.setConfigurationName(library);
        child1.setAttribute(attr1, value1);
        extension1.addConfiguration(child1);

        IDynamicExtension extension1_1 = new DynamicExtension();
        extension1_1.setExtensionId(idValue1);
        extension1_1.setExtensionPoint(extensionPointValue1);
        DynamicConfiguration child2 = new DynamicConfiguration();
        child2.setConfigurationName(library);
        child2.setAttribute(attr2, value2);
        extension1_1.addConfiguration(child2);
        plugin.addExtension(extension1_1);

        IDynamicExtension extension2 = plugin.getExtension(extensionPointValue2, idValue2, true);

        DynamicConfiguration child3 = new DynamicConfiguration();
        child3.setConfigurationName(library);
        child3.setAttribute(attr3, value3);
        extension2.addConfiguration(child3);

        String xmlString = plugin.toXmlString();
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<?eclipse version=\"3.2\"?>\n"
                + "<plugin><extension id=\"id1\" point=\"org.talend.example.ext1\"><library attr1=\"value1\"></library><library attr2=\"value2\"></library></extension><extension id=\"id2\" point=\"org.talend.example.ext2\"><library attr3=\"value3\"></library></extension></plugin>";
        System.out.println(xmlString);
        assertEquals(expected, xmlString);
    }
}
