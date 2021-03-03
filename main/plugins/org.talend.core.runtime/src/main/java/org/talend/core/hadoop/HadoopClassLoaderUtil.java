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
package org.talend.core.hadoop;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.classloader.DynamicClassLoader;
/**
 * created by hcyi on Dec 3, 2019
 * Detailled comment
 *
 */
public class HadoopClassLoaderUtil {

    public final static String[] webhdfsExtraJars = { "jersey-core-1.9.jar", "jersey-client-1.9.jar", "jetty-util-6.1.26.jar" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    private final static String[] webhdfsExtraJarNames = { "jersey-core", "jersey-client", "jetty-util" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    public static boolean isWebHDFS(String nameNodeURI) {
        if (nameNodeURI != null
                && (nameNodeURI.toLowerCase().startsWith("swebhdfs://") || nameNodeURI.toLowerCase().startsWith("webhdfs://"))) {//$NON-NLS-1$ //$NON-NLS-2$
            return true;
        }
        return false;
    }

    public static ClassLoader addExtraJars(ClassLoader classLoader, EHadoopCategory category, String nameNodeURI) {
        if (isWebHDFS(nameNodeURI) && classLoader != null && classLoader instanceof DynamicClassLoader) {
            DynamicClassLoader loader = (DynamicClassLoader) classLoader;
            switch (category) {
            case HDFS:
                List<String> excludedJars = new ArrayList<String>();
                Set<String> libraries = loader.getLibraries();
                for (String lib : libraries) {
                    for (int i = 0; i < webhdfsExtraJarNames.length; i++) {
                        if (lib.contains(webhdfsExtraJarNames[i])) {
                            excludedJars.add(webhdfsExtraJars[i]);
                            break;
                        }
                    }
                }
                if (excludedJars.size() < 3) {
                    List<String> addedJars = new ArrayList<String>();
                    for (String webhdfsExtraJar : webhdfsExtraJars) {
                        if (!excludedJars.contains(webhdfsExtraJar)) {
                            addedJars.add(webhdfsExtraJar);
                        }
                    }
                    try {
                        classLoader = DynamicClassLoader.createNewOneBaseLoader(loader,
                                addedJars.toArray(new String[addedJars.size()]), null);
                    } catch (MalformedURLException e) {
                        ExceptionHandler.process(e);
                    }
                }
                break;
            }
        }
        return classLoader;
    }
}