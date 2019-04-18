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
package org.talend.metadata.managment.ui.utils;

import org.junit.Assert;
import org.junit.Test;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.database.conn.ConnParameterKeys;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.impl.ConnectionFactoryImpl;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.metadata.managment.repository.ManagerConnection;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class DBConnectionContextUtilsTest {

    @SuppressWarnings("nls")
    @Test
    public void testCloneOriginalValueConnectionImpala() {
        final String EXPECT_URL = "jdbc:hive2://tal-qa146.talend.lan:21050/default;principal=impala/tal-qa146.talend.lan@CDH.ONE";
        DatabaseConnection originalConn = ConnectionFactoryImpl.eINSTANCE.createDatabaseConnection();
        originalConn.setDatabaseType(EDatabaseTypeName.IMPALA.getXmlName());
        originalConn.setServerName("tal-qa146.talend.lan");
        originalConn.setPort("21050");
        originalConn.setSID("default");
        originalConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_USE_KRB, Boolean.TRUE.toString());
        originalConn.getParameters().put(ConnParameterKeys.IMPALA_AUTHENTICATION_PRINCIPLA,
                "impala/tal-qa146.talend.lan@CDH.ONE");
        DatabaseConnection clonedConn = DBConnectionContextUtils.cloneOriginalValueConnection(originalConn, false, null);
        Assert.assertEquals(EXPECT_URL, clonedConn.getURL());
    }

    @SuppressWarnings("nls")
    @Test
    public void testSetManagerConnectionValues() {
        final String EXPECT_URL = "jdbc:firebirdsql:tal-rd169.talend.lan/3050:/databases/TUJ?";
        final ManagerConnection managerConnection = new ManagerConnection();
        ConnectionItem connectionItem = PropertiesFactory.eINSTANCE.createConnectionItem();
        DatabaseConnection conn = ConnectionFactory.eINSTANCE.createDatabaseConnection();
        conn.setDatabaseType(EDatabaseTypeName.FIREBIRD.getXmlName());
        conn.setProductId(EDatabaseTypeName.FIREBIRD.getProduct());
        conn.setServerName("tal-rd169.talend.lan");
        conn.setUsername("username");
        conn.setPassword("pwd");
        conn.setPort("3050");
        conn.setFileFieldName("/databases/TUJ");
        connectionItem.setConnection(conn);
        String setManagerConnectionValues = DBConnectionContextUtils.setManagerConnectionValues(managerConnection, connectionItem,
                null, conn.getDatabaseType());
        Assert.assertEquals(EXPECT_URL, setManagerConnectionValues);
    }

}
