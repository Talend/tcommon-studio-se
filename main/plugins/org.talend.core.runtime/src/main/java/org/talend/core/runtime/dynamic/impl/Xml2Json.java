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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.talend.core.runtime.dynamic.DynamicFactory;
import org.talend.core.runtime.dynamic.DynamicServiceUtil;
import org.talend.core.runtime.dynamic.IDynamicPlugin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import us.monoid.json.JSONML;
import us.monoid.json.JSONObject;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class Xml2Json {

    public static void main(String args[]) throws Exception {
        createTemplate();
    }

    public static void xml2json() throws Exception {
        File file = new File("D:\\cmeng\\Downloads\\temp\\plugin.xml");
        String xmlStr = readFile(file);
        JSONObject jo = JSONML.toJSONObject(xmlStr);
        // System.out.println(jo.toString());
        String newXml = JSONML.toString(jo);
        // System.out.println(newXml);
        ObjectMapper om = new ObjectMapper();
        JsonNode jn = om.readTree(jo.toString());
        String formatString = om.writerWithDefaultPrettyPrinter().writeValueAsString(jn);
        // System.out.println(formatString);
        jo = new JSONObject(formatString);
        newXml = JSONML.toString(jo);
        System.out.println(newXml);
    }

    public static void createTemplate() throws Exception {
        File file = new File("D:\\cmeng\\Downloads\\temp\\plugin.xml");
        String xmlStr = readFile(file);
        JSONObject xmlJson = JSONML.toJSONObject(xmlStr);

        IDynamicPlugin plugin = DynamicFactory.getInstance().createPluginFromJson(xmlJson.toString());

        // Map<String, Object> idMap = new HashMap<>();
        // buildIdMap(idMap, (AbstractDynamicElement) plugin);

        addTemplateAndDefault((AbstractDynamicElement) plugin);

        String generatedJson = ((AbstractDynamicElement) plugin).toXmlJson().toString();
        String formatedJson = DynamicServiceUtil.formatJsonString(generatedJson);
        System.out.println(formatedJson);
    }

    private static void addTemplateAndDefault(AbstractDynamicElement element) {
        String regx = "(-CDH_5.*)|([-_]\\d.*)|(-cdh5.*)|(\\.jar)";
        Map<String, Object> attrMap = element.getAttributes();
        Map<String, String> templateMap = new LinkedHashMap<>();
        Map<String, String> defaultMap = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : attrMap.entrySet()) {
            String key = entry.getKey();

            if ("uripath".equals(key)) {
                continue;
            }

            String value = (String) entry.getValue();

            String templateKey = "template_" + key;
            String templateValue = "";
            if (value != null) {
                if ("id".equals(key)) {
                    templateValue = value.replaceAll(regx, "_CDH5_{0}");
                } else if ("context".equals(key)) {
                    templateValue = "plugin:org.talend.hadoop.distribution.cdh5x";
                } else {
                    templateValue = value.replaceAll(regx, ".*");
                }
            }
            templateMap.put(templateKey, templateValue);

            String defaultKey = "default_" + key;
            String defaultValue = value;
            defaultMap.put(defaultKey, defaultValue);

            // System.out.println(key + ":\n\t" + value + "\n\t" + templateValue + "\n");
        }
        for (Map.Entry<String, String> entry : defaultMap.entrySet()) {
            element.setAttribute(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, String> entry : templateMap.entrySet()) {
            element.setAttribute(entry.getKey(), entry.getValue());
        }
        List<AbstractDynamicElement> children = element.getChildren();
        for (AbstractDynamicElement child : children) {
            addTemplateAndDefault(child);
        }
    }

    public static void buildIdMap(Map<String, Object> map, AbstractDynamicElement element) throws Exception {
        String id = (String) element.getAttribute("id");
        if (id == null || id.isEmpty()) {
            throw new Exception("id is null: " + element.toXmlJson().toString());
        }
        if ("library".equals(element.getTagName())) {
            return;
        }
        map.put(id, element);
        List<AbstractDynamicElement> children = element.getChildren();
        for (AbstractDynamicElement child : children) {
            buildIdMap(map, child);
        }
    }

    public static String readFile(File file) throws Exception {
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
