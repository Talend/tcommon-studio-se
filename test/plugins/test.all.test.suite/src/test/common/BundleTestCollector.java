/*******************************************************************************
 * Copyright (c) 2008 Syntax Consulting, Inc. All rights reserved.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package test.common;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.internal.registry.osgi.Activator;
import org.eclipse.core.runtime.IBundleGroup;
import org.eclipse.core.runtime.IBundleGroupProvider;
import org.eclipse.core.runtime.Platform;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This class allows you harvest unit tests from resolved bundles based on filters you supply. It can harvest tests from
 * both bundles and fragments, and can also be used during automated builds using the Eclipse Testing Framework.
 * <p>
 * This class is similar to the JUnit TestCollector class, except that it takes responsibility for both loading the
 * classes and adding them to the test suite. The collector must load the classes using the appropriate bundle
 * classloader for each test, so this work cannot be done in the suite.
 * <p>
 * To use this collector, simply create a JUnit test suite with a method like this:
 * <p>
 *
 * <pre>
 *
 * public static Test suite() {
 * 
 *     TestSuite suite = new TestSuite(&quot;All Tests&quot;);
 * 
 *     PluginTestCollector testCollector = new PluginTestCollector();
 *     testCollector.collectTests(suite, &quot;com.rcpquickstart.&quot;, &quot;com.rcpquickstart.mypackage.&quot;, &quot;*Test&quot;);
 * 
 *     return suite;
 * 
 * }
 * </pre>
 *
 * <p>
 * Note that because JUnit 4 implements suites through annotations, a similar mechanism cannot be used. If anyone has
 * ideas concerning how this could be made to work using JUnit 4, please let me know. Comments and suggestions can be
 * sent to patrick@rcpquickstart.com.
 *
 * @author Patrick Paulin
 */
public class BundleTestCollector {

    /**
     *
     */
    private static final String RECORD_NON_TEST_CLASSES_PROP = "talend.record.non.test.classes";

    // this static flag is used when some test classes fail to load
    // a special test case class is loaded to report this error but no to stop
    // other
    // test case to run. This flag serve the prupose of refering to this class
    // only once
    static boolean isErrorMessageTestCaseAlreadyReferenced = false;

    static private Class<?> errorMessageTestClass;

    /**
     * Create a list of test classes for the bundles currently resolved by the framework. This method works with JUnit
     * 3.x test cases only, meaning that it searches for classes that subclass the TestCase class.
     *
     * @param suite to which tests should be added
     * @param bundleRoot root string that a bundle id needs to start with in order for the bundle to be included in the
     * search
     * @param packageRoot root string that a package needs to start with in order for the package to be included in the
     * search, this value is compulsary, you must specify a package prefix.
     * @param testClassFilter filter string that will be used to search for test cases. The filter applies to the
     * unqualified class name only (not including the package name). Wildcards are allowed, as defined by the
     * {@link Activator Bundle#findEntries(String, String, boolean)} method.
     * @return list of test classes that match the roots and filter passed in
     */
    public void collectJUnit3Tests(TestSuite suite, String bundleRoot, String packageRoot, String testClassFilter) {
        Bundle currentBundle = FrameworkUtil.getBundle(this.getClass());
        // no check for null is done cause this should create an exception if
        // someone change the name of this bundle
        for (Bundle bundle : currentBundle.getBundleContext().getBundles()) {
            if (!isFragment(bundle) && bundle.getSymbolicName().startsWith(bundleRoot)) {
                List<Class<?>> testClasses = getTestClasesInBundle(bundle, packageRoot, testClassFilter);

                for (Class clazz : testClasses) {
                    suite.addTestSuite(clazz);
                }
            }
        }
    }

