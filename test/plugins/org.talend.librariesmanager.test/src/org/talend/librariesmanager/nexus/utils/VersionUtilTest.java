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
package org.talend.librariesmanager.nexus.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * DOC hwang  class global comment. Detailled comment
 */
public class VersionUtilTest {

    @Test
    public void testGetSNAPSHOTVersion() {
        VersionUtil util = new VersionUtil();
        String rVersion = "6.0.0-20191015.030844-1";
        String result = util.getSNAPSHOTVersion(rVersion);
        Assert.assertEquals(result, "6.0.0-SNAPSHOT");
        
        rVersion = null;
        result = util.getSNAPSHOTVersion(rVersion);
        Assert.assertNull(result);
        
        rVersion = "6.0.0";
        result = util.getSNAPSHOTVersion(rVersion);
        Assert.assertEquals(result, "6.0.0");
    }
}
