// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.commons.utils.database;

import java.sql.SQLException;
import java.util.List;

import org.talend.commons.i18n.internal.Messages;
import org.talend.commons.utils.TalendDBUtils;
import org.talend.fakejdbc.FakeResultSet;

public class EXASOLResultSet extends FakeResultSet {

    private String[] tableMeta = null;

    private List<String[]> data;

    int index = -1;

    @Override
    public boolean next() throws SQLException {
        if (data == null || data.size() == 0 || index >= data.size() - 1) {
            return false;
        }
        index++;
        return true;
    }

    public static int indexOf(String string, String[] search) {
        for (int i = 0; i < search.length; i++) {
            if (search[i].equals(string)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String getString(String columnLabel) throws SQLException {
        int columnIndex = indexOf(columnLabel, tableMeta);

        if (columnIndex == -1) {
            throw new SQLException(Messages.getString("DB2ForZosResultSet.unknowCloumn") + columnLabel); //$NON-NLS-1$
        }

        return getString(columnIndex + 1);
    }

    @Override
    public int getInt(String columnLabel) throws SQLException {
        String str = getString(columnLabel);
        if (columnLabel.equals("TYPE_NAME")) {
            int index = TalendDBUtils.convertToJDBCType(str);
            return index;
        } else if (columnLabel.equals("IS_NULLABLE")) {
            if (str.equals("N")) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return Integer.parseInt(str);
        }
    }

    @Override
    public boolean getBoolean(String columnLabel) throws SQLException {
        String str = getString(columnLabel);
        return Boolean.parseBoolean(str);
    }

    @Override
    public String getString(int columnIndex) throws SQLException {
        String[] row = data.get(index);
        columnIndex--;

        if (columnIndex < 0 || columnIndex > row.length) {
            throw new SQLException(
                    Messages.getString("DB2ForZosResultSet.parameterIndex") + columnIndex + Messages.getString("DB2ForZosResultSet.outofRange")); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return row[columnIndex];
    }

    public void setMetadata(String[] tableMeta) {
        this.tableMeta = tableMeta;

    }

    public void setData(List<String[]> data) {
        this.data = data;
    }

}