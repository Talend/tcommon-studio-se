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
package org.talend.core.model.metadata.types;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.runtime.i18n.Messages;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public final class JavaTypesManager {

    public static final String DEFAULT_CHAR = "' '"; //$NON-NLS-1$

    public static final String DEFAULT_BOOLEAN = "false"; //$NON-NLS-1$

    public static final String ANOTHER_BOOLEAN = "true"; //$NON-NLS-1$

    public static final String DEFAULT_NUMBER = "0"; //$NON-NLS-1$

    public static final String DEFAULT_STRING = ""; //$NON-NLS-1$

    public static final String NULL = "null"; //$NON-NLS-1$

    public static final String JAVA_PRIMITIVE_CHAR = "char"; //$NON-NLS-1$

    public static final String JAVA_PRIMITIVE_BOOLEAN = "boolean"; //$NON-NLS-1$

    public static final JavaType BOOLEAN = new JavaType(Boolean.class, boolean.class);

    public static final JavaType BYTE = new JavaType(Byte.class, byte.class);

    public static final JavaType BYTE_ARRAY = new JavaType(byte[].class, false, false);

    public static final JavaType CHARACTER = new JavaType(Character.class, char.class);

    public static final JavaType DATE = new JavaType(Date.class, true, false);

    public static final JavaType FILE = new JavaType(String.class, true, "File"); //$NON-NLS-1$

    public static final JavaType DIRECTORY = new JavaType(String.class, true, "Directory"); //$NON-NLS-1$

    public static final JavaType VALUE_LIST = new JavaType(String.class, true, "List Of Value"); //$NON-NLS-1$

    public static final JavaType DOUBLE = new JavaType(Double.class, double.class);

    public static final JavaType FLOAT = new JavaType(Float.class, float.class);

    public static final JavaType INTEGER = new JavaType(Integer.class, int.class);

    public static final JavaType LONG = new JavaType(Long.class, long.class);

    public static final JavaType SHORT = new JavaType(Short.class, short.class);

    public static final JavaType STRING = new JavaType(String.class, false, false);

    public static final JavaType OBJECT = new JavaType(Object.class, false, true);

    public static final JavaType DYNAMIC = new JavaType(Object.class, false, true);

    public static final JavaType LIST = new JavaType(List.class, false, true);

    public static final JavaType BIGDECIMAL = new JavaType(BigDecimal.class, false, true);

    public static final JavaType PASSWORD = new JavaType(String.class, true, "Password"); //$NON-NLS-1$

    public static final JavaType[] JAVA_TYPES = new JavaType[] { BOOLEAN, BYTE, BYTE_ARRAY, CHARACTER, DATE, DOUBLE, FLOAT,
            BIGDECIMAL, INTEGER, LONG, OBJECT, SHORT, STRING, LIST };

    private static Map<String, Map<String, List<DBTypeUtil>>> javaTypeMappingFromExtension;

    private static Map<String, JavaType> shortNameToJavaType;

    private static Map<String, JavaType> canonicalClassNameToJavaType;

    private static Map<String, JavaType> labelToJavaType;

    private static Map<String, JavaType> idToJavaType;

    private static List<JavaType> javaTypes;

    private static String[] javaTypesLabelsArray = new String[0];

    private static final List<String> JAVA_PRIMITIVE_TYPES = new ArrayList<String>();

    static {
        init();
    }

    private static final Set<String> PRIMITIVE_TYPES_SET = new HashSet<String>(JAVA_PRIMITIVE_TYPES);

    /**
     * DOC amaumont Comment method "init".
     */
    private static void init() {

        shortNameToJavaType = new HashMap<String, JavaType>();
        labelToJavaType = new HashMap<String, JavaType>();
        idToJavaType = new HashMap<String, JavaType>();
        canonicalClassNameToJavaType = new HashMap<String, JavaType>();
        javaTypes = new ArrayList<JavaType>();
        javaTypeMappingFromExtension = new HashMap<String, Map<String, List<DBTypeUtil>>>();

        for (JavaType javaType : JAVA_TYPES) {
            addJavaType(javaType);
        }
        IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
        if (extensionRegistry != null) {
            IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint("org.talend.core.java_type"); //$NON-NLS-1$
            if (extensionPoint != null) {
                IExtension[] extensions = extensionPoint.getExtensions();
                for (IExtension extension : extensions) {
                    IConfigurationElement[] configurationElements = extension.getConfigurationElements();
                    for (IConfigurationElement configurationElement : configurationElements) {
                        try {
                            String className = configurationElement.getAttribute("nullableClass");
                            Class myClass = Platform.getBundle(configurationElement.getContributor().getName()).loadClass(
                                    className);
                            boolean isGenerateWithCanonicalName = configurationElement.getAttribute("generateWithCanonicalName") == null ? false
                                    : Boolean.valueOf(configurationElement.getAttribute("generateWithCanonicalName"));
                            boolean isObjectBased = configurationElement.getAttribute("objectBased") == null ? false : Boolean
                                    .valueOf(configurationElement.getAttribute("objectBased"));
                            JavaType javaType = new JavaType(myClass, isGenerateWithCanonicalName, isObjectBased);
                            addJavaType(javaType);

                            IConfigurationElement[] dbMappingElements = configurationElement.getChildren();
                            Map<String, List<DBTypeUtil>> dbAndDBType = new HashMap<String, List<DBTypeUtil>>();
                            for (IConfigurationElement dbMappingElement : dbMappingElements) {
                                String mappingId = dbMappingElement.getAttribute("mapping_id");
                                IConfigurationElement[] dbTypeElements = dbMappingElement.getChildren();
                                List<DBTypeUtil> dbTypes = new ArrayList<DBTypeUtil>();
                                for (IConfigurationElement dbTypeElement : dbTypeElements) {
                                    boolean isDefault = dbTypeElement.getAttribute("default") == null ? false : Boolean
                                            .valueOf(dbTypeElement.getAttribute("default"));
                                    boolean isIgnoreLen = dbTypeElement.getAttribute("ignoreLen") == null ? false : Boolean
                                            .valueOf(dbTypeElement.getAttribute("ignoreLen"));
                                    boolean isIgnorePre = dbTypeElement.getAttribute("ignorePre") == null ? false : Boolean
                                            .valueOf(dbTypeElement.getAttribute("ignorePre"));
                                    DBTypeUtil dbType = new DBTypeUtil(dbTypeElement.getAttribute("DbType"), isDefault,
                                            isIgnoreLen, isIgnorePre);
                                    dbTypes.add(dbType);
                                }
                                dbAndDBType.put(mappingId, dbTypes);
                            }
                            javaTypeMappingFromExtension.put(javaType.getId(), dbAndDBType);
                        } catch (ClassNotFoundException e) {
                            ExceptionHandler.process(e);
                        }
                    }
                }
            }
        }

        idToJavaType.put(PASSWORD.getId(), PASSWORD);
    }

    /**
     * Add a java type to the common list.
     * 
     * @param javaType
     */
    public static void addJavaType(JavaType javaType) {
        String primitiveName = null;
        Class primitiveClass = javaType.getPrimitiveClass();
        if (primitiveClass != null) {
            primitiveName = primitiveClass.getSimpleName();
            shortNameToJavaType.put(primitiveName, javaType);
            canonicalClassNameToJavaType.put(primitiveClass.getCanonicalName(), javaType);
            JAVA_PRIMITIVE_TYPES.add(primitiveClass.getSimpleName());
        }

        String nullableName = javaType.getNullableClass().getSimpleName();
        // if java type id exist , don't add it
        if (idToJavaType.keySet().contains(javaType.getId())) {
            return;
        }
        shortNameToJavaType.put(nullableName, javaType);
        canonicalClassNameToJavaType.put(javaType.getNullableClass().getCanonicalName(), javaType);
        labelToJavaType.put(javaType.getLabel(), javaType);
        idToJavaType.put(javaType.getId(), javaType);
        javaTypes.add(javaType);
    }

    /**
     * 
     * Search JavaType from short name (ex: "Double" or "double").
     * 
     * @param typeName
     * @return JavaType if found, else null
     */
    public static JavaType getJavaTypeFromName(String typeName) {
        return shortNameToJavaType.get(typeName);
    }

    /**
     * 
     * DOC amaumont Comment method "getShortNameFromJavaType".
     * 
     * @param javaType
     * @return primitive name if JavaType is primitive (boolean, int, etc.), else return Object type name (String, Date,
     * etc.)
     */
    public static String getShortNameFromJavaType(JavaType javaType) {
        Class primitiveClass = javaType.getPrimitiveClass();
        if (primitiveClass != null) {
            return primitiveClass.getSimpleName();
        }
        return javaType.getNullableClass().getSimpleName();
    }

    /**
     * 
     * Search JavaType from label (ex: "double / Double" or "String").
     * 
     * @param typeName
     * @return JavaType if found, else null
     */
    public static JavaType getJavaTypeFromLabel(String label) {
        return labelToJavaType.get(label);
    }

    /**
     * 
     * Search JavaType from label (ex: "double / Double" or "String").
     * 
     * @param typeName
     * @return JavaType if found, else null
     */
    public static JavaType getJavaTypeFromId(String id) {
        JavaType javaTypeFromId = idToJavaType.get(id);
        if (javaTypeFromId == null) {
            throw new IllegalArgumentException("Unknown java id type : '" + id + "'"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return javaTypeFromId;
    }

    public static String[] getJavaTypesLabels() {
        int lstSize = javaTypes.size();
        if (javaTypesLabelsArray.length != lstSize) {
            javaTypesLabelsArray = new String[lstSize];
            for (int i = 0; i < lstSize; i++) {
                javaTypesLabelsArray[i] = javaTypes.get(i).getLabel();
            }
        }
        return (String[]) ArrayUtils.clone(javaTypesLabelsArray);
    }

    public static JavaType[] getJavaTypes() {
        return javaTypes.toArray(new JavaType[0]);
    }

    /**
     * 
     * Search JavaType from short name (ex: "Double" or "double").
     * 
     * @param typeName
     * @return JavaType if found, else null
     */
    public static JavaType getJavaTypeFromCanonicalName(String canonicalName) {
        return canonicalClassNameToJavaType.get(canonicalName);
    }

    /**
     * 
     * Get primitive or object type according to id and nullable parameters.
     * 
     * @param idType
     * @param nullable
     * @return canonical name of class (java.lang.String or int)
     */
    public static String getTypeToGenerate(String idType, boolean nullable) {
        JavaType javaTypeFromId = getJavaTypeFromId(idType);
        return getTypeToGenerate(javaTypeFromId, nullable);
    }

    /**
     * DOC amaumont Comment method "getFinalType".
     * 
     * @param javaType
     * @param nullable
     * @return
     */
    public static String getTypeToGenerate(JavaType javaType, boolean nullable) {
        if (javaType == null) {
            return null;
        }
        Class primitiveClass = javaType.getPrimitiveClass();
        Class nullableClass = javaType.getNullableClass();
        if (nullable) {
            if (javaType.isGenerateWithCanonicalName()) {
                return nullableClass.getCanonicalName();
            } else {
                return nullableClass.getSimpleName();
            }
        } else {
            if (primitiveClass != null) {
                return javaType.getPrimitiveClass().getSimpleName();
            } else {
                if (javaType.isGenerateWithCanonicalName()) {
                    return nullableClass.getCanonicalName();
                } else {
                    return nullableClass.getSimpleName();
                }
            }
        }
    }

    /**
     * 
     * Return true if given type represents a primitive java type.
     * 
     * @param type
     * @return true if given type represents a primitive java type
     */
    public static boolean isJavaPrimitiveType(String type) {
        if (type == null) {
            throw new IllegalArgumentException();
        }
        return PRIMITIVE_TYPES_SET.contains(type);
    }

    /**
     * 
     * Return true if given type represents a primitive java type.
     * 
     * @param type
     * @return true if given type represents a primitive java type
     */
    public static boolean isJavaPrimitiveType(String idType, boolean nullable) {
        String typeToGenerate = getTypeToGenerate(idType, nullable);
        return isJavaPrimitiveType(typeToGenerate);
    }

    /**
     * 
     * Return true if given type represents a primitive java type.
     * 
     * @param type
     * @return true if given type represents a primitive java type
     */
    public static boolean isJavaPrimitiveType(JavaType javaType, boolean nullable) {
        String typeToGenerate = getTypeToGenerate(javaType, nullable);
        return isJavaPrimitiveType(typeToGenerate);
    }

    /**
     * They are all the number type.
     * <p>
     * int(Integer), float(Float), double(Double), long(Long), short(Short), byte(Byte), BigDecimal.
     * </p>
     * 
     * @param type
     * @return true if given type represents a primitive java type
     * @deprecated use same method without boolean <code>nullable</code>
     */
    @Deprecated
    public static boolean isNumberType(JavaType javaType, boolean nullable) {
        return Number.class.isAssignableFrom(javaType.getNullableClass());
    }

    /**
     * They are all the number type.
     * <p>
     * int(Integer), float(Float), double(Double), long(Long), short(Short), byte(Byte), BigDecimal.
     * </p>
     * 
     * @param type
     * @return true if given type represents a primitive java type
     */
    public static boolean isNumberType(JavaType javaType) {
        return Number.class.isAssignableFrom(javaType.getNullableClass());
    }

    /**
     * They are all the INTEGER number type.
     * <p>
     * int(Integer), long(Long), short(Short), byte(Byte)
     * </p>
     * 
     * @param type
     * @return true if given type represents a primitive java type
     */
    public static boolean isNumberIntType(JavaType javaType) {
        return javaType == BYTE || javaType == INTEGER || javaType == SHORT || javaType == LONG;
    }

    /**
     * 
     * Return the default value for a given type.
     * 
     * @param type
     * @return
     */
    public static String getDefaultValueFromJavaType(String type) {
        if (type == null) {
            throw new IllegalArgumentException();
        }
        if (isJavaPrimitiveType(type)) {
            if (type.equals(JAVA_PRIMITIVE_CHAR)) {
                return DEFAULT_CHAR;
            } else if (type.equals(JAVA_PRIMITIVE_BOOLEAN)) {
                return DEFAULT_BOOLEAN;
            } else {
                return DEFAULT_NUMBER;
            }
        } else {
            return NULL;
        }
    }

    /**
     * 
     * Return the default value for a given type, if the given parameter "defaultValue" is set, this value is returned.
     * 
     * @param type
     * @return string value if the case is valid or the default value is valid. If the type is primitive and the
     * <code>defaultValue</code> is not set, java null is returned.
     */
    public static String getDefaultValueFromJavaType(String type, String defaultValue) {
        if (defaultValue != null && defaultValue.length() > 0) {
            return defaultValue;
        } else {
            return getDefaultValueFromJavaType(type);
        }

    }

    /**
     * 
     * Return the default value for a given type.
     * 
     * @param type
     * @return
     */
    public static String getDefaultValueFromJavaIdType(String idType, boolean nullable) {
        String typeToGenerate = getTypeToGenerate(idType, nullable);
        return getDefaultValueFromJavaType(typeToGenerate);
    }

    /**
     * 
     * Return the default value for a given type.
     * 
     * @param type
     * @return
     */
    public static String getDefaultValueFromJavaIdType(String idType, boolean nullable, String defaultValue) {
        String typeToGenerate = getTypeToGenerate(idType, nullable);
        return getDefaultValueFromJavaType(typeToGenerate, defaultValue);
    }

    public static JavaType getDefaultJavaType() {
        return STRING;
    }

    public static void main(String[] args) {

        JavaType[] javaTypes2 = getJavaTypes();
        for (JavaType javaType : javaTypes2) {
            System.out.println("id = " + javaType.getId()); //$NON-NLS-1$
        }

        System.out.println(Messages.getString("JavaTypesManager.integer") + isNumberType(INTEGER)); //$NON-NLS-1$
        System.out.println(Messages.getString("JavaTypesManager.bigDecimal") + isNumberType(BIGDECIMAL)); //$NON-NLS-1$
        System.out.println(Messages.getString("JavaTypesManager.list") + isNumberType(LIST)); //$NON-NLS-1$
        System.out.println(Messages.getString("JavaTypesManager.string") + isNumberType(STRING)); //$NON-NLS-1$

        System.out.println(JavaTypesManager.getJavaTypeFromName("String")); //$NON-NLS-1$
        System.out.println(JavaTypesManager.getJavaTypeFromName("int")); //$NON-NLS-1$
        System.out.println(JavaTypesManager.getJavaTypeFromName("Integer")); //$NON-NLS-1$
        System.out.println(JavaTypesManager.getJavaTypeFromName("integer")); //$NON-NLS-1$
        System.out.println(JavaTypesManager.getJavaTypeFromName("Object")); //$NON-NLS-1$

        System.out.println(JavaTypesManager.getDefaultValueFromJavaType("boolean")); //$NON-NLS-1$
        System.out.println(JavaTypesManager.getDefaultValueFromJavaIdType("id_Boolean", false)); //$NON-NLS-1$
        System.out.println(JavaTypesManager.getDefaultValueFromJavaIdType("id_Double", true)); //$NON-NLS-1$
        System.out.println(JavaTypesManager.getDefaultValueFromJavaIdType("id_Double", false)); //$NON-NLS-1$
        System.out.println(JavaTypesManager.getDefaultValueFromJavaIdType("id_Integer", true)); //$NON-NLS-1$

        System.out.println(JavaTypesManager.getDefaultValueFromJavaType("char")); //$NON-NLS-1$

    }

    public static Map<String, Map<String, List<DBTypeUtil>>> getJavaTypeMappingFromExtension() {
        return javaTypeMappingFromExtension;
    }

    /**
     * the numerical types with id_.
     */
    public final static String[] NUMBERS = new String[] { JavaTypesManager.INTEGER.getId(), JavaTypesManager.FLOAT.getId(),
            JavaTypesManager.DOUBLE.getId(), JavaTypesManager.LONG.getId(), JavaTypesManager.SHORT.getId(),
            JavaTypesManager.BIGDECIMAL.getId(), JavaTypesManager.BYTE.getId() };

    public static boolean isNumber(String type) {
        return ArrayUtils.contains(NUMBERS, type);
    }

    public static boolean isString(String type) {
        return JavaTypesManager.STRING.getId().equals(type);
    }

    public static boolean isBoolean(String type) {
        return JavaTypesManager.BOOLEAN.getId().equals(type);
    }
}
