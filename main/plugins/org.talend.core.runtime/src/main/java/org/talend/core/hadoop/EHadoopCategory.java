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
package org.talend.core.hadoop;

/**
 * created by ycbai on Jul 31, 2014 Detailled comment
 *
 */
public enum EHadoopCategory {

    HADOOP_CLUSTER,

    HDFS,

    MAP_REDUCE,

    YARN,

    HCATALOG,

    HIVE,

    HBASE,

    MAPRDB,

    OOZIE,

    CUSTOM

    ;

    public String getName() {
        return this.name();
    }

}
