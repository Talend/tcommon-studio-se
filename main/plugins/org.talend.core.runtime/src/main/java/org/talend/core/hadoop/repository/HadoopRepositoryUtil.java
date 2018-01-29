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
package org.talend.core.hadoop.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.hadoop.IHadoopClusterService;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.service.IMetadataManagmentUiService;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.utils.json.JSONArray;
import org.talend.utils.json.JSONException;
import org.talend.utils.json.JSONObject;

/**
 * created by ycbai on 2013-10-24 Detailled comment
 *
 */
public class HadoopRepositoryUtil {

    /**
     * DOC ycbai Comment method "getHadoopPropertiesJsonStr".
     *
     * <p>
     * Get the json string type of hadoop properties from list type.
     * </p>
     *
     * @param properties
     * @return
     * @throws JSONException
     */
    public static String getHadoopPropertiesJsonStr(List<Map<String, Object>> properties) {
        JSONArray jsonArr = null;
        try {
            jsonArr = new JSONArray();
            if (properties != null && properties.size() > 0) {
                for (Map<String, Object> map : properties) {
                    JSONObject object = new JSONObject();
                    Iterator<String> it = map.keySet().iterator();
                    while (it.hasNext()) {
                        String key = it.next();
                        object.put(key, map.get(key));
                    }
                    jsonArr.put(object);
                }
            }
        } catch (JSONException e) {
            ExceptionHandler.process(e);
        }

        return jsonArr.toString();
    }

    public static List<Map<String, Object>> getHadoopPropertiesList(String propertiesJsonStr) {
        return getHadoopPropertiesList(propertiesJsonStr, false, false);
    }

    /**
     * DOC ycbai Comment method "getHadoopPropertiesList".
     *
     * <p>
     * Get the list type of hadoop properties from json string.
     * </p>
     *
     * @param propertiesJsonStr
     * @param includeQuotes
     * @return
     * @throws JSONException
     */
    public static List<Map<String, Object>> getHadoopPropertiesList(String propertiesJsonStr, boolean isContextMode,
            boolean includeQuotes) {
        List<Map<String, Object>> properties = new ArrayList<>();
        try {
            if (StringUtils.isNotEmpty(propertiesJsonStr)) {
                JSONArray jsonArr = new JSONArray(propertiesJsonStr);
                for (int i = 0; i < jsonArr.length(); i++) {
                    HashMap<String, Object> map = new HashMap<>();
                    JSONObject object = jsonArr.getJSONObject(i);
                    Iterator<String> it = object.keys();
                    while (it.hasNext()) {
                        String key = it.next();
                        String value = String.valueOf(object.get(key));
                        if (includeQuotes) {
                            if (isContextMode && "VALUE".equals(key)) { //$NON-NLS-1$
                                value = TalendQuoteUtils.removeQuotesIfExist(value);
                            } else {
                                value = TalendQuoteUtils.addQuotesIfNotExist(value);
                            }
                        } else {
                            value = TalendQuoteUtils.removeQuotesIfExist(value);
                        }
                        map.put(key, value);
                    }
                    properties.add(map);
                }
            }
        } catch (JSONException e) {
            ExceptionHandler.process(e);
        }

        return properties;
    }

    /**
     * DOC ycbai Comment method "getHadoopPropertiesFullList".
     *
     * <p>
     * Get the full list type of hadoop properties (with the parent properties of the hadoop cluster it belongs) from
     * json string.
     * </p>
     *
     * @param connection
     * @param propertiesJsonStr
     * @param includeQuotes
     * @return
     */
    public static List<Map<String, Object>> getHadoopPropertiesFullList(Connection connection, String propertiesJsonStr,
            boolean includeQuotes) {
        return getHadoopPropertiesFullList(connection, propertiesJsonStr, includeQuotes, false);
    }

