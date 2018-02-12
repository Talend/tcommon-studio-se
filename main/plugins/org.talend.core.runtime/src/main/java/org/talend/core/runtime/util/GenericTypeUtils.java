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
package org.talend.core.runtime.util;

import java.util.List;

import org.apache.avro.Schema;
import org.apache.commons.lang3.reflect.TypeLiteral;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.talend.daikon.properties.property.EnumProperty;
import org.talend.daikon.properties.property.Property;


/**
 * created by nrousseau on May 20, 2016
 * Detailled comment
 *
 */
public class GenericTypeUtils {
    
    private static final TypeLiteral<List<String>> LIST_STRING_TYPE = new TypeLiteral<List<String>>() {// empty
    };

    private static final TypeLiteral<List<Boolean>> LIST_BOOLEAN_TYPE = new TypeLiteral<List<Boolean>>() {// empty
    };

    public static boolean isStringType(String type) {
        return TypeUtils.toString(String.class).equals(type) || TypeUtils.toString(LIST_STRING_TYPE.getType()).equals(type);
    }

    public static boolean isStringType(Property<?> property) {
        return isStringType(property.getType());
    }

    public static boolean isBooleanType(String type) {
        return TypeUtils.toString(Boolean.class).equals(type) || TypeUtils.toString(LIST_BOOLEAN_TYPE.getType()).equals(type);
    }

    public static boolean isBooleanType(Property<?> property) {
        return isBooleanType(property.getType());
    }

    public static boolean isEnumType(Property<?> property) {
        if (property instanceof EnumProperty) {
            return true;
        }
        if (property.getPossibleValues() != null && !property.getPossibleValues().isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isSchemaType(Property<?> property) {
        return TypeUtils.toString(Schema.class).equals(property.getType());
    }

    public static boolean isIntegerType(Property<?> property) {
        return TypeUtils.toString(Integer.class).equals(property.getType());
    }
    
    public static boolean isListStringType(Property<?> property) {
        return TypeUtils.toString(LIST_STRING_TYPE.getType()).equals(property.getType());
    }
    
    public static boolean isListType(Property<?> property) {
        return property.getType().startsWith("java.util.List");
    }

    public static boolean isObjectType(Property<?> property) {
        return TypeUtils.toString(Object.class).equals(property.getType());
    }

    public static boolean isLongType(Property<?> property) {
        return TypeUtils.toString(Long.class).equals(property.getType());
    }

    public static boolean isFloatType(Property<?> property) {
        return TypeUtils.toString(Float.class).equals(property.getType());
    }

    public static boolean isDoubleType(Property<?> property) {
        return TypeUtils.toString(Double.class).equals(property.getType());
    }

}
