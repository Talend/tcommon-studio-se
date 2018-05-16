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
package org.talend.metadata.managment.ui.wizard.metadata.hive.creator;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.CoreException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.database.conn.ConnParameterKeys;
import org.talend.core.database.conn.DatabaseConnStrUtil;
import org.talend.core.database.conn.template.DbConnStrForHive;
import org.talend.core.database.conn.template.EDatabaseConnTemplate;
import org.talend.core.hadoop.EHadoopCategory;
import org.talend.core.hadoop.IHadoopDistributionService;
import org.talend.core.hadoop.conf.EHadoopConfProperties;
import org.talend.core.hadoop.conf.EHadoopConfs;
import org.talend.core.hadoop.conf.EHadoopProperties;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.connection.hive.HiveModeInfo;
import org.talend.core.model.metadata.connection.hive.HiveServerVersionInfo;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.runtime.hd.IHDistribution;
import org.talend.core.runtime.hd.IHDistributionVersion;
import org.talend.core.runtime.hd.hive.HiveMetadataHelper;
import org.talend.metadata.managment.creator.AbstractHadoopDBConnectionCreator;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;

/**
 * created by ycbai on 2015年6月29日 Detailled comment
 *
 */
public class HiveConnectionCreator extends AbstractHadoopDBConnectionCreator {

    @Override
    public ConnectionItem create(Map<String, Map<String, String>> initParams) throws CoreException {
        DatabaseConnection connection = ConnectionFactory.eINSTANCE.createDatabaseConnection();
        Property connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
        setPropertyParameters(connectionProperty);

        DatabaseConnectionItem connectionItem = PropertiesFactory.eINSTANCE.createDatabaseConnectionItem();
        connectionItem.setProperty(connectionProperty);
        connectionItem.setConnection(connection);

        Map<String, String> paramsMap = new HashMap<>();
        retrieveCommonParameters(paramsMap);
        retrieveConnParameters(initParams, paramsMap);
        setParameters(connection, paramsMap);
        initializeConnectionParameters(connection);
        fillDefaultValues(connection);

        return connectionItem;
    }

    @Override
    public String getTypeName() {
        return EHadoopConfs.HIVE.getName();
    }

    @Override
    protected void retrieveCommonParameters(Map<String, String> paramsMap) {
        super.retrieveCommonParameters(paramsMap);
        paramsMap.put(ConnParameterKeys.CONN_PARA_KEY_DB_TYPE, EDatabaseConnTemplate.HIVE.getDBTypeName());
        paramsMap.put(ConnParameterKeys.CONN_PARA_KEY_DB_PRODUCT, EDatabaseTypeName.HIVE.getProduct());
        String userName = paramsMap.get(ConnParameterKeys.CONN_PARA_KEY_USERNAME);
        if (userName != null && relativeHadoopClusterItem != null) {
            Connection connection = ((ConnectionItem) relativeHadoopClusterItem).getConnection();
            paramsMap.put(ConnParameterKeys.CONN_PARA_KEY_USERNAME,
                    ConnectionContextHelper.getParamValueOffContext(connection, userName));
        }
    }

    private void retrieveConnParameters(Map<String, Map<String, String>> initParams, Map<String, String> paramsMap) {
        if (paramsMap == null) {
            return;
        }
        Map<String, String> params = initParams.get(getTypeName());
        if (params != null) {
            String hivePrincipal = params.get(EHadoopConfProperties.HIVE_SERVER2_AUTHENTICATION_KERBEROS_PRINCIPAL.getName());
            if (StringUtils.isNotEmpty(hivePrincipal)) {
                paramsMap.put(ConnParameterKeys.HIVE_AUTHENTICATION_HIVEPRINCIPLA, hivePrincipal);
            }
            String metastoreurl = params.get(EHadoopConfProperties.HIVE_METASTORE_URIS.getName());
            if (StringUtils.isNotEmpty(metastoreurl)) {
                paramsMap.put(ConnParameterKeys.HIVE_AUTHENTICATION_METASTOREURL, metastoreurl);
            }
            String username = params.get(EHadoopConfProperties.JAVAX_JDO_OPTION_CONNECTIONUSERNAME.getName());
            if (StringUtils.isNotEmpty(username)) {
                paramsMap.put(ConnParameterKeys.HIVE_AUTHENTICATION_USERNAME, username);
            }
            String password = params.get(EHadoopConfProperties.JAVAX_JDO_OPTION_CONNECTIONPASSWORD.getName());
            if (StringUtils.isNotEmpty(password)) {
                paramsMap.put(ConnParameterKeys.HIVE_AUTHENTICATION_PASSWORD, password);
            }
        }
    }

