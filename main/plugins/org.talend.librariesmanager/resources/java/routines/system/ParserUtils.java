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

import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ParserUtils {

    public static List<String> parseTo_List(String s) {
        return parseTo_List(s, null);
    }

    /**
     * the source should be a string wrapped in chars[ ] which stands for it is a collection
     *
     * @param stSrc
     * @param fieldSep
     * @return
     */
    public static List<String> parseTo_List(final String strSrc, String fieldSep) {
        if (strSrc == null) {
            return null;
        }
        List<String> list = new ArrayList<String>();

        // the source string is wrap in [] which means it is a collection
        if ((fieldSep == null || "".equals(fieldSep)) || !(strSrc.startsWith("[") && strSrc.endsWith("]"))) {
            list.add(strSrc);
            return list;
        }
        String strTemp = strSrc.substring(1, strSrc.length() - 1); // remove the [ ]
        for (String str : strTemp.split(fieldSep, -1)) {
            list.add(str);
        }
        return list;
    }

    public static Character parseTo_Character(String s) {
        if (s == null) {
            return null;
        }
        return s.charAt(0);
    }

    public static char parseTo_char(String s) {
        return parseTo_Character(s);
    }

    public static Byte parseTo_Byte(String s) {
        if (s == null) {
            return null;
        }
        return Byte.decode(s).byteValue();
    }

    public static Byte parseTo_Byte(String s, boolean isDecode) {
        if (s == null) {
            return null;
        }
        if (isDecode) {
            return Byte.decode(s).byteValue();
        } else {
            return Byte.parseByte(s);
        }
    }

    public static byte parseTo_byte(String s) {
        return parseTo_Byte(s);
    }

    public static byte parseTo_byte(String s, boolean isDecode) {
        return parseTo_Byte(s, isDecode);
    }

    public static Double parseTo_Double(String s) {
        if (s == null) {
            return null;
        }
        return Double.parseDouble(s);
    }

    public static double parseTo_double(String s) {
        return parseTo_Double(s);
    }

    public static float parseTo_float(String s) {
        return Float.parseFloat(s);
    }

    public static Float parseTo_Float(String s) {
        if (s == null) {
            return null;
        }
        return parseTo_float(s);
    }

    public static int parseTo_int(String s) {
        return Integer.parseInt(s);
    }

    public static int parseTo_int(String s, boolean isDecode) {
        if (isDecode) {
            return Integer.decode(s).intValue();
        } else {
            return Integer.parseInt(s);
        }
    }

    public static Integer parseTo_Integer(String s) {
        if (s == null) {
            return null;
        }
        return parseTo_int(s);
    }

    public static Integer parseTo_Integer(String s, boolean isDecode) {
        if (s == null) {
            return null;
        }
        return parseTo_int(s, isDecode);
    }

    public static short parseTo_short(String s) {
        return Short.parseShort(s);
    }

    public static short parseTo_short(String s, boolean isDecode) {
        if (isDecode) {
            return Short.decode(s).shortValue();
        } else {
            return Short.parseShort(s);
        }
    }

    public static Short parseTo_Short(String s) {
        if (s == null) {
            return null;
        }
        return parseTo_short(s);
    }

    public static Short parseTo_Short(String s, boolean isDecode) {
        if (s == null) {
            return null;
        }
        return parseTo_short(s, isDecode);
    }

    public static long parseTo_long(String s) {
        return Long.parseLong(s);
    }

    public static long parseTo_long(String s, boolean isDecode) {
        if (isDecode) {
            return Long.decode(s).longValue();
        } else {
            return Long.parseLong(s);
        }
    }

    public static Long parseTo_Long(String s) {
        if (s == null) {
            return null;
        }
        return parseTo_long(s);
    }

    public static Long parseTo_Long(String s, boolean isDecode) {
        if (s == null) {
            return null;
        }
        return parseTo_long(s, isDecode);
    }

    public static Boolean parseTo_Boolean(String s) {
        if (s == null) {
            return null;
        }
        if (s.equals("1")) { //$NON-NLS-1$
            return Boolean.parseBoolean("true"); //$NON-NLS-1$
        }
        return Boolean.parseBoolean(s);
    }

    public static boolean parseTo_boolean(String s) {
        return parseTo_Boolean(s);
    }

    public static String parseTo_String(String s) {
        return s;
    }

    public static String parseTo_String(final List<String> s, String fieldSep) {
        if (s == null) {
            return null;
        }
        StringBuffer result = new StringBuffer();
        result.append("[");
        for (int i = 0; i < s.size(); i++) {
            if (i != 0) {
                result.append(fieldSep);
            }
            result.append(s.get(i));
        }
        result.append("]");

        return result.toString();
    }

    public static BigDecimal parseTo_BigDecimal(String s) {
        if (s == null) {
            return null;
        }
        try {
            return new BigDecimal(s);

        } catch (NumberFormatException nfe) {

            if (nfe.getMessage() == null) {

                throw new NumberFormatException("Incorrect input \"" + s + "\" for BigDecimal.");

            } else {

                throw nfe;
            }
        }
    }

    public static routines.system.Document parseTo_Document(String s) throws org.dom4j.DocumentException {
        return parseTo_Document(s, false);
    }

    public static routines.system.Document parseTo_Document(String s, boolean ignoreDTD) throws org.dom4j.DocumentException {
        return parseTo_Document(s, false, null);
    }

    public static routines.system.Document parseTo_Document(String s, boolean ignoreDTD, String encoding)
            throws org.dom4j.DocumentException {
        if (s == null) {
            return null;
        }
        routines.system.Document theDoc = new routines.system.Document();
        org.dom4j.io.SAXReader reader = new org.dom4j.io.SAXReader();

        if (ignoreDTD) {
            reader.setEntityResolver(new EntityResolver() {

                public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                    return new org.xml.sax.InputSource(new java.io.ByteArrayInputStream(
                            "<?xml version='1.0' encoding='UTF-8'?>".getBytes()));
                }
            });
        }

        org.dom4j.Document document = reader.read(new java.io.StringReader(s));
        if (encoding != null && !("".equals(encoding))) {
            document.setXMLEncoding(encoding);
        }
        theDoc.setDocument(document);
        return theDoc;
    }

    /**
     * parse epoch time to {@link java.util.Date}
     */
    public static java.util.Date parseTo_Date(Long epoch) throws NumberFormatException {
        return new java.util.Date(epoch * 1000);
    }

    public static java.util.Date parseTo_Date(String epoch) {
        try{
            return parseTo_Date(Long.parseLong(epoch));
        } catch (NumberFormatException e) {
            Double epochDouble = Double.parseDouble(epoch);
            if (epochDouble.doubleValue() == epochDouble.longValue()) {
                return parseTo_Date(epochDouble.longValue());
            }
            throw e;
        }
    }

    /**
     * convert a date in String format to a {@link java.util.Date}
     *
     * @param dateString could be common representation like "2007-09-13"
     * @param pattern
     * @return
     */
    public static java.util.Date parseTo_Date(String dateString, String pattern) {
        // check the parameter for supporting " ","2007-09-13"," 2007-09-13 "
        if (dateString == null || dateString.length() == 0) {
            return null;
        }
        dateString = dateString.trim();
        if (pattern == null) {
            pattern = Constant.dateDefaultPattern;
        }
        java.util.Date date = null;
        // add by wliu for special pattern:yyyy-MM-dd'T'HH:mm:ss'000Z'---------start
        if (pattern.equals("yyyy-MM-dd'T'HH:mm:ss'000Z'")) {
            if (!dateString.endsWith("000Z")) {
                throw new RuntimeException("Unparseable date: \"" + dateString + "\"");
            }
            pattern = "yyyy-MM-dd'T'HH:mm:ss";
            dateString = dateString.substring(0, dateString.lastIndexOf("000Z"));
        }
        // add by wliu -------------------------------------------------------end
        DateFormat format = FastDateParser.getInstance(pattern);
        ParsePosition pp = new ParsePosition(0);
        pp.setIndex(0);

        date = format.parse(dateString, pp);
        if (pp.getIndex() != dateString.length() || date == null) {
            throw new RuntimeException("Unparseable date: \"" + dateString + "\"");
        }

        return date;
    }

    public static java.util.Date parseTo_Date(String s, String pattern, boolean lenient) {
        // check the parameter for supporting " ","2007-09-13"," 2007-09-13 "
        if (s != null) {
            s = s.trim();
        }
        if (s == null || s.length() == 0) {
            return null;
        }
        if (pattern == null) {
            pattern = Constant.dateDefaultPattern;
        }
        java.util.Date date = null;
        // try {
        // date = FastDateParser.getInstance(pattern).parse(s);
        // } catch (java.text.ParseException e) {
        // e.printStackTrace();
        // System.err.println("Current string to parse '" + s + "'");
        // }
        // add by wliu for special pattern:yyyy-MM-dd'T'HH:mm:ss'000Z'---------start
        if (pattern.equals("yyyy-MM-dd'T'HH:mm:ss'000Z'")) {
            if (!s.endsWith("000Z")) {
                throw new RuntimeException("Unparseable date: \"" + s + "\""); //$NON-NLS-1$ //$NON-NLS-2$
            }
            pattern = "yyyy-MM-dd'T'HH:mm:ss";
            s = s.substring(0, s.lastIndexOf("000Z"));
        }
        // add by wliu -------------------------------------------------------end
        DateFormat format = FastDateParser.getInstance(pattern, lenient);
        ParsePosition pp = new ParsePosition(0);
        pp.setIndex(0);

        date = format.parse(s, pp);
        if (pp.getIndex() != s.length() || date == null) {
            throw new RuntimeException("Unparseable date: \"" + s + "\""); //$NON-NLS-1$ //$NON-NLS-2$
        }

        return date;
    }

    public static java.util.Date parseTo_Date(java.util.Date date, String pattern) {
        // java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat(pattern);
        // java.util.Date date = null;
        // try {
        // date = simpleDateFormat.parse(date);
        // } catch (java.text.ParseException e) {
        // e.printStackTrace();
        // System.err.println("Current string to parse '" + s + "'");
        // }
        return date;
    }

    /**
     * in order to transform the string "1.234.567,89" to number 1234567.89
     */
    public static String parseTo_Number(String s, Character thousandsSeparator, Character decimalSeparator) {
        if (s == null) {
            return null;
        }
        String result = s;
        if (thousandsSeparator != null) {
            result = StringUtils.deleteChar(s, thousandsSeparator);
        }
        if (decimalSeparator != null) {
            result = result.replace(decimalSeparator, '.');
        }
        return result;
    }

    private static final Set<String> primitiveType = new HashSet<String>();

    private static final Map<String, String> primitiveTypeToDefaultValueMap = new HashMap<String, String>();

    static {
        primitiveType.add("boolean");
        primitiveType.add("int");
        primitiveType.add("byte");
        primitiveType.add("char");
        primitiveType.add("double");
        primitiveType.add("float");
        primitiveType.add("long");
        primitiveType.add("short");

        primitiveTypeToDefaultValueMap.put("boolean", "false");
        primitiveTypeToDefaultValueMap.put("int", "0");
        primitiveTypeToDefaultValueMap.put("byte", "0");
        primitiveTypeToDefaultValueMap.put("char", " ");
        primitiveTypeToDefaultValueMap.put("double", "0");
        primitiveTypeToDefaultValueMap.put("float", "0");
        primitiveTypeToDefaultValueMap.put("long", "0");
        primitiveTypeToDefaultValueMap.put("short", "0");
    }

    public static Object parse(String text, String javaType, String pattern) {
        if ("String".equals(javaType) || "Object".equals(javaType)) {
            return text;
        }

        if (text == null || text.length() == 0) {
            boolean isPrimitiveType = primitiveType.contains(javaType);
            if (!isPrimitiveType) {
                return null;
            } else {
                text = primitiveTypeToDefaultValueMap.get(javaType);
            }
        } else {
            if ("java.util.Date".equals(javaType)) {
                return ParserUtils.parseTo_Date(text, pattern);
            }

            if ("byte[]".equals(javaType)) {
                return text.getBytes();
            }
        }

        try {
            Method method = ParserUtils.class.getMethod("parseTo_" + javaType, String.class);
            return method.invoke(null, text);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
