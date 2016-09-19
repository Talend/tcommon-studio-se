package org.talend.core.model.metadata;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Test;

public class MetadataSchemaTest {

    @Test
    public void testInitializeAllColumns() throws Exception {
        File file = new File("resources/tRowGenerator.xml");
        assertTrue(file.exists());

        MetadataSchema ms = new MetadataSchema();
        List<IMetadataColumn> list = ms.initializeAllColumns(file);
        assertTrue(list.size() == 3);

        IMetadataColumn column1 = list.get(0);
        assertEquals("col1", column1.getLabel());
        assertEquals("id_String", column1.getTalendType());

        IMetadataColumn column2 = list.get(1);
        assertEquals("col2", column2.getLabel());
        assertEquals("id_Integer", column2.getTalendType());

        IMetadataColumn column3 = list.get(2);
        assertEquals("col3", column3.getLabel());
        assertEquals("id_Date", column3.getTalendType());
    }

}
