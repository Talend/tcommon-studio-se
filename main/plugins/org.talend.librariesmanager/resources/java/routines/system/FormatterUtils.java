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
package routines.system;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.Locale;

import routines.TalendDate;

public class FormatterUtils {

    /**
     * format every type object, use runtime type checking
     * @param obj
     * @param pattern
     * @return the string content
     */
    public static String fm(Object obj, String pattern) {
        if(obj == null) {
            return null;
        }

        //the order is on purpose here for the appear rank
        if(obj instanceof String) {
            return obj.toString();
        } else if(obj instanceof Integer) {//not call the format(int i, String pattern) method to avoid auto cast from Integer to int
            return obj.toString();
        } else if(obj instanceof Long) {
            return obj.toString();
        } else if(obj instanceof Date) {
            return TalendDate.formatDate(pattern == null ? Constant.dateDefaultPattern : pattern, (Date)obj);
        } else if(obj instanceof Boolean) {
          return obj.toString();
        } else if(obj instanceof BigDecimal) {
            return ((BigDecimal)obj).toPlainString();
        } else if(obj instanceof Double) {
            return obj.toString();
        } else if(obj instanceof Float) {
            return obj.toString();
        } else if(obj instanceof Character) {
            return obj.toString();
        } else if(obj instanceof char[]) {
            return format((char[])obj, null);
        } else if(obj instanceof byte[]) {
            return format((byte[])obj, null);
        }

        return obj.toString();
    }

    public static String format(Object obj, String pattern) {
        return (obj == null) ? null : obj.toString();
    }

    public static String format(Date date, String pattern) {
        if (date != null) {
            return TalendDate.formatDate(pattern == null ? Constant.dateDefaultPattern : pattern, date);
        } else {
            return null;
        }
    }

    public static String format(BigDecimal decimal, String pattern) {
        if (decimal == null) {
            return null;
        }
        return decimal.toPlainString();
    }

    public static String format_BigDecimal(BigDecimal decimal, Integer scale) {
        if (decimal == null) {
            return null;
        }

        if (scale == null) {
            return decimal.toPlainString();
        }

        return decimal.setScale(scale, RoundingMode.HALF_UP).toPlainString();
    }

    public static String format(byte data[], String pattern) {
        return Charset.defaultCharset().decode(java.nio.ByteBuffer.wrap(data)).toString();
    }

    public static String format(char data[], String pattern) {
        return String.valueOf(data);
    }

    public static String format(boolean b, String pattern) {
        return String.valueOf(b);
    }

    public static String format(char c, String pattern) {
        return String.valueOf(c);
    }

    public static String format(int i, String pattern) {
        return String.valueOf(i);
    }

    public static String format(long l, String pattern) {
        return String.valueOf(l);
    }

    public static String format(float f, String pattern) {
        return String.valueOf(f);
    }

    public static String format(double d, String pattern) {
        return String.valueOf(d);
    }

    public static String format_Date(java.util.Date date, String pattern) {
        if (date != null) {
            return TalendDate.formatDate(pattern == null ? Constant.dateDefaultPattern : pattern, date);
        } else {
            return null;
        }
    }

    /**
     * Formats a Date into a date/time string under an user specified timezone.
     *
     * @param date the time value to be formatted into a time string.
     * @param pattern the pattern to format.
     * @return the formatted time string.
     *
     * {talendTypes} String
     *
     * {Category} TalendDate
     *
     * {param} string("yyyy-MM-dd HH:mm:ss") pattern : the pattern to format
     *
     * {param} date(myDate) date : the time value to be formatted into a time string
     *
     * {example} formatDate("yyyy-MM-dd", new Date()) #
     */
    public static String format_DateInUTC(java.util.Date date, String pattern) {
        if (date != null) {
            return TalendDate.formatDateInUTC(pattern == null ? Constant.dateDefaultPattern : pattern, date);
        } else {
            return null;
        }
    }

    public static String format_Date_Locale(java.util.Date date, String pattern, String locale) {
        if (date != null) {
            return TalendDate.formatDateLocale(pattern == null ? Constant.dateDefaultPattern : pattern, date, locale);
        } else {
            return null;
        }
    }

