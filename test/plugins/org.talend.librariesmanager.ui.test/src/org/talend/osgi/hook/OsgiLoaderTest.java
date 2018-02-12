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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.ops4j.pax.url.mvn.MavenResolver;
import org.ops4j.pax.url.mvn.ServiceConstants;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.talend.osgi.hook.notification.JarMissingObservable;
import org.talend.osgi.hook.notification.JarMissingObservable.JarMissingEvent;

/**
 * created by sgandon on 4 févr. 2015 Detailled comment
 *
 */
public class OsgiLoaderTest {

    private static final String PATH_RES = "/resources/plugins/";

    /**
     * created by sgandon on 1 juin 2015 Detailled comment
     *
     */
    private class MissingJarObserverWithFolderResolution implements Observer {

        /**
         * 
         */
        private final Boolean[] observerCalled;

        private boolean checkJarName;

        /**
         * DOC sgandon MissingJarObserverWithFolderResolution constructor comment.
         * 
         * @param observerCalled
         */
        private MissingJarObserverWithFolderResolution(Boolean[] observerCalled, boolean checkJarName) {
            this.observerCalled = observerCalled;
            this.checkJarName = checkJarName;
        }

        @Override
        public void update(Observable o, Object arg) {
            if (arg != null && arg instanceof JarMissingEvent) {
                JarMissingEvent jarMissingEvent = (JarMissingEvent) arg;
                String jarName = jarMissingEvent.getJarName();
                if (!checkJarName || jarName.equals(EXISTING_JAR_NAME)) {

                    try {
                        copyExistingJarIntoLibJavaFolder(jarName);
                        this.observerCalled[0] = true;
                    } catch (URISyntaxException e1) {
                        throw new RuntimeException(e1);
                    }
                } else {
                    throw new RuntimeException("Missing jar observer did not receive the expected jar name:" + jarName + "!=" //$NON-NLS-1$
                            + EXISTING_JAR_NAME);
                }
            } else {// notification is not expected so thrown an exception for the test to fail
                throw new RuntimeException("Missing jar observer did not receive the proper event :" + arg);
            }
        }
    }

    /**
     * created by sgandon on 1 juin 2015 Detailled comment
     *
     */
    private class MissingJarObserverWithMavenResolution implements Observer {

        /**
         * 
         */
        private final Boolean[] observerCalled;

        private boolean checkJarName;

        /**
         * DOC sgandon MissingJarObserverWithMavenResolution constructor comment.
         * 
         * @param observerCalled
         */
        private MissingJarObserverWithMavenResolution(Boolean[] observerCalled, boolean checkJarName) {
            this.observerCalled = observerCalled;
            this.checkJarName = checkJarName;
        }

        @Override
        public void update(Observable o, Object arg) {
            if (arg != null && arg instanceof JarMissingEvent) {
                JarMissingEvent jarMissingEvent = (JarMissingEvent) arg;
                String jarName = jarMissingEvent.getJarName();
                if (!checkJarName || jarName.equals(EXISTING_JAR_NAME)) {
                    try {
                        copyExistingJarIntoMavenLocalRepo(false);
                        this.observerCalled[0] = true;
                    } catch (URISyntaxException | IOException e1) {
                        throw new RuntimeException(e1);
                    }

                } else {
                    throw new RuntimeException("Missing jar observer did not receive the expected jar name:" + jarName + "!=" //$NON-NLS-1$
                            + EXISTING_JAR_NAME);
                }
            } else {// notification is not expected so thrown an exception for the test to fail
                throw new RuntimeException("Missing jar observer did not receive the proper event :" + arg);
            }
        }
    }

    /**
     * 
     */
    private static final String EXISTING_JAR_NAME = "any-existing.jar"; //$NON-NLS-1$

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private Bundle osgiBundle;

    public static final String FRAGMENT_WITH_LIB = "test.lib.fragment.with.existing.jar"; //$NON-NLS-1$

    private static String FRAGMENT_WITH_LIB_FOLDER_URL;

    public static final String BUNDLE_NO_LIB = "test.lib.plugin.with.no.jar"; //$NON-NLS-1$

    private static String BUNDLE_NO_LIB_FOLDER_URL;

    public static final String BUNDLE_CALLING_LIB = "test.call.plugin.lib.jar"; //$NON-NLS-1$

    private static String BUNDLE_CALLING_LIB_FOLDER_URL;

    private static String LIB_POM_URL;

    private static File javaLibFolder;

    final File libJavaFolderFile = getLibJavaFolderFile();

    public File temp_maven_repo;

    private MavenResolver paxResolver;

    /**
     * return the folder where to find the missing libraries or null if none was defined
     * */
    static public File getLibJavaFolderFile() {
        if (javaLibFolder == null) {
            String property = System.getProperty("talend.library.path"); //$NON-NLS-1$
            if (property != null) {
                javaLibFolder = new File(property);
            }// else keep javaLibFolder null
        }
        return javaLibFolder;

    }