    /**
     * Create a list of test classes for the bundles currently resolved by the framework.
     *
     * @param bundlePrefix root string that a bundle id needs to start with in order for the bundle to be included in
     * the search, may be null or empty
     * @param bundleSuffix the suffix of the bundle or null if no suffix is needed, ay be null or empty
     * @param packagePrefixs comma separated list of package prefix, may be null or empty
     * @param testClassFilter filter string that will be used to search for test cases. The filter applies to the
     * unqualified class name only (not including the package name). Wildcards are allowed, as defined by the
     * {@link Activator Bundle#findEntries(String, String, boolean)} method.
     * @return
     * @return list of test classes that match the roots and filter passed in
     */
    public Class<?>[] collectTestsClasses(String bundlePrefix, String bundleSuffix, String packagePrefixs,
            String testClassFilter, boolean useFragmentOnly) {
        List<Class<?>> allTestClasses = new ArrayList<Class<?>>();
        Bundle currentBundle = FrameworkUtil.getBundle(this.getClass());
        // no check for null is done cause this should create an exception if
        // someone change the name of this bundle
        collectClassesFromBundles(bundlePrefix, bundleSuffix, packagePrefixs, testClassFilter, useFragmentOnly,
                allTestClasses, currentBundle.getBundleContext().getBundles());
        return allTestClasses.toArray(new Class[allTestClasses.size()]);
    }

    /**
     * DOC sgandon Comment method "collectClassesFromBundles".
     *
     * @param bundlePrefix root string that a bundle id needs to start with in order for the bundle to be included in
     * the search, may be null or empty
     * @param bundleSuffix the suffix of the bundle or null if no suffix is needed, ay be null or empty
     * @param packagePrefixs comma separated list of package prefix, may be null or empty
     * @param testClassFilter filter string that will be used to search for test cases. The filter applies to the
     * unqualified class name only (not including the package name). Wildcards are allowed, as defined by the
     * {@link Activator Bundle#findEntries(String, String, boolean)} method.
     * @param allTestClasses , the list to be filled with the found classes
     * @param bundles all the bundles to find the classes in.
     */
    private void collectClassesFromBundles(String bundlePrefix, String bundleSuffix, String packagePrefixs,
            String testClassFilter, boolean useFragmentOnly, List<Class<?>> allTestClasses, Bundle[] bundles) {
        String[] allBundlePrefixes = bundlePrefix != null ? bundlePrefix.split(",") : new String[0];
        String[] allBundleSuffixes = bundleSuffix != null ? bundleSuffix.split(",") : new String[0];
        for (Bundle bundle : bundles) {
            // @formatter:off
            if ((!useFragmentOnly || isFragment(bundle))
                    && (bundlePrefix == null || bundlePrefix.equals("") || isPrefix(bundle.getSymbolicName(), allBundlePrefixes))
                    && (bundleSuffix == null || bundleSuffix.equals("")
                            || isSuffix(bundle.getSymbolicName(), allBundleSuffixes))) {
                // @formatter:on
                List<Class<?>> testClasses = getTestClasesInBundle(bundle, packagePrefixs, testClassFilter);
                allTestClasses.addAll(testClasses);
            }
        }
    }

    /**
     * Create a list of test classes for the bundles currently resolved by the framework.
     *
     * @param featureNames coma separateed list of features Id or null or empty
     * @param bundlePrefix root string that a bundle id needs to start with in order for the bundle to be included in
     * the search, may be null or empty
     * @param bundleSuffix the suffix of the bundle or null if no suffix is needed, ay be null or empty
     * @param packagePrefixs comma separated list of package prefix, may be null or empty
     * @param testClassFilter filter string that will be used to search for test cases. The filter applies to the
     * unqualified class name only (not including the package name). Wildcards are allowed, as defined by the
     * {@link Activator Bundle#findEntries(String, String, boolean)} method.
     * @return
     * @return list of test classes that match the roots and filter passed in
     */
    public Class<?>[] collectTestsClasses(String featureNames, String bundlePrefix, String bundleSuffix,
            String packagePrefixs, String testClassFilter, boolean useFragmentOnly) {
        if (featureNames != null && !"".equals(featureNames)) {
            return collectTestsClassesFromFeature(featureNames, bundlePrefix, bundleSuffix, packagePrefixs,
                    testClassFilter, useFragmentOnly);
        } else {
            return collectTestsClasses(bundlePrefix, bundleSuffix, packagePrefixs, testClassFilter, useFragmentOnly);
        }
    }