    /**
     * in order to transform the number "1234567.89" to string 123,456.89
     */
    public static String format_Number(String s, String thousandsSeparator, String decimalSeparator) {
        Character thousandsSeparatorChar = null;
        if(thousandsSeparator!=null && !thousandsSeparator.isEmpty()) {
            thousandsSeparatorChar = thousandsSeparator.charAt(0);
        }

        Character decimalSeparatorChar = null;
        if(decimalSeparator!=null && !decimalSeparator.isEmpty()) {
            decimalSeparatorChar = decimalSeparator.charAt(0);
        }

        return format_Number(s, thousandsSeparatorChar, decimalSeparatorChar);
    }

    /**
     * in order to transform the number "1234567.89" to string 123,456.89
     */
    public static String format_Number(String s, Character thousandsSeparator, Character decimalSeparator) {
        if (s == null) {
            return null;
        }
        String result = s;
        int decimalIndex = s.indexOf("."); //$NON-NLS-1$

        if (decimalIndex == -1) {
            if (thousandsSeparator != null) {
                return formatNumber(result, thousandsSeparator);
            } else {
                return result;
            }
        }

        if (thousandsSeparator != null) {
            result = formatNumber(s.substring(0, decimalIndex), thousandsSeparator);
        } else {
            result = s.substring(0, decimalIndex);
        }

        if (decimalSeparator != null) {
            result += (s.substring(decimalIndex)).replace('.', decimalSeparator);
        } else {
            result += s.substring(decimalIndex);
        }
        return result;
    }

    private static String formatNumber(String s, char thousandsSeparator) {

        StringBuilder sb = new StringBuilder(s);
        int index = sb.length();

        index = index - 3;
        while (index > 0 && sb.charAt(index - 1) != '-') {
            sb.insert(index, thousandsSeparator);
            index = index - 3;
        }

        return sb.toString();
    }

    /**
     * Bug 13352 by nsun: always return the format using "." for decimal separator.
     */
    public static String unformat_Number(String s, Character thousandsSeparator, Character decimalSeparator) {
        if (s == null) {
            return null;
        }
        String result = s;
        int decimalIndex = s.indexOf(decimalSeparator);
        if (decimalIndex == -1) {
            if (thousandsSeparator != null) {
                return unformatNumber(result, thousandsSeparator);
            } else {
                return result;
            }
        }
        if (thousandsSeparator != null) {
            result = unformatNumber(s.substring(0, decimalIndex), thousandsSeparator);
        } else {
            result = s.substring(0, decimalIndex);
        }

        if (decimalSeparator != null) {
            if ("\\.".equals(decimalSeparator)) {
                result += (s.substring(decimalIndex)).replace(thousandsSeparator, decimalSeparator);
            } else {
                result += (s.substring(decimalIndex)).replace(decimalSeparator, '.');
            }
        } else {
            result += s.substring(decimalIndex);
        }
        return result;
    }

    private static String unformatNumber(String str, Character thousandsSeparator) {
        StringBuilder returnString = new StringBuilder();
        String separator = thousandsSeparator.toString();
        if (".".equals(separator)) {
            separator = "\\.";
        }
        String[] s = str.split(separator);
        for (String part : s) {
            returnString.append(part);
        }
        return returnString.toString();
    }

    private static final DecimalFormat df = new DecimalFormat("#.####################################", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
    static {
        df.setRoundingMode(RoundingMode.HALF_UP);
    }

    /**
     * the number format to avoid scientific notation and avoid locale-related and do best to keep precision
     */
    public static String formatNumber(Object number) {
        if(number == null) {
            return null;
        }
        //df.format(float/Float) will call Float.doubleValue, it will do some bad thing.
        if(number instanceof Float) {
            return formatUnwithE(number);
        }
        return df.format(number);
    }

    //overload it for performance to avoid auto convert to Double object
    public static String formatNumber(double number) {
        return df.format(number);
    }

    //overload it for performance to avoid auto convert to Float object
    public static String formatNumber(float number) {
        return formatUnwithE(number);
    }

    /**
     * DOC Administrator Comment method "formatUnwithE". In java when double more than six decimal that use toString
     * will rentru contains E scientific natation.
     *
     * @param arg like: double falot String .... e.g:1.0E-8
     * @return 0.00000001 as String
     */
    public static String formatUnwithE(Object arg) {
        String doubleString = String.valueOf(arg);
        int index = doubleString.indexOf("E");
        if (index != -1) {
            return (new BigDecimal(doubleString)).toPlainString();
        }
        return doubleString;
    }

}
