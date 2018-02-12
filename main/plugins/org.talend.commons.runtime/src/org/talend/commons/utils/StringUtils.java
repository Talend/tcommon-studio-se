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
package org.talend.commons.utils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.talend.commons.i18n.internal.Messages;

/**
 * 
 * Utility class for strings. <br/>
 * 
 * $Id$
 * 
 */
public class StringUtils {

    public static String repeat(String str, int repeat) {
        return org.apache.commons.lang.StringUtils.repeat(str, repeat);
    }

    public static String join(Object[] array, String separator) {
        return org.apache.commons.lang.StringUtils.join(array, separator);
    }

    public static String[] split(String string, char separator) {
        return org.apache.commons.lang.StringUtils.split(string, separator);
    }

    public static String replace(String text, String repl, String with) {
        return org.apache.commons.lang.StringUtils.replace(text, repl, with);
    }

    public static List<String> splitAsList(String string, char separator) {
        String[] strings = org.apache.commons.lang.StringUtils.split(string, separator);
        return Arrays.asList(strings);
    }

    public static String capitalize(String str) {
        return org.apache.commons.lang.StringUtils.capitalize(str);
    }

    public static String getMysqlProtectedColumnName(String colname) {
        return "`" + colname + "`"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    public static String getPostgresqlProtectedColumnName(String colname) {
        return "\\\"" + colname + "\\\""; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Extract string between the first delimiter and the second delimiter.
     * 
     * @param text
     * @param delimiter
     * @return
     */
    public static String extractFirstDelimitedString(String text, String delimiter) {

        String returned = ""; //$NON-NLS-1$

        int start = text.indexOf(delimiter, 0);

        if (start != -1) {

            int end = text.indexOf(delimiter, start + 1);

            if (end != -1) {
                returned = text.substring(start + 1, end);
            }
        }

        return returned;
    }

    public static String protectMetachar(String input) {

        input = replace(input, "\\", "\\\\\\\\"); //$NON-NLS-1$ //$NON-NLS-2$
        input = replace(input, "+", "\\\\+"); //$NON-NLS-1$ //$NON-NLS-2$
        input = replace(input, ".", "\\\\."); //$NON-NLS-1$ //$NON-NLS-2$
        input = replace(input, "[", "\\\\["); //$NON-NLS-1$ //$NON-NLS-2$
        input = replace(input, "]", "\\]"); //$NON-NLS-1$ //$NON-NLS-2$
        input = replace(input, "(", "\\\\("); //$NON-NLS-1$ //$NON-NLS-2$
        input = replace(input, ")", "\\\\)"); //$NON-NLS-1$ //$NON-NLS-2$
        input = replace(input, "^", "\\\\^"); //$NON-NLS-1$ //$NON-NLS-2$
        input = replace(input, "$", "\\\\$"); //$NON-NLS-1$ //$NON-NLS-2$

        return input;
    }

    public static String removeSpecialCharsForPackage(String input) {
        input = input.replaceAll(" ", ""); //$NON-NLS-1$ //$NON-NLS-2$
        input = input.replaceAll("/", "."); //$NON-NLS-1$ //$NON-NLS-2$
        input = input.replaceAll("&", "and"); //$NON-NLS-1$ //$NON-NLS-2$
        input = input.replaceAll("<", "lt"); //$NON-NLS-1$ //$NON-NLS-2$
        input = input.replaceAll(">", "gt"); //$NON-NLS-1$ //$NON-NLS-2$
        input = input.replaceAll("'", "apos"); //$NON-NLS-1$ //$NON-NLS-2$
        input = input.replaceAll("\"", "quot"); //$NON-NLS-1$ //$NON-NLS-2$
        input = input.replaceAll("\\(", "_"); //$NON-NLS-1$ //$NON-NLS-2$
        input = input.replaceAll("\\)", "_"); //$NON-NLS-1$ //$NON-NLS-2$
        return input;
    }

    public static String loadConvert(String inputStr, String language) {
        if (inputStr == null) {
            return null;
        }
        char[] inputChars = new char[inputStr.length()];
        inputStr.getChars(0, inputStr.length(), inputChars, 0);
        String loadConvert = null;
        if (language.equalsIgnoreCase("perl")) { //$NON-NLS-1$
            loadConvert = loadConvert(inputChars, 0, inputStr.length(), new char[inputStr.length()], 'x');
        } else {
            loadConvert = loadConvert(inputChars, 0, inputStr.length(), new char[inputStr.length()], 'u');
        }
        return loadConvert;
    }

    /*
     * Converts encoded &#92;uxxxx to unicode chars and changes special saved chars to their original forms. it can deal
     * with the unicode encode character and the octal encode character, for example: String s =
     * "\\u8C2D\\u5148\\u94FE\0022\22\022"; it is very useful in GUI, such as Text.getText(), and will to keep the input
     * string like: \22; it can support the perl String like this: \\x2\\x22
     */
    private static String loadConvert(char[] in, int off, int len, char[] convtBuf, char preHex) {
        boolean limitLengthForHex = false;
        int limitLength = 0;
        if (preHex == 'u') {
            limitLengthForHex = true; // in java, it must be \\uxxxx

            limitLength = 4;

        } else if (preHex == 'x') {

            limitLengthForHex = false; // in perl, it can like this \\xhh
            limitLength = 2;

        } else {
            throw new IllegalArgumentException(Messages.getString("StringUtils.IllegalArgument0")); //$NON-NLS-1$
        }

        if (convtBuf.length < len) {
            int newLen = len * 2;
            if (newLen < 0) {
                newLen = Integer.MAX_VALUE;
            }
            convtBuf = new char[newLen];
        }
        char aChar;
        char[] out = convtBuf;
        int outLen = 0;
        int end = off + len;

        while (off < end) {
            aChar = in[off++];
            if (aChar == '\\') {
                if (off < len
                        && (in[off] == preHex || in[off] == 't' || in[off] == 'r' || in[off] == 'n' || in[off] == 'f' || Character
                                .isDigit(in[off]))) {
                    aChar = in[off++];
                }
                if (aChar == preHex) {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < limitLength; i++) {

                        if (off == len) {
                            if (limitLengthForHex) {
                                throw new IllegalArgumentException(Messages.getString("StringUtils.IllegalArgument1")); //$NON-NLS-1$
                            } else {
                                break;
                            }
                        }

                        if (limitLengthForHex) {
                            aChar = in[off++];
                        } else if (Character.isDigit(in[off])) {
                            aChar = in[off++];
                        } else {
                            break;
                        }

                        switch (aChar) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            value = (value << 4) + aChar - '0';
                            break;
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                            value = (value << 4) + 10 + aChar - 'a';
                            break;
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                            value = (value << 4) + 10 + aChar - 'A';
                            break;
                        default:
                            throw new IllegalArgumentException(Messages.getString("StringUtils.IllegalArgument2")); //$NON-NLS-1$
                        }
                    }
                    out[outLen++] = (char) value;
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    } else if (Character.isDigit(aChar)) {

                        int value = 0;
                        for (int i = 0; i < 3; i++) {

                            switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                                value = (value << 3) + aChar - '0';
                                break;
                            default:
                                throw new IllegalArgumentException(Messages.getString("StringUtils.IllegalArgument3")); //$NON-NLS-1$
                            }

                            if (off < len && Character.isDigit(in[off])) {
                                aChar = in[off++];
                            } else {
                                break;
                            }

                        }

                        aChar = (char) value;

                    }
                    out[outLen++] = aChar;
                }
            } else {
                out[outLen++] = aChar;
            }
        }
        return new String(out, 0, outLen);
    }

    /**
     * The same as routines.system.StringUtils.escapeChar(...).
     * 
     * make \n to \\n. It will process these chars: \n, \r, \t, \f, \\, \", \', \b
     * 
     */
    public static String escapeChar(String s) {

        if (s == null) {
            return null;
        }

        int length = s.length();
        StringBuffer sb = new StringBuffer(length);
        for (int i = 0; i < length; i++) {

            char c = s.charAt(i);

            switch (c) {
            case '\n':
                sb.append("\\").append('n'); //$NON-NLS-1$
                break;
            case '\r':
                sb.append("\\").append('r'); //$NON-NLS-1$
                break;
            case '\t':
                sb.append("\\").append('t'); //$NON-NLS-1$
                break;
            case '\f':
                sb.append("\\").append('f'); //$NON-NLS-1$
                break;
            case '\b':
                sb.append("\\").append('b'); //$NON-NLS-1$
                break;
            case '\"':
                sb.append("\\").append('\"'); //$NON-NLS-1$
                break;
            case '\'':
                sb.append("\\").append('\''); //$NON-NLS-1$
                break;
            default:
                sb.append(c);
            }
        }

        return sb.toString();
    }

    public static String subString(String str, int len) {
        if (len < 0) {
            return str;
        }
        if ("".equals(str) || str == null) {
            return null;
        } else if (str.length() <= len) {
            return str;
        } else {
            return str.substring(0, len);
        }
    }

    private static final String OPEN_BRACE = "{";

    private static final String CLOSE_BRACE = "}";

    /**
     * Replace MessageFormat.format(..) because MessageFormat does'nt support single quote correctly This method replace
     * in the text all the patterns {0}, {1}, etc.. by all values in arguments array One or more values can be null, no
     * exception is thrown if a value is null.
     * 
     * @param pattern
     * @param arguments
     * @return
     * 
     */
    public static String replacePrms(String text, Object... arguments) {

        for (int i = 0; i < arguments.length; i++) {
            text = replace(text, OPEN_BRACE + i + CLOSE_BRACE, String.valueOf(arguments[i]));
        }
        return text;
    }

    private static final StringDigitComparator STR_DIGIT_COMPARATOR = new StringDigitComparator();

    static class StringDigitComparator implements Comparator<String> {

        final Pattern DIGIT_PATTERN = Pattern.compile("^(\\d+)"); //$NON-NLS-1$

        @Override
        public int compare(String s1, String s2) {
            return compare(s1, s2, true);
        }

        public int compare(String s1, String s2, boolean ignoreCase) {
            int n1 = s1.length(), n2 = s2.length();
            for (int i1 = 0, i2 = 0; i1 < n1 && i2 < n2; i1++, i2++) {
                char c1 = s1.charAt(i1);
                char c2 = s2.charAt(i2);
                // check the digit, all start by digit
                if (Character.isDigit(c1) && Character.isDigit(c2)) {
                    // get the digit
                    String tmp1 = s1.substring(i1);
                    String tmp2 = s2.substring(i2);
                    Matcher matcher1 = DIGIT_PATTERN.matcher(tmp1);
                    Matcher matcher2 = DIGIT_PATTERN.matcher(tmp2);
                    if (matcher1.find() && matcher2.find()) {
                        String digit1 = matcher1.group(1);
                        String digit2 = matcher2.group(1);
                        int d1 = Integer.parseInt(digit1);
                        int d2 = Integer.parseInt(digit2);
                        if (d1 == d2) { // same digit, check the left strings
                            String left1 = tmp1.substring(digit1.length());
                            String left2 = tmp2.substring(digit2.length());
                            return compare(left1, left2, ignoreCase);
                        } else {
                            return d1 - d2;
                        }

                    }

                }
                if (c1 != c2) {
                    if (ignoreCase) {
                        c1 = Character.toUpperCase(c1);
                        c2 = Character.toUpperCase(c2);
                        if (c1 != c2) {
                            c1 = Character.toLowerCase(c1);
                            c2 = Character.toLowerCase(c2);
                            if (c1 != c2) {
                                return c1 - c2;
                            }
                        }
                    } else {
                        return c1 - c2;
                    }

                }
            }
            return n1 - n2;
        }
    };

    public static int compareStringDigit(String s1, String s2, boolean ignoreCase) {
        return STR_DIGIT_COMPARATOR.compare(s1, s2, ignoreCase);
    }

    public static int compareStringDigit(String s1, String s2) {
        return STR_DIGIT_COMPARATOR.compare(s1, s2);
    }

}
