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
package org.talend.commons.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public class BeanUtils {

    private static Logger log = Logger.getLogger(BeanUtils.class);

    private BeanUtils() {
    }

    public static Field getDeclaredField(Object object, String propertyName) throws NoSuchFieldException {
        return getDeclaredField(object.getClass(), propertyName);
    }

    public static Field getDeclaredField(Class clazz, String propertyName) throws NoSuchFieldException {
        for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(propertyName);
            } catch (NoSuchFieldException e) {
                log.error(e);
            }
        }
        throw new NoSuchFieldException("No such field: " + clazz.getName() + '.' + propertyName);
    }

    public static List<Field> getDeclaredFields(Class clazz) {
        List<Field> list = new ArrayList();
        for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            Field[] ff = superClass.getDeclaredFields();
            if (ff != null && ff.length > 0) {
                for (Field f : ff) {
                    list.add(f);
                }
            }
        }
        return list;
    }

    public static Object forceGetProperty(Object object, String propertyName) throws NoSuchFieldException {
        Field field = getDeclaredField(object, propertyName);

        boolean accessible = field.isAccessible();
        field.setAccessible(true);

        Object result = null;
        try {
            result = field.get(object);
        } catch (IllegalAccessException e) {
            log.info("error wont' happen");
        }
        field.setAccessible(accessible);
        return result;
    }

    public static Object getGetProperty(Object object, String propertyName) throws NoSuchFieldException, SecurityException,
            NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (object instanceof Map) {
            Map map = (Map) object;
            return map.get(propertyName);
        } else {
            String methodName = BeanUtils.getGetterName(String.class, propertyName);
            Method method = object.getClass().getMethod(methodName, new Class[0]);
            Object result = method.invoke(object, new Object[0]);
            return result;
        }
    }

    public static Object nestecdGetProperty(Object object, String propertyName) throws SecurityException,
            IllegalArgumentException, NoSuchFieldException, NoSuchMethodException, IllegalAccessException,
            InvocationTargetException {
        if (StringUtils.contains(propertyName, ".")) {
            String[] fileds = StringUtils.split(propertyName, ".");
            for (String f : fileds) {
                try {
                    object = BeanUtils.getGetProperty(object, f);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (object == null) {
                    break;
                }
            }
            return object;
        } else {
            return getGetProperty(object, propertyName);
        }
    }

    public static void forceSetProperty(Object object, String propertyName, Object newValue) throws NoSuchFieldException {
        Field field = getDeclaredField(object, propertyName);
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            field.set(object, newValue);
        } catch (IllegalAccessException e) {
            log.info("Error won't happen");
        }
        field.setAccessible(accessible);
    }

    public static List<Field> getFieldsByType(Object object, Class type) {
        List<Field> list = new ArrayList<Field>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().isAssignableFrom(type)) {
                list.add(field);
            }
        }
        return list;
    }

    public static Class getPropertyType(Class type, String name) throws NoSuchFieldException {
        return getDeclaredField(type, name).getType();
    }

    public static String getGetterName(Class type, String fieldName) {
        if (type.getName().equals("boolean")) {
            return "is" + StringUtils.capitalize(fieldName);
        } else {
            return "get" + StringUtils.capitalize(fieldName);
        }
    }

    public static Method getGetterMethod(Class type, String fieldName) {
        try {
            return type.getMethod(getGetterName(type, fieldName));
        } catch (NoSuchMethodException e) {
            log.error(e);
        }
        return null;
    }

    public static void copyObject(Object from, Object to) {
        List<Field> fields = BeanUtils.getDeclaredFields(from.getClass());
        for (Field field : fields) {
            try {
                Object value = BeanUtils.getGetProperty(from, field.getName());
                if (value != null) {
                    org.apache.commons.beanutils.BeanUtils.setProperty(to, field.getName(), value);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e);
            }
        }
    }
}
