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
package org.talend.core.hadoop;

import java.util.Map;

import org.talend.core.IService;
import org.talend.core.runtime.hd.IDistributionsManager;
import org.talend.core.runtime.hd.IDynamicDistributionManager;
import org.talend.core.runtime.hd.IHDistribution;
import org.talend.core.runtime.hd.IHDistributionVersion;

/**
 * created by cmeng on Jan 15, 2016 Detailled comment
 *
 */
public interface IHadoopDistributionService extends IService {

    /**
     * 
     * According to the service, find the distributions. The service is name of HadoopComponent service.
     * {@link org.talend.hadoop.distribution.component.HadoopComponent}.
     */
    IHDistribution[] getDistributions(String service);

    /**
     * Check the distribution version support the service or not.
     * 
     * The service must be full name, and be sub class of
     * {@link org.talend.hadoop.distribution.component.HadoopComponent)
     */
    boolean doSupportService(IHDistributionVersion distributionVersion, String service);

    /**
     * Check the boolean methods for sub class of {@link org.talend.hadoop.distribution.component.HadoopComponent)
     * 
     * If no the method in the HadoopComponent object, won't contain in the result map.
     */
    Map<String, Boolean> doSupportMethods(IHDistributionVersion distributionVersion, String... methods) throws Exception;

    boolean doSupportMethod(IHDistributionVersion distributionVersion, String method) throws Exception;

    /**
     * Find the matched distribution via name.
     * 
     * If the display is true, the name match for display one of distribution.
     */
    IHDistribution getHadoopDistribution(String name, boolean byDisplay);

    /**
     * Find the matched distribution via version .
     * 
     * If the display is true, the version match for display one of distribution.
     */
    IHDistributionVersion getHadoopDistributionVersion(String version, boolean byDisplay);

    /**
     * 
     * for Hadoop Distribution {@link HadoopComponent}.
     */
    IDistributionsManager getHadoopDistributionManager();

    /**
     * 
     * for MapReduce Distribution {@link MRComponent}.
     */
    IDistributionsManager getMapReduceDistributionManager();

    /**
     * 
     * for HBase in Database wizard.{@link HBaseComponent}.
     */
    IDistributionsManager getHBaseDistributionManager();

    /**
     * 
     * for Maprdb in Database wizard.{@link MaprdbComponent}.
     */
    IDistributionsManager getMaprdbDistributionManager();

    /**
     * 
     * for Spark Streaming Distribution {@link SparkStreamingComponent}.
     */
    IDistributionsManager getSparkStreamingDistributionManager();

    /**
     * 
     * for Spark Batch Distribution {@link SparkBatchComponent}.
     */
    IDistributionsManager getSparkBatchDistributionManager();

    /**
     * 
     * for Hive Distribution {@link HiveComponent}.
     */
    IDistributionsManager getHiveDistributionManager();

    /**
     * 
     * for Impala Distribution {@link ImpalaComponent}.
     */
    IDistributionsManager getImpalaDistributionManager();

    /**
     * 
     * Temp for Oozie.
     */
    IHDistribution[] getOozieDistributions();

    /**
     * 
     * Comment method "doSupportMapRTicket".
     * 
     * @param distributionVersion
     * @return
     */
    boolean doSupportMapRTicket(IHDistributionVersion distributionVersion);

    IDynamicDistributionManager getDynamicDistributionManager();
}
