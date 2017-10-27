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
package org.talend.commons.ui.utils.loader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;

/**
 * ggu class global comment. Detailled comment
 */
public class MyURLClassLoader extends URLClassLoader {

    public static interface IAssignableClassFilter {

        public boolean filter(URL[] urls);

        public boolean filter(Class clazz);

        public boolean filter(String clazzName);
    }

    private static Logger log = Logger.getLogger(MyURLClassLoader.class);

    private Map pclasses = new HashMap();

    public MyURLClassLoader(String fileName) throws IOException {
        this(new File(fileName).toURL());
    }

    public MyURLClassLoader(URL url) {
        this(new URL[] { url });
    }

    public MyURLClassLoader(URL[] urls) {
        super(urls, Class.class.getClassLoader());
    }

    public MyURLClassLoader(URL[] urls, ClassLoader parentLoader) {
        super(urls, parentLoader);
    }

    public Class[] getAssignableClasses(Class type) throws IOException {
        return getAssignableClasses(type, null);
    }

    @SuppressWarnings("unchecked")
    public Class[] getAssignableClasses(Class type, IAssignableClassFilter filter) throws IOException {
        List classes = new ArrayList();
        URL[] urls = getURLs();
        for (URL url : urls) {
            if (filter != null && filter.filter(new URL[] { url })) {
                continue;
            }
            File file = new File(url.getFile());
            if (!file.isDirectory() && file.exists() && file.canRead()) {
                ZipFile zipFile = null;
                try {
                    zipFile = new ZipFile(file);
                } catch (IOException ex) {
                    ExceptionHandler.process(ex);
                }
                Enumeration<? extends ZipEntry> entries = zipFile.entries();
                while (entries.hasMoreElements()) {
                    Class cls = null;
                    String entryName = entries.nextElement().getName();
                    String className = changeFileNameToClassName(entryName);
                    if (className != null) {
                        if (filter != null && filter.filter(className)) {
                            continue;
                        }
                        try {
                            cls = loadClass(className);
                        } catch (Throwable th) {
                            log.warn(th);
                        }
                        if (cls != null) {
                            if (filter != null && filter.filter(cls)) {
                                continue;
                            }
                            if (isAssignableType(type, cls)) {
                                classes.add(cls);
                            }
                        }
                    }
                }
            }
        }
        return (Class[]) classes.toArray(new Class[classes.size()]);
    }

    @SuppressWarnings("unchecked")
    private boolean isAssignableType(Class type, Class current) {
        if (type == null || current == null || current.equals(Object.class)) {
            return false;
        }

        if (type.isAssignableFrom(current)) {
            return true;
        } else
        // sometimes can not assign the java generic, use the class url
        if (type.getName() != null && type.getName().equals(current.getName())) {
            return true;
        } else {
            //
            if (type.isInterface()) {//
                for (Class interfaceClazz : current.getInterfaces()) {
                    if (interfaceClazz.equals(type)) {
                        return true;
                    } else {
                        if (isAssignableType(type, interfaceClazz)) {
                            return true;
                        }
                    }
                }
                if (isAssignableType(type, current.getSuperclass())) {
                    return true;
                }
            } else {
                return isAssignableType(type, current.getSuperclass());
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.net.URLClassLoader#findClass(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    protected synchronized Class findCslass(String className) throws ClassNotFoundException {
        Class cls = (Class) pclasses.get(className);
        if (cls == null) {
            cls = super.findClass(className);
            pclasses.put(className, cls);
        }
        return cls;
    }

    public static String changeFileNameToClassName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("File Name == null");
        }
        String className = null;
        if (name.toLowerCase().endsWith(".class")) {
            className = name.replace('/', '.');
            className = className.replace('\\', '.');
            className = className.substring(0, className.length() - 6);
        }
        return className;
    }

    protected void classHasBeenLoaded(Class cls) {
    }

}
