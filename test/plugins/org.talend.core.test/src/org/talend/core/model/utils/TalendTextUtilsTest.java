package org.talend.core.model.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TalendTextUtilsTest {

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
    public void testcheckAndAddSQLQuote() {
        String query = "SELECT column_name FROM information_schema.tables WHERE table_name = 'abc';";
        String quoteStyle = "\"";
        String result = TalendTextUtils.checkAndAddSQLQuote(query, quoteStyle, true);
        assertEquals(result, "\"SELECT column_name FROM information_schema.tables WHERE table_name = 'abc';\"");
    }


}
