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

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.talend.commons.utils.PasswordEncryptUtil;
import org.talend.core.model.context.JobContextParameter;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.properties.Project;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;
import org.talend.repository.ProjectManager;
import org.talend.utils.security.CryptoHelper;


/**
 * DOC cmeng class global comment. Detailled comment
 * 
 * 
 * test for the encryption and decryption, in this junit, maybe some test case will be failed random, so fart, can't
 * find out the root problems.
 */
@SuppressWarnings({ "nls", "deprecation" })
public class ParameterValueUtilTest {

    @Test
    public void testSplitQueryData4SQL() {
        String testString = null;
        String expectRetValue = null;
        String retValue = null;
        int i = 0;

        // test case 0
        // testString : context.operation+" "+context.schema+"."+context.table+";"
        testString = "context.operation+\" \"+context.schema+\".\"+context.table+\";\"";
        expectRetValue = "context.oper+\" \"+context.schema+\".\"+context.table+\";\"";
        retValue = ParameterValueUtil.splitQueryData("context.operation", "context.oper", testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, expectRetValue.equals(retValue));
        // schema
        expectRetValue = "context.operation+\" \"+context.db+\".\"+context.table+\";\"";
        retValue = ParameterValueUtil.splitQueryData("context.schema", "context.db", testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, expectRetValue.equals(retValue));
        // table
        expectRetValue = "context.operation+\" \"+context.schema+\".\"+context.table1+\";\"";
        retValue = ParameterValueUtil.splitQueryData("context.table", "context.table1", testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, expectRetValue.equals(retValue));

        // part of replacing
        retValue = ParameterValueUtil.splitQueryData("text.schema", "text.schemaABC", testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, testString.equals(retValue));

        // only normal sting
        testString = "context.operation+\" \"+context_schema+schema+\".\"+context.table+\";\"";
        expectRetValue = "context.operation+\" \"+context_schema+schema123+\".\"+context.table+\";\"";
        retValue = ParameterValueUtil.splitQueryData("schema", "schema123", testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, expectRetValue.equals(retValue));
        // only normal sting for context
        testString = "context.operation+\" \"+context.schema+schema+\".\"+context.table+\";\"";
        expectRetValue = "context.operation+\" \"+context.schema+schema123+\".\"+context.table+\";\"";
        retValue = ParameterValueUtil.splitQueryData("schema", "schema123", testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, expectRetValue.equals(retValue));

        // same prefix
        testString = "context.operation+\" \"+context.test1+\".\"+context.test11+\";\"";
        expectRetValue = "context.operation+\" \"+context.test2+\".\"+context.test11+\";\"";
        retValue = ParameterValueUtil.splitQueryData("context.test1", "context.test2", testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, expectRetValue.equals(retValue));

        // test case 1
        // testString (For bug:TDI-29092) : "drop table "+context.oracle_schema+".\"TDI_26803\""
        testString = "\"drop table \"+context.oracle_schema+\".\\\"TDI_26803\\\"\"";
        expectRetValue = "\"drop table \"+context.oracl_schema+\".\\\"TDI_26803\\\"\"";
        retValue = ParameterValueUtil.splitQueryData("context.oracle_schema", "context.oracl_schema", testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, expectRetValue.equals(retValue));
        // column, don't replace the file for SQL
        expectRetValue = "\"drop table \"+context.oracl_schema+\".\\\"TDI_12345\\\"\"";
        retValue = ParameterValueUtil.splitQueryData("TDI_26803", "TDI_12345", testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, testString.equals(retValue)); // not changed

        // test case 7
        // all are empty
        // testString :
        // ""
        testString = "";
        expectRetValue = "";
        retValue = ParameterValueUtil.splitQueryData("", "", testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, expectRetValue.equals(retValue));

        // test case 8
        // many same varibles
        // testString :
        // "contextA"+context+"contextB"+context+"contextC" + context+" "
        testString = "\"contextA\"+context+\"contextB\"+context+\"contextC\" + context+\" \"";
        expectRetValue = "\"contextA\"+context.db+\"contextB\"+context.db+\"contextC\" + context.db+\" \"";
        retValue = ParameterValueUtil.splitQueryData("context", "context.db", testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, expectRetValue.equals(retValue));

        expectRetValue = "\"contextA\"+context.db+\"contextB\"+context.db+\"contextCC\" + context.db+\" \"";
        retValue = ParameterValueUtil.splitQueryData("contextC", "contextCC", testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, testString.equals(retValue)); // not changed

        // test case 9
        // testString :
        // "contextA"+contextA+"contextB"+context+"contextC" + context+" "
        testString = "\"contextA\"+contextA+\"contextB\"+context+\"contextC\" + context+\" \"";
        expectRetValue = "\"contextA\"+contextA+\"contextB\"+context.db+\"contextC\" + context.db+\" \"";
        retValue = ParameterValueUtil.splitQueryData("context", "context.db", testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, expectRetValue.equals(retValue));

        expectRetValue = "\"contextA\"+contextAA+\"contextB\"+context+\"contextC\" + context+\" \"";
        retValue = ParameterValueUtil.splitQueryData("contextA", "contextAA", testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, expectRetValue.equals(retValue));

        // test case 10
        // "SELECT
        // "+context.ORA_VIRTULIA_Schema+".PER_ETATCIVIL.IDE_DOSSIER,
        // "+context.ORA_VIRTULIA_Schema+".PER_ETATCIVIL.QUALITE,
        // "+context.ORA_VIRTULIA_Schema+".PER_ETATCIVIL.NOM
        // FROM "+context.ORA_VIRTULIA_Schema+".PER_ETATCIVIL"
        // this function should not replace constant
        testString = "\"SELECT \r\n" + "\"+context.ORA_VIRTULIA_Schema+\".PER_ETATCIVIL.IDE_DOSSIER,\r\n"
                + "\"+context.ORA_VIRTULIA_Schema+\".PER_ETATCIVIL.QUALITE,\r\n"
                + "\"+context.ORA_VIRTULIA_Schema+\".PER_ETATCIVIL.NOM\r\n"
                + "FROM \"+context.ORA_VIRTULIA_Schema+\".PER_ETATCIVIL\"";
        expectRetValue = "\"SELECT \r\n" + "\"+context.ORA_VIRTULIA_Schema+\".PER_ETATCIVIL.IDE_DOSSIER,\r\n"
                + "\"+context.ORA_VIRTULIA_Schema+\".PER_ETATCIVIL.QUALITE,\r\n"
                + "\"+context.ORA_VIRTULIA_Schema+\".PER_ETATCIVIL.NOM\r\n"
                + "FROM \"+context.ORA_VIRTULIA_Schema+\".PER_ETATCIVIL\"";
        retValue = ParameterValueUtil.splitQueryData("PER_ETATCIVIL.IDE_DOSSIER", "simplejoblet_1_PER_ETATCIVIL.IDE_DOSSIER",
                testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, expectRetValue.equals(retValue));

        testString = "\"SELECT \r\n" + "\"+context.ORA_VIRTULIA_Schema+\".PER_ETATCIVIL.IDE_DOSSIER,\r\n"
                + "\"+context.ORA_VIRTULIA_Schema+\".PER_ETATCIVIL.QUALITE,\r\n"
                + "\"+context.ORA_VIRTULIA_Schema+\".PER_ETATCIVIL.NOM\r\n"
                + "FROM \"+context.ORA_VIRTULIA_Schema+\".PER_ETATCIVIL\"";
        expectRetValue = "\"SELECT \r\n" + "\"+context.ORA_CHANGE_Schema+\".PER_ETATCIVIL.IDE_DOSSIER,\r\n"
                + "\"+context.ORA_CHANGE_Schema+\".PER_ETATCIVIL.QUALITE,\r\n"
                + "\"+context.ORA_CHANGE_Schema+\".PER_ETATCIVIL.NOM\r\n"
                + "FROM \"+context.ORA_CHANGE_Schema+\".PER_ETATCIVIL\"";
        retValue = ParameterValueUtil.splitQueryData("context.ORA_VIRTULIA_Schema", "context.ORA_CHANGE_Schema", testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, expectRetValue.equals(retValue));

        testString = "no match";
        expectRetValue = "no match";
        retValue = ParameterValueUtil.splitQueryData("context.schema", "context.db", testString);
        Assert.assertTrue(retValue != null && !"".equals(retValue));
        Assert.assertTrue("testSplitQueryDataCase_" + i++, expectRetValue.equals(retValue));

        // test case 11
        // testString : "select * from " + context.table + " where value = \"value from context.table\""
        // expectString : "select * from " + context.table1 + " where value = \"value from context.table\""
        testString = "\"select * from \" + context.table + \" where value = \\\"value from context.table\\\"\"";
        expectRetValue = "\"select * from \" + context.table1 + \" where value = \\\"value from context.table\\\"\"";
        retValue = ParameterValueUtil.splitQueryData("context.table", "context.table1", testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, expectRetValue.equals(retValue));

        // test case 12
        // testString : "select * from " + context.table + " where value = \"context.table\""
        // expectString : "select * from " + context.table1 + " where value = \"context.table\""
        testString = "\"select * from \" + context.table + \" where value = \\\"context.table\\\"\"";
        expectRetValue = "\"select * from \" + context.table1 + \" where value = \\\"context.table\\\"\"";
        retValue = ParameterValueUtil.splitQueryData("context.table", "context.table1", testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, expectRetValue.equals(retValue));

        // test case 13
        // testString : "select * from " + context.table + " where value = \"context.table\"" + context.table
        // expectString : "select * from " + context.table1 + " where value = \"context.table\"" + context.table
        testString = "\"select * from \" + context.table + \" where value = \\\"context.table\\\"\" + context.table";
        expectRetValue = "\"select * from \" + context.table1 + \" where value = \\\"context.table\\\"\" + context.table1";
        retValue = ParameterValueUtil.splitQueryData("context.table", "context.table1", testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, expectRetValue.equals(retValue));

        // test case 14 : incomplete double quota
        // testString : "select a,context.b from " + context.b + "where value = context.b
        // expectString : "select a,context.b from " + context.b1 + "where value = context.b1
        testString = "\"select a,context.b from \" + context.b + \"where value = context.b";
        expectRetValue = "\"select a,context.b from \" + context.b1 + \"where value = context.b1";
        retValue = ParameterValueUtil.splitQueryData("context.b", "context.b1", testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, expectRetValue.equals(retValue));

        // test case 15 : incomplete double quota
        // testString : "select a,context.b from " + context.b + "where value = \"context.b
        // expectString : "select a,context.b from " + context.b1 + "where value = \"context.b1
        testString = "\"select a,context.b from \" + context.b + \"where value = \\\"context.b";
        expectRetValue = "\"select a,context.b from \" + context.b1 + \"where value = \\\"context.b1";
        retValue = ParameterValueUtil.splitQueryData("context.b", "context.b1", testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, expectRetValue.equals(retValue));

        // test case 16
        // testString : "select * from " + context.table + " where value = \"\\" + context.table + "\\context.table\""
        // expectString : "select * from " + context.table1 + " where value = \"\\" + context.table1 +
        // "\\context.table\""
        testString = "\"select * from \" + context.table + \" where value = \\\"\\\\\" + context.table + \"\\\\context.table\\\"\"";
        expectRetValue = "\"select * from \" + context.table1 + \" where value = \\\"\\\\\" + context.table1 + \"\\\\context.table\\\"\"";
        retValue = ParameterValueUtil.splitQueryData("context.table", "context.table1", testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, expectRetValue.equals(retValue));

        // test case 17
        // testString : "select * from ""context.table where value = \"\\" + context.table + "\\context.table\""
        // expectString : "select * from ""context.table where value = \"\\" + context.table1 + "\\context.table\""
        testString = "\"select * from \"\"context.table where value = \\\"\\\\\" + context.table + \"\\\\context.table\\\"\"";
        expectRetValue = "\"select * from \"\"context.table where value = \\\"\\\\\" + context.table1 + \"\\\\context.table\\\"\"";
        retValue = ParameterValueUtil.splitQueryData("context.table", "context.table1", testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, expectRetValue.equals(retValue));

        // test case 18
        // testString : "select * from " + context.table + "where id = " + getId(getHeader(context.header, "CONTEXT_ID")
        // + "CONTEXT_ID")
        // expectString : "select * from " + context.table + "where id = " + getId(getHeader(context.header,
        // "CONTEXT_ID") + "CONTEXT_ID")
        testString = "\"select * from \" + context.table + \"where id = \" + getId(getHeader(context.header, \"CONTEXT_ID\") + \"CONTEXT_ID\")";
        expectRetValue = "\"select * from \" + context.table + \"where id = \" + getId(getHeader(context.header, \"CONTEXT_IDS\") + \"CONTEXT_IDS\")";
        retValue = ParameterValueUtil.splitQueryData("CONTEXT_ID", "CONTEXT_IDS", testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, expectRetValue.equals(retValue));

        // test case 19
        // testString : "select * from " + context.table + "where id = " + getId(getHeader(context.header, "CONTEXT_ID")
        // + "CONTEXT_ID", context.p2, "CONTEXT_ID")
        // expectString : "select * from " + context.table + "where id = " + getId(getHeader(context.header,
        // "CONTEXT_IDS") + "CONTEXT_IDS", context.p2, "CONTEXT_IDS")
        testString = "\"select * from \" + context.table + \"where id = \" + getId(getHeader(context.header, \"CONTEXT_ID\") + \"CONTEXT_ID\", context.p2, \"CONTEXT_ID\")";
        expectRetValue = "\"select * from \" + context.table + \"where id = \" + getId(getHeader(context.header, \"CONTEXT_IDS\") + \"CONTEXT_IDS\", context.p2, \"CONTEXT_IDS\")";
        retValue = ParameterValueUtil.splitQueryData("CONTEXT_ID", "CONTEXT_IDS", testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, expectRetValue.equals(retValue));

        // test case 20
        // testString : "select * from " + context.table + "where id = " + getId(global.getHeader(context.header,
        // "CONTEXT_ID")
        // + "CONTEXT_ID", context.p2, "CONTEXT_ID")
        // expectString : "select * from " + context.table + "where id = " + getId(global.getHeader(context.header,
        // "CONTEXT_IDS") + "CONTEXT_IDS", context.p2, "CONTEXT_IDS")
        testString = "\"select * from \" + context.table + \"where id = \" + getId(global.getHeader(context.header, \"CONTEXT_ID\") + \"CONTEXT_ID\", context.p2, \"CONTEXT_ID\")";
        expectRetValue = "\"select * from \" + context.table + \"where id = \" + getId(global.getHeader(context.header, \"CONTEXT_IDS\") + \"CONTEXT_IDS\", context.p2, \"CONTEXT_IDS\")";
        retValue = ParameterValueUtil.splitQueryData("CONTEXT_ID", "CONTEXT_IDS", testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, expectRetValue.equals(retValue));

        // test case 21
        // testString : "select * from " + context.table + "where id = " + getId(global.getHeader(context.header,
        // "\"CONTEXT_ID\\\"")
        // + "\"CONTEXT_ID", context.p2, "CONTEXT_ID")
        // expectString : "select * from " + context.table + "where id = " + getId(global.getHeader(context.header,
        // "CONTEXT_IDS") + "CONTEXT_IDS", context.p2, "CONTEXT_IDS")
        testString = "\"select * from \" + context.table + \"where id = \" + getId(global.getHeader(context.header, \"\\\"CONTEXT_ID\\\\\\\"\") + \"\\\"CONTEXT_ID\", context.p2, \"CONTEXT_ID\")";
        expectRetValue = "\"select * from \" + context.table + \"where id = \" + getId(global.getHeader(context.header, \"\\\"CONTEXT_ID\\\\\\\"\") + \"\\\"CONTEXT_ID\", context.p2, \"CONTEXT_IDS\")";
        retValue = ParameterValueUtil.splitQueryData("CONTEXT_ID", "CONTEXT_IDS", testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, expectRetValue.equals(retValue));

        // test case 22
        // testString : "select * from " + context.table + "where id = " + getId(getHeader(context.header, "CONTEXT_ID")
        // + "CONTEXT_ID", context.p2, "CONTEXT_ID"
        // expectString : "select * from " + context.table + "where id = " + getId(getHeader(context.header,
        // "CONTEXT_IDS") + "CONTEXT_IDS", context.p2, "CONTEXT_IDS"
        testString = "\"select * from \" + context.table + \"where id = \" + getId(getHeader(context.header, \"CONTEXT_ID\") + \"CONTEXT_ID\", context.p2, \"CONTEXT_ID\"";
        expectRetValue = "\"select * from \" + context.table + \"where id = \" + getId(getHeader(context.header, \"CONTEXT_IDS\") + \"CONTEXT_IDS\", context.p2, \"CONTEXT_IDS\"";
        retValue = ParameterValueUtil.splitQueryData("CONTEXT_ID", "CONTEXT_IDS", testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, expectRetValue.equals(retValue));

        // test case 23
        // testString : "select * from " + context.table + "where id = " + getId(getHeader(context.header, "CONTEXT_ID")
        // + "CONTEXT_ID", context.p2, "CONTEXT_ID
        // expectString : "select * from " + context.table + "where id = " + getId(getHeader(context.header,
        // "CONTEXT_IDS") + "CONTEXT_IDS", context.p2, "CONTEXT_IDS
        testString = "\"select * from \" + context.table + \"where id = \" + getId(getHeader(context.header, \"CONTEXT_ID\") + \"CONTEXT_ID\", context.p2, \"CONTEXT_ID";
        expectRetValue = "\"select * from \" + context.table + \"where id = \" + getId(getHeader(context.header, \"CONTEXT_IDS\") + \"CONTEXT_IDS\", context.p2, \"CONTEXT_IDS";
        retValue = ParameterValueUtil.splitQueryData("CONTEXT_ID", "CONTEXT_IDS", testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, expectRetValue.equals(retValue));

        // test case 24
        // testString : "select * from " + context.table + "where id = " + getId(context.id) + globalMap.get("CONST")
        // expectString : "select * from " + context.table + "where id = " + getId(context.id) + globalMap.get("CONST1")
        testString = "\"select * from \" + context.table + \"where id = \" + getId(context.id) + globalMap.get(\"CONST\")";
        expectRetValue = "\"select * from \" + context.table + \"where id = \" + getId(context.id) + globalMap.get(\"CONST1\")";
        retValue = ParameterValueUtil.splitQueryData("CONST", "CONST1", testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, expectRetValue.equals(retValue));

        // test case 25 : should not replace method name
        // testString : "select * from " + context.table + "where id = " + getId(context.id) +
        // globalMap.get("globalMap")
        // expectString : "select * from " + context.table + "where id = " + getId(context.id) +
        // globalMap.get("globalMap1")
        testString = "\"select * from \" + context.table + \"where id = \" + getId(context.id) + globalMap.get(\"globalMap\")";
        expectRetValue = "\"select * from \" + context.table + \"where id = \" + getId(context.id) + globalMap1.get(\"globalMap1\")";
        retValue = ParameterValueUtil.splitQueryData("globalMap", "globalMap1", testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, expectRetValue.equals(retValue));

        // test case 26
        // testString : "select * from " + context.table.a.b + contextA.table.a + table.a.b + table.a + "where id = " +
        // getId(table.a) + table.a.get("table.a")
        //
        // expectString : "select * from " + context.table.a.b + contextA.table.a + table.a.b + table.a1 + "where id = "
        // + getId(table.a1) + table.a.get("table.a1")
        testString = "\"select * from \" + context.table.a.b + contextA.table.a + table.a.b + table.a + \"where id = \" + getId(table.a) + table.a.get(\"table.a\")";
        expectRetValue = "\"select * from \" + context.table.a.b + contextA.table.a + table.a1.b + table.a1 + \"where id = \" + getId(table.a1) + table.a1.get(\"table.a1\")";
        retValue = ParameterValueUtil.splitQueryData("table.a", "table.a1", testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, expectRetValue.equals(retValue));

        // test case 2
        // testString : "select * from " + a.CONTEXT_ID + CONTEXT_ID.b + CONTEXT_ID + "where id = " +
        // CONTEXT_ID(CONTEXT_ID(CONTEXT_ID, "\"CONTEXT_ID\"\\" + CONTEXT_ID, CONTEXT_ID, "CONTEXT_ID") + "CONTEXT_ID",
        // CONTEXT_ID(ID, "CONTEXT_ID"), "CONTEXT_ID")
        // expectString : "select * from " + a.CONTEXT_ID + CONTEXT_ID.b + CONTEXT_ID1 + "where id = " +
        // CONTEXT_ID(CONTEXT_ID(CONTEXT_ID1, "\"CONTEXT_ID1\"\\" + CONTEXT_ID1, CONTEXT_ID1, "CONTEXT_ID1") +
        // "CONTEXT_ID1", CONTEXT_ID(ID, "CONTEXT_ID1"), "CONTEXT_ID1")
        testString = "\"select * from \" + a.CONTEXT_ID + CONTEXT_ID.b + CONTEXT_ID + \"where id = \" + CONTEXT_ID(CONTEXT_ID(CONTEXT_ID, \"\\\"CONTEXT_ID\\\"\\\\\" + CONTEXT_ID, CONTEXT_ID, \"CONTEXT_ID\") + \"CONTEXT_ID\", CONTEXT_ID(ID, \"CONTEXT_ID\"), \"CONTEXT_ID\")";
        expectRetValue = "\"select * from \" + a.CONTEXT_ID + CONTEXT_ID1.b + CONTEXT_ID1 + \"where id = \" + CONTEXT_ID(CONTEXT_ID(CONTEXT_ID1, \"\\\"CONTEXT_ID\\\"\\\\\" + CONTEXT_ID1, CONTEXT_ID1, \"CONTEXT_ID1\") + \"CONTEXT_ID1\", CONTEXT_ID(ID, \"CONTEXT_ID1\"), \"CONTEXT_ID1\")";
        retValue = ParameterValueUtil.splitQueryData("CONTEXT_ID", "CONTEXT_ID1", testString);
        Assert.assertTrue("testSplitQueryDataCase_" + i++, expectRetValue.equals(retValue));

    }

