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
package org.talend.utils.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author scorreia <br>
 * This class permits to load property file by simply giving the JVM argument. <br>
 * Ex.: -D"talend_props=/home/myname/myfile.properties"
 */
public final class PropertiesLoader {

    private static Logger log = Logger.getLogger(PropertiesLoader.class);

    /**
     * Use as a JVM argument for loading properties file. <br>
     * Ex.: -D"talend_props=/home/myname/myfile.properties"
     */
    public static final String MY_PROP_KEY = "talend_props";

    /**
     * the propertie used or set.
     */
    private static final String PROPERTIES_FILENAME = System.getProperty(MY_PROP_KEY);

    private static final String USAGE = "Try with -D" + MY_PROP_KEY
            + "=file.properties with file.properties a relative or absolute file path.";

    private static final String QUOTE = "'";

    private static TypedProperties curProp;
    
	private static Properties convertToProperties(Dictionary config) {
		Enumeration keys = config.keys();
		Properties props = new Properties();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			props.setProperty(key, (String) config.get(key));
		}
		return props;
	}
    
    public static void setConfig(Dictionary config) {
    	Properties prop = convertToProperties(config);
    	curProp = new TypedProperties(prop);
    }

    /**
     * Method "quotedAbs" puts quotes around the path of the given file.
     * 
     * @param in the file
     * @return the absolute path between quotes.
     */
    private static String quotedAbsolutePath(File in) {
        return (in == null) ? QUOTE : QUOTE + in.getAbsolutePath() + QUOTE;
    }

    /**
     * Method "getProperties".
     * 
     * @return the properties loaded from the file given as JVM argument with
     * -D"talend_props=/home/myname/myfile.properties". Extra arguments to the JVM are default to the returned
     * Properties. All System properties are default value to the returned properties. this means, that the returned
     * properties can be used to get the System properties. This also means that if a property is in the file and also
     * in the JVM argument, the one in the file will be used, not the one in the command line.
     * @see {@link PropertiesLoader#MY_PROP_KEY} for the key to give to the JVM
     */
    public static synchronized TypedProperties getProperties() {
        if (curProp == null) { // should not happen
            curProp = initialize();
        }
        return curProp;
    }

    /**
     * Method "getProperties". Loads a new TypedProperties from the file "propertiesFilename" that the classloader of
     * the given class "clazz" is able to find in its resources.
     * 
     * @param clazz the class whose classloader will be used to get the properties file
     * @param propertiesFilename the properties file name (not the path)
     * @return typed properties loaded from the given file or empty typed properties if file could not be found
     */
    public static synchronized TypedProperties getProperties(Class<?> clazz, String propertiesFilename) {
        TypedProperties prop = new TypedProperties();

        InputStream inStream = clazz.getClassLoader().getResourceAsStream(propertiesFilename);
        if (inStream == null) {
            // try to get it directly from the class (this is for use in Eclipse plugin environment)
            inStream = clazz.getResourceAsStream(propertiesFilename);
        }

        // System.out.println("url: " + inStream);

        if (inStream == null) {
            log.error("Properties file not found: " + propertiesFilename);
        } else {
            try {
                prop.load(inStream);
            } catch (IOException e) {
                log.error("Properties file " + propertiesFilename + " not found: " + e.getMessage());
            }
        }
        return prop;
    }

    public static synchronized void setProperties(Class<?> clazz, String propertiesFilename, String key, String oldValue,
            String newValue) {
        if (oldValue.equals(newValue)) {
            return;
        }
        try {
            PropertiesReloader.setProperties(clazz, propertiesFilename, key, oldValue, newValue);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * DOC scorreia Comment method "initialize".
     * 
     * @return the loaded properties or new empty Properties
     */
    private static synchronized TypedProperties initialize() {
        Properties sysProp = System.getProperties();
        TypedProperties prop = new TypedProperties(sysProp);
        if ((PROPERTIES_FILENAME == null) || (PROPERTIES_FILENAME.length() == 0)) {
            log.warn("Warning: no properties file name given in JVM arguments." + USAGE);
        } else {
            initialize(prop, new File(PROPERTIES_FILENAME));
        } // eof if
        return prop;
    }

    /**
     * Method "initialize" initializes the given properties with the content of the given file.
     * 
     * @param prop the output properties (filled in with the input file)
     * @param in a properties file
     * @return true if ok.
     */
    public static synchronized boolean initialize(TypedProperties prop, File in) {
        boolean ok = true;
        try {
            if (in.exists()) {
                log.info("Loading Properties from file: " + quotedAbsolutePath(in));
            } else {
                ok = false;
                log.info("Given file for properties does not exist: " + quotedAbsolutePath(in));
            }
            String filename = in.getAbsolutePath();
            ok = ok && loadPropertiesLow(filename, prop);
            if (!ok) {
                log.warn("Warning: Problem when loading properties from file " + filename);
            }
        } catch (Exception e) {
            ok = false;
            log.error(USAGE);
            log.error("ERROR: could not load properties file=" + quoted(in.toString()), e);
        } // eof try

        return ok;
    }

    /**
     * DOC scorreia Comment method "loadPropertiesLow".
     * 
     * @param filename
     * @return
     */
    private static boolean loadPropertiesLow(String filename, TypedProperties prop) throws Exception {
        boolean ok = true;
        FileInputStream is = null;
        try {
            is = new FileInputStream(filename);
            // FIXME dead code here
            if (is == null) {
                throw new Exception("ERROR: Could not load file = " + quoted(filename));
            }
            prop.load(is);
            is.close();
            is = null;
        } catch (Exception e) {
            ok = false;
            if (is != null) {
                try {
                    is.close();
                } catch (Exception ignore) {
                    /* ignore catch !! */
                }
            }
            throw e;
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return ok;
    }

    private static String quoted(final String in) {
        return (in == null) ? QUOTE + QUOTE : QUOTE + in + QUOTE;
    }

    private PropertiesLoader() {
    }
}
