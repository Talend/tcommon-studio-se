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

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;

/**
 * created by ggu on Aug 20, 2014 Detailled comment
 *
 */
@SuppressWarnings("nls")
public class ContextParameterUtilsTest {

    @Test
    public void testIsContainContextParam4Null() {
        Assert.assertFalse(ContextParameterUtils.isContainContextParam(null));
    }

    @Test
    public void testIsContainContextParam4Normal() {
        Assert.assertFalse(ContextParameterUtils.isContainContextParam(""));
        Assert.assertFalse(ContextParameterUtils.isContainContextParam("123"));
        Assert.assertFalse(ContextParameterUtils.isContainContextParam("ABC"));
        Assert.assertFalse(ContextParameterUtils.isContainContextParam("ABC 123 ()"));
    }

    @Test
    public void testIsContainContextParam4Context() {
        Assert.assertTrue(ContextParameterUtils.isContainContextParam("context.var1"));
        Assert.assertTrue(ContextParameterUtils.isContainContextParam("abc context.var1"));
        Assert.assertFalse(ContextParameterUtils.isContainContextParam("abccontext.var1"));
        Assert.assertFalse(ContextParameterUtils.isContainContextParam("abc_context.var1"));
        Assert.assertTrue(ContextParameterUtils.isContainContextParam("abc-context.var1"));
        Assert.assertTrue(ContextParameterUtils.isContainContextParam("abc\ncontext.var1"));
        // tab
        Assert.assertTrue(ContextParameterUtils.isContainContextParam("abc  context.var1"));

        Assert.assertTrue(ContextParameterUtils.isContainContextParam("abc\rcontext.var1"));
        Assert.assertTrue(ContextParameterUtils.isContainContextParam("abc\tcontext.var1"));
        Assert.assertTrue(ContextParameterUtils.isContainContextParam("abc\0context.var1"));

        Assert.assertTrue(ContextParameterUtils.isContainContextParam("context.var1+\"abc\""));
        Assert.assertTrue(ContextParameterUtils.isContainContextParam("context.var1+\"\nabc\""));
        Assert.assertTrue(ContextParameterUtils.isContainContextParam("context.var1+\"abc 123\""));
        Assert.assertTrue(ContextParameterUtils.isContainContextParam("\"abc\"+context.var1"));
        Assert.assertTrue(ContextParameterUtils.isContainContextParam("\"abc\n\"+context.var1"));
        Assert.assertTrue(ContextParameterUtils.isContainContextParam("\"abc\"+\"123\"+context.var1"));
        Assert.assertTrue(ContextParameterUtils.isContainContextParam("\"abc\"+\"123\n\"+context.var1"));
        Assert.assertTrue(ContextParameterUtils.isContainContextParam("\"abc\"+\"123\n\t\0\"+context.var1"));
        Assert.assertTrue(ContextParameterUtils.isContainContextParam("context.__"));

        // ????
        Assert.assertFalse(ContextParameterUtils.isContainContextParam("context."));
        Assert.assertFalse(ContextParameterUtils.isContainContextParam("context.123"));
        Assert.assertFalse(ContextParameterUtils.isContainContextParam("context.$%%"));
        Assert.assertFalse(ContextParameterUtils.isContainContextParam("context.\t"));
        Assert.assertFalse(ContextParameterUtils.isContainContextParam("context.++"));
    }

    @Test
    public void testIsContainContextParam4ContextInString() {
        Assert.assertFalse(ContextParameterUtils.isContainContextParam("\"context.var1\""));
        Assert.assertFalse(ContextParameterUtils.isContainContextParam("\"context.var1 abc\""));
        Assert.assertFalse(ContextParameterUtils.isContainContextParam("\"abc 123context.var1\""));
        Assert.assertFalse(ContextParameterUtils.isContainContextParam("\"abc\ncontext.var1\""));
    }

