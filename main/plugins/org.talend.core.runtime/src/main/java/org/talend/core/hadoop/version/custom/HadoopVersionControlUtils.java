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
package org.talend.core.hadoop.version.custom;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EMap;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.database.conn.ConnParameterKeys;
import org.talend.core.hadoop.IHadoopDistributionService;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.runtime.hd.IDistributionsManager;

/**
 * Created by Marvin Wang on Mar 26, 2013.
 */
public class HadoopVersionControlUtils {

    public final static String JAR_SEPARATOR = ";"; //$NON-NLS-1$

    public final static String EMPTY_STR = ""; //$NON-NLS-1$

    public static Map<String, Set<String>> getCustomVersionMap(DatabaseConnection connection, ECustomVersionGroup group) {
        Map<String, Set<String>> map = new HashMap<String, Set<String>>();
        if (connection == null || group == null) {
            return map;
        }

        EMap<String, String> parameters = connection.getParameters();
        if (parameters.size() == 0) {
            return map;
        }

        String jarString = parameters.get(ConnParameterKeys.CONN_PARA_KEY_HADOOP_CUSTOM_JARS);
        if (jarString != null && !jarString.isEmpty()) {
            Set<String> jarSet = new HashSet<String>();
            String[] jarArray = jarString.split(JAR_SEPARATOR);
            for (String jar : jarArray) {
                jarSet.add(jar);
            }
            map.put(group.getName(), jarSet);
        }

        return map;
    }

    public static void injectCustomVersionMap(DatabaseConnection connection, Map<String, Set<String>> map,
            ECustomVersionGroup group) {
        if (connection == null || map == null || group == null) {
            return;
        }
        EMap<String, String> parameters = connection.getParameters();
        StringBuffer jarBuffer = new StringBuffer();
        Set<String> jars = map.get(group.getName());
        if (jars != null) {
            for (String jar : jars) {
                jarBuffer.append(jar).append(JAR_SEPARATOR);
            }
            if (jarBuffer.length() > 0) {
                jarBuffer.deleteCharAt(jarBuffer.length() - 1);
            }
        }
        parameters.put(ConnParameterKeys.CONN_PARA_KEY_HADOOP_CUSTOM_JARS, jarBuffer.toString());
    }

    public static String getCompCustomJarsParamFromRep(DatabaseConnection connection, ECustomVersionGroup versionGroup) {
        if (connection == null || versionGroup == null) {
            return EMPTY_STR;
        }
        EMap<String, String> parameters = connection.getParameters();
        if (parameters.size() == 0) {
            return EMPTY_STR;
        }

        return parameters.get(versionGroup.getName());
    }

    public static Map<String, Set<String>> getRepCustomJarParamFromComp(String compCustomJars, ECustomVersionGroup versionGroup) {
        Map<String, Set<String>> customVersionMap = new HashMap<String, Set<String>>();
        if (StringUtils.isEmpty(compCustomJars)) {
            return customVersionMap;
        }
        Set<String> jarSet = new HashSet<String>();
        String[] jarArray = compCustomJars.split(JAR_SEPARATOR);
        for (String jar : jarArray) {
            jarSet.add(jar);
        }
        customVersionMap.put(versionGroup.getName(), jarSet);

        return customVersionMap;
    }

    public static IDistributionsManager getDistributionsManager(ECustomVersionType type) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IHadoopDistributionService.class)) {
            IHadoopDistributionService hadoopDistributionService = (IHadoopDistributionService) GlobalServiceRegister
                    .getDefault().getService(IHadoopDistributionService.class);
            IDistributionsManager distributionManager = null;
            if (type != null) {
                if (type == ECustomVersionType.SPARK) {
                    distributionManager = hadoopDistributionService.getSparkBatchDistributionManager();
                } else if (type == ECustomVersionType.SPARK_STREAMING) {
                    distributionManager = hadoopDistributionService.getSparkStreamingDistributionManager();
                } else if (type == ECustomVersionType.MAP_REDUCE) {
                    distributionManager = hadoopDistributionService.getMapReduceDistributionManager();
                }
            }
            if (distributionManager == null) {// default one
                distributionManager = hadoopDistributionService.getHadoopDistributionManager();
            }
            return distributionManager;
        }
        return null;
    }
}
