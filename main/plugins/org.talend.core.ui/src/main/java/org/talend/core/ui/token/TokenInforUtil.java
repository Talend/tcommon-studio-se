// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.ui.token;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;

/**
 * ggu class global comment. Detailled comment
 */
public final class TokenInforUtil {

    /**
     * 
     * calc the average to x.x. for example, 5/3,should be 1.8
     */
    public static String calcAverageToStr(int total, int per) {
        return String.valueOf(calcAverage(total, per));
    }

    public static float calcAverage(int total, int per) {
        if (per > 0 && total > 0) {
            BigDecimal bd = new BigDecimal(1.0f * total / per);
            bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
            return bd.floatValue();
        }
        return 0;
    }

    /**
     * 
     * ggu Comment method "getDateAfter".
     * 
     * add the days after date
     */
    public static Date getDateAfter(Date date, int days) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + days);
        return now.getTime();
    }

    /**
     * 
     * ggu Comment method "convertTopComponents".
     * 
     * convert the map to json, and only with the max values.
     */
    public static JSONObject convertTopComponents(Map<String, Integer> numComponentMap, final int max) throws JSONException {
        JSONObject topComponentsObject = new JSONObject();
        if (numComponentMap != null && !numComponentMap.isEmpty()) {
            List<Integer> numList = new ArrayList(new HashSet(numComponentMap.values()));
            Collections.sort(numList);
            if (numList.size() > max) {
                List<Integer> tmpList = new ArrayList<Integer>();
                for (int i = 0; i < max; i++) {
                    tmpList.add(numList.get(i));
                }
                numList = tmpList;
            }
            for (String name : numComponentMap.keySet()) {
                Integer num = numComponentMap.get(name);
                if (num != null && numList.contains(num)) { // is top20
                    topComponentsObject.put(name, num);
                    if (topComponentsObject.length() > max) {
                        break;
                    }
                }
            }
        }
        return topComponentsObject;
    }

    public static JSONArray convertTopComponentsArray(Map<String, Integer> numComponentMap, final int max) throws JSONException {
        JSONArray topComponentsArray = new JSONArray();
        JSONObject topComponentsObject = convertTopComponents(numComponentMap, max);
        Iterator<String> keys = topComponentsObject.keys();
        while (keys.hasNext()) {
            topComponentsArray.put(keys.next());
        }
        return topComponentsArray;
    }

    public static JSONObject mergeJSON(JSONObject source, JSONObject target) throws JSONException {
        Iterator<String> keys = source.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object o = source.get(key);
            if (target.has(key)) {
                // concatenate the data
                if (o instanceof Integer) {
                    // need to add to the total
                    int nbSource = source.getInt(key);
                    int nbTarget = target.getInt(key);
                    target.put(key, nbSource + nbTarget);
                } else if (o instanceof JSONObject) {
                    JSONObject objectSource = (JSONObject) o;
                    JSONObject objectTarget = target.getJSONObject(key);
                    mergeJSON(objectSource, objectTarget);
                } else if (o instanceof JSONArray) {
                    JSONArray sourceArray = (JSONArray) o;
                    JSONArray targetArray = target.getJSONArray(key);

                    Set<Object> data = new HashSet<>();
                    for (int i = 0; i < sourceArray.length(); i++) {
                        data.add(sourceArray.get(i));
                    }
                    for (int i = 0; i < targetArray.length(); i++) {
                        data.add(targetArray.get(i));
                    }
                    targetArray = new JSONArray();
                    Map<String,List<JSONObject>> objectMap = new HashMap<String,List<JSONObject>>();
                    for (Object obj : data) {
                        if((obj instanceof JSONObject) && ((JSONObject)obj).get("component_name")!=null){//$NON-NLS-1$
                            List<JSONObject> dataList = new ArrayList<JSONObject>();
                            String componentName = (String) ((JSONObject)obj).get("component_name");//$NON-NLS-1$
                            if(objectMap.containsKey(componentName)){
                                dataList =  objectMap.get(componentName);
                                dataList.add((JSONObject)obj);
                            }else{
                                dataList.add((JSONObject)obj);
                                objectMap.put(componentName, dataList);
                            }
                        }else{
                            targetArray.put(obj);
                        }
                    }
                    target.put(key, targetArray);
                   for(String objKey : objectMap.keySet()){
                       List<JSONObject> dataList = objectMap.get(objKey);
                       if(dataList.size()>1){
                           JSONObject targetObj = dataList.get(0);
                           for(int i=1;i<dataList.size();i++){
                               JSONObject obj = dataList.get(i);
                               targetObj = mergeJSON((JSONObject)obj, targetObj);
                           }
                           targetArray.put(targetObj);
                           target.put(key, targetArray);
                       }else{
                           for (Object obj : dataList) {
                               targetArray.put(obj);
                           }
                           target.put(key, targetArray);
                       }
                   }
                } else {
                    // for simple string / other data
                    target.put(key, o);
                }
            } else {
                target.put(key, o);
            }
        }
        return target;
    }
}