    @Test
    public void testGetVariableFromCode4Null() {
        Assert.assertNull(ContextParameterUtils.getVariableFromCode(null));
    }

    @Test
    public void testGetVariableFromCode4String() {
        Assert.assertNull(ContextParameterUtils.getVariableFromCode(""));
        Assert.assertNull(ContextParameterUtils.getVariableFromCode("abc"));
        Assert.assertNull(ContextParameterUtils.getVariableFromCode("123"));
        Assert.assertNull(ContextParameterUtils.getVariableFromCode("context."));

        Assert.assertNull(ContextParameterUtils.getVariableFromCode("context.123"));
        Assert.assertNull(ContextParameterUtils.getVariableFromCode("context.+++"));
        Assert.assertEquals("___",ContextParameterUtils.getVariableFromCode("context.___"));
        Assert.assertNull(ContextParameterUtils.getVariableFromCode("context.\n"));
        Assert.assertNull(ContextParameterUtils.getVariableFromCode("context.\0"));
    }

    @Test
    public void testGetVariableFromCode4Context() {
        String var = ContextParameterUtils.getVariableFromCode("context.abc");
        Assert.assertEquals("abc", var);

        var = ContextParameterUtils.getVariableFromCode("context.abc_123");
        Assert.assertEquals("abc_123", var);

        var = ContextParameterUtils.getVariableFromCode("context.abc 123");
        Assert.assertEquals("abc", var);

        var = ContextParameterUtils.getVariableFromCode("context.abc\t123");
        Assert.assertEquals("abc", var);

        var = ContextParameterUtils.getVariableFromCode("context.abc-123");
        Assert.assertEquals("abc", var);

    }

    @Test
    public void testIsContextParamOfContextType() {
        ContextType contextType = createContextType("TEST");
        contextType.getContextParameter().add(createContextParameterType("conn_Login", "talend"));
        contextType.getContextParameter().add(createContextParameterType("conn_Passwd", "123"));
        assertTrue(ContextParameterUtils.isContextParamOfContextType(contextType, "context.conn_Login"));
        assertTrue(ContextParameterUtils.isContextParamOfContextType(contextType, "context.conn_Passwd"));
        assertFalse(ContextParameterUtils.isContextParamOfContextType(contextType, "context.conn_Name"));
    }

    private ContextType createContextType(String name) {
        ContextType contextType = TalendFileFactory.eINSTANCE.createContextType();
        contextType.setName(name);
        return contextType;
    }

    private ContextParameterType createContextParameterType(String name, String value) {
        ContextParameterType contextParameterType = TalendFileFactory.eINSTANCE.createContextParameterType();
        contextParameterType.setName(name);
        contextParameterType.setValue(value);
        return contextParameterType;
    }

    @Test
    public void testIsValidParameterName() {
        assertTrue(ContextParameterUtils.isValidParameterName("abc"));
        assertTrue(ContextParameterUtils.isValidParameterName("abc123"));
        assertTrue(ContextParameterUtils.isValidParameterName("abc_123"));
        assertTrue(ContextParameterUtils.isValidParameterName("abc_de_123"));
        assertTrue(ContextParameterUtils.isValidParameterName("_abc"));
        assertFalse(ContextParameterUtils.isValidParameterName("abc-de"));
        assertFalse(ContextParameterUtils.isValidParameterName("abc%de"));
        assertFalse(ContextParameterUtils.isValidParameterName("a*&^e"));
        assertFalse(ContextParameterUtils.isValidParameterName("123abc"));
    }

    @Test
    public void testGetValidParameterName() {
        assertNull(ContextParameterUtils.getValidParameterName(null));
        assertNull(ContextParameterUtils.getValidParameterName(""));
        assertEquals("abc_de", ContextParameterUtils.getValidParameterName("abc_de"));
        assertEquals("abc_de", ContextParameterUtils.getValidParameterName("abc-de"));
        assertEquals("_int", ContextParameterUtils.getValidParameterName("int"));
    }

}
