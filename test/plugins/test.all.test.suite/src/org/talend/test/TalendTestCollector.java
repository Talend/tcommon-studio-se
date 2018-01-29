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

package org.talend.test;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.log4j.Logger;

import test.common.BundleTestCollector;

/**
 * DOC sgandon class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class TalendTestCollector {

    private static Logger log = Logger.getLogger(TalendTestCollector.class);

    /**
     * System property key to specify string representing the prefix of plugin names to look for test classes, this may
     * be empty.
     * 
     * @see GenericTestsJUnit4Suite.DEFAULT_PLUGIN_PREFIX
     */
    public static final String TEST_PLUGIN_PREFIX_SP = "test.plugin.prefix"; //$NON-NLS-1$

    /**
     * default plugin prefix
     */
    public static final String DEFAULT_PLUGIN_PREFIX = "org.talend."; //$NON-NLS-1$

    /**
     * System property key to specify string representing the suffix of plugin names to look for test classes, this may
     * be empty
     * 
     * @see GenericTestsJUnit4Suite.DEFAULT_PLUGIN_SUFFIX
     */
    public static final String TEST_PLUGIN_SUFFIX_SP = "test.plugin.suffix"; //$NON-NLS-1$

    /**
     * default plugin suffic
     */
    public static final String DEFAULT_PLUGIN_SUFFIX = ".test"; //$NON-NLS-1$

    /**
     * System property key to specify string representing the package prefix list value, this may be empty.
     * 
     * @see GenericTestsJUnit4Suite.DEFAULT_PLUGIN_SUFFIX
     */
    public static final String TEST_PACKAGE_PREFIX_SP = "test.package.prefix"; //$NON-NLS-1$

    /**
     * default class filter value
     */
    public static final String DEFAULT_PACKAGE_PREFIX = "org.talend"; //$NON-NLS-1$

    /**
     * System property key to specify string representing the filter to select test classes, you may use the * wildcard.
     * 
     * @see GenericTestsJUnit4Suite.TEST_CLASS_FILTER_SP
     */
    public static final String TEST_CLASS_FILTER_SP = "test.class.filter"; //$NON-NLS-1$

    /**
     * default class filter value
     */
    public static final String DEFAULT_CLASS_FILTER = "*Test"; //$NON-NLS-1$

    /**
     * System property key to specify a boolean (false or true) to use only Eclipse fragment instead of all plugins. the
     * default value is true
     * 
     */
    public static final String ONLY_USE_FRAGMENT_SP = "test.only.fragments"; //$NON-NLS-1$

    /**
     * key used to get the list of features to search for plugins and classes
     **/
    private static final String TEST_FEATURE_LIST_SP = "test.feature.list";

    /**
     * add tests
     */
    private static final String DEFAULT_FEATURE_LIST = null;

    public Class<?>[] getTests() {
        BundleTestCollector testCollector = new BundleTestCollector();
        Class<?>[] allCollectedTestClasses = testCollector.collectTestsClasses(
                System.getProperty(TEST_FEATURE_LIST_SP, DEFAULT_FEATURE_LIST),
                System.getProperty(TEST_PLUGIN_PREFIX_SP, DEFAULT_PLUGIN_PREFIX),
                System.getProperty(TEST_PLUGIN_SUFFIX_SP, DEFAULT_PLUGIN_SUFFIX),
                System.getProperty(TEST_PACKAGE_PREFIX_SP, DEFAULT_PACKAGE_PREFIX),
                System.getProperty(TEST_CLASS_FILTER_SP, DEFAULT_CLASS_FILTER),
                Boolean.parseBoolean(System.getProperty(ONLY_USE_FRAGMENT_SP, "true")));

        // sort the tests classes
        Arrays.sort(allCollectedTestClasses, new Comparator<Class<?>>() {

            @Override
            public int compare(Class<?> c1, Class<?> c2) {
                return c1.getName().compareTo(c2.getName());
            }
        });

        // log in console
        StringBuffer classListMessage = new StringBuffer('\n' + allCollectedTestClasses.length
                + " Test classes will be tested : \n");
        for (Class<?> clazz : allCollectedTestClasses) {
            classListMessage.append(clazz.getName()).append('\n');
        }
        System.out.println(classListMessage.toString());

        return allCollectedTestClasses; //$NON-NLS-1$
    }

}
