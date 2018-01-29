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
 * created by ycbai on Jul 31, 2014 Detailled comment
 *
 */
public enum EHadoopProperties {

    NAMENODE_URI,

    JOBTRACKER,

    RESOURCE_MANAGER,

    RESOURCEMANAGER_SCHEDULER_ADDRESS,

    JOBHISTORY_ADDRESS,

    STAGING_DIRECTORY,

    USE_DATANODE_HOSTNAME,

    NAMENODE_PRINCIPAL,

    JOBTRACKER_PRINCIPAL,

    RESOURCE_MANAGER_PRINCIPAL,

    JOBHISTORY_PRINCIPAL,

    HIVE_PRINCIPAL,

    DATABASE,

    PORT,

    HBASE_MASTER_PRINCIPAL,

    HBASE_REGIONSERVER_PRINCIPAL,

    HBASE_TABLE_NS_MAPPING,

    MAPRDB_MASTER_PRINCIPAL,

    MAPRDB_REGIONSERVER_PRINCIPAL,

    MAPRDB_TABLE_NS_MAPPING,

    CLOUDERA_NAVIGATOR_USERNAME,

    CLOUDERA_NAVIGATOR_PASSWORD,

    CLOUDERA_NAVIGATOR_URL,

    CLOUDERA_NAVIGATOR_METADATA_URL,

    CLOUDERA_NAVIGATOR_CLIENT_URL,

    MAPRTICKET_CLUSTER,

    MAPRTICKET_DURATION,

    MAPR_HOME_DIR,

    HADOOP_LOGIN,

    GOOGLE_PROJECT_ID,

    GOOGLE_CLUSTER_ID,

    GOOGLE_REGION,

    GOOGLE_JARS_BUCKET,

    HD_WEBHCAT_HOSTNAME,

    HD_WEBHCAT_PORT,

    HD_WEBHCAT_USERNAME,

    HD_INSIGHT_USERNAME,

    HD_AZURE_HOSTNAME,

    HD_AZURE_CONTAINER,

    HD_AZURE_USERNAME,

    HD_AZURE_DEPLOYBOLB,

    HD_JOB_RESULT_FOLDER,
    ;

    public String getName() {
        return this.name();
    }

}
