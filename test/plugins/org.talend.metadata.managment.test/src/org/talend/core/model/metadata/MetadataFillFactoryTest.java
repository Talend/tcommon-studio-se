// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.model.metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.database.conn.template.EDatabaseConnTemplate;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.metadata.managment.model.MetadataFillFactory;
import org.talend.metadata.managment.model.SybaseConnectionFillerImpl;
import org.talend.utils.properties.PropertiesLoader;
import org.talend.utils.properties.TypedProperties;

import junit.framework.Assert;

/**
 * DOC zshen class global comment. Detailled comment
 */
public class MetadataFillFactoryTest {

    private Map<String, String> initParameterMap() {
        Map<String, String> returnMap = new HashMap<String, String>();
        TypedProperties connectionParams = PropertiesLoader.getProperties(MetadataFillFactoryTest.class,
                "connectionParameter.properties");
        returnMap.put("SqlTypeName", connectionParams.get("SqlTypeName") == null ? null : connectionParams.get("SqlTypeName")
                .toString());
        returnMap.put("driverClassName", connectionParams.get("driver") == null ? null : connectionParams.get("driver")
                .toString());
        returnMap.put("jdbcUrl", connectionParams.get("url") == null ? null : connectionParams.get("url").toString());
        returnMap.put("user", connectionParams.get("user") == null ? null : connectionParams.get("user").toString());
        returnMap.put("password", connectionParams.get("password") == null ? null : connectionParams.get("password").toString());
        returnMap.put("aDDParameter", connectionParams.get("aDDParameter") == null ? null : connectionParams.get("aDDParameter")
                .toString());
        returnMap.put("author", connectionParams.get("author") == null ? null : connectionParams.get("author").toString());
        returnMap.put("host", connectionParams.get("host") == null ? null : connectionParams.get("host").toString());
        returnMap.put("name", connectionParams.get("name") == null ? null : connectionParams.get("name").toString());
        returnMap.put("status", connectionParams.get("status") == null ? null : connectionParams.get("status").toString());
        returnMap.put("port", connectionParams.get("port") == null ? null : connectionParams.get("port").toString());
        returnMap.put("version",
                connectionParams.get("version") == null || connectionParams.get("version").toString().isEmpty() ? null
                        : connectionParams.get("version").toString());
        returnMap.put("retrieveAllMetadata",
                connectionParams.get("retrieveAllMetadata") == null ? null : connectionParams.get("retrieveAllMetadata")
                        .toString());
        returnMap.put("purpose", connectionParams.get("purpose") == null ? null : connectionParams.get("purpose").toString());

        return returnMap;
    }

    /**
     * Test method for {@link org.talend.metadata.managment.model.MetadataFillFactory#fillUIParams(java.util.Map)}.
     */
    @Test
    public void testFillUIParams() {

        Map<String, String> parameterMap = initParameterMap();
        IMetadataConnection metadataConnection = MetadataFillFactory.getDBInstance().fillUIParams(parameterMap);
        assertSame(metadataConnection.getPort(), parameterMap.get("port"));
        assertSame(metadataConnection.getServerName(), parameterMap.get("host"));
        assertSame(metadataConnection.getDriverJarPath(), parameterMap.get("driverPath"));
        assertSame(metadataConnection.getStatus(), parameterMap.get("status"));
        assertSame(metadataConnection.getFileFieldName(), parameterMap.get("filePath"));
        assertSame(metadataConnection.getDriverClass(), parameterMap.get("driverClassName"));
        assertSame(metadataConnection.getPurpose(), parameterMap.get("purpose"));
        assertSame(metadataConnection.getDatafilter(), parameterMap.get("datafilter"));
        assertSame(metadataConnection.getUniverse(), parameterMap.get("universe"));
        assertSame(metadataConnection.getUsername(), parameterMap.get(TaggedValueHelper.USER));
        assertSame(metadataConnection.getPassword(), parameterMap.get(TaggedValueHelper.PASSWORD));
        assertSame(metadataConnection.getDbVersionString(), parameterMap.get("version"));
        assertSame(metadataConnection.getDbType(), parameterMap.get("SqlTypeName"));
        assertSame(metadataConnection.getAdditionalParams(), parameterMap.get("aDDParameter"));
        assertSame(metadataConnection.getDatabase(), parameterMap.get("dbName"));
        assertSame(metadataConnection.getDescription(), parameterMap.get("description"));
        assertTrue(parameterMap.get("retrieveAllMetadata") == null
                || Boolean.toString(metadataConnection.isRetrieveAllMetadata()).equals(parameterMap.get("retrieveAllMetadata")));
        assertSame(metadataConnection.getUrl(), parameterMap.get("jdbcUrl"));
        assertNotNull("Product is not null", metadataConnection.getProduct());
        assertEquals("Mapping is mysql_id", "mysql_id", metadataConnection.getMapping());

    }

    /**
     * Test method for
     * {@link org.talend.metadata.managment.model.MetadataFillFactory#getDBInstance(org.talend.core.database.EDatabaseTypeName)}
     * .
     */
    @Test
    public void testGetDBInstance() {
        MetadataFillFactory dbInstance = MetadataFillFactory.getDBInstance(EDatabaseTypeName.SYBASEASE);
        Assert.assertTrue("MetadataFiller should be instanceof SybaseConnectionFillerImpl", //$NON-NLS-1$
                dbInstance.getMetadataFiller() instanceof SybaseConnectionFillerImpl);
        dbInstance = MetadataFillFactory.getDBInstance(EDatabaseTypeName.SYBASEIQ);
        Assert.assertTrue("MetadataFiller should be instanceof SybaseConnectionFillerImpl", //$NON-NLS-1$
                dbInstance.getMetadataFiller() instanceof SybaseConnectionFillerImpl);
        for (EDatabaseConnTemplate databaseType : EDatabaseConnTemplate.values()) {
            dbInstance = MetadataFillFactory.getDBInstance(EDatabaseTypeName.getTypeFromDbType(databaseType.getDBTypeName()));
            Assert.assertNotNull(dbInstance);
        }
    }

}
