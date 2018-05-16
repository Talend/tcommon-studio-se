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
package org.talend.osgi.hook;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.osgi.container.ModuleRevision;
import org.eclipse.osgi.internal.hookregistry.BundleFileWrapperFactoryHook;
import org.eclipse.osgi.storage.BundleInfo.Generation;
import org.eclipse.osgi.storage.bundlefile.BundleEntry;
import org.eclipse.osgi.storage.bundlefile.BundleFile;
import org.eclipse.osgi.storage.bundlefile.BundleFileWrapper;
import org.eclipse.osgi.storage.bundlefile.FileBundleEntry;
import org.osgi.framework.Bundle;
import org.osgi.framework.namespace.HostNamespace;
import org.osgi.framework.wiring.BundleWire;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.log.LogService;
import org.talend.osgi.hook.maven.MavenResolver;
import org.talend.osgi.hook.notification.JarMissingObservable;

/**
 * created by sgandon on 14 oct. 2014 Detailled comment
 *
 */
public class JarLoaderBundleFileWrapperFactory implements BundleFileWrapperFactoryHook {

    // file instance use to return the information that the jar was found in a fragment
    private static final File FOUND_IN_FRAGMENT_FILE = new File(""); //$NON-NLS-1$

    final String listOfBundlePrefixes = System.getProperty("org.talend.bundle.prefixes.for.jar.loader", //$NON-NLS-1$
            "org.talend,com.oaklandsw"); //$NON-NLS-1$

    Set<String> listOfBundlePrefixesSet = null;

    /**
     * created by sgandon on 17 oct. 2014 Detailled comment
     *
     */
    static class TalendBundleFileWrapper extends BundleFileWrapper {

        private Generation generation;

        Set<String> missingJars;

