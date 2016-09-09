// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

    MAPRDB_MASTER_PRINCIPAL,

    MAPRDB_REGIONSERVER_PRINCIPAL,

    CLOUDERA_NAVIGATOR_USERNAME,

    CLOUDERA_NAVIGATOR_PASSWORD,

    CLOUDERA_NAVIGATOR_URL,

    CLOUDERA_NAVIGATOR_METADATA_URL,

    CLOUDERA_NAVIGATOR_CLIENT_URL,

    MAPRTICKET_CLUSTER,

    MAPRTICKET_DURATION,

    MAPR_HOME_DIR,

    HADOOP_LOGIN,

    ;

    public String getName() {
        return this.name();
    }

}
