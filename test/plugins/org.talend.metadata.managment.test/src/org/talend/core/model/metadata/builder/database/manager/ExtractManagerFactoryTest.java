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
package org.talend.core.model.metadata.builder.database.manager;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.builder.database.manager.dbs.AS400ExtractManager;
import org.talend.core.model.metadata.builder.database.manager.dbs.AccessExtractManager;
import org.talend.core.model.metadata.builder.database.manager.dbs.GeneralJDBCExtractManager;
import org.talend.core.model.metadata.builder.database.manager.dbs.HSQLDBExtractManager;
import org.talend.core.model.metadata.builder.database.manager.dbs.HiveExtractManager;
import org.talend.core.model.metadata.builder.database.manager.dbs.IBMDB2ExtractManager;
import org.talend.core.model.metadata.builder.database.manager.dbs.JAVADBExtractManager;
import org.talend.core.model.metadata.builder.database.manager.dbs.MSSQLExtractManager;
import org.talend.core.model.metadata.builder.database.manager.dbs.MySQLExtractManager;
import org.talend.core.model.metadata.builder.database.manager.dbs.OracleExtractManager;
import org.talend.core.model.metadata.builder.database.manager.dbs.TeradataExtractManager;

/**
 * created by ggu on Jul 4, 2012 Detailled comment
 * 
 */
public class ExtractManagerFactoryTest {

    private Map<EDatabaseTypeName, Class> extractManagerMap;

    @Before
    public void setUp() throws Exception {
        extractManagerMap = new HashMap<EDatabaseTypeName, Class>();
        extractManagerMap.put(EDatabaseTypeName.ACCESS, AccessExtractManager.class);
        extractManagerMap.put(EDatabaseTypeName.AS400, AS400ExtractManager.class);
        extractManagerMap.put(EDatabaseTypeName.GENERAL_JDBC, GeneralJDBCExtractManager.class);
        extractManagerMap.put(EDatabaseTypeName.HIVE, HiveExtractManager.class);
        extractManagerMap.put(EDatabaseTypeName.HSQLDB, HSQLDBExtractManager.class);
        extractManagerMap.put(EDatabaseTypeName.HSQLDB_SERVER, HSQLDBExtractManager.class);
        extractManagerMap.put(EDatabaseTypeName.HSQLDB_WEBSERVER, HSQLDBExtractManager.class);
        extractManagerMap.put(EDatabaseTypeName.HSQLDB_IN_PROGRESS, HSQLDBExtractManager.class);
        extractManagerMap.put(EDatabaseTypeName.IBMDB2, IBMDB2ExtractManager.class);
        extractManagerMap.put(EDatabaseTypeName.IBMDB2ZOS, IBMDB2ExtractManager.class);
        extractManagerMap.put(EDatabaseTypeName.JAVADB_EMBEDED, JAVADBExtractManager.class);
        extractManagerMap.put(EDatabaseTypeName.JAVADB_DERBYCLIENT, JAVADBExtractManager.class);
        extractManagerMap.put(EDatabaseTypeName.JAVADB_JCCJDBC, JAVADBExtractManager.class);
        extractManagerMap.put(EDatabaseTypeName.MSSQL, MSSQLExtractManager.class);
        extractManagerMap.put(EDatabaseTypeName.MSSQL05_08, MSSQLExtractManager.class);
        extractManagerMap.put(EDatabaseTypeName.MYSQL, MySQLExtractManager.class);
        extractManagerMap.put(EDatabaseTypeName.ORACLEFORSID, OracleExtractManager.class);
        extractManagerMap.put(EDatabaseTypeName.ORACLESN, OracleExtractManager.class);
        extractManagerMap.put(EDatabaseTypeName.ORACLE_CUSTOM, OracleExtractManager.class);
        extractManagerMap.put(EDatabaseTypeName.ORACLE_OCI, OracleExtractManager.class);
        extractManagerMap.put(EDatabaseTypeName.TERADATA, TeradataExtractManager.class);
    }

    @After
    public void tearDown() throws Exception {
        if (extractManagerMap != null) {
            extractManagerMap.clear();
            extractManagerMap = null;
        }
    }

    @SuppressWarnings("rawtypes")
    private void assertManager(EDatabaseTypeName type, ExtractManager manager) {
        Assert.assertNotNull(extractManagerMap);

        Assert.assertNotNull(manager);
        Assert.assertNotNull(type);

        Class clazz = extractManagerMap.get(type);
        if (clazz == null) { // by default
            clazz = ExtractManager.class;
        }

        Assert.assertTrue("can't work for " + type.getDisplayName(), clazz.isInstance(manager));
    }

    @Test
    public void testCreateByDisplayName() {
        Assert.assertNull(ExtractManagerFactory.createByDisplayName(null));

        Assert.assertNull(ExtractManagerFactory.createByDisplayName("123"));

        Assert.assertNull(ExtractManagerFactory.createByDisplayName("ABC"));

        for (EDatabaseTypeName type : EDatabaseTypeName.values()) {
            ExtractManager manager = ExtractManagerFactory.createByDisplayName(type.getDisplayName());
            assertManager(type, manager);
        }
    }

    @Test
    public void testCreate() {
        Assert.assertNull(ExtractManagerFactory.create(null));

        for (EDatabaseTypeName type : EDatabaseTypeName.values()) {
            ExtractManager manager = ExtractManagerFactory.create(type);
            assertManager(type, manager);
        }
    }

}
