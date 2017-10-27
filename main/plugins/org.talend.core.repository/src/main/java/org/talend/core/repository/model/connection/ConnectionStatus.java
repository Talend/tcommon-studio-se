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
package org.talend.core.repository.model.connection;

/**
 * Object who is traduce the result of the Connection Test. <br/>
 * 
 * $Id: ConnectionStatus.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public class ConnectionStatus {

    private boolean result;

    private String messageException;

    public boolean getResult() {
        return this.result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessageException() {
        return this.messageException;
    }

    public void setMessageException(String messageException) {
        this.messageException = messageException;
    }
}
