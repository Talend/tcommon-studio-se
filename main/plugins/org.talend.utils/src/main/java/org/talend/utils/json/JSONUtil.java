// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.utils.json;

import java.util.Iterator;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class JSONUtil {

    /**
     * If the key have been existed in target,
     *
     * - for JSONObject, will merge the children.
     *
     * - for JSONArray, will append the source array in the end.
     *
     * - else, will overwrite.
     *
     * will return a new json always, means, won't break and change the source and target json.
     */
    public static JSONObject merge(JSONObject source, JSONObject target) {
        JSONObject mergedJson = new JSONObject();
        try {
            if (source != null && target != null) {
                mergedJson = new JSONObject(target.toString());

                Iterator<String> keys = source.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    Object o = source.get(key);
                    if (mergedJson.has(key)) {
                        if (o instanceof JSONObject) {
                            JSONObject objectSource = (JSONObject) o;
                            JSONObject objectTarget = mergedJson.getJSONObject(key);
                            JSONObject subJson = merge(objectSource, objectTarget);
                            mergedJson.put(key, subJson);
                        } else if (o instanceof JSONArray) {
                            JSONArray sourceArray = (JSONArray) o;
                            JSONArray targetArray = mergedJson.getJSONArray(key);

                            for (int i = 0; i < sourceArray.length(); i++) {
                                targetArray.put(sourceArray.get(i));
                            }
                        } else {
                            // overwrite
                            mergedJson.put(key, o);
                        }
                    } else { // add directly
                        mergedJson.put(key, o);
                    }
                }

            } else if (source == null && target != null) {
                mergedJson = new JSONObject(target.toString()); // contain the non-null

            } else if (target == null && source != null) {
                mergedJson = new JSONObject(source.toString()); // contain the non-null

            }
        } catch (JSONException e) {
            //
        }
        return mergedJson;
    }
}
