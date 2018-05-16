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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.MessageFormat;

import org.junit.Before;
import org.junit.Test;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.hadoop.IHadoopDistributionService;
import org.talend.core.runtime.hd.IDistributionsManager;
import org.talend.core.runtime.hd.IHDConstants;
import org.talend.core.runtime.hd.IHDistribution;
import org.talend.core.runtime.hd.IHDistributionVersion;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class HadoopVersionControlUtilsTest {

    @Before
    public void before() {
        if (getHadoopDistributionService() == null) {
            fail("Can't test for hadoop distribution, because there are no any distribution bundles to load.");
        }
    }

    private IHadoopDistributionService getHadoopDistributionService() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IHadoopDistributionService.class)) {
            return (IHadoopDistributionService) GlobalServiceRegister.getDefault().getService(IHadoopDistributionService.class);
        }
        return null;

    }

    @Test
    public void testGetDistributionsManager_nullType() throws Exception {
        doTestDistributionsManagerForService(null, IHDConstants.SERVICE_HADOOP);

    }

    @Test
    public void testGetDistributionsManager_Default() throws Exception {
        doTestDistributionsManagerForService(ECustomVersionType.ALL, IHDConstants.SERVICE_HADOOP);
    }

    @Test
    public void testGetDistributionsManager_SparkBatch() throws Exception {
        doTestDistributionsManagerForService(ECustomVersionType.SPARK, IHDConstants.SERVICE_SPARK_BATCH);
    }

    @Test
    public void testGetDistributionsManager_SparkStreaming() throws Exception {
        doTestDistributionsManagerForService(ECustomVersionType.SPARK_STREAMING, IHDConstants.SERVICE_SPARK_STREAMING);
    }

    @Test
    public void testGetDistributionsManager_MapReduce() throws Exception {
        doTestDistributionsManagerForService(ECustomVersionType.MAP_REDUCE, IHDConstants.SERVICE_MR);
    }

    private void doTestDistributionsManagerForService(ECustomVersionType type, String service) throws Exception {
        IDistributionsManager distributionsManager = HadoopVersionControlUtils.getDistributionsManager(type);
        assertNotNull(distributionsManager);

        IHDistribution[] distributions = distributionsManager.getDistributions();
        assertNotNull(distributions);
        assertTrue(distributions.length > 0);
        IHadoopDistributionService hadoopDistributionService = getHadoopDistributionService();

        for (IHDistribution d : distributions) {
            IHDistributionVersion[] versions = d.getHDVersions();
            for (IHDistributionVersion v : versions) {
                assertTrue(MessageFormat.format(
                        "Not support the service \"{0}\" for the version \"{1}\" of distribution \"{2}\"", service,
                        d.getDisplayName(), v.getDisplayVersion()), hadoopDistributionService.doSupportService(v, service));
            }
        }

    }
}
