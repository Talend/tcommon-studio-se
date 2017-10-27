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
package org.talend.core.model.utils;

import static org.junit.Assert.*;

import org.junit.Test;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;

/**
 * DOC hcyi class global comment. Detailled comment
 */
public class UpdateRepositoryHelperTest {

    /**
     * Test method for
     * {@link org.talend.core.model.utils.UpdateRepositoryHelper#getRepositorySourceName(org.talend.core.model.properties.Item)}
     * .
     */
    @Test
    public void testGetRepositorySourceName() {
        // Test DatabaseConnectionItem
        DatabaseConnectionItem databaseConnItem = PropertiesFactory.eINSTANCE.createDatabaseConnectionItem();
        Property myProperty = PropertiesFactory.eINSTANCE.createProperty();
        ItemState itemState = PropertiesFactory.eINSTANCE.createItemState();
        DatabaseConnection databaseConnection = ConnectionFactory.eINSTANCE.createDatabaseConnection();
        //
        myProperty.setId("_bK4nYKPgEeGSwOgmctA-XA");
        myProperty.setLabel("mysql");
        myProperty.setVersion("0.1");
        //
        itemState.setPath("");
        //
        databaseConnection.setName("mysql");
        databaseConnection.setLabel("mysql");
        databaseConnection.setDatabaseType("MySQL");
        databaseConnection.setURL("jdbc:mysql://127.0.0.1:3306/go?noDatetimeStringSync=true");
        databaseConnection.setDriverClass("org.gjt.mm.mysql.Driver");
        databaseConnection.setDbVersionString("MYSQL_5");
        databaseConnection.setPort("3306");
        databaseConnection.setUsername("root");
        databaseConnection.setRawPassword("toor");
        databaseConnection.setServerName("127.0.0.1");
        databaseConnection.setProductId("MYSQL");
        databaseConnection.setAdditionalParams("noDatetimeStringSync=true");
        //
        databaseConnItem.setProperty(myProperty);
        databaseConnItem.setState(itemState);
        databaseConnItem.setConnection(databaseConnection);

        assertEquals("DB (MYSQL):mysql", UpdateRepositoryHelper.getRepositorySourceName(databaseConnItem));

        // Test ContextItem
        ContextItem contextItem = PropertiesFactory.eINSTANCE.createContextItem();
        Property myContextProperty = PropertiesFactory.eINSTANCE.createProperty();
        ItemState contextItemState = PropertiesFactory.eINSTANCE.createItemState();
        //
        myContextProperty.setId("_DHiJ0KPlEeGSwOgmctA-XA");
        myContextProperty.setLabel("testContext");
        myContextProperty.setVersion("0.1");
        //
        contextItemState.setPath("");
        //
        contextItem.setProperty(myContextProperty);
        contextItem.setState(contextItemState);
        contextItem.setDefaultContext("Default");

        assertEquals("Context:testContext", UpdateRepositoryHelper.getRepositorySourceName(contextItem));

    }
}
