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
package org.talend.designer.maven.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import org.apache.commons.lang3.StringUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.properties.Property;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.designer.runprocess.IRunProcessService;

/**
 * DOC zwxue class global comment. Detailled comment
 */
public class ClasspathsJarGenerator {

    private static final String CLASSPATHS_JAR_NAME = "classpath.jar"; //$NON-NLS-1$

    private static final String SLASH = "/"; //$NON-NLS-1$

    /**
     * Manifest classpath separator
     */
    private static final String BLANK = " "; //$NON-NLS-1$

    private static IRunProcessService service;

    public static String createJar(Property property, String classpath) throws Exception {
        return createJar(property, classpath, BLANK);
    }

    public static String createJar(Property property, String classpath, String separator) throws Exception {
        classpath = generateClasspathForManifest(classpath, separator);

        Manifest manifest = new Manifest();
        Attributes a = manifest.getMainAttributes();
        a.put(Attributes.Name.MANIFEST_VERSION, "1.0"); //$NON-NLS-1$
        a.put(Attributes.Name.IMPLEMENTATION_VENDOR, "Talend Open Studio"); //$NON-NLS-1$
        a.put(Attributes.Name.CLASS_PATH, classpath);

        String jarLocation = getJarLocation(property);
        File jarFile = new File(jarLocation);
        if (!jarFile.exists()) {
            jarFile.createNewFile();
        }
        JarOutputStream stream = null;
        try {
            stream = new JarOutputStream(new FileOutputStream(jarLocation), manifest);
            stream.flush();
        } finally {
            stream.close();
        }

        return jarLocation;
    }

    private static String generateClasspathForManifest(String classpath, String separator) throws Exception {
        if (BLANK.equals(separator)) {
            return classpath;
        }
        String[] classpathArray = classpath.split(separator);
        StringBuilder newClasspath = new StringBuilder();
        for (String cp : classpathArray) {
            if (cp.endsWith(".jar") || cp.endsWith(".exe")) { //$NON-NLS-1$ //$NON-NLS-2$
                // files should be start with /
                cp = prepend(cp);
            } else if (!cp.endsWith(".")) { //$NON-NLS-1$
                // directory need to wrap with /
                cp = wrapWithSlash(cp);
            }
            // cp = StringUtils.replace(cp, " ", "%20"); //$NON-NLS-1$ //$NON-NLS-2$
            newClasspath.append(cp + BLANK);
        }
        return newClasspath.toString().trim();
    }

    public static String getClasspathFromManifest(Property property) throws Exception {
        String jarLocation = getJarLocation(property);
        JarInputStream stream = null;
        try {
            stream = new JarInputStream(new FileInputStream(jarLocation));
            Manifest manifest = stream.getManifest();
            String classpath = manifest.getMainAttributes().getValue(Attributes.Name.CLASS_PATH);
            return classpath;
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    private static String wrapWithSlash(String str) {
        str = StringUtils.strip(str, SLASH);
        str = StringUtils.wrap(str, SLASH);
        return str;
    }

    private static String prepend(String str) {
        str = StringUtils.prependIfMissing(str, SLASH);
        return str;
    }

    private static String getJarLocation(Property property) {
        ITalendProcessJavaProject jobProject = getRunProcessService().getTalendJobJavaProject(property);
        String jarLocation = jobProject.getTargetFolder().getFile(CLASSPATHS_JAR_NAME).getLocation().toPortableString();
        return jarLocation;
    }

    private static IRunProcessService getRunProcessService() {
        if (service == null) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
                service = (IRunProcessService) GlobalServiceRegister.getDefault().getService(IRunProcessService.class);
            }
        }
        return service;
    }

}