    @SuppressWarnings("unused")
    public Class<?>[] collectTestsClassesFromFeature(String featureNames, String bundlePrefix, String bundleSuffix,
            String packagePrefixs, String testClassFilter, boolean useFragmentOnly) {
        List<Class<?>> allTestClasses = new ArrayList<Class<?>>();
        String[] allFeatures = featureNames != null ? featureNames.split(",") : new String[0];
        // sort the array to search in it
        Arrays.sort(allFeatures);
        IBundleGroupProvider[] bundleGroupProviders = Platform.getBundleGroupProviders();
        for (IBundleGroupProvider bundleProv : bundleGroupProviders) {
            IBundleGroup[] bundleGroups = bundleProv.getBundleGroups();
            for (IBundleGroup bundleGroup : bundleGroups) {
                if (Arrays.binarySearch(allFeatures, bundleGroup.getIdentifier()) >= 0) {
                    System.out.println("Looking for bundle in feature : " + bundleGroup.getIdentifier());
                    collectClassesFromBundles(bundlePrefix, bundleSuffix, packagePrefixs, testClassFilter,
                            useFragmentOnly, allTestClasses, bundleGroup.getBundles());
                }
            }
        }
        return allTestClasses.toArray(new Class[allTestClasses.size()]);
    }

    // @Test
    // public void collectTestsClassesFromFeatureTest() {
    // collectTestsClassesFromFeature("org.eclipse.rcp", null, null, null, "*", false);
    // }

    private List<Class<?>> getTestClasesInBundle(Bundle bundle, String packagePrefix, String testClassFilter) {
        List<Class<?>> testClassesInBundle = new ArrayList<Class<?>>();
        Bundle bundleHost = bundle;
        if (isFragment(bundle)) {// load the host bundle if it is a fragment
            String hostName = bundle.getHeaders().get("Fragment-Host"); //$NON-NLS-1$
            // should never be null
            bundleHost = Platform.getBundle(hostName.split(";")[0]);
            if (bundleHost == null) {
                // use the store mechanisme to report error is
                Class<?> messageTestCase = createCollectionErrorMessageTestCase(
                        "Failed to find host bundle named[" + hostName.split(";")[0] + "].");
                testClassesInBundle.add(messageTestCase);
                return testClassesInBundle;
            } // else keep going everything is fine
        }
        String[] allPrefixes = packagePrefix != null ? packagePrefix.split(",") : new String[0];
        if (bundle != null) {
            Enumeration testClassNames = bundle.findEntries("/", testClassFilter + ".class", true); //$NON-NLS-1$
            try {
                String outputSubdir = getIdeOutputSubDir(bundle);
                if (testClassNames != null) {
                    while (testClassNames.hasMoreElements()) {

                        /*
                         * Take relative path produced by findEntries method and convert it into a properly formatted
                         * class name. The package root is used to determine the start of the qualified class name in
                         * the path.
                         */
                        String testClassPath = ((URL) testClassNames.nextElement()).getPath();
                        testClassPath = testClassPath.replace('/', '.');

                        String testClassName = testClassPath;
                        if (outputSubdir != null) {
                            testClassName = testClassName.substring(".".length() + outputSubdir.length());
                        } else {// else no output dir we are at runtime so class
                                // at root
                            // just remove the first .
                            testClassName = testClassName.substring(".".length());
                        }

                        if (!isPrefix(testClassName, allPrefixes)) {// if wrong
                                                                    // prefix
                                                                    // move to
                                                                    // next
                                                                    // class
                            continue;
                        } // else we want that class, it has the proper prefix

                        testClassName = testClassName.substring(0, testClassName.length() - ".class".length()); //$NON-NLS-1$

                        /*
                         * Attempt to load the class using the bundle classloader.
                         */
                        Class<?> testClass = null;
                        try {
                            testClass = bundleHost.loadClass(testClassName);
                            // check that class has Test cases, if not we do not keep it
                            if (!TestCase.class.isAssignableFrom(testClass) && !hasTestCaseMethods(testClass)) {
                                testClass = null;
                                if (Boolean.getBoolean(RECORD_NON_TEST_CLASSES_PROP)) {
                                    StringBuffer mess = new StringBuffer()
                                            .append("the class has not test case :[")
                                            .append(testClassName)
                                            .append("], bundle [")
                                            .append(bundle.getSymbolicName())
                                            .append("]");
                                    testClass = createCollectionErrorMessageTestCase(mess.toString());
                                }
                            } // else testClass contains test case so keep going to record the test case
                        } catch (ClassNotFoundException e) {
                            StringBuffer mess = new StringBuffer()
                                    .append("Failed to load class name [")
                                    .append(testClassName)
                                    .append("], bundle [")
                                    .append(bundle.getSymbolicName())
                                    .append("]");
                            testClass = createCollectionErrorMessageTestCase(mess.toString());
                        }

                        /*
                         * If the class is not abstract, add it to list
                         */
                        if (testClass != null && !Modifier.isAbstract(testClass.getModifiers())) {
                            testClassesInBundle.add(testClass);
                        }
                    }
                }
            } catch (IOException ioe) {
                StringBuffer mess = new StringBuffer()
                        .append("Failed to load the build.properties file :")
                        .append(ioe.getMessage())
                        .append(bundle.getSymbolicName());
                testClassesInBundle.add(createCollectionErrorMessageTestCase(mess.toString()));

            }
        } // else return an empty list
        return testClassesInBundle;
    }