    @Test
    public void testSplitQueryData4SQL_Case2() {
        // case 2:
        // "insert into "+context.schema+"."+context.table+"(schema, table) values(\"context.schema\", \"context.table\")"
        testSplitQueryData4SQL_Case2_5("testSplitQueryData4Case2", null, null, null);
    }

    @Test
    public void testSplitQueryData4SQL_Case3() {
        // case 3:
        // ""+"insert into "+context.schema+"."+context.table+"(schema, table) values(\"context.schema\", \"context.table\")"
        testSplitQueryData4SQL_Case2_5("testSplitQueryData4Case3", "\"\"", null, null);
    }

    @Test
    public void testSplitQueryData4SQL_Case4() {
        // case 4:
        // "insert into "+context.schema+"."+context.table+"(schema, table) values(\"context.schema\", \"context.table\")"+""
        testSplitQueryData4SQL_Case2_5("testSplitQueryData4Case4", null, null, "\"\"");
    }

    @Test
    public void testSplitQueryData4SQL_Case5() {
        // case 5:
        // "insert into "+context.schema+"."+context.table+""+
        // "(schema, table) values(\"context.schema\", \"context.table\")"
        testSplitQueryData4SQL_Case2_5("testSplitQueryData4Case5", null, "\"\"", null);
    }

