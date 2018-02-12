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
package org.talend.commons.utils.data.reflection;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public final class ReflectUtils {

    /**
     * Default Constructor. Must not be used.
     */
    private ReflectUtils() {
    }

    public static void setValue(Object object, String property, Object value) {
        try {
            PropertyUtils.setProperty(object, property, value);
        } catch (IllegalAccessException e) {
            throw new ReflectionPropertyException(object != null ? object.getClass() : null, property, false, e);
        } catch (InvocationTargetException e) {
            throw new ReflectionPropertyException(object != null ? object.getClass() : null, property, false, e);
        } catch (NoSuchMethodException e) {
            throw new ReflectionPropertyException(object != null ? object.getClass() : null, property, false, e);
        } catch (IllegalArgumentException e) {
            throw new ReflectionPropertyException(object != null ? object.getClass() : null, property, false, e);
        }
    }

    /**
     * DOC amaumont Comment method "getValue".
     * 
     * @param data
     * @param beanPropertyName
     * @return
     */
    public static Object getValue(Object object, String property) {
        try {
            return PropertyUtils.getProperty(object, property);
        } catch (IllegalAccessException e) {
            throw new ReflectionPropertyException(object != null ? object.getClass() : null, property, false, e);
        } catch (InvocationTargetException e) {
            throw new ReflectionPropertyException(object != null ? object.getClass() : null, property, false, e);
        } catch (NoSuchMethodException e) {
            throw new ReflectionPropertyException(object != null ? object.getClass() : null, property, false, e);
        }
    }
}
