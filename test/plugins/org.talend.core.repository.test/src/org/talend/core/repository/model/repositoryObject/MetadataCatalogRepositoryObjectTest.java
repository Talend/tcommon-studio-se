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

/**
 * created by xqliu on Sep 2, 2013 Detailled comment
 * 
 */
public class MetadataCatalogRepositoryObjectTest {

    private static final String CATALOG_NAME = "catalogName"; //$NON-NLS-1$

    /**
     * Test method for
     * {@link org.talend.core.repository.model.repositoryObject.MetadataCatalogRepositoryObject#getProperty()}.
     */
    @Test
    public void testGetProperty() {
        Catalog catalog = orgomg.cwm.resource.relational.RelationalFactory.eINSTANCE.createCatalog();
        catalog.setName(CATALOG_NAME);

        IRepositoryViewObject mockViewObject = mock(IRepositoryViewObject.class);
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        when(mockViewObject.getProperty()).thenReturn(property);

        ConnectionItem connItem = PropertiesFactory.eINSTANCE.createConnectionItem();
        Connection conn = ConnectionFactory.eINSTANCE.createConnection();
        connItem.setConnection(conn);
        Catalog catalog2 = orgomg.cwm.resource.relational.RelationalFactory.eINSTANCE.createCatalog();
        catalog2.setName(CATALOG_NAME);
        conn.getDataPackage().add(catalog2);

        property.setItem(connItem);

        MetadataCatalogRepositoryObject catalogRepObject = new MetadataCatalogRepositoryObject(mockViewObject, catalog);

        Property property2 = catalogRepObject.getProperty();
        Assert.assertTrue(property2.equals(property));
        Assert.assertTrue(catalog2.equals(catalogRepObject.getCatalog()));
    }
}