    @BeforeClass
    public static void staticInit() throws IOException {
        FRAGMENT_WITH_LIB_FOLDER_URL = FileLocator.toFileURL(OsgiLoaderTest.class.getResource(PATH_RES + FRAGMENT_WITH_LIB))
                .toExternalForm(); //$NON-NLS-1$
        BUNDLE_NO_LIB_FOLDER_URL = FileLocator.toFileURL(OsgiLoaderTest.class.getResource(PATH_RES + BUNDLE_NO_LIB))
                .toExternalForm(); //$NON-NLS-1$
        BUNDLE_CALLING_LIB_FOLDER_URL = FileLocator.toFileURL(OsgiLoaderTest.class.getResource(PATH_RES + BUNDLE_CALLING_LIB))
                .toExternalForm(); //$NON-NLS-1$
        LIB_POM_URL = FileLocator.toFileURL(OsgiLoaderTest.class.getResource(PATH_RES + "/pom.xml")).toExternalForm(); //$NON-NLS-1$
    }

    @Before
    public void init() throws IOException, ConfigurationException {
        // we use osgi bundle because the context of this bundle may not be created
        this.osgiBundle = Platform.getBundle("org.eclipse.osgi"); //$NON-NLS-1$
        // remove any file copied in the lib folder
        FileUtils.deleteDirectory(libJavaFolderFile);
        libJavaFolderFile.mkdirs();
        // create a temporary maven repo and setup pax service to use it as a local repo.
        File base = new File("target");
        base.mkdirs();
        try {
            temp_maven_repo = File.createTempFile("talend.hook.maven", ".dir", base);
            temp_maven_repo.delete();
            temp_maven_repo.mkdirs();
            System.out.println("Caching" + " to " + temp_maven_repo.getAbsolutePath());
        } catch (IOException exc) {
            throw new AssertionError("cannot create cache", exc);
        }
        ServiceReference<ManagedService> managedServiceRef = osgiBundle.getBundleContext().getServiceReference(
                ManagedService.class);
        if (managedServiceRef != null) {
            ManagedService managedService = osgiBundle.getBundleContext().getService(managedServiceRef);
            Dictionary<String, String> props = new Hashtable<String, String>();
            props.put(ServiceConstants.PID + "." + ServiceConstants.PROPERTY_LOCAL_REPOSITORY, temp_maven_repo.toURI()
                    .toASCIIString());
            managedService.updated(props);
            ServiceReference<org.ops4j.pax.url.mvn.MavenResolver> mavenResolverService = osgiBundle.getBundleContext()
                    .getServiceReference(org.ops4j.pax.url.mvn.MavenResolver.class);
            paxResolver = osgiBundle.getBundleContext().getService(mavenResolverService);

        } else {
            throw new RuntimeException("Failed to load the service :" + ManagedService.class.getCanonicalName()); //$NON-NLS-1$
        }

    }

    @After
    public void clean() throws IOException {
        FileUtils.deleteDirectory(temp_maven_repo);
    }

    @Test
    public void CheckFindingExistingJar() throws IOException, BundleException {
        Bundle fragmentBundle = osgiBundle.getBundleContext().installBundle(FRAGMENT_WITH_LIB_FOLDER_URL);
        Bundle bundle = osgiBundle.getBundleContext().installBundle(BUNDLE_NO_LIB_FOLDER_URL);
        try {
            bundle.start();// I am not sure to understand why we need to start this bundle.
            // check finding of existing jar in fragment
            assertNotNull(bundle);
            URL jarURL = FileLocator.find(bundle, new Path("lib/any-existing.jar"), null); //$NON-NLS-1$ 
            checkFileExistsOrNot(jarURL, true);
        } finally {
            fragmentBundle.uninstall();
            bundle.uninstall();
        }
        bundle = Platform.getBundle(BUNDLE_NO_LIB);
        assertNull(bundle);
        fragmentBundle = Platform.getBundle(FRAGMENT_WITH_LIB);
        assertNull(fragmentBundle);
    }

    /**
     * DOC sgandon Comment method "checkFileExists".
     * 
     * @param jarURL
     * @param shouldExist
     * @throws IOException
     */
    public void checkFileExistsOrNot(URL jarURL, boolean shouldExist) throws IOException {
        URL fileURL = FileLocator.toFileURL(jarURL);
        File jarFile = new File(fileURL.getFile());
        if (shouldExist) {
            assertTrue("file should be found", jarFile.exists()); //$NON-NLS-1$
        } else {
            assertFalse("file should NOT be found", jarFile.exists()); //$NON-NLS-1$
        }
    }

    @Test
    public void CheckFindingExistingJarUsingFindEntries() throws IOException, BundleException {
        Bundle fragmentBundle = osgiBundle.getBundleContext().installBundle(FRAGMENT_WITH_LIB_FOLDER_URL);
        Bundle bundle = osgiBundle.getBundleContext().installBundle(BUNDLE_NO_LIB_FOLDER_URL);
        try {
            bundle.start();// I am not sure to understand why we need to start this bundle.
            // check finding of existing jar in fragment
            assertNotNull(bundle);
            URL[] jarURL = FileLocator.findEntries(bundle, new Path("lib/any-existing.jar")); //$NON-NLS-1$ 
            assertEquals("There should be only one URL", 1, jarURL.length); //$NON-NLS-1$
            URL fileURL = FileLocator.toFileURL(jarURL[0]);
            File jarFile = new File(fileURL.getFile());
            assertTrue("file should be found", jarFile.exists()); //$NON-NLS-1$
        } finally {
            fragmentBundle.uninstall();
            bundle.uninstall();
        }
        bundle = Platform.getBundle(BUNDLE_NO_LIB);
        assertNull(bundle);
        fragmentBundle = Platform.getBundle(FRAGMENT_WITH_LIB);
        assertNull(fragmentBundle);
    }

