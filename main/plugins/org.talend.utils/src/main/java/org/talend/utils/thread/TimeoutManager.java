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


/**
 * DOC hwang  class global comment. Detailled comment
 */
public class TimeoutManager {
    
    public static final String SOCKET_TIMEOUT = "http.socket.timeout";//$NON-NLS-1$

    public static final String CONNECTION_TIMEOUT = "http.connection.timeout";//$NON-NLS-1$
    
    public static Integer getSocketTimeout() {
        String so_timeOut = System.getProperty(TimeoutManager.SOCKET_TIMEOUT);
        if(so_timeOut == null || "".equals(so_timeOut)) {
            return null;
        }
        try {
            return Integer.parseInt(so_timeOut);
        } catch (NumberFormatException e) {
            
        }
        return null;
    }
    
    public static Integer getConnectionTimeout() {
        String co_timeOut = System.getProperty(TimeoutManager.CONNECTION_TIMEOUT);
        if(co_timeOut == null || "".equals(co_timeOut)) {
            return null;
        }
        try {
            return Integer.parseInt(co_timeOut);
        } catch (NumberFormatException e) {
            
        }
        return null;
    }
    
}
