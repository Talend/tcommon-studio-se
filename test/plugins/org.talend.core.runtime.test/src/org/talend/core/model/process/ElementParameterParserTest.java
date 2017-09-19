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
package org.talend.core.model.process;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * created by wchen on 2014-4-16 Detailled comment
 * 
 */
public class ElementParameterParserTest {

    @Test
    public void testCanEncrypt() {
        String paramName = "__PASSWORD__";
        // mock parameter
        IElementParameter parameter = mock(IElementParameter.class);
        when(parameter.getVariableName()).thenReturn(paramName);
        when(parameter.getName()).thenReturn("PASSWORD");

        // mock the node
        IElement node = mock(IElement.class);
        List elementParametersWithChildrens = new ArrayList();
        elementParametersWithChildrens.add(parameter);
        when(node.getElementParametersWithChildrens()).thenReturn(elementParametersWithChildrens);

        // "ab"
        when(parameter.getValue()).thenReturn("\"ab\"");
        assertTrue(ElementParameterParser.canEncrypt(node, paramName));
        // "a\"b"
        when(parameter.getValue()).thenReturn("\"a\\\"b\"");
        assertTrue(ElementParameterParser.canEncrypt(node, paramName));
        // "a\\b"
        when(parameter.getValue()).thenReturn("\"a\\\\b\"");
        assertTrue(ElementParameterParser.canEncrypt(node, paramName));

        // "a\\\\b"
        when(parameter.getValue()).thenReturn("\"a\\\\\\\\b\"");
        assertTrue(ElementParameterParser.canEncrypt(node, paramName));

        // "test"+context.mypassword + "a"
        when(parameter.getValue()).thenReturn("\"test\"+context.mypassword + \"a\"");
        assertFalse(ElementParameterParser.canEncrypt(node, paramName));
        // "a" + "b"
        when(parameter.getValue()).thenReturn("\"a\" + \"b\"");
        assertFalse(ElementParameterParser.canEncrypt(node, paramName));
    }

    @Test
    public void testGetEncryptedValue() {
        String paramName = "__PASSWORD__";
        // mock parameter
        IElementParameter parameter = mock(IElementParameter.class);
        when(parameter.getVariableName()).thenReturn(paramName);
        when(parameter.getName()).thenReturn("PASSWORD");

        // mock the node
        IElement node = mock(IElement.class);
        List elementParametersWithChildrens = new ArrayList();
        elementParametersWithChildrens.add(parameter);
        when(node.getElementParametersWithChildrens()).thenReturn(elementParametersWithChildrens);

        // "ab"
        when(parameter.getValue()).thenReturn("\"ab\"");
        assertEquals("\"b2ffaf80bd12fbe9\"", ElementParameterParser.getEncryptedValue(node, paramName));
        // "a\"b"
        when(parameter.getValue()).thenReturn("\"a\\\"b\"");
        assertEquals("\"81f12ffeb5daed01\"", ElementParameterParser.getEncryptedValue(node, paramName));
        // "a\\b"
        when(parameter.getValue()).thenReturn("\"a\\\\b\"");
        assertEquals("\"a745923703c42ed8\"", ElementParameterParser.getEncryptedValue(node, paramName));
        // "a\\\\b"
        when(parameter.getValue()).thenReturn("\"a\\\\\\\\b\"");
        assertEquals("\"d0e9b75c43bbc953\"", ElementParameterParser.getEncryptedValue(node, paramName));
        // "test"+context.mypassword + "a"
        when(parameter.getValue()).thenReturn("\"test\"+context.mypassword + \"a\"");
        assertEquals("\"test\"+context.mypassword + \"a\"", ElementParameterParser.getEncryptedValue(node, paramName));
        // "a" + "b"
        when(parameter.getValue()).thenReturn("\"a\" + \"b\"");
        assertEquals("\"a\" + \"b\"", ElementParameterParser.getEncryptedValue(node, paramName));
        // \\123456/
        when(parameter.getValue()).thenReturn("\"\\\\123456/\"");
        assertEquals("\"4bd6e9847da74e5d8c3ef1db4cd6aed6\"", ElementParameterParser.getEncryptedValue(node, paramName));
        // \123456/
        when(parameter.getValue()).thenReturn("\"\\123456/\"");
        assertEquals("\"71ba460575307d5af4f7aba1746784ea\"", ElementParameterParser.getEncryptedValue(node, paramName));
    }
}
