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
package org.talend.commons.utils;

import static org.junit.Assert.*;

import org.junit.Test;
import org.talend.utils.ProductVersion;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public class VersionUtilsTest {

    /**
     * Test method for {@link org.talend.commons.utils.VersionUtils#getTalendVersion()}.
     */
    @Test
    public void testGetTalendVersion() {
        ProductVersion talendVersion = ProductVersion.fromString(VersionUtils.getTalendVersion());
        ProductVersion studioVersion = ProductVersion.fromString(VersionUtils.getDisplayVersion());
        assertEquals(studioVersion, talendVersion);
    }
}