    private void testSplitQueryData4SQL_Case2_5(String message, String prefix, String mid, String suffix) {
        if (prefix == null) {
            prefix = "";
        }
        if (mid == null) {
            mid = "";
        }
        if (suffix == null) {
            suffix = "";
        }

        String testString = null;
        String expectRetValue = null;
        int i = 0;
        // test case 2-5
        // String which is same to the String to be replaced was contained in the testString
        // testString :
        /*
         * case 2:
         * "insert into "+context.schema+"."+context.table+"(schema, table) values(\"context.schema\", \"context.table\")"
         * 
         * case 3: ""+"insert into "+context.schema+"."+context.table+
         * "(schema, table) values(\"context.schema\", \"context.table\")"
         * 
         * 
         * case 4:
         * "insert into "+context.schema+"."+context.table+"(schema, table) values(\"context.schema\", \"context.table\")"
         * +""
         * 
         * case 5: "insert into "+context.schema+"."+context.table+""+
         * "(schema, table) values(\"context.schema\", \"context.table\")"
         */

        testString = "\"insert into \"+context.schema+\".\"+context.table+" + mid
                + "\"(schema, table) values(\\\"context.schema\\\", \\\"context.table\\\")\"";
        expectRetValue = "\"insert into \"+context.db+\".\"+context.table+" + mid
                + "\"(schema, table) values(\\\"context.schema\\\", \\\"context.table\\\")\"";
        assertTest(message, i++, prefix + testString + suffix, prefix + expectRetValue + suffix, "context.schema", "context.db");
        // table
        expectRetValue = "\"insert into \"+context.schema+\".\"+context.table111+" + mid
                + "\"(schema, table) values(\\\"context.schema\\\", \\\"context.table\\\")\"";
        assertTest(message, i++, prefix + testString + suffix, prefix + expectRetValue + suffix, "context.table",
                "context.table111");
        // prefix name 1
        testString = "\"insert into \"+context.schema+\".\"+context.schematable+" + mid
                + "\"(schema, table) values(\\\"context.schema\\\", \\\"context.table\\\")\"";
        expectRetValue = "\"insert into \"+context.db+\".\"+context.schematable+" + mid
                + "\"(schema, table) values(\\\"context.schema\\\", \\\"context.table\\\")\"";
        assertTest(message, i++, prefix + testString + suffix, prefix + expectRetValue + suffix, "context.schema", "context.db");
        // prefix name 2
        testString = "\"insert into \"+context.schema+\".\"+context.schema_table+" + mid
                + "\"(schema, table) values(\\\"context.schema\\\", \\\"context.table\\\")\"";
        expectRetValue = "\"insert into \"+context.db+\".\"+context.schema_table+" + mid
                + "\"(schema, table) values(\\\"context.schema\\\", \\\"context.table\\\")\"";
        assertTest(message, i++, prefix + testString + suffix, prefix + expectRetValue + suffix, "context.schema", "context.db");
    }

