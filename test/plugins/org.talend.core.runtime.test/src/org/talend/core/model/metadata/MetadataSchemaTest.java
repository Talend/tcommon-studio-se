package org.talend.core.model.metadata;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.junit.Test;

public class MetadataSchemaTest {

    @Test
    public void testInitializeAllColumns() throws Exception {
        URL xmlURL = FileLocator.find(Platform.getBundle("org.talend.core.runtime.test"),
                new Path("/resources/tRowGenerator.xml"), null);
        if (xmlURL != null) {
            xmlURL = FileLocator.toFileURL(xmlURL);
        }
        File file = new File(xmlURL.getFile());
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
