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
package org.talend.metadata.managment.connection.manager;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.talend.core.database.conn.ConnParameterKeys;
import org.talend.core.hadoop.version.EHadoopVersion4Drivers;
import org.talend.core.model.metadata.builder.MetadataConnection;
import org.talend.core.model.metadata.connection.hive.HiveModeInfo;
import org.talend.metadata.managment.hive.handler.CDH4YarnHandler;
import org.talend.metadata.managment.hive.handler.CDH5YarnHandler;
import org.talend.metadata.managment.hive.handler.HDP130Handler;
import org.talend.metadata.managment.hive.handler.HDP200YarnHandler;
import org.talend.metadata.managment.hive.handler.HiveConnectionHandler;
import org.talend.metadata.managment.hive.handler.Mapr212Handler;

/**
 * created by qiongli on 2014-3-11 Detailled comment
 * 
 */
public class HiveConnectionManagerTest {

    /**
     * DOC qiongli Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * DOC qiongli Comment method "tearDown".
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for
     * {@link org.talend.metadata.managment.connection.manager.HiveConnectionManager#createHandler(org.talend.core.model.metadata.IMetadataConnection)}
     * .
     */
    @Test
    public void testCreateHandler_embeded() {
        MetadataConnection mc = new MetadataConnection();
        mc.setParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_VERSION, EHadoopVersion4Drivers.HDP_1_3.getVersionValue());
        mc.setParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_MODE, HiveModeInfo.EMBEDDED.getName());
        HiveConnectionHandler createHandler = HiveConnectionManager.getInstance().createHandler(mc);
        assertTrue(createHandler instanceof HDP130Handler);

        mc.setParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_VERSION, EHadoopVersion4Drivers.CLOUDERA_CDH4_YARN.getVersionValue());
        createHandler = HiveConnectionManager.getInstance().createHandler(mc);
        assertTrue(createHandler instanceof CDH4YarnHandler);

        mc.setParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_VERSION, EHadoopVersion4Drivers.HDP_2_0.getVersionValue());
        createHandler = HiveConnectionManager.getInstance().createHandler(mc);
        assertTrue(createHandler instanceof HDP200YarnHandler);

        mc.setParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_VERSION, EHadoopVersion4Drivers.MAPR212.getVersionValue());
        createHandler = HiveConnectionManager.getInstance().createHandler(mc);
        assertTrue(createHandler instanceof Mapr212Handler);

        mc.setParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_VERSION, EHadoopVersion4Drivers.MAPR301.getVersionValue());
        createHandler = HiveConnectionManager.getInstance().createHandler(mc);
        assertTrue(createHandler instanceof Mapr212Handler);

        mc.setParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_VERSION, EHadoopVersion4Drivers.CLOUDERA_CDH5.getVersionValue());
        createHandler = HiveConnectionManager.getInstance().createHandler(mc);
        assertTrue(createHandler instanceof CDH5YarnHandler);
    }

    /**
     * 
     * DOC qiongli Comment method "testCreateHandler_standalone".
     */
    @Test
    public void testCreateHandler_standalone() {
        MetadataConnection mc = new MetadataConnection();
        mc.setParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_VERSION, EHadoopVersion4Drivers.HDP_1_3.getVersionValue());
        mc.setParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_MODE, HiveModeInfo.STANDALONE.getName());
        HiveConnectionHandler createHandler = HiveConnectionManager.getInstance().createHandler(mc);
        assertFalse(createHandler instanceof HDP130Handler);

        mc.setParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_VERSION, EHadoopVersion4Drivers.CLOUDERA_CDH4_YARN.getVersionValue());
        createHandler = HiveConnectionManager.getInstance().createHandler(mc);
        assertFalse(createHandler instanceof CDH4YarnHandler);

        mc.setParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_VERSION, EHadoopVersion4Drivers.HDP_2_0.getVersionValue());
        createHandler = HiveConnectionManager.getInstance().createHandler(mc);
        assertFalse(createHandler instanceof HDP200YarnHandler);

        mc.setParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_VERSION, EHadoopVersion4Drivers.MAPR212.getVersionValue());
        createHandler = HiveConnectionManager.getInstance().createHandler(mc);
        assertFalse(createHandler instanceof Mapr212Handler);

        mc.setParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_VERSION, EHadoopVersion4Drivers.MAPR301.getVersionValue());
        createHandler = HiveConnectionManager.getInstance().createHandler(mc);
        assertFalse(createHandler instanceof Mapr212Handler);

        mc.setParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_VERSION, EHadoopVersion4Drivers.CLOUDERA_CDH5.getVersionValue());
        createHandler = HiveConnectionManager.getInstance().createHandler(mc);
        assertFalse(createHandler instanceof CDH5YarnHandler);
    }
}