    @Test
    public void CheckFindingExistingJarUsingFindEntriesAndMaven() throws IOException, BundleException, URISyntaxException {
        Bundle bundle = osgiBundle.getBundleContext().installBundle(BUNDLE_NO_LIB_FOLDER_URL);
        try {
            URL[] jarURL = FileLocator.findEntries(bundle, new Path("lib/any-existing.jar")); //$NON-NLS-1$ 
            URL fileURL = FileLocator.toFileURL(jarURL[0]);
            assertFalse("File should not exists", new File(fileURL.getFile()).exists()); //$NON-NLS-1$
            copyExistingJarIntoMavenLocalRepo(false);
            jarURL = FileLocator.findEntries(bundle, new Path("lib/any-existing.jar")); //$NON-NLS-1$ 
            fileURL = FileLocator.toFileURL(jarURL[0]);
            assertTrue("File should exists", new File(fileURL.getFile()).exists()); //$NON-NLS-1$
        } finally {
            bundle.uninstall();
        }
        bundle = Platform.getBundle(BUNDLE_NO_LIB);
        assertNull(bundle);
    }

    @Test
    public void CheckFindingExistingJarUsingFindEntriesAndMavenOnlySnapshot() throws IOException, BundleException,
            URISyntaxException {
        Bundle bundle = osgiBundle.getBundleContext().installBundle(BUNDLE_NO_LIB_FOLDER_URL);
        try {
            URL[] jarURL = FileLocator.findEntries(bundle, new Path("lib/any-existing.jar")); //$NON-NLS-1$ 
            URL fileURL = FileLocator.toFileURL(jarURL[0]);
            assertFalse("File should not exists", new File(fileURL.getFile()).exists()); //$NON-NLS-1$
            copyExistingJarIntoMavenLocalRepo(true);
            jarURL = FileLocator.findEntries(bundle, new Path("lib/any-existing.jar")); //$NON-NLS-1$ 
            fileURL = FileLocator.toFileURL(jarURL[0]);
            assertTrue("File should exists", new File(fileURL.getFile()).exists()); //$NON-NLS-1$
        } finally {
            bundle.uninstall();
        }
        bundle = Platform.getBundle(BUNDLE_NO_LIB);
        assertNull(bundle);
    }

    @Test
    public void CheckFindingExistingJarUsingFindEntriesDoesNotTriggerMissingJarObservable() throws IOException, BundleException {
        final Boolean[] observerCalled = new Boolean[1];
        observerCalled[0] = false;
        Observer observer = new Observer() {

            @Override
            public void update(Observable o, Object arg) {
                if (arg != null && arg instanceof JarMissingEvent) {
                    JarMissingEvent jarMissingEvent = (JarMissingEvent) arg;
                    String jarName = jarMissingEvent.getJarName();
                    if (jarName.equals(EXISTING_JAR_NAME)) {
                        observerCalled[0] = true;
                    } else {
                        throw new RuntimeException("Missing jar observer did not receive the expected jar name:" + jarName + "!=" //$NON-NLS-1$
                                + EXISTING_JAR_NAME);
                    }
                } else {// notification is not expected so thrown an exception for the test to fail
                    throw new RuntimeException("Missing jar observer did not receive the proper event :" + arg);
                }
            }
        };

        setupMissingJarLoadingObserver(observer);

        Bundle fragmentBundle = osgiBundle.getBundleContext().installBundle(FRAGMENT_WITH_LIB_FOLDER_URL);
        Bundle bundle = osgiBundle.getBundleContext().installBundle(BUNDLE_NO_LIB_FOLDER_URL);
        try {
            bundle.start();// I am not sure to understand why we need to start this bundle.
            // check finding of existing jar in fragment
            assertNotNull(bundle);
            FileLocator.find(bundle, new Path("lib/any-existing.jar"), null); //$NON-NLS-1$ 
            assertFalse("Observer should never be called", observerCalled[0]); //$NON-NLS-1$
        } finally {
            fragmentBundle.uninstall();
            bundle.uninstall();
            unsetupMissingJarLoadingObserver(observer);
        }
        bundle = Platform.getBundle(BUNDLE_NO_LIB);
        assertNull(bundle);
        fragmentBundle = Platform.getBundle(FRAGMENT_WITH_LIB);
        assertNull(fragmentBundle);
    }

