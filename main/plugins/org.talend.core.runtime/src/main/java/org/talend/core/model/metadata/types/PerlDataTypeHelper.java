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

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

/**
 * PerlDataTypeHelper is used to determine the Perl Type or the Talend Type of a string. TODO : This class don't
 * determine the type DATE.
 * 
 * $Id: PerlDataTypeHelper.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public final class PerlDataTypeHelper {

    /**
     * Default Constructor. Must not be used.
     */
    private PerlDataTypeHelper() {
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
     * 
     * cLi Comment method "getNewTalendTypeOfValue".
     * 
     * @return return new Perl Type, such as string, decimal, int
     */
    public static String getNewTalendTypeOfValue(final String value) {
        if (value == null) {
            return null;
        }
        String oldTalendType = getTalendTypeOfValue(value);
        if (oldTalendType != null) {
            if (oldTalendType.equals("NUMBER") || oldTalendType.equals("Integer")) { //$NON-NLS-1$ //$NON-NLS-2$
                return oldTalendType;
            }
        }
        return PerlTypesManager.getNewTypeName(oldTalendType.toLowerCase());
    }

    /**
     * getTypeOfValue return STRING, CHAR, NUMBER, LONG, FLOAT, DOUBLE.
     * 
     * @param value
     * @return string or null if the value.equals("")
     * @deprecated
     */
    public static String getTalendTypeOfValue(final String value) {
        String perlType = getPerlTypeOfValue(value);
        if (perlType != null) {
            if (perlType.equals("Integer")) { //$NON-NLS-1$
                return "NUMBER"; //$NON-NLS-1$
            } else if (perlType.equals("Character")) { //$NON-NLS-1$
                return "CHAR"; //$NON-NLS-1$
            }
        } else {
            return "String"; //$NON-NLS-1$
        }
        return perlType.toUpperCase();
    }

    /**
     * getTypeOfValue return String, Character, Integer, Long, Float, Double.
     * 
     * @param value
     * @return string or null if the value.equals("")
     * @deprecated
     */
    public static String getPerlTypeOfValue(final String value) {

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
                    return "Integer"; //$NON-NLS-1$
                }
            } catch (Exception e) {
                //
            }
            return "Character"; //$NON-NLS-1$
        }

        // SPECIFIQUE USE CASE (integer begin by 0; multiple dot use ; use of char E in scientific notation)
        //
        if (!isNumber(value)) {
            // Warning : 1.7E38 is interpreted like a float !
            return "String"; //$NON-NLS-1$
        }
        //
        // first char is 0 and no dot just after => force type String needed
        if (value.substring(0, 1).equals("0") && (!value.substring(1, 2).equals("."))) { //$NON-NLS-1$ //$NON-NLS-2$
            return "String"; //$NON-NLS-1$
        }
        //
        // content more one dot => String
        if (value.contains(".")) { //$NON-NLS-1$
            if (value.substring(value.indexOf(".") + 1, value.length()).contains(".")) { //$NON-NLS-1$ //$NON-NLS-2$
                return "String"; //$NON-NLS-1$
            }
        }
        // END SPECIFIQUE USE CASE

        // content only a dot => float or double
        if (value.contains(".")) { //$NON-NLS-1$
            try {
                Float nbrFloat = Float.parseFloat(value);
                if ((!nbrFloat.toString().equals("Infinity")) && (!nbrFloat.toString().equals("-Infinity"))) { //$NON-NLS-1$ //$NON-NLS-2$
                    if ((nbrFloat >= FLOAT_MIN) && (nbrFloat <= FLOAT_MAX)) {
                        return "Float"; //$NON-NLS-1$
                    }
                }

                try {
                    // * double flottant double 4.9*10 -324 � 1.7*10 308
                    Double nbrDouble = Double.parseDouble(value);
                    if ((!nbrDouble.toString().equals("Infinity")) && (!nbrDouble.toString().equals("-Infinity"))) { //$NON-NLS-1$ //$NON-NLS-2$
                        if ((nbrDouble >= DOUBLE_MIN) && (nbrDouble <= DOUBLE_MAX)) {
                            return "Double"; //$NON-NLS-1$
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
            return "Integer"; //$NON-NLS-1$
        } catch (Exception e) {
            // 
        }
        try {
            Long.parseLong(value);
            return "Long"; //$NON-NLS-1$
        } catch (Exception e) {
            // 
        }
        try {
            Double.parseDouble(value);
            return "Double"; //$NON-NLS-1$
        } catch (Exception e) {
            // 
        }

        // by default the type is string
        return "String"; //$NON-NLS-1$
    }

    /**
     * getGlobalType compare two type and determine the common Type.
     * 
     * @param type1
     * @param type2
     * @return
     * @deprecated not use the old type.
     */
    public static String getCommonType(final String type1, final String type2) {
        if ((type1 == "String") || (type2 == "String")) { //$NON-NLS-1$ //$NON-NLS-2$
            return "String"; //$NON-NLS-1$
        }
        if ((type1 == "Double") || (type2 == "Double")) { //$NON-NLS-1$ //$NON-NLS-2$
            return "Double"; //$NON-NLS-1$
        }
        if ((type1 == "Float") || (type2 == "Float")) { //$NON-NLS-1$ //$NON-NLS-2$
            return "Float"; //$NON-NLS-1$
        }
        if ((type1 == "Long") || (type2 == "Long")) { //$NON-NLS-1$ //$NON-NLS-2$
            return "Long"; //$NON-NLS-1$
        }
        return "String"; //$NON-NLS-1$
    }

    public static String getNewCommonType(final String type1, final String type2) {
        String newType1 = PerlTypesManager.getNewTypeName(type1);
        String newType2 = PerlTypesManager.getNewTypeName(type2);
        if (PerlTypesManager.STRING.equals(newType1) || PerlTypesManager.STRING.equals(newType2)) {
            return PerlTypesManager.STRING;
        }
        if (PerlTypesManager.DECIMAL.equals(newType1) || PerlTypesManager.DECIMAL.equals(newType2)) {
            return PerlTypesManager.DECIMAL;
        }
        return PerlTypesManager.STRING;
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

}
