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

package org.talend.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.URIUtil;
import org.osgi.framework.Bundle;

import test.common.BundleTestCollector;

/**
 * DOC sgandon class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class TalendSwtBotTestCollectorAnt extends TalendTestCollector{
    private static final String PROPERTY_FOLDER_PATH = "/conf/"; //$NON-NLS-1$
	
    private static Logger log = Logger.getLogger(TalendSwtBotTestCollectorAnt.class);
    
    public static final String TEST_PLUGIN_PREFIX_SUFFIX_PACKAGE_CLASS_FRAGMENTS="test.plugin.prefix.suffix.package.class.fragments";
    public static final String TEST_PLUGIN_PREFIX_SUFFIX_PACKAGE_CLASS_FRAGMENTS_REMOVE="test.plugin.prefix.suffix.package.class.fragments.remove";

    /**
     * System property key to specify string representing the prefix of plugin names to look for test classes, this may
     * be empty.
     * 
     * @see GenericTestsJUnit4Suite.DEFAULT_PLUGIN_PREFIX
     */
    public static final String TEST_PLUGIN_PREFIX_SP = "test.plugin.prefix"; //$NON-NLS-1$
    public static final String TEST_PLUGIN_PREFIX_REMOVE_SP = "test.plugin.prefix.remove"; //$NON-NLS-1$

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
    public static final String TEST_PLUGIN_SUFFIX_REMOVE_SP = "test.plugin.suffix.remove"; //$NON-NLS-1$

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
    public static final String TEST_PACKAGE_PREFIX_REMOVE_SP = "test.package.prefix.remove"; //$NON-NLS-1$

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
    public static final String TEST_CLASS_FILTER_REMOVE_SP = "test.class.filter.remove"; //$NON-NLS-1$

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
    public static final String ONLY_USE_FRAGMENT_REMOVE_SP = "test.only.fragments.remove"; //$NON-NLS-1$

    /**
     * key used to get the list of features to search for plugins and classes
     **/
    private static final String TEST_FEATURE_LIST_SP = "test.feature.list";
    private static final String TEST_FEATURE_LIST_REMOVE_SP = "test.feature.list.remove";

    /**
     * add tests
     */
    private static final String DEFAULT_FEATURE_LIST = null;
    
    private static final long MAX = Integer.MAX_VALUE;

    public Class<?>[] getTests() {
    	
        List<String[]> addClassCollectors =  new ArrayList<String[]>();
        for(int i=1; i<MAX; i++) {
        	
        	if(System.getProperty(TEST_PLUGIN_PREFIX_SUFFIX_PACKAGE_CLASS_FRAGMENTS+i)!=null && !"".equals(System.getProperty(TEST_PLUGIN_PREFIX_SUFFIX_PACKAGE_CLASS_FRAGMENTS+i))) {
        		System.err.println("Add - "  + System.getProperty(TEST_PLUGIN_PREFIX_SUFFIX_PACKAGE_CLASS_FRAGMENTS+i));
            	if(this.splitTheParameters(System.getProperty(TEST_PLUGIN_PREFIX_SUFFIX_PACKAGE_CLASS_FRAGMENTS+i)).length <= 5){
            		addClassCollectors.add(this.splitTheParameters(":"+System.getProperty(TEST_PLUGIN_PREFIX_SUFFIX_PACKAGE_CLASS_FRAGMENTS+i)));
            	}else {
            		addClassCollectors.add(this.splitTheParameters(System.getProperty(TEST_PLUGIN_PREFIX_SUFFIX_PACKAGE_CLASS_FRAGMENTS+i)));
            	}
            	
        	} else {
        		break;
        	}
/*        	if(System.getProperty(TEST_CLASS_FILTER_SP+i)!=null && !"".equals(System.getProperty(TEST_CLASS_FILTER_SP+i))) {
            	String[] collector = new String[]{
                        System.getProperty(TEST_FEATURE_LIST_SP+i, DEFAULT_FEATURE_LIST),
                        System.getProperty(TEST_PLUGIN_PREFIX_SP+i, DEFAULT_PLUGIN_PREFIX),
                        System.getProperty(TEST_PLUGIN_SUFFIX_SP+i, DEFAULT_PLUGIN_SUFFIX),
                        System.getProperty(TEST_PACKAGE_PREFIX_SP+i, DEFAULT_PACKAGE_PREFIX),
                        System.getProperty(TEST_CLASS_FILTER_SP+i, DEFAULT_CLASS_FILTER),
                        System.getProperty(ONLY_USE_FRAGMENT_SP+i, "true")	
                };
            	addClassCollectors.add(collector);
        	} else {
        		break;
        	}*/
        }
        List<Class> classes = getClassesViaFilter(addClassCollectors);
        for(Class c : classes) {
        	System.err.println("Add - "  + c.getCanonicalName());
        }
        
        List<String[]> removeClassCollectors = new ArrayList<String[]>();
        for(int i=1; i<MAX; i++) {
        	if(System.getProperty(TEST_PLUGIN_PREFIX_SUFFIX_PACKAGE_CLASS_FRAGMENTS_REMOVE+i)!=null && !"".equals(System.getProperty(TEST_PLUGIN_PREFIX_SUFFIX_PACKAGE_CLASS_FRAGMENTS_REMOVE+i))) {
        		System.err.println("Remove - "  + System.getProperty(TEST_PLUGIN_PREFIX_SUFFIX_PACKAGE_CLASS_FRAGMENTS_REMOVE+i));
        		if(this.splitTheParameters(System.getProperty(TEST_PLUGIN_PREFIX_SUFFIX_PACKAGE_CLASS_FRAGMENTS_REMOVE+i)).length <=5){
        			removeClassCollectors.add(this.splitTheParameters(":"+System.getProperty(TEST_PLUGIN_PREFIX_SUFFIX_PACKAGE_CLASS_FRAGMENTS_REMOVE+i)));
        		} else {
        			removeClassCollectors.add(this.splitTheParameters(System.getProperty(TEST_PLUGIN_PREFIX_SUFFIX_PACKAGE_CLASS_FRAGMENTS_REMOVE+i)));
        		}
        	} else {
        		break;
        	}
        	
/*        	if(System.getProperty(TEST_CLASS_FILTER_REMOVE_SP+i)!=null && !"".equals(System.getProperty(TEST_CLASS_FILTER_REMOVE_SP+i))) {
            	String[] collector = new String[]{                
                		System.getProperty(TEST_FEATURE_LIST_REMOVE_SP+i, DEFAULT_FEATURE_LIST),
                        System.getProperty(TEST_PLUGIN_PREFIX_REMOVE_SP+i, DEFAULT_PLUGIN_PREFIX),
                        System.getProperty(TEST_PLUGIN_SUFFIX_REMOVE_SP+i, DEFAULT_PLUGIN_SUFFIX),
                        System.getProperty(TEST_PACKAGE_PREFIX_REMOVE_SP+i, DEFAULT_PACKAGE_PREFIX),
                        System.getProperty(TEST_CLASS_FILTER_REMOVE_SP+i, DEFAULT_CLASS_FILTER),
                        System.getProperty(ONLY_USE_FRAGMENT_REMOVE_SP+i, "true")};
            	removeClassCollectors.add(collector);
        	} else{
        		break;
        	}*/
        }
        List<Class> exceptionClasses = getClassesViaFilter(removeClassCollectors);
        for(Class c : exceptionClasses) {
        	System.err.println("Remove - "  + c.getCanonicalName());
        }
        
        // remove the except classes from added classes
        for(Class clazz : exceptionClasses) {
        	if(classes.contains(clazz)) {
        		classes.remove(clazz);
        	}
        }
        
        for(Class c : classes) {
        	System.err.println("Finally - "  + c.getCanonicalName());
        }
        
        // Add ProfiledPropertyLoader
        ProfiledPropertyLoader loader = new ProfiledPropertyLoader();
        try {
        	loader.loadProperties();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        System.err.println("properties.flag: " + System.getProperty("properties.flag"));
        // load the properties here.
        return classes.toArray(new Class<?>[]{});
    }
    
    
    public static Class<?>[] getTests(String[] parameters) {
        BundleTestCollector testCollector = new BundleTestCollector();
        Class<?>[] allCollectedTestClasses = testCollector.collectTestsClasses(
        		parameters[0],
        		parameters[1],
        		parameters[2],
        		parameters[3],
        		parameters[4],
                Boolean.parseBoolean(parameters[5]));
        StringBuffer classListMessage = new StringBuffer(allCollectedTestClasses.length + " Test classes will be tested : \n");
        for (Class<?> clazz : allCollectedTestClasses) {
            classListMessage.append(clazz.getName()).append('\n');
        }
        System.err.println(classListMessage.toString());
        return allCollectedTestClasses; //$NON-NLS-1$
    }
    
    public static void addArrayToArrayList(List<Class> classes, Class[] clazz) {
    	for(Class clz : clazz) {
    		if(!classes.contains(clz)) {
    			classes.add(clz);
    		}
    	}
    }
    
    public static List<Class> getClassesViaFilter(List<String[]> filters) {
        List<Class> filterClasses = new ArrayList<Class>(); 
        for(String[] filter : filters) {
        	Class<?>[] classFilter = getTests(filter);
        	addArrayToArrayList(filterClasses, classFilter);
        }
        return filterClasses;
    }
    
    public String[] splitTheParameters(String param){
    	return param.split(":");
    }
    
//
//    /**
//     * return a File instance pointing to the file or folder located below this plugin sample folder
//     * 
//     * @param subSamplePath, path located below /sample/, so subpath should never start with a /, example : test.xsl
//     * @return the File instance pointing to the obsolute path of the subSamplePath
//     * @throws IOException
//     * @throws URISyntaxException
//     */
//    public File getFileFromCurrentPluginSampleFolder(String subSamplePath) throws IOException, URISyntaxException {
//        Bundle bundle = Platform.getBundle("test.all.test.suite");
//        URL url = bundle.getEntry(PROPERTY_FOLDER_PATH + subSamplePath);
//        URI escapedURI = getFileUrl(url);
//        return URIUtil.toFile(escapedURI);
//    }
//
//    /**
//     * Convert any URL to a file URL
//     * 
//     * @param url, may be a eclipse URL
//     * @return a file:/ url
//     * @throws IOException
//     * @throws URISyntaxException
//     */
//    public URI getFileUrl(URL url) throws IOException, URISyntaxException {
//        // bundleresource://875.fwk22949069:4/top400/
//        URL unescapedURL = FileLocator.toFileURL(url);// bug 145096
//        // file:/E:sv n/org.talend.model.migration.test/samples/top400/
//        URI escapedURI = new URI(unescapedURL.getProtocol(), unescapedURL.getPath(), unescapedURL.getQuery());
//        // file:/E:sv%20n/org.talend.model.migration.test/samples/top400/
//        return escapedURI;
//    }
//
//    /**
//     * Load properties to System from properties file.
//     * 
//     * @throws IOException
//     * @throws URISyntaxException
//     */
//    public void loadProperties() throws IOException, URISyntaxException {
//        String defaultFileName = "global.swtbot_.properties";
//        String fileName = "global.swtbot_" + System.getProperty("talend.testing.profile", "") + ".properties";
//        File propertiesFile = null;
//        try {
//            propertiesFile = getFileFromCurrentPluginSampleFolder(fileName);
//        } catch (Exception e) {
//            System.out.println("Couldn't load the properties file: " + fileName + ", try to load the default the file: "
//                    + defaultFileName);
//            propertiesFile = getFileFromCurrentPluginSampleFolder(defaultFileName);
//        }
//        loadPropertiesFile(propertiesFile);
//    }
//
//    /**
//     * Load properties file
//     */
//    public void loadPropertiesFile(File file) {
//        Properties props = new Properties();
//		Properties sysProp = System.getProperties();
//        try {
//            InputStream is = new FileInputStream(file);
//            props.load(is);
//            // System.setProperties(props);
//            for (Object key : props.keySet()) {
//            	if(!sysProp.containsKey(key)) {
//            		System.setProperty((String) key, (String) props.get(key));
//            	} else {
//            		System.out.println("The key of '" + key + "' is existed! - value - " + System.getProperty((String) key));
//            	}
//            }
//            is.close();
//        } catch (IOException e) {
//            System.out.println("Failed to load properties file!");
//        }
//
//        System.out.println("Property: " + System.getProperty("properties.flag"));
//    }
}