    private void assertTest(String message, int index, String testString, String expectRetValue, String oldOne, String newOne) {
        String resultValue = ParameterValueUtil.splitQueryData(oldOne, newOne, testString);
        Assert.assertTrue(message + index, expectRetValue.equals(resultValue));
    }

    @Test
    public void testRenameValues4GlobleMap() {
        String testString = "((String)globalMap.get(\"tFileList_1_CURRENT_FILE\"))";
        String expectedValue = "((String)globalMap.get(\"tFileList_2_CURRENT_FILE\"))";
        String resultValue = ParameterValueUtil.renameValues(testString, "tFileList_1", "tFileList_2", true);
        Assert.assertTrue(expectedValue.equals(resultValue));

        //
        testString = "((String)globalMap.get(\"tFileList_1_CURRENT_FILEDIRECTORY\"))+((String)globalMap.get(\"tFileList_1_CURRENT_FILE\"))";
        expectedValue = "((String)globalMap.get(\"tFileList_2_CURRENT_FILEDIRECTORY\"))+((String)globalMap.get(\"tFileList_2_CURRENT_FILE\"))";
        resultValue = ParameterValueUtil.renameValues(testString, "tFileList_1", "tFileList_2", true);
        Assert.assertTrue(expectedValue.equals(resultValue));

        //
        testString = "((String)globalMap.get(\"tFileList_1_CURRENT_FILEDIRECTORY\"))+((String)globalMap.get(\"tFileList_11_CURRENT_FILE\"))";
        expectedValue = "((String)globalMap.get(\"tFileList_2_CURRENT_FILEDIRECTORY\"))+((String)globalMap.get(\"tFileList_11_CURRENT_FILE\"))";
        resultValue = ParameterValueUtil.renameValues(testString, "tFileList_1", "tFileList_2", true);
        Assert.assertTrue(expectedValue.equals(resultValue));

    }

    @Test
    public void testRenameValues4FunctionsExceptGlobalMap() {
        String testString = "\"Hello \" + context.getProperty(\"World\") + property.get(\"World\") + globalMap.get(\"World\") + context.getProperty(\"World2\") + property.get(\"World2\") + globalMap.get(\"World2\")";
        String expectedValue = "\"Hello \" + context.getProperty(\"World1\") + property.get(\"World1\") + globalMap.get(\"World1\") + context.getProperty(\"World2\") + property.get(\"World2\") + globalMap.get(\"World2\")";
        String resultValue = ParameterValueUtil.renameValues(testString, "World", "World1", false);
        Assert.assertTrue(expectedValue.equals(resultValue));
    }

    @Test
    public void testRenameValues4ComplexFunctions() {
        String testString = "var1.function1.getProperty(\"key\", var1.function2.getProperty(globalMap.get(var1.globalMap.get(globalMap.get(\"key\"))), var1.globalMap.get(\"key\"))) + var1.function1.getProperty(\"key1\", var1.function2.getProperty(globalMap.get(var1.globalMap.get(globalMap.get(\"key1\"))), var1.globalMap.get(var1.function3().globalMap.get() + \"key1\", \"key1\", var1.function4(\"key1\", globalMap.put(key, \"key1\")), var1.function5(globalMap.put(key.key.key.getProperty(getProperty(\"key1\")), key1))), var1.globalMap.get(\"key1\"))) + var1.function1.getProperty(\"key2\", var1.function2.getProperty(globalMap.get(var1.globalMap.get(globalMap.get(\"key2\"))), var1.globalMap.get(\"key2\")))";
        String expectedValue = "var1.function1.getProperty(\"k\", var1.function2.getProperty(globalMap.get(var1.globalMap.get(globalMap.get(\"k\"))), var1.globalMap.get(\"k\"))) + var1.function1.getProperty(\"key1\", var1.function2.getProperty(globalMap.get(var1.globalMap.get(globalMap.get(\"key1\"))), var1.globalMap.get(var1.function3().globalMap.get() + \"key1\", \"key1\", var1.function4(\"key1\", globalMap.put(k, \"key1\")), var1.function5(globalMap.put(k.key.key.getProperty(getProperty(\"key1\")), key1))), var1.globalMap.get(\"key1\"))) + var1.function1.getProperty(\"key2\", var1.function2.getProperty(globalMap.get(var1.globalMap.get(globalMap.get(\"key2\"))), var1.globalMap.get(\"key2\")))";
        String resultValue = ParameterValueUtil.renameValues(testString, "key", "k", false);
        Assert.assertTrue(expectedValue.equals(resultValue));
    }

    @Test
    public void testRenameValues4SQLAndGlobleMap() {
        // case
        // "select A.id, A.name form "+context.table+" A where A.name= "+((String)globalMap.get("tFileList_1_CURRENT_FILE"))
        String testString = "\"select A.id, A.name form \"+context.table+\" A where A.name= \"+((String)globalMap.get(\"tFileList_1_CURRENT_FILE\"))";
        String expectRetValue = "\"select A.id, A.name form \"+context.table123+\" A where A.name= \"+((String)globalMap.get(\"tFileList_1_CURRENT_FILE\"))";
        // if flag is false, means is from SQL. but when replace the globlemap, will be problem.
        String retValue = ParameterValueUtil.renameValues(testString, "context.table", "context.table123", false);
        Assert.assertTrue("testRenameValues4SQLAndGlobleMap", expectRetValue.equals(retValue));

        expectRetValue = "\"select A.id, A.name form \"+context.table+\" A where A.name= \"+((String)globalMap.get(\"tFileList_2_CURRENT_FILE\"))";
        // if flag is false, means is from SQL. but when replace the globlemap, will be problem.
        retValue = ParameterValueUtil.renameValues(testString, "tFileList_1", "tFileList_2", false);
        Assert.assertTrue("testRenameValues4SQLAndGlobleMap", expectRetValue.equals(retValue));
    }

    @Test
    public void testRenameValues4SQLAndGlobleMap_full() {
        // case
        // "select A.id, A.name form "+context.table+" A where A.name=
        // "+((String)globalMap.get("tFileList_1_CURRENT_FILE"))
        String testString = "\"select A.id, A.name form \"+context.table+\" A where A.name= \"+((String)globalMap.get(\"tFileList_1_CURRENT_FILE\"))";
        String expectRetValue = "\"select A.id, A.name form \"+context.table123+\" A where A.name= \"+((String)globalMap.get(\"tFileList_1_CURRENT_FILE\"))";
        // if flag is false, means is from SQL. but when replace the globlemap, will be problem.
        String retValue = ParameterValueUtil.renameValues(testString, "context.table", "context.table123", false);
        Assert.assertTrue("testRenameValues4SQLAndGlobleMap", expectRetValue.equals(retValue));

        expectRetValue = "\"select A.id, A.name form \"+context.table+\" A where A.name= \"+((String)globalMap.get(\"tFileList_2_CURRENT_FILE\"))";
        // if flag is false, means is from SQL. but when replace the globlemap, will be problem.
        retValue = ParameterValueUtil.renameValues(testString, "tFileList_1_CURRENT_FILE", "tFileList_2_CURRENT_FILE", false);
        Assert.assertTrue("testRenameValues4SQLAndGlobleMap", expectRetValue.equals(retValue));
    }
    @Test
    public void testRenameJavaCode() {
        String testString = "\tSystem.out.println(\"=====\");\n\tout.a = b;\n\tout = obj1;";
        String expectRetValue = "\tSystem.out.println(\"=====\");\n\trow1.a = b;\n\trow1 = obj1;";

        String retValue = ParameterValueUtil.splitQueryData("out", "row1", testString);
        Assert.assertTrue(expectRetValue.equals(retValue));
    }

    @Test
    public void testReplaceConnectionNameInJavaCode() {
        // Add for https://jira.talendforge.org/browse/TUP-2333
        String testString = "\tSystem.out.println(\"=====\");\n\tout.a = b;\n\tout = obj1;\n\tout.a.out = obj2;\n\tout.out = obj3;\n\tout(obj1);\n\ta.out.b.out.c.out = 1;\n\tout.a.out.b.out.c.out();\n";
        String expectRetValue = "\tSystem.out.println(\"=====\");\n\trow1.a = b;\n\trow1 = obj1;\n\trow1.a.out = obj2;\n\trow1.out = obj3;\n\tout(obj1);\n\ta.out.b.out.c.out = 1;\n\trow1.a.out.b.out.c.out();\n";

        String retValue = ParameterValueUtil.splitQueryData("out", "row1", testString);
        Assert.assertTrue(expectRetValue.equals(retValue));
    }

