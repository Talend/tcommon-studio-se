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
package org.talend.metadata.managment.hive.handler;

import java.util.Map;

import org.talend.core.database.conn.ConnParameterKeys;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.database.PluginConstant;

/**
 * handle some hadoop parameters for hive connection with HDP2.0,it use yarn framework.
 * 
 */
public class HDP200YarnHandler extends HortonWorksHandler implements IHiveYarnHandler {

    /**
     * DOC qiongli HDP200Handler constructor comment.
     * 
     * @param metadataConnection
     */
    public HDP200YarnHandler(IMetadataConnection metadataConnection) {
        super(metadataConnection);
    }

    /*
     * If user didn't fill the property in connection wizard,set a default value for this properties. Requried
     * properties:"mapreduce.framework.name";"yarn.resourcemanager.address";"yarn.application.classpath"
     */
    @Override
    protected Map<String, String> getDefaultHadoopParameters() {
        Map<String, String> map = super.getDefaultHadoopParameters();
        Map<String, String> hadoopPropertiesMap = getHadoopPropertiesMap();
        if (hadoopPropertiesMap.get(MAP_FM_NAME) == null) {
            map.put(MAP_FM_NAME, MAP_FM_NAME_VALUE);
        }
        if (hadoopPropertiesMap.get(YARN_RM_ADDRESS) == null) {
            Object jobTrackerURL = getMetadataConnection().getParameter(ConnParameterKeys.CONN_PARA_KEY_JOB_TRACKER_URL);
            if (jobTrackerURL != null && !PluginConstant.EMPTY_STRING.equals(jobTrackerURL.toString().trim())) {
                map.put(YARN_RM_ADDRESS, jobTrackerURL.toString());
            }
        }
        if (hadoopPropertiesMap.get(YARN_CLASSPATH) == null) {
            map.put(YARN_CLASSPATH, YARN_CLASSPATH_VALUE);
        }
        return map;
    }

}
