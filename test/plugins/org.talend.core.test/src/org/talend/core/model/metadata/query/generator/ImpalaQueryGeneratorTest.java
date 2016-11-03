package org.talend.core.model.metadata.query.generator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
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

public class ImpalaQueryGeneratorTest {

    private IElement sqlElement;

    private IMetadataTable newTable;

    @Before
    public void before() {
        sqlElement = mock(IElement.class);
        IElementParameter newParam = mock(IElementParameter.class);
        List<? extends IElementParameter> elementParameters = sqlElement.getElementParameters();
        when(newParam.getName()).thenReturn("DBNAME");
        when(newParam.getFieldType()).thenReturn(EParameterFieldType.TEXT);
        when(newParam.getValue()).thenReturn("talend");
        when(newParam.isShow(elementParameters)).thenReturn(true);
        when(sqlElement.getElementParameter("DBNAME")).thenReturn(newParam);

        newParam = mock(IElementParameter.class);
        when(newParam.getName()).thenReturn("DBTABLE");
        when(newParam.getFieldType()).thenReturn(EParameterFieldType.DBTABLE);
        when(newParam.getValue()).thenReturn("mytable");
        when(newParam.isShow(elementParameters)).thenReturn(true);
        when(sqlElement.getElementParameterFromField(EParameterFieldType.DBTABLE)).thenReturn(newParam);

        newTable = new MetadataTable();
        newTable.setLabel("mytable");
        MetadataColumn newColumn = new MetadataColumn();
        newColumn.setLabel("newColumn");
        newColumn.setOriginalDbColumnName("newColumn");
        List<IMetadataColumn> columnsList = new ArrayList<IMetadataColumn>();
        columnsList.add(newColumn);
        newTable.setListColumns(columnsList);

    }

    @Test
    public void testGenerateQuery() {
        ImpalaQueryGenerator generator = new ImpalaQueryGenerator(EDatabaseTypeName.IMPALA);

        // test built-in mode
        generator.setParameters(sqlElement, newTable, "myschema", "mytable");
        String resultString = generator.generateQuery();
        StringBuilder expectSql = new StringBuilder();
        expectSql.append(TalendTextUtils.getQuoteChar())
                .append("SELECT \n  myschema.mytable.newColumn\nFROM myschema.mytable")
                .append(TalendTextUtils.getQuoteChar());
        String expectString = expectSql.toString();

        assertNotNull(resultString);
        assertEquals(expectString, resultString);
        
        // test repository mode
        generator.setParameters(sqlElement, newTable, "context.Impala_Database", "mytable");
        resultString = generator.generateQuery();
        expectSql = new StringBuilder();
        expectString = null;
        expectSql.append(TalendTextUtils.getQuoteChar())
                .append("SELECT \n  \"+context.Impala_Database+\".mytable.newColumn\nFROM \"+context.Impala_Database+\".mytable")
                .append(TalendTextUtils.getQuoteChar());
        expectString = expectSql.toString();

        assertNotNull(resultString);
        assertEquals(expectString, resultString);
    }

}
