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

import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.talend.core.runtime.util.GenericTypeUtils;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyValueEvaluator;
import org.talend.daikon.properties.property.StringProperty;

/**
 * created by ycbai on 2016年9月19日 Detailled comment
 *
 */
public abstract class AbstractPropertyValueEvaluator implements PropertyValueEvaluator {

    public Object getTypedValue(Property property, Object rawValue) {
        if (GenericTypeUtils.isSchemaType(property)) {
            return rawValue;
        }
        String stringValue = String.valueOf(rawValue);
        if (GenericTypeUtils.isBooleanType(property)) {
            return new Boolean(stringValue);
        }
        if (GenericTypeUtils.isIntegerType(property) && rawValue != null) {
            if (stringValue.isEmpty()) { // regard empty value as null
                return null;
            }
            try {
                return Integer.valueOf(stringValue);
            } catch (Exception e) {
                // value not existing anymore
                // return any value to let the component work without exception
                return 0;
            }
        } else if (GenericTypeUtils.isLongType(property) && rawValue != null) {
            if (stringValue.isEmpty()) { // regard empty value as null
                return null;
            }
            try {
                return Long.valueOf(stringValue);
            } catch (Exception e) {
                // value not existing anymore
                // return any value to let the component work without exception
                return 0;
            }
        } else if (GenericTypeUtils.isFloatType(property) && rawValue != null) {
            if (stringValue.isEmpty()) { // regard empty value as null
                return null;
            }
            try {
                return Float.valueOf(stringValue);
            } catch (Exception e) {
                // value not existing anymore
                // return any value to let the component work without exception
                return 0;
            }
        } else if (GenericTypeUtils.isDoubleType(property) && rawValue != null) {
            if (stringValue.isEmpty()) { // regard empty value as null
                return null;
            }
            try {
                return Double.valueOf(stringValue);
            } catch (Exception e) {
                // value not existing anymore
                // return any value to let the component work without exception
                return 0;
            }
        }
        
        if (GenericTypeUtils.isListStringType(property) && rawValue != null) {
            return rawValue;
        }
        
        if (GenericTypeUtils.isListType(property) && rawValue != null) {
            return rawValue;
        }
        
        if (property instanceof StringProperty) {
            if (property.getPossibleValues() != null && !property.getPossibleValues().isEmpty()) {
                return TalendQuoteUtils.removeQuotes(stringValue);
            }
        }

        if (GenericTypeUtils.isEnumType(property)) {
            List<?> possibleValues = property.getPossibleValues();
            if (possibleValues != null) {
                Object firstValue = null;
                if (!possibleValues.isEmpty()) {
                    firstValue = possibleValues.get(0);
                }
                String stringStoredValue = TalendQuoteUtils.removeQuotes(stringValue);
                for (Object possibleValue : possibleValues) {
                    if (possibleValue.toString().equals(stringStoredValue)) {
                        return possibleValue;
                    }
                }
                if (firstValue != null) {
                    return firstValue;
                }
            }
        }
        
        if (GenericTypeUtils.isStringType(property)) {
            return TalendQuoteUtils.removeQuotes(StringEscapeUtils.unescapeJava(stringValue));
        }
        return rawValue;
    }

}
