// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.utils.format;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * @author scorreia
 *
 * This class contains utilities for formatting strings.
 */
public final class StringFormatUtil {

    public static final int PERCENT = 0;

    public static final int INT_NUMBER = 1;

    public static final int DOUBLE_NUMBER = 2;

    public static final int OTHER = 99999;

    /**
     * Method "padString".
     *
     * @param stringToPad the string to which white spaces must be added.
     * @param size the size of the new string.
     * @return the given string completed with white spaces up the the given size.
     */
    public static String padString(String stringToPad, int size) {
        return String.format("%" + size + "s", stringToPad); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * DOC Zqin Comment method "format".
     *
     * @param input the object that was formated.
     * @param style the style of formated, it should be 0, 1,2,99999.
     * @return the formated object.
     */
    public static Object format(Object input, int style) {

        try {
            if (checkInput(input)) {
                DecimalFormat format = null;

                // MOD qiongli 2011-6-1 bug 21589,if input is out of the decimal precision,replace it with threshold.
                BigDecimal zero = new BigDecimal(0);
                BigDecimal temp = new BigDecimal(input.toString());
                BigDecimal min = new BigDecimal(10E-5);
                BigDecimal max = new BigDecimal(9999 * 10E-5);
                boolean isUseScientific = false;
                switch (style) {
                case PERCENT:
                    if (temp.compareTo(min) == -1 && temp.compareTo(zero) == 1) {
                        isUseScientific = true;
                    } else if (temp.compareTo(max) == 1 && temp.compareTo(new BigDecimal(1)) == -1) {
                        input = max.toString();
                    }
                    format = (DecimalFormat) DecimalFormat.getPercentInstance(Locale.ENGLISH);
                    format.applyPattern("0.00%"); //$NON-NLS-1$
                    break;
                case INT_NUMBER:
                    min = new BigDecimal(10E-3);
                    if (temp.compareTo(min) == -1 && temp.compareTo(zero) == 1) {
                        isUseScientific = true;
                    }
                    format = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.ENGLISH);
                    format.applyPattern("0"); //$NON-NLS-1$
                    break;
                case DOUBLE_NUMBER:
                    min = new BigDecimal(10E-3);
                    if (temp.compareTo(min) == -1 && temp.compareTo(zero) == 1) {
                        isUseScientific = true;
                    }
                    format = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.ENGLISH);
                    format.applyPattern("0.00"); //$NON-NLS-1$
                    break;
                default:
                    format = (DecimalFormat) DecimalFormat.getInstance(Locale.getDefault());
                    return format.parse(input.toString());
                }
                if (isUseScientific) {
                    format.applyPattern("0.###E0%"); //$NON-NLS-1$
                }

                return format.format(new Double(input.toString()));
            } else {
                return input;
            }
        } catch (Exception e) {
            return input;
        }
    }

    /**
     * DOC Zqin Comment method "formatPersent".
     *
     * @param input the object that was formated.
     * @return the persent form of this valid input object
     */
    public static String formatPersent(Object input) {

        if (checkInput(input)) {
            Double db = new Double(input.toString());
            DecimalFormat format = (DecimalFormat) DecimalFormat.getPercentInstance(Locale.ENGLISH);
            format.applyPattern("0.00%"); //$NON-NLS-1$
            return format.format(db);
        }

        return null;
    }

    /**
     * DOC Zqin Comment method "formatDouble".
     *
     * @param input the object that was formated.
     * @return the Double form of this valid input object.
     */
    public static Double formatDouble(Object input) {

        if (checkInput(input)) {
            Double db = new Double(input.toString());
            DecimalFormat format = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.ENGLISH);
            format.applyPattern("0.00"); //$NON-NLS-1$
            return Double.valueOf(format.format(db));
        }

        return null;
    }

    /**
     * DOC Zshen Comment method "formatDouble".
     *
     * @param input the object that was formated.
     * @return the Double form of this valid input object and to retain four decimal places
     */
    public static Double formatFourDecimalDouble(Object input) {

        if (checkInput(input)) {
            Double db = new Double(input.toString());
            DecimalFormat format = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.ENGLISH);
            format.applyPattern("0.0000"); //$NON-NLS-1$
            return Double.valueOf(format.format(db));
        }

        return null;
    }

    /**
     * DOC Zqin Comment method "parseDouble".
     *
     * @param input the object that was parsed.
     * @return the Double object represents this input
     */
    public static Double parseDouble(Object input) {
        if (input != null) {
            // MOD yyi 2010-04-09 12483 Locale.getDefault() to Locale.US
            DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance(Locale.US);
            try {
                Number number = format.parse(input.toString());
                return number.doubleValue();
            } catch (ParseException e) {
                return Double.NaN;
            }

        }

        return null;
    }

    /**
     * DOC Zqin Comment method "checkInput".
     *
     * @param input the object that was formated.
     * @return true if the input is valid, else false;
     */
    private static boolean checkInput(Object input) {
        if (input == null || "".equals(input)) { //$NON-NLS-1$
            return false;
        } else {
            Double db = new Double(input.toString());
            if (db.equals(Double.NaN)) {
                return false;
            }
        }

        return true;
    }

    public static Double formatPercentDecimalDouble(Object input) {
        if (checkInput(input)) {
            BigDecimal bd1 = new BigDecimal(input.toString());
            BigDecimal bd2 = new BigDecimal("100"); //$NON-NLS-1$
            return bd1.multiply(bd2).doubleValue();
        }
        return null;
    }
}