    /**
     * check the testString starts with one of the prefixes.
     *
     * @param testString , the string to test
     * @param allPrefixes , all the prefix to check
     * @return true if the testString starts with one of the prefixes or false otherwise. Also true is return if no
     * prefix is specifed
     */
    private boolean isPrefix(String testClassName, String[] allPrefixes) {
        if (allPrefixes == null || allPrefixes.length == 0) {
            return true;
        }
        // test "NOT" condition first to avoid to have prefix we don't want.
        for (String prefix : allPrefixes) {
            if (prefix.startsWith("!")) { //$NON-NLS-1$
                String testPrefix = prefix.replace("!", ""); //$NON-NLS-1$//$NON-NLS-2$
                if (testClassName.startsWith(testPrefix)) {
                    return false;
                }
            }
        }
        for (String prefix : allPrefixes) {
            if (testClassName != null && testClassName.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * check the testString ends with one of the suffixes.
     *
     * @param testString , the string to test
     * @param suffixes , all the suffixes to check
     * @return true if the testString ends with one of the suffixes or false otherwise. Also true is return if no suffix
     * is specifed
     */
    private boolean isSuffix(String testClassName, String[] suffixes) {
        if (suffixes == null || suffixes.length == 0) {
            return true;
        }
        for (String prefix : suffixes) {
            if (testClassName.endsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * get the IdeOutputDir
     *
     * @param bundle the bundle to fing the output dir
     * @return the dir subpath or null if none found
     * @throws IOException if the build.properties failed to load.
     */
    private String getIdeOutputSubDir(Bundle bundle) throws IOException {
        String outputSubDir = null;
        // get output folder path in case we are running from the IDE
        URL buildPropUrl = bundle.getEntry("/build.properties");
        if (buildPropUrl != null) {
            Properties buildProp = new Properties();
            InputStream buildPropStream = buildPropUrl.openStream();
            try {
                buildProp.load(buildPropStream);
                outputSubDir = buildProp.getProperty("output..", null);
            } finally {
                buildPropStream.close();
            }
        } // else return null
        return outputSubDir;
    }

    /**
     * check that clazz has got at least one method with a Test annotation
     *
     * @param clazz to look for Test methods
     * @return true if th class has at least one method annotated with Test
     */
    private boolean hasTestCaseMethods(Class<?> clazz) {
        for (Method method : clazz.getMethods()) {
            if (method.getAnnotation(Test.class) != null) {
                return true;
            } // else keep looking
        }
        return false;
    }

    /**
     * DOC sgandon creates a message test case and store error messages that may have happend during the collection of
     * test cases so that is does not stop the test process.
     *
     * @param errorMessage the message to display in the Junit report
     * @return the test case or null if alread created
     */
    protected Class<?> createCollectionErrorMessageTestCase(String errorMessage) {
        // a special test case class is loaded to report this error so that
        // test case can still keep running. This flag serve the prupose of
        // refering to this class only once
        if (!isErrorMessageTestCaseAlreadyReferenced) {
            errorMessageTestClass = ErrorMessageTestCase.class;
            isErrorMessageTestCaseAlreadyReferenced = true;
        } // else class already referenced so we just add new informations
        ErrorMessageTestCase.addNewMessage(errorMessage);
        return errorMessageTestClass;
    }

    private boolean isFragment(Bundle bundle) {
        Enumeration headerKeys = bundle.getHeaders().keys();
        while (headerKeys.hasMoreElements()) {
            if (headerKeys.nextElement().toString().equals("Fragment-Host")) { //$NON-NLS-1$
                return true;
            }
        }
        return false;
    }
}
