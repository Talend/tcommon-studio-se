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
package org.talend.utils.properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.talend.utils.string.StringUtilities;

/**
 * @author scorreia This class extends the Properties class and gives methods for getting typed values more easily.
 * 
 */
public class TypedProperties extends Properties {

    private static Logger log = Logger.getLogger(TypedProperties.class);

    /**
     * A generated serial ID.
     */
    private static final long serialVersionUID = 2581080352306726049L;

    /**
     * The default delimiters used in the splitting of strings.
     */
    public static final String DEFAULT_DELIMITERS = ",";

    Set<String> anomaliesAlreadyLogged = new HashSet<String>();

    /**
     * DOC scorreia TypedProperties constructor comment.
     */
    public TypedProperties() {
        super();
    }

    /**
     * DOC scorreia TypedProperties constructor comment.
     * 
     * @param defaults
     */
    public TypedProperties(Properties defaults) {
        super(defaults);
    }

    /**
     * Method "getBooleanValue".
     * 
     * @param <T> the class type that enters into the key definition
     * @param clazz the class
     * @param shortKey the end of the key
     * @param defaultValue
     * @return the value of the given key
     */
    public <T> boolean getBooleanValue(Class<T> clazz, String shortKey, boolean defaultValue) {
        return getBooleanValue(this.buildKey(clazz, shortKey), defaultValue);
    }

    /**
     * Method "getBooleanValue".
     * 
     * @param key the full key
     * @param defaultValue the default value if none is found
     * @return the value
     */
    public boolean getBooleanValue(String key, boolean defaultValue) {
        String value = this.getProperty(key);
        if (value == null) {
            logWarningPropertyNotFound(key, (Object) defaultValue);
            return defaultValue;
        } // else
        return Boolean.parseBoolean(value.trim());
    }

    private void logWarningPropertyNotFound(String notFoundKey, Object defaultValue) {
        if (!anomaliesAlreadyLogged.contains(notFoundKey)) {
            anomaliesAlreadyLogged.add(notFoundKey);
            log.warn("!!! PROPERTY NOT FOUND !!!: the key '" + notFoundKey
                    + "' can't be found in the JobServer properties file, the default value '" + defaultValue
                    + "' will be used, please be sure this is not an anomaly.");
        }
    }

    /**
     * Method "getBooleanValue".
     * 
     * @param key the full key
     * @return the value or null when not found
     */
    public Boolean getBooleanValue(String key) {
        String value = this.getProperty(key);
        if (value == null) {
            return null;
        } // else
        return Boolean.parseBoolean(value.trim());
    }

    /**
     * Method "getLongValue".
     * 
     * The properties key is evaluated to {@link Class#getName()}+"."+shortKey
     * 
     * @param <T> the class type that enters into the key definition
     * @param clazz the class
     * @param shortKey the end of the key
     * @param defaultValue
     * @return the value of the given key
     */
    public <T> long getLongValue(Class<T> clazz, String shortKey, long defaultValue) {
        return getLongValue(this.buildKey(clazz, shortKey), defaultValue);
    }

    /**
     * DOC scorreia Comment method "getDoubleValue".
     * 
     * @param <T>
     * @param clazz
     * @param shortKey
     * @param defaultValue
     * @return
     */
    public <T> double getDoubleValue(Class<T> clazz, String shortKey, double defaultValue) {
        return getDoubleValue(this.buildKey(clazz, shortKey), defaultValue);
    }

    /**
     * DOC scorreia Comment method "getDoubleValue".
     * 
     * @param key
     * @param defaultValue
     * @return
     */
    public double getDoubleValue(String key, double defaultValue) {
        String value = this.getProperty(key);
        if (value == null) {
            logWarningPropertyNotFound(key, (Object) defaultValue);
            return defaultValue;
        } // else
        return Double.parseDouble(value.trim());
    }

    /**
     * Method "getIntValue".
     * 
     * The key is the concatenation of the class name and the shortKey
     * 
     * @param <T>
     * @param clazz
     * @param shortKey
     * @param defaultValue
     * @return the value of the given key
     */
    public <T> int getIntValue(Class<T> clazz, String shortKey, int defaultValue) {
        return getIntValue(this.buildKey(clazz, shortKey), defaultValue);
    }

    public <T> String getStringValue(Class<T> clazz, String shortKey, String defaultValue) {
        return getStringValue(this.buildKey(clazz, shortKey), defaultValue);
    }

    /**
     * DOC scorreia Comment method "getStringValue".
     * 
     * @param key
     * @param defaultValue
     * @return
     */
    private String getStringValue(String key, String defaultValue) {
        String value = this.getProperty(key);
        if (value == null) {
            logWarningPropertyNotFound(key, (Object) defaultValue);
            return defaultValue;
        } // else
        return value;
    }

