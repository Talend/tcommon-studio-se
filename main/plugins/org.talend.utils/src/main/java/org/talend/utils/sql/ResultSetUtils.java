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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.talend.utils.format.StringFormatUtil;

/**
 * @author scorreia
 * 
 * Utility class for working with ResultSet.
 */
public final class ResultSetUtils {

    private static final String NULLDATE = "0000-00-00 00:00:00"; //$NON-NLS-1$

    private ResultSetUtils() {
    }

    /**
     * Method "resultSetPrinter" prints the header, the column types and then the rows. Each row is formatted with a
     * fixed column width.
     * 
     * Attention: set.next() is called up to the end of the result set.
     * 
     * @param set a resultSet to print
     * @param width the width of the column for display
     * @throws SQLException
     */
    public static void printResultSet(ResultSet set, int width) throws SQLException {
        // print header

        ResultSetMetaData metaData = set.getMetaData();
        int columnCount = metaData.getColumnCount();
        // find the largest column name
        int minWidth = width;
        for (int i = 1; i <= columnCount; i++) {
            minWidth = Math.max(minWidth, metaData.getColumnName(i).length());
        }
        // print header
        String header = new String();
        for (int i = 1; i <= columnCount; i++) {
            String columnName = StringFormatUtil.padString(metaData.getColumnName(i), minWidth);
            header += columnName;
        }
        System.out.println(header);

        // print column types
        String types = new String();
        for (int i = 1; i <= columnCount; i++) {
            String columnTypeName = StringFormatUtil.padString(metaData.getColumnTypeName(i), minWidth);
            types += columnTypeName;
        }
        System.out.println(types);

        // print rows
        while (set.next()) {
            System.out.println(formatRow(set, columnCount, minWidth));
        }
    }

    /**
     * Method "formatRow".
     * 
     * @param set a result set
     * @param nbColumns the number of columns of the result set
     * @param width the fixed width of each column
     * @return the current row of the result set format with fixed width.
     * @throws SQLException
     */
    public static String formatRow(ResultSet set, int nbColumns, int width) throws SQLException {
        String row = new String();
        for (int i = 1; i <= nbColumns; i++) {
            Object col = set.getObject(i);
            row += StringFormatUtil.padString(col != null ? col.toString() : "", width);
        }
        return row;
    }

    /**
     * 
     * Get Object by special column index
     * 
     * @param set
     * @param columnIndex
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public static Object getBigObject(ResultSet set, int columnIndex) throws SQLException {
        Object object = null;
        try {
            object = set.getObject(columnIndex);
            if (object != null && object instanceof Clob) {
                Reader is = ((Clob) object).getCharacterStream();
                BufferedReader br = new BufferedReader(is);
                String str = br.readLine();
                StringBuffer sb = new StringBuffer();
                while (str != null) {
                    sb.append(str);
                    str = br.readLine();
                }
                return sb.toString();
            }
        } catch (SQLException e) {
            if (NULLDATE.equals(set.getString(columnIndex))) {
                object = null;
            } else {
                throw e;
            }

        } catch (IOException e) {
            object = null;
        }
        return object;
    }

    /**
     * 
     * Get Object by special column index
     * 
     * @param set
     * @param columnIndex
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public static Object getBigObject(ResultSet set, String columnName) throws SQLException {
        Object object = null;
        try {
            object = set.getObject(columnName);
            if (object != null && object instanceof Clob) {
                Reader is = ((Clob) object).getCharacterStream();
                BufferedReader br = new BufferedReader(is);
                String str = br.readLine();
                StringBuffer sb = new StringBuffer();
                while (str != null) {
                    sb.append(str);
                    str = br.readLine();
                }
                return sb.toString();
            }
        } catch (SQLException e) {
            if (NULLDATE.equals(set.getString(columnName))) {
                object = null;
            } else {
                throw e;
            }

        } catch (IOException e) {
            object = null;
        }
        return object;
    }

    /**
     * 
     * Get Object by special column index
     * 
     * @param set
     * @param columnIndex
     * @return
     * @throws SQLException
     */
    public static Object getObject(ResultSet set, int columnIndex) throws SQLException {
        Object object = null;
        try {
            object = set.getObject(columnIndex);
        } catch (SQLException e) {
            if (NULLDATE.equals(set.getString(columnIndex))) {
                object = null;
            } else {
                throw e;
            }

        }
        return object;
    }

    /**
     * 
     * Get Object by special column name
     * 
     * @param set
     * @param columnName
     * @return
     * @throws SQLException
     */
    public static Object getObject(ResultSet set, String columnName) throws SQLException {
        Object object = null;
        try {
            object = set.getObject(columnName);
        } catch (SQLException e) {
            if (NULLDATE.equals(set.getString(columnName))) {
                object = null;
            } else {
                throw e;
            }

        }
        return object;
    }

}
