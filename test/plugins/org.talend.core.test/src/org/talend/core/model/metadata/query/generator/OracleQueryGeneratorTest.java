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
package org.talend.core.model.metadata.query.generator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataColumn;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElement;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.utils.TalendTextUtils;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class OracleQueryGeneratorTest {

    /**
     * Test method for {@link org.talend.core.model.metadata.query.AbstractQueryGenerator#generateQuery()}.
     */
    @Test
    public void testGenerateQuery() {
        OracleQueryGenerator oraclegenerator = new OracleQueryGenerator(EDatabaseTypeName.ORACLEFORSID);
        IElement OracleSQLElement = mock(IElement.class);
        IElementParameter newParam = mock(IElementParameter.class);
        List<? extends IElementParameter> elementParameters = OracleSQLElement.getElementParameters();
        when(newParam.getName()).thenReturn("DBNAME");
        when(newParam.getFieldType()).thenReturn(EParameterFieldType.TEXT);
        when(newParam.getValue()).thenReturn("talend");
        when(newParam.isShow(elementParameters)).thenReturn(true);
        when(OracleSQLElement.getElementParameter("DBNAME")).thenReturn(newParam);

        newParam = mock(IElementParameter.class);
        when(newParam.getName()).thenReturn("DBTABLE");
        when(newParam.getFieldType()).thenReturn(EParameterFieldType.DBTABLE);
        when(newParam.getValue()).thenReturn("mytable");
        when(newParam.isShow(elementParameters)).thenReturn(true);
        when(OracleSQLElement.getElementParameterFromField(EParameterFieldType.DBTABLE)).thenReturn(newParam);

        IMetadataTable newTable = new MetadataTable();

        newTable.setLabel("mytable");

        MetadataColumn newColumn = new MetadataColumn();

        newColumn.setLabel("newColumn");

        newColumn.setOriginalDbColumnName("newColumn");

        List<IMetadataColumn> columnsList = new ArrayList<IMetadataColumn>();

        columnsList.add(newColumn);

        newTable.setListColumns(columnsList);

        oraclegenerator.setParameters(OracleSQLElement, newTable, "myschema", "mytable");

        String resultString = oraclegenerator.generateQuery();

        StringBuilder expectSql = new StringBuilder();
        expectSql.append(TalendTextUtils.getQuoteChar()).append("SELECT \n  myschema.mytable.newColumn\nFROM myschema.mytable")
                .append(TalendTextUtils.getQuoteChar());
        String expectString = expectSql.toString();

        assertNotNull(resultString);

        assertTrue(!"".equals(resultString));

        assertTrue(expectString.equals(resultString));
    }

}