    private void fillDefaultValues(DatabaseConnection connection) {
        String distribution = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_DISTRIBUTION);
        String version = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_VERSION);

        // server
        String[] hiveServersDisplay = HiveMetadataHelper.getHiveServersDisplay(distribution, version, false);
        if (hiveServersDisplay == null || hiveServersDisplay.length == 0) {
            return;
        }
        HiveServerVersionInfo server = HiveServerVersionInfo.getByDisplay(hiveServersDisplay[0]);
        String hiveServer = server.getKey();
        connection.getParameters().put(ConnParameterKeys.HIVE_SERVER_VERSION, hiveServer);

        // model
        String[] hiveModesDisplay = HiveMetadataHelper.getHiveModesDisplay(distribution, version, hiveServer, false);
        if (hiveModesDisplay == null || hiveModesDisplay.length == 0) {
            return;
        }
        HiveModeInfo hiveMode = HiveModeInfo.getByDisplay(hiveModesDisplay[0]);
        boolean isEmbeddedMode = (hiveMode == HiveModeInfo.EMBEDDED);
        connection.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_MODE, hiveMode.getName());
        IHadoopDistributionService hadoopDistributionService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IHadoopDistributionService.class)) {
            hadoopDistributionService = (IHadoopDistributionService) GlobalServiceRegister.getDefault().getService(
                    IHadoopDistributionService.class);
        }
        if (hadoopDistributionService == null) {
            return;
        }
        IHDistribution hiveDistribution = hadoopDistributionService.getHiveDistributionManager().getDistribution(distribution,
                false);
        if (hiveDistribution == null) {
            return;
        }
        IHDistributionVersion hiveVersion = hiveDistribution.getHDVersion(version, false);
        if (hiveVersion == null) {
            return;
        }

        if (StringUtils.isEmpty(connection.getSID())) {
            String defaultDatabase = hiveVersion.getDefaultConfig(distribution, EHadoopCategory.HIVE.getName(),
                    EHadoopProperties.DATABASE.getName());
            if (StringUtils.isNotEmpty(defaultDatabase)) {
                connection.setSID(defaultDatabase);
            }
        }
        if (StringUtils.isEmpty(connection.getPort())) {
            String defaultPort = null;
            if (isEmbeddedMode) {
                defaultPort = hiveVersion.getDefaultConfig(distribution, EHadoopCategory.HIVE.getName(),
                        HiveModeInfo.EMBEDDED.getName(), EHadoopProperties.PORT.getName());
            } else {
                defaultPort = hiveVersion.getDefaultConfig(distribution, EHadoopCategory.HIVE.getName(),
                        HiveModeInfo.STANDALONE.getName(), EHadoopProperties.PORT.getName());
            }
            if (StringUtils.isNotEmpty(defaultPort)) {
                connection.setPort(defaultPort);
            }
        }
        if (StringUtils.isEmpty(connection.getURL())) {
            String template = null;
            if (HiveServerVersionInfo.HIVE_SERVER_2.getKey().equals(hiveServer)) {
                template = DbConnStrForHive.URL_HIVE_2_TEMPLATE;
            } else {
                template = DbConnStrForHive.URL_HIVE_1_TEMPLATE;
            }
            String url = DatabaseConnStrUtil.getHiveURLString(connection, connection.getServerName(), connection.getPort(),
                    connection.getSID(), template);
            if (StringUtils.isNotEmpty(url)) {
                connection.setURL(url);
            }
        }
    }
}
