// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
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
import org.talend.utils.security.StudioEncryption;

/**
 * created by wchen on 2014-4-16 Detailled comment
 *
 */
public class ElementParameterParserTest {

    private static StudioEncryption se = StudioEncryption.getStudioEncryption(StudioEncryption.KEY_ROUTINE);

    private static String decryptPassword(String input) {
        input = input.replace("\"", "");
        return se.decrypt(input);
    }
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
    public void testGetEncryptedValue() throws Exception {
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
        assertEquals("\"ab\"",
                decryptPassword(ElementParameterParser.getEncryptedValue(node, paramName)));
        // "a\"b"
        when(parameter.getValue()).thenReturn("\"a\\\"b\"");
        assertEquals("\"a\\\"b\"",
                decryptPassword(ElementParameterParser.getEncryptedValue(node, paramName)));
        // "a\\b"
        when(parameter.getValue()).thenReturn("\"a\\\\b\"");
        assertEquals("\"a\\\\b\"",
                decryptPassword(ElementParameterParser.getEncryptedValue(node, paramName)));
        // "a\\\\b"
        when(parameter.getValue()).thenReturn("\"a\\\\\\\\b\"");
        assertEquals("\"a\\\\\\\\b\"",
                decryptPassword(ElementParameterParser.getEncryptedValue(node, paramName)));
        // "test"+context.mypassword + "a"
        when(parameter.getValue()).thenReturn("\"test\"+context.mypassword + \"a\"");
        assertEquals("\"test\"+context.mypassword + \"a\"",
                decryptPassword(ElementParameterParser.getEncryptedValue(node, paramName)));
        // "a" + "b"
        when(parameter.getValue()).thenReturn("\"a\" + \"b\"");
        assertEquals("\"a\" + \"b\"", decryptPassword(ElementParameterParser.getEncryptedValue(node, paramName)));
        // \\123456/
        when(parameter.getValue()).thenReturn("\"\\\\123456/\"");
        assertEquals("\"\\\\123456/\"",
                decryptPassword(ElementParameterParser.getEncryptedValue(node, paramName)));
        // \123456/
        when(parameter.getValue()).thenReturn("\"\\123456/\"");
        assertEquals("\"\\123456/\"",
                decryptPassword(ElementParameterParser.getEncryptedValue(node, paramName)));
    }
}
