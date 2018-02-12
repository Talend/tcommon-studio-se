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
package org.talend.core.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

/**
 * DOC ycbai class global comment. Detailled comment
 * 
 * General reflection utils.
 * 
 */
public class ReflectionUtils {

    private static Logger log = Logger.getLogger(ReflectionUtils.class.getName());

    /**
     * DOC ycbai Comment method "getPublicField".
     * 
     * Returns the value of a public field.
     * 
     * @param owner
     * @param fieldName
     * @return
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws Exception
     */
    public static Object getPublicField(Object owner, String fieldName) throws SecurityException, NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException {
        Object fieldValue = null;
        Class ownerClass = owner.getClass();
        Field field = ownerClass.getField(fieldName);
        fieldValue = field.get(owner);

        return fieldValue;
    }

    /**
     * DOC ycbai Comment method "getPrivateField".
     * 
     * Returns the value of a private field.
     * 
     * @param owner
     * @param fieldName
     * @return
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static Object getPrivateField(Object owner, String fieldName) throws SecurityException, NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException {
        Object fieldValue = null;
        Class ownerClass = owner.getClass();
        Field f = ownerClass.getDeclaredField(fieldName);
        f.setAccessible(true);
        fieldValue = f.get(owner);

        return fieldValue;
    }

    /**
     * DOC ycbai Comment method "getStaticField".
     * 
     * Returns the value of a static field.
     * 
     * @param className
     * @param loader
     * @param fieldName
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static Object getStaticField(String className, ClassLoader loader, String fieldName) throws ClassNotFoundException,
            SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Object fieldValue = null;
        Class ownerClass = null;
        if (loader != null) {
            ownerClass = Class.forName(className, true, loader);
        } else {
            ownerClass = Class.forName(className);
        }
        Field field = ownerClass.getField(fieldName);
        fieldValue = field.get(ownerClass);

        return fieldValue;
    }

    /**
     * To set the field using the given <code>value</code>. Added by Marvin Wang on Oct 19, 2012.
     */
    public static void setStaticFieldValue(String className, ClassLoader loader, String fieldName, Object value) {
        try {
            Class ownerClass = null;
            if (loader != null) {
                ownerClass = Class.forName(className, true, loader);
            } else {
                ownerClass = Class.forName(className);
            }
            Field field = ownerClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(null, value);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public static Object getStaticField(String className, String fieldName) throws SecurityException, IllegalArgumentException,
            ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        return getStaticField(className, null, fieldName);
    }

    /**
     * DOC ycbai Comment method "invokeMethod".
     * 
     * Returns the value of a method.
     * 
     * @param owner
     * @param methodName
     * @param args
     * @param argTypes
     * @return
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static Object invokeMethod(Object owner, String methodName, Object[] args, Class... argTypes)
            throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        Object returnValue = null;
        Class ownerClass = owner.getClass();
        Class[] argsClass = new Class[args.length];
        if (argTypes.length > 0 && argTypes.length == args.length) {
            argsClass = argTypes;
        } else {
            for (int i = 0, j = args.length; i < j; i++) {
                argsClass[i] = args[i].getClass();
            }
        }
        Method method = ownerClass.getMethod(methodName, argsClass);
        method.setAccessible(true);
        returnValue = method.invoke(owner, args);

        return returnValue;
    }

    public static Object invokeDeclaredMethod(Object owner, String methodName, Object[] args, Class... argTypes)
            throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        Object returnValue = null;
        Class ownerClass = owner.getClass();
        Class[] argsClass = new Class[args.length];
        if (argTypes.length > 0 && argTypes.length == args.length) {
            argsClass = argTypes;
        } else {
            for (int i = 0, j = args.length; i < j; i++) {
                argsClass[i] = args[i].getClass();
            }
        }
        Method method = ownerClass.getDeclaredMethod(methodName, argsClass);
        method.setAccessible(true);
        returnValue = method.invoke(owner, args);

        return returnValue;
    }

    /**
     * DOC ycbai Comment method "invokeStaticMethod".
     * 
     * Returns the value of a static method.
     * 
     * @param className
     * @param loader
     * @param methodName
     * @param args
     * @param argTypes
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static Object invokeStaticMethod(String className, ClassLoader loader, String methodName, Object[] args,
            Class... argTypes) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        Object returnValue = null;
        Class ownerClass = null;
        if (loader != null) {
            ownerClass = Class.forName(className, true, loader);
        } else {
            ownerClass = Class.forName(className);
        }
        Class[] argsClass = new Class[args.length];
        if (argTypes.length > 0 && argTypes.length == args.length) {
            argsClass = argTypes;
        } else {
            for (int i = 0, j = args.length; i < j; i++) {
                argsClass[i] = args[i].getClass();
            }
        }
        Method method = ownerClass.getMethod(methodName, argsClass);
        method.setAccessible(true);
        returnValue = method.invoke(null, args);

        return returnValue;
    }

    public static Object invokeStaticMethod(String className, String methodName, Object[] args, Class... argTypes)
            throws SecurityException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        return invokeStaticMethod(className, null, methodName, args, argTypes);
    }

    /**
     * DOC ycbai Comment method "newInstance".
     * 
     * Create a new instance of a class.
     * 
     * @param className
     * @param initialize
     * @param loader
     * @param args
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws IllegalArgumentException
     */
    public static Object newInstance(String className, ClassLoader loader, Object[] args, Class... argTypes)
            throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException,
            InstantiationException, IllegalAccessException, InvocationTargetException {
        Object instance = null;
        Class newClass = null;
        if (loader != null) {
            newClass = Class.forName(className, true, loader);
        } else {
            newClass = Class.forName(className);
        }
        Class[] argsClass = new Class[args.length];
        if (argTypes.length > 0 && argTypes.length == args.length) {
            argsClass = argTypes;
        } else {
            for (int i = 0, j = args.length; i < j; i++) {
                argsClass[i] = args[i].getClass();
            }
        }
        Constructor cons = newClass.getDeclaredConstructor(argsClass);
        cons.setAccessible(true);
        instance = cons.newInstance(args);

        return instance;
    }

    public static Object newInstance(String className, Object[] args, Class... argTypes) throws SecurityException,
            IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException {
        return newInstance(className, null, args, argTypes);
    }

    /**
     * DOC ycbai Comment method "isInstance".
     * 
     * Whether or not the object is the instance of the class.
     * 
     * @param obj
     * @param cls
     * @return
     */
    public static boolean isInstance(Object obj, Class cls) {
        return cls.isInstance(obj);
    }

    /**
     * DOC ycbai Comment method "getByArray".
     * 
     * Returns the value of a element of a array by the index.
     * 
     * @param array
     * @param index
     * @return
     */
    public static Object getByArray(Object array, int index) {
        return Array.get(array, index);
    }
    

    public static Class getClass(String className, ClassLoader loader) {
        Class ownerClass = null;
        try {
            if (loader != null) {
                ownerClass = Class.forName(className, true, loader);
            } else {
                ownerClass = Class.forName(className);
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return ownerClass;
    }
}
