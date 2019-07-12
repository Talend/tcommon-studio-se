package org.talend.core.model.metadata;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MetadataTableTest {

    private List<IMetadataColumn> unusedColumns;

    private List<IMetadataColumn> listColumns;

    private List<String> originalColumns;

    @Before
    public void setUp() {
        init();
    }

    private void init() {
        originalColumns = new ArrayList<String>();
        originalColumns.add("B");
        originalColumns.add("C");
        originalColumns.add("A");

        unusedColumns = new ArrayList<IMetadataColumn>();
        listColumns = new ArrayList<IMetadataColumn>();

        MetadataColumn metadataColumn0 = new MetadataColumn();
        metadataColumn0.setLabel("A");
        listColumns.add(metadataColumn0);

        MetadataColumn metadataColumn1 = new MetadataColumn();
        metadataColumn1.setLabel("C");
        listColumns.add(metadataColumn1);

        MetadataColumn metadataColumn2 = new MetadataColumn();
        metadataColumn2.setLabel("B");
        listColumns.add(metadataColumn2);

    }

    @Test
    public void testGetListColumns() {
        IMetadataTable table = new MetadataTable();
        table.setOriginalColumns(originalColumns);
        table.setListColumns(listColumns);
        table.setUnusedColumns(unusedColumns);
        
        table.setRepository(false);
        List<IMetadataColumn> listColumns = table.getListColumns(true);
        String label0 = listColumns.get(0).getLabel();
        assertEquals(label0, "A");
        String label1 = listColumns.get(1).getLabel();
        assertEquals(label1, "C");
        String label2 = listColumns.get(2).getLabel();
        assertEquals(label2, "B");

        table.setRepository(true);
        List<IMetadataColumn> OrderedlistColumns = table.getListColumns(true);
        String label00 = OrderedlistColumns.get(0).getLabel();
        assertEquals(label00, "B");
        String label11 = OrderedlistColumns.get(1).getLabel();
        assertEquals(label11, "C");
        String label22 = OrderedlistColumns.get(2).getLabel();
        assertEquals(label22, "A");
    }
}
