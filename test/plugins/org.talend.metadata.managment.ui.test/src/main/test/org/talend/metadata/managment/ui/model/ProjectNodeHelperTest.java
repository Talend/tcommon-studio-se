package org.talend.metadata.managment.ui.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SchemaHelper;

import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

public class ProjectNodeHelperTest {

    private DatabaseConnection connection;

    private MetadataTable metadataTable;

    @Before
    public void setUp() throws Exception {
        connection = ConnectionFactory.eINSTANCE.createDatabaseConnection();
        connection.setDatabaseType(EDatabaseTypeName.IBMDB2ZOS.getXmlName());
        connection.setSID("DB2TEST");
        connection.setUiSchema("Schema1");
        List<Schema> schemas = new ArrayList<Schema>();
        Catalog c = CatalogHelper.createCatalog("DB2TEST");
        Schema s = SchemaHelper.createSchema("Schema1");
        schemas.add(s);
        CatalogHelper.addSchemas(schemas, c);
        c.getDataManager().add(connection);
        ConnectionHelper.addCatalog(c, connection);

        metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
        metadataTable.setName("testTable");
        metadataTable.setId("abc");
    }

    @Test
    public void testAddDefaultTableForSpecifiedDataPackage() throws Exception {
        
        ProjectNodeHelper.addTableForSpecifiedDataPackage(connection, metadataTable);
        MetadataTable table = ConnectionHelper.getTableById(connection, "abc");
        
        assertTrue(table != null);
        assertEquals("testTable", table.getName());
        
        assertTrue(table.eContainer() instanceof Schema);
        Schema schema = (Schema) table.eContainer();
        assertEquals("Schema1", schema.getName());
        
        assertTrue(schema.eContainer() instanceof Catalog);
        Catalog catalog = (Catalog) schema.eContainer();
        assertEquals("DB2TEST", catalog.getName());
    }

}
