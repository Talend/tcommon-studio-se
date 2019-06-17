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
package org.talend.designer.maven.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MavenVersionHelperTest {

    @Test
    public void testCompareTo() {
        assertTrue(MavenVersionHelper.compareTo("1.0.0", "1.0.1") < 0);
        assertTrue(MavenVersionHelper.compareTo("1.0.0", "1.1.0") < 0);
        assertTrue(MavenVersionHelper.compareTo("1.0.0", "1.1.1") < 0);

        assertTrue(MavenVersionHelper.compareTo("1.0.0", "0.1.0") > 0);
        assertTrue(MavenVersionHelper.compareTo("1.0.0", "0.0.1") > 0);
        assertTrue(MavenVersionHelper.compareTo("1.0.0", "0.1.1") > 0);

        assertTrue(MavenVersionHelper.compareTo("1.0.0-SNAPSHOT", "1.0.1") < 0);
        assertTrue(MavenVersionHelper.compareTo("1.0.0-SNAPSHOT", "1.0.1-SNAPSHOT") < 0);

    }

    @Test
    public void testGetJarOriginalVersion() {
        // slf4j-log4j12-1.7.10.jar
        String jarName = "slf4j-log4j12-1.7.10.jar";
        String result = "1.7.10";
        assertEquals(MavenVersionHelper.getJarOriginalVersion(jarName), result);
    }

}
