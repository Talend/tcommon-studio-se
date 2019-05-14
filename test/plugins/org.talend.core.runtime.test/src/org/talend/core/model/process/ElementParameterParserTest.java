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
        assertEquals("\"n6BjxAHdNWGl2Jc0gLSS0A==\"", ElementParameterParser.getEncryptedValue(node, paramName));
        // "a\"b"
        when(parameter.getValue()).thenReturn("\"a\\\"b\"");
        assertEquals("\"QKgfO0uOwvcPufyMDlbn9Q==\"", ElementParameterParser.getEncryptedValue(node, paramName));
        // "a\\b"
        when(parameter.getValue()).thenReturn("\"a\\\\b\"");
        assertEquals("\"WhsqmBji8VPVQoOBwPedpQ==\"", ElementParameterParser.getEncryptedValue(node, paramName));
        // "a\\\\b"
        when(parameter.getValue()).thenReturn("\"a\\\\\\\\b\"");
        assertEquals("\"R3oj7HgFHM6LYZI9Lp17VA==\"", ElementParameterParser.getEncryptedValue(node, paramName));
        // "test"+context.mypassword + "a"
        when(parameter.getValue()).thenReturn("\"test\"+context.mypassword + \"a\"");
        assertEquals("\"test\"+context.mypassword + \"a\"", ElementParameterParser.getEncryptedValue(node, paramName));
        // "a" + "b"
        when(parameter.getValue()).thenReturn("\"a\" + \"b\"");
        assertEquals("\"a\" + \"b\"", ElementParameterParser.getEncryptedValue(node, paramName));
        // \\123456/
        when(parameter.getValue()).thenReturn("\"\\\\123456/\"");
        assertEquals("\"FlFFR5wmamqG3zkNPbbopw==\"", ElementParameterParser.getEncryptedValue(node, paramName));
        // \123456/
        when(parameter.getValue()).thenReturn("\"\\123456/\"");
        assertEquals("\"tEqqmqoHd22LTnvd5/jCDA==\"", ElementParameterParser.getEncryptedValue(node, paramName));
    }
}
