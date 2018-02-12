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

import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * DOC guanglong.du class global comment. Detailled comment
 */
public final class TalendDBUtils {

    private static Map<String, Integer> dbMap;

    private TalendDBUtils() {

    }

    public static Map<String, Integer> getTalendDBMap() {
        if (dbMap == null) {
            dbMap = new HashMap<String, Integer>();
            dbMap.put(talendDBTypeName(Boolean.class), Types.BOOLEAN);
            dbMap.put(talendDBTypeName(Byte.class), Types.DECIMAL);
            dbMap.put(talendDBTypeName(Character.class), Types.CHAR);
            dbMap.put(talendDBTypeName(Date.class), Types.DATE);
            dbMap.put(talendDBTypeName(String.class), Types.CLOB);
            dbMap.put(talendDBTypeName(Double.class), Types.DOUBLE);
            dbMap.put(talendDBTypeName(Float.class), Types.FLOAT);
            dbMap.put(talendDBTypeName(Integer.class), Types.INTEGER);
            dbMap.put(talendDBTypeName(Long.class), Types.INTEGER);
            dbMap.put(talendDBTypeName(Short.class), Types.SMALLINT);
            dbMap.put("DECIMAL", Types.DECIMAL);
            dbMap.put("VARCHAR", Types.VARCHAR);
            dbMap.put("TIMESTAMP", Types.TIMESTAMP);
            dbMap.put("BINARY", Types.BINARY);
            dbMap.put("REF", Types.REF);
            dbMap.put("DATALINK", Types.DATALINK);
            dbMap.put("BIGINT", Types.BIGINT);
            dbMap.put("CHAR", Types.CHAR);
        }
        return dbMap;
    }

    private static String talendDBTypeName(Class<?> nullableClass) {
        return nullableClass.getSimpleName().toUpperCase();
    }

    public static int convertToJDBCType(String talendType) {
        Integer type = getTalendDBMap().get(talendType.trim());
        return null == type ? 0 : type;
    }
}