    @Test
    public void CheckTriggerMissingJarObservableWithProperGAV() throws IOException, BundleException {
        final Boolean[] observerCalled = new Boolean[1];
        observerCalled[0] = false;
        Observer observer = new Observer() {

            @Override
            public void update(Observable o, Object arg) {
                if (arg != null && arg instanceof JarMissingEvent) {
                    JarMissingEvent jarMissingEvent = (JarMissingEvent) arg;
                    String expectedMvnUriStr = "mvn:org.talend.libraries/bar/6.0.0";
                    try {
                        if (!jarMissingEvent.getMvnUri().toASCIIString().equals("mvn:org.talend.studio/any-existing/1.0.0")) {

                            if (jarMissingEvent.getMvnUri().toASCIIString().equals(expectedMvnUriStr)) {
                                observerCalled[0] = true;
                            } else {
                                throw new RuntimeException(
                                        "Missing jar observer did not receive the expected maven uri " + jarMissingEvent.getMvnUri().toASCIIString() + " != " //$NON-NLS-1$ //$NON-NLS-2$
                                                + expectedMvnUriStr);
                            }
                        }// else ignor this bundle
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                } else {// notification is not expected so thrown an exception for the test to fail
                    throw new RuntimeException("Missing jar observer did not receive the proper event :" + arg);
                }
            }
        };

        setupMissingJarLoadingObserver(observer);

        Bundle bundleNoJar = osgiBundle.getBundleContext().installBundle(BUNDLE_NO_LIB_FOLDER_URL);
        try {
            bundleNoJar.getResource("foo");
            assertTrue("Observer should have been called", observerCalled[0]); //$NON-NLS-1$
        } finally {
            bundleNoJar.uninstall();
            unsetupMissingJarLoadingObserver(observer);
        }
        bundleNoJar = Platform.getBundle(BUNDLE_NO_LIB);
        assertNull(bundleNoJar);
    }

    @Test
    public void CheckAccessinClassInFragmentLib() throws IOException, BundleException, ClassNotFoundException,
            InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException,
            InvocationTargetException {
        Bundle fragmentBundle = osgiBundle.getBundleContext().installBundle(FRAGMENT_WITH_LIB_FOLDER_URL);
        Bundle bundleNoJar = osgiBundle.getBundleContext().installBundle(BUNDLE_NO_LIB_FOLDER_URL);
        Bundle bundle = osgiBundle.getBundleContext().installBundle(BUNDLE_CALLING_LIB_FOLDER_URL);
        try {
            callBundleMethodToCallLibApi(bundle);
            // no assert here because the above should fail in case of error.
        } finally {
            bundle.uninstall();
            bundleNoJar.uninstall();
            fragmentBundle.uninstall();
        }
        bundle = Platform.getBundle(BUNDLE_CALLING_LIB);
        assertNull(bundle);
        bundleNoJar = Platform.getBundle(BUNDLE_NO_LIB);
        assertNull(bundleNoJar);
        fragmentBundle = Platform.getBundle(FRAGMENT_WITH_LIB);
        assertNull(fragmentBundle);
    }

    @Test
    public void CheckAccessinClassInFragmentLibAndNoNotificationExpected() throws IOException, BundleException,
            ClassNotFoundException, InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException,
            IllegalArgumentException, InvocationTargetException {
        Bundle fragmentBundle = osgiBundle.getBundleContext().installBundle(FRAGMENT_WITH_LIB_FOLDER_URL);
        Bundle bundleNoJar = osgiBundle.getBundleContext().installBundle(BUNDLE_NO_LIB_FOLDER_URL);
        Bundle bundle = osgiBundle.getBundleContext().installBundle(BUNDLE_CALLING_LIB_FOLDER_URL);
        final Boolean[] observerCalled = new Boolean[1];
        observerCalled[0] = false;
        Observer observer = new Observer() {

            @Override
            public void update(Observable o, Object arg) {
                if (arg != null && arg instanceof JarMissingEvent) {
                    JarMissingEvent jarMissingEvent = (JarMissingEvent) arg;
                    try {
                        if (jarMissingEvent.getMvnUri().toASCIIString().equals("mvn:org.talend.studio/any-existing/1.0.0")) {
                            observerCalled[0] = true;
                        }// else ignor this bundle
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                } else {// notification is not expected so thrown an exception for the test to fail
                    throw new RuntimeException("Missing jar observer did not receive the proper event :" + arg);
                }
            }
        };
        setupMissingJarLoadingObserver(observer);

        try {
            callBundleMethodToCallLibApi(bundle);
            assertFalse("Objserver should never be called", observerCalled[0]);
        } finally {
            bundle.uninstall();
            bundleNoJar.uninstall();
            fragmentBundle.uninstall();
        }
        bundle = Platform.getBundle(BUNDLE_CALLING_LIB);
        assertNull(bundle);
        bundleNoJar = Platform.getBundle(BUNDLE_NO_LIB);
        assertNull(bundleNoJar);
        fragmentBundle = Platform.getBundle(FRAGMENT_WITH_LIB);
        assertNull(fragmentBundle);
    }

    @Test()
    public void CheckFailAccessinClassInFragmentLib() throws IOException, BundleException, SecurityException,
            IllegalArgumentException, ClassNotFoundException, InstantiationException, IllegalAccessException,
            NoSuchMethodException, InvocationTargetException {
        Bundle bundleNoJar = osgiBundle.getBundleContext().installBundle(BUNDLE_NO_LIB_FOLDER_URL);
        Bundle bundle = osgiBundle.getBundleContext().installBundle(BUNDLE_CALLING_LIB_FOLDER_URL);
        try {
            // this should fail if the Activator fails to load the class from the fragment lib
            try {
                // this forces the bundle classloader to be initialised
                bundleNoJar.getResource("foo"); //$NON-NLS-1$
                callBundleMethodToCallLibApi(bundle);
                fail("starting bundle should have thrown an exception."); //$NON-NLS-1$
            } catch (InvocationTargetException be) {
                if (!(be.getCause() instanceof NoClassDefFoundError)) {
                    throw be;
                }// else do nothing cause this is an expected exception
            }
            // no assert here because the above should fail in case of error.
        } finally {
            bundle.uninstall();
            bundleNoJar.uninstall();
        }
        bundle = Platform.getBundle(BUNDLE_CALLING_LIB);
        assertNull(bundle);
        bundleNoJar = Platform.getBundle(BUNDLE_NO_LIB);
        assertNull(bundleNoJar);
    }

    @Test
    public void CheckNoExistingJarClassLoadGeneratesMissingJarObservableCallButNoJarReturned() throws IOException,
            BundleException, SecurityException, IllegalArgumentException, ClassNotFoundException, InstantiationException,
            IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final Boolean[] observerCalled = new Boolean[1];
        observerCalled[0] = false;
        Observer observer = new Observer() {

            @Override
            public void update(Observable o, Object arg) {
                observerCalled[0] = true;
            }
        };
        setupMissingJarLoadingObserver(observer);
        Bundle bundleNoJar = osgiBundle.getBundleContext().installBundle(BUNDLE_NO_LIB_FOLDER_URL);
        Bundle bundle = osgiBundle.getBundleContext().installBundle(BUNDLE_CALLING_LIB_FOLDER_URL);
        try {
            try {
                // this forces the bundle classloader to be initialised
                bundleNoJar.getResource("foo"); //$NON-NLS-1$
                callBundleMethodToCallLibApi(bundle);
                fail("Bundle start should have thrown an exception cause no lib is provided"); //$NON-NLS-1$
            } catch (InvocationTargetException be) {
                if (!(be.getCause() instanceof NoClassDefFoundError)) {
                    throw be;
                }// else do nothing cause this is an expected exception
            }
            // check finding of existing jar in fragment
            assertTrue(observerCalled[0]);

        } finally {
            bundle.uninstall();
            bundleNoJar.uninstall();
            unsetupMissingJarLoadingObserver(observer);
        }
        bundle = Platform.getBundle(BUNDLE_CALLING_LIB);
        assertNull(bundle);
        bundleNoJar = Platform.getBundle(BUNDLE_NO_LIB);
        assertNull(bundleNoJar);
    }

    @Test
    public void CheckNoExistingJarClassLoadGeneratesMissingJarObservableCallAndJarReturned() throws IOException, BundleException,
            SecurityException, IllegalArgumentException, ClassNotFoundException, InstantiationException, IllegalAccessException,
            NoSuchMethodException, InvocationTargetException {
        final Boolean[] observerCalled = new Boolean[1];
        observerCalled[0] = false;
        Observer observer = new MissingJarObserverWithFolderResolution(observerCalled, false);
        setupMissingJarLoadingObserver(observer);
        Bundle bundleNoJar = osgiBundle.getBundleContext().installBundle(BUNDLE_NO_LIB_FOLDER_URL);
        Bundle bundle = osgiBundle.getBundleContext().installBundle(BUNDLE_CALLING_LIB_FOLDER_URL);
        try {
            // this forces the bundle classloader to be initialised
            bundleNoJar.getResource("foo"); //$NON-NLS-1$
            callBundleMethodToCallLibApi(bundle);
            assertTrue(observerCalled[0]);

        } finally {
            bundle.uninstall();
            bundleNoJar.uninstall();
            unsetupMissingJarLoadingObserver(observer);
        }
        bundle = Platform.getBundle(BUNDLE_CALLING_LIB);
        assertNull(bundle);
        bundleNoJar = Platform.getBundle(BUNDLE_NO_LIB);
        assertNull(bundleNoJar);
    }

    @Test
    public void CheckNoExistingJarClassLoadGeneratesMissingJarObservableCallAndJarReturnedMaven() throws IOException,
            BundleException, SecurityException, IllegalArgumentException, ClassNotFoundException, InstantiationException,
            IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final Boolean[] observerCalled = new Boolean[1];
        observerCalled[0] = false;
        Observer observer = new MissingJarObserverWithMavenResolution(observerCalled, false);
        setupMissingJarLoadingObserver(observer);
        Bundle bundleNoJar = osgiBundle.getBundleContext().installBundle(BUNDLE_NO_LIB_FOLDER_URL);
        Bundle bundle = osgiBundle.getBundleContext().installBundle(BUNDLE_CALLING_LIB_FOLDER_URL);
        try {
            // this forces the bundle classloader to be initialised
            bundleNoJar.getResource("foo"); //$NON-NLS-1$
            callBundleMethodToCallLibApi(bundle);
            assertTrue(observerCalled[0]);

        } finally {
            bundle.uninstall();
            bundleNoJar.uninstall();
            unsetupMissingJarLoadingObserver(observer);
        }
        bundle = Platform.getBundle(BUNDLE_CALLING_LIB);
        assertNull(bundle);
        bundleNoJar = Platform.getBundle(BUNDLE_NO_LIB);
        assertNull(bundleNoJar);
    }