    public static List<Map<String, Object>> getHadoopPropertiesFullList(Connection connection, String propertiesJsonStr,
            boolean includeQuotes, boolean useOriginalValue) {
        IHadoopClusterService hadoopClusterService = getHadoopClusterService();
        List<Map<String, Object>> parentProperties = null;
        if (hadoopClusterService != null) {
            Connection hadoopClusterConnection = hadoopClusterService.getHadoopClusterConnectionBySubConnection(connection);
            if (hadoopClusterConnection != null) {
                String hadoopClusterPropertiesStrs = hadoopClusterService.getHadoopClusterProperties(connection);
                parentProperties = getHadoopProperties(hadoopClusterConnection, hadoopClusterPropertiesStrs, includeQuotes,
                        useOriginalValue);
            }
        }
        List<Map<String, Object>> properties = getHadoopProperties(connection, propertiesJsonStr, includeQuotes, useOriginalValue);
        Map<String, Map<String, Object>> propertiesMap = new HashMap<>();
        for (Map<String, Object> proMap : properties) {
            String property = String.valueOf(proMap.get("PROPERTY")); //$NON-NLS-1$
            propertiesMap.put(property, proMap);
        }
        if (parentProperties != null) {
            for (Map<String, Object> parentProMap : parentProperties) {
                String property = String.valueOf(parentProMap.get("PROPERTY")); //$NON-NLS-1$
                if (!propertiesMap.containsKey(property)) {
                    propertiesMap.put(property, parentProMap);
                    properties.add(parentProMap);
                }
            }
        }

        return properties;
    }

