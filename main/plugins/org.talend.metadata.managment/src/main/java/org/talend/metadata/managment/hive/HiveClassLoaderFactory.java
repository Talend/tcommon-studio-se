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
package org.talend.metadata.managment.hive;

import java.io.File;
import java.util.Set;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.classloader.ClassLoaderFactory;
import org.talend.core.classloader.DynamicClassLoader;
import org.talend.core.database.conn.ConnParameterKeys;
import org.talend.core.hadoop.EHadoopConfigurationJars;
import org.talend.core.hadoop.IHadoopClusterService;
import org.talend.core.hadoop.repository.HadoopRepositoryUtil;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.runtime.hd.IHDistribution;
import org.talend.core.runtime.hd.hive.HiveMetadataHelper;
import org.talend.metadata.managment.connection.manager.DatabaseConnConstants;

/**
 * <pre>
 * This factory is used to get the class loaders for hive by the different index.
 * </pre>
 * 
 * <pre>
 * <li>For hive server one, the index format should be like this: <B>HIVE:HORTONWORKS:HDP_1_0:EMBEDDED</B>. 
 * <li>For hive server two, the index format should be like this: <B>HIVE2:HORTONWORKS:HDP_1_0:EMBEDDED</B>.
 * </pre>
 * 
 * Created by Marvin Wang on Mar 12, 2013.
 */
public class HiveClassLoaderFactory {

    private static HiveClassLoaderFactory instance = null;

    private final static String PATH_SEPARATOR = "/"; //$NON-NLS-1$

    private HiveClassLoaderFactory() {
    }

    /**
     * Gets an instance of <code>HiveClassLoaderFactory</code>, which is synchronized. Added by Marvin Wang on Mar 12,
     * 2013.
     * 
     * @return
     */
    public static synchronized HiveClassLoaderFactory getInstance() {
        if (instance == null) {
            instance = new HiveClassLoaderFactory();
        }
        return instance;
    }

    /**
     * Gets an instance of <code>ClassLoader</code> by the given argument, it invokes {
     * {@link #getHive1ClassLoader(IMetadataConnection)} and {@link #getHive2ClassLoader(IMetadataConnection)}. Added by
     * Marvin Wang on Mar 13, 2013.
     * 
     * @param metadataConn
     * @return
     */
    public ClassLoader getClassLoader(IMetadataConnection metadataConn) {
        if (!Platform.isRunning()) {
            return ClassLoader.getSystemClassLoader();
        }
        ClassLoader classloader = null;
        String url = metadataConn.getUrl();

        // url = "jdbc:hive2://";
        if (url != null) {
            if (url.startsWith(DatabaseConnConstants.HIVE_2_URL_FORMAT)) {
                classloader = getHive2ClassLoader(metadataConn);
            } else if (url.startsWith(DatabaseConnConstants.HIVE_1_URL_FORMAT)) {
                classloader = getHive1ClassLoader(metadataConn);
            } else {
                // do nothing
            }
        }

        appendExtraJars(metadataConn, classloader);

        return classloader;
    }

    /**
     * DOC ycbai Comment method "appendExtraJars".
     * 
     * <p>
     * Add the extra jars which hive connection needed like when creating a hive embedded connection with kerberos.
     * </p>
     * 
     * @param metadataConn
     * @param classLoader
     */
    private void appendExtraJars(IMetadataConnection metadataConn, ClassLoader classLoader) {
        if (classLoader instanceof DynamicClassLoader) {
            DynamicClassLoader loader = (DynamicClassLoader) classLoader;
            loadConfigurationJars(metadataConn, loader);
            loadAuthDriverJars(metadataConn, loader);
        }
    }

    private void loadConfigurationJars(IMetadataConnection metadataConn, DynamicClassLoader loader) {
        String distroKey = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_DISTRIBUTION);
        IHDistribution distribution = HiveMetadataHelper.getDistribution(distroKey, false);
        if (distribution != null && distribution.useCustom()) {
            return;
        }

