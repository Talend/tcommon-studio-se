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
package org.talend.utils.string;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

/**
 * DOC xqliu class global comment. Detailed comment
 */
public class StringUtilitiesRealTest {

    /**
     * Test method for {@link org.talend.utils.string.StringUtilities#getRandomString(java.lang.Integer)}
     */
    @Test
    public void testGetRandomString() {
        int strLen = 6;
        int strSize = 100;
        Set<String> strs = new HashSet<String>();
        for (int i = 0; i < strSize; ++i) {
            String str = StringUtilities.getRandomString(strLen);
            assertEquals(str.length(), strLen);
            strs.add(str);
        }
        assertEquals(strs.size(), strSize);
    }
}
