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

import org.apache.log4j.Logger;
import org.talend.core.model.metadata.IMetadataConnection;

/**
 * created by xqliu on 2014-2-27 Detailled comment
 * 
 */
public class CDH5YarnHandler extends ClouderaHandler implements IHiveYarnHandler {

    private static Logger log = Logger.getLogger(CDH5YarnHandler.class);

    private final String MAP_MB = "mapreduce.map.memory.mb"; //$NON-NLS-1$

    private final String REDUCE_MB = "mapreduce.reduce.memory.mb"; //$NON-NLS-1$

    private final String MAP_MB_VALUE = "1000"; //$NON-NLS-1$

    private final String REDUCE_MB_VALUE = "1000"; //$NON-NLS-1$

    public CDH5YarnHandler(IMetadataConnection metadataConnection) {
        super(metadataConnection);
    }

    /*
     * (non-Javadoc)
     * 
     * If user didn't fill the property in connection wizard,set a default value for this properties. Requried
     * properties:"yarn.application.classpath";"yarn.app.mapreduce.am.resource.mb";"mapreduce.map.memory.mb";
     * "mapreduce.reduce.memory.mb"
     */
    @Override
    protected Map<String, String> getDefaultHadoopParameters() {
        Map<String, String> map = super.getDefaultHadoopParameters();
        Map<String, String> hadoopPropertiesMap = getHadoopPropertiesMap();
        if (hadoopPropertiesMap.get(MAP_MB) == null) {
            map.put(MAP_MB, MAP_MB_VALUE);
        }
        if (hadoopPropertiesMap.get(REDUCE_MB) == null) {
            map.put(REDUCE_MB, REDUCE_MB_VALUE);
        }
        if (hadoopPropertiesMap.get(YARN_MB) == null) {
            map.put(YARN_MB, YARN_MB_VALUE);
        }
        if (hadoopPropertiesMap.get(YARN_CLASSPATH) == null) {
            map.put(YARN_CLASSPATH, YARN_CLASSPATH_VALUE);
        }
        log.info("YARN_CLASSPATH=" + map.get(YARN_CLASSPATH));
        log.info("MAP_FM_NAME=" + map.get(MAP_FM_NAME));
        return map;
    }
}
