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
package org.talend.core.model.metadata.query.generator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataColumn;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.utils.TalendTextUtils;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class H2QueryGeneratorTest {

    /**
     * Test method for {@link org.talend.core.model.metadata.query.AbstractQueryGenerator#generateQuery()}.
     */
    @Test
    public void testGenerateQuery() {
        // since H2 only need the table,no need to fake a node(for get db and schema) for it
        H2QueryGenerator h2QG = new H2QueryGenerator(EDatabaseTypeName.H2);

        IMetadataTable newTable = new MetadataTable();

        newTable.setLabel("mytable");

        MetadataColumn newColumn = new MetadataColumn();

        newColumn.setLabel("newColumn");

        newColumn.setOriginalDbColumnName("newColumn");

        List<IMetadataColumn> columnsList = new ArrayList<IMetadataColumn>();

        columnsList.add(newColumn);

        newTable.setListColumns(columnsList);

        h2QG.setParameters(null, newTable, "", "mytable");

        String resultString = h2QG.generateQuery();

        StringBuilder expectSql = new StringBuilder();
        expectSql.append(TalendTextUtils.getQuoteChar())
                .append("SELECT \n  \\\"mytable\\\".\\\"newColumn\\\"\n FROM \\\"mytable\\\"")
                .append(TalendTextUtils.getQuoteChar());
        String expectString = expectSql.toString();

        assertNotNull(resultString);

        assertTrue(!"".equals(resultString));

        assertTrue(expectString.equals(resultString));
    }

}