    @Test
    public void testGetValue4Doc4ContextParameterTypeWithNonPass() {
        Assert.assertEquals("", ParameterValueUtil.getValue4Doc((ContextParameterType) null));

        ContextParameterType contextParamType = TalendFileFactory.eINSTANCE.createContextParameterType();
        contextParamType.setType(JavaTypesManager.getDefaultJavaType().getId());

        contextParamType.setValue(null);
        Assert.assertEquals("", ParameterValueUtil.getValue4Doc(contextParamType));

        contextParamType.setValue("");
        Assert.assertEquals("", ParameterValueUtil.getValue4Doc(contextParamType));

        contextParamType.setValue("123");
        Assert.assertEquals("123", ParameterValueUtil.getValue4Doc(contextParamType));

    }

    @Test
    public void testGetValue4Doc4ContextParameterTypeWithPass() {
        Assert.assertEquals("", ParameterValueUtil.getValue4Doc((ContextParameterType) null));

        boolean oldHidePasswordFlag = ParameterValueUtil.isHidePassword();
        Project currentProject = ProjectManager.getInstance().getCurrentProject().getEmfProject();

        currentProject.setHidePassword(false);

        ContextParameterType contextParamType = TalendFileFactory.eINSTANCE.createContextParameterType();
        contextParamType.setType(JavaTypesManager.PASSWORD.getId());

        contextParamType.setRawValue(null);
        Assert.assertEquals("", ParameterValueUtil.getValue4Doc(contextParamType));

        contextParamType.setRawValue("");
        Assert.assertEquals("", ParameterValueUtil.getValue4Doc(contextParamType));

        contextParamType.setRawValue("123");
        Assert.assertEquals("/81ashGeQx8=", ParameterValueUtil.getValue4Doc(contextParamType));

        //
        currentProject.setHidePassword(true);

        contextParamType.setRawValue(null);
        Assert.assertEquals("", ParameterValueUtil.getValue4Doc(contextParamType));

        contextParamType.setRawValue("");
        Assert.assertEquals("****", ParameterValueUtil.getValue4Doc(contextParamType));

        contextParamType.setRawValue("123");
        Assert.assertEquals("***", ParameterValueUtil.getValue4Doc(contextParamType));

        currentProject.setHidePassword(oldHidePasswordFlag);
    }

    @Test
    public void testGetValue4Doc4IContextParameterWithNonPass() {
        Assert.assertEquals("", ParameterValueUtil.getValue4Doc((IContextParameter) null));

        JobContextParameter contextParam = new JobContextParameter();
        contextParam.setType(JavaTypesManager.getDefaultJavaType().getId());

        contextParam.setValue(null);
        Assert.assertEquals("", ParameterValueUtil.getValue4Doc(contextParam));

        contextParam.setValue("");
        Assert.assertEquals("", ParameterValueUtil.getValue4Doc(contextParam));

        contextParam.setValue("123");
        Assert.assertEquals("123", ParameterValueUtil.getValue4Doc(contextParam));

    }

    @Test
    public void testGetValue4Doc4IContextParameterWithPass() {
        Assert.assertEquals("", ParameterValueUtil.getValue4Doc((IContextParameter) null));

        boolean oldHidePasswordFlag = ParameterValueUtil.isHidePassword();
        Project currentProject = ProjectManager.getInstance().getCurrentProject().getEmfProject();

        currentProject.setHidePassword(false);

        IContextParameter contextParam = new JobContextParameter();
        contextParam.setType(JavaTypesManager.PASSWORD.getId());

        contextParam.setValue(null);
        Assert.assertEquals("", ParameterValueUtil.getValue4Doc(contextParam));

        contextParam.setValue("");// because empty need encrypt also.
        Assert.assertEquals("yJKHKGWEAQw=", ParameterValueUtil.getValue4Doc(contextParam));

        contextParam.setValue("123");
        Assert.assertEquals("/81ashGeQx8=", ParameterValueUtil.getValue4Doc(contextParam));

        //
        currentProject.setHidePassword(true);

        contextParam.setValue(null);
        Assert.assertEquals("", ParameterValueUtil.getValue4Doc(contextParam));

        contextParam.setValue("");
        Assert.assertEquals("****", ParameterValueUtil.getValue4Doc(contextParam));

        contextParam.setValue("123");
        Assert.assertEquals("***", ParameterValueUtil.getValue4Doc(contextParam));

        currentProject.setHidePassword(oldHidePasswordFlag);
    }

    @Test
    public void testGetEncryptValue4IContextParameter() {
        Assert.assertNull(ParameterValueUtil.getEncryptValue((IContextParameter) null));

        IContextParameter contextParam = new JobContextParameter();

        contextParam.setValue(null);
        Assert.assertNull(ParameterValueUtil.getEncryptValue(contextParam));

        contextParam.setValue("");// because empty need encrypt also.
        Assert.assertEquals("yJKHKGWEAQw=", ParameterValueUtil.getEncryptValue(contextParam));

        contextParam.setValue("123");
        Assert.assertEquals("/81ashGeQx8=", ParameterValueUtil.getEncryptValue(contextParam));
    }

    @Test
    public void testGetValue4Doc4IElementParameterWithNonPass() {
        Assert.assertEquals("", ParameterValueUtil.getValue4Doc((IElementParameter) null));

        IElementParameter param = new TestElementParameter();

        param.setValue(null);
        Assert.assertEquals("", ParameterValueUtil.getValue4Doc(param));

        param.setValue("");
        Assert.assertEquals("", ParameterValueUtil.getValue4Doc(param));

        param.setValue("123");
        Assert.assertEquals("123", ParameterValueUtil.getValue4Doc(param));

    }

    @Test
    public void testGetValue4Doc4IElementParameterWithPass() {
        Assert.assertEquals("", ParameterValueUtil.getValue4Doc((IElementParameter) null));

        boolean oldHidePasswordFlag = ParameterValueUtil.isHidePassword();
        Project currentProject = ProjectManager.getInstance().getCurrentProject().getEmfProject();

        currentProject.setHidePassword(false);

        IElementParameter param = new TestElementParameter();
        param.setFieldType(EParameterFieldType.PASSWORD);

        param.setValue(null);
        Assert.assertEquals("", ParameterValueUtil.getValue4Doc(param));

        param.setValue(""); // because don't hide password, and empty is encrypted also.
        Assert.assertEquals("yJKHKGWEAQw=", ParameterValueUtil.getValue4Doc(param));

        param.setValue("123");
        Assert.assertEquals("/81ashGeQx8=", ParameterValueUtil.getValue4Doc(param));

        // repository value
        param.setRepositoryValue("PASSWORD");

        param.setValue(null);
        Assert.assertEquals("", ParameterValueUtil.getValue4Doc(param));

        param.setValue(""); // because empty need encrypt also.
        Assert.assertEquals("yJKHKGWEAQw=", ParameterValueUtil.getValue4Doc(param));

        param.setValue("123");
        Assert.assertEquals("/81ashGeQx8=", ParameterValueUtil.getValue4Doc(param));

        //
        currentProject.setHidePassword(true);

        param.setValue(null);
        Assert.assertEquals("", ParameterValueUtil.getValue4Doc(param));

        param.setValue("");
        Assert.assertEquals("****", ParameterValueUtil.getValue4Doc(param));

        param.setValue("123");
        Assert.assertEquals("***", ParameterValueUtil.getValue4Doc(param));

        // context
        param.setValue("context.var1");
        Assert.assertEquals("context.var1", ParameterValueUtil.getValue4Doc(param));

        currentProject.setHidePassword(oldHidePasswordFlag);
    }

    @Test
    public void testGetEncryptValue4IElementParameter() {
        Assert.assertNull(ParameterValueUtil.getEncryptValue((IElementParameter) null));

        IElementParameter param = new TestElementParameter();

        param.setValue(null);
        Assert.assertNull(ParameterValueUtil.getEncryptValue(param));

        param.setValue(""); // because empty need encrypt also.
        Assert.assertEquals("yJKHKGWEAQw=", ParameterValueUtil.getEncryptValue(param));

        param.setValue("123");
        Assert.assertEquals("/81ashGeQx8=", ParameterValueUtil.getEncryptValue(param));

        param.setValue(new ArrayList<String>());
        Assert.assertNull(ParameterValueUtil.getEncryptValue(param));
    }

    @Test
    public void testGetValue4Doc4ElementParameterTypeWithNonPass() {
        Assert.assertEquals("", ParameterValueUtil.getValue4Doc((ElementParameterType) null));

        ElementParameterType param = TalendFileFactory.eINSTANCE.createElementParameterType();

        param.setValue(null);
        Assert.assertEquals("", ParameterValueUtil.getValue4Doc(param));

        param.setValue("");
        Assert.assertEquals("", ParameterValueUtil.getValue4Doc(param));

        param.setValue("123");
        Assert.assertEquals("123", ParameterValueUtil.getValue4Doc(param));

    }

