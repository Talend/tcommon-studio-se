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
package org.talend.core.runtime.evaluator;

import static org.junit.Assert.*;
import static org.talend.daikon.properties.property.PropertyFactory.*;

import java.util.Collections;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.talend.components.api.component.Connector;
import org.talend.components.api.component.PropertyPathConnector;
import org.talend.components.common.FixedConnectorsComponentProperties;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.property.Property;

/**
 * created by ycbai on 2016年9月19日 Detailled comment
 *
 */
public class AbstractPropertyValueEvaluatorTest {

    private AbstractPropertyValueEvaluator evalutor;

    @Before
    public void before() {
        evalutor = new AbstractPropertyValueEvaluator() {

            @Override
            public <T> T evaluate(Property<T> arg0, Object arg1) {
                return null;
            }
        };
    }

    @Test
    public void testGetTypedValue() {
        TestProperties props = (TestProperties) new TestProperties("test").init(); //$NON-NLS-1$

        // Test String type
        Object value = evalutor.getTypedValue(props.stringProp, "str"); //$NON-NLS-1$
        assertEquals("str", value); //$NON-NLS-1$

        // Test Boolean type
        value = evalutor.getTypedValue(props.booleanProp, "true"); //$NON-NLS-1$
        assertEquals(true, value);

        // Test Integer type
        value = evalutor.getTypedValue(props.integerProp, "1"); //$NON-NLS-1$
        assertEquals(1, value);
        value = evalutor.getTypedValue(props.integerProp, "str"); //$NON-NLS-1$
        assertEquals(0, value);
        value = evalutor.getTypedValue(props.integerProp, ""); //$NON-NLS-1$
        assertEquals(null, value);

        // Test Enum type
        value = evalutor.getTypedValue(props.enumProp, "str"); //$NON-NLS-1$
        assertEquals(EnumType.E1, value);
        value = evalutor.getTypedValue(props.enumProp, "E2"); //$NON-NLS-1$
        assertEquals(EnumType.E2, value);
    }

    class TestProperties extends FixedConnectorsComponentProperties {

        public Property<String> stringProp = newProperty("stringProp"); //$NON-NLS-1$

        public Property<Boolean> booleanProp = newBoolean("booleanProp"); //$NON-NLS-1$

        public Property<Integer> integerProp = newInteger("integerProp"); //$NON-NLS-1$

        public Property<EnumType> enumProp = newEnum("enumProp", EnumType.class); //$NON-NLS-1$

        protected transient PropertyPathConnector MAIN_CONNECTOR = new PropertyPathConnector(Connector.MAIN_NAME, "schema"); //$NON-NLS-1$

        public TestProperties(String name) {
            super(name);
        }

        @Override
        public void setupLayout() {
            super.setupLayout();
            Form form = Form.create(this, Form.MAIN);
            form.addRow(stringProp);
            form.addRow(booleanProp);
            form.addRow(integerProp);
            form.addRow(enumProp);
        }

        @Override
        protected Set<PropertyPathConnector> getAllSchemaPropertiesConnectors(boolean isOutputConnection) {
            if (isOutputConnection) {
                return Collections.singleton(MAIN_CONNECTOR);
            } else {
                return Collections.EMPTY_SET;
            }
        }

    }

    enum EnumType {
        E1,
        E2
    }

}
