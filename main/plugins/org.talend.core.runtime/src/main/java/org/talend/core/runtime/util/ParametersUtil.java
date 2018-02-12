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

import java.util.Map;

/**
 * DOC ggu class global comment. Detailled comment
 */
public final class ParametersUtil {

    public static boolean hasBoolFlag(Map<String, Object> parameters, String flagName) {
        if (parameters == null || parameters.isEmpty() || flagName == null) {
            return false;
        }
        if (!parameters.containsKey(flagName)) {
            return false;
        }
        final Object flagValue = parameters.get(flagName);
        if (flagValue == null) {
            return false;
        }
        return Boolean.parseBoolean(flagValue.toString());
    }

    public static Object getObject(Map<String, Object> parameters, String keyName, Class valueClazz) {
        if (parameters == null || parameters.isEmpty() || keyName == null) {
            return null;
        }
        if (!parameters.containsKey(keyName)) {
            return null;
        }
        final Object value = parameters.get(keyName);
        if (value == null) {
            return null;
        }
        if (valueClazz == null) { // don't check
            return value;
        } else if (valueClazz.isInstance(value)) { // match the class
            return value;
        }
        return null;

    }
}
