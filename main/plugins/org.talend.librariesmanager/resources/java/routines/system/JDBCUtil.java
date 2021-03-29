// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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

import java.sql.ResultSet;
import java.util.Date;

public class JDBCUtil {

    public static String getString(ResultSet rs, int index, boolean trim) throws java.sql.SQLException {
        String result = rs.getString(index);
        if (trim && result != null) {
            return result.trim();
        }
        return result;
    }

    public static Double getDoubleObject(ResultSet rs, int index) throws java.sql.SQLException {
        if (rs.getObject(index) != null) {
            return rs.getDouble(index);
        }

        return null;
    }

    public static Boolean getBooleanObject(ResultSet rs, int index) throws java.sql.SQLException {
        if (rs.getObject(index) != null) {
            return rs.getBoolean(index);
        }

        return null;
    }

    public static double getDouble(ResultSet rs, int index) throws java.sql.SQLException {
        if (rs.getObject(index) != null) {
            return rs.getDouble(index);
        }

        throw new RuntimeException("Null value in non-Nullable column");
    }

    public static boolean getBoolean(ResultSet rs, int index) throws java.sql.SQLException {
        if (rs.getObject(index) != null) {
            return rs.getBoolean(index);
        }

        throw new RuntimeException("Null value in non-Nullable column");
    }

    /**
     * getDate can be called with the resultSet having either a java.sql.Timestamp or a java.sql.Date
     * the double try implementation is not something im proud of, but having two methods would require 
     * a huge refactoring that in the end is the same as doing that
     * @param rs
     * @param index
     * @return java.util.Date converted from java.sql.Timestamp/Date
     * @throws java.sql.SQLException
     */
    public static Date getDate(ResultSet rs, int index) throws java.sql.SQLException {
        Date result = null;
        try {
            if(rs.getTimestamp(index) != null) {
                result = new Date(rs.getTimestamp(index).getTime());
                return result;
            }
        } catch (java.sql.SQLException e) {
        }
        try {
            if(rs.getDate(index) != null) {
                result = new Date(rs.getDate(index).getTime());
                return result;
            }
        } catch (java.sql.SQLException e) {
        }
        return result;
    }

    //decrease the get method call number
    public static Double getDoubleObject2(ResultSet rs, int index) throws java.sql.SQLException {
        Double result = rs.getDouble(index);
        if(result == 0 && rs.getObject(index) == null){
            return null;
        }
        return result;
    }

    //decrease the get method call number
    public static double getDouble2(ResultSet rs, int index) throws java.sql.SQLException {
        Double result = rs.getDouble(index);
        if(result == 0 && rs.getObject(index) == null){
            throw new RuntimeException("Null value in non-Nullable column");
        }
        return result;
    }

}
