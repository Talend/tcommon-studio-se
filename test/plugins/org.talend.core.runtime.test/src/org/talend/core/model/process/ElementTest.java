// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.talend.core.model.utils.TestElementParameter;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
@SuppressWarnings("nls")
public class ElementTest {

    class TestElement extends Element {

        @Override
        public boolean isReadOnly() {
            return false;
        }

        @Override
        public void setReadOnly(boolean readOnly) {
        }

        @Override
        public String getElementName() {
            return "";
        }

        @Override
        public Map<String, IElementParameter> getMapNameToParam() {
            return super.getMapNameToParam();
        }
    }

    @Test
    public void testSetElementParameters() {
        TestElement elem = new TestElement();

        IElementParameter param1 = new TestElementParameter();
        param1.setName("param1");
        param1.setValue("value1");
        elem.addElementParameter(param1);
        IElementParameter param2 = new TestElementParameter();
        param2.setName("param2");
        param2.setValue("value2");
        elem.addElementParameter(param2);
        assertEquals("value1", elem.getElementParameter("param1").getValue());
        assertEquals("value2", elem.getElementParameter("param2").getValue());

        @SuppressWarnings("unchecked")
        List<IElementParameter> oldParams = (List<IElementParameter>) elem.getElementParameters();
        List<IElementParameter> newParams = new ArrayList<>();
        IElementParameter param3 = new TestElementParameter();
        param3.setName("param2");
        param3.setValue("value2_new");
        newParams.add(param3);
        IElementParameter param4 = new TestElementParameter();
        param4.setName("param4");
        param4.setValue("value4_new");
        newParams.add(param4);
        elem.setElementParameters(newParams);
        assertNotEquals(oldParams, elem.getElementParameters());
        assertEquals(2, elem.getElementParameters().size());
        assertEquals(null, elem.getElementParameter("param1"));
        assertEquals("value2_new", elem.getElementParameter("param2").getValue());
        assertEquals("value4_new", elem.getElementParameter("param4").getValue());

        // test listener
        IElementParameter param5 = new TestElementParameter();
        param5.setName("param5");
        param5.setValue("value5");
        oldParams.add(param5);
        assertEquals(null, elem.getElementParameter("param5"));
        assertEquals(false, elem.getMapNameToParam().containsKey("param5"));
        assertTrue(elem.getMapNameToParam().containsKey("param2"));
        assertTrue(elem.getMapNameToParam().containsKey("param4"));
        elem.addElementParameter(param5);
        assertEquals("value5", elem.getElementParameter("param5").getValue());
        assertTrue(elem.getMapNameToParam().containsKey("param5"));
    }

}
