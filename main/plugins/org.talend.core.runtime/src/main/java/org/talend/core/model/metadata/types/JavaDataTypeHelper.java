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
package org.talend.core.model.metadata.types;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

/**
 * DataTypeManager is used to determine the Java Type or the Talend Type of a string. TODO : This class don't determine
 * the type DATE.
 * 
 * $Id: DataTypeHelper.java 1155 2006-12-21 09:51:10Z amaumont $
 * 
 */
public final class JavaDataTypeHelper {

    /**
     * Default Constructor. Must not be used.
     */
    private JavaDataTypeHelper() {
    }

    /**
     * Constant to define the min and the max of type Int Float Double.
     */
    private static final int INT_MIN = -2147483648;

    private static final int INT_MAX = 2147483647;

    private static final float FLOAT_MIN = -1.4E-45f;

    private static final float FLOAT_MAX = 3.4E38f;

    private static final double DOUBLE_MIN = 4.9E-324d;

    private static final double DOUBLE_MAX = 1.7E308d;

    /**
     * getTypeOfValue return STRING, CHAR, NUMBER, LONG, FLOAT, DOUBLE.
     * 
     * @param value
     * @return string or null if the value.equals("")
     */
    public static String getTalendTypeOfValue(final String value) {
        String javaType = getJavaTypeOfValue(value);
        if (javaType == null) {
            // if (javaType.equals("Integer")) {
            // return "NUMBER";
            // } else if (javaType.equals("Character")) {
            // return "CHAR";
            // }
            // } else {
            return JavaTypesManager.STRING.getId();
        }
        return javaType;
    }

    /**
     * getTypeOfValue return String, Character, Integer, Long, Float, Double.
     * 
     * @param value
     * @return string or null if the value.equals("")
     */
    public static String getJavaTypeOfValue(final String value) {

        // empty value => type is undef
        if (value.equals("")) { //$NON-NLS-1$
            return null;
        }

        // only 1 char => type is char or int
        if (value.length() == 1) {
            try {
                // * int Entier -2 147 483 648 � 2 147 483 647
                Integer nbr = Integer.parseInt(value);
                if ((nbr >= INT_MIN) && (nbr <= INT_MAX)) {
                    return JavaTypesManager.INTEGER.getId();
                }
            } catch (Exception e) {
                //
            }
            return JavaTypesManager.CHARACTER.getId();
        }

        // see the bug "6680",qli comment.

        if (value.equals(JavaTypesManager.DEFAULT_BOOLEAN) || value.equals(JavaTypesManager.ANOTHER_BOOLEAN)) {
            return JavaTypesManager.BOOLEAN.getId();
        }

        if (isDateOne(value) || isDateTwo(value)) {
            return JavaTypesManager.DATE.getId();
        }

        // SPECIFIQUE USE CASE (integer begin by 0; multiple dot use ; use of char E in scientific notation)
        //
        if (!isNumber(value)) {
            // Warning : 1.7E38 is interpreted like a float !
            return JavaTypesManager.STRING.getId();
        }
        //
        // first char is 0 and no dot just after => force type String needed
        if (value.substring(0, 1).equals("0") && (!value.substring(1, 2).equals("."))) { //$NON-NLS-1$ //$NON-NLS-2$
            return JavaTypesManager.STRING.getId();
        }
        //
        // content more one dot => String
        if (value.contains(".")) { //$NON-NLS-1$
            if (value.substring(value.indexOf(".") + 1, value.length()).contains(".")) { //$NON-NLS-1$ //$NON-NLS-2$
                return JavaTypesManager.STRING.getId();
            }
        }
        // END SPECIFIQUE USE CASE

        // content only a dot => float or double
        if (value.contains(".")) { //$NON-NLS-1$
            try {
                Float nbrFloat = Float.parseFloat(value);
                if ((!nbrFloat.toString().equals("Infinity")) && (!nbrFloat.toString().equals("-Infinity"))) { //$NON-NLS-1$ //$NON-NLS-2$
                    if ((nbrFloat >= FLOAT_MIN) && (nbrFloat <= FLOAT_MAX)) {
                        return JavaTypesManager.FLOAT.getId();
                    }
                }

                try {
                    // * double flottant double 4.9*10 -324 � 1.7*10 308
                    Double nbrDouble = Double.parseDouble(value);
                    if ((!nbrDouble.toString().equals("Infinity")) && (!nbrDouble.toString().equals("-Infinity"))) { //$NON-NLS-1$ //$NON-NLS-2$
                        if ((nbrDouble >= DOUBLE_MIN) && (nbrDouble <= DOUBLE_MAX)) {
                            return JavaTypesManager.DOUBLE.getId();
                        }
                    }
                } catch (Exception e) {
                    // 
                }

            } catch (Exception e) {
                // 
            }
        }

        // is not a char, not a float : parseMethod is usable
        try {
            Integer.parseInt(value);
            return JavaTypesManager.INTEGER.getId();
        } catch (Exception e) {
            // 
        }
        try {
            Long.parseLong(value);
            return JavaTypesManager.LONG.getId();
        } catch (Exception e) {
            // 
        }
        try {
            Double.parseDouble(value);
            return JavaTypesManager.DOUBLE.getId();
        } catch (Exception e) {
            // 
        }

        // by default the type is string
        return JavaTypesManager.STRING.getId();
    }

    /**
     * 
     * qli comment the method "isDateOne".
     * 
     * @param value
     * 
     * @return boolean
     */
    private static boolean isDateOne(String value) {
        if (value != null) {
            try {
                String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS Z";//$NON-NLS-1$
                value = value.replace("Z", " UTC");//$NON-NLS-1$//$NON-NLS-2$
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

                Date dt = simpleDateFormat.parse(value);
                simpleDateFormat.format(dt);
                return true;
            } catch (ParseException pe) {
                return false;
            }
        } else
            return false;
    }