    private static List<Map<String, Object>> getHadoopProperties(Connection connection, String propertiesJsonStr,
            boolean includeQuotes, boolean useOriginalValue) {
        List<Map<String, Object>> properties = null;
        IMetadataManagmentUiService mmUIService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IMetadataManagmentUiService.class)) {
            mmUIService = (IMetadataManagmentUiService) GlobalServiceRegister.getDefault().getService(
                    IMetadataManagmentUiService.class);
        }
        ContextType contextType = null;
        if (mmUIService != null) {
            contextType = mmUIService.getContextTypeForContextMode(connection, null, true);
        }
        if (useOriginalValue && contextType != null) {
            properties = getHadoopPropertiesWithOriginalValue(propertiesJsonStr, contextType, includeQuotes);
        } else {
            properties = getHadoopPropertiesList(propertiesJsonStr, contextType != null, includeQuotes);
        }
        return properties;
    }

    public static boolean useClouderaNavi(Connection hadoopSubConnection) {
        IHadoopClusterService hadoopClusterService = getHadoopClusterService();
        if (hadoopClusterService != null) {
            return hadoopClusterService.useClouderaNavi(hadoopSubConnection);
        }
        return false;
    }

    public static String getClouderaNaviUserName(Connection hadoopSubConnection) {
        IHadoopClusterService hadoopClusterService = getHadoopClusterService();
        if (hadoopClusterService != null) {
            return hadoopClusterService.getClouderaNaviUserName(hadoopSubConnection);
        }
        return null;
    }

    public static String getClouderaNaviPassword(Connection hadoopSubConnection) {
        IHadoopClusterService hadoopClusterService = getHadoopClusterService();
        if (hadoopClusterService != null) {
            return hadoopClusterService.getClouderaNaviPassword(hadoopSubConnection);
        }
        return null;
    }

    public static String getClouderaNaviUrl(Connection hadoopSubConnection) {
        IHadoopClusterService hadoopClusterService = getHadoopClusterService();
        if (hadoopClusterService != null) {
            return hadoopClusterService.getClouderaNaviUrl(hadoopSubConnection);
        }
        return null;
    }

    public static String getClouderaNaviMetadataUrl(Connection hadoopSubConnection) {
        IHadoopClusterService hadoopClusterService = getHadoopClusterService();
        if (hadoopClusterService != null) {
            return hadoopClusterService.getClouderaNaviMetadataUrl(hadoopSubConnection);
        }
        return null;
    }

    public static String getClouderaNaviClientUrl(Connection hadoopSubConnection) {
        IHadoopClusterService hadoopClusterService = getHadoopClusterService();
        if (hadoopClusterService != null) {
            return hadoopClusterService.getClouderaNaviClientUrl(hadoopSubConnection);
        }
        return null;
    }

    public static boolean clouderaNaviAutoCommit(Connection hadoopSubConnection) {
        IHadoopClusterService hadoopClusterService = getHadoopClusterService();
        if (hadoopClusterService != null) {
            return hadoopClusterService.clouderaNaviAutoCommit(hadoopSubConnection);
        }
        return false;
    }

    public static boolean clouderaNaviDisableSSL(Connection hadoopSubConnection) {
        IHadoopClusterService hadoopClusterService = getHadoopClusterService();
        if (hadoopClusterService != null) {
            return hadoopClusterService.clouderaNaviDisableSSL(hadoopSubConnection);
        }
        return false;
    }

    public static boolean clouderaNaviDieOnError(Connection hadoopSubConnection) {
        IHadoopClusterService hadoopClusterService = getHadoopClusterService();
        if (hadoopClusterService != null) {
            return hadoopClusterService.clouderaNaviDieOnError(hadoopSubConnection);
        }
        return false;
    }

    public static String getOriginalValueOfProperties(String propertiesStrings, ContextType contextType) {
        return getOriginalValueOfProperties(propertiesStrings, contextType, false);
    }

    public static String getOriginalValueOfProperties(String propertiesStrings, ContextType contextType, boolean includeQuotes) {
        String originalValueOfProperties = propertiesStrings;
        if (propertiesStrings != null && !propertiesStrings.isEmpty()) {
            List<Map<String, Object>> propertiesList = getHadoopPropertiesWithOriginalValue(propertiesStrings, contextType,
                    includeQuotes);
            if (propertiesList != null && !propertiesList.isEmpty()) {
                originalValueOfProperties = getHadoopPropertiesJsonStr(propertiesList);
            }
        }
        return originalValueOfProperties;
    }

    /**
     * DOC ycbai Comment method "getHadoopPropertiesWithOriginalValue".
     *
     * <p>
     * Get hadoop properties list which convert conext value to original value if needed.
     * </p>
     *
     * @param propertiesStrings
     * @param contextType
     * @param includeQuotes
     * @return
     */
    public static List<Map<String, Object>> getHadoopPropertiesWithOriginalValue(String propertiesStrings,
            ContextType contextType, boolean includeQuotes) {
        boolean isContextMode = contextType == null ? false : true;
        List<Map<String, Object>> propertiesList = getHadoopPropertiesList(propertiesStrings, isContextMode, includeQuotes);
        if (!isContextMode) {
            return propertiesList;
        }

        List<Map<String, Object>> newPropertiesList = new ArrayList<>();
        for (Map<String, Object> propMap : propertiesList) {
            Map<String, Object> newPropMap = new HashMap<>();
            Iterator<Map.Entry<String, Object>> propMapEntryIter = propMap.entrySet().iterator();
            while (propMapEntryIter.hasNext()) {
                Entry<String, Object> propMapEntry = propMapEntryIter.next();
                String propKey = propMapEntry.getKey();
                Object propValue = propMapEntry.getValue();
                if (propKey != null && propValue != null) {
                    String newValue = ContextParameterUtils.getOriginalValue(contextType, String.valueOf(propValue));
                    newPropMap.put(propKey, newValue);
                }
            }
            if (!newPropMap.isEmpty()) {
                newPropertiesList.add(newPropMap);
            }
        }

        return newPropertiesList;
    }

    /**
     * @return an instance of {@link IHadoopClusterService} if it is loaded. Else return 'null'.
     */
    public static IHadoopClusterService getHadoopClusterService() {
        IHadoopClusterService hadoopClusterService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IHadoopClusterService.class)) {
            hadoopClusterService = (IHadoopClusterService) GlobalServiceRegister.getDefault().getService(
                    IHadoopClusterService.class);
        }
        return hadoopClusterService;
    }

}
