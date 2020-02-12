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
package org.talend.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/*
* Created by bhe on Dec 24, 2019
*/
public class JavaVersionTest {

    @Test
    public void testCompare() throws Exception {

        JavaVersion v1 = new JavaVersion("1.7.0_80");
        JavaVersion v2 = new JavaVersion("1.8.0_151");
        JavaVersion v3 = new JavaVersion("1.8.0_161");
        JavaVersion v4 = new JavaVersion("1.8.0_211");
        JavaVersion v5 = new JavaVersion("12");
        JavaVersion v6 = new JavaVersion("1.8.0_161");
        JavaVersion v7 = new JavaVersion("1.8.0_212-8u");
        JavaVersion v8 = new JavaVersion("1.8.0_99-7c");

        assertTrue(v1.compareTo(v2) < 0);
        assertTrue(v2.compareTo(v3) < 0);
        assertTrue(v3.compareTo(v4) < 0);
        assertTrue(v4.compareTo(v5) < 0);
        assertTrue(v3.compareTo(v6) == 0);
        assertTrue(v7.compareTo(v4) > 0);
        assertTrue(v7.compareTo(v5) < 0);
        assertTrue(v7.compareTo(v8) > 0);

        assertTrue(v3.equals(v6));
        assertTrue(v3.hashCode() == v6.hashCode());

        assertFalse(v3.equals(v4));
    }

}