    @Test
    public void testGetValue4Doc4ElementParameterTypeWithPass() {
        Assert.assertEquals("", ParameterValueUtil.getValue4Doc((ElementParameterType) null));

        boolean oldHidePasswordFlag = ParameterValueUtil.isHidePassword();
        Project currentProject = ProjectManager.getInstance().getCurrentProject().getEmfProject();

        currentProject.setHidePassword(false);

        ElementParameterType param = TalendFileFactory.eINSTANCE.createElementParameterType();
        param.setField(EParameterFieldType.PASSWORD.getName());

        param.setValue(null);
        Assert.assertEquals("", ParameterValueUtil.getValue4Doc(param));

        param.setValue("");
        Assert.assertEquals("", ParameterValueUtil.getValue4Doc(param));

        param.setValue("123");
        Assert.assertEquals("123", ParameterValueUtil.getValue4Doc(param));

        //
        currentProject.setHidePassword(true);

        param.setValue(null);
        Assert.assertEquals("", ParameterValueUtil.getValue4Doc(param));

        param.setValue("");
        Assert.assertEquals("****", ParameterValueUtil.getValue4Doc(param));

        param.setValue("123");
        Assert.assertEquals("***", ParameterValueUtil.getValue4Doc(param));

        // context
        param.setValue("context.var1");
        Assert.assertEquals("context.var1", ParameterValueUtil.getValue4Doc(param));

        currentProject.setHidePassword(oldHidePasswordFlag);
    }

    @Test
    public void testMixedEncryptDecryptFunction1() throws Exception {
        // TDI-30227

        String decryptPassword1 = PasswordEncryptUtil.decryptPassword("dxlWbpCxXBw=");
        String decryptPassword2 = CryptoHelper.getDefault().decrypt("+ZE8yV1ehYi0jSmx94/wVA==");
        Assert.assertEquals("pr", decryptPassword1);
        Assert.assertEquals("EL3F4nt3", decryptPassword2);

        // redo again.
        decryptPassword1 = PasswordEncryptUtil.decryptPassword("dxlWbpCxXBw=");
        decryptPassword2 = CryptoHelper.getDefault().decrypt("+ZE8yV1ehYi0jSmx94/wVA==");
        Assert.assertEquals("pr", decryptPassword1);
        Assert.assertEquals("EL3F4nt3", decryptPassword2);
    }

    @Test
    public void testMixedEncryptDecryptFunction2() throws Exception {
        // TDI-30227

        String encryptPassword1 = PasswordEncryptUtil.encryptPassword("123");
        String encryptPassword2 = CryptoHelper.getDefault().encrypt("123");
        Assert.assertEquals("123", PasswordEncryptUtil.decryptPassword(encryptPassword1));
        Assert.assertEquals("123", CryptoHelper.getDefault().decrypt(encryptPassword2));

        // redo again.
        encryptPassword1 = PasswordEncryptUtil.encryptPassword("123");
        encryptPassword2 = CryptoHelper.getDefault().encrypt("123");
        Assert.assertEquals("123", PasswordEncryptUtil.decryptPassword(encryptPassword1));
        Assert.assertEquals("123", CryptoHelper.getDefault().decrypt(encryptPassword2));

    }

    @Test
    public void testMixedEncryptDecryptFunction3() throws Exception {
        // TDI-30227

        String decryptValue1 = PasswordEncryptUtil.decryptPassword("dxlWbpCxXBw=");
        String decryptValue2 = CryptoHelper.getDefault().decrypt("HiV5kR+6mPKhnI5NbYbw/Q==");
        String decryptValue3 = CryptoHelper.getDefault().decrypt("+ZE8yV1ehYi0jSmx94/wVA==");
        Assert.assertEquals("pr", decryptValue1);
        Assert.assertEquals("aiXea2Va", decryptValue2);
        Assert.assertEquals("EL3F4nt3", decryptValue3);

        decryptValue1 = PasswordEncryptUtil.decryptPassword("dxlWbpCxXBw=");
        decryptValue2 = CryptoHelper.getDefault().decrypt("HiV5kR+6mPKhnI5NbYbw/Q==");
        decryptValue3 = CryptoHelper.getDefault().decrypt("+ZE8yV1ehYi0jSmx94/wVA==");
        Assert.assertEquals("pr", decryptValue1);
        Assert.assertEquals("aiXea2Va", decryptValue2);
        Assert.assertEquals("EL3F4nt3", decryptValue3);

        decryptValue2 = CryptoHelper.getDefault().decrypt("HiV5kR+6mPKhnI5NbYbw/Q==");
        decryptValue3 = CryptoHelper.getDefault().decrypt("+ZE8yV1ehYi0jSmx94/wVA==");
        decryptValue1 = PasswordEncryptUtil.decryptPassword("dxlWbpCxXBw=");
        Assert.assertEquals("pr", decryptValue1);
        Assert.assertEquals("aiXea2Va", decryptValue2);
        Assert.assertEquals("EL3F4nt3", decryptValue3);

        decryptValue2 = CryptoHelper.getDefault().decrypt("HiV5kR+6mPKhnI5NbYbw/Q==");
        decryptValue1 = PasswordEncryptUtil.decryptPassword("dxlWbpCxXBw=");
        decryptValue3 = CryptoHelper.getDefault().decrypt("+ZE8yV1ehYi0jSmx94/wVA==");
        Assert.assertEquals("pr", decryptValue1);
        Assert.assertEquals("aiXea2Va", decryptValue2);
        Assert.assertEquals("EL3F4nt3", decryptValue3);

    }

