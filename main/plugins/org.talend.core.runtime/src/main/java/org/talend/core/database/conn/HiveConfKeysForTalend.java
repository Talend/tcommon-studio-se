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
package org.talend.core.database.conn;

/**
 * These keys in this enum are the key of configuration properties for Hive from <code>HiveConf</code>. Created by
 * Marvin Wang on Nov 22, 2012.
 */
public enum HiveConfKeysForTalend {

    /**
     * This is for the url of name node, the value of the key is <code>fs.default.name</code>.
     */
    HIVE_CONF_KEY_FS_DEFAULT_NAME("fs.default.name"), //$NON-NLS-1$

    HIVE_CONF_KEY_HADOOP_USER_NAME("HADOOP_USER_NAME"), //$NON-NLS-1$

    /**
     * This key is for job traker url, value of the key is <code>mapred.job.tracker</code>.
     */
    HIVE_CONF_KEY_MAPRED_JOB_TRACKER("mapred.job.tracker"), //$NON-NLS-1$

    HIVE_CONF_KEY_MAPREDUCE_FRAMEWORK_NAME("mapreduce.framework.name"), //$NON-NLS-1$

    HIVE_CONF_KEY_YARN_RESOURCEMANAGER_ADDRESS("yarn.resourcemanager.address"), //$NON-NLS-1$

    HIVE_CONF_KEY_JDO_CONNECTION_URL("javax.jdo.option.ConnectionURL"), //$NON-NLS-1$

    HIVE_CONF_KEY_JDO_CONNECTION_USERNAME("javax.jdo.option.ConnectionUserName"), //$NON-NLS-1$

    HIVE_CONF_KEY_JDO_CONNECTION_PASSWORD("javax.jdo.option.ConnectionPassword"), //$NON-NLS-1$

    HIVE_CONF_KEY_JDO_CONNECTION_DRIVERNAME("javax.jdo.option.ConnectionDriverName"), //$NON-NLS-1$

    HIVE_CONF_KEY_HIVE_METASTORE_LOCAL("hive.metastore.local"), //$NON-NLS-1$

    HIVE_CONF_KEY_HIVE_METASTORE_URI("hive.metastore.uris"), //$NON-NLS-1$

    HIVE_CONF_KEY_HIVE_METASTORE_EXECUTE_SETUGI("hive.metastore.execute.setugi"), //$NON-NLS-1$

    HIVE_CONF_KEY_TALEND_HIVE_MODE("talend.hive.mode"), //$NON-NLS-1$

    HIVE_CONF_KEY_HIVE_METASTORE_SASL_ENABLED("hive.metastore.sasl.enabled"), //$NON-NLS-1$

    HIVE_CONF_KEY_HIVE_SECURITY_AUTHORIZATION_ENABLED("hive.security.authorization.enabled"), //$NON-NLS-1$

    HIVE_CONF_KEY_HIVE_METASTORE_KERBEROS_PRINCIPAL("hive.metastore.kerberos.principal"), //$NON-NLS-1$

    ;

    private String key;

    HiveConfKeysForTalend(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    @Override
    public String toString() {
        return this.key;
    }

}
