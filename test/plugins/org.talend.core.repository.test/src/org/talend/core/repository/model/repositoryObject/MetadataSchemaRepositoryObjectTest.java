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
package org.talend.core.repository.model.repositoryObject;

import static org.mockito.Mockito.*;

import org.junit.Assert;
import org.junit.Test;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryViewObject;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * created by xqliu on Sep 2, 2013 Detailled comment
 * 
 */
public class MetadataSchemaRepositoryObjectTest {

    private static final String CATALOG_NAME = "catalogName"; //$NON-NLS-1$

    private static final String SCHEMA_NAME = "schemaName"; //$NON-NLS-1$

    /**
     * Test method for
     * {@link org.talend.core.repository.model.repositoryObject.MetadataSchemaRepositoryObject#getProperty()}.
     */
    @Test
    public void testGetProperty() {
        // the connection have schema only
        Schema schema = orgomg.cwm.resource.relational.RelationalFactory.eINSTANCE.createSchema();
        schema.setName(SCHEMA_NAME);

        IRepositoryViewObject mockViewObject = mock(IRepositoryViewObject.class);
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        when(mockViewObject.getProperty()).thenReturn(property);

        ConnectionItem connItem = PropertiesFactory.eINSTANCE.createConnectionItem();
        Connection conn = ConnectionFactory.eINSTANCE.createConnection();
        connItem.setConnection(conn);
        Schema schema2 = orgomg.cwm.resource.relational.RelationalFactory.eINSTANCE.createSchema();
        schema2.setName(SCHEMA_NAME);
        conn.getDataPackage().add(schema2);

        property.setItem(connItem);

        MetadataSchemaRepositoryObject schemaRepObject = new MetadataSchemaRepositoryObject(mockViewObject, schema);

        Property property2 = schemaRepObject.getProperty();
        Assert.assertTrue(property2.equals(property));
        Assert.assertTrue(schema2.equals(schemaRepObject.getSchema()));
    }

    /**
     * Test method for
     * {@link org.talend.core.repository.model.repositoryObject.MetadataSchemaRepositoryObject#getProperty()}.
     */
    @Test
    public void testGetProperty2() {
        // the connection have both catalog and schema
        Schema schema = orgomg.cwm.resource.relational.RelationalFactory.eINSTANCE.createSchema();
        schema.setName(SCHEMA_NAME);

        IRepositoryViewObject mockViewObject = mock(IRepositoryViewObject.class);
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        when(mockViewObject.getProperty()).thenReturn(property);

        ConnectionItem connItem = PropertiesFactory.eINSTANCE.createConnectionItem();
        Connection conn = ConnectionFactory.eINSTANCE.createConnection();
        connItem.setConnection(conn);
        Catalog catalog = orgomg.cwm.resource.relational.RelationalFactory.eINSTANCE.createCatalog();
        catalog.setName(CATALOG_NAME);
        conn.getDataPackage().add(catalog);
        Schema schema2 = orgomg.cwm.resource.relational.RelationalFactory.eINSTANCE.createSchema();
        schema2.setName(SCHEMA_NAME);
        catalog.getOwnedElement().add(schema2);

        property.setItem(connItem);

        MetadataSchemaRepositoryObject schemaRepObject = new MetadataSchemaRepositoryObject(mockViewObject, schema);
        MetadataCatalogRepositoryObject catalogRepositoryObject = mock(MetadataCatalogRepositoryObject.class);
        when(catalogRepositoryObject.getLabel()).thenReturn(CATALOG_NAME);
        schemaRepObject.setParentCatalogObject(catalogRepositoryObject);

        Property property2 = schemaRepObject.getProperty();
        Assert.assertTrue(property2.equals(property));
        Assert.assertTrue(schema2.equals(schemaRepObject.getSchema()));
    }
}
