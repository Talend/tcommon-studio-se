// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.model.process;

import java.util.LinkedHashMap;

/**
 * created by wchen on 2014-1-7 Detailled comment TraceDataBean for UI
 */
public class TraceData {

    private static final long serialVersionUID = -4580437449518099406L;

    private String connectionId;

    private int nbLine;

    private LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();

    public String getConnectionId() {
        return this.connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public int getNbLine() {
        return this.nbLine;
    }

    public void setNbLine(int nbLine) {
        this.nbLine = nbLine;
    }

    public LinkedHashMap<String, String> getData() {
        return this.data;
    }

    public void setData(LinkedHashMap<String, String> data) {
        this.data = data;
    }

}