    /**
     * DOC scorreia Comment method "getLongValue".
     * 
     * @param key
     * @param defaultValue
     * @return
     * @throws NumberFormatException if the string does not contain a parsable <code>long</code>.
     */
    public long getLongValue(String key, long defaultValue) {
        String value = this.getProperty(key);
        if (value == null) {
            logWarningPropertyNotFound(key, (Object) defaultValue);
            return defaultValue;
        } // else
        return Long.parseLong(value.trim());
    }

    /**
     * DOC scorreia Comment method "getIntValue".
     * 
     * @param key
     * @param defaultValue
     * @return
     * @throws NumberFormatException if the string does not contain a parsable <code>int</code>.
     */
    public int getIntValue(String key, int defaultValue) {
        String value = this.getProperty(key);
        if (value == null) {
            logWarningPropertyNotFound(key, (Object) defaultValue);
            return defaultValue;
        } // else
        return Integer.parseInt(value.trim());
    }

    /**
     * DOC scorreia Comment method "getValues".
     * 
     * @param key
     * @param defaultValues
     * @param delimiters
     * @return
     */
    public List<String> getValues(String key, List<String> defaultValues, String delimiters) {
        String value = this.getProperty(key);
        if (value == null) {
            logWarningPropertyNotFound(key, String.valueOf((Object) defaultValues));
            return defaultValues;
        } // else
        return StringUtilities.tokenize(value, delimiters);
    }
    
    /**
     * https://jira.talendforge.org/browse/TDI-25753
     * DOC talend Comment method "getValuesWithoutWarning".
     * @param clazz
     * @param shortKey
     * @param defaultValues
     * @return
     */
    public <T> List<String> getValuesWithoutWarning(Class<T> clazz, String shortKey, List<String> defaultValues) {
        String key = buildKey(clazz, shortKey);
        String value = this.getProperty(key);
        if (value == null) {
            return defaultValues;
        } // else
        return StringUtilities.tokenize(value, DEFAULT_DELIMITERS);
    }
    
    /**
     * DOC scorreia Comment method "getValues".
     * 
     * @param key
     * @param defaultValues
     * @return
     */
    public List<String> getValues(String key, List<String> defaultValues) {
        return getValues(key, defaultValues, DEFAULT_DELIMITERS);
    }

    /**
     * DOC scorreia Comment method "getValues".
     * 
     * @param <T>
     * @param clazz
     * @param shortKey
     * @param defaultValues
     * @return
     */
    public <T> List<String> getValues(Class<T> clazz, String shortKey, List<String> defaultValues) {
        return getValues(this.buildKey(clazz, shortKey), defaultValues, DEFAULT_DELIMITERS);
    }

    /**
     * DOC scorreia Comment method "getValues".
     * 
     * @param <T>
     * @param clazz
     * @param shortKey
     * @param defaultValues
     * @param delimiters
     * @return
     */
    public <T> List<String> getValues(Class<T> clazz, String shortKey, List<String> defaultValues, String delimiters) {
        return getValues(this.buildKey(clazz, shortKey), defaultValues, delimiters);
    }

    /**
     * Method "buildKey".
     * 
     * @param <T>
     * @param clazz
     * @param shortKey
     * @return the key: {@link Class#getName()}+"."+key
     */
    private <T> String buildKey(Class<T> clazz, String shortKey) {
        if (clazz == null) {
            return shortKey;
        } // else
        return clazz.getName() + "." + shortKey;
    }

    /**
     * Method "logProperties".
     * 
     * @param all if true, all properties will be returned (even the default ones)
     * @param main if true, the main properties are returned, not the default.
     * @return a string containing the requested properties
     * 
     * If all and main are both true, the main properties will be returned twice. When all properties are returned, the
     * format: is one property per line. When the main properties are returned, the format is the that of the HashTable
     * (i.e. one line with properties separated by comma).
     */
    public String logProperties(boolean all, boolean main) {
        StringBuilder builder = new StringBuilder();
        if (all) {
            builder.append("PROPERTIES: List of input properties:\n");
            Enumeration<?> names = this.propertyNames();

            ArrayList<String> namesList = new ArrayList<String>();

            while (names.hasMoreElements()) {
                String key = (String) names.nextElement();
                namesList.add(key);
            }
            Collections.sort(namesList);

            int namesListListSize = namesList.size();
            for (int i = 0; i < namesListListSize; i++) {
                String key = namesList.get(i);
                String property = this.getProperty(key);
                builder.append(key + "=" + property + "\n");
            }
            builder.append("PROPERTIES: End of list.\n");
        }
        if ((!all) && (main)) {
            builder.append("TYPED PROPERTIES=" + this.toString());
        }
        return builder.toString();
    }
}
