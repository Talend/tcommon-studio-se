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
package org.talend.commons.utils.database;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * created by xqliu on Jul 23, 2013 Detailled comment
 * 
 */
public class SybaseResultSetTest {

    /**
     * Test method for {@link org.talend.commons.utils.database.SybaseResultSet#getInt(java.lang.String)} .
     * 
     * @throws SQLException
     */
    @Test
    public void testGetInt() throws SQLException {
        String col1 = "TYPE_NAME"; //$NON-NLS-1$
        String col2 = "IS_NULLABLE"; //$NON-NLS-1$
        String col3 = "name"; //$NON-NLS-1$
        String col4 = "age"; //$NON-NLS-1$
        String[] tableMeta = { col1, col2, col3, col4 };

        List<String[]> data = new ArrayList<String[]>();
        data.add(new String[] { "DECIMAL", "N", "aaa", "7" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        data.add(new String[] { "CHAR", "Y", "bbb", "6" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        SybaseResultSet sybaseResultSet = new SybaseResultSet();
        sybaseResultSet.setMetadata(tableMeta);
        sybaseResultSet.setData(data);

        int i = 0;
        while (sybaseResultSet.next()) {
            switch (i) {
            case 0:
                assertTrue(sybaseResultSet.getInt(col1) == Types.DECIMAL);
                assertTrue(sybaseResultSet.getInt(col2) == 1);
                try {
                    sybaseResultSet.getInt(col3);
                    assertTrue(Boolean.FALSE);
                } catch (Exception e) {
                    assertTrue(Boolean.TRUE);
                }
                assertTrue(sybaseResultSet.getInt(col4) == 7);
                break;
            case 1:
                assertTrue(sybaseResultSet.getInt(col1) == Types.CHAR);
                assertTrue(sybaseResultSet.getInt(col2) == 0);
                try {
                    sybaseResultSet.getInt(col3);
                    assertTrue(Boolean.FALSE);
                } catch (Exception e) {
                    assertTrue(Boolean.TRUE);
                }
                assertTrue(sybaseResultSet.getInt(col4) == 6);
                break;
            }
            i++;
        }
    }
}