        /**
         * DOC sgandon BundleFileWrapperExtension constructor comment.
         * 
         * @param bundleFile
         * @param generation
         */
        private TalendBundleFileWrapper(BundleFile bundleFile, Generation generation) {
            super(bundleFile);
            this.generation = generation;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.osgi.storage.bundlefile.BundleFileWrapper#getEntry(java.lang.String)
         */
        @Override
        public BundleEntry getEntry(String path) {
            // we are using te getEntry to trick equinox when a jar file is missing
            BundleEntry be = super.getEntry(path);
            // handle missing jar only for Hosts Bundles and not Fragments
            if (be == null
                    && path.endsWith(".jar") && ((generation.getRevision().getTypes() & ModuleRevision.TYPE_FRAGMENT) == 0)) { //$NON-NLS-1$ //jar file that does not exists.
                // use the getFile to find the jar from the lib/java folder.
                File file = getFile(path, false, false);
                if (file == null) {
                    // look into fragments first because since we return a fake entry (in the next lines) that will
                    // prevent to look into
                    // fragment in calling methods.
                    URL resourcePathInFragment = findInFragments(generation.getRevision().getBundle(), path);
                    if (resourcePathInFragment != null) {
                        return null;
                    }
                    // if not found in fragments then we can notify the observers
                    // notify any observable to get a chance for the lib to be installed
                    String jarName = new File(path).getName();
                    File libJavaFolderFile = MissingJarServices.getLibJavaFolderFile();

                    MissingJarServices.getJarMissingObservable().notifyObservers(
                            new JarMissingObservable.JarMissingEvent(jarName, generation, libJavaFolderFile.getAbsolutePath()));
                    MissingJarServices
                            .logDebugInfo("MissingJar notification for :" + generation.getRevision().getSymbolicName() + "/" + path + " (" + MissingJarServices.getJarMissingObservable().countObservers() + " observers)."); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

                    // let's do another check to see if the bundle was installed during the above
                    // notification
                    MavenResolver mavenResolver = MissingJarServices.getMavenResolver();
                    URI mvnUri;
                    try {
                        mvnUri = URIUtil.getMvnUri(path, generation, true);
                        if (mvnUri != null && mavenResolver != null) {// try to resolve it
                            try {
                                File jarFile = mavenResolver.resolve(mvnUri.toASCIIString());
                                be = new FileBundleEntry(jarFile, path);
                            } catch (IOException e) {// failed to find local artifact, try snapshot
                                try {
                                    // add snapshot to uri
                                    URI snapMvnUri = URIUtil.addSnapshotToUri(mvnUri);
                                    File jarFile = mavenResolver.resolve(snapMvnUri.toASCIIString());
                                    be = new FileBundleEntry(jarFile, path);
                                } catch (IOException e1) {// failed to find SNAPSHOT artifact

                                    MissingJarServices
                                            .getLogService()
                                            .log(LogService.LOG_DEBUG,
                                                    "Could not resolve the maven URI (" + mvnUri + ") for path :" + generation.getRevision().getSymbolicName() + "/" + path, e1); //$NON-NLS-2$
                                }
                            }
                        }// else no maven URI found so try old style lib/java folder
                        if (be == null) {
                            File jarFile = new File(libJavaFolderFile, jarName);
                            if (jarFile.exists()) {
                                be = new FileBundleEntry(jarFile, path);
                            } else {// no jar found so return a

                                // fake the entry cause using the getFile returns a file that get tested for existance
                                // this does not get tested. This is called the first time the jar is missing.
                                // we also record the missing jar
                                getMissingJars().add(path + '/');// we add slash because the wrapBundleFile,
                                                                 // getEntry("");
                                                                 // line
                                                                 // will
                                                                 // call this method with path equals to path + '/'
                                be = new MissingJarBundleEntry(path);
                                MissingJarServices
                                        .logDebugInfo("fake FileBundleEntry created for :" + generation.getRevision().getSymbolicName() + "/" + path); //$NON-NLS-1$//$NON-NLS-2$
                            }
                        }
                    } catch (URISyntaxException e1) {
                        MissingJarServices
                                .getLogService()
                                .log(LogService.LOG_ERROR,
                                        "Error reading the Bundle-Classpath maven URI for path :" + generation.getRevision().getSymbolicName() + "/" + path, e1); //$NON-NLS-2$

                    }
                } else {// else entry is a jar an was found when calling getFile
                    be = new FileBundleEntry(file, path);
                }
            } else {
                // check for a fake entry, that is a missing jar already check and create a specific bundle entry so
                // that the Wrapper factory can detect this is a missing jar entry
                if (getMissingJars().contains(path)) {
                    be = new MissingJarBundleEntry(path);
                    MissingJarServices
                            .logDebugInfo("MissingJarBundleEntry created for :" + generation.getRevision().getSymbolicName() + "/" + path); //$NON-NLS-1$//$NON-NLS-2$
                }
            }

            // else be is found or not a jar so don't care.
            return be;
        }

        /**
         * DOC sgandon Comment method "getMissingJars".
         * 
         * @return
         */
        private Set<String> getMissingJars() {
            if (missingJars == null) {
                missingJars = new HashSet<String>();
            }
            return missingJars;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.osgi.storage.bundlefile.BundleFileWrapper#getFile(java.lang.String, boolean)
         */
        @Override
        public File getFile(String path, boolean nativeCode) {
            return getFile(path, nativeCode, true);
        }

        /**
         * DOC sgandon Comment method "getFileBis".
         * 
         * @param path
         * @param nativeCode
         * @return
         */
        protected File getFile(String path, boolean nativeCode, boolean notifyMissingJar) {
            File file = super.getFile(path, nativeCode);
            // handle missing jar only for Host Bundles and not Fragments
            if (file == null
                    && path.endsWith(".jar") && ((generation.getRevision().getTypes() & ModuleRevision.TYPE_FRAGMENT) == 0)) { //$NON-NLS-1$ //jar file not found in current bundle.
                URL resourcePathInFragment = findInFragments(generation.getRevision().getBundle(), path);
                if (resourcePathInFragment != null) {
                    return null;
                }
                file = MissingJarBundleFileWrapper.lookForMissingJar(path, generation, notifyMissingJar);
            }// else file found so return default value.
            return file;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.osgi.internal.hookregistry.BundleFileWrapperFactoryHook#wrapBundleFile(org.eclipse.osgi.storage.
     * bundlefile.BundleFile, org.eclipse.osgi.storage.BundleInfo.Generation, boolean)
     */
    @Override
    public BundleFileWrapper wrapBundleFile(BundleFile bundleFile, Generation generation, boolean base) {
        if (canHandleBundle(bundleFile.getBaseFile().getName())) {
            // all this is done because NestedDirbundleFile has a private cp that can't be acced
            if (base) {// base bundle file so create a Talend wrapper in case one jar is missing
                return new TalendBundleFileWrapper(bundleFile, generation);
            } else {// inner jar or inner folder or missing jar bundle entry
                // only handle missing jar entry
                // we check by calling the getEntry that we have a missing jar
                BundleEntry entry = bundleFile.getEntry(""); //$NON-NLS-1$
                if (entry != null && entry instanceof MissingJarBundleEntry) {
                    String path = ((MissingJarBundleEntry) entry).path;
                    MissingJarServices
                            .logDebugInfo("MissingJarBundleFileWrapper creating for :" + generation.getRevision().getSymbolicName() + "/" + path); //$NON-NLS-1$//$NON-NLS-2$ 
                    return new MissingJarBundleFileWrapper(bundleFile, generation, path);

                }// else not the trick for proxying missign jars so do not create any wrapper.
            }
        }
        return null;// means we do not create any wrapper.
    }

    /**
     * DOC sgandon Comment method "canHandleBundle".
     * 
     * @param bundleSymbolicName
     * @return
     */
    private boolean canHandleBundle(String bundleSymbolicName) {
        if (listOfBundlePrefixesSet == null) {
            String[] prefixesArray = listOfBundlePrefixes.split(","); //$NON-NLS-1$
            listOfBundlePrefixesSet = new HashSet<String>(prefixesArray.length);
            Collections.addAll(listOfBundlePrefixesSet, prefixesArray);
        }
        for (String prefix : listOfBundlePrefixesSet) {
            if (bundleSymbolicName.startsWith(prefix)) {
                return true;
            }// else keep looking
        }
        return false;
    }

    private static URL findInFragments(Bundle b, String filePath) {
        BundleWiring myWiring = b.adapt(BundleWiring.class);
        if (myWiring != null) {
            List<BundleWire> wires = myWiring.getProvidedWires(HostNamespace.HOST_NAMESPACE);
            for (BundleWire wire : wires) {
                Bundle fragment = wire.getRequirerWiring().getBundle();
                URL fileURL = fragment.getEntry(filePath.toString());
                if (fileURL != null) {
                    return fileURL;
                }
            }
        }// else return null cause we cannot get the wiring
        return null;
    }

}
