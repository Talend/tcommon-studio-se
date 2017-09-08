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

import org.junit.Assert;
import org.junit.Test;

/**
 * created by xqliu on Jul 10, 2013 Detailled comment
 * 
 */
public class Java2SqlTypeTest {

    /**
     * Test method for {@link org.talend.utils.sql.Java2SqlType#getJavaTypeBySqlType(java.lang.String)}.
     */
    @Test
    public void testGetJavaTypeBySqlType() {
        String DATE = "DATE"; //$NON-NLS-1$
        String BIGINT = "BIGINT"; //$NON-NLS-1$
        String INTEGER = "INTEGER"; //$NON-NLS-1$
        String SMALLINT = "SMALLINT"; //$NON-NLS-1$
        String FLOAT = "FLOAT"; //$NON-NLS-1$
        String CHAR = "CHAR"; //$NON-NLS-1$
        String VARCHAR = "VARCHAR"; //$NON-NLS-1$
        String DECIMAL = "DECIMAL"; //$NON-NLS-1$
        String TIME = "TIME"; //$NON-NLS-1$
        String TIMESTMP = "TIMESTMP"; //$NON-NLS-1$
        String TIMESTAMP = "TIMESTAMP"; //$NON-NLS-1$
        String BLOB = "BLOB"; //$NON-NLS-1$
        String CLOB = "CLOB"; //$NON-NLS-1$
        String DISTINCT = "DISTINCT"; //$NON-NLS-1$
        String DOUBLE = "DOUBLE"; //$NON-NLS-1$
        String LONGVAR = "LONGVAR"; //$NON-NLS-1$
        String LONGVARCHAR = "LONGVARCHAR"; //$NON-NLS-1$
        String REAL = "REAL"; //$NON-NLS-1$
        String empty = ""; //$NON-NLS-1$
        String blank = " "; //$NON-NLS-1$
        String str = "skjfkljsdlkf29038409iwejh"; //$NON-NLS-1$
        String LONG = "LONG"; //$NON-NLS-1$
        String nullString = null;

        Assert.assertTrue(Types.DATE == Java2SqlType.getJavaTypeBySqlType(DATE));
        Assert.assertTrue(Types.BIGINT == Java2SqlType.getJavaTypeBySqlType(BIGINT));
        Assert.assertTrue(Types.INTEGER == Java2SqlType.getJavaTypeBySqlType(INTEGER));
        Assert.assertTrue(Types.SMALLINT == Java2SqlType.getJavaTypeBySqlType(SMALLINT));
        Assert.assertTrue(Types.FLOAT == Java2SqlType.getJavaTypeBySqlType(FLOAT));
        Assert.assertTrue(Types.CHAR == Java2SqlType.getJavaTypeBySqlType(CHAR));
        Assert.assertTrue(Types.VARCHAR == Java2SqlType.getJavaTypeBySqlType(VARCHAR));
        Assert.assertTrue(Types.DECIMAL == Java2SqlType.getJavaTypeBySqlType(DECIMAL));
        Assert.assertTrue(Types.TIME == Java2SqlType.getJavaTypeBySqlType(TIME));
        Assert.assertTrue(Types.TIMESTAMP == Java2SqlType.getJavaTypeBySqlType(TIMESTMP));
        Assert.assertTrue(Types.TIMESTAMP == Java2SqlType.getJavaTypeBySqlType(TIMESTAMP));
        Assert.assertTrue(Types.BLOB == Java2SqlType.getJavaTypeBySqlType(BLOB));
        Assert.assertTrue(Types.CLOB == Java2SqlType.getJavaTypeBySqlType(CLOB));
        Assert.assertTrue(Types.DISTINCT == Java2SqlType.getJavaTypeBySqlType(DISTINCT));
        Assert.assertTrue(Types.DOUBLE == Java2SqlType.getJavaTypeBySqlType(DOUBLE));
        Assert.assertTrue(Types.LONGVARCHAR == Java2SqlType.getJavaTypeBySqlType(LONGVAR));
        Assert.assertTrue(Types.LONGVARCHAR == Java2SqlType.getJavaTypeBySqlType(LONGVARCHAR));
        Assert.assertTrue(Types.REAL == Java2SqlType.getJavaTypeBySqlType(REAL));
        Assert.assertTrue(Types.BIGINT == Java2SqlType.getJavaTypeBySqlType(LONG));

        Assert.assertTrue(0 == Java2SqlType.getJavaTypeBySqlType(empty));
        Assert.assertTrue(0 == Java2SqlType.getJavaTypeBySqlType(blank));
        Assert.assertTrue(0 == Java2SqlType.getJavaTypeBySqlType(str));

        try {
            Java2SqlType.getJavaTypeBySqlType(nullString);
            Assert.assertTrue(false);
        } catch (NullPointerException npe) {
            Assert.assertTrue(true);
        }
    }
}
