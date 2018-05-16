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
package org.talend.commons.utils.database;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.talend.commons.i18n.internal.Messages;
import org.talend.commons.utils.TalendDBUtils;
import org.talend.fakejdbc.FakeResultSet;

/**
 * created by xqliu on Oct 26, 2012 Detailled comment
 * 
 */
public class SybaseResultSet extends FakeResultSet {

    private String[] tableMeta = null;

    private List<String[]> data;

    int index = -1;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.utils.database.FakeResultSet#next()
     */
    @Override
    public boolean next() throws SQLException {
        if (data == null || data.size() == 0 || index >= data.size() - 1) {
            return false;
        }
        index++;
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.utils.database.FakeResultSet#getString(java.lang.String)
     */
    @Override
    public String getString(String columnLabel) throws SQLException {
        int columnIndex = ArrayUtils.indexOf(tableMeta, columnLabel);

        if (columnIndex == -1) {
            throw new SQLException(Messages.getString("SybaseResultSet.unknowCloumn") + columnLabel); //$NON-NLS-1$
        }

        return getString(columnIndex + 1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.utils.database.FakeResultSet#getInt(java.lang.String)
     */
    @Override
    public int getInt(String columnLabel) throws SQLException {
        int value = 0;
        String str = getString(columnLabel);
        if (columnLabel.equals("TYPE_NAME")) { //$NON-NLS-1$
            value = TalendDBUtils.convertToJDBCType(str);
        } else if (columnLabel.equals("IS_NULLABLE")) { //$NON-NLS-1$
            if (str.equals("N")) { //$NON-NLS-1$
                value = 1;
            } else {
                value = 0;
            }
        } else {
            value = Integer.parseInt(str);
        }
        return value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.utils.database.FakeResultSet#getBoolean(java.lang.String)
     */
    @Override
    public boolean getBoolean(String columnLabel) throws SQLException {
        String str = getString(columnLabel);
        return Boolean.parseBoolean(str);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.utils.database.FakeResultSet#getString(int)
     */
    @Override
    public String getString(int columnIndex) throws SQLException {
        String[] row = data.get(index);
        columnIndex--;

        if (columnIndex < 0 || columnIndex > row.length) {
            throw new SQLException(
                    Messages.getString("SybaseResultSet.parameterIndex") + columnIndex + Messages.getString("SybaseResultSet.outofRange")); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return row[columnIndex];
    }

    /**
     * DOC bqian Comment method "setMetadata".
     * 
     * @param table_meta
     */
    public void setMetadata(String[] tableMeta) {
        this.tableMeta = tableMeta;
    }

    /**
     * DOC bqian Comment method "setData".
     * 
     * @param tables
     */
    public void setData(List<String[]> data) {
        this.data = data;
    }
}
