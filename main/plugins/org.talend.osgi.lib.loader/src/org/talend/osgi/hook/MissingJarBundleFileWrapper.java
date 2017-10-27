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
package org.talend.osgi.hook;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;

import org.eclipse.osgi.container.Module;
import org.eclipse.osgi.storage.BundleInfo.Generation;
import org.eclipse.osgi.storage.bundlefile.BundleEntry;
import org.eclipse.osgi.storage.bundlefile.BundleFile;
import org.eclipse.osgi.storage.bundlefile.BundleFileWrapper;
import org.osgi.service.log.LogService;
import org.talend.osgi.hook.maven.MavenResolver;
import org.talend.osgi.hook.notification.JarMissingObservable;

/**
 * This file wrapper is used to proxy the loading of a missing jar. whenever there is a call to a BundleFile method, it
 * will use the resolved jar instance is any or else try to find it from the lib/java folder or ask the user for
 * providing this jar.
 *
 */
public class MissingJarBundleFileWrapper extends BundleFileWrapper {

    private BundleFile resolvedBundleFile;

    private String jarPath;

    private Generation generation;

    @SuppressWarnings("unchecked")
    private Enumeration<String> EMPTY_STRING_ENUM = Collections.enumeration(Collections.EMPTY_LIST);

    /**
     * DOC sgandon MissingJarBundleFileWrapper constructor comment.
     * 
     * @param bundleFile
     * @param generation
     * @param jarPath
     */
    public MissingJarBundleFileWrapper(BundleFile bundleFile, Generation generation, String jarPath) {
        super(bundleFile);
        this.generation = generation;
        this.jarPath = jarPath;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.osgi.storage.bundlefile.BundleFileWrapper#getFile(java.lang.String, boolean)
     */
    @Override
    public File getFile(String path, boolean nativeCode) {
        if (resolvedBundleFile == null) {
            resolveMissingJarBundleFile();
        }
        if (resolvedBundleFile != null) {
            return resolvedBundleFile.getFile(path, nativeCode);
        }
        return null;
    }

    /**
     * DOC sgandon Comment method "createResolvedBundleFile".
     * 
     * @param jarFile
     */
    private void createResolvedBundleFile(File jarFile) {
        resolvedBundleFile = generation.getBundleInfo().getStorage().createBundleFile(jarFile, generation, false, false);
        MissingJarServices
                .logDebugInfo("MissingJarBundleFileWrapper resolved :" + generation.getRevision().getSymbolicName() + "/" + jarPath + "(" + jarFile.getAbsolutePath() + ")"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$                     
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.osgi.storage.bundlefile.BundleFileWrapper#getEntry(java.lang.String)
     */
    @Override
    public BundleEntry getEntry(String path) {
        if (resolvedBundleFile == null) {
            resolveMissingJarBundleFile();
        }
        if (resolvedBundleFile != null) {
            return resolvedBundleFile.getEntry(path);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.osgi.storage.bundlefile.BundleFileWrapper#getEntryPaths(java.lang.String)
     */
    @Override
    public Enumeration<String> getEntryPaths(String path) {
        if (resolvedBundleFile == null) {
            resolveMissingJarBundleFile();
        }
        if (resolvedBundleFile != null) {
            return resolvedBundleFile.getEntryPaths(path);
        }
        return EMPTY_STRING_ENUM;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.osgi.storage.bundlefile.BundleFileWrapper#getEntryPaths(java.lang.String, boolean)
     */
    @Override
    public Enumeration<String> getEntryPaths(String path, boolean recurse) {
        if (resolvedBundleFile == null) {
            resolveMissingJarBundleFile();
        }
        if (resolvedBundleFile != null) {
            return resolvedBundleFile.getEntryPaths(path, recurse);
        }
        return EMPTY_STRING_ENUM;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.osgi.storage.bundlefile.BundleFile#getResourceURL(java.lang.String,
     * org.eclipse.osgi.container.Module, int)
     */
    @Override
    public URL getResourceURL(String path, Module hostModule, int index) {
        if (resolvedBundleFile == null) {
            resolveMissingJarBundleFile();
        }
        if (resolvedBundleFile != null) {
            return resolvedBundleFile.getResourceURL(path, hostModule, index);
        }
        return null;
    }

    /**
     * DOC sgandon Comment method "resolveMissingJarBundleFile".
     */
    void resolveMissingJarBundleFile() {
        File jarFile = lookForMissingJar(jarPath.substring(0, jarPath.length() - 1), generation, true);
        if (jarFile != null && jarFile.exists()) {
            createResolvedBundleFile(jarFile);
        } else {// log an error if observer where notified and nothing was resolved
            if (MissingJarServices.getJarMissingObservable().countObservers() != 0) {
                String message = "one third party library file [" + jarPath.substring(0, jarPath.length() - 1) //$NON-NLS-1$
                        + "] was not found, it is required for bundle [" + getBundleFile().getBaseFile().getName() //$NON-NLS-1$
                        + "]."; //$NON-NLS-1$ 
                if (MissingJarServices.getLogService() != null) {
                    MissingJarServices.getLogService().log(LogService.LOG_DEBUG, message);
                } else {// no log service available so throw a runtime exception to try
                    // notifying the user of the problem.
                    throw new RuntimeException(message);
                }
            }// else we don't have any observer, this means the Studio is not yet completly loaded.
        }

    }

    /**
     * Looks for missing jar using a maven resolver if a maven parameter is found in the Bundle-Classpath hearder.
     * otherwise looks for the bundle in the lib/java folder.
     * 
     * @param path of the jar to find as denoted in the Bundle-ClassPath header
     * @param generation the generation of the bundle to get the manifest from
     * @param notifyMissingJar this will call the notification service if true
     */
    static public File lookForMissingJar(String path, Generation generation, boolean notifyMissingJar) {
        // look for mvn url.
        try {
            MavenResolver mavenResolver = MissingJarServices.getMavenResolver();
            URI mvnUri = URIUtil.getMvnUri(path, generation, true);
            if (mvnUri != null && mavenResolver != null) {// try to resolve it
                try {
                    File jarFile = mavenResolver.resolve(mvnUri.toASCIIString());
                    return jarFile;
                } catch (IOException e) {// failed to find local artifact, try snapshot
                    try {
                        // add snapshot to uri
                        URI snapMvnUri = URIUtil.addSnapshotToUri(mvnUri);
                        File jarFile = mavenResolver.resolve(snapMvnUri.toASCIIString());
                        return jarFile;
                    } catch (IOException e1) {// failed to find SNAPSHOT artifact

                        MissingJarServices
                                .getLogService()
                                .log(LogService.LOG_DEBUG,
                                        "Could not resolve the maven URI (" + mvnUri + ") for path :" + generation.getRevision().getSymbolicName() + "/" + path, e1); //$NON-NLS-2$
                    }
                }
            }// else no maven URI found so try old style lib/java folder
            File libJavaFolderFile = MissingJarServices.getLibJavaFolderFile();
            if (libJavaFolderFile != null) {
                String jarName = new File(path).getName();
                File jarFile = new File(libJavaFolderFile, jarName);
                if (jarFile.exists()) {
                    return jarFile;
                } else {// jar file not found in the lib/java folder
                    if (notifyMissingJar) {
                        // notify any observable to get a chance for the lib to be installed
                        MissingJarServices.getJarMissingObservable()
                                .notifyObservers(
                                        new JarMissingObservable.JarMissingEvent(jarName, generation, libJavaFolderFile
                                                .getAbsolutePath()));
                        MissingJarServices
                                .logDebugInfo("MissingJar notification for :" + generation.getRevision().getSymbolicName() + "/" + path + " (" + MissingJarServices.getJarMissingObservable().countObservers() + " observers)."); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

                        // let's do another check to see if the bundle was installed during the above
                        // notification
                        if (mvnUri != null && mavenResolver != null) {// try to resolve it
                            try {
                                jarFile = mavenResolver.resolve(mvnUri.toASCIIString());
                                return jarFile;
                            } catch (IOException e) {// failed to find local artifact, try snapshot
                                try {
                                    // add snapshot to uri
                                    URI snapMvnUri = URIUtil.addSnapshotToUri(mvnUri);
                                    jarFile = mavenResolver.resolve(snapMvnUri.toASCIIString());
                                    return jarFile;
                                } catch (IOException e1) {// failed to find SNAPSHOT artifact

                                    MissingJarServices
                                            .getLogService()
                                            .log(LogService.LOG_DEBUG,
                                                    "Could not resolve the maven URI (" + mvnUri + ") for path :" + generation.getRevision().getSymbolicName() + "/" + path, e1); //$NON-NLS-2$
                                }
                            }
                        }// else no maven URI found so try old style lib/java folder
                        if (jarFile.exists()) {
                            return jarFile;
                        } // else no jar found so return null
                    }// else no nothification required.
                }
            }// else //no lib folder found so return null
        } catch (URISyntaxException | RuntimeException e) {
            MissingJarServices
                    .getLogService()
                    .log(LogService.LOG_ERROR,
                            "Error reading the Bundle-Classpath maven URI for path :" + generation.getRevision().getSymbolicName() + "/" + path, e); //$NON-NLS-2$
        }
        return null;
    }

}