        Object[] configurationJars = new String[0];
        String useCustomConfs = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_USE_CUSTOM_CONFS);
        if (Boolean.valueOf(useCustomConfs)) {
            String clusterId = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_HADOOP_CLUSTER_ID);
            String customConfsJarName = getCustomConfsJarName(clusterId);
            if (customConfsJarName != null) {
                configurationJars = new String[] { customConfsJarName };
            }
        } else {
            String useKrb = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_USE_KRB);
            if (Boolean.valueOf(useKrb)) {
                configurationJars = EHadoopConfigurationJars.HIVE.getEnableSecurityJars();
            } else {
                configurationJars = EHadoopConfigurationJars.HIVE.getDisableSecurityJars();
            }
        }

        // It is no use for now since there are some fix jars configurations in component.
        /*
         * String executionEngine = (String) metadataConn.getParameter(ConnParameterKeys.HIVE_EXECUTION_ENGINE);
         * EHiveExecutionTypes executionType = EHiveExecutionTypes.getTypeFromValue(executionEngine); if
         * (Boolean.valueOf(EHiveExecutionTypes.TEZ.equals(executionType))) { String hiveVersion = (String)
         * metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_VERSION); String[] tezJars =
         * EHiveWithTezJars.getJarsByVersion(hiveVersion); if (tezJars != null) { configurationJars =
         * ArrayUtils.addAll(configurationJars, tezJars); } }
         */

        ILibraryManagerService librairesManagerService = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                ILibraryManagerService.class);
        String libStorePath = loader.getLibStorePath();
        for (Object dependentJar : configurationJars) {
            librairesManagerService.retrieve((String) dependentJar, libStorePath, true, new NullProgressMonitor());
            String jarPath = libStorePath + PATH_SEPARATOR + dependentJar;
            File jarFile = new File(jarPath);
            if (jarFile.exists()) {
                loader.addLibrary(jarFile.getAbsolutePath());
            }
        }
    }

    private static String getCustomConfsJarName(String clusterId) {
        IHadoopClusterService hadoopClusterService = HadoopRepositoryUtil.getHadoopClusterService();
        if (hadoopClusterService != null) {
            return hadoopClusterService.getCustomConfsJarName(clusterId);
        }
        return null;
    }

    private void loadAuthDriverJars(IMetadataConnection metadataConn, DynamicClassLoader loader) {
        Set<String> libraries = loader.getLibraries();
        String driverJarPath = (String) metadataConn.getParameter(ConnParameterKeys.HIVE_AUTHENTICATION_DRIVERJAR_PATH);
        if (driverJarPath != null) {
            final File driverJar = new File(driverJarPath);
            if (driverJar.exists()) {
                if (!libraries.contains(driverJar)) {
                    loader.addLibrary(driverJar.getAbsolutePath());
                }
            }
        }
    }

    /**
     * Gets the class loader for different version by the given parameters for hive1 server. Added by Marvin Wang on Mar
     * 12, 2013.
     * 
     * @param metadataConn
     * @return
     */
    protected ClassLoader getHive1ClassLoader(IMetadataConnection metadataConn) {
        ClassLoader loader = null;
        // For hive embedded model, all parameters below should not be null, if null, then need to check.
        String distroKey = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_DISTRIBUTION);
        String distroVersion = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_VERSION);
        String hiveModel = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_MODE);

        IHDistribution distribution = HiveMetadataHelper.getDistribution(distroKey, false);
        if (distribution != null && distribution.useCustom()) {
            String jarsStr = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_HADOOP_CUSTOM_JARS);
            String index = "HadoopCustomVersion:Hive:" + metadataConn.getId(); //$NON-NLS-1$
            loader = ClassLoaderFactory.getCustomClassLoader(index, jarsStr);
        } else {
            String index = "HIVE" + ":" + distroKey + ":" + distroVersion + ":" + hiveModel; //$NON-NLS-1$  //$NON-NLS-2$  //$NON-NLS-3$ //$NON-NLS-4$ 
            loader = ClassLoaderFactory.getClassLoader(index);
            // If there is no class loader gotten by index, then use the current classloader.
            if (loader == null) {
                loader = HiveClassLoaderFactory.class.getClassLoader();
            }
        }

        return loader;
    }

    /**
     * 
     * Added by Marvin Wang on Mar 27, 2013.
     * 
     * @param metadataConn
     * @return
     */
    protected ClassLoader getHive2ClassLoader(IMetadataConnection metadataConn) {
        ClassLoader loader = null;
        // For hive embedded model, all parameters below should not be null, if null, then need to check.
        String distroKey = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_DISTRIBUTION);
        String distroVersion = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_VERSION);
        String hiveModel = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_MODE);

        IHDistribution distribution = HiveMetadataHelper.getDistribution(distroKey, false);
        if (distribution != null && distribution.useCustom()) {
            String jarsStr = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_HADOOP_CUSTOM_JARS);
            String index = "HadoopCustomVersion:Hive2:" + metadataConn.getId(); //$NON-NLS-1$
            loader = ClassLoaderFactory.getCustomClassLoader(index, jarsStr);
        } else {
            String index = "HIVE2" + ":" + distroKey + ":" + distroVersion + ":" + hiveModel; //$NON-NLS-1$  //$NON-NLS-2$  //$NON-NLS-3$ //$NON-NLS-4$ 
            loader = ClassLoaderFactory.getClassLoader(index);
            // If there is no class loader gotten by index, then use the current classloader.
            if (loader == null) {
                loader = HiveClassLoaderFactory.class.getClassLoader();
            }
        }

        return loader;
    }
}
