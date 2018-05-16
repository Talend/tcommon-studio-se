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
package org.talend.core.classloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public class DynamicClassLoader extends URLClassLoader {

    private final static String PATH_SEPARATOR = "/"; //$NON-NLS-1$

    private String libStorePath;

    /**
     * DOC ycbai DynamicClassLoader constructor comment.
     */
    public DynamicClassLoader() {
        this(new URL[0]);
    }

    public DynamicClassLoader(URL[] urls) {
        super(urls);
    }

    public DynamicClassLoader(ClassLoader parent) {
        this(new URL[0], parent);
    }

    public DynamicClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public void addLibrary(String lib) {
        if (lib != null) {
            File libFile = new File(lib);
            try {
                addURL(libFile.toURI().toURL());
            } catch (MalformedURLException e) {
                ExceptionHandler.process(e);
            }
        }
    }

    public void addLibraries(List<String> libs) {
        if (libs != null && libs.size() > 0) {
            for (String lib : libs) {
                addLibrary(lib);
            }
        }
    }

    /**
     * DOC ycbai Comment method "getLibraries".
     * 
     * <p>
     * Get libraries which this classloader contains.
     * </p>
     * 
     * @return
     */
    public Set<String> getLibraries() {
        Set<String> set = new HashSet<String>();
        URL[] urLs = getURLs();
        if (urLs != null) {
            for (URL url : urLs) {
                try {
                    File file = new File(url.toURI());
                    set.add(file.getName());
                } catch (URISyntaxException e) {
                    // dont care...
                }
            }
        }

        return set;
    }

    /**
     * DOC ycbai Comment method "getLibStorePath".
     * 
     * <p>
     * Get folder path which stores libraries of classloader.
     * </p>
     * 
     * @return
     */
    public String getLibStorePath() {
        return this.libStorePath;
    }

    public void setLibStorePath(String libStorePath) {
        this.libStorePath = libStorePath;
    }

    public static DynamicClassLoader createNewOneBaseLoader(DynamicClassLoader baseLoader, String[] addedJars,
            String[] excludedJars) throws MalformedURLException {
        if (baseLoader == null) {
            baseLoader = new DynamicClassLoader();
        }
        if (addedJars == null) {
            addedJars = new String[0];
        }
        if (excludedJars == null) {
            excludedJars = new String[0];
        }
        URL[] baseURLs = baseLoader.getURLs();
        String libPath = baseLoader.getLibStorePath();
        if (libPath == null) {
            libPath = ClassLoaderFactory.getLibPath();
        }
        List<URL> urlList = new ArrayList<URL>(Arrays.asList(baseURLs));
        updateLoaderURLs(urlList, libPath, addedJars, true);
        updateLoaderURLs(urlList, libPath, excludedJars, false);
        DynamicClassLoader loader = new DynamicClassLoader(urlList.toArray(new URL[0]));
        loader.setLibStorePath(libPath);

        return loader;
    }

    public static DynamicClassLoader updateBaseLoader(DynamicClassLoader baseLoader, String[] addedJars)
            throws MalformedURLException {
        if (baseLoader == null) {
            baseLoader = new DynamicClassLoader();
        }
        if (addedJars == null) {
            addedJars = new String[0];
        }
        String libPath = baseLoader.getLibStorePath();
        if (libPath == null) {
            libPath = ClassLoaderFactory.getLibPath();
        }
        updateBaseLoaderURLs(baseLoader, addedJars, libPath);
        baseLoader.setLibStorePath(libPath);
        return baseLoader;
    }

    private static void updateBaseLoaderURLs(DynamicClassLoader baseLoader, String[] jars, String libPath)
            throws MalformedURLException {
        Set<String> libraries = baseLoader.getLibraries();
        for (String jarName : jars) {
            ILibraryManagerService librairesService = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                    ILibraryManagerService.class);
            librairesService.retrieve(jarName, libPath, true, new NullProgressMonitor());
            String jarPath = libPath + PATH_SEPARATOR + jarName;
            File jarFile = new File(jarPath);
            if (jarFile.exists()) {
                if (!libraries.contains(jarName)) {
                    baseLoader.addLibrary(jarFile.getAbsolutePath());
                }
            }
        }
    }

    private static void updateLoaderURLs(List<URL> urlList, String libPath, String[] jars, boolean added)
            throws MalformedURLException {
        for (String jarName : jars) {
            if (added) {
                ILibraryManagerService librairesService = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                        ILibraryManagerService.class);
                librairesService.retrieve(jarName, libPath, true, new NullProgressMonitor());
            }
            String jarPath = libPath + PATH_SEPARATOR + jarName;
            File jarFile = new File(jarPath);
            URL jarUrl = jarFile.toURI().toURL();
            if (jarFile.exists()) {
                if (added && !urlList.contains(jarUrl)) {
                    urlList.add(jarUrl);
                }
                if (!added && urlList.contains(jarUrl)) {
                    urlList.remove(jarUrl);
                }
            }
        }
    }

}
