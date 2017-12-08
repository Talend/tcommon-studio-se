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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.talend.core.runtime.dynamic.IDynamicAttribute;
import org.talend.core.runtime.i18n.Messages;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONObject;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public abstract class AbstractDynamicElement implements IDynamicAttribute {

    public static final String XML_TAG_NAME = "tagName"; //$NON-NLS-1$

    public static final String XML_ELEMENTS = "childNodes"; //$NON-NLS-1$

    // private String tagName;

    private Map<String, Object> attributeMap;

    private List<AbstractDynamicElement> children;

    public AbstractDynamicElement() {
        attributeMap = new HashMap<>();
        children = new ArrayList<>();
    }

    @Override
    public void setAttribute(String key, Object value) {
        attributeMap.put(key, value);
    }

    @Override
    public Object getAttribute(String key) {
        return attributeMap.get(key);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributeMap;
    }

    @Override
    public Object removeAttribute(String key) {
        return attributeMap.remove(key);
    }

    public void addChild(AbstractDynamicElement child) {
        if (child == null) {
            throw new IllegalArgumentException("New added element should not be null!");
        }
        children.add(child);
    }

    public void addChild(int index, AbstractDynamicElement child) {
        if (child == null) {
            throw new IllegalArgumentException("New added element should not be null!");
        }
        children.add(index, child);
    }

    @Override
    public int getChildIndex(IDynamicAttribute child) {
        if (child == null) {
            return -1;
        }
        return children.indexOf(child);
    }

    public void removeChild(AbstractDynamicElement child) {
        children.remove(child);
    }

    public List<AbstractDynamicElement> getChildren() {
        return children;
    }

    @Override
    public JSONObject toXmlJson() throws Exception {
        JSONObject json = createJsonObject();

        String tagName = getTagName();
        if (tagName == null) {
            throw new Exception(Messages.getString("DynamicElement.tagName.empty")); //$NON-NLS-1$
        }
        json.put(XML_TAG_NAME, tagName);

        for (Map.Entry<String, Object> entry : attributeMap.entrySet()) {
            String key = entry.getKey();
            json.put(key, convert2JsonValue(key));
        }

        JSONArray childArray = new JSONArray();
        json.put(XML_ELEMENTS, childArray);

        for (AbstractDynamicElement child : children) {
            childArray.put(child.toXmlJson());
        }

        return json;
    }

    public void initAttributesFromXmlJson(JSONObject json) throws Exception {
        Iterator<String> keyIter = json.keys();
        if (keyIter != null) {
            while (keyIter.hasNext()) {
                String key = keyIter.next();
                if (XML_TAG_NAME.equals(key)) {
                    continue;
                } else if (XML_ELEMENTS.equals(key)) {
                    continue;
                }
                Object value = readFromJsonValue(json, key);
                attributeMap.put(key, value);
            }
        }
    }

    protected Object convert2JsonValue(String key) throws Exception {
        return attributeMap.get(key);
    }

    protected Object readFromJsonValue(JSONObject json, String key) throws Exception {
        return json.opt(key);
    }

    public static String getTagNameFrom(JSONObject xmlJson) throws Exception {
        return xmlJson.optString(XML_TAG_NAME);
    }

    public static JSONArray getChildrenFrom(JSONObject xmlJson) throws Exception {
        return xmlJson.optJSONArray(XML_ELEMENTS);
    }

    private JSONObject createJsonObject() throws Exception {
        JSONObject json = new JSONObject();
        // Field field = JSONObject.class.getDeclaredField("map"); //$NON-NLS-1$
        // field.setAccessible(true);
        // field.set(json, new LinkedHashMap<>());
        return json;
    }
}
