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
package org.talend.core.hadoop.version.custom;

/**
 * created by ycbai on 2013-3-13 Detailled comment
 * 
 */
public enum ECustomVersionGroup {

    COMMON,

    HIVE,

    HBASE,

    MAPRDB,

    PIG,

    PIG_HBASE,
    PIG_HCATALOG,

    MAP_REDUCE,

    SPARK,

    SPARK_STREAMING,

    ALL;

    private final static String PREFIX = "HADOOP_CUSTOM_VERSION:"; //$NON-NLS-1$

    public String getName() {
        return PREFIX + name();
    }

}
