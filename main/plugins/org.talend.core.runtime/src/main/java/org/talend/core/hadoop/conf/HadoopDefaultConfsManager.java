// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.hadoop.conf;

import org.talend.core.runtime.conf.DefaultConfsManager;

/**
 * created by ycbai on Jul 31, 2014 Detailled comment
 *
 */
public class HadoopDefaultConfsManager extends DefaultConfsManager {

    private final static String CONF_FILE_PATH = "resources/hadoop/hadoop_default_confs.json"; //$NON-NLS-1$

    private final static String ROOT_ELEMENT = "HADOOP"; //$NON-NLS-1$

    private static HadoopDefaultConfsManager manager = new HadoopDefaultConfsManager(CONF_FILE_PATH);

    private HadoopDefaultConfsManager(String confFilePath) {
        super(CONF_FILE_PATH);
    }

    public static synchronized final HadoopDefaultConfsManager getInstance() {
        return manager;
    }

    @Override
    protected String getRootElement() {
        return ROOT_ELEMENT;
    }

}