    @Test
    public void CheckNoExistingJarClassLoadGeneratesMissingJarObservableCallAndJarReturnedLater() throws IOException,
            BundleException, ClassNotFoundException, InstantiationException, IllegalAccessException, SecurityException,
            NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        final Boolean[] observerCalled = new Boolean[1];
        observerCalled[0] = false;
        Observer observer = new MissingJarObserverWithFolderResolution(observerCalled, true);
        Bundle bundleNoJar = osgiBundle.getBundleContext().installBundle(BUNDLE_NO_LIB_FOLDER_URL);
        Bundle bundle = osgiBundle.getBundleContext().installBundle(BUNDLE_CALLING_LIB_FOLDER_URL);
        try {
            // this forces the bundle classloader to be initialised
            bundleNoJar.getResource("foo"); //$NON-NLS-1$
            URL jarURL = FileLocator.find(bundle, new Path("lib/any-existing.jar"), null); //$NON-NLS-1$
            checkFileExistsOrNot(jarURL, false);

            setupMissingJarLoadingObserver(observer);
            callBundleMethodToCallLibApi(bundle);
            assertTrue(observerCalled[0]);

        } finally {
            bundle.uninstall();
            bundleNoJar.uninstall();
            unsetupMissingJarLoadingObserver(observer);
        }
        bundle = Platform.getBundle(BUNDLE_CALLING_LIB);
        assertNull(bundle);
        bundleNoJar = Platform.getBundle(BUNDLE_NO_LIB);
        assertNull(bundleNoJar);
    }

    @Test
    public void CheckNoExistingJarClassLoadGeneratesMissingJarObservableCallAndJarReturnedLaterMaven() throws IOException,
            BundleException, ClassNotFoundException, InstantiationException, IllegalAccessException, SecurityException,
            NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        final Boolean[] observerCalled = new Boolean[1];
        observerCalled[0] = false;
        Observer observer = new MissingJarObserverWithMavenResolution(observerCalled, true);
        Bundle bundleNoJar = osgiBundle.getBundleContext().installBundle(BUNDLE_NO_LIB_FOLDER_URL);
        Bundle bundle = osgiBundle.getBundleContext().installBundle(BUNDLE_CALLING_LIB_FOLDER_URL);
        try {
            // this forces the bundle classloader to be initialised
            bundleNoJar.getResource("foo"); //$NON-NLS-1$
            URL jarURL = FileLocator.find(bundle, new Path("lib/any-existing.jar"), null); //$NON-NLS-1$
            checkFileExistsOrNot(jarURL, false);

            setupMissingJarLoadingObserver(observer);
            callBundleMethodToCallLibApi(bundle);
            assertTrue(observerCalled[0]);

        } finally {
            bundle.uninstall();
            bundleNoJar.uninstall();
            unsetupMissingJarLoadingObserver(observer);
        }
        bundle = Platform.getBundle(BUNDLE_CALLING_LIB);
        assertNull(bundle);
        bundleNoJar = Platform.getBundle(BUNDLE_NO_LIB);
        assertNull(bundleNoJar);
    }

    @Test
    public void CheckNoExistingJarClassLoadGeneratesMissingJarObservableCallAndJarReturnedLaterAfter2Find() throws IOException,
            BundleException, ClassNotFoundException, InstantiationException, IllegalAccessException, SecurityException,
            NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        final Boolean[] observerCalled = new Boolean[1];
        observerCalled[0] = false;
        Observer observer = new MissingJarObserverWithFolderResolution(observerCalled, true);
        Bundle bundleNoJar = osgiBundle.getBundleContext().installBundle(BUNDLE_NO_LIB_FOLDER_URL);
        Bundle bundle = osgiBundle.getBundleContext().installBundle(BUNDLE_CALLING_LIB_FOLDER_URL);
        try {
            // this forces the bundle classloader to be initialised
            bundleNoJar.getResource("foo"); //$NON-NLS-1$
            URL jarURL = FileLocator.find(bundleNoJar, new Path("lib/any-existing.jar"), null); //$NON-NLS-1$
            checkFileExistsOrNot(jarURL, false);
            jarURL = FileLocator.find(bundleNoJar, new Path("lib/any-existing.jar"), null); //$NON-NLS-1$
            checkFileExistsOrNot(jarURL, false);

            setupMissingJarLoadingObserver(observer);
            callBundleMethodToCallLibApi(bundle);
            assertTrue(observerCalled[0]);

        } finally {
            bundle.uninstall();
            bundleNoJar.uninstall();
            unsetupMissingJarLoadingObserver(observer);
        }
        bundle = Platform.getBundle(BUNDLE_CALLING_LIB);
        assertNull(bundle);
        bundleNoJar = Platform.getBundle(BUNDLE_NO_LIB);
        assertNull(bundleNoJar);
    }

    @Test
    public void CheckNoExistingJarClassLoadGeneratesMissingJarObservableCallAndJarReturnedLaterAfter2FindMaven()
            throws IOException, BundleException, ClassNotFoundException, InstantiationException, IllegalAccessException,
            SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        final Boolean[] observerCalled = new Boolean[1];
        observerCalled[0] = false;
        Observer observer = new MissingJarObserverWithMavenResolution(observerCalled, true);
        Bundle bundleNoJar = osgiBundle.getBundleContext().installBundle(BUNDLE_NO_LIB_FOLDER_URL);
        Bundle bundle = osgiBundle.getBundleContext().installBundle(BUNDLE_CALLING_LIB_FOLDER_URL);
        try {
            // this forces the bundle classloader to be initialised
            bundleNoJar.getResource("foo"); //$NON-NLS-1$
            URL jarURL = FileLocator.find(bundleNoJar, new Path("lib/any-existing.jar"), null); //$NON-NLS-1$
            checkFileExistsOrNot(jarURL, false);
            jarURL = FileLocator.find(bundleNoJar, new Path("lib/any-existing.jar"), null); //$NON-NLS-1$
            checkFileExistsOrNot(jarURL, false);

            setupMissingJarLoadingObserver(observer);
            callBundleMethodToCallLibApi(bundle);
            assertTrue(observerCalled[0]);

        } finally {
            bundle.uninstall();
            bundleNoJar.uninstall();
            unsetupMissingJarLoadingObserver(observer);
        }
        bundle = Platform.getBundle(BUNDLE_CALLING_LIB);
        assertNull(bundle);
        bundleNoJar = Platform.getBundle(BUNDLE_NO_LIB);
        assertNull(bundleNoJar);
    }

    @Test
    public void CheckFindMissingJarObservableCallAndJarReturnedLaterAfter() throws IOException, BundleException,
            ClassNotFoundException, InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException,
            IllegalArgumentException, InvocationTargetException {
        final Boolean[] observerCalled = new Boolean[1];
        observerCalled[0] = false;
        Observer observer = new MissingJarObserverWithFolderResolution(observerCalled, true);
        Bundle bundleNoJar = osgiBundle.getBundleContext().installBundle(BUNDLE_NO_LIB_FOLDER_URL);
        Bundle bundle = osgiBundle.getBundleContext().installBundle(BUNDLE_CALLING_LIB_FOLDER_URL);
        try {
            // this forces the bundle classloader to be initialised
            bundleNoJar.getResource("foo"); //$NON-NLS-1$
            URL jarURL = FileLocator.find(bundleNoJar, new Path("lib/any-existing.jar"), null); //$NON-NLS-1$
            checkFileExistsOrNot(jarURL, false);
            setupMissingJarLoadingObserver(observer);
            jarURL = FileLocator.find(bundleNoJar, new Path("lib/any-existing.jar"), null); //$NON-NLS-1$
            checkFileExistsOrNot(jarURL, true);

            assertTrue(observerCalled[0]);

        } finally {
            bundle.uninstall();
            bundleNoJar.uninstall();
            unsetupMissingJarLoadingObserver(observer);
        }
        bundle = Platform.getBundle(BUNDLE_CALLING_LIB);
        assertNull(bundle);
        bundleNoJar = Platform.getBundle(BUNDLE_NO_LIB);
        assertNull(bundleNoJar);
    }

    @Test
    public void CheckFindMissingJarObservableCallAndJarReturnedLaterAfterMaven() throws IOException, BundleException,
            ClassNotFoundException, InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException,
            IllegalArgumentException, InvocationTargetException {
        final Boolean[] observerCalled = new Boolean[1];
        observerCalled[0] = false;
        Observer observer = new MissingJarObserverWithMavenResolution(observerCalled, true);
        Bundle bundleNoJar = osgiBundle.getBundleContext().installBundle(BUNDLE_NO_LIB_FOLDER_URL);
        Bundle bundle = osgiBundle.getBundleContext().installBundle(BUNDLE_CALLING_LIB_FOLDER_URL);
        try {
            // this forces the bundle classloader to be initialised
            bundleNoJar.getResource("foo"); //$NON-NLS-1$
            URL jarURL = FileLocator.find(bundleNoJar, new Path("lib/any-existing.jar"), null); //$NON-NLS-1$
            checkFileExistsOrNot(jarURL, false);
            setupMissingJarLoadingObserver(observer);
            jarURL = FileLocator.find(bundleNoJar, new Path("lib/any-existing.jar"), null); //$NON-NLS-1$
            checkFileExistsOrNot(jarURL, true);

            assertTrue(observerCalled[0]);

        } finally {
            bundle.uninstall();
            bundleNoJar.uninstall();
            unsetupMissingJarLoadingObserver(observer);
        }
        bundle = Platform.getBundle(BUNDLE_CALLING_LIB);
        assertNull(bundle);
        bundleNoJar = Platform.getBundle(BUNDLE_NO_LIB);
        assertNull(bundleNoJar);
    }

    @Test
    public void CheckInvokeMissingJarMethodAfterFindJarFailed() throws IOException, BundleException, ClassNotFoundException,
            InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException,
            InvocationTargetException {
        final Boolean[] observerCalled = new Boolean[1];
        observerCalled[0] = false;
        Observer observer = new MissingJarObserverWithFolderResolution(observerCalled, true);
        Bundle bundleNoJar = osgiBundle.getBundleContext().installBundle(BUNDLE_NO_LIB_FOLDER_URL);
        Bundle bundle = osgiBundle.getBundleContext().installBundle(BUNDLE_CALLING_LIB_FOLDER_URL);
        try {
            // this forces the bundle classloader to be initialised
            bundleNoJar.getResource("foo"); //$NON-NLS-1$
            URL jarURL = FileLocator.find(bundleNoJar, new Path("lib/any-existing.jar"), null); //$NON-NLS-1$
            checkFileExistsOrNot(jarURL, false);
            setupMissingJarLoadingObserver(observer);
            callBundleMethodToCallLibApi(bundle);
            assertTrue(observerCalled[0]);

        } finally {
            bundle.uninstall();
            bundleNoJar.uninstall();
            unsetupMissingJarLoadingObserver(observer);
        }
        bundle = Platform.getBundle(BUNDLE_CALLING_LIB);
        assertNull(bundle);
        bundleNoJar = Platform.getBundle(BUNDLE_NO_LIB);
        assertNull(bundleNoJar);
    }

    @Test
    public void CheckInvokeMissingJarMethodAfterFindJarFailedMaven() throws IOException, BundleException, ClassNotFoundException,
            InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException,
            InvocationTargetException {
        final Boolean[] observerCalled = new Boolean[1];
        observerCalled[0] = false;
        Observer observer = new MissingJarObserverWithMavenResolution(observerCalled, true);
        Bundle bundleNoJar = osgiBundle.getBundleContext().installBundle(BUNDLE_NO_LIB_FOLDER_URL);
        Bundle bundle = osgiBundle.getBundleContext().installBundle(BUNDLE_CALLING_LIB_FOLDER_URL);
        try {
            // this forces the bundle classloader to be initialised
            bundleNoJar.getResource("foo"); //$NON-NLS-1$
            URL jarURL = FileLocator.find(bundleNoJar, new Path("lib/any-existing.jar"), null); //$NON-NLS-1$
            checkFileExistsOrNot(jarURL, false);
            setupMissingJarLoadingObserver(observer);
            callBundleMethodToCallLibApi(bundle);
            assertTrue(observerCalled[0]);

        } finally {
            bundle.uninstall();
            bundleNoJar.uninstall();
            unsetupMissingJarLoadingObserver(observer);
        }
        bundle = Platform.getBundle(BUNDLE_CALLING_LIB);
        assertNull(bundle);
        bundleNoJar = Platform.getBundle(BUNDLE_NO_LIB);
        assertNull(bundleNoJar);
    }

    @Test
    public void testAddSnapshotToMvnURI() throws URISyntaxException {
        assertEquals(new URI("mvn:foo/bar"), URIUtil.addSnapshotToUri(new URI("mvn:foo/bar")));
        assertEquals(new URI("mvn:foo/bar/LATEST"), URIUtil.addSnapshotToUri(new URI("mvn:foo/bar/LATEST")));
        assertEquals(new URI("mvn:foo/bar/12-SNAPSHOT"), URIUtil.addSnapshotToUri(new URI("mvn:foo/bar/12-SNAPSHOT")));
        assertEquals(new URI("mvn:foo/bar/12-SNAPSHOT"), URIUtil.addSnapshotToUri(new URI("mvn:foo/bar/12")));
        assertEquals(new URI("mvn:foo/bar/12-SNAPSHOT/jar"), URIUtil.addSnapshotToUri(new URI("mvn:foo/bar/12/jar")));
        assertEquals(new URI("mvn:http://!foo/bar/12-SNAPSHOT/jar"),
                URIUtil.addSnapshotToUri(new URI("mvn:http://!foo/bar/12/jar")));
    }

    /**
     * DOC sgandon Comment method "unsetupMissingJarLoadingObserver".
     * 
     * @param observer
     */
    private void unsetupMissingJarLoadingObserver(Observer observer) {
        BundleContext bundleContext = osgiBundle.getBundleContext();
        @SuppressWarnings("unchecked")
        ServiceReference<JarMissingObservable> serviceReference = (ServiceReference<JarMissingObservable>) bundleContext
                .getServiceReference(JarMissingObservable.class.getCanonicalName());
        JarMissingObservable missingJarObservable = bundleContext.getService(serviceReference);
        missingJarObservable.deleteObserver(observer);
    }

    /**
     * looks for the OSGI service that notify that a jar is missing when loading a bundle and register this as a
     * listener
     */
    @SuppressWarnings("restriction")
    private void setupMissingJarLoadingObserver(Observer observer) {
        BundleContext bundleContext = osgiBundle.getBundleContext();
        @SuppressWarnings("unchecked")
        ServiceReference<JarMissingObservable> serviceReference = (ServiceReference<JarMissingObservable>) bundleContext
                .getServiceReference(JarMissingObservable.class.getCanonicalName());
        JarMissingObservable missingJarObservable = bundleContext.getService(serviceReference);
        missingJarObservable.addObserver(observer);
    }

    /**
     * DOC sgandon Comment method "callBundleMethodToCallLibApi".
     * 
     * @param bundle
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    private void callBundleMethodToCallLibApi(Bundle bundle) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        Class<?> activatorClass = bundle.loadClass("tst.call.plugin.lib.Activator"); //$NON-NLS-1$
        Object newInstance = activatorClass.newInstance();
        Method startMethod = activatorClass.getMethod("libCall"); //$NON-NLS-1$
        startMethod.invoke(newInstance);
    }

    /**
     * DOC sgandon Comment method "copyExistingJarIntoLibJavaFolder".
     * 
     * @param jarName
     * @throws URISyntaxException
     */
    private void copyExistingJarIntoLibJavaFolder(String jarName) throws URISyntaxException {
        File sourceFile = new File(new URI(FRAGMENT_WITH_LIB_FOLDER_URL + "lib/any-existing.jar")); //$NON-NLS-1$
        File desctinationFile = new File(libJavaFolderFile, jarName);
        try {
            FileUtils.copyFile(sourceFile, desctinationFile);
        } catch (IOException e) {
            throw new RuntimeException("failed to copy jar", e); //$NON-NLS-1$
        }
    }

    /**
     * DOC sgandon Comment method "copyExistingJarIntoLibJavaFolder".
     * 
     * @param isSnapshot
     * 
     * @param jarName
     * @throws URISyntaxException
     * @throws IOException
     */
    private void copyExistingJarIntoMavenLocalRepo(boolean isSnapshot) throws URISyntaxException, IOException {
        File sourceFile = new File(new URI(FRAGMENT_WITH_LIB_FOLDER_URL + "lib/any-existing.jar")); //$NON-NLS-1$
        paxResolver.upload("org.talend.studio", "any-existing", null, "jar", isSnapshot ? "1.0.0-SNAPSHOT" : "1.0.0", sourceFile);
    }

}
