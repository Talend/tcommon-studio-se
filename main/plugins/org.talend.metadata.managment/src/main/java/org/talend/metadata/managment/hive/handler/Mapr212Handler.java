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
package org.talend.metadata.managment.hive.handler;

import java.util.HashMap;
import java.util.Map;

import org.talend.core.model.metadata.IMetadataConnection;

/**
 * created by msjian on 2013-11-7 Detailled comment
 * 
 */
public class Mapr212Handler extends MaprHandler {

    private static final String MAP_MB = "mapred.map.child.java.opts"; //$NON-NLS-1$

    private static final String REDUCE_MB = "mapred.reduce.child.java.opts"; //$NON-NLS-1$

    private static final String MAP_MB_VALUE = "-Xmx512m"; //$NON-NLS-1$

    private static final String REDUCE_MB_VALUE = "-Xmx512m"; //$NON-NLS-1$

    public Mapr212Handler(IMetadataConnection metadataConnection) {
        super(metadataConnection);
    }

    /*
     * (non-Javadoc)
     * 
     * If user didn't fill the property in connection wizard,set a default value for this properties. Requried
     * properties:"mapred.map.child.java.opts";"mapred.reduce.child.java.opts"
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
