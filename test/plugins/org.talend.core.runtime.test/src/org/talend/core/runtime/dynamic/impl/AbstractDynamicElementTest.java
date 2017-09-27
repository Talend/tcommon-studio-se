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

import org.junit.Test;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONObject;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
@SuppressWarnings("nls")
public class AbstractDynamicElementTest {

    @Test
    public void testToXmlJson() throws Exception {
        String libraries = "libraries";
        String attr1 = "attr1";
        String attr2 = "attr2";
        String value1 = "value1";
        String value2 = "value2";

        String library = "library";
        String attr3 = "attr3";
        String value3 = "value3";
        DynamicElement dynamicConfiguration = new DynamicElement();
        dynamicConfiguration.setTagName(libraries);
        dynamicConfiguration.setAttribute(attr1, value1);
        dynamicConfiguration.setAttribute(attr2, value2);

        DynamicElement childConfiguration = new DynamicElement();
        childConfiguration.setTagName(library);
        childConfiguration.setAttribute(attr3, value3);

        dynamicConfiguration.addChild(childConfiguration);

        JSONObject xmlJson = dynamicConfiguration.toXmlJson();

        assertEquals(libraries, xmlJson.getString(AbstractDynamicElement.XML_TAG_NAME));
        assertEquals(value1, xmlJson.getString(attr1));
        assertEquals(value2, xmlJson.getString(attr2));

        JSONArray children = xmlJson.getJSONArray(AbstractDynamicElement.XML_ELEMENTS);
        assertEquals(1, children.length());

        JSONObject childJson = children.getJSONObject(0);
        assertEquals(library, childJson.getString(AbstractDynamicElement.XML_TAG_NAME));
        assertEquals(value3, childJson.getString(attr3));
    }

    class DynamicElement extends AbstractDynamicElement {

        public void setTagName(String tagName) {
            setAttribute(XML_TAG_NAME, tagName);
        }

        @Override
        public String getTagName() {
            return (String) getAttribute(XML_TAG_NAME);
        }

    }
}