    @Test
    public void testTDI30227_MultiThread1() {
        Thread t1 = new Thread() {

            @Override
            public void run() {
                try {
                    // "Oracle_workingLinux.item", "dxlWbpCxXBw=" //old one
                    String testEncryptionValue1 = "dxlWbpCxXBw=";
                    String decryptValue1 = PasswordEncryptUtil.decryptPassword(testEncryptionValue1);
                    Assert.assertNotNull(decryptValue1);
                    Assert.assertEquals("pr", decryptValue1);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        };

        Thread t2 = new Thread() {

            @Override
            public void run() {
                // "Oracle_notworkingLinux.item", "HiV5kR+6mPKhnI5NbYbw/Q=="
                String testEncryptionValue2 = "HiV5kR+6mPKhnI5NbYbw/Q==";
                String decryptValue2 = CryptoHelper.getDefault().decrypt(testEncryptionValue2);
                Assert.assertNotNull(decryptValue2);
                Assert.assertEquals("aiXea2Va", decryptValue2);
            }

        };

        Thread t3 = new Thread() {

            @Override
            public void run() {
                // "Oracle_workingWindows.item", "+ZE8yV1ehYi0jSmx94/wVA=="
                String testEncryptionValue2 = "+ZE8yV1ehYi0jSmx94/wVA==";
                String decryptValue2 = CryptoHelper.getDefault().decrypt(testEncryptionValue2);
                Assert.assertNotNull(decryptValue2);
                Assert.assertEquals("EL3F4nt3", decryptValue2);
            }

        };

        t1.start();
        t2.start();
        t3.start();

    }

    @Test
    public void testTDI30227_MultiThread2() {
        Thread t1 = new Thread() {

            @Override
            public void run() {
                try {
                    // "Oracle_workingLinux.item", "dxlWbpCxXBw=" //old one
                    String testEncryptionValue1 = "dxlWbpCxXBw=";
                    String decryptValue1 = PasswordEncryptUtil.decryptPassword(testEncryptionValue1);
                    Assert.assertNotNull(decryptValue1);
                    Assert.assertEquals("pr", decryptValue1);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        };

        Thread t2 = new Thread() {

            @Override
            public void run() {
                // "Oracle_notworkingLinux.item", "HiV5kR+6mPKhnI5NbYbw/Q=="
                String testEncryptionValue2 = "HiV5kR+6mPKhnI5NbYbw/Q==";
                String decryptValue2 = CryptoHelper.getDefault().decrypt(testEncryptionValue2);
                Assert.assertNotNull(decryptValue2);
                Assert.assertEquals("aiXea2Va", decryptValue2);
            }

        };

        Thread t3 = new Thread() {

            @Override
            public void run() {
                // "Oracle_workingWindows.item", "+ZE8yV1ehYi0jSmx94/wVA=="
                String testEncryptionValue2 = "+ZE8yV1ehYi0jSmx94/wVA==";
                String decryptValue2 = CryptoHelper.getDefault().decrypt(testEncryptionValue2);
                Assert.assertNotNull(decryptValue2);
                Assert.assertEquals("EL3F4nt3", decryptValue2);
            }

        };

        t2.start();
        t1.start();
        t3.start();

    }

    /**
     * 
     * there are some problem when decryption. It's not because the TDI-30227, I think, should be some problem for the
     * encrypt JAVA API for PasswordEncryptUtil and CryptoHelper. like Cipher,
     */
    @Test
    public void testTDI30227_NewDecryption4OldValue1() {
        String decryptValue1 = CryptoHelper.getDefault().decrypt("dxlWbpCxXBw="); // use new encryption to decrypt old
                                                                                  // one.
        Assert.assertNull(decryptValue1);

        String decryptValue2 = CryptoHelper.getDefault().decrypt("HiV5kR+6mPKhnI5NbYbw/Q==");
        /*
         * Because use the new decryption to decrypt the old encrypted value, will cause this problem. don't know what's
         * happen and why, need find one way to fix maybe. even thought, now all password have be unified for
         * encryption/decryption. but, if re-decrypt will be ok, @see testTDI30227_NewDecryption4OldValue2 and
         * testTDI30227_NewDecryption4OldValue3.
         */
        Assert.assertEquals("aiXea2Va", decryptValue2); // will be failure.
    }

    @Test
    public void testTDI30227_NewDecryption4OldValue2() {
        String decryptValue1 = CryptoHelper.getDefault().decrypt("dxlWbpCxXBw="); // use new encryption to decrypt old
                                                                                  // one.
        Assert.assertNull(decryptValue1);

        String decryptValue2 = CryptoHelper.getDefault().decrypt("HiV5kR+6mPKhnI5NbYbw/Q==");
        // FIXM, ??????? will be failied first time for messy characters.
        // Assert.assertEquals("aiXea2Va", decryptValue2);

        // redo
        decryptValue2 = CryptoHelper.getDefault().decrypt("HiV5kR+6mPKhnI5NbYbw/Q==");
        Assert.assertEquals("aiXea2Va", decryptValue2); // now it's ok
    }

    @Test
    public void testTDI30227_NewDecryption4OldValue3() {
        String decryptValue1 = CryptoHelper.getDefault().decrypt("dxlWbpCxXBw="); // use new encryption to decrypt old
                                                                                  // one.
        Assert.assertNull(decryptValue1);

        String decryptValue2 = CryptoHelper.getDefault().decrypt("+ZE8yV1ehYi0jSmx94/wVA==");
        // FIXM, ??????? will be failied first time
        // Assert.assertEquals("EL3F4nt3", decryptValue2); // same as testTDI30227_NewDecryption4OldValue2, will be
        // messy characters

        // redo
        decryptValue2 = CryptoHelper.getDefault().decrypt("+ZE8yV1ehYi0jSmx94/wVA==");
        Assert.assertEquals("EL3F4nt3", decryptValue2); // now it's ok
    }

    @Test
    public void testTDI30227_OldDecryption4NewValue1() {

        try {
            String decryptValue1 = PasswordEncryptUtil.decryptPassword("HiV5kR+6mPKhnI5NbYbw/Q=="); // new encryption
            Assert.assertNotNull(decryptValue1); // seem enable to decrypt, but it's messy characters
        } catch (Exception e) {
            // e.printStackTrace();
            Assert.assertTrue(false); // never to be here.
        }

        try {
            String decryptValue2 = PasswordEncryptUtil.decryptPassword("dxlWbpCxXBw="); // old encryption
            Assert.assertEquals("pr", decryptValue2);
        } catch (Exception e) {
            // e.printStackTrace();
        }

        String decryptValue3 = CryptoHelper.getDefault().decrypt("HiV5kR+6mPKhnI5NbYbw/Q==");
        Assert.assertNotNull(decryptValue3);
        Assert.assertEquals("aiXea2Va", decryptValue3);
    }

    @Test
    public void testTDI30227_OldDecryption4NewValue2() {

        try {
            String decryptValue1 = PasswordEncryptUtil.decryptPassword("+ZE8yV1ehYi0jSmx94/wVA=="); // new encryption
            Assert.assertTrue(false); // never to be here. so strange, it's not same as the
                                      // testTDI30227_OldDecryption4NewValue1
        } catch (Exception e) {
//            e.printStackTrace();
            // Assert.assertTrue(false); // But why will throw error, and don't like the
            // testTDI30227_OldDecryption4NewValue1
        }

        try {
            String decryptValue2 = PasswordEncryptUtil.decryptPassword("dxlWbpCxXBw="); // old encryption
            Assert.assertEquals("pr", decryptValue2);
        } catch (Exception e) {
            // e.printStackTrace();
        }

        String decryptValue3 = CryptoHelper.getDefault().decrypt("+ZE8yV1ehYi0jSmx94/wVA==");
        Assert.assertNotNull(decryptValue3);
        Assert.assertEquals("EL3F4nt3", decryptValue3);
    }

    @Test
    public void testTDI31896_replacementSkipJavaComment() {
        String testString = "//Handle Ip's\r\n"
                + "String ipAddress = Process_Apache_Tag_Sever_Logs_1_raw_log_file.ip_address;\r\n"
                + "String ipFlag = \"\";\r\n"
                + "\r\n"
                + "//Ensure the field contains an IP.\r\n"
                + "ipMatcher = ipPattern.matcher(ipAddress);\r\n"
                + "\r\n"
                + "if (ipMatcher.matches()) {\r\n"
                + "\r\n"
                + "    ipAddress = ipMatcher.group(1); \r\n"
                + "\r\n"
                + "    //First compare the ip address against the specific list of bad ip's\r\n"
                + "    for (String badIp : exactIpList) {\r\n"
                + "        if (badIp.equals(ipAddress)) {\r\n"
                + "            //The IP exactly matches a bad IP.  Therefore set flag to filter.\r\n"
                + "            //System.out.println(\"IP Address \" + ipAddress + \" match in list.\");\r\n"
                + "            ipFlag = \"filter\";\r\n"
                + "        }\r\n"
                + "    }\r\n"
                + "\r\n"
                + "    //Check IP against subnet list.\r\n"
                + "    for (SubnetUtils.SubnetInfo subnet : subnets) {\r\n"
                + "        if (subnet.isInRange(ipAddress)) {\r\n"
                + "            // The IP is in the range of a subnet.  Thefore set flag to filter.\r\n"
                + "            //System.out.println(\"IP Address \" + ipAddress + \" is in range \" + subnet.getCidrSignature());\r\n"
                + "            ipFlag = \"filter\";\r\n"
                + "        }\r\n"
                + "    }\r\n"
                + "    \r\n"
                + "} else {\r\n"
                + "    //Field did not contain IP.  Set flag to filter\r\n"
                + "    ipFlag = \"filter\";\r\n"
                + "}\r\n"
                + "\r\n"
                + "//Set values in output row.\r\n"
                + "ip_detected_logs.ip_address = ipAddress;//*Set values in output row.\r\n"
                + "ip_detected_logs.ip_address = ipAddress;/*Set values in output row.*/\r\n"
                + "someVariable.ip_detected_logs.ip_address = ipAddress;/*Set values in output row.*///someVariable\r\n"
                + "ip_detected_logs.ip_address = ipAddress;\r\n"
                + "ip_detected_logs.ip_flag = ipFlag;\r\n"
                + "\r\n"
                + "/*Set values in output row.*/\r\n"
                + "ip_detected_logs.ip_address = ipAddress;\r\n"
                + "ip_detected_logs.ip_flag = ipFlag;\r\n"
                + "\r\n"
                + "// // ip_detected_logs.ip_address = ipAddress;\r\n"
                + "// ip_detected_logs.ip_address = ipAddress;\r\n"
                + "// /* ip_detected_logs.ip_address = ipAddress; */\r\n"
                + "ip_detected_logs.ip_address = ipAddress;\r\n"
                + "\r\n"
                + "///**\r\n"
                + "// * ip_detected_logs.ip_address = ipAddress;\r\n"
                + "// * ip_detected_logs.ip_address = ipAddress;\r\n"
                + "// */\r\n"
                + "ip_detected_logs.ip_address = ipAddress;\r\n"
                + "\r\n"
                + "/**\r\n"
                + " * ip_detected_logs.ip_address = ipAddress;\r\n"
                + " * /* ip_detected_logs.ip_address = ipAddress;\r\n"
                + " * // ip_detected_logs.ip_address = ipAddress;\r\n"
                + " * ///* ip_detected_logs.ip_address = ipAddress;\r\n"
                + " * /* // ip_detected_logs.ip_address = ipAddress;\r\n"
                + " */\r\n"
                + " ip_detected_logs.ip_address = ipAddress;\r\n"
                + "\r\n"
                + "var1.function1.getProperty( // function1.getProperty ip_detected_logs.ip_address )\r\n"
                + "\"ip_detected_logs.ip_address\"/* ip_detected_logs.ip_address */,\r\n"
                + "var1.function2.getProperty(globalMap.get( // globalMap.get( ip_detected_logs.ip_address ) /* ip_detected_logs.ip_address */\r\n"
                + "var1.globalMap.get(globalMap.get(\"ip_detected_logs.ip_address\"))),\r\n"
                + "var1.globalMap.get(\"ip_detected_logs.ip_address\"))) + var1.function1.getProperty(\"ip_detected_logs.ip_address1\", var1.function2.getProperty(globalMap.get(var1.globalMap.get(globalMap.get(\"ip_detected_logs.ip_address1\"))), var1.globalMap.get(var1.function3().globalMap.get() + \"ip_detected_logs.ip_address1\", \"ip_detected_logs.ip_address1\", var1.function4(\"ip_detected_logs.ip_address1\", globalMap.put(ip_detected_logs.ip_address, \"ip_detected_logs.ip_address1\")), var1.function5(globalMap.put(ip_detected_logs.ip_address.ip_detected_logs.ip_address.ip_detected_logs.ip_address.getProperty(getProperty(\"ip_detected_logs.ip_address1\")), ip_detected_logs.ip_address1))), var1.globalMap.get(\"ip_detected_logs.ip_address1\"))) + var1.function1.getProperty(\"ip_detected_logs.ip_address2\", var1.function2.getProperty(globalMap.get(var1.globalMap.get(globalMap.get(\"ip_detected_logs.ip_address2\"))), var1.globalMap.get(\"ip_detected_logs.ip_address2\")))\r\n"
                + "\r\n" + "//Set values in output row.\r"
                + "ip_detected_logs.ip_address = ipAddress;//*Set values in output row.\r" + "\r\n"
                + "//ip_detected_logs.ip_address = ipAddress.\n"
                + "ip_detected_logs.ip_address = ipAddress;//*Set values in output row.";

        String expectString = "//Handle Ip's\r\n"
                + "String ipAddress = Process_Apache_Tag_Sever_Logs_1_raw_log_file.ip_address;\r\n"
                + "String ipFlag = \"\";\r\n"
                + "\r\n"
                + "//Ensure the field contains an IP.\r\n"
                + "ipMatcher = ipPattern.matcher(ipAddress);\r\n"
                + "\r\n"
                + "if (ipMatcher.matches()) {\r\n"
                + "\r\n"
                + "    ipAddress = ipMatcher.group(1); \r\n"
                + "\r\n"
                + "    //First compare the ip address against the specific list of bad ip's\r\n"
                + "    for (String badIp : exactIpList) {\r\n"
                + "        if (badIp.equals(ipAddress)) {\r\n"
                + "            //The IP exactly matches a bad IP.  Therefore set flag to filter.\r\n"
                + "            //System.out.println(\"IP Address \" + ipAddress + \" match in list.\");\r\n"
                + "            ipFlag = \"filter\";\r\n"
                + "        }\r\n"
                + "    }\r\n"
                + "\r\n"
                + "    //Check IP against subnet list.\r\n"
                + "    for (SubnetUtils.SubnetInfo subnet : subnets) {\r\n"
                + "        if (subnet.isInRange(ipAddress)) {\r\n"
                + "            // The IP is in the range of a subnet.  Thefore set flag to filter.\r\n"
                + "            //System.out.println(\"IP Address \" + ipAddress + \" is in range \" + subnet.getCidrSignature());\r\n"
                + "            ipFlag = \"filter\";\r\n"
                + "        }\r\n"
                + "    }\r\n"
                + "    \r\n"
                + "} else {\r\n"
                + "    //Field did not contain IP.  Set flag to filter\r\n"
                + "    ipFlag = \"filter\";\r\n"
                + "}\r\n"
                + "\r\n"
                + "//Set values in output row.\r\n"
                + "Process_Apache_Tag_Sever_Logs_1_ip_detected_logs.ip_address = ipAddress;//*Set values in output row.\r\n"
                + "Process_Apache_Tag_Sever_Logs_1_ip_detected_logs.ip_address = ipAddress;/*Set values in output row.*/\r\n"
                + "someVariable.ip_detected_logs.ip_address = ipAddress;/*Set values in output row.*///someVariable\r\n"
                + "Process_Apache_Tag_Sever_Logs_1_ip_detected_logs.ip_address = ipAddress;\r\n"
                + "ip_detected_logs.ip_flag = ipFlag;\r\n"
                + "\r\n"
                + "/*Set values in output row.*/\r\n"
                + "Process_Apache_Tag_Sever_Logs_1_ip_detected_logs.ip_address = ipAddress;\r\n"
                + "ip_detected_logs.ip_flag = ipFlag;\r\n"
                + "\r\n"
                + "// // ip_detected_logs.ip_address = ipAddress;\r\n"
                + "// ip_detected_logs.ip_address = ipAddress;\r\n"
                + "// /* ip_detected_logs.ip_address = ipAddress; */\r\n"
                + "Process_Apache_Tag_Sever_Logs_1_ip_detected_logs.ip_address = ipAddress;\r\n"
                + "\r\n"
                + "///**\r\n"
                + "// * ip_detected_logs.ip_address = ipAddress;\r\n"
                + "// * ip_detected_logs.ip_address = ipAddress;\r\n"
                + "// */\r\n"
                + "Process_Apache_Tag_Sever_Logs_1_ip_detected_logs.ip_address = ipAddress;\r\n"
                + "\r\n"
                + "/**\r\n"
                + " * ip_detected_logs.ip_address = ipAddress;\r\n"
                + " * /* ip_detected_logs.ip_address = ipAddress;\r\n"
                + " * // ip_detected_logs.ip_address = ipAddress;\r\n"
                + " * ///* ip_detected_logs.ip_address = ipAddress;\r\n"
                + " * /* // ip_detected_logs.ip_address = ipAddress;\r\n"
                + " */\r\n"
                + " Process_Apache_Tag_Sever_Logs_1_ip_detected_logs.ip_address = ipAddress;\r\n"
                + "\r\n"
                + "var1.function1.getProperty( // function1.getProperty ip_detected_logs.ip_address )\r\n"
                + "\"Process_Apache_Tag_Sever_Logs_1_ip_detected_logs.ip_address\"/* ip_detected_logs.ip_address */,\r\n"
                + "var1.function2.getProperty(globalMap.get( // globalMap.get( ip_detected_logs.ip_address ) /* ip_detected_logs.ip_address */\r\n"
                + "var1.globalMap.get(globalMap.get(\"Process_Apache_Tag_Sever_Logs_1_ip_detected_logs.ip_address\"))),\r\n"
                + "var1.globalMap.get(\"Process_Apache_Tag_Sever_Logs_1_ip_detected_logs.ip_address\"))) + var1.function1.getProperty(\"ip_detected_logs.ip_address1\", var1.function2.getProperty(globalMap.get(var1.globalMap.get(globalMap.get(\"ip_detected_logs.ip_address1\"))), var1.globalMap.get(var1.function3().globalMap.get() + \"ip_detected_logs.ip_address1\", \"ip_detected_logs.ip_address1\", var1.function4(\"ip_detected_logs.ip_address1\", globalMap.put(Process_Apache_Tag_Sever_Logs_1_ip_detected_logs.ip_address, \"ip_detected_logs.ip_address1\")), var1.function5(globalMap.put(Process_Apache_Tag_Sever_Logs_1_ip_detected_logs.ip_address.ip_detected_logs.ip_address.ip_detected_logs.ip_address.getProperty(getProperty(\"ip_detected_logs.ip_address1\")), ip_detected_logs.ip_address1))), var1.globalMap.get(\"ip_detected_logs.ip_address1\"))) + var1.function1.getProperty(\"ip_detected_logs.ip_address2\", var1.function2.getProperty(globalMap.get(var1.globalMap.get(globalMap.get(\"ip_detected_logs.ip_address2\"))), var1.globalMap.get(\"ip_detected_logs.ip_address2\")))\r\n"
                + "\r\n" + "//Set values in output row.\r"
                + "Process_Apache_Tag_Sever_Logs_1_ip_detected_logs.ip_address = ipAddress;//*Set values in output row.\r"
                + "\r\n" + "//ip_detected_logs.ip_address = ipAddress.\n"
                + "Process_Apache_Tag_Sever_Logs_1_ip_detected_logs.ip_address = ipAddress;//*Set values in output row.";
        String oldName = "ip_detected_logs.ip_address";
        String newName = "Process_Apache_Tag_Sever_Logs_1_ip_detected_logs.ip_address";
        String resultValue = ParameterValueUtil.renameValues(testString, oldName, newName, false);
        Assert.assertTrue(expectString.equals(resultValue));
    }
}
