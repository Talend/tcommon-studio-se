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
package org.talend.metadata.managment.connection.manager;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import org.talend.core.GlobalServiceRegister;
import org.talend.core.classloader.ClassLoaderFactory;
import org.talend.core.classloader.DynamicClassLoader;
import org.talend.core.database.EDatabase4DriverClassName;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.database.conn.ConnParameterKeys;
import org.talend.core.hadoop.IHadoopDistributionService;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.runtime.hd.IHDistribution;
import org.talend.core.runtime.hd.IHDistributionVersion;

/**
 * DOC ggu class global comment. Detailed comment
 */
public class ImpalaConnectionManager extends DataBaseConnectionManager {

    private final static ImpalaConnectionManager manager = new ImpalaConnectionManager();

    private ImpalaConnectionManager() {
    }

    public static ImpalaConnectionManager getInstance() {
        return manager;
    }

    public void checkConnection(IMetadataConnection metadataConn) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, SQLException {
        createConnection(metadataConn);
    }

    public Connection createConnection(IMetadataConnection metadataConn) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, SQLException {
        Class<?> driver = Class.forName(EDatabase4DriverClassName.IMPALA.getDriverClass(), true, getClassLoader(metadataConn));
        Driver hiveDriver = (Driver) driver.newInstance();

        // 3. Try to connect by driver
        Properties info = new Properties();
        String username = metadataConn.getUsername();
        String password = metadataConn.getPassword();

        username = username != null ? username : ""; //$NON-NLS-1$
        password = password != null ? password : "";//$NON-NLS-1$
        info.setProperty("user", username);//$NON-NLS-1$
        info.setProperty("password", password);//$NON-NLS-1$

        String url = metadataConn.getUrl();
        Connection conn = hiveDriver.connect(url, info);
        return conn;
    }

    private IHadoopDistributionService getHadoopDistributionService() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IHadoopDistributionService.class)) {
            return (IHadoopDistributionService) GlobalServiceRegister.getDefault().getService(IHadoopDistributionService.class);
        }
        return null;
    }

    private ClassLoader getClassLoader(IMetadataConnection metadataConn) {
        IHadoopDistributionService hadoopService = getHadoopDistributionService();
        if (hadoopService != null) {
            String distribution = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_IMPALA_DISTRIBUTION);
            String version = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_IMPALA_VERSION);

            IHDistribution impalaDistribution = hadoopService.getImpalaDistributionManager().getDistribution(distribution, false);
            if (impalaDistribution != null) {
                String impalaIndex = EDatabaseTypeName.IMPALA.getProduct() + ClassLoaderFactory.KEY_SEPARATOR
                        + impalaDistribution.getName();
                if (impalaDistribution.useCustom()) {
                    String jarsStr = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_HADOOP_CUSTOM_JARS);
                    String index = "CustomImpala" + ClassLoaderFactory.KEY_SEPARATOR + impalaIndex + ClassLoaderFactory.KEY_SEPARATOR + metadataConn.getId(); //$NON-NLS-1$
                    DynamicClassLoader classLoader = ClassLoaderFactory.getCustomClassLoader(index, jarsStr);
                    if (classLoader != null) {
                        return classLoader;
                    }
                } else {
                    IHDistributionVersion impalaVersion = impalaDistribution.getHDVersion(version, false);
                    if (impalaVersion != null) {
                        boolean isKeb = Boolean.valueOf((String) metadataConn
                                .getParameter(ConnParameterKeys.CONN_PARA_KEY_USE_KRB));
                        DynamicClassLoader classLoader = ClassLoaderFactory.getClassLoader(impalaIndex
                                + ClassLoaderFactory.KEY_SEPARATOR + impalaVersion.getVersion() + (isKeb ? "?USE_KRB" : ""));//$NON-NLS-1$//$NON-NLS-2$

                        // if not work for extension point, try modules from hadoop distribution
                        if (classLoader == null) {
                            classLoader = ClassLoaderFactory.getClassLoader(impalaVersion);
                        }
                        if (classLoader != null) {
                            return classLoader;
                        }
                    }
                }

            }

        }

        return this.getClass().getClassLoader();

    }
}