    /**
     * 
     * qli comment the method "isDateTwo".
     * 
     * @param value
     * 
     * @return boolean
     */
    private static boolean isDateTwo(String value) {
        if (value != null) {
            try {
                String dateFormat = "yyyy-MM-dd";//$NON-NLS-1$
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

                Date dt = simpleDateFormat.parse(value);
                simpleDateFormat.format(dt);
                return true;
            } catch (ParseException pe) {
                return false;
            }
        } else
            return false;
    }

    /**
     * getGlobalType compare two type and determine the common Type.
     * 
     * @param type1
     * @param type2
     * @return
     */
    public static String getCommonType(final String type1, final String type2) {
        return getTypeByPriority(type1, type2);

        // JavaType idType = null;
        // // String
        // idType = getCommonType(type1, type2, JavaTypesManager.STRING);
        // if (idType != null) {
        // return idType.getId();
        // }
        // // Integer
        // idType = getCommonType(type1, type2, JavaTypesManager.INTEGER);
        // if (idType != null) {
        // return idType.getId();
        // }
        // // Double
        // idType = getCommonType(type1, type2, JavaTypesManager.DOUBLE);
        // if (idType != null) {
        // return idType.getId();
        // }
        // // float
        // idType = getCommonType(type1, type2, JavaTypesManager.FLOAT);
        // if (idType != null) {
        // return idType.getId();
        // }
        // // Long
        // idType = getCommonType(type1, type2, JavaTypesManager.LONG);
        // if (idType != null) {
        // return idType.getId();
        // }
        // // Character
        // idType = getCommonType(type1, type2, JavaTypesManager.CHARACTER);
        // if (idType != null) {
        // return idType.getId();
        // }
        // return JavaTypesManager.STRING.getId();
    }

    private static JavaType getCommonType(final String type1, final String type2, JavaType testType) {
        if ((type1 == testType.getLabel()) || (type2 == testType.getLabel()) || (type1 == testType.getId())
                || (type2 == testType.getId())) {
            return testType;
        }
        return null;
    }

    // Function to test whether the string is valid number or not
    public static boolean isNumber(String value) {
        Perl5Compiler compiler = new Perl5Compiler();
        Perl5Matcher matcher = new Perl5Matcher();
        Pattern pattern = null;

        String strValidRealPattern = "^([-]|[.]|[-.]|[0-9])[0-9]*[.]*[0-9]+$"; //$NON-NLS-1$
        String strValidIntegerPattern = "^([-]|[0-9])[0-9]*$"; //$NON-NLS-1$
        String regex = "(" + strValidRealPattern + ")|(" + strValidIntegerPattern + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        try {
            pattern = compiler.compile(regex);
            if (matcher.contains(value, pattern)) {
                return matcher.matches(value, pattern);
            }
        } catch (MalformedPatternException e) {
            // do nothing
        }
        return false;
    }

    public static String getTypeByPriority(String type1, String type2) {
        EJavaTypePriority priority1 = EJavaTypePriority.getJavaTypePriority(type1);
        EJavaTypePriority priority2 = EJavaTypePriority.getJavaTypePriority(type2);
        if (priority1.isNumberType() != priority2.isNumberType()) {
            return EJavaTypePriority.STRING.getIdName();
        }
        if (priority1.getPriority() > priority2.getPriority()) {
            return priority1.getIdName();
        } else {
            return priority2.getIdName();
        }
    }

    /**
     * DOC nrousseau GuessSchemaUtil class global comment. Detailled comment
     */
    private enum EJavaTypePriority {
        STRING("id_String", 10, false), //$NON-NLS-1$
        BOOLEAN("id_Boolean", 9, false), //$NON-NLS-1$
        INTEGER("id_Integer", 1, true), //$NON-NLS-1$
        LONG("id_Long", 2, true), //$NON-NLS-1$
        CHARACTER("id_Character", 1, false), //$NON-NLS-1$
        DATE("id_Date", 9, false), //$NON-NLS-1$
        FLOAT("id_Float", 3, true), //$NON-NLS-1$
        DOUBLE("id_Double", 4, true), //$NON-NLS-1$
        BIGDECIMAL("id_BigDecimal", 5, true); //$NON-NLS-1$

        private String idName;

        private int priority;

        private boolean numberType;

        private EJavaTypePriority(String name, int priority, boolean numberType) {
            this.idName = name;
            this.priority = priority;
            this.numberType = numberType;
        }

        private static EJavaTypePriority getJavaTypePriority(String idName) {
            for (EJavaTypePriority type : EJavaTypePriority.values()) {
                if (type.getIdName().equals(idName)) {
                    return type;
                }
            }
            return STRING;
        }

        /**
         * Getter for idName.
         * 
         * @return the idName
         */
        public String getIdName() {
            return this.idName;
        }

        /**
         * Sets the idName.
         * 
         * @param idName the idName to set
         */
        public void setIdName(String idName) {
            this.idName = idName;
        }

        /**
         * Getter for priority.
         * 
         * @return the priority
         */
        public int getPriority() {
            return this.priority;
        }

        /**
         * Sets the priority.
         * 
         * @param priority the priority to set
         */
        public void setPriority(int priority) {
            this.priority = priority;
        }

        /**
         * Getter for numberType.
         * 
         * @return the numberType
         */
        public boolean isNumberType() {
            return this.numberType;
        }

        /**
         * Sets the numberType.
         * 
         * @param numberType the numberType to set
         */
        public void setNumberType(boolean numberType) {
            this.numberType = numberType;
        }
    };

}
