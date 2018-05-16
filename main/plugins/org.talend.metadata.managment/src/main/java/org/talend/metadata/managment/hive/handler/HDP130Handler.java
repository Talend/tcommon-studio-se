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

import java.util.HashMap;
import java.util.Map;

import org.talend.core.model.metadata.IMetadataConnection;

/**
 * created by xqliu on 2013-11-1 Detailled comment
 * 
 */
public class HDP130Handler extends HortonWorksHandler {

    private final String MAP_MB = "mapred.job.map.memory.mb"; //$NON-NLS-1$

    private final String REDUCE_MB = "mapred.job.reduce.memory.mb"; //$NON-NLS-1$

    private final String MAP_MB_VALUE = "1000"; //$NON-NLS-1$

    private final String REDUCE_MB_VALUE = "1000"; //$NON-NLS-1$

    public HDP130Handler(IMetadataConnection metadataConnection) {
        super(metadataConnection);
    }

    /*
     * (non-Javadoc)
     * 
     * If user didn't fill the property in connection wizard,set a default value for this properties. Requried
     * properties:"mapred.job.map.memory.mb";"mapred.job.reduce.memory.mb"
     */
    @Override
    protected Map<String, String> getDefaultHadoopParameters() {
        Map<String, String> map = new HashMap<String, String>();
        Map<String, String> hadoopPropertiesMap = getHadoopPropertiesMap();
        if (hadoopPropertiesMap.get(MAP_MB) == null) {
            map.put(MAP_MB, MAP_MB_VALUE);
        }
        if (hadoopPropertiesMap.get(REDUCE_MB) == null) {
            map.put(REDUCE_MB, REDUCE_MB_VALUE);
        }
        return map;
    }
}
