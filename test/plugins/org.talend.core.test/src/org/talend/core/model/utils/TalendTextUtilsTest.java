package org.talend.core.model.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TalendTextUtilsTest {

    @Test
    public void testAddStrInQuery() {
        String pass = "test ' \\b'aaa";
        String result = TalendTextUtils.addStrInQuery(pass);
        assertEquals(result, "\"test ' \\\\b'aaa\"");

        pass = "test '\\t'a  bb '\\t'aa";
        result = TalendTextUtils.addStrInQuery(pass);
        assertEquals(result, "\"test '\\\\t'a  bb '\\\\t'aa\"");

        pass = "test '\\f'abada";
        result = TalendTextUtils.addStrInQuery(pass);
        assertEquals(result, "\"test '\\\\f'abada\"");

        pass = "test '\\r'aa cca '\\r'";
        result = TalendTextUtils.addStrInQuery(pass);
        assertEquals(result, "\"test '\\\\r'aa cca '\\\\r'\"");

        pass = "test '\\n'aa ba";
        result = TalendTextUtils.addStrInQuery(pass);
        assertEquals(result, "\"test '\\\\n'aa ba\"");

        pass = "test '\"'a aa";
        result = TalendTextUtils.addStrInQuery(pass);
        assertEquals(result, "\"test '\\\"'a aa\"");

        pass = "test '\\'a aa";
        result = TalendTextUtils.addStrInQuery(pass);
        assertEquals(result, "\"test '\\\\'a aa\"");
    }

    @Test
    public void testcheckAndAddSQLQuote() {
        String query = "SELECT column_name FROM information_schema.tables WHERE table_name = 'abc';";
        String quoteStyle = "\"";
        String result = TalendTextUtils.checkAndAddSQLQuote(query, quoteStyle, true);
        assertEquals(result, "\"SELECT column_name FROM information_schema.tables WHERE table_name = 'abc';\"");
    }

    @Test
    public void testConvertSlashForSpecialChar() {
        String pass = "test ' \\b'aaa";
        String result = TalendTextUtils.convertSlashForSpecialChar(pass);
        assertEquals(result, "test ' \\\\b'aaa");

        pass = "test '\\t'a  bb '\\t'aa";
        result = TalendTextUtils.convertSlashForSpecialChar(pass);
        assertEquals(result, "test '\\\\t'a  bb '\\\\t'aa");

        pass = "test '\\f'abada";
        result = TalendTextUtils.convertSlashForSpecialChar(pass);
        assertEquals(result, "test '\\\\f'abada");

        pass = "test '\\r'aa cca '\\r'";
        result = TalendTextUtils.convertSlashForSpecialChar(pass);
        assertEquals(result, "test '\\\\r'aa cca '\\\\r'");

        pass = "test '\\n'aa ba";
        result = TalendTextUtils.convertSlashForSpecialChar(pass);
        assertEquals(result, "test '\\\\n'aa ba");

        pass = "test '\"'a aa";
        result = TalendTextUtils.convertSlashForSpecialChar(pass);
        assertEquals(result, "test '\\\"'a aa");

        pass = "test '\\'a aa";
        result = TalendTextUtils.convertSlashForSpecialChar(pass);
        assertEquals(result, "test '\\\\'a aa");
    }

    @Test
    public void testHidePassword(){
        String pass = "(( String) globalMap(\"myPasswd\"))";
        String result = TalendTextUtils.hidePassword(pass);
        assertEquals(result,pass);

        String temp = "ccc";
        pass = "\"aaa\""+temp+"\"bbb\"";
        result = TalendTextUtils.hidePassword(pass);
        assertEquals(result,pass);

        pass = " value ";
        result = TalendTextUtils.hidePassword(pass);
        assertEquals(result, "********");

        pass = " va  lue ";
        result = TalendTextUtils.hidePassword(pass);
        assertEquals(result, "********");

        pass = null;
        result = TalendTextUtils.hidePassword(pass);
        assertEquals(result, "********");

        pass = " ";
        result = TalendTextUtils.hidePassword(pass);
        assertEquals(result, "********");

        pass = "\"\"";
        result = TalendTextUtils.hidePassword(pass);
        assertEquals(result, "********");

        pass = "\"  \"";
        result = TalendTextUtils.hidePassword(pass);
        assertEquals(result, "********");

        pass = "context.value";
        result = TalendTextUtils.hidePassword(pass);
        assertEquals(result, pass);

        pass = "context. value";
        result = TalendTextUtils.hidePassword(pass);
        assertEquals(result, "********");

        pass = "con text. value";
        result = TalendTextUtils.hidePassword(pass);
        assertEquals(result, "********");

        pass = "value*value";
        result = TalendTextUtils.hidePassword(pass);
        assertEquals(result, "********");

        pass = "value\"value";
        result = TalendTextUtils.hidePassword(pass);
        assertEquals(result, "********");

        pass = "xyz";
        result = TalendTextUtils.hidePassword(pass);
        assertEquals(result, "********");

        pass = "value**value";
        result = TalendTextUtils.hidePassword(pass);
        assertEquals(result, "********");

        pass = "value**va*lu**e";
        result = TalendTextUtils.hidePassword(pass);
        assertEquals(result, "********");

        pass = "value\"valu\"e";
        result = TalendTextUtils.hidePassword(pass);
        assertEquals(result, pass);
    }

    @Test
    public void testRemoveSlashForSpecialChar() {
        String pass = "INSTR(TRIM(b),'\\\\b',1,2)";
        String result = TalendTextUtils.removeSlashForSpecialChar(pass);
        assertEquals(result, "INSTR(TRIM(b),'\\b',1,2)");

        pass = "INSTR(TRIM(b),'\\\\t' ,1, '\\\\t'2)";
        result = TalendTextUtils.removeSlashForSpecialChar(pass);
        assertEquals(result, "INSTR(TRIM(b),'\\t' ,1, '\\t'2)");

        pass = "INSTR(TRIM(b),'\\\\f',1,2)";
        result = TalendTextUtils.removeSlashForSpecialChar(pass);
        assertEquals(result, "INSTR(TRIM(b),'\\f',1,2)");

        pass = "INSTR(TRIM(b),'\\\\n',1,2)";
        result = TalendTextUtils.removeSlashForSpecialChar(pass);
        assertEquals(result, "INSTR(TRIM(b),'\\n',1,2)");

        pass = "INSTR(TRIM(b),'\\\\r',1,2)";
        result = TalendTextUtils.removeSlashForSpecialChar(pass);
        assertEquals(result, "INSTR(TRIM(b),'\\r',1,2)");

        pass = "INSTR(TRIM(b),'\\\"',1,2)";
        result = TalendTextUtils.removeSlashForSpecialChar(pass);
        assertEquals(result, "INSTR(TRIM(b),'\"',1,2)");

        pass = "INSTR(TRIM(b),'\\\\',1,2)";
        result = TalendTextUtils.removeSlashForSpecialChar(pass);
        assertEquals(result, "INSTR(TRIM(b),'\\',1,2)");
    }

    @Test
    public void testRemoveStrInQuery() {
        String pass = "\"INSTR(TRIM(b),'\\\\b',1,2)\"";
        String result = TalendTextUtils.removeStrInQuery(pass);
        assertEquals(result, "INSTR(TRIM(b),'\\b',1,2)");

        pass = "\"INSTR(TRIM(b),'\\\\t' ,1, '\\\\t'2)\"";
        result = TalendTextUtils.removeStrInQuery(pass);
        assertEquals(result, "INSTR(TRIM(b),'\\t' ,1, '\\t'2)");

        pass = "\"INSTR(TRIM(b),'\\\\f',1,2)\"";
        result = TalendTextUtils.removeStrInQuery(pass);
        assertEquals(result, "INSTR(TRIM(b),'\\f',1,2)");

        pass = "\"INSTR(TRIM(b),'\\\\n',1,2)\"";
        result = TalendTextUtils.removeStrInQuery(pass);
        assertEquals(result, "INSTR(TRIM(b),'\\n',1,2)");

        pass = "\"INSTR(TRIM(b),'\\\\r',1,2)\"";
        result = TalendTextUtils.removeStrInQuery(pass);
        assertEquals(result, "INSTR(TRIM(b),'\\r',1,2)");

        pass = "\"INSTR(TRIM(b),'\\\"',1,2)\"";
        result = TalendTextUtils.removeStrInQuery(pass);
        assertEquals(result, "INSTR(TRIM(b),'\"',1,2)");

        pass = "\"INSTR(TRIM(b),'\\\\',1,2)\"";
        result = TalendTextUtils.removeStrInQuery(pass);
        assertEquals(result, "INSTR(TRIM(b),'\\',1,2)");
    }
}
