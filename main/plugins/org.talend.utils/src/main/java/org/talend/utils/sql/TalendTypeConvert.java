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
package org.talend.utils.sql;

import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * DOC qiongli class global comment. Detailled comment
 */
public final class TalendTypeConvert {

    private static Map<String, Integer> map;

    private static String idStr = "id_";

    private TalendTypeConvert() {

    }

    private static Map<String, Integer> getMap() {
        if (map == null) {
            map = new HashMap<String, Integer>();
            map.put(talendTypeName(Boolean.class), Types.BOOLEAN);
            map.put(talendTypeName(Byte.class), Types.DECIMAL);
            map.put(talendTypeName(Character.class), Types.CHAR);
            map.put(talendTypeName(Date.class), Types.DATE);
            map.put(talendTypeName(String.class), Types.CLOB);
            map.put(talendTypeName(Double.class), Types.DOUBLE);
            map.put(talendTypeName(Float.class), Types.FLOAT);
            map.put(talendTypeName(Integer.class), Types.INTEGER);
            map.put(talendTypeName(Long.class), Types.INTEGER);
            map.put(talendTypeName(Short.class), Types.SMALLINT);
        }
        return map;
    }

    private static String talendTypeName(Class<?> nullableClass) {
        return idStr + nullableClass.getSimpleName();
    }

    /**
     * 
     * convert talendType to jdbcType.
     * 
     * @param xsdDataType
     * @return
     */
    public static int convertToJDBCType(String talendType) {
        Integer type = getMap().get(talendType);
        return null == type ? 0 : type;
    }

    /**
     * 
     * DOC qiongli Comment method "convertToObject".
     * 
     * @param talendType
     * @param value
     * @param datePattern:just for date type then parse with the given pattern(format).
     * @return
     */
    public static Object convertToObject(String talendType, String value, String datePattern) {
        Object object = null;
        // bug 19036 .remove the epmty string, '\r','\n'.
        value = value.trim();
        value = StringUtils.remove(value, "\r");
        value = StringUtils.remove(value, "\n");

        try {
            if (talendType.equals(talendTypeName(Boolean.class))) {
                object = Boolean.valueOf(value).booleanValue();
            } else if (talendType.equals(talendTypeName(Byte.class))) {
                object = Byte.valueOf(value).byteValue();
            } else if (talendType.equals(talendTypeName(Date.class))) {
                // MOD qiongli 2011-10-27 TDQ-3802.
                if (datePattern == null || StringUtils.EMPTY.equals(datePattern.trim())) {
                    datePattern = "yyyy-MM-dd";
                } else {
                    datePattern = StringUtils.replace(datePattern, "\"", StringUtils.EMPTY);
                }
                // add the parameter 'Locale.US' because make like 'AM/PM' could be parsed.then format and output as
                // the given 'datePattern'
                SimpleDateFormat sdf = new SimpleDateFormat(datePattern, Locale.US);
                object = sdf.parse(value);
            } else if (talendType.equals(talendTypeName(Double.class))) {
                object = Double.parseDouble(value);
            } else if (talendType.equals(talendTypeName(Float.class))) {
                object = Float.parseFloat(value);
            } else if (talendType.equals(talendTypeName(Integer.class))) {
                object = Integer.parseInt(value);
            } else if (talendType.equals(talendTypeName(Long.class))) {
                object = Long.parseLong(value);
            } else if (talendType.equals(talendTypeName(Short.class))) {
                object = Short.parseShort(value);
            } else if (talendType.equals(talendTypeName(String.class)) || talendType.equals(talendTypeName(Character.class))) {
                object = value;
            }
        } catch (ClassCastException ce) {
            return null;
        } catch (Exception e) {
            return null;
        }
        return object;
    }

    /**
     * 
     * Convert the TalendType to Java type.
     * 
     * @param talendType
     * @return
     */
    public static String convertToJavaType(String talendType) {
        if (talendType == null) {
            return "";
        }
        talendType = StringUtils.remove(talendType, idStr);
        return talendType;
    }
}
