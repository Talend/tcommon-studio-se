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
package org.talend.core.model.general;

/**
 * DOC qwei class global comment. Detailled comment <br/>
 * 
 */
public class InstallModule {

    private String osName;

    private String commandName;

    public InstallModule(String osName, String commandName) {
        super();
        this.osName = osName;
        this.commandName = commandName;
    }

    public String getosName() {
        return this.osName;
    }

    public void setosName(String osName) {
        this.osName = osName;
    }

    public String getcommandName() {
        return this.commandName;
    }

    public void setcommandName(String commandName) {
        this.commandName = commandName;
    }

}
