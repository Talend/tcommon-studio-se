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
package org.talend.core.hadoop.conf;

/**
 * created by ycbai on 2015年6月1日 Detailled comment
 *
 */
public enum EHadoopConfs {

    HDFS("HDFS", new String[] { "core-site.xml", "hdfs-site.xml" }), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    MAPREDUCE2("MAPREDUCE2", new String[] { "core-site.xml", "mapred-site.xml" }), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    YARN("YARN", new String[] { "core-site.xml", "yarn-site.xml" }), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    WEBHCAT("WEBHCAT", new String[] { "webhcat-site.xml" }), //$NON-NLS-1$ //$NON-NLS-2$ 

    HIVE("HIVE", new String[] { "mapred-site.xml", "hive-site.xml" }), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    HBASE("HBASE", new String[] { "hdfs-site.xml", "hbase-site.xml" }), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    MAPRDB("MAPRDB", new String[] { "hdfs-site.xml", "hbase-site.xml" }), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    OOZIE("OOZIE", new String[] { "oozie-site.xml" }), //$NON-NLS-1$ //$NON-NLS-2$ 

    SPARK("SPARK", new String[0]), //$NON-NLS-1$

    PIG("PIG", new String[0]), //$NON-NLS-1$

    SQOOP("SQOOP", new String[] { "sqoop-site.xml" }), //$NON-NLS-1$ //$NON-NLS-2$

    STORM("STORM", new String[] { "storm-site.xml" }), //$NON-NLS-1$ //$NON-NLS-2$

    TEZ("TEZ", new String[] { "tez-site.xml" }), //$NON-NLS-1$ //$NON-NLS-2$

    ZOOKEEPER("ZOOKEEPER", new String[0]), //$NON-NLS-1$

    ;

    private String name;

    private String[] confFiles;

    EHadoopConfs(String name, String[] confFiles) {
        this.name = name;
        this.confFiles = confFiles;
    }

    public String getName() {
        return this.name;
    }

    public String[] getConfFiles() {
        return this.confFiles;
    }

    public static EHadoopConfs getConfByName(String name) {
        EHadoopConfs[] confs = values();
        for (EHadoopConfs conf : confs) {
            if (conf.getName().equals(name)) {
                return conf;
            }
        }
        return null;
    }

}
