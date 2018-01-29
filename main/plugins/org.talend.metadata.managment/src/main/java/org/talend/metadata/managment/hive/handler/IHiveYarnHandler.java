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

/**
 * the needed common parameters for yarn framework.
 * 
 */
public interface IHiveYarnHandler {

    public final String MAP_FM_NAME = "mapreduce.framework.name"; //$NON-NLS-1$

    public final String MAP_FM_NAME_VALUE = "yarn"; //$NON-NLS-1$

    public final String YARN_RM_ADDRESS = "yarn.resourcemanager.address"; //$NON-NLS-1$

    public final String YARN_MB = "yarn.app.mapreduce.am.resource.mb"; //$NON-NLS-1$

    public final String YARN_MB_VALUE = "1000"; //$NON-NLS-1$

    public final String YARN_CLASSPATH = "yarn.application.classpath"; //$NON-NLS-1$

    public final String YARN_CLASSPATH_VALUE = "$HADOOP_CONF_DIR,$HADOOP_COMMON_HOME/*,$HADOOP_COMMON_HOME/lib/*,$HADOOP_HDFS_HOME/*,$HADOOP_HDFS_HOME/lib/*,$HADOOP_MAPRED_HOME/*,$HADOOP_MAPRED_HOME/lib/*,$YARN_HOME/*,$YARN_HOME/lib/*,$HADOOP_YARN_HOME/*,$HADOOP_YARN_HOME/lib/*,$HADOOP_COMMON_HOME/share/hadoop/common/*,$HADOOP_COMMON_HOME/share/hadoop/common/lib/*,$HADOOP_HDFS_HOME/share/hadoop/hdfs/*,$HADOOP_HDFS_HOME/share/hadoop/hdfs/lib/*,$HADOOP_YARN_HOME/share/hadoop/yarn/*,$HADOOP_YARN_HOME/share/hadoop/yarn/lib/*"; //$NON-NLS-1$

}
