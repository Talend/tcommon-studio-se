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
package org.talend.utils.thread;


import org.junit.Assert;
import org.junit.Test;

/**
 * DOC hwang  class global comment. Detailled comment
 */
public class TimeoutManagerTest {

    @Test
    public void testGetSocketTimeout() {
        System.setProperty(TimeoutManager.SOCKET_TIMEOUT, null);
        Assert.assertNull(TimeoutManager.getSocketTimeout());
        
        System.setProperty(TimeoutManager.SOCKET_TIMEOUT, "aa");
        Assert.assertNull(TimeoutManager.getSocketTimeout());
        
        System.setProperty(TimeoutManager.SOCKET_TIMEOUT, "50");
        Assert.assertTrue(TimeoutManager.getSocketTimeout() == 50);
        
        System.setProperty(TimeoutManager.SOCKET_TIMEOUT, "");
        Assert.assertNull(TimeoutManager.getSocketTimeout());
    }
    
    @Test
    public void testGetConnectionTimeout() {
        System.setProperty(TimeoutManager.CONNECTION_TIMEOUT, null);
        Assert.assertNull(TimeoutManager.getConnectionTimeout());
        
        System.setProperty(TimeoutManager.CONNECTION_TIMEOUT, "aa");
        Assert.assertNull(TimeoutManager.getConnectionTimeout());
        
        System.setProperty(TimeoutManager.CONNECTION_TIMEOUT, "50");
        Assert.assertTrue(TimeoutManager.getConnectionTimeout() == 50);
        
        System.setProperty(TimeoutManager.CONNECTION_TIMEOUT, "");
        Assert.assertNull(TimeoutManager.getConnectionTimeout());
    }
}
