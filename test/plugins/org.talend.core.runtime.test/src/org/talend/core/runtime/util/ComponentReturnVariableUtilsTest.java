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
package org.talend.core.runtime.util;

import static org.junit.Assert.*;

import org.junit.Test;
import org.talend.components.api.component.ComponentDefinition;
import org.talend.core.runtime.util.ComponentReturnVariableUtils;


/**
 * created by nrousseau on Jun 21, 2016
 * Detailled comment
 *
 */
public class ComponentReturnVariableUtilsTest {

    @Test
    public void testGetStudioNameFromVariable() {
        assertEquals("ERROR_MESSAGE", ComponentReturnVariableUtils.getStudioNameFromVariable(ComponentDefinition.RETURN_ERROR_MESSAGE));
        assertEquals("NB_REJECT", ComponentReturnVariableUtils.getStudioNameFromVariable(ComponentDefinition.RETURN_REJECT_RECORD_COUNT));
        assertEquals("NB_SUCCESS", ComponentReturnVariableUtils.getStudioNameFromVariable(ComponentDefinition.RETURN_SUCCESS_RECORD_COUNT));
        assertEquals("NB_LINE", ComponentReturnVariableUtils.getStudioNameFromVariable(ComponentDefinition.RETURN_TOTAL_RECORD_COUNT));
        assertEquals("MY_VARIABLE_TEST", ComponentReturnVariableUtils.getStudioNameFromVariable("myVariableTest"));
        assertEquals("MY_VARIABLE_TEST", ComponentReturnVariableUtils.getStudioNameFromVariable("MyVariableTest"));
        assertEquals("M_Y_VARIABLE_TEST", ComponentReturnVariableUtils.getStudioNameFromVariable("MYVariableTest"));
    }

    
    @Test
    public void testGetTranslationForVariable() {
        assertEquals("Error Message", ComponentReturnVariableUtils.getTranslationForVariable(ComponentDefinition.RETURN_ERROR_MESSAGE,"aaa"));
        assertEquals("Number of reject", ComponentReturnVariableUtils.getTranslationForVariable(ComponentDefinition.RETURN_REJECT_RECORD_COUNT,"aaa"));
        assertEquals("Number of success", ComponentReturnVariableUtils.getTranslationForVariable(ComponentDefinition.RETURN_SUCCESS_RECORD_COUNT,"aaa"));
        assertEquals("Number of line", ComponentReturnVariableUtils.getTranslationForVariable(ComponentDefinition.RETURN_TOTAL_RECORD_COUNT,"aaa"));
        assertEquals("test1", ComponentReturnVariableUtils.getTranslationForVariable("myVariableTest","test1"));
        assertEquals("test2", ComponentReturnVariableUtils.getTranslationForVariable("ERROR_MESSAGE","test2"));
    }
}
